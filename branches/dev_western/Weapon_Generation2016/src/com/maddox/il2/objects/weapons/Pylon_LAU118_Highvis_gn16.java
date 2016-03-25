// US LAU-118 pylon -- launcher rail for AGM-45 and AGM-88 missiles , High visibility cream

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU118_Highvis_gn16 extends Pylon
{

    public Pylon_LAU118_Highvis_gn16()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance2", "Ordnance2HV");
        mesh.materialReplace("Ordnance2p", "Ordnance2HVp");
        mesh.materialReplace("Ordnance2q", "Ordnance2HVq");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU118_Highvis_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU118_gn16/mono.sim");
        Property.set(class1, "massa", 54.4F);
        Property.set(class1, "dragCx", 0.008F);  // stock Pylons is +0.035F
    }
}