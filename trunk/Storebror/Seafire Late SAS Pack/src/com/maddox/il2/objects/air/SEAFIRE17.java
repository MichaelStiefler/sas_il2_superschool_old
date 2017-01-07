// This file is part of the SAS IL-2 Sturmovik 1946
// Late Seafire Mod package.
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
// Last Edited at: 2013/03/11

package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SEAFIRE17 extends SeafireLateBubbleTopCanopy {

	public SEAFIRE17() {
	}

	static {
		Class class1 = SEAFIRE17.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Spit");
		Property.set(class1, "meshName", "3DO/Plane/SeafireMkXVII_SAS(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1946.5F);
		Property.set(class1, "FlightModel", "FlightModels/SeafireXVII.fmd:SeafireLate_FM");
		Property.set(class1, "cockpitClass", new Class[] { CockpitSeafireLate.class });
		Property.set(class1, "LOSElevation", 0.5926F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 9, 3, 9, 9, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalBomb02", "_ExternalDev02",
				"_ExternalDev03", "_ExternalBomb03", "_ExternalBomb01", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10",
				"_ExternalDev11", "_ExternalDev12", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07",
				"_ExternalRock08", "_ExternalDev13" });
		Aircraft.weaponsRegister(class1, "default", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkpzl 120",
				"MGunHispanoMkIkpzl 120", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "90gal_dt", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkpzl 120",
				"MGunHispanoMkIkpzl 120", "PylonSpitC 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				"FuelTankGun_TankSF17 1" });
		Aircraft.weaponsRegister(class1, "1x250lb", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkpzl 120",
				"MGunHispanoMkIkpzl 120", "PylonSpitC 1", null, null, null, null, "BombGun250lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null });
		Aircraft.weaponsRegister(class1, "1x500lb", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkpzl 120",
				"MGunHispanoMkIkpzl 120", "PylonSpitC 1", null, null, null, null, "BombGun500lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null });
		Aircraft.weaponsRegister(class1, "2x250lb", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkpzl 120",
				"MGunHispanoMkIkpzl 120", null, "3 BombGun250lbsE 1", "PylonSpitL 1", "PylonSpitR 1", "BombGun250lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "90gal_dt+2x250lb", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350",
				"MGunHispanoMkIkpzl 120", "MGunHispanoMkIkpzl 120", "PylonSpitC 1", "3 BombGun250lbsE 1", "PylonSpitL 1", "PylonSpitR 1", "BombGun250lbsE 1", null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, "FuelTankGun_TankSF17 1" });
		Aircraft.weaponsRegister(class1, "3x250lb", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkpzl 120",
				"MGunHispanoMkIkpzl 120", "PylonSpitC 1", "3 BombGun250lbsE 1", "PylonSpitL 1", "PylonSpitR 1", "BombGun250lbsE 1", "BombGun250lbsE 1", null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "1x500lb+2x250lb", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350",
				"MGunHispanoMkIkpzl 120", "MGunHispanoMkIkpzl 120", "PylonSpitC 1", "3 BombGun250lbsE 1", "PylonSpitL 1", "PylonSpitR 1", "BombGun250lbsE 1", "BombGun500lbsE 1", null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "8xHVAR", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkpzl 120",
				"MGunHispanoMkIkpzl 120", null, null, null, null, null, null, null, null, "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1", null, null, null,
				"RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1",
				"RocketGunHVAR5BEAU 1", null });
		Aircraft.weaponsRegister(class1, "90gal_dt+8xHVAR", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350",
				"MGunHispanoMkIkpzl 120", "MGunHispanoMkIkpzl 120", "PylonSpitC 1", null, null, null, null, null, null, null, "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1",
				"PylonSpitROCK 1", null, null, null, "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1",
				"RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "FuelTankGun_TankSF17 1" });
		Aircraft.weaponsRegister(class1, "1x250lb+8xHVAR", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350",
				"MGunHispanoMkIkpzl 120", "MGunHispanoMkIkpzl 120", "PylonSpitC 1", null, null, null, null, "BombGun500lbsE 1", null, null, "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1",
				"PylonSpitROCK 1", null, null, null, "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1",
				"RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", null });
		Aircraft.weaponsRegister(class1, "1x500lb+8xHVAR", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350",
				"MGunHispanoMkIkpzl 120", "MGunHispanoMkIkpzl 120", "PylonSpitC 1", null, null, null, null, "BombGun500lbsE 1", null, null, "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1",
				"PylonSpitROCK 1", null, null, null, "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1",
				"RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", null });
		Aircraft.weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null });
	}
}