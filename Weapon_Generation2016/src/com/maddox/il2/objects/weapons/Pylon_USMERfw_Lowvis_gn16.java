
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_USMERfw_Lowvis_gn16 extends Pylon
{

    public Pylon_USMERfw_Lowvis_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_USMERfw_Lowvis_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_US_TERMER_n16/USMERfw.sim");
        Property.set(class1, "massa", 90.0F);
        Property.set(class1, "dragCx", 0.036F);  // stock Pylons is +0.035F
    }
}