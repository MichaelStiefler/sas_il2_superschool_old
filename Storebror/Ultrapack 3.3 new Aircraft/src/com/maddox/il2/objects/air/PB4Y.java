package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;

public abstract class PB4Y extends Scheme4
    implements TypeTransport
{

    public PB4Y()
    {
        fCSink = 0.0F;
        fCSteer = 0.0F;
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        fCSteer = 22.2F * f;
    }

    protected void moveFlap(float f)
    {
        float f1 = -35.5F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        int i = 0;
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxammo"))
            {
                int j = s.charAt(6) - 48;
                if(s.length() > 7)
                    j = 10;
                if(getEnergyPastArmor(6.87F, shot) > 0.0F && World.Rnd().nextFloat() < 0.05F)
                {
                    switch(j)
                    {
                    case 1:
                        j = 10;
                        i = 0;
                        break;

                    case 2:
                        j = 10;
                        i = 1;
                        break;

                    case 3:
                        j = 11;
                        i = 0;
                        break;

                    case 4:
                        j = 11;
                        i = 1;
                        break;

                    case 5:
                        j = 12;
                        i = 0;
                        break;

                    case 6:
                        j = 12;
                        i = 1;
                        break;

                    case 7:
                        j = 13;
                        i = 0;
                        break;

                    case 8:
                        j = 14;
                        i = 0;
                        break;

                    case 9:
                        j = 15;
                        i = 0;
                        break;

                    case 10:
                        j = 15;
                        i = 1;
                        break;
                    }
                    this.FM.AS.setJamBullets(j, i);
                    return;
                }
            }
            if(s.startsWith("xxcontrols"))
            {
                int k = s.charAt(10) - 48;
                switch(k)
                {
                default:
                    break;

                case 1:
                case 2:
                    if(getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Aileron Controls Out..");
                    }
                    break;

                case 3:
                    if(World.Rnd().nextFloat() < 0.125F && getEnergyPastArmor(5.2F, shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    getEnergyPastArmor(2.0F, shot);
                    break;

                case 4:
                case 5:
                case 6:
                    if(World.Rnd().nextFloat() < 0.252F && getEnergyPastArmor(5.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.125F)
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        if(World.Rnd().nextFloat() < 0.125F)
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    getEnergyPastArmor(2.0F, shot);
                    break;
                }
                return;
            }
            if(s.startsWith("xxeng"))
            {
                int l = s.charAt(5) - 49;
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(0.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 140000F)
                        {
                            this.FM.AS.setEngineStuck(shot.initiator, l);
                            Aircraft.debugprintln(this, "*** Engine (" + l + ") Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 85000F)
                        {
                            this.FM.AS.hitEngine(shot.initiator, l, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + l + ") Crank Case Hit - Engine Damaged..");
                        }
                    } else
                    if(World.Rnd().nextFloat() < 0.005F)
                    {
                        this.FM.EI.engines[l].setCyliderKnockOut(shot.initiator, 1);
                    } else
                    {
                        this.FM.EI.engines[l].setReadyness(shot.initiator, this.FM.EI.engines[l].getReadyness() - 0.00082F);
                        Aircraft.debugprintln(this, "*** Engine (" + l + ") Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[l].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(5.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[l].getCylindersRatio() * 0.75F)
                    {
                        this.FM.EI.engines[l].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        Aircraft.debugprintln(this, "*** Engine (" + l + ") Cylinders Hit, " + this.FM.EI.engines[l].getCylindersOperable() + "/" + this.FM.EI.engines[l].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 18000F)
                        {
                            this.FM.AS.hitEngine(shot.initiator, l, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + l + ") Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("mag1"))
                {
                    this.FM.EI.engines[l].setMagnetoKnockOut(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine (" + l + ") Module: Magneto #0 Destroyed..");
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("mag2"))
                {
                    this.FM.EI.engines[l].setMagnetoKnockOut(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Engine (" + l + ") Module: Magneto #1 Destroyed..");
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("oil1") && getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                {
                    this.FM.AS.setOilState(shot.initiator, l, 1);
                    Aircraft.debugprintln(this, "*** Engine (" + l + ") Module: Oil Filter Pierced..");
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
            if(s.startsWith("xxoil"))
            {
                int i1 = s.charAt(5) - 49;
                if(getEnergyPastArmor(0.21F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2435F)
                    this.FM.AS.hitOil(shot.initiator, i1);
                Aircraft.debugprintln(this, "*** Engine (" + i1 + ") Module: Oil Tank Pierced..");
                return;
            }
            if(s.startsWith("xxpnm"))
            {
                if(getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 1.22F), shot) > 0.0F)
                {
                    debuggunnery("Pneumo System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 1);
                }
                return;
            }
            if(s.startsWith("xxradio"))
            {
                getEnergyPastArmor(World.Rnd().nextFloat(5F, 25F), shot);
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(s.startsWith("xxspark1") && chunkDamageVisible("Keel1") > 1 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Keel1 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D" + chunkDamageVisible("Keel1"), shot.initiator);
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
            if(s.startsWith("xxtank"))
            {
                int j1 = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.06F, shot) > 0.0F)
                {
                    if(this.FM.AS.astateTankStates[j1] == 0)
                    {
                        this.FM.AS.hitTank(shot.initiator, j1, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j1, 1);
                    }
                    if(shot.powerType == 3)
                    {
                        if(shot.power < 16100F)
                        {
                            if(this.FM.AS.astateTankStates[j1] < 4 && World.Rnd().nextFloat() < 0.21F)
                                this.FM.AS.hitTank(shot.initiator, j1, 1);
                        } else
                        {
                            this.FM.AS.hitTank(shot.initiator, j1, World.Rnd().nextInt(1, 1 + (int)(shot.power / 16100F)));
                        }
                    } else
                    if(shot.power > 16100F)
                        this.FM.AS.hitTank(shot.initiator, j1, World.Rnd().nextInt(1, 1 + (int)(shot.power / 16100F)));
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
            return;
        }
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
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
            if(chunkDamageVisible("Rudder1") < 1)
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
        if(s.startsWith("xwinglin"))
        {
            if(chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            return;
        }
        if(s.startsWith("xwingrin"))
        {
            if(chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            return;
        }
        if(s.startsWith("xwinglmid"))
        {
            if(chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            return;
        }
        if(s.startsWith("xwingrmid"))
        {
            if(chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            return;
        }
        if(s.startsWith("xwinglout"))
        {
            if(chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            return;
        }
        if(s.startsWith("xwingrout"))
        {
            if(chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
            return;
        }
        if(s.startsWith("xaronel"))
        {
            if(chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL", shot);
            return;
        }
        if(s.startsWith("xaroner"))
        {
            if(chunkDamageVisible("AroneR") < 1)
                hitChunk("AroneR", shot);
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
        if(s.startsWith("xgear"))
        {
            if(World.Rnd().nextFloat() < 0.05F)
            {
                Aircraft.debugprintln(this, "*** Gear Hydro Failed..");
                this.FM.Gears.setHydroOperable(false);
            }
            return;
        }
        if(s.startsWith("xturret"))
            return;
        if(s.startsWith("xmgun"))
        {
            int k1 = 10 * (s.charAt(5) - 48) + (s.charAt(6) - 48);
            if(getEnergyPastArmor(6.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.35F)
            {
                switch(k1)
                {
                case 1:
                    k1 = 10;
                    i = 0;
                    break;

                case 2:
                    k1 = 10;
                    i = 1;
                    break;

                case 3:
                    k1 = 11;
                    i = 0;
                    break;

                case 4:
                    k1 = 11;
                    i = 1;
                    break;

                case 5:
                    k1 = 12;
                    i = 0;
                    break;

                case 6:
                    k1 = 12;
                    i = 1;
                    break;

                case 7:
                    k1 = 13;
                    i = 0;
                    break;

                case 8:
                    k1 = 14;
                    i = 0;
                    break;

                case 9:
                    k1 = 15;
                    i = 0;
                    break;

                case 10:
                    k1 = 15;
                    i = 1;
                    break;
                }
                this.FM.AS.setJamBullets(k1, i);
            }
            return;
        }
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int l1;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                l1 = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                l1 = s.charAt(6) - 49;
            } else
            {
                l1 = s.charAt(5) - 49;
            }
            hitFlesh(l1, shot, byte0);
            return;
        } else
        {
            return;
        }
    }

    public void msgExplosion(Explosion explosion)
    {
        setExplosion(explosion);
        if(explosion.chunkName != null && explosion.power > 0.0F)
        {
            if(explosion.chunkName.equals("Tail1_D3"))
                return;
            if(explosion.chunkName.equals("WingLIn_D3"))
                return;
            if(explosion.chunkName.equals("WingRIn_D3"))
                return;
            if(explosion.chunkName.equals("WingLMid_D3"))
                return;
            if(explosion.chunkName.equals("WingRMid_D3"))
                return;
            if(explosion.chunkName.equals("WingLOut_D3"))
                return;
            if(explosion.chunkName.equals("WingROut_D3"))
                return;
        }
        super.msgExplosion(explosion);
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f2, 0.05F, 0.75F, 0.0F, -55F), 0.0F);
        hiermesh.chunkSetAngles("GearC10_D0", 0.0F, Aircraft.cvt(f2, 0.05F, 0.75F, 0.0F, -60F), 0.0F);
        hiermesh.chunkSetAngles("GearC8_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.1F, 0.0F, -140F), 0.0F);
        hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.1F, 0.0F, -140F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -30F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, 180F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -98F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -20F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f1, 0.02F, 0.82F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f1, 0.02F, 0.82F, 0.0F, -30F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f1, 0.02F, 0.82F, 0.0F, 180F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f1, 0.02F, 0.82F, 0.0F, -98F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f1, 0.02F, 0.82F, 0.0F, -20F), 0.0F);
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    public void moveWheelSink()
    {
        fCSink = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.5F, 0.0F, 0.5F);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.456F, 0.0F, 0.2821F);
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.456F, 0.0F, 0.2821F);
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33:
            hitProp(1, j, actor);
            this.FM.EI.engines[1].setEngineStuck(actor);
            this.FM.AS.hitTank(actor, 1, World.Rnd().nextInt(0, 9));
            // fall through

        case 34:
            hitProp(0, j, actor);
            this.FM.EI.engines[0].setEngineStuck(actor);
            this.FM.AS.hitTank(actor, 0, World.Rnd().nextInt(2, 8));
            this.FM.AS.hitTank(actor, 1, World.Rnd().nextInt(0, 5));
            // fall through

        case 35:
            this.FM.AS.hitTank(actor, 0, World.Rnd().nextInt(0, 4));
            break;

        case 36:
            hitProp(2, j, actor);
            this.FM.EI.engines[2].setEngineStuck(actor);
            this.FM.AS.hitTank(actor, 2, World.Rnd().nextInt(0, 9));
            // fall through

        case 37:
            hitProp(3, j, actor);
            this.FM.EI.engines[3].setEngineStuck(actor);
            this.FM.AS.hitTank(actor, 2, World.Rnd().nextInt(0, 5));
            this.FM.AS.hitTank(actor, 3, World.Rnd().nextInt(2, 8));
            // fall through

        case 38:
            this.FM.AS.hitTank(actor, 3, World.Rnd().nextInt(0, 4));
            break;

        case 25:
            this.FM.turret[0].bIsOperable = false;
            return false;

        case 26:
            this.FM.turret[1].bIsOperable = false;
            return false;

        case 27:
            this.FM.turret[2].bIsOperable = false;
            return false;

        case 28:
            this.FM.turret[3].bIsOperable = false;
            return false;

        case 29:
            this.FM.turret[4].bIsOperable = false;
            return false;

        case 30:
            this.FM.turret[5].bIsOperable = false;
            return false;

        case 19:
            killPilot(this, 5);
            killPilot(this, 6);
            killPilot(this, 7);
            killPilot(this, 8);
            cut("StabL");
            cut("StabR");
            break;

        case 13:
            killPilot(this, 0);
            killPilot(this, 1);
            killPilot(this, 2);
            killPilot(this, 3);
            break;

        case 17:
            cut("Keel1");
            hierMesh().chunkVisible("Keel1_CAP", false);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveBayDoor(float f)
    {
        for(int i = 1; i < 10; i++)
            hierMesh().chunkSetAngles("Bay0" + i + "_D0", 0.0F, -30F * f, 0.0F);

        for(int j = 10; j < 13; j++)
            hierMesh().chunkSetAngles("Bay" + j + "_D0", 0.0F, -30F * f, 0.0F);

    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag)
        {
            if(this.FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this);
            if(this.FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this);
            if(this.FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this);
            if(this.FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this);
            if(!(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode())
            {
                for(int i = 0; i < this.FM.EI.getNum(); i++)
                    if(this.FM.AS.astateEngineStates[i] > 3 && World.Rnd().nextFloat() < 0.2F)
                        this.FM.EI.engines[i].setExtinguisherFire();

            }
        }
        for(int j = 1; j < 10; j++)
            if(this.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + j + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + j + "_D0", hierMesh().isChunkVisible("Pilot" + j + "_D0"));

    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 2:
            this.FM.turret[0].setHealth(f);
            break;

        case 4:
            this.FM.turret[1].setHealth(f);
            break;

        case 5:
            this.FM.turret[2].setHealth(f);
            break;

        case 6:
            this.FM.turret[3].setHealth(f);
            break;

        case 7:
            this.FM.turret[4].setHealth(f);
            break;

        case 8:
            this.FM.turret[5].setHealth(f);
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
        hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
    }

    public void update(float f)
    {
        super.update(f);
        if(this.FM.CT.getGear() > 0.9F)
        {
            resetYPRmodifier();
            Aircraft.xyz[1] = fCSink;
            Aircraft.ypr[1] = fCSteer;
            hierMesh().chunkSetLocate("GearC3_D0", Aircraft.xyz, Aircraft.ypr);
        }
    }

    private float fCSink;
    private float fCSteer;

    static 
    {
        Class class1 = PB4Y.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
