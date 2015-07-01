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

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;

public class CockpitSwordfish extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (fm != null) {
				if (bNeedSetUp) {
					reflectPlaneMats();
					bNeedSetUp = false;
				}
				if (((Swordfish1) aircraft()).bChangedPit) {
					reflectPlaneToModel();
					((Swordfish1) aircraft()).bChangedPit = false;
				}
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				setNew.throttle = 0.92F * setOld.throttle + 0.08F * fm.CT.PowerControl;
				setNew.mix = 0.92F * setOld.mix + 0.08F * fm.EI.engines[0].getControlMix();
				setNew.altimeter = fm.getAltitude();
				setNew.azimuth = 0.97F * setOld.azimuth + 0.03F * -fm.Or.getYaw();
				if (setOld.azimuth > 270F && setNew.azimuth < 90F)
					setOld.azimuth -= 360F;
				if (setOld.azimuth < 90F && setNew.azimuth > 270F)
					setOld.azimuth += 360F;
				setNew.waypointAzimuth = 0.91F * setOld.waypointAzimuth + 0.09F * (waypointAzimuth() - setOld.azimuth) + World.Rnd().nextFloat(-10F, 10F);
				setNew.vspeed = 0.99F * setOld.vspeed + 0.01F * fm.getVertSpeed();
				mesh.chunkSetAngles("Turret1AP", 0.0F, ((SndAircraft) (aircraft())).FM.turret[0].tu[0], 0.0F);
				mesh.chunkSetAngles("Turret1BP", 0.0F, ((SndAircraft) (aircraft())).FM.turret[0].tu[1], 0.0F);
			}
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float throttle;
		float mix;
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

	protected void setCameraOffset() {
		cameraCenter.add(0.045000001788139343D, 0.0D, 0.0D);
	}

	public CockpitSwordfish() {
		super("3DO/Cockpit/Swordfish/hier.him", "bf109");
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		cockpitAirBrakePos = 0.0F;
		pictAiler = 0.0F;
		pictElev = 0.0F;
		pictManf = 1.0F;
		bNeedSetUp = true;
		tmpP = new Point3d();
		tmpV = new Vector3d();
		cockpitNightMats = (new String[] { "TEMPPIT5-op", "TEMPPIT6-op", "TEMPPIT14-op", "TEMPPIT18-op", "TEMPPIT22-op", "TEMPPIT28-op", "TEMPPIT38-op", "TEMPPIT1-tr", "TEMPPIT2-tr", "TEMPPIT3-tr", "TEMPPIT4-tr", "TEMPPIT5-tr", "TEMPPIT6-tr",
				"TEMPPIT14-tr", "TEMPPIT18-tr", "TEMPPIT22-tr", "TEMPPIT28-tr", "TEMPPIT38-tr", "TEMPPIT1_damage", "TEMPPIT3_damage" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
		if (((Swordfish1) aircraft()).bChangedPit) {
			reflectPlaneToModel();
			((Swordfish1) aircraft()).bChangedPit = false;
		}
		float f1 = fm.CT.getWing();
		mesh.chunkSetAngles("WingLIn_D0", 0.0F, 85F * f1, 0.0F);
		mesh.chunkSetAngles("WingRIn_D0", 0.0F, -85F * f1, 0.0F);
		mesh.chunkSetAngles("Z_Columnbase", 16F * (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Column", 45F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Elev", -16F * pictElev, 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[2] = cvt(pictAiler, -1F, 1.0F, -0.027F, 0.027F);
		mesh.chunkSetLocate("Z_Shlang01", Cockpit.xyz, Cockpit.ypr);
		Cockpit.xyz[2] = -Cockpit.xyz[2];
		mesh.chunkSetLocate("Z_Shlang02", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Z_Throtle1", 65.45F * interp(setNew.throttle, setOld.throttle, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_BasePedal", 20F * fm.CT.getRudder(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 0.0F, -20F * fm.CT.getRudder());
		mesh.chunkSetAngles("Z_RightPedal", 0.0F, 0.0F, -20F * fm.CT.getRudder());
		mesh.chunkSetAngles("Z_Trim1", 1000F * fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
		mesh.chunkSetAngles("COMPASS_M", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("STRELK_V_LONG", -floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeed()), 0.0F, 257.2222F, 0.0F, 10F), speedometerScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("STREL_ALT_LONG", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F), 0.0F, 0.0F);
		mesh.chunkSetAngles("STREL_ALT_SHORT", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F), 0.0F, 0.0F);
		mesh.chunkSetAngles("SHKALA_DIRECTOR", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("STRELKA_VY", -floatindex(cvt(setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), variometerScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("STRELKA_BOOST", cvt(pictManf = 0.91F * pictManf + 0.09F * fm.EI.engines[0].getManifoldPressure(), 0.7242097F, 2.103161F, 60F, -240F), 0.0F, 0.0F);
		mesh.chunkSetAngles("STRELKA_FUEL1", cvt(fm.M.fuel, 88.38F, 350.23F, 0.0F, -306F), 0.0F, 0.0F);
		mesh.chunkSetAngles("STRELKA_RPM", -floatindex(cvt(fm.EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 10F), rpmScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("STRELK_TEMP_OIL", cvt(fm.EI.engines[0].tOilIn, 0.0F, 160F, 0.0F, -306F), 0.0F, 0.0F);
		mesh.chunkSetAngles("STR_OIL_LB", cvt(1.0F + 0.05F * fm.EI.engines[0].tOilIn, 0.0F, 10F, 0.0F, -36F), 0.0F, 0.0F);
		mesh.chunkSetAngles("STRELK_TURN_UP", -cvt(getBall(8D), -8F, 8F, 35F, -35F), 0.0F, 0.0F);
		w.set(fm.getW());
		fm.Or.transform(w);
		mesh.chunkSetAngles("STREL_TURN_DOWN", -cvt(w.z, -0.23562F, 0.23562F, -48F, 48F), 0.0F, 0.0F);
		mesh.chunkSetAngles("STRELKA_GOR", fm.Or.getKren(), 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(fm.Or.getTangage(), -45F, 45F, 0.022F, -0.022F);
		mesh.chunkSetLocate("STRELKA_GOS", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("STRELKA_HOUR", -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		mesh.chunkSetAngles("STRELKA_MINUTE", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		mesh.chunkSetAngles("STRELKA_SECUND", -cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		cockpitAirBrakePos = 0.9F * cockpitAirBrakePos + 0.1F * ((Swordfish) fm.actor).airBrakePos;
		elevTurretA = 70F * (1.0F - cockpitAirBrakePos);
		mesh.chunkSetAngles("TurrBaseA", 0.0F, elevTurretA, 0.0F);
		mesh.chunkSetAngles("TurrBase1B", elevTurretA, 0.0F, 0.0F);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 0x80) != 0)
			mesh.chunkVisible("Z_OilSplats_D1", true);
		if ((fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("Panel_D0", false);
			mesh.chunkVisible("Panel_D1", true);
			mesh.chunkVisible("STRELK_V_LONG", false);
			mesh.chunkVisible("STREL_ALT_LONG", false);
			mesh.chunkVisible("STREL_ALT_SHORT", false);
			mesh.chunkVisible("STRELKA_VY", false);
			mesh.chunkVisible("STRELKA_RPM", false);
			mesh.chunkVisible("STRELK_TEMP_RAD", false);
			mesh.chunkVisible("STRELK_TEMP_OIL", false);
		}
		retoggleLight();
	}

	protected void reflectPlaneToModel() {
		HierMesh hiermesh = aircraft().hierMesh();
		mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
		mesh.chunkVisible("WingLMid_D1", hiermesh.isChunkVisible("WingLMid_D1"));
		mesh.chunkVisible("WingLMid_D2", hiermesh.isChunkVisible("WingLMid_D2"));
		mesh.chunkVisible("WingLMid_D3", hiermesh.isChunkVisible("WingLMid_D3"));
		mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
		mesh.chunkVisible("WingRMid_D1", hiermesh.isChunkVisible("WingRMid_D1"));
		mesh.chunkVisible("WingRMid_D2", hiermesh.isChunkVisible("WingRMid_D2"));
		mesh.chunkVisible("WingRMid_D3", hiermesh.isChunkVisible("WingRMid_D3"));
		mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
		mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
		mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
		mesh.chunkVisible("WingLIn_D3", hiermesh.isChunkVisible("WingLIn_D3"));
		mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
		mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
		mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
		mesh.chunkVisible("WingRIn_D3", hiermesh.isChunkVisible("WingRIn_D3"));
		mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
		mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
		mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
		mesh.chunkVisible("CF_D3", hiermesh.isChunkVisible("CF_D3"));
		mesh.chunkVisible("Pilot3_D0", hiermesh.isChunkVisible("Pilot3_D0"));
		mesh.chunkVisible("head3_D0", hiermesh.isChunkVisible("Pilot3_D0"));
		mesh.chunkVisible("Pilot3_D1", hiermesh.isChunkVisible("Pilot3_D1"));
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
		mesh.materialReplace("Gloss1D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
		mesh.materialReplace("Gloss1D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
		mesh.materialReplace("Gloss2D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
		mesh.materialReplace("Gloss2D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
		mesh.materialReplace("Gloss2D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
		mesh.materialReplace("Matt1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
		mesh.materialReplace("Matt1D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt1D2o"));
		mesh.materialReplace("Matt1D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
		mesh.materialReplace("Matt2D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt2D1o"));
		mesh.materialReplace("Matt2D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
		mesh.materialReplace("Matt2D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gear"));
		mesh.materialReplace("Gear", mat);
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
	private float cockpitAirBrakePos;
	private float elevTurretA;
	private float pictAiler;
	private float pictElev;
	private float pictManf;
	private boolean bNeedSetUp;
	private static final float speedometerScale[] = { 0.0F, 15.5F, 76F, 153.5F, 234F, 304F, 372.5F, 440F, 504F, 566F, 630F };
	private static final float rpmScale[] = { 0.0F, 15F, 32F, 69.5F, 106.5F, 143F, 180F, 217.5F, 253F, 290F, 327.5F };
	private static final float variometerScale[] = { -158F, -111F, -65.5F, -32.5F, 0.0F, 32.5F, 65.5F, 111F, 158F };
	private Point3d tmpP;
	private Vector3d tmpV;

	static {
		Property.set(CLASS.THIS(), "normZN", 1.0F);
		Property.set(CLASS.THIS(), "gsZN", 0.95F);
	}

}
