// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 14.12.2019 13:37:35
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   MARTIN_167.java

package com.maddox.il2.objects.air;

import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            MARTIN_Maryland, PaintSchemeBMPar03, Aircraft, NetAircraft

public class MARTIN_167 extends com.maddox.il2.objects.air.MARTIN_Maryland
{

    public MARTIN_167()
    {
    }

    private static com.maddox.il2.objects.air.Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        com.maddox.il2.objects.air.Aircraft._WeaponSlot a_lweaponslot[] = new com.maddox.il2.objects.air.Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning303s_GB", 350);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning303s_GB", 350);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning303s_GB", 350);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning303s_GB", 350);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(10, "MGunBrowning303t_GB", 500);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(11, "MGunBrowning303t_GB", 500);
        }
        catch(java.lang.Exception exception) { }
        return a_lweaponslot;
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.MARTIN_167.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Maryland");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/MARTIN-167/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        com.maddox.rts.Property.set(class1, "yearService", 1939F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1945F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/M167.fmd:GLENN_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitMARTIN_Maryland.class, com.maddox.il2.objects.air.CockpitMARTIN_167_Bombardier.class
        });
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 10, 11, 3, 3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04"
        });
    }
}