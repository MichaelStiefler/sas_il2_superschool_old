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
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class IL_4_DB3F extends IL_4 {

	public IL_4_DB3F() {
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.crew = 4;
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 0: // '\0'
			if (f < -27F) {
				f = -27F;
				flag = false;
			}
			if (f > 27F) {
				f = 27F;
				flag = false;
			}
			if (f1 < -30F) {
				f1 = -30F;
				flag = false;
			}
			if (f1 > 30F) {
				f1 = 30F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f < 2.0F && f > -2F && f1 < 25F)
				flag = false;
			if (f1 < -10F) {
				f1 = -10F;
				flag = false;
			}
			if (f1 > 99F) {
				f1 = 99F;
				flag = false;
			}
			break;

		case 2: // '\002'
			if (f < -25F) {
				f = -25F;
				flag = false;
			}
			if (f > 25F) {
				f = 25F;
				flag = false;
			}
			if (f1 < -90F) {
				f1 = -90F;
				flag = false;
			}
			if (f1 > 6F) {
				f1 = 6F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		float f3 = Math.max(-f * 1100F, -75F);
		float f4 = Math.max(-f1 * 1100F, -75F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -100F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -f3, 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, f3, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -100F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f4, 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, -f4, 0.0F);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(hierMesh(), f, f1, f2);
	}

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    public static boolean bChangedPit = false;

	static {
		Class class1 = IL_4_DB3F.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "DB-3");
		Property.set(class1, "meshName", "3DO/Plane/DB-3F/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
		Property.set(class1, "yearService", 1936F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/DB-3F.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitDB3F.class, CockpitDB3F_Bombardier.class, CockpitDB3F_FGunner.class, CockpitDB3F_TGunner.class, CockpitDB3F_BGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 11, 12, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_BombSpawn01", "_BombSpawn02" });
		weaponsRegister(class1, "default", new String[] { "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", null, null, null, null, null, null });
		weaponsRegister(class1, "10fab50", new String[] { "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", null, null, null, null, "BombGunFAB50 5", "BombGunFAB50 5" });
		weaponsRegister(class1, "10fab100", new String[] { "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", null, null, null, null, "BombGunFAB100 5", "BombGunFAB100 5" });
		weaponsRegister(class1, "3fab250", new String[] { "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", "BombGunFAB250", "BombGunNull", "BombGunFAB250", "BombGunFAB250", null, null });
		weaponsRegister(class1, "3fab25010fab50", new String[] { "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", "BombGunFAB250", "BombGunNull", "BombGunFAB250", "BombGunFAB250", "BombGunFAB50 5", "BombGunFAB50 5" });
		weaponsRegister(class1, "3fab25010fab100", new String[] { "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", "BombGunFAB250", "BombGunNull", "BombGunFAB250", "BombGunFAB250", "BombGunFAB100 5", "BombGunFAB100 5" });
		weaponsRegister(class1, "1fab500", new String[] { "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", "BombGunFAB500", null, null, null, null, null });
		weaponsRegister(class1, "1fab5002fab250", new String[] { "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", "BombGunFAB500", "BombGunNull", "BombGunFAB250", "BombGunFAB250", null, null });
		weaponsRegister(class1, "3fab500", new String[] { "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", "BombGunFAB500", "BombGunNull", "BombGunFAB500", "BombGunFAB500", null, null });
		weaponsRegister(class1, "3fab50010fab50", new String[] { "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", "BombGunFAB500", "BombGunNull", "BombGunFAB500", "BombGunFAB500", "BombGunFAB50 5", "BombGunFAB50 5" });
		weaponsRegister(class1, "1fab1000", new String[] { "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", "BombGunFAB1000", null, null, null, null, null });
		weaponsRegister(class1, "1fab100010fab50", new String[] { "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", "BombGunFAB1000", "BombGunNull", null, null, "BombGunFAB50 5", "BombGunFAB50 5" });
        weaponsRegister(class1, "1rrab2", new String[] {
                "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", "BombGunRRAB2", null, null, null, null, null
            });
            weaponsRegister(class1, "2rrab3", new String[] {
                "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", null, null, "BombGunRRAB3", "BombGunRRAB3", null, null
            });
            weaponsRegister(class1, "3rrab3", new String[] {
                "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", "BombGunRRAB3", "BombGunNull", "BombGunRRAB3", "BombGunRRAB3", null, null
            });
            weaponsRegister(class1, "6sab55100", new String[] {
                "MGunShKASt 1200", "MGunShKASt 1200", "MGunShKASt 1200", null, null, null, null, "BombGunSAB55100 3", "BombGunSAB55100 3"
            });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null });
	}
}