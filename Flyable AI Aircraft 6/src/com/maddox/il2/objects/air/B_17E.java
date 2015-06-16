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

public class B_17E extends B_17 {

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

		case 1: // '\001'
			if (f < -45F) {
				f = -45F;
				flag = false;
			}
			if (f > 20F) {
				f = 20F;
				flag = false;
			}
			if (f1 < -35F) {
				f1 = -35F;
				flag = false;
			}
			if (f1 > 20F) {
				f1 = 20F;
				flag = false;
			}
			break;

		case 2: // '\002'
			if (f < -50F) {
				f = -50F;
				flag = false;
			}
			if (f > 30F) {
				f = 30F;
				flag = false;
			}
			if (f1 < -35F) {
				f1 = -35F;
				flag = false;
			}
			if (f1 > 20F) {
				f1 = 20F;
				flag = false;
			}
			break;

		case 3: // '\003'
			if (f1 < -3F) {
				f1 = -3F;
				flag = false;
			}
			if (f1 > 66F) {
				f1 = 66F;
				flag = false;
			}
			break;

		case 4: // '\004'
			if (f1 < -85F) {
				f1 = -85F;
				flag = false;
			}
			if (f1 > 6F) {
				f1 = 6F;
				flag = false;
			}
			break;

		case 5: // '\005'
			if (f < -70F) {
				f = -60F;
				flag = false;
			}
			if (f > 50F) {
				f = 50F;
				flag = false;
			}
			if (f1 < -30F) {
				f1 = -30F;
				flag = false;
			}
			if (f1 > 41F) {
				f1 = 41F;
				flag = false;
			}
			break;

		case 6: // '\006'
			if (f < -70F) {
				f = -70F;
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
			if (f1 > 45F) {
				f1 = 45F;
				flag = false;
			}
			break;

		case 7: // '\007'
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

	public static boolean bChangedPit = false;

	static {
		Class class1 = B_17E.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "B-17");
		Property.set(class1, "meshName", "3DO/Plane/B-17E(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
		Property.set(class1, "meshName_us", "3DO/Plane/B-17E(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar03());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1942F);
		Property.set(class1, "yearExpired", 2800.9F);
		Property.set(class1, "FlightModel", "FlightModels/B-17E.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitB17E.class, CockpitB17E_Bombardier.class, CockpitB17E_FLGunner.class, CockpitB17E_FRGunner.class, CockpitB17E_TGunner.class, CockpitB17E_BGunner.class, CockpitB17E_LGunner.class,
				CockpitB17E_RGunner.class, CockpitB17E_AGunner.class });
		weaponTriggersRegister(class1, new int[] { 11, 12, 13, 13, 14, 14, 15, 16, 17, 17, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN10", "_MGUN09", "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 600", "MGunBrowning50t 600",
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null });
		weaponsRegister(class1, "16x20FragClusters", new String[] { "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 600", "MGunBrowning50t 600",
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGunM26A2 8", "BombGunM26A2 8" });
		weaponsRegister(class1, "16x500", new String[] { "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 600", "MGunBrowning50t 600",
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun500lbs 8", "BombGun500lbs 8" });
		weaponsRegister(class1, "8x1000", new String[] { "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 500", "MGunBrowning50t 500", "MGunBrowning50t 600", "MGunBrowning50t 600",
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun1000lbs 4", "BombGun1000lbs 4" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
