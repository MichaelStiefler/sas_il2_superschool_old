package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class Do17 extends FLAME2_SCHEME2a
    implements TypeBomber, TypeTransport
{

    public Do17()
    {
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurAltitude = 300F;
        fSightCurSpeed = 50F;
        fSightCurForwardAngle = 0.0F;
        fSightSetForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurReadyness = 0.0F;
        bPitUnfocused = true;
        wheel1 = 0.0F;
        wheel2 = 0.0F;
        kl = 1.0F;
        kr = 1.0F;
        kc = 1.0F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        this.FM.Gears.computePlaneLandPose(this.FM);
    }

    public void rareAction(float paramFloat, boolean paramBoolean)
    {
        super.rareAction(paramFloat, paramBoolean);
        if(paramBoolean)
        {
            if(this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.39F)
                this.FM.AS.hitTank(this, 0, 1);
            if(this.FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.39F)
                this.FM.AS.hitTank(this, 1, 1);
            if(this.FM.AS.astateEngineStates[2] > 3 && World.Rnd().nextFloat() < 0.39F)
                this.FM.AS.hitTank(this, 2, 1);
            if(this.FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this);
            if(this.FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this);
            if(this.FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this);
            if(this.FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this);
        }
        for(int i = 1; i <= 4; i++)
            if(this.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("hmask" + i + "_d0", false);
            else
                hierMesh().chunkVisible("hmask" + i + "_d0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void doWoundPilot(int paramInt, float paramFloat)
    {
        switch(paramInt)
        {
        case 1:
            this.FM.turret[0].setHealth(paramFloat);
            break;

        case 2:
            this.FM.turret[1].setHealth(paramFloat);
            break;

        case 3:
            this.FM.turret[2].setHealth(paramFloat);
            break;
        }
    }

    public void doMurderPilot(int paramInt)
    {
        switch(paramInt)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("hmask1_d0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("hmask2_d0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;

        case 2:
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("hmask3_d0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            break;

        case 3:
            hierMesh().chunkVisible("Pilot4_D0", false);
            hierMesh().chunkVisible("hmask4_d0", false);
            hierMesh().chunkVisible("Pilot4_D1", true);
            break;
        }
    }

    protected boolean cutFM(int paramInt1, int paramInt2, Actor paramActor)
    {
        switch(paramInt1)
        {
        default:
            break;

        case 33:
            hitProp(0, paramInt2, paramActor);
            break;

        case 36:
            hitProp(1, paramInt2, paramActor);
            break;

        case 35:
            this.FM.AS.hitEngine(this, 0, 3);
            if(World.Rnd().nextInt(0, 99) < 66)
                this.FM.AS.hitEngine(this, 0, 1);
            break;

        case 38:
            this.FM.AS.hitEngine(this, 1, 3);
            if(World.Rnd().nextInt(0, 99) < 66)
                this.FM.AS.hitEngine(this, 1, 1);
            break;

        case 11:
            hierMesh().chunkVisible("Wire1_D0", false);
            break;

        case 19:
            hierMesh().chunkVisible("Wire1_D0", false);
            this.FM.Gears.hitCentreGear();
            break;

        case 13:
            killPilot(this, 0);
            killPilot(this, 1);
            killPilot(this, 2);
            killPilot(this, 3);
            return false;
        }
        return super.cutFM(paramInt1, paramInt2, paramActor);
    }

    public void update(float paramFloat)
    {
        super.update(paramFloat);
    }

    protected void moveFlap(float paramFloat)
    {
        float f = -50F * paramFloat;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f, 0.0F);
    }

    protected void moveRudder(float paramFloat)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * paramFloat, 0.0F);
        hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -30F * paramFloat, 0.0F);
    }

    public void moveSteering(float paramFloat)
    {
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(paramFloat, -65F, 65F, 65F, -65F), 0.0F);
    }

    public boolean turretAngles(int paramInt, float paramArrayOfFloat[])
    {
        boolean bool = super.turretAngles(paramInt, paramArrayOfFloat);
        float f1 = -paramArrayOfFloat[0];
        float f2 = paramArrayOfFloat[1];
        switch(paramInt)
        {
        default:
            break;

        case 0:
            if(f2 > 45F)
            {
                f2 = 45F;
                bool = false;
            }
            if(f2 < -40F)
            {
                f2 = -40F;
                bool = false;
            }
            if(f1 > 50F)
            {
                f1 = 50F;
                bool = false;
            }
            if(f1 < -25F)
            {
                f1 = -25F;
                bool = false;
            }
            break;

        case 1:
            if(f2 > 80F)
            {
                f2 = 80F;
                bool = false;
            }
            if(f2 < -3F)
            {
                f2 = -3F;
                bool = false;
            }
            if(f1 > 55F)
            {
                f1 = 55F;
                bool = false;
            }
            if(f1 < -55F)
            {
                f1 = -55F;
                bool = false;
            }
            break;

        case 2:
            if(f2 > 45F)
            {
                f2 = 45F;
                bool = false;
            }
            if(f2 < -40F)
            {
                f2 = -40F;
                bool = false;
            }
            if(f1 > 50F)
            {
                f1 = 50F;
                bool = false;
            }
            if(f1 < -50F)
            {
                f1 = -50F;
                bool = false;
            }
            break;

        case 3:
            if(f2 > 35F)
            {
                f2 = 35F;
                bool = false;
            }
            if(f2 < 0.0F)
            {
                f2 = 0.0F;
                bool = false;
            }
            if(f1 > 65F)
            {
                f1 = 65F;
                bool = false;
            }
            if(f1 < -30F)
            {
                f1 = -30F;
                bool = false;
            }
            break;

        case 4:
            if(f2 > 35F)
            {
                f2 = 35F;
                bool = false;
            }
            if(f2 < 0.0F)
            {
                f2 = 0.0F;
                bool = false;
            }
            if(f1 > 30F)
            {
                f1 = 30F;
                bool = false;
            }
            if(f1 >= -65F)
                break;
            f1 = -65F;
            bool = false;
            // fall through

        case 5:
            f2 = 45F;
            bool = false;
            if(f2 < -40F)
            {
                f2 = -40F;
                bool = false;
            }
            if(f1 > 50F)
            {
                f1 = 50F;
                bool = false;
            }
            if(f1 < -25F)
            {
                f1 = -25F;
                bool = false;
            }
            break;
        }
        paramArrayOfFloat[0] = -f1;
        paramArrayOfFloat[1] = f2;
        return bool;
    }

    public void moveWheelSink()
    {
        wheel1 = 0.8F * wheel1 + 0.2F * this.FM.Gears.gWheelSinking[0];
        wheel2 = 0.8F * wheel2 + 0.2F * this.FM.Gears.gWheelSinking[1];
        resetYPRmodifier();
        Aircraft.xyz[1] = -Aircraft.cvt(wheel1, 0.0F, 0.3F, 0.0F, 0.3F);
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = -Aircraft.cvt(wheel2, 0.0F, 0.3F, 0.0F, 0.3F);
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void mydebuggunnery(String paramString)
    {
        System.out.println(paramString);
    }

    protected void setControlDamage(Shot paramShot, int paramInt)
    {
        if(World.Rnd().nextFloat() < 0.002F && getEnergyPastArmor(4F, paramShot) > 0.0F)
            this.FM.AS.setControlsDamage(paramShot.initiator, paramInt);
    }

    protected void hitChunk(String paramString, Shot paramShot)
    {
        super.hitChunk(paramString, paramShot);
    }

    protected void hitBone(String paramString, Shot paramShot, Point3d paramPoint3d)
    {
        if(paramString.startsWith("xx"))
        {
            if(paramString.startsWith("xxarmor"))
                if(paramString.endsWith("p1"))
                {
                    if(Aircraft.v1.z > 0.5D)
                        getEnergyPastArmor(4D / Aircraft.v1.z, paramShot);
                    else
                    if(Aircraft.v1.x > 0.93969261646270752D)
                        getEnergyPastArmor((8D / Aircraft.v1.x) * (double)World.Rnd().nextFloat(1.0F, 1.2F), paramShot);
                    else
                        getEnergyPastArmor(3F, paramShot);
                } else
                if(paramString.endsWith("p2"))
                    getEnergyPastArmor(4D / Math.abs(Aircraft.v1.z), paramShot);
                else
                if(paramString.endsWith("p3"))
                    getEnergyPastArmor((7D / Math.abs(Aircraft.v1.x)) * (double)World.Rnd().nextFloat(1.0F, 1.2F), paramShot);
                else
                    getEnergyPastArmor(3F, paramShot);
            if(paramString.endsWith("p4"))
            {
                if(Aircraft.v1.x > 0.70710676908493042D)
                    getEnergyPastArmor((7D / Aircraft.v1.x) * (double)World.Rnd().nextFloat(1.0F, 1.2F), paramShot);
                else
                if(Aircraft.v1.x > -0.70710676908493042D)
                    getEnergyPastArmor(5F, paramShot);
                else
                    getEnergyPastArmor(3F, paramShot);
            } else
            if(paramString.endsWith("a1") || paramString.endsWith("a2"))
                getEnergyPastArmor(3D, paramShot);
            if(paramString.startsWith("xxarmturr"))
                getEnergyPastArmor(3F, paramShot);
            if(paramString.startsWith("xxspar"))
            {
                getEnergyPastArmor(3F, paramShot);
                if((paramString.endsWith("cf1") || paramString.endsWith("cf2")) && World.Rnd().nextFloat() < 0.1F && chunkDamageVisible("CF") > 1 && getEnergyPastArmor(15.9F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), paramShot) > 0.0F)
                {
                    msgCollision(this, "Tail1_D0", "Tail1_D0");
                    msgCollision(this, "WingLIn_D0", "WingLIn_D0");
                    msgCollision(this, "WingRIn_D0", "WingRIn_D0");
                }
                if((paramString.endsWith("t1") || paramString.endsWith("t2")) && World.Rnd().nextFloat() < 0.1F && chunkDamageVisible("Tail1") > 1 && getEnergyPastArmor(15.9F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), paramShot) > 0.0F)
                    msgCollision(this, "Tail1_D0", "Tail1_D0");
                if((paramString.endsWith("li1") || paramString.endsWith("li2")) && (double)World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLIn") > 1 && getEnergyPastArmor(13.5F * World.Rnd().nextFloat(1.0F, 2.0F), paramShot) > 0.0F)
                    nextDMGLevels(1, 2, "WingLIn_D2", paramShot.initiator);
                if((paramString.endsWith("ri1") || paramString.endsWith("ri2")) && (double)World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingRIn") > 1 && getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 2.0F), paramShot) > 0.0F)
                    nextDMGLevels(1, 2, "WingRIn_D2", paramShot.initiator);
                if((paramString.endsWith("lm1") || paramString.endsWith("lm2")) && (double)World.Rnd().nextFloat() < 1.0D - 0.86D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLMid") > 1 && getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 2.0F), paramShot) > 0.0F)
                    nextDMGLevels(1, 2, "WingLMid_D2", paramShot.initiator);
                if((paramString.endsWith("rm1") || paramString.endsWith("rm2")) && (double)World.Rnd().nextFloat() < 1.0D - 0.86D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingRMid") > 1 && getEnergyPastArmor(13.5F * World.Rnd().nextFloat(1.0F, 2.0F), paramShot) > 0.0F)
                    nextDMGLevels(1, 2, "WingRMid_D2", paramShot.initiator);
                if((paramString.endsWith("lo1") || paramString.endsWith("lo2")) && (double)World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLOut") > 1 && getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), paramShot) > 0.0F)
                    nextDMGLevels(1, 2, "WingLOut_D2", paramShot.initiator);
                if((paramString.endsWith("ro1") || paramString.endsWith("ro2")) && (double)World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingROut") > 1 && getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), paramShot) > 0.0F)
                    nextDMGLevels(1, 2, "WingROut_D2", paramShot.initiator);
                if(paramString.endsWith("e1") && (paramPoint3d.y > 2.79D || paramPoint3d.y < 2.32D) && getEnergyPastArmor(17F, paramShot) > 0.0F)
                    nextDMGLevels(3, 2, "Engine1_D0", paramShot.initiator);
                if(paramString.endsWith("e2") && (paramPoint3d.y < -2.79D || paramPoint3d.y > -2.32D) && getEnergyPastArmor(17F, paramShot) > 0.0F)
                    nextDMGLevels(3, 2, "Engine2_D0", paramShot.initiator);
                if((paramString.endsWith("k1") || paramString.endsWith("k2")) && (double)World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingROut") > 1 && getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), paramShot) > 0.0F)
                    nextDMGLevels(1, 2, "Keel1_D0", paramShot.initiator);
                if((paramString.endsWith("sr1") || paramString.endsWith("sr2")) && (double)World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingROut") > 1 && getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), paramShot) > 0.0F)
                    nextDMGLevels(1, 2, "StabR_D0", paramShot.initiator);
                if((paramString.endsWith("sl1") || paramString.endsWith("sl2")) && (double)World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingROut") > 1 && getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), paramShot) > 0.0F)
                    nextDMGLevels(1, 2, "StabL_D0", paramShot.initiator);
            }
            if(paramString.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.01F && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0].haveBullets())
            {
                this.FM.AS.hitTank(paramShot.initiator, 0, 100);
                this.FM.AS.hitTank(paramShot.initiator, 1, 100);
                this.FM.AS.hitTank(paramShot.initiator, 2, 100);
                this.FM.AS.hitTank(paramShot.initiator, 3, 100);
                msgCollision(this, "CF_D0", "CF_D0");
            }
            if(paramString.startsWith("xxeng"))
            {
                int i = 0;
                if(paramString.startsWith("xxeng2"))
                    i = 1;
                if(paramString.endsWith("prop"))
                {
                    int j = i;
                    if(getEnergyPastArmor(2.0F, paramShot) > 0.0F && World.Rnd().nextFloat() < 0.35F)
                        this.FM.AS.setEngineSpecificDamage(paramShot.initiator, j, 3);
                }
                if(paramString.endsWith("case"))
                {
                    if(getEnergyPastArmor(0.2F, paramShot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < paramShot.power / 190000F)
                            this.FM.AS.setEngineStuck(paramShot.initiator, i);
                        if(World.Rnd().nextFloat() < paramShot.power / 48000F)
                            this.FM.AS.hitEngine(paramShot.initiator, i, 2);
                    }
                } else
                if(paramString.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(1.6F, paramShot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[i].getCylindersRatio() * 1.4F)
                    {
                        this.FM.EI.engines[i].setCyliderKnockOut(paramShot.initiator, World.Rnd().nextInt(1, (int)(paramShot.power / 4000F)));
                        if(this.FM.AS.astateEngineStates[i] < 1)
                        {
                            this.FM.AS.hitEngine(paramShot.initiator, i, 1);
                            this.FM.AS.doSetEngineState(paramShot.initiator, i, 1);
                        }
                        if(World.Rnd().nextFloat() < paramShot.power / 900000F)
                            this.FM.AS.hitEngine(paramShot.initiator, i, 3);
                        getEnergyPastArmor(25F, paramShot);
                    }
                } else
                if(paramString.endsWith("supc") && getEnergyPastArmor(0.05F, paramShot) > 0.0F && World.Rnd().nextFloat() < 0.89F)
                    this.FM.AS.setEngineSpecificDamage(paramShot.initiator, i, 0);
                if(getEnergyPastArmor(0.42F, paramShot) > 0.0F && (paramString.endsWith("oil1") || paramString.endsWith("oil2") || paramString.endsWith("oil3")))
                    this.FM.AS.hitOil(paramShot.initiator, i);
            }
            if(paramString.startsWith("xxtank"))
            {
                int i = paramString.charAt(6) - 49;
                if(i < 4 && getEnergyPastArmor(0.2F, paramShot) > 0.0F)
                    if(paramShot.power < 14100F)
                    {
                        if(this.FM.AS.astateTankStates[i] < 1)
                            this.FM.AS.hitTank(paramShot.initiator, i, 1);
                        if(this.FM.AS.astateTankStates[i] < 4 && World.Rnd().nextFloat() < 0.12F)
                            this.FM.AS.hitTank(paramShot.initiator, i, 1);
                        if(paramShot.powerType == 3 && this.FM.AS.astateTankStates[i] > 0 && World.Rnd().nextFloat() < 0.04F)
                            this.FM.AS.hitTank(paramShot.initiator, i, 10);
                    } else
                    {
                        this.FM.AS.hitTank(paramShot.initiator, i, World.Rnd().nextInt(0, (int)(paramShot.power / 35000F)));
                    }
            }
            if(paramString.startsWith("xxlock"))
            {
                if(paramString.startsWith("xxlockr") && (paramString.startsWith("xxlockr1") || paramString.startsWith("xxlockr2")) && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), paramShot) > 0.0F)
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), paramShot.initiator);
                if(paramString.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), paramShot) > 0.0F)
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), paramShot.initiator);
                if(paramString.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), paramShot) > 0.0F)
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), paramShot.initiator);
                if(paramString.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), paramShot) > 0.0F)
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), paramShot.initiator);
                if(paramString.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), paramShot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), paramShot.initiator);
                }
            }
        }
        if(paramString.startsWith("xxmgun"))
            if(paramString.endsWith("1"))
            {
                if(getEnergyPastArmor(5F, paramShot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    this.FM.AS.setJamBullets(10, 0);
                    getEnergyPastArmor(11.98F, paramShot);
                }
            } else
            if(paramString.endsWith("2"))
            {
                if(getEnergyPastArmor(4.85F, paramShot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    this.FM.AS.setJamBullets(11, 2);
                    getEnergyPastArmor(11.98F, paramShot);
                }
            } else
            if(paramString.endsWith("3"))
            {
                if(getEnergyPastArmor(4.85F, paramShot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    this.FM.AS.setJamBullets(12, 1);
                    getEnergyPastArmor(11.98F, paramShot);
                }
            } else
            if(paramString.endsWith("4"))
            {
                if(getEnergyPastArmor(4.85F, paramShot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    this.FM.AS.setJamBullets(13, 4);
                    getEnergyPastArmor(11.98F, paramShot);
                }
            } else
            if(paramString.endsWith("5") && getEnergyPastArmor(4.85F, paramShot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
            {
                this.FM.AS.setJamBullets(14, 3);
                getEnergyPastArmor(11.98F, paramShot);
            }
        if(paramString.startsWith("xcf"))
        {
            setControlDamage(paramShot, 0);
            setControlDamage(paramShot, 1);
            setControlDamage(paramShot, 2);
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", paramShot);
            getEnergyPastArmor(4F, paramShot);
        } else
        if(paramString.startsWith("xnose"))
        {
            if(chunkDamageVisible("Nose") < 3)
                hitChunk("Nose", paramShot);
            if(paramShot.power > 200000F)
            {
                this.FM.AS.hitPilot(paramShot.initiator, 0, World.Rnd().nextInt(3, 192));
                this.FM.AS.hitPilot(paramShot.initiator, 1, World.Rnd().nextInt(3, 192));
                this.FM.AS.hitPilot(paramShot.initiator, 2, World.Rnd().nextInt(3, 192));
                this.FM.AS.hitPilot(paramShot.initiator, 3, World.Rnd().nextInt(3, 192));
            }
        } else
        if(paramString.startsWith("xtail"))
        {
            setControlDamage(paramShot, 1);
            setControlDamage(paramShot, 2);
            if(chunkDamageVisible("Tail1") < 2)
                hitChunk("Tail1", paramShot);
            if(World.Rnd().nextFloat() < 0.1F)
                this.FM.AS.setCockpitState(paramShot.initiator, this.FM.AS.astateCockpitState | 0x10);
            if(World.Rnd().nextFloat() < 0.1F)
                this.FM.AS.setCockpitState(paramShot.initiator, this.FM.AS.astateCockpitState | 0x20);
        } else
        if(paramString.startsWith("xkeel1"))
        {
            setControlDamage(paramShot, 2);
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", paramShot);
        } else
        if(paramString.startsWith("xkeel2"))
        {
            setControlDamage(paramShot, 2);
            if(chunkDamageVisible("Keel2") < 2)
                hitChunk("Keel2", paramShot);
        } else
        if(paramString.startsWith("xrudder1"))
        {
            setControlDamage(paramShot, 2);
            hitChunk("Rudder1", paramShot);
        } else
        if(paramString.startsWith("xrudder2"))
        {
            setControlDamage(paramShot, 2);
            hitChunk("Rudder2", paramShot);
        } else
        if(paramString.startsWith("xstabl"))
            hitChunk("StabL", paramShot);
        else
        if(paramString.startsWith("xstabr"))
            hitChunk("StabR", paramShot);
        else
        if(paramString.startsWith("xvatorl"))
            hitChunk("VatorL", paramShot);
        else
        if(paramString.startsWith("xvatorr"))
            hitChunk("VatorR", paramShot);
        else
        if(paramString.startsWith("xwinglin"))
        {
            setControlDamage(paramShot, 0);
            if(chunkDamageVisible("WingLIn") < 2)
                hitChunk("WingLIn", paramShot);
        } else
        if(paramString.startsWith("xwingrin"))
        {
            setControlDamage(paramShot, 0);
            if(chunkDamageVisible("WingRIn") < 2)
                hitChunk("WingRIn", paramShot);
        } else
        if(paramString.startsWith("xwinglmid"))
        {
            setControlDamage(paramShot, 0);
            if(chunkDamageVisible("WingLMid") < 2)
                hitChunk("WingLMid", paramShot);
        } else
        if(paramString.startsWith("xwingrmid"))
        {
            setControlDamage(paramShot, 0);
            if(chunkDamageVisible("WingRMid") < 2)
                hitChunk("WingRMid", paramShot);
        } else
        if(paramString.startsWith("xwinglout"))
        {
            if(chunkDamageVisible("WingLOut") < 2)
                hitChunk("WingLOut", paramShot);
        } else
        if(paramString.startsWith("xwingrout"))
        {
            if(chunkDamageVisible("WingROut") < 2)
                hitChunk("WingROut", paramShot);
        } else
        if(paramString.startsWith("xaronel"))
            hitChunk("AroneL", paramShot);
        else
        if(paramString.startsWith("xaroner"))
            hitChunk("AroneR", paramShot);
        else
        if(paramString.startsWith("xflap01"))
            hitChunk("Flap01", paramShot);
        else
        if(paramString.startsWith("xflap02"))
            hitChunk("Flap02", paramShot);
        else
        if(paramString.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", paramShot);
        } else
        if(paramString.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", paramShot);
        } else
        if(paramString.startsWith("xgear"))
        {
            if(World.Rnd().nextFloat() < 0.1F && this.FM.Gears.isHydroOperable())
            {
                this.FM.Gears.setHydroOperable(false);
                gearDamageFX(paramString);
            }
        } else
        if(paramString.startsWith("xturret"))
        {
            if(paramString.startsWith("xturret1"))
                this.FM.AS.setJamBullets(10, 0);
            if(paramString.startsWith("xturret2"))
                this.FM.AS.setJamBullets(11, 0);
            if(paramString.startsWith("xturret3"))
                this.FM.AS.setJamBullets(12, 0);
            if(paramString.startsWith("xturret4"))
                this.FM.AS.setJamBullets(13, 0);
            if(paramString.startsWith("xturret5"))
                this.FM.AS.setJamBullets(14, 0);
        } else
        if(paramString.startsWith("xpilot") || paramString.startsWith("xhead"))
        {
            int i = 0;
            int j;
            if(paramString.endsWith("a"))
            {
                i = 1;
                j = paramString.charAt(6) - 49;
            } else
            if(paramString.endsWith("b"))
            {
                i = 2;
                j = paramString.charAt(6) - 49;
            } else
            {
                j = paramString.charAt(5) - 49;
            }
            hitFlesh(j, paramShot, i);
        }
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 0.0F, 75F * f);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f * kl, 0.1F, 1.0F, 0.0F, -107F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f * kr, 0.1F, 1.0F, 0.0F, -107F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f * kl, 0.1F, 1.0F, 0.0F, -19F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f * kr, 0.1F, 1.0F, 0.0F, -19F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f * kl, 0.1F, 1.0F, 0.0F, 60F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f * kr, 0.1F, 1.0F, 0.0F, 60F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f * kr, 0.01F, 0.2F, 0.0F, -65.5F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f * kl, 0.01F, 0.2F, 0.0F, -65.5F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f * kr, 0.01F, 0.2F, 0.0F, 65.5F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f * kl, 0.01F, 0.2F, 0.0F, 65.5F), 0.0F);
        hiermesh.chunkSetAngles("GearC2M_D0", 0.0F, 0.0F, 15F * f * kc);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
        if(this.FM.CT.getGear() >= 0.9985F)
        {
            kl = 1.0F;
            kr = 1.0F;
            kc = 1.0F;
        }
    }

    private void gearDamageFX(String s)
    {
        if(s.startsWith("xgearl") || s.startsWith("GearL"))
        {
            if(this.FM.isPlayers())
                HUD.log("Left Gear:  Hydraulic system Failed");
            kl = World.Rnd().nextFloat();
            kr = World.Rnd().nextFloat() * kl;
            kc = 0.1F;
            cutGearCovers("L");
        } else
        if(s.startsWith("xgearr") || s.startsWith("GearR"))
        {
            if(this.FM.isPlayers())
                HUD.log("Right Gear:  Hydraulic system Failed");
            kr = World.Rnd().nextFloat();
            kl = World.Rnd().nextFloat() * kr;
            kc = 0.1F;
            cutGearCovers("R");
        } else
        {
            if(this.FM.isPlayers())
                HUD.log("Center Gear:  Hydraulic system Failed");
            kc = World.Rnd().nextFloat();
            kl = World.Rnd().nextFloat() * kc;
            kr = World.Rnd().nextFloat() * kc;
            cutGearCovers("C");
        }
        this.FM.CT.GearControl = 1.0F;
        this.FM.Gears.setHydroOperable(false);
    }

    private void cutGearCovers(String s)
    {
        Vector3d vector3d = new Vector3d();
        if(World.Rnd().nextFloat() < 0.3F)
        {
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Gear" + s + 5 + "_D0"));
            wreckage.collide(true);
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
            hierMesh().chunkVisible("Gear" + s + 5 + "_D0", false);
            Wreckage wreckage1 = new Wreckage(this, hierMesh().chunkFind("Gear" + s + 6 + "_D0"));
            wreckage1.collide(true);
            vector3d.set(this.FM.Vwld);
            wreckage1.setSpeed(vector3d);
            hierMesh().chunkVisible("Gear" + s + 6 + "_D0", false);
        } else
        if(World.Rnd().nextFloat() < 0.3F)
        {
            int i = World.Rnd().nextInt(2) + 5;
            Wreckage wreckage2 = new Wreckage(this, hierMesh().chunkFind("Gear" + s + i + "_D0"));
            wreckage2.collide(true);
            vector3d.set(this.FM.Vwld);
            wreckage2.setSpeed(vector3d);
            hierMesh().chunkVisible("Gear" + s + i + "_D0", false);
        }
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

    public void typeBomberUpdate(float paramFloat)
    {
        if((double)Math.abs(this.FM.Or.getKren()) > 4.5D)
        {
            fSightCurReadyness -= 0.0666666F * paramFloat;
            if(fSightCurReadyness < 0.0F)
                fSightCurReadyness = 0.0F;
        }
        if(fSightCurReadyness < 1.0F)
            fSightCurReadyness += 0.0333333F * paramFloat;
        else
        if(bSightAutomation)
        {
            fSightCurDistance -= (fSightCurSpeed / 3.6F) * paramFloat;
            if(fSightCurDistance < 0.0F)
            {
                fSightCurDistance = 0.0F;
                typeBomberToggleAutomation();
            }
            fSightCurForwardAngle = (float)Math.toDegrees(Math.atan(fSightCurDistance / fSightCurAltitude));
            if((double)fSightCurDistance < (double)(fSightCurSpeed / 3.6F) * Math.sqrt(fSightCurAltitude * 0.203874F))
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

    public void typeBomberReplicateToNet(NetMsgGuaranted paramNetMsgGuaranted)
        throws IOException
    {
        paramNetMsgGuaranted.writeByte((bSightAutomation ? 1 : 0) | (bSightBombDump ? 2 : 0));
        paramNetMsgGuaranted.writeFloat(fSightCurDistance);
        paramNetMsgGuaranted.writeByte((int)fSightCurForwardAngle);
        paramNetMsgGuaranted.writeByte((int)((fSightCurSideslip + 3F) * 33.33333F));
        paramNetMsgGuaranted.writeFloat(fSightCurAltitude);
        paramNetMsgGuaranted.writeByte((int)(fSightCurSpeed / 2.5F));
        paramNetMsgGuaranted.writeByte((int)(fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput paramNetMsgInput)
        throws IOException
    {
        int i = paramNetMsgInput.readUnsignedByte();
        bSightAutomation = (i & 1) != 0;
        bSightBombDump = (i & 2) != 0;
        fSightCurDistance = paramNetMsgInput.readFloat();
        fSightCurForwardAngle = paramNetMsgInput.readUnsignedByte();
        fSightCurSideslip = -3F + (float)paramNetMsgInput.readUnsignedByte() / 33.33333F;
        fSightCurAltitude = paramNetMsgInput.readFloat();
        fSightCurSpeed = (float)paramNetMsgInput.readUnsignedByte() * 2.5F;
        fSightCurReadyness = (float)paramNetMsgInput.readUnsignedByte() / 200F;
    }

    public boolean bSightAutomation;
    private boolean bSightBombDump;
    public float fSightCurDistance;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurForwardAngle;
    public float fSightSetForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurReadyness;
    public boolean bPitUnfocused;
    private float wheel1;
    private float wheel2;
    private static float kl = 1.0F;
    private static float kr = 1.0F;
    private static float kc = 1.0F;

    static 
    {
        Class localClass = Do17.class;
        Property.set(localClass, "originCountry", PaintScheme.countryGermany);
    }
}
