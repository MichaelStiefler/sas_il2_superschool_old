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

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class IL_4_DB3T extends IL_4 implements TypeBomber {

	public IL_4_DB3T() {
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.crew = 4;
	}

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    public static boolean bChangedPit = false;

    static {
		Class class1 = IL_4_DB3T.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "DB-3");
		Property.set(class1, "meshName", "3DO/Plane/DB-3T/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
		Property.set(class1, "yearService", 1936F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/DB-3T.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitDB3T.class, CockpitDB3T_FGunner.class, CockpitDB3T_TGunner.class, CockpitDB3T_BGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 11, 12, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01" });
		weaponsRegister(class1, "default", new String[] { "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", null });
		weaponsRegister(class1, "torp1", new String[] { "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", "BombGun4512" });
		weaponsRegister(class1, "1x53mmCirc", new String[] { "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", "BombGunTorp45_36AV_A" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null });
	}
}
