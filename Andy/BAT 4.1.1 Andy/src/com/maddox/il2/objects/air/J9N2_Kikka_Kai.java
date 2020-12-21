// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 04.11.2020 17:36:31
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   J9N2_Kikka_Kai.java

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.air:
//            KikkaKai123, PaintSchemeBMPar03, TypeFighter, TypeBNZFighter, 
//            NetAircraft, Aircraft

public class J9N2_Kikka_Kai extends com.maddox.il2.objects.air.KikkaKai123
    implements com.maddox.il2.objects.air.TypeFighter, com.maddox.il2.objects.air.TypeBNZFighter
{

    public J9N2_Kikka_Kai()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(super.thisWeaponsName.endsWith("2xdpt"))
        {
            hierMesh().chunkVisible("Pylon_D0", true);
            return;
        }
        if(super.thisWeaponsName.startsWith("2x30mm"))
        {
            hierMesh().chunkVisible("barrelL_D0", false);
            hierMesh().chunkVisible("barrelR_D0", false);
            hierMesh().chunkVisible("30mmL_D0", true);
            hierMesh().chunkVisible("30mmR_D0", true);
            return;
        }
        if(super.thisWeaponsName.startsWith("1x50mm"))
        {
            hierMesh().chunkVisible("PatchL_D0", true);
            hierMesh().chunkVisible("PatchR_D0", true);
            hierMesh().chunkVisible("barrelL_D0", false);
            hierMesh().chunkVisible("barrelR_D0", false);
            return;
        } else
        {
            return;
        }
    }


    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.J9N2_Kikka_Kai.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Kikka");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/J9N1_Kikka-Kai/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        com.maddox.rts.Property.set(class1, "yearService", 1943.1F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1945.5F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Kikka_Kai.fmd:Kikka_Kai_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitKikka.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.74185F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 1, 1, 3, 3, 3, 3, 3, 9, 
            9, 2, 2, 2, 2, 2, 2, 9, 9, 9, 
            9, 9, 9, 9, 0
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalDev01", 
            "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", 
            "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_CANNON05"
        });
    }
}