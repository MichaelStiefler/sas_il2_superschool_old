// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 13.11.2020 14:15:26
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   CockpitCR32quater.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Aircraft, CR_32quater, CR_32quaterx

public class CockpitCR32quater extends com.maddox.il2.objects.air.CockpitPilot
{
    private class Variables
    {

        float altimeter;
        float azimuth;
        float mix;
        float throttle;
        float turn;

        private Variables()
        {
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
            com.maddox.il2.objects.air.CR_32quater cr_32quater = (com.maddox.il2.objects.air.CR_32quater)aircraft();
            if(com.maddox.il2.objects.air.CR_32quaterx.bChangedPit)
            {
                reflectPlaneToModel();
                com.maddox.il2.objects.air.CR_32quater cr_32quater1 = (com.maddox.il2.objects.air.CR_32quater)aircraft();
                com.maddox.il2.objects.air.CR_32quaterx.bChangedPit = false;
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
            setNew.mix = (10F * setOld.mix + ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getControlMix()) / 11F;
            setNew.throttle = (10F * setOld.throttle + ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.PowerControl) / 11F;
            w.set(fm.getW());
            ((com.maddox.il2.fm.FlightModelMain) (fm)).Or.transform(w);
            setNew.turn = (33F * setOld.turn + ((com.maddox.JGP.Tuple3f) (w)).z) / 34F;
            float f = ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.getAileron();
            mesh.chunkSetAngles("AroneL_D0", 0.0F, -20F * f, 0.0F);
            mesh.chunkSetAngles("AroneR_D0", 0.0F, -20F * f, 0.0F);
            return true;
        }

        Interpolater()
        {
        }
    }


