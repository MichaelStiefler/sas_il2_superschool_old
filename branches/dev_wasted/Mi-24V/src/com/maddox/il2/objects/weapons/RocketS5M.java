// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10.02.2013 0:51:49
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketS5.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket

public class RocketS5M extends Rocket
{

    public RocketS5M()
    {
    }

    public void start(float f, int i)
    {
        setMesh("3DO/Arms/S5/mono_open.sim");
        super.start(f, i);
        super.speed.normalize();
        super.speed.scale(525D);
        super.noGDelay = -1L;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketS5M.class;
        Property.set(class1, "mesh", "3DO/Arms/S5/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
        Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Tracers/GuidedRocket/White.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 7F);
        Property.set(class1, "timeLife", 60F);
        Property.set(class1, "timeFire", 1.1F);
        Property.set(class1, "force", 1300F);
        Property.set(class1, "power", 1.08F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.055F);
        Property.set(class1, "massa", 3.86F);
        Property.set(class1, "massaEnd", 3F);
    }
}