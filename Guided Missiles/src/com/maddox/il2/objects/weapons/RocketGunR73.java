package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunR73 extends MissileGun implements RocketGunWithDelay {

	static {
		Class class1 = RocketGunR73.class;
		Property.set(class1, "bulletClass", (Object) MissileR73.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
