package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.ScoreItem;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.*;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Spawn;

import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.weapons:
// Bomb

public class BombVDVRPG extends com.maddox.il2.objects.weapons.Bomb {

	public BombVDVRPG() {
		spawnArg = new ActorSpawnArg();
	}

	public void start() {
		super.start();
//		getOwner().getSpeed(speed);
        speed.z = -20D;
        setSpeed(speed);
        Orient orient = new Orient();
        orient.setYPR(orient.getYaw(), orient.getPitch(), orient.getRoll());
//        setMesh("3DO/arms/VDVSoldier/mono.sim");
	}

	public void msgCollision(com.maddox.il2.engine.Actor actor,
			java.lang.String s, java.lang.String s1) {
		com.maddox.JGP.Point3d point3d = new Point3d();
		
		com.maddox.il2.engine.Actor actor3 = getOwner();

		Orient orient = new Orient();
		pos.getAbs(point3d, orient);
		orient.setYPR(orient.getYaw(), orient.getPitch(), orient.getRoll());
		pos.getAbs(point3d);

		if (!debark) {

			Random randomGenerator = new Random();
			int spreadX = randomGenerator.nextInt(50) - 25;
			if (spreadX >= 0)
				spreadX += 10;
			else
				spreadX -= 10;
//			point3d.x += spreadX;
			int spreadY = randomGenerator.nextInt(40) - 25;
			if (spreadY >= 0)
				spreadY += 10;
			else
				spreadY -= 10;
//			point3d.y += spreadY;

			
			
			class1 = com.maddox.il2.objects.vehicles.artillery.ArtilleryWasted.VDVRPGInfantry.class;

			spawn = (ActorSpawn) Spawn.get(class1.getName());

			int i = randomGenerator.nextInt(360);
			insert(spawn, new Loc (this.pos.getAbsPoint().x += spreadX, this.pos.getAbsPoint().y += spreadY, this.pos.getAbsPoint().z, i, 0.0F, 0.0F), actor3.getArmy());

			World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 15D, false));

			debark = true;
		}
		destroy();
	}

	private Actor insert(ActorSpawn actorspawn, Loc loc, int i) {
		spawnArg.clear();
		spawnArg.point = loc.getPoint();
		spawnArg.orient = loc.getOrient();
		spawnArg.army = i;
		spawnArg.armyExist = true;
		Actor actor;
		actor = actorspawn.actorSpawn(spawnArg);
		((com.maddox.il2.objects.ActorAlign) (com.maddox.il2.objects.ActorAlign) actor).align();
		return actor;
	}

	public Class class1;
	public ActorSpawn spawn;
	private ActorSpawnArg spawnArg;
	private boolean debark = false;
	public NetChannel netchannel;

	static {
		java.lang.Class class1 = com.maddox.il2.objects.weapons.BombVDVRPG.class;
		com.maddox.rts.Property.set(class1, "mesh",
				"3DO/Arms/Null/mono.sim");
		com.maddox.rts.Property.set(class1, "radius", 0F);
		com.maddox.rts.Property.set(class1, "power", 0.1F);
		com.maddox.rts.Property.set(class1, "powerType", 0);
		com.maddox.rts.Property.set(class1, "kalibr", 6.00001F);
		com.maddox.rts.Property.set(class1, "massa", 75.0F);
		com.maddox.rts.Property.set(class1, "sound", (Object)null);
	}
}