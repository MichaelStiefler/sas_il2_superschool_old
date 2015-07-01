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

import com.maddox.rts.Property;

public class B_17D extends B_17 {

	protected void moveBayDoor(float f) {
		hierMesh().chunkSetAngles("Bay01_D0", 0.0F, -85F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay02_D0", 0.0F, -85F * f, 0.0F);
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
		case 2: // '\002'
			FM.turret[0].setHealth(f);
			FM.turret[1].setHealth(f);
			FM.turret[2].setHealth(f);
			break;

		case 6: // '\006'
			FM.turret[3].setHealth(f);
			break;

		case 7: // '\007'
			FM.turret[4].setHealth(f);
			break;

		case 8: // '\b'
			FM.turret[5].setHealth(f);
			break;
		}
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.AS.wantBeaconsNet(true);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 0: // '\0'
			if (f < -11F) {
				f = -11F;
				flag = false;
			}
			if (f > 11F) {
				f = 11F;
				flag = false;
			}
			if (f1 < -14F) {
				f1 = -14F;
				flag = false;
			}
			if (f1 > 14F) {
				f1 = 14F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f < -26F) {
				f = -26F;
				flag = false;
			}
			if (f > 0.0F) {
				f = 0.0F;
				flag = false;
			}
			if (f1 < -14F) {
				f1 = -14F;
				flag = false;
			}
			if (f1 > 14F) {
				f1 = 14F;
				flag = false;
			}
			break;

		case 2: // '\002'
			if (f < -11F) {
				f = -11F;
				flag = false;
			}
			if (f > 11F) {
				f = 11F;
				flag = false;
			}
			if (f1 < -25F) {
				f1 = -25F;
				flag = false;
			}
			if (f1 > 0.0F) {
				f1 = 0.0F;
				flag = false;
			}
			break;

		case 3: // '\003'
			if (f < -12F) {
				f = -12F;
				flag = false;
			}
			if (f > 12F) {
				f = 12F;
				flag = false;
			}
			if (f1 < -45F) {
				f1 = -45F;
				flag = false;
			}
			if (f1 > 2.0F) {
				f1 = 2.0F;
				flag = false;
			}
			break;

		case 4: // '\004'
			if (f < -41F) {
				f = -41F;
				flag = false;
			}
			if (f > 45F) {
				f = 45F;
				flag = false;
			}
			if (f1 < -10F) {
				f1 = -10F;
				flag = false;
			}
			if (f1 > 45F) {
				f1 = 45F;
				flag = false;
			}
			break;

		case 5: // '\005'
			if (f < -45F) {
				f = -45F;
				flag = false;
			}
			if (f > 53F) {
				f = 53F;
				flag = false;
			}
			if (f1 < -10F) {
				f1 = -10F;
				flag = false;
			}
			if (f1 > 45F) {
				f1 = 45F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public static boolean bChangedPit = false;

	static {
		Class class1 = B_17D.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "B-17");
		Property.set(class1, "meshName", "3DO/Plane/B-17D(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
		Property.set(class1, "meshName_us", "3DO/Plane/B-17D(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 2800.9F);
		Property.set(class1, "FlightModel", "FlightModels/B-17D.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitB17D.class, CockpitB17D_Bombardier.class, CockpitB17D_FGunner.class, CockpitB17D_LGunner.class, CockpitB17D_RGunner.class, CockpitB17D_BGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 13, 14, 15, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_BombSpawn01", "_BombSpawn02" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 600", "MGunBrowning50t 600", null, null });
		weaponsRegister(class1, "20x100", new String[] { "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 600", "MGunBrowning50t 600", "BombGun100lbs 10",
				"BombGun100lbs 10" });
		weaponsRegister(class1, "14x300",
				new String[] { "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 600", "MGunBrowning50t 600", "BombGun300lbs 7", "BombGun300lbs 7" });
		weaponsRegister(class1, "4x1000", new String[] { "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 600", "MGunBrowning50t 600", "BombGun1000lbs 2",
				"BombGun1000lbs 2" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null });
	}
}
