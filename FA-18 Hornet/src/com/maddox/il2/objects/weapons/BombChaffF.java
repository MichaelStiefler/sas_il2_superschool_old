// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 6/4/2013 4:22:42 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BombBLU_108.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb, BombletBLU108

public class BombChaffF extends Bomb
{

    public BombChaffF()
    {
        bFinsDeployed = false;
    }

    protected boolean haveSound()
    {
        return false;
    }

    public void start()
    {
        super.start();
        ttcurTM = World.Rnd().nextFloat(0.5F, 1.0F);
        t1 = Time.current() + 10L;
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
        Actor actor = null;
        if(Actor.isValid(getOwner()))
            actor = getOwner();
        Point3d point3d = new Point3d(pos.getAbsPoint());
        Orient orient = new Orient();
        Vector3d vector3d = new Vector3d();
        for(int i = 0; i < 50; i++)
        {
            orient.set(World.Rnd().nextFloat(0.0F, 360F), World.Rnd().nextFloat(-90F, 90F), World.Rnd().nextFloat(-180F, 180F));
            getSpeed(vector3d);
            vector3d.add(World.Rnd().nextDouble(-7D, 7D), World.Rnd().nextDouble(-7D, 7D), World.Rnd().nextDouble(-7D, 7D));
            BombletBLU108 BombletBLU108 = new BombletBLU108();
            ((Bomb) (BombletBLU108)).pos.setUpdateEnable(true);
            ((Bomb) (BombletBLU108)).pos.setAbs(point3d, orient);
            ((Bomb) (BombletBLU108)).pos.reset();
            BombletBLU108.start();
            BombletBLU108.setOwner(actor, false, false, false);
            BombletBLU108.setSpeed(vector3d);
        }

        postDestroy();
    }

    private boolean bFinsDeployed;
    private float ttcurTM;
    private long t1;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombChaffF.class;
        Property.set(class1, "mesh", "3do/arms/2KgBomblet/mono.sim");
        Property.set(class1, "radius", 0.001F);
        Property.set(class1, "power", 0.001F);
        Property.set(class1, "dragCoefficient", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.00001F);
        Property.set(class1, "massa", 0.00001F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}