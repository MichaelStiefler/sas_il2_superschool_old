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

import com.maddox.il2.ai.World;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class G_55 extends G_55xyz {

	public G_55() {
	}

	public void onAircraftLoaded() {
		if (this == World.getPlayerAircraft()) {
			this.FM.CT.bHasCockpitDoorControl = false;
		}
	}

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "G.55");
		Property.set(class1, "meshName_it", "3DO/Plane/G-55(it)/hier.him");
		Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
		Property.set(class1, "meshName", "3DO/Plane/G-55(multi)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
		Property.set(class1, "yearService", 1943F);
		Property.set(class1, "yearExpired", 1948.5F);
		Property.set(class1, "FlightModel", "FlightModels/G-55.fmd");
		Property.set(class1, "LOSElevation", 0.9119F);
		Property.set(class1, "cockpitClass", new Class[] { CockpitMC_205.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03" });
		weaponsRegister(class1, "default", new String[] { "MGunBredaSAFAT127g55 300", "MGunBredaSAFAT127g55 300", "MGunMG15120t 200", "MGunMG15120kh 250", "MGunMG15120kh 250" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null });
	}
}
