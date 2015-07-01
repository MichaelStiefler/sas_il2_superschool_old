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

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public class DXXI_DK extends DXXI {

	public DXXI_DK() {
		hasRevi = false;
		gyroDelta = 0.0F;
	}

	public void missionStarting() {
		super.missionStarting();
		if (FM.isStationedOnGround())
			gyroDelta += (float) Math.random() * 360F;
	}

	public boolean hasRevi() {
		return hasRevi;
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (FM.Or.getKren() < -10F || FM.Or.getKren() > 10F)
			gyroDelta -= 0.01D;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (thisWeaponsName.equals("madsen")) {
			hierMesh().chunkVisible("MadsenR_D0", true);
			hierMesh().chunkVisible("MadsenL_D0", true);
		}
		if (Config.isUSE_RENDER() && World.cur().camouflage == 1) {
			hierMesh().chunkVisible("GearL3_D0", false);
			hierMesh().chunkVisible("GearR3_D0", false);
			hierMesh().chunkVisible("GearL3W_D0", true);
			hierMesh().chunkVisible("GearR3W_D0", true);
		}
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		if (World.cur().camouflage == 1) {
			hiermesh.chunkVisible("GearL3_D0", false);
			hiermesh.chunkVisible("GearR3_D0", false);
			hiermesh.chunkVisible("GearL3W_D0", true);
			hiermesh.chunkVisible("GearR3W_D0", true);
		}
	}

	protected void moveFan(float f) {
		if (Config.isUSE_RENDER()) {
			super.moveFan(f);
			float f1 = FM.CT.getAileron();
			float f2 = FM.CT.getElevator();
			hierMesh().chunkSetAngles("Stick_D0", 0.0F, 9F * f1, cvt(f2, -1F, 1.0F, -8F, 9.5F));
			hierMesh().chunkSetAngles("pilotarm2_d0", cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, cvt(f1, -1F, 1.0F, 6F, -8F) - cvt(f2, -1F, 1.0F, -37F, 35F));
			hierMesh().chunkSetAngles("pilotarm1_d0", 0.0F, 0.0F, cvt(f1, -1F, 1.0F, -16F, 14F) + cvt(f2, -1F, 0.0F, -61F, 0.0F) + cvt(f2, 0.0F, 1.0F, 0.0F, 43F));
		}
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
		if (hierMesh().isChunkVisible("GearR22_D2") && !hierMesh().isChunkVisible("gearr31_d0")) {
			hierMesh().chunkVisible("gearr31_d0", true);
			hierMesh().chunkVisible("gearr32_d0", true);
			Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("GearR22_D1"));
			wreckage.collide(true);
			Vector3d vector3d = new Vector3d();
			vector3d.set(FM.Vwld);
			wreckage.setSpeed(vector3d);
		}
		if (hierMesh().isChunkVisible("GearL22_D2") && !hierMesh().isChunkVisible("gearl31_d0")) {
			hierMesh().chunkVisible("gearl31_d0", true);
			hierMesh().chunkVisible("gearl32_d0", true);
			Wreckage wreckage1 = new Wreckage(this, hierMesh().chunkFind("GearL22_D1"));
			wreckage1.collide(true);
			Vector3d vector3d1 = new Vector3d();
			vector3d1.set(FM.Vwld);
			wreckage1.setSpeed(vector3d1);
		}
	}

	public void auxPlus(int i) {
		switch (i) {
		case 1: // '\001'
			gyroDelta++;
			break;
		}
	}

	public void auxMinus(int i) {
		switch (i) {
		case 1: // '\001'
			gyroDelta--;
			break;
		}
	}

	private boolean hasRevi;
	public float gyroDelta;

	static {
		Class class1 = DXXI_DK.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "D.XXI");
		Property.set(class1, "meshName", "3DO/Plane/DXXI_DK/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar00DXXI());
		Property.set(class1, "yearService", 1938F);
		Property.set(class1, "yearExpired", 1943F);
		Property.set(class1, "FlightModel", "FlightModels/FokkerDK.fmd");
		Property.set(class1, "LOSElevation", 0.8472F);
		Property.set(class1, "originCountry", PaintScheme.countryNoName);
		Property.set(class1, "cockpitClass", new Class[] { CockpitDXXI_DK.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning303sipzl_noSmoke 500", "MGunBrowning303sipzl_noSmoke 500", null, null });
		weaponsRegister(class1, "madsen", new String[] { "MGunBrowning303sipzl_noSmoke 500", "MGunBrowning303sipzl_noSmoke 500", "MGunMadsen20 60", "MGunMadsen20 60" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null });
	}
}
