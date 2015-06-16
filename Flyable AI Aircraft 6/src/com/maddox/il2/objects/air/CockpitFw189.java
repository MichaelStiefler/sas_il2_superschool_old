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

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitFw189 extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			setTmp = setOld;
			setOld = setNew;
			setNew = setTmp;
			setNew.altimeter = fm.getAltitude();
			setNew.throttlel = (10F * setOld.throttlel + fm.EI.engines[0].getControlThrottle()) / 11F;
			setNew.throttler = (10F * setOld.throttler + fm.EI.engines[1].getControlThrottle()) / 11F;
			w.set(fm.getW());
			fm.Or.transform(w);
			setNew.turn = (12F * setOld.turn + w.z) / 13F;
			float f = waypointAzimuth();
			if (useRealisticNavigationInstruments()) {
				setNew.waypointAzimuth.setDeg(f - 90F);
				setOld.waypointAzimuth.setDeg(f - 90F);
				setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
			} else {
				setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(1.0F));
			}
			setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
			setNew.vspeed = (499F * setOld.vspeed + fm.getVertSpeed()) / 500F;
			setNew.beaconDirection = (10F * setOld.beaconDirection + getBeaconDirection()) / 11F;
			setNew.beaconRange = (10F * setOld.beaconRange + getBeaconRange()) / 11F;
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float altimeter;
		float throttlel;
		float throttler;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		AnglesFork radioCompassAzimuth;
		float beaconDirection;
		float beaconRange;
		float turn;
		float vspeed;

		private Variables() {
			azimuth = new AnglesFork();
			waypointAzimuth = new AnglesFork();
			radioCompassAzimuth = new AnglesFork();
		}

	}

	protected float waypointAzimuth() {
		return waypointAzimuthInvertMinus(30F);
	}

	public CockpitFw189() {
		super("3DO/Cockpit/Fw189/CockpitFw189.him", "he111");
		setOld = new Variables();
		setNew = new Variables();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		w = new Vector3f();
		HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK1");
		Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
		light1 = new LightPointActor(new LightPoint(), loc.getPoint());
		light1.light.setColor(218F, 143F, 128F);
		light1.light.setEmit(0.0F, 0.0F);
		pos.base().draw.lightMap().put("LAMPHOOK1", light1);
		cockpitNightMats = (new String[] { "clocks1", "clocks2", "clocks2DMG", "clocks3", "clocks3DMG", "clocks4", "clocks5", "clocks6", "clocL11", "clocL13", "clocL14", "clocL21", "clocR23", "clocR24", "Eqpt_II", "gauges_1_TR", "gauges_4_TR",
				"gauges_6_TR", "gauges_7_TR" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		printCompassHeading = true;
		if (acoustics != null)
			acoustics.globFX = new ReverbFXRoom(0.3F);
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("CF_D" + aircraft().chunkDamageVisible("CF"), false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("CF_D" + aircraft().chunkDamageVisible("CF"), true);
		super.doFocusLeave();
	}

	public void reflectWorldToInstruments(float f) {
		if (fm.isTick(44, 0)) {
			if ((fm.AS.astateCockpitState & 8) == 0) {
				mesh.chunkVisible("Z_GearLRed1", fm.CT.getGear() == 0.0F || fm.Gears.isAnyDamaged());
				mesh.chunkVisible("Z_GearRRed1", fm.CT.getGear() == 0.0F || fm.Gears.isAnyDamaged());
				mesh.chunkVisible("Z_GearLGreen1", fm.CT.getGear() == 1.0F && fm.Gears.lgear);
				mesh.chunkVisible("Z_GearRGreen1", fm.CT.getGear() == 1.0F && fm.Gears.rgear);
			} else {
				mesh.chunkVisible("Z_GearLRed1", false);
				mesh.chunkVisible("Z_GearRRed1", false);
				mesh.chunkVisible("Z_GearLGreen1", false);
				mesh.chunkVisible("Z_GearRGreen1", false);
			}
			if ((fm.AS.astateCockpitState & 0x40) == 0) {
				mesh.chunkVisible("zFuelWarning1", fm.M.fuel < 50F);
				mesh.chunkVisible("zFuelWarning2", fm.M.fuel < 50F);
			}
		}
		mesh.chunkSetAngles("zColumn1", 0.0F, 0.0F, -10F * (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl));
		mesh.chunkSetAngles("zColumn2", 0.0F, 0.0F, -40F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl));
		if (fm.CT.getRudder() > 0.0F) {
			mesh.chunkSetAngles("zPedalL", 0.0F, 0.0F, -10F * fm.CT.getRudder());
			mesh.chunkSetAngles("zPedalR", 0.0F, 0.0F, -45F * fm.CT.getRudder());
		} else {
			mesh.chunkSetAngles("zPedalL", 0.0F, 0.0F, -45F * fm.CT.getRudder());
			mesh.chunkSetAngles("zPedalR", 0.0F, 0.0F, -10F * fm.CT.getRudder());
		}
		mesh.chunkSetAngles("zOilFlap1", 0.0F, 0.0F, -50F * fm.EI.engines[0].getControlRadiator());
		mesh.chunkSetAngles("zOilFlap2", 0.0F, 0.0F, -50F * fm.EI.engines[1].getControlRadiator());
		mesh.chunkSetAngles("zMix1", 0.0F, 0.0F, -30F * fm.EI.engines[0].getControlMix());
		mesh.chunkSetAngles("zMix2", 0.0F, 0.0F, -30F * fm.EI.engines[1].getControlMix());
		mesh.chunkSetAngles("zFlaps1", 0.0F, 0.0F, -45F * fm.CT.FlapsControl);
		if (fm.EI.engines[0].getControlProp() >= 0.0F)
			mesh.chunkSetAngles("zPitch1", 0.0F, 0.0F, -65F * fm.EI.engines[0].getControlProp());
		if (fm.EI.engines[1].getControlProp() >= 0.0F)
			mesh.chunkSetAngles("zPitch2", 0.0F, 0.0F, -65F * fm.EI.engines[1].getControlProp());
		mesh.chunkSetAngles("zThrottle1", 0.0F, 0.0F, -33.6F * interp(setNew.throttlel, setOld.throttlel, f));
		mesh.chunkSetAngles("zThrottle2", 0.0F, 0.0F, -33.6F * interp(setNew.throttler, setOld.throttler, f));
		mesh.chunkSetAngles("zCompressor1", 0.0F, 0.0F, -25F * (float) fm.EI.engines[0].getControlCompressor());
		mesh.chunkSetAngles("zCompressor2", 0.0F, 0.0F, -25F * (float) fm.EI.engines[1].getControlCompressor());
		mesh.chunkSetAngles("zHour", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zMinute", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zSecond", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zAH1", 0.0F, 0.0F, fm.Or.getKren());
		mesh.chunkSetAngles("zAH2", 0.0F, 0.0F, cvt(fm.Or.getTangage(), -30F, 30F, -6.5F, 6.5F));
		mesh.chunkSetAngles("zTurnBank", cvt(setNew.turn, -0.23562F, 0.23562F, 30F, -30F), 0.0F, 0.0F);
		float f1 = getBall(4.5D);
		mesh.chunkSetAngles("zBall", cvt(f1, -4F, 4F, -8F, 8F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zBall2", cvt(f1, -4.5F, 4.5F, -9F, 9F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zVSI", cvt(setNew.vspeed, -15F, 15F, -160F, 160F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zSpeed", floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 400F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("zAlt1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zAlt2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
		if (useRealisticNavigationInstruments()) {
			mesh.chunkSetAngles("zCompass", setNew.azimuth.getDeg(f) - setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
			// mesh.chunkSetAngles("Z_Compass3", setNew.azimuth.getDeg(f) -
			// setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
			mesh.chunkSetAngles("zRepeater", -setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
			mesh.chunkSetAngles("zPatin", -setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
			// mesh.chunkSetAngles("Z_Compass4",
			// -setNew.waypointAzimuth.getDeg(f) - 90F, 0.0F, 0.0F);
			// mesh.chunkSetAngles("Z_Compass5",
			// setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F, 0.0F);
			mesh.chunkSetAngles("zMagnetic", -setNew.azimuth.getDeg(f), 0.0F, 0.0F);
			mesh.chunkSetAngles("zNavP", -(setNew.waypointAzimuth.getDeg(f * 0.1F) - setNew.azimuth.getDeg(f)), 0.0F, 0.0F);
		} else {
			mesh.chunkSetAngles("zRepeater", -setNew.azimuth.getDeg(f), 0.0F, 0.0F);
			mesh.chunkSetAngles("zPatin", -setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
			// mesh.chunkSetAngles("Z_Compass4", -setNew.azimuth.getDeg(f) -
			// 90F, 0.0F, 0.0F);
			mesh.chunkSetAngles("zCompass", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
			// mesh.chunkSetAngles("Z_Compass3", setNew.waypointAzimuth.getDeg(f
			// * 0.1F), 0.0F, 0.0F);
			mesh.chunkSetAngles("zMagnetic", -setNew.azimuth.getDeg(f), 0.0F, 0.0F);
			mesh.chunkSetAngles("zNavP", -setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
		}
		mesh.chunkSetAngles("zRPM1", floatindex(cvt(fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("zRPM2", floatindex(cvt(fm.EI.engines[1].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("zBoost1", cvt(fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zBoost2", cvt(fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOilTemp1", cvt(fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 130F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOilTemp2", cvt(fm.EI.engines[1].tOilOut, 0.0F, 100F, 0.0F, 130F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zCoolant1", cvt(fm.EI.engines[0].tWaterOut, 0.0F, 100F, 0.0F, 126F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zCoolant2", cvt(fm.EI.engines[1].tWaterOut, 0.0F, 100F, 0.0F, 126F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP1-1", cvt(fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP1-2", cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP1-3", cvt(fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP1-4", cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP2-1", cvt(fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP2-2", cvt(1.0F + 0.05F * fm.EI.engines[1].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP2-3", cvt(fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zOFP2-4", cvt(1.0F + 0.05F * fm.EI.engines[1].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zFuel1", cvt(fm.M.fuel / 0.72F, 0.0F, 330F, 0.0F, 140F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zFuel2", cvt(fm.M.fuel / 0.72F, 0.0F, 330F, 0.0F, 140F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zExtT", cvt(Atmosphere.temperature((float) fm.Loc.z), 223.09F, 323.09F, -145F, 145F), 0.0F, 0.0F);
		float f2 = (float) Math.toDegrees(fm.EI.engines[0].getPropPhi() - fm.EI.engines[0].getPropPhiMin());
		f2 = (float) (int) (-f2 / 0.2F) * 0.2F;
		mesh.chunkSetAngles("zProp1-1", f2 * 60F, 0.0F, 0.0F);
		mesh.chunkSetAngles("zProp1-2", f2 * 5F, 0.0F, 0.0F);
		f2 = (float) Math.toDegrees(fm.EI.engines[1].getPropPhi() - fm.EI.engines[0].getPropPhiMin());
		f2 = (float) (int) (-f2 / 0.2F) * 0.2F;
		mesh.chunkSetAngles("zProp2-1", f2 * 60F, 0.0F, 0.0F);
		mesh.chunkSetAngles("zProp2-2", f2 * 5F, 0.0F, 0.0F);
		mesh.chunkSetAngles("zFlapsIL", 145F * fm.CT.getFlap(), 0.0F, 0.0F);
		mesh.chunkSetAngles("zFlapsIR", 145F * fm.CT.getFlap(), 0.0F, 0.0F);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 4) != 0) {
			mesh.chunkVisible("ZHolesL_D1", true);
			mesh.chunkVisible("PanelL_D1", true);
			mesh.chunkVisible("PanelL_D0", false);
			mesh.chunkVisible("zVSI", false);
			mesh.chunkVisible("zBlip1", false);
		}
		if ((fm.AS.astateCockpitState & 8) != 0) {
			mesh.chunkVisible("ZHolesL_D2", true);
			mesh.chunkVisible("PanelFloat_D1", true);
			mesh.chunkVisible("PanelFloat_D0", false);
			mesh.chunkVisible("zProp1-1", false);
			mesh.chunkVisible("zProp1-2", false);
			mesh.chunkVisible("zProp2-1", false);
			mesh.chunkVisible("zProp2-2", false);
			mesh.chunkVisible("zFlapsIL", false);
			mesh.chunkVisible("zFlapsIR", false);
		}
		if ((fm.AS.astateCockpitState & 0x10) != 0) {
			mesh.chunkVisible("ZHolesR_D1", true);
			mesh.chunkVisible("PanelR_D1", true);
			mesh.chunkVisible("PanelR_D0", false);
			mesh.chunkVisible("zRPM1", false);
			mesh.chunkVisible("zBoost2", false);
			mesh.chunkVisible("zOilTemp2", false);
			mesh.chunkVisible("zCoolant1", false);
			mesh.chunkVisible("zOFP1-1", false);
			mesh.chunkVisible("zOFP1-2", false);
			mesh.chunkVisible("zFlapsIR", false);
		}
		if ((fm.AS.astateCockpitState & 0x20) != 0)
			mesh.chunkVisible("ZHolesR_D2", true);
		if ((fm.AS.astateCockpitState & 1) != 0)
			mesh.chunkVisible("ZHolesF_D1", true);
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("PanelT_D1", true);
			mesh.chunkVisible("PanelT_D0", false);
			mesh.chunkVisible("zFuel2", false);
			mesh.chunkVisible("zOFP1-3", false);
			mesh.chunkVisible("zOFP1-4", false);
		}
		if ((fm.AS.astateCockpitState & 0x80) != 0)
			mesh.chunkVisible("zOil_D1", true);
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl) {
			light1.light.setEmit(0.0032F, 7.2F);
			setNightMats(true);
		} else {
			light1.light.setEmit(0.0F, 0.0F);
			setNightMats(false);
		}
	}

	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	private LightPointActor light1;
	private float pictAiler;
	private float pictElev;
	public Vector3f w;
	private static final float speedometerScale[] = { 0.0F, 0.1F, 19F, 37.25F, 63.5F, 91.5F, 112F, 135.5F, 159.5F, 186.5F, 213F, 238F, 264F, 289F, 314.5F, 339.5F, 359.5F, 360F, 360F, 360F };
	private static final float rpmScale[] = { 0.0F, 11.25F, 54F, 111F, 171.5F, 229.5F, 282.5F, 334F, 342.5F, 342.5F };

}
