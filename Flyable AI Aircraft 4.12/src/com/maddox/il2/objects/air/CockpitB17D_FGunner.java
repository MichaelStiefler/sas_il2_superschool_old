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

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class CockpitB17D_FGunner extends CockpitGunner {

	public void moveGun(Orient orient) {

		Orient auxOrient1 = new Orient();
		Orient auxOrient2 = new Orient();
		auxOrient1.set(orient);
		auxOrient2.set(orient);

		float f;
		float f1;

		f = orient.getYaw();
		f1 = orient.getTangage();
		HUD.training("f=" + f + " f1=" + f1);
		if (f < -20F) {
			f = -20F;
		}
		if (f > 10F) {
			f = 10F;
		}
		if (f1 > 30F) {
			f1 = 30F;
		}
		if (f1 < -20F) {
			f1 = -20F;
		}
		orient.setYPR(f, f1, 0.0F);
		orient.wrap();

		f = auxOrient1.getYaw();
		f1 = auxOrient1.getTangage();
		bGunAuxFire1 = true;
		if (f < -10F) {
			f = -10F;
			bGunAuxFire1 = false;
		}
		if (f > 10F) {
			f = 10F;
			bGunAuxFire1 = false;
		}
		if (f1 > 30F) {
			f1 = 30F;
			bGunAuxFire1 = false;
		}
		if (f1 < -20F) {
			f1 = -20F;
			bGunAuxFire1 = false;
		}
		auxOrient1.setYPR(f, f1, 0.0F);
		auxOrient1.wrap();

		f = auxOrient2.getYaw();
		f1 = auxOrient2.getTangage();
		bGunAuxFire2 = true;
		if (f < -20F) {
			f = -20F;
			bGunAuxFire2 = false;
		}
		if (f > 10F) {
			f = 10F;
			bGunAuxFire2 = false;
		}
		if (f1 > 10F) {
			f1 = 10F;
			bGunAuxFire2 = false;
		}
		if (f1 < -20F) {
			f1 = -20F;
			bGunAuxFire2 = false;
		}
		auxOrient2.setYPR(f, f1, 0.0F);
		auxOrient2.wrap();

		if (isRealMode()) {
			if (aircraft().FM.turret[1].bIsOperable) {
				aircraft().FM.turret[1].bIsAIControlled = false;
				aircraft().FM.turret[1].tu[0] = auxOrient1.getAzimut() + 15.0F;
				aircraft().FM.turret[1].tu[1] = auxOrient1.getTangage();
			}
			if (aircraft().FM.turret[2].bIsOperable) {
				aircraft().FM.turret[2].bIsAIControlled = false;
				aircraft().FM.turret[2].tu[0] = auxOrient2.getAzimut();
				aircraft().FM.turret[2].tu[1] = auxOrient2.getTangage();
			}
		} else {
			aircraft().FM.turret[1].bIsAIControlled = true;
			aircraft().FM.turret[2].bIsAIControlled = true;
		}

		super.moveGun(orient);
		mesh.chunkSetAngles("TurretA", -13F, -orient.getYaw() + 15.0F, 0.0F);
		mesh.chunkSetAngles("TurretB", -15.5F, orient.getTangage(), 0.0F);
		mesh.chunkSetAngles("TurretC", 0.0F, -cvt(orient.getYaw() - 15.0F, -17F, 17F, -17F, 17F), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f < -20F)
					f = -20F;
				if (f > 10F)
					f = 10F;
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
			fm.CT.WeaponControl[10] = bGunFire;
			fm.CT.WeaponControl[11] = bGunFire & bGunAuxFire1;
			fm.CT.WeaponControl[12] = bGunFire & bGunAuxFire2;
			if (bGunFire) {
				if (hook1 == null)
					hook1 = new HookNamed(aircraft(), "_MGUN01");
				doHitMasterAircraft(aircraft(), hook1, "_MGUN01");
			}
		}
	}

	public void doGunFire(boolean flag) {
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			else
				bGunFire = flag;
			fm.CT.WeaponControl[10] = bGunFire;
			fm.CT.WeaponControl[11] = bGunFire & bGunAuxFire1;
			fm.CT.WeaponControl[12] = bGunFire & bGunAuxFire2;
		}
	}

	public CockpitB17D_FGunner() {
		super("3DO/Cockpit/B-25J-FGun/FGunnerB17D.him", "bf109");
		hook1 = null;
		bGunAuxFire1 = false;
		bGunAuxFire2 = false;
		cockpitNightMats = (new String[] { "textrbm9", "texture25" });
		setNightMats(false);
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
			aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
			aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
		aircraft().hierMesh().chunkVisible("Turret2B_D0", true);
		aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
		super.doFocusLeave();
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 1) != 0)
			mesh.chunkVisible("XGlassDamage1", true);
		if ((fm.AS.astateCockpitState & 8) != 0)
			mesh.chunkVisible("XGlassDamage2", true);
		if ((fm.AS.astateCockpitState & 0x20) != 0)
			mesh.chunkVisible("XGlassDamage2", true);
		if ((fm.AS.astateCockpitState & 2) != 0)
			mesh.chunkVisible("XGlassDamage3", true);
		if ((fm.AS.astateCockpitState & 4) != 0)
			mesh.chunkVisible("XHullDamage1", true);
		if ((fm.AS.astateCockpitState & 0x10) != 0)
			mesh.chunkVisible("XHullDamage2", true);
	}

	public void reflectWorldToInstruments(float f) {
		mesh.chunkSetAngles("zSpeed", 0.0F, floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
		mesh.chunkSetAngles("zSpeed1", 0.0F, floatindex(cvt(fm.getSpeedKMH(), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
		mesh.chunkSetAngles("zAlt1", 0.0F, cvt((float) fm.Loc.z, 0.0F, 9144F, 0.0F, 10800F), 0.0F);
		mesh.chunkSetAngles("zAlt2", 0.0F, cvt((float) fm.Loc.z, 0.0F, 9144F, 0.0F, 1080F), 0.0F);
		mesh.chunkSetAngles("zHour", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		mesh.chunkSetAngles("zMinute", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("zSecond", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		mesh.chunkSetAngles("zCompass1", 0.0F, fm.Or.getAzimut(), 0.0F);
		WayPoint waypoint = fm.AP.way.curr();
		if (waypoint != null) {
			waypoint.getP(P1);
			V.sub(P1, fm.Loc);
			float f2 = (float) (57.295779513082323D * Math.atan2(V.x, V.y));
			mesh.chunkSetAngles("zCompass2", 0.0F, 90F + f2, 0.0F);
		}
	}

	public void toggleLight() {
		cockpitLightControl = !cockpitLightControl;
		if (cockpitLightControl)
			setNightMats(true);
		else
			setNightMats(false);
	}

	private static final float speedometerScale[] = { 0.0F, 2.5F, 54F, 104F, 154.5F, 205.5F, 224F, 242F, 259.5F, 277.5F, 296.25F, 314F, 334F, 344.5F };
	private static Point3d P1 = new Point3d();
	private static Vector3d V = new Vector3d();
	private Hook hook1;
	private boolean bGunAuxFire1;
	private boolean bGunAuxFire2;

	static {
		Property.set(CockpitB17F_FGunner.class, "aiTuretNum", 0);
		Property.set(CockpitB17F_FGunner.class, "weaponControlNum", 10);
		Property.set(CockpitB17F_FGunner.class, "astatePilotIndx", 2);
	}
}
