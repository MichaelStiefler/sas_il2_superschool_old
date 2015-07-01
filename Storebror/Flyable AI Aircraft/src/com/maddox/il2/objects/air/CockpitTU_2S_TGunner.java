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

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitTU_2S_TGunner extends CockpitGunner {
	private class Variables {

		float man1;
		float man2;
		float altimeter;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		float vspeed;
		float inert;

		private Variables() {
			azimuth = new AnglesFork();
			waypointAzimuth = new AnglesFork();
		}

	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (fm != null) {
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				setNew.man1 = 0.92F * setOld.man1 + 0.08F * fm.EI.engines[0].getManifoldPressure();
				setNew.man2 = 0.92F * setOld.man2 + 0.08F * fm.EI.engines[1].getManifoldPressure();
				setNew.altimeter = fm.getAltitude();
				setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
				setNew.vspeed = (100F * setOld.vspeed + fm.getVertSpeed()) / 101F;
				if (useRealisticNavigationInstruments())
					setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), getBeaconDirection());
				else
					setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), waypointAzimuth() - setOld.azimuth.getDeg(1.0F));
				setNew.inert = 0.999F * setOld.inert + 0.001F * (fm.EI.engines[0].getStage() == 6 ? 0.867F : 0.0F);
			}
			return true;
		}

		Interpolater() {
		}
	}

	protected float waypointAzimuth() {
		return super.waypointAzimuthInvertMinus(10F);
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("TurrelA", -orient.getYaw(), 0.0F, 0.0F);
		mesh.chunkSetAngles("TurrelB", -orient.getTangage(), 0.0F, 0.0F);
		mesh.chunkSetAngles("TurrelQ", -cvt(orient.getYaw(), -45F, 12F, -45F, 12F), 0.0F, 0.0F);
		mesh.chunkSetAngles("TurrelC", -cvt(orient.getTangage(), -1F, 45F, -1F, 45F), 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[0] = cvt(orient.getYaw(), 0.0F, 12F, 0.0F, -0.14F);
		mesh.chunkSetLocate("TurrelD", Cockpit.xyz, Cockpit.ypr);
	}

	public void clipAnglesGun(Orient orient) {
		if (!isRealMode())
			return;
		if (!aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		if (f < -45F)
			f = -45F;
		if (f > 45F)
			f = 45F;
		if (f1 > 45F)
			f1 = 45F;
		if (f1 < -1F)
			f1 = -1F;
		orient.setYPR(f, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!isRealMode())
			return;
		if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
			bGunFire = false;
		fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
		if (bGunFire) {
			if (hook1 == null)
				hook1 = new HookNamed(aircraft(), "_MGUN01");
			doHitMasterAircraft(aircraft(), hook1, "_MGUN01");
		}
	}

	public void doGunFire(boolean flag) {
		if (!isRealMode())
			return;
		if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
			bGunFire = false;
		else
			bGunFire = flag;
		fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
	}

	public CockpitTU_2S_TGunner() {
		super("3DO/Cockpit/Pe-2series84-TGun/TGunnerTU2S.him", "il2rear");
		bNeedSetUp = true;
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		hook1 = null;
		cockpitNightMats = (new String[] { "GP_I", "GP_II", "GP_II_DM", "GP_III_DM", "GP_III", "GP_IV_DM", "GP_IV", "GP_V", "GP_VI", "GP_VII", "compass", "Eqpt_II", "Trans_II", "Trans_VI_Pilot", "Trans_VII_Pilot" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
		// mesh.chunkVisible("WireL_D0",
		// aircraft().hierMesh().isChunkVisible("WireL_D0"));
		// mesh.chunkVisible("WireR_D0",
		// aircraft().hierMesh().isChunkVisible("WireR_D0"));
		mesh.chunkSetAngles("Z_Gear1", fm.CT.GearControl > 0.5F ? -60F : 0.0F, 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[1] = -0.095F * fm.CT.getRudder();
		mesh.chunkSetLocate("Z_RightPedal", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Z_Columnbase", -8F * (pictElev = 0.65F * pictElev + 0.35F * fm.CT.ElevatorControl), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Column", -45F * (pictAiler = 0.65F * pictAiler + 0.35F * fm.CT.AileronControl), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Brake", 21.5F * fm.CT.BrakeControl, 0.0F, 0.0F);
		resetYPRmodifier();
		resetYPRmodifier();
		if (fm.EI.engines[0].getControlRadiator() > 0.5F)
			Cockpit.xyz[1] = 0.011F;
		mesh.chunkSetLocate("Z_RadL", Cockpit.xyz, Cockpit.ypr);
		resetYPRmodifier();
		if (fm.EI.engines[1].getControlRadiator() > 0.5F)
			Cockpit.xyz[1] = 0.011F;
		mesh.chunkSetLocate("Z_RadR", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, -720F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, -7200F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Altimeter3", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, -720F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Altimeter4", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, -7200F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Speedometer1", -floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Speedometer2", -floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(fm.Or.getTangage(), -45F, 45F, 0.018F, -0.018F);
		mesh.chunkSetLocate("Z_TurnBank1", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Z_TurnBank1Q", -fm.Or.getKren(), 0.0F, 0.0F);
		w.set(fm.getW());
		fm.Or.transform(w);
		mesh.chunkSetAngles("Z_TurnBank2", cvt(w.z, -0.23562F, 0.23562F, -27F, 27F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Climb1", cvt(setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Compass1", cvt(setNew.waypointAzimuth.getDeg(f), -90F, 90F, -55.5F, 55.5F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Compass2", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_RPM1", cvt(fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, -360F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_RPM2", cvt(fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, -3600F), 0.0F, 0.0F);
		if ((fm.AS.astateCockpitState & 0x40) == 0) {
			mesh.chunkSetAngles("Z_RPM3", cvt(fm.EI.engines[1].getRPM(), 0.0F, 10000F, 0.0F, -360F), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_RPM4", cvt(fm.EI.engines[1].getRPM(), 0.0F, 10000F, 0.0F, -3600F), 0.0F, 0.0F);
		}
		mesh.chunkSetAngles("Z_Fuel1", cvt(fm.M.fuel, 0.0F, 360F, 0.0F, -198F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_InertGas", cvt(setNew.inert, 0.0F, 1.0F, 0.0F, -300F), 0.0F, 0.0F);
		float f1 = 0.0F;
		if (fm.M.fuel > 1.0F)
			f1 = cvt(fm.EI.engines[0].getRPM(), 0.0F, 570F, 0.0F, 0.26F);
		mesh.chunkSetAngles("Z_FuelPres1", cvt(f1, 0.0F, 1.0F, 0.0F, -270F), 0.0F, 0.0F);
		f1 = 0.0F;
		if (fm.M.fuel > 1.0F)
			f1 = cvt(fm.EI.engines[1].getRPM(), 0.0F, 570F, 0.0F, 0.26F);
		mesh.chunkSetAngles("Z_FuelPres2", cvt(f1, 0.0F, 1.0F, 0.0F, -270F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Temp1", cvt(fm.EI.engines[0].tWaterOut, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Temp2", cvt(fm.EI.engines[1].tWaterOut, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Pres1", cvt(setNew.man1, 0.399966F, 1.599864F, 0.0F, -300F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Pres2", cvt(setNew.man2, 0.399966F, 1.599864F, 0.0F, -300F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Oil1", cvt(fm.EI.engines[0].tOilIn, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Oil2", cvt(fm.EI.engines[1].tOilIn, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Oilpres1", cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness(), 0.0F, 7.45F, 0.0F, -270F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Oilpres2", cvt(1.0F + 0.05F * fm.EI.engines[1].tOilOut * fm.EI.engines[1].getReadyness(), 0.0F, 7.45F, 0.0F, -270F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_AirPres", -116F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_HPres", fm.Gears.isHydroOperable() ? -102F : 0.0F, 0.0F, 0.0F);
		f1 = Atmosphere.temperature((float) fm.Loc.z) - 273.09F;
		if (f1 < -40F)
			mesh.chunkSetAngles("Z_AirTemp", cvt(f1, -40F, -20F, 52F, 35F), 0.0F, 0.0F);
		else if (f1 < 0.0F)
			mesh.chunkSetAngles("Z_AirTemp", cvt(f1, -20F, 0.0F, 35F, 0.0F), 0.0F, 0.0F);
		else if (f1 < 20F)
			mesh.chunkSetAngles("Z_AirTemp", cvt(f1, 0.0F, 20F, 0.0F, -18.5F), 0.0F, 0.0F);
		else
			mesh.chunkSetAngles("Z_AirTemp", cvt(f1, 20F, 50F, -18.5F, -37F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_FlapPos", cvt(fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, -180F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Compass4", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
		mesh.chunkVisible("XRGearUp", fm.CT.getGear() < 0.01F || !fm.Gears.rgear);
		mesh.chunkVisible("XLGearUp", fm.CT.getGear() < 0.01F || !fm.Gears.lgear);
		mesh.chunkVisible("XCGearUp", fm.CT.getGear() < 0.01F);
		mesh.chunkVisible("XRGearDn", fm.CT.getGear() > 0.99F && fm.Gears.rgear);
		mesh.chunkVisible("XLGearDn", fm.CT.getGear() > 0.99F && fm.Gears.lgear);
		mesh.chunkVisible("XCGearDn", fm.CT.getGear() > 0.99F);
		mesh.chunkVisible("XFlapsUp", fm.CT.getFlap() > 0.5F);
		mesh.chunkVisible("XOverG1", fm.getOverload() > 3F);
		mesh.chunkVisible("XOverG2", fm.getOverload() < -1F);
		mesh.chunkVisible("XERimCenter", Math.abs(fm.CT.getTrimElevatorControl()) < 0.02F);
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			// aircraft().hierMesh().chunkVisible("NDetails_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		// aircraft().hierMesh().chunkVisible("NDetails_D0", true);
		super.doFocusLeave();
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

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 1) != 0) {
			mesh.chunkVisible("XGlassDamage2", true);
			mesh.chunkVisible("XGlassDamage3", true);
			mesh.chunkVisible("XGlassDamage4", true);
			mesh.chunkVisible("XHullDamage3", true);
		}
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("Panel_D0", false);
			mesh.chunkVisible("Panel_D1", true);
			mesh.chunkVisible("Z_Fuel1", false);
			mesh.chunkVisible("Z_Pres1", false);
			mesh.chunkVisible("Z_Altimeter3", false);
			mesh.chunkVisible("Z_Altimeter4", false);
			mesh.chunkVisible("XHullDamage3", true);
		}
		if ((fm.AS.astateCockpitState & 4) != 0) {
			mesh.chunkVisible("XGlassDamage3", true);
			mesh.chunkVisible("XHullDamage1", true);
		}
		if ((fm.AS.astateCockpitState & 8) != 0) {
			mesh.chunkVisible("XGlassDamage1", true);
			mesh.chunkVisible("XGlassDamage5", true);
			mesh.chunkVisible("XHullDamage3", true);
		}
		if ((fm.AS.astateCockpitState & 0x10) != 0) {
			mesh.chunkVisible("XGlassDamage4", true);
			mesh.chunkVisible("XHullDamage2", true);
		}
		if ((fm.AS.astateCockpitState & 0x20) != 0) {
			mesh.chunkVisible("XGlassDamage1", true);
			mesh.chunkVisible("XGlassDamage6", true);
			mesh.chunkVisible("XHullDamage3", true);
		}
		if ((fm.AS.astateCockpitState & 2) != 0 && (fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("Panel_D0", false);
			mesh.chunkVisible("Panel_D1", false);
			mesh.chunkVisible("Panel_D2", true);
			mesh.chunkVisible("Z_Altimeter1", false);
			mesh.chunkVisible("Z_Altimeter2", false);
			mesh.chunkVisible("Z_Speedometer1", false);
			mesh.chunkVisible("Z_Speedometer2", false);
			mesh.chunkVisible("Z_AirTemp", false);
			mesh.chunkVisible("Z_Pres2", false);
			mesh.chunkVisible("Z_RPM1", false);
			mesh.chunkVisible("Z_RPM2", false);
			mesh.chunkVisible("Z_InertGas", false);
			mesh.chunkVisible("Z_FuelPres2", false);
			mesh.chunkVisible("Z_Oilpres1", false);
			mesh.chunkVisible("Z_Oilpres2", false);
		}
		retoggleLight();
	}

	private boolean bNeedSetUp;
	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private float pictAiler;
	private float pictElev;
	private static final float speedometerScale[] = { 0.0F, 0.0F, 10.5F, 42.5F, 85F, 125F, 165.5F, 181F, 198F, 214.5F, 231F, 249F, 266.5F, 287.5F, 308F, 326.5F, 346F };
	private Hook hook1;

	static {
		Property.set(CLASS.THIS(), "aiTuretNum", 0);
		Property.set(CLASS.THIS(), "weaponControlNum", 10);
		Property.set(CLASS.THIS(), "astatePilotIndx", 1);
	}

}
