// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 09.11.2012 0:33:13
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MIG_27.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.Missile;
import com.maddox.il2.objects.weapons.RocketGunFlare;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapInt;
import com.maddox.il2.ai.air.Maneuver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.maddox.il2.objects.weapons.*;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;

import com.maddox.il2.ai.ground.TgtSAM;

// Referenced classes of package com.maddox.il2.objects.air:
//            MIG_23, Chute, PaintSchemeFMParMiG21, TypeGuidedMissileCarrier, 
//            TypeCountermeasure, TypeThreatDetector, Cockpit, NetAircraft, 
//            Aircraft, EjectionSeat

public class Su_25 extends Su_25X
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeStormovikArmored, TypeLaserSpotter
{

    public Su_25()
    {
        guidedMissileUtils = null;
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        lastCommonThreatActive = 0L;
        intervalCommonThreat = 1000L;
        lastRadarLockThreatActive = 0L;
        intervalRadarLockThreat = 1000L;
        lastMissileLaunchThreatActive = 0L;
        intervalMissileLaunchThreat = 1000L;
        dynamoOrient = 0.0F;
        guidedMissileUtils = new GuidedMissileUtils(this);
        intervalMissileLaunchThreat = 1000L;
        guidedMissileUtils = new GuidedMissileUtils(this);
		fxSPO10 = newSound("aircraft.Sirena2", false);
		smplSPO10 = new Sample("sample.Sirena2.wav", 256, 65535);
		SPO10SoundPlaying = false;
		smplSPO10.setInfinite(true);
		counter = 0;
		radarmode = 0;
		APmode1 = false;
		APmode2 = false;
		APmode3 = false;
		backfireList = new ArrayList();
		backfire = false;
		tX4Prev = 0L;
		headPos = new float[3];
		headOr = new float[3];
		pilotHeadT = 0.0F;
		pilotHeadY = 0.0F;
		
		laserOn = false;
		laserLock = false;
    }
    
    public void typeBomberAdjDistancePlus() {
     this.fSightCurForwardAngle += 0.2F;
     if (this.fSightCurForwardAngle > 6.0F) {
       this.fSightCurForwardAngle = 6.0F;
     }
   }
 
   public void typeBomberAdjDistanceMinus()
   {
     this.fSightCurForwardAngle -= 0.2F; 
     if (this.fSightCurForwardAngle < -30.0F) {
       this.fSightCurForwardAngle = -30.0F;
     }
   }
    
    public void typeBomberAdjSideslipPlus() {
    	this.fSightCurSideslip += 0.2F;
    		if (this.fSightCurSideslip > 12.0F) {
    	       this.fSightCurSideslip = 12.0F;
    	     }
    	   }
    	 
    public void typeBomberAdjSideslipMinus() {
    	     this.fSightCurSideslip -= 0.2F;
    	     if (this.fSightCurSideslip < -12.0F) {
    	       this.fSightCurSideslip = -12.0F;
    	     }
    	   }
    
    
    public void laserUpdate() {
    	
    	if(!laserLock){
    	hierMesh().chunkSetAngles("LaserMsh_D0", -fSightCurForwardAngle/* + ((FlightModelMain) (super.FM)).Or.getTangage()*/, -fSightCurSideslip, 0.0F);
    	
//    	this.pos.setUpdateEnable(true);
    	
    	this.pos.getRender(_tmpLoc);
    	LaserHook[1] = new HookNamed(this, "_Laser1");
    	
    	LaserLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
    	this.LaserHook[1].computePos(this, _tmpLoc, LaserLoc1);
    	LaserLoc1.get(LaserP1);
    	LaserLoc1.set(30000.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
    	this.LaserHook[1].computePos(this, _tmpLoc, LaserLoc1);
    	LaserLoc1.get(LaserP2);
    	Engine.land(); 
    	if (Landscape.rayHitHQ(LaserP1, LaserP2, LaserPL)) 
    	{
    		LaserPL.z -= 0.95D;
    		LaserP2.interpolate(LaserP1, LaserPL, 1.0F);
//    		this.Laser[1].setPos(LaserP2);
    		
    		Su_25.spot.set(LaserP2);
    		
    		Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(LaserP2.x, LaserP2.y, LaserP2.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
    		
    	}
    		
//    		float f1 = (float)LaserP1.distance(LaserPL);
//    		float f2 = f1 * 0.5F + 30.0F;
//    		float f3 = 0.5F - (0.5F * f1 / 1000.0F);
//    		this.Laser[1].setEmit(f3, f2);

    		

    		
    	} else if(laserLock) {
    		LaserP3.x = LaserP2.x + -(fSightCurForwardAngle * 6); 
    		LaserP3.y = LaserP2.y + (fSightCurSideslip * 6); 
    		LaserP3.z = LaserP2.z; 
    		
    		Su_25.spot.set(LaserP3);
    		Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(LaserP3.x, LaserP3.y, LaserP3.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
    		
////    		hierMesh().chunkSetAngles("LaserMsh_D0", fSightCurForwardAngle/* + ((FlightModelMain) (super.FM)).Or.getTangage()*/, -fSightCurSideslip, 0.0F);
//        	
//        	this.pos.setUpdateEnable(true);
//        	
//        	this.pos.getRender(_tmpLoc);
//        	LaserHook[1] = new HookNamed(this, "_Laser1");
//        	
//        	LaserLoc1i.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
//        	this.LaserHook[1].computePos(this, _tmpLoc, LaserLoc1i);
//        	LaserLoc1i.get(LaserP1i);
//        	LaserLoc1i.set(30000.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
//        	this.LaserHook[1].computePos(this, _tmpLoc, LaserLoc1i);
//        	LaserLoc1i.get(LaserP2i);
//        	Engine.land(); 
//        	if (Landscape.rayHitHQ(LaserP1i, LaserP2i, LaserPLi)) 
//        	{
//        		LaserPLi.z -= 0.95D;
//        		LaserP2i.interpolate(LaserP1i, LaserPLi, 1.0F);
////        		this.Laser[1].setPos(LaserP2);
//        		
//        		Su_25.spot.set(LaserP2i);  		
//
//        		Actor trgt = War.getNearestEnemy(this, 6000F);	
//        	
//        		
//        		
//        	Ve.set(trgt.pos.getAbsPoint());
//		    Ve.sub(FM.Loc);
//		    FM.Or.transformInv(Ve);
//		    tmpOr.setAT0(Ve);
//		    float f1 = tmpOr.getTangage();
//		    float f2 = tmpOr.getYaw();
//		    if (f2 > 75.0F)
//		       f2 = 75.0F;
//		    if (f2 < -75.0F)
//		       f2 = -75.0F;
//		    if (f1 < -75.0F)
//		       f1 = -75.0F;
//		    if (f1 > 75.0F)
//		       f1 = 75.0F;
//		    pilotHeadT = f1;
//		    pilotHeadY = f2;
////		    hierMesh().chunkSetLocate("LaserMsh_D0", this.headPos, this.headOr);
//    		
//		    tmpOr.setYPR(0.0F, 0.0F, 0.0F);
//		    tmpOr.increment(0.0F, this.pilotHeadY, 0.0F);
//		    tmpOr.increment(this.pilotHeadT, 0.0F, 0.0F);
//		    tmpOr.increment(0.0F, 0.0F, -0.2F * this.pilotHeadT + 0.05F * this.pilotHeadY);
//		    this.headOr[0] = tmpOr.getYaw();
//		    this.headOr[1] = tmpOr.getPitch();
//		    this.headOr[2] = tmpOr.getRoll();
//		    this.headPos[0] = (0.0005F * Math.abs(this.pilotHeadY));
//		    this.headPos[1] = (-1.0E-004F * Math.abs(this.pilotHeadY));
//		    this.headPos[2] = 0.0F;
//		    
//		    hierMesh().chunkSetLocate("LaserMsh_D0", this.headPos, this.headOr);
//		    
////		    hierMesh().chunkSetAngles("LaserMsh_D0", pilotHeadT, pilotHeadY, 0.0F);
//    		
//    		Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(LaserP2i.x, LaserP2i.y, LaserP2i.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
//                }
    	}
    	
    }
    
    private void checkAmmo()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
        {
            if(FM.CT.Weapons[i] == null)
                continue;
            for(int j = 0; j < FM.CT.Weapons[i].length; j++)
            {
                if(!FM.CT.Weapons[i][j].haveBullets())
                    continue;
                if(FM.CT.Weapons[i][j] instanceof RocketGunFlare)
                {
                    backfire = true;
                    backfireList.add(FM.CT.Weapons[i][j]);
                } 
            }        
        }

    }
    
    public void backFire()
    {
        if(backfireList.isEmpty())
        {
            return;
        } else
        {
            ((RocketGunFlare)backfireList.remove(0)).shots(3);
            return;
        }
    }
    
    public void auxPressed(int i)
    {
    	super.auxPressed(i);
    	
    	if(i == 20){
    		if(!APmode1) {
    			APmode1 = true;
    			HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude ON");
            	((FlightModelMain) (super.FM)).AP.setStabAltitude(1000);
    		} else  if(APmode1) {
        			APmode1 = false;
        			HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude OFF");
                	((FlightModelMain) (super.FM)).AP.setStabAltitude(false);
    		}
    	}
    	
    	if(i == 21){
    		if(!APmode2) {
    			APmode2 = true;
    			HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction ON");
    			((FlightModelMain) (super.FM)).AP.setStabDirection(true);
    			((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
    		} else  if(APmode2) {
        			APmode2 = false;
        			HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction OFF");
        			((FlightModelMain) (super.FM)).AP.setStabDirection(false);
        			((FlightModelMain) (super.FM)).CT.bHasRudderControl = true;
    		}
    	}
    	
    	if(i == 22){
    		if(!APmode3) {
    			APmode3 = true;
    			HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route ON");
    			((FlightModelMain) (super.FM)).AP.setWayPoint(true);
    		} else  if(APmode3) {
        			APmode3 = false;
        			HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route OFF");
        			((FlightModelMain) (super.FM)).AP.setWayPoint(false);
        			((FlightModelMain) (super.FM)).CT.AileronControl = 0.0F;
        			((FlightModelMain) (super.FM)).CT.ElevatorControl = 0.0F;
        			((FlightModelMain) (super.FM)).CT.RudderControl = 0.0F;
    		}
    	}
    	
    	if(i == 23){
    		((FlightModelMain) (super.FM)).CT.AileronControl = 0.0F;
			((FlightModelMain) (super.FM)).CT.ElevatorControl = 0.0F;
			((FlightModelMain) (super.FM)).CT.RudderControl = 0.0F;
			((FlightModelMain) (super.FM)).AP.setWayPoint(false);
			((FlightModelMain) (super.FM)).AP.setStabDirection(false);
			((FlightModelMain) (super.FM)).AP.setStabAltitude(false);
			APmode1 = false;
			APmode2 = false;
			APmode3 = false;
			HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: All Off");
    	}
    	
    	if(i == 24){	
			if(!laserOn) {
    			laserOn = true;
    			laserLock = false;
    			HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: On");
    			fSightCurSideslip = 0.0F;
    			fSightCurForwardAngle = 0.0F;
    			
    		} else  if(laserOn) {
        			laserOn = false;
        			laserLock = false;
        			HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: Off");
        			fSightCurSideslip = 0.0F;
        			fSightCurForwardAngle = 0.0F;
    		}
    	}
    	
    	if(i == 25){	
			if(!laserLock) {
				laserLock = true;
    			HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: Locked");
    			fSightCurSideslip = 0.0F;
    			fSightCurForwardAngle = 0.0F;
    			
    		} else  if(laserLock) {
    			laserLock = false;
        			HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: Unlocked");
        			fSightCurSideslip = 0.0F;
        			fSightCurForwardAngle = 0.0F;
    		}
    	}
    		
    	
    }
    

    public boolean typeRadarToggleMode() {
//    	radarmode++;
//		if(radarmode > 3)
//			radarmode = 0;
//		
//		if(radarmode == 0)
//        {
//            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
//            {
//                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: All Off");
//            	((FlightModelMain) (super.FM)).AP.setStabAltitude(false);
//            	((FlightModelMain) (super.FM)).AP.setStabDirection(false);
//            	((FlightModelMain) (super.FM)).AP.setStabAll(false);
//            }
//        } else
//        if(radarmode == 1)
//        {
//            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
//            {
//            	HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude");
//            	((FlightModelMain) (super.FM)).AP.setStabAltitude(1000);
//            	((FlightModelMain) (super.FM)).AP.setStabDirection(false);
//            	((FlightModelMain) (super.FM)).AP.setWayPoint(false);
//            }
//        } else
//        	if(radarmode == 2)
//            {
//                if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
//                {
//                	HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction");
//                	((FlightModelMain) (super.FM)).AP.setStabAltitude(false);
//                	((FlightModelMain) (super.FM)).AP.setStabDirection(true);
//                	((FlightModelMain) (super.FM)).AP.setWayPoint(false);
//                }
//            } else
//            	if(radarmode == 3)
//                {
//                    if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
//                    {
//                    	HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Navigation");
//                    	((FlightModelMain) (super.FM)).AP.setStabAltitude(false);
//                    	((FlightModelMain) (super.FM)).AP.setStabDirection(false);
//                    	((FlightModelMain) (super.FM)).AP.setWayPoint(true);
//                    }
//                }
		return true;
	}
    
    public void rareAction(float f, boolean flag) {
    		if (counter++ % 5 == 0) {
    			sirenaWarning();
    	}
		super.rareAction(f, flag);
	}
    
//TODO: RWR code...
	
    private boolean sirenaWarning()
    {
//SPIKE Code:
    	boolean SPIKE = false;
		Point3d point3d = new Point3d();
		super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft spike = War.getNearestEnemy(this, 6000F);
        if (spike != null)
        {
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Actor) (spike)).pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (spike)).pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (spike)).pos.getAbsPoint().z;
        double d3 = d2 - (double)Landscape.Hmin((float)((Actor) (spike)).pos.getAbsPoint().x, (float)((Actor) (spike)).pos.getAbsPoint().y);
        if(d3 < 0.0D)
            d3 = 0.0D;
        int i = (int)(-((double)((Actor) (spike)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i = 360 + i;
        int j = (int)(-((double)((Actor) (spike)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j = 360 + j;
        Actor actor = War.getNearestEnemy(spike, 6000F);
        if((actor instanceof Aircraft) && spike.getArmy() != World.getPlayerArmy() && (spike instanceof TypeFighterAceMaker)&& ((spike instanceof TypeSupersonic) || (spike instanceof TypeFastJet)) && actor == World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
        	{
                pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                new String();
                new String();
                int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
                int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                boolean flag2 = false;
                Engine.land();
                int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                if(i1 >= 28 && i1 < 32 && f < 7.5F)
                    flag2 = true;
                new String();
                double d8 = d4 - d;
                double d9 = d5 - d1;
                float f1 = 57.32484F * (float)Math.atan2(d9, -d8);
                int j1 = (int)(Math.floor((int)f1) - 90D);
                if(j1 < 0)
                    j1 = 360 + j1;
                int k1 = j1 - i;
                double d10 = d - d4;
                double d11 = d1 - d5;
                Random random = new Random();
                float f2 = ((float)random.nextInt(20) - 10F) / 100F + 1.0F;
                int l1 = random.nextInt(6) - 3;
                float f3 = 19000F;
                float f4 = f3;
                if(d3 < 1200D)
                    f4 = (float)(d3 * 0.80000001192092896D * 3D);
                int i2 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f2) / 10D) * 10D);
                if((float)i2 > f3)
                    i2 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
                float f5 = 57.32484F * (float)Math.atan2(i2, d7);
                int j2 = (int)(Math.floor((int)f5) - 90D);
                int k2 = (j2 - (90 - j)) + l1;
                int l2 = (int)f3;
                if((float)i2 < f3)
                    if(i2 > 1150)
                        l2 = (int)(Math.ceil((double)i2 / 900D) * 900D);
                    else
                        l2 = (int)(Math.ceil((double)i2 / 500D) * 500D);
                int i3 = k1 + l1;
                int j3 = i3;
                if(j3 < 0)
                    j3 += 360;
                float f6 = (float)((double)f4 + Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 3D)) * ((double)f4 * 0.25D));
                int k3 = (int)((double)f6 * Math.cos(Math.toRadians(k2)));
                if((double)i2 <= (double)k3 && (double)i2 <= 14000D && (double)i2 >= 200D && k2 >= -30 && k2 <= 30 && Math.sqrt(i3 * i3) <= 60D)
                {
                    SPIKE = true;
                } else {
                	SPIKE = false;
                }
                
        	}
        

   	
    	
 //SPO 10 Code:

		Aircraft aircraft = World.getPlayerAircraft();
		double dd = Main3D.cur3D().land2D.worldOfsX()
				+ ((Actor) (actor)).pos.getAbsPoint().x;
		double dd1 = Main3D.cur3D().land2D.worldOfsY()
				+ ((Actor) (actor)).pos.getAbsPoint().y;
		double dd2 = Main3D.cur3D().land2D.worldOfsY()
				+ ((Actor) (actor)).pos.getAbsPoint().z;
		int ii = (int) (-((double) ((Actor) (aircraft)).pos.getAbsOrient()
				.getYaw() - 90D));
		if (ii < 0)
			ii = 360 + ii;
		if(SPIKE && actor == World.getPlayerAircraft())
    	{
			pos.getAbs(point3d);
			double d31 = Main3D.cur3D().land2D.worldOfsX()
					+ spike.pos.getAbsPoint().x;
			double d41 = Main3D.cur3D().land2D.worldOfsY()
					+ spike.pos.getAbsPoint().y;
			double d51 = Main3D.cur3D().land2D.worldOfsY()
					+ spike.pos.getAbsPoint().z;
			double d81 = (int) (Math.ceil((dd2 - d51) / 10D) * 10D);
			String s = "";
			if (dd2 - d51 - 500D >= 0.0D)
				s = " low";
			if ((dd2 - d51) + 500D < 0.0D)
				s = " high";
			new String();
			double d91 = d31 - dd;
			double d101 = d41 - dd1;
			float f11 = 57.32484F * (float) Math.atan2(d101, -d91);
			int j11 = (int) (Math.floor((int) f11) - 90D);
			if (j11 < 0)
				j11 = 360 + j11;
			int k11 = j11 - ii;
			if (k11 < 0)
				k11 = 360 + k11;
			int l11 = (int) (Math.ceil((double) (k11 + 15) / 30D) - 1.0D);
			if (l11 < 1)
				l11 = 12;
			double d111 = dd - d31;
			double d12 = dd1 - d41;
			double d13 = Math
					.ceil(Math.sqrt(d12 * d12 + d111 * d111) / 10D) * 10D;
				bRadarWarning = d13 <= 8000D && d13 >= 500D
						&& Math.sqrt(d81 * d81) <= 6000D;
				HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-10: Spike at " + l11 + " o'clock" + s + "!");
				playSirenaWarning(bRadarWarning);
    			} else {
    				bRadarWarning = false;
    				playSirenaWarning(bRadarWarning);
    			}
        }
			
//Missile detection code:		
	return true;
    }
    
    private boolean sirenaLaunchWarning()
    {
    	
    	Point3d point3d = new Point3d();
        pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Actor actor = World.getPlayerAircraft();
        super.pos.getAbs(point3d);
		Aircraft aircraft = World.getPlayerAircraft();
		double dd = Main3D.cur3D().land2D.worldOfsX()
				+ ((Actor) (actor)).pos.getAbsPoint().x;
		double dd1 = Main3D.cur3D().land2D.worldOfsY()
				+ ((Actor) (actor)).pos.getAbsPoint().y;
		double dd2 = Main3D.cur3D().land2D.worldOfsY()
				+ ((Actor) (actor)).pos.getAbsPoint().z;
		int ii = (int) (-((double) ((Actor) (aircraft)).pos.getAbsOrient()
				.getYaw() - 90D));
		if (ii < 0)
			ii = 360 + ii;
		List list = Engine.missiles();
		int m = list.size();
		for (int t = 0; t < m; t++) {
			// Actor actor = (Actor)entry.getValue();
			Actor missile = (Actor) list.get(t);
		
		if((missile instanceof com.maddox.il2.objects.weapons.Missile || missile instanceof com.maddox.il2.objects.weapons.MissileSAM) && missile.getArmy() != World.getPlayerArmy() && missile.getSpeed(vector3d) > 20D && ((Missile) missile).getMissileTarget() == actor)
    	{
				pos.getAbs(point3d);
				double d31 = Main3D.cur3D().land2D.worldOfsX()
						+ actor.pos.getAbsPoint().x;
				double d41 = Main3D.cur3D().land2D.worldOfsY()
						+ actor.pos.getAbsPoint().y;
				double d51 = Main3D.cur3D().land2D.worldOfsY()
						+ actor.pos.getAbsPoint().z;
				double d81 = (int) (Math.ceil((dd2 - d51) / 10D) * 10D);
				String s = "";
				if (dd2 - d51 - 500D >= 0.0D)
					s = " LOW";
				if ((dd2 - d51) + 500D < 0.0D)
					s = " HIGH";
				new String();
				double d91 = d31 - dd;
				double d101 = d41 - dd1;
				float f11 = 57.32484F * (float) Math.atan2(d101, -d91);
				int j11 = (int) (Math.floor((int) f11) - 90D);
				if (j11 < 0)
					j11 = 360 + j11;
				int k11 = j11 - ii;
				if (k11 < 0)
					k11 = 360 + k11;
				int l11 = (int) (Math.ceil((double) (k11 + 15) / 30D) - 1.0D);
				if (l11 < 1)
					l11 = 12;
				double d111 = dd - d31;
				double d12 = dd1 - d41;
				double d13 = Math
						.ceil(Math.sqrt(d12 * d12 + d111 * d111) / 10D) * 10D;
				bRadarWarning = d13 <= 8000D && d13 >= 500D
						&& Math.sqrt(d81 * d81) <= 6000D;
				HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-10: MISSILE AT " + l11 + " O'CLOCK" + s + "!!!");
				playSirenaWarning(bRadarWarning);
				if ((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) && (FM instanceof Maneuver))
				{
					backFire();
					
//					if (FM.CT.Weapons[7] != null) {
//						for (int i = 0; i < FM.CT.Weapons[7].length; ++i) {
//							if ((FM.CT.Weapons[7][i] != null) && (FM.CT.Weapons[7][i].countBullets() != 0)) {
//								FM.CT.Weapons[7][i].shots(3);
//							}
//						}
//					}
				}
    			} else {
    				bRadarWarning = false;
    				playSirenaWarning(bRadarWarning);
    			}
		}
	return true;
    }
		
    public void playSirenaWarning(boolean isThreatened)
    {
        if(isThreatened && !sirenaSoundPlaying)
        {
//            fxSirena.play(smplSirena);
//            sirenaSoundPlaying = true;
        } else
        if(!isThreatened && sirenaSoundPlaying)
        {
//            fxSirena.cancel();
//            sirenaSoundPlaying = false;
        }
    }
    
    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public long getChaffDeployed()
    {
        if(hasChaff)
            return lastChaffDeployed;
        else
            return 0L;
    }

    public long getFlareDeployed()
    {
        if(hasFlare)
            return lastFlareDeployed;
        else
            return 0L;
    }

    public void setCommonThreatActive()
    {
        long l = Time.current();
        if(l - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = l;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long l = Time.current();
        if(l - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = l;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long l = Time.current();
        if(l - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
        {
            lastMissileLaunchThreatActive = l;
            doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat()
    {
    }

    private void doDealRadarLockThreat()
    {
    }

    private void doDealMissileLaunchThreat()
    {
    }

    public void onAircraftLoaded()
    {
    	checkAmmo();
        super.onAircraftLoaded();
        super.bHasSK1Seat = false;
        ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
        
        bHasDeployedDragChute = false;
        guidedMissileUtils.onAircraftLoaded();
    }

    public void update(float f)
    {
    	if(laserOn){
    		laserUpdate();
    	}
    	sirenaLaunchWarning();
        guidedMissileUtils.update();
        super.update(f);
        if(((FlightModelMain) (super.FM)).CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute1 = new Chute(this);
            chute2 = new Chute(this);
            chute1.setMesh("3do/plane/ChuteSu_25/mono.sim");
            chute2.setMesh("3do/plane/ChuteSu_25/mono.sim");
//            chute1.collide(true);
//            chute2.collide(true);
            chute1.mesh().setScale(0.5F);
            chute2.mesh().setScale(0.5F);
            ((Actor) (chute1)).pos.setRel(new Point3d(-8D, 0.0D, 0.59999999999999998D), new Orient(20.0F, 90F, 0.0F));
            ((Actor) (chute2)).pos.setRel(new Point3d(-8D, 0.0D, 0.59999999999999998D), new Orient(-20.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl)
            if(((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() > 600F || ((FlightModelMain) (super.FM)).CT.DragChuteControl < 1.0F)
            {
                if(chute1 != null && chute2 != null)
                {
                    chute1.tangleChute(this);
                    chute2.tangleChute(this);
                    ((Actor) (chute1)).pos.setRel(new Point3d(-15D, 0.0D, 1.0D), new Orient(20.0F, 90F, 0.0F));
                    ((Actor) (chute2)).pos.setRel(new Point3d(-15D, 0.0D, 1.0D), new Orient(-20.0F, 90F, 0.0F));
                }
                ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
                ((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() < 20F)
            {
                if(chute1 != null && chute2 != null) {
                    chute1.tangleChute(this);
                	chute2.tangleChute(this);
                }
                ((Actor) (chute1)).pos.setRel(new Orient(10.0F, 100F, 0.0F));
                ((Actor) (chute2)).pos.setRel(new Orient(-10.0F, 100F, 0.0F));
                ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
                ((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !((FlightModelMain) (super.FM)).CT.bHasDragChuteControl && Time.current() > removeChuteTimer) {
            chute1.destroy();
        	chute2.destroy();
        }
        guidedMissileUtils.update();
        if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 10000D;
        if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 10000D;
        if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 0.97999999999999998D && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 10000D;
        if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 0.97999999999999998D && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() < 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 10000D;
        if(super.FM.getAltitude() > 500F && (double)calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 7000D;
        if(super.FM.getAltitude() > 500F && (double)calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 7000D;
        if(super.FM.getAltitude() > 1000F && (double)calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 1500D;
        if(super.FM.getAltitude() > 1000F && (double)calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 1500D;
        if(super.FM.getAltitude() > 1500F && (double)calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 1500D;
        if(super.FM.getAltitude() > 1500F && (double)calculateMach() >= 1.2D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 1500D;
        if(super.FM.getAltitude() > 10000F && (double)calculateMach() >= 2.23D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 10000F && (double)calculateMach() >= 2.23D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 11000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 4000D;
        if(super.FM.getAltitude() > 11000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 4000D;
        if(super.FM.getAltitude() > 12500F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
        if(super.FM.getAltitude() > 12500F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
        if(super.FM.getAltitude() > 14000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
        if(super.FM.getAltitude() > 14000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
        if(super.FM.getAltitude() > 15000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
        if(super.FM.getAltitude() > 15000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
        if(super.FM.getAltitude() > 15500F && (double)calculateMach() >= 2.0099999999999998D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 15500F && (double)calculateMach() >= 2.0099999999999998D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 16000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
        if(super.FM.getAltitude() > 16000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 2000D;
        if(super.FM.getAltitude() > 16000F && (double)calculateMach() >= 1.9199999999999999D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 16000F && (double)calculateMach() >= 1.9199999999999999D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 17000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3000D;
        if(super.FM.getAltitude() > 17000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3000D;
        if(super.FM.getAltitude() > 17000F && (double)calculateMach() >= 1.8200000000000001D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 17000F && (double)calculateMach() >= 1.8200000000000001D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 17500F && (double)calculateMach() >= 1.73D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 17500F && (double)calculateMach() >= 1.73D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 18000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3000D;
        if(super.FM.getAltitude() > 18000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3000D;
        if(super.FM.getAltitude() > 18000F && (double)calculateMach() >= 1.5900000000000001D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 18000F && (double)calculateMach() >= 1.5900000000000001D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 18500F && (double)calculateMach() >= 1.5800000000000001D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 18500F && (double)calculateMach() >= 1.5800000000000001D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 18800F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3000D;
        if(super.FM.getAltitude() > 18800F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 3000D;
        if(super.FM.getAltitude() > 19000F && (double)calculateMach() >= 1.52D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 19000F && (double)calculateMach() >= 1.52D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 8000D;
        if(super.FM.getAltitude() > 20000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 7000D;
        if(super.FM.getAltitude() > 20000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 7000D;
        if(super.FM.getAltitude() > 21000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 17000D;
        if(super.FM.getAltitude() > 21000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x -= 17000D;
    }






	public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 60F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void doEjectCatapult()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 40D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(10, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat1_D0", false);
        super.FM.setTakenMortalDamage(true, null);
        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
        ((FlightModelMain) (super.FM)).CT.bHasAileronControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = false;
    }
    
    private static Point3d LaserP3 = new Point3d();
    
    public boolean laserOn;
    public boolean laserLock;
    
	private float[] headPos = new float[3];
	private float[] headOr = new float[3];
	private float pilotHeadT = 0.0F;
	private float pilotHeadY = 0.0F;
	public static Orient tmpOr = new Orient();
	private static Vector3d Ve = new Vector3d();

    private Hook[] LaserHook = { null, null, null, null };
    
    private LightPointWorld[] Laser;

    private static Loc LaserLoc1 = new Loc();
    private static Point3d LaserP1 = new Point3d();
    private static Point3d LaserP2 = new Point3d();
    private static Point3d LaserPL = new Point3d();
    
    private static Loc LaserLoc1i = new Loc();
    private static Point3d LaserP1i = new Point3d();
    private static Point3d LaserP2i = new Point3d();
    private static Point3d LaserPLi = new Point3d();
    
    private long tX4Prev;
    private boolean backfire;
    private ArrayList backfireList;
    
    public boolean APmode1;
    public boolean APmode2;
    public boolean APmode3;
    
    public int radarmode;
	private int counter = 0;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private long lastCommonThreatActive;
    private long intervalCommonThreat;
    private long lastRadarLockThreatActive;
    private long intervalRadarLockThreat;
    private long lastMissileLaunchThreatActive;
    private long intervalMissileLaunchThreat;
    private boolean bHasDeployedDragChute;
    private Chute chute1;
    private Chute chute2;
    private long removeChuteTimer;
    private BulletEmitter g1;
    private int oldbullets;
    public static boolean bChangedPit = false;
    private float dynamoOrient;
    public boolean bToFire;
    
    private SoundFX fxSPO10;
	private Sample smplSPO10;
	private boolean SPO10SoundPlaying;

	 private SoundFX fxSirenaLaunch;
	 private Sample smplSirenaLaunch;
	 private boolean sirenaLaunchSoundPlaying;
	 private boolean bRadarWarningLaunch;
	    
	 private SoundFX fxSirena;
	 private Sample smplSirena;
	 private boolean sirenaSoundPlaying;
	 
	 private boolean bRadarWarning;
	
    static 
    {
        Class class1 = com.maddox.il2.objects.air.Su_25.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Su-25");
        Property.set(class1, "meshName", "3DO/Plane/Su-25/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944.9F);
        Property.set(class1, "yearExpired", 1948.3F);
        Property.set(class1, "FlightModel", "FlightModels/SU-25.fmd:SU25FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitSu_25.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 
            9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 
            9, 9, 0, 0, 0, 0, 2, 2, 2, 2,
            2, 2, 3, 3, 3, 3, 3, 3, 3, 3,
            9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", 
            "_ExternalBomb09", "_ExternalBomb10", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", 
            "_ExternalDev09", "_ExternalDev10", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08",
            "_ExternalDev11", "_ExternalDev12", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12",
            "_Flare01", "_Flare02", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18",
            "_ExternalDev13", "_ExternalDev14",
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 52;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xFAB_250M46+2xR_60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "6xFAB-250M46+2xPTB800+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "8xRBK-250+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "6xRBK-250+2xPTB800+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "8xZB-360+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "8xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "8xS-24+2xR_60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "6xS-24+2xPTB800+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "2xS-24+4xFAB-250=2xPTB800+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "2xRBK-250=2xS-24+2xB-8+2xPTB800+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunS24", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "4xRBK-250+2xB-8+2xPTB800+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            

            s = "6xRBK-250+2xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);

            s = "4xFAB-250M46+2xB-8+2xPTB800+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);

            s = "2xFAB-250M46+4xB-8+2xPTB800+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);

            s = "6xFAB-250M46+2xSPPU-22+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
	a_lweaponslot[32] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
	a_lweaponslot[33] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
	a_lweaponslot[34] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
	a_lweaponslot[35] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
	a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);

            s = "6xB-8+2xSPPU-22+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
	a_lweaponslot[32] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
	a_lweaponslot[33] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
	a_lweaponslot[34] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
	a_lweaponslot[35] = new Aircraft._WeaponSlot(0, "MGunGSH23ki", 150);
	a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);

            s = "2xKh-29L+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
           
            
            s = "2xKh-29L+2xPTB-800L+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);            
            
            s = "2xKh-29L+4xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);             
            
            s = "2xKh-29L+2xPTB-800L+4xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "BombGunNull", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunKh29", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);     
            
            s = "8xFAB-100+6xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);   
            
            s = "2xKMGU-2(PTAB-2.5)+6xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2", 1);
            
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);  
            
            s = "2xKMGU-2(AO-2.5RT)+6xB-8+2xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunGSH_301ki", 150);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2", 1);
            
            a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "BombGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);  
            
            s = "None";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}
