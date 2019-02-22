package com.maddox.il2.objects.weapons;

import java.util.List;

import com.maddox.rts.Property;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.JGP.Color3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Hook;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Selector;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeLaserSpotter;
import com.maddox.il2.objects.air.TypeX4Carrier;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;

import com.maddox.il2.objects.air.Mi24V;

public class Missile9M114 extends Missile {

//    public boolean interpolateStep()
//    {
//        float f = Time.tickLenFs();
//        float f1 = (float)getSpeed((Vector3d)null);
//        f1 += (320F - f1) * 0.1F * f;
//        pos.getAbs(p, or);
//        v.set(1.0D, 0.0D, 0.0D);
//        or.transform(v);
//        v.scale(f1);
//        setSpeed(v);
//        p.x += v.x * (double)f;
//        p.y += v.y * (double)f;
//        p.z += v.z * (double)f;
//        if(isNet() && isNetMirror())
//        {
//            pos.setAbs(p, or);
//            return false;
//        }
//        if(Actor.isValid(getOwner()))
//        {
//            if((getOwner() != World.getPlayerAircraft() || !((RealFlightModel)fm).isRealMode()) && (fm instanceof Pilot))
//            {
//                Pilot pilot = (Pilot)fm;
//                if(pilot.target_ground != null)
//                    victim = pilot.target_ground;
//                else
//                if(pilot.target != null)
//                    victim = pilot.target.actor;
//                else
//                    victim = null;
//            } else
//            {
////                if((victim = Main3D.cur3D().getViewPadlockEnemy()) == null)
////                    victim = Selector.look(true, true, Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()], -1, -1, World.getPlayerAircraft(), false);
////            	victim = Mi24V.lock;
//            	checklaser();
//                if(!laseron)
//                    if(victim.isDestroyed())
//                    {
//                        victim = null;
//                    } else
//                    {
//                        victim.pos.getAbs(pT);
//                        pT.sub(p);
//                        or.transformInv(pT);
//                        if(pT.y > pT.x / 4D || pT.y < -pT.x / 4D || pT.z > pT.x / 4D || pT.z < -pT.x / 4D)
//                            victim = null;
//                    }
//                if(Time.current() > tStart + 500L && (getOwner() instanceof TypeX4Carrier) && ((TypeX4Carrier)fm.actor).typeX4CgetdeltaAzimuth() != ((TypeX4Carrier)fm.actor).typeX4CgetdeltaTangage())
//                {
//                    ((TypeX4Carrier)fm.actor).typeX4CResetControls();
//                    doExplosionAir();
//                    postDestroy();
//                    collide(false);
//                    drawing(false);
//                }
//            }
//            if(laseron)
//            {
//                pT.sub(p);
//                or.transformInv(pT);
//                if(pT.x > 0.0D)
//                {
//                    if(pT.y > 0.10000000000000001D)
//                        deltaAzimuth = -1F;
//                    if(pT.y < -0.10000000000000001D)
//                        deltaAzimuth = 1F;
//                    if(pT.z < -0.10000000000000001D)
//                        deltaTangage = -1F;
//                    if(pT.z > 0.10000000000000001D)
//                        deltaTangage = 1F;
//                    or.increment(10F * f * deltaAzimuth, 10F * f * deltaTangage, 5520F * f);
//                    deltaAzimuth = deltaTangage = 0.0F;
//                }
//            } else
//            {
//                or.increment(0.0F, 0.0F, 0.0F);
//            }
//        }
//        pos.setAbs(p, or);
//        return false;
//    }
	
