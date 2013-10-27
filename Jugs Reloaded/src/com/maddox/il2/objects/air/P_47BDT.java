package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class P_47BDT extends P_47ModPack {

	protected void moveFan(float f) {
		if (!Config.isUSE_RENDER())
			return;
		switch (FM.EI.engines[0].getStage()) {
			case 1:
			case 2:
			case 3:
				f = 0.0F;
				break;
			case 4:
				f = -150F;
				break;
			case 5:
				f = -400F;
				break;
			default:
				break;
		}
//		if (engineState >= 1 && engineState <= 3)
//			f = 0.0F;
//		if (engineState == 4)
//			f = -150F;
//		if (engineState == 5)
//			f = -400F;
		hierMesh().chunkFind(Aircraft.Props[1][0]);
		int i = 0;
		for (int j = 0; j < 2; j++) {
			if (oldProp[j] < 2) {
				i = Math.abs((int) (FM.EI.engines[0].getw() * 0.06F));
				if (i >= 1)
					i = 1;
				if (i != oldProp[j] && hierMesh().isChunkVisible(Aircraft.Props[j][oldProp[j]])) {
					hierMesh().chunkVisible(Aircraft.Props[j][oldProp[j]], false);
					oldProp[j] = i;
					hierMesh().chunkVisible(Aircraft.Props[j][i], true);
				}
			}
			if (i == 0) {
				propPos[j] = (propPos[j] + 57.3F * FM.EI.engines[0].getw() * f) % 360F;
			} else {
				float f1 = 57.3F * FM.EI.engines[0].getw();
				f1 %= 2880F;
				f1 /= 2880F;
				if (f1 <= 0.5F)
					f1 *= 2.0F;
				else
					f1 = f1 * 2.0F - 2.0F;
				f1 *= 1200F;
				propPos[j] = (propPos[j] + f1 * f) % 360F;
			}
			if (j == 0)
				hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, propPos[j], 0.0F);
			else
				hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, -propPos[j], 0.0F);
		}
	}

	public void hitProp(int i, int j, Actor actor) {
		if (i > FM.EI.getNum() - 1 || oldProp[i] == 2)
			return;
		if (isChunkAnyDamageVisible("Prop" + (i + 1)) || isChunkAnyDamageVisible("PropRot" + (i + 1))) {
			hierMesh().chunkVisible(Aircraft.Props[i + 1][0], false);
			hierMesh().chunkVisible(Aircraft.Props[i + 1][1], false);
			hierMesh().chunkVisible(Aircraft.Props[i + 1][2], true);
		}
		super.hitProp(i, j, actor);
	}

	public void update(float f) {
		if (this == World.getPlayerAircraft()) World.cur().diffCur.Torque_N_Gyro_Effects = false;
		super.update(f);
	}
	static {
		Class class1 = P_47BDT.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "P-47");
		Property.set(class1, "meshName", "3DO/Plane/P-47B-DT(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
		Property.set(class1, "meshName_us", "3DO/Plane/P-47B-DT(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 1947.5F);
		Property.set(class1, "FlightModel", "FlightModels/P-47B15.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitP_47D10.class });
		Property.set(class1, "LOSElevation", 0.9879F);
		Property.set(class1, "StockOrdnanceAvailable", 0);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 0, 0 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08" });
	}
}
