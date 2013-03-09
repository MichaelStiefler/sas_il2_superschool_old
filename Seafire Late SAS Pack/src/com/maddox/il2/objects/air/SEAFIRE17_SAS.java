package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class SEAFIRE17_SAS extends SPITFIRE9 implements TypeFighterAceMaker, TypeBNZFighter {

	public SEAFIRE17_SAS() {
		k14Mode = 0;
		k14WingspanType = 0;
		k14Distance = 200F;
		flapps = 0.0F;
		arrestor = 0.0F;
		bailingOut = false;
		canopyForward = false;
		okToJump = false;
		sideDoorOpened = false;
		bWingsFolded = false;
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	public void msgCollision(Actor actor, String s, String s1) {
		if (isNet() && isNetMirror())
			return;
		if (s.startsWith("Hook")) {
			return;
		} else {
			super.msgCollision(actor, s, s1);
			return;
		}
	}

	protected void moveFlap(float f) {
		float f1 = -85F * f;
		hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
		hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
		hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
		hierMesh().chunkSetAngles("Flap05_D0", 0.0F, f1, 0.0F);
		hierMesh().chunkSetAngles("Flap06_D0", 0.0F, f1, 0.0F);
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.6F, 0.0F, -95F), 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, -95F), 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -75F), 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(hierMesh(), f);
	}

	public void moveSteering(float f) {
		hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
	}

	public void moveWheelSink() {
		resetYPRmodifier();
		Aircraft.xyz[2] = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[0], 0.0F, 0.247F, 0.0F, -0.247F);
		hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
		Aircraft.xyz[2] = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[1], 0.0F, 0.247F, 0.0F, 0.247F);
		hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
	}

	public void moveCockpitDoor(float f) {
		if (bailingOut && f >= 1.0F && !canopyForward) {
			canopyForward = true;
			FM.CT.forceCockpitDoor(0.0F);
			FM.AS.setCockpitDoor(this, 1);
		} else if (canopyForward) {
			hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 160F * f, 0.0F);
			if (f >= 1.0F) {
				okToJump = true;
				super.hitDaSilk();
			}
		} else {
			Aircraft.xyz[0] = 0.0F;
			Aircraft.xyz[2] = 0.0F;
			Aircraft.ypr[0] = 0.0F;
			Aircraft.ypr[1] = 0.0F;
			Aircraft.ypr[2] = 0.0F;
			Aircraft.xyz[1] = f * 0.548F;
			hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
			float f1 = (float) Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
			hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
			hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
		}
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	public void hitDaSilk() {
		if (okToJump)
			super.hitDaSilk();
		else if (FM.isPlayers() || isNetPlayer() || !FM.AS.isPilotDead(0)) {
			if (FM.CT.getCockpitDoor() < 1.0F && !bailingOut) {
				bailingOut = true;
				FM.AS.setCockpitDoor(this, 1);
			} else if (FM.CT.getCockpitDoor() == 1.0F && !bailingOut) {
				bailingOut = true;
				okToJump = true;
				canopyForward = true;
				super.hitDaSilk();
			}
		}
		if (!sideDoorOpened && FM.AS.bIsAboutToBailout && !FM.AS.isPilotDead(0)) {
			sideDoorOpened = true;
			FM.CT.forceCockpitDoor(0.0F);
			FM.AS.setCockpitDoor(this, 1);
		}
	}

	public void moveArrestorHook(float f) {
		hierMesh().chunkSetAngles("Hook2_D0", 0.0F, -35F * f, 0.0F);
		resetYPRmodifier();
		Aircraft.xyz[2] = 0.3385F * f;
		hierMesh().chunkSetLocate("Hook1_D0", Aircraft.xyz, Aircraft.ypr);
		arrestor = f;
	}
	
    public void rareAction(float f, boolean flag)
    {
    	super.rareAction(f, flag);
    	if (okToJump && FM.CT.getCockpitDoor() >= 1.0F && canopyForward && bailingOut && !FM.AS.bIsAboutToBailout) {
    		AircraftState.bCheckPlayerAircraft = false;
    		super.hitDaSilk();
    		AircraftState.bCheckPlayerAircraft = false;
    	}
    }

	public void update(float f) {
		super.update(f);
		float f1 = FM.CT.getArrestor();
		float f2 = 81F * f1 * f1 * f1 * f1 * f1 * f1 * f1;
		if (f1 > 0.01F)
			if (FM.Gears.arrestorVAngle != 0.0F) {
				arrestor = Aircraft.cvt(FM.Gears.arrestorVAngle, -f2, f2, -f2, f2);
				moveArrestorHook(f1);
				if (FM.Gears.arrestorVAngle >= -81F)
					;
			} else {
				float f3 = 58F * FM.Gears.arrestorVSink;
				if (f3 > 0.0F && FM.getSpeedKMH() > 60F)
					Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
				arrestor += f3;
				if (arrestor > f2)
					arrestor = f2;
				if (arrestor < -f2)
					arrestor = -f2;
				moveArrestorHook(f1);
			}
		float f4 = ((FlightModelMain) (super.FM)).EI.engines[0].getControlRadiator();
		if (Math.abs(flapps - f4) > 0.01F) {
			flapps = f4;
			hierMesh().chunkSetAngles("Oil1_D0", 0.0F, -20F * f4, 0.0F);
			hierMesh().chunkSetAngles("Oil2_D0", 0.0F, -20F * f4, 0.0F);
		}
		if (bWingsFolded) {
			hierMesh().chunkVisible("SoporteR_ala", true);
			hierMesh().chunkVisible("SoporteL_ala", true);
		} else if (FM.Gears.onGround()) {
			hierMesh().chunkVisible("SoporteR_ala", false);
			hierMesh().chunkVisible("SoporteL_ala", false);
		}
	}

	protected void moveWingFold(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, -112F * f, 0.0F);
		hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, -112F * f, 0.0F);
		hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, -112F * f, 0.0F);
		hiermesh.chunkSetAngles("WingROut_D0", 0.0F, -112F * f, 0.0F);
	}

	public void moveWingFold(float f) {
		moveWingFold(hierMesh(), f);
		if (f < 0.001F) {
			setGunPodsOn(true);
			hideWingWeapons(false);
		} else {
			setGunPodsOn(false);
			FM.CT.WeaponControl[0] = false;
			hideWingWeapons(true);
		}
		bWingsFolded = (f > 0.999F);
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 19: // '\023'
			FM.CT.bHasArrestorControl = false;
			break;
		}
		return super.cutFM(i, j, actor);
	}

	public boolean typeFighterAceMakerToggleAutomation() {
		k14Mode++;
		if (k14Mode > 2)
			k14Mode = 0;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + k14Mode);
		return true;
	}

	public void typeFighterAceMakerAdjDistanceReset() {
	}

	public void typeFighterAceMakerAdjDistancePlus() {
		k14Distance += 10F;
		if (k14Distance > 800F)
			k14Distance = 800F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
	}

	public void typeFighterAceMakerAdjDistanceMinus() {
		k14Distance -= 10F;
		if (k14Distance < 200F)
			k14Distance = 200F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
	}

	public void typeFighterAceMakerAdjSideslipReset() {
	}

	public void typeFighterAceMakerAdjSideslipPlus() {
		k14WingspanType--;
		if (k14WingspanType < 0)
			k14WingspanType = 0;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
	}

	public void typeFighterAceMakerAdjSideslipMinus() {
		k14WingspanType++;
		if (k14WingspanType > 9)
			k14WingspanType = 9;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
	}

	public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		netmsgguaranted.writeByte(k14Mode);
		netmsgguaranted.writeByte(k14WingspanType);
		netmsgguaranted.writeFloat(k14Distance);
	}

	public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		k14Mode = netmsginput.readByte();
		k14WingspanType = netmsginput.readByte();
		k14Distance = netmsginput.readFloat();
	}

	public int k14Mode;
	public int k14WingspanType;
	public float k14Distance;
	private float arrestor;
	private float flapps;
	public static boolean bChangedPit = false;
	private boolean bailingOut;
	private boolean canopyForward;
	private boolean okToJump;
	private boolean sideDoorOpened;
	private boolean bWingsFolded;

	static {
		Class class1 = SEAFIRE17_SAS.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Spit");
		Property.set(class1, "meshName", "3DO/Plane/SeafireMkXVII_SAS(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1946.5F);
		Property.set(class1, "FlightModel", "FlightModels/SeafireXVII.fmd:SeafireLate_FM");
		Property.set(class1, "cockpitClass", new Class[] { CockpitSeafire17_SAS.class });
		Property.set(class1, "LOSElevation", 0.5926F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 9, 3, 9, 9, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalBomb02", "_ExternalDev02",
				"_ExternalDev03", "_ExternalBomb03", "_ExternalBomb01", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10",
				"_ExternalDev11", "_ExternalDev12", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07",
				"_ExternalRock08", "_ExternalDev13" });
		Aircraft.weaponsRegister(class1, "default", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkpzl 120",
				"MGunHispanoMkIkpzl 120", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "90gal_dt", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkpzl 120",
				"MGunHispanoMkIkpzl 120", "PylonSpitC 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				"FuelTankGun_TankSF17 1" });
		Aircraft.weaponsRegister(class1, "1x250lb", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkpzl 120",
				"MGunHispanoMkIkpzl 120", "PylonSpitC 1", null, null, null, null, "BombGun250lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null });
		Aircraft.weaponsRegister(class1, "1x500lb", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkpzl 120",
				"MGunHispanoMkIkpzl 120", "PylonSpitC 1", null, null, null, null, "BombGun500lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null });
		Aircraft.weaponsRegister(class1, "2x250lb", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkpzl 120",
				"MGunHispanoMkIkpzl 120", null, "3 BombGun250lbsE 1", "PylonSpitL 1", "PylonSpitR 1", "BombGun250lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "90gal_dt+2x250lb", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350",
				"MGunHispanoMkIkpzl 120", "MGunHispanoMkIkpzl 120", "PylonSpitC 1", "3 BombGun250lbsE 1", "PylonSpitL 1", "PylonSpitR 1", "BombGun250lbsE 1", null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, "FuelTankGun_TankSF17 1" });
		Aircraft.weaponsRegister(class1, "3x250lb", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkpzl 120",
				"MGunHispanoMkIkpzl 120", "PylonSpitC 1", "3 BombGun250lbsE 1", "PylonSpitL 1", "PylonSpitR 1", "BombGun250lbsE 1", "BombGun250lbsE 1", null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "1x500lb+2x250lb", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350",
				"MGunHispanoMkIkpzl 120", "MGunHispanoMkIkpzl 120", "PylonSpitC 1", "3 BombGun250lbsE 1", "PylonSpitL 1", "PylonSpitR 1", "BombGun250lbsE 1", "BombGun500lbsE 1", null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "8xHVAR", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkpzl 120",
				"MGunHispanoMkIkpzl 120", null, null, null, null, null, null, null, null, "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1", null, null, null,
				"RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1",
				"RocketGunHVAR5BEAU 1", null });
		Aircraft.weaponsRegister(class1, "90gal_dt+8xHVAR", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350",
				"MGunHispanoMkIkpzl 120", "MGunHispanoMkIkpzl 120", "PylonSpitC 1", null, null, null, null, null, null, null, "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1",
				"PylonSpitROCK 1", null, null, null, "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1",
				"RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "FuelTankGun_TankSF17 1" });
		Aircraft.weaponsRegister(class1, "1x250lb+8xHVAR", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350",
				"MGunHispanoMkIkpzl 120", "MGunHispanoMkIkpzl 120", "PylonSpitC 1", null, null, null, null, "BombGun500lbsE 1", null, null, "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1",
				"PylonSpitROCK 1", null, null, null, "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1",
				"RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", null });
		Aircraft.weaponsRegister(class1, "1x500lb+8xHVAR", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350",
				"MGunHispanoMkIkpzl 120", "MGunHispanoMkIkpzl 120", "PylonSpitC 1", null, null, null, null, "BombGun500lbsE 1", null, null, "PylonSpitROCK 1", "PylonSpitROCK 1", "PylonSpitROCK 1",
				"PylonSpitROCK 1", null, null, null, "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1",
				"RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", null });
		Aircraft.weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null });
	}
}
