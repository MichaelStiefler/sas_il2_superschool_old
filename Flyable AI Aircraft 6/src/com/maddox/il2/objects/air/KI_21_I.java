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

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.CLASS;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class KI_21_I extends KI_21 implements TypeBomber {

	public KI_21_I() {
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

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		hiermesh.chunkSetAngles("GearL10_D0", 0.0F, cvt(f, 0.05F, 0.75F, 0.0F, -38F), 0.0F);
		hiermesh.chunkSetAngles("GearL11_D0", 0.0F, 0.0F, cvt(f, 0.05F, 0.75F, 0.0F, -45F));
		hiermesh.chunkSetAngles("GearL13_D0", 0.0F, cvt(f, 0.05F, 0.75F, 0.0F, -157F), 0.0F);
		hiermesh.chunkSetAngles("GearR10_D0", 0.0F, cvt(f1, 0.34F, 0.99F, 0.0F, -38F), 0.0F);
		hiermesh.chunkSetAngles("GearR11_D0", 0.0F, 0.0F, cvt(f1, 0.05F, 0.75F, 0.0F, -45F));
		hiermesh.chunkSetAngles("GearR13_D0", 0.0F, cvt(f1, 0.34F, 0.99F, 0.0F, -157F), 0.0F);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(hierMesh(), f, f1, f2);
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
			if (f1 > 30F) {
				f1 = 30F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f < -45F) {
				f = -45F;
				flag = false;
			}
			if (f > 45F) {
				f = 45F;
				flag = false;
			}
			if (f1 < -5F) {
				f1 = -5F;
				flag = false;
			}
			if (f1 > 60F) {
				f1 = 60F;
				flag = false;
			}
			break;

		case 2: // '\002'
			if (f < -5F) {
				f = -5F;
				flag = false;
			}
			if (f > 50F) {
				f = 50F;
				flag = false;
			}
			if (f1 < -35F) {
				f1 = -35F;
				flag = false;
			}
			if (f1 > 0.0F) {
				f1 = 0.0F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
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
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Ki-21");
		Property.set(class1, "meshName", "3DO/Plane/Ki-21-I(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
		Property.set(class1, "meshName_ja", "3DO/Plane/Ki-21-I(ja)/hier.him");
		Property.set(class1, "PaintScheme_ja", new PaintSchemeBCSPar01());
		Property.set(class1, "yearService", 1937F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/Ki-21-I.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitKI_21_I.class, CockpitKI_21_I_Bombardier.class, CockpitKI_21_I_NGunner.class, CockpitKI_21_I_TGunner.class, CockpitKI_21_I_BGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 11, 12, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning303t 750", "MGunBrowning303t 750", "MGunBrowning303t 750", null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "20x15", new String[] { "MGunBrowning303t 750", "MGunBrowning303t 750", "MGunBrowning303t 750", "BombGun15kgJ 2", "BombGun15kgJ 2", "BombGun15kgJ 2", "BombGun15kgJ 2", "BombGun15kgJ 2", "BombGun15kgJ 2", "BombGun15kgJ 3",
				"BombGun15kgJ 3", "BombGun15kgJ 2" });
		weaponsRegister(class1, "14x50", new String[] { "MGunBrowning303t 750", "MGunBrowning303t 750", "MGunBrowning303t 750", "BombGun50kgJ 1", "BombGun50kgJ 1", "BombGun50kgJ 1", "BombGun50kgJ 1", "BombGun50kgJ 2", "BombGun50kgJ 2", "BombGun50kgJ 2",
				"BombGun50kgJ 2", "BombGun50kgJ 2" });
		weaponsRegister(class1, "6x100", new String[] { "MGunBrowning303t 750", "MGunBrowning303t 750", "MGunBrowning303t 750", "BombGun100kgJ", "BombGun100kgJ", "BombGun100kgJ", "BombGun100kgJ", "BombGun100kgJ", "BombGun100kgJ", null, null, null });
		weaponsRegister(class1, "3x250", new String[] { "MGunBrowning303t 750", "MGunBrowning303t 750", "MGunBrowning303t 750", "BombGun250kgJ", null, null, null, "BombGun250kgJ", "BombGun250kgJ", null, null, null });
		weaponsRegister(class1, "1x500", new String[] { "MGunBrowning303t 750", "MGunBrowning303t 750", "MGunBrowning303t 750", null, "BombGun500kgJ", null, null, null, null, null, null, null });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
