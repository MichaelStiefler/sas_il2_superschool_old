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

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitJU525E_GunnerOpen extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("TurretA", -orient.getYaw(), 0.0F, 0.0F);
		mesh.chunkSetAngles("TurretB", 0.0F, 0.0F, -orient.getTangage());
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f < -50F)
					f = -50F;
				if (f > 50F)
					f = 50F;
				if (f1 > 45F)
					f1 = 45F;
				if (f1 < -5F)
					f1 = -5F;
				if (Math.abs(f) < 3.5F) {
					if (f1 < -2.5F)
						f1 = -2.5F;
				} else if (Math.abs(f) < 18.5F && f1 < -2.5F - 0.6333333F * (Math.abs(f) - 3.5F))
					f1 = -2.5F - 0.6333333F * (Math.abs(f) - 3.5F);
				orient.setYPR(f, f1, 0.0F);
				orient.wrap();
			}
	}

	protected void interpTick() {
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
			if (bGunFire) {
				if (hook1 == null)
					hook1 = new HookNamed(aircraft(), "_MGUN01");
				doHitMasterAircraft(aircraft(), hook1, "_MGUN01");
			}
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

	public CockpitJU525E_GunnerOpen() {
		super("3DO/Cockpit/Ju52-TGun/hier.him", "he111_gunner");
		bNeedSetUp = true;
		hook1 = null;
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			bNeedSetUp = false;
			reflectPlaneMats();
		}
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("CF_D" + aircraft().chunkDamageVisible("CF"), false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("CF_D" + aircraft().chunkDamageVisible("CF"), true);
		super.doFocusLeave();
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
		mesh.materialReplace("Gloss1D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
		mesh.materialReplace("Gloss1D2o", mat);
	}

	private Hook hook1;
	private boolean bNeedSetUp;

	static {
		Property.set(CockpitJU525E_GunnerOpen.class, "aiTuretNum", 0);
		Property.set(CockpitJU525E_GunnerOpen.class, "weaponControlNum", 10);
		Property.set(CockpitJU525E_GunnerOpen.class, "astatePilotIndx", 1);
	}
}
