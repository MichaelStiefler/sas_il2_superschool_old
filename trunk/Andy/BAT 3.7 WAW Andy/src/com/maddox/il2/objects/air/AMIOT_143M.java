// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11.05.2019 14:41:07
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   AMIOT_143M.java

package com.maddox.il2.objects.air;

import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            AMIOT_143, PaintSchemeBMPar03, Aircraft, NetAircraft

public class AMIOT_143M extends com.maddox.il2.objects.air.AMIOT_143
{

    public AMIOT_143M()
    {
    }

    private static com.maddox.il2.objects.air.Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        com.maddox.il2.objects.air.Aircraft._WeaponSlot a_lweaponslot[] = new com.maddox.il2.objects.air.Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunMAC1934t", 800);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunMAC1934t", 1200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(12, "MGunMAC1934t", 1200);
        }
        catch(java.lang.Exception exception) { }
        return a_lweaponslot;
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.AMIOT_143M.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Amiot");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/AMIOT-143/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        com.maddox.rts.Property.set(class1, "yearService", 1935F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1940F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/A143.fmd:AMIOT143_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitAMIOT_143.class, com.maddox.il2.objects.air.CockpitAMIOT_143M_Bombardier.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.76315F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 11, 12, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn05", "_ExternalBomb06", 
            "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_BombSpawn06"
        });
    }
}