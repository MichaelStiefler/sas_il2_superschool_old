
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.sound.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;


public class Yak_36SMF extends Yak_36MF
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, TypeDockable
{

    public Yak_36SMF()
    {
        fxSirena = newSound("aircraft.Sirena2", false);
        smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
        guidedMissileUtils = null;
        sirenaSoundPlaying = false;
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
        smplSirena.setInfinite(true);
        trgtlongAI = null;
        missilesList = new ArrayList();
        hasIR = false;
        hasPRHM = false;
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

    private boolean sirenaWarning()
    {
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if(World.getPlayerAircraft() == null)
            return false;
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j += 360;
        Aircraft aircraft1 = War.getNearestEnemy(this, 4000F);
        if((aircraft1 instanceof Aircraft) && aircraft1.getArmy() != World.getPlayerArmy() && (aircraft1 instanceof TypeFighterAceMaker) && (aircraft1 instanceof TypeRadarGunsight) && aircraft1 != World.getPlayerAircraft() && aircraft1.getSpeed(vector3d) > 20D)
        {
            super.pos.getAbs(point3d);
            double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x;
            double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y;
            double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z;
            new String();
            new String();
            double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
            new String();
            double d7 = d3 - d;
            double d8 = d4 - d1;
            float f = 57.32484F * (float)Math.atan2(d8, -d7);
            int k = (int)(Math.floor((int)f) - 90D);
            if(k < 0)
                k += 360;
            int l = k - i;
            double d9 = d - d3;
            double d10 = d1 - d4;
            double d11 = Math.sqrt(d6 * d6);
            int i1 = (int)(Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
            float f1 = 57.32484F * (float)Math.atan2(i1, d11);
            int j1 = (int)(Math.floor((int)f1) - 90D);
            if(j1 < 0)
                j1 += 360;
            int k1 = j1 - j;
            int l1 = (int)(Math.ceil(((double)i1 * 3.2808399000000001D) / 100D) * 100D);
            if(l1 >= 5280)
                l1 = (int)Math.floor(l1 / 5280);
            bRadarWarning = (double)i1 <= 3000D && (double)i1 >= 50D && k1 >= 195 && k1 <= 345 && Math.sqrt(l * l) >= 120D;
            playSirenaWarning(bRadarWarning);
        } else
        {
            bRadarWarning = false;
            playSirenaWarning(bRadarWarning);
        }
        return true;
    }

    public void playSirenaWarning(boolean flag)
    {
        if(flag && !sirenaSoundPlaying)
        {
            fxSirena.play(smplSirena);
            sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "AN/APR-36: Enemy at Six!");
        } else
        if(!flag && sirenaSoundPlaying)
        {
            fxSirena.cancel();
            sirenaSoundPlaying = false;
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
        super.FM.Skill = 3;
        droptank();
        FM.CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
    }

    private final void doRemoveTankL()
    {
        if(hierMesh().chunkFindCheck("TankL") != -1)
        {
            hierMesh().hideSubTrees("TankL");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("TankL"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }

    private final void doRemoveTankR()
    {
        if(hierMesh().chunkFindCheck("TankR") != -1)
        {
            hierMesh().hideSubTrees("TankR");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("TankR"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }

    private void droptank()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                    if(FM.CT.Weapons[i][j].haveBullets())
                    {
                        if(FM.CT.Weapons[i][j] instanceof FuelTankGun_Tankyak)
                        {
                            havedroptank = true;
                            super.hierMesh().chunkVisible("TankL", true);
                            super.hierMesh().chunkVisible("TankR", true);
                        }
                        if(FM.CT.Weapons[i][j] instanceof RocketGunR13MS)
                            hasIR = true;
                        if(FM.CT.Weapons[i][j] instanceof RocketGunR3RL)
                            hasPRHM = true;
                    }

            }

    }

    private void AIswitchmissile()
    {
        if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !(super.FM instanceof Pilot))
            return;
        Pilot pilot = (Pilot)super.FM;
        if((pilot.get_maneuver() == 63 || ((Maneuver) (pilot)).target != null) && hasIR)
        {
            Class theMissileClass = com.maddox.il2.objects.weapons.MissileR3RL.class;
            guidedMissileUtils.changeMissileClass(theMissileClass);
        }
        if((pilot.get_maneuver() != 63 || ((Maneuver) (pilot)).target == null) && hasPRHM)
        {
            Class theMissileClass = com.maddox.il2.objects.weapons.MissileR13MS.class;
            guidedMissileUtils.changeMissileClass(theMissileClass);
        }
    }

    public void update(float f)
    {
        if(bNeedSetup)
            checkAsDrone();
        guidedMissileUtils.update();
        AIswitchmissile();
        if(super.FM instanceof Maneuver)
            receivingRefuel(f);
        sirenaWarning();
        super.update(f);
        if(havedroptank && !FM.CT.Weapons[9][1].haveBullets())
        {
            havedroptank = false;
            doRemoveTankL();
            doRemoveTankR();
        }
        updateDragChute();
    }

    private void updateDragChute()
    {
        if(FM.CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteF86/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.5F);
            ((Actor) (chute)).pos.setRel(new Point3d(-5D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && FM.CT.bHasDragChuteControl)
            if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() > 600F || FM.CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    ((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
                FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                ((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
                FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !FM.CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
            chute.destroy();
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
        super.missionStarting();
        checkAsDrone();
    }

    private void checkAsDrone()
    {
        if(target_ == null)
        {
            if(FM.AP.way.curr().getTarget() == null)
                FM.AP.way.next();
            target_ = FM.AP.way.curr().getTarget();
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
        if(FM.AS.isMaster() && !typeDockableIsDocked())
        {
            Aircraft aircraft = War.getNearestFriend(this);
            if(aircraft instanceof TypeTankerDrogue)
            {
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
            }
        }
    }

    public void typeDockableAttemptDetach()
    {
        if(FM.AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
            ((TypeDockable)queen_).typeDockableRequestDetach(this);
    }

    public void typeDockableRequestAttach(Actor actor1)
    {
    }

    public void typeDockableRequestDetach(Actor actor1)
    {
    }

    public void typeDockableRequestAttach(Actor actor1, int j, boolean flag1)
    {
    }

    public void typeDockableRequestDetach(Actor actor1, int j, boolean flag1)
    {
    }

    public void typeDockableDoAttachToDrone(Actor actor1, int j)
    {
    }

    public void typeDockableDoDetachFromDrone(int j)
    {
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i)
    {
        queen_ = actor;
        dockport_ = i;
        queen_last = queen_;
        queen_time = 0L;
        FM.EI.setEngineRunning();
        FM.CT.setGearAirborne();
        moveGear(0.0F);
        com.maddox.il2.fm.FlightModel flightmodel = ((SndAircraft) ((Aircraft)queen_)).FM;
        if(aircIndex() == 0 && (super.FM instanceof Maneuver) && (flightmodel instanceof Maneuver))
        {
            Maneuver maneuver = (Maneuver)flightmodel;
            Maneuver maneuver1 = (Maneuver)super.FM;
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
            com.maddox.il2.engine.ActorNet actornet = null;
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

    private void receivingRefuel(float f)
    {
        int i = aircIndex();

        if(typeDockableIsDocked())
        {
            if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
            {
                ((Maneuver)super.FM).unblock();
                ((Maneuver)super.FM).set_maneuver(48);
                for(int j = 0; j < i; j++)
                    ((Maneuver)super.FM).push(48);

                if(FM.AP.way.curr().Action != 3)
                    ((FlightModelMain) ((Maneuver)super.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).AP.way.Cur());
                ((Pilot)FM).setDumbTime(3000L);
            }
            FuelTank fuelTanks[];
            fuelTanks = FM.CT.getFuelTanks();
            if(FM.M.fuel < FM.M.maxFuel - 12F)
            {
                float getFuel = ((TypeTankerDrogue) (queen_)).requestRefuel((Aircraft) this, 11.101F, f);
                FM.M.fuel += getFuel;
            }
            else if(fuelTanks.length > 0 && fuelTanks[0] != null && !FM.M.bFuelTanksDropped)
            {
                float getFuel = ((TypeTankerDrogue) (queen_)).requestRefuel((Aircraft) this, 11.101F, f);
                float freeTankSum = 0F;
                for(int num = 0; num < fuelTanks.length; num++)
                    freeTankSum += fuelTanks[num].checkFreeTankSpace();
                if(freeTankSum < 12F)
                    typeDockableAttemptDetach();
                for(int num = 0; num < fuelTanks.length; num++)
                    fuelTanks[num].doRefuel(getFuel * (fuelTanks[num].checkFreeTankSpace() / freeTankSum));
            }
            else
                typeDockableAttemptDetach();
        } else
        if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
        {
            if(FM.CT.GearControl == 0.0F && FM.EI.engines[0].getStage() == 0)
                FM.EI.setEngineRunning();
            if(dtime > 0L && ((Maneuver)super.FM).Group != null)
            {
                ((Maneuver)super.FM).Group.leaderGroup = null;
                ((Maneuver)super.FM).set_maneuver(22);
                ((Pilot)FM).setDumbTime(3000L);
                if(Time.current() > dtime + 3000L)
                {
                    dtime = -1L;
                    ((Maneuver)super.FM).clear_stack();
                    ((Maneuver)super.FM).set_maneuver(0);
                    ((Pilot)FM).setDumbTime(0L);
                }
            } else
            if(FM.AP.way.curr().Action == 0)
            {
                Maneuver maneuver = (Maneuver)super.FM;
                if(maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                    maneuver.Group.setGroupTask(2);
            }
        }
    }

    private long tfire;
    public BulletEmitter Weapons[][];
    private boolean hasIR;
    private boolean hasPRHM;
    private GuidedMissileUtils guidedMissileUtils;
    private SoundFX fxSirena;
    private Sample smplSirena;
    private boolean sirenaSoundPlaying;
    private boolean bRadarWarning;
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
    private Actor trgtlongAI;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak-36MF");
        Property.set(class1, "meshName", "3DO/Plane/Yak-36MF/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/Yak-36MF.fmd:yak36");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitYak36MF.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        int ai[] = new int[61];
        ai[3] = 9;
        ai[4] = 9;
        ai[5] = 2;
        ai[6] = 2;
        ai[7] = 2;
        ai[8] = 2;
        ai[9] = 9;
        ai[10] = 9;
        ai[11] = 3;
        ai[12] = 3;
        ai[13] = 3;
        ai[14] = 3;
        ai[15] = 3;
        ai[16] = 3;
        ai[17] = 3;
        ai[18] = 3;
        ai[19] = 3;
        ai[20] = 3;
        ai[21] = 3;
        ai[22] = 3;
        ai[23] = 3;
        ai[24] = 3;
        ai[25] = 3;
        ai[26] = 3;
        ai[27] = 9;
        ai[28] = 9;
        ai[29] = 3;
        ai[30] = 3;
        ai[31] = 9;
        ai[32] = 9;
        ai[33] = 9;
        ai[34] = 9;
        ai[35] = 3;
        ai[36] = 3;
        ai[37] = 9;
        ai[38] = 9;
        ai[39] = 9;
        ai[40] = 9;
        ai[41] = 9;
        ai[42] = 9;
        ai[43] = 2;
        ai[44] = 2;
        ai[45] = 2;
        ai[46] = 2;
        ai[47] = 2;
        ai[48] = 2;
        ai[49] = 2;
        ai[50] = 2;
        ai[51] = 2;
        ai[52] = 2;
        ai[53] = 2;
        ai[54] = 2;
        Aircraft.weaponTriggersRegister(class1, ai);
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalDev03", 
            "_ExternalDev04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", 
            "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalDev05", "_ExternalDev06", "_ExternalRock21", 
            "_ExternalRock22", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", 
            "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", 
            "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_CANNON04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", 
            "_CANNON09"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 61;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xR-3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xR-13M+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xR-3R+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-3R+2xR-13M+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-13M+2xR-3R+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-13M+4xR-3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR3RL", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB250+4xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xFAB250+2xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB250+4xR-13M+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB250+2xR-13M+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK250+4xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK250+2xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRBK250+4xR-13M+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK250+2xR-13M+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xS-24+4xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xS-24+4xR-13M+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xS-24+2xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xS-24+2xR-13M+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-27+4xR-13M+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-27+2xS-24+2xR-13M+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRBK250+2xUB-27+2xR-13M+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB250+2xUB-27+2xR-13M+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xUB-27+4xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRBK250+4xUB-27+2xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB250+4xUB-27+2xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRBK250+2xUB-27+4xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB250+2xUB-27+4xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK250+2xUB-27+2xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB250+2xUB-27+2xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-27+2xS-24+4xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-27+4xS-24+2xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-27+2xKh-66+4xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+2xR-13M+2xR-3R+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunR3RL", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(5, "RocketGunR3RL", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+2xS-24+2xR-13M+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunS24", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunS24", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(5, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66L+2xUB-27+4xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunKh66L", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunKh66L", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66L+2xR-13M+2xR-3R+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunKh66L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunKh66L", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunR3RL", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(5, "RocketGunR3RL", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66L+2xS-24+2xR-13M+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunKh66L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunKh66L", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunS24", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunS24", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(5, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xZB360+4xR-13M+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xZB360+2xUB-27+4xR-13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH6_30ki", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5MS", 27);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonUB27", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR13MS", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}