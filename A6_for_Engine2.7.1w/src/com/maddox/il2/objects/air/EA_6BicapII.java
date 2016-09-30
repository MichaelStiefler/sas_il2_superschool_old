
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


public class EA_6BicapII extends A_6fuelReceiver
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeAcePlane
{

    public EA_6BicapII()
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
        backfireList = new ArrayList();
        backfire = false;
        ratdeg = 0F;
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

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        FM.Skill = 3;
        super.fuelReceiveRate = 10.093F;
        if(thisWeaponsName.endsWith("__ALQ3"))
        {
            hierMesh().chunkVisible("ALQ99Body_1", true);
            hierMesh().chunkVisible("ALQ99prop_1", true);
            hierMesh().chunkVisible("ALQ99Body_3", true);
            hierMesh().chunkVisible("ALQ99prop_3", true);
            hierMesh().chunkVisible("ALQ99Body_5", true);
            hierMesh().chunkVisible("ALQ99prop_5", true);
            FM.M.massEmpty += 482F * 3F;
            FM.Sq.dragProducedCx += 0.028F * 3F;
        }
        if(thisWeaponsName.endsWith("__ALQ4"))
        {
            hierMesh().chunkVisible("ALQ99Body_1", true);
            hierMesh().chunkVisible("ALQ99prop_1", true);
            hierMesh().chunkVisible("ALQ99Body_2", true);
            hierMesh().chunkVisible("ALQ99prop_2", true);
            hierMesh().chunkVisible("ALQ99Body_4", true);
            hierMesh().chunkVisible("ALQ99prop_4", true);
            hierMesh().chunkVisible("ALQ99Body_5", true);
            hierMesh().chunkVisible("ALQ99prop_5", true);
            FM.M.massEmpty += 482F * 4F;
            FM.Sq.dragProducedCx += 0.028F * 4F;
        }
        if(thisWeaponsName.endsWith("__ALQ5"))
        {
            hierMesh().chunkVisible("ALQ99Body_1", true);
            hierMesh().chunkVisible("ALQ99prop_1", true);
            hierMesh().chunkVisible("ALQ99Body_2", true);
            hierMesh().chunkVisible("ALQ99prop_2", true);
            hierMesh().chunkVisible("ALQ99Body_3", true);
            hierMesh().chunkVisible("ALQ99prop_3", true);
            hierMesh().chunkVisible("ALQ99Body_4", true);
            hierMesh().chunkVisible("ALQ99prop_4", true);
            hierMesh().chunkVisible("ALQ99Body_5", true);
            hierMesh().chunkVisible("ALQ99prop_5", true);
            FM.M.massEmpty += 482F * 5F;
            FM.Sq.dragProducedCx += 0.028F * 5F;
        }
        if(!thisWeaponsName.startsWith("empty"))
        {
            hasChaff = true;
            hasFlare = true;
        }
    }

    public void update(float f)
    {
        guidedMissileUtils.update();

        if(FM.getSpeedKMH() > 185F)
            RATrot();

        super.update(f);
        if(super.backfire)
            backFire();
    }

    public void missionStarting()
    {
        super.missionStarting();
    }

    void RATrot()
    {
        if(FM.getSpeedKMH() < 250F)
            ratdeg += 10F;
        else if(FM.getSpeedKMH() < 400F)
            ratdeg += 20F;
        else if(FM.getSpeedKMH() < 550F)
            ratdeg += 25F;
        else
            ratdeg += 31F;
        if(ratdeg > 720F) ratdeg -= 1440F;
        for(int i=1; i<6; i++)
        {
            hierMesh().chunkSetAngles("ALQ99prop_" + i, 0.0F, 0.0F, ratdeg);

            if(hierMesh().isChunkVisible("ALQ99Body_"+ i))
            {
                if(FM.getSpeedKMH() > 300F)
                {
                    hierMesh().chunkVisible("ALQ99proprot_" + i, true);
                    hierMesh().chunkVisible("ALQ99prop_" + i, false);
                }
                else
                {
                    hierMesh().chunkVisible("ALQ99proprot_" + i, false);
                    hierMesh().chunkVisible("ALQ99prop_" + i, true);
                }
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
    public boolean bToFire;
    private long tX4Prev;
    private ArrayList missilesList;
    private ArrayList backfireList;
    private boolean backfire;
//     public BulletEmitter Weapons[][];
    private float deltaAzimuth;
    private float deltaTangage;
    private float ratdeg;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "EA-6B");
        Property.set(class1, "meshName", "3DO/Plane/EA-6B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1985F);
        Property.set(class1, "yearExpired", 2016F);
        Property.set(class1, "FlightModel", "FlightModels/EA6BicapII.fmd:A6");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitEA_6B.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 
            2, 2, 2, 2, 7, 7, 8, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_ExternalDev01",  "_ExternalDev02",  "_ExternalDev03",  "_ExternalDev04",  "_ExternalDev05",  "_ExternalDev06",  "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02",
            "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_Flare01",        "_Flare02",        "_Chaff01",        "_Chaff02"
        });
        String s = "";
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            char c = 18;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[c];
            s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[14] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(7, "RocketGunFlareNEW", 20);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(8, "RocketGunChaff", 20);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(8, "RocketGunChaff", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x330galDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3x330galDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5x330galDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x330galDroptank + 4x AN_ALQ99__ALQ4";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x330galDroptank + 3x AN_ALQ99__ALQ3";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5x AN_ALQ99__ALQ5";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF100_Outboard", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "empty";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - EA_6BicapII : " + s);
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}