
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;


public class A_6A extends A_6
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeAcePlane, TypeFuelDump, TypeDockable
{

    public A_6A()
    {
        guidedMissileUtils = null;
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
        missilesList = new ArrayList();
        tX4Prev = 0L;
        g1 = null;
        backfireList = new ArrayList();
        backfire = false;
    }

    private void checkAmmo()
    {
        missilesList.clear();
        for(int i = 0; i < ((FlightModelMain) (super.FM)).CT.Weapons.length; i++)
            if(((FlightModelMain) (super.FM)).CT.Weapons[i] != null)
            {
                for(int j = 0; j < ((FlightModelMain) (super.FM)).CT.Weapons[i].length; j++)
                    if(((FlightModelMain) (super.FM)).CT.Weapons[i][j].haveBullets())
                    {
                        if(((FlightModelMain) (super.FM)).CT.Weapons[i][j] instanceof RocketGunFlare)
                            backfireList.add(((FlightModelMain) (super.FM)).CT.Weapons[i][j]);
                        else
                            missilesList.add(((FlightModelMain) (super.FM)).CT.Weapons[i][j]);
                    }

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

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void typeX4CAdjSidePlus()
    {
        deltaAzimuth = 0.1F;
    }

    public void typeX4CAdjSideMinus()
    {
        deltaAzimuth = -0.1F;
    }

    public void typeX4CAdjAttitudePlus()
    {
        deltaTangage = 0.1F;
    }

    public void typeX4CAdjAttitudeMinus()
    {
        deltaTangage = -0.1F;
    }

    public void typeX4CResetControls()
    {
        deltaAzimuth = deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth()
    {
        return deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage()
    {
        return deltaTangage;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        FM.Skill = 3;
        if(((FlightModelMain) (super.FM)).CT.Weapons[0] != null)
            g1 = ((FlightModelMain) (super.FM)).CT.Weapons[0][0];
    }

    public void update(float f)
    {
        if(bNeedSetup)
            checkAsDrone();
        guidedMissileUtils.update();
        int i = aircIndex();
        if(super.FM instanceof Maneuver)
            if(typeDockableIsDocked())
            {
                if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
                {
                    ((Maneuver)super.FM).unblock();
                    ((Maneuver)super.FM).set_maneuver(48);
                    for(int j = 0; j < i; j++)
                        ((Maneuver)super.FM).push(48);

                    if(((FlightModelMain) (super.FM)).AP.way.curr().Action != 3)
                        ((FlightModelMain) ((Maneuver)super.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).AP.way.Cur());
                    ((Pilot)super.FM).setDumbTime(3000L);
                }
                if(((FlightModelMain) (super.FM)).M.fuel < ((FlightModelMain) (super.FM)).M.maxFuel)
                    ((FlightModelMain) (super.FM)).M.fuel += 20F * f;
            } else
            if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
            {
                if(((FlightModelMain) (super.FM)).CT.GearControl == 0.0F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 0)
                    ((FlightModelMain) (super.FM)).EI.setEngineRunning();
                if(dtime > 0L && ((Maneuver)super.FM).Group != null)
                {
                    ((Maneuver)super.FM).Group.leaderGroup = null;
                    ((Maneuver)super.FM).set_maneuver(22);
                    ((Pilot)super.FM).setDumbTime(3000L);
                    if(Time.current() > dtime + 3000L)
                    {
                        dtime = -1L;
                        ((Maneuver)super.FM).clear_stack();
                        ((Maneuver)super.FM).set_maneuver(0);
                        ((Pilot)super.FM).setDumbTime(0L);
                    }
                } else
                if(((FlightModelMain) (super.FM)).AP.way.curr().Action == 0)
                {
                    Maneuver maneuver = (Maneuver)super.FM;
                    if(maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                        maneuver.Group.setGroupTask(2);
                }
            }

        if(FM.CT.getArrestor() > 0.2F)
            if(FM.Gears.arrestorVAngle != 0.0F)
            {
                float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
                arrestor = 0.8F * arrestor + 0.2F * f1;
                moveArrestorHook(arrestor);
            } else
            {
                float f2 = (-33F * FM.Gears.arrestorVSink) / 57F;
                if(f2 < 0.0F && FM.getSpeedKMH() > 60F)
                    Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                if(f2 > 0.0F && FM.CT.getArrestor() < 0.95F)
                    f2 = 0.0F;
                if(f2 > 0.2F)
                    f2 = 0.2F;
                if(f2 > 0.0F)
                    arrestor = 0.7F * arrestor + 0.3F * (arrestor + f2);
                else
                    arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
                if(arrestor < 0.0F)
                    arrestor = 0.0F;
                else
                if(arrestor > 1.0F)
                    arrestor = 1.0F;
                moveArrestorHook(arrestor);
            }

        super.update(f);
        if(super.backfire)
            backFire();
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
            if(((FlightModelMain) (super.FM)).AP.way.curr().getTarget() == null)
                ((FlightModelMain) (super.FM)).AP.way.next();
            target_ = ((FlightModelMain) (super.FM)).AP.way.curr().getTarget();
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
        if(((FlightModelMain) (super.FM)).AS.isMaster() && !typeDockableIsDocked())
        {
            Aircraft aircraft = War.getNearestFriend(this);
            if(aircraft instanceof TypeTankerDrogue)
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
        }
    }

    public void typeDockableAttemptDetach()
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
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
        ((FlightModelMain) (super.FM)).EI.setEngineRunning();
        ((FlightModelMain) (super.FM)).CT.setGearAirborne();
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

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
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
    public static float FlowRate = 10F;
    public static float FuelReserve = 1500F;
    public boolean bToFire;
    private float arrestor;
    private long tX4Prev;
    private ArrayList missilesList;
    private ArrayList backfireList;
    private boolean backfire;
    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private long dtime;
    private Actor target_;
    private Actor queen_;
    private int dockport_;
    public BulletEmitter Weapons[][];
    private BulletEmitter g1;
    private float deltaAzimuth;
    private float deltaTangage;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-6A");
        Property.set(class1, "meshName", "3DO/Plane/A-6A/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1959F);
        Property.set(class1, "yearExpired", 1980F);
        Property.set(class1, "FlightModel", "FlightModels/A6A.fmd:A6");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitA_6.class, com.maddox.il2.objects.air.CockpitA_6_Bombardier.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            2, 9, 9, 9, 9, 9, 9, 3, 3, 3, 
            3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 
            9, 9, 9, 9, 2, 2, 2, 2, 9, 9, 
            3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 9, 9, 9, 9, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", 
            "_ExternalBomb04", "_ExternalBomb05", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", 
            "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalDev11", "_Dev12", 
            "_Bomb06", "_ExternalBomb07", "_ExternalBomb08", "_Bomb09", "_Bomb10", "_Bomb11", "_Dev13", "_Dev14", "_Dev15", "_Dev16", 
            "_Bomb12", "_ExternalBomb13", "_ExternalBomb14", "_Bomb15", "_Bomb16", "_Bomb17", "_Bomb18", "_ExternalBomb19", "_ExternalBomb20", "_Bomb21", 
            "_Bomb22", "_Bomb23", "_Dev17", "_Dev18", "_Dev19", "_Dev20", "_Bomb24", "_ExternalBomb25", "_ExternalBomb26", "_Bomb27", 
            "_Bomb28", "_Bomb29", "_Bomb30", "_ExternalBomb31", "_ExternalBomb32", "_Bomb33", "_Bomb34", "_Bomb35"
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
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 4x Mk82 500Lbs Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x300GalDroptank + 3x Mk82 500Lbs Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5x Mk82 500Lbs Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 4x Mk82HD 500Lbs Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x300GalDroptank + 3x Mk82HD 500Lbs Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5x Mk82HD 500Lbs Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 4x Mk83 1000Lbs Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 2x Mk83 1000Lbs Bombs + 2x Aim9B";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 2x Mk83 1000Lbs Bombs + Flares";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x300GalDroptank + 3x Mk83 1000Lbs Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5x Mk83 1000Lbs Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3x Mk83 1000Lbs Bombs + Flares";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 4x Mk84 2000Lbs Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 2x Mk84 2000Lbs Bombs + 2x Aim9B";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x300GalDroptank + 3x Mk84 2000Lbs Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x300GalDroptank + 3x Mk84 2000Lbs Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5x Mk84 2000Lbs Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3x Mk84 2000Lbs Bombs + Flares";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 4x CBU24 Cluster Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x300GalDroptank + 3x CBU24 Cluster Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5x CBU24 Cluster Bombs";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3x CBU24 Cluster Bombs + Flares";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x AGM12C";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 4x AGM12C";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 2x AGM12C + 2xAim9B";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunAIM9B", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9B", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 2x AGM12C + Flares";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x300GalDroptank + 2x AGM12C";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 4x Zuni rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 4x LAU61 rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3x Mk83 1000Lbs Bombs + 2x Zuni rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3x Mk83 1000Lbs Bombs + 2x LAU61 rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3x Mk84 2000Lbs Bombs + 2x Zuni rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3x Mk84 2000Lbs Bombs + 2x LAU61 rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3x CBU24 Cluster Bombs + 2x Zuni rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3x CBU24 Cluster Bombs + 2x LAU61 rocket pods";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk81 250Lbs bomb";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk81 250Lbs bomb + 2x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk81 250Lbs bomb + 2xAim9B + 2x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk81 250Lbs bomb + Flares + 2x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk81 250Lbs bomb + 4x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk82 500Lbs bomb";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk82 500Lbs bomb + 2x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk82 500Lbs bomb + 2x Aim9B + 2x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk82 500Lbs bomb + 2x AGM12C + 2x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk82 500Lbs bomb + Flares + 2x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk82 500Lbs bomb + 4x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk82 HD bomb";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk82 HD bomb + 2x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk82 HD bomb + 2x Aim9B + 2x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk82 HD bomb + 2x AGM12C + 2x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk82 HD + Flares + 2x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x Mk82 HD bomb + 4x300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "18x Mk81 250Lbs bomb";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "18x Mk81 250Lbs bomb + Flares";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk81 250Lbs bomb + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk81 250Lbs bomb + Flares + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk81 250Lbs bomb + 2x Aim9B + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk81 250Lbs bomb + 2x AGM12C + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk81 250Lbs bomb + 2x Zuni + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk81 250Lbs bomb + 2x LAU32 + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "18x Mk82 500Lbs bomb";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "18x Mk82 500Lbs bomb + Flares";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk82 500Lbs bomb + 2x Aim9B + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk82 500Lbs bomb + Flares + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk82 500Lbs bomb + 2x AGM12C + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk82 500Lbs bomb + 2x Zuni + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk82 500Lbs bomb + 2x LAU32 + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "18x Mk82 HD bomb";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "18x Mk82 HD bomb + Flares";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk82 HD bomb + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk82 HD bomb + 2x Aim9B + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk82 HD bomb + Flares + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk82 HD bomb + 2x AGM12C + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk82 HD bomb + 2x ZUNI + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x Mk82 HD bomb + 2x ZUNI + 1x 300gal Droptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU130green", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "30x Mk81 250lbs bomb";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xMk81 250lbs bomb + 3x 300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "18x Mk81 250lbs bomb + 2x 300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "30x Mk82 500lbs bomb";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xMk82 500lbs bomb + 3x 300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "18x Mk82 500lbs bomb + 2x 300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "30x Mk82 HD bomb";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xMk82 HD bomb + 3x 300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "18x Mk82 HD bomb + 2x 300GalDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(9, "PylonF16TER", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x AGM65 Maverick";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 4x AGM65 Maverick";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 2x AGM65 Maverick + 2xAim9B";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunAIM9B", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9B", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300GalDroptank + 2x AGM65 Maverick + Flares";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_LAU3", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x300GalDroptank + 2x AGM65 Maverick";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "empty";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}