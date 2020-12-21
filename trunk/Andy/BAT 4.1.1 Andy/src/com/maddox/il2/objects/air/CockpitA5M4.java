// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 14.11.2020 14:28:04
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   CockpitA5M4.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Way;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Autopilotage;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CLASS;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit, A5M4

public class CockpitA5M4 extends com.maddox.il2.objects.air.CockpitPilot
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
                if(cockpitDimControl)
                {
                    if(setNew.dimPos < 1.0F)
                        setNew.dimPos = setOld.dimPos + 0.03F;
                } else
                if(setNew.dimPos > 0.0F)
                    setNew.dimPos = setOld.dimPos - 0.03F;
                setNew.manifold = 0.8F * setOld.manifold + 0.2F * ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getManifoldPressure();
                setNew.throttle = 0.8F * setOld.throttle + 0.2F * ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.PowerControl;
                setNew.prop = 0.8F * setOld.prop + 0.2F * ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getControlProp();
                setNew.mix = 0.8F * setOld.mix + 0.2F * ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getControlMix();
                setNew.man = 0.92F * setOld.man + 0.08F * ((com.maddox.il2.fm.FlightModelMain) (fm)).EI.engines[0].getManifoldPressure();
                setNew.altimeter = fm.getAltitude();
                float f = waypointAzimuth();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((com.maddox.il2.fm.FlightModelMain) (fm)).Or.azimut());
                setNew.waypointDeviation.setDeg(setOld.waypointDeviation.getDeg(0.1F), (f - setOld.azimuth.getDeg(1.0F)) + com.maddox.il2.ai.World.Rnd().nextFloat(-5F, 5F));
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), f);
                setNew.vspeed = 0.5F * setOld.vspeed + 0.5F * fm.getVertSpeed();
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float dimPos;
        float throttle;
        float prop;
        float mix;
        float altimeter;
        float man;
        float vspeed;
        float manifold;
        com.maddox.il2.ai.AnglesFork azimuth;
        com.maddox.il2.ai.AnglesFork waypointAzimuth;
        com.maddox.il2.ai.AnglesFork waypointDeviation;

        private Variables()
        {
            dimPos = 0.0F;
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            waypointDeviation = new AnglesFork();
        }

        Variables(Variables variables)
        {
            this();
        }
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
            aircraft().hierMesh().chunkVisible("Head1_D0", false);
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
            aircraft().hierMesh().chunkVisible("Head1_D0", true);
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
            bEntered = true;
            super.mesh.chunkVisible("SuperReticle", true);
            super.mesh.chunkVisible("Z_BoxTinter", true);
            super.mesh.chunkVisible("EDET", false);
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
            super.mesh.chunkVisible("SuperReticle", false);
            super.mesh.chunkVisible("Z_BoxTinter", false);
            super.mesh.chunkVisible("EDET", true);
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

    public CockpitA5M4()
    {
        super("3DO/Cockpit/Ki-27(Ko)/CockpitA5M4.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictRad = 0.0F;
        pictGun = 0.0F;
        pictFlap = 0.0F;
        bNeedSetUp = true;
        oldTime = -1L;
        enteringAim = false;
        bEntered = false;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        super.cockpitNightMats = (new java.lang.String[] {
            "gauge1", "gauge2", "gauge3", "gauge4", "gauge1_d", "gauge2_d", "gauge3_d", "gauge4_d", "Arrows", "Digits"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, com.maddox.rts.Time.current(), null);
        if(super.acoustics != null)
            super.acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    public void reflectWorldToInstruments(float f)
    {
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
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            reflectPlaneToModel();
            bNeedSetUp = false;
        }
        if(com.maddox.il2.objects.air.A5M4.bChangedPit)
        {
            reflectPlaneToModel();
            com.maddox.il2.objects.air.A5M4.bChangedPit = false;
        }
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[1] = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getCockpitDoor(), 0.05F, 0.95F, 0.0F, 0.55F);
        super.mesh.chunkSetAngles("Z_ReViTinter", 0.0F, cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -180F), 0.0F);
        super.mesh.chunkSetAngles("Z_BoxTinter", 0.0F, cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -180F), 0.0F);
        super.mesh.chunkSetAngles("Z_ColumnBase", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.AileronControl) * 20F, 0.0F);
        super.mesh.chunkSetAngles("Z_Column", 0.0F, -(pictElev = 0.85F * pictElev + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.ElevatorControl) * 10F, 0.0F);
        super.mesh.chunkSetAngles("Z_ColumnWire", 0.0F, pictElev * 20F, 0.0F);
        super.mesh.chunkSetAngles("Z_PedalBase", 0.0F, -30F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("Z_RightPedal", 0.0F, 20F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getBrake(), 0.0F);
        super.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 20F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getBrake(), 0.0F);
        super.mesh.chunkSetAngles("Z_RightWire", -30F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_LeftWire", -30F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Radiat", 0.0F, -450F * (pictRad = 0.9F * pictRad + 0.1F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlRadiator()), 0.0F);
        super.mesh.chunkSetAngles("Z_Throtle1", 0.0F, cvt(setNew.throttle, 0.0F, 1.1F, -38F, 38F), 0.0F);
        super.mesh.chunkSetAngles("Z_Throtle2", 0.0F, 30F * (pictGun = 0.8F * pictGun + 0.2F * (((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.saveWeaponControl[0] ? 1.0F : 0.0F)), 0.0F);
        super.mesh.chunkSetAngles("zPitch1", 0.0F, cvt(setNew.prop, 0.0F, 1.0F, -38F, 38F), 0.0F);
        super.mesh.chunkSetAngles("zTrim1", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.trimElevator, -0.5F, 0.5F, 35F, -35F), 0.0F);
        super.mesh.chunkSetAngles("zTrim2", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.trimElevator, -0.5F, 0.5F, -35F, 35F), 0.0F);
        resetYPRmodifier();
        com.maddox.il2.objects.air.Cockpit.xyz[1] = cvt(setNew.mix, 0.0F, 1.2F, 0.03675F, 0.0F);
        long l = com.maddox.rts.Time.current();
        long l1 = l - oldTime;
        oldTime = l;
        float f1 = (float)l1 * 0.00016F;
        if(pictFlap < ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.FlapsControl)
        {
            if(pictFlap + f1 >= ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.FlapsControl)
                pictFlap = ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.FlapsControl;
            else
                pictFlap += f1;
        } else
        if(pictFlap - f1 <= ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.FlapsControl)
            pictFlap = ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.FlapsControl;
        else
            pictFlap -= f1;
        super.mesh.chunkSetAngles("Z_Flaps", 0.0F, -3450F * pictFlap, 0.0F);
        super.mesh.chunkSetAngles("Z_Mag1", cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlMagnetos(), 0.0F, 3F, 76.5F, -28.5F), 0.0F, 0.0F);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) == 0)
        {
            super.mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, -7200F), 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, -720F), 0.0F, 0.0F);
        }
        super.mesh.chunkSetAngles("Z_Speedometer1", -floatindex(cvt(0.539957F * com.maddox.il2.fm.Pitot.Indicator((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 300F, 0.0F, 15F), speedometerScale), 0.0F, 0.0F);
        w.set(super.fm.getW());
        ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("Z_TurnBank1", cvt(((com.maddox.JGP.Tuple3f) (w)).z, -0.23562F, 0.23562F, -30F, 30F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TurnBank2", cvt(getBall(6D), -6F, 6F, 6F, -6F), 0.0F, 0.0F);
        float f2 = setNew.vspeed;
        if(java.lang.Math.abs(f2) < 5F)
            super.mesh.chunkSetAngles("Z_Climb1", cvt(f2, -5F, 5F, 90F, -90F), 0.0F, 0.0F);
        else
        if(f2 > 0.0F)
            super.mesh.chunkSetAngles("Z_Climb1", cvt(f2, 5F, 30F, -90F, -180F), 0.0F, 0.0F);
        else
            super.mesh.chunkSetAngles("Z_Climb1", cvt(f2, -30F, -5F, 180F, 90F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass1", -setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Clock_H", cvt(com.maddox.il2.ai.World.getTimeofDay(), 0.0F, 24F, 0.0F, -720F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Clock_Min", cvt(com.maddox.il2.ai.World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);

        super.mesh.chunkSetAngles("Z_Manipres", cvt(setNew.manifold, 0.33339F, 1.66661F, 150F, -150F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPM1", cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 200F, 3000F, -8.5F, -323F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Fuelpres", cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 2.0F, 0.0F, -360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Oiltemp1", cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, -300F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Oilpres1", cvt(1.0F + 0.05F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 5.5F, 0.0F, -300F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Tempcyl", cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tWaterOut, 0.0F, 360F, 0.0F, -90.6F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Fuel", cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 0.0F, 108F, -41F, -320F), 0.0F, 0.0F);
    }

    public void reflectCockpitState()
    {
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 2) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage1", true);
            super.mesh.chunkVisible("XGlassDamage2", true);
            super.mesh.chunkVisible("XHullDamage3", true);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 1) != 0)
            super.mesh.chunkVisible("XGlassDamage7", true);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) != 0)
        {
            super.mesh.chunkVisible("Panel_D0", false);
            super.mesh.chunkVisible("Panel_D1", true);
            super.mesh.chunkVisible("Z_Speedometer1", false);
            super.mesh.chunkVisible("Z_TurnBank1", false);
            super.mesh.chunkVisible("Z_TurnBank2", false);
            super.mesh.chunkVisible("Z_Climb1", false);
            super.mesh.chunkVisible("Z_RPM1", false);
            super.mesh.chunkVisible("Z_Manipres", false);
            super.mesh.chunkVisible("Z_Fuel", false);
            super.mesh.chunkVisible("Z_Fuelpres", false);
            super.mesh.chunkVisible("Z_Altimeter1", false);
            super.mesh.chunkVisible("Z_Altimeter2", false);
            super.mesh.chunkVisible("Z_Oiltemp1", false);
            super.mesh.chunkVisible("Z_Oilpres1", false);
            super.mesh.chunkVisible("Z_Clock_H", false);
            super.mesh.chunkVisible("Z_Clock_Min", false);
            super.mesh.chunkVisible("Z_Tempcyl", false);
            super.mesh.chunkVisible("XHullDamage3", true);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 4) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage1", true);
            super.mesh.chunkVisible("XHullDamage1", true);
            super.mesh.chunkVisible("XHullDamage2", true);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 8) != 0)
            super.mesh.chunkVisible("XHullDamage4", true);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x80) != 0)
            super.mesh.chunkVisible("Z_OilSplats_D1", true);
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage6", true);
            super.mesh.chunkVisible("XHullDamage1", true);
            super.mesh.chunkVisible("XHullDamage5", true);
        }
        if((((com.maddox.il2.fm.FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) != 0)
            super.mesh.chunkVisible("XHullDamage4", true);
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

    protected void reflectPlaneMats()
    {
        com.maddox.il2.engine.HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        super.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        super.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        super.mesh.materialReplace("Matt2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        super.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
        super.mesh.materialReplace("Matt1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        super.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
        super.mesh.materialReplace("Matt2D2o", mat);
    }

    protected void reflectPlaneToModel()
    {
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public com.maddox.JGP.Vector3f w;
    private float pictAiler;
    private float pictElev;
    private float pictRad;
    private float pictGun;
    private float pictFlap;
    private boolean bNeedSetUp;
    private long oldTime;
    private boolean enteringAim;
    private static final float speedometerScale[] = {
        0.0F, 6.5F, 16.5F, 49F, 91.5F, 143.5F, 199F, 260F, 318F, 376.5F, 
        433F, 484F, 534F, 576F, 620F, 660F
    };
    private float saveFov;
    private boolean bEntered;
    private com.maddox.JGP.Point3d tmpP;
    private com.maddox.JGP.Vector3d tmpV;

    static 
    {
        com.maddox.rts.Property.set(com.maddox.rts.CLASS.THIS(), "normZNs", new float[] {
            0.35F, 0.35F, 1.2F, 0.35F
        });
    }






}