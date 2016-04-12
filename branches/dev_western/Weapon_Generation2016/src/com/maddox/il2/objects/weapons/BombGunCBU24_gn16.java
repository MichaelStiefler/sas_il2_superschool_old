// US CBU-24 Cluster bomb ('s gun) in SUU-30B/B dispenser

/*
* Base color is dark brown for Air Force F-105 and F-4, A-7

* When you want gray color for Navy or Marine Corps Jets F-4, A-4, A-6, A-7, add this code to mother Jets.

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
                    if(((FlightModelMain) (super.FM)).CT.Weapons[i][j] instanceof BombGunCBU24_gn16)
                        ((BombGunCBU24_gn16)((FlightModelMain) (super.FM)).CT.Weapons[i][j]).matGray();
                }
            }
    }

*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class BombGunCBU24_gn16 extends BombGun
{

    public BombGunCBU24_gn16()
    {
    }

    public void matGray()
    {
        ((BombCBU24_gn16) bomb).matGray();
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
        Class class1 = com.maddox.il2.objects.weapons.BombGunCBU24_gn16.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombCBU24_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
