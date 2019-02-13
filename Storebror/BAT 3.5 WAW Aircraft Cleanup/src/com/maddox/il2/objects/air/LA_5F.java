package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class LA_5F extends LA_X {

    public LA_5F() {
    }

    static {
        Class class1 = LA_5F.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "La");
        Property.set(class1, "meshName", "3DO/Plane/La-5F(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/La-5F.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitLA_5FN.class} );
        Property.set(class1, "LOSElevation", 0.750618F);
        weaponTriggersRegister(class1, new int[] { 1, 1, 3, 3, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02" });
        weaponsRegister(class1, "default", new String[] { "MGunShVAKs 170", "MGunShVAKs 200", null, null, null, null });
        weaponsRegister(class1, "2xFAB50", new String[] { "MGunShVAKs 170", "MGunShVAKs 200", "BombGunFAB50 1", "BombGunFAB50 1", null, null });
        weaponsRegister(class1, "2xFAB100", new String[] { "MGunShVAKs 170", "MGunShVAKs 200", "BombGunFAB100 1", "BombGunFAB100 1", null, null });
        weaponsRegister(class1, "2xDROPTANK", new String[] { "MGunShVAKs 170", "MGunShVAKs 200", null, null, "FuelTankGun_Tank80", "FuelTankGun_Tank80" });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null });
    }
}
