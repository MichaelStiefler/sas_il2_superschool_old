package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.BaseGameVersion;
import com.maddox.sound.ReverbFXRoom;

public class CockpitP_35 extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (fm != null) {
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				setNew.throttle = (10F * setOld.throttle + fm.CT.PowerControl) / 11F;
				setNew.prop = (8F * setOld.prop + fm.CT.getStepControl()) / 9F;
				setNew.altimeter = fm.getAltitude();
				setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut() - 90F);
				if (BaseGameVersion.is412orLater()) {
					setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth.getDeg(1.0F)) + World.rnd().nextFloat(-30F, 30F)) / 11F;
				} else {
					setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth.getDeg(1.0F)) + World.cur().rnd.nextFloat(-30F, 30F)) / 11F;
				}
				setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
				float f = 30F;
				if (flapsLever != 0.0F && flaps == fm.CT.getFlap()) {
					flapsLever = flapsLever * 0.8F;
					if (Math.abs(flapsLever) < 0.1F)
						flapsLever = 0.0F;
				} else if (flaps < fm.CT.getFlap()) {
					flaps = fm.CT.getFlap();
					flapsLever = flapsLever + 2.0F;
					if (flapsLever > f)
						flapsLever = f;
				} else if (flaps > fm.CT.getFlap()) {
					flaps = fm.CT.getFlap();
					flapsLever = flapsLever - 2.0F;
					if (flapsLever < -f)
						flapsLever = -f;
				}
				f = 14.5F;
				if (gearsLever != 0.0F && gears == fm.CT.getGear()) {
					gearsLever = gearsLever * 0.8F;
					if (Math.abs(gearsLever) < 0.1F)
						gearsLever = 0.0F;
				} else if (gears < fm.CT.getGear()) {
					gears = fm.CT.getGear();
					gearsLever = gearsLever + 2.0F;
					if (gearsLever > f)
						gearsLever = f;
				} else if (gears > fm.CT.getGear()) {
					gears = fm.CT.getGear();
					gearsLever = gearsLever - 2.0F;
					if (gearsLever < -f)
						gearsLever = -f;
				}
				fuelPerct = (fm.M.fuel / fm.M.maxFuel) * 100F;
				byte byte0;
				float f1;
				if (fuelPerct > (float) fuelTankLevels[0]) {
					if (fuelPerct > (float) fuelTankLevels[1])
						byte0 = 2;
					else
						byte0 = 1;
					f1 = fuelPerct - (float) fuelTankLevels[byte0 - 1];
				} else {
					byte0 = 0;
					f1 = fuelPerct;
				}
				float f2 = 17F;
				if (byte0 != 0) {
					if (f1 < 7F)
						f2 = f1 + 10F;
				} else if (f1 < 17F)
					f2 = f1;
				if (fuelSwitchAngle > fuelSwitchAngles[byte0]) {
					fuelSwitchAngle -= 4;
					f2 = 0.0F;
				}
				if (fuelSwitchAngle < fuelSwitchAngles[byte0]) {
					fuelSwitchAngle += 4;
					f2 = 0.0F;
				}
				setNew.fuelPSI = (15F * setOld.fuelPSI + f2) / 16F;
			}
			if (gearsLever != 0.0F || flapsLever != 0.0F) {
				amps[1] = 60F;
				amps[2] = 0.6F;
			} else {
				if (amps[0] < 12F)
					amps[2] = 0.02F;
				if (fm.EI.engines[0].getStage() != 0)
					amps[1] = 10F;
				else
					amps[1] = 0.0F;
			}
			if (amps[0] != amps[1]) {
				if (amps[0] > amps[1]) {
					amps[0] -= amps[2];
					if (amps[0] < amps[1])
						amps[0] = amps[1];
				} else {
					amps[0] += amps[2];
					if (amps[0] > amps[1])
						amps[0] = amps[1];
				}
				if ((fm.AS.astateCockpitState & 2) != 0)
					setNew.reviPos = (9F * setOld.reviPos + 1.0F) / 10F;
			}
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float throttle;
		float prop;
		float altimeter;
		AnglesFork azimuth;
		float vspeed;
		float waypointAzimuth;
		float fuelPSI;
		float reviPos;

		private Variables() {
			azimuth = new AnglesFork();
		}

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

	public CockpitP_35() {
		super("3DO/Cockpit/Hawk-75A3/hier.him", "bf109");
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		bNeedSetUp = true;
		reversedThrottle = false;
		leftGun = null;
		rightGun = null;
		flapsLever = 0.0F;
		flaps = 0.0F;
		gearsLever = 0.0F;
		gears = 0.0F;
		fuelSwitchAngle = 0;
		fuelPerct = 100F;
		tmpP = new Point3d();
		tmpV = new Vector3d();
		cockpitNightMats = (new String[] { "Arrows_Segment", "Indicators_2", "Indicators_2_Dam", "Indicators_3", "Indicators_3_Dam", "Indicators_4", "Indicators_4_Dam", "Indicators_5", "Indicators_5_Dam", "Indicators_6", "Indicators_7", "Plastics" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		if (acoustics != null)
			acoustics.globFX = new ReverbFXRoom(0.45F);
		Aircraft aircraft = aircraft();
		try {
			if (!(aircraft.getGunByHookName("_MGUN01") instanceof GunEmpty)) {
				leftGun = aircraft.getGunByHookName("_MGUN02");
				rightGun = aircraft.getGunByHookName("_MGUN01");
			}
		} catch (Exception exception) {
		}
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
		resetYPRmodifier();
		xyz[1] = cvt(fm.CT.getCockpitDoor(), 0.1F, 0.9F, 0.0F, 0.7F);
		mesh.chunkSetLocate("Z_canopy", xyz, ypr);
		if ((double) fm.CT.getCockpitDoor() > 0.5D)
			mesh.chunkSetAngles("Z_canopy_lever", 0.0F, -cvt(fm.CT.getCockpitDoor(), 0.9F, 1.0F, 7F, 0.0F), 0.0F);
		else
			mesh.chunkSetAngles("Z_canopy_lever", 0.0F, -cvt(fm.CT.getCockpitDoor(), 0.0F, 0.1F, 0.0F, 7F), 0.0F);
		mesh.chunkSetAngles("Z_canopy_open_rod", xyz[1] * 2570F, 0.0F, 0.0F);
		w.set(fm.getW());
		fm.Or.transform(w);
		mesh.chunkSetAngles("Zairspeed", 0.0F, floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 20F), speedometerScale), 0.0F);
		mesh.chunkSetAngles("Zturn_and_bank", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 28F, -28F), 0.0F);
		mesh.chunkSetAngles("Zturn_and_bank2", 0.0F, cvt(getBall(7D), -7F, 7F, -13F, 13F), 0.0F);
		mesh.chunkSetAngles("Z_Ball", 0.0F, cvt(getBall(7D), -7F, 7F, -12F, 12F), 0.0F);
		mesh.chunkSetAngles("Zclimb", 0.0F, cvt(setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F);
		mesh.chunkSetAngles("Zclock_hour", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		mesh.chunkSetAngles("Zclock_minute", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("Zaltimeter_01", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 720F), 0.0F);
		mesh.chunkSetAngles("Zaltimeter_02", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 7200F), 0.0F);
		mesh.chunkSetAngles("ZTempOil", 0.0F, cvt(fm.EI.engines[0].tOilIn, 0.0F, 100F, 0.0F, 180F), 0.0F);
		mesh.chunkSetAngles("ZTempCyl", 0.0F, cvt(fm.EI.engines[0].tWaterOut, 0.0F, 400F, 0.0F, 62F), 0.0F);
		mesh.chunkSetAngles("Zmanifold_pressure", 0.0F, cvt(pictManifold = 0.85F * pictManifold + 0.15F * fm.EI.engines[0].getManifoldPressure() * 76F, 30F, 120F, 38F, 322.4F), 0.0F);
		float f1 = fm.EI.engines[0].getRPM();
		if (f1 > 600F)
			mesh.chunkSetAngles("Zrpm", 0.0F, cvt(fm.EI.engines[0].getRPM(), 600F, 3000F, 16.5F, 343.2F), 0.0F);
		else
			mesh.chunkSetAngles("Zrpm", 0.0F, cvt(fm.EI.engines[0].getRPM(), 0.0F, 600F, 0.0F, 16.5F), 0.0F);
		resetYPRmodifier();
		xyz[1] = interp(setNew.prop, setOld.prop, f);
		mesh.chunkSetAngles("Z_Prop_Pitch", 0.0F, 65F * xyz[1], 0.0F);
		xyz[1] = xyz[1] * 0.02F - 0.02F;
		mesh.chunkSetLocate("Z_Prop_Pitch_strut", xyz, ypr);
		xyz[1] = cvt(fm.EI.engines[0].getControlMix(), 0.0F, 1.2F, 0.0F, 1.0F);
		mesh.chunkSetAngles("Z_Mixture", 0.0F, 68F * xyz[1], 0.0F);
		xyz[1] = xyz[1] * 0.02F - 0.02F;
		mesh.chunkSetLocate("Z_Mixture_strut", xyz, ypr);
		if (reversedThrottle)
			xyz[1] = 1.0F - interp(setNew.throttle, setOld.throttle, f);
		else
			xyz[1] = interp(setNew.throttle, setOld.throttle, f);
		mesh.chunkSetAngles("Z_Throttle", 0.0F, cvt(xyz[1], 0.0F, 1.1F, -3F, 60F), 0.0F);
		xyz[1] = xyz[1] * 0.02F - 0.021F;
		mesh.chunkSetLocate("Z_Throttle_strut", xyz, ypr);
		mesh.chunkSetAngles("Z_Flaps_lever", 0.0F, flapsLever, 0.0F);
		mesh.chunkSetAngles("Z_gear_lever", 0.0F, gearsLever + 14.5F, 0.0F);
		mesh.chunkSetAngles("Z_Rudder_Trim", 0.0F, 722F * fm.CT.getTrimRudderControl(), 0.0F);
		mesh.chunkSetAngles("Z_Elevator_Trim", 0.0F, -722F * fm.CT.getTrimElevatorControl(), 0.0F);
		mesh.chunkSetAngles("Z_Aileron_Rod", 0.0F, 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 20F);
		mesh.chunkSetAngles("Z_Control_Column", 0.0F, -(pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 16F, 0.0F);
		mesh.chunkSetAngles("Z_Elevator_Rod", 0.0F, pictElev * 16F, 0.0F);
		mesh.chunkSetAngles("Z_CowlFlaps", 0.0F, 720F * fm.EI.engines[0].getControlRadiator(), 0.0F);
		mesh.chunkSetAngles("Z_Pedal_Frame_L", 0.0F, 12F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("Z_Pedal_Frame_R", 0.0F, -12F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("Z_Rudderwire_L", 0.0F, 0.0F, 12F * fm.CT.getRudder());
		mesh.chunkSetAngles("Z_Rudderwire_R", 0.0F, 0.0F, -12F * fm.CT.getRudder());
		if (leftGun != null) {
			int i = leftGun.countBullets();
			int k = (int) ((float) i * 0.01F);
			int i1 = (int) ((float) i * 0.1F) - 10 * k;
			int k1 = i - (k * 100 + i1 * 10);
			bullets[0] = 0.8F * bullets[0] + 0.2F * (float) k;
			bullets[1] = 0.8F * bullets[1] + 0.2F * (float) i1;
			bullets[2] = 0.8F * bullets[2] + 0.2F * (float) k1;
			mesh.chunkSetAngles("Z_Lammo00", 0.0F, -cvt(bullets[0], 0.0F, 9F, 0.0F, 324F), 0.0F);
			mesh.chunkSetAngles("Z_Lammo01", 0.0F, -cvt(bullets[1], 0.0F, 9F, 0.0F, 324F), 0.0F);
			mesh.chunkSetAngles("Z_Lammo02", 0.0F, -cvt(bullets[2], 0.0F, 9F, 0.0F, 324F), 0.0F);
		}
		if (rightGun != null) {
			int j = rightGun.countBullets();
			int l = (int) ((float) j * 0.01F);
			int j1 = (int) ((float) j * 0.1F) - 10 * l;
			int l1 = j - (l * 100 + j1 * 10);
			bullets[3] = 0.8F * bullets[3] + 0.2F * (float) l;
			bullets[4] = 0.8F * bullets[4] + 0.2F * (float) j1;
			bullets[5] = 0.8F * bullets[5] + 0.2F * (float) l1;
			mesh.chunkSetAngles("Z_Rammo00", 0.0F, -cvt(bullets[3], 0.0F, 9F, 0.0F, 324F), 0.0F);
			mesh.chunkSetAngles("Z_Rammo01", 0.0F, -cvt(bullets[4], 0.0F, 9F, 0.0F, 324F), 0.0F);
			mesh.chunkSetAngles("Z_Rammo02", 0.0F, -cvt(bullets[5], 0.0F, 9F, 0.0F, 324F), 0.0F);
		}
		if (BaseGameVersion.is412orLater()) {
			if (fm.Gears.lgear)
				mesh.chunkSetAngles("ZGearL", 0.0F, cvt(fm.CT.getGearL(), 0.0F, 1.0F, 0.0F, 76F), 0.0F);
			if (fm.Gears.rgear)
				mesh.chunkSetAngles("ZGearR", 0.0F, cvt(fm.CT.getGearR(), 0.0F, 1.0F, 0.0F, -76F), 0.0F);
			if (fm.Gears.cgear)
				mesh.chunkSetAngles("ZGearC", 0.0F, cvt(fm.CT.getGearC(), 0.0F, 1.0F, 0.0F, 90F), 0.0F);
		} else {
			if (fm.Gears.lgear)
				mesh.chunkSetAngles("ZGearL", 0.0F, cvt(fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 76F), 0.0F);
			if (fm.Gears.rgear)
				mesh.chunkSetAngles("ZGearR", 0.0F, cvt(fm.CT.getGear(), 0.0F, 1.0F, 0.0F, -76F), 0.0F);
			if (fm.Gears.cgear)
				mesh.chunkSetAngles("ZGearC", 0.0F, cvt(fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 90F), 0.0F);
		}
		mesh.chunkSetAngles("Zflaps", 0.0F, cvt(fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, -80F), 0.0F);
		mesh.chunkSetAngles("Zcompass", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
		mesh.chunkSetAngles("Zcompass02", 0.0F, -setNew.azimuth.getDeg(f) - 8F, 0.0F);
		mesh.chunkSetAngles("Zflaps", 0.0F, -76.5F * fm.CT.getFlap(), 0.0F);
		if (fuelPerct > (float) fuelTankLevels[0]) {
			mesh.chunkSetAngles("Zfuel", 0.0F, 295.5F, 0.0F);
			mesh.chunkSetAngles("ZfuelWing", 0.0F, cvt(fuelPerct, fuelTankLevels[0], 100F, 13.5F, 285.5F), 0.0F);
		} else {
			mesh.chunkSetAngles("Zfuel", 0.0F, cvt(fuelPerct, 0.0F, fuelTankLevels[0], -3.5F, 295.5F), 0.0F);
			mesh.chunkSetAngles("ZfuelWing", 0.0F, 13.5F, 0.0F);
		}
		if (setNew.fuelPSI < 12F)
			mesh.chunkVisible("XFlareRed_01", true);
		else
			mesh.chunkVisible("XFlareRed_01", false);
		mesh.chunkSetAngles("Z_Fuel_Selector", 0.0F, fuelSwitchAngle, 0.0F);
		mesh.chunkSetAngles("ZFuelPres", 0.0F, cvt(setNew.fuelPSI, 0.0F, 25F, 0.0F, -180F), 0.0F);
		mesh.chunkSetAngles("Zfuel_press", 0.0F, cvt(setNew.fuelPSI, 0.0F, 30F, 0.0F, 293.5F), 0.0F);
		mesh.chunkSetAngles("ZoilPres", 0.0F, cvt((60F + fm.EI.engines[0].tOilIn * 0.222F) * fm.EI.engines[0].getReadyness(), 0.0F, 200F, 0.0F, 180F), 0.0F);
		mesh.chunkSetAngles("Zoxy", 0.0F, 173F, 0.0F);
		mesh.chunkSetAngles("Z_Amp01", 0.0F, 0.0F, cvt(amps[0], 0.0F, 150F, -9.5F, -108.5F));
		mesh.chunkSetAngles("Z_cockpit_lights", 0.0F, cockpitLightControl ? -20F : 20F, 0.0F);
		mesh.chunkSetAngles("Z_Ignition_switch", 0.0F, cvt(fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F);
		mesh.chunkSetAngles("Z_nav_lights", 0.0F, fm.AS.bNavLightsOn ? -20F : 20F, 0.0F);
		mesh.chunkSetAngles("Z_landing_lights", 0.0F, fm.AS.bLandingLightOn ? -20F : 20F, 0.0F);
		mesh.chunkSetAngles("Z_Pedal_L", 0.0F, 25F * fm.CT.BrakeControl, 0.0F);
		mesh.chunkSetAngles("Z_Pedal_R", 0.0F, 25F * fm.CT.BrakeControl, 0.0F);
		mesh.chunkSetAngles("Z_REVI3_IRON", 0.0F, cvt(interp(setNew.reviPos, setOld.reviPos, f), 0.0F, 1.0F, 0.0F, 90F), 0.0F);
		mesh.chunkSetAngles("ZCarbAir", 0.0F, floatindex(cvt(Atmosphere.temperature((float) fm.Loc.z) - 273.15F, -40F, 50F, 0.0F, 4F), airTempScale), 0.0F);
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
		if (aircraft().getRegiment() != null) {
			if (!aircraft().getRegiment().country().equals("fi")) {
				mesh.chunkVisible("Finnish_writings01", false);
				mesh.chunkVisible("Finnish_writings02", false);
				mesh.chunkVisible("Finnish_writings03", false);
			}
			if (aircraft().getRegiment().country().equals("fr"))
				reversedThrottle = true;
		}
		if (aircraft().thisWeaponsName.startsWith("P35A")) {
			mesh.chunkVisible("2xcal303", false);
			mesh.chunkVisible("1xcal50cal303", false);
			mesh.chunkVisible("2xcal50", true);
		} else {
			mesh.chunkVisible("2xcal303", false);
			mesh.chunkVisible("1xcal50cal303", true);
			mesh.chunkVisible("2xcal50", false);
		}
		// if(aircraft().thisWeaponsName.startsWith("1x50"))
		// {
		// mesh.chunkVisible("2xcal303", false);
		// mesh.chunkVisible("1xcal50cal303", true);
		// } else
		// if(aircraft().thisWeaponsName.startsWith("2x50"))
		// {
		// mesh.chunkVisible("2xcal303", false);
		// mesh.chunkVisible("2xcal50", true);
		// }
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 2) != 0) {
			mesh.chunkVisible("X_GlassDamage5", true);
			mesh.chunkVisible("reticle", false);
			mesh.chunkVisible("reticlemask", false);
			mesh.chunkVisible("REVI3_RAMKA", false);
			mesh.chunkVisible("REVI3_STEKLO", false);
			mesh.chunkVisible("REVI3_RAMKA_damage", true);
			mesh.chunkVisible("REVI3_STEKLO_damage", true);
		}
		if ((fm.AS.astateCockpitState & 1) != 0) {
			mesh.chunkVisible("X_GlassDamage4", true);
			mesh.chunkVisible("X_GlassDamage1", true);
		}
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("Gauges01", false);
			mesh.chunkVisible("D_Gauges01", true);
			mesh.chunkVisible("X_HullDamage1", true);
			mesh.chunkVisible("X_HullDamage4", true);
			mesh.chunkVisible("Zairspeed", false);
			mesh.chunkVisible("Zmanifold_pressure", false);
			mesh.chunkVisible("Zclimb", false);
			mesh.chunkVisible("ZCarbAir", false);
		}
		if ((fm.AS.astateCockpitState & 8) != 0) {
			mesh.chunkVisible("Gauges02", false);
			mesh.chunkVisible("D_Gauges02", true);
			mesh.chunkVisible("X_HullDamage2", true);
			mesh.chunkVisible("X_HullDamage4", true);
			mesh.chunkVisible("Zturn_and_bank", false);
			mesh.chunkVisible("Zturn_and_bank2", false);
			mesh.chunkVisible("Zaltimeter_01", false);
			mesh.chunkVisible("Zaltimeter_02", false);
			mesh.chunkVisible("ZTempOil", false);
			mesh.chunkVisible("ZoilPres", false);
			mesh.chunkVisible("ZFuelPres", false);
			mesh.chunkVisible("Zfuel", false);
			mesh.chunkVisible("Zfuel_press", false);
		}
		if ((fm.AS.astateCockpitState & 4) != 0) {
			mesh.chunkVisible("X_GlassDamage3", true);
			mesh.chunkVisible("X_HullDamage3", true);
		}
		if ((fm.AS.astateCockpitState & 0x80) != 0)
			mesh.chunkVisible("ZOil", true);
		if ((fm.AS.astateCockpitState & 0x10) != 0) {
			mesh.chunkVisible("X_GlassDamage2", true);
			mesh.chunkVisible("X_HullDamage1", true);
		}
		if ((fm.AS.astateCockpitState & 0x20) != 0) {
			mesh.chunkVisible("Gauges03", false);
			mesh.chunkVisible("D_Gauges03", true);
			mesh.chunkVisible("Zrpm", false);
			mesh.chunkVisible("ZTempCyl", false);
			mesh.chunkVisible("Z_Ball", false);
			mesh.chunkVisible("Zclock_hour", false);
			mesh.chunkVisible("Zclock_minute", false);
			mesh.chunkVisible("Zcompass02", false);
			mesh.chunkVisible("X_GlassDamage1", true);
			mesh.chunkVisible("X_HullDamage3", true);
			mesh.chunkVisible("X_HullDamage4", true);
		}
		retoggleLight();
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl)
			setNightMats(true);
		else
			setNightMats(false);
	}

	private void retoggleLight() {
		if (cockpitLightControl) {
			setNightMats(false);
			setNightMats(true);
		} else {
			setNightMats(true);
			setNightMats(false);
		}
	}

	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private float pictAiler;
	private float pictElev;
	private float pictManifold;
	private boolean bNeedSetUp;
	private boolean reversedThrottle;
	private Gun leftGun;
	private Gun rightGun;
	private float flapsLever;
	private float flaps;
	private float gearsLever;
	private float gears;
	private float bullets[] = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F };
	private float amps[] = { 0.0F, 0.0F, 0.0F };
	private static final int fuelSwitchAngles[] = { 0, -72, -152 };
	private static final int fuelTankLevels[] = { 35, 75, 100 };
	private int fuelSwitchAngle;
	private float fuelPerct;
	private static final float speedometerScale[] = { 0.0F, 7F, 14.2F, 32F, 57.16F, 87.45F, 120.9F, 158.3F, 193F, 230.4F, 267.2F, 303.7F, 340F, 377F, 5F, 412.7F, 448.7F, 484.2F, 518F, 553.6F, 587F, 622F };
	private static final float airTempScale[] = { -2.5F, 42F, 135F, 224F, 298.5F };
	private Point3d tmpP;
	private Vector3d tmpV;

	static {
		Property.set(CLASS.THIS(), "normZN", 1.06F);
	}

}
