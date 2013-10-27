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

import com.maddox.rts.CLASS;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class Swordfish1 extends Swordfish {

	public Swordfish1() {
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

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Swordfish");
		Property.set(class1, "meshName", "3DO/Plane/Swordfish1(multi)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
		Property.set(class1, "meshName_gb", "3DO/Plane/Swordfish1(gb)/hier.him");
		Property.set(class1, "PaintScheme_gb", new PaintSchemeBMPar02f());
		Property.set(class1, "meshName_rn", "3DO/Plane/Swordfish1(gb)/hier.him");
		Property.set(class1, "PaintScheme_rn", new PaintSchemeBMPar02f());
		Property.set(class1, "yearService", 1936F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Swordfish.fmd");
		Property.set(class1, "originCountry", PaintScheme.countryBritain);
		Property.set(class1, "cockpitClass", new Class[] { CockpitSwordfish.class, CockpitSwordfish_TAG.class });
		weaponTriggersRegister(class1, new int[] { 0, 10, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_turret1", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10",
				"_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22",
				"_ExternalBomb23", "_ExternalBomb01", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalBomb27", "_ExternalBomb28", "_ExternalBomb29", "_ExternalBomb30", "_ExternalBomb31", "_ExternalBomb32", "_ExternalBomb33" });
		weaponsRegister(class1, "default", new String[] { "MGunVikkersKs 300", "MGunVikkersKt 600", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "1_1xTorpedo", new String[] { "MGunVikkersKs 300", "MGunVikkersKt 600", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "BombGunTorpMk12",
				null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2_3x500lb", new String[] { "MGunVikkersKs 300", "MGunVikkersKt 600", "BombGun500lbsE", "BombGun500lbsE", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, "BombGun500lbsE", null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "3_1x500lb+4x250lb", new String[] { "MGunVikkersKs 300", "MGunVikkersKt 600", null, null, "BombGun250lbsE", "BombGun250lbsE", "BombGun250lbsE", "BombGun250lbsE", null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, "BombGun500lbsE", null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "4_1x500lb+8x100lb", new String[] { "MGunVikkersKs 300", "MGunVikkersKt 600", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				"BombGun500lbsE", "BombGunNull", "BombGun50kg", "BombGun50kg", "BombGun50kg", "BombGun50kg", "BombGun50kg", "BombGun50kg", "BombGun50kg", "BombGun50kg" });
		weaponsRegister(class1, "5_3x500lb+8xflare", new String[] { "MGunVikkersKs 300", "MGunVikkersKt 600", "BombGun500lbsE", "BombGun500lbsE", null, null, null, null, "BombGunParaFlareUK", "BombGunNull", "BombGunNull", "BombGunParaFlareUK",
				"BombGunParaFlareUK", "BombGunNull", "BombGunNull", "BombGunParaFlareUK", "BombGunParaFlareUK", "BombGunNull", "BombGunNull", "BombGunParaFlareUK", "BombGunParaFlareUK", "BombGunNull", "BombGunNull", "BombGunParaFlareUK", null,
				"BombGun500lbsE", null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "6_1xtorpedo+8xflare", new String[] { "MGunVikkersKs 300", "MGunVikkersKt 600", null, null, null, null, null, null, "BombGunParaFlareUK", "BombGunNull", "BombGunNull", "BombGunParaFlareUK", "BombGunParaFlareUK",
				"BombGunNull", "BombGunNull", "BombGunParaFlareUK", "BombGunParaFlareUK", "BombGunNull", "BombGunNull", "BombGunParaFlareUK", "BombGunParaFlareUK", "BombGunNull", "BombGunNull", "BombGunParaFlareUK", "BombGunTorpMk12", null, null, null,
				null, null, null, null, null, null, null });
		weaponsRegister(class1, "7_1x500lb+4x250lb+8xflare", new String[] { "MGunVikkersKs 300", "MGunVikkersKt 600", null, null, "BombGun250lbsE", "BombGun250lbsE", "BombGun250lbsE", "BombGun250lbsE", "BombGunParaFlareUK", "BombGunNull", "BombGunNull",
				"BombGunParaFlareUK", "BombGunParaFlareUK", "BombGunNull", "BombGunNull", "BombGunParaFlareUK", "BombGunParaFlareUK", "BombGunNull", "BombGunNull", "BombGunParaFlareUK", "BombGunParaFlareUK", "BombGunNull", "BombGunNull",
				"BombGunParaFlareUK", null, "BombGun500lbsE", null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "8_1x500lb+8xFlare_AI", new String[] { "MGunVikkersKs 300", "MGunVikkersKt 600", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				"BombGun500lbsE", "BombGunNull", "BombGunParaFlareUK", "BombGunParaFlareUK", "BombGunParaFlareUK", "BombGunParaFlareUK", "BombGunParaFlareUK", "BombGunParaFlareUK", "BombGunParaFlareUK", "BombGunParaFlareUK" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null });
	}
}
