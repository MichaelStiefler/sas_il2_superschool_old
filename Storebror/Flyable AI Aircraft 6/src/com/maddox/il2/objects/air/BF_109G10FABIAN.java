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
import com.maddox.rts.Property;

public class BF_109G10FABIAN extends BF_109 implements TypeBNZFighter, TypeAcePlane {

	public BF_109G10FABIAN() {
		kangle = 0.0F;
		flapps = 0.0F;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.Skill = 3;
	}

	public void update(float f) {
		if (FM.getSpeed() > 5F) {
			hierMesh().chunkSetAngles("SlatL_D0", 0.0F, cvt(FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F), 0.0F);
			hierMesh().chunkSetAngles("SlatR_D0", 0.0F, cvt(FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F), 0.0F);
		}
		if (Math.abs(flapps - kangle) > 0.01F) {
			flapps = kangle;
			hierMesh().chunkSetAngles("Flap01L_D0", 0.0F, -20F * kangle, 0.0F);
			hierMesh().chunkSetAngles("Flap01U_D0", 0.0F, 20F * kangle, 0.0F);
			hierMesh().chunkSetAngles("Flap02L_D0", 0.0F, -20F * kangle, 0.0F);
			hierMesh().chunkSetAngles("Flap02U_D0", 0.0F, 20F * kangle, 0.0F);
		}
		kangle = 0.95F * kangle + 0.05F * FM.EI.engines[0].getControlRadiator();
		if (kangle > 1.0F)
			kangle = 1.0F;
		super.update(f);
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		float f3 = 0.8F;
		float f4 = -0.5F * (float) Math.cos((double) (f / f3) * 3.1415926535897931D) + 0.5F;
		if (f <= f3 || f == 1.0F) {
			hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F * f4, 0.0F);
			hiermesh.chunkSetAngles("GearL2_D0", -33.5F * f4, 0.0F, 0.0F);
		}
		f4 = -0.5F * (float) Math.cos((double) ((f1 - (1.0F - f3)) / f3) * 3.1415926535897931D) + 0.5F;
		if (f1 >= 1.0F - f3) {
			hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F * f4, 0.0F);
			hiermesh.chunkSetAngles("GearR2_D0", 33.5F * f4, 0.0F, 0.0F);
		}
		if (f > 0.99F) {
			hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F, 0.0F);
			hiermesh.chunkSetAngles("GearL2_D0", -33.5F, 0.0F, 0.0F);
		}
		if (f < 0.01F) {
			hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 0.0F);
			hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 0.0F);
		}
		if (f1 > 0.99F) {
			hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F, 0.0F);
			hiermesh.chunkSetAngles("GearR2_D0", 33.5F, 0.0F, 0.0F);
		}
		if (f1 < 0.01F) {
			hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 0.0F);
			hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 0.0F);
		}
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(hierMesh(), f, f1, f2);
	}

	public void moveSteering(float f) {
		hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
	}

	private float kangle;
	private float flapps;

	static {
		Class class1 = BF_109G10FABIAN.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Bf109");
		Property.set(class1, "meshName", "3DO/Plane/Bf-109G-10(ofFabian)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1946F);
		Property.set(class1, "FlightModel", "FlightModels/Bf-109G-10.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109G10.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 1, 1, 1, 1, 1, 9, 9 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_ExternalDev02", "_ExternalDev03" });
		weaponsRegister(class1, "default", new String[] { "MGunMG131si 300", "MGunMG131si 300", null, "MGunMK108ki 65", null, null, null, null, null, null });
		weaponsRegister(class1, "R4-MK108", new String[] { "MGunMG131si 300", "MGunMG131si 300", null, "MGunMK108ki 65", null, null, "MGunMK108kh 35", "MGunMK108kh 35", "PylonMk108", "PylonMk108" });
		weaponsRegister(class1, "R6-151-20", new String[] { "MGunMG131si 300", "MGunMG131si 300", "MGunMK108ki 65", null, "MGunMG15120kh 140 ", "MGunMG15120kh 140", null, null, "PylonMG15120", "PylonMG15120" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null });
	}
}
