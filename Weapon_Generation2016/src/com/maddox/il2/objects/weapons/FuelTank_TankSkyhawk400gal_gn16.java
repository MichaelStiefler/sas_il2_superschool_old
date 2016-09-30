// 400 gal. Fueltank for A-4 Skyhawk and A-1 Skyraider with horizontal 2x tailfins

/*
* Base color is low visibility dark gray for 1980s and later.

* When you want high visibility white gray for 1960-1970s, add this code to mother Jets.

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
                    if(((FlightModelMain) (super.FM)).CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk400gal_gn16)
                        ((FuelTankGun_TankSkyhawk400gal_gn16)((FlightModelMain) (super.FM)).CT.Weapons[i][j]).matHighvis();
                }
            }
    }

*/

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class FuelTank_TankSkyhawk400gal_gn16 extends FuelTank
{

    public FuelTank_TankSkyhawk400gal_gn16()
    {
    }

    public void matHighvis()
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
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_TankSkyhawk400gal_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/TankSkyhawk400gal_gn16/mono.sim");
        Property.set(class1, "kalibr", 0.707F);
        Property.set(class1, "massa", 1535F);
    }
}