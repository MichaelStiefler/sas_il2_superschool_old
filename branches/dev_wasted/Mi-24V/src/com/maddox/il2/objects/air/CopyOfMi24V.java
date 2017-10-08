package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.Missile;
import com.maddox.il2.objects.weapons.RocketGunFlare;
import com.maddox.il2.objects.weapons.RocketGunSturmV;
import com.maddox.il2.game.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapExt;
import com.maddox.il2.fm.RealFlightModel;
import java.util.Random;
import java.util.List;
import com.maddox.JGP.*;
import com.maddox.rts.Time;
import com.maddox.sound.*;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;



// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, Aircraft, PaintSchemeFMPar05, TypeScout, 
//            TypeTransport, TypeStormovik, NetAircraft

public class CopyOfMi24V extends Scheme2a
    implements TypeStormovik, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeFighter, TypeLaserSpotter
{

    public CopyOfMi24V()
    {
        suka = new Loc();
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
        rotorrpm = 0;
        obsLookTime = 0;
        obsLookAzimuth = 0.0F;
        obsLookElevation = 0.0F;
        obsAzimuth = 0.0F;
        obsElevation = 0.0F;
        obsAzimuthOld = 0.0F;
        obsElevationOld = 0.0F;
        obsMove = 0.0F;
        obsMoveTot = 0.0F;
        bObserverKilled = false;
        guidedMissileUtils = new GuidedMissileUtils(this);
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
        guidedMissileUtils = new GuidedMissileUtils(this);
        fxSirena = newSound("aircraft.Sirena2", false);
        smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
        smplSirena.setInfinite(true);
        collV = 0.0F; //By PAL
        lcollV = collV; //By PAL
        hover = false;
        
		APmode1 = false;
		APmode2 = false;
		APmode3 = false;
		
		laserOn = false;
		
		backfireList = new ArrayList();
		backfire = false;
		tX4Prev = 0L;
		
    }
    
    public void laserUpdate() {
    	
    	if(laserOn){
//    	hierMesh().chunkSetAngles("Laser_D0", LaserOr.getTangage(), -LaserOr.getYaw(), 0.0F);
    	
//    		hierMesh().chunkSetAngles("LTurret2A", -LaserOr.getYaw(), 0.0F, 0.0F);
//    		hierMesh().chunkSetAngles("LTurret2B", 0.0F, LaserOr.getTangage(), 0.0F);
    		
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
    		
    		CopyOfMi24V.spot.set(LaserP2);
    		
    		Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(LaserP2.x, LaserP2.y, LaserP2.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
    	}
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
			HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: All Off");
    	}
    	
    	if(i == 24){	
			if(!laserOn) {
    			laserOn = true;
    			laserLock = false;
    			HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: On");
    			
    		} else  if(laserOn) {
        			laserOn = false;
        			laserLock = false;
        			HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: Off");
    		}
    	}
    		
    	
    }
    
    
//    public boolean typeDiveBomberToggleAutomation()
//    {
//		return APmode1;
//    }

    public void typeDiveBomberAdjVelocityPlus()
    {
    }

    public void typeDiveBomberAdjVelocityMinus()
    {
    }

    public void typeDiveBomberAdjVelocityReset()
    {
    }

    public void typeDiveBomberAdjDiveAngleReset()
    {
    }

    public void typeDiveBomberAdjDiveAnglePlus()
    {
    }

    public void typeDiveBomberAdjDiveAngleMinus()
    {
    }

    public void typeDiveBomberAdjAltitudeReset()
    {
    }

    public void typeDiveBomberAdjAltitudePlus()
    {
    }

    public void typeDiveBomberAdjAltitudeMinus()
    {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
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
        long curTime = Time.current();
        if(curTime - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = curTime;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long curTime = Time.current();
        if(curTime - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = curTime;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long curTime = Time.current();
        if(curTime - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
        {
            lastMissileLaunchThreatActive = curTime;
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

//TODO: RWR code...
	
//    private boolean sirenaWarning()
//    {
////SPIKE Code:
//    	boolean SPIKE = false;
//		Point3d point3d = new Point3d();
//		super.pos.getAbs(point3d);
//        Vector3d vector3d = new Vector3d();
//        Aircraft spike = War.getNearestEnemy(this, 6000F);
//        if (spike != null)
//        {
//        double d = Main3D.cur3D().land2D.worldOfsX() + ((Actor) (spike)).pos.getAbsPoint().x;
//        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (spike)).pos.getAbsPoint().y;
//        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (spike)).pos.getAbsPoint().z;
//        double d3 = d2 - (double)Landscape.Hmin((float)((Actor) (spike)).pos.getAbsPoint().x, (float)((Actor) (spike)).pos.getAbsPoint().y);
//        if(d3 < 0.0D)
//            d3 = 0.0D;
//        int i = (int)(-((double)((Actor) (spike)).pos.getAbsOrient().getYaw() - 90D));
//        if(i < 0)
//            i = 360 + i;
//        int j = (int)(-((double)((Actor) (spike)).pos.getAbsOrient().getPitch() - 90D));
//        if(j < 0)
//            j = 360 + j;
//        Actor actor = War.getNearestEnemy(spike, 6000F);
//        if((actor instanceof Aircraft) && spike.getArmy() != World.getPlayerArmy() && (spike instanceof TypeFighterAceMaker)&& ((spike instanceof TypeSupersonic) || (spike instanceof TypeFastJet)) && actor == World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
//        	{
//                pos.getAbs(point3d);
//                double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
//                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
//                double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
//                new String();
//                new String();
//                int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
//                double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
//                boolean flag2 = false;
//                Engine.land();
//                int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
//                float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
//                if(i1 >= 28 && i1 < 32 && f < 7.5F)
//                    flag2 = true;
//                new String();
//                double d8 = d4 - d;
//                double d9 = d5 - d1;
//                float f1 = 57.32484F * (float)Math.atan2(d9, -d8);
//                int j1 = (int)(Math.floor((int)f1) - 90D);
//                if(j1 < 0)
//                    j1 = 360 + j1;
//                int k1 = j1 - i;
//                double d10 = d - d4;
//                double d11 = d1 - d5;
//                Random random = new Random();
//                float f2 = ((float)random.nextInt(20) - 10F) / 100F + 1.0F;
//                int l1 = random.nextInt(6) - 3;
//                float f3 = 19000F;
//                float f4 = f3;
//                if(d3 < 1200D)
//                    f4 = (float)(d3 * 0.80000001192092896D * 3D);
//                int i2 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f2) / 10D) * 10D);
//                if((float)i2 > f3)
//                    i2 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
//                float f5 = 57.32484F * (float)Math.atan2(i2, d7);
//                int j2 = (int)(Math.floor((int)f5) - 90D);
//                int k2 = (j2 - (90 - j)) + l1;
//                int l2 = (int)f3;
//                if((float)i2 < f3)
//                    if(i2 > 1150)
//                        l2 = (int)(Math.ceil((double)i2 / 900D) * 900D);
//                    else
//                        l2 = (int)(Math.ceil((double)i2 / 500D) * 500D);
//                int i3 = k1 + l1;
//                int j3 = i3;
//                if(j3 < 0)
//                    j3 += 360;
//                float f6 = (float)((double)f4 + Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 3D)) * ((double)f4 * 0.25D));
//                int k3 = (int)((double)f6 * Math.cos(Math.toRadians(k2)));
//                if((double)i2 <= (double)k3 && (double)i2 <= 14000D && (double)i2 >= 200D && k2 >= -30 && k2 <= 30 && Math.sqrt(i3 * i3) <= 60D)
//                {
//                    SPIKE = true;
//                } else {
//                	SPIKE = false;
//                }
//                
//        	}
//        
//
//   	
//    	
// //SPO 10 Code:
//
//		Aircraft aircraft = World.getPlayerAircraft();
//		double dd = Main3D.cur3D().land2D.worldOfsX()
//				+ ((Actor) (actor)).pos.getAbsPoint().x;
//		double dd1 = Main3D.cur3D().land2D.worldOfsY()
//				+ ((Actor) (actor)).pos.getAbsPoint().y;
//		double dd2 = Main3D.cur3D().land2D.worldOfsY()
//				+ ((Actor) (actor)).pos.getAbsPoint().z;
//		int ii = (int) (-((double) ((Actor) (aircraft)).pos.getAbsOrient()
//				.getYaw() - 90D));
//		if (ii < 0)
//			ii = 360 + ii;
//		if(SPIKE && actor == World.getPlayerAircraft())
//    	{
//			pos.getAbs(point3d);
//			double d31 = Main3D.cur3D().land2D.worldOfsX()
//					+ spike.pos.getAbsPoint().x;
//			double d41 = Main3D.cur3D().land2D.worldOfsY()
//					+ spike.pos.getAbsPoint().y;
//			double d51 = Main3D.cur3D().land2D.worldOfsY()
//					+ spike.pos.getAbsPoint().z;
//			double d81 = (int) (Math.ceil((dd2 - d51) / 10D) * 10D);
//			String s = "";
//			if (dd2 - d51 - 500D >= 0.0D)
//				s = " low";
//			if ((dd2 - d51) + 500D < 0.0D)
//				s = " high";
//			new String();
//			double d91 = d31 - dd;
//			double d101 = d41 - dd1;
//			float f11 = 57.32484F * (float) Math.atan2(d101, -d91);
//			int j11 = (int) (Math.floor((int) f11) - 90D);
//			if (j11 < 0)
//				j11 = 360 + j11;
//			int k11 = j11 - ii;
//			if (k11 < 0)
//				k11 = 360 + k11;
//			int l11 = (int) (Math.ceil((double) (k11 + 15) / 30D) - 1.0D);
//			if (l11 < 1)
//				l11 = 12;
//			double d111 = dd - d31;
//			double d12 = dd1 - d41;
//			double d13 = Math
//					.ceil(Math.sqrt(d12 * d12 + d111 * d111) / 10D) * 10D;
//				bRadarWarning = d13 <= 8000D && d13 >= 500D
//						&& Math.sqrt(d81 * d81) <= 6000D;
//				HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-10: Spike at " + l11 + " o'clock" + s + "!");
//				playSirenaWarning(bRadarWarning);
//    			} else {
//    				bRadarWarning = false;
//    				playSirenaWarning(bRadarWarning);
//    			}
//        }
//			
////Missile detection code:		
//	return true;
//    }
    
//    private boolean sirenaLaunchWarning()
//    {
//    	
//    	Point3d point3d = new Point3d();
//        pos.getAbs(point3d);
//        Vector3d vector3d = new Vector3d();
//        Actor actor = World.getPlayerAircraft();
//        super.pos.getAbs(point3d);
//		Aircraft aircraft = World.getPlayerAircraft();
//		double dd = Main3D.cur3D().land2D.worldOfsX()
//				+ ((Actor) (actor)).pos.getAbsPoint().x;
//		double dd1 = Main3D.cur3D().land2D.worldOfsY()
//				+ ((Actor) (actor)).pos.getAbsPoint().y;
//		double dd2 = Main3D.cur3D().land2D.worldOfsY()
//				+ ((Actor) (actor)).pos.getAbsPoint().z;
//		int ii = (int) (-((double) ((Actor) (aircraft)).pos.getAbsOrient()
//				.getYaw() - 90D));
//		if (ii < 0)
//			ii = 360 + ii;
//		List list = Engine.missiles();
//		int m = list.size();
//		for (int t = 0; t < m; t++) {
//			// Actor actor = (Actor)entry.getValue();
//			Actor missile = (Actor) list.get(t);
//		
//		if((missile instanceof com.maddox.il2.objects.weapons.Missile || missile instanceof com.maddox.il2.objects.weapons.MissileSAM) && missile.getArmy() != World.getPlayerArmy() && missile.getSpeed(vector3d) > 20D && ((Missile) missile).getMissileTarget() == actor)
//    	{
//				pos.getAbs(point3d);
//				double d31 = Main3D.cur3D().land2D.worldOfsX()
//						+ actor.pos.getAbsPoint().x;
//				double d41 = Main3D.cur3D().land2D.worldOfsY()
//						+ actor.pos.getAbsPoint().y;
//				double d51 = Main3D.cur3D().land2D.worldOfsY()
//						+ actor.pos.getAbsPoint().z;
//				double d81 = (int) (Math.ceil((dd2 - d51) / 10D) * 10D);
//				String s = "";
//				if (dd2 - d51 - 500D >= 0.0D)
//					s = " LOW";
//				if ((dd2 - d51) + 500D < 0.0D)
//					s = " HIGH";
//				new String();
//				double d91 = d31 - dd;
//				double d101 = d41 - dd1;
//				float f11 = 57.32484F * (float) Math.atan2(d101, -d91);
//				int j11 = (int) (Math.floor((int) f11) - 90D);
//				if (j11 < 0)
//					j11 = 360 + j11;
//				int k11 = j11 - ii;
//				if (k11 < 0)
//					k11 = 360 + k11;
//				int l11 = (int) (Math.ceil((double) (k11 + 15) / 30D) - 1.0D);
//				if (l11 < 1)
//					l11 = 12;
//				double d111 = dd - d31;
//				double d12 = dd1 - d41;
//				double d13 = Math
//						.ceil(Math.sqrt(d12 * d12 + d111 * d111) / 10D) * 10D;
//				bRadarWarning = d13 <= 8000D && d13 >= 500D
//						&& Math.sqrt(d81 * d81) <= 6000D;
//				HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-10: MISSILE AT " + l11 + " O'CLOCK" + s + "!!!");
//				playSirenaWarning(bRadarWarning);
//				if ((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) && (FM instanceof Maneuver))
//				{
//					backFire();
//					
////					if (FM.CT.Weapons[7] != null) {
////						for (int i = 0; i < FM.CT.Weapons[7].length; ++i) {
////							if ((FM.CT.Weapons[7][i] != null) && (FM.CT.Weapons[7][i].countBullets() != 0)) {
////								FM.CT.Weapons[7][i].shots(3);
////							}
////						}
////					}
//				}
//    			} else {
//    				bRadarWarning = false;
//    				playSirenaWarning(bRadarWarning);
//    			}
//		}
//	return true;
//    }
		
//    public void playSirenaWarning(boolean isThreatened)
//    {
//        if(isThreatened && !sirenaSoundPlaying)
//        {
////            fxSirena.play(smplSirena);
////            sirenaSoundPlaying = true;
//        } else
//        if(!isThreatened && sirenaSoundPlaying)
//        {
////            fxSirena.cancel();
////            sirenaSoundPlaying = false;
//        }
//    }
    
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

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.turret[1].bIsAIControlled = false;
        guidedMissileUtils.onAircraftLoaded();
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        
//        ((FlightModelMain) (super.FM)).CT.bHasArrestorControl = true;
//        bFLIRlock = false;
        
        if(super.FM.AS.bNavLightsOn)
        {
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            pos.getAbs(point3d, orient);
            l.set(point3d, orient);
            Eff3DActor eff3dactor = Eff3DActor.New(this, findHook("_RedLight"), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", 1.0F);
            eff3dactor.postDestroy(Time.current() + 500L);
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 0.3F, 0.3F);
            lightpointactor.light.setEmit(1.0F, 3F);
            eff3dactor.draw.lightMap().put("light", lightpointactor);
        }
        if(!bObserverKilled)
            if(obsLookTime == 0)
            {
                obsLookTime = 2 + World.Rnd().nextInt(1, 3);
                obsMoveTot = 1.0F + World.Rnd().nextFloat() * 1.5F;
                obsMove = 0.0F;
                obsAzimuthOld = obsAzimuth;
                obsElevationOld = obsElevation;
                if((double)World.Rnd().nextFloat() > 0.80000000000000004D)
                {
                    obsAzimuth = 0.0F;
                    obsElevation = 0.0F;
                } else
                {
                    obsAzimuth = World.Rnd().nextFloat() * 140F - 70F;
                    obsElevation = World.Rnd().nextFloat() * 50F - 20F;
                }
            } else
            {
                obsLookTime--;
            }
        if(FLIR)
            FLIR();
    }

    protected void moveElevator(float f)
    {
        //hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, 5 * f);
        updateControlsVisuals();
    }

    private final void updateControlsVisuals()
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F,  -7F * ((FlightModelMain) (super.FM)).CT.getElevator());
    }

    protected void moveAileron(float f)
    {
        //hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 5 * f, 0.0F);
        updateAileron();
    }

    private final void updateAileron()
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -7F * ((FlightModelMain) (super.FM)).CT.getAileron(), 0.0F);
    }

    protected void moveFlap(float f)
    {
    }

    protected void moveRudder(float f)
    {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
	float f_1_ = Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, -90.0F);
	float f_2_ = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, -85.0F);
	float f_3_ = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, 85.0F);
	float f_4_ = (f <= 0.5F ? Aircraft.cvt(f, 0.1F, 0.5F, 0.0F, 70.0F)
		      : Aircraft.cvt(f, 0.8F, 1.0F, 70.0F, 0.0F));
	float f_5_ = (f <= 0.5F ? Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, -70.0F)
		      : Aircraft.cvt(f, 0.8F, 1.0F, -70.0F, 0.0F));
    float f_6_ = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -90.0F);
    float f_7_ = Aircraft.cvt(f, 0.1F, 0.8F, 0.0F, 90.0F);
    float f_8_ = Aircraft.cvt(f, 0.2F, 0.9F, 0.0F, -90.0F);
    float f_9_ = Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, -140.0F);
	hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, f_1_);
	hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 0.0F, f_9_);
	hiermesh.chunkSetAngles("GearL3_D0", 0.0F, f_2_, 0.0F);
	hiermesh.chunkSetAngles("GearR3_D0", 0.0F, f_3_, 0.0F);
	hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f_4_, 0.0F);
	hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f_5_, 0.0F);
	hiermesh.chunkSetAngles("GearL2_D0", f_8_, 0.0F, 0.0F);
	hiermesh.chunkSetAngles("GearR2_D0", f_7_, 0.0F, 0.0F);
    } 

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }


    public void moveSteering(float f)
    {
    }
    
    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.95F, 0.0F, 0.9F);
        hierMesh().chunkSetAngles("Door1_D0", -90F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Door2_D0", 0.0F, -65F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }
    
    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -120F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 120F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 120F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -120F * f, 0.0F);
    }

    protected void moveFan(float f)
    {
        rotorrpm = Math.abs((int)((double)(FM.EI.engines[0].getw() * 0.025F) + FM.Vwld.length() / 30D));
        if(rotorrpm >= 1)
            rotorrpm = 1;
        if((FM.EI.engines[0].getw() > 100F) && (FM.EI.engines[1].getw() > 100F))
        {
            hierMesh().chunkVisible("Prop1_D0", false);
            hierMesh().chunkVisible("PropRot1_D0", true);
        }
        if((FM.EI.engines[0].getw() < 100F) && (FM.EI.engines[1].getw() < 100F))
        {
            hierMesh().chunkVisible("Prop1_D0", true);
            hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if(hierMesh().isChunkVisible("Prop1_D1"))
        {
            hierMesh().chunkVisible("Prop1_D0", false);
            hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if((FM.EI.engines[0].getw() > 100F) && (FM.EI.engines[1].getw() > 100F))
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", true);
        }
        if((FM.EI.engines[0].getw() < 100F) && (FM.EI.engines[1].getw() < 100F))
        {
            hierMesh().chunkVisible("Prop2_D0", true);
            hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if(hierMesh().isChunkVisible("Prop2_D1"))
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if(hierMesh().isChunkVisible("Tail1_CAP"))
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", false);
            hierMesh().chunkVisible("Prop2_D1", false);
        }
        dynamoOrient = bDynamoRotary ? (dynamoOrient - 100F) % 360F : (float)((double)dynamoOrient - (double)rotorrpm * 25D) % 360F;
        hierMesh().chunkSetAngles("Prop1_D0", -dynamoOrient, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Prop2_D0", 0.0F, 0.0F, dynamoOrient);
    }
    
    private void TailRotorDamage()
    {
    	if(!TailRotorDestroyed)
    	{
        hierMesh().chunkVisible("Prop2_D0", false);
        hierMesh().chunkVisible("PropRot2_D0", false);
        hierMesh().chunkVisible("Prop2_D1", true);
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Tail Rotor: Destroyed!");
        TailRotorDestroyed = true;
    	}
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("GearL22_D0", 0.0F, cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, -10F), 0.0F);
        hierMesh().chunkSetAngles("GearR22_D0", 0.0F, cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.32F, 0.0F, 10F), 0.0F);
//        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[2], 0.0F, 0.0F, 0.0F, 0.0F);
//        hierMesh().chunkSetLocate("GearC3_D0", xyz, ypr);
    }


