package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunChaff extends RocketGun {
	static {
		Class class1 = RocketGunChaff.class;
		Property.set(class1, "bulletClass", (Object) RocketFlare.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 3.5F);
		Property.set(class1, "sound", "weapon.Flare");
		Property.set(class1, "cassette", 1);
	}
}
