// US LAU-130 , launcher for 19x 2.75inch rocket -- Mk4 FFAR and HYDRA70 , gray color
// same to LAU-3 , LAU-61
// with tail fairing version

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU130_grayTC_gn16 extends Pylon
{

    public Pylon_LAU130_grayTC_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU130_grayTC_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU130_gn16/monoTC.sim");
        Property.set(class1, "massa", 93.0F);
        Property.set(class1, "dragCx", 0.028F);  // stock Pylons is +0.035F
    }
}