// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 31.10.2020 10:01:47
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   V65.java

package com.maddox.il2.objects.air;

import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            V65x, PaintSchemeFMPar01, Aircraft, NetAircraft

public class V65 extends com.maddox.il2.objects.air.V65x
{

    public V65()
    {
    }


    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.V65.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "V65");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/V65/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        com.maddox.rts.Property.set(class1, "yearService", 1925F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1937F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/V65.fmd:V65_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitV65.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.74185F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 10, 3, 3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04"
        });
    }
}