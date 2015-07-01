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

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class TBD1 extends TBD {

	public TBD1() {
	}

	public static String getSkinPrefix(String s, Regiment regiment) {
		if (regiment == null || regiment.country() == null)
			return "";
		if (regiment.country().equals("us")) {
			int i = Mission.getMissionDate(true);
			if (i > 0) {
				if (i < 0x128310f)
					return "41_";
				if (i < 0x1285563)
					return "early42_";
			}
		}
		return "";
	}

	static {
		Class class1 = TBD1.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "TBD");
		Property.set(class1, "meshName", "3DO/Plane/TBD-1(Multi)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
		Property.set(class1, "meshName_us", "3DO/Plane/TBD-1(US)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFCSPar01());
		Property.set(class1, "originCountry", PaintScheme.countryUSA);
		Property.set(class1, "yearService", 1937F);
		Property.set(class1, "yearExpired", 1944.5F);
		Property.set(class1, "FlightModel", "FlightModels/TBD-1.fmd");
		Property.set(class1, "LOSElevation", 0.5926F);
		Property.set(class1, "cockpitClass", new Class[] { CockpitTBD1.class, CockpitTBD_TGunner.class });
		weaponTriggersRegister(class1, new int[] { 0, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN04", "_ExternalBomb00", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08",
				"_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalDev00", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04",
				"_ExternalDev05", "_ExternalDev06" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning303si 350", "MGunBrowning303t 600", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "1x1000", new String[] { "MGunBrowning303si 350", "MGunBrowning303t 600", null, "BombGun1000lbs 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonTBD_CRack", null,
				null, null, null });
		weaponsRegister(class1, "2x500", new String[] { "MGunBrowning303si 350", "MGunBrowning303t 600", null, null, "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonTBD_FRack",
				null, null, null, null, null });
		weaponsRegister(class1, "6x100", new String[] { "MGunBrowning303si 350", "MGunBrowning303t 600", null, null, null, null, null, null, null, null, null, null, "BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1",
				"BombGun100lbs 1", "BombGun100lbs 1", null, null, null, "PylonTBD_LRack", null, "PylonTBD_RRack", null });
		weaponsRegister(class1, "12x100", new String[] { "MGunBrowning303si 350", "MGunBrowning303t 600", null, null, null, null, "BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1",
				"BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1", null, null, null, "PylonTBD_LRack", "PylonTBD_LRack", "PylonTBD_RRack", "PylonTBD_RRack" });
		weaponsRegister(class1, "torpedo", new String[] { "MGunBrowning303si 350", "MGunBrowning303t 600", "BombGunTorpMk13a", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonTBD_TRack", null, null, null,
				null, null, null });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
