// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 15.01.2020 17:33:15
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   Target_Banner.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Finger;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme0, PaintSchemeFMPar05, TypeGlider, TypeTransport, 
//            TypeBomber, NetAircraft, Aircraft

public class Target_Banner extends com.maddox.il2.objects.air.Scheme0
    implements com.maddox.il2.objects.air.TypeGlider, com.maddox.il2.objects.air.TypeTransport, com.maddox.il2.objects.air.TypeBomber
{

    public Target_Banner()
    {
        DmgLevel = 0;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.AS.bIsEnableToBailout = false;
        FM.Gears.setHydroOperable(false);
        FM.Gears.setOperable(false);
        FM.CT.bHasBrakeControl = false;
        FM.setCapableOfTaxiing(false);
        FM.setCapableOfACM(false);
        ((com.maddox.il2.ai.air.Pilot)FM).silence = true;
        if(super.thisWeaponsName.startsWith("01"))
            com.maddox.rts.Property.set(com.maddox.il2.objects.air.Target_Banner.class, "gliderStringLength", 100F);
        if(super.thisWeaponsName.startsWith("de") || super.thisWeaponsName.startsWith("02"))
            com.maddox.rts.Property.set(com.maddox.il2.objects.air.Target_Banner.class, "gliderStringLength", 500F);
        if(super.thisWeaponsName.startsWith("03"))
            com.maddox.rts.Property.set(com.maddox.il2.objects.air.Target_Banner.class, "gliderStringLength", 1000F);
        if(super.thisWeaponsName.startsWith("04"))
            com.maddox.rts.Property.set(com.maddox.il2.objects.air.Target_Banner.class, "gliderStringLength", 1500F);
    }

    protected void hitBone(java.lang.String s, com.maddox.il2.ai.Shot shot, com.maddox.JGP.Point3d point3d)
    {
        if(s.startsWith("xkeel"))
        {
            com.maddox.il2.game.HUD.logCenter("Aerial target hit");
            if(DmgLevel == 0)
                DmgLevel = 1;
            if(DmgLevel == 1)
            {
                hierMesh().chunkVisible("Banner_D1", true);
                hierMesh().chunkVisible("Banner_D0", false);
                DmgLevel = 2;
            }
        }
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

    public void missionStarting()
    {
        onAircraftLoaded();
    }

    private int DmgLevel;

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.Target_Banner.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Target Banner");
        com.maddox.rts.Property.set(class1, "meshName", "3do/Plane/Target-Banner(Multi1)/Hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        com.maddox.rts.Property.set(class1, "yearService", 1936F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1969F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/TTB.fmd:TTB_FM");
        com.maddox.rts.Property.set(class1, "gliderString", "3do/Arms/TTB-Cable/mono.sim");
        com.maddox.rts.Property.set(class1, "gliderStringKx", 30F);
        com.maddox.rts.Property.set(class1, "gliderStringFactor", 1.8F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_Clip00"
        });
    }
}