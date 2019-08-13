package com.maddox.il2.objects.weapons;

import java.io.IOException;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeX4Carrier;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgSpawn;
import com.maddox.rts.NetObj;
import com.maddox.rts.NetSpawn;
import com.maddox.rts.NetUpdate;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public class RocketIgo_1_B extends RocketBomb
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
                new RocketIgo_1_B(actor, netmsginput.channel(), i, point3d, orient, f);
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
            RocketIgo_1_B.v.x = netmsginput.readFloat();
            RocketIgo_1_B.v.y = netmsginput.readFloat();
            RocketIgo_1_B.v.z = netmsginput.readFloat();
            setSpeed(RocketIgo_1_B.v);
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
                getSpeed(RocketIgo_1_B.v);
                out.writeFloat((float)RocketIgo_1_B.v.x);
                out.writeFloat((float)RocketIgo_1_B.v.y);
                out.writeFloat((float)RocketIgo_1_B.v.z);
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
        if(tEStart > 0L && Time.current() > tEStart)
        {
            tEStart = -1L;
            setThrust(17500F);
            if(Config.isUSE_RENDER())
            {
                if(smoke != null)
                    Eff3DActor.setIntesity(smoke, 1.0F);
                if(sprite != null)
                    Eff3DActor.setIntesity(sprite, 1.0F);
                if(flame != null)
                    flame.drawing(true);
                light.light.setEmit(2.0F, 100F);
            }
        }
        if(World.Rnd().nextFloat() > 0.8F && fl1 != null)
            Eff3DActor.setIntesity(fl1, World.Rnd().nextFloat(0.1F, 2.5F));
        if(World.Rnd().nextFloat() > 0.8F && fl2 != null)
            Eff3DActor.setIntesity(fl2, World.Rnd().nextFloat(0.1F, 2.5F));
        float f = Time.tickLenFs();
        pos.getAbs(p, or);
        if(first)
            first = false;
        if(Actor.isValid(getOwner()))
        {
            if((getOwner() != World.getPlayerAircraft() || !((RealFlightModel)fm).isRealMode()) && (fm instanceof Maneuver) && target != null)
            {
                pT.set(target.pos.getAbsPoint());
                Point3d point3d = new Point3d();
                point3d.x = pT.x;
                point3d.y = pT.y;
                point3d.z = pT.z + 2.5D;
                point3d.sub(p);
                or.transformInv(point3d);
                Aircraft aircraft = (Aircraft)getOwner();
                boolean flag = aircraft.FM.isCapableOfACM() && !aircraft.FM.isReadyToDie() && !aircraft.FM.isTakenMortalDamage() && !aircraft.FM.AS.bIsAboutToBailout && !aircraft.FM.AS.isPilotDead(1);
                if(point3d.x > -10D && flag)
                {
                    double d = Aircraft.cvt((float)fm.Skill * aircraft.FM.AS.getPilotHealth(1), 0.0F, 3F, 10F, 2.0F);
                    if(point3d.y > d)
                        ((TypeX4Carrier)fm.actor).typeX4CAdjSideMinus();
                    if(point3d.y < -d)
                        ((TypeX4Carrier)fm.actor).typeX4CAdjSidePlus();
                    if(point3d.z < -d)
                        ((TypeX4Carrier)fm.actor).typeX4CAdjAttitudeMinus();
                    if(point3d.z > d)
                        ((TypeX4Carrier)fm.actor).typeX4CAdjAttitudePlus();
                }
            }
            getSpeed(RocketBomb.spd);
            float f1 = (float)RocketBomb.spd.length();
            Vector3d vector3d = new Vector3d(0.0D, 0.0D, 0.0D);
            vector3d.y = -azimuthControlScaleFact * (double)f1 * (double)((TypeX4Carrier)fm.actor).typeX4CgetdeltaAzimuth();
            vector3d.z = tangageControlScaleFact * (double)f1 * (double)((TypeX4Carrier)fm.actor).typeX4CgetdeltaTangage();
            if(vector3d.y != 0.0D || vector3d.z != 0.0D)
                f1 *= 0.9992F;
            pos.getAbs(RocketBomb.Or);
            RocketBomb.Or.transform(vector3d);
            vector3d.z += ((double)f1 * 0.0018D * (double)f * (double)Atmosphere.g() * (double)Minit) / (double)M;
            if(isThrust)
                vector3d.z += (0.032D * (double)f * (double)Atmosphere.g() * (double)Minit) / (double)M;
            RocketBomb.spd.add(vector3d);
            float f2 = (float)RocketBomb.spd.length();
            float f3 = f1 / f2;
            RocketBomb.spd.scale(f3);
            setSpeed(RocketBomb.spd);
            ((TypeX4Carrier)fm.actor).typeX4CResetControls();
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

    public RocketIgo_1_B()
    {
        first = true;
        fm = null;
    }

    public RocketIgo_1_B(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        first = true;
        fm = null;
        net = new Mirror(this, netchannel, i);
        pos.setAbs(point3d, orient);
        pos.reset();
        pos.setBase(actor, null, true);
        doStart(-1F);
        v.set(1.0D, 0.0D, 0.0D);
        orient.transform(v);
        v.scale(f);
        setSpeed(v);
        collide(false);
    }

    public void start(float f)
    {
        Actor actor = pos.base();
        if(Actor.isValid(actor) && (actor instanceof Aircraft))
        {
            if(actor.isNetMirror())
            {
                destroy();
                return;
            }
            net = new Master(this);
        }
        doStart(f);
    }

    private void doStart(float f)
    {
        this.start(-1F);
        fm = ((Aircraft)getOwner()).FM;
        if(fm instanceof Maneuver)
        {
            maneuver = (Maneuver)fm;
            target = maneuver.target_ground;
        }
        if(Config.isUSE_RENDER())
        {
            fl1 = Eff3DActor.New(this, findHook("_NavLightR"), null, 1.0F, "3DO/Effects/Fireworks/FlareBlue1.eff", -1F);
            fl2 = Eff3DActor.New(this, findHook("_NavLightG"), null, 1.0F, "3DO/Effects/Fireworks/FlareBlue1.eff", -1F);
            if(flame != null)
                flame.drawing(false);
            if(fl1 != null)
                Eff3DActor.setIntesity(fl1, 10F);
            com.maddox.il2.engine.Hook hook = null;
            hook = findHook("_SMOKE01");
            smoke01 = Eff3DActor.New(this, hook, null, 1.0F, "Effects/Smokes/SmokeBlack_planeTrail.eff", -1F);
            if(smoke01 != null)
                smoke01.pos.changeHookToRel();
        }
        noGDelay = -1L;
        tEStart = Time.current() + 1000L;
        if(Config.isUSE_RENDER())
        {
            if(smoke != null)
                Eff3DActor.setIntesity(smoke, 0.0F);
            if(sprite != null)
                Eff3DActor.setIntesity(sprite, 0.0F);
            if(flame != null)
                flame.drawing(false);
            light.light.setEmit(0.0F, 0.0F);
        }
        pos.getAbs(p, or);
        or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
        pos.setAbs(p, or);
    }

    public void destroy()
    {
        if(isNet() && isNetMirror())
            doExplosionAir();
        if(Config.isUSE_RENDER())
        {
            Eff3DActor.finish(fl1);
            Eff3DActor.finish(fl2);
            if(smoke01 != null)
                Eff3DActor.finish(smoke01);
        }
        super.destroy();
    }

    protected void doExplosionAir()
    {
        pos.getTime(Time.current(), p);
        MsgExplosion.send(null, null, p, getOwner(), 45F, 2.0F, 1, 550F);
        super.doExplosionAir();
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel)
        throws IOException
    {
        NetMsgSpawn netmsgspawn = super.netReplicate(netchannel);
        netmsgspawn.writeNetObj(getOwner().net);
        Point3d point3d = pos.getAbsPoint();
        netmsgspawn.writeFloat((float)point3d.x);
        netmsgspawn.writeFloat((float)point3d.y);
        netmsgspawn.writeFloat((float)point3d.z);
        Orient orient = pos.getAbsOrient();
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
    private Maneuver maneuver;
    private Actor target;
    private Eff3DActor fl1;
    private Eff3DActor fl2;
    private Eff3DActor smoke01;
    private static Orient or = new Orient();
    private static Point3d p = new Point3d();
    private static Point3d pT = new Point3d();
    private static Vector3d v = new Vector3d();
    private long tEStart;
    private static double azimuthControlScaleFact = 1.0D;
    private static double tangageControlScaleFact = 1.0D;
    private boolean first;

    static 
    {
        Class class1 = RocketIgo_1_B.class;
        Property.set(class1, "mesh", "3do/Arms/Igo-1-B/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Rocket/rocketsmokewhite.eff");
        Property.set(class1, "smokeStart", "3do/effects/rocket/rocketsmokewhitestart.eff");
        Property.set(class1, "smokeTile", "3do/effects/rocket/rocketsmokewhitetile.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 0.5F);
        Property.set(class1, "sound", "weapon.bomb_std");
        Property.set(class1, "radius", 100F);
        Property.set(class1, "timeLife", 1000F);
        Property.set(class1, "timeFire", 12F);
        Property.set(class1, "force", 0.0F);
        Property.set(class1, "power", 300F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 1.1F);
        Property.set(class1, "massa", 680F);
        Property.set(class1, "massaEnd", 550F);
        Spawn.add(class1, new SPAWN());
    }

}
