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

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class CockpitFI_156_Gunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = -orient.getYaw();
		float f1 = orient.getTangage();
		mesh.chunkSetAngles("TurretA", -40F, f, 0.0F);
		mesh.chunkSetAngles("TurretB", 0.0F, f1, 0.0F);
		mesh.chunkSetLocate("IBone", Cockpit.xyz, Cockpit.ypr);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f < -35F)
					f = -35F;
				if (f > 35F)
					f = 35F;
				if (f1 > 45F)
					f1 = 45F;
				if (f1 < -40F)
					f1 = -40F;
				orient.setYPR(f, f1, 0.0F);
				orient.wrap();
			}
	}

	protected void interpTick() {
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			fm.CT.WeaponControl[10] = bGunFire;
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
			fm.CT.WeaponControl[10] = bGunFire;
		}
	}

	public CockpitFI_156_Gunner() {
		super("3DO/Cockpit/FI_156/FI_156_Gunner.him", "bf109");
		bNeedSetUp = true;
		hook1 = null;
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("CF_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("CF_D0", true);
		super.doFocusLeave();
	}

	public void toggleDim() {
		cockpitDimControl = !cockpitDimControl;
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
	}

	private boolean bNeedSetUp;
	private Hook hook1;

	static {
		Property.set(CLASS.THIS(), "aiTuretNum", 0);
		Property.set(CLASS.THIS(), "weaponControlNum", 10);
		Property.set(CLASS.THIS(), "astatePilotIndx", 1);
	}
}
