package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class ManchesterBombers extends Scheme2
    implements TypeTransport, TypeBomber
{

    public ManchesterBombers()
    {
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 850F;
        fSightCurSpeed = 150F;
        fSightCurReadyness = 0.0F;
        fSightSetForwardAngle = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = Math.max(-f * 800F, -85F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 98F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 98F * f);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, -146F * f);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, -146F * f);
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
        hierMesh().chunkSetAngles("Rudder2_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("FlapWL_D0", 0.0F, 0.0F, 40F * f);
        hierMesh().chunkSetAngles("FlapCL_D0", 0.0F, 0.0F, 40F * f);
        hierMesh().chunkSetAngles("FlapWR_D0", 0.0F, 0.0F, 40F * f);
        hierMesh().chunkSetAngles("FlapCR_D0", 0.0F, 0.0F, 40F * f);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag)
        {
            if(this.FM.AS.astateEngineStates[0] > 3)
            {
                if(World.Rnd().nextFloat() < 0.02F)
                    this.FM.AS.hitTank(this, 0, 1);
                if(World.Rnd().nextFloat() < 0.02F)
                    this.FM.AS.hitTank(this, 1, 1);
            }
            if(this.FM.AS.astateEngineStates[1] > 3)
            {
                if(World.Rnd().nextFloat() < 0.02F)
                    this.FM.AS.hitTank(this, 2, 1);
                if(World.Rnd().nextFloat() < 0.02F)
                    this.FM.AS.hitTank(this, 3, 1);
            }
            if(this.FM.AS.astateTankStates[0] > 5 && World.Rnd().nextFloat() < 0.02F)
                this.FM.AS.hitTank(this, 1, 1);
            if(this.FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.02F)
                this.FM.AS.hitTank(this, 0, 1);
            if(this.FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.02F)
                this.FM.AS.hitTank(this, 2, 1);
            if(this.FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.02F)
                this.FM.AS.hitTank(this, 1, 1);
            if(this.FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.02F)
                this.FM.AS.hitTank(this, 3, 1);
            if(this.FM.AS.astateTankStates[3] > 5 && World.Rnd().nextFloat() < 0.02F)
                this.FM.AS.hitTank(this, 2, 1);
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
            if(f1 > 50F)
            {
                f1 = 50F;
                flag = false;
            }
            break;

        case 1:
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

        case 2:
            if(f1 < 0.0F)
            {
                f1 = 0.0F;
                flag = false;
            }
            if(f1 > 88F)
            {
                f1 = 88F;
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
            this.FM.AS.hitEngine(this, 0, 2);
            if(World.Rnd().nextInt(0, 99) < 66)
                this.FM.AS.hitEngine(this, 0, 2);
            break;

        case 37:
            this.FM.AS.hitEngine(this, 1, 2);
            if(World.Rnd().nextInt(0, 99) < 66)
                this.FM.AS.hitEngine(this, 1, 2);
            break;

        case 19:
            killPilot(actor, 6);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("BayL_D0", 0.0F, 85F * f, 0.0F);
        hierMesh().chunkSetAngles("BayR_D0", 0.0F, -85F * f, 0.0F);
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
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        if(World.Rnd().nextFloat() < 0.1F)
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    break;

                case 2:
                case 3:
                    if(getEnergyPastArmor(3F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
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
                            this.FM.AS.setEngineStuck(shot.initiator, j);
                            debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat(10000F, 50000F) < shot.power)
                        {
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                            debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Damaged..");
                        }
                    } else
                    if(World.Rnd().nextFloat() < 0.04F)
                    {
                        this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, 1);
                    } else
                    {
                        this.FM.EI.engines[j].setReadyness(shot.initiator, this.FM.EI.engines[j].getReadyness() - 0.02F);
                        debuggunnery("*** Engine" + j + " Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[j].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[j].getCylindersRatio() * 0.66F)
                    {
                        this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 32200F)));
                        debuggunnery("*** Engine" + j + " Cylinders Hit, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 1000000F)
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
                        this.FM.EI.engines[j].setKillCompressor(shot.initiator);
                    getEnergyPastArmor(2.0F, shot);
                }
                if(s.endsWith("gear"))
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        if(Aircraft.Pd.y > 0.0D && Aircraft.Pd.z < 0.189D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power)
                            this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 0);
                        if(Aircraft.Pd.y < 0.0D && Aircraft.Pd.z < 0.189D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power)
                            this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 1);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 6);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 1);
                    }
                    getEnergyPastArmor(2.0F, shot);
                }
                if(s.endsWith("oil1") && getEnergyPastArmor(4.21F, shot) > 0.0F)
                {
                    this.FM.AS.hitOil(shot.initiator, j);
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
                    if(this.FM.AS.astateTankStates[i1] == 0)
                    {
                        this.FM.AS.hitTank(shot.initiator, i1, 2);
                        this.FM.AS.doSetTankState(shot.initiator, i1, 2);
                    }
                    if(World.Rnd().nextFloat() < 0.15F)
                        this.FM.AS.hitTank(shot.initiator, i1, 1);
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F)
                        this.FM.AS.hitTank(shot.initiator, i1, 2);
                }
            } else
            if(s.startsWith("xxgun"))
            {
                int l = s.charAt(5) - 49;
                if(World.Rnd().nextFloat() < 0.5F)
                    this.FM.AS.setJamBullets(10 + l, 0);
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
                } else
                if(s.startsWith("xxlockk") && getEnergyPastArmor(6.5F, shot) > 0.0F)
                {
                    debuggunnery("*** Rudder2 Lock Damaged..");
                    nextDMGLevels(1, 2, "Rudder2_D0", shot.initiator);
                }
            } else
            if(s.startsWith("xxpar"))
            {
                if(s.startsWith("xxpark") && chunkDamageVisible("Keel1") > 2 && getEnergyPastArmor(10.7D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** Keel1 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if(s.startsWith("xxparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(12.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D2", shot.initiator);
                }
                if(s.startsWith("xxparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(12.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D2", shot.initiator);
                }
                if(s.startsWith("xxparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(8.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D2", shot.initiator);
                }
                if(s.startsWith("xxparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(8.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D2", shot.initiator);
                }
                if(s.startsWith("xxparrs") && chunkDamageVisible("StabL") > 2 && getEnergyPastArmor(8.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(s.startsWith("xxparls") && chunkDamageVisible("StabR") > 2 && getEnergyPastArmor(8.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
            }
        } else
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 2)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 2)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel1"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xkeel2"))
        {
            if(chunkDamageVisible("Keel2") < 2)
                hitChunk("Keel2", shot);
        } else
        if(s.startsWith("xkeel3"))
        {
            if(chunkDamageVisible("Keel3") < 2)
                hitChunk("Keel3", shot);
        } else
        if(s.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 2)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder2") < 2)
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
            if(chunkDamageVisible("WingLIn") < 2)
                hitChunk("WingLIn", shot);
        } else
        if(s.startsWith("xwingrin"))
        {
            if(chunkDamageVisible("WingRIn") < 2)
                hitChunk("WingRIn", shot);
        } else
        if(s.startsWith("xwinglout"))
        {
            if(chunkDamageVisible("WingLOut") < 2)
                hitChunk("WingLOut", shot);
        } else
        if(s.startsWith("xwingrout"))
        {
            if(chunkDamageVisible("WingROut") < 2)
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
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(12.88F, 16.96F), shot) > 0.0F)
            {
                debuggunnery("Undercarriage: Stuck..");
                this.FM.AS.setInternalDamage(shot.initiator, 3);
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
            this.FM.turret[0].bIsOperable = false;
            break;

        case 3:
            this.FM.turret[1].bIsOperable = false;
            break;

        case 4:
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
        double d = ((double)fSightCurSpeed / 3.6D) * Math.sqrt((double)fSightCurAltitude * 0.203873598D);
        d -= (double)(fSightCurAltitude * fSightCurAltitude) * 1.419E-005D;
        fSightSetForwardAngle = (float)Math.toDegrees(Math.atan(d / (double)fSightCurAltitude));
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

    public void update(float f)
    {
        if((this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode())
        {
            float f1 = this.FM.EI.engines[0].getRPM();
            if(f1 < 300F && f1 > 30F)
                ((RealFlightModel)this.FM).producedShakeLevel = (1500F - f1) / 3000F;
            float f3 = this.FM.EI.engines[0].getRPM();
            if(f3 < 1000F && f3 > 301F)
                ((RealFlightModel)this.FM).producedShakeLevel = (1500F - f3) / 8000F;
            float f5 = this.FM.EI.engines[0].getRPM();
            if(f5 > 1001F && f5 < 1500F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.07F;
            float f7 = this.FM.EI.engines[0].getRPM();
            if(f7 > 1501F && f7 < 2000F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.05F;
            float f9 = this.FM.EI.engines[0].getRPM();
            if(f9 > 2001F && f9 < 2500F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.04F;
            float f11 = this.FM.EI.engines[0].getRPM();
            if(f11 > 2501F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.03F;
        }
        if((this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode())
        {
            float f2 = this.FM.EI.engines[1].getRPM();
            if(f2 < 300F && f2 > 30F)
                ((RealFlightModel)this.FM).producedShakeLevel = (1500F - f2) / 3000F;
            float f4 = this.FM.EI.engines[1].getRPM();
            if(f4 < 1000F && f4 > 301F)
                ((RealFlightModel)this.FM).producedShakeLevel = (1500F - f4) / 8000F;
            float f6 = this.FM.EI.engines[1].getRPM();
            if(f6 > 1001F && f6 < 1500F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.07F;
            float f8 = this.FM.EI.engines[1].getRPM();
            if(f8 > 1501F && f8 < 2000F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.05F;
            float f10 = this.FM.EI.engines[1].getRPM();
            if(f10 > 2001F && f10 < 2500F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.04F;
            float f12 = this.FM.EI.engines[1].getRPM();
            if(f12 > 2501F)
                ((RealFlightModel)this.FM).producedShakeLevel = 0.03F;
        }
        if(this.FM.getSpeedKMH() > 250F && this.FM.getVertSpeed() > 0.0F && this.FM.getAltitude() < 5000F)
            this.FM.producedAF.x += 20F * (250F - this.FM.getSpeedKMH());
        if(this.FM.isPlayers() && this.FM.Sq.squareElevators > 0.0F)
        {
            if(this.FM.getSpeedKMH() > 300F && this.FM.getSpeedKMH() < 400F)
            {
                this.FM.SensPitch = 0.25F;
                this.FM.producedAM.y -= 300F * (200F - this.FM.getSpeedKMH());
            }
            if(this.FM.getSpeedKMH() >= 400F)
            {
                this.FM.SensPitch = 0.22F;
                this.FM.producedAM.y -= 150F * (200F - this.FM.getSpeedKMH());
            } else
            {
                this.FM.SensPitch = 0.3F;
            }
        }
        super.update(f);
    }

    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    public float fSightSetForwardAngle;

    static 
    {
        Class class1 = ManchesterBombers.class;
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
    }
}