	/*     */   public boolean interpolateStep()
	/*     */   {
	/*  41 */     float f1 = Time.tickLenFs();
	/*  42 */     float f2 = (float)getSpeed(null);
	/*  43 */     f2 += (320.0F - f2) * 70.1F * f1;
	/*  44 */     this.pos.getAbs(p, or);
	/*  45 */     v.set(1.0D, 0.0D, 0.0D);
	/*  46 */     or.transform(v);
	/*  47 */     v.scale(f2);
	/*  48 */     setSpeed(v);
	/*  49 */     p.x += v.x * f1;
	/*  50 */     p.y += v.y * f1;
	/*  51 */     p.z += v.z * f1;
	/*  52 */     if ((isNet()) && (isNetMirror()))
	/*     */     {
	/*  54 */       this.pos.setAbs(p, or);
	/*  55 */       return false;
	/*     */     }
	/*  57 */     if (Actor.isValid(getOwner()))
	/*     */     {
	/*  59 */       if (((getOwner() != World.getPlayerAircraft()) || (!((RealFlightModel)this.fm).isRealMode())) && ((this.fm instanceof Pilot)))
	/*     */       {
	/*  61 */         Pilot localPilot = (Pilot)this.fm;
	/*  62 */         if (localPilot.target_ground != null) {
	/*  63 */           this.victim = localPilot.target_ground;
	/*     */         }
	/*  65 */         else if (localPilot.target != null) {
	/*  66 */           this.victim = localPilot.target.actor;
	/*     */         } else {
	/*  68 */           this.victim = null;
	/*     */         }
	/*     */       } else {
	/*  71 */         checklaser();
	/*  72 */         if (!this.laseron)
	/*  73 */           if (this.victim.isDestroyed())
	/*     */           {
	/*  75 */             this.victim = null;
	/*     */           }
	/*     */           else {
	/*  78 */             this.victim.pos.getAbs(pT);
	/*  79 */             pT.sub(p);
	/*  80 */             or.transformInv(pT);
	/*  81 */             if ((pT.y > pT.x / 4.0D) || (pT.y < -pT.x / 4.0D) || (pT.z > pT.x / 4.0D) || (pT.z < -pT.x / 4.0D))
	/*  82 */               this.victim = null;
	/*     */           }
	/*  84 */         if ((Time.current() > this.tStart + 50000L) && ((getOwner() instanceof TypeX4Carrier)) && (((TypeX4Carrier)this.fm.actor).typeX4CgetdeltaAzimuth() != ((TypeX4Carrier)this.fm.actor).typeX4CgetdeltaTangage()))
	/*     */         {
	/*  86 */           ((TypeX4Carrier)this.fm.actor).typeX4CResetControls();
	/*  87 */           doExplosionAir();
	/*  88 */           postDestroy();
	/*  89 */           collide(false);
	/*  90 */           drawing(false);
	/*     */         }
	/*     */       }
	/*  93 */       if (this.laseron)
	/*     */       {
	/*  95 */         pT.sub(p);
	/*  96 */         or.transformInv(pT);
	/*  97 */         float f3 = 0.1F;
	/*  98 */         if (p.distance(pT) > 0.0D)
	/*     */         {
	/* 100 */           if (pT.y > 0.1D)
	/* 101 */             this.deltaAzimuth = (-f3);
	/* 102 */           if (pT.y < -0.1D)
	/* 103 */             this.deltaAzimuth = f3;
	/* 104 */           if (pT.z < -0.1D)
	/* 105 */             this.deltaTangage = (-f3);
	/* 106 */           if (pT.z > 0.1D)
	/* 107 */             this.deltaTangage = f3;
	/* 108 */           or.increment(20.0F * f3 * this.deltaAzimuth, 20.0F * f3 * this.deltaTangage, 10550.0F * f1);
	/* 109 */           this.deltaAzimuth = (this.deltaTangage = 0.0F);
	/*     */         }
	/*     */       }
	/*     */       else {
	/* 113 */         or.increment(0.0F, 0.0F, 10550.0F * f1);
	/*     */       }
	/*     */     }
	/* 116 */     this.pos.setAbs(p, or);
	/* 117 */     return false;
	/*     */   }
    
       private void checklaser()
       {
         this.laseron = false;
         List localList = com.maddox.il2.engine.Engine.targets();
         int i = localList.size();
         for (int j = 0; j < i; j++)
         {
           Actor localActor = (Actor)localList.get(j);
           if (((localActor instanceof TypeLaserSpotter)) && (localActor.pos.getAbsPoint().distance(this.pos.getAbsPoint()) < 20000.0D) && (localActor == World.getPlayerAircraft()))
           {
             Point3d localPoint3d = new Point3d();
             TypeLaserSpotter localTypeLaserSpotter = (TypeLaserSpotter)localActor;
             localPoint3d = TypeLaserSpotter.spot;
             if (this.pos.getAbsPoint().distance(localPoint3d) >= 15000.0D) {}
             pT.set(localPoint3d);
             this.laseron = true;
           }
          }
      }
       
	
  static class SPAWN extends Missile.SPAWN {

