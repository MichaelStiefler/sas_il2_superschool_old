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
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHS_123B1 extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (fm != null) {
				if (bNeedSetUp) {
					reflectPlaneMats();
					bNeedSetUp = false;
				}
				if (((HS_123B1) aircraft()).bChangedPit) {
					reflectPlaneToModel();
					((HS_123B1) aircraft()).bChangedPit = false;
				}
				setTmp = setOld;
				setOld = setNew;
				setNew = setTmp;
				setNew.altimeter = fm.getAltitude();
				if (cockpitDimControl) {
					if (setNew.dimPosition > 0.0F)
						setNew.dimPosition = setOld.dimPosition - 0.05F;
				} else if (setNew.dimPosition < 1.0F)
					setNew.dimPosition = setOld.dimPosition + 0.05F;
				setNew.throttle = (10F * setOld.throttle + fm.CT.PowerControl) / 11F;
				setNew.mix = (8F * setOld.mix + fm.EI.engines[0].getControlMix()) / 9F;
				float f = waypointAzimuth();
				if (useRealisticNavigationInstruments()) {
					setNew.waypointAzimuth.setDeg(f - 90F);
					setOld.waypointAzimuth.setDeg(f - 90F);
				} else {
					setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(1.0F));
				}
				setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
				w.set(fm.getW());
				fm.Or.transform(w);
				setNew.turn = (12F * setOld.turn + w.z) / 13F;
			}
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float altimeter;
		float throttle;
		float dimPosition;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		float turn;
		float mix;

		private Variables() {
			azimuth = new AnglesFork();
			waypointAzimuth = new AnglesFork();
		}

	}

	protected float waypointAzimuth() {
		return super.waypointAzimuthInvertMinus(30F);
	}

	protected void setCameraOffset() {
		cameraCenter.add(0.0D, 0.019999999552965164D, 0.0D);
	}

	public CockpitHS_123B1() {
		super("3DO/Cockpit/HS-123/hier.him", "i16");
		setOld = new Variables();
		setNew = new Variables();
		pictAiler = 0.0F;
		pictElev = 0.0F;
		pictManifold = 0.0F;
		bNeedSetUp = true;
		w = new Vector3f();
		setNew.dimPosition = 1.0F;
		HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK1");
		Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
		light1 = new LightPointActor(new LightPoint(), loc.getPoint());
		light1.light.setColor(227F, 65F, 33F);
		light1.light.setEmit(0.0F, 0.0F);
		pos.base().draw.lightMap().put("LAMPHOOK1", light1);
		hooknamed = new HookNamed(mesh, "LAMPHOOK2");
		loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
		light2 = new LightPointActor(new LightPoint(), loc.getPoint());
		light2.light.setColor(227F, 65F, 33F);
		light2.light.setEmit(0.0F, 0.0F);
		pos.base().draw.lightMap().put("LAMPHOOK2", light2);
		cockpitNightMats = (new String[] { "ZClocks1", "ZClocks1DMG", "ZClocks2", "ZClocks3", "FW190A4Compass", "oxigen" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		limits6DoF = (new float[] { 0.7F, 0.055F, -0.07F, 0.11F, 0.07F, -0.13F, 0.03F, -0.03F });
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
		if (((HS_123B1) aircraft()).bChangedPit) {
			reflectPlaneToModel();
			((HS_123B1) aircraft()).bChangedPit = false;
		}
		mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Altimeter2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_ReviTinter", cvt(interp(setNew.dimPosition, setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -30F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_ReviTint", cvt(interp(setNew.dimPosition, setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 40F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_ATA1", 15.5F + cvt(pictManifold = 0.75F * pictManifold + 0.25F * fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 336F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_RPM1", floatindex(cvt(fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_FuelQuantity1", -44.5F + floatindex(cvt(fm.M.fuel / 0.72F, 0.0F, 400F, 0.0F, 8F), fuelScale), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Iengtemprad1", cvt(fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 58.5F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_EngTemp1", cvt(fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 58.5F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_FuelPress1", cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_OilPress1", cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_TurnBank1", cvt(setNew.turn, -0.23562F, 0.23562F, 30F, -30F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_TurnBank2", cvt(getBall(6D), -6F, 6F, -7F, 7F), 0.0F, 0.0F);
		if (useRealisticNavigationInstruments()) {
			mesh.chunkSetAngles("Z_Azimuth1", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_Compass1", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
		} else {
			mesh.chunkSetAngles("Z_Compass1", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
			mesh.chunkSetAngles("Z_Azimuth1", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
		}
		mesh.chunkSetAngles("Z_PropPitch1", 270F - (float) Math.toDegrees(fm.EI.engines[0].getPropPhi() - fm.EI.engines[0].getPropPhiMin()) * 60F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_PropPitch2", 105F - (float) Math.toDegrees(fm.EI.engines[0].getPropPhi() - fm.EI.engines[0].getPropPhiMin()) * 5F, 0.0F, 0.0F);
		mesh.chunkVisible("Z_FuelRed1", fm.M.fuel < 36F);
		mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Second1", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Column", (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 15F, 0.0F, (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 10F);
		mesh.chunkSetAngles("Z_PedalStrut", fm.CT.getRudder() * 15F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_PedalStrut2", fm.CT.getRudder() * 15F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_LeftPedal", -fm.CT.getRudder() * 15F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_RightPedal", -fm.CT.getRudder() * 15F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Throttle", interp(setNew.throttle, setOld.throttle, f) * 57.72727F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Throttle_tube", -interp(setNew.throttle, setOld.throttle, f) * 57.72727F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Mix", interp(setNew.mix, setOld.mix, f) * 70F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Mix_tros", -interp(setNew.mix, setOld.mix, f) * 70F, 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_MagnetoSwitch", -45F + 28.333F * (float) fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
	}

	public void toggleDim() {
		cockpitDimControl = !cockpitDimControl;
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl) {
			light1.light.setEmit(0.005F, 0.5F);
			light2.light.setEmit(0.005F, 0.5F);
			setNightMats(true);
		} else {
			light1.light.setEmit(0.0F, 0.0F);
			light2.light.setEmit(0.0F, 0.0F);
			setNightMats(false);
		}
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 2) != 0)
			mesh.chunkVisible("Z_HullDamage1", true);
		if ((fm.AS.astateCockpitState & 1) != 0 && (fm.AS.astateCockpitState & 0x40) != 0) {
			mesh.chunkVisible("Z_HullDamage1", true);
			mesh.chunkVisible("Z_HullDamage2", true);
		}
		if ((fm.AS.astateCockpitState & 4) != 0)
			mesh.chunkVisible("Z_HullDamage4", true);
		if ((fm.AS.astateCockpitState & 8) == 0)
			;
		if ((fm.AS.astateCockpitState & 0x80) != 0)
			mesh.chunkVisible("Z_OilSplats_D1", true);
		if ((fm.AS.astateCockpitState & 0x10) != 0)
			mesh.chunkVisible("Z_HullDamage3", true);
		if ((fm.AS.astateCockpitState & 0x20) != 0)
			mesh.chunkVisible("Z_HullDamage2", true);
	}

	protected void reflectPlaneToModel() {
		HierMesh hiermesh = aircraft().hierMesh();
		mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
		mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
		mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
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
		mat = hiermesh.material(hiermesh.materialFind("OverlayD1o"));
		mesh.materialReplace("OverlayD1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("OverlayD2o"));
		mesh.materialReplace("OverlayD2o", mat);
	}

	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	private LightPointActor light1;
	private LightPointActor light2;
	private float pictAiler;
	private float pictElev;
	private float pictManifold;
	private boolean bNeedSetUp;
	public Vector3f w;
	private static final float speedometerScale[] = { 0.0F, -12.33333F, 18.5F, 37F, 62.5F, 90F, 110.5F, 134F, 158.5F, 186F, 212.5F, 238.5F, 265F, 289.5F, 315F, 339.5F, 346F, 346F };
	private static final float rpmScale[] = { 0.0F, 11.25F, 54F, 111F, 171.5F, 229.5F, 282.5F, 334F, 342.5F, 342.5F };
	private static final float fuelScale[] = { 0.0F, 9F, 21F, 29.5F, 37F, 48F, 61.5F, 75.5F, 92F, 92F };

	static {
		Property.set(CLASS.THIS(), "normZNs", new float[] { 0.72F, 0.6F, 0.57F, 0.6F });
	}

}
