// This file is part of the SAS IL-2 Sturmovik 1946 4.13
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
// Last Edited on: 2014/10/28

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.rts.Property;

public class CockpitCantZ506B_LGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(false);
			((CantZ506B) fm.actor).bPitUnfocused = false;
			aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		if (isFocused()) {
			aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
			((CantZ506B) fm.actor).bPitUnfocused = true;
			super.doFocusLeave();
		}
	}

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
		reflectPlaneToModel();
	}

	public void moveGun(Orient orient) {
		Orient aircraftGunOrient = new Orient();
		aircraftGunOrient.set(orient.getYaw(), orient.getPitch(), orient.getRoll());
		super.moveGun(aircraftGunOrient);
		// aircraft().hierMesh().chunkSetAngles("Turret3A_D0", 0.0F,
		// orient.getYaw(), 0.0F);
		// aircraft().hierMesh().chunkSetAngles("Turret3B_D0", 0.0F,
		// orient.getTangage(), 0.0F);
		mesh.chunkSetAngles("Zturret3A", 0.0F, orient.getYaw(), 0.0F);
		mesh.chunkSetAngles("Zturret3B", 0.0F, orient.getTangage(), 0.0F);
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
		if (f < -60F)
			f = -60F;
		if (f > 45F)
			f = 45F;
		if (f1 > 35F)
			f1 = 35F;
		if (f1 < -35F)
			f1 = -35F;
		orient.setYPR(f, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!isRealMode())
			return;
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
		} else {
			iCocking = 0;
		}
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

	public CockpitCantZ506B_LGunner() {
		super("3DO/Cockpit/SM79Bombardier/CantZ506_LGun.him", "he111_gunner");
		hook1 = null;
		iCocking = 0;
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Glass2"));
		mesh.materialReplace("Glass2", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
		mesh.materialReplace("Gloss2D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
		mesh.materialReplace("Matt1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Pilot1"));
		mesh.materialReplace("Pilot1", mat);
	}

	protected void reflectPlaneToModel() {
	}

	private boolean bNeedSetUp;
	private Hook hook1;
	private int iCocking;

	static {
		Property.set(CockpitCantZ506B_LGunner.class, "aiTuretNum", 2);
		Property.set(CockpitCantZ506B_LGunner.class, "weaponControlNum", 12);
		Property.set(CockpitCantZ506B_LGunner.class, "astatePilotIndx", 4);
	}
}
