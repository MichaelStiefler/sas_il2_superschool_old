// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 22.09.2020 21:33:44
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   CockpitPiperCub.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Way;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Autopilotage;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Aircraft, Cockpit

public class CockpitPiperCub extends com.maddox.il2.objects.air.CockpitPilot
{
    class Interpolater extends com.maddox.il2.engine.InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle = 0.85F * setOld.throttle + ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.PowerControl * 0.15F;
                setNew.prop = 0.85F * setOld.prop + ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.getStepControl() * 0.15F;
                setNew.stage = 0.85F * setOld.stage + (float)((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getControlCompressor() * 0.15F;
                setNew.mix = 0.85F * setOld.mix + ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getControlMix() * 0.15F;
                setNew.altimeter = fm.getAltitude();
                setNew.azimuth = (35F * setOld.azimuth + -((com.maddox.il2.fm.FlightModelMain) (fm)).Or.getYaw()) / 36F;
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + waypointAzimuth() + com.maddox.il2.ai.World.Rnd().nextFloat(-30F, 30F)) / 11F;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float throttle;
        float prop;
        float mix;
        float stage;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;

        private Variables()
        {
        }

    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            ((com.maddox.il2.engine.ActorHMesh)((com.maddox.il2.engine.Interpolate) (super.fm)).actor).hierMesh().chunkVisible("WingLOut_D0", false);
            ((com.maddox.il2.engine.ActorHMesh)((com.maddox.il2.engine.Interpolate) (super.fm)).actor).hierMesh().chunkVisible("WingROut_D0", false);
            com.maddox.il2.engine.hotkey.HookPilot hookpilot = com.maddox.il2.engine.hotkey.HookPilot.current;
            hookpilot.doAim(false);
            return true;
        } else
        {
            ((com.maddox.il2.engine.ActorHMesh)((com.maddox.il2.engine.Interpolate) (super.fm)).actor).hierMesh().chunkVisible("WingLOut", true);
            ((com.maddox.il2.engine.ActorHMesh)((com.maddox.il2.engine.Interpolate) (super.fm)).actor).hierMesh().chunkVisible("WingROut", true);
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(!isFocused())
        {
            ((com.maddox.il2.engine.ActorHMesh)((com.maddox.il2.engine.Interpolate) (super.fm)).actor).hierMesh().chunkVisible("WingLOut", false);
            ((com.maddox.il2.engine.ActorHMesh)((com.maddox.il2.engine.Interpolate) (super.fm)).actor).hierMesh().chunkVisible("WingROut", false);
            return;
        } else
        {
            ((com.maddox.il2.engine.ActorHMesh)((com.maddox.il2.engine.Interpolate) (super.fm)).actor).hierMesh().chunkVisible("WingLOut_D0", true);
            ((com.maddox.il2.engine.ActorHMesh)((com.maddox.il2.engine.Interpolate) (super.fm)).actor).hierMesh().chunkVisible("WingROut_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    protected float waypointAzimuth()
    {
        com.maddox.il2.ai.WayPoint waypoint = ((com.maddox.il2.fm.FlightModelMain) (super.fm)).AP.way.curr();
        if(waypoint == null)
        {
            return 0.0F;
        } else
        {
            waypoint.getP(tmpP);
            tmpV.sub(tmpP, ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc);
            return (float)(57.295779513082323D * java.lang.Math.atan2(-((com.maddox.JGP.Tuple3d) (tmpV)).y, ((com.maddox.JGP.Tuple3d) (tmpV)).x));
        }
    }

    public CockpitPiperCub()
    {
        super("3DO/Cockpit/PiperCub/hier.him", "il2");
        gun = null;
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictGear = 0.0F;
        pictFlap = 0.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        super.cockpitNightMats = (new java.lang.String[] {
            "Instrumentos001", "Instrumentos002", "Instrumentos003"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, com.maddox.rts.Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(gun == null)
            gun = ((com.maddox.il2.objects.air.Aircraft)((com.maddox.il2.engine.Interpolate) (super.fm)).actor).getBulletEmitterByHookName("_ExternalBomb03");
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        resetYPRmodifier();
        super.mesh.chunkSetAngles("Trim_Ele", 0.0F, 0.0F, 722F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getTrimElevatorControl());
        super.mesh.chunkSetAngles("Z_Pedal_D", 0.0F, 0.0F, -15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder());
        super.mesh.chunkSetAngles("Z_Pedal_I", 0.0F, 0.0F, 15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder());
        super.mesh.chunkSetAngles("Z_Acelerador", 0.0F, 0.0F, -70F * setNew.throttle);
        super.mesh.chunkSetAngles("Z_Acelerador2", 0.0F, 0.0F, -70F * setNew.throttle);
        super.mesh.chunkSetAngles("Z_Palanca", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.AileronControl) * 20F, (pictElev = 0.85F * pictElev + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.ElevatorControl) * 10F);
        super.mesh.chunkSetAngles("z_Flap", 0.0F, 0.0F, 18F * (pictFlap = 0.75F * pictFlap + 0.95F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.FlapsControl));
        resetYPRmodifier();
        resetYPRmodifier();
        if(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getStage() > 0 && ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getStage() < 3)
            com.maddox.il2.objects.air.Cockpit.xyz[1] = 0.02825F;
        super.mesh.chunkSetLocate("Z_Starter", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetAngles("Z_Magneto", 0.0F, 60F * (float)((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlMagnetos(), 0.0F);
        super.mesh.chunkSetAngles("Z_Radiador", 0.0F, 0.0F, 3F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlRadiator());
        super.mesh.chunkSetAngles("Z_Hora", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        super.mesh.chunkSetAngles("Z_Minuto", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("Z_Altimetro1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F);
        super.mesh.chunkSetAngles("Z_Altimetro2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F);
        super.mesh.chunkSetAngles("Z_ASI", 0.0F, floatindex(cvt(com.maddox.il2.fm.Pitot.Indicator((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 225.0F, 0.0F, 14F), speedometerScale), 0.0F);
        super.mesh.chunkSetAngles("Z_RPM", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 2500F, 30F, 240F), 0.0F);
        super.mesh.chunkSetAngles("Z_Climb", 0.0F, floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
        super.mesh.chunkSetAngles("Z_OilTemp", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 20F, 120F, 30.0F, -30F), 0.0F);
        super.mesh.chunkSetAngles("Z_Oilpres", 0.0F, cvt(1.0F + 0.05F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getReadyness(), 0.0F, 7.45F, 150.0F, 210F), 0.0F);
        super.mesh.chunkSetAngles("Z_Compass", setNew.azimuth, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Fuel1", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 0.0F, 180F, 0.0F, 275F), 0.0F);
        super.mesh.chunkSetAngles("Z_Fuel2", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 0.0F, 180F, 0.0F, 275F), 0.0F);
        w.set(super.fm.getW());
        ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("Z_Palo", 0.0F, cvt(((com.maddox.JGP.Tuple3f) (w)).z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F);
        super.mesh.chunkSetAngles("Z_Bola", 0.0F, cvt(getBall(7D), -7F, 7F, -15F, 15F), 0.0F);
        if(gun != null && gun.haveBullets())
            super.mesh.chunkVisible("Skydiver", true);
        else
            super.mesh.chunkVisible("Skydiver", false);
    }

    protected void reflectPlaneMats()
    {
        com.maddox.il2.engine.HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        super.mesh.materialReplace("Gloss1D0o", mat);
    }

    protected void reflectPlaneToModel()
    {
        com.maddox.il2.engine.HierMesh hiermesh = aircraft().hierMesh();
        super.mesh.chunkVisible("WingLMid", hiermesh.isChunkVisible("WingLMid_D0"));
        super.mesh.chunkVisible("WingRMid", hiermesh.isChunkVisible("WingRMid_D0"));
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

    private com.maddox.il2.ai.BulletEmitter gun;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public com.maddox.JGP.Vector3f w;
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private float pictGear;
    private float pictFlap;
    private static final float speedometerScale[] = {
        0.0F, 5.5F, 13.5F, 27F, 43F, 62F, 90F, 120F, 150F, 180F,
        210F, 243F, 270F, 298F, 324F
    };
    private static final float variometerScale[] = {
        -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 
        124F, 147F, 170F
    };
    private com.maddox.JGP.Point3d tmpP;
    private com.maddox.JGP.Vector3d tmpV;


    static
    {
        Property.set(CLASS.THIS(), "normZNs", new float[] {
            1.30F, 0.57F, 1.55F, 0.57F
        });
    }




}