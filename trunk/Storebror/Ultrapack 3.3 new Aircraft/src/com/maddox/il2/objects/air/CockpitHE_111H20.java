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

public class CockpitHE_111H20 extends CockpitPilot
{
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
            float f = waypointAzimuth();
            setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(1.0F));
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
            setNew.vspeed = (499F * setOld.vspeed + fm.getVertSpeed()) / 500F;
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
        float beaconDirection;
        float beaconRange;
        float turn;
        float vspeed;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
        }
    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            ((HE_111xyz)((Interpolate) (this.fm)).actor).bPitUnfocused = false;
            aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot1_FAK", false);
            aircraft().hierMesh().chunkVisible("Head1_FAK", false);
            aircraft().hierMesh().chunkVisible("Pilot1_FAL", false);
            aircraft().hierMesh().chunkVisible("Pilot2_FAK", false);
            aircraft().hierMesh().chunkVisible("Pilot2_FAL", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        ((HE_111xyz)((Interpolate) (this.fm)).actor).bPitUnfocused = true;
        aircraft().hierMesh().chunkVisible("Cockpit_D0", aircraft().hierMesh().isChunkVisible("Nose_D0") || aircraft().hierMesh().isChunkVisible("Nose_D1") || aircraft().hierMesh().isChunkVisible("Nose_D2"));
        aircraft().hierMesh().chunkVisible("Turret1C_D0", aircraft().hierMesh().isChunkVisible("Turret1B_D0"));
        aircraft().hierMesh().chunkVisible("Pilot1_FAK", aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
        aircraft().hierMesh().chunkVisible("Head1_FAK", aircraft().hierMesh().isChunkVisible("Head1_D0"));
        aircraft().hierMesh().chunkVisible("Pilot1_FAL", aircraft().hierMesh().isChunkVisible("Pilot1_D1"));
        aircraft().hierMesh().chunkVisible("Pilot2_FAK", aircraft().hierMesh().isChunkVisible("Pilot2_D0"));
        aircraft().hierMesh().chunkVisible("Pilot2_FAL", aircraft().hierMesh().isChunkVisible("Pilot2_D1"));
        super.doFocusLeave();
    }

    public CockpitHE_111H20()
    {
        super("3DO/Cockpit/He-111H-20/hier_H20.him", "he111");
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
            "clocks1", "clocks2", "clocks2DMG", "clocks3", "clocks3DMG", "clocks4", "clocks5", "clocks6", "AFN-1"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
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
        this.mesh.chunkSetAngles("zTurretA", 0.0F, this.fm.turret[0].tu[0], 0.0F);
        this.mesh.chunkSetAngles("zTurretB", 0.0F, this.fm.turret[0].tu[1], 0.0F);
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
        this.mesh.chunkSetAngles("zVSI", cvt(setNew.vspeed, -15F, 15F, -160F, 160F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed", floatindex(cvt(Pitot.Indicator((float)this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 400F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRepeater", -setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass4", -setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCompass", setNew.waypointAzimuth.getDeg(f * 0.1F) - 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMagnetic", -setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zNavP", -setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM1", floatindex(cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM2", floatindex(cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBoost1", cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBoost2", cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilTemp1", cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 100F, 0.0F, 130F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilTemp2", cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 100F, 0.0F, 130F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCoolant1", cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 100F, 0.0F, 126F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCoolant2", cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 100F, 0.0F, 126F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-1", cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-2", cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-3", cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-4", cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-1", cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-2", cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-3", cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-4", cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel1", cvt(this.fm.M.fuel / 0.72F, 0.0F, 2000F, 0.0F, 140F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel2", cvt(this.fm.M.fuel / 0.72F, 0.0F, 2000F, 0.0F, 140F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zExtT", cvt(Atmosphere.temperature((float)this.fm.Loc.z), 223.09F, 323.09F, -145F, 145F), 0.0F, 0.0F);
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
        this.mesh.chunkSetAngles("AFN1", 0.0F, cvt(setNew.beaconDirection, -45F, 45F, -14F, 14F), 0.0F);
        this.mesh.chunkSetAngles("AFN2", 0.0F, cvt(setNew.beaconRange, 0.0F, 1.0F, 26.5F, -26.5F), 0.0F);
        this.mesh.chunkVisible("AFN1_RED", isOnBlindLandingMarker());
    }

    public boolean isOnBlindLandingMarker()
    {
        return false;
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 4) != 0)
        {
            this.mesh.chunkVisible("ZHolesL_D1", true);
            this.mesh.chunkVisible("PanelL_D1", true);
            this.mesh.chunkVisible("PanelL_D0", false);
            this.mesh.chunkVisible("zVSI", false);
            this.mesh.chunkVisible("zBlip1", false);
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
            this.mesh.chunkVisible("PanelR_D0", false);
            this.mesh.chunkVisible("zRPM1", false);
            this.mesh.chunkVisible("zBoost2", false);
            this.mesh.chunkVisible("zOilTemp2", false);
            this.mesh.chunkVisible("zCoolant1", false);
            this.mesh.chunkVisible("zOFP1-1", false);
            this.mesh.chunkVisible("zOFP1-2", false);
            this.mesh.chunkVisible("zFlapsIR", false);
        }
        if((this.fm.AS.astateCockpitState & 0x20) != 0)
            this.mesh.chunkVisible("ZHolesR_D2", true);
        if((this.fm.AS.astateCockpitState & 1) != 0)
            this.mesh.chunkVisible("ZHolesF_D1", true);
        if((this.fm.AS.astateCockpitState & 0x40) != 0)
        {
            this.mesh.chunkVisible("PanelT_D1", true);
            this.mesh.chunkVisible("PanelT_D0", false);
            this.mesh.chunkVisible("zFuel2", false);
            this.mesh.chunkVisible("zOFP1-3", false);
            this.mesh.chunkVisible("zOFP1-4", false);
        }
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
    private static final float speedometerScale[] = {
        0.0F, 0.1F, 19F, 37.25F, 63.5F, 91.5F, 112F, 135.5F, 159.5F, 186.5F, 
        213F, 238F, 264F, 289F, 314.5F, 339.5F, 359.5F, 360F, 360F, 360F
    };
    private static final float rpmScale[] = {
        0.0F, 11.25F, 54F, 111F, 171.5F, 229.5F, 282.5F, 334F, 342.5F, 342.5F
    };

    static 
    {
        Property.set(CockpitHE_111H20.class, "normZNs", new float[] {
            1.0F, 1.0F, 1.25F, 1.0F
        });
    }
}
