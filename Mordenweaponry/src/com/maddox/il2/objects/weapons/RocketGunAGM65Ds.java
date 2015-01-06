package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAGM65Ds extends MissileGun
implements RocketGunWithDelay
{

    public RocketGunAGM65Ds()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunAGM65Ds.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.MissileAGM65D.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5.0F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}
