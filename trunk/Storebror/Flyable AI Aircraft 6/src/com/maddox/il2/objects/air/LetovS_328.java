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

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.Mission;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class LetovS_328 extends Letov {

	public LetovS_328() {
	}

	public static String getSkinPrefix(String s, Regiment regiment) {
		if (regiment == null || regiment.country() == null)
			return "";
		if (regiment.country().equals("sk")) {
			int i = Mission.getMissionDate(true);
			if (i > 0) {
				if (i < 0x127e1b5)
					return "CSR_";
				if (i < 0x1280929)
					return "SKvsPL_";
				if (i > 0x128a4bd)
					return "SNP_";
			}
		}
		return "";
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

	public void update(float f) {
		if (!this.FM.turret[0].bIsAIControlled)
			this.gunOutOverride = 1;
		super.update(f);
	}

	static {
		Class class1 = LetovS_328.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "S-328");
		Property.set(class1, "meshName", "3do/Plane/LetovS-328/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar00s());
		Property.set(class1, "meshName_de", "3do/Plane/LetovS-328_DE/hier.him");
		Property.set(class1, "PaintScheme_de", new PaintSchemeBMPar00s());
		Property.set(class1, "meshName_sk", "3do/Plane/LetovS-328_SK/hier.him");
		Property.set(class1, "PaintScheme_sk", new PaintSchemeBMPar00s());
		Property.set(class1, "yearService", 1935F);
		Property.set(class1, "yearExpired", 1950F);
		Property.set(class1, "FlightModel", "FlightModels/LetovS-328.fmd");
		Property.set(class1, "originCountry", PaintScheme.countrySlovakia);
		Property.set(class1, "cockpitClass", new Class[] { CockpitLetovS_328.class, CockpitLetovS_328_Gunner.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 10, 10, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08",
				"_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07",
				"_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17" });
		weaponsRegister(class1, "default", new String[] { "MGunVz30sS328 400", "MGunVz30sS328 400", "MGunVz30t 420", "MGunVz30t 420", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "8*10kg", new String[] { "MGunVz30sS328 400", "MGunVz30sS328 400", "MGunVz30t 420", "MGunVz30t 420", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1",
				null, null, null, null, null, "BombGun10kgCZ", "BombGun10kgCZ", "BombGun10kgCZ", "BombGun10kgCZ", "BombGun10kgCZ", "BombGun10kgCZ", "BombGun10kgCZ", "BombGun10kgCZ", null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "4*20kg", new String[] { "MGunVz30sS328 400", "MGunVz30sS328 400", "MGunVz30t 420", "MGunVz30t 420", null, null, "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", null, null, null,
				null, null, null, null, "BombGun20kgCZ", "BombGun20kgCZ", null, null, "BombGun20kgCZ", "BombGun20kgCZ", null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "6*20kg", new String[] { "MGunVz30sS328 400", "MGunVz30sS328 400", "MGunVz30t 420", "MGunVz30t 420", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1",
				null, null, null, null, null, "BombGun20kgCZ", "BombGun20kgCZ", "BombGun20kgCZ", "BombGun20kgCZ", null, null, "BombGun20kgCZ", "BombGun20kgCZ", null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2*20kg+6*10kg", new String[] { "MGunVz30sS328 400", "MGunVz30sS328 400", "MGunVz30t 420", "MGunVz30t 420", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1",
				"PylonS328 1", null, null, null, null, null, "BombGun20kgCZ", "BombGun20kgCZ", "BombGun10kgCZ", "BombGun10kgCZ", "BombGun10kgCZ", "BombGun10kgCZ", "BombGun10kgCZ", "BombGun10kgCZ", null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2*50kgCZ", new String[] { "MGunVz30sS328 400", "MGunVz30sS328 400", "MGunVz30t 420", "MGunVz30t 420", "PylonS328 1", "PylonS328 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, "BombGun50kgCZ", "BombGun50kgCZ", null });
		weaponsRegister(class1, "1*100kgCZ", new String[] { "MGunVz30sS328 400", "MGunVz30sS328 400", "MGunVz30t 420", "MGunVz30t 420", "PylonS328 1", "PylonS328 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, "BombGun100kgCZ" });
		weaponsRegister(class1, "2*50kg", new String[] { "MGunVz30sS328 400", "MGunVz30sS328 400", "MGunVz30t 420", "MGunVz30t 420", "PylonS328 1", "PylonS328 1", null, null, null, null, null, null, null, null, "BombGun50kg", "BombGun50kg", null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "1*100kg", new String[] { "MGunVz30sS328 400", "MGunVz30sS328 400", "MGunVz30t 420", "MGunVz30t 420", "PylonS328 1", "PylonS328 1", null, null, null, null, null, null, null, null, null, null, "BombGun100kg", null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "1*ParaFlare", new String[] { "MGunVz30sS328 400", "MGunVz30sS328 400", "MGunVz30t 420", "MGunVz30t 420", "PylonS328 1", "PylonS328 1", null, null, null, null, null, null, "BombGunParaFlare", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2*ParaFlare", new String[] { "MGunVz30sS328 400", "MGunVz30sS328 400", "MGunVz30t 420", "MGunVz30t 420", "PylonS328 1", "PylonS328 1", null, null, null, null, null, null, "BombGunParaFlare", "BombGunParaFlare", null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "1*ParaFlare+2*20kg", new String[] { "MGunVz30sS328 400", "MGunVz30sS328 400", "MGunVz30t 420", "MGunVz30t 420", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1",
				"PylonS328 1", "BombGunParaFlare", null, null, null, null, null, null, "BombGun20kgCZ", "BombGun20kgCZ", null, null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2*ParaFlare+6*10kg", new String[] { "MGunVz30sS328 400", "MGunVz30sS328 400", "MGunVz30t 420", "MGunVz30t 420", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1", "PylonS328 1",
				"PylonS328 1", "BombGunParaFlare", "BombGunParaFlare", null, null, null, null, null, "BombGun10kgCZ", "BombGun10kgCZ", "BombGun10kgCZ", "BombGun10kgCZ", "BombGun10kgCZ", "BombGun10kgCZ", null, null, null, null, null, null, null, null,
				null });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null });
	}
}
