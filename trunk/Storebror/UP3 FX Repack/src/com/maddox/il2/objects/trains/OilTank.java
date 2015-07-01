package com.maddox.il2.objects.trains;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Spawn;

public class OilTank extends Wagon implements MsgCollisionRequestListener, MsgExplosionListener, MsgShotListener {
	public static class SPAWN implements WagonSpawn {

		public Wagon wagonSpawn(Train train) {
			return new OilTank(train);
		}

		public SPAWN() {
		}
	}

	protected void explode(Actor actor) {
		new MsgAction(0.0D) {

			public void doAction() {
				Point3d point3d = new Point3d();
				pos.getAbs(point3d);
				Explosions.ExplodeVagonFuel(point3d, point3d, 2.0F);
			}

		};
		new MsgAction(1.0D, new Wagon.Pair(this, actor)) {

			public void doAction(Object obj) {
				Actor actor1 = getOwner();
				if (actor1 != null)
					((Train)actor1).wagonDied(((Wagon.Pair)obj).victim, ((Wagon.Pair)obj).initiator);
				life = -1F;
				ActivateMesh();
			}

		};
		//TODO: +++ FX Repack Mod +++
        int i = World.Rnd().nextInt(10);
        if(i < 2)
    		//TODO: --- FX Repack Mod ---
		new Wagon.MyMsgAction(1.82D, this, actor) {

			public void doAction(Object obj) {
				Point3d point3d = new Point3d();
				pos.getAbs(point3d);
				float f = 180F;
				int i = 0;
				float f1 = 120F;
				MsgExplosion.send((Actor)obj, "Body", point3d, (Actor)obj2, 0.0F, f, i, f1);
			}

		};
		//TODO: +++ FX Repack Mod +++
//		new MsgAction(2.2D, this) {
//
//			public void doAction(Object obj) {
//				Wagon wagon = (Wagon)obj;
//				Eff3DActor.New(wagon, new HookNamed(wagon, "Damage"), null, 1.0F, "Effects/Smokes/SmokeOilTank1.eff", 30F);
//				Eff3DActor.New(wagon, new HookNamed(wagon, "Damage"), null, 1.0F, "Effects/Smokes/SmokeOilTank3.eff", 35F);
//			}
//
//		};
//		new MsgAction(34D, this) {
//
//			public void doAction(Object obj) {
//				Wagon wagon = (Wagon)obj;
//				Eff3DActor.New(wagon, new HookNamed(wagon, "Damage"), null, 1.0F, "Effects/Smokes/SmokeOilTank2.eff", 156F);
//			}
//
//		};
		//TODO: --- FX Repack Mod ---
	}

	public OilTank(Train train) {
		super(train, getMeshName(0), getMeshName(1));
		//TODO: +++ FX Repack Mod +++
        life = 0.024F;
        ignoreTNT = 0.3F;
        killTNT = 1.8F;
//		life = 0.02F;
//		ignoreTNT = 0.35F;
//		killTNT = 2.8F;
		//TODO: --- FX Repack Mod ---
		bodyMaterial = 2;
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
		return "3do/Trains/OilTank" + (i == 1 ? "_Dmg" : "") + "/" + s + "/hier.him";
	}

	public static String getMeshNameForEditor() {
		return getMeshName(0);
	}

	private static Class cls;

	static {
		cls = OilTank.class;
		Spawn.add(cls, new SPAWN());
	}
}
