package com.maddox.il2.objects.air;

import com.maddox.il2.ai.War;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class HampdenMkI extends HAMPDEN
{

    public HampdenMkI()
    {
        pilot3kill = false;
        BlisTurOpen = false;
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.625F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(thisWeaponsName.startsWith("1 x"))
        {
            hierMesh().chunkVisible("Bay3_D0", true);
            hierMesh().chunkVisible("Bay4_D0", true);
            hierMesh().chunkVisible("PilonTorp_D0", true);
            hierMesh().chunkVisible("Bay1_D0", false);
            hierMesh().chunkVisible("Bay2_D0", false);
            return;
        } else
        {
            return;
        }
    }

    public void rareAction(float f, boolean flag)
    {
        Aircraft aircraft = War.getNearestEnemy(this, 6000F);
        if(!pilot3kill && aircraft != null && !FM.AS.bIsAboutToBailout)
        {
            hierMesh().chunkVisible("Blister3_D0", false);
            hierMesh().chunkVisible("BlisturretOpen_D0", true);
            BlisTurOpen = true;
        }
        if(!pilot3kill && aircraft == null && !FM.AS.bIsAboutToBailout && BlisTurOpen)
        {
            hierMesh().chunkVisible("Blister3_D0", true);
            hierMesh().chunkVisible("BlisturretOpen_D0", false);
            BlisTurOpen = false;
        }
    }

    public void update(float f)
    {
        super.update(f);
        if(!FM.isPlayers() && FM.Gears.onGround())
            if(FM.EI.engines[1].getRPM() < 100F)
                FM.CT.cockpitDoorControl = 1.0F;
            else
                FM.CT.cockpitDoorControl = 0.0F;
        if(FM.AS.bIsAboutToBailout && BlisTurOpen && !pilot3kill)
        {
            hierMesh().chunkVisible("BlisturretOpen_D0", false);
            hierMesh().chunkVisible("Blister3_D0", true);
            BlisTurOpen = false;
        }
    }

    public void doKillPilot(int i)
    {
        switch(i)
        {
        case 2:
            FM.turret[0].bIsOperable = false;
            break;

        case 3:
            FM.turret[1].bIsOperable = false;
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            hierMesh().chunkVisible("HMask2_D0", false);
            break;

        case 2:
            pilot3kill = true;
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            hierMesh().chunkVisible("HMask3_D0", false);
            break;

        case 3:
            hierMesh().chunkVisible("Pilot4_D0", false);
            hierMesh().chunkVisible("Pilot4_D1", true);
            hierMesh().chunkVisible("HMask4_D0", false);
            break;

        case 4:
            hierMesh().chunkVisible("Pilot5_D0", false);
            hierMesh().chunkVisible("Pilot5_D1", true);
            hierMesh().chunkVisible("HMask5_D0", false);
            break;
        }
    }

    private boolean pilot3kill;
    private boolean BlisTurOpen;

    static 
    {
        Class class1 = HampdenMkI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hampden");
        Property.set(class1, "meshName", "3DO/Plane/Hampden-MkI/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/HAMP.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitHampdenMkI.class, CockpitHampdenMkI_Bombardier.class, CockpitHampden_TGunner.class, CockpitHampden_BGunner.class
        });
        Property.set(class1, "LOSElevation", 0.7394F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 10, 10, 11, 11, 3, 3, 3, 3, 2, 
            9, 9, 3, 3, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", 
            "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb04", "_ExternalBomb03", "_ExternalBomb05", "_ExternalBomb06"
        });
    }
}
