
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Aircraft, Cockpit

public class CockpitF_4E extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.altimeter = ((FlightModelMain) (fm)).getAltitude();
            setNew.radioaltimeter = ((FlightModelMain) (fm)).getAltitude() - (float)Engine.land().HQ_Air(((FlightModelMain) (fm)).Loc.x, ((FlightModelMain) (fm)).Loc.y);
            setNew.throttlel = (10F * setOld.throttlel + ((FlightModelMain) (fm)).EI.engines[0].getControlThrottle()) / 11F;
            setNew.throttler = (10F * setOld.throttler + ((FlightModelMain) (fm)).EI.engines[1].getControlThrottle()) / 11F;
            setNew.fuelpressl = (10F * setOld.fuelpressl + (((FlightModelMain) (fm)).M.fuel <= 1.0F ? 0.0F : 80F * ((FlightModelMain) (fm)).EI.engines[0].getPowerOutput() * ((FlightModelMain) (fm)).EI.engines[0].getReadyness())) / 11F;
            setNew.fuelpressr = (10F * setOld.fuelpressr + (((FlightModelMain) (fm)).M.fuel <= 1.0F ? 0.0F : 80F * ((FlightModelMain) (fm)).EI.engines[1].getPowerOutput() * ((FlightModelMain) (fm)).EI.engines[1].getReadyness())) / 11F;
            if( ((FlightModelMain) (fm)).getOverload() > setOld.accelloMax )
                setNew.accelloMax = ((FlightModelMain) (fm)).getOverload();
            if( ((FlightModelMain) (fm)).getOverload() < setOld.accelloMin )
                setNew.accelloMin = ((FlightModelMain) (fm)).getOverload();
            float nozzle = 100F;
            if(((FlightModelMain) (fm)).EI.engines[0].getControlThrottle() > 1.00F)
                nozzle = 100F;
            else if(((FlightModelMain) (fm)).EI.engines[0].getControlThrottle() > 0.95F)
                nozzle = 75F;
            else if(((FlightModelMain) (fm)).EI.engines[0].getControlThrottle() > 0.60F)
                nozzle = 10F;
            else if(((FlightModelMain) (fm)).EI.engines[0].getControlThrottle() > 0.35F)
                nozzle = 30F;
            else if(((FlightModelMain) (fm)).EI.engines[0].getControlThrottle() > 0.05F)
                nozzle = 50F;
            else
                nozzle = 100F;
            setNew.nozzlel = (4F * setOld.nozzlel + nozzle) / 5F;
            if(((FlightModelMain) (fm)).EI.engines[1].getControlThrottle() > 1.00F)
                nozzle = 100F;
            else if(((FlightModelMain) (fm)).EI.engines[1].getControlThrottle() > 0.95F)
                nozzle = 75F;
            else if(((FlightModelMain) (fm)).EI.engines[1].getControlThrottle() > 0.60F)
                nozzle = 10F;
            else if(((FlightModelMain) (fm)).EI.engines[1].getControlThrottle() > 0.35F)
                nozzle = 30F;
            else if(((FlightModelMain) (fm)).EI.engines[1].getControlThrottle() > 0.05F)
                nozzle = 50F;
            else
                nozzle = 100F;
            setNew.nozzler = (4F * setOld.nozzler + nozzle) / 5F;
            float f = waypointAzimuth();
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
                    setNew.ilsLoc = 0.0F;
                    setNew.ilsGS = 0.0F;
                }
            } else
            {
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f);
                setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(0.1F) - 90F);
            }
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
            setNew.vspeed = (299F * setOld.vspeed + ((FlightModelMain) (fm)).getVertSpeed()) / 300F;
            if(cockpitDimControl)
            {
                if(setNew.dimPosition > 0.0F)
                    setNew.dimPosition = setOld.dimPosition - 0.05F;
            } else
            if(setNew.dimPosition < 1.0F)
                setNew.dimPosition = setOld.dimPosition + 0.05F;
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float altimeter;
        float radioaltimeter;
        float vspeed;
        float throttlel;
        float throttler;
        float fuelpressl;
        float fuelpressr;
        float accelloMax;
        float accelloMin;
        float nozzlel;
        float nozzler;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float beaconDirection;
        float beaconRange;
        float ilsLoc;
        float ilsGS;
        float dimPosition;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
            accelloMax = 1.0F;
            accelloMin = 1.0F;
        }

        Variables(Variables variables)
        {
            this();
        }
    }


    protected float waypointAzimuth()
    {
        return waypointAzimuthInvertMinus(30F);
    }

    public CockpitF_4E()
    {
        super("3DO/Cockpit/F4E/F4.him", "he111");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        tmpP = new Point3d();
        tmpV = new Vector3d();
        gun = new Gun[4];
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        bU4 = false;
        cockpitNightMats = (new String[] {
            "2petitsb_d1", "2petitsb", "aiguill1", "badinetm_d1", "badinetm", "baguecom", "brasdele", "comptemu_d1", "comptemu", "petitfla_d1", 
            "petitfla", "turnbank"
        });
        setNightMats(false);
        setNew.dimPosition = 1.0F;
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void removeCanopy()
    {
        mesh.chunkVisible("Blister", false);
        mesh.chunkVisible("Z_Holes2_D1", false);
        mesh.chunkVisible("Z_Holes1_D1", false);
    }

    public void reflectWorldToInstruments(float f)
    {
        float feetmultiplier = 3.28084F;
        float newAltimeterFeet = setNew.altimeter * feetmultiplier;
        float oldAltimeterFeet = setOld.altimeter * feetmultiplier;
        float newRadioAltimeterFeet = setNew.radioaltimeter * feetmultiplier;
        float oldRadioAltimeterFeet = setOld.radioaltimeter * feetmultiplier;
        float AOAunits = ((FlightModelMain) (fm)).getAOA() * 0.7728F + 12.22F;

        Cockpit.xyz[0] = Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;

        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        if(gun[0] == null)
        {
            gun[0] = ((Aircraft)((Interpolate) (fm)).actor).getGunByHookName("_CANNON03");
            gun[1] = ((Aircraft)((Interpolate) (fm)).actor).getGunByHookName("_CANNON01");
            gun[2] = ((Aircraft)((Interpolate) (fm)).actor).getGunByHookName("_CANNON02");
            gun[3] = ((Aircraft)((Interpolate) (fm)).actor).getGunByHookName("_CANNON04");
        }
        if(((FlightModelMain) (fm)).isTick(44, 0))
        {
            mesh.chunkVisible("Z_GearLGreen", ((FlightModelMain) (fm)).CT.getGear() == 1.0F && ((FlightModelMain) (fm)).Gears.lgear);
            mesh.chunkVisible("Z_GearRGreen", ((FlightModelMain) (fm)).CT.getGear() == 1.0F && ((FlightModelMain) (fm)).Gears.rgear);
            mesh.chunkVisible("Z_GearCGreen", ((FlightModelMain) (fm)).CT.getGear() == 1.0F && ((FlightModelMain) (fm)).Gears.cgear);
            mesh.chunkVisible("Z_FlapLEGreen", ((FlightModelMain) (fm)).CT.FlapsControl > 0.2F);
            mesh.chunkVisible("Z_FlapTEGreen", ((FlightModelMain) (fm)).CT.FlapsControl > 0.2F);
            if(!bU4)
            {
                mesh.chunkVisible("Z_GunLamp01", !gun[0].haveBullets());
                mesh.chunkVisible("Z_GunLamp02", !gun[1].haveBullets());
                mesh.chunkVisible("Z_GunLamp03", !gun[2].haveBullets());
                mesh.chunkVisible("Z_GunLamp04", !gun[3].haveBullets());
            }
            mesh.chunkVisible("Z_CabinLamp", ((Tuple3d) (((FlightModelMain) (fm)).Loc)).z > 12000D);
            mesh.chunkVisible("Z_FuelLampV", ((FlightModelMain) (fm)).M.fuel < 300F);
            mesh.chunkVisible("Z_FuelLampIn", ((FlightModelMain) (fm)).M.fuel < 300F);
        }
        mesh.chunkSetAngles("Z_ReviTint", cvt(interp(setNew.dimPosition, setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -45F), 0.0F, 0.0F);
        resetYPRmodifier();
        mesh.chunkSetAngles("Blister", 0.0F, 0.0F, 60F * ((FlightModelMain) (fm)).CT.getCockpitDoor());
        mesh.chunkSetAngles("Gearcontrol", 0.0F, 0.0F, -15F * ((FlightModelMain) (fm)).CT.GearControl);
        mesh.chunkSetAngles("Hookcontrol", 0.0F, 0.0F, 15F * ((FlightModelMain) (fm)).CT.arrestorControl);
        resetYPRmodifier();
        Cockpit.xyz[1] = ((FlightModelMain) (fm)).CT.FlapsControl >= ((FlightModelMain) (fm)).CT.getFlap() ? 0.0F : -0.0107F;
        mesh.chunkSetLocate("Z_FlapEin", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = ((FlightModelMain) (fm)).CT.FlapsControl <= ((FlightModelMain) (fm)).CT.getFlap() ? 0.0F : -0.0107F;
        mesh.chunkSetLocate("Z_FlapAus", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_Column", 10F * (pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (fm)).CT.AileronControl), 0.0F, 10F * (pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (fm)).CT.ElevatorControl));
        resetYPRmodifier();
        if(((FlightModelMain) (fm)).CT.saveWeaponControl[0])
            Cockpit.xyz[2] = -0.0025F;
        mesh.chunkSetLocate("Z_Columnbutton1", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        if(((FlightModelMain) (fm)).CT.saveWeaponControl[2] || ((FlightModelMain) (fm)).CT.saveWeaponControl[3])
            Cockpit.xyz[2] = -0.00325F;
        mesh.chunkSetLocate("Z_Columnbutton2", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_PedalStrut", 20F * ((FlightModelMain) (fm)).CT.getRudder(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_LeftPedal", -20F * ((FlightModelMain) (fm)).CT.getRudder(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RightPedal", -20F * ((FlightModelMain) (fm)).CT.getRudder(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ThrottleL", 0.0F, -75F * interp(setNew.throttlel, setOld.throttlel, f), 0.0F);
        mesh.chunkSetAngles("Z_ThrottleR", 0.0F, -75F * interp(setNew.throttler, setOld.throttler, f), 0.0F);
        mesh.chunkSetAngles("Z_FuelLeverL", ((FlightModelMain) (fm)).EI.engines[0].getControlMagnetos() != 3 ? 0.0F : 6.5F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_FuelLeverR", ((FlightModelMain) (fm)).EI.engines[1].getControlMagnetos() != 3 ? 0.0F : 6.5F, 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[1] = 0.03675F * ((FlightModelMain) (fm)).CT.getTrimElevatorControl();
        mesh.chunkSetLocate("Z_TailTrim", Cockpit.xyz, Cockpit.ypr);
        if(((FlightModelMain) (fm)).CT.Weapons[3] != null && !((FlightModelMain) (fm)).CT.Weapons[3][0].haveBullets())
            mesh.chunkSetAngles("Z_Bombbutton", 0.0F, 53F, 0.0F);
        mesh.chunkSetAngles("Z_AmmoCounter1", cvt(gun[1].countBullets(), 0.0F, 100F, 0.0F, -7F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_AmmoCounter2", cvt(gun[2].countBullets(), 0.0F, 100F, 0.0F, -7F), 0.0F, 0.0F);
        if(((FlightModelMain) (fm)).getSpeedKMH() < 400F)
            mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (fm)).Loc)).z, ((FlightModelMain) (fm)).getSpeedKMH()), 0.0F, 1500.24F, 0.0F, 58F), speedometerIndScale), 0.0F, 0.0F);
        else
            mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(((FlightModelMain) (fm)).getSpeedKMH(), 0.0F, 1500.24F, 0.0F, 58F), speedometerIndScale), 0.0F, 0.0F);
        if(((FlightModelMain) (fm)).getSpeedKMH() < 1500F)
            mesh.chunkSetAngles("Z_MachMeter", floatindex(cvt(((Atmosphere.sonicSpeed((float)((Tuple3d) (((FlightModelMain) (fm)).Loc)).z) / 334F) - 1F), -0.5F, 0.0F, 0.0F, 5F), machmeterScale), 0.0F, 0.0F);
        else
            mesh.chunkSetAngles("Z_MachMeter", floatindex(cvt((((Atmosphere.sonicSpeed((float)((Tuple3d) (((FlightModelMain) (fm)).Loc)).z) / 334F) - 1F) + ((1500F - ((FlightModelMain) (fm)).getSpeedKMH()) / 1500F)), -0.5F, 0.0F, 0.0F, 5F), machmeterScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_TAS_1", cvt(((FlightModelMain) (fm)).getSpeedKMH(), 0.0F, 3704F, 0.0F, 72000F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_TAS_10", cvt((float)Math.floor(((FlightModelMain) (fm)).getSpeedKMH() / 18.52F), 0.0F, 200F, 0.0F, 7200F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_TAS_100", cvt((float)Math.floor(((FlightModelMain) (fm)).getSpeedKMH() / 185.2F), 0.0F, 20F, 0.0F, 720F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_TAS_1000", cvt((float)Math.floor(((FlightModelMain) (fm)).getSpeedKMH() / 1852F), 0.0F, 2F, 0.0F, 72F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(newAltimeterFeet, oldAltimeterFeet, f) , 0.0F, 110000F, 0.0F, 39600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1_1k", cvt((float)Math.floor(newAltimeterFeet / 1000F), 0.0F, 110F, 0.0F, 3960F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1_10k", cvt(((float)Math.floor(newAltimeterFeet / 10000F)), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1m_1", cvt((setNew.altimeter / 10F), 0.0F, 4000F, 0.0F, 144000F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1m_10", cvt(((float)Math.floor(setNew.altimeter / 100F)), 0.0F, 400F, 0.0F, 14400F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1m_100", cvt(((float)Math.floor(setNew.altimeter / 1000F)), 0.0F, 40F, 0.0F, 1440F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1m_1000", cvt(((float)Math.floor(setNew.altimeter / 10000F)), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);

        if(newRadioAltimeterFeet < 500F)
            mesh.chunkSetAngles("Z_Altimeter3", cvt(interp(newRadioAltimeterFeet, oldRadioAltimeterFeet, f), 0.0F, 500F, 0.0F, 180F), 0.0F, 0.0F);
        else if(newAltimeterFeet < 1000F)
            mesh.chunkSetAngles("Z_Altimeter3", cvt(interp(newRadioAltimeterFeet, oldRadioAltimeterFeet, f), 0.0F, 1000F, 90F, 270F), 0.0F, 0.0F);
        else if(newAltimeterFeet < 5000F)
            mesh.chunkSetAngles("Z_Altimeter3", cvt(interp(newRadioAltimeterFeet, oldRadioAltimeterFeet, f), 0.0F, 5000F, 254F, 336F), 0.0F, 0.0F);
        else
            mesh.chunkSetAngles("Z_Altimeter3", 336F, 0.0F, 0.0F);

        mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Second1", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Climb1", floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Compass1", setNew.azimuth.getDeg(f) + 180F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Compass3", -setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Azimuth_2", setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F, 0.0F);
        w.set(((FlightModelMain) (fm)).getW());
        ((FlightModelMain) (fm)).Or.transform(w);
        mesh.chunkSetAngles("Z_horizont1", 0.0F, 0.0F, ((FlightModelMain) (fm)).Or.getKren());
        mesh.chunkSetAngles("Z_horizont1b", 0.0F, ((FlightModelMain) (fm)).Or.getTangage(), 0.0F);
        mesh.chunkSetAngles("Z_horizont1a", setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_horizont2", 0.0F, 0.0F, ((FlightModelMain) (fm)).Or.getKren());
        mesh.chunkSetAngles("Z_horizont2b", 0.0F, ((FlightModelMain) (fm)).Or.getTangage(), 0.0F);
        mesh.chunkSetAngles("zBall", 0.0F, cvt(getBall(6D), -6F, 6F, -9F, 9F), 0.0F);
        Cockpit.xyz[0] = -cvt(setNew.ilsLoc, -63F, 63F, -0.036F, 0.036F);
        Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        Cockpit.ypr[0] = cvt(((FlightModelMain) (fm)).Or.getKren(), -35F, 35F, -35F, 35F);
        Cockpit.ypr[1] = Cockpit.ypr[2] = 0.0F;
        mesh.chunkSetLocate("BL_Vert", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = -cvt(setNew.ilsGS, -0.5F, 0.5F, -0.036F, 0.036F);
        Cockpit.xyz[0] = Cockpit.xyz[2] = 0.0F;
        Cockpit.ypr[0] = cvt(((FlightModelMain) (fm)).Or.getKren(), -35F, 35F, -35F, 35F);
        Cockpit.ypr[1] = Cockpit.ypr[2] = 0.0F;
        mesh.chunkSetLocate("BL_Horiz", Cockpit.xyz, Cockpit.ypr);
        if(((FlightModelMain) (fm)).AS.isAAFIAS)
            mesh.chunkVisible("BL_Light", isOnBlindLandingMarker());
        mesh.chunkSetAngles("Z_AOA", cvt(30F - AOAunits, 0F, 30F, 0.0F, 270F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Accello1", cvt(((FlightModelMain) (fm)).getOverload(), -5F, 10F, -220F, 114F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Accello2", cvt(setNew.accelloMax, -5F, 10F, -220F, 114F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Accello3", cvt(setNew.accelloMin, -5F, 10F, -220F, 114F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Exhaust_Temp1", cvt(((FlightModelMain) (fm)).EI.engines[0].tWaterOut, 90F, 1200F, -224.0F, 51.428F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Exhaust_Temp2", cvt(((FlightModelMain) (fm)).EI.engines[1].tWaterOut, 90F, 1200F, -224.0F, 51.428F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Exhaust_Temp1s", cvt((((FlightModelMain) (fm)).EI.engines[0].tWaterOut / 10), 0F, 120F, 0.0F, 43200F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Exhaust_Temp2s", cvt((((FlightModelMain) (fm)).EI.engines[1].tWaterOut / 10), 0F, 120F, 0.0F, 43200F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RPM1", cvt(((FlightModelMain) (fm)).EI.engines[0].getRPM() / 41F, 0.0F, 100F, 0.0F, 270F),0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RPM2", cvt(((FlightModelMain) (fm)).EI.engines[1].getRPM() / 41F, 0.0F, 100F, 0.0F, 270F),0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RPM1s", cvt(((FlightModelMain) (fm)).EI.engines[0].getRPM() / 41F, 0.0F, 100F, 0.0F, 3600F),0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RPM2s", cvt(((FlightModelMain) (fm)).EI.engines[1].getRPM() / 41F, 0.0F, 100F, 0.0F, 3600F),0.0F, 0.0F);
        mesh.chunkSetAngles("Z_GasPressureL", cvt(((FlightModelMain) (fm)).M.fuel <= 1.0F ? 0.0F : 0.6F * ((FlightModelMain) (fm)).EI.engines[0].getPowerOutput(), 0.0F, 1.0F, 0.0F, 273.5F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_GasPressureR", cvt(((FlightModelMain) (fm)).M.fuel <= 1.0F ? 0.0F : 0.6F * ((FlightModelMain) (fm)).EI.engines[1].getPowerOutput(), 0.0F, 1.0F, 0.0F, 273.5F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_GasTempL", cvt(((FlightModelMain) (fm)).EI.engines[0].tWaterOut, 300F, 1000F, 0.0F, 96F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_GasTempR", cvt(((FlightModelMain) (fm)).EI.engines[1].tWaterOut, 300F, 1000F, 0.0F, 96F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_OilPressureL", cvt(1.0F + 0.005F * ((FlightModelMain) (fm)).EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 278F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_OilPressureR", cvt(1.0F + 0.005F * ((FlightModelMain) (fm)).EI.engines[1].tOilOut, 0.0F, 10F, 0.0F, 278F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_FuelPressL", cvt((interp(setNew.fuelpressl, setOld.fuelpressl, f)), 0.0F, 160F, 0.0F, 278F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_FuelPressR", cvt((interp(setNew.fuelpressr, setOld.fuelpressr, f)), 0.0F, 160F, 0.0F, 278F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Nozzle1", cvt(-setNew.nozzlel, -100F, 0.0F, -51F, 51F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Nozzle2", cvt(-setNew.nozzler, -100F, 0.0F, -51F, 51F), 0.0F, 0.0F);
        float fuel_inlbs = ((FlightModelMain) (fm)).M.fuel * 2.20463F;
        mesh.chunkSetAngles("Z_Fuel", cvt(fuel_inlbs, 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelnum_10", cvt(((float)(Math.floor(fuel_inlbs / 10F) % 10)), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelnum_100", cvt(((float)(Math.floor(fuel_inlbs / 100F) % 10)), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelnum_1000", cvt(((float)(Math.floor(fuel_inlbs / 1000F) % 10)), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelnum_10000", cvt(((float)(Math.floor(fuel_inlbs / 10000F) % 10)), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkVisible("Z_EngineFireL", ((FlightModelMain) (fm)).AS.astateEngineStates[0] > 3);
        mesh.chunkVisible("Z_EngineFireR", ((FlightModelMain) (fm)).AS.astateEngineStates[1] > 3);
        mesh.chunkVisible("Z_EngineOverheatL", ((FlightModelMain) (fm)).AS.astateEngineStates[0] > 1 || ((FlightModelMain) (fm)).EI.engines[0].tWaterOut > ((FlightModelMain) (fm)).EI.engines[0].tWaterCritMax);
        mesh.chunkVisible("Z_EngineOverheatR", ((FlightModelMain) (fm)).AS.astateEngineStates[1] > 1 || ((FlightModelMain) (fm)).EI.engines[1].tWaterOut > ((FlightModelMain) (fm)).EI.engines[1].tWaterCritMax);
        if(((FlightModelMain) (fm)).CT.getGear() == 1.0F && ((FlightModelMain) (fm)).CT.FlapsControl > 0.2F)
        {
            mesh.chunkVisible("Z_AOALanding1", AOAunits > 19.499F);
            mesh.chunkVisible("Z_AOALanding2", AOAunits < 20.100F && AOAunits > 17.900F);
            mesh.chunkVisible("Z_AOALanding3", AOAunits < 18.500F);
        }
        else
        {
            mesh.chunkVisible("Z_AOALanding1", false);
            mesh.chunkVisible("Z_AOALanding2", false);
            mesh.chunkVisible("Z_AOALanding3", false);
        }
    }

    public void reflectCockpitState()
    {
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("HullDamage2", true);
            mesh.chunkVisible("XGlassDamage4", true);
            mesh.chunkVisible("Speedometer1", false);
            mesh.chunkVisible("Speedometer1_D1", true);
            mesh.chunkVisible("Z_Speedometer1", false);
            mesh.chunkVisible("Z_Speedometer2", false);
            mesh.chunkVisible("RPML", false);
            mesh.chunkVisible("RPML_D1", true);
            mesh.chunkVisible("Z_RPML", false);
            mesh.chunkVisible("FuelRemainV", false);
            mesh.chunkVisible("FuelRemainV_D1", true);
            mesh.chunkVisible("Z_FuelRemainV", false);
        }
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("HullDamage4", true);
            mesh.chunkVisible("XGlassDamage3", true);
            mesh.chunkVisible("Altimeter1", false);
            mesh.chunkVisible("Altimeter1_D1", true);
            mesh.chunkVisible("Z_Altimeter1", false);
            mesh.chunkVisible("Z_Altimeter3", false);
            mesh.chunkVisible("GasPressureL", false);
            mesh.chunkVisible("GasPressureL_D1", true);
            mesh.chunkVisible("Z_GasPressureL", false);
        }
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("HullDamage1", true);
            mesh.chunkVisible("XGlassDamage4", true);
            mesh.chunkVisible("RPMR", false);
            mesh.chunkVisible("RPMR_D1", true);
            mesh.chunkVisible("Z_RPMR", false);
            mesh.chunkVisible("FuelPressR", false);
            mesh.chunkVisible("FuelPressR_D1", true);
            mesh.chunkVisible("Z_FuelPressR", false);
        }
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("HullDamage3", true);
            mesh.chunkVisible("XGlassDamage3", true);
            mesh.chunkVisible("GasPressureR", false);
            mesh.chunkVisible("GasPressureR_D1", true);
            mesh.chunkVisible("Z_GasPressureR", false);
        }
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 1) != 0)
        {
            mesh.chunkVisible("XGlassDamage1", true);
            mesh.chunkVisible("XGlassDamage2", true);
            mesh.chunkVisible("Climb", false);
            mesh.chunkVisible("Climb_D1", true);
            mesh.chunkVisible("Z_Climb1", false);
            mesh.chunkVisible("FuelPressR", false);
            mesh.chunkVisible("FuelPressR_D1", true);
            mesh.chunkVisible("Z_FuelPressR", false);
        }
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 2) != 0)
        {
            mesh.chunkVisible("XGlassDamage1", true);
            mesh.chunkVisible("HullDamage1", true);
            mesh.chunkVisible("HullDamage2", true);
            mesh.chunkVisible("Revi_D0", false);
            mesh.chunkVisible("Revi_D1", true);
            mesh.chunkVisible("Z_Z_RETICLE", false);
            mesh.chunkVisible("Z_Z_MASK", false);
            mesh.chunkVisible("FuelPressL", false);
            mesh.chunkVisible("FuelPressL_D1", true);
            mesh.chunkVisible("Z_FuelPressL", false);
        }
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("HullDamage1", true);
            mesh.chunkVisible("Altimeter1", false);
            mesh.chunkVisible("Altimeter1_D1", true);
            mesh.chunkVisible("Z_Altimeter1", false);
            mesh.chunkVisible("Z_Altimeter3", false);
            mesh.chunkVisible("Climb", false);
            mesh.chunkVisible("Climb_D1", true);
            mesh.chunkVisible("Z_Climb1", false);
            mesh.chunkVisible("AFN", false);
            mesh.chunkVisible("AFN_D1", true);
            mesh.chunkVisible("Z_AFN1", false);
            mesh.chunkVisible("Z_AFN2", false);
            mesh.chunkVisible("FuelPressL", false);
            mesh.chunkVisible("FuelPressL_D1", true);
            mesh.chunkVisible("Z_FuelPressL", false);
            mesh.chunkVisible("FuelRemainIn", false);
            mesh.chunkVisible("FuelRemainIn_D1", true);
            mesh.chunkVisible("Z_FuelRemainIn", false);
        }
        ((FlightModelMain) (fm)).AS.getClass();
        retoggleLight();
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleDim()
    {
        cockpitDimControl = !cockpitDimControl;
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

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private Point3d tmpP;
    private Vector3d tmpV;
    private Gun gun[];
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private boolean bU4;
    private static final float speedometerIndScale[] = {
          0.00F,   2.41F,   4.82F,  10.36F,  16.14F,  23.44F,  35.11F,  47.26F,  60.07F,  73.80F,
         87.14F,  99.47F, 111.75F, 123.91F, 135.64F, 142.87F, 150.10F, 157.33F, 164.56F, 171.81F,
        179.09F, 186.37F, 193.65F, 200.93F, 206.65F, 212.30F, 217.94F, 223.59F, 229.03F, 234.10F,
        239.17F, 244.24F, 249.31F, 253.44F, 257.23F, 261.03F, 264.83F, 268.63F, 272.49F, 276.34F,
        280.20F, 284.06F, 287.80F, 291.43F, 295.07F, 298.71F, 302.34F, 305.49F, 308.60F, 311.70F,
        314.80F, 317.84F, 320.74F, 323.65F, 326.55F, 329.45F, 332.35F
      };
    private static final float machmeterScale[] = {
        -103.6F, -74.6F, -51.0F, -35.4F, -19.1F, 0.0F
    };
    private static final float variometerScale[] = {
        -170F, -160F, -145F, -125F, -88F, -55F, 0.0F, 55F, 88F, 125F, 
        145F, 160F, 170F
    };
    private static final float rpmScale[] = {
        0.0F, 0.0F, 0.0F, 16.5F, 34.5F, 55F, 77.5F, 104F, 133.5F, 162.5F, 
        192F, 224F, 254F, 255.5F, 260F
    };


}