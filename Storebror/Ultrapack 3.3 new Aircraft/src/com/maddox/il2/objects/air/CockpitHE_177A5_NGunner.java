package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_177A5_NGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (!super.doFocusEnter())
			return false;
//		if (CommonUtils.is411orLater()) {
			if (doToKGCheck) {
				doToKGCheck = false;
				if ((aircraft() instanceof HE_177A5) && ((HE_177A5) aircraft()).hasToKG) {
					hasToKG = true;
					showToKG();
				}
			}
//		}

		((HE_177A5) fm.actor).bPitUnfocused = false;
		aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
		aircraft().hierMesh().chunkVisible("Head1_D0", false);
		aircraft().hierMesh().chunkVisible("Hmask1_D0", false);
		aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
		aircraft().hierMesh().chunkVisible("Hmask2_D0", false);
		aircraft().hierMesh().chunkVisible("Pilot1_D1", false);
		aircraft().hierMesh().chunkVisible("Pilot2_D1", false);
		aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
		return true;
	}

	protected void doFocusLeave() {
		if (!isFocused())
			return;
		((HE_177A5) fm.actor).bPitUnfocused = true;
		if (!this.fm.AS.isPilotParatrooper(0)) {
			aircraft().hierMesh().chunkVisible("Pilot1_D0", !this.fm.AS.isPilotDead(0));
			aircraft().hierMesh().chunkVisible("Head1_D0", !this.fm.AS.isPilotDead(0));
			aircraft().hierMesh().chunkVisible("Pilot1_D1", this.fm.AS.isPilotDead(0));
		}
		if (!this.fm.AS.isPilotParatrooper(1)) {
			aircraft().hierMesh().chunkVisible("Pilot2_D0", !this.fm.AS.isPilotDead(1));
			aircraft().hierMesh().chunkVisible("Pilot2_D1", this.fm.AS.isPilotDead(1));
		}
		aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
		super.doFocusLeave();
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("z_Turret1A", 0.0F, orient.getYaw(), 0.0F);
		this.mesh.chunkSetAngles("z_Turret1B", 0.0F, orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (!isRealMode())
			return;
		if (!aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw() + GUN_OFFSET_YAW;
		float f1 = orient.getTangage();
		if (f1 > 40F)
			f1 = 40F;
		if (f1 < -40F)
			f1 = -40F;
		if (f > 60F)
			f = 60F;
		if (f < -20F)
			f = -20F;
		orient.setYPR(f - GUN_OFFSET_YAW, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!isRealMode())
			return;
		if (this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
			this.bGunFire = false;
		this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
		if (this.bGunFire) {
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

	public void reflectWorldToInstruments(float f) {
//		if (CommonUtils.is411orLater()) {
			if (hasToKG) {
				mesh.chunkSetAngles("Ship", 0.0F, -((HE_177A5) aircraft()).fAOB, 0.0F);
				mesh.chunkSetAngles("speedDial", 0.0F, 6.4F * ((HE_177A5) aircraft()).fShipSpeed, 0.0F);
				mesh.chunkSetAngles("Z_Angle", 0.0F, ((HE_177A5) aircraft()).FM.AS.getGyroAngle(), 0.0F);
			}
//		}
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 4) != 0) {
			this.mesh.chunkVisible("XGlassHoles1", true);
			this.mesh.chunkVisible("XGlassHoles3", true);
		}
		if ((this.fm.AS.astateCockpitState & 8) != 0) {
			this.mesh.chunkVisible("XGlassHoles7", true);
			this.mesh.chunkVisible("XGlassHoles4", true);
			this.mesh.chunkVisible("XGlassHoles2", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
			this.mesh.chunkVisible("XGlassHoles5", true);
			this.mesh.chunkVisible("XGlassHoles3", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x20) != 0)
			this.mesh.chunkVisible("XGlassHoles1", true);
		this.mesh.chunkVisible("XGlassHoles6", true);
		this.mesh.chunkVisible("XGlassHoles4", true);
		if ((this.fm.AS.astateCockpitState & 1) != 0)
			this.mesh.chunkVisible("XGlassHoles6", true);
		if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
			this.mesh.chunkVisible("XGlassHoles7", true);
			this.mesh.chunkVisible("XGlassHoles2", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
			this.mesh.chunkVisible("XGlassHoles5", true);
			this.mesh.chunkVisible("XGlassHoles2", true);
		}
	}

	private void showToKG() {
		System.out.println("CockpitHE_177_NGunner showToKG()");
		mesh.chunkVisible("Lofte", false);
		mesh.chunkVisible("TorpedoBoxNew", true);
		mesh.chunkVisible("speedDial", true);
		mesh.chunkVisible("angleDial", true);
		mesh.chunkVisible("Ship", true);
		mesh.chunkVisible("Z_Angle", true);
	}

	public CockpitHE_177A5_NGunner() {
		super("3DO/Cockpit/He-177A-5/NGunner.him", "he111_gunner");
		doToKGCheck = true;
		hasToKG = false;
		hook1 = null;
		iCocking = 0;
	}

	private Hook hook1;
	private int iCocking;
	private boolean doToKGCheck;
	private boolean hasToKG;
	protected static final float GUN_OFFSET_YAW = 20.0F;

	static {
		Class class1 = CockpitHE_177A5_NGunner.class;
		Property.set(class1, "aiTuretNum", 0);
		Property.set(class1, "weaponControlNum", 10);
		Property.set(class1, "astatePilotIndx", 1);
	}
}
