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
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitB25C25_Bombardier extends CockpitBombardier {
	private class Variables {

		AnglesFork azimuth;
		AnglesFork waypointAzimuth;

		private Variables() {
			azimuth = new AnglesFork();
			waypointAzimuth = new AnglesFork();
		}

	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			setTmp = setOld;
			setOld = setNew;
			setNew = setTmp;
			float f = ((B_25C25)aircraft()).fSightCurForwardAngle;
			float f1 = ((B_25C25)aircraft()).fSightHeadTurn * 30F;
			float f2 = aircraft().FM.Or.getTangage();
			float f3 = aircraft().FM.Or.getKren() + (float)Math.sin(Math.toRadians(f)) * f1 * 0.01F
					+ (float)Math.sin(Math.toRadians(f1)) * f2;
			if (f2 > 20F)
				f2 = 20F;
			if (f2 < -20F)
				f2 = -20F;
			if (f3 > 20F)
				f3 = 20F;
			if (f3 < -20F)
				f3 = -20F;
			float f4 = f - f2;
			mesh.chunkSetAngles("RollGyro", 0.0F, -f3, 0.0F);
			mesh.chunkSetAngles("BlackBox", -f1, 0.0F, f4);
			if (bEntered) {
				HookPilot hookpilot = HookPilot.cur();
				hookpilot.setInstantOrient(aAim - f1, tAim + f, 0.0F);
			}
			mesh.chunkSetAngles("TurretA", 0.0F, aircraft().FM.turret[0].tu[0], 0.0F);
			mesh.chunkSetAngles("TurretB", 0.0F, aircraft().FM.turret[0].tu[1], 0.0F);
			float f5 = waypointAzimuth();
			setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
			if (useRealisticNavigationInstruments()) {
				setNew.waypointAzimuth.setDeg(f5 - 90F);
				setOld.waypointAzimuth.setDeg(f5 - 90F);
			} else {
				setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f5);
			}
			return true;
		}

		Interpolater() {
		}
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			HookPilot hookpilot = HookPilot.cur();
			hookpilot.doAim(false);
			Point3d point3d = new Point3d();
			point3d.set(0.15D, 0.0D, -0.1D);
			hookpilot.setTubeSight(point3d);
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

	private void prepareToEnter() {
		HookPilot hookpilot = HookPilot.cur();
		if (hookpilot.isPadlock())
			hookpilot.stopPadlock();
		hookpilot.doAim(true);
		hookpilot.setSimpleAimOrient(0.0F, -33F, 0.0F);
		enteringAim = true;
	}

	private void enter() {
		saveFov = Main3D.FOVX;
		CmdEnv.top().exec("fov " + saveBSFov);
		Main3D.cur3D().aircraftHotKeys.enableBombSightFov();
		HookPilot hookpilot = HookPilot.cur();
		hookpilot.setInstantOrient(aAim, tAim, 0.0F);
		hookpilot.setSimpleUse(true);
		hookpilot.setStabilizedBSUse(true);
		doSetSimpleUse(true);
		HotKeyEnv.enable("PanView", false);
		HotKeyEnv.enable("SnapView", false);
		bEntered = true;
	}

	private void leave() {
		if (enteringAim) {
			HookPilot hookpilot = HookPilot.cur();
			hookpilot.setInstantOrient(0.0F, -33F, 0.0F);
			hookpilot.doAim(false);
			hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
			return;
		}
		if (!bEntered) {
			return;
		} else {
			saveBSFov = Main3D.FOVX;
			Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
			CmdEnv.top().exec("fov " + saveFov);
			HookPilot hookpilot1 = HookPilot.cur();
			hookpilot1.setInstantOrient(0.0F, -33F, 0.0F);
			hookpilot1.doAim(false);
			hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
			hookpilot1.setSimpleUse(false);
			hookpilot1.setStabilizedBSUse(false);
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
		if (!isFocused())
			return;
		if (isToggleAim() == flag)
			return;
		if (flag)
			prepareToEnter();
		else
			leave();
	}

	public CockpitB25C25_Bombardier() {
		super("3DO/Cockpit/B-25J-Bombardier/BombardierB25C25.him", "bf109");
		enteringAim = false;
		saveBSFov = 23.913F;
		bEntered = false;
		setOld = new Variables();
		setNew = new Variables();
		try {
			Loc loc = new Loc();
			HookNamed hooknamed = new HookNamed(mesh, "CAMERAAIM");
			hooknamed.computePos(this, new Loc(), loc);
			aAim = loc.getOrient().getAzimut();
			tAim = loc.getOrient().getTangage();
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		cockpitNightMats = (new String[] { "textrbm9", "texture25", "norden_crosshair" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		printCompassHeading = true;
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl)
			setNightMats(true);
		else
			setNightMats(false);
	}

	public void reflectWorldToInstruments(float f) {
//        float f1 = ((B_25C25)aircraft()).fSightCurForwardAngle;
//        float f2 = ((B_25C25)aircraft()).fSightCurAltitude;
//        float f3 = ((B_25C25)aircraft()).fSightCurSpeed;
//        boolean flag = ((B_25C25)aircraft()).bSightClutch;
//        float f4 = ((B_25C25)aircraft()).fSightCurSideslip;
//        float f5 = ((B_25C25)aircraft()).fSightHeadTurn * 30F;
//        float f6 = ((B_25C25)aircraft()).getBombSightPDI();
//        mesh.chunkSetAngles("zFootBall", f5, 0.0F, 0.0F);
//        mesh.chunkSetAngles("zNPDI", f6 * 10F, 0.0F, 0.0F);
//        mesh.chunkSetAngles("zBsClutch", f5, 0.0F, 0.0F);
//        mesh.chunkSetAngles("zBSArm01", f5, 0.0F, 0.0F);
//        mesh.chunkSetAngles("zBSArm02", f6 * 10F, 0.0F, 0.0F);
//        mesh.chunkSetAngles("zClutchLever", flag ? 0.0F : 30F, 0.0F, 0.0F);
//        mesh.chunkSetAngles("zNAutoClutch", f6 * -10F, 0.0F, 0.0F);
//        mesh.chunkSetAngles("zSightAngle", 0.0F, 0.0F, -f1);
//        mesh.chunkSetAngles("zMirDrClutch", 0.0F, 0.0F, 5F * f1);
//        mesh.chunkSetAngles("zBSpeed", 0.0F, 0.0F, -cvt(f3 * f2, 100000F, 2.25E+007F, 0.0F, 360F));
//        mesh.chunkSetAngles("zNTurnDrift", f4, 0.0F, 0.0F);
//        mesh.chunkSetAngles("Z_IntToggle", 0.0F, 0.0F, aircraft().FM.CT.bombReleaseMode != 5 ? -60F : 0.0F);
//        mesh.chunkVisible("intLightOff", aircraft().FM.CT.bombReleaseMode != 5);
//        mesh.chunkVisible("intLightOn", aircraft().FM.CT.bombReleaseMode == 5);
//        mesh.chunkSetAngles("Z_IntBInterval", 0.0F, 0.0F, -5.64F * (float)aircraft().FM.CT.bombTrainAmount);
//        mesh.chunkSetAngles("Z_GroundSpeed", 0.0F, 0.0F, -184F + (float)aircraft().FM.CT.bombTrainDelay * 18.4F);
//        mesh.chunkSetAngles("Z_KnobGrndSpd", 0.0F, 0.0F, (float)aircraft().FM.CT.bombTrainDelay * -32F);
		
		
		mesh.chunkSetAngles(
				"zSpeed",
				0.0F,
				floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale),
				0.0F);
		mesh.chunkSetAngles("zSpeed1", 0.0F, floatindex(cvt(fm.getSpeedKMH(), 0.0F, 836.859F, 0.0F, 13F), speedometerScale),
				0.0F);
		mesh.chunkSetAngles("zAlt1", 0.0F, cvt((float)fm.Loc.z, 0.0F, 9144F, 0.0F, 10800F), 0.0F);
		mesh.chunkSetAngles("zAlt2", 0.0F, cvt((float)fm.Loc.z, 0.0F, 9144F, 0.0F, 1080F), 0.0F);
		mesh.chunkSetAngles("zHour", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		mesh.chunkSetAngles("zMinute", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("zSecond", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("zCompass1", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
		mesh.chunkSetAngles("zCompass2", 0.0F, setNew.waypointAzimuth.getDeg(0.1F), 0.0F);
		if (enteringAim) {
			HookPilot hookpilot = HookPilot.cur();
			if (hookpilot.isAimReached()) {
				enteringAim = false;
				enter();
			} else if (!hookpilot.isAim())
				enteringAim = false;
		}
		if (bEntered) {
			mesh.chunkVisible("BlackBox", true);
			mesh.chunkVisible("zReticle", true);
		} else {
			mesh.chunkVisible("BlackBox", false);
			mesh.chunkVisible("zReticle", false);
		}
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 1) != 0)
			mesh.chunkVisible("XGlassDamage1", true);
		if ((fm.AS.astateCockpitState & 8) != 0)
			mesh.chunkVisible("XGlassDamage2", true);
		if ((fm.AS.astateCockpitState & 0x20) != 0)
			mesh.chunkVisible("XGlassDamage2", true);
		if ((fm.AS.astateCockpitState & 2) != 0)
			mesh.chunkVisible("XGlassDamage3", true);
		if ((fm.AS.astateCockpitState & 4) != 0)
			mesh.chunkVisible("XHullDamage1", true);
		if ((fm.AS.astateCockpitState & 0x10) != 0)
			mesh.chunkVisible("XHullDamage2", true);
	}

	private static final float speedometerScale[] = { 0.0F, 2.5F, 54F, 104F, 154.5F, 205.5F, 224F, 242F, 259.5F, 277.5F,
			296.25F, 314F, 334F, 344.5F };
	private float saveFov;
	private float saveBSFov;
	private float aAim;
	private float tAim;
	private boolean bEntered;
	private boolean enteringAim;
	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;

	static {
		Property.set(CockpitB25C25_Bombardier.class, "astatePilotIndx", 2);
        Property.set(CockpitB25C25_Bombardier.class, "aiTuretNum", -2);
        Property.set(CockpitB25C25_Bombardier.class, "weaponControlNum", 3);
	}

}
