package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitAH1_TGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		super.mesh.chunkSetAngles("Body", 0.0F, 0.0F, -1F);
		super.mesh.chunkSetAngles("Turret1A", -orient.getYaw(), 0.0F, 0.0F);
		super.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode()) if (!aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
		} else {
			float f = orient.getYaw();
			float f1 = orient.getTangage();
			if (f < -60F) f = -60F;
			if (f > 60F) f = 60F;
			if (f1 > 2.0F) f1 = 2.0F;
			if (f1 < -85F) f1 = -85F;
			orient.setYPR(f, f1, 0.0F);
			orient.wrap();
		}
	}

	protected void interpTick() {
		if (isRealMode()) {
			if (super.emitter == null || !super.emitter.haveBullets() || !aiTurret().bIsOperable) super.bGunFire = false;
			this.fm.CT.WeaponControl[weaponControlNum()] = super.bGunFire;
			if (super.bGunFire) {
				if (hook1 == null) hook1 = new HookNamed(aircraft(), "_MGUN01");
				doHitMasterAircraft(aircraft(), hook1, "_MGUN01");
			}
		}
	}

	public void doGunFire(boolean flag) {
		if (isRealMode()) {
			if (super.emitter == null || !super.emitter.haveBullets() || !aiTurret().bIsOperable) super.bGunFire = false;
			else super.bGunFire = flag;
			this.fm.CT.WeaponControl[weaponControlNum()] = super.bGunFire;
		}
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 4) != 0) super.mesh.chunkVisible("Z_Holes1_D1", true);
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) super.mesh.chunkVisible("Z_Holes2_D1", true);
	}

	public CockpitAH1_TGunner() {
		super("3DO/Cockpit/A-20G-TGun/TGunnerAH1.him", "he111_gunner");
		hook1 = null;
	}

	private Hook hook1;

	static {
		Class class1 = CockpitAH1_TGunner.class;
		Property.set(class1, "aiTuretNum", 0);
		Property.set(class1, "weaponControlNum", 10);
		Property.set(class1, "astatePilotIndx", 1);
	}
}
