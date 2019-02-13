package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F4UCORSAIR2 extends F4U {

    public F4UCORSAIR2() {
    }

    static {
        Class class1 = F4UCORSAIR2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F4U");
        Property.set(class1, "meshName", "3DO/Plane/CorsairMkII(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "meshName_gb", "3DO/Plane/CorsairMkII(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/F4U-1Aclipped.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF4U1A.class} );
        Property.set(class1, "LOSElevation", 1.0585F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 3, 3, 3, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb02", "_ExternalBomb03" });
        weaponsRegister(class1, "default", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 375", "MGunBrowning50kWF 375", null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2x154dt", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 375", "MGunBrowning50kWF 375", null, null, "PylonF4UPLN3", "PylonF4UPLN3", null, null, null, "FuelTankGun_Tank154gal", "FuelTankGun_Tank154gal" });
        weaponsRegister(class1, "1x178dt", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 375", "MGunBrowning50kWF 375", null, "FuelTankGun_Tank178gal", null, null, null, null, null, null, null });
        weaponsRegister(class1, "1x178dt2x154dt", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 375", "MGunBrowning50kWF 375", null, "FuelTankGun_Tank178gal", "PylonF4UPLN3", "PylonF4UPLN3", null, null, null, "FuelTankGun_Tank154gal", "FuelTankGun_Tank154gal" });
        weaponsRegister(class1, "2x1541x178dt", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 375", "MGunBrowning50kWF 375", null, "FuelTankGun_Tank178gal", "PylonF4UPLN3", "PylonF4UPLN3", null, "BombGun154Napalm 1", "BombGun154Napalm 1", null, null });
        weaponsRegister(class1, "1x5002x154dt", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 375", "MGunBrowning50kWF 375", "PylonF4UPLN2", null, "PylonF4UPLN3", "PylonF4UPLN3", "BombGun500lbsE 1", null, null, "FuelTankGun_Tank154gal", "FuelTankGun_Tank154gal" });
        weaponsRegister(class1, "2x500", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 375", "MGunBrowning50kWF 375", null, null, "PylonF4UPLN3", "PylonF4UPLN3", null, "BombGun500lbsE 1", "BombGun500lbsE 1", null, null });
        weaponsRegister(class1, "1x10002x154dt", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 375", "MGunBrowning50kWF 375", "PylonF4UPLN2", null, "PylonF4UPLN3", "PylonF4UPLN3", "BombGun1000lbsGPE 1", null, null, "FuelTankGun_Tank154gal", "FuelTankGun_Tank154gal" });
        weaponsRegister(class1, "2x1000", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 375", "MGunBrowning50kWF 375", null, null, "PylonF4UPLN3", "PylonF4UPLN3", null, "BombGun1000lbsGPE 1", "BombGun1000lbsGPE 1", null, null });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        class1 = F4U.class;
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
    }
}
