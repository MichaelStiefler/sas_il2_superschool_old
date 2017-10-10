// Decompiled by DJ v3.12.12.101 Copyright 2016 Atanas Neshkov  Date: 05.10.2017 20:29:34
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Mi24V.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.*;
import com.maddox.sound.*;
import com.maddox.util.HashMapExt;
import com.maddox.util.HashMapInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mi24V extends Scheme2
    implements TypeHelicopter, TypeStormovik, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeLaserSpotter
{

    public Mi24V()
    {
    	
    	 forceTrim_x = 0.0D;
         forceTrim_y = 0.0D;
         forceTrim_z = 0.0D;
         getTrim = false;

         curAngleRotor = 0.0F;
         lastTimeFan = Time.current();
         oMainRotor = new Orient();
         vThrust = new Vector3f();
         iMainTorque = 0.0F;
         MainRotorStatus = 1.0F;
         TailRotorStatus = 1.0F;
    	
        rocketHookSelected = 2;
        suka = new Loc();
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
        rotorRPM = 0.0F;
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
        curAngleRotor = 0.0F;
        lastTimeFan = Time.current();
        oMainRotor = new Orient();
        vThrust = new Vector3f();
        iMainTorque = 0.0F;
        MainRotorStatus = 1.0F;
        TailRotorStatus = 1.0F;
        
        laserOn = false;
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
    	LaserLoc1.set(6000.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
    	this.LaserHook[1].computePos(this, _tmpLoc, LaserLoc1);
    	LaserLoc1.get(LaserP2);
    	Engine.land(); 
    	if (Landscape.rayHitHQ(LaserP1, LaserP2, LaserPL)) 
    	{
    		LaserPL.z -= 0.95D;
    		LaserP2.interpolate(LaserP1, LaserPL, 1.0F);
//    		this.Laser[1].setPos(LaserP2);
    		
    		Mi24V.spot.set(LaserP2);
    		
//    		Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(LaserP2.x, LaserP2.y, LaserP2.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
    	}
    }
    }
    
//    public void auxPressed(int i)
//    {
//    	super.auxPressed(i);
//    	
//    	if(i == 20){
//    		if(!APmode1) {
//    			APmode1 = true;
//    			HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude ON");
//            	((FlightModelMain) (super.FM)).AP.setStabAltitude(1000);
//    		} else  if(APmode1) {
//        			APmode1 = false;
//        			HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude OFF");
//                	((FlightModelMain) (super.FM)).AP.setStabAltitude(false);
//    		}
//    	}
//    	
//    	if(i == 21){
//    		if(!APmode2) {
//    			APmode2 = true;
//    			HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction ON");
//    			((FlightModelMain) (super.FM)).AP.setStabDirection(true);
//    			((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
//    		} else  if(APmode2) {
//        			APmode2 = false;
//        			HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction OFF");
//        			((FlightModelMain) (super.FM)).AP.setStabDirection(false);
//        			((FlightModelMain) (super.FM)).CT.bHasRudderControl = true;
//    		}
//    	}
//    	
//    	if(i == 22){
//    		if(!APmode3) {
//    			APmode3 = true;
//    			HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route ON");
//    			((FlightModelMain) (super.FM)).AP.setWayPoint(true);
//    		} else  if(APmode3) {
//        			APmode3 = false;
//        			HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route OFF");
//        			((FlightModelMain) (super.FM)).AP.setWayPoint(false);
//        			((FlightModelMain) (super.FM)).CT.AileronControl = 0.0F;
//        			((FlightModelMain) (super.FM)).CT.ElevatorControl = 0.0F;
//        			((FlightModelMain) (super.FM)).CT.RudderControl = 0.0F;
//    		}
//    	}
//    	
//    	if(i == 23){
//    		((FlightModelMain) (super.FM)).CT.AileronControl = 0.0F;
//			((FlightModelMain) (super.FM)).CT.ElevatorControl = 0.0F;
//			((FlightModelMain) (super.FM)).CT.RudderControl = 0.0F;
//			((FlightModelMain) (super.FM)).AP.setWayPoint(false);
//			((FlightModelMain) (super.FM)).AP.setStabDirection(false);
//			((FlightModelMain) (super.FM)).AP.setStabAltitude(false);
//			HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: All Off");
//    	}
//    	
//    	if(i == 24){	
//			if(!laserOn) {
//    			laserOn = true;
//    			laserLock = false;
//    			HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: On");
//    			
//    		} else  if(laserOn) {
//        			laserOn = false;
//        			laserLock = false;
//        			HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: Off");
//    		}
//    	}
//    		
//    	
//    }

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

    private boolean sirenaWarning()
    {
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j += 360;
        Actor actor = War.getNearestEnemy(this, 4000F);
        if((actor instanceof Aircraft) && actor.getArmy() != World.getPlayerArmy() && (actor instanceof TypeFighterAceMaker) && (actor instanceof TypeRadarGunsight) && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
        {
            super.pos.getAbs(point3d);
            double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (actor.pos.getAbsPoint())).x;
            double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).y;
            double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).z;
            new String();
            new String();
            double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
            new String();
            double d7 = d3 - d;
            double d8 = d4 - d1;
            float f = 57.32484F * (float)Math.atan2(d8, -d7);
            int i1 = (int)(Math.floor((int)f) - 90D);
            if(i1 < 0)
                i1 += 360;
            int j1 = i1 - i;
            double d9 = d - d3;
            double d10 = d1 - d4;
            double d11 = Math.sqrt(d6 * d6);
            int k1 = (int)(Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
            float f1 = 57.32484F * (float)Math.atan2(k1, d11);
            int l1 = (int)(Math.floor((int)f1) - 90D);
            if(l1 < 0)
                l1 += 360;
            int i2 = l1 - j;
            int j2 = (int)(Math.ceil(((double)k1 * 3.2810000000000001D) / 100D) * 100D);
            if(j2 >= 5280)
                j2 = (int)Math.floor(j2 / 5280);
            bRadarWarning = (double)k1 <= 3000D && (double)k1 >= 50D && i2 >= 195 && i2 <= 345 && Math.sqrt(j1 * j1) >= 120D;
            playSirenaWarning(bRadarWarning);
        } else
        {
            bRadarWarning = false;
            playSirenaWarning(bRadarWarning);
        }
        return true;
    }

    public void playSirenaWarning(boolean isThreatened)
    {
        if(isThreatened && !sirenaSoundPlaying)
        {
            fxSirena.play(smplSirena);
            sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-2: Enemy on Six!");
        } else
        if(!isThreatened && sirenaSoundPlaying)
        {
            fxSirena.cancel();
            sirenaSoundPlaying = false;
        }
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        ((FlightModelMain) (super.FM)).CT.bHasBrakeControl = true;
        ((FlightModelMain) (super.FM)).CT.bHasAirBrakeControl = false;
        FM.turret[1].bIsAIControlled = false;
        guidedMissileUtils.onAircraftLoaded();
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(((FlightModelMain) (super.FM)).AS.bNavLightsOn)
        {
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            super.pos.getAbs(point3d, orient);
            l.set(point3d, orient);
            Eff3DActor eff3dactor = Eff3DActor.New(this, findHook("_RedLight"), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", 1.0F);
            eff3dactor.postDestroy(Time.current() + 500L);
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 0.3F, 0.3F);
            lightpointactor.light.setEmit(1.0F, 3F);
            ((Actor) (eff3dactor)).draw.lightMap().put("light", lightpointactor);
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

    protected void moveElevator(float f1)
    {
    }

    protected void moveAileron(float f1)
    {
    }

    protected void moveFlap(float f)
    {
    }

    protected void moveRudder(float f)
    {
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f_1_ = Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, -90F);
        float f_2_ = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, -85F);
        float f_3_ = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, 85F);
        float f_4_ = f <= 0.5F ? Aircraft.cvt(f, 0.1F, 0.5F, 0.0F, 70F) : Aircraft.cvt(f, 0.8F, 1.0F, 70F, 0.0F);
        float f_5_ = f <= 0.5F ? Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, -70F) : Aircraft.cvt(f, 0.8F, 1.0F, -70F, 0.0F);
        float f_6_ = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -90F);
        float f_7_ = Aircraft.cvt(f, 0.1F, 0.8F, 0.0F, 90F);
        float f_8_ = Aircraft.cvt(f, 0.2F, 0.9F, 0.0F, -90F);
        float f_9_ = Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, -140F);
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
        float f_1_ = Aircraft.cvt(1.0F, 0.1F, 0.9F, 0.0F, -90F);
        if(((FlightModelMain) (super.FM)).CT.GearControl > 0.1F)
        {
            if(f < -80F)
                hierMesh().chunkSetAngles("GearC2_D0", -80F, 0.0F, f_1_);
            else
            if(f > 80F)
                hierMesh().chunkSetAngles("GearC2_D0", 80F, 0.0F, f_1_);
            else
                hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, f_1_);
        } else
        {
            hierMesh().chunkSetAngles("GearC2_D0", 0.0F, 0.0F, f_1_);
        }
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
        float tRPM = Math.max(((FlightModelMain) (super.FM)).EI.engines[0].getRPM(), ((FlightModelMain) (super.FM)).EI.engines[1].getRPM());
        tRPM *= 0.061F;
        float aThrottle = (((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle() + ((FlightModelMain) (super.FM)).EI.engines[1].getControlThrottle()) / 2.0F;
        float aThrust = (((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() + ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput()) / 2.0F;
        if(aThrust * aThrust < 0.1F)
            tRPM = 0.0F;
        rotorRPM += (tRPM - rotorRPM) * 0.0001F;
        if(aThrust * aThrottle > 0.25F)
        {
            hierMesh().chunkVisible("Prop1_D0", false);
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot1_D0", true);
            hierMesh().chunkVisible("PropRot2_D0", true);
        } else
        {
            hierMesh().chunkVisible("Prop1_D0", true);
            hierMesh().chunkVisible("Prop2_D0", true);
            hierMesh().chunkVisible("PropRot1_D0", false);
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
        long curTime = Time.current();
        diffAngleRotor = (6F * rotorRPM * (float)(curTime - lastTimeFan)) / 1000F;
        curAngleRotor += diffAngleRotor;
        lastTimeFan = curTime;
        hierMesh().chunkSetAngles("Prop1_D0", -curAngleRotor % 360F, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Prop2_D0", 0.0F, 0.0F, -(curAngleRotor * 5.86F) % 360F);
    }

    private void tiltRotor(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -((FlightModelMain) (super.FM)).CT.getElevator() / 10F);
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -((FlightModelMain) (super.FM)).CT.getAileron() / 10F, 0.0F);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("GearL22_D0", 0.0F, Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, -10F), 0.0F);
        hierMesh().chunkSetAngles("GearR22_D0", 0.0F, Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[1], 0.0F, 0.32F, 0.0F, 10F), 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        boolean flag = false;
        boolean flag1 = this instanceof Mi24V;
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
                        getEnergyPastArmor(7.0700000000000003D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot);
                        shot.powerType = 0;
                        break;

                    case 2: // '\002'
                    case 3: // '\003'
                        getEnergyPastArmor(5.0499999999999998D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 0.0001D), shot);
                        shot.powerType = 0;
                        if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999999999999D)
                            doRicochet(shot);
                        break;

                    case 4: // '\004'
                        if(((Tuple3d) (point3d)).x > -1.3500000000000001D)
                        {
                            getEnergyPastArmor(5.0499999999999998D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
                            shot.powerType = 0;
                            if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999999999999D)
                                doRicochet(shot);
                        } else
                        {
                            getEnergyPastArmor(5.0499999999999998D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
                        }
                        break;

                    case 5: // '\005'
                    case 6: // '\006'
                        getEnergyPastArmor(20.199999999999999D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
                        if(shot.power > 0.0F)
                            break;
                        if(Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999999999999D)
                            doRicochet(shot);
                        else
                            doRicochetBack(shot);
                        break;

                    case 7: // '\007'
                        getEnergyPastArmor(20.200000762939453D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot);
                        if(shot.power <= 0.0F)
                            doRicochetBack(shot);
                        break;
                    }
                }
                if(s.startsWith("xxarmorc1"))
                    getEnergyPastArmor(7.0700000000000003D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot);
                if(s.startsWith("xxarmort1"))
                    getEnergyPastArmor(6.0599999999999996D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot);
                return;
            }
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 1 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.10000000000000001D && getEnergyPastArmor(3.3999999999999999D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot) > 0.0F)
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
                    if(chunkDamageVisible("WingLMid") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.12D && getEnergyPastArmor(6.96D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot) > 0.0F)
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
                    if(chunkDamageVisible("WingRMid") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.12D && getEnergyPastArmor(6.96D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot) > 0.0F)
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
                    if(chunkDamageVisible("WingLOut") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.12D && getEnergyPastArmor(6.96D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot) > 0.0F)
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
                    if(chunkDamageVisible("WingROut") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.12D && getEnergyPastArmor(6.96D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(6.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(6.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.86F / (float)Math.sqrt(((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y + ((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
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
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        } else
                        {
                            debuggunnery("Engine Module: Prop Governor Hit, Oil Pipes Damaged..");
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        }
                } else
                if(s.endsWith("gear"))
                {
                    if(getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Engine Module: Reductor Hit, Bullet Jams Reductor Gear..");
                        ((FlightModelMain) (super.FM)).EI.engines[0].setEngineStuck(shot.initiator);
                    }
                } else
                if(s.endsWith("supc"))
                {
                    if(getEnergyPastArmor(0.01F, shot) > 0.0F)
                    {
                        debuggunnery("Engine Module: Supercharger Disabled..");
                        ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                    }
                } else
                if(s.endsWith("feed"))
                {
                    if(getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            ((FlightModelMain) (super.FM)).EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if(World.Rnd().nextFloat() < 0.05F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            ((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else
                if(s.endsWith("fue1"))
                {
                    if(getEnergyPastArmor(0.89F, shot) > 0.0F)
                    {
                        debuggunnery("Engine Module: Fuel Feed Line Pierced, Engine Fires..");
                        ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 100);
                    }
                } else
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(2.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 175000F)
                        {
                            debuggunnery("Engine Module: Crank Case Hit, Bullet Jams Ball Bearings..");
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                        {
                            debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + ((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() + "..");
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    ((FlightModelMain) (super.FM)).EI.engines[0].setReadyness(shot.initiator, ((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + ((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() + "..");
                    getEnergyPastArmor(22.5F, shot);
                } else
                if(s.endsWith("cyl1"))
                {
                    if(getEnergyPastArmor(1.3F, shot) > 0.0F && World.Rnd().nextFloat() < ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersRatio() * 1.75F)
                    {
                        ((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                        debuggunnery("Engine Module: Cylinders Assembly Hit, " + ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[0].getCylinders() + " Operating..");
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                        {
                            debuggunnery("Engine Module: Cylinders Assembly Hit, Engine Fires..");
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 3);
                        }
                        if(World.Rnd().nextFloat() < 0.01F)
                        {
                            debuggunnery("Engine Module: Cylinders Assembly Hit, Bullet Jams Piston Head..");
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
                        }
                        getEnergyPastArmor(22.5F, shot);
                    }
                    if(Math.abs(((Tuple3d) (point3d)).y) < 0.13800000000000001D && getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            ((FlightModelMain) (super.FM)).EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if(World.Rnd().nextFloat() < 0.05F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            ((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else
                if(s.startsWith("xxeng1mag"))
                {
                    int k = s.charAt(9) - 49;
                    debuggunnery("Engine Module: Magneto " + k + " Hit, Magneto " + k + " Disabled..");
                    ((FlightModelMain) (super.FM)).EI.engines[0].setMagnetoKnockOut(shot.initiator, k);
                } else
                if(s.startsWith("xxeng1oil") && getEnergyPastArmor(0.5F, shot) > 0.0F)
                {
                    debuggunnery("Engine Module: Oil Radiator Hit, Oil Radiator Pierced..");
                    ((FlightModelMain) (super.FM)).AS.hitOil(shot.initiator, 0);
                }
            }
            if(s.startsWith("xxw1"))
                if(((FlightModelMain) (super.FM)).AS.astateEngineStates[0] == 0)
                {
                    debuggunnery("Engine Module: Water Radiator Pierced..");
                    ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
                    ((FlightModelMain) (super.FM)).AS.doSetEngineState(shot.initiator, 0, 1);
                } else
                if(((FlightModelMain) (super.FM)).AS.astateEngineStates[0] == 1)
                {
                    debuggunnery("Engine Module: Water Radiator Pierced..");
                    ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
                    ((FlightModelMain) (super.FM)).AS.doSetEngineState(shot.initiator, 0, 2);
                }
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(((FlightModelMain) (super.FM)).AS.astateTankStates[l] == 0)
                    {
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced..");
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 2, 1);
                        ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, l, 1);
                    } else
                    if(((FlightModelMain) (super.FM)).AS.astateTankStates[l] == 1)
                    {
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced..");
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 2, 1);
                        ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, l, 2);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 2, 2);
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced, State Shifted..");
                    }
                }
            }
            if(s.startsWith("xxmgun"))
            {
                if(s.endsWith("01"))
                {
                    debuggunnery("Armament System: Left Machine Gun: Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 0);
                }
                if(s.endsWith("02"))
                {
                    debuggunnery("Armament System: Right Machine Gun: Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
            }
            if(s.startsWith("xxcannon"))
            {
                if(s.endsWith("01") && getEnergyPastArmor(0.25F, shot) > 0.0F)
                {
                    debuggunnery("Armament System: Left Cannon: Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 0);
                }
                if(s.endsWith("02") && getEnergyPastArmor(0.25F, shot) > 0.0F)
                {
                    debuggunnery("Armament System: Right Cannon: Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if(s.startsWith("xxammo"))
            {
                if(s.startsWith("xxammol1") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Left Cannon: Chain Feed Jammed, Gun Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 0);
                }
                if(s.startsWith("xxammor1") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Right Cannon: Chain Feed Jammed, Gun Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 1);
                }
                if(s.startsWith("xxammol2") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Left Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 0);
                }
                if(s.startsWith("xxammor2") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Right Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 12.6F), shot);
            }
            if(s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.00345F && ((FlightModelMain) (super.FM)).CT.Weapons[3] != null && ((FlightModelMain) (super.FM)).CT.Weapons[3][0].haveBullets())
            {
                debuggunnery("Armament System: Bomb Payload Detonated..");
                ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 0, 10);
                ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 1, 10);
                nextDMGLevels(3, 2, "CF_D0", shot.initiator);
            }
            if(s.startsWith("xxpnm") && getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
            {
                debuggunnery("Pneumo System: Disabled..");
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 1);
            }
            if(s.startsWith("xxhyd") && getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
            {
                debuggunnery("Hydro System: Disabled..");
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 0);
            }
            if(s.startsWith("xxins"))
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x40);
            return;
        }
        if(s.startsWith("xcockpit") || s.startsWith("xblister"))
            if(((Tuple3d) (point3d)).z > 0.47299999999999998D)
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
            else
            if(((Tuple3d) (point3d)).y > 0.0D)
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 4);
            else
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x10);
        if(s.startsWith("xcf"))
        {
            if(((Tuple3d) (point3d)).x < -1.9399999999999999D)
            {
                if(chunkDamageVisible("Tail1") < 3)
                    hitChunk("Tail1", shot);
            } else
            {
                if(((Tuple3d) (point3d)).x <= 1.3420000000000001D)
                    if(((Tuple3d) (point3d)).z < -0.59099999999999997D || ((Tuple3d) (point3d)).z > 0.40799999999999997D && ((Tuple3d) (point3d)).x > 0.0D)
                    {
                        getEnergyPastArmor(5.0499999999999998D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
                        if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999999999999D)
                            doRicochet(shot);
                    } else
                    {
                        getEnergyPastArmor(5.0499999999999998D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 0.0001D), shot);
                        if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999999999999D)
                            doRicochet(shot);
                    }
                if(chunkDamageVisible("CF") < 3)
                    hitChunk("CF", shot);
            }
        } else
        if(s.startsWith("xoil"))
        {
            if(((Tuple3d) (point3d)).z < -0.98099999999999998D)
            {
                getEnergyPastArmor(5.0499999999999998D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
                if(shot.power <= 0.0F)
                    doRicochet(shot);
            } else
            if(((Tuple3d) (point3d)).x > 0.53700000000000003D || ((Tuple3d) (point3d)).x < -0.10000000000000001D)
            {
                getEnergyPastArmor(0.20000000000000001D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot);
                if(shot.power <= 0.0F)
                    doRicochetBack(shot);
            } else
            {
                getEnergyPastArmor(5.0499999999999998D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 0.0001D), shot);
                if(shot.power <= 0.0F)
                    doRicochet(shot);
            }
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xeng"))
        {
            if(((Tuple3d) (point3d)).z > 0.159D)
                getEnergyPastArmor((double)(1.25F * World.Rnd().nextFloat(0.95F, 1.12F)) / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
            else
            if(((Tuple3d) (point3d)).x > 1.335D && ((Tuple3d) (point3d)).x < 2.3860000000000001D && ((Tuple3d) (point3d)).z > -0.059999999999999998D && ((Tuple3d) (point3d)).z < 0.064000000000000001D)
                getEnergyPastArmor(0.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 0.0001D), shot);
            else
            if(((Tuple3d) (point3d)).x > 2.2999999999999998D && ((Tuple3d) (point3d)).x < 2.992D && ((Tuple3d) (point3d)).z > -0.23499999999999999D && ((Tuple3d) (point3d)).z < 0.010999999999999999D)
                getEnergyPastArmor(4.04D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 0.0001D), shot);
            else
            if(((Tuple3d) (point3d)).x > 2.5590000000000002D && ((Tuple3d) (point3d)).z < -0.59499999999999997D)
                getEnergyPastArmor(4.04D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
            else
            if(((Tuple3d) (point3d)).x > 1.849D && ((Tuple3d) (point3d)).x < 2.2509999999999999D && ((Tuple3d) (point3d)).z < -0.70999999999999996D)
                getEnergyPastArmor(4.04D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
            else
            if(((Tuple3d) (point3d)).x > 3.0030000000000001D)
                getEnergyPastArmor(World.Rnd().nextFloat(2.3F, 3.2F), shot);
            else
            if(((Tuple3d) (point3d)).z < -0.60600000619888306D)
                getEnergyPastArmor(5.0499999999999998D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
            else
                getEnergyPastArmor(5.0499999999999998D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 0.0001D), shot);
            if(Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999999999999D && (shot.power <= 0.0F || World.Rnd().nextFloat() < 0.1F))
                doRicochet(shot);
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xtail"))
            hitChunk("Tail1", shot);
        else
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
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
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 0);
            }
            if((s.endsWith("2a") || s.endsWith("2b")) && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
            {
                debuggunnery("Undercarriage: Stuck..");
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
            }
        } else
        if(s.startsWith("xturret"))
        {
            if(getEnergyPastArmor(0.25F, shot) > 0.0F)
            {
                debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                ((FlightModelMain) (super.FM)).AS.setJamBullets(10, 0);
                ((FlightModelMain) (super.FM)).AS.setJamBullets(10, 1);
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

    public void update(float f)
    {
    	laserUpdate();
        tiltRotor(f);
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
        
        
        super.update(f);
        
        if((double)super.FM.getSpeedKMH() >= 280D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            FM.Sq.dragParasiteCx += 0.03F;
        if((double)super.FM.getSpeedKMH() >= 280D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.03F;
        if((double)super.FM.getSpeedKMH() >= 290D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.03F;
        if((double)super.FM.getSpeedKMH() >= 290D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.03F;
        if((double)super.FM.getSpeedKMH() >= 310D && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.03F;
        if((double)super.FM.getSpeedKMH() >= 310D && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.03F;
//        
//        if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !(super.FM instanceof Pilot))
//        {
//        	
//        float kren = ((FlightModelMain) (super.FM)).CT.getAileron() + ((FlightModelMain) (super.FM)).CT.getTrimAileronControl();
//        kren *= 35F;
//        float tang = ((FlightModelMain) (super.FM)).CT.getElevator() + ((FlightModelMain) (super.FM)).CT.getTrimElevatorControl();
//        tang -= 0.05F;
//        tang *= tang >= 0.0F ? 35F : 50F;
//        vThrust.set(0.0F, 0.0F, 1.0F);
//        oMainRotor.set(0.0F, tang, kren);
//        oMainRotor.transform(vThrust);
//        ((FlightModelMain) (super.FM)).EI.engines[0].setVector(vThrust);
//        ((FlightModelMain) (super.FM)).EI.engines[1].setVector(vThrust);
//        float aThrottle = (((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle() + ((FlightModelMain) (super.FM)).EI.engines[1].getControlThrottle()) / 2.0F;
//        float aThrust = (((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() + ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput()) / 2.0F;
//        super.FM.getW().scale(0.75F * aThrottle);
//        float MainTorque = ((FlightModelMain) (super.FM)).CT.getRudder() + ((FlightModelMain) (super.FM)).CT.getTrimRudderControl();
//        MainTorque *= ((FlightModelMain) (super.FM)).Gears.nOfGearsOnGr > 1 ? 0.3F : 1.5F;
//        MainTorque *= aThrottle * aThrust;
//        iMainTorque += (MainTorque - iMainTorque) * 0.05F;
//        ((FlightModelMain) (super.FM)).Or.increment(iMainTorque, 0.0F, 0.0F);
//        float fStab = super.FM.getAltitude() <= 2300F ? ((FlightModelMain) (super.FM)).CT.getAirBrake() : 0.0F;
//        if(((FlightModelMain) (super.FM)).CT.getAirBrake() > 0.5F)
//            super.FM.setGCenter(0.1F - fStab * 25F);
//        if(this == World.getPlayerAircraft() && super.FM.turret.length > 0 && ((FlightModelMain) (super.FM)).AS.astatePilotStates[1] < 90 && super.FM.turret[0].bIsAIControlled && (super.FM.getOverload() > 3F || super.FM.getOverload() < -1.5F))
//            Voice.speakRearGunShake();
//        sirenaWarning();
//        if(hierMesh().isChunkVisible("Tail1_CAP") || ((FlightModelMain) (super.FM)).CT.getAirBrake() > 0.5F)
//            ((FlightModelMain) (super.FM)).Or.increment(aThrottle * aThrust * 5F, 0.0F, 0.0F);
//        } else {
//        	Vector3f eVect = new Vector3f();
//            eVect.x = 5.0F; //By PAL, to produce some tendency to advance
//            eVect.y = 0.0F;
//            eVect.z = 0.0F;
//            eVect.normalize();
//            FM.EI.engines[0].setVector(eVect);
//            FM.EI.engines[1].setVector(eVect);
////            ((FlightModelMain) (super.FM)).CT.FlapsControl = 1.0F;
////            FM.producedAF.z += 135000;
//            
////            float avT = (FM.EI.engines[0].getThrustOutput() + FM.EI.engines[1].getThrustOutput())  / 2F;
//            
////            FM.producedAF.x += avT * 10;
//            ((FlightModelMain) (super.FM)).Sq.liftWingLIn = 17.0F;
//            ((FlightModelMain) (super.FM)).Sq.liftWingRIn = 17.0F;
//            ((FlightModelMain) (super.FM)).Sq.liftWingLMid = 17.0F;
//            ((FlightModelMain) (super.FM)).Sq.liftWingRMid = 17.0F;
//            ((FlightModelMain) (super.FM)).Sq.liftWingLOut = 17.0F;
//            ((FlightModelMain) (super.FM)).Sq.liftWingROut = 17.0F;
//            
//            ((FlightModelMain) (super.FM)).Sq.squareWing = 102.0F;
////            super.FM.getW().scale(0.6D);
//            
//            ((FlightModelMain) (super.FM)).SensYaw = 0.5F; 
//            ((FlightModelMain) (super.FM)).SensPitch = 0.3F;
//            ((FlightModelMain) (super.FM)).SensRoll = 0.3F;
//                        
//            com.maddox.JGP.Vector3d vector3d = new Vector3d();
//        	getSpeed(vector3d);
//        	com.maddox.JGP.Point3d point3d1 = new Point3d();
//            float alt = (float)(FM.getAltitude() - com.maddox.il2.ai.World.land().HQ(point3d1.x, point3d1.y));        
//            
//            if(((com.maddox.il2.ai.air.Maneuver)super.FM).get_task() == 7 && alt > 4)
//            {
//            ((FlightModelMain) (super.FM)).CT.bHasGearControl = false;
//        } else {
//        	((FlightModelMain) (super.FM)).CT.bHasGearControl = true;
//        }
//            
//            if(!Mission.isNet() && ((com.maddox.il2.ai.air.Maneuver)super.FM).get_task() == 7 && (!super.FM.isPlayers() || !(super.FM instanceof com.maddox.il2.fm.RealFlightModel) || !((com.maddox.il2.fm.RealFlightModel)super.FM).isRealMode()))
//            {    	
//            	pos.getAbs(point3d1);        
//            	if(alt < 10 && vector3d.z < 0)
//            		vector3d.z = 0;
//            	setSpeed(vector3d);  
//            }
//            
//        }
        
        
        tiltRotor(f);
        guidedMissileUtils.update();
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
        
        
        
        
        double kren = ((FlightModelMain) (super.FM)).Or.getKren();
        double tang = ((FlightModelMain) (super.FM)).Or.getTangage();
        float aile = ((FlightModelMain) (super.FM)).CT.getAileron() + (float)forceTrim_x;
        if(aile > 1.0F)
            aile = 1.0F;
        if(aile < -1F)
            aile = -1F;
        float elev = ((FlightModelMain) (super.FM)).CT.getElevator() + (float)forceTrim_y;
        if(elev > 1.0F)
            elev = 1.0F;
        if(elev < -1F)
            elev = -1F;
        float rudd = ((FlightModelMain) (super.FM)).CT.getRudder() + ((FlightModelMain) (super.FM)).CT.getTrimRudderControl();
        if(rudd > 1.0F)
        	rudd = 1.0F;
        if(rudd < -1F)
        	rudd = -1F;
        if(getTrim)
        {
            forceTrim_x = aile;
            forceTrim_y = elev;
            forceTrim_z = rudd;
            getTrim = false;
        }
        double Wx = ((Tuple3d) (super.FM.getW())).x;
        double Wy = ((Tuple3d) (super.FM.getW())).y;
        double Wz = ((Tuple3d) (super.FM.getW())).z;
        double AWx = ((Tuple3d) (super.FM.getAW())).x;
        double AWy = ((Tuple3d) (super.FM.getAW())).y;
        double AWz = ((Tuple3d) (super.FM.getAW())).z;
        float aPitch = (((FlightModelMain) (super.FM)).EI.engines[0].getControlProp() + ((FlightModelMain) (super.FM)).EI.engines[1].getControlProp()) / 2.0F;
        float aThrottle = (((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle() + ((FlightModelMain) (super.FM)).EI.engines[1].getControlThrottle()) / 2.0F;
        float aThrust = (((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() + ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput()) / 2.0F;
        Vector3d vFlow = super.FM.getVflow();
        double sinkRate = ((Tuple3d) (vFlow)).z;
        float airDensity = Atmosphere.density((float)((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).z);
        double rotorSurface = 30D;
        double rotorSurface_cyclic = 10D;
        double tailRotorSurface = 1.3500000000000001D;
        double rotorCy = 4.3D;
        double rotorCx = 0.0014D;
        double rotorCx_dyn = 0.00050000000000000001D;
        double rotorLineCx = 0.00050000000000000001D * (double)(aPitch * aPitch * 15F * 15F);
        double tailRotorLineCx = 0.00050000000000000001D * (double)(rudd * rudd * 5F * 5F);
        double rotorCyDyn_0 = 0.17999999999999999D;
        double rotorCyDyn_line = 0.089999999999999997D;
        double rotorDiameter = 17.300000000000001D;
        double tailRotorDiameter = 4D;
        double rotorRPM_max = 4D;
        double rotorRPM = (double)aThrust * rotorRPM_max;
        double hubDirection_x = Math.toRadians(0.0D);
        double hubDirection_y = Math.toRadians(5D);
        double rotorHeight = 2D;
        double rotorSpeed = 6.2831853071795862D * (rotorDiameter / 2D) * 0.5D * rotorRPM;
        double autoPitch = PitchAuto(-Wy);
        double autoRoll = RollAuto(Wx);
        double d_hubDirection_x = Math.toRadians(-((double)aile + autoRoll) * 2D);
        double d_hubDirection_y = Math.toRadians(((double)elev + autoPitch) * 5D);
        double rotorLift_dyn = 0.5D * (rotorCyDyn_0 + rotorCyDyn_line * 13D * (double)aPitch) * rotorSurface * (double)airDensity * rotorSpeed * rotorSpeed;
        double rotorLift_moment_z = 0.5D * (rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * rotorSpeed * rotorSpeed;
        double rotorLift_moment_y = -(rotorDiameter / 2D) * 0.5D * 0.5D * (rotorCyDyn_line * 5D * ((double)elev + autoPitch)) * rotorSurface_cyclic * (double)airDensity * rotorSpeed * rotorSpeed;
        double rotorLift_moment_x = (rotorDiameter / 2D) * 0.5D * 0.5D * (rotorCyDyn_line * 3D * ((double)aile + autoRoll)) * rotorSurface_cyclic * (double)airDensity * rotorSpeed * rotorSpeed;
        rotorLift_moment_y += rotorLift_dyn * (rotorHeight * d_hubDirection_y);
        rotorLift_moment_x += rotorLift_dyn * (rotorHeight * d_hubDirection_x);
        double tailRotorSpeed = (6.2831853071795862D * (tailRotorDiameter / 2D) * 0.5D * (double)aThrust * 1112D) / 60D;
        double tailRotorLift_dyn = 0.5D * (rotorCyDyn_line * 10D * (double)rudd) * (double)airDensity * tailRotorSpeed * tailRotorSpeed;
        double tailRotorLift_moment_y = (tailRotorDiameter / 2D) * 0.5D * 0.5D * (rotorCx + tailRotorLineCx) * tailRotorSurface * (double)airDensity * tailRotorSpeed * tailRotorSpeed;
        double tailRotorLift_moment_z = tailRotorLift_dyn * 10D;
        double rotateSpeed_z = Wz * (rotorDiameter / 2D) * 0.5D;
        double rotateSpeed_y = Wy * (rotorDiameter / 2D) * 0.5D;
        double rotateSpeed_x = Wx * (rotorDiameter / 2D) * 0.5D;
        double inertia_x = (double)(-((FlightModelMain) (super.FM)).M.getFullMass()) * (AWx * 0.0D * 0.033000000000000002D + Wx * 0.0D) * 3D * 3D;
        double inertia_y = (double)(-((FlightModelMain) (super.FM)).M.getFullMass()) * (AWy * 0.0D * 0.033000000000000002D + Wy * 0.0D) * 17D * 17D;
        double inertia_z = (double)(-((FlightModelMain) (super.FM)).M.getFullMass()) * (AWz * 0.0D * 0.033000000000000002D + Wz * 0.0D) * 17D * 17D;
        double balanceMoment_x = (rotorDiameter / 2D) * 0.5D * rotateSpeed_x * rotateSpeed_x * (rotorSurface + 6.4000000000000004D) * (double)airDensity * rotorCy * 0.5D;
        if(rotateSpeed_x < 0.0D)
            balanceMoment_x = 0.0D - balanceMoment_x;
        double balanceMoment_y = (rotorDiameter / 2D) * 0.5D * rotateSpeed_y * rotateSpeed_y * rotorSurface * (double)airDensity * rotorCy * 0.5D;
        if(rotateSpeed_y < 0.0D)
            balanceMoment_y = 0.0D - balanceMoment_y;
        double balanceMoment_z = 10D * rotateSpeed_z * rotateSpeed_z * tailRotorSurface * (double)airDensity * rotorCy * 0.5D;
        if(rotateSpeed_z < 0.0D)
            balanceMoment_z = 0.0D - balanceMoment_z;
        double G = 9.8100000000000005D * (double)((FlightModelMain) (super.FM)).M.getFullMass();
        double balanceMoment_G_x = 0.0D;
        double balanceMoment_G_y = 0.0D;
        double balanceMoment_G_z = 0.0D;
        float antiSinkForce;
        if(sinkRate >= 0.0D)
            antiSinkForce = -(float)(0.5D * rotorCy * rotorSurface * (double)airDensity * sinkRate * sinkRate);
        else
            antiSinkForce = (float)(0.5D * rotorCy * rotorSurface * (double)airDensity * sinkRate * sinkRate);
        float headOnForce;
        double dragMoment_y;
        if(((Tuple3d) (vFlow)).x >= 0.0D)
        {
            headOnForce = -(float)(0.5D * (rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
            dragMoment_y = -2D * (0.5D * (rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
        } else
        {
            headOnForce = (float)(0.5D * (rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
            dragMoment_y = 2D * (0.5D * (rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
        }
        float sideForce;
        float tailRotorMoment;
        double dragMoment_x;
        if(((Tuple3d) (vFlow)).y >= 0.0D)
        {
            sideForce = -(float)(0.5D * (rotorCx + rotorLineCx + 1.0D) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
            tailRotorMoment = (float)(0.5D * rotorCy * tailRotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y) * 10F;
            dragMoment_x = 2D * (0.5D * (rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
        } else
        {
            sideForce = (float)(0.5D * (rotorCx + rotorLineCx + 1.0D) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
            tailRotorMoment = -(float)(0.5D * rotorCy * tailRotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y) * 10F;
            dragMoment_x = -2D * (0.5D * (rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
        }
        double rotorLift_3D_x = Math.sin(hubDirection_y - d_hubDirection_y) * Math.cos(hubDirection_x + d_hubDirection_x) * rotorLift_dyn;
        double rotorLift_3D_y = Math.sin(hubDirection_x + d_hubDirection_x) * rotorLift_dyn;
        double rotorLift_3D_z = Math.cos(hubDirection_y - d_hubDirection_y) * Math.cos(hubDirection_x + d_hubDirection_x) * rotorLift_dyn;
        ((FlightModelMain) (super.FM)).producedAF.x += (double)headOnForce + rotorLift_3D_x;
        ((FlightModelMain) (super.FM)).producedAF.y += (double)sideForce + rotorLift_3D_y;
        ((FlightModelMain) (super.FM)).producedAF.z += (double)antiSinkForce + rotorLift_3D_z;
        ((FlightModelMain) (super.FM)).producedAM.x += (dragMoment_x - balanceMoment_x) + rotorLift_moment_x;
        ((FlightModelMain) (super.FM)).producedAM.y += ((dragMoment_y + tailRotorLift_moment_y) - balanceMoment_y) + rotorLift_moment_y;
        ((FlightModelMain) (super.FM)).producedAM.z += ((double)tailRotorMoment - tailRotorLift_moment_z - balanceMoment_z) + rotorLift_moment_z;
        rotateSpeed_z = 0.0D;
        rotateSpeed_y = 0.0D;
        rotateSpeed_x = 0.0D;
        headOnForce = 0.0F;
        sideForce = 0.0F;
        
//        FM.producedAF.z += aPitch * 40000;
        
        
        Vector3d localVector3d = new Vector3d();
        getSpeed(localVector3d);
        Point3d localPoint3d1 = new Point3d();
        this.pos.getAbs(localPoint3d1);
        float falt = (float)(this.FM.getAltitude() - World.land().HQ(localPoint3d1.x, localPoint3d1.y));
        if ((falt < 10.0F) && (this.FM.getSpeedKMH() < 60.0F) && (localVector3d.z < -1.0D))
        {
        	localVector3d.z *= 0.9D;
        	setSpeed(localVector3d);
        }
    }
    
    public double PitchAuto(double p)
    {
        p = -(p * 4D);
        if(p >= 0.20000000000000001D)
            p = 0.20000000000000001D;
        if(p <= -0.20000000000000001D)
            p = -0.20000000000000001D;
        return p;
    }

    public double RollAuto(double k)
    {
        k = -(k * 4D);
        if(k >= 0.20000000000000001D)
            k = 0.20000000000000001D;
        if(k <= -0.20000000000000001D)
            k = -0.20000000000000001D;
        return k;
    }

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
    
    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
        	HUD.log(AircraftHotKeys.hudLogWeaponId, "Trim: Set");
            getTrim = true;
//            float ctrlZ = ((FlightModelMain) (super.FM)).CT.getRudder();
//            float trimZ = ((FlightModelMain) (super.FM)).CT.getTrimRudderControl();
//            ((FlightModelMain) (super.FM)).CT.setTrimRudderControl(ctrlZ - trimZ);
        if(i == 21)
        {
            forceTrim_x = 0.0D;
            forceTrim_y = 0.0D;
            forceTrim_z = 0.0D;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Trim: Reset");
        }
    	if(i == 24){	
			if(!laserOn) {
    			laserOn = true;
    			laserLock = false;
    			HUD.log(AircraftHotKeys.hudLogWeaponId, "Raduga-Sh: On");
    			
    		} else  if(laserOn) {
        			laserOn = false;
        			laserLock = false;
        			HUD.log(AircraftHotKeys.hudLogWeaponId, "Raduga-Sh: Off");
    		}
    	}
    }


    


    
    private float pictVBrake;
    /*     */   private float pictAileron;
    /*     */   private float pictVator;
    /*     */   private float pictRudder;

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
    
    public boolean FLIR;
    
    public BulletEmitter Weapons[][];
    public int rocketHookSelected;
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
    private float rotorRPM;
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
    private float curAngleRotor;
    private float diffAngleRotor;
    private long lastTimeFan;
    private Orient oMainRotor;
    private Vector3f vThrust;
    private float iMainTorque;
    private float MainRotorStatus;
    private float TailRotorStatus;
    
    public double forceTrim_x;
    public double forceTrim_y;
    public double forceTrim_z;
    public boolean getTrim;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.Mi24V.class;
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
            9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_BombSpawn01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", 
            "_Flare01", "_Flare02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", 
            "_ExternalRock11", "_ExternalRock12", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", 
            "_ExternalDev11", "_ExternalDev12"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 32;
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
            s = "64xS-5M+4xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "128xS-5M+4xR-60M";
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
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "40xS-8+4xR-60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "Pylon_B8", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS8", 20);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "80xS-8+4xR-60M";
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
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunR60M", 1);
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
            s = "10xS-13";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "20xS-13";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "10xS-13+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
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
            s = "20xS-13+4xSturm-V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonB13", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS13", 5);
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
