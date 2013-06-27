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
// Last Edited at: 2013/01/22

package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class PBN1 extends PBYX {

	public PBN1() {
		fSightCurAltitude = 300F;
		fSightCurSpeed = 50F;
		fSightCurForwardAngle = 0.0F;
		fSightSetForwardAngle = 0.0F;
		fSightCurSideslip = 0.0F;
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
		case 2: // '\002'
			FM.turret[0].setHealth(f);
			break;

		case 5: // '\005'
			FM.turret[1].setHealth(f);
			break;

		case 6: // '\006'
			FM.turret[2].setHealth(f);
			break;
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("HMask1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			hierMesh().chunkVisible("Head1_D0", false);
			break;

		case 1: // '\001'
			hierMesh().chunkVisible("Pilot2_D0", false);
			hierMesh().chunkVisible("HMask2_D0", false);
			hierMesh().chunkVisible("Pilot2_D1", true);
			break;

		case 2: // '\002'
			hierMesh().chunkVisible("Pilot3_D0", false);
			hierMesh().chunkVisible("HMask3_D0", false);
			hierMesh().chunkVisible("Pilot3_D1", true);
			break;

		case 3: // '\003'
			hierMesh().chunkVisible("Pilot4_D0", false);
			hierMesh().chunkVisible("HMask4_D0", false);
			hierMesh().chunkVisible("Pilot4_D1", true);
			break;

		case 5: // '\005'
			hierMesh().chunkVisible("Pilot6_D0", false);
			hierMesh().chunkVisible("HMask6_D0", false);
			hierMesh().chunkVisible("Pilot6_D1", true);
			break;

		case 6: // '\006'
			hierMesh().chunkVisible("Pilot7_D0", false);
			hierMesh().chunkVisible("HMask7_D0", false);
			hierMesh().chunkVisible("Pilot7_D1", true);
			break;
		}
	}

	public boolean typeBomberToggleAutomation() {
		return false;
	}

	public void typeBomberAdjDistanceReset() {
		fSightCurForwardAngle = 0.0F;
	}

	public void typeBomberAdjDistancePlus() {
		fSightCurForwardAngle += 0.2F;
		if (fSightCurForwardAngle > 75F)
			fSightCurForwardAngle = 75F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (fSightCurForwardAngle * 1.0F)) });
	}

	public void typeBomberAdjDistanceMinus() {
		fSightCurForwardAngle -= 0.2F;
		if (fSightCurForwardAngle < -15F)
			fSightCurForwardAngle = -15F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (fSightCurForwardAngle * 1.0F)) });
	}

	public void typeBomberAdjSideslipReset() {
		fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus() {
		fSightCurSideslip++;
		if (fSightCurSideslip > 45F)
			fSightCurSideslip = 45F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (fSightCurSideslip * 1.0F)) });
	}

	public void typeBomberAdjSideslipMinus() {
		fSightCurSideslip--;
		if (fSightCurSideslip < -45F)
			fSightCurSideslip = -45F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (fSightCurSideslip * 1.0F)) });
	}

	public void typeBomberAdjAltitudeReset() {
		fSightCurAltitude = 300F;
	}

	public void typeBomberAdjAltitudePlus() {
		fSightCurAltitude += 10F;
		if (fSightCurAltitude > 10000F)
			fSightCurAltitude = 10000F;
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
		if (fSightCurSpeed > 520F)
			fSightCurSpeed = 520F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus() {
		fSightCurSpeed -= 5F;
		if (fSightCurSpeed < 50F)
			fSightCurSpeed = 50F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
	}

	public void typeBomberUpdate(float f) {
		double d = ((double) fSightCurSpeed / 3.6000000000000001D) * Math.sqrt((double) fSightCurAltitude * 0.20387359799999999D);
		d -= (double) (fSightCurAltitude * fSightCurAltitude) * 1.419E-005D;
		fSightSetForwardAngle = (float) Math.toDegrees(Math.atan(d / (double) fSightCurAltitude));
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

	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurForwardAngle;
	public float fSightSetForwardAngle;
	public float fSightCurSideslip;

	static {
		Class class1 = PBN1.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "PBY");
		Property.set(class1, "meshName", "3DO/Plane/PBN-1/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar04());
		Property.set(class1, "yearService", 1943F);
		Property.set(class1, "yearExpired", 2048F);
		Property.set(class1, "FlightModel", "FlightModels/PBN-1.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitPBN1.class, CockpitPBN1_Bombardier.class, CockpitPBN1_NGunner.class, CockpitPBN1_LGunner.class, CockpitPBN1_RGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 11, 12, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning50t 1000", "MGunBrowning50t 1000", "MGunBrowning50t 1000", null, null, null, null });
		weaponsRegister(class1, "4500", new String[] { "MGunBrowning50t 1000", "MGunBrowning50t 1000", "MGunBrowning50t 1000", "BombGun500lbs 1", "BombGun500lbs 1", "BombGun500lbs 1", "BombGun500lbs 1" });
		weaponsRegister(class1, "41000", new String[] { "MGunBrowning50t 1000", "MGunBrowning50t 1000", "MGunBrowning50t 1000", "BombGun1000lbs 1", "BombGun1000lbs 1", "BombGun1000lbs 1", "BombGun1000lbs 1" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null });
	}
}
