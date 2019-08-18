// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11.05.2019 14:51:43
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   UC_78.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            UC_BobCat, PaintSchemeBMPar00, Aircraft, NetAircraft

public class UC_78 extends com.maddox.il2.objects.air.UC_BobCat
{

    public UC_78()
    {
        kl = 1.0F;
        kr = 1.0F;
        kc = 1.0F;
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC2_D0", 0.0F, f, 0.0F);
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
        if(super.thisWeaponsName.startsWith("General"))
        {
            hierMesh().chunkVisible("General", true);
            return;
        }
        if(super.thisWeaponsName.startsWith("JAG"))
        {
            hierMesh().chunkVisible("Pass1", true);
            hierMesh().chunkVisible("Pass2", true);
            return;
        }
        if(super.thisWeaponsName.equals("1x100Kg_Crate"))
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
        if(s.startsWith("xgearl") && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.05F)
        {
            debuggunnery("Hydro System: Disabled..");
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 0);
            gearDamageFX(s);
        }
        if(s.startsWith("xgearr") && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.05F)
        {
            debuggunnery("Hydro System: Disabled..");
            ((com.maddox.il2.fm.FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 0);
            gearDamageFX(s);
        }
    }

    private void gearDamageFX(java.lang.String s)
    {
        if(s.startsWith("xgearl"))
        {
            if(super.FM.isPlayers())
                com.maddox.il2.game.HUD.log("Left Gear:  Hydraulic system Failed");
            com.maddox.il2.game.HUD.log("-");
            kl = com.maddox.il2.ai.World.Rnd().nextFloat();
            kr = com.maddox.il2.ai.World.Rnd().nextFloat() * kl;
        } else
        if(s.startsWith("xgearr"))
        {
            if(super.FM.isPlayers())
                com.maddox.il2.game.HUD.log("Right Gear:  Hydraulic system Failed");
            com.maddox.il2.game.HUD.log("-");
            kr = com.maddox.il2.ai.World.Rnd().nextFloat();
            kl = com.maddox.il2.ai.World.Rnd().nextFloat() * kr;
        }
        kc = 0.0F;
        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).CT.GearControl = 0.4F;
        ((com.maddox.il2.fm.FlightModelMain) (super.FM)).Gears.setHydroOperable(false);
    }

    private static float kl = 1.0F;
    private static float kr = 1.0F;
    private static float kc = 1.0F;

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.UC_78.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Bobcat");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/T-50/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        com.maddox.rts.Property.set(class1, "yearService", 1937F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1965F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/UC78.fmd:UC78_FM");
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