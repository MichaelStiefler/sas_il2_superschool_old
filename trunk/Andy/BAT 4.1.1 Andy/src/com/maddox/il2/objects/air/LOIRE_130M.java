// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 26.10.2020 07:00:16
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   LOIRE_130M.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Turret;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            LOIRE_xyz, PaintSchemeBMPar00, Aircraft, AircraftLH, 
//            Cockpit, NetAircraft

public class LOIRE_130M extends com.maddox.il2.objects.air.LOIRE_xyz
{

    public LOIRE_130M()
    {
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        default:
            break;

        case 0: // '\0'
            if(f < -30F)
            {
                f = -30F;
                flag = false;
            }
            if(f > 30F)
            {
                f = 30F;
                flag = false;
            }
            if(f1 < -30F)
            {
                f1 = -30F;
                flag = false;
            }
            if(f1 > 30F)
            {
                f1 = 30F;
                flag = false;
            }
            break;

        case 1: // '\001'
            if(f < -90F)
            {
                f = -90F;
                flag = false;
            }
            if(f > 90F)
            {
                f = 90F;
                flag = false;
            }
            if(f1 < 0.0F)
            {
                f1 = 0.0F;
                flag = false;
            }
            if(f1 > 30F)
            {
                f1 = 30F;
                flag = false;
            }
            if(f > -3.5F && f < 3.5F && f1 < 14F && f1 > 2.0F)
                flag = false;
            if(f > 31.5F && f < 41.5F)
                flag = false;
            if(f < -41.5F && f > -31.5F)
                flag = false;
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 1: // '\001'
            super.FM.turret[0].setHealth(f);
            break;

        case 2: // '\002'
            super.FM.turret[1].setHealth(f);
            break;
        }
    }

    public void doKillPilot(int i)
    {
        switch(i)
        {
        case 1: // '\001'
            super.FM.turret[0].bIsOperable = false;
            break;

        case 2: // '\002'
            super.FM.turret[1].bIsOperable = false;
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        if(i > 3)
        {
            return;
        } else
        {
            hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
            hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
            hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
            hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
            return;
        }
    }

    public void update(float f)
    {
        super.update(f);
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 2; j++)
                if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.clpGearEff[i][j] != null)
                {
                    tmpp.set(((com.maddox.il2.engine.Actor) (((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.clpGearEff[i][j])).pos.getAbsPoint());
                    tmpp.z = 0.01D;
                    ((com.maddox.il2.engine.Actor) (((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.clpGearEff[i][j])).pos.setAbs(tmpp);
                    ((com.maddox.il2.engine.Actor) (((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.clpGearEff[i][j])).pos.reset();
                }

        }

        if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.getCockpitDoor() < 0.98F)
        {
            hierMesh().chunkVisible("Turret1A_D0", false);
            hierMesh().chunkVisible("Turret1B_D0", false);
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot4_D0", false);
            hierMesh().chunkVisible("Pilot3_D0", true);
            hierMesh().chunkVisible("Pilot5_D0", true);
            hierMesh().chunkVisible("Head5_D0", true);
        } else
        {
            hierMesh().chunkVisible("Turret1A_D0", true);
            hierMesh().chunkVisible("Turret1B_D0", true);
            hierMesh().chunkVisible("Pilot2_D0", true);
            hierMesh().chunkVisible("Pilot4_D0", true);
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Pilot5_D0", false);
            hierMesh().chunkVisible("Head5_D0", false);
            return;
        }
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("FlapL_D0", 0.0F, 25F * f, 0.0F);
        hierMesh().chunkSetAngles("FlapR_D0", 0.0F, 25F * f, 0.0F);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("HatchL_D0", 0.0F, 85F * f, 0.0F);
        hierMesh().chunkSetAngles("HatchR_D0", 0.0F, -85F * f, 0.0F);
        hierMesh().chunkSetAngles("Panel_D0", 0.0F, 0.0F, -70F * f);
        if(com.maddox.il2.engine.Config.isUSE_RENDER())
        {
            if(com.maddox.il2.game.Main3D.cur3D().cockpits != null && com.maddox.il2.game.Main3D.cur3D().cockpits[0] != null)
                com.maddox.il2.game.Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void rareAction(float f, boolean flag)
    {
        com.maddox.il2.ai.War war = com.maddox.il2.ai.War.cur();
        com.maddox.il2.ai.War _tmp = war;
        com.maddox.il2.engine.Actor actor = com.maddox.il2.ai.War.GetNearestEnemy(this, 16, 6000F);
        com.maddox.il2.ai.War _tmp1 = war;
        com.maddox.il2.objects.air.Aircraft aircraft = com.maddox.il2.ai.War.getNearestEnemy(this, 5000F);
        if((actor != null && !(actor instanceof com.maddox.il2.objects.bridges.BridgeSegment) || aircraft != null) && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.getCockpitDoor() < 0.01F)
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setCockpitDoor(this, 1);
    }

    private static com.maddox.JGP.Point3d tmpp = new Point3d();

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.LOIRE_130M.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Loire130.fmd:Loire_FM");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Loire130/hier130M.him");
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Loire130");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        com.maddox.rts.Property.set(class1, "yearService", 1931.5F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1946.5F);
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitLoire130.class
        });
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 10, 10, 9, 3, 9, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN10", "_MGUN11", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalBomb02"
        });
    }
}