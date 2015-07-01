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

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class B_17G extends B_17 {

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 19: // '\023'
			killPilot(this, 4);
			break;
		}
		return super.cutFM(i, j, actor);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		for (int i = 1; i < 7; i++)
			if (FM.getAltitude() < 3000F)
				hierMesh().chunkVisible("HMask" + i + "_D0", false);
			else
				hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.AS.wantBeaconsNet(true);
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

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 0: // '\0'
			if (f < -70F) {
				f = -70F;
				flag = false;
			}
			if (f > 70F) {
				f = 70F;
				flag = false;
			}
			if (f1 < -45F) {
				f1 = -45F;
				flag = false;
			}
			if (f1 > 20F) {
				f1 = 20F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f < -20F) {
				f = -20F;
				flag = false;
			}
			if (f > 20F) {
				f = 20F;
				flag = false;
			}
			if (f1 < -20F) {
				f1 = -20F;
				flag = false;
			}
			if (f1 > 20F) {
				f1 = 20F;
				flag = false;
			}
			break;

		case 2: // '\002'
			if (f < -20F) {
				f = -20F;
				flag = false;
			}
			if (f > 20F) {
				f = 20F;
				flag = false;
			}
			if (f1 < -20F) {
				f1 = -20F;
				flag = false;
			}
			if (f1 > 20F) {
				f1 = 20F;
				flag = false;
			}
			break;

		case 3: // '\003'
			if (f1 < -3F) {
				f1 = -3F;
				flag = false;
			}
			if (f1 > 66F) {
				f1 = 66F;
				flag = false;
			}
			break;

		case 4: // '\004'
			if (f1 < -75F) {
				f1 = -75F;
				flag = false;
			}
			if (f1 > 6F) {
				f1 = 6F;
				flag = false;
			}
			break;

		case 5: // '\005'
			if (f < -32F) {
				f = -32F;
				flag = false;
			}
			if (f > 84F) {
				f = 84F;
				flag = false;
			}
			if (f1 < -17F) {
				f1 = -17F;
				flag = false;
			}
			if (f1 > 43F) {
				f1 = 43F;
				flag = false;
			}
			break;

		case 6: // '\006'
			if (f < -80F) {
				f = -80F;
				flag = false;
			}
			if (f > 39F) {
				f = 39F;
				flag = false;
			}
			if (f1 < -28F) {
				f1 = -28F;
				flag = false;
			}
			if (f1 > 40F) {
				f1 = 40F;
				flag = false;
			}
			break;

		case 7: // '\007'
			if (f < -25F) {
				f = -25F;
				flag = false;
			}
			if (f > 25F) {
				f = 25F;
				flag = false;
			}
			if (f1 < -25F) {
				f1 = -25F;
				flag = false;
			}
			if (f1 > 25F) {
				f1 = 25F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
		case 2: // '\002'
			FM.turret[0].setHealth(f);
			break;

		case 3: // '\003'
			FM.turret[1].setHealth(f);
			break;

		case 4: // '\004'
			FM.turret[2].setHealth(f);
			break;

		case 5: // '\005'
			FM.turret[3].setHealth(f);
			FM.turret[4].setHealth(f);
			break;
		}
	}

	public static boolean bChangedPit = false;

	static {
		Class class1 = B_17G.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "B-17");
		Property.set(class1, "meshNameDemo", "3DO/Plane/B-17G(USA)/hier.him");
		Property.set(class1, "meshName", "3DO/Plane/B-17G(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar04());
		Property.set(class1, "meshName_us", "3DO/Plane/B-17G(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar04());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1943.5F);
		Property.set(class1, "yearExpired", 2800.9F);
		Property.set(class1, "FlightModel", "FlightModels/B-17G.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitB17G.class, CockpitB17G_Bombardier.class, CockpitB17G_FGunner.class, CockpitB17G_FLGunner.class, CockpitB17G_FRGunner.class, CockpitB17G_TGunner.class, CockpitB17G_AGunner.class,
				CockpitB17G_BGunner.class, CockpitB17G_LGunner.class, CockpitB17G_RGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 10, 11, 12, 13, 13, 14, 14, 15, 16, 17, 17, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN10", "_MGUN09", "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning50t 365", "MGunBrowning50t 365", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 500", "MGunBrowning50t 500",
				"MGunBrowning50t 600", "MGunBrowning50t 600", "MGunBrowning50t 500", "MGunBrowning50t 500", null, null });
		weaponsRegister(class1, "16x20FragClusters", new String[] { "MGunBrowning50t 365", "MGunBrowning50t 365", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 500", "MGunBrowning50t 500",
				"MGunBrowning50t 600", "MGunBrowning50t 600", "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGunM26A2 8", "BombGunM26A2 8" });
		weaponsRegister(class1, "16x500", new String[] { "MGunBrowning50t 365", "MGunBrowning50t 365", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 500", "MGunBrowning50t 500",
				"MGunBrowning50t 600", "MGunBrowning50t 600", "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun500lbs 8", "BombGun500lbs 8" });
		weaponsRegister(class1, "8x1000", new String[] { "MGunBrowning50t 365", "MGunBrowning50t 365", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375", "MGunBrowning50t 500", "MGunBrowning50t 500",
				"MGunBrowning50t 600", "MGunBrowning50t 600", "MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun1000lbs 4", "BombGun1000lbs 4" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
