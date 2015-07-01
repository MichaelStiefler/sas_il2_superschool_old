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

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;

public class CockpitJU_87G2RUDEL_Gunner extends CockpitGunner {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			fm = World.getPlayerFM();
			if (fm == null)
				return true;
			if (bNeedSetUp) {
				reflectPlaneMats();
				bNeedSetUp = false;
			}
			if (JU_87.bChangedPit) {
				reflectPlaneToModel();
				JU_87.bChangedPit = false;
			}
			return true;
		}

		Interpolater() {
		}
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = orient.getYaw();
		float f1 = -orient.getTangage();
		mesh.chunkSetAngles("TurretA", 0.0F, f, 0.0F);
		mesh.chunkSetAngles("TurretB", 0.0F, f1, 0.0F);
		mesh.chunkSetAngles("Hose", -0.333F * Math.abs(f1) - 3F, 0.5F * f, 0.0F);
		mesh.chunkSetAngles("PatronsL", 0.0F, f, 0.0F);
		mesh.chunkSetAngles("PatronsL_add", 0.0F, cvt(f, -25F, 0.0F, -91F, 0.0F), 0.0F);
		mesh.chunkSetAngles("PatronsR", 0.0F, f, 0.0F);
		mesh.chunkSetAngles("PatronsR_add", 0.0F, cvt(f, 0.0F, 25F, 0.0F, 91F), 0.0F);
		if (f1 < -30F - 5F * f)
			mesh.chunkVisible("PatronsL_add", false);
		else
			mesh.chunkVisible("PatronsL_add", true);
		if (f1 < -30F + 5F * f)
			mesh.chunkVisible("PatronsR_add", false);
		else
			mesh.chunkVisible("PatronsR_add", true);
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
		if (f < -25F)
			f = -25F;
		if (f > 25F)
			f = 25F;
		if (f1 > 45F)
			f1 = 45F;
		if (f1 < -10F)
			f1 = -10F;
		orient.setYPR(f, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!isRealMode())
			return;
		if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
			bGunFire = false;
		fm.CT.WeaponControl[10] = bGunFire;
		if (bGunFire) {
			if (hook1 == null)
				hook1 = new HookNamed(aircraft(), "_MGUN01");
			doHitMasterAircraft(aircraft(), hook1, "_MGUN01");
			if (hook2 == null)
				hook2 = new HookNamed(aircraft(), "_MGUN02");
			doHitMasterAircraft(aircraft(), hook2, "_MGUN02");
		}
	}

	public void doGunFire(boolean flag) {
		if (!isRealMode())
			return;
		if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
			bGunFire = false;
		else
			bGunFire = flag;
		fm.CT.WeaponControl[10] = bGunFire;
	}

	public CockpitJU_87G2RUDEL_Gunner() {
		super("3DO/Cockpit/Ju-87D-3-Gun/hier.him", "bf109");
		bNeedSetUp = true;
		hook1 = null;
		hook2 = null;
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
		mesh.materialReplace("Matt1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
		mesh.materialReplace("Matt1D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt1D2o"));
		mesh.materialReplace("Matt1D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Rudel1"));
		mesh.materialReplace("Rudel1", mat);
		mat = hiermesh.material(hiermesh.materialFind("Rudel2"));
		mesh.materialReplace("Rudel2", mat);
		// mat = hiermesh.material(hiermesh.materialFind("Overlay3"));
		// mesh.materialReplace("Overlay3", mat);
		// mat = hiermesh.material(hiermesh.materialFind("Overlay4"));
		// mesh.materialReplace("Overlay4", mat);
		mat = hiermesh.material(hiermesh.materialFind("Rudel7"));
		mesh.materialReplace("Rudel7", mat);
		mat = hiermesh.material(hiermesh.materialFind("OverlayD1o"));
		mesh.materialReplace("OverlayD1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("OverlayD2o"));
		mesh.materialReplace("OverlayD2o", mat);
	}

	protected void reflectPlaneToModel() {
		HierMesh hiermesh = aircraft().hierMesh();
		mesh.chunkVisible("Tail1_D0", hiermesh.isChunkVisible("Tail1_D0"));
		mesh.chunkVisible("Tail1_D1", hiermesh.isChunkVisible("Tail1_D1"));
		mesh.chunkVisible("Tail1_D2", hiermesh.isChunkVisible("Tail1_D2"));
		mesh.chunkVisible("Tail1_D3", hiermesh.isChunkVisible("Tail1_D3"));
	}

	public void reflectWorldToInstruments(float f) {
		if (fm == null)
			return;
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
	}

	private boolean bNeedSetUp;
	private Hook hook1;
	private Hook hook2;

}
