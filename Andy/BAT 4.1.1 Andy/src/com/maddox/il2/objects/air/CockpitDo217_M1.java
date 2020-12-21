// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 12.11.2020 16:33:17
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   CockpitDo217_M1.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorDraw;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.Turret;
import com.maddox.rts.*;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapExt;
import java.io.PrintStream;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit, Do217, Do217_M1

public class CockpitDo217_M1 extends com.maddox.il2.objects.air.CockpitPilot
{
    class Interpolater extends com.maddox.il2.engine.InterpolateRef
    {

        public boolean tick()
        {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.forceCockpitDoor(1.0F);
            ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.cockpitDoorControl = 1.0F;
            if(cockpitDimControl)
            {
                if(setNew.dimPos < 1.0F)
                    setNew.dimPos = setOld.dimPos + 0.03F;
            } else
            if(setNew.dimPos > 0.0F)
                setNew.dimPos = setOld.dimPos - 0.03F;
            setNew.altimeter = fm.getAltitude();
            setNew.throttlel = (10F * setOld.throttlel + ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getControlThrottle()) / 11F;
            setNew.throttler = (10F * setOld.throttler + ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[1].getControlThrottle()) / 11F;
            setNew.vspeed = (499F * setOld.vspeed + fm.getVertSpeed()) / 500F;
            setNew.elevTrim = 0.85F * setOld.elevTrim + ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.getTrimElevatorControl() * 0.15F;
            setNew.rudderTrim = 0.85F * setOld.rudderTrim + ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.getTrimRudderControl() * 0.15F;
            setNew.ailTrim = 0.85F * setOld.ailTrim + ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.getTrimAileronControl() * 0.15F;
            setNew.propL = 0.85F * setOld.propL + ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getControlProp() * 0.15F;
            setNew.propR = 0.85F * setOld.propR + ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[1].getControlProp() * 0.15F;
            float f = prevFuel - ((com.maddox.il2.fm.FlightModelMain) (fm)).M.fuel;
            prevFuel = ((com.maddox.il2.fm.FlightModelMain) (fm)).M.fuel;
            f /= 0.72F;
            f /= com.maddox.rts.Time.tickLenFs();
            f *= 3600F;
            setNew.cons = 0.91F * setOld.cons + 0.09F * f;
            float f1 = waypointAzimuth();
            if(useRealisticNavigationInstruments())
            {
                setNew.waypointAzimuth.setDeg(f1 - 90F);
                setOld.waypointAzimuth.setDeg(f1 - 90F);
                setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
            } else
            {
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f1 - setOld.azimuth.getDeg(1.0F));
            }
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((com.maddox.il2.fm.FlightModelMain) (fm)).Or.azimut());
            setNew.beaconDirection = (10F * setOld.beaconDirection + getBeaconDirection()) / 11F;
            setNew.beaconRange = (10F * setOld.beaconRange + getBeaconRange()) / 11F;
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float dimPos;
        float altimeter;
        float throttlel;
        float throttler;
        float vspeed;
        float cons;
        float elevTrim;
        float rudderTrim;
        float ailTrim;
        float propL;
        float propR;
        com.maddox.il2.ai.AnglesFork radioCompassAzimuth;
        com.maddox.il2.ai.AnglesFork azimuth;
        com.maddox.il2.ai.AnglesFork waypointAzimuth;
        float beaconDirection;
        float beaconRange;

        private Variables()
        {
            dimPos = 0.0F;
            radioCompassAzimuth = new AnglesFork();
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
        }

