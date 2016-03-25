// US Cluster bomb CBU-24 , gray color for Navy, Marine Corps

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;


public class BombCBU24_gray_gn16 extends Bomb
{

    public BombCBU24_gray_gn16()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("OrdnanceO", "OrdnanceGrayO");
        mesh.materialReplace("OrdnanceP", "OrdnanceGrayP");
        mesh.materialReplace("OrdnanceQ", "OrdnanceGrayQ");
    }

    public void start()
    {
        super.start();
        t1 = Time.current() + 1000L * (long)Math.max(delayExplosion, 3F) + TrueRandom.nextLong(-250L, 250L);
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if(t1 < Time.current())
            doFireContaineds();
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(t1 > Time.current())
            doFireContaineds();
        super.msgCollision(actor, s, s1);
    }

    private void doFireContaineds()
    {
        Explosions.AirFlak(pos.getAbsPoint(), 1);
        Actor actor = null;
        if(Actor.isValid(getOwner()))
            actor = getOwner();
        Point3d point3d = new Point3d(pos.getAbsPoint());
        Orient orient = new Orient();
        Vector3d vector3d = new Vector3d();
        for(int i = 0; i < 660; i++)
        {
            orient.set(TrueRandom.nextFloat(0.0F, 360F), TrueRandom.nextFloat(-90F, 90F), TrueRandom.nextFloat(-180F, 180F));
            getSpeed(vector3d);
            vector3d.add(TrueRandom.nextDouble(-15D, 15D), TrueRandom.nextDouble(-15D, 15D), TrueRandom.nextDouble(-15D, 15D));
            BombletBLU26 bombletblu26 = new BombletBLU26();
            ((Bomb) (bombletblu26)).pos.setUpdateEnable(true);
            ((Bomb) (bombletblu26)).pos.setAbs(point3d, orient);
            ((Bomb) (bombletblu26)).pos.reset();
            bombletblu26.start();
            bombletblu26.setOwner(actor, false, false, false);
            bombletblu26.setSpeed(vector3d);
        }

        postDestroy();
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    private long t1;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombCBU24_gray_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/CBU24_gn16/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.18F);
        Property.set(class1, "massa", 362.336F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}