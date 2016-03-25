
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU7_Highvis_gn16 extends Pylon
{

    public Pylon_LAU7_Highvis_gn16()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance1", "Ordnance1HV");
        mesh.materialReplace("Ordnance1p", "Ordnance1HVp");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU7_Highvis_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU7_gn16/mono.sim");
        Property.set(class1, "massa", 40.8F);
        Property.set(class1, "dragCx", 0.003F);  // stock Pylons is +0.035F
    }
}