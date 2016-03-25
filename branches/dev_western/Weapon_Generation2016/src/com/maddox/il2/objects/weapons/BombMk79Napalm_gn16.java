
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;
import com.maddox.rts.Time;


public class BombMk79Napalm_gn16 extends Bomb
{

    public BombMk79Napalm_gn16()
    {
    }

    public void start()
    {
        super.start();
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        doFireContaineds();
        Point3d point3d = new Point3d();
        super.pos.getTime(Time.current(), point3d);
        Class class1 = getClass();
        float f = Property.floatValue(class1, "power", 0.0F);
        int i = Property.intValue(class1, "powerType", 0);
        float f1 = Property.floatValue(class1, "radius", 1.0F);
        MsgExplosion.send(actor, s1, point3d, getOwner(), super.M, f, i, f1);
    }

    private void doFireContaineds()
    {
        Actor actor = null;
        if(Actor.isValid(getOwner()))
            actor = getOwner();
        Orient orient = new Orient();
        Vector3d vector3d = new Vector3d();
        Point3d point3d = new Point3d(super.pos.getAbsPoint());
        point3d.z += 2D;
        for(int i = 0; i < 20; i++)
        {
            orient.set(World.Rnd().nextFloat(0.0F, 360F), World.Rnd().nextFloat(-90F, 90F), World.Rnd().nextFloat(-180F, 180F));
            getSpeed(vector3d);
            Vector3d vector3d1 = vector3d;
            vector3d1.add(World.Rnd().nextFloat(30F, -30F), World.Rnd().nextFloat(30F, -30F), 0.0D);
            vector3d1.z = 2D;
            if(i < 4)
            {
                vector3d1.x *= 0.55400001192092896D;
                vector3d1.y *= 0.55400001192092896D;
                BombletFire2 bombletfire2 = new BombletFire2();
                ((Actor) (bombletfire2)).pos.setUpdateEnable(true);
                ((Actor) (bombletfire2)).pos.setAbs(point3d, orient);
                ((Actor) (bombletfire2)).pos.reset();
                bombletfire2.start();
                bombletfire2.setOwner(actor, false, false, false);
                bombletfire2.setSpeed(vector3d1);
            } else
            {
                vector3d1.x *= 0.60800002384185791D;
                vector3d1.y *= 0.60800002384185791D;
                BombletFire bombletfire = new BombletFire();
                ((Actor) (bombletfire)).pos.setUpdateEnable(true);
                ((Actor) (bombletfire)).pos.setAbs(point3d, orient);
                ((Actor) (bombletfire)).pos.reset();
                bombletfire.start();
                bombletfire.setOwner(actor, false, false, false);
                bombletfire.setSpeed(vector3d1);
            }
        }

        postDestroy();
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombMk79Napalm_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk79_US_napalm_gn16/mono.sim");
        Property.set(class1, "radius", 114.6805F);
        Property.set(class1, "power", 317.5F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.50F);
        Property.set(class1, "massa", 453.6F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
