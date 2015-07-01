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

import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitFW200_PGunner extends CockpitGunner {

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("z_Turret2A", 0.0F, orient.getYaw(), 0.0F);
		mesh.chunkSetAngles("z_Turret2B", 0.0F, orient.getTangage(), 0.0F);
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
		if (f1 > 80F)
			f1 = 80F;

		float f2 = Math.abs(f); // Make lower turret limit the same for both
								// left and right side.
		float f1Min = -10F; // Lower turret limit.
		float fDistCenter = -1F; // Distance from center position of lower
									// turret limit raise area.

		// Check if Turret is in front or rear fuselage direction, if so raise
		// lower turret limit.
		if (f2 > 135F)
			fDistCenter = 180F - f2;
		else if (f2 < 45F)
			fDistCenter = f2;
		if (fDistCenter > 0F) {
			f1Min += (float) Math.cos((float) Math.PI * fDistCenter / 90F) * 20F;
		}
		// Check if Turret is closing in on wing area from behind, if so raise
		// lower turret limit.
		else if (f2 > 45F && f2 < 72.5F) {
			fDistCenter = 72.5F - f2;
			f1Min += (float) Math.cos((float) Math.PI * fDistCenter / 55F) * 10F;
		}
		// Check if Turret is closing in on wing area from front, if so raise
		// lower turret limit.
		else if (f2 >= 72.5F && f2 < 85F) {
			fDistCenter = f2 - 72.5F;
			f1Min += (float) Math.cos((float) Math.PI * fDistCenter / 25F) * 10F;
		}

		if (f1 < f1Min)
			f1 = f1Min;

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
				hook1 = new HookNamed(aircraft(), "_MGUN01");
			doHitMasterAircraft(aircraft(), hook1, "_MGUN01");
			if (iCocking > 0)
				iCocking = 0;
			else
				iCocking = 1;
		} else {
			iCocking = 0;
		}
		if (emitter != null) {
			boolean flag = emitter.countBullets() % 2 == 0;
			mesh.chunkVisible("ZTurret2B-Bullet01", flag);
			mesh.chunkVisible("ZTurret2B-Bullet02", !flag);
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

	protected void reflectPlaneMats() {
	}

	public CockpitFW200_PGunner() {
		super("3DO/Cockpit/Do217k1/FW200PGun.him", "he111_gunner");
		w = new Vector3f();
		bNeedSetUp = true;
		hook1 = null;
		iCocking = 0;
	}

	public Vector3f w;
	private boolean bNeedSetUp;
	private Hook hook1;
	private int iCocking;

	static {
		Property.set(CockpitFW200_PGunner.class, "aiTuretNum", 0);
		Property.set(CockpitFW200_PGunner.class, "weaponControlNum", 10);
		Property.set(CockpitFW200_PGunner.class, "astatePilotIndx", 1);
	}
}
