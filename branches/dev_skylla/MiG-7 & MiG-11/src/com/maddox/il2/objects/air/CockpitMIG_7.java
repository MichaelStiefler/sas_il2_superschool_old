package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

/**
 * 
 * @author SAS~Skylla, SAS~Storebror.
 * @see MIG_7.java
 *
 * TODO's: siehe MIG_7.java
**/


public class CockpitMIG_7 extends CockpitPilot{

    private class Variables
    {

        float throttle;
        float prop;
        float altimeter;
        float azimuth;
        float vspeed;
        float mix;
        float xyz[] = {
            0.0F, 0.0F, 0.0F
        };
        float ypr[] = {
            0.0F, 0.0F, 0.0F
        };

        private Variables()
        {
        }

    }

    class Interpolater extends InterpolateRef
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
                setNew.prop = (10F * setOld.prop + fm.EI.engines[0].getControlProp()) / 11F;
                setNew.mix = 0.8F * setOld.mix + 0.2F * fm.EI.engines[0].getControlMix();
                setNew.altimeter = fm.getAltitude();
                if(Math.abs(fm.Or.getKren()) < 30F)
                    setNew.azimuth = (35F * setOld.azimuth + fm.Or.azimut()) / 36F;
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    public CockpitMIG_7()
    {
        super("3do/cockpit/MiG-7/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictFlap = 0.0F;
        pictGear = 0.0F;
        pictDoor = 0.0F;
        pictRadiator = 0.0F;
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        resetYPRmodifier();
//        float f1 = Cockpit.xyz[0];
        Cockpit.xyz[0] = -cvt(fm.CT.getCockpitDoor(), 0.11F, 0.99F, 0.0F, 0.6F);
        mesh.chunkSetLocate("Blister", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        if(fm.CT.saveWeaponControl[2])
            Cockpit.xyz[2] = -0.004F;
        mesh.chunkSetLocate("r_three", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("richag", 0.0F, (pictElev = 0.85F * pictElev + 0.12F * fm.CT.ElevatorControl) * 10F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 15F);
        mesh.chunkSetAngles("Ped_Base", 0.0F, -fm.CT.getRudder() * 18F, 0.0F);
        mesh.chunkSetAngles("LightK", 0.0F, cockpitLightControl ? 60F : 0.0F, 0.0F);
        mesh.chunkSetAngles("LightD", 0.0F, cockpitLightControl ? 60F : 0.0F, 0.0F);
        mesh.chunkSetAngles("PedalL", 0.0F, fm.CT.getRudder() * 18F, 0.0F);
        mesh.chunkSetAngles("PedalR", 0.0F, fm.CT.getRudder() * 18F, 0.0F);
        mesh.chunkSetAngles("TrubkaR", 0.0F, fm.CT.getRudder() * 18F, 0.0F);
        mesh.chunkSetAngles("TrosR", 0.0F, fm.CT.getRudder() * 18F, 0.0F);
        mesh.chunkSetAngles("TrosL", 0.0F, fm.CT.getRudder() * 18F, 0.0F);
//        mesh.chunkSetAngles("zFlapPos1a", 0.0F, 0.0F, cvt(fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, 9F));
        mesh.chunkSetAngles("ANO", 0.0F, fm.AS.bNavLightsOn ? -40F : 0.0F, 0.0F);
        mesh.chunkSetAngles("Fara", 0.0F, fm.AS.bLandingLightOn ? -40F : 0.0F, 0.0F);
        mesh.chunkSetAngles("ZamokR", -85F * (pictDoor = 0.89F * pictDoor + 0.11F * fm.CT.getCockpitDoor()), 0.0F, 0.0F);
        mesh.chunkSetAngles("Flaps", -153F * (pictFlap = 0.89F * pictFlap + 0.11F * fm.CT.FlapsControl), 0.0F, 0.0F);
        mesh.chunkSetAngles("Radiator", 60F * (pictRadiator = 0.89F * pictRadiator + 0.11F * fm.EI.engines[0].getControlRadiator()), 0.0F, 0.0F);
        mesh.chunkSetAngles("Drossel", 0.0F, interp(setNew.throttle, setOld.throttle, f) * -45F * 0.91F, 0.0F);
        mesh.chunkSetAngles("Forsaj", 0.0F, interp(setNew.mix, setOld.mix, f) * -37F * 0.91F, 0.0F);
//        mesh.chunkSetAngles("r_one", 0.0F, -20F * (float)(fm.CT.WeaponControl[0] ? 1 : 0), 0.0F);
//        mesh.chunkSetAngles("r_two", 0.0F, -20F * (float)(fm.CT.WeaponControl[1] ? 1 : 0), 0.0F);
        mesh.chunkSetAngles("r_two", 0.0F, -20F * (float)(fm.CT.WeaponControl[0] ? 1 : 0), 0.0F);
        mesh.chunkSetAngles("r_turn", 0.0F, -20F * fm.CT.BrakeControl, 0.0F);
        if(fm.Gears.isHydroOperable())
            mesh.chunkSetAngles("Gear", -150F * (pictGear = 0.89F * pictGear + 0.11F * fm.CT.GearControl), 0.0F, 0.0F);
        mesh.chunkSetAngles("zAlt1a", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        mesh.chunkSetAngles("zAlt1b", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
//        mesh.chunkSetAngles("zAzimuth1a", 0.0F, cvt(fm.Or.getTangage(), -40F, 40F, -40F, 40F), 0.0F);
        mesh.chunkSetAngles("zAzimuth1b", 0.0F, interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
        mesh.chunkSetLocate("zHorizon1a", setOld.xyz, setOld.ypr);
        mesh.chunkSetAngles("zHorizon1b", 0.0F, fm.Or.getKren(), 0.0F);
        mesh.chunkSetAngles("zManifold1a", 0.0F, cvt(fm.EI.engines[0].getManifoldPressure(), 0.4F, 2.133F, 0.0F, 334.286F), 0.0F);
        mesh.chunkSetAngles("zGas1a", 0.0F, cvt(fm.M.fuel / 0.72F, 0.0F, 300F, 0.0F, 171F), 0.0F);
        mesh.chunkSetAngles("zSpeed1a", 0.0F, floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F);
        w.set(fm.getW());
        fm.Or.transform(w);
        mesh.chunkSetAngles("zTurn1a", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
        mesh.chunkSetAngles("zSlide1a", 0.0F, cvt(getBall(8D), -8F, 8F, -28F, 28F), 0.0F);
        mesh.chunkSetAngles("zVariometer1a", 0.0F, cvt(setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F);
        mesh.chunkSetAngles("zRPM1a", 0.0F, cvt(fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        mesh.chunkSetAngles("zRPM1b", 0.0F, cvt(fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zTOilOut1a", 0.0F, cvt(fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 180F), 0.0F);
        mesh.chunkSetAngles("zOilPrs1a", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 180F), 0.0F);
        mesh.chunkSetAngles("zGasPrs1a", 0.0F, cvt(fm.M.fuel <= 1.0F ? 0.0F : cvt(fm.EI.engines[0].getRPM(), 0.0F, 3050F, 0.0F, 4F), 0.0F, 8F, 0.0F, -180F), 0.0F);
        mesh.chunkSetAngles("zTWater1a", 0.0F, cvt(fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 92F), 0.0F);
        mesh.chunkSetAngles("zTOilIn1a", 0.0F, cvt(fm.EI.engines[0].tOilIn, 0.0F, 120F, 0.0F, 92F), 0.0F);
        mesh.chunkSetAngles("zClock1a", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zClock1b", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkVisible("Z_GearLGreen1", fm.CT.getGear() == 1.0F);
        mesh.chunkVisible("Z_GearRGreen1", fm.CT.getGear() == 1.0F);
        mesh.chunkVisible("Z_GearLRed1", fm.CT.getGear() == 0.0F);
        mesh.chunkVisible("Z_GearRRed1", fm.CT.getGear() == 0.0F);
//        mesh.chunkVisible("Z_Red1", fm.M.fuel < 36F);
//        mesh.chunkVisible("Z_Red2", fm.EI.engines[0].tWaterOut > 110F);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0 || (fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("prib_D1", false);
            mesh.chunkVisible("prib_N1", false);
            mesh.chunkVisible("prib_DD1", true);
            mesh.chunkVisible("zAzimuth1a", false);
            mesh.chunkVisible("zAzimuth1b", false);
            mesh.chunkVisible("zHorizon1a", false);
            mesh.chunkVisible("zHorizon1b", false);
            mesh.chunkVisible("zManifold1a", false);
            mesh.chunkVisible("zVariometer1a", false);
            mesh.chunkVisible("zSpeed1a", false);
            mesh.chunkVisible("zTOilIn1a", false);
        }
        if((fm.AS.astateCockpitState & 8) != 0 || (fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("prib_D2", false);
            mesh.chunkVisible("prib_N2", false);
            mesh.chunkVisible("prib_DD2", true);
            mesh.chunkVisible("zRPM1a", false);
            mesh.chunkVisible("zRPM1b", false);
            mesh.chunkVisible("zAlt1a", false);
            mesh.chunkVisible("zAlt1b", false);
            mesh.chunkVisible("zGas1a", false);
            mesh.chunkVisible("zTurn1a", false);
            mesh.chunkVisible("zTOilOut1a", false);
            mesh.chunkVisible("zOilPrs1a", false);
            mesh.chunkVisible("zGasPrs1a", false);
        }
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("panel_d", false);
            mesh.chunkVisible("panel_n", false);
            mesh.chunkVisible("panel_dd", true);
            mesh.chunkVisible("zTWater1a", false);
        }
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
//        mesh.chunkVisible("Engine1_D0", true);
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if((fm.AS.astateCockpitState & 4) == 0 && (fm.AS.astateCockpitState & 0x10) == 0)
        {
            mesh.chunkVisible("prib_D1", !cockpitLightControl);
            mesh.chunkVisible("prib_N1", cockpitLightControl);
        }
        if((fm.AS.astateCockpitState & 8) == 0 && (fm.AS.astateCockpitState & 0x20) == 0)
        {
            mesh.chunkVisible("prib_D2", !cockpitLightControl);
            mesh.chunkVisible("prib_N2", cockpitLightControl);
        }
        if((fm.AS.astateCockpitState & 0x40) == 0)
        {
            mesh.chunkVisible("panel_d", !cockpitLightControl);
            mesh.chunkVisible("panel_n", cockpitLightControl);
        }
        if(cockpitLightControl)
            mesh.materialReplace("Strelki", "Strelki_n");
        else
            mesh.materialReplace("Strelki", "Strelki");
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private float pictFlap;
    private float pictGear;
    private float pictDoor;
    private float pictRadiator;
    private static final float speedometerScale[] = {
        0.0F, 0.0F, 15.5F, 50F, 95.5F, 137F, 182.5F, 212F, 230F, 242F, 
        254.5F, 267.5F, 279F, 292F, 304F, 317F, 329.5F, 330F
    };



}
