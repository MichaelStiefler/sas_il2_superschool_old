package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunJDAM84 extends RocketBombGun {

	public void setRocketTimeLife(float f) {
		timeLife = 30F;
	}

	static {
		Class class1 = RocketGunJDAM84.class;
		Property.set(class1, "bulletClass", (Object) JDAM84.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 2.0F);
		Property.set(class1, "sound", "weapon.bombgun");
	}
}