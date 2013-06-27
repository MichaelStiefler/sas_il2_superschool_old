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
// Last Edited at: 2013/01/29

package com.maddox.il2.objects.air;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class Do217_K2 extends Do217 implements TypeX4Carrier, TypeGuidedBombCarrier {

	public Do217_K2() {
		bToFire = false;
		deltaAzimuth = 0.0F;
		deltaTangage = 0.0F;
		isGuidingBomb = false;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (thisWeaponsName.startsWith("1xFritzX") || thisWeaponsName.startsWith("2xFritzX")) {
			hierMesh().chunkVisible("WingRackR_D0", true);
			hierMesh().chunkVisible("WingRackL_D0", true);
		}
		if (thisWeaponsName.startsWith("1xHS293") || thisWeaponsName.startsWith("2xHS293")) {
			hierMesh().chunkVisible("WingRackR1_D0", true);
			hierMesh().chunkVisible("WingRackL1_D0", true);
		}
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		if (i == 5) {
			if (f1 > 5F) {
				f1 = 5F;
				flag = false;
			}
			if (f1 < -5F) {
				f1 = -5F;
				flag = false;
			}
			if (f > 5F) {
				f = 5F;
				flag = false;
			}
			if (f < -5F) {
				f = -5F;
				flag = false;
			}
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
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

	public void typeBomberAdjDistancePlus() {
		fSightCurForwardAngle++;
		if (fSightCurForwardAngle > 85F)
			fSightCurForwardAngle = 85F;
		fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
		if (!isGuidingBomb)
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) fSightCurForwardAngle) });
		if (bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjDistanceMinus() {
		fSightCurForwardAngle--;
		if (fSightCurForwardAngle < 0.0F)
			fSightCurForwardAngle = 0.0F;
		fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
		if (!isGuidingBomb)
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) fSightCurForwardAngle) });
		if (bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjSideslipPlus() {
		if (!isGuidingBomb) {
			fSightCurSideslip += 0.1F;
			if (fSightCurSideslip > 3F)
				fSightCurSideslip = 3F;
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (fSightCurSideslip * 10F)) });
		}
	}

	public void typeBomberAdjSideslipMinus() {
		if (!isGuidingBomb) {
			fSightCurSideslip -= 0.1F;
			if (fSightCurSideslip < -3F)
				fSightCurSideslip = -3F;
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (fSightCurSideslip * 10F)) });
		}
	}

	public void typeBomberAdjAltitudePlus() {
		if (!isGuidingBomb) {
			fSightCurAltitude += 10F;
			if (fSightCurAltitude > 10000F)
				fSightCurAltitude = 10000F;
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
			fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
		}
	}

	public void typeBomberAdjAltitudeMinus() {
		if (!isGuidingBomb) {
			fSightCurAltitude -= 10F;
			if (fSightCurAltitude < 850F)
				fSightCurAltitude = 850F;
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
			fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
		}
	}

	protected void mydebug(String s) {
	}

	public boolean bToFire;
	private float deltaAzimuth;
	private float deltaTangage;
	private boolean isGuidingBomb;
	private boolean isMasterAlive;

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Do-217");
		Property.set(class1, "meshName", "3do/plane/Do217_K2/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "yearService", 1943F);
		Property.set(class1, "yearExpired", 1945F);
		Property.set(class1, "FlightModel", "FlightModels/Do217K-2.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitDo217_K1.class, CockpitDo217_Bombardier.class, CockpitDo217_NGunner.class, CockpitDo217_TGunner.class, CockpitDo217_BGunner.class, CockpitDo217_LGunner.class, CockpitDo217_RGunner.class,
				CockpitDo217_PGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 10, 11, 12, 13, 14, 15, 15, 15, 15, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN10", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02",
				"_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02" });
		weaponsRegister(class1, "default", new String[] { "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG131tu 750", "MGunMG131t 500", "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300", null, null, null,
				null, null, null, null, null, null, null });
		weaponsRegister(class1, "2xFritzX", new String[] { "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG131tu 750", "MGunMG131t 500", "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300",
				"RocketGunFritzX 1", "BombGunNull 1", "RocketGunFritzX 1", "BombGunNull 1", null, null, null, null, null, null });
		weaponsRegister(class1, "2xHS293", new String[] { "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG131tu 750", "MGunMG131t 500", "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300", null, null, null,
				null, "RocketGunHS_293 1", "BombGunNull 1", "RocketGunHS_293 1", "BombGunNull 1", null, null });
		weaponsRegister(class1, "1xFritzX", new String[] { "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG131tu 750", "MGunMG131t 500", "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300",
				"RocketGunFritzX 1", null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "1xFritzX+1x300LTank", new String[] { "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG131tu 750", "MGunMG131t 500", "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300",
				"RocketGunFritzX 1", null, null, null, null, null, null, null, "FuelTankGun_Type_D 1", null });
		weaponsRegister(class1, "1xHS293+1x300LTank", new String[] { "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG131tu 750", "MGunMG131t 500", "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300", null,
				null, null, null, "RocketGunHS_293 1", null, null, null, null, "FuelTankGun_Type_D 1" });
		weaponsRegister(class1, "1xFritzX+1x900LTank", new String[] { "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG131tu 750", "MGunMG131t 500", "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300",
				"RocketGunFritzX 1", null, null, null, null, null, null, null, "FuelTankGun_Tank900 1", null });
		weaponsRegister(class1, "1xHS293+1x900LTank", new String[] { "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG131tu 750", "MGunMG131t 500", "MGunMG81t 1000", "MGunMG81t 1000", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300", "MGunMG81t 300", null,
				null, null, null, "RocketGunHS_293 1", null, null, null, null, "FuelTankGun_Tank900 1" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
