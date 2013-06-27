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
// Last Edited at: 2013/01/23

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.HUD;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class CockpitR10_TGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("TurrelA", 0.0F, orient.getYaw(), 0.0F);
		mesh.chunkSetAngles("TurrelB", 0.0F, orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();

				if (f < -138.0F)
					f = -138.0F;
				else if (f > 150.0F)
					f = 150.0F;
				if (f1 < -35.0F)
					f1 = -35.0F;
				else if (f1 > 80.0F)
					f1 = 80.0F;
				float f2 = Math.abs(f);
				if (f2 < 18.0F) {
					float f1Min = 50.0F - (57.0F / 18.0F) * f2;
					if (f1 < f1Min)
						f1 = f1Min;
				} else if (f2 < 37.0F && f1 < -7.0F)
					f1 = -7.0F;
				else if (f2 > 98.0F && f1 < -7.0F)
					f1 = -7.0F;

				// if(f < -90F)
				// f = -90F;
				// else if(f > 90F)
				// f = 90F;
				// else if(f1 > 80F)
				// f1 = 80F;
				// if(f1 < -15F)
				// f1 = -15F;

				HUD.training("f=" + f + " f1=" + f1);

				// float f2 = Math.abs(f);
				// if(f2 < 2.F && f1 < 45.0F)
				// f1 = 45.0F;
				// else if(f2 < 5.0F && f1 < 32F)
				// f1 = 16.1F;
				// else if(f2 < 20.0F && f1 < 10.0F)
				// f1 = -8.5F;
				// else if(f2 < 40.0F && f1 < -5.0F)
				// f1 = -45F;
				// else if(f2 < 60.0F && f1 < 0.0F)
				// f1 = -7.8F;

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
					hook1 = new HookNamed(aircraft(), "_MGUN03");
				doHitMasterAircraft(aircraft(), hook1, "_MGUN03");
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

	protected void reflectPlaneMats() {
	}

	public CockpitR10_TGunner() {
		super("3DO/Cockpit/Il-10-TGun/TGunnerR10.him", "bf109");
		hook1 = null;
	}

	public void reflectWorldToInstruments(float f) {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
		mesh.materialReplace("Matt1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
		mesh.materialReplace("Matt2D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
		mesh.materialReplace("Gloss2D0o", mat);
	}

	private Hook hook1;

	static {
		Property.set(CLASS.THIS(), "aiTuretNum", 0);
		Property.set(CLASS.THIS(), "weaponControlNum", 10);
		Property.set(CLASS.THIS(), "astatePilotIndx", 1);
	}
}
