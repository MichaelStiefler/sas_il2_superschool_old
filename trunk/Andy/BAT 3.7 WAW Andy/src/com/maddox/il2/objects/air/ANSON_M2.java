// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11.05.2019 14:41:34
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   ANSON_M2.java

package com.maddox.il2.objects.air;

import com.maddox.rts.CLASS;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            ANSON, PaintSchemeBMPar03, Aircraft, NetAircraft

public class ANSON_M2 extends com.maddox.il2.objects.air.ANSON
{

    public ANSON_M2()
    {
    }

    private static com.maddox.il2.objects.air.Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        com.maddox.il2.objects.air.Aircraft._WeaponSlot a_lweaponslot[] = new com.maddox.il2.objects.air.Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning303si_GB", 350);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(10, "MGunVikkersKt", 500);
        }
        catch(java.lang.Exception exception) { }
        return a_lweaponslot;
    }

    static 
    {
        java.lang.Class class1 = com.maddox.rts.CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Anson");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Anson/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        com.maddox.rts.Property.set(class1, "yearService", 1939F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1945F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/AVRO.fmd:Anson_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.Cockpit_Anson.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 1.0728F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 10, 3, 3, 3, 3, 9
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01"
        });
    }
}