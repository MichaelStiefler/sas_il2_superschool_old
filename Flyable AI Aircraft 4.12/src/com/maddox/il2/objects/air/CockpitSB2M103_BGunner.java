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

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitSB2M103_BGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("Turret1A", 5F, -orient.getYaw(), 0.0F);
		mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage() + 15.0F, 0.0F);
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
				if (f1 > 0F)
					f1 = 0F;
				if (f1 < -65F)
					f1 = -65F;
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
					hook1 = new HookNamed(aircraft(), "_MGUN04");
				doHitMasterAircraft(aircraft(), hook1, "_MGUN04");
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

	public void reflectCockpitState() {
		if (fm.AS.astateCockpitState != 0) {
			mesh.chunkVisible("XGlassDamage1", true);
			mesh.chunkVisible("XGlassDamage2", true);
			mesh.chunkVisible("XHullDamage1", true);
			mesh.chunkVisible("XHullDamage2", true);
			mesh.chunkVisible("XHullDamage3", true);
		}
	}

	public CockpitSB2M103_BGunner() {
		super("3DO/Cockpit/DB3early-BGun/BGunnerSB2M103.him", "he111_gunner");
		hook1 = null;
	}

	private Hook hook1;

	static {
		Property.set(CockpitSB2M103_BGunner.class, "aiTuretNum", 2);
		Property.set(CockpitSB2M103_BGunner.class, "weaponControlNum", 12);
		Property.set(CockpitSB2M103_BGunner.class, "astatePilotIndx", 2);
	}
}
