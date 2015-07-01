package com.maddox.il2.objects.trains;

import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.rts.Spawn;

public class Passenger extends Wagon implements MsgCollisionRequestListener, MsgExplosionListener, MsgShotListener {
	public static class SPAWN implements WagonSpawn {

		public Wagon wagonSpawn(Train train) {
			return new Passenger(train);
		}

		public SPAWN() {
		}
	}

	public Passenger(Train train) {
		super(train, getMeshName(0), getMeshName(1));
		//TODO: +++ FX Repack Mod +++
        life = 0.02F;
        ignoreTNT = 0.45F;
        killTNT = 1.8F;
//		life = 0.015F;
//		ignoreTNT = 0.42F;
//		killTNT = 1.5F;
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
		return "3do/Trains/Wagon" + (i == 1 ? "_Dmg" : "") + "/" + s + "/hier.him";
	}

	public static String getMeshNameForEditor() {
		return getMeshName(0);
	}

	private static Class cls;

	static {
		cls = Passenger.class;
		Spawn.add(cls, new SPAWN());
	}
}
