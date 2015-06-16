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

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitBLENHEIM4 extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (fm != null) {
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				setNew.throttle1 = 0.85F * setOld.throttle1 + ((FlightModelMain) (fm)).EI.engines[0].getControlThrottle() * 0.15F;
				setNew.throttle2 = 0.85F * setOld.throttle2 + ((FlightModelMain) (fm)).EI.engines[1].getControlThrottle() * 0.15F;
				setNew.prop1 = 0.85F * setOld.prop1 + ((FlightModelMain) (fm)).EI.engines[0].getControlProp() * 0.15F;
				setNew.prop2 = 0.85F * setOld.prop2 + ((FlightModelMain) (fm)).EI.engines[1].getControlProp() * 0.15F;
				setNew.altimeter = fm.getAltitude();
				setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
				if (useRealisticNavigationInstruments()) {
					setNew.waypointDeviation.setDeg(setOld.waypointDeviation.getDeg(0.1F), getBeaconDirection());
					setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
				} else {
					float f = waypointAzimuth();
					setNew.waypointDeviation.setDeg(setOld.waypointDeviation.getDeg(0.1F), f - setOld.azimuth.getDeg(1.0F));
					setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(1.0F) - 90F);
				}
				if ((((FlightModelMain) (fm)).AS.astateCockpitState & 0x40) == 0)
					setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
				else
					setNew.vspeed = (1990F * setOld.vspeed + fm.getVertSpeed()) / 2000F;
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
		float altimeter;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		float vspeed;
		AnglesFork waypointDeviation;

		private Variables() {
			azimuth = new AnglesFork();
			waypointAzimuth = new AnglesFork();
			waypointDeviation = new AnglesFork();
		}

	}

	protected float waypointAzimuth() {
		return super.waypointAzimuthInvertMinus(10F);
	}

	public CockpitBLENHEIM4() {
		super("3DO/Cockpit/Blenheim/MKIV.him", "bf109");
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		pictFlap = 0.0F;
		pictGear = 0.0F;
		super.cockpitNightMats = (new String[] { "prib_one_fin", "prib_two", "prib_three", "panel", "gauges2", "prib_one_fin_damage", "prib_two_damage", "prib_three_damage", "gauges2_damage", "PEICES1", "PEICES2", "PEICES3" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		if (super.acoustics != null)
			super.acoustics.globFX = new ReverbFXRoom(0.45F);
	}

	public void reflectWorldToInstruments(float f) {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
		resetYPRmodifier();
		mesh.chunkSetAngles("Z_RDF", 0.0F, 90F + setNew.waypointAzimuth.getDeg(f * 0.02F), 0.0F);
		super.mesh.chunkSetAngles("Z_Trim1", 0.0F, 161F * ((FlightModelMain) (super.fm)).CT.getTrimAileronControl(), 0.0F);
		super.mesh.chunkSetAngles("Z_Trim2", 0.0F, 332F * ((FlightModelMain) (super.fm)).CT.getTrimRudderControl(), 0.0F);
		super.mesh.chunkSetAngles("Z_Trim3", 0.0F, 722F * ((FlightModelMain) (super.fm)).CT.getTrimElevatorControl(), 0.0F);
		super.mesh.chunkSetAngles("Z_Flaps1", 0.0F, -75.5F * (pictFlap = 0.85F * pictFlap + 0.15F * ((FlightModelMain) (super.fm)).CT.FlapsControl), 0.0F);
		super.mesh.chunkSetAngles("Z_Gear1", 0.0F, -75.5F * (pictGear = 0.85F * pictGear + 0.15F * ((FlightModelMain) (super.fm)).CT.GearControl), 0.0F);
		super.mesh.chunkSetAngles("Z_Throtle1", 0.0F, 90F * interp(setNew.throttle1, setOld.throttle1, f), 0.0F);
		super.mesh.chunkSetAngles("Z_Throtle2", 0.0F, 90F * interp(setNew.throttle2, setOld.throttle2, f), 0.0F);
		super.mesh.chunkSetAngles("Z_Prop1", 0.0F, 100F * interp(setNew.prop1, setOld.prop1, f), 0.0F);
		super.mesh.chunkSetAngles("Z_Prop2", 0.0F, 100F * interp(setNew.prop2, setOld.prop2, f), 0.0F);
		super.mesh.chunkSetAngles("Z_Mixture1", 0.0F, 90F * ((FlightModelMain) (super.fm)).EI.engines[0].getControlMix(), 0.0F);
		super.mesh.chunkSetAngles("Z_Mixture2", 0.0F, 90F * ((FlightModelMain) (super.fm)).EI.engines[1].getControlMix(), 0.0F);
		super.mesh.chunkSetAngles("Z_RightPedal", 0.0F, -10F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
		super.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 10F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F);
		super.mesh.chunkSetAngles("Z_Columnbase", 0.0F, (pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl) * 8F, 0.0F);
		super.mesh.chunkSetAngles("Z_Column", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl) * 57F, 0.0F);
		super.mesh.chunkSetAngles("Z_Brake", 0.0F, -25F * ((FlightModelMain) (super.fm)).CT.getBrake(), 0.0F);
		super.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
		super.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) == 0)
			super.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, floatindex(cvt(Pitot.Indicator((float) ((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeed()), 0.0F, 180.0555F, 0.0F, 35F), speedometerScaleFAF), 0.0F);
		w.set(super.fm.getW());
		((FlightModelMain) (super.fm)).Or.transform(w);
		super.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, -48F, 48F), 0.0F);
		super.mesh.chunkSetAngles("Z_TurnBank1", 0.0F, cvt(getBall(8D), -8F, 8F, 35F, -35F), 0.0F);
		super.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, -((FlightModelMain) (super.fm)).Or.getKren(), 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[2] = cvt(((FlightModelMain) (super.fm)).Or.getTangage(), -45F, 45F, 0.0275F, -0.0275F);
		super.mesh.chunkSetLocate("Z_TurnBank4", Cockpit.xyz, Cockpit.ypr);
		super.mesh.chunkSetAngles("Z_Climb1", 0.0F, floatindex(cvt(setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), variometerScale), 0.0F);
		super.mesh.chunkSetAngles("Z_Compass1", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
		super.mesh.chunkSetAngles("Z_Compass2", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
		super.mesh.chunkSetAngles("Z_RPM1", 0.0F, floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 1200F, 3500F, 0.0F, 8F), rpmScale), 0.0F);
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) == 0)
			super.mesh.chunkSetAngles("Z_RPM2", 0.0F, floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 1200F, 3500F, 0.0F, 8F), rpmScale), 0.0F);
		else
			super.mesh.chunkSetAngles("Z_RPM2", 0.0F, floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 600F, 7000F, 0.0F, 8F), rpmScale), 0.0F);
		super.mesh.chunkSetAngles("Z_Hour1", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		super.mesh.chunkSetAngles("Z_Minute1", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		super.mesh.chunkSetAngles("Z_Fuel1", 0.0F, 0.0F, cvt(((FlightModelMain) (super.fm)).M.fuel, 0.0F, 1500F, 0.0F, 77F));
		super.mesh.chunkSetAngles("Z_Fuel2", 0.0F, 0.0F, cvt(((FlightModelMain) (super.fm)).M.fuel, 0.0F, 1500F, 0.0F, 77F));
		if ((fm.AS.astateCockpitState & 0x40) == 0)
			mesh.chunkSetAngles("Z_Oil1", 0.0F, cvt(fm.EI.engines[0].tOilIn, 49F, 101F, 0.0F, 274F), 0.0F);
		mesh.chunkSetAngles("Z_Oil2", 0.0F, cvt(fm.EI.engines[1].tOilIn, 49F, 101F, 0.0F, 274F), 0.0F);
		mesh.chunkSetAngles("Z_Oilpres1", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilIn * fm.EI.engines[0].getReadyness(), 0.0F, 12.59F, 0.0F, 277F), 0.0F);
		mesh.chunkSetAngles("Z_Oilpres2", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[1].tOilIn * fm.EI.engines[1].getReadyness(), 0.0F, 12.59F, 0.0F, 277F), 0.0F);
		float f1 = 0.5F * ((FlightModelMain) (super.fm)).EI.engines[0].getRPM() + 0.5F * ((FlightModelMain) (super.fm)).EI.engines[1].getRPM();
		f1 = 2.5F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
		super.mesh.chunkSetAngles("Z_Suction", 0.0F, cvt(f1, 0.0F, 10F, 0.0F, 302F), 0.0F);
		super.mesh.chunkSetAngles("Z_AirTemp", 0.0F, cvt(Atmosphere.temperature((float) ((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z) - 273.15F, -17.8F, 60F, 0.0F, -109.5F), 0.0F);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 2) != 0) {
			mesh.chunkVisible("XGlassDamage1", true);
			mesh.chunkVisible("XHullDamage1", true);
			mesh.chunkVisible("XGlassDamage2", true);
		}
		if ((fm.AS.astateCockpitState & 0x10) != 0) {
			mesh.chunkVisible("XGlassDamage3", true);
			mesh.chunkVisible("XGlassDamage2", true);
			mesh.chunkVisible("XHullDamage1", true);
		}
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("XHullDamage3", true);
			mesh.chunkVisible("XHullDamage2", true);
			mesh.chunkVisible("XGlassDamage3", true);
		}
		if ((fm.AS.astateCockpitState & 4) != 0)
			mesh.chunkVisible("XGlassDamage3", true);
		mesh.chunkVisible("XGlassDamage2", true);
		if ((fm.AS.astateCockpitState & 8) != 0)
			mesh.chunkVisible("XHullDamage2", true);
		mesh.chunkVisible("XGlassDamage1", true);
		if ((fm.AS.astateCockpitState & 0x10) != 0)
			mesh.chunkVisible("XGlassDamage2", true);
		mesh.chunkVisible("XGlassDamage3", true);
		if ((fm.AS.astateCockpitState & 0x20) != 0)
			mesh.chunkVisible("XHullDamage3", true);
		mesh.chunkVisible("XGlassDamage1", true);
	}

	public void toggleLight() {
		super.cockpitLightControl = !super.cockpitLightControl;
		if (super.cockpitLightControl)
			setNightMats(true);
		else
			setNightMats(false);
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("Nose_D" + aircraft().chunkDamageVisible("Nose"), false);
			aircraft().hierMesh().chunkVisible("CF_D" + aircraft().chunkDamageVisible("CF"), false);
			aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
			aircraft().hierMesh().chunkVisible("Blister1_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("Nose_D" + aircraft().chunkDamageVisible("Nose"), true);
		aircraft().hierMesh().chunkVisible("CF_D" + aircraft().chunkDamageVisible("CF"), true);
		aircraft().hierMesh().chunkVisible("Pilot2_D0", true);
		aircraft().hierMesh().chunkVisible("Blister1_D0", true);
		super.doFocusLeave();
	}

	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private float pictAiler;
	private float pictElev;
	private float pictFlap;
	private float pictGear;
	private static final float speedometerScaleFAF[] = { 0.0F, 0.0F, 1.0F, 3F, 7.5F, 34.5F, 46F, 63F, 76F, 94F, 112.5F, 131F, 150F, 168.5F, 187F, 203F, 222F, 242.5F, 258.5F, 277F, 297F, 315.5F, 340F, 360F, 376.5F, 392F, 407F, 423.5F, 442F, 459F, 476F,
			492.5F, 513F, 534.5F, 552F, 569.5F };
	private static final float variometerScale[] = { -158F, -110F, -63.5F, -29F, 0.0F, 29F, 63.5F, 110F, 158F };
	private static final float rpmScale[] = { 0.0F, 10F, 75F, 126.5F, 179.5F, 232F, 284.5F, 336F };

	static {
		Property.set(CLASS.THIS(), "normZNs", new float[] { 0.55F, 0.55F, 0.75F, 0.75F });
	}

}
