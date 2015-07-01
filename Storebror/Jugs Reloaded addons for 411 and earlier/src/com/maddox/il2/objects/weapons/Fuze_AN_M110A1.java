package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_AN_M110A1 extends Fuze {

	public Fuze_AN_M110A1() {
	}

	static {
		Class class1 = Fuze_AN_M110A1.class;
		Property.set(class1, "type", 0);
		Property.set(class1, "airTravelToArm", 220F);
		Property.set(class1, "minDelay", 0.0F);
		Property.set(class1, "maxDelay", 0.0F);
	}
}
