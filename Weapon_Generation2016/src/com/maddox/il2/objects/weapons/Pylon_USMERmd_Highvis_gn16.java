
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_USMERmd_Highvis_gn16 extends Pylon
{

    public Pylon_USMERmd_Highvis_gn16()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance1", "Ordnance1HV");
        mesh.materialReplace("Ordnance1p", "Ordnance1HVp");
        mesh.materialReplace("Ordnance1q", "Ordnance1HVq");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_USMERmd_Highvis_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_US_TERMER_gn16/USMERmd.sim");
        Property.set(class1, "massa", 90.0F);
        Property.set(class1, "dragCx", 0.036F);  // stock Pylons is +0.035F
    }
}