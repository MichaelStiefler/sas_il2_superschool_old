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
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitRE_2002 extends CockpitPilot {
	private class Variables {

		float altimeter;
		float throttle;
		float mix;
		float prop;
		float turn;
		float vspeed;
		float stbyPosition;
		Point3d planeLoc;
		Point3d planeMove;
		Vector3d compassPoint[];
		Vector3d cP[];

		private Variables() {
			planeLoc = new Point3d();
			planeMove = new Point3d();
			compassPoint = new Vector3d[4];
			cP = new Vector3d[4];
			compassPoint[0] = new Vector3d(0.0D, Math.sqrt(1.0D - CockpitRE_2002.compassZ * CockpitRE_2002.compassZ), CockpitRE_2002.compassZ);
			compassPoint[1] = new Vector3d(-Math.sqrt(1.0D - CockpitRE_2002.compassZ * CockpitRE_2002.compassZ), 0.0D, CockpitRE_2002.compassZ);
			compassPoint[2] = new Vector3d(0.0D, -Math.sqrt(1.0D - CockpitRE_2002.compassZ * CockpitRE_2002.compassZ), CockpitRE_2002.compassZ);
			compassPoint[3] = new Vector3d(Math.sqrt(1.0D - CockpitRE_2002.compassZ * CockpitRE_2002.compassZ), 0.0D, CockpitRE_2002.compassZ);
			cP[0] = new Vector3d(0.0D, Math.sqrt(1.0D - CockpitRE_2002.compassZ * CockpitRE_2002.compassZ), CockpitRE_2002.compassZ);
			cP[1] = new Vector3d(-Math.sqrt(1.0D - CockpitRE_2002.compassZ * CockpitRE_2002.compassZ), 0.0D, CockpitRE_2002.compassZ);
			cP[2] = new Vector3d(0.0D, -Math.sqrt(1.0D - CockpitRE_2002.compassZ * CockpitRE_2002.compassZ), CockpitRE_2002.compassZ);
			cP[3] = new Vector3d(Math.sqrt(1.0D - CockpitRE_2002.compassZ * CockpitRE_2002.compassZ), 0.0D, CockpitRE_2002.compassZ);
		}

	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (bNeedSetUp) {
				reflectPlaneMats();
				bNeedSetUp = false;
			}
			setTmp = setOld;
			setOld = setNew;
			setNew = setTmp;
			if ((fm.AS.astateCockpitState & 2) != 0 && setNew.stbyPosition < 1.0F) {
				delay--;
				if (delay <= 0) {
					setNew.stbyPosition = setOld.stbyPosition + 0.03F;
					setOld.stbyPosition = setNew.stbyPosition;
					sightDamaged = true;
				}
			}
			if (fm.CT.getRadiator() < currentRadiator)
				radiatorDelta = -1;
			else if (fm.CT.getRadiator() > currentRadiator)
				radiatorDelta = 1;
			if (radiator > 1.0F || radiator < -1F)
				radiatorDelta = 0;
			if (radiatorDelta != 0)
				radiator = radiator + (float) radiatorDelta * 0.2F;
			else if (currentRadiator == fm.CT.getRadiator())
				radiator = radiator * 0.5F;
			currentRadiator = fm.CT.getRadiator();
			setNew.altimeter = fm.getAltitude();
			setNew.throttle = (10F * setOld.throttle + fm.EI.engines[0].getControlThrottle()) / 11F;
			setNew.mix = (10F * setOld.mix + fm.EI.engines[0].getControlMix()) / 11F;
			setNew.prop = (10F * setOld.prop + fm.EI.engines[0].getControlProp()) / 11F;
			pictGear = 0.85F * pictGear + 0.15F * fm.CT.GearControl;
			w.set(fm.getW());
			fm.Or.transform(w);
			setNew.turn = (12F * setOld.turn + w.z) / 13F;
			setNew.vspeed = (299F * setOld.vspeed + fm.getVertSpeed()) / 300F;
			if (flaps == fm.CT.getFlap()) {
				flapsDirection = 0;
				sfxStop(16);
			} else if (flaps < fm.CT.getFlap()) {
				if (flapsDirection == 0)
					sfxStart(16);
				flaps = fm.CT.getFlap();
				flapsDirection = 1;
				flapsLeverAngle = flapsLeverAngle + flapsIncrement;
			} else if (flaps > fm.CT.getFlap()) {
				if (flapsDirection == 0)
					sfxStart(16);
				flaps = fm.CT.getFlap();
				flapsDirection = -1;
				flapsLeverAngle = flapsLeverAngle - flapsIncrement;
			}
			if (!fm.Gears.bTailwheelLocked && tailWheelLock < 1.0F)
				tailWheelLock = tailWheelLock + 0.1F;
			else if (fm.Gears.bTailwheelLocked && tailWheelLock > 0.0F)
				tailWheelLock = tailWheelLock - 0.1F;
			updateCompass();
			return true;
		}

		Interpolater() {
		}
	}

	public CockpitRE_2002() {
		super("3DO/Cockpit/Re2000/Re2002.him", "bf109");
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		bNeedSetUp = true;
		pictAiler = 0.0F;
		pictElev = 0.0F;
		flaps = 0.0F;
		pictGear = 0.0F;
		tailWheelLock = 1.0F;
		flapsDirection = 0;
		flapsLeverAngle = 0.0F;
		flapsIncrement = 10F;
		rpmGeneratedPressure = 0.0F;
		oilPressure = 0.0F;
		radiator = 0.0F;
		currentRadiator = 0.0F;
		radiatorDelta = 0;
		sightDamaged = false;
		compassFirst = 0;
		delay = 80;
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
		cockpitNightMats = (new String[] { "D_strum1", "D_strum2", "D_strum3", "D_strum4", "equip01", "equip04", "equip05", "gunsight", "panel1", "stick", "strum1", "strum2", "strum3", "strum4" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		if (acoustics != null)
			acoustics.globFX = new ReverbFXRoom(0.45F);
		limits6DoF = (new float[] { 0.7F, 0.055F, -0.07F, 0.11F, 0.15F, -0.11F, 0.025F, -0.045F });
	}

	public void reflectWorldToInstruments(float f) {
		float f1 = 0.0F;
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
		f1 = pictManifold = 0.85F * pictManifold + 0.15F * fm.EI.engines[0].getManifoldPressure() * 76F;
		if (f1 < 76F)
			mesh.chunkSetAngles("Z_need_airpress", -cvt(f1, 40F, 76F, 12F, 210F), 0.0F, 0.0F);
		else
			mesh.chunkSetAngles("Z_need_airpress", -cvt(f1, 76F, 100F, 210F, 328F), 0.0F, 0.0F);
		f1 = -15F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl);
		mesh.chunkSetAngles("Z_stick03", f1, 0.0F, 0.0F);
		f1 = -14F * (pictElev = 0.85F * pictElev + 0.2F * fm.CT.ElevatorControl);
		mesh.chunkSetAngles("Z_stick01", f1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_stick02", 0.0F, 0.0F, f1);
		mesh.chunkSetAngles("Z_gunsight_rim", 49.3F * setNew.stbyPosition, 0.0F, 0.0F);
		f1 = fm.CT.getRudder();
		mesh.chunkSetAngles("Z_rudder_01", -15F * f1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_rudder_02", 15F * f1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_rudder_03", 15F * f1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_rudder_04", -15F * f1, 0.0F, 0.0F);
		f1 = interp(setNew.throttle, setOld.throttle, f);
		mesh.chunkSetAngles("Z_throttle_lvr", cvt(f1, 0.0F, 1.0F, 0.0F, 50F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_wep_lvr", cvt(f1, 1.0F, 1.1F, 0.0F, 40F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_mix_lvr", 40F * interp(setNew.mix, setOld.mix, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_pitch_lvr", 45F * interp(setNew.prop, setOld.prop, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_need_alt_01", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, -3600F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_need_alt_02", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, -360F), 0.0F, 0.0F);
		float f3 = Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH());
		if (f3 < 100F)
			f1 = cvt(f3, 0.0F, 100F, 0.0F, 41F);
		else if (f3 < 200F)
			f1 = cvt(f3, 100F, 200F, 41F, 110F);
		else if (f3 < 300F)
			f1 = cvt(f3, 200F, 300F, 110F, 144F);
		else if (f3 < 400F)
			f1 = cvt(f3, 300F, 400F, 144F, 212F);
		else if (f3 < 500F)
			f1 = cvt(f3, 400F, 500F, 212F, 292F);
		else
			f1 = cvt(f3, 500F, 550F, 292F, 328F);
		mesh.chunkSetAngles("Z_need_speed_02", -f1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_need_speed_01", -f1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_need_clock01", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, -720F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_need_clock02", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_need_oiltemp_01", -floatindex(cvt(fm.EI.engines[0].tOilOut, 30F, 150F, 0.0F, 12F), oilTempScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_need_oiltemp_02", -floatindex(cvt(fm.EI.engines[0].tOilIn, 30F, 150F, 0.0F, 12F), oilTempScale), 0.0F, 0.0F);
		f1 = fm.EI.engines[0].getRPM();
		mesh.chunkSetAngles("Z_need_RPM", cvt(f1, 0.0F, 4000F, 0.0F, -322F), 0.0F, 0.0F);
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
		float f4 = 0.0F;
		if (fm.EI.engines[0].tOilOut > 90F)
			f4 = cvt(fm.EI.engines[0].tOilOut, 90F, 110F, 1.1F, 1.5F);
		else if (fm.EI.engines[0].tOilOut < 50F)
			f4 = cvt(fm.EI.engines[0].tOilOut, 0.0F, 50F, 2.0F, 0.9F);
		else
			f4 = cvt(fm.EI.engines[0].tOilOut, 50F, 90F, 0.9F, 1.1F);
		float f5 = f4 * fm.EI.engines[0].getReadyness() * oilPressure;
		mesh.chunkSetAngles("Z_need_oilpress", cvt(f5, 0.0F, 15F, 0.0F, -300F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_need_cyltemp", cvt(fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, -77F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_need_fuelpress", cvt(rpmGeneratedPressure, 0.0F, 1800F, 0.0F, -200F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_magneto_switch", -38F * (float) fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_need_turnbank01", cvt(setNew.turn, -0.2F, 0.2F, -20F, 20F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_need_turnbank02", cvt(getBall(8D), -8F, 8F, 7.5F, -7.5F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_need_climb", cvt(setNew.vspeed, -25F, 25F, 180F, -180F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_fuel_01", cvt(fm.M.fuel, fm.M.maxFuel * 0.28F, fm.M.maxFuel, 0.0F, 210F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_fuel_02", cvt(fm.M.fuel, 0.0F, fm.M.maxFuel * 0.28F, 0.0F, 180F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_flaps_indicator", 146F * fm.CT.getFlap(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_elev_trim", 360F * fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_elev_indicator", -180F * fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_rudder_trim", -180F * fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[2] = -0.03F * tailWheelLock;
		mesh.chunkSetLocate("Z_tailwheel_lock", Cockpit.xyz, Cockpit.ypr);
		if (fm.CT.bHasBrakeControl) {
			float f2 = fm.CT.getBrake();
			mesh.chunkSetAngles("Z_need_brakes_airpress02", -cvt(f2 + f2 * fm.CT.getRudder(), 0.0F, 1.5F, 0.0F, 135F), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_need_brakes_airpress01", cvt(f2 - f2 * fm.CT.getRudder(), 0.0F, 1.5F, 0.0F, 135F), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_need_brakes_airpress03", -150F + f2 * 20F, 0.0F, 0.0F);
			resetYPRmodifier();
			Cockpit.xyz[2] = -0.01F * f2;
			mesh.chunkSetLocate("Z_brakes", Cockpit.xyz, Cockpit.ypr);
		}
		if (fm.EI.engines[0].getControlFeather() == 1)
			mesh.chunkSetAngles("Z_pitch_vario", -66F, 0.0F, 0.0F);
		else if (fm.CT.getStepControlAuto(0))
			mesh.chunkSetAngles("Z_pitch_vario", 66F, 0.0F, 0.0F);
		else
			mesh.chunkSetAngles("Z_pitch_vario", 0.0F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_gear_lvr", 78F * pictGear, 0.0F, 0.0F);
		if (fm.Gears.lgear)
			mesh.chunkSetAngles("Z_gear01", 90F - 90F * fm.CT.getGearL(), 0.0F, 0.0F);
		if (fm.Gears.rgear)
			mesh.chunkSetAngles("Z_gear02", -90F + 90F * fm.CT.getGearR(), 0.0F, 0.0F);
		mesh.chunkVisible("Z_gearlamp01", fm.CT.getGearL() == 0.0F && fm.Gears.lgear);
		mesh.chunkVisible("Z_gearlamp02", fm.CT.getGearR() == 0.0F && fm.Gears.rgear);
		mesh.chunkVisible("Z_gearlamp03", fm.CT.getGearL() == 1.0F && fm.Gears.lgear);
		mesh.chunkVisible("Z_gearlamp04", fm.CT.getGearR() == 1.0F && fm.Gears.rgear);
		mesh.chunkSetAngles("Z_flaps", -flapsLeverAngle, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_radiatorflaps", radiator * -8F, 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.ypr[1] = fm.CT.getCockpitDoor() * 90.0F;
		double dPosIndex = (double) fm.CT.getCockpitDoor();
		double dOffset = Math.PI / 5.2D;
		Cockpit.xyz[0] = ((float) Math.cos(dPosIndex * Math.PI / 2.0D + dOffset) - (float) Math.cos(dOffset)) * 0.4F;
		Cockpit.xyz[2] = ((float) Math.sin(dPosIndex * Math.PI / 2.0D + dOffset) - (float) Math.sin(dOffset)) * 0.4F;
		mesh.chunkSetLocate("Z_sliding_cnp", Cockpit.xyz, Cockpit.ypr);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 2) != 0) {
			mesh.chunkVisible("gunsight_lense", false);
			mesh.chunkVisible("D_gunsight_lense", true);
			mesh.chunkVisible("reticle", false);
			mesh.chunkVisible("reticlemask", false);
		}
		if ((fm.AS.astateCockpitState & 1) != 0)
			mesh.chunkVisible("XGlassHoles_04", true);
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("Gauges01", false);
			mesh.chunkVisible("D_Gauges01", true);
			mesh.chunkVisible("Z_need_clock01", false);
			mesh.chunkVisible("Z_need_clock02", false);
			mesh.chunkVisible("Z_need_alt_01", false);
			mesh.chunkVisible("Z_need_alt_02", false);
			mesh.chunkVisible("Z_need_climb", false);
			mesh.chunkVisible("Z_need_speed_01", false);
		}
		if ((fm.AS.astateCockpitState & 4) != 0) {
			mesh.chunkVisible("XGlassHoles_05", true);
			mesh.chunkVisible("XGlassHoles_03", true);
		}
		if ((fm.AS.astateCockpitState & 8) != 0) {
			mesh.chunkVisible("XHoles_01", true);
			mesh.chunkVisible("Gauges02", false);
			mesh.chunkVisible("D_Gauges02", true);
			mesh.chunkVisible("Z_need_speed_02", false);
			mesh.chunkVisible("Z_need_brakes_airpress03", false);
			mesh.chunkVisible("Z_need_brakes_airpress01", false);
			mesh.chunkVisible("Z_need_brakes_airpress02", false);
			mesh.chunkVisible("Z_need_airpress", false);
			mesh.chunkVisible("Z_need_fuelpress", false);
		}
		if ((fm.AS.astateCockpitState & 0x10) != 0) {
			mesh.chunkVisible("XHoles_02", true);
			mesh.chunkVisible("Gauges03", false);
			mesh.chunkVisible("D_Gauges03", true);
			mesh.chunkVisible("Z_need_oiltemp_02", false);
			mesh.chunkVisible("Z_need_oiltemp_01", false);
			mesh.chunkVisible("Z_need_oilpress", false);
			mesh.chunkVisible("Z_need_RPM", false);
			mesh.chunkVisible("Z_need_turnbank01", false);
			mesh.chunkVisible("Z_need_turnbank02", false);
		}
		if ((fm.AS.astateCockpitState & 0x20) != 0) {
			mesh.chunkVisible("XGlassHoles_01", true);
			mesh.chunkVisible("XGlassHoles_02", true);
		}
		if ((fm.AS.astateCockpitState & 0x80) != 0)
			mesh.chunkVisible("ZOil", true);
	}

	protected void reflectPlaneToModel() {
	}

	public void doToggleAim(boolean flag) {
		super.doToggleAim(flag);
		if (flag && sightDamaged) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(true);
			hookpilot.setAim(new Point3d(-0.5D, -0.0177800003439188D, 0.89499998092651367D));
		}
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl) {
			light1.light.setEmit(0.003F, 0.6F);
			light2.light.setEmit(0.003F, 0.6F);
			setNightMats(true);
		} else {
			light1.light.setEmit(0.0F, 0.0F);
			light2.light.setEmit(0.0F, 0.0F);
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
			Vector3d vector3d2 = new Vector3d();
			double d1 = vector3d.dot(compassSpeed[k]);
			vector3d2.set(vector3d);
			d1 *= 0.28000000000000003D;
			vector3d2.scale(-d1);
			compassSpeed[k].add(vector3d2);
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
		Vector3d vector3d1 = new Vector3d(vector3d);
		vector3d1.normalize();
		vector3d1.scale(-compassZ);
		vector3d1.sub(vector3d);
		for (int j1 = 0; j1 < 4; j1++)
			setNew.compassPoint[j1].add(vector3d1);

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
		mesh.chunkSetAngles("compass_base", 0.0F, -(float) ((d2 * 180D) / 3.1415926000000001D), (float) ((d3 * 180D) / 3.1415926000000001D));
		mesh.chunkSetAngles("compass_header", -(float) (90D - (d4 * 180D) / 3.1415926000000001D), 0.0F, 0.0F);
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
	private float flaps;
	private float pictManifold;
	private float pictGear;
	private float tailWheelLock;
	private int flapsDirection;
	private float flapsLeverAngle;
	private float flapsIncrement;
	private LightPointActor light1;
	private LightPointActor light2;
	private float rpmGeneratedPressure;
	private float oilPressure;
	private static final float oilTempScale[] = { 0.0F, 10.4F, 20.5F, 31F, 45F, 59F, 79F, 98F, 124F, 153F, 191F, 238F, 297F };
	private float radiator;
	private float currentRadiator;
	private int radiatorDelta;
	private boolean sightDamaged;
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
	private int delay;

}
