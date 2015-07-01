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
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;

public class CockpitDB3B_BGunner extends CockpitGunner {

	public void setRealMode(boolean flag) {
		super.setRealMode(flag);
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			bEntered = true;
			saveFov = Main3D.FOVX;
			CmdEnv.top().exec("fov 31");
			Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
			HotKeyEnv.enable("PanView", false);
			HotKeyEnv.enable("SnapView", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		if (bEntered) {
			bEntered = false;
			Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
			CmdEnv.top().exec("fov " + saveFov);
			boolean flag = HotKeyEnv.isEnabled("aircraftView");
			HotKeyEnv.enable("PanView", flag);
			HotKeyEnv.enable("SnapView", flag);
		}
		super.doFocusLeave();
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("Turret1A", 0.0F, -orient.getYaw(), 0.0F);
		mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage() - 15.0F, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f < -30F)
					f = -30F;
				if (f > 30F)
					f = 30F;
				if (f1 > 27F)
					f1 = 27F;
				if (f1 < -23F)
					f1 = -23F;
				orient.setYPR(f, f1, 0.0F);
				orient.wrap();
			}
	}

	protected void interpTick() {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
			if (bGunFire) {
				if (hook1 == null)
					hook1 = new HookNamed(aircraft(), "_MGUN03");
				doHitMasterAircraft(aircraft(), hook1, "_MGUN03");
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

	public void reflectCockpitState() {
	}

	public CockpitDB3B_BGunner() {
		super("3DO/Cockpit/DB3early-BGun/BGunnerDB3B.him", "he111_gunner");
		// super("3DO/Cockpit/Il-4-BGun/hier.him", "he111_gunner");
		bNeedSetUp = true;
		bEntered = false;
		hook1 = null;
	}

	private boolean bNeedSetUp;
	private boolean bEntered;
	private float saveFov;
	private Hook hook1;

	static {
		Property.set(CockpitDB3B_BGunner.class, "aiTuretNum", 2);
		Property.set(CockpitDB3B_BGunner.class, "weaponControlNum", 12);
		Property.set(CockpitDB3B_BGunner.class, "astatePilotIndx", 3);
	}
}
