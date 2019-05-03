package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;

public class MissileKS1 extends Missile implements MissileInterceptable
{

	private float victimOffsetZ = Float.MIN_VALUE;
	private float cruiseAltitude = Float.MIN_VALUE;
	private float smoothDampeningFactor = 1F;
	private int altOffsetSign = 0;
	private int flightPhase = 0;
	Vector3d speedVector = new Vector3d();

	public boolean computeSpecialStepBefore() {

		if (!Actor.isValid(this.getVictim())) {
//			System.out.println("victim invalid!");
			return true;
		}
		float victimDistance = (float)GuidedMissileUtils.distanceBetween(this, this.getVictim());
		if (victimOffsetZ == Float.MIN_VALUE) {
			victimOffsetZ = this.getVictimOffsetPoint3f().z;
//			System.out.println("victim offset = " + victimOffsetZ);
		}
		if (cruiseAltitude == Float.MIN_VALUE) {
			if (Actor.isValid(this.getOwner())) {
				this.cruiseAltitude = (float)this.getOwner().pos.getAbsPoint().z;
				if (cruiseAltitude < 2000F) cruiseAltitude = 2000F;
				if (cruiseAltitude > 10000F) cruiseAltitude = 10000F;
//				System.out.println("cruise altitude = " + cruiseAltitude);
			}
		}
		float alt = (float)this.pos.getAbsPoint().z;
		if (victimDistance < 2500F) {
//			System.out.println("distance=" + victimDistance + ", offset z=" + this.victimOffsetZ + ", alt=" + alt);
			this.setVictimOffsetZ(this.victimOffsetZ);
			return true;
		}
		float offsetZ = 2000F;
		if (victimDistance < 20000F) {
			if (flightPhase != 2) {
				flightPhase = 2;
				smoothDampeningFactor = 1F;
				altOffsetSign = 0;
			}
			offsetZ = 400F;
			if (victimDistance > 5000F) {
				float altOffset = alt-offsetZ;
				if (altOffset > 20F) {
					if (altOffsetSign != 1) {
						if (altOffsetSign != 0) smoothDampeningFactor *= 2F;
						altOffsetSign = 1;
					}
					offsetZ -= Math.min(1F, altOffset / 1500F) * (victimDistance - alt) / smoothDampeningFactor;
				} else if (altOffset < -20F) {
					if (altOffsetSign != -1) {
						if (altOffsetSign != 0) smoothDampeningFactor *= 2F;
						altOffsetSign = -1;
					}
					offsetZ += Math.min(1F, -altOffset / 1500F) * (victimDistance - alt) / smoothDampeningFactor;
				}
			}
		} else {
			if (flightPhase != 1) {
				flightPhase = 1;
				smoothDampeningFactor = 1F;
				altOffsetSign = 0;
			}
			offsetZ = cruiseAltitude;
			if (victimDistance > 30000F) {
				float altOffset = alt-offsetZ;
				if (altOffset > 100F) {
					if (altOffsetSign != 1) {
						if (altOffsetSign != 0) smoothDampeningFactor *= 2F;
						altOffsetSign = 1;
					}
					offsetZ -= Math.min(1F, altOffset / 1500F) * (victimDistance - alt) / smoothDampeningFactor;
				} else if (altOffset < -100F) {
					if (altOffsetSign != -1) {
						if (altOffsetSign != 0) smoothDampeningFactor *= 2F;
						altOffsetSign = -1;
					}
					offsetZ += Math.min(1F, -altOffset / 1500F) * (victimDistance - alt) / smoothDampeningFactor;
				}
			}
		}
//		System.out.println("distance=" + victimDistance + ", offset z=" + offsetZ + ", alt=" + alt);
		this.setVictimOffsetZ(offsetZ);

		return true;
	}

