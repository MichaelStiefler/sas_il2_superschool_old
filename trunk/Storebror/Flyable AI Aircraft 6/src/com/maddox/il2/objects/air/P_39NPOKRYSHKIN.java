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

public class P_39NPOKRYSHKIN extends P_39 implements TypeAcePlane {

	public P_39NPOKRYSHKIN() {
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.Skill = 3;
	}

	static {
		Class class1 = P_39NPOKRYSHKIN.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "P39");
		Property.set(class1, "meshName", "3do/plane/P-39N(ofPokryshkin)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
		Property.set(class1, "FlightModel", "FlightModels/P-39N(ofPokryshkin).fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitP_39N1.class });
		weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 1, 1, 1, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_CANNON01", "_ExternalBomb01" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning50si 320", "MGunBrowning50si 320", "MGunBrowning303k 1300", "MGunBrowning303k 1300", "MGunBrowning303k 1300", "MGunBrowning303k 1300", "MGunM4ki 60", null });
		weaponsRegister(class1, "1xFAB250", new String[] { "MGunBrowning50si 320", "MGunBrowning50si 320", "MGunBrowning303k 1300", "MGunBrowning303k 1300", "MGunBrowning303k 1300", "MGunBrowning303k 1300", "MGunM4ki 60", "BombGunFAB250 1" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null });
	}
}
