package com.maddox.il2.objects.trains;

import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.rts.Spawn;

public class Platform1 extends Wagon implements MsgCollisionRequestListener, MsgExplosionListener, MsgShotListener {
	public static class SPAWN implements WagonSpawn {

		public Wagon wagonSpawn(Train train) {
			return new Platform1(train);
		}

		public SPAWN() {
		}
	}

	public Platform1(Train train) {
		super(train, getMeshName(0), getMeshName(1));
		//TODO: +++ FX Repack Mod +++
        life = 0.028F;
        ignoreTNT = 0.6F;
        killTNT = 4.5F;
//		life = 0.012F;
//		ignoreTNT = 0.4F;
//		killTNT = 3F;
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
		return "3do/Trains/Platform1" + (i == 1 ? "_Dmg" : "") + "/" + s + "/hier.him";
	}

	public static String getMeshNameForEditor() {
		return getMeshName(0);
	}

	private static Class cls;

	static {
		cls = Platform1.class;
		Spawn.add(cls, new SPAWN());
	}
}
