package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAGM_154B extends RocketBombGun {

	public void setRocketTimeLife(float f) {
		timeLife = 30F;
	}

	static {
		Class class1 = RocketGunAGM_154B.class;
		Property.set(class1, "bulletClass", (Object) AGM_154B.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 2.0F);
		Property.set(class1, "sound", "weapon.bombgun");
	}
}