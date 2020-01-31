// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 30.11.2019 11:49:34
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   Tow_Targets.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Way;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Interpolate;
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
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
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

// Referenced classes of package com.maddox.il2.objects.air:
//            ZOK_Drones, Chute, Aircraft, TypeDockable, 
//            PaintSchemeFMPar05, NetAircraft

public class Tow_Targets extends com.maddox.il2.objects.air.ZOK_Drones
    implements com.maddox.il2.objects.air.TypeDockable
{

    public Tow_Targets()
    {
        TargetReleased = true;
        TargetType = 0;
        DmgLevel = 0;
        OnChute = false;
    }

    public void onAircraftLoaded()
    {
        FM.AS.bIsEnableToBailout = false;
        FM.Gears.setHydroOperable(false);
        FM.Gears.setOperable(false);
        FM.CT.bHasBrakeControl = false;
        FM.setCapableOfTaxiing(false);
        FM.setCapableOfACM(false);
        ((com.maddox.il2.ai.air.Pilot)FM).silence = true;
        hierMesh().chunkVisible("Display", false);
        if(super.thisWeaponsName.startsWith("de") || super.thisWeaponsName.startsWith("01"))
        {
            hierMesh().chunkVisible("Banner", true);
            hierMesh().chunkVisible("HitBoxB", true);
            TargetType = 1;
        }
        if(super.thisWeaponsName.startsWith("02"))
        {
            hierMesh().chunkVisible("Reflector", true);
            hierMesh().chunkVisible("ParaCap", true);
            hierMesh().chunkVisible("HitBoxR", true);
        }
    }

    public void deploychute()
    {
        hierMesh().chunkVisible("ParaCap", false);
        chute = new Chute(this);
        chute.setMesh("3do/Arms/P85-ParaBrake/mono.sim");
        chute.mesh().setScale(0.6F);
        chute.collide(false);
        ((com.maddox.il2.engine.Actor) (chute)).pos.setRel(new Point3d(-0.42999999999999999D, 0.0D, -0.035000000000000003D), new Orient(0.0F, 90F, 0.0F));
        OnChute = true;
    }

    protected void hitBone(java.lang.String s, com.maddox.il2.ai.Shot shot, com.maddox.JGP.Point3d point3d)
    {
        if(s.startsWith("xkeel"))
        {
            if(!TargetReleased)
                com.maddox.il2.game.HUD.logCenter("Aerial target hit");
            if(DmgLevel == 0)
                DmgLevel = 1;
            if(DmgLevel == 1 && TargetType == 0 || TargetType == 2)
            {
                hierMesh().chunkVisible("ReflectorDmg", true);
                hierMesh().chunkVisible("Reflector", false);
                DmgLevel = 2;
            }
            if(DmgLevel == 1 && TargetType == 1 || TargetType == 3)
            {
                hierMesh().chunkVisible("BannerDmg", true);
                hierMesh().chunkVisible("Banner", false);
                DmgLevel = 2;
            }
        }
    }

    public void destroy()
    {
        if(!TargetReleased)
            com.maddox.il2.game.HUD.logCenter("Aerial target lost");
        super.destroy();
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if((super.FM instanceof com.maddox.il2.fm.RealFlightModel) && ((com.maddox.il2.fm.RealFlightModel)super.FM).isRealMode() || !flag || !(super.FM instanceof com.maddox.il2.ai.air.Pilot))
            return;
        if(flag && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AP.way.curr().Action == 3 && typeDockableIsDocked() && java.lang.Math.abs(((com.maddox.il2.fm.FlightModelMain) (((com.maddox.il2.objects.sounds.SndAircraft) ((com.maddox.il2.objects.air.Aircraft)queen_)).FM)).Or.getKren()) < 3F)
            if(super.FM.isPlayers())
            {
                if((super.FM instanceof com.maddox.il2.fm.RealFlightModel) && !((com.maddox.il2.fm.RealFlightModel)super.FM).isRealMode())
                {
                    typeDockableAttemptDetach();
                    ((com.maddox.il2.ai.air.Maneuver)super.FM).set_maneuver(22);
                    ((com.maddox.il2.ai.air.Maneuver)super.FM).setCheckStrike(false);
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).Vwld.z -= 5D;
                    dtime = com.maddox.rts.Time.current();
                }
            } else
            {
                typeDockableAttemptDetach();
                ((com.maddox.il2.ai.air.Maneuver)super.FM).set_maneuver(22);
                ((com.maddox.il2.ai.air.Maneuver)super.FM).setCheckStrike(false);
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).Vwld.z -= 5D;
                dtime = com.maddox.rts.Time.current();
            }
    }

    public void update(float f)
    {
        if(TargetType == 0 && !TargetReleased && com.maddox.il2.fm.Pitot.Indicator((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.FM)).Loc)).z, super.FM.getSpeedKMH()) > 900F)
        {
            com.maddox.il2.game.HUD.logCenter("Target overspeed: Cable weaklink failed");
            TargetType = 2;
            ((com.maddox.il2.objects.air.TypeDockable)((com.maddox.il2.engine.Interpolate) (FM)).actor).typeDockableAttemptDetach();
        }
        if(TargetType == 1 && !TargetReleased && com.maddox.il2.fm.Pitot.Indicator((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.FM)).Loc)).z, super.FM.getSpeedKMH()) > 500F)
        {
            com.maddox.il2.game.HUD.logCenter("Target overspeed: Cable weaklink failed");
            TargetType = 3;
            ((com.maddox.il2.objects.air.TypeDockable)((com.maddox.il2.engine.Interpolate) (FM)).actor).typeDockableAttemptDetach();
        }
        if(TargetReleased && TargetType == 0 || TargetType == 2)
        {
            if(!OnChute)
                deploychute();
            getSpeed(v3d);
            v3d.scale(0.97499999999999998D);
            if(java.lang.Math.abs(((com.maddox.JGP.Tuple3d) (v3d)).x) < 9.9999997473787516E-006D)
                v3d.x = 9.9999997473787516E-006D;
            if(java.lang.Math.abs(((com.maddox.JGP.Tuple3d) (v3d)).y) < 9.9999997473787516E-006D)
                v3d.y = 9.9999997473787516E-006D;
            if(v3d.z < -2D)
                v3d.z += 1.1F * com.maddox.rts.Time.tickConstLenFs();
            setSpeed(v3d);
        }
        if(TargetReleased && TargetType == 1 || TargetType == 3)
        {
            getSpeed(v3d);
            v3d.scale(0.99199999999999999D);
            if(v3d.z < -2D)
                v3d.z += 1.1F * com.maddox.rts.Time.tickConstLenFs();
            setSpeed(v3d);
        }
        if(TargetReleased && super.FM.getAltitude() < 500F)
        {
            hierMesh().chunkVisible("HitBoxB", false);
            hierMesh().chunkVisible("HitBoxR", false);
        }
        if(TargetReleased && super.FM.Gears.onGround() && TargetType == 0 || TargetType == 2)
            chute.destroy();
        super.update(f);
        if(bNeedSetup)
            checkAsDrone();
        int i = aircIndex();
        if(super.FM instanceof com.maddox.il2.ai.air.Maneuver)
            if(typeDockableIsDocked())
            {
                if(!(super.FM instanceof com.maddox.il2.fm.RealFlightModel) || !((com.maddox.il2.fm.RealFlightModel)super.FM).isRealMode())
                {
                    ((com.maddox.il2.ai.air.Maneuver)super.FM).unblock();
                    ((com.maddox.il2.ai.air.Maneuver)super.FM).set_maneuver(48);
                    for(int j = 0; j < i; j++)
                        ((com.maddox.il2.ai.air.Maneuver)super.FM).push(48);

                    if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AP.way.curr().Action != 3)
                        ((com.maddox.il2.fm.FlightModelMain) ((com.maddox.il2.ai.air.Maneuver)super.FM)).AP.way.setCur(((com.maddox.il2.fm.FlightModelMain) (((com.maddox.il2.objects.sounds.SndAircraft) ((com.maddox.il2.objects.air.Aircraft)queen_)).FM)).AP.way.Cur());
                    ((com.maddox.il2.ai.air.Pilot)super.FM).setDumbTime(3000L);
                }
            } else
            if(!(super.FM instanceof com.maddox.il2.fm.RealFlightModel) || !((com.maddox.il2.fm.RealFlightModel)super.FM).isRealMode())
            {
                if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.GearControl == 0.0F && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getStage() == 0)
                    ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.setEngineRunning();
                if(dtime > 0L && ((com.maddox.il2.ai.air.Maneuver)super.FM).Group != null)
                {
                    ((com.maddox.il2.ai.air.Maneuver)super.FM).Group.leaderGroup = null;
                    ((com.maddox.il2.ai.air.Maneuver)super.FM).set_maneuver(22);
                    ((com.maddox.il2.ai.air.Pilot)super.FM).setDumbTime(3000L);
                    if(com.maddox.rts.Time.current() > dtime + 3000L)
                    {
                        dtime = -1L;
                        ((com.maddox.il2.ai.air.Maneuver)super.FM).clear_stack();
                        ((com.maddox.il2.ai.air.Maneuver)super.FM).set_maneuver(0);
                        ((com.maddox.il2.ai.air.Pilot)super.FM).setDumbTime(0L);
                    }
                } else
                if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).AP.way.curr().Action == 0)
                {
                    com.maddox.il2.ai.air.Maneuver maneuver = (com.maddox.il2.ai.air.Maneuver)super.FM;
                    if(maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                        maneuver.Group.setGroupTask(2);
                }
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
        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.setEngineRunning();
        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.setGearAirborne();
        moveGear(0.0F);
        com.maddox.il2.fm.FlightModel flightmodel = ((com.maddox.il2.objects.sounds.SndAircraft) ((com.maddox.il2.objects.air.Aircraft)queen_)).FM;
        TargetReleased = false;
        if(TargetType == 0)
            hierMesh().chunkVisible("WinchCableR", true);
        if(TargetType == 1)
            hierMesh().chunkVisible("WinchCableB", true);
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
            FM.CT.bHasAileronControl = false;
            FM.CT.bHasRudderControl = false;
            FM.CT.bHasElevatorControl = false;
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].setEngineDies(this);
            hierMesh().chunkVisible("WinchCableR", false);
            hierMesh().chunkVisible("WinchCableB", false);
            if(TargetType < 2)
                com.maddox.il2.game.HUD.logCenter("Target released");
            if(TargetType == 0 || TargetType == 2)
                deploychute();
            TargetReleased = true;
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


    private boolean bNeedSetup;
    private long dtime;
    private com.maddox.il2.engine.Actor queen_last;
    private long queen_time;
    private com.maddox.il2.engine.Actor target_;
    private com.maddox.il2.engine.Actor queen_;
    private int dockport_;
    private boolean TargetReleased;
    private int TargetType;
    private int DmgLevel;
    private com.maddox.il2.objects.air.Chute chute;
    private boolean OnChute;
    private static com.maddox.JGP.Vector3d v3d = new Vector3d();

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.Tow_Targets.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Aerial Target");
        com.maddox.rts.Property.set(class1, "meshName", "3do/Plane/Tow-Targets(Multi1)/Hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        com.maddox.rts.Property.set(class1, "yearService", 1949F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1969F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Tow-Targets.fmd:Tow-Targets_FM");
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_Clip00"
        });
    }
}