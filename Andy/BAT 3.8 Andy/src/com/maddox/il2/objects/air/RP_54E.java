// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 30.11.2019 11:48:26
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   RP_54E.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Way;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Land2D;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Autopilotage;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Finger;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.air:
//            ZOK_Drones, TypeDockable, Aircraft, Chute, 
//            PaintSchemeFMPar05, TypeStormovik, TypeFighterAceMaker, TypeRadarGunsight, 
//            NetAircraft

public class RP_54E extends com.maddox.il2.objects.air.ZOK_Drones
    implements com.maddox.il2.objects.air.TypeStormovik, com.maddox.il2.objects.air.TypeFighterAceMaker, com.maddox.il2.objects.air.TypeRadarGunsight, com.maddox.il2.objects.air.TypeDockable
{

    public RP_54E()
    {
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        bNeedSetup = true;
        DroneType = 0;
        DroneReleased = 0;
        ChuteReleased = false;
        OnChute54 = false;
        Airbags = false;
        NextECM = 0L;
        NextMsg = 0L;
        bDynamoOperational = true;
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
        APmode1 = false;
        APmode2 = false;
        APmode3 = false;
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", -30F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Rudder2_D0", -30F * f, 0.0F, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -30F * f);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -30F * f);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 30F * f);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -30F * f);
    }

    public void typeFighterAceMakerRangeFinder()
    {
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset()
    {
    }

    public void typeFighterAceMakerAdjDistancePlus()
    {
        k14Distance += 10F;
        if(k14Distance > 800F)
            k14Distance = 800F;
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 10F;
        if(k14Distance < 200F)
            k14Distance = 200F;
    }

    public void typeFighterAceMakerAdjSideslipReset()
    {
    }

    public void typeFighterAceMakerAdjSideslipPlus()
    {
        k14WingspanType--;
        if(k14WingspanType < 0)
            k14WingspanType = 0;
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
    }

    public void typeFighterAceMakerReplicateToNet(com.maddox.rts.NetMsgGuaranted netmsgguaranted)
        throws java.io.IOException
    {
        netmsgguaranted.writeByte(k14Mode);
        netmsgguaranted.writeByte(k14WingspanType);
        netmsgguaranted.writeFloat(k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(com.maddox.rts.NetMsgInput netmsginput)
        throws java.io.IOException
    {
        k14Mode = netmsginput.readByte();
        k14WingspanType = netmsginput.readByte();
        k14Distance = netmsginput.readFloat();
    }

    public void auxPressed(int i)
    {
        if(i == 20)
            if(!APmode1)
            {
                if(((com.maddox.il2.engine.Interpolate) (super.FM)).actor == com.maddox.il2.ai.World.getPlayerAircraft())
                    com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "Autopilot: Altitude hold ON");
                ((com.maddox.il2.fm.FlightModelMain) (FM)).AP.setStabAltitude(FM.getAltitude());
                APmode1 = true;
            } else
            if(APmode1)
            {
                if(((com.maddox.il2.engine.Interpolate) (super.FM)).actor == com.maddox.il2.ai.World.getPlayerAircraft())
                    com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "Autopilot: Altitude hold OFF");
                ((com.maddox.il2.fm.FlightModelMain) (FM)).AP.setStabAltitude(false);
                APmode1 = false;
            }
        if(i == 21)
            if(!APmode2)
            {
                if(((com.maddox.il2.engine.Interpolate) (super.FM)).actor == com.maddox.il2.ai.World.getPlayerAircraft())
                    com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "Autopilot: Direction hold ON");
                ((com.maddox.il2.fm.FlightModelMain) (FM)).AP.setStabDirection(true);
                ((com.maddox.il2.fm.FlightModelMain) (FM)).CT.bHasRudderControl = false;
                APmode2 = true;
            } else
            if(APmode2)
            {
                if(((com.maddox.il2.engine.Interpolate) (super.FM)).actor == com.maddox.il2.ai.World.getPlayerAircraft())
                    com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "Autopilot: Direction hold OFF");
                ((com.maddox.il2.fm.FlightModelMain) (FM)).AP.setStabDirection(false);
                ((com.maddox.il2.fm.FlightModelMain) (FM)).CT.bHasRudderControl = true;
                APmode2 = false;
            }
        if(i == 22)
            if(!APmode3)
            {
                if(((com.maddox.il2.engine.Interpolate) (super.FM)).actor == com.maddox.il2.ai.World.getPlayerAircraft())
                    com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "Autopilot: Route hold ON");
                ((com.maddox.il2.fm.FlightModelMain) (FM)).AP.setWayPoint(true);
                APmode3 = true;
            } else
            if(APmode3)
            {
                if(((com.maddox.il2.engine.Interpolate) (super.FM)).actor == com.maddox.il2.ai.World.getPlayerAircraft())
                    com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "Autopilot: Route hold OFF");
                ((com.maddox.il2.fm.FlightModelMain) (FM)).AP.setWayPoint(false);
                ((com.maddox.il2.fm.FlightModelMain) (FM)).CT.AileronControl = 0.0F;
                ((com.maddox.il2.fm.FlightModelMain) (FM)).CT.ElevatorControl = 0.0F;
                ((com.maddox.il2.fm.FlightModelMain) (FM)).CT.RudderControl = 0.0F;
                APmode3 = false;
            }
        if(i == 23)
        {
            ((com.maddox.il2.fm.FlightModelMain) (FM)).CT.AileronControl = 0.0F;
            ((com.maddox.il2.fm.FlightModelMain) (FM)).CT.ElevatorControl = 0.0F;
            ((com.maddox.il2.fm.FlightModelMain) (FM)).CT.RudderControl = 0.0F;
            ((com.maddox.il2.fm.FlightModelMain) (FM)).AP.setWayPoint(false);
            ((com.maddox.il2.fm.FlightModelMain) (FM)).AP.setStabDirection(false);
            ((com.maddox.il2.fm.FlightModelMain) (FM)).AP.setStabAltitude(false);
            ((com.maddox.il2.fm.FlightModelMain) (FM)).CT.bHasRudderControl = true;
            if(((com.maddox.il2.engine.Interpolate) (super.FM)).actor == com.maddox.il2.ai.World.getPlayerAircraft())
                com.maddox.il2.game.HUD.log(com.maddox.il2.game.AircraftHotKeys.hudLogWeaponId, "Autopilot: All modes OFF");
            APmode1 = false;
            APmode2 = false;
            APmode3 = false;
        }
    }

    public void msgCollisionRequest(com.maddox.il2.engine.Actor actor, boolean aflag[])
    {
        super.msgCollisionRequest(actor, aflag);
        if(queen_last != null && queen_last == actor && (queen_time == 0L || com.maddox.rts.Time.current() < queen_time + 5000L))
            aflag[0] = false;
        else
            aflag[0] = true;
    }

    public void missionStarting()
    {
        checkAsDrone();
    }

    private void checkAsDrone()
    {
        if(target_ == null)
        {
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AP.way.curr().getTarget() == null)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AP.way.next();
            target_ = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AP.way.curr().getTarget();
            if(com.maddox.il2.engine.Actor.isValid(target_) && (target_ instanceof com.maddox.il2.ai.Wing))
            {
                com.maddox.il2.ai.Wing wing = (com.maddox.il2.ai.Wing)target_;
                int i = aircIndex();
                if(com.maddox.il2.engine.Actor.isValid(wing.airc[i / 2]))
                    target_ = wing.airc[i / 2];
                else
                    target_ = null;
            }
        }
        if(com.maddox.il2.engine.Actor.isValid(target_) && (target_ instanceof com.maddox.il2.objects.air.TypeDockable))
        {
            queen_last = target_;
            queen_time = com.maddox.rts.Time.current();
            if(isNetMaster())
                ((com.maddox.il2.objects.air.TypeDockable)target_).typeDockableRequestAttach(this, aircIndex() % 2, true);
        }
        bNeedSetup = false;
        target_ = null;
    }

    public int typeDockableGetDockport()
    {
        if(typeDockableIsDocked())
            return dockport_;
        else
            return -1;
    }

    public com.maddox.il2.engine.Actor typeDockableGetQueen()
    {
        return queen_;
    }

    public boolean typeDockableIsDocked()
    {
        return com.maddox.il2.engine.Actor.isValid(queen_);
    }

    public void typeDockableAttemptAttach()
    {
        if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.isMaster() && !typeDockableIsDocked())
        {
            com.maddox.il2.objects.air.Aircraft aircraft = com.maddox.il2.ai.War.getNearestFriend(this);
            if(aircraft instanceof com.maddox.il2.objects.air.TypeDockable)
                ((com.maddox.il2.objects.air.TypeDockable)aircraft).typeDockableRequestAttach(this);
        }
    }

    public void typeDockableAttemptDetach()
    {
        if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.isMaster() && typeDockableIsDocked() && com.maddox.il2.engine.Actor.isValid(queen_))
            ((com.maddox.il2.objects.air.TypeDockable)queen_).typeDockableRequestDetach(this);
    }

    public void typeDockableRequestAttach(com.maddox.il2.engine.Actor actor)
    {
    }

    public void typeDockableRequestDetach(com.maddox.il2.engine.Actor actor)
    {
    }

    public void typeDockableRequestAttach(com.maddox.il2.engine.Actor actor, int i, boolean flag)
    {
    }

    public void typeDockableRequestDetach(com.maddox.il2.engine.Actor actor, int i, boolean flag)
    {
    }

    public void typeDockableDoAttachToDrone(com.maddox.il2.engine.Actor actor, int i)
    {
    }

    public void typeDockableDoDetachFromDrone(int i)
    {
    }

    public void typeDockableDoAttachToQueen(com.maddox.il2.engine.Actor actor, int i)
    {
        queen_ = actor;
        dockport_ = i;
        queen_last = queen_;
        queen_time = 0L;
        DroneReleased = 1;
        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.setGearAirborne();
        moveGear(0.0F);
        com.maddox.il2.fm.FlightModel flightmodel = ((com.maddox.il2.objects.sounds.SndAircraft) ((com.maddox.il2.objects.air.Aircraft)queen_)).FM;
        FM.EI.setThrottle(0.0F);
        FM.EI.engines[0].setEngineStops(this);
        if(aircIndex() == 0 && (super.FM instanceof com.maddox.il2.ai.air.Maneuver) && (flightmodel instanceof com.maddox.il2.ai.air.Maneuver))
        {
            com.maddox.il2.ai.air.Maneuver maneuver = (com.maddox.il2.ai.air.Maneuver)flightmodel;
            com.maddox.il2.ai.air.Maneuver maneuver1 = (com.maddox.il2.ai.air.Maneuver)super.FM;
            if(maneuver.Group != null && maneuver1.Group != null && maneuver1.Group.numInGroup(this) == maneuver1.Group.nOfAirc - 1)
            {
                com.maddox.il2.ai.air.AirGroup airgroup = new AirGroup(maneuver1.Group);
                maneuver1.Group.delAircraft(this);
                airgroup.addAircraft(this);
                airgroup.attachGroup(maneuver.Group);
                airgroup.rejoinGroup = null;
                airgroup.leaderGroup = null;
                airgroup.clientGroup = maneuver.Group;
            }
        }
    }

    public void typeDockableDoDetachFromQueen(int i)
    {
        if(dockport_ == i)
        {
            queen_last = queen_;
            queen_time = com.maddox.rts.Time.current();
            queen_ = null;
            dockport_ = 0;
            DroneReleased = 2;
            t1 = com.maddox.rts.Time.current() + 5000L;
            if(((com.maddox.il2.fm.FlightModelMain) (FM)).AP.way.curr().Action == 2)
                ((com.maddox.il2.fm.FlightModelMain) (FM)).AP.way.curr().Action = 0;
            if(DroneType < 1)
                com.maddox.il2.game.HUD.logCenter("Drone released");
            if(DroneType == 1)
                moveCockpitDoor(1.0F);
            if(ECMpods == 1)
            {
                setName("ECM");
                ECMpods = 2;
            }
        }
    }

    public void typeDockableReplicateToNet(com.maddox.rts.NetMsgGuaranted netmsgguaranted)
        throws java.io.IOException
    {
        if(typeDockableIsDocked())
        {
            netmsgguaranted.writeByte(1);
            com.maddox.il2.engine.ActorNet actornet = null;
            if(com.maddox.il2.engine.Actor.isValid(queen_))
            {
                actornet = queen_.net;
                if(actornet.countNoMirrors() > 0)
                    actornet = null;
            }
            netmsgguaranted.writeByte(dockport_);
            netmsgguaranted.writeNetObj(actornet);
        } else
        {
            netmsgguaranted.writeByte(0);
        }
    }

    public void typeDockableReplicateFromNet(com.maddox.rts.NetMsgInput netmsginput)
        throws java.io.IOException
    {
        if(netmsginput.readByte() == 1)
        {
            dockport_ = netmsginput.readByte();
            com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
            if(netobj != null)
            {
                com.maddox.il2.engine.Actor actor = (com.maddox.il2.engine.Actor)netobj.superObj();
                ((com.maddox.il2.objects.air.TypeDockable)actor).typeDockableDoAttachToDrone(this, dockport_);
            }
        }
    }

    private boolean FriendECM()
    {
        com.maddox.JGP.Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        com.maddox.JGP.Vector3d vector3d = new Vector3d();
        com.maddox.il2.objects.air.Aircraft aircraft = com.maddox.il2.ai.World.getPlayerAircraft();
        double d = com.maddox.il2.game.Main3D.cur3D().land2D.worldOfsX() + ((com.maddox.JGP.Tuple3d) (((com.maddox.il2.engine.Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = com.maddox.il2.game.Main3D.cur3D().land2D.worldOfsY() + ((com.maddox.JGP.Tuple3d) (((com.maddox.il2.engine.Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = com.maddox.il2.game.Main3D.cur3D().land2D.worldOfsY() + ((com.maddox.JGP.Tuple3d) (((com.maddox.il2.engine.Actor) (aircraft)).pos.getAbsPoint())).z;
        java.util.List list = com.maddox.il2.engine.Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            com.maddox.il2.engine.Actor actor = (com.maddox.il2.engine.Actor)list.get(j);
            if((actor instanceof com.maddox.il2.objects.air.Aircraft) && com.maddox.il2.engine.Actor.isAlive(actor) && actor.getArmy() != com.maddox.il2.ai.World.getPlayerArmy() && actor.pos.getAbsPoint().distance(point3d) < 20000D && actor.getSpeed(vector3d) > 20D)
            {
                if(ECMpods == 2)
                    ((com.maddox.il2.ai.air.Maneuver)((com.maddox.il2.objects.air.Aircraft)actor).FM).set_maneuver(21);
                if(ECMpods == 2 && com.maddox.rts.Time.current() >= NextMsg && ((com.maddox.il2.engine.Interpolate) (super.FM)).actor == com.maddox.il2.ai.World.getPlayerAircraft())
                    com.maddox.il2.game.HUD.log("ECM: Jamming enemy aircraft radar");
                NextECM = com.maddox.rts.Time.current() + 10000L;
                if(NextMsg <= com.maddox.rts.Time.current())
                    NextMsg = com.maddox.rts.Time.current() + 30000L;
                if(actor.pos.getAbsPoint().distance(point3d) < 5000D)
                    NextECM = com.maddox.rts.Time.current() + 1000L;
            }
        }

        return true;
    }

    private boolean FoeECM()
    {
        com.maddox.JGP.Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        com.maddox.JGP.Vector3d vector3d = new Vector3d();
        com.maddox.il2.objects.air.Aircraft aircraft = com.maddox.il2.ai.World.getPlayerAircraft();
        double d = com.maddox.il2.game.Main3D.cur3D().land2D.worldOfsX() + ((com.maddox.JGP.Tuple3d) (((com.maddox.il2.engine.Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = com.maddox.il2.game.Main3D.cur3D().land2D.worldOfsY() + ((com.maddox.JGP.Tuple3d) (((com.maddox.il2.engine.Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = com.maddox.il2.game.Main3D.cur3D().land2D.worldOfsY() + ((com.maddox.JGP.Tuple3d) (((com.maddox.il2.engine.Actor) (aircraft)).pos.getAbsPoint())).z;
        java.util.List list = com.maddox.il2.engine.Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            com.maddox.il2.engine.Actor actor = (com.maddox.il2.engine.Actor)list.get(j);
            if((actor instanceof com.maddox.il2.objects.air.Aircraft) && com.maddox.il2.engine.Actor.isAlive(actor) && aircraft.getArmy() == com.maddox.il2.ai.World.getPlayerArmy() && actor.pos.getAbsPoint().distance(point3d) < 20000D && actor.getSpeed(vector3d) > 20D)
            {
                if(ECMpods == 2)
                    ((com.maddox.il2.ai.air.Maneuver)((com.maddox.il2.objects.air.Aircraft)actor).FM).set_maneuver(21);
                if(ECMpods == 2 && com.maddox.rts.Time.current() >= NextMsg && ((com.maddox.il2.engine.Interpolate) (super.FM)).actor == com.maddox.il2.ai.World.getPlayerAircraft())
                    com.maddox.il2.game.HUD.log("Radar jamming detected");
                NextECM = com.maddox.rts.Time.current() + 10000L;
                if(NextMsg <= com.maddox.rts.Time.current())
                    NextMsg = com.maddox.rts.Time.current() + 30000L;
                if(actor.pos.getAbsPoint().distance(point3d) < 5000D)
                    NextECM = com.maddox.rts.Time.current() + 1000L;
            }
        }

        return true;
    }

    public void onAircraftLoaded()
    {
        FM.AS.bIsEnableToBailout = false;
        FM.Gears.setHydroOperable(true);
        FM.Gears.setOperable(false);
        FM.CT.bHasBrakeControl = false;
        FM.setCapableOfTaxiing(false);
        ((com.maddox.il2.ai.air.Pilot)FM).silence = true;
        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
        if(super.thisWeaponsName.startsWith("01") || super.thisWeaponsName.startsWith("02"))
            hierMesh().chunkVisible("Target", true);
        if(super.thisWeaponsName.startsWith("03"))
        {
            hierMesh().chunkVisible("ReconHatch", false);
            hierMesh().chunkVisible("ReconCover", true);
            hierMesh().chunkVisible("ReconCameras", true);
            DroneType = 1;
        }
        if(super.thisWeaponsName.startsWith("04"))
        {
            hierMesh().chunkVisible("ReconHatch", false);
            hierMesh().chunkVisible("ReconCover", true);
            hierMesh().chunkVisible("ReconCameras", true);
            hierMesh().chunkVisible("Station01", true);
            hierMesh().chunkVisible("Station02", true);
            DroneType = 1;
        }
        if(super.thisWeaponsName.startsWith("05"))
        {
            hierMesh().chunkVisible("ECM_PodR", true);
            hierMesh().chunkVisible("ECM_PodL", true);
            hierMesh().chunkVisible("ECM_MountR", true);
            hierMesh().chunkVisible("ECM_MountL", true);
            hierMesh().chunkVisible("ECM_Rat0R", true);
            hierMesh().chunkVisible("ECM_Rat0L", true);
            DroneType = 2;
            ECMpods = 1;
        }
        if(super.thisWeaponsName.startsWith("de") || super.thisWeaponsName.startsWith("06") || super.thisWeaponsName.startsWith("07"))
            DroneType = 2;
    }

    public void moveArrestorHook(float f)
    {
        if(!ChuteReleased)
        {
            if(DroneType == 1)
                FM.CT.dropExternalStores(true);
            resetYPRmodifier();
            hierMesh().chunkSetAngles("CF_D0", 0.0F, 0.0F, 90F);
            if(ECMpods > 0)
            {
                hierMesh().chunkVisible("ECM_Rat1R", false);
                hierMesh().chunkVisible("ECM_Rat0R", true);
                hierMesh().chunkVisible("ECM_Rat1L", false);
                hierMesh().chunkVisible("ECM_Rat0L", true);
                setName("NM_NN10");
                ECMpods = 0;
            }
            ChuteReleased = true;
        }
    }

    protected void moveFan(float f)
    {
        super.moveFan(f);
        if(ECMpods > 0)
        {
            if(bDynamoOperational)
            {
                pk = java.lang.Math.abs((int)(FM.Vwld.length() / 14D));
                if(pk >= 1)
                    pk = 1;
            }
            if(bDynamoRotary != (pk == 1))
            {
                bDynamoRotary = pk == 1;
                hierMesh().chunkVisible("ECM_Rat0R", !bDynamoRotary);
                hierMesh().chunkVisible("ECM_Rat1R", bDynamoRotary);
                hierMesh().chunkVisible("ECM_Rat0L", !bDynamoRotary);
                hierMesh().chunkVisible("ECM_Rat1L", bDynamoRotary);
            }
            dynamoOrient = bDynamoRotary ? (dynamoOrient - 120F) % 360F : (float)((double)dynamoOrient - FM.Vwld.length() * 1.5444015264511108D) % 360F;
            hierMesh().chunkSetAngles("ECM_Rat0R", 0.0F, 0.0F, dynamoOrient);
            hierMesh().chunkSetAngles("ECM_Rat0L", 0.0F, 0.0F, dynamoOrient);
        }
    }

    public void moveCockpitDoor(float f)
    {
        if(DroneType == 1)
        {
            resetYPRmodifier();
            com.maddox.il2.objects.air.Aircraft.xyz[1] = com.maddox.il2.objects.air.Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, 0.45F);
            com.maddox.il2.objects.air.Aircraft.ypr[2] = com.maddox.il2.objects.air.Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, 0.15F);
            hierMesh().chunkSetLocate("ReconCover", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
        }
    }

    private final void doRemoveParaHatch()
    {
        com.maddox.il2.objects.Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("ParaHatch"));
        wreckage.collide(true);
        com.maddox.JGP.Vector3d vector3d = new Vector3d();
        getSpeed(vector3d);
        vector3d.z -= 10D;
        vector3d.set(vector3d);
        wreckage.setSpeed(vector3d);
    }

    public void deploychute()
    {
        hierMesh().chunkVisible("ParaHatch", false);
        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].setEngineDies(this);
        chute = new Chute(this);
        chute.setMesh("3do/Arms/ZOK-ParaBallistic/mono.sim");
        chute.mesh().setScale(1.25F);
        chute.collide(false);
        ((com.maddox.il2.engine.Actor) (chute)).pos.setRel(new Point3d(1.3500000000000001D, 0.0D, -0.5D), new Orient(-90F, 0.0F, -90F));
        OnChute54 = true;
        moveCockpitDoor(0.0F);
        doRemoveParaHatch();
    }

    protected void hitBone(java.lang.String s, com.maddox.il2.ai.Shot shot, com.maddox.JGP.Point3d point3d)
    {
        if(DroneType == 0 && s.startsWith("xtail"))
        {
            DmgLevel = DmgLevel + 1;
            com.maddox.il2.game.HUD.logCenter("Target drone hit");
            if(chunkDamageVisible("Fuse") < 2)
                hitChunk("Fuse", shot);
            if(DmgLevel > 500)
                moveArrestorHook(90F);
        }
        if(DroneType > 0 && s.startsWith("xcf"))
        {
            if(chunkDamageVisible("Fuse") < 2)
                hitChunk("Fuse", shot);
            DmgLevel = DmgLevel + 1;
        }
        if(s.startsWith("xkeel1"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder1"))
        {
            if(chunkDamageVisible("Rudder1") < 2)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xkeel2"))
        {
            if(chunkDamageVisible("Keel2") < 2)
                hitChunk("Keel2", shot);
        } else
        if(s.startsWith("xrudder2"))
        {
            if(chunkDamageVisible("Rudder2") < 2)
                hitChunk("Rudder2", shot);
        } else
        if(s.startsWith("xstabl"))
        {
            if(chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
        } else
        if(s.startsWith("xvatorl"))
        {
            if(chunkDamageVisible("VatorL") < 2)
                hitChunk("VatorL", shot);
        } else
        if(s.startsWith("xstabr"))
        {
            if(chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvatorr"))
        {
            if(chunkDamageVisible("VatorR") < 2)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xeng"))
        {
            if(chunkDamageVisible("Engine1") < 2)
            {
                hitChunk("Engine1", shot);
                DmgLevel = DmgLevel + 1;
            }
        } else
        if(s.startsWith("xwinglin"))
        {
            if(chunkDamageVisible("WingLIn") < 2)
            {
                hitChunk("WingLIn", shot);
                DmgLevel = DmgLevel + 1;
            }
        } else
        if(s.startsWith("xaroner"))
        {
            if(chunkDamageVisible("AroneR") < 2)
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xwingrin"))
        {
            if(chunkDamageVisible("WingRIn") < 2)
            {
                hitChunk("WingRIn", shot);
                DmgLevel = DmgLevel + 1;
            }
        } else
        if(s.startsWith("xaronel") && chunkDamageVisible("AroneL") < 2)
            hitChunk("AroneL", shot);
    }

    public void setExhaustFlame(int i, int j)
    {
        if(j == 0)
            switch(i)
            {
            case 0: // '\0'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 1: // '\001'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 2: // '\002'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 3: // '\003'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                // fall through

            case 4: // '\004'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 5: // '\005'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 6: // '\006'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 7: // '\007'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 8: // '\b'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 9: // '\t'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 10: // '\n'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;

            case 11: // '\013'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;

            case 12: // '\f'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;

            default:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;
            }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(DroneType == 1 && !OnChute54 && DroneReleased == 0)
        {
            moveCockpitDoor(1.0F);
            DroneReleased = 3;
        }
        if(!ChuteReleased && DroneType == 0 && !((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.bHasAileronControl && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.nOfGearsOnGr == 0)
            moveArrestorHook(90F);
        if(!ChuteReleased && DroneType == 0 && !((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.bHasElevatorControl && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.nOfGearsOnGr == 0)
            moveArrestorHook(90F);
        if(ECMpods == 1 && !OnChute54 && DroneReleased == 0)
        {
            setName("ECM");
            ECMpods = 2;
        }
        if(ECMpods > 0 && DmgLevel > 0)
        {
            hierMesh().chunkVisible("ECM_Rat1R", false);
            hierMesh().chunkVisible("ECM_Rat0R", true);
            hierMesh().chunkVisible("ECM_Rat1L", false);
            hierMesh().chunkVisible("ECM_Rat0L", true);
            setName("NM_NN10");
            ECMpods = 0;
        }
        if(DmgLevel == 0 && !ChuteReleased && DroneReleased == 3 && ((com.maddox.il2.ai.air.Maneuver)((com.maddox.il2.objects.air.Aircraft)((com.maddox.il2.engine.Interpolate) (super.FM)).actor).FM).get_maneuver() == 49)
            moveArrestorHook(90F);
        if(DmgLevel == 0 && !ChuteReleased && DroneReleased == 0 && ((com.maddox.il2.ai.air.Maneuver)((com.maddox.il2.objects.air.Aircraft)((com.maddox.il2.engine.Interpolate) (super.FM)).actor).FM).get_maneuver() == 49)
            moveArrestorHook(90F);
        if(!ChuteReleased && DroneReleased == 0 && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F)
            moveArrestorHook(90F);
        if(!ChuteReleased && DroneReleased == 0 && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F)
            moveArrestorHook(90F);
        if(!ChuteReleased && DroneReleased == 3 && ((com.maddox.il2.fm.FlightModelMain) (FM)).AP.way.curr().Action == 2)
            moveArrestorHook(90F);
        if(!ChuteReleased && DroneReleased == 0 && ((com.maddox.il2.fm.FlightModelMain) (FM)).AP.way.curr().Action == 2)
            moveArrestorHook(90F);
        if(ECMpods > 1 && com.maddox.rts.Time.current() >= NextECM && ((com.maddox.il2.engine.Interpolate) (super.FM)).actor != com.maddox.il2.ai.World.getPlayerAircraft() && ((com.maddox.il2.engine.Interpolate) (super.FM)).actor.getArmy() != com.maddox.il2.ai.World.getPlayerArmy())
            FoeECM();
        if(ECMpods > 1 && com.maddox.rts.Time.current() >= NextECM && ((com.maddox.il2.engine.Interpolate) (super.FM)).actor != com.maddox.il2.ai.World.getPlayerAircraft() && ((com.maddox.il2.engine.Interpolate) (super.FM)).actor.getArmy() == com.maddox.il2.ai.World.getPlayerArmy())
            FriendECM();
        if(ECMpods > 1 && com.maddox.rts.Time.current() >= NextECM && ((com.maddox.il2.engine.Interpolate) (super.FM)).actor == com.maddox.il2.ai.World.getPlayerAircraft())
            FriendECM();
        if(flag && FM.AP.way.curr().Action == 3 && typeDockableIsDocked() && java.lang.Math.abs(((com.maddox.il2.objects.air.Aircraft)queen_).FM.Or.getKren()) < 10F && !(((com.maddox.il2.objects.air.Aircraft)queen_).FM instanceof com.maddox.il2.fm.RealFlightModel) && !((com.maddox.il2.fm.RealFlightModel)((com.maddox.il2.objects.air.Aircraft)queen_).FM).isRealMode())
        {
            typeDockableAttemptDetach();
            ((com.maddox.il2.ai.air.Maneuver)FM).set_maneuver(22);
            ((com.maddox.il2.ai.air.Maneuver)FM).setCheckStrike(false);
            FM.Vwld.z -= 5D;
            dtime = com.maddox.rts.Time.current();
        }
    }

    public void doSetSootState(int i, int j)
    {
        for(int k = 0; k < 2; k++)
        {
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateSootEffects[i][k] != null)
                com.maddox.il2.engine.Eff3DActor.finish(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateSootEffects[i][k]);
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateSootEffects[i][k] = null;
        }

        switch(j)
        {
        case 1: // '\001'
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = com.maddox.il2.engine.Eff3DActor.New(this, findHook("_Engine1ES_01"), null, 0.5F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = com.maddox.il2.engine.Eff3DActor.New(this, findHook("_Engine1ES_02"), null, 0.5F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 3: // '\003'
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = com.maddox.il2.engine.Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = com.maddox.il2.engine.Eff3DActor.New(this, findHook("_Engine1ES_01"), null, 0.5F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 2: // '\002'
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = com.maddox.il2.engine.Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 0.5F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = com.maddox.il2.engine.Eff3DActor.New(this, findHook("_Engine1ES_01"), null, 0.5F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 5: // '\005'
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateSootEffects[i][0] = com.maddox.il2.engine.Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/AfterBurnerF100D.eff", -1F);
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = com.maddox.il2.engine.Eff3DActor.New(this, findHook("_Engine1ES_01"), null, 0.5F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 4: // '\004'
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astateSootEffects[i][1] = com.maddox.il2.engine.Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 0.5F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;
        }
    }

    public void update(float f)
    {
        super.update(f);
        if(bNeedSetup)
            checkAsDrone();
        if((FM instanceof com.maddox.il2.ai.air.Maneuver) && (!(FM instanceof com.maddox.il2.fm.RealFlightModel) || !((com.maddox.il2.fm.RealFlightModel)FM).isRealMode()) && typeDockableIsDocked())
        {
            ((com.maddox.il2.ai.air.Maneuver)FM).unblock();
            ((com.maddox.il2.ai.air.Maneuver)FM).set_maneuver(48);
            ((com.maddox.il2.ai.air.Maneuver)FM).AP.way.setCur(((com.maddox.il2.objects.air.Aircraft)queen_).FM.AP.way.Cur());
            ((com.maddox.il2.ai.air.Pilot)FM).setDumbTime(3000L);
        }
        if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.isMaster() && com.maddox.il2.engine.Config.isUSE_RENDER())
            if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() > 0.5F && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getStage() == 6)
            {
                if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() > 0.5F)
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setSootState(this, 0, 3);
            } else
            {
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setSootState(this, 0, 0);
            }
        setExhaustFlame(java.lang.Math.round(com.maddox.il2.objects.air.Aircraft.cvt(((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
        if(DroneReleased == 2 && t1 < com.maddox.rts.Time.current())
        {
            FM.EI.setThrottle(0.5F);
            FM.EI.engines[0].setEngineRunning(this);
            DroneReleased = 3;
        }
        if(ChuteReleased)
        {
            if(!OnChute54)
                deploychute();
            getSpeed(v3d);
            v3d.scale(0.96999999999999997D);
            if(java.lang.Math.abs(((com.maddox.JGP.Tuple3d) (v3d)).x) < 9.9999997473787516E-006D)
                v3d.x = 9.9999997473787516E-006D;
            if(java.lang.Math.abs(((com.maddox.JGP.Tuple3d) (v3d)).y) < 9.9999997473787516E-006D)
                v3d.y = 9.9999997473787516E-006D;
            if(v3d.z < -2D)
                v3d.z += 1.1F * com.maddox.rts.Time.tickConstLenFs();
            setSpeed(v3d);
        }
        if(ChuteReleased && !Airbags && super.FM.getAltitude() < 500F)
        {
            hierMesh().chunkVisible("AirbagsOut", true);
            hierMesh().chunkVisible("AirbagsIn", false);
            Strobe = com.maddox.il2.engine.Eff3DActor.New(this, findHook("_Strobe"), null, 1.0F, "3do/Effects/ZOK/ZOK_Strobe.eff", -1F);
            Airbags = true;
        }
        if(ChuteReleased && super.FM.Gears.onGround())
            chute.destroy();
        if(DroneReleased == 1 && super.FM.Gears.onGround() || DroneReleased == 3 && super.FM.Gears.onGround())
        {
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].setEngineDies(this);
            DroneReleased = 4;
        }
    }


    public boolean APmode1;
    public boolean APmode2;
    public boolean APmode3;
    private boolean bNeedSetup;
    private long dtime;
    private com.maddox.il2.engine.Actor queen_last;
    private long queen_time;
    private com.maddox.il2.engine.Actor target_;
    private com.maddox.il2.engine.Actor queen_;
    private int dockport_;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    private int ECMpods;
    private long NextECM;
    private long NextMsg;
    private boolean bDynamoOperational;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int pk;
    private int DroneType;
    private int DroneReleased;
    private int DmgLevel;
    private long t1;
    private boolean ChuteReleased;
    public boolean OnChute54;
    private com.maddox.il2.objects.air.Chute chute;
    private static com.maddox.JGP.Vector3d v3d = new Vector3d();
    private boolean Airbags;
    private com.maddox.il2.engine.Eff3DActor Strobe;

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.RP_54E.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "RP-54E Drone");
        com.maddox.rts.Property.set(class1, "meshName", "3do/Plane/RP-54E(Multi1)/Hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        com.maddox.rts.Property.set(class1, "yearService", 1949F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1969F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/RP-54E.fmd:RP-54E_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitRP_54E.class
        });
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 3, 3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_CANNON01", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02"
        });
    }
}