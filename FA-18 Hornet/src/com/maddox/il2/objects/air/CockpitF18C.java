// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10/3/2012 3:09:47 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitT4.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Time;
import com.maddox.sound.*;
import java.util.ArrayList;
import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, AircraftLH, F_18, Aircraft, 
//            Cockpit, TypeSupersonic

public class CockpitF18C extends CockpitPilot
{
    private class Variables
    {

        float altimeter;
        float throttlel;
        float throttler;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float PDI;
        float ilsLoc;
        float ilsGS;
        float navDiviation0;
        float navDiviation1;
        boolean navTo0;
        boolean navTo1;
        float navDist0;
        float navDist1;
        float vspeed;
        float vspeed2;
        float dimPosition;
        float beaconDirection;
        float beaconRange;
        float pitch;
        float bank;
        float fpmPitch;
        float fpmYaw;
        boolean isGeneratorAllive;
        boolean isBatteryOn;
        float k14wingspan;
        float k14mode;
        float k14x;
        float k14y;
        float k14w;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
            isGeneratorAllive = false;
            isBatteryOn = false;
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
            aircraft().hierMesh().chunkVisible("Canopy_D0", false);
            aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            aircraft().hierMesh().chunkVisible("Capglass", false);
            aircraft().hierMesh().chunkVisible("Capglass2", false);
            aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
            aircraft().hierMesh().chunkVisible("Seat1_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Canopy_D0", true);
            aircraft().hierMesh().chunkVisible("Blister1_D0", true);
            aircraft().hierMesh().chunkVisible("Capglass", true);
            aircraft().hierMesh().chunkVisible("Capglass2", true);
            aircraft().hierMesh().chunkVisible("Pilot1_D0", true);
            aircraft().hierMesh().chunkVisible("Seat1_D0", true);
        super.doFocusLeave();
    }


    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.altimeter = fm.getAltitude();
            setNew.throttlel = (10F * setOld.throttlel + ((FlightModelMain) (fm)).EI.engines[0].getControlThrottle()) / 11F;
            setNew.throttler = (10F * setOld.throttler + ((FlightModelMain) (fm)).EI.engines[1].getControlThrottle()) / 11F;
            float f = ((F_18S)aircraft()).k14Distance;
            setNew.k14w = (5F * CockpitF18C.k14TargetWingspanScale[((F_18S)aircraft()).k14WingspanType]) / f;
            setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
            setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitF18C.k14TargetMarkScale[((F_18S)aircraft()).k14WingspanType];
            setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float)((F_18S)aircraft()).k14Mode;
            com.maddox.JGP.Vector3d vector3d = ((SndAircraft) (aircraft())).FM.getW();
            double d = 0.00125D * (double)f;
            float f1 = (float)Math.toDegrees(d * ((Tuple3d) (vector3d)).z);
            float f2 = -(float)Math.toDegrees(d * ((Tuple3d) (vector3d)).y);
            float f3 = floatindex((f - 200F) * 0.04F, CockpitF18C.k14BulletDrop) - CockpitF18C.k14BulletDrop[0];
            f2 += (float)Math.toDegrees(Math.atan(f3 / f));
            setNew.k14x = 0.92F * setOld.k14x + 0.08F * f1;
            setNew.k14y = 0.92F * setOld.k14y + 0.08F * f2;
            if(setNew.k14x > 7F)
                setNew.k14x = 7F;
            if(setNew.k14x < -7F)
                setNew.k14x = -7F;
            if(setNew.k14y > 7F)
                setNew.k14y = 7F;
            if(setNew.k14y < -7F)
                setNew.k14y = -7F;
            f = waypointAzimuth();
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
            if(useRealisticNavigationInstruments())
            {
                setNew.waypointAzimuth.setDeg(f - 90F);
                setOld.waypointAzimuth.setDeg(f - 90F);
                setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
                if(((FlightModelMain) (fm)).AS.listenLorenzBlindLanding)
                {
                    setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                    setNew.ilsGS = (10F * setOld.ilsGS + getGlidePath()) / 11F;
                    setNew.navDiviation0 = setNew.ilsLoc;
                } else
                {
                    setNew.ilsLoc = 0.0F;
                    setNew.ilsGS = 0.0F;
                    setNew.navDiviation0 = normalizeDegree(normalizeDegree(waypointAzimuth(0.02F) + 90F) - normalizeDegree(getNDBDirection()));
                    if(Math.abs(setNew.navDiviation0) < 90F)
                        setNew.navTo0 = true;
                    else
                    if(setNew.navDiviation0 > 270F)
                    {
                        setNew.navTo0 = true;
                        setNew.navDiviation0 = setNew.navDiviation0 - 360F;
                    } else
                    {
                        setNew.navTo0 = false;
                        setNew.navDiviation0 = normalizeDegree180(setNew.navDiviation0);
                        if(setNew.navDiviation0 >= 90F)
                            setNew.navDiviation0 = 180F - setNew.navDiviation0;
                        else
                            setNew.navDiviation0 = -180F - setNew.navDiviation0;
                    }
                }
            } else
            {
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f);
                setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(0.1F) - 90F);
            }
            setNew.vspeed = (299F * setOld.vspeed + fm.getVertSpeed()) / 300F;
            setNew.vspeed2 = (49F * setOld.vspeed2 + fm.getVertSpeed()) / 50F;
            setNew.pitch = ((FlightModelMain) (fm)).Or.getPitch();
            setNew.bank = ((FlightModelMain) (fm)).Or.getRoll();
            Vector3d vector3d1 = new Vector3d();
            ((FlightModelMain) (fm)).Or.transformInv(((FlightModelMain) (fm)).Vwld, vector3d1);
            setNew.fpmPitch = FMMath.RAD2DEG(-(float)Math.atan2(((Tuple3d) (vector3d1)).z, ((Tuple3d) (vector3d1)).x));
            setNew.fpmYaw = FMMath.RAD2DEG((float)Math.atan2(((Tuple3d) (vector3d1)).y, ((Tuple3d) (vector3d1)).x));
            if(cockpitDimControl)
            {
                if(setNew.dimPosition > 0.0F)
                    setNew.dimPosition = setOld.dimPosition - 0.05F;
            } else
            if(setNew.dimPosition < 1.0F)
                setNew.dimPosition = setOld.dimPosition + 0.05F;
            if(((FlightModelMain) (fm)).EI.engines[0].getRPM() > 200F || ((FlightModelMain) (fm)).EI.engines[1].getRPM() > 200F)
                setNew.isGeneratorAllive = true;
            else
                setNew.isGeneratorAllive = false;
            return true;
        }

        Interpolater()
        {
        }
    }

    private class ClipBoard
    {

        public int setPage(int i)
        {
            hidePages();
            if(i < 1)
                page_ = 1;
            else
            if(i > 10)
                page_ = 10;
            else
                page_ = i;
            mesh.chunkVisible("Int_ClipBoardP" + page_, true);
            return page_;
        }

        public void hidePages()
        {
            for(int i = 1; i <= 10; i++)
                mesh.chunkVisible("Int_ClipBoardP" + i, false);

        }

        public void clipBoardShowHide(boolean flag)
        {
            if(flag)
            {
                Cockpit.xyz[0] = 0.05F;
                Cockpit.xyz[1] = 0.255F;
                Cockpit.xyz[2] = -0.05F;
                mesh.chunkSetLocate("Int_ClipBoard", Cockpit.xyz, Cockpit.ypr);
            } else
            {
                Cockpit.xyz[0] = 0.0F;
                Cockpit.xyz[1] = 0.0F;
                Cockpit.xyz[2] = 0.0F;
                mesh.chunkSetLocate("Int_ClipBoard", Cockpit.xyz, Cockpit.ypr);
            }
        }

        int page_;
        public static final int maxPage_ = 10;

        private ClipBoard()
        {
            page_ = 1;
            hidePages();
            mesh.chunkVisible("Int_ClipBoardP1", true);
        }

        ClipBoard(ClipBoard clipboard)
        {
            this();
        }
    }


    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(10F);
    }

    public CockpitF18C()
    {
        super("3DO/Cockpit/T-4/hier.him", "bf109");
        gun = new Gun[4];
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        tmpP = new Point3d();
        tmpV = new Vector3d();
        tmpMat = new Matrix3d();
        bNeedSetUp = true;
        isDimmer = false;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        blinkCounter = 0;
        bU4 = false;
        super.cockpitNightMats = (new String[] {
            "2petitsb_d1", "2petitsb", "aiguill1", "badinetm_d1", "badinetm", "baguecom", "brasdele", "comptemu_d1", "comptemu", "petitfla_d1", 
            "petitfla", "turnbank"
        });
        setNightMats(false);
        setNew.dimPosition = 1.0F;
        interpPut(new Interpolater(), null, Time.current(), null);
        if(super.acoustics != null)
            super.acoustics.globFX = new ReverbFXRoom(0.05F);
        aoaWarnFX = aircraft().newSound("aircraft.AOA_Slow", false);
        aoaWarnFX2 = aircraft().newSound("aircraft.AOA_Mid", false);
        aoaWarnFX3 = aircraft().newSound("aircraft.AOA_Fast", false);
        aoaWarnFX4 = aircraft().newSound("aircraft.AOA_Stall", false);
        clipBoard = new ClipBoard(null);
        hudPitchRudderStr = new String[37];
        for(int i = 18; i >= 0; i--)
            hudPitchRudderStr[i] = "Z_Z_HUD_PITCH" + (90 - i * 5);

        for(int j = 1; j <= 18; j++)
            hudPitchRudderStr[j + 18] = "Z_Z_HUD_PITCHN" + j * 5;

        ((AircraftLH)aircraft()).bWantBeaconKeys = true;
    }

    public void reflectWorldToInstruments(float f)
    {
    	if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) == 0)
        {          
            int i = ((F_18S)aircraft()).k14Mode;
            resetYPRmodifier();
            Cockpit.xyz[0] = setNew.k14w;
            if(i == 1)
            {
                super.mesh.chunkSetAngles("Z_Z_RETICLE", -setNew.k14x, -setNew.k14y, 0.0F);
            }
            if(i == 0)
            {
                super.mesh.chunkSetAngles("Z_Z_RETICLE", 0.0F, 0.0F, 0.0F);
            }
        }
    	if(bNeedSetUp)
        {
            super.mesh.chunkVisible("Int_Marker", false);
            bNeedSetUp = false;
        }
        ((AircraftLH)aircraft()).bWantBeaconKeys = true;
        AircraftLH aircraftlh = (AircraftLH)aircraft();
        AircraftLH.printCompassHeading = true;
        resetYPRmodifier();
        moveControls(f);
        moveHUD(f);
        moveGauge(f);
        drawSound(f);
        if(((FlightModelMain) (super.fm)).AS.bShowSmokesOn)
            super.mesh.chunkVisible("Int_SmokeON", true);
        else
            super.mesh.chunkVisible("Int_SmokeON", false);
        if(((FlightModelMain) (super.fm)).CT.getAirBrake() > 0.0F)
            super.mesh.chunkVisible("Int_Speed_Ext", true);
        else
            super.mesh.chunkVisible("Int_Speed_Ext", false);
        if(((FlightModelMain) (super.fm)).CT.getGear() < 0.999999F)
            super.mesh.chunkVisible("Int_LDG_ON", false);
        else
            super.mesh.chunkVisible("Int_LDG_ON", true);
        super.mesh.chunkVisible("Int_Marker", isDimmer);
        ((F_18S)aircraft()).clipBoardPage_ = clipBoard.setPage(((F_18S)aircraft()).clipBoardPage_);
        clipBoard.clipBoardShowHide(((F_18S)aircraft()).showClipBoard_);
        ((FlightModelMain) (super.fm)).Or.getMatrix(tmpMat);
        float f1 = (float)tmpMat.getElement(2, 0);
        f1 = 1.0F - f1;
        f1 *= 0.045F;
        f1 += (float)tmpMat.getElement(2, 2) * -0.014F;
        if(f1 < 0.0F)
            f1 = 0.0F;
        resetYPRmodifier();
        Cockpit.xyz[2] = f1;
        super.mesh.chunkSetLocate("ShadowMove1", Cockpit.xyz, Cockpit.ypr);
    }

    protected void moveControls(float f)
    {
        super.mesh.chunkSetAngles("FlightStick", ((FlightModelMain) (super.fm)).CT.AileronControl * 10F, 0.0F, ((FlightModelMain) (super.fm)).CT.ElevatorControl * 10F);
        super.mesh.chunkSetAngles("Throttole", 0.0F, 0.0F, ((FlightModelMain) (super.fm)).CT.PowerControl * -50F);
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = 0.0F;
        Cockpit.xyz[2] = ((FlightModelMain) (super.fm)).CT.getRudder() * -0.07F;
        super.mesh.chunkSetLocate("FootPedal_L", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = ((FlightModelMain) (super.fm)).CT.getRudder() * 0.07F;
        super.mesh.chunkSetLocate("FootPedal_R", Cockpit.xyz, Cockpit.ypr);
    }

    protected void moveGauge(float f)
    {
        super.mesh.chunkSetAngles("Gauge_ADI1", setNew.bank, 0.0F, setNew.pitch);
        super.mesh.chunkSetAngles("Gauge_StdByADI1", setNew.bank, 0.0F, setNew.pitch);
        super.mesh.chunkSetAngles("GaugeMove_RPM_L", floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 3828F, 0.0F, 300F) / 100F, rpmScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GaugeMove_RPM_R", floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 0.0F, 3828F, 0.0F, 300F) / 100F, rpmScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GaugeMove_TGT_L", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 200F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GaugeMove_TGT_R", cvt(((FlightModelMain) (super.fm)).EI.engines[1].tOilOut, 0.0F, 100F, 0.0F, 200F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GaugeMove_FF_L", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 3828F, 0.0F, 330F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GaugeMove_FF_R", cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 0.0F, 3828F, 0.0F, 330F), 0.0F, 0.0F);
        float f1 = interp(setNew.altimeter, setOld.altimeter, f);
        float f2 = cvt(f1, 0.0F, 30480F, 0.0F, 36000F);
        super.mesh.chunkSetAngles("GaugeMove_ALT_N", f2, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GaugeMove_ALT_3", 0.0F, 0.0F, -f2);
        f2 = cvt(f1, 0.0F, 30480F, 0.0F, 3600F);
        float f3 = (float)(int)(f2 / 36F) * 36F;
        if(f2 - f3 > 34.2F)
            f3 += (1.0F - ((f3 + 36F) - f2) / 1.8F) * 36F;
        super.mesh.chunkSetAngles("GaugeMove_ALT_4", 0.0F, 0.0F, -f3);
        f2 = cvt(f1, 0.0F, 30480F, 0.0F, 360F);
        f3 = (float)(int)(f2 / 36F) * 36F;
        if(f2 - f3 > 35.82F)
            f3 += (1.0F - ((f3 + 36F) - f2) / 0.18F) * 36F;
        super.mesh.chunkSetAngles("GaugeMove_ALT_5", 0.0F, 0.0F, -f3);
        f2 = setNew.azimuth.getDeg(f) + 90F;
        super.mesh.chunkSetAngles("GaugeMove_HSI_HDG", -f2, 0.0F, 0.0F);
        f2 = setNew.waypointAzimuth.getDeg(f) + 90F;
        super.mesh.chunkSetAngles("GaugeMove_HSI_CRS", f2, 0.0F, 0.0F);
        f2 = setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F + 180F;
        super.mesh.chunkSetAngles("GaugeMove_HSI_TACAN", f2, 0.0F, 0.0F);
        resetYPRmodifier();
        if(((FlightModelMain) (super.fm)).AS.listenLorenzBlindLanding)
        {
            f2 = setNew.ilsLoc;
            Cockpit.xyz[0] = -f2 * 0.001F;
        } else
        if(((FlightModelMain) (super.fm)).AS.listenNDBeacon || ((FlightModelMain) (super.fm)).AS.listenRadioStation)
        {
            f2 = setNew.navDiviation0;
            Cockpit.xyz[0] = -f2 * 0.002F;
        }
        if(Cockpit.xyz[0] < -0.02F)
            Cockpit.xyz[0] = -0.02F;
        else
        if(Cockpit.xyz[0] > 0.02F)
            Cockpit.xyz[0] = 0.02F;
        super.mesh.chunkSetLocate("GaugeMove_HSI_DIV", Cockpit.xyz, Cockpit.ypr);
        float f4 = 0.0F;
        float f5 = calculateMach();
        f4 = f5 * 180F;
        super.mesh.chunkSetAngles("GaugeMove_SPD_MACH", f4, 0.0F, 0.0F);
        f4 = 0.0F;
        float f6 = Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH());
        f6 *= 0.53996F;
        if(f6 <= 50F)
            f4 = (f6 / 50F) * 15F;
        else
        if(f6 <= 100F)
            f4 = (f6 - 50F) + 15F;
        else
        if(f6 <= 150F)
            f4 = (f6 - 100F) * 1.2F + 65F;
        else
        if(f6 <= 200F)
            f4 = (f6 - 150F) * 0.9F + 125F;
        else
        if(f6 <= 300F)
            f4 = (f6 - 200F) * 0.6F + 170F;
        else
        if(f6 <= 400F)
            f4 = (f6 - 300F) * 0.45F + 230F;
        else
        if(f6 <= 700F)
            f4 = (f6 - 400F) * 0.3F + 275F;
        super.mesh.chunkSetAngles("GaugeMove_SPD", f4, 0.0F, 0.0F);
        float f7 = setNew.vspeed2 * 3.48F;
        f7 *= 60F;
        float f8 = Math.abs(f7);
        boolean flag = f7 < 0.0F;
        if(f8 <= 1000F)
            f2 = f8 * 0.07F;
        else
        if(f8 <= 2000F)
            f2 = (f8 - 1000F) * 0.035F + 70F;
        else
        if(f8 <= 4000F)
            f2 = (f8 - 2000F) * 0.0175F + 105F;
        else
        if(f8 <= 6000F)
            f2 = (f8 - 4000F) * 0.00875F + 140F;
        else
            f2 = 157.5F;
        if(flag)
            f2 = -f2;
        super.mesh.chunkSetAngles("GaugeMove_VS", f2, 0.0F, 0.0F);
        f2 = super.fm.getAOA() * 9F;
        if(f2 < 0.0F)
            f2 = 0.0F;
        else
        if(f2 > 270F)
            f2 = 270F;
        f2 *= -1F;
        super.mesh.chunkSetAngles("GaugeMove_AOA", f2, 0.0F, 0.0F);
        float f9 = super.fm.getOverload();
        f9--;
        f2 = f9 * 20F;
        super.mesh.chunkSetAngles("GaugeMove_G", f2, 0.0F, 0.0F);
        f2 = ((FlightModelMain) (super.fm)).M.fuel / ((FlightModelMain) (super.fm)).M.maxFuel;
        f2 *= 220F;
        super.mesh.chunkSetAngles("GaugeMove_FUEL_L", f2, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GaugeMove_FUEL_R", f2, 0.0F, 0.0F);
        if(((FlightModelMain) (super.fm)).CT.getGear() > 0.0F)
            super.mesh.chunkSetAngles("GaugeMove_Gear", 0.0F, 0.0F, 0.0F);
        else
            super.mesh.chunkSetAngles("GaugeMove_Gear", 0.0F, 0.0F, -15.5F);
    }

    protected void moveHUD(float f)
    {
        blinkCounter++;
        float f1 = Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH());
        f1 *= 0.53996F;
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = 0.001613948F * f1;
        Cockpit.xyz[2] = 0.0F;
        boolean flag = false;
        boolean flag1 = false;
        if(!setNew.isBatteryOn && !setNew.isGeneratorAllive)
            flag1 = false;
        else
            flag1 = true;
        for(int i = 0; i < 37; i++)
            super.mesh.chunkVisible(hudPitchRudderStr[i], false);

        super.mesh.chunkVisible("Z_Z_HUD_PITCHN3", false);
        super.mesh.chunkVisible("Z_Z_ALT_1", flag1);
        super.mesh.chunkVisible("Z_Z_ALT_2", flag1);
        super.mesh.chunkVisible("Z_Z_ALT_3", flag1);
        super.mesh.chunkVisible("Z_Z_ALT_4", flag1);
        super.mesh.chunkVisible("Z_Z_ALT_5", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_ALT_CY", flag1);
        super.mesh.chunkVisible("Z_Z_G_1", flag1);
        super.mesh.chunkVisible("Z_Z_G_2", flag1);
        super.mesh.chunkVisible("Z_Z_HDG_3", flag1);
        super.mesh.chunkVisible("Z_Z_HDG_2", flag1);
        super.mesh.chunkVisible("Z_Z_HDG_1", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_HDG", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_Mach_3", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_Mach_2", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_Mach_1", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_SPD", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_HIDE1", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_LOWER_ROLL", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_MISC", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_FPM", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_FPM_NO", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_VS", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_VSBG", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_AOABG", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_AOA", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_BANK", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_DST_0", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_DST_1", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_DST_2", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_DST_3", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_DIR", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_DIR_BG", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_GS", flag1);
        super.mesh.chunkVisible("Z_Z_RETICLE", flag1);
        super.mesh.chunkVisible("Z_Z_RETICLECROSS", flag1);
        if(!flag1)
            return;
        super.mesh.chunkSetLocate("Z_Z_HUD_SPD", Cockpit.xyz, Cockpit.ypr);
        float f2 = setNew.pitch;
        if(f2 > 90F)
            for(; f2 > 90F; f2 -= 360F);
        else
        if(f2 < -90F)
            for(; f2 < -90F; f2 += 360F);
        f2 -= 90F;
        f2 = -f2;
        int j = (int)f2 / 5;
        for(int k = j - 3; k <= j + 2; k++)
            if(k >= 0 && k < 37)
            {
                super.mesh.chunkVisible(hudPitchRudderStr[k], true);
                super.mesh.chunkSetAngles(hudPitchRudderStr[k], setNew.bank, 0.0F, -setNew.pitch);
            }

        if(((FlightModelMain) (super.fm)).CT.getGear() < 0.999999F)
        {
            super.mesh.chunkVisible("Z_Z_HUD_PITCHN3", false);
        } else
        {
            super.mesh.chunkSetAngles("Z_Z_HUD_PITCHN3", setNew.bank, 0.0F, -setNew.pitch);
            super.mesh.chunkVisible("Z_Z_HUD_PITCHN3", true);
        }
        Cockpit.xyz[0] = 0.0F;
        boolean flag2 = false;
        if(((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1 || f1 > 10F)
        {
            Cockpit.xyz[1] = -setNew.fpmYaw;
            if(Cockpit.xyz[1] < -6.5F)
            {
                Cockpit.xyz[1] = -6.5F;
                flag2 = true;
            } else
            if(Cockpit.xyz[1] > 6.5F)
            {
                Cockpit.xyz[1] = 6.5F;
                flag2 = true;
            }
            Cockpit.xyz[2] = -setNew.fpmPitch;
            if(Cockpit.xyz[2] < -11.5F)
            {
                Cockpit.xyz[2] = -11.5F;
                flag2 = true;
            } else
            if(Cockpit.xyz[2] > 7F)
            {
                Cockpit.xyz[2] = 7F;
                flag2 = true;
            }
        } else
        {
            Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        }
        super.mesh.chunkSetAngles("Z_Z_HUD_FPM", 0.0F, Cockpit.xyz[1], Cockpit.xyz[2]);
        super.mesh.chunkVisible("Z_Z_HUD_FPM_NO", flag2);
        float f3 = normalizeDegree(setNew.bank);
        super.mesh.chunkSetAngles("Z_Z_HUD_BANK", f3, 0.0F, 0.0F);
        if(f3 > 90F && f3 < 270F)
            super.mesh.chunkVisible("Z_Z_HUD_LOWER_ROLL", true);
        else
            super.mesh.chunkVisible("Z_Z_HUD_LOWER_ROLL", false);
        float f7 = setNew.altimeter;
        f3 = cvt(f7, 0.0F, 30480F, 0.0F, 3600000F);
        f3 = 0.0F;
        super.mesh.chunkSetAngles("Z_Z_ALT_1", 0.0F, 0.0F, f3);
        f3 = cvt(f7, 0.0F, 30480F, 0.0F, 360000F);
        f3 = (float)(int)(f3 / 36F) * 36F;
        super.mesh.chunkSetAngles("Z_Z_ALT_2", 0.0F, 0.0F, f3);
        f3 = cvt(f7, 0.0F, 30480F, 0.0F, 36000F);
        f3 = (float)(int)(f3 / 36F) * 36F;
        super.mesh.chunkSetAngles("Z_Z_ALT_3", 0.0F, 0.0F, f3);
        f3 = cvt(f7, 0.0F, 30480F, 0.0F, 3600F);
        f3 = (float)(int)(f3 / 36F) * 36F;
        super.mesh.chunkSetAngles("Z_Z_ALT_4", 0.0F, 0.0F, f3);
        f3 = cvt(f7, 0.0F, 30480F, 0.0F, 1800F);
        if(f3 <= 360F)
        {
            super.mesh.chunkSetAngles("Z_Z_HUD_ALT_CY", 0.0F, 0.0F, f3);
            super.mesh.chunkVisible("Z_Z_HUD_ALT_CY", true);
        } else
        {
            super.mesh.chunkVisible("Z_Z_HUD_ALT_CY", false);
        }
        f3 = cvt(f7, 0.0F, 30480F, 0.0F, 360F);
        f3 = (float)(int)(f3 / 36F) * 36F;
        super.mesh.chunkSetAngles("Z_Z_ALT_5", 0.0F, 0.0F, f3);
        float f8 = super.fm.getOverload();
        f3 = (float)(int)Math.abs(f8) * 36F;
        super.mesh.chunkSetAngles("Z_Z_G_1", 0.0F, 0.0F, f3);
        f3 = (float)((int)(Math.abs(f8) * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_G_2", 0.0F, 0.0F, f3);
        float f9 = normalizeDegree(setNew.azimuth.getDeg(f) + 90F);
        super.mesh.chunkSetAngles("Z_Z_HUD_HDG", 0.0F, f9, 0.0F);
        f9 += 0.5F;
        if(f9 > 360F)
            f9 = 0.0F;
        f3 = (float)(int)(f9 / 100F) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDG_1", 0.0F, 0.0F, f3);
        f3 = (float)(((int)f9 % 100) / 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDG_2", 0.0F, 0.0F, f3);
        f3 = (float)((int)f9 % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDG_3", 0.0F, 0.0F, f3);
        f3 = super.fm.getAOA();
        flag = false;
        if(f3 < 0.0F)
        {
            f3 = 0.0F;
            flag = true;
        } else
        if(f3 > 20F)
        {
            f3 = 20F;
            flag = true;
        }
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = f3 * 0.01F;
        Cockpit.xyz[2] = 0.0F;
        super.mesh.chunkSetLocate("Z_Z_HUD_AOA", Cockpit.xyz, Cockpit.ypr);
        if(flag && blinkCounter % 10 < 5)
            super.mesh.chunkVisible("Z_Z_HUD_AOA", false);
        else
            super.mesh.chunkVisible("Z_Z_HUD_AOA", true);
        f3 = setNew.vspeed2 * 3.48F;
        f3 *= 60F;
        flag = false;
        if(f3 > 2000F)
        {
            f3 = 2000F;
            flag = true;
        }
        if(f3 < -2000F)
        {
            f3 = -2000F;
            flag = true;
        }
        f3 /= 1000F;
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = f3 * 0.05F;
        Cockpit.xyz[2] = 0.0F;
        super.mesh.chunkSetLocate("Z_Z_HUD_VS", Cockpit.xyz, Cockpit.ypr);
        if(flag && blinkCounter % 10 < 5)
            super.mesh.chunkVisible("Z_Z_HUD_VS", false);
        else
            super.mesh.chunkVisible("Z_Z_HUD_VS", true);
        float f10 = calculateMach();
        f3 = (float)(int)f10 * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Mach_1", 0.0F, 0.0F, f3);
        f3 = (float)((int)(f10 * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Mach_2", 0.0F, 0.0F, f3);
        f3 = (float)((int)(f10 * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Mach_3", 0.0F, 0.0F, f3);
        if(((FlightModelMain) (super.fm)).AS.listenLorenzBlindLanding || ((FlightModelMain) (super.fm)).AS.listenRadioStation || ((FlightModelMain) (super.fm)).AS.listenNDBeacon)
        {
            super.mesh.chunkVisible("Z_Z_HUD_DST_0", true);
            super.mesh.chunkVisible("Z_Z_HUD_DST_1", true);
            super.mesh.chunkVisible("Z_Z_HUD_DST_2", true);
            super.mesh.chunkVisible("Z_Z_HUD_DST_3", true);
            super.mesh.chunkVisible("Z_Z_HUD_DIR", true);
            super.mesh.chunkVisible("Z_Z_HUD_DIR_BG", true);
            float f11;
            if(((FlightModelMain) (super.fm)).AS.listenLorenzBlindLanding)
            {
                f11 = getNDBDist();
                super.mesh.chunkVisible("Z_Z_HUD_GS", true);
                float f4 = setNew.ilsGS;
                Cockpit.xyz[0] = 0.0F;
                Cockpit.xyz[1] = f4 * 0.05F;
                Cockpit.xyz[2] = 0.0F;
                if(Cockpit.xyz[1] < -0.1F)
                    Cockpit.xyz[1] = -0.1F;
                else
                if(Cockpit.xyz[1] > 0.1F)
                    Cockpit.xyz[1] = 0.1F;
                super.mesh.chunkSetLocate("Z_Z_HUD_GS", Cockpit.xyz, Cockpit.ypr);
                f4 = setNew.ilsLoc;
                Cockpit.xyz[0] = -f4 * 0.005F;
            } else
            {
                f11 = getNDBDist();
                super.mesh.chunkVisible("Z_Z_HUD_GS", false);
                float f5 = setNew.navDiviation0;
                Cockpit.xyz[0] = -f5 * 0.01F;
            }
            Cockpit.xyz[1] = 0.0F;
            Cockpit.xyz[2] = 0.0F;
            if(Cockpit.xyz[0] < -0.1F)
                Cockpit.xyz[0] = -0.1F;
            else
            if(Cockpit.xyz[0] > 0.1F)
                Cockpit.xyz[0] = 0.1F;
            float f6 = ((float)(int)f11 / 100F) * 36F;
            super.mesh.chunkSetAngles("Z_Z_HUD_DST_3", 0.0F, 0.0F, f6);
            f6 = (float)(((int)f11 % 100) / 10) * 36F;
            super.mesh.chunkSetAngles("Z_Z_HUD_DST_2", 0.0F, 0.0F, f6);
            f6 = (float)(int)(f11 % 10F) * 36F;
            super.mesh.chunkSetAngles("Z_Z_HUD_DST_1", 0.0F, 0.0F, f6);
            f6 = ((f11 * 10F) % 10F) * 36F;
            super.mesh.chunkSetAngles("Z_Z_HUD_DST_0", 0.0F, 0.0F, f6);
            super.mesh.chunkSetLocate("Z_Z_HUD_DIR", Cockpit.xyz, Cockpit.ypr);
        } else
        {
            super.mesh.chunkVisible("Z_Z_HUD_DST_0", false);
            super.mesh.chunkVisible("Z_Z_HUD_DST_1", false);
            super.mesh.chunkVisible("Z_Z_HUD_DST_2", false);
            super.mesh.chunkVisible("Z_Z_HUD_DST_3", false);
            super.mesh.chunkVisible("Z_Z_HUD_DIR", false);
            super.mesh.chunkVisible("Z_Z_HUD_DIR_BG", false);
            super.mesh.chunkVisible("Z_Z_HUD_GS", false);
        }
    }

    protected void drawSound(float f)
    {
        if(aoaWarnFX != null)
            if(setNew.fpmPitch >= 9.7F && setNew.fpmPitch < 12F && ((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX.isPlaying())
                    aoaWarnFX.play();
            } else
            {
                aoaWarnFX.cancel();
            }
        if(aoaWarnFX2 != null)
            if(setNew.fpmPitch >= 12F && setNew.fpmPitch < 14F && ((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX2.isPlaying())
                    aoaWarnFX2.play();
            } else
            {
                aoaWarnFX2.cancel();
            }
        if(aoaWarnFX3 != null)
            if(setNew.fpmPitch >= 14F && setNew.fpmPitch < 15.5F && ((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX3.isPlaying())
                    aoaWarnFX3.play();
            } else
            {
                aoaWarnFX3.cancel();
            }
        if(aoaWarnFX4 != null)
            if(setNew.fpmPitch >= 15.4F && ((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX4.isPlaying())
                    aoaWarnFX4.play();
            } else
            {
                aoaWarnFX4.cancel();
            }
    }

    public float normalizeDegree(float f)
    {
        if(f < 0.0F)
            do
                f += 360F;
            while(f < 0.0F);
        else
        if(f > 360F)
            do
                f -= 360F;
            while(f >= 360F);
        return f;
    }

    public float normalizeDegree180(float f)
    {
        if(f < -180F)
            do
                f += 360F;
            while(f < -180F);
        else
        if(f > 180F)
            do
                f -= 360F;
            while(f > 180F);
        return f;
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

    public float calculateMach()
    {
        return super.fm.getSpeedKMH() / getMachForAlt(super.fm.getAltitude());
    }

    public void reflectCockpitState()
    {
    }

    public void toggleDim()
    {
        isDimmer = !isDimmer;
    }

    public void toggleLight()
    {
    	super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
        {
            super.mesh.chunkVisible("Z_Z_NVision", true);
        } else
        {
            super.mesh.chunkVisible("Z_Z_NVision", false);
        }
    }

    private void retoggleLight()
    {
    }

    protected float getNDBDist()
    {
        int i = ((FlightModelMain) (super.fm)).AS.getBeacon();
        if(i == 0)
        {
            return 0.0F;
        } else
        {
            ArrayList arraylist = Main.cur().mission.getBeacons(((Interpolate) (super.fm)).actor.getArmy());
            Actor actor = (Actor)arraylist.get(i - 1);
            tmpV.sub(actor.pos.getAbsPoint(), ((FlightModelMain) (super.fm)).Loc);
            return (float)(tmpV.length() * 0.0010000000474974513D) / 1.853F;
        }
    }

    protected float getNDBDirection()
    {
        int i = ((FlightModelMain) (super.fm)).AS.getBeacon();
        if(i == 0)
        {
            return 0.0F;
        } else
        {
            ArrayList arraylist = Main.cur().mission.getBeacons(((Interpolate) (super.fm)).actor.getArmy());
            Actor actor = (Actor)arraylist.get(i - 1);
            tmpV.x = ((Tuple3d) (actor.pos.getAbsPoint())).x;
            tmpV.y = ((Tuple3d) (actor.pos.getAbsPoint())).y;
            tmpV.z = ((Tuple3d) (actor.pos.getAbsPoint())).z + 20D;
            ((Actor) (aircraft())).pos.getAbs(tmpP);
            tmpP.sub(tmpV);
            float f = 57.32484F * (float)Math.atan2(-((Tuple3d) (tmpP)).y, -((Tuple3d) (tmpP)).x);
            return 360F - f;
        }
    }

    public boolean useRealisticNavigationInstruments()
    {
        return World.cur().diffCur.RealisticNavigationInstruments;
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private Point3d tmpP;
    private Vector3d tmpV;
    private Matrix3d tmpMat;
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private boolean bU4;
    private boolean isDimmer;
    private int blinkCounter;
    private SoundFX aoaWarnFX;
    private SoundFX aoaWarnFX2;
    private SoundFX aoaWarnFX3;
    private SoundFX aoaWarnFX4;
    private String hudPitchRudderStr[];
    private Gun gun[];
    private static final float rpmScale[] = {
        0.0F, 190F, 220F, 300F
    };
    private ClipBoard clipBoard;
    private static final float k14TargetMarkScale[] = {
        0.0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F
    };
    private static final float k14TargetWingspanScale[] = {
        9.9F, 10.52F, 13.8F, 16.34F, 19F, 20F, 22F, 29.25F, 30F, 32.85F
    };
    private static final float k14BulletDrop[] = {
        5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 
        8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 
        10.603F, 10.704F, 10.739F, 10.782F, 10.789F
    };







}