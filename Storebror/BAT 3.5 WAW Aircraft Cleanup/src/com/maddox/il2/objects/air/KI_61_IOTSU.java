package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_61_IOTSU extends KI_61 {

    public KI_61_IOTSU() {
    }

    static {
        Class class1 = KI_61_IOTSU.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-61");
        Property.set(class1, "meshName", "3DO/Plane/Ki-61-I(Otsu)(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_ja", "3DO/Plane/Ki-61-I(Otsu)(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeBCSPar01());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1946F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-61-IOtsu.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_61.class} );
        Property.set(class1, "LOSElevation", 0.81055F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02" });
        weaponsRegister(class1, "default", new String[] { "MGunHo103si 250", "MGunHo103si 250", "MGunHo103k 250", "MGunHo103k 250", null, null, null, null });
        weaponsRegister(class1, "1x250", new String[] { "MGunHo103si 250", "MGunHo103si 250", "MGunHo103k 250", "MGunHo103k 250", null, "BombGun250kgJ 1", null, null });
        weaponsRegister(class1, "2x250", new String[] { "MGunHo103si 250", "MGunHo103si 250", "MGunHo103k 250", "MGunHo103k 250", "BombGun250kgJ 1", "BombGun250kgJ 1", null, null });
        weaponsRegister(class1, "2x150dt", new String[] { "MGunHo103si 250", "MGunHo103si 250", "MGunHo103k 250", "MGunHo103k 250", null, null, "FuelTankGun_TankKi61Underwing", "FuelTankGun_TankKi61Underwing" });
        weaponsRegister(class1, "1x150dt", new String[] { "MGunHo103si 250", "MGunHo103si 250", "MGunHo103k 250", "MGunHo103k 250", null, null, null, "FuelTankGun_TankKi61Underwing" });
        weaponsRegister(class1, "1x150dt+1x250kg", new String[] { "MGunHo103si 250", "MGunHo103si 250", "MGunHo103k 250", "MGunHo103k 250", null, "BombGun250kgJ 1", "FuelTankGun_TankKi61Underwing", null });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null });
    }
}
