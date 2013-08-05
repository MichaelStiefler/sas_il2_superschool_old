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

import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class MC_200_1 extends MC_200xyz {

	public MC_200_1() {
	}

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "M.C.200");
		Property.set(class1, "meshName_it", "3DO/Plane/MC-200_I(it)/hier.him");
		Property.set(class1, "PaintScheme_it", new PaintSchemeFCSPar02());
		Property.set(class1, "meshName", "3DO/Plane/MC-200_I(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "yearService", 1939F);
		Property.set(class1, "yearExpired", 1948.5F);
		Property.set(class1, "FlightModel", "FlightModels/MC-200.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitMC_200_VII.class });
		weaponTriggersRegister(class1, new int[] { 0, 0 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
		weaponsRegister(class1, "default", new String[] { "MGunBredaSAFAT127siMC200 350", "MGunBredaSAFAT127siMC200 350" });
		weaponsRegister(class1, "none", new String[] { null, null });
	}
}
