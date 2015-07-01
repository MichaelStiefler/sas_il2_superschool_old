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

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class MC_202 extends Scheme1 implements TypeFighter {

	public MC_202() {
	}

	public void doMurderPilot(int i) {
		switch (i) {
		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("Head1_D0", false);
			hierMesh().chunkVisible("HMask1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			break;
		}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (FM.getAltitude() < 3000F)
			hierMesh().chunkVisible("HMask1_D0", false);
		else
			hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -88F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -88F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -100F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -100F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -114F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, -114F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Math.max(-f * 1500F, -80F), 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Math.max(-f1 * 1500F, -80F), 0.0F);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(hierMesh(), f, f1, f2);
	}

	protected void moveFlap(float f) {
		float f1 = -45F * f;
		hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	static {
		Class class1 = MC_202.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "M.C.202");
		Property.set(class1, "meshNameDemo", "3DO/Plane/MC-202/hier.him");
		Property.set(class1, "meshName_it", "3DO/Plane/MC-202/hier.him");
		Property.set(class1, "PaintScheme_it", new PaintSchemeFCSPar01());
		Property.set(class1, "meshName", "3DO/Plane/MC-202(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "originCountry", PaintScheme.countryItaly);
		Property.set(class1, "yearService", 1942F);
		Property.set(class1, "yearExpired", 1948.5F);
		Property.set(class1, "FlightModel", "FlightModels/MC-202.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitMC_202.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
		weaponsRegister(class1, "default", new String[] { "MGunBredaSAFAT127si 370", "MGunBredaSAFAT127si 370", "MGunBredaSAFAT77k 500", "MGunBredaSAFAT77k 500" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null });
	}
}
