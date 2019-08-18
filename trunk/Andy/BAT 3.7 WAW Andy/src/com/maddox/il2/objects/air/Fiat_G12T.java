// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11.05.2019 14:46:42
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   Fiat_G12T.java

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.CLASS;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Fiat_G12, PaintSchemeBMPar03, Aircraft, NetAircraft

public class Fiat_G12T extends com.maddox.il2.objects.air.Fiat_G12
{

    public Fiat_G12T()
    {
    }

    private static com.maddox.il2.objects.air.Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        com.maddox.il2.objects.air.Aircraft._WeaponSlot a_lweaponslot[] = new com.maddox.il2.objects.air.Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = null;
        }
        catch(java.lang.Exception exception) { }
        return a_lweaponslot;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(super.thisWeaponsName.startsWith("24x") || super.thisWeaponsName.startsWith("4x") || super.thisWeaponsName.startsWith("10x") || super.thisWeaponsName.startsWith("15x"))
        {
            hierMesh().chunkVisible("Blister1_D0", false);
            return;
        } else
        {
            return;
        }
    }

    static 
    {
        java.lang.Class class1 = com.maddox.rts.CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Fiat-G12");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Fiat-G12/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        com.maddox.rts.Property.set(class1, "yearService", 1939F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1943F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/FG12.fmd:FiatG12_FM");
        com.maddox.rts.Property.set(class1, "LOSElevation", 1.0728F);
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitFiatG12.class
        });
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_BombSpawn01"
        });
    }
}