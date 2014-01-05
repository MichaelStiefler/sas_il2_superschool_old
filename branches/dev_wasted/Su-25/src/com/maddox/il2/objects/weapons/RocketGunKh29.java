package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunKh29 extends MissileGun implements RocketGunWithDelay {


  static {
    Class class1 = com.maddox.il2.objects.weapons.RocketGunKh29.class;
    Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.MissileKh29.class);
    Property.set(class1, "bullets", 1);
    Property.set(class1, "shotFreq", 6.25F);
    Property.set(class1, "sound", "weapon.rocketgun_132");
  }
}
