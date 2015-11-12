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

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitLetovS_328_Gunner extends CockpitGunner {

	Loc l;
	private Hook hook1;
	private Hook hook2;

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("Turret1A_D0X", false);
			aircraft().hierMesh().chunkVisible("Turret1B_D0X", false);
			((LetovS_328) aircraft()).gunOutOverride = this.isRealMode() ? 1 : 0;
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("Turret1A_D0X", true);
		aircraft().hierMesh().chunkVisible("Turret1B_D0X", true);
		((LetovS_328) aircraft()).gunOutOverride = this.isRealMode() ? 1 : 0;
		super.doFocusLeave();
	}

	public void moveGun(Orient orient) {
		// Dont't use super.moveGun(orient); here, instead use Letov's own gun
		// movement methods:
		float[] af = new float[3];
		orient.get(af);
		af[0] = -af[0];
		af[1] = -af[1];
		aircraft().turretAngles(0, af);
		float[] af2 = new float[3];
		orient.get(af2);
		float[] af3 = (float[]) ((LetovS_328) aircraft()).turretPos.clone();
		float fTemp = af3[1];
		af3[1] = -af3[0];
		af3[0] = -fTemp;
		af3[2] = 0F;
		mesh.chunkSetLocate("Turret3A", af3, af2);
		// ^^ calling Letov gun movement method, proceed as usual from here on.
		float f = -orient.getYaw();
		float f1 = -orient.getTangage();
		mesh.chunkSetAngles("Turret3B", 0.0F, -f1, 0.0F);
		mesh.chunkSetAngles("Turret3C", 0.0F, f, 0.0F);
		float f2 = 0.01905F * (float) Math.sin(Math.toRadians(f));
		float f3 = 0.01905F * (float) Math.cos(Math.toRadians(f));
		f = (float) Math.toDegrees(Math.atan(f2 / (f3 + 0.3565F)));
		mesh.chunkSetAngles("Turret3D", 0.0F, -f, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f1 < -15F)
					f1 = -15F;
				if (f1 > 45F)
					f1 = 45F;
				if (f < -135F)
					f = -135F;
				if (f > 135F)
					f = 135F;
				orient.setYPR(f, f1, 0.0F);
				orient.wrap();
			}
	}

	protected void interpTick() {
		if (isRealMode()) {
			((LetovS_328) aircraft()).gunOutOverride = this.isRealMode() ? 1 : 0;
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
			if (bGunFire) {
				if (hook1 == null)
					hook1 = new HookNamed(aircraft(), "_MGUN03");
				doHitMasterAircraft(aircraft(), hook1, "_MGUN03");
				if (hook2 == null)
					hook2 = new HookNamed(aircraft(), "_MGUN04");
				doHitMasterAircraft(aircraft(), hook2, "_MGUN04");
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

	public CockpitLetovS_328_Gunner() {
		super("3DO/Cockpit/TB-3-RGun/GunnerS328II.him", "i16");
		l = new Loc();
		hook1 = null;
		hook2 = null;
	}

	static {
		Property.set(CockpitLetovS_328_Gunner.class, "aiTuretNum", 0);
		Property.set(CockpitLetovS_328_Gunner.class, "weaponControlNum", 10);
		Property.set(CockpitLetovS_328_Gunner.class, "astatePilotIndx", 1);
	}
}