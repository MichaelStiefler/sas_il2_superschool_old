package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunH19 extends RocketGun {

	public RocketGunH19() {
	}

	public void setRocketTimeLife(float f) {
		timeLife = -1F;
	}

	static {
		Class class1 = RocketGunH19.class;
		Property.set(class1, "bulletClass", (Object) RocketH19.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 1.0F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
