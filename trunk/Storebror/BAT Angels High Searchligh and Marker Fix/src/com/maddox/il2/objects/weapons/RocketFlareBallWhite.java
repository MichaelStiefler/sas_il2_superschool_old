package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class RocketFlareBallWhite extends RocketFlareBall {

    public RocketFlareBallWhite(Actor actor, Point3d point3d, Orient orient) {
        super(actor, point3d, orient);
    }

    static {
        Class theClass = RocketFlareBallWhite.class;
        initCommon(theClass);
        Property.set(theClass, "mesh", "3DO/Arms/RocketFlareBallWhite/mono.sim");
        Property.set(theClass, "effect", "3DO/Effects/Fireworks/FlareBallWhite.eff");
        Property.set(theClass, "lightColor", new Color3f(1.0F, 0.9F, 0.6F));
        Property.set(theClass, "emitColor", new Color3f(0.8F, 0.8F, 1.0F));
    }
}
