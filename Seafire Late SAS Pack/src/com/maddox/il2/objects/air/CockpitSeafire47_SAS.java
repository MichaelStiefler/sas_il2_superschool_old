package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitSeafire47_SAS extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (fm != null) {
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				setNew.throttle = 0.92F * setOld.throttle + 0.08F * fm.CT.PowerControl;
				setNew.prop = 0.92F * setOld.prop + 0.08F * fm.EI.engines[0].getControlProp();
				setNew.mix = 0.92F * setOld.mix + 0.08F * fm.EI.engines[0].getControlMix();
				setNew.altimeter = fm.getAltitude();
				if (Math.abs(fm.Or.getKren()) < 30F)
					setNew.azimuth = 0.97F * setOld.azimuth + 0.03F * -fm.Or.getYaw();
				if (setOld.azimuth > 270F && setNew.azimuth < 90F)
					setOld.azimuth -= 360F;
				if (setOld.azimuth < 90F && setNew.azimuth > 270F)
					setOld.azimuth += 360F;
				setNew.waypointAzimuth = 0.91F * setOld.waypointAzimuth + 0.09F * (waypointAzimuth() - setOld.azimuth) + World.Rnd().nextFloat(-10F, 10F);
				setNew.vspeed = 0.99F * setOld.vspeed + 0.01F * fm.getVertSpeed();
				float f = ((SEAFIRE47_SAS) aircraft()).k14Distance;
				setNew.k14w = (5F * CockpitSeafire47_SAS.k14TargetWingspanScale[((SEAFIRE47_SAS) aircraft()).k14WingspanType]) / f;
				setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
				setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitSeafire47_SAS.k14TargetMarkScale[((SEAFIRE47_SAS) aircraft()).k14WingspanType];
				setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float) ((SEAFIRE47_SAS) aircraft()).k14Mode;
				Vector3d vector3d = aircraft().FM.getW();
				double d = 0.00125D * (double) f;
				float f1 = (float) Math.toDegrees(d * vector3d.z);
				float f2 = -(float) Math.toDegrees(d * vector3d.y);
				float f3 = floatindex((f - 200F) * 0.04F, CockpitSeafire47_SAS.k14BulletDrop) - CockpitSeafire47_SAS.k14BulletDrop[0];
				f2 += (float) Math.toDegrees(Math.atan(f3 / f));
				setNew.k14x = 0.92F * setOld.k14x + 0.08F * f1;
				setNew.k14y = 0.92F * setOld.k14y + 0.08F * f2;
				if (setNew.k14x > 7F)
					setNew.k14x = 7F;
				if (setNew.k14x < -7F)
					setNew.k14x = -7F;
				if (setNew.k14y > 7F)
					setNew.k14y = 7F;
				if (setNew.k14y < -7F)
					setNew.k14y = -7F;
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
		float altimeter;
		float azimuth;
		float vspeed;
		float waypointAzimuth;
		float k14wingspan;
		float k14mode;
		float k14x;
		float k14y;
		float k14w;

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
		cameraCenter.add(0.029999999329447746D, 0.0D, 0.0D);
	}

	public CockpitSeafire47_SAS() {
		super("3DO/Cockpit/SeafireMkXVII_SAS/hier.him", "bf109");
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		pictBrake = 0.0F;
		pictFlap = 0.0F;
		pictGear = 0.0F;
		pictManf = 1.0F;
		bNeedSetUp = true;
		tmpP = new Point3d();
		tmpV = new Vector3d();
		if (aircraft() instanceof SEAFIRE47_SAS) {
			mesh.materialReplace("BORT2", "BORT2b");
			mesh.materialReplace("text14", "text14b");
		}
		cockpitNightMats = (new String[] { "COMPASS", "BORT2", "prib_five", "prib_five_damage", "prib_one", "prib_one_damage", "prib_three", "prib_three_damage", "prib_two", "prib_two_damage",
				"text13", "text15" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		if (acoustics != null)
			acoustics.globFX = new ReverbFXRoom(0.2F);
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("Blister2_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("Blister2_D0", true);
		super.doFocusLeave();
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			reflectPlaneToModel();
			bNeedSetUp = false;
		}
		int i = ((SEAFIRE47_SAS) aircraft()).k14Mode;
		boolean flag = i < 2;
		mesh.chunkVisible("Z_Z_RETICLE", flag);
		flag = i > 0;
		mesh.chunkVisible("Z_Z_RETICLE1", flag);
		mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, setNew.k14x, setNew.k14y);
		resetYPRmodifier();
		Cockpit.xyz[0] = setNew.k14w;
		for (int j = 1; j < 7; j++) {
			mesh.chunkVisible("Z_Z_AIMMARK" + j, flag);
			mesh.chunkSetLocate("Z_Z_AIMMARK" + j, Cockpit.xyz, Cockpit.ypr);
		}
		if (aircraft() instanceof SEAFIRE47_SAS) {
			if (SEAFIRE47_SAS.bChangedPit) {
				reflectPlaneToModel();
				SEAFIRE47_SAS.bChangedPit = false;
			}
		}
		float f1 = fm.CT.getWing();
		mesh.chunkSetAngles("WingLMid_D0", 0.0F, -112F * f1, 0.0F);
		mesh.chunkSetAngles("WingLOut_D0", 0.0F, -112F * f1, 0.0F);
		mesh.chunkSetAngles("WingRMid_D0", 0.0F, -112F * f1, 0.0F);
		mesh.chunkSetAngles("WingROut_D0", 0.0F, -112F * f1, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(fm.CT.arrestorControl, 0.01F, 0.99F, 0.0F, 0.03F);
		mesh.chunkSetLocate("Arrester1", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkVisible("XLampArrest", fm.CT.getArrestor() > 0.9F);
		f1 = fm.CT.getAileron();
		mesh.chunkSetAngles("AroneL_D0", 0.0F, -30F * f1, 0.0F);
		mesh.chunkSetAngles("AroneR_D0", 0.0F, -30F * f1, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.55F);
		mesh.chunkSetLocate("Blister_D0", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkVisible("XLampGearUpL", fm.CT.getGear() == 0.0F);
		mesh.chunkVisible("XLampGearDownL", fm.CT.getGear() == 1.0F);
		mesh.chunkVisible("XLampFuel", fm.M.fuel < 0.25F * fm.M.maxFuel);
		mesh.chunkSetAngles("Z_Columnbase", 0.0F, 8F * (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl), 0.0F);
		mesh.chunkSetAngles("Z_Column", -30F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Shlang01a", -5F * pictAiler, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Shlang01b", -9F * pictAiler, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Shlang01c", -12F * pictAiler, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Shlang02a", -5F * pictAiler, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Shlang02b", -7.5F * pictAiler, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Shlang02c", -15F * pictAiler, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Shlang03a", -5F * pictAiler, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Shlang03b", -8.5F * pictAiler, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Shlang03c", -18.5F * pictAiler, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Stick_Shtok01", 0.0F, 0.0F, 8F * pictElev);
		mesh.chunkSetAngles("Z_ColumnSwitch", -18F * (pictBrake = 0.89F * pictBrake + 0.11F * fm.CT.BrakeControl), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Throtle1", -64.5454F * interp(setNew.throttle, setOld.throttle, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_BasePedal", 20F * fm.CT.getRudder(), 0.0F, 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[2] = 0.0578F * fm.CT.getRudder();
		mesh.chunkSetLocate("Z_LeftPedal", Cockpit.xyz, Cockpit.ypr);
		Cockpit.xyz[2] = -0.0578F * fm.CT.getRudder();
		mesh.chunkSetLocate("Z_RightPedal", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("Z_Gear1", -160F + 160F * (pictGear = 0.89F * pictGear + 0.11F * fm.CT.GearControl), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Flaps1", 160F * (pictFlap = 0.89F * pictFlap + 0.11F * fm.CT.FlapsControl), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Trim1", 1000F * fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Trim2", 1000F * fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Prop1", -90F * setNew.prop, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Mixture1", -60F * setNew.mix, 0.0F, 0.0F);
		mesh.chunkSetAngles("COMPASS_M", -interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("SHKALA_DIRECTOR", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
		mesh.chunkSetAngles("STREL_ALT_LONG", 0.0F, 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F));
		mesh.chunkSetAngles("STREL_ALT_SHORT", 0.0F, 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F));
		mesh.chunkSetAngles("STREL_ALT_SHRT1", 0.0F, 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -108F));
		mesh.chunkSetAngles("STRELKA_BOOST", 0.0F, 0.0F, -cvt(pictManf = 0.91F * pictManf + 0.09F * fm.EI.engines[0].getManifoldPressure(), 0.5173668F, 2.72369F, -70F, 250F));
		mesh.chunkSetAngles("STRELKA_FUEL", 0.0F, 0.0F, cvt(fm.M.fuel, 0.0F, 378.54F, 0.0F, 68F));
		mesh.chunkSetAngles("STRELKA_RPM", 0.0F, 0.0F, -floatindex(cvt(fm.EI.engines[0].getRPM(), 1000F, 5000F, 2.0F, 10F), rpmScale));
		mesh.chunkSetAngles("STRELK_TEMP_OIL", 0.0F, 0.0F, -cvt(fm.EI.engines[0].tOilOut, 50F, 100F, 0.0F, 271F));
		mesh.chunkSetAngles("STRELK_TEMP_RAD", 0.0F, 0.0F, -floatindex(cvt(fm.EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 14F), radScale));
		mesh.chunkSetAngles("STR_OIL_LB", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, -37F), 0.0F);
		w.set(fm.getW());
		fm.Or.transform(w);
		mesh.chunkSetAngles("STREL_TURN_DOWN", 0.0F, 0.0F, -cvt(w.z, -0.23562F, 0.23562F, -48F, 48F));
		mesh.chunkSetAngles("STRELK_TURN_UP", 0.0F, 0.0F, -cvt(getBall(8D), -8F, 8F, 35F, -35F));
		mesh.chunkVisible("STRELK_V_SHORT", false);
		mesh.chunkSetAngles("STRELK_V_LONG", 0.0F, 0.0F, -floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeed()), 0.0F, 257.2222F, 0.0F, 25F), speedometerScale));
		mesh.chunkSetAngles("STRELKA_VY", 0.0F, 0.0F, -floatindex(cvt(setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), variometerScale));
		mesh.chunkSetAngles("STRELKA_GOR", 0.0F, 0.0F, fm.Or.getKren());
		resetYPRmodifier();
		Cockpit.xyz[2] = cvt(fm.Or.getTangage(), -45F, 45F, 0.032F, -0.032F);
		mesh.chunkSetLocate("STRELKA_GOS", Cockpit.xyz, Cockpit.ypr);
		mesh.chunkSetAngles("STRELKA_HOUR", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		mesh.chunkSetAngles("STRELKA_MINUTE", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("STRELKA_SECUND", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("STR_CLIMB", 0.0F, 0.0F, cvt(fm.CT.trimElevator, -0.5F, 0.5F, -35.23F, 35.23F));
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 0x80) != 0)
			mesh.chunkVisible("Z_OilSplats_D1", true);
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
		mat = hiermesh.material(hiermesh.materialFind("Matt1D2o"));
		mesh.materialReplace("Matt1D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
		mesh.materialReplace("Matt2D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
		mesh.materialReplace("Matt2D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Overlay6"));
		mesh.materialReplace("Overlay6", mat);
		mat = hiermesh.material(hiermesh.materialFind("Overlay7"));
		mesh.materialReplace("Overlay7", mat);
		mat = hiermesh.material(hiermesh.materialFind("OverlayD1o"));
		mesh.materialReplace("OverlayD1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("OverlayD2o"));
		mesh.materialReplace("OverlayD2o", mat);
	}

	protected void reflectPlaneToModel() {
		HierMesh hiermesh = aircraft().hierMesh();
		mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
		mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
		mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
		mesh.chunkVisible("WingLIn_D3", hiermesh.isChunkVisible("WingLIn_D3"));
		mesh.chunkVisible("WingLIn_CAP", hiermesh.isChunkVisible("WingLIn_CAP"));
		mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
		mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
		mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
		mesh.chunkVisible("WingRIn_D3", hiermesh.isChunkVisible("WingRIn_D3"));
		mesh.chunkVisible("WingRIn_CAP", hiermesh.isChunkVisible("WingRIn_CAP"));
		mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
		mesh.chunkVisible("WingLMid_D1", hiermesh.isChunkVisible("WingLMid_D1"));
		mesh.chunkVisible("WingLMid_D2", hiermesh.isChunkVisible("WingLMid_D2"));
		mesh.chunkVisible("WingLMid_D3", hiermesh.isChunkVisible("WingLMid_D3"));
		mesh.chunkVisible("WingLMid_CAP", hiermesh.isChunkVisible("WingLMid_CAP"));
		mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
		mesh.chunkVisible("WingRMid_D1", hiermesh.isChunkVisible("WingRMid_D1"));
		mesh.chunkVisible("WingRMid_D2", hiermesh.isChunkVisible("WingRMid_D2"));
		mesh.chunkVisible("WingRMid_D3", hiermesh.isChunkVisible("WingRMid_D3"));
		mesh.chunkVisible("WingRMid_CAP", hiermesh.isChunkVisible("WingRMid_CAP"));
		mesh.chunkVisible("WingLOut_D0", hiermesh.isChunkVisible("WingLOut_D0"));
		mesh.chunkVisible("WingLOut_D1", hiermesh.isChunkVisible("WingLOut_D1"));
		mesh.chunkVisible("WingLOut_D2", hiermesh.isChunkVisible("WingLOut_D2"));
		mesh.chunkVisible("WingLOut_D3", hiermesh.isChunkVisible("WingLOut_D3"));
		mesh.chunkVisible("WingROut_D0", hiermesh.isChunkVisible("WingROut_D0"));
		mesh.chunkVisible("WingROut_D1", hiermesh.isChunkVisible("WingROut_D1"));
		mesh.chunkVisible("WingROut_D2", hiermesh.isChunkVisible("WingROut_D2"));
		mesh.chunkVisible("WingROut_D3", hiermesh.isChunkVisible("WingROut_D3"));
		mesh.chunkVisible("AroneL_D0", hiermesh.isChunkVisible("AroneL_D0"));
		mesh.chunkVisible("AroneL_D1", hiermesh.isChunkVisible("AroneL_D1"));
		mesh.chunkVisible("AroneR_D0", hiermesh.isChunkVisible("AroneR_D0"));
		mesh.chunkVisible("AroneR_D1", hiermesh.isChunkVisible("AroneR_D1"));
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl)
			setNightMats(true);
		else
			setNightMats(false);
	}

	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private float pictAiler;
	private float pictElev;
	private float pictBrake;
	private float pictFlap;
	private float pictGear;
	private float pictManf;
	private boolean bNeedSetUp;
	private static final float speedometerScale[] = { 0.0F, 7.5F, 17.5F, 37F, 63F, 88.5F, 114.5F, 143F, 171.5F, 202.5F, 228.5F, 255.5F, 282F, 309F, 336.5F, 366.5F, 394F, 421F, 448.5F, 474.5F, 500.5F,
			530F, 557.5F, 584F, 609F, 629F };
	private static final float radScale[] = { 0.0F, 3F, 7F, 13.5F, 21.5F, 27F, 34.5F, 50.5F, 71F, 94F, 125F, 161F, 202.5F, 253F, 315.5F };
	private static final float rpmScale[] = { 0.0F, 0.0F, 0.0F, 22F, 58F, 103.5F, 152.5F, 193.5F, 245F, 281.5F, 311.5F };
	private static final float variometerScale[] = { -158F, -111F, -65.5F, -32.5F, 0.0F, 32.5F, 65.5F, 111F, 158F };
	private static final float k14TargetMarkScale[] = { -0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F };
	private static final float k14TargetWingspanScale[] = { 9.9F, 10.52F, 13.8F, 16.34F, 19F, 20F, 22F, 29.25F, 30F, 32.85F };
	private static final float k14BulletDrop[] = { 5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F,
			10.381F, 10.513F, 10.603F, 10.704F, 10.739F, 10.782F, 10.789F };
	private Point3d tmpP;
	private Vector3d tmpV;

}
