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

public class P_36A3 extends P_36 {

	public P_36A3() {
		kangle = 0.0F;
		flapps = 0.0F;
	}

	public static String getSkinPrefix(String s, Regiment regiment) {
        if(regiment == null || regiment.country() == null)
            return "P36_";
        if(regiment.country().equals("fi"))
        {
            int i = Mission.getMissionDate(true);
            if(i > 0)
            {
                if(i < 0x1282fd5)
                    return "early_";
                if(i > 0x12855b9)
                    return "late_";
            }
            return "";
        }
        if(regiment.country().equals("fr"))
            return "FR_";
        if(!regiment.country().equals("us"))
            return "P36_";
        else
            return "";
	}

	public void update(float f) {
		if (Math.abs(flapps - kangle) > 0.01F) {
			flapps = kangle;
			for (int i = 1; i < 12; i++) {
				String s = "Water" + i + "_D0";
				hierMesh().chunkSetAngles(s, 0.0F, -10F * kangle, 0.0F);
			}

		}
		kangle = 0.95F * kangle + 0.05F * FM.EI.engines[0].getControlRadiator();
		super.update(f);
	}

	private float kangle;
	private float flapps;

	static {
		Class class1 = P_36A3.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "P-36");
		Property.set(class1, "meshName", "3DO/Plane/Hawk75A-3(Multi1)/P-36_hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar07());
		Property.set(class1, "meshName_us", "3DO/Plane/Hawk75A-3(USA)/P-36_hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(class1, "yearService", 1938F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/P-36A.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitP_36.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning50si 200", "MGunBrowning50si 200", "MGunBrowning303k 500", "MGunBrowning303k 500", "MGunBrowning303k 500", "MGunBrowning303k 500" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null });
//	    weaponTriggersRegister(class1, new int[] { 0, 0 });
//	    weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
//	    weaponsRegister(class1, "default", new String[] { "MGunBrowning50si 200", "MGunBrowning303si 600" });
//	    weaponsRegister(class1, "none", new String[] { null, null });
	}
}
