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
// Last Edited at: 2013/01/31

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitFW200_RGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("TurretRA", 0.0F, -orient.getYaw() - 27.9F, 0.0F);
		mesh.chunkSetAngles("TurretRB", 0.0F, orient.getTangage(), 0.0F);
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
		if (f < -90F)
			f = -90F;
		if (f > 35F)
			f = 35F;
		if (f1 > 30F)
			f1 = 30F;
		if (f1 < -40F)
			f1 = -40F;
		if (f1 < -55F + 0.5F * f)
			f1 = -55F + 0.5F * f;
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
				hook1 = new HookNamed(aircraft(), "_MGUN05");
			doHitMasterAircraft(aircraft(), hook1, "_MGUN05");
			if (iCocking > 0)
				iCocking = 0;
			else
				iCocking = 1;
			iNewVisDrums = (int) ((float) emitter.countBullets() / 250F);
			if (iNewVisDrums < iOldVisDrums) {
				iOldVisDrums = iNewVisDrums;
				mesh.chunkVisible("DrumR1", iNewVisDrums > 1);
				mesh.chunkVisible("DrumR2", iNewVisDrums > 0);
				sfxClick(13);
			}
		} else {
			iCocking = 0;
		}
		resetYPRmodifier();
		xyz[0] = -0.07F * (float) iCocking;
		mesh.chunkSetLocate("LeverR", xyz, ypr);
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

	public CockpitFW200_RGunner() {
		super("3DO/Cockpit/He-111H-2-RGun/RGunnerFW200.him", "he111_gunner");
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
		Property.set(CockpitFW200_RGunner.class, "aiTuretNum", 4);
		Property.set(CockpitFW200_RGunner.class, "weaponControlNum", 14);
		Property.set(CockpitFW200_RGunner.class, "astatePilotIndx", 5);
	}
}