/*    private float floatindex(float f, float af[])
    {
        int i = (int)f;
        if(i >= af.length - 1)
            return af[af.length - 1];
        if(i < 0)
            return af[0];
        if(i == 0)
        {
            if(f > 0.0F)
                return af[0] + f * (af[1] - af[0]);
            else
                return af[0];
        } else
        {
            return af[i] + (f % (float)i) * (af[i + 1] - af[i]);
        }
    } */

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        boolean flag = false;
        boolean flag1 = (this instanceof CopyOfMi24V);
//        boolean flag2 = !(this instanceof IL_2_1940Early) && !(this instanceof IL_2_1940Late);
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.startsWith("xxarmorp"))
                {
                    int i = s.charAt(8) - 48;
                    switch(i)
                    {
                    default:
                        break;

                    case 1: // '\001'
                        getEnergyPastArmor(7.070000171661377D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                        shot.powerType = 0;
                        break;

                    case 2: // '\002'
                    case 3: // '\003'
                        getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
                        shot.powerType = 0;
                        if(shot.power <= 0.0F && Math.abs(v1.x) > 0.86599999666213989D)
                            doRicochet(shot);
                        break;

                    case 4: // '\004'
                        if(point3d.x > -1.3500000000000001D)
                        {
                            getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                            shot.powerType = 0;
                            if(shot.power <= 0.0F && Math.abs(v1.x) > 0.86599999666213989D)
                                doRicochet(shot);
                        } else
                        {
                            getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                        }
                        break;

                    case 5: // '\005'
                    case 6: // '\006'
                        getEnergyPastArmor(20.200000762939453D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
                        if(shot.power > 0.0F)
                            break;
                        if(Math.abs(v1.x) > 0.86599999666213989D)
                            doRicochet(shot);
                        else
                            doRicochetBack(shot);
                        break;

                    case 7: // '\007'
                        getEnergyPastArmor(20.200000762939453D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F)
                            doRicochetBack(shot);
                        break;
                    }
                }
                if(s.startsWith("xxarmorc1"))
                    getEnergyPastArmor(7.070000171661377D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                if(s.startsWith("xxarmort1"))
                    getEnergyPastArmor(6.059999942779541D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                return;
            }
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 1 && (double)World.Rnd().nextFloat() > Math.abs(v1.x) + 0.10000000149011612D && getEnergyPastArmor(3.4000000953674316D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: Keel Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if(s.startsWith("xxsparlm"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingLMid") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingLMid Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingLMid") > 2 && (double)World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparrm"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingRMid") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingRMid Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingRMid") > 2 && (double)World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparlo"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingLOut") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingLOut Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingLOut_D" + chunkDamageVisible("WingLOut"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingLOut") > 2 && (double)World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparro"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingROut") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingROut Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingROut_D" + chunkDamageVisible("WingROut"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingROut") > 2 && (double)World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(6.5D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(6.5D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.86F / (float)Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            }
            if(s.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
            }
            if(s.startsWith("xxeng"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("prop"))
                {
                    if(getEnergyPastArmor(3.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F)
                        if(World.Rnd().nextFloat() < 0.5F)
                        {
                            debuggunnery("Engine Module: Prop Governor Hit, Disabled..");
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        } else
                        {
                            debuggunnery("Engine Module: Prop Governor Hit, Oil Pipes Damaged..");
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        }
                } else
                if(s.endsWith("gear"))
                {
                    if(getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Engine Module: Reductor Hit, Bullet Jams Reductor Gear..");
                        FM.EI.engines[0].setEngineStuck(shot.initiator);
                    }
                } else
                if(s.endsWith("supc"))
                {
                    if(getEnergyPastArmor(0.01F, shot) > 0.0F)
                    {
                        debuggunnery("Engine Module: Supercharger Disabled..");
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                    }
                } else
                if(s.endsWith("feed"))
                {
                    if(getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if(World.Rnd().nextFloat() < 0.05F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else
                if(s.endsWith("fue1"))
                {
                    if(getEnergyPastArmor(0.89F, shot) > 0.0F)
                    {
                        debuggunnery("Engine Module: Fuel Feed Line Pierced, Engine Fires..");
                        FM.AS.hitEngine(shot.initiator, 0, 100);
                    }
                } else
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(2.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 175000F)
                        {
                            debuggunnery("Engine Module: Crank Case Hit, Bullet Jams Ball Bearings..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                        {
                            debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                    getEnergyPastArmor(22.5F, shot);
                } else
                if(s.endsWith("cyl1"))
                {
                    if(getEnergyPastArmor(1.3F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.75F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                        debuggunnery("Engine Module: Cylinders Assembly Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Operating..");
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                        {
                            debuggunnery("Engine Module: Cylinders Assembly Hit, Engine Fires..");
                            FM.AS.hitEngine(shot.initiator, 0, 3);
                        }
                        if(World.Rnd().nextFloat() < 0.01F)
                        {
                            debuggunnery("Engine Module: Cylinders Assembly Hit, Bullet Jams Piston Head..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        getEnergyPastArmor(22.5F, shot);
                    }
                    if(Math.abs(point3d.y) < 0.1379999965429306D && getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if(World.Rnd().nextFloat() < 0.05F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else
                if(s.startsWith("xxeng1mag"))
                {
                    int k = s.charAt(9) - 49;
                    debuggunnery("Engine Module: Magneto " + k + " Hit, Magneto " + k + " Disabled..");
                    FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, k);
                } else
                if(s.startsWith("xxeng1oil") && getEnergyPastArmor(0.5F, shot) > 0.0F)
                {
                    debuggunnery("Engine Module: Oil Radiator Hit, Oil Radiator Pierced..");
                    FM.AS.hitOil(shot.initiator, 0);
                }
            }
            if(s.startsWith("xxw1"))
                if(FM.AS.astateEngineStates[0] == 0)
                {
                    debuggunnery("Engine Module: Water Radiator Pierced..");
                    FM.AS.hitEngine(shot.initiator, 0, 1);
                    FM.AS.doSetEngineState(shot.initiator, 0, 1);
                } else
                if(FM.AS.astateEngineStates[0] == 1)
                {
                    debuggunnery("Engine Module: Water Radiator Pierced..");
                    FM.AS.hitEngine(shot.initiator, 0, 1);
                    FM.AS.doSetEngineState(shot.initiator, 0, 2);
                }
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(FM.AS.astateTankStates[l] == 0)
                    {
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced..");
                        FM.AS.hitTank(shot.initiator, 2, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 1);
                    } else
                    if(FM.AS.astateTankStates[l] == 1)
                    {
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced..");
                        FM.AS.hitTank(shot.initiator, 2, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 2);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.hitTank(shot.initiator, 2, 2);
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced, State Shifted..");
                    }
                }
            }
            if(s.startsWith("xxmgun"))
            {
                if(s.endsWith("01"))
                {
                    debuggunnery("Armament System: Left Machine Gun: Disabled..");
                    FM.AS.setJamBullets(0, 0);
                }
                if(s.endsWith("02"))
                {
                    debuggunnery("Armament System: Right Machine Gun: Disabled..");
                    FM.AS.setJamBullets(0, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
            }
            if(s.startsWith("xxcannon"))
            {
                if(s.endsWith("01") && getEnergyPastArmor(0.25F, shot) > 0.0F)
                {
                    debuggunnery("Armament System: Left Cannon: Disabled..");
                    FM.AS.setJamBullets(1, 0);
                }
                if(s.endsWith("02") && getEnergyPastArmor(0.25F, shot) > 0.0F)
                {
                    debuggunnery("Armament System: Right Cannon: Disabled..");
                    FM.AS.setJamBullets(1, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if(s.startsWith("xxammo"))
            {
                if(s.startsWith("xxammol1") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Left Cannon: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(1, 0);
                }
                if(s.startsWith("xxammor1") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Right Cannon: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(1, 1);
                }
                if(s.startsWith("xxammol2") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Left Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(0, 0);
                }
                if(s.startsWith("xxammor2") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Right Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(0, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 12.6F), shot);
            }
            if(s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.00345F && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0].haveBullets())
            {
                debuggunnery("Armament System: Bomb Payload Detonated..");
                FM.AS.hitTank(shot.initiator, 0, 10);
                FM.AS.hitTank(shot.initiator, 1, 10);
                nextDMGLevels(3, 2, "CF_D0", shot.initiator);
            }
            if(s.startsWith("xxpnm") && getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
            {
                debuggunnery("Pneumo System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 1);
            }
            if(s.startsWith("xxhyd") && getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
            {
                debuggunnery("Hydro System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if(s.startsWith("xxins"))
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            return;
        }
        if(s.startsWith("xcockpit") || s.startsWith("xblister"))
            if(point3d.z > 0.47299999999999998D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            else
            if(point3d.y > 0.0D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
            else
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
        if(s.startsWith("xcf"))
        {
            if(point3d.x < -1.9399999999999999D)
            {
                if(chunkDamageVisible("Tail1") < 3)
                    hitChunk("Tail1", shot);
            } else
            {
                if(point3d.x <= 1.3420000000000001D)
                    if(point3d.z < -0.59099999999999997D || point3d.z > 0.40799999237060547D && point3d.x > 0.0D)
                    {
                        getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F && Math.abs(v1.x) > 0.86599999666213989D)
                            doRicochet(shot);
                    } else
                    {
                        getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F && Math.abs(v1.x) > 0.86599999666213989D)
                            doRicochet(shot);
                    }
                if(chunkDamageVisible("CF") < 3)
                    hitChunk("CF", shot);
            }
        } else
        if(s.startsWith("xoil"))
        {
            if(point3d.z < -0.98099999999999998D)
            {
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                if(shot.power <= 0.0F)
                    doRicochet(shot);
            } else
            if(point3d.x > 0.53700000000000003D || point3d.x < -0.10000000000000001D)
            {
                getEnergyPastArmor(0.20000000298023224D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                if(shot.power <= 0.0F)
                    doRicochetBack(shot);
            } else
            {
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
                if(shot.power <= 0.0F)
                    doRicochet(shot);
            }
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xeng"))
        {
            if(point3d.z > 0.159D)
                getEnergyPastArmor((double)(1.25F * World.Rnd().nextFloat(0.95F, 1.12F)) / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 1.335D && point3d.x < 2.3860000000000001D && point3d.z > -0.059999999999999998D && point3d.z < 0.064000000000000001D)
                getEnergyPastArmor(0.5D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 2.5299999999999998D && point3d.x < 2.992D && point3d.z > -0.23499999999999999D && point3d.z < 0.010999999999999999D)
                getEnergyPastArmor(4.0399999618530273D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 2.5590000000000002D && point3d.z < -0.59499999999999997D)
                getEnergyPastArmor(4.0399999618530273D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 1.849D && point3d.x < 2.2509999999999999D && point3d.z < -0.70999999999999996D)
                getEnergyPastArmor(4.0399999618530273D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 3.0030000000000001D)
                getEnergyPastArmor(World.Rnd().nextFloat(2.3F, 3.2F), shot);
            else
            if(point3d.z < -0.60600000619888306D)
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
            else
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
            if(Math.abs(v1.x) > 0.86599999666213989D && (shot.power <= 0.0F || World.Rnd().nextFloat() < 0.1F))
                doRicochet(shot);
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xtail"))
        {
            {
                hitChunk("Tail1", shot);
            }
        } else
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder") && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
            TailRotorDamage();
        } else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel"))
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner"))
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
            {
                debuggunnery("Hydro System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if((s.endsWith("2a") || s.endsWith("2b")) && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
            {
                debuggunnery("Undercarriage: Stuck..");
                FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else
        if(s.startsWith("xturret"))
        {
            if(getEnergyPastArmor(0.25F, shot) > 0.0F)
            {
                debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                FM.AS.setJamBullets(10, 0);
                FM.AS.setJamBullets(10, 1);
                getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 26.35F), shot);
            }
        } else
        if(s.startsWith("xhelm"))
        {
            getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 3.56F), shot);
            if(shot.power <= 0.0F)
                doRicochetBack(shot);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int i1;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                i1 = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                i1 = s.charAt(6) - 49;
            } else
            {
                i1 = s.charAt(5) - 49;
            }
            hitFlesh(i1, shot, byte0);
        }
    }


    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            // fall through

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Head2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            bObserverKilled = true;
            // fall through

        default:
            return;
        }
    }

    /*   private void stability() 
    {
        double d = 0.0D;
        Vector3d vector3d = new Vector3d();
        getSpeed(vector3d);
        Point3d point3d = new Point3d();
        pos.getAbs(point3d);
        float f = (float)((double)FM.getAltitude() - World.land().HQ(point3d.x, point3d.y));
        if(f < 10F && FM.getSpeedKMH() < 60F && vector3d.z < -1D)
        {
            vector3d.z *= 0.70000000000000002D;
            setSpeed(vector3d);
        }
        if(((FlightModelMain) (super.FM)).Gears.nOfGearsOnGr > 2)
        {
            vector3d.x *= 0.98999999999999999D;
            vector3d.y *= 0.98999999999999999D;
            setSpeed(vector3d);
        }
        if(FM.getSpeedKMH() > 300F)
        {
            d = (FM.getSpeedKMH() - FM.VmaxFLAPS) / 10F;
            if(d < 0.0D)
                d = 0.0D;
        }
      Point3d point3d1 = new Point3d(0.0D, 0.0D, 0.0D);
      point3d1.x = 0.0D - ((double)(FM.Or.getTangage() / 10F) - (double)FM.CT.getElevator() * 2.5D);
      point3d1.y = 0.0D - ((double)(FM.Or.getKren() / 10F) - (double)FM.CT.getAileron() * 2.5D)/ - d;
      point3d1.z = 2D;
      FM.EI.engines[0].setPropPos(point3d1); 
    } */
    
    
    private void FLIR()
    {
        Vector3d vector3d = new Vector3d();
        List list = Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if(((actor instanceof Aircraft) || (actor instanceof ArtilleryGeneric) || (actor instanceof CarGeneric) || (actor instanceof TankGeneric)) && !(actor instanceof StationaryGeneric) && !(actor instanceof TypeLaserSpotter) && actor.pos.getAbsPoint().distance(pos.getAbsPoint()) < 20000D)
            {
                Point3d point3d = new Point3d();
                Orient orient = new Orient();
                actor.pos.getAbs(point3d, orient);
                l.set(point3d, orient);
                Eff3DActor eff3dactor = Eff3DActor.New(actor, null, new Loc(), 1.0F, "effects/Explodes/Air/Zenitka/Germ_88mm/Glow.eff", 1.0F);
                eff3dactor.postDestroy(Time.current() + 1500L);
                LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
                lightpointactor.light.setColor(1.0F, 0.9F, 0.5F);
                if(actor instanceof Aircraft)
                    lightpointactor.light.setEmit(8F, 50F);
                else
                if(!(actor instanceof ArtilleryGeneric))
                    lightpointactor.light.setEmit(5F, 30F);
                else
                    lightpointactor.light.setEmit(3F, 10F);
                eff3dactor.draw.lightMap().put("light", lightpointactor);
            }
        }

    }
   
    

//TODO: Update Section
    public void update(float f)
    
    
    {
    	laserUpdate();
    	
//    	if(((FlightModelMain) (super.FM)).CT.arrestorControl > 0.0F && !bFLIRlock)
//        {
//            bFLIRlock = true;
//        } else
//        	
//        if(bFLIRlock && (((FlightModelMain) (super.FM)).CT.arrestorControl < 1.0F)) {
//        		bFLIRlock = false;
//    }
    	
    	
//        stability();
    	
    	
//    	if(hover)
//        {
//    		float f1 = super.FM.Or.getKren();
//    		float f2 = super.FM.Or.getTangage();
//            Vector3d vector3d = new Vector3d();
//            getSpeed(vector3d);
//            
//            if(FM.getVertSpeed() < 1F && FM.getVertSpeed() > -1F && FM.getSpeedKMH() < 30F && FM.getSpeedKMH() > 30F) {
//            	vector3d.z = 0.0D;
//            	vector3d.x = 0.0D;
//            	vector3d.y = 0.0D;
//            	setSpeed(vector3d);
//            	
//            	FM.Or.increment(0.0F, 0.0F, 0.0F); 
//            	
//            	super.FM.getW().scale(0.2D); 
//            	
//            }
//            if(FM.getSpeedKMH() < 30F && FM.getSpeedKMH() > 30F && f1 < -10F && f1 < 10F && f2 < -10F && f2 < 10F){
//            	vector3d.x = 0.0D;
//            	vector3d.y = 0.0D;
//            	setSpeed(vector3d);
            	

            	
            	
//            	if(f1 > -20F && f1 < 0F)
//            		super.FM.CT.ElevatorControl = 0.5F;
//            	if(f1 < 20F && f1 > 0F)
//            		super.FM.CT.ElevatorControl = -0.5F;

//            ((Pilot)super.FM).setSpeedMode(7);
//            float f1 = super.FM.Or.getKren();
//            if(f1 > 0.0F)
//                f1 -= 25F;
//            else
//                f1 -= 335F;
//            if(f1 < -180F)
//                f1 += 360F;
//            f1 *= -0.01F;
//            super.FM.CT.AileronControl = f1 * 0.5F;
//            super.FM.CT.ElevatorControl = -0.04F * (super.FM.Or.getTangage() - 1.0F);
//        }

    	
        guidedMissileUtils.update();
        boolean flag = false;
        if(obsMove < obsMoveTot && !bObserverKilled && !((FlightModelMain) (super.FM)).AS.isPilotParatrooper(1))
        {
            if(obsMove < 0.2F || obsMove > obsMoveTot - 0.2F)
                obsMove += 0.29999999999999999D * (double)f;
            else
            if(obsMove < 0.1F || obsMove > obsMoveTot - 0.1F)
                obsMove += 0.15F;
            else
                obsMove += 1.2D * (double)f;
            obsLookAzimuth = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsAzimuthOld, obsAzimuth);
            obsLookElevation = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsElevationOld, obsElevation);
            hierMesh().chunkSetAngles("Head2_D0", 0.0F, obsLookAzimuth, obsLookElevation);
        }
        super.update(f);
        
        
        Pilot pilot = (Pilot)FM;
        if((pilot != null) && !Mission.isNet())
        {
            Actor actor = War.GetNearestEnemy(this, 1, 3000F);
            if(pilot != null && isAlive(actor) && !(actor instanceof BridgeSegment))
            {
                Point3d point3d = new Point3d();
                actor.pos.getAbs(point3d);
                if(pos.getAbsPoint().distance(point3d) < 1700D)
                {
                    point3d.sub(FM.Loc);
                    FM.Or.transformInv(point3d);
                    if(point3d.y < 0.0D)
                    {
                        FM.turret[0].target = actor;
                        FM.turret[0].tMode = 2;
                    }
                }
            } else
            if(actor != null)
            {
                    if(FM.turret[0].target != null && !(FM.turret[0].target instanceof Aircraft) && !isAlive(FM.turret[0].target))
                        FM.turret[0].target = null;

            }
        }
//        float f1 = FM.CT.getAirBrake();
//        f1 = FM.CT.getAileron();
//        if(Math.abs(pictAileron - f1) > 0.01F)
//            pictAileron = f1;
//        f1 = FM.CT.getRudder();
//        if(Math.abs(pictRudder - f1) > 0.01F)
//            pictRudder = f1;
//        f1 = FM.CT.getElevator();
//        if(Math.abs(pictVator - f1) > 0.01F)
//            pictVator = f1;
//        float f2 = FM.EI.getPowerOutput() * Aircraft.cvt(FM.getSpeedKMH(), 0.0F, 40F, 10.0F, 0.0F);
/*        if(FM.getSpeedKMH() < 100F)
        {
            if(FM.Or.getTangage() > 5F)
            {
                FM.getW().scale(Aircraft.cvt(FM.Or.getTangage(), 45F, 90F, 1.0F, 0.1F));
                float f3 = FM.Or.getTangage();
                if(Math.abs(FM.Or.getKren()) > 90F)
                    f3 = 90F + (90F - f3);
                float f4 = f3 - 90F;
                FM.CT.trimElevator = Aircraft.cvt(f4, -20F, 20F, 0.5F, -0.5F);
                f4 = FM.Or.getKren();
                if(Math.abs(f4) > 90F)
                    if(f4 > 0.0F)
                        f4 = 180F - f4;
                    else
                        f4 = -180F - f4;
                FM.CT.trimAileron = Aircraft.cvt(f4, -20F, 20F, 0.5F, -0.5F);
                FM.CT.trimRudder = Aircraft.cvt(f4, -15F, 15F, 0.04F, -0.04F);
            }
        } else
        {
            FM.CT.trimAileron = 0.0F;
            FM.CT.trimElevator = 0.0F;
            FM.CT.trimRudder = 0.0F;
        } */
        
//        FM.Or.increment(f2 * (FM.CT.getRudder() + FM.CT.getTrimRudderControl()), f2 * (FM.CT.getElevator() + FM.CT.getTrimElevatorControl()), f2 * (FM.CT.getAileron() + FM.CT.getTrimAileronControl()));
        if((double)super.FM.getSpeedKMH() >= 290D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            FM.Sq.dragParasiteCx += 0.02F;
        if((double)super.FM.getSpeedKMH() >= 290D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.02F;
        if((double)super.FM.getSpeedKMH() >= 310D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.02F;
        if((double)super.FM.getSpeedKMH() >= 310D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.02F;
        if((double)super.FM.getSpeedKMH() >= 320D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.02F;
        if((double)super.FM.getSpeedKMH() >= 320D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.02F;
        if(super.FM.getAltitude() > 4000F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 0.8F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.z -= 1000D;
        if(super.FM.getAltitude() > 4000F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 0.8F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.z -= 1000D;
        if(super.FM.getAltitude() > 4500F && ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 0.8F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.z -= 1000D;
        if(super.FM.getAltitude() > 4500F && ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput() > 0.8F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.z -= 1000D; 

        if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !(super.FM instanceof Pilot))
        {
        
        float avW = (FM.EI.engines[0].getw() + FM.EI.engines[1].getw())  / 2F;    
        if(avW > 100F)  //By PAL, enough power from 2 engines
        {                                 	
            Vector3f eVect = new Vector3f();
            eVect.x = -(FM.CT.getElevator() + FM.CT.getTrimElevatorControl()); //By PAL, to produce some tendency to advance
            eVect.y = -(FM.CT.getAileron() + FM.CT.getTrimRudderControl());
            eVect.z = 1.5F;
            eVect.normalize();
            FM.EI.engines[0].setVector(eVect);
            FM.EI.engines[1].setVector(eVect);
 //           float sens = 2;/*cvt(FM.getSpeedKMH(), 0.0F, 300F, 0.0F, 4F); */
            FM.Or.increment((FM.CT.getRudder() + FM.CT.getTrimRudderControl()) / 2, FM.CT.getElevator() + FM.CT.getTrimElevatorControl(),
            	FM.CT.getAileron() + FM.CT.getTrimAileronControl()); //By PAL, wrong getTrimElevator               
            super.FM.getW().scale(0.6D);          
        }
        else
        {
        	if(((FlightModelMain) (super.FM)).Gears.nOfGearsOnGr < 2)
            {
        		FM.producedAF.z -= (100D - (double)avW) * 300D + 15000D; //By PAL, moved here, clutch effect
            }
            Vector3d vector3d = new Vector3d();
            getSpeed(vector3d);
            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
            float f1 = (float)((double)FM.getAltitude() - World.land().HQ(point3d.x, point3d.y));
            if(f1 < 10F && FM.getSpeedKMH() < 60F && vector3d.z < -1D)
            {
                vector3d.z *= 0.70000000000000002D;
                setSpeed(vector3d);
            }
        }
        if(this == World.getPlayerAircraft() && FM.turret.length > 0 && FM.AS.astatePilotStates[1] < 90 && FM.turret[0].bIsAIControlled && (FM.getOverload() > 3F || FM.getOverload() < -0.7F))
            Voice.speakRearGunShake();      
//        sirenaWarning();
        float avT = (FM.EI.engines[0].getThrustOutput() + FM.EI.engines[1].getThrustOutput())  / 2F;    
        if(hierMesh().isChunkVisible("Tail1_CAP") || TailRotorDestroyed && ((FlightModelMain) (super.FM)).Gears.nOfGearsOnGr < 2)
        {
        	FM.Or.increment(avT, 0.0F, 0.0F);
            FM.CT.bHasRudderControl = false;
            FM.CT.bHasRudderTrim = false;
        }
        } else {
        	Vector3f eVect = new Vector3f();
            eVect.x = 2.0F; //By PAL, to produce some tendency to advance
            eVect.y = 0.0F;
            eVect.z = 2.0F;
//            eVect.normalize();
            FM.EI.engines[0].setVector(eVect);
            FM.EI.engines[1].setVector(eVect);
            ((FlightModelMain) (super.FM)).CT.FlapsControl = 1.0F;
            FM.producedAF.z += 35000;
            
//            float avT = (FM.EI.engines[0].getThrustOutput() + FM.EI.engines[1].getThrustOutput())  / 2F;
            
//            FM.producedAF.x += avT * 10;
            ((FlightModelMain) (super.FM)).Sq.liftWingLIn = 17.0F;
            ((FlightModelMain) (super.FM)).Sq.liftWingRIn = 17.0F;
            ((FlightModelMain) (super.FM)).Sq.liftWingLMid = 17.0F;
            ((FlightModelMain) (super.FM)).Sq.liftWingRMid = 17.0F;
            ((FlightModelMain) (super.FM)).Sq.liftWingLOut = 17.0F;
            ((FlightModelMain) (super.FM)).Sq.liftWingROut = 17.0F;
            
            ((FlightModelMain) (super.FM)).Sq.squareWing = 102.0F;
            super.FM.getW().scale(0.6D);
            
            ((FlightModelMain) (super.FM)).SensYaw = 0.5F; 
            ((FlightModelMain) (super.FM)).SensPitch = 0.3F;
            ((FlightModelMain) (super.FM)).SensRoll = 0.3F;
            
            
            com.maddox.JGP.Vector3d vector3d = new Vector3d();
        	getSpeed(vector3d);
        	com.maddox.JGP.Point3d point3d1 = new Point3d();
            float alt = (float)(FM.getAltitude() - com.maddox.il2.ai.World.land().HQ(point3d1.x, point3d1.y));        
            
            if(((com.maddox.il2.ai.air.Maneuver)super.FM).get_task() == 7 && alt > 4)
            {
            ((FlightModelMain) (super.FM)).CT.bHasGearControl = false;
        } else {
        	((FlightModelMain) (super.FM)).CT.bHasGearControl = true;
        }
            
            if(!Mission.isNet() && ((com.maddox.il2.ai.air.Maneuver)super.FM).get_task() == 7 && (!super.FM.isPlayers() || !(super.FM instanceof com.maddox.il2.fm.RealFlightModel) || !((com.maddox.il2.fm.RealFlightModel)super.FM).isRealMode()))
            {    	
            	pos.getAbs(point3d1);        
            	if(alt < 9 && vector3d.z < 0)
            		vector3d.z = 0;
            	setSpeed(vector3d);  
            }
            
        }
    }

//    public boolean turretAngles(int i, float af[])
//    {
//        boolean flag = super.turretAngles(i, af);
//        if(af[0] < -60F)
//        {
//            af[0] = -60F;
//            flag = false;
//        } else
//        if(af[0] > 60F)
//        {
//            af[0] = 60F;
//            flag = false;
//        }
//        float f = Math.abs(af[0]);
//        if(af[1] < -80F)
//        {
//            af[1] = -80F;
//            flag = false;
//        }
//        if(af[1] > 20F)
//        {
//            af[1] = 20F;
//            flag = false;
//        }
//        if(!flag)
//            return false;
//        float f1 = af[1];
//        if(f < 1.2F && f1 < 13.3F)
//            return false;
//        return f1 >= -3.1F || f1 <= -4.6F;
//    }
    
    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        default:
            break;

        case 0: // '\0'
        	if(f < -80F)
            {
                f = -80F;
                flag = false;
            }
            if(f > 20F)
            {
                f = 20F;
                flag = false;
            }
            if(f1 < -60F)
            {
                f1 = -45F;
                flag = false;
            }
            if(f1 > 60F)
            {
                f1 = 60F;
                flag = false;
            }
            break;

        case 1: // '\001'
        	if(f < -80F)
            {
                f = -80F;
                flag = false;
            }
            if(f > 20F)
            {
                f = 20F;
                flag = false;
            }
            if(f1 < -60F)
            {
                f1 = -45F;
                flag = false;
            }
            if(f1 > 60F)
            {
                f1 = 60F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    private long tX4Prev;
    private boolean backfire;
    private ArrayList backfireList;
    
    public static Orient LaserOr = new Orient();
    
    public boolean laserOn;
    public boolean laserLock;
    
    private Hook[] LaserHook = { null, null, null, null };
    
    private static Loc LaserLoc1 = new Loc();
    private static Point3d LaserP1 = new Point3d();
    private static Point3d LaserP2 = new Point3d();
    private static Point3d LaserPL = new Point3d();

    public boolean APmode1;
    public boolean APmode2;
    public boolean APmode3;
    
    private SoundFX fxSirenaLaunch;
    private Sample smplSirenaLaunch;
    private boolean sirenaLaunchSoundPlaying;
    private boolean bRadarWarningLaunch;
    
    public boolean FLIR;
    
	public BulletEmitter[][]	Weapons;
    public int rocketHookSelected 	= 2;
    private static Loc l = new Loc();
    private int obsLookTime;
    private float obsLookAzimuth;
    private float obsLookElevation;
    private float obsAzimuth;
    private float obsElevation;
    private float obsAzimuthOld;
    private float obsElevationOld;
    private float obsMove;
    private float obsMoveTot;
    boolean bObserverKilled;
    public static boolean bChangedPit = false;
    public Loc suka;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int rotorrpm;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean bRadarWarning;
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
    private SoundFX fxSirena;
    private Sample smplSirena;
    private boolean sirenaSoundPlaying;
    private float collV; //By PAL
    private float lcollV; //By PAL   
    private boolean TailRotorDestroyed;
	
    public static Actor lock;
    
    private boolean hover;
    
//    public boolean bFLIRlock;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.CopyOfMi24V.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mi-24V");
        Property.set(class1, "meshName", "3DO/Plane/Mi-24V/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1950F);
        Property.set(class1, "yearExpired", 1960.5F);
        Property.set(class1, "FlightModel", "FlightModels/Mi-24V.fmd:HIND");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitMi24.class, 
            com.maddox.il2.objects.air.CockpitMi24_GUNNER.class, com.maddox.il2.objects.air.CockpitMi24_FLIR.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 9, 9, 2, 2, 2, 2,
            7, 7, 9, 9, 2, 2, 2, 2, 2, 2,
            2, 2, 9, 9, 9, 9, 3, 3, 3, 3,
            9, 9, 0, 0, 9, 9, 3, 3, 3, 3,
            3, 3, 3, 3, 9, 9, 2, 2, 2, 2
        });
        
        
//TODO: Hooks
        
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_BombSpawn05", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04",
            "_Flare01", "_Flare02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", 
            "_ExternalRock11", "_ExternalRock12", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04",
            "_ExternalDev11", "_ExternalDev12", "_BombSpawn01", "_BombSpawn02", "_ExternalDev13", "_ExternalDev14", "_ExternalRock13", "_ExternalRock13", "_ExternalRock14", "_ExternalRock14",
            "_ExternalRock15", "_ExternalRock15", "_ExternalRock16", "_ExternalRock16", "_ExternalDev15", "_ExternalDev16", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07"
          
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 50;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "Default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "64xS-5M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "128xS-5M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "64xS-5M+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "128xS-5M+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "64xS-5K+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5K", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5K", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "128xS-5K+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5K", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS5K", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5K", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5K", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "40xS-8";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "80xS-8";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "40xS-8+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "80xS-8+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "40xS-8DM";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8DM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8DM", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "80xS-8DM";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8DM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8DM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8DM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8DM", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "40xS-8DM+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8DM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8DM", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "80xS-8DM+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8DM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8DM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8DM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8DM", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "40xS-8KOM";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "80xS-8KOM";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "40xS-8KOM+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "80xS-8KOM+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            
//TODO: R-60 loaoduts
            
            s = "64xS-5M+4xR-60M+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            
            a_lweaponslot[34] = new Aircraft._WeaponSlot(9, "APU60L", 35);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "APU60R", 35);
            
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunR60M", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunR60M", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "40xS-8+4xR-60M+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            
            
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(9, "APU60L", 35);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "APU60R", 35);
            
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunR60M", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunR60M", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunR60M", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunR60M", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "2xFAB-250M46";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB-250M46";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRBK-250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK-250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "64xS-5M+2xFAB-250M46";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "64xS-5M+2xFAB-250M46+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "64xS-5M+2xRBK-250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "64xS-5M+2xRBK-250+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);

            s = "2xPTB";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "64xS-5M+2xPTB";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "2xPTB+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "64xS-5M+2xPTB+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            
//TODO: UPK-23 loadouts          
            
            s = "64xS-5M+2xUPK-23-250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "PylonUPK23", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "PylonUPK23", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(1, "MGunGSh23Ls", 125);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(1, "MGunGSh23Ls", 125);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(1, "MGunGSh23Ls", 125);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(1, "MGunGSh23Ls", 125);
            
            
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            s = "40xS-8+2xUPK-23-250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "PylonUPK23", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "PylonUPK23", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(1, "MGunGSh23Ls", 125);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(1, "MGunGSh23Ls", 125);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(1, "MGunGSh23Ls", 125);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(1, "MGunGSh23Ls", 125);
            
            
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            
            
            
            
            
            s = "64xS-5M+VDV Squad";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "128xS-5M+VDV Squad";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);

            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "64xS-5M+4xSturm-V+VDV Squad";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);

            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "128xS-5M+4xSturm-V+VDV Squad";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);

            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "64xS-5K+4xSturm-V+VDV Squad";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5K", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5K", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);

            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "128xS-5K+4xSturm-V+VDV Squad";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5K", 32);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS5K", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5K", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5K", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);

            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "40xS-8+VDV Squad";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);

            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "80xS-8+VDV Squad";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);

            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "40xS-8+4xSturm-V+VDV Squad";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);

            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "80xS-8+4xSturm-V+VDV Squad";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);

            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunVDVAKS", 4);
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);

            
            
            
            

            s = "None";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}
