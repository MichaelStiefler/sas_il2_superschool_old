// US LAU-130 , launcher for 19x 2.75inch rocket -- Mk4 FFAR and HYDRA70
// same to LAU-3 , LAU-61

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;
import com.maddox.il2.objects.air.*;


public class Pylon_LAU130_gn16 extends Pylon
{

    public Pylon_LAU130_gn16()
    {
    }

    public void matGreen()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("LAU10o", "LAU10greeno");
        mesh.materialReplace("LAU10p", "LAU10greenp");
        mesh.materialReplace("LAU10q", "LAU10greenq");
    }

    public void matBrown()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("LAU10o", "LAU10browno");
        mesh.materialReplace("LAU10p", "LAU10brownp");
        mesh.materialReplace("LAU10q", "LAU10brownq");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU130_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU130_gn16/mono.sim");
        Property.set(class1, "massa", 93.0F);
        Property.set(class1, "dragCx", 0.030F);  // stock Pylons is +0.035F
    }
}