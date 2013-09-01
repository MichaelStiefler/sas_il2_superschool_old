package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTankGun_Tank165gal extends FuelTankGun
{

    public FuelTankGun_Tank165gal()
    {
    }

    static 
    {
        Class class1 = FuelTankGun_Tank165gal.class;
        Property.set(class1, "bulletClass", (Object)FuelTank_Tank165gal.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
    }
}
