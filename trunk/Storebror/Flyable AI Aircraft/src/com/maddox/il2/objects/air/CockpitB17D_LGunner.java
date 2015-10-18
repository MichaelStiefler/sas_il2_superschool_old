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
// Last Edited at: 2013/06/11

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitB17D_LGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("Tail1_D0", false);
			aircraft().hierMesh().chunkVisible("Turret6B_D0", false);
			aircraft().hierMesh().chunkVisible("Turret7B_D0", false);
			aircraft().hierMesh().chunkVisible("Pilot7_D0", false);
			aircraft().hierMesh().chunkVisible("Head7_D0", false);
			aircraft().hierMesh().chunkVisible("Pilot8_D0", false);
			aircraft().hierMesh().chunkVisible("Head8_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("Tail1_D0", true);
		aircraft().hierMesh().chunkVisible("Turret6B_D0", true);
		aircraft().hierMesh().chunkVisible("Turret7B_D0", true);
		aircraft().hierMesh().chunkVisible("Pilot7_D0", true);
		aircraft().hierMesh().chunkVisible("Head7_D0", true);
		aircraft().hierMesh().chunkVisible("Pilot8_D0", true);
		aircraft().hierMesh().chunkVisible("Head8_D0", true);
		super.doFocusLeave();
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("TurretLA", 0.0F, -orient.getYaw() + 32.0F, 0.0F);
		mesh.chunkSetAngles("TurretLB", -43F, orient.getTangage(), 0.0F);
		mesh.chunkSetAngles("TurretLC", 0.0F, -orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f < -50F)
					f = -50F;
				if (f > 40F)
					f = 40F;
				if (f1 > 32F)
					f1 = 32F;
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
					hook1 = new HookNamed(aircraft(), "_MGUN07");
				doHitMasterAircraft(aircraft(), hook1, "_MGUN07");
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

	public CockpitB17D_LGunner() {
		super("3DO/Cockpit/B-25J-LGun/LGunnerB17D.him", "he111_gunner");
		hook1 = null;
	}

	public void reflectWorldToInstruments(float f) {
		mesh.chunkSetAngles("TurretRA", 0.0F, aircraft().FM.turret[5].tu[0], 0.0F);
		mesh.chunkSetAngles("TurretRB", 0.0F, aircraft().FM.turret[5].tu[1], 0.0F);
		mesh.chunkSetAngles("TurretRC", 0.0F, aircraft().FM.turret[5].tu[1], 0.0F);
		mesh.chunkVisible("TurretRC", false);
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 4) != 0)
			mesh.chunkVisible("XGlassDamage1", true);
		if ((fm.AS.astateCockpitState & 8) != 0) {
			mesh.chunkVisible("XGlassDamage1", true);
			mesh.chunkVisible("XHullDamage1", true);
		}
		if ((fm.AS.astateCockpitState & 0x10) != 0) {
			mesh.chunkVisible("XGlassDamage2", true);
			mesh.chunkVisible("XHullDamage2", true);
		}
		if ((fm.AS.astateCockpitState & 0x20) != 0) {
			mesh.chunkVisible("XGlassDamage2", true);
			mesh.chunkVisible("XHullDamage3", true);
		}
	}

	private Hook hook1;

	static {
		Property.set(CockpitB17D_LGunner.class, "aiTuretNum", 5);
		Property.set(CockpitB17D_LGunner.class, "weaponControlNum", 15);
		Property.set(CockpitB17D_LGunner.class, "astatePilotIndx", 7);
	}
}