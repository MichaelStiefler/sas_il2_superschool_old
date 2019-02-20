package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Tuple3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class A_6E_tanker extends A_6
    implements TypeDockable, TypeTankerDrogue
{

    public A_6E_tanker()
    {
        bDrogueExtended = true;
        bInRefueling = false;
        maxSendRefuel = 11.101F;
        drones = new Actor[1];
        waitRefuelTimer = 0L;
        ratdeg = 0.0F;
        bEmpty = false;
    }

    public boolean isDrogueExtended()
    {
        return bDrogueExtended;
    }

    private void checkChangeWeaponColors()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++);
            }

    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.turret[0].bIsAIControlled = false;
        ((FlightModelMain) (FM)).M.maxFuel += 880F;
        ((FlightModelMain) (FM)).M.fuel += 880F;
        ((FlightModelMain) (FM)).M.massEmpty += 370F;
        ((FlightModelMain) (FM)).M.mass += 370F;
        ((FlightModelMain) (FM)).M.maxWeight += 1250F;
        if(thisWeaponsName.startsWith("none"))
            bEmpty = true;
    }

    public void update(float f)
    {
        drogueRefuel(f);
        if(FM.getSpeedKMH() > 185F)
            RATrot();
        super.update(f);
    }

    public boolean typeDockableIsDocked()
    {
        return true;
    }

    public void typeDockableAttemptAttach()
    {
    }

    public void typeDockableAttemptDetach()
    {
        if(FM.AS.isMaster())
        {
            for(int i = 0; i < drones.length; i++)
                if(Actor.isValid(drones[i]))
                    typeDockableRequestDetach(drones[i], i, true);

        }
    }

    public void typeDockableRequestAttach(Actor actor)
    {
        if(actor instanceof Aircraft)
        {
            Aircraft aircraft = (Aircraft)actor;
            if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AS.isMaster() && ((SndAircraft) (aircraft)).FM.getSpeedKMH() > 10F && FM.getSpeedKMH() > 10F && isDrogueExtended())
            {
                for(int i = 0; i < drones.length; i++)
                {
                    if(Actor.isValid(drones[i]))
                        continue;
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    this.pos.getAbs(loc);
                    actor.pos.getAbs(loc1);
                    Loc loc2 = new Loc();
                    HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
                    hooknamed.computePos(this, loc, loc2);
                    Loc loc3 = new Loc();
                    HookNamed hooknamed1 = new HookNamed((ActorMesh)actor, "_Probe");
                    hooknamed1.computePos(actor, loc1, loc3);
                    if(loc2.getPoint().distance(loc3.getPoint()) >= 8D)
                        continue;
                    if(FM.AS.isMaster())
                        typeDockableRequestAttach(actor, i, true);
                    else
                        ((FlightModelMain) (FM)).AS.netToMaster(32, i, 0, actor);
                    break;
                }

            }
        }
    }

    public void typeDockableRequestDetach(Actor actor)
    {
        for(int i = 0; i < drones.length; i++)
            if(actor == drones[i])
            {
                Aircraft aircraft = (Aircraft)actor;
                if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AS.isMaster())
                    if(((FlightModelMain) (FM)).AS.isMaster())
                        typeDockableRequestDetach(actor, i, true);
                    else
                        ((FlightModelMain) (FM)).AS.netToMaster(33, i, 1, actor);
            }

    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag)
    {
        if(i >= 0 && i <= 1)
            if(flag)
            {
                if(((FlightModelMain) (FM)).AS.isMaster())
                {
                    ((FlightModelMain) (FM)).AS.netToMirrors(34, i, 1, actor);
                    typeDockableDoAttachToDrone(actor, i);
                } else
                {
                    ((FlightModelMain) (FM)).AS.netToMaster(34, i, 1, actor);
                }
            } else
            if(((FlightModelMain) (FM)).AS.isMaster())
            {
                if(!Actor.isValid(drones[i]))
                {
                    ((FlightModelMain) (FM)).AS.netToMirrors(34, i, 1, actor);
                    typeDockableDoAttachToDrone(actor, i);
                }
            } else
            {
                ((FlightModelMain) (FM)).AS.netToMaster(34, i, 0, actor);
            }
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag)
    {
        if(flag)
            if(((FlightModelMain) (FM)).AS.isMaster())
            {
                ((FlightModelMain) (FM)).AS.netToMirrors(35, i, 1, actor);
                typeDockableDoDetachFromDrone(i);
            } else
            {
                ((FlightModelMain) (FM)).AS.netToMaster(35, i, 1, actor);
            }
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i)
    {
        if(!Actor.isValid(drones[i]))
        {
            Loc loc = new Loc();
            Loc loc1 = new Loc();
            this.pos.getAbs(loc);
            actor.pos.getAbs(loc1);
            Loc loc2 = new Loc();
            HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
            hooknamed.computePos(this, loc, loc2);
            Loc loc3 = new Loc();
            HookNamed hooknamed1 = new HookNamed((ActorMesh)actor, "_Probe");
            hooknamed1.computePos(actor, loc1, loc3);
            Loc loc4 = new Loc();
            Loc loc5 = new Loc();
            loc4 = loc1;
            loc4.sub(loc3);
            loc5 = loc2;
            loc5.sub(loc);
            loc4.add(loc5);
            loc4.add(loc);
            actor.pos.setAbs(loc4);
            actor.pos.setBase(this, null, true);
            actor.pos.resetAsBase();
            drones[i] = actor;
            ((TypeDockable)drones[i]).typeDockableDoAttachToQueen(this, i);
        }
    }

    public void typeDockableDoDetachFromDrone(int i)
    {
        if(Actor.isValid(drones[i]))
        {
            drones[i].pos.setBase(null, null, true);
            ((TypeDockable)drones[i]).typeDockableDoDetachFromQueen(i);
            drones[i] = null;
        }
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i)
    {
    }

    public void typeDockableDoDetachFromQueen(int i)
    {
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        for(int i = 0; i < drones.length; i++)
            if(Actor.isValid(drones[i]))
            {
                netmsgguaranted.writeByte(1);
                ActorNet actornet = drones[i].net;
                if(actornet.countNoMirrors() == 0)
                    netmsgguaranted.writeNetObj(actornet);
                else
                    netmsgguaranted.writeNetObj(null);
            } else
            {
                netmsgguaranted.writeByte(0);
            }

    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        for(int i = 0; i < drones.length; i++)
            if(netmsginput.readByte() == 1)
            {
                NetObj netobj = netmsginput.readNetObj();
                if(netobj != null)
                    typeDockableDoAttachToDrone((Actor)netobj.superObj(), i);
            }

    }

    public void missionStarting()
    {
        super.missionStarting();
        checkChangeWeaponColors();
    }

    private void RATrot()
    {
        if(FM.getSpeedKMH() < 250F)
            ratdeg -= 10F;
        else
        if(FM.getSpeedKMH() < 400F)
            ratdeg -= 20F;
        else
        if(FM.getSpeedKMH() < 550F)
            ratdeg -= 25F;
        else
            ratdeg -= 31F;
        if(ratdeg < 720F)
            ratdeg += 1440F;
        hierMesh().chunkSetAngles("AA42R_rat", 0.0F, 0.0F, ratdeg);
        if(FM.getSpeedKMH() > 300F)
        {
            hierMesh().chunkVisible("AA42R_rat_rot", true);
            hierMesh().chunkVisible("AA42R_rat", false);
        } else
        {
            hierMesh().chunkVisible("AA42R_rat_rot", false);
            hierMesh().chunkVisible("AA42R_rat", true);
        }
    }

    private void drogueRefuel(float f)
    {
        float f1 = Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (FM)).Loc)).z, FM.getSpeed()) * 3.6F;
        if(bEmpty || FM.getAltitude() < 1000F || FM.CT.getGear() > 0.0F || FM.CT.getArrestor() > 0.0F || f1 > 580F || f1 < 325F || FM.M.fuel < FM.M.maxFuel * 0.2F)
        {
            hierMesh().chunkVisible("AA42R_FuelLine1", false);
            hierMesh().chunkVisible("AA42R_Drogue1", false);
            hierMesh().chunkVisible("AA42R_Drogue1_Fold", true);
            if(bDrogueExtended)
                hierMesh().materialReplace("CYellowOff", "CYellowOff");
            bDrogueExtended = false;
            waitRefuelTimer = Time.current() + 8000L;
            typeDockableAttemptDetach();
            if(bInRefueling)
            {
                hierMesh().materialReplace("CGreenOff", "CGreenOff");
                bInRefueling = false;
            }
        } else
        {
            hierMesh().chunkVisible("AA42R_FuelLine1", true);
            hierMesh().chunkVisible("AA42R_Drogue1", true);
            hierMesh().chunkVisible("AA42R_Drogue1_Fold", false);
            if(!bDrogueExtended && !bInRefueling)
                hierMesh().materialReplace("CYellowOff", "CYellowOn");
            bDrogueExtended = true;
            waitRefuelTimer = Time.current() + 8000L;
        }
        if(bDrogueExtended && ((FlightModelMain) (FM)).AS.isMaster())
        {
            for(int i = 0; i < drones.length; i++)
                if(Actor.isValid(drones[i]))
                {
                    if(!bInRefueling)
                    {
                        hierMesh().materialReplace("CGreenOff", "CGreenOn");
                        hierMesh().materialReplace("CYellowOff", "CYellowOff");
                        bInRefueling = true;
                    }
                } else
                if(bInRefueling)
                {
                    hierMesh().materialReplace("CGreenOff", "CGreenOff");
                    hierMesh().materialReplace("CYellowOff", "CYellowOn");
                    bInRefueling = false;
                }

        }
    }

    public final float requestRefuel(Aircraft aircraft, float f, float f1)
    {
        if(bDrogueExtended && ((FlightModelMain) (FM)).AS.isMaster())
        {
            for(int i = 0; i < drones.length; i++)
                if(Actor.isValid(drones[i]) && drones[i] == aircraft)
                {
                    if(f > maxSendRefuel)
                        f = maxSendRefuel;
                    if(FM.M.requestFuel(f * f1))
                        return f * f1;
                }

        }
        return 0.0F;
    }

    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private Actor target_;
    private Actor queen_;
    private int dockport_;
    private boolean bDrogueExtended;
    private boolean bInRefueling;
    private Actor drones[];
    private float maxSendRefuel;
    private long waitRefuelTimer;
    private float ratdeg;
    private boolean bEmpty;

    static 
    {
        Class class1 = A_6E_tanker.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-6E");
        Property.set(class1, "meshName", "3DO/Plane/A-6E/A6Etanker.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1977F);
        Property.set(class1, "yearExpired", 1997F);
        Property.set(class1, "FlightModel", "FlightModels/A6E.fmd:A6");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitA_6.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 
            9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 
            9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 7, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_ExternalDev06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", 
            "_ExternalDev05", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb01", "_Bomb06", "_ExternalBomb07", "_Bomb08", "_ExternalBomb09", 
            "_Bomb10", "_ExternalBomb11", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", 
            "_Bomb12", "_Bomb13", "_ExternalBomb14", "_ExternalBomb15", "_Bomb16", "_ExternalBomb17", "_ExternalBomb18", "_Bomb19NOUSE", "_ExternalBomb20", "_Bomb21", 
            "_Bomb22NOUSE", "_ExternalBomb23", "_Bomb24", "_Bomb25", "_ExternalBomb26", "_ExternalBomb27", "_Bomb28", "_Bomb29", "_ExternalBomb30", "_ExternalBomb31", 
            "_Bomb32", "_Bomb33", "_ExternalBomb34", "_ExternalBomb35", "_ExternalBomb36", "_ExternalBomb37", "_ExternalBomb38", "_ExternalBomb39", "_ExternalBomb40", "_ExternalBomb41", 
            "_ExternalBomb42", "_ExternalBomb43", "_ExternalBomb44", "_ExternalBomb45", "_ExternalBomb46", "_ExternalBomb47", "_ExternalBomb48", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", 
            "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", 
            "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_Rock05", "_Rock06", "_Rock07", 
            "_Rock08", "_Rock09", "_Rock10", "_Rock11", "_Rock12", "_Rock13", "_Rock14", "_Rock15", "_Rock16", "_Rock17", 
            "_Rock18", "_Rock19", "_Rock20", "_Rock21", "_Rock22", "_Rock23", "_Rock24", "_Rock25", "_Rock26", "_Rock27", 
            "_Rock28", "_Rock29", "_Rock30", "_ExternalRock31", "_ExternalRock31", "_ExternalRock32", "_ExternalRock32", "_Rock33", "_Rock34", "_ExternalRock35", 
            "_ExternalRock35", "_ExternalRock36", "_ExternalRock36", "_ExternalRock37", "_ExternalRock37", "_ExternalRock38", "_ExternalRock38", "_Flare01", "_Chaff01"
        });
    }
}
