// This file is part of the SAS IL-2 Sturmovik 1946 4.12
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
// Last Edited at: 2013/06/11

package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class B_25H1 extends B_25 implements TypeBomber, TypeStormovik, TypeStormovikArmored {

	public B_25H1() {
		fSightCurForwardAngle = 0.0F;
		fSightCurSideslip = 0.0F;
		fSightCurAltitude = 3000F;
		fSightCurSpeed = 200F;
		fSightCurReadyness = 0.0F;
		bChangedPit = false;
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		for (int i = 1; i < 7; i++)
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

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.AS.wantBeaconsNet(true);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 0: // '\0'
			return false;

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
			if (f < -38F) {
				f = -38F;
				flag = false;
			}
			if (f > 38F) {
				f = 38F;
				flag = false;
			}
			if (f1 < -41F) {
				f1 = -41F;
				flag = false;
			}
			if (f1 > 43F) {
				f1 = 43F;
				flag = false;
			}
			break;

		case 3: // '\003'
			if (f < -85F) {
				f = -85F;
				flag = false;
			}
			if (f > 22F) {
				f = 22F;
				flag = false;
			}
			if (f1 < -40F) {
				f1 = -40F;
				flag = false;
			}
			if (f1 > 32F) {
				f1 = 32F;
				flag = false;
			}
			break;

		case 4: // '\004'
			if (f < -37F) {
				f = -37F;
				flag = false;
			}
			if (f > 70F) {
				f = 70F;
				flag = false;
			}
			if (f1 < -40F) {
				f1 = -40F;
				flag = false;
			}
			if (f1 > 32F) {
				f1 = 32F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public boolean typeBomberToggleAutomation() {
		return false;
	}

	public void typeBomberAdjDistanceReset() {
	}

	public void typeBomberAdjDistancePlus() {
	}

	public void typeBomberAdjDistanceMinus() {
	}

	public void typeBomberAdjSideslipReset() {
	}

	public void typeBomberAdjSideslipPlus() {
	}

	public void typeBomberAdjSideslipMinus() {
	}

	public void typeBomberAdjAltitudeReset() {
	}

	public void typeBomberAdjAltitudePlus() {
	}

	public void typeBomberAdjAltitudeMinus() {
	}

	public void typeBomberAdjSpeedReset() {
	}

	public void typeBomberAdjSpeedPlus() {
	}

	public void typeBomberAdjSpeedMinus() {
	}

	public void typeBomberUpdate(float f) {
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 19: // '\023'
			killPilot(this, 4);
			break;
		}
		return super.cutFM(i, j, actor);
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
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
	public float fSightCurForwardAngle;
	public float fSightCurSideslip;
	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurReadyness;

	static {
		Class class1 = B_25H1.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "B-25");
		Property.set(class1, "meshName", "3DO/Plane/B-25H-1(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
		Property.set(class1, "meshName_us", "3DO/Plane/B-25H-1(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1943F);
		Property.set(class1, "yearExpired", 1956.6F);
		Property.set(class1, "FlightModel", "FlightModels/B-25H.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitB25H1.class, CockpitB25H_TGunner.class, CockpitB25H_AGunner.class, CockpitB25H_LGunner.class, CockpitB25H_RGunner.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 11, 11, 12, 12, 13, 14, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
		weaponHooksRegister(class1, new String[] { "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN13", "_MGUN14", "_MGUN15", "_MGUN16", "_CANNON01", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN11", "_MGUN12", "_ExternalDev01", "_ExternalDev02",
				"_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_BombSpawn02", "_BombSpawn03", "_BombSpawn01",
				"_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11",
				"_ExternalDev12", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "40xParaF", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, "BombGunParafrag8 20", "BombGunParafrag8 20", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "60xParaF", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, "BombGunParafrag8 20", "BombGunParafrag8 20", "BombGunParafrag8 20", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "6x20xParaF", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null, null, null,
				"BombGunM26A2 1", "BombGunM26A2 1", "BombGunM26A2 1", null, "BombGunM26A2 1", "BombGunM26A2 1", "BombGunM26A2 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "12x100lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, "BombGun100lbs 6", "BombGun100lbs 6", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "3x250lbs3x500lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null, null, null,
				"BombGun500lbs 1", "BombGun500lbs 1", "BombGun500lbs 1", "BombGunNull 1", "BombGun250lbs 1", "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null });
		weaponsRegister(class1, "3x250lbs2x1000lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunBrowning50k 400", "MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null,
				null, null, "BombGun1000lbs 1", "BombGun1000lbs 1", null, null, "BombGun250lbs 1", "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "3x250lbs1x1000lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunBrowning50k 400", "MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null,
				null, null, "BombGun500lbs 1", "BombGun500lbs 1", "BombGun1000lbs 1", "BombGunNull 1", "BombGun250lbs 1", "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null });
		weaponsRegister(class1, "6x250lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, "BombGun250lbs 3", "BombGun250lbs 3", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "8x250lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", "PylonB25PLN2", "PylonB25PLN2", "PylonB25PLN2", "PylonB25PLN2", "BombGun250lbs 1",
				"BombGun250lbs 1", "BombGun250lbs 1", "BombGun250lbs 1", "BombGun250lbs 1", "BombGun250lbs 1", "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "4x500lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, "BombGun500lbs 2", "BombGun500lbs 2", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "4x500lbs1x1000lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunBrowning50k 400", "MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, "BombGun1000lbs 1", "BombGunNull 1", null, null, null, null, "BombGun500lbs 2", "BombGun500lbs 2", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "6x500lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, "BombGun500lbs 3", "BombGun500lbs 3", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2x1000lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, "BombGun1000lbs 1", "BombGun1000lbs 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "3x1000lbs", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				"BombGun1000lbs 1", "BombGunNull 1", null, null, null, null, "BombGun1000lbs 1", "BombGun1000lbs 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "10x50kg", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, "BombGunFAB50 5", "BombGunFAB50 5", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "8x100kg", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, "BombGunFAB100 4", "BombGunFAB100 4", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2x250kg6x100kg", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", "PylonB25PLN2", "PylonB25PLN2", "PylonB25PLN2", "PylonB25PLN2", null, null, null,
				null, null, "BombGunFAB250 1", "BombGunFAB250 1", null, null, null, null, null, null, null, null, null, "BombGunFAB100 3", "BombGunFAB100 3", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "8xHVAR", new String[] { "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50ki 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400", "MGunBrowning50k 400",
				"MGunM4_75 21", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 450", "MGunBrowning50t 400", "MGunBrowning50t 400", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, "PylonB25RAIL", "PylonB25RAIL", "PylonB25RAIL", "PylonB25RAIL", "PylonB25RAIL", "PylonB25RAIL", "PylonB25RAIL", "PylonB25RAIL", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1",
				"RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1", "RocketGunHVAR5 1" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
