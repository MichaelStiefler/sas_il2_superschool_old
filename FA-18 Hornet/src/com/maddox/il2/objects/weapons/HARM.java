// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10/18/2012 6:17:01 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   HARM.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.objects.vehicles.artillery.AAA;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeX4Carrier;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.ships.Ship;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;


import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketBomb

public class HARM extends JDAM84
{
    static class SPAWN
        implements NetSpawn
    {

        public void netSpawn(int i, NetMsgInput netmsginput)
        {
            NetObj netobj = netmsginput.readNetObj();
            if(netobj == null)
                return;
            try
            {
                Actor actor = (Actor)netobj.superObj();
                Point3d point3d = new Point3d(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
                Orient orient = new Orient(netmsginput.readFloat(), netmsginput.readFloat(), 0.0F);
                float f = netmsginput.readFloat();
                HARM HARM = new HARM(actor, netmsginput.channel(), i, point3d, orient, f);
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }

        SPAWN()
        {
        }
    }

    class Mirror extends ActorNet
    {

        public void msgNetNewChannel(NetChannel netchannel)
        {
            if(!Actor.isValid(actor()))
                return;
            if(netchannel.isMirrored(this))
                return;
            try
            {
                if(netchannel.userState == 0)
                {
                    NetMsgSpawn netmsgspawn = actor().netReplicate(netchannel);
                    if(netmsgspawn != null)
                    {
                        postTo(netchannel, netmsgspawn);
                        actor().netFirstUpdate(netchannel);
                    }
                }
            }
            catch(Exception exception)
            {
                NetObj.printDebug(exception);
            }
        }

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            if(netmsginput.isGuaranted())
                return false;
            if(isMirrored())
            {
                out.unLockAndSet(netmsginput, 0);
                postReal(Message.currentTime(true), out);
            }
            HARM.v.x = netmsginput.readFloat();
            HARM.v.y = netmsginput.readFloat();
            HARM.v.z = netmsginput.readFloat();
            setSpeed(HARM.v);
            return true;
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i)
        {
            super(actor, netchannel, i);
            out = new NetMsgFiltered();
        }
    }

    class Master extends ActorNet
        implements NetUpdate
    {

        public void msgNetNewChannel(NetChannel netchannel)
        {
            if(!Actor.isValid(actor()))
                return;
            if(netchannel.isMirrored(this))
                return;
            try
            {
                if(netchannel.userState == 0)
                {
                    NetMsgSpawn netmsgspawn = actor().netReplicate(netchannel);
                    if(netmsgspawn != null)
                    {
                        postTo(netchannel, netmsgspawn);
                        actor().netFirstUpdate(netchannel);
                    }
                }
            }
            catch(Exception exception)
            {
                NetObj.printDebug(exception);
            }
        }

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            return false;
        }

        public void netUpdate()
        {
            try
            {
                out.unLockAndClear();
                getSpeed(HARM.v);
                out.writeFloat((float)HARM.v.x);
                out.writeFloat((float)HARM.v.y);
                out.writeFloat((float)HARM.v.z);
                post(Time.current(), out);
            }
            catch(Exception exception)
            {
                NetObj.printDebug(exception);
            }
        }

        NetMsgFiltered out;

        public Master(Actor actor)
        {
            super(actor);
            out = new NetMsgFiltered();
        }
    }


