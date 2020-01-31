// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.12.2019 14:39:39
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   V_156_F.java

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            SB2U_FN, PaintSchemeBMPar00, Aircraft, NetAircraft

public class V_156_F extends com.maddox.il2.objects.air.SB2U_FN
{

    public V_156_F()
    {
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("Brake1_D0", 0.0F, 0.0F, 85F * f);
        hierMesh().chunkSetAngles("Brake2_D0", 0.0F, 0.0F, 85F * f);
        hierMesh().chunkSetAngles("Brake3_D0", 0.0F, 0.0F, 88F * f);
        hierMesh().chunkSetAngles("Brake4_D0", 0.0F, 0.0F, 88F * f);
    }

    private static com.maddox.il2.objects.air.Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        com.maddox.il2.objects.air.Aircraft._WeaponSlot a_lweaponslot[] = new com.maddox.il2.objects.air.Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunDarne1933", 350);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(10, "MGunDarne1933t", 500);
        }
        catch(java.lang.Exception exception) { }
        return a_lweaponslot;
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.V_156_F.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "V-156F");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/V156F/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        com.maddox.rts.Property.set(class1, "yearService", 1940F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1946.5F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/SB2U.fmd:SB2U_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitSB2U.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.84305F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 10, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02"
        });
    }
}