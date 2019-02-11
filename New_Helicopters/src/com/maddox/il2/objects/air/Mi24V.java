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
import com.maddox.il2.ai.ground.Aim;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.buildings.Plate;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.trains.Wagon;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketGunSturmV;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.sound.*;
import com.maddox.util.HashMapInt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class Mi24V extends Scheme2 implements TypeHelicopter, TypeStormovikArmored,
		TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeLaserSpotter, TypeFighterAceMaker {

	public Mi24V() {
		sndTrim = this.newSound("cockpit.trimmer", false);
		sndProp = this.newSound("propeller.mi24", true);
		bAPazimut = false;
		bAPkrentang = true;
		bAPalt = false;
		apALltPitch = 0.0F;
		apAzimut = 0.0F;
	    apKren = 0.0F;
	    apTang = 0.0F;
		forceTrim_x = 0.0D;
		forceTrim_y = 0.0D;
		forceTrim_z = 0.0D;
		getTrim = false;
		curAngleRotor = 0.0F;
		lastTimeFan = Time.current();
		rocketHookSelected = 2;
		suka = new Loc();
		aPitch = 0.0F;
		aOldPitch = 0.0F;
		rotorRPM = 0.0D;
		tailRotorRPM = 0.0D;
		reductorRPM = 0.0D;
		engineRPM = 0.0D;
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
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
	}
	

	public void rotorSound() {
		this.sndProp.setParent(this.getRootFX());
		this.sndProp.setPosition(this.FM.EI.engines[0].getEnginePos());
		this.sndProp.setControl(100, (float) rotorRPM);
		this.sndProp.setControl(108, aPitch);
	}
	
	public void dustEmit() {
		float alt = FM.getAltitude() - Landscape.HQ_Air((float) this.FM.Loc.x, (float) this.FM.Loc.y);
		float f = (float) (cvt(alt, 0F, 20F, TrueRandom.nextFloat(0.4F, 0.9F), 0.01F) * (rotorRPM / 240D));
		if (alt < 20F && rotorRPM > 80) {
			Loc locDust00 = new Loc();
			HookNamed hookDust00 = new HookNamed(this, "_Dust00");
			locDust00.set(0.0D, alt + 3, 0.0D, 0.0F, 0.0F, 0.0F);
			hookDust00.computePos(this, _tmpLoc, locDust00);
			locDust00.getPoint().z = Landscape.HQ_Air((float) locDust00.getPoint().x, (float) locDust00.getPoint().y);
			if (Engine.cur.land.isWater(locDust00.getPoint().x, locDust00.getPoint().y)) {
				Eff3DActor.New(locDust00, f, "3DO/Effects/Aircraft/GrayGroundDust2.eff", TrueRandom.nextFloat(0.2F));
			} else {
				Eff3DActor.New(locDust00, f, "3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage != 1 ? "1" : "2") + ".eff", TrueRandom.nextFloat(0.2F));
			}

			Loc locDust01 = new Loc();
			HookNamed hookDust01 = new HookNamed(this, "_Dust01");
			locDust01.set(0.0D, alt + 3, 0.0D, 0.0F, 0.0F, 0.0F);
			hookDust01.computePos(this, _tmpLoc, locDust01);
			locDust01.getPoint().z = Landscape.HQ_Air((float) locDust01.getPoint().x, (float) locDust01.getPoint().y);
			if (Engine.cur.land.isWater(locDust01.getPoint().x, locDust01.getPoint().y)) {
				Eff3DActor.New(locDust01, f, "3DO/Effects/Aircraft/GrayGroundDust2.eff", TrueRandom.nextFloat(0.2F));
			} else {
				Eff3DActor.New(locDust01, f, "3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage != 1 ? "1" : "2") + ".eff", TrueRandom.nextFloat(0.2F));
			}

			Loc locDust02 = new Loc();
			HookNamed hookDust02 = new HookNamed(this, "_Dust02");
			locDust02.set(0.0D, alt + 3, 0.0D, 0.0F, 0.0F, 0.0F);
			hookDust02.computePos(this, _tmpLoc, locDust02);
			locDust02.getPoint().z = Landscape.HQ_Air((float) locDust02.getPoint().x, (float) locDust02.getPoint().y);
			if (Engine.cur.land.isWater(locDust02.getPoint().x, locDust02.getPoint().y)) {
				Eff3DActor.New(locDust02, f, "3DO/Effects/Aircraft/GrayGroundDust2.eff", TrueRandom.nextFloat(0.2F));
			} else {
				Eff3DActor.New(locDust02, f, "3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage != 1 ? "1" : "2") + ".eff", TrueRandom.nextFloat(0.2F));
			}

			Loc locDust03 = new Loc();
			HookNamed hookDust03 = new HookNamed(this, "_Dust03");
			locDust03.set(0.0D, alt + 3, 0.0D, 0.0F, 0.0F, 0.0F);
			hookDust03.computePos(this, _tmpLoc, locDust03);
			locDust03.getPoint().z = Landscape.HQ_Air((float) locDust03.getPoint().x, (float) locDust03.getPoint().y);
			if (Engine.cur.land.isWater(locDust03.getPoint().x, locDust03.getPoint().y)) {
				Eff3DActor.New(locDust03, f, "3DO/Effects/Aircraft/GrayGroundDust2.eff", TrueRandom.nextFloat(0.2F));
			} else {
				Eff3DActor.New(locDust03, f, "3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage != 1 ? "1" : "2") + ".eff", TrueRandom.nextFloat(0.2F));
			}
		}
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
    		if (!FM.Gears.onGround()) {
    			aPitch = 0.75F;
    			engineRPM = 15000;
    			reductorRPM = 228D;
    			rotorRPM = 228D;
    			FM.CT.setPowerControl(1.0F);
    		}
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
        	Reflection.setFloat(FM.EI.engines[0], "thrustMax", 7000.0F);
        	Reflection.setFloat(FM.EI.engines[0], "engineAcceleration", 4.0F);
        	Reflection.setFloat(FM.EI.engines[0], "propReductor", 1.0F);
      	
            this.FM.EI.engines[1].setVector(eVect);
            this.FM.EI.engines[1].setPos(ePos);
            this.FM.EI.engines[1].setPropPos(ePropPos);
        	
        	Reflection.setFloat(FM.EI.engines[1], "tChangeSpeed", 0.000001F);
        	Reflection.setFloat(FM.EI.engines[1], "thrustMax", 7000.0F);
        	Reflection.setFloat(FM.EI.engines[1], "engineAcceleration", 4.0F);
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
        	polares.Cy0_max = 7.15F;
        	polares.FlapsMult = 1.0F;
        	polares.FlapsAngSh = 4.0F;
        	polares.lineCyCoeff = 0.29F;
        	polares.AOAMinCx_Shift = 0.0F;
        	polares.Cy0_0 = 5.53F;
        	polares.AOACritH_0 = 10.0F;
        	polares.AOACritL_0 = -6.0F;
        	polares.CyCritH_0 = 7.9934692F;
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
        	if(rotorRPM > 100F)
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
        
        if(hierMesh().isChunkVisible("Prop1_D1"))
        {
            hierMesh().chunkVisible("Prop1_D0", false);
            hierMesh().chunkVisible("PropRot1_D0", false);
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
        diffAngleRotor = (float) ((6D * rotorRPM * (curTime - lastTimeFan)) / 1000D);
        curAngleRotor += diffAngleRotor;
        diffAngleTailRotor = (float) ((6D * tailRotorRPM * (curTime - lastTimeFan)) / 1000D);
        curAngleTailRotor += diffAngleTailRotor;
        lastTimeFan = curTime;
        hierMesh().chunkSetAngles("Prop1_D0", -curAngleRotor % 360F, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Prop2_D0", 0.0F, 0.0F, -curAngleTailRotor % 360F);
		if(TailRotorDestroyed)
    	{
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
                        getEnergyPastArmor(7.070000171661377D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                        shot.powerType = 0;
                        break;

                    case 2: // '\002'
                    case 3: // '\003'
                        getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
                        shot.powerType = 0;
                        if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)
                            doRicochet(shot);
                        break;

                    case 4: // '\004'
                        if(((Tuple3d) (point3d)).x > -1.3500000000000001D)
                        {
                            getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
                            shot.powerType = 0;
                            if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)
                                doRicochet(shot);
                        } else
                        {
                            getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                        }
                        break;

                    case 5: // '\005'
                    case 6: // '\006'
                        getEnergyPastArmor(20.200000762939453D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
                        if(shot.power > 0.0F)
                            break;
                        if(Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)
                            doRicochet(shot);
                        else
                            doRicochetBack(shot);
                        break;

                    case 7: // '\007'
                        getEnergyPastArmor(20.200000762939453D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F)
                            doRicochetBack(shot);
                        break;
                    }
                }
                if(s.startsWith("xxarmorc1"))
                    getEnergyPastArmor(7.070000171661377D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.startsWith("xxarmort1"))
                    getEnergyPastArmor(6.059999942779541D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                return;
            }
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 1 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.10000000149011612D && getEnergyPastArmor(3.4000000953674316D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
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
                    if(chunkDamageVisible("WingLMid") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
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
                    if(chunkDamageVisible("WingRMid") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
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
                    if(chunkDamageVisible("WingLOut") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
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
                    if(chunkDamageVisible("WingROut") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(6.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(6.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
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
                    if(Math.abs(((Tuple3d) (point3d)).y) < 0.1379999965429306D && getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
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
                    int j = s.charAt(9) - 49;
                    debuggunnery("Engine Module: Magneto " + j + " Hit, Magneto " + j + " Disabled..");
                    ((FlightModelMain) (super.FM)).EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
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
                int k = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(((FlightModelMain) (super.FM)).AS.astateTankStates[k] == 0)
                    {
                        debuggunnery("Fuel System: Fuel Tank " + k + " Pierced..");
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 2, 1);
                        ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, k, 1);
                    } else
                    if(((FlightModelMain) (super.FM)).AS.astateTankStates[k] == 1)
                    {
                        debuggunnery("Fuel System: Fuel Tank " + k + " Pierced..");
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 2, 1);
                        ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, k, 2);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 2, 2);
                        debuggunnery("Fuel System: Fuel Tank " + k + " Pierced, State Shifted..");
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
                    if(((Tuple3d) (point3d)).z < -0.59099999999999997D || ((Tuple3d) (point3d)).z > 0.40799999237060547D && ((Tuple3d) (point3d)).x > 0.0D)
                    {
                        getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)
                            doRicochet(shot);
                    } else
                    {
                        getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)
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
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
                if(shot.power <= 0.0F)
                    doRicochet(shot);
            } else
            if(((Tuple3d) (point3d)).x > 0.53700000000000003D || ((Tuple3d) (point3d)).x < -0.10000000000000001D)
            {
                getEnergyPastArmor(0.20000000298023224D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(shot.power <= 0.0F)
                    doRicochetBack(shot);
            } else
            {
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
                if(shot.power <= 0.0F)
                    doRicochet(shot);
            }
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xeng"))
        {
            if(((Tuple3d) (point3d)).z > 0.159D)
                getEnergyPastArmor((double)(1.25F * World.Rnd().nextFloat(0.95F, 1.12F)) / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
            else
            if(((Tuple3d) (point3d)).x > 1.335D && ((Tuple3d) (point3d)).x < 2.3860000000000001D && ((Tuple3d) (point3d)).z > -0.059999999999999998D && ((Tuple3d) (point3d)).z < 0.064000000000000001D)
                getEnergyPastArmor(0.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
            else
            if(((Tuple3d) (point3d)).x > 2.5299999999999998D && ((Tuple3d) (point3d)).x < 2.992D && ((Tuple3d) (point3d)).z > -0.23499999999999999D && ((Tuple3d) (point3d)).z < 0.010999999999999999D)
                getEnergyPastArmor(4.0399999618530273D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
            else
            if(((Tuple3d) (point3d)).x > 2.5590000000000002D && ((Tuple3d) (point3d)).z < -0.59499999999999997D)
                getEnergyPastArmor(4.0399999618530273D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
            else
            if(((Tuple3d) (point3d)).x > 1.849D && ((Tuple3d) (point3d)).x < 2.2509999999999999D && ((Tuple3d) (point3d)).z < -0.70999999999999996D)
                getEnergyPastArmor(4.0399999618530273D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
            else
            if(((Tuple3d) (point3d)).x > 3.0030000000000001D)
                getEnergyPastArmor(World.Rnd().nextFloat(2.3F, 3.2F), shot);
            else
            if(((Tuple3d) (point3d)).z < -0.60600000619888306D)
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
            else
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
            if(Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D && (shot.power <= 0.0F || World.Rnd().nextFloat() < 0.1F))
                doRicochet(shot);
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xtail"))
            hitChunk("Tail1", shot);
        else
        if(s.startsWith("xkeel"))
        {
        	if(chunkDamageVisible("Keel1") < 2) {
        		hitChunk("Keel1", shot);
        		TailRotorDamage();
        	}
        } else
        if(s.startsWith("xrudder") && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
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
            int l;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                l = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                l = s.charAt(6) - 49;
            } else
            {
                l = s.charAt(5) - 49;
            }
            hitFlesh(l, shot, byte0);
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
		if(TailRotorDestroyed) this.FM.producedAM.z += 100000;
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
		if (isAI) stability(); else human();
		if(k14Mode == 1)
        {		
			Vector3d vTmp1 = new Vector3d();
			Point3d pTmp1 = new Point3d();
			Point3d pTmp2 = new Point3d();
			vTmp1.set(1.0D, 0.0D, 0.0D);
			FM.Or.transform(vTmp1);
			vTmp1.scale(6000.0D);
			pTmp1.set(FM.Loc);
			pTmp1.add(vTmp1);
			
			Engine.land();
			if (Landscape.rayHitHQ(FM.Loc, pTmp1, pTmp2)) {
				k14Distance = (float) pos.getAbsPoint().distance(pTmp2);
			}
        }
		rotorSound();
		dustEmit();
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
		super.update(f);
	}
		
	public void human() {
		
		if (FM.EI.getCurControl(0) && !FM.EI.getCurControl(1)) FM.EI.engines[1].setControlProp(FM.EI.engines[0].getControlProp());
		if (FM.EI.getCurControl(1) && !FM.EI.getCurControl(0)) FM.EI.engines[0].setControlProp(FM.EI.engines[1].getControlProp());
		
		if(bAPkrentang) {
			if (FM.CT.getElevator() > 0.07F || FM.CT.getElevator() < -0.07F) apTang = -FM.Or.getTangage();
			if (FM.CT.getAileron() > 0.07F || FM.CT.getAileron() < -0.07F) apKren = -FM.Or.getKren();
			FM.CT.setTrimElevatorControl(cvt(-FM.Or.getTangage(), apTang - 7F, apTang + 7F, -0.2F, 0.2F));
			FM.CT.setTrimAileronControl(cvt(-FM.Or.getKren(), apKren - 15F, apKren + 15F, -0.2F, 0.2F));
		} else {
			FM.CT.setTrimElevatorControl(0.0F);
			FM.CT.setTrimAileronControl(0.0F);
		}
        float aileT = FM.CT.getAileron() + (float)forceTrim_x;
        float aile = aileT + FM.CT.getTrimAileronControl();
        if(aile > 1.0F)
            aile = 1.0F;
        if(aile < -1F)
            aile = -1F;
        float elevT = FM.CT.getElevator() + (float)forceTrim_y;
        float elev = elevT + FM.CT.getTrimElevatorControl();
        if(elev > 1.0F)
            elev = 1.0F;
        if(elev < -1F)
            elev = -1F;
        
		if(bAPazimut) {
			if (FM.CT.getRudder() > 0.07F || FM.CT.getRudder() < -0.07F) apAzimut = -FM.Or.getAzimut();
			FM.CT.setTrimRudderControl(cvt(-FM.Or.getAzimut(), apAzimut - 10F, apAzimut + 10F, -0.2F, 0.2F));
		} else {
			FM.CT.setTrimRudderControl(0.0F);
		}
        
		float ruddT = FM.CT.getRudder() +  + (float)forceTrim_z;
        float rudd = ruddT + FM.CT.getTrimRudderControl();
        if(rudd > 1.0F)
        	rudd = 1.0F;
        if(rudd < -1F)
        	rudd = -1F;
        if(getTrim)
        {
            forceTrim_x = aileT;
            forceTrim_y = elevT;
            forceTrim_z = ruddT;
            getTrim = false;
        }
        double Wx = FM.getW().x;
        double Wy = FM.getW().y;
        double Wz = FM.getW().z;
        double AWx = FM.getAW().x;
        double AWy = FM.getAW().y;
        double AWz = FM.getAW().z;
        if(bAPalt) {
        	apALltPitch = (cvt(FM.getAltitude(), apAlt - 20F, apAlt + 20F, 0.2F, -0.2F));
        } else {
        	apALltPitch = 0.0F;
        }
        float inPitch = (FM.EI.engines[0].getControlProp() + FM.EI.engines[1].getControlProp()) / 2.0F;
        float aPitchT = aOldPitch;
        if (aPitchT < inPitch) aPitchT += (0.007F - (rotorRPM / 120000D));
        if (aPitchT > inPitch) aPitchT -= (0.007F - (rotorRPM / 120000D));
        aOldPitch = aPitchT;
        aPitch = aPitchT + apALltPitch;
        if(aPitch > 1.0F) aPitch = 1.0F;
        Vector3d vFlow = super.FM.getVflow();
        double sinkRate = ((Tuple3d) (vFlow)).z;
        float falt = (float)(FM.getAltitude() - Landscape.HQ_Air((float) FM.Loc.x, (float) FM.Loc.y));
        float airDensity = Atmosphere.density((float)((Tuple3d) (FM.Loc)).z);
        double rotorSurface = 20D;
        double rotorSurface_cyclic = 10D;
        double tailRotorSurface = 1.3500000000000001D;
        double rotorCy = 1.3D;
        double rotorCx = 0.02D;
        double rotorLineCx = 0.00075 * (double)(aPitch * aPitch * 12F * 12F);
        double tailRotorLineCx = 0.00075D * (double)(rudd * rudd * 10F * 10F);
        double spdFlowCoef_0 = (double) cvt((float) vFlow.x, 5.5F, 22.2F, 1.0F, 1.3F);
        double altFlowCoef_1 = (double) cvt(falt, 2.0F, 12F, cvt((float) vFlow.x, 5.5F, 22.2F, 1.2F, 1.0F), 1.0F);
        double rotorCyDyn_0 = 0.0198D * altFlowCoef_1 * spdFlowCoef_0 * airDensity; //0.022D
        double rotorCyDyn_line = 0.0765D * altFlowCoef_1 * spdFlowCoef_0 * airDensity; //0.085D
        if (sinkRate < 0.0D && Math.abs(vFlow.x) < Math.abs(sinkRate)) {
        	((RealFlightModel)FM).producedShakeLevel += 0.005F * Math.abs(sinkRate);
        	if (sinkRate < 3.0D) {
        		rotorCyDyn_0 -= Math.abs(sinkRate) * 0.001D;
        		rotorCyDyn_line -= Math.abs(sinkRate) * 0.001D;
        	}
        }
        double rotorDiameter = 17.3D;
        double tailRotorDiameter = 3.0D;
        float fAOA = this.FM.getAOA();
        float fuselageCxS = (5 - cvt((float) vFlow.x, 41.6F, 83.3F, 0F, 4.0F));
        float fuselageCyS = 20;
        double load = (FM.M.getFullMass() - FM.M.massEmpty) * airDensity;
        double bladeAOACx = aPitch > 0.7D - (load / 27000D) ? (load / 50000D) * ((aPitch - (0.7D  - (load / 27000D))) / (0.3D + (load / 27000))) * (rotorRPM / 240D) : 0D;
        double bladeCx = FM.Or.getTangage() > 0.0 ? ((vFlow.x + vFlow.z) * Math.abs(FM.Or.getTangage())) * 0.0001D : -(((vFlow.x + vFlow.z) * Math.abs(FM.Or.getTangage()) * 0.0001D) / 3);
        float flowRPM = cvt((float) vFlow.x, 0.0F, 27F, 0.0F, (float) ((200D - (double) (Math.abs(fAOA) * 3D)) - (sinkRate > 0D ? sinkRate * 12D : 0D)));
        engineRPM = Math.sqrt((Math.pow(FM.EI.engines[0].getRPM(), 2D) + Math.pow(FM.EI.engines[1].getRPM(), 2D)) / 2D);
        reductorRPM = engineRPM * 0.016D > flowRPM ? engineRPM * 0.016D > 228 ? 228 : engineRPM * 0.016D : flowRPM;
        HUD.training("flowRPM " + flowRPM);
        double tempRPM = reductorRPM * ((1.0D + bladeCx) - bladeAOACx);
        rotorRPM += rotorRPM < tempRPM ? 0.005 + (reductorRPM * 0.0007D) : -(0.005 + (reductorRPM * 0.0007D));
        double hubDirection_x = Math.toRadians(0.0D);
        double hubDirection_y = Math.toRadians(5D);
        double rotorHeight = 2D;
        double rotorSpeed = 6.2831853071795862D * (rotorDiameter / 2D) * 0.5D * (rotorRPM / 60D);
        double autoPitch = PitchAuto(-Wy);
        double autoRoll = RollAuto(Wx);
        double d_hubDirection_x = Math.toRadians(-((double)aile + autoRoll) * 2D);
        double d_hubDirection_y = Math.toRadians(((double)elev + autoPitch) * 5D);
        double rotorLift_dyn = 0.5D * (rotorCyDyn_0 + rotorCyDyn_line * 12D * (double)aPitch) * rotorSurface * (double)airDensity * rotorSpeed * rotorSpeed;
        double rotorLift_moment_z = 0.5D * (rotorCx + rotorLineCx * 1.2D * (double)aPitch) * rotorSurface * (double)airDensity * rotorSpeed * rotorSpeed;
        double rotorLift_moment_y = -(rotorDiameter / 2D) * 0.5D * 0.5D * (rotorCyDyn_line * 5D * ((double)elev + autoPitch)) * rotorSurface_cyclic * (double)airDensity * rotorSpeed * rotorSpeed;
        double rotorLift_moment_x = (rotorDiameter / 2D) * 0.5D * 0.5D * (rotorCyDyn_line * 3D * ((double)aile + autoRoll)) * rotorSurface_cyclic * (double)airDensity * rotorSpeed * rotorSpeed;
        rotorLift_moment_y += rotorLift_dyn * (rotorHeight * Math.sin(d_hubDirection_y));
        rotorLift_moment_x += rotorLift_dyn * (rotorHeight * Math.sin(d_hubDirection_x));  
        tailRotorRPM = engineRPM * 0.07413D;
        double tailRotorSpeed = 6.2831853071795862D * (tailRotorDiameter / 2D) * 0.5000000000000003D * (tailRotorRPM / 60D);
        double tailRotorLift_dyn = 0.5D * (rotorCyDyn_line * 10D * (double)rudd) * (double)airDensity * tailRotorSpeed * tailRotorSpeed;
        double tailRotorLift_moment_y = 0.5D * (tailRotorDiameter / 2D) * 0.5000000000000003D * (rotorCx + tailRotorLineCx) * tailRotorSurface * (double)airDensity * tailRotorSpeed * tailRotorSpeed;
        double tailRotorLift_moment_z = tailRotorLift_dyn * 10D;
        double rotateSpeed_z = Wz * (rotorDiameter / 2D) * 0.5D;
        double rotateSpeed_y = Wy * (rotorDiameter / 2D) * 0.5D;
        double rotateSpeed_x = Wx * (rotorDiameter / 2D) * 0.5D;
        double inertia_x = (double)(-FM.M.getFullMass()) * (AWx * 0.0D * 0.033000000000000002D + Wx * 0.0D) * 3D * 3D;
        double inertia_y = (double)(-FM.M.getFullMass()) * (AWy * 0.0D * 0.033000000000000002D + Wy * 0.0D) * 17D * 17D;
        double inertia_z = (double)(-FM.M.getFullMass()) * (AWz * 0.0D * 0.033000000000000002D + Wz * 0.0D) * 17D * 17D;
        double balanceMoment_x = (rotorDiameter / 2D) * 0.66000000000000003D * rotateSpeed_x * rotateSpeed_x * rotorSurface * (double)airDensity * rotorCy * 0.5D;
        if(rotateSpeed_x < 0.0D)
            balanceMoment_x = 0.0D - balanceMoment_x;
        double balanceMoment_y = (rotorDiameter / 2D) * 0.66000000000000003D * rotateSpeed_y * rotateSpeed_y * rotorSurface * (double)airDensity * rotorCy * 0.5D;
        if(rotateSpeed_y < 0.0D)
            balanceMoment_y = 0.0D - balanceMoment_y;
        double balanceMoment_z = 10D * rotateSpeed_z * rotateSpeed_z * tailRotorSurface * (double)airDensity * rotorCy * 0.5D;
        if(rotateSpeed_z < 0.0D)
            balanceMoment_z = 0.0D - balanceMoment_z;
        float antiSinkForce;
        if(sinkRate >= 0.0D)
            antiSinkForce = -(float)((rotorCy * rotorSurface + fuselageCyS) * (double)airDensity * sinkRate * sinkRate);
        else
            antiSinkForce = (float)((rotorCy * rotorSurface + fuselageCyS) * (double)airDensity * sinkRate * sinkRate);
        float headOnForce;
        double dragMoment_y;
        if(((Tuple3d) (vFlow)).x >= 0.0D)
        {
            headOnForce = -(float)(((rotorCx + rotorLineCx) * rotorSurface+fuselageCxS) * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
            dragMoment_y = -2D * ((rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
        } else
        {
            headOnForce = (float)(((rotorCx + rotorLineCx) * rotorSurface+fuselageCxS) * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
            dragMoment_y = 2D * ((rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).x * ((Tuple3d) (vFlow)).x);
        }
        float sideForce;
        float tailRotorMoment;
        double dragMoment_x;
        if(((Tuple3d) (vFlow)).y >= 0.0D)
        {
            sideForce = -(float)(((rotorCx + rotorLineCx) * rotorSurface + fuselageCyS) * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
            tailRotorMoment = -(float)(rotorCy * tailRotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y) * 10F;
            dragMoment_x = 2D * ((rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
        } else
        {
            sideForce = (float)(((rotorCx + rotorLineCx) * rotorSurface + fuselageCyS) * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
            tailRotorMoment = (float)(rotorCy * tailRotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y) * 10F;
            dragMoment_x = -2D * ((rotorCx + rotorLineCx) * rotorSurface * (double)airDensity * ((Tuple3d) (vFlow)).y * ((Tuple3d) (vFlow)).y);
        }
        double rotorLift_3D_x = Math.sin(hubDirection_y - d_hubDirection_y) * Math.cos(hubDirection_x + d_hubDirection_x) * rotorLift_dyn;
        double rotorLift_3D_y = Math.sin(hubDirection_x + d_hubDirection_x) * rotorLift_dyn;
        double rotorLift_3D_z = Math.cos(hubDirection_y - d_hubDirection_y) * Math.cos(hubDirection_x + d_hubDirection_x) * rotorLift_dyn;
        float antiLiftForce;
        if(sinkRate >= 1.0D)
            antiLiftForce = (float)(0.5D * rotorCy * rotorSurface * (double)airDensity * sinkRate * sinkRate) * 20F;
        else
            antiLiftForce = 0.0F;
        FM.producedAF.x += (double)headOnForce + rotorLift_3D_x;
        FM.producedAF.y += (double)sideForce + rotorLift_3D_y;
        FM.producedAF.z += ((double)antiSinkForce + rotorLift_3D_z) - (double)antiLiftForce;
        FM.producedAM.x += (double)dragMoment_x - balanceMoment_x+ rotorLift_moment_x;
        FM.producedAM.y += (double)((dragMoment_y + tailRotorLift_moment_y) - balanceMoment_y) + rotorLift_moment_y;
        FM.producedAM.z += !TailRotorDestroyed ? FM.producedAM.z += tailRotorMoment - tailRotorLift_moment_z - balanceMoment_z + rotorLift_moment_z : rotorRPM * 50000 + aPitch * 50000;
        FM.Vwld.z *= sinkRate > 0.0 ? cvt((float) sinkRate, 1.0F, 10.0F, 1.0F, 0.95F) : 1.0;
        FM.Sq.dragParasiteCx += fAOA > 0.0F ? fAOA / 5 : 0;
                      
        rotateSpeed_z = 0;
        rotateSpeed_y = 0;
        rotateSpeed_x = 0;
        headOnForce = 0;
        sideForce = 0;
        
        if(rotorRPM > 0) {
        	float shakeMe = 0F;
        	float shakeRPM = (float) ((rotorRPM / 2.4D) * 0.01D);
        	if (vFlow.x < 22.2D && vFlow.x > 11.1D) {
        		shakeMe = cvt((float) vFlow.x, 11.1F, 22.2F, 0.03F, shakeRPM * 0.010F + (aPitch * shakeRPM) * 0.015F);
        	} else if (vFlow.x < 11.1D) {
        		shakeMe = cvt((float) vFlow.x, 5.5F, 11.1F, shakeRPM * 0.010F + (aPitch * shakeRPM) * 0.015F, 0.03F);
        	}
        	((RealFlightModel)FM).producedShakeLevel += shakeMe;
        }
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
		if (i == 20) {
			HUD.log(AircraftHotKeys.hudLogWeaponId, "Trim: Set");
			sndTrim.cancel();
			sndTrim.setParent(getRootFX());
			sndTrim.play(pos.getAbsPoint());
			getTrim = true;
		}
		if (i == 21) {
			forceTrim_x = 0.0D;
			forceTrim_y = 0.0D;
			forceTrim_z = 0.0D;
			HUD.log(AircraftHotKeys.hudLogWeaponId, "Trim: Reset");
			sndTrim.cancel();
			sndTrim.setParent(getRootFX());
			sndTrim.play(pos.getAbsPoint());
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
		if (i == 27) {
			if (!bAPazimut) {
				bAPazimut = true;
				apAzimut = -FM.Or.getAzimut();
				HUD.log(AircraftHotKeys.hudLogWeaponId, "AP: Heading: ON");
			} else if (bAPazimut) {
				bAPazimut = false;
				HUD.log(AircraftHotKeys.hudLogWeaponId, "AP: Heading: OFF");
			}
		}
		if (i == 28) {
			if (!bAPalt) {
				bAPalt = true;
				apAlt = FM.getAltitude();
				HUD.log(AircraftHotKeys.hudLogWeaponId, "AP: Altitude: ON");
			} else if (bAPalt) {
				bAPalt = false;
				HUD.log(AircraftHotKeys.hudLogWeaponId, "AP: Altitude: OFF");
			}
		}
		if (i == 29) {
			if (!bAPkrentang) {
				bAPkrentang = true;
				HUD.log(AircraftHotKeys.hudLogWeaponId, "AP: Pitch + Roll: ON");
			} else if (bAPkrentang) {
				bAPkrentang = false;
				HUD.log(AircraftHotKeys.hudLogWeaponId, "AP: Pitch + Roll: OFF");
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
    
    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        if(k14Mode == 0)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: Navigation");
        } else
        if(k14Mode == 1)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: Unguided Rocket");
        } else
        if(k14Mode == 2 && FM.actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: Unguided Bomb");
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset()
    {
    }

    public void typeFighterAceMakerAdjDistancePlus()
    {
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
    }

    public void typeFighterAceMakerAdjSideslipReset()
    {
    }

    public void typeFighterAceMakerAdjSideslipPlus()
    {
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte(k14Mode);
        netmsgguaranted.writeByte(k14WingspanType);
        netmsgguaranted.writeFloat(k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        k14Mode = netmsginput.readByte();
        k14WingspanType = netmsginput.readByte();
        k14Distance = netmsginput.readFloat();
    }
    
    public float aPitch;
    public float aOldPitch;
    
    public double engineRPM;
    public double reductorRPM;
    public double rotorRPM;
    public double tailRotorRPM;
    
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    
//    private Sample spProp;
    protected SoundFX sndProp;
    
    protected SoundFX sndTrim;
    
    private boolean bAPazimut;
    private boolean bAPalt;
    private boolean bAPkrentang;
    
    private float apALltPitch;
    private float apAlt;
    private float apKren;
    private float apTang;
    private float apAzimut;
    
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
	private float curAngleTailRotor;
	private float diffAngleTailRotor;
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
