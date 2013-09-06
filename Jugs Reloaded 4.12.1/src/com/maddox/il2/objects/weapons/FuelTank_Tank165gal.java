package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_Tank165gal extends FuelTank {

	public FuelTank_Tank165gal() {
	}

	static {
		Class class1 = FuelTank_Tank165gal.class;
		Property.set(class1, "mesh", "3DO/Arms/Tank165gal/mono.sim");
		Property.set(class1, "kalibr", 0.6F);
		Property.set(class1, "massa", 477F);
	}
}
