package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.weapons.MissileInterceptable;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;

public class E_3A extends C_135
    implements TypeBomber, TypeRadar
{
    public class EnemyRadarData
        implements Comparable
    {

        public int compareTo(Object obj)
        {
            EnemyRadarData enemyradardata = (EnemyRadarData)obj;
            byte byte0 = 0;
            if(distance - enemyradardata.distance > 0.0D)
                byte0 = 1;
            else
            if(distance - enemyradardata.distance < 0.0D)
                byte0 = -1;
            return byte0;
        }

        Actor actor;
        double distance;
        double speed;
        double dirdiff;

        public EnemyRadarData()
        {
            actor = null;
            distance = -1D;
            speed = -1D;
            dirdiff = 0.0D;
        }
    }


    public E_3A()
    {
        radarDisplayRange = 0;
        radarDisplayVrt = 0;
        radarDisplayHol = 0;
        rotoMode = 0;
        rotoOrient = 0.0F;
        rotodegpm = 0;
        rareTimer = -1L;
        awacsTimer = -1L;
        awacsCounter = 0;
        enemyPlaneList = new ArrayList();
        enemyCruiseMissileList = new ArrayList();
        friendlyPlaneList = new ArrayList();
        friendlyAwacsList = new ArrayList();
        radarDisplayRange = 0;
        radarDisplayVrt = 0;
        radarDisplayHol = 0;
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        return "E-3A_";
    }

    private static double distanceBetween(Actor actor, Actor actor1)
    {
        double d = 999999.99899999995D;
        if(!Actor.isValid(actor) || !Actor.isValid(actor1))
        {
            return d;
        } else
        {
            Loc loc = new Loc();
            Point3d point3d = new Point3d();
            Point3d point3d1 = new Point3d();
            actor.pos.getAbs(loc);
            loc.get(point3d);
            actor1.pos.getAbs(point3d1);
            double d1 = point3d.distance(point3d1);
            return d1;
        }
    }

    private static float angleBetween(Actor actor, Actor actor1)
    {
        float f = 180.1F;
        if(!Actor.isValid(actor) || !Actor.isValid(actor1))
        {
            return f;
        } else
        {
            double d = 0.0D;
            Loc loc = new Loc();
            Point3d point3d = new Point3d();
            Point3d point3d1 = new Point3d();
            Vector3d vector3d = new Vector3d();
            Vector3d vector3d1 = new Vector3d();
            actor.pos.getAbs(loc);
            loc.get(point3d);
            actor1.pos.getAbs(point3d1);
            vector3d.sub(point3d1, point3d);
            d = vector3d.length();
            vector3d.scale(1.0D / d);
            vector3d1.set(1.0D, 0.0D, 0.0D);
            loc.transform(vector3d1);
            d = vector3d1.dot(vector3d);
            float f1 = Geom.RAD2DEG((float)Math.acos(d));
            return f1;
        }
    }

    private static float angle360Between(Actor actor, Actor actor1)
    {
        float f = 360.1F;
        if(!Actor.isValid(actor) || !Actor.isValid(actor1))
            return f;
        double d = 0.0D;
        double d1 = (double)(-actor.pos.getAbsOrient().getYaw()) + 90D;
        if(d1 < 0.0D)
            d1 += 360D;
        double d2 = actor1.pos.getAbsPoint().x - actor.pos.getAbsPoint().x;
        double d3 = actor1.pos.getAbsPoint().y - actor.pos.getAbsPoint().y;
        double d4 = Math.toDegrees(Math.atan2(d3, -d2)) - 90D;
        if(d4 < 0.0D)
            d4 += 360D;
        double d5 = d4 - d1;
        if(d5 < 0.0D)
            d5 += 360D;
        f = (float)d5;
        return f;
    }

    public void update(float f)
    {
        super.update(f);
        moveRotodome(f);
        if(!FM.AS.isMaster() || !Config.isUSE_RENDER())
            return;
        for(int i = 0; i < FM.EI.engines.length; i++)
            if(FM.EI.engines[i].getPowerOutput() > 0.8F && FM.EI.engines[i].getStage() == 6)
            {
                if(FM.EI.engines[i].getPowerOutput() > 0.95F)
                    FM.AS.setSootState(this, i, 3);
                else
                    FM.AS.setSootState(this, i, 2);
            } else
            {
                FM.AS.setSootState(this, i, 0);
            }

    }

    protected void moveRotodome(float f)
    {
        if(FM.EI.engines[0].getStage() < 6 && FM.EI.engines[1].getStage() < 6 && FM.EI.engines[2].getStage() < 6 && FM.EI.engines[3].getStage() < 6)
        {
            rotoMode = 0;
            rotodegpm = 0;
            return;
        }
        if(FM.getAltitude() < 950F)
        {
            rotoMode = 1;
            rotodegpm = 90;
        } else
        if(FM.getAltitude() > 1000F)
        {
            rotoMode = 2;
            rotodegpm = 2160;
        }
        rotoOrient -= (f * (float)rotodegpm) / 60F;
        if(rotoOrient < -360F)
            rotoOrient += 360F;
        hierMesh().chunkSetAngles("RADAR_D0", rotoOrient, 0.0F, 0.0F);
    }

    public void rareAction(float f, boolean flag)
    {
        if(rareTimer != Time.current() && rotoMode == 2 && Time.current() > awacsTimer + 10000L)
        {
            AWACS();
            awacsTimer = Time.current();
            awacsCounter++;
        }
        super.rareAction(f, flag);
        rareTimer = Time.current();
    }

    private void AWACS()
    {
        enemyPlaneList.clear();
        enemyCruiseMissileList.clear();
        friendlyPlaneList.clear();
        friendlyAwacsList.clear();
        List list = Engine.targets();
        Object obj = null;
        double d = 0.0D;
        double d2 = 0.0D;
        boolean flag = false;
        Point3d point3d = new Point3d();
        for(int i = 0; i < list.size(); i++)
        {
            Actor actor = (Actor)list.get(i);
            if(actor != this && Actor.isValid(actor) && ((actor instanceof Aircraft) || (actor instanceof MissileInterceptable)) && distanceBetween(this, actor) <= maxRange)
            {
                double d1 = actor.pos.getAbsPoint().z - Engine.land().HQ_Air(actor.pos.getAbsPoint().x, actor.pos.getAbsPoint().y);
                double d3 = actor.pos.getAbsPoint().z;
                if(d1 >= minAltitude && (d1 >= thresholdLA || distanceBetween(this, actor) <= maxRangeLA) && (d1 >= thresholdAbsLA || d3 <= 120D || distanceBetween(this, actor) <= maxRangeAbsLA) && !Landscape.rayHitHQ(pos.getAbsPoint(), actor.pos.getAbsPoint(), point3d) && (Mission.curCloudsType() <= 4 || (double)Mission.curCloudsHeight() - 20D <= pos.getAbsPoint().z && (double)Mission.curCloudsHeight() - 30D <= actor.pos.getAbsPoint().z || Main.cur().clouds == null || distanceBetween(this, actor) <= maxRangeLA * (double)Main.cur().clouds.getVisibility(pos.getAbsPoint(), actor.pos.getAbsPoint()) * (double)TrueRandom.nextFloat(0.7F, 1.2F) * (double)(float)(7 - Mission.curCloudsType())))
                    if(actor.getArmy() != getArmy())
                    {
                        if(actor instanceof Aircraft)
                        {
                            enemyPlaneList.add(actor);
                            if(actor instanceof TypeRadarWarningReceiver)
                                ((TypeRadarWarningReceiver)actor).myRadarSearchYou(this, sr_soundpreset);
                        } else
                        if((actor instanceof MissileInterceptable) && ((MissileInterceptable)actor).isReleased())
                            enemyCruiseMissileList.add(actor);
                    } else
                    if(actor instanceof Aircraft)
                        if(actor instanceof E_3A)
                        {
                            friendlyAwacsList.add(actor);
                        } else
                        {
                            friendlyPlaneList.add(actor);
                            if(actor == World.getPlayerAircraft())
                                flag = true;
                        }
            }
        }

        if(this == World.getPlayerAircraft() && awacsCounter % 5 == 0)
            AWACSmessageOwn();
        else
        if(flag && enemyPlaneList.size() > 0 && awacsCounter % 5 == 0)
            AWACSmessageSend();
    }

    private void AWACSmessageOwn()
    {
        if(enemyPlaneList.size() + enemyCruiseMissileList.size() + friendlyPlaneList.size() + friendlyAwacsList.size() == 0)
            HUD.logCenter("                                          No Radar detect");
        else
            HUD.logCenter("                     " + enemyPlaneList.size() + " enemy planes detected, " + enemyCruiseMissileList.size() + " enemy missiles detected / " + friendlyPlaneList.size() + " friendly aircrafts");
    }

    private void AWACSmessageSend()
    {
        ArrayList arraylist = new ArrayList();
        arraylist.clear();
        Object obj = null;
        Vector3d vector3d = new Vector3d();
        Main3D.cur3D();
        int i = HUD.drawSpeed();
        for(int j = 0; j < enemyPlaneList.size(); j++)
        {
            EnemyRadarData enemyradardata = new EnemyRadarData();
            enemyradardata.actor = (Actor)enemyPlaneList.get(j);
            enemyradardata.distance = distanceBetween(enemyradardata.actor, World.getPlayerAircraft());
            enemyradardata.actor.pos.speed(vector3d);
            enemyradardata.speed = vector3d.length();
            enemyradardata.dirdiff = angleBetween(enemyradardata.actor, World.getPlayerAircraft());
            arraylist.add(enemyradardata);
        }

        Collections.sort(arraylist);
        int k = (int)((angle360Between(World.getPlayerAircraft(), ((EnemyRadarData)arraylist.get(0)).actor) + 5.5F) * 0.1F) * 10;
        int l = (int)(-((double)((EnemyRadarData)arraylist.get(0)).actor.pos.getAbsOrient().getYaw() - 90D - 5.5D) * 0.10000000149011612D) * 10;
        if(l < 0)
            l += 360;
        switch(i)
        {
        case 1:
        case 4:
        default:
            HUD.logCenter("                     Target bearing " + k + "\260" + ", range " + (int)(((EnemyRadarData)arraylist.get(0)).distance * 0.001D) + "km, height " + (int)(((EnemyRadarData)arraylist.get(0)).actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10 + "m, heading " + l + "\260" + ", Spd " + (int)(((EnemyRadarData)arraylist.get(0)).speed * 0.35999999999999999D + 0.5D) * 10 + "km/h");
            break;

        case 2:
        case 5:
            HUD.logCenter("                     Target bearing " + k + "\260" + ", range " + (int)(((EnemyRadarData)arraylist.get(0)).distance * 0.00053995680000000002D) + "NM, height " + (int)(((EnemyRadarData)arraylist.get(0)).actor.pos.getAbsPoint().z * 0.32807999999999998D) * 10 + "ft, heading " + l + "\260" + ", Spd " + (int)(((EnemyRadarData)arraylist.get(0)).speed * 0.19438449999999999D + 0.5D) * 10 + "kt");
            break;

        case 3:
        case 6:
            HUD.logCenter("                     Target bearing " + k + "\260" + ", range " + (int)(((EnemyRadarData)arraylist.get(0)).distance * 0.00062137100000000001D) + "mile, height " + (int)(((EnemyRadarData)arraylist.get(0)).actor.pos.getAbsPoint().z * 0.32807999999999998D) * 10 + "ft, heading " + l + "\260" + ", Spd " + (int)(((EnemyRadarData)arraylist.get(0)).speed * 0.22369359999999999D + 0.5D) * 10 + "mph");
            break;
        }
    }

    public void typeRadarGainMinus()
    {
    }

    public void typeRadarGainPlus()
    {
    }

    public void typeRadarRangeMinus()
    {
        radarDisplayRange++;
        if(radarDisplayRange > 2)
            radarDisplayRange = 2;
        if(radarDisplayRange == 1)
        {
            radarDisplayVrt = 0;
            radarDisplayHol = 0;
        } else
        if(radarDisplayRange == 2)
        {
            if(radarDisplayVrt == 0)
                radarDisplayVrt = 1;
            else
            if(radarDisplayVrt == 1)
                radarDisplayVrt = 2;
            if(radarDisplayHol == 0)
                radarDisplayHol = 1;
            else
            if(radarDisplayHol == 1)
                radarDisplayHol = 2;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar dislay range " + radarDisplayRange);
    }

    public void typeRadarRangePlus()
    {
        radarDisplayRange--;
        if(radarDisplayRange < 0)
            radarDisplayRange = 0;
        if(radarDisplayRange == 0)
        {
            radarDisplayVrt = 0;
            radarDisplayHol = 0;
        } else
        if(radarDisplayRange == 1)
        {
            if(radarDisplayVrt < 2)
                radarDisplayVrt = 0;
            else
                radarDisplayVrt = 1;
            if(radarDisplayHol < 2)
                radarDisplayHol = 0;
            else
                radarDisplayHol = 1;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar dislay range " + radarDisplayRange);
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
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
        if(radarDisplayRange == 0)
            return;
        radarDisplayVrt--;
        if(radarDisplayVrt < 0)
            radarDisplayVrt = 0;
    }

    public void typeBomberAdjDistanceMinus()
    {
        if(radarDisplayRange == 0)
            return;
        int i = 0;
        if(radarDisplayRange == 1)
            i = 1;
        else
        if(radarDisplayRange == 2)
            i = 3;
        radarDisplayVrt++;
        if(radarDisplayVrt > i)
            radarDisplayVrt = i;
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
        if(radarDisplayRange == 0)
            return;
        radarDisplayHol--;
        if(radarDisplayHol < 0)
            radarDisplayHol = 0;
    }

    public void typeBomberAdjSideslipMinus()
    {
        if(radarDisplayRange == 0)
            return;
        int i = 0;
        if(radarDisplayRange == 1)
            i = 1;
        else
        if(radarDisplayRange == 2)
            i = 3;
        radarDisplayHol++;
        if(radarDisplayHol > i)
            radarDisplayHol = i;
    }

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
    }

    public void typeBomberAdjAltitudeMinus()
    {
    }

    public void typeBomberAdjSpeedReset()
    {
    }

    public void typeBomberAdjSpeedPlus()
    {
    }

    public void typeBomberAdjSpeedMinus()
    {
    }

    public void typeBomberUpdate(float f)
    {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    public boolean typeBomberToggleAutomation()
    {
        return true;
    }

    public ArrayList getEnemyPlaneList()
    {
        return enemyPlaneList;
    }

    public ArrayList getEnemyCruiseMissileList()
    {
        return enemyCruiseMissileList;
    }

    public ArrayList getFriendlyPlaneList()
    {
        return friendlyPlaneList;
    }

    public ArrayList getFriendlyAwacsList()
    {
        return friendlyAwacsList;
    }

    private long rareTimer;
    private int rotoMode;
    private int rotodegpm;
    private float rotoOrient;
    private long awacsTimer;
    private int awacsCounter;
    private ArrayList enemyPlaneList;
    private ArrayList enemyCruiseMissileList;
    private ArrayList friendlyPlaneList;
    private ArrayList friendlyAwacsList;
    public int radarDisplayRange;
    public int radarDisplayVrt;
    public int radarDisplayHol;
    private static double maxRange = 550000D;
    private static double maxRangeLA = 320000D;
    private static double thresholdLA = 1200D;
    private static double maxRangeAbsLA = 180000D;
    private static double thresholdAbsLA = 100D;
    private static double minAltitude = 30D;
    private static int maxDetectNum = 640;
    private static int maxTrackNum = 240;
    private static String sr_soundpreset = "aircraft.APR25AAA";

    static 
    {
        Class class1 = E_3A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Sentry");
        Property.set(class1, "meshName", "3DO/Plane/707/hierE-3.him");
        Property.set(class1, "yearService", 1977F);
        Property.set(class1, "yearExpired", 2050F);
        Property.set(class1, "FlightModel", "FlightModels/E-3A.fmd:C135FM");
        Property.set(class1, "cockpitClass", new Class[] {
            Cockpit707.class, CockpitE_3RADAR.class
        });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[0]);
        Aircraft.weaponHooksRegister(class1, new String[0]);
    }
}
