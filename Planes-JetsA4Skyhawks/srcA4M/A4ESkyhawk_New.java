// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 03/11/2015 04:00:21 p.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   A4ESkyhawk_New.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3f;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.RocketAIM9BUtils;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.*;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Skyhawk_New, TypeAIM9Carrier, TypeStormovik, TypeBNZFighter, 
//            TypeFighterAceMaker, PaintSchemeFMPar05, Aircraft, NetAircraft, 
//            Cockpit, TypeGSuit

public class A4ESkyhawk_New extends Skyhawk_New
    implements TypeAIM9Carrier, TypeStormovik, TypeBNZFighter, TypeFighterAceMaker
{

    public A4ESkyhawk_New()
    {
        guidedMissileUtils = null;
        trgtPk = 0.0F;
        trgtAI = null;
        bToFire = false;
        tX4Prev = 0L;
        rocketsList = new ArrayList();
        rocketsList.clear();
        trgtPk = 0.0F;
        trgtAI = null;
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        steera = 0.0F;
        lastRadarLockThreatActive = 0L;
        intervalRadarLockThreat = 1000L;
        lastMissileLaunchThreatActive = 0L;
        intervalMissileLaunchThreat = 1000L;
        rocketsList = new ArrayList();
        guidedMissileUtils = new RocketAIM9BUtils(this);
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        lastCommonThreatActive = 0L;
        intervalCommonThreat = 1000L;
        lastRadarLockThreatActive = 0L;
        intervalRadarLockThreat = 1000L;
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
        long curTime = Time.current();
        if(curTime - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = curTime;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long curTime = Time.current();
        if(curTime - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = curTime;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long curTime = Time.current();
        if(curTime - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
        {
            lastMissileLaunchThreatActive = curTime;
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

    public void getGFactors(TypeGSuit.GFactors theGFactors)
    {
        theGFactors.setGFactors(1.0F, 1.0F, 1.0F, 1.8F, 1.5F, 1.0F);
    }

    public Actor getMissileTarget()
    {
        return guidedMissileUtils.getMissileTarget();
    }

    public Point3f getMissileTargetOffset()
    {
        return guidedMissileUtils.getSelectedActorOffset();
    }

    public boolean hasMissiles()
    {
        return !rocketsList.isEmpty();
    }

    public void shotMissile()
    {
        if(hasMissiles())
            rocketsList.remove(0);
    }

    public int getMissileLockState()
    {
        return guidedMissileUtils.getMissileLockState();
    }

    private float getMissilePk()
    {
        float thePk = 0.0F;
        guidedMissileUtils.setMissileTarget(guidedMissileUtils.lookForGuidedMissileTarget(((Interpolate) (super.FM)).actor, guidedMissileUtils.getMaxPOVfrom(), guidedMissileUtils.getMaxPOVto(), guidedMissileUtils.getPkMaxDist()));
        if(Actor.isValid(guidedMissileUtils.getMissileTarget()))
            thePk = guidedMissileUtils.Pk(((Interpolate) (super.FM)).actor, guidedMissileUtils.getMissileTarget());
        return thePk;
    }

    private void checkAIlaunchMissile()
    {
        if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !(super.FM instanceof Pilot))
            return;
        if(rocketsList.isEmpty())
            return;
        Pilot pilot = (Pilot)super.FM;
        if((pilot.get_maneuver() == 27 || pilot.get_maneuver() == 62 || pilot.get_maneuver() == 63) && ((Maneuver) (pilot)).target != null)
        {
            trgtAI = ((Interpolate) (((Maneuver) (pilot)).target)).actor;
            if(!Actor.isValid(trgtAI) || !(trgtAI instanceof Aircraft))
                return;
            bToFire = false;
            if(trgtPk > 25F && Actor.isValid(guidedMissileUtils.getMissileTarget()) && (guidedMissileUtils.getMissileTarget() instanceof Aircraft) && guidedMissileUtils.getMissileTarget().getArmy() != ((Interpolate) (super.FM)).actor.getArmy() && Time.current() > tX4Prev + 10000L)
            {
                bToFire = true;
                tX4Prev = Time.current();
                shootRocket();
                bToFire = false;
            }
        }
    }

    public void shootRocket()
    {
        if(rocketsList.isEmpty())
        {
            return;
        } else
        {
            ((RocketGun)rocketsList.get(0)).shots(1);
            return;
        }
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        rocketsList.clear();
        guidedMissileUtils.createMissileList(rocketsList);
        if(super.thisWeaponsName.startsWith("2xAAMs"))
        {
            hierMesh().chunkVisible("RailL1_D0", true);
            hierMesh().chunkVisible("RailR1_D0", true);
        }
        if(super.thisWeaponsName.startsWith("8xZunis"))
        {
            hierMesh().chunkVisible("PylonCenter2", true);
            hierMesh().chunkVisible("CapR_D0", true);
            hierMesh().chunkVisible("CapL_D0", true);
        }
        if(super.thisWeaponsName.startsWith("4xCBUs") || super.thisWeaponsName.startsWith("2xMk83") || super.thisWeaponsName.startsWith("6xMk81") || super.thisWeaponsName.startsWith("6xMk82") || super.thisWeaponsName.startsWith("6xSnakeEyes"))
        {
            hierMesh().chunkVisible("CapR_D0", true);
            hierMesh().chunkVisible("CapL_D0", true);
        }
        if(super.thisWeaponsName.startsWith("3xMk81") || super.thisWeaponsName.startsWith("3xMk82") || super.thisWeaponsName.startsWith("3xSnakeEyes") || super.thisWeaponsName.endsWith("6xMk81") || super.thisWeaponsName.endsWith("6xMk82") || super.thisWeaponsName.endsWith("6xSnakeEyes") || super.thisWeaponsName.endsWith("6xCBUs"))
            hierMesh().chunkVisible("PylonCenter2", true);
        else
            return;
    }

    public void update(float f)
    {
        super.update(f);
        trgtPk = getMissilePk();
        guidedMissileUtils.checkLockStatus();
        checkAIlaunchMissile();
        if(((FlightModelMain) (super.FM)).CT.saveWeaponControl[2])
        {
            hierMesh().chunkVisible("CapR_D0", false);
            hierMesh().chunkVisible("CapL_D0", false);
        }
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 45F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -70F * f, 0.0F);
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + k14Mode);
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
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 10F;
        if(k14Distance < 200F)
            k14Distance = 200F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset()
    {
    }

    public void typeFighterAceMakerAdjSideslipPlus()
    {
        k14WingspanType--;
        if(k14WingspanType < 0)
            k14WingspanType = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte(k14Mode);
        netmsgguaranted.writeByte(k14WingspanType);
        netmsgguaranted.writeFloat(k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        k14Mode = netmsginput.readByte();
        k14WingspanType = netmsginput.readByte();
        k14Distance = netmsginput.readFloat();
    }

    private long lastMissileLaunchThreatActive;
    private long intervalMissileLaunchThreat;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    public Aircraft aircraft;
    public Actor actor;
    private float llpos;
    public static boolean bChangedPit = false;
    public boolean bToFire;
    private ArrayList rocketsList;
    private long tX4Prev;
    private float trgtPk;
    private Actor trgtAI;
    private float steera;
    private RocketAIM9BUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private long lastCommonThreatActive;
    private long intervalCommonThreat;
    private long lastRadarLockThreatActive;
    private long intervalRadarLockThreat;
    private static final float NEG_G_TOLERANCE_FACTOR = 1F;
    private static final float NEG_G_TIME_FACTOR = 1F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 1.8F;
    private static final float POS_G_TIME_FACTOR = 1.5F;
    private static final float POS_G_RECOVERY_FACTOR = 1F;
    private static Actor hunted = null;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Skyhawk");
        Property.set(class1, "meshName", "3DO/Plane/A4ESkyhawk_New(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/SkyHawkv1.fmd:SkyHawkv1_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitSkyhawk_New.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 9, 9, 0, 0, 9, 9, 
            3, 3, 3, 3, 2, 2, 2, 2, 9, 2, 
            2, 2, 2, 9, 2, 2, 2, 2, 9, 9, 
            9, 3, 3, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", 
            "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalRock01", "_ExternalRock02", "_MGUN01", "_MGUN02", "_ExternalRock03", "_ExternalRock04", 
            "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", 
            "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalDev01", "_ExternalDev02", 
            "_ExternalDev03", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16"
        });
        String as[] = new String[47];
        as[0] = "MGunHispanoMkIki 200";
        as[1] = "MGunHispanoMkIki 200";
        Aircraft.weaponsRegister(class1, "default", as);
        String as1[] = new String[47];
        as1[0] = "MGunHispanoMkIki 200";
        as1[1] = "MGunHispanoMkIki 200";
        as1[4] = "FuelTankGun_TankSkyhawk 1";
        as1[5] = "FuelTankGun_TankSkyhawk 1";
        Aircraft.weaponsRegister(class1, "2xDpt ", as1);
        String as2[] = new String[47];
        as2[0] = "MGunHispanoMkIki 200";
        as2[1] = "MGunHispanoMkIki 200";
        as2[8] = "BombGunMk81 1";
        as2[9] = "BombGunMk81 1";
        as2[10] = "BombGunMk81 1";
        as2[11] = "BombGunMk81 1";
        as2[12] = "BombGunMk81 1";
        as2[13] = "BombGunMk81 1";
        as2[14] = "PylonTERoc 1";
        as2[15] = "PylonTERoc 1";
        as2[18] = "RocketGun5inchZuni 1";
        as2[19] = "RocketGun5inchZuni 1";
        as2[20] = "RocketGun5inchZuni 1";
        as2[21] = "RocketGun5inchZuni 1";
        as2[22] = "RocketGun5inchZuni 1";
        as2[23] = "RocketGun5inchZuni 1";
        as2[24] = "RocketGun5inchZuni 1";
        as2[25] = "RocketGun5inchZuni 1";
        as2[36] = "Pylon_Zuni 1";
        as2[37] = "Pylon_Zuni 1";
        as2[38] = "FuelTankGun_TankSkyhawk 1";
        Aircraft.weaponsRegister(class1, "6xMk81_8xZunis_1xDpt", as2);
        String as3[] = new String[47];
        as3[0] = "MGunHispanoMkIki 200";
        as3[1] = "MGunHispanoMkIki 200";
        as3[8] = "BombGunMk82 1";
        as3[9] = "BombGunMk82 1";
        as3[10] = "BombGunMk82 1";
        as3[11] = "BombGunMk82 1";
        as3[12] = "BombGunMk82 1";
        as3[13] = "BombGunMk82 1";
        as3[14] = "PylonTERoc 1";
        as3[15] = "PylonTERoc 1";
        as3[18] = "RocketGun5inchZuni 1";
        as3[19] = "RocketGun5inchZuni 1";
        as3[20] = "RocketGun5inchZuni 1";
        as3[21] = "RocketGun5inchZuni 1";
        as3[22] = "RocketGun5inchZuni 1";
        as3[23] = "RocketGun5inchZuni 1";
        as3[24] = "RocketGun5inchZuni 1";
        as3[25] = "RocketGun5inchZuni 1";
        as3[36] = "Pylon_Zuni 1";
        as3[37] = "Pylon_Zuni 1";
        as3[38] = "FuelTankGun_TankSkyhawk 1";
        Aircraft.weaponsRegister(class1, "6xMk82_8xZunis_1xDpt", as3);
        String as4[] = new String[47];
        as4[0] = "MGunHispanoMkIki 200";
        as4[1] = "MGunHispanoMkIki 200";
        as4[8] = "BombGunMk82SnakeEye 1";
        as4[9] = "BombGunMk82SnakeEye 1";
        as4[10] = "BombGunMk82SnakeEye 1";
        as4[11] = "BombGunMk82SnakeEye 1";
        as4[12] = "BombGunMk82SnakeEye 1";
        as4[13] = "BombGunMk82SnakeEye 1";
        as4[14] = "PylonTERoc 1";
        as4[15] = "PylonTERoc 1";
        as4[18] = "RocketGun5inchZuni 1";
        as4[19] = "RocketGun5inchZuni 1";
        as4[20] = "RocketGun5inchZuni 1";
        as4[21] = "RocketGun5inchZuni 1";
        as4[22] = "RocketGun5inchZuni 1";
        as4[23] = "RocketGun5inchZuni 1";
        as4[24] = "RocketGun5inchZuni 1";
        as4[25] = "RocketGun5inchZuni 1";
        as4[36] = "Pylon_Zuni 1";
        as4[37] = "Pylon_Zuni 1";
        as4[38] = "FuelTankGun_TankSkyhawk 1";
        Aircraft.weaponsRegister(class1, "6xSnakeEyes_8xZunis_1xDpt", as4);
        String as5[] = new String[47];
        as5[0] = "MGunHispanoMkIki 200";
        as5[1] = "MGunHispanoMkIki 200";
        as5[8] = "BombGunCBU24 1";
        as5[9] = "BombGunCBU24 1";
        as5[12] = "BombGunCBU24 1";
        as5[13] = "BombGunCBU24 1";
        as5[14] = "PylonTERoc 1";
        as5[15] = "PylonTERoc 1";
        as5[18] = "RocketGun5inchZuni 1";
        as5[19] = "RocketGun5inchZuni 1";
        as5[20] = "RocketGun5inchZuni 1";
        as5[21] = "RocketGun5inchZuni 1";
        as5[22] = "RocketGun5inchZuni 1";
        as5[23] = "RocketGun5inchZuni 1";
        as5[24] = "RocketGun5inchZuni 1";
        as5[25] = "RocketGun5inchZuni 1";
        as5[36] = "Pylon_Zuni 1";
        as5[37] = "Pylon_Zuni 1";
        as5[38] = "FuelTankGun_TankSkyhawk 1";
        Aircraft.weaponsRegister(class1, "4xCBUs_8xZunis_1xDpt", as5);
        String as6[] = new String[47];
        as6[0] = "MGunHispanoMkIki 200";
        as6[1] = "MGunHispanoMkIki 200";
        as6[4] = "BombGunMk83 1";
        as6[5] = "BombGunMk83 1";
        as6[6] = "BombGunMk83 1";
        as6[7] = "BombGunMk83 1";
        as6[38] = "FuelTankGun_TankSkyhawk 1";
        Aircraft.weaponsRegister(class1, "4xMk83_1xDpt", as6);
        String as7[] = new String[47];
        as7[0] = "MGunHispanoMkIki 200";
        as7[1] = "MGunHispanoMkIki 200";
        as7[4] = "BombGunMk83 1";
        as7[5] = "BombGunMk83 1";
        as7[18] = "RocketGun5inchZuni 1";
        as7[19] = "RocketGun5inchZuni 1";
        as7[20] = "RocketGun5inchZuni 1";
        as7[21] = "RocketGun5inchZuni 1";
        as7[22] = "RocketGun5inchZuni 1";
        as7[23] = "RocketGun5inchZuni 1";
        as7[24] = "RocketGun5inchZuni 1";
        as7[25] = "RocketGun5inchZuni 1";
        as7[36] = "Pylon_Zuni 1";
        as7[37] = "Pylon_Zuni 1";
        as7[38] = "FuelTankGun_TankSkyhawk 1";
        Aircraft.weaponsRegister(class1, "2xMk83_8xZunis_1xDpt", as7);
        Aircraft.weaponsRegister(class1, "8xZunis_6xMk81_2xDpt", new String[] {
            "MGunHispanoMkIki 200", "MGunHispanoMkIki 200", 0, 0, "FuelTankGun_TankSkyhawk 1", "FuelTankGun_TankSkyhawk 1", 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", 
            "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, "Pylon_Zuni 1", "Pylon_Zuni 1", 0, 0, 
            0, "BombGunMk81 1", "BombGunMk81 1", "BombGunMk81 1", "BombGunMk81 1", "BombGunMk81 1", "BombGunMk81 1"
        });
        Aircraft.weaponsRegister(class1, "8xZunis_6xMk82_2xDpt", new String[] {
            "MGunHispanoMkIki 200", "MGunHispanoMkIki 200", 0, 0, "FuelTankGun_TankSkyhawk 1", "FuelTankGun_TankSkyhawk 1", 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", 
            "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, "Pylon_Zuni 1", "Pylon_Zuni 1", 0, 0, 
            0, "BombGunMk82 1", "BombGunMk82 1", "BombGunMk82 1", "BombGunMk82 1", "BombGunMk82 1", "BombGunMk82 1"
        });
        Aircraft.weaponsRegister(class1, "8xZunis_6xSnakeEyes_2xDpt", new String[] {
            "MGunHispanoMkIki 200", "MGunHispanoMkIki 200", 0, 0, "FuelTankGun_TankSkyhawk 1", "FuelTankGun_TankSkyhawk 1", 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", 
            "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, "Pylon_Zuni 1", "Pylon_Zuni 1", 0, 0, 
            0, "BombGunMk82SnakeEye 1", "BombGunMk82SnakeEye 1", "BombGunMk82SnakeEye 1", "BombGunMk82SnakeEye 1", "BombGunMk82SnakeEye 1", "BombGunMk82SnakeEye 1"
        });
        Aircraft.weaponsRegister(class1, "8xZunis_6xCBUs_2xDpt", new String[] {
            "MGunHispanoMkIki 200", "MGunHispanoMkIki 200", 0, 0, "FuelTankGun_TankSkyhawk 1", "FuelTankGun_TankSkyhawk 1", 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", 
            "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", "RocketGun5inchZuni 1", 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, "Pylon_Zuni 1", "Pylon_Zuni 1", 0, 0, 
            0, "BombGunCBU24 1", "BombGunCBU24 1", "BombGunCBU24 1", "BombGunCBU24 1", "BombGunCBU24 1", "BombGunCBU24 1"
        });
        String as8[] = new String[47];
        as8[0] = "MGunHispanoMkIki 200";
        as8[1] = "MGunHispanoMkIki 200";
        as8[4] = "PylonSUU11 1";
        as8[5] = "PylonSUU11 1";
        as8[16] = "MGunMiniGun 1";
        as8[17] = "MGunMiniGun 1";
        as8[29] = "RocketGunAIM_9B 1";
        as8[30] = "BombGunNull 1";
        as8[32] = "RocketGunAIM_9B 1";
        as8[33] = "BombGunNull 1";
        as8[38] = "FuelTankGun_TankSkyhawk 1";
        Aircraft.weaponsRegister(class1, "2xAAMs_2xMinigun_1xDpt", as8);
        String as9[] = new String[47];
        as9[0] = "MGunHispanoMkIki 200";
        as9[1] = "MGunHispanoMkIki 200";
        as9[4] = "FuelTankGun_TankSkyhawk 1";
        as9[5] = "FuelTankGun_TankSkyhawk 1";
        as9[29] = "RocketGunAIM_9B 1";
        as9[30] = "BombGunNull 1";
        as9[32] = "RocketGunAIM_9B 1";
        as9[33] = "BombGunNull 1";
        Aircraft.weaponsRegister(class1, "2xAAMs_2xDpt", as9);
        String as10[] = new String[47];
        as10[0] = "MGunHispanoMkIki 200";
        as10[1] = "MGunHispanoMkIki 200";
        as10[8] = "BombGunMk81 1";
        as10[9] = "BombGunMk81 1";
        as10[10] = "BombGunMk81 1";
        as10[11] = "BombGunMk81 1";
        as10[12] = "BombGunMk81 1";
        as10[13] = "BombGunMk81 1";
        as10[14] = "PylonTERoc 1";
        as10[15] = "PylonTERoc 1";
        as10[29] = "RocketGunAIM_9B 1";
        as10[30] = "BombGunNull 1";
        as10[32] = "RocketGunAIM_9B 1";
        as10[33] = "BombGunNull 1";
        as10[38] = "FuelTankGun_TankSkyhawk 1";
        Aircraft.weaponsRegister(class1, "2xAAMs_6xMk81_1xDpt", as10);
        String as11[] = new String[47];
        as11[0] = "MGunHispanoMkIki 200";
        as11[1] = "MGunHispanoMkIki 200";
        as11[8] = "BombGunMk82 1";
        as11[9] = "BombGunMk82 1";
        as11[10] = "BombGunMk82 1";
        as11[11] = "BombGunMk82 1";
        as11[12] = "BombGunMk82 1";
        as11[13] = "BombGunMk82 1";
        as11[14] = "PylonTERoc 1";
        as11[15] = "PylonTERoc 1";
        as11[29] = "RocketGunAIM_9B 1";
        as11[30] = "BombGunNull 1";
        as11[32] = "RocketGunAIM_9B 1";
        as11[33] = "BombGunNull 1";
        as11[38] = "FuelTankGun_TankSkyhawk 1";
        Aircraft.weaponsRegister(class1, "2xAAMs_6xMk82_1xDpt", as11);
        String as12[] = new String[47];
        as12[0] = "MGunHispanoMkIki 200";
        as12[1] = "MGunHispanoMkIki 200";
        as12[8] = "BombGunMk82SnakeEye 1";
        as12[9] = "BombGunMk82SnakeEye 1";
        as12[10] = "BombGunMk82SnakeEye 1";
        as12[11] = "BombGunMk82SnakeEye 1";
        as12[12] = "BombGunMk82SnakeEye 1";
        as12[13] = "BombGunMk82SnakeEye 1";
        as12[14] = "PylonTERoc 1";
        as12[15] = "PylonTERoc 1";
        as12[29] = "RocketGunAIM_9B 1";
        as12[30] = "BombGunNull 1";
        as12[32] = "RocketGunAIM_9B 1";
        as12[33] = "BombGunNull 1";
        as12[38] = "FuelTankGun_TankSkyhawk 1";
        Aircraft.weaponsRegister(class1, "2xAAMs_6xSnakeEyes_1xDpt", as12);
        String as13[] = new String[47];
        as13[0] = "MGunHispanoMkIki 200";
        as13[1] = "MGunHispanoMkIki 200";
        as13[8] = "BombGunCBU24 1";
        as13[9] = "BombGunCBU24 1";
        as13[12] = "BombGunCBU24 1";
        as13[13] = "BombGunCBU24 1";
        as13[14] = "PylonTERoc 1";
        as13[15] = "PylonTERoc 1";
        as13[29] = "RocketGunAIM_9B 1";
        as13[30] = "BombGunNull 1";
        as13[32] = "RocketGunAIM_9B 1";
        as13[33] = "BombGunNull 1";
        as13[38] = "FuelTankGun_TankSkyhawk 1";
        Aircraft.weaponsRegister(class1, "2xAAMs_4xCBUs_1xDpt", as13);
        Aircraft.weaponsRegister(class1, "2xAAMs_2xDpt_6xMk81", new String[] {
            "MGunHispanoMkIki 200", "MGunHispanoMkIki 200", 0, 0, "FuelTankGun_TankSkyhawk 1", "FuelTankGun_TankSkyhawk 1", 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, "RocketGunAIM_9B 1", 
            "BombGunNull 1", 0, "RocketGunAIM_9B 1", "BombGunNull 1", 0, 0, 0, 0, 0, 0, 
            0, "BombGunMk81 1", "BombGunMk81 1", "BombGunMk81 1", "BombGunMk81 1", "BombGunMk81 1", "BombGunMk81 1"
        });
        Aircraft.weaponsRegister(class1, "2xAAMs_2xDpt_6xMk82", new String[] {
            "MGunHispanoMkIki 200", "MGunHispanoMkIki 200", 0, 0, "FuelTankGun_TankSkyhawk 1", "FuelTankGun_TankSkyhawk 1", 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, "RocketGunAIM_9B 1", 
            "BombGunNull 1", 0, "RocketGunAIM_9B 1", "BombGunNull 1", 0, 0, 0, 0, 0, 0, 
            0, "BombGunMk81 1", "BombGunMk81 1", "BombGunMk81 1", "BombGunMk81 1", "BombGunMk81 1", "BombGunMk81 1"
        });
        Aircraft.weaponsRegister(class1, "2xAAMs_2xDpt_6xSnakeEyes", new String[] {
            "MGunHispanoMkIki 200", "MGunHispanoMkIki 200", 0, 0, "FuelTankGun_TankSkyhawk 1", "FuelTankGun_TankSkyhawk 1", 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, "RocketGunAIM_9B 1", 
            "BombGunNull 1", 0, "RocketGunAIM_9B 1", "BombGunNull 1", 0, 0, 0, 0, 0, 0, 
            0, "BombGunMk82SnakeEye 1", "BombGunMk82SnakeEye 1", "BombGunMk82SnakeEye 1", "BombGunMk82SnakeEye 1", "BombGunMk82SnakeEye 1", "BombGunMk82SnakeEye 1"
        });
        Aircraft.weaponsRegister(class1, "2xAAMs_2xDpt_6xCBUs", new String[] {
            "MGunHispanoMkIki 200", "MGunHispanoMkIki 200", 0, 0, "FuelTankGun_TankSkyhawk 1", "FuelTankGun_TankSkyhawk 1", 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, "RocketGunAIM_9B 1", 
            "BombGunNull 1", 0, "RocketGunAIM_9B 1", "BombGunNull 1", 0, 0, 0, 0, 0, 0, 
            0, "BombGunCBU24 1", "BombGunCBU24 1", "BombGunCBU24 1", "BombGunCBU24 1", "BombGunCBU24 1", "BombGunCBU24 1"
        });
        Aircraft.weaponsRegister(class1, "3xMk81_2xDpt", new String[] {
            "MGunHispanoMkIki 200", "MGunHispanoMkIki 200", 0, 0, "FuelTankGun_TankSkyhawk 1", "FuelTankGun_TankSkyhawk 1", 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, "BombGunMk81 1", 0, "BombGunMk81 1", "BombGunMk81 1"
        });
        Aircraft.weaponsRegister(class1, "3xMk82_2xDpt", new String[] {
            "MGunHispanoMkIki 200", "MGunHispanoMkIki 200", 0, 0, "FuelTankGun_TankSkyhawk 1", "FuelTankGun_TankSkyhawk 1", 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, "BombGunMk82 1", 0, "BombGunMk82 1", "BombGunMk82 1"
        });
        Aircraft.weaponsRegister(class1, "3xSnakeEyes_2xDpt", new String[] {
            "MGunHispanoMkIki 200", "MGunHispanoMkIki 200", 0, 0, "FuelTankGun_TankSkyhawk 1", "FuelTankGun_TankSkyhawk 1", 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, "BombGunMk82SnakeEye 1", 0, "BombGunMk82SnakeEye 1", "BombGunMk82SnakeEye 1"
        });
        String as14[] = new String[47];
        as14[0] = "MGunHispanoMkIki 200";
        as14[1] = "MGunHispanoMkIki 200";
        as14[4] = "FuelTankGun_TankSkyhawk 1";
        as14[5] = "FuelTankGun_TankSkyhawk 1";
        as14[33] = "BombGun500lbsE 1";
        Aircraft.weaponsRegister(class1, "1x500lb_2xDpt", as14);
        Aircraft.weaponsRegister(class1, "none", new String[47]);
    }
}
