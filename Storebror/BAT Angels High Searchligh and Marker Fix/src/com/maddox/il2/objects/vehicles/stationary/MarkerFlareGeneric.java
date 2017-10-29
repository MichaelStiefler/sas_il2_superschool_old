package com.maddox.il2.objects.vehicles.stationary;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.weapons.RocketFlareBall;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public abstract class MarkerFlareGeneric extends ActorHMesh
    implements ActorAlign, MsgExplosionListener, MsgShotListener
{
    public static class SPAWN
        implements ActorSpawn
    {

        public Actor actorSpawn(ActorSpawnArg actorspawnarg)
        {
           // System.out.println("MarkerFlareGeneric SPAWN actorSpawn");
            MarkerFlareGeneric markerflaregeneric = null;
            try
            {
                MarkerFlareGeneric.constr_arg2 = actorspawnarg;
                markerflaregeneric = (MarkerFlareGeneric)cls.newInstance();
                MarkerFlareGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
            {
                MarkerFlareGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create MarkerFlareGeneric object [class:" + cls.getName() + "]");
                return null;
            }
            return markerflaregeneric;
        }

        public Class cls;

        public SPAWN(Class class1)
        {
           // System.out.println("MarkerFlareGeneric SPAWN");
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", MarkerFlareGeneric.mesh_name);
            this.cls = class1;
            com.maddox.rts.Spawn.add(this.cls, this);
        }
    }

    class Mirror extends ActorNet
    {

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
           // System.out.println("MarkerFlareGeneric Mirror netInput");
            return true;
        }

        NetMsgFiltered out;

        public Mirror(Actor actor1, NetChannel netchannel1, int i)
        {
            super(actor1, netchannel1, i);
           // System.out.println("MarkerFlareGeneric Mirror");
            out = new NetMsgFiltered();
        }
    }

    class Master extends ActorNet
    {

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
           // System.out.println("MarkerFlareGeneric Master netInput");
            return true;
        }

        public Master(Actor actor1)
        {
            super(actor1);
           // System.out.println("MarkerFlareGeneric Master");
        }
    }

    class Move extends Interpolate
    {

        public boolean tick()
        {
//           // System.out.println("MarkerFlareGeneric Move tick()");
//            //FIXME
//            if (1==1) return true;
            if(engineSFX != null)
                if(engineSTimer >= 0)
                {
//                    float f = (float)(Math.random() + 1.0D);
//                    Random random = new Random();
//                    int i = random.nextInt(5);
//                    int i = TrueRandom.nextInt(5);
                    if(--engineSTimer <= 0)
                    {
                        float f = (float)(Math.random() + 1.0D);
                        int i = World.Rnd().nextInt(5);
                        engineSTimer = (int)MarkerFlareGeneric.SecsToTicks(MarkerFlareGeneric.Rnd((float)(10 + i) + f, (float)(10 + i) + f));
                        if(!danger())
                            engineSTimer = -engineSTimer;
                    }
                } else
                if(++engineSTimer >= 0)
                {
                    engineSTimer = -(int)MarkerFlareGeneric.SecsToTicks(MarkerFlareGeneric.Rnd(1.0F, 1.0F));
                    if(danger())
                        engineSTimer = -engineSTimer;
                }
            return true;
        }

        Move()
        {
           // System.out.println("MarkerFlareGeneric Move");
        }
    }


    public static final double Rnd(double d, double d1)
    {
       // System.out.println("MarkerFlareGeneric Rnd(double d, double d1)");
        return World.Rnd().nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1)
    {
       // System.out.println("MarkerFlareGeneric Rnd(float f, float f1)");
        return World.Rnd().nextFloat(f, f1);
    }

