// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 31.10.2020 16:06:02
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   CockpitAR_196T.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.*;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit

public class CockpitAR_196T extends com.maddox.il2.objects.air.CockpitPilot
{
    private class Variables
    {

        float flaps;
        float gear;
        float throttle;
        float prop;
        float mix;
        float divebrake;
        float altimeter;
        float man;
        float vspeed;
        com.maddox.il2.ai.AnglesFork azimuth;

        private Variables()
        {
            azimuth = new AnglesFork();
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
                setNew.flaps = 0.9F * setOld.flaps + 0.1F * ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.FlapsControl;
                setNew.gear = 0.7F * setOld.gear + 0.3F * ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.GearControl;
                setNew.throttle = 0.8F * setOld.throttle + 0.2F * ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.PowerControl;
                setNew.prop = 0.8F * setOld.prop + 0.2F * ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getControlProp();
                setNew.mix = 0.8F * setOld.mix + 0.2F * ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getControlMix();
                setNew.divebrake = 0.8F * setOld.divebrake + 0.2F * ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.AirBrakeControl;
                setNew.man = 0.92F * setOld.man + 0.08F * ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getManifoldPressure();
                setNew.altimeter = fm.getAltitude();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((com.maddox.il2.fm.FlightModelMain) (fm)).Or.azimut());
                setNew.vspeed = (399F * setOld.vspeed + fm.getVertSpeed()) / 400F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    public CockpitAR_196T()
    {
        super("3DO/Cockpit/Ar-196T1/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        super.cockpitNightMats = (new java.lang.String[] {
            "GagePanel1", "GagePanel1_D1", "GagePanel2", "GagePanel2_D1", "GagePanel3", "GagePanel3_D1", "GagePanel4", "GagePanel4_D1", "misc2"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, com.maddox.rts.Time.current(), null);
        if(super.acoustics != null)
            super.acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    public void reflectWorldToInstruments(float f)
    {
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[1] = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.7F);
        super.mesh.chunkSetLocate("Canopy", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetAngles("Z_Trim1", 0.0F, 1444F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getTrimAileronControl(), 0.0F);
        super.mesh.chunkSetAngles("Z_Trim2", 0.0F, 1444F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getTrimRudderControl(), 0.0F);
        super.mesh.chunkSetAngles("Z_Trim3", 0.0F, 1444F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getTrimElevatorControl(), 0.0F);
        super.mesh.chunkSetAngles("Z_Flaps1", 0.0F, -77F * setNew.flaps, 0.0F);
        super.mesh.chunkSetAngles("Z_Gear1", 0.0F, -77F * setNew.gear, 0.0F);
        super.mesh.chunkSetAngles("Z_Throtle1", 0.0F, -40F * setNew.throttle, 0.0F);
        super.mesh.chunkSetAngles("Z_Prop1", 0.0F, -68F * setNew.prop, 0.0F);
        super.mesh.chunkSetAngles("Z_Mixture1", 0.0F, -55F * setNew.mix, 0.0F);
        super.mesh.chunkSetAngles("Z_Supercharger1", 0.0F, -55F * (float)((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlCompressor(), 0.0F);
        super.mesh.chunkSetAngles("Z_RightPedal", 0.0F, 20F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 20F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("Z_Columnbase", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.AileronControl) * 8F, 0.0F);
        super.mesh.chunkSetAngles("Z_Column", 0.0F, (pictElev = 0.85F * pictElev + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.ElevatorControl) * 8F, 0.0F);
        super.mesh.chunkSetAngles("Z_DiveBrake1", 0.0F, -77F * setNew.divebrake, 0.0F);
        super.mesh.chunkSetAngles("Z_CowlFlap1", 0.0F, -28F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlRadiator(), 0.0F);
        if(((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.Weapons[3] != null && !((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.Weapons[3][0].haveBullets())
            super.mesh.chunkSetAngles("Z_Bomb1", 0.0F, 35F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, -3600F), 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, -36000F), 0.0F);
        super.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, -floatindex(cvt(com.maddox.il2.fm.Pitot.Indicator((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 925.9998F, 0.0F, 10F), speedometerScale), 0.0F);
        w.set(super.fm.getW());
        ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("Z_TurnBank1", 0.0F, cvt(((com.maddox.JGP.Tuple3f) (w)).z, -0.23562F, 0.23562F, 22F, -22F), 0.0F);
        super.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, cvt(getBall(6D), -6F, 6F, -16F, 16F), 0.0F);
        super.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, -((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.getKren(), 0.0F);
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[2] = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.getTangage(), -45F, 45F, 0.025F, -0.025F);
        super.mesh.chunkSetLocate("Z_TurnBank4", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetAngles("Z_Climb1", 0.0F, setNew.vspeed >= 0.0F ? -floatindex(cvt(setNew.vspeed / 5.08F, 0.0F, 6F, 0.0F, 12F), variometerScale) : floatindex(cvt(-setNew.vspeed / 5.08F, 0.0F, 6F, 0.0F, 12F), variometerScale), 0.0F);
        super.mesh.chunkSetAngles("Z_Compass1", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
        super.mesh.chunkSetAngles("Z_Compass2", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        super.mesh.chunkSetAngles("Z_RPM1", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, -360F), 0.0F);
        super.mesh.chunkSetAngles("Z_Hour1", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay(), 0.0F, 24F, 0.0F, -360F), 0.0F);
        super.mesh.chunkSetAngles("Z_Minute1", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F);
        super.mesh.chunkSetAngles("Z_Fuel1", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 0.0F, 400F, 0.0F, -87F), 0.0F);
        super.mesh.chunkSetAngles("Z_Fuel2", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 0.0F, 400F, 0.0F, -87F), 0.0F);
        super.mesh.chunkSetAngles("Z_Fuel3", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 0.0F, 400F, 0.0F, -87F), 0.0F);
        super.mesh.chunkSetAngles("Z_Fuel4", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 0.0F, 400F, 0.0F, -87F), 0.0F);
        super.mesh.chunkSetAngles("Z_FuelPres1", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.3F, 0.0F, -180F), 0.0F);
        super.mesh.chunkSetAngles("Z_Temp1", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, -74F), 0.0F);
        super.mesh.chunkSetAngles("Z_Temp2", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, -180F), 0.0F);
        super.mesh.chunkSetAngles("Z_Pres1", 0.0F, cvt(setNew.man, 0.3386378F, 2.539784F, 0.0F, -344F), 0.0F);
        super.mesh.chunkSetAngles("Z_Oil1", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 200F, 0.0F, -180F), 0.0F);
        super.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, cvt(1.0F + 0.05F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getReadyness(), 0.0F, 7.45F, 0.0F, -301F), 0.0F);
        float f1 = ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getRPM();
        f1 = 2.5F * (float)java.lang.Math.sqrt(java.lang.Math.sqrt(java.lang.Math.sqrt(java.lang.Math.sqrt(f1))));
        super.mesh.chunkSetAngles("Z_Suction1", 0.0F, cvt(f1, 0.0F, 10F, 0.0F, -300F), 0.0F);
        if(((com.maddox.il2.fm.FlightModelMain) (super.fm)).Gears.lgear)
        {
            resetYPRmodifier();
            com.maddox.il2.objects.air.Cockpit.xyz[0] = -0.133F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear();
            super.mesh.chunkSetLocate("Z_GearL1", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        }
        if(((com.maddox.il2.fm.FlightModelMain) (super.fm)).Gears.rgear)
        {
            resetYPRmodifier();
            com.maddox.il2.objects.air.Cockpit.xyz[0] = -0.133F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getGear();
            super.mesh.chunkSetLocate("Z_GearR1", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        }
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[0] = -0.118F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getFlap();
        super.mesh.chunkSetLocate("Z_Flap1", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetAngles("Z_EnginePrim", 0.0F, ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getStage() > 0 ? -36F : 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_MagSwitch", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, -111F), 0.0F);
    }

    public void reflectCockpitState()
    {
    }

    public void toggleLight()
    {
        super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public com.maddox.JGP.Vector3f w;
    private float pictAiler;
    private float pictElev;
    private static final float speedometerScale[] = {
        0.0F, 16.5F, 79.5F, 143F, 206.5F, 229.5F, 251F, 272.5F, 294F, 316F, 
        339.5F
    };
    private static final float variometerScale[] = {
        0.0F, 25F, 49.5F, 64F, 78.5F, 89.5F, 101F, 112F, 123F, 134.5F, 
        145.5F, 157F, 168F, 168F
    };


    static
    {
        Property.set(CLASS.THIS(), "normZNs", new float[] {
            0.60F, 0.60F, 0.90F, 0.60F
        });
    }



}