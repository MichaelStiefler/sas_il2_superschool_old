package com.maddox.il2.objects.trains;

import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Spawn;

public class LocomotiveTrailorUSSR extends Wagon implements MsgCollisionRequestListener, MsgExplosionListener, MsgShotListener {
	public static class SPAWN implements WagonSpawn {

		public Wagon wagonSpawn(Train train) {
			return new LocomotiveTrailorUSSR(train);
		}

		public SPAWN() {
		}
	}

	protected void explode(Actor actor) {
		new MsgAction(0.0D, this) {

			public void doAction(Object obj) {
				Wagon wagon = (Wagon)obj;
				//TODO: +++ FX Repack Mod +++
                Eff3DActor.New(wagon, new HookNamed(wagon, "Select1"), null, 1.0F, "Effects/Explodes/Objects/Vags/Fire.eff", -1F);
                Eff3DActor.New(wagon, new HookNamed(wagon, "Select1"), null, 1.0F, "Effects/Explodes/Objects/Vags/Fire_Smoke.eff", -1F);
//				Eff3DActor.New(wagon, new HookNamed(wagon, "Damage"), null, 1.0F, "Effects/Smokes/SmokeTrailorFC.eff", 32F);
				//TODO: --- FX Repack Mod ---
			}

		};
		new MsgAction(7D, new Wagon.Pair(this, actor)) {

			public void doAction(Object obj) {
				Actor actor1 = getOwner();
				if (actor1 != null)
					((Train)actor1).wagonDied(((Wagon.Pair)obj).victim, ((Wagon.Pair)obj).initiator);
				life = -1F;
				ActivateMesh();
			}

		};
	}

	public LocomotiveTrailorUSSR(Train train) {
		super(train, getMeshName(0), getMeshName(1));
		//TODO: +++ FX Repack Mod +++
        life = 0.024F;
        ignoreTNT = 0.4F;
        killTNT = 1.3F;
//		life = 0.015F;
//		ignoreTNT = 0.29F;
//		killTNT = 1.9F;
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
		return "3do/Trains/PrvzB" + (i == 1 ? "_Dmg" : "") + "/" + s + "/hier.him";
	}

	public static String getMeshNameForEditor() {
		return getMeshName(0);
	}

	private static Class cls;

	static {
		cls = LocomotiveTrailorUSSR.class;
		Spawn.add(cls, new SPAWN());
	}
}
