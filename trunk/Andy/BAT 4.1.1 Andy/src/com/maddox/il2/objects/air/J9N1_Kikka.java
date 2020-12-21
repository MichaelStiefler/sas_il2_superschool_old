// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 04.11.2020 17:26:48
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   J9N1_Kikka.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombIJN_RATO;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
//            Kikka123, PaintSchemeBMPar03, TypeStormovik, TypeBNZFighter, 
//            NetAircraft, Aircraft

public class J9N1_Kikka extends com.maddox.il2.objects.air.Kikka123
    implements com.maddox.il2.objects.air.TypeStormovik, com.maddox.il2.objects.air.TypeBNZFighter
{

    public J9N1_Kikka()
    {
        bHasBoosters = true;
        boosterFireOutTime = -1L;
        oldctl = -1F;
        curctl = -1F;
    }

    public void destroy()
    {
        doCutBoosters();
        super.destroy();
    }

    public void doFireBoosters()
    {
        com.maddox.il2.engine.Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
        com.maddox.il2.engine.Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
    }

    public void doCutBoosters()
    {
        for(int i = 0; i < 2; i++)
            if(booster[i] != null)
            {
                booster[i].start();
                booster[i] = null;
            }

    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        for(int i = 0; i < 2; i++)
            try
            {
                booster[i] = new BombIJN_RATO();
                booster[i].pos.setBase(this, findHook("_BoosterH" + (i + 1)), false);
                booster[i].pos.resetAsBase();
                booster[i].drawing(true);
            }
            catch(java.lang.Exception exception)
            {
                debugprintln("Structure corrupt - can't hang Starthilferakete..");
            }

    }

    protected boolean cutFM(int i, int j, com.maddox.il2.engine.Actor actor)
    {
        switch(i)
        {
        case 33: // '!'
        case 34: // '"'
        case 35: // '#'
        case 36: // '$'
        case 37: // '%'
        case 38: // '&'
            doCutBoosters();
            FM.AS.setGliderBoostOff();
            bHasBoosters = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f)
    {
        super.update(f);
        if(!(FM instanceof com.maddox.il2.ai.air.Pilot))
            return;
        if(bHasBoosters)
        {
            if(FM.getAltitude() > 300F && boosterFireOutTime == -1L && FM.Loc.z != 0.0D && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.05F)
            {
                doCutBoosters();
                FM.AS.setGliderBoostOff();
                bHasBoosters = false;
            }
            if(bHasBoosters && boosterFireOutTime == -1L && FM.Gears.onGround() && FM.EI.getPowerOutput() > 0.8F && FM.EI.engines[0].getStage() == 6 && FM.EI.engines[1].getStage() == 6 && FM.getSpeedKMH() > 20F)
            {
                boosterFireOutTime = com.maddox.rts.Time.current() + 30000L;
                doFireBoosters();
                FM.AS.setGliderBoostOn();
            }
            if(bHasBoosters && boosterFireOutTime > 0L)
            {
                if(com.maddox.rts.Time.current() < boosterFireOutTime)
                    FM.producedAF.x += 20000D;
                if(com.maddox.rts.Time.current() > boosterFireOutTime + 10000L)
                {
                    doCutBoosters();
                    FM.AS.setGliderBoostOff();
                    bHasBoosters = false;
                }
            }
        }
    }


    public static boolean bChangedPit = false;
    private com.maddox.il2.objects.weapons.Bomb booster[] = {
        null, null
    };
    protected boolean bHasBoosters;
    protected long boosterFireOutTime;
    private float oldctl;
    private float curctl;

    static 
    {
        java.lang.Class class1 = com.maddox.il2.objects.air.J9N1_Kikka.class;
        new NetAircraft.SPAWN(class1);
        com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "Kikka");
        com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/J9N1-Kikka(Multi1)/hier.him");
        com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        com.maddox.rts.Property.set(class1, "yearService", 1943.1F);
        com.maddox.rts.Property.set(class1, "yearExpired", 1945.5F);
        com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/Kikka.fmd:kikka_fm");
        com.maddox.rts.Property.set(class1, "cockpitClass", new java.lang.Class[] {
            com.maddox.il2.objects.air.CockpitKikka.class
        });
        com.maddox.rts.Property.set(class1, "LOSElevation", 0.74185F);
        com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 1, 1, 3, 3, 3, 3, 3, 9, 
            9
        });
        com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalDev01", 
            "_ExternalDev02"
        });
    }
}