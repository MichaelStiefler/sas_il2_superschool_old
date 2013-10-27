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

public class FW_200C3U4 extends FW_200 implements TypeBomber, TypeX4Carrier, TypeGuidedBombCarrier {

	public FW_200C3U4() {
		fSightCurAltitude = 300F;
		fSightCurSpeed = 50F;
		fSightCurForwardAngle = 0.0F;
		fSightSetForwardAngle = 0.0F;
		fSightCurSideslip = 0.0F;
		deltaAzimuth = 0.0F;
		deltaTangage = 0.0F;
		isGuidingBomb = false;
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

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (thisWeaponsName.startsWith("2HS293")) {
			hierMesh().chunkVisible("Hs293RackL", true);
			hierMesh().chunkVisible("Hs293RackR", true);
		}
	}

	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurForwardAngle;
	public float fSightSetForwardAngle;
	public float fSightCurSideslip;
	private float deltaAzimuth;
	private float deltaTangage;
	private boolean isGuidingBomb;
	private boolean isMasterAlive;

	static {
		Class class1 = FW_200C3U4.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "FW200");
		Property.set(class1, "meshName", "3DO/Plane/FW-200C-3U4/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
		Property.set(class1, "yearService", 1941.1F);
		Property.set(class1, "yearExpired", 1949F);
		Property.set(class1, "FlightModel", "FlightModels/FW-200C-3U4.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitFW200.class, CockpitFW200_Bombardier.class, CockpitFW200_PGunner.class, CockpitFW200_TGunner.class, CockpitFW200_FGunner.class, CockpitFW200_BGunner.class, CockpitFW200_LGunner.class, CockpitFW200_RGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 15, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_BombSpawn01", "_BombSpawn02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05",
				"_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb06", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13",
				"_BombSpawn14" });
		weaponsRegister(class1, "default", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null });
		weaponsRegister(class1, "12sc50", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", null, null, null, null, null, null, null, null, null, null, "BombGunSC50", "BombGunSC50",
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50" });
		weaponsRegister(class1, "12sc502sc250", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", null, null, "BombGunSC250", "BombGunSC250", null, null, null, null, null, null,
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50" });
		weaponsRegister(class1, "12sc504sc250", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", null, null, "BombGunSC250", "BombGunSC250", "BombGunSC250", "BombGunSC250", null,
				null, null, null, "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50" });
		weaponsRegister(class1, "12sc504sc500", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", null, null, "BombGunSC500", "BombGunSC500", "BombGunSC500", "BombGunSC500", null,
				null, null, null, "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50" });
		weaponsRegister(class1, "12sc502sc1000", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", null, null, "BombGunSC1000", "BombGunSC1000", null, null, null, null, null, null,
				"BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50" });
		weaponsRegister(class1, "12sc504sc1000", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", null, null, "BombGunSC1000", "BombGunSC1000", "BombGunSC1000", "BombGunSC1000", null,
				null, null, null, "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50" });
		weaponsRegister(class1, "2sc250", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", "BombGunSC250", "BombGunSC250", null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "4sc250", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", "BombGunSC250", "BombGunSC250", null, null, "BombGunSC250", "BombGunSC250", null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "6sc250", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", "BombGunSC250", "BombGunSC250", "BombGunSC250", "BombGunSC250", "BombGunSC250",
				"BombGunSC250", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2sc500", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", "BombGunSC500", "BombGunSC500", null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "4sc500", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", "BombGunSC500", "BombGunSC500", null, null, "BombGunSC500", "BombGunSC500", null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "6sc500", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", "BombGunSC500", "BombGunSC500", "BombGunSC500", "BombGunSC500", "BombGunSC500",
				"BombGunSC500", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2sd500", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", "BombGunSD500", "BombGunSD500", null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "4sd500", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", "BombGunSD500", "BombGunSD500", null, null, "BombGunSD500", "BombGunSD500", null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "6sd500", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", "BombGunSD500", "BombGunSD500", "BombGunSD500", "BombGunSD500", "BombGunSD500",
				"BombGunSD500", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2sc1000", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", null, null, null, null, "BombGunSC1000", "BombGunSC1000", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "4sc1000", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", null, null, "BombGunSC1000", "BombGunSC1000", "BombGunSC1000", "BombGunSC1000", null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2sc1800", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", null, null, null, null, "BombGunSC1800", "BombGunSC1800", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2ab1000", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", null, null, null, null, "BombGunAB1000", "BombGunAB1000", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "4ab1000", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", null, null, "BombGunAB1000", "BombGunAB1000", "BombGunAB1000", "BombGunAB1000", null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2pc1600", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", null, null, null, null, "BombGunPC1600", "BombGunPC1600", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2HS293", new String[] { "MGunMG15t 1125", "MGunMGFFt 300", "MGunMG15t 1125", "MGunMG15t 450", "MGunMG15t 450", "MGunMG15t 1125", null, null, null, null, null, null, "RocketGunHS_293 1", "BombGunNull 1",
				"RocketGunHS_293 1", "BombGunNull 1", null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
