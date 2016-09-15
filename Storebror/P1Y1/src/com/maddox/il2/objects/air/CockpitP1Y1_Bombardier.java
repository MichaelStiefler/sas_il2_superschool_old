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

public class CockpitP1Y1_Bombardier extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            float f = ((P1Y1) CockpitP1Y1_Bombardier.this.aircraft()).fSightCurForwardAngle;
            float f1 = ((P1Y1) CockpitP1Y1_Bombardier.this.aircraft()).fSightCurSideslip;
            CockpitP1Y1_Bombardier.this.mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, f);
            if (CockpitP1Y1_Bombardier.this.bEntered) {
                if (P1Y.getGameVersion() >= 4.11F) {
                    HookPilot.current.setInstantOrient(CockpitP1Y1_Bombardier.this.aAim + (10F * f1), CockpitP1Y1_Bombardier.this.tAim + f, 0.0F);
                } else {
                    HookPilot.current.setSimpleAimOrient(CockpitP1Y1_Bombardier.this.aAim + f1, CockpitP1Y1_Bombardier.this.tAim + f, 0.0F);
                }
            }
            CockpitP1Y1_Bombardier.this.mesh.chunkSetAngles("Turret1A", 0.0F, -CockpitP1Y1_Bombardier.this.aircraft().FM.turret[1].tu[0], 0.0F);
            CockpitP1Y1_Bombardier.this.mesh.chunkSetAngles("Turret1B", 0.0F, CockpitP1Y1_Bombardier.this.aircraft().FM.turret[1].tu[1], 0.0F);
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Nose1_D0", false);
            this.aircraft().hierMesh().chunkVisible("ND_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            if (P1Y.getGameVersion() >= 4.11F) {
                Point3d point3d = new Point3d();
                point3d.set(0.15D, 0.09D, -0.06D);
                hookpilot.setTubeSight(point3d);
            }
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) {
            return;
        } else {
            this.leave();
            this.aircraft().hierMesh().chunkVisible("Nose1_D0", true);
            this.aircraft().hierMesh().chunkVisible("ND_D0", true);
            this.aircraft().hierMesh().chunkVisible("Turret2B_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    private void prepareToEnter() {
        if (P1Y.getGameVersion() < 4.11F)
            return;
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(-30F, -49.5F, 0.0F);
        this.enteringAim = true;
    }

    private void enter411andLater() {
        saveFov = Main3D.FOVX;
        HookPilot hookpilot = HookPilot.current;
        CmdEnv.top().exec("fov 23.913");
        if (hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.setInstantOrient(aAim, tAim, 0.0F);
        if (P1Y.getGameVersion() >= 4.12F)
            Main3D.cur3D().aircraftHotKeys.enableBombSightFov();
        else
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        hookpilot.setSimpleUse(true);
        doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    private void enter410andEarlier() {
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 23.913");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(aAim, tAim, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    private void enter() {
        if (P1Y.getGameVersion() >= 4.11F)
            enter411andLater();
        else
            enter410andEarlier();
    }

    private void leave411andLater() {
        HookPilot hookpilot = HookPilot.current;
        if (enteringAim) {
            hookpilot.doAim(false);
            hookpilot.setInstantOrient(-30F, -49.5F, 0.0F);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if (!bEntered)
            return;
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
        CmdEnv.top().exec("fov " + saveFov);
        hookpilot.doAim(false);
        hookpilot.setInstantOrient(-30F, -49.5F, 0.0F);
        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        hookpilot.setSimpleUse(false);
        doSetSimpleUse(false);
        boolean flag = HotKeyEnv.isEnabled("aircraftView");
        HotKeyEnv.enable("PanView", flag);
        HotKeyEnv.enable("SnapView", flag);
        bEntered = false;
    }

    private void leave410andEarlier() {
        if (!bEntered)
            return;
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
        CmdEnv.top().exec("fov " + saveFov);
        HookPilot hookpilot1 = HookPilot.current;
        hookpilot1.doAim(false);
        hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        hookpilot1.setSimpleUse(false);
        boolean flag = HotKeyEnv.isEnabled("aircraftView");
        HotKeyEnv.enable("PanView", flag);
        HotKeyEnv.enable("SnapView", flag);
        bEntered = false;
    }

    private void leave() {
        if (P1Y.getGameVersion() >= 4.11F)
            leave411andLater();
        else
            leave410andEarlier();
    }

    public void destroy() {
        super.destroy();
        this.leave();
    }

    public void doToggleAim(boolean flag) {
        if (!this.isFocused()) {
            return;
        }
        if (this.isToggleAim() == flag) {
            return;
        }
        if (flag) {
            if (P1Y.getGameVersion() >= 4.11F)
                prepareToEnter();
            else
                enter();
        } else {
            this.leave();
        }
    }

    public CockpitP1Y1_Bombardier() {
        super("3DO/Cockpit/P1Y1-Bombardier/hier.him", "he111");
        this.enteringAim = false;
        this.bEntered = false;
        try {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(this.mesh, "CAMERAAIM");
            hooknamed.computePos(this, this.pos.getAbs(), loc);
            this.aAim = loc.getOrient().getAzimut();
            this.tAim = loc.getOrient().getTangage();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (P1Y.getGameVersion() >= 4.11F) {
            if (this.enteringAim) {
                HookPilot hookpilot = HookPilot.current;
                if (hookpilot.isAimReached()) {
                    this.enteringAim = false;
                    this.enter();
                } else if (!hookpilot.isAim()) {
                    this.enteringAim = false;
                }
            }
        }
        if (this.bEntered) {
            this.mesh.chunkSetAngles("zAngleMark", -this.floatindex(this.cvt(((P1Y1) this.aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
            boolean flag = ((P1Y1) this.aircraft()).fSightCurReadyness > 0.93F;
            this.mesh.chunkVisible("BlackBox", true);
            this.mesh.chunkVisible("zReticle", flag);
            this.mesh.chunkVisible("zAngleMark", flag);
        } else {
            this.mesh.chunkVisible("BlackBox", false);
            this.mesh.chunkVisible("zReticle", false);
            this.mesh.chunkVisible("zAngleMark", false);
        }
    }

    private boolean            enteringAim;
    private static final float angleScale[] = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
    private float              saveFov;
    private float              aAim;
    private float              tAim;
    private boolean            bEntered;

    static {
        Property.set(CockpitP1Y1_Bombardier.class, "astatePilotIndx", 0);
    }

}
