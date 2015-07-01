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
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Time;

public class CockpitU2VS extends CockpitPilot {
	private class Variables {

		float throttle;
		float prop;
		float mix;
		float turn;
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
				if (bNeedSetUp) {
					reflectPlaneMats();
					bNeedSetUp = false;
				}
				if (((U_2VS) aircraft()).bChangedPit) {
					reflectPlaneToModel();
					((U_2VS) aircraft()).bChangedPit = false;
				}
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				setNew.throttle = (10F * setOld.throttle + fm.CT.PowerControl) / 11F;
				setNew.prop = (10F * setOld.prop + fm.EI.engines[0].getControlProp()) / 11F;
				setNew.mix = (10F * setOld.mix + ((FlightModelMain) (fm)).EI.engines[0].getControlMix()) / 11F;
				setNew.altimeter = fm.getAltitude();
				if (Math.abs(fm.Or.getKren()) < 30F)
					setNew.azimuth = (35F * setOld.azimuth + -fm.Or.getYaw()) / 36F;
				if (setOld.azimuth > 270F && setNew.azimuth < 90F)
					setOld.azimuth -= 360F;
				if (setOld.azimuth < 90F && setNew.azimuth > 270F)
					setOld.azimuth += 360F;
				setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth) + World.Rnd().nextFloat(-10F, 10F)) / 11F;
				setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
				setNew.turn = (12F * setOld.turn + w.z) / 13F;
				w.set(fm.getW());
				mesh.chunkSetAngles("Turret1AP", 0.0F, ((SndAircraft) (aircraft())).FM.turret[0].tu[0], 0.0F);
				mesh.chunkSetAngles("Turret1BP", 0.0F, ((SndAircraft) (aircraft())).FM.turret[0].tu[1], 0.0F);
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

	public CockpitU2VS() {
		super("3DO/Cockpit/U-2vs/hier.him", "i16");
		setOld = new Variables();
		setNew = new Variables();
		w = new Vector3f();
		bNeedSetUp = true;
		pictAiler = 0.0F;
		pictElev = 0.0F;
		tmpP = new Point3d();
		tmpV = new Vector3d();
		cockpitNightMats = (new String[] { "prib_one", "prib_one_dd", "prib_two", "prib_two_dd", "prib_three", "prib_three_dd", "prib_four", "prib_four_dd", "shkala", "oxigen" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
		if (((U_2VS) aircraft()).bChangedPit) {
			reflectPlaneToModel();
			((U_2VS) aircraft()).bChangedPit = false;
		}
		super.mesh.chunkSetAngles("StickBase", 0.0F, 15F * pictAiler, 0.0F);
		super.mesh.chunkSetAngles("Stick", 0.0F, 0.0F * (pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl), 12F * (pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl));
		super.mesh.chunkSetAngles("Stick2", 0.0F, 0.0F * (pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl), 12F * (pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl));
		super.mesh.chunkSetAngles("SticksRod", 0.0F, pictElev * 12F, 0.0F);
		super.mesh.chunkSetAngles("StickElevRod", 0.0F, pictElev * 12F, 0.0F);
		super.mesh.chunkSetAngles("Rudder", 15F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Rudder2", 11F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("RCableL", -15F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("RCableL2", -29F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("RCableR", -15F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("RCableR2", -29F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, -25F + 50F * -interp(setNew.throttle, setOld.throttle, f));
		super.mesh.chunkSetAngles("Mixture", 0.0F, 0.0F, -25F + 50F * interp(setNew.mix, setOld.mix, f));
		super.mesh.chunkSetAngles("zMagnetoSwitch", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("zCompass", 0.0F, 90F + interp(-setNew.azimuth, -setOld.azimuth, f), 0.0F);
		if ((fm.AS.astateCockpitState & 4) == 0)
			mesh.chunkSetAngles("zRPS1a", 0.0F, floatindex(cvt(fm.EI.engines[0].getRPM(), 0.0F, 2400F, 0.0F, 11F), rpmScale), 0.0F);
		mesh.chunkSetAngles("zAlt1a", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
		mesh.chunkSetAngles("zAlt1b", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("zSpeed1a", 0.0F, floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 600F, 0.0F, 12F), speedometerScale), 0.0F);
		mesh.chunkSetAngles("zTurn1a", 0.0F, cvt(setNew.turn, -0.2F, 0.2F, 26F, -26F), 0.0F);
		mesh.chunkSetAngles("zSlide1a", 0.0F, cvt(getBall(8D), -8F, 8F, 26F, -26F), 0.0F);
		mesh.chunkSetAngles("zVariometer1a", 0.0F, cvt(setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
		mesh.chunkSetAngles("zTOilOut1a", 0.0F, cvt(fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 180F), 0.0F);
		mesh.chunkSetAngles("zOilPrs1a", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 180F), 0.0F);
		mesh.chunkSetAngles("zClock1a", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("zClock1b", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		mesh.chunkSetAngles("zTwater1a", 0.0F, cvt(fm.EI.engines[0].tWaterOut, 0.0F, 300F, 0.0F, -60F), 0.0F);
		mesh.chunkSetAngles("zGasPrs1a", 0.0F, cvt(fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 8F, 0.0F, 180F), 0.0F);
	}

	public void reflectCockpitState() {
	}

	protected void reflectPlaneToModel() {
		HierMesh hiermesh = aircraft().hierMesh();
		mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
		mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
		mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
		mesh.chunkVisible("CF_D3", hiermesh.isChunkVisible("CF_D3"));
		mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
		mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
		mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
		mesh.chunkVisible("WingLIn_D3", hiermesh.isChunkVisible("WingLIn_D3"));
		mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
		mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
		mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
		mesh.chunkVisible("WingRIn_D3", hiermesh.isChunkVisible("WingRIn_D3"));
		mesh.chunkVisible("WingLOut_D0", hiermesh.isChunkVisible("WingLOut_D0"));
		mesh.chunkVisible("WingLOut_D1", hiermesh.isChunkVisible("WingLOut_D1"));
		mesh.chunkVisible("WingLOut_D2", hiermesh.isChunkVisible("WingLOut_D2"));
		mesh.chunkVisible("WingLOut_D3", hiermesh.isChunkVisible("WingLOut_D3"));
		mesh.chunkVisible("WingROut_D0", hiermesh.isChunkVisible("WingROut_D0"));
		mesh.chunkVisible("WingROut_D1", hiermesh.isChunkVisible("WingROut_D1"));
		mesh.chunkVisible("WingROut_D2", hiermesh.isChunkVisible("WingROut_D2"));
		mesh.chunkVisible("WingROut_D3", hiermesh.isChunkVisible("WingROut_D3"));
		mesh.chunkVisible("Pilot2_D0", hiermesh.isChunkVisible("Pilot2_D0"));
		mesh.chunkVisible("Pilot2_D1", hiermesh.isChunkVisible("Pilot2_D1"));
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
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
		mesh.materialReplace("Gloss2D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
		mesh.materialReplace("Matt1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
		mesh.materialReplace("Matt1D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
		mesh.materialReplace("Matt2D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
		mesh.materialReplace("Matt2D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("OverlayD1o"));
		mesh.materialReplace("OverlayD1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("OverlayD2o"));
		mesh.materialReplace("OverlayD2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Pilot2"));
		mesh.materialReplace("Pilot2", mat);
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
	private boolean bNeedSetUp;
	private float pictAiler;
	private float pictElev;
	private static final float speedometerScale[] = { 0.0F, 0.0F, 18F, 45F, 75.5F, 107F, 137.5F, 170F, 206.5F, 243.75F, 286.5F, 329.5F, 374.5F };
	private static final float rpmScale[] = { 0.0F, 5.5F, 18.5F, 59F, 99.5F, 134.5F, 165.75F, 198F, 228F, 255.5F, 308F, 345F };
	private Point3d tmpP;
	private Vector3d tmpV;
}
