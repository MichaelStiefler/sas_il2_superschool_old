package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunNebelgerat_S200_V extends BombGun {
    static {
        Class class1 = BombGunNebelgerat_S200_V.class;
        Property.set(class1, "bulletClass", (Object) BombNebelgerat_S200_V.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}