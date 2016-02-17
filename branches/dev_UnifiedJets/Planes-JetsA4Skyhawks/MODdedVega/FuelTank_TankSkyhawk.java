// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 03/11/2015 10:48:31 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FuelTank_TankSkyhawk.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            FuelTank

public class FuelTank_TankSkyhawk extends FuelTank
{

    public FuelTank_TankSkyhawk()
    {
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

//    static Class _mthclass$(String s)
//    {
//        try
//        {
//            return Class.forName(s);
//        }
//        catch(ClassNotFoundException classnotfoundexception)
//        {
//            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
//        }
//    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_TankSkyhawk.class;
        Property.set(class1, "mesh", "3DO/Arms/TankSkyhawk/mono.sim");
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 1150F);
    }
}
