package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Time;

public class CockpitP_47N15K extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (fm != null) {
				reflectCockpitMats();
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				setNew.throttle = 0.85F * setOld.throttle + ((FlightModelMain) (fm)).CT.PowerControl * 0.15F;
				setNew.prop = 0.85F * setOld.prop + ((FlightModelMain) (fm)).CT.getStepControl() * 0.15F;
				setNew.stage = 0.85F * setOld.stage + (float) ((FlightModelMain) (fm)).EI.engines[0].getControlCompressor() * 0.15F;
				setNew.mix = 0.85F * setOld.mix + ((FlightModelMain) (fm)).EI.engines[0].getControlMix() * 0.15F;
				setNew.manifold = 0.7F * setOld.manifold + 0.3F * ((FlightModelMain) (fm)).EI.engines[0].getManifoldPressure();
				setNew.altimeter = fm.getAltitude();
				if (Math.abs(((FlightModelMain) (fm)).Or.getKren()) < 45F)
					setNew.azimuth = (35F * setOld.azimuth + -((FlightModelMain) (fm)).Or.getYaw()) / 36F;
				if (setOld.azimuth > 270F && setNew.azimuth < 90F)
					setOld.azimuth -= 360F;
				if (setOld.azimuth < 90F && setNew.azimuth > 270F)
					setOld.azimuth += 360F;
				setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
				setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
				pictTurba = 0.97F * pictTurba + 0.03F * (0.5F * ((FlightModelMain) (fm)).EI.engines[0].getPowerOutput() + 0.5F * cvt(((FlightModelMain) (fm)).EI.engines[0].getRPM(), 0.0F, 2000F, 0.0F, 1.0F));
				float f = ((P_47N15) aircraft()).k14Distance;
				setNew.k14w = (4.95F * CockpitP_47N15K.k14TargetWingspanScale[((P_47N15) aircraft()).k14WingspanType]) / f;
				setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
				setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitP_47N15K.k14TargetMarkScale[((P_47N15) aircraft()).k14WingspanType];
				setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float) ((P_47N15) aircraft()).k14Mode;
				Vector3d vector3d = ((SndAircraft) (aircraft())).FM.getW();
				double d = 0.00125D * (double) f;
				float f1 = (float) Math.toDegrees(d * ((Tuple3d) (vector3d)).z);
				float f2 = -(float) Math.toDegrees(d * ((Tuple3d) (vector3d)).y);
				float f3 = floatindex((f - 200F) * 0.02F, CockpitP_47N15K.k14BulletDrop) - CockpitP_47N15K.k14BulletDrop[0];
				f2 += (float) Math.toDegrees(Math.atan(f3 / f));
				setNew.k14x = 0.92F * setOld.k14x + 0.08F * f1;
				setNew.k14y = 0.92F * setOld.k14y + 0.08F * f2;
				if (setNew.k14x > 7F)
					setNew.k14x = 7F;
				if (setNew.k14x < -7F)
					setNew.k14x = -7F;
				if (setNew.k14y > 7F)
					setNew.k14y = 7F;
				if (setNew.k14y < -7F)
					setNew.k14y = -7F;
				if (bPowerON)
					fzp = fzp < 1.0F ? fzp + 0.05F : 1.0F;
				else
					fzp = fzp > 0.0F ? fzp - 0.005F : 0.0F;
				if (fm.getAltitude() > 3000F) {
					CockpitP_47N15K.oxyp = CockpitP_47N15K.oxyp > 0.0F ? CockpitP_47N15K.oxyp - 1E-005F : 0.0F;
					if (iOxy++ > 90)
						iOxy = 0;
				}
			}
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float throttle;
		float prop;
		float mix;
		float stage;
		float manifold;
		float altimeter;
		float azimuth;
		float vspeed;
		float waypointAzimuth;
		float k14wingspan;
		float k14mode;
		float k14x;
		float k14y;
		float k14w;

		private Variables() {
		}

		Variables(Variables variables) {
			this();
		}
	}

	protected float waypointAzimuth() {
		WayPoint waypoint = ((FlightModelMain) (super.fm)).AP.way.curr();
		if (waypoint == null) {
			return 0.0F;
		} else {
			waypoint.getP(tmpP);
			tmpV.sub(tmpP, ((FlightModelMain) (super.fm)).Loc);
			return (float) (57.295779513082323D * Math.atan2(-((Tuple3d) (tmpV)).y, ((Tuple3d) (tmpV)).x));
		}
	}

	public CockpitP_47N15K() {
		super("3DO/Cockpit/P-47D-30/CockpitP47N15.him", "bf109");
		bRadarWarning = false;
		bPowerON = true;
		bTWlock = false;
		setOld = new Variables(null);
		setNew = new Variables(null);
		w = new Vector3f();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		tmpP = new Point3d();
		tmpV = new Vector3d();
		super.cockpitNightMats = (new String[] { "prib1", "pribA", "pribB", "pribC", "prib4", "prib5", "prib6", "prib7", "pri14", "prib1_d1", "pribA_d1", "prib4_d1", "Frame07", "Frame08", "panel", "shkalb", "Frame20", "shkala" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
	}

	public void reflectWorldToInstruments(float f) {
		checkForEnemyBehind();
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(((FlightModelMain) (super.fm)).CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.65F);
		super.mesh.chunkSetLocate("Body_blister", Cockpit.xyz, Cockpit.ypr);
		super.mesh.chunkSetLocate("Glass1", Cockpit.xyz, Cockpit.ypr);
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) == 0) {
			int i = ((P_47N15) aircraft()).k14Mode;
			boolean flag = i < 2;
			if (bPowerON)
				super.mesh.chunkVisible("Z_Z_RETICLE", flag);
			else
				super.mesh.chunkVisible("Z_Z_RETICLE", false);
			flag = i > 0;
			if (bPowerON)
				super.mesh.chunkVisible("Z_Z_RETICLE1", flag);
			else
				super.mesh.chunkVisible("Z_Z_RETICLE1", false);
			super.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, setNew.k14x, setNew.k14y);
			resetYPRmodifier();
			Cockpit.xyz[0] = setNew.k14w + 0.02F;
			for (int j = 1; j < 7; j++) {
				if (bPowerON)
					super.mesh.chunkVisible("Z_Z_AIMMARK" + j, flag);
				else
					super.mesh.chunkVisible("Z_Z_AIMMARK" + j, false);
				super.mesh.chunkSetLocate("Z_Z_AIMMARK" + j, Cockpit.xyz, Cockpit.ypr);
			}

		}
		super.mesh.chunkSetAngles("armPedalL", 0.0F, -15F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
		super.mesh.chunkSetAngles("armPedalR", 0.0F, 15F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
		super.mesh.chunkSetAngles("PedalL", 0.0F, 15F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
		super.mesh.chunkSetAngles("PedalR", 0.0F, -15F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
		super.mesh.chunkSetAngles("Stick", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl) * 20F, (pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl) * 16F);
		super.mesh.chunkSetAngles("Z_Target1", setNew.k14wingspan, 0.0F, 0.0F);
		super.mesh.chunkSetAngles("supercharge", 0.0F, 60F * setNew.throttle, 0.0F);
		super.mesh.chunkSetAngles("throttle", 0.0F, 62F * setNew.throttle, 0.0F);
		super.mesh.chunkSetAngles("prop", 0.0F, 70F * setNew.prop, 0.0F);
		super.mesh.chunkSetAngles("mixtura", 0.0F, 55F * setNew.mix, 0.0F);
		super.mesh.chunkSetAngles("flaplever", 0.0F, 0.0F, 70F * ((FlightModelMain) (super.fm)).CT.FlapsControl);
		super.mesh.chunkSetAngles("zfuelR", 0.0F, floatindex(cvt(((FlightModelMain) (super.fm)).M.fuel, 0.0F, 981F, 0.0F, 6F), fuelGallonsScale), 0.0F);
		super.mesh.chunkSetAngles("zfuelL", 0.0F, -floatindex(cvt(((FlightModelMain) (super.fm)).M.fuel, 0.0F, 981F, 0.0F, 4F), fuelGallonsAuxScale), 0.0F);
		super.mesh.chunkSetAngles("zacceleration", 0.0F, cvt(super.fm.getOverload(), -4F, 12F, -77F, 244F), 0.0F);
		super.mesh.chunkSetAngles("zSpeed1a", 0.0F, floatindex(cvt(Pitot.Indicator((float) ((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), speedometerScale), 0.0F);
		super.mesh.chunkSetAngles("zclimb", 0.0F, floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
		w.set(super.fm.getW());
		((FlightModelMain) (super.fm)).Or.transform(w);
		super.mesh.chunkSetAngles("zTurn1a", 0.0F, cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 25F, -25F), 0.0F);
		super.mesh.chunkSetAngles("zSlide1a", 0.0F, cvt(getBall(7D), -7F, 7F, -16F, 16F), 0.0F);
		super.mesh.chunkSetAngles("zManifold1a", 0.0F, cvt(((FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 320F), 0.0F);
		super.mesh.chunkSetAngles("z_Altimeter1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F);
		super.mesh.chunkSetAngles("z_Altimeter2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F);
		super.mesh.chunkSetAngles("z_Altimeter3", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 360F), 0.0F);
		super.mesh.chunkSetAngles("zRPM1a", 0.0F, cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 316F), 0.0F);
		super.mesh.chunkSetAngles("zoiltemp1a", 0.0F, cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 300F, 0.0F, 84F), 0.0F);
		super.mesh.chunkSetAngles("zClock1b", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		super.mesh.chunkSetAngles("zClock1a", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		super.mesh.chunkSetAngles("zhorizont1a", 0.0F, -((FlightModelMain) (super.fm)).Or.getKren(), 0.0F);
		if (((FlightModelMain) (super.fm)).EI.engines[0].getStage() < 2) {
			super.mesh.chunkSetAngles("zhorizont1a", 0.0F, -30F, 0.0F);
			super.mesh.chunkSetLocate("zhorizont1b", new float[] { 0.0F, 0.0F, -0.01F }, new float[] { 0.0F, 0.0F, -10F });
			super.mesh.chunkVisible("Frame_64sc", true);
		} else {
			super.mesh.chunkSetAngles("zhorizont1a", 0.0F, -((FlightModelMain) (super.fm)).Or.getKren(), 0.0F);
			resetYPRmodifier();
			Cockpit.xyz[2] = cvt(((FlightModelMain) (super.fm)).Or.getTangage(), -45F, 45F, 0.0328F, -0.0328F);
			super.mesh.chunkSetLocate("zhorizont1b", Cockpit.xyz, Cockpit.ypr);
			super.mesh.chunkVisible("Frame_64sc", false);
		}
		super.mesh.chunkSetAngles("zpressfuel1a", 0.0F, cvt(((FlightModelMain) (super.fm)).M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.4F, 0.0F, -154F), 0.0F);
		super.mesh.chunkSetAngles("zpressoil1a", 0.0F, cvt(1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[0].tOilOut * ((FlightModelMain) (super.fm)).EI.engines[0].getReadyness(), 0.0F, 7.45F, 0.0F, 180F), 0.0F);
		super.mesh.chunkSetAngles("zAzimuth1a", 0.0F, cvt(((FlightModelMain) (super.fm)).Or.getTangage(), -5F, 5F, -5F, 5F), 0.0F);
		super.mesh.chunkSetAngles("zAzimuth1b", 0.0F, 90F - setNew.azimuth, 0.0F);
		super.mesh.chunkSetAngles("zMagAzimuth1a", 0.0F, cvt(((FlightModelMain) (super.fm)).Or.getTangage(), -65F, 65F, -65F, 65F), 0.0F);
		super.mesh.chunkSetAngles("zMagAzimuth1b", -90F + setNew.azimuth, 0.0F, 0.0F);
		super.mesh.chunkVisible("Z_Red1", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F || !((FlightModelMain) (super.fm)).Gears.lgear || !((FlightModelMain) (super.fm)).Gears.rgear);
		super.mesh.chunkVisible("Z_Green1", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F);
		super.mesh.chunkVisible("Z_Red2", ((FlightModelMain) (super.fm)).M.fuel / ((FlightModelMain) (super.fm)).M.maxFuel < 0.15F);
		super.mesh.chunkSetAngles("ztemp1a", 0.0F, cvt((Atmosphere.temperature((float) ((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z) - 273.15F) + 25F * ((FlightModelMain) (super.fm)).EI.engines[0].getPowerOutput(), -50F, 150F, 0.0F, 105F), 0.0F);
		super.mesh.chunkSetAngles("zpress1a", 0.0F, cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, -50F, 150F, -15F, 115F), 0.0F);
		float f1 = ((FlightModelMain) (super.fm)).EI.engines[0].getRPM();
		f1 = 1.8F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
		super.mesh.chunkSetAngles("zpresswater1a", 0.0F, cvt(f1, 0.0F, 5F, 0.0F, 300F), 0.0F);
		float f2 = ((FlightModelMain) (super.fm)).EI.engines[0].getRPM();
		f2 = 550F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f2))));
		super.mesh.chunkSetAngles("zfas1a", 0.0F, cvt(f2, 0.0F, 2000F, 0.0F, 180F), 0.0F);
		super.mesh.chunkSetAngles("zoxipress1a", 0.0F, cvt(300F, 0.0F, 500F, 0.0F, 180F), 0.0F);
		float f3 = ((FlightModelMain) (super.fm)).EI.engines[0].getRPM();
		f3 = 1.52F * (float) Math.sqrt(Math.sqrt(Math.sqrt(f3)));
		super.mesh.chunkSetAngles("zsuction1a", 0.0F, cvt(f3, 0.0F, 10F, 0.0F, 300F), 0.0F);
		float f4 = 0.0F;
		int k = ((FlightModelMain) (super.fm)).EI.engines[0].getStage();
		f4 = k == 2 ? 75F : 0.0F;
		if (((FlightModelMain) (super.fm)).AS.bLandingLightOn)
			f4 += 35F;
		if (((FlightModelMain) (super.fm)).AS.bNavLightsOn)
			f4 += 5F;
		if (super.cockpitLightControl)
			f4 += 3F;
		if (((FlightModelMain) (super.fm)).AS.bShowSmokesOn)
			f4 += 5F;
		super.mesh.chunkSetAngles("zamper", 0.0F, cvt(f4 - cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 150F, 2380F, 0.0F, 41.1F), 0.0F, 150F, 0.0F, 90F), 0.0F);
		super.mesh.chunkSetAngles("MagSwitch", 0.0F, 30F * (float) ((FlightModelMain) (super.fm)).EI.engines[0].getControlMagnetos(), 0.0F);
		float f5 = 0.0F;
		float f6 = P_47Z.bExtTank ? ((FlightModelMain) (super.fm)).M.fuel + 284F : ((FlightModelMain) (super.fm)).M.fuel;
		if (f6 < 705F)
			f5 = f6 > 350F ? -90F : -180F;
		super.mesh.chunkSetAngles("fuelsw1", f5, 0.0F, 0.0F);
		float f7 = 0.0F;
		if (P_47Z.bExtTank && f6 > 1006F)
			f7 = 90F;
		super.mesh.chunkSetAngles("fuelsw2", f7, 0.0F, 0.0F);
		if (bPowerON) {
			super.mesh.chunkVisible("Z_Red1", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F || !((FlightModelMain) (super.fm)).Gears.lgear || !((FlightModelMain) (super.fm)).Gears.rgear);
			super.mesh.chunkVisible("Z_Green1", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F);
			super.mesh.chunkVisible("Z_Red2", ((FlightModelMain) (super.fm)).M.fuel / ((FlightModelMain) (super.fm)).M.maxFuel < 0.15F);
			super.mesh.chunkVisible("Z_WaterInjL1", ((FlightModelMain) (super.fm)).EI.engines[0].getControlAfterburner());
			super.mesh.chunkVisible("Z_Green2", bRadarWarning);
			super.mesh.chunkVisible("Z_Red4", ((FlightModelMain) (super.fm)).AS.bShowSmokesOn);
			if (super.fm.getAltitude() > 3000F) {
				super.mesh.chunkVisible("Z_Oxy_ON", iOxy < 45);
				super.mesh.chunkVisible("Z_Oxy_OFF", iOxy > 46 && iOxy < 89);
			} else {
				super.mesh.chunkVisible("Z_Oxy_ON", false);
				super.mesh.chunkVisible("Z_Oxy_OFF", false);
			}
		} else {
			super.mesh.chunkVisible("Z_Red1", false);
			super.mesh.chunkVisible("Z_Green1", false);
			super.mesh.chunkVisible("Z_Red2", false);
			super.mesh.chunkVisible("Z_WaterInjL1", false);
			super.mesh.chunkVisible("Z_Red3", false);
			super.mesh.chunkVisible("Z_Green2", false);
			super.mesh.chunkVisible("Z_Red4", false);
		}
		if (bNavLightsState != ((FlightModelMain) (super.fm)).AS.bNavLightsOn) {
			sfxClick(1);
			bNavLightsState = ((FlightModelMain) (super.fm)).AS.bNavLightsOn;
		}
		if (bLandingLightState != ((FlightModelMain) (super.fm)).AS.bLandingLightOn) {
			sfxClick(1);
			bLandingLightState = ((FlightModelMain) (super.fm)).AS.bLandingLightOn;
		}
		if (iMagnetosState != ((FlightModelMain) (super.fm)).EI.engines[0].getControlMagnetos()) {
			sfxClick(2);
			iMagnetosState = ((FlightModelMain) (super.fm)).EI.engines[0].getControlMagnetos();
		}
	}

	public void reflectCockpitState() {
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) != 0) {
			super.mesh.chunkVisible("Z_Holes1_D1", true);
			super.mesh.chunkVisible("pricel", false);
			super.mesh.chunkVisible("pricel_d1", true);
			super.mesh.chunkVisible("Z_Z_RETICLE", false);
			super.mesh.chunkVisible("Z_Z_MASK", false);
		}
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 1) != 0)
			super.mesh.chunkVisible("Z_Holes1_D1", true);
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) != 0) {
			super.mesh.chunkVisible("pribors1", false);
			super.mesh.chunkVisible("pribors1_d1", true);
			super.mesh.chunkVisible("zamper", false);
			super.mesh.chunkVisible("zAzimuth1a", false);
			super.mesh.chunkVisible("zAzimuth1b", false);
			super.mesh.chunkVisible("zSpeed1a", false);
			super.mesh.chunkVisible("zacceleration", false);
			super.mesh.chunkVisible("zMagAzimuth1a", false);
			super.mesh.chunkVisible("zMagAzimuth1b", false);
			super.mesh.chunkVisible("zpresswater1a", false);
			super.mesh.chunkVisible("zclimb", false);
			super.mesh.chunkVisible("zRPM1a", false);
			super.mesh.chunkVisible("zoiltemp1a", false);
			super.mesh.chunkVisible("zturbormp1a", false);
			super.mesh.chunkVisible("zfas1a", false);
			super.mesh.chunkVisible("zoxipress1a", false);
		}
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 4) == 0 || (((FlightModelMain) (super.fm)).AS.astateCockpitState & 8) != 0)
			super.mesh.chunkVisible("Z_Holes2_D1", true);
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x80) != 0)
			super.mesh.chunkVisible("Z_OilSplats_D1", true);
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) == 0 || (((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) != 0) {
			super.mesh.chunkVisible("pribors2", false);
			super.mesh.chunkVisible("pribors2_d1", true);
			super.mesh.chunkVisible("zClock1b", false);
			super.mesh.chunkVisible("zClock1a", false);
			super.mesh.chunkVisible("zfuelR", false);
			super.mesh.chunkVisible("zfuelL", false);
			super.mesh.chunkVisible("zsuction1a", false);
			super.mesh.chunkVisible("zTurn1a", false);
			super.mesh.chunkVisible("zSlide1a", false);
			super.mesh.chunkVisible("zhorizont1a", false);
			super.mesh.chunkVisible("zAlt1a", false);
			super.mesh.chunkVisible("zAlt1b", false);
			super.mesh.chunkVisible("zpressfuel1a", false);
			super.mesh.chunkVisible("zpressoil1a", false);
			super.mesh.chunkVisible("ztempoil1a", false);
			super.mesh.chunkVisible("zManifold1a", false);
		}
		retoggleLight();
	}

	protected void reflectCockpitMats() {
		stage = ((FlightModelMain) (super.fm)).EI.engines[0].getStage();
		if (stage > 0 && stage < 4 && !bEngage && !bEnergize) {
			super.mesh.materialReplace(super.mesh.materialFind("Texture21s"), "Texture21sd");
			bEnergize = true;
			bEngage = false;
		}
		if (stage >= 4 && stage < 6 && (!bEngage) & bEnergize) {
			super.mesh.materialReplace(super.mesh.materialFind("Texture21s"), "Texture21su");
			bEnergize = false;
			bEngage = true;
		}
		if ((stage == 0 || stage == 6) && (bEngage || bEnergize)) {
			super.mesh.materialReplace(super.mesh.materialFind("Texture21s"), "Texture21s");
			bEnergize = false;
			bEngage = false;
		}
		if (((FlightModelMain) (super.fm)).EI.engines[0].getControlMagnetos() == 0 && bPowerON) {
			if (super.mesh.chunkFindCheck("Z_Z_RETICLE0") >= 0)
				super.mesh.chunkVisible("Z_Z_RETICLE0", false);
			int i = super.mesh.materialFind("K-14windows");
			if (i > 0)
				super.mesh.materialReplace(i, "K-14windows_off");
			i = super.mesh.materialFind("shkala");
			if (i > 0)
				super.mesh.materialReplace(i, "shkala_off");
			bPowerON = false;
		}
		if (((FlightModelMain) (super.fm)).EI.engines[0].getControlMagnetos() > 0 && !bPowerON) {
			if (super.mesh.chunkFindCheck("Z_Z_RETICLE0") >= 0)
				super.mesh.chunkVisible("Z_Z_RETICLE0", true);
			int j = super.mesh.materialFind("K-14windows");
			if (j > 0)
				super.mesh.materialReplace(j, "K-14windows");
			j = super.mesh.materialFind("shkala");
			if (j > 0)
				super.mesh.materialReplace(j, "shkala");
			bPowerON = true;
			sfxStart(9);
		}
		if (((FlightModelMain) (super.fm)).Gears.bTailwheelLocked && !bTWlock) {
			int k = super.mesh.materialFind("wlock");
			if (k > 0)
				super.mesh.materialReplace(k, "wlock_on");
			bTWlock = true;
		}
		if (!((FlightModelMain) (super.fm)).Gears.bTailwheelLocked && bTWlock) {
			int l = super.mesh.materialFind("wlock");
			if (l > 0)
				super.mesh.materialReplace(l, "wlock");
			bTWlock = false;
		}
		if (!Main3D.cur3D().isViewMirror())
			super.mesh.chunkVisible("MirrorFrame", false);
		else
			super.mesh.chunkVisible("MirrorFrame", true);
	}

	private void checkForEnemyBehind() {
		Aircraft aircraft1 = War.getNearestEnemy((P_47N15) aircraft(), 800F);
		if (aircraft1 != null && ((aircraft1 instanceof TypeFighter) || (aircraft1 instanceof TypeStormovik))) {
			danger = ((SndAircraft) (aircraft1)).FM;
			dist = (float) ((FlightModelMain) (danger)).Loc.distance(((FlightModelMain) (((SndAircraft) ((P_47N15) aircraft())).FM)).Loc);
			VDanger.sub(((FlightModelMain) (danger)).Loc, ((FlightModelMain) (((SndAircraft) ((P_47N15) aircraft())).FM)).Loc);
			VDanger.normalize();
			bRadarWarning = ((Tuple3d) (VDanger)).x < 0.0D && dist > 100F;
		} else {
			danger = null;
			bRadarWarning = false;
		}
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

	private FlightModel danger;
	private static Vector3d VDanger = new Vector3d();
	private boolean bRadarWarning;
	private float dist;
	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private float pictAiler;
	private float pictElev;
	private float pictTurba;
	private static final float fuelGallonsScale[] = { 0.0F, 8.25F, 17.5F, 36.5F, 54F, 90F, 108F };
	private static final float fuelGallonsAuxScale[] = { 0.0F, 38F, 62.5F, 87F, 104F };
	private static final float speedometerScale[] = { 0.0F, 5F, 47.5F, 92F, 134F, 180F, 227F, 241F, 255F, 262.5F, 270F, 283F, 296F, 312F, 328F };
	private static final float variometerScale[] = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };
	private static final float k14TargetMarkScale[] = { -0F, -4.2F, -15F, -26.5F, -42.5F, -57F, -62.5F, -70.5F, -83.5F, -97.5F, -102.2F, -107.5F };
	private static final float k14TargetWingspanScale[] = { 9.9F, 10.52F, 12F, 13.8F, 16.34F, 19F, 20F, 22F, 25F, 29.25F, 30F, 32.85F };
	private static final float k14BulletDrop[] = { 5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 10.603F, 10.704F, 10.739F, 10.782F,
			10.789F };
	private Point3d tmpP;
	private Vector3d tmpV;
	private boolean bPowerON;
	private static boolean bNavLightsState = false;
	private static boolean bLandingLightState = false;
	private static int iMagnetosState = 3;
	private float fzp;
	private static float oxyp = 1.0F;
	private int iOxy;
	private boolean bTWlock;
	private int stage;
	private static boolean bEnergize = false;
	private static boolean bEngage = false;
}
