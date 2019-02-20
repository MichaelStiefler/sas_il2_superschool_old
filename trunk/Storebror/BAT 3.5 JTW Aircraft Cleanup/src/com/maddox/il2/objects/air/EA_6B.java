package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class EA_6B extends A_6fuelReceiver
{

    public EA_6B()
    {
        ratdeg = 0.0F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        this.fuelReceiveRate = 10.093F;
        if(thisWeaponsName.endsWith("__ALQ3"))
        {
            hierMesh().chunkVisible("ALQ99Body_1", true);
            hierMesh().chunkVisible("ALQ99prop_1", true);
            hierMesh().chunkVisible("ALQ99Body_3", true);
            hierMesh().chunkVisible("ALQ99prop_3", true);
            hierMesh().chunkVisible("ALQ99Body_5", true);
            hierMesh().chunkVisible("ALQ99prop_5", true);
            FM.M.massEmpty += 1446F;
            FM.Sq.dragProducedCx += 0.08400001F;
        }
        if(thisWeaponsName.endsWith("__ALQ4"))
        {
            hierMesh().chunkVisible("ALQ99Body_1", true);
            hierMesh().chunkVisible("ALQ99prop_1", true);
            hierMesh().chunkVisible("ALQ99Body_2", true);
            hierMesh().chunkVisible("ALQ99prop_2", true);
            hierMesh().chunkVisible("ALQ99Body_4", true);
            hierMesh().chunkVisible("ALQ99prop_4", true);
            hierMesh().chunkVisible("ALQ99Body_5", true);
            hierMesh().chunkVisible("ALQ99prop_5", true);
            FM.M.massEmpty += 1928F;
            FM.Sq.dragProducedCx += 0.112F;
        }
        if(thisWeaponsName.endsWith("__ALQ5"))
        {
            hierMesh().chunkVisible("ALQ99Body_1", true);
            hierMesh().chunkVisible("ALQ99prop_1", true);
            hierMesh().chunkVisible("ALQ99Body_2", true);
            hierMesh().chunkVisible("ALQ99prop_2", true);
            hierMesh().chunkVisible("ALQ99Body_3", true);
            hierMesh().chunkVisible("ALQ99prop_3", true);
            hierMesh().chunkVisible("ALQ99Body_4", true);
            hierMesh().chunkVisible("ALQ99prop_4", true);
            hierMesh().chunkVisible("ALQ99Body_5", true);
            hierMesh().chunkVisible("ALQ99prop_5", true);
            FM.M.massEmpty += 2410F;
            FM.Sq.dragProducedCx += 0.14F;
        }
    }

    public void update(float f)
    {
        if(FM.getSpeedKMH() > 185F)
            RATrot();
        super.update(f);
    }

    public void missionStarting()
    {
        super.missionStarting();
    }

    private void RATrot()
    {
        if(FM.getSpeedKMH() < 250F)
            ratdeg += 10F;
        else
        if(FM.getSpeedKMH() < 400F)
            ratdeg += 20F;
        else
        if(FM.getSpeedKMH() < 550F)
            ratdeg += 25F;
        else
            ratdeg += 31F;
        if(ratdeg > 720F)
            ratdeg -= 1440F;
        for(int i = 1; i < 6; i++)
        {
            hierMesh().chunkSetAngles("ALQ99prop_" + i, 0.0F, 0.0F, ratdeg);
            if(hierMesh().isChunkVisible("ALQ99Body_" + i))
                if(FM.getSpeedKMH() > 300F)
                {
                    hierMesh().chunkVisible("ALQ99proprot_" + i, true);
                    hierMesh().chunkVisible("ALQ99prop_" + i, false);
                } else
                {
                    hierMesh().chunkVisible("ALQ99proprot_" + i, false);
                    hierMesh().chunkVisible("ALQ99prop_" + i, true);
                }
        }

    }

    private float ratdeg;

    static 
    {
        Class class1 = EA_6B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "EA-6B");
        Property.set(class1, "meshName", "3DO/Plane/EA-6B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 2002F);
        Property.set(class1, "FlightModel", "FlightModels/EA6B.fmd:A6");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitEA_6B.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 
            2, 2, 2, 2, 7, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_ExternalDev06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalRock35", "_ExternalRock35", "_ExternalRock36", "_ExternalRock36", 
            "_ExternalRock37", "_ExternalRock37", "_ExternalRock38", "_ExternalRock38", "_Flare01", "_Chaff01"
        });
    }
}
