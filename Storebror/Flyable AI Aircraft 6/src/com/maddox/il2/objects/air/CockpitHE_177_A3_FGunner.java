package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_177_A3_FGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (!super.doFocusEnter())
			return false;
		((HE_177A3) fm.actor).bPitUnfocused = false;
		aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
		aircraft().hierMesh().chunkVisible("Hmask3_D0", false);
		aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
		aircraft().hierMesh().chunkVisible("Turret2A_D0", false);
		aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
		this.isOppositeSideAiControlled = aircraft().FM.turret[2].bIsAIControlled;
		aircraft().FM.turret[2].bIsAIControlled = true;
		aircraft().FM.turret[1].bIsAIControlled = this.onAuto;
		((HE_177A3) aircraft()).setVentralGunnerDirection(HE_177A3.VENTRAL_GUNNER_FORWARD);
		return true;
	}

	protected void doFocusLeave() {
		if (!isFocused())
			return;
		((HE_177A3) fm.actor).bPitUnfocused = true;
		if (!this.fm.AS.isPilotParatrooper(2)) {
			aircraft().hierMesh().chunkVisible("Pilot3_D0", !this.fm.AS.isPilotDead(2));
			aircraft().hierMesh().chunkVisible("Pilot3_D1", this.fm.AS.isPilotDead(2));
		}
		aircraft().hierMesh().chunkVisible("Turret2A_D0", true);
		aircraft().hierMesh().chunkVisible("Turret2B_D0", true);
		this.onAuto = aircraft().FM.turret[1].bIsAIControlled;
		aircraft().FM.turret[2].bIsOperable = aircraft().FM.turret[2].health > 0.0F;
		aircraft().FM.turret[2].bIsAIControlled = this.isOppositeSideAiControlled;
		super.doFocusLeave();
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("z_Turret3A", -orient.getYaw(), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("z_Turret3B", 0.0F, orient.getTangage() /* + 90.0F */, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (!isRealMode())
			return;
		if (!aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage() + GUN_OFFSET_TANGAGE;
		if (f1 > 7F)
			f1 = 7F;
		if (f1 < -40F)
			f1 = -40F;
		if (f > 15F)
			f = 15F;
		if (f < -15F)
			f = -15F;
		orient.setYPR(f, f1 - GUN_OFFSET_TANGAGE, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!isRealMode())
			return;
		if (this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
			this.bGunFire = false;
		this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
		if (this.bGunFire) {
			if (this.hook1 == null)
				this.hook1 = new HookNamed(aircraft(), "_MGUN02");
			doHitMasterAircraft(aircraft(), this.hook1, "_MGUN02");
		}
	}

	public void doGunFire(boolean flag) {
		if (!isRealMode())
			return;
		if (this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
			this.bGunFire = false;
		else
			this.bGunFire = flag;
		this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
	}

	public CockpitHE_177_A3_FGunner() {
		super("3DO/Cockpit/He-177A-3/FGunner.him", "he111_gunner");
		this.hook1 = null;
		this.onAuto = true;
	}

	private Hook hook1;
	private boolean onAuto;
	private boolean isOppositeSideAiControlled;
	protected static final float GUN_OFFSET_TANGAGE = -16.5F;

	static {
		Class class1 = CockpitHE_177_A3_FGunner.class;
		Property.set(class1, "aiTuretNum", 1);
		Property.set(class1, "weaponControlNum", 11);
		Property.set(class1, "astatePilotIndx", 2);
	}
}
