package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAGM84A extends MissileGun implements RocketGunWithDelay {
	static {
		Class class1 = RocketGunAGM84A.class;
		Property.set(class1, "bulletClass", (Object) MissileAGM84A.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 2.0F);
		Property.set(class1, "sound", "weapon.bombgun");
		Property.set(class1, "dateOfUse", 0x128ca21);
	}
}
