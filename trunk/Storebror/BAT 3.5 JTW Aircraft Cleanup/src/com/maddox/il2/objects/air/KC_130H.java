package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class KC_130H extends C_130X
    implements TypeDockable, TypeTankerDrogue
{

    public KC_130H()
    {
        bFirstTime = true;
        bDrogueExtended = true;
        maxSendRefuel = 10.093F;
        drones = new Actor[2];
        waitRefuelTimer = 0L;
        bEmpty = false;
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        return "KC_";
    }

    public boolean isDrogueExtended()
    {
        return bDrogueExtended;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(thisWeaponsName.startsWith("none"))
            bEmpty = true;
    }

    public void missionStarting()
    {
        super.missionStarting();
        bWingBroken[0] = bWingBroken[1] = false;
        bFirstTime = true;
    }

    public void update(float f)
    {
        drogueRefuel();
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
            if(aircraft.FM.AS.isMaster() && aircraft.FM.getSpeedKMH() > 100F && FM.getSpeedKMH() > 100F && isDrogueExtended())
            {
                for(int i = 0; i < drones.length; i++)
                {
                    if(Actor.isValid(drones[i]))
                        continue;
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    pos.getAbs(loc);
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
                    {
                        typeDockableRequestAttach(actor, i, true);
                        return;
                    } else
                    {
                        FM.AS.netToMaster(32, i, 0, actor);
                        return;
                    }
                }

            }
        }
    }

    public void typeDockableRequestDetach(Actor actor)
    {
        for(int i = 0; i < drones.length; i++)
        {
            if(actor != drones[i])
                continue;
            Aircraft aircraft = (Aircraft)actor;
            if(!aircraft.FM.AS.isMaster())
                continue;
            if(FM.AS.isMaster())
                typeDockableRequestDetach(actor, i, true);
            else
                FM.AS.netToMaster(33, i, 1, actor);
        }

    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag)
    {
        if(bFirstTime)
            drogueRefuel();
        if(i >= 0 && i <= 1 && bDrogueExtended)
            if(flag)
            {
                if(FM.AS.isMaster())
                {
                    FM.AS.netToMirrors(34, i, 1, actor);
                    typeDockableDoAttachToDrone(actor, i);
                } else
                {
                    FM.AS.netToMaster(34, i, 1, actor);
                }
            } else
            if(FM.AS.isMaster())
            {
                if(!Actor.isValid(drones[i]))
                {
                    FM.AS.netToMirrors(34, i, 1, actor);
                    typeDockableDoAttachToDrone(actor, i);
                }
            } else
            {
                FM.AS.netToMaster(34, i, 0, actor);
            }
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag)
    {
        if(flag)
            if(FM.AS.isMaster())
            {
                FM.AS.netToMirrors(35, i, 1, actor);
                typeDockableDoDetachFromDrone(i);
            } else
            {
                FM.AS.netToMaster(35, i, 1, actor);
            }
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i)
    {
        if(!Actor.isValid(drones[i]))
        {
            Loc loc = new Loc();
            Loc loc1 = new Loc();
            pos.getAbs(loc);
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
        {
            if(netmsginput.readByte() != 1)
                continue;
            NetObj netobj = netmsginput.readNetObj();
            if(netobj != null)
                typeDockableDoAttachToDrone((Actor)netobj.superObj(), i);
        }

    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        default:
            break;

        case 19:
            killPilot(this, 4);
            break;

        case 33:
        case 34:
            if(FM.AS.isMaster())
            {
                typeDockableRequestDetach(drones[0], 0, true);
                bWingBroken[0] = true;
            }
            break;

        case 36:
        case 37:
            if(FM.AS.isMaster())
            {
                typeDockableRequestDetach(drones[1], 0, true);
                bWingBroken[1] = true;
            }
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 3; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

        for(int j = 0; j < 2; j++)
            if(bWingBroken[j])
            {
                hierMesh().chunkVisible("Rfp_Drogue" + (j + 1) + "_Fold", false);
                hierMesh().chunkVisible("Rfp_iGreen" + (j + 1), false);
                hierMesh().chunkVisible("Rfp_iRed" + (j + 1), false);
                hierMesh().chunkVisible("Rfp_iYellow" + (j + 1), false);
                hierMesh().chunkVisible("Rfp_FuelLine" + (j + 1), false);
                hierMesh().chunkVisible("Rfp_Drogue" + (j + 1), false);
            }

    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    private void drogueRefuel()
    {
        float f = Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (FM)).Loc)).z, FM.getSpeed()) * 3.6F;
        if(bEmpty || FM.getAltitude() < 1000F && FM.getAltitude() != 0.0F && !bFirstTime || FM.CT.getGear() > 0.0F || f > 580F || f < 250F && f != 0.0F && !bFirstTime || FM.M.fuel < FM.M.maxFuel * 0.12F)
        {
            hierMesh().chunkVisible("Rfp_Drogue1", false);
            hierMesh().chunkVisible("Rfp_Drogue2", false);
            hierMesh().chunkVisible("Rfp_FuelLine1", false);
            hierMesh().chunkVisible("Rfp_FuelLine2", false);
            if(!bWingBroken[0])
                hierMesh().chunkVisible("Rfp_Drogue1_Fold", true);
            if(!bWingBroken[1])
                hierMesh().chunkVisible("Rfp_Drogue2_Fold", true);
            if(bDrogueExtended)
            {
                hierMesh().materialReplace("CYellowOff1", "CYellowOff1");
                hierMesh().materialReplace("CYellowOff2", "CYellowOff2");
            }
            bDrogueExtended = false;
            waitRefuelTimer = Time.current() + 8000L;
            typeDockableAttemptDetach();
            boolean flag = false;
            for(int j = 0; j < 2; j++)
                if(bInRefueling[j])
                {
                    bInRefueling[j] = false;
                    flag = true;
                }

            if(flag)
                hierMesh().materialReplace("CGreenOff1", "CGreenOff1");
            hierMesh().materialReplace("CGreenOff2", "CGreenOff2");
        } else
        {
            if(!bWingBroken[0])
            {
                hierMesh().chunkVisible("Rfp_Drogue1", true);
                hierMesh().chunkVisible("Rfp_FuelLine1", true);
            }
            if(!bWingBroken[1])
            {
                hierMesh().chunkVisible("Rfp_Drogue2", true);
                hierMesh().chunkVisible("Rfp_FuelLine2", true);
            }
            hierMesh().chunkVisible("Rfp_Drogue1_Fold", false);
            hierMesh().chunkVisible("Rfp_Drogue2_Fold", false);
            if(!bDrogueExtended && !bInRefueling[0])
            {
                hierMesh().materialReplace("CYellowOff1", "CYellowOn1");
                hierMesh().materialReplace("CYellowOff2", "CYellowOn2");
            }
            bDrogueExtended = true;
            waitRefuelTimer = Time.current() + 8000L;
        }
        if(bDrogueExtended && FM.AS.isMaster())
        {
            for(int i = 0; i < drones.length; i++)
            {
                if(Actor.isValid(drones[i]))
                {
                    if(!bInRefueling[i])
                    {
                        hierMesh().materialReplace("CGreenOff" + String.valueOf(i + 1), "CGreenOn" + String.valueOf(i + 1));
                        hierMesh().materialReplace("CYellowOff" + String.valueOf(i + 1), "CYellowOff" + String.valueOf(i + 1));
                        bInRefueling[i] = true;
                    }
                    continue;
                }
                if(bInRefueling[i])
                {
                    hierMesh().materialReplace("CGreenOff" + String.valueOf(i + 1), "CGreenOff" + String.valueOf(i + 1));
                    hierMesh().materialReplace("CYellowOff" + String.valueOf(i + 1), "CYellowOn" + String.valueOf(i + 1));
                    bInRefueling[i] = false;
                }
            }

        }
        if(bFirstTime && (FM.getAltitude() != 0.0F || f != 0.0F))
            bFirstTime = false;
    }

    public final float requestRefuel(Aircraft aircraft, float f, float f1)
    {
        if(bDrogueExtended && FM.AS.isMaster())
        {
            for(int i = 0; i < drones.length; i++)
            {
                if(!Actor.isValid(drones[i]) || drones[i] != aircraft)
                    continue;
                if(f > maxSendRefuel)
                    f = maxSendRefuel;
                if(FM.M.requestFuel(f * f1))
                    return f * f1;
            }

        }
        return 0.0F;
    }

    public static boolean bChangedPit = false;
    private boolean bFirstTime;
    private boolean bDrogueExtended;
    private boolean bInRefueling[] = {
        false, false
    };
    private Actor drones[];
    private float maxSendRefuel;
    private long waitRefuelTimer;
    private boolean bEmpty;
    private boolean bWingBroken[] = {
        false, false
    };

    static 
    {
        Class class1 = KC_130H.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "KC-130H");
        Property.set(class1, "meshName", "3DO/Plane/C-130/hierKC.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 2080F);
        Property.set(class1, "FlightModel", "FlightModels/LockheedC-130.fmd:C130_FM");
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            14, 3, 3, 3, 2, 2, 2, 2, 2, 2, 
            3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN11", "_MGUN11", "_BombSpawn01", "_BombSpawn02", "_Rock01", "_Rock02", "_Rock03", "_Rock04", "_Rock05", "_Rock06", 
            "_InternalBomb01"
        });
    }
}
