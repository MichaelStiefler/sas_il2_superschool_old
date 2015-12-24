
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.air.A_6A;
import com.maddox.il2.objects.air.A_6E;
import com.maddox.il2.objects.air.KA_6D;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.Missile;
import com.maddox.il2.objects.weapons.MissileSAM;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapExt;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;


public class A_6 extends Scheme2
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

    public A_6()
    {
        elevatorsField = null;
        tflap = 0L;
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
        hold = false;
        t1 = 0L;
        tangate = 0.0F;
        azimult = 0.0F;
        tf = 0L;
        APmode1 = false;
        APmode2 = false;
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
        Flaps = false;
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
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log("Autopilot Mode: Altitude ON");
                ((FlightModelMain) (super.FM)).AP.setStabAltitude(1000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log("Autopilot Mode: Altitude OFF");
                ((FlightModelMain) (super.FM)).AP.setStabAltitude(false);
            }
        if(i == 21)
            if(!APmode2)
            {
                APmode2 = true;
                HUD.log("Autopilot Mode: Direction ON");
                ((FlightModelMain) (super.FM)).AP.setStabDirection(true);
                ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
            } else
            if(APmode2)
            {
                APmode2 = false;
                HUD.log("Autopilot Mode: Direction OFF");
                ((FlightModelMain) (super.FM)).AP.setStabDirection(false);
                ((FlightModelMain) (super.FM)).CT.bHasRudderControl = true;
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
        boolean flag = false;
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = War.getNearestEnemy(this, 6000F);
        if(aircraft != null)
        {
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - (double)Landscape.Hmin((float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i += 360;
            int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j += 360;
            Aircraft aircraft1 = War.getNearestEnemy(aircraft, 6000F);
            boolean flag1;
            if((aircraft1 instanceof Aircraft) && aircraft.getArmy() != World.getPlayerArmy() && (aircraft instanceof TypeFighterAceMaker) && ((aircraft instanceof TypeSupersonic) || (aircraft instanceof TypeFastJet)) && aircraft1 == World.getPlayerAircraft() && aircraft1.getSpeed(vector3d) > 20D)
            {
                super.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + ((Actor) (aircraft1)).pos.getAbsPoint().x;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft1)).pos.getAbsPoint().y;
                double d8 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft1)).pos.getAbsPoint().z;
                new String();
                new String();
                int k = (int)(Math.floor(((Actor) (aircraft1)).pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
                int i1 = (int)(Math.floor((aircraft1.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                double d10 = (int)(Math.ceil((d2 - d8) / 10D) * 10D);
                boolean flag2 = false;
                Engine.land();
                int k1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(((Actor) (aircraft1)).pos.getAbsPoint().x), Engine.land().WORLD2PIXY(((Actor) (aircraft1)).pos.getAbsPoint().y));
                float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                if(k1 >= 28 && k1 < 32 && f < 7.5F)
                    flag2 = true;
                new String();
                double d14 = d4 - d;
                double d16 = d6 - d1;
                float f1 = 57.32484F * (float)Math.atan2(d16, -d14);
                int l1 = (int)(Math.floor((int)f1) - 90D);
                if(l1 < 0)
                    l1 += 360;
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
                j1 += 360;
            if(flag1 && aircraft1 == World.getPlayerAircraft() && (aircraft1 instanceof F_18))
            {
                super.pos.getAbs(point3d);
                double d11 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                double d12 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
                double d13 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                double d15 = (int)(Math.ceil((d9 - d13) / 10D) * 10D);
                String s = "";
                if(d9 - d13 - 500D >= 0.0D)
                    s = " low";
                if((d9 - d13) + 500D < 0.0D)
                    s = " high";
                new String();
                double d17 = d11 - d5;
                double d18 = d12 - d7;
                float f2 = 57.32484F * (float)Math.atan2(d18, -d17);
                int j2 = (int)(Math.floor((int)f2) - 90D);
                if(j2 < 0)
                    j2 += 360;
                int k2 = j2 - j1;
                if(k2 < 0)
                    k2 += 360;
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
                    aircraftbrg = Aircraft.cvt(l2, 0.0F, 12F, 0.0F, 360F);
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
        super.pos.getAbs(point3d);
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
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Actor)obj).pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Actor)obj).pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Actor)obj).pos.getAbsPoint().z;
        int i = (int)((double)(-((Aircraft)obj1).pos.getAbsOrient().getYaw()) - 90D);
        if(i < 0)
            i += 360;
        List list = Engine.missiles();
        int j = list.size();
        for(int k = 0; k < j; k++)
        {
            Actor actor = (Actor)list.get(k);
            if(((actor instanceof Missile) || (actor instanceof MissileSAM)) && actor.getSpeed(vector3d) > 20D)
            {
                super.pos.getAbs(point3d);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (actor.pos.getAbsPoint())).x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).y;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).z;
                double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                String s = "";
                if(d2 - d5 - 500D >= 0.0D)
                    s = " LOW";
                if((d2 - d5) + 500D < 0.0D)
                    s = " HIGH";
                new String();
                double d7 = d3 - d;
                double d8 = d4 - d1;
                float f = 57.32484F * (float)Math.atan2(d8, -d7);
                int i1 = (int)(Math.floor((int)f) - 90D);
                if(i1 < 0)
                    i1 += 360;
                int j1 = i1 - i;
                if(j1 < 0)
                    j1 += 360;
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
                    misslebrg = Aircraft.cvt(k1, 0.0F, 12F, 0.0F, 360F);
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
                FM.AP.setStabAltitude(2000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Released");
                FM.AP.setStabAltitude(false);
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
        gfactors.setGFactors(2.5F, 2.5F, 2.0F, 9.5F, 3F, 3F);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.AS.wantBeaconsNet(true);
        t1 = Time.current();
        FM.CT.toggleRocketHook();
        FM.CT.bHasSideDoor = false;
        if(this instanceof com.maddox.il2.objects.air.A_6A || this instanceof com.maddox.il2.objects.air.KA_6D)
            bGen1st = true;
        else
            bGen1st = false;
        if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))
        {
            FM.Sq.squareElevators += 2.0F;
            FM.Sq.liftStab += 2.0F;
        }
    }

    public void checkHydraulicStatus()
    {
        if(FM.EI.engines[0].getStage() < 6 && FM.Gears.nOfGearsOnGr > 0)
        {
            hasHydraulicPressure = false;
            FM.CT.bHasAileronControl = false;
            FM.CT.bHasElevatorControl = false;
            FM.CT.AirBrakeControl = 0.0F;
        } else
        if(!hasHydraulicPressure)
        {
            hasHydraulicPressure = true;
            FM.CT.bHasAileronControl = true;
            FM.CT.bHasElevatorControl = true;
            FM.CT.bHasAirBrakeControl = true;
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
                if(FM.AS.astateLandingLightEffects[j] != null)
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
        boolean bSideDoorOccupy = false;
        if(FM.CT.bHasSideDoor && FM.CT.bMoveSideDoor)
        {
            if(FM.CT.getCockpitDoor() > 0.0F && FM.CT.getCockpitDoor() < 1.0F)
                bSideDoorOccupy = true;
        }
        if(!bSideDoorOccupy)
            FM.CT.setActiveDoor( 1 );     // setActiveDoor <== not SIDE_DOOR
        if (FM.Gears.onGround() && !bSideDoorOccupy)
        {
            if(FM.CT.getCockpitDoor() > 0.95F)
            {
                hierMesh().chunkVisible("HMask1_D0", false);
                hierMesh().chunkVisible("HMask2_D0", false);
            }
            else
            {
                hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
                hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Pilot2_D0"));
            }

            if(FM.brakeShoe && FM.CT.getCockpitDoor() > 0.4F)
            {
                hierMesh().chunkSetAngles("StairsL", 0.0F, -180.0F, 0.0F);
                hierMesh().chunkSetAngles("StairsR", 0.0F, 180.0F, 0.0F);
            }
            else
            {
                hierMesh().chunkSetAngles("StairsL", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("StairsR", 0.0F, 0.0F, 0.0F);
            }
        }
        if(FM.Gears.onGround() && FM.brakeShoe)
        {
            FM.CT.bHasSideDoor = true;
        }
        else
        {
            if(FM.CT.bHasSideDoor)
            {
                FM.CT.setActiveDoor( 2 );     // setActiveDoor <== SIDE_DOOR
                FM.CT.cockpitDoorControl = 0.0F;
                FM.CT.bHasSideDoor = false;
            }
        }
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            Vector3d vector3d = FM.getVflow();
            mn = (float)vector3d.lengthSquared();
            mn = (float)Math.sqrt(mn);
            A_6 a_6 = this;
            float f1 = mn;
            World.cur().getClass();
            a_6.mn = f1 / Atmosphere.sonicSpeed((float)((Tuple3d) (FM.Loc)).z);
            if(mn >= 0.9F && (double)mn < 1.1000000000000001D)
                ts = true;
            else
                ts = false;
            ft = World.getTimeofDay() % 0.01F;
            if(ft == 0.0F)
                UpdateLightIntensity();
        }
        if(FLIR)
            FLIR();
        if(!FLIR)
            FM.AP.setStabAltitude(false);
        if ((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) && (FM instanceof Maneuver))
            if (FM.AP.way.isLanding() && FM.getSpeed() > FM.VmaxFLAPS && FM.getSpeed() > FM.AP.way.curr().getV() * 1.4F)
            {
                if (this.FM.CT.AirBrakeControl != 1.0F)
                    this.FM.CT.AirBrakeControl = 1.0F;
            }
            else if (((Maneuver) FM).get_maneuver() == 25 && FM.AP.way.isLanding() && FM.getSpeed() < FM.VmaxFLAPS * 1.16F)
            {
                if (FM.getSpeed() > FM.VminFLAPS * 0.5F && FM.Gears.onGround())
                {
                    if (FM.CT.AirBrakeControl != 1.0F)
                        FM.CT.AirBrakeControl = 1.0F;
                }
                else if (FM.CT.AirBrakeControl != 0.0F)
                    FM.CT.AirBrakeControl = 0.0F;
            }
            else if (((Maneuver) FM).get_maneuver() == 66)
            {
                if (FM.CT.AirBrakeControl != 0.0F)
                    FM.CT.AirBrakeControl = 0.0F;
            }
            else if (((Maneuver) FM).get_maneuver() == 7)
            {
                if (FM.CT.AirBrakeControl != 1.0F)
                    FM.CT.AirBrakeControl = 1.0F;
            }
            else if (hasHydraulicPressure && FM.CT.AirBrakeControl != 0.0F)
                FM.CT.AirBrakeControl = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 0.91F || FM.EI.engines[1].getThrustOutput() > 0.91F)
            FM.CT.AirBrakeControl = 0.0F;
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
        FM.setTakenMortalDamage(true, null);
        FM.CT.WeaponControl[0] = false;
        FM.CT.WeaponControl[1] = false;
        FM.CT.bHasAileronControl = false;
        FM.CT.bHasRudderControl = false;
        FM.CT.bHasElevatorControl = false;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
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
                    getEnergyPastArmor(8.77F, shot);
                else
                if(s.endsWith("g1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(40F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 2);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                }
            } else
            if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch(i)
                {
                case 1: // '\001'
                case 2: // '\002'
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(1.1F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3: // '\003'
                case 4: // '\004'
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
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
                        ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 24000F)
                {
                    ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                s.endsWith("exht");
            }
            if(s.startsWith("xxtank"))
            {
                int j = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(((FlightModelMain) (super.FM)).AS.astateTankStates[j] == 0)
                    {
                        debuggunnery("Fuel Tank (" + j + "): Pierced..");
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, j, 1);
                        ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, j, 2);
                        debuggunnery("Fuel Tank (" + j + "): Hit..");
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
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
            else
            if(s.startsWith("xxpnm"))
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 1);
        } else
        {
            if(s.startsWith("xcockpit"))
            {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
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
                    ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 0);
                }
                if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
                {
                    debuggunnery("Undercarriage: Stuck..");
                    ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
                }
            } else
            if(s.startsWith("xpilot") || s.startsWith("xhead"))
            {
                byte byte0 = 0;
                int k;
                if(s.endsWith("a"))
                {
                    byte0 = 1;
                    k = s.charAt(6) - 49;
                } else
                if(s.endsWith("b"))
                {
                    byte0 = 2;
                    k = s.charAt(6) - 49;
                } else
                {
                    k = s.charAt(5) - 49;
                }
                hitFlesh(k, shot, byte0);
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19: // '\023'
            FM.EI.engines[0].setEngineDies(actor);
            return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    private void bailout()
    {
        if(overrideBailout)
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 0 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep < 2)
            {
                if(((FlightModelMain) (super.FM)).CT.cockpitDoorControl > 0.5F && ((FlightModelMain) (super.FM)).CT.getCockpitDoor() > 0.5F)
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
                else
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 2;
            } else
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 2 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 3)
            {
                switch(((FlightModelMain) (super.FM)).AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(((FlightModelMain) (super.FM)).CT.cockpitDoorControl < 0.5F)
                        doRemoveBlisters();
                    break;

                case 3: // '\003'
                    lTimeNextEject = Time.current() + 1000L;
                    break;
                }
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(20, ((FlightModelMain) (super.FM)).AS.astateBailoutStep, 1, null);
                ((FlightModelMain) (super.FM)).AS.astateBailoutStep = (byte)(((FlightModelMain) (super.FM)).AS.astateBailoutStep + 1);
                if(((FlightModelMain) (super.FM)).AS.astateBailoutStep == 4)
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
            } else
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 11 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 19)
            {
                byte byte0 = ((FlightModelMain) (super.FM)).AS.astateBailoutStep;
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(20, ((FlightModelMain) (super.FM)).AS.astateBailoutStep, 1, null);
                ((FlightModelMain) (super.FM)).AS.astateBailoutStep = (byte)(((FlightModelMain) (super.FM)).AS.astateBailoutStep + 1);
                if((super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_maneuver() != 44)
                {
                    World.cur();
                    if(((FlightModelMain) (super.FM)).AS.actor != World.getPlayerAircraft())
                        ((Maneuver)super.FM).set_maneuver(44);
                }
                if(((FlightModelMain) (super.FM)).AS.astatePilotStates[byte0 - 11] < 99)
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
                        ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 51;
                        super.FM.setTakenMortalDamage(true, null);
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
                        ((FlightModelMain) (super.FM)).AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        ((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                    ((FlightModelMain) (super.FM)).AS.astatePilotStates[byte0 - 11] = 99;
                } else
                {
                    EventLog.type("astatePilotStates[" + (byte0 - 11) + "]=" + ((FlightModelMain) (super.FM)).AS.astatePilotStates[byte0 - 11]);
                }
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
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
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
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
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
        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
        ((FlightModelMain) (super.FM)).CT.bHasAileronControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = false;
    }

    private final void doRemoveBlisters()
    {
        for(int i = 1; i < 10; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && ((FlightModelMain) (super.FM)).AS.getPilotHealth(i - 1) > 0.0F)
            {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(((FlightModelMain) (super.FM)).Vwld);
                wreckage.setSpeed(vector3d);
            }

    }

    public void typeFighterAceMakerRangeFinder()
    {
        if(k14Mode != 1)
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
        {
            k14Distance = 200F;
            hunted = War.GetNearestEnemyAircraft(FM.actor, 2700F, 9);
        }
        if(hunted != null)
        {
            k14Distance = (float)FM.actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
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
        if(calculateMach() <= 1.0F)
        {
            FM.VmaxAllowed = FM.getSpeedKMH() + f;
            SonicBoom = 0.0F;
            isSonic = false;
        }
        if(calculateMach() >= 1.0F)
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
        if(calculateMach() > 1.01F || calculateMach() < 1.0F)
            Eff3DActor.finish(shockwave);
    }

    public void engineSurge(float f)
    {
        if(FM.AS.isMaster())
        {
            for(int i = 0; i < 1; i++)
                if(curthrl == -1F)
                {
                    curthrl = oldthrl = FM.EI.engines[i].getControlThrottle();
                } else
                {
                    curthrl = FM.EI.engines[i].getControlThrottle();
                    if(curthrl < 1.05F)
                    {
                        if((curthrl - oldthrl) / f > 20F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                        {
                            if(FM.actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.01D * (double)(FM.EI.engines[i].getRPM() / 1000F);
                            FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                                FM.AS.hitEngine(this, i, 100);
                            if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                                FM.EI.engines[i].setEngineDies(this);
                        }
                        if((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6)
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
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                        }
                    }
                    oldthrl = curthrl;
                }

        }
    }

    public void update(float f)
    {
        if(FM.getSpeedKMH() > 700F && FM.CT.bHasFlapsControl)
        {
            FM.CT.FlapsControl = 0.0F;
            FM.CT.bHasFlapsControl = false;
        } else
        {
            FM.CT.bHasFlapsControl = true;
        }
        if((((FlightModelMain) (super.FM)).AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && super.FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            ((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = false;
            if(Time.current() > lTimeNextEject)
                bailout();
        }
        if(((FlightModelMain) (super.FM)).AS.isMaster() && Config.isUSE_RENDER())
        {
            if(FM.EI.engines[0].getThrustOutput() > 0.5F && FM.EI.engines[0].getStage() == 6)
            {
                if(FM.EI.engines[0].getThrustOutput() > 1.001F)
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 5);
                else
                if(FM.EI.engines[0].getThrustOutput() > 0.92F && FM.EI.engines[0].getThrustOutput() < 1.001F)
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 3);
                else
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 2);
            } else
            {
                ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 0);
            }
            setExhaustFlame(Math.round(Aircraft.cvt(FM.EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
            if(FM.EI.engines[1].getThrustOutput() > 0.25F && FM.EI.engines[1].getStage() == 6)
            {
                if(FM.EI.engines[1].getThrustOutput() > 1.001F)
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 1, 5);
                else
                if(FM.EI.engines[1].getThrustOutput() > 0.92F && FM.EI.engines[1].getThrustOutput() < 1.001F)
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 1, 3);
                else
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 1, 2);
            } else
            {
                ((FlightModelMain) (super.FM)).AS.setSootState(this, 1, 0);
            }
            setExhaustFlame(Math.round(Aircraft.cvt(FM.EI.engines[1].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
            if(FM instanceof RealFlightModel)
                umn();
        }
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
        if(FLIR)
            laser(TypeLaserSpotter.spot);
        updatecontrollaser();
        engineSurge(f);
        typeFighterAceMakerRangeFinder();
        checkHydraulicStatus();
        moveHydraulics(f);
        soundbarier();
        super.update(f);
        computeThrust();
        computeLift();
        Flaps();
        if(FM.getSpeedKMH() > 250F)
            FM.CT.cockpitDoorControl = 0.0F;
        if(FM.getSpeedKMH() > 300F)
            FM.CT.bHasCockpitDoorControl = false;
        else
            FM.CT.bHasCockpitDoorControl = true;
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
            // fall through

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Head2_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            bObserverKilled = true;
            // fall through

        default:
            return;
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

    public void netUpdateWayPoint()
    {
        super.netUpdateWayPoint();
        if(!(FM instanceof RealFlightModel))
            return;
        else
            return;
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
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.5F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 4F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            break;

        case 2: // '\002'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.8F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.5F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;

        case 5: // '\005'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurnerF18.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurnerF18A.eff", -1F);
            break;

        case 4: // '\004'
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;
        }
    }

    private final void umn()
    {
        Vector3d vector3d = FM.getVflow();
        mn = (float)vector3d.lengthSquared();
        mn = (float)Math.sqrt(mn);
        A_6 a_6 = this;
        float f = mn;
        World.cur().getClass();
        a_6.mn = f / Atmosphere.sonicSpeed((float)FM.Loc.z);
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

    public void moveHydraulics(float f)
    {
        if(gearTargetAngle >= 0.0F)
        {
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
        //    hierMesh().chunkSetAngles("GearL6_D0", 0.0F, -gearCurrentAngle, 0.0F);
        //    hierMesh().chunkSetAngles("GearR6_D0", 0.0F, gearCurrentAngle, 0.0F);
        //    hierMesh().chunkSetAngles("GearC4_D0", 0.0F, gearCurrentAngle, 0.0F);
        }
    }

    public void moveCockpitDoor(float f)
    {
        if(FM.CT.bMoveSideDoor)
        {
            hierMesh().chunkSetAngles("Nose1_D0", 0.0F, 0.0F, 45F * f);
        }
        else
        {
            resetYPRmodifier();
            if(f < 0.05F)
            {
                Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, 0.01F);
                Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, 0.01F);
            } else
            {
                Aircraft.xyz[1] = Aircraft.cvt(f, 0.1F, 0.99F, 0.01F, 1.1F);
                Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, 0.01F);
            }
            hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
            if(Config.isUSE_RENDER())
            {
                if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                    Main3D.cur3D().cockpits[0].onDoorMoved(f);
                setDoorSnd(f);
            }
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 100F * f2, 0.0F);
        hiermesh.chunkSetAngles("GearC21_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, -92F * f2, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 90F * f, 35F * f);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -90F * f1, 35F * f1);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 100F * f, 35F * f);
        hiermesh.chunkSetAngles("GearL11_D0", 0.0F, Aircraft.cvt(f, 0.03F, 0.15F, 0.0F, 30F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -100F * f1, 35F * f1);
        hiermesh.chunkSetAngles("GearR11_D0", 0.0F, Aircraft.cvt(f1, 0.03F, 0.15F, 0.0F, -30F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.19F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.19F, 0.0F, 90F), 0.0F);
    }

    protected void moveGear(float f, float f1, float f2) {
        moveGear(hierMesh(), f, f1, f2);
    }

    // ************************************************************************************************
    // Gear code for backward compatibility, older base game versions don'f1 indepently move their gears
    public static void moveGear(HierMesh hiermesh, float f) {
        moveGear(hiermesh, f, f, f); // re-route old style function calls to new code
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        float f = FM.Gears.gWheelSinking[2];
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 0.19F, 0.0F, 0.19F);
        hierMesh().chunkSetLocate("GearC22_D0", Aircraft.xyz, Aircraft.ypr);
        f = Aircraft.cvt(f, 0.0F, 19F, 0.0F, 30F);
        hierMesh().chunkSetAngles("GearC7_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("GearC8_D0", 0.0F, 2.0F * f, 0.0F);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.19F, 0.0F, 0.19F);
        hierMesh().chunkSetLocate("GearL22_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.19F, 0.0F, 0.19F);
        hierMesh().chunkSetLocate("GearR22_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, 35F * f, 0.0F);
    }

    public void moveSteering(float f)
    {
        if(FM.CT.GearControl > 0.5F && FM.Gears.onGround())
            hierMesh().chunkSetAngles("GearC7_D0", 0.0F, -1.0F * f, 0.0F);
        if(FM.CT.GearControl < 0.5F)
            hierMesh().chunkSetAngles("GearC7_D0", 0.0F, 0.0F, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 24F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -24F * f, 0.0F);
    }

    protected void moveAileron(float f)
    {
        if(FM.CT.getWing() > 0.001F)
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, 0.0F, 0.0F);
            return;
        }
        if(f < 0F)
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -55F * f, 0.0F);
            hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, -55F * f, 0.0F);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, 0.0F, 0.0F);
        }
        else
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -55F * f, 0.0F);
            hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, -55F * f, 0.0F);
        }
    }

    protected void moveFlap(float f)
    {
        resetYPRmodifier();
        if(FM.CT.getWing() == 0.0F)
        {
            Aircraft.xyz[0] = 0.16F * f;
            Aircraft.ypr[1] = -24F * f;
        }
        else
            FM.CT.FlapsControl = 0.0F;
        hierMesh().chunkSetLocate("Flap01_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("Flap02_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("Flap03_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("Flap04_D0", Aircraft.xyz, Aircraft.ypr);

        float xyz_Lin[] = { 0.0F, 0.0F, 0.0F };
        float ypr_Lin[] = { 0.0F, 0.0F, 0.0F };
        float xyz_Lout[] = { 0.0F, 0.0F, 0.0F };
        float ypr_Lout[] = { 0.0F, 0.0F, 0.0F };
        float xyz_Rin[] = { 0.0F, 0.0F, 0.0F };
        float ypr_Rin[] = { 0.0F, 0.0F, 0.0F };
        float xyz_Rout[] = { 0.0F, 0.0F, 0.0F };
        float ypr_Rout[] = { 0.0F, 0.0F, 0.0F };
        if(FM.CT.getWing() == 0.0F)
        {
            xyz_Lin[0] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.141F);
            xyz_Lin[1] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, 0.078F);
            xyz_Lin[2] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.075F);
            ypr_Lin[1] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, 27.5F);
            xyz_Lout[0] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.059F);
            xyz_Lout[1] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.09F);
            xyz_Lout[2] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.061F);
            ypr_Lout[2] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -27.5F);
            xyz_Rin[0] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, 0.141F);
            xyz_Rin[1] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, 0.078F);
            xyz_Rin[2] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.075F);
            ypr_Rin[1] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -27.5F);
            xyz_Rout[0] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, 0.059F);
            xyz_Rout[1] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.09F);
            xyz_Rout[2] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.061F);
            ypr_Rout[2] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -27.5F);
        }
        hierMesh().chunkSetLocate("SlatL_D0", xyz_Lin, ypr_Lin);
        hierMesh().chunkSetLocate("SlatL_Out", xyz_Lout, ypr_Lout);
        hierMesh().chunkSetLocate("SlatR_D0", xyz_Rin, ypr_Rin);
        hierMesh().chunkSetLocate("SlatR_Out", xyz_Rout, ypr_Rout);
    }

    protected void moveFan(float f)
    {
    }

    protected void moveAirBrake(float f)
    {
        if(FM.CT.getWing() > 0.001F)
        {
            hierMesh().chunkSetAngles("BrakeL1_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("BrakeL2_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("BrakeR1_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("BrakeR2_D0", 0.0F, 0.0F, 0.0F);
        }
        else
        {
            hierMesh().chunkSetAngles("BrakeL1_D0", 0.0F, 0.0F, -60F * f);
            hierMesh().chunkSetAngles("BrakeL2_D0", 0.0F, 0.0F, 60F * f);
            hierMesh().chunkSetAngles("BrakeR1_D0", 0.0F, 0.0F, -60F * f);
            hierMesh().chunkSetAngles("BrakeR2_D0", 0.0F, 0.0F, 60F * f);
        }
        if(bGen1st)
        {
            hierMesh().chunkSetAngles("BrakeR_D0", 0.0F, 50F * f, 0.0F);
            hierMesh().chunkSetAngles("BrakeL_D0", 0.0F, -50F * f, 0.0F);
        }
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 0.0F, 70F * f);
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 140F), 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -140F), 0.0F);
    }

    public void moveWingFold(float f)
    {
        if(f < 0.001F)
        {
            setGunPodsOn(true);
            hideWingWeapons(false);
        } else
        {
            setGunPodsOn(false);
            ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
            hideWingWeapons(false);
            resetYPRmodifier();
            hierMesh().chunkSetLocate("Flap01_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("Flap02_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("Flap03_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("Flap04_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("SlatL_Out", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("SlatR_Out", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("BrakeL1_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("BrakeL2_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("BrakeR1_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("BrakeR2_D0", 0.0F, 0.0F, 0.0F);
        }
        moveWingFold(hierMesh(), f);
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

    public void computeThrust()
    {
        if(FM.EI.engines[0].getThrustOutput() > 0.95F && (double)calculateMach() < 0.32000000000000001D && FM.EI.engines[0].getStage() > 5)
            FM.producedAF.x += 9000D;
        if(FM.EI.engines[1].getThrustOutput() > 0.95F && (double)calculateMach() < 0.32000000000000001D && FM.EI.engines[1].getStage() > 5)
            FM.producedAF.x += 9000D;
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && FM.EI.engines[0].getStage() == 6 && FM.EI.engines[1].getThrustOutput() < 1.001F && FM.EI.engines[1].getStage() == 6)
            if((double)f > 13.5D)
            {
                f1 = 11F;
            } else
            {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = ((358F * f3 - 7309F * f2) + 41826F * f) / 7560F;
            }
        FM.producedAF.x -= f1 * 1000F;
    }

    public void computeLift()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float f = calculateMach();
        if(calculateMach() < 0.0F);
        float f1 = 0.0F;
        if((double)f > 2.25D)
        {
            f1 = 0.12F;
        } else
        {
            float f2 = f * f;
            float f3 = f2 * f;
            float f4 = f3 * f;
            float f5 = f4 * f;
            float f6 = f5 * f;
            float f7 = f6 * f;
            float f8 = f7 * f;
            float f9 = f8 * f;
            f1 = ((((((-0.0998429F * f8 + 1.02985F * f7) - 4.34775F * f6) + 9.5949F * f5) - 11.7055F * f4) + 7.70811F * f3) - 2.5701F * f2) + 0.398187F * f + 0.078F;
        }
        polares.lineCyCoeff = f1;
    }

    private boolean Flaps()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        if(FM.EI.engines[0].getThrustOutput() > 0.95F && (double)calculateMach() < 0.35999999999999999D && !Flaps)
        {
            polares.Cy0_1 += 1.2D;
            Flaps = true;
        }
        if(FM.EI.engines[0].getThrustOutput() < 0.95F && (double)calculateMach() >= 0.35999999999999999D && Flaps)
        {
            polares.Cy0_1 -= 1.2D;
            Flaps = false;
        }
        return Flaps;
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    public float Fuelamount;
    public long tvect;
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
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
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
    private boolean APmode2;
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
    public float fNozzleOpenL;
    private Field thrustMaxField[];
    private long lTimeNextEject;
    boolean bObserverKilled;
    private int obsLookTime;
    private float obsLookAzimuth;
    private float obsLookElevation;
    private float obsAzimuth;
    private float obsElevation;
    private float obsAzimuthOld;
    private float obsElevationOld;
    private float obsMove;
    private float obsMoveTot;
    private boolean Flaps;
    private boolean bGen1st;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.A_6.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}