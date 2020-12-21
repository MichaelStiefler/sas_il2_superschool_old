// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 29.09.2020 10:06:12
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   F8F1.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.CLASS;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            F8Fabc, PaintSchemeFMPar05, NetAircraft, Aircraft

public class F8F1 extends com.maddox.il2.objects.air.F8Fabc
{

    public F8F1()
    {
        flapps = 0.0F;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(!super.FM.isPlayers())
        {
            float f1 = com.maddox.il2.fm.Pitot.Indicator((float)FM.Loc.z, FM.getSpeed());
            if(FM.getAltitude() > 500F || f1 > 51.44F)
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.cockpitDoorControl = 0.0F;
            else
                ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.cockpitDoorControl = 1.0F;
        }
    }

    public void update(float f)
    {
        super.update(f);
        float f1 = FM.EI.engines[0].getControlRadiator();
        if(java.lang.Math.abs(flapps - f1) > 0.01F)
        {
            flapps = f1;
            for(int i = 1; i < 5; i++)
                hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -22F * f1, 0.0F);

        }
    }


    private float flapps;

    static 
    {
        java.lang.Class class1 = com.maddox.rts.CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "F8F");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/F8F-1(Multi1)/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        com.maddox.rts.Property.set(class1, "yearService", 1945.5F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1955.5F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/F8F-2.fmd");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitF8F1.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 1.0585F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 9, 3, 3, 3, 3, 9, 
            9, 9, 9, 2, 2, 2, 2, 2, 2
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev03", 
            "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06"
        });
    }
}