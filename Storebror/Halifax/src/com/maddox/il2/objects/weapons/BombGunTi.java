package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunTi extends BombGun {

    protected boolean haveSound() {
        return true;
    }

    public void setBombDelay(float f) {
        this.bombDelay = 0.0F;
        if (this.bomb != null)
            this.bomb.delayExplosion = this.bombDelay;
    }

    static {
        Class class1 = BombGunTi.class;
        Property.set(class1, "bulletClass", (Object) BombTi.class);
        Property.set(class1, "bullets", 32);
        Property.set(class1, "shotFreq", 8F);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun_phball");
    }
}
