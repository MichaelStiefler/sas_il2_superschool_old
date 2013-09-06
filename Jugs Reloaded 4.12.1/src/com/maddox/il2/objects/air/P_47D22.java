package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_47D22 extends P_47 {
	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		com.maddox.il2.ai.EventLog.type("onAircraftLoaded: " + this.thisWeaponsName);
		hierMesh().chunkVisible("ETank_D0", false);
		boolean bCenterRackVisible = false;
		boolean bWingRacksVisible = false;
		bCenterRackVisible = ((this.thisWeaponsName.equalsIgnoreCase("tank")) || (this.thisWeaponsName.equalsIgnoreCase("tank2x500")) || (this.thisWeaponsName.equalsIgnoreCase("tank6x45")) || (this.thisWeaponsName.equalsIgnoreCase("tank2x5006x45"))
				|| (this.thisWeaponsName.equalsIgnoreCase("1x1000")) || (this.thisWeaponsName.equalsIgnoreCase("1x10002x500")) || (this.thisWeaponsName.equalsIgnoreCase("1x10006x45")) || (this.thisWeaponsName.equalsIgnoreCase("1x10002x5006x45")) || (this.thisWeaponsName
				.indexOf("c_") != -1));
		bWingRacksVisible = ((this.thisWeaponsName.equalsIgnoreCase("tank2x500")) || (this.thisWeaponsName.equalsIgnoreCase("tank2x5006x45")) || (this.thisWeaponsName.equalsIgnoreCase("2x500")) || (this.thisWeaponsName.equalsIgnoreCase("2x5006x45"))
				|| (this.thisWeaponsName.equalsIgnoreCase("1x10002x500")) || (this.thisWeaponsName.equalsIgnoreCase("1x10002x5006x45")) || (this.thisWeaponsName.indexOf("w_") != -1));
		hierMesh().chunkVisible("Rack_D0", bCenterRackVisible);
		hierMesh().chunkVisible("RackL_D0", bWingRacksVisible);
		hierMesh().chunkVisible("RackR_D0", bWingRacksVisible);
	}

	static {
		Class localClass = P_47D22.class;
		new NetAircraft.SPAWN(localClass);
		Property.set(localClass, "iconFar_shortClassName", "P-47");
		Property.set(localClass, "meshName", "3DO/Plane/P-47D-22(Multi1)/hier.him");
		Property.set(localClass, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(localClass, "meshName_us", "3DO/Plane/P-47D-22(USA)/hier.him");
		Property.set(localClass, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(localClass, "yearService", 1943.0F);
		Property.set(localClass, "yearExpired", 1947.5F);
		Property.set(localClass, "FlightModel", "FlightModels/P-47D-22.fmd");
		Property.set(localClass, "cockpitClass", new Class[] { CockpitP_47D22.class });
		Property.set(localClass, "LOSElevation", 0.9879F);
		weaponTriggersRegister(localClass, new int[] { 0, 0, 0, 0, 1, 1, 1, 1, 9, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 9, 9, 9 });
		weaponHooksRegister(localClass, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalDev01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01", "_ExternalDev02", "_ExternalDev03",
				"_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01" });
	}
}