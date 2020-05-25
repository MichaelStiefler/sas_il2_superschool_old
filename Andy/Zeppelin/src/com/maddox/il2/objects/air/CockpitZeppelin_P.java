// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 24.05.2020 11:47:41
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   CockpitZeppelin_P.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Way;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorDraw;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Autopilotage;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.util.HashMapExt;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit, Aircraft

public class CockpitZeppelin_P extends com.maddox.il2.objects.air.CockpitPilot
{
    private class Variables
    {

        float throttle[] = {
            0.0F, 0.0F, 0.0F, 0.0F
        };
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;

        private Variables()
        {
        }

    }

    class Interpolater extends com.maddox.il2.engine.InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                if(bNeedSetUp)
                {
                    reflectPlaneMats();
                    bNeedSetUp = false;
                }
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                for(int i = 0; i < 4; i++)
                    setNew.throttle[i] = (10F * setOld.throttle[i] + ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[i].getControlThrottle()) / 11F;

                setNew.altimeter = fm.getAltitude();
                if(java.lang.Math.abs(((com.maddox.il2.fm.FlightModelMain) (fm)).Or.getKren()) < 30F)
                    setNew.azimuth = (35F * setOld.azimuth + -((com.maddox.il2.fm.FlightModelMain) (fm)).Or.getYaw()) / 36F;
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth) + com.maddox.il2.ai.World.Rnd().nextFloat(-10F, 10F)) / 11F;
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
        {
            return 0.0F;
        } else
        {
            waypoint.getP(com.maddox.il2.objects.air.Cockpit.P1);
            com.maddox.il2.objects.air.Cockpit.V.sub(com.maddox.il2.objects.air.Cockpit.P1, ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc);
            return (float)(57.295779513082323D * java.lang.Math.atan2(-((com.maddox.JGP.Tuple3d) (com.maddox.il2.objects.air.Cockpit.V)).y, ((com.maddox.JGP.Tuple3d) (com.maddox.il2.objects.air.Cockpit.V)).x));
        }
    }

    public CockpitZeppelin_P()
    {
        super("3DO/Cockpit/Zeppelin_P/hier.him", "i16");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        light1 = new LightPointActor(new LightPoint(), new Point3d(3.6749499999999999D, 0.72745000000000004D, 1.04095D));
        light2 = new LightPointActor(new LightPoint(), new Point3d(3.6749499999999999D, -0.77925D, 1.04095D));
        light1.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        light2.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        super.pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        super.pos.base().draw.lightMap().put("LAMPHOOK2", light2);
        super.cockpitNightMats = (new java.lang.String[] {
            "Bombgauges", "Gauge02", "Gauge03", "Instr01", "Instr01_dd", "Instr02", "Instr02_dd", "oxigen"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, com.maddox.rts.Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        super.mesh.chunkSetAngles("Z_Column", 0.0F, 10F * (pictElev = 0.85F * pictElev + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.ElevatorControl), 0.0F);
        super.mesh.chunkSetAngles("Z_AroneL", 0.0F, -115F * (pictAiler = 0.85F * pictAiler + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.AileronControl), 0.0F);
        super.mesh.chunkSetAngles("Z_AroneR", 0.0F, -115F * pictAiler, 0.0F);
        super.mesh.chunkSetAngles("Z_RightPedal", 0.0F, -25F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 25F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("Z_RPedalStep", 0.0F, -25F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("Z_LPedalStep", 0.0F, 25F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
        for(int i = 0; i < 4; i++)
        {
            super.mesh.chunkSetAngles("Z_Throttle" + (i + 1), 0.0F, -90F * interp(setNew.throttle[i], setOld.throttle[i], f), 0.0F);
            super.mesh.chunkSetAngles("Z_Throtlev" + (i + 1), 0.0F, -90F * interp(setNew.throttle[i], setOld.throttle[i], f), 0.0F);
        }

        super.mesh.chunkSetAngles("Z_Compass1", 0.0F, interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
        super.mesh.chunkSetAngles("Z_Compass2", 0.0F, interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F) - 187F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F) - 187F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter3", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F) - 187F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter4", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F) - 187F, 0.0F);
        super.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, floatindex(cvt(com.maddox.il2.fm.Pitot.Indicator((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 50F, 400F, 1.0F, 8F), speedometerScale) + 55F, 0.0F);
        super.mesh.chunkSetAngles("Z_Speedometer2", 0.0F, floatindex(cvt(com.maddox.il2.fm.Pitot.Indicator((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 50F, 400F, 1.0F, 8F), speedometerScale) + 55F, 0.0F);
        w.set(super.fm.getW());
        ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("Z_TurnBank1", 0.0F, cvt(((com.maddox.JGP.Tuple3f) (w)).z, -0.23562F, 0.23562F, 22F, -22F), 0.0F);
        super.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, cvt(((com.maddox.JGP.Tuple3f) (w)).z, -0.23562F, 0.23562F, 22F, -22F), 0.0F);
        super.mesh.chunkSetAngles("Z_Horizon1", 0.0F, ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.getKren(), 0.0F);
        super.mesh.chunkSetAngles("Z_Horizon2", 0.0F, ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.getKren(), 0.0F);
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[2] = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.getTangage(), -35F, 35F, 0.028F, -0.028F);
        super.mesh.chunkSetLocate("Z_Tangage1", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetLocate("Z_Tangage2", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetAngles("Z_Variometr", 0.0F, cvt(setNew.vspeed, -40F, 40F, -180F, 180F), 0.0F);
        for(int j = 0; j < 4; j++)
            super.mesh.chunkSetAngles("Z_RPM" + (j + 1), 0.0F, floatindex(cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[j].getRPM(), 400F, 2400F, 2.0F, 13F), engineRPMScale), 0.0F);

        super.mesh.chunkSetAngles("Z_RPK1", 0.0F, cvt(interp(setNew.waypointAzimuth, setOld.waypointAzimuth, f), -25F, 25F, 30F, -30F), 0.0F);
        super.mesh.chunkSetAngles("Z_RPK2", 0.0F, cvt(interp(setNew.waypointAzimuth, setOld.waypointAzimuth, f), -25F, 25F, 30F, -30F), 0.0F);
        super.mesh.chunkSetAngles("Z_Minute1", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F) + 180F, 0.0F);
        super.mesh.chunkSetAngles("Z_Hour1", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F) + 180F, 0.0F);
    }

    public void reflectCockpitState()
    {
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 4) != 0)
            super.mesh.chunkVisible("HullDamage3", true);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 8) != 0)
            super.mesh.chunkVisible("HullDamage4", true);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) != 0)
            super.mesh.chunkVisible("HullDamage1", true);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) != 0)
            super.mesh.chunkVisible("HullDamage2", true);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) != 0)
        {
            super.mesh.chunkVisible("HullDamage1", true);
            super.mesh.chunkVisible("HullDamage3", true);
            super.mesh.materialReplace("Instr01", "Instr01_dd");
            super.mesh.materialReplace("Instr01_night", "Instr01_dd_night");
            super.mesh.materialReplace("Instr02", "Instr02_dd");
            super.mesh.materialReplace("Instr02_night", "Instr02_dd_night");
            super.mesh.chunkVisible("Z_Speedometer1", false);
            super.mesh.chunkVisible("Z_Speedometer2", false);
            super.mesh.chunkVisible("Z_TurnBank1", false);
            super.mesh.chunkVisible("Z_TurnBank2", false);
            super.mesh.chunkVisible("Z_Variometr", false);
            super.mesh.chunkVisible("Z_Altimeter1", false);
            super.mesh.chunkVisible("Z_Altimeter2", false);
            super.mesh.chunkVisible("Z_Altimeter3", false);
            super.mesh.chunkVisible("Z_Altimeter4", false);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 2) != 0)
            super.mesh.chunkVisible("XGlassDamage2", true);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 1) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage1", true);
            super.mesh.chunkVisible("XGlassDamage3", true);
        }
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            ((com.maddox.il2.engine.ActorHMesh)((com.maddox.il2.engine.Interpolate) (super.fm)).actor).hierMesh().chunkVisible("Windscreen_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        ((com.maddox.il2.engine.ActorHMesh)((com.maddox.il2.engine.Interpolate) (super.fm)).actor).hierMesh().chunkVisible("Windscreen_D0", true);
        super.doFocusLeave();
    }

    protected void reflectPlaneMats()
    {
        com.maddox.il2.engine.HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        super.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleLight()
    {
        super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
        {
            light1.light.setEmit(0.98F, 0.45F);
            light2.light.setEmit(0.98F, 0.45F);
            setNightMats(true);
        } else
        {
            light1.light.setEmit(0.0F, 0.0F);
            light2.light.setEmit(0.0F, 0.0F);
            setNightMats(false);
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public com.maddox.JGP.Vector3f w;
    private boolean bNeedSetUp;
    private com.maddox.il2.engine.LightPointActor light1;
    private com.maddox.il2.engine.LightPointActor light2;
    private float pictAiler;
    private float pictElev;
    private static final float speedometerScale[] = {
        0.0F, 0.0F, 52F, 91F, 139.5F, 190F, 249.5F, 308F, 360F
    };
    private static final float engineRPMScale[] = {
        0.0F, 0.0F, 0.0F, 40F, 80.5F, 115.3F, 145.5F, 177.6F, 206.5F, 234.5F, 
        261F, 287F, 320F, 358.5F
    };









}