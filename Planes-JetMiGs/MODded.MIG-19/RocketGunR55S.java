// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 8/14/2013 6:02:05 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunR55S.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MissileGun, RocketGunWithDelay

public class RocketGunR55S extends MissileGun
    implements RocketGunWithDelay
{

    public RocketGunR55S()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunR55S.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.MissileR55S.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}