    public CockpitCR32quater()
    {
        super("3DO/Cockpit/CR32/hier.him", "u2");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        bEntered = false;
        enteringAim = false;
        interpPut(new Interpolater(), null, com.maddox.rts.Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        if(enteringAim)
        {
            com.maddox.il2.engine.hotkey.HookPilot hookpilot = com.maddox.il2.engine.hotkey.HookPilot.current;
            if(hookpilot.isAimReached())
            {
                enteringAim = false;
                enter();
            } else
            if(!hookpilot.isAim())
                enteringAim = false;
        }
        super.mesh.chunkSetAngles("zAlt", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("zSpeed", 0.0F, floatindex(cvt(com.maddox.il2.fm.Pitot.Indicator((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 500F, 0.0F, 25F), speedometerScale), 0.0F);
        super.mesh.chunkSetAngles("zBoost", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure(), 0.0F, 0.8F, 0.5F, 360F), 0.0F);
        super.mesh.chunkSetAngles("zCompass", 0.0F, 90F + interp(-setNew.azimuth, -setOld.azimuth, f), 0.0F);
        super.mesh.chunkSetAngles("zFuelPrs", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.6F, 0.0F, 270F), 0.0F);
        super.mesh.chunkSetAngles("zMinute", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("zHour", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        super.mesh.chunkSetAngles("zOilPrs", 0.0F, cvt(1.0F + 0.05F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 328F), 0.0F);
        super.mesh.chunkSetAngles("zOilOut", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 338F), 0.0F);
        super.mesh.chunkSetAngles("zWaterOut", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 338F), 0.0F);
        super.mesh.chunkSetAngles("zRPM", 0.0F, floatindex(cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 2600F, 0.0F, 13F), rpmScale), 0.0F);
        super.mesh.chunkSetAngles("zSlide", 0.0F, cvt(getBall(3D), -2F, 2.0F, -4F, 4F), 0.0F);
        super.mesh.chunkSetAngles("zTurn", 0.0F, cvt(setNew.turn, -0.7F, 0.7F, -1.5F, 1.5F), 0.0F);
        super.mesh.chunkSetAngles("StickBase", 0.0F, 12F * pictAiler, 0.0F);
        super.mesh.chunkSetAngles("Stick", 0.0F, 0.0F * (pictAiler = 0.85F * pictAiler + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.AileronControl), 12F * (pictElev = 0.85F * pictElev + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.ElevatorControl));
        super.mesh.chunkSetAngles("StickElevRod", 0.0F, pictElev * 12F, 0.0F);
        super.mesh.chunkSetAngles("Rudder", 15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("RCableL", -15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("RCableR", -15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, 0.0F + 40F * interp(setNew.throttle, setOld.throttle, f));
        super.mesh.chunkSetAngles("Mixture", 0.0F, 0.0F, 0.0F + 38F * interp(setNew.mix, setOld.mix, f));
        super.mesh.chunkSetAngles("zMagnetoSwitch", cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F, 0.0F);
    }

    protected void reflectPlaneToModel()
    {
    }

    protected void reflectPlaneMats()
    {
        com.maddox.il2.engine.HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        super.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        super.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        super.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        super.mesh.materialReplace("Matt1D0o", mat);
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            com.maddox.il2.engine.hotkey.HookPilot hookpilot = com.maddox.il2.engine.hotkey.HookPilot.current;
            hookpilot.doAim(false);
            com.maddox.JGP.Point3d point3d = new Point3d();
            point3d.set(0.25D, 0.0D, 0.0D);
            hookpilot.setTubeSight(point3d);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(!isFocused())
        {
            return;
        } else
        {
            leave();
            super.doFocusLeave();
            return;
        }
    }

    private void prepareToEnter()
    {
        com.maddox.il2.engine.hotkey.HookPilot hookpilot = com.maddox.il2.engine.hotkey.HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        enteringAim = true;
    }

    private void enter()
    {
        com.maddox.il2.engine.hotkey.HookPilot hookpilot = com.maddox.il2.engine.hotkey.HookPilot.current;
        hookpilot.setSimpleUse(true);
        doSetSimpleUse(true);
        com.maddox.rts.HotKeyEnv.enable("PanView", false);
        com.maddox.rts.HotKeyEnv.enable("SnapView", false);
    }

    public void doSetSimpleUse(boolean flag)
    {
        super.doSetSimpleUse(flag);
        if(flag)
        {
            saveFov = com.maddox.il2.game.Main3D.FOVX;
            com.maddox.rts.CmdEnv.top().exec("fov 31");
            com.maddox.il2.game.Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
            com.maddox.il2.engine.hotkey.HookPilot hookpilot = com.maddox.il2.engine.hotkey.HookPilot.current;
            if(hookpilot.isPadlock())
                hookpilot.stopPadlock();
            hookpilot.doAim(true);
            hookpilot.setSimpleUse(true);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            com.maddox.rts.HotKeyEnv.enable("PanView", false);
            com.maddox.rts.HotKeyEnv.enable("SnapView", false);
            bEntered = true;
            super.mesh.chunkVisible("SuperReticle", true);
            super.mesh.chunkVisible("Sight", false);
        }
    }

    private void leave()
    {
        if(enteringAim)
        {
            com.maddox.il2.engine.hotkey.HookPilot hookpilot = com.maddox.il2.engine.hotkey.HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if(!bEntered)
        {
            return;
        } else
        {
            com.maddox.il2.game.Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            com.maddox.rts.CmdEnv.top().exec("fov " + saveFov);
            com.maddox.il2.engine.hotkey.HookPilot hookpilot1 = com.maddox.il2.engine.hotkey.HookPilot.current;
            hookpilot1.doAim(false);
            hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setSimpleUse(false);
            doSetSimpleUse(false);
            boolean flag = com.maddox.rts.HotKeyEnv.isEnabled("aircraftView");
            com.maddox.rts.HotKeyEnv.enable("PanView", flag);
            com.maddox.rts.HotKeyEnv.enable("SnapView", flag);
            bEntered = false;
            super.mesh.chunkVisible("Sight", true);
            super.mesh.chunkVisible("SuperReticle", false);
            return;
        }
    }

    public void destroy()
    {
        leave();
        super.destroy();
    }

    public void doToggleAim(boolean flag)
    {
        if(!isFocused())
            return;
        if(isToggleAim() == flag)
            return;
        if(flag)
            prepareToEnter();
        else
            leave();
    }

    public void reflectCockpitState()
    {
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private com.maddox.JGP.Vector3f w;
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private boolean enteringAim;
    private static final float speedometerScale[] = {
        0.0F, 1.0F, 3F, 6.2F, 12F, 26.5F, 39F, 51F, 67.5F, 85.5F, 
        108F, 131.5F, 154F, 180F, 205.7F, 228.2F, 251F, 272.9F, 291.9F, 314.5F, 
        336.5F, 354F, 360F, 363F, 364F, 365F
    };
    private static final float rpmScale[] = {
        0.0F, 12.5F, 25F, 50F, 75F, 100F, 125F, 150F, 175F, 200F, 
        225F, 250F, 275F, 300F
    };
    private float saveFov;
    private boolean bEntered;



    static 
    {
        com.maddox.rts.Property.set(com.maddox.rts.CLASS.THIS(), "normZNs", new float[] {
            0.55F, 0.55F, 1.20F, 0.55F
        });
    }




}