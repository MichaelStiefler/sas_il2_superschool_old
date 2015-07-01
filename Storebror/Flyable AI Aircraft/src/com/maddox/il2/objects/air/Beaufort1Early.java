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
// Last Edited at: 2013/06/12

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class Beaufort1Early extends Beaufort {

	public Beaufort1Early() {
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.Gears.computePlaneLandPose(FM);
		if (thisWeaponsName.startsWith("1x")) {
			hierMesh().chunkVisible("BayDoorL_D0", false);
			hierMesh().chunkVisible("BayDoorR_D0", false);
            bombBayDoorsRemoved = true;
		}
	}

	public void update(float f) {
		super.update(f);
		for (int i = 0; i < 2; i++) {
			float f1 = FM.EI.engines[i].getControlRadiator();
			if (Math.abs(flapps[i] - f1) <= 0.01F)
				continue;
			flapps[i] = f1;
			for (int j = 1; j < 21; j++)
				hierMesh().chunkSetAngles("Water" + (j + 20 * i) + "_D0", 0.0F, -20F * f1, 0.0F);

		}

	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.11F, 0.89F, 0.0F, -100F), 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, cvt(f, 0.01F, 0.29F, 0.0F, -63F), 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f, 0.01F, 0.29F, 0.0F, -58F), 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f, 0.21F, 0.99F, 0.0F, -100F), 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, cvt(f, 0.11F, 0.39F, 0.0F, -63F), 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f, 0.11F, 0.39F, 0.0F, -58F), 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(hierMesh(), f);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		case 0: // '\0'
			if (f1 > 70F) {
				f1 = 70F;
				flag = false;
			}
			if (f1 < -1F) {
				f1 = -1F;
				flag = false;
			}
			if (f > 90F) {
				f = 90F;
				flag = false;
			}
			if (f < -90F) {
				f = -90F;
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
		}
	}

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        if(regiment == null || regiment.country() == null)
            return "";
        boolean flag = false;
        int i = Mission.getMissionDate(true);
        if(i > 0 && i > 0x1285681)
            flag = true;
        if(regiment.country().equals("gb"))
            if(flag)
                return "gb_latewar_";
            else
                return "gb_";
        if(flag)
            return "latewar_";
        else
            return "";
    }

	static {
		Class class1 = com.maddox.il2.objects.air.Beaufort1Early.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Beaufort");
		Property.set(class1, "meshName", "3DO/Plane/Beaufort/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar09gb());
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Beaufort_MkI_early.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitBEAUFORT.class, CockpitBEAUFORT_Bombardier.class, CockpitBEAUFORT_TGunner.class });
		Property.set(class1, "LOSElevation", 0.7394F);
		weaponTriggersRegister(class1, new int[] { 10, 10, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09",
				"_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14" });
        weaponsRegister(class1, "default", new String[] {
                "MGunBrowning303t 950", "MGunBrowning303t 950", "MGunBrowning303k 400", null, null, null, null, null, null, null, 
                null, null, null, null, null, null, null, null
            });
            weaponsRegister(class1, "8x250lb", new String[] {
                "MGunBrowning303t 950", "MGunBrowning303t 950", "MGunBrowning303k 400", null, "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", 
                "BombGun250lbsE 1", "BombGun250lbsE 1", null, null, null, null, null, null
            });
            weaponsRegister(class1, "4x500lb", new String[] {
                "MGunBrowning303t 950", "MGunBrowning303t 950", "MGunBrowning303k 400", null, null, null, null, null, null, null, 
                null, null, "BombGun500lbsE 1", "BombGun500lbsE 1", "BombGun500lbsE 1", "BombGun500lbsE 1", null, null
            });
            weaponsRegister(class1, "2x1000lb", new String[] {
                "MGunBrowning303t 950", "MGunBrowning303t 950", "MGunBrowning303k 400", null, null, null, null, null, null, null, 
                null, null, null, null, null, null, "BombGun1000lbsGPE 1", "BombGun1000lbsGPE 1"
            });
            weaponsRegister(class1, "1xtorp", new String[] {
                "MGunBrowning303t 950", "MGunBrowning303t 950", "MGunBrowning303k 400", "BombGunTorpMk12", null, null, null, null, null, null, 
                null, null, null, null, null, null, null, null
            });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
