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

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class U_2VS extends U_2 {

	public U_2VS() {
		bChangedPit = true;
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
		case 1: // '\001'
			FM.turret[0].setHealth(f);
			break;
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
		default:
			break;

		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("Head1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			if (!FM.AS.bIsAboutToBailout && World.cur().isHighGore())
				hierMesh().chunkVisible("Gore1_D0", true);
			break;

		case 1: // '\001'
			hierMesh().chunkVisible("Pilot2_D0", false);
			hierMesh().chunkVisible("Pilot2_D1", true);
			if (!FM.AS.bIsAboutToBailout && World.cur().isHighGore())
				hierMesh().chunkVisible("Gore2_D0", true);
			break;
		}
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

	public boolean bChangedPit;

	static {
		Class class1 = U_2VS.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "U-2");
		Property.set(class1, "meshName", "3do/plane/U-2VS/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "yearService", 1942F);
		Property.set(class1, "yearExpired", 1967.8F);
		Property.set(class1, "FlightModel", "FlightModels/U-2VS.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitU2VS.class, CockpitU2VS_TGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
		weaponsRegister(class1, "default", new String[] { "MGunUBt 250", null, null, null, null });
		weaponsRegister(class1, "2ao10", new String[] { "MGunUBt 250", "BombGunAO10 1", "BombGunAO10 1", null, null });
		weaponsRegister(class1, "4ao10", new String[] { "MGunUBt 250", "BombGunAO10 1", "BombGunAO10 1", "BombGunAO10 1", "BombGunAO10 1" });
		weaponsRegister(class1, "2fab50", new String[] { "MGunUBt 250", "BombGunFAB50", "BombGunFAB50", null, null });
		weaponsRegister(class1, "4fab50", new String[] { "MGunUBt 250", "BombGunFAB50", "BombGunFAB50", "BombGunFAB50", "BombGunFAB50" });
		weaponsRegister(class1, "2fab100", new String[] { "MGunUBt 250", "BombGunFAB100", "BombGunFAB100", null, null });
		weaponsRegister(class1, "2x4", new String[] { "MGunUBt 250", "BombGunFAB100", "BombGunFAB100", "BombGunFAB50", "BombGunFAB50" });
        weaponsRegister(class1, "4fab100", new String[] {
                "MGunUBt 250", "BombGunFAB100", "BombGunFAB100", "BombGunFAB100", "BombGunFAB100"
            });
            weaponsRegister(class1, "2fab50+2sab3", new String[] {
                "MGunUBt 250", "BombGunSAB3m", "BombGunSAB3m", "BombGunFAB50", "BombGunFAB50"
            });
            weaponsRegister(class1, "2fab100+2sab15", new String[] {
                "MGunUBt 250", "BombGunSAB15", "BombGunSAB15", "BombGunFAB100", "BombGunFAB100"
            });
            weaponsRegister(class1, "4sab15", new String[] {
                "MGunUBt 250", "BombGunSAB15", "BombGunSAB15", "BombGunSAB15", "BombGunSAB15"
            });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null });
	}
}
