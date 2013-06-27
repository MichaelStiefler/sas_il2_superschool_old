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
// Last Edited at: 2013/02/05

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_111Z_RNGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = -orient.getYaw();
		float f1 = orient.getTangage();
		mesh.chunkSetAngles("TurretA", 19F, f, 0.0F);
		mesh.chunkSetAngles("TurretB", 0.0F, f1, 0.0F);
		if (f > 15F)
			f = 15F;
		if (f1 < -21F)
			f1 = -21F;
		mesh.chunkSetAngles("CameraRodA", 0.0F, f, 0.0F);
		mesh.chunkSetAngles("CameraRodB", 0.0F, f1, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f < -25F)
					f = -25F;
				if (f > 25F)
					f = 25F;
				if (f1 > 30F)
					f1 = 30F;
				if (f1 < -20F)
					f1 = -20F;
				orient.setYPR(f, f1, 0.0F);
				orient.wrap();
			}
	}

	protected void interpTick() {
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
			if (bGunFire)
				mesh.chunkSetAngles("Butona", 0.15F, 0.0F, 0.0F);
			else
				mesh.chunkSetAngles("Butona", 0.0F, 0.0F, 0.0F);
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

	public CockpitHE_111Z_RNGunner() {
		super("3DO/Cockpit/He-111H-6-NGun/RNGunnerHE111Z.him", "he111_gunner");
	}

	public void reflectWorldToInstruments(float f) {
		mesh.chunkSetAngles("zColumn1", 0.0F, 0.0F, -10F * fm.CT.ElevatorControl);
		mesh.chunkSetAngles("zColumn2", 0.0F, 0.0F, -40F * fm.CT.AileronControl);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 4) != 0)
			mesh.chunkVisible("ZHolesL_D1", true);
		if ((fm.AS.astateCockpitState & 8) != 0)
			mesh.chunkVisible("ZHolesL_D2", true);
		if ((fm.AS.astateCockpitState & 0x10) != 0)
			mesh.chunkVisible("ZHolesR_D1", true);
		if ((fm.AS.astateCockpitState & 0x20) != 0)
			mesh.chunkVisible("ZHolesR_D2", true);
		if ((fm.AS.astateCockpitState & 1) != 0)
			mesh.chunkVisible("ZHolesF_D1", true);
		if ((fm.AS.astateCockpitState & 0x80) != 0)
			mesh.chunkVisible("zOil_D1", true);
	}

	static {
		Property.set(CockpitHE_111Z_RNGunner.class, "aiTuretNum", 0);
		Property.set(CockpitHE_111Z_RNGunner.class, "weaponControlNum", 10);
		Property.set(CockpitHE_111Z_RNGunner.class, "astatePilotIndx", 6);
	}
}
