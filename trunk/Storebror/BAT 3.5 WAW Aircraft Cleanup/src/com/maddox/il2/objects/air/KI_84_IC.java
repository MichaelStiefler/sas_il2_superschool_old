package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_84_IC extends KI_84 {

    public KI_84_IC() {
    }

    static {
        Class class1 = KI_84_IC.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-84");
        Property.set(class1, "meshName", "3DO/Plane/Ki-84-Ic(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_ja", "3DO/Plane/Ki-84-Ic(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-84-Ia.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_84_IB.class} );
        Property.set(class1, "LOSElevation", 0.0985F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 9, 9, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02" });
        weaponsRegister(class1, "default", new String[] { "MGunHo5s 150", "MGunHo5s 150", "MGunHo115k 65", "MGunHo115k 65", null, null, null, null, null, null });
        weaponsRegister(class1, "2x100", new String[] { "MGunHo5s 150", "MGunHo5s 150", "MGunHo115k 65", "MGunHo115k 65", "BombGun100kgJ 1", "BombGun100kgJ 1", "PylonKI84PLN2", "PylonKI84PLN2", null, null });
        weaponsRegister(class1, "2x250", new String[] { "MGunHo5s 150", "MGunHo5s 150", "MGunHo115k 65", "MGunHo115k 65", "BombGun250kgJ 1", "BombGun250kgJ 1", "PylonKI84PLN2", "PylonKI84PLN2", null, null });
        weaponsRegister(class1, "2tank200", new String[] { "MGunHo5s 150", "MGunHo5s 150", "MGunHo115k 65", "MGunHo115k 65", null, null, "PylonKI84PLN2", "PylonKI84PLN2", "FuelTankGun_TankKi84", "FuelTankGun_TankKi84" });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null });
    }
}
