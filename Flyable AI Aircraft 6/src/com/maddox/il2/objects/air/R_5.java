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

public class R_5 extends R_5xyz {

	public R_5() {
	}

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "R-5");
		Property.set(class1, "meshName", "3do/plane/R-5/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFCSPar08());
		Property.set(class1, "yearService", 1931F);
		Property.set(class1, "yearExpired", 1944F);
		Property.set(class1, "FlightModel", "FlightModels/R-5.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitR_5.class, CockpitR5Gunner.class });
		weaponTriggersRegister(class1, new int[] { 0, 10, 10, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06",
				"_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18",
				"_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04",
				"_ExternalDev05" });
		weaponsRegister(class1, "default", new String[] { "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "Gunpods", new String[] { "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", "MGunPV1i 200", "MGunPV1i 200", "MGunPV1i 200", "MGunPV1i 200", null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonR5GPodL 1", "PylonR5GPodR 1", null, null, null });
		weaponsRegister(class1, "Gunpods+8x10+2x100", new String[] { "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", "MGunPV1i 200", "MGunPV1i 200", "MGunPV1i 200", "MGunPV1i 200", null, null, null, null, null, null, null, null,
				"BombGunFAB100 1", "BombGunFAB100 1", "BombGunAO10S 1", null, "BombGunAO10S 1", null, "BombGunAO10S 1", null, "BombGunAO10S 1", null, "BombGunAO10S 1", null, "BombGunAO10S 1", null, "BombGunAO10S 1", null, "BombGunAO10S 1", null,
				"PylonR5GPodL 1", "PylonR5GPodR 1", "PylonR5BombRackL 1", "PylonR5BombRackR 1", "PylonR5BombRackC 1" });
		weaponsRegister(class1, "Gunpods+16x10+2x100", new String[] { "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", "MGunPV1i 200", "MGunPV1i 200", "MGunPV1i 200", "MGunPV1i 200", null, null, null, null, null, null, null, null,
				"BombGunFAB100 1", "BombGunFAB100 1", "BombGunAO10S 1", "BombGunAO10S 1", "BombGunAO10S 1", "BombGunAO10S 1", "BombGunAO10S 1", "BombGunAO10S 1", "BombGunAO10S 1", "BombGunAO10S 1", "BombGunAO10S 1", "BombGunAO10S 1", "BombGunAO10S 1",
				"BombGunAO10S 1", "BombGunAO10S 1", "BombGunAO10S 1", "BombGunAO10S 1", "BombGunAO10S 1", "PylonR5GPodL 1", "PylonR5GPodR 1", "PylonR5BombRackL 1", "PylonR5BombRackR 1", "PylonR5BombRackC 1" });
		weaponsRegister(class1, "Gunpods+4x50+2x50", new String[] { "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", "MGunPV1i 200", "MGunPV1i 200", "MGunPV1i 200", "MGunPV1i 200", null, null, null, null, "BombGunFAB50 1", "BombGunFAB50 1",
				"BombGunFAB50 1", "BombGunFAB50 1", "BombGunFAB50 1", "BombGunFAB50 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonR5GPodL 1", "PylonR5GPodR 1", "PylonR5BombRackL 1",
				"PylonR5BombRackR 1", "PylonR5BombRackC 1" });
        weaponsRegister(class1, "Gunpods+4x100", new String[] {
                "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", "MGunPV1i 200", "MGunPV1i 200", "MGunPV1i 200", "MGunPV1i 200", null, null, null, 
                null, null, null, "BombGunFAB100 1", "BombGunFAB100 1", "BombGunFAB100 1", "BombGunFAB100 1", null, null, null, 
                null, null, null, null, null, null, null, null, null, null, 
                null, null, null, "PylonR5GPodL 1", "PylonR5GPodR 1", "PylonR5BombRackL 1", "PylonR5BombRackR 1", "PylonR5BombRackC 1"
            });
		weaponsRegister(class1, "8x50+2x100", new String[] { "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", null, null, null, null, "BombGunFAB50 1", "BombGunFAB50 1", "BombGunFAB50 1", "BombGunFAB50 1", "BombGunFAB50 1", "BombGunFAB50 1",
				"BombGunFAB50 1", "BombGunFAB50 1", "BombGunFAB100 1", "BombGunFAB100 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonR5BombRackL 1", "PylonR5BombRackR 1",
				"PylonR5BombRackC 1" });
		weaponsRegister(class1, "2x100+2x100", new String[] { "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", null, null, null, null, null, null, null, null, null, null, "BombGunFAB100 1", "BombGunFAB100 1", "BombGunFAB100 1", "BombGunFAB100 1",
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonR5BombRackL 1", "PylonR5BombRackR 1", "PylonR5BombRackC 1" });
		weaponsRegister(class1, "2x100+2x50+2x100", new String[] { "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", null, null, null, null, null, null, null, null, "BombGunFAB50 1", "BombGunFAB50 1", "BombGunFAB100 1", "BombGunFAB100 1",
				"BombGunFAB100 1", "BombGunFAB100 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonR5BombRackL 1", "PylonR5BombRackR 1", "PylonR5BombRackC 1" });
        weaponsRegister(class1, "2xfab250", new String[] {
                "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", null, null, null, null, null, null, null, 
                null, null, null, "BombGunFAB250 1", "BombGunFAB250 1", null, null, null, null, null, 
                null, null, null, null, null, null, null, null, null, null, 
                null, null, null, null, null, "PylonR5BombRackL 1", "PylonR5BombRackR 1", null
            });
            weaponsRegister(class1, "2xrrab3", new String[] {
                "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", null, null, null, null, null, null, null, 
                null, null, null, null, null, null, null, null, null, null, 
                null, null, null, null, null, null, null, null, null, null, 
                "BombGunRRAB3 1", null, "BombGunRRAB3 1", null, null, "PylonR5BombRackL 1", "PylonR5BombRackR 1", null
            });
            weaponsRegister(class1, "2xrrab3_1", new String[] {
                "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", null, null, null, null, null, null, null, 
                null, null, null, null, null, null, null, null, null, null, 
                null, null, null, null, null, null, null, null, null, null, 
                "BombGunRRAB3_1 1", null, "BombGunRRAB3_1 1", null, null, "PylonR5BombRackL 1", "PylonR5BombRackR 1", null
            });
            weaponsRegister(class1, "2x100+2xrrab3", new String[] {
                "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", null, null, null, null, null, null, null, 
                null, null, null, null, null, "BombGunFAB100 1", "BombGunFAB100 1", null, null, null, 
                null, null, null, null, null, null, null, null, null, null, 
                "BombGunRRAB3 1", null, "BombGunRRAB3 1", null, null, "PylonR5BombRackL 1", "PylonR5BombRackR 1", "PylonR5BombRackC 1"
            });
            weaponsRegister(class1, "2x100+2xrrab3_1", new String[] {
                "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", null, null, null, null, null, null, null, 
                null, null, null, null, null, "BombGunFAB100 1", "BombGunFAB100 1", null, null, null, 
                null, null, null, null, null, null, null, null, null, null, 
                "BombGunRRAB3_1 1", null, "BombGunRRAB3_1 1", null, null, "PylonR5BombRackL 1", "PylonR5BombRackR 1", "PylonR5BombRackC 1"
            });
            weaponsRegister(class1, "rrab3+fab250", new String[] {
                "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", null, null, null, null, null, null, null, 
                null, null, null, "BombGunFAB250 1", null, null, null, null, null, null, 
                null, null, null, null, null, null, null, null, null, null, 
                null, null, "BombGunRRAB3 1", null, null, "PylonR5BombRackL 1", "PylonR5BombRackR 1", null
            });
            weaponsRegister(class1, "rrab3_1+fab250", new String[] {
                "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", null, null, null, null, null, null, null, 
                null, null, null, "BombGunFAB250 1", null, null, null, null, null, null, 
                null, null, null, null, null, null, null, null, null, null, 
                null, null, "BombGunRRAB3_1 1", null, null, "PylonR5BombRackL 1", "PylonR5BombRackR 1", null
            });
            weaponsRegister(class1, "4x100+6xsab3", new String[] {
                "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", null, null, null, null, "BombGunSAB3m 1", "BombGunSAB3m 1", "BombGunSAB3m 1", 
                "BombGunSAB3m 1", "BombGunSAB3m 1", "BombGunSAB3m 1", "BombGunFAB100 1", "BombGunFAB100 1", "BombGunFAB100 1", "BombGunFAB100 1", null, null, null, 
                null, null, null, null, null, null, null, null, null, null, 
                null, null, null, null, null, "PylonR5BombRackL 1", "PylonR5BombRackR 1", "PylonR5BombRackC 1"
            });
            weaponsRegister(class1, "4x100+2xsab15", new String[] {
                "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", null, null, null, null, null, null, null, 
                null, "BombGunSAB15 1", "BombGunSAB15 1", "BombGunFAB100 1", "BombGunFAB100 1", "BombGunFAB100 1", "BombGunFAB100 1", null, null, null, 
                null, null, null, null, null, null, null, null, null, null, 
                null, null, null, null, null, "PylonR5BombRackL 1", "PylonR5BombRackR 1", "PylonR5BombRackC 1"
            });
            weaponsRegister(class1, "2xfab2502xsab15", new String[] {
                "MGunPV1sipzl 500", "MGunDA762t 500", "MGunDA762t 500", null, null, null, null, null, null, null, 
                null, null, null, "BombGunFAB250 1", "BombGunFAB250 1", "BombGunSAB15 1", "BombGunSAB15 1", null, null, null, 
                null, null, null, null, null, null, null, null, null, null, 
                null, null, null, null, null, "PylonR5BombRackL 1", "PylonR5BombRackR 1", "PylonR5BombRackC 1"
            });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null });
	}
}
