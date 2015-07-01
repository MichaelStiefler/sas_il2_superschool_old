package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.MsgShot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public class RocketFlare extends Missile implements MissileInterceptable {
	static class SPAWN extends Missile.SPAWN {

		public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
			new RocketFlare(actor, netchannel, i, point3d, orient, f);
		}

		SPAWN() {
		}
	}

	public RocketFlare(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		MissileInit(actor, netchannel, i, point3d, orient, f);
	}

	public void start(float f, int i) {
		float f1 = 30F;
		super.start(f1, i);
		getOwner().getSpeed(super.speed);
		super.speed.z = -50D;
		setSpeed(super.speed);
		Eff3DActor.New(this, null, new Loc(), 1.0F, "3do/Effects/Fireworks/Piropatron.eff", f1);
		Eff3DActor.New(this, null, new Loc(), 0.8F, "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff", f1);
	}

	protected void doExplosion(Actor actor, String s) {
		super.pos.getTime(Time.current(), p);
		Class class1 = getClass();
		float f = Property.floatValue(class1, "power", 1000F);
		int i = Property.intValue(class1, "powerType", 0);
		float f1 = Property.floatValue(class1, "radius", 0.0F);
		getSpeed(super.speed);
		Vector3f vector3f = new Vector3f(super.speed);
		vector3f.normalize();
		vector3f.scale(850F);
		MsgShot.send(actor, s, p, vector3f, super.M, getOwner(), (float) ((double) (0.5F * super.M) * super.speed.lengthSquared()), 3, 0.0D);
		MsgExplosion.send(actor, s, p, getOwner(), super.M, f, i, f1);
		destroy();
	}

	protected void doExplosionAir() {
	}

	private static Point3d p = new Point3d();

	static {
		Class class1 = com.maddox.il2.objects.weapons.RocketFlare.class;
		Property.set(class1, "mesh", "3do/arms/Piropatron/mono.sim");
		Property.set(class1, "sprite", (String) null);
		Property.set(class1, "flame", (String) null);
		Property.set(class1, "smoke", "EFFECTS/Smokes/SmokeMissilessmall.eff");
		Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
		Property.set(class1, "emitLen", 5F);
		Property.set(class1, "emitMax", 20F);
		Property.set(class1, "sound", (String) null);
		Property.set(class1, "timeLife", 7F);
		Property.set(class1, "timeFire", 0.2F);
		Property.set(class1, "force", 2500F);
		Property.set(class1, "forceT1", 0.0F);
		Property.set(class1, "forceP1", 0.0F);
		Property.set(class1, "forceT2", 0.0F);
		Property.set(class1, "forceP2", 0.0F);
		Property.set(class1, "dragCoefficient", 0.0F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "power", 1E-005F);
		Property.set(class1, "radius", 0.0F);
		Property.set(class1, "kalibr", 0.001F);
		Property.set(class1, "massa", 3F);
		Property.set(class1, "massaEnd", 20000F);
		Property.set(class1, "stepMode", 0);
		Property.set(class1, "launchType", 2);
		Property.set(class1, "detectorType", 2);
		Property.set(class1, "sunRayAngle", 0.0F);
		Property.set(class1, "multiTrackingCapable", 0);
		Property.set(class1, "canTrackSubs", 0);
		Property.set(class1, "minPkForAI", 95F);
		Property.set(class1, "timeForNextLaunchAI", 10000L);
		Property.set(class1, "engineDelayTime", 0L);
		Property.set(class1, "attackDecisionByAI", 1);
		Property.set(class1, "targetType", 256);
		Property.set(class1, "shotFreq", 5F);
		Property.set(class1, "groundTrackFactor", 3.402823E+038F);
		Property.set(class1, "flareLockTime", 1000L);
		Property.set(class1, "trackDelay", 10000L);
		Property.set(class1, "failureRate", 0.0F);
		Property.set(class1, "maxLockGForce", 50F);
		Property.set(class1, "maxFOVfrom", 30F);
		Property.set(class1, "maxFOVto", 360F);
		Property.set(class1, "PkMaxFOVfrom", 35F);
		Property.set(class1, "PkMaxFOVto", 3.402823E+038F);
		Property.set(class1, "PkDistMin", 0.1F);
		Property.set(class1, "PkDistOpt", 0.1F);
		Property.set(class1, "PkDistMax", 0.1F);
		Property.set(class1, "leadPercent", 0.0F);
		Property.set(class1, "maxGForce", 50F);
		Property.set(class1, "stepsForFullTurn", 10);
		Property.set(class1, "fxLock", (String) null);
		Property.set(class1, "fxNoLock", (String) null);
		Property.set(class1, "smplLock", (String) null);
		Property.set(class1, "smplNoLock", (String) null);
		Property.set(class1, "friendlyName", "Flare");
		Property.set(class1, "iconFar_shortClassName", "Flare");
		Spawn.add(class1, new SPAWN());
	}
}
