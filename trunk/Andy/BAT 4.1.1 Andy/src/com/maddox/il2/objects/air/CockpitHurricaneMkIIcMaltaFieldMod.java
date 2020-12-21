// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.11.2020 11:23:28
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   CockpitHurricaneMkIIcMaltaFieldMod.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Way;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Autopilotage;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.BaseGameVersion;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit, Aircraft, HurricaneMkIIcMaltaFieldMod

public class CockpitHurricaneMkIIcMaltaFieldMod extends com.maddox.il2.objects.air.CockpitPilot
{
    private class Variables
    {

        float throttle;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;
        float mk2w;
        float mk2d;
        float mk2wa;
        float mk2da;

        Variables()
        {
            mk2w = 1.0F;
            mk2d = 1.0F;
            mk2wa = -36F;
            mk2da = 15.5F;
        }
    }

    class Interpolater extends com.maddox.il2.engine.InterpolateRef
    {

        public boolean tick()
        {
            if(bNeedSetUp)
            {
                reflectPlaneMats();
                bNeedSetUp = false;
            }
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle = (10F * setOld.throttle + fm.CT.PowerControl) / 11F;
                setNew.altimeter = fm.getAltitude();
                if(java.lang.Math.abs(fm.Or.getKren()) < 30F)
                    setNew.azimuth = (35F * setOld.azimuth + -fm.Or.getYaw()) / 36F;
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth) + com.maddox.il2.ai.World.Rnd().nextFloat(-30F, 30F)) / 11F;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                setNew.mk2w = 0.9F * setOld.mk2w + 0.1F * com.maddox.il2.objects.air.CockpitHurricaneMkIIcMaltaFieldMod.Mk2TargetWingspanScale[((com.maddox.il2.objects.air.HurricaneMkIIcMaltaFieldMod)aircraft()).Mk2WingspanType];
                setNew.mk2d = 0.9F * setOld.mk2d + 0.1F * com.maddox.il2.objects.air.CockpitHurricaneMkIIcMaltaFieldMod.Mk2TargetDistanceScale[(int)(((com.maddox.il2.objects.air.HurricaneMkIIcMaltaFieldMod)aircraft()).Mk2Distance / 50F) - 3];
                setNew.mk2wa = 0.9F * setOld.mk2wa + 0.1F * com.maddox.il2.objects.air.CockpitHurricaneMkIIcMaltaFieldMod.Mk2TargetWingspanAng[((com.maddox.il2.objects.air.HurricaneMkIIcMaltaFieldMod)aircraft()).Mk2WingspanType];
                setNew.mk2da = 0.9F * setOld.mk2da + 0.1F * com.maddox.il2.objects.air.CockpitHurricaneMkIIcMaltaFieldMod.Mk2TargetDistanceAng[(int)(((com.maddox.il2.objects.air.HurricaneMkIIcMaltaFieldMod)aircraft()).Mk2Distance / 50F) - 3];
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    protected float waypointAzimuth()
    {
        com.maddox.il2.ai.WayPoint waypoint = fm.AP.way.curr();
        if(waypoint == null)
        {
            return 0.0F;
        } else
        {
            waypoint.getP(tmpP);
            tmpV.sub(tmpP, fm.Loc);
            return (float)(57.295779513082323D * java.lang.Math.atan2(-tmpV.y, tmpV.x));
        }
    }

    protected void setCameraOffset()
    {
        cameraCenter.add(0.035000000000000003D, 0.0D, 0.0D);
    }

