// US Navy and Marine Corps LAU-10 , launcher for 4x 5inch Zuni rocket , High visibility white

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU10_Highvis_gn16 extends Pylon
{

    public Pylon_LAU10_Highvis_gn16()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("LAU_10", "LAU_10HV");
        mesh.materialReplace("LAU_10p", "LAU_10HVp");
        mesh.materialReplace("LAU_10q", "LAU_10HVq");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU10_Highvis_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU10_gn16/mono.sim");
        Property.set(class1, "massa", 63.5F);
        Property.set(class1, "dragCx", 0.029F);  // stock Pylons is +0.035F
    }
}