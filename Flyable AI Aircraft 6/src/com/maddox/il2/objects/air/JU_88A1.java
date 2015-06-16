// This file is part of the SAS IL-2 Sturmovik 1946 4.13
// Flyable AI Aircraft Mod package.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Original file source: 1C/Maddox/TD
// Modified by: SAS - Special Aircraft Services
//              www.sas1946.com
//
// Last Edited by: SAS~Storebror
// Last Edited on: 2014/12/11

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.HUD;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class JU_88A1 extends JU_88DivebomberLotfe7C
    implements TypeScout
{

    public JU_88A1()
    {
        needsToOpenBombays = false;
        iRust = 1;
    }

    public void blisterRemoved(int i)
    {
        float f = FM.getAltitude() - Landscape.HQ_Air((float)FM.Loc.x, (float)FM.Loc.y);
        if(f < 0.3F)
        {
            if(!topBlisterRemoved)
                doRemoveTopBlister();
        } else
        if(i == 2 || i == 3 || i == 4)
        {
            if(!blisterRemoved)
                doRemoveBlister1();
        } else
        if(i == 1)
        {
            hierMesh().chunkVisible("Pilot4_D0", false);
            hierMesh().chunkVisible("Turret3B_D0", false);
            hierMesh().chunkVisible("Turret3C_D0", false);
        }
    }

    protected void doRemoveBlister1()
    {
        blisterRemoved = true;
        doWreck("Blister1_D0");
        hierMesh().chunkVisible("Turret3B_D0", false);
        hierMesh().chunkVisible("Turret3C_D0", false);
    }

    protected void doRemoveTopBlister()
    {
        topBlisterRemoved = true;
        hierMesh().chunkVisible("Turret2B_D0", false);
        hierMesh().chunkVisible("Turret2C_D0", false);
        doWreck("BlisterTop_D0");
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        float f = 0.0F;
        float f1 = 0.0F;
        needsToOpenBombays = false;
        if(thisWeaponsName.endsWith("RustA"))
        {
            iRust = 1;
            if(thisWeaponsName.endsWith("2xTank900L_RustA"))
                f1 += 45F;
            if(thisWeaponsName.startsWith("28x"))
                needsToOpenBombays = true;
        } else
        if(thisWeaponsName.endsWith("RustB"))
        {
            iRust = 2;
            f1 += 60F;
            if(!thisWeaponsName.endsWith("LiteRustB"))
                f += 900F;
            if(thisWeaponsName.startsWith("10x"))
                needsToOpenBombays = true;
        } else
        if(thisWeaponsName.endsWith("RustC"))
        {
            iRust = 3;
            FM.CT.bHasBayDoorControl = false;
            f1 += 120F;
            f1 += 45F;
            f += 1400F;
        } else
        if(thisWeaponsName.endsWith("RustF"))
        {
            iRust = 3;
            FM.CT.bHasBayDoorControl = false;
            f += 900F;
            f1 += 60F;
            f1 += 100F;
            if(thisWeaponsName.endsWith("xTank900L_RustF"))
            {
                f1 += 90F;
                if(thisWeaponsName.endsWith("2xTank900L_RustF"))
                    f1 += 30F;
            }
        }
        float f2 = FM.M.fuel / FM.M.maxFuel;
        FM.M.fuel += f2 * f;
        FM.M.maxFuel += f;
        FM.M.massEmpty += f1;
    }

    public boolean turretAngles(int i, float af[])
    {
        for(int j = 0; j < 2; j++)
        {
            af[j] = (af[j] + 3600F) % 360F;
            if(af[j] > 180F)
                af[j] -= 360F;
        }

        af[2] = 0.0F;
        boolean flag = true;
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        default:
            break;

        case 0: // '\0'
            if(bNavigatorUseBombsight)
            {
                flag = false;
                f = 0.0F;
                f1 = 0.0F;
                break;
            }
            if(f < -20F)
            {
                f = -20F;
                flag = false;
            }
            if(f > 20F)
            {
                f = 20F;
                flag = false;
            }
            if(f1 < -15F)
            {
                f1 = -15F;
                flag = false;
            }
            if(f1 > 20F)
            {
                f1 = 20F;
                flag = false;
            }
            break;

        case 1: // '\001'
            if(f < -40F)
            {
                f = -40F;
                flag = false;
            }
            if(f > 40F)
            {
                f = 40F;
                flag = false;
            }
            if(f1 < -5F)
            {
                f1 = -5F;
                flag = false;
            }
            if(f1 > 60F)
            {
                f1 = 60F;
                flag = false;
            }
            if(f > 30F)
            {
                if(f1 < cvt(f, 30F, 40F, -5F, 25F))
                    f1 = cvt(f, 30F, 40F, -5F, 25F);
                break;
            }
            if(f > 5.3F)
            {
                if(f1 < cvt(f, 5.3F, 25F, -1.72F, -5F))
                    f1 = cvt(f, 5.3F, 25F, -1.72F, -5F);
                break;
            }
            if(f > -5.3F)
            {
                if(f1 < -1.72F)
                    f1 = -1.72F;
                break;
            }
            if(f > -30F)
            {
                if(f1 < cvt(f, -30F, -5.3F, 5F, -1.72F))
                    f1 = cvt(f, -30F, -5.3F, 5F, -1.72F);
                break;
            }
            if(f1 < cvt(f, -40F, -30F, 25F, -5F))
                f1 = cvt(f, -40F, -30F, 25F, -5F);
            break;

        case 2: // '\002'
            if(f < -40F)
            {
                f = -40F;
                flag = false;
            }
            if(f > 40F)
            {
                f = 40F;
                flag = false;
            }
            if(f1 < -50F)
            {
                f1 = -50F;
                flag = false;
            }
            if(f1 > 0.0F)
            {
                f1 = 0.0F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected void moveBayDoor(float f)
    {
        if(!needsToOpenBombays && !FM.isPlayers())
            return;
        if(iRust != 3)
        {
            hierMesh().chunkSetAngles("Bay1_D0", 0.0F, 87F * f, 0.0F);
            hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -86F * f, 0.0F);
            hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 86F * f, 0.0F);
            hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -87F * f, 0.0F);
        }
        if(iRust == 1)
        {
            hierMesh().chunkSetAngles("Bay5_D0", 0.0F, 85F * f, 0.0F);
            hierMesh().chunkSetAngles("Bay6_D0", 0.0F, -85F * f, 0.0F);
            hierMesh().chunkSetAngles("Bay7_D0", 0.0F, 85F * f, 0.0F);
            hierMesh().chunkSetAngles("Bay8_D0", 0.0F, -85F * f, 0.0F);
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 4; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void update(float f)
    {
        updateJU87D5(f);
        updateJU87(f);
        super.update(f);
        if(Pitot.Indicator((float)FM.Loc.z, FM.getSpeed()) > 70F && (double)FM.CT.getFlap() > 0.01D && FM.CT.FlapsControl != 0.0F)
        {
            FM.CT.FlapsControl = 0.0F;
            World.cur();
            if(FM.actor == World.getPlayerAircraft())
                HUD.log("FlapsRaised");
        }
        float f1 = FM.EI.engines[0].getControlRadiator();
        if(f1 != 0.0F)
            hierMesh().chunkSetAngles("Radl11_D0", -30F * f1, 0.0F, 0.0F);
        f1 = FM.EI.engines[1].getControlRadiator();
        if(f1 != 0.0F)
            hierMesh().chunkSetAngles("Radr11_D0", -30F * f1, 0.0F, 0.0F);
    }

    protected void moveFlap(float f)
    {
        float f1 = -42.5F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -35F * f, 0.0F);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -20F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -20F * f, 0.0F);
    }

    protected void moveElevator(float f)
    {
        if(f > 0.0F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -40F * f, 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -40F * f, 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -25F * f, 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -25F * f, 0.0F);
        }
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Brake02_D0", 0.0F, -90F * f, 0.0F);
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 1: // '\001'
            FM.turret[0].setHealth(f);
            break;

        case 2: // '\002'
            FM.turret[1].setHealth(f);
            break;

        case 3: // '\003'
            FM.turret[2].setHealth(f);
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            hierMesh().chunkVisible("HMask2_D0", false);
            break;

        case 2: // '\002'
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            hierMesh().chunkVisible("HMask3_D0", false);
            break;

        case 3: // '\003'
            hierMesh().chunkVisible("Pilot4_D0", false);
            hierMesh().chunkVisible("Pilot4_D1", true);
            hierMesh().chunkVisible("HMask4_D0", false);
            break;
        }
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        if(regiment == null || regiment.country() == null)
            return "";
        if(regiment.country().equals(PaintScheme.countryHungary))
            return PaintScheme.countryHungary + "_";
        if(regiment.country().equals(PaintScheme.countryRomania))
            return PaintScheme.countryRomania + "_";
        else
            return "";
    }

    private int iRust;
    private boolean needsToOpenBombays;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju-88");
        Property.set(class1, "meshName", "3DO/Plane/Ju-88A-1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-88A-1.fmd");
        Property.set(class1, "LOSElevation", 1.0976F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_88A4.class, CockpitJU_88A4_Bombardier.class, CockpitJU_88A4_NGunner.class, CockpitJU_88A4_LGunner.class, CockpitJU_88A4_BGunner.class });
        weaponTriggersRegister(class1, new int[] {
            10, 11, 12, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 
            9
        });
        weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", 
            "_BombSpawn08", "_BombSpawn08a", "_BombSpawn09", "_BombSpawn09a", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", 
            "_BombSpawn15a", "_BombSpawn16", "_BombSpawn16a", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22", "_BombSpawn23", 
            "_BombSpawn24", "_ExternalBomb04", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb03", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", 
            "_ExternalDev04"
        });
        weaponsRegister(class1, "default", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "28xSC50_RustA", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", 
            "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", 
            "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", 
            "BombGunSC50", null, null, null, null, null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "28xSC50+1xTank900L_RustA", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", 
            "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", 
            "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", 
            "BombGunSC50", null, null, null, null, null, null, null, "FuelTankGun_Tank900", null, 
            null
        });
        weaponsRegister(class1, "28xSC50+2xTank900L_RustA", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", 
            "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", 
            "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", 
            "BombGunSC50", null, null, null, null, null, null, "FuelTankGun_Tank900", "FuelTankGun_Tank900", null, 
            null
        });
        weaponsRegister(class1, "10xSC50_RustB", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, "BombGunSC50", 
            null, "BombGunSC50", null, "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", 
            "BombGunSC50", null, null, null, null, null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "4xSC250_RustB", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, "BombGunSC250", "BombGunSC250", "BombGunSC250", "BombGunSC250", null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "2xSC500_RustB", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, "BombGunSC500", "BombGunSC500", null, null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "1xSC1000_RustB", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, "BombGunSC1000", null, null, null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "6xSC250_LiteRustB", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, "BombGunSC250", "BombGunSC250", "BombGunSC250", "BombGunSC250", "BombGunSC250", "BombGunSC250", null, null, "PylonETC250", 
            "PylonETC250"
        });
        weaponsRegister(class1, "6xSC250_RustB", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, "BombGunSC250", "BombGunSC250", "BombGunSC250", "BombGunSC250", "BombGunSC250", "BombGunSC250", null, null, "PylonETC250", 
            "PylonETC250"
        });
        weaponsRegister(class1, "4xSC500_LiteRustB", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, "BombGunSC500", "BombGunSC500", "BombGunSC500", "BombGunSC500", null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "4xAB500_LiteRustB", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, "BombGunAB500", "BombGunAB500", "BombGunAB500", "BombGunAB500", null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "2xSC1000_LiteRustB", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, "BombGunSC1000", "BombGunSC1000", null, null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "2xAB1000_LiteRustB", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, "BombGunAB1000", "BombGunAB1000", null, null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "4xSC500+2xSC250_LiteRustB", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, "BombGunSC500", "BombGunSC500", "BombGunSC500", "BombGunSC500", "BombGunSC250", "BombGunSC250", null, null, "PylonETC250", 
            "PylonETC250"
        });
        weaponsRegister(class1, "2xSC250_RustC", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, "BombGunSC250", "BombGunSC250", null, null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "1xSC500_RustC", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, "BombGunSC500", null, null, null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "1xAB500_RustC", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, "BombGunAB500", null, null, null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "4xSC250_RustC", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, "BombGunSC250", "BombGunSC250", "BombGunSC250", "BombGunSC250", null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "2xSC500_RustC", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, "BombGunSC500", "BombGunSC500", null, null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "2xAB500_RustC", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, "BombGunAB500", "BombGunAB500", null, null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "1xSC1000_RustC", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, "BombGunSC1000", null, null, null, null, null, null, null, 
            null
        });
        weaponsRegister(class1, "1xTank900L_RustF", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, "FuelTankGun_Tank900", null, 
            null
        });
        weaponsRegister(class1, "2xTank900L_RustF", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, "FuelTankGun_Tank900", "FuelTankGun_Tank900", null, 
            null
        });
        weaponsRegister(class1, "1xSC500+1xTank900L_RustF", new String[] {
            "MGunMG15t 375", "MGunMG15t 900", "MGunMG15t 450", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, "BombGunSC500", null, null, null, null, "FuelTankGun_Tank900", null, 
            null
        });
        weaponsRegister(class1, "none", new String[] {
            "MGunMG15t 0", "MGunMG15t 0", "MGunMG15t 0", null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null, null, null, null, null, null, null, null, null, null, 
            null
        });
    }
}
