// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 05.10.2019 10:01:36
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   DH_86.java

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            DH_86xyz, PaintSchemeBMPar00, Aircraft, NetAircraft

public class DH_86 extends com.maddox.il2.objects.air.DH_86xyz
{

    public DH_86()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(super.thisWeaponsName.startsWith("8xWounded"))
        {
            hierMesh().chunkVisible("Mash1", true);
            hierMesh().chunkVisible("Mash2", true);
            hierMesh().chunkVisible("Mash3", true);
            hierMesh().chunkVisible("Mash4", true);
            hierMesh().chunkVisible("Mash5", true);
            hierMesh().chunkVisible("Mash6", true);
            hierMesh().chunkVisible("Mash7", true);
            hierMesh().chunkVisible("Mash8", true);
            hierMesh().chunkVisible("Nurse_D0", true);
            hierMesh().chunkVisible("NurseHead_D0", true);
            hierMesh().chunkVisible("Seats_N", true);
            hierMesh().chunkVisible("Seats_L", false);
            hierMesh().chunkVisible("Seats_R", false);
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).M.massEmpty += 900F;
        } else
        if(super.thisWeaponsName.startsWith("3xCrates"))
        {
            hierMesh().chunkVisible("Seats_L", false);
            hierMesh().chunkVisible("Seats_R", false);
            hierMesh().chunkVisible("BOX1", true);
            hierMesh().chunkVisible("BOX2", true);
            hierMesh().chunkVisible("BOX3", true);
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        drawBombs();
        if(super.FM.getAltitude() < 3000F)
        {
            for(int i = 1; i < 3; i++)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);

        } else
        {
            for(int i = 1; i < 3; i++)
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

        }
    }

    protected void drawBombs()
    {
        int i = 0;
        if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3] != null)
        {
            for(int k = 0; k < ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3].length; k++)
                if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][k] != null)
                    i += ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][k].countBullets();

            if(super.thisWeaponsName.startsWith("3xCrates"))
            {
                for(int l = i + 1; l <= 3; l++)
                    hierMesh().chunkVisible("BOX" + l, false);

            }
        }
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.DH_86.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "DH-86");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/DH-86/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        com.maddox.rts.Property.set(class1, "yearService", 1937F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1945F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/DH86.fmd:DH86_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitDH_86P.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.5265F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_BombSpawn01"
        });
    }
}