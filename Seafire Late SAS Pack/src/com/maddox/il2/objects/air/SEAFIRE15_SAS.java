package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class SEAFIRE15_SAS extends SEAFIRE3 implements TypeFighterAceMaker, TypeBNZFighter {

	public SEAFIRE15_SAS() {
		k14Mode = 0;
		k14WingspanType = 0;
		k14Distance = 200F;
		flapps = 0.0F;
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
		resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.55F);
		hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
		float f1 = (float) Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
		hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
		hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	public void update(float f) {
		super.update(f);
		float f1 = ((FlightModelMain) (super.FM)).EI.engines[0].getControlRadiator();
		if (Math.abs(flapps - f1) > 0.01F) {
			flapps = f1;
			hierMesh().chunkSetAngles("Oil1_D0", 0.0F, -20F * f1, 0.0F);
			hierMesh().chunkSetAngles("Oil2_D0", 0.0F, -20F * f1, 0.0F);
		}
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
	private float flapps;

	static {
		Class class1 = SEAFIRE15_SAS.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Spit");
		Property.set(class1, "meshName", "3DO/Plane/SeafireMkXV_SAS(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
		Property.set(class1, "yearService", 1942.11F);
		Property.set(class1, "yearExpired", 1944.12F);
		Property.set(class1, "FlightModel", "FlightModels/SeafireXV.fmd:SeafireLate_FM");
		Property.set(class1, "cockpitClass", new Class[] { CockpitSeafire15_SAS.class });
		Property.set(class1, "LOSElevation", 0.5926F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 9, 3, 9, 9, 3, 3, 9, 9, 9, 9, 2, 2, 2, 2, 9, 9 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02",
				"_ExternalDev03", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalRock01", "_ExternalRock02",
				"_ExternalRock03", "_ExternalRock04", "_ExternalDev08", "_ExternalDev09" });
		Aircraft.weaponsRegister(class1, "default", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkWF 120",
				"MGunHispanoMkIkWF 120", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "1x30dt", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkWF 120",
				"MGunHispanoMkIkWF 120", null, null, null, null, null, null, null, null, null, null, null, null, null, null, "FuelTankGun_TankSpit30", null });
		Aircraft.weaponsRegister(class1, "1x45dt", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkWF 120",
				"MGunHispanoMkIkWF 120", null, null, null, null, null, null, null, null, null, null, null, null, null, null, "FuelTankGun_TankSpit45", null });
		Aircraft.weaponsRegister(class1, "1x90dt", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkWF 120",
				"MGunHispanoMkIkWF 120", null, null, null, null, null, null, null, null, null, null, null, null, null, null, "FuelTankGun_TankSpit90", null });
		Aircraft.weaponsRegister(class1, "1xDT", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkWF 120",
				"MGunHispanoMkIkWF 120", "PylonSpitC", null, null, null, null, null, null, null, null, null, null, null, null, null, null, "FuelTankGun_TankSF17" });
		Aircraft.weaponsRegister(class1, "1x500", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkWF 120",
				"MGunHispanoMkIkWF 120", "PylonSpitC", "BombGun500lbsE 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "2x250", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkWF 120",
				"MGunHispanoMkIkWF 120", null, null, "PylonSpitL", "PylonSpitR", "BombGun250lbsE 1", "BombGun250lbsE 1", null, null, null, null, null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "1x500_2x250", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkWF 120",
				"MGunHispanoMkIkWF 120", "PylonSpitC", "BombGun500lbsE 1", "PylonSpitL", "PylonSpitR", "BombGun250lbsE 1", "BombGun250lbsE 1", null, null, null, null, null, null, null, null, null, null });
		Aircraft.weaponsRegister(class1, "4x60hvar", new String[] { "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunBrowning303kWF 350", "MGunHispanoMkIkWF 120",
				"MGunHispanoMkIkWF 120", null, null, null, null, null, null, "PylonSpitROCK", "PylonSpitROCK", "PylonSpitROCK", "PylonSpitROCK", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1",
				"RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", null, null });
		Aircraft.weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
