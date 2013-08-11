package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_Tank165gal2 extends FuelTank
{

    public FuelTank_Tank165gal2()
    {
    }

    static 
    {
        Class class1 = FuelTank_Tank165gal2.class;
        Property.set(class1, "mesh", "3DO/Arms/P-38_tank/mono.sim");
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 450.5F);
    }
}
