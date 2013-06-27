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
import com.maddox.il2.engine.hotkey.HookGunner;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class CockpitDB3F_FGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		mesh.chunkSetAngles("TurretA", 0.0F, f, 0.0F);
		mesh.chunkSetAngles("TurretB", 0.0F, f1, 0.0F);
		mesh.chunkSetAngles("Ret_Base", 0.0F, f1, 0.0F);
		mesh.chunkSetAngles("Reticle", 0.0F, f1, 0.0F);
		if (f > 25F)
			f = 25F;
		if (f < -25F)
			f = -25F;
		if (f1 < -22F)
			f1 = -22F;
		mesh.chunkSetAngles("Cam_A", 0.0F, f, 0.0F);
		mesh.chunkSetAngles("Cam_B", 0.0F, -f1, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode()) {
			float f = orient.getYaw();
			float f1 = orient.getTangage();
			if (f > 30F)
				f = 30F;
			if (f < -30F)
				f = -30F;
			if (f1 > 30F)
				f1 = 30F;
			if (f1 < -30F)
				f1 = -30F;
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

	public CockpitDB3F_FGunner() {
		super("3DO/Cockpit/Il-4-NGun/hier.him", "he111_gunner");
		hook1 = null;
	}

	protected boolean doFocusEnter() {
		boolean flag = super.doFocusEnter();
		HookGunner hookgunner = HookGunner.current();
		hookgunner.setLimits(90F, -50F, 80F);
		return flag;
	}

	public boolean focusEnter() {
		return super.focusEnter();
	}

	public void reflectWorldToInstruments(float f) {
	}

	public void reflectCockpitState() {
	}

	private Hook hook1;

	static {
		Property.set(CockpitDB3F_FGunner.class, "aiTuretNum", 0);
		Property.set(CockpitDB3F_FGunner.class, "weaponControlNum", 10);
		Property.set(CockpitDB3F_FGunner.class, "astatePilotIndx", 1);
		Property.set(CLASS.THIS(), "normZN", 1.2F);
	}
}
