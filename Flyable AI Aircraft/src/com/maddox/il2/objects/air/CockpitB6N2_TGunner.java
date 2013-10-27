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

public class CockpitB6N2_TGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("Turret1A", 0.0F, orient.getYaw(), 0.0F);
		mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f < -30F)
					f = -30F;
				if (f > 24F)
					f = 24F;
				if (f1 > 30F)
					f1 = 30F;
				if (f1 < -10F)
					f1 = -10F;
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
				if (hook2 == null)
					hook2 = new HookNamed(aircraft(), "_MGUN02");
				doHitMasterAircraft(aircraft(), hook2, "_MGUN02");
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

	public CockpitB6N2_TGunner() {
		super("3DO/Cockpit/D3A1-TGun/TGunnerB6N2.him", "he111_gunner");
		hook1 = null;
		bNeedSetUp = true;
		mesh.chunkVisible("Canopy", false);
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 1) != 0) {
			mesh.chunkVisible("XGlassDamage1", true);
			mesh.chunkVisible("XGlassDamage2", true);
		}
		if ((fm.AS.astateCockpitState & 4) != 0)
			mesh.chunkVisible("XHullDamage1", true);
		if ((fm.AS.astateCockpitState & 0x10) != 0)
			mesh.chunkVisible("XHullDamage2", true);
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
	}

	private Hook hook1;
	private Hook hook2;
	private boolean bNeedSetUp;

	static {
		Property.set(CLASS.THIS(), "aiTuretNum", 0);
		Property.set(CLASS.THIS(), "weaponControlNum", 10);
		Property.set(CLASS.THIS(), "astatePilotIndx", 1);
	}
}
