package com.maddox.il2.objects.air;

import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class J2M5 extends J2M {

    public J2M5() {
    }

    static {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "J2M");
        Property.set(class1, "meshName", "3DO/Plane/J2M5(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ja", "3DO/Plane/J2M5(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/J2M5.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJ2M5.class} );
        Property.set(class1, "LOSElevation", 1.113F);
        weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02" });
        weaponsRegister(class1, "default", new String[] { "MGunHo5k 210", "MGunHo5k 190", "MGunHo5k 190", "MGunHo5k 210", null, null });
        weaponsRegister(class1, "2x60", new String[] { "MGunHo5k 210", "MGunHo5k 190", "MGunHo5k 190", "MGunHo5k 210", "BombGun60kgJ 1", "BombGun60kgJ 1" });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null });
    }
}
