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
// Last Edited at: 2013/01/22

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class CockpitDB3T_FGunner extends CockpitGunner {

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
		mesh.chunkSetAngles("Z_Need_Alt_Small", cvt(fm.getAltitude(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
		mesh.chunkSetAngles("Z_Need_Alt_Big", cvt(fm.getAltitude(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = -orient.getYaw();
		float f1 = orient.getTangage();
		mesh.chunkSetAngles("Body1", f, 0.0F, 0.0F);
		mesh.chunkSetAngles("Gun", 0.0F, 0.0F, f1);
		float f2 = 0.01905F * (float) Math.sin(Math.toRadians(f));
		float f3 = 0.01905F * (float) Math.cos(Math.toRadians(f));
		f = (float) Math.toDegrees(Math.atan(f2 / (f3 + 0.3565F)));
		mesh.chunkSetAngles("camerarod", 0.0F, 0.0F, f1);
		if (f1 > 0.0F) {
			mesh.chunkSetAngles("Extract4", 0.0F, 0.0F, cvt(f1, 0.0F, 46F, 0.0F, 6F));
			resetYPRmodifier();
			Cockpit.xyz[1] = f1 * 0.0018F;
			mesh.chunkSetLocate("Extract3", Cockpit.xyz, Cockpit.ypr);
			mesh.chunkSetLocate("Extract2", Cockpit.xyz, Cockpit.ypr);
			mesh.chunkSetLocate("Extract1", Cockpit.xyz, Cockpit.ypr);
		} else {
			mesh.chunkSetAngles("Extract4", 0.0F, 0.0F, cvt(f1, -53F, 0.0F, 15F, 0.0F));
			resetYPRmodifier();
			Cockpit.xyz[1] = f1 * 0.0018F;
			mesh.chunkSetLocate("Extract3", Cockpit.xyz, Cockpit.ypr);
			mesh.chunkSetLocate("Extract2", Cockpit.xyz, Cockpit.ypr);
			mesh.chunkSetLocate("Extract1", Cockpit.xyz, Cockpit.ypr);
		}
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
		if (f1 < -53F)
			f1 = -53F;
		if (f1 > 46F)
			f1 = 46F;
		if (f > cvt(f1, 0.0F, 46F, 75F, 60F))
			f = cvt(f1, 0.0F, 46F, 75F, 60F);
		if (f < -75F)
			f = -75F;
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
				hook1 = new HookNamed(aircraft(), "_MGUN01");
			doHitMasterAircraft(aircraft(), hook1, "_MGUN01");
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

	public CockpitDB3T_FGunner() {
		super("3DO/Cockpit/Pe-8_NGUN/DB3T_hier.him", "i16");
		bNeedSetUp = true;
		hook1 = null;
		BulletEmitter abulletemitter[] = aircraft().FM.CT.Weapons[weaponControlNum()];
		if (abulletemitter != null) {
			for (int i = 0; i < abulletemitter.length; i++)
				if (abulletemitter[i] instanceof Actor)
					((Actor) abulletemitter[i]).visibilityAsBase(false);

		}
		cockpitNightMats = (new String[] { "Turretparts3", "KPAT" });
		setNightMats(false);
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl)
			setNightMats(true);
		else
			setNightMats(false);
	}

	private boolean bNeedSetUp;
	private Hook hook1;

	static {
		Property.set(CockpitDB3T_FGunner.class, "aiTuretNum", 0);
		Property.set(CockpitDB3T_FGunner.class, "weaponControlNum", 10);
		Property.set(CockpitDB3T_FGunner.class, "astatePilotIndx", 1);
		Property.set(CLASS.THIS(), "aiTuretNum", 0);
		Property.set(CLASS.THIS(), "weaponControlNum", 10);
		Property.set(CLASS.THIS(), "astatePilotIndx", 1);
		Property.set(CLASS.THIS(), "normZN", 1.1F);
		Property.set(CLASS.THIS(), "gsZN", 1.1F);
	}
}
