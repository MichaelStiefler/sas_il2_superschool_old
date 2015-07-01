package com.maddox.il2.objects.weapons;

import java.util.List;
import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public class SA2 extends MissileSAM {

	public boolean interpolateStep() {
		if (range == 0.0D) {
			Random random = new Random();
			int i = random.nextInt(100) + 10;
			range = i;
		}
		float f = Time.tickLenFs();
		float f1 = (float) getSpeed(null);
		f1 += (500F - f1) * 0.1F * f;
		super.pos.getAbs(p, or);
		v.set(1.0D, 0.0D, 0.0D);
		or.transform(v);
		v.scale(f1);
		setSpeed(v);
		p.x += ((Tuple3d) (v)).x * (double) f;
		p.y += ((Tuple3d) (v)).y * (double) f;
		p.z += ((Tuple3d) (v)).z * (double) f;
		if (isNet() && isNetMirror()) {
			super.pos.setAbs(p, or);
			return false;
		}
		if (Time.current() > tStart + 350L)
			if (victim == null || !Actor.isValid(victim)) {
				victim = NearestTargets.getEnemy(9, -1, p, 35000D, getOwner().getArmy());
			} else {
				if (victim != null) {
					List list1 = Engine.countermeasures();
					int m = list1.size();
					for (int t = 0; t < m; t++) {
						Actor flarechaff = (Actor) list1.get(t);
						if (flarechaff instanceof RocketFlare)
							victim = flarechaff;
					}

				}
				victim.pos.getAbs(pT);
				pT.sub(p);
				or.transformInv(pT);
				if (p.distance(victim.pos.getAbsPoint()) < 5000D && (victim instanceof TypeFighter) && ((Tuple3d) (victim.pos.getAbsPoint())).z > 1000D && victim != World.getPlayerAircraft())
					doevade();
				if (p.distance(victim.pos.getAbsPoint()) < range || p.distance(victim.pos.getAbsPoint()) > lastdist) {
					super.pos.setAbs(p, or);
					doExplosionAir();
					postDestroy();
					collide(false);
					drawing(false);
					return false;
				}
				if (p.distance(victim.pos.getAbsPoint()) > range) {
					if (((Tuple3d) (pT)).y > 1.1000000000000001D)
						deltaAzimuth = -0.1F;
					if (((Tuple3d) (pT)).y < -1.1000000000000001D)
						deltaAzimuth = 0.1F;
					if (((Tuple3d) (pT)).z < -1.1000000000000001D)
						deltaTangage = -0.1F;
					if (((Tuple3d) (pT)).z > 1.1000000000000001D)
						deltaTangage = 0.1F;
					or.increment(40F * f * deltaAzimuth, 40F * f * deltaTangage, 0.0F);
					deltaAzimuth = deltaTangage = 0.0F;
				}
				lastdist = p.distance(victim.pos.getAbsPoint());
			}
		super.pos.setAbs(p, or);
		return false;
	}

	public boolean doevade() {
		((Pilot) ((SndAircraft) ((Aircraft) victim)).FM).setSpeedMode(9);
		((FlightModelMain) ((Pilot) ((SndAircraft) ((Aircraft) victim)).FM)).CT.ElevatorControl = 0.1F;
		((FlightModelMain) ((Pilot) ((SndAircraft) ((Aircraft) victim)).FM)).CT.AileronControl = 0.3F;
		return true;
	}

	public SA2() {
		lastdist = 35000D;
		range = 0.0D;
		victim = null;
		tStart = 0L;
		deltaAzimuth = 0.0F;
		deltaTangage = 0.0F;
		victim = null;
	}

	public SA2(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		lastdist = 20000D;
		range = 0.0D;
		victim = null;
		tStart = 0L;
		super.pos.setAbs(point3d, orient);
		super.pos.reset();
		super.pos.setBase(actor, null, true);
		doStart(-1F);
		v.set(1.0D, 0.0D, 0.0D);
		orient.transform(v);
		v.scale(f);
		setSpeed(v);
		collide(false);
	}

	public void start(float f, int i) {
		Actor actor = super.pos.base();
		if (Actor.isValid(actor) && (actor instanceof Aircraft) && actor.isNetMirror()) {
			destroy();
			return;
		} else {
			doStart(f);
			return;
		}
	}

	private void doStart(float f) {
		super.start(-1F, 0);
		tStart = Time.current();
		Eff3DActor.New(this, findHook("_SMOKE"), null, 1.0F, "3do/Effects/RocketSidewinder/NikeSmoke.eff", -1F);
		super.flame.drawing(false);
		super.pos.getAbs(p, or);
		or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
		super.pos.setAbs(p, or);
	}

	private static Orient or = new Orient();
	private static Point3d p = new Point3d();
	private static Point3d pT = new Point3d();
	private static Vector3d v = new Vector3d();
	private long tStart;
	private float deltaAzimuth;
	private float deltaTangage;
	private Actor victim;
	private double lastdist;
	private double range;

	static {
		Class class1 = SA2.class;
		Property.set(class1, "timeFire", 20F);
		Spawn.add(class1, new MissileSAM.SPAWN());
	}
}
