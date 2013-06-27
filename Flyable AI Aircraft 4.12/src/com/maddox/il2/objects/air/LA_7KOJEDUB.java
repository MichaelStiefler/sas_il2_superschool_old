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
// Last Edited at: 2013/01/22

package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class LA_7KOJEDUB extends LA_X implements TypeAcePlane {

	public LA_7KOJEDUB() {
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.Skill = 3;
	}

	static {
		Class class1 = LA_7KOJEDUB.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "La");
		Property.set(class1, "meshName", "3DO/Plane/La-7(ofKojedub)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1948.5F);
		Property.set(class1, "FlightModel", "FlightModels/La-7.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitLA_7.class });
		weaponTriggersRegister(class1, new int[] { 1, 1, 3, 3, 9, 9 });
		weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02" });
		weaponsRegister(class1, "default", new String[] { "MGunShVAKANTIMATTERs 340", "MGunShVAKANTIMATTERs 340", null, null, null, null });
		weaponsRegister(class1, "2xFAB50", new String[] { "MGunShVAKANTIMATTERs 340", "MGunShVAKANTIMATTERs 340", "BombGunFAB50 1", "BombGunFAB50 1", null, null });
		weaponsRegister(class1, "2xFAB100", new String[] { "MGunShVAKANTIMATTERs 340", "MGunShVAKANTIMATTERs 340", "BombGunFAB100 1", "BombGunFAB100 1", null, null });
		weaponsRegister(class1, "2xDROPTANK", new String[] { "MGunShVAKANTIMATTERs 340", "MGunShVAKANTIMATTERs 340", null, null, "FuelTankGun_Tank80", "FuelTankGun_Tank80" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null });
	}
}
