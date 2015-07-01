package com.maddox.il2.objects.trains;

import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Spawn;

public class Cargo2 extends Wagon implements MsgCollisionRequestListener, MsgExplosionListener, MsgShotListener {
	public static class SPAWN implements WagonSpawn {

		public Wagon wagonSpawn(Train train) {
			return new Cargo2(train);
		}

		public SPAWN() {
		}
	}

	//TODO: +++ FX Repack Mod +++
    protected void explode(Actor actor)
    {
        new MsgAction(0.0D) {

            public void doAction()
            {
                Point3d point3d = new Point3d();
                pos.getAbs(point3d);
                Explosions.ExplodeVagons(point3d, point3d, 2.0F);
            }

        }
;
        if((crushSeed & 1) == 0)
            new MsgAction(0.80000000000000004D) {

                public void doAction()
                {
                    Point3d point3d = new Point3d();
                    pos.getAbs(point3d);
                    Explosions.ExplodeVagons(point3d, point3d, 2.0F);
                }

            }
;
        Random random = new Random();
        int i = random.nextInt(10);
        if(i < 2)
            new Wagon.MyMsgAction(0.42999999999999999D, this, actor) {

                public void doAction(Object obj)
                {
                    Point3d point3d = new Point3d();
                    pos.getAbs(point3d);
                    float f = 180F;
                    int j = 0;
                    float f1 = 140F;
                    MsgExplosion.send((Actor)obj, "Body", point3d, (Actor)obj2, 0.0F, f, j, f1);
                }

            }
;
        new MsgAction(0.69999999999999996D, new Wagon.Pair(this, actor)) {

            public void doAction(Object obj)
            {
                Actor actor1 = getOwner();
                if(actor1 != null)
                    ((Train)actor1).wagonDied(((Wagon.Pair)obj).victim, ((Wagon.Pair)obj).initiator);
                life = -1F;
                ActivateMesh();
            }

        }
;
        new MsgAction(1.2D, this) {

            public void doAction(Object obj)
            {
                Wagon wagon = (Wagon)obj;
                Eff3DActor.New(wagon, new HookNamed(wagon, "Select1"), null, 1.0F, "Effects/Explodes/Objects/StAir/Smoke.eff.eff", 3F);
                Eff3DActor.New(wagon, new HookNamed(wagon, "Select1"), null, 1.0F, "Effects/Explodes/Objects/Vags/Fire.eff", 60F);
            }

        }
;
    }

	//TODO: --- FX Repack Mod ---
	
	
	public Cargo2(Train train) {
		super(train, getMeshName(0), getMeshName(1));
		//TODO: +++ FX Repack Mod +++
        life = 0.02F;
        ignoreTNT = 0.18F;
        killTNT = 1.1F;
//		life = 0.015F;
//		ignoreTNT = 0.3F;
//		killTNT = 1.4F;
		//TODO: --- FX Repack Mod ---
		bodyMaterial = 3;
	}

	private static String getMeshName(int i) {
		String s;
		switch (World.cur().camouflage) {
		case 0: // '\0'
			s = "summer";
			break;

		case 1: // '\001'
			s = "winter";
			break;

		default:
			s = "summer";
			break;
		}
		return "3do/Trains/Cargo" + (i == 1 ? "_Dmg" : "") + "/" + s + "/hier.him";
	}

	public static String getMeshNameForEditor() {
		return getMeshName(0);
	}

	private static Class cls;

	static {
		cls = Cargo2.class;
		Spawn.add(cls, new SPAWN());
	}
}
