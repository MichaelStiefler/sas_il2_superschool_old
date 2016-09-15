package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpMk12 extends Torpedo {

    static {
        Class class1 = BombTorpMk12.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk12_Torpedo/mono.sim");
        Property.set(class1, "radius", 85.8F);
        Property.set(class1, "power", 352F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.45F);
        Property.set(class1, "massa", 702F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 20.55F);
        Property.set(class1, "traveltime", 68.12F);
        Property.set(class1, "startingspeed", 0.0F);
        Property.set(class1, "impactAngleMin", 14F);
        Property.set(class1, "impactAngleMax", 27F);
        Property.set(class1, "impactSpeed", 90F);
        Property.set(class1, "armingTime", 3F);
        Property.set(class1, "dropAltitude", 40F);
        Property.set(class1, "dropSpeed", 250F);
    }
}
