package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class P_47D extends P_47 implements TypeFighterAceMaker {
	public int k14Mode;
	public int k14WingspanType;
	public float k14Distance;

	public P_47D() {
		this.k14Mode = 0;
		this.k14WingspanType = 0;
		this.k14Distance = 200.0F;
	}

	public boolean typeFighterAceMakerToggleAutomation() {
		this.k14Mode += 1;
		if (this.k14Mode > 2)
			this.k14Mode = 0;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + this.k14Mode);
		return true;
	}

	public void typeFighterAceMakerAdjDistanceReset() {
	}

	public void typeFighterAceMakerAdjDistancePlus() {
		this.k14Distance += 10.0F;
		if (this.k14Distance > 800.0F)
			this.k14Distance = 800.0F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
	}

	public void typeFighterAceMakerAdjDistanceMinus() {
		this.k14Distance -= 10.0F;
		if (this.k14Distance < 200.0F)
			this.k14Distance = 200.0F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
	}

	public void typeFighterAceMakerAdjSideslipReset() {
	}

	public void typeFighterAceMakerAdjSideslipPlus() {
		this.k14WingspanType -= 1;
		if (this.k14WingspanType < 0)
			this.k14WingspanType = 0;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
	}

	public void typeFighterAceMakerAdjSideslipMinus() {
		this.k14WingspanType += 1;
		if (this.k14WingspanType > 9)
			this.k14WingspanType = 9;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
	}

	public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted paramNetMsgGuaranted) throws IOException {
		paramNetMsgGuaranted.writeByte(this.k14Mode);
		paramNetMsgGuaranted.writeByte(this.k14WingspanType);
		paramNetMsgGuaranted.writeFloat(this.k14Distance);
	}

	public void typeFighterAceMakerReplicateFromNet(NetMsgInput paramNetMsgInput) throws IOException {
		this.k14Mode = paramNetMsgInput.readByte();
		this.k14WingspanType = paramNetMsgInput.readByte();
		this.k14Distance = paramNetMsgInput.readFloat();
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		hierMesh().chunkVisible("ETank_D0", false);
		boolean bCenterRackVisible = false;
		boolean bWingRacksVisible = false;
		bCenterRackVisible = ((this.thisWeaponsName.equalsIgnoreCase("tank")) || (this.thisWeaponsName.equalsIgnoreCase("tank2x500")) || (this.thisWeaponsName.equalsIgnoreCase("tank6x45")) || (this.thisWeaponsName.equalsIgnoreCase("tank2x5006x45"))
				|| (this.thisWeaponsName.equalsIgnoreCase("1x1000")) || (this.thisWeaponsName.equalsIgnoreCase("1x10002x500")) || (this.thisWeaponsName.equalsIgnoreCase("1x10006x45")) || (this.thisWeaponsName.equalsIgnoreCase("1x10002x5006x45")) || (this.thisWeaponsName
				.indexOf("c_") != -1));
		bWingRacksVisible = ((this.thisWeaponsName.equalsIgnoreCase("tank2x500")) || (this.thisWeaponsName.equalsIgnoreCase("tank2x5006x45")) || (this.thisWeaponsName.equalsIgnoreCase("2x500")) || (this.thisWeaponsName.equalsIgnoreCase("2x5006x45"))
				|| (this.thisWeaponsName.equalsIgnoreCase("1x10002x500")) || (this.thisWeaponsName.equalsIgnoreCase("1x10002x5006x45")) || (this.thisWeaponsName.indexOf("w_") != -1));
		hierMesh().chunkVisible("Rack_D0", bCenterRackVisible);
		hierMesh().chunkVisible("RackL_D0", bWingRacksVisible);
		hierMesh().chunkVisible("RackR_D0", bWingRacksVisible);
	}

	static {
		Class localClass = P_47D.class;
		new NetAircraft.SPAWN(localClass);
		Property.set(localClass, "iconFar_shortClassName", "P-47");
		Property.set(localClass, "meshName", "3DO/Plane/P-47D(Multi1)/hier.him");
		Property.set(localClass, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(localClass, "meshName_us", "3DO/Plane/P-47D(USA)/hier.him");
		Property.set(localClass, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(localClass, "noseart", 1);
		Property.set(localClass, "yearService", 1944.0F);
		Property.set(localClass, "yearExpired", 1947.5F);
		Property.set(localClass, "FlightModel", "FlightModels/P-47D-27_late.fmd");
		Property.set(localClass, "cockpitClass", new Class[] { CockpitP_47D25.class });
		Property.set(localClass, "LOSElevation", 1.1104F);
		weaponTriggersRegister(localClass, new int[] { 0, 0, 0, 0, 1, 1, 1, 1, 9, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 9, 9, 9 });
		weaponHooksRegister(localClass, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalDev01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01", "_ExternalDev02", "_ExternalDev03",
				"_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01" });
	}
}