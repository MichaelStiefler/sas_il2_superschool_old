package com.maddox.il2.objects.weapons;

import java.io.IOException;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeX4Carrier;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgSpawn;
import com.maddox.rts.NetObj;
import com.maddox.rts.NetSpawn;
import com.maddox.rts.NetUpdate;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public class RocketJetManX4Mini extends RocketX4 {
	static class SPAWN implements NetSpawn {

		public void netSpawn(int i, NetMsgInput netmsginput) {
			NetObj netobj;
			netobj = netmsginput.readNetObj();
			if (netobj == null)
				return;
			try {
				Actor actor = (Actor)netobj.superObj();
				Point3d point3d = new Point3d(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
				Orient orient = new Orient(netmsginput.readFloat(), netmsginput.readFloat(), 0.0F);
				float f = netmsginput.readFloat();
				new RocketJetManX4Mini(actor, netmsginput.channel(), i, point3d, orient, f);
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
			return;
		}

		SPAWN() {
		}
	}

	class Mirror extends ActorNet {

		public void msgNetNewChannel(NetChannel netchannel) {
			if (!Actor.isValid(actor()))
				return;
			if (netchannel.isMirrored(this))
				return;
			try {
				if (netchannel.userState == 0) {
					NetMsgSpawn netmsgspawn = actor().netReplicate(netchannel);
					if (netmsgspawn != null) {
						postTo(netchannel, netmsgspawn);
						actor().netFirstUpdate(netchannel);
					}
				}
			} catch (Exception exception) {
				printDebug(exception);
			}
			return;
		}

		public boolean netInput(NetMsgInput netmsginput) throws IOException {
			if (netmsginput.isGuaranted())
				return false;
			if (isMirrored()) {
				out.unLockAndSet(netmsginput, 0);
				postReal(Message.currentTime(true), out);
			}
			RocketJetManX4Mini.p.x = netmsginput.readFloat();
			RocketJetManX4Mini.p.y = netmsginput.readFloat();
			RocketJetManX4Mini.p.z = netmsginput.readFloat();
			int i = netmsginput.readShort();
			int j = netmsginput.readShort();
			float f = -(((float)i * 180F) / 32000F);
			float f1 = ((float)j * 90F) / 32000F;
			RocketJetManX4Mini.or.set(f, f1, 0.0F);
			pos.setAbs(RocketJetManX4Mini.p, RocketJetManX4Mini.or);
			return true;
		}

		NetMsgFiltered out;

		public Mirror(Actor actor, NetChannel netchannel, int i) {
			super(actor, netchannel, i);
			out = new NetMsgFiltered();
		}
	}

	class Master extends ActorNet implements NetUpdate {

		public void msgNetNewChannel(NetChannel netchannel) {
			if (!Actor.isValid(actor()))
				return;
			if (netchannel.isMirrored(this))
				return;
			try {
				if (netchannel.userState == 0) {
					NetMsgSpawn netmsgspawn = actor().netReplicate(netchannel);
					if (netmsgspawn != null) {
						postTo(netchannel, netmsgspawn);
						actor().netFirstUpdate(netchannel);
					}
				}
			} catch (Exception exception) {
				printDebug(exception);
			}
			return;
		}

		public boolean netInput(NetMsgInput netmsginput) throws IOException {
			return false;
		}

		public void netUpdate() {
			try {
				out.unLockAndClear();
				pos.getAbs(RocketJetManX4Mini.p, RocketJetManX4Mini.or);
				out.writeFloat((float)RocketJetManX4Mini.p.x);
				out.writeFloat((float)RocketJetManX4Mini.p.y);
				out.writeFloat((float)RocketJetManX4Mini.p.z);
				RocketJetManX4Mini.or.wrap();
				int i = (int)((RocketJetManX4Mini.or.getYaw() * 32000F) / 180F);
				int j = (int)((RocketJetManX4Mini.or.tangage() * 32000F) / 90F);
				out.writeShort(i);
				out.writeShort(j);
				post(Time.current(), out);
			} catch (Exception exception) {
				printDebug(exception);
			}
		}

		NetMsgFiltered out;

		public Master(Actor actor) {
			super(actor);
			out = new NetMsgFiltered();
		}
	}

	public boolean interpolateStep() {
		float f = Time.tickLenFs();
		float f1 = (float)getSpeed(null);
		f1 += (320F - f1) * 0.1F * f;
		pos.getAbs(p, or);
		v.set(1.0D, 0.0D, 0.0D);
		or.transform(v);
		v.scale(f1);
		setSpeed(v);
		p.x += v.x * (double)f;
		p.y += v.y * (double)f;
		p.z += v.z * (double)f;
		if (isNet() && isNetMirror()) {
			pos.setAbs(p, or);
			return false;
		}
		if (Actor.isValid(getOwner())) {
			if ((getOwner() != World.getPlayerAircraft() || !((RealFlightModel)fm).isRealMode()) && (fm instanceof Pilot)) {
				Pilot pilot = (Pilot)fm;
				if (pilot.target != null) {
					pilot.target.Loc.get(pT);
					pT.sub(p);
					or.transformInv(pT);
					if (pT.x > -10D) {
						double d = Aircraft.cvt(fm.Skill, 0.0F, 3F, 15F, 0.0F);
						if (pT.y > d)
							((TypeX4Carrier)fm.actor).typeX4CAdjSideMinus();
						if (pT.y < -d)
							((TypeX4Carrier)fm.actor).typeX4CAdjSidePlus();
						if (pT.z < -d)
							((TypeX4Carrier)fm.actor).typeX4CAdjAttitudeMinus();
						if (pT.z > d)
							((TypeX4Carrier)fm.actor).typeX4CAdjAttitudePlus();
					}
				}
			}
			or.increment(50F * f * ((TypeX4Carrier)fm.actor).typeX4CgetdeltaAzimuth(),
					50F * f * ((TypeX4Carrier)fm.actor).typeX4CgetdeltaTangage(), 0.0F);
			or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
			((TypeX4Carrier)fm.actor).typeX4CResetControls();
		}
		pos.setAbs(p, or);
		if (Time.current() > tStart + 500L) {
			Actor actor = NearestTargets.getEnemy(0, -1, p, 800D, 0);
			if (Actor.isValid(actor)) {
				float f2 = (float)p.distance(actor.pos.getAbsPoint());
				if ((actor instanceof Aircraft) && (f2 < 20F || f2 < 40F && f2 > prevd && prevd != 1000F)) {
					doExplosionAir();
					postDestroy();
					collide(false);
					drawing(false);
				}
				prevd = f2;
			} else {
				prevd = 1000F;
			}
		}
		if (!Actor.isValid(getOwner()) || !(getOwner() instanceof Aircraft)) {
			doExplosionAir();
			postDestroy();
			collide(false);
			drawing(false);
			return false;
		} else {
			return false;
		}
	}

	public RocketJetManX4Mini() {
		fm = null;
		tStart = 0L;
		prevd = 1000F;
	}

	public RocketJetManX4Mini(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		fm = null;
		tStart = 0L;
		prevd = 1000F;
		net = new Mirror(this, netchannel, i);
		pos.setAbs(point3d, orient);
		pos.reset();
		pos.setBase(actor, null, true);
		doStart(-1F);
		v.set(1.0D, 0.0D, 0.0D);
		orient.transform(v);
		v.scale(f);
		setSpeed(v);
		collide(false);
	}

	public void start(float f, int i) {
		Actor actor = pos.base();
		if (Actor.isValid(actor) && (actor instanceof Aircraft)) {
			if (actor.isNetMirror()) {
				destroy();
				return;
			}
			net = new Master(this);
		}
		doStart(f);
	}

	private void doStart(float f) {
		super.start(-1F, 0);
		fm = ((Aircraft)getOwner()).FM;
		tStart = Time.current();
		if (Config.isUSE_RENDER()) {
			fl1 = Eff3DActor.New(this, findHook("_NavLightR"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
			fl2 = Eff3DActor.New(this, findHook("_NavLightG"), null, 1.0F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
			flame.drawing(false);
		}
		pos.getAbs(p, or);
		or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
		pos.setAbs(p, or);
	}

	public void destroy() {
		if (isNet() && isNetMirror())
			doExplosionAir();
		if (Config.isUSE_RENDER()) {
			Eff3DActor.finish(fl1);
			Eff3DActor.finish(fl2);
		}
		super.destroy();
	}

	protected void doExplosion(Actor actor, String s) {
		pos.getTime(Time.current(), p);
		MsgExplosion.send(actor, s, p, getOwner(), 45F, 2.0F, 1, 550F);
		super.doExplosion(actor, s);
	}

	protected void doExplosionAir() {
		pos.getTime(Time.current(), p);
		MsgExplosion.send(null, null, p, getOwner(), 45F, 2.0F, 1, 550F);
		super.doExplosionAir();
	}

	public NetMsgSpawn netReplicate(NetChannel netchannel) throws IOException {
		NetMsgSpawn netmsgspawn = super.netReplicate(netchannel);
		netmsgspawn.writeNetObj(getOwner().net);
		Point3d point3d = pos.getAbsPoint();
		netmsgspawn.writeFloat((float)point3d.x);
		netmsgspawn.writeFloat((float)point3d.y);
		netmsgspawn.writeFloat((float)point3d.z);
		Orient orient = pos.getAbsOrient();
		netmsgspawn.writeFloat(orient.azimut());
		netmsgspawn.writeFloat(orient.tangage());
		float f = (float)getSpeed(null);
		netmsgspawn.writeFloat(f);
		return netmsgspawn;
	}

	private FlightModel fm;
	private Eff3DActor fl1;
	private Eff3DActor fl2;
	private static Orient or = new Orient();
	private static Point3d p = new Point3d();
	private static Point3d pT = new Point3d();
	private static Vector3d v = new Vector3d();
	private long tStart;
	private float prevd;

	static {
		Class class1 = RocketJetManX4Mini.class;
		Property.set(class1, "mesh", "3do/arms/JetManX4Mini/mono.sim");
		Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
		Property.set(class1, "flame", "3do/effects/rocket/mono.sim");
		Property.set(class1, "smoke", "3DO/Effects/Tracers/GuidedRocket/White.eff");
		Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
		Property.set(class1, "emitLen", 50F);
		Property.set(class1, "emitMax", 1.0F);
		Property.set(class1, "sound", "weapon.rocket_132");
		Property.set(class1, "radius", 40F);
		Property.set(class1, "timeLife", 30F);
		Property.set(class1, "timeFire", 33F);
		Property.set(class1, "force", 15712F);
		Property.set(class1, "power", 2.0F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.22F);
		Property.set(class1, "massa", 60F);
		Property.set(class1, "massaEnd", 45F);
		Spawn.add(class1, new SPAWN());
	}

}
