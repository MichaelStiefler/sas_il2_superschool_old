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

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class ME_210CA1ZSTR extends ME_210 implements TypeFighter, TypeBNZFighter, TypeStormovik, TypeStormovikArmored {

	public ME_210CA1ZSTR() {
		bChangedPit = false;
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

	protected void moveBayDoor(float f) {
	}

	public static String getSkinPrefix(String s, Regiment regiment) {
		if (regiment == null || regiment.country() == null)
			return "";
		if (regiment.country().equals(PaintScheme.countryHungary))
			return PaintScheme.countryHungary + "_";
		else
			return "";
	}

	public static boolean bChangedPit;

	static {
		Class class1 = ME_210CA1ZSTR.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Me-210");
		Property.set(class1, "meshName", "3DO/Plane/Me-210Ca-1Zerstorer/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
		Property.set(class1, "yearService", 1943F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Me-210Ca-1.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitME_210CA1ZSTR.class, CockpitMe210_Gunner.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 10, 10, 1 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_MGUN03", "_MGUN04", "_CANNON03" });
		weaponsRegister(class1, "default", new String[] { "MGunMG17ki 505", "MGunMG17ki 500", "MGunMG15120k 325", "MGunMG15120k 325", "MGunMG131tj 500", "MGunMG131tj 500", "MGunBofors40 29" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null });
	}
}
