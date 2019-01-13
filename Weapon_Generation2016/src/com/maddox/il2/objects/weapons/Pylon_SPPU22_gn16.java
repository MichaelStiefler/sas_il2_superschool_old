// Russian SPPU-22 2x 23mm gunpod for Su-25 , Su-17 and other attaker jets.
// Last Modified by: western0221 2019-01-13

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_SPPU22_gn16 extends Pylon
{

    public Pylon_SPPU22_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_SPPU22_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/SPPU22_gn16/mono.sim");
        Property.set(class1, "massa", 235.0F);    // SPPU-22's empty weight 235kg (not including 260x round bullets @ 329g)
        Property.set(class1, "dragCx", 0.027F);  // stock Pylons is +0.035F
    }
}