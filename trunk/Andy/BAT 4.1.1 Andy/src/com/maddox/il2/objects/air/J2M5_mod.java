// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 03.11.2020 16:05:47
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   J2M5_mod.java

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.rts.CLASS;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            J2M, PaintSchemeFMPar01, PaintSchemeFCSPar05, Aircraft, 
//            Cockpit, NetAircraft

public class J2M5_mod extends com.maddox.il2.objects.air.J2M
{

    public J2M5_mod()
    {
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        com.maddox.il2.objects.air.Aircraft.xyz[1] = -com.maddox.il2.objects.air.Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, -0.61F);
        hierMesh().chunkSetLocate("Blister1_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
        if(com.maddox.il2.engine.Config.isUSE_RENDER())
        {
            if(com.maddox.il2.game.Main3D.cur3D().cockpits != null && com.maddox.il2.game.Main3D.cur3D().cockpits[0] != null)
                com.maddox.il2.game.Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void replicateDropFuelTanks()
    {
        super.replicateDropFuelTanks();
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        java.lang.Object aobj[] = pos.getBaseAttached();
        if(aobj == null)
            return;
        for(int i = 0; i < aobj.length; i++)
            if(aobj[i] instanceof com.maddox.il2.objects.weapons.FuelTank)
                hierMesh().chunkVisible("Pilon_D0", true);

    }



    static 
    {
        java.lang.Class var_class = com.maddox.rts.CLASS.THIS();
        new NetAircraft.SPAWN(var_class);
        com.maddox.rts.Property.set(var_class, "iconFar_shortClassName", "J2M");
        com.maddox.rts.Property.set(var_class, "meshName", "3DO/Plane/J2M5(Multi1)/hierR.him");
        com.maddox.rts.Property.set(var_class, "PaintScheme", new PaintSchemeFMPar01());
        com.maddox.rts.Property.set(var_class, "meshName_ja", "3DO/Plane/J2M5(ja)/hierR.him");
        com.maddox.rts.Property.set(var_class, "PaintScheme_ja", new PaintSchemeFCSPar05());
        com.maddox.rts.Property.set(var_class, "yearService", 1944F);
        com.maddox.rts.Property.set(var_class, "yearExpired", 1945.5F);
        com.maddox.rts.Property.set(var_class, "FlightModel", "FlightModels/J2M5_mod.fmd");
        com.maddox.rts.Property.set(var_class, "cockpitClass", com.maddox.il2.objects.air.CockpitJ2M5.class);
        com.maddox.rts.Property.set(var_class, "LOSElevation", 1.113F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(var_class, new int[] {
            1, 0, 0, 1, 3, 3, 9, 9
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(var_class, new java.lang.String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02"
        });
    }
}