    public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
      new MissileAIM9B(actor, netchannel, i, point3d, orient, f);
    }
  }

  public Missile9M114(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
    this.MissileInit(actor, netchannel, i, point3d, orient, f);
    victim = null;
    fm = null;
    tStart = 0L;
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

  public Missile9M114() {
      victim = null;
      fm = null;
      tStart = 0L;
      deltaAzimuth = 0.0F;
      deltaTangage = 0.0F;
      victim = null;
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

  private boolean laseron;
  private FlightModel fm;
  private static Orient or = new Orient();
  private static Point3d p = new Point3d();
  private static Point3d pT = new Point3d();
  private static Vector3d v = new Vector3d();
  private long tStart;
  private float deltaAzimuth;
  private float deltaTangage;
  private Actor victim;

  static {
    Class class1 = com.maddox.il2.objects.weapons.Missile9M114.class;
    Property.set(class1, "mesh", "3DO/Arms/5inchZuni/mono.sim");
    Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
    Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
    Property.set(class1, "smoke", "3DO/Effects/Tracers/GuidedRocket/White.eff");
    Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
    Property.set(class1, "emitLen", 50F);
    Property.set(class1, "emitMax", 1.0F);
    Property.set(class1, "sound", "weapon.rocket_132");
    Property.set(class1, "timeLife", 20F); // Rocket life time in Seconds
    Property.set(class1, "timeFire", 5F); // Rocket Engine Burn time in Seconds
    Property.set(class1, "force", 74530.54F); // Rocket Engine Power (in Newton)
    Property.set(class1, "forceT1", 2F); // Time1, i.e. time until Rocket Engine force output maximum reached (in Seconds), 0 disables this feature
    Property.set(class1, "forceP1", 0.0F); // Power1, i.e. Rocket Engine force output at beginning (in Percent)
    Property.set(class1, "forceT2", 3F); // Time2, i.e. time before Rocket Engine burn time ends (in Seconds), from this time on Rocket Engine power output decreases, 0 disables this feature
    Property.set(class1, "forceP2", 50.0F); // Power2, i.e. Rocket Engine force output at the end of burn time (in Percent)
    Property.set(class1, "dragCoefficient", 0.3F); // Aerodynamic Drag Coefficient
    Property.set(class1, "powerType", 0);
    Property.set(class1, "power", 35.0F); // RL Data: 4.5kg HE warhead, for realism reduced to 1/10th of it's RL weight
    Property.set(class1, "radius", 1.0F);
    Property.set(class1, "kalibr", 1.30F);
    Property.set(class1, "massa", 31.4F);
    Property.set(class1, "massaEnd", 20.0F);
    Property.set(class1, "stepMode", Missile.STEP_MODE_BEAMRIDER); // target tracking mode
    Property.set(class1, "launchType", Missile.LAUNCH_TYPE_QUICK); // launch pattern
    Property.set(class1, "detectorType", 0); // detector type
    Property.set(class1, "sunRayAngle", 0.0F); // max. Angle at which the missile will track Sun Ray, zero disables Sun Ray tracking (only valid for IR detector missiles)
    Property.set(class1, "multiTrackingCapable", 0); // set whether or not this type of missile can fight multiple different targets simultaneously. Usually "1" for IR missiles and "0" for others.
    Property.set(class1, "canTrackSubs", 0); // set whether or not submerged objects can be tracked
    Property.set(class1, "minPkForAI", 25.0F); // min. Kill Probability for AI to launch a missile
    Property.set(class1, "timeForNextLaunchAI", 10000L); // time between two Missile launches for AI
    Property.set(class1, "engineDelayTime", -200L); // Rocket Engine Start Delay Time (in milliseconds), negative Value means that Engine is started before Missile leaves it's rail
    Property.set(class1, "attackDecisionByAI", 1); // let AI decide whether or not to attack. Usually "1" for short range missiles and "0" for all others
    Property.set(class1, "targetType", 272); // Type of valid targets, can be any combination of TARGET_AIR, TARGET_GROUND and TARGET_SHIP
    Property.set(class1, "shotFreq", 5.01F); // Minimum time (in seconds) between two missile launches

    // Factor for missile tracking random ground targets issue.
    // Calculation: Angle to ground (in degrees) divided by:
    //              (altitude in meters divided by 1000)^2
    Property.set(class1, "groundTrackFactor", 1000.0F); // lower value means higher probability of ground target tracking
    Property.set(class1, "flareLockTime", 50000L); // time (in milliseconds) for missile locking on to different target, i.e. flare (sun, ground clutter etc.)
    Property.set(class1, "trackDelay", 1000L); // time (in milliseconds) for missile tracking target after launch (i.e. the time for the missile to fly straight first to be clear of the launching A/C)
    Property.set(class1, "failureRate", 30.0F); // Failure rate in percent
    Property.set(class1, "maxLockGForce", 2.0F); // max. G-Force for lockon
    Property.set(class1, "maxFOVfrom", 30.0F); // max Angle offset from launch A/C POV
    Property.set(class1, "maxFOVto", 360.0F); // max Angle offset from target aft POV
    Property.set(class1, "PkMaxFOVfrom", 35.0F); // max Angle offset from launch A/C POV for Pk calculation
    Property.set(class1, "PkMaxFOVto", Float.MAX_VALUE); // max Angle offset from target aft POV for Pk calculation
    Property.set(class1, "PkDistMin", 300.0F); // min Distance for Pk calculation
    Property.set(class1, "PkDistOpt", 1500.0F); // optimum Distance for Pk calculation
    Property.set(class1, "PkDistMax", 5000.0F); // max Distance for Pk calculation
    Property.set(class1, "leadPercent", 0.0F); // Track calculation lead value
    Property.set(class1, "maxGForce", 12.0F); // max turning rate G-Force
    Property.set(class1, "stepsForFullTurn", 17); // No. of ticks (1 tick = 30ms) for full control surface turn, higher value means slower reaction and smoother flight, lower value means higher agility
    Property.set(class1, "fxLock", (String)null); // prs file for Lock Tone
    Property.set(class1, "fxNoLock", (String)null); // prs file for No Lock Tone
    Property.set(class1, "smplLock", (String)null); // wav file for Lock Tone
    Property.set(class1, "smplNoLock", (String)null); // wav file for No Lock Tone
    Property.set(class1, "friendlyName", "9M114"); // Display Name of this missile
    Spawn.add(class1, new SPAWN());
  }
}
