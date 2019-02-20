package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_47D30 extends P_47X
{

    public P_47D30()
    {
        bCanopyInit = false;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.CT.bHasCockpitDoorControl = true;
        FM.CT.dvCockpitDoor = 1.0F;
    }

    public void update(float f)
    {
        super.update(f);
        if(!bCanopyInit && FM.isStationedOnGround())
        {
            bCanopyInit = true;
            System.out.println("*** Initial canopy state: " + (FM.CT.getCockpitDoor() != 1.0F ? "closed" : "open"));
        }
    }

    private boolean bCanopyInit;

    static 
    {
        Class class1 = P_47D30.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-47");
        Property.set(class1, "meshName", "3DO/Plane/P-47D-30(Multi1)/hier30.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_us", "3DO/Plane/P-47D-30(USA)/hier30.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1947.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-47D-30.fmd:REPUBLIC");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitP_47D30.class
        });
        Property.set(class1, "LOSElevation", 1.1104F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 1, 1, 1, 1, 9, 3, 
            3, 3, 9, 9, 2, 2, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalDev01", "_ExternalBomb02", 
            "_ExternalBomb03", "_ExternalBomb01", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06"
        });
    }
}