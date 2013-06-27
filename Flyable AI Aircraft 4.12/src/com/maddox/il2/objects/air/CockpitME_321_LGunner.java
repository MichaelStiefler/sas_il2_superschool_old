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

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitME_321_LGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("TurretLA", 0.0F, -orient.getYaw(), 0.0F);
		mesh.chunkSetAngles("TurretLB", 0.0F, orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f < -55F)
					f = -55F;
				if (f > 40F)
					f = 40F;
				if (f1 > 30F)
					f1 = 30F;
				if (f1 < -40F)
					f1 = -40F;
				if (f1 < -55F - 0.5F * f)
					f1 = -55F - 0.5F * f;
				orient.setYPR(f, f1, 0.0F);
				orient.wrap();
			}
	}

	protected void interpTick() {
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
			if (bGunFire) {
				if (hook1 == null)
					hook1 = new HookNamed(aircraft(), "_MGUN03");
				doHitMasterAircraft(aircraft(), hook1, "_MGUN03");
				if (iCocking > 0)
					iCocking = 0;
				else
					iCocking = 1;
				iNewVisDrums = (int) ((float) emitter.countBullets() / 250F);
				if (iNewVisDrums < iOldVisDrums) {
					iOldVisDrums = iNewVisDrums;
					mesh.chunkVisible("DrumL1", iNewVisDrums > 1);
					mesh.chunkVisible("DrumL2", iNewVisDrums > 0);
					sfxClick(13);
				}
			} else {
				iCocking = 0;
			}
			resetYPRmodifier();
			Cockpit.xyz[0] = -0.07F * (float) iCocking;
			mesh.chunkSetLocate("LeverL", Cockpit.xyz, Cockpit.ypr);
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

	public CockpitME_321_LGunner() {
		super("3DO/Cockpit/He-111H-2-LGun/LGunnerME321.him", "he111_gunner");
		hook1 = null;
		iCocking = 0;
		iOldVisDrums = 2;
		iNewVisDrums = 2;
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl) {
			mesh.chunkVisible("Flare", true);
			setNightMats(true);
		} else {
			mesh.chunkVisible("Flare", false);
			setNightMats(false);
		}
	}

	public void reflectCockpitState() {
		if (fm.AS.astateCockpitState != 0)
			mesh.chunkVisible("Holes_D1", true);
	}

	private Hook hook1;
	private int iCocking;
	private int iOldVisDrums;
	private int iNewVisDrums;

	static {
		Property.set(CockpitME_321_LGunner.class, "aiTuretNum", 2);
		Property.set(CockpitME_321_LGunner.class, "weaponControlNum", 12);
		Property.set(CockpitME_321_LGunner.class, "astatePilotIndx", 3);
	}
}
