// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 15.12.2019 10:53:20
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   Avro_Bison.java

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Biplanexyz, PaintSchemeFMPar00, NetAircraft, PaintScheme, 
//            Aircraft

public class Avro_Bison extends com.maddox.il2.objects.air.Biplanexyz
{

    public Avro_Bison()
    {
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 40F * f, 0.0F);
        hierMesh().chunkSetAngles("SlatR_D0", 0.0F, -40F * f, 0.0F);
    }

    public void update(float f)
    {
        super.update(f);
        super.onAircraftLoaded();
        if(super.FM.isPlayers())
            if(!com.maddox.il2.game.Main3D.cur3D().isViewOutside())
            {
                hierMesh().chunkVisible("Blister1_D0", false);
                hierMesh().chunkVisible("AroneL_D0", false);
                hierMesh().chunkVisible("AroneR_D0", false);
                hierMesh().chunkVisible("StrutsL_D0", false);
                hierMesh().chunkVisible("StrutsR_D0", false);
                hierMesh().chunkVisible("WingLMid_D0", false);
                hierMesh().chunkVisible("WingRMid_D0", false);
                hierMesh().chunkVisible("WingLOut_D0", false);
                hierMesh().chunkVisible("WingROut_D0", false);
                hierMesh().chunkVisible("WireL_D0", false);
                hierMesh().chunkVisible("WireR_D0", false);
                hierMesh().chunkVisible("CF_D0", false);
                hierMesh().chunkVisible("Engine1_D0", false);
                hierMesh().chunkVisible("SlatL_D0", false);
                hierMesh().chunkVisible("SlatR_D0", false);
            } else
            {
                hierMesh().chunkVisible("Blister1_D0", true);
                hierMesh().chunkVisible("AroneL_D0", true);
                hierMesh().chunkVisible("AroneR_D0", true);
                hierMesh().chunkVisible("StrutsL_D0", true);
                hierMesh().chunkVisible("StrutsR_D0", true);
                hierMesh().chunkVisible("WingLMid_D0", true);
                hierMesh().chunkVisible("WingRMid_D0", true);
                hierMesh().chunkVisible("WingLOut_D0", true);
                hierMesh().chunkVisible("WingROut_D0", true);
                hierMesh().chunkVisible("WireL_D0", true);
                hierMesh().chunkVisible("WireR_D0", true);
                hierMesh().chunkVisible("CF_D0", true);
                hierMesh().chunkVisible("Engine1_D0", true);
                hierMesh().chunkVisible("SlatL_D0", true);
                hierMesh().chunkVisible("SlatR_D0", true);
            }
        if(super.FM.isPlayers())
        {
            if(!com.maddox.il2.game.Main3D.cur3D().isViewOutside())
                hierMesh().chunkVisible("Blister1_D1", false);
            hierMesh().chunkVisible("Blister1_D2", false);
            hierMesh().chunkVisible("AroneL_D1", false);
            hierMesh().chunkVisible("AroneR_D1", false);
            hierMesh().chunkVisible("StrutsL_D1", false);
            hierMesh().chunkVisible("StrutsR_D1", false);
            hierMesh().chunkVisible("AroneL_D2", false);
            hierMesh().chunkVisible("AroneR_D2", false);
            hierMesh().chunkVisible("StrutsL_D2", false);
            hierMesh().chunkVisible("StrutsR_D2", false);
            hierMesh().chunkVisible("WingLMid_D1", false);
            hierMesh().chunkVisible("WingRMid_D1", false);
            hierMesh().chunkVisible("WingLOut_D1", false);
            hierMesh().chunkVisible("WingROut_D1", false);
            hierMesh().chunkVisible("WireL_D1", false);
            hierMesh().chunkVisible("WireR_D1", false);
            hierMesh().chunkVisible("CF_D1", false);
            hierMesh().chunkVisible("Engine1_D1", false);
            hierMesh().chunkVisible("WingLMid_D2", false);
            hierMesh().chunkVisible("WingRMid_D2", false);
            hierMesh().chunkVisible("WingLOut_D2", false);
            hierMesh().chunkVisible("WingROut_D2", false);
            hierMesh().chunkVisible("WireL_D2", false);
            hierMesh().chunkVisible("WireR_D2", false);
            hierMesh().chunkVisible("CF_D2", false);
            hierMesh().chunkVisible("Engine1_D2", false);
            hierMesh().chunkVisible("SlatL_D1", false);
            hierMesh().chunkVisible("SlatR_D1", false);
            hierMesh().chunkVisible("SlatL_D2", false);
            hierMesh().chunkVisible("SlatR_D2", false);
        }
    }

    static java.lang.Class _mthclass$(java.lang.String s)
    {
        try
        {
            return java.lang.Class.forName(s);
        }
        catch(java.lang.ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.Avro_Bison.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Bison");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Avro_Bison/hierBison.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        com.maddox.rts.Property.set(class1, "originCountry", com.maddox.il2.objects.air.PaintScheme.countryBritain);
        com.maddox.rts.Property.set(class1, "yearService", 1922F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1936F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/OspreyL.fmd:OspreyL_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitAvro_Bison.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.742F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 10, 9, 9, 9, 9, 3, 3, 3, 
            3, 9, 9, 3, 3, 3, 3, 3, 3, 3, 
            3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", 
            "_ExternalBomb04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", 
            "_ExternalBomb12"
        });
    }
}