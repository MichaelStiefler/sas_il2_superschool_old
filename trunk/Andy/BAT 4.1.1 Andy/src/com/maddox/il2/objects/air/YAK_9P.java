// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 26.10.2020 13:52:44
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   YAK_9P.java

package com.maddox.il2.objects.air;

import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            YAK_9UT, TypeBNZFighter, PaintSchemeFMPar04, Aircraft, 
//            NetAircraft

public class YAK_9P extends com.maddox.il2.objects.air.YAK_9UT
    implements com.maddox.il2.objects.air.TypeBNZFighter
{

    public YAK_9P()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(super.thisWeaponsName.endsWith("20"))
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).M.referenceWeight += 50F;
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.YAK_9P.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Yak");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Yak-9P(Multi1)/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        com.maddox.rts.Property.set(class1, "yearService", 1947.5F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1956.8F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Yak-9P.fmd:YAK9P");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitYAK_9U.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.6432F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 1
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_CANNON01"
        });

    }
}