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

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.HUD;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class JU_88C2 extends JU_88NEW
    implements TypeFighter, TypeBNZFighter, TypeStormovik
{

    public JU_88C2()
    {
    }

    public void blisterRemoved(int i)
    {
        float f = FM.getAltitude() - Landscape.HQ_Air((float)FM.Loc.x, (float)FM.Loc.y);
        if(f < 0.3F)
        {
            if(!topBlisterRemoved)
                doRemoveTopBlister();
        } else
        if(i == 2 || i == 3)
        {
            if(!blisterRemoved)
                doRemoveBlister1();
        } else
        if(i == 1)
        {
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Turret2B_D0", false);
            hierMesh().chunkVisible("Turret2C_D0", false);
        }
    }

    protected void doRemoveBlister1()
    {
        blisterRemoved = true;
        doWreck("Blister1_D0");
        hierMesh().chunkVisible("Turret2B_D0", false);
        hierMesh().chunkVisible("Turret2C_D0", false);
    }

    protected void doRemoveTopBlister()
    {
        topBlisterRemoved = true;
        hierMesh().chunkVisible("Turret1B_D0", false);
        hierMesh().chunkVisible("Turret1C_D0", false);
        doWreck("BlisterTop_D0");
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int i;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                i = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                i = s.charAt(6) - 49;
            } else
            {
                i = s.charAt(5) - 49;
            }
            if(i == 2)
                i = 1;
            else
            if(i == 3)
                i = 2;
            hitFlesh(i, shot, byte0);
            return;
        }
        if(s.startsWith("xxmgun"))
        {
            if(s.endsWith("01"))
            {
                debuggunnery("MG17-1: Disabled..");
                FM.AS.setJamBullets(0, 0);
            }
            if(s.endsWith("02"))
            {
                debuggunnery("MG17-2: Disabled..");
                FM.AS.setJamBullets(0, 1);
            }
            if(s.endsWith("03"))
            {
                debuggunnery("MG17-3: Disabled..");
                FM.AS.setJamBullets(0, 2);
            }
            getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 12.96F), shot);
        }
        if(s.startsWith("xxcannon"))
        {
            if(s.endsWith("01"))
            {
                debuggunnery("Cannon MG151/20-1: Disabled..");
                FM.AS.setJamBullets(1, 0);
            }
            getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
        }
        if(s.equals("xxarmorp6"))
            getEnergyPastArmor(0.5F, shot);
        if(s.equals("xxarmorp7"))
            getEnergyPastArmor(0.5F, shot);
        super.hitBone(s, shot, point3d);
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("Bay1_D0", 0.0F, 87F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -86F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 86F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -87F * f, 0.0F);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 3; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void update(float f)
    {
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
            } else
            if(f > 5.3F)
            {
                if(f1 < cvt(f, 5.3F, 25F, -1.72F, -5F))
                    f1 = cvt(f, 5.3F, 25F, -1.72F, -5F);
            } else
            if(f > -5.3F)
            {
                if(f1 < -1.72F)
                    f1 = -1.72F;
            } else
            if(f > -30F)
            {
                if(f1 < cvt(f, -30F, -5.3F, 5F, -1.72F))
                    f1 = cvt(f, -30F, -5.3F, 5F, -1.72F);
            } else
            if(f1 < cvt(f, -40F, -30F, 25F, -5F))
                f1 = cvt(f, -40F, -30F, 25F, -5F);
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

    protected void moveAirBrake(float f)
    {
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
        }
    }

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju-88");
        Property.set(class1, "meshName", "3DO/Plane/Ju-88C-2/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-88C-2.fmd");
        Property.set(class1, "LOSElevation", 1.0F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_88A4.class, CockpitJU_88A4_LGunner.class});
        weaponTriggersRegister(class1, new int[] {
            10, 11, 0, 0, 0, 1, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3
        });
        weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MG1701", "_MG1702", "_MG1703", "_MGFF01", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", 
            "_BombSpawn19", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22", "_BombSpawn23", "_BombSpawn24"
        });
        weaponsRegister(class1, "default", new String[] {
            "MGunMG15t 900", "MGunMG15t 525", "MGunMG17k 1000", "MGunMG17k 1000", "MGunMG81t 1000", "MGunMG15120k 400", null, null, null, null, 
            null, null, null, null, null, null
        });
        weaponsRegister(class1, "none", new String[] {
            "MGunMG15t 0", "MGunMG15t 0", "MGunMG17k 0", "MGunMG17k 0", "MGunMG81t 0", "MGunMG15120k 0", null, null, null, null, 
            null, null, null, null, null, null
        });
    }
}
