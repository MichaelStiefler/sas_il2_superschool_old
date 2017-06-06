
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapExt;
import com.maddox.sas1946.il2.util.Reflection;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;


public class F_14 extends Scheme2
    implements TypeSupersonic, TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeGSuit, TypeFastJet, TypeBomber, TypeStormovikArmored, TypeAcePlane, TypeLaserDesignator, TypeRadar, TypeFuelDump
{

    public float getDragForce(float f, float f1, float f2, float f3)
    {
        throw new UnsupportedOperationException("getDragForce not supported anymore.");
    }

    public float getDragInGravity(float f, float f1, float f2, float f3, float f4, float f5)
    {
        throw new UnsupportedOperationException("getDragInGravity supported anymore.");
    }

    public float getForceInGravity(float f, float f1, float f2)
    {
        throw new UnsupportedOperationException("getForceInGravity supported anymore.");
    }

    public float getDegPerSec(float f, float f1)
    {
        throw new UnsupportedOperationException("getDegPerSec supported anymore.");
    }

    public float getGForce(float f, float f1)
    {
        throw new UnsupportedOperationException("getGForce supported anymore.");
    }

    public F_14()
    {
        bForceTakeoffElTrim = false;
        bForceFlapmodeAuto = false;
        oldTrimElevator = 0.0F;
        elevatorsField = null;
        lLightHook = new Hook[4];
        SonicBoom = 0.0F;
        oldthrl = -1F;
        curthrl = -1F;
        k14Mode = 2;
        k14WingspanType = 0;
        k14Distance = 200F;
        overrideBailout = false;
        ejectComplete = false;
        lightTime = 0.0F;
        ft = 0.0F;
        mn = 0.0F;
        ts = false;
        ictl = false;
        engineSurgeDamage = 0.0F;
        hasHydraulicPressure = true;
        super.bWantBeaconKeys = true;
        lTimeNextEject = 0L;
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
        bToFire = false;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 3000F;
        fSightCurSpeed = 200F;
        fSightCurReadyness = 0.0F;
        arrestor = 0.0F;
        trimauto = false;
        t1 = 0L;
        tangate = 0.0F;
        azimult = 0.0F;
        tf = 0L;
        APmode1 = false;
        APmode2 = false;
        APmode3 = false;
        radartogle = false;
        v = 0.0F;
        h = 0.0F;
        lockmode = 0;
        radargunsight = 0;
        leftscreen = 2;
        Bingofuel = 1000;
        radarrange = 1;
        Nvision = false;
        fxRWR = newSound("aircraft.RWR2", false);
        smplRWR = new Sample("RWR2.wav", 256, 65535);
        RWRSoundPlaying = false;
        fxMissileWarning = newSound("aircraft.MissileMissile", false);
        smplMissileWarning = new Sample("MissileMissile.wav", 256, 65535);
        MissileSoundPlaying = false;
        misslebrg = 0.0F;
        aircraftbrg = 0.0F;
        thrustMaxField = new Field[2];
        bHasCenterTank = false;
        bHasWingTank = false;
        bTakeoffFlapAssist = false;
        bSpawnedAsAI = false;
        bTakeoffAIAssist = false;
        bStableAIGround = false;
        arrestor = 0.0F;
        autoEng = false;
        curFlaps = 0.0F;
        desiredPosition = 0.0F;
        tVarWingInput = -1L;
        oldVarWingControlSwitch = 0;
        bFlapsOutFixed = false;
        bFlapsInFixed = false;
        stockCy0_0 = 0.12F;
        stockCy0_1 = 0.60F;
        stockCxMin_0 = 0.039F;
        stockCxMin_1 = 0.10F;
        stockCyCritH_0 = 1.4F;
        stockGCenter = 0.0F;
        stockDefaultElevatorTrim = 0.0F;
        stockparabCxCoeff_0 = 0.0004F;
        stockparabCxCoeff_1 = 0.0005F;
        stockSqWing = 54.5F;
        stockSqWingOut = 13.25F;
        stockSqFlaps = 13.3F;
        antiColLight = new Eff3DActor[3];
        laserSpotPos = new Point3d();
        laserTimer = -1L;
    }

    private static final float toMeters(float f)
    {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f)
    {
        return 0.4470401F * f;
    }

    public float checkfuel(int i)
    {
        FuelTank afueltank[] = FM.CT.getFuelTanks();
        if(afueltank.length == 0 || FM.M.bFuelTanksDropped)
        {
            return 0.0F;
        } else
        {
            Fuelamount = afueltank[i].checkFuel();
            return Fuelamount;
        }
    }

    private void checkDroptanks()
    {
        FuelTank afueltank[] = FM.CT.getFuelTanks();
        if(afueltank.length == 1 || afueltank.length == 3)
            bHasCenterTank = true;
        if(afueltank.length == 2 || afueltank.length == 3)
            bHasWingTank = true;
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
            if(!radartogle)
            {
                radartogle = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar ON");
                radarmode = 0;
            } else
            {
                radartogle = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar OFF");
            }
        if(i == 21)
            if(!Nvision)
            {
                Nvision = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Nvision ON");
            } else
            {
                Nvision = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Nvision OFF");
            }
        if(i == 22)
        {
            lockmode++;
            if(lockmode > 1)
                lockmode = 0;
        }
        if(i == 23)
        {
            radargunsight++;
            if(radargunsight > 3)
                radargunsight = 0;
            if(radargunsight == 0)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: funnel");
            if(radargunsight == 1)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: Radar ranging");
            if(radargunsight == 2)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: Unguided Rocket");
            if(radargunsight == 3)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: Ground");
        }
        if(i == 24)
        {
            leftscreen++;
            if(leftscreen > 2)
                leftscreen = 0;
            if(leftscreen == 0)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: Fuel");
            else
            if(leftscreen == 1)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: FPAS");
            else
            if(leftscreen == 2)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: Engine");
        }
        if(i == 25)
        {
            Bingofuel += 500;
            if(Bingofuel > 6000)
                Bingofuel = 1000;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Bingofuel  " + Bingofuel);
        }
        if(i == 26)
        {
            if(hold && t1 + 200L < Time.current() && FLIR)
            {
                hold = false;
                holdFollow = false;
                HUD.log("Laser Pos Unlock");
                t1 = Time.current();
            }
            if(!hold && t1 + 200L < Time.current() && FLIR)
            {
                hold = true;
                holdFollow = false;
                actorFollowing = null;
                HUD.log("Laser Pos Lock");
                t1 = Time.current();
            }
            if(!FLIR)
                setLaserOn(false);
        }
        if(i == 27)
        {
            if(holdFollow && t1 + 200L < Time.current() && FLIR)
            {
                hold = false;
                holdFollow = false;
                actorFollowing = null;
                HUD.log("Laser Track Unlock");
                t1 = Time.current();
            }
            if(!holdFollow && t1 + 200L < Time.current() && FLIR)
            {
                hold = false;
                holdFollow = true;
                actorFollowing = null;
                HUD.log("Laser Track Lock");
                t1 = Time.current();
            }
        }
        if(i == 28)
            if(!ILS)
            {
                ILS = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ILS ON");
            }
            else
            {
                ILS = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ILS OFF");
            }
        if(i == 30)
            if(!APmode2)
            {
                APmode2 = true;
                HUD.log("Autopilot Mode: Direction ON");
                FM.AP.setStabDirection(true);
                FM.CT.bHasRudderControl = false;
            } else
            if(APmode2)
            {
                APmode2 = false;
                HUD.log("Autopilot Mode: Direction OFF");
                FM.AP.setStabDirection(false);
                FM.CT.bHasRudderControl = true;
            }
    }

    private boolean RWRWarning()
    {
        boolean flag = false;
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = War.getNearestEnemy(this, 6000F);
        if(aircraft != null)
        {
            double d = Main3D.cur3D().land2D.worldOfsX() + ((Actor) (aircraft)).pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft)).pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft)).pos.getAbsPoint().z;
            double d3 = d2 - (double)Landscape.Hmin((float)((Actor) (aircraft)).pos.getAbsPoint().x, (float)((Actor) (aircraft)).pos.getAbsPoint().y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            Aircraft aircraft1 = War.getNearestEnemy(aircraft, 6000F);
            boolean flag1;
            if((aircraft1 instanceof Aircraft) && aircraft.getArmy() != World.getPlayerArmy() && (aircraft instanceof TypeFighterAceMaker) && ((aircraft instanceof TypeSupersonic) || (aircraft instanceof TypeFastJet)) && aircraft1 == World.getPlayerAircraft() && aircraft1.getSpeed(vector3d) > 20D)
            {
                pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + ((Actor) (aircraft1)).pos.getAbsPoint().x;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft1)).pos.getAbsPoint().y;
                double d8 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft1)).pos.getAbsPoint().z;
                int k = (int)(Math.floor(((Actor) (aircraft1)).pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
                int i1 = (int)(Math.floor((aircraft1.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                double d10 = (int)(Math.ceil((d2 - d8) / 10D) * 10D);
                boolean flag2 = false;
                int k1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(((Actor) (aircraft1)).pos.getAbsPoint().x), Engine.land().WORLD2PIXY(((Actor) (aircraft1)).pos.getAbsPoint().y));
                float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                if(k1 >= 28 && k1 < 32 && f < 7.5F)
                    flag2 = true;
                double d14 = d4 - d;
                double d16 = d6 - d1;
                float f1 = 57.32484F * (float)Math.atan2(d16, -d14);
                int l1 = (int)(Math.floor((int)f1) - 90D);
                if(l1 < 0)
                    l1 = 360 + l1;
                int i2 = l1 - i;
                double d19 = d - d4;
                double d20 = d1 - d6;
                Random random = new Random();
                float f3 = ((float)random.nextInt(20) - 10F) / 100F + 1.0F;
                int i3 = random.nextInt(6) - 3;
                float f4 = 19000F;
                float f5 = f4;
                if(d3 < 1200D)
                    f5 = (float)(d3 * 0.80000001192092896D * 3D);
                int j3 = (int)(Math.ceil(Math.sqrt((d20 * d20 + d19 * d19) * (double)f3) / 10D) * 10D);
                if((float)j3 > f4)
                    j3 = (int)(Math.ceil(Math.sqrt(d20 * d20 + d19 * d19) / 10D) * 10D);
                float f6 = 57.32484F * (float)Math.atan2(j3, d10);
                int k3 = (int)(Math.floor((int)f6) - 90D);
                int l3 = (k3 - (90 - j)) + i3;
                int i4 = (int)f4;
                if((float)j3 < f4)
                    if(j3 > 1150)
                        i4 = (int)(Math.ceil((double)j3 / 900D) * 900D);
                    else
                        i4 = (int)(Math.ceil((double)j3 / 500D) * 500D);
                int j4 = i2 + i3;
                int k4 = j4;
                if(k4 < 0)
                    k4 += 360;
                float f7 = (float)((double)f5 + Math.sin(Math.toRadians(Math.sqrt(i2 * i2) * 3D)) * ((double)f5 * 0.25D));
                int l4 = (int)((double)f7 * Math.cos(Math.toRadians(l3)));
                if((double)j3 <= (double)l4 && (double)j3 <= 14000D && (double)j3 >= 200D && l3 >= -30 && l3 <= 30 && Math.sqrt(j4 * j4) <= 60D)
                    flag1 = true;
                else
                    flag1 = false;
            } else
            {
                flag1 = false;
            }
            Aircraft aircraft2 = World.getPlayerAircraft();
            double d5 = Main3D.cur3D().land2D.worldOfsX() + ((Actor) (aircraft1)).pos.getAbsPoint().x;
            double d7 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft1)).pos.getAbsPoint().y;
            double d9 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft1)).pos.getAbsPoint().z;
            int j1 = (int)(-((double)((Actor) (aircraft2)).pos.getAbsOrient().getYaw() - 90D));
            if(j1 < 0)
                j1 = 360 + j1;
            if(flag1 && aircraft1 == World.getPlayerAircraft() && (aircraft1 instanceof F_14))
            {
                pos.getAbs(point3d);
                double d11 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                double d12 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
                double d13 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                double d15 = (int)(Math.ceil((d9 - d13) / 10D) * 10D);
                String s = "";
                if(d9 - d13 - 500D >= 0.0D)
                    s = " low";
                if((d9 - d13) + 500D < 0.0D)
                    s = " high";
                double d17 = d11 - d5;
                double d18 = d12 - d7;
                float f2 = 57.32484F * (float)Math.atan2(d18, -d17);
                int j2 = (int)(Math.floor((int)f2) - 90D);
                if(j2 < 0)
                    j2 = 360 + j2;
                int k2 = j2 - j1;
                if(k2 < 0)
                    k2 = 360 + k2;
                int l2 = (int)(Math.ceil((double)(k2 + 15) / 30D) - 1.0D);
                if(l2 < 1)
                    l2 = 12;
                double d21 = d5 - d11;
                double d22 = d7 - d12;
                double d23 = Math.ceil(Math.sqrt(d22 * d22 + d21 * d21) / 10D) * 10D;
                if(bMissileWarning)
                {
                    bRadarWarning = false;
                    playRWRWarning();
                } else
                {
                    bRadarWarning = d23 <= 8000D && d23 >= 500D && Math.sqrt(d15 * d15) <= 6000D;
                    aircraftbrg = cvt(l2, 0.0F, 12F, 0.0F, 360F);
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Enemy at " + l2 + " o'clock" + s + "!");
                }
                playRWRWarning();
            } else
            {
                bRadarWarning = false;
                playRWRWarning();
                aircraftbrg = 0.0F;
            }
        } else
        {
            bRadarWarning = false;
            playRWRWarning();
            aircraftbrg = 0.0F;
        }
        return true;
    }

    private boolean RWRLaunchWarning()
    {
        Point3d point3d = new Point3d();
        pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Object obj;
        if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))
            obj = World.getPlayerAircraft();
        else
            obj = this;
        super.pos.getAbs(point3d);
        Object obj1;
        if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))
            obj1 = World.getPlayerAircraft();
        else
            obj1 = this;
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Actor)(Actor)obj).pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Actor)(Actor)obj).pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Actor)(Actor)obj).pos.getAbsPoint().z;
        int i = (int)(-((double)((Actor)(Actor)obj1).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i = 360 + i;
        List list = Engine.missiles();
        int j = list.size();
        if(j == 0 && (bMissileWarning || backfire))
        {
            bMissileWarning = false;
            playRWRWarning();
            backfire = false;
            misslebrg = 0.0F;
            return false;
        }
        for(int k = 0; k < j; k++)
        {
            Actor actor = (Actor)list.get(k);
            if(((actor instanceof Missile) || (actor instanceof MissileSAM)) && actor.getSpeed(vector3d) > 20D && ((Missile)actor).getMissileTarget() == this)
            {
                pos.getAbs(point3d);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                String s = "";
                if(d2 - d5 - 500D >= 0.0D)
                    s = " LOW";
                if((d2 - d5) + 500D < 0.0D)
                    s = " HIGH";
                double d7 = d3 - d;
                double d8 = d4 - d1;
                float f = 57.32484F * (float)Math.atan2(d8, -d7);
                int i1 = (int)(Math.floor((int)f) - 90D);
                if(i1 < 0)
                    i1 = 360 + i1;
                int j1 = i1 - i;
                if(j1 < 0)
                    j1 = 360 + j1;
                int k1 = (int)(Math.ceil((double)(j1 + 15) / 30D) - 1.0D);
                if(k1 < 1)
                    k1 = 12;
                double d9 = d - d3;
                double d10 = d1 - d4;
                double d11 = Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D;
                bMissileWarning = true;
                if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))
                {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "MISSILE AT " + k1 + " O'CLOCK" + s + "!!!" + misslebrg);
                    playRWRWarning();
                    misslebrg = cvt(k1, 0.0F, 12F, 0.0F, 360F);
                }
                if((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode()) && (FM instanceof Maneuver))
                    backfire = true;
            } else
            {
                bMissileWarning = false;
                playRWRWarning();
                backfire = false;
                misslebrg = 0.0F;
            }
        }

        return true;
    }

    public void playRWRWarning()
    {
        if(bRadarWarning && !fxRWR.isPlaying())
            fxRWR.start();
        else
        if(!bRadarWarning && fxRWR.isPlaying())
            fxRWR.stop();
        if(bMissileWarning && !fxMissileWarning.isPlaying())
        {
            fxMissileWarning.start();
            fxRWR.stop();
        } else
        if(!bMissileWarning && fxMissileWarning.isPlaying())
            fxMissileWarning.stop();
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
        if(FLIR)
        {
            azimult++;
            tf = Time.current();
        } else
        if(radartogle && lockmode == 0)
            h += 0.0035F;
    }

    public void typeBomberAdjDistanceMinus()
    {
        if(FLIR)
        {
            azimult--;
            tf = Time.current();
        } else
        if(radartogle && lockmode == 0)
            h -= 0.0035F;
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
        if(FLIR)
        {
            tangate++;
            tf = Time.current();
        } else
        if(radartogle && lockmode == 0)
            v += 0.0035F;
    }

    public void typeBomberAdjSideslipMinus()
    {
        if(FLIR)
        {
            tangate--;
            tf = Time.current();
        } else
        if(radartogle && lockmode == 0)
            v -= 0.0035F;
    }

    public void updatecontrollaser()
    {
        if(tf + 5L <= Time.current())
        {
            tangate = 0.0F;
            azimult = 0.0F;
        }

        if(!FLIR && laserTimer > 0L && Time.current() > laserTimer && getLaserOn())
        {
            setLaserOn(false);
        }

        if(bHasPaveway)
            checkgroundlaser();
    }

    private void checkgroundlaser()
    {
        boolean laseron = false;
        double targetDistance = 0.0D;
        float targetAngle = 0.0F;
        float targetBait = 0.0F;
        float maxTargetBait = 0.0F;
        // superior the Laser spot of this Paveway's owner than others'
        while(getLaserOn())
        {
            Point3d point3d = new Point3d();
            point3d = getLaserSpot();
            if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) < 1.0F)
                break;
            targetDistance = this.pos.getAbsPoint().distance(point3d);
            if (targetDistance > maxPavewayDistance)
                break;
            targetAngle = angleBetween(this, point3d);
            if (targetAngle > maxPavewayFOVfrom)
                break;

            laseron = true;
            break;
        }
        // seak other Laser designator spots when Paveway's owner doesn't spot Laser
        if(!laseron)
        {
            List list = Engine.targets();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                Actor actor = (Actor)list.get(j);
                if((actor instanceof TypeLaserDesignator) && ((TypeLaserDesignator) actor).getLaserOn() && actor.getArmy() == this.getArmy())
                {
                    Point3d point3d = new Point3d();
                    point3d = ((TypeLaserDesignator)actor).getLaserSpot();
                    // Not target about objects behind of clouds from the Paveway's seaker.
                    if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) < 1.0F)
                        continue;
                    targetDistance = this.pos.getAbsPoint().distance(point3d);
                    if (targetDistance > maxPavewayDistance)
                        continue;
                    targetAngle = angleBetween(this, point3d);
                    if (targetAngle > maxPavewayFOVfrom)
                        continue;

                    targetBait = 1 / targetAngle / (float) (targetDistance * targetDistance);
                    if (targetBait <= maxTargetBait)
                        continue;

                    maxTargetBait = targetBait;
                    laseron = true;
                }
            }
        }
        setLaserArmEngaged(laseron);
    }

    private static float angleBetween(Actor actorFrom, Point3d pointTo) {
        float angleRetVal = 180.1F;
        double angleDoubleTemp = 0.0D;
        Loc angleActorLoc = new Loc();
        Point3d angleActorPos = new Point3d();
        Vector3d angleTargRayDir = new Vector3d();
        Vector3d angleNoseDir = new Vector3d();
        actorFrom.pos.getAbs(angleActorLoc);
        angleActorLoc.get(angleActorPos);
        angleTargRayDir.sub(pointTo, angleActorPos);
        angleDoubleTemp = angleTargRayDir.length();
        angleTargRayDir.scale(1.0D / angleDoubleTemp);
        angleNoseDir.set(1.0D, 0.0D, 0.0D);
        angleActorLoc.transform(angleNoseDir);
        angleDoubleTemp = angleNoseDir.dot(angleTargRayDir);
        angleRetVal = Geom.RAD2DEG((float) Math.acos(angleDoubleTemp));
        return angleRetVal;
    }

    public Point3d getLaserSpot()
    {
        return laserSpotPos;
    }

    public boolean setLaserSpot(Point3d p3d)
    {
        laserSpotPos = p3d;
        return true;
    }

    public boolean getLaserOn()
    {
        return bLaserOn;
    }

    public boolean setLaserOn(boolean flag)
    {
        if(bLaserOn != flag)
        {
            if(bLaserOn == false)
            {
                if(FM.actor == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: ON");
            }
            else
            {
                if(FM.actor == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: OFF");
                hold = false;
                holdFollow = false;
                actorFollowing = null;
            }
        }

        return bLaserOn = flag;
    }

    public boolean getLaserArmEngaged()
    {
        return bLGBengaged;
    }

    public boolean setLaserArmEngaged(boolean flag)
    {
        if(bLGBengaged != flag)
        {
            if(bLGBengaged == false)
            {
                if(this == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser Bomb: Engaged");
            }
            else
            {
                if(this == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser Bomb: Disengaged");
            }
        }

        return bLGBengaged = flag;
    }

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
        if(FLIR)
            if(!APmode3)
            {
                APmode3 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Engaged");
                ((FlightModelMain) (FM)).AP.setStabAltitude(1000F);
            } else
            if(APmode3)
            {
                APmode3 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Released");
                ((FlightModelMain) (FM)).AP.setStabAltitude(false);
            }
    }

    public void typeBomberAdjAltitudeMinus()
    {
    }

    public void typeBomberAdjSpeedReset()
    {
    }

    public void typeBomberAdjSpeedPlus()
    {
        radarrange++;
        if(radarrange > 5)
            radarrange = 5;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeBomberAdjSpeedMinus()
    {
        radarrange--;
        if(radarrange < 1)
            radarrange = 1;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeBomberUpdate(float f)
    {
        if((double)Math.abs(FM.Or.getKren()) > 4.5D)
        {
            fSightCurReadyness -= 0.0666666F * f;
            if(fSightCurReadyness < 0.0F)
                fSightCurReadyness = 0.0F;
        }
        if(fSightCurReadyness < 1.0F)
            fSightCurReadyness += 0.0333333F * f;
        else
        if(bSightAutomation)
        {
            fSightCurDistance -= toMetersPerSecond(fSightCurSpeed) * f;
            if(fSightCurDistance < 0.0F)
            {
                fSightCurDistance = 0.0F;
                typeBomberToggleAutomation();
            }
            fSightCurForwardAngle = (float)Math.toDegrees(Math.atan(fSightCurDistance / toMeters(fSightCurAltitude)));
            if((double)fSightCurDistance < (double)toMetersPerSecond(fSightCurSpeed) * Math.sqrt(toMeters(fSightCurAltitude) * 0.2038736F))
                bSightBombDump = true;
            if(bSightBombDump)
                if(FM.isTick(3, 0))
                {
                    if(FM.CT.Weapons[3] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets())
                    {
                        FM.CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else
                {
                    FM.CT.WeaponControl[3] = false;
                }
        }
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte((bSightAutomation ? 1 : 0) | (bSightBombDump ? 2 : 0));
        netmsgguaranted.writeFloat(fSightCurDistance);
        netmsgguaranted.writeByte((int)fSightCurForwardAngle);
        netmsgguaranted.writeByte((int)((fSightCurSideslip + 3F) * 33.33333F));
        netmsgguaranted.writeFloat(fSightCurAltitude);
        netmsgguaranted.writeByte((int)(fSightCurSpeed / 2.5F));
        netmsgguaranted.writeByte((int)(fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        int i = netmsginput.readUnsignedByte();
        bSightAutomation = (i & 1) != 0;
        bSightBombDump = (i & 2) != 0;
        fSightCurDistance = netmsginput.readFloat();
        fSightCurForwardAngle = netmsginput.readUnsignedByte();
        fSightCurSideslip = -3F + (float)netmsginput.readUnsignedByte() / 33.33333F;
        fSightCurAltitude = netmsginput.readFloat();
        fSightCurSpeed = (float)netmsginput.readUnsignedByte() * 2.5F;
        fSightCurReadyness = (float)netmsginput.readUnsignedByte() / 200F;
    }

    private void laser(Point3d point3d)
    {
        point3d.z = World.land().HQ(point3d.x, point3d.y);
        Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(point3d.x, point3d.y, point3d.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
        eff3dactor.postDestroy(Time.current() + 1500L);
    }

    private void FLIR()
    {
        List list = Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if(!(actor instanceof Aircraft) && !(actor instanceof ArtilleryGeneric) && !(actor instanceof CarGeneric) && !(actor instanceof TankGeneric) || (actor instanceof StationaryGeneric) || (actor instanceof TypeLaserDesignator) || actor.pos.getAbsPoint().distance(pos.getAbsPoint()) >= 20000D)
                continue;
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            actor.pos.getAbs(point3d, orient);
//            l.set(point3d, orient);
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

    public boolean typeDiveBomberToggleAutomation()
    {
        return true;
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
    }


    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.AS.wantBeaconsNet(true);
        FM.Skill = 3;
        FM.turret[0].bIsAIControlled = false;
        FM.CT.bHasBombSelect = true;
        actl = FM.SensRoll;
        ectl = FM.SensPitch;
        rctl = FM.SensYaw;
        t1 = Time.current();
        FM.CT.bHasAntiColLights = true;
        FM.CT.bHasFormationLights = true;
        if(FM instanceof RealFlightModel)
            Reflection.invokeMethod(FM, "init_G_Limits");
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        stockCy0_0 = polares.Cy0_0;
        stockCy0_1 = polares.Cy0_1;
        stockCxMin_0 = polares.CxMin_0;
        stockCxMin_1 = polares.CxMin_1;
        stockCyCritH_0 = polares.CyCritH_0;
        stockGCenter = Reflection.getFloat(FM, "GCenter");
        stockDefaultElevatorTrim = Reflection.getFloat(FM, "DefaultElevatorTrim");
        stockparabCxCoeff_0 = polares.parabCxCoeff_0;
        stockparabCxCoeff_1 = polares.parabCxCoeff_1;
        stockSqWing = FM.Sq.squareWing;
        stockSqWingOut = FM.Sq.liftWingLOut;
        stockSqFlaps = FM.Sq.squareFlaps;
        FM.CT.toggleRocketHook();
        bCarryLaserpod = false;
        if(thisWeaponsName.startsWith("Fighter: 4xAIM54"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon2L", true);
            hierMesh().chunkVisible("Pylon2R", true);
            FM.Sq.dragParasiteCx += 0.0001F;
        }
        if(thisWeaponsName.startsWith("Fighter: 6xAIM54"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon2L", true);
            hierMesh().chunkVisible("Pylon2R", true);
            FM.Sq.dragParasiteCx += 0.0001F;
        }
        if(thisWeaponsName.startsWith("Fighter: 2xAIM54"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            FM.Sq.dragParasiteCx += 0.00007F;
        }
        if(thisWeaponsName.startsWith("Recon: 2xAIM54"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            FM.Sq.dragParasiteCx += 0.00007F;
        }
        if(thisWeaponsName.startsWith("GAttack: 2xMk"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            FM.Sq.dragParasiteCx += 0.00007F;
        }
        if(thisWeaponsName.startsWith("GAttackFLIR: ALQ"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            FM.Sq.dragParasiteCx += 0.00007F;
            bCarryLaserpod = true;
        }
        if(thisWeaponsName.startsWith("Recon: ALQ"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            FM.Sq.dragParasiteCx += 0.00007F;
        }
        if(thisWeaponsName.startsWith("GAttackFLIR: 2xGBU"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            FM.Sq.dragParasiteCx += 0.00007F;
            bCarryLaserpod = true;
        }
        if(thisWeaponsName.startsWith("GAttack: 2xCBU"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            FM.Sq.dragParasiteCx += 0.00007F;
        }
        if(thisWeaponsName.startsWith("GAttack: 4xMk"))
         {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon2L", true);
            hierMesh().chunkVisible("Pylon2R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            hierMesh().chunkVisible("Pylon4L", true);
            hierMesh().chunkVisible("Pylon4R", true);
            FM.Sq.dragParasiteCx += 0.0001F;
        }
        if(thisWeaponsName.startsWith("GAttack: 4xCBU"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon2L", true);
            hierMesh().chunkVisible("Pylon2R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            hierMesh().chunkVisible("Pylon4L", true);
            hierMesh().chunkVisible("Pylon4R", true);
            FM.Sq.dragParasiteCx += 0.0001F;
        }
        if(thisWeaponsName.startsWith("GAttackFLIR: 4xGBU"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon2L", true);
            hierMesh().chunkVisible("Pylon2R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            hierMesh().chunkVisible("Pylon4L", true);
            hierMesh().chunkVisible("Pylon4R", true);
            FM.Sq.dragParasiteCx += 0.0001F;
            bCarryLaserpod = true;
        }
    }

    public void missionStarting()
    {
        super.missionStarting();
        checkDroptanks();
        if(!(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))
            bSpawnedAsAI = true;

        tVarWingInput = -1L;
        laserTimer = -1L;
        bLaserOn = false;
        FLIR = false;
    }

    public void updateLLights()
    {
        super.pos.getRender(Actor._tmpLoc);
        if(lLight == null)
        {
            if(Actor._tmpLoc.getX() >= 1.0D)
            {
                lLight = new LightPointWorld[4];
                for(int i = 0; i < 4; i++)
                {
                    lLight[i] = new LightPointWorld();
                    lLight[i].setColor(1.0F, 1.0F, 1.0F);
                    lLight[i].setEmit(0.0F, 0.0F);
                    try
                    {
                        lLightHook[i] = new HookNamed(this, "_LandingLight0" + i);
                    }
                    catch(Exception exception) { }
                }

            }
        } else
        {
            for(int j = 0; j < 4; j++)
            {
                if(FM.AS.astateLandingLightEffects[j] != null)
                {
                    lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP1);
                    lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP2);
                    if(Landscape.rayHitHQ(lLightP1, lLightP2, lLightPL))
                    {
                        lLightPL.z++;
                        lLightP2.interpolate(lLightP1, lLightPL, 0.95F);
                        lLight[j].setPos(lLightP2);
                        float f = (float)lLightP1.distance(lLightPL);
                        float f1 = f * 0.5F + 60F;
                        float f2 = 0.7F - (0.8F * f * lightTime) / 2000F;
                        lLight[j].setEmit(f2, f1);
                    } else
                    {
                        lLight[j].setEmit(0.0F, 0.0F);
                    }
                    continue;
                }
                if(lLight[j].getR() != 0.0F)
                    lLight[j].setEmit(0.0F, 0.0F);
            }

        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(FM.crew > 1 && !bObserverKilled)
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
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            Vector3d vector3d = FM.getVflow();
            mn = (float)vector3d.lengthSquared();
            mn = (float)Math.sqrt(mn);
            World.cur().getClass();
            mn /= Atmosphere.sonicSpeed((float)((Tuple3d) (FM.Loc)).z);
            if(mn >= 0.9F && (double)mn < 1.1000000000000001D)
                ts = true;
            else
                ts = false;
            ft = World.getTimeofDay() % 0.01F;
            if(ft == 0.0F)
                UpdateLightIntensity();
        }
        if(FM.Gears.onGround() && FM.CT.getCockpitDoor() == 1.0F)
        {
            hierMesh().chunkVisible("HMask1_D0", false);
            if(FM.crew > 1)
                hierMesh().chunkVisible("HMask2_D0", false);
        } else
        {
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
            if(FM.crew > 1)
                hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Pilot2_D0"));
        }
        if(FLIR)
            FLIR();
        if((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode()) && (FM instanceof Maneuver))
            AiAirbrakeOperation();
        formationlights();
        if(!FM.isPlayers())
            FM.CT.bAntiColLights = FM.AS.bNavLightsOn;
        anticollights();
        if(FM.AP.way.curr().Action == 3 && !((Maneuver)super.FM).hasBombs())
        {
            FM.AP.way.next();
            ((Maneuver)super.FM).target_ground = null;
            ((Maneuver)super.FM).Group.setGroupTask(1);
        }
    }

    private final void UpdateLightIntensity()
    {
        if(World.getTimeofDay() >= 6F && World.getTimeofDay() < 7F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 6F, 7F, 1.0F, 0.1F);
        else
        if(World.getTimeofDay() >= 18F && World.getTimeofDay() < 19F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 18F, 19F, 0.1F, 1.0F);
        else
        if(World.getTimeofDay() >= 7F && World.getTimeofDay() < 18F)
            lightTime = 0.1F;
        else
            lightTime = 1.0F;
    }

    public boolean typeBomberToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        if(k14Mode == 0)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Bomb");
        } else
        if(k14Mode == 1)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Gunnery");
        } else
        if(k14Mode == 2 && FM.actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Navigation");
        return true;
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset()
    {
    }

    public void typeFighterAceMakerAdjDistancePlus()
    {
        k14Distance += 10F;
        if(k14Distance > 1500F)
            k14Distance = 1500F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 10F;
        if(k14Distance < 20F)
            k14Distance = 20F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
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

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        default:
            break;

        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1: // '\001'
            if(FM.crew > 1)
            {
                hierMesh().chunkVisible("Pilot2_D0", false);
                hierMesh().chunkVisible("Head2_D0", false);
                hierMesh().chunkVisible("HMask2_D0", false);
                hierMesh().chunkVisible("Pilot2_D1", true);
                bObserverKilled = true;
            }
            break;
        }
    }


    public void doEjectCatapultStudent()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 30D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat02");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(1, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat2_D0", false);
    }

    public void doEjectCatapultInstructor()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 30D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(1, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat1_D0", false);
        super.FM.setTakenMortalDamage(true, null);
        FM.CT.WeaponControl[0] = false;
        FM.CT.WeaponControl[1] = false;
        FM.CT.bHasAileronControl = false;
        FM.CT.bHasRudderControl = false;
        FM.CT.bHasElevatorControl = false;
        FM.CT.bHasVarWingControl = false;
    }

    public void checkHydraulicStatus()
    {
        if(FM.EI.engines[0].getStage() < 6 && FM.Gears.nOfGearsOnGr > 0)
        {
            hasHydraulicPressure = false;
            if(FM.CT.getWing() == 0.0F)
                FM.CT.VarWingControl = 0.8F;
            FM.CT.bHasFlapsControl = false;
            FM.CT.bHasAileronControl = false;
            FM.CT.bHasElevatorControl = false;
            FM.CT.bHasRudderControl = false;
            FM.CT.bHasAirBrakeControl = false;
            updateControlsVisuals();
        } else
        if(!hasHydraulicPressure)
        {
            hasHydraulicPressure = true;
            if(FM.CT.getWing() == 0.0F)
                FM.CT.VarWingControl = 0.0F;
            FM.CT.bHasFlapsControl = true;
            FM.CT.bHasAileronControl = true;
            if(FM.CT.getWing() > 0.3F)
            {
                FM.CT.ElevatorControl = 0.0F;
                FM.CT.setTrimElevatorControl(0.0F);
                FM.CT.bHasElevatorControl = false;
                updateControlsVisuals();
            }
            else
                FM.CT.bHasElevatorControl = true;
            FM.CT.bHasRudderControl = true;
            FM.CT.bHasAirBrakeControl = true;
        }
    }

    public void moveCockpitDoor(float f)
    {
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 0.0F, 45F * f);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 90F * f2, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.11F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 0.0F, Aircraft.cvt(f2, 0.01F, 0.72F, 0.0F, -25F));
        hiermesh.chunkSetAngles("GearC8_D0", 0.0F, -60F * f2, 0.0F);
        resetXYZYPR();
        double lenH = 0.362D;  // Length of GearC71b : High
        double lenL = 0.282D;  // Length of GearC71d : Low
        double lenS = 0.408D;  // Length of GearC71c : Side
        Aircraft.xyz[0] = Aircraft.cvt(f2, 0.15F, 0.83F, 0.0F, 0.04F);
        hiermesh.chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
        double contraH = ((double)Aircraft.xyz[0] + 0.178D) * lenH / (lenH + lenL);
        double contraL = ((double)Aircraft.xyz[0] + 0.178D) * lenL / (lenH + lenL);
        float fH = (float)Math.toDegrees(Math.acos((lenH - contraH) / lenH)) * 0.7F;
        float fL = (float)Math.toDegrees(Math.acos((lenL - contraL) / lenL)) * 1.3F;
        hiermesh.chunkSetAngles("GearC71b_D0", 0.0F, fH -35F, 0.0F);
        hiermesh.chunkSetAngles("GearC71c_D0", 0.0F, -0.54F * fH + 19.7F, 0.0F);
        hiermesh.chunkSetAngles("GearC71d_D0", 0.0F, -fL - fH + 84F, 0.0F);
        hiermesh.chunkSetAngles("GearC51_D0", 0.0F, Aircraft.cvt(f2, 0.4F, 0.8F, 0.0F, 125F), 0.0F);
        if(f2 < 0.7F)
            hiermesh.chunkSetAngles("GearC22_D0", 0.0F, 0.0F, 0.0F);

        resetXYZYPR();
        if(f < 0.5F)
        {
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.3F, 0.48F, 0.0F, 0.15F);
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.1F, 0.5F, 0.0F, -50F);
            Aircraft.ypr[2] = Aircraft.cvt(f, 0.05F, 0.14F, 0.0F, -8F);
        } else
        if(f < 0.75F)
        {
            Aircraft.xyz[1] = 0.15F;
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.5F, 0.75F, -50F, -85F);
            Aircraft.ypr[2] = Aircraft.cvt(f, 0.5F, 0.75F, -8F, -2.8F);
        } else
        {
            Aircraft.xyz[1] = 0.15F;
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.75F, 1.0F, -85F, -105F);
            Aircraft.ypr[2] = Aircraft.cvt(f, 0.75F, 1.0F, -2.8F, -0.8F);
        }
        hiermesh.chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
//        hiermesh.chunkSetAngles("GearL3_D0", Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, 90F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, 98F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.65F, 1.0F, 0.0F, 16.32F));
        if(f < 0.6F)
            hiermesh.chunkSetAngles("GearL51_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.4F, 0.6F, 0.0F, -77.4F));
        else
            hiermesh.chunkSetAngles("GearL51_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.6F, 1.0F, -77.4F, -167.627F));
        hiermesh.chunkSetAngles("GearL22_D0", 0.0F, Aircraft.cvt(f, 0.1F, 1.0F, 0.0F, 4.25F), 0.0F);
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.1F, 1.0F, 0.0F, -0.222F);
        hiermesh.chunkSetLocate("GearL23_D0", Aircraft.xyz, Aircraft.ypr);
        resetXYZYPR();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.4F, 0.8F, 0.0F, 0.51F);
        hiermesh.chunkSetLocate("GearL4_D0", Aircraft.xyz, Aircraft.ypr);
        float f3 = (float)Math.toDegrees(Math.acos((0.96F - 0.104F - Aircraft.xyz[1]) / 0.96F));
        f3 -= (float)Math.toDegrees(Math.acos((0.96F - 0.104F) / 0.96F));
        hiermesh.chunkSetAngles("GearL41_D0", 0.0F, 0.0F, -f3);
        hiermesh.chunkSetAngles("GearL42_D0", 0.0F, 0.0F, f3 * 2.0F);
       // hiermesh.chunkSetAngles("GearL15_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.4F, 0.6F, 0.0F, 70F));
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0",  0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, 95F), 0.0F);
        hiermesh.chunkSetAngles("GearL11_D0",  0.0F, Aircraft.cvt(f, 0.03F, 0.1F, 0.0F, -75F), 0.0F);

        resetXYZYPR();
        if(f1 < 0.5F)
        {
            Aircraft.xyz[1] = Aircraft.cvt(f1, 0.3F, 0.48F, 0.0F, -0.15F);
            Aircraft.ypr[1] = Aircraft.cvt(f1, 0.1F, 0.5F, 0.0F, -50F);
            Aircraft.ypr[2] = Aircraft.cvt(f1, 0.05F, 0.14F, 0.0F, 8F);
        } else
        if(f1 < 0.75F)
        {
            Aircraft.xyz[1] = -0.15F;
            Aircraft.ypr[1] = Aircraft.cvt(f1, 0.5F, 0.75F, -50F, -85F);
            Aircraft.ypr[2] = Aircraft.cvt(f1, 0.5F, 0.75F, 8F, 2.8F);
        } else
        {
            Aircraft.xyz[1] = -0.15F;
            Aircraft.ypr[1] = Aircraft.cvt(f1, 0.75F, 1.0F, -85F, -105F);
            Aircraft.ypr[2] = Aircraft.cvt(f1, 0.75F, 1.0F, 2.8F, 0.8F);
        }
        hiermesh.chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearR3_D0", Aircraft.cvt(f1, 0.1F, 0.2F, 0.0F, -98F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.65F, 1.0F, 0.0F, 16.32F));
        if(f1 < 0.6F)
            hiermesh.chunkSetAngles("GearR51_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.4F, 0.6F, 0.0F, -77.4F));
        else
            hiermesh.chunkSetAngles("GearR51_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.6F, 1.0F, -77.4F, -167.627F));
        hiermesh.chunkSetAngles("GearR22_D0", 0.0F, Aircraft.cvt(f1, 0.1F, 1.0F, 0.0F, 4.25F), 0.0F);
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(f1, 0.1F, 1.0F, 0.0F, -0.222F);
        hiermesh.chunkSetLocate("GearR23_D0", Aircraft.xyz, Aircraft.ypr);
        resetXYZYPR();
        Aircraft.xyz[1] = Aircraft.cvt(f1, 0.4F, 0.8F, 0.0F, 0.51F);
        hiermesh.chunkSetLocate("GearR4_D0", Aircraft.xyz, Aircraft.ypr);
        f3 = (float)Math.toDegrees(Math.acos((0.96F - 0.104F - Aircraft.xyz[1]) / 0.96F));
        f3 -= (float)Math.toDegrees(Math.acos((0.96F - 0.104F) / 0.96F));
        hiermesh.chunkSetAngles("GearR41_D0", 0.0F, 0.0F, -f3);
        hiermesh.chunkSetAngles("GearR42_D0", 0.0F, 0.0F, f3 * 2.0F);
       // hiermesh.chunkSetAngles("GearR15_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.4F, 0.6F, 0.0F, -70F));
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.1F, 0.0F, 95F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0",  0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearR11_D0", 0.0F, Aircraft.cvt(f1, 0.03F, 0.1F, 0.0F, 75F), 0.0F );
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        moveGear(hiermesh, f, f, f);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        if(FM.CT.getGearC() > 0.999F)
        {
            double lenH = 0.362D;  // Length of GearC71b : High
            double lenL = 0.282D;  // Length of GearC71d : Low
            double lenS = 0.408D;  // Length of GearC71c : Side
            float f = FM.Gears.gWheelSinking[2];
            resetYPRmodifier();
            Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 0.22F, 0.0F, 0.22F) + 0.065F;
//            Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 0.22F, 0.0F, 0.22F) + 0.04F;
            hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
            double contraH = ((double)Aircraft.xyz[0] + 0.178D) * lenH / (lenH + lenL);
            double contraL = ((double)Aircraft.xyz[0] + 0.178D) * lenL / (lenH + lenL);
            float fH = (float)Math.toDegrees(Math.acos((lenH - contraH) / lenH)) * 0.7F;
            float fL = (float)Math.toDegrees(Math.acos((lenL - contraL) / lenL)) * 1.3F;
            hierMesh().chunkSetAngles("GearC71b_D0", 0.0F, fH - 35F, 0.0F);
            hierMesh().chunkSetAngles("GearC71c_D0", 0.0F, -0.54F * fH + 19.7F, 0.0F);
            hierMesh().chunkSetAngles("GearC71d_D0", 0.0F, -fL - fH + 84F, 0.0F);
//            hierMesh().chunkSetAngles("GearC8_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.19F, -140F, -128F), 0.0F);
        }
        if(FM.CT.getGearL() > 0.999F)
        {
            float f1 = FM.Gears.gWheelSinking[0];
            resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f1, 0.0F, 0.19F, 0.0F, 0.19F) + 0.51F;
            float f4 = (float)Math.toDegrees(Math.acos((0.8559999F - Aircraft.xyz[1]) / 0.96F));
            f4 -= (float)Math.toDegrees(Math.acos(0.89166665077209473D));
            hierMesh().chunkSetLocate("GearL4_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetAngles("GearL41_D0", 0.0F, 0.0F, -f4);
            hierMesh().chunkSetAngles("GearL42_D0", 0.0F, 0.0F, f4 * 2.0F);
        }
        if(FM.CT.getGearR() > 0.999F)
        {
            float f2 = FM.Gears.gWheelSinking[1];
            resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f2, 0.0F, 0.19F, 0.0F, 0.19F) + 0.51F;
            float f5 = (float)Math.toDegrees(Math.acos((0.8559999F - Aircraft.xyz[1]) / 0.96F));
            f5 -= (float)Math.toDegrees(Math.acos(0.89166665077209473D));
            hierMesh().chunkSetLocate("GearR4_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetAngles("GearR41_D0", 0.0F, 0.0F, -f5);
            hierMesh().chunkSetAngles("GearR42_D0", 0.0F, 0.0F, f5 * 2.0F);
        }
    }

    private static void resetXYZYPR()
    {
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
    }

    public void moveSteering(float f)
    {
        if(FM.CT.GearControl < 0.7F || FM.Gears.isCatapultArmed())
            hierMesh().chunkSetAngles("GearC22_D0", 0.0F, 0.0F, 0.0F);
        else if(FM.CT.GearControl > 0.7F && FM.Gears.onGround())
            hierMesh().chunkSetAngles("GearC22_D0", 0.0F, 0.0F, -1.0F * f);
    }

    protected void moveCatLaunchBar(float f)
    {
        hierMesh().chunkSetAngles("GearC51_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 125F, 35F), 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, 20F * f, 0.0F);
        hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, 20F * f, 0.0F);
    }


    protected void moveElevator(float f)
    {
        updateControlsVisuals();
    }


    protected void moveAileron(float f)
    {
        updateControlsVisuals();
        updateAileronVisuals();
    }

    private final void updateControlsVisuals()
    {
        if(!FM.CT.bHasElevatorControl || FM.CT.getVarWing() > 0.8F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -35F * FM.CT.getElevator() + 27F * FM.CT.getAileron(), 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -35F * FM.CT.getElevator() - 27F * FM.CT.getAileron(), 0.0F);
        }
    }


    protected void moveFlap(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.5F, 0.0F, -0.087F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.5F, 0.0F, -0.03F);
        Aircraft.ypr[1] = Aircraft.cvt(f, 0.01F, 0.5F, 0.0F, 34F);
        hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.5F, 0.0F, 0.087F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.5F, 0.0F, -0.03F);
        Aircraft.ypr[1] = Aircraft.cvt(f, 0.01F, 0.5F, 0.0F, -34F);
        hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);

        if(bFlapsOutFixed || FM.CT.getVarWing() > 0.6F)
        {
            hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 0.0F, 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -35 * f, 0.0F);
            hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -35 * f, 0.0F);
        }

        if(bFlapsInFixed || FM.CT.getVarWing() > 0.6F)
        {
            hierMesh().chunkSetAngles("Flap03_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Flap04_D0", 0.0F, 0.0F, 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("Flap03_D0", 0.0F, -35 * f, 0.0F);
            hierMesh().chunkSetAngles("Flap04_D0", 0.0F, -35 * f, 0.0F);
        }

        updateAileronVisuals();
    }

    private final void updateAileronVisuals()
    {
        boolean bflapDep = FM.CT.getFlap() > 0.329F;
        boolean bflapDepMax = FM.CT.getFlap() > 0.8F;
        if(FM.CT.getAileron() < 0F)
        {
            if(bflapDep)
            {
                hierMesh().chunkSetAngles("AroneL_D0", 0.0F, Math.max(-55F * FM.CT.getAileron(), (bflapDepMax ? 17.5F : 4.5F)), 0.0F);
                hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -(bflapDepMax ? 17.5F : 4.5F), 0.0F);
            }
            else
            {
                hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -55F * FM.CT.getAileron(), 0.0F);
                hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, 0.0F);
            }
        }
        else
        {
            if(bflapDep)
            {
                hierMesh().chunkSetAngles("AroneL_D0", 0.0F, (bflapDepMax ? 17.5F : 4.5F), 0.0F);
                hierMesh().chunkSetAngles("AroneR_D0", 0.0F, Math.min(-55F * FM.CT.getAileron(), -(bflapDepMax ? 17.5F : 4.5F)), 0.0F);
            }
            else
            {
                hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -55F * FM.CT.getAileron(), 0.0F);
            }
        }
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -70F * f, 0.0F);
        arrestor = f;
    }

    private void calculateArrestor()
    {
        if(FM.Gears.arrestorVAngle != 0.0F)
        {
            float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
            arrestor = 0.8F * arrestor + 0.2F * f1;
            moveArrestorHook(arrestor);
        } else
        {
            float f2 = (-33F * FM.Gears.arrestorVSink) / 57F;
            if(f2 < 0.0F && FM.getSpeedKMH() > 60F)
                Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if(f2 > 0.0F && FM.CT.getArrestor() < 0.95F)
                f2 = 0.0F;
            if(f2 > 0.2F)
                f2 = 0.2F;
            if(f2 > 0.0F)
                arrestor = 0.7F * arrestor + 0.3F * (arrestor + f2);
            else
                arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
            if(arrestor < 0.0F)
                arrestor = 0.0F;
            else
            if(arrestor > 1.0F)
                arrestor = 1.0F;
            moveArrestorHook(arrestor);
        }
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        FM.CT.VarWingControl = Math.max(FM.CT.getVarWing(), FM.CT.getWing());
        if(FM.CT.getWing() > 0.3F || !hasHydraulicPressure)
        {
            FM.CT.ElevatorControl = 0.0F;
            FM.CT.setTrimElevatorControl(0.0F);
            FM.CT.bHasElevatorControl = false;
            updateControlsVisuals();
        }
        else
            FM.CT.bHasElevatorControl = true;
        computeVarWing();
    }

    public void moveWingFold(float f)
    {
        moveWingFold(hierMesh(), f);
    }

    private void moveInletRamps()
    {
        hierMesh().chunkSetAngles("IntakeDoor_Lf", 0.0F, 0.0F, cvt(calculateMach(), 1.1F, 2.2F, 0.0F, 13.2F));
        hierMesh().chunkSetAngles("IntakeDoor_Rf", 0.0F, 0.0F, cvt(calculateMach(), 1.1F, 2.2F, 0.0F, 13.2F));
        float fRamp3rd = 0.0F;
        if(calculateMach() < 1.2F)
            fRamp3rd = cvt(calculateMach(), 0.9F, 1.2F, 0.0F, -4.22F);
        else if(calculateMach() < 1.6F)
            fRamp3rd = cvt(calculateMach(), 1.2F, 1.4F, -4.22F, -9.85F);
        else if(calculateMach() < 2.0F)
            fRamp3rd = cvt(calculateMach(), 1.6F, 1.9F, -9.85F, -16.87F);
        else 
            fRamp3rd = cvt(calculateMach(), 2.0F, 2.3F, -16.87F, -22.5F);
        hierMesh().chunkSetAngles("IntakeDoor_Lr", 0.0F, 0.0F, fRamp3rd);
        hierMesh().chunkSetAngles("IntakeDoor_Rr", 0.0F, 0.0F, fRamp3rd);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        int i = part(s);
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.endsWith("p1"))
                {
                    getEnergyPastArmor(13.350000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                } else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(8.770001F, shot);
                else
                if(s.endsWith("g1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(40F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                }
            } else
            if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int j = s.charAt(10) - 48;
                switch(j)
                {
                case 1: // '\001'
                case 2: // '\002'
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(1.1F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3: // '\003'
                case 4: // '\004'
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;
                }
            } else
            if(s.startsWith("xxeng1"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("bloc"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 20F)
                {
                    FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 24000F)
                {
                    FM.AS.hitEngine(shot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                s.endsWith("exht");
            } else
            if(s.startsWith("xxmgun0"))
            {
                int k = s.charAt(7) - 49;
                if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                {
                    debuggunnery("Armament: mnine Gun (" + k + ") Disabled..");
                    FM.AS.setJamBullets(0, k);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(FM.AS.astateTankStates[l] == 0)
                    {
                        debuggunnery("Fuel Tank (" + l + "): Pierced..");
                        FM.AS.hitTank(shot.initiator, l, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        FM.AS.hitTank(shot.initiator, l, 2);
                        debuggunnery("Fuel Tank (" + l + "): Hit..");
                    }
                }
            } else
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
            } else
            if(s.startsWith("xxhyd"))
                FM.AS.setInternalDamage(shot.initiator, 3);
            else
            if(s.startsWith("xxpnm"))
                FM.AS.setInternalDamage(shot.initiator, 1);
        } else
        {
            if(s.startsWith("xcockpit"))
            {
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                getEnergyPastArmor(0.05F, shot);
            }
            if(s.startsWith("xcf"))
                hitChunk("CF", shot);
            else
            if(s.startsWith("xnose"))
                hitChunk("Nose", shot);
            else
            if(s.startsWith("xtail"))
            {
                if(chunkDamageVisible("Tail1") < 3)
                    hitChunk("Tail1", shot);
            } else
            if(s.startsWith("xkeel"))
            {
                if(chunkDamageVisible("Keel1") < 2)
                    hitChunk("Keel1", shot);
            } else
            if(s.startsWith("xrudder"))
                hitChunk("Rudder1", shot);
            else
            if(s.startsWith("xstab"))
            {
                if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                    hitChunk("StabL", shot);
                if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
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
                if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
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
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 13: // '\r'
            FM.Gears.cgear = false;
            float f = World.Rnd().nextFloat(0.0F, 1.0F);
            if(f < 0.1F)
            {
                FM.AS.hitEngine(this, 0, 100);
                if((double)World.Rnd().nextFloat(0.0F, 1.0F) < 0.48999999999999999D)
                    FM.EI.engines[0].setEngineDies(actor);
                FM.EI.engines[1].setEngineDies(actor);
            } else
            if((double)f > 0.55000000000000004D)
                FM.EI.engines[0].setEngineDies(actor);
            FM.EI.engines[1].setEngineDies(actor);
            return super.cutFM(i, j, actor);

        case 19: // '\023'
            FM.EI.engines[0].setEngineDies(actor);
            FM.EI.engines[1].setEngineDies(actor);
            return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }


    public void typeFighterAceMakerRangeFinder()
    {
        if(k14Mode == 2)
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
        {
            k14Distance = 500F;
            hunted = War.GetNearestEnemyAircraft(FM.actor, 2700F, 9);
        }
        if(hunted != null)
        {
            k14Distance = (float)FM.actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
            if(k14Distance > 1500F)
                k14Distance = 1500F;
            else
            if(k14Distance < 20F)
                k14Distance = 20F;
        }
    }

    public float getAirPressure(float f)
    {
        float f1 = 1.0F - (0.0065F * f) / 288.15F;
        float f2 = 5.255781F;
        return 101325F * (float)Math.pow(f1, f2);
    }

    public float getAirPressureFactor(float f)
    {
        return getAirPressure(f) / 101325F;
    }

    public float getAirDensity(float f)
    {
        return (getAirPressure(f) * 0.0289644F) / (8.31447F * (288.15F - 0.0065F * f));
    }

    public float getAirDensityFactor(float f)
    {
        return getAirDensity(f) / 1.225F;
    }

    public float getMachForAlt(float f)
    {
        f /= 1000F;
        int i = 0;
        for(i = 0; i < TypeSupersonic.fMachAltX.length - 1 && TypeSupersonic.fMachAltX[i] <= f; i++);
        if(i == 0)
        {
            return TypeSupersonic.fMachAltY[0];
        } else
        {
            float f1 = TypeSupersonic.fMachAltY[i - 1];
            float f2 = TypeSupersonic.fMachAltY[i] - f1;
            float f3 = TypeSupersonic.fMachAltX[i - 1];
            float f4 = TypeSupersonic.fMachAltX[i] - f3;
            float f5 = (f - f3) / f4;
            return f1 + f2 * f5;
        }
    }

    public float calculateMach()
    {
        return FM.getSpeedKMH() / getMachForAlt(FM.getAltitude());
    }

    public void soundbarier()
    {
        float f = getMachForAlt(FM.getAltitude()) - FM.getSpeedKMH();
        if(f < 0.5F)
            f = 0.5F;
        float f1 = FM.getSpeedKMH() - getMachForAlt(FM.getAltitude());
        if(f1 < 0.5F)
            f1 = 0.5F;
        if((double)calculateMach() <= 1.0D)
        {
            FM.VmaxAllowed = FM.getSpeedKMH() + f;
            SonicBoom = 0.0F;
            isSonic = false;
        }
        if((double)calculateMach() >= 1.0D)
        {
            FM.VmaxAllowed = FM.getSpeedKMH() + f1;
            isSonic = true;
        }
        if(FM.VmaxAllowed > 1500F)
            FM.VmaxAllowed = 1500F;
        if(isSonic && SonicBoom < 1.0F)
        {
            super.playSound("aircraft.SonicBoom", true);
            super.playSound("aircraft.SonicBoomInternal", true);
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
            if(Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(FM.getAltitude()))
                shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            SonicBoom = 1.0F;
        }
        if((double)calculateMach() > 1.01D || (double)calculateMach() < 1.0D)
            Eff3DActor.finish(shockwave);
    }

    public void engineSurge(float f)
    {
        if(FM.AS.isMaster())
        {
            for(int i = 0; i < 2; i++)
            {
                if(curthrl == -1F)
                {
                    curthrl = oldthrl = FM.EI.engines[i].getControlThrottle();
                    continue;
                }
                curthrl = FM.EI.engines[i].getControlThrottle();
                if(curthrl < 1.05F)
                {
                    if((curthrl - oldthrl) / f > 35F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                    {
                        if(FM.actor == World.getPlayerAircraft())
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Fans Surge!!!");
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.01D * (double)(FM.EI.engines[i].getRPM() / 1000F);
                        FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage);
                        if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                            FM.AS.hitEngine(this, i, 100);
                        if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                            FM.EI.engines[i].setEngineDies(this);
                    }
                    if((curthrl - oldthrl) / f < -35F && (curthrl - oldthrl) / f > -100F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6)
                    {
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.001D * (double)(FM.EI.engines[i].getRPM() / 1000F);
                        FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage);
                        if(World.Rnd().nextFloat() < 0.4F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                        {
                            if(FM.actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                            FM.EI.engines[i].setEngineStops(this);
                        } else
                        if(FM.actor == World.getPlayerAircraft())
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Fans Surge!!!");
                    }
                }
                oldthrl = curthrl;
            }

        }
    }

    private void gearlimit()
    {
        float f = FM.getSpeedKMH() - 650F;
        if(f < 0.0F)
            f = 0.0F;
        FM.CT.dvGear = 0.2F - f / 500F;
        if(FM.CT.dvGear < 0.0F)
            FM.CT.dvGear = 0.0F;
    }



    public void update(float f)
    {
        if((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            FM.AS.bIsAboutToBailout = false;
            if(FM.crew > 1)
            {
                if(Time.current() > lTimeNextEject)
                    bailout();
            } else
            {
                bailout();
            }
        }
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            for(int en = 0; en < 2; en++)
            {
                if(FM.EI.engines[en].getThrustOutput() > 0.4F && FM.EI.engines[en].getStage() == 6)
                {
                    if(FM.EI.engines[en].getThrustOutput() > 1.001F)
                        FM.AS.setSootState(this, en, 5);
                    else
                    if(FM.EI.engines[en].getThrustOutput() > 0.96F && FM.EI.engines[en].getThrustOutput() < 1.001F)
                        FM.AS.setSootState(this, en, 3);
                    else
                        FM.AS.setSootState(this, en, 2);
                } else
                {
                    FM.AS.setSootState(this, en, 0);
                }
                setExhaustFlame(Math.round(Aircraft.cvt(FM.EI.engines[en].getThrustOutput(), 0.7F, 0.92F, 0.0F, 12F)), en);
            }
            if(super.FM instanceof RealFlightModel)
                umn();
        }
        if(FLIR)
            laser(getLaserSpot());
        updatecontrollaser();
        engineSurge(f);
        typeFighterAceMakerRangeFinder();
        soundbarier();
        checkHydraulicStatus();
        computeLift();
        computeVarWing();
        computeFlapsFixing();
        computeCombatFlaps(f);
        computeEnergy();
        computeSupersonicLimiter();
        computeSubsonicLimiter();
        FlapAssistTakeoff();
        RWRLaunchWarning();
        if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))
            RWRWarning();
        if(FM.crew > 1 && obsMove < obsMoveTot && !bObserverKilled && !FM.AS.isPilotParatrooper(1))
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
        gearlimit();
        moveInletRamps();
        super.update(f);
        if(FM.CT.getArrestor() > 0.2F)
            calculateArrestor();
        if(this instanceof F_14A)
        {
            int step;

            fNozzleOpenL = FM.EI.engines[0].getPowerOutput() <= 0.92F ? cvt(FM.EI.engines[0].getPowerOutput(), 0.0F, 0.92F, 0F, 0.9999F) : cvt(FM.EI.engines[0].getPowerOutput(), 0.92F, 1.1F, 0.9999F, 0F);
            step = (int)Math.floor(fNozzleOpenL * 10F);
            for(int i = 0; i < 10; i++)
                hierMesh().chunkVisible("ExhaustL" + i, i == step);

            fNozzleOpenR = FM.EI.engines[1].getPowerOutput() <= 0.92F ? cvt(FM.EI.engines[1].getPowerOutput(), 0.0F, 0.92F, 0F, 0.9999F) : cvt(FM.EI.engines[1].getPowerOutput(), 0.92F, 1.1F, 0.9999F, 0F);
            step = (int)Math.floor(fNozzleOpenR * 10F);
            for(int i = 0; i < 10; i++)
                hierMesh().chunkVisible("ExhaustR" + i, i == step);
        }
        if((this instanceof F_14B) || (this instanceof F_14D))
        {
            float deg;

            fNozzleOpenL = FM.EI.engines[0].getPowerOutput() <= 0.92F ? cvt(FM.EI.engines[0].getPowerOutput(), 0.0F, 0.92F, 0.0F, 1.0F) : cvt(FM.EI.engines[0].getPowerOutput(), 0.92F, 1.1F, 1.0F, 0.0F);
            deg = -9F * (1F - fNozzleOpenL);
            for(int i = 1; i < 33; i++)
                hierMesh().chunkSetAngles("Eflap" + i, deg, 0.0F, 0.0F);

            fNozzleOpenR = FM.EI.engines[1].getPowerOutput() <= 0.92F ? cvt(FM.EI.engines[1].getPowerOutput(), 0.0F, 0.92F, 0.0F, 1.0F) : cvt(FM.EI.engines[1].getPowerOutput(), 0.92F, 1.1F, 1.0F, 0.0F);
            deg = -9F * (1F - fNozzleOpenR);
            for(int j = 33; j > 32 && j < 65; j++)
                hierMesh().chunkSetAngles("Eflap" + j, deg, 0.0F, 0.0F);
        }

        float f1 = cvt(FM.getSpeedKMH(), 500F, 1000F, 0.999F, 0.601F);
        if(FM.getSpeed() > 7F && World.Rnd().nextFloat() < getAirDensityFactor(FM.getAltitude()))
        {
            if(FM.getOverload() > 5.7F)
            {
                pull01 = Eff3DActor.New(this, findHook("_Pull01"), null, f1, "3DO/Effects/Aircraft/Pullingvapor.eff", -1F);
                pull02 = Eff3DActor.New(this, findHook("_Pull02"), null, f1, "3DO/Effects/Aircraft/Pullingvapor.eff", -1F);
                pull03 = Eff3DActor.New(this, findHook("_Pull03"), null, f1, "3DO/Effects/Aircraft/Pullingvapor.eff", -1F);
                pull04 = Eff3DActor.New(this, findHook("_Pull04"), null, f1, "3DO/Effects/Aircraft/Pullingvapor.eff", -1F);
            }
            if(FM.getOverload() <= 5.7F)
            {
                Eff3DActor.finish(pull01);
                Eff3DActor.finish(pull02);
                Eff3DActor.finish(pull03);
                Eff3DActor.finish(pull04);
            }
        }
        if(FM.getSpeedKMH() > 300F)
            FM.CT.cockpitDoorControl = 0.0F;
    }


    private final void umn()
    {
        Vector3d vector3d = FM.getVflow();
        mn = (float)vector3d.lengthSquared();
        mn = (float)Math.sqrt(mn);
        mn /= Atmosphere.sonicSpeed((float)((Tuple3d) (FM.Loc)).z);
        if(mn >= lteb)
            ts = true;
        else
            ts = false;
    }

    public boolean ist()
    {
        return ts;
    }

    public float gmnr()
    {
        return mn;
    }

    public boolean inr()
    {
        return ictl;
    }

    public void typeRadarGainMinus()
    {
        radarrange++;
        if(radarrange > 4)
            radarrange = 4;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeRadarGainPlus()
    {
        radarrange--;
        if(radarrange < 1)
            radarrange = 1;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeRadarRangeMinus()
    {
        radarrange++;
        if(radarrange > 4)
            radarrange = 4;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeRadarRangePlus()
    {
        radarrange--;
        if(radarrange < 1)
            radarrange = 1;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeRadarReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    public void typeRadarReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public boolean typeRadarToggleMode()
    {
        radarmode++;
        if(radarmode > 2)
            radarmode = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "radar mode" + radarmode);
        return false;
    }

    public float getFlowRate()
    {
        return FlowRate;
    }

    public float getFuelReserve()
    {
        return FuelReserve;
    }

    public void doSetSootState(int i, int j)
    {
        for(int k = 0; k < 2; k++)
        {
            if(FM.AS.astateSootEffects[i][k] != null)
                Eff3DActor.finish(FM.AS.astateSootEffects[i][k]);
            FM.AS.astateSootEffects[i][k] = null;
        }

        switch(j)
        {
        case 1: // '\001'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 3: // '\003'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 4F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.5F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;

        case 2: // '\002'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.8F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.5F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;

        case 5: // '\005'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 3F, "3DO/Effects/Aircraft/AfterBurnerF100D.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.5F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;

        case 4: // '\004'
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;
        }
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("BrakeL_D0", 0.0F, -40F * f, 0.0F);
        hierMesh().chunkSetAngles("BrakeR_D0", 0.0F, 40F * f, 0.0F);
    }

    public void setExhaustFlame(int i, int j)
    {
        String pfl = "";
        if(j == 1)
            pfl = "b";

        switch(i)
        {
            case 0: // '\0'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 1: // '\001'
                hierMesh().chunkVisible("Exhaust1" + pfl, true);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 2: // '\002'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, true);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 3: // '\003'
                hierMesh().chunkVisible("Exhaust1" + pfl, true);
                hierMesh().chunkVisible("Exhaust2" + pfl, true);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;
                //By western:  Old code was "fall through", but why ?
                //             "fall through" makes do nothing about above 5 lines.

            case 4: // '\004'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, true);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 5: // '\005'
                hierMesh().chunkVisible("Exhaust1" + pfl, true);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, true);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 6: // '\006'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, true);
                hierMesh().chunkVisible("Exhaust3" + pfl, true);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 7: // '\007'
                hierMesh().chunkVisible("Exhaust1" + pfl, true);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, true);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 8: // '\b'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, true);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, true);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 9: // '\t'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, true);
                hierMesh().chunkVisible("Exhaust4" + pfl, true);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 10: // '\n'
                hierMesh().chunkVisible("Exhaust1" + pfl, true);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, true);
                break;

            case 11: // '\013'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, true);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, true);
                break;

            case 12: // '\f'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, true);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, true);
                break;

            default:
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;
        }
    }

    private void bailout()
    {
        if(overrideBailout)
            if(FM.AS.astateBailoutStep >= 0 && FM.AS.astateBailoutStep < 2)
            {
                if(FM.CT.cockpitDoorControl > 0.5F && FM.CT.getCockpitDoor() > 0.5F)
                    FM.AS.astateBailoutStep = 11;
                else
                    FM.AS.astateBailoutStep = 2;
            } else
            if(FM.AS.astateBailoutStep >= 2 && FM.AS.astateBailoutStep <= 3)
            {
                switch(FM.AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(FM.CT.cockpitDoorControl < 0.5F)
                        doRemoveBlisters();
                    break;

                case 3: // '\003'
                    lTimeNextEject = Time.current() + 1000L;
                    break;
                }
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                FM.AS.astateBailoutStep = (byte)(FM.AS.astateBailoutStep + 1);
                if(FM.AS.astateBailoutStep == 4)
                    FM.AS.astateBailoutStep = 11;
            } else
            if(FM.AS.astateBailoutStep >= 11 && FM.AS.astateBailoutStep <= 19)
            {
                byte byte0 = FM.AS.astateBailoutStep;
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                FM.AS.astateBailoutStep = (byte)(FM.AS.astateBailoutStep + 1);
                if((super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_maneuver() != 44)
                {
                    World.cur();
                    if(FM.AS.actor != World.getPlayerAircraft())
                        ((Maneuver)super.FM).set_maneuver(44);
                }
                if(FM.AS.astatePilotStates[byte0 - 11] < 99)
                {
                    if(byte0 == 11)
                    {
                        doRemoveBodyFromPlane(2);
                        doEjectCatapultStudent();
                        lTimeNextEject = Time.current() + 1000L;
                    } else
                    if(byte0 == 12)
                    {
                        doRemoveBodyFromPlane(1);
                        doEjectCatapultInstructor();
                        FM.AS.astateBailoutStep = 51;
                        super.FM.setTakenMortalDamage(true, null);
                        FM.CT.WeaponControl[0] = false;
                        FM.CT.WeaponControl[1] = false;
                        FM.CT.bHasVarWingControl = false;
                        FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                    FM.AS.astatePilotStates[byte0 - 11] = 99;
                } else
                {
                    EventLog.type("astatePilotStates[" + (byte0 - 11) + "]=" + FM.AS.astatePilotStates[byte0 - 11]);
                }
            }
    }

    public void computeCombatFlaps(float f)
    {
        // Auto combat flaps is only F-14B and F-14D
        if(this instanceof F_14A)
            return;

        if(FM.CT.FlapsControl <= 0.30F && FM.CT.FlapsControl >= 0.20F)
        {
            if(!autoEng)
            {
                autoEng = true;
                curFlaps = FM.CT.getFlap();
                if(this == World.getPlayerAircraft())
                    HUD.log("FlapsCombat");
            }
            desiredPosition = 0.2857F * Math.max(Aircraft.cvt(FM.getAOA(), 6.0F, 15F, 0.0F, 1.0F), 1.0F - Aircraft.cvt(FM.getSpeedKMH(), 350F, 500F, 0.0F, 1.0F));
            curFlaps = flapsMovement(f, desiredPosition, curFlaps, 999F, 0.25F);
            FM.CT.forceFlaps(curFlaps);
            if(Math.abs(desiredPosition - curFlaps) >= 0.02F)
                sfxFlaps(true);
            else
                sfxFlaps(false);
        } else
        {
            autoEng = false;
        }
    }

    private float flapsMovement(float f, float f1, float f2, float f3, float f4)
    {
        float f5 = (float)Math.exp(-f / f3);
        float f6 = f1 + (f2 - f1) * f5;
        if(f6 < f1)
        {
            f6 += f4 * f;
            if(f6 > f1)
                f6 = f1;
        } else
        if(f6 > f1)
        {
            f6 -= f4 * f;
            if(f6 < f1)
                f6 = f1;
        }
        return f6;
    }

    private void computeFlapsFixing()
    {
        // when flap position is above 10 degrees, force 10 degrees Combat mode automatically.
        if(FM.CT.FlapsControl > 0.30F && FM.getSpeedKMH() > 550F)
        {
            if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() && (FM instanceof Pilot))
            {
                Main3D.cur3D().aircraftHotKeys.setFlapIndex(1);
                HUD.log("FlapsDegree", new Object[] { new Float(((float)Math.floor((double)FM.CT.FlapsControl * FM.CT.FlapStageMax * 100D + 0.06D) / 100F)) });
            }
            FM.CT.FlapsControl = 0.2857F;
        }

    // Only Leading Edge Slats working in any speed and any VG-wing degree
        if(FM.getSpeedKMH() > 740F && !bFlapsOutFixed)
        {
            if(!bStableAIGround)
            {
                Polares polares = (Polares)Reflection.getValue(FM, "Wing");
                polares.Cy0_1 = 0.8F * stockCy0_0 + 0.2F * stockCy0_1;
                polares.CxMin_1 = 0.8F * stockCxMin_0 + 0.2F * stockCxMin_1;
                polares.parabCxCoeff_1 = 0.8F * stockparabCxCoeff_0 + 0.2F * stockparabCxCoeff_1;
                FM.Sq.squareFlaps = 0.35F * stockSqFlaps;
            }
            bFlapsOutFixed = true;
            bFlapsInFixed = true;
            bTakeoffFlapAssist = false;
        }
        else if(FM.getSpeedKMH() <= 740F && bFlapsOutFixed)
        {
            if(!bStableAIGround)
            {
                Polares polares = (Polares)Reflection.getValue(FM, "Wing");
                polares.Cy0_1 = stockCy0_1;
                polares.CxMin_1 = stockCxMin_1;
                polares.parabCxCoeff_1 = stockparabCxCoeff_1;
                FM.Sq.squareFlaps = stockSqFlaps;
            }
            bFlapsOutFixed = false;
        }

        if(bFlapsOutFixed)
            bFlapsInFixed = true;
        else
        {
            if(FM.CT.getVarWing() > 0.02F && !bFlapsInFixed)
            {
                if(!bStableAIGround)
                {
                    Polares polares = (Polares)Reflection.getValue(FM, "Wing");
                    polares.Cy0_1 = 0.14F * stockCy0_0 + 0.86F * stockCy0_1;
                    polares.CxMin_1 = 0.14F * stockCxMin_0 + 0.86F * stockCxMin_1;
                    polares.parabCxCoeff_1 = 0.14F * stockparabCxCoeff_0 + 0.86F * stockparabCxCoeff_1;
                    FM.Sq.squareFlaps = 0.90F * stockSqFlaps;
                }
                bFlapsInFixed = true;
                bTakeoffFlapAssist = false;
            }
            else if(FM.CT.getVarWing() <= 0.02F && !bTakeoffFlapAssist && bFlapsInFixed)
            {
                if(!bStableAIGround)
                {
                    Polares polares = (Polares)Reflection.getValue(FM, "Wing");
                    polares.Cy0_1 = stockCy0_1;
                    polares.CxMin_1 = stockCxMin_1;
                    polares.parabCxCoeff_1 = stockparabCxCoeff_1;
                    FM.Sq.squareFlaps = stockSqFlaps;
                }
                bFlapsInFixed = false;
            }
        }

        moveFlap(FM.CT.getFlap());
    }


    public void computeLift()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float x = this.calculateMach();
        if(this.calculateMach() >= 0.0F);
        float Lift = 0.0F;
        if((double)x > 2.2F)
        {
            Lift = 0.03F;
        } else
        {
            float x2 = x * x;
            float x3 = x2 * x;
            float x4 = x3 * x;
            float x5 = x4 * x;
            float x6 = x5 * x;
            float x7 = x6 * x;
            float x8 = x7 * x;
            Lift = 0.00458079F * x7 - 0.0605523F * x6 + 0.294479F * x5 - 0.685669F * x4 + 0.821472F * x3 - 0.508206F * x2 + 0.123823F * x + 0.1F;
            //{{0.0, 0.1},{0.2, 0.11}, {0.6, 0.1},{0.97, 0.09}, {1.4, 0.07},{1.9, 0.04}, {2.2, 0.03}, {2.5, 0.02}}
            }
        polares.lineCyCoeff= Lift;  // westerntemp
    }


    public void computeEnergy()
    {
        float x = this.FM.getOverload();
        if(this.FM.getOverload() < 4.5F)
        {
            float Energy = 0.0F;
            if((double)x >=10F)
            {
                Energy = 0.07F;
            } else
            {
                float x2 = x * x;
                float x3 = x2 * x;
                float x4 = x3 * x;
                float x5 = x4 * x;
                float x6 = x5 * x;
                Energy = 0.0000006734F * x5 + 0.000000987654F * x4 - 0.00000757583F * x3 + 0.00000222222F * x2 + 0.0000170512F * x;
            //{{-3,0.0001},{-1.5,0.00001},{0,0},{1.5,0.00001},{3,0.0001},{10,0.07}}

            }
            FM.Sq.dragParasiteCx += Energy;
        }
    }


    public void computeSupersonicLimiter()
    {
        float x = FM.getAltitude() / 1000F;
        float Drag = 0.0F;
        if(this.calculateMach() >= 1.19F)
        {
            if(x > 2.5F)
            {
                Drag = 0.0F;
            }
            else
            {
                float x2 = x * x;
                Drag = 0.0008F - 0.00032F * x;
                //{{0,0.0008},{2.5, 0}}
            }
            FM.Sq.dragParasiteCx += Drag;
         }
    }

    protected void moveVarWing(float f)
    {
        float f1 = 55F * f;
        hierMesh().chunkSetAngles("WingPivotL", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("WingPivotR", 0.0F, f1, 0.0F);
    }


    public void computeVarWing()
    {
        if(FM == null || !(FM instanceof Pilot) || !FM.CT.bHasVarWingControl)
            return;

        Polares polares = (Polares)Reflection.getValue(FM, "Wing");

        if((!(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode()) && (FM instanceof Maneuver))
        {
            //When AI goes 'GAttack' waypoint, set VarWingControlSwitch = "BOMB". Others "AUTO".
            if(FM.AP.way.curr().Action == 3 || (((Maneuver)FM).Group != null && ((Maneuver)FM).Group.grTask == 4) || ((Maneuver)FM).get_task() == 4)
                FM.CT.VarWingControlSwitch = 4;
            else 
                FM.CT.VarWingControlSwitch = 0;
        }

        float target_value = 0.0F;
      
        // VarWing degree decision part
        switch(FM.CT.VarWingControlSwitch)
        {
        default:
        case 0:   // AUTO
            if(calculateMach() < 0.435F)
                target_value = 0F;   // 20 deg.
            else if(calculateMach() < 0.55F)
                target_value = 0.036364F;   // 22 deg.
            else if(calculateMach() < 0.97F)
            {
                if(FM.getAltitude() > 6090F)   // over 20000 feet
                {
                    if(calculateMach() < 0.64F)
                        target_value = 0.036364F;   // 22 deg.
                    else if(calculateMach() < 0.7F)
                        target_value = cvt(calculateMach(), 0.64F, 0.7F, 0.036364F, 0.0964F);   // 22 ~ 25.88 deg.
                    else if(calculateMach() < 0.738F)
                        target_value = cvt(calculateMach(), 0.7F, 0.738F, 0.0964F, 0.1396F);   // 25.88 ~ 28.53 deg.
                    else if(calculateMach() < 0.776F)
                        target_value = cvt(calculateMach(), 0.738F, 0.776F, 0.1396F, 0.2045F);   // 28.53 ~ 32.5 deg.
                    else if(calculateMach() < 0.8F)
                        target_value = cvt(calculateMach(), 0.7776F, 0.8F, 0.2045F, 0.3F);   // 32.5 ~ 37.869 deg.
                    else if(calculateMach() < 0.87222F)
                        target_value = cvt(calculateMach(), 0.8F, 0.87222F, 0.3F, 0.49F);   // 37.869 ~ 50 deg.
                    else
                        target_value = cvt(calculateMach(), 0.87222F, 0.97F, 0.49F, 0.6555F);   // 50 ~ 60 deg.
                }
                else   // below 20000 feet
                {
                    if(calculateMach() < 0.6F)
                        target_value = cvt(calculateMach(), 0.55F, 0.6F, 0.036364F, 0.0964F);   // 22 ~ 25.88 deg.
                    else if(calculateMach() < 0.66F)
                        target_value = cvt(calculateMach(), 0.6F, 0.66F, 0.0964F, 0.1636F);   // 25.88 ~ 30 deg.
                    else if(calculateMach() < 0.75F)
                        target_value = cvt(calculateMach(), 0.66F, 0.75F, 0.1636F, 0.2636F);   // 30 ~ 36.11 deg.
                    else if(calculateMach() < 0.8F)
                        target_value = cvt(calculateMach(), 0.75F, 0.8F, 0.2636F, 0.3272F);   // 36.11 ~ 40 deg.
                    else if(calculateMach() < 0.87222F)
                        target_value = cvt(calculateMach(), 0.8F, 0.87222F, 0.3272F, 0.49F);   // 40 ~ 50 deg.
                    else
                        target_value = cvt(calculateMach(), 0.87222F, 0.97F, 0.49F, 0.6555F);   // 50 ~ 60 deg.
                }
            }
            else if(calculateMach() < 1.1F)
                target_value = cvt(calculateMach(), 0.97F, 1.1F, 0.6555F, 0.7373F);   // 60 ~ 65 deg.
            else
                target_value = cvt(calculateMach(), 1.1F, 1.2F, 0.7373F, 0.8F);   // 65 ~ 68 deg.
            oldVarWingControlSwitch = 0;
            break;
        case 1:   // Extend
            if(oldVarWingControlSwitch != 1)
                tVarWingInput = Time.current();
            if(Time.current() > tVarWingInput + 400L)
            {
                target_value = FM.CT.VarWingControl - 0.005F;
                if(target_value < 0.0F)
                    target_value = 0.0F;
            }
            else
                target_value = FM.CT.VarWingControl;
            oldVarWingControlSwitch = 1;
            break;
        case 2:   // Keep
            target_value = FM.CT.VarWingControl;
            if(calculateMach() < 0.97F && target_value > 0.65549F)
                target_value = 0.65549F;
            oldVarWingControlSwitch = 2;
            break;
        case 3:   // Retract
            if(oldVarWingControlSwitch != 3)
                tVarWingInput = Time.current();
            if(Time.current() > tVarWingInput + 400L)
            {
                target_value = FM.CT.VarWingControl + 0.005F;
                if(FM.CT.getWing() < 0.8F && target_value > 0.8F)
                    target_value = 0.8F;
                if(calculateMach() < 0.97F && FM.CT.getWing() < 0.65549F && target_value > 0.65549F)
                    target_value = 0.65549F;
            }
            else
                target_value = FM.CT.VarWingControl;
            oldVarWingControlSwitch = 3;
            break;
        case 4:   // BOMB; fixed 55 degrees
            target_value = 0.6034F;
            oldVarWingControlSwitch = 4;
            break;
        }
        if(target_value < varWingExtendLimitter())
            target_value = varWingExtendLimitter();
        FM.CT.VarWingControl = Math.max(target_value, FM.CT.getWing());

        // VarWing flight charactoristic part
        if(!bStableAIGround)
        {
            float fsweep = 0F;
            if(FM.CT.getVarWing() < 0.24F)
                fsweep = cvt(FM.CT.getVarWing(), 0.0F, 0.24F, 0.0F, 1.0F);
            else if(FM.CT.getVarWing() < 0.4F)
                fsweep = cvt(FM.CT.getVarWing(), 0.24F, 0.4F, 1.0F, 2.0F);
            else if(FM.CT.getVarWing() < 0.64F)
                fsweep = cvt(FM.CT.getVarWing(), 0.4F, 0.64F, 2.0F, 3.0F);
            else
                fsweep = cvt(FM.CT.getVarWing(), 0.64F, 0.8F, 3.0F, 4.0F);
            Reflection.setFloat(FM, "GCenter", stockGCenter + floatindex(fsweep, gcenterScale));
            Reflection.setFloat(FM, "DefaultElevatorTrim", stockDefaultElevatorTrim + floatindex(fsweep, defaultelevtrimScale));
            polares.CxMin_0 = floatindex(fsweep, cxmin0Scale);
            polares.Cy0_0 = floatindex(fsweep, cy00Scale);
            polares.CyCritH_0 = floatindex(fsweep, cycrith0Scale);
            FM.Sq.squareWing = stockSqWing - floatindex(fsweep, wingsquredecScale) * 2F;
            FM.Sq.liftWingLOut = FM.Sq.liftWingROut = stockSqWingOut - floatindex(fsweep, wingsquredecScale);
            FM.Wingspan = floatindex(fsweep, wingspanScale);
        }
    }

    private float varWingExtendLimitter()
    {
        float limit = 0F;
        if(calculateMach() < 0.435F)
            limit = 0F;   // 20 deg.
        else if(calculateMach() < 0.75F)
            limit = 0.036364F;   // 22 deg.
        else if(calculateMach() < 0.8F)
            limit = cvt(calculateMach(), 0.75F, 0.8F, 0.036364F, 0.3F);   // 22 ~ 37.869 deg.
        else if(calculateMach() < 0.87222F)
            limit = cvt(calculateMach(), 0.8F, 0.87222F, 0.3F, 0.49F);   // 37.869 ~ 50 deg.
        else if(calculateMach() < 0.97F)
            limit = cvt(calculateMach(), 0.87222F, 0.97F, 0.49F, 0.6555F);   // 50 ~ 60 deg.
        else if(calculateMach() < 1.1F)
            limit = cvt(calculateMach(), 0.97F, 1.1F, 0.6555F, 0.7373F);   // 60 ~ 65 deg.
        else
            limit = cvt(calculateMach(), 1.1F, 1.2F, 0.7373F, 0.8F);   // 65 ~ 68 deg.

        return limit;
    }
  
    public void computeSubsonicLimiter()
       { 
        float x = this.calculateMach();
        float Drag = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && FM.EI.engines[0].getStage() == 6 && (double)calculateMach() >= 0.9 && this instanceof com.maddox.il2.objects.air.F_14A) 
        if (x > 0.97) 
	{
	Drag = 0.0025F;
	} else{ 
		float x2 = x * x; 
		Drag = 0.0285714F * x - 0.0252143F;
		//{{0.9,0.0005},{0.97, 0.0025}}
	}
	((FlightModelMain) (super.FM)).Sq.dragParasiteCx += Drag;
         }


    private void FlapAssistTakeoff()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");

        if(bSpawnedAsAI)
        {
            if(FM.Gears.onGround() && FM.getSpeedKMH() < 160F && !bStableAIGround)
            {
                polares.Cy0_0 = stockCy0_0 * 0.01F;
                polares.Cy0_1 = stockCy0_1 * 0.1F;
                Reflection.setFloat(FM, "GCenter", stockGCenter);
                FM.Sq.liftStab -= 3.0F;
                FM.Sq.squareElevators -= 6.1F;
                bStableAIGround = true;
            }
            if(FM.getSpeedKMH() > 160F && bStableAIGround)
            {
                polares.Cy0_0 = stockCy0_0;
                polares.Cy0_1 = stockCy0_1;
                Reflection.setFloat(FM, "GCenter", stockGCenter);
                FM.Sq.liftStab += 3.0F;
                FM.Sq.squareElevators += 6.1F;
                bStableAIGround = false;
            }

            if(FM.Gears.nearGround() && FM.getSpeedKMH() > 220F && !bTakeoffAIAssist)
            {
                FM.Sq.liftStab += 1.5F;
                FM.Sq.squareElevators += 3.0F;
                bTakeoffAIAssist = true;
            }
            else if(bTakeoffAIAssist)
            {
                FM.Sq.liftStab -= 1.5F;
                FM.Sq.squareElevators -= 3.0F;
                bTakeoffAIAssist = false;
            }
        }

        if(bFlapsOutFixed)
            return;

        if(FM.EI.engines[0].getThrustOutput() > 0.97F && FM.EI.engines[1].getThrustOutput() > 0.97F && ((double)calculateMach() < 0.32D || FM.Gears.onGround()) && !bTakeoffFlapAssist && !bStableAIGround)
        {
            polares.Cy0_1 += (bSpawnedAsAI ? 1.8D : 0.8D);
            bTakeoffFlapAssist = true;
        }
        if(bTakeoffFlapAssist && !FM.Gears.nearGround())
        {
            if((FM.EI.engines[0].getThrustOutput() < 0.97F && FM.EI.engines[1].getThrustOutput() < 0.97F) || (double)calculateMach() >= 0.32D)
            {
                polares.Cy0_1 -= (bSpawnedAsAI ? 1.8D : 0.8D);
                bTakeoffFlapAssist = false;
            }
        }
        return;
    }

    private void AiAirbrakeOperation()
    {
        if(FM.AP.way.isLanding() && FM.getSpeed() > FM.VmaxFLAPS && FM.getSpeed() > FM.AP.way.curr().getV() * 1.4F)
        {
            if(FM.CT.AirBrakeControl != 1.0F)
                FM.CT.AirBrakeControl = 1.0F;
        } else
        if(((Maneuver)super.FM).get_maneuver() == 25 && FM.AP.way.isLanding() && FM.getSpeed() < FM.VmaxFLAPS * 1.2F)
        {
            if(FM.getSpeed() > FM.VminFLAPS * 0.5F && FM.Gears.nearGround())
            {
                if(FM.Gears.onGround())
                {
                    if(FM.CT.AirBrakeControl != 1.0F)
                        FM.CT.AirBrakeControl = 1.0F;
                } else
                if(FM.CT.AirBrakeControl != 1.0F)
                    FM.CT.AirBrakeControl = 1.0F;
            } else
            if(FM.CT.AirBrakeControl != 0.0F)
                FM.CT.AirBrakeControl = 0.0F;
        } else
        if(((Maneuver)super.FM).get_maneuver() == 66)
        {
            if(FM.CT.AirBrakeControl != 0.0F)
                FM.CT.AirBrakeControl = 0.0F;
        } else
        if(((Maneuver)super.FM).get_maneuver() == 7)
        {
            if(FM.CT.AirBrakeControl != 1.0F)
                FM.CT.AirBrakeControl = 1.0F;
        } else
        if(hasHydraulicPressure && FM.CT.AirBrakeControl != 0.0F)
            FM.CT.AirBrakeControl = 0.0F;
    }

    private final void doRemoveBlisters()
    {
        for(int i = 1; i < 10; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && FM.AS.getPilotHealth(i - 1) > 0.0F)
            {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(FM.Vwld);
                wreckage.setSpeed(vector3d);
            }

    }

    private void anticollights()
    {
        if(FM.CT.bAntiColLights)
        {
            for(int i = 0; i < 3; i++)
            {
                if(antiColLight[i] == null)
                {
                    try
                    {
                        antiColLight[i] = Eff3DActor.New(this, findHook("_AntiColLight" + Integer.toString(i + 1)), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRedFlash.eff", -1.0F, false);
                    } catch(Exception excp) {}
                }
            }
        }
        else
        {
            for(int i = 0; i < 3; i++)
              if(antiColLight[i] != null)
              {
                  Eff3DActor.finish(antiColLight[i]);
                  antiColLight[i] = null;
              }
        }
    }

    private void formationlights()
    {
        Mission.cur();
        int ws = Mission.curCloudsType();
        Mission.cur();
        float we = Mission.curCloudsHeight() + 500F;
        if((World.getTimeofDay() <= 6.5F || World.getTimeofDay() > 18F || ws > 4 && FM.getAltitude() < we) && !FM.isPlayers())
            FM.CT.bFormationLights = true;
        if((World.getTimeofDay() > 6.5F && World.getTimeofDay() <= 18F && ws <= 4 || World.getTimeofDay() > 6.5F && World.getTimeofDay() <= 18F && FM.getAltitude() > we) && !FM.isPlayers())
            FM.CT.bFormationLights = false;
        hierMesh().chunkVisible("SlightNose", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightTail", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightKeelL", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightKeelR", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightWTipL", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightWTipR", FM.CT.bFormationLights);
    }


    public float Fuelamount;
    public boolean radartogle;
    public int radarmode;
    public int targetnum;
    public float lockrange;
    public int radargunsight;
    public int leftscreen;
    public int Bingofuel;
    public boolean Nvision;
    public int lockmode;
    private boolean APmode1;
    private boolean APmode2;
    private boolean APmode3;
    public boolean ILS;
    public float azimult;
    public float tangate;
    public long tf;
    public float v;
    public float h;
    public float misslebrg;
    public float aircraftbrg;
    public boolean backfire;
    private float oldthrl;
    private float curthrl;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private float lightTime;
    private float ft;
    private LightPointWorld lLight[];
    private Hook lLightHook[];
    private Loc lLightLoc1 = new Loc();
    private Point3d lLightP1 = new Point3d();
    private Point3d lLightP2 = new Point3d();
    private Point3d lLightPL = new Point3d();
    private boolean ictl;
    private float mn;
    private static float lteb = 0.92F;
    private boolean ts;
    public boolean bChangedPit = false;
    private float SonicBoom;
    private Eff3DActor shockwave;
    private boolean isSonic;
    public int LockState = 0;
    public Actor hunted = null;
    private float engineSurgeDamage;
    private static final float NEG_G_TOLERANCE_FACTOR = 3.5F;
    private static final float NEG_G_TIME_FACTOR = 2.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 7F;
    private static final float POS_G_TIME_FACTOR = 3F;
    private static final float POS_G_RECOVERY_FACTOR = 5F;
    private long lTimeNextEject;
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
    public boolean bToFire;
    private float deltaAzimuth;
    private float deltaTangage;
//    private static Loc l = new Loc();
    public boolean FLIR;
    private SoundFX fxRWR;
    private Sample smplRWR;
    private boolean RWRSoundPlaying;
    private SoundFX fxMissileWarning;
    private Sample smplMissileWarning;
    private boolean MissileSoundPlaying;
    public boolean bRadarWarning;
    public boolean bMissileWarning;
    public boolean hold;
    public boolean holdFollow;
    public Actor actorFollowing;
    private Eff3DActor pull01;
    private Eff3DActor pull02;
    private Eff3DActor pull03;
    private Eff3DActor pull04;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    public boolean trimauto;
    private long t1;
    public int radarrange;
    private Field thrustMaxField[];
    public float fNozzleOpenL;
    public float fNozzleOpenR;
    private Field elevatorsField;
    public static float FlowRate = 10F;
    public static float FuelReserve = 1500F;
    private boolean bForceTakeoffElTrim;
    public boolean bForceFlapmodeAuto;
    private float oldTrimElevator;
    public boolean bHasCenterTank;
    public boolean bHasWingTank;
    private boolean bSpawnedAsAI;
    private float actl;
    private float rctl;
    private float ectl;
    private long tVarWingInput;
    private int oldVarWingControlSwitch;
    public boolean hasHydraulicPressure;
    private boolean bTakeoffFlapAssist;
    private boolean bTakeoffAIAssist;
    private boolean bStableAIGround;
    private float arrestor;
    private boolean autoEng;
    private float curFlaps;
    private float desiredPosition;
    private boolean bFlapsOutFixed;
    private boolean bFlapsInFixed;
    private float stockCy0_0;
    private float stockCy0_1;
    private float stockCxMin_0;
    private float stockCxMin_1;
    private float stockCyCritH_0;
    private float stockGCenter;
    private float stockDefaultElevatorTrim;
    private float stockparabCxCoeff_0;
    private float stockparabCxCoeff_1;
    private float stockSqWing;
    private float stockSqWingOut;
    private float stockSqFlaps;
    private Eff3DActor antiColLight[];

    public boolean bCarryLaserpod = false;
    private Point3d laserSpotPos;
    private boolean bLaserOn = false;
    public long laserTimer;
    private boolean bLGBengaged = false;
    public boolean bHasPaveway = false;
    private static float maxPavewayFOVfrom = 45.0F;
    private static double maxPavewayDistance = 20000D;


    private static final float gcenterScale[] = {
        0.0F, 0.0F, -0.7F, -1.4F, -2F
    };

    private static final float defaultelevtrimScale[] = {
        0.0F, 0.0F, 0.0F, -4F, -14F
    };

    private static final float cxmin0Scale[] = {
        0.04F, 0.035F, 0.032F, 0.030F, 0.028F
    };

    private static final float cy00Scale[] = {
        0.13F, 0.07F, 0.04F, 0.02F, 0.011F
    };

    private static final float cycrith0Scale[] = {
        1.4F, 1.3F, 1.2F, 1.15F, 1.1F
    };

    private static final float wingspanScale[] = {
        19.55F, 17.6F, 16.1F, 13.5F, 11.7F
    };

    private static final float wingsquredecScale[] = {
        0.0F, 0.4F, 1.0F, 2.0F, 2.8F
    };

    static
    {
        Class class1 = com.maddox.il2.objects.air.F_14.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}