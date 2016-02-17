// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 17/10/2015 02:32:48 p.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FlightModelMainAIM9B.java

package com.maddox.il2.fm;


// Referenced classes of package com.maddox.il2.fm:
//            FlightModelMain, Arm

public class FlightModelMainAIM9B
{

    private FlightModelMainAIM9B()
    {
    }

    public static Arm getFmArm(FlightModelMain theFM)
    {
        return theFM.Arms;
    }

    public static float getFmGCenter(FlightModelMain theFM)
    {
        return theFM.Arms.GCENTER;
    }
}
