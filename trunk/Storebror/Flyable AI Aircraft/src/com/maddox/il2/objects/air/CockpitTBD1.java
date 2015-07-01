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
// Last Edited at: 2013/06/11

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.CLASS;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitTBD1 extends CockpitPilot {
	private class Variables {

		float throttle;
		float altimeter;
		float azimuth;
		float vspeed;
		float waypointAzimuth;

		private Variables() {
		}

	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (fm != null) {
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				setNew.throttle = (10F * setOld.throttle + fm.CT.PowerControl) / 11F;
				setNew.altimeter = fm.getAltitude();
				if (Math.abs(fm.Or.getKren()) < 30F)
					setNew.azimuth = (35F * setOld.azimuth + -fm.Or.getYaw()) / 36F;
				if (setOld.azimuth > 270F && setNew.azimuth < 90F)
					setOld.azimuth -= 360F;
				if (setOld.azimuth < 90F && setNew.azimuth > 270F)
					setOld.azimuth += 360F;
				setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
				setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
			}
			return true;
		}

		Interpolater() {
		}
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("Blister2_D0", false);
			aircraft().hierMesh().chunkVisible("Blister5_D0", false);
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(false);
			Point3d point3d = new Point3d();
			point3d.set(0.23000000417232513D, 0.0D, -0.05000000074505806D);
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
			aircraft().hierMesh().chunkVisible("Blister2_D0", true);
			aircraft().hierMesh().chunkVisible("Blister5_D0", true);
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
		bEntered = true;
	}

	public void doSetSimpleUse(boolean flag) {
		super.doSetSimpleUse(flag);
		if (flag) {
			saveFov = Main3D.FOVX;
			CmdEnv.top().exec("fov 31");
			Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
			mesh.chunkVisible("SuperReticle", true);
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
			boolean flag = HotKeyEnv.isEnabled("aircraftView");
			HotKeyEnv.enable("PanView", flag);
			HotKeyEnv.enable("SnapView", flag);
			bEntered = false;
			mesh.chunkVisible("SuperReticle", false);
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
		if (waypoint == null) {
			return 0.0F;
		} else {
			waypoint.getP(tmpP);
			tmpV.sub(tmpP, fm.Loc);
			return (float) (57.295779513082323D * Math.atan2(-tmpV.y, tmpV.x));
		}
	}

	public CockpitTBD1() {
		super("3DO/Cockpit/F2A-2/TBD.him", "bf109");
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		bNeedSetUp = true;
		enteringAim = false;
		bEntered = false;
		tmpP = new Point3d();
		tmpV = new Vector3d();
		cockpitNightMats = (new String[] { "F2ABoxes", "F2Acables", "F2Agauges", "F2Agauges1", "F2Agauges3", "F2AWindshields", "F2Azegary4" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		if (acoustics != null)
			acoustics.globFX = new ReverbFXRoom(0.45F);
		limits6DoF = (new float[] { 0.7F, 0.055F, -0.07F, 0.11F, 0.12F, -0.11F, 0.03F, -0.04F });
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
			bNeedSetUp = false;
		}
		if (gun[0] == null)
			gun[0] = ((Aircraft) fm.actor).getGunByHookName("_MGUN01");
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.725F);
		mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Z_Columnbase", 0.0F, 25F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl), 0.0F);
		mesh.chunkSetAngles("Z_Column", 0.0F, 0.0F, 10F * (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl));
		resetYPRmodifier();
		Cockpit.xyz[0] = cvt(pictAiler, -1F, 1.0F, -0.054F, 0.054F);
		mesh.chunkSetLocate("Z_ColumnR", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetLocate("Z_ColumnL", Cockpit.xyz, Cockpit.ypr);
		resetYPRmodifier();
		Cockpit.xyz[0] = cvt(fm.CT.getRudder(), -1F, 1.0F, -0.0575F, 0.0575F);
		mesh.chunkSetLocate("Z_Pedal_L", Cockpit.xyz, Cockpit.ypr);
		resetYPRmodifier();
		Cockpit.xyz[0] = cvt(fm.CT.getRudder(), -1F, 1.0F, 0.0575F, -0.0575F);
		mesh.chunkSetLocate("Z_Pedal_R", Cockpit.xyz, Cockpit.ypr);
		resetYPRmodifier();
		if (fm.EI.engines[0].getStage() > 0 && fm.EI.engines[0].getStage() < 3)
			Cockpit.xyz[1] = 0.02825F;
		mesh.chunkSetLocate("Z_Starter", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Z_Ign_Switch", 0.0F, -20F * (float) fm.EI.engines[0].getControlMagnetos(), 0.0F);
		mesh.chunkSetAngles("Z_Throttle", 0.0F, 100F * (pictThtl = 0.9F * pictThtl + 0.1F * fm.EI.engines[0].getControlThrottle()), 0.0F);
		mesh.chunkSetAngles("Z_mixture", 0.0F, 91.66667F * (pictMix = 0.9F * pictMix + 0.1F * fm.EI.engines[0].getControlMix()), 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(pictProp = 0.9F * pictProp + 0.1F * fm.EI.engines[0].getControlProp(), 0.0F, 1.0F, 0.0F, -0.035F);
		mesh.chunkSetLocate("Z_Pitch", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Z_Booster_Lever", 0.0F, -5F * (float) fm.EI.engines[0].getControlCompressor(), 0.0F);
		mesh.chunkSetAngles("Z_TL_lock", 0.0F, fm.Gears.bTailwheelLocked ? -30F : 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_TL_wheel", 0.0F, 2240F * fm.CT.getArrestor(), 0.0F);
		if (fm.CT.FlapsControl == 0.0F && fm.CT.getFlap() != 0.0F)
			mesh.chunkSetAngles("Z_Flaps", 0.0F, 0.0F, 0.0F);
		else if (fm.CT.FlapsControl == 1.0F && fm.CT.getFlap() != 1.0F)
			mesh.chunkSetAngles("Z_Flaps", 0.0F, -70F, 0.0F);
		else
			mesh.chunkSetAngles("Z_Flaps", 0.0F, -35F, 0.0F);
		if (fm.CT.GearControl == 0.0F && fm.CT.getGear() != 0.0F)
			mesh.chunkSetAngles("Z_gearlever", 0.0F, 4F, 0.0F);
		else if (fm.CT.GearControl == 1.0F && fm.CT.getGear() != 1.0F)
			mesh.chunkSetAngles("Z_gearlever", 0.0F, -70F, 0.0F);
		else
			mesh.chunkSetAngles("Z_gearlever", 0.0F, -35F, 0.0F);
		mesh.chunkSetAngles("Z_Magn_Compas", 90F + interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Compass", 90F + interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[0] = cvt(fm.CT.getGear(), 0.0F, 1.0F, 0.0F, -0.135F);
		mesh.chunkSetLocate("Z_Gear", Cockpit.xyz, Cockpit.ypr);
		resetYPRmodifier();
		Cockpit.xyz[0] = cvt(fm.CT.getFlap(), 0.0F, 1.0F, -0.04155F, 0.0211F);
		mesh.chunkSetLocate("Z_Flap", Cockpit.xyz, Cockpit.ypr);
		if (gun[0] != null && gun[0].haveBullets()) {
			mesh.chunkSetAngles("Z_Ammo_W1", 0.0F, -0.36F * (float) gun[0].countBullets(), 0.0F);
			mesh.chunkSetAngles("Z_Ammo_W2", 0.0F, -3.6F * (float) gun[0].countBullets(), 0.0F);
			mesh.chunkSetAngles("Z_Ammo_W3", 0.0F, -36F * (float) gun[0].countBullets(), 0.0F);
			mesh.chunkSetAngles("Z_Ammo_W4", 0.0F, -0.36F * (float) gun[0].countBullets(), 0.0F);
			mesh.chunkSetAngles("Z_Ammo_W5", 0.0F, -3.6F * (float) gun[0].countBullets(), 0.0F);
			mesh.chunkSetAngles("Z_Ammo_W6", 0.0F, -36F * (float) gun[0].countBullets(), 0.0F);
		}
		mesh.chunkSetAngles("Z_TL_indi", 0.0F, 45F * fm.CT.getArrestor(), 0.0F);
		mesh.chunkSetAngles("Z_Manifold", cvt(fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 1.693189F, 20F, 340F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Alt_Large", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Alt_Small", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Speed", floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 981.5598F, 0.0F, 53F), speedometerScale), 0.0F, 0.0F);
		w.set(fm.getW());
		fm.Or.transform(w);
		mesh.chunkSetAngles("Z_Turn", cvt(w.z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Bank", cvt(getBall(8D), -8F, 8F, 12F, -12F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Climb", cvt(setNew.vspeed, -20F, 20F, -180F, 180F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Clock_Min", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Clock_H", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Hor_Handle", -fm.Or.getKren(), 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(fm.Or.getTangage(), -45F, 45F, 0.019F, -0.019F);
		mesh.chunkSetLocate("Z_Hor_Handle2", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Z_Temp_Handle", cvt(fm.EI.engines[0].tWaterOut, 0.0F, 400F, 0.0F, 100F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Temp_Eng", cvt(fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 170F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Oil_Eng", cvt(1.0F + 0.05F * fm.EI.engines[0].tOilIn, 0.0F, 15F, 0.0F, 180F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Fuel_Eng", cvt(fm.M.fuel > 1.0F ? 10F * fm.EI.engines[0].getPowerOutput() : 0.0F, 0.0F, 20F, 0.0F, 180F), 0.0F, 0.0F);
		if (fm.EI.engines[0].getRPM() < 1000F)
			mesh.chunkSetAngles("Z_Tahometr_Eng", cvt(fm.EI.engines[0].getRPM(), 0.0F, 1000F, 0.0F, 90F), 0.0F, 0.0F);
		else
			mesh.chunkSetAngles("Z_Tahometr_Eng", cvt(fm.EI.engines[0].getRPM(), 1000F, 3500F, 90F, 540F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_carbmixtemp", cvt(Atmosphere.temperature((float) fm.Loc.z), 213.09F, 333.09F, -180F, 180F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Fuel_1", cvt(fm.M.fuel, 0.0F, 504F, 0.0F, -272.5F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Fuel_2", cvt(fm.M.fuel, 0.0F, 504F, 0.0F, -272.5F), 0.0F, 0.0F);
		if (fm.Gears.isHydroOperable())
			mesh.chunkSetAngles("Z_hydropress", 133F, 0.0F, 0.0F);
		else
			mesh.chunkSetAngles("Z_hydropress", 1.0F, 0.0F, 0.0F);
		mesh.chunkVisible("XLampGear_1", !fm.Gears.lgear || !fm.Gears.lgear);
	}

	public void reflectCockpitState() {
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl)
			setNightMats(true);
		else
			setNightMats(false);
	}

	private Gun gun[] = { null };
	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	private Vector3f w;
	private float pictAiler;
	private float pictElev;
	private float pictThtl;
	private float pictMix;
	private float pictProp;
	private boolean bNeedSetUp;
	private boolean enteringAim;
	private static final float speedometerScale[] = { 0.0F, 0.5F, 1.0F, 2.0F, 5.5F, 14F, 20F, 26F, 33.5F, 42F, 50.5F, 60.5F, 71.5F, 81.5F, 95.2F, 108.5F, 122.5F, 137F, 152F, 166.7F, 182F, 198F, 214.5F, 231F, 247.5F, 263.5F, 278.5F, 294F, 307F, 317F,
			330.5F, 343F, 355.5F, 367.5F, 379.5F, 391.5F, 404F, 417F, 430.5F, 444F, 458F, 473.5F, 487.5F, 503.5F, 519.5F, 535.5F, 552F, 569.5F, 586F, 602.5F, 619F, 631.5F, 643F, 648.5F };
	private float saveFov;
	private boolean bEntered;
	private Point3d tmpP;
	private Vector3d tmpV;

	static {
		Property.set(CLASS.THIS(), "normZNs", new float[] { 1.26F, 1.12F, 1.15F, 1.12F });
	}

}
