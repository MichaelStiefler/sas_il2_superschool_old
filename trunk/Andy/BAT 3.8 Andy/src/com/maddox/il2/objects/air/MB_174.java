// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11.11.2019 06:24:05
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   MB_174.java

package com.maddox.il2.objects.air;

import com.maddox.rts.CLASS;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            MB_174xyz, PaintSchemeBMPar02, Aircraft, NetAircraft

public class MB_174 extends com.maddox.il2.objects.air.MB_174xyz
{

    public MB_174()
    {
    }

    private static com.maddox.il2.objects.air.Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        com.maddox.il2.objects.air.Aircraft._WeaponSlot a_lweaponslot[] = new com.maddox.il2.objects.air.Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunMAC1934", 1000);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunMAC1934", 1000);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(10, "MGunMAC1934t", 350);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(10, "MGunMAC1934t", 350);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(11, "MGunMAC1934", 500);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(11, "MGunMAC1934", 500);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(11, "MGunMAC1934", 500);
        }
        catch(java.lang.Exception exception) { }
        return a_lweaponslot;
    }

    static 
    {
        java.lang.Class class1 = com.maddox.rts.CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "MB174");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/MB-174/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        com.maddox.rts.Property.set(class1, "yearService", 1939F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1952F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/MB_174.fmd:MB174_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitMB_174.class
        });
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 10, 10, 11, 11, 11, 3, 3, 3, 
            3, 3, 3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", 
            "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08"
        });
    }
}