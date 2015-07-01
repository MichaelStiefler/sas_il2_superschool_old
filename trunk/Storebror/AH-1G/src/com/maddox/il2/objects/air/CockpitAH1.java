package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitAH1 extends CockpitPilot {
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
				setNew.azimuth = (35F * setOld.azimuth + -fm.Or.getYaw()) / 36F;
				if (setOld.azimuth > 270F && setNew.azimuth < 90F) setOld.azimuth -= 360F;
				if (setOld.azimuth < 90F && setNew.azimuth > 270F) setOld.azimuth += 360F;
				setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + waypointAzimuth() + World.Rnd().nextFloat(-30F, 30F)) / 11F;
				setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
				Variables variables = setNew;
				float f = 0.5F * setOld.radioalt;
				float f1 = 0.5F;
				float f2 = fm.getAltitude();
				World.cur();
				World.land();
				variables.radioalt = f + f1 * (f2 - Landscape.HQ_Air((float) fm.Loc.x, (float) fm.Loc.y));
				if (cockpitDimControl) {
					if (setNew.dimPosition > 0.0F) setNew.dimPosition = setOld.dimPosition - 0.05F;
				} else if (setNew.dimPosition < 1.0F) setNew.dimPosition = setOld.dimPosition + 0.05F;
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
		float stage;
		float altimeter;
		float azimuth;
		float vspeed;
		float waypointAzimuth;
		float radioalt;
		float dimPosition;

		private Variables() {
		}

		Variables(Variables variables) {
			this();
		}
	}

	protected float waypointAzimuth() {
		WayPoint waypoint = fm.AP.way.curr();
		if (waypoint == null) {
			return 0.0F;
		} else {
			waypoint.getP(tmpP);
			tmpV.sub(tmpP, fm.Loc);
			return (float) (57.295779513082323D * Math.atan2(-((Tuple3d) (tmpV)).y, ((Tuple3d) (tmpV)).x));
		}
	}

	public CockpitAH1() {
		super("3DO/Cockpit/AH1/hier.him", "bf109");
		setOld = new Variables(null);
		setNew = new Variables(null);
		w = new Vector3f();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		rotorrpm = 0;
		bNeedSetUp = true;
		tmpP = new Point3d();
		tmpV = new Vector3d();
		this.cockpitNightMats = (new String[] { "Instrumentos001", "Instrumentos002", "Instrumentos003" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		setNew.dimPosition = 1.0F;
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
		this.mesh.chunkSetAngles("Z_Colectivo", 0.0F, 0.0F, -30F * setNew.throttle);
		this.mesh.chunkSetAngles("Z_Gases", 0.0F, 200F * setNew.throttle, 0.0F);
		this.mesh.chunkSetAngles("Z_Gases2", 0.0F, 200F * setNew.throttle, 0.0F);
		this.mesh.chunkSetAngles("Z_Palanca", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * -10F, (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * -10F);
		this.mesh.chunkSetAngles("Z_Palanca2", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * -10F, (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * -10F);
		this.mesh.chunkSetAngles("Z_Pedal_D", 0.0F, 0.0F, -15F * fm.CT.getRudder());
		this.mesh.chunkSetAngles("Z_Pedal_I", 0.0F, 0.0F, 15F * fm.CT.getRudder());
		this.mesh.chunkSetAngles("Z_Radiador", 0.0F, 0.0F, -60F * fm.EI.engines[0].getControlRadiator());
		if ((double) fm.CT.PowerControl <= 0.11D) this.mesh.chunkSetAngles("Z_clutch", 0.0F, 0.0F, 700F * fm.CT.PowerControl);
		this.mesh.chunkSetAngles("Z_Magneto", 0.0F, 20F * (float) fm.EI.engines[0].getControlMagnetos(), 0.0F);
		this.mesh.chunkVisible("A_Fuel1", fm.M.fuel < 52F);
		this.mesh.chunkVisible("A_Fuel2", fm.M.fuel < 26F);
		resetYPRmodifier();
		if (fm.EI.engines[0].getStage() > 0 && fm.EI.engines[0].getStage() < 3) Cockpit.xyz[1] = 0.01F;
		this.mesh.chunkSetLocate("Z_Starter", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkVisible("Z_Starter2", fm.EI.engines[0].getStage() > 0 && fm.EI.engines[0].getStage() < 3);
		this.mesh.chunkVisible("A_EngFire", fm.AS.astateEngineStates[0] > 2);
		if (fm.CT.bHasArrestorControl) {
			this.mesh.chunkVisible("Z_Flotadores_A", true);
			this.mesh.chunkVisible("Z_Flotadores_D", false);
			this.mesh.chunkVisible("A_Foats_A", true);
		}
		this.mesh.chunkVisible("A_Foats_R", fm.CT.getArrestor() > 0.05F);
		this.mesh.chunkSetAngles("Mira", 0.0F, cvt(interp(setNew.dimPosition, setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -90F), 0.0F);
		this.mesh.chunkVisible("Reticulo", setNew.dimPosition < 0.5F);
		this.mesh.chunkSetAngles("Z_Altimetro2", 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F);
		this.mesh.chunkSetAngles("Z_Altimetro1", 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F);
		this.mesh.chunkSetAngles("Z_Altimetro4", 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F);
		this.mesh.chunkSetAngles("Z_Altimetro3", 0.0F, -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F);
		this.mesh.chunkSetAngles("Z_ASI1", 0.0F, -floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), speedometerScale), 0.0F);
		this.mesh.chunkSetAngles("Z_ASI2", 0.0F, -floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), speedometerScale), 0.0F);
		w.set(this.fm.getW());
		fm.Or.transform(w);
		this.mesh.chunkSetAngles("Z_Horizonte1", 0.0F, fm.Or.getKren(), 0.0F);
		this.mesh.chunkSetAngles("Z_Horizonte2", 0.0F, 0.0F, -fm.Or.getTangage());
		this.mesh.chunkSetAngles("Z_Horizonte3", 0.0F, fm.Or.getKren(), 0.0F);
		this.mesh.chunkSetAngles("Z_Horizonte4", 0.0F, 0.0F, -fm.Or.getTangage());
		this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -90F + -setNew.azimuth, 0.0F);
		this.mesh.chunkSetAngles("Z_Compass2", 0.0F, -90F + -interp(setNew.waypointAzimuth, setOld.waypointAzimuth, f), 0.0F);
		this.mesh.chunkSetAngles("Z_Compass3", 0.0F, -90F + -setNew.azimuth, 0.0F);
		this.mesh.chunkSetAngles("Z_Compass4", 0.0F, -90F + -interp(setNew.waypointAzimuth, setOld.waypointAzimuth, f), 0.0F);
		this.mesh.chunkSetAngles("Z_Compass5", 0.0F, -90F + -interp(setNew.waypointAzimuth, setOld.waypointAzimuth, f), 0.0F);
		this.mesh.chunkSetAngles("Z_Compass6", 0.0F, -90F + -interp(setNew.waypointAzimuth, setOld.waypointAzimuth, f), 0.0F);
		this.mesh.chunkSetAngles("Z_Variometro1", 0.0F, -floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
		this.mesh.chunkSetAngles("Z_Variometro2", 0.0F, -floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
		rotorrpm = Math.abs((int) ((double) (fm.EI.engines[0].getw() * 1.0F) + fm.Vwld.length() * 4D));
		if (rotorrpm >= 250) rotorrpm = 250;
		if (rotorrpm <= 40) rotorrpm = 0;
		this.mesh.chunkSetAngles("Z_RotorRPM", 0.0F, -rotorrpm, 0.0F);
		this.mesh.chunkSetAngles("Z_EngineRPM", 0.0F, -cvt(fm.EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 504F), 0.0F);
		this.mesh.chunkSetAngles("Z_Temp1", 0.0F, -cvt(fm.EI.engines[0].tOilOut, 20F, 120F, 0.0F, 210F), 0.0F);
		this.mesh.chunkSetAngles("Z_Oil1", 0.0F, -cvt(fm.EI.engines[0].tOilOut, 20F, 120F, 0.0F, 250F), 0.0F);
		this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, -cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness(), 0.0F, 7.45F, 0.0F, 256F), 0.0F);
		this.mesh.chunkSetAngles("Z_FuelPres1", 0.0F, -cvt(fm.M.fuel <= 5F ? 0.0F : 0.78F, 0.0F, 1.0F, 0.0F, 255F), 0.0F);
		this.mesh.chunkSetAngles("Z_Compass", setNew.azimuth, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Hora", 0.0F, -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		this.mesh.chunkSetAngles("Z_Minuto", 0.0F, -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		this.mesh.chunkSetAngles("Z_Fuel1", 0.0F, cvt(fm.M.fuel, 0.0F, 180F, 0.0F, 110F), 0.0F);
		this.mesh.chunkSetAngles("Z_Fuel2", 0.0F, -cvt(fm.M.fuel, 0.0F, 180F, 0.0F, 110F), 0.0F);
		w.set(this.fm.getW());
		fm.Or.transform(w);
		this.mesh.chunkSetAngles("Z_Palo", 0.0F, -cvt(w.z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F);
		this.mesh.chunkSetAngles("Z_Bola", 0.0F, -cvt(getBall(7D), -7F, 7F, -15F, 15F), 0.0F);
		this.mesh.chunkSetAngles("Z_RadioAlt", 0.0F, cvt(interp(setNew.radioalt, setOld.radioalt, f), 6.27F, 306.27F, 0.0F, -330F), 0.0F);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("XGlassDamage1", true);
		if ((fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
		if ((fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
		if ((fm.AS.astateCockpitState & 2) != 0) this.mesh.chunkVisible("XGlassDamage3", true);
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		this.mesh.materialReplace("Gloss1D0o", mat);
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) setNightMats(true);
		else setNightMats(false);
	}

	public void toggleDim() {
		this.cockpitDimControl = !this.cockpitDimControl;
	}

	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private float pictAiler;
	private float pictElev;
	private int rotorrpm;
	private boolean bNeedSetUp;
	private static final float speedometerScale[] = { 0.0F, 15.5F, 77F, 175F, 275F, 360F, 412F, 471F, 539F, 610.5F, 669.75F, 719F };
	private static final float variometerScale[] = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };
	private Point3d tmpP;
	private Vector3d tmpV;

}
