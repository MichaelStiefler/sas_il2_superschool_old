// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 14.07.2020 17:10:02
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   KI_98_1.java

package com.maddox.il2.objects.air;

import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            KI_98, PaintSchemeFMPar01, PaintSchemeFMPar05, NetAircraft, 
//            Aircraft

public class KI_98_1 extends com.maddox.il2.objects.air.KI_98
{

    public KI_98_1()
    {
    }


    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.KI_98_1.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "ki-98");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/ki-98/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        com.maddox.rts.Property.set(class1, "meshName_ja", "3DO/Plane/ki-98/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme_ja", new PaintSchemeFMPar05());
        com.maddox.rts.Property.set(class1, "yearService", 1945.5F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1945.5F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Ki_98.fmd");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitKi98_2.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 1.0151F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 1, 3, 3, 3, 3, 9, 9, 9, 
            9
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_Cannon01", "_Cannon02", "_Cannon03", "_Externalbomb01", "_Externalbomb02", "_Externalbomb03", "_Externalbomb04", "_Externaldev01", "_Externaldev02", "_ExternalBomb05", 
            "_ExternalBomb06"
        });
    }
}