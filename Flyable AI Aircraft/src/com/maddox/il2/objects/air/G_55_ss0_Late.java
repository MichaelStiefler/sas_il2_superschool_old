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

public class G_55_ss0_Late extends G_55xyz {

	public G_55_ss0_Late() {
	}

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "G.55");
		Property.set(class1, "meshName_it", "3DO/Plane/G-55_ss0(it)/hier.him");
		Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
		Property.set(class1, "meshName", "3DO/Plane/G-55_ss0(multi)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1948.5F);
		Property.set(class1, "FlightModel", "FlightModels/G-55_ss0-late.fmd");
		Property.set(class1, "LOSElevation", 0.9119F);
		Property.set(class1, "cockpitClass", new Class[] { CockpitMC_205.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01" });
		weaponsRegister(class1, "default", new String[] { "MGunBredaSAFAT127g55 300", "MGunBredaSAFAT127g55 300", "MGunBredaSAFAT127g55k 300", "MGunBredaSAFAT127g55k 300", "MGunMG15120t 200" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null });
	}
}
