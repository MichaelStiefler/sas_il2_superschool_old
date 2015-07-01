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

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Time;

public class CockpitME_210CA1ZSTR extends CockpitPilot {
	private class Variables {

		float altimeter;
		float throttle1;
		float throttle2;
		float dimPosition;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		float turn;
		float beaconDirection;
		float beaconRange;
		float mix1;
		float mix2;
		float vspeed;
		float radioalt;

		private Variables() {
			azimuth = new AnglesFork();
			waypointAzimuth = new AnglesFork();
		}

	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (ME_210CA1ZSTR.bChangedPit) {
				reflectPlaneToModel();
				ME_210CA1ZSTR.bChangedPit = false;
			}
			setTmp = setOld;
			setOld = setNew;
			setNew = setTmp;
			setNew.altimeter = fm.getAltitude();
			if (cockpitDimControl) {
				if (setNew.dimPosition > 0.0F)
					setNew.dimPosition = setNew.dimPosition - 0.05F;
			} else if (setNew.dimPosition < 1.0F)
				setNew.dimPosition = setNew.dimPosition + 0.05F;
			setNew.throttle1 = 0.91F * setOld.throttle1 + 0.09F * fm.EI.engines[0].getControlThrottle();
			setNew.throttle2 = 0.91F * setOld.throttle2 + 0.09F * fm.EI.engines[1].getControlThrottle();
			setNew.mix1 = 0.88F * setOld.mix1 + 0.12F * fm.EI.engines[0].getControlMix();
			setNew.mix2 = 0.88F * setOld.mix2 + 0.12F * fm.EI.engines[1].getControlMix();
			float f = waypointAzimuth();
			if (useRealisticNavigationInstruments()) {
				setNew.waypointAzimuth.setDeg(f - 90F);
				setOld.waypointAzimuth.setDeg(f - 90F);
			} else {
				setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(1.0F));
			}
			setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
			w.set(fm.getW());
			((FlightModelMain) (fm)).Or.transform(w);
			setNew.turn = (12F * setOld.turn + ((Tuple3f) (w)).z) / 13F;
			World.cur();
			World.land();
			setNew.radioalt = 0.9F * setOld.radioalt + 0.1F * (fm.getAltitude() - Landscape.HQ_Air((float) ((Tuple3d) (((FlightModelMain) (fm)).Loc)).x, (float) ((Tuple3d) (((FlightModelMain) (fm)).Loc)).y));
			setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
			setNew.beaconDirection = (10F * setOld.beaconDirection + getBeaconDirection()) / 11F;
			setNew.beaconRange = (10F * setOld.beaconRange + getBeaconRange()) / 11F;
			middleAngle = middleAngleTarget * 0.05F + 0.95F * middleAngle;
			return true;
		}

		Interpolater() {
		}
	}

	protected float waypointAzimuth() {
		return waypointAzimuthInvertMinus(30F);
	}

	protected void reflectPlaneToModel() {
	}

	public CockpitME_210CA1ZSTR() {
		super("3DO/Cockpit/Me210/hier.him", "bf109");
		gun = new Gun[3];
		setOld = new Variables();
		setNew = new Variables();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		pictManifold1 = 0.0F;
		pictManifold2 = 0.0F;
		w = new Vector3f();
		middleAngle = 0.0F;
		middleAngleTarget = 0.0F;
		setNew.dimPosition = 0.0F;
		super.cockpitDimControl = !super.cockpitDimControl;
		super.cockpitNightMats = (new String[] { "bague1", "bague2", "boitier", "brasdele", "cadran1", "cadran2", "cadran7", "cadran8", "consoledr2", "enggauge", "extra", "extraAFN", "fils", "gauche", "oxyge", "pan2a", "panel", "skala" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		printCompassHeading = true;
		limits6DoF = (new float[] { 0.65F, 0.055F, -0.07F, 0.11F, 0.21F, -0F, 0.05F, -0.025F });
	}

	public void reflectWorldToInstruments(float f) {
		if (gun[0] == null) {
			gun[0] = ((Aircraft) fm.actor).getGunByHookName("_CANNON01");
			gun[1] = ((Aircraft) fm.actor).getGunByHookName("_CANNON02");
			gun[2] = ((Aircraft) fm.actor).getGunByHookName("_MGUN01");
		}
		resetYPRmodifier();
		Cockpit.xyz[2] = 0.06815F * interp(setNew.dimPosition, setOld.dimPosition, f);
		super.mesh.chunkVisible("Z_GearLGreen", ((FlightModelMain) (super.fm)).CT.getGear() == 1.0F && ((FlightModelMain) (super.fm)).Gears.lgear);
		super.mesh.chunkVisible("Z_GearRGreen", ((FlightModelMain) (super.fm)).CT.getGear() == 1.0F && ((FlightModelMain) (super.fm)).Gears.rgear);
		super.mesh.chunkVisible("Z_GearLRed", ((FlightModelMain) (super.fm)).CT.getGear() == 0.0F);
		super.mesh.chunkVisible("Z_GearRRed", ((FlightModelMain) (super.fm)).CT.getGear() == 0.0F);
		super.mesh.chunkVisible("Z_Red4", ((FlightModelMain) (super.fm)).CT.getFlap() < 0.1F);
		super.mesh.chunkVisible("Z_GearLRed", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F);
		super.mesh.chunkVisible("Z_Red6", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F);
		super.mesh.chunkVisible("Z_GearRRed", ((FlightModelMain) (super.fm)).CT.getGear() < 0.05F);
		super.mesh.chunkVisible("Z_Red8", ((FlightModelMain) (super.fm)).CT.getFlap() < 0.1F);
		super.mesh.chunkVisible("Z_Green1", ((FlightModelMain) (super.fm)).CT.getFlap() > 0.665F);
		super.mesh.chunkVisible("Z_GearLGreen", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F && ((FlightModelMain) (super.fm)).Gears.lgear);
		super.mesh.chunkVisible("Z_Green3", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F && ((FlightModelMain) (super.fm)).Gears.cgear);
		super.mesh.chunkVisible("Z_GearRGreen", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F && ((FlightModelMain) (super.fm)).Gears.rgear);
		super.mesh.chunkVisible("Z_Green5", ((FlightModelMain) (super.fm)).CT.getFlap() > 0.665F);
		super.mesh.chunkVisible("Z_White1", ((FlightModelMain) (super.fm)).CT.getFlap() > 0.265F);
		super.mesh.chunkVisible("Z_White2", ((FlightModelMain) (super.fm)).CT.getFlap() > 0.265F);
		super.mesh.chunkVisible("Z_FuelL1", ((FlightModelMain) (super.fm)).M.fuel < 36F);
		super.mesh.chunkVisible("Z_FuelL2", ((FlightModelMain) (super.fm)).M.fuel < 102F);
		super.mesh.chunkVisible("Z_FuelR1", ((FlightModelMain) (super.fm)).M.fuel < 36F);
		super.mesh.chunkVisible("Z_FuelR2", ((FlightModelMain) (super.fm)).M.fuel < 102F);
		if (gun[0] != null)
			super.mesh.chunkVisible("Z_AmmoL", gun[0].countBullets() == 0);
		if (gun[1] != null)
			super.mesh.chunkVisible("Z_AmmoR", gun[1].countBullets() == 0);
		super.mesh.chunkSetAngles("Z_Columnbase", 0.0F, -(pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl) * 10F, 0.0F);
		super.mesh.chunkSetAngles("Z_Column", 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl) * 15F, 0.0F);
		resetYPRmodifier();
		if (((FlightModelMain) (super.fm)).CT.saveWeaponControl[1])
			Cockpit.xyz[2] = 0.00545F;
		super.mesh.chunkSetLocate("Z_Columnbutton1", Cockpit.xyz, Cockpit.ypr);
		resetYPRmodifier();
		Cockpit.xyz[2] = -0.05F * ((FlightModelMain) (super.fm)).CT.getRudder();
		super.mesh.chunkSetLocate("Z_LeftPedal", Cockpit.xyz, Cockpit.ypr);
		Cockpit.xyz[2] = 0.05F * ((FlightModelMain) (super.fm)).CT.getRudder();
		super.mesh.chunkSetLocate("Z_RightPedal", Cockpit.xyz, Cockpit.ypr);
		super.mesh.chunkSetAngles("Z_Throttle1", interp(setNew.throttle1, setOld.throttle1, f) * 52.2F, 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Throttle2", interp(setNew.throttle2, setOld.throttle2, f) * 52.2F, 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Mixture1", interp(setNew.mix1, setOld.mix2, f) * 52.2F, 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Mixture2", interp(setNew.mix1, setOld.mix2, f) * 52.2F, 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Pitch1", ((FlightModelMain) (super.fm)).EI.engines[0].getControlProp() * 60F, 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Pitch2", ((FlightModelMain) (super.fm)).EI.engines[1].getControlProp() * 60F, 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Radiat1", 0.0F, 0.0F, ((FlightModelMain) (super.fm)).EI.engines[0].getControlRadiator() * 15F);
		super.mesh.chunkSetAngles("Z_Radiat2", 0.0F, 0.0F, ((FlightModelMain) (super.fm)).EI.engines[1].getControlRadiator() * 15F);
		if (useRealisticNavigationInstruments()) {
			super.mesh.chunkSetAngles("Z_Azimuth1", setNew.azimuth.getDeg(f) - setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
			super.mesh.chunkSetAngles("Z_Compass1", -setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
			super.mesh.chunkSetAngles("Z_Autopilot1", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
			super.mesh.chunkSetAngles("Z_Autopilot2", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
		} else {
			super.mesh.chunkSetAngles("Z_Compass1", -setNew.azimuth.getDeg(f), 0.0F, 0.0F);
			super.mesh.chunkSetAngles("Z_Azimuth1", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
			super.mesh.chunkSetAngles("Z_Autopilot1", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
			super.mesh.chunkSetAngles("Z_Autopilot2", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
		}
		if (gun[0] != null)
			super.mesh.chunkSetAngles("Z_AmmoCounter1", cvt(gun[0].countBullets(), 0.0F, 500F, 13F, 0.0F), 0.0F, 0.0F);
		if (gun[2] != null)
			super.mesh.chunkSetAngles("Z_AmmoCounter2", cvt(gun[2].countBullets(), 0.0F, 500F, 13F, 0.0F), 0.0F, 0.0F);
		if (gun[1] != null)
			super.mesh.chunkSetAngles("Z_AmmoCounter3", cvt(gun[1].countBullets(), 0.0F, 500F, 13F, 0.0F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Second1", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Panelmid", 0.0F, cvt(middleAngle, 0.0F, 1.0F, 0.0F, -80F), 0.0F);
		super.mesh.chunkSetAngles("PanelmidAFN", 0.0F, cvt(middleAngle, 0.0F, 1.0F, 0.0F, -80F), 0.0F);
		super.mesh.chunkSetAngles("Z_TurnBank1", cvt(setNew.turn, -0.23562F, 0.23562F, 21F, -21F), 0.0F, 0.0F);
		float f1 = getBall(4D);
		super.mesh.chunkSetAngles("Z_TurnBank2", cvt(f1, -4F, 4F, 10F, -10F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_TurnBank3", cvt(f1, -3.8F, 3.8F, 9F, -9F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_TurnBank4", cvt(f1, -3.3F, 3.3F, 7.5F, -7.5F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Horizon1", 0.0F, 0.0F, ((FlightModelMain) (super.fm)).Or.getKren());
		super.mesh.chunkSetAngles("Z_Horizon2", cvt(((FlightModelMain) (super.fm)).Or.getTangage(), -45F, 45F, -23F, 23F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Climb1", cvt(setNew.vspeed, -15F, 15F, -136F, 136F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Speed1", floatindex(cvt(Pitot.Indicator((float) ((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("RPM1", floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("RPM2", floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("ATA1", cvt(pictManifold1 = 0.75F * pictManifold1 + 0.25F * ((FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332.5F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("ATA2", cvt(pictManifold2 = 0.75F * pictManifold2 + 0.25F * ((FlightModelMain) (super.fm)).EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332.5F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("zAlt1", cvt(interp(setNew.radioalt, setOld.radioalt, f), 0.0F, 750F, 0.0F, 228.5F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("zAlt2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("zAlt3", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 14000F, 0.0F, 313F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Fuel1", cvt(((FlightModelMain) (super.fm)).M.fuel / 0.72F, 0.0F, 400F, 0.0F, 66.5F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Temp1", cvt(Atmosphere.temperature((float) ((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z), 233.09F, 313.09F, -42.5F, 42.4F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Temp2", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 68F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Temp3", cvt(((FlightModelMain) (super.fm)).EI.engines[1].tOilOut, 0.0F, 120F, 0.0F, 68F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_AirPressure1", 170F, 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Temp4", -(float) Math.toDegrees(((FlightModelMain) (super.fm)).EI.engines[0].getPropPhi() - ((FlightModelMain) (super.fm)).EI.engines[0].getPropPhiMin()) * 60F, 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Temp6", -(float) Math.toDegrees(((FlightModelMain) (super.fm)).EI.engines[0].getPropPhi() - ((FlightModelMain) (super.fm)).EI.engines[0].getPropPhiMin()) * 5F, 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Temp5", -(float) Math.toDegrees(((FlightModelMain) (super.fm)).EI.engines[1].getPropPhi() - ((FlightModelMain) (super.fm)).EI.engines[1].getPropPhiMin()) * 60F, 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Temp7", -(float) Math.toDegrees(((FlightModelMain) (super.fm)).EI.engines[1].getPropPhi() - ((FlightModelMain) (super.fm)).EI.engines[1].getPropPhiMin()) * 5F, 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Fuelpress1", cvt(((FlightModelMain) (super.fm)).M.fuel > 1.0F ? 0.77F : 0.0F, 0.0F, 2.0F, 0.0F, 160F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Fuelpress2", cvt(((FlightModelMain) (super.fm)).M.fuel > 1.0F ? 0.77F : 0.0F, 0.0F, 2.0F, 0.0F, 160F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_OilPress1", cvt(1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 160F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_OilPress2", cvt(1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 160F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Oiltemp1", floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 40F, 120F, 0.0F, 8F), oilTScale), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_Oiltemp2", floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[1].tOilOut, 40F, 120F, 0.0F, 8F), oilTScale), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Z_AFN22", 0.0F, cvt(setNew.beaconDirection, -45F, 45F, -20F, 20F), 0.0F);
		super.mesh.chunkSetAngles("Z_AFN21", 0.0F, cvt(setNew.beaconRange, 0.0F, 1.0F, 20F, -20F), 0.0F);
		super.mesh.chunkVisible("AFN2_RED", isOnBlindLandingMarker());
	}

	public void toggleLight() {
		super.cockpitLightControl = !super.cockpitLightControl;
		if (super.cockpitLightControl)
			setNightMats(true);
		else
			setNightMats(false);
	}

	public void reflectCockpitState() {
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) != 0) {
			super.mesh.chunkVisible("HullDamage3", true);
			super.mesh.chunkVisible("Revi_D0", false);
			super.mesh.chunkVisible("Z_Z_RETICLE", false);
			super.mesh.chunkVisible("Z_Z_MASK", false);
			super.mesh.chunkVisible("Revi_D1", true);
		}
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 1) != 0)
			super.mesh.chunkVisible("HullDamage2", true);
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) != 0)
			super.mesh.chunkVisible("XGlassDamage1", true);
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 4) != 0)
			super.mesh.chunkVisible("HullDamage1", true);
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 8) != 0)
			super.mesh.chunkVisible("XGlassDamage3", true);
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x80) != 0)
			super.mesh.chunkVisible("XGlassDamage2", true);
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) != 0)
			super.mesh.chunkVisible("HullDamage1", true);
		if ((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) != 0)
			super.mesh.chunkVisible("XGlassDamage4", true);
	}

	public void doToggleDim() {
		if (middleAngleTarget < 0.5F)
			middleAngleTarget = 1.0F;
		else
			middleAngleTarget = 0.0F;
	}

	private Gun gun[];
	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	private float middleAngle;
	private float middleAngleTarget;
	private float pictAiler;
	private float pictElev;
	private float pictManifold1;
	private float pictManifold2;
	public Vector3f w;
	private static final float speedometerScale[] = { -14F, 0.0F, 18F, 40F, 68F, 95F, 116F, 139F, 166F, 193F, 209F, 235F, 264F, 289F, 310F, 333F, 346F };
	private static final float rpmScale[] = { 0.0F, 36.5F, 70F, 111F, 149.5F, 186.5F, 233.5F, 282.5F, 308F, 318.5F };
	private static final float oilTScale[] = { 0.0F, 24.5F, 47.5F, 74F, 102.5F, 139F, 188F, 227.5F, 290.5F };

}
