package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAGM65D_gn16 extends MissileGun implements RocketGunWithDelay {

	static {
		Class class1 = RocketGunAGM65D_gn16.class;
		Property.set(class1, "bulletClass", (Object) MissileAGM65D_gn16.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}

}