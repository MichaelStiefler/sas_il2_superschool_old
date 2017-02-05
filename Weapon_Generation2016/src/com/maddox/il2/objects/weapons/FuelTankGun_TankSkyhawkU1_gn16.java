// 300 gal. Fueltank gun for A-4 Skyhawk and A-6 Intruder with 1x under tailfin

/*
* Base color is low visibility dark gray for 1980s and later.

* When you want high visibility white gray for 1960-1970s, add this code to mother Jets.

    public void missionStarting()
    {
        super.missionStarting();
        checkChangeWeaponColors();
    }

    private void checkChangeWeaponColors()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkU1_gn16)
                        ((FuelTankGun_TankSkyhawkU1_gn16)FM.CT.Weapons[i][j]).matHighvis();
                }
            }
    }

*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class FuelTankGun_TankSkyhawkU1_gn16 extends FuelTankGun
{

    public FuelTankGun_TankSkyhawkU1_gn16()
    {
    }

    public void matHighvis()
    {
        ((FuelTank_TankSkyhawkU1_gn16) bomb).matHighvis();
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
        Class class1 = com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawkU1_gn16.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.FuelTank_TankSkyhawkU1_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
    }
}