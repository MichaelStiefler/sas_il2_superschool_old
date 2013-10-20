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

public class Bomb20lbsSBC extends Bomb
{
    protected boolean haveSound()
    {
        return false;
    }

    static 
    {
        Class class1 = Bomb20lbsSBC.class;
        Property.set(class1, "mesh", "3DO/Arms/Bomb20lbsSBC/mono.sim");
        Property.set(class1, "power", 4.55F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.12827F);
        Property.set(class1, "massa", 9.07F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        try {
        	Class fuze1 = Class.forName("com.maddox.il2.objects.weapons.Fuze_PistolNo45");
        	Class fuze2 = Class.forName("com.maddox.il2.objects.weapons.Fuze_PistolNo34");
        	Property.set(class1, "fuze", new Object[] { fuze1, fuze2 });
        } catch (Exception e) { }
    }
}
