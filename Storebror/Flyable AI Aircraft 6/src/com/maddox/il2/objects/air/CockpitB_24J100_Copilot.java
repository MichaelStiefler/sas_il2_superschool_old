// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   CockpitB24D_Copilot.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitCopilot, B_24J100, Aircraft, Cockpit

public class CockpitB_24J100_Copilot extends CockpitCopilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (bNeedSetUp) {
				bNeedSetUp = false;
				reflectPlaneMats();
			}
			if (fm != null) {
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				setNew.throttle1 = 0.85F * setOld.throttle1 + fm.EI.engines[0].getCoPControlThrottle() * 0.15F;
				setNew.throttle2 = 0.85F * setOld.throttle2 + fm.EI.engines[1].getCoPControlThrottle() * 0.15F;
				setNew.throttle3 = 0.85F * setOld.throttle3 + fm.EI.engines[2].getCoPControlThrottle() * 0.15F;
				setNew.throttle4 = 0.85F * setOld.throttle4 + fm.EI.engines[3].getCoPControlThrottle() * 0.15F;
				setNew.supercharger1 = 0.85F * setOld.supercharger1 + fm.EI.engines[0].getCoPontrolManualAfterburner() * 0.15F;
				setNew.supercharger2 = 0.85F * setOld.supercharger2 + fm.EI.engines[1].getCoPontrolManualAfterburner() * 0.15F;
				setNew.supercharger3 = 0.85F * setOld.supercharger3 + fm.EI.engines[2].getCoPontrolManualAfterburner() * 0.15F;
				setNew.supercharger4 = 0.85F * setOld.supercharger4 + fm.EI.engines[3].getCoPontrolManualAfterburner() * 0.15F;
				setNew.mix1 = 0.85F * setOld.mix1 + fm.EI.engines[0].getCoPControlMix() * 0.15F;
				setNew.mix2 = 0.85F * setOld.mix2 + fm.EI.engines[1].getCoPControlMix() * 0.15F;
				setNew.mix3 = 0.85F * setOld.mix3 + fm.EI.engines[2].getCoPControlMix() * 0.15F;
				setNew.mix4 = 0.85F * setOld.mix4 + fm.EI.engines[3].getCoPControlMix() * 0.15F;
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
				setNew.altimeter = fm.getAltitude();
				float f1 = waypointAzimuth();
				setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
				if (useRealisticNavigationInstruments()) {
					setNew.waypointAzimuth.setDeg(f1 - 90F);
					setOld.waypointAzimuth.setDeg(f1 - 90F);
					setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F),
							radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F));
					if (fm.AS.listenLorenzBlindLanding && fm.AS.isAAFIAS) {
						setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
						setNew.ilsGS = (10F * setOld.ilsGS + getGlidePath()) / 11F;
					} else {
						setNew.ilsLoc = 0.0F;
						setNew.ilsGS = 0.0F;
					}
				} else {
					setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f1);
					setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f1 - setOld.azimuth.getDeg(0.1F)
							- 90F);
				}
				engine1PropPitch = 0.5F * engine1PropPitch + 0.5F * (float)fm.EI.engines[0].getControlPropDelta();
				engine2PropPitch = 0.5F * engine2PropPitch + 0.5F * (float)fm.EI.engines[1].getControlPropDelta();
				engine3PropPitch = 0.5F * engine3PropPitch + 0.5F * (float)fm.EI.engines[2].getControlPropDelta();
				engine4PropPitch = 0.5F * engine4PropPitch + 0.5F * (float)fm.EI.engines[3].getControlPropDelta();
				for (int i = 0; i < 4; i++) {
					float f2 = fm.EI.engines[i].getCoPControlRadiator();
					if (f2 < enginePrevRadiators[i])
						engineRadiators[i] -= 0.20000000000000001D;
					else if (f2 > enginePrevRadiators[i])
						engineRadiators[i] += 0.20000000000000001D;
					else
						engineRadiators[i] *= 0.5F;
					if (engineRadiators[i] > 1.0F)
						engineRadiators[i] = 1.0F;
					if (engineRadiators[i] < -1F)
						engineRadiators[i] = -1F;
					enginePrevRadiators[i] = f2;
				}

				setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
			}
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float throttle1;
		float throttle2;
		float throttle3;
		float throttle4;
		float supercharger1;
		float supercharger2;
		float supercharger3;
		float supercharger4;
		float altimeter;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		AnglesFork radioCompassAzimuth;
		float vspeed;
		float ilsLoc;
		float ilsGS;
		float mix1;
		float mix2;
		float mix3;
		float mix4;

		private Variables() {
			azimuth = new AnglesFork();
			waypointAzimuth = new AnglesFork();
			radioCompassAzimuth = new AnglesFork();
		}

	}

	protected float waypointAzimuth() {
		return super.waypointAzimuthInvertMinus(10F);
	}

	public CockpitB_24J100_Copilot() {
		super("3DO/Cockpit/B-24D-Cockpit/copilot_hier.him", "bf109");
		bNeedSetUp = true;
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		gearsLever = 0.0F;
		gears = 0.0F;
		flapsLever = 0.0F;
		flaps = 0.0F;
		pictManf1 = 1.0F;
		pictManf2 = 1.0F;
		pictManf3 = 1.0F;
		pictManf4 = 1.0F;
		engine1PropPitch = 0.0F;
		engine2PropPitch = 0.0F;
		engine3PropPitch = 0.0F;
		engine4PropPitch = 0.0F;
		bombReleaseTimer = 4000F;
		isSlideRight = false;
		dialsR1Dmg = false;
		dialsR2Dmg = false;
		dialsL1Dmg = false;
		dialsL2Dmg = false;
		dialDmg1 = 0.0F;
		cockpitNightMats = (new String[] { "BC434Gg", "Gauges", "Gauges_Dmg", "Gyrocompass", "Needles", "Station" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		light1 = new LightPointActor(new LightPoint(), new Point3d(4.1200000000000001D, 0.25D, 1.48D));
		light1.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
		light1.light.setEmit(0.0F, 0.0F);
		pos.base().draw.lightMap().put("LAMPHOOK1", light1);
	}

	public void reflectWorldToInstruments(float f) {
		Aircraft.xyz[0] = 0.0F;
		Aircraft.xyz[2] = 0.0F;
		Aircraft.ypr[0] = 0.0F;
		Aircraft.ypr[1] = 0.0F;
		Aircraft.ypr[2] = 0.0F;
		Aircraft.xyz[1] = fm.CT.getCockpitDoor() * 0.5F;
		if ((double)Aircraft.xyz[1] < 0.01D)
			Aircraft.xyz[1] = 0.0F;
		if (isSlideRight)
			mesh.chunkSetLocate("BlisterRight", Aircraft.xyz, Aircraft.ypr);
		else
			mesh.chunkSetLocate("BlisterLeft", Aircraft.xyz, Aircraft.ypr);
		mesh.chunkSetAngles("zThrottle1", 90F * interp(setNew.throttle1, setOld.throttle1, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("zThrottle2", 90F * interp(setNew.throttle2, setOld.throttle2, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("zThrottle3", 90F * interp(setNew.throttle3, setOld.throttle3, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("zThrottle4", 90F * interp(setNew.throttle4, setOld.throttle4, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("zMixture1", floatindex(cvt(setNew.mix1, 0.0F, 1.2F, 0.0F, 3F), mixScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("zMixture2", floatindex(cvt(setNew.mix2, 0.0F, 1.2F, 0.0F, 3F), mixScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("zMixture3", floatindex(cvt(setNew.mix3, 0.0F, 1.2F, 0.0F, 3F), mixScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("zMixture4", floatindex(cvt(setNew.mix4, 0.0F, 1.2F, 0.0F, 3F), mixScale), 0.0F, 0.0F);
		float f1 = 0.0F;
		if (engine1PropPitch > 0.0F && engine2PropPitch > 0.0F && engine3PropPitch > 0.0F && engine4PropPitch > 0.0F) {
			mesh.chunkSetAngles("zPropPitchGUp", 0.0F, 0.0F, engine1PropPitch * -115F);
			f1 = -cvt(engine1PropPitch, 0.8F, 1.0F, 0.01F, 20F);
		} else if (engine1PropPitch < 0.0F && engine2PropPitch < 0.0F && engine3PropPitch < 0.0F && engine4PropPitch < 0.0F) {
			mesh.chunkSetAngles("zPropPGangDown", 0.0F, 0.0F, engine1PropPitch * 115F);
			f1 = cvt(-engine1PropPitch, 0.8F, 1.0F, 0.01F, 20F);
		} else {
			mesh.chunkSetAngles("zPropPGangDown", 0.0F, 0.0F, 0.0F);
			mesh.chunkSetAngles("zPropPitchGUp", 0.0F, 0.0F, 0.0F);
		}
		mesh.chunkSetAngles("zPropPitch1", 0.0F, 0.0F, f1 == 0.0F ? engine1PropPitch * 20F : f1);
		mesh.chunkSetAngles("zPropPitch2", 0.0F, 0.0F, f1 == 0.0F ? engine2PropPitch * 20F : f1);
		mesh.chunkSetAngles("zPropPitch3", 0.0F, 0.0F, f1 == 0.0F ? engine3PropPitch * 20F : f1);
		mesh.chunkSetAngles("zPropPitch4", 0.0F, 0.0F, f1 == 0.0F ? engine4PropPitch * 20F : f1);
		f1 = 0.0F;
		if (engineRadiators[0] > 0.0F && engineRadiators[1] > 0.0F && engineRadiators[2] > 0.0F && engineRadiators[3] > 0.0F) {
			mesh.chunkSetAngles("zCowlFGOpen", 0.0F, 0.0F, engineRadiators[0] * -115F);
			f1 = -cvt(engineRadiators[0], 0.8F, 1.0F, 0.01F, 20F);
		} else if (engineRadiators[0] < 0.0F && engineRadiators[1] < 0.0F && engineRadiators[2] < 0.0F
				&& engineRadiators[3] < 0.0F) {
			mesh.chunkSetAngles("zCowlFGClose", 0.0F, 0.0F, engineRadiators[0] * 115F);
			f1 = cvt(-engineRadiators[0], 0.8F, 1.0F, 0.01F, 20F);
		} else {
			mesh.chunkSetAngles("zCowlFGClose", 0.0F, 0.0F, 0.0F);
			mesh.chunkSetAngles("zCowlFGOpen", 0.0F, 0.0F, 0.0F);
		}
		mesh.chunkSetAngles("zCowlFlaps1", 0.0F, 0.0F, f1 == 0.0F ? engineRadiators[0] * 20F : f1);
		mesh.chunkSetAngles("zCowlFlaps2", 0.0F, 0.0F, f1 == 0.0F ? engineRadiators[1] * 20F : f1);
		mesh.chunkSetAngles("zCowlFlaps3", 0.0F, 0.0F, f1 == 0.0F ? engineRadiators[2] * 20F : f1);
		mesh.chunkSetAngles("zCowlFlaps4", 0.0F, 0.0F, f1 == 0.0F ? engineRadiators[3] * 20F : f1);
		mesh.chunkSetAngles("zLandingLights", 0.0F, 0.0F, fm.AS.bLandingLightOn ? -30F : 0.0F);
		mesh.chunkSetAngles("zCockpitLights", 0.0F, cockpitLightControl ? -60F : 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[2] = -(pictElev = 0.85F * pictElev + 0.15F * fm.CT.getElevator()) * 0.1F;
		Cockpit.ypr[0] = (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 45F;
		mesh.chunkSetLocate("zSteering1", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetLocate("zSteering2", Cockpit.xyz, Cockpit.ypr);
		resetYPRmodifier();
		Cockpit.xyz[2] = -fm.CT.getRudder() * 0.1F;
		mesh.chunkSetLocate("zPedalsL", Cockpit.xyz, Cockpit.ypr);
		Cockpit.xyz[2] = fm.CT.getRudder() * 0.1F;
		mesh.chunkSetLocate("zPedalsR", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("zBrakesL", -fm.CT.getBrakeL() * 15F, 0.0F, 0.0F);
		mesh.chunkSetAngles("zBrakesR", -fm.CT.getBrakeR() * 15F, 0.0F, 0.0F);
		mesh.chunkSetAngles("zAutopilotGang", 0.0F, 0.0F, fm.CT.courseAutopilotControl ? 20F : 0.0F);
		mesh.chunkSetAngles("zGear", gearsLever, 0.0F, 0.0F);
		mesh.chunkSetAngles("zFlaps", flapsLever, 0.0F, 0.0F);
		mesh.chunkSetAngles("z_AHorZ2", 0.0F, -fm.Or.getKren(), 0.0F);
		resetYPRmodifier();
		xyz[2] = cvt(fm.Or.getTangage(), -45F, 45F, 0.0365F, -0.0365F);
		mesh.chunkSetLocate("z_AHorZ1", xyz, ypr);
		if (!dialsR2Dmg) {
			mesh.chunkSetAngles(
					"z_ManP1",
					0.0F,
					pictManf1 = 0.9F * pictManf1 + 0.1F
							* cvt(fm.EI.engines[0].getManifoldPressure(), 0.334F, 2.00526F, 0.0F, -322F), 0.0F);
			mesh.chunkSetAngles(
					"z_ManP2",
					0.0F,
					pictManf2 = 0.9F * pictManf2 + 0.1F
							* cvt(fm.EI.engines[1].getManifoldPressure(), 0.334F, 2.00526F, 0.0F, -322F), 0.0F);
		}
		mesh.chunkSetAngles("z_ManP3", 0.0F,
				pictManf3 = 0.9F * pictManf3 + 0.1F
						* cvt(fm.EI.engines[2].getManifoldPressure(), 0.334F, 2.00526F, 0.0F, -322F), 0.0F);
		mesh.chunkSetAngles("z_ManP4", 0.0F,
				pictManf4 = 0.9F * pictManf4 + 0.1F
						* cvt(fm.EI.engines[3].getManifoldPressure(), 0.334F, 2.00526F, 0.0F, -322F), 0.0F);
		boolean flag = false;
		float f2 = cvt(fm.M.fuel, 0.0F, 100F, 0.0F, 1.0F);
		for (int i = 0; i < 4; i++) {
			mesh.chunkVisible(
					"xPropL" + (i + 1) + "Green",
					fm.EI.engines[i].getElPropPos() <= 0.0F && fm.EI.engines[i].getControlPropDelta() == -1
							|| fm.EI.engines[i].getElPropPos() >= 1.0F && fm.EI.engines[i].getControlPropDelta() == 1);
			if (fm.EI.engines[i].getStage() > 0 && fm.EI.engines[i].getStage() < 4)
				mesh.chunkSetAngles("zStarter" + (i + 1), 0.0F, 0.0F, -25F);
			else if (fm.EI.engines[i].getStage() >= 4 && fm.EI.engines[i].getStage() < 6)
				mesh.chunkSetAngles("zStarter" + (i + 1), 0.0F, 0.0F, 25F);
			else
				mesh.chunkSetAngles("zStarter" + (i + 1), 0.0F, 0.0F, 0.0F);
			float f5 = fm.EI.engines[i].getRPM();
			if (i < 2 && !dialsR1Dmg || i > 1 && !dialsR2Dmg)
				mesh.chunkSetAngles("z_RPM" + (i + 1), 0.0F, -cvt(f5, 0.0F, 4500F, 0.0F, 323F), 0.0F);
			if (fm.Or.getKren() < -110F || fm.Or.getKren() > 110F)
				rpmGeneratedPressure[i] = rpmGeneratedPressure[i] - 2.0F;
			else if (f5 < rpmGeneratedPressure[i])
				rpmGeneratedPressure[i] = rpmGeneratedPressure[i] - (rpmGeneratedPressure[i] - f5) * 0.01F;
			else
				rpmGeneratedPressure[i] = rpmGeneratedPressure[i] + (f5 - rpmGeneratedPressure[i]) * 0.001F;
			if (rpmGeneratedPressure[i] < 800F)
				oilPressure[i] = cvt(rpmGeneratedPressure[i], 0.0F, 800F, 0.0F, 4F);
			else if (rpmGeneratedPressure[i] < 2000F)
				oilPressure[i] = cvt(rpmGeneratedPressure[i], 800F, 2000F, 4F, 5F);
			else
				oilPressure[i] = cvt(rpmGeneratedPressure[i], 2000F, 3000F, 5F, 5.8F);
			float f7 = 0.0F;
			if (fm.EI.engines[i].tOilIn > 90F)
				f7 = cvt(fm.EI.engines[i].tOilIn, 90F, 110F, 1.1F, 1.5F);
			else if (fm.EI.engines[i].tOilIn < 50F)
				f7 = cvt(fm.EI.engines[i].tOilIn, 0.0F, 50F, 2.0F, 0.9F);
			else
				f7 = cvt(fm.EI.engines[i].tOilIn, 50F, 90F, 0.9F, 1.1F);
			float f9 = f7 * fm.EI.engines[i].getReadyness() * oilPressure[i];
			mesh.chunkSetAngles("z_OilP" + (i + 1), 0.0F, -cvt(f9, 0.0F, 7F, 0.0F, 300F), 0.0F);
			if (i < 2 && !dialsR1Dmg || i > 1 && !dialsR2Dmg)
				mesh.chunkSetAngles("z_FuelP" + (i + 1), 0.0F, -cvt(rpmGeneratedPressure[i], 0.0F, 2000F * f2, 0.0F, 200F),
						0.0F);
			if (i > 1 && !dialsR1Dmg || i < 2 && !dialsR2Dmg)
				mesh.chunkSetAngles("z_ECylT" + (i + 1), 0.0F, -cvt(fm.EI.engines[i].tWaterOut, 0.0F, 300F, 0.0F, 41F), 0.0F);
			if (!dialsR1Dmg)
				mesh.chunkSetAngles("z_OilT" + (i + 1), 0.0F, -cvt(fm.EI.engines[i].tOilIn, 30F, 150F, 0.0F, 41F), 0.0F);
			if (fm.EI.engines[i].getStage() > 0 && fm.EI.engines[i].getStage() < 6)
				flag = true;
		}

		if (flag)
			mesh.chunkSetAngles("zBattery", 0.0F, -60F, 0.0F);
		else
			mesh.chunkSetAngles("zBattery", 0.0F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Hour", 0.0F, -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		mesh.chunkSetAngles("Z_Min", 0.0F, -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("Z_Sec", 0.0F, -cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("z_Flap", 0.0F, -90F * fm.CT.getFlap(), 0.0F);
		if (fm.CT.bHasBrakeControl) {
			mesh.chunkSetAngles("z_HBrkI", 0.0F, -cvt(fm.CT.getBrakeL(), 0.0F, 1.5F, 0.0F, 160F), 0.0F);
			mesh.chunkSetAngles("z_HBrkO", 0.0F, 0.0F, cvt(fm.CT.getBrakeR(), 0.0F, 1.5F, 0.0F, 160F));
		}
		mesh.chunkSetAngles("Z_HydSys", 0.0F, fm.Gears.bIsHydroOperable ? -165.5F : 0.0F, 0.0F);
		if (!dialsL1Dmg) {
			float f3 = 0.25F * fm.EI.engines[0].getRPM() + 0.25F * fm.EI.engines[1].getRPM() + 0.25F
					* fm.EI.engines[2].getRPM() + 0.25F * fm.EI.engines[3].getRPM();
			f3 = 2.5F * (float)Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f3))));
			mesh.chunkSetAngles("Z_Suction", 0.0F, -cvt(f3, 0.0F, 10F, 0.0F, 280F), 0.0F);
		}
		mesh.chunkSetAngles("zMagneto1", 0.0F, -30F * (float)fm.EI.engines[0].getControlMagnetos(), 0.0F);
		mesh.chunkSetAngles("zMagneto2", 0.0F, -30F * (float)fm.EI.engines[1].getControlMagnetos(), 0.0F);
		mesh.chunkSetAngles("zMagneto3", 0.0F, -30F * (float)fm.EI.engines[2].getControlMagnetos(), 0.0F);
		mesh.chunkSetAngles("zMagneto4", 0.0F, -30F * (float)fm.EI.engines[3].getControlMagnetos(), 0.0F);
		if (fm.EI.engines[0].getControlMagnetos() == 0 && fm.EI.engines[1].getControlMagnetos() == 0
				&& fm.EI.engines[2].getControlMagnetos() == 0 && fm.EI.engines[3].getControlMagnetos() == 0)
			mesh.chunkSetAngles("zIgnMaster", 0.0F, -25F, 0.0F);
		else
			mesh.chunkSetAngles("zIgnMaster", 0.0F, 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[2] = 0.0F;
		if (fm.AS.externalStoresDropped)
			Cockpit.xyz[2] = 0.03F;
		mesh.chunkSetLocate("zBombRelease", Cockpit.xyz, Cockpit.ypr);
		if (fm.CT.WeaponControl[3] && bombReleaseTimer == 4000F)
			bombReleaseTimer -= Time.tickLenFms() * f;
		if (bombReleaseTimer < 4000F && bombReleaseTimer > 0.0F) {
			bombReleaseTimer -= Time.tickLenFms() * f;
			mesh.chunkVisible("xBombRelGreen", true);
		} else {
			mesh.chunkVisible("xBombRelGreen", false);
		}
		mesh.chunkSetAngles("zSupercharger1", 90F * interp(setNew.supercharger1, setOld.supercharger1, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("zSupercharger2", 90F * interp(setNew.supercharger2, setOld.supercharger2, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("zSupercharger3", 90F * interp(setNew.supercharger3, setOld.supercharger3, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("zSupercharger4", 90F * interp(setNew.supercharger4, setOld.supercharger4, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("z_Comp", setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
		mesh.chunkSetAngles("z_GyComp", setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
		if (fm.EI.engines[0].getControlFeather() != 0 || fm.EI.engines[1].getControlFeather() != 0
				|| fm.EI.engines[2].getControlFeather() != 0 || fm.EI.engines[3].getControlFeather() != 0)
			mesh.chunkSetAngles("zFeatherCover", 0.0F, 0.0F, -55F);
		resetYPRmodifier();
		Cockpit.xyz[1] = (float)fm.EI.engines[0].getControlFeather() * 0.01F;
		mesh.chunkSetLocate("zFeather1", Cockpit.xyz, Cockpit.ypr);
		resetYPRmodifier();
		Cockpit.xyz[1] = (float)fm.EI.engines[1].getControlFeather() * 0.01F;
		mesh.chunkSetLocate("zFeather2", Cockpit.xyz, Cockpit.ypr);
		resetYPRmodifier();
		Cockpit.xyz[1] = (float)fm.EI.engines[2].getControlFeather() * 0.01F;
		mesh.chunkSetLocate("zFeather3", Cockpit.xyz, Cockpit.ypr);
		resetYPRmodifier();
		Cockpit.xyz[1] = (float)fm.EI.engines[3].getControlFeather() * 0.01F;
		mesh.chunkSetLocate("zFeather4", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("zAileronTScale", 180F * fm.CT.getTrimAileronControl(), 0.0F, 0.0F);
		mesh.chunkSetAngles("zAileronTWheel", 720F * fm.CT.getTrimAileronControl(), 0.0F, 0.0F);
		mesh.chunkSetAngles("zElevTScale", 180F * fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
		mesh.chunkSetAngles("zElevTWheel", 720F * fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
		mesh.chunkSetAngles("zRudderTKnob", 0.0F, 1040F * fm.CT.getTrimRudderControl(), 0.0F);
		mesh.chunkSetAngles("zRudderTScale", 0.0F, 260F * fm.CT.getTrimRudderControl(), 0.0F);
		mesh.chunkVisible("xBDoorsRed", fm.CT.getBayDoor() == 1.0F);
		mesh.chunkVisible("xWLockGreen", fm.CT.getGearC() > 0.99F && fm.Gears.cgear && fm.CT.getGearR() > 0.99F
				&& fm.Gears.rgear && fm.CT.getGearL() > 0.99F && fm.Gears.lgear);
		if (fm.AS.isAAFIAS)
			mesh.chunkVisible("xMBeaconRed", isOnBlindLandingMarker());
		mesh.chunkVisible("xGunButtonUp", fm.CT.WeaponControl[1]);
		mesh.chunkVisible("xGunButtonDn", !fm.CT.WeaponControl[1]);
		if (!dialsL1Dmg) {
			mesh.chunkSetAngles("z_AirTemp", 0.0F,
					cvt(Atmosphere.temperature((float)fm.Loc.z) - 273.15F, -45F, 45F, 55F, -55F), 0.0F);
			mesh.chunkSetAngles(
					"Z_MPH",
					0.0F,
					-floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 482.8F, 0.0F, 15F),
							speedometerScale), 0.0F);
			mesh.chunkSetAngles("z_RComp", 0.0F, -setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F);
			float f4 = ((B_24J100)aircraft()).getBombSightPDI();
			float f6 = Math.abs(f4);
			float f8 = floatindex(cvt(f6, 0.0F, 0.6666667F, 0.0F, 4F), pdiScale);
			float f10 = 1.0F;
			if (f4 > 0.0F)
				f10 = -1F;
			mesh.chunkSetAngles("z_PDI", 0.0F, f8 * f10, 0.0F);
		} else {
			mesh.chunkSetAngles("z_RComp", 0.0F, -fm.Or.getRoll() + 180F, 0.0F);
		}
		if (!dialsL2Dmg) {
			mesh.chunkSetAngles("z_Alt1", 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F),
					0.0F);
			mesh.chunkSetAngles("z_Alt2", 0.0F,
					-cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F);
			mesh.chunkSetAngles("z_Alt3", 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 360F),
					0.0F);
			mesh.chunkSetAngles("Z_Climb", 0.0F, -floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale),
					0.0F);
			w.set(fm.getW());
			fm.Or.transform(w);
			mesh.chunkSetAngles("z_TurnB", 0.0F, -cvt(w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
			mesh.chunkSetAngles("z_TurnBB", 0.0F, -cvt(getBall(6D), -6F, 6F, -9.7F, 9.7F), 0.0F);
		} else {
			dialDmg1 += f * 3F;
			mesh.chunkSetAngles("z_Alt2", 0.0F, dialDmg1, 0.0F);
			mesh.chunkSetAngles("z_TurnBB", 0.0F,
					-cvt(getBall(0.10000000000000001D) - fm.Or.getKren(), -10F, 10F, -9.7F, 9.7F), 0.0F);
		}
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 2) != 0) {
			mesh.chunkVisible("PnlDialsR1", false);
			mesh.chunkVisible("PnlDialsR1_Dmg", true);
			dialsR1Dmg = true;
		}
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("PnlDialsL1", false);
			mesh.chunkVisible("PnlDialsL1_Dmg", true);
			dialsL1Dmg = true;
		}
		if ((fm.AS.astateCockpitState & 1) != 0) {
			mesh.chunkVisible("PnlDialsL2", false);
			mesh.chunkVisible("PnlDialsL2_Dmg", true);
			dialsL2Dmg = true;
		}
		if ((fm.AS.astateCockpitState & 4) != 0) {
			mesh.chunkVisible("PnlDialsL2", false);
			mesh.chunkVisible("PnlDialsL2_Dmg", true);
			dialsL2Dmg = true;
		}
		if ((fm.AS.astateCockpitState & 0x10) != 0) {
			mesh.chunkVisible("PnlDialsR2", false);
			mesh.chunkVisible("PnlDialsR2_Dmg", true);
			dialsR2Dmg = true;
		}
		retoggleLight();
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
		mesh.materialReplace("Gloss2D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Pilot1"));
		mesh.materialReplace("Pilot1", mat);
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl) {
			setNightMats(true);
			light1.light.setEmit(0.4F, 1.0F);
		} else {
			setNightMats(false);
			light1.light.setEmit(0.0F, 0.0F);
		}
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

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			HierMesh hiermesh = aircraft().hierMesh();
			hiermesh.chunkVisible("Blister1_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		HierMesh hiermesh = aircraft().hierMesh();
		if (!hiermesh.isChunkVisible("Nose_Cap"))
			hiermesh.chunkVisible("Blister1_D0", true);
		super.doFocusLeave();
	}

	public boolean isViewRight() {
		Loc loc = new Loc();
		Loc loc1 = new Loc();
		HookPilot.current.computePos(this, loc, loc1);
		float f = loc1.getOrient().getYaw();
		if (f < 0.0F)
			isSlideRight = true;
		else
			isSlideRight = false;
		return isSlideRight;
	}

	private boolean bNeedSetUp;
	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private float pictAiler;
	private float pictElev;
	private float gearsLever;
	private float gears;
	private float flapsLever;
	private float flaps;
	private float pictManf1;
	private float pictManf2;
	private float pictManf3;
	private float pictManf4;
	private float engine1PropPitch;
	private float engine2PropPitch;
	private float engine3PropPitch;
	private float engine4PropPitch;
	private float engineRadiators[] = { 0.0F, 0.0F, 0.0F, 0.0F };
	private float enginePrevRadiators[] = { 0.0F, 0.0F, 0.0F, 0.0F };
	private float rpmGeneratedPressure[] = { 0.0F, 0.0F, 0.0F, 0.0F };
	private float oilPressure[] = { 0.0F, 0.0F, 0.0F, 0.0F };
	private float bombReleaseTimer;
	private boolean isSlideRight;
	private LightPointActor light1;
	private boolean dialsR1Dmg;
	private boolean dialsR2Dmg;
	private boolean dialsL1Dmg;
	private boolean dialsL2Dmg;
	private float dialDmg1;
	private static final float pdiScale[] = { 0.0F, 21.75F, 32.25F, 38.25F, 45F };
	private static final float speedometerScale[] = { 0.0F, 8F, 17.5F, 38F, 65F, 94F, 126.5F, 162.5F, 198F, 233F, 266F, 280.5F,
			296F, 312F, 328F, 344F };
	private static final float variometerScale[] = { -180F, -157F, -130F, -108F, -84F, -46.5F, 0.0F, 46.5F, 84F, 108F, 130F,
			157F, 180F };
	private static final float mixScale[] = { 0.0F, 17F, 44F, 105F };

	static {
		Property.set(CockpitB_24J100_Copilot.class, "aiTuretNum", -4);
		Property.set(CockpitB_24J100_Copilot.class, "weaponControlNum", 10);
		Property.set(CockpitB_24J100_Copilot.class, "astatePilotIndx", 1);
		Property.set(CockpitB_24J100_Copilot.class, "normZNs", new float[] { 0.75F, 1.58F, 1.27F, 0.75F });
	}

}
