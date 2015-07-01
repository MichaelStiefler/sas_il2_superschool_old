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

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class SU_2 extends Scheme1 implements TypeScout, TypeBomber, TypeStormovik {

	public SU_2() {
		flapps = 0.0F;
		fSightCurAltitude = 300F;
		fSightCurSpeed = 50F;
		fSightCurForwardAngle = 0.0F;
		fSightSetForwardAngle = 0.0F;
		fSightCurSideslip = 0.0F;
	}

	protected void moveBayDoor(float f) {
		hierMesh().chunkSetAngles("Bay01_D0", 0.0F, -90F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay02_D0", 0.0F, -90F * f, 0.0F);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		for (int i = 1; i < 3; i++)
			if (FM.getAltitude() < 3000F)
				hierMesh().chunkVisible("HMask" + i + "_D0", false);
			else
				hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL2a_D0", 0.0F, -42F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -30F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -140F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL7_D0", 0.0F, -80F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -90F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearR2a_D0", 0.0F, -42F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -30F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -140F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, -90F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearR7_D0", 0.0F, -80F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -40F * f2, 0.0F);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(hierMesh(), f, f1, f2);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xcf")) {
			if (chunkDamageVisible("CF") < 3)
				hitChunk("CF", shot);
		} else if (s.startsWith("xtail")) {
			if (chunkDamageVisible("Tail1") < 3)
				hitChunk("Tail1", shot);
		} else if (s.startsWith("xkeel")) {
			if (chunkDamageVisible("Keel1") < 2)
				hitChunk("Keel1", shot);
		} else if (s.startsWith("xrudder"))
			hitChunk("Rudder1", shot);
		else if (s.startsWith("xstabl"))
			hitChunk("StabL", shot);
		else if (s.startsWith("xstabr"))
			hitChunk("StabR", shot);
		else if (s.startsWith("xvatorl"))
			hitChunk("VatorL", shot);
		else if (s.startsWith("xvatorr"))
			hitChunk("VatorR", shot);
		else if (s.startsWith("xwinglin")) {
			if (chunkDamageVisible("WingLIn") < 3)
				hitChunk("WingLIn", shot);
			if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.1F)
				FM.AS.hitTank(shot.initiator, 0, 1);
		} else if (s.startsWith("xwingrin")) {
			if (chunkDamageVisible("WingRIn") < 3)
				hitChunk("WingRIn", shot);
			if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.1F)
				FM.AS.hitTank(shot.initiator, 1, 1);
		} else if (s.startsWith("xwinglmid")) {
			if (chunkDamageVisible("WingLMid") < 3)
				hitChunk("WingLMid", shot);
		} else if (s.startsWith("xwingrmid")) {
			if (chunkDamageVisible("WingRMid") < 3)
				hitChunk("WingRMid", shot);
		} else if (s.startsWith("xwinglout")) {
			if (chunkDamageVisible("WingLOut") < 3)
				hitChunk("WingLOut", shot);
		} else if (s.startsWith("xwingrout")) {
			if (chunkDamageVisible("WingROut") < 3)
				hitChunk("WingROut", shot);
		} else if (s.startsWith("xaronel"))
			hitChunk("AroneL", shot);
		else if (s.startsWith("xaroner"))
			hitChunk("AroneR", shot);
		else if (s.startsWith("xengine1")) {
			if (chunkDamageVisible("Engine1") < 2)
				hitChunk("Engine1", shot);
			if (getEnergyPastArmor(1.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 0.5F) {
				FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
				if (FM.AS.astateEngineStates[0] < 1) {
					FM.AS.hitEngine(shot.initiator, 0, 1);
					FM.AS.doSetEngineState(shot.initiator, 0, 1);
				}
				if (World.Rnd().nextFloat() < shot.power / 960000F)
					FM.AS.hitEngine(shot.initiator, 0, 3);
				getEnergyPastArmor(25F, shot);
			}
		} else if (s.startsWith("xgearl"))
			hitChunk("GearL2", shot);
		else if (s.startsWith("xgearr"))
			hitChunk("GearR2", shot);
		else if (s.startsWith("xturret")) {
			if (s.startsWith("xturret1"))
				FM.AS.setJamBullets(10, 0);
			if (s.startsWith("xturret2"))
				FM.AS.setJamBullets(11, 0);
			if (s.startsWith("xturret3"))
				FM.AS.setJamBullets(12, 0);
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int i;
			if (s.endsWith("a")) {
				byte0 = 1;
				i = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				i = s.charAt(6) - 49;
			} else {
				i = s.charAt(5) - 49;
			}
			hitFlesh(i, shot, byte0);
		}
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		case 0: // '\0'
			if (f < -90F) {
				f = -90F;
				flag = false;
			}
			if (f > 90F) {
				f = 90F;
				flag = false;
			}
			if (f1 < -5F) {
				f1 = -5F;
				flag = false;
			}
			if (f1 > 60F) {
				f1 = 60F;
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
		fSightCurForwardAngle = 0.0F;
	}

	public void typeBomberAdjDistancePlus() {
		fSightCurForwardAngle += 0.2F;
		if (fSightCurForwardAngle > 75F)
			fSightCurForwardAngle = 75F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (fSightCurForwardAngle * 1.0F)) });
	}

	public void typeBomberAdjDistanceMinus() {
		fSightCurForwardAngle -= 0.2F;
		if (fSightCurForwardAngle < -15F)
			fSightCurForwardAngle = -15F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (fSightCurForwardAngle * 1.0F)) });
	}

	public void typeBomberAdjSideslipReset() {
		fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus() {
		fSightCurSideslip++;
		if (fSightCurSideslip > 45F)
			fSightCurSideslip = 45F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (fSightCurSideslip * 1.0F)) });
	}

	public void typeBomberAdjSideslipMinus() {
		fSightCurSideslip--;
		if (fSightCurSideslip < -45F)
			fSightCurSideslip = -45F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (fSightCurSideslip * 1.0F)) });
	}

	public void typeBomberAdjAltitudeReset() {
		fSightCurAltitude = 300F;
	}

	public void typeBomberAdjAltitudePlus() {
		fSightCurAltitude += 10F;
		if (fSightCurAltitude > 10000F)
			fSightCurAltitude = 10000F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
	}

	public void typeBomberAdjAltitudeMinus() {
		fSightCurAltitude -= 10F;
		if (fSightCurAltitude < 300F)
			fSightCurAltitude = 300F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
	}

	public void typeBomberAdjSpeedReset() {
		fSightCurSpeed = 50F;
	}

	public void typeBomberAdjSpeedPlus() {
		fSightCurSpeed += 5F;
		if (fSightCurSpeed > 520F)
			fSightCurSpeed = 520F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus() {
		fSightCurSpeed -= 5F;
		if (fSightCurSpeed < 50F)
			fSightCurSpeed = 50F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
	}

	public void typeBomberUpdate(float f) {
		double d = ((double) fSightCurSpeed / 3.6000000000000001D) * Math.sqrt((double) fSightCurAltitude * 0.20387359799999999D);
		d -= (double) (fSightCurAltitude * fSightCurAltitude) * 1.419E-005D;
		fSightSetForwardAngle = (float) Math.toDegrees(Math.atan(d / (double) fSightCurAltitude));
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		netmsgguaranted.writeFloat(fSightCurAltitude);
		netmsgguaranted.writeFloat(fSightCurSpeed);
		netmsgguaranted.writeFloat(fSightCurForwardAngle);
		netmsgguaranted.writeFloat(fSightCurSideslip);
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		fSightCurAltitude = netmsginput.readFloat();
		fSightCurSpeed = netmsginput.readFloat();
		fSightCurForwardAngle = netmsginput.readFloat();
		fSightCurSideslip = netmsginput.readFloat();
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
		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("HMask1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			hierMesh().chunkVisible("Head1_D0", false);
			break;

		case 1: // '\001'
			hierMesh().chunkVisible("Pilot2_D0", false);
			hierMesh().chunkVisible("HMask2_D0", false);
			hierMesh().chunkVisible("Pilot2_D1", true);
			break;
		}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 11: // '\013'
		case 19: // '\023'
			hierMesh().chunkVisible("Wire_D0", false);
			break;
		}
		return super.cutFM(i, j, actor);
	}

	public void update(float f) {
		float f1 = cvt(FM.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.0F, -10F);
		if (Math.abs(flapps - f1) > 0.01F) {
			flapps = f1;
			for (int i = 1; i < 17; i++) {
				String s = "cowlflap" + i + "_D0";
				hierMesh().chunkSetAngles(s, 0.0F, f1, 0.0F);
			}

		}
		super.update(f);
	}

	private float flapps;

	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurForwardAngle;
	public float fSightSetForwardAngle;
	public float fSightCurSideslip;

	static {
		Class class1 = SU_2.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Su-2");
		Property.set(class1, "meshName", "3DO/Plane/Su-2/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
		Property.set(class1, "originCountry", PaintScheme.countryRussia);
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 1948.5F);
		Property.set(class1, "FlightModel", "FlightModels/Su-2.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitSU_2.class, CockpitSU_2_Bombardier.class, CockpitSU_2_TGunner.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_BombSpawn01", "_BombSpawn02" });
		weaponsRegister(class1, "default", new String[] { "MGunShKASki 1700", "MGunShKASki 1700", "MGunShKASt 1000", null, null, null, null });
		weaponsRegister(class1, "30ao10", new String[] { "MGunShKASki 1700", "MGunShKASki 1700", "MGunShKASt 1000", null, null, "BombGunAO10 15", "BombGunAO10 15" });
		weaponsRegister(class1, "4fab50", new String[] { "MGunShKASki 1700", "MGunShKASki 1700", "MGunShKASt 1000", null, null, "BombGunFAB50 2", "BombGunFAB50 2" });
		weaponsRegister(class1, "6fab50", new String[] { "MGunShKASki 1700", "MGunShKASki 1700", "MGunShKASt 1000", "BombGunFAB50 1", "BombGunFAB50 1", "BombGunFAB50 2", "BombGunFAB50 2" });
		weaponsRegister(class1, "4fab100", new String[] { "MGunShKASki 1700", "MGunShKASki 1700", "MGunShKASt 1000", null, null, "BombGunFAB100 2", "BombGunFAB100 2" });
		weaponsRegister(class1, "6fab100", new String[] { "MGunShKASki 1700", "MGunShKASki 1700", "MGunShKASt 1000", "BombGunFAB100 1", "BombGunFAB100 1", "BombGunFAB100 2", "BombGunFAB100 2" });
		weaponsRegister(class1, "2fab250", new String[] { "MGunShKASki 1700", "MGunShKASki 1700", "MGunShKASt 1000", "BombGunFAB250 1", "BombGunFAB250 1", null, null });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null });
	}
}
