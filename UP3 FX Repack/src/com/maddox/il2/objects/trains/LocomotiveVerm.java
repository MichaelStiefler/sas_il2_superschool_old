package com.maddox.il2.objects.trains;

import com.maddox.JGP.Point3d;
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

public class LocomotiveVerm extends Wagon implements MsgCollisionRequestListener, MsgExplosionListener, MsgShotListener {
	public static class SPAWN implements WagonSpawn {

		public Wagon wagonSpawn(Train train) {
			return new LocomotiveVerm(train);
		}

		public SPAWN() {
		}
	}

	protected void explode(Actor actor) {
		new MsgAction(0.0D, this) {

			public void doAction(Object obj) {
				//TODO: +++ FX Repack Mod +++
                Point3d point3d = new Point3d();
                pos.getAbs(point3d);
                Explosions.ExplodeLoco(point3d, point3d, 2.0F);
//				LocomotiveVerm locomotiveverm = (LocomotiveVerm)obj;
//				Eff3DActor.New(locomotiveverm, new HookNamed(locomotiveverm, "Damage"), null, 1.0F,
//						"effects/Explodes/RS82/Water/Fontain.eff", 2.0F);
//				Eff3DActor.New(locomotiveverm, new HookNamed(locomotiveverm, "Damage"), null, 1.0F,
//						"Effects/Smokes/LocomotiveFC2.eff", 25F);
				//TODO: --- FX Repack Mod ---
			}

		};
		//TODO: +++ FX Repack Mod +++
		new MsgAction(1D, new Wagon.Pair(this, actor)) {
//		new MsgAction(2D, new Wagon.Pair(this, actor)) {
			//TODO: --- FX Repack Mod ---

			public void doAction(Object obj) {
				Actor actor1 = getOwner();
				if (actor1 != null)
					((Train)actor1).wagonDied(((Wagon.Pair)obj).victim, ((Wagon.Pair)obj).initiator);
				life = -1F;
				ActivateMesh();
			}

		};
		//TODO: +++ FX Repack Mod +++
//		new MsgAction(6.5D, this) {
//
//			public void doAction(Object obj) {
//				LocomotiveVerm locomotiveverm = (LocomotiveVerm)obj;
//				Eff3DActor.New(locomotiveverm, new HookNamed(locomotiveverm, "Damage"), null, 1.0F,
//						"Effects/Smokes/LocomotiveFC1.eff", 35F);
//			}
//
//		};
		//TODO: --- FX Repack Mod ---
	}

	protected LocomotiveVerm(Train train, String s, String s1) {
		super(train, s, s1);
		//TODO: +++ FX Repack Mod +++
        life = 0.028F;
        ignoreTNT = 0.8F;
        killTNT = 5F;
//		life = 0.015F;
//		ignoreTNT = 0.84F;
//		killTNT = 3.5F;
		//TODO: --- FX Repack Mod ---
		bodyMaterial = 2;
		//TODO: +++ FX Repack Mod +++
        pipe = Eff3DActor.New(this, new HookNamed(this, "Vapor"), null, 1.0F, "Effects/Smokes/SmokeBlack_Locomotive.eff", -1F);
//		pipe = Eff3DActor.New(this, new HookNamed(this, "Vapor"), null, 1.0F, "Effects/Smokes/SmokeLocomotive.eff", -1F);
		//TODO: --- FX Repack Mod ---
	}

	public LocomotiveVerm(Train train) {
		this(train, getMeshName(0), getMeshName(1));
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
		return "3do/Trains/Prvz_B" + (i == 1 ? "_Dmg" : "") + "/" + s + "/hier.him";
	}

	public static String getMeshNameForEditor() {
		return getMeshName(0);
	}

	void place(Point3d point3d, Point3d point3d1, boolean flag, boolean flag1) {
		super.place(point3d, point3d1, flag, flag1);
		if (pipe == null)
			return;
		float f;
		if (!IsDead()) {
			f = ((Train)getOwner()).getEngineSmokeKoef();
			if (f == 0.0F && ((Train)getOwner()).stoppedForever())
				f = -1F;
		} else {
			f = -1F;
		}
		if (f >= 0.0F) {
			pipe._setIntesity(f);
		} else {
			pipe._finish();
			pipe = null;
		}
	}

	protected void ActivateMesh() {
		if (IsDead() && pipe != null) {
			pipe._finish();
			pipe = null;
		}
		super.ActivateMesh();
	}

	private static Class cls;
	protected Eff3DActor pipe;

	static {
		cls = LocomotiveVerm.class;
		Spawn.add(cls, new SPAWN());
	}
}
