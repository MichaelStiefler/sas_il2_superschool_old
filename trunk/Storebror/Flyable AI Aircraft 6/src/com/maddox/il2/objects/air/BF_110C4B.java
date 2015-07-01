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

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class BF_110C4B extends BF_110 {

	public BF_110C4B() {
	}

	public void blisterRemoved(int i) {
		if (i == 1) {
			doWreck("Blister1A_D0");
			doWreck("Blister1B_D0");
		}
		if (i == 2)
			doWreck("Blister2A_D0");
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.AS.wantBeaconsNet(true);
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	public static boolean bChangedPit = false;

	static {
		Class class1 = BF_110C4B.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Bf-110");
		Property.set(class1, "meshName", "3DO/Plane/Bf-110C-4B/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
		Property.set(class1, "yearService", 1940F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Bf-110C-4.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitBF_110C4B.class, CockpitBF_110Early_Gunner.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 10, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_MGUN05", "_ExternalBomb01", "_ExternalBomb02" });
		weaponsRegister(class1, "default", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1000", "MGunMG17ki 1048", "MGunMGFFki 180", "MGunMGFFki 180", "MGunMG15t 750", null, null });
		weaponsRegister(class1, "2sc250", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1000", "MGunMG17ki 1048", "MGunMGFFki 180", "MGunMGFFki 180", "MGunMG15t 750", "BombGunSC250", "BombGunSC250" });
		weaponsRegister(class1, "2ab250", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1000", "MGunMG17ki 1048", "MGunMGFFki 180", "MGunMGFFki 180", "MGunMG15t 750", "BombGunAB250", "BombGunAB250" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null });
	}
}
