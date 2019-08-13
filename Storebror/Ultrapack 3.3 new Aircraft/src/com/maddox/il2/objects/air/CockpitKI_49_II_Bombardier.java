package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitKI_49_II_Bombardier extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            float f = ((KI_49)aircraft()).fSightCurForwardAngle;
            float f1 = ((KI_49)aircraft()).fSightCurSideslip;
            mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, f);
            if(bEntered)
            {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setInstantOrient(aAim + 10F * f1, tAim + f, 0.0F);
            }
            mesh.chunkSetAngles("Turret1A", 0.0F, -aircraft().FM.turret[0].tu[0], 0.0F);
            mesh.chunkSetAngles("Turret1B", 0.0F, aircraft().FM.turret[0].tu[1], 0.0F);
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
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.15D, 0.09D, -0.06D);
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
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(-30F, -49.5F, 0.0F);
        enteringAim = true;
    }

    private void enter()
    {
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 23.913");
        Main3D.cur3D().aircraftHotKeys.enableBombSightFov();
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
            hookpilot.setInstantOrient(-30F, -49.5F, 0.0F);
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
            hookpilot1.setInstantOrient(-30F, -49.5F, 0.0F);
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

    public CockpitKI_49_II_Bombardier()
    {
        super("3DO/Cockpit/Ki49-Bombardier/hier.him", "he111");
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
        interpPut(new Interpolater(), null, Time.current(), null);
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
            this.mesh.chunkSetAngles("zAngleMark", -floatindex(cvt(((KI_49) ((KI_49_II)aircraft())).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
            boolean flag = ((KI_49) ((KI_49_II)aircraft())).fSightCurReadyness > 0.93F;
            this.mesh.chunkVisible("BlackBox", true);
            this.mesh.chunkVisible("zReticle", flag);
            this.mesh.chunkVisible("zAngleMark", flag);
        } else
        {
            this.mesh.chunkVisible("BlackBox", false);
            this.mesh.chunkVisible("zReticle", false);
            this.mesh.chunkVisible("zAngleMark", false);
        }
    }

    private boolean enteringAim;
    private static final float angleScale[] = {
        -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 
        75F, 76.5F, 77F, 78F, 79F, 80F
    };
    private float saveFov;
    private float aAim;
    private float tAim;
    private boolean bEntered;

    static 
    {
        Property.set(CockpitKI_49_II_Bombardier.class, "astatePilotIndx", 0);
    }
}
