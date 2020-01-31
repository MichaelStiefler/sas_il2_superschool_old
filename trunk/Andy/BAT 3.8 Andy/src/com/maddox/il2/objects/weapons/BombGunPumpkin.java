// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 21.01.2020 16:15:02
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   BombGunPumpkin.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            BombGun

public class BombGunPumpkin extends com.maddox.il2.objects.weapons.BombGun
{

    public BombGunPumpkin()
    {
    }


    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.weapons.BombGunPumpkin.class;
        com.maddox.rts.Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.BombPumpkin.class);
        com.maddox.rts.Property.set(class1, "bullets", 1);
        com.maddox.rts.Property.set(class1, "shotFreq", 0.25F);
        com.maddox.rts.Property.set(class1, "external", 1);
        com.maddox.rts.Property.set(class1, "sound", "weapon.bombgun");
    }
}