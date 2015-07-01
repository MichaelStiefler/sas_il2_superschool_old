package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunPL3 extends MissileGun implements RocketGunWithDelay {
	static {
		Class class1 = RocketGunPL3.class;
		Property.set(class1, "bulletClass", (Object) MissilePL3.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
