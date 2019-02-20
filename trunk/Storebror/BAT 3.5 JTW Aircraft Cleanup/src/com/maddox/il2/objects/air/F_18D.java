package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.objects.weapons.RocketGunAIM9L_gn16;
import com.maddox.rts.Property;

public class F_18D extends F_18
{

    public F_18D()
    {
        bulletEmitters = null;
        wingFoldValue = 0.0F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        bulletEmitters = new BulletEmitter[weaponHookArray.length];
        for(int i = 0; i < weaponHookArray.length; i++)
            bulletEmitters[i] = getBulletEmitterByHookName(weaponHookArray[i]);

    }

    public void update(float f)
    {
        computeF404_GE400_AB();
        super.update(f);
    }

    private void computeF404_GE400_AB()
    {
        float f = Aircraft.cvt(calculateMach(), 1.0F, 1.75F, 1.0F, 0.4F);
        float f1 = Aircraft.cvt(FM.getAltitude(), 16500F, 18000F, 1.0F, 0.4F);
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() > 5)
            FM.producedAF.x += 19200D * (double)f * (double)f1;
        if(FM.EI.engines[1].getThrustOutput() > 1.001F && FM.EI.engines[1].getStage() > 5)
            FM.producedAF.x += 19200D * (double)f * (double)f1;
    }

    public void missionStarting()
    {
        super.missionStarting();
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
    }

    public void updateHook()
    {
        for(int i = 0; i < weaponHookArray.length; i++)
            try
            {
                if(bulletEmitters[i] instanceof RocketGunAIM9L_gn16)
                    ((RocketGunAIM9L_gn16)bulletEmitters[i]).updateHook(weaponHookArray[i]);
            }
            catch(Exception exception) { }

    }

    public void moveWingFold(float f)
    {
        moveWingFold(hierMesh(), f);
        super.moveWingFold(f);
        if(wingFoldValue != f)
        {
            wingFoldValue = f;
            this.needUpdateHook = true;
        }
    }

    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(byte byte0)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
        }
        catch(Exception exception)
        {
            System.out.println("Weapon register error - F_18D : Default loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] GenerateCenterPylonConfig(byte byte0)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_SUU62_F18C_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
        }
        catch(Exception exception)
        {
            System.out.println("Weapon register error - F_18D : Center Pylon loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] GenerateCenterTankConfig(byte byte0)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_SUU62_F18C_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
        }
        catch(Exception exception)
        {
            System.out.println("Weapon register error - F_18D : Center Tank loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] GenerateNoPylonConfig(byte byte0)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
        }
        catch(Exception exception)
        {
            System.out.println("Weapon register error - F_18D : No Pylon loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static String weaponHookArray[] = {
        "_CANNON01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", 
        "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", "_ExternalBomb01", 
        "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", 
        "_ExternalBomb12", "_ExternalBomb13", "_ExMis01", "_ExMis01", "_ExMis02", "_ExMis02", "_ExMis03", "_ExFLIR", "_ExMis04", "_ExLASER", 
        "_ExternalMis05", "_ExternalMis05", "_ExternalMis06", "_ExternalMis06", "_ExternalMis07", "_ExternalMis07", "_ExternalMis08", "_ExternalMis08", "_ExternalMis09", "_ExternalMis09", 
        "_ExternalMis10", "_ExternalMis10", "_ExternalMis11", "_ExternalMis11", "_ExternalMis12", "_ExternalMis12", "_ExternalMis13", "_ExternalMis13", "_ExternalMis14", "_ExternalMis14", 
        "_ExternalMis15", "_ExternalMis15", "_ExternalMis16", "_ExternalMis16", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", 
        "_ExternalRock04", "_ExternalRock04", "_ExternalRock05", "_ExternalRock05", "_ExternalRock06", "_ExternalRock06", "_ExternalRock07", "_ExternalRock07", "_ExternalRock08", "_ExternalRock08", 
        "_ExternalRock09", "_ExternalRock09", "_ExternalRock10", "_ExternalRock10", "_ExternalRock11", "_ExternalRock11", "_ExternalRock12", "_ExternalRock12", "_Rock13", "_Rock14", 
        "_Rock15", "_Rock16", "_Rock17", "_Rock18", "_Rock19", "_Rock20", "_Rock21", "_Rock22", "_Rock23", "_Rock24", 
        "_Rock25", "_Rock26", "_Rock27", "_Rock28", "_Flare01", "_Flare02", "_Chaff01", "_Chaff02"
    };
    private BulletEmitter bulletEmitters[];
    private float wingFoldValue;

    static 
    {
        Class class1 = F_18D.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-18D");
        Property.set(class1, "meshName", "3DO/Plane/F-18D/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1989F);
        Property.set(class1, "yearExpired", 2050F);
        Property.set(class1, "FlightModel", "FlightModels/F-18D.fmd:F18_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitF_18C.class, CockpitF18FLIR.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 
            9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 7, 7, 8, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", 
            "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", "_ExternalBomb01", 
            "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", 
            "_ExternalBomb12", "_ExternalBomb13", "_ExMis01", "_ExMis01", "_ExMis02", "_ExMis02", "_ExMis03", "_ExFLIR", "_ExMis04", "_ExLASER", 
            "_ExternalMis05", "_ExternalMis05", "_ExternalMis06", "_ExternalMis06", "_ExternalMis07", "_ExternalMis07", "_ExternalMis08", "_ExternalMis08", "_ExternalMis09", "_ExternalMis09", 
            "_ExternalMis10", "_ExternalMis10", "_ExternalMis11", "_ExternalMis11", "_ExternalMis12", "_ExternalMis12", "_ExternalMis13", "_ExternalMis13", "_ExternalMis14", "_ExternalMis14", 
            "_ExternalMis15", "_ExternalMis15", "_ExternalMis16", "_ExternalMis16", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", 
            "_ExternalRock04", "_ExternalRock04", "_ExternalRock05", "_ExternalRock05", "_ExternalRock06", "_ExternalRock06", "_ExternalRock07", "_ExternalRock07", "_ExternalRock08", "_ExternalRock08", 
            "_ExternalRock09", "_ExternalRock09", "_ExternalRock10", "_ExternalRock10", "_ExternalRock11", "_ExternalRock11", "_ExternalRock12", "_ExternalRock12", "_Rock13", "_Rock14", 
            "_Rock15", "_Rock16", "_Rock17", "_Rock18", "_Rock19", "_Rock20", "_Rock21", "_Rock22", "_Rock23", "_Rock24", 
            "_Rock25", "_Rock26", "_Rock27", "_Rock28", "_Flare01", "_Flare02", "_Chaff01", "_Chaff02"
        });
    }
}
