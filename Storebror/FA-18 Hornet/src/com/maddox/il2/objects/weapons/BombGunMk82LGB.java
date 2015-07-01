package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunMk82LGB extends BombGunSC50 {

	static {
		Class class1 = BombGunMk82LGB.class;
		Property.set(class1, "bulletClass", (Object) BombMk82LGB.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 6F);
		Property.set(class1, "external", 1);
		Property.set(class1, "sound", "weapon.bombgun");
	}
}
