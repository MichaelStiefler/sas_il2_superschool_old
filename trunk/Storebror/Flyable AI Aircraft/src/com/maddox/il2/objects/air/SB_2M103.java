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

public class SB_2M103 extends SB {
	static {
		Class class1 = SB_2M103.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "SB");
		Property.set(class1, "meshNameDemo", "3DO/Plane/SB-2M-103(Russian)/hier.him");
		Property.set(class1, "meshName", "3DO/Plane/SB-2M-103(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
		Property.set(class1, "meshName_ru", "3DO/Plane/SB-2M-103(Russian)/hier.him");
		Property.set(class1, "PaintScheme_ru", new PaintSchemeBCSPar01());
		Property.set(class1, "yearService", 1938F);
		Property.set(class1, "yearExpired", 1944F);
		Property.set(class1, "FlightModel", "FlightModels/SB-2M-103.fmd");
	    Property.set(class1, "cockpitClass", new Class[] { CockpitSB.class, CockpitSB_Bombardier.class, CockpitSB_NGunner.class, CockpitSB2M103_TGunner.class, CockpitSB_BGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 10, 11, 12, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_ExternalBomb01", "_ExternalBomb02" });
		weaponsRegister(class1, "default", new String[] { "MGunShKASt 960", "MGunShKASt 960", "MGunShKASt 1000", "MGunShKASt 500", null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "6xfab50", new String[] { "MGunShKASt 960", "MGunShKASt 960", "MGunShKASt 1000", "MGunShKASt 500", "BombGunFAB50", "BombGunFAB50", "BombGunFAB50", "BombGunFAB50", "BombGunFAB50", "BombGunFAB50", null, null, null });
		weaponsRegister(class1, "6xfab100", new String[] { "MGunShKASt 700", "MGunShKASt 700", "MGunShKASt 1000", "MGunShKASt 300", "BombGunFAB100", "BombGunFAB100", "BombGunFAB100", "BombGunFAB100", "BombGunFAB100", "BombGunFAB100", null, null, null });
		weaponsRegister(class1, "1xfab250", new String[] { "MGunShKASt 960", "MGunShKASt 960", "MGunShKASt 1000", "MGunShKASt 500", null, null, null, null, null, null, "BombGunFAB250", null, null });
		weaponsRegister(class1, "2xfab250", new String[] { "MGunShKASt 960", "MGunShKASt 960", "MGunShKASt 1000", "MGunShKASt 500", null, null, null, null, null, null, null, "BombGunFAB250", "BombGunFAB250" });
		weaponsRegister(class1, "2xfab500", new String[] { "MGunShKASt 960", "MGunShKASt 960", "MGunShKASt 1000", "MGunShKASt 500", null, null, null, null, null, null, null, "BombGunFAB500", "BombGunFAB500" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
