// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 20.03.2020 17:22:21
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   M33_F.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.io.PrintStream;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, TypeSailPlane, PaintSchemeFMPar01, Aircraft, 
//            Cockpit, AircraftLH, NetAircraft

public class M33_F extends com.maddox.il2.objects.air.Scheme1
    implements com.maddox.il2.objects.air.TypeSeaPlane
{

    public M33_F()
    {
        haveSecondProp = -1;
    }

    public void onAircraftLoaded()
    {
        com.maddox.il2.ai.BulletEmitter be[] = new com.maddox.il2.ai.BulletEmitter[2];
        super.onAircraftLoaded();
        be[0] = getBulletEmitterByHookName("_MGUN01");
        be[1] = getBulletEmitterByHookName("_MGUN02");
        int gunCnt = 0;
        for(int i = 0; i < be.length; i++)
            if(be[i] != com.maddox.il2.objects.weapons.GunEmpty.get())
                gunCnt++;

        if(gunCnt >= 2)
        {
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Fio_D0", false);
        } else
        {
            hierMesh().chunkVisible("Pilot2_D0", true);
            hierMesh().chunkVisible("Fio_D0", true);
        }
    }

    protected void moveFan(float f)
    {
        if(haveSecondProp == -1)
            try
            {
                hierMesh().chunkFind(com.maddox.il2.objects.air.Aircraft.Props[1][0]);
                haveSecondProp = 1;
            }
            catch(java.lang.Exception e)
            {
                haveSecondProp = 0;
            }
        int i = 0;
        for(int j = 0; j < (haveSecondProp != 1 ? 1 : 2); j++)
        {
            if(oldProp[j] < 2)
            {
                i = java.lang.Math.abs((int)(FM.EI.engines[0].getw() * 0.06F));
                if(i >= 1)
                    i = 1;
                if(i != oldProp[j] && hierMesh().isChunkVisible(com.maddox.il2.objects.air.Aircraft.Props[j][oldProp[j]]))
                {
                    hierMesh().chunkVisible(com.maddox.il2.objects.air.Aircraft.Props[j][oldProp[j]], false);
                    oldProp[j] = i;
                    hierMesh().chunkVisible(com.maddox.il2.objects.air.Aircraft.Props[j][i], true);
                }
            }
            if(i == 0)
            {
                propPos[j] = (propPos[j] + 57.3F * FM.EI.engines[0].getw() * f) % 360F;
            } else
            {
                float f1 = 57.3F * FM.EI.engines[0].getw();
                f1 %= 2880F;
                f1 /= 2880F;
                if(f1 <= 0.5F)
                    f1 *= 2.0F;
                else
                    f1 = f1 * 2.0F - 2.0F;
                f1 *= 1200F;
                propPos[j] = (propPos[j] + f1 * f) % 360F;
            }
            if(j == 0)
            {
                hierMesh().chunkSetAngles(com.maddox.il2.objects.air.Aircraft.Props[j][i], 0.0F, -propPos[j], 0.0F);
                cockpitFanMoveFan(com.maddox.il2.objects.air.Aircraft.Props[j][i], 0.0F, -propPos[j], 0.0F);
            } else
            {
                hierMesh().chunkSetAngles(com.maddox.il2.objects.air.Aircraft.Props[j][i], 0.0F, propPos[j], 0.0F);
                cockpitFanMoveFan(com.maddox.il2.objects.air.Aircraft.Props[j][i], 0.0F, propPos[j], 0.0F);
            }
        }

    }

    void cockpitFanMoveFan(java.lang.String mesh, float x, float y, float z)
    {
        if(com.maddox.il2.engine.Config.isUSE_RENDER())
            try
            {
                if(com.maddox.il2.game.Main3D.cur3D().cockpits != null && com.maddox.il2.game.Main3D.cur3D().cockpits[0] != null && (mesh.equals("PropRot2_D0") || mesh.equals("Prop2_D0")))
                    com.maddox.il2.game.Main3D.cur3D().cockpits[0].mesh.chunkSetAngles(mesh, x, y, z);
                if(com.maddox.il2.game.Main3D.cur3D().cockpits != null && com.maddox.il2.game.Main3D.cur3D().cockpits[1] != null)
                    com.maddox.il2.game.Main3D.cur3D().cockpits[1].mesh.chunkSetAngles(mesh, x, y, z);
            }
            catch(java.lang.Exception exception) { }
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh1, float f1)
    {
    }

    protected void moveGear(float f1)
    {
    }

    public void moveWheelSink()
    {
    }

    public void moveSteering(float f1)
    {
    }

    public void update(float f)
    {
        super.update(f);
        for(int i = 0; i < 3; i++)
        {
            for(int i_0_ = 0; i_0_ < 2; i_0_++)
                if(FM.Gears.clpGearEff[i][i_0_] != null)
                {
                    tmpp.set(FM.Gears.clpGearEff[i][i_0_].pos.getAbsPoint());
                    tmpp.z = 0.01D;
                    FM.Gears.clpGearEff[i][i_0_].pos.setAbs(tmpp);
                    FM.Gears.clpGearEff[i][i_0_].pos.reset();
                }

        }

        if(FM.CT.BrakeControl > 0.4F)
            FM.CT.BrakeControl = 0.4F;
    }

    protected void moveAileron(float f)
    {
        if(f > 0.0F)
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -16F * f, 0.0F);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -28.5F * f, 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -16F * f, 0.0F);
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -28.5F * f, 0.0F);
        }
    }

    protected void moveElevator(float f)
    {
        if(f > 0.0F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -35F * f, 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -35F * f, 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -24.5F * f, 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -24.5F * f, 0.0F);
        }
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -30F * f, 0.0F);
    }

    public void moveCockpitDoor(float f)
    {
        if(!hierMesh().isChunkVisible("Fio_D0"))
            return;
        resetYPRmodifier();
        com.maddox.il2.objects.air.Aircraft.xyz[2] = com.maddox.il2.objects.air.Aircraft.cvt(f, 0.7F, 0.9F, 0.0F, 0.22F);
        hierMesh().chunkSetLocate("Pilot2_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
        hierMesh().chunkSetAngles("Hatch_D0", 0.0F, 60F * f, 0.0F);
        if(com.maddox.il2.engine.Config.isUSE_RENDER())
        {
            if(com.maddox.il2.game.Main3D.cur3D().cockpits != null && com.maddox.il2.game.Main3D.cur3D().cockpits[0] != null)
                com.maddox.il2.game.Main3D.cur3D().cockpits[0].onDoorMoved(f);
            if(com.maddox.il2.game.Main3D.cur3D().cockpits != null && com.maddox.il2.game.Main3D.cur3D().cockpits[1] != null)
                com.maddox.il2.game.Main3D.cur3D().cockpits[1].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected void hitBone(java.lang.String s, com.maddox.il2.ai.Shot shot, com.maddox.JGP.Point3d point3d)
    {
        super.hitBone(s, shot, point3d);
        if(s.startsWith("xgearc"))
            hitChunk("GearC2", shot);
        if(s.startsWith("xgearl"))
            hitChunk("GearL2", shot);
        if(s.startsWith("xgearr"))
            hitChunk("GearR2", shot);
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1"))
                {
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    if(getEnergyPastArmor((double)com.maddox.il2.ai.World.Rnd().nextFloat(32.5F, 65F) / (java.lang.Math.abs(com.maddox.il2.objects.air.Aircraft.v1.x) + 9.9999997473787516E-005D), shot) < 0.0F)
                        doRicochetBack(shot);
                } else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(13.130000114440918D / (java.lang.Math.abs(com.maddox.il2.objects.air.Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("p3"))
                    getEnergyPastArmor((double)com.maddox.il2.ai.World.Rnd().nextFloat(8.7F, 9.81F) / (java.lang.Math.abs(com.maddox.il2.objects.air.Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                return;
            }
            if(s.startsWith("xxcanon0"))
            {
                int i = s.charAt(8) - 49;
                if(getEnergyPastArmor(6.29F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Cannon (" + i + ") Disabled..");
                    FM.AS.setJamBullets(1, i);
                    getEnergyPastArmor(com.maddox.il2.ai.World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int j = s.charAt(10) - 48;
                switch(j)
                {
                default:
                    break;

                case 1: // '\001'
                case 2: // '\002'
                case 3: // '\003'
                case 4: // '\004'
                    if(getEnergyPastArmor(0.99F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.175F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 5: // '\005'
                case 6: // '\006'
                    if(getEnergyPastArmor(0.22F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.275F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;

                case 7: // '\007'
                    if(getEnergyPastArmor(4.2F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.175F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    break;

                case 8: // '\b'
                    if(getEnergyPastArmor(3.2F, shot) > 0.0F)
                    {
                        com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 9: // '\t'
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
                    }
                    break;
                }
                return;
            }
            if(s.startsWith("xxeng1"))
            {
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(0.2F, shot) > 0.0F)
                    {
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 140000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, 0);
                            com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                        }
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 85000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                        }
                    } else
                    if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.01F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                    } else
                    {
                        FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - 0.002F);
                        com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(5.85F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 0.75F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, com.maddox.il2.ai.World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 48000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("gear"))
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        if(com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        if(com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                        if(com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        if(com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    }
                    getEnergyPastArmor(2.0F, shot);
                }
                if(s.endsWith("prop") && getEnergyPastArmor(0.2F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.5F)
                    FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                return;
            }
            if(s.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(2.5F, shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
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
                    getEnergyPastArmor(com.maddox.il2.ai.World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
            if(s.startsWith("xxoil"))
            {
                if(getEnergyPastArmor(0.25F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.125F)
                {
                    FM.AS.hitOil(shot.initiator, 0);
                    getEnergyPastArmor(0.22F, shot);
                    debuggunnery("Engine Module: Oil Tank Pierced..");
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(6.9600000381469727D / (java.lang.Math.abs(com.maddox.il2.objects.air.Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLIn Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(6.9600000381469727D / (java.lang.Math.abs(com.maddox.il2.objects.air.Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRIn Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(6.9600000381469727D / (java.lang.Math.abs(com.maddox.il2.objects.air.Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(6.9600000381469727D / (java.lang.Math.abs(com.maddox.il2.objects.air.Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(6.9600000381469727D / (java.lang.Math.abs(com.maddox.il2.objects.air.Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(6.9600000381469727D / (java.lang.Math.abs(com.maddox.il2.objects.air.Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.86F / (float)java.lang.Math.sqrt(com.maddox.il2.objects.air.Aircraft.v1.y * com.maddox.il2.objects.air.Aircraft.v1.y + com.maddox.il2.objects.air.Aircraft.v1.z * com.maddox.il2.objects.air.Aircraft.v1.z), shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.25F)
                {
                    debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(l > 3)
                    return;
                if(getEnergyPastArmor(0.8F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.45F)
                {
                    if(FM.AS.astateTankStates[l] == 0)
                    {
                        debuggunnery("Fuel Tank (" + l + "): Pierced..");
                        FM.AS.hitTank(shot.initiator, l, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.008F || shot.powerType == 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.6F)
                    {
                        FM.AS.hitTank(shot.initiator, l, 1);
                        debuggunnery("Fuel Tank (" + l + "): Hit..");
                    }
                }
                return;
            } else
            {
                return;
            }
        }
        if(s.startsWith("xcf") || s.startsWith("xcock"))
        {
            hitChunk("CF", shot);
            return;
        }
        if(s.startsWith("xeng"))
        {
            if(chunkDamageVisible("Engine1") < 2 && getEnergyPastArmor(1.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xtail"))
        {
            if(getEnergyPastArmor(2.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 5000F) < shot.power)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
        {
            if(getEnergyPastArmor(1.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 1 && getEnergyPastArmor(2.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 3000F) < shot.power && getEnergyPastArmor(1.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl") && getEnergyPastArmor(1.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr") && getEnergyPastArmor(1.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1 && getEnergyPastArmor(1.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1 && getEnergyPastArmor(1.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwinglin") && getEnergyPastArmor(2.5F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 5000F) < shot.power)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xwingrin") && getEnergyPastArmor(2.5F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 5000F) < shot.power)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xwinglmid") && getEnergyPastArmor(2.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 4000F) < shot.power)
                hitChunk("WingLMid", shot);
            if(s.startsWith("xwingrmid") && getEnergyPastArmor(2.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 4000F) < shot.power)
                hitChunk("WingRMid", shot);
            if(s.startsWith("xwinglout") && getEnergyPastArmor(1.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
                hitChunk("WingLOut", shot);
            if(s.startsWith("xwingrout") && getEnergyPastArmor(1.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, 3000F) < shot.power)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel") && chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner") && chunkDamageVisible("AroneR") < 1)
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.05F)
            {
                debuggunnery("Hydro System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(com.maddox.il2.ai.World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
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

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -33F * f, 0.0F);
        hierMesh().chunkSetAngles("GearC11_D0", 0.0F, -30F * f, 0.0F);
    }

    protected int haveSecondProp;
    private static com.maddox.JGP.Point3d tmpp = new Point3d();

    static 
    {
        java.lang.Class var_class = com.maddox.il2.objects.air.M33_F.class;
        new NetAircraft.SPAWN(var_class);
        com.maddox.rts.Property.set(var_class, "iconFar_shortClassName", "M33");
        com.maddox.rts.Property.set(var_class, "meshName", "3DO/Plane/M33_F(Multi1)/hier.him");
        com.maddox.rts.Property.set(var_class, "PaintScheme", new PaintSchemeFMPar01());
        com.maddox.rts.Property.set(var_class, "meshName_ja", "3DO/Plane/M33_F(Multi1)/hier.him");
        com.maddox.rts.Property.set(var_class, "PaintScheme_ja", new PaintSchemeFMPar01());
        com.maddox.rts.Property.set(var_class, "yearService", 1932F);
        com.maddox.rts.Property.set(var_class, "yearExpired", 1945.5F);
        int M33FMType = 0;
        try
        {
            if(com.maddox.il2.fm.FlightModelMain.sectFile("FlightModels/FlightModels/M33.fmd") != null)
                M33FMType = 33;
            else
            if(com.maddox.il2.fm.FlightModelMain.sectFile("FlightModels/A6M2N.fmd") != null)
                M33FMType = 0;
        }
        catch(java.lang.Exception exception) { }
        switch(M33FMType)
        {
        case 33: // '!'
            com.maddox.rts.Property.set(var_class, "FlightModel", "FlightModels/FlightModels/M33.fmd");
            java.lang.System.out.println("M33 Loading FMD: FlightModels/FlightModels/M33.fmd");
            break;

        case 0: // '\0'
        default:
            com.maddox.rts.Property.set(var_class, "FlightModel", "FlightModels/A6M2N.fmd");
            java.lang.System.out.println("M33 Loading FMD: FlightModels/A6M2N.fmd");
            break;
        }
        com.maddox.rts.Property.set(var_class, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitM33_F.class, com.maddox.il2.objects.air.CockpitM33_F_Front.class
        });
        com.maddox.rts.Property.set(var_class, "LOSElevation", 1.01885F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(var_class, new int[2]);
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(var_class, new java.lang.String[] {
            "_MGUN01", "_MGUN02"
        });
    }
}