    public CockpitHurricaneMkIIcMaltaFieldMod()
    {
        super("3DO/Cockpit/HurricaneMkIIc_Malta_Fieldmod/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        bNeedSetUp = true;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        cockpitNightMats = (new java.lang.String[] {
            "BORT2", "BORT4", "COMPASS", "prib_five_fin", "prib_five", "prib_four", "prib_one_fin", "prib_one", "prib_six", "prib_three", 
            "prib_two", "pricel"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, com.maddox.rts.Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            adjustReticle();
            bNeedSetUp = false;
        }
        if(java.lang.Math.abs((setNew.mk2w - setOld.mk2w) / setNew.mk2w) > 0.0003F || java.lang.Math.abs((setNew.mk2d - setOld.mk2d) / setNew.mk2d) > 0.0003F)
            adjustReticle();
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[1] = cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.55F);
        mesh.chunkSetLocate("FONAR2", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        mesh.chunkSetLocate("FONAR_GLASS2", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        mesh.chunkVisible("XLampEngHeat", (fm.EI.engines[0].tOilOut > 115F) | (fm.EI.engines[0].tWaterOut > 115F));
        float f1 = fm.CT.getGear();
        float f2 = fm.CT.getGear();
        if(com.maddox.sas1946.il2.util.BaseGameVersion.is412orLater())
        {
            f1 = fm.CT.getGearL();
            f2 = fm.CT.getGearR();
        }
        mesh.chunkVisible("XLampGearUpL", f1 == 0.0F && fm.Gears.lgear);
        mesh.chunkVisible("XLampGearUpR", f2 == 0.0F && fm.Gears.rgear);
        mesh.chunkVisible("XLampGearDownL", f1 == 1.0F && fm.Gears.lgear);
        mesh.chunkVisible("XLampGearDownR", f2 == 1.0F && fm.Gears.rgear);
        resetYPRmodifier();
        mesh.chunkSetAngles("RUSBase", 0.0F, 10F * (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl), 0.0F);
        mesh.chunkSetAngles("RUS", -35F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl), 0.0F, 0.0F);
        mesh.chunkVisible("RUS_GUN", !fm.CT.WeaponControl[0]);
        mesh.chunkSetAngles("RUS_TORM", 0.0F, 0.0F, -40F * fm.CT.getBrake());
        com.maddox.il2.objects.air.Cockpit.xyz[2] = 0.01625F * fm.CT.getAileron();
        mesh.chunkSetLocate("RUS_TAND_L", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        com.maddox.il2.objects.air.Cockpit.xyz[2] = -com.maddox.il2.objects.air.Cockpit.xyz[2];
        mesh.chunkSetLocate("RUS_TAND_R", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        mesh.chunkSetAngles("RUD", 0.0F, -81.81F * interp(setNew.throttle, setOld.throttle, f), 0.0F);
        resetYPRmodifier();
        if(fm.CT.GearControl == 0.0F && fm.CT.getGear() != 0.0F)
        {
            com.maddox.il2.objects.air.Cockpit.xyz[0] = 0.05F;
            com.maddox.il2.objects.air.Cockpit.xyz[2] = 0.0F;
        } else
        if(fm.CT.GearControl == 1.0F && fm.CT.getGear() != 1.0F)
        {
            com.maddox.il2.objects.air.Cockpit.xyz[0] = -0.05F;
            com.maddox.il2.objects.air.Cockpit.xyz[2] = 0.0F;
        } else
        if(java.lang.Math.abs(fm.CT.FlapsControl - fm.CT.getFlap()) > 0.02F)
            if(fm.CT.FlapsControl - fm.CT.getFlap() > 0.0F)
            {
                com.maddox.il2.objects.air.Cockpit.xyz[0] = -0.05F;
                com.maddox.il2.objects.air.Cockpit.xyz[2] = 0.0345F;
            } else
            {
                com.maddox.il2.objects.air.Cockpit.xyz[0] = 0.05F;
                com.maddox.il2.objects.air.Cockpit.xyz[2] = 0.0345F;
            }
        mesh.chunkSetLocate("SHAS_RUCH_T", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        mesh.chunkSetAngles("PROP_CONTR", (fm.CT.getStepControl() >= 0.0F ? fm.CT.getStepControl() : 1.0F - interp(setNew.throttle, setOld.throttle, f)) * -60F, 0.0F, 0.0F);
        mesh.chunkSetAngles("PEDALY", 9F * fm.CT.getRudder(), 0.0F, 0.0F);
        mesh.chunkSetAngles("COMPASS_M", -interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("SHKALA_DIRECTOR", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("STREL_ALT_LONG", 0.0F, 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F));
        mesh.chunkSetAngles("STREL_ALT_SHORT", 0.0F, 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F));
        mesh.chunkSetAngles("STRELKA_BOOST", 0.0F, 0.0F, -cvt(fm.EI.engines[0].getManifoldPressure(), 0.5173668F, 2.72369F, -70F, 250F));
        mesh.chunkSetAngles("STRELKA_FUEL", 0.0F, 0.0F, cvt(fm.M.fuel, 0.0F, 307.7F, 0.0F, 60F));
        mesh.chunkSetAngles("STRELK_FUEL_LB", 0.0F, -cvt(fm.M.fuel > 1.0F ? 10F * fm.EI.engines[0].getPowerOutput() : 0.0F, 0.0F, 10F, 0.0F, 10F), 0.0F);
        mesh.chunkSetAngles("STRELKA_RPM", 0.0F, 0.0F, -floatindex(cvt(fm.EI.engines[0].getRPM(), 1000F, 5000F, 2.0F, 10F), rpmScale));
        mesh.chunkSetAngles("STRELK_TEMP_OIL", 0.0F, 0.0F, -cvt(fm.EI.engines[0].tOilIn, 40F, 100F, 0.0F, 270F));
        mesh.chunkSetAngles("STRELK_TEMP_RAD", 0.0F, 0.0F, -floatindex(cvt(fm.EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 14F), radScale));
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[2] = 0.05865F * fm.EI.engines[0].getControlRadiator();
        mesh.chunkSetLocate("zRadFlap", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        w.set(fm.getW());
        fm.Or.transform(w);
        mesh.chunkSetAngles("STREL_TURN_DOWN", 0.0F, 0.0F, -cvt(w.z, -0.23562F, 0.23562F, -48F, 48F));
        mesh.chunkSetAngles("STRELK_TURN_UP", 0.0F, 0.0F, -cvt(getBall(8D), -8F, 8F, 35F, -35F));
        mesh.chunkVisible("STRELK_V_SHORT", false);
        if(bFAF)
            mesh.chunkSetAngles("STRELK_V_LONG", 0.0F, 0.0F, -floatindex(cvt(com.maddox.il2.fm.Pitot.Indicator((float)fm.Loc.z, fm.getSpeed()), 0.0F, 143.0528F, 0.0F, 32F), speedometerScaleFAF));
        else
            mesh.chunkSetAngles("STRELK_V_LONG", 0.0F, 0.0F, -floatindex(cvt(com.maddox.il2.fm.Pitot.Indicator((float)fm.Loc.z, fm.getSpeed()), 0.0F, 180.0555F, 0.0F, 35F), speedometerScale));
        mesh.chunkSetAngles("STRELKA_VY", 0.0F, 0.0F, -floatindex(cvt(setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), variometerScale));
        mesh.chunkSetAngles("STRELKA_GOR_FAF", 0.0F, 0.0F, fm.Or.getKren());
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[2] = cvt(fm.Or.getTangage(), -45F, 45F, 0.032F, -0.032F);
        mesh.chunkSetLocate("STRELKA_GOR_RAF", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        mesh.chunkSetLocate("STRELKA_GOS_FAF", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        mesh.chunkSetAngles("STRELKA_HOUR", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("STRELKA_MINUTE", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("STRELKA_SECUND", 0.0F, cvt(((com.maddox.il2.ai.World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("HullDamage3", true);
            mesh.chunkVisible("XGlassDamage4", true);
            mesh.materialReplace("prib_four", "prib_four_damage");
            mesh.materialReplace("prib_four_night", "prib_four_damage_night");
            mesh.materialReplace("prib_three", "prib_three_damage");
            mesh.materialReplace("prib_three_night", "prib_three_damage_night");
            mesh.chunkVisible("STRELK_TEMP_OIL", false);
            mesh.chunkVisible("STRELK_TEMP_RAD", false);
            mesh.chunkVisible("STRELKA_BOOST", false);
            mesh.chunkVisible("STRELKA_FUEL", false);
        }
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("HullDamage4", true);
            mesh.chunkVisible("HullDamage2_RAF", true);
            mesh.chunkVisible("HullDamage2_FAF", true);
            mesh.materialReplace("prib_three", "prib_three_damage");
            mesh.materialReplace("prib_three_night", "prib_three_damage_night");
            mesh.chunkVisible("STRELK_TEMP_OIL", false);
            mesh.chunkVisible("STRELK_TEMP_RAD", false);
            mesh.chunkVisible("STRELKA_BOOST", false);
            mesh.chunkVisible("STRELKA_FUEL", false);
        }
        if((fm.AS.astateCockpitState & 0x80) != 0);
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("HullDamage3", true);
            mesh.chunkVisible("HullDamage6", true);
            mesh.chunkVisible("XGlassDamage3", true);
            mesh.materialReplace("prib_two", "prib_two_damage");
            mesh.materialReplace("prib_two_night", "prib_two_damage_night");
            mesh.chunkVisible("STREL_ALT_LONG", false);
            mesh.chunkVisible("STREL_ALT_SHORT", false);
            mesh.chunkVisible("STRELKA_RPM", false);
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("HullDamage5", true);
            mesh.chunkVisible("HullDamage6", true);
        }
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("HullDamage1", true);
            if(bFAF)
                mesh.chunkVisible("HullDamage2_FAF", true);
            else
                mesh.chunkVisible("HullDamage2_RAF", true);
            mesh.materialReplace("prib_one", "prib_one_damage");
            mesh.materialReplace("prib_one_fin", "prib_one_fin_damage");
            mesh.materialReplace("prib_one_night", "prib_one_damage_night");
            mesh.materialReplace("prib_one_fin_night", "prib_one_fin_damage_night");
            mesh.chunkVisible("STRELK_V_LONG", false);
            mesh.chunkVisible("STRELK_V_SHORT", false);
            mesh.chunkVisible("STRELKA_GOR_FAF", false);
            mesh.chunkVisible("STRELKA_GOR_RAF", false);
            mesh.chunkVisible("STRELKA_VY", false);
            mesh.chunkVisible("STREL_TURN_DOWN", false);
        }
        if((fm.AS.astateCockpitState & 1) != 0)
        {
            mesh.chunkVisible("XGlassDamage3", true);
            mesh.chunkVisible("XGlassDamage4", true);
        }
        if((fm.AS.astateCockpitState & 2) != 0)
        {
            mesh.chunkVisible("XGlassDamage1", true);
            mesh.chunkVisible("XGlassDamage2", true);
        }
        retoggleLight();
    }

    protected void reflectPlaneMats()
    {
        if(aircraft().getRegiment().country().equals("fi"))
        {
            bFAF = true;
            mesh.chunkVisible("PRIBORY_RAF", false);
            mesh.chunkVisible("PRIBORY_FAF", true);
            mesh.chunkVisible("STRELKA_GOR_RAF", false);
            mesh.chunkVisible("STRELKA_GOR_FAF", true);
            mesh.chunkVisible("STRELKA_GOS_FAF", true);
            mesh.chunkVisible("STRELKA_HOUR", true);
            mesh.chunkVisible("STRELKA_MINUTE", true);
            mesh.chunkVisible("STRELKA_SECUND", true);
        } else
        {
            bFAF = false;
            mesh.chunkVisible("PRIBORY_RAF", true);
            mesh.chunkVisible("PRIBORY_FAF", false);
            mesh.chunkVisible("STRELKA_GOR_RAF", true);
            mesh.chunkVisible("STRELKA_GOR_FAF", false);
            mesh.chunkVisible("STRELKA_GOS_FAF", false);
            mesh.chunkVisible("STRELKA_HOUR", false);
            mesh.chunkVisible("STRELKA_MINUTE", false);
            mesh.chunkVisible("STRELKA_SECUND", false);
        }
        com.maddox.il2.engine.HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
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

    private void adjustReticle()
    {
        RetScale = setNew.mk2d * 1.15F;
        RetOffset = (1.0F - RetScale) / 2.0F;
        BarScale = (setNew.mk2d / setNew.mk2w) * 1.24F;
        BarOffset = (1.0F - BarScale) / 2.0F;
        setReticleSize();
        mesh.chunkSetAngles("RingB", setNew.mk2wa, 0.0F, 0.0F);
        mesh.chunkSetAngles("RingT", setNew.mk2da, 0.0F, 0.0F);
    }

    private void setReticleSize()
    {
        int i = mesh.materialFind("Reticle");
        if(i < 0)
            return;
        com.maddox.il2.engine.Mat mat = mesh.material(i);
        if(mat != null && mat.isValidLayer(0))
        {
            mat.setLayer(0);
            mat.set((byte)11, BarOffset);
            mat.set((byte)12, RetOffset);
            mat.set((byte)13, BarScale);
            mat.set((byte)14, RetScale);
            mat.set((short)0, true);
        }
        if(mat.isValidLayer(1))
        {
            mat.setLayer(1);
            mat.set((byte)11, BarOffset);
            mat.set((byte)12, RetOffset);
            mat.set((byte)13, BarScale);
            mat.set((byte)14, RetScale);
            mat.set((short)0, true);
        }
        if(mat.isValidLayer(2))
        {
            mat.setLayer(2);
            mat.set((byte)11, RetOffset);
            mat.set((byte)12, RetOffset);
            mat.set((byte)13, RetScale);
            mat.set((byte)14, RetScale);
            mat.set((short)0, true);
        }
        if(mat.isValidLayer(3))
        {
            mat.setLayer(3);
            mat.set((byte)11, RetOffset);
            mat.set((byte)12, RetOffset);
            mat.set((byte)13, RetScale);
            mat.set((byte)14, RetScale);
            mat.set((short)0, true);
        }
    }

    private float BarOffset;
    private float BarScale;
    private float RetOffset;
    private float RetScale;
    private static final float Mk2TargetWingspanScale[] = {
        0.87F, 0.9426F, 1.0F, 1.16F, 1.3232F, 1.4501F, 1.5481F, 1.7129F, 1.7401F, 1.9107F, 
        2.0302F, 2.1505F, 2.3202F, 2.6102F, 2.8069F, 2.9002F, 3.1259F
    };
    private static final float Mk2TargetDistanceScale[] = {
        0.5714F, 0.6154F, 0.6667F, 0.7272F, 0.8F, 0.8889F, 1.0F, 1.1429F, 1.3333F, 1.6F, 
        2.0F, 2.6667F
    };
    private static final float Mk2TargetWingspanAng[] = {
        -29F, -26.6F, -24.632F, -19.25F, -13.97F, -9.25F, -6.1235F, -0.8695F, 0.0F, 4.998F, 
        8.5F, 11.6125F, 16F, 21.25F, 25.4875F, 27.5F, 32.3625F
    };
    private static final float Mk2TargetDistanceAng[] = {
        33F, 23F, 14.5F, 6F, 0.125F, -5.75F, -10.75F, -15.75F, -20.125F, -24.5F, 
        -28.25F, -32F
    };
    public float limits6DoF[] = {
        0.7F, 0.055F, -0.07F, 0.11F, 0.14F, -0.11F, 0.03F, -0.03F
    };
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public com.maddox.JGP.Vector3f w;
    private boolean bFAF;
    private float pictAiler;
    private float pictElev;
    private boolean bNeedSetUp;
    private static final float speedometerScale[] = {
        0.0F, 0.0F, 1.0F, 3F, 7.5F, 34.5F, 46F, 63F, 76F, 94F, 
        112.5F, 131F, 150F, 168.5F, 187F, 203F, 222F, 242.5F, 258.5F, 277F, 
        297F, 315.5F, 340F, 360F, 376.5F, 392F, 407F, 423.5F, 442F, 459F, 
        476F, 492.5F, 513F, 534.5F, 552F, 569.5F
    };
    private static final float speedometerScaleFAF[] = {
        0.0F, 0.0F, 2.0F, 6F, 21F, 40F, 56F, 72.5F, 89.5F, 114F, 
        135.5F, 157F, 182.5F, 205F, 227.5F, 246.5F, 265.5F, 286F, 306F, 326F, 
        345.5F, 363F, 385F, 401F, 414.5F, 436.5F, 457F, 479F, 496.5F, 515F, 
        539F, 559.5F, 576.5F
    };
    private static final float radScale[] = {
        0.0F, 3F, 7F, 13.5F, 21.5F, 27F, 34.5F, 50.5F, 71F, 94F, 
        125F, 161F, 202.5F, 253F, 315.5F
    };
    private static final float rpmScale[] = {
        0.0F, 0.0F, 0.0F, 22F, 58F, 103.5F, 152.5F, 193.5F, 245F, 281.5F, 
        311.5F
    };
    private static final float variometerScale[] = {
        -158F, -110F, -63.5F, -29F, 0.0F, 29F, 63.5F, 110F, 158F
    };
    private com.maddox.JGP.Point3d tmpP;
    private com.maddox.JGP.Vector3d tmpV;

    static 
    {
        com.maddox.rts.Property.set(com.maddox.rts.CLASS.THIS(), "normZN", 0.77F);
    }



    static 
    {
        com.maddox.rts.Property.set(com.maddox.rts.CLASS.THIS(), "normZNs", new float[] {
            0.55F, 0.55F, 0.75F, 0.55F
        });
    }








}