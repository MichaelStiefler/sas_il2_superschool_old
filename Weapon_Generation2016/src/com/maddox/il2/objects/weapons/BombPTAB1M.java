// Last Modified by: western0221 2019-01-09

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb

public class BombPTAB1M extends Bomb
{

    public BombPTAB1M()
    {
    }

    protected boolean haveSound()
    {
        return index % 25 == 0;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombPTAB1M.class;
        Property.set(class1, "mesh", "3do/arms/AO1Sch_bomblet/mono.sim");
        Property.set(class1, "radius", 0.35F);
        Property.set(class1, "power", 2.8F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.07F);
        Property.set(class1, "massa", 1.0F);
        Property.set(class1, "sound", "weapon.bomb_cassette");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_AD_A.class
        })));
    }
}
