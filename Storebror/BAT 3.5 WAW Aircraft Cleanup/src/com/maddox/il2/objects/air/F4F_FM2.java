package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F4F_FM2 extends F4F implements TypeStormovik {

    public F4F_FM2() {
    }

    static {
        Class class1 = F4F_FM2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FM2");
        Property.set(class1, "meshName", "3DO/Plane/FM-2(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_us", "3DO/Plane/FM-2(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/FM-2.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF4F4.class} );
        Property.set(class1, "LOSElevation", 1.28265F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb01", "_ExternalBomb02" });
        weaponsRegister(class1, "default", new String[] { "MGunBrowning50kWF 430", "MGunBrowning50kWF 430", "MGunBrowning50kWF 430", "MGunBrowning50kWF 430", null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2x58dt", new String[] { "MGunBrowning50kWF 430", "MGunBrowning50kWF 430", "MGunBrowning50kWF 430", "MGunBrowning50kWF 430", "PylonF4FPLN1", "PylonF4FPLN1", "FuelTankGun_TankF4F", "FuelTankGun_TankF4F", null, null, null, null });
        weaponsRegister(class1, "2x100", new String[] { "MGunBrowning50kWF 430", "MGunBrowning50kWF 430", "MGunBrowning50kWF 430", "MGunBrowning50kWF 430", null, null, null, null, "PylonF4FPLN2", "PylonF4FPLN2", "BombGun100lbs 1", "BombGun100lbs 1" });
        weaponsRegister(class1, "2x1002x58dt", new String[] { "MGunBrowning50kWF 430", "MGunBrowning50kWF 430", "MGunBrowning50kWF 430", "MGunBrowning50kWF 430", "PylonF4FPLN1", "PylonF4FPLN1", "FuelTankGun_TankF4F", "FuelTankGun_TankF4F", "PylonF4FPLN2", "PylonF4FPLN2", "BombGun100lbs 1", "BombGun100lbs 1" });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null });
    }
}
