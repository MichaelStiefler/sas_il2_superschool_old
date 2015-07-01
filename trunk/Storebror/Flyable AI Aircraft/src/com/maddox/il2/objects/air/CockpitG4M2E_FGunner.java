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

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitG4M2E_FGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("z_Turret5A", 0.0F, orient.getYaw() - 85.0F, 0.0F);
		mesh.chunkSetAngles("z_Turret5B", 0.0F, orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (!isRealMode())
			return;
		if (!aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		if (f1 > 35F)
			f1 = 35F;
		if (f1 < -20F)
			f1 = -20F;
		if (f > 60F)
			f = 60F;
		if (f < -20F)
			f = -20F;
		orient.setYPR(f, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!isRealMode())
			return;
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
	}

	public void doGunFire(boolean flag) {
		if (!isRealMode())
			return;
		if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
			bGunFire = false;
		else
			bGunFire = flag;
		fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
	}

	public void reflectCockpitState() {
	}

	public CockpitG4M2E_FGunner() {
		super("3DO/Cockpit/Do217k1/FGunner_G4M2E.him", "he111_gunner");
		hook1 = null;
		iCocking = 0;
	}

	private Hook hook1;
	private int iCocking;

	static {
		Property.set(CockpitG4M2E_FGunner.class, "aiTuretNum", 1);
		Property.set(CockpitG4M2E_FGunner.class, "weaponControlNum", 11);
		Property.set(CockpitG4M2E_FGunner.class, "astatePilotIndx", 2);
	}
}
