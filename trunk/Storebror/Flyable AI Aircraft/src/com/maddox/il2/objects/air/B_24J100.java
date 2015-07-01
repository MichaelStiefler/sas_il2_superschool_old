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
// Last Edited at: 2013/06/11

package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class B_24J100 extends B_24 implements TypeBomber, TypeX4Carrier, TypeGuidedBombCarrier {

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (thisWeaponsName.endsWith("Bat")) {
			hierMesh().chunkVisible("BatWingRackR_D0", true);
			hierMesh().chunkVisible("BatWingRackL_D0", true);
			return;
		} else {
			return;
		}
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

	public B_24J100() {
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

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 19: // '\023'
			killPilot(this, 4);
			break;
		}
		return super.cutFM(i, j, actor);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 0: // '\0'
			if (f < -85F) {
				f = -85F;
				flag = false;
			}
			if (f > 85F) {
				f = 85F;
				flag = false;
			}
			if (f1 < -32F) {
				f1 = -32F;
				flag = false;
			}
			if (f1 > 46F) {
				f1 = 46F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f1 < -0F) {
				f1 = -0F;
				flag = false;
			}
			if (f1 > 20F) {
				f1 = 20F;
				flag = false;
			}
			break;

		case 2: // '\002'
			if (f1 < -70F) {
				f1 = -70F;
				flag = false;
			}
			if (f1 > 7F) {
				f1 = 7F;
				flag = false;
			}
			break;

		case 3: // '\003'
			if (f < -35F) {
				f = -35F;
				flag = false;
			}
			if (f > 64F) {
				f = 64F;
				flag = false;
			}
			if (f1 < -37F) {
				f1 = -37F;
				flag = false;
			}
			if (f1 > 50F) {
				f1 = 50F;
				flag = false;
			}
			break;

		case 4: // '\004'
			if (f < -67F) {
				f = -67F;
				flag = false;
			}
			if (f > 34F) {
				f = 34F;
				flag = false;
			}
			if (f1 < -37F) {
				f1 = -37F;
				flag = false;
			}
			if (f1 > 50F) {
				f1 = 50F;
				flag = false;
			}
			break;

		case 5: // '\005'
			if (f < -85F) {
				f = -85F;
				flag = false;
			}
			if (f > 85F) {
				f = 85F;
				flag = false;
			}
			if (f1 < -32F) {
				f1 = -32F;
				flag = false;
			}
			if (f1 > 46F) {
				f1 = 46F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
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
		if (!isGuidingBomb)
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) fSightCurForwardAngle) });
		if (bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjDistanceMinus() {
		fSightCurForwardAngle--;
		if (fSightCurForwardAngle < 0.0F)
			fSightCurForwardAngle = 0.0F;
		fSightCurDistance = toMeters(fSightCurAltitude) * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
		if (!isGuidingBomb)
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) fSightCurForwardAngle) });
		if (bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjSideslipReset() {
		fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus() {
		fSightCurSideslip += 0.1F;
		if (fSightCurSideslip > 3F)
			fSightCurSideslip = 3F;
		if (!isGuidingBomb)
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (fSightCurSideslip * 10F)) });
	}

	public void typeBomberAdjSideslipMinus() {
		fSightCurSideslip -= 0.1F;
		if (fSightCurSideslip < -3F)
			fSightCurSideslip = -3F;
		if (!isGuidingBomb)
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (fSightCurSideslip * 10F)) });
	}

	public void typeBomberAdjAltitudeReset() {
		fSightCurAltitude = 3000F;
	}

	public void typeBomberAdjAltitudePlus() {
		fSightCurAltitude += 50F;
		if (fSightCurAltitude > 50000F)
			fSightCurAltitude = 50000F;
		if (!isGuidingBomb)
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) fSightCurAltitude) });
		fSightCurDistance = toMeters(fSightCurAltitude) * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
	}

	public void typeBomberAdjAltitudeMinus() {
		fSightCurAltitude -= 50F;
		if (fSightCurAltitude < 1000F)
			fSightCurAltitude = 1000F;
		if (!isGuidingBomb)
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
		if (!isGuidingBomb)
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus() {
		fSightCurSpeed -= 10F;
		if (fSightCurSpeed < 100F)
			fSightCurSpeed = 100F;
		if (!isGuidingBomb)
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
			if ((double) fSightCurDistance < (double) toMetersPerSecond(fSightCurSpeed) * Math.sqrt(toMeters(fSightCurAltitude) * 0.2038736F))
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

	public boolean bToFire;
	private float deltaAzimuth;
	private float deltaTangage;
	private boolean isGuidingBomb;
	private boolean isMasterAlive;
	public static boolean bChangedPit = false;
	private boolean bSightAutomation;
	private boolean bSightBombDump;
	public float fSightCurDistance;
	public float fSightCurForwardAngle;
	public float fSightCurSideslip;
	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurReadyness;

	static {
		Class class1 = B_24J100.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "B-24");
		Property.set(class1, "meshName", "3DO/Plane/B-24J-100-CF(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
		Property.set(class1, "meshName_us", "3DO/Plane/B-24J-100-CF(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1943.5F);
		Property.set(class1, "yearExpired", 2800.9F);
		Property.set(class1, "FlightModel", "FlightModels/B-24J.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitB_24J100.class, CockpitB_24J100_Bombardier.class, CockpitB_24J100_FGunner.class, CockpitB_24J100_TGunner.class, CockpitB_24J100_AGunner.class, CockpitB_24J100_BGunner.class,
				CockpitB_24J100_RGunner.class, CockpitB_24J100_LGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 12, 13, 14, 15, 15, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05",
				"_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning50t 365", "MGunBrowning50t 365", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375",
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "16x500", new String[] { "MGunBrowning50t 365", "MGunBrowning50t 365", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375",
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun500lbs 2", "BombGun500lbs 2", "BombGun500lbs 2", "BombGun500lbs 2", "BombGun500lbs 2", "BombGun500lbs 2", "BombGun500lbs 2", "BombGun500lbs 2", null, null, null, null });
		weaponsRegister(class1, "16xRazon", new String[] { "MGunBrowning50t 365", "MGunBrowning50t 365", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375",
				"MGunBrowning50t 500", "MGunBrowning50t 500", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", null, null, null, null });
		weaponsRegister(class1, "8xRazon", new String[] { "MGunBrowning50t 365", "MGunBrowning50t 365", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375",
				"MGunBrowning50t 500", "MGunBrowning50t 500", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2xBat", new String[] { "MGunBrowning50t 365", "MGunBrowning50t 365", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375",
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, null, null, null, null, null, "RocketGunBat 1", "BombGunNull 1", "BombGunNull 1", "RocketGunBat 1" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
