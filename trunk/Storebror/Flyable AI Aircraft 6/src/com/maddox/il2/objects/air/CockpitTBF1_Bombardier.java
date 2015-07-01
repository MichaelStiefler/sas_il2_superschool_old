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
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitTBF1_Bombardier extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (bEntered) {
				float f = 0.0F;
				float f1 = 0.0F;
				if (aircraft() instanceof TBF1) {
					f = ((TBF1) aircraft()).fSightCurForwardAngle;
					f1 = ((TBF1) aircraft()).fSightCurSideslip;
				} else if (aircraft() instanceof TBF1C) {
					f = ((TBF1C) aircraft()).fSightCurForwardAngle;
					f1 = ((TBF1C) aircraft()).fSightCurSideslip;
				} else if (aircraft() instanceof TBM3) {
					f = ((TBM3) aircraft()).fSightCurForwardAngle;
					f1 = ((TBM3) aircraft()).fSightCurSideslip;
				} else if (aircraft() instanceof TBM3AVENGER3) {
					f = ((TBM3AVENGER3) aircraft()).fSightCurForwardAngle;
					f1 = ((TBM3AVENGER3) aircraft()).fSightCurSideslip;
				}
				mesh.chunkSetAngles("BlackBox", 0.0F, -f1, f);
				HookPilot hookpilot = HookPilot.current;
				hookpilot.setInstantOrient(aAim + f1, tAim + f, 0.0F);
			}
			return true;
		}

		Interpolater() {
		}
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(false);
			Point3d point3d = new Point3d();
			point3d.set(0.0D, 0.0D, 0.0D);
			hookpilot.setTubeSight(point3d);
			enter();
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		if (!isFocused()) {
			return;
		} else {
			leave();
			super.doFocusLeave();
			return;
		}
	}

	private void enter() {
		saveFov = Main3D.FOVX;
		CmdEnv.top().exec("fov 23.913");
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
	}

	private void leave() {
		if (!bEntered) {
			return;
		} else {
			Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
			CmdEnv.top().exec("fov " + saveFov);
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(false);
			hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
			hookpilot.setSimpleUse(false);
			doSetSimpleUse(false);
			boolean flag = HotKeyEnv.isEnabled("aircraftView");
			HotKeyEnv.enable("PanView", flag);
			HotKeyEnv.enable("SnapView", flag);
			bEntered = false;
			return;
		}
	}

	public void destroy() {
		super.destroy();
		leave();
	}

	public void doToggleAim(boolean flag) {
	}

	public CockpitTBF1_Bombardier() {
		super("3DO/Cockpit/TBM-Bombardier/hier.him", "bf109");
		bEntered = false;
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
		interpPut(new Interpolater(), null, Time.current(), null);
	}

	public void reflectWorldToInstruments(float f) {
		boolean flag = false;
		if (aircraft() instanceof TBF1) {
			mesh.chunkSetAngles("zAngleMark", -floatindex(cvt(((TBF1) aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
			flag = ((TBF1) aircraft()).fSightCurReadyness > 0.93F;
		} else if (aircraft() instanceof TBF1C) {
			mesh.chunkSetAngles("zAngleMark", -floatindex(cvt(((TBF1C) aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
			flag = ((TBF1C) aircraft()).fSightCurReadyness > 0.93F;
		} else if (aircraft() instanceof TBM3) {
			mesh.chunkSetAngles("zAngleMark", -floatindex(cvt(((TBM3) aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
			flag = ((TBM3) aircraft()).fSightCurReadyness > 0.93F;
		} else if (aircraft() instanceof TBM3AVENGER3) {
			mesh.chunkSetAngles("zAngleMark", -floatindex(cvt(((TBM3AVENGER3) aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
			flag = ((TBM3AVENGER3) aircraft()).fSightCurReadyness > 0.93F;
		}
		mesh.chunkVisible("BlackBox", true);
		mesh.chunkVisible("zReticle", flag);
		mesh.chunkVisible("zAngleMark", flag);
	}

	private static final float angleScale[] = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
	private float saveFov;
	private float aAim;
	private float tAim;
	private boolean bEntered;

	static {
		Property.set(CockpitTBF1_Bombardier.class, "astatePilotIndx", 0);
	}

}
