package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class A6M3 extends A6M {

    public A6M3() {
    }

    static {
        Class class1 = A6M3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A6M");
        Property.set(class1, "meshName", "3DO/Plane/A6M3(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1940.5F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/A6M3.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitA6M2.class} );
        Property.set(class1, "LOSElevation", 1.01885F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 3, 9, 9, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalDev01", "_ExternalBomb02", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb03", "_ExternalBomb04" });
        weaponsRegister(class1, "default", new String[] { "MGunMG15spzl 1000", "MGunMG15spzl 1000", "MGunMGFFk 100", "MGunMGFFk 100", null, null, null, null, null, null, null });
        weaponsRegister(class1, "1xdt", new String[] { "MGunMG15spzl 1000", "MGunMG15spzl 1000", "MGunMGFFk 100", "MGunMGFFk 100", "FuelTankGun_Tank0", null, null, null, null, null, null });
        weaponsRegister(class1, "1x250", new String[] { "MGunMG15spzl 1000", "MGunMG15spzl 1000", "MGunMGFFk 100", "MGunMGFFk 100", null, "PylonA6MPLN1", "BombGun250kg", null, null, null, null });
        weaponsRegister(class1, "2x60", new String[] { "MGunMG15spzl 1000", "MGunMG15spzl 1000", "MGunMGFFk 100", "MGunMGFFk 100", null, null, null, "PylonA6MPLN2", "PylonA6MPLN2", "BombGun50kg", "BombGun50kg" });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null });
    }
}
