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
// Last Edited at: 2013/01/22

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class CockpitKI_21_II_TGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = -orient.getYaw();
		float f1 = orient.getTangage();
		mesh.chunkSetAngles("Turret3A", -16F, -f, 0.0F);
		mesh.chunkSetAngles("Turret3B", 0.0F, f1, 0.0F);
		mesh.chunkSetAngles("Turret3D", 0.0F, -f, 0.0F);
		mesh.chunkSetAngles("Turret3E", 0.0F, f1, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f < -40F)
					f = -40F;
				if (f > 40F)
					f = 40F;
				if (f1 > 35F)
					f1 = 35F;
				if (f1 < 0.0F)
					f1 = 0.0F;
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
				if (iCocking > 0)
					iCocking = 0;
				else
					iCocking = 1;
			} else {
				iCocking = 0;
			}
			resetYPRmodifier();
			Cockpit.xyz[1] = -0.07F * (float) iCocking;
			Cockpit.ypr[1] = 0.0F;
			mesh.chunkSetLocate("Turret3C", Cockpit.xyz, Cockpit.ypr);
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

	public CockpitKI_21_II_TGunner() {
		super("3DO/Cockpit/G4M1-11-TGun/TGunnerKI21II.him", "he111_gunner");
		bNeedSetUp = true;
		iCocking = 0;
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

	public void reflectCockpitState() {
	}

	private boolean bNeedSetUp;
	private int iCocking;

	static {
		Property.set(CLASS.THIS(), "aiTuretNum", 1);
		Property.set(CLASS.THIS(), "weaponControlNum", 11);
		Property.set(CLASS.THIS(), "astatePilotIndx", 3);
	}
}
