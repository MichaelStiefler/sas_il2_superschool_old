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
// Last Edited on: 2014/12/11

package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.ToKGUtils;
import com.maddox.rts.CLASS;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class HE_177A3 extends HE_177 implements TypeDiveBomber, TypeX4Carrier, TypeGuidedBombCarrier, TypeHasToKG {

	public HE_177A3() {
		this.bPitUnfocused = true;
		this.hasToKG = false;
		this.bombBayPos = 0.0F;
		this.isGuidingBomb = false;
		this.ventralGunnerTargetCheckTime = 0L;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		BulletEmitter abulletemitter[][] = FM.CT.Weapons;
		if (abulletemitter[3] == null)
			return;
		for (int i = 0; i < abulletemitter[3].length; i++) {
			if (!abulletemitter[3][i].haveBullets())
				continue;
			if (abulletemitter[3][i].getHookName().equals("_ExternalBomb00"))
				hierMesh().chunkVisible("PylonC", true);
			if (abulletemitter[3][i].getHookName().equals("_ExternalBomb01"))
				hierMesh().chunkVisible("WingPylonL", true);
			if (abulletemitter[3][i].getHookName().equals("_ExternalBomb02"))
				hierMesh().chunkVisible("WingPylonR", true);
		}

		if (thisWeaponsName.indexOf("Torpedo") != -1) {
			this.hasToKG = true;
		}

		if (thisWeaponsName.indexOf("293") != -1 || thisWeaponsName.indexOf("FritzX") != -1) {
			this.isGuidingBomb = true;
		}

	}

	public void typeBomberAdjAltitudePlus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjAttitudePlus();
			return;
		}
		if (hasToKG) {
			fShipSpeed++;
			if (fShipSpeed > 35F)
				fShipSpeed = 35F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int)fShipSpeed) });
			return;
		}
		super.typeBomberAdjAltitudePlus();
	}

	public void typeBomberAdjAltitudeMinus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjAttitudeMinus();
			return;
		}
		if (hasToKG) {
			fShipSpeed--;
			if (fShipSpeed < 0.0F)
				fShipSpeed = 0.0F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int)fShipSpeed) });
			return;
		}
		super.typeBomberAdjAltitudeMinus();
	}

	public void typeBomberAdjSideslipPlus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjSidePlus();
			return;
		}
		if (hasToKG) {
			fAOB++;
			if (fAOB > 180F)
				fAOB = 180F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGAOB", new Object[] { new Integer((int)fAOB) });
			return;
		}
		fSightCurSideslip += 0.05F;
		if (fSightCurSideslip > 3F)
			fSightCurSideslip = 3F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(fSightCurSideslip * 10F) });
	}

	public void typeBomberAdjSideslipMinus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjSideMinus();
			return;
		}
		if (hasToKG) {
			fAOB--;
			if (fAOB < -180F)
				fAOB = -180F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGAOB", new Object[] { new Integer((int)fAOB) });
			return;
		}
		super.typeBomberAdjSideslipMinus();
	}

	public void typeBomberAdjDistancePlus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjAttitudePlus();
			return;
		}
		if (hasToKG) {
			fShipSpeed++;
			if (fShipSpeed > 35F)
				fShipSpeed = 35F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int)fShipSpeed) });
			return;
		}
		super.typeBomberAdjDistancePlus();
	}

	public void typeBomberAdjDistanceMinus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjAttitudeMinus();
			return;
		}
		if (hasToKG) {
			fShipSpeed--;
			if (fShipSpeed < 0.0F)
				fShipSpeed = 0.0F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int)fShipSpeed) });
			return;
		}
		super.typeBomberAdjDistanceMinus();
	}

	public void typeBomberAdjSpeedPlus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjAttitudePlus();
			return;
		}
		if (hasToKG) {
			fShipSpeed++;
			if (fShipSpeed > 35F)
				fShipSpeed = 35F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int)fShipSpeed) });
			return;
		}
		super.typeBomberAdjSpeedPlus();
	}

	public void typeBomberAdjSpeedMinus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjAttitudeMinus();
			return;
		}
		if (hasToKG) {
			fShipSpeed--;
			if (fShipSpeed < 0.0F)
				fShipSpeed = 0.0F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int)fShipSpeed) });
			return;
		}
		super.typeBomberAdjSpeedMinus();
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

	public boolean typeDiveBomberToggleAutomation() {
		return false;
	}

	public void typeDiveBomberAdjAltitudeReset() {
	}

	public void typeDiveBomberAdjAltitudePlus() {
	}

	public void typeDiveBomberAdjAltitudeMinus() {
	}

	public void typeDiveBomberAdjVelocityReset() {
	}

	public void typeDiveBomberAdjVelocityPlus() {
	}

	public void typeDiveBomberAdjVelocityMinus() {
	}

	public void typeDiveBomberAdjDiveAngleReset() {
	}

	public void typeDiveBomberAdjDiveAnglePlus() {
	}

	public void typeDiveBomberAdjDiveAngleMinus() {
	}

	public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
	}

	public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
	}

	public boolean isSalvo() {
		return false;
	}

	void checkAftVentralGun() {
		if (this.FM.CT.BayDoorControl < 0.5F)
			return;
		float aftVentralGunTangage = this.FM.turret[2].tu[1];
		aftVentralGunTangage = Math.min(aftVentralGunTangage,
				CockpitHE_177_A3_BGunner.GUN_OFFSET_TANGAGE * (this.getBombBayPos() - 1F));
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
			f += CockpitHE_177_A3_NGunner.GUN_OFFSET_YAW;
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
			f -= CockpitHE_177_A3_NGunner.GUN_OFFSET_YAW;
			break;

		case 1: // Forward Ventral Gunner
			f1 += CockpitHE_177_A3_FGunner.GUN_OFFSET_TANGAGE;
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
			f1 -= CockpitHE_177_A3_FGunner.GUN_OFFSET_TANGAGE;
			break;

		case 2: // Aft Ventral Gunner
			f1 += CockpitHE_177_A3_BGunner.GUN_OFFSET_TANGAGE;
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

			f1 -= CockpitHE_177_A3_BGunner.GUN_OFFSET_TANGAGE;
			break;

		case 3: // Forward Top Gunner (Remote Turret)
			f1 += CockpitHE_177_A3_PGunner.GUN_OFFSET_TANGAGE;
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
				f1 = Math.max(f1, 15F * ((float)Math.cos(Math.PI * f2 / 20F)));
			else if (f2 > 65F && f2 < 85F)
				f1 = Math.max(f1, 5F * ((float)Math.cos(Math.PI * (f2 - 75F) / 20F)));
			else if (f2 > 170F)
				f1 = Math.max(f1, 5F * ((float)Math.cos(Math.PI * (180 - f2) / 20F)));

			f1 -= CockpitHE_177_A3_PGunner.GUN_OFFSET_TANGAGE;
			break;

		case 4: // Aft Top Gunner
			f1 += CockpitHE_177_A3_TGunner.GUN_OFFSET_TANGAGE;
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
				f1 = Math
						.max(f1, 10F * ((float)Math.cos(Math.PI * f2 / 100F) - 1F) + 27F * (float)Math.cos(Math.PI * f2 / 20F));
				flag = false;
			} else if (f2 < 50F) {
				f1 = Math.max(f1, 10F * ((float)Math.cos(Math.PI * f2 / 100F) - 1F));
				flag = false;
			} else if (f2 > 90F && f2 < 98F) {
				f1 = Math.max(f1, 5F + 15F * ((float)Math.cos(Math.PI * (98F - f2) / 16F) - 1F));
				flag = false;
			} else if (f2 >= 98F && f2 < 130F) {
				f1 = Math.max(f1, 5F + 5F * ((float)Math.cos(Math.PI * (98F - f2) / 64F) - 1F));
				flag = false;
			} else if (f2 >= 130F) {
				f1 = Math.max(f1, 10F + 10F * ((float)Math.cos(Math.PI * (180F - f2) / 100F) - 1F));
				flag = false;
			}
			f1 -= CockpitHE_177_A3_TGunner.GUN_OFFSET_TANGAGE;
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
		if (FM.AS.isPilotDead(2))
			return;
		switch (i) {
		case VENTRAL_GUNNER_FORWARD:
			hierMesh().chunkSetAngles("Pilot3_D0", 0.0F, 0.0F, 0.0F);
			hierMesh().chunkSetAngles("Pilot3_D1", 0.0F, 0.0F, 0.0F);
			this.FM.turret[1].bIsOperable = this.FM.turret[1].health > 0.0F;
			this.FM.turret[2].bIsOperable = false;
			break;

		case VENTRAL_GUNNER_AFT:
			hierMesh()
					.chunkSetLocate("Pilot3_D0", new float[] { 0.0F, -0.1147F, 0.3522F }, new float[] { 0.0F, 180.0F, 42.5F });
			hierMesh().chunkSetLocate("Pilot3_D1", new float[] { 0.0F, 0.0F, 0.3522F }, new float[] { 0.0F, 180.0F, 20F });
			this.FM.turret[2].bIsOperable = this.FM.turret[2].health > 0.0F;
			this.FM.turret[1].bIsOperable = false;
			break;
		}
	}

	public float getBombBayPos() {
		return bombBayPos;
	}

	public void setBombBayPos(float theBombBayPos) {
		this.bombBayPos = theBombBayPos;
	}

	public boolean bToFire;
	private float deltaAzimuth;
	private float deltaTangage;
	private boolean isMasterAlive;
	public boolean bPitUnfocused;
	private float bombBayPos;
	long ventralGunnerTargetCheckTime;
	public boolean hasToKG;
	protected float fAOB;
	protected float fShipSpeed;
	protected int spreadAngle;
	private boolean isGuidingBomb;
	protected static final int VENTRAL_GUNNER_FORWARD = 0;
	protected static final int VENTRAL_GUNNER_AFT = 1;

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "He-177");
		Property.set(class1, "meshName", "3DO/Plane/He-177A-3/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "yearService", 1942F);
		Property.set(class1, "yearExpired", 1945F);
		Property.set(class1, "FlightModel", "FlightModels/He-177A-3.fmd");
		Property.set(class1, "LOSElevation", 1.0976F);
		Property.set(class1, "cockpitClass", new Class[] { CockpitHE_177.class, CockpitHE_177_Bombardier.class,
				CockpitHE_177_A3_NGunner.class, CockpitHE_177_A3_FGunner.class, CockpitHE_177_A3_BGunner.class,
				CockpitHE_177_A3_PGunner.class, CockpitHE_177_A3_TGunner.class, CockpitHE_177_A3_RGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 13, 14, 15, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07",
				"_BombSpawn00", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06",
				"_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13",
				"_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19",
				"_ExternalBomb00", "_ExternalBomb01", "_ExternalBomb02", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22",
				"_BombSpawn23", "_BombSpawn24", "_BombSpawn25", "_BombSpawn26", "_BombSpawn27", "_BombSpawn28", "_BombSpawn29",
				"_BombSpawn30", "_BombSpawn31", "_BombSpawn32", "_BombSpawn33", "_BombSpawn34", "_BombSpawn35", "_BombSpawn36",
				"_BombSpawn37", "_BombSpawn38", "_BombSpawn39", "_BombSpawn40", "_BombSpawn41", "_BombSpawn42", "_BombSpawn43",
				"_BombSpawn44", "_BombSpawn45", "_BombSpawn46", "_BombSpawn47", "_BombSpawn48", "_BombSpawn49", "_BombSpawn50",
				"_BombSpawn51", "_BombSpawn52", "_BombSpawn53", "_BombSpawn54", "_BombSpawn55", "_BombSpawn56", "_BombSpawn57",
				"_BombSpawn58", "_BombSpawn59", "_BombSpawn60", "_BombSpawn61", "_BombSpawn62", "_BombSpawn63", "_BombSpawn64",
				"_BombSpawn65", "_BombSpawn66", "_BombSpawn67" });
		weaponsRegister(class1, "default", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "12xSC50", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, "BombGunSC50", null, "BombGunSC50", "BombGunSC50", null,
				"BombGunSC50", null, null, null, null, null, null, null, null, null, null, "BombGunSC50", null, "BombGunSC50",
				"BombGunSC50", null, "BombGunSC50", null, null, null, null, null, null, null, null, null, null, "BombGunSC50",
				null, "BombGunSC50", "BombGunSC50", null, "BombGunSC50", null });
		weaponsRegister(class1, "12xSC70", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, "BombGunSC70", null, "BombGunSC70", "BombGunSC70", null,
				"BombGunSC70", null, null, null, null, null, null, null, null, null, null, "BombGunSC70", null, "BombGunSC70",
				"BombGunSC70", null, "BombGunSC70", null, null, null, null, null, null, null, null, null, null, "BombGunSC70",
				null, "BombGunSC70", "BombGunSC70", null, "BombGunSC70", null });
		weaponsRegister(class1, "24xSC50", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50",
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", null, null, null, null, null, null, null, null,
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50",
				"BombGunSC50", null, null, null, null, null, null, null, null, "BombGunSC50", "BombGunSC50", "BombGunSC50",
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50" });
		weaponsRegister(class1, "24xSC70", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70",
				"BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", null, null, null, null, null, null, null, null,
				"BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70",
				"BombGunSC70", null, null, null, null, null, null, null, null, "BombGunSC70", "BombGunSC70", "BombGunSC70",
				"BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70" });
		weaponsRegister(class1, "36xSC50", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				"BombGunSC50", null, "BombGunSC50", "BombGunSC50", null, "BombGunSC50", null, "BombGunSC50", "BombGunSC50",
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", null, "BombGunSC50",
				null, "BombGunSC50", "BombGunSC50", null, "BombGunSC50", null, "BombGunSC50", "BombGunSC50", "BombGunSC50",
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", null, "BombGunSC50", null,
				"BombGunSC50", "BombGunSC50", null, "BombGunSC50", null, "BombGunSC50", "BombGunSC50", "BombGunSC50",
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50" });
		weaponsRegister(class1, "36xSC70", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				"BombGunSC70", null, "BombGunSC70", "BombGunSC70", null, "BombGunSC70", null, "BombGunSC70", "BombGunSC70",
				"BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", null, "BombGunSC70",
				null, "BombGunSC70", "BombGunSC70", null, "BombGunSC70", null, "BombGunSC70", "BombGunSC70", "BombGunSC70",
				"BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", null, "BombGunSC70", null,
				"BombGunSC70", "BombGunSC70", null, "BombGunSC70", null, "BombGunSC70", "BombGunSC70", "BombGunSC70",
				"BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70" });
		weaponsRegister(class1, "48xSC50", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50",
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50",
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50",
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50",
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50",
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50",
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50" });
		weaponsRegister(class1, "48xSC70", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				"BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70",
				"BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70",
				"BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70",
				"BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70",
				"BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70",
				"BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70",
				"BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70" });
		weaponsRegister(class1, "12xSC250", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, "BombGunSC250 1", "BombGunSC250 1", "BombGunSC250 1", "BombGunSC250 1", "BombGunSC250 1",
				"BombGunSC250 1", "BombGunSC250 1", "BombGunSC250 1", "BombGunSC250 1", "BombGunSC250 1", "BombGunSC250 1",
				"BombGunSC250 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "12xAB250", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, "BombGunAB250 1", "BombGunAB250 1", "BombGunAB250 1", "BombGunAB250 1", "BombGunAB250 1",
				"BombGunAB250 1", "BombGunAB250 1", "BombGunAB250 1", "BombGunAB250 1", "BombGunAB250 1", "BombGunAB250 1",
				"BombGunAB250 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "6xSC500", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, "BombGunSC500 1",
				"BombGunSC500 1", "BombGunSC500 1", "BombGunSC500 1", "BombGunSC500 1", "BombGunSC500 1", null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null });
		weaponsRegister(class1, "6xSD500", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, "BombGunSD500 1",
				"BombGunSD500 1", "BombGunSD500 1", "BombGunSD500 1", "BombGunSD500 1", "BombGunSD500 1", null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null });
		weaponsRegister(class1, "6xAB500", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, "BombGunAB500 1",
				"BombGunAB500 1", "BombGunAB500 1", "BombGunAB500 1", "BombGunAB500 1", "BombGunAB500 1", null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null });
		weaponsRegister(class1, "10xSC500", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, "BombGunSC500 1", "BombGunSC500 1", "BombGunSC500 1", "BombGunSC500 1", "BombGunSC500 1",
				"BombGunSC500 1", "BombGunSC500 1", "BombGunSC500 1", "BombGunSC500 1", null, null, "BombGunSC500 1", null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2xSC1000", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null,
				"BombGunSC1000 1", "BombGunSC1000 1", null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "4xSC1000", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", "BombGunSC1000 1",
				"BombGunSC1000 1", null, null, "BombGunSC1000 1", "BombGunSC1000 1", null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null });
		weaponsRegister(class1, "4xSC1000+2xSC500", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", "BombGunSC1000 1",
				"BombGunSC1000 1", null, null, "BombGunSC1000 1", "BombGunSC1000 1", "BombGunSC500 1", "BombGunSC500 1", null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2xSD1000", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null,
				"BombGunSD1000 1", "BombGunSD1000 1", null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "4xSD1000", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, "BombGunSD1000 1",
				"BombGunSD1000 1", "BombGunSD1000 1", "BombGunSD1000 1", null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null });
		weaponsRegister(class1, "4xSD1000+2xSD500", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, "BombGunSD1000 1",
				"BombGunSD1000 1", "BombGunSD1000 1", "BombGunSD1000 1", "BombGunSD500 1", "BombGunSD500 1", null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null });
		weaponsRegister(class1, "2xAB1000", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null,
				"BombGunAB1000 1", "BombGunAB1000 1", null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2xSC1800", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "BombGunSC1800 1",
				"BombGunSC1800 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2xPC1600", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "BombGunPC1600 1",
				"BombGunPC1600 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "1xSC2000", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, "BombGunSC2000 1", null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "1xSC2000+2xSC1800", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, "BombGunSC2000 1",
				"BombGunSC1800 1", "BombGunSC1800 1", null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "1xSC2500", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, "BombGunSC2500 1", null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "1xHs293", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, "RocketGunHS_293 1", null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2xHs293", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "RocketGunHS_293 1",
				"RocketGunHS_293 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "3xHs293", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, "RocketGunHS_293 1",
				"RocketGunHS_293 1", "RocketGunHS_293 1", null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null });
		weaponsRegister(class1, "1xFritzX", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, "RocketGunFritzX 1", null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2xFritzX", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "RocketGunFritzX 1",
				"RocketGunFritzX 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "3xFritzX", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, "RocketGunFritzX 1",
				"RocketGunFritzX 1", "RocketGunFritzX 1", null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null });
		weaponsRegister(class1, "2xTorpedo", new String[] { "MGunMG81t 2000", "MGunMG15120MGt  300", "MGunMG131t 2000",
				"MGunMG131t 750", "MGunMG131t 750", "MGunMG131t 750", "MGunMG15120MGt 300", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				"BombGunTorpF5Bheavy 1", "BombGunTorpF5Bheavy 1", null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
