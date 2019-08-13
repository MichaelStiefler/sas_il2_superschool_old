package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitKI_48 extends CockpitPilot
{
    private class Variables
    {

        float throttle1;
        float throttle2;
        float prop1;
        float prop2;
        float mix1;
        float mix2;
        private float manifold1;
        private float manifold2;
        float altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork waypointDirection;
        float vspeed;
        float inert;
        float turn;





        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            waypointDirection = new AnglesFork();
            manifold1 = 0.0F;
            manifold2 = 0.0F;
        }

    }

    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle1 = 0.9F * setOld.throttle1 + 0.1F * fm.EI.engines[0].getControlThrottle();
                setNew.prop1 = 0.9F * setOld.prop1 + 0.1F * fm.EI.engines[0].getControlProp();
                setNew.mix1 = 0.8F * setOld.mix1 + 0.2F * fm.EI.engines[0].getControlMix();
                setNew.throttle2 = 0.9F * setOld.throttle2 + 0.1F * fm.EI.engines[1].getControlThrottle();
                setNew.prop2 = 0.9F * setOld.prop2 + 0.1F * fm.EI.engines[1].getControlProp();
                setNew.mix2 = 0.8F * setOld.mix2 + 0.2F * fm.EI.engines[1].getControlMix();
                setNew.manifold1 = 0.8F * setOld.manifold1 + 0.2F * fm.EI.engines[0].getManifoldPressure();
                setNew.manifold2 = 0.8F * setOld.manifold2 + 0.2F * fm.EI.engines[1].getManifoldPressure();
                setNew.altimeter = fm.getAltitude();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), -fm.Or.azimut() - 90F);
                w.set(fm.getW());
                fm.Or.transform(w);
                setNew.turn = (12F * setOld.turn + w.z) / 13F;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                float f = waypointAzimuth();
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), (f - setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                setNew.waypointDirection.setDeg(setOld.waypointDirection.getDeg(1.0F), f);
                setNew.inert = 0.999F * setOld.inert + 0.001F * (fm.EI.engines[0].getStage() == 6 ? 0.867F : 0.0F);
                w.set(fm.getW());
                fm.Or.transform(w);
                setNew.turn = (33F * setOld.turn + w.z) / 34F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    protected float waypointAzimuth()
    {
        WayPoint waypoint = fm.AP.way.curr();
        if(waypoint == null)
            return 0.0F;
        waypoint.getP(tmpP);
        tmpV.sub(tmpP, fm.Loc);
        float f;
        for(f = (float)(57.295779513082323D * Math.atan2(-tmpV.y, tmpV.x)); f <= -180F; f += 180F);
        for(; f > 180F; f -= 180F);
        return f;
    }

    public CockpitKI_48()
    {
        super("3DO/Cockpit/Ki48-II/Cockpitki48.him", "he111");
        bNeedSetUp = true;
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        cockpitNightMats = (new String[] {
            "Pressure_Gauges", "Needs_Instr", "maininstrumentspanel4", "instructions", "instru_main_pan", "instru_main_pan2", "instru_main_pan3", "dmaininstrumentspanel4", "dinstru_main_pan", "dinstru_main_pan2", 
            "dinstru_main_pan3", "GP1_d1", "GP1", "GP7", "GP9"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        limits6DoF = (new float[] {
            0.7F, 0.055F, -0.07F, 0.11F, 0.15F, -0.11F, 0.03F, -0.025F
        });
        if(acoustics != null)
            acoustics.globFX = new ReverbFXRoom(0.45F);
        AircraftLH.printCompassHeading = true;
        limits6DoF = (new float[] {
            0.7F, 0.055F, -0.07F, 0.08F, 0.12F, -0.11F, 0.04F, -0.03F
        });
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        mesh.chunkSetAngles("Z_Gears", fm.CT.GearControl > 0.5F ? -60F : 0.0F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Throttle_L", -31F * interp(setNew.throttle1, setOld.throttle1, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Throttle_R", -31F * interp(setNew.throttle2, setOld.throttle2, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Propitch_L", -720F * interp(setNew.prop1, setOld.prop1, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Propitch_R", -720F * interp(setNew.prop2, setOld.prop2, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Mixture_L", 41.67F * interp(setNew.mix1, setOld.mix1, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Mixture_R", 41.67F * interp(setNew.mix2, setOld.mix2, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Compass2", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(fm.Or.getTangage(), -45F, 45F, -0.03F, 0.03F);
        mesh.chunkSetAngles("Z_AH3", 0.0F, 0.0F, fm.Or.getKren());
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(fm.Or.getTangage(), -45F, 45F, -0.03F, 0.03F);
        mesh.chunkSetLocate("Z_AH4", Cockpit.xyz, Cockpit.ypr);
        w.set(fm.getW());
        fm.Or.transform(w);
        float f1 = getBall(7D);
        mesh.chunkSetAngles("Z_TurnBank1", cvt(f1, -5F, 5F, 8.5F, -8.5F), 0.0F, 0.0F);
        mesh.chunkVisible("X_LGearDown", fm.CT.getGear() == 1.0F && fm.Gears.lgear);
        mesh.chunkVisible("X_RGearDown", fm.CT.getGear() == 1.0F && fm.Gears.lgear);
        mesh.chunkVisible("X_LGearUp", fm.CT.getGear() == 0.0F);
        mesh.chunkVisible("X_RGearUp", fm.CT.getGear() == 0.0F);
        mesh.chunkVisible("X_TailGearDown", fm.CT.getGear() == 1.0F && fm.Gears.cgear);
        mesh.chunkVisible("X_TailGearUp", fm.CT.getGear() == 0.0F);
        mesh.chunkSetAngles("Z_CColumn_Elev", 0.0F, -(pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 10F, 0.0F);
        mesh.chunkSetAngles("Z_CColumn", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 15F, 0.0F);
        resetYPRmodifier();
        if(fm.CT.saveWeaponControl[3])
            Cockpit.xyz[1] = -0.002545F;
        mesh.chunkSetLocate("Z_Trig1", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[1] = 0.05F * fm.CT.getRudder();
        mesh.chunkSetLocate("Z_Rudder_L", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = -0.05F * fm.CT.getRudder();
        mesh.chunkSetLocate("Z_Rudder_R", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_Magn_L", cvt(fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, -85F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Magn_R", cvt(fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 85F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_ClockHour", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("Z_Need_ClockMinute", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("Z_Need_Turn", 0.0F, cvt(setNew.turn, -0.23562F, 0.23562F, 21F, -21F), 0.0F);
        float f2 = getBall(4D);
        mesh.chunkSetAngles("Z_Need_Bank", 0.0F, cvt(f2, -4F, 4F, -13F, 13F), 0.0F);
        mesh.chunkSetAngles("Z_Need_Climb", 0.0F, floatindex(cvt(setNew.vspeed, -30F, 30F, 0.0F, 12F), variometerScale), 0.0F);
        mesh.chunkSetAngles("Z_Need_AirSpeed", 0.0F, floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 700F, 0.0F, 14F), speedometerScale), 0.0F);
        mesh.chunkSetAngles("Z_Need_LTachio", 0.0F, cvt(fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 38.8F), 0.0F);
        mesh.chunkSetAngles("Z_Need_RTachio", 0.0F, cvt(fm.EI.engines[1].getRPM(), 0.0F, 3500F, 0.0F, 38.8F), 0.0F);
        int i = 0;
        if(fm.EI.getCurControl(1) && !fm.EI.getCurControl(0))
            i = 1;
        mesh.chunkSetAngles("Z_cyltempsel", 0.0F, i == 0 ? 0.0F : 54F, 0.0F);
        mesh.chunkSetAngles("Z_Need_cyltemp", 0.0F, cvt(fm.EI.engines[i].tWaterOut, 0.0F, 360F, 0.0F, 56F), 0.0F);
        mesh.chunkSetAngles("Z_Need_alt_km", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("Z_Need_Compass", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("Z_Need_FuelQuant", 0.0F, cvt(fm.M.fuel / fm.M.maxFuel, 0.0F, 1.0F, 5F, 335F), 0.0F);
        mesh.chunkSetAngles("Z_Need_L_oilpress", 0.0F, cvt(60F + fm.EI.engines[0].tOilIn * 0.222F, 0.0F, 100F, 0.0F, 209F), 0.0F);
        mesh.chunkSetAngles("Z_Need_R_oilpress", 0.0F, cvt(60F + fm.EI.engines[1].tOilIn * 0.222F, 0.0F, 100F, 0.0F, -212F), 0.0F);
        mesh.chunkSetAngles("Z_Need_L_oiltemp", 0.0F, cvt(fm.EI.engines[0].tOilIn, 0.0F, 130F, 0.0F, 57F), 0.0F);
        mesh.chunkSetAngles("Z_Need_R_oiltemp", 0.0F, cvt(fm.EI.engines[1].tOilIn, 0.0F, 130F, 0.0F, 57F), 0.0F);
        mesh.chunkSetAngles("Z_Supercharger_L", 0.0F, -40F * (float)fm.EI.engines[0].getControlCompressor() + 20F, 0.0F);
        mesh.chunkSetAngles("Z_Supercharger_R", 0.0F, -40F * (float)fm.EI.engines[1].getControlCompressor() + 20F, 0.0F);
        mesh.chunkSetAngles("Z_Need_L_Manifold", 0.0F, 180F + cvt(setNew.manifold1, 0.200068F, 1.799932F, -164F, 164F), 0.0F);
        mesh.chunkSetAngles("Z_Need_R_Manifold", 0.0F, 180F + cvt(setNew.manifold2, 0.200068F, 1.799932F, -164F, 164F), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[1] = 0.05F * cvt(fm.EI.engines[0].tWaterOut, 0.0F, 324F, 0.0F, 1.0F);
        mesh.chunkSetLocate("Z_Need_ExhGasAnalys1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = 0.05F * cvt(fm.EI.engines[1].tWaterOut, 0.0F, 324F, 0.0F, 1.0F);
        mesh.chunkSetLocate("Z_Need_ExhGasAnalys2", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkVisible("X_LGearDown", fm.CT.getGear() > 0.99F && fm.Gears.lgear);
        mesh.chunkVisible("X_RGearDown", fm.CT.getGear() > 0.99F && fm.Gears.rgear);
        mesh.chunkVisible("X_LGearUp", fm.CT.getGear() < 0.01F && fm.Gears.lgear);
        mesh.chunkVisible("X_RGearUp", fm.CT.getGear() < 0.01F && fm.Gears.rgear);
        mesh.chunkVisible("X_TailGearDown", fm.CT.getGear() > 0.99F && fm.Gears.cgear);
        mesh.chunkVisible("X_TailGearUp", fm.CT.getGear() < 0.01F && fm.Gears.cgear);
        mesh.chunkSetAngles("Z_Need_L_fuelpress", 0.0F, fm.M.fuel > 1.0F ? 129F : 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Need_R_fuelpress", 0.0F, fm.M.fuel > 1.0F ? -129F : 0.0F, 0.0F);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 1) != 0)
        {
            mesh.chunkVisible("XGlassDamage2", true);
            mesh.chunkVisible("XGlassDamage3", true);
            mesh.chunkVisible("XGlassDamage4", true);
            mesh.chunkVisible("XHullDamage3", true);
        }
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("nstr2", false);
            mesh.chunkVisible("DInstr2", true);
            mesh.chunkVisible("Z_Fuel1", false);
            mesh.chunkVisible("Z_Need_L_oiltemp", false);
            mesh.chunkVisible("Z_Need_alt_km", false);
            mesh.chunkVisible("Z_Need_L_Manifold", false);
            mesh.chunkVisible("XHullDamage3", true);
        }
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("XGlassDamage3", true);
            mesh.chunkVisible("XHullDamage1", true);
        }
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("XGlassDamage1", true);
            mesh.chunkVisible("XGlassDamage5", true);
            mesh.chunkVisible("XHullDamage3", true);
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("XGlassDamage4", true);
            mesh.chunkVisible("XHullDamage2", true);
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("XGlassDamage1", true);
            mesh.chunkVisible("XGlassDamage6", true);
            mesh.chunkVisible("XHullDamage3", true);
        }
        if((fm.AS.astateCockpitState & 2) != 0 && (fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("Panel_D0", false);
            mesh.chunkVisible("Panel_D1", false);
            mesh.chunkVisible("Panel_D2", true);
            mesh.chunkVisible("Z_Altimeter1", false);
            mesh.chunkVisible("Z_Altimeter2", false);
            mesh.chunkVisible("Z_Speedometer1", false);
            mesh.chunkVisible("Z_Speedometer2", false);
            mesh.chunkVisible("Z_AirTemp", false);
            mesh.chunkVisible("Z_Pres2", false);
            mesh.chunkVisible("Z_RPM1", false);
            mesh.chunkVisible("Z_RPM2", false);
            mesh.chunkVisible("Z_InertGas", false);
            mesh.chunkVisible("Z_FuelPres2", false);
            mesh.chunkVisible("Z_Oilpres1", false);
            mesh.chunkVisible("Z_Oilpres2", false);
        }
        retoggleLight();
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
        {
            light1.light.setEmit(1.0F, 0.5F);
            light2.light.setEmit(1.0F, 0.5F);
            light3.light.setEmit(1.0F, 0.5F);
            light4.light.setEmit(1.0F, 0.5F);
            setNightMats(true);
        } else
        {
            light1.light.setEmit(0.0F, 0.0F);
            light2.light.setEmit(0.0F, 0.0F);
            light3.light.setEmit(0.0F, 0.0F);
            light4.light.setEmit(0.0F, 0.0F);
            setNightMats(false);
        }
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

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
    }

    private boolean bNeedSetUp;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private LightPointActor light1;
    private LightPointActor light2;
    private LightPointActor light3;
    private LightPointActor light4;
    public Vector3f w;
    private float pictAiler;
    private float pictElev;
    private static final float speedometerScale[] = {
        0.0F, 8.6F, 23.6F, 64.2F, 114.5F, 172.8F, 239.4F, 299F, 360F, 417F, 
        479F, 533F, 582F, 627F, 657F
    };
    private static final float variometerScale[] = {
        -180F, -162F, -143F, -125F, -108F, -90F, 0.0F, 90F, 108F, 125F, 
        143F, 162F, 180F
    };
    private Point3d tmpP;
    private Vector3d tmpV;
}
