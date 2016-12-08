// 275 gal. Fueltank gun for F-5E TigerII with 3x tailfin.

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
                    if(((FlightModelMain) (super.FM)).CT.Weapons[i][j] instanceof FuelTankGun_TankF5E275gal_gn16)
                        ((FuelTankGun_TankF5E275gal_gn16)((FlightModelMain) (super.FM)).CT.Weapons[i][j]).matTwoTone();
                }
            }
    }

* matGreen() is also prepared for Vietnum camo green.
*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class FuelTankGun_TankF5E275gal_gn16 extends FuelTankGun
{

    public FuelTankGun_TankF5E275gal_gn16()
    {
    }

    public void matTwoTone()
    {
        ((FuelTank_TankF5E275gal_gn16) bomb).matTwoTone();
    }

    public void matGreen()
    {
        ((FuelTank_TankF5E275gal_gn16) bomb).matGreen();
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
        Class class1 = com.maddox.il2.objects.weapons.FuelTankGun_TankF5E275gal_gn16.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.FuelTank_TankF5E275gal_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
    }
}