// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 18.10.2020 10:44:58
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   CockpitMailexpress.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit, LH_Sirius

public class CockpitMailexpress extends com.maddox.il2.objects.air.CockpitPilot
{
    class Interpolater extends com.maddox.il2.engine.InterpolateRef
    {

        public boolean tick()
        {
            if(bNeedSetUp)
            {
                reflectPlaneMats();
                bNeedSetUp = false;
            }
            com.maddox.il2.objects.air.Expressmail expressmail = (com.maddox.il2.objects.air.Expressmail)aircraft();
            if(com.maddox.il2.objects.air.Expressmail.bChangedPit)
            {
                reflectPlaneToModel();
                com.maddox.il2.objects.air.Expressmail expressmail1 = (com.maddox.il2.objects.air.Expressmail)aircraft();
                com.maddox.il2.objects.air.Expressmail.bChangedPit = false;
            }
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.altimeter = fm.getAltitude();
            if(java.lang.Math.abs(((com.maddox.il2.fm.FlightModelMain) (fm)).Or.getKren()) < 30F)
                setNew.azimuth = (35F * setOld.azimuth + ((com.maddox.il2.fm.FlightModelMain) (fm)).Or.azimut()) / 36F;
            if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                setOld.azimuth -= 360F;
            if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                setOld.azimuth += 360F;
            setNew.throttle = (10F * setOld.throttle + ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.PowerControl) / 11F;
            w.set(fm.getW());
            ((com.maddox.il2.fm.FlightModelMain) (fm)).Or.transform(w);
            setNew.turn = (33F * setOld.turn + ((com.maddox.JGP.Tuple3f) (w)).z) / 34F;
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float altimeter;
        float azimuth;
        float throttle;
        float turn;

        private Variables()
        {
        }

    }


    public CockpitMailexpress()
    {
        super("3DO/Cockpit/Expressv2/CockpitLHExpress.him", "u2");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        interpPut(new Interpolater(), null, com.maddox.rts.Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[0] = -cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.getTangage(), -20F, 20F, 0.0385F, -0.0385F);
        super.mesh.chunkSetLocate("zPitch", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        super.mesh.chunkSetAngles("zRPM", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 317F), 0.0F);
        super.mesh.chunkSetAngles("zAlt", 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30500F, 0.0F, -18000F), 0.0F);
        super.mesh.chunkSetAngles("zSpeed", 0.0F, floatindex(cvt(0.539957F * com.maddox.il2.fm.Pitot.Indicator((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 300F, 0.0F, 15F), speedometerScale), 0.0F);
        super.mesh.chunkSetAngles("zBoost", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure(), 0.72421F, 1.27579F, -160F, 160F), 0.0F);
        super.mesh.chunkSetAngles("zMinute", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("zHour", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        super.mesh.chunkSetAngles("zCompass", 0.0F, 90F + interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
        super.mesh.chunkSetAngles("zOilPrs", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 0.0F, 234F, 0.0F, 180F), 0.0F);
        super.mesh.chunkSetAngles("zOilIn", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 87.5F), 0.0F);
        super.mesh.chunkSetAngles("zOilOut", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 50F, 150F, 0.0F, 77F), 0.0F);
        super.mesh.chunkSetAngles("Stick", 0.0F, 24F * (pictAiler = 0.85F * pictAiler + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.AileronControl), 24F * (pictElev = 0.85F * pictElev + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.ElevatorControl));
        super.mesh.chunkSetAngles("Column_Cam", 0.0F, 24F * pictAiler, 0.0F);
        super.mesh.chunkSetAngles("Column_Rod", 0.0F, -24F * pictAiler, 0.0F);
        super.mesh.chunkSetAngles("Rudder", 26F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("RCableL", -26F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("RCableR", -26F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Boost", 0.0F, 0.0F, -90F + 90F * interp(setNew.throttle, setOld.throttle, f));
        super.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, -90F + 90F * interp(setNew.throttle, setOld.throttle, f));
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Tail1_D0", false);
            aircraft().hierMesh().chunkVisible("Keel1_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(com.maddox.il2.engine.Actor.isAlive(aircraft()))
            aircraft().hierMesh().chunkVisible("Tail1_D0", true);
        aircraft().hierMesh().chunkVisible("Keel1_D0", true);
        super.doFocusLeave();
    }

    protected void reflectPlaneToModel()
    {
    }

    protected void reflectPlaneMats()
    {
        com.maddox.il2.engine.HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        super.mesh.materialReplace("Gloss1D0o", mat);
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private com.maddox.JGP.Vector3f w;
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private static final float speedometerScale[] = {
        0.0F, 6.5F, 16.5F, 49F, 91.5F, 143.5F, 199F, 260F, 318F, 376.5F, 
        433F, 484F, 534F, 576F, 620F, 660F
    };
    private static final float oilTempScale[] = {
        -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 
        124F, 147F, 170F
    };










}