    public boolean interpolateStep()
    {
        float f = Time.tickLenFs();
        super.pos.getAbs(p, or);
        if(first)
            first = false;
        if(Actor.isValid(getOwner()))
        {
            ((TypeX4Carrier)((Interpolate) (fm)).actor).typeX4CResetControls();
            if(target != null)
            {
                pT = target.pos.getAbsPoint();
                Point3d point3d = new Point3d();
                point3d.x = ((Tuple3d) (pT)).x;
                point3d.y = ((Tuple3d) (pT)).y;
                point3d.z = ((Tuple3d) (pT)).z + 2.5D;
                point3d.sub(p);
                or.transformInv(point3d);
                if(((Tuple3d) (point3d)).x > -10D)
                {
                    double d;
                    if(target instanceof AAA)
                        d = Aircraft.cvt(((FlightModelMain) (fm)).Skill, 0.0F, 3F, 4F / targetRCSMax, 1.0F / targetRCSMax);
                    else
                        d = Aircraft.cvt(((FlightModelMain) (fm)).Skill, 0.0F, 3F, 20F, 4F);
                    if(((Tuple3d) (point3d)).y > d)
                        ((TypeX4Carrier)((Interpolate) (fm)).actor).typeX4CAdjSideMinus();
                    if(((Tuple3d) (point3d)).y < -d)
                        ((TypeX4Carrier)((Interpolate) (fm)).actor).typeX4CAdjSidePlus();
                    if(((Tuple3d) (point3d)).z < -d)
                        ((TypeX4Carrier)((Interpolate) (fm)).actor).typeX4CAdjAttitudeMinus();
                    if(((Tuple3d) (point3d)).z > d)
                        ((TypeX4Carrier)((Interpolate) (fm)).actor).typeX4CAdjAttitudePlus();
                }
            }
            getSpeed(RocketBomb.spd);
            float f1 = (float)RocketBomb.spd.length();
            Vector3d vector3d = new Vector3d(0.0D, 0.0D, 0.0D);
            vector3d.y = -azimuthControlScaleFact * (double)f1 * (double)((TypeX4Carrier)((Interpolate) (fm)).actor).typeX4CgetdeltaAzimuth();
            vector3d.z = tangageControlScaleFact * (double)f1 * (double)((TypeX4Carrier)((Interpolate) (fm)).actor).typeX4CgetdeltaTangage();
            if(((Tuple3d) (vector3d)).y != 0.0D || ((Tuple3d) (vector3d)).z != 0.0D)
                f1 *= 0.9992F;
            super.pos.getAbs(RocketBomb.Or);
            RocketBomb.Or.transform(vector3d);
            vector3d.z += (double)f1 * 0.0070000000000000001D * (double)f * (double)Atmosphere.g();
            RocketBomb.spd.add(vector3d);
            float f2 = (float)RocketBomb.spd.length();
            float f3 = f1 / f2;
            RocketBomb.spd.scale(f3);
            setSpeed(RocketBomb.spd);
            ((TypeX4Carrier)((Interpolate) (fm)).actor).typeX4CResetControls();
        }
        if(!Actor.isValid(getOwner()) || !(getOwner() instanceof Aircraft))
        {
            doExplosionAir();
            postDestroy();
            collide(false);
            drawing(false);
            return true;
        } else
        {
            return true;
        }
    }

    public HARM()
    {
        first = true;
        targetRCSMax = 0.0F;
        fm = null;
        tStart = 0L;
        prevd = 1000F;
    }

    public HARM(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        first = true;
        targetRCSMax = 0.0F;
        fm = null;
        tStart = 0L;
        prevd = 1000F;
        super.net = new Mirror(this, netchannel, i);
        super.pos.setAbs(point3d, orient);
        super.pos.reset();
        super.pos.setBase(actor, null, true);
        doStart(-1F);
        v.set(1.0D, 0.0D, 0.0D);
        orient.transform(v);
        v.scale(f);
        setSpeed(v);
        collide(false);
    }

    public void start(float f)
    {
        Actor actor = super.pos.base();
        if(Actor.isValid(actor) && (actor instanceof Aircraft))
        {
            if(actor.isNetMirror())
            {
                destroy();
                return;
            }
            super.net = new Master(this);
        }
        doStart(f);
    }

    private void doStart(float f)
    {
        super.start(-1F);
        tStart = Time.current();
        super.pos.getAbs(p, or);
        or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
        super.pos.setAbs(p, or);
        fm = ((SndAircraft) ((Aircraft)getOwner())).FM;
        List list = Engine.targets();
        int i = list.size();
        float f1 = 0.0F;
        Actor actor = null;
        for(int j = 0; j < i; j++)
        {
            Actor actor1 = (Actor)list.get(j);
            if((actor1 instanceof AAA))
            {
                Point3d point3d = new Point3d();
                Point3d point3d1 = actor1.pos.getAbsPoint();
                point3d.x = ((Tuple3d) (point3d1)).x;
                point3d.y = ((Tuple3d) (point3d1)).y;
                point3d.z = ((Tuple3d) (point3d1)).z;
                super.pos.getAbs(p, or);
                point3d.sub(p);
                or.transformInv(point3d);
                float f2 = antennaPattern(point3d, actor1);
                if(f2 > f1 && (double)f2 > 0.001D)
                {
                    f1 = f2;
                    actor = actor1;
                    targetRCSMax = estimateRCS(actor);
                }
            }
        }

        target = actor;
    }

