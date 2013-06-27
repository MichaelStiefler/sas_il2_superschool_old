// This file is part of the SAS IL-2 Sturmovik 1946 4.12
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
// Last Edited at: 2013/01/22

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitSB2M103_Bombardier extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			resetYPRmodifier();
			float f = ((SB_2M103) aircraft()).fSightCurSpeed;
			float f1 = ((SB_2M103) aircraft()).fSightCurAltitude;
			curAlt = (19F * curAlt + f1) / 20F;
			curSpd = (19F * curSpd + f) / 20F;
			mesh.chunkSetAngles("zScaleKM", 0.04F * curAlt, 0.0F, 0.0F);
			mesh.chunkSetAngles("zScaleM", 0.36F * curAlt, 0.0F, 0.0F);
			mesh.chunkSetAngles("zScaleKMH", -0.8F * (curSpd - 50F), 0.0F, 0.0F);
			float f2 = 0.5F * (float) Math.tan(Math.atan((83.333335876464844D * Math.sqrt((2.0F * curAlt) / Atmosphere.g())) / (double) curAlt));
			float f3 = (float) Math.tan(Math.atan(((double) (curSpd / 3.6F) * Math.sqrt((2.0F * curAlt) / Atmosphere.g())) / (double) curAlt));
			Cockpit.xyz[0] = -0.0005F * curAlt;
			Cockpit.xyz[1] = -1F * (f2 - f3);
			mesh.chunkSetLocate("zScaleCurve", Cockpit.xyz, Cockpit.ypr);
			return true;
		}

		Interpolater() {
		}
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(true);
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
		if (isFocused()) {
			leave();
			super.doFocusLeave();
		}
	}

	private void enter() {
		saveFov = Main3D.FOVX;
		CmdEnv.top().exec("fov 45.0");
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
		if (bEntered) {
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
		}
	}

	public void destroy() {
		super.destroy();
		leave();
	}

	public void doToggleAim(boolean flag) {
	}

	public CockpitSB2M103_Bombardier() {
		super("3DO/Cockpit/TB-3-Bombardier/hier.him", "he111");
		curAlt = 300F;
		curSpd = 50F;
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
		cockpitNightMats = (new String[] { "BombGauges", "Gauge03" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
	}

	public void reflectWorldToInstruments(float f) {
		if (bEntered) {
			mesh.chunkVisible("BlackBox", true);
			mesh.chunkVisible("zReticle", true);
			mesh.chunkVisible("zScaleCurve", true);
			mesh.chunkVisible("zScaleM", true);
			mesh.chunkVisible("zScaleKM", true);
			mesh.chunkVisible("zScaleKMH", true);
		} else {
			mesh.chunkVisible("BlackBox", false);
			mesh.chunkVisible("zReticle", false);
			mesh.chunkVisible("zScaleCurve", false);
			mesh.chunkVisible("zScaleM", false);
			mesh.chunkVisible("zScaleKM", false);
			mesh.chunkVisible("zScaleKMH", false);
		}
	}

	private float curAlt;
	private float curSpd;
	private float saveFov;
	private float aAim;
	private float tAim;
	private boolean bEntered;

	static {
		Property.set(CockpitSB2M103_Bombardier.class, "astatePilotIndx", 0);
	}

}
