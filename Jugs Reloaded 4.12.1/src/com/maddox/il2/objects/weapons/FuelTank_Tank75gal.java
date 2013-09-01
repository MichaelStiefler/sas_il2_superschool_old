package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_Tank75gal extends FuelTank
{

    public FuelTank_Tank75gal()
    {
    }

    static 
    {
        Class class1 = FuelTank_Tank75gal.class;
        Property.set(class1, "mesh", "3DO/Arms/Tank75gal/mono.sim");
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 156F);
    }
}
