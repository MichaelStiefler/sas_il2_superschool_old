
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class FuelTankGun_TankF100_335gal_gn16 extends FuelTankGun
{

    public FuelTankGun_TankF100_335gal_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTankGun_TankF100_335gal_gn16.class;
        Property.set(class1, "bulletClass", (Object) FuelTank_TankF100_335gal_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
    }
}