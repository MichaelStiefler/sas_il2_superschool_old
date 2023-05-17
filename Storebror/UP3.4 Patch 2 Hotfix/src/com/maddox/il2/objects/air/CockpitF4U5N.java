package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitF4U5N extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitF4U5N.this.fm != null) {
                CockpitF4U5N.this.setTmp = CockpitF4U5N.this.setOld;
                CockpitF4U5N.this.setOld = CockpitF4U5N.this.setNew;
                CockpitF4U5N.this.setNew = CockpitF4U5N.this.setTmp;
                CockpitF4U5N.this.setNew.throttle = (0.85F * CockpitF4U5N.this.setOld.throttle) + (CockpitF4U5N.this.fm.CT.PowerControl * 0.15F);
                CockpitF4U5N.this.setNew.prop = (0.85F * CockpitF4U5N.this.setOld.prop) + (CockpitF4U5N.this.fm.CT.getStepControl() * 0.15F);
                CockpitF4U5N.this.setNew.altimeter = CockpitF4U5N.this.fm.getAltitude();
                float f = CockpitF4U5N.this.waypointAzimuth(10F);
                CockpitF4U5N.this.setNew.azimuth.setDeg(CockpitF4U5N.this.setOld.azimuth.getDeg(1.0F), CockpitF4U5N.this.fm.Or.azimut());
                CockpitF4U5N.this.setNew.waypointAzimuth.setDeg(CockpitF4U5N.this.setOld.waypointAzimuth.getDeg(0.1F), f);
                CockpitF4U5N.this.setNew.vspeed = ((199F * CockpitF4U5N.this.setOld.vspeed) + CockpitF4U5N.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        float      prop;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    public CockpitF4U5N() {
        super("3DO/Cockpit/F4U-5N/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictFlap = 0.0F;
        this.pictGear = 0.0F;
        this.pictManf = 1.0F;
        this.bNeedSetUp = true;
        this.cockpitNightMats = (new String[] { "aigtemp", "instru01", "instru02", "instru03", "instru04", "instru05" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
        this.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float delta) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        TypeRadarAPSCarrier apsc = (TypeRadarAPSCarrier) this.aircraft();
        if (apsc.getRadarAPS().hasMessages()) {
            this.mesh.chunkSetAngles("RadarSWGun", 0.0F, 0.0F, 60F);
        } else {
            this.mesh.chunkSetAngles("RadarSWGun", 0.0F, 0.0F, 0.0F);
        }
        if (apsc.getRadarAPS().hasPower()) {
            this.mesh.chunkVisible("RadarScreen", true);
            this.mesh.chunkVisible("RadarScreenOFF", false);
            this.mesh.chunkSetAngles("RadarSWOn", 0.0F, 30F, 0.0F);
            apsc.getRadarAPS().radarScanCockpit(this.mesh, 10, 4);
        } else {
            this.mesh.chunkVisible("RadarScreen", false);
            this.mesh.chunkVisible("RadarScreenOFF", true);
            this.mesh.chunkSetAngles("RadarSWOn", 0.0F, 0.0F, 0.0F);
            apsc.getRadarAPS().clearRadarMarks(this.mesh, 10, 4);
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.625F);
        Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.06845F);
        Cockpit.ypr[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 1.0F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Trim1", 0.0F, 722F * this.fm.CT.getTrimAileronControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2", 0.0F, 722F * this.fm.CT.getTrimRudderControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim3", 0.0F, 722F * this.fm.CT.getTrimElevatorControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps1", 0.0F, -37F * (this.pictFlap = (0.75F * this.pictFlap) + (0.25F * this.fm.CT.FlapsControl)), 0.0F);
        this.mesh.chunkSetAngles("Z_Gear1", 0.0F, 46F * (this.pictGear = (0.8F * this.pictGear) + (0.2F * this.fm.CT.GearControl)), 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, 51.8F * this.interp(this.setNew.throttle, this.setOld.throttle, delta), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 0.0F, 35F - (35F * this.interp(this.setNew.prop, this.setOld.prop, delta)), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 0.0F, 47F * this.fm.EI.engines[0].getControlMix(), 0.0F);
        this.mesh.chunkSetAngles("Z_Supercharger1", 0.0F, -15F * this.fm.EI.engines[0].getControlCompressor(), 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, 10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, -10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 8F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 8F);
        this.mesh.chunkSetAngles("Z_WingFold1", 0.0F, 54F * this.fm.CT.wingControl, 0.0F);
        this.mesh.chunkSetAngles("Z_Hook1", 0.0F, -57F * this.fm.CT.arrestorControl, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, delta), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, delta), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 796.3598F, 0.0F, 11F), CockpitF4U5N.speedometerScale), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, this.cvt(this.getBall(7D), -7F, 7F, -10F, 10F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, -0.0285F, 0.0285F);
        this.mesh.chunkSetLocate("Z_TurnBank4", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_TurnBank5", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitF4U5N.variometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -90F + this.setNew.waypointAzimuth.getDeg(delta * 0.1F), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, this.setNew.azimuth.getDeg(delta), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", 0.0F, -this.setNew.azimuth.getDeg(delta), 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 600F, 0.0F, 300F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres1", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 1.0F, 0.0F, 255F), 0.0F);
        this.mesh.chunkSetAngles("Z_TankPres1", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 1.0F, 0.0F, 255F), 0.0F);
        this.mesh.chunkSetAngles("Z_HydPres1", 0.0F, this.fm.Gears.bIsHydroOperable ? 200F : 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 97.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.pictManf = (0.9F * this.pictManf) + (0.1F * this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 344.5F)), 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", 0.0F, this.cvt(this.fm.EI.engines[0].tOilIn, 20F, 120F, 0.0F, 78F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilIn * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 256F), 0.0F);
        this.resetYPRmodifier();
        if (this.fm.Gears.cgear) {
            Cockpit.xyz[2] = -0.028F * this.fm.CT.getGearC();
        }
        this.mesh.chunkSetLocate("Z_GearInd1", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        if (this.fm.Gears.lgear) {
            Cockpit.xyz[2] = -0.028F * this.fm.CT.getGearL();
        }
        this.mesh.chunkSetLocate("Z_GearInd2", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        if (this.fm.Gears.rgear) {
            Cockpit.xyz[2] = -0.028F * this.fm.CT.getGearR();
        }
        this.mesh.chunkSetLocate("Z_GearInd3", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkVisible("Z_WaterInjL1", this.fm.EI.engines[0].getControlAfterburner());
        this.mesh.chunkVisible("Z_StallWarnL1", this.fm.getAOA() > this.fm.AOA_Crit);
        this.mesh.chunkVisible("Z_CarbAirL1", false);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Pricel_D0", false);
            this.mesh.chunkVisible("Pricel_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("Z_RPM1", false);
            this.mesh.chunkVisible("Z_RPM2", false);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_TurnBank1", false);
            this.mesh.chunkVisible("Z_TurnBank2", false);
            this.mesh.chunkVisible("Z_Climb1", false);
            this.mesh.chunkVisible("Z_Hour1", false);
            this.mesh.chunkVisible("Z_Minute1", false);
            this.mesh.chunkVisible("Z_FuelPres1", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        this.fm.AS.getClass();
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh h = this.aircraft().hierMesh();
        Mat m = h.material(h.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", m);
        this.mesh.chunkVisible("1A_Bars", false);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    private void retoggleLight() {
        if (this.cockpitLightControl) {
            this.setNightMats(false);
            this.setNightMats(true);
        } else {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictFlap;
    private float              pictGear;
    private float              pictManf;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 15.5F, 77F, 175F, 275F, 360F, 412F, 471F, 539F, 610.5F, 669.75F, 719F };
    private static final float variometerScale[]  = { -175.5F, -160.5F, -145.5F, -128F, -100F, -65.5F, 0.0F, 65.5F, 100F, 128F, 145.5F, 160.5F, 175.5F };
}
