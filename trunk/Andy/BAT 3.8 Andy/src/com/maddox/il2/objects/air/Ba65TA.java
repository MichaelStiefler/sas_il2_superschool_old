// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 18.01.2020 09:48:19
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   Ba65TA.java

package com.maddox.il2.objects.air;

import com.maddox.rts.CLASS;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Ba65B, PaintSchemeBMPar02, Aircraft, NetAircraft

public class Ba65TA extends com.maddox.il2.objects.air.Ba65B
{

    public Ba65TA()
    {
    }

    private static com.maddox.il2.objects.air.Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        com.maddox.il2.objects.air.Aircraft._WeaponSlot a_lweaponslot[] = new com.maddox.il2.objects.air.Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBredaSAFAT127s", 350);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBredaSAFAT127s", 350);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBredaSAFAT77s", 500);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBredaSAFAT77s", 500);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(10, "MGunBredaSAFAT77t", 500);
        }
        catch(java.lang.Exception exception) { }
        return a_lweaponslot;
    }

    static 
    {
        java.lang.Class class1 = com.maddox.rts.CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Ba.65");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Ba65Fiat/hierTA.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        com.maddox.rts.Property.set(class1, "yearService", 1938F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1943F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Ba65AA.fmd:BA65_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitBa65A.class
        });
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 1, 1, 10, 3, 3, 3, 3, 3, 
            3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_ExternalBomb01", 
            "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04"
        });
    }
}