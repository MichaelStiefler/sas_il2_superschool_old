package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_61_IHEI extends KI_61 {

    public KI_61_IHEI() {
    }

    static {
        Class class1 = KI_61_IHEI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-61");
        Property.set(class1, "meshName", "3DO/Plane/Ki-61-I(Hei)(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_ja", "3DO/Plane/Ki-61-I(Hei)(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1946F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-61-IHei.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_61.class} );
        Property.set(class1, "LOSElevation", 0.81055F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02" });
        weaponsRegister(class1, "default", new String[] { "MGunHo103si 250", "MGunHo103si 250", "MGunMG15120k 120", "MGunMG15120k 120", null, null, null, null });
        weaponsRegister(class1, "1x250", new String[] { "MGunHo103si 250", "MGunHo103si 250", "MGunMG15120k 120", "MGunMG15120k 120", null, "BombGun250kgJ 1", null, null });
        weaponsRegister(class1, "2x250", new String[] { "MGunHo103si 250", "MGunHo103si 250", "MGunMG15120k 120", "MGunMG15120k 120", "BombGun250kgJ 1", "BombGun250kgJ 1", null, null });
        weaponsRegister(class1, "2x150dt", new String[] { "MGunHo103si 250", "MGunHo103si 250", "MGunMG15120k 120", "MGunMG15120k 120", null, null, "FuelTankGun_TankKi61Underwing", "FuelTankGun_TankKi61Underwing" });
        weaponsRegister(class1, "1x150dt", new String[] { "MGunHo103si 250", "MGunHo103si 250", "MGunMG15120k 120", "MGunMG15120k 120", null, null, null, "FuelTankGun_TankKi61Underwing" });
        weaponsRegister(class1, "1x150dt+1x250kg", new String[] { "MGunHo103si 250", "MGunHo103si 250", "MGunMG15120k 120", "MGunMG15120k 120", null, "BombGun250kgJ 1", "FuelTankGun_TankKi61Underwing", null });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null });
    }
}
