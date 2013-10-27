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

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitP_36 extends CockpitPilot {
	private class Variables {

		float throttle;
		float prop;
		float altimeter;
		float azimuth;
		float vspeed;
		float waypointAzimuth;

		private Variables() {
		}

	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (fm != null) {
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				setNew.throttle = (10F * setOld.throttle + fm.CT.PowerControl) / 11F;
				setNew.prop = (8F * setOld.prop + fm.CT.getStepControl()) / 9F;
				setNew.altimeter = fm.getAltitude();
				if (Math.abs(fm.Or.getKren()) < 45F)
					setNew.azimuth = (35F * setOld.azimuth + -fm.Or.getYaw()) / 36F;
				if (setOld.azimuth > 270F && setNew.azimuth < 90F)
					setOld.azimuth -= 360F;
				if (setOld.azimuth < 90F && setNew.azimuth > 270F)
					setOld.azimuth += 360F;
				setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
				setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
			}
			return true;
		}

		Interpolater() {
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

	public CockpitP_36() {
		super("3DO/Cockpit/P-40B/CockpitP36.him", "bf109");
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		bNeedSetUp = true;
		tmpP = new Point3d();
		tmpV = new Vector3d();
		cockpitNightMats = (new String[] { "Arrows_Segment", "Indicators_2", "Indicators_2_Dam", "Indicators_3", "Indicators_3_Dam", "Indicators_4", "Indicators_4_Dam", "Indicators_5", "Indicators_5_Dam", "Indicators_6", "Indicators_7" });
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
		Cockpit.xyz[1] = cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.63F);
		mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkVisible("XLampCoolant1", fm.EI.engines[0].tOilOut > fm.EI.engines[0].tOilCritMax);
		mesh.chunkSetAngles("Z_Altimeter2", 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F), 0.0F);
		mesh.chunkSetAngles("Z_Altimeter1", 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F), 0.0F);
		if ((fm.AS.astateCockpitState & 0x40) == 0) {
			mesh.chunkSetAngles("Z_Speedometer1", 0.0F, floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
			mesh.chunkSetAngles("Z_Hour1", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
			mesh.chunkSetAngles("Z_Minute1", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		}
		w.set(fm.getW());
		fm.Or.transform(w);
		mesh.chunkSetAngles("Z_TurnBank1", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 25F, -25F), 0.0F);
		mesh.chunkSetAngles("Z_TurnBank2", 0.0F, cvt(getBall(8D), -8F, 8F, -13F, 13F), 0.0F);
		mesh.chunkSetAngles("Z_TurnBank9", 0.0F, -fm.Or.getKren(), 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[2] = cvt(fm.Or.getTangage(), -45F, 45F, 0.03015F, -0.03015F);
		mesh.chunkSetLocate("Z_TurnBank3", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Z_Climb1", 0.0F, floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
		mesh.chunkSetAngles("Z_Compass1", 0.0F, -interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
		mesh.chunkSetAngles("Z_Compass2", 0.0F, interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
		mesh.chunkSetAngles("Z_RPM1", 0.0F, cvt(fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 504F), 0.0F);
		mesh.chunkSetAngles("Z_Fuel1", 0.0F, cvt(fm.M.fuel, 0.0F, 400F, 0.0F, 267F), 0.0F);
		mesh.chunkSetAngles("Z_Temp1", 0.0F, cvt(fm.EI.engines[0].tOilIn, 0.0F, 100F, 0.0F, 180F), 0.0F);
		mesh.chunkSetAngles("Z_Pres1", 0.0F, cvt(fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 1.71F, 0.0F, 343.5F), 0.0F);
		mesh.chunkSetAngles("Z_OilPres", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness(), 0.0F, 7.45F, 0.0F, 180F), 0.0F);
		mesh.chunkSetAngles("Z_FuelPres", 0.0F, cvt(fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 4F, 0.0F, -180F), 0.0F);
		mesh.chunkSetAngles("Z_Carbair1", 0.0F, cvt(Atmosphere.temperature((float) fm.Loc.z) - 273.15F, -50F, 50F, -50F, 50F), 0.0F);
		float f1 = fm.EI.engines[0].getRPM();
		f1 = 2.5F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
		mesh.chunkSetAngles("Z_Suction1", 0.0F, cvt(f1, 0.0F, 10F, 0.0F, 306F), 0.0F);
		mesh.chunkSetAngles("Z_Coolant1", 0.0F, cvt(fm.EI.engines[0].tWaterOut, 40F, 160F, -65F, 65F), 0.0F);
		mesh.chunkSetAngles("Z_Trim1", 0.0F, 0.0F, 0.0F);
		if (fm.CT.getTrimAileronControl() > fm.CT.trimAileron + 0.01F)
			mesh.chunkSetAngles("Z_Trim1", 0.0F, 33F, 0.0F);
		else if (fm.CT.getTrimAileronControl() < fm.CT.trimAileron - 0.01F)
			mesh.chunkSetAngles("Z_Trim1", 0.0F, -33F, 0.0F);
		mesh.chunkSetAngles("Z_Trim2", 0.0F, 722F * fm.CT.getTrimRudderControl(), 0.0F);
		mesh.chunkSetAngles("Z_Trim3", 0.0F, -722F * fm.CT.getTrimElevatorControl(), 0.0F);
		mesh.chunkSetAngles("Z_gear_lever", -30F * fm.CT.GearControl, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_flaps_lever", 45F * fm.CT.FlapsControl, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Rad_lever", 0.0F, -75F * fm.EI.engines[0].getControlRadiator(), 0.0F);
		mesh.chunkSetAngles("Z_Rad_rod", 0.0F, 75F * fm.EI.engines[0].getControlRadiator(), 0.0F);
		mesh.chunkSetAngles("Z_Throtle1", 0.0F, 66.81F * interp(setNew.throttle, setOld.throttle, f), 0.0F);
		mesh.chunkSetAngles("Z_Prop1", 0.0F, 66.5F * interp(setNew.prop, setOld.prop, f), 0.0F);
		mesh.chunkSetAngles("Z_Mixture1", 0.0F, 60.8F * fm.EI.engines[0].getControlMix(), 0.0F);
		mesh.chunkSetAngles("Z_RightPedal1", 0.0F, -20F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("Z_RightPedal2", 0.0F, 20F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("Z_RightPedal3", 0.0F, 0.0F, 20F * fm.CT.getRudder());
		mesh.chunkSetAngles("Z_LeftPedal1", 0.0F, 20F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("Z_LeftPedal2", 0.0F, -20F * fm.CT.getRudder(), 0.0F);
		mesh.chunkSetAngles("Z_LeftPedal3", 0.0F, 0.0F, -20F * fm.CT.getRudder());
		mesh.chunkSetAngles("Z_Columnbase", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 20F, 0.0F);
		mesh.chunkSetAngles("Z_Column", 0.0F, -(pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 16F, 0.0F);
		mesh.chunkSetAngles("Z_ColumnCable1", 0.0F, pictElev * 16F, 0.0F);
		if (fm.Gears.lgear)
			mesh.chunkSetAngles("Z_GearInd1", 0.0F, cvt(fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 72F), 0.0F);
		if (fm.Gears.rgear)
			mesh.chunkSetAngles("Z_GearInd2", 0.0F, cvt(fm.CT.getGear(), 0.0F, 1.0F, 0.0F, -72F), 0.0F);
		mesh.chunkSetAngles("Z_GearInd3", 0.0F, cvt(fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 88F), 0.0F);
		mesh.chunkSetAngles("Z_FlapInd1", 0.0F, cvt(fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, -80F), 0.0F);
		mesh.chunkSetAngles("Z_Ignition", 0.0F, cvt(fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F);
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 2) != 0)
			mesh.chunkVisible("XGlassDamage4", true);
		if ((fm.AS.astateCockpitState & 1) != 0)
			mesh.chunkVisible("XGlassDamage1", true);
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("Panel_D0", false);
			mesh.chunkVisible("Panel_D1", true);
			mesh.chunkVisible("Z_TurnBank1", false);
			mesh.chunkVisible("Z_TurnBank2", false);
			mesh.chunkVisible("Z_Climb1", false);
			mesh.chunkVisible("Z_Fuel1", false);
			mesh.chunkVisible("Z_Altimeter1", false);
			mesh.chunkVisible("Z_Altimeter2", false);
			mesh.chunkVisible("Z_Climb1", false);
			mesh.chunkVisible("Z_Pres1", false);
			mesh.chunkVisible("Z_Suction1", false);
			mesh.chunkVisible("Z_RPM1", false);
			mesh.chunkVisible("Z_Temp1", false);
			mesh.chunkVisible("Z_OilPres", false);
			mesh.chunkVisible("Z_FuelPres", false);
			mesh.chunkVisible("Z_Coolant1", false);
			mesh.chunkVisible("XHullDamage2", true);
		}
		if ((fm.AS.astateCockpitState & 4) != 0) {
			mesh.chunkVisible("XGlassDamage3", true);
			mesh.chunkVisible("XHullDamage4", true);
		}
		if ((fm.AS.astateCockpitState & 8) != 0) {
			mesh.chunkVisible("XGlassDamage5", true);
			mesh.chunkVisible("XHullDamage1", true);
		}
		if ((fm.AS.astateCockpitState & 0x80) != 0)
			mesh.chunkVisible("Z_OilSplats_D1", true);
		if ((fm.AS.astateCockpitState & 0x10) != 0) {
			mesh.chunkVisible("XGlassDamage2", true);
			mesh.chunkVisible("XHullDamage3", true);
		}
		if ((fm.AS.astateCockpitState & 0x20) != 0)
			mesh.chunkVisible("XGlassDamage5", true);
		retoggleLight();
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

	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private float pictAiler;
	private float pictElev;
	private boolean bNeedSetUp;
	private static final float speedometerScale[] = { 0.0F, 17F, 56.5F, 107.5F, 157F, 204F, 220.5F, 238.5F, 256.5F, 274.5F, 293F, 311F, 330F, 342F };
	private static final float variometerScale[] = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };
	private Point3d tmpP;
	private Vector3d tmpV;

	static {
		Property.set(CLASS.THIS(), "normZN", 1.06F);
	}

}
