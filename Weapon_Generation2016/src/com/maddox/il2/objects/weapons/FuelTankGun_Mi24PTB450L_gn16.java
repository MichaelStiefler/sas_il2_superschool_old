// Last Modified by: western0221 2019-10-20

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            FuelTankGun

public class FuelTankGun_Mi24PTB450L_gn16 extends FuelTankGun
{

    public FuelTankGun_Mi24PTB450L_gn16()
    {
    }

    static 
    {
        Class var_class = com.maddox.il2.objects.weapons.FuelTankGun_Mi24PTB450L_gn16.class;
        Property.set(var_class, "bulletClass", (Object) FuelTank_Mi24PTB450L_gn16.class);
        Property.set(var_class, "bullets", 1);
        Property.set(var_class, "shotFreq", 0.25F);
        Property.set(var_class, "external", 1);
    }
}
