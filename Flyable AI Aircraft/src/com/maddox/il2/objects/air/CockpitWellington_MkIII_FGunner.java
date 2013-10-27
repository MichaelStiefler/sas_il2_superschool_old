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

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitWellington_MkIII_FGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("CF_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("CF_D0", true);
		super.doFocusLeave();
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = orient.getYaw();
		float f1 = orient.getTangage();

		// TODO: Make sure turret is not in center position, otherwise screen
		// will be black (turret glitch)
		if (Math.abs(f) < 2.2F) {
			if (f1 > -2.0F && f1 < 0.0F)
				f1 = -2.0F;
			else if (f1 < 2.3F && f1 >= 0.0F)
				f1 = 2.3F;
		}
		mesh.chunkSetAngles("Body", 180F, 0.0F, 0.0F);
		mesh.chunkSetAngles("TurretA", 0.0F, -f, 0.0F);
		mesh.chunkSetAngles("TurretB", 0.0F, f1, 0.0F);
		mesh.chunkSetAngles("TurretC", 0.0F, cvt(f, -38F, 38F, -15F, 15F), 0.0F);
		mesh.chunkSetAngles("TurretE", -f, 0.0F, 0.0F);
		mesh.chunkSetAngles("TurretF", 0.0F, f1, 0.0F);
		mesh.chunkSetAngles("TurretG", -cvt(f, -33F, 33F, -33F, 33F), 0.0F, 0.0F);
		mesh.chunkSetAngles("TurretH", 0.0F, cvt(f1, -10F, 32F, -10F, 32F), 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[0] = cvt(Math.max(Math.abs(f), Math.abs(f1)), 0.0F, 20F, 0.0F, 0.3F);
		mesh.chunkSetLocate("TurretI", Cockpit.xyz, Cockpit.ypr);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f < -38F)
					f = -38F;
				if (f > 38F)
					f = 38F;
				if (f1 > 38F)
					f1 = 38F;
				if (f1 < -41F)
					f1 = -41F;
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
				if (hook2 == null)
					hook2 = new HookNamed(aircraft(), "_MGUN02");
				doHitMasterAircraft(aircraft(), hook2, "_MGUN02");
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

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
	}

	public CockpitWellington_MkIII_FGunner() {
		super("3DO/Cockpit/BoultonPaulWellington/WellingtonMkIIIFGunner.him", "bf109");
		hook1 = null;
		hook2 = null;
	}

	private Hook hook1;
	private Hook hook2;

	static {
		Property.set(CockpitWellington_MkIII_FGunner.class, "aiTuretNum", 0);
		Property.set(CockpitWellington_MkIII_FGunner.class, "weaponControlNum", 10);
		Property.set(CockpitWellington_MkIII_FGunner.class, "astatePilotIndx", 2);
	}
}
