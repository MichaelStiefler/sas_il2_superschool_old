// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 16.05.2019 16:40:24
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   Dornie_J_Wal.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Turret;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import java.io.IOException;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, TypeTransport, TypeBomber, TypeSeaPlane, 
//            PaintSchemeBMPar02, Aircraft, AircraftLH, NetAircraft

public class Dornie_J_Wal extends com.maddox.il2.objects.air.Scheme1
    implements com.maddox.il2.objects.air.TypeTransport, com.maddox.il2.objects.air.TypeBomber, com.maddox.il2.objects.air.TypeSeaPlane
{

    public Dornie_J_Wal()
    {
        tmpp = new Point3d();
    }

    protected boolean cutFM(int i, int j, com.maddox.il2.engine.Actor actor)
    {
        switch(i)
        {
        case 33: // '!'
            return super.cutFM(34, j, actor);

        case 36: // '$'
            return super.cutFM(37, j, actor);

        case 11: // '\013'
            cutFM(17, j, actor);
            super.FM.cut(17, j, actor);
            cutFM(18, j, actor);
            super.FM.cut(18, j, actor);
            return super.cutFM(i, j, actor);

        case 19: // '\023'
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.bIsSail = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 45F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 45F * f, 0.0F);
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

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            hierMesh().chunkVisible("Gore0_D0", true);
            break;

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;
        }
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh1, float f1)
    {
    }

    protected void moveGear(float f)
    {
        com.maddox.il2.objects.air.Dornie_J_Wal.moveGear(hierMesh(), f);
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
            float f2 = java.lang.Math.abs(f);
            if(f2 < 114F)
            {
                if(f1 < -33F)
                {
                    f1 = -33F;
                    flag = false;
                }
            } else
            if(f2 < 153F)
            {
                if(f1 < -24F)
                    f1 = -24F;
                if(f1 < -61.962F + 0.333F * f)
                    flag = false;
                if(f1 < -19.111F + 0.185185F * f && f1 > 40F - 0.333F * f)
                    flag = false;
            } else
            if(f2 < 168F)
            {
                if(f1 < -97.666F + 0.481482F * f)
                    f1 = -97.666F + 0.481482F * f;
                if(f1 < -19.111F + 0.185185F * f)
                    flag = false;
            } else
            {
                if(f1 < -97.666F + 0.481482F * f)
                    f1 = -97.666F + 0.481482F * f;
                flag = false;
            }
            if(f1 > 60F)
            {
                f1 = 60F;
                flag = false;
            }
            break;

        case 1: // '\001'
            float f3 = java.lang.Math.abs(f);
            if(f3 < 2.0F)
                flag = false;
            if(f3 < 6.5F)
            {
                if(f1 < -4F)
                    f1 = -4F;
                if(f1 < 17.666F - 3.333F * f)
                    flag = false;
            } else
            if(f3 < 45F)
            {
                if(f1 < 1.232F - 0.805F * f)
                {
                    f1 = 1.232F - 0.805F * f;
                    flag = false;
                }
            } else
            if(f3 < 105F)
            {
                if(f1 < -35F)
                {
                    f1 = -35F;
                    flag = false;
                }
            } else
            if(f1 < 2.2F)
            {
                f1 = 2.2F;
                flag = false;
            }
            if(f > 161F)
            {
                f = 161F;
                flag = false;
            }
            if(f < -161F)
            {
                f = -161F;
                flag = false;
            }
            if(f1 > 56F)
                flag = false;
            if(f1 > 48F)
                f1 = 48F;
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected void hitBone(java.lang.String s, com.maddox.il2.ai.Shot shot, com.maddox.JGP.Point3d point3d)
    {
        if(s.startsWith("cf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("tail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("keel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("rudder"))
            hitChunk("Rudder1", shot);
        else
        if(s.startsWith("stabl"))
            hitChunk("StabL", shot);
        else
        if(s.startsWith("stabr"))
            hitChunk("StabR", shot);
        else
        if(s.startsWith("vatorl"))
            hitChunk("VatorL", shot);
        else
        if(s.startsWith("vatorr"))
            hitChunk("VatorR", shot);
        else
        if(s.startsWith("winglmid"))
        {
            if(chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            if(shot.powerType == 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.1F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 0, 1);
            if(shot.powerType == 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.1F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 1, 1);
        } else
        if(s.startsWith("aronel"))
            hitChunk("AroneL", shot);
        else
        if(s.startsWith("aroner"))
            hitChunk("AroneR", shot);
        else
        if(s.startsWith("engine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
            if(getEnergyPastArmor(1.45F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getCylindersRatio() * 0.5F)
            {
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, com.maddox.il2.ai.World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateEngineStates[0] < 1)
                {
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.doSetEngineState(shot.initiator, 0, 1);
                }
                if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 960000F)
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 3);
                getEnergyPastArmor(25F, shot);
            }
        } else
        if(s.startsWith("gearl"))
            hitChunk("GearL2", shot);
        else
        if(s.startsWith("gearr"))
            hitChunk("GearR2", shot);
        else
        if(s.startsWith("xturret"))
        {
            if(s.startsWith("turret1"))
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setJamBullets(10, 0);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int i;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                i = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                i = s.charAt(6) - 49;
            } else
            {
                i = s.charAt(5) - 49;
            }
            hitFlesh(i, shot, byte0);
        }
    }

    public void msgShot(com.maddox.il2.ai.Shot shot)
    {
        setShot(shot);
        if(shot.chunkName.startsWith("WingLMid") && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 0, 1);
        if(shot.chunkName.startsWith("Engine1") && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
        if(shot.chunkName.startsWith("Pilot1"))
            killPilot(shot.initiator, 0);
        if(shot.chunkName.startsWith("Pilot2"))
            killPilot(shot.initiator, 1);
        super.msgShot(shot);
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

    public void typeBomberUpdate(float f1)
    {
    }

    public void typeBomberReplicateToNet(com.maddox.rts.NetMsgGuaranted netmsgguaranted1)
        throws java.io.IOException
    {
    }

    public void typeBomberReplicateFromNet(com.maddox.rts.NetMsgInput netmsginput1)
        throws java.io.IOException
    {
    }

    private com.maddox.JGP.Point3d tmpp;

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.Dornie_J_Wal.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Do-J-Wal");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Do-J-Wal/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        com.maddox.rts.Property.set(class1, "yearService", 1937F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1945.5F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Do-J-Wal.fmd:DoJWal_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitDornie_J_Wal.class, com.maddox.il2.objects.air.CockpitDornie_J_Wal_NGunner.class
        });
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 3, 3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb07", "_ExternalBomb08"
        });
    }
}