// This file is part of the SAS IL-2 Sturmovik 1946 4.13
// Flyable AI Aircraft Mod package.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Original file source: 1C/Maddox/TD
// Modified by: SAS - Special Aircraft Services
//              www.sas1946.com
//
// Last Edited by: SAS~Storebror
// Last Edited on: 2014/10/28

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class B_25C25 extends B_25 {

	public B_25C25() {
		bpos = 1.0F;
		bcurpos = 1.0F;
		btme = -1L;
	}

	public static String getSkinPrefix(String s, Regiment regiment) {
		if (regiment == null || regiment.country() == null)
			return "";
		if (regiment.country().equals("gb")) {
			int i = Mission.getMissionDate(true);
			if (i > 0 && i < 0x128a3de)
				return "PreInvasion_";
		}
		return "";
	}

	public void update(float f) {
		super.update(f);
		if (!FM.AS.isMaster())
			return;
		if (bpos == 0.0F) {
			if (bcurpos > bpos) {
				bcurpos -= 0.2F * f;
				if (bcurpos < 0.0F)
					bcurpos = 0.0F;
			}
			resetYPRmodifier();
			xyz[1] = -0.31F + 0.31F * bcurpos;
			hierMesh().chunkSetLocate("Turret3A_D0", xyz, ypr);
		} else if (bpos == 1.0F) {
			if (bcurpos < bpos) {
				bcurpos += 0.2F * f;
				if (bcurpos > 1.0F) {
					bcurpos = 1.0F;
					bpos = 0.5F;
					FM.turret[2].bIsOperable = true;
				}
			}
			resetYPRmodifier();
			xyz[1] = -0.3F + 0.3F * bcurpos;
			hierMesh().chunkSetLocate("Turret3A_D0", xyz, ypr);
		}
		if (Time.current() > btme) {
			btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
			if (FM.turret[2].target == null) {
				FM.turret[2].bIsOperable = false;
				bpos = 0.0F;
			}
			if (FM.turret[1].target != null && FM.AS.astatePilotStates[4] < 90)
				bpos = 1.0F;
		}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 19: // '\023'
			killPilot(this, 4);
			break;
		}
		return super.cutFM(i, j, actor);
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.AS.wantBeaconsNet(true);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		for (int i = 1; i < 6; i++)
			if (FM.getAltitude() < 3000F)
				hierMesh().chunkVisible("HMask" + i + "_D0", false);
			else
				hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
		case 2: // '\002'
			FM.turret[0].setHealth(f);
			break;

		case 3: // '\003'
			FM.turret[1].setHealth(f);
			FM.turret[2].setHealth(f);
			break;
		}
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 0: // '\0'
			if (f < -23F) {
				f = -23F;
				flag = false;
			}
			if (f > 23F) {
				f = 23F;
				flag = false;
			}
			if (f1 < -25F) {
				f1 = -25F;
				flag = false;
			}
			if (f1 > 15F) {
				f1 = 15F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f1 < 0.0F) {
				f1 = 0.0F;
				flag = false;
			}
			if (f1 > 88F) {
				f1 = 88F;
				flag = false;
			}
			break;

		case 2: // '\002'
			if (f1 < -88F) {
				f1 = -88F;
				flag = false;
			}
			if (f1 > 2.0F) {
				f1 = 2.0F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	private float bpos;
	private float bcurpos;
	private long btme;
	public static boolean bChangedPit = false;

	static {
		Class class1 = B_25C25.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "B-25");
		Property.set(class1, "meshName", "3DO/Plane/B-25C-25(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "meshName_ru", "3DO/Plane/B-25C-25(ru)/hier.him");
		Property.set(class1, "PaintScheme_ru", new PaintSchemeBMPar02());
		Property.set(class1, "meshName_us", "3DO/Plane/B-25C-25(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar02());
		Property.set(class1, "yearService", 1943F);
		Property.set(class1, "yearExpired", 1956.6F);
		Property.set(class1, "FlightModel", "FlightModels/B-25C.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitB25C25.class, CockpitB25C25_Copilot.class, CockpitB25C25_Bombardier.class, CockpitB25C25_FGunner.class, CockpitB25C25_TGunner.class });
		weaponTriggersRegister(class1, new int[] { 0, 10, 11, 11, 12, 12, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning50ki 400", "MGunBrowning50t 400", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50tj 450", "MGunBrowning50tj 450", null, null, null });
		weaponsRegister(class1, "40xParaF", new String[] { "MGunBrowning50ki 400", "MGunBrowning50t 400", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50tj 450", "MGunBrowning50tj 450", null, "BombGunParafrag8 20", "BombGunParafrag8 20" });
		weaponsRegister(class1, "60xParaF", new String[] { "MGunBrowning50ki 400", "MGunBrowning50t 400", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50tj 450", "MGunBrowning50tj 450", "BombGunParafrag8 20", "BombGunParafrag8 20",
				"BombGunParafrag8 20" });
		weaponsRegister(class1, "4x20FragClusters", new String[] { "MGunBrowning50ki 400", "MGunBrowning50t 400", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50tj 450", "MGunBrowning50tj 450", null, "BombGunM26A2 2", "BombGunM26A2 2" });
		weaponsRegister(class1, "12x100lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50t 400", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50tj 450", "MGunBrowning50tj 450", null, "BombGun100lbs 6", "BombGun100lbs 6" });
		weaponsRegister(class1, "6x250lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50t 400", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50tj 450", "MGunBrowning50tj 450", null, "BombGun250lbs 3", "BombGun250lbs 3" });
		weaponsRegister(class1, "4x500lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50t 400", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50tj 450", "MGunBrowning50tj 450", null, "BombGun500lbs 2", "BombGun500lbs 2" });
		weaponsRegister(class1, "2x1000lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50t 400", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50tj 450", "MGunBrowning50tj 450", null, "BombGun1000lbs 1", "BombGun1000lbs 1" });
		weaponsRegister(class1, "1x2000lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50t 400", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50tj 450", "MGunBrowning50tj 450", "BombGun2000lbs 1", null, null });
		weaponsRegister(class1, "10x50kg", new String[] { "MGunBrowning50ki 400", "MGunBrowning50t 400", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50tj 450", "MGunBrowning50tj 450", null, "BombGunFAB50 5", "BombGunFAB50 5" });
		weaponsRegister(class1, "8x100kg", new String[] { "MGunBrowning50ki 400", "MGunBrowning50t 400", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50tj 450", "MGunBrowning50tj 450", null, "BombGunFAB100 4", "BombGunFAB100 4" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null });
	}
}
