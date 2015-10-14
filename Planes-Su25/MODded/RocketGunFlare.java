// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunFlare.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunFlare extends RocketGun
{

    public RocketGunFlare()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunFlare.class;
        Property.set(class1, "bulletClass", com.maddox.il2.objects.weapons.RocketFlare.class);
        Property.set(class1, "bullets", 3);
        Property.set(class1, "shotFreq", 5F);
        Property.set(class1, "sound", "weapon.Flare");
        Property.set(class1, "cassette", 1);
    }
}