    private float antennaPattern(Point3d point3d, Actor actor)
    {
        float f = (float)Math.sqrt(((Tuple3d) (point3d)).x * ((Tuple3d) (point3d)).x + ((Tuple3d) (point3d)).y * ((Tuple3d) (point3d)).y + ((Tuple3d) (point3d)).z * ((Tuple3d) (point3d)).z);
        if(f > 32000F)
            return 0.0F;
        float f1 = (float)Math.atan2(((Tuple3d) (point3d)).y, ((Tuple3d) (point3d)).x);
        float f2 = (float)Math.sqrt(((Tuple3d) (point3d)).x * ((Tuple3d) (point3d)).x + ((Tuple3d) (point3d)).y * ((Tuple3d) (point3d)).y);
        float f3 = (float)Math.atan2(((Tuple3d) (point3d)).z, f2);
        f3 += 0.2617992F;
        f /= 1000F;
        double d;
        if(Math.cos(f1) > 0.0D && Math.cos(f3) > 0.0D)
            d = (Math.cos(f1) * Math.cos(f3)) / (double)(f * f);
        else
            d = 0.0D;
        if(d > 0.0D && (actor instanceof AAA))
        {
            float f4 = estimateRCS(actor);
            d *= f4;
        }
        return (float)d;
    }

    private float estimateRCS(Actor actor)
    {
        float f = 0.0F;
        f = actor.collisionR();
        if(f < 5F)
            f = 5F;
        return f / 5F;
    }

    public void destroy()
    {
        if(isNet() && isNetMirror())
            doExplosionAir();
        super.destroy();
    }

    protected void doExplosionAir()
    {
        super.pos.getTime(Time.current(), p);
        MsgExplosion.send(null, null, p, getOwner(), 45F, 2.0F, 1, 550F);
        super.doExplosionAir();
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel)
        throws IOException
    {
        NetMsgSpawn netmsgspawn = super.netReplicate(netchannel);
        netmsgspawn.writeNetObj(getOwner().net);
        Point3d point3d = super.pos.getAbsPoint();
        netmsgspawn.writeFloat((float)((Tuple3d) (point3d)).x);
        netmsgspawn.writeFloat((float)((Tuple3d) (point3d)).y);
        netmsgspawn.writeFloat((float)((Tuple3d) (point3d)).z);
        Orient orient = super.pos.getAbsOrient();
        netmsgspawn.writeFloat(orient.azimut());
        netmsgspawn.writeFloat(orient.tangage());
        float f = (float)getSpeed(null);
        netmsgspawn.writeFloat(f);
        return netmsgspawn;
    }

    protected void mydebug(String s)
    {
    }

    private FlightModel fm;
    private Actor target;
    private static Orient or = new Orient();
    private static Point3d p = new Point3d();
    private static Point3d pT = new Point3d();
    private static Vector3d v = new Vector3d();
    private static Vector3d pOld = new Vector3d();
    private static Vector3d pNew = new Vector3d();
    private static Actor hunted = null;
    private long tStart;
    private float prevd;
    private static double azimuthControlScaleFact = 0.96000000000000002D;
    private static double tangageControlScaleFact = 0.96000000000000002D;
    private boolean first;
    private float targetRCSMax;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.HARM.class;
        Property.set(class1, "mesh", "3DO/Arms/AGM-88/mono.sim");
        Property.set(class1, "sprite", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smoke", "EFFECTS/Smokes/SmokeBlack_missiles.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 2.0F);
        Property.set(class1, "radius", 75F);
        Property.set(class1, "timeLife", 160F);
        Property.set(class1, "timeFire", 133F);
        Property.set(class1, "force", 235712F);
        Property.set(class1, "power", 125F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 1.34F);
        Property.set(class1, "massa", 556F);
        Property.set(class1, "massaEnd", 511F);
        Spawn.add(class1, new SPAWN());
    }

}