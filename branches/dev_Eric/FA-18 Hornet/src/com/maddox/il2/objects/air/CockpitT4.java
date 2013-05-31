// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 9/22/2012 8:08:06 PM
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

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit, AircraftLH, T_4, 
//            TypeSupersonic

public class CockpitT4 extends CockpitPilot
{
    private class ClipBoard
    {

        public int setPage(int val)
        {
            hidePages();
            if(val < 1)
                page_ = 1;
            else
            if(val > 10)
                page_ = 10;
            else
                page_ = val;
            mesh.chunkVisible("Int_ClipBoardP" + page_, true);
            return page_;
        }

        public void hidePages()
        {
            for(int i = 1; i <= 10; i++)
                mesh.chunkVisible("Int_ClipBoardP" + i, false);

        }

        public void clipBoardShowHide(boolean isShow)
        {
            if(isShow)
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
            float f = waypointAzimuth();
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
            Vector3d vec = new Vector3d();
            ((FlightModelMain) (fm)).Or.transformInv(((FlightModelMain) (fm)).Vwld, vec);
            setNew.fpmPitch = FMMath.RAD2DEG(-(float)Math.atan2(((Tuple3d) (vec)).z, ((Tuple3d) (vec)).x));
            setNew.fpmYaw = FMMath.RAD2DEG((float)Math.atan2(((Tuple3d) (vec)).y, ((Tuple3d) (vec)).x));
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


    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(10F);
    }

    public CockpitT4()
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

        for(int i = 1; i <= 18; i++)
            hudPitchRudderStr[i + 18] = "Z_Z_HUD_PITCHN" + i * 5;

