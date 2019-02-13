package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CR_42 extends CR_42X {

    public CR_42() {
    }

    static {
        Class class1 = CR_42.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "CR.42");
        Property.set(class1, "meshName", "3DO/Plane/CR42(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_it", "3DO/Plane/CR42(it)/hier.him");
        Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/CR42.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitCR42.class} );
        Property.set(class1, "LOSElevation", 0.742F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02" });
        weaponsRegister(class1, "default", new String[] { "MGunBredaSAFAT127siCR42 400", "MGunBredaSAFAT127siCR42 400", null, null });
        weaponsRegister(class1, "2sc50", new String[] { "MGunBredaSAFAT127siCR42 400", "MGunBredaSAFAT127siCR42 400", "BombGunSC50 1", "BombGunSC50 1" });
        weaponsRegister(class1, "2sc70", new String[] { "MGunBredaSAFAT127siCR42 400", "MGunBredaSAFAT127siCR42 400", "BombGunSC70 1", "BombGunSC70 1" });
        weaponsRegister(class1, "2x50", new String[] { "MGunBredaSAFAT127siCR42 400", "MGunBredaSAFAT127siCR42 400", "BombGunIT_50_M 1", "BombGunIT_50_M 1" });
        weaponsRegister(class1, "2x100", new String[] { "MGunBredaSAFAT127siCR42 400", "MGunBredaSAFAT127siCR42 400", "BombGunIT_100_M 1", "BombGunIT_100_M 1" });
        weaponsRegister(class1, "none", new String[] { null, null, null, null });
    }
}
