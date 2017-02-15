
package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.CrossVersion;
import java.io.PrintStream;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, F_14, Aircraft

public class CockpitF_14Bombsight extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            float f = ((F_14)aircraft()).fSightCurForwardAngle;
            float f1 = ((F_14)aircraft()).fSightCurSideslip;
            mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, f);
            if(bEntered && bBAiming)
            {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setSimpleAimOrient(aAim + 10F * f1, tAim + f, 0.0F);
            }
            float f2 = fm.getAltitude();
            float f3 = (float)(-(Math.abs(((FlightModelMain) (fm)).Vwld.length()) * Math.sin(Math.toRadians(Math.abs(((FlightModelMain) (fm)).Or.getTangage())))) * 0.10189999639987946D);
            f3 += (float)Math.sqrt(f3 * f3 + 2.0F * f2 * 0.1019F);
            float f4 = Math.abs((float)((FlightModelMain) (fm)).Vwld.length()) * (float)Math.cos(Math.toRadians(Math.abs(((FlightModelMain) (fm)).Or.getTangage())));
            float f5 = (f4 * f3 + 10F) - 10F;
            alpha = 90F - Math.abs(((FlightModelMain) (fm)).Or.getTangage()) - (float)Math.toDegrees(Math.atan(f5 / f2));
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
            enter();
            go_top();
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

    private void enter()
    {
        saveFov = Main3D.FOVX;
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    private void leave()
    {
        if(!bEntered)
        {
            return;
        } else
        {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + saveFov);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            bEntered = false;
            return;
        }
    }

    private void go_top()
    {
        bBAiming = false;
        CmdEnv.top().exec("fov 33.3");
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(false);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(aDiv, tDiv, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
    }

    private void go_bottom()
    {
        bBAiming = true;
        CmdEnv.top().exec("fov 23.913");
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(aAim, tAim, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
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
            go_bottom();
        else
            go_top();
    }

    public CockpitF_14Bombsight()
    {
        super("3DO/Cockpit/Ar-234B-2-Bombardier/hier.him", "he111");
        bEntered = false;
        bBAiming = false;
        CrossVersion.setPrintCompassHeading(this, true);
        try
        {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(super.mesh, "CAMERAAIM");
            hooknamed.computePos(this, super.pos.getAbs(), loc);
            aAim = loc.getOrient().getAzimut();
            tAim = loc.getOrient().getTangage();
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        try
        {
            Loc loc1 = new Loc();
            HookNamed hooknamed1 = new HookNamed(super.mesh, "CAMERA");
            hooknamed1.computePos(this, super.pos.getAbs(), loc1);
            aDiv = loc1.getOrient().getAzimut();
            tDiv = loc1.getOrient().getTangage();
        }
        catch(Exception exception1)
        {
            System.out.println(exception1.getMessage());
            exception1.printStackTrace();
        }
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bBAiming)
        {
            super.mesh.chunkSetAngles("zAngleMark", -floatindex(cvt(((F_14)aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
            boolean flag = ((F_14)aircraft()).fSightCurReadyness > 0.93F;
            super.mesh.chunkVisible("zReticle", flag);
            super.mesh.chunkVisible("zAngleMark", flag);
        } else
        {
            super.mesh.chunkSetAngles("zGSDimm", -alpha, 0.0F, 0.0F);
        }
    }

    public boolean isEnableFocusing()
    {
        if(aircraft().thisWeaponsName.startsWith("GAttackLGB:") || aircraft().thisWeaponsName.startsWith("Fighter:") || aircraft().thisWeaponsName.startsWith("Recon:") || aircraft().thisWeaponsName.startsWith("Def"))
            return false;
        else
            return super.isEnableFocusing();
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    private float saveFov;
    private float aAim;
    private float tAim;
    private float aDiv;
    private float tDiv;
    private float alpha;
    private boolean bEntered;
    private boolean bBAiming;
    private static final float angleScale[] = {
        -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 
        75F, 76.5F, 77F, 78F, 79F, 80F
    };

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitF_14Bombsight.class, "astatePilotIndx", 0);
    }





}