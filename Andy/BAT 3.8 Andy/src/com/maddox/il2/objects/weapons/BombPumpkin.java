// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 21.01.2020 16:12:30
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   BombPumpkin.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb

public class BombPumpkin extends com.maddox.il2.objects.weapons.Bomb
{

    public BombPumpkin()
    {
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.weapons.BombPumpkin.class;
        com.maddox.rts.Property.set(class1, "mesh", "3DO/Arms/FatMan/mono.sim");
        com.maddox.rts.Property.set(class1, "power", 2900F);
        com.maddox.rts.Property.set(class1, "powerType", 0);
        com.maddox.rts.Property.set(class1, "kalibr", 0.8128F);
        com.maddox.rts.Property.set(class1, "massa", 5300F);
        com.maddox.rts.Property.set(class1, "sound", "weapon.bomb_big");
        com.maddox.rts.Property.set(class1, "fuze", ((java.lang.Object) (new java.lang.Object[] {
            com.maddox.il2.objects.weapons.Fuze_EL_AZ25.class
        })));
    }
}