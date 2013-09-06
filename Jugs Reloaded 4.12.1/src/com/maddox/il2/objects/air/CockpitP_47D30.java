package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.Time;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;
import com.maddox.sound.SoundFX;
import com.maddox.sound.SoundPreset;

public class CockpitP_47D30 extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (fm != null) {
				if (bNeedSetUp) {
					initLocalSounds();
					loadLocalSoundsFX();
					setReticle();
					bNeedSetUp = false;
				}
				updateSound();
				reflectCockpitMats();
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				setNew.throttle = 0.85F * setOld.throttle + fm.CT.PowerControl * 0.15F;
				setNew.prop = 0.85F * setOld.prop + fm.CT.getStepControl() * 0.15F;
				setNew.stage = 0.85F * setOld.stage + (float) fm.EI.engines[0].getControlCompressor() * 0.15F;
				setNew.mix = 0.85F * setOld.mix + fm.EI.engines[0].getControlMix() * 0.15F;
				setNew.manifold = 0.7F * setOld.manifold + 0.3F * fm.EI.engines[0].getManifoldPressure();
				setNew.altimeter = fm.getAltitude();
				if (Math.abs(fm.Or.getKren()) < 45F)
					setNew.azimuth = (35F * setOld.azimuth + -fm.Or.getYaw()) / 36F;
				if (setOld.azimuth > 270F && setNew.azimuth < 90F)
					setOld.azimuth -= 360F;
				if (setOld.azimuth < 90F && setNew.azimuth > 270F)
					setOld.azimuth += 360F;
				setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
				setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
				if (World.cur().diffCur.ComplexEManagement)
					pictTurba = 0.97F * pictTurba + 0.03F * (0.9F * fm.EI.engines[0].getPowerOutput() * cvt((float) Math.pow(setNew.stage, 1.5D), 0.0F, 27F, 0.0F, 1.0F) + 0.2F * cvt((float) Math.sqrt(fm.EI.engines[0].getRPM()), 0.0F, 45F, 0.0F, 1.0F));
				else
					pictTurba = 0.97F * pictTurba + 0.03F * (0.5F * fm.EI.engines[0].getPowerOutput() + 0.5F * cvt(fm.EI.engines[0].getRPM(), 0.0F, 2000F, 0.0F, 1.0F));
				sfxCanopyWind(!bCanopyClosed);
				sfxTurboSound(fm.EI.engines[0].getStage() == 6);
				if (bPowerON)
					fzp = fzp >= 1.0F ? 1.0F : fzp + 0.05F;
				else
					fzp = fzp <= 0.0F ? 0.0F : fzp - 0.005F;
				if (fm.getAltitude() > 3000F) {
					CockpitP_47D30.oxyp = CockpitP_47D30.oxyp <= 0.0F ? 0.0F : CockpitP_47D30.oxyp - 1E-005F;
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

		private Variables() {
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

	public CockpitP_47D30() {
		super("3DO/Cockpit/P-47D-30/hier30.him", "p47cls");
		bTWlock = false;
		bCanopyClosed = true;
		bMusicPresent = false;
		bCanopyInit = false;
		bPowerON = true;
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		bNeedSetUp = true;
		pictAiler = 0.0F;
		pictElev = 0.0F;
		tmpP = new Point3d();
		tmpV = new Vector3d();
		light1 = new LightPointActor(new LightPoint(), new Point3d(-0.69999999999999996D, 0.14999999999999999D, 0.75D));
		light2 = new LightPointActor(new LightPoint(), new Point3d(-0.69999999999999996D, -0.14999999999999999D, 0.75D));
		light3 = new LightPointActor(new LightPoint(), new Point3d(-0.65000000000000002D, 0.26000000000000001D, 0.69999999999999996D));
		light4 = new LightPointActor(new LightPoint(), new Point3d(-0.65000000000000002D, -0.26000000000000001D, 0.69999999999999996D));
		light1.light.setColor(0.96F, 0.87F, 0.74F);
		light2.light.setColor(0.96F, 0.87F, 0.74F);
		light3.light.setColor(0.96F, 0.87F, 0.74F);
		light4.light.setColor(0.96F, 0.87F, 0.74F);
		light1.light.setEmit(0.0F, 0.0F);
		light2.light.setEmit(0.0F, 0.0F);
		light3.light.setEmit(0.0F, 0.0F);
		light4.light.setEmit(0.0F, 0.0F);
		pos.base().draw.lightMap().put("LAMPHOOK1", light1);
		pos.base().draw.lightMap().put("LAMPHOOK2", light2);
		pos.base().draw.lightMap().put("LAMPHOOK3", light3);
		pos.base().draw.lightMap().put("LAMPHOOK4", light4);
		cockpitNightMats = (new String[] { "prib1", "pribA", "pribB", "pribC", "prib4", "prib5", "prib6", "prib7", "pri14", "prib1_d1", "pribA_d1", "prib4_d1", "Frame08", "Frame64", "panel", "shkalb", "Frame20", "shkala" });
		resetNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		if (acoustics != null)
			acoustics.globFX = new ReverbFXRoom(0.45F);
	}

	public void reflectWorldToInstruments(float f) {
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.65F);
		mesh.chunkSetLocate("Body_blister", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetLocate("Glass1", Cockpit.xyz, Cockpit.ypr);
		changeSound();
		sfxFlapSound();
		if (World.cur().diffCur.ComplexEManagement)
			adjustMix(fm.EI.engines[0].getControlMix());
		mesh.chunkSetAngles("armPedalL", 0.0F, -15F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("armPedalR", 0.0F, 15F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("PedalL", 0.0F, 15F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("PedalR", 0.0F, -15F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("Stick", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 20F, (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 16F);
		if (World.cur().diffCur.ComplexEManagement)
			mesh.chunkSetAngles("supercharge", 0.0F, 6F * setNew.stage, 0.0F);
		else
			mesh.chunkSetAngles("supercharge", 0.0F, 60F * setNew.throttle, 0.0F);
		mesh.chunkSetAngles("throttle", 0.0F, 62F * setNew.throttle, 0.0F);
		mesh.chunkSetAngles("prop", 0.0F, 70F * setNew.prop, 0.0F);
		mesh.chunkSetAngles("mixtura", 0.0F, 55F * setNew.mix, 0.0F);
		mesh.chunkSetAngles("flaplever", 0.0F, 0.0F, 70F * fm.CT.FlapsControl);
		mesh.chunkSetAngles("zfuelR", 0.0F, floatindex(cvt(fm.M.fuel, 0.0F, 981F, 0.0F, 6F), fuelGallonsScale), 0.0F);
		mesh.chunkSetAngles("zfuelL", 0.0F, -floatindex(cvt(fm.M.fuel, 0.0F, 981F, 0.0F, 4F), fuelGallonsAuxScale), 0.0F);
		mesh.chunkSetAngles("zacceleration", 0.0F, cvt(fm.getOverload(), -4F, 12F, -77F, 244F), 0.0F);
		mesh.chunkSetAngles("zSpeed1a", 0.0F, floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), speedometerScale), 0.0F);
		mesh.chunkSetAngles("zclimb", 0.0F, floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
		w.set(fm.getW());
		fm.Or.transform(w);
		mesh.chunkSetAngles("zTurn1a", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
		mesh.chunkSetAngles("zSlide1a", 0.0F, cvt(getBall(7D), -7F, 7F, -10F, 10F), 0.0F);
		mesh.chunkSetAngles("zSlide1b", 0.0F, cvt(getBall(7D), -7F, 7F, -16F, 16F), 0.0F);
		mesh.chunkSetAngles("zManifold1a", 0.0F, cvt(setNew.manifold, 0.3386378F, 2.370465F, -115F, 205F), 0.0F);
		mesh.chunkSetAngles("z_Altimeter1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F);
		mesh.chunkSetAngles("z_Altimeter2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F);
		mesh.chunkSetAngles("z_Altimeter3", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("zRPM1a", 0.0F, cvt(fm.EI.engines[0].getRPM(), 0.0F, 4500F, -73.5F, 253.5F), 0.0F);
		mesh.chunkSetAngles("zClock1b", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		mesh.chunkSetAngles("zClock1a", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("zClock1c", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		if (fm.EI.engines[0].getStage() < 2) {
			mesh.chunkSetAngles("zhorizont1a", 0.0F, -30F, 0.0F);
			mesh.chunkSetLocate("zhorizont1b", new float[] { 0.0F, 0.0F, -0.01F }, new float[] { 0.0F, 0.0F, -10F });
			mesh.chunkVisible("Frame_64sc", true);
		} else {
			mesh.chunkSetAngles("zhorizont1a", 0.0F, -fm.Or.getKren(), 0.0F);
			resetYPRmodifier();
			Cockpit.xyz[2] = cvt(fm.Or.getTangage(), -45F, 45F, 0.0328F, -0.0328F);
			mesh.chunkSetLocate("zhorizont1b", Cockpit.xyz, Cockpit.ypr);
			mesh.chunkVisible("Frame_64sc", false);
		}
		mesh.chunkSetAngles("zturborpm1a", 0.0F, cvt(pictTurba, 0.0F, 2.0F, -18F, 198F), 0.0F);
		mesh.chunkSetAngles("zoiltemp1a", 0.0F, cvt(fm.EI.engines[0].tWaterOut, 0.0F, 350F, 4F, 90F), 0.0F);
		mesh.chunkSetAngles("zpressoil1a", 0.0F, cvt(0.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness(), 0.0F, 7.45F, 0.0F, 180F), 0.0F);
		mesh.chunkSetAngles("zpressfuel1a", 0.0F, cvt(fm.M.fuel > 1.0F ? 0.26F * fm.EI.engines[0].getReadyness() * fzp : 0.0F, 0.0F, 0.4F, 0.0F, -180F), 0.0F);
		mesh.chunkSetAngles("zAzimuth1a", 0.0F, cvt(fm.Or.getTangage(), -5F, 5F, -5F, 5F), 0.0F);
		mesh.chunkSetAngles("zAzimuth1b", 0.0F, 90F - setNew.azimuth, 0.0F);
		mesh.chunkSetAngles("zMagAzimuth1a", 0.0F, cvt(fm.Or.getTangage(), -65F, 65F, -65F, 65F), 0.0F);
		mesh.chunkSetAngles("zMagAzimuth1b", -90F + setNew.azimuth, 0.0F, 0.0F);
		mesh.chunkSetAngles("ztemp1a", 0.0F, cvt((Atmosphere.temperature((float) fm.Loc.z) - 273.15F) + 25F * fm.EI.engines[0].getPowerOutput(), -50F, 150F, 0.0F, 105F), 0.0F);
		mesh.chunkSetAngles("zpress1a", 0.0F, cvt(fm.EI.engines[0].tOilOut, -50F, 150F, -15F, 115F), 0.0F);
		float f1 = fm.EI.engines[0].getRPM();
		f1 = 1.8F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
		mesh.chunkSetAngles("zpresswater1a", 0.0F, cvt(f1, 0.0F, 5F, 0.0F, 300F), 0.0F);
		float f2 = fm.EI.engines[0].getRPM();
		f2 = 550F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f2))));
		mesh.chunkSetAngles("zfas1a", 0.0F, cvt(f2, 0.0F, 2000F, 0.0F, 180F), 0.0F);
		mesh.chunkSetAngles("zoxipress1a", 0.0F, cvt(oxyp * 400F, 0.0F, 500F, 0.0F, 180F), 0.0F);
		float f3 = fm.EI.engines[0].getRPM();
		f3 = 1.52F * (float) Math.sqrt(Math.sqrt(Math.sqrt(f3)));
		mesh.chunkSetAngles("zsuction1a", 0.0F, cvt(f3, 0.0F, 10F, 0.0F, 300F), 0.0F);
		float f4 = 0.0F;
		int i = fm.EI.engines[0].getStage();
		f4 = i != 2 ? 0.0F : 75F;
		if (fm.AS.bLandingLightOn)
			f4 += 35F;
		if (fm.AS.bNavLightsOn)
			f4 += 5F;
		if (cockpitLightControl)
			f4 += 3F;
		if (fm.AS.bShowSmokesOn)
			f4 += 5F;
		mesh.chunkSetAngles("zamper", 0.0F, cvt(f4 - cvt(fm.EI.engines[0].getRPM(), 150F, 2380F, 0.0F, 41.1F), 0.0F, 150F, 0.0F, 90F), 0.0F);
		mesh.chunkSetAngles("MagSwitch", 0.0F, 30F * (float) fm.EI.engines[0].getControlMagnetos(), 0.0F);
		float f5 = 0.0F;
		float f6 = P_47D30.bExtTank ? fm.M.fuel + 284F : fm.M.fuel;
		if (f6 < 705F)
			f5 = f6 <= 350F ? -180F : -90F;
		mesh.chunkSetAngles("fuelsw1", f5, 0.0F, 0.0F);
		float f7 = 0.0F;
		if (P_47D30.bExtTank && f6 > 1006F)
			f7 = 90F;
		mesh.chunkSetAngles("fuelsw2", f7, 0.0F, 0.0F);
		if (bPowerON) {
			mesh.chunkVisible("Z_Red1", fm.CT.getGear() < 0.05F || !fm.Gears.lgear || !fm.Gears.rgear);
			mesh.chunkVisible("Z_Green1", fm.CT.getGear() > 0.95F);
			mesh.chunkVisible("Z_Red2", fm.M.fuel / fm.M.maxFuel < 0.15F);
			mesh.chunkVisible("Z_WaterInjL1", fm.EI.engines[0].getControlAfterburner());
			boolean flag = false;
			if (World.cur().diffCur.ComplexEManagement) {
				if (pictTurba > 0.0F && fm.EI.engines[0].getControlCompressor() > 0)
					if (pictTurba < 0.145F || pictTurba > 1.085F) {
						flag = true;
					} else {
						if (iTurba++ > 60)
							iTurba = 0;
						flag = iTurba > 30;
					}
			} else if (pictTurba > 1.085F)
				flag = true;
			mesh.chunkVisible("Z_Red3", flag);
			mesh.chunkVisible("Z_Green2", false);
			mesh.chunkVisible("Z_Red4", fm.AS.bShowSmokesOn);
			if (fm.getAltitude() > 3000F) {
				mesh.chunkVisible("Z_Oxy_ON", iOxy < 45);
				mesh.chunkVisible("Z_Oxy_OFF", iOxy > 46 && iOxy < 89);
			} else {
				mesh.chunkVisible("Z_Oxy_ON", false);
				mesh.chunkVisible("Z_Oxy_OFF", false);
			}
		} else {
			mesh.chunkVisible("Z_Red1", false);
			mesh.chunkVisible("Z_Green1", false);
			mesh.chunkVisible("Z_Red2", false);
			mesh.chunkVisible("Z_WaterInjL1", false);
			mesh.chunkVisible("Z_Red3", false);
			mesh.chunkVisible("Z_Green2", false);
			mesh.chunkVisible("Z_Red4", false);
		}
		if (bNavLightsState != fm.AS.bNavLightsOn) {
			sfxClick(1);
			bNavLightsState = fm.AS.bNavLightsOn;
		}
		if (bLandingLightState != fm.AS.bLandingLightOn) {
			sfxClick(1);
			bLandingLightState = fm.AS.bLandingLightOn;
		}
		if (iMagnetosState != fm.EI.engines[0].getControlMagnetos()) {
			sfxClick(2);
			iMagnetosState = fm.EI.engines[0].getControlMagnetos();
		}
		if (Main3D.cur3D().isViewMirror())
			mesh.chunkVisible("MirrorFrame", true);
		else
			mesh.chunkVisible("MirrorFrame", false);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 2) != 0) {
			mesh.chunkVisible("Z_Holes1_D1", true);
			mesh.chunkVisible("pricel", false);
			mesh.chunkVisible("pricel_d1", true);
			mesh.chunkVisible("Z_Z_RETIC27", false);
			mesh.chunkVisible("Z_Z_MASK", false);
		}
		if ((fm.AS.astateCockpitState & 1) != 0)
			mesh.chunkVisible("Z_Holes1_D1", true);
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("pribors1", false);
			mesh.chunkVisible("pribors1_d1", true);
			mesh.chunkVisible("zamper", false);
			mesh.chunkVisible("zAzimuth1a", false);
			mesh.chunkVisible("zAzimuth1b", false);
			mesh.chunkVisible("zSpeed1a", false);
			mesh.chunkVisible("zacceleration", false);
			mesh.chunkVisible("zMagAzimuth1a", false);
			mesh.chunkVisible("zMagAzimuth1b", false);
			mesh.chunkVisible("zpresswater1a", false);
			mesh.chunkVisible("zclimb", false);
			mesh.chunkVisible("zRPM1a", false);
			mesh.chunkVisible("zoiltemp1a", false);
			mesh.chunkVisible("zturbormp1a", false);
			mesh.chunkVisible("zfas1a", false);
			mesh.chunkVisible("zoxipress1a", false);
		}
		if ((fm.AS.astateCockpitState & 4) == 0)
			;
		if ((fm.AS.astateCockpitState & 8) != 0)
			mesh.chunkVisible("Z_Holes2_D1", true);
		if ((fm.AS.astateCockpitState & 0x80) != 0)
			mesh.chunkVisible("Z_OilSplats_D1", true);
		if ((fm.AS.astateCockpitState & 0x10) == 0)
			;
		if ((fm.AS.astateCockpitState & 0x20) != 0) {
			mesh.chunkVisible("pribors2", false);
			mesh.chunkVisible("pribors2_d1", true);
			mesh.chunkVisible("zClock1b", false);
			mesh.chunkVisible("zClock1a", false);
			mesh.chunkVisible("zfuelR", false);
			mesh.chunkVisible("zfuelL", false);
			mesh.chunkVisible("zsuction1a", false);
			mesh.chunkVisible("zTurn1a", false);
			mesh.chunkVisible("zSlide1a", false);
			mesh.chunkVisible("zhorizont1a", false);
			mesh.chunkVisible("zAlt1a", false);
			mesh.chunkVisible("zAlt1b", false);
			mesh.chunkVisible("zpressfuel1a", false);
			mesh.chunkVisible("zpressoil1a", false);
			mesh.chunkVisible("ztempoil1a", false);
			mesh.chunkVisible("zManifold1a", false);
		}
		retoggleLight();
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		float f = 0.75F;
		String s = "glass";
		int i = mesh.materialFind(s);
		if (cockpitLightControl) {
			setNightMats(true);
			light1.light.setEmit(1.0F * f, 0.5F * f);
			light2.light.setEmit(1.0F * f, 0.5F * f);
			light3.light.setEmit(1.0F * f, 0.5F * f);
			light4.light.setEmit(1.0F * f, 0.5F * f);
			mesh.materialReplace(i, "glass__night");
		} else {
			setNightMats(false);
			light1.light.setEmit(0.0F, 0.0F);
			light2.light.setEmit(0.0F, 0.0F);
			light3.light.setEmit(0.0F, 0.0F);
			light4.light.setEmit(0.0F, 0.0F);
			mesh.materialReplace(i, "glass");
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

	private void resetNightMats(boolean flag) {
		if (cockpitNightMats != null) {
			for (int i = 0; i < cockpitNightMats.length; i++) {
				int j = mesh.materialFind(cockpitNightMats[i] + "_night");
				if (j < 0) {
					System.out.println("*** Did not find " + cockpitNightMats[i] + "_night");
					continue;
				}
				Mat mat = mesh.material(j);
				if (mat.isValidLayer(0)) {
					mat.setLayer(0);
					mat.set((byte) 10, 1.0F);
					mat.set((short) 0, flag);
				}
			}

		}
	}

	private void adjustMix(float f) {
		if (f < 0.8F)
			f = 0.0F;
		fm.EI.setMix(f);
		fm.CT.setMixControl(f);
	}

	private void loadLocalSoundsFX() {
		if (sounds != null) {
			SoundFX soundfx = sounds[15];
			if (soundfx == null) {
				soundfx1 = aircraft().newSound(sfxPreset, false, false);
				if (soundfx1 != null) {
					soundfx1.setParent(aircraft().getRootFX());
					sounds[15] = soundfx1;
					soundfx1.setUsrFlag(15);
					soundfx1.setPosition(new Point3d(1.0D, 0.0D, 0.0D));
					sndWind = sounds[15];
					System.out.println("*** Canopy wind sound loaded");
				}
			}
			soundfx = sounds[14];
			if (soundfx == null) {
				soundfx1 = aircraft().newSound(sfxPreset, false, false);
				if (soundfx1 != null) {
					soundfx1.setParent(aircraft().getRootFX());
					sounds[14] = soundfx1;
					soundfx1.setUsrFlag(14);
					soundfx1.setPosition(new Point3d(0.5D, 0.0D, -0.5D));
					sndFlaps = sounds[14];
					System.out.println("*** Flaps sound loaded");
				}
			}
			soundfx = sounds[13];
			if (soundfx == null) {
				soundfx1 = aircraft().newSound(sfxPreset, false, false);
				if (soundfx1 != null) {
					soundfx1.setParent(aircraft().getRootFX());
					sounds[13] = soundfx1;
					soundfx1.setUsrFlag(13);
					soundfx1.setPosition(new Point3d(0.5D, 0.0D, -0.5D));
					sndFlapsEnd = sounds[13];
					System.out.println("*** FlapsEnd sound loaded");
				}
			}
			soundfx = sounds[12];
			if (soundfx == null) {
				soundfx1 = aircraft().newSound(sfxPreset, false, false);
				if (soundfx1 != null) {
					soundfx1.setParent(aircraft().getRootFX());
					sounds[12] = soundfx1;
					soundfx1.setUsrFlag(12);
					soundfx1.setPosition(new Point3d(0.0D, 0.0D, -1D));
					((P_47X) aircraft()).soundGearDn = sounds[12];
					System.out.println("*** GearDn sound loaded");
				}
			}
			soundfx = sounds[11];
			if (soundfx == null) {
				soundfx1 = aircraft().newSound(sfxPreset, false, false);
				if (soundfx1 != null) {
					soundfx1.setParent(aircraft().getRootFX());
					sounds[11] = soundfx1;
					soundfx1.setUsrFlag(11);
					soundfx1.setPosition(new Point3d(0.0D, 0.0D, -1D));
					((P_47X) aircraft()).soundGearUp = sounds[11];
					System.out.println("*** GearUp sound loaded");
				}
			}
			soundfx = sounds[10];
			if (soundfx == null) {
				soundfx1 = aircraft().newSound(sfxPreset, false, false);
				if (soundfx1 != null) {
					soundfx1.setParent(aircraft().getRootFX());
					sounds[10] = soundfx1;
					soundfx1.setUsrFlag(10);
					soundfx1.setPosition(new Point3d(0.0D, 0.0D, -1D));
					((P_47X) aircraft()).soundWheels = sounds[10];
					System.out.println("*** GearUp sound loaded");
				}
			}
			soundfx = sounds[8];
			if (soundfx == null) {
				soundfx1 = aircraft().newSound(sfxPreset, false, false);
				if (soundfx1 != null) {
					soundfx1.setParent(aircraft().getRootFX());
					sounds[8] = soundfx1;
					soundfx1.setUsrFlag(8);
					soundfx1.setPosition(new Point3d(0.5D, 0.0D, -0.5D));
					sndTurbo = sounds[8];
					System.out.println("*** Turbo sound loaded");
				}
			}
		}
	}

	private void sfxFlapSound() {
		if (((P_47X) aircraft()).bFlaps) {
			if (sndFlaps != null)
				sndFlaps.setPlay(true);
			iFlapEndCycles = fm.CT.FlapsControl != 0.0F ? 10 : 20;
		}
		if (((P_47X) aircraft()).bFlapsEnd) {
			if (sndFlapsEnd != null)
				sndFlapsEnd.setPlay(true);
			if (iFlapEndCycles-- == 0) {
				if (sndFlaps != null)
					sndFlaps.setPlay(false);
				((P_47X) aircraft()).bFlapsEnd = false;
			}
		}
	}

	private void sfxCanopyWind(boolean flag) {
		if (sndWind != null) {
			vol = fm.getSpeedKMH() / 180F;
			vol = vol <= 1.0F ? vol : 1.0F;
			sndWind.setVolume(vol);
			sndWind.setPlay(flag);
		}
	}

	private void sfxTurboSound(boolean flag) {
		if (sndTurbo != null) {
			vol = pictTurba * 0.5F + 0.2F;
			vol = vol <= 1.0F ? vol : 1.0F;
			sndTurbo.setVolume(vol);
			pitch = pictTurba * 2.0F + 1.0F;
			sndTurbo.setPitch(pitch);
			sndTurbo.setPlay(flag);
		}
	}

	public void destroy() {
		if (isDestroyed())
			return;
		super.destroy();
		if (sndWind != null)
			sndWind.cancel();
		if (sndFlaps != null)
			sndFlaps.cancel();
		if (sndFlapsEnd != null)
			sndFlapsEnd.cancel();
		if (sndTurbo != null)
			sndTurbo.cancel();
	}

	private void setReticle() {
		UserCfg usercfg = World.cur().userCfg;
		String s = usercfg.getSkin("P-47D-30");
		System.out.println("*** Skin Loaded: " + s);
		String s1 = "ReticleMk8";
		int i = mesh.materialFind(s1);
		System.out.println("*** Material Found: " + i);
		if (i > 0 && s != null) {
			if (s.regionMatches(0, "Navy_", 0, 5))
				mesh.materialReplace(i, "ReticleMk8n");
			if (s.regionMatches(0, "Ground_", 0, 7))
				mesh.materialReplace(i, "ReticleMk8g");
		}
		String s2 = aircraft().getRegiment().country();
		System.out.println("*** Country Found: " + s2);
	}

	protected void reflectCockpitMats() {
		stage = fm.EI.engines[0].getStage();
		if (stage > 0 && stage < 4 && !bEngage && !bEnergize) {
			mesh.materialReplace(mesh.materialFind("Texture21s"), "Texture21sd");
			bEnergize = true;
			bEngage = false;
		}
		if (stage >= 4 && stage < 6 && (!bEngage) & bEnergize) {
			mesh.materialReplace(mesh.materialFind("Texture21s"), "Texture21su");
			bEnergize = false;
			bEngage = true;
		}
		if ((stage == 0 || stage == 6) && (bEngage || bEnergize)) {
			mesh.materialReplace(mesh.materialFind("Texture21s"), "Texture21s");
			bEnergize = false;
			bEngage = false;
		}
		if (fm.EI.engines[0].getControlMagnetos() == 0 && bPowerON) {
			mesh.chunkVisible("Z_Z_RETICLE", false);
			int i = mesh.materialFind("Mk8window");
			if (i > 0)
				mesh.materialReplace(i, "Mk8window_off");
			i = mesh.materialFind("shkala");
			if (i > 0)
				mesh.materialReplace(i, "shkala_off");
			bPowerON = false;
			System.out.println("*** Electrics turned OFF");
		}
		if (fm.EI.engines[0].getControlMagnetos() > 0 && !bPowerON) {
			mesh.chunkVisible("Z_Z_RETICLE", true);
			int j = mesh.materialFind("Mk8window");
			if (j > 0)
				mesh.materialReplace(j, "Mk8window");
			j = mesh.materialFind("shkala");
			if (j > 0)
				mesh.materialReplace(j, "shkala");
			bPowerON = true;
			sfxStart(9);
			System.out.println("*** Electrics turned ON");
		}
		if (fm.Gears.bTailwheelLocked && !bTWlock) {
			int k = mesh.materialFind("wlock");
			if (k > 0)
				mesh.materialReplace(k, "wlock_on");
			bTWlock = true;
			System.out.println("*** Tail Wheel locked");
		}
		if (!fm.Gears.bTailwheelLocked && bTWlock) {
			int l = mesh.materialFind("wlock");
			if (l > 0)
				mesh.materialReplace(l, "wlock");
			bTWlock = false;
			System.out.println("*** Tail Wheel unlocked");
		}
	}

	private void initLocalSounds() {
		sfxPreset = null;
		sfxPreset = new SoundPreset("aircraft.cockpit_p47");
		sounds = new SoundFX[18];
		System.out.println("*** Local Sounds Loaded: aircraft.cockpit_p47.prs");
	}

	private void resetAcoustics(String s) {
		((P_47X) aircraft()).setAcoustics(null);
		acoustics = new Acoustics(s);
		acoustics.setParent(Engine.worldAcoustics());
		((P_47X) aircraft()).setAcoustics(acoustics);
		((P_47X) aircraft()).enableDoorSnd(true);
		acoustics.globFX = new ReverbFXRoom(0.45F);
		System.out.println("*** Acoustics Loaded:" + s);
	}

	private void updateSound() {
		if ((fm.CT.getCockpitDoor() == 0.0F || fm.CT.getCockpitDoor() == 1.0F) && Main3D.cur3D().isViewOutside() && !bMusicPresent && fm.isStationedOnGround()) {
			CmdEnv.top().exec("music PLAY");
			bMusicPresent = true;
			System.out.println("*** Music set to play outside");
		}
	}

	private void changeSound() {
		if (!bCanopyInit && fm.isTick(88, 0)) {
			CmdEnv.top().exec("music STOP");
			bCanopyInit = true;
			bMusicPresent = false;
			System.out.println("*** Music stopped on canopy state: " + fm.CT.getCockpitDoor());
		}
		if (fm.CT.getCockpitDoor() == 0.0F && !bCanopyClosed) {
			resetAcoustics("p47cls");
			bCanopyClosed = true;
			System.out.println("*** Canopy closed - music: " + bMusicPresent);
		}
		if (fm.CT.getCockpitDoor() == 1.0F && bCanopyClosed) {
			resetAcoustics("p47opn");
			bCanopyClosed = false;
			System.out.println("*** Canopy open - music: " + bMusicPresent);
		}
		if (fm.isStationedOnGround() && !Main3D.cur3D().isViewOutside()) {
			if (bMusicPresent && fm.CT.getCockpitDoor() == 0.0F) {
				CmdEnv.top().exec("music STOP");
				bMusicPresent = false;
				System.out.println("*** Music set to stop");
			}
			if (!bMusicPresent && fm.CT.getCockpitDoor() == 1.0F) {
				CmdEnv.top().exec("music PLAY");
				bMusicPresent = true;
				System.out.println("*** Music set to play");
			}
		}
	}

	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private float pictAiler;
	private float pictElev;
	private float pictTurba;
	private LightPointActor light1;
	private LightPointActor light2;
	private LightPointActor light3;
	private LightPointActor light4;
	private static final float fuelGallonsScale[] = { 0.0F, 8.25F, 17.5F, 36.5F, 54F, 90F, 108F };
	private static final float fuelGallonsAuxScale[] = { 0.0F, 38F, 62.5F, 87F, 104F };
	private static final float speedometerScale[] = { 0.0F, 5F, 47.5F, 92F, 134F, 180F, 227F, 241F, 255F, 262.5F, 270F, 283F, 296F, 312F, 328F };
	private static final float variometerScale[] = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };
	private Point3d tmpP;
	private Vector3d tmpV;
	private boolean bNeedSetUp;
	private int stage;
	private static boolean bEnergize = false;
	private static boolean bEngage = false;
	private static boolean bNavLightsState = false;
	private static boolean bLandingLightState = false;
	private static int iMagnetosState = 3;
	private boolean bTWlock;
	private boolean bCanopyClosed;
	private boolean bMusicPresent;
	private boolean bCanopyInit;
	private float vol;
	private float pitch;
	private boolean bPowerON;
	private float fzp;
	private static float oxyp = 1.0F;
	private int iOxy;
	private SoundFX soundfx1;
	private SoundFX sndWind;
	private SoundFX sndTurbo;
	private SoundFX sndFlaps;
	private SoundFX sndFlapsEnd;
	private int iFlapEndCycles;
	private static int iTurba;

}
