package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunR3R extends MissileGun implements RocketGunWithDelay {
	static {
		Class class1 = RocketGunR3R.class;
		Property.set(class1, "bulletClass", (Object) MissileR3R.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 5F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
