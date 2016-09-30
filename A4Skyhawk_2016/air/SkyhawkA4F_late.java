
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3f;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Skyhawk, TypeTankerDrogue, TypeDockable, Aircraft, 
//            PaintSchemeFMPar05, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, 
//            NetAircraft, Cockpit, TypeGSuit

public class SkyhawkA4F_late extends SkyhawkFuelReceiver
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

    public SkyhawkA4F_late()
    {
        bChangedPit = false;
        guidedMissileUtils = null;
        trgtPk = 0.0F;
        trgtAI = null;
        bToFire = false;
        trgtPk = 0.0F;
        trgtAI = null;
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
        bHasLAUcaps = false;
    }

    private void checkChangeWeaponColors()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j] instanceof Pylon_USTER_gn16)
                        ((Pylon_USTER_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_USMERfw_gn16)
                        ((Pylon_USMERfw_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_gn16)
                        ((Pylon_LAU10_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                        ((Pylon_LAU10_Cap_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU7_gn16)
                        ((Pylon_LAU7_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU118_gn16)
                        ((Pylon_LAU118_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof BombGunCBU24_gn16)
                        ((BombGunCBU24_gn16)FM.CT.Weapons[i][j]).matGray();
                    else if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk_gn16)
                        ((FuelTankGun_TankSkyhawk_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkNF_gn16)
                        ((FuelTankGun_TankSkyhawkNF_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk400gal_gn16)
                        ((FuelTankGun_TankSkyhawk400gal_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_Mk4HIPEGpod_gn16)
                        ((Pylon_Mk4HIPEGpod_gn16)FM.CT.Weapons[i][j]).matHighvis();

                    if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                        bHasLAUcaps = true;
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16)
                        bHasLAUcaps = true;
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16)
                        bHasLAUcaps = true;
                }
            }
    }

    private void checkDeleteLAUcaps()
    {
        if(FM.CT.saveWeaponControl[2])
        {
            for(int i = 0; i < FM.CT.Weapons.length; i++)
                if(FM.CT.Weapons[i] != null)
                {
                    for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                        if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                            ((Pylon_LAU10_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                        else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16)
                            ((Pylon_LAU130_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                        else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16)
                            ((Pylon_LAU131_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                }
            bHasLAUcaps = false;
        }
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

    public Actor getMissileTarget()
    {
        return guidedMissileUtils.getMissileTarget();
    }

    public Point3f getMissileTargetOffset()
    {
        return guidedMissileUtils.getSelectedActorOffset();
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

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void update(float f)
    {
        if(bHasLAUcaps)
            checkDeleteLAUcaps();
        super.update(f);
        guidedMissileUtils.update();
    }

    public void missionStarting()
    {
        super.missionStarting();

        checkChangeWeaponColors();
    }

    static Class _mthclass$(String x0)
    {
        try
        {
            return Class.forName(x0);
        }
        catch(ClassNotFoundException x1)
        {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private float llpos;
    public boolean bChangedPit;
    public boolean bToFire;
    private float trgtPk;
    private Actor trgtAI;
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
    private boolean bHasLAUcaps;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Skyhawk");
        Property.set(class1, "meshName", "3DO/Plane/SkyhawkA4F(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1967F);
        Property.set(class1, "yearExpired", 1985F);
        Property.set(class1, "FlightModel", "FlightModels/a4f_P408.fmd:SKYHAWKS");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitSkyhawkA4F.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 
            9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 
            9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 7, 7, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01",        "_CANNON02",        "_MGUN01",          "_MGUN02",          "_ExternalDev01",   "_ExternalDev02",   "_ExternalDev03",   "_ExternalDev04",   "_ExternalDev05",   "_ExternalDev01",
            "_ExternalDev02",   "_ExternalDev03",   "_ExternalDev04",   "_ExternalDev05",   "_ExternalBomb02",  "_ExternalBomb03",  "_ExternalBomb04",  "_ExternalBomb05",  "_ExternalBomb01",  "_ExternalBomb06",
            "_ExternalBomb07",  "_ExternalBomb08",  "_ExternalBomb09",  "_ExternalBomb10",  "_ExternalBomb11",  "_ExternalBomb12",  "_Bomb13",          "_ExternalBomb14",  "_Bomb15",          "_ExternalBomb16",
            "_Bomb17",          "_ExternalBomb18",  "_ExternalRock01",  "_ExternalRock01",  "_ExternalRock02",  "_ExternalRock02",  "_ExternalRock03",  "_ExternalRock03",  "_ExternalRock04",  "_ExternalRock04",
            "_ExternalDev06",   "_ExternalDev07",   "_ExternalDev08",   "_ExternalDev09",   "_ExternalDev06",   "_ExternalDev07",   "_ExternalDev08",   "_ExternalDev09",   "_ExternalDev10",   "_ExternalDev11",
            "_ExternalDev12",   "_ExternalDev10",   "_ExternalDev11",   "_ExternalDev12",   "_Rock05",          "_Rock06",          "_Rock07",          "_Rock08",          "_Rock09",          "_Rock10",
            "_Rock11",          "_Rock12",          "_Rock13",          "_Rock14",          "_Rock15",          "_Rock16",          "_Rock17",          "_Rock18",          "_Rock19",          "_Rock20",
            "_Rock21",          "_Rock22",          "_Rock23",          "_Rock24",          "_Rock25",          "_Rock26",          "_Rock27",          "_Rock28",          "_ExternalRock43",  "_ExternalRock43",
            "_ExternalRock29",  "_ExternalRock29",  "_ExternalRock30",  "_ExternalRock30",  "_ExternalRock31",  "_ExternalRock31",  "_ExternalRock32",  "_ExternalRock32",  "_ExternalRock33",  "_ExternalRock33",
            "_ExternalRock34",  "_ExternalRock34",  "_ExternalRock35",  "_ExternalRock35",  "_ExternalRock36",  "_ExternalRock36",  "_ExternalRock37",  "_ExternalRock37",  "_ExternalRock38",  "_ExternalRock38",
            "_ExternalRock39",  "_ExternalRock39",  "_ExternalRock40",  "_ExternalRock40",  "_Rock41",          "_Rock42",          "_Bomb19",          "_Bomb20",          "_ExternalBomb21",  "_ExternalBomb22",
            "_Bomb23",          "_ExternalBomb24",  "_ExternalBomb25",  "_Bomb26",          "_ExternalBomb27",  "_ExternalBomb28",  "_Flare01",         "_Flare02",         "_Chaff01"
        });
        String s = "";
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 119;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9D";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9D+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9D+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM117+2xZuni+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk81+2xZuni+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82+2xZuni+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82HD+2xZuni+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xCBU24+2xZuni+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xCBU24+2xLAU3+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 19);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 19);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83+2xZuni+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniAP_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniAP_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk77Napalm+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk77Napalm+2xLAU3+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 19);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 19);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77Napalm+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77Napalm+2xLAU32+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 7);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 7);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xZuni+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xZuni+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xLAU3+2xLAU32+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_TC_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_TC_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_Cap_gn16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 7);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 7);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 19);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 19);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 19);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 19);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xLAU3+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 19);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 19);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 19);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 19);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 19);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk81+2xZuni+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+2xZuni+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+2xZuni+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xCBU24+2xZuni+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk81+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82HD+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xCBU24+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xM117+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xM117+2x300Dt+Flare";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xMk83+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xMk84+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk77Napalm+2xMk82+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk77Napalm+2xLAU3+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 19);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 19);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xHIPEG+2xAIM9D";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm4000rpm", 750);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm4000rpm", 750);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xHIPEG+2xAIM9D+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm4000rpm", 750);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm4000rpm", 750);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xHIPEG+2xLAU32+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm4000rpm", 750);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm4000rpm", 750);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_TC_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_TC_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_Cap_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 7);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 7);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk81+2xAIM9D+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82+2xAIM9D+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82HD+2xAIM9D+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xCBU24+2xAIM9D+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk81+2xAIM9D+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+2xAIM9D+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+2xAIM9D+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xCBU24+2xAIM9D+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk81+2x300Dt+Flare";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+2x300Dt+Flare";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+2x300Dt+Flare";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xCBU24+2x300Dt+Flare";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "16xMk81+2xAIM9D";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[110] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[111] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[112] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[113] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[114] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[115] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "16xMk81+2xFlare";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[110] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[111] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[112] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[113] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[114] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[115] = new Aircraft._WeaponSlot(3, "BombGunMk81_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM12C+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM12E+2xLAU32+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_TC_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_TC_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_Cap_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "RocketGunAGM12E_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(4, "RocketGunAGM12E_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 7);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 7);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xB28EXnuke70kt+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunB28EXnuke70kt_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xB28REnuke70kt+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunB28REnuke70ktpara_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xB43nuke70kt+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunB43nuke70kt_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xB57nuke5ktpara+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunB57nuke5ktpara_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - SkyhawkA4F_late : " + s);
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}