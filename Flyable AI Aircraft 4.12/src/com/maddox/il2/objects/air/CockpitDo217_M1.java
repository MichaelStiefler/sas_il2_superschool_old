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
// Last Edited at: 2013/01/22

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public class CockpitDo217_M1 extends CockpitPilot {
	private class Variables {

		float dimPos;
		float altimeter;
		float throttlel;
		float throttler;
		float vspeed;
		float cons;
		float elevTrim;
		float rudderTrim;
		float ailTrim;
		float propL;
		float propR;
		AnglesFork radioCompassAzimuth;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		float beaconDirection;
		float beaconRange;

		private Variables() {
			dimPos = 0.0F;
			radioCompassAzimuth = new AnglesFork();
			azimuth = new AnglesFork();
			waypointAzimuth = new AnglesFork();
		}

	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			setTmp = setOld;
			setOld = setNew;
			setNew = setTmp;
			fm.CT.forceCockpitDoor(1.0F);
			fm.CT.cockpitDoorControl = 1.0F;
			if (cockpitDimControl) {
				if (setNew.dimPos < 1.0F)
					setNew.dimPos = setOld.dimPos + 0.03F;
			} else if (setNew.dimPos > 0.0F)
				setNew.dimPos = setOld.dimPos - 0.03F;
			setNew.altimeter = fm.getAltitude();
			setNew.throttlel = (10F * setOld.throttlel + fm.EI.engines[0].getControlThrottle()) / 11F;
			setNew.throttler = (10F * setOld.throttler + fm.EI.engines[1].getControlThrottle()) / 11F;
			setNew.vspeed = (499F * setOld.vspeed + fm.getVertSpeed()) / 500F;
			setNew.elevTrim = 0.85F * setOld.elevTrim + fm.CT.getTrimElevatorControl() * 0.15F;
			setNew.rudderTrim = 0.85F * setOld.rudderTrim + fm.CT.getTrimRudderControl() * 0.15F;
			setNew.ailTrim = 0.85F * setOld.ailTrim + fm.CT.getTrimAileronControl() * 0.15F;
			setNew.propL = 0.85F * setOld.propL + fm.EI.engines[0].getControlProp() * 0.15F;
			setNew.propR = 0.85F * setOld.propR + fm.EI.engines[1].getControlProp() * 0.15F;
			float f = prevFuel - fm.M.fuel;
			prevFuel = fm.M.fuel;
			f /= 0.72F;
			f /= Time.tickLenFs();
			f *= 3600F;
			setNew.cons = 0.91F * setOld.cons + 0.09F * f;
			float f1 = waypointAzimuth();
			if (useRealisticNavigationInstruments()) {
				setNew.waypointAzimuth.setDeg(f1 - 90F);
				setOld.waypointAzimuth.setDeg(f1 - 90F);
				setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
			} else {
				setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f1 - setOld.azimuth.getDeg(1.0F));
			}
			setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
			setNew.beaconDirection = (10F * setOld.beaconDirection + getBeaconDirection()) / 11F;
			setNew.beaconRange = (10F * setOld.beaconRange + getBeaconRange()) / 11F;
			return true;
		}

		Interpolater() {
		}
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			((Do217) fm.actor).bPitUnfocused = false;
			aircraft().hierMesh().chunkVisible("Interior1_D0", false);
			aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
			aircraft().hierMesh().chunkVisible("Head1_D0", false);
			aircraft().hierMesh().chunkVisible("Hmask1_D0", false);
			aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
			aircraft().hierMesh().chunkVisible("Hmask2_D0", false);
			aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
			aircraft().hierMesh().chunkVisible("Hmask3_D0", false);
			aircraft().hierMesh().chunkVisible("Pilot4_D0", false);
			aircraft().hierMesh().chunkVisible("Hmask4_D0", false);
			aircraft().hierMesh().chunkVisible("Pilot1_D1", false);
			aircraft().hierMesh().chunkVisible("Pilot2_D1", false);
			aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
			aircraft().hierMesh().chunkVisible("Pilot4_D1", false);
			aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
			aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
			aircraft().hierMesh().chunkVisible("Turret5B_D0", false);
			if (aircraft() instanceof Do217_M1) {
				mesh.chunkVisible("k2-Box", false);
				mesh.chunkVisible("k2-Cable", false);
				mesh.chunkVisible("k2-cushion", false);
				mesh.chunkVisible("k2-FuG203", false);
				mesh.chunkVisible("k2-gunsight", false);
			}
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		((Do217) fm.actor).bPitUnfocused = true;
		aircraft().hierMesh().chunkVisible("Interior1_D0", true);
		if (!fm.AS.isPilotParatrooper(0)) {
			aircraft().hierMesh().chunkVisible("Pilot1_D0", !fm.AS.isPilotDead(0));
			aircraft().hierMesh().chunkVisible("Head1_D0", !fm.AS.isPilotDead(0));
			aircraft().hierMesh().chunkVisible("Pilot1_D1", fm.AS.isPilotDead(0));
		}
		if (!fm.AS.isPilotParatrooper(1)) {
			aircraft().hierMesh().chunkVisible("Pilot2_D0", !fm.AS.isPilotDead(1));
			aircraft().hierMesh().chunkVisible("Pilot2_D1", fm.AS.isPilotDead(1));
		}
		if (!fm.AS.isPilotParatrooper(2)) {
			aircraft().hierMesh().chunkVisible("Pilot3_D0", !fm.AS.isPilotDead(2));
			aircraft().hierMesh().chunkVisible("Pilot3_D1", fm.AS.isPilotDead(2));
		}
		if (!fm.AS.isPilotParatrooper(3)) {
			aircraft().hierMesh().chunkVisible("Pilot4_D0", !fm.AS.isPilotDead(3));
			aircraft().hierMesh().chunkVisible("Pilot4_D1", fm.AS.isPilotDead(3));
		}
		aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
		aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
		aircraft().hierMesh().chunkVisible("Turret5B_D0", true);
		if (aircraft() instanceof Do217_M1) {
			mesh.chunkVisible("k2-Box", true);
			mesh.chunkVisible("k2-Cable", true);
			mesh.chunkVisible("k2-cushion", true);
			mesh.chunkVisible("k2-FuG203", true);
			mesh.chunkVisible("k2-gunsight", true);
		}
		super.doFocusLeave();
	}

	protected float waypointAzimuth() {
		return super.waypointAzimuthInvertMinus(20F);
	}

	public CockpitDo217_M1() {
		super("3DO/Cockpit/Do217K1/hier.him", "he111");
		setOld = new Variables();
		setNew = new Variables();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		prevFuel = 0.0F;
		Pn = new Point3d();
		pictManf1 = 1.0F;
		pictManf2 = 1.0F;
		HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK1");
		Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
		light1 = new LightPointActor(new LightPoint(), loc.getPoint());
		light1.light.setColor(218F, 143F, 128F);
		light1.light.setEmit(0.0F, 0.0F);
		pos.base().draw.lightMap().put("LAMPHOOK1", light1);
		cockpitNightMats = (new String[] { "Peil1", "Peil2", "Instrument1", "Instrument2", "Instrument4", "Instrument5", "Instrument6", "Instrument7", "Instrument8", "Instrument9", "Needles" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		printCompassHeading = true;
		w = new Vector3f();
		buzzerFX = aircraft().newSound("models.buzzthru", false);
	}

	public void toggleDim() {
		cockpitDimControl = !cockpitDimControl;
	}

	public void reflectWorldToInstruments(float f) {
		printCompassHeading = true;
		mesh.chunkSetAngles("ZWheel", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 40F, 0.0F);
		mesh.chunkSetAngles("ZColumn", 0.0F, -7F - (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 10F, 0.0F);
		mesh.chunkSetAngles("PedalR", 0.0F, -fm.CT.getRudder() * 10F, 0.0F);
		mesh.chunkSetAngles("PedalL", 0.0F, fm.CT.getRudder() * 10F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(fm.CT.getRudder(), -1F, 1.0F, 0.08F, -0.08F);
		mesh.chunkSetLocate("PedalRbar", Cockpit.xyz, Cockpit.ypr);
		Cockpit.xyz[1] = cvt(fm.CT.getRudder(), -1F, 1.0F, -0.08F, 0.08F);
		mesh.chunkSetLocate("PedalLbar", Cockpit.xyz, Cockpit.ypr);
		float f1 = interp(setNew.altimeter, setOld.altimeter, f);
		mesh.chunkSetAngles("z_HeightKm", 0.0F, cvt(f1, 0.0F, 10000F, 0.0F, 257F), 0.0F);
		mesh.chunkSetAngles("z_Height", 0.0F, cvt(f1, 0.0F, 20000F, 0.0F, 7200F), 0.0F);
		mesh.chunkSetAngles("Z_Alt1", 0.0F, cvt(f1, 0.0F, 6000F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("Z_Alt1Set", 0.0F, (((Do217) aircraft()).fSightCurAltitude * 360F) / 6000F, 0.0F);
		Pn.set(fm.Loc);
		Pn.z = (double) f1 - Engine.cur.land.HQ(Pn.x, Pn.y);
		mesh.chunkSetAngles("z_AFN101", 0.0F, cvt((float) Pn.z, 0.0F, 750F, 0.0F, 223F), 0.0F);
		mesh.chunkSetAngles("z_Speed", 0.0F, floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 50F, 800F, 0.0F, 15F), speedometerScale), 0.0F);
		mesh.chunkSetAngles("z_VelX", 0.0F, floatindex(cvt(fm.getSpeedKMH(), 50F, 800F, 0.0F, 15F), speedometerScale), 0.0F);
		float f2 = fm.EI.engines[0].getRPM();
		float f3 = fm.EI.engines[1].getRPM();
		mesh.chunkSetAngles("z_RPM1", 0.0F, cvt(f2, 600F, 3600F, -172F, 151F), 0.0F);
		mesh.chunkSetAngles("z_RPM2", 0.0F, cvt(f3, 600F, 3600F, -172F, 151F), 0.0F);
		mesh.chunkSetAngles("z_ATA1", 0.0F, pictManf1 = 0.9F * pictManf1 + 0.1F * cvt(fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, -162.5F, 162.5F), 0.0F);
		mesh.chunkSetAngles("z_ATA2", 0.0F, pictManf2 = 0.9F * pictManf2 + 0.1F * cvt(fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, -162.5F, 162.5F), 0.0F);
		mesh.chunkSetAngles("Z_Hours", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		mesh.chunkSetAngles("Z_Minutes", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("z_ThrottleL", 0.0F, -25F * interp(setNew.throttlel, setOld.throttlel, f), 0.0F);
		mesh.chunkSetAngles("z_ThrottleR", 0.0F, -25F * interp(setNew.throttler, setOld.throttler, f), 0.0F);
		float f4 = setNew.vspeed * 9F;
		mesh.chunkSetAngles("Z_Variometer", 0.0F, f4, 0.0F);
		w.set(fm.getW());
		fm.Or.transform(w);
		mesh.chunkSetAngles("Z_Stick", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
		float f5 = cvt(getBall(6D), -6F, 6F, -0.02F, 0.02F);
		resetYPRmodifier();
		Cockpit.xyz[0] = -f5;
		mesh.chunkSetLocate("Z_Ball", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetLocate("Z_Ball01", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetLocate("Z_Ball02", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("z_AH1", 0.0F, -fm.Or.getKren(), 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(fm.Or.getTangage(), -45F, 45F, 0.011F, -0.015F);
		mesh.chunkSetLocate("Z_Horizon", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("z_ElevTrim", 0.0F, -cvt(interp(setNew.elevTrim, setOld.elevTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
		mesh.chunkSetAngles("z_RudderTrim", 0.0F, cvt(interp(setNew.rudderTrim, setOld.rudderTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
		mesh.chunkSetAngles("z_AileronTrim", 0.0F, cvt(interp(setNew.ailTrim, setOld.ailTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
		float f6 = setNew.cons;
		float f7 = (f6 * f2) / (f2 + f3);
		float f8 = (f6 * f3) / (f2 + f3);
		mesh.chunkSetAngles("z_FuelCons1", 0.0F, cvt(f7, 0.0F, 600F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("z_FuelCons2", 0.0F, cvt(f8, 0.0F, 600F, 0.0F, 360F), 0.0F);
		float f9 = fm.M.fuel / 0.72F;
		mesh.chunkSetAngles("z_Fuel3", 0.0F, cvt(f9, 0.0F, 1100F, 0.0F, 69F), 0.0F);
		mesh.chunkSetAngles("z_Fuel1", 0.0F, cvt(f9, 1100F, 2670F, 0.0F, 84F), 0.0F);
		mesh.chunkSetAngles("z_Fuel2", 0.0F, cvt(f9, 1100F, 2670F, 0.0F, 84F), 0.0F);
		mesh.chunkSetAngles("z_OilPres1", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness(), 0.0F, 15F, 0.0F, -135F), 0.0F);
		mesh.chunkSetAngles("z_OilPres2", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[1].tOilOut * fm.EI.engines[1].getReadyness(), 0.0F, 15F, 0.0F, -135F), 0.0F);
		mesh.chunkSetAngles("z_FuelPres1", 0.0F, cvt(fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F);
		mesh.chunkSetAngles("z_FuelPres2", 0.0F, cvt(fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F);
		mesh.chunkSetAngles("Z_TempCylL", 0.0F, cvt(fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 75F), 0.0F);
		mesh.chunkSetAngles("Z_TempCylR", 0.0F, cvt(fm.EI.engines[1].tOilOut, 0.0F, 160F, 0.0F, 75F), 0.0F);
		mesh.chunkSetAngles("z_OAT", 0.0F, floatindex(cvt(Atmosphere.temperature((float) fm.Loc.z) - 273.15F, -60F, 40F, 0.0F, 10F), OATscale), 0.0F);
		mesh.chunkSetAngles("z_TrimIndicator", 0.0F, cvt(interp(setNew.elevTrim, setOld.elevTrim, f), -0.5F, 0.5F, 40F, -40F), 0.0F);
		mesh.chunkSetAngles("PropPitchLeverL", 0.0F, -70F * (1.0F - interp(setNew.propL, setOld.propL, f)), 0.0F);
		mesh.chunkSetAngles("PropPitchLeverR", 0.0F, -70F * (1.0F - interp(setNew.propR, setOld.propR, f)), 0.0F);
		mesh.chunkVisible("RWL_Ein", fm.CT.getGear() < 0.01F || !fm.Gears.lgear);
		mesh.chunkVisible("RWL_Aus", fm.CT.getGear() > 0.99F && fm.Gears.lgear);
		mesh.chunkVisible("RWR_Ein", fm.CT.getGear() < 0.01F || !fm.Gears.rgear);
		mesh.chunkVisible("RWR_Aus", fm.CT.getGear() > 0.99F && fm.Gears.rgear);
		mesh.chunkVisible("LK_Ein", fm.CT.getFlap() < 0.1F);
		mesh.chunkVisible("LK_Start", fm.CT.getFlap() > 0.1F && fm.CT.getFlap() < 0.5F);
		mesh.chunkVisible("LK_Aus", fm.CT.getFlap() > 0.5F);
		mesh.chunkSetAngles("Z_Turret1A", 0.0F, -fm.turret[0].tu[0], 0.0F);
		mesh.chunkSetAngles("Z_Turret1B", 0.0F, fm.turret[0].tu[1], 0.0F);
		mesh.chunkSetAngles("Z_Turret3A", 0.0F, -fm.turret[2].tu[0], 0.0F);
		mesh.chunkSetAngles("Z_Turret3B", 0.0F, fm.turret[2].tu[1], 0.0F);
		mesh.chunkSetAngles("Z_Turret5A", 0.0F, -fm.turret[4].tu[0], 0.0F);
		mesh.chunkSetAngles("Z_Turret5B", 0.0F, fm.turret[4].tu[1], 0.0F);
		if (useRealisticNavigationInstruments()) {
			mesh.chunkSetAngles("Z_Compass1", -setNew.waypointAzimuth.getDeg(f * 0.1F) - 90F, 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Compass2", setNew.azimuth.getDeg(f) - setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Compass3", setNew.azimuth.getDeg(f) - setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Compass4", -setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Compass5", setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Compass7", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Compass8", setNew.waypointAzimuth.getDeg(f * 0.1F) + 90F, 0.0F, 0.0F);
			mesh.chunkSetAngles("zNavP", -(setNew.waypointAzimuth.getDeg(f * 0.1F) - setNew.azimuth.getDeg(f)), 0.0F, 0.0F);
			float f10 = setNew.beaconDirection;
			float f11 = setNew.beaconRange;
			mesh.chunkSetAngles("z_AFN2a", 0.0F, cvt(f10, -40F, 40F, -32F, 32F), 0.0F);
			mesh.chunkSetAngles("z_AFN2b", 0.0F, 0.0F, cvt(f11, 0.0F, 1.0F, -20F, 15F));
			mesh.chunkVisible("AFN1_RED", isOnBlindLandingMarker());
		} else {
			mesh.chunkSetAngles("Z_Compass1", -setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Compass2", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Compass3", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Compass4", -setNew.azimuth.getDeg(f), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Compass5", 0.0F, 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Compass7", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Compass8", setNew.waypointAzimuth.getDeg(f * 0.1F) + 90F, 0.0F, 0.0F);
			mesh.chunkSetAngles("zNavP", -setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
		}
	}

	protected void mydebugcockpit(String s) {
		System.out.println(s);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 4) != 0) {
			mesh.chunkVisible("XGlassHoles1", true);
			mesh.chunkVisible("XGlassHoles3", true);
			mesh.chunkVisible("Alt-dmg", true);
			mesh.chunkVisible("z_Alt", false);
			mesh.chunkVisible("Z_Alt1Set", false);
			mesh.chunkVisible("RPM-dmg", true);
			mesh.chunkVisible("z_RPM1", false);
			mesh.chunkVisible("z_RPM2", false);
		}
		if ((fm.AS.astateCockpitState & 8) != 0) {
			mesh.chunkVisible("XGlassHoles7", true);
			mesh.chunkVisible("XGlassHoles4", true);
			mesh.chunkVisible("XGlassHoles2", true);
			mesh.chunkVisible("ATA-dmg", true);
			mesh.chunkVisible("z_ATA", false);
			mesh.chunkVisible("Speed-dmg", true);
			mesh.chunkVisible("z_Speed", false);
			mesh.chunkVisible("FuelCons2-dmg", true);
			mesh.chunkVisible("z_FuelCons2", false);
		}
		if ((fm.AS.astateCockpitState & 0x10) != 0) {
			mesh.chunkVisible("XGlassHoles5", true);
			mesh.chunkVisible("XGlassHoles3", true);
			mesh.chunkVisible("OAT-dmg", true);
			mesh.chunkVisible("z_OAT", false);
			mesh.chunkVisible("Variometer-dmg", true);
			mesh.chunkVisible("z_Variometer", false);
		}
		if ((fm.AS.astateCockpitState & 0x20) != 0)
			mesh.chunkVisible("XGlassHoles1", true);
		mesh.chunkVisible("XGlassHoles6", true);
		mesh.chunkVisible("XGlassHoles4", true);
		if ((fm.AS.astateCockpitState & 1) != 0)
			mesh.chunkVisible("XGlassHoles6", true);
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("XGlassHoles7", true);
			mesh.chunkVisible("XGlassHoles2", true);
			mesh.chunkVisible("Oxy-dmg", true);
			mesh.chunkVisible("z_Oxy", false);
			mesh.chunkVisible("Press-dmg", true);
			mesh.chunkVisible("z_OilPres1", false);
		}
		if ((fm.AS.astateCockpitState & 0x80) != 0) {
			mesh.chunkVisible("XGlassHoles5", true);
			mesh.chunkVisible("XGlassHoles2", true);
		}
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
	private float prevFuel;
	protected SoundFX buzzerFX;
	private static final float speedometerScale[] = { 0.0F, 17F, 41F, 66F, 93F, 119F, 139F, 164F, 188F, 214F, 239F, 266F, 292F, 317F, 341F, 372F };
	private static final float OATscale[] = { 0.0F, 7F, 17F, 27F, 37F, 47F, 56F, 65F, 72F, 80F, 85F };
	private Point3d Pn;
	private float pictManf1;
	private float pictManf2;
	public Vector3f w;

}
