package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class Mig_15KISh extends Mig_15K
    implements TypeStormovik, TypeGuidedMissileCarrier, TypeX4Carrier, TypeCountermeasure, TypeThreatDetector, TypeZBReceiver, TypeDockable, TypeRadarGunsight
{

    public Mig_15KISh()
    {
        FlapAngle = 55F;
        hasDroptanks = false;
        hasMissiles = false;
        hasBombs = false;
        hasKAB = false;
        hasBoosters = false;
        ProbeOut = false;
        Ratos = 0;
        boosterFireOutTime = -1L;
        overrideBailout = false;
        ejectComplete = false;
        lTimeNextEject = 0L;
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
        bToFire = false;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 3000F;
        fSightCurSpeed = 200F;
        fSightCurReadyness = 0.0F;
        fxKAB = newSound("weapon.K5.lock", false);
        smplKAB = new Sample("K5_lock.wav", 256, 65535);
        smplKAB.setInfinite(true);
        KABSoundPlaying = false;
        KABEngaged = false;
        KAB = 0;
        APmode5 = false;
        ExtFuel = 0.0F;
        TwoTanks = true;
        APmode6 = false;
        APmode7 = false;
    }

    public void typeFighterAceMakerRangeFinder()
    {
    }

    public void typeX4CAdjSidePlus()
    {
        deltaAzimuth = 0.002F;
        k14WingspanType--;
        if(k14WingspanType < 0)
            k14WingspanType = 0;
    }

    public void typeX4CAdjSideMinus()
    {
        deltaAzimuth = -0.002F;
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
    }

    public void typeX4CAdjAttitudePlus()
    {
        deltaTangage = 0.002F;
    }

    public void typeX4CAdjAttitudeMinus()
    {
        deltaTangage = -0.002F;
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

    public void moveRefuel(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.95F, 0.0F, -0.8F);
        hierMesh().chunkSetLocate("ExtrasProbe", Aircraft.xyz, Aircraft.ypr);
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
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
        }
    }

    public void typeDockableAttemptDetach()
    {
        if(this.FM.AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
            ((TypeDockable)queen_).typeDockableRequestDetach(this);
        if(ProbeOut)
        {
            moveRefuel(-90F);
            ProbeOut = false;
        }
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
            if(ProbeOut)
            {
                moveRefuel(-90F);
                ProbeOut = false;
            }
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

    private boolean KABscan()
    {
        Point3d point3d = new Point3d();
        pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Actor) (aircraft)).pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft)).pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft)).pos.getAbsPoint().z;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i = 360 + i;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j = 360 + j;
        for(java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
        {
            Actor actor = (Actor)entry.getValue();
            if(((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && actor.getArmy() != World.getPlayerArmy() && actor != World.getPlayerAircraft())
            {
                pos.getAbs(point3d);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                new String();
                new String();
                int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
                int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                new String();
                double d7 = d3 - d;
                double d8 = d4 - d1;
                float f = 57.32484F * (float)Math.atan2(d8, -d7);
                int i1 = (int)(Math.floor((int)f) - 90D);
                if(i1 < 0)
                    i1 = 360 + i1;
                int j1 = i1 - i;
                double d9 = d - d3;
                double d10 = d1 - d4;
                double d11 = Math.sqrt(d6 * d6);
                int k1 = (int)Math.ceil(Math.sqrt(d10 * d10 + d9 * d9));
                float f1 = 57.32484F * (float)Math.atan2(k1, d11);
                int l1 = (int)(Math.floor((int)f1) - 90D);
                if(l1 < 0)
                    l1 = 360 + l1;
                int i2 = l1 - j;
                k1 = (int)((double)k1 / 1000D);
                int j2 = (int)Math.ceil(k1);
                byte byte0 = 9;
                if(actor instanceof ShipGeneric)
                    byte0 = 40;
                if(actor instanceof BigshipGeneric)
                    byte0 = 60;
                if(j1 < 0)
                    j1 = 360 + j1;
                double d12 = FM.getAltitude() / (float)j2;
                if(k1 <= byte0 && (double)j2 <= 15D)
                {
                    if((double)j1 <= 20D || (double)j1 >= 340D)
                    {
                        if(d12 >= 325D)
                        {
                            KABEngaged = true;
                            playKAB(KABEngaged);
                        } else
                        {
                            KABEngaged = false;
                            playKAB(KABEngaged);
                        }
                    } else
                    {
                        KABEngaged = false;
                        playKAB(KABEngaged);
                    }
                } else
                {
                    KABEngaged = false;
                    playKAB(KABEngaged);
                }
            }
        }

        return true;
    }

    public void playKAB(boolean flag)
    {
        if(flag && !KABSoundPlaying)
        {
            KABSoundPlaying = true;
            fxKAB.play(smplKAB);
            KAB = KAB + 1;
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "KAB-500M engaged");
        } else
        if(!flag && KABSoundPlaying)
        {
            KABSoundPlaying = false;
            fxKAB.cancel();
            if(KAB > 1 && ((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "KAB-500M disengaged");
        }
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        if(this.thisWeaponsName.startsWith("01"))
        {
            hierMesh().chunkVisible("S3ASR", true);
            hierMesh().chunkVisible("S3RailASR", true);
            hierMesh().chunkVisible("S6ASR", true);
            hierMesh().chunkVisible("S6RailASR", true);
            scopemode = 1;
        }
        if(this.thisWeaponsName.startsWith("02"))
        {
            hierMesh().chunkVisible("S2FAB", true);
            hierMesh().chunkVisible("S7PTB", true);
            hasKAB = true;
            hasDroptanks = true;
            TwoTanks = false;
            FlapAngle = 20F;
            if(this.FM.M.fuel > 324F)
                hasBoosters = true;
        }
        if(this.thisWeaponsName.startsWith("03"))
        {
            hierMesh().chunkVisible("S1AAM", true);
            hierMesh().chunkVisible("S1RailAAM", true);
            hierMesh().chunkVisible("S8AAM", true);
            hierMesh().chunkVisible("S8RailAAM", true);
            hasMissiles = true;
            FlapAngle = 20F;
            scopemode = 1;
        }
        if(this.thisWeaponsName.startsWith("04"))
        {
            hierMesh().chunkVisible("S3ASR", true);
            hierMesh().chunkVisible("S3RailASR", true);
            hierMesh().chunkVisible("S6ASR", true);
            hierMesh().chunkVisible("S6RailASR", true);
        }
        if(this.thisWeaponsName.startsWith("05"))
        {
            hierMesh().chunkVisible("S4RAT", true);
            hierMesh().chunkVisible("S5PTB", true);
            hasDroptanks = true;
            TwoTanks = false;
            if(this.FM.M.fuel > 299F)
                hasBoosters = true;
            ORDmode = 3;
        }
        if(this.thisWeaponsName.startsWith("06"))
            scopemode = 1;
        if(this.thisWeaponsName.startsWith("07"))
        {
            hierMesh().chunkVisible("S3ASR", true);
            hierMesh().chunkVisible("S6ASR", true);
        }
        if(this.thisWeaponsName.startsWith("08"))
        {
            hierMesh().chunkVisible("S1AAM", true);
            hierMesh().chunkVisible("S1RailAAM", true);
            hierMesh().chunkVisible("S3ASR", true);
            hierMesh().chunkVisible("S3RailASR", true);
            hierMesh().chunkVisible("S6ASR", true);
            hierMesh().chunkVisible("S6RailASR", true);
            hierMesh().chunkVisible("S8AAM", true);
            hierMesh().chunkVisible("S8RailAAM", true);
            ORDmode = 5;
        }
        if(this.thisWeaponsName.startsWith("09"))
        {
            hierMesh().chunkVisible("S1FAB", true);
            hierMesh().chunkVisible("S2FAB", true);
            hierMesh().chunkVisible("S7FAB", true);
            hierMesh().chunkVisible("S8FAB", true);
            hasBombs = true;
            FlapAngle = 20F;
            if(this.FM.M.fuel > 508F)
                hasBoosters = true;
            ORDmode = 4;
        }
        if(this.thisWeaponsName.startsWith("10"))
        {
            hierMesh().chunkVisible("S2FAB", true);
            hierMesh().chunkVisible("S7FAB", true);
            hasBombs = true;
            FlapAngle = 20F;
            if(this.FM.M.fuel > 460F)
                hasBoosters = true;
            ORDmode = 2;
        }
        if(this.thisWeaponsName.startsWith("11"))
        {
            hierMesh().chunkVisible("S1FAB", true);
            hierMesh().chunkVisible("S2FAB", true);
            hierMesh().chunkVisible("S7FAB", true);
            hierMesh().chunkVisible("S8FAB", true);
            hasBombs = true;
            FlapAngle = 20F;
            if(this.FM.M.fuel > 616F)
                hasBoosters = true;
            ORDmode = 1;
        }
        if(this.thisWeaponsName.startsWith("12"))
        {
            hierMesh().chunkVisible("S2PTB", true);
            hierMesh().chunkVisible("S7PTB", true);
            hasDroptanks = true;
            FlapAngle = 20F;
            if(this.FM.M.fuel > 388F)
                hasBoosters = true;
        }
        if(this.thisWeaponsName.startsWith("13"))
        {
            hierMesh().chunkVisible("S1AAM", true);
            hierMesh().chunkVisible("S1RailAAM", true);
            hierMesh().chunkVisible("S2PTB", true);
            hierMesh().chunkVisible("S3ASR", true);
            hierMesh().chunkVisible("S3RailASR", true);
            hierMesh().chunkVisible("S6ASR", true);
            hierMesh().chunkVisible("S6RailASR", true);
            hierMesh().chunkVisible("S7PTB", true);
            hierMesh().chunkVisible("S8AAM", true);
            hierMesh().chunkVisible("S8RailAAM", true);
            hasDroptanks = true;
            FlapAngle = 20F;
            if(this.FM.M.fuel > 284F)
                hasBoosters = true;
            scopemode = 1;
        }
        if(this.thisWeaponsName.startsWith("14"))
        {
            hierMesh().chunkVisible("S1AAM", true);
            hierMesh().chunkVisible("S1RailAAM", true);
            hierMesh().chunkVisible("S2PTB", true);
            hierMesh().chunkVisible("S3ASR", true);
            hierMesh().chunkVisible("S3RailASR", true);
            hierMesh().chunkVisible("S6ASR", true);
            hierMesh().chunkVisible("S6RailASR", true);
            hierMesh().chunkVisible("S7PTB", true);
            hierMesh().chunkVisible("S8AAM", true);
            hierMesh().chunkVisible("S8RailAAM", true);
            hasMissiles = true;
            hasDroptanks = true;
            FlapAngle = 20F;
            if(this.FM.M.fuel > 74F)
                hasBoosters = true;
            scopemode = 1;
        }
        if(this.thisWeaponsName.startsWith("15"))
        {
            hierMesh().chunkVisible("S2FAB", true);
            hierMesh().chunkVisible("S3ASR", true);
            hierMesh().chunkVisible("S3RailASR", true);
            hierMesh().chunkVisible("S6ASR", true);
            hierMesh().chunkVisible("S6RailASR", true);
            hierMesh().chunkVisible("S7PTB", true);
            hasKAB = true;
            hasDroptanks = true;
            TwoTanks = false;
            FlapAngle = 20F;
            if(this.FM.M.fuel > 106F)
                hasBoosters = true;
        }
        if(this.thisWeaponsName.startsWith("16"))
        {
            hierMesh().chunkVisible("S1AAM", true);
            hierMesh().chunkVisible("S1RailASR", true);
            hierMesh().chunkVisible("S4RAT", true);
            hierMesh().chunkVisible("S5PTB", true);
            hierMesh().chunkVisible("S8AAM", true);
            hierMesh().chunkVisible("S8RailASR", true);
            hasDroptanks = true;
            TwoTanks = false;
            if(this.FM.M.fuel > 81F)
                hasBoosters = true;
            ORDmode = 3;
        }
        if(this.thisWeaponsName.startsWith("17"))
        {
            hierMesh().chunkVisible("S1FAB", true);
            hierMesh().chunkVisible("S2PTB", true);
            hierMesh().chunkVisible("S3ASR", true);
            hierMesh().chunkVisible("S3RailASR", true);
            hierMesh().chunkVisible("S6ASR", true);
            hierMesh().chunkVisible("S6RailASR", true);
            hierMesh().chunkVisible("S7PTB", true);
            hierMesh().chunkVisible("S8AAM", true);
            hierMesh().chunkVisible("S8RailAAM", true);
            hasDroptanks = true;
            hasBoosters = true;
            FlapAngle = 20F;
        }
        if(this.thisWeaponsName.startsWith("18"))
        {
            hierMesh().chunkVisible("S1FAB", true);
            hierMesh().chunkVisible("S2PTB", true);
            hierMesh().chunkVisible("S3ASR", true);
            hierMesh().chunkVisible("S3RailASR", true);
            hierMesh().chunkVisible("S6ASR", true);
            hierMesh().chunkVisible("S6RailASR", true);
            hierMesh().chunkVisible("S7PTB", true);
            hierMesh().chunkVisible("S8FAB", true);
            hasDroptanks = true;
            hasBombs = true;
            hasBoosters = true;
            FlapAngle = 20F;
            ORDmode = 4;
        }
        if(this.thisWeaponsName.startsWith("19"))
        {
            hierMesh().chunkVisible("S1FAB", true);
            hierMesh().chunkVisible("S2PTB", true);
            hierMesh().chunkVisible("S3ASR", true);
            hierMesh().chunkVisible("S3RailASR", true);
            hierMesh().chunkVisible("S6ASR", true);
            hierMesh().chunkVisible("S6RailASR", true);
            hierMesh().chunkVisible("S7PTB", true);
            hierMesh().chunkVisible("S8FAB", true);
            hasDroptanks = true;
            hasBombs = true;
            hasBoosters = true;
            FlapAngle = 20F;
            ORDmode = 1;
        }
        if(this.thisWeaponsName.startsWith("20"))
        {
            hierMesh().chunkVisible("S1FAB", true);
            hierMesh().chunkVisible("S2PTB", true);
            hierMesh().chunkVisible("S3ASR", true);
            hierMesh().chunkVisible("S6ASR", true);
            hierMesh().chunkVisible("S7PTB", true);
            hierMesh().chunkVisible("S8FAB", true);
            hasDroptanks = true;
            hasBombs = true;
            hasBoosters = true;
            FlapAngle = 20F;
            ORDmode = 4;
        }
        if(this.thisWeaponsName.startsWith("21"))
        {
            hierMesh().chunkVisible("S1FAB", true);
            hierMesh().chunkVisible("S2PTB", true);
            hierMesh().chunkVisible("S3ASR", true);
            hierMesh().chunkVisible("S6ASR", true);
            hierMesh().chunkVisible("S7PTB", true);
            hierMesh().chunkVisible("S8FAB", true);
            hasDroptanks = true;
            hasBombs = true;
            hasBoosters = true;
            FlapAngle = 20F;
            ORDmode = 1;
        }
        if(this.thisWeaponsName.startsWith("22"))
        {
            hierMesh().chunkVisible("S1FAB", true);
            hierMesh().chunkVisible("S2FAB", true);
            hierMesh().chunkVisible("S3ASR", true);
            hierMesh().chunkVisible("S3RailASR", true);
            hierMesh().chunkVisible("S6ASR", true);
            hierMesh().chunkVisible("S6RailASR", true);
            hierMesh().chunkVisible("S7FAB", true);
            hierMesh().chunkVisible("S8FAB", true);
            if(this.FM.M.fuel > 274F)
                hasBoosters = true;
            hasBombs = true;
            FlapAngle = 20F;
            ORDmode = 4;
        }
    }

    public float checkExtFuel(int i)
    {
        FuelTank afueltank[] = this.FM.CT.getFuelTanks();
        if(afueltank.length == 0)
            return 0.0F;
        if(!TwoTanks)
//            ExtFuel = afueltank[0].Fuel * 1.102311F;
            ExtFuel = Reflection.getFloat(afueltank[0], "Fuel") * 1.102311F;
        if(TwoTanks)
//            ExtFuel = afueltank[0].Fuel * 1.102311F + afueltank[1].Fuel * 1.102311F;
            ExtFuel = Reflection.getFloat(afueltank[0], "Fuel") * 1.102311F + Reflection.getFloat(afueltank[1], "Fuel") * 1.102311F;
        return ExtFuel;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(hasKAB)
            KABscan();
        if(hasDroptanks && !APmode6)
            checkExtFuel(0);
        if(ExtFuel <= 0.0F && APmode6)
        {
            this.FM.CT.dropFuelTanks();
            hasDroptanks = false;
            APmode6 = false;
        }
        if((super.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode() || !flag || !(this.FM instanceof Pilot))
            return;
        if(flag && this.FM.AP.way.curr().Action == 3 && typeDockableIsDocked() && Math.abs(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).Or.getKren()) < 3F)
            if(this.FM.isPlayers())
            {
                if((this.FM instanceof RealFlightModel) && !((RealFlightModel)this.FM).isRealMode())
                {
                    typeDockableAttemptDetach();
                    ((Maneuver)this.FM).set_maneuver(22);
                    ((Maneuver)this.FM).setCheckStrike(false);
                    this.FM.Vwld.z -= 5D;
                    dtime = Time.current();
                }
            } else
            {
                typeDockableAttemptDetach();
                ((Maneuver)this.FM).set_maneuver(22);
                ((Maneuver)this.FM).setCheckStrike(false);
                this.FM.Vwld.z -= 5D;
                dtime = Time.current();
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

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33:
        case 34:
        case 35:
        case 36:
        case 37:
        case 38:
            doCutBoosters();
            FM.AS.setGliderBoostOff();
            hasBoosters = false;
            break;
        }
        return super.cutFM(i, j, actor);
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

    protected void moveFlap(float f)
    {
        float f1 = -FlapAngle * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
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
        if(bNeedSetup)
            checkAsDrone();
        guidedMissileUtils.update();
        int i = aircIndex();
        if(this.FM instanceof Maneuver)
            if(typeDockableIsDocked())
            {
                if(!(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode())
                {
                    ((Maneuver)this.FM).unblock();
                    ((Maneuver)this.FM).set_maneuver(48);
                    for(int j = 0; j < i; j++)
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
        if(hasDroptanks && !this.FM.CT.Weapons[9][0].haveBullets())
        {
            ExtFuel = 0.0F;
            hasDroptanks = false;
            if(!hasMissiles && !hasBombs)
                FlapAngle = 55F;
        }
        if(!hasDroptanks && hasMissiles && !this.FM.CT.Weapons[2][2].haveBullets())
        {
            FlapAngle = 55F;
            hasMissiles = false;
        }
        if(!hasDroptanks && hasBombs && !this.FM.CT.Weapons[3][2].haveBullets())
        {
            FlapAngle = 55F;
            hasBombs = false;
        }
        if(hasKAB && !this.FM.CT.Weapons[3][0].haveBullets())
        {
            hasKAB = false;
            fxKAB.cancel();
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "KAB-500M launched");
        }
    }

    protected boolean hasBoosters;
    private int Ratos;
    protected long boosterFireOutTime;
    private Eff3DActor RatoL;
    private Eff3DActor RatoR;
    private Eff3DActor RatoLsmk;
    private Eff3DActor RatoRsmk;
    private float FlapAngle;
    private boolean hasDroptanks;
    private boolean hasMissiles;
    private boolean hasBombs;
    private boolean hasKAB;
    private boolean ProbeOut;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private long lTimeNextEject;
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
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
    public boolean bToFire;
    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private long dtime;
    private Actor target_;
    private Actor queen_;
    private int dockport_;
    private float deltaAzimuth;
    private float deltaTangage;
    public float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    private SoundFX fxKAB;
    private Sample smplKAB;
    private boolean KABSoundPlaying;
    private boolean KABEngaged;
    private int KAB;
    public boolean APmode5;
    public float ExtFuel;
    private boolean TwoTanks;
    public boolean APmode6;
    public boolean APmode7;

    static 
    {
        Class class1 = Mig_15KISh.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-15KISh");
        Property.set(class1, "meshName", "3DO/Plane/MiG-15KISh(Multi1)/Hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1949F);
        Property.set(class1, "yearExpired", 1969F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-15KISh.fmd:MiG-15KISh_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitMig_15KISh.class, CockpitP85_RP5.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            1, 0, 0, 0, 9, 9, 9, 9, 2, 2, 
            2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 
            3, 3, 3, 3, 9, 9, 2, 2, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExtTank01", "_ExtTank02", "_ExtTank03", "_ExternalDev01", "_ExternalRock01", "_ExternalRock01", 
            "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", 
            "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04", "_ExternalDev02", "_ExternalDev03", "_ExternalRock05", "_ExternalRock06", "_ExternalBomb05", "_ExternalBomb05"
        });
    }
}
