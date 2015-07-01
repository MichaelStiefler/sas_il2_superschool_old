package com.maddox.il2.objects.air;

import com.maddox.JGP.Geom;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_177_A3_PGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("Body", 0.0F, 0.0F, -1F);
		float f = orient.getYaw();
		f -= 1.5F * Math.sin(Geom.DEG2RAD(f));
		float f1 = orient.getTangage();
		f1 -= 1.5F * Math.cos(Geom.DEG2RAD(f));
		mesh.chunkSetAngles("Turret1A", -f, 0.0F, 0.0F);
		mesh.chunkSetAngles("Turret1B", 0.0F, f1, 0.0F);
		aircraft().hierMesh().chunkSetAngles("Pilot4_D0", -f, 0.0F, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		float f = orient.getYaw();
		float f1 = orient.getTangage() + GUN_OFFSET_TANGAGE;
		while (f < -180F)
			f += 360F;
		while (f > 180F)
			f -= 360F;
		while (prevA0 < -180F)
			prevA0 += 360F;
		while (prevA0 > 180F)
			prevA0 -= 360F;
		if (!isRealMode())
			prevA0 = f;
		else {
			if (bNeedSetUp) {
				prevTime = Time.current() - 1L;
				bNeedSetUp = false;
			}
			if (f < -120F && prevA0 > 120F)
				f += 360F;
			else if (f > 120F && prevA0 < -120F)
				prevA0 += 360F;
			float f3 = f - prevA0;
			float f4 = 0.001F * (float) (Time.current() - prevTime);
			float f5 = Math.abs(f3 / f4);
			if (f5 > 120F)
				if (f > prevA0)
					f = prevA0 + 120F * f4;
				else if (f < prevA0)
					f = prevA0 - 120F * f4;
			prevTime = Time.current();
			float minf1 = 0F;
			float maxf1 = 87F;
			if (f1 > maxf1)
				f1 = maxf1;
			if (f1 < minf1)
				f1 = minf1;
			float f2 = Math.abs(f);
			if (f2 < 10F)
				minf1 = Math.max(minf1, 15F * ((float) Math.cos(Math.PI * f2 / 20F)));
			else if (f2 > 65F && f2 < 85F)
				minf1 = Math.max(minf1, 5F * ((float) Math.cos(Math.PI * (f2 - 75F) / 20F)));
			else if (f2 > 170F)
				minf1 = Math.max(minf1, 5F * ((float) Math.cos(Math.PI * (180 - f2) / 20F)));
			if (f1 < minf1) {
				this.tangageOffset += minf1 - f1;
				f1 = minf1;
			} else {
				if (this.tangageOffset < f1 - minf1) { // Offset is smaller than difference between current f1 and minf1
					f1 -= this.tangageOffset;
					this.tangageOffset = 0F;
				} else { // Offset is larger than difference between current f1 and minf1
					this.tangageOffset -= f1 - minf1;
					f1 = minf1;
				}
			}

			orient.setYPR(f, f1 - GUN_OFFSET_TANGAGE, 0.0F);
			orient.wrap();
			prevA0 = f;
		}
	}

	protected void interpTick() {
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
			if (bGunFire) {
				if (hook1 == null)
					hook1 = new HookNamed(aircraft(), "_MGUN04");
				doHitMasterAircraft(aircraft(), hook1, "_MGUN04");
				if (hook2 == null)
					hook2 = new HookNamed(aircraft(), "_MGUN05");
				doHitMasterAircraft(aircraft(), hook2, "_MGUN05");
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

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 4) != 0)
			mesh.chunkVisible("Z_Holes1_D1", true);
		if ((fm.AS.astateCockpitState & 0x10) != 0)
			mesh.chunkVisible("Z_Holes2_D1", true);
	}

	public CockpitHE_177_A3_PGunner() {
		super("3DO/Cockpit/He-177-PGun/hier.him", "he111_gunner");
		bNeedSetUp = true;
		prevTime = -1L;
		prevA0 = 0.0F;
		hook1 = null;
		hook2 = null;
		this.tangageOffset = 0F;
	}

	private boolean bNeedSetUp;
	private long prevTime;
	private float prevA0;
	private Hook hook1;
	private Hook hook2;
	private float tangageOffset;
	protected static final float GUN_OFFSET_TANGAGE = 15.0F;

	static {
		Class class1 = CockpitHE_177_A3_PGunner.class;
		Property.set(class1, "aiTuretNum", 3);
		Property.set(class1, "weaponControlNum", 13);
		Property.set(class1, "astatePilotIndx", 3);
	}
}
