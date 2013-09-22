package com.maddox.il2.objects.air;

import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class WhirlwindA extends Whirlwind implements TypeFighter, TypeStormovik {

	public void onAircraftLoaded() {
		super.onAircraftLoaded();

		if (this.thisWeaponsName.startsWith("2x")) {
			hierMesh().chunkVisible("WingRackL_D0", true);
			hierMesh().chunkVisible("WingRackR_D0", true);
			return;
		}
	}

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Whirlwind");
		Property.set(class1, "meshName", "3DO/Plane/Whirlwind(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1946.5F);
		Property.set(class1, "FlightModel", "FlightModels/whirlwind.fmd:whirlwind_FM");
		Property.set(class1, "cockpitClass", new Class[] { CockpitWhirlwind.class });
		Property.set(class1, "LOSElevation", 0.6731F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock01",
				"_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalRock05", "_ExternalRock05", "_ExternalRock06", "_ExternalRock06", "_ExternalRock07", "_ExternalRock07",
				"_ExternalRock08" });
	}
}
