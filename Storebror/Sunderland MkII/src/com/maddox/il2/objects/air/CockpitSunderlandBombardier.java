package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

/*
 * Bombardier Cockpit for Sunderland Mk.II by Barnesy/CWatson/Freemodding
 * Latest edit: 2016-03-09 by SAS~Storebror (Made cross version compatible)
 */
public class CockpitSunderlandBombardier extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            float f = ((Short_Sunderland) CockpitSunderlandBombardier.this.aircraft()).fSightCurForwardAngle;
            float f1 = ((Short_Sunderland) CockpitSunderlandBombardier.this.aircraft()).fSightCurSideslip;

            // Distinguish between base game versions, 4.10 and earlier use different viewports
            if (CockpitSunderlandBombardier.ConfigVersionToFloat() < 4.11F)
                CockpitSunderlandBombardier.this.mesh.chunkSetAngles("BlackBox", 0.0F, -f1, f);
            else
                CockpitSunderlandBombardier.this.mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, f);
            // ---

            if (CockpitSunderlandBombardier.this.bEntered) {
                HookPilot hookpilot = HookPilot.current;

                // Distinguish between base game versions, 4.10 and earlier use different function names
                if (CockpitSunderlandBombardier.ConfigVersionToFloat() < 4.11F)
                    hookpilot.setSimpleAimOrient(CockpitSunderlandBombardier.this.aAim + f1, CockpitSunderlandBombardier.this.tAim + f, 0.0F);
                else
                    hookpilot.setInstantOrient(CockpitSunderlandBombardier.this.aAim + 10F * f1, CockpitSunderlandBombardier.this.tAim + f, 0.0F);
                // ---
            }
            return true;
        }

        Interpolater() {}
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);

            // Distinguish between base game versions, 4.11 and later use different tube sight bombsight style
            if (CockpitSunderlandBombardier.ConfigVersionToFloat() >= 4.11F) {
                Point3d point3d = new Point3d();
                point3d.set(0.15D, 0.09D, -0.06D);
                hookpilot.setTubeSight(point3d);
            }
            // ---

            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            this.leave();
            super.doFocusLeave();
        }
    }

    private void prepareToEnter() {
        // Distinguish between base game versions, 4.10 and earlier don't use this function
        if (CockpitSunderlandBombardier.ConfigVersionToFloat() < 4.11F)
            return;
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(-30F, -49.5F, 0.0F);
        this.enteringAim = true;
    }

    private void enter() {
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 23.913");

        // Distinguish between base game versions, 4.10 and earlier use different way of entering "aimed" position
        if (CockpitSunderlandBombardier.ConfigVersionToFloat() < 4.11F) {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
            HookPilot hookpilot = HookPilot.current;
            if (hookpilot.isPadlock())
                hookpilot.stopPadlock();
            hookpilot.doAim(true);
            hookpilot.setSimpleUse(true);
            hookpilot.setSimpleAimOrient(this.aAim, this.tAim, 0.0F);
        } else {
            Main3D.cur3D().aircraftHotKeys.enableBombSightFov();
            HookPilot hookpilot = HookPilot.current;
            hookpilot.setInstantOrient(this.aAim, this.tAim, 0.0F);
            hookpilot.setSimpleUse(true);
            this.doSetSimpleUse(true);
        }
        // ---

        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
    }

    private void leave() {
        // Distinguish between base game versions, 4.11 and later use different way of leaving "aimed" position
        if (CockpitSunderlandBombardier.ConfigVersionToFloat() >= 4.11F) {
            if (this.enteringAim) {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setInstantOrient(-30F, -49.5F, 0.0F);
                hookpilot.doAim(false);
                hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
                return;
            }
        }
        // ---

        if (!this.bEntered)
            return;

        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
        CmdEnv.top().exec("fov " + this.saveFov);
        HookPilot hookpilot1 = HookPilot.current;
        // Distinguish between base game versions, 4.10 and earlier use different way of leaving "aimed" position
        if (CockpitSunderlandBombardier.ConfigVersionToFloat() < 4.11F) {
            hookpilot1.doAim(false);
            hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setSimpleUse(false);
        } else {
            hookpilot1.setInstantOrient(-30F, -49.5F, 0.0F);
            hookpilot1.doAim(false);
            hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setSimpleUse(false);
            this.doSetSimpleUse(false);
        }
        boolean flag = HotKeyEnv.isEnabled("aircraftView");
        HotKeyEnv.enable("PanView", flag);
        HotKeyEnv.enable("SnapView", flag);
        this.bEntered = false;
    }

    public void destroy() {
        super.destroy();
        this.leave();
    }

    public void doToggleAim(boolean flag) {
        if (!this.isFocused() || this.isToggleAim() == flag)
            return;
        if (flag) {
            // Distinguish between base game versions, 4.10 and earlier use different way of entering "aimed" position
            if (CockpitSunderlandBombardier.ConfigVersionToFloat() < 4.11F)
                this.enter();
            else
                this.prepareToEnter();
        } else {
            this.leave();
        }
    }

    public CockpitSunderlandBombardier() {
        super("3DO/Cockpit/SunderlandBombardier/BombardierSS.him", "he111");
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
        // Distinguish between base game versions, 4.11 and later use animated entering of "aimed" position
        if (CockpitSunderlandBombardier.ConfigVersionToFloat() >= 4.11F) {
            if (this.enteringAim) {
                HookPilot hookpilot = HookPilot.current;
                if (hookpilot.isAimReached()) {
                    this.enteringAim = false;
                    this.enter();
                } else if (!hookpilot.isAim())
                    this.enteringAim = false;
            }
        }
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            if (hookpilot.isAimReached()) {
                this.enteringAim = false;
                this.enter();
            } else if (!hookpilot.isAim())
                this.enteringAim = false;
        }
        if (this.bEntered) {
            this.mesh.chunkSetAngles("zAngleMark", -this.floatindex(this.cvt(((Short_Sunderland) this.aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
            boolean flag = ((Short_Sunderland) this.aircraft()).fSightCurReadyness > 0.93F;
            this.mesh.chunkVisible("BlackBox", true);
            this.mesh.chunkVisible("zReticle", flag);
            this.mesh.chunkVisible("zAngleMark", flag);
        } else {
            this.mesh.chunkVisible("BlackBox", false);
            this.mesh.chunkVisible("zReticle", false);
            this.mesh.chunkVisible("zAngleMark", false);
        }
    }

    // +++ Get Base Game Version (taken from SAS Common Utils)
    private static float fConfigVersion = -99.999F;

    private static float ConfigVersionToFloat() {
        if (fConfigVersion > 0.0F)
            return fConfigVersion;
        String theVersion = "4.00m";
        try {
            theVersion = (String) Config.class.getDeclaredField("VERSION").get(null);
        } catch (Exception e) {
            System.out.println("Couldn't resolve game version due to following error:");
            e.printStackTrace();
            return fConfigVersion;
        }
        String versionToParse = "";
        boolean digitSet = false;
        theVersion = theVersion.trim();
        for (int pos = 0; pos < theVersion.length(); pos++) {
            if (Character.isDigit(theVersion.charAt(pos)))
                versionToParse += theVersion.charAt(pos);
            else if (theVersion.charAt(pos) == '.') {
                if (digitSet)
                    continue;
                digitSet = true;
                versionToParse += theVersion.charAt(pos);
            } else
                break;
        }
        versionToParse += 'F';
        fConfigVersion = Float.parseFloat(versionToParse);
        System.out.println("SAS Common Utils Game Version = " + fConfigVersion);
        return fConfigVersion;
    }
    // ---

    private boolean            enteringAim;
    private static final float angleScale[] = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
    private float              saveFov;
    private float              aAim;
    private float              tAim;
    private boolean            bEntered;

    static {
        Property.set(CockpitSunderlandBombardier.class, "astatePilotIndx", 0);
    }

}
