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

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class TU_2S extends TU_2 implements TypeBomber {

	public TU_2S() {
		fSightCurAltitude = 300F;
		fSightCurSpeed = 50F;
		fSightCurForwardAngle = 0.0F;
		fSightSetForwardAngle = 0.0F;
		fSightCurSideslip = 0.0F;
	}

	protected void moveBayDoor(float f) {
		hierMesh().chunkSetAngles("Bay01_D0", 0.0F, -85F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay02_D0", 0.0F, 85F * f, 0.0F);
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
		fSightCurAltitude += 50F;
		if (fSightCurAltitude > 50000F)
			fSightCurAltitude = 50000F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
	}

	public void typeBomberAdjAltitudeMinus() {
		fSightCurAltitude -= 50F;
		if (fSightCurAltitude < 1000F)
			fSightCurAltitude = 1000F;
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
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		fSightCurAltitude = netmsginput.readFloat();
		fSightCurSpeed = netmsginput.readFloat();
	}

	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurForwardAngle;
	public float fSightSetForwardAngle;
	public float fSightCurSideslip;

	static {
		Class class1 = TU_2S.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Tu-2");
		Property.set(class1, "meshName", "3DO/Plane/Tu-2S/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
		Property.set(class1, "yearService", 1942.5F);
		Property.set(class1, "yearExpired", 1956.6F);
		Property.set(class1, "FlightModel", "FlightModels/Tu-2S.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitTU_2S.class, CockpitTU_2S_Bombardier.class, CockpitTU_2S_TGunner.class, CockpitTU_2S_RTGunner.class, CockpitTU_2S_BGunner.class });
		weaponTriggersRegister(class1, new int[] { 1, 1, 10, 11, 12, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05",
				"_BombSpawn06" });
		weaponsRegister(class1, "default", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "6fab50", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", null, null, null, "BombGunFAB50", "BombGunFAB50", "BombGunFAB50", "BombGunFAB50", "BombGunFAB50", "BombGunFAB50" });
		weaponsRegister(class1, "8fab50", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB50", "BombGunFAB50", null, "BombGunFAB50", "BombGunFAB50", "BombGunFAB50", "BombGunFAB50",
				"BombGunFAB50", "BombGunFAB50" });
		weaponsRegister(class1, "6fab100", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", null, null, null, "BombGunFAB100", "BombGunFAB100", "BombGunFAB100", "BombGunFAB100", "BombGunFAB100",
				"BombGunFAB100" });
		weaponsRegister(class1, "8fab100", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB100", "BombGunFAB100", null, "BombGunFAB100", "BombGunFAB100", "BombGunFAB100", "BombGunFAB100",
				"BombGunFAB100", "BombGunFAB100" });
		weaponsRegister(class1, "1fab250", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", null, null, "BombGunFAB250", null, null, null, null, null, null });
		weaponsRegister(class1, "2fab250", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB250", "BombGunFAB250", null, null, null, null, null, null, null });
		weaponsRegister(class1, "2fab2506fab100", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB250", "BombGunFAB250", null, "BombGunFAB100", "BombGunFAB100", "BombGunFAB100", "BombGunFAB100",
				"BombGunFAB100", "BombGunFAB100" });
		weaponsRegister(class1, "3fab250", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB250", "BombGunFAB250", "BombGunFAB250", null, null, null, null, null, null });
		weaponsRegister(class1, "4fab250", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", null, null, null, null, null, "BombGunFAB250", "BombGunFAB250", "BombGunFAB250", "BombGunFAB250" });
		weaponsRegister(class1, "6fab250", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB250", "BombGunFAB250", null, null, null, "BombGunFAB250", "BombGunFAB250", "BombGunFAB250",
				"BombGunFAB250" });
		weaponsRegister(class1, "1fab500", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", null, null, "BombGunFAB500", null, null, null, null, null, null });
		weaponsRegister(class1, "2fab500", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB500", "BombGunFAB500", null, null, null, null, null, null, null });
		weaponsRegister(class1, "2fab5006fab50", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB500", "BombGunFAB500", null, "BombGunFAB50", "BombGunFAB50", "BombGunFAB50", "BombGunFAB50",
				"BombGunFAB50", "BombGunFAB50" });
		weaponsRegister(class1, "2fab5006fab100", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB500", "BombGunFAB500", null, "BombGunFAB100", "BombGunFAB100", "BombGunFAB100", "BombGunFAB100",
				"BombGunFAB100", "BombGunFAB100" });
		weaponsRegister(class1, "2fab5004fab250", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB500", "BombGunFAB500", null, null, null, "BombGunFAB250", "BombGunFAB250", "BombGunFAB250",
				"BombGunFAB250" });
		weaponsRegister(class1, "3fab500", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB500", "BombGunFAB500", "BombGunFAB500", null, null, null, null, null, null });
		weaponsRegister(class1, "4fab500", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB500", "BombGunFAB500", null, "BombGunFAB500", "BombGunFAB500", null, null, null, null });
		weaponsRegister(class1, "6fab500", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB500", "BombGunFAB500", null, null, null, "BombGunFAB500", "BombGunFAB500", "BombGunFAB500",
				"BombGunFAB500" });
		weaponsRegister(class1, "1fab1000", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", null, null, "BombGunFAB1000", null, null, null, null, null, null });
		weaponsRegister(class1, "1fab10002fab250", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB250", "BombGunFAB250", "BombGunFAB1000", null, null, null, null, null, null });
		weaponsRegister(class1, "2fab1000", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB1000", "BombGunFAB1000", null, null, null, null, null, null, null });
		weaponsRegister(class1, "2fab10006fab50", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB1000", "BombGunFAB1000", null, "BombGunFAB50", "BombGunFAB50", "BombGunFAB50", "BombGunFAB50",
				"BombGunFAB50", "BombGunFAB50" });
		weaponsRegister(class1, "2fab10006fab100", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB1000", "BombGunFAB1000", null, "BombGunFAB100", "BombGunFAB100", "BombGunFAB100",
				"BombGunFAB100", "BombGunFAB100", "BombGunFAB100" });
		weaponsRegister(class1, "2fab10002fab250", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB1000", "BombGunFAB1000", null, "BombGunFAB250", "BombGunFAB250", null, null, null, null });
		weaponsRegister(class1, "2fab10001fab500", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB1000", "BombGunFAB1000", "BombGunFAB500", null, null, null, null, null, null });
		weaponsRegister(class1, "3fab1000", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", "BombGunFAB1000", "BombGunFAB1000", "BombGunFAB1000", null, null, null, null, null, null });
		weaponsRegister(class1, "1fab2000", new String[] { "MGunShVAKk 250", "MGunShVAKk 250", "MGunUBt 550", "MGunUBt 450", "MGunUBt 350", null, null, "BombGunFAB2000", null, null, null, null, null, null });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
