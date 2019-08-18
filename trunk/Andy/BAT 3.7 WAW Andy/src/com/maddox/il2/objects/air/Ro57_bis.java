// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 21.05.2019 06:19:02
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   Ro57_bis.java

package com.maddox.il2.objects.air;

import com.maddox.rts.CLASS;
import com.maddox.rts.Finger;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Ro57x_bis, PaintSchemeFMPar04, Aircraft, NetAircraft

public class Ro57_bis extends com.maddox.il2.objects.air.Ro57x_bis
{

    public Ro57_bis()
    {
    }

    public void typeDiveBomberAdjAltitudeMinus()
    {
    }

    public void typeDiveBomberAdjAltitudePlus()
    {
    }

    public void typeDiveBomberAdjAltitudeReset()
    {
    }

    public void typeDiveBomberAdjDiveAngleMinus()
    {
    }

    public void typeDiveBomberAdjDiveAnglePlus()
    {
    }

    public void typeDiveBomberAdjDiveAngleReset()
    {
    }

    public void typeDiveBomberAdjVelocityMinus()
    {
    }

    public void typeDiveBomberAdjVelocityPlus()
    {
    }

    public void typeDiveBomberAdjVelocityReset()
    {
    }

    public void typeDiveBomberReplicateFromNet(com.maddox.rts.NetMsgInput netmsginput1)
        throws java.io.IOException
    {
    }

    public void typeDiveBomberReplicateToNet(com.maddox.rts.NetMsgGuaranted netmsgguaranted1)
        throws java.io.IOException
    {
    }

    public boolean typeDiveBomberToggleAutomation()
    {
        return false;
    }

    private static com.maddox.il2.objects.air.Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        com.maddox.il2.objects.air.Aircraft._WeaponSlot a_lweaponslot[] = new com.maddox.il2.objects.air.Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMG15120ki", 60);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMG15120ki", 60);
        }
        catch(java.lang.Exception exception) { }
        return a_lweaponslot;
    }

    static 
    {
        java.lang.Class class1 = com.maddox.rts.CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Ro.57");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Ro57_bis/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        com.maddox.rts.Property.set(class1, "yearService", 1943F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1945F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Ro57.fmd:RO57_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitRo57_bis.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.6731F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 3, 3, 3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05"
        });
    }
}