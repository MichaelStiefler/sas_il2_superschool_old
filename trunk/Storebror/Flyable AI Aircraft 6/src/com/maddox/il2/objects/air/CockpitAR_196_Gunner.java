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

public class CockpitAR_196_Gunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("Turret1B_D0", aircraft().hierMesh().isChunkVisible("Turret1A_D0"));
		super.doFocusLeave();
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		orient.add(new Orient(0.0F, 30.0F, 0.0F));
		mesh.chunkSetAngles("Turret_Base", 0.0F, orient.getYaw(), 0.0F);
		mesh.chunkSetAngles("MGun", 0.0F, orient.getTangage(), orient.getKren() * 0.75F);
		mesh.chunkSetAngles("Turret_Base2", 0.0F, orient.getYaw(), 0.0F);
		mesh.chunkSetAngles("MGun2", 0.0F, cvt(orient.getTangage(), -20F, 45F, -20F, 45F), 0.0F);
		orient.sub(new Orient(0.0F, 30.0F, 0.0F));
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				if (f < -90F)
					f = -90F;
				if (f > 90F)
					f = 90F;
				float f1 = orient.getTangage();
				if (f1 > 60F)
					f1 = 60F;
				if (f1 < -30F)
					f1 = -30F;
				float f2;
				for (f2 = Math.abs(f); f2 > 180F; f2 -= 180F)
					;
				if (f1 < -floatindex(cvt(f2, 0.0F, 180F, 0.0F, 36F), angles))
					f1 = -floatindex(cvt(f2, 0.0F, 180F, 0.0F, 36F), angles);
				orient.setYPR(f, f1, 0.0F);
				orient.wrap();
			}
	}

	protected void interpTick() {
		if (bGunFire)
			mesh.chunkSetAngles("Trigger", 0.0F, 17.5F, 0.0F);
		else
			mesh.chunkSetAngles("Trigger", 0.0F, 0.0F, 0.0F);
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

	public CockpitAR_196_Gunner() {
		super("3DO/Cockpit/SBD-3-TGun/AR_196_Gun.him", "he111_gunner");
		hook1 = null;
		bNeedSetUp = true;
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
	}

	private static final float angles[] = { 4F, 5.5F, 5.5F, 7F, 10.5F, 15.5F, 24F, 33F, 40F, 46F, 52.5F, 59F, 64.5F, 69F, 69F, 69F, 69F, 69F, 69F, 69F, 69F, 69F, 69F, 66.5F, 62.5F, 55F, 49.5F, -40F, -74.5F, -77F, -77F, -77F, -77F, -77F, -77F, -77F, -77F };
	private Hook hook1;
	private boolean bNeedSetUp;

	static {
		Property.set(CockpitAR_196_Gunner.class, "aiTuretNum", 0);
		Property.set(CockpitAR_196_Gunner.class, "weaponControlNum", 10);
		Property.set(CockpitAR_196_Gunner.class, "astatePilotIndx", 1);
	}
}
