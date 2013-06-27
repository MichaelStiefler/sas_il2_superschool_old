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
// Last Edited at: 2013/02/06

package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class Wellington3 extends Wellington implements TypeBomber {

	public Wellington3() {
		bSightAutomation = false;
		fSightCurDistance = 0.0F;
		fSightCurForwardAngle = 0.0F;
		fSightCurSideslip = 0.0F;
		fSightCurAltitude = 850F;
		fSightCurSpeed = 150F;
		fSightCurReadyness = 0.0F;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.Gears.computePlaneLandPose(FM);
		// mydebuggunnery("H = " + FM.Gears.H);
		// mydebuggunnery("Pitch = " + FM.Gears.Pitch);
		if (thisWeaponsName.startsWith("1x4000")) {
			hierMesh().chunkVisible("Bay_D0", false);
			hierMesh().chunkVisible("BayStruct_D0", false);
			hierMesh().chunkVisible("Bay_Mod423_D0", true);
		}
	}

	// protected void mydebuggunnery(String s)
	// {
	// System.out.println(s);
	// }

	public boolean typeBomberToggleAutomation() {
		return false;
	}

	public void typeBomberAdjDistanceReset() {
		fSightCurForwardAngle = 0.0F;
	}

	public void typeBomberAdjDistancePlus() {
		fSightCurForwardAngle += 0.4F;
		if (fSightCurForwardAngle > 75F)
			fSightCurForwardAngle = 75F;
		fSightCurDistance = toMeters(fSightCurAltitude) * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) fSightCurForwardAngle) });
		if (bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjDistanceMinus() {
		fSightCurForwardAngle -= 0.4F;
		if (fSightCurForwardAngle < -15F)
			fSightCurForwardAngle = -15F;
		fSightCurDistance = toMeters(fSightCurAltitude) * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) fSightCurForwardAngle) });
		if (bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjSideslipReset() {
		fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus() {
		fSightCurSideslip += 0.5D;
		if (fSightCurSideslip > 10F)
			fSightCurSideslip = 10F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip " + fSightCurSideslip);
	}

	public void typeBomberAdjSideslipMinus() {
		fSightCurSideslip -= 0.5D;
		if (fSightCurSideslip < -10F)
			fSightCurSideslip = -10F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip " + fSightCurSideslip);
	}

	public void typeBomberAdjAltitudeReset() {
		fSightCurAltitude = 300F;
	}

	public void typeBomberAdjAltitudePlus() {
		fSightCurAltitude += 10F;
		if (fSightCurAltitude > 6000F)
			fSightCurAltitude = 6000F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
	}

	public void typeBomberAdjAltitudeMinus() {
		fSightCurAltitude -= 10F;
		if (fSightCurAltitude < 300F)
			fSightCurAltitude = 300F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
	}

	public void typeBomberAdjSpeedReset() {
		fSightCurSpeed = 50F;
	}

	public void typeBomberAdjSpeedPlus() {
		fSightCurSpeed += 5F;
		if (fSightCurSpeed > 650F)
			fSightCurSpeed = 650F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus() {
		fSightCurSpeed -= 5F;
		if (fSightCurSpeed < 50F)
			fSightCurSpeed = 50F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
	}

	public void typeBomberUpdate(float f) {
		if (bSightAutomation) {
			double d = ((double) fSightCurSpeed / 3.6000000000000001D) * Math.sqrt((double) fSightCurAltitude * 0.20387359799999999D);
			d -= (double) (fSightCurAltitude * fSightCurAltitude) * 1.419E-005D;
			fSightCurForwardAngle = (float) Math.atan(d / (double) fSightCurAltitude);
		}
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		netmsgguaranted.writeFloat(fSightCurAltitude);
		netmsgguaranted.writeFloat(fSightCurSpeed);
		netmsgguaranted.writeFloat(fSightCurForwardAngle);
		netmsgguaranted.writeFloat(fSightCurSideslip);
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		fSightCurAltitude = netmsginput.readFloat();
		fSightCurSpeed = netmsginput.readFloat();
		fSightCurForwardAngle = netmsginput.readFloat();
		fSightCurSideslip = netmsginput.readFloat();
	}

	private static final float toMeters(float f) {
		return 0.3048F * f;
	}

	public boolean bSightAutomation;
	public float fSightCurDistance;
	public float fSightCurForwardAngle;
	public float fSightCurSideslip;
	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurReadyness;

	static {
		Class class1 = Wellington3.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Wellington");
		Property.set(class1, "meshName", "3DO/Plane/Wellington/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "yearService", 1940F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Wellington3.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitWellington_MKIII.class, CockpitWellington_MkIII_Bombardier.class, CockpitWellington_MkIII_AGunner.class, CockpitWellington_MkIII_FGunner.class, CockpitWellington_MkIII_LGunner.class,
				CockpitWellington_MkIII_RGunner.class });
		Property.set(class1, "LOSElevation", 0.7394F);
		weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 11, 11, 12, 13, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07",
				"_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_BombSpawn21" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350",
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "16x250lb", new String[] { "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350",
				"BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1",
				"BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", null, null, null });
		weaponsRegister(class1, "9x500lb", new String[] { "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350",
				null, "BombGun500lbsE 1", null, null, "BombGun500lbsE 1", null, null, "BombGun500lbsE 1", null, "BombGun500lbsE 1", null, "BombGun500lbsE 1", "BombGun500lbsE 1", null, "BombGun500lbsE 1", "BombGun500lbsE 1", null, "BombGun500lbsE 1",
				null, null, null });
		weaponsRegister(class1, "2x1000lb6x250lb", new String[] { "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350",
				"MGunBrowning303t 350", null, null, null, "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", null, null, null, null, null, null, "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", null, null, null, "BombGun1000lbsGPE 1",
				"BombGun1000lbsGPE 1", null });
		weaponsRegister(class1, "2x2000lb", new String[] { "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350",
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "BombGun2000lbs 1", "BombGun2000lbs 1", null });
		weaponsRegister(class1, "1x4000lb", new String[] { "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350", "MGunBrowning303t 350",
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "BombGun4000HCmkI 1" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
