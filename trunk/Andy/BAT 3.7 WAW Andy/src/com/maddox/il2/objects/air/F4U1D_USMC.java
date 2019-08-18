// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11.07.2019 17:02:43
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   F4U1D_USMC.java

package com.maddox.il2.objects.air;

import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            F4U, PaintSchemeFMPar05, PaintSchemeFMPar06, NetAircraft, 
//            Aircraft

public class F4U1D_USMC extends com.maddox.il2.objects.air.F4U
{

    public F4U1D_USMC()
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
        java.lang.Class class1 = com.maddox.il2.objects.air.F4U1D_USMC.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "F4U");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/F4U-1D(Multi1)/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        com.maddox.rts.Property.set(class1, "meshName_us", "3DO/Plane/F4U-1D(USA)/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar05());
        com.maddox.rts.Property.set(class1, "meshName_rz", "3DO/Plane/F4U-1D(RZ)/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme_rz", new PaintSchemeFMPar06());
        com.maddox.rts.Property.set(class1, "yearService", 1944F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1948.5F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/F4U1D_USMC.fmd:F4U1D_USMC_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitF4U1D.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 1.0585F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 
            3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", 
            "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb02", "_ExternalBomb03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", 
            "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08"
        });
    }
}