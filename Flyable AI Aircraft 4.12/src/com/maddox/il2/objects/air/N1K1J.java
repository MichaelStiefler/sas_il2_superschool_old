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
// Last Edited at: 2013/02/03

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class N1K1J extends N1K {

	public N1K1J() {
	}

	public void update(float f) {
		super.update(f);
		float f1 = FM.EI.engines[0].getControlRadiator();
		if (Math.abs(flapps - f1) > 0.01F) {
			flapps = f1;
			for (int i = 1; i < 11; i++)
				hierMesh().chunkSetAngles("Cowflap" + i + "_D0", 0.0F, -20F * f1, 0.0F);

		}
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (FM.CT.Weapons[3] != null) {
			hierMesh().chunkVisible("RackL_D0", true);
			hierMesh().chunkVisible("RackR_D0", true);
		}
	}

	public void moveCockpitDoor(float f) {
		resetYPRmodifier();
		xyz[1] = -cvt(f, 0.1F, 0.99F, 0.0F, -0.61F);
		hierMesh().chunkSetLocate("Blister1_D0", xyz, ypr);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	static {
		Class class1 = N1K1J.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "N1K");
		Property.set(class1, "meshName", "3DO/Plane/N1K1-J(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "meshName_ja", "3DO/Plane/N1K1-J(ja)/hier.him");
		Property.set(class1, "PaintScheme_ja", new PaintSchemeFMPar04());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/N1K1-J.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitN1K1JA.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1, 1, 3, 3, 9, 9 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02" });
		weaponsRegister(class1, "default", new String[] { "MGunMG15s 525", "MGunMG15s 525", "MGunHo5k 60", "MGunHo5k 60", "MGunHo5k 60", "MGunHo5k 60", null, null, null, null });
		weaponsRegister(class1, "1x400dt", new String[] { "MGunMG15s 525", "MGunMG15s 525", "MGunHo5k 60", "MGunHo5k 60", "MGunHo5k 60", "MGunHo5k 60", null, null, "PylonN1K1PLN1", "FuelTankGun_TankN1K1" });
		weaponsRegister(class1, "2x30", new String[] { "MGunMG15s 525", "MGunMG15s 525", "MGunHo5k 60", "MGunHo5k 60", "MGunHo5k 60", "MGunHo5k 60", "BombGun30kgJ 1", "BombGun30kgJ 1", null, null });
		weaponsRegister(class1, "2x60", new String[] { "MGunMG15s 525", "MGunMG15s 525", "MGunHo5k 60", "MGunHo5k 60", "MGunHo5k 60", "MGunHo5k 60", "BombGun60kgJ 1", "BombGun60kgJ 1", null, null });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null });
	}
}
