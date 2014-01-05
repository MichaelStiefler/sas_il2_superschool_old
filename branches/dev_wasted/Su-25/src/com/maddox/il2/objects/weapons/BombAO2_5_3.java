// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 21.12.2013 21:00:33
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BombAO2_5_3.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb

public class BombAO2_5_3 extends Bomb
{

    public BombAO2_5_3()
    {
    }

    protected boolean haveSound()
    {
        return index % 16 == 0;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombAO2_5_3.class;
        Property.set(class1, "mesh", "3do/arms/AO-2_5-3/mono.sim");
        Property.set(class1, "radius", 4F);
        Property.set(class1, "power", 0.12F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.1F);
        Property.set(class1, "massa", 2.0F);
        Property.set(class1, "randomOrient", 1);
        Property.set(class1, "sound", "weapon.bomb_cassette");
        Property.set(class1, "fuze", new Object[] { Fuze_AM_A.class, Fuze_AVSh_2.class });
    }
}