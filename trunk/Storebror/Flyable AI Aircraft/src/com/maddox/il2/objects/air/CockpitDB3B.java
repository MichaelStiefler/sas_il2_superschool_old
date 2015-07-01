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
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitDB3B extends CockpitPilot {
	private class Variables {

		float throttle1;
		float throttle2;
		float prop1;
		float prop2;
		float mix1;
		float mix2;
		float man1;
		float man2;
		float altimeter;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		AnglesFork waypointDirection;
		float vspeed;
		float inert;
		float xyz[] = { 0.0F, 0.0F, 0.0F };
		float ypr[] = { 0.0F, 0.0F, 0.0F };

		private Variables() {
			azimuth = new AnglesFork();
			waypointAzimuth = new AnglesFork();
			waypointDirection = new AnglesFork();
		}

	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
            if(IL_4_DB3B.bChangedPit)
                reflectPlaneToModel();
			if (fm != null) {
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				setNew.throttle1 = 0.9F * setOld.throttle1 + 0.1F * fm.EI.engines[0].getControlThrottle();
				setNew.prop1 = 0.9F * setOld.prop1 + 0.1F * fm.EI.engines[0].getControlProp();
				setNew.mix1 = 0.8F * setOld.mix1 + 0.2F * fm.EI.engines[0].getControlMix();
				setNew.man1 = 0.92F * setOld.man1 + 0.08F * fm.EI.engines[0].getManifoldPressure();
				setNew.throttle2 = 0.9F * setOld.throttle2 + 0.1F * fm.EI.engines[1].getControlThrottle();
				setNew.prop2 = 0.9F * setOld.prop2 + 0.1F * fm.EI.engines[1].getControlProp();
				setNew.mix2 = 0.8F * setOld.mix2 + 0.2F * fm.EI.engines[1].getControlMix();
				setNew.man2 = 0.92F * setOld.man2 + 0.08F * fm.EI.engines[1].getManifoldPressure();
				setNew.altimeter = fm.getAltitude();
				setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
				setNew.vspeed = (100F * setOld.vspeed + fm.getVertSpeed()) / 101F;
				if (useRealisticNavigationInstruments()) {
					setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), getBeaconDirection());
				} else {
					float f = waypointAzimuth();
					setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f - fm.Or.azimut());
					setNew.waypointDirection.setDeg(setOld.waypointDirection.getDeg(1.0F), f);
				}
				mesh.chunkSetAngles("TurretA", 0.0F, -aircraft().FM.turret[0].tu[0], 0.0F);
				mesh.chunkSetAngles("TurretB", 0.0F, -aircraft().FM.turret[0].tu[1], 0.0F);
				mesh.chunkSetAngles("Ret_Base", 0.0F, aircraft().FM.turret[0].tu[1], 0.0F);
				mesh.chunkSetAngles("Reticle", 0.0F, aircraft().FM.turret[0].tu[1], 0.0F);
				setNew.inert = 0.999F * setOld.inert + 0.001F * (fm.EI.engines[0].getStage() == 6 ? 0.867F : 0.0F);
				if (aircraft().hierMesh().isChunkVisible("Pilot2_D0") || aircraft().hierMesh().isChunkVisible("Pilot2_D1")) {
					mesh.chunkVisible("Pilot2a_D0", true);
					mesh.chunkVisible("Pilot2b_D0", false);
				}
				if (aircraft().hierMesh().isChunkVisible("Pilot2a_D0")) {
					mesh.chunkVisible("Pilot2a_D0", false);
					mesh.chunkVisible("Pilot2b_D0", true);
				}
				float f1 = 15F;
				if (flapsLever != 0.0F && flaps == fm.CT.getFlap()) {
					flapsLever = flapsLever * 0.8F;
					if (Math.abs(flapsLever) < 0.1F)
						flapsLever = 0.0F;
				} else if (flaps < fm.CT.getFlap()) {
					flaps = fm.CT.getFlap();
					flapsLever = flapsLever + 2.0F;
					if (flapsLever > f1)
						flapsLever = f1;
				} else if (flaps > fm.CT.getFlap()) {
					flaps = fm.CT.getFlap();
					flapsLever = flapsLever - 2.0F;
					if (flapsLever < -f1)
						flapsLever = -f1;
				}
				f1 = 20F;
				if (gearsLever != 0.0F && gears == fm.CT.getGear()) {
					gearsLever = gearsLever * 0.8F;
					if (Math.abs(gearsLever) < 0.1F)
						gearsLever = 0.0F;
				} else if (gears < fm.CT.getGear()) {
					gears = fm.CT.getGear();
					gearsLever = gearsLever + 2.0F;
					if (gearsLever > f1)
						gearsLever = f1;
				} else if (gears > fm.CT.getGear()) {
					gears = fm.CT.getGear();
					gearsLever = gearsLever - 2.0F;
					if (gearsLever < -f1)
						gearsLever = -f1;
				}
			}
			return true;
		}

		Interpolater() {
		}
	}

	protected float waypointAzimuth() {
		return super.waypointAzimuthInvertMinus(10F);
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("CF_D" + aircraft().chunkDamageVisible("CF"), false);
			aircraft().hierMesh().chunkVisible("Cockpit1_D0", false);
			aircraft().hierMesh().chunkVisible("Cockpit2_D0", false);
			aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("CF_D" + aircraft().chunkDamageVisible("CFE"), true);
		aircraft().hierMesh().chunkVisible("Cockpit1_D0", true);
		aircraft().hierMesh().chunkVisible("Cockpit2_D0", true);
		aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
		super.doFocusLeave();
	}

	public CockpitDB3B() {
		super("3DO/Cockpit/Il-4/hier.him", "he111");
		bNeedSetUp = true;
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		flapsLever = 0.0F;
		flaps = 0.0F;
		gearsLever = 0.0F;
		gears = 0.0F;
		cockpitNightMats = (new String[] { "Prib_One", "DPrib_One", "Prib_Two", "DPrib_Two", "Prib_Three", "DPrib_Three", "Prib_Four", "Prib_Five", "DPrib_Five", "Prib_Six", "DPrib_Six", "Prib_Seven", "DPrib_Seven", "Prib_Eight", "DPrib_Eight" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		if (acoustics != null)
			acoustics.globFX = new ReverbFXRoom(0.45F);
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.58F);
		mesh.chunkSetLocate("CSlider", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("hTrim_Oelir", 0.0F, 1000F * fm.CT.getTrimAileronControl(), 0.0F);
		mesh.chunkSetAngles("hTrim_Rudd", 0.0F, 1000F * fm.CT.getTrimRudderControl(), 0.0F);
		mesh.chunkSetAngles("hTrim_Elev", 0.0F, 1000F * fm.CT.getTrimElevatorControl(), 0.0F);
		mesh.chunkSetAngles("hThrot_L", 0.0F, 31F * interp(setNew.throttle1, setOld.throttle1, f), 0.0F);
		mesh.chunkSetAngles("hThrot_R", 0.0F, 31F * interp(setNew.throttle2, setOld.throttle2, f), 0.0F);
		mesh.chunkSetAngles("tThrot_L", 0.0F, -31F * interp(setNew.throttle1, setOld.throttle1, f), 0.0F);
		mesh.chunkSetAngles("tThrot_R", 0.0F, -31F * interp(setNew.throttle2, setOld.throttle2, f), 0.0F);
		mesh.chunkSetAngles("hPitch_L", 0.0F, 45F * interp(setNew.prop1, setOld.prop1, f), 0.0F);
		mesh.chunkSetAngles("hPitch_R", 0.0F, 45F * interp(setNew.prop2, setOld.prop2, f), 0.0F);
		mesh.chunkSetAngles("tPitch_L", 0.0F, -45F * interp(setNew.prop1, setOld.prop1, f), 0.0F);
		mesh.chunkSetAngles("tPitch_R", 0.0F, -45F * interp(setNew.prop2, setOld.prop2, f), 0.0F);
		mesh.chunkSetAngles("hMix_L", 0.0F, 40F * interp(setNew.mix1, setOld.mix1, f), 0.0F);
		mesh.chunkSetAngles("hMix_R", 0.0F, 40F * interp(setNew.mix2, setOld.mix2, f), 0.0F);
		mesh.chunkSetAngles("Pedal_L1a", 0.0F, -7F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("Pedal_L1b", 0.0F, 7F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("Pedal_R1a", 0.0F, 7F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("Pedal_R1b", 0.0F, -7F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("Pedal_L2", 0.0F, 7F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("tPedal_L2", 0.0F, -7F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("Pedal_R2", 0.0F, -7F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("tPedal_R2", 0.0F, 7F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("Column", 0.0F, -10F * (pictElev = 0.65F * pictElev + 0.35F * fm.CT.ElevatorControl), 0.0F);
		mesh.chunkSetAngles("Wheel", 0.0F, 45F * (pictAiler = 0.65F * pictAiler + 0.35F * fm.CT.AileronControl), 0.0F);
		mesh.chunkSetAngles("hNagn_L", 0.0F, 30F * (float) fm.EI.engines[0].getControlCompressor(), 0.0F);
		mesh.chunkSetAngles("hNagn_R", 0.0F, 30F * (float) fm.EI.engines[1].getControlCompressor(), 0.0F);
		mesh.chunkSetAngles("hGears", 0.0F, -gearsLever, 0.0F);
		mesh.chunkSetAngles("hFlaps", 0.0F, -flapsLever, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[2] = -0.025F * fm.CT.AileronControl;
		mesh.chunkSetLocate("tWheel_R", Cockpit.xyz, Cockpit.ypr);
		resetYPRmodifier();
		Cockpit.xyz[2] = 0.025F * fm.CT.AileronControl;
		mesh.chunkSetLocate("tWheel_L", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Brake_L", 0.0F, 10F * fm.CT.BrakeControl, 0.0F);
		mesh.chunkSetAngles("Brake_R", 0.0F, 10F * fm.CT.BrakeControl, 0.0F);
		mesh.chunkSetAngles("hMagn_L", 0.0F, cvt(fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F);
		mesh.chunkSetAngles("hMagn_R", 0.0F, cvt(fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, -90F), 0.0F);
		mesh.chunkSetAngles("swLandLight", 0.0F, fm.AS.bLandingLightOn ? 60F : 0.0F, 0.0F);
		mesh.chunkSetAngles("swNavLight", 0.0F, fm.AS.bNavLightsOn ? 60F : 0.0F, 0.0F);
		mesh.chunkSetAngles("zAlt1a", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
		mesh.chunkSetAngles("zAlt1b", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("zSpeed1a", 0.0F, floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F);
		mesh.chunkSetAngles("zAzimAPa", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
		mesh.chunkSetAngles("zAzimAPb", 0.0F, setNew.waypointDirection.getDeg(f), 0.0F);
		resetYPRmodifier();
		setOld.xyz[2] = cvt(fm.Or.getTangage(), -40F, 40F, 0.025F, -0.025F);
		mesh.chunkSetLocate("zHorizon1a", setOld.xyz, setOld.ypr);
		mesh.chunkSetAngles("zHorizon1b", 0.0F, -fm.Or.getKren(), 0.0F);
		resetYPRmodifier();
		setOld.xyz[2] = cvt(fm.Or.getTangage(), -20F, 20F, 0.03F, -0.03F);
		mesh.chunkSetLocate("zHorAPa", setOld.xyz, setOld.ypr);
		mesh.chunkSetAngles("zHorAPb", 0.0F, cvt(fm.Or.getKren(), -40F, 40F, -38F, 38F), 0.0F);
		resetYPRmodifier();
		w.set(fm.getW());
		fm.Or.transform(w);
		mesh.chunkSetAngles("zTurn1a", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 25F, -25F), 0.0F);
		mesh.chunkSetAngles("zSlide1a", 0.0F, -cvt(getBall(8D), -8F, 8F, 25F, -25F), 0.0F);
		mesh.chunkSetAngles("zVariometer1a", 0.0F, cvt(setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F);
		mesh.chunkSetAngles("zSlideAP", 0.0F, -cvt(getBall(8D), -8F, 8F, 13F, -13F), 0.0F);
		mesh.chunkSetAngles("zRPM1a_L", 0.0F, cvt(fm.EI.engines[0].getRPM(), 0.0F, 2850F, 0.0F, 256.5F), 0.0F);
		mesh.chunkSetAngles("zRPM1a_R", 0.0F, cvt(fm.EI.engines[1].getRPM(), 0.0F, 2850F, 0.0F, 256.5F), 0.0F);
		mesh.chunkSetAngles("zManifold1a_L", 0.0F, floatindex(cvt(fm.EI.engines[0].getManifoldPressure(), 0.399966F, 2.133152F, 3F, 16F), manifoldScale), 0.0F);
		mesh.chunkSetAngles("zManifold1a_R", 0.0F, floatindex(cvt(fm.EI.engines[1].getManifoldPressure(), 0.399966F, 2.133152F, 3F, 16F), manifoldScale), 0.0F);
		mesh.chunkSetAngles("zGas1a", 0.0F, cvt(fm.M.fuel / 0.725F, 0.0F, 1200F, 0.0F, 180F), 0.0F);
		mesh.chunkSetAngles("zOilPrs1a_L", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 180F), 0.0F);
		mesh.chunkSetAngles("zOilPrs1a_R", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[1].tOilOut, 0.0F, 15F, 0.0F, 180F), 0.0F);
		mesh.chunkSetAngles("zGasPrs1a_L", 0.0F, cvt(fm.M.fuel > 1.0F ? cvt(fm.EI.engines[0].getRPM(), 0.0F, 3050F, 0.0F, 4F) : 0.0F, 0.0F, 8F, 0.0F, -180F), 0.0F);
		mesh.chunkSetAngles("zGasPrs1a_R", 0.0F, cvt(fm.M.fuel > 1.0F ? cvt(fm.EI.engines[1].getRPM(), 0.0F, 3050F, 0.0F, 4F) : 0.0F, 0.0F, 8F, 0.0F, -180F), 0.0F);
		mesh.chunkSetAngles("zCylHead_L", 0.0F, cvt(fm.EI.engines[0].tWaterOut, 0.0F, 300F, 0.0F, 70F), 0.0F);
		mesh.chunkSetAngles("zCylHead_R", 0.0F, cvt(fm.EI.engines[1].tWaterOut, 0.0F, 300F, 0.0F, 70F), 0.0F);
		mesh.chunkSetAngles("zTOilOut1a_L", 0.0F, cvt(fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 180F), 0.0F);
		mesh.chunkSetAngles("zTOilOut1a_R", 0.0F, cvt(fm.EI.engines[1].tOilOut, 0.0F, 125F, 0.0F, 180F), 0.0F);
		mesh.chunkSetAngles("zFlaps", 0.0F, cvt(fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, -168F), 0.0F);
		mesh.chunkSetAngles("zClock1a", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("zClock1b", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		mesh.chunkSetAngles("zClock1c", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 21600F), 0.0F);
		mesh.chunkSetAngles("zRPK2", 0.0F, cvt(setNew.waypointAzimuth.getDeg(f), -30F, 30F, -70F, 70F), 0.0F);
		mesh.chunkSetAngles("zCompass1b", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
		mesh.chunkSetAngles("zCompass2b", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
		mesh.chunkVisible("xGearUP_L", fm.CT.getGear() == 0.0F && fm.Gears.lgear);
		mesh.chunkVisible("xGearUP_R", fm.CT.getGear() == 0.0F && fm.Gears.rgear);
		mesh.chunkVisible("xGearDown_L", fm.CT.getGear() == 1.0F && fm.Gears.lgear);
		mesh.chunkVisible("xGearDown_R", fm.CT.getGear() == 1.0F && fm.Gears.rgear);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 1) != 0) {
			mesh.chunkVisible("xGlassDm2", true);
			mesh.chunkVisible("xGlassDm3", true);
			mesh.chunkVisible("xGlassDm4", true);
			mesh.chunkVisible("xHullDm3", true);
		}
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("IPGages_D0", false);
			mesh.chunkVisible("IPGages_D1", true);
			mesh.chunkVisible("zGas1a", false);
			mesh.chunkVisible("zManifold1a_R", false);
			mesh.chunkVisible("zCylHead_L", false);
			mesh.chunkVisible("zPres_L1", false);
			mesh.chunkVisible("zAPOilPres", false);
			mesh.chunkVisible("xHullDm3", true);
		}
		if ((fm.AS.astateCockpitState & 4) != 0) {
			mesh.chunkVisible("xGlassDm3", true);
			mesh.chunkVisible("xHullDm1", true);
		}
		if ((fm.AS.astateCockpitState & 8) != 0) {
			mesh.chunkVisible("xGlassDm1", true);
			mesh.chunkVisible("xGlassDm5", true);
			mesh.chunkVisible("xHullDm3", true);
		}
		if ((fm.AS.astateCockpitState & 0x10) != 0) {
			mesh.chunkVisible("xGlassDm4", true);
			mesh.chunkVisible("xHullDm2", true);
		}
		if ((fm.AS.astateCockpitState & 0x20) != 0) {
			mesh.chunkVisible("xGlassDm1", true);
			mesh.chunkVisible("xGlassDm6", true);
			mesh.chunkVisible("xHullDm3", true);
		}
		if ((fm.AS.astateCockpitState & 2) != 0 && (fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("IPGages_D0", false);
			mesh.chunkVisible("IPGages_D1", false);
			mesh.chunkVisible("IPGages_D2", true);
			mesh.chunkVisible("zAir", false);
			mesh.chunkVisible("zFront_L", false);
			mesh.chunkVisible("zAzimAPa", false);
			mesh.chunkVisible("zAzimAPb", false);
			mesh.chunkVisible("zHorAPa", false);
			mesh.chunkVisible("zHorAPb", false);
			mesh.chunkVisible("zSlideAP", false);
			mesh.chunkVisible("zAPVacPres", false);
			mesh.chunkVisible("zHorizon1b", false);
			mesh.chunkVisible("zSpeed1a", false);
			mesh.chunkVisible("zSlide1a", false);
			mesh.chunkVisible("zTurn1a", false);
			mesh.chunkVisible("zRPM1a_L", false);
			mesh.chunkVisible("zRPM1b_L", false);
			mesh.chunkVisible("zTOilOut1a_L", false);
			mesh.chunkVisible("zTOilOut1a_R", false);
			mesh.chunkVisible("zOilPrs1a_L", false);
			mesh.chunkVisible("zOilPrs1a_R", false);
			mesh.chunkVisible("zGasPrs1a_L", false);
			mesh.chunkVisible("zGasPrs1a_R", false);
			mesh.chunkVisible("zPres_R1", false);
			mesh.chunkVisible("zPres_R3", false);
		}
		retoggleLight();
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl)
			setNightMats(true);
		else
			setNightMats(false);
	}

    protected void reflectPlaneToModel()
    {
        if(isFocused())
        {
            aircraft().hierMesh().chunkVisible("CFE_D0", false);
            aircraft().hierMesh().chunkVisible("CFE_D1", false);
            aircraft().hierMesh().chunkVisible("CFE_D2", false);
            aircraft().hierMesh().chunkVisible("CFE_D3", false);
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

	protected void reflectPlaneMats() {
		com.maddox.il2.engine.Mat mat = aircraft().hierMesh().material(aircraft().hierMesh().materialFind("Gloss1D0o"));
		com.maddox.il2.engine.Mat mat1 = aircraft().hierMesh().material(aircraft().hierMesh().materialFind("Pilot1"));
		mesh.materialReplace("Gloss1D0o", mat);
		mesh.materialReplace("Pilot1", mat1);
	}

	private boolean bNeedSetUp;
	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private float pictAiler;
	private float pictElev;
	private static final float speedometerScale[] = { 0.0F, 0.0F, 15.5F, 50F, 95.5F, 137F, 182.5F, 212F, 230F, 242F, 254.5F, 267.5F, 279F, 292F, 304F, 317F, 329.5F, 330F };
	private static final float manifoldScale[] = { 0.0F, 0.0F, 0.0F, 0.0F, 26F, 52F, 79F, 106F, 132F, 160F, 185F, 208F, 235F, 260F, 286F, 311F, 336F };
	private float flapsLever;
	private float flaps;
	private float gearsLever;
	private float gears;

}
