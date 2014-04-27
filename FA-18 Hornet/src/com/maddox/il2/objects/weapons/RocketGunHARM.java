package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunHARM extends RocketGunJDAM84 {

	public void setRocketTimeLife(float f) {
		timeLife = 30F;
	}

	static {
		Class class1 = RocketGunHARM.class;
		Property.set(class1, "bulletClass", (Object) HARM.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 3.0F);
		Property.set(class1, "sound", "weapon.rocket_132");
	}
}