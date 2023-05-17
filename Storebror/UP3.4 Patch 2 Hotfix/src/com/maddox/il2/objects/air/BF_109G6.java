package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class BF_109G6 extends BF_109Gx implements TypeBNZFighter {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.initIntakeAndFlaps();
    }
    
    static {
        Class class1 = BF_109G6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3DO/Plane/Bf-109G-6Early/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "meshName_fi", "3DO/Plane/Bf-109G-6Early(fi)/hier.him");
        Property.set(class1, "PaintScheme_fi", new PaintSchemeFCSPar02());
        Property.set(class1, "meshName_hu", "3DO/Plane/Bf-109G-6Early(hu)/hier.him");
        Property.set(class1, "PaintScheme_hu", new PaintSchemeFMPar04());
        Property.set(class1, "meshName_sk", "3DO/Plane/Bf-109G-6Early(sk)/hier.him");
        Property.set(class1, "PaintScheme_sk", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109G-6Early.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109G6.class });
        Property.set(class1, "LOSElevation", 0.7498F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 1, 1, 1, 1, 1, 9, 9, 9, 9, 2, 2, 9, 9, 3, 3, 3, 3, 3, 3, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_ExternalDev01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01",
                "_ExternalRock02", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08" });
    }
}
