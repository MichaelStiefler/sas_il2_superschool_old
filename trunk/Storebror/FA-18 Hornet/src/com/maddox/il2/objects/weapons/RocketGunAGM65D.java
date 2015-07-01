package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAGM65D extends MissileGun implements RocketGunWithDelay {

	public void setRocketTimeLife(float f) {
		timeLife = 30F;
	}

	static {
		Class class1 = RocketGunAGM65D.class;
		Property.set(class1, "bulletClass", (Object) MissileAGM65D.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 4.0F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}