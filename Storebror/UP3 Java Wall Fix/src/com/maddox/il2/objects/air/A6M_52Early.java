package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class A6M_52Early extends A6M
{

    public A6M_52Early()
    {
    }

    static 
    {
        Class class1 = A6M_52Early.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A6M");
        Property.set(class1, "meshName", "3DO/Plane/A6M-52Early(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "meshName_ja", "3DO/Plane/A6M-52Early(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar02());
        Property.set(class1, "yearService", 1943.5F);
        Property.set(class1, "yearExpired", 1943.5F);
        Property.set(class1, "FlightModel", "FlightModels/A6M5a.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitA6M5a.class
        });
        Property.set(class1, "LOSElevation", 1.01885F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 1, 1, 9, 9, 3, 9, 9, 3, 
            3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalDev01", "_ExternalBomb02", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb03", 
            "_ExternalBomb04"
        });
    }
}