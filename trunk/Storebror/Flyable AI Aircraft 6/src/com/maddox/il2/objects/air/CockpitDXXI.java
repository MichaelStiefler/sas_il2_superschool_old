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
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CLASS;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitDXXI extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		protected float getGyroDelta() {
			return 0.0F;
		}

		public boolean tick() {
			if (bNeedSetUp) {
				reflectPlaneMats();
				bNeedSetUp = false;
			}
			if (ac != null && ac.bChangedPit) {
				reflectPlaneToModel();
				ac.bChangedPit = false;
			}
			setTmp = setOld;
			setOld = setNew;
			setNew = setTmp;
			if ((fm.AS.astateCockpitState & 2) != 0 && setNew.stbyPosition < 1.0F) {
				setNew.stbyPosition = setOld.stbyPosition + 0.0125F;
				setOld.stbyPosition = setNew.stbyPosition;
			}
			setNew.altimeter = fm.getAltitude();
			if (useRealisticNavigationInstruments())
				setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut() + getGyroDelta());
			else
				setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
			setNew.throttle = (10F * setOld.throttle + fm.EI.engines[0].getControlThrottle()) / 11F;
			setNew.mix = (10F * setOld.mix + fm.EI.engines[0].getControlMix()) / 11F;
			setNew.prop = setOld.prop;
			if (setNew.prop < fm.EI.engines[0].getControlProp() - 0.01F)
				setNew.prop += 0.0025F;
			if (setNew.prop > fm.EI.engines[0].getControlProp() + 0.01F)
				setNew.prop -= 0.0025F;
			w.set(fm.getW());
			fm.Or.transform(w);
			setNew.turn = (12F * setOld.turn + w.z) / 13F;
			setNew.vspeed = (299F * setOld.vspeed + fm.getVertSpeed()) / 300F;
			pictSupc = 0.8F * pictSupc + 0.2F * (float) fm.EI.engines[0].getControlCompressor();
			if (cockpitDimControl) {
				if (setNew.dimPos < 1.0F)
					setNew.dimPos = setOld.dimPos + 0.03F;
			} else if (setNew.dimPos > 0.0F)
				setNew.dimPos = setOld.dimPos - 0.03F;
			if ((double) flaps > (double) fm.CT.FlapsControl - 0.050000000000000003D && (double) flaps < (double) fm.CT.FlapsControl + 0.050000000000000003D || fm.CT.bHasFlapsControlRed)
				flapsDirection = 0;
			else if (flaps < fm.CT.FlapsControl) {
				flaps = flaps + 0.00095F;
				flapsPump = flapsPump + flapsPumpIncrement;
				flapsDirection = 1;
				if (flapsPump < 0.0F || flapsPump > 1.0F)
					flapsPumpIncrement = -flapsPumpIncrement;
			} else if (flaps > fm.CT.FlapsControl) {
				flaps = flaps - 0.005F;
				flapsPump = flapsPump + flapsPumpIncrement;
				flapsDirection = -1;
				if (flapsPump < 0.0F || flapsPump > 1.0F)
					flapsPumpIncrement = -flapsPumpIncrement;
			}
			if (!fm.Gears.bTailwheelLocked && tailWheelLock < 1.0F)
				tailWheelLock = tailWheelLock + 0.05F;
			else if (fm.Gears.bTailwheelLocked && tailWheelLock > 0.0F)
				tailWheelLock = tailWheelLock - 0.05F;
			updateCompass();
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float altimeter;
		AnglesFork azimuth;
		float throttle;
		float mix;
		float prop;
		float turn;
		float vspeed;
		float stbyPosition;
		float dimPos;
		Point3d planeLoc;
		Point3d planeMove;
		Vector3d compassPoint[];
		Vector3d cP[];

		private Variables() {
			azimuth = new AnglesFork();
			planeLoc = new Point3d();
			planeMove = new Point3d();
			compassPoint = new Vector3d[4];
			cP = new Vector3d[4];
			compassPoint[0] = new Vector3d(0.0D, Math.sqrt(1.0D - CockpitDXXI.compassZ * CockpitDXXI.compassZ), CockpitDXXI.compassZ);
			compassPoint[1] = new Vector3d(-Math.sqrt(1.0D - CockpitDXXI.compassZ * CockpitDXXI.compassZ), 0.0D, CockpitDXXI.compassZ);
			compassPoint[2] = new Vector3d(0.0D, -Math.sqrt(1.0D - CockpitDXXI.compassZ * CockpitDXXI.compassZ), CockpitDXXI.compassZ);
			compassPoint[3] = new Vector3d(Math.sqrt(1.0D - CockpitDXXI.compassZ * CockpitDXXI.compassZ), 0.0D, CockpitDXXI.compassZ);
			cP[0] = new Vector3d(0.0D, Math.sqrt(1.0D - CockpitDXXI.compassZ * CockpitDXXI.compassZ), CockpitDXXI.compassZ);
			cP[1] = new Vector3d(-Math.sqrt(1.0D - CockpitDXXI.compassZ * CockpitDXXI.compassZ), 0.0D, CockpitDXXI.compassZ);
			cP[2] = new Vector3d(0.0D, -Math.sqrt(1.0D - CockpitDXXI.compassZ * CockpitDXXI.compassZ), CockpitDXXI.compassZ);
			cP[3] = new Vector3d(Math.sqrt(1.0D - CockpitDXXI.compassZ * CockpitDXXI.compassZ), 0.0D, CockpitDXXI.compassZ);
		}

	}

	public CockpitDXXI() {
		super("3DO/Cockpit/DXXI_DUTCH/hier.him", "bf109");
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		bNeedSetUp = true;
		pictAiler = 0.0F;
		pictElev = 0.0F;
		pictSupc = 0.0F;
		flaps = 0.0F;
		bEntered = false;
		hasRevi = false;
		tailWheelLock = 1.0F;
		flapsDirection = 0;
		flapsPump = 0.0F;
		flapsPumpIncrement = 0.1F;
		rpmGeneratedPressure = 0.0F;
		oilPressure = 0.0F;
		compassFirst = 0;
		enteringAim = false;
		HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK01");
		Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
		light1 = new LightPointActor(new LightPoint(), loc.getPoint());
		light1.light.setColor(126F, 232F, 245F);
		light1.light.setEmit(0.0F, 0.0F);
		pos.base().draw.lightMap().put("LAMPHOOK01", light1);
		hooknamed = new HookNamed(mesh, "LAMPHOOK02");
		loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
		light2 = new LightPointActor(new LightPoint(), loc.getPoint());
		light2.light.setColor(126F, 232F, 245F);
		light2.light.setEmit(0.0F, 0.0F);
		pos.base().draw.lightMap().put("LAMPHOOK02", light2);
		hooknamed = new HookNamed(mesh, "LAMPHOOK03");
		loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
		light3 = new LightPointActor(new LightPoint(), loc.getPoint());
		light3.light.setColor(126F, 232F, 245F);
		light3.light.setEmit(0.0F, 0.0F);
		pos.base().draw.lightMap().put("LAMPHOOK03", light3);
		hooknamed = new HookNamed(mesh, "LAMPHOOK04");
		loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
		light4 = new LightPointActor(new LightPoint(), loc.getPoint());
		light4.light.setColor(126F, 232F, 245F);
		light4.light.setEmit(0.0F, 0.0F);
		pos.base().draw.lightMap().put("LAMPHOOK04", light4);
		hooknamed = new HookNamed(mesh, "LAMPHOOK05");
		loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
		light5 = new LightPointActor(new LightPoint(), loc.getPoint());
		light5.light.setColor(126F, 232F, 245F);
		light5.light.setEmit(0.0F, 0.0F);
		pos.base().draw.lightMap().put("LAMPHOOK05", light5);
		cockpitNightMats = (new String[] { "gauge_speed", "gauge_alt", "gauge_fuel", "gauges_various_1", "gauges_various_2", "LABELS1", "gauges_various_3", "gauges_various4", "gauges_various_3_dam", "gauge_alt_dam", "gauges_various_2_dam" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		if (acoustics != null)
			acoustics.globFX = new ReverbFXRoom(0.45F);
	}

	public void setRevi() {
		hasRevi = true;
		mesh.chunkVisible("reticle", true);
		mesh.chunkVisible("reticlemask", true);
		mesh.chunkVisible("Revi_D0", true);
		mesh.chunkVisible("Z_sight_cap", false);
		mesh.chunkVisible("tubeSight", false);
		mesh.chunkVisible("tubeSightLens", false);
		mesh.chunkVisible("tube_inside", false);
		mesh.chunkVisible("tube_mask", false);
		mesh.chunkVisible("Z_sight_cap_inside", false);
		mesh.chunkVisible("GlassTube", false);
		mesh.chunkVisible("GlassRevi", true);
		mesh.chunkVisible("Z_reviIron", true);
		mesh.chunkVisible("Z_reviDimmer", true);
		mesh.chunkVisible("Z_reviDimmerLever", true);
		mesh.materialReplace("PanelC", "PanelCRevi");
		HookPilot hookpilot = HookPilot.current;
		hookpilot.setTubeSight(false);
	}

	protected boolean getChangedPit() {
		return false;
	}

	protected void setChangedPit(boolean changedPit) {
	}

	public void reflectWorldToInstruments(float f) {
		float f1 = 0.0F;
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
		if (enteringAim) {
			HookPilot hookpilot = HookPilot.current;
			if (hookpilot.isAimReached()) {
				enteringAim = false;
				enter();
			} else if (!hookpilot.isAim())
				enteringAim = false;
		}
		if (getChangedPit()) {
			reflectPlaneToModel();
			setChangedPit(false);
		}
		mesh.chunkSetAngles("Z_reviIron", 90F * setNew.stbyPosition, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_manifold", cvt(pictManifold = 0.85F * pictManifold + 0.15F * fm.EI.engines[0].getManifoldPressure() * 76F, 30F, 120F, 22F, 296F), 0.0F, 0.0F);
		f1 = -15F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl);
		mesh.chunkSetAngles("Z_stick_horiz_axis", f1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_aileron_rods", -f1 / 14F, 0.0F, 0.0F);
		f1 = 12F * (pictElev = 0.85F * pictElev + 0.2F * fm.CT.ElevatorControl) + 2.0F;
		mesh.chunkSetAngles("Z_Stick", f1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_elev_wire1", -f1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_elev_wire2", -f1, 0.0F, 0.0F);
		f1 = fm.CT.getRudder();
		mesh.chunkSetAngles("Z_wheel_break_valve", -12F * f1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_pedal_L", 24F * f1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_pedal_R", -24F * f1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_rudder_rod_L", -25F * f1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_rudder_rod_R", 25F * f1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Throttle", -70F * interp(setNew.throttle, setOld.throttle, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Mixture", -70F * interp(setNew.mix, setOld.mix, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_alt1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 7200F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_alt2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 720F), 0.0F, 0.0F);
		float f2 = Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH());
		if (f2 < 80F)
			mesh.chunkSetAngles("Z_Need_speed", cvt(f2, 0.0F, 80F, 0.0F, -11F), 0.0F, 0.0F);
		else if (f2 < 120F)
			mesh.chunkSetAngles("Z_Need_speed", cvt(f2, 80F, 120F, -11F, -40F), 0.0F, 0.0F);
		else if (f2 < 160F)
			mesh.chunkSetAngles("Z_Need_speed", cvt(f2, 120F, 160F, -40F, -78.5F), 0.0F, 0.0F);
		else if (f2 < 200F)
			mesh.chunkSetAngles("Z_Need_speed", cvt(f2, 160F, 200F, -78.5F, -130.5F), 0.0F, 0.0F);
		else if (f2 < 360F)
			mesh.chunkSetAngles("Z_Need_speed", cvt(f2, 200F, 360F, -130.5F, -329F), 0.0F, 0.0F);
		else
			mesh.chunkSetAngles("Z_Need_speed", cvt(f2, 360F, 600F, -329F, -550F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_gyro", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_clock_hour", -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, -720F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_clock_minute", -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_clock_sec", -cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_fuel", -cvt(fm.M.fuel, 0.0F, 300F, 0.0F, 52F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_oiltemp", cvt(fm.EI.engines[0].tOilIn, 0.0F, 100F, 0.0F, 329F), 0.0F, 0.0F);
		f1 = fm.EI.engines[0].getRPM();
		mesh.chunkSetAngles("Z_Need_rpm", cvt(f1, 440F, 3320F, 0.0F, -332F), 0.0F, 0.0F);
		if (fm.Or.getKren() < -110F || fm.Or.getKren() > 110F)
			rpmGeneratedPressure = rpmGeneratedPressure - 2.0F;
		else if (f1 < rpmGeneratedPressure)
			rpmGeneratedPressure = rpmGeneratedPressure - (rpmGeneratedPressure - f1) * 0.01F;
		else
			rpmGeneratedPressure = rpmGeneratedPressure + (f1 - rpmGeneratedPressure) * 0.001F;
		if (rpmGeneratedPressure < 800F)
			oilPressure = cvt(rpmGeneratedPressure, 0.0F, 800F, 0.0F, 4F);
		else if (rpmGeneratedPressure < 1800F)
			oilPressure = cvt(rpmGeneratedPressure, 800F, 1800F, 4F, 5F);
		else
			oilPressure = cvt(rpmGeneratedPressure, 1800F, 2750F, 5F, 5.8F);
		float f3 = 0.0F;
		if (fm.EI.engines[0].tOilOut > 90F)
			f3 = cvt(fm.EI.engines[0].tOilOut, 90F, 110F, 1.1F, 1.5F);
		else if (fm.EI.engines[0].tOilOut < 50F)
			f3 = cvt(fm.EI.engines[0].tOilOut, 0.0F, 50F, 2.0F, 0.9F);
		else
			f3 = cvt(fm.EI.engines[0].tOilOut, 50F, 90F, 0.9F, 1.1F);
		float f4 = f3 * fm.EI.engines[0].getReadyness() * oilPressure;
		mesh.chunkSetAngles("Z_Need_oilpressure", cvt(f4, 0.0F, 7F, 0.0F, 315F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_cylheadtemp", cvt(fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 110F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_fuelpressure", cvt(rpmGeneratedPressure, 0.0F, 1800F, 0.0F, 65F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_magneto", -30F * (float) fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[1] = -cvt(fm.Or.getTangage(), -20F, 20F, 0.04F, -0.04F);
		mesh.chunkSetLocate("Z_Need_red_liquid", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Z_Need_Turn", -cvt(setNew.turn, -0.2F, 0.2F, -30F, 30F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_bank", -cvt(getBall(8D), -8F, 8F, 14F, -14F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_variometer", -cvt(setNew.vspeed, -20F, 20F, 180F, -180F), 0.0F, 0.0F);
		if (fm.getAltitude() < 3000F)
			mesh.chunkSetAngles("Z_Need_oxygeneflow", 0.0F, 0.0F, 0.0F);
		else
			mesh.chunkSetAngles("Z_Need_oxygeneflow", 200F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_oxygenetank", 250F, 0.0F, 0.0F);
		if (ac.tiltCanopyOpened) {
			mesh.chunkSetAngles("CanopyL1", ac.canopyF * 125F, 0.0F, 0.0F);
			mesh.chunkSetAngles("CanopyL2", ac.canopyF * 80F, 0.0F, 0.0F);
		} else {
			mesh.chunkSetAngles("CanopyL1", 0.0F, 0.0F, 0.0F);
			mesh.chunkSetAngles("CanopyL2", 0.0F, 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_sliding_window_L", cvt(ac.canopyF, 0.0F, 1.0F, 0.0F, 0.75F), 0.0F, 0.0F);
		}
		mesh.chunkSetAngles("Z_flaps_indicator", 0.7F * flaps, 0.0F, 0.0F);
		if (flapsDirection == 1) {
			mesh.chunkSetAngles("Z_flaps_valve", -33F, 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_flapsLeverKnob", 33F, 0.0F, 0.0F);
		} else if (flapsDirection == -1) {
			mesh.chunkSetAngles("Z_flaps_valve", 33F, 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_flapsLeverKnob", -33F, 0.0F, 0.0F);
		} else {
			mesh.chunkSetAngles("Z_flaps_valve", 0.0F, 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_flapsLeverKnob", 0.0F, 0.0F, 0.0F);
		}
		mesh.chunkSetAngles("Z_trim_indicator", 1.9F * -fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_trim_wheel", 600F * fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
		if (fm.CT.bHasBrakeControl) {
			float f5 = fm.CT.getBrake();
			mesh.chunkSetAngles("Z_break_handle", f5 * 20F, 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Need_breakpressureR", cvt(f5 + f5 * fm.CT.getRudder(), 0.0F, 1.5F, 0.0F, 148F), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Need_breakpressureL", -cvt(f5 - f5 * fm.CT.getRudder(), 0.0F, 1.5F, 0.0F, 148F), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Need_breakpressure1", -150F + f5 * 20F, 0.0F, 0.0F);
		}
		mesh.chunkSetAngles("Z_flaps_pump", -flapsPump * 40F, 0.0F, 0.0F);
		if (fm.AS.bLandingLightOn)
			mesh.chunkSetAngles("Z_switch_landing_light", -35F, 0.0F, 0.0F);
		else
			mesh.chunkSetAngles("Z_switch_landing_light", 0.0F, 0.0F, 0.0F);
		if (fm.AS.bNavLightsOn)
			mesh.chunkSetAngles("Z_switch_navigation_light", -35F, 0.0F, 0.0F);
		else
			mesh.chunkSetAngles("Z_switch_navigation_light", 0.0F, 0.0F, 0.0F);
		if (cockpitLightControl)
			mesh.chunkSetAngles("Z_switch_cockpit_light", -35F, 0.0F, 0.0F);
		else
			mesh.chunkSetAngles("Z_switch_cockpit_light", 0.0F, 0.0F, 0.0F);
		if (tailWheelLock >= 1.0F) {
			mesh.chunkSetAngles("Z_tailwheel", tailWheelLock * 57F, 0.0F, 7F);
			mesh.chunkSetAngles("Z_tailwheel_lever_wire", tailWheelLock * 57F, 0.0F, 7F);
		} else {
			mesh.chunkSetAngles("Z_tailwheel", tailWheelLock * 57F, 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_tailwheel_lever_wire", tailWheelLock * 57F, 0.0F, 0.0F);
		}
		mesh.chunkSetAngles("Z_wheelLockKnob", tailWheelLock * 57F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_extinguisher", (float) fm.EI.engines[0].getExtinguishers() * 95F, 0.0F, 0.0F);
		if (hasRevi) {
			mesh.chunkSetAngles("Z_reviDimmer", -cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -90F), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_reviDimmerLever", -cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, 0.004F), 0.0F, 0.0F);
		} else {
			float f6 = cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -130F);
			mesh.chunkSetAngles("Z_sight_cap", f6, 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_sight_cap_big", f6, 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_sight_cap_inside", f6, 0.0F, 0.0F);
		}
		mesh.chunkSetAngles("Z_trigger", 1.6F * (fm.CT.saveWeaponControl[0] ? 1.0F : 0.0F), 0.0F, 0.0F);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 2) != 0) {
			if (hasRevi) {
				mesh.chunkVisible("reticle", false);
				mesh.chunkVisible("reticlemask", false);
				mesh.chunkVisible("Revi_D0", false);
				mesh.chunkVisible("Revi_D1", true);
			}
			mesh.chunkVisible("GlassDamageFront2", true);
			mesh.chunkVisible("HullDamageRear", true);
		}
		if ((fm.AS.astateCockpitState & 1) != 0) {
			mesh.chunkVisible("GlassDamageFront", true);
			mesh.chunkVisible("HullDamageRear", true);
		}
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("Gauges_d0", false);
			mesh.chunkVisible("Gauges_d1", true);
			mesh.chunkVisible("HullDamageFront", true);
			mesh.chunkVisible("Z_Need_manifold", false);
			mesh.chunkVisible("Z_Need_oilpressure", false);
			mesh.chunkVisible("Z_Need_rpm", false);
			mesh.chunkVisible("Z_Need_alt1", false);
			mesh.chunkVisible("Z_Need_alt2", false);
			mesh.chunkVisible("Z_Need_variometer", false);
			mesh.chunkVisible("Z_Need_clock_sec", false);
			mesh.chunkVisible("Z_Need_clock_minute", false);
			mesh.chunkVisible("Z_Need_clock_hour", false);
			mesh.chunkVisible("Z_Need_clock_timer", false);
			mesh.chunkVisible("Z_Need_cylheadtemp", false);
		}
		if ((fm.AS.astateCockpitState & 4) != 0) {
			mesh.chunkVisible("GlassDamageLeft", true);
			mesh.chunkVisible("HullDamageLeft", true);
		}
		if ((fm.AS.astateCockpitState & 8) != 0) {
			mesh.chunkVisible("GlassDamageLeft", true);
			mesh.chunkVisible("GlassDamageLeft2", true);
			mesh.chunkVisible("HullDamageLeft", true);
		}
		if ((fm.AS.astateCockpitState & 0x10) != 0) {
			mesh.chunkVisible("GlassDamageRight", true);
			mesh.chunkVisible("HullDamageRight", true);
		}
		if ((fm.AS.astateCockpitState & 0x20) != 0) {
			mesh.chunkVisible("GlassDamageRight", true);
			mesh.chunkVisible("HullDamageRight", true);
		}
		if ((fm.AS.astateCockpitState & 0x80) != 0)
			if (hasRevi)
				mesh.chunkVisible("OilRevi", true);
			else
				mesh.chunkVisible("Oil", true);
	}

	protected void reflectPlaneToModel() {
		HierMesh hiermesh = aircraft().hierMesh();
		mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
		mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
		mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
		if (ac.blisterRemoved) {
			mesh.chunkVisible("CanopyL1", false);
			mesh.chunkVisible("CanopyL2", false);
			mesh.chunkVisible("Z_sliding_window_L", false);
			doToggleUp(false);
		}
	}

	public void doToggleUp(boolean flag) {
		if (ac.tiltCanopyOpened)
			super.doToggleUp(flag);
	}

	private void prepareToEnter() {
		if (hasRevi) {
			enter();
			return;
		}
		HookPilot hookpilot = HookPilot.current;
		if (hookpilot.isPadlock())
			hookpilot.stopPadlock();
		hookpilot.doAim(true);
		hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
		enteringAim = true;
	}

	private void enter() {
		HookPilot hookpilot = HookPilot.current;
		hookpilot.doAim(true);
		bEntered = true;
		if (!hasRevi) {
			hookpilot.setSimpleUse(true);
			doSetSimpleUse(true);
			HotKeyEnv.enable("PanView", false);
			HotKeyEnv.enable("SnapView", false);
		}
	}

	public void doSetSimpleUse(boolean flag) {
		super.doSetSimpleUse(flag);
		if (flag) {
			saveFov = Main3D.FOVX;
			CmdEnv.top().exec("fov 31");
			Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
			mesh.chunkVisible("superretic", true);
			mesh.chunkVisible("Z_sight_cap_big", true);
		}
	}

	private void leave(boolean flag) {
		if (enteringAim) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(false);
			hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
			return;
		}
		if (bEntered) {
			HookPilot hookpilot1 = HookPilot.current;
			hookpilot1.doAim(false);
			if (flag)
				bEntered = false;
			if (!hasRevi) {
				Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
				CmdEnv.top().exec("fov " + saveFov);
				hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
				hookpilot1.setSimpleUse(false);
				doSetSimpleUse(false);
				boolean flag1 = HotKeyEnv.isEnabled("aircraftView");
				HotKeyEnv.enable("PanView", flag1);
				HotKeyEnv.enable("SnapView", flag1);
				mesh.chunkVisible("superretic", false);
				mesh.chunkVisible("Z_sight_cap_big", false);
			}
		}
	}

	public void destroy() {
		leave(false);
		super.destroy();
	}

	public void doToggleAim(boolean flag) {
		if (isFocused() && isToggleAim() != flag)
			if (flag)
				prepareToEnter();
			else
				leave(true);
	}

	public void toggleDim() {
		cockpitDimControl = !cockpitDimControl;
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
		mesh.materialReplace("Gloss1D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
		mesh.materialReplace("Gloss1D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
		mesh.materialReplace("Gloss2D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
		mesh.materialReplace("Gloss2D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
		mesh.materialReplace("Gloss2D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
		mesh.materialReplace("Matt2D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
		mesh.materialReplace("Matt2D2o", mat);
	}

	protected boolean doFocusEnter() {
		aircraft().hierMesh().chunkVisible("Tail1_D" + aircraft().chunkDamageVisible("Tail1"), false);
		if (super.doFocusEnter()) {
			if (hasRevi && bEntered)
				enter();
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(false);
			if (!hasRevi) {
				Point3d point3d = new Point3d();
				point3d.set(0.2800000011920929D, 0.0D, 0.0D);
				hookpilot.setTubeSight(point3d);
			} else {
				hookpilot.setAim(reviAimPoint);
			}
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("Tail1_D" + aircraft().chunkDamageVisible("Tail1"), true);
		if (isFocused()) {
			leave(false);
			super.doFocusLeave();
		}
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl) {
			light1.light.setEmit(0.005F, 0.2F);
			light2.light.setEmit(0.005F, 0.2F);
			light3.light.setEmit(0.005F, 0.2F);
			light4.light.setEmit(0.002F, 0.1F);
			light5.light.setEmit(0.005F, 0.2F);
			setNightMats(true);
		} else {
			light1.light.setEmit(0.0F, 0.0F);
			light2.light.setEmit(0.0F, 0.0F);
			light3.light.setEmit(0.0F, 0.0F);
			light4.light.setEmit(0.0F, 0.0F);
			light5.light.setEmit(0.0F, 0.0F);
			setNightMats(false);
		}
	}

	private void initCompass() {
		accel = new Vector3d();
		compassSpeed = new Vector3d[4];
		compassSpeed[0] = new Vector3d(0.0D, 0.0D, 0.0D);
		compassSpeed[1] = new Vector3d(0.0D, 0.0D, 0.0D);
		compassSpeed[2] = new Vector3d(0.0D, 0.0D, 0.0D);
		compassSpeed[3] = new Vector3d(0.0D, 0.0D, 0.0D);
		float af[] = { 87F, 77.5F, 65.3F, 41.5F, -0.3F, -43.5F, -62.9F, -64F, -66.3F, -75.8F };
		float af1[] = { 55.8F, 51.5F, 47F, 40.1F, 33.8F, 33.7F, 32.7F, 35.1F, 46.6F, 61F };
		float f = cvt(Engine.land().config.declin, -90F, 90F, 9F, 0.0F);
		float f1 = floatindex(f, af);
		compassNorth = new Vector3d(0.0D, Math.cos(0.017452777777777779D * (double) f1), -Math.sin(0.017452777777777779D * (double) f1));
		compassSouth = new Vector3d(0.0D, -Math.cos(0.017452777777777779D * (double) f1), Math.sin(0.017452777777777779D * (double) f1));
		float f2 = floatindex(f, af1);
		compassNorth.scale((f2 / 600F) * Time.tickLenFs());
		compassSouth.scale((f2 / 600F) * Time.tickLenFs());
		segLen1 = 2D * Math.sqrt(1.0D - compassZ * compassZ);
		segLen2 = segLen1 / Math.sqrt(2D);
		compassLimit = -1D * Math.sin(0.01745328888888889D * compassLimitAngle);
		compassLimit *= compassLimit;
		compassAcc = 4.6666666599999997D * (double) Time.tickLenFs();
		compassSc = 0.10193679899999999D / (double) Time.tickLenFs() / (double) Time.tickLenFs();
	}

	private void updateCompass() {
		if (compassFirst == 0) {
			initCompass();
			fm.getLoc(setOld.planeLoc);
		}
		fm.getLoc(setNew.planeLoc);
		setNew.planeMove.set(setNew.planeLoc);
		setNew.planeMove.sub(setOld.planeLoc);
		accel.set(setNew.planeMove);
		accel.sub(setOld.planeMove);
		accel.scale(compassSc);
		accel.x = -accel.x;
		accel.y = -accel.y;
		accel.z = -accel.z - 1.0D;
		accel.scale(compassAcc);
		if (accel.length() > -compassZ * 0.69999999999999996D)
			accel.scale((-compassZ * 0.69999999999999996D) / accel.length());
		for (int i = 0; i < 4; i++) {
			compassSpeed[i].set(setOld.compassPoint[i]);
			compassSpeed[i].sub(setNew.compassPoint[i]);
		}

		for (int j = 0; j < 4; j++) {
			double d = compassSpeed[j].length();
			d = 0.98499999999999999D / (1.0D + d * d * 15D);
			compassSpeed[j].scale(d);
		}

		Vector3d vector3d = new Vector3d();
		vector3d.set(setOld.compassPoint[0]);
		vector3d.add(setOld.compassPoint[1]);
		vector3d.add(setOld.compassPoint[2]);
		vector3d.add(setOld.compassPoint[3]);
		vector3d.normalize();
		for (int k = 0; k < 4; k++) {
			Vector3d vector3d1 = new Vector3d();
			double d1 = vector3d.dot(compassSpeed[k]);
			vector3d1.set(vector3d);
			d1 *= 0.28000000000000003D;
			vector3d1.scale(-d1);
			compassSpeed[k].add(vector3d1);
		}

		for (int l = 0; l < 4; l++)
			compassSpeed[l].add(accel);

		compassSpeed[0].add(compassNorth);
		compassSpeed[2].add(compassSouth);
		for (int i1 = 0; i1 < 4; i1++) {
			setNew.compassPoint[i1].set(setOld.compassPoint[i1]);
			setNew.compassPoint[i1].add(compassSpeed[i1]);
		}

		vector3d.set(setNew.compassPoint[0]);
		vector3d.add(setNew.compassPoint[1]);
		vector3d.add(setNew.compassPoint[2]);
		vector3d.add(setNew.compassPoint[3]);
		vector3d.scale(0.25D);
		Vector3d vector3d2 = new Vector3d(vector3d);
		vector3d2.normalize();
		vector3d2.scale(-compassZ);
		vector3d2.sub(vector3d);
		for (int j1 = 0; j1 < 4; j1++)
			setNew.compassPoint[j1].add(vector3d2);

		for (int k1 = 0; k1 < 4; k1++)
			setNew.compassPoint[k1].normalize();

		for (int l1 = 0; l1 < 2; l1++) {
			compassDist(setNew.compassPoint[0], setNew.compassPoint[2], segLen1);
			compassDist(setNew.compassPoint[1], setNew.compassPoint[3], segLen1);
			compassDist(setNew.compassPoint[0], setNew.compassPoint[1], segLen2);
			compassDist(setNew.compassPoint[2], setNew.compassPoint[3], segLen2);
			compassDist(setNew.compassPoint[1], setNew.compassPoint[2], segLen2);
			compassDist(setNew.compassPoint[3], setNew.compassPoint[0], segLen2);
			for (int i2 = 0; i2 < 4; i2++)
				setNew.compassPoint[i2].normalize();

			compassDist(setNew.compassPoint[3], setNew.compassPoint[0], segLen2);
			compassDist(setNew.compassPoint[1], setNew.compassPoint[2], segLen2);
			compassDist(setNew.compassPoint[2], setNew.compassPoint[3], segLen2);
			compassDist(setNew.compassPoint[0], setNew.compassPoint[1], segLen2);
			compassDist(setNew.compassPoint[1], setNew.compassPoint[3], segLen1);
			compassDist(setNew.compassPoint[0], setNew.compassPoint[2], segLen1);
			for (int j2 = 0; j2 < 4; j2++)
				setNew.compassPoint[j2].normalize();

		}

		Orientation orientation = new Orientation();
		fm.getOrient(orientation);
		for (int k2 = 0; k2 < 4; k2++) {
			setNew.cP[k2].set(setNew.compassPoint[k2]);
			orientation.transformInv(setNew.cP[k2]);
		}

		Vector3d vector3d3 = new Vector3d();
		vector3d3.set(setNew.cP[0]);
		vector3d3.add(setNew.cP[1]);
		vector3d3.add(setNew.cP[2]);
		vector3d3.add(setNew.cP[3]);
		vector3d3.scale(0.25D);
		Vector3d vector3d4 = new Vector3d();
		vector3d4.set(vector3d3);
		vector3d4.normalize();
		float f = (float) (vector3d4.x * vector3d4.x + vector3d4.y * vector3d4.y);
		if ((double) f > compassLimit || vector3d3.z > 0.0D) {
			for (int l2 = 0; l2 < 4; l2++) {
				setNew.cP[l2].set(setOld.cP[l2]);
				setNew.compassPoint[l2].set(setOld.cP[l2]);
				orientation.transform(setNew.compassPoint[l2]);
			}

			vector3d3.set(setNew.cP[0]);
			vector3d3.add(setNew.cP[1]);
			vector3d3.add(setNew.cP[2]);
			vector3d3.add(setNew.cP[3]);
			vector3d3.scale(0.25D);
		}
		vector3d4.set(setNew.cP[0]);
		vector3d4.sub(vector3d3);
		double d2 = -Math.atan2(vector3d3.y, -vector3d3.z);
		vectorRot2(vector3d3, d2);
		vectorRot2(vector3d4, d2);
		double d3 = Math.atan2(vector3d3.x, -vector3d3.z);
		vectorRot1(vector3d4, -d3);
		double d4 = Math.atan2(vector3d4.y, vector3d4.x);
		mesh.chunkSetAngles("Z_Need_compassBase", -(float) ((d2 * 180D) / 3.1415926000000001D), -(float) ((d3 * 180D) / 3.1415926000000001D), 0.0F);
		mesh.chunkSetAngles("Z_Need_compass", 0.0F, (float) (90D - (d4 * 180D) / 3.1415926000000001D), 0.0F);
		compassFirst++;
	}

	private void vectorRot1(Vector3d vector3d, double d) {
		double d1 = Math.sin(d);
		double d2 = Math.cos(d);
		double d3 = vector3d.x * d2 - vector3d.z * d1;
		vector3d.z = vector3d.x * d1 + vector3d.z * d2;
		vector3d.x = d3;
	}

	private void vectorRot2(Vector3d vector3d, double d) {
		double d1 = Math.sin(d);
		double d2 = Math.cos(d);
		double d3 = vector3d.y * d2 - vector3d.z * d1;
		vector3d.z = vector3d.y * d1 + vector3d.z * d2;
		vector3d.y = d3;
	}

	private void compassDist(Vector3d vector3d, Vector3d vector3d1, double d) {
		Vector3d vector3d2 = new Vector3d();
		vector3d2.set(vector3d);
		vector3d2.sub(vector3d1);
		double d1 = vector3d2.length();
		if (d1 < 9.9999999999999995E-007D)
			d1 = 9.9999999999999995E-007D;
		d1 = (d - d1) / d1 / 2D;
		vector3d2.scale(d1);
		vector3d.add(vector3d2);
		vector3d1.sub(vector3d2);
	}

	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	private Vector3f w;
	private boolean bNeedSetUp;
	private float pictAiler;
	private float pictElev;
	private float pictSupc;
	private float flaps;
	private float pictManifold;
	private boolean bEntered;
	private float saveFov;
	private boolean hasRevi;
	private float tailWheelLock;
	private int flapsDirection;
	private float flapsPump;
	private float flapsPumpIncrement;
	private LightPointActor light1;
	private LightPointActor light2;
	private LightPointActor light3;
	private LightPointActor light4;
	private LightPointActor light5;
	protected DXXI ac;
	private float rpmGeneratedPressure;
	private float oilPressure;
	private static double compassZ = -0.20000000000000001D;
	private double segLen1;
	private double segLen2;
	private double compassLimit;
	private static double compassLimitAngle = 12D;
	private Vector3d compassSpeed[];
	int compassFirst;
	private Vector3d accel;
	private Vector3d compassNorth;
	private Vector3d compassSouth;
	private double compassAcc;
	private double compassSc;
	private final Point3d reviAimPoint = new Point3d(-0.87D, 0.0028D, 0.80279999999999996D);
	private boolean enteringAim;

	static {
		Property.set(CLASS.THIS(), "normZN", 1.1F);
	}

}
