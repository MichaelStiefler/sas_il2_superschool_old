// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 26.12.2012 18:39:53
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   JetEraStatic.java

package com.maddox.il2.objects.vehicles.planes;


// Referenced classes of package com.maddox.il2.objects.vehicles.planes:
//            PlaneGeneric

public abstract class Su25Static
{
    public static class Su_25 extends PlaneGeneric
    {

        public Su_25()
        {
        }
    }

    public Su25Static()
    {
    }

    static 
    {
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.Su25Static.Su_25.class);
    }
}