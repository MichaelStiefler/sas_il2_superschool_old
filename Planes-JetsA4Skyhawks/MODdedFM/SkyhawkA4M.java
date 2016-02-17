// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 17/10/2015 02:32:17 p.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SkyhawkA4M.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3f;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
//import com.maddox.il2.objects.weapons.RocketAIM9BUtils;
//import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import java.io.IOException;
import java.util.ArrayList;
import com.maddox.util.HashMapInt;

// Referenced classes of package com.maddox.il2.objects.air:
//            Skyhawk, TypeAIM9Carrier, TypeStormovik, TypeBNZFighter, 
//            TypeFighterAceMaker, PaintSchemeFMPar05, Aircraft, NetAircraft, 
//            Cockpit, TypeGSuit

public class SkyhawkA4M extends Skyhawk
    implements /*TypeAIM9Carrier, */ TypeGuidedMissileCarrier, TypeStormovik, TypeBNZFighter, TypeFighterAceMaker
{

    public SkyhawkA4M()
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
        //By PAL
        guidedMissileUtils = new GuidedMissileUtils(this); //new RocketAIM9BUtils(this);
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
		//By PAL
        if(super.thisWeaponsName.compareToIgnoreCase("default") == 0 || super.thisWeaponsName.compareToIgnoreCase("none") == 0)
        {
        	hierMesh().chunkVisible("PylonCenter_D0", false);        	
        	hierMesh().chunkVisible("PylonL1_D0", false);
        	hierMesh().chunkVisible("PylonR1_D0", false);
        	hierMesh().chunkVisible("PylonL2_D0", false);
        	hierMesh().chunkVisible("PylonR2_D0", false);        	        	
        }			        
        if(super.thisWeaponsName.startsWith("3xMk81") || super.thisWeaponsName.startsWith("3xMk82") || super.thisWeaponsName.startsWith("3xSnakeEyes") || super.thisWeaponsName.endsWith("6xMk81") || super.thisWeaponsName.endsWith("6xMk82") || super.thisWeaponsName.endsWith("6xSnakeEyes") || super.thisWeaponsName.endsWith("6xCBUs"))
            hierMesh().chunkVisible("PylonCenter2", true);
        else
            return;
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
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
    //By PAL
    //private RocketAIM9BUtils guidedMissileUtils;
    private GuidedMissileUtils guidedMissileUtils;
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
        Property.set(class1, "meshName", "3DO/Plane/SkyhawkA4M(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/a4m.fmd:SKYHAWKS"); //"FlightModels/a4m.fmd:Skyhawkv1_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitSkyhawkA4M.class
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
        
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 47; //By PAL, error 86;
            
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            String s = "default";            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);

            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            s = "2xAAMs";
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);        

	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        s = "2xDpt ";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
	        s = "6xMk81_8xZunis_1xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
	        s = "6xMk82_8xZunis_1xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
	        s = "6xSnakeEyes_8xZunis_1xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
	        s = "4xCBUs_8xZunis_1xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
	        a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
	        a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
	        a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
	        s = "4xMk83_1xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
	        a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
	        s = "2xMk83_8xZunis_1xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        s = "8xZunis_6xMk81_2xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        s = "8xZunis_6xMk82_2xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        s = "8xZunis_6xSnakeEyes_2xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "RocketGun5inchZuni", 1);
	        a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "RocketGun5inchZuni", 1);
	        a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "Pylon_Zuni", 1);
	        a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        s = "8xZunis_6xCBUs_2xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "PylonSUU11", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "PylonSUU11", 1);
	        a_lweaponslot[16] = new Aircraft._WeaponSlot(0, "MGunMiniGun", 1);
	        a_lweaponslot[17] = new Aircraft._WeaponSlot(0, "MGunMiniGun", 1);
	        a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
	        a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
	        a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
	        s = "2xAAMs_2xMinigun_1xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
	        a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
	        s = "2xAAMs_2xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
	        a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
	        a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
	        s = "2xAAMs_6xMk81_1xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
	        a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
	        a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
	        s = "2xAAMs_6xMk82_1xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
	        a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
	        a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
	        s = "2xAAMs_6xSnakeEyes_1xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
	        a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
	        a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
	        a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
	        s = "2xAAMs_4xCBUs_1xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
	        a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
	        a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        s = "2xAAMs_2xDpt_6xMk81";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
	        a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
	        a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        s = "2xAAMs_2xDpt_6xMk82";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
	        a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
	        a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        s = "2xAAMs_2xDpt_6xSnakeEyes";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
	        a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
	        a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "BombGunNull", 1);
	        a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);	        
	        a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunCBU24", 1);
	        s = "2xAAMs_2xDpt_6xCBUs";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
	        s = "4xMk81_2xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
	        a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);	        
	        s = "4xMk82_2xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);	        
	        a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
	        s = "4xSnakeEyes_2xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];
	        a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
	        a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk", 1);
	        a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "BombGun500lbsE", 1);
	        s = "1x500lb_2xDpt";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
	
	        a_lweaponslot = new Aircraft._WeaponSlot[byte0];		
	        s = "none";
	        arraylist.add(s);
	        hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}
