// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 29.11.2019 11:13:38
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   DC_2.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Finger;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme2, TypeTransport, TypeBomber, PaintSchemeBMPar02, 
//            PaintScheme, Aircraft, NetAircraft

public class DC_2 extends com.maddox.il2.objects.air.Scheme2
    implements com.maddox.il2.objects.air.TypeTransport, com.maddox.il2.objects.air.TypeBomber
{

    public DC_2()
    {
    }

    public void setOnGround(com.maddox.JGP.Point3d point3d, com.maddox.il2.engine.Orient orient, com.maddox.JGP.Vector3d vector3d)
    {
        super.setOnGround(point3d, orient, vector3d);
        if(super.FM.isPlayers())
        {
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.bHasCockpitDoorControl = true;
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.dvCockpitDoor = 0.5F;
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.cockpitDoorControl = 1.0F;
        }
    }

    public void update(float f)
    {
        super.update(f);
        if(!super.FM.isPlayers() && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.onGround())
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[1].getRPM() < 100F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.cockpitDoorControl = 1.0F;
            else
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.cockpitDoorControl = 0.0F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(super.thisWeaponsName.startsWith("16x") || super.thisWeaponsName.startsWith("5x"))
        {
            hierMesh().chunkVisible("Blister1_D0", false);
            return;
        } else
        {
            return;
        }
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -35F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -35F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 20F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 20F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -110F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -110F * f, 0.0F);
    }

    protected void moveGear(float f)
    {
        com.maddox.il2.objects.air.DC_2.moveGear(hierMesh(), f);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Door01_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(f, 0.0F, 0.99F, 0.0F, 160F), 0.0F);
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(f, 0.0F, 0.99F, 0.0F, 160F), 0.0F);
        hierMesh().chunkSetAngles("Hatch_D0", 0.0F, 90F * f, 0.0F);
    }

    public void msgShot(com.maddox.il2.ai.Shot shot)
    {
        setShot(shot);
        if(shot.chunkName.startsWith("WingLOut") && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && java.lang.Math.abs(((com.maddox.JGP.Tuple3d) (com.maddox.il2.objects.air.Aircraft.Pd)).y) < 6D)
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 0, 1);
        if(shot.chunkName.startsWith("WingROut") && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && java.lang.Math.abs(((com.maddox.JGP.Tuple3d) (com.maddox.il2.objects.air.Aircraft.Pd)).y) < 6D)
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 3, 1);
        if(shot.chunkName.startsWith("WingLIn") && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && java.lang.Math.abs(((com.maddox.JGP.Tuple3d) (com.maddox.il2.objects.air.Aircraft.Pd)).y) < 1.940000057220459D)
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 1, 1);
        if(shot.chunkName.startsWith("WingRIn") && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && java.lang.Math.abs(((com.maddox.JGP.Tuple3d) (com.maddox.il2.objects.air.Aircraft.Pd)).y) < 1.940000057220459D)
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 2, 1);
        if(shot.chunkName.startsWith("Engine1") && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
        if(shot.chunkName.startsWith("Engine2") && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 1, 1);
        if(shot.chunkName.startsWith("Nose") && ((com.maddox.JGP.Tuple3d) (com.maddox.il2.objects.air.Aircraft.Pd)).x > 4.9000000953674316D && ((com.maddox.JGP.Tuple3d) (com.maddox.il2.objects.air.Aircraft.Pd)).z > -0.090000003576278687D && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.1F)
            if(((com.maddox.JGP.Tuple3d) (com.maddox.il2.objects.air.Aircraft.Pd)).y > 0.0D)
            {
                killPilot(shot.initiator, 0);
                super.FM.setCapableOfBMP(false, shot.initiator);
            } else
            {
                killPilot(shot.initiator, 1);
            }
        if(shot.chunkName.startsWith("Tail") && ((com.maddox.JGP.Tuple3d) (com.maddox.il2.objects.air.Aircraft.Pd)).x < -5.8000001907348633D && ((com.maddox.JGP.Tuple3d) (com.maddox.il2.objects.air.Aircraft.Pd)).x > -6.7899999618530273D && ((com.maddox.JGP.Tuple3d) (com.maddox.il2.objects.air.Aircraft.Pd)).z > -0.44900000000000001D && ((com.maddox.JGP.Tuple3d) (com.maddox.il2.objects.air.Aircraft.Pd)).z < 0.12399999797344208D)
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitPilot(shot.initiator, com.maddox.il2.ai.World.Rnd().nextInt(3, 4), (int)(shot.mass * 1000F * com.maddox.il2.ai.World.Rnd().nextFloat(0.9F, 1.1F)));
        if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateEngineStates[0] > 2 && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateEngineStates[1] > 2 && com.maddox.il2.ai.World.Rnd().nextInt(0, 99) < 33)
            super.FM.setCapableOfBMP(false, shot.initiator);
        super.msgShot(shot);
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 3; i++)
            if(super.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected boolean cutFM(int i, int j, com.maddox.il2.engine.Actor actor)
    {
        switch(i)
        {
        default:
            break;

        case 13: // '\r'
            killPilot(this, 0);
            killPilot(this, 1);
            break;

        case 35: // '#'
            if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.25F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(this, 1, com.maddox.il2.ai.World.Rnd().nextInt(2, 6));
            break;

        case 38: // '&'
            if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.25F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.hitTank(this, 2, com.maddox.il2.ai.World.Rnd().nextInt(2, 6));
            break;
        }
        return super.cutFM(i, j, actor);
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

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.DC_2.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "DC-2");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/DC-2/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        com.maddox.rts.Property.set(class1, "originCountry", com.maddox.il2.objects.air.PaintScheme.countryUSA);
        com.maddox.rts.Property.set(class1, "yearService", 1934F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1939F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/DC-2.fmd:DC2_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitC47.class
        });
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_BombSpawn01"
        });
    }
}