// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 20.03.2020 10:45:10
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   Keystone_B3a.java

package com.maddox.il2.objects.air;

import com.maddox.rts.Finger;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgOutput;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Keystone_B3a_cde, TypeBomber, TypeTransport, PaintSchemeBMPar00, 
//            Aircraft, NetAircraft

public class Keystone_B3a extends com.maddox.il2.objects.air.Keystone_B3a_cde
    implements com.maddox.il2.objects.air.TypeBomber, com.maddox.il2.objects.air.TypeTransport
{

    public Keystone_B3a()
    {
        bChangedExts = false;
        bChangedPit = true;
        fSightSetForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 850F;
        fSightCurSpeed = 250F;
        fSightCurReadyness = 0.0F;
        llpos = 0.0F;
    }

    public boolean typeBomberToggleAutomation()
    {
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus()
    {
        fSightCurForwardAngle++;
        if(fSightCurForwardAngle > 75F)
            fSightCurForwardAngle = 75F;
    }

    public void typeBomberAdjDistanceMinus()
    {
        fSightCurForwardAngle--;
        if(fSightCurForwardAngle < 0.0F)
            fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjSideslipReset()
    {
        fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus()
    {
        fSightCurSideslip += 0.10000000000000001D;
        if(fSightCurSideslip > 3F)
            fSightCurSideslip = 3F;
    }

    public void typeBomberAdjSideslipMinus()
    {
        fSightCurSideslip -= 0.1F;
        if(fSightCurSideslip < -3F)
            fSightCurSideslip = -3F;
    }

    public void typeBomberAdjAltitudeReset()
    {
        fSightCurAltitude = 850F;
    }

    public void typeBomberAdjAltitudePlus()
    {
        fSightCurAltitude += 10F;
        if(fSightCurAltitude > 6000F)
            fSightCurAltitude = 6000F;
    }

    public void typeBomberAdjAltitudeMinus()
    {
        fSightCurAltitude -= 10F;
        if(fSightCurAltitude < 850F)
            fSightCurAltitude = 850F;
        fSightCurDistance = fSightCurAltitude * (float)java.lang.Math.tan(java.lang.Math.toRadians(fSightCurForwardAngle));
    }

    public void typeBomberAdjSpeedReset()
    {
        fSightCurSpeed = 250F;
    }

    public void typeBomberAdjSpeedPlus()
    {
        fSightCurSpeed += 10F;
        if(fSightCurSpeed > 300F)
            fSightCurSpeed = 300F;
    }

    public void typeBomberAdjSpeedMinus()
    {
        fSightCurSpeed -= 10F;
        if(fSightCurSpeed < 250F)
            fSightCurSpeed = 250F;
    }

    public void typeBomberUpdate(float f)
    {
        double d = ((double)fSightCurSpeed / 3.6000000000000001D) * java.lang.Math.sqrt((double)fSightCurAltitude * 0.20387359799999999D);
        d -= (double)(fSightCurAltitude * fSightCurAltitude) * 1.419E-005D;
        fSightSetForwardAngle = (float)java.lang.Math.atan(d / (double)fSightCurAltitude);
    }

    public void typeBomberReplicateToNet(com.maddox.rts.NetMsgGuaranted netmsgguaranted)
        throws java.io.IOException
    {
        netmsgguaranted.writeFloat(fSightCurAltitude);
        netmsgguaranted.writeFloat(fSightCurSpeed);
        netmsgguaranted.writeFloat(fSightCurForwardAngle);
        netmsgguaranted.writeFloat(fSightCurSideslip);
    }

    public void typeBomberReplicateFromNet(com.maddox.rts.NetMsgInput netmsginput)
        throws java.io.IOException
    {
        fSightCurAltitude = netmsginput.readFloat();
        fSightCurSpeed = netmsginput.readFloat();
        fSightCurForwardAngle = netmsginput.readFloat();
        fSightCurSideslip = netmsginput.readFloat();
    }


    public boolean bChangedExts;
    public static boolean bChangedPit = false;
    public float fSightSetForwardAngle;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    private float llpos;

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.Keystone_B3a.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Keystone_B3a");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Keystone_B3a(Multi1)/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        com.maddox.rts.Property.set(class1, "yearService", 1937F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1948F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/KeystoneB3a.fmd:KeystoneB3a_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitKeystone_B3aFront.class, com.maddox.il2.objects.air.CockpitKeystone_B3a_Bombardier.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.73425F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 11, 12, 3, 3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04"
        });
    }
}