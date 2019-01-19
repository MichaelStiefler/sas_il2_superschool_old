package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;
import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.util.HashMapExt;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;

public class CockpitMi24 extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                if(bNeedSetUp)
                    bNeedSetUp = false;
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle = 0.9F * setOld.throttle + ((FlightModelMain) (fm)).CT.PowerControl * 0.1F;
                setNew.starter = 0.94F * setOld.starter + 0.06F * (((FlightModelMain) (fm)).EI.engines[0].getStage() <= 0 || ((FlightModelMain) (fm)).EI.engines[0].getStage() >= 6 ? 0.0F : 1.0F);
                setNew.altimeter = fm.getAltitude();
                float f = waypointAzimuth();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(f - 90F);
                    setOld.waypointAzimuth.setDeg(f - 90F);
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
                    if(((FlightModelMain) (fm)).AS.listenLorenzBlindLanding && ((FlightModelMain) (fm)).AS.isAAFIAS)
                    {
                        setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                        setNew.ilsGS = (10F * setOld.ilsGS + getGlidePath()) / 11F;
                    } else
                    {
                        //setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), getBeaconDirection());
                        setNew.ilsLoc = 0.0F;
                        setNew.ilsGS = 0.0F;
                    }
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), waypointAzimuth() - ((FlightModelMain) (fm)).Or.azimut());
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(0.1F) - 90F);
                }
                //setNew.beaconDirection = (10F * setOld.beaconDirection + getBeaconDirection()) / 11F;
                //setNew.beaconRange = (10F * setOld.beaconRange + getBeaconRange()) / 11F;
               
                Variables variables = setNew;
                float f1 = 0.9F * setOld.radioalt;
                float f2 = 0.1F;
                float f3 = fm.getAltitude();
                World.cur();
                World.land();
                variables.radioalt = f1 + f2 * (f3 - Landscape.HQ_Air((float)fm.Loc.x, (float)fm.Loc.y));
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

        float throttle;
        float vspeed;
        float starter;
        float altimeter;
        float radioalt;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float beaconDirection;
        float beaconRange;
        float ilsLoc;
        float ilsGS;

        private Variables()
        {
            throttle = 0.0F;
            starter = 0.0F;
            altimeter = 0.0F;
            vspeed = 0.0F;
            radioalt = 0.0F;
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
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
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(Actor.isAlive(aircraft()))
            aircraft().hierMesh().chunkVisible("CF_D0", true);
        super.doFocusLeave();
    }

