package com.maddox.il2.objects.vehicles.artillery;

import java.util.List;
import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.ground.Aim;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.weapons.SA2;
import com.maddox.rts.NetChannel;

public abstract class ArtillerySAM {
	public static class S_75 extends ArtilleryGeneric {

		public Actor findEnemy(Aim aim) {
			CheckRadar();
			Strela();
			return null;
		}

		public void Strela() {
			Point3d point3d = new Point3d();
			Orient orient = new Orient();
			super.pos.getAbs(point3d, orient);
			orient.setYPR(orient.getYaw(), orient.getPitch() + 45F, orient.getRoll());
			Random random = new Random();
			int i = random.nextInt(100);
			List list = Engine.targets();
			int s = list.size();
			for (int h = 0; h < s; h++) {
				Actor actor = (Actor) list.get(h);
				if (i < 60 && actor.pos.getAbsPoint().distance(point3d) < 35000D && actor.pos.getAbsPoint().distance(point3d) > 3000D && (actor instanceof Aircraft) && actor.getArmy() != getArmy() && !launched && radaralive
						&& ((Tuple3d) (actor.pos.getAbsPoint())).z > 200D) {
					_V1.sub(actor.pos.getAbsPoint(), super.pos.getAbsPoint());
					super.pos.getAbsOrient().transformInv(_V1);
					float f = 57.32484F * (float) Math.atan2(((Tuple3d) (_V1)).y, -((Tuple3d) (_V1)).x);
					int k = (int) f + 180;
					if (k > 360)
						k -= 360;
					if (k > 300 && k < 360 || k < 60) {
						point3d.z += 5D;
						setMesh("3do/SAM/S-75/hier2.him");
						launched = true;
						SA2 sa2 = new SA2(this, netchannel, 1, point3d, orient, 100F);
						sa2.start(60F, 0);
					}
				}
			}

		}

		public void CheckRadar() {
			Point3d point3d = new Point3d();
			super.pos.getAbs(point3d);
			List list = Engine.targets();
			int i = list.size();
			for (int j = 0; j < i; j++) {
				Actor actor = (Actor) list.get(j);
				if ((actor instanceof com.maddox.il2.objects.vehicles.stationary.StationarySAM.S_75_radar) && actor.pos.getAbsPoint().distance(point3d) < 2000D && actor.getArmy() == getArmy())
					radaralive = true;
				else if (!(actor instanceof com.maddox.il2.objects.vehicles.stationary.StationarySAM.S_75_radar) && actor.pos.getAbsPoint().distance(point3d) < 2000D && actor.getArmy() == getArmy())
					radaralive = false;
			}

		}

		public boolean radaralive;
		private boolean launched;
		private static Vector3d _V1 = new Vector3d();
		public NetChannel netchannel;

		public S_75() {
			launched = false;
			radaralive = true;
			netchannel = null;
		}
	}

	static {
		new ArtilleryGeneric.SPAWN(ArtillerySAM.S_75.class);
	}
}
