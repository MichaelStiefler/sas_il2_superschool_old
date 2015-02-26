package com.maddox.il2.objects.air;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.il2.objects.weapons.Missile;
import com.maddox.il2.objects.weapons.MissileSAM;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sound.SoundFX;

public class F_16 extends Scheme1
    implements TypeSupersonic, TypeFighter, TypeFighterAceMaker, TypeGSuit, TypeFastJet, TypeRadar, TypeBomber, TypeX4Carrier, TypeLaserSpotter, TypeStormovikArmored, TypeSemiRadar, TypeGroundRadar
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

    public F_16()
    {
        elevatorsField = null;
        tflap = 0L;
        bForceFlapmodeAuto = false;
        lLightHook = new Hook[4];
        SonicBoom = 0.0F;
        bSlatsOff = false;
        oldctl = -1F;
        curctl = -1F;
        oldthrl = -1F;
        curthrl = -1F;
        k14Mode = 2;
        k14WingspanType = 0;
        k14Distance = 200F;
        AirBrakeControl = 0.0F;
        DragChuteControl = 0.0F;
        overrideBailout = false;
        ejectComplete = false;
        lightTime = 0.0F;
        ft = 0.0F;
        mn = 0.0F;
        ts = false;
        ictl = false;
        engineSurgeDamage = 0.0F;
        gearTargetAngle = -1F;
        gearCurrentAngle = -1F;
        hasHydraulicPressure = true;
        radarmode = 0;
        targetnum = 0;
        lockrange = 0.04F;
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 3000F;
        fSightCurSpeed = 200F;
        fSightCurReadyness = 0.0F;
        clipBoardPage_ = 1;
        showClipBoard_ = false;
        missilesList = new ArrayList();
        lockmode = 0;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        radargunsight = 0;
        leftscreen = 0;
        Bingofuel = 1000;
        radarrange = 1;
        bDynamoOperational = true;
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
        hold = false;
        t1 = 0L;
        tangate = 0.0F;
        azimult = 0.0F;
        tf = 0L;
        APmode1 = false;
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
        fxMissileWarning = newSound("aircraft.MissileMissile", false);
        misslebrg = 0.0F;
        aircraftbrg = 0.0F;
        FL = false;
        thrustMaxField = new Field[2];
        thrustMaxFromEmd = new float[2];
    }

    public float checkfuel(int i)
    {
        FuelTank fuelTanks[] = this.FM.CT.getFuelTanks();
        if(fuelTanks.length == 0)
            return 0.0F;
        for(i = 0; i < fuelTanks.length; i++)
            Fuelamount = fuelTanks[i].Fuel;

        return Fuelamount;
    }

    private static final float toMeters(float f)
    {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f)
    {
        return 0.4470401F * f;
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
                HUD.log("Lazer Unlock");
                t1 = Time.current();
            }
            if(!hold && t1 + 200L < Time.current() && FLIR)
            {
                hold = true;
                HUD.log("Lazer Lock");
                t1 = Time.current();
            }
        }
        if(i == 27)
            if(!ILS)
            {
                ILS = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ILS ON");
            } else
            {
                ILS = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ILS OFF");
            }
        if(i == 28)
            if(!FL)
            {
                FL = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "FL ON");
            } else
            {
                FL = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "FL OFF");
            }
    }

    private boolean RWRWarning()
    {
        boolean SPIKE = false;
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft spike = War.getNearestEnemy(this, 6000F);
        if(spike != null)
        {
            double d = Main3D.cur3D().land2D.worldOfsX() + spike.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + spike.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + spike.pos.getAbsPoint().z;
            double d3 = d2 - (double)Landscape.Hmin((float)spike.pos.getAbsPoint().x, (float)spike.pos.getAbsPoint().y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)((Actor) (spike)).pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i += 360;
            int j = (int)(-((double)((Actor) (spike)).pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j += 360;
            Actor actor = War.getNearestEnemy(spike, 6000F);
            if((actor instanceof Aircraft) && spike.getArmy() != World.getPlayerArmy() && (spike instanceof TypeFighterAceMaker) && ((spike instanceof TypeSupersonic) || (spike instanceof TypeFastJet)) && actor == World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
            {
                super.pos.getAbs(point3d);
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
                    j1 += 360;
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
                    SPIKE = true;
                else
                    SPIKE = false;
            } else
            {
                SPIKE = false;
            }
            Aircraft aircraft = World.getPlayerAircraft();
            double dd = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
            double dd1 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
            double dd2 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
            int ii = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
            if(ii < 0)
                ii += 360;
            if(SPIKE && actor == World.getPlayerAircraft() && (actor instanceof F_18))
            {
                super.pos.getAbs(point3d);
                double d31 = Main3D.cur3D().land2D.worldOfsX() + spike.pos.getAbsPoint().x;
                double d41 = Main3D.cur3D().land2D.worldOfsY() + spike.pos.getAbsPoint().y;
                double d51 = Main3D.cur3D().land2D.worldOfsY() + spike.pos.getAbsPoint().z;
                double d81 = (int)(Math.ceil((dd2 - d51) / 10D) * 10D);
                String s = "";
                if(dd2 - d51 - 500D >= 0.0D)
                    s = " low";
                if((dd2 - d51) + 500D < 0.0D)
                    s = " high";
                new String();
                double d91 = d31 - dd;
                double d101 = d41 - dd1;
                float f11 = 57.32484F * (float)Math.atan2(d101, -d91);
                int j11 = (int)(Math.floor((int)f11) - 90D);
                if(j11 < 0)
                    j11 += 360;
                int k11 = j11 - ii;
                if(k11 < 0)
                    k11 += 360;
                int l11 = (int)(Math.ceil((double)(k11 + 15) / 30D) - 1.0D);
                if(l11 < 1)
                    l11 = 12;
                double d111 = dd - d31;
                double d12 = dd1 - d41;
                double d13 = Math.ceil(Math.sqrt(d12 * d12 + d111 * d111) / 10D) * 10D;
                if(bMissileWarning)
                {
                    bRadarWarning = false;
                    playRWRWarning();
                } else
                {
                    bRadarWarning = d13 <= 8000D && d13 >= 500D && Math.sqrt(d81 * d81) <= 6000D;
                    aircraftbrg = Aircraft.cvt(l11, 0.0F, 12F, 0.0F, 360F);
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Enemy at " + l11 + " o'clock" + s + "!");
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
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Actor actor;
        if((this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode() || !(this.FM instanceof Pilot))
            actor = World.getPlayerAircraft();
        else
            actor = this;
        super.pos.getAbs(point3d);
        Aircraft aircraft;
        if((this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode() || !(this.FM instanceof Pilot))
            aircraft = World.getPlayerAircraft();
        else
            aircraft = this;
        double dd = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
        double dd1 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
        double dd2 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
        int ii = (int)(-aircraft.pos.getAbsOrient().getYaw() - 90D);
        if(ii < 0)
            ii += 360;
        List list = Engine.missiles();
        int m = list.size();
        for(int t = 0; t < m; t++)
        {
            Actor missile = (Actor)list.get(t);
            if(((missile instanceof Missile) || (missile instanceof MissileSAM)) && missile.getSpeed(vector3d) > 20D && ((Missile)missile).getMissileTarget() == this)
            {
                super.pos.getAbs(point3d);
                double d31 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (missile.pos.getAbsPoint())).x;
                double d41 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (missile.pos.getAbsPoint())).y;
                double d51 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (missile.pos.getAbsPoint())).z;
                double d81 = (int)(Math.ceil((dd2 - d51) / 10D) * 10D);
                String s = "";
                if(dd2 - d51 - 500D >= 0.0D)
                    s = " LOW";
                if((dd2 - d51) + 500D < 0.0D)
                    s = " HIGH";
                new String();
                double d91 = d31 - dd;
                double d101 = d41 - dd1;
                float f11 = 57.32484F * (float)Math.atan2(d101, -d91);
                int j11 = (int)(Math.floor((int)f11) - 90D);
                if(j11 < 0)
                    j11 += 360;
                int k11 = j11 - ii;
                if(k11 < 0)
                    k11 += 360;
                int l11 = (int)(Math.ceil((double)(k11 + 15) / 30D) - 1.0D);
                if(l11 < 1)
                    l11 = 12;
                double d111 = dd - d31;
                double d12 = dd1 - d41;
                double d13 = Math.ceil(Math.sqrt(d12 * d12 + d111 * d111) / 10D) * 10D;
                bMissileWarning = true;
                if((this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode() || !(this.FM instanceof Pilot))
                {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "MISSILE AT " + l11 + " O'CLOCK" + s + "!!!" + misslebrg);
                    playRWRWarning();
                    misslebrg = Aircraft.cvt(l11, 0.0F, 12F, 0.0F, 360F);
                }
                if((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && (this.FM instanceof Maneuver))
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
    }

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
        if(FLIR)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Engaged");
                this.FM.AP.setStabAltitude(2000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Released");
                this.FM.AP.setStabAltitude(false);
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
        if((double)Math.abs(this.FM.Or.getKren()) > 4.5D)
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
            if(fSightCurDistance < toMetersPerSecond(fSightCurSpeed) * Math.sqrt(toMeters(fSightCurAltitude) * 0.2038736F))
                bSightBombDump = true;
            if(bSightBombDump)
                if(this.FM.isTick(3, 0))
                {
                    if(this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets())
                    {
                        this.FM.CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else
                {
                    this.FM.CT.WeaponControl[3] = false;
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
        point3d.z = World.land().HQ(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y);
        Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y, ((Tuple3d) (point3d)).z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
        eff3dactor.postDestroy(Time.current() + 1500L);
    }

    private void FLIR()
    {
        List list = Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if(((actor instanceof Aircraft) || (actor instanceof ArtilleryGeneric) || (actor instanceof CarGeneric) || (actor instanceof TankGeneric)) && !(actor instanceof StationaryGeneric) && !(actor instanceof TypeLaserSpotter) && actor.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 20000D)
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
                ((Actor) (eff3dactor)).draw.lightMap().put("light", lightpointactor);
            }
        }

    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        t1 = Time.current();
        this.FM.LimitLoad = 9F;
        if(this.FM instanceof RealFlightModel)
            Reflection.invokeMethod(this.FM, "init_G_Limits");
        this.FM.CT.toggleRocketHook();
        getThrustMaxFromEmd();
        if((this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode() || !(this.FM instanceof Pilot))
        {
            this.FM.Sq.squareElevators += 2.0F;
            this.FM.Sq.liftStab += 2.0F;
        }
    }

    public void checkHydraulicStatus()
    {
        if(this.FM.EI.engines[0].getStage() < 6 && this.FM.Gears.nOfGearsOnGr > 0)
        {
            gearTargetAngle = 90F;
            hasHydraulicPressure = false;
            this.FM.CT.bHasAileronControl = false;
            this.FM.CT.bHasElevatorControl = false;
            this.FM.CT.AirBrakeControl = 0.0F;
        } else
        if(!hasHydraulicPressure)
        {
            gearTargetAngle = 0.0F;
            hasHydraulicPressure = true;
            this.FM.CT.bHasAileronControl = true;
            this.FM.CT.bHasElevatorControl = true;
            this.FM.CT.bHasAirBrakeControl = true;
        }
    }

    public void moveHydraulics(float f)
    {
        if(gearTargetAngle >= 0.0F)
            if(gearCurrentAngle < gearTargetAngle)
            {
                gearCurrentAngle += 90F * f * 0.8F;
                if(gearCurrentAngle >= gearTargetAngle)
                {
                    gearCurrentAngle = gearTargetAngle;
                    gearTargetAngle = -1F;
                }
            } else
            {
                gearCurrentAngle -= 90F * f * 0.8F;
                if(gearCurrentAngle <= gearTargetAngle)
                {
                    gearCurrentAngle = gearTargetAngle;
                    gearTargetAngle = -1F;
                }
            }
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
                if(this.FM.AS.astateLandingLightEffects[j] != null)
                {
                    lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP1);
                    lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP2);
                    Engine.land();
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
                } else
                if(lLight[j].getR() != 0.0F)
                    lLight[j].setEmit(0.0F, 0.0F);

        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(this.FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(this.FM.isPlayers())
            bChangedPit = true;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(this.FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            Vector3d vector3d = this.FM.getVflow();
            mn = (float)vector3d.lengthSquared();
            mn = (float)Math.sqrt(mn);
            F_16 F_16 = this;
            float f1 = mn;
            World.cur().getClass();
            F_16.mn = f1 / Atmosphere.sonicSpeed((float)((Tuple3d) (this.FM.Loc)).z);
            if(mn >= 0.9F && (double)mn < 1.1000000000000001D)
                ts = true;
            else
                ts = false;
            ft = World.getTimeofDay() % 0.01F;
            if(ft == 0.0F)
                UpdateLightIntensity();
        }
        hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
        if(FLIR)
            FLIR();
        if(!FLIR)
            this.FM.AP.setStabAltitude(false);
        if(!this.FM.isPlayers())
            if(((Maneuver)this.FM).get_maneuver() == 25 && this.FM.AP.way.isLanding())
                this.FM.CT.FlapsControlSwitch = 2;
            else
            if(((Maneuver)this.FM).get_maneuver() == 26)
                this.FM.CT.FlapsControlSwitch = 1;
            else
                this.FM.CT.FlapsControlSwitch = 0;
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
        if(k14Distance > 800F)
            k14Distance = 800F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 10F;
        if(k14Distance < 200F)
            k14Distance = 200F;
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
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
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
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 60D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(10, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat1_D0", false);
        this.FM.setTakenMortalDamage(true, null);
        this.FM.CT.WeaponControl[0] = false;
        this.FM.CT.WeaponControl[1] = false;
        this.FM.CT.bHasAileronControl = false;
        this.FM.CT.bHasRudderControl = false;
        this.FM.CT.bHasElevatorControl = false;
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 0.0F, 40F * f);
        hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 0.0F, 40F * f);
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 0.0F, 30F * f);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 90F * f);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearC55_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, 85F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 45F * f, -90F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -45F * f, -90F * f);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
        resetYPRmodifier();
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        float f = this.FM.Gears.gWheelSinking[2];
        Aircraft.xyz[2] = -f;
        hierMesh().chunkSetLocate("GearC211_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -20F * f, 0.0F);
        resetYPRmodifier();
        f = this.FM.Gears.gWheelSinking[0] + this.FM.Gears.gWheelSinking[1];
        Aircraft.xyz[1] = -f / 2.0F;
        hierMesh().chunkSetLocate("GearB21_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetAngles("GearB3_D0", 0.0F, 20F * f, 0.0F);
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

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
    }

    public void moveSteering(float f)
    {
        if(this.FM.CT.GearControl > 0.5F && this.FM.Gears.onGround())
            hierMesh().chunkSetAngles("GearC7_D0", 0.0F, -30F * f, 0.0F);
        if(this.FM.CT.GearControl < 0.5F)
            hierMesh().chunkSetAngles("GearC7_D0", 0.0F, 0.0F, 0.0F);
    }

    protected void moveElevator(float f)
    {
        updateControlsVisuals();
    }

    protected void moveAileron(float f)
    {
        updateControlsVisuals();
        if(this.FM.getSpeedKMH() > 570F)
        {
            float f1 = 2.0F * f;
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 30F * f1);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -30F * f1);
        } else
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 30F * f);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -30F * f);
        }
    }

    private final void updateControlsVisuals()
    {
        if(this.FM.getSpeedKMH() > 590F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -30F * this.FM.CT.getElevator() + 17F * this.FM.CT.getAileron());
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -30F * this.FM.CT.getElevator() - 17F * this.FM.CT.getAileron());
        } else
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -17F * this.FM.CT.getElevator() + 10F * this.FM.CT.getAileron());
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -17F * this.FM.CT.getElevator() - 10F * this.FM.CT.getAileron());
        }
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 0.0F, 30F * f);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 0.0F, 30F * f);
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
                    if(shot.power <= -7F)
                        doRicochetBack(shot);
                } else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(8.770001F, shot);
                else
                if(s.endsWith("g1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(40F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if(shot.power <= -7F)
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
                    if(World.Rnd().nextFloat() < -10.5F && getEnergyPastArmor(1.1F, shot) > 200F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3: // '\003'
                case 4: // '\004'
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 200F && World.Rnd().nextFloat() < -10.25F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 200F && World.Rnd().nextFloat() < -10.25F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;
                }
            } else
            if(s.startsWith("xxengine1"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("bloc"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 20F)
                {
                    this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 5800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 44000F)
                    {
                        this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F)
                    {
                        this.FM.AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 44000F)
                {
                    this.FM.AS.hitEngine(shot.initiator, 0, 3);
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
                    this.FM.AS.setJamBullets(0, k);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(this.FM.AS.astateTankStates[l] == 0)
                    {
                        debuggunnery("Fuel Tank (" + l + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, l, 1);
                        this.FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        this.FM.AS.hitTank(shot.initiator, l, 2);
                        debuggunnery("Fuel Tank (" + l + "): Hit..");
                    }
                }
            } else
            if(s.startsWith("xxhyd"))
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            else
            if(s.startsWith("xxpnm"))
                this.FM.AS.setInternalDamage(shot.initiator, 1);
        } else
        {
            if(s.startsWith("xcockpit"))
            {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                getEnergyPastArmor(0.05F, shot);
            }
            if(s.startsWith("xcf"))
                hitChunk("CF", shot);
            else
            if(s.startsWith("xnose"))
            {
                if(chunkDamageVisible("Nose") < 2)
                    hitChunk("Nose", shot);
                if(chunkDamageVisible("Tail2") < 2)
                    hitChunk("Tail2", shot);
            } else
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
            if(s.startsWith("xvator"))
            {
                if(s.startsWith("xvatorl"))
                    hitChunk("VatorL", shot);
                if(s.startsWith("xvatorr"))
                    hitChunk("VatorR", shot);
            } else
            if(s.startsWith("xwing"))
            {
                if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 2)
                    hitChunk("WingLIn", shot);
                if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 2)
                    hitChunk("WingRIn", shot);
                if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                {
                    hitChunk("WingLMid", shot);
                    hitChunk("Flap1", shot);
                    hitChunk("Flap12", shot);
                }
                if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                {
                    hitChunk("WingRMid", shot);
                    hitChunk("Flap2", shot);
                    hitChunk("Flap22", shot);
                }
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
                    this.FM.AS.setInternalDamage(shot.initiator, 0);
                }
                if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
                {
                    debuggunnery("Undercarriage: Stuck..");
                    this.FM.AS.setInternalDamage(shot.initiator, 3);
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
label0:
        switch(i)
        {
        default:
            break;

        case 13: // '\r'
            this.FM.Gears.cgear = false;
            float f = World.Rnd().nextFloat();
            for(int k = 0; k < 1; k++)
            {
                if(f < 0.1F)
                {
                    this.FM.AS.hitEngine(this, k, 100);
                    if(World.Rnd().nextFloat() < 0.49F)
                        this.FM.EI.engines[k].setEngineDies(actor);
                    break label0;
                }
                if(f > 0.55F)
                    this.FM.EI.engines[k].setEngineDies(actor);
            }

            break;

        case 34: // '"'
            this.FM.Gears.lgear = false;
            break;

        case 37: // '%'
            this.FM.Gears.rgear = false;
            break;

        case 19: // '\023'
            for(int l = 0; l < 1; l++)
            {
                this.FM.CT.bHasAirBrakeControl = false;
                this.FM.EI.engines[l].setEngineDies(actor);
            }

            break;

        case 11: // '\013'
            this.FM.CT.bHasElevatorControl = false;
            this.FM.CT.bHasRudderControl = false;
            this.FM.CT.bHasRudderTrim = false;
            this.FM.CT.bHasElevatorTrim = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void typeFighterAceMakerRangeFinder()
    {
        if(k14Mode != 1)
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
        {
            k14Distance = 200F;
            hunted = War.GetNearestEnemyAircraft(this.FM.actor, 2700F, 9);
        }
        if(hunted != null)
        {
            k14Distance = (float)this.FM.actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
            if(k14Distance > 800F)
                k14Distance = 800F;
            else
            if(k14Distance < 200F)
                k14Distance = 200F;
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
        for(i = 0; i < TypeSupersonic.fMachAltX.length; i++)
            if(TypeSupersonic.fMachAltX[i] > f)
                break;

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
        return this.FM.getSpeedKMH() / getMachForAlt(this.FM.getAltitude());
    }

    public void soundbarier()
    {
        float f = getMachForAlt(this.FM.getAltitude()) - this.FM.getSpeedKMH();
        if(f < 0.5F)
            f = 0.5F;
        float f1 = this.FM.getSpeedKMH() - getMachForAlt(this.FM.getAltitude());
        if(f1 < 0.5F)
            f1 = 0.5F;
        if(calculateMach() <= 1.0F)
        {
            this.FM.VmaxAllowed = this.FM.getSpeedKMH() + f;
            SonicBoom = 0.0F;
            isSonic = false;
        }
        if(calculateMach() >= 1.0F)
        {
            this.FM.VmaxAllowed = this.FM.getSpeedKMH() + f1;
            isSonic = true;
        }
        if(this.FM.VmaxAllowed > 1500F)
            this.FM.VmaxAllowed = 1500F;
        if(isSonic && SonicBoom < 1.0F)
        {
            super.playSound("aircraft.SonicBoom", true);
            super.playSound("aircraft.SonicBoomInternal", true);
            if(this.FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
            if(Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(this.FM.getAltitude()))
                shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            SonicBoom = 1.0F;
        }
        if(calculateMach() > 1.01F || calculateMach() < 1.0F)
            Eff3DActor.finish(shockwave);
    }

    public void engineSurge(float f)
    {
        if(this.FM.AS.isMaster())
        {
            for(int i = 0; i < 1; i++)
                if(curthrl == -1F)
                {
                    curthrl = oldthrl = this.FM.EI.engines[i].getControlThrottle();
                } else
                {
                    curthrl = this.FM.EI.engines[i].getControlThrottle();
                    if(curthrl < 1.05F)
                    {
                        if((curthrl - oldthrl) / f > 20F && this.FM.EI.engines[i].getRPM() < 3200F && this.FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                        {
                            if(this.FM.actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.01D * (double)(this.FM.EI.engines[i].getRPM() / 1000F);
                            this.FM.EI.engines[i].doSetReadyness(this.FM.EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.05F && (this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode())
                                this.FM.AS.hitEngine(this, i, 100);
                            if(World.Rnd().nextFloat() < 0.05F && (this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode())
                                this.FM.EI.engines[i].setEngineDies(this);
                        }
                        if((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && this.FM.EI.engines[i].getRPM() < 3200F && this.FM.EI.engines[i].getStage() == 6)
                        {
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.001D * (double)(this.FM.EI.engines[i].getRPM() / 1000F);
                            this.FM.EI.engines[i].doSetReadyness(this.FM.EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.4F && (this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode())
                            {
                                if(this.FM.actor == World.getPlayerAircraft())
                                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                                this.FM.EI.engines[i].setEngineStops(this);
                            } else
                            if(this.FM.actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                        }
                    }
                    oldthrl = curthrl;
                }

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

    public void update(float f)
    {
        if(this.FM.CT.getFlap() < this.FM.CT.FlapsControl)
            this.FM.CT.forceFlaps(flapsMovement(f, this.FM.CT.FlapsControl, this.FM.CT.getFlap(), 999F, Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.08F)));
        else
            this.FM.CT.forceFlaps(flapsMovement(f, this.FM.CT.FlapsControl, this.FM.CT.getFlap(), 999F, Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.7F)));
        if((this.FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && this.FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            this.FM.AS.bIsAboutToBailout = false;
            bailout();
        }
        float f2 = this.FM.getSpeedKMH() - 1000F;
        if(f2 < 0.0F)
            f2 = 0.0F;
        this.FM.CT.dvGear = 0.2F - f2 / 1000F;
        if(this.FM.CT.dvGear < 0.0F)
            this.FM.CT.dvGear = 0.0F;
        if((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && (this.FM instanceof Maneuver))
        {
            if(this.FM.AP.way.isLanding() && this.FM.Gears.onGround() && this.FM.getSpeed() > 40F)
            {
                this.FM.CT.AirBrakeControl = 1.0F;
                if(this.FM.CT.bHasDragChuteControl)
                    this.FM.CT.DragChuteControl = 1.0F;
            }
            if(this.FM.AP.way.isLanding() && this.FM.Gears.onGround() && this.FM.getSpeed() < 40F)
            {
                this.FM.CT.AirBrakeControl = 0.0F;
                if(this.FM.getSpeed() < 20F)
                    this.FM.CT.DragChuteControl = 0.0F;
            }
        }
        if(this.FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            if(this.FM.AS.isMaster() && Config.isUSE_RENDER())
            {
                if(this.FM.EI.engines[0].getPowerOutput() > 1.001F && this.FM.EI.engines[0].getStage() == 6)
                {
                    if(World.getTimeofDay() >= 18F || World.getTimeofDay() <= 6F)
                        this.FM.AS.setSootState(this, 0, 5);
                    else
                        this.FM.AS.setSootState(this, 0, 3);
                } else
                {
                    this.FM.AS.setSootState(this, 0, 0);
                }
                setExhaustFlame(Math.round(Aircraft.cvt(this.FM.EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
            }
            if(this.FM instanceof RealFlightModel)
                umn();
        }
        if(FLIR)
            laser(TypeLaserSpotter.spot);
        updatecontrollaser();
        engineSurge(f);
        typeFighterAceMakerRangeFinder();
        checkHydraulicStatus();
        moveHydraulics(f);
        soundbarier();
        groundcrew();
        super.update(f);
        computeflightmodel();
        moveSlat();
        restoreElevatorControl();
        if(this.FM.getSpeedKMH() > 300F)
            this.FM.CT.cockpitDoorControl = 0.0F;
        for(int i = 1; i < 33; i++)
        {
            fNozzleOpenL = this.FM.EI.engines[0].getPowerOutput() > 0.92F ? Aircraft.cvt(this.FM.EI.engines[0].getPowerOutput(), 0.92F, 1.1F, 0.0F, -9F) : Aircraft.cvt(this.FM.EI.engines[0].getPowerOutput(), 0.0F, 0.92F, -9F, 0.0F);
            hierMesh().chunkSetAngles("Eflap" + i, fNozzleOpenL, 0.0F, 0.0F);
        }

        float m = Aircraft.cvt(this.FM.getSpeedKMH(), 500F, 2000F, 0.999F, 0.301F);
        if(this.FM.getSpeed() > 7F && World.Rnd().nextFloat() < getAirDensityFactor(this.FM.getAltitude()))
        {
            if(this.FM.getOverload() > 5.7F)
            {
                pull01 = Eff3DActor.New(this, findHook("_Pull01"), null, m, "3DO/Effects/Aircraft/PullingvaporF16.eff", -1F);
                pull02 = Eff3DActor.New(this, findHook("_Pull02"), null, m, "3DO/Effects/Aircraft/PullingvaporF16.eff", -1F);
            }
            if(this.FM.getOverload() <= 5.7F)
            {
                Eff3DActor.finish(pull01);
                Eff3DActor.finish(pull02);
            }
        }
    }

    private void groundcrew()
    {
        if(this.FM.getSpeed() < 5F && this.FM.Gears.onGround() && this.FM.CT.cockpitDoorControl > 0.5F)
            hierMesh().chunkVisible("crew", true);
        else
            hierMesh().chunkVisible("crew", false);
    }

    public void setExhaustFlame(int i, int j)
    {
        if(j == 0)
            switch(i)
            {
            case 0: // '\0'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 1: // '\001'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 2: // '\002'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 3: // '\003'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                // fall through

            case 4: // '\004'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 5: // '\005'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 6: // '\006'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 7: // '\007'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 8: // '\b'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 9: // '\t'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;

            case 10: // '\n'
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;

            case 11: // '\013'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;

            case 12: // '\f'
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;

            default:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;
            }
    }

    private void computeflightmodel()
    {
        float fl = (float)((double)(Aircraft.cvt(this.FM.getAOA(), 0.0F, 5F, 0.0F, 1.0F) * this.FM.getAOA()) * 0.025000000000000001D * (double)Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 500F, 0.0F, 1.0F));
        if(this.FM.getSpeedKMH() > 465F || this.FM.CT.FlapsControlSwitch == 0)
        {
            if(this.FM.CT.FlapsControlSwitch > 0)
                bForceFlapmodeAuto = true;
            else
                bForceFlapmodeAuto = false;
            if(Time.current() > tflap + 3000L && Time.current() < tflap + 4000L)
            {
                this.FM.CT.FlapsControlSwitch = 0;
                this.FM.CT.setTrimElevatorControl(0.0F);
            } else
            if(Time.current() > tflap + 4000L)
                this.FM.CT.FlapsControl = Aircraft.cvt(fl, 0.0F, 0.2F, 0.0F, 0.2F);
        } else
        {
            bForceFlapmodeAuto = false;
            float newFlapsControl = Aircraft.cvt(this.FM.getSpeedKMH(), 330F, 465F, 1.0F, 0.0F);
            if(this.FM.CT.FlapsControlSwitch == 1 && newFlapsControl > this.FM.CT.FlapStage[0])
                newFlapsControl = this.FM.CT.FlapStage[0];
            this.FM.CT.FlapsControl = newFlapsControl;
            tflap = Time.current();
            if((double)this.FM.CT.FlapsControl > 0.59999999999999998D && this.FM.Gears.onGround())
                this.FM.CT.setTrimElevatorControl(0.7F);
        }
        if(this.FM.getAOA() > 28F || this.FM.getSpeedKMH() < 469F && this.FM.CT.FlapsControl > 0.16F && this.FM.CT.getGear() < 0.8F || this.FM.getOverload() >= 6F)
            this.FM.CT.AirBrakeControl = 0.0F;
        computeThrust();
    }

    private void computeThrust()
    {
        for(int engineNumber = 0; engineNumber < this.FM.EI.getNum(); engineNumber++)
        {
            double dAFOffset = 0.0D;
            if(this.FM.EI.engines[engineNumber].getThrustOutput() > 1.001F && this.FM.EI.engines[engineNumber].getStage() > 5)
            {
                float fAlt = this.FM.getAltitude() / 100F;
                dAFOffset = ((-5.6371922939999996E-007D * Math.pow(fAlt, 4D) + 0.0001189116D * Math.pow(fAlt, 3D)) - 0.0046231645660000003D * Math.pow(fAlt, 2D) - 0.53986529949999995D * (double)fAlt) + 196.01121040000001D;
                if(dAFOffset < 0.0D)
                    dAFOffset = 0.0D;
                dAFOffset *= this.FM.EI.engines[engineNumber].getRPM() / 10F;
                this.FM.producedAF.x += dAFOffset;
            }
            float fKEASMachLimit = 1.02F + 0.0001F * this.FM.getAltitude();
            if(fKEASMachLimit > 2.0F)
                fKEASMachLimit = 2.0F;
            try
            {
                float thrustMaxFactor = 1.0F;
                if(calculateMach() > fKEASMachLimit)
                    thrustMaxFactor = Math.max(0.0F, 1.0F - (calculateMach() - fKEASMachLimit) * 7F);
                thrustMaxField[engineNumber].setFloat(this.FM.EI.engines[engineNumber], thrustMaxFromEmd[engineNumber] * thrustMaxFactor);
            }
            catch(IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch(IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

    }

    private void getThrustMaxFromEmd()
    {
        for(int engineNumber = 0; engineNumber < this.FM.EI.getNum(); engineNumber++)
        {
            thrustMaxField[engineNumber] = Reflection.getField(this.FM.EI.engines[engineNumber], "thrustMax");
            try
            {
                thrustMaxFromEmd[engineNumber] = thrustMaxField[engineNumber].getFloat(this.FM.EI.engines[engineNumber]);
            }
            catch(IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch(IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

    }

    private float clamp11(float f)
    {
        if(f < -1F)
            f = -1F;
        else
        if(f > 1.0F)
            f = 1.0F;
        return f;
    }

    public void restoreElevatorControl()
    {
        this.FM.CT.bHasElevatorControl = true;
    }

    public void netUpdateWayPoint()
    {
        super.netUpdateWayPoint();
        if(!(this.FM instanceof RealFlightModel))
        {
            return;
        } else
        {
            computeElevators();
            return;
        }
    }

    public void computeElevators()
    {
        if(this.FM.CT.StabilizerControl)
            return;
        if(this.FM.Gears.onGround() && this.FM.CT.FlapsControl > 0.68F)
            return;
        if(this.FM.getSpeedKMH() < 30F)
            return;
        float elevatorControl = this.FM.CT.getElevator();
        float targetGForce = 0.0F;
        if(this.FM.CT.ElevatorControl > 0.0F)
            targetGForce = (this.FM.getLimitLoad() * 0.9F - 1.0F) * this.FM.CT.ElevatorControl + 1.0F;
        else
            targetGForce = 1.0F - (this.FM.Negative_G_Limit * 0.9F - 1.0F) * this.FM.CT.ElevatorControl;
        float gForceDiff = this.FM.getOverload() - targetGForce;
        elevatorControl -= gForceDiff / Math.max(this.FM.getSpeedKMH() / 1.6F, 200F);
        elevatorControl = clamp11(elevatorControl);
        this.FM.CT.bHasElevatorControl = false;
        if(elevatorsField == null)
        {
            elevatorsField = Reflection.getField(this.FM.CT, "Elevators");
            elevatorsField.setAccessible(true);
        }
        try
        {
            elevatorsField.setFloat(this.FM.CT, elevatorControl);
        }
        catch(IllegalAccessException illegalaccessexception) { }
    }

    private void moveSlat()
    {
        if(this.FM.Gears.onGround())
        {
            hierMesh().chunkSetAngles("Slat01_D0", 0.0F, 0.0F, -14.5F);
            hierMesh().chunkSetAngles("Slat02_D0", 0.0F, 0.0F, -14.5F);
        } else
        if(this.FM.getSpeed() > 5F && !this.FM.Gears.onGround())
        {
            hierMesh().chunkSetAngles("Slat01_D0", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -14.5F));
            hierMesh().chunkSetAngles("Slat02_D0", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -14.5F));
        }
        if(this.FM.getSpeed() > 10F && !this.FM.Gears.onGround())
        {
            hierMesh().chunkSetAngles("Slat01_D0", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -20.5F));
            hierMesh().chunkSetAngles("Slat02_D0", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -20.5F));
        }
        if(this.FM.getSpeed() > 20F && !this.FM.Gears.onGround())
        {
            hierMesh().chunkSetAngles("Slat01_D0", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -30.5F));
            hierMesh().chunkSetAngles("Slat02_D0", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -30.5F));
        }
    }

    public void moveRefuel(float f1)
    {
    }

    public void doSetSootState(int i, int j)
    {
        for(int k = 0; k < 2; k++)
        {
            if(this.FM.AS.astateSootEffects[i][k] != null)
                Eff3DActor.finish(this.FM.AS.astateSootEffects[i][k]);
            this.FM.AS.astateSootEffects[i][k] = null;
        }

        switch(j)
        {
        case 1: // '\001'
            this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 3: // '\003'
            this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.5F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 4F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            break;

        case 2: // '\002'
            this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.8F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.5F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;

        case 5: // '\005'
            this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurnerF18.eff", -1F);
            this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurnerF18A.eff", -1F);
            break;

        case 4: // '\004'
            this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;
        }
    }

    private void bailout()
    {
        if(overrideBailout)
            if(this.FM.AS.astateBailoutStep >= 0 && this.FM.AS.astateBailoutStep < 2)
            {
                if(this.FM.CT.cockpitDoorControl > 0.5F && this.FM.CT.getCockpitDoor() > 0.5F)
                {
                    this.FM.AS.astateBailoutStep = 11;
                    doRemoveBlisters();
                } else
                {
                    this.FM.AS.astateBailoutStep = 2;
                }
            } else
            if(this.FM.AS.astateBailoutStep >= 2 && this.FM.AS.astateBailoutStep <= 3)
            {
                switch(this.FM.AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(this.FM.CT.cockpitDoorControl < 0.5F)
                        doRemoveBlister1();
                    break;

                case 3: // '\003'
                    doRemoveBlisters();
                    break;
                }
                if(this.FM.AS.isMaster())
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                AircraftState as1 = this.FM.AS;
                as1.astateBailoutStep = (byte)(as1.astateBailoutStep + 1);
                if(this.FM.AS.astateBailoutStep == 4)
                    this.FM.AS.astateBailoutStep = 11;
            } else
            if(this.FM.AS.astateBailoutStep >= 11 && this.FM.AS.astateBailoutStep <= 19)
            {
                int i = this.FM.AS.astateBailoutStep;
                if(this.FM.AS.isMaster())
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                AircraftState as2 = this.FM.AS;
                as2.astateBailoutStep = (byte)(as2.astateBailoutStep + 1);
                if(i == 11)
                {
                    this.FM.setTakenMortalDamage(true, null);
                    if((this.FM instanceof Maneuver) && ((Maneuver)this.FM).get_maneuver() != 44)
                    {
                        World.cur();
                        if(this.FM.AS.actor != World.getPlayerAircraft())
                            ((Maneuver)this.FM).set_maneuver(44);
                    }
                }
                if(this.FM.AS.astatePilotStates[i - 11] < 99)
                {
                    doRemoveBodyFromPlane(i - 10);
                    if(i == 11)
                    {
                        doEjectCatapult();
                        this.FM.setTakenMortalDamage(true, null);
                        this.FM.CT.WeaponControl[0] = false;
                        this.FM.CT.WeaponControl[1] = false;
                        this.FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        this.FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                        if(i > 10 && i <= 19)
                            EventLog.onBailedOut(this, i - 11);
                    }
                }
            }
    }

    private final void doRemoveBlister1()
    {
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1 && this.FM.AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    protected final void doRemoveBlisters()
    {
        for(int i = 2; i < 10; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && this.FM.AS.getPilotHealth(i - 1) > 0.0F)
            {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(this.FM.Vwld);
                wreckage.setSpeed(vector3d);
            }

    }

    private final void umn()
    {
        Vector3d vector3d = this.FM.getVflow();
        mn = (float)vector3d.lengthSquared();
        mn = (float)Math.sqrt(mn);
        F_16 Yak_36 = this;
        float f = mn;
        World.cur().getClass();
        Yak_36.mn = f / Atmosphere.sonicSpeed((float)this.FM.Loc.z);
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
    }

    public void typeRadarGainPlus()
    {
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

    public boolean typeBomberToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        if(k14Mode == 0)
        {
            if(this.FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Bomb");
        } else
        if(k14Mode == 1)
        {
            if(this.FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Gunnery");
        } else
        if(k14Mode == 2 && this.FM.actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Navigation");
        return true;
    }

    public boolean typeGuidedBombCisMasterAlive()
    {
        return isMasterAlive;
    }

    public void typeGuidedBombCsetMasterAlive(boolean flag)
    {
        isMasterAlive = flag;
    }

    public boolean typeGuidedBombCgetIsGuiding()
    {
        return isGuidingBomb;
    }

    public void typeGuidedBombCsetIsGuiding(boolean flag)
    {
        isGuidingBomb = flag;
    }

    public void typeX4CAdjSidePlus()
    {
        deltaAzimuth = 0.002F;
    }

    public void typeX4CAdjSideMinus()
    {
        deltaAzimuth = -0.002F;
    }

    public void typeX4CAdjAttitudePlus()
    {
        deltaTangage = 0.002F;
    }

    public void typeX4CAdjAttitudeMinus()
    {
        deltaTangage = -0.002F;
    }

    public void typeX4CResetControls()
    {
        deltaAzimuth = deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth()
    {
        return deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage()
    {
        return deltaTangage;
    }

    public float Fuelamount;
    public int nozzlemode;
    public long tvect;
    private boolean bDynamoOperational;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int pk;
    public int radarrange;
    private long twait;
    protected boolean bSlatsOff;
    private float oldctl;
    private float curctl;
    private float oldthrl;
    private float curthrl;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    public float AirBrakeControl;
    public float DragChuteControl;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private float lightTime;
    private float ft;
    private LightPointWorld lLight[];
    private Hook lLightHook[];
    private static Loc lLightLoc1 = new Loc();
    private static Point3d lLightP1 = new Point3d();
    private static Point3d lLightP2 = new Point3d();
    private static Point3d lLightPL = new Point3d();
    private boolean ictl;
    private static float mteb = 1.0F;
    private float mn;
    private static float uteb = 1.25F;
    private static float lteb = 0.92F;
    private float actl;
    private float rctl;
    private float ectl;
    private boolean ts;
    private float H1;
    public static boolean bChangedPit = false;
    private float SonicBoom;
    private Eff3DActor shockwave;
    private boolean isSonic;
    public static int LockState = 0;
    static Actor hunted = null;
    private float engineSurgeDamage;
    private float gearTargetAngle;
    private float gearCurrentAngle;
    public boolean hasHydraulicPressure;
    private static final float NEG_G_TOLERANCE_FACTOR = 2.5F;
    private static final float NEG_G_TIME_FACTOR = 2.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 2.0F;
    private static final float POS_G_TOLERANCE_FACTOR = 9.5F;
    private static final float POS_G_TIME_FACTOR = 3F;
    private static final float POS_G_RECOVERY_FACTOR = 3F;
    private Eff3DActor pull1;
    private Eff3DActor pull2;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    public boolean FLIR;
    private static Loc l = new Loc();
    public int clipBoardPage_;
    public boolean showClipBoard_;
    private ArrayList missilesList;
    private float deltaAzimuth;
    private float deltaTangage;
    private boolean isGuidingBomb;
    private boolean isMasterAlive;
    private Eff3DActor pull01;
    private Eff3DActor pull02;
    public int lockmode;
    private boolean APmode1;
    public boolean ILS;
    public boolean FL;
    public float azimult;
    public float tangate;
    public long tf;
    public float v;
    public float h;
    public boolean hold;
    private long t1;
    public boolean radartogle;
    public int radarmode;
    public int targetnum;
    public float lockrange;
    public int radargunsight;
    public int leftscreen;
    public int Bingofuel;
    public boolean Nvision;
    public float misslebrg;
    public float aircraftbrg;
    public boolean backfire;
    public boolean bRadarWarning;
    public boolean bMissileWarning;
    private SoundFX fxRWR;
    private SoundFX fxMissileWarning;
    private Field elevatorsField;
    private long tflap;
    public boolean bForceFlapmodeAuto;
    public float fNozzleOpenL;
    private float thrustMaxFromEmd[];
    private Field thrustMaxField[];

    static 
    {
        Class class1 = com.maddox.il2.objects.air.F_16.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
