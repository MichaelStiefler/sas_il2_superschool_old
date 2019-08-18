// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 12.05.2019 09:38:39
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   SKUA_B24.java

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Turret;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            SKUA, TypeStormovik, TypeDiveBomber, PaintSchemeBMPar_Skua, 
//            Aircraft, NetAircraft

public class SKUA_B24 extends com.maddox.il2.objects.air.SKUA
    implements com.maddox.il2.objects.air.TypeStormovik, com.maddox.il2.objects.air.TypeDiveBomber
{

    public SKUA_B24()
    {
        btme = -1L;
        bGunUp = false;
        fGunPos = 0.0F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(super.thisWeaponsName.startsWith("def") || super.thisWeaponsName.startsWith("no"))
        {
            hierMesh().chunkVisible("Arm1x_D0", false);
            hierMesh().chunkVisible("Arm1_D0", false);
            hierMesh().chunkVisible("Arm2_D0", false);
        }
    }

    public void update(float f)
    {
        super.update(f);
        if(!bGunUp)
        {
            if(fGunPos > 0.0F)
            {
                fGunPos -= 0.2F * f;
                super.FM.turret[0].bIsOperable = false;
                hierMesh().chunkVisible("Turret1A_D0", false);
                hierMesh().chunkVisible("Turret1B_D0", false);
                hierMesh().chunkVisible("Turdown_D0", true);
            }
        } else
        if(fGunPos < 1.0F)
        {
            fGunPos += 0.2F * f;
            if(fGunPos > 0.8F && fGunPos < 0.9F)
            {
                super.FM.turret[0].bIsOperable = true;
                hierMesh().chunkVisible("Turret1A_D0", true);
                hierMesh().chunkVisible("Turret1B_D0", true);
                hierMesh().chunkVisible("Turdown_D0", false);
            }
        }
        if(fGunPos < 0.333F)
            hierMesh().chunkSetAngles("Blister2_D0", 0.0F, -com.maddox.il2.objects.air.Aircraft.cvt(fGunPos, 0.0F, 0.333F, 0.0F, 41F), 0.0F);
        else
        if(fGunPos < 0.666F)
        {
            resetYPRmodifier();
            com.maddox.il2.objects.air.Aircraft.xyz[1] = com.maddox.il2.objects.air.Aircraft.cvt(fGunPos, 0.333F, 0.666F, 0.0F, -0.4F);
        } else
        {
            hierMesh().chunkSetAngles("Blister2_D0", 0.0F, -com.maddox.il2.objects.air.Aircraft.cvt(fGunPos, 0.666F, 1.0F, 41F, 71F), 0.0F);
        }
        if(super.FM.turret[0].bIsAIControlled)
        {
            if(super.FM.turret[0].target != null && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astatePilotStates[2] < 90)
                bGunUp = true;
            if(com.maddox.rts.Time.current() > btme)
            {
                btme = com.maddox.rts.Time.current() + com.maddox.il2.ai.World.Rnd().nextLong(5000L, 12000L);
                if(super.FM.turret[0].target == null && ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.astatePilotStates[2] < 90)
                    bGunUp = false;
            }
        }
    }

    boolean bGunUp;
    public long btme;
    public float fGunPos;

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.SKUA_B24.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "SKUA");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/SKUA/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar_Skua());
        com.maddox.rts.Property.set(class1, "yearService", 1938F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1943F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/SKUA.fmd:SKUA_FM");
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.87195F);
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitSKUA.class
        });
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 10, 3, 3, 9, 9, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb03", 
            "_ExternalBomb04", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb07", 
            "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", 
            "_ExternalBomb14", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb17", 
            "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", 
            "_ExternalBomb24", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalBomb25", "_ExternalBomb26"
        });
    }
}