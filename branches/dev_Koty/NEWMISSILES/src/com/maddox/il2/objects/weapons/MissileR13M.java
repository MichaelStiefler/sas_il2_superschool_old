// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 19.11.2018 22:42:43
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileR13M.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Missile

public class MissileR13M extends Missile
{
    static class SPAWN extends Missile.SPAWN
    {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
        {
            new MissileR13M(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN()
        {
        }
    }


    public MissileR13M(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    public MissileR13M()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.MissileR13M.class;
        Property.set(class1, "mesh", "3do/arms/R13M/mono.sim");
        Property.set(class1, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
        Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
        Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 0.4F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "timeLife", 60F);
        Property.set(class1, "timeFire", 2.8F);
        Property.set(class1, "force", 20000F);
        Property.set(class1, "forceT1", 0.5F);
        Property.set(class1, "forceP1", 0.0F);
        Property.set(class1, "forceT2", 0.2F);
        Property.set(class1, "forceP2", 50F);
        Property.set(class1, "dragCoefficient", 0.3F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "power", 1.02F);
        Property.set(class1, "radius", 12.93F);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 88.5F);
        Property.set(class1, "massaEnd", 60F);
        Property.set(class1, "stepMode", 0);
        Property.set(class1, "launchType", 1);
        Property.set(class1, "detectorType", 1);
        Property.set(class1, "sunRayAngle", 12.5F);
        Property.set(class1, "multiTrackingCapable", 1);
        Property.set(class1, "canTrackSubs", 0);
        Property.set(class1, "minPkForAI", 25F);
        Property.set(class1, "timeForNextLaunchAI", 10000L);
        Property.set(class1, "engineDelayTime", -200L);
        Property.set(class1, "attackDecisionByAI", 1);
        Property.set(class1, "targetType", 1);
        Property.set(class1, "shotFreq", 0.01F);
        Property.set(class1, "groundTrackFactor", 16F);
        Property.set(class1, "flareLockTime", 1000L);
        Property.set(class1, "trackDelay", 1000L);
        Property.set(class1, "failureRate", 30F);
        Property.set(class1, "maxLockGForce", 4F);
        Property.set(class1, "maxFOVfrom", 2.5F);
        Property.set(class1, "maxFOVfrom_real", 30F);
        Property.set(class1, "maxFOVto", 180F);
        Property.set(class1, "PkMaxFOVfrom", 30F);
        Property.set(class1, "PkMaxFOVto", 70F);
        Property.set(class1, "PkDistMin", 400F);
        Property.set(class1, "PkDistOpt", 4500F);
        Property.set(class1, "PkDistMax", 8000F);
        Property.set(class1, "leadPercent", 100F);
        Property.set(class1, "maxGForce", 15F);
        Property.set(class1, "stepsForFullTurn", 8);
        Property.set(class1, "fxLock", "weapon.K5.lock");
        Property.set(class1, "fxNoLock", "weapon.K5.nolock");
        Property.set(class1, "smplLock", "K5_lock.wav");
        Property.set(class1, "smplNoLock", "K5_no_lock.wav");
        Property.set(class1, "friendlyName", "R-13M");
        Spawn.add(class1, new SPAWN());
    }
}