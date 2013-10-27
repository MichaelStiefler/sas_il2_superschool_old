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

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitDB3M_TGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("Turret2B_D0", aircraft().hierMesh().isChunkVisible("Turret2A_D0"));
		super.doFocusLeave();
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		mesh.chunkSetAngles("TurretA", 0.0F, f, 0.0F);
		mesh.chunkSetAngles("TurretB", 0.0F, f1, 0.0F);
		if (f1 > 50F)
			f1 = 50F;
		mesh.chunkSetAngles("Cam_B", 0.0F, f1, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		float f2 = Math.abs(f);
		for (; f < -180F; f += 360F)
			;
		for (; f > 180F; f -= 360F)
			;
		for (; prevA0 < -180F; prevA0 += 360F)
			;
		for (; prevA0 > 180F; prevA0 -= 360F)
			;
		if (!isRealMode()) {
			prevA0 = f;
		} else {
			if (bNeedSetUp) {
				prevTime = Time.current() - 1L;
				bNeedSetUp = false;
			}
			if (f < -120F && prevA0 > 120F)
				f += 360F;
			else if (f > 120F && prevA0 < -120F)
				prevA0 += 360F;
			float f3 = f - prevA0;
			float f4 = 0.002F * (float) (Time.current() - prevTime);
			float f5 = Math.abs(f3 / f4);
			if (f5 > 120F)
				if (f > prevA0)
					f = prevA0 + 120F * f4;
				else if (f < prevA0)
					f = prevA0 - 120F * f4;
			prevTime = Time.current();
			if (f1 > 65F)
				f1 = 65F;
			float f6;
			if (f2 <= 90F)
				f6 = Math.max((float) (-Math.pow(0.1F * f2, 2D)) - 3F, -15F);
			else
				f6 = Math.max((float) (-Math.pow(0.13F * (180F - f2), 2D)), -15F);
			if (f1 < f6)
				f1 = f6;
			orient.setYPR(f, f1, 0.0F);
			orient.wrap();
			prevA0 = f;
		}
	}

	protected void interpTick() {
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
			if (bGunFire) {
				if (hook1 == null)
					hook1 = new HookNamed(aircraft(), "_MGUN02");
				doHitMasterAircraft(aircraft(), hook1, "_MGUN02");
			}
		}
		if (fm != null) {
			azimuthOld = azimuthNew;
			azimuthNew.setDeg(azimuthOld.getDeg(1.0F), fm.Or.azimut());
			altOld = altNew;
			altNew = fm.getAltitude();
		}
	}

	public void doGunFire(boolean flag) {
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			else
				bGunFire = flag;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
		}
	}

	public CockpitDB3M_TGunner() {
		super("3DO/Cockpit/Il-4-TGun/hier.him", "he111_gunner");
		bNeedSetUp = true;
		bNeedSetUp1 = true;
		prevTime = -1L;
		prevA0 = 0.0F;
		hook1 = null;
		azimuthOld = new AnglesFork();
		azimuthNew = new AnglesFork();
		cockpitNightMats = (new String[] { "Prib_One", "Prib_Four", "Shkala128" });
		setNightMats(false);
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp1) {
			reflectPlaneMats();
			bNeedSetUp1 = false;
		}
		mesh.chunkSetAngles("zAzimuth1b", 0.0F, -azimuthNew.getDeg(f), 0.0F);
		mesh.chunkSetAngles("zAlt1a", 0.0F, cvt(interp(altNew, altOld, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
		mesh.chunkSetAngles("zAlt1b", 0.0F, cvt(interp(altNew, altOld, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("zSpeed1a", 0.0F, floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F);
	}

	public void reflectCockpitState() {
		switch (aircraft().chunkDamageVisible("CF")) {
		case 1: // '\001'
			mesh.chunkVisible("CF_D0", false);
			mesh.chunkVisible("CF_D1", true);
			mesh.chunkVisible("CF_D2", false);
			mesh.chunkVisible("xHolesDm1a", true);
			mesh.chunkVisible("xHolesDm1b", true);
			mesh.chunkVisible("xHolesDm2a", false);
			mesh.chunkVisible("xHolesDm2b", false);
			break;

		case 2: // '\002'
			mesh.chunkVisible("CF_D0", false);
			mesh.chunkVisible("CF_D1", false);
			mesh.chunkVisible("CF_D2", true);
			mesh.chunkVisible("xHolesDm1a", true);
			mesh.chunkVisible("xHolesDm1b", true);
			mesh.chunkVisible("xHolesDm2a", true);
			mesh.chunkVisible("xHolesDm2b", true);
			break;

		case 3: // '\003'
			mesh.chunkVisible("CF_D0", false);
			mesh.chunkVisible("CF_D1", false);
			mesh.chunkVisible("CF_D2", true);
			mesh.chunkVisible("xHolesDm1a", true);
			mesh.chunkVisible("xHolesDm1b", true);
			mesh.chunkVisible("xHolesDm2a", true);
			mesh.chunkVisible("xHolesDm2b", true);
			break;

		default:
			mesh.chunkVisible("CF_D0", true);
			mesh.chunkVisible("CF_D1", false);
			mesh.chunkVisible("CF_D2", false);
			mesh.chunkVisible("xHolesDm1a", false);
			mesh.chunkVisible("xHolesDm1b", false);
			mesh.chunkVisible("xHolesDm2a", false);
			mesh.chunkVisible("xHolesDm2b", false);
			break;
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
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
		mesh.materialReplace("Gloss2D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
		mesh.materialReplace("Gloss2D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
		mesh.materialReplace("Gloss2D2o", mat);
	}

	private boolean bNeedSetUp;
	private boolean bNeedSetUp1;
	private long prevTime;
	private float prevA0;
	private Hook hook1;
	AnglesFork azimuthOld;
	AnglesFork azimuthNew;
	float altOld;
	float altNew;
	private static final float speedometerScale[] = { 0.0F, 0.0F, 15.5F, 50F, 95.5F, 137F, 182.5F, 212F, 230F, 242F, 254.5F, 267.5F, 279F, 292F, 304F, 317F, 329.5F, 330F };

	static {
		Property.set(CockpitDB3M_TGunner.class, "aiTuretNum", 1);
		Property.set(CockpitDB3M_TGunner.class, "weaponControlNum", 11);
		Property.set(CockpitDB3M_TGunner.class, "astatePilotIndx", 2);
	}
}
