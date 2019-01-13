// Russian KMGU-2 cluster bombet cointainer for Su-25 , MiG-27 and other attaker jets.
// Last Modified by: western0221 2019-01-12

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_KMGU2_gn16 extends Pylon
{

    public Pylon_KMGU2_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_KMGU2_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/KMGU2_gn16/mono.sim");
        Property.set(class1, "massa", 170.0F);    // KMGU-2's empty weight 170kg (not including internal cluster bomlets)
        Property.set(class1, "dragCx", 0.030F);  // stock Pylons is +0.035F
    }
}