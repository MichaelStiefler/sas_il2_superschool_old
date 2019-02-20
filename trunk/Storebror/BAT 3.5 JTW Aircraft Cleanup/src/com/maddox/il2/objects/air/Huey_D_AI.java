package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class Huey_D_AI extends HueyXAI
    implements TypeScout, TypeTransport, TypeStormovik
{

    public Huey_D_AI()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(getGunByHookName("_MGUN03") instanceof GunEmpty)
            hierMesh().chunkVisible("miniguns", false);
        if((getBulletEmitterByHookName("_ExternalRock01") instanceof GunEmpty) && (getBulletEmitterByHookName("_ExternalRock02") instanceof GunEmpty) && (getBulletEmitterByHookName("_ExternalRock03") instanceof GunEmpty) && (getBulletEmitterByHookName("_ExternalRock04") instanceof GunEmpty) && (getBulletEmitterByHookName("_ExternalRock05") instanceof GunEmpty) && (getBulletEmitterByHookName("_ExternalRock06") instanceof GunEmpty) && (getBulletEmitterByHookName("_ExternalRock07") instanceof GunEmpty) && (getBulletEmitterByHookName("_ExternalRock08") instanceof GunEmpty) && (getBulletEmitterByHookName("_ExternalRock09") instanceof GunEmpty) && (getBulletEmitterByHookName("_ExternalRock10") instanceof GunEmpty) && (getBulletEmitterByHookName("_ExternalRock11") instanceof GunEmpty) && (getBulletEmitterByHookName("_ExternalRock12") instanceof GunEmpty))
            hierMesh().chunkVisible("Rocklaunch", false);
        if((getBulletEmitterByHookName("_MGUN03") instanceof GunEmpty) && (getBulletEmitterByHookName("_ExternalRock01") instanceof GunEmpty))
            hierMesh().chunkVisible("Mount", false);
        if(getBulletEmitterByHookName("_MGUN01") instanceof GunEmpty)
        {
            hierMesh().chunkVisible("M60L", false);
            hierMesh().chunkVisible("M60R", false);
            hierMesh().chunkVisible("Gunmount", false);
            hierMesh().chunkVisible("Gunmount2", false);
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Pilot4_D0", false);
            hierMesh().chunkVisible("Turret1A_D0", false);
            hierMesh().chunkVisible("Turret2A_D0", false);
        }
        if(World.cur().camouflage == 2)
            hierMesh().chunkVisible("filter", true);
        else
            hierMesh().chunkVisible("filter", false);
    }

    static 
    {
        Class class1 = Huey_D_AI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Huey_D_AI");
        Property.set(class1, "meshName", "3DO/Plane/AI_Buddy_Huey_D/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1956F);
        Property.set(class1, "yearExpired", 1986.5F);
        Property.set(class1, "FlightModel", "FlightModels/Huey_D_AI.fmd:Huey_D_AI_FM");
        Aircraft.weaponTriggersRegister(class1, new int[] {
            11, 11, 1, 1, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", 
            "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalDev01", "_ExternalDev02"
        });
    }
}