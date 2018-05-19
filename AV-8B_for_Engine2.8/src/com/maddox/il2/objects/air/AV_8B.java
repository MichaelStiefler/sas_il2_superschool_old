
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.sound.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.*;

// Referenced classes of package com.maddox.il2.objects.air:
//            AV_8, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector,
//            TypeGSuit, TypeAcePlane, TypeFuelDump, TypeDockable,
//            PaintSchemeFMPar05, Aircraft, TypeFighterAceMaker, TypeSupersonic,
//            TypeFastJet, F_18, NetAircraft, Chute,
//            TypeTankerDrogue

public class AV_8B extends AV_8
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeDockable, TypeRadarWarningReceiver
{

    public AV_8B()
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
        counterFlareList = new ArrayList();
        counterChaffList = new ArrayList();
        bHasAGM = false;
        bHasAShM = false;
        bHasUGR = false;
        lastAGMcheck = -1L;
        backfire = false;
        if(Config.cur.ini.get("Mods", "RWRTextStop", 0) > 0) bRWR_Show_Text_Warning = false;
        rwrUtils = new RadarWarningReceiverUtils(this, RWR_MAX_DETECT, RWR_KEEP_SECONDS, RWR_RECEIVE_ELEVATION, RWR_DETECT_IRMIS, RWR_DETECT_ELEVATION, bRWR_Show_Text_Warning);
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
        if(counterFlareList.isEmpty())
            hasFlare = false;
        else
        {
            if(Time.current() > lastFlareDeployed + 700L)
            {
                ((RocketGunFlare_gn16)counterFlareList.get(0)).shots(1);
                hasFlare = true;
                lastFlareDeployed = Time.current();
                if(!((RocketGunFlare_gn16)counterFlareList.get(0)).haveBullets())
                    counterFlareList.remove(0);
            }
        }
        if(counterChaffList.isEmpty())
            hasChaff = false;
        else
        {
            if(Time.current() > lastChaffDeployed + 900L)
            {
                ((RocketGunChaff_gn16)counterChaffList.get(0)).shots(1);
                hasChaff = true;
                lastChaffDeployed = Time.current();
                if(!((RocketGunChaff_gn16)counterChaffList.get(0)).haveBullets())
                    counterChaffList.remove(0);
            }
        }
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public RadarWarningReceiverUtils getRadarWarningReceiverUtils()
    {
        return rwrUtils;
    }

    public void myRadarSearchYou(Actor actor)
    {
        rwrUtils.recordRadarSearched(actor, (String) null);
    }

    public void myRadarSearchYou(Actor actor, String soundpreset)
    {
        rwrUtils.recordRadarSearched(actor, soundpreset);
    }

    public void myRadarLockYou(Actor actor)
    {
        rwrUtils.recordRadarLocked(actor, (String) null);
    }

    public void myRadarLockYou(Actor actor, String soundpreset)
    {
        rwrUtils.recordRadarLocked(actor, soundpreset);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        FM.Skill = 3;
        FM.CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        FM.turret[0].bIsAIControlled = false;
        rwrUtils.onAircraftLoaded();
        rwrUtils.setLockTone("", "aircraft.usRWRLock", "aircraft.usRWRLaunchWarningMissileMissile", "", "usRWRLock.wav", "usRWRLaunchWarningMissileMissile.wav");
    }

    private void checkAmmo()
    {
        counterFlareList.clear();
        counterChaffList.clear();
        super.bHasPaveway = false;
        bHasAGM = false;
        bHasAShM = false;
        bHasUGR = false;
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                    if(FM.CT.Weapons[i][j].haveBullets())
                    {
                        if(FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16)
                            counterFlareList.add(FM.CT.Weapons[i][j]);
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunChaff_gn16)
                            counterChaffList.add(FM.CT.Weapons[i][j]);
                        else if(FM.CT.Weapons[i][j] instanceof BombGunGBU10_Mk84LGB_gn16 ||
                                FM.CT.Weapons[i][j] instanceof BombGunGBU12_Mk82LGB_gn16 ||
                                FM.CT.Weapons[i][j] instanceof BombGunGBU16_Mk83LGB_gn16)
                            super.bHasPaveway = true;
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunAGM65E_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunAGM84E_gn16)
                            bHasAGM = true;
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunAGM84D_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunAGM84J_gn16)
                            bHasAShM = true;
                        else if(FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71AP_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunHYDRA_gn16)
                            bHasUGR = true;
                    }

            }

    }

    private void checkAIAGMrest()
    {
        bHasAGM = false;
        bHasAShM = false;
        bHasUGR = false;
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                    if(FM.CT.Weapons[i][j].haveBullets())
                    {
                        if(FM.CT.Weapons[i][j] instanceof RocketGunAGM65E_gn16 ||
                           FM.CT.Weapons[i][j] instanceof RocketGunAGM84E_gn16)
                            bHasAGM = true;
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunAGM84D_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunAGM84J_gn16)
                            bHasAShM = true;
                        else if(FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71AP_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunHYDRA_gn16)
                            bHasUGR = true;
                    }
            }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);

        if((!(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode())
           && !((Maneuver)super.FM).hasBombs() && FM.AP.way.curr().Action == 3)
        {
            if(!bHasAGM && !bHasAShM && !bHasUGR)
                FM.AP.way.next();
            else if(!bHasAGM && !bHasUGR && bHasAShM && (FM.AP.way.curr().getTarget() == null || !(FM.AP.way.curr().getTarget() instanceof TgtShip)))
                FM.AP.way.next();
            else if(!bHasAGM && bHasUGR && !bHasAShM && FM.CT.rocketNameSelected != "Zuni" && FM.CT.rocketNameSelected != "Hydra")
            {
                for(int i = 0; i < 4; i++)
                {
                    if(FM.CT.rocketNameSelected == "Zuni" || FM.CT.rocketNameSelected == "Hydra")
                        break;
                    FM.CT.toggleRocketHook();
                }
            }
            else if(Time.current() > lastAGMcheck + 30000L)
            {
                checkAIAGMrest();
                lastAGMcheck = Time.current();
            }
        }
    }

    public void update(float f)
    {
        if(bNeedSetup)
            checkAsDrone();
        guidedMissileUtils.update();
        if(super.FM instanceof Maneuver)
            receivingRefuel(f);
        rwrUtils.update();
        backfire = rwrUtils.getBackfire();
        bRadarWarning = rwrUtils.getRadarLockedWarning();
        bMissileWarning = rwrUtils.getMissileWarning();
        super.update(f);
        if(backfire)
            backFire();
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
        }
        else if(bHasDeployedDragChute && FM.CT.bHasDragChuteControl)
            if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() > 600F || FM.CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    ((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
                removeChuteTimer = Time.current() + 250L;
            }
            else if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                ((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
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
        checkAmmo();
        checkAsDrone();
    }

    public void startCockpitSounds()
    {
        rwrUtils.setSoundEnable(true);
    }

    public void stopCockpitSounds()
    {
        rwrUtils.stopAllRWRSounds();
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
            if((aircraft instanceof TypeTankerDrogue) && FM.CT.getRefuel() > 0.95F)
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
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
        FlightModel flightmodel = ((SndAircraft) ((Aircraft)queen_)).FM;
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
        }
        else
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
            if(FM.CT.getRefuel() < 0.9F)
            {
                typeDockableAttemptDetach();
            }
            else
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
                    float freeTankSum = 0F;
                    for(int num = 0; num < fuelTanks.length; num++)
                        freeTankSum += fuelTanks[num].checkFreeTankSpace();
                    if(freeTankSum < 12F)
                        typeDockableAttemptDetach();
                    float getFuel = ((TypeTankerDrogue) (queen_)).requestRefuel((Aircraft) this, 11.101F, f);
                    for(int num = 0; num < fuelTanks.length; num++)
                        fuelTanks[num].doRefuel(getFuel * (fuelTanks[num].checkFreeTankSpace() / freeTankSum));
                }
                else
                    typeDockableAttemptDetach();
            }
        }
        else if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
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
            }
            else if(FM.AP.way.curr().Action == 0)
            {
                Maneuver maneuver = (Maneuver)super.FM;
                if(maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                    maneuver.Group.setGroupTask(2);
            }
        }
    }

    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(byte bt)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[bt];
        try
        {
            a_lweaponslot[92] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - AV_8B : Default loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

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
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private long dtime;
    private Actor target_;
    private Actor queen_;
    private int dockport_;
    private ArrayList counterFlareList;
    private ArrayList counterChaffList;
    private boolean bHasAGM;
    private boolean bHasAShM;
    private boolean bHasUGR;
    private long lastAGMcheck;

    private RadarWarningReceiverUtils rwrUtils;
    public float misslebrg;
    public float aircraftbrg;
    public boolean backfire;
    public boolean bRadarWarning;
    public boolean bMissileWarning;

    private static final int RWR_MAX_DETECT = 16;
    private static final int RWR_KEEP_SECONDS = 7;
    private static final double RWR_RECEIVE_ELEVATION = 45.0D;
    private static final boolean RWR_DETECT_IRMIS = false;
    private static final boolean RWR_DETECT_ELEVATION = false;
    private boolean bRWR_Show_Text_Warning = true;

    static
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Harrier");
        Property.set(class1, "meshName", "3DO/Plane/AV-8B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1997F);
        Property.set(class1, "yearExpired", 2014F);
        Property.set(class1, "FlightModel", "FlightModels/AV-8B.fmd:AV8B");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitAV_8B.class, com.maddox.il2.objects.air.CockpitAV8FLIR.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 9, 9, 9, 9, 9, 9, 9, 9, 9,
            9, 9, 9, 9, 9, 9, 9, 9, 9, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 7, 7, 8, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01",       "_ExternalDev01",  "_ExternalDev02",  "_ExternalDev03",  "_ExternalDev04",  "_ExternalDev05",  "_ExternalDev06",  "_ExternalDev07",  "_ExternalDev08",  "_ExternalDev09",
            "_ExternalDev10",  "_ExternalDev11",  "_ExternalDev12",  "_ExternalDev13",  "_ExternalDev14",  "_ExternalDev15",  "_ExternalDev16",  "_ExternalDev17",  "_ExternalDev18",  "_ExternalBomb01",
            "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11",
            "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalMis01",  "_ExternalMis01",  "_ExternalMis02",  "_ExternalMis02",
            "_ExternalMis03",  "_ExternalMis03",  "_ExternalMis04",  "_ExternalMis04",  "_ExternalMis05",  "_ExternalMis05",  "_ExternalMis06",  "_ExternalMis06",  "_ExternalRock01", "_ExternalRock01",
            "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalRock05", "_ExternalRock05", "_ExternalRock06", "_ExternalRock06",
            "_ExternalRock07", "_ExternalRock07", "_ExternalRock08", "_ExternalRock08", "_Rock09",         "_Rock10",         "_Rock11",         "_Rock12",         "_Rock13",         "_Rock14",
            "_Rock15",         "_Rock16",         "_Rock17",         "_Rock18",         "_Rock19",         "_Rock20",         "_Rock21",         "_Rock22",         "_Rock23",         "_Rock24",
            "_Rock25",         "_Rock26",         "_Rock27",         "_Rock28",         "_Rock29",         "_Rock30",         "_Rock31",         "_Rock32",         "_Rock33",         "_Rock34",
            "_Rock35",         "_Rock36",         "_FlareL",         "_FlareR",         "_ChaffL",         "_ChaffR"
        });
        String s = "empty";
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 96;
            s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM120+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM120+4xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM120+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM120";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM120+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM120+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM120+2xAIM9+2xDt+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM120+2xDt+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9+4xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82HD+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xMk82_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xMk82HD_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82HD+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83HD+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83HD+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82HD+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xMk82_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xMk82HD_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk83";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk83HD";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk20+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "10xMk20_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk20+2xMk82+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk20+2xMk83+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+2xMk20+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83+2xMk20+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk77_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+2xMk77+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+2xMk82HD+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+2xMk83HD+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82+2xZuni+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82HD+2xZuni+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83+2xZuni+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83HD+2xZuni+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk20+2xZuni+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk77+2xZuni+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+2xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+2xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+4xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+4xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83+4xZuniAP";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83HD+4xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk20+4xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+4xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xZuni+2xMk82+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xZuniAP+2xMk83+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xZuni+2xMk20+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xZuni+2xMk77+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+2xHydra70+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+2xHydra70+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk20+4xHydra70";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+4xHydra70";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xHydra70+2xMk82";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xHydra70+2xMk82HD";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xHydra70+2xMk20";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xHydra70+2xM77";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82HD+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83HD+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk20+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk77+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xZuni+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xHydra70+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xZuni+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xHydra70+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xGBU12+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU16+1xGBU12+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU16_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "7xGBU12";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU16+6xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xGBU12+2xMk82_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xGBU12+2xMk20_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU16+2xZuni_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xGBU12+2xZuni_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xGBU12+4xHydra70_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU16+1xMk82+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+1xMk20+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+1xZuni+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xGBU12+4xZuni+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU12+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU16+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU12+2xMk20+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xGBU16+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU12+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xGBU12+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xGBU12+3xMk82+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xGBU12+3xZuni+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xGBU12+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM82+1xMk82+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM83+1xMk82+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM82+1xMk20+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM83+1xMk20+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM82+1xGBU12+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM83+1xGBU12+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM82+4xMk20_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM83+2xMk83_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xJDAM82+2xAIM9_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xJDAM82+2xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xJDAM82+2xZuni_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM82+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM83+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xJDAM82+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xJDAM82+Mk20+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xJDAM82+Zuni+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM82+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM83+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xJDAM82+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xJDAM83+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xJDAM82+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xMk82";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xMk20";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+4xZuni_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xMk83_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+6xMk82_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+6xMk20_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xGBU12+2xAIM9_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xGBU16+2xAIM9_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+6xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xJDAM82+2xAIM9_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xJDAM83+2xAIM9_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+6xJDAM82_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xMk83+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xZuni+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xGBU12+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xGBU16+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+2xMk83+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+6xMk82+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+6xMk20+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+2xGBU12+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+2xGBU16+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+6xGBU12+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+3xGBU12+3xMk20+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+2xJDAM82+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+2xJDAM83+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+6xJDAM82+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xGBU12+2xZuni+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xGBU16+2xZuni+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xMk82+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xMk83+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xMk20+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xMk77+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xGBU12+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xGBU16+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+2xZuni+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+2xAIM9+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+2xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+2xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+1xAGM65E";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+2xAGM65E_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+1xAGM65E+1xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+1xAGM65E+1xGBU16_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+1xAGM65E";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+2xAGM65E_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+1xAGM65E+1xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+1xAGM65E+1xGBU16_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84D+2xAGM65E";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84D+1xAGM65E+1xGBU12";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84D+1xAGM65E+1xGBU16";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84J+2xAGM65E";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84J+1xAGM65E+1xGBU12";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84J+1xAGM65E+1xGBU16";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84D+1xAGM65E+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84D+1xGBU12+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84D+1xGBU16+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84J+1xAGM65E+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84J+1xGBU12+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84J+1xGBU16+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_2xMk82+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_2xMk77+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_2xMk77+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_2xAGM65E_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_1xAGM65E+1xMk77_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_1xAGM65E+1xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_1xMk77+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_1xMk77+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_1xAGM65E+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_1xGBU12+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - AV_8B : " + s);
            System.out.println(exception.getMessage());
            exception.printStackTrace();
 }
    }
}