// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 24.03.2020 16:29:39
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   MB5.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.DifficultySettings;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.security.SecureRandom;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, PaintSchemeFMPar04, TypeFighter, TypeBNZFighter, 
//            TypeStormovik, Aircraft, Cockpit, NetAircraft, 
//            PaintScheme

public class MB5 extends com.maddox.il2.objects.air.Scheme1
    implements com.maddox.il2.objects.air.TypeFighter, com.maddox.il2.objects.air.TypeBNZFighter, com.maddox.il2.objects.air.TypeStormovik
{

    public MB5()
    {
        kangle = 0.0F;
        flapps = 0.0F;
        SecondProp = 0;
        java.security.SecureRandom securerandom = new SecureRandom();
        securerandom.setSeed(java.lang.System.currentTimeMillis());
        com.maddox.il2.ai.RangeRandom rangerandom = new RangeRandom(securerandom.nextLong());
        for(int i = 0; i < rndgear.length; i++)
            rndgear[i] = rangerandom.nextFloat(0.0F, 0.15F);

    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
    }

    protected void nextDMGLevel(java.lang.String s, int i, com.maddox.il2.engine.Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(java.lang.String s, int i, com.maddox.il2.engine.Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    public void msgCollision(com.maddox.il2.engine.Actor actor, java.lang.String s, java.lang.String s1)
    {
        if(isNet() && isNetMirror())
            return;
        if(s.startsWith("Hook"))
        {
            return;
        } else
        {
            super.msgCollision(actor, s, s1);
            return;
        }
    }

    protected void moveFlap(float f)
    {
        float f1 = -85F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap05_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap06_D0", 0.0F, f1, 0.0F);
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh, float f, float f1, float f2, boolean flag, float af[])
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("TapaL", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(f, 0.01F, 0.08F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(f1, 0.2F, 0.99F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("TapaR", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(f1, 0.11F, 0.18F, 0.0F, 85F), 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(f2, 0.1F, 0.99F, 0.0F, -45F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(f2, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, com.maddox.il2.objects.air.Aircraft.cvt(f2, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh, float f, float f1, float f2)
    {
        com.maddox.il2.objects.air.MB5.moveGear(hiermesh, f, f1, f2, true, rndgearnull);
    }

    protected void moveGear(float f, float f1, float f2)
    {
        com.maddox.il2.objects.air.MB5.moveGear(hierMesh(), f, f1, f2, FM.CT.GearControl > 0.5F, rndgear);
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh, float f, boolean flag)
    {
        com.maddox.il2.objects.air.MB5.moveGear(hiermesh, f, f, f, flag, rndgearnull);
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh, float f)
    {
        com.maddox.il2.objects.air.MB5.moveGear(hiermesh, f, f, f, true, rndgearnull);
    }

    protected void moveGear(float f)
    {
        com.maddox.il2.objects.air.MB5.moveGear(hierMesh(), f, f, f, FM.CT.GearControl > 0.5F, rndgear);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        com.maddox.il2.objects.air.Aircraft.xyz[2] = com.maddox.il2.objects.air.Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.247F, -0.02F, 0.227F);
        hierMesh().chunkSetLocate("GearL3_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
        com.maddox.il2.objects.air.Aircraft.xyz[2] = com.maddox.il2.objects.air.Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.247F, 0.02F, -0.227F);
        hierMesh().chunkSetLocate("GearR3_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
    }

    public void moveCockpitDoor(float f)
    {
        com.maddox.il2.objects.air.Aircraft.xyz[0] = 0.0F;
        com.maddox.il2.objects.air.Aircraft.xyz[2] = 0.0F;
        com.maddox.il2.objects.air.Aircraft.ypr[0] = 0.0F;
        com.maddox.il2.objects.air.Aircraft.ypr[1] = 0.0F;
        com.maddox.il2.objects.air.Aircraft.ypr[2] = 0.0F;
        com.maddox.il2.objects.air.Aircraft.xyz[1] = f * 0.548F;
        hierMesh().chunkSetLocate("Blister1_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
        float f1 = (float)java.lang.Math.sin(com.maddox.il2.objects.air.Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
        hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
        hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
        if(com.maddox.il2.engine.Config.isUSE_RENDER())
        {
            if(com.maddox.il2.game.Main3D.cur3D().cockpits != null && com.maddox.il2.game.Main3D.cur3D().cockpits[0] != null)
                com.maddox.il2.game.Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected void moveFan(float f)
    {
        hierMesh().chunkFind(com.maddox.il2.objects.air.Aircraft.Props[1][0]);
        SecondProp = 1;
        int i = 0;
        for(int j = 0; j < (SecondProp == 1 ? 2 : 1); j++)
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
                hierMesh().chunkSetAngles(com.maddox.il2.objects.air.Aircraft.Props[j][i], 0.0F, propPos[j], 0.0F);
            else
                hierMesh().chunkSetAngles(com.maddox.il2.objects.air.Aircraft.Props[j][i], 0.0F, -(propPos[j] - 20F), 0.0F);
        }

    }

    public void hitProp(int i, int j, com.maddox.il2.engine.Actor actor)
    {
        if(i > FM.EI.getNum() - 1 || oldProp[i] == 2)
            return;
        if((isChunkAnyDamageVisible("Prop" + (i + 1)) || isChunkAnyDamageVisible("PropRot" + (i + 1))) && SecondProp == 1)
        {
            hierMesh().chunkVisible(com.maddox.il2.objects.air.Aircraft.Props[i + 1][0], false);
            hierMesh().chunkVisible(com.maddox.il2.objects.air.Aircraft.Props[i + 1][1], false);
            hierMesh().chunkVisible(com.maddox.il2.objects.air.Aircraft.Props[i + 1][2], true);
        }
        super.hitProp(i, j, actor);
    }

    public void update(float f)
    {
        com.maddox.il2.ai.World.cur().diffCur.Torque_N_Gyro_Effects = false;
        super.update(f);
        if(java.lang.Math.abs(flapps - kangle) > 0.01F)
        {
            flapps = kangle;
            hierMesh().chunkSetAngles("Oil1_D0", 0.0F, -14F * kangle, 0.0F);
        }
        kangle = 0.95F * kangle + 0.05F * FM.EI.engines[0].getControlRadiator();
        if(kangle > 1.0F)
            kangle = 1.0F;
    }

    public void doMurderPilot(int i)
    {
        if(i != 0)
        {
            return;
        } else
        {
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            return;
        }
    }

    protected void hitBone(java.lang.String s, com.maddox.il2.ai.Shot shot, com.maddox.JGP.Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1"))
                    getEnergyPastArmor(6.78F, shot);
                else
                if(s.endsWith("g1"))
                    getEnergyPastArmor(9.96F / (1E-005F + (float)java.lang.Math.abs(com.maddox.il2.objects.air.Aircraft.v1.x)), shot);
                else
                if(s.endsWith("g2"))
                    getEnergyPastArmor(com.maddox.il2.ai.World.Rnd().nextFloat(30F, 50F) / (1E-005F + (float)java.lang.Math.abs(com.maddox.il2.objects.air.Aircraft.v1.x)), shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                if(s.endsWith("1"))
                {
                    if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.12F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                    }
                    if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.12F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                    }
                    if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.12F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Arone Controls Out..");
                    }
                } else
                if(s.endsWith("2"))
                {
                    if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                    }
                } else
                if(s.endsWith("3") && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 2);
                    com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.5F / (float)java.lang.Math.sqrt(com.maddox.il2.objects.air.Aircraft.v1.y * com.maddox.il2.objects.air.Aircraft.v1.y + com.maddox.il2.objects.air.Aircraft.v1.z * com.maddox.il2.objects.air.Aircraft.v1.z), shot) > 0.0F)
                {
                    com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(3.5F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(3.5F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(3.5F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(3.5F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if((s.endsWith("lo1") || s.endsWith("lo2")) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(3.5F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if((s.endsWith("ro1") || s.endsWith("ro2")) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(3.5F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if((s.endsWith("sl1") || s.endsWith("sl2") || s.endsWith("sl3") || s.endsWith("sl4") || s.endsWith("sl5")) && chunkDamageVisible("StabL") > 2 && getEnergyPastArmor(3.5F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D3", shot.initiator);
                }
                if((s.endsWith("sr1") || s.endsWith("sr2") || s.endsWith("sr3") || s.endsWith("sr4") || s.endsWith("sr5")) && chunkDamageVisible("StabR") > 2 && getEnergyPastArmor(3.5F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D3", shot.initiator);
                }
                if(s.endsWith("e1"))
                    getEnergyPastArmor(6F, shot);
                return;
            }
            if(s.startsWith("xxeng1"))
            {
                if(s.endsWith("prp") && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                if(s.endsWith("cas") && getEnergyPastArmor(0.1F, shot) > 0.0F)
                {
                    if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 200000F)
                    {
                        FM.AS.setEngineStuck(shot.initiator, 0);
                        com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                    }
                    if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 50000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 2);
                        com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                    }
                    if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 28000F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                        com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Cylinder Feed Out, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    }
                    FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - com.maddox.il2.ai.World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                }
                if(s.endsWith("cyl") && getEnergyPastArmor(0.45F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.75F)
                {
                    FM.EI.engines[0].setCyliderKnockOut(shot.initiator, com.maddox.il2.ai.World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    if(FM.AS.astateEngineStates[0] < 1)
                        FM.AS.hitEngine(shot.initiator, 0, 1);
                    if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 3);
                        com.maddox.il2.objects.air.Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("sup") && getEnergyPastArmor(0.05F, shot) > 0.0F)
                    FM.EI.engines[0].setKillCompressor(shot.initiator);
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int i = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.25F)
                {
                    FM.AS.hitTank(shot.initiator, i, 1);
                    if(shot.powerType == 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.11F)
                        FM.AS.hitTank(shot.initiator, i, 2);
                }
                return;
            }
            if(s.startsWith("xxmgunl1") && getEnergyPastArmor(4.85F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.75F)
                FM.AS.setJamBullets(0, 0);
            if(s.startsWith("xxmgunr1") && getEnergyPastArmor(4.85F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.75F)
                FM.AS.setJamBullets(0, 1);
            if(s.startsWith("xxmgunl2") && getEnergyPastArmor(4.85F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.75F)
                FM.AS.setJamBullets(0, 2);
            if(s.startsWith("xxmgunr2") && getEnergyPastArmor(4.85F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.75F)
                FM.AS.setJamBullets(0, 3);
            if(s.startsWith("xxhispa1") && getEnergyPastArmor(4.85F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.75F)
                FM.AS.setJamBullets(1, 0);
            if(s.startsWith("xxhispa2") && getEnergyPastArmor(4.85F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.75F)
                FM.AS.setJamBullets(1, 1);
            if(s.startsWith("xxhispa3") && getEnergyPastArmor(4.85F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.75F)
                FM.AS.setJamBullets(1, 2);
            if(s.startsWith("xxhispa4") && getEnergyPastArmor(4.85F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.75F)
                FM.AS.setJamBullets(1, 3);
            return;
        }
        if(s.startsWith("xcf") || s.startsWith("xcockpit"))
            hitChunk("CF", shot);
        else
        if(s.startsWith("xeng"))
        {
            if(chunkDamageVisible("Engine1") < 3)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
            hitChunk("Keel1", shot);
        else
        if(s.startsWith("xrudder"))
            hitChunk("Rudder1", shot);
        else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 3)
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 3)
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
                if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.mass + 0.02F)
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
        {
            if(s.startsWith("xoil"))
            {
                if(getEnergyPastArmor(0.5F, shot) > 0.0F)
                {
                    debuggunnery("Engine Module: Oil Radiator Hit, Oil Radiator Pierced..");
                    FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if(s.startsWith("xwater"))
            {
                if(FM.AS.astateEngineStates[0] == 0)
                {
                    debuggunnery("Engine Module: Water Radiator Pierced..");
                    FM.AS.hitEngine(shot.initiator, 0, 1);
                    FM.AS.doSetEngineState(shot.initiator, 0, 1);
                } else
                if(FM.AS.astateEngineStates[0] == 1)
                {
                    debuggunnery("Engine Module: Water Radiator Pierced..");
                    FM.AS.hitEngine(shot.initiator, 0, 1);
                    FM.AS.doSetEngineState(shot.initiator, 0, 2);
                }
                return;
            }
            if(s.startsWith("xpilot") || s.startsWith("xhead"))
            {
                byte byte0 = 0;
                int j;
                if(s.endsWith("a"))
                {
                    byte0 = 1;
                    j = s.charAt(6) - 49;
                } else
                if(s.endsWith("b"))
                {
                    byte0 = 2;
                    j = s.charAt(6) - 49;
                } else
                {
                    j = s.charAt(5) - 49;
                }
                hitFlesh(j, shot, byte0);
            }
        }
    }

    protected boolean cutFM(int i, int j, com.maddox.il2.engine.Actor actor)
    {
        if(i == 11 || i == 17 || i == 18 || i == 19)
        {
            FM.CT.bHasArrestorControl = false;
            hierMesh().chunkVisible("Wire_D0", false);
        }
        return super.cutFM(i, j, actor);
    }

    static void myResetYPRmodifier()
    {
        com.maddox.il2.objects.air.Aircraft.xyz[0] = com.maddox.il2.objects.air.Aircraft.xyz[1] = com.maddox.il2.objects.air.Aircraft.xyz[2] = com.maddox.il2.objects.air.Aircraft.ypr[0] = com.maddox.il2.objects.air.Aircraft.ypr[1] = com.maddox.il2.objects.air.Aircraft.ypr[2] = 0.0F;
    }

    static float smoothCvt(float f, float f1, float f2, float f3, float f4)
    {
        f = java.lang.Math.min(java.lang.Math.max(f, f1), f2);
        return f3 + (f4 - f3) * (-0.5F * (float)java.lang.Math.cos((double)((f - f1) / (f2 - f1)) * 3.1415926535897931D) + 0.5F);
    }


    private float flapps;
    public static boolean bChangedPit = false;
    protected int SecondProp;
    private float kangle;
    float rndgear[] = {
        0.0F, 0.0F, 0.0F
    };
    static float rndgearnull[] = {
        0.0F, 0.0F, 0.0F
    };

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.MB5.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "MB5");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/MB5/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        com.maddox.rts.Property.set(class1, "yearService", 1944F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1950F);
        com.maddox.rts.Property.set(class1, "originCountry", com.maddox.il2.objects.air.PaintScheme.countryBritain);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/MB5.fmd:MB5_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitMB5.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.5926F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 
            3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", 
            "_ExternalBomb03", "_ExternalBomb04"
        });
    }
}