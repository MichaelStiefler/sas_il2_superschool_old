package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitF7F_3N extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitF7F_3N.this.fm != null) {
                CockpitF7F_3N.this.setTmp = CockpitF7F_3N.this.setOld;
                CockpitF7F_3N.this.setOld = CockpitF7F_3N.this.setNew;
                CockpitF7F_3N.this.setNew = CockpitF7F_3N.this.setTmp;
                CockpitF7F_3N.this.setNew.throttle1 = (0.9F * CockpitF7F_3N.this.setOld.throttle1) + (0.1F * CockpitF7F_3N.this.fm.EI.engines[0].getControlThrottle());
                CockpitF7F_3N.this.setNew.prop1 = (0.9F * CockpitF7F_3N.this.setOld.prop1) + (0.1F * CockpitF7F_3N.this.fm.EI.engines[0].getControlProp());
                CockpitF7F_3N.this.setNew.mix1 = (0.8F * CockpitF7F_3N.this.setOld.mix1) + (0.2F * CockpitF7F_3N.this.fm.EI.engines[0].getControlMix());
                CockpitF7F_3N.this.setNew.throttle2 = (0.9F * CockpitF7F_3N.this.setOld.throttle2) + (0.1F * CockpitF7F_3N.this.fm.EI.engines[1].getControlThrottle());
                CockpitF7F_3N.this.setNew.prop2 = (0.9F * CockpitF7F_3N.this.setOld.prop2) + (0.1F * CockpitF7F_3N.this.fm.EI.engines[1].getControlProp());
                CockpitF7F_3N.this.setNew.mix2 = (0.8F * CockpitF7F_3N.this.setOld.mix2) + (0.2F * CockpitF7F_3N.this.fm.EI.engines[1].getControlMix());
                CockpitF7F_3N.this.setNew.man1 = (0.92F * CockpitF7F_3N.this.setOld.man1) + (0.08F * CockpitF7F_3N.this.fm.EI.engines[0].getManifoldPressure());
                CockpitF7F_3N.this.setNew.man2 = (0.92F * CockpitF7F_3N.this.setOld.man2) + (0.08F * CockpitF7F_3N.this.fm.EI.engines[1].getManifoldPressure());
                CockpitF7F_3N.this.setNew.altimeter = CockpitF7F_3N.this.fm.getAltitude();
                if (CockpitF7F_3N.this.useRealisticNavigationInstruments()) {
                    CockpitF7F_3N.this.setNew.waypointAzimuth.setDeg(CockpitF7F_3N.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitF7F_3N.this.getBeaconDirection());
                } else {
                    CockpitF7F_3N.this.setNew.waypointAzimuth.setDeg(CockpitF7F_3N.this.setOld.waypointAzimuth.getDeg(0.1F), CockpitF7F_3N.this.waypointAzimuth() - CockpitF7F_3N.this.fm.Or.azimut());
                }
                CockpitF7F_3N.this.setNew.azimuth.setDeg(CockpitF7F_3N.this.setOld.azimuth.getDeg(1.0F), CockpitF7F_3N.this.fm.Or.azimut());
                CockpitF7F_3N.this.setNew.vspeed = ((199F * CockpitF7F_3N.this.setOld.vspeed) + CockpitF7F_3N.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      throttle2;
        float      prop1;
        float      prop2;
        float      mix1;
        float      mix2;
        float      man1;
        float      man2;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(5F);
    }

    public CockpitF7F_3N() {
        super("3DO/Cockpit/F7F-3N/hier.him", "bf109");
        this.bNeedSetUp = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.cockpitNightMats = (new String[] { "256-10_Gauges6_DMG", "256-10_Gauges6", "256-16_Gauges7_DMG", "256-16_Gauges7", "256-5_Gauges1_DMG", "256-5_Gauges1", "256-6_Gauges2_DMG", "256-6_Gauges2", "256-7_Gauges3_DMG", "256-7_Gauges3", "256-8_Gauges4_DMG", "256-8_Gauges4", "256-9_Gauges5_DMG", "256-9_Gauges5", "256-18", "256-21", "256-22", "128_Gauge_DF", "128_Gauge_DF_DMG" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
        this.limits6DoF = (new float[] { 0.7F, 0.055F, -0.07F, 0.11F, 0.12F, -0.11F, 0.04F, -0.03F });
    }

    public void reflectWorldToInstruments(float f) {
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
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, -0.835F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, -70F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", 0.0F, -70F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 0.0F, -70F * this.interp(this.setNew.prop1, this.setOld.prop1, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2", 0.0F, -70F * this.interp(this.setNew.prop2, this.setOld.prop2, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 0.0F, -47F * this.interp(this.setNew.mix1, this.setOld.mix1, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture2", 0.0F, -47F * this.interp(this.setNew.mix2, this.setOld.mix2, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Supercharger1", 0.0F, 73F - (73F * this.fm.EI.engines[0].getControlCompressor()), 0.0F);
        this.mesh.chunkSetAngles("Z_Supercharger2", 0.0F, 73F - (73F * this.fm.EI.engines[1].getControlCompressor()), 0.0F);
        this.mesh.chunkSetAngles("Z_BombBay1", 0.0F, 40F * this.fm.CT.BayDoorControl, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = -0.1F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_LeftPedal1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = 0.1F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_RightPedal1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column1", 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 7F, 0.0F);
        if (this.fm.Gears.lgear) {
            this.mesh.chunkSetAngles("Z_GearLInd", 0.0F, 86F * this.fm.CT.getGear(), 0.0F);
        }
        if (this.fm.Gears.rgear) {
            this.mesh.chunkSetAngles("Z_GearRInd", 0.0F, -86F * this.fm.CT.getGear(), 0.0F);
        }
        if (this.fm.Gears.cgear) {
            this.mesh.chunkSetAngles("Z_GearCInd", 0.0F, 86F * this.fm.CT.getGear(), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_FlapInd", 0.0F, -67.5F * this.fm.CT.getFlap(), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 804.6721F, 0.0F, 10F), CockpitF7F_3N.speedometerScale), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 22F, -22F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, -16F, 16F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_Climb1", 0.0F, this.cvt(this.setNew.vspeed, -10.159F, 10.159F, -180F, 180F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_RPM1", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 305F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_RPM2", 0.0F, this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 5000F, 0.0F, 305F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 1344F, 0.0F, 70.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres1", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.3F, 0.0F, 301F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres2", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.3F, 0.0F, 301F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 87.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", 0.0F, this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 350F, 0.0F, 87.5F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.cvt(this.setNew.man1, 0.3386378F, 1.693189F, 0.0F, 285.5F), 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.cvt(this.setNew.man1, 0.3386378F, World.Rnd().nextFloat(1.0F, 2.0F), 0.0F, 285.5F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Pres2", 0.0F, this.cvt(this.setNew.man2, 0.3386378F, 1.693189F, 0.0F, 285.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 50F, 150F, 0.0F, 77F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oil2", 0.0F, this.cvt(this.fm.EI.engines[1].tOilOut, 50F, 150F, 0.0F, 77F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 302.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres2", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness()), 0.0F, 7.45F, 0.0F, 302.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_CarbIn1", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -15F, 55F, -35F, 35F), 0.0F);
        this.mesh.chunkSetAngles("Z_CarbIn2", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -15F, 55F, -35F, 35F), 0.0F);
        this.mesh.chunkSetAngles("Z_Hydro1", 0.0F, this.cvt(this.fm.Gears.isHydroOperable() ? 0.8F : 0.0F, 0.0F, 1.0F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_OxyPres1", 0.0F, 228F, 0.0F);
        this.mesh.chunkSetAngles("Z_OxyQ1", 0.0F, 279F, 0.0F);
        this.mesh.chunkSetAngles("Z_AH1", 0.0F, this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, -0.025F, 0.025F);
        this.mesh.chunkSetLocate("Z_AH2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkVisible("Z_GearGreen1", this.fm.CT.getGear() > 0.95F);
        this.mesh.chunkVisible("Z_GearRed1", (this.fm.CT.getGear() < 0.05F) || !this.fm.Gears.lgear || !this.fm.Gears.rgear);
        this.mesh.chunkSetAngles("Z_DF", 0.0F, this.cvt(this.setNew.waypointAzimuth.getDeg(f * 0.2F), -25F, 25F, -30F, 30F), 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Pricel_D0", false);
            this.mesh.chunkVisible("Pricel_D1", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Z_Pres2", false);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Oilpres1", false);
            this.mesh.chunkVisible("Z_FuelPres1", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("Z_DF", false);
            this.mesh.chunkVisible("DF_gauge_D0", false);
            this.mesh.chunkVisible("DF_gauge_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XHullDamage3", true);
        }
        this.retoggleLight();
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

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLIn_D0", !hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingRIn_D0", !hiermesh.isChunkVisible("WingRIn_D0"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    private boolean            bNeedSetUp;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 16.5F, 79.5F, 143F, 206.5F, 229.5F, 251F, 272.5F, 294F, 316F, 339.5F };

    static {
        Property.set(CockpitF7F_3N.class, "normZNs", new float[] { 0.5F, 0.5F, 1.5F, 0.5F });
    }

}
