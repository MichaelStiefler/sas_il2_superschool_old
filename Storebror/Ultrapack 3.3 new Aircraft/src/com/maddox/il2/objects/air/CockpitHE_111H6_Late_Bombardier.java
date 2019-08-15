package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_111H6_Late_Bombardier extends CockpitPilot
{
    private class Variables
    {

        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            float f = ((HE_111)aircraft()).fSightCurForwardAngle;
            float f1 = ((HE_111)aircraft()).fSightCurSideslip;
            mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, f);
            if(bEntered)
            {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setInstantOrient(aAim + 10F * f1, tAim + f, 0.0F);
            }
            mesh.chunkSetAngles("TurretA", 0.0F, aircraft().FM.turret[0].tu[0], 0.0F);
            mesh.chunkSetAngles("TurretB", 0.0F, aircraft().FM.turret[0].tu[1], 0.0F);
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            float f2 = waypointAzimuthInvertMinus(30F);
            if(useRealisticNavigationInstruments())
            {
                setNew.waypointAzimuth.setDeg(f2 - 90F);
                setOld.waypointAzimuth.setDeg(f2 - 90F);
                setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
            } else
            {
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f2 - setOld.azimuth.getDeg(1.0F));
            }
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
            return true;
        }

        Interpolater()
        {
        }
    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            ((HE_111)((Interpolate) (this.fm)).actor).bPitUnfocused = false;
            bTurrVisible = aircraft().hierMesh().isChunkVisible("Turret1C_D0");
            aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot1_FAK", false);
            aircraft().hierMesh().chunkVisible("Head1_FAK", false);
            aircraft().hierMesh().chunkVisible("Pilot1_FAL", false);
            aircraft().hierMesh().chunkVisible("Pilot2_FAK", false);
            aircraft().hierMesh().chunkVisible("Pilot2_FAL", false);
            aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            aircraft().hierMesh().chunkVisible("Window_D0", false);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.11D, 0.018D, -0.08D);
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
            ((HE_111)((Interpolate) (this.fm)).actor).bPitUnfocused = false;
            aircraft().hierMesh().chunkVisible("Turret1C_D0", bTurrVisible);
            aircraft().hierMesh().chunkVisible("Cockpit_D0", aircraft().hierMesh().isChunkVisible("Nose_D0") || aircraft().hierMesh().isChunkVisible("Nose_D1") || aircraft().hierMesh().isChunkVisible("Nose_D2"));
            aircraft().hierMesh().chunkVisible("Turret1C_D0", aircraft().hierMesh().isChunkVisible("Turret1B_D0"));
            aircraft().hierMesh().chunkVisible("Window_D0", true);
            aircraft().hierMesh().chunkVisible("Pilot1_FAK", aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
            aircraft().hierMesh().chunkVisible("Head1_FAK", aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
            aircraft().hierMesh().chunkVisible("Pilot1_FAL", aircraft().hierMesh().isChunkVisible("Pilot1_D1"));
            aircraft().hierMesh().chunkVisible("Pilot2_FAK", aircraft().hierMesh().isChunkVisible("Pilot2_D0"));
            aircraft().hierMesh().chunkVisible("Pilot2_FAL", aircraft().hierMesh().isChunkVisible("Pilot2_D1"));
            leave();
            super.doFocusLeave();
            return;
        }
    }

    private void prepareToEnter()
    {
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(0.0F, -90F, 0.0F);
        enteringAim = true;
    }

    private void enter()
    {
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 23.913");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setInstantOrient(aAim, tAim, 0.0F);
        hookpilot.setSimpleUse(true);
        doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    private void leave()
    {
        if(enteringAim)
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.setInstantOrient(0.0F, -90F, 0.0F);
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if(!bEntered)
        {
            return;
        } else
        {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + saveFov);
            HookPilot hookpilot1 = HookPilot.current;
            hookpilot1.setInstantOrient(0.0F, -90F, 0.0F);
            hookpilot1.doAim(false);
            hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setSimpleUse(false);
            doSetSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            bEntered = false;
            return;
        }
    }

    public void destroy()
    {
        super.destroy();
        leave();
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

    public CockpitHE_111H6_Late_Bombardier()
    {
        super("3DO/Cockpit/He-111P-4-Bombardier/hier-H6-Late.him", "he111");
        enteringAim = false;
        bEntered = false;
        try
        {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(this.mesh, "CAMERAAIM");
            hooknamed.computePos(this, this.pos.getAbs(), loc);
            aAim = loc.getOrient().getAzimut();
            tAim = loc.getOrient().getTangage();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        this.cockpitNightMats = (new String[] {
            "clocks1", "clocks2", "clocks4", "clocks5"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        setOld = new Variables();
        setNew = new Variables();
        AircraftLH.printCompassHeading = true;
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(enteringAim)
        {
            HookPilot hookpilot = HookPilot.current;
            if(hookpilot.isAimReached())
            {
                enteringAim = false;
                enter();
            } else
            if(!hookpilot.isAim())
                enteringAim = false;
        }
        if(bEntered)
        {
            this.mesh.chunkSetAngles("zAngleMark", -floatindex(cvt(((HE_111)aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
            boolean flag = ((HE_111)aircraft()).fSightCurReadyness > 0.93F;
            this.mesh.chunkVisible("BlackBox", true);
            this.mesh.chunkVisible("zReticle", flag);
            this.mesh.chunkVisible("zAngleMark", flag);
        } else
        {
            this.mesh.chunkVisible("BlackBox", false);
            this.mesh.chunkVisible("zReticle", false);
            this.mesh.chunkVisible("zAngleMark", false);
        }
        this.mesh.chunkSetAngles("zAltWheel", 0.0F, cvt(((HE_111)aircraft()).fSightCurAltitude, 0.0F, 10000F, 0.0F, 375.8333F), 0.0F);
        this.mesh.chunkSetAngles("zAnglePointer", 0.0F, ((HE_111)aircraft()).fSightCurForwardAngle, 0.0F);
        this.mesh.chunkSetAngles("zAngleWheel", 0.0F, -10F * ((HE_111)aircraft()).fSightCurForwardAngle, 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, floatindex(cvt(Pitot.Indicator((float)this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 550F, 0.0F, 11F), speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zSpeedPointer", 0.0F, cvt(((HE_111)aircraft()).fSightCurSpeed, 150F, 600F, 0.0F, 60F), 0.0F);
        this.mesh.chunkSetAngles("zSpeedWheel", 0.0F, 0.333F * ((HE_111)aircraft()).fSightCurSpeed, 0.0F);
        this.mesh.chunkSetAngles("zAlt1", 0.0F, cvt(this.fm.getAltitude(), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zAlt2", -cvt(this.fm.getAltitude(), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkVisible("zRed1", this.fm.CT.BayDoorControl > 0.66F);
        this.mesh.chunkVisible("zYellow1", this.fm.CT.BayDoorControl < 0.33F);
        if(useRealisticNavigationInstruments())
        {
            this.mesh.chunkSetAngles("Z_Compass3", setNew.azimuth.getDeg(f) - setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass4", -setNew.waypointAzimuth.getDeg(f) - 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass5", setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F, 0.0F);
        } else
        {
            this.mesh.chunkSetAngles("Z_Compass4", -setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass3", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        }
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 4) != 0)
            this.mesh.chunkVisible("ZHolesL_D1", true);
        if((this.fm.AS.astateCockpitState & 8) != 0)
            this.mesh.chunkVisible("ZHolesL_D2", true);
        if((this.fm.AS.astateCockpitState & 0x10) != 0)
            this.mesh.chunkVisible("ZHolesR_D1", true);
        if((this.fm.AS.astateCockpitState & 0x20) != 0)
            this.mesh.chunkVisible("ZHolesR_D2", true);
        if((this.fm.AS.astateCockpitState & 1) != 0)
            this.mesh.chunkVisible("ZHolesF_D1", true);
        if((this.fm.AS.astateCockpitState & 0x80) != 0)
            this.mesh.chunkVisible("zOil_D1", true);
    }

    private boolean enteringAim;
    private static final float angleScale[] = {
        -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 
        75F, 76.5F, 77F, 78F, 79F, 80F
    };
    private static final float speedometerScale[] = {
        0.0F, 6.01F, 18.5F, 43.65F, 75.81F, 113.39F, 152F, 192.26F, 232.24F, 270.67F, 
        308.39F, 343.38F
    };
    private boolean bTurrVisible;
    private float saveFov;
    private float aAim;
    private float tAim;
    private boolean bEntered;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;

    static 
    {
        Property.set(CockpitHE_111H6_Late_Bombardier.class, "astatePilotIndx", 1);
    }
}
