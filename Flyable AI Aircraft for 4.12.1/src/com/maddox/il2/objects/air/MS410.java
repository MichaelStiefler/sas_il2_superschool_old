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

public class MS410 extends MS400X {

	public MS410() {
		flapps = 0.0F;
	}

	public void update(float f) {
		float f1 = FM.EI.engines[0].getControlRadiator();
		if (Math.abs(flapps - f1) > 0.01F) {
			flapps = f1;
			hierMesh().chunkSetAngles("OilRad_D0", 0.0F, -20F * f1, 0.0F);
		}
		super.update(f);
	}

	private float flapps;

	static {
		Class class1 = MS410.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Morane");
		Property.set(class1, "meshNameDemo", "3DO/Plane/MS410(fi)/hier.him");
		Property.set(class1, "meshName_fi", "3DO/Plane/MS410(fi)/hier.him");
		Property.set(class1, "PaintScheme_fi", new PaintSchemeFCSPar02());
		Property.set(class1, "meshName", "3DO/Plane/MS410(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "yearService", 1938F);
		Property.set(class1, "yearExpired", 1951.8F);
		Property.set(class1, "FlightModel", "FlightModels/MS410.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitMS410.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01" });
		weaponsRegister(class1, "default", new String[] { "MGunMAC1934 500", "MGunMAC1934 500", "MGunMAC1934 500", "MGunMAC1934 500", "MGunHispanoMkIki 60" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null });
	}
}
