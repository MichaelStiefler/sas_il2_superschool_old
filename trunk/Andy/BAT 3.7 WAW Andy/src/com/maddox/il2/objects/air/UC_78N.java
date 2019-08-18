// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11.05.2019 14:52:08
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   UC_78N.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            UC_BobCat, TypeSailPlane, PaintSchemeBMPar00, Aircraft, 
//            NetAircraft

public class UC_78N extends com.maddox.il2.objects.air.UC_BobCat
    implements com.maddox.il2.objects.air.TypeSeaPlane
{

    public UC_78N()
    {
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh1, float f1)
    {
    }

    protected void moveGear(float f1)
    {
    }

    public void moveWheelSink()
    {
    }

    public void moveSteering(float f1)
    {
    }

    public void update(float f)
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

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag)
            drawBombs();
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(super.thisWeaponsName.startsWith("Amiral"))
        {
            hierMesh().chunkVisible("Amiral", true);
            return;
        }
        if(super.thisWeaponsName.equals("1x70Kg_Crate"))
        {
            hierMesh().chunkVisible("Seats2", false);
            hierMesh().chunkVisible("BOX1", true);
            return;
        } else
        {
            return;
        }
    }

    protected void drawBombs()
    {
        int i = 0;
        if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3] != null)
        {
            for(int j = 0; j < ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3].length; j++)
                if(((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][j] != null)
                    i += ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.Weapons[3][j].countBullets();

            if(super.thisWeaponsName.startsWith("1x"))
            {
                for(int k = i + 1; k <= 1; k++)
                    hierMesh().chunkVisible("BOX" + k, false);

            }
        }
    }

    protected void hitBone(java.lang.String s, com.maddox.il2.ai.Shot shot, com.maddox.JGP.Point3d point3d)
    {
        if(s.startsWith("xgearl"))
        {
            if(chunkDamageVisible("GearL2") < 2)
                hitChunk("GearL2", shot);
        } else
        if(s.startsWith("xgearr") && chunkDamageVisible("GearR2") < 2)
            hitChunk("GearR2", shot);
    }

    private static com.maddox.JGP.Point3d tmpp = new Point3d();

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.UC_78N.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Bobcat Hydro");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/T-50N/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        com.maddox.rts.Property.set(class1, "yearService", 1937F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1945F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/UC78N.fmd:UC78_FM");
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.5265F);
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitUC_78.class
        });
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            3
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_BombSpawn01"
        });
    }
}