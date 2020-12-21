// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 24.11.2020 11:04:11
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   CockpitElectra10.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Way;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Autopilotage;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit

public class CockpitElectra10 extends com.maddox.il2.objects.air.CockpitPilot
{
    class Interpolater extends com.maddox.il2.engine.InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle1 = 0.85F * setOld.throttle1 + ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getControlThrottle() * 0.15F;
                setNew.throttle2 = 0.85F * setOld.throttle2 + ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[1].getControlThrottle() * 0.15F;
                setNew.prop1 = 0.85F * setOld.prop1 + ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getControlProp() * 0.15F;
                setNew.prop2 = 0.85F * setOld.prop2 + ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[1].getControlProp() * 0.15F;
                setNew.altimeter = fm.getAltitude();
                float f = waypointAzimuth();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((com.maddox.il2.fm.FlightModelMain) (fm)).Or.azimut());
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), f);
                setNew.waypointDeviation.setDeg(setOld.waypointDeviation.getDeg(0.1F), (f - setOld.azimuth.getDeg(1.0F)) + com.maddox.il2.ai.World.Rnd().nextFloat(-10F, 10F));
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float throttle1;
        float throttle2;
        float prop1;
        float prop2;
        float altimeter;
        com.maddox.il2.ai.AnglesFork azimuth;
        com.maddox.il2.ai.AnglesFork waypointAzimuth;
        float vspeed;
        com.maddox.il2.ai.AnglesFork waypointDeviation;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            waypointDeviation = new AnglesFork();
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
            aircraft().hierMesh().chunkVisible("CF_D0", false);
            aircraft().hierMesh().chunkVisible("Allglass", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("CF_D0", true);
        aircraft().hierMesh().chunkVisible("Allglass", true);
        super.doFocusLeave();
    }

    protected float waypointAzimuth()
    {
        com.maddox.il2.ai.WayPoint waypoint = ((com.maddox.il2.fm.FlightModelMain) (super.fm)).AP.way.curr();
        if(waypoint == null)
            return 0.0F;
        waypoint.getP(tmpP);
        tmpV.sub(tmpP, ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc);
        float f;
        for(f = (float)(57.295779513082323D * java.lang.Math.atan2(-((com.maddox.JGP.Tuple3d) (tmpV)).y, ((com.maddox.JGP.Tuple3d) (tmpV)).x)); f <= -180F; f += 180F);
        for(; f > 180F; f -= 180F);
        return f;
    }

    public CockpitElectra10()
    {
        super("3DO/Cockpit/Electra10P/heir.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictFlap = 0.0F;
        pictGear = 0.0F;
        pictManf1 = 1.0F;
        pictManf2 = 1.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        super.cockpitNightMats = (new java.lang.String[] {
            "texture01_dmg", "texture01", "texture02_dmg", "texture02", "texture03_dmg", "texture03", "texture04_dmg", "texture04", "texture05_dmg", "texture05", 
            "texture06_dmg", "texture06", "texture21_dmg", "texture21", "texture25"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, com.maddox.rts.Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        super.mesh.chunkSetAngles("Z_Column", 0.0F, -(pictElev = 0.85F * pictElev + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.ElevatorControl) * 8F, 0.0F);
        super.mesh.chunkSetAngles("Z_AroneL", 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.AileronControl) * 68F, 0.0F);
        super.mesh.chunkSetAngles("Z_AroneR", 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.AileronControl) * 68F, 0.0F);
        super.mesh.chunkSetAngles("Z_RightPedal", 0.0F, -10F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 10F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("Z_RPedalStep", 0.0F, -10F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("Z_LPedalStep", 0.0F, 10F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("Z_PedalWireR", 0.0F, 0.0F, -10F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder());
        super.mesh.chunkSetAngles("Z_PedalWireL", 0.0F, 0.0F, 10F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder());
        super.mesh.chunkSetAngles("zFlaps1", 0.0F, 38F * (pictFlap = 0.75F * pictFlap + 0.25F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.FlapsControl), 0.0F);
        super.mesh.chunkSetAngles("zOilFlap1", 0.0F, -35F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlRadiator(), 0.0F);
        super.mesh.chunkSetAngles("zOilFlap2", 0.0F, -35F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[1].getControlRadiator(), 0.0F);
        super.mesh.chunkSetAngles("zMix1", 0.0F, -45.8F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlMix(), 0.0F);
        super.mesh.chunkSetAngles("zMix2", 0.0F, -45.8F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[1].getControlMix(), 0.0F);
        super.mesh.chunkSetAngles("zPitch1", 0.0F, -54F * interp(setNew.prop1, setOld.prop1, f), 0.0F);
        super.mesh.chunkSetAngles("zPitch2", 0.0F, -54F * interp(setNew.prop2, setOld.prop2, f), 0.0F);
        super.mesh.chunkSetAngles("zThrottle1", 0.0F, -49F * interp(setNew.throttle1, setOld.throttle1, f), 0.0F);
        super.mesh.chunkSetAngles("zThrottle2", 0.0F, -49F * interp(setNew.throttle2, setOld.throttle2, f), 0.0F);
        super.mesh.chunkSetAngles("zCompressor1", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlCompressor(), 0.0F, 1.0F, 0.0F, -50F), 0.0F);
        super.mesh.chunkSetAngles("zCompressor2", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[1].getControlCompressor(), 0.0F, 1.0F, 0.0F, -50F), 0.0F);
        if(((com.maddox.il2.fm.FlightModelMain) (super.fm)).Gears.cgear)
        {
            resetYPRmodifier();
            com.maddox.il2.objects.air.Cockpit.xyz[1] = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear(), 0.0F, 1.0F, 0.0F, 0.0135F);
            super.mesh.chunkSetLocate("Z_Gearc1", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        }
        if(((com.maddox.il2.fm.FlightModelMain) (super.fm)).Gears.lgear)
        {
            resetYPRmodifier();
            com.maddox.il2.objects.air.Cockpit.xyz[1] = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear(), 0.0F, 1.0F, 0.0F, 0.0135F);
            super.mesh.chunkSetLocate("Z_GearL1", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        }
        if(((com.maddox.il2.fm.FlightModelMain) (super.fm)).Gears.rgear)
        {
            resetYPRmodifier();
            com.maddox.il2.objects.air.Cockpit.xyz[1] = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear(), 0.0F, 1.0F, 0.0F, 0.0135F);
            super.mesh.chunkSetLocate("Z_GearR1", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        }
        super.mesh.chunkSetAngles("zFlaps2", 0.0F, 90F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getFlap(), 0.0F);
        super.mesh.chunkSetAngles("zHour", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        super.mesh.chunkSetAngles("zMinute", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("zSecond", 0.0F, cvt(((com.maddox.il2.ai.World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("zAH1", 0.0F, -((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.getKren(), 0.0F);
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[2] = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.getTangage(), -45F, 45F, 0.0365F, -0.0365F);
        super.mesh.chunkSetLocate("zAH2", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) == 0)
            super.mesh.chunkSetAngles("Z_Climb1", 0.0F, floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
        w.set(super.fm.getW());
        ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("zTurnBank", 0.0F, cvt(((com.maddox.JGP.Tuple3f) (w)).z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        super.mesh.chunkSetAngles("zBall", 0.0F, cvt(getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
        super.mesh.chunkSetAngles("zSpeed", 0.0F, floatindex(cvt(com.maddox.il2.fm.Pitot.Indicator((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
        super.mesh.chunkSetAngles("zAlt1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        super.mesh.chunkSetAngles("zAlt2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        super.mesh.chunkSetAngles("zCompass1", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
        super.mesh.chunkSetAngles("zCompass2", 0.0F, -0.5F * setNew.azimuth.getDeg(f), 0.0F);
        super.mesh.chunkSetAngles("zCompass3", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) == 0)
        {
            super.mesh.chunkSetAngles("zMagnetic", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
            super.mesh.chunkSetAngles("zNavP", 0.0F, setNew.waypointAzimuth.getDeg(f), 0.0F);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) == 0)
        {
            super.mesh.chunkSetAngles("zRPM1", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            super.mesh.chunkSetAngles("zRPM2", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            super.mesh.chunkSetAngles("Z_Pres1", 0.0F, pictManf1 = 0.9F * pictManf1 + 0.1F * cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
            super.mesh.chunkSetAngles("Z_Pres2", 0.0F, pictManf2 = 0.9F * pictManf2 + 0.1F * cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[1].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
        }
        super.mesh.chunkSetAngles("Z_FuelPres1", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        super.mesh.chunkSetAngles("Z_FuelPres2", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        super.mesh.chunkSetAngles("Z_Carbair1", 0.0F, -cvt(com.maddox.il2.fm.Atmosphere.temperature((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc)).z) - 273.15F, -50F, 150F, -25F, 75F), 0.0F);
        super.mesh.chunkSetAngles("Z_Carbair2", 0.0F, cvt(com.maddox.il2.fm.Atmosphere.temperature((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc)).z) - 273.15F, -50F, 150F, -25F, 75F), 0.0F);
        super.mesh.chunkSetAngles("Z_Temp1", 0.0F, -cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 100F), 0.0F);
        super.mesh.chunkSetAngles("Z_Temp2", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[1].tWaterOut, 0.0F, 350F, 0.0F, 100F), 0.0F);
        super.mesh.chunkSetAngles("zOilTemp1", 0.0F, -cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut, -50F, 150F, -25F, 75F), 0.0F);
        super.mesh.chunkSetAngles("zOilTemp2", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[1].tOilOut, -50F, 150F, -25F, 75F), 0.0F);
        super.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, cvt(1.0F + 0.05F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[1].tOilOut * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[1].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        super.mesh.chunkSetAngles("Z_Oilpres2", 0.0F, cvt(1.0F + 0.05F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        super.mesh.chunkSetAngles("Z_Brkpres1", 0.0F, 118F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getBrake(), 0.0F);
        super.mesh.chunkSetAngles("zFuel1", 0.0F, -cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 0.0F, 2332F, 0.0F, 95F), 0.0F);
        super.mesh.chunkSetAngles("zFuel2", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 0.0F, 2332F, 0.0F, 95F), 0.0F);
        super.mesh.chunkSetAngles("zFuel3", 0.0F, -cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 0.0F, 1916F, 0.0F, 90.5F), 0.0F);
        super.mesh.chunkSetAngles("zFuel4", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 0.0F, 1916F, 0.0F, 90.5F), 0.0F);
        super.mesh.chunkSetAngles("zFuel5", 0.0F, -cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 1916F, 3084F, 0.0F, 102.5F), 0.0F);
        super.mesh.chunkSetAngles("zFuel6", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 1916F, 3084F, 0.0F, 102.5F), 0.0F);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) == 0)
            super.mesh.chunkSetAngles("zFreeAir", 0.0F, cvt(com.maddox.il2.fm.Atmosphere.temperature((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc)).z) - 273.15F, -70F, 150F, -26.6F, 57F), 0.0F);
        super.mesh.chunkSetAngles("zHydPres", 0.0F, ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Gears.bIsHydroOperable ? 165.5F : 0.0F, 0.0F);
        float f1 = 0.5F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getRPM() + 0.5F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[1].getRPM();
        f1 = 2.5F * (float)java.lang.Math.sqrt(java.lang.Math.sqrt(java.lang.Math.sqrt(java.lang.Math.sqrt(f1))));
        super.mesh.chunkSetAngles("zSuction", 0.0F, cvt(f1, 0.0F, 10F, 0.0F, 297F), 0.0F);
        super.mesh.chunkVisible("Z_GearRed1", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear() > 0.01F && ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear() < 0.99F);
        super.mesh.chunkVisible("Z_GearLGreen1", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear() > 0.99F && ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Gears.lgear);
    }

    public void reflectCockpitState()
    {
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x80) == 0);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 2) != 0)
        {
            super.mesh.chunkVisible("Pricel_D0", false);
            super.mesh.chunkVisible("Pricel_D1", true);
            super.mesh.chunkVisible("Z_Z_RETICLE", false);
            super.mesh.chunkVisible("Z_Z_MASK", false);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 1) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage", true);
            super.mesh.chunkVisible("XGlassDamage1", true);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) != 0)
        {
            super.mesh.chunkVisible("Panel_D0", false);
            super.mesh.chunkVisible("Panel_D1", true);
            super.mesh.chunkVisible("zCompass3", false);
            super.mesh.chunkVisible("Z_FuelPres1", false);
            super.mesh.chunkVisible("Z_FuelPres2", false);
            super.mesh.chunkVisible("Z_Oilpres1", false);
            super.mesh.chunkVisible("Z_Oilpres2", false);
            super.mesh.chunkVisible("zOilTemp1", false);
            super.mesh.chunkVisible("zOilTemp2", false);
            super.mesh.chunkVisible("Z_Brkpres1", false);
            super.mesh.chunkVisible("zHydPres", false);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 4) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage", true);
            super.mesh.chunkVisible("XHullDamage2", true);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 8) != 0)
            super.mesh.chunkVisible("XGlassDamage2", true);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) != 0)
            super.mesh.chunkVisible("XHullDamage1", true);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) != 0)
            super.mesh.chunkVisible("XGlassDamage2", true);
        retoggleLight();
    }

    public void toggleLight()
    {
        super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private void retoggleLight()
    {
        if(super.cockpitLightControl)
        {
            setNightMats(false);
            setNightMats(true);
        } else
        {
            setNightMats(true);
            setNightMats(false);
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public com.maddox.JGP.Vector3f w;
    private float pictAiler;
    private float pictElev;
    private float pictFlap;
    private float pictGear;
    private float pictManf1;
    private float pictManf2;
    private static final float speedometerScale[] = {
        0.0F, 2.5F, 59F, 126F, 155.5F, 223.5F, 240F, 254.5F, 271F, 285F, 
        296.5F, 308.5F, 324F, 338.5F
    };
    private static final float variometerScale[] = {
        -180F, -157F, -130F, -108F, -84F, -46.5F, 0.0F, 46.5F, 84F, 108F, 
        130F, 157F, 180F
    };
    private com.maddox.JGP.Point3d tmpP;
    private com.maddox.JGP.Vector3d tmpV;


    static 
    {
        com.maddox.rts.Property.set(com.maddox.rts.CLASS.THIS(), "normZNs", new float[] {
            0.35F, 0.55F, 0.85F, 1.27F
        });
    }




}