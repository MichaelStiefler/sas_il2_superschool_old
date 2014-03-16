package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitP_47DModPack extends CockpitPilot {

	class Variables {
		float throttle;
		float prop;
		float mix;
		float stage;
		float altimeter;
		float azimuth;
		float vspeed;
		float waypointAzimuth;
		float k14wingspan;
		float k14mode;
		float k14x;
		float k14y;
		float k14w;
		float supercharge;

		private Variables() {
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
				setNew.stage = 0.85F * setOld.stage + (float) fm.EI.engines[0].getControlCompressor() * 0.15F;
				setNew.mix = 0.85F * setOld.mix + fm.EI.engines[0].getControlMix() * 0.15F;
				setNew.altimeter = fm.getAltitude();
				if (Math.abs(fm.Or.getKren()) < 45F)
					setNew.azimuth = (35F * setOld.azimuth - fm.Or.getYaw()) / 36F;
				if (setOld.azimuth > 270F && setNew.azimuth < 90F)
					setOld.azimuth -= 360F;
				if (setOld.azimuth < 90F && setNew.azimuth > 270F)
					setOld.azimuth += 360F;
				setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
				setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
				float f;
				if (fm.getAltitude() > 3000F) {
					f = (float) Math.sin(1.0F * cvt(fm.getOverload(), 1.0F, 8F, 1.0F, 0.45F) * cvt(fm.AS.astatePilotStates[0], 0.0F, 100F, 1.0F, 0.1F) * (0.001F * (float) Time.current()));
					if (f > 0.0F) {
						pictBlinker += 0.3F;
						if (pictBlinker > 1.0F)
							pictBlinker = 1.0F;
					} else {
						pictBlinker -= 0.3F;
						if (pictBlinker < 0.0F)
							pictBlinker = 0.0F;
					}
				}
				if (fm.EI.engines[0].getRPM() < 0.01F) {
					pictTurba = 0.0F;
				} else {
					if (fm.getAltitude() < 1500F)
						pictTurba = 0.05F * fm.EI.engines[0].getManifoldPressure() * setNew.supercharge;
					else
						pictTurba = 0.2F * fm.EI.engines[0].getManifoldPressure() + 0.0001F * fm.getAltitude() * setNew.supercharge;
				}
				if (aircraft() instanceof P_47ModPackAceMakerGunsight) {
					P_47ModPackAceMakerGunsight amgAircraft = (P_47ModPackAceMakerGunsight) aircraft();
					f = amgAircraft.k14Distance;
					setNew.k14w = (5F * CockpitP_47DModPack.k14TargetWingspanScale[amgAircraft.k14WingspanType]) / f;
					setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
					setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitP_47DModPack.k14TargetMarkScale[amgAircraft.k14WingspanType];
					setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float) amgAircraft.k14Mode;
					Vector3d vector3d = aircraft().FM.getW();
					double d = 0.00125D * (double) f;
					float f1 = (float) Math.toDegrees(d * vector3d.z);
					float f2 = -(float) Math.toDegrees(d * vector3d.y);
					float f3 = floatindex((f - 200F) * 0.04F, CockpitP_47DModPack.k14BulletDrop) - CockpitP_47DModPack.k14BulletDrop[0];
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
				setNew.supercharge = 0.99F * setOld.supercharge + 0.01F * setNew.throttle;
			}
			return true;
		}

		Interpolater() {
		}
	}

	protected float waypointAzimuth() {
		WayPoint waypoint = this.fm.AP.way.curr();
		if (waypoint == null) {
			return 0.0F;
		} else {
			waypoint.getP(Cockpit.P1);
			Cockpit.V.sub(Cockpit.P1, this.fm.Loc);
			return (float) (57.295779513082323D * Math.atan2(-Cockpit.V.y, Cockpit.V.x));
		}
	}

	public CockpitP_47DModPack(String hierFile) {
		super(hierFile, "bf109");
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		pictBlinker = 0.0F;
		bNeedSetUp = true;
		this.cockpitNightMats = (new String[] { "prib1", "prib2", "prib3", "prib4", "prib5", "prib6", "shkala", "prib1_d1", "prib2_d1", "prib3_d1", "prib4_d1", "prib5_d1", "prib6_d1" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		if (this.acoustics != null)
			this.acoustics.globFX = new ReverbFXRoom(0.45F);
	}

	public void reflectWorldToInstruments(float f) {
		if (aircraft() instanceof P_47ModPackAceMakerGunsight) {
			int i = ((P_47ModPackAceMakerGunsight) aircraft()).k14Mode;
			boolean flag = i < 2;
			this.mesh.chunkVisible("Z_Z_RETICLE", flag);
			flag = i > 0;
			this.mesh.chunkVisible("Z_Z_RETICLE1", flag);
			this.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, setNew.k14x, setNew.k14y);
			resetYPRmodifier();
			Cockpit.xyz[0] = setNew.k14w;
			for (int j = 1; j < 7; j++) {
				this.mesh.chunkVisible("Z_Z_AIMMARK" + j, flag);
				this.mesh.chunkSetLocate("Z_Z_AIMMARK" + j, Cockpit.xyz, Cockpit.ypr);
			}
			this.mesh.chunkSetAngles("Z_Target1", setNew.k14wingspan, 0.0F, 0.0F);
		}

		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
		resetYPRmodifier();
		Cockpit.xyz[1] = cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.85F);
		this.mesh.chunkSetLocate("canopy", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkSetAngles("armPedalL", 0.0F, -15F * this.fm.CT.getRudder(), 0.0F);
		this.mesh.chunkSetAngles("armPedalR", 0.0F, 15F * this.fm.CT.getRudder(), 0.0F);
		this.mesh.chunkSetAngles("PedalL", 0.0F, 15F * this.fm.CT.getRudder(), 0.0F);
		this.mesh.chunkSetAngles("PedalR", 0.0F, -15F * this.fm.CT.getRudder(), 0.0F);
		this.mesh.chunkSetAngles("Stick", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * this.fm.CT.AileronControl) * 20F, (pictElev = 0.85F * pictElev + 0.15F * this.fm.CT.ElevatorControl) * 16F);
		this.mesh.chunkSetAngles("supercharge", 0.0F, cvt(setNew.supercharge, 0.3F, 0.75F, -2F, 44F), 0.0F);
		this.mesh.chunkSetAngles("throtle", 0.0F, 0.0F, cvt(setNew.throttle, 0.0F, 1.1F, -6F, -68F));
		this.mesh.chunkSetAngles("prop", 0.0F, 70F * setNew.prop, 0.0F);
		this.mesh.chunkSetAngles("mixtura", 0.0F, 55F * setNew.mix, 0.0F);
		this.mesh.chunkSetAngles("flaplever", 0.0F, 0.0F, 70F * this.fm.CT.FlapsControl);
		this.mesh.chunkSetAngles("zfuelR", 0.0F, floatindex(cvt(this.fm.M.fuel, 0.0F, 981F, 0.0F, 6F), fuelGallonsScale), 0.0F);
		this.mesh.chunkSetAngles("zfuelL", 0.0F, -floatindex(cvt(this.fm.M.fuel, 0.0F, 981F, 0.0F, 4F), fuelGallonsAuxScale), 0.0F);
		this.mesh.chunkSetAngles("zacceleration", 0.0F, cvt(this.fm.getOverload(), -4F, 12F, -77F, 244F), 0.0F);
		this.mesh.chunkSetAngles("zSpeed1a", 0.0F, floatindex(cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), speedometerScale), 0.0F);
		this.mesh.chunkSetAngles("zclimb", 0.0F, floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
		w.set(this.fm.getW());
		this.fm.Or.transform(w);
		this.mesh.chunkSetAngles("zTurn1a", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 25F, -25F), 0.0F);
		this.mesh.chunkSetAngles("zSlide1a", 0.0F, cvt(getBall(7D), -7F, 7F, -16F, 16F), 0.0F);
		this.mesh.chunkSetAngles("zManifold1a", 0.0F, cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 210F), 0.0F);
		this.mesh.chunkSetAngles("zAlt1c", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 300F), 0.0F);
		this.mesh.chunkSetAngles("zAlt1a", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
		this.mesh.chunkSetAngles("zAlt1b", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
		this.mesh.chunkSetAngles("zRPM1a", 0.0F, cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 315F), 0.0F);
		this.mesh.chunkSetAngles("zoiltemp1a", 0.0F, cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 300F, 0.0F, 120F), 0.0F);
		this.mesh.chunkSetAngles("ztempoil1a", 0.0F, cvt(this.fm.EI.engines[0].tOilOut, -50F, 150F, 0.0F, 90F), 0.0F);
		this.mesh.chunkSetAngles("ZTemp1a", 0.0F, cvt(Atmosphere.temperature((float) ((Tuple3d) (this.fm.Loc)).z) - 273.15F, -15F, 55F, 0.0F, 30F), 0.0F);
		this.mesh.chunkSetAngles("zClock1b", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		this.mesh.chunkSetAngles("zClock1a", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		this.mesh.chunkSetAngles("zhorizont1a", 0.0F, -this.fm.Or.getKren(), 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[2] = cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0328F, -0.0328F);
		this.mesh.chunkSetLocate("zhorizont1b", Cockpit.xyz, Cockpit.ypr);
		if (this.mesh.chunkFindCheck("zturborpm1a") > 0)
			this.mesh.chunkSetAngles("zturborpm1a", 0.0F, cvt(pictTurba, 0.0F, 2.0F, 0.0F, 207.5F), 0.0F);
		this.mesh.chunkSetAngles("zpressfuel1a", 0.0F, cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.4F, 0.0F, -180F), 0.0F);
		this.mesh.chunkSetAngles("zpressoil1a", 0.0F, cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness(), 0.0F, 7.45F, 0.0F, 60F), 0.0F);
		this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, cvt(this.fm.Or.getTangage(), -5F, 5F, -5F, 5F), 0.0F);
		this.mesh.chunkSetAngles("zAzimuth1b", 0.0F, 90F - setNew.azimuth, 0.0F);
		this.mesh.chunkSetAngles("zMagAzimuth1a", 0.0F, cvt(this.fm.Or.getTangage(), -65F, 65F, -65F, 65F), 0.0F);
		this.mesh.chunkSetAngles("zMagAzimuth1b", -90F + setNew.azimuth, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Zfas1a", 0.0F, cvt(this.fm.Gears.isHydroOperable() ? 0.8F : 0.0F, 0.0F, 1.0F, 0.0F, 110F), 0.0F);
		this.mesh.chunkSetAngles("zpresswater1a", 0.0F, cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 200F, 0.0F, 250F), 0.0F);
		float f1 = this.fm.EI.engines[0].getRPM();
		f1 = 4.2F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
		this.mesh.chunkSetAngles("ZSuction1a", 0.0F, cvt(f1, 0.0F, 10F, 0.0F, 180F), 0.0F);
		this.mesh.chunkSetAngles("ZOxipress1a", 0.0F, 90F, 0.0F);
		Cockpit.xyz[2] = 0.01F * pictBlinker;
		this.mesh.chunkSetLocate("zBlink1", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkSetLocate("zBlink2", Cockpit.xyz, Cockpit.ypr);
		resetYPRmodifier();
		this.mesh.chunkVisible("Z_Red1", this.fm.CT.getGear() < 0.05F || !this.fm.Gears.lgear || !this.fm.Gears.rgear);
		this.mesh.chunkVisible("Z_Green1", this.fm.CT.getGear() > 0.95F);
		this.mesh.chunkVisible("Z_Red2", this.fm.M.fuel / this.fm.M.maxFuel < 0.15F);
		float f2 = (float) Math.sin(0.007F * (float) Time.current());
		this.mesh.chunkVisible("Z_Red3", (f2 * f2 + pictTurba) - 0.7F > 0.5F);
		this.mesh.chunkVisible("Z_Green2", this.fm.AS.bNavLightsOn);
		this.mesh.chunkVisible("Z_Red4", this.fm.AS.bNavLightsOn);
		if (this.mesh.chunkFindCheck("zSlip") > 0)
			this.mesh.chunkSetAngles("zSlip", 0.0F, cvt(getBall(7D), -7F, 7F, -16F, 16F), 0.0F);
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 2) != 0) {
			if (this.mesh.chunkFindCheck("Z_Holes1_D1") > 0)
				this.mesh.chunkVisible("Z_Holes1_D1", true);
			if (this.mesh.chunkFindCheck("Z_Holes1_D2") > 0)
				this.mesh.chunkVisible("Z_Holes1_D2", true);
			this.mesh.chunkVisible("pricel", false);
			if (this.mesh.chunkFindCheck("Z_Z_MASK") > 0)
				this.mesh.chunkVisible("Z_Z_MASK", true);
			if (this.mesh.chunkFindCheck("Z_Z_MASK2") > 0)
				this.mesh.chunkVisible("Z_Z_MASK2", true);
			if (this.mesh.chunkFindCheck("pricel_d1") > 0)
				this.mesh.chunkVisible("pricel_d1", true);
			if (this.mesh.chunkFindCheck("ZSlip") > 0)
				this.mesh.chunkVisible("ZSlip", false);
		}
		if ((this.fm.AS.astateCockpitState & 1) != 0) {
			if (this.mesh.chunkFindCheck("Z_Holes1_D1") > 0)
				this.mesh.chunkVisible("Z_Holes1_D1", true);
			if (this.mesh.chunkFindCheck("Z_Holes2_D1") > 0)
				this.mesh.chunkVisible("Z_Holes2_D1", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
			this.mesh.chunkVisible("pribors1", false);
			this.mesh.chunkVisible("pribors1_d1", true);
			this.mesh.chunkVisible("zamper", false);
			this.mesh.chunkVisible("zAzimuth1a", false);
			this.mesh.chunkVisible("zAzimuth1b", false);
			this.mesh.chunkVisible("zSpeed1a", false);
			this.mesh.chunkVisible("zacceleration", false);
			this.mesh.chunkVisible("zMagAzimuth1a", false);
			this.mesh.chunkVisible("zMagAzimuth1b", false);
			this.mesh.chunkVisible("zpresswater1a", false);
			this.mesh.chunkVisible("zclimb", false);
			this.mesh.chunkVisible("zRPM1a", false);
			this.mesh.chunkVisible("zoiltemp1a", false);
			this.mesh.chunkVisible("zfas1a", false);
			this.mesh.chunkVisible("zoxipress1a", false);
			if (this.mesh.chunkFindCheck("zturborpm1a") > 0)
				this.mesh.chunkVisible("zturborpm1a", false);
		}
		if (((this.fm.AS.astateCockpitState & 0x4) == 0) || ((this.fm.AS.astateCockpitState & 0x8) != 0)) {
			if (this.mesh.chunkFindCheck("Z_Holes2_D1") > 0)
				this.mesh.chunkVisible("Z_Holes2_D1", true);
			if (this.mesh.chunkFindCheck("Z_Holes2_D2") > 0)
				this.mesh.chunkVisible("Z_Holes2_D2", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
			this.mesh.chunkVisible("Z_OilSplats_D1", true);
		}
		if (((this.fm.AS.astateCockpitState & 0x10) == 0) || ((this.fm.AS.astateCockpitState & 0x20) != 0)) {
			this.mesh.chunkVisible("pribors2", false);
			this.mesh.chunkVisible("pribors2_d1", true);
			this.mesh.chunkVisible("zClock1b", false);
			this.mesh.chunkVisible("zClock1a", false);
			this.mesh.chunkVisible("zfuelR", false);
			this.mesh.chunkVisible("zfuelL", false);
			this.mesh.chunkVisible("zsuction1a", false);
			this.mesh.chunkVisible("zTurn1a", false);
			this.mesh.chunkVisible("zSlide1a", false);
			this.mesh.chunkVisible("zhorizont1a", false);
			this.mesh.chunkVisible("zAlt1a", false);
			this.mesh.chunkVisible("zAlt1b", false);
			this.mesh.chunkVisible("zpressfuel1a", false);
			this.mesh.chunkVisible("zpressoil1a", false);
			this.mesh.chunkVisible("ztempoil1a", false);
			if (this.mesh.chunkFindCheck("zturborpm1a") > 0)
				this.mesh.chunkVisible("zturbormp1a", false);
			this.mesh.chunkVisible("zManifold1a", false);
			this.mesh.chunkVisible("zBlink1", false);
			this.mesh.chunkVisible("zBlink2", false);
		}
		retoggleLight();
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		this.mesh.materialReplace("Gloss1D0o", mat);
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl)
			setNightMats(true);
		else
			setNightMats(false);
	}

	private void retoggleLight() {
		if (this.cockpitLightControl) {
			setNightMats(false);
			setNightMats(true);
		} else {
			setNightMats(true);
			setNightMats(false);
		}
	}

	private Variables setOld;
	protected Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private float pictAiler;
	private float pictElev;
	private float pictTurba;
	private float pictBlinker;
	private boolean bNeedSetUp;
	private static final float fuelGallonsScale[] = { 0.0F, 8.25F, 17.5F, 36.5F, 54F, 90F, 108F };
	private static final float fuelGallonsAuxScale[] = { 0.0F, 38F, 62.5F, 87F, 104F };
	private static final float speedometerScale[] = { 0.0F, 5F, 47.5F, 92F, 134F, 180F, 227F, 241F, 255F, 262.5F, 270F, 283F, 296F, 312F, 328F };
	private static final float variometerScale[] = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };
	private static final float k14TargetMarkScale[] = { -0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F };
	private static final float k14TargetWingspanScale[] = { 9.9F, 10.52F, 13.8F, 16.34F, 19F, 20F, 22F, 29.25F, 30F, 32.85F };
	private static final float k14BulletDrop[] = { 5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 10.603F, 10.704F, 10.739F, 10.782F,
			10.789F };
}
