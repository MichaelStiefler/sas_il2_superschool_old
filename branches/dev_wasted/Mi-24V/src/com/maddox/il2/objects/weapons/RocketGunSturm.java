// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 09.02.2013 19:29:33
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunAGM65.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunSturm extends RocketGun
{

    public RocketGunSturm()
    {
    }


    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunSturm.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.Sturm.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.1F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}