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

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombWalterStarthilferakete;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class ME_321 extends Scheme0 implements TypeGlider, TypeTransport, TypeBomber {

	public ME_321() {
	}

	public void msgCollision(Actor actor, String s, String s1) {
		if ((actor instanceof ActorLand) && FM.getVertSpeed() > -10F) {
			return;
		} else {
			super.msgCollision(actor, s, s1);
			return;
		}
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
		case 2: // '\002'
			FM.turret[0].setHealth(f);
			break;

		case 3: // '\003'
			FM.turret[1].setHealth(f);
			break;

		case 4: // '\004'
			FM.turret[2].setHealth(f);
			break;

		case 5: // '\005'
			FM.turret[3].setHealth(f);
			break;
		}
	}

	public void doKillPilot(int i) {
		switch (i) {
		case 2: // '\002'
			FM.turret[0].bIsOperable = false;
			break;

		case 3: // '\003'
			FM.turret[1].bIsOperable = false;
			break;

		case 4: // '\004'
			FM.turret[2].bIsOperable = false;
			break;

		case 5: // '\005'
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
		}
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xpilot") || s.startsWith("xhead")) {
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

	public void destroy() {
		doCutBoosters();
		super.destroy();
	}

	protected void moveAileron(float f) {
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
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 33: // '!'
			return super.cutFM(34, j, actor);

		case 36: // '$'
			return super.cutFM(37, j, actor);
		}
		return super.cutFM(i, j, actor);
	}

	public void update(float f) {
		FM.CT.GearControl = 1.0F;
		FM.GearCX = 0.0F;
		FM.Gears.lgear = true;
		FM.Gears.rgear = true;
		super.update(f);
	}

	public void doCutCart() {
		hierMesh().chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 2.0F);
		hierMesh().chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 2.0F);
		hierMesh().chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 2.0F);
		cut("Cart");
	}

	public void doFireBoosters() {
		Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
		Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
		Eff3DActor.New(this, findHook("_Booster3"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
		Eff3DActor.New(this, findHook("_Booster4"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		for (int i = 0; i < 4; i++)
			try {
				booster[i] = new BombWalterStarthilferakete();
				booster[i].pos.setBase(this, findHook("_BoosterH" + (i + 1)), false);
				booster[i].pos.resetAsBase();
				booster[i].drawing(true);
			} catch (Exception exception) {
				debugprintln("Structure corrupt - can't hang Walter-Starthilferakete..");
			}

	}

	public void doCutBoosters() {
		for (int i = 0; i < 4; i++)
			if (booster[i] != null) {
				booster[i].start();
				booster[i] = null;
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

	private Bomb booster[] = { null, null, null, null };

	static {
		Class class1 = ME_321.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Me-321");
		Property.set(class1, "meshName", "3do/plane/Me-321/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "originCountry", PaintScheme.countryGermany);
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Me-321.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitME_321.class, CockpitME_321_FLGunner.class, CockpitME_321_FRGunner.class, CockpitME_321_LGunner.class, CockpitME_321_RGunner.class });
		Property.set(class1, "gliderString", "3DO/Arms/TowCable/mono.sim");
		Property.set(class1, "gliderBoostThrust", 1960F);
		Property.set(class1, "gliderStringLength", 140F);
		Property.set(class1, "gliderStringKx", 100F);
		Property.set(class1, "gliderStringFactor", 1.89F);
		Property.set(class1, "gliderCart", 1);
		Property.set(class1, "gliderBoosters", 1);
		Property.set(class1, "gliderFireOut", 30F);
		weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01" });
		weaponsRegister(class1, "default", new String[] { "MGunMG15t 750", "MGunMG15t 750", "MGunMG15t 1500", "MGunMG15t 1550", null });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null });
	}
}
