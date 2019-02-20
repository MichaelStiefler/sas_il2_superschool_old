package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.objects.weapons.BombGunCBU24_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawk400gal_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawkNF_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawk_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU10_Cap_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU10_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU118_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU130_Cap_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU131_Cap_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU7_gn16;
import com.maddox.il2.objects.weapons.Pylon_Mk4HIPEGpod_gn16;
import com.maddox.il2.objects.weapons.Pylon_USMERfw_gn16;
import com.maddox.il2.objects.weapons.Pylon_USMERmd_gn16;
import com.maddox.il2.objects.weapons.Pylon_USTER_gn16;
import com.maddox.il2.objects.weapons.RocketGunChaff_gn16;
import com.maddox.il2.objects.weapons.RocketGunFlare_gn16;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class SkyhawkA4F_late extends SkyhawkFuelReceiver
    implements TypeGuidedMissileCarrier, TypeCountermeasure
{

    public SkyhawkA4F_late()
    {
        bChangedPit = false;
        guidedMissileUtils = null;
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        counterFlareList = new ArrayList();
        counterChaffList = new ArrayList();
        guidedMissileUtils = new GuidedMissileUtils(this);
        bHasLAUcaps = false;
    }

    private void checkChangeWeaponColors()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
        {
            if(FM.CT.Weapons[i] == null)
                continue;
            for(int j = 0; j < FM.CT.Weapons[i].length; j++)
            {
                if(FM.CT.Weapons[i][j] instanceof Pylon_USTER_gn16)
                    ((Pylon_USTER_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_USMERfw_gn16)
                    ((Pylon_USMERfw_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_USMERmd_gn16)
                    ((Pylon_USMERmd_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_gn16)
                    ((Pylon_LAU10_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                    ((Pylon_LAU10_Cap_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_LAU7_gn16)
                    ((Pylon_LAU7_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_LAU118_gn16)
                    ((Pylon_LAU118_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof BombGunCBU24_gn16)
                    ((BombGunCBU24_gn16)FM.CT.Weapons[i][j]).matGray();
                else
                if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk_gn16)
                    ((FuelTankGun_TankSkyhawk_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkNF_gn16)
                    ((FuelTankGun_TankSkyhawkNF_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk400gal_gn16)
                    ((FuelTankGun_TankSkyhawk400gal_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_Mk4HIPEGpod_gn16)
                    ((Pylon_Mk4HIPEGpod_gn16)FM.CT.Weapons[i][j]).matHighvis();
                if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                    bHasLAUcaps = true;
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16)
                    bHasLAUcaps = true;
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16)
                    bHasLAUcaps = true;
                if(FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16)
                {
                    counterFlareList.add(FM.CT.Weapons[i][j]);
                    continue;
                }
                if(FM.CT.Weapons[i][j] instanceof RocketGunChaff_gn16)
                    counterChaffList.add(FM.CT.Weapons[i][j]);
            }

        }

    }

    private void checkDeleteLAUcaps()
    {
        if(FM.CT.saveWeaponControl[2])
        {
            for(int i = 0; i < FM.CT.Weapons.length; i++)
            {
                if(FM.CT.Weapons[i] == null)
                    continue;
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                    {
                        ((Pylon_LAU10_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                        continue;
                    }
                    if(FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16)
                    {
                        ((Pylon_LAU130_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                        continue;
                    }
                    if(FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16)
                        ((Pylon_LAU131_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                }

            }

            bHasLAUcaps = false;
        }
    }

    public void backFire()
    {
        if(counterFlareList.isEmpty())
            hasFlare = false;
        else
        if(Time.current() > lastFlareDeployed + 700L)
        {
            ((RocketGunFlare_gn16)counterFlareList.get(0)).shots(1);
            hasFlare = true;
            lastFlareDeployed = Time.current();
            if(!((RocketGunFlare_gn16)counterFlareList.get(0)).haveBullets())
                counterFlareList.remove(0);
        }
        if(counterChaffList.isEmpty())
            hasChaff = false;
        else
        if(Time.current() > lastChaffDeployed + 1300L)
        {
            ((RocketGunChaff_gn16)counterChaffList.get(0)).shots(1);
            hasChaff = true;
            lastChaffDeployed = Time.current();
            if(!((RocketGunChaff_gn16)counterChaffList.get(0)).haveBullets())
                counterChaffList.remove(0);
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

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
    }

    public void update(float f)
    {
        if(bHasLAUcaps)
            checkDeleteLAUcaps();
        super.update(f);
        guidedMissileUtils.update();
        if(this.backfire)
            backFire();
    }

    public void missionStarting()
    {
        super.missionStarting();
        checkChangeWeaponColors();
    }

    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(byte byte0)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
        }
        catch(Exception exception)
        {
            System.out.println("Weapon register error - SkyhawkA4F_late : Default loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    public boolean bChangedPit;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private ArrayList counterFlareList;
    private ArrayList counterChaffList;
    private boolean bHasLAUcaps;

    static 
    {
        Class class1 = SkyhawkA4F_late.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Skyhawk");
        Property.set(class1, "meshName", "3DO/Plane/SkyhawkA4F(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1967F);
        Property.set(class1, "yearExpired", 1985F);
        Property.set(class1, "FlightModel", "FlightModels/a4f_P408.fmd:SKYHAWKS");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitSkyhawkA4F.class
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
            "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev01", 
            "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb01", "_ExternalBomb06", 
            "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_Bomb13", "_ExternalBomb14", "_Bomb15", "_ExternalBomb16", 
            "_Bomb17", "_ExternalBomb18", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", 
            "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", 
            "_ExternalDev12", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_Rock09", "_Rock10", 
            "_Rock11", "_Rock12", "_Rock13", "_Rock14", "_Rock15", "_Rock16", "_Rock17", "_Rock18", "_Rock19", "_Rock20", 
            "_Rock21", "_Rock22", "_Rock23", "_Rock24", "_Rock25", "_Rock26", "_Rock27", "_Rock28", "_ExternalRock43", "_ExternalRock43", 
            "_ExternalRock29", "_ExternalRock29", "_ExternalRock30", "_ExternalRock30", "_ExternalRock31", "_ExternalRock31", "_ExternalRock32", "_ExternalRock32", "_ExternalRock33", "_ExternalRock33", 
            "_ExternalRock34", "_ExternalRock34", "_ExternalRock35", "_ExternalRock35", "_ExternalRock36", "_ExternalRock36", "_ExternalRock37", "_ExternalRock37", "_ExternalRock38", "_ExternalRock38", 
            "_ExternalRock39", "_ExternalRock39", "_ExternalRock40", "_ExternalRock40", "_Rock41", "_Rock42", "_Bomb19", "_Bomb20", "_ExternalBomb21", "_ExternalBomb22", 
            "_Bomb23", "_ExternalBomb24", "_ExternalBomb25", "_Bomb26", "_ExternalBomb27", "_ExternalBomb28", "_Flare01", "_Flare02", "_Chaff01"
        });
    }
}
