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
// Last Edited at: 2013/01/22

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.rts.Property;

public class AR_196A3 extends AR_196 {

	public AR_196A3() {
	}

	public void update(float f) {
		super.update(f);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++)
				if (FM.Gears.clpGearEff[i][j] != null) {
					tmpp.set(FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
					tmpp.z = 0.01D;
					FM.Gears.clpGearEff[i][j].pos.setAbs(tmpp);
					FM.Gears.clpGearEff[i][j].pos.reset();
				}

		}

	}

	private static Point3d tmpp = new Point3d();

	static {
		Class class1 = AR_196A3.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "FlightModel", "FlightModels/Ar-196A-3.fmd");
		Property.set(class1, "meshName", "3DO/Plane/Ar-196A-3/hier.him");
		Property.set(class1, "iconFar_shortClassName", "Ar-196");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
		Property.set(class1, "yearService", 1938.5F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "cockpitClass", new Class[] { CockpitAR_196.class, CockpitAR_196_Gunner.class });
		weaponTriggersRegister(class1, new int[] { 0, 1, 1, 10, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_CANNON01", "_CANNON02", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02" });
		weaponsRegister(class1, "default", new String[] { "MGunMG17si 500", "MGunMGFFki 60", "MGunMGFFki 60", "MGunMG15t 525", null, null });
		weaponsRegister(class1, "2sc50", new String[] { "MGunMG17si 500", "MGunMGFFki 60", "MGunMGFFki 60", "MGunMG15t 525", "BombGunSC50 1", "BombGunSC50 1" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null });
	}
}
