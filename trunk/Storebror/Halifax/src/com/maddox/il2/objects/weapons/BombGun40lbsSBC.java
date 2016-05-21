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

public class BombGun40lbsSBC extends BombGun
{
    static 
    {
        Class class1 = BombGun40lbsSBC.class;
        Property.set(class1, "bulletClass", (Object)Bomb40lbsSBC.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 4.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
