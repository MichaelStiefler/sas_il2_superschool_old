package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAIM120A extends MissileGun implements RocketGunWithDelay {

	static {
		Class class1 = RocketGunAIM120A.class;
		Property.set(class1, "bulletClass", (Object) MissileAIM120A.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 5.0F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}