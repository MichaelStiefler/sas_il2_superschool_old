// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10/25/2012 11:44:27 AM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AGM65D.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Selector;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeX4Carrier;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            AGM

public class AGM65D extends AGM
{

    public boolean interpolateStep()
    {
        float f = Time.tickLenFs();
        float f1 = (float)getSpeed((Vector3d)null);
        f1 += (320F - f1) * 70.1F * f;
        pos.getAbs(p, or);
        v.set(1.0D, 0.0D, 0.0D);
        or.transform(v);
        v.scale(f1);
        setSpeed(v);
        p.x += v.x * (double)f;
        p.y += v.y * (double)f;
        p.z += v.z * (double)f;
        if(isNet() && isNetMirror())
        {
            pos.setAbs(p, or);
            return false;
        }
        if(Actor.isValid(getOwner()))
        {
            if((getOwner() != World.getPlayerAircraft() || !((RealFlightModel)fm).isRealMode()) && (fm instanceof Pilot))
            {
                Pilot pilot = (Pilot)fm;
                if(pilot.target_ground != null)
                    victim = pilot.target_ground;
                else
                if(pilot.target != null)
                    victim = pilot.target.actor;
                else
                    victim = null;
            } else
            {
                if(!(victim instanceof BigshipGeneric) && !(victim instanceof ShipGeneric) && (victim = Main3D.cur3D().getViewPadlockEnemy()) == null)
                    victim = Selector.look(true, true, Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()], -1, -1, World.getPlayerAircraft(), false);
                if(!Actor.isValid(victim) && (victim = NearestTargets.getEnemy(6, -1, p, 5000D, 0)) != null)
                    if(victim.isDestroyed())
                    {
                        victim = null;
                    } else
                    {
                        victim.pos.getAbs(pT);
                        pT.sub(p);
                        or.transformInv(pT);
                        if(pT.y > pT.x / 4D || pT.y < -pT.x / 4D || pT.z > pT.x / 4D || pT.z < -pT.x / 4D)
                            victim = null;
                    }
                if(Time.current() > tStart + 500L && (getOwner() instanceof TypeX4Carrier) && ((TypeX4Carrier)fm.actor).typeX4CgetdeltaAzimuth() != ((TypeX4Carrier)fm.actor).typeX4CgetdeltaTangage())
                {
                    ((TypeX4Carrier)fm.actor).typeX4CResetControls();
                    doExplosionAir();
                    postDestroy();
                    collide(false);
                    drawing(false);
                }
            }
            if(victim != null)
            {
                victim.pos.getAbs(pT);
                pT.sub(p);
                or.transformInv(pT);
                if(pT.x > 0.0D)
                {
                    if(pT.y > 0.10000000000000001D)
                        deltaAzimuth = -1F;
                    if(pT.y < -0.10000000000000001D)
                        deltaAzimuth = 1F;
                    if(pT.z < -0.10000000000000001D)
                        deltaTangage = -1F;
                    if(pT.z > 0.10000000000000001D)
                        deltaTangage = 1F;
                    or.increment(20F * f * deltaAzimuth, 20F * f * deltaTangage, 10550F * f);
                    deltaAzimuth = deltaTangage = 0.0F;
                }
            } else
            {
                or.increment(0.0F, 0.0F, 10550F * f);
            }
        }
        pos.setAbs(p, or);
        return false;
    }

    public AGM65D()
    {
        victim = null;
        fm = null;
        tStart = 0L;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        victim = null;
    }

    public AGM65D(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        victim = null;
        fm = null;
        tStart = 0L;
        net = new AGM.Mirror(this, netchannel, i);
        pos.setAbs(point3d, orient);
        pos.reset();
        pos.setBase(actor, (Hook)null, true);
        doStart(-1F);
        v.set(1.0D, 0.0D, 0.0D);
        orient.transform(v);
        v.scale(f);
        setSpeed(v);
        collide(false);
    }

    public void start(float f, int i)
    {
        Actor actor = pos.base();
        if(Actor.isValid(actor) && (actor instanceof Aircraft))
        {
            if(actor.isNetMirror())
            {
                destroy();
                return;
            }
            net = new AGM.Master(this);
        }
        doStart(f);
    }

    private void doStart(float f)
    {
        super.start(-1F, 0);
        fm = ((Aircraft)getOwner()).FM;
        tStart = Time.current();
        pos.getAbs(p, or);
        or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
        pos.setAbs(p, or);
    }

    private FlightModel fm;
    private static Orient or = new Orient();
    private static Point3d p = new Point3d();
    private static Point3d pT = new Point3d();
    private static Vector3d v = new Vector3d();
    private long tStart;
    private float deltaAzimuth;
    private float deltaTangage;
    private Actor victim;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.AGM65D.class;
        Property.set(class1, "mesh", "3DO/Arms/AGM65D/mono.sim");
        Property.set(class1, "sprite", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smoke", "EFFECTS/Smokes/SmokeBlack_missiles.eff");
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "emitLen", 0.0F);
        Property.set(class1, "emitMax", 0.0F);
        Property.set(class1, "radius", 24.93F);
        Property.set(class1, "timeLife", 160F);
        Property.set(class1, "timeFire", 53F);
        Property.set(class1, "force", 20.0F);
        Property.set(class1, "power", 1.52F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.8F);
        Property.set(class1, "massa", 204F);
        Property.set(class1, "massaEnd", 185F);
        Spawn.add(class1, new AGM.SPAWN());
    }
}