// This file is part of the SAS IL-2 Sturmovik 1946 4.12
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
// Last Edited at: 2013/08/13

package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class B_29X extends Scheme4 implements TypeTransport {

	public B_29X() {
		bChangedPit = true;
		calibDistance = 0.0F;
		bToFire = false;
		deltaAzimuth = 0.0F;
		deltaTangage = 0.0F;
		isGuidingBomb = false;
		bSightAutomation = false;
		bSightBombDump = false;
		fSightCurDistance = 0.0F;
		fSightCurForwardAngle = 0.0F;
		fSightCurSideslip = 0.0F;
		fSightCurAltitude = 3000F;
		fSightCurSpeed = 200F;
		fSightCurReadyness = 0.0F;
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

	public boolean typeGuidedBombCisMasterAlive() {
		return isMasterAlive;
	}

	public void typeGuidedBombCsetMasterAlive(boolean flag) {
		isMasterAlive = flag;
	}

	public boolean typeGuidedBombCgetIsGuiding() {
		return isGuidingBomb;
	}

	public void typeGuidedBombCsetIsGuiding(boolean flag) {
		isGuidingBomb = flag;
	}

	public void typeX4CAdjSidePlus() {
		deltaAzimuth = 0.002F;
	}

	public void typeX4CAdjSideMinus() {
		deltaAzimuth = -0.002F;
	}

	public void typeX4CAdjAttitudePlus() {
		deltaTangage = 0.002F;
	}

	public void typeX4CAdjAttitudeMinus() {
		deltaTangage = -0.002F;
	}

	public void typeX4CResetControls() {
		deltaAzimuth = deltaTangage = 0.0F;
	}

	public float typeX4CgetdeltaAzimuth() {
		return deltaAzimuth;
	}

	public float typeX4CgetdeltaTangage() {
		return deltaTangage;
	}

	private static final float toMeters(float f) {
		return 0.3048F * f;
	}

	private static final float toMetersPerSecond(float f) {
		return 0.4470401F * f;
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
		fSightCurDistance = toMeters(fSightCurAltitude) * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) fSightCurForwardAngle) });
		if (bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjDistanceMinus() {
		fSightCurForwardAngle--;
		if (fSightCurForwardAngle < 0.0F)
			fSightCurForwardAngle = 0.0F;
		fSightCurDistance = toMeters(fSightCurAltitude) * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
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
		fSightCurAltitude = 3000F;
	}

	public void typeBomberAdjAltitudePlus() {
		fSightCurAltitude += 50F;
		if (fSightCurAltitude > 50000F)
			fSightCurAltitude = 50000F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) fSightCurAltitude) });
		fSightCurDistance = toMeters(fSightCurAltitude) * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
	}

	public void typeBomberAdjAltitudeMinus() {
		fSightCurAltitude -= 50F;
		if (fSightCurAltitude < 1000F)
			fSightCurAltitude = 1000F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) fSightCurAltitude) });
		fSightCurDistance = toMeters(fSightCurAltitude) * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
	}

	public void typeBomberAdjSpeedReset() {
		fSightCurSpeed = 200F;
	}

	public void typeBomberAdjSpeedPlus() {
		fSightCurSpeed += 10F;
		if (fSightCurSpeed > 450F)
			fSightCurSpeed = 450F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus() {
		fSightCurSpeed -= 10F;
		if (fSightCurSpeed < 100F)
			fSightCurSpeed = 100F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) fSightCurSpeed) });
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
			fSightCurDistance -= toMetersPerSecond(fSightCurSpeed) * f;
			if (fSightCurDistance < 0.0F) {
				fSightCurDistance = 0.0F;
				typeBomberToggleAutomation();
			}
			fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(fSightCurDistance / toMeters(fSightCurAltitude)));
			calibDistance = toMetersPerSecond(fSightCurSpeed) * AircraftLH.floatindex(Aircraft.cvt(toMeters(fSightCurAltitude), 0.0F, 7000F, 0.0F, 7F), calibrationScale);
			if ((double) fSightCurDistance < (double) calibDistance + (double) toMetersPerSecond(fSightCurSpeed) * Math.sqrt(toMeters(fSightCurAltitude) * 0.2038736F))
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
		if (netmsginput.available() == 0) { // Stock B-29 from Server, no data available
			bSightAutomation = false;
			bSightBombDump = false;
			fSightCurDistance = 0.0F;
			fSightCurForwardAngle = 0.0F;
			fSightCurSideslip = 0.0F;
			fSightCurAltitude = 0.0F;
			fSightCurSpeed = 0.0F;
			fSightCurReadyness = 0.0F;
			return;
		}
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

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.AS.wantBeaconsNet(true);
		this.FM.CT.bHasBayDoorControl = true;
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxammo0")) {
				int i = s.charAt(7) - 48;
				if (getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.15F)
					switch (i) {
					case 3: // '\003'
					default:
						break;

					case 1: // '\001'
						if (World.Rnd().nextFloat() < 0.347F)
							FM.AS.setJamBullets(12, 0);
						if (World.Rnd().nextFloat() < 0.347F)
							FM.AS.setJamBullets(12, 1);
						break;

					case 2: // '\002'
						if (World.Rnd().nextFloat() < 0.347F)
							FM.AS.setJamBullets(13, 0);
						if (World.Rnd().nextFloat() < 0.347F)
							FM.AS.setJamBullets(13, 1);
						break;

					case 4: // '\004'
						if (World.Rnd().nextFloat() < 0.223F)
							FM.AS.setJamBullets(10, 0);
						if (World.Rnd().nextFloat() < 0.223F)
							FM.AS.setJamBullets(10, 1);
						if (World.Rnd().nextFloat() < 0.223F)
							FM.AS.setJamBullets(10, 2);
						if (World.Rnd().nextFloat() < 0.223F)
							FM.AS.setJamBullets(10, 3);
						break;

					case 5: // '\005'
						if (World.Rnd().nextFloat() < 0.347F)
							FM.AS.setJamBullets(11, 0);
						if (World.Rnd().nextFloat() < 0.347F)
							FM.AS.setJamBullets(11, 1);
						break;
					}
				return;
			}
			if (s.startsWith("xxcontrols")) {
				int j = s.charAt(10) - 48;
				switch (j) {
				default:
					break;

				case 1: // '\001'
				case 2: // '\002'
				case 3: // '\003'
					if (getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
						FM.AS.setControlsDamage(shot.initiator, 0);
						debugprintln(this, "*** Aileron Controls Out..");
					}
					break;

				case 4: // '\004'
					if (getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
						FM.AS.setControlsDamage(shot.initiator, 1);
					break;

				case 5: // '\005'
				case 6: // '\006'
					if (getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
						FM.AS.setControlsDamage(shot.initiator, 2);
					break;
				}
				return;
			}
			if (s.startsWith("xxcockpit")) {
				if (World.Rnd().nextFloat() < 0.1F)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
				if (point3d.x > 4.505000114440918D)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
				else if (point3d.y > 0.0D) {
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
					if (World.Rnd().nextFloat() < 0.1F)
						FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
				} else {
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
					if (World.Rnd().nextFloat() < 0.1F)
						FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
				}
			} else if (s.startsWith("xxeng")) {
				int k = s.charAt(5) - 49;
				if (s.endsWith("case")) {
					if (getEnergyPastArmor(0.2F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 140000F) {
							FM.AS.setEngineStuck(shot.initiator, k);
							debugprintln(this, "*** Engine (" + k + ") Crank Case Hit - Engine Stucks..");
						}
						if (World.Rnd().nextFloat() < shot.power / 85000F) {
							FM.AS.hitEngine(shot.initiator, k, 2);
							debugprintln(this, "*** Engine (" + k + ") Crank Case Hit - Engine Damaged..");
						}
					} else if (World.Rnd().nextFloat() < 0.005F) {
						FM.EI.engines[k].setCyliderKnockOut(shot.initiator, 1);
					} else {
						FM.EI.engines[k].setReadyness(shot.initiator, FM.EI.engines[k].getReadyness() - 0.00082F);
						debugprintln(this, "*** Engine (" + k + ") Crank Case Hit - Readyness Reduced to " + FM.EI.engines[k].getReadyness() + "..");
					}
					getEnergyPastArmor(12F, shot);
				}
				if (s.endsWith("cyls")) {
					if (getEnergyPastArmor(5.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[k].getCylindersRatio() * 0.75F) {
						FM.EI.engines[k].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
						debugprintln(this, "*** Engine (" + k + ") Cylinders Hit, " + FM.EI.engines[k].getCylindersOperable() + "/" + FM.EI.engines[k].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 18000F) {
							FM.AS.hitEngine(shot.initiator, k, 2);
							debugprintln(this, "*** Engine (" + k + ") Cylinders Hit - Engine Fires..");
						}
					}
					getEnergyPastArmor(25F, shot);
				}
				if (s.endsWith("mag1")) {
					FM.EI.engines[k].setMagnetoKnockOut(shot.initiator, 0);
					debugprintln(this, "*** Engine (" + k + ") Module: Magneto #0 Destroyed..");
					getEnergyPastArmor(25F, shot);
				}
				if (s.endsWith("prop") && getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
					if (World.Rnd().nextFloat() < 0.5F)
						FM.EI.engines[k].setKillPropAngleDevice(shot.initiator);
					else
						FM.EI.engines[k].setKillPropAngleDeviceSpeeds(shot.initiator);
					getEnergyPastArmor(15.1F, shot);
					debugprintln(this, "*** Engine (" + k + ") Module: Prop Governor Fails..");
				}
				if (s.endsWith("oil") && getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
					FM.AS.setOilState(shot.initiator, k, 1);
					debugprintln(this, "*** Engine (" + k + ") Module: Oil Filter Pierced..");
				}
				return;
			}
			if (s.startsWith("xxspar")) {
				if (s.startsWith("xxspare1") && chunkDamageVisible("Engine1") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					debugprintln(this, "*** Engine1 Spars Damaged..");
					nextDMGLevels(1, 2, "Engine1_D3", shot.initiator);
				}
				if (s.startsWith("xxspare2") && chunkDamageVisible("Engine2") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					debugprintln(this, "*** Engine2 Spars Damaged..");
					nextDMGLevels(1, 2, "Engine2_D3", shot.initiator);
				}
				if (s.startsWith("xxspare3") && chunkDamageVisible("Engine3") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					debugprintln(this, "*** Engine3 Spars Damaged..");
					nextDMGLevels(1, 2, "Engine3_D3", shot.initiator);
				}
				if (s.startsWith("xxspare4") && chunkDamageVisible("Engine4") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					debugprintln(this, "*** Engine4 Spars Damaged..");
					nextDMGLevels(1, 2, "Engine4_D3", shot.initiator);
				}
				if (s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					debugprintln(this, "*** WingLIn Spars Damaged..");
					nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					debugprintln(this, "*** WingRIn Spars Damaged..");
					nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					debugprintln(this, "*** WingLMid Spars Damaged..");
					nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					debugprintln(this, "*** WingRMid Spars Damaged..");
					nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
				}
				if (s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					debugprintln(this, "*** WingLOut Spars Damaged..");
					nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
				}
				if (s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
					debugprintln(this, "*** WingROut Spars Damaged..");
					nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
				}
				if (s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 1 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.125F) {
					debugprintln(this, "*** Keel1 Spars Damaged..");
					nextDMGLevels(1, 2, "Keel1_D" + chunkDamageVisible("Keel1"), shot.initiator);
				}
				if (s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 2 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.125F) {
					debugprintln(this, "*** StabL: Spars Damaged..");
					nextDMGLevels(1, 2, "StabL_D" + chunkDamageVisible("StabL"), shot.initiator);
				}
				if (s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 2 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.125F) {
					debugprintln(this, "*** StabR: Spars Damaged..");
					nextDMGLevels(1, 2, "StabR_D" + chunkDamageVisible("StabR"), shot.initiator);
				}
				if (s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2)
					if (getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) <= 0.125F)
						;
				return;
			}
			if (s.startsWith("xxtank")) {
				int l = s.charAt(6) - 49;
				l /= 2;
				if (getEnergyPastArmor(0.06F, shot) > 0.0F) {
					if (FM.AS.astateTankStates[l] == 0) {
						FM.AS.hitTank(shot.initiator, l, 1);
						FM.AS.doSetTankState(shot.initiator, l, 1);
					}
					if (shot.powerType == 3) {
						if (shot.power < 16100F) {
							if (FM.AS.astateTankStates[l] < 4 && World.Rnd().nextFloat() < 0.21F)
								FM.AS.hitTank(shot.initiator, l, 1);
						} else {
							FM.AS.hitTank(shot.initiator, l, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
						}
					} else if (shot.power > 16100F)
						FM.AS.hitTank(shot.initiator, l, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
				}
				return;
			} else {
				return;
			}
		}
		if (s.startsWith("xcf")) {
			if (chunkDamageVisible("CF") < 3)
				hitChunk("CF", shot);
			return;
		}
		if (s.startsWith("xtail")) {
			if (chunkDamageVisible("Tail1") < 3)
				hitChunk("Tail1", shot);
			return;
		}
		if (s.startsWith("xkeel")) {
			if (chunkDamageVisible("Keel1") < 2)
				hitChunk("Keel1", shot);
			return;
		}
		if (s.startsWith("xrudder")) {
			if (chunkDamageVisible("Rudder1") < 2)
				hitChunk("Rudder1", shot);
			return;
		}
		if (s.startsWith("xstabl")) {
			if (chunkDamageVisible("StabL") < 2)
				hitChunk("StabL", shot);
			return;
		}
		if (s.startsWith("xstabr")) {
			if (chunkDamageVisible("StabR") < 2)
				hitChunk("StabR", shot);
			return;
		}
		if (s.startsWith("xvatorl")) {
			if (chunkDamageVisible("VatorL") < 2)
				hitChunk("VatorL", shot);
			return;
		}
		if (s.startsWith("xvatorr")) {
			if (chunkDamageVisible("VatorR") < 2)
				hitChunk("VatorR", shot);
			return;
		}
		if (s.startsWith("xwinglin")) {
			if (chunkDamageVisible("WingLIn") < 3)
				hitChunk("WingLIn", shot);
			return;
		}
		if (s.startsWith("xwingrin")) {
			if (chunkDamageVisible("WingRIn") < 3)
				hitChunk("WingRIn", shot);
			return;
		}
		if (s.startsWith("xwinglmid")) {
			if (chunkDamageVisible("WingLMid") < 3)
				hitChunk("WingLMid", shot);
			return;
		}
		if (s.startsWith("xwingrmid")) {
			if (chunkDamageVisible("WingRMid") < 3)
				hitChunk("WingRMid", shot);
			return;
		}
		if (s.startsWith("xwinglout")) {
			if (chunkDamageVisible("WingLOut") < 3)
				hitChunk("WingLOut", shot);
			return;
		}
		if (s.startsWith("xwingrout")) {
			if (chunkDamageVisible("WingROut") < 3)
				hitChunk("WingROut", shot);
			return;
		}
		if (s.startsWith("xaronel")) {
			if (chunkDamageVisible("AroneL") < 2)
				hitChunk("AroneL", shot);
			return;
		}
		if (s.startsWith("xaroner")) {
			if (chunkDamageVisible("AroneR") < 2)
				hitChunk("AroneR", shot);
			return;
		}
		if (s.startsWith("xengine1")) {
			if (chunkDamageVisible("Engine1") < 2)
				hitChunk("Engine1", shot);
			return;
		}
		if (s.startsWith("xengine2")) {
			if (chunkDamageVisible("Engine2") < 2)
				hitChunk("Engine2", shot);
			return;
		}
		if (s.startsWith("xengine3")) {
			if (chunkDamageVisible("Engine3") < 2)
				hitChunk("Engine3", shot);
			return;
		}
		if (s.startsWith("xengine4")) {
			if (chunkDamageVisible("Engine4") < 2)
				hitChunk("Engine4", shot);
			return;
		}
		if (s.startsWith("xgear")) {
			if (World.Rnd().nextFloat() < 0.05F) {
				debugprintln(this, "*** Gear Hydro Failed..");
				FM.Gears.setHydroOperable(false);
			}
			return;
		}
		if (s.startsWith("xturret"))
			return;
		if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int i1;
			if (s.endsWith("a")) {
				byte0 = 1;
				i1 = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				i1 = s.charAt(6) - 49;
			} else {
				i1 = s.charAt(5) - 49;
			}
			hitFlesh(i1, shot, byte0);
			return;
		} else {
			return;
		}
	}

	public void msgExplosion(Explosion explosion) {
		setExplosion(explosion);
		if (explosion.chunkName != null && explosion.power > 0.0F) {
			if (explosion.chunkName.equals("Tail1_D3"))
				return;
			if (explosion.chunkName.equals("WingLIn_D3"))
				return;
			if (explosion.chunkName.equals("WingRIn_D3"))
				return;
			if (explosion.chunkName.equals("WingLMid_D3"))
				return;
			if (explosion.chunkName.equals("WingRMid_D3"))
				return;
			if (explosion.chunkName.equals("WingLOut_D3"))
				return;
			if (explosion.chunkName.equals("WingROut_D3"))
				return;
		}
		super.msgExplosion(explosion);
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 91F * f2, 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 0.0F, cvt(f2, 0.01F, 0.06F, 0.0F, 93F));
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 0.0F, cvt(f2, 0.01F, 0.06F, 0.0F, 93F));
		hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 54F * f2, 0.0F);
		hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 108F * f2, 0.0F);
		hiermesh.chunkSetAngles("GearC8_D0", 0.0F, 59F * f2, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -81F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL7_D0", 0.0F, cvt(f, 0.01F, 0.12F, 0.0F, 67F), 0.0F);
		hiermesh.chunkSetAngles("GearL8_D0", 0.0F, cvt(f, 0.01F, 0.12F, 0.0F, -67F), 0.0F);
		hiermesh.chunkSetAngles("GearL9_D0", 0.0F, cvt(f, 0.01F, 0.12F, 0.0F, 84F), 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -81F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearR7_D0", 0.0F, cvt(f1, 0.01F, 0.12F, 0.0F, -67F), 0.0F);
		hiermesh.chunkSetAngles("GearR8_D0", 0.0F, cvt(f1, 0.01F, 0.12F, 0.0F, 67F), 0.0F);
		hiermesh.chunkSetAngles("GearR9_D0", 0.0F, cvt(f1, 0.01F, 0.12F, 0.0F, -84F), 0.0F);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(hierMesh(), f, f1, f2);
	}

	public void moveWheelSink() {
		resetYPRmodifier();
		xyz[1] = cvt(FM.Gears.gWheelSinking[2], 0.0F, 0.325F, 0.0F, 0.325F);
		hierMesh().chunkSetLocate("GearC3_D0", xyz, ypr);
		resetYPRmodifier();
		xyz[0] = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.325F, 0.0F, -0.325F);
		hierMesh().chunkSetLocate("GearL3_D0", xyz, ypr);
		resetYPRmodifier();
		xyz[0] = cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.325F, 0.0F, -0.325F);
		hierMesh().chunkSetLocate("GearR3_D0", xyz, ypr);
	}

	protected void moveRudder(float f) {
		super.moveRudder(f);
		if (FM.CT.getGear() > 0.9F)
			hierMesh().chunkSetAngles("GearC33_D0", 0.0F, -30F * f, 0.0F);
	}

	protected void moveFlap(float f) {
		resetYPRmodifier();
		xyz[0] = -0.4436F * f;
		xyz[2] = 0.063F * f;
		ypr[1] = 30F * f;
		hierMesh().chunkSetLocate("Flap01_D0", xyz, ypr);
		hierMesh().chunkSetLocate("Flap02_D0", xyz, ypr);
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 33: // '!'
			hitProp(1, j, actor);
			FM.EI.engines[1].setEngineStuck(actor);
			FM.AS.hitTank(actor, 1, World.Rnd().nextInt(0, 9));
			// fall through

		case 34: // '"'
			hitProp(0, j, actor);
			FM.EI.engines[0].setEngineStuck(actor);
			FM.AS.hitTank(actor, 0, World.Rnd().nextInt(2, 8));
			FM.AS.hitTank(actor, 1, World.Rnd().nextInt(0, 5));
			// fall through

		case 35: // '#'
			FM.AS.hitTank(actor, 0, World.Rnd().nextInt(0, 4));
			break;

		case 36: // '$'
			hitProp(2, j, actor);
			FM.EI.engines[2].setEngineStuck(actor);
			FM.AS.hitTank(actor, 2, World.Rnd().nextInt(0, 9));
			// fall through

		case 37: // '%'
			hitProp(3, j, actor);
			FM.EI.engines[3].setEngineStuck(actor);
			FM.AS.hitTank(actor, 2, World.Rnd().nextInt(0, 5));
			FM.AS.hitTank(actor, 3, World.Rnd().nextInt(2, 8));
			// fall through

		case 38: // '&'
			FM.AS.hitTank(actor, 3, World.Rnd().nextInt(0, 4));
			break;

		case 25: // '\031'
			FM.turret[0].bIsOperable = false;
			return false;

		case 26: // '\032'
			FM.turret[1].bIsOperable = false;
			return false;

		case 27: // '\033'
			FM.turret[2].bIsOperable = false;
			return false;

		case 28: // '\034'
			FM.turret[3].bIsOperable = false;
			return false;

		case 29: // '\035'
			FM.turret[4].bIsOperable = false;
			return false;

		case 30: // '\036'
			FM.turret[5].bIsOperable = false;
			return false;

		case 19: // '\023'
			killPilot(this, 5);
			killPilot(this, 6);
			killPilot(this, 7);
			killPilot(this, 8);
			break;
		}
		return super.cutFM(i, j, actor);
	}

	protected void moveBayDoor(float f) {
		hierMesh().chunkSetAngles("Bay01_D0", 0.0F, 0.0F, -90F * f);
		hierMesh().chunkSetAngles("Bay02_D0", 0.0F, 0.0F, -90F * f);
		hierMesh().chunkSetAngles("Bay03_D0", 0.0F, 0.0F, -90F * f);
		hierMesh().chunkSetAngles("Bay04_D0", 0.0F, 0.0F, -90F * f);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (flag) {
			if (FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.0023F)
				FM.AS.hitTank(this, 0, 1);
			if (FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.0023F)
				FM.AS.hitTank(this, 1, 1);
			if (FM.AS.astateEngineStates[2] > 3 && World.Rnd().nextFloat() < 0.0023F)
				FM.AS.hitTank(this, 2, 1);
			if (FM.AS.astateEngineStates[3] > 3 && World.Rnd().nextFloat() < 0.0023F)
				FM.AS.hitTank(this, 3, 1);
			if (FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.04F)
				nextDMGLevel(FM.AS.astateEffectChunks[0] + "0", 0, this);
			if (FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.04F)
				nextDMGLevel(FM.AS.astateEffectChunks[1] + "0", 0, this);
			if (FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.04F)
				nextDMGLevel(FM.AS.astateEffectChunks[2] + "0", 0, this);
			if (FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.04F)
				nextDMGLevel(FM.AS.astateEffectChunks[3] + "0", 0, this);
			if (!(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) {
				for (int i = 0; i < FM.EI.getNum(); i++)
					if (FM.AS.astateEngineStates[i] > 3 && World.Rnd().nextFloat() < 0.2F)
						FM.EI.engines[i].setExtinguisherFire();

			}
		}
		for (int j = 1; j < 7; j++)
			if (FM.getAltitude() < 3000F)
				hierMesh().chunkVisible("HMask" + j + "_D0", false);
			else
				hierMesh().chunkVisible("HMask" + j + "_D0", hierMesh().isChunkVisible("Pilot" + j + "_D0"));

	}

	public void update(float f) {
		super.update(f);
		for (int i = 0; i < 4; i++) {
			float f1 = FM.EI.engines[i].getControlRadiator();
			if (Math.abs(flapps[i] - f1) <= 0.01F)
				continue;
			flapps[i] = f1;
			hierMesh().chunkSetAngles("Water" + (i + 1) + "_D0", 0.0F, -10F * f1, 0.0F);
			for (int j = 0; j < 8; j++)
				hierMesh().chunkSetAngles("Water" + (i * 8 + j + 5) + "_D0", 0.0F, 0.0F, -20F * f1);

		}

	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
		case 3: // '\003'
			FM.turret[0].setHealth(f);
			FM.turret[1].setHealth(f);
			break;

		case 4: // '\004'
			FM.turret[2].setHealth(f);
			FM.turret[3].setHealth(f);
			break;

		case 5: // '\005'
			FM.turret[4].setHealth(f);
			break;
		}
	}

	public void doMurderPilot(int i) {
		hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
		hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
		hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
		hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 0: // '\0'
			if (f1 < -1F) {
				f1 = -1F;
				flag = false;
			}
			if (f1 > 80F) {
				f1 = 80F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f1 < -80F) {
				f1 = -80F;
				flag = false;
			}
			if (f1 > 1.0F) {
				f1 = 1.0F;
				flag = false;
			}
			break;

		case 2: // '\002'
			if (f1 < -1F) {
				f1 = -1F;
				flag = false;
			}
			if (f1 > 80F) {
				f1 = 80F;
				flag = false;
			}
			break;

		case 3: // '\003'
			if (f1 < -80F) {
				f1 = -80F;
				flag = false;
			}
			if (f1 > 1.0F) {
				f1 = 1.0F;
				flag = false;
			}
			break;

		case 4: // '\004'
			if (f < -35F) {
				f = -35F;
				flag = false;
			}
			if (f > 35F) {
				f = 35F;
				flag = false;
			}
			if (f1 < -45F) {
				f1 = -45F;
				flag = false;
			}
			if (f1 > 45F) {
				f1 = 45F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public static boolean bChangedPit = false;
	public boolean bToFire;
	protected float deltaAzimuth;
	protected float deltaTangage;
	protected boolean isGuidingBomb;
	protected boolean isMasterAlive;
	private float calibDistance;
	private boolean bSightAutomation;
	private boolean bSightBombDump;
	private float fSightCurDistance;
	public float fSightCurForwardAngle;
	public float fSightCurSideslip;
	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurReadyness;
	private float flapps[] = { 0.0F, 0.0F, 0.0F, 0.0F };
	static final float calibrationScale[] = { 0.0F, 0.2F, 0.4F, 0.66F, 0.86F, 1.05F, 1.2F, 1.6F };

	static {
		Class class1 = B_29X.class;
		Property.set(class1, "originCountry", PaintScheme.countryUSA);
	}
}
