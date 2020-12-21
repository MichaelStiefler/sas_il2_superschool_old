// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 04.11.2020 17:13:33
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   CockpitKikka.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.Way;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Autopilotage;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit

public class CockpitKikka extends com.maddox.il2.objects.air.CockpitPilot
{
    private class Variables
    {

        float flap;
        float throttle;
        float pitch;
        float mix;
        float gear;
        float tlock;
        float altimeter;
        float manifold;
        com.maddox.il2.ai.AnglesFork azimuth;
        float vspeed;
        float dimPosition;

        private Variables()
        {
            azimuth = new AnglesFork();
        }

        Variables(Variables variables)
        {
            this();
        }
    }

    class Interpolater extends com.maddox.il2.engine.InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.flap = 0.88F * setOld.flap + 0.12F * ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.FlapsControl;
                setNew.tlock = 0.7F * setOld.tlock + 0.3F * (((com.maddox.il2.fm.FlightModelMain) (fm)).Gears.bTailwheelLocked ? 1.0F : 0.0F);
                if(cockpitDimControl)
                {
                    if(setNew.dimPosition > 0.0F)
                        setNew.dimPosition = setOld.dimPosition - 0.05F;
                } else
                if(setNew.dimPosition < 1.0F)
                    setNew.dimPosition = setOld.dimPosition + 0.05F;
                if(((com.maddox.il2.fm.FlightModelMain) (fm)).CT.GearControl < 0.5F && setNew.gear < 1.0F)
                    setNew.gear = setOld.gear + 0.02F;
                if(((com.maddox.il2.fm.FlightModelMain) (fm)).CT.GearControl > 0.5F && setNew.gear > 0.0F)
                    setNew.gear = setOld.gear - 0.02F;
                setNew.throttle = 0.9F * setOld.throttle + 0.1F * ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.PowerControl;
                setNew.manifold = 0.8F * setOld.manifold + 0.2F * ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getManifoldPressure();
                setNew.pitch = 0.9F * setOld.pitch + 0.1F * ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getControlProp();
                setNew.mix = 0.9F * setOld.mix + 0.1F * ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getControlMix();
                setNew.altimeter = fm.getAltitude();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((com.maddox.il2.fm.FlightModelMain) (fm)).Or.azimut());
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    protected float waypointAzimuth()
    {
        com.maddox.il2.ai.WayPoint waypoint = ((com.maddox.il2.fm.FlightModelMain) (super.fm)).AP.way.curr();
        if(waypoint == null)
            return 0.0F;
        waypoint.getP(tmpP);
        tmpV.sub(tmpP, ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc);
        float f;
        for(f = (float)(57.295779513082323D * java.lang.Math.atan2(-((com.maddox.JGP.Tuple3d) (tmpV)).y, ((com.maddox.JGP.Tuple3d) (tmpV)).x)); f <= -180F; f += 180F);
        for(; f > 180F; f -= 180F);
        return f;
    }

    public CockpitKikka()
    {
        super("3DO/Cockpit/Kikka/hier.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        super.cockpitNightMats = (new java.lang.String[] {
            "GP_I", "GP_II", "GP_III", "GP_IV", "GP_V", "GP_VI", "GP_VII"
        });
        setNightMats(false);
        super.cockpitDimControl = !super.cockpitDimControl;
        interpPut(new Interpolater(), null, com.maddox.rts.Time.current(), null);
        if(super.acoustics != null)
            super.acoustics.globFX = new ReverbFXRoom(0.45F);
        super.limits6DoF = (new float[] {
            0.45F, 0.055F, -0.07F, 0.11F, 0.13F, -0.07F, 0.025F, -0.03F
        });
    }

    public void reflectWorldToInstruments(float f)
    {
        com.maddox.il2.objects.air.Cockpit.xyz[1] = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.649F);
        com.maddox.il2.objects.air.Cockpit.xyz[2] = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, -0.034F);
        super.mesh.chunkSetLocate("CanopyB", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetAngles("FLCS", -20F * (pictElev = 0.85F * pictElev + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.ElevatorControl), 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.AileronControl) * 20F);
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[1] = cvt(setNew.gear, 0.0F, 0.05F, 0.0F, -0.008F);
        if(setNew.gear > 0.85F)
            com.maddox.il2.objects.air.Cockpit.xyz[1] = 0.0F;
        super.mesh.chunkSetLocate("GearHandleKnob", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetAngles("GearLockHandle", cvt(setNew.gear, 0.1F, 0.25F, 0.0F, 90F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GearLockSpring", 0.0F, 0.0F, cvt(setNew.gear, 0.1F, 0.25F, 0.0F, 45F));
        super.mesh.chunkSetAngles("GearHandle", cvt(setNew.gear, 0.5F, 1.0F, 0.0F, 90F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("FlapHandle", 0.0F, 0.0F, -60F * setNew.flap);
        resetYPRmodifier();
        if(java.lang.Math.abs(setNew.flap - setOld.flap) > 0.001F)
            com.maddox.il2.objects.air.Cockpit.xyz[2] = -0.008F;
        super.mesh.chunkSetLocate("FlapHandleKnob", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetAngles("TWheelLockLvr", -30F * setNew.tlock, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("ChargerLvr", 0.0F, 0.0F, 40F * (float)((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlCompressor());
        super.mesh.chunkSetAngles("TQHandle", 0.0F, 0.0F, 54.5454F * setNew.throttle);
        super.mesh.chunkSetAngles("PropPitchLvr", 0.0F, 0.0F, 60F * setNew.pitch);
        super.mesh.chunkSetAngles("MixLvr", 0.0F, 0.0F, 60F * cvt(setNew.mix, 1.0F, 1.2F, 0.5F, 1.0F));
        super.mesh.chunkSetAngles("PedalCrossBar", 0.0F, -15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("Pedal_L", 15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getBrake(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Pedal_R", 15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getBrake(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("RudderCable_L", -15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("RudderCable_R", -15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("PriTrigger", 0.0F, 0.0F, ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.saveWeaponControl[0] ? 15F : 0.0F);
        resetYPRmodifier();
        if(((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.saveWeaponControl[1])
            com.maddox.il2.objects.air.Cockpit.xyz[2] = -0.005F;
        super.mesh.chunkSetLocate("SecTrigger", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        if(((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.saveWeaponControl[2])
            com.maddox.il2.objects.air.Cockpit.xyz[2] = -0.005F;
        super.mesh.chunkSetLocate("TreTrigger", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetAngles("CowlFlapLvr", 0.0F, 0.0F, 50F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlRadiator());
        super.mesh.chunkSetAngles("OilCoolerLvr", -86F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlRadiator(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("FBoxSW_ANO", 0.0F, 0.0F, ((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.bNavLightsOn ? 50F : 0.0F);
        super.mesh.chunkSetAngles("FBoxSW_LandLt", 0.0F, 0.0F, ((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.bLandingLightOn ? 50F : 0.0F);
        super.mesh.chunkSetAngles("FBoxSW_Starter", 0.0F, 0.0F, ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getStage() > 0 ? 50F : 0.0F);
        super.mesh.chunkSetAngles("FBoxSW_UVLight", 0.0F, 0.0F, super.cockpitLightControl ? 50F : 0.0F);
        super.mesh.chunkSetAngles("GSDimmArm", cvt(interp(setNew.dimPosition, setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -55F), 0.0F, 0.0F);
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[1] = cvt(interp(setNew.dimPosition, setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 0.05F);
        super.mesh.chunkSetLocate("GSDimmBase", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetAngles("NeedCylTemp", cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tWaterOut, 0.0F, 360F, 0.0F, 75F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("NeedExhTemp", cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tWaterOut, 0.0F, 324F, 0.0F, 75F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("NeedOilTemp", floatindex(cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilIn, 0.0F, 160F, 0.0F, 7F), oilTScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("NeedOilPress", cvt(1.0F + 0.05F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 30F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("NeedFuelPress", cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.6F, 0.0F, 305F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("NeedVMPress", cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 300F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("IgnitionSwitch", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getStage() == 0 ? 0.0F : cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 60F), 0.0F, 0.0F);
        w.set(super.fm.getW());
        ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("NeedTurn", cvt(((com.maddox.JGP.Tuple3f) (w)).z, -0.23562F, 0.23562F, -25F, 25F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("NeedBank", cvt(getBall(8D), -8F, 8F, 10F, -10F), 0.0F, 0.0F);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) == 0 || (((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) == 0 || (((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 4) == 0)
        {
            super.mesh.chunkSetAngles("NeedAHCyl", 0.0F, -((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.getKren(), 0.0F);
            super.mesh.chunkSetAngles("NeedAHBar", 0.0F, 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.getTangage(), -45F, 45F, 20F, -20F));
        }
        super.mesh.chunkSetAngles("NeedClimb", floatindex(cvt(setNew.vspeed, -30F, 30F, 0.5F, 6.5F), vsiNeedleScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("NeedSpeed", floatindex(cvt(com.maddox.il2.fm.Pitot.Indicator((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 1000F, 0.0F, 20F), speedometerScale), 0.0F, 0.0F);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) == 0 || (((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 8) == 0 || (((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) == 0)
        {
            super.mesh.chunkSetAngles("NeedCompass_A", 0.0F, 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.getTangage(), -20F, 20F, 20F, -20F));
            super.mesh.chunkSetAngles("NeedCompass_B", -setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
        }
        super.mesh.chunkSetAngles("NeedAlt_Km", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("NeedAlt_M", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("NeedMin", cvt(com.maddox.il2.ai.World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("NeedHour", cvt(com.maddox.il2.ai.World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("NeedManPress", cvt(setNew.manifold, 0.4000511F, 1.799932F, -144F, 192F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("NeedRPM", floatindex(cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 7F), revolutionsScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("NeedFuel", cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 0.0F, 525F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkVisible("FlareFuelLow", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel < 52.5F && (((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) == 0);
        super.mesh.chunkVisible("FlareGearDn_A", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear() > 0.99F);
        super.mesh.chunkVisible("FlareGearDn_B", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear() > 0.99F);
        super.mesh.chunkVisible("FlareGearUp_A", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear() < 0.01F);
        super.mesh.chunkVisible("FlareGearUp_B", ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear() < 0.01F);
    }

    public void reflectCockpitState()
    {
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 4) != 0)
        {
            super.mesh.materialReplace("GP_II", "DGP_II");
            super.mesh.chunkVisible("NeedManPress", false);
            super.mesh.chunkVisible("NeedRPM", false);
            super.mesh.chunkVisible("NeedFuel", false);
            retoggleLight();
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 8) != 0)
        {
            super.mesh.materialReplace("GP_III", "DGP_III");
            super.mesh.chunkVisible("NeedAlt_Km", false);
            super.mesh.chunkVisible("NeedAlt_M", false);
            super.mesh.chunkVisible("NeedSpeed", false);
            retoggleLight();
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) != 0)
        {
            super.mesh.materialReplace("GP_IV", "DGP_IV");
            super.mesh.chunkVisible("NeedCylTemp", false);
            super.mesh.chunkVisible("NeedOilTemp", false);
            super.mesh.chunkVisible("NeedVAmmeter", false);
            super.mesh.materialReplace("GP_IV_night", "DGP_IV_night");
            retoggleLight();
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) != 0)
        {
            super.mesh.materialReplace("GP_V", "DGP_V");
            super.mesh.chunkVisible("NeedExhTemp", false);
            super.mesh.chunkVisible("NeedOilPress", false);
            super.mesh.chunkVisible("NeedHour", false);
            super.mesh.chunkVisible("NeedMin", false);
            super.mesh.chunkVisible("NeedTurn", false);
            super.mesh.chunkVisible("NeedBank", false);
            retoggleLight();
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x80) != 0)
            super.mesh.chunkVisible("OilSplats", true);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 2) != 0)
        {
            super.mesh.chunkVisible("GunSight_T3", false);
            super.mesh.chunkVisible("GS_Lenz", false);
            super.mesh.chunkVisible("GSGlassMain", false);
            super.mesh.chunkVisible("GSDimmArm", false);
            super.mesh.chunkVisible("GSDimmBase", false);
            super.mesh.chunkVisible("GSGlassDimm", false);
            super.mesh.chunkVisible("DGunSight_T3", true);
            super.mesh.chunkVisible("DGS_Lenz", true);
            super.mesh.chunkVisible("Z_Z_RETICLE", false);
            super.mesh.chunkVisible("Z_Z_MASK", false);
            super.mesh.chunkVisible("DamageGlass2", true);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 1) != 0)
        {
            super.mesh.chunkVisible("DamageGlass1", true);
            super.mesh.chunkVisible("DamageGlass3", true);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) != 0)
        {
            super.mesh.materialReplace("GP_VI", "DGP_VI");
            super.mesh.chunkVisible("NeedFuelPress", false);
            super.mesh.chunkVisible("NeedVMPress", false);
            super.mesh.chunkVisible("NeedClimb", false);
            super.mesh.materialReplace("GP_VI_night", "DGP_VI_night");
            retoggleLight();
            super.mesh.chunkVisible("DamageGlass4", true);
        }
    }

    public void toggleDim()
    {
        super.cockpitDimControl = !super.cockpitDimControl;
    }

    public void toggleLight()
    {
        super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private void retoggleLight()
    {
        if(super.cockpitLightControl)
        {
            setNightMats(false);
            setNightMats(true);
        } else
        {
            setNightMats(true);
            setNightMats(false);
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public com.maddox.JGP.Vector3f w;
    private float pictAiler;
    private float pictElev;
    private static final float vsiNeedleScale[] = {
        -200F, -160F, -125F, -90F, 90F, 125F, 160F, 200F
    };
    private static final float speedometerScale[] = {
        0.0F, 10F, 35F, 70F, 105F, 145F, 190F, 230F, 275F, 315F, 
        360F, 397.5F, 435F, 470F, 505F, 537.5F, 570F, 600F, 630F, 655F, 
        680F
    };
    private static final float revolutionsScale[] = {
        0.0F, 20F, 75F, 125F, 180F, 220F, 285F, 335F
    };
    private static final float oilTScale[] = {
        0.0F, 24F, 51F, 81F, 114F, 160F, 211F, 264.1F
    };
    private com.maddox.JGP.Point3d tmpP;
    private com.maddox.JGP.Vector3d tmpV;

    static 
    {
        com.maddox.rts.Property.set(com.maddox.rts.CLASS.THIS(), "normZNs", new float[] {
            1.00F, 0.56F, 0.80F, 0.56F
        });
    }






}