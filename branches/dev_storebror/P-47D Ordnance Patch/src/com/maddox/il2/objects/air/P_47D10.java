package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_47D10 extends P_47
{
  public void onAircraftLoaded()
  {
    super.onAircraftLoaded();
    com.maddox.il2.ai.EventLog.type("onAircraftLoaded: "+this.thisWeaponsName);
    hierMesh().chunkVisible("ETank_D0", false);
    hierMesh().chunkVisible("RackL_D0", false);
    hierMesh().chunkVisible("RackR_D0", false);
    boolean bCenterRackVisible = false;
    bCenterRackVisible = ( (this.thisWeaponsName.equalsIgnoreCase("tank"))
        || (this.thisWeaponsName.equalsIgnoreCase("tank2x500"))
        || (this.thisWeaponsName.equalsIgnoreCase("tank6x45"))
        || (this.thisWeaponsName.equalsIgnoreCase("tank2x5006x45"))
        || (this.thisWeaponsName.equalsIgnoreCase("1x1000"))
        || (this.thisWeaponsName.equalsIgnoreCase("1x10002x500"))
        || (this.thisWeaponsName.equalsIgnoreCase("1x10006x45"))
        || (this.thisWeaponsName.equalsIgnoreCase("1x10002x5006x45"))
        || (this.thisWeaponsName.indexOf("center_") != -1) );
    hierMesh().chunkVisible("Rack_D0", bCenterRackVisible);
  }

  static
  {
    Class localClass = P_47D10.class;
    new NetAircraft.SPAWN(localClass);

    Property.set(localClass, "iconFar_shortClassName", "P-47");
    Property.set(localClass, "meshName", "3DO/Plane/P-47D-10(Multi1)/hier.him");
    Property.set(localClass, "PaintScheme", new PaintSchemeFMPar03());
    Property.set(localClass, "meshName_us", "3DO/Plane/P-47D-10(USA)/hier.him");
    Property.set(localClass, "PaintScheme_us", new PaintSchemeFMPar06());

    Property.set(localClass, "yearService", 1943.0F);
    Property.set(localClass, "yearExpired", 1947.5F);

    Property.set(localClass, "FlightModel", "FlightModels/P-47D-10.fmd");
    Property.set(localClass, "cockpitClass", (Object)(CockpitP_47D10.class));
    Property.set(localClass, "LOSElevation", 0.9879F);

    weaponTriggersRegister(localClass, new int[] { 0, 0, 0, 0, 1, 1, 1, 1, 9, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 9, 9, 9 });
    weaponHooksRegister(localClass, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalDev01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01" });

    weaponsRegister(localClass, "default", new String[] { "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    weaponsRegister(localClass, "overload", new String[] { "MGunBrowning50k 425", "MGunBrowning50k 425", "MGunBrowning50k 425", "MGunBrowning50k 425", "MGunBrowning50k 425", "MGunBrowning50k 425", "MGunBrowning50k 425", "MGunBrowning50k 425", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    weaponsRegister(localClass, "tank", new String[] { "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "FuelTankGun_Tank75gal", null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    weaponsRegister(localClass, "tank6x45", new String[] { "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "FuelTankGun_Tank75gal", null, null, null, "PylonRO_4andHalfInch_3", "PylonRO_4andHalfInch_3", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", null, null, null });
    weaponsRegister(localClass, "6x45", new String[] { "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", null, null, null, null, "PylonRO_4andHalfInch_3", "PylonRO_4andHalfInch_3", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", null, null, null });
    weaponsRegister(localClass, "1x1000", new String[] { "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", null, null, null, "BombGun1000lbs", null, null, null, null, null, null, null, null, null, null, null });
    weaponsRegister(localClass, "1x10006x45", new String[] { "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", "MGunBrowning50k 200", null, null, null, "BombGun1000lbs", "PylonRO_4andHalfInch_3", "PylonRO_4andHalfInch_3", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", null, null, null });
    
    weaponsRegister(localClass, "*default", new String[] { "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    weaponsRegister(localClass, "*center_tank075", new String[] { "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "FuelTankGun_Tank75gal", null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    weaponsRegister(localClass, "*center_tank108", new String[] { "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", null, null, null, null, null, null, null, null, null, null, null, null, null, null, "FuelTankGun_Tank108gal2" });
    weaponsRegister(localClass, "*center_tank110", new String[] { "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", null, null, null, null, null, null, null, null, null, null, null, null, null, null, "FuelTankGun_Tank110gal2" });
    weaponsRegister(localClass, "*center_tank200", new String[] { "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "FuelTankGun_Tank200gal", null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    weaponsRegister(localClass, "*center_tank075_6x45", new String[] { "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "FuelTankGun_Tank75gal", null, null, null, "PylonRO_4andHalfInch_3", "PylonRO_4andHalfInch_3", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", null, null, null });
    weaponsRegister(localClass, "*center_tank108_6x45", new String[] { "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", null, null, null, null, "PylonRO_4andHalfInch_3", "PylonRO_4andHalfInch_3", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", null, null, "FuelTankGun_Tank108gal2" });
    weaponsRegister(localClass, "*center_tank110_6x45", new String[] { "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", null, null, null, null, "PylonRO_4andHalfInch_3", "PylonRO_4andHalfInch_3", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", null, null, "FuelTankGun_Tank110gal2" });
    weaponsRegister(localClass, "*center_tank200_6x45", new String[] { "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "FuelTankGun_Tank200gal", null, null, null, "PylonRO_4andHalfInch_3", "PylonRO_4andHalfInch_3", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", null, null, null });
    weaponsRegister(localClass, "*6x45", new String[] { "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", null, null, null, null, "PylonRO_4andHalfInch_3", "PylonRO_4andHalfInch_3", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", null, null, null });
    weaponsRegister(localClass, "*center_1x500lbs", new String[] { "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", null, null, null, "BombGun500lbs", null, null, null, null, null, null, null, null, null, null, null });
    weaponsRegister(localClass, "*center_1x500lbs_6x45", new String[] { "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", "MGunBrowningM2 425", null, null, null, "BombGun500lbs", "PylonRO_4andHalfInch_3", "PylonRO_4andHalfInch_3", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", "RocketGun4andHalfInch", null, null, null });
    weaponsRegister(localClass, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
  }
}