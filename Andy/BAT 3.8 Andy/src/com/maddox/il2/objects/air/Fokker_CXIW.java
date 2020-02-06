// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 06.02.2020 17:54:06
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   Fokker_CXIW.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.air:
//            Biplanexyz, PaintSchemeFMPar00, TypeSailPlane, TypeStormovikArmored, 
//            NetAircraft, Aircraft

public class Fokker_CXIW extends com.maddox.il2.objects.air.Biplanexyz
    implements com.maddox.il2.objects.air.TypeSeaPlane, com.maddox.il2.objects.air.TypeStormovikArmored
{

    public Fokker_CXIW()
    {
        tmpp = new Point3d();
    }

    public void update(float f)
    {
        super.update(f);
        super.onAircraftLoaded();
        if(super.FM.isPlayers())
            if(!com.maddox.il2.game.Main3D.cur3D().isViewOutside())
            {
                hierMesh().chunkVisible("Blister1_D0", false);
                hierMesh().chunkVisible("Blister2_D0", false);
            } else
            {
                hierMesh().chunkVisible("Blister1_D0", true);
                hierMesh().chunkVisible("Blister2_D0", true);
            }
        if(super.FM.isPlayers())
        {
            if(!com.maddox.il2.game.Main3D.cur3D().isViewOutside())
                hierMesh().chunkVisible("Blister1_D1", false);
            hierMesh().chunkVisible("Blister2_D1", false);
            hierMesh().chunkVisible("Blister1_D2", false);
            hierMesh().chunkVisible("Blister2_D3", false);
        }
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 40F * f, 0.0F);
        hierMesh().chunkSetAngles("SlatR_D0", 0.0F, -40F * f, 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("FSteerL_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("FSteerR_D0", 0.0F, -30F * f, 0.0F);
    }

    public void update1(float f)
    {
        super.update(f);
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 2; j++)
                if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.clpGearEff[i][j] != null)
                {
                    tmpp.set(((com.maddox.il2.engine.Actor) (((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.clpGearEff[i][j])).pos.getAbsPoint());
                    tmpp.z = 0.01D;
                    ((com.maddox.il2.engine.Actor) (((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.clpGearEff[i][j])).pos.setAbs(tmpp);
                    ((com.maddox.il2.engine.Actor) (((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.clpGearEff[i][j])).pos.reset();
                }

        }

    }

    private com.maddox.JGP.Point3d tmpp;

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.Fokker_CXIW.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Fokker_CXIW");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/Fokker_CXIW/hierS.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        com.maddox.rts.Property.set(class1, "yearService", 1931F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1943F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Fokker_CXIW.fmd:Fokker_CXIW_FM");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitFokker_CXIW.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.742F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 10
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_MGUN01", "_MGUN02", "_MGUN03"
        });
    }
}