        Variables(Variables variables)
        {
            this();
        }
    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            ((com.maddox.il2.objects.air.Do217)((com.maddox.il2.engine.Interpolate) (super.fm)).actor).bPitUnfocused = false;
            aircraft().hierMesh().chunkVisible("Interior1_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
            aircraft().hierMesh().chunkVisible("Head1_D0", false);
            aircraft().hierMesh().chunkVisible("Hmask1_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
            aircraft().hierMesh().chunkVisible("Hmask2_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
            aircraft().hierMesh().chunkVisible("Hmask3_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot4_D0", false);
            aircraft().hierMesh().chunkVisible("Hmask4_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot1_D1", false);
            aircraft().hierMesh().chunkVisible("Pilot2_D1", false);
            aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
            aircraft().hierMesh().chunkVisible("Pilot4_D1", false);
            aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
            aircraft().hierMesh().chunkVisible("Turret5B_D0", false);
            if(aircraft() instanceof com.maddox.il2.objects.air.Do217_M1)
            {
                super.mesh.chunkVisible("k2-Box", false);
                super.mesh.chunkVisible("k2-Cable", false);
                super.mesh.chunkVisible("k2-cushion", false);
                super.mesh.chunkVisible("k2-FuG203", false);
                super.mesh.chunkVisible("k2-gunsight", false);
            }
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        ((com.maddox.il2.objects.air.Do217)((com.maddox.il2.engine.Interpolate) (super.fm)).actor).bPitUnfocused = true;
        aircraft().hierMesh().chunkVisible("Interior1_D0", true);
        if(!((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.isPilotParatrooper(0))
        {
            aircraft().hierMesh().chunkVisible("Pilot1_D0", !((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.isPilotDead(0));
            aircraft().hierMesh().chunkVisible("Head1_D0", !((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.isPilotDead(0));
            aircraft().hierMesh().chunkVisible("Pilot1_D1", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.isPilotDead(0));
        }
        if(!((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.isPilotParatrooper(1))
        {
            aircraft().hierMesh().chunkVisible("Pilot2_D0", !((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.isPilotDead(1));
            aircraft().hierMesh().chunkVisible("Pilot2_D1", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.isPilotDead(1));
        }
        if(!((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.isPilotParatrooper(2))
        {
            aircraft().hierMesh().chunkVisible("Pilot3_D0", !((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.isPilotDead(2));
            aircraft().hierMesh().chunkVisible("Pilot3_D1", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.isPilotDead(2));
        }
        if(!((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.isPilotParatrooper(3))
        {
            aircraft().hierMesh().chunkVisible("Pilot4_D0", !((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.isPilotDead(3));
            aircraft().hierMesh().chunkVisible("Pilot4_D1", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.isPilotDead(3));
        }
        aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
        aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
        aircraft().hierMesh().chunkVisible("Turret5B_D0", true);
        if(aircraft() instanceof com.maddox.il2.objects.air.Do217_M1)
        {
            super.mesh.chunkVisible("k2-Box", true);
            super.mesh.chunkVisible("k2-Cable", true);
            super.mesh.chunkVisible("k2-cushion", true);
            super.mesh.chunkVisible("k2-FuG203", true);
            super.mesh.chunkVisible("k2-gunsight", true);
        }
        super.doFocusLeave();
    }

    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(20F);
    }

    public CockpitDo217_M1()
    {
        super("3DO/Cockpit/Do217K1/hier.him", "he111");
        setOld = new Variables(null);
        setNew = new Variables(null);
        pictAiler = 0.0F;
        pictElev = 0.0F;
        prevFuel = 0.0F;
        Pn = new Point3d();
        pictManf1 = 1.0F;
        pictManf2 = 1.0F;
        com.maddox.il2.engine.HookNamed hooknamed = new HookNamed(super.mesh, "LAMPHOOK1");
        com.maddox.il2.engine.Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(218F, 143F, 128F);
        light1.light.setEmit(0.0F, 0.0F);
        super.pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        super.cockpitNightMats = (new java.lang.String[] {
            "Peil1", "Peil2", "Instrument1", "Instrument2", "Instrument4", "Instrument5", "Instrument6", "Instrument7", "Instrument8", "Instrument9", 
            "Needles"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, com.maddox.rts.Time.current(), null);
        super.printCompassHeading = true;
        w = new Vector3f();
        buzzerFX = aircraft().newSound("models.buzzthru", false);
    }

    public void toggleDim()
    {
        super.cockpitDimControl = !super.cockpitDimControl;
    }

    public void reflectWorldToInstruments(float f)
    {
        super.printCompassHeading = true;
        super.mesh.chunkSetAngles("ZWheel", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.AileronControl) * 40F, 0.0F);
        super.mesh.chunkSetAngles("ZColumn", 0.0F, -7F - (pictElev = 0.85F * pictElev + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.ElevatorControl) * 10F, 0.0F);
        super.mesh.chunkSetAngles("PedalR", 0.0F, -((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder() * 10F, 0.0F);
        super.mesh.chunkSetAngles("PedalL", 0.0F, ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder() * 10F, 0.0F);
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[1] = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), -1F, 1.0F, 0.08F, -0.08F);
        super.mesh.chunkSetLocate("PedalRbar", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        com.maddox.il2.objects.air.Cockpit.xyz[1] = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), -1F, 1.0F, -0.08F, 0.08F);
        super.mesh.chunkSetLocate("PedalLbar", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        float f1 = interp(setNew.altimeter, setOld.altimeter, f);
        super.mesh.chunkSetAngles("z_HeightKm", 0.0F, cvt(f1, 0.0F, 10000F, 0.0F, 257F), 0.0F);
        super.mesh.chunkSetAngles("z_Height", 0.0F, cvt(f1, 0.0F, 20000F, 0.0F, 7200F), 0.0F);
        super.mesh.chunkSetAngles("Z_Alt1", 0.0F, cvt(f1, 0.0F, 6000F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("Z_Alt1Set", 0.0F, (((com.maddox.il2.objects.air.Do217)aircraft()).fSightCurAltitude * 360F) / 6000F, 0.0F);
        Pn.set(((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc);
        Pn.z = (double)f1 - com.maddox.il2.engine.Engine.cur.land.HQ(((com.maddox.JGP.Tuple3d) (Pn)).x, ((com.maddox.JGP.Tuple3d) (Pn)).y);
        super.mesh.chunkSetAngles("z_AFN101", 0.0F, cvt((float)((com.maddox.JGP.Tuple3d) (Pn)).z, 0.0F, 750F, 0.0F, 223F), 0.0F);
        super.mesh.chunkSetAngles("z_Speed", 0.0F, floatindex(cvt(com.maddox.il2.fm.Pitot.Indicator((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 50F, 800F, 0.0F, 15F), speedometerScale), 0.0F);
        super.mesh.chunkSetAngles("z_VelX", 0.0F, floatindex(cvt(super.fm.getSpeedKMH(), 50F, 800F, 0.0F, 15F), speedometerScale), 0.0F);
        float f2 = ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getRPM();
        float f3 = ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[1].getRPM();
        super.mesh.chunkSetAngles("z_RPM1", 0.0F, cvt(f2, 600F, 3600F, -172F, 151F), 0.0F);
        super.mesh.chunkSetAngles("z_RPM2", 0.0F, cvt(f3, 600F, 3600F, -172F, 151F), 0.0F);
        super.mesh.chunkSetAngles("z_ATA1", 0.0F, pictManf1 = 0.9F * pictManf1 + 0.1F * cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, -162.5F, 162.5F), 0.0F);
        super.mesh.chunkSetAngles("z_ATA2", 0.0F, pictManf2 = 0.9F * pictManf2 + 0.1F * cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, -162.5F, 162.5F), 0.0F);
        super.mesh.chunkSetAngles("Z_Hours", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        super.mesh.chunkSetAngles("Z_Minutes", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("z_ThrottleL", 0.0F, -25F * interp(setNew.throttlel, setOld.throttlel, f), 0.0F);
        super.mesh.chunkSetAngles("z_ThrottleR", 0.0F, -25F * interp(setNew.throttler, setOld.throttler, f), 0.0F);
        float f4 = setNew.vspeed * 9F;
        super.mesh.chunkSetAngles("Z_Variometer", 0.0F, f4, 0.0F);
        w.set(super.fm.getW());
        ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("Z_Stick", 0.0F, cvt(((com.maddox.JGP.Tuple3f) (w)).z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        float f5 = cvt(getBall(6D), -6F, 6F, -0.02F, 0.02F);
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[0] = -f5;
        super.mesh.chunkSetLocate("Z_Ball", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetLocate("Z_Ball01", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetLocate("Z_Ball02", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetAngles("z_AH1", 0.0F, -((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.getKren(), 0.0F);
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[1] = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.getTangage(), -45F, 45F, 0.011F, -0.015F);
        super.mesh.chunkSetLocate("Z_Horizon", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetAngles("z_ElevTrim", 0.0F, -cvt(interp(setNew.elevTrim, setOld.elevTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
        super.mesh.chunkSetAngles("z_RudderTrim", 0.0F, cvt(interp(setNew.rudderTrim, setOld.rudderTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
        super.mesh.chunkSetAngles("z_AileronTrim", 0.0F, cvt(interp(setNew.ailTrim, setOld.ailTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
        float f6 = setNew.cons;
        float f7 = (f6 * f2) / (f2 + f3);
        float f8 = (f6 * f3) / (f2 + f3);
        super.mesh.chunkSetAngles("z_FuelCons1", 0.0F, cvt(f7, 0.0F, 600F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("z_FuelCons2", 0.0F, cvt(f8, 0.0F, 600F, 0.0F, 360F), 0.0F);
        float f9 = ((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel / 0.72F;
        super.mesh.chunkSetAngles("z_Fuel3", 0.0F, cvt(f9, 0.0F, 1100F, 0.0F, 69F), 0.0F);
        super.mesh.chunkSetAngles("z_Fuel1", 0.0F, cvt(f9, 1100F, 2670F, 0.0F, 84F), 0.0F);
        super.mesh.chunkSetAngles("z_Fuel2", 0.0F, cvt(f9, 1100F, 2670F, 0.0F, 84F), 0.0F);
        super.mesh.chunkSetAngles("z_OilPres1", 0.0F, cvt(1.0F + 0.05F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getReadyness(), 0.0F, 15F, 0.0F, -135F), 0.0F);
        super.mesh.chunkSetAngles("z_OilPres2", 0.0F, cvt(1.0F + 0.05F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[1].tOilOut * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[1].getReadyness(), 0.0F, 15F, 0.0F, -135F), 0.0F);
        super.mesh.chunkSetAngles("z_FuelPres1", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 3F, 0.0F, 135F), 0.0F);
        super.mesh.chunkSetAngles("z_FuelPres2", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 3F, 0.0F, 135F), 0.0F);
        super.mesh.chunkSetAngles("Z_TempCylL", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 75F), 0.0F);
        super.mesh.chunkSetAngles("Z_TempCylR", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[1].tOilOut, 0.0F, 160F, 0.0F, 75F), 0.0F);
        super.mesh.chunkSetAngles("z_OAT", 0.0F, floatindex(cvt(com.maddox.il2.fm.Atmosphere.temperature((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc)).z) - 273.15F, -60F, 40F, 0.0F, 10F), OATscale), 0.0F);
        super.mesh.chunkSetAngles("z_TrimIndicator", 0.0F, cvt(interp(setNew.elevTrim, setOld.elevTrim, f), -0.5F, 0.5F, 40F, -40F), 0.0F);
        super.mesh.chunkSetAngles("PropPitchLeverL", 0.0F, -70F * (1.0F - interp(setNew.propL, setOld.propL, f)), 0.0F);
        super.mesh.chunkSetAngles("PropPitchLeverR", 0.0F, -70F * (1.0F - interp(setNew.propR, setOld.propR, f)), 0.0F);
        super.mesh.chunkVisible("RWL_Ein", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear() < 0.01F || !((com.maddox.il2.fm.FlightModelMain) (super.fm)).Gears.lgear);
        super.mesh.chunkVisible("RWL_Aus", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear() > 0.99F && ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Gears.lgear);
        super.mesh.chunkVisible("RWR_Ein", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear() < 0.01F || !((com.maddox.il2.fm.FlightModelMain) (super.fm)).Gears.rgear);
        super.mesh.chunkVisible("RWR_Aus", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear() > 0.99F && ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Gears.rgear);
        super.mesh.chunkVisible("LK_Ein", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getFlap() < 0.1F);
        super.mesh.chunkVisible("LK_Start", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getFlap() > 0.1F && ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getFlap() < 0.5F);
        super.mesh.chunkVisible("LK_Aus", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getFlap() > 0.5F);
        super.mesh.chunkSetAngles("Z_Turret1A", 0.0F, -super.fm.turret[0].tu[0], 0.0F);
        super.mesh.chunkSetAngles("Z_Turret1B", 0.0F, super.fm.turret[0].tu[1], 0.0F);
        super.mesh.chunkSetAngles("Z_Turret3A", 0.0F, -super.fm.turret[2].tu[0], 0.0F);
        super.mesh.chunkSetAngles("Z_Turret3B", 0.0F, super.fm.turret[2].tu[1], 0.0F);
        super.mesh.chunkSetAngles("Z_Turret5A", 0.0F, -super.fm.turret[4].tu[0], 0.0F);
        super.mesh.chunkSetAngles("Z_Turret5B", 0.0F, super.fm.turret[4].tu[1], 0.0F);
        if(useRealisticNavigationInstruments())
        {
            super.mesh.chunkSetAngles("Z_Compass1", -setNew.waypointAzimuth.getDeg(f * 0.1F) - 90F, 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Compass2", setNew.azimuth.getDeg(f) - setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Compass3", setNew.azimuth.getDeg(f) - setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Compass4", -setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Compass5", setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Compass7", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Compass8", setNew.waypointAzimuth.getDeg(f * 0.1F) + 90F, 0.0F, 0.0F);
            super.mesh.chunkSetAngles("zNavP", -(setNew.waypointAzimuth.getDeg(f * 0.1F) - setNew.azimuth.getDeg(f)), 0.0F, 0.0F);
            float f10 = setNew.beaconDirection;
            float f11 = setNew.beaconRange;
            super.mesh.chunkSetAngles("z_AFN2a", 0.0F, cvt(f10, -40F, 40F, -32F, 32F), 0.0F);
            super.mesh.chunkSetAngles("z_AFN2b", 0.0F, 0.0F, cvt(f11, 0.0F, 1.0F, -20F, 15F));
            super.mesh.chunkVisible("AFN1_RED", isOnBlindLandingMarker());
        } else
        {
            super.mesh.chunkSetAngles("Z_Compass1", -setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Compass2", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Compass3", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Compass4", -setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Compass5", 0.0F, 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Compass7", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Compass8", setNew.waypointAzimuth.getDeg(f * 0.1F) + 90F, 0.0F, 0.0F);
            super.mesh.chunkSetAngles("zNavP", -setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        }
    }

    protected void mydebugcockpit(java.lang.String s)
    {
        java.lang.System.out.println(s);
    }

    public void reflectCockpitState()
    {
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 4) != 0)
        {
            super.mesh.chunkVisible("XGlassHoles1", true);
            super.mesh.chunkVisible("XGlassHoles3", true);
            super.mesh.chunkVisible("Alt-dmg", true);
            super.mesh.chunkVisible("z_Alt", false);
            super.mesh.chunkVisible("Z_Alt1Set", false);
            super.mesh.chunkVisible("RPM-dmg", true);
            super.mesh.chunkVisible("z_RPM1", false);
            super.mesh.chunkVisible("z_RPM2", false);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 8) != 0)
        {
            super.mesh.chunkVisible("XGlassHoles7", true);
            super.mesh.chunkVisible("XGlassHoles4", true);
            super.mesh.chunkVisible("XGlassHoles2", true);
            super.mesh.chunkVisible("ATA-dmg", true);
            super.mesh.chunkVisible("z_ATA", false);
            super.mesh.chunkVisible("Speed-dmg", true);
            super.mesh.chunkVisible("z_Speed", false);
            super.mesh.chunkVisible("FuelCons2-dmg", true);
            super.mesh.chunkVisible("z_FuelCons2", false);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) != 0)
        {
            super.mesh.chunkVisible("XGlassHoles5", true);
            super.mesh.chunkVisible("XGlassHoles3", true);
            super.mesh.chunkVisible("OAT-dmg", true);
            super.mesh.chunkVisible("z_OAT", false);
            super.mesh.chunkVisible("Variometer-dmg", true);
            super.mesh.chunkVisible("z_Variometer", false);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) != 0)
            super.mesh.chunkVisible("XGlassHoles1", true);
        super.mesh.chunkVisible("XGlassHoles6", true);
        super.mesh.chunkVisible("XGlassHoles4", true);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 1) != 0)
            super.mesh.chunkVisible("XGlassHoles6", true);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) != 0)
        {
            super.mesh.chunkVisible("XGlassHoles7", true);
            super.mesh.chunkVisible("XGlassHoles2", true);
            super.mesh.chunkVisible("Oxy-dmg", true);
            super.mesh.chunkVisible("z_Oxy", false);
            super.mesh.chunkVisible("Press-dmg", true);
            super.mesh.chunkVisible("z_OilPres1", false);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x80) != 0)
        {
            super.mesh.chunkVisible("XGlassHoles5", true);
            super.mesh.chunkVisible("XGlassHoles2", true);
        }
    }

    public void toggleLight()
    {
        super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
        {
            light1.light.setEmit(0.0032F, 7.2F);
            setNightMats(true);
        } else
        {
            light1.light.setEmit(0.0F, 0.0F);
            setNightMats(false);
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private com.maddox.il2.engine.LightPointActor light1;
    private float pictAiler;
    private float pictElev;
    private float prevFuel;
    protected com.maddox.sound.SoundFX buzzerFX;
    private static final float speedometerScale[] = {
        0.0F, 17F, 41F, 66F, 93F, 119F, 139F, 164F, 188F, 214F, 
        239F, 266F, 292F, 317F, 341F, 372F
    };
    private static final float OATscale[] = {
        0.0F, 7F, 17F, 27F, 37F, 47F, 56F, 65F, 72F, 80F, 
        85F
    };
    private com.maddox.JGP.Point3d Pn;
    private float pictManf1;
    private float pictManf2;
    public com.maddox.JGP.Vector3f w;




    static 
    {
        com.maddox.rts.Property.set(com.maddox.rts.CLASS.THIS(), "normZNs", new float[] {
            0.35F, 0.35F, 1.0F, 0.95F
        });
    }





}