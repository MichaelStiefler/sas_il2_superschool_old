// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 03/11/2015 04:00:41 p.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Skyhawk_New.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, TypeFighter, TypeSupersonic, PaintScheme, 
//            AircraftLH, Aircraft, EjectionSeat

public class Skyhawk_New extends Scheme1
    implements TypeFighter, TypeSupersonic
{

    public Skyhawk_New()
    {
        critSpeed = 0.0F;
        lTimeNextEject = 0L;
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
        overrideBailout = false;
        ejectComplete = false;
        lTimeNextEject = 0L;
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

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -117F);
        float f2 = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -90F);
        float f3 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -45F);
        float f4 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -90F);
        float f5 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -45F);
        float f6 = Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, -90F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, f5, 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, -f3, 0.0F);
        hiermesh.chunkSetAngles("GearC8_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, f4, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -f4, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", f6, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", -f6, 0.0F, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        float f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[2], 0.0F, 0.19075F, 0.0F, 1.0F);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.19075F * f;
        hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -55F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -55F * f, 0.0F);
        hierMesh().chunkSetAngles("SlatL_D0", -1.5F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("SlatR_D0", 1.5F * f, 0.0F, 0.0F);
    }

    protected void moveFan(float f1)
    {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.endsWith("p1"))
                {
                    getEnergyPastArmor(13.350000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                } else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(8.77F, shot);
                else
                if(s.endsWith("g1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(40F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 2);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                }
            } else
            if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch(i)
                {
                case 1: // '\001'
                case 2: // '\002'
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(1.1F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3: // '\003'
                case 4: // '\004'
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;
                }
            } else
            if(s.startsWith("xxeng1"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("bloc"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersRatio() * 20F)
                {
                    ((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 24000F)
                {
                    ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                s.endsWith("exht");
            } else
            if(s.startsWith("xxmgun0"))
            {
                int j = s.charAt(7) - 49;
                if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Machine Gun (" + j + ") Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(0, j);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else
            if(s.startsWith("xxtank"))
            {
                int k = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(((FlightModelMain) (super.FM)).AS.astateTankStates[k] == 0)
                    {
                        debuggunnery("Fuel Tank (" + k + "): Pierced..");
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, k, 1);
                        ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, k, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, k, 2);
                        debuggunnery("Fuel Tank (" + k + "): Hit..");
                    }
                }
            } else
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
            } else
            if(s.startsWith("xxhyd"))
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
            else
            if(s.startsWith("xxpnm"))
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 1);
        } else
        {
            if(s.startsWith("xcockpit"))
            {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
                getEnergyPastArmor(0.05F, shot);
            }
            if(s.startsWith("xcf"))
                hitChunk("CF", shot);
            else
            if(s.startsWith("xnose"))
                hitChunk("Nose", shot);
            else
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
                hitChunk("Rudder1", shot);
            else
            if(s.startsWith("xstab"))
            {
                if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                    hitChunk("StabL", shot);
                if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
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
                if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                    hitChunk("WingLMid", shot);
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
                if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
                {
                    debuggunnery("Hydro System: Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 0);
                }
                if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
                {
                    debuggunnery("Undercarriage: Stuck..");
                    ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
                }
            } else
            if(s.startsWith("xpilot") || s.startsWith("xhead"))
            {
                byte byte0 = 0;
                int l;
                if(s.endsWith("a"))
                {
                    byte0 = 1;
                    l = s.charAt(6) - 49;
                } else
                if(s.endsWith("b"))
                {
                    byte0 = 2;
                    l = s.charAt(6) - 49;
                } else
                {
                    l = s.charAt(5) - 49;
                }
                hitFlesh(l, shot, byte0);
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19: // '\023'
            ((FlightModelMain) (super.FM)).EI.engines[0].setEngineDies(actor);
            return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public float getMachForAlt(float f)
    {
        f /= 1000F;
        int i = 0;
        for(i = 0; i < TypeSupersonic.fMachAltX.length; i++)
            if(TypeSupersonic.fMachAltX[i] > f)
                break;

        if(i == 0)
        {
            return TypeSupersonic.fMachAltY[0];
        } else
        {
            float f1 = TypeSupersonic.fMachAltY[i - 1];
            float f2 = TypeSupersonic.fMachAltY[i] - f1;
            float f3 = TypeSupersonic.fMachAltX[i - 1];
            float f4 = TypeSupersonic.fMachAltX[i] - f3;
            float f5 = (f - f3) / f4;
            return f1 + f2 * f5;
        }
    }

    public float getAirPressure(float f)
    {
        float f1 = 1.0F - (0.0065F * f) / 288.15F;
        float f2 = 5.255781F;
        return 101325F * (float)Math.pow(f1, f2);
    }

    public float getAirPressureFactor(float f)
    {
        return getAirPressure(f) / 101325F;
    }

    public float getAirDensity(float f)
    {
        return (getAirPressure(f) * 0.0289644F) / (8.31447F * (288.15F - 0.0065F * f));
    }

    public float getAirDensityFactor(float f)
    {
        return getAirDensity(f) / 1.225F;
    }

    public void soundbarier()
    {
    }

    public float calculateMach()
    {
        return super.FM.getSpeedKMH() / getMachForAlt(super.FM.getAltitude());
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("FuBrake01_D0", -45F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("FuBrake02_D0", 45F * f, 0.0F, 0.0F);
    }

    private void bailout()
    {
        if(overrideBailout)
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 0 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep < 2)
            {
                if(((FlightModelMain) (super.FM)).CT.cockpitDoorControl > 0.5F && ((FlightModelMain) (super.FM)).CT.getCockpitDoor() > 0.5F)
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
                else
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 2;
            } else
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 2 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 3)
            {
                switch(((FlightModelMain) (super.FM)).AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(((FlightModelMain) (super.FM)).CT.cockpitDoorControl < 0.5F)
                        doRemoveBlisters();
                    break;

                case 3: // '\003'
                    lTimeNextEject = Time.current() + 1000L;
                    break;
                }
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(20, ((FlightModelMain) (super.FM)).AS.astateBailoutStep, 1, null);
                ((FlightModelMain) (super.FM)).AS.astateBailoutStep = (byte)(((FlightModelMain) (super.FM)).AS.astateBailoutStep + 1);
                if(((FlightModelMain) (super.FM)).AS.astateBailoutStep == 4)
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
            } else
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 11 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 19)
            {
                byte byte0 = ((FlightModelMain) (super.FM)).AS.astateBailoutStep;
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(20, ((FlightModelMain) (super.FM)).AS.astateBailoutStep, 1, null);
                ((FlightModelMain) (super.FM)).AS.astateBailoutStep = (byte)(((FlightModelMain) (super.FM)).AS.astateBailoutStep + 1);
                if((super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_maneuver() != 44)
                {
                    World.cur();
                    if(((FlightModelMain) (super.FM)).AS.actor != World.getPlayerAircraft())
                        ((Maneuver)super.FM).set_maneuver(44);
                }
                if(((FlightModelMain) (super.FM)).AS.astatePilotStates[byte0 - 11] < 99)
                {
                    if(byte0 == 11)
                    {
                        doRemoveBodyFromPlane(2);
                        doEjectCatapultStudent();
                        lTimeNextEject = Time.current() + 1000L;
                    } else
                    if(byte0 == 12)
                    {
                        doRemoveBodyFromPlane(1);
                        doEjectCatapultInstructor();
                        ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 51;
                        super.FM.setTakenMortalDamage(true, null);
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
                        ((FlightModelMain) (super.FM)).AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        ((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                    ((FlightModelMain) (super.FM)).AS.astatePilotStates[byte0 - 11] = 99;
                } else
                {
                    EventLog.type("astatePilotStates[" + (byte0 - 11) + "]=" + ((FlightModelMain) (super.FM)).AS.astatePilotStates[byte0 - 11]);
                }
            }
    }

    public void doEjectCatapultStudent()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat02");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(1, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat2_D0", false);
    }

    public void doEjectCatapultInstructor()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(1, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat1_D0", false);
    }

    private final void doRemoveBlisters()
    {
        for(int i = 1; i < 10; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && ((FlightModelMain) (super.FM)).AS.getPilotHealth(i - 1) > 0.0F)
            {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(((FlightModelMain) (super.FM)).Vwld);
                wreckage.setSpeed(vector3d);
            }

    }

    public void update(float f)
    {
        if(Config.isUSE_RENDER() && ((FlightModelMain) (super.FM)).AS.isMaster())
            if(((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() > 0.8F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 6)
            {
                if(((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() > 0.95F)
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 3);
                else
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 2);
            } else
            {
                ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 0);
            }
        if(super.FM.getSpeedKMH() > 310F);
        if((double)calculateMach() < 0.78000000000000003D)
            critSpeed = 0.0F;
        if((((FlightModelMain) (super.FM)).AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && super.FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            ((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = false;
            if(Time.current() > lTimeNextEject)
                bailout();
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
    }

    private float critSpeed;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private long lTimeNextEject;
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
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;

    static 
    {
        Class class1 = CLASS.THIS();
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
