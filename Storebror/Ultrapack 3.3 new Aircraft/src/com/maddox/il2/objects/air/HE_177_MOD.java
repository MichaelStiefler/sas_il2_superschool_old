package com.maddox.il2.objects.air;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Polares;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.CrossVersion;

public abstract class HE_177_MOD extends Scheme2 implements TypeBomber, TypeTransport {

	public HE_177_MOD() {
		this.bPitUnfocused = true;
		this.bSightAutomation = false;
		this.bSightBombDump = false;
		this.fSightCurDistance = 0.0F;
		this.fSightCurForwardAngle = 0.0F;
		this.fSightCurSideslip = 0.0F;
		this.fSightCurAltitude = 850F;
		this.fSightCurSpeed = 150F;
		this.fSightCurReadyness = 0.0F;
		this.calibDistance = 0.0F;
		this.bombBayPos = 0.0F;
		this.ventralGunnerTargetCheckTime = 0L;
		this.lastEngineStatusChange = new long[] { 0L, 0L };
		this.engineStatus = new boolean[] { false, false };
		if (this.fEngineShakeLevel == null) this.fEngineShakeLevel = new float[2]; // initialize damaged engines array
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.AS.wantBeaconsNet(true);
		this.initFlapDrag();
	}

	// New Gear Animation Code,
	// historically accurate in terms of moving main gear before tail wheel
	// In order to do so, new Parameter "bDown" has been introduced to distinguish between
	// gear up and gear down
	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2, boolean bDown) {
		if (f < 0.5F) {
			hiermesh.chunkSetAngles("GearL1_11_D0", 0.0F, Aircraft.cvt(f, 0.025F, 0.305F, 0.0F, -90F), 0.0F); // Left inner Gear Cover, gear up
			hiermesh.chunkSetAngles("GearL2_11_D0", 0.0F, Aircraft.cvt(f, 0.075F, 0.355F, 0.0F, -90F), 0.0F); // Left outer Gear Cover, gear up
		} else {
			hiermesh.chunkSetAngles("GearL1_11_D0", 0.0F, Aircraft.cvt(f, 0.67F, 0.95F, -90F, 0.0F), 0.0F); // Left inner Gear Cover, gear down
			hiermesh.chunkSetAngles("GearL2_11_D0", 0.0F, Aircraft.cvt(f, 0.72F, 0.999F, -90F, 0.0F), 0.0F); // Left outer Gear Cover, gear down
		}
		if (f1 < 0.5F) {
			hiermesh.chunkSetAngles("GearR1_11_D0", 0.0F, Aircraft.cvt(f1, 0.001F, 0.28F, 0.0F, -90F), 0.0F); // Right inner Gear Cover, gear up
			hiermesh.chunkSetAngles("GearR2_11_D0", 0.0F, Aircraft.cvt(f1, 0.05F, 0.33F, 0.0F, -90F), 0.0F); // Right outer Gear Cover, gear up
		} else {
			hiermesh.chunkSetAngles("GearR1_11_D0", 0.0F, Aircraft.cvt(f1, 0.645F, 0.925F, -90F, 0.0F), 0.0F); // Right inner Gear Cover, gear down
			hiermesh.chunkSetAngles("GearR2_11_D0", 0.0F, Aircraft.cvt(f1, 0.695F, 0.975F, -90F, 0.0F), 0.0F); // Right outer Gear Cover, gear down
		}
		
		if (bDown) { // Center gear, lower
			hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.175F, 0.0F, -150F), 0.0F);
			hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.175F, 0.0F, -130F), 0.0F);
			hiermesh.chunkSetAngles("GearC6_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.175F, 0.0F, -150F), 0.0F);
			hiermesh.chunkSetAngles("GearC7_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.175F, 0.0F, -130F), 0.0F);
			hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f2, 0.15F, 0.25F, 0.0F, -90F), 0.0F);
		} else { // Center gear, raise
			hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.75F, 0.925F, 0.0F, -150F), 0.0F);
			hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f2, 0.75F, 0.925F, 0.0F, -130F), 0.0F);
			hiermesh.chunkSetAngles("GearC6_D0", 0.0F, Aircraft.cvt(f2, 0.75F, 0.925F, 0.0F, -150F), 0.0F);
			hiermesh.chunkSetAngles("GearC7_D0", 0.0F, Aircraft.cvt(f2, 0.75F, 0.925F, 0.0F, -130F), 0.0F);
			hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f2, 0.9F, 0.999F, 0.0F, -90F), 0.0F);
		}

		hiermesh.chunkSetAngles("GearR1_2_D0", 0.0F, Aircraft.cvt(f1, 0.25F, 0.675F, 0.0F, -90F), 0.0F); // Right inner Gear
		hiermesh.chunkSetAngles("GearR1_7_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.25F, 0.675F, 0.0F, -144F));
		hiermesh.chunkSetAngles("GearR1_10_D0", 0.0F, Aircraft.cvt(f1, 0.25F, 0.675F, 0.0F, -90F), 0.0F);
		hiermesh.chunkSetAngles("GearR1_12_D0", 0.0F, Aircraft.cvt(f1, 0.25F, 0.675F, 0.0F, -90F), 0.0F);
		
		hiermesh.chunkSetAngles("GearR2_2_D0", 0.0F, Aircraft.cvt(f1, 0.3F, 0.725F, 0.0F, -90F), 0.0F); // Right outer Gear
		hiermesh.chunkSetAngles("GearR2_7_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.3F, 0.725F, 0.0F, -144F));
		hiermesh.chunkSetAngles("GearR2_10_D0", 0.0F, Aircraft.cvt(f1, 0.3F, 0.725F, 0.0F, -90F), 0.0F);
		hiermesh.chunkSetAngles("GearR2_12_D0", 0.0F, Aircraft.cvt(f1, 0.3F, 0.725F, 0.0F, -90F), 0.0F);
		
		hiermesh.chunkSetAngles("GearL1_2_D0", 0.0F, Aircraft.cvt(f, 0.275F, 0.7F, 0.0F, -90F), 0.0F); // Left inner Gear
		hiermesh.chunkSetAngles("GearL1_7_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.275F, 0.7F, 0.0F, -144F));
		hiermesh.chunkSetAngles("GearL1_10_D0", 0.0F, Aircraft.cvt(f, 0.275F, 0.7F, 0.0F, -90F), 0.0F);
		hiermesh.chunkSetAngles("GearL1_12_D0", 0.0F, Aircraft.cvt(f, 0.275F, 0.7F, 0.0F, -90F), 0.0F);
		
		hiermesh.chunkSetAngles("GearL2_2_D0", 0.0F, Aircraft.cvt(f, 0.325F, 0.75F, 0.0F, -90F), 0.0F); // Left outer Gear
		hiermesh.chunkSetAngles("GearL2_7_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.325F, 0.75F, 0.0F, 144F));
		hiermesh.chunkSetAngles("GearL2_10_D0", 0.0F, Aircraft.cvt(f, 0.325F, 0.75F, 0.0F, -90F), 0.0F);
		hiermesh.chunkSetAngles("GearL2_12_D0", 0.0F, Aircraft.cvt(f, 0.325F, 0.75F, 0.0F, -90F), 0.0F);
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		moveGear(hiermesh, f, f1, f2, true);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(hierMesh(), f, f1, f2, this.FM.CT.GearControl > 0.5F);
	}

	// ************************************************************************************************
	// Gear code for backward compatibility, older base game versions don'f1 indepently move their gears
	public static void moveGear(HierMesh hiermesh, float f, boolean bDown) {
		moveGear(hiermesh, f, f, f, bDown); // re-route old style function calls to new code
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		moveGear(hiermesh, f, f, f, true); // re-route old style function calls to new code
	}

	protected void moveGear(float f) {
		moveGear(hierMesh(), f, this.FM.CT.GearControl > 0.5F);
	}

	// ************************************************************************************************

	public void moveSteering(float f) {
		hierMesh().chunkSetAngles("GearC3_D0", -Aircraft.cvt(f, -65F, 65F, 65F, -65F), 0.0F, 0.0F);
	}

	public void moveWheelSink() {
		resetYPRmodifier();
		Aircraft.xyz[2] = Aircraft.cvt(FM.Gears.gWheelSinking[0] - 0.055F, 0.0F, 0.1625F, 0.0F, 0.1625F);
		hierMesh().chunkSetLocate("GearL1_3_D0", Aircraft.xyz, Aircraft.ypr);
		hierMesh().chunkSetLocate("GearL2_3_D0", Aircraft.xyz, Aircraft.ypr);
		Aircraft.xyz[2] = Aircraft.cvt(FM.Gears.gWheelSinking[1] - 0.055F, 0.0F, 0.1625F, 0.0F, 0.1625F);
		hierMesh().chunkSetLocate("GearR1_3_D0", Aircraft.xyz, Aircraft.ypr);
		hierMesh().chunkSetLocate("GearR2_3_D0", Aircraft.xyz, Aircraft.ypr);
	}
	
	private void rotateInnerWheels() {
        hierMesh().chunkSetAngles("GearL1_1_D0", 0.0F, -FM.Gears.gWheelAngles[0], 0.0F);
        hierMesh().chunkSetAngles("GearR1_1_D0", 0.0F, -FM.Gears.gWheelAngles[1], 0.0F);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (flag) {
			if (FM.AS.astateEngineStates[0] > 3) {
				if (World.Rnd().nextFloat() < 0.05F)
					FM.AS.hitTank(this, 0, 1);
				if (World.Rnd().nextFloat() < 0.05F)
					FM.AS.hitTank(this, 1, 1);
			}
			if (FM.AS.astateEngineStates[1] > 3) {
				if (World.Rnd().nextFloat() < 0.05F)
					FM.AS.hitTank(this, 2, 1);
				if (World.Rnd().nextFloat() < 0.05F)
					FM.AS.hitTank(this, 3, 1);
			}
			if (FM.AS.astateTankStates[0] > 5 && World.Rnd().nextFloat() < 0.02F)
				nextDMGLevel(FM.AS.astateEffectChunks[0] + "0", 0, this);
			if (FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.02F)
				nextDMGLevel(FM.AS.astateEffectChunks[1] + "0", 0, this);
			if (FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.125F)
				FM.AS.hitTank(this, 2, 1);
			if (FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.125F)
				FM.AS.hitTank(this, 1, 1);
			if (FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.02F)
				nextDMGLevel(FM.AS.astateEffectChunks[2] + "0", 0, this);
			if (FM.AS.astateTankStates[3] > 5 && World.Rnd().nextFloat() < 0.02F)
				nextDMGLevel(FM.AS.astateEffectChunks[3] + "0", 0, this);
		}
		for (int i = 1; i < 7; i++)
			if (FM.getAltitude() < 3000F)
				hierMesh().chunkVisible("HMask" + i + "_D0", false);
			else
				hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

		for (int i = 0; i < 2; i++) {
			float theEngineStatus = FM.EI.engines[i].getReadyness() * FM.EI.engines[i].getRPM();
			boolean isEngineDead = (theEngineStatus < 1F);
			if (this.engineStatus[i] != isEngineDead) {
				this.lastEngineStatusChange[i] = Time.current();
			}
			this.engineStatus[i] = isEngineDead;
			if (FM.EI.engines[i].getControlFeather() != (isEngineDead ? 1 : 0)) {
				if (Time.current() - this.lastEngineStatusChange[i] > 3000) {
					FM.EI.engines[i].setControlFeather(isEngineDead ? 1 : 0);
					this.lastEngineStatusChange[i] = Time.current();
				}
			}
		}
	}

	protected void moveFlap(float f) {
		if (f <= 0.2F) {
			xyz[0] = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 0.7F);
			xyz[2] = -f * 0.18F;
		} else {
			xyz[0] = 0.7F;
			xyz[2] = -0.036F;
		}
		xyz[1] = 0F;

		ypr[0] = 0F;
		ypr[1] = -45.0F * f;
		ypr[2] = 0F;

		hierMesh().chunkSetLocate("Flap02_D0", xyz, ypr);
		hierMesh().chunkSetLocate("Flap03_D0", xyz, ypr);

		if (f <= 0.2F) {
			ypr[0] = f * -13.5F;
			ypr[2] = f * 2.0F;
		} else {
			ypr[0] = -2.7F;
			ypr[2] = 0.4F;
		}

		xyz[0] *= 0.8F;
		xyz[2] *= 0.5F;
		hierMesh().chunkSetLocate("Flap01_D0", xyz, ypr);
		ypr[0] *= -1F;
		ypr[2] *= -1F;
		hierMesh().chunkSetLocate("Flap04_D0", xyz, ypr);
	}

	private void engineShake() {
		if(!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel)) return; // don't shake for non-player aircraft.

		Arrays.fill(this.fEngineShakeLevel, 0.0F); // initialize Engine Shake Level Array
		
		for (int i=0; i<2; i++) { // check each engine's stage
			if (this.FM.EI.engines[i].getStage() < Motor._E_STAGE_CATCH_UP) continue; // engine is not running, no shake
			if (this.FM.EI.engines[i].getStage() > Motor._E_STAGE_NOMINAL) {
				if (this.FM.EI.engines[i].getRPM() == 0.0F)	continue; // engine is not running, no shake
			}
			if (this.FM.EI.engines[i].getStage() < Motor._E_STAGE_CATCH_FIRE)
				this.fEngineShakeLevel[i] = fStartupShakeLevel * (float)(this.FM.EI.engines[i].getStage()) / 5.0F; // engine is starting, set specified startup shake level
			else {
				this.fEngineShakeLevel[i] = 1.0F - this.FM.EI.engines[i].getReadyness(); // engine is running, set shake level according to damage
				float engineRpm = FM.EI.engines[i].getRPM();
				if (engineRpm < 1000F && engineRpm > 100F)
					this.fEngineShakeLevel[i] += (1000F - engineRpm) / 5000F + fShakeThreshold;
				if (this.fEngineShakeLevel[i] < fShakeThreshold) this.fEngineShakeLevel[i] = 0.0F; // only take shake levels into account which exceed the threshold shake setting
			}

			// Shake whole aircraft, not just inside the cockpit.
			if (this.fEngineShakeLevel[i] == 0.0F) continue; // don't apply aircraft shake force vector if there's no shake to apply
			Point3f theEnginePos = this.FM.EI.engines[i].getEnginePos(); // get engine position
			Vector3f theEngineShake = new Vector3f(World.Rnd().nextFloat(-1.0F, 1.0F), World.Rnd().nextFloat(-1.0F, 1.0F), World.Rnd().nextFloat(-1.0F, 1.0F)); // generate random shake force vector
			float fShakeFactor = (float)Math.pow(this.FM.M.massEmpty, 0.3F) / (float)2 * 10000F * fMaxShake; // calculate shake force, must fit for aircraft weight etc.
			theEngineShake.scale(this.fEngineShakeLevel[i] * fShakeFactor); // scale random shake vector accordingly
			Vector3f theEngineMomentum = new Vector3f(); // instantiate the damage shake moment
			theEngineMomentum.cross(theEnginePos, theEngineShake); // damage shake momentum is the vector's cartesian product between engine pos and random shake force
			this.FM.producedAM.x += theEngineMomentum.x; // apply x-axis turn momentum only
		}
		
		float fTotalShake = 0.0F; // aggregated shake level of all engines
		int iShakeWeightFactor = 2; // weighted shake, engine with highest shake level counts most
		Arrays.sort(fEngineShakeLevel); // sort shake level array (sorts in ascending order)
		for (int i=1; i>=0; i--) { // go through the list of engine shake levels in descending order
			if (this.fEngineShakeLevel[i] == 0.0F) break; // no more engine shake? exit loop.
			fTotalShake += this.fEngineShakeLevel[i] * (float)iShakeWeightFactor; // add weighted total shake
			iShakeWeightFactor >>= 1; // reduce weight factor by 2
		}
		fTotalShake /= (float)(2); // adjust total shake to 0.0F through 1.0F again.
		if (((RealFlightModel)this.FM).producedShakeLevel < fTotalShake * fMaxShake) // if in-cockpit shake is stronger already, do nothing.
			((RealFlightModel)this.FM).producedShakeLevel = fTotalShake * fMaxShake; // apply shake level according to max shake level setting.
	}
	
//	private void checkThrottleMovement(float f) {
//		if(!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel)) return; // don't shake for non-player aircraft.
//            for(int i = 0; i < 2; i++)
//            {
//                if(curctl[i] == -1F)
//                {
//                    curctl[i] = oldctl[i] = FM.EI.engines[i].getControlThrottle();
//                    continue;
//                }
//                curctl[i] = FM.EI.engines[i].getControlThrottle();
//                if((curctl[i] - oldctl[i]) / f > 6F && FM.EI.engines[i].getRPM() < 2400F && FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.25F) {
//                    HUD.training("Throttle Movement!!!");
//                	FM.AS.setEngineState(this, i, 4);
////                    FM.AS.hitEngine(this, i, 100);
//                }
//                if((curctl[i] - oldctl[i]) / f < -3F && FM.EI.engines[i].getRPM() < 2400F && FM.EI.engines[i].getStage() == 6)
//                {
////                    if(World.Rnd().nextFloat() < 0.25F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
////                        FM.EI.engines[i].setEngineStops(this);
////                    if(World.Rnd().nextFloat() < 0.75F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
////                        FM.EI.engines[i].setKillCompressor(this);
//                }
//                oldctl[i] = curctl[i];
//            }
//	}

	public void update(float f) {
		for (int i = 0; i < 2; i++) {
			float f1 = FM.EI.engines[i].getControlRadiator();
			if (Math.abs(flapps[i] - f1) > 0.01F) {
				flapps[i] = f1;
				for (int j = 1; j < 20; j++) {
					String lr = i == 0 ? "L" : "R";
					String s = "RAD" + lr + j + "_D0";
					hierMesh().chunkSetAngles(s, 0.0F, -30F * f1, 0.0F);
				}
			}
		}
		this.rotateInnerWheels();
		this.flapDrag();
//		this.checkThrottleMovement(f);
		super.update(f);
		this.engineShake();
	}

	protected void moveBayDoor(float f) {
		if (this.thisWeaponsName.startsWith("A5")) {
			this.FM.CT.BayDoorControl = 0.0F;
			return;
		}
		this.setBombBayPos(f);
		hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -74F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -90F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay3_D0", 0.0F, -74F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -90F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay5_D0", 0.0F, -74F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay6_D0", 0.0F, -90F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay7_D0", 0.0F, -74F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay8_D0", 0.0F, -90F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay9_D0", 0.0F, -74F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay10_D0", 0.0F, -90F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay11_D0", 0.0F, -74F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay12_D0", 0.0F, -90F * f, 0.0F);
	}

	void checkAftVentralGun() {
		if (this.FM.CT.getBayDoor() < 0.5F)
			return;
		float aftVentralGunTangage = this.FM.turret[2].tu[1];
		aftVentralGunTangage = Math.min(aftVentralGunTangage, CockpitHE_177A5_BGunner.GUN_OFFSET_TANGAGE * (this.getBombBayPos() - 1F));
		this.FM.turret[2].tu[0] = 0.0F;
		this.FM.turret[2].tu[1] = aftVentralGunTangage;
		this.hierMesh().chunkSetAngles("Turret3A_D0", 0.0F, 0.0F, 0.0F);
		this.hierMesh().chunkSetAngles("Turret3B_D0", 0.0F, aftVentralGunTangage, 0.0F);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		float f2 = 0F;
		switch (i) {
		default:
			break;

		case 0: // Nose Gunner
			f += CockpitHE_177A5_NGunner.GUN_OFFSET_YAW;
			if (f1 > 40F) {
				f1 = 40F;
				flag = false;
			}
			if (f1 < -40F) {
				f1 = -40F;
				flag = false;
			}
			if (f > 60F) {
				f = 60F;
				flag = false;
			}
			if (f < -20F) {
				f = -20F;
				flag = false;
			}
			f -= CockpitHE_177A5_NGunner.GUN_OFFSET_YAW;
			break;

		case 1: // Forward Ventral Gunner
			f1 += CockpitHE_177A5_FGunner.GUN_OFFSET_TANGAGE;
			if (f1 > 7F) {
				f1 = 7F;
				flag = false;
			}
			if (f1 < -40F) {
				f1 = -40F;
				flag = false;
			}
			if (f > 15F) {
				f = 15F;
				flag = false;
			}
			if (f < -15F) {
				f = -15F;
				flag = false;
			}
			f1 -= CockpitHE_177A5_FGunner.GUN_OFFSET_TANGAGE;
			break;

		case 2: // Aft Ventral Gunner
			f1 += CockpitHE_177A5_BGunner.GUN_OFFSET_TANGAGE;
			if (f1 > -3F) {
				f1 = -3F;
				flag = false;
			}
			if (f1 < -80F) {
				f1 = -80F;
				flag = false;
			}
			if (f > 50F) {
				f = 50F;
				flag = false;
			}
			if (f < -40F) {
				f = -40F;
				flag = false;
			}

			// Check if external weapons are in the way of our gun...
			String weaponsName = this.thisWeaponsName;
			if (weaponsName.equals("A51xSC2500")) {
				if (this.FM.CT.Weapons[3][0].haveBullets()) {
					if ((f > -25F) && (f < 25F) && (f1 > -40F)) {
						f1 = -40F;
						flag = false;
					}
				}
			} else if (weaponsName.equals("A51xHs293")) {
				if (this.FM.CT.Weapons[3][0].haveBullets()) {
					if ((f < 42F) && (f1 > -14F)) {
						f1 = -14F;
						flag = false;
					}
					if ((f > -15F) && (f < 15F) && (f1 > -47.5F)) {
						f1 = -47.5F;
						flag = false;
					}
				}
			} else if (weaponsName.equals("A53xHs293")) {
				if (this.FM.CT.Weapons[3][4].haveBullets()) {
					if ((f < 42F) && (f1 > -14F)) {
						f1 = -14F;
						flag = false;
					}
					if ((f > -15F) && (f < 15F) && (f1 > -47.5F)) {
						f1 = -47.5F;
						flag = false;
					}
				}
			} else if (weaponsName.equals("A51xFritzX")) {
				if (this.FM.CT.Weapons[3][0].haveBullets()) {
					if ((f > -20F) && (f < 20F) && (f1 > -27.5F)) {
						f1 = -27.5F;
						flag = false;
					}
				}
			} else if (weaponsName.equals("A53xFritzX")) {
				if (this.FM.CT.Weapons[3][4].haveBullets()) {
					if ((f > -20F) && (f < 20F) && (f1 > -27.5F)) {
						f1 = -27.5F;
						flag = false;
					}
				}
			} else if (weaponsName.equals("A52xLT50")) {
				if (this.FM.CT.Weapons[3][0].haveBullets()) {
					if (f1 > ((-f + 1F) * 2F) - 3F) {
						f1 = ((f - 1F) * -2F) - 3F;
						flag = false;
					}
				}
				if (this.FM.CT.Weapons[3][1].haveBullets()) {
					if (f1 > ((f + 1F) * 2F) - 3F) {
						f1 = ((f + 1F) * 2F) - 3F;
						flag = false;
					}
				}
			} else if (weaponsName.equals("A52xLT50_spread")) {
				if (this.FM.CT.Weapons[3][0].haveBullets()) {
					if (f1 > ((-f + 1F) * 2F) - 3F) {
						f1 = ((f - 1F) * -2F) - 3F;
						flag = false;
					}
				}
				if (this.FM.CT.Weapons[3][3].haveBullets()) {
					if (f1 > ((f + 1F) * 2F) - 3F) {
						f1 = ((f + 1F) * 2F) - 3F;
						flag = false;
					}
				}
			} else if (weaponsName.equals("A54xLT50")) {
				if (this.FM.CT.Weapons[3][2].haveBullets()) {
					if (f1 > ((-f + 1F) * 2F) - 3F) {
						f1 = ((f - 1F) * -2F) - 3F;
						flag = false;
					}
				}
				if (this.FM.CT.Weapons[3][3].haveBullets()) {
					if (f1 > ((f + 1F) * 2F) - 3F) {
						f1 = ((f + 1F) * 2F) - 3F;
						flag = false;
					}
				}
			} else if (weaponsName.equals("A54xLT50_spread")) {
				if (this.FM.CT.Weapons[3][4].haveBullets()) {
					if (f1 > ((-f + 1F) * 2F) - 3F) {
						f1 = ((f - 1F) * -2F) - 3F;
						flag = false;
					}
				}
				if (this.FM.CT.Weapons[3][7].haveBullets()) {
					if (f1 > ((f + 1F) * 2F) - 3F) {
						f1 = ((f + 1F) * 2F) - 3F;
						flag = false;
					}
				}
			}

			f1 -= CockpitHE_177A5_BGunner.GUN_OFFSET_TANGAGE;
			break;

		case 3: // Forward Top Gunner (Remote Turret)
			f1 += CockpitHE_177A5_PGunner.GUN_OFFSET_TANGAGE;
			if (f1 > 87F) {
				f1 = 87F;
				flag = false;
			}
			if (f1 < 0F) {
				f1 = 0F;
				flag = false;
			}
			f2 = Math.abs(f);
			if (f2 < 10F)
				f1 = Math.max(f1, 15F * ((float) Math.cos(Math.PI * f2 / 20F)));
			else if (f2 > 65F && f2 < 85F)
				f1 = Math.max(f1, 5F * ((float) Math.cos(Math.PI * (f2 - 75F) / 20F)));
			else if (f2 > 170F)
				f1 = Math.max(f1, 5F * ((float) Math.cos(Math.PI * (180 - f2) / 20F)));

			f1 -= CockpitHE_177A5_PGunner.GUN_OFFSET_TANGAGE;
			break;

		case 4: // Aft Top Gunner
			f1 += CockpitHE_177A5_TGunner.GUN_OFFSET_TANGAGE;
			if (f1 > 80F) {
				f1 = 80F;
				flag = false;
			}
			if (f1 < -10F) {
				f1 = -10F;
				flag = false;
			}
			f2 = Math.abs(f);
			if (f2 < 10F) {
				f1 = Math.max(f1, 10F * ((float) Math.cos(Math.PI * f2 / 100F) - 1F) + 27F * (float) Math.cos(Math.PI * f2 / 20F));
				flag = false;
			} else if (f2 < 50F) {
				f1 = Math.max(f1, 10F * ((float) Math.cos(Math.PI * f2 / 100F) - 1F));
				flag = false;
			} else if (f2 > 90F && f2 < 98F) {
				f1 = Math.max(f1, 5F + 15F * ((float) Math.cos(Math.PI * (98F - f2) / 16F) - 1F));
				flag = false;
			} else if (f2 >= 98F && f2 < 130F) {
				f1 = Math.max(f1, 5F + 5F * ((float) Math.cos(Math.PI * (98F - f2) / 64F) - 1F));
				flag = false;
			} else if (f2 >= 130F) {
				f1 = Math.max(f1, 10F + 10F * ((float) Math.cos(Math.PI * (180F - f2) / 100F) - 1F));
				flag = false;
			}
			f1 -= CockpitHE_177A5_TGunner.GUN_OFFSET_TANGAGE;
			break;

		case 5: // Rear Gunner
			if (f < -22.5F) {
				f = -22.5F;
				flag = false;
			}
			if (f > 38F) {
				f = 38F;
				flag = false;
			}
			if (f1 > 40F) {
				f1 = 40F;
				flag = false;
			}
			if (f1 < -20F) {
				f1 = -20F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	protected void setVentralGunnerDirection(int i) {
		if (FM.AS.isPilotDead(2)) return;
		switch (i) {
		case VENTRAL_GUNNER_FORWARD:
			hierMesh().chunkSetAngles("Pilot3_D0", 0.0F, 0.0F, 0.0F);
			hierMesh().chunkSetAngles("Pilot3_D1", 0.0F, 0.0F, 0.0F);
			this.FM.turret[1].bIsOperable = this.FM.turret[1].health > 0.0F;
			this.FM.turret[2].bIsOperable = false;
			break;

		case VENTRAL_GUNNER_AFT:
			hierMesh().chunkSetLocate("Pilot3_D0", new float[] { 0.0F, -0.1147F, 0.3522F }, new float[] { 0.0F, 180.0F, 42.5F });
			hierMesh().chunkSetLocate("Pilot3_D1", new float[] { 0.0F, 0.0F, 0.3522F }, new float[] { 0.0F, 180.0F, 20F });
			this.FM.turret[2].bIsOperable = this.FM.turret[2].health > 0.0F;
			this.FM.turret[1].bIsOperable = false;
			break;
		}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 33:
			hitProp(0, j, actor);
			break;

		case 36:
			hitProp(1, j, actor);
			break;

		case 35:
			FM.EI.engines[0].setEngineStuck(actor);
			break;

		case 38:
			FM.EI.engines[1].setEngineStuck(actor);
			break;

		case 11:
			hierMesh().chunkVisible("Wire_D0", false);
			break;

		case 19:
			hierMesh().chunkVisible("Wire_D0", false);
			break;

		case 13:
			killPilot(this, 0);
			killPilot(this, 1);
			killPilot(this, 2);
			killPilot(this, 3);
			killPilot(this, 4);
			killPilot(this, 5);
			killPilot(this, 6);
			return false;
		}
		return super.cutFM(i, j, actor);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor"))
				if (s.endsWith("p1")) {
					if (Aircraft.v1.z > 0.5D)
						getEnergyPastArmor(5D / Aircraft.v1.z, shot);
					else if (Aircraft.v1.x > 0.93969261646270752D)
						getEnergyPastArmor((10D / Aircraft.v1.x) * (double) World.Rnd().nextFloat(1.0F, 1.2F), shot);
				} else if (s.endsWith("p2"))
					getEnergyPastArmor(5D / Math.abs(Aircraft.v1.z), shot);
				else if (s.endsWith("p3a") || s.endsWith("p3b"))
					getEnergyPastArmor((8D / Math.abs(Aircraft.v1.x)) * (double) World.Rnd().nextFloat(1.0F, 1.2F), shot);
				else if (s.endsWith("p4")) {
					if (Aircraft.v1.x > 0.70710676908493042D)
						getEnergyPastArmor((8D / Aircraft.v1.x) * (double) World.Rnd().nextFloat(1.0F, 1.2F), shot);
					else if (Aircraft.v1.x > -0.70710676908493042D)
						getEnergyPastArmor(6F, shot);
				} else if (s.endsWith("o1") || s.endsWith("o2"))
					if (Aircraft.v1.x > 0.70710676908493042D)
						getEnergyPastArmor((8D / Aircraft.v1.x) * (double) World.Rnd().nextFloat(1.0F, 1.2F), shot);
					else
						getEnergyPastArmor(5F, shot);
			if (s.startsWith("xxcontrols")) {
				int i = s.charAt(10) - 48;
				switch (i) {
				default:
					break;

				case 1:
				case 2:
					if (getEnergyPastArmor(1.0F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < 0.12F) {
							FM.AS.setControlsDamage(shot.initiator, 1);
							debuggunnery("Evelator Controls Out..");
						}
						if (World.Rnd().nextFloat() < 0.12F) {
							FM.AS.setControlsDamage(shot.initiator, 2);
							debuggunnery("Rudder Controls Out..");
						}
					}
					break;

				case 3:
				case 4:
					if (getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
						FM.AS.setControlsDamage(shot.initiator, 0);
						debuggunnery("Ailerons Controls Out..");
					}
					break;

				case 5:
					if (getEnergyPastArmor(0.1F, shot) <= 0.0F)
						break;
					if (World.Rnd().nextFloat() < 0.75F) {
						FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
						debuggunnery("*** Engine1 Throttle Controls Out..");
					}
					if (World.Rnd().nextFloat() < 0.45F) {
						FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
						debuggunnery("*** Engine1 Prop Controls Out..");
					}
					break;

				case 6:
					if (getEnergyPastArmor(0.1F, shot) <= 0.0F)
						break;
					if (World.Rnd().nextFloat() < 0.75F) {
						FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
						debuggunnery("*** Engine2 Throttle Controls Out..");
					}
					if (World.Rnd().nextFloat() < 0.45F) {
						FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
						debuggunnery("*** Engine2 Prop Controls Out..");
					}
					break;
				}
			}
			if (s.startsWith("xxspar")) {
				if ((s.endsWith("cf1") || s.endsWith("cf2")) && World.Rnd().nextFloat() < 0.1F && chunkDamageVisible("CF") > 2 && getEnergyPastArmor(19.9F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
					debuggunnery("*** CF Spars Broken in Half..");
					msgCollision(this, "Tail1_D0", "Tail1_D0");
					msgCollision(this, "WingLIn_D0", "WingLIn_D0");
					msgCollision(this, "WingRIn_D0", "WingRIn_D0");
				}
				if ((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && World.Rnd().nextFloat() < 0.1F && chunkDamageVisible("Tail1") > 2
						&& getEnergyPastArmor(19.9F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
					debuggunnery("*** Tail1 Spars Broken in Half..");
					msgCollision(this, "Tail1_D0", "Tail1_D0");
				}
				if ((s.endsWith("k1") || s.endsWith("k2") || s.endsWith("k3")) && World.Rnd().nextFloat() < 0.1F && chunkDamageVisible("Keel1") > 2
						&& getEnergyPastArmor(19.9F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
					debuggunnery("*** Keel1 Spars Damaged..");
					msgCollision(this, "Keel1_D0", "Keel1_D0");
				}
				if ((s.endsWith("li1") || s.endsWith("li2")) && (double) World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLIn") > 2
						&& getEnergyPastArmor(17.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					debuggunnery("*** WingLIn Spars Damaged..");
					nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if ((s.endsWith("ri1") || s.endsWith("ri2")) && (double) World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingRIn") > 2
						&& getEnergyPastArmor(17.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					debuggunnery("*** WingRIn Spars Damaged..");
					nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if ((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && (double) World.Rnd().nextFloat() < 1.0D - 0.86D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLMid") > 2
						&& getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					debuggunnery("*** WingLMid Spars Damaged..");
					nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if ((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && (double) World.Rnd().nextFloat() < 1.0D - 0.86D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingRMid") > 2
						&& getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					debuggunnery("*** WingRMid Spars Damaged..");
					nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if ((s.endsWith("lo1") || s.endsWith("lo2") || s.endsWith("lo3") || s.endsWith("lo4")) && (double) World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLOut") > 2
						&& getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					debuggunnery("*** WingLOut Spars Damaged..");
					nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if ((s.endsWith("ro1") || s.endsWith("ro2") || s.endsWith("ro3") || s.endsWith("ro4")) && (double) World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingROut") > 2
						&& getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					debuggunnery("*** WingROut Spars Damaged..");
					nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				if (s.endsWith("e1") && (point3d.y > 2.79D || point3d.y < 2.32D) && getEnergyPastArmor(18F, shot) > 0.0F) {
					debuggunnery("*** Engine1 Suspension Broken in Half..");
					nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
				}
				if (s.endsWith("e2") && (point3d.y < -2.79D || point3d.y > -2.32D) && getEnergyPastArmor(18F, shot) > 0.0F) {
					debuggunnery("*** Engine2 Suspension Broken in Half..");
					nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
				}
			}
			if (s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.01F && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0].haveBullets()) {
				debuggunnery("*** Bomb Payload Detonates..");
				FM.AS.hitTank(shot.initiator, 0, 100);
				FM.AS.hitTank(shot.initiator, 1, 100);
				FM.AS.hitTank(shot.initiator, 2, 100);
				FM.AS.hitTank(shot.initiator, 3, 100);
				msgCollision(this, "CF_D0", "CF_D0");
			}
			if (s.startsWith("xxprop")) {
				int j = 0;
				if (s.endsWith("2"))
					j = 1;
				if (getEnergyPastArmor(2.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.35F) {
					FM.AS.setEngineSpecificDamage(shot.initiator, j, 3);
					debuggunnery("*** Engine" + (j + 1) + " Governor Failed..");
				}
			}
			if (s.startsWith("xxeng1")) {
				int k = 0;
				if (s.startsWith("xxeng2"))
					k = 1;
				if (s.endsWith("case")) {
					if (getEnergyPastArmor(0.2F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 140000F) {
							FM.AS.setEngineStuck(shot.initiator, k);
							debuggunnery("*** Engine" + (k + 1) + " Crank Case Hit - Engine Stucks..");
						}
						if (World.Rnd().nextFloat() < shot.power / 85000F) {
							FM.AS.hitEngine(shot.initiator, k, 2);
							debuggunnery("*** Engine" + (k + 1) + " Crank Case Hit - Engine Damaged..");
						}
					}
				} else if (s.endsWith("cyls")) {
					if (getEnergyPastArmor(1.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[k].getCylindersRatio() * 0.5F) {
						FM.EI.engines[k].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
						debuggunnery("*** Engine" + (k + 1) + " Cylinders Hit, " + FM.EI.engines[k].getCylindersOperable() + "/" + FM.EI.engines[k].getCylinders() + " Left..");
						if (FM.AS.astateEngineStates[k] < 1) {
							FM.AS.hitEngine(shot.initiator, k, 1);
							FM.AS.doSetEngineState(shot.initiator, k, 1);
						}
						if (World.Rnd().nextFloat() < shot.power / 960000F) {
							FM.AS.hitEngine(shot.initiator, k, 3);
							debuggunnery("*** Engine" + (k + 1) + " Cylinders Hit - Engine Fires..");
						}
						getEnergyPastArmor(25F, shot);
					}
				} else if (s.endsWith("sup") && getEnergyPastArmor(0.05F, shot) > 0.0F && World.Rnd().nextFloat() < 0.89F) {
					FM.AS.setEngineSpecificDamage(shot.initiator, k, 0);
					debuggunnery("*** Engine" + (k + 1) + " Supercharger Out..");
				}
			}
			if (s.startsWith("xxoil")) {
				int l = 0;
				if (s.endsWith("2"))
					l = 1;
				if (getEnergyPastArmor(0.21F, shot) > 0.0F) {
					FM.AS.hitOil(shot.initiator, l);
					getEnergyPastArmor(0.42F, shot);
				}
			}
			if (s.startsWith("xxtank")) {
				int i1 = s.charAt(6) - 49;
				if (i1 < 4 && getEnergyPastArmor(1.8F, shot) > 0.0F)
					if (shot.power < 16100F) {
						if (FM.AS.astateTankStates[i1] < 1)
							FM.AS.hitTank(shot.initiator, i1, 1);
						if (FM.AS.astateTankStates[i1] < 4 && World.Rnd().nextFloat() < 0.12F)
							FM.AS.hitTank(shot.initiator, i1, 1);
						if (shot.powerType == 3 && FM.AS.astateTankStates[i1] > 0 && World.Rnd().nextFloat() < 0.04F)
							FM.AS.hitTank(shot.initiator, i1, 10);
					} else {
						FM.AS.hitTank(shot.initiator, i1, World.Rnd().nextInt(0, (int) (shot.power / 16100F)));
					}
			}
			if (s.startsWith("xxlock")) {
				if (s.startsWith("xxlockr") && (s.startsWith("xxlockr1") || s.startsWith("xxlockr2")) && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
					nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
				if (s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
					nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
				if (s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
					nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
				if (s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
					nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
				if (s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					debuggunnery("Lock Construction: AroneR Lock Shot Off..");
					nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
				}
			}
		}
		if (s.startsWith("xoil")) {
			if (s.equals("xoil1")) {
				FM.AS.hitOil(shot.initiator, 0);
				s = "xengine1";
			}
			if (s.equals("xoil2")) {
				FM.AS.hitOil(shot.initiator, 1);
				s = "xengine2";
			}
		}
		if (s.startsWith("xcf")) {
			if (chunkDamageVisible("CF") < 3)
				hitChunk("CF", shot);
		} else if (s.startsWith("xtail")) {
			if (chunkDamageVisible("Tail1") < 3)
				hitChunk("Tail1", shot);
		} else if (s.startsWith("xkeel")) {
			if (chunkDamageVisible("Keel1") < 2)
				hitChunk("Keel1", shot);
		} else if (s.startsWith("xrudder"))
			hitChunk("Rudder1", shot);
		else if (s.startsWith("xstabl"))
			hitChunk("StabL", shot);
		else if (s.startsWith("xstabr"))
			hitChunk("StabR", shot);
		else if (s.startsWith("xvatorl"))
			hitChunk("VatorL", shot);
		else if (s.startsWith("xvatorr"))
			hitChunk("VatorR", shot);
		else if (s.startsWith("xwinglin")) {
			if (chunkDamageVisible("WingLIn") < 2)
				hitChunk("WingLIn", shot);
		} else if (s.startsWith("xwingrin")) {
			if (chunkDamageVisible("WingRIn") < 2)
				hitChunk("WingRIn", shot);
		} else if (s.startsWith("xwinglmid")) {
			if (chunkDamageVisible("WingLMid") < 2)
				hitChunk("WingLMid", shot);
		} else if (s.startsWith("xwingrmid")) {
			if (chunkDamageVisible("WingRMid") < 2)
				hitChunk("WingRMid", shot);
		} else if (s.startsWith("xwinglout")) {
			if (chunkDamageVisible("WingLOut") < 2)
				hitChunk("WingLOut", shot);
		} else if (s.startsWith("xwingrout")) {
			if (chunkDamageVisible("WingROut") < 2)
				hitChunk("WingROut", shot);
		} else if (s.startsWith("xaronel"))
			hitChunk("AroneL", shot);
		else if (s.startsWith("xaroner"))
			hitChunk("AroneR", shot);
		else if (s.startsWith("xengine1")) {
			if (chunkDamageVisible("Engine1") < 2)
				hitChunk("Engine1", shot);
		} else if (s.startsWith("xengine2")) {
			if (chunkDamageVisible("Engine2") < 2)
				hitChunk("Engine2", shot);
		} else if (s.startsWith("xgear")) {
			if (World.Rnd().nextFloat() < 0.1F) {
				debuggunnery("*** Gear Hydro Failed..");
				FM.Gears.setHydroOperable(false);
			}
		} else if (s.startsWith("xturret")) {
			if (s.startsWith("xturret1"))
				FM.AS.setJamBullets(10, 0);
			if (s.startsWith("xturret2"))
				FM.AS.setJamBullets(11, 0);
			if (s.startsWith("xturret3"))
				FM.AS.setJamBullets(12, 0);
			if (s.startsWith("xturret4"))
				FM.AS.setJamBullets(13, 0);
			if (s.startsWith("xturret5"))
				FM.AS.setJamBullets(14, 0);
			if (s.startsWith("xturret6"))
				FM.AS.setJamBullets(15, 0);
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
//			System.out.println("hitBone " + s);
			byte byte0 = 0;
			int j1;
			if (s.endsWith("a")) {
				byte0 = 1;
				j1 = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				j1 = s.charAt(6) - 49;
			} else {
				j1 = s.charAt(5) - 49;
			}
			hitFlesh(j1, shot, byte0);
		}
	}

	public void doWoundPilot(int i, float f) {
//		System.out.println("doWoundPilot " + i);
		switch (i) {
		case 1: // Front Gunner
			FM.turret[0].setHealth(f);
			break;

		case 2: // Ventral Gunners
			FM.turret[1].setHealth(f);
			FM.turret[2].setHealth(f);
			break;

		case 3: // Remote Turret Gunner
			FM.turret[3].setHealth(f);
			break;

		case 4: // Upper Gunner
			FM.turret[4].setHealth(f);
			break;

		case 5: // Rear Gunner
			FM.turret[5].setHealth(f);
			break;
		}
	}

	public void doMurderPilot(int i) {
//		System.out.println("doMurderPilot(" + i + ")");
		hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
		hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
		hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
		if (i == 0)
			hierMesh().chunkVisible("Head1_D0", false);
	}

	public boolean typeBomberToggleAutomation() {
		bSightAutomation = !bSightAutomation;
		bSightBombDump = false;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (bSightAutomation ? "ON" : "OFF"));
		return bSightAutomation;
	}

	public void typeBomberAdjDistanceReset() {
		fSightCurDistance = 0.0F;
		fSightCurForwardAngle = 0.0F;
	}

	public void typeBomberAdjDistancePlus() {
		fSightCurForwardAngle++;
		if (fSightCurForwardAngle > 85F)
			fSightCurForwardAngle = 85F;
		fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) fSightCurForwardAngle) });
		if (bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjDistanceMinus() {
		fSightCurForwardAngle--;
		if (fSightCurForwardAngle < 0.0F)
			fSightCurForwardAngle = 0.0F;
		fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) fSightCurForwardAngle) });
		if (bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjSideslipReset() {
		fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus() {
		fSightCurSideslip += 0.05F;
		if (fSightCurSideslip > 3F)
			fSightCurSideslip = 3F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(fSightCurSideslip * 10F) });
	}

	public void typeBomberAdjSideslipMinus() {
		fSightCurSideslip -= 0.05F;
		if (fSightCurSideslip < -3F)
			fSightCurSideslip = -3F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(fSightCurSideslip * 10F) });
	}

	public void typeBomberAdjAltitudeReset() {
		fSightCurAltitude = 850F;
	}

	public void typeBomberAdjAltitudePlus() {
		fSightCurAltitude += 10F;
		if (fSightCurAltitude > 10000F)
			fSightCurAltitude = 10000F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
		fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
	}

	public void typeBomberAdjAltitudeMinus() {
		fSightCurAltitude -= 10F;
		if (fSightCurAltitude < 850F)
			fSightCurAltitude = 850F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
		fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
	}

	public void typeBomberAdjSpeedReset() {
		fSightCurSpeed = 150F;
	}

	public void typeBomberAdjSpeedPlus() {
		fSightCurSpeed += 10F;
		if (fSightCurSpeed > 600F)
			fSightCurSpeed = 600F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus() {
		fSightCurSpeed -= 10F;
		if (fSightCurSpeed < 150F)
			fSightCurSpeed = 150F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
	}

	public void typeBomberUpdate(float f) {
		if ((double) Math.abs(FM.Or.getKren()) > 4.5D) {
			fSightCurReadyness -= 0.0666666F * f;
			if (fSightCurReadyness < 0.0F)
				fSightCurReadyness = 0.0F;
		}
		if (fSightCurReadyness < 1.0F)
			fSightCurReadyness += 0.0333333F * f;
		else if (bSightAutomation) {
			fSightCurDistance -= (fSightCurSpeed / 3.6F) * f;
			if (fSightCurDistance < 0.0F) {
				fSightCurDistance = 0.0F;
				typeBomberToggleAutomation();
			}
			fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(fSightCurDistance / fSightCurAltitude));
			calibDistance = (fSightCurSpeed / 3.6F) * CrossVersion.floatindex(Aircraft.cvt(fSightCurAltitude, 0.0F, 7000F, 0.0F, 7F), calibrationScale);
			if ((double) fSightCurDistance < (double) calibDistance + (double) (fSightCurSpeed / 3.6F) * Math.sqrt(fSightCurAltitude * (2F / 9.81F)))
				bSightBombDump = true;
			if (bSightBombDump)
				if (FM.isTick(3, 0)) {
					if (FM.CT.Weapons[3] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets()) {
						FM.CT.WeaponControl[3] = true;
						HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
					}
				} else {
					FM.CT.WeaponControl[3] = false;
				}
		}
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		netmsgguaranted.writeByte((bSightAutomation ? 1 : 0) | (bSightBombDump ? 2 : 0));
		netmsgguaranted.writeFloat(fSightCurDistance);
		netmsgguaranted.writeByte((int) fSightCurForwardAngle);
		netmsgguaranted.writeByte((int) ((fSightCurSideslip + 3F) * 33.33333F));
		netmsgguaranted.writeFloat(fSightCurAltitude);
		netmsgguaranted.writeByte((int) (fSightCurSpeed / 2.5F));
		netmsgguaranted.writeByte((int) (fSightCurReadyness * 200F));
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		int i = netmsginput.readUnsignedByte();
		bSightAutomation = (i & 1) != 0;
		bSightBombDump = (i & 2) != 0;
		fSightCurDistance = netmsginput.readFloat();
		fSightCurForwardAngle = netmsginput.readUnsignedByte();
		fSightCurSideslip = -3F + (float) netmsginput.readUnsignedByte() / 33.33333F;
		fSightCurAltitude = netmsginput.readFloat();
		fSightCurSpeed = (float) netmsginput.readUnsignedByte() * 2.5F;
		fSightCurReadyness = (float) netmsginput.readUnsignedByte() / 200F;
	}

	public float getBombBayPos() {
		return bombBayPos;
	}

	public void setBombBayPos(float theBombBayPos) {
		this.bombBayPos = theBombBayPos;
	}
	
	public String getWeaponsName() {
		return this.thisWeaponsName;
	}
	
	private void initFlapDrag() {
		try {
			Class fmClass = this.FM.getClass();
			while (fmClass != null && fmClass.getName().compareTo("com.maddox.il2.fm.FlightModelMain") != 0 )
				fmClass = fmClass.getSuperclass(); // make our way back through class inheritance
			Field wingField = fmClass.getDeclaredField("Wing");
			wingField.setAccessible(true);
			this.theWing = (Polares)wingField.get(this.FM);
			this.CxMin_1_original = this.theWing.CxMin_1;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void flapDrag() {
		float fCxMin_1_factor = 0.33F;
		if (this.FM.CT.getFlap() > 0.5F) fCxMin_1_factor += Math.pow(this.FM.CT.getFlap() + 0.5F, 2F);
//		HUD.training("fD " + this.FM.CT.getFlap() + " : " + fCxMin_1_factor);
		this.theWing.CxMin_1 = this.CxMin_1_original * fCxMin_1_factor ;
	}
	
	private float bombBayPos;

	public boolean bPitUnfocused;
	public boolean bSightAutomation;
	private boolean bSightBombDump;
	public float fSightCurDistance;
	public float fSightCurForwardAngle;
	public float fSightCurSideslip;
	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurReadyness;
	private float calibDistance;
	static final float calibrationScale[] = { 0.0F, 0.2F, 0.4F, 0.66F, 0.86F, 1.05F, 1.2F, 1.6F };
	private float flapps[] = { 0.0F, 0.0F };
	private Polares theWing;
	private float CxMin_1_original;
	
	private static float fShakeThreshold = 0.2F;    // Shake Threshold, apply no shake if shake level would be lower than this value
	private static float fMaxShake = 0.2F;          // Maximum Shake Level
	private static float fStartupShakeLevel = 0.8F; // Max. Startup Shake Level in range 0.0F - 1.0F
	private float[] fEngineShakeLevel = null;       // Array of current shake levels per engine

	long lastEngineStatusChange[];
	boolean engineStatus[];
	long ventralGunnerTargetCheckTime;
	protected static final int VENTRAL_GUNNER_FORWARD = 0;
	protected static final int VENTRAL_GUNNER_AFT = 1;

//	private float oldctl[] = { -1F, -1F };
//	private float curctl[] = { -1F, -1F };

	static {
		Class class1 = HE_177_MOD.class;
		Property.set(class1, "originCountry", PaintScheme.countryGermany);
	}
}
