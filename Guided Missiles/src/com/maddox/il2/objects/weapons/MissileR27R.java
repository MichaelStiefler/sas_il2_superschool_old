package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;

public class MissileR27R extends Missile {
	static class SPAWN extends Missile.SPAWN {

		SPAWN() {
		}

		public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
			new MissileR27R(actor, netchannel, i, point3d, orient, f);
		}
	}

	static {
		Class class1 = MissileR27R.class;
		Property.set(class1, "mesh", "3do/arms/R27R/mono.sim");
		Property.set(class1, "sprite", "EFFECTS/Smokes/SmokeBlack_missiles.eff");
		Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
		Property.set(class1, "smoke", "EFFECTS/Smokes/SmokeBlack_Wreckage.eff");
		Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
		Property.set(class1, "emitLen", 50F);
		Property.set(class1, "emitMax", 0.4F);
		Property.set(class1, "sound", "weapon.rocket_132");
		Property.set(class1, "timeLife", 60F);
		Property.set(class1, "timeFire", 8.9F);
		Property.set(class1, "force", 50000F);
		Property.set(class1, "forceT1", 0.5F);
		Property.set(class1, "forceP1", 0.0F);
		Property.set(class1, "forceT2", 0.2F);
		Property.set(class1, "forceP2", 50F);
		Property.set(class1, "dragCoefficient", 0.3F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "power", 3.9F);
		Property.set(class1, "radius", 15.93F);
		Property.set(class1, "kalibr", 2.3F);
		Property.set(class1, "massa", 253F);
		Property.set(class1, "massaEnd", 150F);
		Property.set(class1, "stepMode", 0);
		Property.set(class1, "launchType", 2);
		Property.set(class1, "detectorType", 3);
		Property.set(class1, "sunRayAngle", 0.0F);
		Property.set(class1, "multiTrackingCapable", 1);
		Property.set(class1, "canTrackSubs", 0);
		Property.set(class1, "minPkForAI", 35F);
		Property.set(class1, "timeForNextLaunchAI", 10000L);
		Property.set(class1, "engineDelayTime", 500);
		Property.set(class1, "attackDecisionByAI", 1);
		Property.set(class1, "targetType", 1);
		Property.set(class1, "shotFreq", 1.01F);
		Property.set(class1, "groundTrackFactor", 16F);
		Property.set(class1, "flareLockTime", 1000L);
		Property.set(class1, "trackDelay", 1000L);
		Property.set(class1, "failureRate", 30F);
		Property.set(class1, "maxLockGForce", 8F);
		Property.set(class1, "maxFOVfrom", 20F);
		Property.set(class1, "maxFOVto", 180F);
		Property.set(class1, "PkMaxFOVfrom", 25F);
		Property.set(class1, "PkMaxFOVto", 80F);
		Property.set(class1, "PkDistMin", 3000F);
		Property.set(class1, "PkDistOpt", 15000F);
		Property.set(class1, "PkDistMax", 40000F);
		Property.set(class1, "leadPercent", 100F);
		Property.set(class1, "maxGForce", 8F);
		Property.set(class1, "stepsForFullTurn", 12);
		Property.set(class1, "fxLock", "weapon.R60.lock");
		Property.set(class1, "fxNoLock", "weapon.R60.nolock");
		Property.set(class1, "smplLock", "R60_lock.wav");
		Property.set(class1, "smplNoLock", "R60_no_lock.wav");
		Property.set(class1, "friendlyName", "R-27");
		Spawn.add(class1, new SPAWN());
	}

	public MissileR27R() {
	}

	public MissileR27R(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		this.MissileInit(actor, netchannel, i, point3d, orient, f);
	}
}
