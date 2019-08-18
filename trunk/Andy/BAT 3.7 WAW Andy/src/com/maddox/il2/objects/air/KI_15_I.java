// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 20.05.2019 16:49:39
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   KI_15_I.java

package com.maddox.il2.objects.air;

import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            KI_15xyz, PaintSchemeBMPar02, Aircraft, NetAircraft

public class KI_15_I extends com.maddox.il2.objects.air.KI_15xyz
{

    public KI_15_I()
    {
    }

    private static com.maddox.il2.objects.air.Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        com.maddox.il2.objects.air.Aircraft._WeaponSlot a_lweaponslot[] = new com.maddox.il2.objects.air.Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunVikkersKt", 500);
        }
        catch(java.lang.Exception exception) { }
        return a_lweaponslot;
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.KI_15_I.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Ki-15");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Ki-15/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        com.maddox.rts.Property.set(class1, "yearService", 1937F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1945.5F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Ki15I.fmd:Ki15_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitKI_15.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.76315F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 9, 9, 9, 9, 9, 3, 3, 3, 3, 
            3, 3, 3, 3, 9, 9
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", 
            "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalDev06", "_ExternalDev07"
        });
    }
}