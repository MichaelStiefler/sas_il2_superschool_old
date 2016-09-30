
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.MsgShot;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import java.util.List;


public class RocketChaff_gn16 extends RocketChaff
{

    public RocketChaff_gn16()
    {
    }

    public void start(float f, int i)
    {
        float f1 = 30F;
        super.start(f1, i);
        getOwner().getSpeed(speed);
        speed.x *= 0.98D;
        speed.z -= 8D;
        setSpeed(speed);
        Engine.countermeasures().add(this);
    }

    protected void doExplosion(Actor actor, String s)
    {
        pos.getTime(Time.current(), p);
        Class class1 = getClass();
        float f = Property.floatValue(class1, "power", 1000F);
        int i = Property.intValue(class1, "powerType", 0);
        float f1 = Property.floatValue(class1, "radius", 0.0F);
        getSpeed(speed);
        Vector3f vector3f = new Vector3f(speed);
        vector3f.normalize();
        vector3f.scale(850F);
        MsgShot.send(actor, s, p, vector3f, M, getOwner(), (float)((double)(0.5F * M) * speed.lengthSquared()), 3, 0.0D);
        MsgExplosion.send(actor, s, p, getOwner(), M, f, i, f1);
        destroy();
        Engine.countermeasures().remove(this);
    }

    protected void doExplosionAir()
    {
    }

    private static Point3d p = new Point3d();
    private static Vector3d v = new Vector3d();

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketChaff_gn16.class;
        Property.set(class1, "mesh", "3do/arms/Dummy_Transparent_gn16/mono.sim");
        Property.set(class1, "sprite", (Object)null);
        Property.set(class1, "flame", (Object)null);
        Property.set(class1, "smoke", "3DO/Effects/Aircraft/ChaffSmoke_gn16.eff");
        Property.set(class1, "emitColor", new Color3f(0.01F, 0.01F, 0.01F));
        Property.set(class1, "emitLen", 0F);
        Property.set(class1, "emitMax", 0F);
        Property.set(class1, "sound", (Object)null);
        Property.set(class1, "timeLife", 7F);
        Property.set(class1, "timeFire", 1F);
        Property.set(class1, "force", 2F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 1E-005F);
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "kalibr", 0.3F);
        Property.set(class1, "massa", 2.5F);
        Property.set(class1, "massaEnd", 2F);
        Property.set(class1, "friendlyName", "Chaff");
        Property.set(class1, "iconFar_shortClassName", "Chaff");
    }
}