	public boolean computeSpecialStepAfter() {
		float turnStepMax = this.getTurnStepMax();
		if (turnStepMax == 0F) return true;
		float roll = this.getMissileOrient().getKren();
		while (roll > 180F) roll-=360F;
		while (roll < -180F) roll+=360F;

		float curTurn = this.getDeltaAzimuth();
		float targetRoll = curTurn / turnStepMax;
		if (targetRoll > 1F) targetRoll = 1F;
		if (targetRoll < -1F) targetRoll = -1F;
		targetRoll *= 45F;

		float newRoll = (roll * 2F + targetRoll) / 3F;
//		System.out.println("Failstate" + this.getFailState() + ", speed=" + (this.getSpeed(null) * 3.6F) + ", turnStepMax=" + turnStepMax + ", curTurn=" + curTurn + ", targetRoll=" + targetRoll + ", Roll=" + this.getMissileOrient().getKren() + ", newRoll=" + newRoll);
		this.getMissileOrient().setYPR(this.getMissileOrient().getYaw(), this.getMissileOrient().getPitch(), -newRoll);

		float speed = (float)this.getSpeed(this.speedVector) * 3.6F;
		float machOne = MissilePhysics.getMachForAlt((float)this.pos.getAbsPoint().z);
		if (speed > machOne * 0.95F) {
			this.speedVector.scale(machOne * 0.95F / speed);
			this.setSpeed(this.speedVector);
		}

		return true;
	}


