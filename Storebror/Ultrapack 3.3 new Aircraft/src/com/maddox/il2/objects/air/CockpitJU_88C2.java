package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitJU_88C2 extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(JU_88C2.bChangedPit)
            {
                reflectPlaneToModel();
                JU_88C2.bChangedPit = false;
            }
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle1 = 0.85F * setOld.throttle1 + fm.EI.engines[0].getControlThrottle() * 0.15F;
                setNew.prop1 = 0.85F * setOld.prop1 + fm.EI.engines[0].getControlProp() * 0.15F;
                setNew.throttle2 = 0.85F * setOld.throttle2 + fm.EI.engines[1].getControlThrottle() * 0.15F;
                setNew.prop2 = 0.85F * setOld.prop2 + fm.EI.engines[1].getControlProp() * 0.15F;
                setNew.altimeter = fm.getAltitude();
                float f = waypointAzimuth();
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(1.0F));
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                if(cockpitDimControl)
                {
                    if(setNew.dimPosition > 0.0F)
                        setNew.dimPosition = setNew.dimPosition - 0.05F;
                } else
                if(setNew.dimPosition < 1.0F)
                    setNew.dimPosition = setNew.dimPosition + 0.05F;
                float f1 = prevFuel - fm.M.fuel;
                prevFuel = fm.M.fuel;
                f1 /= 0.72F;
                f1 /= Time.tickLenFs();
                f1 *= 3600F;
                setNew.cons = 0.91F * setOld.cons + 0.09F * f1;
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
        float prop1;
        float throttle2;
        float prop2;
        float altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float vspeed;
        float dimPosition;
        float cons;
        float alpha;
        float beaconDirection;
        float beaconRange;

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
            aircraft().hierMesh().chunkVisible("fakeNose_D0", false);
            aircraft().hierMesh().chunkVisible("fakeNose_D1", false);
            aircraft().hierMesh().chunkVisible("fakeNose_D2", false);
            aircraft().hierMesh().chunkVisible("fakeNose_D3", false);
            aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(isFocused())
        {
            aircraft().hierMesh().chunkVisible("fakeNose_D0", aircraft().hierMesh().isChunkVisible("Nose_D0"));
            aircraft().hierMesh().chunkVisible("fakeNose_D1", aircraft().hierMesh().isChunkVisible("Nose_D1"));
            aircraft().hierMesh().chunkVisible("fakeNose_D2", aircraft().hierMesh().isChunkVisible("Nose_D2"));
            aircraft().hierMesh().chunkVisible("fakeNose_D3", aircraft().hierMesh().isChunkVisible("Nose_D3"));
            aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
            aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
            super.doFocusLeave();
        }
    }

    public CockpitJU_88C2()
    {
        super("3DO/Cockpit/Ju-88C-2/hier.him", "he111");
        bNeedSetUp = true;
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictFlap = 0.0F;
        pictGear = 0.0F;
        pictManf1 = 1.0F;
        pictManf2 = 1.0F;
        prevFuel = 0.0F;
        this.cockpitNightMats = (new String[] {
            "88a4_I_Set1", "88a4_I_Set2", "88a4_I_Set3", "88a4_I_Set4", "88a4_I_Set5", "88a4_I_Set6", "88a4_I_SetEng", "88a4_SlidingGlass", "88gardinen", "88InstrGenrl", 
            "Peil1", "skala", "Pedal", "Peil2", "PeilNeedle", "radar_glass"
        });
        setNightMats(false);
        setNew.dimPosition = setOld.dimPosition = 0.0F;
        this.cockpitDimControl = !this.cockpitDimControl;
        interpPut(new Interpolater(), null, Time.current(), null);
        if(this.acoustics != null)
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    public void toggleDim()
    {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        resetYPRmodifier();
        Cockpit.xyz[2] = 0.06815F * interp(setNew.dimPosition, setOld.dimPosition, f);
        this.mesh.chunkSetAngles("Z_Trim1", cvt(this.fm.CT.getTrimElevatorControl(), -0.5F, 0.5F, -750F, 750F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zColumn1", 7F * (pictElev = 0.85F * pictElev + 0.15F * this.fm.CT.ElevatorControl), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zColumn2", 52.2F * (pictAiler = 0.85F * pictAiler + 0.15F * this.fm.CT.AileronControl), 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[2] = -0.1F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("zPedalL", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = 0.1F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("zPedalR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zTurret1A", 0.0F, this.fm.turret[0].tu[0], 0.0F);
        this.mesh.chunkSetAngles("zTurret1B", 0.0F, this.fm.turret[0].tu[1], 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[2] = pictFlap = 0.85F * pictFlap + 0.00948F * this.fm.CT.FlapsControl;
        this.mesh.chunkSetLocate("zFlaps1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = pictGear = 0.85F * pictGear + 0.007095F * this.fm.CT.GearControl;
        this.mesh.chunkSetLocate("zGear1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = -0.1134F * setNew.prop1;
        this.mesh.chunkSetLocate("zPitch1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = -0.1134F * setNew.prop2;
        this.mesh.chunkSetLocate("zPitch2", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = -0.1031F * setNew.throttle1;
        this.mesh.chunkSetLocate("zThrottle1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = -0.1031F * setNew.throttle2;
        this.mesh.chunkSetLocate("zThrottle2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("TempMeter", floatindex(cvt(Atmosphere.temperature((float)this.fm.Loc.z), 213.09F, 293.09F, 0.0F, 8F), frAirTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Radiator_Sw1", cvt(this.fm.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.0F, -120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Radiator_Sw2", cvt(this.fm.EI.engines[1].getControlRadiator(), 0.0F, 1.0F, 0.0F, -120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSw1", cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 100F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSw2", cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 100F, 0.0F), 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[2] = -(10.27825F * (float)Math.tan(Math.toRadians(setNew.alpha)));
        if(Cockpit.xyz[2] < -2.2699F)
            Cockpit.xyz[2] = -2.2699F;
        this.mesh.chunkSetAngles("zHour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMinute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBall", 0.0F, cvt(getBall(4D), -4F, 4F, -9F, 9F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 6000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt3", (((JU_88C2)aircraft()).fDiveRecoveryAlt * 360F) / 6000F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed", floatindex(cvt(Pitot.Indicator((float)this.fm.Loc.z, this.fm.getSpeedKMH()), 50F, 750F, 0.0F, 14F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", cvt(setNew.vspeed, -15F, 15F, -151F, 151F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zWaterTemp1", cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 64F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zWaterTemp2", cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 120F, 0.0F, 64F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilPress1", cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness(), 0.0F, 7.47F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilPress2", cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness(), 0.0F, 7.47F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuelPress1", cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.32F, 0.0F, 0.5F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuelPress2", cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.32F, 0.0F, 0.5F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM1", cvt(this.fm.EI.engines[0].getRPM(), 600F, 3600F, 0.0F, 331F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM2", cvt(this.fm.EI.engines[1].getRPM(), 600F, 3600F, 0.0F, 331F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ATA1", pictManf1 = 0.9F * pictManf1 + 0.1F * cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 325.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ATA2", pictManf2 = 0.9F * pictManf2 + 0.1F * cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 325.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel1", cvt(this.fm.M.fuel, 0.0F, 1008F, 0.0F, 77F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel2", cvt(this.fm.M.fuel, 0.0F, 1008F, 0.0F, 77F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuelPress", cvt(setNew.cons, 100F, 500F, 0.0F, 240F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", setNew.waypointAzimuth.getDeg(f * 0.1F) - 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass4", -setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass5", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass7", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass8", setNew.waypointAzimuth.getDeg(f * 0.1F) + 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zHORIZ1", -this.fm.Or.getKren(), 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(this.fm.Or.getTangage(), -45F, 45F, 0.045F, -0.045F);
        this.mesh.chunkSetLocate("zHORIZ2", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[2] = 0.02125F * this.fm.CT.getTrimElevatorControl();
        this.mesh.chunkSetLocate("zTRIM1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = 0.02125F * this.fm.CT.getTrimAileronControl();
        this.mesh.chunkSetLocate("zTRIM2", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = 0.02125F * this.fm.CT.getTrimRudderControl();
        this.mesh.chunkSetLocate("zTRIM3", Cockpit.xyz, Cockpit.ypr);
        w.set(this.fm.getW());
        this.fm.Or.transform(w);
        this.mesh.chunkSetAngles("Z_TurnBank1", cvt(w.z, -0.23562F, 0.23562F, 25F, -25F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, cvt(getBall(4D), -4F, 4F, -9F, 9F), 0.0F);
        this.mesh.chunkVisible("XLampGearUpL", this.fm.CT.getGear() < 0.01F || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearDownL", this.fm.CT.getGear() > 0.99F && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearUpR", this.fm.CT.getGear() < 0.01F || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearDownR", this.fm.CT.getGear() > 0.99F && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearUpC", this.fm.CT.getGear() < 0.01F);
        this.mesh.chunkVisible("XLampGearDownC", this.fm.CT.getGear() > 0.99F);
        this.mesh.chunkVisible("XLampFlap1", this.fm.CT.getFlap() < 0.1F);
        this.mesh.chunkVisible("XLampFlap2", this.fm.CT.getFlap() > 0.1F && this.fm.CT.getFlap() < 0.5F);
        this.mesh.chunkVisible("XLampFlap3", this.fm.CT.getFlap() > 0.5F);
        this.mesh.chunkSetAngles("zAH1", 0.0F, cvt(setNew.beaconDirection, -45F, 45F, 14F, -14F), 0.0F);
        this.mesh.chunkSetAngles("zAH2", cvt(setNew.beaconRange, 0.0F, 1.0F, 26.5F, -26.5F), 0.0F, 0.0F);
        this.mesh.chunkVisible("AFN1_RED", isOnBlindLandingMarker());
    }

    public boolean isOnBlindLandingMarker()
    {
        return false;
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 0x80) != 0);
        if((this.fm.AS.astateCockpitState & 2) != 0)
            this.mesh.chunkVisible("XGlassDamage1", true);
        if((this.fm.AS.astateCockpitState & 1) != 0)
            this.mesh.chunkVisible("XGlassDamage5", true);
        if((this.fm.AS.astateCockpitState & 0x40) != 0);
        if((this.fm.AS.astateCockpitState & 4) != 0)
            this.mesh.chunkVisible("XGlassDamage3", true);
        if((this.fm.AS.astateCockpitState & 8) != 0)
        {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XGlassDamage5", true);
        }
        if((this.fm.AS.astateCockpitState & 0x10) != 0)
            this.mesh.chunkVisible("XGlassDamage2", true);
        if((this.fm.AS.astateCockpitState & 0x20) != 0)
            this.mesh.chunkVisible("XGlassDamage6", true);
        retoggleLight();
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private void retoggleLight()
    {
        if(this.cockpitLightControl)
        {
            setNightMats(false);
            setNightMats(true);
        } else
        {
            setNightMats(true);
            setNightMats(false);
        }
    }

    protected void reflectPlaneToModel()
    {
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    private boolean bNeedSetUp;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private float pictAiler;
    private float pictElev;
    private float pictFlap;
    private float pictGear;
    private float pictManf1;
    private float pictManf2;
    private float prevFuel;
    private static final float speedometerScale[] = {
        0.0F, 16F, 35.5F, 60.5F, 88F, 112.5F, 136F, 159.5F, 186.5F, 211.5F, 
        240F, 268F, 295.5F, 321F, 347F
    };
    private static final float frAirTempScale[] = {
        76.5F, 68F, 57F, 44.5F, 29.5F, 14.5F, 1.5F, -10F, -19F
    };
}
