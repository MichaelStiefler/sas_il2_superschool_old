package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.CLASS;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class KI_49 extends Scheme2 {

    public KI_49() {
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 850F;
        fSightCurSpeed = 150F;
        fSightCurReadyness = 0.0F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.AS.wantBeaconsNet(true);
        this.FM.CT.bHasBayDoorControl = true;
        this.FM.crew = 8;
        this.FM.AS.astatePilotFunctions[4] = 6;
        this.FM.AS.astatePilotFunctions[5] = 7;
        this.FM.AS.astatePilotFunctions[6] = 10;
        this.FM.AS.astatePilotFunctions[7] = 11;
    }

    public void moveSteering(float f) {
        hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void moveWheelSink() {
//        if (FM.CT.getGear() < 0.999F) {
//            return;
//        } else {
//            resetYPRmodifier();
//            hierMesh().chunkSetAngles("GearL10_D0", 0.0F, cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.35F, -38F, -27.5F), 0.0F);
//            hierMesh().chunkSetAngles("GearL11_D0", 0.0F, 0.0F, cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.35F, -45F, -48.25F));
//            xyz[2] = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.35F, 0.0F, -0.325F);
//            hierMesh().chunkSetLocate("GearL12_D0", xyz, ypr);
//            hierMesh().chunkSetAngles("GearR10_D0", 0.0F, cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.35F, -38F, -27.5F), 0.0F);
//            hierMesh().chunkSetAngles("GearR11_D0", 0.0F, 0.0F, cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.35F, -45F, -48.25F));
//            xyz[2] = cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.35F, 0.0F, -0.325F);
//            hierMesh().chunkSetLocate("GearR12_D0", xyz, ypr);
//            return;
//        }
    }

    protected void moveFlap(float f) {
//        float f1 = -45F * f;
//        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
//        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 0.65F) + Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, 0.1F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -0.087F) + Aircraft.cvt(f, 0.2F, 0.9F, 0.0F, 0.033F);
        Aircraft.ypr[1] = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -5.0F) + Aircraft.cvt(f, 0.2F, 0.33F, 0.0F, -15.0F) + Aircraft.cvt(f, 0.33F, 1.0F, 0.0F, -25.0F);
        Aircraft.ypr[0] = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -7.5F);
        Aircraft.ypr[2] = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 1.6F);
        this.hierMesh().chunkSetLocate("Flap01_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.ypr[0] *= -1F;
        Aircraft.ypr[2] *= -1F;
        this.hierMesh().chunkSetLocate("Flap02_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (FM.getAltitude() < 3000F) {
            for (int i = 1; i < 7; i++)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);

        } else {
            for (int j = 1; j < 7; j++)
                hierMesh().chunkVisible("HMask" + j + "_D0", hierMesh().isChunkVisible("Pilot1_D0"));

        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            default:
                break;

            case 33: // '!'
                hitProp(0, j, actor);
                break;

            case 36: // '$'
                hitProp(1, j, actor);
                break;

            case 34: // '"'
                FM.AS.hitEngine(this, 0, 2);
                if (World.Rnd().nextInt(0, 99) < 66)
                    FM.AS.hitEngine(this, 0, 2);
                break;

            case 37: // '%'
                FM.AS.hitEngine(this, 1, 2);
                if (World.Rnd().nextInt(0, 99) < 66)
                    FM.AS.hitEngine(this, 1, 2);
                break;

            case 19: // '\023'
                killPilot(actor, 5);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxbomb")) {
                if (World.Rnd().nextFloat() < 0.001F && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0].haveBullets()) {
                    debugprintln(this, "*** Bomb Payload Detonates..");
                    FM.AS.hitTank(shot.initiator, 0, 10);
                    FM.AS.hitTank(shot.initiator, 1, 10);
                    FM.AS.hitTank(shot.initiator, 2, 10);
                    FM.AS.hitTank(shot.initiator, 3, 10);
                    nextDMGLevels(3, 2, "CF_D0", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxcontrol")) {
                int i = s.charAt(9) - 48;
                switch (i) {
                    default:
                        break;

                    case 1: // '\001'
                    case 2: // '\002'
                        if (getEnergyPastArmor(2.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.1F)
                            FM.AS.setControlsDamage(shot.initiator, 2);
                        break;

                    case 3: // '\003'
                        if (getEnergyPastArmor(2.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2F)
                            FM.AS.setControlsDamage(shot.initiator, 1);
                        break;

                    case 4: // '\004'
                    case 5: // '\005'
                        if (getEnergyPastArmor(2.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                            FM.AS.setControlsDamage(shot.initiator, 0);
                        break;
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                int j = s.charAt(5) - 49;
                if (s.endsWith("case")) {
                    if (getEnergyPastArmor(1.7F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(20000F, 140000F) < shot.power) {
                            FM.AS.setEngineStuck(shot.initiator, j);
                            debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat(10000F, 50000F) < shot.power) {
                            FM.AS.hitEngine(shot.initiator, j, 2);
                            debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.04F) {
                        FM.EI.engines[j].setCyliderKnockOut(shot.initiator, 1);
                    } else {
                        FM.EI.engines[j].setReadyness(shot.initiator, FM.EI.engines[j].getReadyness() - 0.02F);
                        debuggunnery("*** Engine" + j + " Crank Case Hit - Readyness Reduced to " + FM.EI.engines[j].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if (getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[j].getCylindersRatio() * 0.9878F) {
                        FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        debuggunnery("*** Engine" + j + " Cylinders Hit, " + FM.EI.engines[j].getCylindersOperable() + "/" + FM.EI.engines[j].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 48000F) {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            debuggunnery("*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("supc")) {
                    if (getEnergyPastArmor(0.05F, shot) > 0.0F)
                        FM.EI.engines[j].setKillCompressor(shot.initiator);
                    getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("prop")) {
                    if (getEnergyPastArmor(0.1F, shot) > 0.0F) {
                        if (Pd.y > 0.0D && Pd.z < 0.18899999558925629D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power)
                            FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 0);
                        if (Pd.y < 0.0D && Pd.z < 0.18899999558925629D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power)
                            FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 1);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 6);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 1);
                    }
                    getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("oil1") && getEnergyPastArmor(4.21F, shot) > 0.0F) {
                    FM.AS.hitOil(shot.initiator, j);
                    getEnergyPastArmor(0.42F, shot);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                if (s.startsWith("xxlockal")) {
                    if (getEnergyPastArmor(4.35F, shot) > 0.0F) {
                        debuggunnery("*** AroneL Lock Damaged..");
                        nextDMGLevels(1, 2, "AroneL_D0", shot.initiator);
                    }
                } else if (s.startsWith("xxlockar")) {
                    if (getEnergyPastArmor(4.35F, shot) > 0.0F) {
                        debuggunnery("*** AroneR Lock Damaged..");
                        nextDMGLevels(1, 2, "AroneR_D0", shot.initiator);
                    }
                } else if (s.startsWith("xxlockvl")) {
                    if (getEnergyPastArmor(4.32F, shot) > 0.0F) {
                        debuggunnery("*** VatorL Lock Damaged..");
                        nextDMGLevels(1, 2, "VatorL_D0", shot.initiator);
                    }
                } else if (s.startsWith("xxlockvr")) {
                    if (getEnergyPastArmor(4.32F, shot) > 0.0F) {
                        debuggunnery("*** VatorR Lock Damaged..");
                        nextDMGLevels(1, 2, "VatorR_D0", shot.initiator);
                    }
                } else if (s.startsWith("xxlockr") && getEnergyPastArmor(4.32F, shot) > 0.0F) {
                    debuggunnery("*** Rudder1 Lock Damaged..");
                    nextDMGLevels(1, 2, "Rudder1_D0", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                if ((s.endsWith("li1") || s.endsWith("li2")) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    debuggunnery("*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if ((s.endsWith("ri1") || s.endsWith("ri2")) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    debuggunnery("*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if ((s.endsWith("lm1") || s.endsWith("lm2")) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    debuggunnery("*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if ((s.endsWith("rm1") || s.endsWith("rm2")) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    debuggunnery("*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if ((s.endsWith("lo1") || s.endsWith("lo2")) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    debuggunnery("*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if ((s.endsWith("ro1") || s.endsWith("ro2")) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    debuggunnery("*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.endsWith("e1") && getEnergyPastArmor(28F, shot) > 0.0F) {
                    debuggunnery("*** Engine1 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if (s.endsWith("e3") && getEnergyPastArmor(28F, shot) > 0.0F) {
                    debuggunnery("*** Engine2 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                int k = s.charAt(5) - 49;
                if (getEnergyPastArmor(4.21F, shot) > 0.0F) {
                    FM.AS.hitOil(shot.initiator, k);
                    getEnergyPastArmor(0.42F, shot);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int l = s.charAt(6) - 49;
                if (getEnergyPastArmor(0.06F, shot) > 0.0F) {
                    if (FM.AS.astateTankStates[l] == 0) {
                        FM.AS.hitTank(shot.initiator, l, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if (shot.powerType == 3) {
                        if (shot.power < 16100F) {
                            if (FM.AS.astateTankStates[l] < 4 && World.Rnd().nextFloat() < 0.21F)
                                FM.AS.hitTank(shot.initiator, l, 1);
                        } else {
                            FM.AS.hitTank(shot.initiator, l, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
                        }
                    } else if (shot.power > 16100F)
                        FM.AS.hitTank(shot.initiator, l, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
                }
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcf")) {
            if (chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else if (s.startsWith("xtail")) {
            if (chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) {
            if (chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else if (s.startsWith("xrudder")) {
            if (chunkDamageVisible("Rudder1") < 2)
                hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstabl")) {
            if (chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
        } else if (s.startsWith("xstabr")) {
            if (chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
        } else if (s.startsWith("xvatorl")) {
            if (chunkDamageVisible("VatorL") < 2)
                hitChunk("VatorL", shot);
        } else if (s.startsWith("xvatorr")) {
            if (chunkDamageVisible("VatorR") < 2)
                hitChunk("VatorR", shot);
        } else if (s.startsWith("xwinglin")) {
            if (chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
        } else if (s.startsWith("xwingrin")) {
            if (chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
        } else if (s.startsWith("xwinglmid")) {
            if (chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
        } else if (s.startsWith("xwingrmid")) {
            if (chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
        } else if (s.startsWith("xwinglout")) {
            if (chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
        } else if (s.startsWith("xwingrout")) {
            if (chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else if (s.startsWith("xaronel")) {
            if (chunkDamageVisible("AroneL") < 2)
                hitChunk("AroneL", shot);
        } else if (s.startsWith("xaroner")) {
            if (chunkDamageVisible("AroneR") < 2)
                hitChunk("AroneR", shot);
        } else if (s.startsWith("xengine1")) {
            if (chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else if (s.startsWith("xengine2")) {
            if (chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
        } else {
            if (s.startsWith("xturret")) {
                if (s.startsWith("xturret1"))
                    FM.AS.setJamBullets(10, 0);
                if (s.startsWith("xturret2"))
                    FM.AS.setJamBullets(11, 0);
                if (s.startsWith("xturret3"))
                    FM.AS.setJamBullets(12, 0);
                if (s.startsWith("xturret4"))
                    FM.AS.setJamBullets(13, 0);
                return;
            }
            if (s.startsWith("xgear")) {
                if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
                    debuggunnery("Hydro System: Disabled..");
                    FM.AS.setInternalDamage(shot.initiator, 0);
                }
                if (s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(12.88F, 16.96F), shot) > 0.0F) {
                    debuggunnery("Undercarriage: Stuck..");
                    FM.AS.setInternalDamage(shot.initiator, 3);
                }
            } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
                byte byte0 = 0;
                int i1;
                if (s.endsWith("a") || s.endsWith("a2")) {
                    byte0 = 1;
                    i1 = s.charAt(6) - 49;
                } else if (s.endsWith("b") || s.endsWith("b2")) {
                    byte0 = 2;
                    i1 = s.charAt(6) - 49;
                } else {
                    i1 = s.charAt(5) - 49;
                }
                hitFlesh(i1, shot, byte0);
            }
        }
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 2: // '\002'
                FM.turret[0].setHealth(f);
                break;

            case 4: // '\004'
                FM.turret[1].setHealth(f);
                break;

            case 5: // '\005'
                FM.turret[2].setHealth(f);
                break;
        }
    }

    public void doMurderPilot(int i) {
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
        if (i == 0)
            hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
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
        fSightCurSideslip += 0.05F;
        if(fSightCurSideslip > 3F)
            fSightCurSideslip = 3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Float(fSightCurSideslip * 10F)
        });
    }

    public void typeBomberAdjSideslipMinus()
    {
        fSightCurSideslip -= 0.05F;
        if(fSightCurSideslip < -3F)
            fSightCurSideslip = -3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Float(fSightCurSideslip * 10F)
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
            if((double)fSightCurDistance < (double)(fSightCurSpeed / 3.6F) * Math.sqrt(fSightCurAltitude * 0.2038736F))
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

    protected void moveBayDoor(float f) {
        hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -100F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -100F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay3_D0", 0.0F, -100F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -100F * f, 0.0F);
    }

    public void update(float f) {
        super.update(f);
        for (int i = 0; i < 2; i++) {
            float f1 = FM.EI.engines[i].getControlRadiator();
            if (Math.abs(flapps[i] - f1) <= 0.01F)
                continue;
            flapps[i] = f1;
            for (int j = 1; j < 10; j++) {
                String s = "RAD" + (i != 0 ? "R" : "L") + "0" + j + "_D0";
                hierMesh().chunkSetAngles(s, 0.0F, -20F * f1, 0.0F);
            }

            for (int k = 10; k < 13; k++) {
                String s1 = "RAD" + (i != 0 ? "R" : "L") + "" + k + "_D0";
                hierMesh().chunkSetAngles(s1, 0.0F, -20F * f1, 0.0F);
            }

        }

    }

    private float flapps[] = { 0.0F, 0.0F };
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;

    static {
        Class class1 = CLASS.THIS();
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
    }
}