  static
  {
    Class class1 = MissileKS1.class;
    Property.set(class1, "mesh", "3do/arms/KS-1/mono.sim");
    Property.set(class1, "sprite", (String)null);
    Property.set(class1, "flame", "3do/Effects/RocketKS1/RocketKS1Flame.sim");
    Property.set(class1, "smoke", "3do/Effects/RocketKS1/RocketKS1Smoke.eff");
    Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
    Property.set(class1, "emitLen", 50.0F);
    Property.set(class1, "emitMax", 0.4F);
    Property.set(class1, "sound", "weapon.rocket_132");
    Property.set(class1, "timeLife", 400F); // Rocket life time in Seconds
    Property.set(class1, "timeFire", 300F); // Rocket Engine Burn time in Seconds
    Property.set(class1, "force", 18900F); // Rocket Engine Power (in Newton)
    Property.set(class1, "forceT1", 10.0F); // Time1, i.e. time until Rocket Engine force output maximum reached (in Seconds), 0 disables this feature
    Property.set(class1, "forceP1", 0.0F); // Power1, i.e. Rocket Engine force output at beginning (in Percent)
    Property.set(class1, "forceT2", 1.0F); // Time2, i.e. time before Rocket Engine burn time ends (in Seconds), from this time on Rocket Engine power output decreases, 0 disables this feature
    Property.set(class1, "forceP2", 100.0F); // Power2, i.e. Rocket Engine force output at the end of burn time (in Percent)
    Property.set(class1, "dragCoefficient", 0.5F); // Aerodynamic Drag Coefficient
    Property.set(class1, "powerType", 0);
    Property.set(class1, "power", 650F); // RL Data: 1015kg HE warhead, for realism reduced to 1/10th of it's RL weight
    Property.set(class1, "radius", 250.0F);
    Property.set(class1, "kalibr", 0.92F);
    Property.set(class1, "massa", 2735.0F);
    Property.set(class1, "massaEnd", 2471.0F); // 264kg fuel load
    Property.set(class1, "stepMode", Missile.STEP_MODE_HOMING); // target tracking mode
    Property.set(class1, "launchType", Missile.LAUNCH_TYPE_DROP); // launch pattern
    Property.set(class1, "detectorType", Missile.DETECTOR_TYPE_RADAR_HOMING); // detector type
    Property.set(class1, "sunRayAngle", 0.0F); // max. Angle at which the missile will track Sun Ray, zero disables Sun Ray tracking (only valid for IR detector missiles)
    Property.set(class1, "multiTrackingCapable", 0); // set whether or not this type of missile can fight multiple different targets simultaneously. Usually "1" for IR missiles and "0" for others.
    Property.set(class1, "canTrackSubs", 0); // set whether or not submerged objects can be tracked
    Property.set(class1, "minPkForAI", 25.0F); // min. Kill Probability for AI to launch a missile
    Property.set(class1, "timeForNextLaunchAI", 10000L); // time between two Missile launches for AI
    Property.set(class1, "engineDelayTime", -5000L); // Rocket Engine Start Delay Time (in milliseconds), negative Value means that Engine is started before Missile leaves it's rail
    Property.set(class1, "attackDecisionByAI", 0); // let AI decide whether or not to attack. Usually "1" for short range missiles and "0" for all others.
    Property.set(class1, "targetType", Missile.TARGET_SHIP); // Type of valid targets, can be any combination of TARGET_AIR, TARGET_GROUND and TARGET_SHIP.
    Property.set(class1, "shotFreq", 5.0F); // Minimum time (in seconds) between two missile launches

    // Factor for missile tracking random ground targets issue.
    // Calculation: Angle to ground (in degrees) divided by:
    //              (altitude in meters divided by 1000)^2
    Property.set(class1, "groundTrackFactor", Float.MAX_VALUE); // lower value means higher probability of ground target tracking
    Property.set(class1, "flareLockTime", 1000L); // time (in milliseconds) for missile locking on to different target, i.e. flare (sun, ground clutter etc.)
    Property.set(class1, "trackDelay", 10000L); // time (in milliseconds) for missile tracking target after launch (i.e. the time for the missile to fly straight first to be clear of the launching A/C)
    Property.set(class1, "failureRate", 10.0F); // Failure rate in percent
    Property.set(class1, "maxLockGForce", 6.0F); // max. G-Force for lockon
    Property.set(class1, "maxFOVfrom", 30.0F); // max Angle offset from launch A/C POV
    Property.set(class1, "maxFOVto", 360.0F); // max Angle offset from target aft POV
    Property.set(class1, "PkMaxFOVfrom", 35.0F); // max Angle offset from launch A/C POV for Pk calculation
    Property.set(class1, "PkMaxFOVto", Float.MAX_VALUE); // max Angle offset from target aft POV for Pk calculation
    Property.set(class1, "PkDistMin", 1000.0F); // min Distance for Pk calculation
    Property.set(class1, "PkDistOpt", 50000.0F); // optimum Distance for Pk calculation
    Property.set(class1, "PkDistMax", 100000.0F); // max Distance for Pk calculation
    Property.set(class1, "leadPercent", 50.0F); // Track calculation lead value
    Property.set(class1, "maxGForce", 3.0F); // max turning rate G-Force
    Property.set(class1, "stepsForFullTurn", 30); // No. of ticks (1 tick = 30ms) for full control surface turn, higher value means slower reaction and smoother flight, lower value means higher agility
    Property.set(class1, "fxLock", (String)null); // prs file for Lock Tone
	Property.set(class1, "fxLockVolume", 1F); // Volume for Lock Tone, "1" is default
    Property.set(class1, "fxNoLock", (String)null); // prs file for No Lock Tone
	Property.set(class1, "fxNoLockVolume", 1F); // Volume for Lock Tone, "1" is default
//    Property.set(class1, "smplLock", (String)null); // wav file for Lock Tone
//    Property.set(class1, "smplNoLock", (String)null); // wav file for No Lock Tone
	Property.set(class1, "singleTone", 0); // set to "1" if one single tone is used for lock / no lock
	Property.set(class1, "growlStyle", 0); // "1" is Sidewinder growl style, others may follow
	Property.set(class1, "fxLockPitch", 1F); // Pitch for Lock Tone
	Property.set(class1, "fxNoLockPitch", 1F); // Pitch for No Lock Tone
    Property.set(class1, "friendlyName", "KS-1"); // Display Name of this missile
    Property.set(class1, "iconFar_shortClassName", "KS-1 Missile"); // Display Name of this missile
    Spawn.add(class1, new SPAWN());
  }

	public MissileKS1() {
	}

  public MissileKS1(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
  {
    MissileInit(actor, netchannel, i, point3d, orient, f);
  }

  static class SPAWN extends Missile.SPAWN
  {
    public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
      new MissileKS1(actor, netchannel, i, point3d, orient, f);
    }
  }
}