package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb100m30 extends Bomb {
    static {
        Class class1 = Bomb100m30.class;
        Property.set(class1, "mesh", "3do/arms/100kg_M1930/mono.sim");
        Property.set(class1, "radius", 65F);
        Property.set(class1, "power", 45F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.246F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
