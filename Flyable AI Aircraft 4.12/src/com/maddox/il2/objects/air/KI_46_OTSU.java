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

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class KI_46_OTSU extends KI_46 implements TypeFighter {

	public KI_46_OTSU() {
		bChangedPit = true;
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

	public boolean bChangedPit;

	static {
		Class class1 = KI_46_OTSU.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Ki-46");
		Property.set(class1, "meshName", "3DO/Plane/Ki-46(Otsu)(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/Ki-46-IIIKai.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitKI_46_OTSU.class });
		weaponTriggersRegister(class1, new int[] { 1, 1 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
		weaponsRegister(class1, "default", new String[] { "MGunHo5ki 200", "MGunHo5ki 200" });
		weaponsRegister(class1, "none", new String[] { null, null });
	}
}
