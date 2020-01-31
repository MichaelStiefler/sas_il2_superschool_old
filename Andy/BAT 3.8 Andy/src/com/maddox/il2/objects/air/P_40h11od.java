// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 08.11.2019 16:58:15
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   P_40h11od.java

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.air:
//            P_40, PaintSchemeFMPar05, TypeStormovik, Aircraft, 
//            NetAircraft

public class P_40h11od extends com.maddox.il2.objects.air.P_40
    implements com.maddox.il2.objects.air.TypeStormovik
{

    public P_40h11od()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if((getBulletEmitterByHookName("_ExternalDev01") instanceof com.maddox.il2.objects.weapons.GunEmpty) && (getBulletEmitterByHookName("_ExternalBomb01") instanceof com.maddox.il2.objects.weapons.GunEmpty) && (getBulletEmitterByHookName("_ExternalBomb04") instanceof com.maddox.il2.objects.weapons.GunEmpty))
            hierMesh().chunkVisible("Pilon_D0", false);
        else
            hierMesh().chunkVisible("Pilon_D0", true);
        if((getBulletEmitterByHookName("_ExternalBomb02") instanceof com.maddox.il2.objects.weapons.GunEmpty) && (getBulletEmitterByHookName("_ExternalBomb06") instanceof com.maddox.il2.objects.weapons.GunEmpty))
        {
            hierMesh().chunkVisible("Pilon2_D0", false);
            hierMesh().chunkVisible("Pilon3_D0", false);
        } else
        {
            hierMesh().chunkVisible("Pilon2_D0", true);
            hierMesh().chunkVisible("Pilon3_D0", true);
        }
        if(thisWeaponsName.startsWith("light"))
        {
            FM.M.massEmpty = 3159F;
            hierMesh().chunkVisible("WingGunL_D0", false);
            hierMesh().chunkVisible("WingGunR_D0", false);
            return;
        } else
        {
            return;
        }
    }

    public void update(float f1)
    {
        super.update(f1);
        f = com.maddox.il2.objects.air.Aircraft.cvt(FM.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 5F, -17F);
        hierMesh().chunkSetAngles("Water2_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("Water3_D0", 0.0F, f, 0.0F);
        f = java.lang.Math.min(f, 0.0F);
        hierMesh().chunkSetAngles("Water1_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("Water4_D0", 0.0F, f, 0.0F);
        if(FM.EI.engines[0].getControlAfterburner())
        {
            FM.EI.engines[0].setAfterburnerType(11);
            com.maddox.il2.game.HUD.logRightBottom("BOOST / WEP ENABLED!");
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


    private static float f;

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.P_40h11od.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "P-40");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/P-40h11od/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        com.maddox.rts.Property.set(class1, "noseart", 1);
        com.maddox.rts.Property.set(class1, "yearService", 1942F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1945.5F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Spitfire-F24.fmd:SPITF22");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitP_40M.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 1.0692F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 0, 0, 3, 9
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalBomb01"
        });
    }
}