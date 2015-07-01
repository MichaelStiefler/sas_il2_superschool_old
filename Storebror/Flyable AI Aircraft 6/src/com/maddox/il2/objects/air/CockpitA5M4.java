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
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CLASS;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitA5M4 extends CockpitPilot {
	private class Variables {

		float dimPos;
		float throttle;
		float prop;
		float mix;
		float altimeter;
		float man;
		float vspeed;
		float manifold;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		AnglesFork waypointDeviation;

		private Variables() {
			dimPos = 0.0F;
			azimuth = new AnglesFork();
			waypointAzimuth = new AnglesFork();
			waypointDeviation = new AnglesFork();
		}

	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (fm != null) {
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				if (cockpitDimControl) {
					if (setNew.dimPos < 1.0F)
						setNew.dimPos = setOld.dimPos + 0.03F;
				} else if (setNew.dimPos > 0.0F)
					setNew.dimPos = setOld.dimPos - 0.03F;
				setNew.manifold = 0.8F * setOld.manifold + 0.2F * fm.EI.engines[0].getManifoldPressure();
				setNew.throttle = 0.8F * setOld.throttle + 0.2F * fm.CT.PowerControl;
				setNew.prop = 0.8F * setOld.prop + 0.2F * fm.EI.engines[0].getControlProp();
				setNew.mix = 0.8F * setOld.mix + 0.2F * fm.EI.engines[0].getControlMix();
				setNew.man = 0.92F * setOld.man + 0.08F * fm.EI.engines[0].getManifoldPressure();
				setNew.altimeter = fm.getAltitude();
				float f = waypointAzimuth();
				setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
				setNew.waypointDeviation.setDeg(setOld.waypointDeviation.getDeg(0.1F), (f - setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-5F, 5F));
				setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), f);
				setNew.vspeed = 0.5F * setOld.vspeed + 0.5F * fm.getVertSpeed();
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
			point3d.set(0.25D, 0.0D, 0.0D);
			hookpilot.setTubeSight(point3d);
			aircraft().hierMesh().chunkVisible("Head1_D0", false);
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
			aircraft().hierMesh().chunkVisible("Head1_D0", true);
			super.doFocusLeave();
			return;
		}
	}

	private void prepareToEnter() {
		HookPilot hookpilot = HookPilot.current;
		if (hookpilot.isPadlock())
			hookpilot.stopPadlock();
		hookpilot.doAim(true);
		hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
		enteringAim = true;
	}

	private void enter() {
		HookPilot hookpilot = HookPilot.current;
		hookpilot.setSimpleUse(true);
		doSetSimpleUse(true);
		HotKeyEnv.enable("PanView", false);
		HotKeyEnv.enable("SnapView", false);
	}

	public void doSetSimpleUse(boolean flag) {
		super.doSetSimpleUse(flag);
		if (flag) {
			saveFov = Main3D.FOVX;
			CmdEnv.top().exec("fov 31");
			Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
			bEntered = true;
			mesh.chunkVisible("SuperReticle", true);
			mesh.chunkVisible("Z_BoxTinter", true);
			mesh.chunkVisible("EDET", false);
		}
	}

	private void leave() {
		if (enteringAim) {
			HookPilot hookpilot = HookPilot.current;
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
			hookpilot1.doAim(false);
			hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
			hookpilot1.setSimpleUse(false);
			doSetSimpleUse(false);
			boolean flag = HotKeyEnv.isEnabled("aircraftView");
			HotKeyEnv.enable("PanView", flag);
			HotKeyEnv.enable("SnapView", flag);
			bEntered = false;
			mesh.chunkVisible("SuperReticle", false);
			mesh.chunkVisible("Z_BoxTinter", false);
			mesh.chunkVisible("EDET", true);
			return;
		}
	}

	public void destroy() {
		leave();
		super.destroy();
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

	protected float waypointAzimuth() {
		WayPoint waypoint = fm.AP.way.curr();
		if (waypoint == null)
			return 0.0F;
		waypoint.getP(tmpP);
		tmpV.sub(tmpP, fm.Loc);
		float f;
		for (f = (float) (57.295779513082323D * Math.atan2(-tmpV.y, tmpV.x)); f <= -180F; f += 180F)
			;
		for (; f > 180F; f -= 180F)
			;
		return f;
	}

	public CockpitA5M4() {
		super("3DO/Cockpit/Ki-27(Ko)/CockpitA5M4.him", "bf109");
		setOld = new Variables();
		setNew = new Variables();
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
		cockpitNightMats = (new String[] { "gauge1", "gauge2", "gauge3", "gauge4", "gauge1_d", "gauge2_d", "gauge3_d", "gauge4_d", "Arrows", "Digits" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		if (acoustics != null)
			acoustics.globFX = new ReverbFXRoom(0.45F);
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
		if (bNeedSetUp) {
			reflectPlaneMats();
			reflectPlaneToModel();
			bNeedSetUp = false;
		}
		if (A5M4.bChangedPit) {
			reflectPlaneToModel();
			A5M4.bChangedPit = false;
		}
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(fm.CT.getCockpitDoor(), 0.05F, 0.95F, 0.0F, 0.55F);
		// mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Z_ReViTinter", 0.0F, cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -180F), 0.0F);
		mesh.chunkSetAngles("Z_BoxTinter", 0.0F, cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -180F), 0.0F);
		mesh.chunkSetAngles("Z_ColumnBase", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 20F, 0.0F);
		mesh.chunkSetAngles("Z_Column", 0.0F, -(pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 10F, 0.0F);
		mesh.chunkSetAngles("Z_ColumnWire", 0.0F, pictElev * 20F, 0.0F);
		mesh.chunkSetAngles("Z_PedalBase", 0.0F, -30F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("Z_RightPedal", 0.0F, 20F * fm.CT.getBrake(), 0.0F);
		mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 20F * fm.CT.getBrake(), 0.0F);
		mesh.chunkSetAngles("Z_RightWire", -30F * fm.CT.getRudder(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_LeftWire", -30F * fm.CT.getRudder(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Radiat", 0.0F, -450F * (pictRad = 0.9F * pictRad + 0.1F * fm.EI.engines[0].getControlRadiator()), 0.0F);
		mesh.chunkSetAngles("Z_Throtle1", 0.0F, cvt(setNew.throttle, 0.0F, 1.1F, -38F, 38F), 0.0F);
		mesh.chunkSetAngles("Z_Throtle2", 0.0F, 30F * (pictGun = 0.8F * pictGun + 0.2F * (fm.CT.saveWeaponControl[0] ? 1.0F : 0.0F)), 0.0F);
		mesh.chunkSetAngles("zPitch1", 0.0F, cvt(setNew.prop, 0.0F, 1.0F, -38F, 38F), 0.0F);
		mesh.chunkSetAngles("zTrim1", 0.0F, cvt(fm.CT.trimElevator, -0.5F, 0.5F, 35F, -35F), 0.0F);
		mesh.chunkSetAngles("zTrim2", 0.0F, cvt(fm.CT.trimElevator, -0.5F, 0.5F, -35F, 35F), 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(setNew.mix, 0.0F, 1.2F, 0.03675F, 0.0F);
		long l = Time.current();
		long l1 = l - oldTime;
		oldTime = l;
		float f1 = (float) l1 * 0.00016F;
		if (pictFlap < fm.CT.FlapsControl) {
			if (pictFlap + f1 >= fm.CT.FlapsControl)
				pictFlap = fm.CT.FlapsControl;
			else
				pictFlap += f1;
		} else if (pictFlap - f1 <= fm.CT.FlapsControl)
			pictFlap = fm.CT.FlapsControl;
		else
			pictFlap -= f1;
		mesh.chunkSetAngles("Z_Flaps", 0.0F, -3450F * pictFlap, 0.0F);
		mesh.chunkSetAngles("Z_Mag1", cvt(fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 76.5F, -28.5F), 0.0F, 0.0F);
		if ((fm.AS.astateCockpitState & 0x40) == 0) {
			mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, -7200F), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, -720F), 0.0F, 0.0F);
		}
		mesh.chunkSetAngles("Z_Speedometer1", -floatindex(cvt(0.539957F * Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 300F, 0.0F, 15F), speedometerScale), 0.0F, 0.0F);
		w.set(fm.getW());
		fm.Or.transform(w);
		mesh.chunkSetAngles("Z_TurnBank1", cvt(w.z, -0.23562F, 0.23562F, -30F, 30F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_TurnBank2", cvt(getBall(6D), -6F, 6F, 6F, -6F), 0.0F, 0.0F);
		float f2 = setNew.vspeed;
		if (Math.abs(f2) < 5F)
			mesh.chunkSetAngles("Z_Climb1", cvt(f2, -5F, 5F, 90F, -90F), 0.0F, 0.0F);
		else if (f2 > 0.0F)
			mesh.chunkSetAngles("Z_Climb1", cvt(f2, 5F, 30F, -90F, -180F), 0.0F, 0.0F);
		else
			mesh.chunkSetAngles("Z_Climb1", cvt(f2, -30F, -5F, 180F, 90F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Compass1", -setNew.azimuth.getDeg(f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Clock_H", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, -720F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Clock_Min", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
		f2 = cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut, 0.0F, 8.1F, 0.0F, 20F);
		for (int i = 1; i < 20; i++)
			mesh.chunkVisible("Z_OilP" + (i < 10 ? "0" + i : "" + i), f2 > (float) (20 - i));

		mesh.chunkSetAngles("Z_Manipres", cvt(setNew.manifold, 0.33339F, 1.66661F, 150F, -150F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_RPM1", cvt(fm.EI.engines[0].getRPM(), 200F, 3000F, -8.5F, -323F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Fuelpres", cvt(fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 2.0F, 0.0F, -360F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Oiltemp1", cvt(fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, -300F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Oilpres1", cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut, 0.0F, 5.5F, 0.0F, -300F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Tempcyl", cvt(fm.EI.engines[0].tWaterOut, 0.0F, 360F, 0.0F, -90.6F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Fuel", cvt(fm.M.fuel, 0.0F, 108F, -41F, -320F), 0.0F, 0.0F);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 2) != 0) {
			mesh.chunkVisible("XGlassDamage1", true);
			mesh.chunkVisible("XGlassDamage2", true);
			mesh.chunkVisible("XHullDamage3", true);
		}
		if ((fm.AS.astateCockpitState & 1) != 0)
			mesh.chunkVisible("XGlassDamage7", true);
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("Panel_D0", false);
			mesh.chunkVisible("Panel_D1", true);
			mesh.chunkVisible("Z_Speedometer1", false);
			mesh.chunkVisible("Z_TurnBank1", false);
			mesh.chunkVisible("Z_TurnBank2", false);
			mesh.chunkVisible("Z_Climb1", false);
			mesh.chunkVisible("Z_RPM1", false);
			mesh.chunkVisible("Z_Manipres", false);
			mesh.chunkVisible("Z_Fuel", false);
			mesh.chunkVisible("Z_Fuelpres", false);
			mesh.chunkVisible("Z_Altimeter1", false);
			mesh.chunkVisible("Z_Altimeter2", false);
			mesh.chunkVisible("Z_Oiltemp1", false);
			mesh.chunkVisible("Z_Oilpres1", false);
			mesh.chunkVisible("Z_Clock_H", false);
			mesh.chunkVisible("Z_Clock_Min", false);
			mesh.chunkVisible("Z_Tempcyl", false);
			mesh.chunkVisible("XHullDamage3", true);
		}
		if ((fm.AS.astateCockpitState & 4) != 0) {
			mesh.chunkVisible("XGlassDamage1", true);
			mesh.chunkVisible("XHullDamage1", true);
			mesh.chunkVisible("XHullDamage2", true);
		}
		if ((fm.AS.astateCockpitState & 8) != 0)
			mesh.chunkVisible("XHullDamage4", true);
		if ((fm.AS.astateCockpitState & 0x80) != 0)
			mesh.chunkVisible("Z_OilSplats_D1", true);
		if ((fm.AS.astateCockpitState & 0x10) != 0) {
			mesh.chunkVisible("XGlassDamage6", true);
			mesh.chunkVisible("XHullDamage1", true);
			mesh.chunkVisible("XHullDamage5", true);
		}
		if ((fm.AS.astateCockpitState & 0x20) != 0)
			mesh.chunkVisible("XHullDamage4", true);
	}

	public void toggleDim() {
		cockpitDimControl = !cockpitDimControl;
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl)
			setNightMats(true);
		else
			setNightMats(false);
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
		mesh.materialReplace("Matt1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
		mesh.materialReplace("Matt2D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
		mesh.materialReplace("Gloss1D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
		mesh.materialReplace("Matt1D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
		mesh.materialReplace("Gloss2D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
		mesh.materialReplace("Matt2D2o", mat);
	}

	protected void reflectPlaneToModel() {
	}

	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private float pictAiler;
	private float pictElev;
	private float pictRad;
	private float pictGun;
	private float pictFlap;
	private boolean bNeedSetUp;
	private long oldTime;
	private boolean enteringAim;
	private static final float speedometerScale[] = { 0.0F, 6.5F, 16.5F, 49F, 91.5F, 143.5F, 199F, 260F, 318F, 376.5F, 433F, 484F, 534F, 576F, 620F, 660F };
	private float saveFov;
	private boolean bEntered;
	private Point3d tmpP;
	private Vector3d tmpV;

	static {
		Property.set(CLASS.THIS(), "normZNs", new float[] { 1.37F, 0.8F, 0.93F, 0.8F });
	}

}
