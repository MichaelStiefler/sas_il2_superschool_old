// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 12.05.2019 10:03:43
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   BF_109L2_LATE.java

package com.maddox.il2.objects.air;

import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.AircraftTools;

// Referenced classes of package com.maddox.il2.objects.air:
//            BF_109LX, PaintSchemeFMPar06, TypeBNZFighter, NetAircraft, 
//            Aircraft

public class BF_109L2_LATE extends com.maddox.il2.objects.air.BF_109LX
    implements com.maddox.il2.objects.air.TypeBNZFighter
{

    public BF_109L2_LATE()
    {
    }

    static java.lang.Class _mthclass$(java.lang.String s)
    {
        try
        {
            return java.lang.Class.forName(s);
        }
        catch(java.lang.ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.BF_109L2_LATE.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Bf109");
        com.maddox.rts.Property.set(class1, "meshName", "3do/plane/Bf-109K-4/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        com.maddox.rts.Property.set(class1, "yearService", 1945.6F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1955F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/BF109L2_Late.fmd:BF109L_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitBF_109K4.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.7498F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 1, 1, 1, 1, 1, 9, 9, 
            9, 9, 3, 3, 3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_ExternalDev01", "_ExternalDev01", 
            "_ExternalDev02", "_ExternalDev03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05"
        });
    }
}