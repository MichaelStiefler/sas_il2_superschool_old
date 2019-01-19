package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.ScoreItem;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.rts.*;

import java.util.ArrayList;
import java.util.Random;
// Referenced classes of package com.maddox.il2.objects.weapons:
// Bomb

public class BombVDVAKS extends com.maddox.il2.objects.weapons.Bomb {

    public BombVDVAKS()
    {
        debark = false;
        spawnArg = new ActorSpawnArg();
    }

    public void start()
    {
        super.start();
        super.speed.z = -20D;
        setSpeed(super.speed);
        Orient orient = new Orient();
        orient.setYPR(orient.getYaw(), orient.getPitch(), orient.getRoll());
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        Point3d point3d = new Point3d();
        Actor actor3 = getOwner();
        Orient orient = new Orient();
        super.pos.getAbs(point3d, orient);
        orient.setYPR(orient.getYaw(), orient.getPitch(), orient.getRoll());
        super.pos.getAbs(point3d);
        if(!debark)
        {
            Random randomGenerator = new Random();
            int spreadX = randomGenerator.nextInt(50) - 25;
            if(spreadX >= 0)
                spreadX += 10;
            else
                spreadX -= 10;
            int spreadY = randomGenerator.nextInt(40) - 25;
            if(spreadY >= 0)
                spreadY += 10;
            else
                spreadY -= 10;
            class1 = com.maddox.il2.objects.vehicles.artillery.ArtilleryWasted.VDVInfantry.class;
            spawn = (ActorSpawn)Spawn.get(class1.getName());
            int i = randomGenerator.nextInt(360);
            insert(spawn, new Loc(super.pos.getAbsPoint().x += spreadX, super.pos.getAbsPoint().y += spreadY, super.pos.getAbsPoint().z, i, 0.0F, 0.0F), actor3.getArmy());
            World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 15D, false));
            debark = true;
        }
        destroy();
    }

    private Actor insert(ActorSpawn actorspawn, Loc loc, int i)
    {
        spawnArg.clear();
        spawnArg.point = loc.getPoint();
        spawnArg.orient = loc.getOrient();
        spawnArg.army = i;
        spawnArg.armyExist = true;
        Actor actor = actorspawn.actorSpawn(spawnArg);
        ((ActorAlign)actor).align();
        return actor;
    }

    public Class class1;
    public ActorSpawn spawn;
    private ActorSpawnArg spawnArg;
    private boolean debark;
    public NetChannel netchannel;

	static {
		java.lang.Class class1 = com.maddox.il2.objects.weapons.BombVDVAKS.class;
		com.maddox.rts.Property.set(class1, "mesh","3DO/Arms/Null/mono.sim");
		com.maddox.rts.Property.set(class1, "radius", 0F);
		com.maddox.rts.Property.set(class1, "power", 0.1F);
		com.maddox.rts.Property.set(class1, "powerType", 0);
		com.maddox.rts.Property.set(class1, "kalibr", 6.00001F);
		com.maddox.rts.Property.set(class1, "massa", 100.0F);
		com.maddox.rts.Property.set(class1, "sound", (Object)null);
	}
}