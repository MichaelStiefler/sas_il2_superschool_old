// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 15.07.2020 17:10:54
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   KI_98_3.java

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Mass;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            KI_98x, PaintSchemeFMPar01, PaintSchemeFMPar05, TypeFighter, 
//            TypeStormovik, NetAircraft, Aircraft

public class KI_98_3 extends com.maddox.il2.objects.air.KI_98x
    implements com.maddox.il2.objects.air.TypeFighter, com.maddox.il2.objects.air.TypeStormovik
{

    public KI_98_3()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(thisWeaponsName.startsWith("S1:") || thisWeaponsName.startsWith("S2:") || thisWeaponsName.startsWith("LR"))
        {
            hierMesh().chunkVisible("PilonL_D0", false);
            hierMesh().chunkVisible("PilonR_D0", false);
            hierMesh().chunkVisible("PilonLout_D0", false);
            hierMesh().chunkVisible("PilonRout_D0", false);
        } else
        if(thisWeaponsName.startsWith("S3:"))
        {
            hierMesh().chunkVisible("PilonL_D0", false);
            hierMesh().chunkVisible("PilonR_D0", false);
            hierMesh().chunkVisible("PilonLout_D0", false);
            hierMesh().chunkVisible("PilonRout_D0", false);
            hierMesh().chunkVisible("cannon3_D0", false);
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).M.massEmpty -= 100F;
        } else
        if(thisWeaponsName.startsWith("S3+"))
        {
            hierMesh().chunkVisible("cannon3_D0", false);
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).M.massEmpty -= 100F;
        } else
        {
            return;
        }
    }


    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.KI_98_3.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "ki-98-II");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/ki-98-II/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        com.maddox.rts.Property.set(class1, "meshName_ja", "3DO/Plane/ki-98-II/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme_ja", new PaintSchemeFMPar05());
        com.maddox.rts.Property.set(class1, "yearService", 1945F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1947F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Ki_98_III.fmd:Ki_98_III_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitKi98_2.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 1.0151F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 1, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 
            9
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_Cannon01", "_Cannon02", "_Cannon03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", 
            "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", 
            "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", 
            "_ExternalDev04"
        });
    }
}