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

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class MS406 extends MS400X {

	public MS406() {
		flapps = 0.0F;
	}

	public static String getSkinPrefix(String s, Regiment regiment) {
		if (regiment == null || regiment.country() == null)
			return "";
		if (regiment.country().equals("fi")) {
			int i = Mission.getMissionDate(true);
			if (i > 0) {
				if (i < 0x1282df2)
					return "winterwar_";
				if (i < 0x1282fd5)
					return "early_";
			}
		}
		return "";
	}

	public void update(float f) {
		resetYPRmodifier();
		xyz[1] = cvt(FM.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.2F, 0.0F);
		if (Math.abs(flapps - xyz[1]) > 0.01F) {
			flapps = xyz[1];
			hierMesh().chunkSetLocate("OilRad_D0", xyz, ypr);
		}
		super.update(f);
	}

	private float flapps;

	static {
		Class class1 = MS406.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Morane");
		Property.set(class1, "meshNameDemo", "3DO/Plane/MS406(fi)/hier.him");
		Property.set(class1, "meshName_fi", "3DO/Plane/MS406(fi)/hier.him");
		Property.set(class1, "PaintScheme_fi", new PaintSchemeFCSPar02());
		Property.set(class1, "meshName", "3DO/Plane/MS406(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
		Property.set(class1, "yearService", 1936F);
		Property.set(class1, "yearExpired", 1951.8F);
		Property.set(class1, "FlightModel", "FlightModels/MS406.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitMS406.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 1 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01" });
		weaponsRegister(class1, "default", new String[] { "MGunMAC1934 300", "MGunMAC1934 300", "MGunHispanoMkIki 60" });
		weaponsRegister(class1, "3xMAC1934", new String[] { "MGunMAC1934 300", "MGunMAC1934 300", "MGunMAC1934i 300" });
		weaponsRegister(class1, "none", new String[] { null, null, null });
	}
}
