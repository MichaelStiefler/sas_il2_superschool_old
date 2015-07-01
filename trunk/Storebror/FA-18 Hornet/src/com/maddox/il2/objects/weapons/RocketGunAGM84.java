package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAGM84 extends RocketBombGun {

	public void setRocketTimeLife(float f) {
		timeLife = 30F;
	}

	static {
		Class class1 = RocketGunAGM84.class;
		Property.set(class1, "bulletClass", (Object)AGM84.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 1.0F);
		Property.set(class1, "sound", "weapon.bombgun");
	}
}
