 package com.maddox.il2.objects.weapons;
 
 import com.maddox.JGP.Point3d;
 import com.maddox.il2.engine.Actor;
 import com.maddox.il2.engine.Orient;
 import com.maddox.rts.NetChannel;
 import com.maddox.rts.Property;
 
 public class MissileK13A extends Missile
 {
   public MissileK13A() {}
   
   static class SPAWN extends Missile.SPAWN
   {
     SPAWN() {}
     
     public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
     {
       new MissileK13A(actor, netchannel, i, point3d, orient, f);
     }
   }
   
   public MissileK13A(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
     MissileInit(actor, netchannel, i, point3d, orient, f);
   }
   
 
 
   static
   {
     Class class1 = MissileK13A.class;
     Property.set(class1, "mesh", "3do/arms/K-13A/mono.sim");
     Property.set(class1, "sprite", "3do/effects/RocketSidewinder/RocketSidewinderSpriteBlack.eff");
     Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
     Property.set(class1, "smoke", "3do/Effects/RocketSidewinder/RocketSidewinderSmoke.eff");
     Property.set(class1, "emitColor", new com.maddox.JGP.Color3f(1.0F, 1.0F, 0.5F));
     Property.set(class1, "emitLen", 50.0F);
     Property.set(class1, "emitMax", 1F);
     Property.set(class1, "sound", "weapon.rocket_132");
     Property.set(class1, "timeLife", 26.0F);
     Property.set(class1, "timeFire", 2.4F);
     Property.set(class1, "force", 12000.0F);
     Property.set(class1, "forceT1", 0.7F);
     Property.set(class1, "forceP1", 0.0F);
     Property.set(class1, "forceT2", 0.2F);
     Property.set(class1, "forceP2", 50.0F);
     Property.set(class1, "dragCoefficient", 0.3F);
     Property.set(class1, "powerType", 0);
     Property.set(class1, "power", 0.42F);
     Property.set(class1, "radius", 8.96F);
     Property.set(class1, "kalibr", 0.2F);
     Property.set(class1, "massa", 72.0F);
     Property.set(class1, "massaEnd", 47.0F);
     Property.set(class1, "stepMode", 0);
     Property.set(class1, "launchType", 1);
     Property.set(class1, "detectorType", 1);
     Property.set(class1, "sunRayAngle", 22.0F);
     Property.set(class1, "multiTrackingCapable", 1);
     Property.set(class1, "canTrackSubs", 0);
     Property.set(class1, "minPkForAI", 25.0F);
     Property.set(class1, "timeForNextLaunchAI", 10000L);
     Property.set(class1, "engineDelayTime", 0L);
     Property.set(class1, "attackDecisionByAI", 1);
     Property.set(class1, "targetType", 1);
     Property.set(class1, "shotFreq", 0.01F);
     
 
 
 
     Property.set(class1, "groundTrackFactor", 9.0F);
     Property.set(class1, "flareLockTime", 1000L);
     Property.set(class1, "trackDelay", 1000L);
     Property.set(class1, "failureRate", 50.0F);
     Property.set(class1, "maxLockGForce", 2.0F);
     Property.set(class1, "maxFOVfrom", 5.0F);//25
     Property.set(class1, "maxFOVfrom_real", 25.0F);
     Property.set(class1, "maxFOVto", 60.0F);
     Property.set(class1, "PkMaxFOVfrom", 25.0F);//25
     Property.set(class1, "PkMaxFOVto", 70.0F);
     Property.set(class1, "PkDistMin", 400.0F);
     Property.set(class1, "PkDistOpt", 1500.0F);
     Property.set(class1, "PkDistMax", 5000.0F);
     Property.set(class1, "maxSpeed", 2012.2145F);
     Property.set(class1, "leadPercent", 50.0F);
     Property.set(class1, "maxGForce", 11.0F);
     Property.set(class1, "stepsForFullTurn", 10);
     Property.set(class1, "fxLock", "weapon.K5.lock");
     Property.set(class1, "fxNoLock", "weapon.K5.nolock");
     Property.set(class1, "smplLock", "K5_lock.wav");
     Property.set(class1, "smplNoLock", "K5_no_lock.wav");
     Property.set(class1, "friendlyName", "R-3S");
     com.maddox.rts.Spawn.add(class1, new SPAWN());
   }
 }

/* Location:           C:\Users\Koty\OLD_PC\Downloads\Projects\RESOLVED\JetEra
 * Qualified Name:     com.maddox.il2.objects.weapons.MissileK13A
 * Java Class Version: 1.1 (45.3)
 * JD-Core Version:    0.7.0.1
 */