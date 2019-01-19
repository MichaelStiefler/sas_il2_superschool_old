// Decompiled by DJ v3.12.12.101 Copyright 2016 Atanas Neshkov  Date: 05.10.2017 20:29:34
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Mi24V.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.ai.ground.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.trains.Wagon;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryWasted.VDVInfantry;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.Missile;
import com.maddox.il2.objects.weapons.RocketGunSturmV;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.sound.*;
import com.maddox.util.HashMapExt;
import com.maddox.util.HashMapInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

public class Mi24V extends Scheme2 implements TypeHelicopter, TypeStormovik,
		TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeLaserSpotter {

	public Mi24V() {

		forceTrim_x = 0.0D;
		forceTrim_y = 0.0D;
		forceTrim_z = 0.0D;
		getTrim = false;
		curAngleRotor = 0.0F;
		lastTimeFan = Time.current();
		rocketHookSelected = 2;
		suka = new Loc();
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
		intervalMissileLaunchThreat = 500L;
		guidedMissileUtils = new GuidedMissileUtils(this);
		curAngleRotor = 0.0F;
		lastTimeFan = Time.current();
		TailRotorDestroyed = false;
		laserOn = false;
		gndYaw = 0.0F;
		gndRoll = 0.0F;
		gndPitch = 0.0F;
		hover = false;
		isAirstart = false;
		AltCheck = false;
		landing = false;
		counter = 0;
		repMod = 0;
		missileLaunchInterval = 0L;
		victim = null;
		missilesList = new ArrayList();
	}

	public void laserUpdate() {
		if (laserOn) {
			this.pos.getRender(_tmpLoc);
			LaserHook[1] = new HookNamed(this, "_Laser1");
			LaserLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
			this.LaserHook[1].computePos(this, _tmpLoc, LaserLoc1);
			LaserLoc1.get(LaserP1);
			LaserLoc1.set(6000.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
			this.LaserHook[1].computePos(this, _tmpLoc, LaserLoc1);
			LaserLoc1.get(LaserP2);
			Engine.land();
			if (Landscape.rayHitHQ(LaserP1, LaserP2, LaserPL)) {
				LaserPL.z -= 0.95D;
				LaserP2.interpolate(LaserP1, LaserPL, 1.0F);
				Mi24V.spot.set(LaserP2);
			}
		}
	}

	public long getChaffDeployed() {
		if (hasChaff)
			return lastChaffDeployed;
		else
			return 0L;
	}

	public long getFlareDeployed() {
		if (hasFlare)
			return lastFlareDeployed;
		else
			return 0L;
	}

	public void setCommonThreatActive() {
		long curTime = Time.current();
		if (curTime - lastCommonThreatActive > intervalCommonThreat) {
			lastCommonThreatActive = curTime;
			doDealCommonThreat();
		}
	}

	public void setRadarLockThreatActive() {
		long curTime = Time.current();
		if (curTime - lastRadarLockThreatActive > intervalRadarLockThreat) {
			lastRadarLockThreatActive = curTime;
			doDealRadarLockThreat();
		}
	}

	public void setMissileLaunchThreatActive() {
		if (bShotFlare) {
			long curTime = Time.current();
			if (curTime - lastMissileLaunchThreatActive > intervalMissileLaunchThreat) {
				lastMissileLaunchThreatActive = curTime;
				doDealMissileLaunchThreat();
				lastFlareDeployed++;
				if (lastFlareDeployed > 2)
				{
					lastFlareDeployed = 0L;
					bShotFlare = false;
				}
			}
		}
		if (FM.CT.Weapons[7][0].haveBullets() && FM.CT.Weapons[7][1].haveBullets()) { 
			hasFlare = true; 
		} else {
			hasFlare = false; 
		}
	}

	private void doDealCommonThreat() {
	}

	private void doDealRadarLockThreat() {
	}

	private void doDealMissileLaunchThreat() {
		if (hasFlare) {
		FM.CT.Weapons[7][0].shots(1);
		FM.CT.Weapons[7][1].shots(1);
		}
	}
		
	private void OperatorLookout() {
		double d = Main3D.cur3D().land2D.worldOfsX() + this.pos.getAbsPoint().x;
		double d1 = Main3D.cur3D().land2D.worldOfsY() + this.pos.getAbsPoint().y;
		double d2 = Main3D.cur3D().land2D.worldOfsY() + this.pos.getAbsPoint().z;
		int i = (int) (-(this.pos.getAbsOrient().getYaw() - 90D));
		if (i < 0) {
			i = 360 + i;
		}
		float f1 = World.getTimeofDay();
		boolean flag = false;
		if (((f1 >= 0.0F) && (f1 <= 5F)) || ((f1 >= 21F) && (f1 <= 24F))) {
			flag = true;
		}
		int i1 = TrueRandom.nextInt(100);
		int i2 = 5 - FM.Skill;
		int i3 = i2 + TrueRandom.nextInt(i2);
		counter++;
		if (counter >= 5 + i3)
			counter = 0;
//Missile look code:
		if (!FM.Gears.onGround()) 
		{
			List list = Engine.missiles();
			int j1 = list.size();
			for(int k1 = 0; k1 < j1; k1++)
			{
				Actor missile = (Actor)list.get(k1);
				if (missile.getSpeed(vector3dop) > 500D && missile.getArmy() != this.getArmy())
				{
					double d3 = Main3D.cur3D().land2D.worldOfsX() + missile.pos.getAbsPoint().x;
					double d4 = Main3D.cur3D().land2D.worldOfsY() + missile.pos.getAbsPoint().y;
					double d5 = Main3D.cur3D().land2D.worldOfsY() + missile.pos.getAbsPoint().z;
					double d8 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
					String s = "";
					if ((d2 - d5 - 500D) >= 0.0D) {
						s = " LOW";
					}
					if (((d2 - d5) + 500D) < 0.0D) {
						s = " HIGH";
					}
					double d9 = d3 - d;
					double d10 = d4 - d1;
					float f = 57.32484F * (float) Math.atan2(d10, -d9);
					int j = (int) (Math.floor((int) f) - 90D);
					if (j < 0) {
						j = 360 + j;
					}
					int k = j - i;
					if (k < 0) {
						k = 360 + k;
					}
					int l = (int) (Math.ceil((k + 15) / 30D) - 1.0D);
					if (l < 1) {
						l = 12;
					}
					double d11 = d - d3;
					double d12 = d1 - d4;
					double d13 = Math.ceil(Math.sqrt((d12 * d12) + (d11 * d11)) / 10D) * 10D;
					if (!flag && (d13 <= 3000D) && (d13 >= 20D) && (Math.sqrt(d8 * d8) <= 2000D)) {
						if (!isAI) {
							HUD.logCenter("MISSILE AT " + l + " O'CLOCK" + s + "!");
							Voice.speakDanger(this, 4);
							HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: ASO-2 Engaged!");
						}
						if (isAI) {
							((Maneuver) FM).set_maneuver(100);
						}
						bShotFlare = true;
					}
					if (flag && (d13 <= 1000D) && (d13 >= 20D) && (Math.sqrt(d8 * d8) <= 500D)) {
						if (!isAI) {
							HUD.logCenter("MISSILE AT " + l + " O'CLOCK" + s + "!");
							Voice.speakDanger(this, 4);
							HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: ASO-2 Engaged!");
						}
						if (isAI) {
							((Maneuver) FM).set_maneuver(100);
						}
						bShotFlare = true;
					}
				}
			}
		}
//Operator Look Air:
		List targets = Engine.targets();
		int v = targets.size();
		for(int b = 0; b < v; b++) {
			Actor threat = (Actor)targets.get(b);
			double d3 = Main3D.cur3D().land2D.worldOfsX() + threat.pos.getAbsPoint().x;
			double d4 = Main3D.cur3D().land2D.worldOfsY() + threat.pos.getAbsPoint().y;
			double d5 = Main3D.cur3D().land2D.worldOfsY() + threat.pos.getAbsPoint().z;
			if (repMod == 2 && counter == 0) 
			{
				if ((threat instanceof Aircraft) && (threat.getArmy() != this.getArmy()) && !FM.Gears.onGround()) {
					double d8 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
					String s = "";
					if ((d2 - d5 - 500D) >= 0.0D) {
						s = " low";
					}
					if (((d2 - d5) + 500D) < 0.0D) {
						s = " high";
					}
					double d9 = d3 - d;
					double d10 = d4 - d1;
					float f = 57.32484F * (float) Math.atan2(d10, -d9);
					int j = (int) (Math.floor((int) f) - 90D);
					if (j < 0) {
						j = 360 + j;
					}
					int k = j - i;
					if (k < 0) {
						k = 360 + k;
					}
					int l = (int) (Math.ceil((k + 15) / 30D) - 1.0D);
					if (l < 1) {
						l = 12;
					}
					double d11 = d - d3;
					double d12 = d1 - d4;
					double d13 = Math.ceil(Math.sqrt((d12 * d12) + (d11 * d11)) / 10D) * 10D;
					String s1 = "Aircraft ";
					if (threat instanceof TypeFighter) {
						s1 = "Fighters ";
					}
					if (threat instanceof TypeBomber) {
						s1 = "Bombers ";
					}
					if (threat instanceof TypeHelicopter) {
						s1 = "Helicopters ";
					}
					if (!flag && (i1 <= 50 + i2) && (d13 <= 4000D) && (d13 >= 500D) && (Math.sqrt(d8 * d8) <= 2000D)) {
						if (!isAI) {
							HUD.training(s1 + "at " + l + " o'clock" + s + "!");
						}
					}
					if (flag && (i1 <= 5 + i2) && (d13 <= 1000D) && (d13 >= 100D) && (Math.sqrt(d8 * d8) <= 500D)) {
						if (!isAI) {
							HUD.training(s1 + "at " + l + " o'clock" + s + "!");
						}
					}
				}
			}
//Operator Look Ground:
			if ((!isAI && repMod == 1 && counter == 0) || (isAI)) 
			{
				if (Actor.isAlive(threat) && ((threat instanceof TankGeneric) || (threat instanceof Wagon) || (threat instanceof ArtilleryGeneric) || (threat instanceof BigshipGeneric) || (threat instanceof ShipGeneric) || (threat instanceof CarGeneric)) && (threat.getArmy() != this.getArmy())) 
				{
					double d8 = (d2 - d5) * 2D;
					if (d8 > 6000D) {
						d8 = 6000D;
					}
					if (flag) {
						d8 = 1500D - (d2 - d5);
					}
					if (!flag && d8 < 2000 + FM.Skill * 100) {
						d8 = FM.Skill * 100 + 2000;
					}
					String s = "units";
					if (threat instanceof TankGeneric) {
						s = "armor";
					}
					if (threat instanceof ArtilleryGeneric) {
						s = "guns";
					}
					if (threat instanceof CarGeneric) {
						s = "vehicles";
					}
					if (threat instanceof Wagon) {
						s = "train";
					}
					if ((threat instanceof BigshipGeneric) || (threat instanceof ShipGeneric)) {
						s = "ship";
						d8 *= 2D;
					}
					double d9 = d3 - d;
					double d10 = d4 - d1;
					float f11 = 57.32484F * (float) Math.atan2(d10, -d9);
					double d11 = Math.floor((int) f11) - 90D;
					if (d11 < 0.0D) {
						d11 = 360D + d11;
					}
					int k = (int) (d11 - i);
					if (k < 0) {
						k = 360 + k;
					}
					int l = (int) (Math.ceil((k + 15) / 30D) - 1.0D);
					if (l < 1) {
						l = 12;
					}
					double d12 = d - d3;
					double d13 = d1 - d4;
					double d14 = Math.ceil(Math.sqrt((d13 * d13) + (d12 * d12)));
					if (d14 <= d8) {
						if (!isAI) {
							HUD.training("Enemy " + s + " spotted at " + l + " o'clock!");
							if(!bManualFire) {
								if (l == 11 || l == 12 || l == 13)
								{
								victim = threat;
								Reflection.setInt(guidedMissileUtils, "engageMode", 1);
								doOperatorFireMissile();
								}
							}
							if (isAI) {
								doLaunchMissileAI();
							}
						}
					}
				}
			}
		}
	}
	
	public GuidedMissileUtils getGuidedMissileUtils() {
		return guidedMissileUtils;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		checkAmmo();
		FM.turret[1].bIsAIControlled = false;
		guidedMissileUtils.onAircraftLoaded();
		World.cur().diffCur.Engine_Overheat = false;
    	if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))  		
        {
    		isAI = false;
        } else {
        	isAI = true;
        	Squares squares = (Squares)Reflection.getValue(FM, "Sq");
        	squares.squareWing = 66.0F;
        	squares.squareAilerons = 1.00F;
        	squares.squareFlaps = 0.81F;
        	squares.liftStab = 5.20F;
        	squares.squareElevators = 2.80F;
        	squares.liftKeel = 0.1F;
        	squares.squareRudders = 2.50F;
        	squares.liftWingLIn = (squares.liftWingRIn = 10.00F);
        	squares.liftWingLMid = (squares.liftWingRMid = 10.00F);
        	squares.liftWingLOut = (squares.liftWingROut = 10.00F);
        	squares.dragFuselageCx = 0.06F;
        	squares.dragAirbrakeCx = 1.00F;
       	
        	Vector3f eVect = new Vector3f();
            eVect.x = 1.0F; eVect.y = 0.0F; eVect.z = 0.0F;
            
            Point3d ePos = new Point3d();
            ePos.x = 1.5F; ePos.y = 0.0F; ePos.z = 0.0F;
            
            Point3d ePropPos = new Point3d();
            ePropPos.x = 1.0F; ePropPos.y = 0.0F; ePropPos.z = 0.0F;
            
            this.FM.EI.engines[0].setVector(eVect);
            this.FM.EI.engines[0].setPos(ePos);
            this.FM.EI.engines[0].setPropPos(ePropPos);
        	
        	Reflection.setFloat(FM.EI.engines[0], "tChangeSpeed", 0.000001F);
        	Reflection.setFloat(FM.EI.engines[0], "horsePowers", 3200.0F);
        	Reflection.setFloat(FM.EI.engines[0], "propReductor", 1.0F);
      	
            this.FM.EI.engines[1].setVector(eVect);
            this.FM.EI.engines[1].setPos(ePos);
            this.FM.EI.engines[1].setPropPos(ePropPos);
        	
        	Reflection.setFloat(FM.EI.engines[1], "tChangeSpeed", 0.000001F);
        	Reflection.setFloat(FM.EI.engines[1], "horsePowers", 3200.0F);
        	Reflection.setFloat(FM.EI.engines[1], "propReductor", 1.0F);
        	Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        	polares.AOA_crit = 10.0F;
        	Reflection.setFloat(FM, "Vmin", 20.0F);
        	Reflection.setFloat(FM, "Vmax", 280.0F);
        	Reflection.setFloat(FM, "VmaxAllowed", 300.0F);
        	Reflection.setFloat(FM, "VmaxH", 280.0F);
        	Reflection.setFloat(FM, "HofVmax", 7900.0F);
        	Reflection.setFloat(FM, "VminFLAPS", 20.0F);
        	Reflection.setFloat(FM, "VmaxFLAPS", 280.0F);
        	Reflection.setFloat(FM, "VmaxFLAPS", 280.0F);
        	polares.Cy0_max = 0.15F;
        	polares.FlapsMult = 1.0F;
        	polares.FlapsAngSh = 4.0F;
        	polares.lineCyCoeff = 0.29F;
        	polares.AOAMinCx_Shift = 0.0F;
        	polares.Cy0_0 = 5.53F;
        	polares.AOACritH_0 = 10.0F;
        	polares.AOACritL_0 = -6.0F;
        	polares.CyCritH_0 = 5.9934692F;
        	polares.CyCritL_0 = -0.7648107F;
        	polares.parabCxCoeff_0 = 5.0E-4F;
        	polares.CxMin_0 = 0.0125F;
        	polares.Cy0_1 = 7.53F;
        	polares.AOACritH_1 = 10.0F;
        	polares.AOACritL_1 = -6.0F;
        	polares.CyCritH_1 = 8.2791651F;
        	polares.CyCritL_1 = -0.7F;
        	polares.CxMin_1 = 0.15F;
        	polares.parabCxCoeff_1 = 7.5E-5F;
        	polares.parabAngle = 5.0F;
        	polares.declineCoeff = 0.008F;
        	polares.maxDistAng = 40.0F;
        	FM.setGCenter(0.0F);
    		FM.CT.bHasBrakeControl = true;
    		FM.CT.bHasAirBrakeControl = false;
        }
	}

	public void rareAction(float f, boolean flag) {
		OperatorLookout();
		super.rareAction(f, flag);
		if (((FlightModelMain) (super.FM)).AS.bNavLightsOn) {
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
		if (!bObserverKilled)
			if (obsLookTime == 0) {
				obsLookTime = 2 + World.Rnd().nextInt(1, 3);
				obsMoveTot = 1.0F + World.Rnd().nextFloat() * 1.5F;
				obsMove = 0.0F;
				obsAzimuthOld = obsAzimuth;
				obsElevationOld = obsElevation;
				if ((double) World.Rnd().nextFloat() > 0.80000000000000004D) {
					obsAzimuth = 0.0F;
					obsElevation = 0.0F;
				} else {
					obsAzimuth = World.Rnd().nextFloat() * 140F - 70F;
					obsElevation = World.Rnd().nextFloat() * 50F - 20F;
				}
			} else {
				obsLookTime--;
			}
	}

	protected void moveElevator(float f1) {
	}

	protected void moveAileron(float f1) {
	}

	protected void moveFlap(float f) {
	}

	protected void moveRudder(float f) {
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		float f_1_ = Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, -90F);
		float f_2_ = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, -85F);
		float f_3_ = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, 85F);
		float f_4_ = f <= 0.5F ? Aircraft.cvt(f, 0.1F, 0.5F, 0.0F, 70F)
				: Aircraft.cvt(f, 0.8F, 1.0F, 70F, 0.0F);
		float f_5_ = f <= 0.5F ? Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, -70F)
				: Aircraft.cvt(f, 0.8F, 1.0F, -70F, 0.0F);
		float f_6_ = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -90F);
		float f_7_ = Aircraft.cvt(f, 0.1F, 0.8F, -7.0F, 90F);
		float f_8_ = Aircraft.cvt(f, 0.2F, 0.9F, 7.0F, -90F);
		float f_9_ = Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, -140F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, f_1_);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 0.0F, f_9_);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, f_2_, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, f_3_, 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f_4_, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f_5_, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", f_8_, 7.0F, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", f_7_, -7.0F, 0.0F);
	}

	public void moveWheelSink() {
		resetYPRmodifier();
		hierMesh().chunkSetAngles("GearL22_D0", 0.0F, FM.Gears.gWheelSinking[0] * -90F, 0.0F);
		hierMesh().chunkSetAngles("GearR22_D0", 0.0F, FM.Gears.gWheelSinking[1] * 90F, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(hierMesh(), f);
	}

	public void moveSteering(float f) {
		float f_1_ = Aircraft.cvt(1.0F, 0.1F, 0.9F, 0.0F, -90F);
		if (FM.CT.GearControl > 0.1F) {
			if (f < -80F)
				hierMesh().chunkSetAngles("GearC2_D0", -80F, 0.0F, f_1_);
			else if (f > 80F) 
				hierMesh().chunkSetAngles("GearC2_D0", 80F, 0.0F, f_1_);
			else
				hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, f_1_);
		} else {
			hierMesh().chunkSetAngles("GearC2_D0", 0.0F, 0.0F, f_1_);
		}
	}

	public void moveCockpitDoor(float f) {
		resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.95F, 0.0F, 0.9F);
		hierMesh().chunkSetAngles("Door1_D0", -90F * f, 0.0F, 0.0F);
		hierMesh().chunkSetAngles("Door2_D0", 0.0F, -65F * f, 0.0F);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	protected void moveBayDoor(float f) {
		hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -120F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 120F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 120F * f, 0.0F);
		hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -120F * f, 0.0F);
	}

    protected void moveFan(float f)
    {
        float tRPM = (float) Math.sqrt((Math.pow(FM.EI.engines[0].getRPM(), 2.0F) + Math.pow(FM.EI.engines[1].getRPM(), 2.0F)) / 2.0F);
        rotorRPM += (tRPM - rotorRPM) * 0.0001F;
        if (isAI)
        {    
        	if(FM.EI.engines[0].getStage() > 5 && FM.EI.engines[1].getStage() > 5)
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
        } else {
        	if(tRPM > 4060F)
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
		if(TailRotorDestroyed)
    	{
			FM.Or.increment(cvt(tRPM, 0.0F, 12500F, 0.0F, 3.0F), 0.0F, 0.0F);
			hierMesh().chunkVisible("Prop2_D0", false);
			hierMesh().chunkVisible("PropRot2_D0", false);
			hierMesh().chunkVisible("Prop2_D1", true);
    	}
    }

	private void tiltRotor(float f) {
		hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F,
				-((FlightModelMain) (super.FM)).CT.getElevator() / 10F);
		hierMesh().chunkSetAngles("AroneL_D0", 0.0F,
				-((FlightModelMain) (super.FM)).CT.getAileron() / 10F, 0.0F);
	}

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1") || s.endsWith("p2"))
                    getEnergyPastArmor(16.65F / (1E-005F + (float)Math.abs(v1.x)), shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                if(s.endsWith("1"))
                {
                    if(getEnergyPastArmor(3.2F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                } else
                if(s.endsWith("2"))
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.33F)
                        {
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                            debugprintln(this, "*** Throttle Quadrant: Hit, Engine 1 Controls Disabled..");
                        }
                        if(World.Rnd().nextFloat() < 0.33F)
                        {
                            FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                            debugprintln(this, "*** Throttle Quadrant: Hit, Engine 2 Controls Disabled..");
                        }
                    }
                } else
                if(s.endsWith("3") || s.endsWith("4"))
                {
                    if(World.Rnd().nextFloat() < 0.12F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        debugprintln(this, "*** Evelator Controls Out..");
                    }
                } else
                if(World.Rnd().nextFloat() < 0.12F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 0);
                    debugprintln(this, "*** Arone Controls Out..");
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.5F / (float)Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F)
                {
                    debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if((s.endsWith("lo1") || s.endsWith("lo2")) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if((s.endsWith("ro1") || s.endsWith("ro2")) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxeng"))
            {
                int i = s.charAt(5) - 49;
                debugprintln(this, "*** Engine" + i + " Hit..");
                if(s.endsWith("case") && getEnergyPastArmor(0.1F, shot) > 0.0F)
                {
                    if(World.Rnd().nextFloat() < shot.power / 200000F)
                    {
                        FM.AS.setEngineStuck(shot.initiator, i);
                        debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                    }
                    if(World.Rnd().nextFloat() < shot.power / 50000F)
                    {
                        FM.AS.hitEngine(shot.initiator, i, 2);
                        debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                    }
                    if(World.Rnd().nextFloat() < shot.power / 28000F)
                    {
                        FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
                        debugprintln(this, "*** Engine Crank Case Hit - Cylinder Feed Out, " + FM.EI.engines[i].getCylindersOperable() + "/" + FM.EI.engines[i].getCylinders() + " Left..");
                    }
                    FM.EI.engines[i].setReadyness(shot.initiator, FM.EI.engines[i].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[i].getReadyness() + "..");
                }
                if(s.endsWith("cyl1") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[i].getCylindersRatio() * 1.75F)
                {
                    FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    debugprintln(this, "*** Engine Cylinders Hit, " + FM.EI.engines[i].getCylindersOperable() + "/" + FM.EI.engines[i].getCylinders() + " Left..");
                    if(FM.AS.astateEngineStates[i] < 1)
                        FM.AS.hitEngine(shot.initiator, i, 1);
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        FM.AS.hitEngine(shot.initiator, i, 3);
                        debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("supc") && getEnergyPastArmor(0.05F, shot) > 0.0F)
                    FM.EI.engines[i].setKillCompressor(shot.initiator);
                if(s.endsWith("prop") && getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    FM.EI.engines[i].setKillPropAngleDevice(shot.initiator);
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int j = s.charAt(6) - 48;
                switch(j)
                {
                case 1: // '\001'
                case 2: // '\002'
                    doHitMeATank(shot, 0);
                    break;

                case 3: // '\003'
                    doHitMeATank(shot, 1);
                    break;

                case 4: // '\004'
                case 5: // '\005'
                    doHitMeATank(shot, 2);
                    break;
                }
                return;
            }
            if(s.startsWith("xxw1") || s.startsWith("xxoil1"))
            {
                if(World.Rnd().nextFloat() < 0.12F && getEnergyPastArmor(2.25F, shot) > 0.0F)
                {
                    FM.AS.hitOil(shot.initiator, 0);
                    debugprintln(this, "*** Oil Radiator L Hit..");
                }
                return;
            }
            if(s.startsWith("xxw2") || s.startsWith("xxoil1"))
            {
                if(World.Rnd().nextFloat() < 0.12F && getEnergyPastArmor(2.25F, shot) > 0.0F)
                {
                    FM.AS.hitOil(shot.initiator, 1);
                    debugprintln(this, "*** Oil Radiator R Hit..");
                }
                return;
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
                return;
            }
            if(s.startsWith("xxmgun0"))
            {
                int k = s.charAt(7) - 49;
                if(getEnergyPastArmor(0.75F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Machine Gun (" + k + ") Disabled..");
                    FM.AS.setJamBullets(0, k);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
            if(s.startsWith("xxcannon0"))
            {
                int l = s.charAt(9) - 49;
                if(getEnergyPastArmor(6.29F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Cannon (" + l + ") Disabled..");
                    FM.AS.setJamBullets(1, l);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            } else
            {
                return;
            }
        }
        if(s.startsWith("xcf") || s.startsWith("xcockpit"))
        {
            hitChunk("CF", shot);
            if(point3d.x > 0.0D)
            {
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
                if(point3d.z > 0.40000000000000002D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            }
        } else
        if(s.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel")) {
            hitChunk("Keel1", shot);
        	TailRotorDamage();
        } else
        if(s.startsWith("xrudder"))
            hitChunk("Rudder1", shot);
        else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl"))
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr"))
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl"))
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr"))
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xwinglmid"))
            {
                if(chunkDamageVisible("WingLMid") < 3)
                    hitChunk("WingLMid", shot);
                if(World.Rnd().nextFloat() < shot.mass + 0.02F)
                    FM.AS.hitOil(shot.initiator, 0);
            }
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
            if(s.endsWith("1"))
            {
                if(World.Rnd().nextFloat() < 0.05F)
                {
                    debuggunnery("Hydro System: Disabled..");
                    FM.AS.setInternalDamage(shot.initiator, 0);
                }
            } else
            if(World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
            {
                debuggunnery("Undercarriage: Stuck..");
                FM.AS.setInternalDamage(shot.initiator, 3);
            }
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

    private final void doHitMeATank(Shot shot, int i)
    {
        if(getEnergyPastArmor(0.2F, shot) > 0.0F)
            if(shot.power < 14100F)
            {
                if(FM.AS.astateTankStates[i] == 0)
                {
                    FM.AS.hitTank(shot.initiator, i, 1);
                    FM.AS.doSetTankState(shot.initiator, i, 1);
                }
                if(FM.AS.astateTankStates[i] > 0 && (World.Rnd().nextFloat() < 0.02F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F))
                    FM.AS.hitTank(shot.initiator, i, 2);
            } else
            {
                FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int)(shot.power / 56000F)));
            }
    }

	public void doMurderPilot(int i) {
		switch (i) {
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
	
	private void TailRotorDamage() {
	int rnd = TrueRandom.nextInt(20);
	if(!TailRotorDestroyed && rnd == 1)
		{
			if (World.getPlayerAircraft() == this) {
				HUD.log(AircraftHotKeys.hudLogWeaponId, "Tail Rotor: Damaged!");
			}
			TailRotorDestroyed = true;
		}
	}
	
	private void stability() { 
    	if (!AltCheck && FM.getAltitude() - Landscape.HQ_Air((float) this.FM.Loc.x, (float) this.FM.Loc.y) > 20) {
    		isAirstart = true;
    		AltCheck = true;
    	}
        if (isAirstart && FM.EI.engines[0].getStage() < 6 && FM.EI.engines[0].getStage() < 6) 
        {
        	FM.EI.engines[0].doSetStage(6);
        	FM.EI.engines[1].doSetStage(6);
        	if (FM.EI.engines[0].getStage() == 6 && FM.EI.engines[0].getStage() == 6) 
        	{
        		isAirstart = false;
        	}
        }
		Vector3f eVect = new Vector3f();
		eVect.x = 1.0F;
		eVect.y = 0.0F;
		eVect.z = 0.0F;
		FM.EI.engines[0].setVector(eVect);
		FM.EI.engines[1].setVector(eVect);

		this.pos.getAbs(localPoint3d1);
		Vector3d localVector3d = new Vector3d();
		getSpeed(localVector3d);
		float afZ = 0.0F;
		float afX = 0.0F;
		float avT = (FM.EI.engines[0].getThrustOutput() + FM.EI.engines[1].getThrustOutput()) / 2F;
		float alt = FM.getAltitude() - Landscape.HQ_Air((float) this.FM.Loc.x, (float) this.FM.Loc.y);
		Polares polares = (Polares)Reflection.getValue(FM, "Wing");
		polares.Cy0_0 = cvt(alt, 30F, 50F, 5.5F, 0.55F);
    	polares.lineCyCoeff = cvt(alt, 30F, 50F, 0.29F, 0.19F);
    	polares.CyCritH_0 = cvt(alt, 30F, 50F, 5.9934692F, 1.1F);
		FM.SensYaw = 0.5F; 
		FM.SensPitch = 0.3F;
		FM.SensRoll = 0.3F;

		if (!tookOff) {
			if (((com.maddox.il2.ai.air.Maneuver) FM).get_maneuver() == 26 && FM.getSpeed() > 0.1F ) 
			{
				afZ = 150000;
				afX = 0;
				float altlim = 10.0F;
				if (FM.Gears.nOfGearsOnGr > 2) {
					gndYaw = FM.Or.getYaw();
					gndRoll = FM.Or.getRoll();
					gndPitch = FM.Or.getPitch();
				}
				FM.Or.set(-gndYaw, cvt(alt, 0.0F, altlim, gndPitch, gndPitch - 8.0F), gndRoll);
				if (!hover)
				{
					float a = 0.0F;
					if (alt < 7)
						a = 7.0F;
					if (alt > 7)
						a = altlim;
					float vertSpd = cvt(alt, 0.0F, a, 0.98F, 0.9F);
					localVector3d.z *= vertSpd + World.cur().rnd.nextDouble(-0.05F, 0.05F);
					localVector3d.x *= 0.9 + World.cur().rnd.nextDouble(-0.05F, 0.05F);
					localVector3d.y *= 0.9 + World.cur().rnd.nextDouble(-0.05F, 0.05F);
					setSpeed(localVector3d);
					if (alt > altlim){
						hover = true;
					}
				}
				if (hover)
				{
					if (FM.getVertSpeed() < 0.0F) 
					{
						localVector3d.z = 0.0;
						setSpeed(localVector3d);
					}
					if (FM.getVertSpeed() > 0.1F)
					{
						float verSPDlimit = cvt(this.FM.getVertSpeed(), 0.0F, 3.0F, 1.0F, 0.85F);
						localVector3d.z *= verSPDlimit;
						setSpeed(localVector3d);
					}
					if (FM.getSpeedKMH() > 80F) {
						afZ = 0.0F;
						afX = 0.0F;
						((com.maddox.il2.ai.air.Maneuver) FM).unblock();
						tookOff = true;
						hover = false;
					}
				}
			}
		}
		if (((com.maddox.il2.ai.air.Maneuver) FM).get_maneuver() == 25) 
		{
			landing = true;
		}
		if (landing) 
		{
			if (alt <= 17 && alt > 7)
			{
				FM.setCapableOfTaxiing(false);
				((com.maddox.il2.ai.air.Maneuver) FM).set_maneuver(66);
				FM.CT.ElevatorControl = 0.8F;
				localVector3d.x *= 0.995;
				localVector3d.y *= 0.995;
				localVector3d.z *= 0.1;
			}		
			if (alt <= 7)
			{
				((com.maddox.il2.ai.air.Maneuver) FM).setSpeedMode(8);	
				localVector3d.x *= 0.97;
				localVector3d.y *= 0.97;
				localVector3d.z *= 0.4;
				if (FM.Gears.nOfGearsOnGr > 2)
				{
					((com.maddox.il2.ai.air.Maneuver) FM).set_maneuver(66);
					FM.CT.BrakeControl = 1.0F;
					FM.EI.setEngineStops();
					MsgDestroy.Post(Time.current() + 12000L, this);
				}
			}
			setSpeed(localVector3d);
		}
		if (FM.getSpeedKMH() > 0.1F && !landing && !(((Maneuver) FM).get_maneuver() == 44) && !(((Maneuver) FM).get_maneuver() == 49) && !FM.isReadyToDie() && !FM.isTakenMortalDamage()) {
			FM.producedAF.z += avT * cvt(alt, 30F, 50F, 30000F, 0F) + afZ;
			FM.producedAF.x += avT * cvt(alt, 30F, 50F, 5000F, 0F) + afX;
			if (FM.getSpeedKMH() >= 250)
				FM.Sq.dragParasiteCx += 0.1F;
			if (FM.getSpeedKMH() >= 260)
				FM.Sq.dragParasiteCx += 0.15F;
			if (FM.getSpeedKMH() >= 290)
				FM.Sq.dragParasiteCx += 0.25F;
		}

	}
	
	private void OperatorTurret() {
		Pilot pilot = (Pilot) FM;
		if ((pilot != null) && !Mission.isNet()) {
			if (isAI) {
				Actor actor = War.GetNearestEnemy(this, 1, 3000F);
				if (pilot != null && isAlive(actor) && !(actor instanceof BridgeSegment)) {
					Point3d point3d = new Point3d();
					actor.pos.getAbs(point3d);
					if (pos.getAbsPoint().distance(point3d) < 1700D) {
						point3d.sub(FM.Loc);
						FM.Or.transformInv(point3d);
						if (point3d.y < 0.0D) {
							FM.turret[0].target = actor;
							FM.turret[0].tMode = 2;
						}
					}
				} else if (actor != null) {
					if (FM.turret[0].target != null && !(FM.turret[0].target instanceof Aircraft) && !isAlive(FM.turret[0].target))
						FM.turret[0].target = null;
				}
			} else {
				Actor actor = victim;
				if (pilot != null && victim != null && isAlive(actor)) {
					Point3d point3d = new Point3d();
					actor.pos.getAbs(point3d);
					if (pos.getAbsPoint().distance(point3d) < 1700D) {
						point3d.sub(FM.Loc);
						FM.Or.transformInv(point3d);
						if (point3d.y < 0.0D) {
							FM.turret[0].target = actor;
							FM.turret[0].tMode = 2;
						}
					}
				} else if (actor != null) {
					if (FM.turret[0].target != null && !(FM.turret[0].target instanceof Aircraft) && !isAlive(FM.turret[0].target))
						FM.turret[0].target = null;
				}
			}
		}
	}
	
	public void update(float f) {
		setMissileLaunchThreatActive();
		laserUpdate();
		tiltRotor(f);
		OperatorTurret();
		guidedMissileUtils.update();
		if (obsMove < obsMoveTot && !bObserverKilled && !((FlightModelMain) (super.FM)).AS.isPilotParatrooper(1)) {
			if (obsMove < 0.2F || obsMove > obsMoveTot - 0.2F)
				obsMove += 0.29999999999999999D * (double) f;
			else if (obsMove < 0.1F || obsMove > obsMoveTot - 0.1F)
				obsMove += 0.15F;
			else
				obsMove += 1.2D * (double) f;
			obsLookAzimuth = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsAzimuthOld, obsAzimuth);
			obsLookElevation = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsElevationOld, obsElevation);
			hierMesh().chunkSetAngles("Head2_D0", 0.0F, obsLookAzimuth, obsLookElevation);
		}
		if (isAI) {
			stability();
		} else {
			human();
		}
		super.update(f);
	}
		
	public void human() {
        double kren = ((FlightModelMain) (super.FM)).Or.getKren();
        double tang = ((FlightModelMain) (super.FM)).Or.getTangage();
        /*Code above not used now, will be used for autopilot*/
        
        
        /** aile = aileron deflection for flightmodel*/
        /*  the two if's below it ensure it is within <-1;1>, forceTrim_x acts to basicaly re-center the cyclic*/
        float aile/*kren*/ = ((FlightModelMain) (super.FM)).CT.getAileron() + (float) forceTrim_x /*+ ((FlightModelMain) (super.FM)).CT.getTrimAileronControl()*/;
        //kren *= 35F;
        if(aile>1)aile=1;
        if(aile<-1)aile=-1;
        
        /** similar for elevator*/
        float elev/*tang*/ = ((FlightModelMain) (super.FM)).CT.getElevator() + (float) forceTrim_y /*+ ((FlightModelMain) (super.FM)).CT.getTrimElevatorControl()*/;
        if(elev>1)elev=1;
        if(elev<-1)elev=-1;
        //tang -= 0.05F;
        //tang *= tang >= 0.0F ? 35F : 50F;
        
        /** anti-torque*/
        /*  does not have force trim yet*/
        float rudd = ((FlightModelMain) (super.FM)).CT.getRudder() + ((FlightModelMain) (super.FM)).CT.getTrimRudderControl();

        /*trim setting code*/
        if(getTrim) /*getTrim goes true if Aux=20 is pressed*/
        {
        forceTrim_x=aile; /*inserts current aile value - that is, already re-centered by the force trim*/
        forceTrim_y=elev; /*similar for elev*/
        getTrim=false; /*sets the getTrim condition false so it stops after first cycle*/
        };
        

        /**Angular speeds and accelerations*/
        double Wx = ((FlightModelMain)(super.FM)).getW().x;
        double Wy = ((FlightModelMain)(super.FM)).getW().y;
        double Wz = ((FlightModelMain)(super.FM)).getW().z;
        

        double AWx = ((FlightModelMain)(super.FM)).getAW().x;
        double AWy = ((FlightModelMain)(super.FM)).getAW().y;
        double AWz = ((FlightModelMain)(super.FM)).getAW().z;
        
        
        
        /**collective is being read here*/
        /* it is an average of both pitch axes*/
        float aPitch = (((FlightModelMain) (super.FM)).EI.engines[0].getControlProp() + ((FlightModelMain) (super.FM)).EI.engines[1].getControlProp()) / 2.0F;
        /*Added this for later use ---^*/
//        vThrust.set(0.0F, 0.0F, /*(1.0F+aPitch)/2*/1F);
//        oMainRotor.set(0.0F, tang, kren);
//        oMainRotor.transform(vThrust);
//        ((FlightModelMain) (super.FM)).EI.engines[0].setVector(vThrust);
//        ((FlightModelMain) (super.FM)).EI.engines[1].setVector(vThrust);
        /**throttle not used now*/
//        float aThrottle = (((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle() + ((FlightModelMain) (super.FM)).EI.engines[1].getControlThrottle()) / 2.0F;
        float Engine1rpm = cvt(FM.EI.engines[0].getRPM(), 0.0F, 12500F, 0.0F, 1.0F);
        float Engine2rpm = cvt(FM.EI.engines[1].getRPM(), 0.0F, 12500F, 0.0F, 1.0F);
        //        float aThrottle = (Engine1rpm + Engine2rpm) / 2.0F;
        		
        float aThrottle = (float) Math.sqrt((Math.pow(Engine1rpm, 2.0F) + Math.pow(Engine2rpm, 2.0F)) / 2.0F);
        
//    	HUD.log(AircraftHotKeys.hudLogWeaponId, "PropAOA: " + ((FlightModelMain) (super.FM)).EI.engines[0].getPropAoA());
//        HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine rpm: " + aThrottle);
    	
        /**only thrust is, to set the rotor RPM (again, an average)*/
        /**rotor RPM code is simplified and is directly controled by
         * thrust, I wanted to add a full rotor RPM code,
         * giving the rotor certain inertia etc, + enabling autorotation
         * but that is not implemented yet*/
//        float aThrust = (((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() + ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput()) / 2.0F;
        float aThrust = aThrottle;

        /**helicopter speeds are loaded into vFlow*/
        Vector3d vFlow;
		vFlow=((FlightModelMain)(super.FM)).getVflow();
		double sinkRate = vFlow.z;
		
		/*old debug HUD.log*/
		//HUD.log(AircraftHotKeys.hudLogWeaponId, "sinkRate" + sinkRate);
		float airDensity = Atmosphere.density((float)((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).z);
        float antiSinkForce;
        float headOnForce;
        float sideForce;
        float tailRotorMoment;
        double rotorSurface = 20/*235*/;  //surface of main rotor
        double rotorSurface_cyclic = 10;//13.183; 
        /*(above) surface of main rotor that acts when cyclic is engaged
         * the whole rotor changes AoA by a sine function around the rotor trajectory
         * so when averaged out, it is roughly equal to full deflection being applied to half of the rotor surface*/
        double tailRotorSurface =1.35;  //surface of tail rotor
        double rotorCy = 1.3;           //drag coefficient for speeds perpendicular to rotors (sinking, rotation around vertical axis)
        

        
        double rotorCx = 0.002;         //drag for flow paralel to the rotors (rotor rotation, helicopter going forward/sideways, rotation around y axis)
        
        double rotorLineCx = 0.0006 * ((aPitch) * (aPitch) * 10 * 10); //increase over rotorCx when higher pitch (collective) is applied
        double tailRotorLineCx = 0.0006 * ((rudd) * (rudd) * 10 * 10); //similar for anti-torque
        double rotorCyDyn_0 = 0.18;         //actual lift coefficient for 0 AoA of rotor blades (used for both main rotor and tail rotor)
        double rotorCyDyn_line = 0.09;      //linear increase with higher pitch angle of rotor blades
        double rotorDiameter = 17.3;        //main rotor diameter, used for rotor speed calculation
        double tailRotorDiameter = 4;       //same for tail rotor
        double rotorRPM_max = 240;            //main rotor max RPM for the simplified code
        double rotor_tailRPM_max=1112;        //tail rotor max RPM
        double rotorRPM = (aThrust*rotorRPM_max/60)*(1-(aPitch*0.05));   
        /*max thrust is 1.0, meaning if thrust is max, revolutions are max too
         * it is impossible to fly the helicopter on one engine now, because the thrust value is average!
         * I should have done some square (or square root) average instead*/
        /*the RPM get decreased at higher collective*/
        double hubDirection_x = Math.toRadians(0);
        double hubDirection_y = Math.toRadians(5);
        /*default rotur hub direction, now 5° forward*/
        
        double rotorHeight = 2;  //height of hub above the CG, important for drag moments
        
        double rotorSpeed = 2*Math.PI*(rotorDiameter/2)*0.66*rotorRPM;  //finally, rotor speed
        /**the rotor speed can be summed up to single speed (it changes with distance from center of rotation)*/
        /*this speed is equivalent to speed at two thirds of the radius from the center*/

    	
        /**Autopilot code is calculated at this point**/
        /* it only reacts to angular speed at the moment*/
        /* the only purpose now is to prevent over-rolling*/
        /* the code itself is above Update*/
        double autoPitch = PitchAuto(-Wy);   
        double autoRoll = RollAuto (Wx);
        

        /**hub direction change (as difference to original position, not final value)*/
        
        double d_hubDirection_x = Math.toRadians(-(aile+autoRoll)*2);
        double d_hubDirection_y = Math.toRadians((elev+autoPitch)*5);
        
        
        
        /**L I F T S   A N D   D R A G S*/
        
        
        /*main rotor lift*/
        /**All come from
         * (1/2).C.S.ro.V^2*/
        /*C varies depending on deflection angles, S on rotor, V on RPM, ro = air density*/
        double rotorLift_dyn = 0.5 * (rotorCyDyn_0+ rotorCyDyn_line * 10 * aPitch) * rotorSurface * airDensity * rotorSpeed * rotorSpeed;
        /*torque created by rotor drag*/
        double rotorLift_moment_z = 0.5 * (rotorCx+rotorLineCx) * rotorSurface * airDensity * rotorSpeed * rotorSpeed;
        /*rotation moments*/
        double rotorLift_moment_y = -(rotorDiameter/2)*0.5 * 0.5 * (rotorCyDyn_line * 5 * (elev+autoPitch)) * rotorSurface_cyclic * airDensity * rotorSpeed * rotorSpeed;
        double rotorLift_moment_x = (rotorDiameter/2)*0.5 * 0.5 * (rotorCyDyn_line * 3 * (aile+autoRoll)) * rotorSurface_cyclic * airDensity * rotorSpeed * rotorSpeed;
        
        
        /*additional moment created by hub tilting*/
        rotorLift_moment_y += rotorLift_dyn*(rotorHeight*Math.sin(d_hubDirection_y));
        rotorLift_moment_x += rotorLift_dyn*(rotorHeight*Math.sin(d_hubDirection_x));
        
        
        /*the 2/3 radius speed on tail rotor*/
        double tailRotorSpeed = 2*Math.PI*(tailRotorDiameter/2)*0.66*aThrust*rotor_tailRPM_max/60;
        
        /*lift for anti-torque*/
        double tailRotorLift_dyn = 0.5*(rotorCyDyn_line * 10 * rudd) * airDensity * tailRotorSpeed * tailRotorSpeed;
        /*torque around Y axis*/
        double tailRotorLift_moment_y = 0.5*(tailRotorDiameter/2)*0.66 /*it acts 2/3 from center of rotation*/ * (rotorCx+tailRotorLineCx) * tailRotorSurface * airDensity * tailRotorSpeed * tailRotorSpeed;
        /*transfering the tailRotorLift_dyn into actual moment*/
        double tailRotorLift_moment_z = tailRotorLift_dyn*10;
        
        
        
        
        /**just declarations*/
        double dragMoment_x;
        double dragMoment_y;
        
        //super.update(f);
        
        double rotateSpeed_x;
        double rotateSpeed_y;
        double rotateSpeed_z;
        
        
        
        rotateSpeed_z = (Wz*(rotorDiameter/2)*0.5);
        
        rotateSpeed_y = (Wy*(rotorDiameter/2)*0.5);
        
        rotateSpeed_x = (Wx*(rotorDiameter/2)*0.5);
        
        double inertia_x = -((FlightModelMain) (super.FM)).M.getFullMass()*(AWx*(1/2)*0.033+Wx*(1/2))*3*3;
        double inertia_y = -((FlightModelMain) (super.FM)).M.getFullMass()*(AWy*(1/12)*0.033+Wy*(1/12))*17*17;
        double inertia_z = -((FlightModelMain) (super.FM)).M.getFullMass()*(AWz*(1/12)*0.033+Wz*(1/12))*17*17;
        
        
        /*these moments are caused by flow perpendicular to rotor blades as a helicopter rotates*/
        double balanceMoment_x = (rotorDiameter/2)*0.66*rotateSpeed_x*rotateSpeed_x*rotorSurface*airDensity*rotorCy*0.5;
        if(rotateSpeed_x<0){balanceMoment_x = 0-balanceMoment_x;};
        double balanceMoment_y = (rotorDiameter/2)*0.66*rotateSpeed_y*rotateSpeed_y*rotorSurface*airDensity*rotorCy*0.5;
        if(rotateSpeed_y<0){balanceMoment_y = 0-balanceMoment_y;};
        double balanceMoment_z = 10*rotateSpeed_z*rotateSpeed_z*tailRotorSurface*airDensity*rotorCy*0.5;
        if(rotateSpeed_z<0){balanceMoment_z = 0-balanceMoment_z;};
        

        /**this originally served to level the helicopter, it will be later used for autopilot code*/
        // G = 9.81 * ((FlightModelMain) (super.FM)).M.getFullMass();
        //double balanceMoment_G_x =0;// -Math.cos(Math.toRadians(tang))*G * (2*Math.sin(Math.toRadians(kren)));
        
        //double balanceMoment_G_y =0;// Math.cos(Math.toRadians(kren))*G * (/*Math.sqrt(4.25)*/2*Math.sin(Math.toRadians(/*14+*/tang)));
        //double balanceMoment_G_z =0;// Math.sin(Math.toRadians(kren))*G * (/*Math.sqrt(4.25)*/0*Math.sin(Math.toRadians(/*14+*/tang)));
        
        if(sinkRate>=0){
        antiSinkForce = -(float) (rotorCy * rotorSurface * airDensity * sinkRate * sinkRate);
        } /*force caused by flow perpendicular to rotor as helicopter sinks (or climbs)*/
        else
        {
        	antiSinkForce = (float) (rotorCy * rotorSurface * airDensity * sinkRate * sinkRate);
        };
        
        /**drag caused by flow in X axis on the main rotor*/
        /*it also produces a moment around Y axis*/
        
        /**These are all multiplied by 2 to better simulate:
         * loss of AoA when going up/down
         * dihedral of the rotor
         * fuselage drag*/
        if(vFlow.x>=0){
            headOnForce = -(float) ((rotorCx+rotorLineCx) * rotorSurface * airDensity * vFlow.x * vFlow.x);
            dragMoment_y =  -(float) 2*((rotorCx+rotorLineCx) * rotorSurface * airDensity * vFlow.x * vFlow.x);
            }
            else
            {
            	headOnForce = (float) ((rotorCx+rotorLineCx) * rotorSurface * airDensity * vFlow.x * vFlow.x);
            	dragMoment_y =  (float) 2*((rotorCx+rotorLineCx) * rotorSurface * airDensity * vFlow.x * vFlow.x);
            };
         /**similar*/
            if(vFlow.y>=0){
                sideForce = -(float) ((rotorCx+rotorLineCx+1) * rotorSurface * airDensity * vFlow.y * vFlow.y);
                tailRotorMoment = (float) (rotorCy * tailRotorSurface * airDensity * vFlow.y * vFlow.y)*10;
                dragMoment_x =  (float) 2*((rotorCx+rotorLineCx) * rotorSurface * airDensity * vFlow.y * vFlow.y);
                }
                else
                {
                	sideForce = (float) ((rotorCx+rotorLineCx+1) * rotorSurface * airDensity * vFlow.y * vFlow.y);
                	tailRotorMoment = -(float) (rotorCy * tailRotorSurface * airDensity * vFlow.y * vFlow.y)*10;
                	dragMoment_x =  -(float) 2*((rotorCx+rotorLineCx) * rotorSurface * airDensity * vFlow.y * vFlow.y);
                };
                
            /** code distributing lift forces into all three axes as the hub rotates*/
                /*(hubDirection_x+d_hubDirection_x) sums up default direction and direction difference*/
              double rotorLift_3D_x = Math.sin(hubDirection_y-d_hubDirection_y)*Math.cos(hubDirection_x+d_hubDirection_x)*rotorLift_dyn;
              double rotorLift_3D_y = Math.sin(hubDirection_x+d_hubDirection_x)*rotorLift_dyn;
              double rotorLift_3D_z = Math.cos(hubDirection_y-d_hubDirection_y)*Math.cos(hubDirection_x+d_hubDirection_x)*rotorLift_dyn;
                
                
        /**all forces and moments summed up and added to base flight model*/
              
              float antiLiftForce;
              if(sinkRate >= 1.0D)
              	antiLiftForce = (float)(0.5D * rotorCy * rotorSurface * (double)airDensity * sinkRate * sinkRate) * 20;
              else
              	antiLiftForce = 0;
              
              this.FM.producedAF.x += headOnForce+rotorLift_3D_x;
              this.FM.producedAF.y += sideForce+rotorLift_3D_y;
              this.FM.producedAF.z += antiSinkForce+rotorLift_3D_z-antiLiftForce;
              this.FM.producedAM.x += dragMoment_x - balanceMoment_x + rotorLift_moment_x;
              this.FM.producedAM.y += dragMoment_y + tailRotorLift_moment_y - balanceMoment_y + rotorLift_moment_y;
              this.FM.producedAM.z += tailRotorMoment - tailRotorLift_moment_z - balanceMoment_z + rotorLift_moment_z;
                      
        rotateSpeed_z = 0;
        rotateSpeed_y = 0;
        rotateSpeed_x = 0;
        headOnForce = 0;
        sideForce = 0;
        
        
        Vector3d localVector3d = new Vector3d();
        getSpeed(localVector3d);
        Point3d localPoint3d1 = new Point3d();
        pos.getAbs(localPoint3d1);
        float falt = (float)(FM.getAltitude() - World.land().HQ(localPoint3d1.x, localPoint3d1.y));
        if ((falt < 10.0F) && (FM.getSpeedKMH() < 60.0F) && (localVector3d.z < -1.0D))
        {
        	localVector3d.z *= 0.9D;
        	setSpeed(localVector3d);
        }
        if (this.FM.getVertSpeed() > 0.01F)
        {
        	float verSPDlimit = cvt(this.FM.getVertSpeed(), 1.0F, 10.0F, 1.0F, 0.95F);
        	localVector3d.z *= verSPDlimit;
        	setSpeed(localVector3d);
        }
        if(FM.getSpeedKMH() >= 270 && FM.EI.engines[0].getStage() > 5 && FM.EI.engines[1].getStage() > 5)
            FM.Sq.dragParasiteCx += 0.04F;
        if(FM.getSpeedKMH() >= 280 && FM.EI.engines[0].getStage() > 5 && FM.EI.engines[1].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.04F;
        if(FM.getSpeedKMH() >= 290 && FM.EI.engines[0].getStage() > 5 && FM.EI.engines[1].getStage() > 5)
        	FM.Sq.dragParasiteCx += 0.05F;
        
        float angleOfattackCx;
        if(this.FM.getAOA() >= 0.5)
        	angleOfattackCx = this.FM.getAOA() / 5;
        else
        	angleOfattackCx = 0;
        FM.Sq.dragParasiteCx += angleOfattackCx;
	}
		
	public double PitchAuto(double p) {
		p = -(p * 4);
		if (p >= 0.2)
			p = 0.2;
		if (p <= -0.2)
			p = -0.2;
		return p;
	}

	public double RollAuto(double k) {
		k = -(k * 4);
		if (k >= 0.2)
			k = 0.2;
		if (k <= -0.2)
			k = -0.2;
		return k;
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 0: // '\0'
			if (f < -80F) {
				f = -80F;
				flag = false;
			}
			if (f > 20F) {
				f = 20F;
				flag = false;
			}
			if (f1 < -60F) {
				f1 = -45F;
				flag = false;
			}
			if (f1 > 60F) {
				f1 = 60F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f < -80F) {
				f = -80F;
				flag = false;
			}
			if (f > 20F) {
				f = 20F;
				flag = false;
			}
			if (f1 < -60F) {
				f1 = -45F;
				flag = false;
			}
			if (f1 > 60F) {
				f1 = 60F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void auxPressed(int i) {
		super.auxPressed(i);
		if (i == 20)
			HUD.log(AircraftHotKeys.hudLogWeaponId, "Trim: Set");
		getTrim = true;
		// float ctrlZ = ((FlightModelMain) (super.FM)).CT.getRudder();
		// float trimZ = ((FlightModelMain)
		// (super.FM)).CT.getTrimRudderControl();
		// ((FlightModelMain) (super.FM)).CT.setTrimRudderControl(ctrlZ -
		// trimZ);
		if (i == 21) {
			forceTrim_x = 0.0D;
			forceTrim_y = 0.0D;
			forceTrim_z = 0.0D;
			HUD.log(AircraftHotKeys.hudLogWeaponId, "Trim: Reset");
		}
		if (i == 22){
			repMod++;
			if(repMod > 3)
				repMod = 0;
			if (repMod == 0) HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: None");
			if (repMod == 1) HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For Ground Targets");
			if (repMod == 2) HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For Air Targets");
//			if (repMod == 3) HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Navigation Info");
		}
		if (i == 23) {
				if (!bManualFire) {
					bManualFire = true;
					HUD.log(AircraftHotKeys.hudLogWeaponId, "9K133 Fire Control: Pilot");

				} else if (bManualFire) {
					bManualFire = false;
					laserOn = false;
					Reflection.setInt(guidedMissileUtils, "engageMode", 1);
					HUD.log(AircraftHotKeys.hudLogWeaponId, "9K133 Fire Control: Operator");
				}
		}
		if (i == 24) {
			if(bManualFire) {
				if (!laserOn) {
					laserOn = true;
					laserLock = false;
					HUD.log(AircraftHotKeys.hudLogWeaponId, "Raduga-Sh: On");

				} else if (laserOn) {
					laserOn = false;
					laserLock = false;
					HUD.log(AircraftHotKeys.hudLogWeaponId, "Raduga-Sh: Off");
				}
			}
		}
	}
	
    private void checkAmmo()
    {
        missilesList.clear();
        for(int i = 0; i < FM.CT.Weapons.length; i++)
        {
            if(FM.CT.Weapons[i] == null)
                continue;
            for(int j = 0; j < FM.CT.Weapons[i].length; j++)
            {
                if(!FM.CT.Weapons[i][j].haveBullets())
                    continue;
                if(FM.CT.Weapons[i][j] instanceof RocketGunSturmV)
                {
                    missilesList.add(FM.CT.Weapons[i][j]);
                }
            }        
        }

    }
	
    private void doOperatorFireMissile() {
    	victim.pos.getAbs(point3dop);
    	Mi24V.spot.set(point3dop);
    	boolean isReady = (FM.getAOA() < (FM.Skill + 3)) && (FM.getOverload() < (FM.Skill + 1));
    	if(missilesList.isEmpty()) {
    		return;
    	} else if (Actor.isAlive(victim) && Time.current() > missileLaunchInterval && isReady) {
    		guidedMissileUtils.update();
    		missileLaunchInterval = Time.current() + 20000L + (4 - FM.Skill) * 1000L;
    		((RocketGunSturmV)missilesList.remove(0)).shots(1);
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Missile Gone!");
    		return;
    	}   
    }
    
    private void doLaunchMissileAI() {
    	if(isAI && !missilesList.isEmpty() && Time.current() > missileLaunchInterval)
    	{
    		if((((Maneuver) FM).get_maneuver() == 7 || ((Maneuver) FM).get_maneuver() == 43) && ((Maneuver) FM).target_ground != null)
    		{
    			((RocketGunSturmV)missilesList.remove(0)).shots(1);
    			missileLaunchInterval = Time.current() + 20000L + (4 - FM.Skill) * 1000L;
    			Voice.speakAttackByRockets(this);
    		}
    	}
    }
	
    private long missileLaunchInterval;
	private ArrayList missilesList;
	Actor victim;
	private boolean bManualFire;
	private boolean bShotFlare;
	
	private int repMod;
	private int counter = 0;
	
    private Vector3d vector3dop = new Vector3d();
    private Point3d  point3dop  = new Point3d();

	private Point3d localPoint3d1 = new Point3d();
    private boolean landing;
    private boolean AltCheck;
    private boolean isAirstart;
	private boolean hover;
	private float gndYaw;
	private float gndRoll;
	private float gndPitch;
    private boolean isAI;
    private boolean tookOff;
    private boolean TailRotorDestroyed;
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
	private float rotorRPM;
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
	private Sample smplSirena;
	private float curAngleRotor;
	private float diffAngleRotor;
	private long lastTimeFan;
	public double forceTrim_x;
	public double forceTrim_y;
	public double forceTrim_z;
	public boolean getTrim;

	static {
		Class class1 = com.maddox.il2.objects.air.Mi24V.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Mi-24V");
		Property.set(class1, "meshName", "3DO/Plane/Mi-24V/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "yearService", 1950F);
		Property.set(class1, "yearExpired", 1960.5F);
		Property.set(class1, "FlightModel", "FlightModels/Mi-24V.fmd:HELIFMD");
		Property.set(class1, "cockpitClass", new Class[] {
				com.maddox.il2.objects.air.CockpitMi24.class,
				com.maddox.il2.objects.air.CockpitMi24_GUNNER.class,
				com.maddox.il2.objects.air.CockpitMi24_FLIR.class });
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 9, 9,
				2, 2, 2, 2, 7, 7, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 3,
				3, 3, 3, 9, 9 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01",
				"_BombSpawn01", "_ExternalDev01", "_ExternalDev02",
				"_ExternalDev03", "_ExternalDev04", "_ExternalRock01",
				"_ExternalRock02", "_ExternalRock03", "_ExternalRock04",
				"_Flare01", "_Flare02", "_ExternalDev05", "_ExternalDev06",
				"_ExternalRock05", "_ExternalRock06", "_ExternalRock07",
				"_ExternalRock08", "_ExternalRock09", "_ExternalRock10",
				"_ExternalRock11", "_ExternalRock12", "_ExternalDev07",
				"_ExternalDev08", "_ExternalDev09", "_ExternalDev10",
				"_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03",
				"_ExternalBomb04", "_ExternalDev11", "_ExternalDev12" });
		try {
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xFAB-250M46";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
			a_lweaponslot[1] = null;
			a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46",
					1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46",
					1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4xFAB-250M46";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
			a_lweaponslot[1] = null;
			a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46",
					1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46",
					1);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46",
					1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46",
					1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xRBK-250";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
			a_lweaponslot[1] = null;
			a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
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
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xPTB";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
			a_lweaponslot[1] = null;
			a_lweaponslot[30] = new Aircraft._WeaponSlot(9,
					"FuelTankGun_PTB800", 1);
			a_lweaponslot[31] = new Aircraft._WeaponSlot(9,
					"FuelTankGun_PTB800", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xPTB+4xSturm-V";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunYakB", 1470);
			a_lweaponslot[1] = null;
			a_lweaponslot[30] = new Aircraft._WeaponSlot(9,
					"FuelTankGun_PTB800", 1);
			a_lweaponslot[31] = new Aircraft._WeaponSlot(9,
					"FuelTankGun_PTB800", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareHeli",
					35);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSturm", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(4, "RocketGunSturmV",
					1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "None";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = null;
			a_lweaponslot[1] = null;
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
		} catch (Exception exception) {
		}
	}
}
