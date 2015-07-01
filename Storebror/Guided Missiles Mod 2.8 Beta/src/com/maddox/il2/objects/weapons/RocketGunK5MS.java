package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunK5MS extends MissileGun implements RocketGunWithDelay {
	static {
		Class class1 = RocketGunK5MS.class;
		Property.set(class1, "bulletClass", (Object) MissileK5MS.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
