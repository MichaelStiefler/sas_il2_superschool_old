package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_TankSF17 extends FuelTank {

	public FuelTank_TankSF17() {
	}

	static {
		Class class1 = com.maddox.il2.objects.weapons.FuelTank_TankSF17.class;
		Property.set(class1, "mesh", "3DO/Arms/SeafireLate_SAS_Droptank/mono.sim");
		Property.set(class1, "kalibr", 0.53F);
		Property.set(class1, "massa", 342F);
	}
}
