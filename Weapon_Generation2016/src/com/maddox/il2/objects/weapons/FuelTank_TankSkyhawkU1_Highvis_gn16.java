
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class FuelTank_TankSkyhawkU1_Highvis_gn16 extends FuelTank
{

    public FuelTank_TankSkyhawkU1_Highvis_gn16()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Tank_Gloss", "Tank_GlossHV");
        mesh.materialReplace("Tank_GlossP", "Tank_GlossHVP");
        mesh.materialReplace("Tank_GlossQ", "Tank_GlossHVQ");
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_TankSkyhawkU1_Highvis_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/TankSkyhawk_gn16/monou1.sim");
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 1150F);
    }
}