        ((AircraftLH)aircraft()).bWantBeaconKeys = true;
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            super.mesh.chunkVisible("Int_Marker", false);
            bNeedSetUp = false;
        }
        ((AircraftLH)aircraft()).bWantBeaconKeys = true;
        AircraftLH _tmp = (AircraftLH)aircraft();
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
        ((F_18)aircraft()).clipBoardPage_ = clipBoard.setPage(((F_18)aircraft()).clipBoardPage_);
        clipBoard.clipBoardShowHide(((F_18)aircraft()).showClipBoard_);
        ((FlightModelMain) (super.fm)).Or.getMatrix(tmpMat);
        float shadowPosP = (float)tmpMat.getElement(2, 0);
        shadowPosP = 1.0F - shadowPosP;
        shadowPosP *= 0.045F;
        shadowPosP += (float)tmpMat.getElement(2, 2) * -0.014F;
        if(shadowPosP < 0.0F)
            shadowPosP = 0.0F;
        resetYPRmodifier();
        Cockpit.xyz[2] = shadowPosP;
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
        float alt = interp(setNew.altimeter, setOld.altimeter, f);
        float val = cvt(alt, 0.0F, 30480F, 0.0F, 36000F);
        super.mesh.chunkSetAngles("GaugeMove_ALT_N", val, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GaugeMove_ALT_3", 0.0F, 0.0F, -val);
        val = cvt(alt, 0.0F, 30480F, 0.0F, 3600F);
        float val2 = (float)(int)(val / 36F) * 36F;
        if(val - val2 > 34.2F)
            val2 += (1.0F - ((val2 + 36F) - val) / 1.8F) * 36F;
        super.mesh.chunkSetAngles("GaugeMove_ALT_4", 0.0F, 0.0F, -val2);
        val = cvt(alt, 0.0F, 30480F, 0.0F, 360F);
        val2 = (float)(int)(val / 36F) * 36F;
        if(val - val2 > 35.82F)
            val2 += (1.0F - ((val2 + 36F) - val) / 0.18F) * 36F;
        super.mesh.chunkSetAngles("GaugeMove_ALT_5", 0.0F, 0.0F, -val2);
        val = setNew.azimuth.getDeg(f) + 90F;
        super.mesh.chunkSetAngles("GaugeMove_HSI_HDG", -val, 0.0F, 0.0F);
        val = setNew.waypointAzimuth.getDeg(f) + 90F;
        super.mesh.chunkSetAngles("GaugeMove_HSI_CRS", val, 0.0F, 0.0F);
        val = setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F + 180F;
        super.mesh.chunkSetAngles("GaugeMove_HSI_TACAN", val, 0.0F, 0.0F);
        resetYPRmodifier();
        if(((FlightModelMain) (super.fm)).AS.listenLorenzBlindLanding)
        {
            val = setNew.ilsLoc;
            Cockpit.xyz[0] = -val * 0.001F;
        } else
        if(((FlightModelMain) (super.fm)).AS.listenNDBeacon || ((FlightModelMain) (super.fm)).AS.listenRadioStation)
        {
            val = setNew.navDiviation0;
            Cockpit.xyz[0] = -val * 0.002F;
        }
        if(Cockpit.xyz[0] < -0.02F)
            Cockpit.xyz[0] = -0.02F;
        else
        if(Cockpit.xyz[0] > 0.02F)
            Cockpit.xyz[0] = 0.02F;
        super.mesh.chunkSetLocate("GaugeMove_HSI_DIV", Cockpit.xyz, Cockpit.ypr);
        float deg = 0.0F;
        float mach = calculateMach();
        deg = mach * 180F;
        super.mesh.chunkSetAngles("GaugeMove_SPD_MACH", deg, 0.0F, 0.0F);
        deg = 0.0F;
        float knot = Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH());
        knot *= 0.53996F;
        if(knot <= 50F)
            deg = (knot / 50F) * 15F;
        else
        if(knot <= 100F)
            deg = (knot - 50F) + 15F;
        else
        if(knot <= 150F)
            deg = (knot - 100F) * 1.2F + 65F;
        else
        if(knot <= 200F)
            deg = (knot - 150F) * 0.9F + 125F;
        else
        if(knot <= 300F)
            deg = (knot - 200F) * 0.6F + 170F;
        else
        if(knot <= 400F)
            deg = (knot - 300F) * 0.45F + 230F;
        else
        if(knot <= 700F)
            deg = (knot - 400F) * 0.3F + 275F;
        super.mesh.chunkSetAngles("GaugeMove_SPD", deg, 0.0F, 0.0F);
        float vs = setNew.vspeed2 * 3.48F;
        vs *= 60F;
        float vsabs = Math.abs(vs);
        boolean isNeg = vs < 0.0F;
        if(vsabs <= 1000F)
            val = vsabs * 0.07F;
        else
        if(vsabs <= 2000F)
            val = (vsabs - 1000F) * 0.035F + 70F;
        else
        if(vsabs <= 4000F)
            val = (vsabs - 2000F) * 0.0175F + 105F;
        else
        if(vsabs <= 6000F)
            val = (vsabs - 4000F) * 0.00875F + 140F;
        else
            val = 157.5F;
        if(isNeg)
            val = -val;
        super.mesh.chunkSetAngles("GaugeMove_VS", val, 0.0F, 0.0F);
        val = super.fm.getAOA() * 9F;
        if(val < 0.0F)
            val = 0.0F;
        else
        if(val > 270F)
            val = 270F;
        val *= -1F;
        super.mesh.chunkSetAngles("GaugeMove_AOA", val, 0.0F, 0.0F);
        float gLoad = super.fm.getOverload();
        gLoad--;
        val = gLoad * 20F;
        super.mesh.chunkSetAngles("GaugeMove_G", val, 0.0F, 0.0F);
        val = ((FlightModelMain) (super.fm)).M.fuel / ((FlightModelMain) (super.fm)).M.maxFuel;
        val *= 220F;
        super.mesh.chunkSetAngles("GaugeMove_FUEL_L", val, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GaugeMove_FUEL_R", val, 0.0F, 0.0F);
        if(((FlightModelMain) (super.fm)).CT.getGear() > 0.0F)
            super.mesh.chunkSetAngles("GaugeMove_Gear", 0.0F, 0.0F, 0.0F);
        else
            super.mesh.chunkSetAngles("GaugeMove_Gear", 0.0F, 0.0F, -15.5F);
    }

