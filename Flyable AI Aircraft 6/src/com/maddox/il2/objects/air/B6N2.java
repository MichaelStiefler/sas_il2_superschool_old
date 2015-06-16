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

import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class B6N2 extends B6N {

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "B6N");
		Property.set(class1, "meshName", "3DO/Plane/B6N2(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
		Property.set(class1, "meshName_ja", "3DO/Plane/B6N2(ja)/hier.him");
		Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar02());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1946.5F);
		Property.set(class1, "FlightModel", "FlightModels/B6N2.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitB6N2.class, CockpitB6N2_Bombardier.class, CockpitB6N2_TGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 11, 9, 3, 9, 3, 9, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalBomb02", "_ExternalDev06", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev04", "_ExternalDev05",
				"_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11" });
		weaponsRegister(class1, "default", new String[] { "MGunVikkersKt 500", "MGunVikkersKt 500", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "6x30", new String[] { "MGunVikkersKt 500", "MGunVikkersKt 500", null, null, null, null, null, null, null, "PylonB6NPLN1", "PylonB6NPLN1", "BombGun30kgJ 1", "BombGun30kgJ 1", "BombGun30kgJ 1", "BombGun30kgJ 1",
				"BombGun30kgJ 1", "BombGun30kgJ 1" });
		weaponsRegister(class1, "6x50", new String[] { "MGunVikkersKt 500", "MGunVikkersKt 500", null, null, null, null, null, null, null, "PylonB6NPLN1", "PylonB6NPLN1", "BombGun50kgJ 1", "BombGun50kgJ 1", "BombGun50kgJ 1", "BombGun50kgJ 1",
				"BombGun50kgJ 1", "BombGun50kgJ 1" });
		weaponsRegister(class1, "1x250", new String[] { "MGunVikkersKt 500", "MGunVikkersKt 500", null, null, "PylonB5NPLN1", "BombGun250kgJ 1", null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2x250", new String[] { "MGunVikkersKt 500", "MGunVikkersKt 500", null, null, null, null, "PylonB6NPLN1", "BombGun250kgJ 1", "BombGun250kgJ 1", null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "1x500", new String[] { "MGunVikkersKt 500", "MGunVikkersKt 500", null, null, "PylonB5NPLN1", "BombGun500kgJ 1", null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "1x600", new String[] { "MGunVikkersKt 500", "MGunVikkersKt 500", null, null, "PylonB5NPLN1", "BombGun600kgJ 1", null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "1x91", new String[] { "MGunVikkersKt 500", "MGunVikkersKt 500", "PylonB5NPLN0", "BombGunTorpType91 1", null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "1x91_late", new String[] { "MGunVikkersKt 500", "MGunVikkersKt 500", "PylonB5NPLN0", "BombGunTorpType91late 1", null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