//    private boolean RndB(float f)
//    {
//        return World.Rnd().nextFloat(0.0F, 1.0F) < f;
//    }

    private static final long SecsToTicks(float f)
    {
       // System.out.println("MarkerFlareGeneric SecsToTicks(float f)");
        long l = (long)(0.5F + (f / Time.tickLenFs()));
        return l >= 1L ? l : 1L;
    }

    public void destroy()
    {
       // System.out.println("MarkerFlareGeneric destroy()");
        if(isDestroyed())
        {
            return;
        } else
        {
            engineSFX = null;
//            engineSTimer = -1E+008F;
            engineSTimer = 0xfa0a1f01;
            breakSounds();
            super.destroy();
            return;
        }
    }

    public Object getSwitchListener(Message message)
    {
       // System.out.println("MarkerFlareGeneric getSwitchListener(Message message)");
        return this;
    }

    public boolean isStaticPos()
    {
       // System.out.println("MarkerFlareGeneric isStaticPos()");
        return true;
    }

    protected MarkerFlareGeneric()
    {
        this(constr_arg2);
       // System.out.println("MarkerFlareGeneric MarkerFlareGeneric()");
    }

    private MarkerFlareGeneric(ActorSpawnArg actorspawnarg)
    {
        super(mesh_name);
       // System.out.println("MarkerFlareGeneric MarkerFlareGeneric(ActorSpawnArg actorspawnarg)");
        timeOfLightsOn = 0L;
//        counter = 0;
//        hadtarget = false;
        actor = null;
        engineSFX = null;
//        engineSTimer = 9999999F;
        engineSTimer = 0x98967f;
//        outCommand = new NetMsgFiltered();
        actorspawnarg.setStationary(this);
        myArmy = getArmy();
        collide(false);
        drawing(true);
        createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        heightAboveLandSurface = 0.0F;
        Align();
        startMove();
    }

    public void startMove()
    {
       // System.out.println("MarkerFlareGeneric startMove()");
        if(!interpEnd("move"))
        {
            interpPut(new Move(), "move", Time.current(), null);
            engineSFX = newSound("objects.siren", false);
            engineSTimer = -(int)SecsToTicks(Rnd(5F, 5F));
        }
    }

    private void Align()
    {
       // System.out.println("MarkerFlareGeneric Align()");
        pos.getAbs(p);
        p.z = Engine.land().HQ(p.x, p.y) + (double)heightAboveLandSurface;
        o.setYPR(pos.getAbsOrient().getYaw(), 0.0F, 0.0F);
        Engine.land().N(p.x, p.y, n);
        o.orient(n);
        pos.setAbs(p, o);
    }

    public void align()
    {
       // System.out.println("MarkerFlareGeneric align()");
        Align();
    }

    private boolean danger()
    {
//        System.out.println("MarkerFlareGeneric danger()");
//        World.MaxVisualDistance = 20000F; // WHAT THE FUCKING FUCK???
        Aircraft aircraft = World.getPlayerAircraft();
//        double playerAltitude = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        float playerAltitude = (float)(aircraft.pos.getAbsPoint().z - World.land().HQ(aircraft.pos.getAbsPoint().x, aircraft.pos.getAbsPoint().y));
        int i = aircraft.getArmy();
        boolean isForceShow = true;
        if(i == myArmy)
            isForceShow = false;
//        Point3d point3d = new Point3d();
//        Orient orient = new Orient();
        pos.getAbs(point3d, orient);
        //Random random = new Random();
        int j = World.Rnd().nextInt(3);
        float f = 90F - (float)j;
        int k = World.Rnd().nextInt(360) - 180;
        orient.setYPR(orient.getYaw(), orient.getPitch() + f, orient.getRoll() + (float)k);
//        if(aircraft.FM.Gears.onGround()) // FIXME
//            timeOfLightsOn = Time.current() + 120000L; // FIXME
        boolean isShow = false;
        float playerDistance = (float)aircraft.pos.getAbsPoint().distance(point3d);
        //if(playerDistance < 20000D && playerAltitude <= 1500D && playerDistance > 2000D)
        //if(playerDistance < (World.MaxVisualDistance - 100F) && playerAltitude <= 2500F && playerDistance > 500F)
        if(true) // FIXME
        {
            if(isForceShow)
                isShow = true;
            if(!isForceShow)
            {
               // System.out.println("MarkerFlareGeneric danger() War.GetNearestEnemy");
//                Actor enemyInSight = War.GetNearestEnemy(aircraft, -1, 12000F, 16);
                Actor enemyInSight = War.GetNearestEnemy(this, -1, World.MaxVisualDistance - 100F, 0);
                if(enemyInSight == null || enemyInSight.getArmy() == myArmy)
                    isShow = true;
            }
            //if(isShow && Time.current() > timeOfLightsOn + 60000L && !aircraft.FM.Gears.onGround())
            if(Time.current() > timeOfLightsOn + 60000L) // FIXME
            //if(true) // FIXME
            {
//                System.out.println("MarkerFlareGeneric danger() new RocketFlareBall");
                RocketFlareBall rocketflareball = new RocketFlareBall(this, point3d, orient);
                rocketflareball.start(30F, 0);
                timeOfLightsOn = Time.current();
            }
        }
        return true;
    }

    public void createNetObject(NetChannel netchannel1, int i)
    {
       // System.out.println("MarkerFlareGeneric createNetObject(NetChannel netchannel1, int i)");
        if(netchannel1 == null)
            net = new Master(this);
        else
            net = new Mirror(this, netchannel1, i);
    }

    public void netFirstUpdate(NetChannel netchannel1)
        throws IOException
    {
       // System.out.println("MarkerFlareGeneric netFirstUpdate(NetChannel netchannel1)");
    }

    public abstract void msgExplosion(Explosion explosion);

    public abstract void msgShot(Shot shot);

    private static String mesh_name = "3do/primitive/siren/mono.sim";
    private float heightAboveLandSurface;
    protected SoundFX engineSFX;
    protected int engineSTimer;
//    protected float engineSTimer;
    private int myArmy;
    private static ActorSpawnArg constr_arg2 = null;
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
    private static Vector3f n = new Vector3f();
//    private NetMsgFiltered outCommand;
    public NetChannel netchannel;
//    private int counter;
//    private boolean hadtarget;
    private long timeOfLightsOn;
    Actor actor;
    private Point3d point3d = new Point3d();
    private Orient orient = new Orient();

}
