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
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitB6N2 extends CockpitPilot {
	private class Variables {

		float throttle;
		float prop;
		float altimeter;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		AnglesFork waypointDeviation;
		float vspeed;
		float manifold;

		private Variables() {
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
				setNew.throttle = 0.85F * setOld.throttle + fm.CT.PowerControl * 0.15F;
				setNew.prop = 0.85F * setOld.prop + fm.CT.getStepControl() * 0.15F;
				setNew.altimeter = fm.getAltitude();
				float f = waypointAzimuth();
				setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
				if (useRealisticNavigationInstruments()) {
					setNew.waypointDeviation.setDeg(setOld.waypointDeviation.getDeg(1.0F), getBeaconDirection());
					setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), f);
				} else {
					setNew.waypointDeviation.setDeg(setOld.waypointDeviation.getDeg(0.1F), f - setOld.azimuth.getDeg(1.0F));
					setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), fm.Or.azimut() + 90F);
				}
				setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
				setNew.manifold = 0.8F * setOld.manifold + 0.2F * fm.EI.engines[0].getManifoldPressure();
			}
			return true;
		}

		Interpolater() {
		}
	}

	protected float waypointAzimuth() {
		return super.waypointAzimuthInvertMinus(5F);
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("Blister1_D0", false);
			aircraft().hierMesh().chunkVisible("Blister2_D0", false);
			aircraft().hierMesh().chunkVisible("Blister3_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("Blister1_D0", true);
		aircraft().hierMesh().chunkVisible("Blister2_D0", true);
		aircraft().hierMesh().chunkVisible("Blister3_D0", true);
		super.doFocusLeave();
	}

	protected void setCameraOffset() {
		cameraCenter.add(0.054999999701976776D, 0.0D, 0.0D);
	}

	public CockpitB6N2() {
		super("3DO/Cockpit/J2M3/B6N2.him", "bf109");
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		pictMix = 0.0F;
		pictGear = 0.0F;
		pictTriE = 0.0F;
		pictTriR = 0.0F;
		pictSupc = 0.0F;
		pictBoox = 0.0F;
		cockpitNightMats = (new String[] { "Arrows_Segment", "D_g_ind_01", "D_g_ind_02", "D_g_ind_03", "D_g_ind_04", "D_g_ind_05", "D_g_ind_06", "D_g_ind_07", "g_ind_01", "g_ind_02", "g_ind_03", "g_ind_04", "g_ind_05", "g_ind_06", "g_ind_07" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		if (acoustics != null)
			acoustics.globFX = new ReverbFXRoom(0.45F);
		printCompassHeading = true;
	}

	public void reflectWorldToInstruments(float f) {
		resetYPRmodifier();
		mesh.chunkSetAngles("Z_Trim1", 720F * (pictTriE = 0.92F * pictTriE + 0.08F * fm.CT.getTrimElevatorControl()), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Trim2", 160F * (pictTriR = 0.92F * pictTriR + 0.08F * fm.CT.getTrimRudderControl()), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_CowlFlaps1", 175.5F * fm.EI.engines[0].getControlRadiator(), 0.0F, 0.0F);
		float f1;
		if (fm.CT.GearControl == 0.0F && fm.CT.getGear() != 0.0F)
			f1 = -32F;
		else if (fm.CT.GearControl == 1.0F && fm.CT.getGear() != 1.0F)
			f1 = 32F;
		else
			f1 = 0.0F;
		mesh.chunkSetAngles("Z_Gear1", pictGear = 0.8F * pictGear + 0.2F * f1, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Gear2", pictGear > 0.0F ? 5F : 0.0F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Throtle1", 70.45F * interp(setNew.throttle, setOld.throttle, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Prop1", 95F * interp(setNew.prop, setOld.prop, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Mixture1", 79F * (pictMix = 0.8F * pictMix + 0.2F * fm.EI.engines[0].getControlMix()), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Supercharger1", -32F * (pictSupc = 0.91F * pictSupc + 0.09F * (float) fm.EI.engines[0].getControlCompressor()), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Boost", -32F * (pictBoox = 0.91F * pictBoox + 0.09F * (fm.EI.engines[0].getControlAfterburner() ? 1.0F : 0.0F)), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Rudder", 16F * fm.CT.getRudder(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_RightPedal", 40F * fm.CT.getBrake() * cvt(fm.CT.getRudder(), -0.5F, 1.0F, 0.0F, 1.0F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_LeftPedal", 40F * fm.CT.getBrake() * cvt(fm.CT.getRudder(), -1F, 0.5F, 1.0F, 0.0F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Columnbase", -18F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Column", -14F * (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Trigger", fm.CT.saveWeaponControl[0] || fm.CT.saveWeaponControl[1] ? -16.5F : 0.0F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Flaps1", -90F * fm.CT.FlapsControl, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_OilCooler", -175.5F * fm.EI.engines[0].getControlRadiator(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Magneto", cvt(fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 76F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Overboost", 0.0F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, 720F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Speedometer1", -floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 92.59998F, 740.7998F, 0.0F, 7F), speedometerScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_TurnBank1", fm.Or.getKren(), 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(fm.Or.getTangage(), -45F, 45F, 0.02705F, -0.02705F);
		mesh.chunkSetLocate("Z_TurnBank1a", Cockpit.xyz, Cockpit.ypr);
		w.set(fm.getW());
		fm.Or.transform(w);
		mesh.chunkSetAngles("Z_TurnBank2", cvt(w.z, -0.23562F, 0.23562F, -21F, 21F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_TurnBank3", cvt(getBall(8D), -8F, 8F, 12F, -12F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Climb1", cvt(setNew.vspeed, -10F, 10F, 180F, -180F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Compass1", -setNew.azimuth.getDeg(f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_RPM1", cvt(fm.EI.engines[0].getRPM(), 500F, 3500F, 0.0F, -315F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Fuel1", cvt(fm.M.fuel, 50F, 403.2F, 0.0F, -240F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Fuel2", cvt(fm.M.fuel, 0.0F, 43.2F, 0.0F, -270F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_FuelPres1", cvt(fm.M.fuel > 1.0F ? 0.32F : 0.0F, 0.0F, 1.0F, 0.0F, -278F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_HydPres1", fm.Gears.isHydroOperable() ? -116F : 0.0F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Temp1", cvt(fm.EI.engines[0].tWaterOut * fm.EI.engines[0].getPowerOutput() * 3.5F, 500F, 900F, 0.0F, -65F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Pres1", cvt(setNew.manifold, 0.200068F, 1.799932F, 164F, -164F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Oil1", -floatindex(cvt(fm.EI.engines[0].tOilOut, 30F, 110F, 0.0F, 4F), oilTempScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Oilpres1", cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness(), 0.0F, 7.45F, 0.0F, -270F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_CylHead_Temp", cvt(fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, -64F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_FireExt_Quan", cvt(fm.EI.engines[0].getExtinguishers(), 0.0F, 11F, 0.0F, -180F), 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[0] = cvt(fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, 0.045F);
		mesh.chunkSetLocate("Z_Flap_Ind", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Z_Ext_Air_Temp", -floatindex(cvt(Atmosphere.temperature((float) fm.Loc.z), 233.09F, 333.09F, 0.0F, 5F), frAirTempScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Trim1a", 0.0F, 0.0F, 27F * fm.CT.getTrimElevatorControl());
		mesh.chunkSetAngles("Z_Trim2a", 0.0F, 0.0F, 27F * fm.CT.getTrimRudderControl());
		mesh.chunkVisible("XLampGearUpR", fm.CT.getGear() < 0.01F || !fm.Gears.rgear);
		mesh.chunkVisible("XLampGearUpL", fm.CT.getGear() < 0.01F || !fm.Gears.lgear);
		mesh.chunkVisible("XLampGearDownR", fm.CT.getGear() > 0.99F && fm.Gears.rgear);
		mesh.chunkVisible("XLampGearDownL", fm.CT.getGear() > 0.99F && fm.Gears.lgear);
	}

	public void reflectCockpitState() {
		retoggleLight();
		if ((fm.AS.astateCockpitState & 2) != 0)
			;
		if ((fm.AS.astateCockpitState & 1) != 0)
			mesh.chunkVisible("XHullDamage1", true);
		mesh.chunkVisible("XGlassDamage1", true);
		mesh.chunkVisible("XGlassDamage4", true);
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("XGlassDamage2", true);
			mesh.chunkVisible("XGlassDamage3", true);
			mesh.chunkVisible("XHullDamage4", true);
		}
		if ((fm.AS.astateCockpitState & 4) != 0)
			mesh.chunkVisible("XHullDamage2", true);
		if ((fm.AS.astateCockpitState & 8) != 0)
			;
		if ((fm.AS.astateCockpitState & 0x80) != 0)
			mesh.chunkVisible("Z_OilSplats_D1", true);
		mesh.chunkVisible("XGlassDamage5", true);
		if ((fm.AS.astateCockpitState & 0x10) != 0)
			mesh.chunkVisible("XHullDamage3", true);
		mesh.chunkVisible("XGlassDamage6", true);
		if ((fm.AS.astateCockpitState & 0x20) != 0)
			;
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

	private void retoggleLight() {
		if (cockpitLightControl) {
			setNightMats(false);
			setNightMats(true);
		} else {
			setNightMats(true);
			setNightMats(false);
		}
	}

	public void doToggleDim() {
	}

	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private float pictAiler;
	private float pictElev;
	private float pictMix;
	private float pictGear;
	private float pictTriE;
	private float pictTriR;
	private float pictSupc;
	private float pictBoox;
	private static final float speedometerScale[] = { 0.0F, 109.5F, 220.5F, 337F, 433.5F, 513F, 605.5F, 301.5F };
	private static final float frAirTempScale[] = { 0.0F, 27.5F, 49.5F, 66F, 82F, 100F };
	private static final float oilTempScale[] = { 0.0F, 43.5F, 95.5F, 172F, 262F };

	static {
		Property.set(CLASS.THIS(), "normZNs", new float[] { 1.3F, 0.9F, 0.85F, 0.9F });
	}

}
