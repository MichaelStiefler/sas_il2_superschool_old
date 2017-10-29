package com.maddox.il2.objects.vehicles.stationary;

import java.io.IOException;
import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public abstract class CandCGeneric extends ActorHMesh
    implements ActorAlign
{
    class Move extends Interpolate
    {

        public boolean tick()
        {
            if(engineSFX != null)
                if(engineSTimer >= 0)
                {
                    if(--engineSTimer <= 0)
                    {
                        engineSTimer = (int)CandCGeneric.SecsToTicks(CandCGeneric.Rnd(Timer1, Timer2));
                        if(!danger())
                            engineSTimer = -engineSTimer;
                    }
                } else
                if(++engineSTimer >= 0)
                {
                    engineSTimer = -(int)CandCGeneric.SecsToTicks(CandCGeneric.Rnd(Timer1, Timer2));
                    if(danger())
                        engineSTimer = -engineSTimer;
                }
            return true;
        }

        Move()
        {
        }
    }

    class Master extends ActorNet
    {

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            return true;
        }

        public Master(Actor actor)
        {
            super(actor);
        }
    }

    class Mirror extends ActorNet
    {

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            return true;
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i)
        {
            super(actor, netchannel, i);
            out = new NetMsgFiltered();
        }
    }

    public static class SPAWN
        implements ActorSpawn
    {

        public Actor actorSpawn(ActorSpawnArg actorspawnarg)
        {
            CandCGeneric candcgeneric = null;
            try
            {
                CandCGeneric.constr_arg2 = actorspawnarg;
                candcgeneric = (CandCGeneric)cls.newInstance();
                CandCGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
            {
                CandCGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create CandCGeneric object [class:" + cls.getName() + "]");
                return null;
            }
            return candcgeneric;
        }

        public Class cls;

        public SPAWN(Class class1)
        {
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", CandCGeneric.mesh_name);
            cls = class1;
            com.maddox.rts.Spawn.add(cls, this);
        }
    }


    public void setTimer(int i)
    {
        Random random = new Random();
        Timer1 = (float)((double)random.nextInt(i) * 0.10000000000000001D);
        Timer2 = (float)((double)random.nextInt(i) * 0.10000000000000001D);
    }

    public void resetTimer(float f)
    {
        Timer1 = f;
        Timer2 = f;
    }

    public static double Rnd(double d, double d1)
    {
        return World.Rnd().nextDouble(d, d1);
    }

    public static float Rnd(float f, float f1)
    {
        return World.Rnd().nextFloat(f, f1);
    }

    public boolean RndB(float f)
    {
        return World.Rnd().nextFloat(0.0F, 1.0F) < f;
    }

    public static long SecsToTicks(float f)
    {
        long l = (long)(0.5D + (double)(f / Time.tickLenFs()));
        return l < 1L ? 1L : l;
    }

    public void destroy()
    {
        if(isDestroyed())
        {
            return;
        } else
        {
            engineSFX = null;
            engineSTimer = 0xfa0a1f01;
            super.destroy();
            return;
        }
    }

    public Object getSwitchListener(Message message)
    {
        return this;
    }

    public boolean isStaticPos()
    {
        return true;
    }

    public CandCGeneric()
    {
        this(constr_arg2);
    }

    public CandCGeneric(ActorSpawnArg actorspawnarg)
    {
        mesh_name = "3do/primitive/siren/mono.sim";
        engineSFX = null;
        engineSTimer = 0x98967f;
        outCommand = new NetMsgFiltered();
        actorspawnarg.setStationary(this);
        myArmy = getArmy();
        setArmy(getArmy());
        collide(false);
        drawing(true);
        createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        heightAboveLandSurface = 0.0F;
        Align();
        startMove();
        Timer1 = 30F;
        Timer2 = 50F;
        delay = 3F;
    }

    public void startMove()
    {
        if(!interpEnd("move"))
        {
            interpPut(new Move(), "move", Time.current(), null);
            engineSFX = newSound("objects.siren", false);
            engineSTimer = -(int)SecsToTicks(Rnd(delay, delay));
        }
    }

    public void Align()
    {
        pos.getAbs(p);
        p.z = Engine.land().HQ(p.x, p.y) + (double)heightAboveLandSurface;
        o.setYPR(pos.getAbsOrient().getYaw(), 0.0F, 0.0F);
        Engine.land().N(p.x, p.y, n);
        o.orient(n);
        pos.setAbs(p, o);
    }

    public void align()
    {
        Align();
    }

    public boolean danger()
    {
        return false;
    }

    public void createNetObject(NetChannel netchannel, int i)
    {
        if(netchannel == null)
            net = new Master(this);
        else
            net = new Mirror(this, netchannel, i);
    }

    public void netFirstUpdate(NetChannel netchannel)
        throws IOException
    {
    }

    public static String mesh_name;
    public float heightAboveLandSurface;
    public SoundFX engineSFX;
    public int engineSTimer;
    public int myArmy;
    public static ActorSpawnArg constr_arg2 = null;
    public static Point3d p = new Point3d();
    public static Orient o = new Orient();
    public static Vector3f n = new Vector3f();
    public static Vector3d tmpv = new Vector3d();
    public NetMsgFiltered outCommand;
    public float Timer1;
    public float Timer2;
    public float delay;

}
