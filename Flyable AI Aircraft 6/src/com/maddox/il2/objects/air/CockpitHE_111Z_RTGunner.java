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

public class CockpitHE_111Z_RTGunner extends CockpitGunner {

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
	}

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
				if (f < -42F)
					f = -42F;
				if (f > 42F)
					f = 42F;
				if (f1 > 60F)
					f1 = 60F;
				if (f1 < -3F)
					f1 = -3F;
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
					hook1 = new HookNamed(aircraft(), "_MGUN02");
				doHitMasterAircraft(aircraft(), hook1, "_MGUN02");
				if (iCocking > 0)
					iCocking = 0;
				else
					iCocking = 1;
			} else {
				iCocking = 0;
			}
			iNewVisDrums = (int) ((float) emitter.countBullets() / 333F);
			if (iNewVisDrums < iOldVisDrums) {
				iOldVisDrums = iNewVisDrums;
				mesh.chunkVisible("Drum1", iNewVisDrums > 2);
				mesh.chunkVisible("Drum2", iNewVisDrums > 1);
				mesh.chunkVisible("Drum3", iNewVisDrums > 0);
				sfxClick(13);
			}
			mesh.chunkSetAngles("CockingLever", -0.75F * (float) iCocking, 0.0F, 0.0F);
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

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
		mesh.materialReplace("Gloss1D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
		mesh.materialReplace("Gloss1D2o", mat);
	}

	public CockpitHE_111Z_RTGunner() {
		super("3DO/Cockpit/He-111H-2-TGun/RTGunnerHE111Z.him", "he111_gunner");
		bNeedSetUp = true;
		hook1 = null;
		iCocking = 0;
		iOldVisDrums = 3;
		iNewVisDrums = 3;
	}

	private boolean bNeedSetUp;
	private Hook hook1;
	private int iCocking;
	private int iOldVisDrums;
	private int iNewVisDrums;

	static {
		Property.set(CockpitHE_111Z_RTGunner.class, "aiTuretNum", 1);
		Property.set(CockpitHE_111Z_RTGunner.class, "weaponControlNum", 11);
		Property.set(CockpitHE_111Z_RTGunner.class, "astatePilotIndx", 7);
	}
}
