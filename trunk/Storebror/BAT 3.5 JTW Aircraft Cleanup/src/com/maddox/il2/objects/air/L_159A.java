package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.RocketBombGun;
import com.maddox.il2.objects.weapons.RocketGunAGM65Ds;
import com.maddox.il2.objects.weapons.RocketGunAGM65L;
import com.maddox.il2.objects.weapons.RocketGunAIM120A;
import com.maddox.il2.objects.weapons.RocketGunAIM9L;
import com.maddox.il2.objects.weapons.RocketGunFlare;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class L_159A extends L39A
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFighter, TypeStormovik
{

    public L_159A()
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
    }

    private void checkAmmo()
    {
        missilesList.clear();
        for(int i = 0; i < this.FM.CT.Weapons.length; i++)
            if(this.FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < this.FM.CT.Weapons[i].length; j++)
                    if(this.FM.CT.Weapons[i][j].haveBullets())
                    {
                        if(this.FM.CT.Weapons[i][j] instanceof RocketGunAGM65Ds)
                            IR = true;
                        if(this.FM.CT.Weapons[i][j] instanceof RocketGunFlare)
                            backfireList.add(this.FM.CT.Weapons[i][j]);
                        else
                            missilesList.add(this.FM.CT.Weapons[i][j]);
                        if(this.FM.CT.Weapons[i][j] instanceof RocketBombGun)
                            IR = true;
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

    public void launchbmb()
    {
        if(missilesList.isEmpty())
        {
            return;
        } else
        {
            ((RocketBombGun)missilesList.remove(0)).shots(1);
            return;
        }
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
        if(this.thisWeaponsName.startsWith("FLIR +"))
        {
            hierMesh().chunkVisible("FLIRPOD", true);
            this.FM.Sq.dragParasiteCx += 0.002F;
        }
        this.FM.CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        if(Config.isUSE_RENDER())
            turbo = Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
        turbosmoke = Eff3DActor.New(this, findHook("_Engine1ES_01"), null, 1.0F, "3DO/Effects/Aircraft/GraySmallTSPD.eff", -1F);
        afterburner = Eff3DActor.New(this, findHook("_Engine1EF_02"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurner.eff", -1F);
        Eff3DActor.setIntesity(turbo, 0.0F);
        Eff3DActor.setIntesity(turbosmoke, 0.0F);
        Eff3DActor.setIntesity(afterburner, 0.0F);
        guidedMissileUtils.onAircraftLoaded();
    }

    private void droptank()
    {
        for(int i = 0; i < this.FM.CT.Weapons.length; i++)
            if(this.FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < this.FM.CT.Weapons[i].length; j++)
                    if(this.FM.CT.Weapons[i][j].haveBullets())
                    {
                        if(this.FM.CT.Weapons[i][j] instanceof RocketGunAIM9L)
                            hasIR = true;
                        if(this.FM.CT.Weapons[i][j] instanceof RocketGunAIM120A)
                            hasPRHM = true;
                        if(this.FM.CT.Weapons[i][j] instanceof RocketGunFlare)
                        {
                            backfire = true;
                            backfireList.add(this.FM.CT.Weapons[i][j]);
                        }
                    }

            }

    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        super.update(f);
        if(this.FM.CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteF86/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.5F);
            ((Actor) (chute)).pos.setRel(new Point3d(-5D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && this.FM.CT.bHasDragChuteControl)
            if(this.FM.CT.DragChuteControl == 1.0F && this.FM.getSpeedKMH() > 600F || this.FM.CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    ((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(this.FM.CT.DragChuteControl == 1.0F && this.FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                ((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !this.FM.CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
            chute.destroy();
        AIgroundattack();
        guidedMissileUtils.checkLockStatus();
        if(this.FM.AS.isMaster() && Config.isUSE_RENDER())
            if(this.FM.EI.engines[0].getPowerOutput() > 0.45F && this.FM.EI.engines[0].getStage() == 6)
            {
                if(this.FM.EI.engines[0].getPowerOutput() > 0.65F)
                {
                    if(this.FM.EI.engines[0].getPowerOutput() > 1.001F)
                        this.FM.AS.setSootState(this, 0, 3);
                    else
                        this.FM.AS.setSootState(this, 0, 2);
                } else
                {
                    this.FM.AS.setSootState(this, 0, 1);
                }
            } else
            if(this.FM.EI.engines[0].getPowerOutput() <= 0.45F || this.FM.EI.engines[0].getStage() < 6)
                this.FM.AS.setSootState(this, 0, 0);
    }

    public void doSetSootState(int i, int j)
    {
        switch(j)
        {
        case 0:
            Eff3DActor.setIntesity(turbo, 0.0F);
            Eff3DActor.setIntesity(turbosmoke, 0.0F);
            Eff3DActor.setIntesity(afterburner, 0.0F);
            break;

        case 1:
            Eff3DActor.setIntesity(turbo, 0.0F);
            Eff3DActor.setIntesity(turbosmoke, 1.0F);
            Eff3DActor.setIntesity(afterburner, 0.0F);
            break;

        case 2:
            Eff3DActor.setIntesity(turbo, 1.0F);
            Eff3DActor.setIntesity(turbosmoke, 1.0F);
            Eff3DActor.setIntesity(afterburner, 0.0F);
            break;

        case 3:
            Eff3DActor.setIntesity(turbo, 1.0F);
            Eff3DActor.setIntesity(turbosmoke, 1.0F);
            Eff3DActor.setIntesity(afterburner, 1.0F);
            break;
        }
    }

    public void AIgroundattack()
    {
        if((!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (this.FM instanceof Maneuver) && ((Maneuver)this.FM).get_task() == 7 && !this.FM.AP.way.isLanding())
            if(missilesList.isEmpty() && !((Maneuver)this.FM).hasBombs())
            {
                Pilot pilot = (Pilot)this.FM;
                Vector3d vector3d = new Vector3d();
                getSpeed(vector3d);
                Point3d point3d1 = new Point3d();
                this.pos.getAbs(point3d1);
                float f = (float)((double)this.FM.getAltitude() - World.land().HQ(((Tuple3d) (point3d1)).x, ((Tuple3d) (point3d1)).y));
                if(f < 55F && ((Tuple3d) (vector3d)).z < 0.0D)
                    vector3d.z = 0.0D;
                else
                if(pilot != null && Actor.isAlive(((Maneuver) (pilot)).target_ground))
                {
                    Point3d point3d2 = new Point3d();
                    ((Maneuver) (pilot)).target_ground.pos.getAbs(point3d2);
                    pilot.set_maneuver(43);
                    if(this.pos.getAbsPoint().distance(point3d2) < 2000D)
                    {
                        point3d2.sub(this.FM.Loc);
                        this.FM.Or.transformInv(point3d2);
                        this.FM.CT.PowerControl = 0.55F;
                    }
                }
                setSpeed(vector3d);
            } else
            if(!missilesList.isEmpty() && Time.current() > tX4Prev + 500L + (IR ? 10000L : 0L))
            {
                Pilot pilot1 = (Pilot)this.FM;
                if(pilot1.get_maneuver() == 43 && ((Maneuver) (pilot1)).target_ground != null)
                {
                    Point3d point3d = new Point3d();
                    ((Maneuver) (pilot1)).target_ground.pos.getAbs(point3d);
                    point3d.sub(this.FM.Loc);
                    this.FM.Or.transformInv(point3d);
                    if(point3d.x > 1000D && point3d.x < (IR ? 2250D : 1250D) + 250D * (double)this.FM.Skill)
                    {
                        if(!IR)
                            point3d.x /= 2 - this.FM.Skill / 3;
                        if(point3d.y < point3d.x && point3d.y > -point3d.x && point3d.z * 1.5D < point3d.x && point3d.z * 1.5D > -point3d.x)
                        {
                            launchMsl();
                            tX4Prev = Time.current();
                            Voice.speakAttackByRockets(this);
                        }
                    }
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
        if(this.FM.AS.isMaster() && !typeDockableIsDocked())
        {
            Aircraft aircraft = War.getNearestFriend(this);
            if(aircraft instanceof TypeTankerDrogue)
            {
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
                this.FM.CT.RefuelControl = 1.0F;
            } else
            {
                this.FM.CT.RefuelControl = 0.0F;
            }
        }
    }

    public void typeDockableAttemptDetach()
    {
        if(this.FM.AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
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

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
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
    private Eff3DActor turbo;
    private Eff3DActor turbosmoke;
    private Eff3DActor afterburner;

    static 
    {
        Class class1 = L_159A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "L-159A");
        Property.set(class1, "meshName", "3DO/Plane/L-159A/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/L-159A.fmd:L39");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitL159.class, CockpitL159FLIR.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 9, 9, 0, 0, 9, 9, 
            3, 3, 3, 3, 2, 2, 2, 2, 9, 2, 
            2, 2, 2, 9, 2, 2, 2, 2, 2, 2, 
            2, 2, 9, 9, 9, 9, 2, 2, 2, 2, 
            9, 2, 2, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", 
            "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalRock01", "_ExternalRock02", "_MGUN01", "_MGUN02", "_ExternalRock03", "_ExternalRock04", 
            "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", 
            "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", 
            "_ExternalRock25", "_ExternalRock26", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", 
            "_ExternalDev05", "_ExternalRock31", "_ExternalRock32", "_ExternalDev06", "_ExternalDev07"
        });
    }
}
