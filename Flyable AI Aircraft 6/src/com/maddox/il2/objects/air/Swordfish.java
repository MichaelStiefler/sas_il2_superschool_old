// This file is part of the SAS IL-2 Sturmovik 1946 4.13
// Flyable AI Aircraft Mod package.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Original file source: 1C/Maddox/TD
// Modified by: SAS - Special Aircraft Services
//              www.sas1946.com
//
// Last Edited by: SAS~Storebror
// Last Edited on: 2014/10/28

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public abstract class Swordfish extends Scheme1 implements TypeBomber, TypeStormovik, TypeScout {

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		hierMesh().chunkSetAngles("TurrBase_D0", 0.0F, 70F, 0.0F);
		hierMesh().chunkSetAngles("TurrBase1_D0", 70F, 0.0F, 0.0F);
		FM.Gears.computePlaneLandPose(FM);
		if (thisWeaponsName.startsWith("1_")) {
			hierMesh().chunkVisible("Torpedo_Support_D0", true);
			return;
		}
		if (thisWeaponsName.startsWith("2_")) {
			hierMesh().chunkVisible("500lbWingRackC_D0", true);
			hierMesh().chunkVisible("500lbWingRackL_D0", true);
			hierMesh().chunkVisible("500lbWingRackR_D0", true);
			return;
		}
		if (thisWeaponsName.startsWith("3_")) {
			hierMesh().chunkVisible("500lbWingRackC_D0 ", true);
			hierMesh().chunkVisible("250lbWingRackL01_D0", true);
			hierMesh().chunkVisible("250lbWingRackL02_D0", true);
			hierMesh().chunkVisible("250lbWingRackR01_D0", true);
			hierMesh().chunkVisible("250lbWingRackR02_D0", true);
			return;
		}
		if (thisWeaponsName.startsWith("4_")) {
			hierMesh().chunkVisible("500lbWingRackC_D0", true);
			hierMesh().chunkVisible("FlareWingRackL_D0", true);
			hierMesh().chunkVisible("FlareWingRackR_D0", true);
		}
		if (thisWeaponsName.startsWith("5_")) {
			hierMesh().chunkVisible("500lbWingRackC_D0", true);
			hierMesh().chunkVisible("500lbWingRackL_D0", true);
			hierMesh().chunkVisible("500lbWingRackR_D0", true);
			hierMesh().chunkVisible("FlareWingRackL_D0", true);
			hierMesh().chunkVisible("FlareWingRackR_D0", true);
		}
		if (thisWeaponsName.startsWith("6_")) {
			hierMesh().chunkVisible("Torpedo_Support_D0", true);
			hierMesh().chunkVisible("FlareWingRackL_D0", true);
			hierMesh().chunkVisible("FlareWingRackR_D0", true);
		}
		if (thisWeaponsName.startsWith("7_")) {
			hierMesh().chunkVisible("500lbWingRackC_D0 ", true);
			hierMesh().chunkVisible("250lbWingRackL01_D0", true);
			hierMesh().chunkVisible("250lbWingRackL02_D0", true);
			hierMesh().chunkVisible("250lbWingRackR01_D0", true);
			hierMesh().chunkVisible("250lbWingRackR02_D0", true);
			hierMesh().chunkVisible("FlareWingRackL_D0", true);
			hierMesh().chunkVisible("FlareWingRackR_D0", true);
		}
		if (thisWeaponsName.startsWith("8_")) {
			hierMesh().chunkVisible("500lbWingRackC_D0 ", true);
			hierMesh().chunkVisible("FlareWingRackL_D0", true);
			hierMesh().chunkVisible("FlareWingRackR_D0", true);
		}
	}

	public Swordfish() {
		bPitUnfocused = true;
		bIsWingTornOff = false;
		airBrakePos = 0.0F;
		arrestor = 0.0F;
		obsLookoutTimeLeft = 2.0F;
		obsLookoutAz = 0.0F;
		obsLookoutEl = 0.0F;
		obsLookoutPos = new float[3][129];
		wheel1 = 0.0F;
		wheel2 = 0.0F;
		slat = 0.0F;
		noenemy = 0;
		wait = 0;
		obsLookTime = 0;
		obsLookAzimuth = 0.0F;
		obsLookElevation = 0.0F;
		obsAzimuth = 0.0F;
		obsElevation = 0.0F;
		obsAzimuthOld = 0.0F;
		obsElevationOld = 0.0F;
		obsMove = 0.0F;
		obsMoveTot = 0.0F;
		TAGLookTime = 0;
		TAGLookAzimuth = 0.0F;
		TAGLookElevation = 0.0F;
		TAGAzimuth = 0.0F;
		TAGElevation = 0.0F;
		TAGAzimuthOld = 0.0F;
		TAGElevationOld = 0.0F;
		TAGMove = 0.0F;
		TAGMoveTot = 0.0F;
		bTAGKilled = false;
		bObserverKilled = false;
		bChangedPit = true;
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	public void sfxAirBrake() {
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
		case 2: // '\002'
			FM.turret[0].setHealth(f);
			break;
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("Head1_D0", false);
			hierMesh().chunkVisible("HMask1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			break;

		case 1: // '\001'
			hierMesh().chunkVisible("Pilot2_D0", false);
			hierMesh().chunkVisible("Head2_D0", false);
			hierMesh().chunkVisible("HMask2_D0", false);
			hierMesh().chunkVisible("Pilot2_D1", true);
			bObserverKilled = true;
			break;

		case 2: // '\002'
			hierMesh().chunkVisible("Pilot3_D0", false);
			hierMesh().chunkVisible("Pilot3up_D0", false);
			hierMesh().chunkVisible("Head3_D0", false);
			hierMesh().chunkVisible("HMask3_D0", false);
			hierMesh().chunkVisible("Pilot3_D1", true);
			bTAGKilled = true;
			break;
		}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (!bTAGKilled) {
			Actor actor = War.GetNearestEnemy(this, 16, 6000F);
			Aircraft aircraft = War.getNearestEnemy(this, 5000F);
			if (actor != null && !(actor instanceof BridgeSegment) || aircraft != null || !this.FM.turret[0].bIsAIControlled) {
				noenemy = 0;
				if (FM.CT.AirBrakeControl < 0.01F) {
					wait = World.Rnd().nextInt(0, 30);
					FM.CT.AirBrakeControl = 1.0F;
				}
			} else {
				noenemy++;
				if (noenemy > 30 + wait && FM.CT.AirBrakeControl > 0.99F)
					FM.CT.AirBrakeControl = 0.0F;
			}
		}
		if (!bObserverKilled)
			if (obsLookTime == 0) {
				obsLookTime = 2 + World.Rnd().nextInt(1, 3);
				obsMoveTot = 1.0F + World.Rnd().nextFloat() * 1.5F;
				obsMove = 0.0F;
				obsAzimuthOld = obsAzimuth;
				obsElevationOld = obsElevation;
				if ((double) World.Rnd().nextFloat() > 0.80000000000000004D) {
					obsAzimuth = 0.0F;
					obsElevation = 0.0F;
				} else {
					obsAzimuth = World.Rnd().nextFloat() * 140F - 70F;
					obsElevation = World.Rnd().nextFloat() * 50F - 20F;
				}
			} else {
				obsLookTime--;
			}
		if (!bTAGKilled)
			if (TAGLookTime == 0) {
				TAGLookTime = 2 + World.Rnd().nextInt(1, 3);
				TAGMoveTot = 1.0F + World.Rnd().nextFloat() * 1.5F;
				TAGMove = 0.0F;
				TAGAzimuthOld = TAGAzimuth;
				TAGElevationOld = TAGElevation;
				if ((double) World.Rnd().nextFloat() > 0.80000000000000004D) {
					TAGAzimuth = 0.0F;
					TAGElevation = 0.0F;
				} else {
					TAGAzimuth = World.Rnd().nextFloat() * 140F - 70F;
					TAGElevation = World.Rnd().nextFloat() * 50F - 20F;
				}
			} else {
				TAGLookTime--;
			}
		if (FM.getAltitude() < 3000F) {
			hierMesh().chunkVisible("HMask1_D0", false);
			hierMesh().chunkVisible("HMask2_D0", false);
			hierMesh().chunkVisible("HMask3_D0", false);
		} else {
			hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
			hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Pilot2_D0"));
			hierMesh().chunkVisible("HMask3_D0", hierMesh().isChunkVisible("Pilot3_D0"));
		}
		if (flag) {
			if (FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.39F)
				FM.AS.hitTank(this, 0, 1);
			if (FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.1F)
				nextDMGLevel(FM.AS.astateEffectChunks[0] + "0", 0, this);
			if (FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.1F)
				nextDMGLevel(FM.AS.astateEffectChunks[1] + "0", 0, this);
			if (FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.1F)
				nextDMGLevel(FM.AS.astateEffectChunks[2] + "0", 0, this);
			if (FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.1F)
				nextDMGLevel(FM.AS.astateEffectChunks[3] + "0", 0, this);
		}
	}

	public void moveAirBrake(float f) {
		airBrakePos = f;
		if (bTAGKilled)
			return;
		hierMesh().chunkSetAngles("TurrBase_D0", 0.0F, 70F * (1.0F - f), 0.0F);
		hierMesh().chunkSetAngles("TurrBase1_D0", 70F * (1.0F - f), 0.0F, 0.0F);
		noenemy = 0;
		wait = World.Rnd().nextInt(0, 30);
		if ((double) f > 0.98999999999999999D) {
			resetYPRmodifier();
			hierMesh().chunkSetLocate("Pilot3_D0", Aircraft.xyz, Aircraft.ypr);
			hierMesh().chunkVisible("Pilot3_D0", false);
			hierMesh().chunkVisible("Head3_D0", false);
			hierMesh().chunkVisible("Pilot3up_D0", true);
		} else {
			if (!hierMesh().isChunkVisible("Pilot3_D0")) {
				hierMesh().chunkVisible("Pilot3_D0", true);
				hierMesh().chunkVisible("Head3_D0", true);
				hierMesh().chunkVisible("Pilot3up_D0", false);
			}
			resetYPRmodifier();
			Aircraft.xyz[2] = 0.45F * f;
			hierMesh().chunkSetLocate("Pilot3_D0", Aircraft.xyz, Aircraft.ypr);
			FM.turret[0].tu[0] = 0.0F;
			FM.turret[0].tu[1] = 0.0F;
		}
	}

	public void moveSteering(float f) {
		hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
	}

	public void moveWheelSink() {
		wheel1 = 0.8F * wheel1 + 0.2F * FM.Gears.gWheelSinking[0];
		wheel2 = 0.8F * wheel2 + 0.2F * FM.Gears.gWheelSinking[1];
		hierMesh().chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(wheel1, 0.0F, 0.04F, 0.0F, 9F), 0.0F);
		hierMesh().chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(wheel1, 0.0F, 0.04F, 0.0F, 5F), 0.0F);
		hierMesh().chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(wheel2, 0.0F, 0.04F, 0.0F, -9F), 0.0F);
		hierMesh().chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(wheel2, 0.0F, 0.04F, 0.0F, -5F), 0.0F);
	}

	public void update(float f) {
		Controls controls = FM.CT;
		float f3 = controls.getFlap();
		if (FM.CT.getArrestor() > 0.2F)
			if (FM.Gears.arrestorVAngle != 0.0F) {
				float f4 = Aircraft.cvt(FM.Gears.arrestorVAngle, -26F, 11F, 1.0F, 0.0F);
				arrestor = 0.8F * arrestor + 0.2F * f4;
				moveArrestorHook(arrestor);
			} else {
				float f5 = (-42F * FM.Gears.arrestorVSink) / 37F;
				if (f5 < 0.0F && FM.getSpeedKMH() > 50F)
					Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
				if (f5 > 0.0F && FM.CT.getArrestor() < 0.95F)
					f5 = 0.0F;
				if (f5 > 0.0F)
					arrestor = 0.7F * arrestor + 0.3F * (arrestor + f5);
				else
					arrestor = 0.3F * arrestor + 0.7F * (arrestor + f5);
				if (arrestor < 0.0F)
					arrestor = 0.0F;
				else if (arrestor > 1.0F)
					arrestor = 1.0F;
				moveArrestorHook(arrestor);
			}
		float f1 = controls.getAileron();
		float f6 = -(f1 * 30F + f3 * 17F);
		hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, f6, 0.0F);
		hierMesh().chunkSetAngles("AroneL2_D0", 0.0F, f6, 0.0F);
		f6 = -(f1 * 30F - f3 * 17F);
		hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, f6, 0.0F);
		hierMesh().chunkSetAngles("AroneR2_D0", 0.0F, f6, 0.0F);
		resetYPRmodifier();
		if (FM.EI.engines[0].getRPM() > 100F)
			slat = 0.96F * slat + 0.04F * Aircraft.cvt(FM.getSpeedKMH(), 80F, 110F, -0.18F, 0.0F);
		else
			slat = 0.995F * slat;
		Aircraft.xyz[1] = slat;
		hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
		hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
		if (FM.AS.isPilotParatrooper(2) && hierMesh().isChunkVisible("Pilot3up_D0"))
			hierMesh().chunkVisible("Pilot3up_D0", false);
		if (obsMove < obsMoveTot && !bObserverKilled && !FM.AS.isPilotParatrooper(1)) {
			if (obsMove < 0.2F || obsMove > obsMoveTot - 0.2F)
				obsMove += 0.29999999999999999D * (double) f;
			else if (obsMove < 0.1F || obsMove > obsMoveTot - 0.1F)
				obsMove += 0.15F;
			else
				obsMove += 1.2D * (double) f;
			obsLookAzimuth = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsAzimuthOld, obsAzimuth);
			obsLookElevation = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsElevationOld, obsElevation);
			hierMesh().chunkSetAngles("Head2_D0", 0.0F, obsLookAzimuth, obsLookElevation);
		}
		if (TAGMove < TAGMoveTot && !bTAGKilled && !FM.AS.isPilotParatrooper(2)) {
			if (TAGMove < 0.2F || TAGMove > TAGMoveTot - 0.2F)
				TAGMove += 0.29999999999999999D * (double) f;
			else if (TAGMove < 0.1F || TAGMove > TAGMoveTot - 0.1F)
				TAGMove += 0.15F;
			else
				TAGMove += 1.2D * (double) f;
			TAGLookAzimuth = Aircraft.cvt(TAGMove, 0.0F, TAGMoveTot, TAGAzimuthOld, TAGAzimuth);
			TAGLookElevation = Aircraft.cvt(TAGMove, 0.0F, TAGMoveTot, TAGElevationOld, TAGElevation);
			hierMesh().chunkSetAngles("Head3_D0", 0.0F, TAGLookAzimuth, TAGLookElevation);
		}
		super.update(f);
	}

	protected void moveAileron(float f) {
		Controls controls = FM.CT;
		float f1 = controls.getFlap();
		float f2 = -(f * 30F + f1 * 17F);
		hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, f2, 0.0F);
		hierMesh().chunkSetAngles("AroneL2_D0", 0.0F, f2, 0.0F);
		f2 = -(f * 30F - f1 * 17F);
		hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, f2, 0.0F);
		hierMesh().chunkSetAngles("AroneR2_D0", 0.0F, f2, 0.0F);
	}

	protected void moveRudder(float f) {
		hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -25F * f, 0.0F);
	}

	protected void moveElevator(float f) {
		if (f < 0.0F) {
			hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -20F * f, 0.0F);
			hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -20F * f, 0.0F);
		} else {
			hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
			hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
		}
	}

	public void moveArrestorHook(float f) {
		hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -52F * f, 0.0F);
		arrestor = f;
	}

	protected void moveWingFold(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("WingLIn_D0", 0.0F, 85F * f, 0.0F);
		hiermesh.chunkSetAngles("WingRIn_D0", 0.0F, -85F * f, 0.0F);
	}

	public void moveWingFold(float f) {
		if (f < 0.001F) {
			setGunPodsOn(true);
			hideWingWeapons(false);
		} else {
			setGunPodsOn(false);
			FM.CT.WeaponControl[0] = false;
			hideWingWeapons(true);
		}
		moveWingFold(hierMesh(), f);
	}

	protected void setControlDamage(Shot shot, int i) {
		if (World.Rnd().nextFloat() < 0.002F && getEnergyPastArmor(4F, shot) > 0.0F)
			FM.AS.setControlsDamage(shot.initiator, i);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				if (s.startsWith("xxarmorp")) {
					int i = s.charAt(8) - 48;
					switch (i) {
					case 2: // '\002'
						getEnergyPastArmor(22.760000228881836D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
						if (shot.power <= 0.0F)
							doRicochetBack(shot);
						break;

					case 3: // '\003'
						getEnergyPastArmor(9.366F, shot);
						break;

					case 5: // '\005'
						getEnergyPastArmor(12.699999809265137D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
						break;
					}
				}
			} else if (s.startsWith("xxspar")) {
				if (s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(6.9600000381469727D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
					nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				if (s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(6.9600000381469727D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
					nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				if (s.startsWith("xxsparlo") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(6.9600000381469727D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
					nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				if (s.startsWith("xxsparro") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(6.9600000381469727D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
					nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				if (s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.86F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
					nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
			} else {
				if (s.startsWith("xxlock")) {
					if (s.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
						nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
					if (s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
						nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
					if (s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
						nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
				}
				if (s.startsWith("xxeng")) {
					if ((s.endsWith("prop") || s.endsWith("pipe")) && getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
						FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
					if (s.endsWith("case") || s.endsWith("gear")) {
						if (getEnergyPastArmor(0.2F, shot) > 0.0F) {
							if (World.Rnd().nextFloat() < shot.power / 140000F)
								FM.AS.setEngineStuck(shot.initiator, 0);
							else if (World.Rnd().nextFloat() < shot.power / 85000F)
								FM.AS.hitEngine(shot.initiator, 0, 2);
							else
								FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - 0.002F);
						} else if (World.Rnd().nextFloat() < 0.05F)
							FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
						else
							FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - 0.002F);
						getEnergyPastArmor(12F, shot);
					}
					if (s.endsWith("cyls")) {
						if (getEnergyPastArmor(6.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 0.75F) {
							FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
							if (World.Rnd().nextFloat() < shot.power / 48000F)
								FM.AS.hitEngine(shot.initiator, 0, 2);
						}
						getEnergyPastArmor(25F, shot);
					}
					if (s.endsWith("supc") && getEnergyPastArmor(0.05F, shot) > 0.0F) {
						FM.EI.engines[0].setKillCompressor(shot.initiator);
						getEnergyPastArmor(2.0F, shot);
					}
				} else if (s.startsWith("xxoil"))
					FM.AS.hitOil(shot.initiator, 0);
				else if (s.startsWith("xxtank")) {
					int j = s.charAt(6) - 49;
					if (getEnergyPastArmor(0.4F, shot) > 0.0F)
						if (shot.power < 14100F) {
							if (FM.AS.astateTankStates[j] < 1)
								FM.AS.hitTank(shot.initiator, j, 1);
							if (FM.AS.astateTankStates[j] < 4 && World.Rnd().nextFloat() < 0.15F)
								FM.AS.hitTank(shot.initiator, j, 1);
							if (shot.powerType == 3 && FM.AS.astateTankStates[j] > 1 && World.Rnd().nextFloat() < 0.2F)
								FM.AS.hitTank(shot.initiator, j, 10);
						} else {
							FM.AS.hitTank(shot.initiator, j, World.Rnd().nextInt(0, (int) (shot.power / 35000F)));
						}
				} else if (s.startsWith("xxmgun")) {
					if (s.endsWith("01"))
						FM.AS.setJamBullets(0, 0);
					if (s.endsWith("02"))
						FM.AS.setJamBullets(1, 0);
					getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
				}
			}
		} else if (s.startsWith("xcf")) {
			setControlDamage(shot, 0);
			setControlDamage(shot, 1);
			setControlDamage(shot, 2);
			if (chunkDamageVisible("CF") < 3)
				hitChunk("CF", shot);
			if (point3d.x > -2.2000000000000002D) {
				if (World.Rnd().nextFloat() < 0.1F)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
				if (point3d.x < -1D && point3d.z > 0.55000000000000004D)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
				if (point3d.z > 0.65000000000000002D)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
				if (Math.abs(Aircraft.v1.x) < 0.80000001192092896D)
					if (point3d.y > 0.0D) {
						if (World.Rnd().nextFloat() < 0.1F)
							FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
						if (World.Rnd().nextFloat() < 0.1F)
							FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
					} else {
						if (World.Rnd().nextFloat() < 0.1F)
							FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
						if (World.Rnd().nextFloat() < 0.1F)
							FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
					}
			}
		} else if (s.startsWith("xarmorp1")) {
			getEnergyPastArmor(20.760000228881836D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
			if (shot.power <= 0.0F)
				doRicochetBack(shot);
		} else if (s.startsWith("xmgun01")) {
			if (getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 8F), shot) > 0.0F)
				FM.AS.setJamBullets(0, 0);
		} else if (s.startsWith("xmgun02")) {
			if (getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 8F), shot) > 0.0F)
				FM.AS.setJamBullets(0, 1);
		} else if (s.startsWith("xeng")) {
			if (chunkDamageVisible("Engine1") < 2)
				hitChunk("Engine1", shot);
		} else if (s.startsWith("xtail")) {
			setControlDamage(shot, 1);
			setControlDamage(shot, 2);
			if (chunkDamageVisible("Tail1") < 3)
				hitChunk("Tail1", shot);
		} else if (s.startsWith("xkeel"))
			hitChunk("Keel1", shot);
		else if (s.startsWith("xrudder")) {
			setControlDamage(shot, 2);
			if (chunkDamageVisible("Rudder1") < 1)
				hitChunk("Rudder1", shot);
		} else if (s.startsWith("xstab")) {
			if (s.startsWith("xstabl"))
				hitChunk("StabL", shot);
			if (s.startsWith("xstabr"))
				hitChunk("StabR", shot);
		} else if (s.startsWith("xvator")) {
			if (s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1)
				hitChunk("VatorL", shot);
			if (s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1)
				hitChunk("VatorR", shot);
		} else if (s.startsWith("xwing")) {
			if (s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3) {
				setControlDamage(shot, 0);
				hitChunk("WingLIn", shot);
			}
			if (s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3) {
				setControlDamage(shot, 0);
				hitChunk("WingRIn", shot);
			}
			if (s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3) {
				setControlDamage(shot, 0);
				hitChunk("WingLMid", shot);
			}
			if (s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3) {
				setControlDamage(shot, 0);
				hitChunk("WingRMid", shot);
			}
			if (s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
				hitChunk("WingLOut", shot);
			if (s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
				hitChunk("WingROut", shot);
		} else if (s.startsWith("xarone")) {
			if (s.startsWith("xaronel1"))
				hitChunk("AroneL1", shot);
			if (s.startsWith("xaronel2"))
				hitChunk("AroneL2", shot);
			if (s.startsWith("xaroner1"))
				hitChunk("AroneR1", shot);
			if (s.startsWith("xaroner2"))
				hitChunk("AroneR2", shot);
		} else if (s.startsWith("xgearr")) {
			if (World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
				FM.AS.setInternalDamage(shot.initiator, 3);
			hitChunk("GearR2", shot);
		} else if (s.startsWith("xgearl")) {
			if (World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
				FM.AS.setInternalDamage(shot.initiator, 3);
			hitChunk("GearL2", shot);
		} else if (s.startsWith("xradiator")) {
			if (World.Rnd().nextFloat() < 0.12F)
				FM.AS.hitOil(shot.initiator, 0);
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int k;
			if (s.endsWith("a")) {
				byte0 = 1;
				k = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				k = s.charAt(6) - 49;
			} else {
				k = s.charAt(5) - 49;
			}
			hitFlesh(k, shot, byte0);
		}
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = af[0];
		float f1 = af[1];
		switch (i) {
		case 0: // '\0'
			if (f < -70F) {
				f = -70F;
				flag = false;
			}
			if (f > 70F) {
				f = 70F;
				flag = false;
			}
			if (f1 < -45F) {
				f1 = -45F;
				flag = false;
			}
			if (f1 > 70F) {
				f1 = 70F;
				flag = false;
			}
			if ((f > -30F || f < 30F) && f1 < -10F) {
				f1 = -10F;
				flag = false;
			}
			break;
		}
		af[0] = f;
		af[1] = f1;
		return flag;
	}

	public boolean bPitUnfocused;
	boolean bIsWingTornOff;
	public float airBrakePos;
	protected float arrestor;
	float obsLookoutTimeLeft;
	float obsLookoutAz;
	float obsLookoutEl;
	float obsLookoutAnim;
	float obsLookoutMax;
	float obsLookoutAzSpd;
	float obsLookoutElSpd;
	int obsLookoutIndex;
	float obsLookoutPos[][];
	private float wheel1;
	private float wheel2;
	private float slat;
	private int noenemy;
	private int wait;
	private int obsLookTime;
	private float obsLookAzimuth;
	private float obsLookElevation;
	private float obsAzimuth;
	private float obsElevation;
	private float obsAzimuthOld;
	private float obsElevationOld;
	private float obsMove;
	private float obsMoveTot;
	private int TAGLookTime;
	private float TAGLookAzimuth;
	private float TAGLookElevation;
	private float TAGAzimuth;
	private float TAGElevation;
	private float TAGAzimuthOld;
	private float TAGElevationOld;
	private float TAGMove;
	private float TAGMoveTot;
	boolean bTAGKilled;
	boolean bObserverKilled;
	public boolean bChangedPit;

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "originCountry", PaintScheme.countryBritain);
	}
}
