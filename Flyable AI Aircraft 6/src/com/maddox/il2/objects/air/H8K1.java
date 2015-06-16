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

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class H8K1 extends H8K implements TypeBomber {

	public H8K1() {
		bSightAutomation = false;
		bSightBombDump = false;
		fSightCurDistance = 0.0F;
		fSightCurForwardAngle = 0.0F;
		fSightCurSideslip = 0.0F;
		fSightCurAltitude = 850F;
		fSightCurSpeed = 150F;
		fSightCurReadyness = 0.0F;
		calibDistance = 0.0F;
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

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 0: // '\0'
			if (f < -35F) {
				f = -35F;
				flag = false;
			}
			if (f > 35F) {
				f = 35F;
				flag = false;
			}
			if (f1 < -25F) {
				f1 = -25F;
				flag = false;
			}
			if (f1 > 25F) {
				f1 = 25F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f < -22F) {
				f = -22F;
				flag = false;
			}
			if (f > 22F) {
				f = 22F;
				flag = false;
			}
			if (f1 < -57F) {
				f1 = -57F;
				flag = false;
			}
			if (f1 > 33F) {
				f1 = 33F;
				flag = false;
			}
			break;

		case 2: // '\002'
			if (f < -22F) {
				f = -22F;
				flag = false;
			}
			if (f > 22F) {
				f = 22F;
				flag = false;
			}
			if (f1 < -57F) {
				f1 = -57F;
				flag = false;
			}
			if (f1 > 33F) {
				f1 = 33F;
				flag = false;
			}
			break;

		case 3: // '\003'
			if (f1 < 0.0F) {
				f1 = 0.0F;
				flag = false;
			}
			if (f1 > 50F) {
				f1 = 50F;
				flag = false;
			}
			break;

		case 4: // '\004'
			if (f < -25F) {
				f = -25F;
				flag = false;
			}
			if (f > 25F) {
				f = 25F;
				flag = false;
			}
			if (f1 < -25F) {
				f1 = -25F;
				flag = false;
			}
			if (f1 > 25F) {
				f1 = 25F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
		case 2: // '\002'
			FM.turret[0].setHealth(f);
			break;

		case 3: // '\003'
			FM.turret[1].setHealth(f);
			FM.turret[2].setHealth(f);
			break;

		case 4: // '\004'
			FM.turret[3].setHealth(f);
			break;

		case 5: // '\005'
			FM.turret[4].setHealth(f);
			break;
		}
	}

	public void doMurderPilot(int i) {
		if (i > 5) {
			return;
		} else {
			hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
			hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
			hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
			hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
			return;
		}
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
		fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) fSightCurForwardAngle) });
		if (bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjDistanceMinus() {
		fSightCurForwardAngle--;
		if (fSightCurForwardAngle < 0.0F)
			fSightCurForwardAngle = 0.0F;
		fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
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
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (fSightCurSideslip * 10F)) });
	}

	public void typeBomberAdjSideslipMinus() {
		fSightCurSideslip -= 0.1F;
		if (fSightCurSideslip < -3F)
			fSightCurSideslip = -3F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (fSightCurSideslip * 10F)) });
	}

	public void typeBomberAdjAltitudeReset() {
		fSightCurAltitude = 850F;
	}

	public void typeBomberAdjAltitudePlus() {
		fSightCurAltitude += 10F;
		if (fSightCurAltitude > 10000F)
			fSightCurAltitude = 10000F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
		fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
	}

	public void typeBomberAdjAltitudeMinus() {
		fSightCurAltitude -= 10F;
		if (fSightCurAltitude < 850F)
			fSightCurAltitude = 850F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
		fSightCurDistance = fSightCurAltitude * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
	}

	public void typeBomberAdjSpeedReset() {
		fSightCurSpeed = 150F;
	}

	public void typeBomberAdjSpeedPlus() {
		fSightCurSpeed += 10F;
		if (fSightCurSpeed > 600F)
			fSightCurSpeed = 600F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus() {
		fSightCurSpeed -= 10F;
		if (fSightCurSpeed < 150F)
			fSightCurSpeed = 150F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
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
			fSightCurDistance -= (fSightCurSpeed / 3.6F) * f;
			if (fSightCurDistance < 0.0F) {
				fSightCurDistance = 0.0F;
				typeBomberToggleAutomation();
			}
			fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(fSightCurDistance / fSightCurAltitude));
			calibDistance = (fSightCurSpeed / 3.6F) * floatindex(Aircraft.cvt(fSightCurAltitude, 0.0F, 7000F, 0.0F, 7F), calibrationScale);
			System.out.println("time = " + floatindex(Aircraft.cvt(fSightCurAltitude, 0.0F, 7000F, 0.0F, 7F), calibrationScale));
			System.out.println("calibDistance = " + calibDistance);
			if ((double) fSightCurDistance < (double) calibDistance + (double) (fSightCurSpeed / 3.6F) * Math.sqrt(fSightCurAltitude * 0.2038736F))
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

	public static boolean bChangedPit = false;
	private boolean bSightAutomation;
	private boolean bSightBombDump;
	private float fSightCurDistance;
	public float fSightCurForwardAngle;
	public float fSightCurSideslip;
	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurReadyness;
	private float calibDistance;
	static final float calibrationScale[] = { 0.0F, 0.2F, 0.4F, 0.66F, 0.86F, 1.05F, 1.2F, 1.6F };

	static {
		Class class1 = H8K1.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "H8K");
		Property.set(class1, "meshName", "3DO/Plane/H8K1(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar04());
		Property.set(class1, "meshName_ja", "3DO/Plane/H8K1(ja)/hier.him");
		Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 2048F);
		Property.set(class1, "FlightModel", "FlightModels/H8K1.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitH8K1.class, CockpitH8K1_Bombardier.class, CockpitH8K1_NGunner.class, CockpitH8K1_AGunner.class, CockpitH8K1_TGunner.class, CockpitH8K1_LGunner.class, CockpitH8K1_RGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07",
				"_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19",
				"_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26" });
		weaponsRegister(class1, "default", new String[] { "MGunMG81t 450", "MGunMG81t 450", "MGunMG81t 450", "MGunMG15120MGt 450", "MGunMG15120MGt 600", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "16x60", new String[] { "MGunMG81t 450", "MGunMG81t 450", "MGunMG81t 450", "MGunMG15120MGt 450", "MGunMG15120MGt 600", null, null, null, null, null, null, null, null, null, null, "BombGun60kgJ", "BombGun60kgJ",
				"BombGun60kgJ", "BombGun60kgJ", "BombGun60kgJ", "BombGun60kgJ", "BombGun60kgJ", "BombGun60kgJ", "BombGun60kgJ", "BombGun60kgJ", "BombGun60kgJ", "BombGun60kgJ", "BombGun60kgJ", "BombGun60kgJ", "BombGun60kgJ", "BombGun60kgJ" });
		weaponsRegister(class1, "8x250", new String[] { "MGunMG81t 450", "MGunMG81t 450", "MGunMG81t 450", "MGunMG15120MGt 450", "MGunMG15120MGt 600", null, null, "BombGun250kgJ", "BombGun250kgJ", "BombGun250kgJ", "BombGun250kgJ", "BombGun250kgJ",
				"BombGun250kgJ", "BombGun250kgJ", "BombGun250kgJ", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2x4512", new String[] { "MGunMG81t 450", "MGunMG81t 450", "MGunMG81t 450", "MGunMG15120MGt 450", "MGunMG15120MGt 600", "BombGunTorpType91", "BombGunTorpType91", null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
