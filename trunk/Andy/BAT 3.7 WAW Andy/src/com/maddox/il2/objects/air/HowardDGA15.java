// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11.05.2019 14:47:55
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   HowardDGA15.java

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Howard, PaintSchemeBMPar00, Aircraft, NetAircraft

public class HowardDGA15 extends com.maddox.il2.objects.air.Howard
{

    public HowardDGA15()
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
        if(super.thisWeaponsName.equals("General"))
        {
            hierMesh().chunkVisible("General", true);
            return;
        }
        if(super.thisWeaponsName.equals("Documents"))
        {
            hierMesh().chunkVisible("Documents", true);
            return;
        } else
        {
            return;
        }
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.HowardDGA15.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "DGA-15");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/DGA15/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        com.maddox.rts.Property.set(class1, "yearService", 1941F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1945F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/DGA.fmd:UC70_FM");
        com.maddox.rts.Property.set(class1, "LOSElevation", 1.0728F);
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitDGA15.class
        });
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[1]);
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_BombSpawn01"
        });
    }
}