//    private float machNumber()
    {
//        return ((MIG_21)super.aircraft()).calculateMach();
    }

    public CockpitMi24()
    {
        super("3DO/Cockpit/Mi-24Pilot/hier.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
//        HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK1");
//        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
//        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
//        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
//        light1.light.setColor(300F, 0F, 0F);
//        light1.light.setEmit(0.0F, 0.0F);
//        pos.base().draw.lightMap().put("LAMPHOOK1", light1);
//        hooknamed = new HookNamed(mesh, "LAMPHOOK2");
//        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
//        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
//        light2 = new LightPointActor(new LightPoint(), loc.getPoint());
//        light2.light.setColor(300F, 0F, 0F);
//        light2.light.setEmit(0.0F, 0.0F);
//        pos.base().draw.lightMap().put("LAMPHOOK2", light2);
//        hooknamed = new HookNamed(mesh, "LAMPHOOK3");
//        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
//        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
//        light3 = new LightPointActor(new LightPoint(), loc.getPoint());
//        light3.light.setColor(300F, 0F, 0F);
//        light3.light.setEmit(0.0F, 0.0F);
//        pos.base().draw.lightMap().put("LAMPHOOK3", light3);
        super.cockpitNightMats = (new String[] {
            "Gause1", "Gause2", "Gause3", "Gause4", "Sidepanel", "instrument1"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
        if(super.acoustics != null)
            super.acoustics.globFX = new ReverbFXRoom(0.45F);
    }
    
    private int iLockState()
    {
        if(!(super.aircraft() instanceof TypeGuidedMissileCarrier))
            return 0;
        else
            return ((TypeGuidedMissileCarrier)super.aircraft()).getGuidedMissileUtils().getMissileLockState();
    }

    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(10F);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneToModel();
            bNeedSetUp = false;
        }
        resetYPRmodifier();
//        super.mesh.chunkSetAngles("Canopy", -90F * ((FlightModelMain) (super.fm)).CT.getCockpitDoor(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_STICK1", 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl) * 10F, -(pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl) * 10F);
        super.mesh.chunkSetAngles("Z_STICK2", 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl) * 10F, -(pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl) * 10F);
//        super.mesh.chunkSetAngles("leftrudder", 10F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
//        super.mesh.chunkSetAngles("rightrudder", 10F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
//        super.mesh.chunkSetAngles("throttle", 0.0F, -40.909F * interp(setNew.throttle, setOld.throttle, f), 0.0F);
        super.mesh.chunkSetAngles("Z_RPM1", -cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 13750F, 0.0F, 346.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPM2", -cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 0.0F, 13750F, 0.0F, 346.5F), 0.0F, 0.0F);
        
        float Engine1rpm = Aircraft.cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 12500F, 0.0F, 1.0F);
        float Engine2rpm = Aircraft.cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 0.0F, 12500F, 0.0F, 1.0F);
        float PropRPM = (float) Math.sqrt((Math.pow(Engine1rpm, 2.0F) + Math.pow(Engine2rpm, 2.0F)) / 2.0F);
        super.mesh.chunkSetAngles("Z_PropRPM", -cvt(PropRPM, 0.0F, 1.1F, 0.0F, 346.5F), 0.0F, 0.0F);
        
        super.mesh.chunkSetAngles("Z_FUEL", -cvt(((FlightModelMain) (super.fm)).M.fuel, 0.0F, 2500F, 0.0F, 301F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TEMPM1", -cvt(((FlightModelMain) (super.fm)).EI.engines[0].tWaterOut, 0F, 120F, 0.0F, 264F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TEMPM2", -cvt(((FlightModelMain) (super.fm)).EI.engines[1].tWaterOut, 0F, 120F, 0.0F, 264F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TEMPS1", -cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0F, 150F, 0.0F, 120F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TEMPS4", -cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0F, 150F, 0.0F, 120F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TEMPS2", -cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0F, 120F, 0.0F, 120F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TEMPS3", -cvt(((FlightModelMain) (super.fm)).EI.engines[1].tOilOut, 0F, 120F, 0.0F, 120F), 0.0F, 0.0F);
        
        super.mesh.chunkSetAngles("Z_TEMPR1", -cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0F, 150F, 0.0F, 120F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TEMPR2", -cvt(((FlightModelMain) (super.fm)).EI.engines[1].tOilOut, 0F, 150F, 0.0F, -120F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TEMPR3", -cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0F, 150F, 0.0F, 120F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TEMPR4", -cvt(((FlightModelMain) (super.fm)).EI.engines[1].tOilOut, 0F, 150F, 0.0F, -120F), 0.0F, 0.0F);
        
        float aPitch = (((FlightModelMain) (super.fm)).EI.engines[0].getControlProp() + ((FlightModelMain) (super.fm)).EI.engines[1].getControlProp()) / 2.0F;
        super.mesh.chunkSetAngles("Z_BladeAng", -cvt(aPitch, 0.0F, 1.153F, 0.0F, 204F), 0.0F, 0.0F);
        
        super.mesh.chunkSetAngles("Z_G", -cvt(super.fm.getOverload(), -2F, 4F, -105F, 207F), 0.0F, 0.0F);
        
        super.mesh.chunkSetAngles("Z_Slide", cvt(getBall(40D), -40F, 40F, -111F, 111F), 0.0F, 0.0F);
        
        super.mesh.chunkSetAngles("Z_Hour", -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_MIN", -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Second", -cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        w.set(fm.getW());
        fm.Or.transform(w);
        super.mesh.chunkSetAngles("Z_COMPASS1", 90F + setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_COMPASSB", -90F + setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_COMPASSC", setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F, 0.0F);
//        super.mesh.chunkSetAngles("Z_SPD", -floatindex(cvt(super.fm.getSpeedKMH(), 50F, 450F, 0.0F, 9F), speedometerScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_SPD", -cvt(((FlightModelMain) (super.fm)).getSpeedKMH(), 50F, 450F, 0.0F, 332F), 0.0F, 0.0F);
//        super.mesh.chunkSetAngles("Z_Speedometer3", cvt(machNumber(), 0.5F, 3F, 0.0F, 349F), 0.0F, 0.0F);
//        super.mesh.chunkSetAngles("Z_Speedometer2", floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 200F, 2500F, 0.0F, 24F), speedometerScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_ALTB", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_ALTS", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 10800F), 0.0F, 0.0F);
        
        float f1 = 0.9F * setOld.radioalt;
        float f2 = 0.1F;
        float f3 = super.fm.getAltitude();
        
        super.mesh.chunkSetAngles("Z_RadAlt", -floatindex(cvt(f1 + f2 * (f3 - Landscape.HQ_Air((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).x, (float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).y)), 0F, 700F, 0.0F, 8F), radaltScale), 0.0F, 0.0F);
        
        super.mesh.chunkSetAngles("Z_VertSpd", -floatindex(cvt(setNew.vspeed, -30F, 30F, 0.0F, 6F), variometerScale), 0.0F, 0.0F);
        
        super.mesh.chunkSetAngles("Z_PVert", -cvt(setNew.vspeed, -10F, 10F, -51F, 51F), 0.0F, 0.0F);
        
        
        Vector3d vector3d = new Vector3d();
        super.fm.getSpeed(vector3d);
        
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt((float) (vector3d.x * 3.6D), -25F, 50F, -0.022F, 0.047F);
        super.mesh.chunkSetLocate("Z_PSpd1", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt((float) (vector3d.y * 3.6D), -25F, 25F, 0.027F, -0.027F);
        super.mesh.chunkSetLocate("Z_PSpd2", Cockpit.xyz, Cockpit.ypr);

        super.mesh.chunkSetAngles("Z_HRZ", 0.0F, -((FlightModelMain) (super.fm)).Or.getKren(), -cvt(((FlightModelMain) (super.fm)).Or.getTangage(), -40.0F, 40.0F, -21.0F, 21.0F));
        super.mesh.chunkSetAngles("Z_HRZEmer", 0.0F, ((FlightModelMain) (super.fm)).Or.getKren(), -cvt(((FlightModelMain) (super.fm)).Or.getTangage(), -40.0F, 40.0F, -63.0F, 63.0F));
//        super.mesh.chunkSetAngles("Z_Gear1", 0.0F, 0.0F, 50F * (pictGear = 0.82F * pictGear + 0.18F * ((FlightModelMain) (super.fm)).CT.GearControl));
        
        super.mesh.chunkVisible("L_GearDown", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F);
        super.mesh.chunkVisible("L_GearUP", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F || !((FlightModelMain) (super.fm)).Gears.lgear || !((FlightModelMain) (super.fm)).Gears.rgear);
    }
/*        super.mesh.chunkVisible("L_DownC", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F);
        super.mesh.chunkVisible("L_DownL", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F);
        super.mesh.chunkVisible("L_DownR", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F);
        super.mesh.chunkVisible("L_UPC", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F || !((FlightModelMain) (super.fm)).Gears.lgear || !((FlightModelMain) (super.fm)).Gears.rgear);
        super.mesh.chunkVisible("L_UPL", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F || !((FlightModelMain) (super.fm)).Gears.lgear || !((FlightModelMain) (super.fm)).Gears.rgear);
        super.mesh.chunkVisible("L_UPR", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F || !((FlightModelMain) (super.fm)).Gears.lgear || !((FlightModelMain) (super.fm)).Gears.rgear);
        
        if(((FlightModelMain) (super.fm)).CT.getFlap() > 0.9F)
            super.mesh.chunkVisible("L_Flapland", true);
       else
            super.mesh.chunkVisible("L_Flapland", false);


        if(((FlightModelMain) (super.fm)).CT.getAirBrake() > 0.1F)
            super.mesh.chunkVisible("L_Airbrake", true);
        else
            super.mesh.chunkVisible("L_Airbrake", false);
            
        if(((FlightModelMain) (super.fm)).CT.getTrimElevatorControl() > 0.19F)
                super.mesh.chunkVisible("L_Trimland", true);
            else
                super.mesh.chunkVisible("L_Trimland", false);
                
        if(((FlightModelMain) (super.fm)).CT.getTrimElevatorControl() == 0.0F)
                super.mesh.chunkVisible("L_Trimneutral", true);
            else
                super.mesh.chunkVisible("L_Trimneutral", false);
              
        if(((FlightModelMain) (super.fm)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.fm)).EI.engines[0].getStage() > 5)
                super.mesh.chunkVisible("L_AB1", true);
            else
                super.mesh.chunkVisible("L_AB1", false);
                
        if(((FlightModelMain) (super.fm)).EI.engines[0].getThrustOutput() > 1.05F && ((FlightModelMain) (super.fm)).EI.engines[0].getStage() > 5)
                super.mesh.chunkVisible("L_AB2", true);
            else
                super.mesh.chunkVisible("L_AB2", false);
          
        super.mesh.chunkVisible("L_Fire1", ((FlightModelMain) (super.fm)).AS.astateEngineStates[0] > 2);
        super.mesh.chunkVisible("L_Fire2", ((FlightModelMain) (super.fm)).AS.astateEngineStates[0] > 2);

        super.mesh.chunkVisible("L_Fuel", ((FlightModelMain) (super.fm)).M.fuel < 750F);

        if(super.fm.getAltitude() < 150F)
                super.mesh.chunkVisible("L_Altitude1", true);
            else
                super.mesh.chunkVisible("L_Altitude1", false);
                
        if(super.fm.getAltitude() < 300F)
                super.mesh.chunkVisible("L_Altitude2", true);
            else
                super.mesh.chunkVisible("L_Altitude2", false);

//        if(super.fm.getAOA() > 20F)
//                super.mesh.chunkVisible("L_AOA1", true);
//            else
//                super.mesh.chunkVisible("L_AOA1", false);
//                
//        if(super.fm.getAOA() > 20F)
//                super.mesh.chunkVisible("L_AOA2", true);
//            else
//                super.mesh.chunkVisible("L_AOA2", false);
                
                
        if(super.fm.getAOA() > 20F)
        {
                super.mesh.chunkVisible("L_AOA1", true);
                super.mesh.chunkVisible("L_AOA2", true);
                } else
        {
                super.mesh.chunkVisible("L_AOA1", false);
                super.mesh.chunkVisible("L_AOA2", false);
        }

        if(((Interpolate) (super.fm)).actor instanceof TypeGuidedMissileCarrier)
        {
            if(iLockState() == 2)
                super.mesh.chunkVisible("L_Missiles", true);
            else
                super.mesh.chunkVisible("L_Missiles", false);
        if(((MIG_21)aircraft()).k14Mode == 2)
            super.mesh.chunkVisible("Z_Z_RETICLE", false);
        else
            super.mesh.chunkVisible("Z_Z_RETICLE", true);
        }
    }

    public void reflectCockpitState()
    {
    } */

/*    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
        {
            light1.light.setEmit(0.0065F, 0.5F);
            light2.light.setEmit(0.0065F, 0.5F);
            light3.light.setEmit(0.0065F, 0.5F);
            setNightMats(true);
        } else
        {
            light1.light.setEmit(0.0F, 0.0F);
            light2.light.setEmit(0.0F, 0.0F);
            light3.light.setEmit(0.0F, 0.0F);
            setNightMats(false);
        }
    } */
    
    protected void reflectPlaneMats()
    {
        mesh.chunkVisible("Cockpit_D0", false);
        mesh.chunkVisible("CF_D0", false);
    }


    protected void reflectPlaneToModel()
    {
    }

    public void doToggleDim()
    {
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private float pictAiler;
    private float pictElev;
    private LightPointActor light1;
    private LightPointActor light2;
    private LightPointActor light3;
    private boolean bNeedSetUp;
    private static final float speedometerScale[] = {
        0.0F, 39F, 84F, 129F, 174F, 213F, 255F, 294F, 330F
    };
    
    private static final float radaltScale[] = {
        0.0F, 155F, 181F, 207F, 232F, 257F, 281F, 303F
    };
    private static final float variometerScale[] = {
        -180F, -141F, -78F, 0.0F, 78F, 141F, 180F 
    };
    private static final float rpmScale[] = {
        -5F, 29F, 58F, 87F, 116F, 155F, 188F, 196.71F, 205.42F, 214.13F, 
        222.84F, 231.55F, 240.26F, 248.97F, 257.68F, 266.39F, 275.1F, 283.81F, 292.52F, 301.23F, 
        310F
    };
    private static final float rpmScale2[] = {
        -5F, 29F, 58F, 87F, 100F, 110F, 120F, 132.07F, 144.14F, 156.21F, 
        168.28F, 180.35F, 192.42F, 204.49F, 216.56F, 228.63F, 240.07F, 252.77F, 264.84F, 276.91F, 
        289F
    };
    public Vector3f w;
    private float pictGear;
}