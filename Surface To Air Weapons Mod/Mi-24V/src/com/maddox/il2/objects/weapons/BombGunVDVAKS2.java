package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            BombGun

public class BombGunVDVAKS2 extends BombGun
{

    public BombGunVDVAKS2()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunVDVAKS2.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.BombVDVAKS.class);
        Property.set(class1, "bullets", 15);
        Property.set(class1, "shotFreq", 3.1F);
        Property.set(class1, "external", 0);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", (Object)null);
    }
}