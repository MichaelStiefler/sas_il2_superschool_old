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
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class Beaufort2 extends Beaufort {

	public Beaufort2() {
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

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 13: // '\r'
			killPilot(this, 1);
			FM.turret[1].bIsOperable = false;
			break;
		}
		return super.cutFM(i, j, actor);
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.11F, 0.89F, 0.0F, -100F), 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, cvt(f, 0.01F, 0.29F, 0.0F, -63F), 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f, 0.01F, 0.29F, 0.0F, -58F), 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, cvt(f, 0.01F, 0.29F, 0.0F, -63F), 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, cvt(f, 0.01F, 0.29F, 0.0F, -58F), 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f, 0.21F, 0.99F, 0.0F, -100F), 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, cvt(f, 0.11F, 0.39F, 0.0F, -63F), 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f, 0.11F, 0.39F, 0.0F, -58F), 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, cvt(f, 0.11F, 0.39F, 0.0F, -63F), 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, cvt(f, 0.11F, 0.39F, 0.0F, -58F), 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(hierMesh(), f);
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

			hierMesh().chunkSetAngles("Water" + (20 + 20 * i) + "a_D0", 0.0F, -20F * f1, 0.0F);
			hierMesh().chunkSetAngles("Water" + (20 + 20 * i) + "b_D0", 0.0F, -20F * f1, 0.0F);
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

		case 1: // '\001'
			if (f1 > 20F) {
				f1 = 20F;
				flag = false;
			}
			if (f1 < -20F) {
				f1 = -20F;
				flag = false;
			}
			if (f > 15F) {
				f = 15F;
				flag = false;
			}
			if (f < -15F) {
				f = -15F;
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
		case 1: // '\001'
			FM.turret[1].setHealth(f);
			break;

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
        boolean flag1 = false;
        int i = Mission.getMissionDate(true);
        if(i > 0)
            if(i > 0x128a5db)
                flag = true;
            else
            if(i < 0x1287a70)
                flag1 = true;
        if(regiment.country().equals("gb"))
        {
            if(World.cur().camouflage == 3 || World.cur().camouflage == 6)
            {
                if(flag)
                    return "gb_latewar_";
                if(flag1)
                    return "gb_early_";
            }
            return "gb_";
        }
        if(flag)
            return "latewar_";
        if(flag1)
            return "early_";
        else
            return "";
    }

	static {
		Class class1 = com.maddox.il2.objects.air.Beaufort2.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Beaufort");
		Property.set(class1, "meshName", "3DO/Plane/Beaufort2/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar09());
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Beaufort_MkII.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitBEAUFORT.class, CockpitBEAUFORT_Bombardier.class, CockpitBEAUFORT_NGunner.class, CockpitBEAUFORT_TGunner.class });
		Property.set(class1, "LOSElevation", 0.7394F);
		weaponTriggersRegister(class1, new int[] { 10, 10, 1, 11, 11, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_ExternalBomb01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08",
				"_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14" });
        weaponsRegister(class1, "default", new String[] {
                "MGunBrowning303t 950", "MGunBrowning303t 950", "MGunBrowning303k 400", "MGunBrowning303t 250", "MGunBrowning303t 250", null, null, null, null, null, 
                null, null, null, null, null, null, null, null, null, null
            });
            weaponsRegister(class1, "8x250lb", new String[] {
                "MGunBrowning303t 950", "MGunBrowning303t 950", "MGunBrowning303k 400", "MGunBrowning303t 250", "MGunBrowning303t 250", null, "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", 
                "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", null, null, null, null, null, null
            });
            weaponsRegister(class1, "4x500lb", new String[] {
                "MGunBrowning303t 950", "MGunBrowning303t 950", "MGunBrowning303k 400", "MGunBrowning303t 250", "MGunBrowning303t 250", null, null, null, null, null, 
                null, null, null, null, "BombGun500lbsE 1", "BombGun500lbsE 1", "BombGun500lbsE 1", "BombGun500lbsE 1", null, null
            });
            weaponsRegister(class1, "2x1000lb", new String[] {
                "MGunBrowning303t 950", "MGunBrowning303t 950", "MGunBrowning303k 400", "MGunBrowning303t 250", "MGunBrowning303t 250", null, null, null, null, null, 
                null, null, null, null, null, null, null, null, "BombGun1000lbsGPE 1", "BombGun1000lbsGPE 1"
            });
            weaponsRegister(class1, "1xtorp", new String[] {
                "MGunBrowning303t 950", "MGunBrowning303t 950", "MGunBrowning303k 400", "MGunBrowning303t 250", "MGunBrowning303t 250", "BombGunTorpMk12", null, null, null, null, 
                null, null, null, null, null, null, null, null, null, null
            });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}