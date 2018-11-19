 package com.maddox.il2.objects.weapons;
 
 import com.maddox.JGP.Point3d;
 import com.maddox.il2.engine.Actor;
 import com.maddox.il2.engine.Orient;
 import com.maddox.rts.NetChannel;
 import com.maddox.rts.Property;
 
 public class MissileAIM9D extends Missile
 {
   public MissileAIM9D() {}
   
   static class SPAWN extends Missile.SPAWN
   {
     SPAWN() {}
     
     public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
     {
       new MissileAIM9D(actor, netchannel, i, point3d, orient, f);
     }
   }
   
   public MissileAIM9D(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
     MissileInit(actor, netchannel, i, point3d, orient, f);
   }
   
 
 
   static
   {
     Class class1 = MissileAIM9D.class;
     Property.set(class1, "mesh", "3do/arms/AIM9D/mono.sim");
     Property.set(class1, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
     Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
     Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
     Property.set(class1, "emitColor", new com.maddox.JGP.Color3f(1.0F, 1.0F, 0.5F));
     Property.set(class1, "emitLen", 50.0F);
     Property.set(class1, "emitMax", 0.4F);
     Property.set(class1, "sound", "weapon.rocket_132");
     Property.set(class1, "timeLife", 60.0F);
     Property.set(class1, "timeFire", 2.8F);
     Property.set(class1, "force", 20000.0F);
     Property.set(class1, "forceT1", 0.5F);
     Property.set(class1, "forceP1", 0.0F);
     Property.set(class1, "forceT2", 0.2F);
     Property.set(class1, "forceP2", 50.0F);
     Property.set(class1, "dragCoefficient", 0.3F);
     Property.set(class1, "powerType", 0);
     Property.set(class1, "power", 1.02F);
     Property.set(class1, "radius", 12.93F);
     Property.set(class1, "kalibr", 0.2F);
     Property.set(class1, "massa", 88.5F);
     Property.set(class1, "massaEnd", 60.0F);
     Property.set(class1, "stepMode", 0);
     Property.set(class1, "launchType", 1);
     Property.set(class1, "detectorType", 1);
     Property.set(class1, "sunRayAngle", 12.5F);
     Property.set(class1, "multiTrackingCapable", 1);
     Property.set(class1, "canTrackSubs", 0);
     Property.set(class1, "minPkForAI", 25.0F);
     Property.set(class1, "timeForNextLaunchAI", 10000L);
     Property.set(class1, "engineDelayTime", 0L);
     Property.set(class1, "attackDecisionByAI", 1);
     Property.set(class1, "targetType", 1);
     Property.set(class1, "shotFreq", 0.01F);
     
 
 
 
     Property.set(class1, "groundTrackFactor", 16.0F);
     Property.set(class1, "flareLockTime", 1000L);
     Property.set(class1, "trackDelay", 1000L);
     Property.set(class1, "failureRate", 30.0F);
     Property.set(class1, "maxLockGForce", 6.0F);
     Property.set(class1, "maxFOVfrom", 2.5F);
     Property.set(class1, "maxFOVfrom_real", 30.0F);
     Property.set(class1, "maxFOVto", 60.0F);
     Property.set(class1, "PkMaxFOVfrom", 35.0F);
     Property.set(class1, "PkMaxFOVto", 70.0F);
     Property.set(class1, "PkDistMin", 400.0F);
     Property.set(class1, "PkDistOpt", 4500.0F);
     Property.set(class1, "PkDistMax", 8000.0F);
     Property.set(class1, "leadPercent", 0.0F);
     Property.set(class1, "maxGForce", 15.0F);
     Property.set(class1, "stepsForFullTurn", 8);
     Property.set(class1, "fxLock", "weapon.AIM9.lock");
     Property.set(class1, "fxNoLock", "weapon.AIM9.nolock");
     Property.set(class1, "smplLock", "AIM9_lock.wav");
     Property.set(class1, "smplNoLock", "AIM9_no_lock.wav");
     Property.set(class1, "friendlyName", "AIM-9D");
     com.maddox.rts.Spawn.add(class1, new SPAWN());
   }
 }

/* Location:           C:\Users\Koty\OLD_PC\Downloads\Projects\RESOLVED\JetEra
 * Qualified Name:     com.maddox.il2.objects.weapons.MissileAIM9D
 * Java Class Version: 1.1 (45.3)
 * JD-Core Version:    0.7.0.1
 */