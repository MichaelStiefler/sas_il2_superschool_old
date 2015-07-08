// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 16/06/2015 00:14:14
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   F_16B_B05.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.reflect.Field;
import com.maddox.sas1946.il2.util.Reflection;



// Referenced classes of package com.maddox.il2.objects.air:
//            F_16, Aircraft, Chute, TypeTankerDrogue,
//            TypeDockable, PaintSchemeFMPar06, TypeGuidedMissileCarrier, TypeCountermeasure,
//            TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump,
//            NetAircraft

public class F_16B_B05 extends F_16
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, TypeDockable
{

    public F_16B_B05()
    {
        lastCommonThreatActive = 0L;
        intervalCommonThreat = 1000L;
        lastRadarLockThreatActive = 0L;
        intervalRadarLockThreat = 1000L;
        lastMissileLaunchThreatActive = 0L;
        intervalMissileLaunchThreat = 1000L;
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        lastCommonThreatActive = 0L;
        intervalCommonThreat = 1000L;
        lastRadarLockThreatActive = 0L;
        intervalRadarLockThreat = 1000L;
        lastMissileLaunchThreatActive = 0L;
        intervalMissileLaunchThreat = 1000L;
        guidedMissileUtils = new GuidedMissileUtils(this);
        removeChuteTimer = -1L;
        trgtlongAI = null;
        missilesList = new ArrayList();
        hasIR = false;
        hasPRHM = false;
        a2a = false;
        IR = false;
        tX4Prev = 0L;
        backfireList = new ArrayList();
        backfire = false;
        thrustMaxField = new Field[2];
        thrustMaxFromEmd = new float[2];
    }

    public float getFlowRate()
    {
        return FlowRate;
    }

    public float getFuelReserve()
    {
        return FuelReserve;
    }

    public long getChaffDeployed()
    {
        if(hasChaff)
            return lastChaffDeployed;
        else
            return 0L;
    }

    public long getFlareDeployed()
    {
        if(hasFlare)
            return lastFlareDeployed;
        else
            return 0L;
    }

    public void setCommonThreatActive()
    {
        long l = Time.current();
        if(l - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = l;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long l = Time.current();
        if(l - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = l;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long l = Time.current();
        if(l - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
        {
            lastMissileLaunchThreatActive = l;
            doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat()
    {
    }

    private void doDealRadarLockThreat()
    {
    }

    private void doDealMissileLaunchThreat()
    {
    }

    public void backFire()
    {
        if(backfireList.isEmpty())
        {
            return;
        } else
        {
            ((RocketGunFlare)backfireList.remove(0)).shots(3);
            return;
        }
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(3.5F, 1.5F, 2.5F, 8.2F, 3F, 3.5F);
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        FM.Skill = 3;
        droptank();
        ((FlightModelMain) (FM)).CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
    }

    private void droptank()
    {
        for(int i = 0; i < ((FlightModelMain) (FM)).CT.Weapons.length; i++)
            if(((FlightModelMain) (FM)).CT.Weapons[i] != null)
            {
                for(int j = 0; j < ((FlightModelMain) (FM)).CT.Weapons[i].length; j++)
                    if(((FlightModelMain) (FM)).CT.Weapons[i][j].haveBullets())
                    {
                        if(((FlightModelMain) (FM)).CT.Weapons[i][j] instanceof RocketGunAIM9L)
                            hasIR = true;
                        if(((FlightModelMain) (FM)).CT.Weapons[i][j] instanceof RocketGunAIM120A)
                            hasPRHM = true;
                        if(((FlightModelMain) (FM)).CT.Weapons[i][j] instanceof RocketGunFlare)
                        {
                            backfire = true;
                            backfireList.add(((FlightModelMain) (FM)).CT.Weapons[i][j]);
                        }
                    }

            }

    }



    public void launchMsl()
    {
        if(missilesList.isEmpty())
        {
            return;
        } else
        {
            ((RocketGunAGM65L)missilesList.remove(0)).shots(1);
            return;
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
    }

    public void update(float f)
    {
        if(bNeedSetup)
            checkAsDrone();
        guidedMissileUtils.update();
        this.computePW_AB();
        this.computeLift();
        int i = aircIndex();
        if(FM instanceof Maneuver)
            if(typeDockableIsDocked())
            {
                if(!(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode())
                {
                    ((Maneuver)FM).unblock();
                    ((Maneuver)FM).set_maneuver(48);
                    for(int j = 0; j < i; j++)
                        ((Maneuver)FM).push(48);

                    if(((FlightModelMain) (FM)).AP.way.curr().Action != 3)
                        ((FlightModelMain) ((Maneuver)FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).AP.way.Cur());
                    ((Pilot)FM).setDumbTime(3000L);
                }
                if(((FlightModelMain) (FM)).M.fuel < ((FlightModelMain) (FM)).M.maxFuel)
                    ((FlightModelMain) (FM)).M.fuel += 20F * f;
            } else
            if(!(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode())
            {
                if(((FlightModelMain) (FM)).CT.GearControl == 0.0F && ((FlightModelMain) (FM)).EI.engines[0].getStage() == 0)
                    ((FlightModelMain) (FM)).EI.setEngineRunning();
                if(dtime > 0L && ((Maneuver)FM).Group != null)
                {
                    ((Maneuver)FM).Group.leaderGroup = null;
                    ((Maneuver)FM).set_maneuver(22);
                    ((Pilot)FM).setDumbTime(3000L);
                    if(Time.current() > dtime + 3000L)
                    {
                        dtime = -1L;
                        ((Maneuver)FM).clear_stack();
                        ((Maneuver)FM).set_maneuver(0);
                        ((Pilot)FM).setDumbTime(0L);
                    }
                } else
                if(((FlightModelMain) (FM)).AP.way.curr().Action == 0)
                {
                    Maneuver maneuver = (Maneuver)FM;
                    if(maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                        maneuver.Group.setGroupTask(2);
                }
            }
        super.update(f);
        if(((FlightModelMain) (FM)).CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteF86/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.5F);
            ((Actor) (chute)).pos.setRel(new Point3d(-5D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && ((FlightModelMain) (FM)).CT.bHasDragChuteControl)
            if(((FlightModelMain) (FM)).CT.DragChuteControl == 1.0F && FM.getSpeedKMH() > 600F || ((FlightModelMain) (FM)).CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    ((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                ((FlightModelMain) (FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (FM)).CT.bHasDragChuteControl = false;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(((FlightModelMain) (FM)).CT.DragChuteControl == 1.0F && FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                ((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
                ((FlightModelMain) (FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (FM)).CT.bHasDragChuteControl = false;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !((FlightModelMain) (FM)).CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
            chute.destroy();
        AIgroundattack();
    }


    public void AIgroundattack()
    {
        if((!(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode()) && (FM instanceof Maneuver) && ((Maneuver)FM).get_task() == 7 && !((FlightModelMain) (FM)).AP.way.isLanding())
            if(missilesList.isEmpty() && !((Maneuver)FM).hasBombs())
            {
                Pilot pilot = (Pilot)FM;
                Vector3d vector3d = new Vector3d();
                getSpeed(vector3d);
                Point3d point3d1 = new Point3d();
                super.pos.getAbs(point3d1);
                float f = (float)((double)FM.getAltitude() - World.land().HQ(((Tuple3d) (point3d1)).x, ((Tuple3d) (point3d1)).y));
                if(f < 55F && ((Tuple3d) (vector3d)).z < 0.0D)
                    vector3d.z = 0.0D;
                else
                if(pilot != null && Actor.isAlive(((Maneuver) (pilot)).target_ground))
                {
                    Point3d point3d2 = new Point3d();
                    ((Maneuver) (pilot)).target_ground.pos.getAbs(point3d2);
                    pilot.set_maneuver(43);
                    if(super.pos.getAbsPoint().distance(point3d2) < 2000D)
                    {
                        point3d2.sub(((FlightModelMain) (FM)).Loc);
                        ((FlightModelMain) (FM)).Or.transformInv(point3d2);
                        ((FlightModelMain) (FM)).CT.PowerControl = 0.55F;
                    }
                }
                setSpeed(vector3d);
            } else
            if(!missilesList.isEmpty() && Time.current() > tX4Prev + 500L + (IR ? 10000L : 0L))
            {
                Pilot pilot1 = (Pilot)FM;
                if(pilot1.get_maneuver() == 43 && ((Maneuver) (pilot1)).target_ground != null)
                {
                    Point3d point3d = new Point3d();
                    ((Maneuver) (pilot1)).target_ground.pos.getAbs(point3d);
                    point3d.sub(((FlightModelMain) (FM)).Loc);
                    ((FlightModelMain) (FM)).Or.transformInv(point3d);
                    if(((Tuple3d) (point3d)).x > 1000D && ((Tuple3d) (point3d)).x < (IR ? 2250D : 1250D) + 250D * (double)((FlightModelMain) (FM)).Skill)
                    {
                        if(!IR)
                            point3d.x /= 2 - ((FlightModelMain) (FM)).Skill / 3;
                        if(((Tuple3d) (point3d)).y < ((Tuple3d) (point3d)).x && ((Tuple3d) (point3d)).y > -((Tuple3d) (point3d)).x && ((Tuple3d) (point3d)).z * 1.5D < ((Tuple3d) (point3d)).x && ((Tuple3d) (point3d)).z * 1.5D > -((Tuple3d) (point3d)).x)
                        {
                            launchMsl();
                            tX4Prev = Time.current();
                            Voice.speakAttackByRockets(this);
                        }
                    }
                }
            }
    }

    public void computePW_AB()
       {
        if(((FlightModelMain) (FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (FM)).EI.engines[0].getStage() > 5)
           ((FlightModelMain) (FM)).producedAF.x += 34930D;
        float x = FM.getAltitude() / 1000F;
        float thrustDegradation = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6)
        if (x > 17.3)
	{
	thrustDegradation = 33.0F;
	} else{
		float x2 = x * x;
		float x3 = x2 * x;
		float x4 = x3 * x;
		float x5 = x4 * x;
		thrustDegradation = 0.0020439F*x4 - 0.048507F * x3 + 0.353167F * x2 - 0.267366F * x;
		//{{0,0},{4, 2},{8, 4},{12.5,7.0},{17.3,33.0}}
	}
	FM.producedAF.x -=  thrustDegradation * 1000F;
         }

    public void computeLift()
       {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float x = this.calculateMach();
        if(this.calculateMach() >= 0.0F);
        float Lift = 0.0F;
        if((double)x > 2.25F)
        {
            Lift = 0.12F;
        } else
        {
            float x2 = x * x;
            float x3 = x2 * x;
            float x4 = x3 * x;
            float x5 = x4 * x;
            float x6 = x5 * x;
            float x7 = x6 * x;
            float x8 = x7 * x;
            float x9 = x8 * x;
            Lift= -0.0474888F*x8 + 0.497163F*x7 - 2.10501F*x6 + 4.57592F*x5 - 5.35342F*x4 + 3.2778F*x3 - 1.06401F*x2 + 0.216849F*x + 0.078F;
           // {{0.0,0.078},{0.25, 0.1}, {0.6, 0.11},{0.97, 0.08}, {1.3, 0.035},{1.68, 0.013}, {2.0, 0.012}, {2.2, 0.011}, {2.3, 0.010}}
            }
        polares.lineCyCoeff= Lift;
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
        if(target_ == null)
        {
            if(((FlightModelMain) (FM)).AP.way.curr().getTarget() == null)
                ((FlightModelMain) (FM)).AP.way.next();
            target_ = ((FlightModelMain) (FM)).AP.way.curr().getTarget();
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

    public boolean typeDockableIsDocked()
    {
        return Actor.isValid(queen_);
    }

    public void typeDockableAttemptAttach()
    {
        if(((FlightModelMain) (FM)).AS.isMaster() && !typeDockableIsDocked())
        {
            Aircraft aircraft = War.getNearestFriend(this);
            if(aircraft instanceof TypeTankerDrogue)
            {
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
                ((FlightModelMain) (FM)).CT.RefuelControl = 1.0F;
            } else
            {
                ((FlightModelMain) (FM)).CT.RefuelControl = 0.0F;
            }
        }
    }

    public void typeDockableAttemptDetach()
    {
        if(((FlightModelMain) (FM)).AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
            ((TypeDockable)queen_).typeDockableRequestDetach(this);
    }

    public void typeDockableRequestAttach(Actor actor)
    {
    }

    public void typeDockableRequestDetach(Actor actor)
    {
    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag)
    {
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag)
    {
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i)
    {
    }

    public void typeDockableDoDetachFromDrone(int i)
    {
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i)
    {
        queen_ = actor;
        dockport_ = i;
        queen_last = queen_;
        queen_time = 0L;
        ((FlightModelMain) (FM)).EI.setEngineRunning();
        ((FlightModelMain) (FM)).CT.setGearAirborne();
        moveGear(0.0F);
        FlightModel flightmodel = ((SndAircraft) ((Aircraft)queen_)).FM;
        if(aircIndex() == 0 && (FM instanceof Maneuver) && (flightmodel instanceof Maneuver))
        {
            Maneuver maneuver = (Maneuver)flightmodel;
            Maneuver maneuver1 = (Maneuver)FM;
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

    public void typeDockableDoDetachFromQueen(int i)
    {
        if(dockport_ == i)
        {
            queen_last = queen_;
            queen_time = Time.current();
            queen_ = null;
            dockport_ = 0;
        }
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
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
        if(netmsginput.readByte() == 1)
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


    private boolean HasAGM;
    private long tfire;
    public BulletEmitter Weapons[][];
    private boolean hasIR;
    private boolean hasPRHM;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private long lastCommonThreatActive;
    private long intervalCommonThreat;
    private long lastRadarLockThreatActive;
    private long intervalRadarLockThreat;
    private long lastMissileLaunchThreatActive;
    private long intervalMissileLaunchThreat;
    public static float FlowRate = 10F;
    public static float FuelReserve = 1500F;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
    public boolean bToFire;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    private boolean havedroptank;
    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private long dtime;
    private Actor target_;
    private Actor queen_;
    private int dockport_;
    private ArrayList missilesList;
    private ArrayList backfireList;
    private Actor trgtlongAI;
    private long tX4Prev;
    private boolean a2a;
    private boolean IR;
    private boolean backfire;
    private float thrustMaxFromEmd[];
    private Field thrustMaxField[];


    static
    {
        Class class1 = com.maddox.il2.objects.air.F_16B_B05.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Viper");
        Property.set(class1, "meshName", "3DO/Plane/F-16B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1973.3F);
        Property.set(class1, "yearExpired", 1999.3F);
        Property.set(class1, "FlightModel", "FlightModels/F-16A_B10.fmd:f16_fm");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitF_16A_B10.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 9, 9, 9, 9, 2, 2,
            2, 2, 2, 2, 2, 2, 9, 9, 9, 9,
            3, 3, 3, 3, 2, 2, 2, 2, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            9, 9, 9, 9, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 9, 9,
            9, 9, 9, 9, 2, 2, 2, 2, 2, 2,
            2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02",
            "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08",
            "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalBomb05", "_ExternalBomb06",
            "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16",
            "_ExternalDev09", "_Dev10", "_ExternalDev11", "_Dev12", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22",
            "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalBomb27", "_ExternalBomb28","_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16",
            "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalDev13", "_ExternalDev14",
            "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30",
            "_ExternalRock31", "_ExternalRock32"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            char c = '\221';
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[c];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9+2x370DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+2x370DT+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+2xCBU87+2x370DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+2xMk83bomb+2x370DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+2xMk84bomb+2x370DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xMk82bomb";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xMk82bomb+2x370DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xMk82SnakeEye";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xMk82SnakeEye+2x370DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xCBU87";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xCBU87+2x370DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+12xMk82bomb";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+12xMk82bomb+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+12xMk82SnakeEye";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xMk82SnakeEye+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+18xMk82bomb";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+18xMk82bomb+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+14xMk82SnakeEye";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+14xMk82SnakeEye+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+14xCBU87";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+14xCBU87+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+4xMk83bomb";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+4xMk83bomb+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+4xMk84bomb";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+4xMk84bomb+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xAGM65B";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xAGM65B+2x370DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xAGM65B+2x370DT+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xAGM65B+6xMk82bomb+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xAGM65B+6xMk82SnakeEye+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xAGM65B+8xCBU87+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xAGM65B+12xMk82bomb+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xLAU3+2x370DT+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+3xLAU3+3xAGM65B+2x370DT+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(4, "RocketGunHYDRA", 19);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(4, "RocketGunHYDRA", 19);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(4, "RocketGunHYDRA", 19);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xLAU3+6xMk82bomb+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xLAU3+12xMk82bomb+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xLAU3+6xMk82SnakeEye+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+6xLAU3+8xCBU87+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+3xLAU3+3xAGM65B+12xMk82bomb+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(4, "RocketGunHYDRA", 19);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(4, "RocketGunHYDRA", 19);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(4, "RocketGunHYDRA", 19);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9+3xLAU3+3xAGM65B+10xCBU87+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(4, "RocketGunHYDRA", 19);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(4, "RocketGunHYDRA", 19);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(4, "RocketGunHYDRA", 19);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC+4xAIM9+2x370DT+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunWPFAC", 19);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunWPFAC", 19);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FO+4xAIM9+2x370DT+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Wtank", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunWPFO", 19);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunWPFO", 19);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC+4xAIM9+6xMk82bomb+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunWPFAC", 19);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunWPFAC", 19);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FO+4xAIM9+6xMk82bomb+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunWPFO", 19);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunWPFO", 19);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FO+FAC+4xAIM9+12xMk82bomb+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(4, "RocketGunWPFAC", 19);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunWPFO", 19);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC+4xAIM9+8xCBU87+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunWPFAC", 19);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunWPFAC", 19);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FO+4xAIM9+8xCBU87+300DT";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 950);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_F16Ctank", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "Pylon_LAU61", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunWPFO", 19);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunWPFO", 19);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}
