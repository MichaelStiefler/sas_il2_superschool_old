// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 19.05.2019 09:11:50
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   UC_43.java

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            UC_43xyz, PaintSchemeFMPar02, Aircraft, NetAircraft

public class UC_43 extends com.maddox.il2.objects.air.UC_43xyz
{

    public UC_43()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(super.thisWeaponsName.equals("General"))
            hierMesh().chunkVisible("General", true);
        else
        if(super.thisWeaponsName.equals("General+Secretary"))
        {
            hierMesh().chunkVisible("General2", true);
            hierMesh().chunkVisible("Femme", true);
            hierMesh().chunkVisible("Head2", true);
        } else
        if(super.thisWeaponsName.equals("2xPax"))
        {
            hierMesh().chunkVisible("Pass1", true);
            hierMesh().chunkVisible("Pass2", true);
        } else
        {
            return;
        }
    }

    private static com.maddox.il2.objects.air.Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        com.maddox.il2.objects.air.Aircraft._WeaponSlot a_lweaponslot[] = new com.maddox.il2.objects.air.Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
        }
        catch(java.lang.Exception exception) { }
        return a_lweaponslot;
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.UC_43.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Staggerwing");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Staggerwing-43/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        com.maddox.rts.Property.set(class1, "yearService", 1935F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1945F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/D17.fmd:UC43_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.Cockpit_Staggerwing.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 1.0728F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[2]);
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_WingTipL", "_WingTipR"
        });
    }
}