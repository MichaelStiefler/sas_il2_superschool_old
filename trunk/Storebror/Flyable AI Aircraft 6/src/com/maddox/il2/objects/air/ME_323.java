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
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class ME_323 extends Scheme7 implements TypeTransport, TypeBomber {

	public ME_323() {
	}

	protected void moveFlap(float f) {
		float f1 = -50F * f;
		hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 0: // '\0'
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
			if (f1 > 30F) {
				f1 = 30F;
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

		case 4: // '\004'
			if (f1 < -3F) {
				f1 = -3F;
				flag = false;
			}
			if (f1 > 60F) {
				f1 = 60F;
				flag = false;
			}
			break;

		case 5: // '\005'
			if (f1 < -3F) {
				f1 = -3F;
				flag = false;
			}
			if (f1 > 60F) {
				f1 = 60F;
				flag = false;
			}
			break;

		case 6: // '\006'
			if (f < -30F) {
				f = -30F;
				flag = false;
			}
			if (f > 30F) {
				f = 30F;
				flag = false;
			}
			if (f1 < 0.0F) {
				f1 = 0.0F;
				flag = false;
			}
			if (f1 > 30F) {
				f1 = 30F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xengine")) {
			int i = s.charAt(7) - 49;
			if (World.Rnd().nextFloat() < 0.1F)
				FM.AS.hitEngine(shot.initiator, i, 1);
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int j;
			if (s.endsWith("a")) {
				byte0 = 1;
				j = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				j = s.charAt(6) - 49;
			} else {
				j = s.charAt(5) - 49;
			}
			hitFlesh(j, shot, byte0);
		}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 33: // '!'
			return super.cutFM(34, j, actor);

		case 36: // '$'
			return super.cutFM(37, j, actor);

		case 3: // '\003'
			return false;

		case 4: // '\004'
			return false;

		case 5: // '\005'
			return false;

		case 6: // '\006'
			return false;
		}
		return super.cutFM(i, j, actor);
	}

	public void update(float f) {
		FM.Gears.lgear = true;
		FM.Gears.rgear = true;
		super.update(f);
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
		case 2: // '\002'
			FM.turret[6].setHealth(f);
			break;

		case 3: // '\003'
			FM.turret[4].setHealth(f);
			break;

		case 4: // '\004'
			FM.turret[5].setHealth(f);
			break;

		case 5: // '\005'
			FM.turret[0].setHealth(f);
			break;

		case 6: // '\006'
			FM.turret[1].setHealth(f);
			break;

		case 7: // '\007'
			FM.turret[2].setHealth(f);
			break;

		case 8: // '\b'
			FM.turret[3].setHealth(f);
			break;
		}
	}

	public void doKillPilot(int i) {
		switch (i) {
		case 2: // '\002'
			FM.turret[6].bIsOperable = false;
			break;

		case 3: // '\003'
			FM.turret[4].bIsOperable = false;
			break;

		case 4: // '\004'
			FM.turret[5].bIsOperable = false;
			break;

		case 5: // '\005'
			FM.turret[0].bIsOperable = false;
			break;

		case 6: // '\006'
			FM.turret[1].bIsOperable = false;
			break;

		case 7: // '\007'
			FM.turret[2].bIsOperable = false;
			break;

		case 8: // '\b'
			FM.turret[3].bIsOperable = false;
			break;
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
		default:
			break;

		case 0: // '\0'
			hierMesh().chunkVisible("Pilot1_D0", false);
			hierMesh().chunkVisible("Pilot1_D1", true);
			hierMesh().chunkVisible("Head1_D0", false);
			if (hierMesh().isChunkVisible("Blister1_D0") && World.cur().isHighGore())
				hierMesh().chunkVisible("Gore1_D0", true);
			break;

		case 1: // '\001'
			hierMesh().chunkVisible("Pilot2_D0", false);
			hierMesh().chunkVisible("Pilot2_D1", true);
			if (hierMesh().isChunkVisible("Blister1_D0") && World.cur().isHighGore())
				hierMesh().chunkVisible("Gore2_D0", true);
			break;

		case 2: // '\002'
			hierMesh().chunkVisible("Pilot3_D0", false);
			hierMesh().chunkVisible("Pilot3_D1", true);
			break;

		case 3: // '\003'
			hierMesh().chunkVisible("Pilot4_D0", false);
			hierMesh().chunkVisible("Pilot4_D1", true);
			break;

		case 4: // '\004'
			hierMesh().chunkVisible("Pilot5_D0", false);
			hierMesh().chunkVisible("Pilot5_D1", true);
			break;
		}
	}

	public void msgShot(Shot shot) {
		setShot(shot);
		if ("CF_D3".equals(shot.chunkName)) {
			return;
		} else {
			super.msgShot(shot);
			return;
		}
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
		Class class1 = ME_323.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Me-323");
		Property.set(class1, "meshName", "3Do/Plane/Me-323/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "originCountry", PaintScheme.countryGermany);
		Property.set(class1, "yearService", 1942F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Me-323.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitME_323.class, CockpitME_323_FLGunner.class, CockpitME_323_FRGunner.class, CockpitME_323_TGunner.class, CockpitME_323_TLGunner.class, CockpitME_323_TRGunner.class,
				CockpitME_323_LGunner.class, CockpitME_323_RGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 15, 16, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_ExternalBomb01" });
		weaponsRegister(class1, "default", new String[] { "MGunMG131t 650", "MGunMG131t 650", "MGunMG131t 525", "MGunMG131t 525", "MGunMG15120t 350", "MGunMG15120t 350", "MGunMG15t 350", null });
		weaponsRegister(class1, "32xPara", new String[] { "MGunMG131t 650", "MGunMG131t 650", "MGunMG131t 525", "MGunMG131t 525", "MGunMG15120t 350", "MGunMG15120t 350", "MGunMG15t 350", "BombGunPara 32" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null });
	}
}
