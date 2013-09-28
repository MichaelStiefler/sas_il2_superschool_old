// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 9/19/2013 5:20:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileKh66L.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Missile

public class MissileKh66L extends Missile
{
    static class SPAWN extends Missile.SPAWN
    {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
        {
            new MissileKh66L(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN()
        {
        }
    }


    public MissileKh66L(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    public MissileKh66L()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.MissileKh66L.class;
        Property.set(class1, "mesh", "3do/arms/Kh-66/mono.sim");
        Property.set(class1, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "exhausts", 2);
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 0.4F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "timeLife", 120F);
        Property.set(class1, "timeFire", 12F);
        Property.set(class1, "force", 20000F);
        Property.set(class1, "forceT1", 1.0F);
        Property.set(class1, "forceP1", 0.0F);
        Property.set(class1, "forceT2", 0.2F);
        Property.set(class1, "forceP2", 50F);
        Property.set(class1, "dragCoefficient", 0.5F);
        Property.set(class1, "power", 103F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "radius", 60F);
        Property.set(class1, "kalibr", 0.275F);
        Property.set(class1, "massa", 278F);
        Property.set(class1, "massaEnd", 255F);
        Property.set(class1, "stepMode", 1);
        Property.set(class1, "launchType", 2);
        Property.set(class1, "detectorType", 3);
        Property.set(class1, "sunRayAngle", 0.0F);
        Property.set(class1, "multiTrackingCapable", 0);
        Property.set(class1, "canTrackSubs", 0);
        Property.set(class1, "minPkForAI", 25F);
        Property.set(class1, "timeForNextLaunchAI", 10000L);
        Property.set(class1, "engineDelayTime", 1000L);
        Property.set(class1, "attackDecisionByAI", 0);
        Property.set(class1, "targetType", 256);
        Property.set(class1, "shotFreq", 5F);
        Property.set(class1, "groundTrackFactor", 1000F);
        Property.set(class1, "flareLockTime", 1000L);
        Property.set(class1, "trackDelay", 1000L);
        Property.set(class1, "failureRate", 20F);
        Property.set(class1, "maxLockGForce", 99.9F);
        Property.set(class1, "maxFOVfrom", 25F);
        Property.set(class1, "maxFOVto", 180F);
        Property.set(class1, "PkMaxFOVfrom", 30F);
        Property.set(class1, "PkMaxFOVto", 70F);
        Property.set(class1, "PkDistMin", 1800F);
        Property.set(class1, "PkDistOpt", 5500F);
        Property.set(class1, "PkDistMax", 10000F);
        Property.set(class1, "leadPercent", 0.0F);
        Property.set(class1, "maxGForce", 12F);
        Property.set(class1, "stepsForFullTurn", 5F);
        Property.set(class1, "fxLock", "weapon.K5.lock");
        Property.set(class1, "fxNoLock", "weapon.K5.nolock");
        Property.set(class1, "smplLock", "K5_lock.wav");
        Property.set(class1, "smplNoLock", "K5_no_lock.wav");
        Property.set(class1, "friendlyName", "Kh-66");
        Spawn.add(class1, new SPAWN());
    }
}