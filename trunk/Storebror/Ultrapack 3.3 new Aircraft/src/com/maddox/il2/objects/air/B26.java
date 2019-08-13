package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class B26 extends Scheme2a
{

    public B26()
    {
        kangle0 = 0.0F;
        kangle1 = 0.0F;
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 109F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, AircraftLH.floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 10F), anglesc6), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.1F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, AircraftLH.floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 10F), anglesc7), 0.0F);
        hiermesh.chunkSetAngles("L", 0.0F, Aircraft.cvt(f, 0.0F, 0.1F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, -89.5F * f);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -50F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, 57F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, -89.5F * f);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -57F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, 50F), 0.0F);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.33F, 0.0F, 0.33F);
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.33F, 0.0F, 0.33F);
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.154F, 0.0F, 0.154F);
        hierMesh().chunkSetLocate("GearC3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("GearC31_D0", -20F * f, 0.0F, 0.0F);
        super.moveRudder(f);
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

        case 4:
            hierMesh().chunkVisible("Pilot5_D0", false);
            hierMesh().chunkVisible("HMask5_D0", false);
            hierMesh().chunkVisible("Pilot5_D1", true);
            break;

        case 5:
            hierMesh().chunkVisible("Pilot6_D0", false);
            hierMesh().chunkVisible("HMask6_D0", false);
            hierMesh().chunkVisible("Pilot6_D1", true);
            break;
        }
    }

    protected void hitBone(String string, Shot shot, Point3d point3d)
    {
        if(string.startsWith("xx"))
        {
            if(string.startsWith("xxarmor"))
            {
                if(string.endsWith("p1") || string.endsWith("p2"))
                    if(Math.abs(Aircraft.v1.x) > 0.5D)
                        getEnergyPastArmor(7.94D / Math.abs(Aircraft.v1.x), shot);
                    else
                        getEnergyPastArmor(9.53D / (1.0D - Math.abs(Aircraft.v1.x)), shot);
                if(string.endsWith("p3"))
                    getEnergyPastArmor(7.94D / (Math.abs(Aircraft.v1.z) + 9.9999999747524271E-007D), shot);
                if(string.endsWith("p4"))
                    getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                if(string.endsWith("p5") || string.endsWith("p6"))
                    getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.y) + 9.9999999747524271E-007D), shot);
                if(string.endsWith("p7"))
                    getEnergyPastArmor(0.5D / (Math.abs(Aircraft.v1.z) + 9.9999999747524271E-007D), shot);
                if(string.endsWith("p8"))
                    getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                if(string.endsWith("a1"))
                    getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.y) + 9.9999999747524271E-007D), shot);
                if(string.endsWith("a2"))
                    getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.y) + 9.9999999747524271E-007D), shot);
                if(string.endsWith("a3"))
                    getEnergyPastArmor(6.35D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                if(string.endsWith("a4") || string.endsWith("a5"))
                    getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                if(string.endsWith("a6") || string.endsWith("a7"))
                    getEnergyPastArmor(6.35D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                if(string.endsWith("r1"))
                    getEnergyPastArmor(3.17D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                if(string.endsWith("r2") || string.endsWith("r3"))
                    getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                if(string.endsWith("c1") || string.endsWith("c2"))
                    getEnergyPastArmor(8.73D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
            } else
            if(string.startsWith("xxcontrols"))
            {
                int i = string.charAt(10) - 48;
                switch(i)
                {
                case 1:
                    if(getEnergyPastArmor(3F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.12F)
                        {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            debuggunnery("Evelator Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.12F)
                        {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            debuggunnery("Rudder Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.12F)
                        {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            debuggunnery("Aileron Controls Out..");
                        }
                    }
                    break;

                case 2:
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            debuggunnery("*** Engine1 Throttle Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            debuggunnery("*** Engine1 Prop Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.25F)
                        {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            debuggunnery("Ailerons Controls Out..");
                        }
                    }
                    break;

                case 3:
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            debuggunnery("*** Engine2 Throttle Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            debuggunnery("*** Engine2 Prop Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.25F)
                        {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            debuggunnery("Ailerons Controls Out..");
                        }
                    }
                    break;

                case 4:
                case 5:
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.12F)
                    {
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        debuggunnery("Evelator Controls Out..");
                    }
                    break;

                case 6:
                case 7:
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.12F)
                    {
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        debuggunnery("Rudder Controls Out..");
                    }
                    break;
                }
            } else
            if(string.startsWith("xxspar"))
            {
                if(string.startsWith("xxspart") && World.Rnd().nextFloat() < 0.1F && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(19.9F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F)
                {
                    debuggunnery("*** Tail1 Spars Broken in Half..");
                    msgCollision(this, "Tail1_D0", "Tail1_D0");
                }
                if((string.endsWith("li1") || string.endsWith("li2")) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if((string.endsWith("ri1") || string.endsWith("ri2")) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if((string.endsWith("lm1") || string.endsWith("lm2")) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if((string.endsWith("rm1") || string.endsWith("rm2")) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if((string.endsWith("lo1") || string.endsWith("lo2")) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if((string.endsWith("ro1") || string.endsWith("ro2")) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(string.endsWith("e1") && getEnergyPastArmor(28F, shot) > 0.0F)
                {
                    debuggunnery("*** Engine1 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if(string.endsWith("e2") && getEnergyPastArmor(28F, shot) > 0.0F)
                {
                    debuggunnery("*** Engine2 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
                if(string.startsWith("xxspark1") && chunkDamageVisible("Keel1") > 1 && getEnergyPastArmor(9.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** Keel1 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if(string.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(string.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
            } else
            if(string.startsWith("xxbomb"))
            {
                if(World.Rnd().nextFloat() < 0.01F && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0].haveBullets())
                {
                    debuggunnery("*** Bomb Payload Detonates..");
                    this.FM.AS.hitTank(shot.initiator, 0, 100);
                    this.FM.AS.hitTank(shot.initiator, 1, 100);
                    this.FM.AS.hitTank(shot.initiator, 2, 100);
                    this.FM.AS.hitTank(shot.initiator, 3, 100);
                    nextDMGLevels(3, 2, "CF_D0", shot.initiator);
                }
            } else
            if(string.startsWith("xxeng"))
            {
                int i = 0;
                if(string.startsWith("xxeng2"))
                    i = 1;
                debuggunnery("Engine Module[" + i + "]: Hit..");
                if(string.endsWith("case"))
                {
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 280000F)
                        {
                            debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                            this.FM.AS.setEngineStuck(shot.initiator, i);
                        }
                        if(World.Rnd().nextFloat() < shot.power / 100000F)
                        {
                            debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                            this.FM.AS.hitEngine(shot.initiator, i, 2);
                        }
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if(string.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[i].getCylindersRatio() * 0.66F)
                    {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 32200F)));
                        debuggunnery("Engine Module: Cylinders Hit, " + this.FM.EI.engines[i].getCylindersOperable() + "/" + this.FM.EI.engines[i].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 1000000F)
                        {
                            this.FM.AS.hitEngine(shot.initiator, i, 2);
                            debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(string.endsWith("eqpt") || string.endsWith("cyls") && World.Rnd().nextFloat() < 0.01F)
                {
                    if(getEnergyPastArmor(0.5F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 4);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 0);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 6);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 1);
                    }
                    getEnergyPastArmor(2.0F, shot);
                }
                if(string.endsWith("gear"))
                {
                    if(getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        debuggunnery("Engine Module: Bullet Jams Reductor Gear..");
                        this.FM.EI.engines[i].setEngineStuck(shot.initiator);
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 12.44565F), shot);
                }
                if(string.endsWith("mag1") || string.endsWith("mag2"))
                {
                    debuggunnery("Engine Module: Magneto " + i + " Destroyed..");
                    this.FM.EI.engines[i].setMagnetoKnockOut(shot.initiator, i);
                }
                if(string.endsWith("oil1"))
                {
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.25F, shot) > 0.0F)
                        debuggunnery("Engine Module: Oil Radiator Hit..");
                    this.FM.AS.hitOil(shot.initiator, i);
                }
                if(string.endsWith("prop") && getEnergyPastArmor(0.42F, shot) > 0.0F)
                    this.FM.EI.engines[i].setKillPropAngleDevice(shot.initiator);
                if(string.endsWith("supc") && getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 12F), shot) > 0.0F)
                {
                    debuggunnery("Engine Module: Turbine Disabled..");
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 0);
                }
            } else
            if(string.startsWith("xxtank"))
            {
                int i = string.charAt(6) - 48;
                switch(i)
                {
                case 1:
                case 2:
                    doHitMeATank(shot, 1);
                    break;

                case 3:
                    doHitMeATank(shot, 0);
                    break;

                case 4:
                case 5:
                    doHitMeATank(shot, 2);
                    break;

                case 6:
                    doHitMeATank(shot, 3);
                    break;

                case 7:
                    doHitMeATank(shot, 0);
                    doHitMeATank(shot, 1);
                    doHitMeATank(shot, 2);
                    doHitMeATank(shot, 3);
                    break;
                }
            } else
            if(string.startsWith("xxpnm"))
            {
                if(getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 1.22F), shot) > 0.0F)
                {
                    debuggunnery("Pneumo System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 1);
                }
            } else
            if(string.startsWith("xxmgun02"))
            {
                this.FM.AS.setJamBullets(0, 0);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(string.startsWith("xxmgun07"))
            {
                this.FM.AS.setJamBullets(0, 0);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(string.startsWith("xxmgun08"))
            {
                this.FM.AS.setJamBullets(0, 1);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(string.startsWith("xxmgun09"))
            {
                this.FM.AS.setJamBullets(0, 2);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(string.startsWith("xxmgun10"))
            {
                this.FM.AS.setJamBullets(0, 3);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(string.startsWith("xxmgun13"))
            {
                this.FM.AS.setJamBullets(0, 4);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(string.startsWith("xxmgun14"))
            {
                this.FM.AS.setJamBullets(0, 5);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(string.startsWith("xxmgun15"))
            {
                this.FM.AS.setJamBullets(0, 6);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(string.startsWith("xxmgun16"))
            {
                this.FM.AS.setJamBullets(0, 7);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(string.startsWith("xxcannon"))
            {
                this.FM.AS.setJamBullets(1, 0);
                getEnergyPastArmor(44.7F, shot);
            } else
            if(string.startsWith("xxlock"))
            {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if(string.startsWith("xxlockr1") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(string.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(string.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(string.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(string.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
            } else
            if(string.startsWith("xxammo0"))
            {
                int i = string.charAt(7) - 48;
                int i_0_;
                int i_1_;
                switch(i)
                {
                default:
                    i_0_ = 0;
                    i_1_ = 0;
                    break;

                case 2:
                    i_0_ = 10;
                    i_1_ = 0;
                    break;

                case 3:
                    i_0_ = 11;
                    i_1_ = 0;
                    break;

                case 4:
                    i_0_ = 11;
                    i_1_ = 1;
                    break;

                case 5:
                    i_0_ = 12;
                    i_1_ = 0;
                    break;

                case 6:
                    i_0_ = 12;
                    i_1_ = 1;
                    break;

                case 7:
                    i_0_ = 0;
                    i_1_ = 0;
                    break;

                case 8:
                    i_0_ = 0;
                    i_1_ = 1;
                    break;

                case 9:
                    i_0_ = 0;
                    i_1_ = 2;
                    break;

                case 10:
                    i_0_ = 0;
                    i_1_ = 3;
                    break;
                }
                if(World.Rnd().nextFloat() < 0.125F)
                    this.FM.AS.setJamBullets(i_0_, i_1_);
                getEnergyPastArmor(4.7F, shot);
            }
        } else
        if(string.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(World.Rnd().nextFloat() < 0.0575F)
                if(point3d.y > 0.0D)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                else
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
            if(point3d.x > 1.726D)
            {
                if(point3d.z > 0.444D)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                if(point3d.z > -0.281D && point3d.z < 0.444D)
                    if(point3d.y > 0.0D)
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                    else
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                if(point3d.x > 2.774D && point3d.x < 3.718D && point3d.z > 0.425D)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                if(World.Rnd().nextFloat() < 0.12F)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
        } else
        if(string.startsWith("xnose"))
        {
            if(chunkDamageVisible("Nose") < 2)
                hitChunk("Nose", shot);
        } else
        if(string.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(string.startsWith("xkeel1"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(string.startsWith("xrudder1"))
        {
            if(chunkDamageVisible("Rudder1") < 2)
                hitChunk("Rudder1", shot);
        } else
        if(string.startsWith("xstabl"))
        {
            if(chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
        } else
        if(string.startsWith("xstabr"))
        {
            if(chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
        } else
        if(string.startsWith("xvatorl"))
        {
            if(chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
        } else
        if(string.startsWith("xvatorr"))
        {
            if(chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
        } else
        if(string.startsWith("xwinglin"))
        {
            if(chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
        } else
        if(string.startsWith("xwingrin"))
        {
            if(chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
        } else
        if(string.startsWith("xwinglmid"))
        {
            if(chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
        } else
        if(string.startsWith("xwingrmid"))
        {
            if(chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
        } else
        if(string.startsWith("xwinglout"))
        {
            if(chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
        } else
        if(string.startsWith("xwingrout"))
        {
            if(chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(string.startsWith("xaronel"))
        {
            if(chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL", shot);
        } else
        if(string.startsWith("xaroner"))
        {
            if(chunkDamageVisible("AroneR") < 1)
                hitChunk("AroneR", shot);
        } else
        if(string.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(string.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
        } else
        if(string.startsWith("xgear"))
        {
            if(World.Rnd().nextFloat() < 0.1F)
            {
                debuggunnery("*** Gear Hydro Failed..");
                this.FM.Gears.setHydroOperable(false);
            }
        } else
        if(string.startsWith("xturret"))
        {
            if(string.startsWith("xturret1"))
                this.FM.AS.setJamBullets(10, 0);
            if(string.endsWith("2b1"))
                this.FM.AS.setJamBullets(11, 0);
            if(string.endsWith("2b2"))
                this.FM.AS.setJamBullets(11, 1);
            if(string.endsWith("3b1"))
                this.FM.AS.setJamBullets(12, 0);
            if(string.endsWith("3b2"))
                this.FM.AS.setJamBullets(12, 1);
            if(string.endsWith("4a"))
                this.FM.AS.setJamBullets(13, 1);
            if(string.endsWith("5a"))
                this.FM.AS.setJamBullets(14, 1);
        } else
        if(string.startsWith("xpilot") || string.startsWith("xhead"))
        {
            int i = 0;
            int i_2_;
            if(string.endsWith("a"))
            {
                i = 1;
                i_2_ = string.charAt(6) - 49;
            } else
            if(string.endsWith("b"))
            {
                i = 2;
                i_2_ = string.charAt(6) - 49;
            } else
            {
                i_2_ = string.charAt(5) - 49;
            }
            hitFlesh(i_2_, shot, i);
        }
    }

    private final void doHitMeATank(Shot shot, int i)
    {
        if(getEnergyPastArmor(0.2F, shot) > 0.0F)
            if(shot.power < 14100F)
            {
                if(this.FM.AS.astateTankStates[i] == 0)
                {
                    this.FM.AS.hitTank(shot.initiator, i, 1);
                    this.FM.AS.doSetTankState(shot.initiator, i, 1);
                }
                if(shot.powerType == 3 && this.FM.AS.astateTankStates[i] > 0 && World.Rnd().nextFloat() < 0.25F)
                    this.FM.AS.hitTank(shot.initiator, i, 2);
            } else
            {
                this.FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int)(shot.power / 56000F)));
            }
    }

    public void rareAction(float f, boolean bool)
    {
        super.rareAction(f, bool);
        if(bool)
        {
            if(this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.0023F)
                this.FM.AS.hitTank(this, 0, 1);
            if(this.FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.0023F)
                this.FM.AS.hitTank(this, 1, 1);
            if(this.FM.AS.astateEngineStates[2] > 3 && World.Rnd().nextFloat() < 0.0023F)
                this.FM.AS.hitTank(this, 2, 1);
            if(this.FM.AS.astateEngineStates[3] > 3 && World.Rnd().nextFloat() < 0.0023F)
                this.FM.AS.hitTank(this, 3, 1);
        }
        for(int i = 1; i < 6; i++)
            if(this.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -110F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 110F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 110F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -110F * f, 0.0F);
    }

    protected boolean cutFM(int i, int i_3_, Actor actor)
    {
        switch(i)
        {
        case 13:
            killPilot(this, 2);
            return false;
        }
        return super.cutFM(i, i_3_, actor);
    }

    public void update(float f)
    {
        kangle0 = 0.95F * kangle0 + 0.05F * this.FM.EI.engines[0].getControlRadiator();
        if(kangle0 > 1.0F)
            kangle0 = 1.0F;
        for(int i = 1; i < 14; i++)
            hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -20F * kangle0, 0.0F);

        kangle1 = 0.95F * kangle1 + 0.05F * this.FM.EI.engines[1].getControlRadiator();
        if(kangle1 > 1.0F)
            kangle1 = 1.0F;
        for(int i = 1; i < 14; i++)
            hierMesh().chunkSetAngles("Waterr" + i + "_D0", 0.0F, -20F * kangle1, 0.0F);

        super.update(f);
    }

    private static final float anglesc7[] = {
        0.0F, -6.5F, -13.5F, -24.5F, -32.5F, -39.75F, -47F, -54.75F, -62.5F, -69.75F, 
        -83.5F
    };
    private static final float anglesc6[] = {
        0.0F, -20.5F, -39.5F, -57.25F, -70F, -79.75F, -87.5F, -92.75F, -95F, -94F, 
        -85F
    };
    private float kangle0;
    private float kangle1;

    static 
    {
        Class var_class = B26.class;
        Property.set(var_class, "originCountry", PaintScheme.countryUSA);
    }
}
