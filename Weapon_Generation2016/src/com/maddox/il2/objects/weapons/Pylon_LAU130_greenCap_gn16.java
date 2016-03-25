// aero dynamics "Fairing" part of "Pylon_LAU130" , 19x 2.75inch rocket -- Mk4 FFAR and HYDRA70 , green color


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU130_greenCap_gn16 extends Pylon
{

    public Pylon_LAU130_greenCap_gn16()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("LAU10o", "LAU10greeno");
        mesh.materialReplace("LAU10p", "LAU10greenp");
        mesh.materialReplace("LAU10q", "LAU10greenq");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU130_greenCap_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU130_gn16/monocap.sim");
        Property.set(class1, "massa", 6F);
        Property.set(class1, "dragCx", 0.01F);  // stock Pylons is +0.035F
        Property.set(class1, "bMinusDrag", 1);  // working inverting dragCx value into minus drag as -0.01F
    }
}