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

public class KB_29P extends KB_29
    implements TypeTankerBoom, TypeDockable
{

    public KB_29P()
    {
        bBoomExtended = true;
        bInRefueling = false;
        maxSendRefuel = 32.026F;
        drones = new Actor[1];
        waitRefuelTimer = 0L;
        bEmpty = false;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 7; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(thisWeaponsName.startsWith("none"))
            bEmpty = true;
    }

    public boolean isBoomExtended()
    {
        return bBoomExtended;
    }

    public void update(float f)
    {
        boomRefuel(f);
        super.update(f);
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

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        default:
            break;

        case 0:
            if(f < -23F)
            {
                f = -23F;
                flag = false;
            }
            if(f > 23F)
            {
                f = 23F;
                flag = false;
            }
            if(f1 < -25F)
            {
                f1 = -25F;
                flag = false;
            }
            if(f1 > 15F)
            {
                f1 = 15F;
                flag = false;
            }
            break;

        case 1:
            if(f1 < 0.0F)
            {
                f1 = 0.0F;
                flag = false;
            }
            if(f1 > 73F)
            {
                f1 = 73F;
                flag = false;
            }
            break;

        case 2:
            if(f < -38F)
            {
                f = -38F;
                flag = false;
            }
            if(f > 38F)
            {
                f = 38F;
                flag = false;
            }
            if(f1 < -41F)
            {
                f1 = -41F;
                flag = false;
            }
            if(f1 > 43F)
            {
                f1 = 43F;
                flag = false;
            }
            break;

        case 3:
            if(f < -85F)
            {
                f = -85F;
                flag = false;
            }
            if(f > 22F)
            {
                f = 22F;
                flag = false;
            }
            if(f1 < -40F)
            {
                f1 = -40F;
                flag = false;
            }
            if(f1 > 32F)
            {
                f1 = 32F;
                flag = false;
            }
            break;

        case 4:
            if(f < -34F)
            {
                f = -34F;
                flag = false;
            }
            if(f > 30F)
            {
                f = 30F;
                flag = false;
            }
            if(f1 < -30F)
            {
                f1 = -30F;
                flag = false;
            }
            if(f1 > 32F)
            {
                f1 = 32F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doKillPilot(int i)
    {
        switch(i)
        {
        case 2:
            FM.turret[0].bIsOperable = false;
            break;

        case 3:
            FM.turret[1].bIsOperable = false;
            break;

        case 4:
            FM.turret[2].bIsOperable = false;
            break;

        case 5:
            FM.turret[3].bIsOperable = false;
            FM.turret[4].bIsOperable = false;
            break;
        }
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
            if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AS.isMaster() && ((SndAircraft) (aircraft)).FM.getSpeedKMH() > 10F && FM.getSpeedKMH() > 10F && isBoomExtended())
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
                    HookNamed hooknamed1 = new HookNamed((ActorMesh)actor, "_Receptacle");
                    hooknamed1.computePos(actor, loc1, loc3);
                    if(loc2.getPoint().distance(loc3.getPoint()) >= 20D)
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
        {
            if(actor != drones[i])
                continue;
            Aircraft aircraft = (Aircraft)actor;
            if(!((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AS.isMaster())
                continue;
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
            HookNamed hooknamed1 = new HookNamed((ActorMesh)actor, "_Receptacle");
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

    private void boomRefuel(float f)
    {
        float f1 = Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (FM)).Loc)).z, FM.getSpeed()) * 3.6F;
        if(bEmpty || FM.getAltitude() < 1000F || FM.CT.getGear() > 0.0F || f1 > 760F || f1 < 325F || (double)FM.M.fuel < (double)FM.M.maxFuel * 0.05000000000000001D)
        {
            hierMesh().chunkSetAngles("GearC9_D0", 0.0F, 0.0F, -61.5F);
            hierMesh().chunkSetAngles("GearC11_D0", 0.0F, -30F, 0.0F);
            hierMesh().chunkSetAngles("GearC12_D0", 0.0F, 30F, 0.0F);
            resetYPRmodifier();
            Aircraft.xyz[1] = -3.5F;
            hierMesh().chunkSetLocate("GearC10_D0", Aircraft.xyz, Aircraft.ypr);
            bBoomExtended = false;
            waitRefuelTimer = Time.current() + 8000L;
            typeDockableAttemptDetach();
        } else
        {
            hierMesh().chunkSetAngles("GearC9_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("GearC11_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("GearC12_D0", 0.0F, 0.0F, 0.0F);
            resetYPRmodifier();
            hierMesh().chunkSetLocate("GearC10_D0", Aircraft.xyz, Aircraft.ypr);
            bBoomExtended = true;
            waitRefuelTimer = Time.current() + 8000L;
        }
    }

    public final float requestRefuel(Aircraft aircraft, float f, float f1)
    {
        if(bBoomExtended && ((FlightModelMain) (FM)).AS.isMaster())
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

    protected boolean cutFM(int i, int j, Actor actor)
    {
        if(FM.AS.isMaster())
            switch(i)
            {
            case 33:
            case 34:
            case 35:
                typeDockableRequestDetach(drones[0], 0, true);
                break;
            }
        return super.cutFM(i, j, actor);
    }

    public static boolean bChangedPit = false;
    private int dockport_;
    private boolean bBoomExtended;
    private boolean bInRefueling;
    private Actor drones[];
    private float maxSendRefuel;
    private long waitRefuelTimer;
    private boolean bEmpty;

    static 
    {
        Class class1 = KB_29P.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "KB-29");
        Property.set(class1, "meshName", "3DO/Plane/KB-29/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1948.5F);
        Property.set(class1, "yearExpired", 1960.9F);
        Property.set(class1, "FlightModel", "FlightModels/B-29.fmd");
        Aircraft.weaponTriggersRegister(class1, new int[] {
            14, 14, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02"
        });
    }
}
