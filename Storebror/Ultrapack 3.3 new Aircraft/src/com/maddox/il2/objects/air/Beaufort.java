package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class Beaufort extends Scheme2
    implements TypeBomber
{

    public Beaufort()
    {
        bombBayDoorsRemoved = false;
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 850F;
        fSightCurSpeed = 150F;
        fSightCurReadyness = 0.0F;
    }

    public boolean needsOpenBombBay()
    {
        return !bombBayDoorsRemoved;
    }

    public boolean canOpenBombBay()
    {
        return !bombBayDoorsRemoved;
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.4F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected void moveBayDoor(float f)
    {
        if(!this.thisWeaponsName.startsWith("1xt"))
        {
            hierMesh().chunkSetAngles("BayDoorL_D0", 0.0F, -90F * f, 0.0F);
            hierMesh().chunkSetAngles("BayDoorR_D0", 0.0F, -90F * f, 0.0F);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 13:
            killPilot(this, 1);
            break;

        case 33:
        case 34:
            hitProp(0, j, actor);
            cut("Engine1");
            break;

        case 36:
        case 37:
            hitProp(1, j, actor);
            cut("Engine2");
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.2F, 0.0F, 0.165F);
        hierMesh().chunkSetLocate("GearL25_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.2F, 0.0F, 0.165F);
        hierMesh().chunkSetLocate("GearR25_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("e1"))
                    getEnergyPastArmor(12.1D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("e2"))
                    getEnergyPastArmor(12.1D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("p1"))
                    getEnergyPastArmor(12.7F, shot);
                if(s.endsWith("p2"))
                    getEnergyPastArmor(12.7F, shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                int i = s.charAt(10) - 48;
                switch(i)
                {
                default:
                    break;

                case 1:
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.15F)
                    {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        if(World.Rnd().nextFloat() < 0.15F)
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        if(World.Rnd().nextFloat() < 0.15F)
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                    }
                    break;

                case 2:
                    if(getEnergyPastArmor(1.5F, shot) <= 0.0F)
                        break;
                    if(World.Rnd().nextFloat() < 0.15F)
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                    if(World.Rnd().nextFloat() < 0.15F)
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                    if(World.Rnd().nextFloat() < 0.15F)
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 7);
                    break;

                case 3:
                case 4:
                    if(getEnergyPastArmor(6F, shot) <= 0.0F)
                        break;
                    if(World.Rnd().nextFloat() < 0.5F)
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                    if(World.Rnd().nextFloat() < 0.5F)
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                    break;

                case 5:
                case 6:
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                    break;
                }
                return;
            }
            if(s.startsWith("xxengine"))
            {
                int j = 0;
                if(s.startsWith("xxengine2"))
                    j = 1;
                if(getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F)
                {
                    if(World.Rnd().nextFloat() < shot.power / 280000F)
                        this.FM.AS.setEngineStuck(shot.initiator, j);
                    if(World.Rnd().nextFloat() < shot.power / 100000F)
                        this.FM.AS.hitEngine(shot.initiator, j, 2);
                }
                if(getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[j].getCylindersRatio() * 0.75F)
                {
                    this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 25200F)));
                    if(World.Rnd().nextFloat() < shot.power / 80000F)
                        this.FM.AS.hitEngine(shot.initiator, j, 2);
                }
                getEnergyPastArmor(25F, shot);
                return;
            }
            if(s.startsWith("xxmgun"))
            {
                int k = s.charAt(6) - 49;
                if(getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    this.FM.AS.setJamBullets(0, k);
                    getEnergyPastArmor(11.98F, shot);
                }
                return;
            }
            if(s.startsWith("xxoil"))
            {
                int l = 0;
                if(s.endsWith("2"))
                    l = 1;
                if(getEnergyPastArmor(0.21F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2435F)
                    this.FM.AS.hitOil(shot.initiator, l);
                Aircraft.debugprintln(this, "*** Engine (" + l + ") Module: Oil Tank Pierced..");
                return;
            }
            if(s.startsWith("xxprop1") && getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F)
                if(World.Rnd().nextFloat() < 0.5F)
                {
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                    Aircraft.debugprintln(this, "*** Engine1 Module: Prop Governor Hit, Disabled..");
                } else
                {
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                    Aircraft.debugprintln(this, "*** Engine1 Module: Prop Governor Hit, Damaged..");
                }
            if(s.startsWith("xxprop2") && getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F)
                if(World.Rnd().nextFloat() < 0.5F)
                {
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 3);
                    Aircraft.debugprintln(this, "*** Engine2 Module: Prop Governor Hit, Disabled..");
                } else
                {
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 4);
                    Aircraft.debugprintln(this, "*** Engine2 Module: Prop Governor Hit, Damaged..");
                }
            if(s.startsWith("xxspar"))
            {
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                if(s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 1 && getEnergyPastArmor(9.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                    nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                return;
            }
            if(s.startsWith("xxstruts"))
            {
                if(s.startsWith("xxstruts1") && chunkDamageVisible("Engine1") > 1 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 8F), shot) > 0.0F)
                    nextDMGLevels(1, 2, "Engine1_D2", shot.initiator);
                if(s.startsWith("xxstruts2") && chunkDamageVisible("Engine2") > 1 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 8F), shot) > 0.0F)
                    nextDMGLevels(1, 2, "Engine2_D2", shot.initiator);
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int i1 = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.9F, shot) > 0.0F && World.Rnd().nextFloat() < 11.25F)
                    if(shot.power < 12000F)
                    {
                        if(this.FM.AS.astateTankStates[i1] == 0)
                        {
                            this.FM.AS.hitTank(shot.initiator, i1, 1);
                            this.FM.AS.doSetTankState(shot.initiator, i1, 1);
                        }
                        if(this.FM.AS.astateTankStates[i1] < 4 && World.Rnd().nextFloat() < 0.1F)
                            this.FM.AS.hitTank(shot.initiator, i1, 1);
                        if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.15F)
                            this.FM.AS.hitTank(shot.initiator, i1, 10);
                    } else
                    {
                        this.FM.AS.hitTank(shot.initiator, i1, World.Rnd().nextInt(0, (int)(shot.power / 40000F)));
                    }
                return;
            } else
            {
                return;
            }
        }
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(point3d.x > 0.5D)
            {
                if(point3d.z > 0.913D)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                if(point3d.z > 0.341D)
                {
                    if(point3d.x < 1.402D)
                    {
                        if(point3d.y > 0.0D)
                            this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                        else
                            this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                    } else
                    {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    }
                } else
                if(point3d.y > 0.0D)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                else
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                if(point3d.x > 1.691D && point3d.x < 1.98D)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
        } else
        if(s.startsWith("xnose"))
            hitChunk("Nose", shot);
        else
        if(s.startsWith("xtail"))
            hitChunk("Tail1", shot);
        else
        if(s.startsWith("xkeel1"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder1"))
            hitChunk("Rudder1", shot);
        else
        if(s.startsWith("xstabl"))
            hitChunk("StabL", shot);
        else
        if(s.startsWith("xstabr"))
            hitChunk("StabR", shot);
        else
        if(s.startsWith("xvatorl"))
            hitChunk("VatorL", shot);
        else
        if(s.startsWith("xvatorr"))
            hitChunk("VatorR", shot);
        else
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
            hitChunk("AroneL", shot);
        else
        if(s.startsWith("xaroner"))
            hitChunk("AroneR", shot);
        else
        if(s.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
            this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 45000F));
        } else
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
            this.FM.EI.engines[1].setReadyness(shot.initiator, this.FM.EI.engines[1].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 45000F));
        } else
        if(s.startsWith("xgearl"))
            hitChunk("GearL2", shot);
        else
        if(s.startsWith("xgearr"))
            hitChunk("GearR2", shot);
        else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int j1;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                j1 = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
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

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            hierMesh().chunkVisible("Head1_D0", false);
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;

        case 2:
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("HMask3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            break;

        case 3:
            hierMesh().chunkVisible("Pilot4_D0", false);
            hierMesh().chunkVisible("HMask4_D0", false);
            hierMesh().chunkVisible("Pilot4_D1", true);
            break;
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(!bombBayDoorsRemoved)
            if(hierMesh().isChunkVisible("BaydoorL_D0") && hierMesh().isChunkVisible("CF_D1"))
            {
                hierMesh().chunkVisible("BaydoorL_D0", false);
                hierMesh().chunkVisible("BaydoorR_D0", false);
                hierMesh().chunkVisible("BaydoorL_D1", true);
                hierMesh().chunkVisible("BaydoorR_D1", true);
            } else
            if(hierMesh().isChunkVisible("BaydoorL_D1") && hierMesh().isChunkVisible("CF_D2"))
            {
                hierMesh().chunkVisible("BaydoorL_D1", false);
                hierMesh().chunkVisible("BaydoorR_D1", false);
                hierMesh().chunkVisible("BaydoorL_D2", true);
                hierMesh().chunkVisible("BaydoorR_D2", true);
            }
        for(int i = 1; i < 5; i++)
            if(this.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void update(float f)
    {
        super.update(f);
        if(!isChunkAnyDamageVisible("Nose_D") && !hierMesh().isChunkVisible("Nose_CAP") && Main3D.cur3D().isViewOutside())
            hierMesh().chunkVisible("Nose_CAP", true);
        else
        if(!Main3D.cur3D().isViewOutside())
            hierMesh().chunkVisible("Nose_CAP", false);
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

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        this.FM.CT.bHasCockpitDoorControl = true;
        this.FM.CT.dvCockpitDoor = 0.65F;
        this.FM.CT.bHasBayDoorControl = true;
    }

    protected boolean bombBayDoorsRemoved;
    protected float flapps[] = {
        0.0F, 0.0F
    };
    public boolean bSightAutomation;
    private boolean bSightBombDump;
    public float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;

    static 
    {
        Class class1 = Beaufort.class;
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
    }
}
