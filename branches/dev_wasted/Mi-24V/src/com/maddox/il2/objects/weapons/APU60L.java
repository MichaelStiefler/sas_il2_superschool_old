// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 14.05.2012 16:42:42
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MiG21Pylon.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            PylonRO_82_1

public class APU60L extends PylonRO_82_1
{

    public APU60L()
    {
    }

    static 
    {
        Property.set(CLASS.THIS(), "mesh", "3DO/Arms/APU-60-II_L/mono.sim");
    }
}