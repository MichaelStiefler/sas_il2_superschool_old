// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 05.10.2019 10:02:07
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   DO_26C.java

package com.maddox.il2.objects.air;

import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            DO_26xyz, PaintSchemeBMPar00, Aircraft, NetAircraft

public class DO_26C extends com.maddox.il2.objects.air.DO_26xyz
{

    public DO_26C()
    {
    }

    private static com.maddox.il2.objects.air.Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        com.maddox.il2.objects.air.Aircraft._WeaponSlot a_lweaponslot[] = new com.maddox.il2.objects.air.Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunMG15120t", 250);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunMG15t", 1125);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(12, "MGunMG15t", 1125);
        }
        catch(java.lang.Exception exception) { }
        return a_lweaponslot;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(super.thisWeaponsName.startsWith("500kg_cargo"))
        {
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).M.massEmpty += 500F;
            return;
        } else
        {
            return;
        }
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.DO_26C.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Do-26C");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/DO-26/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        com.maddox.rts.Property.set(class1, "yearService", 1938F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1945F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/DO26.fmd:DO26_FM");
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.5265F);
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitDo_26.class
        });
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 11, 12, 3, 3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_CANNON01", "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04"
        });
    }
}