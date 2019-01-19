
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;


public class RocketFlareHeli extends RocketFlare
{

    public RocketFlareHeli()
    {
    }
    
    public void start(float f, int i)
    {
        float f1 = 30F;
        super.start(f1, i);
        getOwner().getSpeed(speed);
        setSpeed(speed);
        Eff3DActor.New(this, null, new Loc(), 0.8F, "3do/effects/rocket/rocketsmokewhitetile.eff", f1);
        Engine.countermeasures().add(this);
    }
    
    protected void doExplosion(Actor actor, String s)
    {
        super.doExplosion(actor, s);
    }

    protected void doExplosionAir()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketFlareHeli.class;
        Property.set(class1, "mesh", "3do/arms/Piropatron/mono.sim");
        Property.set(class1, "sprite", (Object)null);
        Property.set(class1, "flame", (Object)null);
        Property.set(class1, "smoke", (Object)null);
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 5F);
        Property.set(class1, "emitMax", 20F);
        Property.set(class1, "sound", (Object)null);
        Property.set(class1, "timeLife", 2F);
        Property.set(class1, "timeFire", 0.001F);
        Property.set(class1, "force", 500F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 1E-005F);
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 3F);
        Property.set(class1, "massaEnd", 3F);
        Property.set(class1, "friendlyName", "Flare");
        Property.set(class1, "iconFar_shortClassName", "Flare");
    }
}
