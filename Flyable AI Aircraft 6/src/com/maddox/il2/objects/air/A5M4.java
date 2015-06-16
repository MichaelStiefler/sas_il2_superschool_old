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

import com.maddox.il2.engine.Actor;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class A5M4 extends A5M {

	public A5M4() {
	}

	public static boolean bChangedPit = false;

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "A5M");
		Property.set(class1, "meshName", "3DO/Plane/A5M4(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "meshName_ja", "3DO/Plane/A5M4(ja)/hier.him");
		Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
		Property.set(class1, "yearService", 1938F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/A5M4.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitA5M4.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02" });
		weaponsRegister(class1, "default", new String[] { "MGunVikkersKs 500", "MGunVikkersKs 500", null, null });
		weaponsRegister(class1, "1xdt", new String[] { "MGunVikkersKs 500", "MGunVikkersKs 500", "PylonA5MPLN1", "FuelTankGun_TankA5M" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null });
	}
}
