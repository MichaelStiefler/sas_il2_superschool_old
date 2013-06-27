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
// Last Edited at: 2013/01/22

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class L2D extends Scheme2 implements TypeTransport {

	public L2D() {
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -45F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -45F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 20F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 20F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -120F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -120F * f1, 0.0F);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(hierMesh(), f, f1, f2);
	}

	public void msgShot(Shot shot) {
		setShot(shot);
		if (shot.chunkName.startsWith("WingLOut") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(Pd.y) < 6D)
			FM.AS.hitTank(shot.initiator, 0, 1);
		if (shot.chunkName.startsWith("WingROut") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(Pd.y) < 6D)
			FM.AS.hitTank(shot.initiator, 3, 1);
		if (shot.chunkName.startsWith("WingLIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(Pd.y) < 1.940000057220459D)
			FM.AS.hitTank(shot.initiator, 1, 1);
		if (shot.chunkName.startsWith("WingRIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(Pd.y) < 1.940000057220459D)
			FM.AS.hitTank(shot.initiator, 2, 1);
		if (shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
			FM.AS.hitEngine(shot.initiator, 0, 1);
		if (shot.chunkName.startsWith("Engine2") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
			FM.AS.hitEngine(shot.initiator, 1, 1);
		if (shot.chunkName.startsWith("Nose") && Pd.x > 4.9000000953674316D && Pd.z > -0.090000003576278687D && World.Rnd().nextFloat() < 0.1F)
			if (Pd.y > 0.0D) {
				killPilot(shot.initiator, 0);
				FM.setCapableOfBMP(false, shot.initiator);
			} else {
				killPilot(shot.initiator, 1);
			}
		if (FM.AS.astateEngineStates[0] > 2 && FM.AS.astateEngineStates[1] > 2 && World.Rnd().nextInt(0, 99) < 33)
			FM.setCapableOfBMP(false, shot.initiator);
		super.msgShot(shot);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("Nose")) {
			if (chunkDamageVisible("Nose") < 3)
				hitChunk("Nose", shot);
			if (World.Rnd().nextFloat() < 0.0575F)
				if (point3d.y > 0.0D)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
				else
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
			if (point3d.x > 1.726D) {
				if (point3d.z > 0.44400000000000001D)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
				if (point3d.z > -0.28100000000000003D && point3d.z < 0.44400000000000001D)
					if (point3d.y > 0.0D)
						FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
					else
						FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
				if (point3d.x > 2.774D && point3d.x < 3.718D && point3d.z > 0.42499999999999999D)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
				if (World.Rnd().nextFloat() < 0.12F)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
			}
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("Head1_D0", false);
			hierMesh().chunkVisible("HMask1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			break;

		case 1: // '\001'
			hierMesh().chunkVisible("Pilot2_D0", false);
			hierMesh().chunkVisible("HMask2_D0", false);
			hierMesh().chunkVisible("Pilot2_D1", true);
			break;
		}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		for (int i = 1; i < 3; i++)
			if (FM.getAltitude() < 3000F)
				hierMesh().chunkVisible("HMask" + i + "_D0", false);
			else
				hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		default:
			break;

		case 13: // '\r'
			killPilot(this, 0);
			killPilot(this, 1);
			break;

		case 35: // '#'
			if (World.Rnd().nextFloat() < 0.25F)
				FM.AS.hitTank(this, 1, World.Rnd().nextInt(2, 6));
			break;

		case 38: // '&'
			if (World.Rnd().nextFloat() < 0.25F)
				FM.AS.hitTank(this, 2, World.Rnd().nextInt(2, 6));
			break;
		}
		return super.cutFM(i, j, actor);
	}

	static {
		Class class1 = L2D.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "L2D");
		Property.set(class1, "meshName", "3DO/Plane/L2D(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
		Property.set(class1, "meshName_ja", "3DO/Plane/L2D(ja)/hier.him");
		Property.set(class1, "PaintScheme_ja", new PaintSchemeBCSPar01());
		Property.set(class1, "originCountry", PaintScheme.countryJapan);
		Property.set(class1, "yearService", 1939F);
		Property.set(class1, "yearExpired", 2999.9F);
		Property.set(class1, "FlightModel", "FlightModels/DC-3.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitL2D.class });
		weaponTriggersRegister(class1, new int[] { 3 });
		weaponHooksRegister(class1, new String[] { "_BombSpawn01" });
		weaponsRegister(class1, "default", new String[] { null });
		weaponsRegister(class1, "5xCargoA", new String[] { "BombGunCargoA 5" });
		weaponsRegister(class1, "none", new String[] { null });
	}
}
