// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 16.05.2019 16:52:05
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   Stearman.java

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, PaintSchemeFMPar00, TypeFighter, TypeTNBFighter, 
//            Aircraft, NetAircraft

public class Stearman extends com.maddox.il2.objects.air.Scheme1
    implements com.maddox.il2.objects.air.TypeFighter, com.maddox.il2.objects.air.TypeTNBFighter
{

    public Stearman()
    {
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(super.thisWeaponsName.equals("2xVickers"))
        {
            hierMesh().chunkVisible("WGun1_D0", true);
            hierMesh().chunkVisible("WGun2_D0", true);
            hierMesh().chunkVisible("WGun3_D0", false);
            hierMesh().chunkVisible("WGun4_D0", false);
            hierMesh().chunkVisible("WGun5_D0", false);
            return;
        }
        if(super.thisWeaponsName.equals("4xVz30"))
        {
            hierMesh().chunkVisible("WGun1_D0", true);
            hierMesh().chunkVisible("WGun2_D0", true);
            hierMesh().chunkVisible("WGun3_D0", true);
            hierMesh().chunkVisible("WGun4_D0", true);
            hierMesh().chunkVisible("Wgun5_D0", false);
            return;
        }
        if(super.thisWeaponsName.equals("3xVickers"))
        {
            hierMesh().chunkVisible("WGun1_D0", true);
            hierMesh().chunkVisible("WGun2_D0", true);
            hierMesh().chunkVisible("WGun3_D0", false);
            hierMesh().chunkVisible("WGun4_D0", false);
            hierMesh().chunkVisible("WGun5_D0", true);
            return;
        } else
        {
            return;
        }
    }

    public void update(float f)
    {
        float f1 = ((com.maddox.il2.fm.FlightModelMain) (super.FM)).EI.engines[0].getControlRadiator();
        com.maddox.il2.objects.air.Aircraft.xyz[0] = 0.0F;
        com.maddox.il2.objects.air.Aircraft.xyz[1] = 0.0F;
        com.maddox.il2.objects.air.Aircraft.xyz[2] = f1 * -0.45F;
        com.maddox.il2.objects.air.Aircraft.ypr[0] = 0.0F;
        com.maddox.il2.objects.air.Aircraft.ypr[1] = 0.0F;
        com.maddox.il2.objects.air.Aircraft.ypr[2] = 0.0F;
        hierMesh().chunkSetLocate("Water_D0", com.maddox.il2.objects.air.Aircraft.xyz, com.maddox.il2.objects.air.Aircraft.ypr);
        super.update(f);
        super.onAircraftLoaded();
        if(super.FM.isPlayers())
            if(!com.maddox.il2.game.Main3D.cur3D().isViewOutside())
                hierMesh().chunkVisible("Goertz_D0", false);
            else
                hierMesh().chunkVisible("Goertz_D0", true);
        if(super.FM.isPlayers() && !com.maddox.il2.game.Main3D.cur3D().isViewOutside())
            hierMesh().chunkVisible("Goertz_D0", false);
    }


    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.Stearman.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Stearman");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Stearman/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        com.maddox.rts.Property.set(class1, "yearService", 1934F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1942F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/S231.fmd:S231_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitU2ST.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.742F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 0
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05"
        });
    }
}