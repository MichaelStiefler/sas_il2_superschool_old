package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Tuple3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Eff3DActor;
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

public class A1H_Tanker extends AD_Tanker
    implements TypeDockable, TypeTankerDrogue
{

    public A1H_Tanker()
    {
        bDrogueExtended = true;
        bInRefueling = false;
        maxSendRefuel = 9.588F;
        drones = new Actor[1];
        waitRefuelTimer = 0L;
        ratdeg = 0.0F;
        bEmpty = false;
    }

    public boolean isDrogueExtended()
    {
        return bDrogueExtended;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
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
        if(FM.CT.getArrestor() > 0.2F)
            calculateArrestor();
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
        if(this.FM.AS.isMaster())
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
            if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AS.isMaster() && ((SndAircraft) (aircraft)).FM.getSpeedKMH() > 10F && this.FM.getSpeedKMH() > 10F && isDrogueExtended())
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
                    if(loc2.getPoint().distance(loc3.getPoint()) >= 7.5D)
                        continue;
                    if(this.FM.AS.isMaster())
                        typeDockableRequestAttach(actor, i, true);
                    else
                        this.FM.AS.netToMaster(32, i, 0, actor);
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
            if(this.FM.AS.isMaster())
                typeDockableRequestDetach(actor, i, true);
            else
                this.FM.AS.netToMaster(33, i, 1, actor);
        }

    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag)
    {
        if(i >= 0 && i <= 1)
            if(flag)
            {
                if(this.FM.AS.isMaster())
                {
                    this.FM.AS.netToMirrors(34, i, 1, actor);
                    typeDockableDoAttachToDrone(actor, i);
                } else
                {
                    this.FM.AS.netToMaster(34, i, 1, actor);
                }
            } else
            if(this.FM.AS.isMaster())
            {
                if(!Actor.isValid(drones[i]))
                {
                    this.FM.AS.netToMirrors(34, i, 1, actor);
                    typeDockableDoAttachToDrone(actor, i);
                }
            } else
            {
                this.FM.AS.netToMaster(34, i, 0, actor);
            }
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag)
    {
        if(flag)
            if(this.FM.AS.isMaster())
            {
                this.FM.AS.netToMirrors(35, i, 1, actor);
                typeDockableDoDetachFromDrone(i);
            } else
            {
                this.FM.AS.netToMaster(35, i, 1, actor);
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
        if(this.FM.AS.isMaster())
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
        hierMesh().chunkSetAngles("D704_rat", 0.0F, 0.0F, ratdeg);
        if(FM.getSpeedKMH() > 300F)
        {
            hierMesh().chunkVisible("D704_rat_rot", true);
            hierMesh().chunkVisible("D704_rat", false);
        } else
        {
            hierMesh().chunkVisible("D704_rat_rot", false);
            hierMesh().chunkVisible("D704_rat", true);
        }
    }

    private void drogueRefuel(float f)
    {
        float f1 = Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (FM)).Loc)).z, FM.getSpeed()) * 3.6F;
        if(bEmpty || FM.getAltitude() < 1000F || FM.CT.getGear() > 0.0F || FM.CT.getArrestor() > 0.0F || f1 > 580F || f1 < 325F || (double)FM.M.fuel < (double)FM.M.maxFuel * 0.20000000000000001D)
        {
            hierMesh().chunkVisible("D704_FuelLine1", false);
            hierMesh().chunkVisible("D704_Drogue1", false);
            hierMesh().chunkVisible("D704_Drogue1_Fold", true);
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
            hierMesh().chunkVisible("D704_FuelLine1", true);
            hierMesh().chunkVisible("D704_Drogue1", true);
            hierMesh().chunkVisible("D704_Drogue1_Fold", false);
            if(!bDrogueExtended && !bInRefueling)
                hierMesh().materialReplace("CYellowOff", "CYellowOn");
            bDrogueExtended = true;
            waitRefuelTimer = Time.current() + 8000L;
        }
        if(bDrogueExtended && ((FlightModelMain) (FM)).AS.isMaster())
        {
            for(int i = 0; i < drones.length; i++)
            {
                if(Actor.isValid(drones[i]))
                {
                    if(!bInRefueling)
                    {
                        hierMesh().materialReplace("CGreenOff", "CGreenOn");
                        hierMesh().materialReplace("CYellowOff", "CYellowOff");
                        bInRefueling = true;
                    }
                    continue;
                }
                if(bInRefueling)
                {
                    hierMesh().materialReplace("CGreenOff", "CGreenOff");
                    hierMesh().materialReplace("CYellowOff", "CYellowOn");
                    bInRefueling = false;
                }
            }

        }
    }

    public final float requestRefuel(Aircraft aircraft, float f, float f1)
    {
        if(bDrogueExtended && ((FlightModelMain) (FM)).AS.isMaster())
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

    private void calculateArrestor()
    {
        if(FM.Gears.arrestorVAngle != 0.0F)
        {
            float f = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
            arrestor = 0.8F * arrestor + 0.2F * f;
            moveArrestorHook(arrestor);
        } else
        {
            float f1 = (-33F * FM.Gears.arrestorVSink) / 57F;
            if(f1 < 0.0F && this.FM.getSpeedKMH() > 60F)
                Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if(f1 > 0.0F && FM.CT.getArrestor() < 0.95F)
                f1 = 0.0F;
            if(f1 > 0.2F)
                f1 = 0.2F;
            if(f1 > 0.0F)
                arrestor = 0.7F * arrestor + 0.3F * (arrestor + f1);
            else
                arrestor = 0.3F * arrestor + 0.7F * (arrestor + f1);
            if(arrestor < 0.0F)
                arrestor = 0.0F;
            else
            if(arrestor > 1.0F)
                arrestor = 1.0F;
            moveArrestorHook(arrestor);
        }
    }

    private boolean bDrogueExtended;
    private boolean bInRefueling;
    private Actor drones[];
    private float maxSendRefuel;
    private long waitRefuelTimer;
    private float ratdeg;
    private boolean bEmpty;

    static 
    {
        Class class1 = A1H_Tanker.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-1H");
        Property.set(class1, "meshName", "3DO/Plane/A1H_Tanker(multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1968F);
        Property.set(class1, "FlightModel", "FlightModels/A1H.fmd");
        Property.set(class1, "LOSElevation", 1.0585F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            1, 1, 1, 1, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev17", "_ExternalDev18"
        });
    }
}
