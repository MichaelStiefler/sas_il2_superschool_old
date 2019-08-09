package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class SHORT extends Scheme4
    implements TypeBomber
{

    public SHORT()
    {
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 3000F;
        fSightCurSpeed = 200F;
        fSightCurReadyness = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = Math.max(-f * 800F, -90F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 85F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 85F * f);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, -141F * f);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, -141F * f);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -25F * f);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 25F * f);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("FlapL_D0", 0.0F, 0.0F, 30F * f);
        hierMesh().chunkSetAngles("FlapR_D0", 0.0F, 0.0F, 30F * f);
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("BayL1_D0", 0.0F, 30F * f, 0.0F);
        hierMesh().chunkSetAngles("BayL2_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("BayC1_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("BayC2_D0", 0.0F, 90F * f, 0.0F);
        hierMesh().chunkSetAngles("BayR1_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("BayR2_D0", 0.0F, 90F * f, 0.0F);
    }

    public void doKillPilot(int i)
    {
        switch(i)
        {
        case 3:
            this.FM.turret[0].bIsOperable = false;
            break;

        case 4:
            this.FM.turret[1].bIsOperable = false;
            break;

        case 5:
            this.FM.turret[2].bIsOperable = false;
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            hierMesh().chunkVisible("Head1_D0", false);
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;

        case 2:
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            break;

        case 3:
            hierMesh().chunkVisible("Pilot4_D0", false);
            hierMesh().chunkVisible("Pilot4_D1", true);
            break;

        case 4:
            hierMesh().chunkVisible("Pilot5_D0", false);
            hierMesh().chunkVisible("Pilot5_D1", true);
            break;

        case 5:
            hierMesh().chunkVisible("Pilot6_D0", false);
            hierMesh().chunkVisible("Pilot6_D1", true);
            break;
        }
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        default:
            break;

        case 0:
            if(f < -70F)
            {
                f = -70F;
                flag = false;
            }
            if(f > 70F)
            {
                f = 70F;
                flag = false;
            }
            if(f1 < -20F)
            {
                f1 = -20F;
                flag = false;
            }
            if(f1 > 60F)
            {
                f1 = 60F;
                flag = false;
            }
            break;

        case 1:
            if(f1 < -3F)
            {
                f1 = -3F;
                flag = false;
            }
            if(f1 > 60F)
            {
                f1 = 60F;
                flag = false;
            }
            break;

        case 2:
            if(f < -70F)
            {
                f = -70F;
                flag = false;
            }
            if(f > 70F)
            {
                f = 70F;
                flag = false;
            }
            if(f1 < -20F)
            {
                f1 = -20F;
                flag = false;
            }
            if(f1 > 60F)
            {
                f1 = 60F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxeng"))
            {
                int i = s.charAt(5) - 49;
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(1.7F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat(20000F, 140000F) < shot.power)
                        {
                            this.FM.AS.setEngineStuck(shot.initiator, i);
                            debuggunnery("*** Engine" + i + " Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat(10000F, 50000F) < shot.power)
                        {
                            this.FM.AS.hitEngine(shot.initiator, i, 2);
                            debuggunnery("*** Engine" + i + " Crank Case Hit - Engine Damaged..");
                        }
                    } else
                    if(World.Rnd().nextFloat() < 0.04F)
                    {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
                    } else
                    {
                        this.FM.EI.engines[i].setReadyness(shot.initiator, this.FM.EI.engines[i].getReadyness() - 0.02F);
                        debuggunnery("*** Engine" + i + " Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[i].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[i].getCylindersRatio() * 0.9878F)
                    {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        debuggunnery("*** Engine" + i + " Cylinders Hit, " + this.FM.EI.engines[i].getCylindersOperable() + "/" + this.FM.EI.engines[i].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                        {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            debuggunnery("*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("supc"))
                {
                    if(getEnergyPastArmor(0.05F, shot) > 0.0F)
                        this.FM.EI.engines[i].setKillCompressor(shot.initiator);
                    getEnergyPastArmor(2.0F, shot);
                }
                if(s.endsWith("oil1"))
                {
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.25F, shot) > 0.0F)
                        debuggunnery("Engine Module: Oil Tank Pierced..");
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int j = s.charAt(6) - 49;
                if(getEnergyPastArmor(1.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.4F)
                {
                    if(this.FM.AS.astateTankStates[j] == 0)
                    {
                        debuggunnery("Fuel Tank (" + j + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, j, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if(World.Rnd().nextFloat() < 0.003F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.2F)
                    {
                        this.FM.AS.hitTank(shot.initiator, j, 4);
                        debuggunnery("Fuel Tank (" + j + "): Hit..");
                    }
                }
                return;
            }
            if(s.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr1") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D2", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.01F && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0].haveBullets())
            {
                Aircraft.debugprintln(this, "*** Bomb Payload Detonates..");
                this.FM.AS.hitTank(shot.initiator, 0, 10);
                this.FM.AS.hitTank(shot.initiator, 1, 10);
                this.FM.AS.hitTank(shot.initiator, 2, 10);
                this.FM.AS.hitTank(shot.initiator, 3, 10);
                nextDMGLevels(3, 2, "CF_D0", shot.initiator);
            }
            return;
        }
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 2)
                hitChunk("CF", shot);
            return;
        }
        if(s.startsWith("xcn"))
        {
            if(chunkDamageVisible("Blister") < 2)
                hitChunk("Blister", shot);
            return;
        }
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 2)
                hitChunk("Tail1", shot);
            return;
        }
        if(s.startsWith("xkeel1"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
            return;
        }
        if(s.startsWith("xrudder1"))
        {
            if(chunkDamageVisible("Rudder1") < 2)
                hitChunk("Rudder1", shot);
            return;
        }
        if(s.startsWith("xstabl"))
        {
            if(chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
            return;
        }
        if(s.startsWith("xstabr"))
        {
            if(chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
            return;
        }
        if(s.startsWith("xvatorl"))
        {
            if(chunkDamageVisible("VatorL") < 2)
                hitChunk("VatorL", shot);
            return;
        }
        if(s.startsWith("xvatorr"))
        {
            if(chunkDamageVisible("VatorR") < 2)
                hitChunk("VatorR", shot);
            return;
        }
        if(s.startsWith("xwinglin"))
        {
            if(chunkDamageVisible("WingLIn") < 2)
                hitChunk("WingLIn", shot);
            return;
        }
        if(s.startsWith("xwingrin"))
        {
            if(chunkDamageVisible("WingRIn") < 2)
                hitChunk("WingRIn", shot);
            return;
        }
        if(s.startsWith("xwinglout"))
        {
            if(chunkDamageVisible("WingLOut") < 2)
                hitChunk("WingLOut", shot);
            return;
        }
        if(s.startsWith("xwingrout"))
        {
            if(chunkDamageVisible("WingROut") < 2)
                hitChunk("WingROut", shot);
            return;
        }
        if(s.startsWith("xaronel"))
        {
            if(chunkDamageVisible("AroneL") < 2)
                hitChunk("AroneL", shot);
            return;
        }
        if(s.startsWith("xaroner"))
        {
            if(chunkDamageVisible("AroneR") < 2)
                hitChunk("AroneR", shot);
            return;
        }
        if(s.startsWith("xbayl1"))
        {
            if(chunkDamageVisible("BayL1") < 2)
                hitChunk("BayL1", shot);
            return;
        }
        if(s.startsWith("xbayl2"))
        {
            if(chunkDamageVisible("BayL2") < 2)
                hitChunk("BayL2", shot);
            return;
        }
        if(s.startsWith("xbayc1"))
        {
            if(chunkDamageVisible("BayC1") < 2)
                hitChunk("BayC1", shot);
            return;
        }
        if(s.startsWith("xbayc2"))
        {
            if(chunkDamageVisible("BayC2") < 2)
                hitChunk("BayC2", shot);
            return;
        }
        if(s.startsWith("xbayr1"))
        {
            if(chunkDamageVisible("BayR1") < 2)
                hitChunk("BayR1", shot);
            return;
        }
        if(s.startsWith("xbayr2"))
        {
            if(chunkDamageVisible("BayR2") < 2)
                hitChunk("BayR2", shot);
            return;
        }
        if(s.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
            return;
        }
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
            return;
        }
        if(s.startsWith("xengine3"))
        {
            if(chunkDamageVisible("Engine3") < 2)
                hitChunk("Engine3", shot);
            return;
        }
        if(s.startsWith("xengine4"))
        {
            if(chunkDamageVisible("Engine4") < 2)
                hitChunk("Engine4", shot);
            return;
        }
        if(s.startsWith("xflapl"))
        {
            if(chunkDamageVisible("FlapL") < 2)
                hitChunk("FlapL", shot);
            return;
        }
        if(s.startsWith("xflapr"))
        {
            if(chunkDamageVisible("FlapR") < 2)
                hitChunk("FlapR", shot);
            return;
        }
        if(s.startsWith("xgear"))
        {
            if(World.Rnd().nextFloat() < 0.05F)
            {
                Aircraft.debugprintln(this, "*** Gear Hydro Failed..");
                this.FM.Gears.setHydroOperable(false);
            }
            return;
        }
        if(s.startsWith("xmgun"))
            if(s.endsWith("01"))
            {
                if(getEnergyPastArmor(5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    this.FM.AS.setJamBullets(0, 0);
                    getEnergyPastArmor(11.98F, shot);
                }
            } else
            if(s.endsWith("02"))
            {
                if(getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    this.FM.AS.setJamBullets(0, 1);
                    getEnergyPastArmor(11.98F, shot);
                }
            } else
            if(s.endsWith("03"))
            {
                if(getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    this.FM.AS.setJamBullets(0, 2);
                    getEnergyPastArmor(11.98F, shot);
                }
            } else
            if(s.endsWith("04"))
            {
                if(getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    this.FM.AS.setJamBullets(0, 3);
                    getEnergyPastArmor(11.98F, shot);
                }
            } else
            if(s.endsWith("05"))
            {
                if(getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    this.FM.AS.setJamBullets(0, 4);
                    getEnergyPastArmor(11.98F, shot);
                }
            } else
            if(s.endsWith("06") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
            {
                this.FM.AS.setJamBullets(0, 5);
                getEnergyPastArmor(11.98F, shot);
            } else
            if(s.endsWith("07"))
            {
                if(getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    this.FM.AS.setJamBullets(0, 6);
                    getEnergyPastArmor(11.98F, shot);
                }
            } else
            if(s.endsWith("08") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
            {
                this.FM.AS.setJamBullets(0, 7);
                getEnergyPastArmor(11.98F, shot);
            }
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int k;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                k = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                k = s.charAt(6) - 49;
            } else
            {
                k = s.charAt(5) - 49;
            }
            hitFlesh(k, shot, byte0);
            return;
        } else
        {
            return;
        }
    }

    private static final float toMeters(float f)
    {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f)
    {
        return 0.4470401F * f;
    }

    public boolean typeBomberToggleAutomation()
    {
        bSightAutomation = !bSightAutomation;
        bSightBombDump = false;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (bSightAutomation ? "ON" : "OFF"));
        return bSightAutomation;
    }

    public void typeBomberAdjDistanceReset()
    {
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus()
    {
        fSightCurForwardAngle++;
        if(fSightCurForwardAngle > 85F)
            fSightCurForwardAngle = 85F;
        fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
            new Integer((int)fSightCurForwardAngle)
        });
        if(bSightAutomation)
            typeBomberToggleAutomation();
    }

    public void typeBomberAdjDistanceMinus()
    {
        fSightCurForwardAngle--;
        if(fSightCurForwardAngle < 0.0F)
            fSightCurForwardAngle = 0.0F;
        fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
            new Integer((int)fSightCurForwardAngle)
        });
        if(bSightAutomation)
            typeBomberToggleAutomation();
    }

    public void typeBomberAdjSideslipReset()
    {
        fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus()
    {
        fSightCurSideslip += 0.1F;
        if(fSightCurSideslip > 3F)
            fSightCurSideslip = 3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Integer((int)(fSightCurSideslip * 10F))
        });
    }

    public void typeBomberAdjSideslipMinus()
    {
        fSightCurSideslip -= 0.1F;
        if(fSightCurSideslip < -3F)
            fSightCurSideslip = -3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Integer((int)(fSightCurSideslip * 10F))
        });
    }

    public void typeBomberAdjAltitudeReset()
    {
        fSightCurAltitude = 3000F;
    }

    public void typeBomberAdjAltitudePlus()
    {
        fSightCurAltitude += 50F;
        if(fSightCurAltitude > 50000F)
            fSightCurAltitude = 50000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
        fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
    }

    public void typeBomberAdjAltitudeMinus()
    {
        fSightCurAltitude -= 50F;
        if(fSightCurAltitude < 1000F)
            fSightCurAltitude = 1000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
        fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
    }

    public void typeBomberAdjSpeedReset()
    {
        fSightCurSpeed = 200F;
    }

    public void typeBomberAdjSpeedPlus()
    {
        fSightCurSpeed += 10F;
        if(fSightCurSpeed > 450F)
            fSightCurSpeed = 450F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberAdjSpeedMinus()
    {
        fSightCurSpeed -= 10F;
        if(fSightCurSpeed < 100F)
            fSightCurSpeed = 100F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberUpdate(float f)
    {
        if((double)Math.abs(this.FM.Or.getKren()) > 4.5D)
        {
            fSightCurReadyness -= 0.0666666F * f;
            if(fSightCurReadyness < 0.0F)
                fSightCurReadyness = 0.0F;
        }
        if(fSightCurReadyness < 1.0F)
            fSightCurReadyness += 0.0333333F * f;
        else
        if(bSightAutomation)
        {
            fSightCurDistance -= toMetersPerSecond(fSightCurSpeed) * f;
            if(fSightCurDistance < 0.0F)
            {
                fSightCurDistance = 0.0F;
                typeBomberToggleAutomation();
            }
            fSightCurForwardAngle = (float)Math.toDegrees(Math.atan(fSightCurDistance / toMeters(fSightCurAltitude)));
            if((double)fSightCurDistance < (double)toMetersPerSecond(fSightCurSpeed) * Math.sqrt(toMeters(fSightCurAltitude) * (2F / 9.81F)))
                bSightBombDump = true;
            if(bSightBombDump)
                if(this.FM.isTick(3, 0))
                {
                    if(this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets())
                    {
                        this.FM.CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else
                {
                    this.FM.CT.WeaponControl[3] = false;
                }
        }
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte((bSightAutomation ? 1 : 0) | (bSightBombDump ? 2 : 0));
        netmsgguaranted.writeFloat(fSightCurDistance);
        netmsgguaranted.writeByte((int)fSightCurForwardAngle);
        netmsgguaranted.writeByte((int)((fSightCurSideslip + 3F) * 33.33333F));
        netmsgguaranted.writeFloat(fSightCurAltitude);
        netmsgguaranted.writeByte((int)(fSightCurSpeed / 2.5F));
        netmsgguaranted.writeByte((int)(fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        int i = netmsginput.readUnsignedByte();
        bSightAutomation = (i & 1) != 0;
        bSightBombDump = (i & 2) != 0;
        fSightCurDistance = netmsginput.readFloat();
        fSightCurForwardAngle = netmsginput.readUnsignedByte();
        fSightCurSideslip = -3F + (float)netmsginput.readUnsignedByte() / 33.33333F;
        fSightCurAltitude = netmsginput.readFloat();
        fSightCurSpeed = (float)netmsginput.readUnsignedByte() * 2.5F;
        fSightCurReadyness = (float)netmsginput.readUnsignedByte() / 200F;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 7; i++)
            if(this.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void update(float f)
    {
        if((this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode())
        {
            float f1 = this.FM.EI.engines[0].getRPM();
            if(f1 < 300F && f1 > 30F)
                ((RealFlightModel)this.FM).producedShakeLevel = (1500F - f1) / 3000F;
            float f5 = this.FM.EI.engines[0].getRPM();
            if(f5 < 1000F && f5 > 301F)
                ((RealFlightModel)this.FM).producedShakeLevel = (1500F - f5) / 8000F;
            float f10 = this.FM.EI.engines[0].getRPM();
            if(f10 > 1001F && f10 < 1500F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.07F;
            float f14 = this.FM.EI.engines[0].getRPM();
            if(f14 > 1501F && f14 < 2000F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.05F;
            float f18 = this.FM.EI.engines[0].getRPM();
            if(f18 > 2001F && f18 < 2500F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.04F;
            float f22 = this.FM.EI.engines[0].getRPM();
            if(f22 > 2501F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.03F;
        }
        if((this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode())
        {
            float f2 = this.FM.EI.engines[1].getRPM();
            if(f2 < 300F && f2 > 30F)
                ((RealFlightModel)this.FM).producedShakeLevel = (1500F - f2) / 3000F;
            float f6 = this.FM.EI.engines[1].getRPM();
            if(f6 < 1000F && f6 > 301F)
                ((RealFlightModel)this.FM).producedShakeLevel = (1500F - f6) / 8000F;
            float f11 = this.FM.EI.engines[1].getRPM();
            if(f11 > 1001F && f11 < 1500F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.07F;
            float f15 = this.FM.EI.engines[1].getRPM();
            if(f15 > 1501F && f15 < 2000F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.05F;
            float f19 = this.FM.EI.engines[1].getRPM();
            if(f19 > 2001F && f19 < 2500F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.04F;
            float f23 = this.FM.EI.engines[1].getRPM();
            if(f23 > 2501F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.03F;
        }
        if((this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode())
        {
            float f3 = this.FM.EI.engines[2].getRPM();
            if(f3 < 300F && f3 > 30F)
                ((RealFlightModel)this.FM).producedShakeLevel = (1500F - f3) / 3000F;
            float f7 = this.FM.EI.engines[2].getRPM();
            if(f7 < 1000F && f7 > 301F)
                ((RealFlightModel)this.FM).producedShakeLevel = (1500F - f7) / 8000F;
            float f12 = this.FM.EI.engines[2].getRPM();
            if(f12 > 1001F && f12 < 1500F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.07F;
            float f16 = this.FM.EI.engines[2].getRPM();
            if(f16 > 1501F && f16 < 2000F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.05F;
            float f20 = this.FM.EI.engines[2].getRPM();
            if(f20 > 2001F && f20 < 2500F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.04F;
            float f24 = this.FM.EI.engines[2].getRPM();
            if(f24 > 2501F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.03F;
        }
        if((this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode())
        {
            float f4 = this.FM.EI.engines[3].getRPM();
            if(f4 < 300F && f4 > 30F)
                ((RealFlightModel)this.FM).producedShakeLevel = (1500F - f4) / 3000F;
            float f8 = this.FM.EI.engines[3].getRPM();
            if(f8 < 1000F && f8 > 301F)
                ((RealFlightModel)this.FM).producedShakeLevel = (1500F - f8) / 8000F;
            float f13 = this.FM.EI.engines[3].getRPM();
            if(f13 > 1001F && f13 < 1500F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.07F;
            float f17 = this.FM.EI.engines[3].getRPM();
            if(f17 > 1501F && f17 < 2000F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.05F;
            float f21 = this.FM.EI.engines[3].getRPM();
            if(f21 > 2001F && f21 < 2500F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.04F;
            float f25 = this.FM.EI.engines[3].getRPM();
            if(f25 > 2501F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.03F;
        }
        if(this.FM.getSpeedKMH() > 250F && this.FM.getVertSpeed() > 0.0F && this.FM.getAltitude() < 5000F)
            this.FM.producedAF.x += 20F * (250F - this.FM.getSpeedKMH());
        if(this.FM.isPlayers() && this.FM.Sq.squareElevators > 0.0F)
        {
            RealFlightModel realflightmodel = (RealFlightModel)this.FM;
            if(realflightmodel.RealMode && realflightmodel.indSpeed > 120F)
            {
                float f9 = 1.0F + 0.005F * (120F - realflightmodel.indSpeed);
                if(f9 < 0.0F)
                    f9 = 0.0F;
                this.FM.SensPitch = 0.25F * f9;
                if(realflightmodel.indSpeed > 120F)
                    this.FM.producedAM.y -= 1720F * (120F - realflightmodel.indSpeed);
            } else
            {
                this.FM.SensPitch = 0.4F;
            }
        }
        super.update(f);
    }

    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    float fSightCurForwardAngle;
    float fSightCurSideslip;
    private float fSightCurAltitude;
    private float fSightCurSpeed;
    float fSightCurReadyness;

    static 
    {
        Property.set(SHORT.class, "originCountry", PaintScheme.countryBritain);
    }
}
