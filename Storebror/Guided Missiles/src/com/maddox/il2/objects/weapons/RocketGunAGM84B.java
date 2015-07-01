package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAGM84B extends MissileGun implements RocketGunWithDelay {

	static {
		Class class1 = RocketGunAGM84B.class;
		Property.set(class1, "bulletClass", (Object) MissileAGM84B.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 1.00F);
		Property.set(class1, "sound", "weapon.bombgun");
	}

	public void setRocketTimeLife(float f) {
		this.timeLife = 30F;
	}
}