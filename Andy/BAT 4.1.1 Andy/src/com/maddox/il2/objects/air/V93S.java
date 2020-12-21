// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 31.10.2020 10:02:21
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   V93S.java

package com.maddox.il2.objects.air;

import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            V92Sx, PaintSchemeFMPar01, Aircraft, NetAircraft

public class V93S extends com.maddox.il2.objects.air.V92Sx
{

    public V93S()
    {
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.V93S.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "V93S");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/V93S/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        com.maddox.rts.Property.set(class1, "yearService", 1925F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1937F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/V93S.fmd:V93S_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitV93S.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.74185F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 10, 3, 3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04"
        });
    }
}