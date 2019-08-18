// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 14.05.2019 18:52:40
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   Albatros_CX.java

package com.maddox.il2.objects.air;

import com.maddox.rts.Finger;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            AlbatrosX, PaintSchemeBMPar00, Aircraft, NetAircraft

public class Albatros_CX extends com.maddox.il2.objects.air.AlbatrosX
{

    public Albatros_CX()
    {
    }

    public boolean typeBomberToggleAutomation()
    {
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
    }

    public void typeBomberAdjDistanceMinus()
    {
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
    }

    public void typeBomberAdjSideslipMinus()
    {
    }

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
    }

    public void typeBomberAdjAltitudeMinus()
    {
    }

    public void typeBomberAdjSpeedReset()
    {
    }

    public void typeBomberAdjSpeedPlus()
    {
    }

    public void typeBomberAdjSpeedMinus()
    {
    }

    public void typeBomberUpdate(float f)
    {
    }

    public void typeBomberReplicateToNet(com.maddox.rts.NetMsgGuaranted netmsgguaranted)
        throws java.io.IOException
    {
    }

    public void typeBomberReplicateFromNet(com.maddox.rts.NetMsgInput netmsginput)
        throws java.io.IOException
    {
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.Albatros_CX.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "CX");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Albatros_CX(Multi1)/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        com.maddox.rts.Property.set(class1, "yearService", 1914F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1918F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Albatros_CX.fmd:Albatros_CX_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitAlbatros_CX.class, com.maddox.il2.objects.air.CockpitAlbatros_CX_TGunner.class
        });
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 10, 3, 3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04"
        });
    }
}