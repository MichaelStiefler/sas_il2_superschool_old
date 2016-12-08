// 150 gal. Fueltank for F-5A Freedom Fighter with 3x tailfin.

/*
* Base color is gray.

* When you want two tone of dark and bright gray, add this code to mother Jets.

    public void missionStarting()
    {
        checkChangeWeaponColors();
    }

    private void checkChangeWeaponColors()
    {
        for(int i = 0; i < ((FlightModelMain) (super.FM)).CT.Weapons.length; i++)
            if(((FlightModelMain) (super.FM)).CT.Weapons[i] != null)
            {
                for(int j = 0; j < ((FlightModelMain) (super.FM)).CT.Weapons[i].length; j++)
                {
                    if(((FlightModelMain) (super.FM)).CT.Weapons[i][j] instanceof FuelTankGun_TankF5A150galT3_gn16)
                        ((FuelTankGun_TankF5A150galT3_gn16)((FlightModelMain) (super.FM)).CT.Weapons[i][j]).matTwoTone();
                }
            }
    }

* matGreen() is also prepared for Vietnum camo green.
*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class FuelTank_TankF5A150galT3_gn16 extends FuelTank
{

    public FuelTank_TankF5A150galT3_gn16()
    {
    }

    public void matTwoTone()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Tank_Gloss", "Tank_Gloss2t");
        mesh.materialReplace("Tank_GlossP", "Tank_Gloss2tP");
    }

    public void matGreen()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Tank_Gloss", "Tank_GlossGr");
        mesh.materialReplace("Tank_GlossP", "Tank_GlossGrP");
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
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_TankF5A150galT3_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/TankF5A150gal_gn16/monot3.sim");
        Property.set(class1, "kalibr", 0.53F);
        Property.set(class1, "massa", 550.4F);
    }
}