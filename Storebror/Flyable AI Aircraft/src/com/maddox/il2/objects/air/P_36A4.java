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
// Last Edited at: 2013/06/12

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class P_36A4 extends P_36 {

	public P_36A4() {
		kangle = 0.0F;
		flapps = 0.0F;
	}

	public static String getSkinPrefix(String s, Regiment regiment) {
        if(regiment == null || regiment.country() == null)
            return "generic_";
        if(regiment.country().equals("du"))
            return "NL_";
        if(regiment.country().equals("gb"))
        {
            int i = Mission.getMissionDate(true);
            if(i > 0 && (World.cur().camouflage == 3 || World.cur().camouflage == 6) && i < 0x1287a70)
                return "RAF_early_";
            else
                return "RAF_";
        }
        if(regiment.country().equals("fi"))
            return "";
        else
            return "generic_";
	}

	public void update(float f) {
		if (Math.abs(flapps - kangle) > 0.01F) {
			flapps = kangle;
			for (int i = 1; i < 9; i++) {
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
		Class class1 = P_36A4.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Mohawk");
		Property.set(class1, "meshName", "3DO/Plane/Hawk75A-4(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar08());
		Property.set(class1, "meshName_us", "3DO/Plane/Hawk75A-4(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFCSPar02());
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/P-36A-4.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitP_36.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07",
				"_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning303si 600", "MGunBrowning303si 600", "MGunBrowning303k 500", "MGunBrowning303k 500", "MGunBrowning303k 500", "MGunBrowning303k 500", null, null, null, null, null, null, null, null,
				null, null });
        weaponsRegister(class1, "6x40lb_bombs", new String[] {
                "MGunBrowning303si 600", "MGunBrowning303si 600", "MGunBrowning303k 500", "MGunBrowning303k 500", "MGunBrowning303k 500", "MGunBrowning303k 500", null, null, null, null, 
                "BombGun40lbsE", "BombGun40lbsE", "BombGun40lbsE", "BombGun40lbsE", "BombGun40lbsE", "BombGun40lbsE"
            });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
