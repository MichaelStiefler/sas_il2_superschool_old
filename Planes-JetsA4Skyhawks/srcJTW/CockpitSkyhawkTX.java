// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 03/11/2015 10:57:09 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitSkyhawkTX.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit

public class CockpitSkyhawkTX extends CockpitPilot
{
    private class Variables
    {

        float throttle;
        float prop;
        float mix;
        float stage;
        float altimeter;
        float vspeed;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
        }

        Variables(Variables variables)
        {
            this();
        }
    }

    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle = 0.85F * setOld.throttle + ((FlightModelMain) (fm)).CT.PowerControl * 0.15F;
                setNew.prop = 0.85F * setOld.prop + ((FlightModelMain) (fm)).CT.getStepControl() * 0.15F;
                setNew.stage = 0.85F * setOld.stage + (float)((FlightModelMain) (fm)).EI.engines[0].getControlCompressor() * 0.15F;
                setNew.mix = 0.85F * setOld.mix + ((FlightModelMain) (fm)).EI.engines[0].getControlMix() * 0.15F;
                setNew.altimeter = fm.getAltitude();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    public CockpitSkyhawkTX()
    {
        super("3DO/Cockpit/CockpitSkyHawkTX/hier.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        super.cockpitNightMats = (new String[] {
            "GagePanel1", "GagePanel2", "GagePanel3", "GagePanel4", "GagePanel5", "GagePanel6", "GagePanel7", "Glass", "needles", "radio1"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        super.mesh.chunkSetAngles("Z_RightPedal", 15F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_LeftPedal", -15F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Throtle1", 40F * setNew.throttle, 0.0F, 0.0F);
        resetYPRmodifier();
        float f1 = ((FlightModelMain) (super.fm)).EI.engines[0].getStage();
        if(f1 > 0.0F && f1 < 7F)
            f1 = 0.0345F;
        else
            f1 = -0.05475F;
        Cockpit.xyz[2] = f1;
        super.mesh.chunkSetLocate("Z_EngShutOff", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("Z_Column", (pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl) * 16F, 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl) * 20F);
        if(((FlightModelMain) (super.fm)).CT.GearControl == 0.0F && ((FlightModelMain) (super.fm)).CT.getGear() != 0.0F)
            f1 = 40F;
        else
        if(((FlightModelMain) (super.fm)).CT.GearControl == 1.0F && ((FlightModelMain) (super.fm)).CT.getGear() != 1.0F)
            f1 = 20F;
        else
            f1 = 0.0F;
        super.mesh.chunkSetAngles("Z_Gear1", f1, 0.0F, 0.0F);
        resetYPRmodifier();
        if(((FlightModelMain) (super.fm)).CT.saveWeaponControl[0])
            Cockpit.xyz[2] = -0.0029F;
        super.mesh.chunkSetLocate("Z_DropTank", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), speedometerScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        f1 = ((FlightModelMain) (super.fm)).EI.engines[0].getPowerOutput();
        super.mesh.chunkSetAngles("Z_Fuel1", cvt((float)Math.sqrt(f1), 0.0F, 1.0F, -59.5F, 223F), 0.0F, 0.0F);
        f1 = cvt(((FlightModelMain) (super.fm)).M.fuel, 0.0F, 1000F, 0.0F, 270F);
        if(f1 < 45F)
            f1 = cvt(f1, 0.0F, 45F, -58F, 45F);
        f1 += 58F;
        super.mesh.chunkSetAngles("Z_Fuel2", f1, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Oilpres1", cvt(1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[0].tOilOut * ((FlightModelMain) (super.fm)).EI.engines[0].getReadyness(), 0.0F, 28F, 0.0F, 180F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPM1", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 1100F, 0.0F, 322F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPM2", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 289F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Temp1", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 40F, 150F, 0.0F, 116.75F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Temp2", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 40F, 150F, 0.0F, 116.75F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Climb1", floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Oxypres1", 142.5F, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass3", 90F + setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass2", setNew.waypointAzimuth.getDeg(0.1F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass1", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TurnBank1", cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        w.set(super.fm.getW());
        ((FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("Z_TurnBank2", cvt(getBall(7D), -7F, 7F, -15F, 15F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TurnBank3a", ((FlightModelMain) (super.fm)).Or.getKren(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, 0.0F, cvt(((FlightModelMain) (super.fm)).Or.getTangage(), -45F, 45F, 1.5F, -1.5F));
        super.mesh.chunkSetAngles("Z_Pres1", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 320F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Ny1", cvt(super.fm.getOverload(), -4F, 12F, -80.5F, 241.5F), 0.0F, 0.0F);
        super.mesh.chunkVisible("Z_GearGreen1", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F);
        super.mesh.chunkVisible("Z_GearRed1", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F || !((FlightModelMain) (super.fm)).Gears.lgear || !((FlightModelMain) (super.fm)).Gears.rgear);
        super.mesh.chunkVisible("Z_LampFuelL", ((FlightModelMain) (super.fm)).M.fuel < 500F);
        super.mesh.chunkVisible("Z_LampFuelR", ((FlightModelMain) (super.fm)).M.fuel < 500F);
        super.mesh.chunkVisible("Z_LampFuelCf", ((FlightModelMain) (super.fm)).M.fuel < 125F);
        super.mesh.chunkVisible("Z_Trim1", Math.abs(((FlightModelMain) (super.fm)).CT.getTrimElevatorControl()) < 0.05F);
        super.mesh.chunkVisible("Z_FireLamp", ((FlightModelMain) (super.fm)).AS.astateEngineStates[0] > 2);
    }

    public void reflectCockpitState()
    {
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage2", true);
            super.mesh.chunkVisible("Pricel_D0", false);
            super.mesh.chunkVisible("Pricel_D1", true);
            super.mesh.chunkVisible("Pricel1_D0", false);
            super.mesh.chunkVisible("Pricel1_D1", true);
            super.mesh.chunkVisible("Z_Z_RETICLE", false);
            super.mesh.chunkVisible("Z_Z_RETICLE1", false);
            for(int i = 1; i < 7; i++)
                super.mesh.chunkVisible("Z_Z_AIMMARK" + i, false);

            super.mesh.chunkVisible("Z_Z_MASK", false);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 1) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage1", true);
            super.mesh.chunkVisible("XGlassDamage4", true);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) != 0)
        {
            super.mesh.chunkVisible("Panel_D0", false);
            super.mesh.chunkVisible("Panel_D1", true);
            super.mesh.chunkVisible("XHullDamage2", true);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 4) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage3", true);
            super.mesh.chunkVisible("XHullDamage3", true);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 8) != 0)
            super.mesh.chunkVisible("XHullDamage4", true);
        ((FlightModelMain) (super.fm)).AS.getClass();
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage4", true);
            super.mesh.chunkVisible("XHullDamage1", true);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) != 0)
        {
            super.mesh.chunkVisible("XHullDamage2", true);
            super.mesh.chunkVisible("XHullDamage3", true);
        }
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
    public Vector3f w;
    private float pictAiler;
    private float pictElev;
    private static final float speedometerScale[] = {
        0.0F, 42F, 65.5F, 88.5F, 111.3F, 134F, 156.5F, 181F, 205F, 227F, 
        249.4F, 271.7F, 294F, 316.5F, 339.5F
    };
    private static final float variometerScale[] = {
        -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 
        124F, 147F, 170F
    };







}
