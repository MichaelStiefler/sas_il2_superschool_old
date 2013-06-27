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

import com.maddox.rts.Property;

public class JU_87G2RUDEL extends JU_87 implements TypeStormovik, TypeAcePlane {

	public JU_87G2RUDEL() {
		flapps = 0.0F;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.Skill = 3;
	}

	public void update(float f) {
		float f1 = FM.EI.engines[0].getControlRadiator();
		if (Math.abs(flapps - f1) > 0.01F) {
			flapps = f1;
			for (int i = 1; i < 5; i++) {
				String s = "Water" + i + "_D0";
				hierMesh().chunkSetAngles(s, 0.0F, 15F - 30F * f1, 0.0F);
			}

		}
		super.update(f);
	}

	private float flapps;

	static {
		Class class1 = JU_87G2RUDEL.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Ju-87");
		Property.set(class1, "meshName", "3do/plane/Ju-87G-2(ofRudel)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
		Property.set(class1, "FlightModel", "FlightModels/Ju-87G-2(ofRudel).fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitJU_87D3.class, CockpitJU_87G2RUDEL_Gunner.class });
		weaponTriggersRegister(class1, new int[] { 1, 1, 10, 10 });
		weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02" });
		weaponsRegister(class1, "default", new String[] { "MGunBK37JU87 12", "MGunBK37JU87 12", "MGunMG81t 250", "MGunMG81t 250" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null });
	}
}
