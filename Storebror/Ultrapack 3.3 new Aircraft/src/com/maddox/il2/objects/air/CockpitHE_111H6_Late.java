package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_111H6_Late extends CockpitPilot
{
    private class Variables
    {

        float altimeter;
        float throttlel;
        float throttler;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float beaconDirection;
        float beaconRange;
        float turn;
        float vspeed;
        float cons;
        float consL;
        float consR;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.altimeter = fm.getAltitude();
            setNew.throttlel = (10F * setOld.throttlel + fm.EI.engines[0].getControlThrottle()) / 11F;
            setNew.throttler = (10F * setOld.throttler + fm.EI.engines[1].getControlThrottle()) / 11F;
            w.set(fm.getW());
            fm.Or.transform(w);
            setNew.turn = (12F * setOld.turn + w.z) / 13F;
            mesh.chunkSetAngles("zTurretA", 0.0F, fm.turret[0].tu[0], 0.0F);
            mesh.chunkSetAngles("zTurretB", 0.0F, fm.turret[0].tu[1], 0.0F);
            float f = waypointAzimuth();
            if(useRealisticNavigationInstruments())
            {
                setNew.waypointAzimuth.setDeg(f - 90F);
                setOld.waypointAzimuth.setDeg(f - 90F);
                setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
            } else
            {
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(1.0F));
            }
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
            setNew.vspeed = (499F * setOld.vspeed + fm.getVertSpeed()) / 500F;
            setNew.beaconDirection = (10F * setOld.beaconDirection + getBeaconDirection()) / 11F;
            setNew.beaconRange = (10F * setOld.beaconRange + getBeaconRange()) / 11F;
            float f1 = prevFuel - fm.M.fuel;
            prevFuel = fm.M.fuel;
            f1 /= 0.72F;
            f1 /= Time.tickLenFs();
            f1 *= 3600F;
            setNew.cons = 0.9F * setOld.cons + 0.1F * f1;
            float f2 = fm.EI.engines[0].getEngineForce().x;
            float f3 = fm.EI.engines[1].getEngineForce().x;
            if(f2 < 100F || fm.EI.engines[0].getRPM() < 600F)
                f2 = 1.0F;
            if(f3 < 100F || fm.EI.engines[1].getRPM() < 600F)
                f3 = 1.0F;
            setNew.consL = 0.9F * setOld.consL + (0.1F * (setNew.cons * f2)) / (f2 + f3);
            setNew.consR = 0.9F * setOld.consR + (0.1F * (setNew.cons * f3)) / (f2 + f3);
            return true;
        }

        Interpolater()
        {
        }
    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            ((HE_111)((Interpolate) (this.fm)).actor).bPitUnfocused = false;
            bTurrVisible = aircraft().hierMesh().isChunkVisible("Turret1C_D0");
            aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot1_FAK", false);
            aircraft().hierMesh().chunkVisible("Head1_FAK", false);
            aircraft().hierMesh().chunkVisible("Pilot1_FAL", false);
            aircraft().hierMesh().chunkVisible("Pilot2_FAK", false);
            aircraft().hierMesh().chunkVisible("Pilot2_FAL", false);
            aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            aircraft().hierMesh().chunkVisible("Window_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        ((HE_111)((Interpolate) (this.fm)).actor).bPitUnfocused = true;
        aircraft().hierMesh().chunkVisible("Turret1C_D0", bTurrVisible);
        aircraft().hierMesh().chunkVisible("Cockpit_D0", aircraft().hierMesh().isChunkVisible("Nose_D0") || aircraft().hierMesh().isChunkVisible("Nose_D1") || aircraft().hierMesh().isChunkVisible("Nose_D2"));
        aircraft().hierMesh().chunkVisible("Turret1C_D0", aircraft().hierMesh().isChunkVisible("Turret1B_D0"));
        aircraft().hierMesh().chunkVisible("Pilot1_FAK", aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
        aircraft().hierMesh().chunkVisible("Head1_FAK", aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
        aircraft().hierMesh().chunkVisible("Pilot1_FAL", aircraft().hierMesh().isChunkVisible("Pilot1_D1"));
        aircraft().hierMesh().chunkVisible("Pilot2_FAK", aircraft().hierMesh().isChunkVisible("Pilot2_D0"));
        aircraft().hierMesh().chunkVisible("Pilot2_FAL", aircraft().hierMesh().isChunkVisible("Pilot2_D1"));
        aircraft().hierMesh().chunkVisible("Window_D0", true);
        super.doFocusLeave();
    }

    protected float waypointAzimuth()
    {
        return waypointAzimuthInvertMinus(30F);
    }

    public CockpitHE_111H6_Late()
    {
        super("3DO/Cockpit/He-111P-4/hier-H6-Late.him", "he111");
        setOld = new Variables();
        setNew = new Variables();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        w = new Vector3f();
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(218F, 143F, 128F);
        light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        this.cockpitNightMats = (new String[] {
            "clocks1", "clocks2", "clocks3B", "clocks4", "clocks5", "clocks6", "clocks7"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
        prevFuel = 0.0F;
    }

    public void reflectWorldToInstruments(float f)
    {
        if(this.fm.isTick(44, 0))
        {
            if((this.fm.AS.astateCockpitState & 8) == 0)
            {
                this.mesh.chunkVisible("Z_GearLRed1", this.fm.CT.getGear() == 0.0F || this.fm.Gears.isAnyDamaged());
                this.mesh.chunkVisible("Z_GearRRed1", this.fm.CT.getGear() == 0.0F || this.fm.Gears.isAnyDamaged());
                this.mesh.chunkVisible("Z_GearLGreen1", this.fm.CT.getGear() == 1.0F && this.fm.Gears.lgear);
                this.mesh.chunkVisible("Z_GearRGreen1", this.fm.CT.getGear() == 1.0F && this.fm.Gears.rgear);
            } else
            {
                this.mesh.chunkVisible("Z_GearLRed1", false);
                this.mesh.chunkVisible("Z_GearRRed1", false);
                this.mesh.chunkVisible("Z_GearLGreen1", false);
                this.mesh.chunkVisible("Z_GearRGreen1", false);
            }
            if((this.fm.AS.astateCockpitState & 0x40) == 0)
            {
                this.mesh.chunkVisible("zFuelWarning1", this.fm.M.fuel < 600F);
                this.mesh.chunkVisible("zFuelWarning2", this.fm.M.fuel < 600F);
            }
        }
        this.mesh.chunkSetAngles("zColumn1", 0.0F, 0.0F, -10F * (pictElev = 0.85F * pictElev + 0.15F * this.fm.CT.ElevatorControl));
        this.mesh.chunkSetAngles("zColumn2", 0.0F, 0.0F, -40F * (pictAiler = 0.85F * pictAiler + 0.15F * this.fm.CT.AileronControl));
        if(this.fm.CT.getRudder() > 0.0F)
        {
            this.mesh.chunkSetAngles("zPedalL", 0.0F, 0.0F, -10F * this.fm.CT.getRudder());
            this.mesh.chunkSetAngles("zPedalR", 0.0F, 0.0F, -45F * this.fm.CT.getRudder());
        } else
        {
            this.mesh.chunkSetAngles("zPedalL", 0.0F, 0.0F, -45F * this.fm.CT.getRudder());
            this.mesh.chunkSetAngles("zPedalR", 0.0F, 0.0F, -10F * this.fm.CT.getRudder());
        }
        this.mesh.chunkSetAngles("zOilFlap1", 0.0F, 0.0F, -50F * this.fm.EI.engines[0].getControlRadiator());
        this.mesh.chunkSetAngles("zOilFlap2", 0.0F, 0.0F, -50F * this.fm.EI.engines[1].getControlRadiator());
        this.mesh.chunkSetAngles("zMix1", 0.0F, 0.0F, -30F * this.fm.EI.engines[0].getControlMix());
        this.mesh.chunkSetAngles("zMix2", 0.0F, 0.0F, -30F * this.fm.EI.engines[1].getControlMix());
        this.mesh.chunkSetAngles("zFlaps1", 0.0F, 0.0F, -45F * this.fm.CT.FlapsControl);
        if(this.fm.EI.engines[0].getControlProp() >= 0.0F)
            this.mesh.chunkSetAngles("zPitch1", 0.0F, 0.0F, -65F * this.fm.EI.engines[0].getControlProp());
        if(this.fm.EI.engines[1].getControlProp() >= 0.0F)
            this.mesh.chunkSetAngles("zPitch2", 0.0F, 0.0F, -65F * this.fm.EI.engines[1].getControlProp());
        this.mesh.chunkSetAngles("zThrottle1", 0.0F, 0.0F, -33.6F * interp(setNew.throttlel, setOld.throttlel, f));
        this.mesh.chunkSetAngles("zThrottle2", 0.0F, 0.0F, -33.6F * interp(setNew.throttler, setOld.throttler, f));
        this.mesh.chunkSetAngles("zCompressor1", 0.0F, 0.0F, -25F * (float)this.fm.EI.engines[0].getControlCompressor());
        this.mesh.chunkSetAngles("zCompressor2", 0.0F, 0.0F, -25F * (float)this.fm.EI.engines[1].getControlCompressor());
        this.mesh.chunkSetAngles("zHour", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMinute", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSecond", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAH1", 0.0F, 0.0F, this.fm.Or.getKren());
        this.mesh.chunkSetAngles("zAH2", 0.0F, 0.0F, cvt(this.fm.Or.getTangage(), -30F, 30F, -6.5F, 6.5F));
        this.mesh.chunkSetAngles("zTurnBank", cvt(setNew.turn, -0.23562F, 0.23562F, 30F, -30F), 0.0F, 0.0F);
        float f1 = getBall(4.5D);
        this.mesh.chunkSetAngles("zBall", cvt(f1, -4F, 4F, -8F, 8F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBall2", cvt(f1, -4.5F, 4.5F, -9F, 9F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zVSI", cvt(setNew.vspeed, -15F, 15F, -135F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed", floatindex(cvt(Pitot.Indicator((float)this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 550F, 0.0F, 11F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
        if(useRealisticNavigationInstruments())
        {
            this.mesh.chunkSetAngles("zCompass", -setNew.azimuth.getDeg(f) + setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass3", setNew.azimuth.getDeg(f) - setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zRepeater", setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass4", -setNew.waypointAzimuth.getDeg(f) - 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass5", setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zMagnetic", -setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zNavP", -(setNew.waypointAzimuth.getDeg(f * 0.1F) - setNew.azimuth.getDeg(f)), 0.0F, 0.0F);
        } else
        {
            this.mesh.chunkSetAngles("zRepeater", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass4", -setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zCompass", -setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass3", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zMagnetic", -setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zNavP", -setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("zRPM1", floatindex(cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3600F, 0.0F, 6F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM2", floatindex(cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 3600F, 0.0F, 6F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBoost1", cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBoost2", cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilTemp1", cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 120F, 30F, 100F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilTemp2", cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 120F, 30F, 100F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCoolant1", cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 30F, 100F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCoolant2", cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 120F, 30F, 100F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-1", cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-2", cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-1", cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-2", cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel1", cvt(this.fm.M.fuel / 0.72F, 0.0F, 2000F, 34.5F, 140F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel2", cvt(this.fm.M.fuel / 0.72F, 0.0F, 2000F, 34.5F, 140F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zExtT", cvt(Atmosphere.temperature((float)this.fm.Loc.z), 213.09F, 313.09F, 43.7F, 136.8F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel1b", cvt(this.fm.M.fuel / 0.72F, 2000F, 2835F, 49F, 132.6F), 0.0F, 0.0F);
        float f2 = (float)Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin());
        f2 = (float)(int)(-f2 / 0.2F) * 0.2F;
        this.mesh.chunkSetAngles("zProp1-1", f2 * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zProp1-2", f2 * 5F, 0.0F, 0.0F);
        f2 = (float)Math.toDegrees(this.fm.EI.engines[1].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin());
        f2 = (float)(int)(-f2 / 0.2F) * 0.2F;
        this.mesh.chunkSetAngles("zProp2-1", f2 * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zProp2-2", f2 * 5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFlapsIL", 145F * this.fm.CT.getFlap(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFlapsIR", 145F * this.fm.CT.getFlap(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("AFN-2A", 0.0F, cvt(setNew.beaconDirection, -45F, 45F, -14F, 14F), 0.0F);
        this.mesh.chunkSetAngles("AFN-2B", 0.0F, cvt(setNew.beaconRange, 0.0F, 1.0F, 22.5F, -22.5F), 0.0F);
        this.mesh.chunkVisible("AFN-2-RED", isOnBlindLandingMarker());
        this.mesh.chunkSetAngles("zFC-1", cvt(setNew.consL, 100F, 500F, 0.0F, -240F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFC-2", cvt(setNew.consR, 100F, 500F, 0.0F, -240F), 0.0F, 0.0F);
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 4) != 0)
        {
            this.mesh.chunkVisible("ZHolesL_D1", true);
            this.mesh.chunkVisible("PanelL_D1", true);
        }
        if((this.fm.AS.astateCockpitState & 8) != 0)
        {
            this.mesh.chunkVisible("ZHolesL_D2", true);
            this.mesh.chunkVisible("PanelFloat_D1", true);
            this.mesh.chunkVisible("PanelFloat_D0", false);
            this.mesh.chunkVisible("zProp1-1", false);
            this.mesh.chunkVisible("zProp1-2", false);
            this.mesh.chunkVisible("zProp2-1", false);
            this.mesh.chunkVisible("zProp2-2", false);
            this.mesh.chunkVisible("zFlapsIL", false);
            this.mesh.chunkVisible("zFlapsIR", false);
        }
        if((this.fm.AS.astateCockpitState & 0x10) != 0)
        {
            this.mesh.chunkVisible("ZHolesR_D1", true);
            this.mesh.chunkVisible("PanelR_D1", true);
            this.mesh.chunkVisible("zOFP1-1", false);
            this.mesh.chunkVisible("zOFP1-2", false);
            this.mesh.chunkVisible("zFlapsIR", false);
        }
        if((this.fm.AS.astateCockpitState & 0x20) != 0)
            this.mesh.chunkVisible("ZHolesR_D2", true);
        if((this.fm.AS.astateCockpitState & 1) != 0)
            this.mesh.chunkVisible("ZHolesF_D1", true);
        if((this.fm.AS.astateCockpitState & 0x40) != 0)
            this.mesh.chunkVisible("PanelT_D1", true);
        if((this.fm.AS.astateCockpitState & 0x80) != 0)
            this.mesh.chunkVisible("zOil_D1", true);
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
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
    private LightPointActor light1;
    private float pictAiler;
    private float pictElev;
    public Vector3f w;
    private float prevFuel;
    private boolean bTurrVisible;
    private static final float speedometerScale[] = {
        0.0F, 6.01F, 18.5F, 43.65F, 75.81F, 113.39F, 152F, 192.26F, 232.24F, 270.67F, 
        308.39F, 343.38F
    };
    private static final float rpmScale[] = {
        0.0F, 14.7F, 76.15F, 143.86F, 215.97F, 282.68F, 346.18F
    };

    static 
    {
        Property.set(CockpitHE_111H6_Late.class, "normZNs", new float[] {
            1.0F, 1.0F, 1.25F, 1.0F
        });
    }
}
