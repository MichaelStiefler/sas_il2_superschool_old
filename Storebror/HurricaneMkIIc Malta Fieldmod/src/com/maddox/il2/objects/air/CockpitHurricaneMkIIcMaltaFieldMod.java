package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.BaseGameVersion;

public class CockpitHurricaneMkIIcMaltaFieldMod extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitHurricaneMkIIcMaltaFieldMod.this.bNeedSetUp) {
                CockpitHurricaneMkIIcMaltaFieldMod.this.reflectPlaneMats();
                CockpitHurricaneMkIIcMaltaFieldMod.this.bNeedSetUp = false;
            }
            if (CockpitHurricaneMkIIcMaltaFieldMod.this.fm != null) {
                CockpitHurricaneMkIIcMaltaFieldMod.this.setTmp = CockpitHurricaneMkIIcMaltaFieldMod.this.setOld;
                CockpitHurricaneMkIIcMaltaFieldMod.this.setOld = CockpitHurricaneMkIIcMaltaFieldMod.this.setNew;
                CockpitHurricaneMkIIcMaltaFieldMod.this.setNew = CockpitHurricaneMkIIcMaltaFieldMod.this.setTmp;
                CockpitHurricaneMkIIcMaltaFieldMod.this.setNew.throttle = (10F * CockpitHurricaneMkIIcMaltaFieldMod.this.setOld.throttle + CockpitHurricaneMkIIcMaltaFieldMod.this.fm.CT.PowerControl) / 11F;
                CockpitHurricaneMkIIcMaltaFieldMod.this.setNew.altimeter = CockpitHurricaneMkIIcMaltaFieldMod.this.fm.getAltitude();
                if (Math.abs(CockpitHurricaneMkIIcMaltaFieldMod.this.fm.Or.getKren()) < 30F)
                    CockpitHurricaneMkIIcMaltaFieldMod.this.setNew.azimuth = (35F * CockpitHurricaneMkIIcMaltaFieldMod.this.setOld.azimuth + -CockpitHurricaneMkIIcMaltaFieldMod.this.fm.Or.getYaw()) / 36F;
                if (CockpitHurricaneMkIIcMaltaFieldMod.this.setOld.azimuth > 270F && CockpitHurricaneMkIIcMaltaFieldMod.this.setNew.azimuth < 90F)
                    CockpitHurricaneMkIIcMaltaFieldMod.this.setOld.azimuth -= 360F;
                if (CockpitHurricaneMkIIcMaltaFieldMod.this.setOld.azimuth < 90F && CockpitHurricaneMkIIcMaltaFieldMod.this.setNew.azimuth > 270F)
                    CockpitHurricaneMkIIcMaltaFieldMod.this.setOld.azimuth += 360F;
                CockpitHurricaneMkIIcMaltaFieldMod.this.setNew.waypointAzimuth = (10F * CockpitHurricaneMkIIcMaltaFieldMod.this.setOld.waypointAzimuth
                        + (CockpitHurricaneMkIIcMaltaFieldMod.this.waypointAzimuth() - CockpitHurricaneMkIIcMaltaFieldMod.this.setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
                CockpitHurricaneMkIIcMaltaFieldMod.this.setNew.vspeed = (199F * CockpitHurricaneMkIIcMaltaFieldMod.this.setOld.vspeed + CockpitHurricaneMkIIcMaltaFieldMod.this.fm.getVertSpeed()) / 200F;

                // +++ Kumpel Code Insert +++
                // calculate reticle scaling and ring rotation parameters from:
                // ((Hurricane)aircraft()).Mk2WingspanType and ((Hurricane)aircraft()).Mk2Distance
                // reticle parameters
                CockpitHurricaneMkIIcMaltaFieldMod.this.setNew.mk2w = 0.9F * CockpitHurricaneMkIIcMaltaFieldMod.this.setOld.mk2w + 0.1F * CockpitHurricaneMkIIcMaltaFieldMod.Mk2TargetWingspanScale[((HurricaneMkIIcMaltaFieldMod) CockpitHurricaneMkIIcMaltaFieldMod.this.aircraft()).Mk2WingspanType];
                // distance must be converted to integer
                CockpitHurricaneMkIIcMaltaFieldMod.this.setNew.mk2d = 0.9F * CockpitHurricaneMkIIcMaltaFieldMod.this.setOld.mk2d + 0.1F * CockpitHurricaneMkIIcMaltaFieldMod.Mk2TargetDistanceScale[(int) ((((HurricaneMkIIcMaltaFieldMod) CockpitHurricaneMkIIcMaltaFieldMod.this.aircraft()).Mk2Distance) / 50F) - 3];
                // gunsight ring positions
                CockpitHurricaneMkIIcMaltaFieldMod.this.setNew.mk2wa = 0.9F * CockpitHurricaneMkIIcMaltaFieldMod.this.setOld.mk2wa + 0.1F * CockpitHurricaneMkIIcMaltaFieldMod.Mk2TargetWingspanAng[((HurricaneMkIIcMaltaFieldMod) CockpitHurricaneMkIIcMaltaFieldMod.this.aircraft()).Mk2WingspanType];
                // distance must be converted to integer
                CockpitHurricaneMkIIcMaltaFieldMod.this.setNew.mk2da = 0.9F * CockpitHurricaneMkIIcMaltaFieldMod.this.setOld.mk2da + 0.1F * CockpitHurricaneMkIIcMaltaFieldMod.Mk2TargetDistanceAng[(int) ((((HurricaneMkIIcMaltaFieldMod) CockpitHurricaneMkIIcMaltaFieldMod.this.aircraft()).Mk2Distance) / 50F) - 3];
                // --- Kumpel Code Insert ---

            }
            return true;
        }

        Interpolater() {}
    }

    private class Variables {

        float throttle;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;

        // +++ Kumpel Code Insert +++
        float mk2w;           // for progressive change
        float mk2d;
        float mk2wa;
        float mk2da;
        // --- Kumpel Code Insert ---

        Variables() {
            this.mk2w = 1.0F; // initial values (from tables)
            this.mk2d = 1.0F;
            this.mk2wa = -36.0F;
            this.mk2da = 15.5F;
        }

    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) {
            return 0.0F;
        } else {
            waypoint.getP(this.tmpP);
            this.tmpV.sub(this.tmpP, this.fm.Loc);
            return (float) (180D / Math.PI * Math.atan2(-this.tmpV.y, this.tmpV.x));
        }
    }

    protected void setCameraOffset() {
        this.cameraCenter.add(0.035D, 0.0D, 0.0D);
    }

    public CockpitHurricaneMkIIcMaltaFieldMod() {
        super("3DO/Cockpit/HurricaneMkIIc_Malta_Fieldmod/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "BORT2", "BORT4", "COMPASS", "prib_five_fin", "prib_five", "prib_four", "prib_one_fin", "prib_one", "prib_six", "prib_three", "prib_two", "pricel" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
//        this.limits6DoF = (new float[] { 0.7F, 0.055F, -0.07F, 0.11F, 0.14F, -0.11F, 0.03F, -0.03F });
        // +++ Kumpel Code Insert +++
//        if (this.acoustics != null)
//            this.acoustics.globFX = new ReverbFXRoom(0.2F);
        // --- Kumpel Code Insert ---
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.adjustReticle();
            this.bNeedSetUp = false;
        }
        // +++ Kumpel Code Insert +++
        // calculate reticle parameters, set texture scaling and rotate gunsight rings
        if (Math.abs((this.setNew.mk2w - this.setOld.mk2w) / this.setNew.mk2w) > 0.0003F || Math.abs((this.setNew.mk2d - this.setOld.mk2d) / this.setNew.mk2d) > 0.0003F)
            this.adjustReticle();
        //--- Kumpel Code Insert ---

        // Move Cockpit Door!
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.55F);
        this.mesh.chunkSetLocate("FONAR2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("FONAR_GLASS2", Cockpit.xyz, Cockpit.ypr);

        this.mesh.chunkVisible("XLampEngHeat", (this.fm.EI.engines[0].tOilOut > 115F) | (this.fm.EI.engines[0].tWaterOut > 115F));
        
        // Cross Version Compatibility!
        float gearL = this.fm.CT.getGear();
        float gearR = this.fm.CT.getGear();
        if (BaseGameVersion.is412orLater()) {
            gearL = this.fm.CT.getGearL();
            gearR = this.fm.CT.getGearR();
        }
        this.mesh.chunkVisible("XLampGearUpL", gearL == 0.0F && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearUpR", gearR == 0.0F && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearDownL", gearL == 1.0F && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearDownR", gearR == 1.0F && this.fm.Gears.rgear);
        
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("RUSBase", 0.0F, 10F * (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl), 0.0F);
        this.mesh.chunkSetAngles("RUS", -35F * (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl), 0.0F, 0.0F);
        this.mesh.chunkVisible("RUS_GUN", !this.fm.CT.WeaponControl[0]);
        this.mesh.chunkSetAngles("RUS_TORM", 0.0F, 0.0F, -40F * this.fm.CT.getBrake());
        xyz[2] = 0.01625F * this.fm.CT.getAileron();
        this.mesh.chunkSetLocate("RUS_TAND_L", xyz, ypr);
        xyz[2] = -xyz[2];
        this.mesh.chunkSetLocate("RUS_TAND_R", xyz, ypr);
        this.mesh.chunkSetAngles("RUD", 0.0F, -81.81F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F);
        this.resetYPRmodifier();
        if (this.fm.CT.GearControl == 0.0F && this.fm.CT.getGear() != 0.0F) {
            xyz[0] = 0.05F;
            xyz[2] = 0.0F;
        } else if (this.fm.CT.GearControl == 1.0F && this.fm.CT.getGear() != 1.0F) {
            xyz[0] = -0.05F;
            xyz[2] = 0.0F;
        } else if (Math.abs(this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.02F)
            if (this.fm.CT.FlapsControl - this.fm.CT.getFlap() > 0.0F) {
                xyz[0] = -0.05F;
                xyz[2] = 0.0345F;
            } else {
                xyz[0] = 0.05F;
                xyz[2] = 0.0345F;
            }
        this.mesh.chunkSetLocate("SHAS_RUCH_T", xyz, ypr);
        this.mesh.chunkSetAngles("PROP_CONTR", (this.fm.CT.getStepControl() < 0.0F ? 1.0F - this.interp(this.setNew.throttle, this.setOld.throttle, f) : this.fm.CT.getStepControl()) * -60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PEDALY", 9F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("COMPASS_M", -this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SHKALA_DIRECTOR", this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STREL_ALT_LONG", 0.0F, 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F));
        this.mesh.chunkSetAngles("STREL_ALT_SHORT", 0.0F, 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F));
        this.mesh.chunkSetAngles("STRELKA_BOOST", 0.0F, 0.0F, -this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.5173668F, 2.72369F, -70F, 250F));
        this.mesh.chunkSetAngles("STRELKA_FUEL", 0.0F, 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 307.7F, 0.0F, 60F));
        this.mesh.chunkSetAngles("STRELK_FUEL_LB", 0.0F, -this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 10F * this.fm.EI.engines[0].getPowerOutput(), 0.0F, 10F, 0.0F, 10F), 0.0F);
        this.mesh.chunkSetAngles("STRELKA_RPM", 0.0F, 0.0F, -this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 1000F, 5000F, 2.0F, 10F), rpmScale));
        this.mesh.chunkSetAngles("STRELK_TEMP_OIL", 0.0F, 0.0F, -this.cvt(this.fm.EI.engines[0].tOilIn, 40F, 100F, 0.0F, 270F));
        this.mesh.chunkSetAngles("STRELK_TEMP_RAD", 0.0F, 0.0F, -this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 14F), radScale));
        this.resetYPRmodifier();
        xyz[2] = 0.05865F * this.fm.EI.engines[0].getControlRadiator();
        this.mesh.chunkSetLocate("zRadFlap", xyz, ypr);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("STREL_TURN_DOWN", 0.0F, 0.0F, -this.cvt(this.w.z, -0.23562F, 0.23562F, -48F, 48F));
        this.mesh.chunkSetAngles("STRELK_TURN_UP", 0.0F, 0.0F, -this.cvt(this.getBall(8D), -8F, 8F, 35F, -35F));
        this.mesh.chunkVisible("STRELK_V_SHORT", false);
        if (this.bFAF)
            this.mesh.chunkSetAngles("STRELK_V_LONG", 0.0F, 0.0F, -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeed()), 0.0F, 143.0528F, 0.0F, 32F), speedometerScaleFAF));
        else
            this.mesh.chunkSetAngles("STRELK_V_LONG", 0.0F, 0.0F, -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeed()), 0.0F, 180.0555F, 0.0F, 35F), speedometerScale));
        this.mesh.chunkSetAngles("STRELKA_VY", 0.0F, 0.0F, -this.floatindex(this.cvt(this.setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), variometerScale));
        this.mesh.chunkSetAngles("STRELKA_GOR_FAF", 0.0F, 0.0F, this.fm.Or.getKren());
        this.resetYPRmodifier();
        xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.032F, -0.032F);
        this.mesh.chunkSetLocate("STRELKA_GOR_RAF", xyz, ypr);
        this.mesh.chunkSetLocate("STRELKA_GOS_FAF", xyz, ypr);
        this.mesh.chunkSetAngles("STRELKA_HOUR", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("STRELKA_MINUTE", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("STRELKA_SECUND", 0.0F, this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("HullDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.materialReplace("prib_four", "prib_four_damage");
            this.mesh.materialReplace("prib_four_night", "prib_four_damage_night");
            this.mesh.materialReplace("prib_three", "prib_three_damage");
            this.mesh.materialReplace("prib_three_night", "prib_three_damage_night");
            this.mesh.chunkVisible("STRELK_TEMP_OIL", false);
            this.mesh.chunkVisible("STRELK_TEMP_RAD", false);
            this.mesh.chunkVisible("STRELKA_BOOST", false);
            this.mesh.chunkVisible("STRELKA_FUEL", false);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("HullDamage4", true);
            this.mesh.chunkVisible("HullDamage2_RAF", true);
            this.mesh.chunkVisible("HullDamage2_FAF", true);
            this.mesh.materialReplace("prib_three", "prib_three_damage");
            this.mesh.materialReplace("prib_three_night", "prib_three_damage_night");
            this.mesh.chunkVisible("STRELK_TEMP_OIL", false);
            this.mesh.chunkVisible("STRELK_TEMP_RAD", false);
            this.mesh.chunkVisible("STRELKA_BOOST", false);
            this.mesh.chunkVisible("STRELKA_FUEL", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) == 0)
            ;
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("HullDamage3", true);
            this.mesh.chunkVisible("HullDamage6", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.materialReplace("prib_two", "prib_two_damage");
            this.mesh.materialReplace("prib_two_night", "prib_two_damage_night");
            this.mesh.chunkVisible("STREL_ALT_LONG", false);
            this.mesh.chunkVisible("STREL_ALT_SHORT", false);
            this.mesh.chunkVisible("STRELKA_RPM", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("HullDamage5", true);
            this.mesh.chunkVisible("HullDamage6", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("HullDamage1", true);
            if (this.bFAF)
                this.mesh.chunkVisible("HullDamage2_FAF", true);
            else
                this.mesh.chunkVisible("HullDamage2_RAF", true);
            this.mesh.materialReplace("prib_one", "prib_one_damage");
            this.mesh.materialReplace("prib_one_fin", "prib_one_fin_damage");
            this.mesh.materialReplace("prib_one_night", "prib_one_damage_night");
            this.mesh.materialReplace("prib_one_fin_night", "prib_one_fin_damage_night");
            this.mesh.chunkVisible("STRELK_V_LONG", false);
            this.mesh.chunkVisible("STRELK_V_SHORT", false);
            this.mesh.chunkVisible("STRELKA_GOR_FAF", false);
            this.mesh.chunkVisible("STRELKA_GOR_RAF", false);
            this.mesh.chunkVisible("STRELKA_VY", false);
            this.mesh.chunkVisible("STREL_TURN_DOWN", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        if (this.aircraft().getRegiment().country().equals("fi")) {
            this.bFAF = true;
            this.mesh.chunkVisible("PRIBORY_RAF", false);
            this.mesh.chunkVisible("PRIBORY_FAF", true);
            this.mesh.chunkVisible("STRELKA_GOR_RAF", false);
            this.mesh.chunkVisible("STRELKA_GOR_FAF", true);
            this.mesh.chunkVisible("STRELKA_GOS_FAF", true);
            this.mesh.chunkVisible("STRELKA_HOUR", true);
            this.mesh.chunkVisible("STRELKA_MINUTE", true);
            this.mesh.chunkVisible("STRELKA_SECUND", true);
        } else {
            this.bFAF = false;
            this.mesh.chunkVisible("PRIBORY_RAF", true);
            this.mesh.chunkVisible("PRIBORY_FAF", false);
            this.mesh.chunkVisible("STRELKA_GOR_RAF", true);
            this.mesh.chunkVisible("STRELKA_GOR_FAF", false);
            this.mesh.chunkVisible("STRELKA_GOS_FAF", false);
            this.mesh.chunkVisible("STRELKA_HOUR", false);
            this.mesh.chunkVisible("STRELKA_MINUTE", false);
            this.mesh.chunkVisible("STRELKA_SECUND", false);
        }
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl)
            this.setNightMats(true);
        else
            this.setNightMats(false);
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

    //  reticle size control
    // =============================================================================
    private void adjustReticle() // calculate reticle scaling parameters, set reticle and rings
    {
        // calculate scaling parameters
        this.RetScale = this.setNew.mk2d * 1.15F; // scaling with distance only
        this.RetOffset = (1.0F - this.RetScale) / 2.0F;
        this.BarScale = this.setNew.mk2d / this.setNew.mk2w * 1.24F; // scaling with distance & wingspan
        this.BarOffset = (1.0F - this.BarScale) / 2.0F;
        // set reticle size by scaling material texture
        this.setReticleSize();
        // set gunsight ring angles
        this.mesh.chunkSetAngles("RingB", this.setNew.mk2wa, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RingT", this.setNew.mk2da, 0.0F, 0.0F);
    }
    
    //###############################################################################################
    //reticle size control is based on the assumption that the gap between horizontal lines is adjusted
    //for target wingspan & distance while the circle, which is a standard 100mph size @ 200 yards 
    //(making it 118 Mils in diameter), is scaled with target distance between 150..700 yards
    //###############################################################################################

    private void setReticleSize() // force reticle texture scaling
    {
        int m = this.mesh.materialFind("Reticle");
        Mat mat_ret;

        if (m < 0) {
            //System.out.println( "*** Reticle Material not found");
            return;
        } else {
            mat_ret = this.mesh.material(m);
            //System.out.println( "*** Reticle Material: " + m);
        }

        if (mat_ret != null)
            if (mat_ret.isValidLayer(0)) { // main bar pattern - horizontal only scaling
                mat_ret.setLayer(0);
                mat_ret.set((byte) 11, this.BarOffset); // x.off - fUAdd in tfTextureCoordScale x.off y.off x.sc y.sc
                mat_ret.set((byte) 12, this.RetOffset); // y.off - fVAdd
                mat_ret.set((byte) 13, this.BarScale); // x.sc - fUMul
                mat_ret.set((byte) 14, this.RetScale); // y.sc - fVMul
                mat_ret.set((short) 0, true);
            }
        if (mat_ret.isValidLayer(1)) { // glow bar pattern - horizontal only scaling

            mat_ret.setLayer(1);
            mat_ret.set((byte) 11, this.BarOffset); // x.off - fUAdd in tfTextureCoordScale x.off y.off x.sc y.sc
            mat_ret.set((byte) 12, this.RetOffset); // y.off - fVAdd
            mat_ret.set((byte) 13, this.BarScale); // x.sc - fUMul
            mat_ret.set((byte) 14, this.RetScale); // y.sc - fVMul
            mat_ret.set((short) 0, true);
        }
        if (mat_ret.isValidLayer(2)) { // main ring pattern
            mat_ret.setLayer(2);
            mat_ret.set((byte) 11, this.RetOffset); // x.off - fUAdd in tfTextureCoordScale x.off y.off x.sc y.sc
            mat_ret.set((byte) 12, this.RetOffset); // y.off - fVAdd
            mat_ret.set((byte) 13, this.RetScale); // x.sc - fUMul
            mat_ret.set((byte) 14, this.RetScale); // y.sc - fVMul
            mat_ret.set((short) 0, true);
        }
        if (mat_ret.isValidLayer(3)) { // glow ring pattern
            mat_ret.setLayer(3);
            mat_ret.set((byte) 11, this.RetOffset); // x.off - fUAdd in tfTextureCoordScale x.off y.off x.sc y.sc
            mat_ret.set((byte) 12, this.RetOffset); // y.off - fVAdd
            mat_ret.set((byte) 13, this.RetScale); // x.sc - fUMul
            mat_ret.set((byte) 14, this.RetScale); // y.sc - fVMul
            mat_ret.set((short) 0, true);
        }
    }

    //+++ Kumpel Code Insert +++
    private float              BarOffset;
    private float              BarScale;
    private float              RetOffset;
    private float              RetScale;

    private static final float Mk2TargetWingspanScale[] = {
            0.87F, 0.9426F, 1F, 1.16F, 1.3232F, 1.4501F, 1.5481F, 1.7129F, 1.7401F, 1.9107F, 2.0302F, 2.1505F, 2.3202F, 2.6102F, 2.8069F, 2.9002F, 3.1259F
         // 30ft    Bf109 Fw190 40ft     Ju87    50ft     Bf110     Do17     60ft     Ju88     70ft    He111     80ft     90ft      Ju52   100ft    Fw200
    };
    private static final float Mk2TargetDistanceScale[] = {
            0.5714F, 0.6154F, 0.6667F, 0.7272F, 0.8F, 0.8889F, 1.0F, 1.1429F, 1.3333F, 1.6F, 2.0F, 2.6667F
         //    700      650      600      550   500      450   400      350      300   250   200      150   yards
    };

    private static final float Mk2TargetWingspanAng[]   = {
            -29F, -26.6F, -24.632F, -19.25F, -13.97F, -9.25F, -6.1235F, -0.8695F, 0F, 4.998F, 8.5F, 11.6125F, 16F, 21.25F, 25.4875F, 27.5F, 32.3625F
          // 30ft Bf109     Fw190     40ft     Ju87    50ft     Bf110      Do17  60ft  Ju88   70ft    He111  80ft   90ft      Ju52   100ft    Fw200
    };
            
    private static final float Mk2TargetDistanceAng[]   = {
            33F, 23F, 14.5F, 6F, 0.125F, -5.75F, -10.75F, -15.75F, -20.125F, -24.5F, -28.25F, -32F
         // 700  650   600   550   500     450      400      350       300     250      200   150   yards
    };
    //--- Kumpel Code Insert ---

    public float               limits6DoF[]             = { 0.7F, 0.055F, -0.07F, 0.11F, 0.14F, -0.11F, 0.03F, -0.03F };
//    public float               limits6DoF[]             = { 0.7F, 0.055F, -0.07F, 0.20F, 0.30F, -0.20F, 0.10F, -0.10F };

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private boolean            bFAF;
    private float              pictAiler;
    private float              pictElev;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[]       = { 0.0F, 0.0F, 1.0F, 3F, 7.5F, 34.5F, 46F, 63F, 76F, 94F, 112.5F, 131F, 150F, 168.5F, 187F, 203F, 222F, 242.5F, 258.5F, 277F, 297F, 315.5F, 340F, 360F, 376.5F, 392F, 407F, 423.5F, 442F, 459F, 476F,
            492.5F, 513F, 534.5F, 552F, 569.5F };
    private static final float speedometerScaleFAF[]    = { 0.0F, 0.0F, 2.0F, 6F, 21F, 40F, 56F, 72.5F, 89.5F, 114F, 135.5F, 157F, 182.5F, 205F, 227.5F, 246.5F, 265.5F, 286F, 306F, 326F, 345.5F, 363F, 385F, 401F, 414.5F, 436.5F, 457F, 479F, 496.5F, 515F,
            539F, 559.5F, 576.5F };
    private static final float radScale[]               = { 0.0F, 3F, 7F, 13.5F, 21.5F, 27F, 34.5F, 50.5F, 71F, 94F, 125F, 161F, 202.5F, 253F, 315.5F };
    private static final float rpmScale[]               = { 0.0F, 0.0F, 0.0F, 22F, 58F, 103.5F, 152.5F, 193.5F, 245F, 281.5F, 311.5F };
    private static final float variometerScale[]        = { -158F, -110F, -63.5F, -29F, 0.0F, 29F, 63.5F, 110F, 158F };
    private Point3d            tmpP;
    private Vector3d           tmpV;

    static {
        Property.set(CLASS.THIS(), "normZN", 0.77F);
    }

}
