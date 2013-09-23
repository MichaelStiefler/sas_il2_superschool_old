package com.maddox.il2.objects.air;

import java.lang.reflect.Field;

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitWhirlwind extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(bNeedSetUp)
            {
                reflectPlaneMats();
                bNeedSetUp = false;
            }
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.trim = 0.92F * setOld.trim + 0.08F * ((FlightModelMain) (fm)).CT.getTrimElevatorControl();
                setNew.throttle1 = 0.85F * setOld.throttle1 + ((FlightModelMain) (fm)).EI.engines[0].getControlThrottle() * 0.15F;
                setNew.throttle2 = 0.85F * setOld.throttle2 + ((FlightModelMain) (fm)).EI.engines[1].getControlThrottle() * 0.15F;
                setNew.prop1 = 0.85F * setOld.prop1 + ((FlightModelMain) (fm)).EI.engines[0].getControlProp() * 0.15F;
                setNew.prop2 = 0.85F * setOld.prop2 + ((FlightModelMain) (fm)).EI.engines[1].getControlProp() * 0.15F;
                setNew.mix1 = 0.85F * setOld.mix1 + ((FlightModelMain) (fm)).EI.engines[0].getControlMix() * 0.15F;
                setNew.mix2 = 0.85F * setOld.mix2 + ((FlightModelMain) (fm)).EI.engines[1].getControlMix() * 0.15F;
                setNew.altimeter = fm.getAltitude();
                float f = waypointAzimuth();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), f);
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(f - 90F);
                    setOld.waypointAzimuth.setDeg(f - 90F);
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f);
                }
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
        float mix1;
        float mix2;
        float altimeter;
        float vspeed;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float trim;

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


    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(10F);
    }

    public CockpitWhirlwind()
    {
        super("3DO/Cockpit/Whirlwind/hier.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictFlap = 0.0F;
        pictGear = 0.0F;
        bNeedSetUp = true;
        super.cockpitNightMats = (new String[] {
            "gauges1", "gauges1_dam", "gauges2", "gauges2_dam", "gauges3", "gauges3_dam", "gauges4", "swbox", "swbox2"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        
        // The field "printCompassHeading" has been a static field of "AircraftLH" class up to IL-2 4.10.1m and became an object field of "Cockpit" class in 4.11.1m onwards.
        // The following code is supposed to dynamically access the available field according to it's presence.
        // Attention: Sticky fingers here, we've got to go through reflection!
        
        Class superClass = this.getClass().getSuperclass().getSuperclass();
        boolean oldBaseGameVersion = true;
        try {
        	Field f = superClass.getDeclaredField("printCompassHeading");
//        	System.out.println("printCompassHeading found in super class, we're on 4.11m+");
        	f.setBoolean(this, true);
//        	System.out.println("printCompassHeading=true success!");
        	oldBaseGameVersion = false;
        } catch (Exception e) {
//        	e.printStackTrace();
        }
        if (oldBaseGameVersion) {
            Class aircradtLHClass = AircraftLH.class;
	        try {
	        	Field f = aircradtLHClass.getField("printCompassHeading");
//	        	System.out.println("printCompassHeading found in AircraftLH class, we're on 4.10.1m-");
	        	f.setBoolean(this, true);
//	        	System.out.println("printCompassHeading=true success!");
	        } catch (Exception e) {
//	        	e.printStackTrace();
	        }
        }
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            aircraft().hierMesh().chunkVisible("Radar_Set", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Blister1_D0", true);
        aircraft().hierMesh().chunkVisible("Radar_Set", true);
        super.doFocusLeave();
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        super.mesh.chunkSetAngles("Z_Trim1", -1722F * setNew.trim, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Flaps1", 60F * (pictFlap = 0.85F * pictFlap + 0.15F * ((FlightModelMain) (super.fm)).CT.FlapsControl), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Gear1", 90F * (pictGear = 0.85F * pictGear + 0.15F * ((FlightModelMain) (super.fm)).CT.GearControl), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Throtle1", 93.1F * setNew.throttle1, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Throtle2", 93.1F * setNew.throttle2, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Prop1", 95F * setNew.prop1, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Prop2", 95F * setNew.prop2, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Mixture1", 90F * setNew.mix1, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Mixture2", 90F * setNew.mix2, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RightPedal", 0.0F, 0.0F, 16F * ((FlightModelMain) (super.fm)).CT.getRudder());
        super.mesh.chunkSetAngles("Z_RPedalStep", 0.0F, 0.0F, 16F * ((FlightModelMain) (super.fm)).CT.getRudder());
        super.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 0.0F, -16F * ((FlightModelMain) (super.fm)).CT.getRudder());
        super.mesh.chunkSetAngles("Z_LPedalStep", 0.0F, 0.0F, -16F * ((FlightModelMain) (super.fm)).CT.getRudder());
        super.mesh.chunkSetAngles("Z_Column", 0.0F, 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl) * 70F);
        super.mesh.chunkSetAngles("Z_Columnbase", 0.0F, 0.0F, (pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl) * 10F);
        resetYPRmodifier();
        if(((FlightModelMain) (super.fm)).CT.saveWeaponControl[0])
            Cockpit.xyz[2] = 0.01065F;
        super.mesh.chunkSetLocate("Z_Columnbutton1", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        if(((FlightModelMain) (super.fm)).CT.saveWeaponControl[1])
            Cockpit.xyz[2] = 0.01065F;
        super.mesh.chunkSetLocate("Z_Columnbutton2", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), speedometerScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass1", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass3", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass2", 90F + setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        float f1 = 0.0F;
        if(((FlightModelMain) (super.fm)).AS.bLandingLightOn)
            f1 -= 35F;
        if(((FlightModelMain) (super.fm)).AS.bNavLightsOn)
            f1 -= 5F;
        if(super.cockpitLightControl)
            f1 -= 2.87F;
        super.mesh.chunkSetAngles("Z_Amper1", cvt(f1 + cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 150F, 2380F, 0.0F, 41.1F), -20F, 130F, -11.5F, 81.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Amper2", cvt(f1 + cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 150F, 2380F, 0.0F, 41.1F), -20F, 130F, -11.5F, 81.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Carbair1", cvt((Atmosphere.temperature((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z) - 273.15F) + 25F * ((FlightModelMain) (super.fm)).EI.engines[0].getPowerOutput(), -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Carbair2", cvt((Atmosphere.temperature((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z) - 273.15F) + 25F * ((FlightModelMain) (super.fm)).EI.engines[1].getPowerOutput(), -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Climb1", floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Coolant1", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tWaterOut, -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Coolant2", cvt(((FlightModelMain) (super.fm)).EI.engines[1].tWaterOut, -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Temp1", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 131.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Temp2", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 131.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TurnBank1", -((FlightModelMain) (super.fm)).Or.getKren(), 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(((FlightModelMain) (super.fm)).Or.getTangage(), -45F, 45F, 0.031F, -0.031F);
        super.mesh.chunkSetLocate("Z_TurnBank2", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("Z_Fuel1", cvt(((FlightModelMain) (super.fm)).M.fuel, 0.0F, 245.2F, 0.0F, 120F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Fuel2", cvt(((FlightModelMain) (super.fm)).M.fuel, 0.0F, 245.2F, 0.0F, 120F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Fuel3", cvt(((FlightModelMain) (super.fm)).M.fuel, 245.2F, 490.4F, 0.0F, 120F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Fuel4", cvt(((FlightModelMain) (super.fm)).M.fuel, 245.2F, 490.4F, 0.0F, 120F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TurnBank3", cvt(getBall(7D), -7F, 7F, -16F, 16F), 0.0F, 0.0F);
        w.set(super.fm.getW());
        ((FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("Z_TurnBank4", cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 29.5F, -29.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Pres1", 73F, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Manifold1", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 320F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Manifold2", cvt(((FlightModelMain) (super.fm)).EI.engines[1].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 320F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Oil1", cvt(1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[0].tOilOut * ((FlightModelMain) (super.fm)).EI.engines[0].getReadyness(), 0.0F, 28F, 0.0F, 164.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Oil2", cvt(1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[1].tOilOut * ((FlightModelMain) (super.fm)).EI.engines[1].getReadyness(), 0.0F, 28F, 0.0F, 164.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_fuelpress1", cvt(((FlightModelMain) (super.fm)).M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.4F, 0.0F, 164F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_fuelpress2", cvt(((FlightModelMain) (super.fm)).M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.4F, 0.0F, 164F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPM1", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 331F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPM2", cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 0.0F, 4500F, 0.0F, 331F), 0.0F, 0.0F);
        super.mesh.chunkVisible("Z_GearGreen1", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F);
        super.mesh.chunkVisible("Z_GearRed1", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F || !((FlightModelMain) (super.fm)).Gears.lgear || !((FlightModelMain) (super.fm)).Gears.rgear);
    }

    public void reflectCockpitState()
    {
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) == 0);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 1) == 0);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) != 0)
        {
            super.mesh.chunkVisible("Panel_D0", false);
            super.mesh.chunkVisible("Panel_D1", true);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 4) == 0);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 8) == 0);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x80) != 0)
            super.mesh.chunkVisible("Z_OilSplats_D1", true);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) == 0);
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) == 0);
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

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        super.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        super.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        super.mesh.materialReplace("Gloss2D2o", mat);
    }

    protected void reflectPlaneToModel()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        super.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        super.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        super.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        super.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        super.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        super.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private float pictAiler;
    private float pictElev;
    private float pictFlap;
    private float pictGear;
    private boolean bNeedSetUp;
    private static final float speedometerScale[] = {
        0.0F, 18.5F, 62F, 107F, 152.5F, 198.5F, 238.5F, 252F, 265F, 278.5F, 
        292F, 305.5F, 319F, 331.5F, 343F
    };
    private static final float variometerScale[] = {
        -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 
        124F, 147F, 170F
    };
}
