// This file is part of the SAS IL-2 Sturmovik 1946 4.13
// Flyable AI Aircraft Mod package.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Original file source: 1C/Maddox/TD
// Modified by: SAS - Special Aircraft Services
//              www.sas1946.com
//
// Last Edited by: SAS~Storebror
// Last Edited on: 2014/10/28

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CLASS;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitDB3F_Bombardier extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (bEntered) {
				float f = ((IL_4_DB3F) aircraft()).fSightCurForwardAngle;
				float f1 = -((IL_4_DB3F) aircraft()).fSightCurSideslip;
				mesh.chunkSetAngles("BlackBox", f1, 0.0F, -f);
				HookPilot hookpilot = HookPilot.current;
				hookpilot.setInstantOrient(-f1, tAim + f, 0.0F);
			}
	           if(fm != null)
	            {
	                setTmp = setOld;
	                setOld = setNew;
	                setNew = setTmp;
	                setNew.throttle1 = 0.9F * setOld.throttle1 + 0.1F * fm.EI.engines[0].getControlThrottle();
	                setNew.throttle2 = 0.9F * setOld.throttle2 + 0.1F * fm.EI.engines[1].getControlThrottle();
	                setNew.altimeter = fm.getAltitude();
	                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
	                setNew.vspeed = (100F * setOld.vspeed + fm.getVertSpeed()) / 101F;
	                if(useRealisticNavigationInstruments())
	                {
	                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), getBeaconDirection());
	                } else
	                {
	                    float f1 = waypointAzimuth();
	                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f1 - fm.Or.azimut());
	                }
	            }
			return true;
		}

		Interpolater() {
		}
	}

    private class Variables
    {

        float throttle1;
        float throttle2;
        float altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float vspeed;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(10F);
    }

    protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(false);
			Point3d point3d = new Point3d();
            point3d.set(0.25D, 0.0D, -0.09D);
			hookpilot.setTubeSight(point3d);
            aircraft().hierMesh().chunkVisible("CFE_D0", false);
            aircraft().hierMesh().chunkVisible("CFE_D1", false);
            aircraft().hierMesh().chunkVisible("CFE_D2", false);
            aircraft().hierMesh().chunkVisible("CFE_D3", false);
            aircraft().hierMesh().chunkVisible("Cockpit1_D0", false);
            aircraft().hierMesh().chunkVisible("Cockpit2_D0", false);
            aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		if (isFocused()) {
			leave();
            aircraft().hierMesh().chunkVisible("CFE_D" + aircraft().chunkDamageVisible("CF"), true);
            aircraft().hierMesh().chunkVisible("Cockpit1_D0", true);
            aircraft().hierMesh().chunkVisible("Cockpit2_D0", true);
            aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
			super.doFocusLeave();
		}
	}

	private void enter() {
		saveFov = Main3D.FOVX;
		CmdEnv.top().exec("fov 60.0");
		Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
		HookPilot hookpilot = HookPilot.current;
		if (hookpilot.isPadlock())
			hookpilot.stopPadlock();
		hookpilot.doAim(true);
		hookpilot.setSimpleUse(true);
		hookpilot.setSimpleAimOrient(aAim, tAim, 0.0F);
		hookpilot.setInstantOrient(aAim, tAim, 0.0F);
		doSetSimpleUse(true);
		HotKeyEnv.enable("PanView", false);
		HotKeyEnv.enable("SnapView", false);
		bEntered = true;
        mesh.chunkVisible("BlackBox", true);
        mesh.chunkVisible("zReticle", true);
        mesh.chunkVisible("zMark1", true);
        mesh.chunkVisible("zMark2", true);
        mesh.chunkVisible("zBulb", true);
        mesh.chunkVisible("zRefraction", true);
	}

	private void leave() {
		if (enteringAim) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.setInstantOrient(-5F, -33F, 0.0F);
			hookpilot.doAim(false);
			hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
			return;
		}
		if (!bEntered) {
			return;
		} else {
			Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
			CmdEnv.top().exec("fov " + saveFov);
			HookPilot hookpilot1 = HookPilot.current;
			hookpilot1.setInstantOrient(-5F, -33F, 0.0F);
			hookpilot1.doAim(false);
			hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
			hookpilot1.setSimpleUse(false);
			doSetSimpleUse(false);
			boolean flag = HotKeyEnv.isEnabled("aircraftView");
			HotKeyEnv.enable("PanView", flag);
			HotKeyEnv.enable("SnapView", flag);
			bEntered = false;
            mesh.chunkVisible("BlackBox", false);
            mesh.chunkVisible("zReticle", false);
            mesh.chunkVisible("zMark1", false);
            mesh.chunkVisible("zMark2", false);
            mesh.chunkVisible("zBulb", false);
            mesh.chunkVisible("zRefraction", false);
			return;
		}
	}

	public void destroy() {
		super.destroy();
		leave();
	}

	public void doToggleAim(boolean flag) {
		if (!isFocused())
			return;
		if (isToggleAim() == flag)
			return;
		if (flag)
			prepareToEnter();
		else
			leave();
	}

	private void prepareToEnter() {
		HookPilot hookpilot = HookPilot.current;
		if (hookpilot.isPadlock())
			hookpilot.stopPadlock();
		hookpilot.doAim(true);
		hookpilot.setSimpleAimOrient(-5F, -33F, 0.0F);
		enteringAim = true;
	}

	public CockpitDB3F_Bombardier() {
		super("3DO/Cockpit/Il-4-Bombardier/hier.him", "he111");
		bEntered = false;
		enteringAim = false;
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
		try {
			Loc loc = new Loc();
			HookNamed hooknamed = new HookNamed(mesh, "CAMERAAIM");
			hooknamed.computePos(this, pos.getAbs(), loc);
			aAim = loc.getOrient().getAzimut();
			tAim = loc.getOrient().getTangage();
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
        cockpitNightMats = (new String[] {
                "Prib_one", "Prib_two", "Prib_three", "DPrib_one", "DPrib_two", "DPrib_three"
            });
            setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
	}

	public void reflectWorldToInstruments(float f) {
		if (enteringAim) {
			HookPilot hookpilot = HookPilot.current;
			if (hookpilot.isAimReached()) {
				enteringAim = false;
				enter();
			} else if (!hookpilot.isAim())
				enteringAim = false;
		}
		if (bEntered) {
			mesh.chunkSetAngles("zMark1", ((IL_4_DB3F) aircraft()).fSightCurForwardAngle * 3.675F, 0.0F, 0.0F);
			float f1 = cvt(((IL_4_DB3F) aircraft()).fSightSetForwardAngle, -15F, 75F, -15F, 75F);
			mesh.chunkSetAngles("zMark2", f1 * 3.675F, 0.0F, 0.0F);
			resetYPRmodifier();
			Cockpit.xyz[0] = cvt(fm.Or.getKren() * Math.abs(fm.Or.getKren()), -1225F, 1225F, -0.23F, 0.23F);
			Cockpit.xyz[1] = cvt((fm.Or.getTangage() - 1.0F) * Math.abs(fm.Or.getTangage() - 1.0F), -1225F, 1225F, 0.23F, -0.23F);
			Cockpit.ypr[0] = cvt(fm.Or.getKren(), -45F, 45F, -180F, 180F);
			mesh.chunkSetLocate("zBulb", Cockpit.xyz, Cockpit.ypr);
			resetYPRmodifier();
			Cockpit.xyz[0] = cvt(Cockpit.xyz[0], -0.23F, 0.23F, 0.0095F, -0.0095F);
			Cockpit.xyz[1] = cvt(Cockpit.xyz[1], -0.23F, 0.23F, 0.0095F, -0.0095F);
			mesh.chunkSetLocate("zRefraction", Cockpit.xyz, Cockpit.ypr);
        } else
        {
            mesh.chunkSetAngles("Z_bomb_hatch_winch", 0.0F, 0.0F, aircraft().getBayDoor() * -720F);
            mesh.chunkSetAngles("Z_hTrim_Oelir02", 1000F * fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_hThrot_L", -31F * interp(setNew.throttle1, setOld.throttle1, f), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_hThrot_R", -31F * interp(setNew.throttle2, setOld.throttle2, f), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_tThrot_L", 0.0F, 0.0F - 31F * interp(setNew.throttle1, setOld.throttle1, f), 0.0F);
            mesh.chunkSetAngles("Z_tThrot_R", 0.0F, 0.0F - 31F * interp(setNew.throttle2, setOld.throttle2, f), 0.0F);
            mesh.chunkSetAngles("Z_Pedal_L2", -15F * fm.CT.getRudder(), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_tPedal_L2", 0.0F, -15F * fm.CT.getRudder(), 0.0F);
            mesh.chunkSetAngles("Z_Pedal_R2", 15F * fm.CT.getRudder(), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_tPedal_R2", 0.0F, 15F * fm.CT.getRudder(), 0.0F);
            mesh.chunkSetAngles("Z_alt_m", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_alt_km", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_speed", -floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 400F, 0.0F, 8F), speedometerScale), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_RPK", cvt(setNew.waypointAzimuth.getDeg(f), -30F, 30F, 34F, -34F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_Compass2b", setNew.azimuth.getDeg(f) - 5F, 0.0F, 0.0F);
            w.set(fm.getW());
            fm.Or.transform(w);
            mesh.chunkSetAngles("Z_pioner", -cvt(w.z, -0.23562F, 0.23562F, 27.5F, -27.5F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_pioner_circle", cvt(getBall(8D), -8F, 8F, 24F, -24F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_variometer", 90F - cvt(setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_acho_hours", -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_acho_minutes", -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_acho_seconds", -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 21600F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_Wheel", 0.0F, 45F * (pictAiler = 0.65F * pictAiler + 0.35F * fm.CT.AileronControl), 0.0F);
        }
	}

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 1) != 0)
        {
            mesh.chunkVisible("DM_6", true);
            mesh.chunkVisible("DM_8", true);
            mesh.chunkVisible("DM_7", true);
            mesh.chunkVisible("DM_1", true);
        }
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("Prib_one", false);
            mesh.chunkVisible("Prib_one_Dm", true);
            mesh.chunkVisible("Z_speed", false);
            mesh.chunkVisible("Z_alt_m", false);
            mesh.chunkVisible("Z_alt_km", false);
            mesh.chunkVisible("DM_2", true);
            mesh.chunkVisible("DM_1", true);
        }
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("DM_7", true);
            mesh.chunkVisible("DM_4", true);
        }
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("DM_2", true);
            mesh.chunkVisible("DM_4", true);
            mesh.chunkVisible("DM_5", true);
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("DM_7", true);
            mesh.chunkVisible("DM_3", true);
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("DM_8", true);
            mesh.chunkVisible("DM_6", true);
        }
        if((fm.AS.astateCockpitState & 2) != 0 && (fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("Prib_two", false);
            mesh.chunkVisible("Prib_two_Dm", true);
            mesh.chunkVisible("Z_pioner", false);
            mesh.chunkVisible("Z_pioner_circle", false);
            mesh.chunkVisible("Z_variometer", false);
            mesh.chunkVisible("DM_5", true);
            mesh.chunkVisible("DM_3", true);
        }
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    protected void reflectPlaneToModel()
    {
        if(isFocused())
        {
            aircraft().hierMesh().chunkVisible("CFE_D0", false);
            aircraft().hierMesh().chunkVisible("CFE_D1", false);
            aircraft().hierMesh().chunkVisible("CFE_D2", false);
            aircraft().hierMesh().chunkVisible("CFE_D3", false);
        }
    }

    private float saveFov;
	private float aAim;
	private float tAim;
	private boolean bEntered;
	private boolean enteringAim;

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private float pictAiler;
    private static final float speedometerScale[] = {
        0.0F, 32F, 50F, 85F, 132.5F, 191.5F, 250F, 303.5F, 360F
    };

    static {
		Property.set(CockpitDB3F_Bombardier.class, "astatePilotIndx", 1);
		Property.set(CLASS.THIS(), "normZN", 0.5F);
	}

}
