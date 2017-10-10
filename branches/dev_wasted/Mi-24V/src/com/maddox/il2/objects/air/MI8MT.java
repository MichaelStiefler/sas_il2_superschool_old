package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Finger;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.util.HashMapInt;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.BombGunVDVAKS;


// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, Aircraft, PaintSchemeFMPar05, TypeScout, 
//            TypeTransport, TypeStormovik, NetAircraft

public class MI8MT extends Scheme2a
    implements TypeTransport, TypeStormovik, TypeBomber
{

    public MI8MT()
    {
   	 forceTrim_x = 0.0D;
     forceTrim_y = 0.0D;
     forceTrim_z = 0.0D;
     getTrim = false;

     curAngleRotor = 0.0F;
     lastTimeFan = Time.current();
     oMainRotor = new Orient();
     vThrust = new Vector3f();
     iMainTorque = 0.0F;
     MainRotorStatus = 1.0F;
     TailRotorStatus = 1.0F;
        suka = new Loc();
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
        rotorrpm = 0;
        obsLookTime = 0;
        obsLookAzimuth = 0.0F;
        obsLookElevation = 0.0F;
        obsAzimuth = 0.0F;
        obsElevation = 0.0F;
        obsAzimuthOld = 0.0F;
        obsElevationOld = 0.0F;
        obsMove = 0.0F;
        obsMoveTot = 0.0F;
        bObserverKilled = false;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(!bObserverKilled)
            if(obsLookTime == 0)
            {
                obsLookTime = 2 + World.Rnd().nextInt(1, 3);
                obsMoveTot = 1.0F + World.Rnd().nextFloat() * 1.5F;
                obsMove = 0.0F;
                obsAzimuthOld = obsAzimuth;
                obsElevationOld = obsElevation;
                if((double)World.Rnd().nextFloat() > 0.80000000000000004D)
                {
                    obsAzimuth = 0.0F;
                    obsElevation = 0.0F;
                } else
                {
                    obsAzimuth = World.Rnd().nextFloat() * 140F - 70F;
                    obsElevation = World.Rnd().nextFloat() * 50F - 20F;
                }
            } else
            {
                obsLookTime--;
            }
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -5 * f, 0.0F);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 5 * f);
    }

    protected void moveFlap(float f)
    {
    }

    protected void moveRudder(float f)
    {
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
    }
    
    public void moveBayDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1F, 0.0F, -0.8F);
        hierMesh().chunkSetLocate("Door1_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveFan(float f)
    {
        rotorrpm = Math.abs((int)((double)(FM.EI.engines[0].getw() * 0.025F) + FM.Vwld.length() / 30D));
        if(rotorrpm >= 1)
            rotorrpm = 1;
        if((FM.EI.engines[0].getw() > 100F) && (FM.EI.engines[1].getw() > 100F))
        {
            hierMesh().chunkVisible("Prop1_D0", false);
            hierMesh().chunkVisible("PropRot1_D0", true);
        }
        if((FM.EI.engines[0].getw() < 100F) && (FM.EI.engines[1].getw() < 100F))
        {
            hierMesh().chunkVisible("Prop1_D0", true);
            hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if(hierMesh().isChunkVisible("Prop1_D1"))
        {
            hierMesh().chunkVisible("Prop1_D0", false);
            hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if((FM.EI.engines[0].getw() > 100F) && (FM.EI.engines[1].getw() > 100F))
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", true);
        }
        if((FM.EI.engines[0].getw() < 100F) && (FM.EI.engines[1].getw() < 100F))
        {
            hierMesh().chunkVisible("Prop2_D0", true);
            hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if(hierMesh().isChunkVisible("Prop2_D1"))
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if(hierMesh().isChunkVisible("Tail1_CAP"))
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", false);
            hierMesh().chunkVisible("Prop2_D1", false);
        }
        dynamoOrient = bDynamoRotary ? (dynamoOrient - 100F) % 360F : (float)((double)dynamoOrient - (double)rotorrpm * 25D) % 360F;
        hierMesh().chunkSetAngles("Prop1_D0", -dynamoOrient, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Prop2_D0", 0.0F, 0.0F, dynamoOrient);
    }

    public void moveWheelSink()
    {
        hierMesh().chunkSetAngles("GearL2_D0", 0.0F, cvt(FM.Gears.gWheelSinking[0], 0.0F, 1.0F, 0.0F, 40F), 0.0F);
        hierMesh().chunkSetAngles("GearR2_D0", 0.0F, cvt(FM.Gears.gWheelSinking[1], 0.0F, 1.0F, 0.0F, 40F), 0.0F);
        hierMesh().chunkSetAngles("GearL22_D0", 0.0F, cvt(FM.Gears.gWheelSinking[0], 0.0F, 1.0F, 0.0F, 30F), 0.0F);
        hierMesh().chunkSetAngles("GearR22_D0", 0.0F, cvt(FM.Gears.gWheelSinking[1], 0.0F, 1.0F, 0.0F, 30F), 0.0F);
        resetYPRmodifier();
        xyz[2] = cvt(FM.Gears.gWheelSinking[2], 0.0F, 1.0F, 0.0F, 1.2F);
        hierMesh().chunkSetLocate("GearC2_D0", xyz, ypr);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1") || s.endsWith("p2"))
                    getEnergyPastArmor(16.65F / (1E-005F + (float)Math.abs(v1.x)), shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                if(s.endsWith("1"))
                {
                    if(getEnergyPastArmor(3.2F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                } else
                if(s.endsWith("2"))
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.33F)
                        {
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                            debugprintln(this, "*** Throttle Quadrant: Hit, Engine 1 Controls Disabled..");
                        }
                        if(World.Rnd().nextFloat() < 0.33F)
                        {
                            FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                            debugprintln(this, "*** Throttle Quadrant: Hit, Engine 2 Controls Disabled..");
                        }
                    }
                } else
                if(s.endsWith("3") || s.endsWith("4"))
                {
                    if(World.Rnd().nextFloat() < 0.12F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        debugprintln(this, "*** Evelator Controls Out..");
                    }
                } else
                if(World.Rnd().nextFloat() < 0.12F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 0);
                    debugprintln(this, "*** Arone Controls Out..");
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.5F / (float)Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F)
                {
                    debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if((s.endsWith("lo1") || s.endsWith("lo2")) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if((s.endsWith("ro1") || s.endsWith("ro2")) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxeng"))
            {
                int i = s.charAt(5) - 49;
                debugprintln(this, "*** Engine" + i + " Hit..");
                if(s.endsWith("case") && getEnergyPastArmor(0.1F, shot) > 0.0F)
                {
                    if(World.Rnd().nextFloat() < shot.power / 200000F)
                    {
                        FM.AS.setEngineStuck(shot.initiator, i);
                        debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                    }
                    if(World.Rnd().nextFloat() < shot.power / 50000F)
                    {
                        FM.AS.hitEngine(shot.initiator, i, 2);
                        debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                    }
                    if(World.Rnd().nextFloat() < shot.power / 28000F)
                    {
                        FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
                        debugprintln(this, "*** Engine Crank Case Hit - Cylinder Feed Out, " + FM.EI.engines[i].getCylindersOperable() + "/" + FM.EI.engines[i].getCylinders() + " Left..");
                    }
                    FM.EI.engines[i].setReadyness(shot.initiator, FM.EI.engines[i].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[i].getReadyness() + "..");
                }
                if(s.endsWith("cyl1") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[i].getCylindersRatio() * 1.75F)
                {
                    FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    debugprintln(this, "*** Engine Cylinders Hit, " + FM.EI.engines[i].getCylindersOperable() + "/" + FM.EI.engines[i].getCylinders() + " Left..");
                    if(FM.AS.astateEngineStates[i] < 1)
                        FM.AS.hitEngine(shot.initiator, i, 1);
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        FM.AS.hitEngine(shot.initiator, i, 3);
                        debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("supc") && getEnergyPastArmor(0.05F, shot) > 0.0F)
                    FM.EI.engines[i].setKillCompressor(shot.initiator);
                if(s.endsWith("prop") && getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    FM.EI.engines[i].setKillPropAngleDevice(shot.initiator);
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int j = s.charAt(6) - 48;
                switch(j)
                {
                case 1: // '\001'
                case 2: // '\002'
                    doHitMeATank(shot, 0);
                    break;

                case 3: // '\003'
                    doHitMeATank(shot, 1);
                    break;

                case 4: // '\004'
                case 5: // '\005'
                    doHitMeATank(shot, 2);
                    break;
                }
                return;
            }
            if(s.startsWith("xxw1") || s.startsWith("xxoil1"))
            {
                if(World.Rnd().nextFloat() < 0.12F && getEnergyPastArmor(2.25F, shot) > 0.0F)
                {
                    FM.AS.hitOil(shot.initiator, 0);
                    debugprintln(this, "*** Oil Radiator L Hit..");
                }
                return;
            }
            if(s.startsWith("xxw2") || s.startsWith("xxoil1"))
            {
                if(World.Rnd().nextFloat() < 0.12F && getEnergyPastArmor(2.25F, shot) > 0.0F)
                {
                    FM.AS.hitOil(shot.initiator, 1);
                    debugprintln(this, "*** Oil Radiator R Hit..");
                }
                return;
            }
            if(s.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxmgun0"))
            {
                int k = s.charAt(7) - 49;
                if(getEnergyPastArmor(0.75F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Machine Gun (" + k + ") Disabled..");
                    FM.AS.setJamBullets(0, k);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
            if(s.startsWith("xxcannon0"))
            {
                int l = s.charAt(9) - 49;
                if(getEnergyPastArmor(6.29F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Cannon (" + l + ") Disabled..");
                    FM.AS.setJamBullets(1, l);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            } else
            {
                return;
            }
        }
        if(s.startsWith("xcf") || s.startsWith("xcockpit"))
        {
            hitChunk("CF", shot);
            if(point3d.x > 0.0D)
            {
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
                if(point3d.z > 0.40000000000000002D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            }
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
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel")) {
            hitChunk("Keel1", shot);
        	TailRotorDamage();
        } else
        if(s.startsWith("xrudder"))
            hitChunk("Rudder1", shot);
        else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl"))
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr"))
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl"))
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr"))
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xwinglmid"))
            {
                if(chunkDamageVisible("WingLMid") < 3)
                    hitChunk("WingLMid", shot);
                if(World.Rnd().nextFloat() < shot.mass + 0.02F)
                    FM.AS.hitOil(shot.initiator, 0);
            }
            if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel"))
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner"))
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(s.endsWith("1"))
            {
                if(World.Rnd().nextFloat() < 0.05F)
                {
                    debuggunnery("Hydro System: Disabled..");
                    FM.AS.setInternalDamage(shot.initiator, 0);
                }
            } else
            if(World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
            {
                debuggunnery("Undercarriage: Stuck..");
                FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int i1;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                i1 = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                i1 = s.charAt(6) - 49;
            } else
            {
                i1 = s.charAt(5) - 49;
            }
            hitFlesh(i1, shot, byte0);
        }
    }

    private final void doHitMeATank(Shot shot, int i)
    {
        if(getEnergyPastArmor(0.2F, shot) > 0.0F)
            if(shot.power < 14100F)
            {
                if(FM.AS.astateTankStates[i] == 0)
                {
                    FM.AS.hitTank(shot.initiator, i, 1);
                    FM.AS.doSetTankState(shot.initiator, i, 1);
                }
                if(FM.AS.astateTankStates[i] > 0 && (World.Rnd().nextFloat() < 0.02F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F))
                    FM.AS.hitTank(shot.initiator, i, 2);
            } else
            {
                FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int)(shot.power / 56000F)));
            }
    }



    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            // fall through

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Head2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            bObserverKilled = true;
            // fall through

        default:
            return;
        }
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
//        FM.CT.bHasBayDoors = true;
        if(thisWeaponsName.endsWith("mm"))
        {
            hierMesh().chunkVisible("Turret1BB_D0", true);
            hierMesh().chunkVisible("Leather_Window", true);
            
        }
//        if(thisWeaponsName.endsWith("mm"))
//        {
//            hierMesh().chunkVisible("Turret1A_D0", true);
//        }
        if(thisWeaponsName.startsWith("2x"))
        {
            hierMesh().chunkVisible("PylonL_D0", true);
            hierMesh().chunkVisible("PylonR_D0", true);
        }
        if(thisWeaponsName.startsWith("4x"))
        {
            hierMesh().chunkVisible("PylonL_D0", true);
            hierMesh().chunkVisible("PylonR_D0", true);
        } else
        {
            return;
        }
    }


    private void TailRotorDamage()
    {
    	if(!TailRotorDestroyed)
    	{
        hierMesh().chunkVisible("Prop2_D0", false);
        hierMesh().chunkVisible("PropRot2_D0", false);
        hierMesh().chunkVisible("Prop2_D1", true);
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Tail Rotor: Destroyed!");
        TailRotorDestroyed = true;
    	}
    }
    
    public void update(float f)
    {
    	if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !(super.FM instanceof Pilot))
        {
    		if(thisWeaponsName.startsWith("VDV Squad") || thisWeaponsName.startsWith("4xUB-32-57+VDV Squad+1x12.7mm") || thisWeaponsName.startsWith("VDV Recon Squad") || thisWeaponsName.startsWith("4xUB-32-57+VDV Recon Squad+1x12.7mm"))
        	{
    			Point3d point3d = new Point3d();
    			float f1 = (float)((double)FM.getAltitude() - World.land().HQ(point3d.x, point3d.y));
    			if(f1 > 15F && FM.getSpeedKMH() > 15F)
    			{
    				FM.CT.BayDoorControl = 0.0F;
    			}
        	}
        }
    	
        Pilot pilot = (Pilot)FM;
        if((pilot != null) && !Mission.isNet())
        {
            Actor actor = War.GetNearestEnemy(this, 1, 4000F);
            if(pilot != null && isAlive(actor) && !(actor instanceof BridgeSegment))
            {
                Point3d point3d = new Point3d();
                actor.pos.getAbs(point3d);
                if(pos.getAbsPoint().distance(point3d) < 3000D)
                {
                    point3d.sub(FM.Loc);
                    FM.Or.transformInv(point3d);
                    if(point3d.y < 0.0D)
                    {
                        FM.turret[0].target = actor;
                        FM.turret[0].tMode = 2;
                    }
                }
            } else
            if(actor != null)
            {
                for(int i = 0; i < FM.turret.length; i++)
                    if(FM.turret[i].target != null && !(FM.turret[i].target instanceof Aircraft) && !isAlive(FM.turret[i].target))
                        FM.turret[i].target = null;

            }
        }
    	
        if(obsMove < obsMoveTot && !bObserverKilled && !((FlightModelMain) (super.FM)).AS.isPilotParatrooper(1))
        {
            if(obsMove < 0.2F || obsMove > obsMoveTot - 0.2F)
                obsMove += 0.29999999999999999D * (double)f;
            else
            if(obsMove < 0.1F || obsMove > obsMoveTot - 0.1F)
                obsMove += 0.15F;
            else
                obsMove += 1.2D * (double)f;
            obsLookAzimuth = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsAzimuthOld, obsAzimuth);
            obsLookElevation = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsElevationOld, obsElevation);
            hierMesh().chunkSetAngles("Head2_D0", 0.0F, obsLookAzimuth, obsLookElevation);
        }
        super.update(f);
        if((double)super.FM.getSpeedKMH() >= 280D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            FM.Sq.dragParasiteCx += 0.03F;
        if((double)super.FM.getSpeedKMH() >= 280D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.03F;
        if((double)super.FM.getSpeedKMH() >= 290D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.03F;
        if((double)super.FM.getSpeedKMH() >= 290D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.03F;
        if((double)super.FM.getSpeedKMH() >= 310D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.03F;
        if((double)super.FM.getSpeedKMH() >= 310D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.03F;
        
        double kren = ((FlightModelMain) (super.FM)).Or.getKren();
        double tang = ((FlightModelMain) (super.FM)).Or.getTangage();
        float aile = ((FlightModelMain) (super.FM)).CT.getAileron() + (float)forceTrim_x;
        if(aile > 1.0F)
            aile = 1.0F;
        if(aile < -1F)
            aile = -1F;
        float elev = ((FlightModelMain) (super.FM)).CT.getElevator() + (float)forceTrim_y;
        if(elev > 1.0F)
            elev = 1.0F;
        if(elev < -1F)
            elev = -1F;
        float rudd = ((FlightModelMain) (super.FM)).CT.getRudder() + ((FlightModelMain) (super.FM)).CT.getTrimRudderControl();
        if(rudd > 1.0F)
        	rudd = 1.0F;
        if(rudd < -1F)
        	rudd = -1F;
        if(getTrim)
        {
            forceTrim_x = aile;
            forceTrim_y = elev;
            forceTrim_z = rudd;
            getTrim = false;
        }
        double Wx = ((Tuple3d) (super.FM.getW())).x;
        double Wy = ((Tuple3d) (super.FM.getW())).y;
        double Wz = ((Tuple3d) (super.FM.getW())).z;
        double AWx = ((Tuple3d) (super.FM.getAW())).x;
        double AWy = ((Tuple3d) (super.FM.getAW())).y;
        double AWz = ((Tuple3d) (super.FM.getAW())).z;
        float aPitch = (((FlightModelMain) (super.FM)).EI.engines[0].getControlProp() + ((FlightModelMain) (super.FM)).EI.engines[1].getControlProp()) / 2.0F;
        float aThrottle = (((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle() + ((FlightModelMain) (super.FM)).EI.engines[1].getControlThrottle()) / 2.0F;
        float aThrust = (((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() + ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput()) / 2.0F;
        Vector3d vFlow = super.FM.getVflow();
        double sinkRate = ((Tuple3d) (vFlow)).z;
        float airDensity = Atmosphere.density((float)((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).z);
        double rotorSurface = 30D;
        double rotorSurface_cyclic = 10D;
        double tailRotorSurface = 1.3500000000000001D;
        double rotorCy = 4.3D;
        double rotorCx = 0.0014D;
        double rotorCx_dyn = 0.00050000000000000001D;
        double rotorLineCx = 0.00050000000000000001D * (double)(aPitch * aPitch * 15F * 15F);
        double tailRotorLineCx = 0.00050000000000000001D * (double)(rudd * rudd * 5F * 5F);
        double rotorCyDyn_0 = 0.17999999999999999D;
        double rotorCyDyn_line = 0.089999999999999997D;
        double rotorDiameter = 17.300000000000001D;
        double tailRotorDiameter = 4D;
        double rotorRPM_max = 4D;
        double rotorRPM = (double)aThrust * rotorRPM_max;
        double hubDirection_x = Math.toRadians(0.0D);
        double hubDirection_y = Math.toRadians(5D);
        double rotorHeight = 2D;
        double rotorSpeed = 6.2831853071795862D * (rotorDiameter / 2D) * 0.5D * rotorRPM;
        double autoPitch = PitchAuto(-Wy);
        double autoRoll = RollAuto(Wx);
        double d_hubDirection_x = Math.toRadians(-((double)aile + autoRoll) * 2D);
        double d_hubDirection_y = Math.toRadians(((double)elev + autoPitch) * 5D);
        double rotorLift_dyn = 0.5D * (rotorCyDyn_0 + rotorCyDyn_line * 13D * (double)aPitch) * rotorSurface * (double)airDensity * rotorSpeed * rotorSpeed;
        double rotorLift_moment_z = 0.5D * (rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * rotorSpeed * rotorSpeed;
        double rotorLift_moment_y = -(rotorDiameter / 2D) * 0.5D * 0.5D * (rotorCyDyn_line * 5D * ((double)elev + autoPitch)) * rotorSurface_cyclic * (double)airDensity * rotorSpeed * rotorSpeed;
        double rotorLift_moment_x = (rotorDiameter / 2D) * 0.5D * 0.5D * (rotorCyDyn_line * 3D * ((double)aile + autoRoll)) * rotorSurface_cyclic * (double)airDensity * rotorSpeed * rotorSpeed;
        rotorLift_moment_y += rotorLift_dyn * (rotorHeight * d_hubDirection_y);
        rotorLift_moment_x += rotorLift_dyn * (rotorHeight * d_hubDirection_x);
        double tailRotorSpeed = (6.2831853071795862D * (tailRotorDiameter / 2D) * 0.5D * (double)aThrust * 1112D) / 60D;
        double tailRotorLift_dyn = 0.5D * (rotorCyDyn_line * 10D * (double)rudd) * (double)airDensity * tailRotorSpeed * tailRotorSpeed;
        double tailRotorLift_moment_y = (tailRotorDiameter / 2D) * 0.5D * 0.5D * (rotorCx + tailRotorLineCx) * tailRotorSurface * (double)airDensity * tailRotorSpeed * tailRotorSpeed;
        double tailRotorLift_moment_z = tailRotorLift_dyn * 10D;
        double rotateSpeed_z = Wz * (rotorDiameter / 2D) * 0.5D;
        double rotateSpeed_y = Wy * (rotorDiameter / 2D) * 0.5D;
        double rotateSpeed_x = Wx * (rotorDiameter / 2D) * 0.5D;
        double inertia_x = (double)(-((FlightModelMain) (super.FM)).M.getFullMass()) * (AWx * 0.0D * 0.033000000000000002D + Wx * 0.0D) * 3D * 3D;
        double inertia_y = (double)(-((FlightModelMain) (super.FM)).M.getFullMass()) * (AWy * 0.0D * 0.033000000000000002D + Wy * 0.0D) * 17D * 17D;
        double inertia_z = (double)(-((FlightModelMain) (super.FM)).M.getFullMass()) * (AWz * 0.0D * 0.033000000000000002D + Wz * 0.0D) * 17D * 17D;
        double balanceMoment_x = (rotorDiameter / 2D) * 0.5D * rotateSpeed_x * rotateSpeed_x * (rotorSurface + 6.4000000000000004D) * (double)airDensity * rotorCy * 0.5D;
        if(rotateSpeed_x < 0.0D)
            balanceMoment_x = 0.0D - balanceMoment_x;
        double balanceMoment_y = (rotorDiameter / 2D) * 0.5D * rotateSpeed_y * rotateSpeed_y * rotorSurface * (double)airDensity * rotorCy * 0.5D;
        if(rotateSpeed_y < 0.0D)
            balanceMoment_y = 0.0D - balanceMoment_y;
        double balanceMoment_z = 10D * rotateSpeed_z * rotateSpeed_z * tailRotorSurface * (double)airDensity * rotorCy * 0.5D;
        if(rotateSpeed_z < 0.0D)
            balanceMoment_z = 0.0D - balanceMoment_z;
        double G = 9.8100000000000005D * (double)((FlightModelMain) (super.FM)).M.getFullMass();
        double balanceMoment_G_x = 0.0D;
        double balanceMoment_G_y = 0.0D;
        double balanceMoment_G_z = 0.0D;
        float antiSinkForce;
        if(sinkRate >= 0.0D)
            antiSinkForce = -(float)(0.5D * rotorCy * rotorSurface * (double)airDensity * sinkRate * sinkRate);
        else
            antiSinkForce = (float)(0.5D * rotorCy * rotorSurface * (double)airDensity * sinkRate * sinkRate);
        float headOnForce;
        double dragMoment_y;
        if(((Tuple3d) (vFlow)).x >= 0.0D)
        {
            headOnForce = -(float)(0.5D * (rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
            dragMoment_y = -2D * (0.5D * (rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
        } else
        {
            headOnForce = (float)(0.5D * (rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
            dragMoment_y = 2D * (0.5D * (rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
        }
        float sideForce;
        float tailRotorMoment;
        double dragMoment_x;
        if(((Tuple3d) (vFlow)).y >= 0.0D)
        {
            sideForce = -(float)(0.5D * (rotorCx + rotorLineCx + 1.0D) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
            tailRotorMoment = (float)(0.5D * rotorCy * tailRotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y) * 10F;
            dragMoment_x = 2D * (0.5D * (rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
        } else
        {
            sideForce = (float)(0.5D * (rotorCx + rotorLineCx + 1.0D) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
            tailRotorMoment = -(float)(0.5D * rotorCy * tailRotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y) * 10F;
            dragMoment_x = -2D * (0.5D * (rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
        }
        double rotorLift_3D_x = Math.sin(hubDirection_y - d_hubDirection_y) * Math.cos(hubDirection_x + d_hubDirection_x) * rotorLift_dyn;
        double rotorLift_3D_y = Math.sin(hubDirection_x + d_hubDirection_x) * rotorLift_dyn;
        double rotorLift_3D_z = Math.cos(hubDirection_y - d_hubDirection_y) * Math.cos(hubDirection_x + d_hubDirection_x) * rotorLift_dyn;
        ((FlightModelMain) (super.FM)).producedAF.x += (double)headOnForce + rotorLift_3D_x;
        ((FlightModelMain) (super.FM)).producedAF.y += (double)sideForce + rotorLift_3D_y;
        ((FlightModelMain) (super.FM)).producedAF.z += (double)antiSinkForce + rotorLift_3D_z;
        ((FlightModelMain) (super.FM)).producedAM.x += (dragMoment_x - balanceMoment_x) + rotorLift_moment_x;
        ((FlightModelMain) (super.FM)).producedAM.y += ((dragMoment_y + tailRotorLift_moment_y) - balanceMoment_y) + rotorLift_moment_y;
        ((FlightModelMain) (super.FM)).producedAM.z += ((double)tailRotorMoment - tailRotorLift_moment_z - balanceMoment_z) + rotorLift_moment_z;
        rotateSpeed_z = 0.0D;
        rotateSpeed_y = 0.0D;
        rotateSpeed_x = 0.0D;
        headOnForce = 0.0F;
        sideForce = 0.0F;
        
//        FM.producedAF.z += aPitch * 40000;
        
        
        Vector3d localVector3d = new Vector3d();
        getSpeed(localVector3d);
        Point3d localPoint3d1 = new Point3d();
        this.pos.getAbs(localPoint3d1);
        float falt = (float)(this.FM.getAltitude() - World.land().HQ(localPoint3d1.x, localPoint3d1.y));
        if ((falt < 10.0F) && (this.FM.getSpeedKMH() < 60.0F) && (localVector3d.z < -1.0D))
        {
        	localVector3d.z *= 0.9D;
        	setSpeed(localVector3d);
        }
    }
    
    public double PitchAuto(double p)
    {
        p = -(p * 4D);
        if(p >= 0.20000000000000001D)
            p = 0.20000000000000001D;
        if(p <= -0.20000000000000001D)
            p = -0.20000000000000001D;
        return p;
    }

    public double RollAuto(double k)
    {
        k = -(k * 4D);
        if(k >= 0.20000000000000001D)
            k = 0.20000000000000001D;
        if(k <= -0.20000000000000001D)
            k = -0.20000000000000001D;
        return k;
    }
    
    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
        	HUD.log(AircraftHotKeys.hudLogWeaponId, "Trim: Set");
            getTrim = true;
//            float ctrlZ = ((FlightModelMain) (super.FM)).CT.getRudder();
//            float trimZ = ((FlightModelMain) (super.FM)).CT.getTrimRudderControl();
//            ((FlightModelMain) (super.FM)).CT.setTrimRudderControl(ctrlZ - trimZ);
        if(i == 21)
        {
            forceTrim_x = 0.0D;
            forceTrim_y = 0.0D;
            forceTrim_z = 0.0D;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Trim: Reset");
        }
    }
    
    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        if(af[0] < -60F)
        {
            af[0] = -60F;
            flag = false;
        } else
        if(af[0] > 60F)
        {
            af[0] = 60F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if(af[1] < -60F)
        {
            af[1] = -60F;
            flag = false;
        }
        if(af[1] > 60F)
        {
            af[1] = 60F;
            flag = false;
        }
        if(!flag)
            return false;
        float f1 = af[1];
        if(f < 1.2F && f1 < 13.3F)
            return false;
        return f1 >= -3.1F || f1 <= -4.6F;
    }


    private int obsLookTime;
    private float obsLookAzimuth;
    private float obsLookElevation;
    private float obsAzimuth;
    private float obsElevation;
    private float obsAzimuthOld;
    private float obsElevationOld;
    private float obsMove;
    private float obsMoveTot;
    boolean bObserverKilled;
    public static boolean bChangedPit = false;
    public Loc suka;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int rotorrpm;
    
    private float collV; //By PAL
    private float lcollV; //By PAL   
    private boolean TailRotorDestroyed;
    
    private float curAngleRotor;
    private float diffAngleRotor;
    private long lastTimeFan;
    private Orient oMainRotor;
    private Vector3f vThrust;
    private float iMainTorque;
    private float MainRotorStatus;
    private float TailRotorStatus;
    
    public double forceTrim_x;
    public double forceTrim_y;
    public double forceTrim_z;
    public boolean getTrim;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.MI8MT.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mi-8");
        Property.set(class1, "meshName", "3DO/Plane/Mi-8/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1950F);
        Property.set(class1, "yearExpired", 1960.5F);
        Property.set(class1, "FlightModel", "FlightModels/Mi-24V.fmd:HIND");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitMi8.class, 
            com.maddox.il2.objects.air.CockpitMi8_TGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 9, 9, 2, 2, 2, 2,
            7, 7, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN03", "_BombSpawn01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04",
            "_Flare01", "_Flare02", "_BombSpawn01"
          
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 13;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "Default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[1] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(1, "RocketGunS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "RocketGunS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xUB-32-57";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "RocketGunS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(1, "RocketGunS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "RocketGunS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(1, "RocketGunS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "RocketGunS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xUB-32-57+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "RocketGunS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(1, "RocketGunS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "RocketGunS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-250+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = null;
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB-250+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = null;
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            s = "2xFAB-250+2xUB-32-57+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = null;
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "RocketGunS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "RocketGunS5M", 32);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "VDV Squad";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
//            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunVDVRPG", 2);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xUB-32-57+VDV Squad+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS2", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
//            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunVDVRPG", 2);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);     
            s = "VDV Recon Squad";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xUB-32-57+VDV Recon Squad+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);   
            
            s = "None";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }

	public boolean typeBomberToggleAutomation() {
		// TODO Auto-generated method stub
		return false;
	}

	public void typeBomberAdjDistanceReset() {
		// TODO Auto-generated method stub
		
	}

	public void typeBomberAdjDistancePlus() {
		// TODO Auto-generated method stub
		
	}

	public void typeBomberAdjDistanceMinus() {
		// TODO Auto-generated method stub
		
	}

	public void typeBomberAdjSideslipReset() {
		// TODO Auto-generated method stub
		
	}

	public void typeBomberAdjSideslipPlus() {
		// TODO Auto-generated method stub
		
	}

	public void typeBomberAdjSideslipMinus() {
		// TODO Auto-generated method stub
		
	}

	public void typeBomberAdjAltitudeReset() {
		// TODO Auto-generated method stub
		
	}

	public void typeBomberAdjAltitudePlus() {
		// TODO Auto-generated method stub
		
	}

	public void typeBomberAdjAltitudeMinus() {
		// TODO Auto-generated method stub
		
	}

	public void typeBomberAdjSpeedReset() {
		// TODO Auto-generated method stub
		
	}

	public void typeBomberAdjSpeedPlus() {
		// TODO Auto-generated method stub
		
	}

	public void typeBomberAdjSpeedMinus() {
		// TODO Auto-generated method stub
		
	}

	public void typeBomberUpdate(float paramFloat) {
		// TODO Auto-generated method stub
		
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted paramNetMsgGuaranted)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void typeBomberReplicateFromNet(NetMsgInput paramNetMsgInput)
			throws IOException {
		// TODO Auto-generated method stub
		
	}
}
