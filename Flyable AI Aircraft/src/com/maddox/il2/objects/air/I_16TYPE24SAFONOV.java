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
// Last Edited at: 2013/06/11

package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class I_16TYPE24SAFONOV extends I_16 implements TypeAcePlane {

	public I_16TYPE24SAFONOV() {
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.Skill = 3;
	}

	static {
		Class class1 = I_16TYPE24SAFONOV.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "I-16");
		Property.set(class1, "meshName", "3DO/Plane/I-16type24(ofSafonov)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
		Property.set(class1, "FlightModel", "FlightModels/I-16type24(ofSafonov).fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitI_16TYPE24.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
		weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02" });
		weaponsRegister(class1, "default", new String[] { "MGunShKASsi 240", "MGunShKASsi 240", "MGunShVAKk 120", "MGunShVAKk 120" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null });
	}
}
