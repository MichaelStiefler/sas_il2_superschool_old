// This file is part of the SAS IL-2 Sturmovik 1946
// Westland Whirlwind Mod.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Mod Creator:          tfs101
// Original file source: SAS~Storebror
// Modified by:          SAS - Special Aircraft Services
//                       www.sas1946.com
//
// Last Edited by:       SAS~Storebror
// Last Edited at:       2013/10/17

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb30LbsInc extends Bomb
{
    static 
    {
        Class class1 = Bomb30LbsInc.class;
        Property.set(class1, "mesh", "3DO/Arms/Bomb30lbsInc/mono.sim");
        Property.set(class1, "power", 6.8F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.1524F);
        Property.set(class1, "massa", 11.36F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "randomOrient", 0);
        try {
        	Class fuze = Class.forName("com.maddox.il2.objects.weapons.Fuze_generic_instant");
        	Property.set(class1, "fuze", new Object[] { fuze });
        } catch (Exception e) { }
//        Property.set(class1, "fuze", new Object[] {Fuze_generic_instant.class});
    }
}
