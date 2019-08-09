package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class WhitleyBombers extends Scheme2
{

    public WhitleyBombers()
    {
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 850F;
        fSightCurSpeed = 150F;
        fSightCurReadyness = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -45F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -45F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -45F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -45F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, 0.0F);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.2375F, 0.0F, 0.2375F);
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.2375F, 0.0F, 0.2375F);
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void update(float f)
    {
        World.cur().diffCur.Torque_N_Gyro_Effects = false;
        super.update(f);
        hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -FM.Gears.gWheelAngles[0], 0.0F);
        hierMesh().chunkSetAngles("GearR3_D0", 0.0F, -FM.Gears.gWheelAngles[1], 0.0F);
        FM.EI.engines[0].addVside *= 9.9999999999999995E-008D;
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -25F * f, 0.0F);
        hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -25F * f, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -25F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -25F * f, 0.0F);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -15F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -15F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        float f1 = -15F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag)
        {
            if(FM.AS.astateEngineStates[0] > 3)
            {
                if(World.Rnd().nextFloat() < 0.02F)
                    FM.AS.hitTank(this, 0, 1);
                if(World.Rnd().nextFloat() < 0.02F)
                    FM.AS.hitTank(this, 1, 1);
            }
            if(FM.AS.astateEngineStates[1] > 3)
            {
                if(World.Rnd().nextFloat() < 0.02F)
                    FM.AS.hitTank(this, 2, 1);
                if(World.Rnd().nextFloat() < 0.02F)
                    FM.AS.hitTank(this, 3, 1);
            }
            if(FM.AS.astateTankStates[0] > 5 && World.Rnd().nextFloat() < 0.02F)
                FM.AS.hitTank(this, 1, 1);
            if(FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.02F)
                FM.AS.hitTank(this, 0, 1);
            if(FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.02F)
                FM.AS.hitTank(this, 2, 1);
            if(FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.02F)
                FM.AS.hitTank(this, 1, 1);
            if(FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.02F)
                FM.AS.hitTank(this, 3, 1);
            if(FM.AS.astateTankStates[3] > 5 && World.Rnd().nextFloat() < 0.02F)
                FM.AS.hitTank(this, 2, 1);
        }
        if(FM.getAltitude() < 3000F)
        {
            for(int i = 1; i < 8; i++)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);

        } else
        {
            for(int j = 1; j < 8; j++)
                hierMesh().chunkVisible("HMask" + j + "_D0", hierMesh().isChunkVisible("Pilot1_D0"));

        }
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        case 2:
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
            if(f1 < -45F)
            {
                f1 = -45F;
                flag = false;
            }
            if(f1 > 20F)
            {
                f1 = 20F;
                flag = false;
            }
            break;

        case 1:
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
            if(f1 < -40F)
            {
                f1 = -40F;
                flag = false;
            }
            if(f1 > 35F)
            {
                f1 = 35F;
                flag = false;
            }
            // fall through

        case 3:
            if(f < -55F)
            {
                f = -55F;
                flag = false;
            }
            if(f > 30F)
            {
                f = 30F;
                flag = false;
            }
            if(f1 < -40F)
            {
                f1 = -40F;
                flag = false;
            }
            if(f1 > 45F)
            {
                f1 = 45F;
                flag = false;
            }
            break;

        case 4:
            if(f < -30F)
            {
                f = -30F;
                flag = false;
            }
            if(f > 55F)
            {
                f = 55F;
                flag = false;
            }
            if(f1 < -40F)
            {
                f1 = -40F;
                flag = false;
            }
            if(f1 > 45F)
            {
                f1 = 45F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        default:
            break;

        case 33:
            hitProp(0, j, actor);
            break;

        case 36:
            hitProp(1, j, actor);
            break;

        case 34:
            FM.AS.hitEngine(this, 0, 2);
            if(World.Rnd().nextInt(0, 99) < 66)
                FM.AS.hitEngine(this, 0, 2);
            break;

        case 37:
            FM.AS.hitEngine(this, 1, 2);
            if(World.Rnd().nextInt(0, 99) < 66)
                FM.AS.hitEngine(this, 1, 2);
            break;

        case 19:
            killPilot(actor, 6);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("Bay01_D0", 0.0F, -85F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay02_D0", 0.0F, -85F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay03_D0", 0.0F, 85F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay04_D0", 0.0F, 85F * f, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxcontrol"))
            {
                int i = s.charAt(9) - 48;
                switch(i)
                {
                case 1:
                    if(getEnergyPastArmor(3F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                            FM.AS.setControlsDamage(shot.initiator, 2);
                        if(World.Rnd().nextFloat() < 0.1F)
                            FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    break;

                case 2:
                case 3:
                    if(getEnergyPastArmor(3F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    break;
                }
            } else
            if(s.startsWith("xxeng"))
            {
                int j = s.charAt(5) - 49;
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(1.7F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat(20000F, 140000F) < shot.power)
                        {
                            FM.AS.setEngineStuck(shot.initiator, j);
                            debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat(10000F, 50000F) < shot.power)
                        {
                            FM.AS.hitEngine(shot.initiator, j, 2);
                            debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Damaged..");
                        }
                    } else
                    if(World.Rnd().nextFloat() < 0.04F)
                    {
                        FM.EI.engines[j].setCyliderKnockOut(shot.initiator, 1);
                    } else
                    {
                        FM.EI.engines[j].setReadyness(shot.initiator, FM.EI.engines[j].getReadyness() - 0.02F);
                        debuggunnery("*** Engine" + j + " Crank Case Hit - Readyness Reduced to " + FM.EI.engines[j].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[j].getCylindersRatio() * 0.66F)
                    {
                        FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 32200F)));
                        debuggunnery("*** Engine" + j + " Cylinders Hit, " + FM.EI.engines[j].getCylindersOperable() + "/" + FM.EI.engines[j].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 1000000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            debuggunnery("*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("supc"))
                {
                    if(getEnergyPastArmor(0.05F, shot) > 0.0F)
                        FM.EI.engines[j].setKillCompressor(shot.initiator);
                    getEnergyPastArmor(2.0F, shot);
                }
                if(s.endsWith("gear"))
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        if(Aircraft.Pd.y > 0.0D && Aircraft.Pd.z < 0.18899999558925629D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power)
                            FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 0);
                        if(Aircraft.Pd.y < 0.0D && Aircraft.Pd.z < 0.18899999558925629D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power)
                            FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 1);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 6);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 1);
                    }
                    getEnergyPastArmor(2.0F, shot);
                }
                if(s.endsWith("oil1") && getEnergyPastArmor(4.21F, shot) > 0.0F)
                {
                    FM.AS.hitOil(shot.initiator, j);
                    getEnergyPastArmor(0.42F, shot);
                }
            } else
            if(s.startsWith("xxfuel"))
            {
                int k = s.charAt(6) - 48;
                int i1;
                switch(k)
                {
                default:
                    if(World.Rnd().nextFloat() < 0.1F)
                        hitBone("xxfuel2", shot, point3d);
                    if(World.Rnd().nextFloat() < 0.1F)
                        hitBone("xxfuel3", shot, point3d);
                    if(World.Rnd().nextFloat() < 0.1F)
                        hitBone("xxfuel5", shot, point3d);
                    if(World.Rnd().nextFloat() < 0.1F)
                        hitBone("xxfuel6", shot, point3d);
                    return;

                case 2:
                    i1 = 1;
                    break;

                case 3:
                    i1 = World.Rnd().nextInt(0, 1);
                    break;

                case 4:
                    i1 = 0;
                    break;

                case 5:
                    i1 = 2;
                    break;

                case 6:
                    i1 = World.Rnd().nextInt(2, 3);
                    break;

                case 7:
                    i1 = 3;
                    break;
                }
                if(getEnergyPastArmor(0.8F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2F)
                {
                    if(FM.AS.astateTankStates[i1] == 0)
                    {
                        FM.AS.hitTank(shot.initiator, i1, 2);
                        FM.AS.doSetTankState(shot.initiator, i1, 2);
                    }
                    if(World.Rnd().nextFloat() < 0.15F)
                        FM.AS.hitTank(shot.initiator, i1, 1);
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F)
                        FM.AS.hitTank(shot.initiator, i1, 2);
                }
            } else
            if(s.startsWith("xxgun"))
            {
                int l = s.charAt(5) - 49;
                if(World.Rnd().nextFloat() < 0.5F)
                    FM.AS.setJamBullets(10 + l, 0);
                getEnergyPastArmor(22.7F, shot);
            } else
            if(s.startsWith("xxlock"))
            {
                if(s.startsWith("xxlockal"))
                {
                    if(getEnergyPastArmor(6.5F, shot) > 0.0F)
                    {
                        debuggunnery("*** AroneL Lock Damaged..");
                        nextDMGLevels(1, 2, "AroneL_D0", shot.initiator);
                    }
                } else
                if(s.startsWith("xxlockar"))
                {
                    if(getEnergyPastArmor(6.5F, shot) > 0.0F)
                    {
                        debuggunnery("*** AroneR Lock Damaged..");
                        nextDMGLevels(1, 2, "AroneR_D0", shot.initiator);
                    }
                } else
                if(s.startsWith("xxlocksl"))
                {
                    if(getEnergyPastArmor(6.5F, shot) > 0.0F)
                    {
                        debuggunnery("*** VatorL Lock Damaged..");
                        nextDMGLevels(1, 2, "VatorL_D0", shot.initiator);
                    }
                } else
                if(s.startsWith("xxlocksr"))
                {
                    if(getEnergyPastArmor(6.5F, shot) > 0.0F)
                    {
                        debuggunnery("*** VatorR Lock Damaged..");
                        nextDMGLevels(1, 2, "VatorR_D0", shot.initiator);
                    }
                } else
                if(s.startsWith("xxlockk") && getEnergyPastArmor(6.5F, shot) > 0.0F)
                {
                    debuggunnery("*** Rudder1 Lock Damaged..");
                    nextDMGLevels(1, 2, "Rudder1_D0", shot.initiator);
                }
            } else
            if(s.startsWith("xxpar"))
            {
                if(s.startsWith("xxpark") && chunkDamageVisible("Keel1") > 1 && getEnergyPastArmor(10.699999809265137D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** Keel1 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if(s.startsWith("xxparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(12.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s.startsWith("xxparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(12.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s.startsWith("xxparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(10.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(10.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(8.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(8.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(s.startsWith("xxparrs") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(8.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(s.startsWith("xxparls") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(8.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
            }
        } else
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 2)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xstabl"))
        {
            if(chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
        } else
        if(s.startsWith("xstabr"))
        {
            if(chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvatorl"))
        {
            if(chunkDamageVisible("VatorL") < 2)
                hitChunk("VatorL", shot);
        } else
        if(s.startsWith("xvatorr"))
        {
            if(chunkDamageVisible("VatorR") < 2)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwinglin"))
        {
            if(chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
        } else
        if(s.startsWith("xwingrin"))
        {
            if(chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
        } else
        if(s.startsWith("xwinglmid"))
        {
            if(chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
        } else
        if(s.startsWith("xwingrmid"))
        {
            if(chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
        } else
        if(s.startsWith("xwinglout"))
        {
            if(chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
        } else
        if(s.startsWith("xwingrout"))
        {
            if(chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xaronel"))
        {
            if(chunkDamageVisible("AroneL") < 2)
                hitChunk("AroneL", shot);
        } else
        if(s.startsWith("xaroner"))
        {
            if(chunkDamageVisible("AroneR") < 2)
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
            {
                debuggunnery("Hydro System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(12.88F, 16.96F), shot) > 0.0F)
            {
                debuggunnery("Undercarriage: Stuck..");
                FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int j1;
            if(s.endsWith("a") || s.endsWith("a2"))
            {
                byte0 = 1;
                j1 = s.charAt(6) - 49;
            } else
            if(s.endsWith("b") || s.endsWith("b2"))
            {
                byte0 = 2;
                j1 = s.charAt(6) - 49;
            } else
            {
                j1 = s.charAt(5) - 49;
            }
            hitFlesh(j1, shot, byte0);
        }
    }

    public void doKillPilot(int i)
    {
        switch(i)
        {
        case 2:
            FM.turret[0].bIsOperable = false;
            break;

        case 3:
            FM.turret[1].bIsOperable = false;
            break;

        case 4:
            FM.turret[2].bIsOperable = false;
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
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
        fSightCurDistance = fSightCurAltitude * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
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
        fSightCurDistance = fSightCurAltitude * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
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
        fSightCurAltitude = 850F;
    }

    public void typeBomberAdjAltitudePlus()
    {
        fSightCurAltitude += 10F;
        if(fSightCurAltitude > 10000F)
            fSightCurAltitude = 10000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
        fSightCurDistance = fSightCurAltitude * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
    }

    public void typeBomberAdjAltitudeMinus()
    {
        fSightCurAltitude -= 10F;
        if(fSightCurAltitude < 850F)
            fSightCurAltitude = 850F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
        fSightCurDistance = fSightCurAltitude * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
    }

    public void typeBomberAdjSpeedReset()
    {
        fSightCurSpeed = 150F;
    }

    public void typeBomberAdjSpeedPlus()
    {
        fSightCurSpeed += 10F;
        if(fSightCurSpeed > 600F)
            fSightCurSpeed = 600F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberAdjSpeedMinus()
    {
        fSightCurSpeed -= 10F;
        if(fSightCurSpeed < 150F)
            fSightCurSpeed = 150F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberUpdate(float f)
    {
        if((double)Math.abs(FM.Or.getKren()) > 4.5D)
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
            fSightCurDistance -= (fSightCurSpeed / 3.6F) * f;
            if(fSightCurDistance < 0.0F)
            {
                fSightCurDistance = 0.0F;
                typeBomberToggleAutomation();
            }
            fSightCurForwardAngle = (float)Math.toDegrees(Math.atan(fSightCurDistance / fSightCurAltitude));
            if((double)fSightCurDistance < (double)(fSightCurSpeed / 3.6F) * Math.sqrt(fSightCurAltitude * (2F / 9.81F)))
                bSightBombDump = true;
            if(bSightBombDump)
                if(FM.isTick(3, 0))
                {
                    if(FM.CT.Weapons[3] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets())
                    {
                        FM.CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else
                {
                    FM.CT.WeaponControl[3] = false;
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

    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;

    static 
    {
        Class class1 = WhitleyBombers.class;
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
    }
}
