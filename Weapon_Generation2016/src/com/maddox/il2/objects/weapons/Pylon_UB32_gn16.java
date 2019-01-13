// USSR UB-32, launcher for 32x S-5 55mm rocket

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_UB32_gn16 extends Pylon
{

    public Pylon_UB32_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_UB32_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_UB32_gn16/mono.sim");
        Property.set(class1, "massa", 103.0F);
        Property.set(class1, "dragCx", 0.022F);  // stock Pylons is +0.035F
    }
}