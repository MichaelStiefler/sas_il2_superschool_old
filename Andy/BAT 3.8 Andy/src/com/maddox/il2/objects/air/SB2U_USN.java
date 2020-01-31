// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.12.2019 14:40:18
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   SB2U_USN.java

package com.maddox.il2.objects.air;

import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            SB2U, PaintSchemeBMPar00, Aircraft, NetAircraft

public class SB2U_USN extends com.maddox.il2.objects.air.SB2U
{

    public SB2U_USN()
    {
    }

    private static com.maddox.il2.objects.air.Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        com.maddox.il2.objects.air.Aircraft._WeaponSlot a_lweaponslot[] = new com.maddox.il2.objects.air.Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(10, "MGunBrowning303t", 500);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
        }
        catch(java.lang.Exception exception) { }
        return a_lweaponslot;
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.SB2U_USN.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "SB2U");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/SB2U/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        com.maddox.rts.Property.set(class1, "yearService", 1940F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1945F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/SB2U.fmd:SB2U_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitSB2U.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.84305F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 10, 3, 3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04"
        });
    }
}