    protected void moveHUD(float f)
    {
        blinkCounter++;
        float knot = Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH());
        knot *= 0.53996F;
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = 0.001613948F * knot;
        Cockpit.xyz[2] = 0.0F;
        int isBlink = 0;
        boolean isHudDisp = false;
        if(!setNew.isBatteryOn && !setNew.isGeneratorAllive)
            isHudDisp = false;
        else
            isHudDisp = true;
        for(int i = 0; i < 37; i++)
            super.mesh.chunkVisible(hudPitchRudderStr[i], false);

        super.mesh.chunkVisible("Z_Z_HUD_PITCHN3", false);
        super.mesh.chunkVisible("Z_Z_ALT_1", isHudDisp);
        super.mesh.chunkVisible("Z_Z_ALT_2", isHudDisp);
        super.mesh.chunkVisible("Z_Z_ALT_3", isHudDisp);
        super.mesh.chunkVisible("Z_Z_ALT_4", isHudDisp);
        super.mesh.chunkVisible("Z_Z_ALT_5", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_ALT_CY", isHudDisp);
        super.mesh.chunkVisible("Z_Z_G_1", isHudDisp);
        super.mesh.chunkVisible("Z_Z_G_2", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HDG_3", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HDG_2", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HDG_1", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_HDG", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_Mach_3", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_Mach_2", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_Mach_1", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_SPD", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_HIDE1", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_LOWER_ROLL", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_MISC", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_FPM", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_FPM_NO", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_VS", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_VSBG", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_AOABG", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_AOA", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_BANK", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_DST_0", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_DST_1", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_DST_2", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_DST_3", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_DIR", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_DIR_BG", isHudDisp);
        super.mesh.chunkVisible("Z_Z_HUD_GS", isHudDisp);
        if(!isHudDisp)
            return;
        super.mesh.chunkSetLocate("Z_Z_HUD_SPD", Cockpit.xyz, Cockpit.ypr);
        float pitchVal = setNew.pitch;
        if(pitchVal > 90F)
            for(; pitchVal > 90F; pitchVal -= 360F);
        else
        if(pitchVal < -90F)
            for(; pitchVal < -90F; pitchVal += 360F);
        pitchVal -= 90F;
        pitchVal = -pitchVal;
        int indexOffset = (int)pitchVal / 5;
        for(int j = indexOffset - 3; j <= indexOffset + 2; j++)
            if(j >= 0 && j < 37)
            {
                super.mesh.chunkVisible(hudPitchRudderStr[j], true);
                super.mesh.chunkSetAngles(hudPitchRudderStr[j], setNew.bank, 0.0F, -setNew.pitch);
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
        boolean fpmNoFlag = false;
        if(((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1 || knot > 10F)
        {
            Cockpit.xyz[1] = -setNew.fpmYaw;
            if(Cockpit.xyz[1] < -6.5F)
            {
                Cockpit.xyz[1] = -6.5F;
                fpmNoFlag = true;
            } else
            if(Cockpit.xyz[1] > 6.5F)
            {
                Cockpit.xyz[1] = 6.5F;
                fpmNoFlag = true;
            }
            Cockpit.xyz[2] = -setNew.fpmPitch;
            if(Cockpit.xyz[2] < -11.5F)
            {
                Cockpit.xyz[2] = -11.5F;
                fpmNoFlag = true;
            } else
            if(Cockpit.xyz[2] > 7F)
            {
                Cockpit.xyz[2] = 7F;
                fpmNoFlag = true;
            }
        } else
        {
            Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        }
        super.mesh.chunkSetAngles("Z_Z_HUD_FPM", 0.0F, Cockpit.xyz[1], Cockpit.xyz[2]);
        super.mesh.chunkVisible("Z_Z_HUD_FPM_NO", fpmNoFlag);
        float val = normalizeDegree(setNew.bank);
        super.mesh.chunkSetAngles("Z_Z_HUD_BANK", val, 0.0F, 0.0F);
        if(val > 90F && val < 270F)
            super.mesh.chunkVisible("Z_Z_HUD_LOWER_ROLL", true);
        else
            super.mesh.chunkVisible("Z_Z_HUD_LOWER_ROLL", false);
        float alt = setNew.altimeter;
        val = cvt(alt, 0.0F, 30480F, 0.0F, 3600000F);
        val = 0.0F;
        super.mesh.chunkSetAngles("Z_Z_ALT_1", 0.0F, 0.0F, val);
        val = cvt(alt, 0.0F, 30480F, 0.0F, 360000F);
        val = (float)(int)(val / 36F) * 36F;
        super.mesh.chunkSetAngles("Z_Z_ALT_2", 0.0F, 0.0F, val);
        val = cvt(alt, 0.0F, 30480F, 0.0F, 36000F);
        val = (float)(int)(val / 36F) * 36F;
        super.mesh.chunkSetAngles("Z_Z_ALT_3", 0.0F, 0.0F, val);
        val = cvt(alt, 0.0F, 30480F, 0.0F, 3600F);
        val = (float)(int)(val / 36F) * 36F;
        super.mesh.chunkSetAngles("Z_Z_ALT_4", 0.0F, 0.0F, val);
        val = cvt(alt, 0.0F, 30480F, 0.0F, 1800F);
        if(val <= 360F)
        {
            super.mesh.chunkSetAngles("Z_Z_HUD_ALT_CY", 0.0F, 0.0F, val);
            super.mesh.chunkVisible("Z_Z_HUD_ALT_CY", true);
        } else
        {
            super.mesh.chunkVisible("Z_Z_HUD_ALT_CY", false);
        }
        val = cvt(alt, 0.0F, 30480F, 0.0F, 360F);
        val = (float)(int)(val / 36F) * 36F;
        super.mesh.chunkSetAngles("Z_Z_ALT_5", 0.0F, 0.0F, val);
        float gLoad = super.fm.getOverload();
        val = (float)(int)Math.abs(gLoad) * 36F;
        super.mesh.chunkSetAngles("Z_Z_G_1", 0.0F, 0.0F, val);
        val = (float)((int)(Math.abs(gLoad) * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_G_2", 0.0F, 0.0F, val);
        float hdg = normalizeDegree(setNew.azimuth.getDeg(f) + 90F);
        super.mesh.chunkSetAngles("Z_Z_HUD_HDG", 0.0F, hdg, 0.0F);
        hdg += 0.5F;
        if(hdg > 360F)
            hdg = 0.0F;
        val = (float)(int)(hdg / 100F) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDG_1", 0.0F, 0.0F, val);
        val = (float)(((int)hdg % 100) / 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDG_2", 0.0F, 0.0F, val);
        val = (float)((int)hdg % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDG_3", 0.0F, 0.0F, val);
        val = super.fm.getAOA();
        isBlink = 0;
        if(val < 0.0F)
        {
            val = 0.0F;
            isBlink = 1;
        } else
        if(val > 20F)
        {
            val = 20F;
            isBlink = 1;
        }
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = val * 0.01F;
        Cockpit.xyz[2] = 0.0F;
        super.mesh.chunkSetLocate("Z_Z_HUD_AOA", Cockpit.xyz, Cockpit.ypr);
        if(isBlink == 1 && blinkCounter % 10 < 5)
            super.mesh.chunkVisible("Z_Z_HUD_AOA", false);
        else
            super.mesh.chunkVisible("Z_Z_HUD_AOA", true);
        val = setNew.vspeed2 * 3.48F;
        val *= 60F;
        isBlink = 0;
        if(val > 2000F)
        {
            val = 2000F;
            isBlink = 1;
        }
        if(val < -2000F)
        {
            val = -2000F;
            isBlink = 1;
        }
        val /= 1000F;
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = val * 0.05F;
        Cockpit.xyz[2] = 0.0F;
        super.mesh.chunkSetLocate("Z_Z_HUD_VS", Cockpit.xyz, Cockpit.ypr);
        if(isBlink == 1 && blinkCounter % 10 < 5)
            super.mesh.chunkVisible("Z_Z_HUD_VS", false);
        else
            super.mesh.chunkVisible("Z_Z_HUD_VS", true);
        float mach = calculateMach();
        val = (float)(int)mach * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Mach_1", 0.0F, 0.0F, val);
        val = (float)((int)(mach * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Mach_2", 0.0F, 0.0F, val);
        val = (float)((int)(mach * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Mach_3", 0.0F, 0.0F, val);
        if(((FlightModelMain) (super.fm)).AS.listenLorenzBlindLanding || ((FlightModelMain) (super.fm)).AS.listenRadioStation || ((FlightModelMain) (super.fm)).AS.listenNDBeacon)
        {
            super.mesh.chunkVisible("Z_Z_HUD_DST_0", true);
            super.mesh.chunkVisible("Z_Z_HUD_DST_1", true);
            super.mesh.chunkVisible("Z_Z_HUD_DST_2", true);
            super.mesh.chunkVisible("Z_Z_HUD_DST_3", true);
            super.mesh.chunkVisible("Z_Z_HUD_DIR", true);
            super.mesh.chunkVisible("Z_Z_HUD_DIR_BG", true);
            float dst;
            if(((FlightModelMain) (super.fm)).AS.listenLorenzBlindLanding)
            {
                dst = getNDBDist();
                super.mesh.chunkVisible("Z_Z_HUD_GS", true);
                val = setNew.ilsGS;
                Cockpit.xyz[0] = 0.0F;
                Cockpit.xyz[1] = val * 0.05F;
                Cockpit.xyz[2] = 0.0F;
                if(Cockpit.xyz[1] < -0.1F)
                    Cockpit.xyz[1] = -0.1F;
                else
                if(Cockpit.xyz[1] > 0.1F)
                    Cockpit.xyz[1] = 0.1F;
                super.mesh.chunkSetLocate("Z_Z_HUD_GS", Cockpit.xyz, Cockpit.ypr);
                val = setNew.ilsLoc;
                Cockpit.xyz[0] = -val * 0.005F;
            } else
            {
                dst = getNDBDist();
                super.mesh.chunkVisible("Z_Z_HUD_GS", false);
                val = setNew.navDiviation0;
                Cockpit.xyz[0] = -val * 0.01F;
            }
            Cockpit.xyz[1] = 0.0F;
            Cockpit.xyz[2] = 0.0F;
            if(Cockpit.xyz[0] < -0.1F)
                Cockpit.xyz[0] = -0.1F;
            else
            if(Cockpit.xyz[0] > 0.1F)
                Cockpit.xyz[0] = 0.1F;
            val = ((float)(int)dst / 100F) * 36F;
            super.mesh.chunkSetAngles("Z_Z_HUD_DST_3", 0.0F, 0.0F, val);
            val = (float)(((int)dst % 100) / 10) * 36F;
            super.mesh.chunkSetAngles("Z_Z_HUD_DST_2", 0.0F, 0.0F, val);
            val = (float)(int)(dst % 10F) * 36F;
            super.mesh.chunkSetAngles("Z_Z_HUD_DST_1", 0.0F, 0.0F, val);
            val = ((dst * 10F) % 10F) * 36F;
            super.mesh.chunkSetAngles("Z_Z_HUD_DST_0", 0.0F, 0.0F, val);
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

    public float normalizeDegree(float val)
    {
        if(val < 0.0F)
            do
                val += 360F;
            while(val < 0.0F);
        else
        if(val > 360F)
            do
                val -= 360F;
            while(val >= 360F);
        return val;
    }

    public float normalizeDegree180(float val)
    {
        if(val < -180F)
            do
                val += 360F;
            while(val < -180F);
        else
        if(val > 180F)
            do
                val -= 360F;
            while(val > 180F);
        return val;
    }

    public float getMachForAlt(float theAltValue)
    {
        theAltValue /= 1000F;
        int i = 0;
        for(i = 0; i < TypeSupersonic.fMachAltX.length; i++)
            if(TypeSupersonic.fMachAltX[i] > theAltValue)
                break;

        if(i == 0)
        {
            return TypeSupersonic.fMachAltY[0];
        } else
        {
            float baseMach = TypeSupersonic.fMachAltY[i - 1];
            float spanMach = TypeSupersonic.fMachAltY[i] - baseMach;
            float baseAlt = TypeSupersonic.fMachAltX[i - 1];
            float spanAlt = TypeSupersonic.fMachAltX[i] - baseAlt;
            float spanMult = (theAltValue - baseAlt) / spanAlt;
            return baseMach + spanMach * spanMult;
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
            float f1 = 57.32484F * (float)Math.atan2(-((Tuple3d) (tmpP)).y, -((Tuple3d) (tmpP)).x);
            return 360F - f1;
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







}