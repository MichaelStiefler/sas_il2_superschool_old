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

public class YAK_9TALBERT extends YAK_9TX implements TypeAcePlane {

	public YAK_9TALBERT() {
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.Skill = 3;
	}

	static {
		Class class1 = YAK_9TALBERT.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Yak");
		Property.set(class1, "meshName", "3DO/Plane/Yak-9T(ofAlbert)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
		Property.set(class1, "FlightModel", "FlightModels/Yak-9T.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_9T.class });
		weaponTriggersRegister(class1, new int[] { 0, 1 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_CANNON01" });
		weaponsRegister(class1, "default", new String[] { "MGunUBsi 200", "MGunNS37ki 32" });
		weaponsRegister(class1, "none", new String[] { null, null });
	}
}
