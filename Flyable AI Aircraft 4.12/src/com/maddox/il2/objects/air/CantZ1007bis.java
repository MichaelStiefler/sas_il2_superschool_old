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
// Last Edited at: 2013/01/28

package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.CLASS;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class CantZ1007bis extends CantZ1007 implements TypeBomber, TypeTransport {

	public CantZ1007bis() {
		fSightCurAltitude = 850F;
		fSightCurSpeed = 150F;
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 3: // '\003'
			if (f < -45F) {
				f = -45F;
				flag = false;
			}
			if (f > 60F) {
				f = 60F;
				flag = false;
			}
			if (f1 < -35F) {
				f1 = -35F;
				flag = false;
			}
			if (f1 > 35F) {
				f1 = 35F;
				flag = false;
			}
			break;

		case 0: // '\0'
			if (f1 < -7F) {
				f1 = -7F;
				flag = false;
			}
			if (f1 > 80F) {
				f1 = 80F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f < -40F) {
				f = -40F;
				flag = false;
			}
			if (f > 40F) {
				f = 40F;
				flag = false;
			}
			if (f1 < -25F) {
				f1 = -25F;
				flag = false;
			}
			if (f1 > 45F) {
				f1 = 45F;
				flag = false;
			}
			break;

		case 2: // '\002'
			if (f < -60F) {
				f = -60F;
				flag = false;
			}
			if (f > 45F) {
				f = 45F;
				flag = false;
			}
			if (f1 < -35F) {
				f1 = -35F;
				flag = false;
			}
			if (f1 > 35F) {
				f1 = 35F;
				flag = false;
			}
			break;
		}
		af[0] = f;
		af[1] = f1;
		return flag;
	}

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
	}

	public void typeBomberAdjDistanceMinus() {
		fSightCurForwardAngle -= 0.4F;
		if (fSightCurForwardAngle < -15F)
			fSightCurForwardAngle = -15F;
	}

	public void typeBomberAdjSideslipReset() {
		fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus() {
		fSightCurSideslip += 0.5D;
		if (thisWeaponsName.startsWith("1x")) {
			if (fSightCurSideslip > 40F)
				fSightCurSideslip = 40F;
			HUD.log(AircraftHotKeys.hudLogWeaponId, "Torpedo Angle  " + fSightCurSideslip);
		} else {
			if (fSightCurSideslip > 10F)
				fSightCurSideslip = 10F;
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip " + fSightCurSideslip);
		}
	}

	public void typeBomberAdjSideslipMinus() {
		fSightCurSideslip -= 0.5D;
		if (thisWeaponsName.startsWith("1x")) {
			if (fSightCurSideslip < -40F)
				fSightCurSideslip = -40F;
			HUD.log(AircraftHotKeys.hudLogWeaponId, "Torpedo Angle  " + fSightCurSideslip);
		} else {
			if (fSightCurSideslip < -10F)
				fSightCurSideslip = -10F;
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip " + fSightCurSideslip);
		}
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
		fSightSetForwardAngle = (float) Math.atan(d / (double) fSightCurAltitude);
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

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "CantZ");
		Property.set(class1, "meshName_it", "3do/plane/CantZ1007bis(it)/hier.him");
		Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
		Property.set(class1, "meshName", "3do/plane/CantZ1007bis(multi)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar09());
		Property.set(class1, "yearService", 1940F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/CantZ1007.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitCant.class, CockpitCant_Bombardier.class, CockpitCant_TGunner.class, CockpitCant_BGunner.class, CockpitCantZ1007bis_LGunner.class, CockpitCantZ1007bis_RGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10",
				"_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04",
				"_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10" });
		weaponsRegister(class1, "default", new String[] { "MGunScotti127s 350", "MGunBredaSAFATSM127s 350", "MGunBredaSAFATSM77s 500", "MGunBredaSAFATSM77s 500", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "12x50", new String[] { "MGunScotti127s 350", "MGunBredaSAFATSM127s 350", "MGunBredaSAFATSM77s 500", "MGunBredaSAFATSM77s 500", "BombGunIT_50Kg", "BombGunIT_50Kg", "BombGunIT_50Kg", "BombGunIT_50Kg", "BombGunIT_50Kg",
				"BombGunIT_50Kg", "BombGunIT_50Kg", "BombGunIT_50Kg", "BombGunIT_50Kg", "BombGunIT_50Kg", "BombGunIT_50Kg", "BombGunIT_50Kg", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "12x100", new String[] { "MGunScotti127s 350", "MGunBredaSAFATSM127s 350", "MGunBredaSAFATSM77s 500", "MGunBredaSAFATSM77s 500", "BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg",
				"BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null });
		weaponsRegister(class1, "6x100", new String[] { "MGunScotti127s 350", "MGunBredaSAFATSM127s 350", "MGunBredaSAFATSM77s 500", "MGunBredaSAFATSM77s 500", "BombGunIT_100Kg", "BombGunNull", "BombGunIT_100Kg", "BombGunNull", "BombGunIT_100Kg",
				"BombGunNull", "BombGunIT_100Kg", "BombGunNull", "BombGunIT_100Kg", "BombGunNull", "BombGunIT_100Kg", "BombGunNull", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2x250+3x100", new String[] { "MGunScotti127s 350", "MGunBredaSAFATSM127s 350", "MGunBredaSAFATSM77s 500", "MGunBredaSAFATSM77s 500", null, null, null, null, null, null, null, null, "BombGunNull", "BombGunIT_100Kg",
				"BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_250Kg", "BombGunNull", "BombGunNull", "BombGunIT_250Kg", null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2x500", new String[] { "MGunScotti127s 350", "MGunBredaSAFATSM127s 350", "MGunBredaSAFATSM77s 500", "MGunBredaSAFATSM77s 500", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, "BombGunIT_500Kg", "BombGunNull", "BombGunNull", "BombGunIT_500Kg", null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2x250+3x100+3x100x2wing", new String[] { "MGunScotti127s 350", "MGunBredaSAFATSM127s 350", "MGunBredaSAFATSM77s 500", "MGunBredaSAFATSM77s 500", null, null, null, null, null, null, null, null, "BombGunNull",
				"BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_250Kg", "BombGunNull", "BombGunNull", "BombGunIT_250Kg", null, null, null, null, "BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg",
				"BombGunIT_100Kg", "BombGunIT_100Kg", null, null, null, null });
		weaponsRegister(class1, "2x250+3x100+3x50x2wing", new String[] { "MGunScotti127s 350", "MGunBredaSAFATSM127s 350", "MGunBredaSAFATSM77s 500", "MGunBredaSAFATSM77s 500", null, null, null, null, null, null, null, null, "BombGunNull",
				"BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_250Kg", "BombGunNull", "BombGunNull", "BombGunIT_250Kg", null, null, null, null, "BombGunIT_50Kg", "BombGunIT_50Kg", "BombGunIT_50Kg", "BombGunIT_50Kg", "BombGunIT_50Kg",
				"BombGunIT_50Kg", null, null, null, null });
		weaponsRegister(class1, "2x250+1x250x2wing", new String[] { "MGunScotti127s 350", "MGunBredaSAFATSM127s 350", "MGunBredaSAFATSM77s 500", "MGunBredaSAFATSM77s 500", null, null, null, null, null, null, null, null, null, null, null, null,
				"BombGunIT_250Kg", "BombGunNull", "BombGunNull", "BombGunIT_250Kg", null, null, null, null, null, null, null, null, null, null, "BombGunIT_250Kg", "BombGunIT_250Kg", null, null });
		weaponsRegister(class1, "2x250+2x250x2wing", new String[] { "MGunScotti127s 350", "MGunBredaSAFATSM127s 350", "MGunBredaSAFATSM77s 500", "MGunBredaSAFATSM77s 500", null, null, null, null, null, null, null, null, null, null, null, null,
				"BombGunIT_250Kg", "BombGunNull", "BombGunNull", "BombGunIT_250Kg", null, null, null, null, null, null, null, null, null, null, "BombGunIT_250Kg", "BombGunIT_250Kg", "BombGunIT_250Kg", "BombGunIT_250Kg" });
		weaponsRegister(class1, "2x500+3x50x2wing", new String[] { "MGunScotti127s 350", "MGunBredaSAFATSM127s 350", "MGunBredaSAFATSM77s 500", "MGunBredaSAFATSM77s 500", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, "BombGunIT_500Kg", "BombGunNull", "BombGunNull", "BombGunIT_500Kg", "BombGunIT_50Kg", "BombGunIT_50Kg", "BombGunIT_50Kg", "BombGunIT_50Kg", "BombGunIT_50Kg", "BombGunIT_50Kg", null, null, null, null });
		weaponsRegister(class1, "2x500+3x100x2wing", new String[] { "MGunScotti127s 350", "MGunBredaSAFATSM127s 350", "MGunBredaSAFATSM77s 500", "MGunBredaSAFATSM77s 500", null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, "BombGunIT_500Kg", "BombGunNull", "BombGunNull", "BombGunIT_500Kg", "BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg", "BombGunIT_100Kg", null, null, null, null });
		weaponsRegister(class1, "2x500+1x250x2wing", new String[] { "MGunScotti127s 350", "MGunBredaSAFATSM127s 350", "MGunBredaSAFATSM77s 500", "MGunBredaSAFATSM77s 500", null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, "BombGunIT_500Kg", "BombGunNull", "BombGunNull", "BombGunIT_500Kg", null, null, null, null, null, null, "BombGunIT_250Kg", "BombGunIT_250Kg", null, null });
		weaponsRegister(class1, "2xMotobombaFFF", new String[] { "MGunScotti127s 350", "MGunBredaSAFATSM127s 350", "MGunBredaSAFATSM77s 500", "MGunBredaSAFATSM77s 500", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, "BombGunTorpFFF", "BombGunNull", "BombGunNull", "BombGunTorpFFF1", null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null });
	}
}
