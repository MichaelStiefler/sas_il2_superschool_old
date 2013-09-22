package com.maddox.il2.objects.air;

import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class P_35 extends P_35xyz {

	public void missionStarting() {
		super.missionStarting();
		if (this.FM.isStationedOnGround()) {
			this.FM.AS.setCockpitDoor(FM.actor, 1);
			this.FM.CT.cockpitDoorControl = 1.0F;
		}
	}

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "P_35");
		Property.set(class1, "meshName", "3DO/Plane/P-35/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
		Property.set(class1, "yearService", 1937F);
		Property.set(class1, "yearExpired", 1951F);
		Property.set(class1, "FlightModel", "FlightModels/P-35.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitP_40B.class });
		Property.set(class1, "LOSElevation", 0.9119F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02" });
	}
}
