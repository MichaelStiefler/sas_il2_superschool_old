package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;





public class RocketGunFlareHeli
extends RocketGun
{
	public RocketGunFlareHeli() {}

	static
	{
		Class localClass = com.maddox.il2.objects.weapons.RocketGunFlareHeli.class;
		Property.set(localClass, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketFlareHeli.class);
		Property.set(localClass, "bullets", 1);
		Property.set(localClass, "shotFreq", 7.0F);
		Property.set(localClass, "sound", "weapon.Flare");
	}
}