// This file is part of the SAS IL-2 Sturmovik 1946
// Westland Whirlwind Mod.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Mod Creator:          tfs101
// Original file source: SAS~S3
// Modified by:          SAS - Special Aircraft Services
//                       www.sas1946.com
//
// Last Edited by:       SAS~Storebror
// Last Edited at:       2013/10/22

package com.maddox.il2.objects.air;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitWhirlwind extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (bNeedSetUp) {
				reflectPlaneMats();
				bNeedSetUp = false;
			}
            if(((Whirlwind)aircraft()).bChangedPit)
            {
                reflectPlaneToModel();
                ((Whirlwind)aircraft()).bChangedPit = false;
            }
			if (fm != null) {
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				setNew.trim = 0.92F * setOld.trim + 0.08F * fm.CT.getTrimElevatorControl();
				setNew.throttle1 = 0.85F * setOld.throttle1 + fm.EI.engines[0].getControlThrottle() * 0.15F;
				setNew.throttle2 = 0.85F * setOld.throttle2 + fm.EI.engines[1].getControlThrottle() * 0.15F;
				setNew.prop1 = 0.85F * setOld.prop1 + fm.EI.engines[0].getControlProp() * 0.15F;
				setNew.prop2 = 0.85F * setOld.prop2 + fm.EI.engines[1].getControlProp() * 0.15F;
				setNew.mix1 = 0.85F * setOld.mix1 + fm.EI.engines[0].getControlMix() * 0.15F;
				setNew.mix2 = 0.85F * setOld.mix2 + fm.EI.engines[1].getControlMix() * 0.15F;
				setNew.altimeter = fm.getAltitude();
				float f = waypointAzimuth();
				setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
				setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), f);
				boolean doUseRealisticNavigationInstruments = false;
				if (useRealisticNavigationInstrumentsMethod != null) {
					try {
						doUseRealisticNavigationInstruments = ((Boolean)useRealisticNavigationInstrumentsMethod.invoke(this, null)).booleanValue();
					} catch (Exception e) {
					}
				}
				if (doUseRealisticNavigationInstruments) {
					setNew.waypointAzimuth.setDeg(f - 90F);
					setOld.waypointAzimuth.setDeg(f - 90F);
				} else {
					setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f);
				}
				setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
				
				setNew.oilPress1 = 0.95F * setOld.oilPress1 + 0.05F * cvt(fm.EI.engines[0].getRPM(), 0F, 750F, 0F, 0.6F) * cvt(fm.EI.engines[0].tOilOut, 0F, 15F, 1.7F, 1F) * cvt(fm.EI.engines[0].tOilOut, 50F, 80F, 1F, 0.85F) * cvt(fm.EI.engines[0].tOilOut, 80F, 100F, 1F, 0.9F) * fm.EI.engines[0].getReadyness();
				setNew.oilPress2 = 0.95F * setOld.oilPress2 + 0.05F * cvt(fm.EI.engines[1].getRPM(), 0F, 750F, 0F, 0.6F) * cvt(fm.EI.engines[1].tOilOut, 0F, 15F, 1.7F, 1F) * cvt(fm.EI.engines[1].tOilOut, 50F, 80F, 1F, 0.85F) * cvt(fm.EI.engines[1].tOilOut, 80F, 100F, 1F, 0.9F) * fm.EI.engines[1].getReadyness();
				if (fm.EI.engines[0].getStage() > 6) setNew.oilPress1 = 0.0F;
				if (fm.EI.engines[0].getRPM() < 100F) setNew.oilPress1 = 0.0F;
				if (fm.EI.engines[1].getStage() > 6) setNew.oilPress2 = 0.0F;
				if (fm.EI.engines[1].getRPM() < 100F) setNew.oilPress2 = 0.0F;
				setNew.oilPress1 *= World.Rnd().nextFloat(0.99F, 1.01F);
				setNew.oilPress2 *= World.Rnd().nextFloat(0.99F, 1.01F);
			}
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float throttle1;
		float throttle2;
		float prop1;
		float prop2;
		float mix1;
		float mix2;
		float altimeter;
		float vspeed;
		float oilPress1;
		float oilPress2;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		float trim;

		private Variables() {
			azimuth = new AnglesFork();
			waypointAzimuth = new AnglesFork();
		}

		Variables(Variables variables) {
			this();
		}
	}

	protected float waypointAzimuth() {
		if (this.waypointAzimuthInvertMinusMethod == null) {
			WayPoint waypoint = fm.AP.way.curr();
			if (waypoint == null) return 0.0F;
			waypoint.getP(tmpP);
			tmpV.sub(tmpP, fm.Loc);
			float f;
			for (f = (float) (57.295779513082323D * Math.atan2(-tmpV.y, tmpV.x)); f <= -180F; f += 180F)
				;
			for (; f > 180F; f -= 180F)
				;
			return f;
		} else {
			Float fargs[] = { new Float(10F) };
			try {
				this.waypointAzimuthInvertMinusMethod.invoke((Object) this, (Object[]) fargs);
			} catch (Exception e) {
			}
		}
		return super.waypointAzimuthInvertMinus(10F);
	}

	public CockpitWhirlwind() {
		super("3DO/Cockpit/Whirlwind/hier.him", "bf109");
		setOld = new Variables(null);
		setNew = new Variables(null);
		w = new Vector3f();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		pictFlap = 0.0F;
		pictGear = 0.0F;
		bNeedSetUp = true;
		super.cockpitNightMats = (new String[] { "gauges1", 
				"gauges1_dam",
				"gauges2", 
				"gauges2_dam", 
				"gauges3", 
				"gauges3_dam",
				"gauges4", 
				"swbox", 
				"swbox2", 
				"leftcontroles", 
				"COMPASS" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		if (acoustics != null)
			acoustics.globFX = new ReverbFXRoom(0.45F);

		// The field "printCompassHeading" has been a static field of "AircraftLH" class in IL-2 4.10.1m and became an object field of "Cockpit" class in 4.11.1m onwards.
		// In 4.09m and earlier the field simply doesn't exist.
		// Additionally 4.09m Cockpit Class doesn't contain "waypointAzimuthInvertMinus" method.
		// The following code is supposed to dynamically access the available fields and methods according to it's presence.
		// Attention: Sticky fingers here, we've got to go through reflection!

		Class superClass = this.getClass();
		boolean oldBaseGameVersion = true;
		boolean waypointAzimuthInvertMinusMethodFound = false;
		boolean useRealisticNavigationInstrumentsMethodFound = false;
		boolean AircraftLHClassExists = false;
		this.waypointAzimuthInvertMinusMethod = null;
		this.useRealisticNavigationInstrumentsMethod = null;
		Class waypointAzimuthInvertMinusMethodParameterTypes[] = { java.lang.Float.class };
//		if (!initLogWritten) System.out.println("*******************************************************");
//		if (!initLogWritten) System.out.println("Whirlwind Cockpit Base Game Version Compatibility Check");

		do {
			superClass = superClass.getSuperclass(); // make our way back through class inheritance
			if (superClass == null)
				break; // superClass is either the Object class, an interface, a primitive type, or void. No further SuperClass available.
			if (oldBaseGameVersion) {
				try {
					Field f = superClass.getDeclaredField("printCompassHeading");
					f.setBoolean(this, true);
					oldBaseGameVersion = false; // This game is 4.11m or newer
//					if (!initLogWritten) System.out.println("printCompassHeading found in Cockpit Class, Base Game Version seems to be 4.11m or newer");
				} catch (Exception e) {
				}
			}
			if (!waypointAzimuthInvertMinusMethodFound) {
				try {
					this.waypointAzimuthInvertMinusMethod = superClass.getDeclaredMethod("waypointAzimuthInvertMinus", waypointAzimuthInvertMinusMethodParameterTypes);
					waypointAzimuthInvertMinusMethodFound = true;
//					if (!initLogWritten) System.out.println("waypointAzimuthInvertMinus Method found, Base Game Version seems to be 4.10m or newer");
				} catch (Exception e) {
				}
			}
			if (!useRealisticNavigationInstrumentsMethodFound) {
				try {
					this.useRealisticNavigationInstrumentsMethod = superClass.getDeclaredMethod("useRealisticNavigationInstruments", null);
					useRealisticNavigationInstrumentsMethodFound = true;
//					if (!initLogWritten) System.out.println("useRealisticNavigationInstruments Method found, Base Game Version seems to be 4.10m or newer");
				} catch (Exception e) {
				}
			}
		} while (oldBaseGameVersion && !waypointAzimuthInvertMinusMethodFound && !useRealisticNavigationInstrumentsMethodFound);

		if (oldBaseGameVersion) {
			Class aircraftLHClass = null;
			try {
				aircraftLHClass = Class.forName("com.maddox.il2.objects.air.AircraftLH");
				AircraftLHClassExists = true;
			} catch (Exception e) {
			}
			if (AircraftLHClassExists) { // This game is 4.10m or 4.10.1m
				try {
					Field f = aircraftLHClass.getField("printCompassHeading");
					f.setBoolean(this, true);
//					if (!initLogWritten) System.out.println("printCompassHeading found in AircraftLH Class, Base Game Version seems to be 4.10m or 4.10.1m");
				} catch (Exception e) {
				}
			}
		}
		
//		if (!initLogWritten) {
//			initLogWritten = true;
//			if (AircraftLHClassExists) {
//				System.out.println("Base Game Version 4.10m or 4.10.1m detected");
//			} else if (!oldBaseGameVersion) {
//				System.out.println("Base Game Version 4.11m or newer detected");
//			} else {
//				System.out.println("Base Game Version 4.09m or older detected");
//			}
//			try {
//				Class configClass = Config.class;
//				Field versionField = configClass.getField("VERSION");
//				String versionString = (String)versionField.get(null);
//				System.out.println("Base Game Version according to Config: " + versionString);
//			} catch (Exception e) {
//			}
//			System.out.println("*******************************************************");
//		}
		
        tmpP = new Point3d();
        tmpV = new Vector3d();
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("Blister1_D0", false);
			aircraft().hierMesh().chunkVisible("Radar_Set", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("Blister1_D0", true);
		aircraft().hierMesh().chunkVisible("Radar_Set", true);
		super.doFocusLeave();
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
        if(((Whirlwind)aircraft()).bChangedPit)
        {
            reflectPlaneToModel();
            ((Whirlwind)aircraft()).bChangedPit = false;
        }
		mesh.chunkSetAngles("Z_Trim1", -1722F * setNew.trim, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Flaps1", 60F * (pictFlap = 0.85F * pictFlap + 0.15F * fm.CT.FlapsControl), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Gear1", 90F * (pictGear = 0.85F * pictGear + 0.15F * fm.CT.GearControl), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Throtle1", 93.1F * setNew.throttle1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Throtle2", 93.1F * setNew.throttle2, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Prop1", 95F * setNew.prop1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Prop2", 95F * setNew.prop2, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Mixture1", 90F * setNew.mix1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Mixture2", 90F * setNew.mix2, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_RightPedal", 0.0F, 0.0F, 16F * fm.CT.getRudder());
		mesh.chunkSetAngles("Z_RPedalStep", 0.0F, 0.0F, 16F * fm.CT.getRudder());
		mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 0.0F, -16F * fm.CT.getRudder());
		mesh.chunkSetAngles("Z_LPedalStep", 0.0F, 0.0F, -16F * fm.CT.getRudder());
		mesh.chunkSetAngles("Z_Column", 0.0F, 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 70F);
		mesh.chunkSetAngles("Z_Columnbase", 0.0F, 0.0F, (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 10F);

        mesh.chunkVisible("RUS_GUN", !fm.CT.WeaponControl[0]);
		
		mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 648.2F, 0.0F, 35.0F), speedometerScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Compass1", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Compass3", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Compass2", 90F + setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
//		float f1 = 0.0F;
//		if (fm.AS.bLandingLightOn)
//			f1 -= 35F;
//		if (fm.AS.bNavLightsOn)
//			f1 -= 5F;
//		if (super.cockpitLightControl)
//			f1 -= 2.87F;
//		mesh.chunkSetAngles("Z_Amper1", cvt(f1 + cvt(fm.EI.engines[0].getRPM(), 150F, 2380F, 0.0F, 41.1F), -20F, 130F, -11.5F, 81.5F), 0.0F, 0.0F);
//		mesh.chunkSetAngles("Z_Amper2", cvt(f1 + cvt(fm.EI.engines[1].getRPM(), 150F, 2380F, 0.0F, 41.1F), -20F, 130F, -11.5F, 81.5F), 0.0F, 0.0F);
//		mesh.chunkSetAngles("Z_Carbair1", cvt((Atmosphere.temperature((float) fm.Loc.z) - 273.15F) + 25F * fm.EI.engines[0].getPowerOutput(), -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
//		mesh.chunkSetAngles("Z_Carbair2", cvt((Atmosphere.temperature((float) fm.Loc.z) - 273.15F) + 25F * fm.EI.engines[1].getPowerOutput(), -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Climb1", floatindex(cvt(setNew.vspeed, -20.32F, 20.32F, 0.0F, 8.0F), variometerScale), 0.0F, 0.0F);
//		mesh.chunkSetAngles("Z_Coolant1", cvt(fm.EI.engines[0].tWaterOut, -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
//		mesh.chunkSetAngles("Z_Coolant2", cvt(fm.EI.engines[1].tWaterOut, -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
//		mesh.chunkSetAngles("Z_Temp1", cvt(fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 131.5F), 0.0F, 0.0F);
//		mesh.chunkSetAngles("Z_Temp2", cvt(fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 131.5F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_TurnBank1", -fm.Or.getKren(), 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[2] = cvt(fm.Or.getTangage(), -45F, 45F, 0.031F, -0.031F);
		mesh.chunkSetLocate("Z_TurnBank2", Cockpit.xyz, Cockpit.ypr);
		if (fm.Gears.onGround()) {
			mesh.chunkSetAngles("Z_Fuel2", floatindex(cvt(fm.M.fuel / 3.2835820895522388059701492537313F, 0.0F, 135.0F, 0.0F, 15.0F), fuelScaleGround), 0.0F, 0.0F);
		} else {
			mesh.chunkSetAngles("Z_Fuel2", floatindex(cvt(fm.M.fuel / 3.2835820895522388059701492537313F, 0.0F, 135.0F, 0.0F, 27.0F), fuelScaleFlight), 0.0F, 0.0F);
		}
//		mesh.chunkSetAngles("Z_Fuel2", cvt(fm.M.fuel, 0.0F, 440.0F, 90.0F, 90.0F), 0.0F, 0.0F);
//		mesh.chunkSetAngles("Z_Fuel3", cvt(fm.M.fuel, 245.2F, 490.4F, 0.0F, 120F), 0.0F, 0.0F);
//		mesh.chunkSetAngles("Z_Fuel4", cvt(fm.M.fuel, 245.2F, 490.4F, 0.0F, 120F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_TurnBank3", cvt(getBall(7D), -8.0F, 8.0F, -35.0F, 35.0F), 0.0F, 0.0F);
		w.set(fm.getW());
		fm.Or.transform(w);
		mesh.chunkSetAngles("Z_TurnBank4", cvt(w.z, -0.23562F, 0.23562F, 48.0F, -48.0F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		float rndBoost1 = Math.abs(fm.EI.engines[0].getManifoldPressure() - 1.0F) * 0.05F + 0.01F;
		if (fm.EI.engines[0].getRPM() < 100.0F) rndBoost1 = 0.0F;
		mesh.chunkSetAngles("Z_Manifold1", cvt(fm.EI.engines[0].getManifoldPressure() + World.Rnd().nextFloat(-rndBoost1, rndBoost1), 0.3F, 3.5F, -70.0F, 250F), 0.0F, 0.0F);
		float rndBoost2 = Math.abs(fm.EI.engines[1].getManifoldPressure() - 1.0F) * 0.05F + 0.01F;
		if (fm.EI.engines[1].getRPM() < 100.0F) rndBoost2 = 0.0F;
		mesh.chunkSetAngles("Z_Manifold2", cvt(fm.EI.engines[1].getManifoldPressure() + World.Rnd().nextFloat(-rndBoost2, rndBoost2), 0.3F, 3.5F, -70.0F, 250F), 0.0F, 0.0F);
//		mesh.chunkSetAngles("Z_Oil1", cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness(), 0.0F, 28F, 0.0F, 164.5F), 0.0F, 0.0F);
//		mesh.chunkSetAngles("Z_Oil2", cvt(1.0F + 0.05F * fm.EI.engines[1].tOilOut * fm.EI.engines[1].getReadyness(), 0.0F, 28F, 0.0F, 164.5F), 0.0F, 0.0F);
//		HUD.training("1: " + fm.EI.engines[0].tOilOut+ ", 2: " + fm.EI.engines[1].tOilOut);
		float zOil1 = cvt(fm.EI.engines[0].tOilOut, 50F, 100F, 0F, 270F);
		float zOil2 = cvt(fm.EI.engines[1].tOilOut, 50F, 100F, 0F, 270F);
		if (fm.EI.engines[0].getStage() > 6) zOil1 = 0.0F;
		if (fm.EI.engines[1].getStage() > 6) zOil2 = 0.0F;
		mesh.chunkSetAngles("Z_Oil1", zOil1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Oil2", zOil2, 0.0F, 0.0F);
		mesh.chunkSetAngles("STRELK_OIL_LB1", 0.0F, cvt(setNew.oilPress1, 0F, 1F, 0F, -37.5F), 0.0F);
		mesh.chunkSetAngles("STRELK_OIL_LB2", 0.0F, cvt(setNew.oilPress2, 0F, 1F, 0F, -37.5F), 0.0F);
//		mesh.chunkSetAngles("Z_fuelpress1", cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.4F, 0.0F, 164F), 0.0F, 0.0F);
//		mesh.chunkSetAngles("Z_fuelpress2", cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.4F, 0.0F, 164F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_RPM1", floatindex(cvt(this.fm.EI.engines[0].getRPM(), 1000.0F, 5000.0F, 2.0F, 10.0F), rpmScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_RPM2", floatindex(cvt(this.fm.EI.engines[1].getRPM(), 1000.0F, 5000.0F, 2.0F, 10.0F), rpmScale), 0.0F, 0.0F);
		mesh.chunkVisible("Z_GearGreen1", fm.CT.getGear() > 0.95F);
		mesh.chunkVisible("Z_GearRed1", fm.CT.getGear() < 0.05F || !fm.Gears.lgear || !fm.Gears.rgear);

	    float fRadiator = fm.EI.engines[0].getControlRadiator();
	    if (Math.abs(this.kangle1 - fRadiator) > 0.01F) {
	      this.kangle1 = fRadiator;
	      mesh.chunkSetAngles("WaterL_D0", 0.0F, -20.0F * fRadiator, 0.0F);
	    }
	    fRadiator = fm.EI.engines[1].getControlRadiator();
	    if (Math.abs(this.kangle2 - fRadiator) > 0.01F) {
	      this.kangle2 = fRadiator;
	      mesh.chunkSetAngles("WaterR_D0", 0.0F, -20.0F * fRadiator, 0.0F);
	    }
	
	
	}

	public void reflectCockpitState() {
//		if ((fm.AS.astateCockpitState & 2) == 0)
//			;
//		if ((fm.AS.astateCockpitState & 1) == 0)
//			;
//		if ((fm.AS.astateCockpitState & 0x40) != 0) {
//			mesh.chunkVisible("Panel_D0", false);
//			mesh.chunkVisible("Panel_D1", true);
//		}
//		if ((fm.AS.astateCockpitState & 4) == 0)
//			;
//		if ((fm.AS.astateCockpitState & 8) == 0)
//			;
//		if ((fm.AS.astateCockpitState & 0x80) != 0)
//			mesh.chunkVisible("Z_OilSplats_D1", true);
//		if ((fm.AS.astateCockpitState & 0x10) == 0)
//			;
//		if ((fm.AS.astateCockpitState & 0x20) == 0)
//			;
		retoggleLight();
	}

	public void toggleLight() {
		super.cockpitLightControl = !super.cockpitLightControl;
		if (super.cockpitLightControl)
			setNightMats(true);
		else
			setNightMats(false);
	}

	private void retoggleLight() {
		if (super.cockpitLightControl) {
			setNightMats(false);
			setNightMats(true);
		} else {
			setNightMats(true);
			setNightMats(false);
		}
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
		mesh.materialReplace("Gloss1D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
		mesh.materialReplace("Gloss2D2o", mat);
	}

	protected void reflectPlaneToModel() {
		HierMesh hiermesh = aircraft().hierMesh();
		mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0") || hiermesh.isChunkVisible("WingLIn_D1") || hiermesh.isChunkVisible("WingLIn_D2") || hiermesh.isChunkVisible("WingLIn_D3"));
		mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0") || hiermesh.isChunkVisible("WingRIn_D1") || hiermesh.isChunkVisible("WingRIn_D2") || hiermesh.isChunkVisible("WingRIn_D3"));
	}

	private Method useRealisticNavigationInstrumentsMethod;
	private Method waypointAzimuthInvertMinusMethod;
	private Point3d tmpP;
	private Vector3d tmpV;
//	private static boolean initLogWritten = false;
	private float kangle1 = 0.0F;
	private float kangle2 = 0.0F;


	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private float pictAiler;
	private float pictElev;
	private float pictFlap;
	private float pictGear;
	private boolean bNeedSetUp;
	
	private static final float[] speedometerScale = { 0.0F, 0.0F, 0.0F, 10.0F, 22.0F, 36.0F, 47.0F, 64.0F, 78.0F, 94.0F, 
		                                              115.0F, 133.0F, 152.0F, 171.0F, 186.0F, 201.0F, 220.5F, 239.0F, 257.0F, 274.0F, 
		                                              295.0F, 314.0F, 337.0F, 359.0F, 377.0F, 395.0F, 410.0F, 426.0F, 446.0F, 465.0F, 
		                                              479.0F, 497.0F, 516.0F, 534.0F, 550.0F, 565.0F };
	
	private static final float[] variometerScale = { -158.0F, -111.0F, -65.5F, -32.5F, 0.0F, 32.5F, 65.5F, 111.0F, 158.0F };

	private static final float[] fuelScaleGround = { 60.0F, 70.5F, 80.0F, 86.5F, 92.8F, 96.6F, 98.8F, 105.5F, 110.0F, 112.8F, 
        114.0F, 115.5F, 117.0F, 118.5F, 124.0F };

	private static final float[] fuelScaleFlight = { 60.0F, 75.0F, 80.5F, 85.0F, 87.7F, 92.0F, 94.8F, 97.0F, 99.7F, 102.1F, 
        103.2F, 104.0F, 104.4F, 105.2F, 105.5F, 106.6F, 107.7F, 110.4F, 111.9F, 113.4F,
        114.4F, 115.6F, 116.5F, 117.2F, 117.7F, 119.0F, 120.0F, 121.2F };

	
	//	private static final float[] speedometerScale = { 0.0F, 7.5F, 17.5F, 37.0F, 63.0F, 88.5F, 114.5F, 143.0F, 171.5F, 202.5F, 228.5F, 255.5F, 282.0F, 309.0F, 336.5F, 366.5F, 394.0F, 421.0F, 448.5F, 474.5F, 500.5F, 530.0F, 557.5F, 584.0F, 609.0F, 629.0F };

//	private static final float[] radScale = { 0.0F, 3.0F, 7.0F, 13.5F, 21.5F, 27.0F, 34.5F, 50.5F, 71.0F, 94.0F, 125.0F, 161.0F, 202.5F, 253.0F, 315.5F };

	private static final float[] rpmScale = { 5.0F, 5.0F, 7.0F, 32.0F, 68.0F, 113.5F, 162.5F, 203.5F, 255.0F, 291.5F, 321.5F };

//	private static final float[] variometerScale = { -158.0F, -111.0F, -65.5F, -32.5F, 0.0F, 32.5F, 65.5F, 111.0F, 158.0F };

//	private static final float speedometerScale[] = { 0.0F, 18.5F, 62F, 107F, 152.5F, 198.5F, 238.5F, 252F, 265F, 278.5F, 292F, 305.5F, 319F, 331.5F, 343F };
//	private static final float variometerScale[] = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };
}
