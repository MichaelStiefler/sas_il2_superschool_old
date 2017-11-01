package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class RocketFlareBallRed extends RocketFlareBall {

    public RocketFlareBallRed(Actor actor, Point3d point3d, Orient orient) {
        super(actor, point3d, orient);
    }

    static {
        Class theClass = RocketFlareBallRed.class;
        initCommon(theClass);
        Property.set(theClass, "mesh", "3DO/Arms/RocketFlareBallRed/mono.sim");
        Property.set(theClass, "effect", "3DO/Effects/Fireworks/FlareBallRed.eff");
        Property.set(theClass, "lightColor", new Color3f(1.0F, 0.0F, 0.0F));
        Property.set(theClass, "emitColor", new Color3f(0.8F, 0.0F, 0.0F));
    }
}
