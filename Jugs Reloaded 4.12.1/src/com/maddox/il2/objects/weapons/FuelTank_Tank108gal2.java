package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_Tank108gal2 extends FuelTank {

	public FuelTank_Tank108gal2() {
	}

	static {
		Class class1 = FuelTank_Tank108gal2.class;
		Property.set(class1, "mesh", "3DO/Arms/Tank108gal2/mono.sim");
		Property.set(class1, "kalibr", 0.6F);
		Property.set(class1, "massa", 294.88F);
	}
}
