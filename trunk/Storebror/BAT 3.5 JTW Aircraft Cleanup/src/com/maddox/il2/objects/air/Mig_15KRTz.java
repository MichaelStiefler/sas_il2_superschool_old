package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class Mig_15KRTz extends Mig_15K
    implements TypeTransport, TypeTankerDrogue, TypeZBReceiver, TypeDockable, TypeStormovik
{

    public Mig_15KRTz()
    {
        FlapAngle = 55F;
        hasDroptanks = false;
        hasBuddy = false;
        WingOn = true;
        ProbeOut = false;
        hasBoosters = false;
        Ratos = 0;
        boosterFireOutTime = -1L;
        FuelOn = false;
        FuelLow = false;
        FuelStop = false;
        WaveOff = false;
        DockSpeed = 10F;
        DockDistance = 5D;
        bDynamoOperational = true;
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
        drones = new Actor[2];
        overrideBailout = false;
        ejectComplete = false;
        lTimeNextEject = 0L;
        APmode5 = false;
        ExtFuel = 0.0F;
        APmode6 = false;
        APmode7 = false;
        PumpedFuel = 0.0F;
        DrogueCtrl = false;
    }

    public boolean typeDockableIsDocked()
    {
        if(!hasBuddy)
            return Actor.isValid(queen_);
        else
            return true;
    }

    public void typeDockableAttemptAttach()
    {
        if(!hasBuddy && this.FM.AS.isMaster() && !typeDockableIsDocked())
        {
            Aircraft aircraft = War.getNearestFriend(this);
            if(aircraft instanceof TypeTankerDrogue)
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
        }
    }

    public void typeDockableAttemptDetach()
    {
        if(hasBuddy && this.FM.AS.isMaster())
        {
            for(int i = 0; i < drones.length; i++)
                if(Actor.isValid(drones[i]))
                    typeDockableRequestDetach(drones[i], i, true);

        }
        if(!hasBuddy)
        {
            if(this.FM.AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
                ((TypeDockable)queen_).typeDockableRequestDetach(this);
            if(ProbeOut)
            {
                moveRefuel(-90F);
                ProbeOut = false;
            }
        }
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        super.msgCollisionRequest(actor, aflag);
        if(queen_last != null && queen_last == actor && (queen_time == 0L || Time.current() < queen_time + 5000L))
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
        if(!hasBuddy)
        {
            if(target_ == null)
            {
                if(this.FM.AP.way.curr().getTarget() == null)
                    this.FM.AP.way.next();
                target_ = this.FM.AP.way.curr().getTarget();
                if(Actor.isValid(target_) && (target_ instanceof Wing))
                {
                    Wing wing = (Wing)target_;
                    int i = aircIndex();
                    if(Actor.isValid(wing.airc[i / 2]))
                        target_ = wing.airc[i / 2];
                    else
                        target_ = null;
                }
            }
            if(Actor.isValid(target_) && (target_ instanceof TypeTankerDrogue))
            {
                queen_last = target_;
                queen_time = Time.current();
                if(isNetMaster())
                    ((TypeDockable)target_).typeDockableRequestAttach(this, aircIndex() % 2, true);
            }
            bNeedSetup = false;
            target_ = null;
        }
    }

    public int typeDockableGetDockport()
    {
        if(typeDockableIsDocked())
            return dockport_;
        else
            return -1;
    }

    public Actor typeDockableGetQueen()
    {
        return queen_;
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i)
    {
        if(!hasBuddy)
        {
            queen_ = actor;
            dockport_ = i;
            queen_last = queen_;
            queen_time = 0L;
            this.FM.EI.setEngineRunning();
            this.FM.CT.setGearAirborne();
            moveGear(0.0F);
            FlightModel flightmodel = ((SndAircraft) ((Aircraft)queen_)).FM;
            if(aircIndex() == 0 && (this.FM instanceof Maneuver) && (flightmodel instanceof Maneuver))
            {
                Maneuver maneuver = (Maneuver)flightmodel;
                Maneuver maneuver1 = (Maneuver)this.FM;
                if(maneuver.Group != null && maneuver1.Group != null && maneuver1.Group.numInGroup(this) == maneuver1.Group.nOfAirc - 1)
                {
                    AirGroup airgroup = new AirGroup(maneuver1.Group);
                    maneuver1.Group.delAircraft(this);
                    airgroup.addAircraft(this);
                    airgroup.attachGroup(maneuver.Group);
                    airgroup.rejoinGroup = null;
                    airgroup.leaderGroup = null;
                    airgroup.clientGroup = maneuver.Group;
                }
            }
        }
    }

    public void typeDockableDoDetachFromQueen(int i)
    {
        if(dockport_ == i)
        {
            queen_last = queen_;
            queen_time = Time.current();
            queen_ = null;
            dockport_ = 0;
            if(ProbeOut)
            {
                moveRefuel(-90F);
                ProbeOut = false;
            }
        }
    }

    public void moveRefuel(float f)
    {
        if(!hasBuddy)
        {
            resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.95F, 0.0F, -0.8F);
            hierMesh().chunkSetLocate("ExtrasProbe", Aircraft.xyz, Aircraft.ypr);
        }
        if(hasBuddy && f > 0.0F && f < 1.0F)
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Standby...");
        if(hasBuddy && ProbeOut && f == 1.0F)
        {
            DrogueCtrl = true;
            ((TypeDockable)((Interpolate) (FM)).actor).typeDockableAttemptDetach();
            hierMesh().chunkVisible("UPAZDrogueOut", false);
            hierMesh().chunkVisible("UPAZDrogueIn", true);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Drogue retracted");
            ProbeOut = false;
            f = -1F;
        }
        if(hasBuddy && !ProbeOut && f == 0.0F)
        {
            DrogueCtrl = true;
            hierMesh().chunkVisible("UPAZDrogueOut", true);
            hierMesh().chunkVisible("UPAZDrogueIn", false);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Drogue deployed");
            ProbeOut = true;
            f = 2.0F;
        }
    }

    public void typeDockableRequestAttach(Actor actor)
    {
        if(hasBuddy && (actor instanceof Aircraft))
        {
            Aircraft aircraft = (Aircraft)actor;
            if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AS.isMaster() && ((SndAircraft) (aircraft)).FM.getSpeedKMH() > DockSpeed && this.FM.getSpeedKMH() > DockSpeed)
            {
                for(int i = 0; i < 1; i++)
                {
                    if(Actor.isValid(drones[i]))
                        continue;
                    HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    this.pos.getAbs(loc1);
                    hooknamed.computePos(this, loc1, loc);
                    actor.pos.getAbs(loc1);
                    if(loc.getPoint().distance(loc1.getPoint()) >= DockDistance)
                        continue;
                    if(this.FM.AS.isMaster())
                        typeDockableRequestAttach(actor, i, true);
                    else
                        this.FM.AS.netToMaster(32, i, 0, actor);
                    break;
                }

            }
        }
    }

    public void typeDockableRequestDetach(Actor actor)
    {
        if(hasBuddy)
        {
            for(int i = 0; i < drones.length; i++)
                if(actor == drones[i])
                {
                    Aircraft aircraft = (Aircraft)actor;
                    if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AS.isMaster())
                        if(this.FM.AS.isMaster())
                            typeDockableRequestDetach(actor, i, true);
                        else
                            this.FM.AS.netToMaster(33, i, 1, actor);
                }

        }
    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag)
    {
        if(hasBuddy && i >= 0 && i <= 4)
            if(flag)
            {
                if(this.FM.AS.isMaster())
                {
                    this.FM.AS.netToMirrors(34, i, 1, actor);
                    typeDockableDoAttachToDrone(actor, i);
                } else
                {
                    this.FM.AS.netToMaster(34, i, 1, actor);
                }
            } else
            if(this.FM.AS.isMaster())
            {
                if(!Actor.isValid(drones[i]))
                {
                    this.FM.AS.netToMirrors(34, i, 1, actor);
                    typeDockableDoAttachToDrone(actor, i);
                }
            } else
            {
                this.FM.AS.netToMaster(34, i, 0, actor);
            }
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag)
    {
        if(hasBuddy && flag)
            if(this.FM.AS.isMaster())
            {
                this.FM.AS.netToMirrors(35, i, 1, actor);
                typeDockableDoDetachFromDrone(i);
            } else
            {
                this.FM.AS.netToMaster(35, i, 1, actor);
            }
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i)
    {
        if(hasBuddy && !Actor.isValid(drones[i]) && i < 1)
        {
            HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
            Loc loc = new Loc();
            Loc loc1 = new Loc();
            this.pos.getAbs(loc1);
            hooknamed.computePos(this, loc1, loc);
            HookNamed hooknamed1 = new HookNamed((ActorMesh)actor, "_Probe");
            Loc loc2 = new Loc();
            hooknamed1.computePos(this, loc, loc2);
            actor.pos.setAbs(loc2);
            actor.pos.setBase(this, null, true);
            actor.pos.resetAsBase();
            drones[i] = actor;
            ((TypeDockable)drones[i]).typeDockableDoAttachToQueen(this, i);
        }
    }

    public void typeDockableDoDetachFromDrone(int i)
    {
        if(hasBuddy && Actor.isValid(drones[i]))
        {
            drones[i].pos.setBase(null, null, true);
            ((TypeDockable)drones[i]).typeDockableDoDetachFromQueen(i);
            drones[i] = null;
            Eff3DActor.finish(GreenSignal);
            Eff3DActor.finish(YellowSignal);
            Eff3DActor.finish(RedSignal);
            FuelOn = false;
            FuelLow = false;
            FuelStop = false;
            WaveOff = false;
        }
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        if(hasBuddy)
        {
            for(int i = 0; i < drones.length; i++)
                if(Actor.isValid(drones[i]))
                {
                    netmsgguaranted.writeByte(1);
                    ActorNet actornet1 = drones[i].net;
                    if(actornet1.countNoMirrors() == 0)
                        netmsgguaranted.writeNetObj(actornet1);
                    else
                        netmsgguaranted.writeNetObj(null);
                } else
                {
                    netmsgguaranted.writeByte(0);
                }

        }
        if(!hasBuddy)
            if(typeDockableIsDocked())
            {
                netmsgguaranted.writeByte(1);
                ActorNet actornet = null;
                if(Actor.isValid(queen_))
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

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        if(hasBuddy)
        {
            for(int i = 0; i < drones.length; i++)
                if(netmsginput.readByte() == 1)
                {
                    NetObj netobj1 = netmsginput.readNetObj();
                    if(netobj1 != null)
                        typeDockableDoAttachToDrone((Actor)netobj1.superObj(), i);
                }

        }
        if(!hasBuddy && netmsginput.readByte() == 1)
        {
            dockport_ = netmsginput.readByte();
            NetObj netobj = netmsginput.readNetObj();
            if(netobj != null)
            {
                Actor actor = (Actor)netobj.superObj();
                ((TypeDockable)actor).typeDockableDoAttachToDrone(this, dockport_);
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        if(this.FM.AS.isMaster())
            switch(i)
            {
            default:
                break;

            case 33:
            case 34:
            case 35:
                if(hasBuddy)
                {
                    typeDockableRequestDetach(drones[0], 0, true);
                    break;
                }
                // fall through

            case 36:
            case 37:
            case 38:
                doCutBoosters();
                FM.AS.setGliderBoostOff();
                hasBoosters = false;
                typeDockableRequestDetach(drones[1], 1, true);
                break;
            }
        return super.cutFM(i, j, actor);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        Aircraft aircraft = World.getPlayerAircraft();
        if(this.thisWeaponsName.startsWith("01"))
        {
            hierMesh().chunkVisible("CamCoverAft", true);
            hierMesh().chunkVisible("CamCoverFwd", true);
            scopemode = 2;
        }
        if(this.thisWeaponsName.startsWith("02"))
        {
            hierMesh().chunkVisible("S2PTB", true);
            hierMesh().chunkVisible("S5PTB", true);
            hierMesh().chunkVisible("CamCoverAft", true);
            hierMesh().chunkVisible("CamCoverFwd", true);
            FlapAngle = 25F;
            hasDroptanks = true;
            if(this.FM.M.fuel > 355F)
                hasBoosters = true;
            scopemode = 2;
        }
        if(this.thisWeaponsName.startsWith("03"))
        {
            hierMesh().chunkVisible("S1FAB", true);
            hierMesh().chunkVisible("S2PTB", true);
            hierMesh().chunkVisible("S5PTB", true);
            hierMesh().chunkVisible("S6FAB", true);
            hierMesh().chunkVisible("CamCoverAft", true);
            hierMesh().chunkVisible("CamCoverFwd", true);
            FlapAngle = 25F;
            hasDroptanks = true;
            if(this.FM.M.fuel > 259F)
                hasBoosters = true;
            scopemode = 2;
        }
        if(this.thisWeaponsName.startsWith("04"))
        {
            hierMesh().chunkVisible("S2PTB", true);
            hierMesh().chunkVisible("S3PTB", true);
            hierMesh().chunkVisible("S4PTB", true);
            hierMesh().chunkVisible("S5PTB", true);
            hierMesh().chunkVisible("UPAZ", true);
            hierMesh().chunkVisible("UPAZRATSt", true);
            FlapAngle = 25F;
            hasDroptanks = true;
            hasBuddy = true;
            hasBoosters = true;
            this.FM.M.maxFuel += 600F;
            this.FM.M.fuel += 600F;
        }
        if(this.thisWeaponsName.startsWith("05"))
        {
            hierMesh().chunkVisible("S2PTB", true);
            hierMesh().chunkVisible("S3PTB", true);
            hierMesh().chunkVisible("S4PTB", true);
            hierMesh().chunkVisible("S5PTB", true);
            hierMesh().chunkVisible("UPAZ", true);
            hierMesh().chunkVisible("UPAZRATSt", true);
            FlapAngle = 25F;
            hasDroptanks = true;
            hasBuddy = true;
            hasBoosters = true;
            DockSpeed = 30F;
            DockDistance = 15D;
            this.FM.M.maxFuel += 600F;
            this.FM.M.fuel += 600F;
        }
    }

    public void doFireBoosters()
    {
        RatoL = Eff3DActor.New(this, findHook("_RatoL"), null, 1.0F, "3do/Effects/P85/P85_Rato.eff", -1F);
        RatoR = Eff3DActor.New(this, findHook("_RatoR"), null, 1.0F, "3do/Effects/P85/P85_Rato.eff", -1F);
        RatoLsmk = Eff3DActor.New(this, findHook("_RatoLsmk"), null, 1.0F, "3do/Effects/P85/P85_RocketSmoke.eff", -1F);
        RatoRsmk = Eff3DActor.New(this, findHook("_RatoRsmk"), null, 1.0F, "3do/Effects/P85/P85_RocketSmoke.eff", -1F);
    }

    public void doCutBoosters()
    {
        Eff3DActor.finish(RatoL);
        Eff3DActor.finish(RatoR);
        Eff3DActor.finish(RatoLsmk);
        Eff3DActor.finish(RatoRsmk);
    }

    public void doDropBoosters()
    {
        hierMesh().chunkVisible("RatoR", false);
        doRemoveRatoR();
        hierMesh().chunkVisible("RatoL", false);
        doRemoveRatoL();
    }

    private final void doRemoveRatoR()
    {
        Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("RatoR"));
        wreckage.collide(true);
        Vector3d vector3d = new Vector3d();
        getSpeed(vector3d);
        vector3d.z -= 10D;
        vector3d.set(vector3d);
        wreckage.setSpeed(vector3d);
    }

    private final void doRemoveRatoL()
    {
        Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("RatoL"));
        wreckage.collide(true);
        Vector3d vector3d = new Vector3d();
        getSpeed(vector3d);
        vector3d.z -= 10D;
        vector3d.set(vector3d);
        wreckage.setSpeed(vector3d);
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        }
    }

    public void doEjectCatapult()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 23D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(12, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat_D0", false);
        this.FM.setTakenMortalDamage(true, null);
        this.FM.CT.WeaponControl[0] = false;
        this.FM.CT.WeaponControl[1] = false;
        this.FM.CT.bHasAileronControl = false;
        this.FM.CT.bHasRudderControl = false;
        this.FM.CT.bHasElevatorControl = false;
        radarmode = 5;
    }

    private void bailout()
    {
        if(overrideBailout)
            if(this.FM.AS.astateBailoutStep >= 0 && this.FM.AS.astateBailoutStep < 2)
            {
                if(this.FM.CT.cockpitDoorControl > 0.5F && this.FM.CT.getCockpitDoor() > 0.5F)
                {
                    this.FM.AS.astateBailoutStep = 11;
                    doRemoveBlisters();
                } else
                {
                    this.FM.AS.astateBailoutStep = 2;
                }
            } else
            if(this.FM.AS.astateBailoutStep >= 2 && this.FM.AS.astateBailoutStep <= 3)
            {
                switch(this.FM.AS.astateBailoutStep)
                {
                case 2:
                    if(this.FM.CT.cockpitDoorControl < 0.5F)
                    {
                        lTimeNextEject = Time.current() + 800L;
                        doRemoveBlister1();
                    }
                    break;

                case 3:
                    doRemoveBlisters();
                    break;
                }
                if(this.FM.AS.isMaster())
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                AircraftState aircraftstate = this.FM.AS;
                aircraftstate.astateBailoutStep = (byte)(aircraftstate.astateBailoutStep + 1);
                if(this.FM.AS.astateBailoutStep == 4)
                    this.FM.AS.astateBailoutStep = 11;
            } else
            if(this.FM.AS.astateBailoutStep >= 11 && this.FM.AS.astateBailoutStep <= 19)
            {
                byte byte0 = this.FM.AS.astateBailoutStep;
                if(this.FM.AS.isMaster())
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                AircraftState aircraftstate1 = this.FM.AS;
                aircraftstate1.astateBailoutStep = (byte)(aircraftstate1.astateBailoutStep + 1);
                if(byte0 == 11)
                {
                    this.FM.setTakenMortalDamage(true, null);
                    if((this.FM instanceof Maneuver) && ((Maneuver)this.FM).get_maneuver() != 44)
                    {
                        World.cur();
                        if(this.FM.AS.actor != World.getPlayerAircraft())
                            ((Maneuver)this.FM).set_maneuver(44);
                    }
                }
                if(this.FM.AS.astatePilotStates[byte0 - 11] < 99)
                {
                    doRemoveBodyFromPlane(byte0 - 10);
                    if(byte0 == 11)
                    {
                        Eff3DActor.New(this, findHook("_EjectSmk"), null, 1.0F, "3do/Effects/P85/P85_RocketSmoke.eff", 0.5F);
                        doEjectCatapult();
                        this.FM.setTakenMortalDamage(true, null);
                        this.FM.CT.WeaponControl[0] = false;
                        this.FM.CT.WeaponControl[1] = false;
                        this.FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        this.FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                }
            }
    }

    private final void doRemoveBlister1()
    {
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1 && this.FM.AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    private final void doRemoveBlisters()
    {
        for(int i = 2; i < 10; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && this.FM.AS.getPilotHealth(i - 1) > 0.0F)
            {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(this.FM.Vwld);
                wreckage.setSpeed(vector3d);
            }

    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 24 && !APmode5 && !hasBoosters && FM.Gears.onGround())
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Ground crew: SPRD's fitted");
            hasBoosters = true;
            boosterFireOutTime = -1L;
            Ratos = 0;
            APmode5 = true;
        }
        if(i == 25 && ExtFuel > 0.0F && !APmode6)
        {
            APmode6 = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Transferring fuel before tank jettison");
        }
        if(i == 26 && !APmode7)
        {
            HUD.log("Ground proximity line ON");
            APmode7 = true;
        }
    }

    protected void moveFan(float f)
    {
        if(hasBuddy)
        {
            if(bDynamoOperational)
            {
                pk = Math.abs((int)(FM.Vwld.length() / 14D));
                if(pk >= 1)
                    pk = 1;
            }
            if(bDynamoRotary != (pk == 1))
            {
                bDynamoRotary = pk == 1;
                hierMesh().chunkVisible("UPAZRATSt", !bDynamoRotary);
                hierMesh().chunkVisible("UPAZRATMv", bDynamoRotary);
            }
            dynamoOrient = bDynamoRotary ? (dynamoOrient - 120F) % 360F : (float)((double)dynamoOrient - FM.Vwld.length() * 1.5444015264511108D) % 360F;
            hierMesh().chunkSetAngles("UPAZRATSt", 0.0F, 0.0F, dynamoOrient);
            super.moveFan(f);
        }
    }

    protected void moveFlap(float f)
    {
        float f1 = -FlapAngle * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void moveGear(float f)
    {
        super.moveGear(f);
        if(scopemode == 2)
        {
            resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.16F);
            Aircraft.xyz[2] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0045F);
            hierMesh().chunkSetLocate("CamCoverFwd", Aircraft.xyz, Aircraft.ypr);
            resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.1727F);
            Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0115F);
            hierMesh().chunkSetLocate("CamCoverAft", Aircraft.xyz, Aircraft.ypr);
        }
    }

    public float checkExtFuel(int i)
    {
        FuelTank afueltank[] = this.FM.CT.getFuelTanks();
        if(afueltank.length == 0)
        {
            return 0.0F;
        } else
        {
//            ExtFuel = afueltank[0].Fuel * 1.102311F + afueltank[1].Fuel * 1.102311F;
            ExtFuel = Reflection.getFloat(afueltank[0], "Fuel") * 1.102311F + Reflection.getFloat(afueltank[1], "Fuel") * 1.102311F;
            return ExtFuel;
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(hasDroptanks && !APmode6)
            checkExtFuel(0);
        if(ExtFuel <= 0.0F && APmode6)
        {
            this.FM.CT.dropFuelTanks();
            hasDroptanks = false;
            APmode6 = false;
        }
        if(!DrogueCtrl && hasBuddy)
            if(this.FM.getAltitude() <= 1000F || (double)this.FM.CT.getGear() > 0.0D)
            {
                hierMesh().chunkVisible("UPAZDrogueOut", false);
                hierMesh().chunkVisible("UPAZDrogueIn", true);
                if(ProbeOut)
                {
                    moveRefuel(-90F);
                    ProbeOut = false;
                }
            } else
            {
                hierMesh().chunkVisible("UPAZDrogueOut", true);
                hierMesh().chunkVisible("UPAZDrogueIn", false);
                if(!ProbeOut)
                {
                    moveRefuel(90F);
                    ProbeOut = true;
                }
            }
    }

    public void update(float f)
    {
        if((this.FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && this.FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            this.FM.AS.bIsAboutToBailout = false;
            if(Time.current() > lTimeNextEject)
                bailout();
        }
        if(Ratos == 0 && hasBoosters && this.FM.Gears.onGround())
        {
            hierMesh().chunkVisible("RatoMounts", true);
            hierMesh().chunkVisible("RatoL", true);
            hierMesh().chunkVisible("RatoR", true);
            Ratos = 1;
        }
        if((this.FM instanceof Pilot) && hasBoosters)
        {
            if(FM.getAltitude() > 500F && boosterFireOutTime == -1L && FM.Loc.z != 0.0D && World.Rnd().nextFloat() < 0.05F)
            {
                doCutBoosters();
                FM.AS.setGliderBoostOff();
                hasBoosters = false;
            }
            if(hasBoosters && boosterFireOutTime == -1L && FM.Gears.onGround() && FM.EI.getPowerOutput() > 0.8F && FM.getSpeedKMH() > 80F)
            {
                boosterFireOutTime = Time.current() + 6000L;
                doFireBoosters();
                FM.AS.setGliderBoostOn();
            }
            if(hasBoosters && boosterFireOutTime > 0L)
            {
                if(Time.current() < boosterFireOutTime)
                    FM.producedAF.x += 35000D;
                if(Time.current() > boosterFireOutTime + 6000L)
                {
                    doCutBoosters();
                    FM.AS.setGliderBoostOff();
                }
                if(Time.current() > boosterFireOutTime + 30000L && Ratos == 1)
                {
                    hierMesh().chunkVisible("RatoR", false);
                    doRemoveRatoR();
                    Ratos = 2;
                }
                if(Time.current() > boosterFireOutTime + 31000L)
                {
                    hierMesh().chunkVisible("RatoL", false);
                    doRemoveRatoL();
                    hasBoosters = false;
                    APmode5 = false;
                }
            }
        }
        super.update(f);
        if(APmode6 && ExtFuel > 0.0F)
        {
            ExtFuel = ExtFuel - 9F * f;
            this.FM.M.fuel += 9F * f;
            if(this.FM.M.fuel >= this.FM.M.maxFuel)
                ExtFuel = 0.0F;
        }
        if(hasBuddy && WingOn && !this.FM.CT.bHasAileronControl && this.FM.Gears.nOfGearsOnGr == 0)
        {
            hierMesh().chunkVisible("UPAZDrogueOut", false);
            hierMesh().chunkVisible("UPAZDrogueIn", false);
            WingOn = false;
        }
        if(hasBuddy && ((TypeDockable)((Interpolate) (FM)).actor).typeDockableIsDocked())
        {
            for(int i = 0; i < drones.length; i++)
                if(Actor.isValid(drones[i]))
                {
                    if(hasDroptanks)
                    {
                        if(PumpedFuel <= 0.0F)
                            checkExtFuel(0);
                        PumpedFuel += 15F * f;
                        if(PumpedFuel >= ExtFuel)
                        {
                            this.FM.CT.dropFuelTanks();
                            hasDroptanks = false;
                        }
                    }
                    if(!hasDroptanks)
                        this.FM.M.fuel -= 15F * f;
                    if(!FuelOn)
                    {
                        GreenSignal = Eff3DActor.New(this, findHook("_GreenSignal"), null, 1.0F, "3do/Effects/P85/P85_SignalGreen.eff", -1F);
                        FuelOn = true;
                    }
                    if(!FuelLow && this.FM.M.fuel < 600F)
                    {
                        Eff3DActor.finish(GreenSignal);
                        YellowSignal = Eff3DActor.New(this, findHook("_YellowSignal"), null, 1.0F, "3do/Effects/P85/P85_SignalYellow.eff", -1F);
                        FuelLow = true;
                    }
                    if(!WaveOff && this.FM.M.fuel < 400F)
                    {
                        Eff3DActor.finish(YellowSignal);
                        RedSignal = Eff3DActor.New(this, findHook("_RedSignal"), null, 1.0F, "3do/Effects/P85/P85_SignalRed.eff", -1F);
                        WaveOff = true;
                    }
                    if(!FuelStop && this.FM.M.fuel < 300F)
                    {
                        ((TypeDockable)((Interpolate) (FM)).actor).typeDockableAttemptDetach();
                        Eff3DActor.finish(RedSignal);
                        FuelStop = true;
                    }
                    if(!DrogueCtrl && this.FM.getAltitude() < 1000F)
                    {
                        ((TypeDockable)((Interpolate) (FM)).actor).typeDockableAttemptDetach();
                        Eff3DActor.finish(GreenSignal);
                        Eff3DActor.finish(YellowSignal);
                        Eff3DActor.finish(RedSignal);
                    }
                }

        }
        if(!hasBuddy)
        {
            if(bNeedSetup)
                checkAsDrone();
            int j = aircIndex();
            if(this.FM instanceof Maneuver)
                if(typeDockableIsDocked())
                {
                    if(!(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode())
                    {
                        ((Maneuver)this.FM).unblock();
                        ((Maneuver)this.FM).set_maneuver(48);
                        for(int k = 0; k < j; k++)
                            ((Maneuver)this.FM).push(48);

                        if(this.FM.AP.way.curr().Action != 3)
                            ((FlightModelMain) ((Maneuver)this.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).AP.way.Cur());
                        ((Pilot)this.FM).setDumbTime(3000L);
                    }
                    if(this.FM.M.fuel > 1193F)
                    {
                        HUD.logCenter("Receiver: Tanks full");
                        ((TypeDockable)((Interpolate) (FM)).actor).typeDockableAttemptDetach();
                    }
                    if(this.FM.M.fuel < this.FM.M.maxFuel)
                    {
                        this.FM.M.fuel += 15F * f;
                        if(!ProbeOut)
                        {
                            moveRefuel(90F);
                            ProbeOut = true;
                        }
                    }
                } else
                if(!(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode())
                {
                    if(this.FM.CT.GearControl == 0.0F && this.FM.EI.engines[0].getStage() == 0)
                        this.FM.EI.setEngineRunning();
                    if(dtime > 0L && ((Maneuver)this.FM).Group != null)
                    {
                        ((Maneuver)this.FM).Group.leaderGroup = null;
                        ((Maneuver)this.FM).set_maneuver(22);
                        ((Pilot)this.FM).setDumbTime(3000L);
                        if(Time.current() > dtime + 3000L)
                        {
                            dtime = -1L;
                            ((Maneuver)this.FM).clear_stack();
                            ((Maneuver)this.FM).set_maneuver(0);
                            ((Pilot)this.FM).setDumbTime(0L);
                        }
                    } else
                    if(this.FM.AP.way.curr().Action == 0)
                    {
                        Maneuver maneuver = (Maneuver)this.FM;
                        if(maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                            maneuver.Group.setGroupTask(2);
                    }
                }
        }
        if(hasDroptanks && !this.FM.CT.Weapons[9][1].haveBullets())
        {
            ExtFuel = 0.0F;
            hasDroptanks = false;
            if(!hasBuddy)
                FlapAngle = 55F;
            if(hasBuddy)
                FlapAngle = 40F;
        }
    }

    private Actor drones[];
    private float FlapAngle;
    private boolean hasDroptanks;
    private boolean hasBuddy;
    private boolean WingOn;
    protected boolean hasBoosters;
    private int Ratos;
    protected long boosterFireOutTime;
    private Eff3DActor RatoL;
    private Eff3DActor RatoR;
    private Eff3DActor RatoLsmk;
    private Eff3DActor RatoRsmk;
    private boolean FuelOn;
    private boolean FuelLow;
    private boolean WaveOff;
    private boolean FuelStop;
    private float DockSpeed;
    private double DockDistance;
    private Eff3DActor GreenSignal;
    private Eff3DActor YellowSignal;
    private Eff3DActor RedSignal;
    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private long dtime;
    private Actor target_;
    private Actor queen_;
    private int dockport_;
    private boolean ProbeOut;
    private boolean bDynamoOperational;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int pk;
    public static boolean bChangedPit = false;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private long lTimeNextEject;
    public boolean APmode5;
    public float ExtFuel;
    public boolean APmode6;
    public boolean APmode7;
    public float PumpedFuel;
    private boolean DrogueCtrl;

    static 
    {
        Class class1 = Mig_15KRTz.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-15KRTz");
        Property.set(class1, "meshName", "3DO/Plane/MiG-15KRTz(Multi1)/Hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1955F);
        Property.set(class1, "yearExpired", 1968.5F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-15KRTz.fmd:MiG-15KRTz_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitMig_15KRTz.class, CockpitP85_PN2.class
        });
        Property.set(class1, "LOSElevation", 1.0585F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 1, 9, 9, 9, 9, 9, 3, 3, 3, 
            3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExtTank01", "_ExtTank02", "_ExtTank03", "_ExtTank04", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", 
            "_ExternalBomb02"
        });
    }

    public float requestRefuel(Aircraft paramAircraft, float paramFloat1, float paramFloat2) {
        // TODO Auto-generated method stub
        return 0;
        
        
        // This is what that method looks like on a Skyhawk Tanker:
        /*
        if(bDrogueExtended && FM.AS.isMaster())
        {
            for(int i = 0; i < drones.length; i++)
            {
                if(!Actor.isValid(drones[i]) || drones[i] != aircraft)
                    continue;
                if(f > maxSendRefuel)
                    f = maxSendRefuel;
                if(FM.M.requestFuel(f * f1))
                    return f * f1;
            }

        }
        return 0.0F;
        */

    }
}
