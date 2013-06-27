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

import com.maddox.rts.Property;

public class MIG_3POKRYSHKIN extends MIG_3 implements TypeAcePlane {

	public MIG_3POKRYSHKIN() {
		kangle = 0.0F;
		flapps = 0.0F;
	}

	public void update(float f) {
		if (FM.getSpeed() > 5F) {
			hierMesh().chunkSetAngles("SlatL_D0", cvt(FM.getAOA(), 6.8F, 11F, 0.0F, 0.9F), 0.0F, 0.0F);
			hierMesh().chunkSetAngles("SlatR_D0", cvt(FM.getAOA(), 6.8F, 11F, 0.0F, 0.9F), 0.0F, 0.0F);
		}
		if (Math.abs(flapps - kangle) > 0.01F) {
			flapps = kangle;
			hierMesh().chunkSetAngles("WaterFlap_D0", 0.0F, 30F * kangle, 0.0F);
			hierMesh().chunkSetAngles("OilRad1_D0", 0.0F, -20F * kangle, 0.0F);
			hierMesh().chunkSetAngles("OilRad2_D0", 0.0F, -20F * kangle, 0.0F);
		}
		kangle = 0.95F * kangle + 0.05F * FM.EI.engines[0].getControlRadiator();
		super.update(f);
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.Skill = 3;
	}

	private float kangle;
	private float flapps;

	static {
		Class class1 = MIG_3POKRYSHKIN.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "MiG");
		Property.set(class1, "meshName", "3do/plane/MIG-3(ofPokryshkin)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
		Property.set(class1, "FlightModel", "FlightModels/MiG-3(ofPokryshkin).fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitMIG_3.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1, 3, 3, 3, 3, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04",
				"_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06" });
		weaponsRegister(class1, "default", new String[] { "MGunShKASs 1450", "MGunShKASs 1450", "MGunUBk 600", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2xBK", new String[] { "MGunShKASs 1450", "MGunShKASs 1450", "MGunUBk 600", "MGunUBk 145", "MGunUBk 145", null, null, null, null, "PylonMiG_3_BK", "PylonMiG_3_BK", null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "6xRS-82", new String[] { "MGunShKASs 1450", "MGunShKASs 1450", "MGunUBk 600", null, null, null, null, null, null, null, null, "PylonRO_82_3", "PylonRO_82_3", "RocketGunRS82 1", "RocketGunRS82 1", "RocketGunRS82 1",
				"RocketGunRS82 1", "RocketGunRS82 1", "RocketGunRS82 1" });
		weaponsRegister(class1, "4xFAB-50",
				new String[] { "MGunShKASs 1450", "MGunShKASs 1450", "MGunUBk 600", null, null, "BombGunFAB50 1", "BombGunFAB50 1", "BombGunFAB50 1", "BombGunFAB50 1", null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2xFAB-100", new String[] { "MGunShKASs 1450", "MGunShKASs 1450", "MGunUBk 600", null, null, null, "BombGunFAB100 1", "BombGunFAB100 1", null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "4xAO-10", new String[] { "MGunShKASs 1450", "MGunShKASs 1450", "MGunUBk 600", null, null, "BombGunAO10 1", "BombGunAO10 1", "BombGunAO10 1", "BombGunAO10 1", null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
