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

public class LI_2 extends Scheme2 implements TypeTransport, TypeBomber {

	public LI_2() {
		fSightCurAltitude = 300F;
		fSightCurSpeed = 50F;
		fSightCurForwardAngle = 0.0F;
		fSightSetForwardAngle = 0.0F;
		fSightCurSideslip = 0.0F;
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
		if (shot.chunkName.startsWith("Turret1")) {
			killPilot(shot.initiator, 2);
			if (Pd.z > 1.3350000381469727D && shot.initiator == World.getPlayerAircraft() && World.cur().isArcade())
				HUD.logCenter("H E A D S H O T");
			return;
		}
		if (shot.chunkName.startsWith("Turret2")) {
			FM.turret[1].bIsOperable = false;
			return;
		}
		if (shot.chunkName.startsWith("Turret3")) {
			FM.turret[2].bIsOperable = false;
			return;
		}
		if (shot.chunkName.startsWith("Tail") && Pd.x < -5.8000001907348633D && Pd.x > -6.7899999618530273D && Pd.z > -0.44900000000000001D && Pd.z < 0.12399999797344208D)
			FM.AS.hitPilot(shot.initiator, World.Rnd().nextInt(3, 4), (int) (shot.mass * 1000F * World.Rnd().nextFloat(0.9F, 1.1F)));
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
			return;
		} else {
			return;
		}
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
		case 0: // '\0'
			FM.turret[3].setHealth(f);
			break;

		case 2: // '\002'
			FM.turret[0].setHealth(f);
			break;

		case 3: // '\003'
			FM.turret[1].setHealth(f);
			break;

		case 4: // '\004'
			FM.turret[2].setHealth(f);
			break;
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

		case 2: // '\002'
			hierMesh().chunkVisible("Pilot3_D0", false);
			hierMesh().chunkVisible("HMask3_D0", false);
			hierMesh().chunkVisible("Pilot3_D1", true);
			break;
		}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		for (int i = 1; i < 4; i++)
			if (FM.getAltitude() < 3000F)
				hierMesh().chunkVisible("HMask" + i + "_D0", false);
			else
				hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 0: // '\0'
			if (f1 < -5F) {
				f1 = -5F;
				flag = false;
			}
			if (f1 > 45F) {
				f1 = 45F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f < -45F) {
				f = -45F;
				flag = false;
			}
			if (f > 45F) {
				f = 45F;
				flag = false;
			}
			if (f1 < -30F) {
				f1 = -30F;
				flag = false;
			}
			if (f1 > 60F) {
				f1 = 60F;
				flag = false;
			}
			break;

		case 2: // '\002'
			if (f < -45F) {
				f = -45F;
				flag = false;
			}
			if (f > 45F) {
				f = 45F;
				flag = false;
			}
			if (f1 < -30F) {
				f1 = -30F;
				flag = false;
			}
			if (f1 > 60F) {
				f1 = 60F;
				flag = false;
			}
			break;

		case 3: // '\003'
			if (f < -5F) {
				f = -5F;
				flag = false;
			}
			if (f > 5F) {
				f = 5F;
				flag = false;
			}
			if (f1 < -5F) {
				f1 = -5F;
				flag = false;
			}
			if (f1 > 5F) {
				f1 = 5F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
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
		if (fSightCurSpeed > 650F)
			fSightCurSpeed = 650F;
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

	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurForwardAngle;
	public float fSightSetForwardAngle;
	public float fSightCurSideslip;

	static {
		Class class1 = LI_2.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Douglas");
		Property.set(class1, "meshName", "3do/plane/Li-2/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "originCountry", PaintScheme.countryRussia);
		Property.set(class1, "yearService", 1939F);
		Property.set(class1, "yearExpired", 1948.5F);
		Property.set(class1, "FlightModel", "FlightModels/Li-2.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitLI2.class, CockpitLI2_Bombardier.class, CockpitLI2_TGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_BombSpawn01" });
		weaponsRegister(class1, "default", new String[] { "MGunUBt 350", "MGunShKASt 150", "MGunShKASt 150", "MGunShKASt 950", null, null, null, null, null });
		weaponsRegister(class1, "4xFAB250", new String[] { "MGunUBt 350", "MGunShKASt 150", "MGunShKASt 150", "MGunShKASt 950", "BombGunFAB250 1", "BombGunFAB250 1", "BombGunFAB250 1", "BombGunFAB250 1", null });
        weaponsRegister(class1, "2xFAB500+2xFAB250", new String[] {
                "MGunUBt 350", "MGunShKASt 150", "MGunShKASt 150", "MGunShKASt 950", "BombGunFAB250 1", "BombGunFAB250 1", "BombGunFAB500 1", "BombGunFAB500 1", null
            });
            weaponsRegister(class1, "4xFAB500", new String[] {
                "MGunUBt 350", "MGunShKASt 150", "MGunShKASt 150", "MGunShKASt 950", "BombGunFAB500 1", "BombGunFAB500 1", "BombGunFAB500 1", "BombGunFAB500 1", null
            });
		weaponsRegister(class1, "12xPara", new String[] { "MGunUBt 350", "MGunShKASt 150", "MGunShKASt 150", "MGunShKASt 950", null, null, null, null, "BombGunPara 12" });
		weaponsRegister(class1, "5xCargoA", new String[] { "MGunUBt 350", "MGunShKASt 150", "MGunShKASt 150", "MGunShKASt 950", null, null, null, null, "BombGunCargoA 5" });
        weaponsRegister(class1, "2xRRAB3", new String[] {
                "MGunUBt 350", "MGunShKASt 150", "MGunShKASt 150", "MGunShKASt 950", null, null, "BombGunRRAB3 1", "BombGunRRAB3 1", null
            });
            weaponsRegister(class1, "2xFAB250+2xSAB15", new String[] {
                "MGunUBt 350", "MGunShKASt 150", "MGunShKASt 150", "MGunShKASt 950", "BombGunSAB15 1", "BombGunSAB15 1", "BombGunFAB250 1", "BombGunFAB250 1", null
            });
            weaponsRegister(class1, "2xFAB250+2xSAB55100", new String[] {
                "MGunUBt 350", "MGunShKASt 150", "MGunShKASt 150", "MGunShKASt 950", "BombGunSAB55100 1", "BombGunSAB55100 1", "BombGunFAB250 1", "BombGunFAB250 1", null
            });
            weaponsRegister(class1, "2xFAB500+2xSAB15", new String[] {
                "MGunUBt 350", "MGunShKASt 150", "MGunShKASt 150", "MGunShKASt 950", "BombGunSAB15 1", "BombGunSAB15 1", "BombGunFAB500 1", "BombGunFAB500 1", null
            });
            weaponsRegister(class1, "2xFAB500+2xSAB55100", new String[] {
                "MGunUBt 350", "MGunShKASt 150", "MGunShKASt 150", "MGunShKASt 950", "BombGunSAB55100 1", "BombGunSAB55100 1", "BombGunFAB500 1", "BombGunFAB500 1", null
            });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null });
	}
}
