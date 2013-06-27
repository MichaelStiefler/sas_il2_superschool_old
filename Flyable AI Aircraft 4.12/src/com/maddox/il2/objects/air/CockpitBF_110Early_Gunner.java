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
// Last Edited at: 2013/01/27

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FMMath;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitBF_110Early_Gunner extends CockpitGunner {
	private class Variables {

		float altimeter;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		AnglesFork radioCompassAzimuth;
		float beaconDirection;
		float beaconRange;

		private Variables() {
			azimuth = new AnglesFork();
			waypointAzimuth = new AnglesFork();
			radioCompassAzimuth = new AnglesFork();
		}

	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			setTmp = setOld;
			setOld = setNew;
			setNew = setTmp;
			setNew.altimeter = fm.getAltitude();
			if (fm == null)
				return true;
			if (bNeedSetUp) {
				reflectPlaneMats();
				bNeedSetUp = false;
			}
			float f = waypointAzimuthInvertMinus(20F);
			if (useRealisticNavigationInstruments()) {
				setNew.waypointAzimuth.setDeg(f - 90F);
				setOld.waypointAzimuth.setDeg(f - 90F);
				setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
			} else {
				setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(1.0F));
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
		bBeaconKeysEnabled = ((AircraftLH) aircraft()).bWantBeaconKeys;
		((AircraftLH) aircraft()).bWantBeaconKeys = true;
		if (super.doFocusEnter()) {
			HierMesh hiermesh = aircraft().hierMesh();
			aircraft().hierMesh().chunkVisible("CF_D" + aircraft().chunkDamageVisible("CF"), false);
			// hiermesh.chunkVisible("Interior_D0", false);
			hiermesh.chunkVisible("Blister1_D0", false);
			hiermesh.chunkVisible("Blister2_D0", false);
			// hiermesh.chunkVisible("Blister3_D0", false);
			// hiermesh.chunkVisible("Blister4_D0", false);
			// hiermesh.chunkVisible("Blister5_D0", false);
			hiermesh.chunkVisible("Turret1B_D0", false);
			hiermesh.chunkVisible("Head1_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		HierMesh hiermesh = aircraft().hierMesh();
		aircraft().hierMesh().chunkVisible("CF_D" + aircraft().chunkDamageVisible("CF"), true);
		// hiermesh.chunkVisible("Interior_D0", true);
		hiermesh.chunkVisible("Blister1_D0", true);
		hiermesh.chunkVisible("Blister2_D0", true);
		// hiermesh.chunkVisible("Blister3_D0", true);
		// hiermesh.chunkVisible("Blister4_D0", true);
		// hiermesh.chunkVisible("Blister5_D0", true);
		hiermesh.chunkVisible("Turret1B_D0", true);
		hiermesh.chunkVisible("Head1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
		super.doFocusLeave();
		((AircraftLH) aircraft()).bWantBeaconKeys = bBeaconKeysEnabled;
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		mesh.chunkSetAngles("TurretA", 1.2F, -f, 0.0F);
		mesh.chunkSetAngles("TurretB", 0.0F, f1, 0.0F);
		mesh.chunkSetAngles("TurretC", 0.0F, -FMMath.clamp(f, -cvt(f1, -19F, 12F, 5F, 35F), cvt(f1, -19F, 12F, 5F, 35F)), 0.0F);
		mesh.chunkSetAngles("TurretD", 0.0F, f1, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (!isRealMode())
			return;
		if (!aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = -orient.getYaw();
		float f1 = orient.getTangage();
		if (f1 < -19F)
			f1 = -19F;
		if (f1 > 30F)
			f1 = 30F;
		float f2;
		if (f1 < 0.0F)
			f2 = cvt(f1, -19F, 0.0F, 20F, 30F);
		else if (f1 < 12F)
			f2 = cvt(f1, 0.0F, 12F, 30F, 35F);
		else
			f2 = cvt(f1, 12F, 30F, 35F, 40F);
		if (f < 0.0F) {
			if (f < -f2)
				f = -f2;
		} else if (f > f2)
			f = f2;
		orient.setYPR(-f, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!isRealMode())
			return;
		if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
			bGunFire = false;
		fm.CT.WeaponControl[10] = bGunFire;
		if (bGunFire) {
			if (hook1 == null)
				hook1 = new HookNamed(aircraft(), "_MGUN05");
			doHitMasterAircraft(aircraft(), hook1, "_MGUN05");
		}
	}

	public void doGunFire(boolean flag) {
		if (!isRealMode())
			return;
		if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
			bGunFire = false;
		else
			bGunFire = flag;
		fm.CT.WeaponControl[10] = bGunFire;
	}

	public CockpitBF_110Early_Gunner() {
		super("3DO/Cockpit/Bf-110C/hiergun.him", "bf109");
		bNeedSetUp = true;
		hook1 = null;
		setOld = new Variables();
		setNew = new Variables();
		cockpitNightMats = (new String[] { "cadranR1", "radio", "bague2" });
		setNightMats(false);
		interpPut(new Interpolater(), null, Time.current(), null);
		HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK01");
		Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
		light1 = new LightPointActor(new LightPoint(), loc.getPoint());
		light1.light.setColor(126F, 232F, 245F);
		light1.light.setEmit(0.0F, 0.0F);
		pos.base().draw.lightMap().put("LAMPHOOK1", light1);
		printCompassHeading = true;
		bBeaconKeysEnabled = ((AircraftLH) aircraft()).bWantBeaconKeys;
		((AircraftLH) aircraft()).bWantBeaconKeys = true;
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
		mesh.materialReplace("Gloss2D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Pilot1"));
		mesh.materialReplace("Pilot1", mat);
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl) {
			light1.light.setEmit(0.004F, 1.0F);
			setNightMats(true);
		} else {
			light1.light.setEmit(0.0F, 0.0F);
			setNightMats(false);
		}
	}

	public void reflectWorldToInstruments(float f) {
		if (fm == null)
			return;
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
		mesh.chunkVisible("Head1_D0", aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
		mesh.chunkVisible("Head1_D1", aircraft().hierMesh().isChunkVisible("Pilot1_D1"));
		mesh.chunkSetAngles("zAlt2", cvt(interp(setNew.altimeter, setNew.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
		mesh.chunkSetAngles("zAlt1", cvt(interp(setNew.altimeter, setNew.altimeter, f), 0.0F, 14000F, 0.0F, 313F), 0.0F, 0.0F);
		if (useRealisticNavigationInstruments()) {
			mesh.chunkSetAngles("Z_CompassRim", 0.0F, -setNew.waypointAzimuth.getDeg(f), 0.0F);
			mesh.chunkSetAngles("Z_CompassPlane", 0.0F, setNew.azimuth.getDeg(f) - setNew.waypointAzimuth.getDeg(f), 0.0F);
			mesh.chunkSetAngles("Z_CompassNeedle", 0.0F, setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F);
		} else {
			mesh.chunkSetAngles("Z_CompassRim", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
			mesh.chunkSetAngles("Z_CompassPlane", 0.0F, setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F);
			mesh.chunkSetAngles("Z_CompassNeedle", 0.0F, 0.0F, 0.0F);
		}
		if (aircraft().FM.AS.listenLorenzBlindLanding) {
			mesh.chunkSetAngles("Z_AFN12", 0.0F, cvt(setNew.beaconDirection, -45F, 45F, -14F, 14F), 0.0F);
			mesh.chunkSetAngles("Z_AFN11", 0.0F, cvt(setNew.beaconRange, 0.0F, 1.0F, 26.5F, -26.5F), 0.0F);
			mesh.chunkSetAngles("Z_AFN22", 0.0F, 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_AFN21", 0.0F, 20F, 0.0F);
			mesh.chunkVisible("AFN1_RED", isOnBlindLandingMarker());
		} else {
			mesh.chunkSetAngles("Z_AFN22", 0.0F, cvt(setNew.beaconDirection, -45F, 45F, -20F, 20F), 0.0F);
			mesh.chunkSetAngles("Z_AFN21", 0.0F, cvt(setNew.beaconRange, 0.0F, 1.0F, 20F, -20F), 0.0F);
			mesh.chunkSetAngles("Z_AFN12", 0.0F, 0.0F, 0.0F);
			mesh.chunkSetAngles("Z_AFN11", 0.0F, -26.5F, 0.0F);
			mesh.chunkVisible("AFN1_RED", false);
		}
	}

	private boolean bNeedSetUp;
	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	private LightPointActor light1;
	private boolean bBeaconKeysEnabled;
	private Hook hook1;

	static {
		Property.set(CLASS.THIS(), "normZN", 0.8F);
	}

}
