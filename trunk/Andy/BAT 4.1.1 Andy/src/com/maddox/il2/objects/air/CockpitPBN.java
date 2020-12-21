// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 01.12.2020 09:28:21
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   CockpitPBN.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit, Aircraft

public class CockpitPBN extends com.maddox.il2.objects.air.CockpitPilot
{
    private class Variables
    {

        float throttle1;
        float throttle2;
        float prop1;
        float prop2;
        float altimeter;
        float elevTrim;
        float rudderTrim;
        com.maddox.il2.ai.AnglesFork azimuth;
        com.maddox.il2.ai.AnglesFork waypointAzimuth;
        com.maddox.il2.ai.AnglesFork radioCompassAzimuth;
        float vspeed;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends com.maddox.il2.engine.InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                if(bNeedSetUp)
                {
                    reflectPlaneMats();
                    bNeedSetUp = false;
                }
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle1 = 0.85F * setOld.throttle1 + fm.EI.engines[0].getControlThrottle() * 0.15F;
                setNew.throttle2 = 0.85F * setOld.throttle2 + fm.EI.engines[1].getControlThrottle() * 0.15F;
                setNew.prop1 = 0.85F * setOld.prop1 + fm.EI.engines[0].getControlProp() * 0.15F;
                setNew.prop2 = 0.85F * setOld.prop2 + fm.EI.engines[1].getControlProp() * 0.15F;
                setNew.elevTrim = 0.85F * setOld.elevTrim + fm.CT.getTrimElevatorControl() * 0.15F;
                setNew.rudderTrim = 0.85F * setOld.rudderTrim + fm.CT.getTrimRudderControl() * 0.15F;
                setNew.altimeter = fm.getAltitude();
                float f = waypointAzimuth();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(f - 90F);
                    setOld.waypointAzimuth.setDeg(f - 90F);
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f);
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(0.1F) - 90F);
                }
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(10F);
    }

    public CockpitPBN()
    {
        super("3DO/Cockpit/PBY/hierPBN.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        bNeedSetUp = true;
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictFlap = 0.0F;
        pictGear = 0.0F;
        pictManf1 = 1.0F;
        pictManf2 = 1.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        cockpitNightMats = (new java.lang.String[] {
            "guages1", "guages1_broken", "guages2", "guages2_broken", "guages3", "guages3_broken", "guages4", "guages4_broken", "leftcontroles", "swbox2"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, com.maddox.rts.Time.current(), null);
        if(acoustics != null)
            acoustics.globFX = new ReverbFXRoom(0.3F);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        mesh.chunkSetAngles("ControlBar", 0.0F, (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 8F, 0.0F);
        mesh.chunkSetAngles("LWheel", 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 68F, 0.0F);
        mesh.chunkSetAngles("RWheel", 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 68F, 0.0F);
        mesh.chunkSetAngles("PilotRPedal", 0.0F, -10F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("CoPilotRPedal", 0.0F, -10F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("PilotLPedal", 0.0F, 10F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("CoPilotLPedal", 0.0F, 10F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("ThrottleL", 0.0F, 44F * interp(setNew.throttle1, setOld.throttle1, f), 0.0F);
        mesh.chunkSetAngles("ThrottleR", 0.0F, 44F * interp(setNew.throttle2, setOld.throttle2, f), 0.0F);
        mesh.chunkSetAngles("PitchL", 0.0F, 44F * interp(setNew.prop1, setOld.prop1, f), 0.0F);
        mesh.chunkSetAngles("PitchR", 0.0F, 44F * interp(setNew.prop2, setOld.prop2, f), 0.0F);
        mesh.chunkSetAngles("MixL", 0.0F, 70F * fm.EI.engines[0].getControlMix(), 0.0F);
        mesh.chunkSetAngles("MixR", 0.0F, 70F * fm.EI.engines[1].getControlMix(), 0.0F);
        mesh.chunkSetAngles("zElevatorTrim", 0.0F, 90F * fm.CT.getTrimElevatorControl(), 0.0F);
        mesh.chunkSetAngles("ZRudderTrim", 0.0F, 90F * fm.CT.getTrimRudderControl(), 0.0F);
        mesh.chunkSetAngles("zHourL", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("zMinL", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zHourR", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("zMinR", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zturnbank1L", 0.0F, -fm.Or.getKren(), 0.0F);
        mesh.chunkSetAngles("zturnbank1R", 0.0F, -fm.Or.getKren(), 0.0F);
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[2] = cvt(fm.Or.getTangage(), -45F, 45F, 0.0365F, -0.0365F);
        mesh.chunkSetLocate("zturnbank2L", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        mesh.chunkSetLocate("zturnbank2R", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        if((fm.AS.astateCockpitState & 0x40) == 0)
            mesh.chunkSetAngles("zClimbneedleL", 0.0F, floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
        mesh.chunkSetAngles("zClimbneedleR", 0.0F, floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
        w.set(fm.getW());
        fm.Or.transform(w);
        mesh.chunkSetAngles("zturnStrelkaL", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        mesh.chunkSetAngles("zturnStrelkaR", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        mesh.chunkSetAngles("zSlipballL", 0.0F, cvt(getBall(6D), -6F, 6F, -14.5F, 14.5F), 0.0F);
        mesh.chunkSetAngles("zSlipballR", 0.0F, cvt(getBall(6D), -6F, 6F, -14.5F, 14.5F), 0.0F);
        mesh.chunkSetAngles("zAirSpeedL", 0.0F, floatindex(cvt(com.maddox.il2.fm.Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()) * 0.621371F, 0.0F, 700F, 0.0F, 12F), speedometerScale), 0.0F);
        mesh.chunkSetAngles("zAirSpeedR", 0.0F, floatindex(cvt(com.maddox.il2.fm.Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()) * 0.621371F, 0.0F, 700F, 0.0F, 12F), speedometerScale), 0.0F);
        mesh.chunkSetAngles("zAltL1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        mesh.chunkSetAngles("zAltL2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        mesh.chunkSetAngles("zAltR1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        mesh.chunkSetAngles("zAltR2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        mesh.chunkSetAngles("zCompassStrelkaL", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("zRCompassStrelkaL", 0.0F, setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F);
        mesh.chunkSetAngles("zRCompassStrelkaR", 0.0F, setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F);
        if((fm.AS.astateCockpitState & 0x40) == 0)
        {
            mesh.chunkSetAngles("zMagnetic", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            mesh.chunkSetAngles("zRCompassneedleL", 0.0F, setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F);
            mesh.chunkSetAngles("zRCompassneedleR", 0.0F, setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F);
            mesh.chunkSetAngles("zCompassneedleL", 0.0F, setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F);
        }
        if((fm.AS.astateCockpitState & 0x40) == 0)
        {
            mesh.chunkSetAngles("zRPML", 0.0F, cvt(fm.EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            mesh.chunkSetAngles("zRPMR", 0.0F, cvt(fm.EI.engines[1].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            mesh.chunkSetAngles("zManPressL", 0.0F, pictManf1 = 0.9F * pictManf1 + 0.1F * cvt(fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
            mesh.chunkSetAngles("zManPressR", 0.0F, pictManf2 = 0.9F * pictManf2 + 0.1F * cvt(fm.EI.engines[1].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
        }
        mesh.chunkSetAngles("zCarbTempR", 0.0F, -cvt((com.maddox.il2.fm.Atmosphere.temperature((float)fm.Loc.z) - 273.15F) + 25F * fm.EI.engines[0].getPowerOutput(), -70F, 150F, -38.5F, 87.5F), 0.0F);
        mesh.chunkSetAngles("zCarbTempL", 0.0F, cvt((com.maddox.il2.fm.Atmosphere.temperature((float)fm.Loc.z) - 273.15F) + 25F * fm.EI.engines[1].getPowerOutput(), -70F, 150F, -38.5F, 87.5F), 0.0F);
        mesh.chunkSetAngles("zCylTempR", 0.0F, cvt(fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 102.5F), 0.0F);
        mesh.chunkSetAngles("zCylTempL", 0.0F, -cvt(fm.EI.engines[1].tWaterOut, 0.0F, 350F, 0.0F, 102.5F), 0.0F);
        mesh.chunkSetAngles("zOilTempR", 0.0F, -cvt(fm.EI.engines[0].tOilIn, -70F, 150F, 34F, -76F), 0.0F);
        mesh.chunkSetAngles("zOilTempL", 0.0F, -cvt(fm.EI.engines[1].tOilIn, -70F, 150F, 34F, -76F), 0.0F);
        mesh.chunkSetAngles("zFuelpressR", 0.0F, -cvt(fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        mesh.chunkSetAngles("zFuelpressL", 0.0F, -cvt(fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        mesh.chunkSetAngles("zOilpressR", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        mesh.chunkSetAngles("zOilpressL", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[1].tOilOut * fm.EI.engines[1].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        mesh.chunkSetAngles("zFuelR", 0.0F, cvt(fm.M.fuel, 0.0F, 2600F, 0.0F, 110F), 0.0F);
        mesh.chunkSetAngles("zFuelL", 0.0F, -cvt(fm.M.fuel, 0.0F, 2600F, 0.0F, 110F), 0.0F);
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[1] = cvt(interp(setNew.elevTrim, setOld.elevTrim, f), -0.1F, 0.1F, -0.01F, 0.01F);
        mesh.chunkSetLocate("zElevatorTrim", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[1] = cvt(interp(setNew.rudderTrim, setOld.rudderTrim, f), -0.1F, 0.1F, -0.01F, 0.01F);
        mesh.chunkSetLocate("zRudderTrim", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 0x80) != 0);
        if((fm.AS.astateCockpitState & 2) != 0)
            mesh.chunkVisible("XGlassDamage2", true);
        if((fm.AS.astateCockpitState & 1) != 0)
        {
            mesh.chunkVisible("XGlassDamage3", true);
            mesh.chunkVisible("XHullDamage1", true);
        }
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("Panel1_D0", false);
            mesh.chunkVisible("Panel3_D0", false);
            mesh.chunkVisible("Panel1_D1", true);
            mesh.chunkVisible("Panel3_D1", true);
            mesh.chunkVisible("XHullDamage3", true);
            mesh.chunkVisible("zTurnStrelkaL ", false);
            mesh.chunkVisible("zSlipballL", false);
            mesh.chunkVisible("zRCompassStrelkaL", false);
            mesh.chunkVisible("zRCompassneedleL", false);
            mesh.chunkVisible("zCompassStrelkaL", false);
            mesh.chunkVisible("zCompassneedleL ", false);
            mesh.chunkVisible("zMinL", false);
            mesh.chunkVisible("zHourL", false);
            mesh.chunkVisible("zClimbneedleL", false);
            mesh.chunkVisible("zAltL2", false);
            mesh.chunkVisible("zAltL1", false);
            mesh.chunkVisible("zAirSpeedL", false);
            mesh.chunkVisible("zRPML", false);
            mesh.chunkVisible("zRPMR", false);
            mesh.chunkVisible("zManPressL", false);
            mesh.chunkVisible("zManPressR", false);
            mesh.chunkVisible("zCarbTempR", false);
            mesh.chunkVisible("zCarbTempL", false);
            mesh.chunkVisible("zCylTempL", false);
            mesh.chunkVisible("zCylTempR", false);
            mesh.chunkVisible("zOilTempL", false);
            mesh.chunkVisible("zOilTempR", false);
            mesh.chunkVisible("zFuelpressL", false);
            mesh.chunkVisible("zFuelpressR", false);
            mesh.chunkVisible("zOilpressL", false);
            mesh.chunkVisible("zOilpressR", false);
            mesh.chunkVisible("zMagnetic ", false);
        }
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("Panel2_D0", false);
            mesh.chunkVisible("Panel2_D1", true);
            mesh.chunkVisible("zTurnStrelkaR ", false);
            mesh.chunkVisible("zSlipballR", false);
            mesh.chunkVisible("zRCompassStrelkaR", false);
            mesh.chunkVisible("zRCompassneedleR", false);
            mesh.chunkVisible("zMinR", false);
            mesh.chunkVisible("zHourR", false);
            mesh.chunkVisible("zClimbneedleR", false);
            mesh.chunkVisible("zAltR2", false);
            mesh.chunkVisible("zAltR1", false);
            mesh.chunkVisible("zAirSpeedR", false);
            mesh.chunkVisible("zFuelL", false);
            mesh.chunkVisible("zFuelR", false);
        }
        if((fm.AS.astateCockpitState & 8) != 0)
            mesh.chunkVisible("XGlassDamage1", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
            mesh.chunkVisible("XHullDamage2", true);
        if((fm.AS.astateCockpitState & 0x20) != 0)
            retoggleLight();
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private void retoggleLight()
    {
        if(cockpitLightControl)
        {
            setNightMats(false);
            setNightMats(true);
        } else
        {
            setNightMats(true);
            setNightMats(false);
        }
    }

    protected void reflectPlaneMats()
    {
        com.maddox.il2.engine.HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
    }

    private boolean bNeedSetUp;
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
        0.0F, 8.5F, 41F, 84F, 134F, 186F, 233.5F, 246.75F, 260F, 273.25F, 
        286.5F, 300.25F, 314F, 328F, 342F
    };
    private static final float variometerScale[] = {
        -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 
        124F, 147F, 170F
    };
    private com.maddox.JGP.Point3d tmpP;
    private com.maddox.JGP.Vector3d tmpV;

    static 
    {
        com.maddox.rts.Property.set(com.maddox.rts.CLASS.THIS(), "normZNs", new float[] {
            1.0F, 1.0F, 1.2F, 1.0F
        });
    }








}