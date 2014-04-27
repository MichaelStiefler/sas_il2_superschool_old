package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombletMK20 extends Bomb {
	protected boolean haveSound() {
		return index % 16 == 0;
	}

	static {
		Class class1 = BombletMK20.class;
		Property.set(class1, "mesh", "3do/arms/2KgBomblet/mono.sim");
		Property.set(class1, "radius", 4F);
		Property.set(class1, "power", 10F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.1F);
		Property.set(class1, "massa", 0.6F);
		Property.set(class1, "randomOrient", 1);
		Property.set(class1, "sound", "weapon.bomb_cassette");
	}
}