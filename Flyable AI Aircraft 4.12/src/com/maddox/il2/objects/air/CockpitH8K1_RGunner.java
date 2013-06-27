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
// Last Edited at: 2013/02/02

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitH8K1_RGunner extends CockpitGunner {
	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("Turret4A", 0.0F, orient.getYaw() - 90.0F, 0.0F);
		mesh.chunkSetAngles("Turret4B", 0.0F, orient.getTangage(), 0.0F);
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
		if (f < -30F)
			f = -30F;
		if (f > 60F)
			f = 60F;
		if (f1 < -55F)
			f1 = -55F;
		if (f1 > 35F)
			f1 = 35F;
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
			if (iCocking > 0)
				iCocking = 0;
			else
				iCocking = 1;
		} else {
			iCocking = 0;
		}
		resetYPRmodifier();
		xyz[1] = -0.07F * (float) iCocking;
		mesh.chunkSetLocate("Turret4C", xyz, ypr);
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

	public CockpitH8K1_RGunner() {
		super("3DO/Cockpit/G4M1-11-RGun/RGunnerH8K1.him", "he111_gunner");
		curMat = null;
		iCocking = 0;
	}

	public void reflectCockpitState() {
	}

	Mat curMat;
	Mat newMat;
	private int iCocking;

	static {
		Property.set(CockpitH8K1_RGunner.class, "aiTuretNum", 2);
		Property.set(CockpitH8K1_RGunner.class, "weaponControlNum", 12);
		Property.set(CockpitH8K1_RGunner.class, "astatePilotIndx", 6);
	}
}
