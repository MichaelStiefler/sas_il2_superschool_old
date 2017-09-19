
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapExt;
import com.maddox.util.HashMapInt;
import java.util.*;
import com.maddox.sas1946.il2.util.Reflection;

// Referenced classes of package com.maddox.il2.objects.air:
//            F_4, Aircraft, TypeFighterAceMaker, TypeRadarGunsight,
//            PaintSchemeFMPar05, TypeGuidedMissileCarrier, TypeCountermeasure,
//            TypeThreatDetector, TypeGSuit, TypeAcePlane
//            TypeFastJet, NetAircraft

public class F_14D extends F_14
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

    public F_14D()
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
        guidedMissileUtils = new GuidedMissileUtils(this);
        engineSFX = null;
        engineSTimer = 0x98967f;
        outCommand = new NetMsgFiltered();
        tX4Prev = 0L;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        counter = 0;
        freq = 800;
        Timer1 = Timer2 = freq;
        error = 0;
        counterFlareList = new ArrayList();
        counterChaffList = new ArrayList();
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        return "F14D_";
    }

    private void checkAmmo()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                    if(FM.CT.Weapons[i][j].haveBullets())
                    {
                        if(FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16)
                            counterFlareList.add(FM.CT.Weapons[i][j]);
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunChaff_gn16)
                            counterChaffList.add(FM.CT.Weapons[i][j]);
                        else if(FM.CT.Weapons[i][j] instanceof BombGunGBU10_Mk84LGB_gn16 ||
                                FM.CT.Weapons[i][j] instanceof BombGunGBU12_Mk82LGB_gn16 ||
                                FM.CT.Weapons[i][j] instanceof BombGunGBU16_Mk83LGB_gn16)
                            super.bHasPaveway = true;
                    }
            }
    }

    public void backFire()
    {
        if(counterFlareList.isEmpty())
            hasFlare = false;
        else
        {
            if(Time.current() > lastFlareDeployed + 1200L)
            {
                ((RocketGunFlare_gn16)counterFlareList.get(0)).shots(1);
                hasFlare = true;
                lastFlareDeployed = Time.current();
                if(!((RocketGunFlare_gn16)counterFlareList.get(0)).haveBullets())
                    counterFlareList.remove(0);
            }
        }
        if(counterChaffList.isEmpty())
            hasChaff = false;
        else
        {
            if(Time.current() > lastChaffDeployed + 1500L)
            {
                ((RocketGunChaff_gn16)counterChaffList.get(0)).shots(1);
                hasChaff = true;
                lastChaffDeployed = Time.current();
                if(!((RocketGunChaff_gn16)counterChaffList.get(0)).haveBullets())
                    counterChaffList.remove(0);
            }
        }
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


    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        checkAmmo();
        guidedMissileUtils.onAircraftLoaded();
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        this.computeF110GE400_AB();
        super.update(f);
        if(super.backfire)
            backFire();
    }


    public void rareAction(float f, boolean flag)
    {
        if(raretimer != Time.current() && this == World.getPlayerAircraft())
        {
            counter++;
            if(counter % 6 == 0)
                AN_APG71();
            if(counter % 12 == 3)
                IRST();
            if(counter % 12 == 9)
                InertialNavigation();
        }
        super.rareAction(f, flag);
        raretimer = Time.current();
    }

    private boolean AN_APG71()
    {
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        boolean flag = false;
        boolean flag1 = false;
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        double d3 = d2 - (double)Landscape.Hmin((float)((Actor) (aircraft)).pos.getAbsPoint().x, (float)((Actor) (aircraft)).pos.getAbsPoint().y);
        if(aircraft instanceof F_14D)
            flag1 = true;
        if(d3 < 0.0D)
            d3 = 0.0D;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j += 360;
        for(java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
        {
            Actor actor = (Actor)entry.getValue();
            if(flag1 && (actor instanceof Aircraft) && actor.getArmy() != World.getPlayerArmy() && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
            {
                super.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (actor.pos.getAbsPoint())).x;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).y;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).z;
                double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                String s = "level with us";
                if(d2 - d6 - 300D >= 0.0D)
                    s = "below us";
                if((d2 - d6) + 300D <= 0.0D)
                    s = "above us";
                if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                    s = "slightly below";
                if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                    s = "slightly above";
                double d8 = d4 - d;
                double d9 = d5 - d1;
                float f = 57.32484F * (float)Math.atan2(d9, -d8);
                int k = (int)(Math.floor((int)f) - 90D);
                if(k < 0)
                    k += 360;
                int l = k - i;
                double d10 = d - d4;
                double d11 = d1 - d5;
                Random random = new Random();
                float f1 = ((float)random.nextInt(20) - 10F) / 100F + 1.0F;
                int i1 = random.nextInt(6) - 3;
                float f2 = 370000F;
                float f3 = f2;
                if(d3 < 15D)
                    f3 = (float)(d3 * 0.80000001192092896D * 3D);
                int j1 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f1) / 10D) * 10D);
                if((float)j1 > f2)
                    j1 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
                float f4 = 57.32484F * (float)Math.atan2(j1, d7);
                int k1 = (int)(Math.floor((int)f4) - 90D);
                int l1 = (k1 - (90 - j)) + i1;
                int i2 = (int)f2;
                if((float)j1 < f2)
                    if(j1 > 1150)
                        i2 = (int)(Math.ceil((double)j1 / 900D) * 900D);
                    else
                        i2 = (int)(Math.ceil((double)j1 / 500D) * 500D);
                int j2 = l + i1;
                int k2 = j2;
                if(k2 < 0)
                    k2 += 360;
                float f5 = (float)((double)f3 + Math.sin(Math.toRadians(Math.sqrt(l * l) * 3D)) * ((double)f3 * 0.25D));
                int l2 = (int)((double)f5 * Math.cos(Math.toRadians(l1)));
                String s1 = "  ";
                if(k2 < 5)
                    s1 = "Dead ahead, ";
                if(k2 >= 5 && (double)k2 <= 7.5D)
                    s1 = "Right 5, ";
                if((double)k2 > 7.5D && (double)k2 <= 12.5D)
                    s1 = "Right 10, ";
                if((double)k2 > 12.5D && (double)k2 <= 17.5D)
                    s1 = "Right 15, ";
                if((double)k2 > 17.5D && k2 <= 25)
                    s1 = "Right 20, ";
                if(k2 > 25 && k2 <= 35)
                    s1 = "Right 30, ";
                if(k2 > 35 && k2 <= 45)
                    s1 = "Right 40, ";
                if(k2 > 45 && k2 <= 60)
                    s1 = "Turn right, ";
                if(k2 > 355)
                    s1 = "Dead ahead, ";
                if(k2 <= 355 && (double)k2 >= 352.5D)
                    s1 = "Left 5, ";
                if((double)k2 < 352.5D && (double)k2 >= 347.5D)
                    s1 = "Left 10, ";
                if((double)k2 < 347.5D && (double)k2 >= 342.5D)
                    s1 = "Left 15, ";
                if((double)k2 < 342.5D && k2 >= 335)
                    s1 = "Left 20, ";
                if(k2 < 335 && k2 >= 325)
                    s1 = "Left 30, ";
                if(k2 < 325 && k2 >= 315)
                    s1 = "Left 40, ";
                if(k2 < 345 && k2 >= 300)
                    s1 = "Turn left, ";
                String s2 = "  ";
                if(l1 < -10)
                    s2 = "nose down";
                if(l1 >= -10 && l1 <= -5)
                    s2 = "down a bit";
                if(l1 > -5 && l1 < 5)
                    s2 = "level";
                if(l1 <= 10 && l1 >= 5)
                    s2 = "up a bit";
                if(l1 > 10)
                    s2 = "pull up";
                String s3 = "  ";
                if(k2 < 5)
                    s3 = "dead ahead, ";
                if(k2 >= 5 && (double)k2 <= 7.5D)
                    s3 = "right by 5\260, ";
                if((double)k2 > 7.5D && (double)k2 <= 12.5D)
                    s3 = "right by 10\260, ";
                if((double)k2 > 12.5D && (double)k2 <= 17.5D)
                    s3 = "right by 15\260, ";
                if((double)k2 > 17.5D && k2 <= 25)
                    s3 = "right by 20\260, ";
                if(k2 > 25 && k2 <= 35)
                    s3 = "right by 30\260, ";
                if(k2 > 35 && k2 <= 45)
                    s3 = "right by 40\260, ";
                if(k2 > 45 && k2 <= 60)
                    s3 = "off our right, ";
                if(k2 > 355)
                    s3 = "dead ahead, ";
                if(k2 <= 355 && (double)k2 >= 352.5D)
                    s3 = "left by 5\260, ";
                if((double)k2 < 352.5D && (double)k2 >= 347.5D)
                    s3 = "left by 10\260, ";
                if((double)k2 < 347.5D && (double)k2 >= 342.5D)
                    s3 = "left by 15\260, ";
                if((double)k2 < 342.5D && k2 >= 335)
                    s3 = "left by 20\260, ";
                if(k2 < 335 && k2 >= 325)
                    s3 = "left by 30\260, ";
                if(k2 < 325 && k2 >= 315)
                    s3 = "left by 40\260, ";
                if(k2 < 345 && k2 >= 300)
                    s3 = "off our left, ";
                if((double)j1 <= (double)l2 && (double)j1 > 100D && l1 >= -20 && l1 <= 20 && Math.sqrt(j2 * j2) <= 60D)
                {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "WSO: Contact " + s3 + s + ", " + i2 + "m");
                    freq = 1;
                } else
                {
                    freq = 1;
                }
                setTimer(freq);
            }
        }

        return true;
    }


    private boolean IRST()
    {
        if(thisWeaponsName.endsWith("IRIAF"))
            return false;

        boolean noTV = false;
        // F-14D has both IR sensor and Visible Ray TV camera.
        // At Daytime, TV camera works as determining aircraft classes.
        // At Night or bad weather, TV camera doesn't work and only IR sensor shows the direction.
        if(World.Sun().ToSun.z < 0.03F
           || ((Mission.curCloudsType() > 3 || (Mission.curCloudsType() > 1 && World.Sun().ToSun.z < 0.10F))
              && Mission.curCloudsHeight() > ((Actor) this).pos.getAbsPoint().z))
            noTV = true;

        int irstListSize = irstPlaneName.length;

        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        boolean flag = false;
        boolean flag1 = false;
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        double d3 = d2 - (double)Landscape.Hmin((float)((Actor) (aircraft)).pos.getAbsPoint().x, (float)((Actor) (aircraft)).pos.getAbsPoint().y);
        if(aircraft instanceof F_14D)
            flag1 = true;
        if(d3 < 0.0D)
            d3 = 0.0D;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j += 360;
        for(java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
        {
            Actor actor = (Actor)entry.getValue();
            if(!noTV && flag1 && (actor instanceof Aircraft) && actor.getArmy() != World.getPlayerArmy() && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D
               && !(Main.cur().clouds != null && Main.cur().clouds.getVisibility(actor.pos.getAbsPoint(), aircraft.pos.getAbsPoint()) < 1.0F)
               && !((Mission.curCloudsType() > 3 || (Mission.curCloudsType() > 1 && World.Sun().ToSun.z < 0.10F))
                    && Mission.curCloudsHeight() > actor.pos.getAbsPoint().z))
            {
                super.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (actor.pos.getAbsPoint())).x;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).y;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).z;
                double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                String s = "level with us";
                if(d2 - d6 - 300D >= 0.0D)
                    s = "below us";
                if((d2 - d6) + 300D <= 0.0D)
                    s = "above us";
                if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                    s = "slightly below";
                if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                    s = "slightly above";
                double d8 = d4 - d;
                double d9 = d5 - d1;
                float f = 57.32484F * (float)Math.atan2(d9, -d8);
                int k = (int)(Math.floor((int)f) - 90D);
                if(k < 0)
                    k += 360;
                int l = k - i;
                double d10 = d - d4;
                double d11 = d1 - d5;
                Random random = new Random();
                float f1 = ((float)random.nextInt(20) - 10F) / 100F + 1.0F;
                int i1 = random.nextInt(6) - 3;
                float f2 = 100000F;
                float f3 = f2;
                if(d3 < 15D)
                    f3 = (float)(d3 * 0.80000001192092896D * 3D);
                int j1 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f1) / 10D) * 10D);
                if((float)j1 > f2)
                    j1 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
                float f4 = 57.32484F * (float)Math.atan2(j1, d7);
                int k1 = (int)(Math.floor((int)f4) - 90D);
                int l1 = (k1 - (90 - j)) + i1;
                int i2 = (int)f2;
                if((float)j1 < f2)
                    if(j1 > 1150)
                        i2 = (int)(Math.ceil((double)j1 / 900D) * 900D);
                    else
                        i2 = (int)(Math.ceil((double)j1 / 500D) * 500D);
                int j2 = l + i1;
                int k2 = j2;
                if(k2 < 0)
                    k2 += 360;
                float f5 = (float)((double)f3 + Math.sin(Math.toRadians(Math.sqrt(l * l) * 3D)) * ((double)f3 * 0.25D));
                int l2 = (int)((double)f5 * Math.cos(Math.toRadians(l1)));
                String s1 = "  ";
                if(k2 < 5)
                    s1 = "Dead ahead, ";
                if(k2 >= 5 && (double)k2 <= 7.5D)
                    s1 = "Right 5, ";
                if((double)k2 > 7.5D && (double)k2 <= 12.5D)
                    s1 = "Right 10, ";
                if((double)k2 > 12.5D && (double)k2 <= 17.5D)
                    s1 = "Right 15, ";
                if((double)k2 > 17.5D && k2 <= 25)
                    s1 = "Right 20, ";
                if(k2 > 25 && k2 <= 35)
                    s1 = "Right 30, ";
                if(k2 > 35 && k2 <= 45)
                    s1 = "Right 40, ";
                if(k2 > 45 && k2 <= 60)
                    s1 = "Turn right, ";
                if(k2 > 355)
                    s1 = "Dead ahead, ";
                if(k2 <= 355 && (double)k2 >= 352.5D)
                    s1 = "Left 5, ";
                if((double)k2 < 352.5D && (double)k2 >= 347.5D)
                    s1 = "Left 10, ";
                if((double)k2 < 347.5D && (double)k2 >= 342.5D)
                    s1 = "Left 15, ";
                if((double)k2 < 342.5D && k2 >= 335)
                    s1 = "Left 20, ";
                if(k2 < 335 && k2 >= 325)
                    s1 = "Left 30, ";
                if(k2 < 325 && k2 >= 315)
                    s1 = "Left 40, ";
                if(k2 < 345 && k2 >= 300)
                    s1 = "Turn left, ";
                String s2 = "  ";
                if(l1 < -10)
                    s2 = "nose down";
                if(l1 >= -10 && l1 <= -5)
                    s2 = "down a bit";
                if(l1 > -5 && l1 < 5)
                    s2 = "level";
                if(l1 <= 10 && l1 >= 5)
                    s2 = "up a bit";
                if(l1 > 10)
                    s2 = "pull up";
                String s3 = "  ";
                if(k2 < 5)
                    s3 = "dead ahead, ";
                if(k2 >= 5 && (double)k2 <= 7.5D)
                    s3 = "right by 5\260, ";
                if((double)k2 > 7.5D && (double)k2 <= 12.5D)
                    s3 = "right by 10\260, ";
                if((double)k2 > 12.5D && (double)k2 <= 17.5D)
                    s3 = "right by 15\260, ";
                if((double)k2 > 17.5D && k2 <= 25)
                    s3 = "right by 20\260, ";
                if(k2 > 25 && k2 <= 35)
                    s3 = "right by 30\260, ";
                if(k2 > 35 && k2 <= 45)
                    s3 = "right by 40\260, ";
                if(k2 > 45 && k2 <= 60)
                    s3 = "off our right, ";
                if(k2 > 355)
                    s3 = "dead ahead, ";
                if(k2 <= 355 && (double)k2 >= 352.5D)
                    s3 = "left by 5\260, ";
                if((double)k2 < 352.5D && (double)k2 >= 347.5D)
                    s3 = "left by 10\260, ";
                if((double)k2 < 347.5D && (double)k2 >= 342.5D)
                    s3 = "left by 15\260, ";
                if((double)k2 < 342.5D && k2 >= 335)
                    s3 = "left by 20\260, ";
                if(k2 < 335 && k2 >= 325)
                    s3 = "left by 30\260, ";
                if(k2 < 325 && k2 >= 315)
                    s3 = "left by 40\260, ";
                if(k2 < 345 && k2 >= 300)
                    s3 = "off our left, ";
                String classnameFull = actor.getClass().getName();
                int idot = classnameFull.lastIndexOf('.');
                int idol = classnameFull.lastIndexOf('$');
                if (idot < idol) idot = idol;
                String classnameSection = classnameFull.substring(idot + 1);
                for(int num = 0; num < irstListSize; num++)
                {
                    if((double)j1 <= (double)l2 && (double)j1 < irstMaxDistance[num] && l1 >= -20 && l1 <= 20 && Math.sqrt(j2 * j2) <= 60D && (classnameSection.indexOf(irstPlaneName[num])) !=-1)
                    {
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "TV Tracking! " + irstPlaneDisplay[num] + " " + s3 + s + ", " + i2 + "m");
                        freq = 1;
                        setTimer(freq);
                        return true;
                    }
                }
            }
        }

        // IR sensor part, when TV camera has no result.
        for(java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
        {
            Actor actor = (Actor)entry.getValue();
            if(flag1 && (actor instanceof Aircraft) && actor.getArmy() != World.getPlayerArmy() && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D
               && !(Main.cur().clouds != null && Main.cur().clouds.getVisibility(actor.pos.getAbsPoint(), aircraft.pos.getAbsPoint()) < 1.0F))
            {
                super.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (actor.pos.getAbsPoint())).x;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).y;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).z;
                double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                String s = "level with us";
                if(d2 - d6 - 300D >= 0.0D)
                    s = "below us";
                if((d2 - d6) + 300D <= 0.0D)
                    s = "above us";
                if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                    s = "slightly below";
                if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                    s = "slightly above";
                double d8 = d4 - d;
                double d9 = d5 - d1;
                float f = 57.32484F * (float)Math.atan2(d9, -d8);
                int k = (int)(Math.floor((int)f) - 90D);
                if(k < 0)
                    k += 360;
                int l = k - i;
                double d10 = d - d4;
                double d11 = d1 - d5;
                Random random = new Random();
                float f1 = ((float)random.nextInt(20) - 10F) / 100F + 1.0F;
                int i1 = random.nextInt(6) - 3;
                float f2 = 100000F;
                float f3 = f2;
                if(d3 < 15D)
                    f3 = (float)(d3 * 0.80000001192092896D * 3D);
                int j1 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f1) / 10D) * 10D);
                if((float)j1 > f2)
                    j1 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
                float f4 = 57.32484F * (float)Math.atan2(j1, d7);
                int k1 = (int)(Math.floor((int)f4) - 90D);
                int l1 = (k1 - (90 - j)) + i1;
                int i2 = (int)f2;
                if((float)j1 < f2)
                    if(j1 > 1150)
                        i2 = (int)(Math.ceil((double)j1 / 900D) * 900D);
                    else
                        i2 = (int)(Math.ceil((double)j1 / 500D) * 500D);
                int j2 = l + i1;
                int k2 = j2;
                if(k2 < 0)
                    k2 += 360;
                float f5 = (float)((double)f3 + Math.sin(Math.toRadians(Math.sqrt(l * l) * 3D)) * ((double)f3 * 0.25D));
                int l2 = (int)((double)f5 * Math.cos(Math.toRadians(l1)));
                String s1 = "  ";
                if(k2 < 5)
                    s1 = "Dead ahead, ";
                if(k2 >= 5 && (double)k2 <= 7.5D)
                    s1 = "Right 5, ";
                if((double)k2 > 7.5D && (double)k2 <= 12.5D)
                    s1 = "Right 10, ";
                if((double)k2 > 12.5D && (double)k2 <= 17.5D)
                    s1 = "Right 15, ";
                if((double)k2 > 17.5D && k2 <= 25)
                    s1 = "Right 20, ";
                if(k2 > 25 && k2 <= 35)
                    s1 = "Right 30, ";
                if(k2 > 35 && k2 <= 45)
                    s1 = "Right 40, ";
                if(k2 > 45 && k2 <= 60)
                    s1 = "Turn right, ";
                if(k2 > 355)
                    s1 = "Dead ahead, ";
                if(k2 <= 355 && (double)k2 >= 352.5D)
                    s1 = "Left 5, ";
                if((double)k2 < 352.5D && (double)k2 >= 347.5D)
                    s1 = "Left 10, ";
                if((double)k2 < 347.5D && (double)k2 >= 342.5D)
                    s1 = "Left 15, ";
                if((double)k2 < 342.5D && k2 >= 335)
                    s1 = "Left 20, ";
                if(k2 < 335 && k2 >= 325)
                    s1 = "Left 30, ";
                if(k2 < 325 && k2 >= 315)
                    s1 = "Left 40, ";
                if(k2 < 345 && k2 >= 300)
                    s1 = "Turn left, ";
                String s2 = "  ";
                if(l1 < -10)
                    s2 = "nose down";
                if(l1 >= -10 && l1 <= -5)
                    s2 = "down a bit";
                if(l1 > -5 && l1 < 5)
                    s2 = "level";
                if(l1 <= 10 && l1 >= 5)
                    s2 = "up a bit";
                if(l1 > 10)
                    s2 = "pull up";
                String s3 = "  ";
                if(k2 < 5)
                    s3 = "dead ahead, ";
                if(k2 >= 5 && (double)k2 <= 7.5D)
                    s3 = "right by 5\260, ";
                if((double)k2 > 7.5D && (double)k2 <= 12.5D)
                    s3 = "right by 10\260, ";
                if((double)k2 > 12.5D && (double)k2 <= 17.5D)
                    s3 = "right by 15\260, ";
                if((double)k2 > 17.5D && k2 <= 25)
                    s3 = "right by 20\260, ";
                if(k2 > 25 && k2 <= 35)
                    s3 = "right by 30\260, ";
                if(k2 > 35 && k2 <= 45)
                    s3 = "right by 40\260, ";
                if(k2 > 45 && k2 <= 60)
                    s3 = "off our right, ";
                if(k2 > 355)
                    s3 = "dead ahead, ";
                if(k2 <= 355 && (double)k2 >= 352.5D)
                    s3 = "left by 5\260, ";
                if((double)k2 < 352.5D && (double)k2 >= 347.5D)
                    s3 = "left by 10\260, ";
                if((double)k2 < 347.5D && (double)k2 >= 342.5D)
                    s3 = "left by 15\260, ";
                if((double)k2 < 342.5D && k2 >= 335)
                    s3 = "left by 20\260, ";
                if(k2 < 335 && k2 >= 325)
                    s3 = "left by 30\260, ";
                if(k2 < 325 && k2 >= 315)
                    s3 = "left by 40\260, ";
                if(k2 < 345 && k2 >= 300)
                    s3 = "off our left, ";
                double IRstrength = 0D;
                EnginesInterface ei = ((Aircraft)actor).FM.EI;
                for(int en = 0; en < ei.engines.length; en++)
                {
                    float add = 0;
                    if(ei.engines[en].getType() == 2 || ei.engines[en].getType() == 3)
                    {   // get IR strength from Jet exhaust from thrust and throttle values
                        add = ei.engines[en].thrustMax;
                        if(ei.engines[en].getPowerOutput() > 1.0F)
                            add *= (ei.engines[en].getPowerOutput() * 1.22F);
                        else
                            add *= (ei.engines[en].getPowerOutput() * 0.8F + 0.2F);
                    }
                    else
                        add = Reflection.getFloat(ei.engines[en], "horsePowers") * (ei.engines[en].getPowerOutput() * 0.9F + 0.1F) * 0.36F;

                    IRstrength += (double)add;
                }
                if(ei.engines.length > 4)
                    IRstrength *= (2.0D / (double)ei.engines.length);
                else
                    IRstrength /= Math.sqrt((double)ei.engines.length);
                IRstrength *= 1.8D;   // approximation distance in meters

                if((double)j1 <= (double)l2 && l1 >= -20 && l1 <= 20 && Math.sqrt(j2 * j2) <= 60D
                   && (double)j1 < IRstrength)
                {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "IR Tracking! " + s3 + s);
                    freq = 1;
                    setTimer(freq);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean InertialNavigation()
    {
        Point3d point3d = new Point3d();
        pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if(aircraft.getSpeed(vector3d) > 20D && ((Actor) (aircraft)).pos.getAbsPoint().z >= 150D && (aircraft instanceof F_14D))
        {
            pos.getAbs(point3d);
            if(Mission.cur() != null)
            {
                error++;
                if(error > 99)
                    error = 1;
            }
            int i = error;
            int j = i;
            Random random = new Random();
            int k = random.nextInt(100);
            if(k > 50)
                i -= i * 2;
            k = random.nextInt(100);
            if(k > 50)
                j -= j * 2;
            double d = Main3D.cur3D().land2D.mapSizeX() / 1000D;
            double d1 = ((Main3D.cur3D().land2D.worldOfsX() + ((Actor) (aircraft)).pos.getAbsPoint().x) / 1000D) / 10D;
            double d2 = ((Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft)).pos.getAbsPoint().y) / 1000D) / 10D;
            char c = (char)(int)(65D + Math.floor((d1 / 676D - Math.floor(d1 / 676D)) * 26D));
            char c1 = (char)(int)(65D + Math.floor((d1 / 26D - Math.floor(d1 / 26D)) * 26D));
            String s = "";
            if(d > 260D)
                s = "" + c + c1;
            else
                s = "" + c1;
            int l = (int)Math.ceil(d2);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "GPS: " + s + "-" + l);
        }
        return true;
    }

    public void setTimer(int i)
    {
        Random random = new Random();
        Timer1 = (float)((double)random.nextInt(i) * 0.10000000000000001D);
        Timer2 = (float)((double)random.nextInt(i) * 0.10000000000000001D);
    }

    public void resetTimer(float f)
    {
        Timer1 = f;
        Timer2 = f;
    }

    public void missionStarting()
    {
        super.missionStarting();
        checkChangeWeaponColors();
    }

    private void checkChangeWeaponColors()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
        {
            if(FM.CT.Weapons[i] == null)
                continue;
            for(int j = 0; j < FM.CT.Weapons[i].length; j++)
            {
                if(FM.CT.Weapons[i][j] instanceof Pylon_AN_ALQ167_gn16)
                    ((Pylon_AN_ALQ167_gn16)FM.CT.Weapons[i][j]).matGray();
            }
        }
    }


    public void computeF110GE400_AB()
    {
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() > 5)
            FM.producedAF.x += 40600D;
        if(FM.EI.engines[1].getThrustOutput() > 1.001F && FM.EI.engines[1].getStage() > 5)
            FM.producedAF.x += 40600D;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() > 5 && calculateMach() > 0.85F)
            FM.producedAF.x += 12000D;
        if(FM.EI.engines[1].getThrustOutput() > 1.001F && FM.EI.engines[1].getStage() > 5 && calculateMach() > 0.85F)
            FM.producedAF.x += 12000D;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() > 5 && calculateMach() > 1.3F)
            FM.producedAF.x -= 34000D;
        if(FM.EI.engines[1].getThrustOutput() > 1.001F && FM.EI.engines[1].getStage() > 5 && calculateMach() > 1.3F)
            FM.producedAF.x -= 34000D;
        float x = FM.getAltitude() / 1000F;
        float thrustDegradation = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6
         && FM.EI.engines[1].getThrustOutput() > 1.001F && FM.EI.engines[1].getStage() == 6)
            if (x > 19.0F)
            {
                thrustDegradation = 20.0F;
            }
            else
            {
                float x2 = x * x;
                float x3 = x2 * x;
                float x4 = x3 * x;
                float x5 = x4 * x;
                float x6 = x5 * x;
                float x7 = x6 * x;
                thrustDegradation = -(17077F*x6/142443000F) + (13553F*x5/2374050F) - (13203787F*x4/142443000F) + (434033F*x3/698250F) - (71389321F*x2/35610750F) + (1247081F*x/339150F);
        //{{0,0},{2,3},{5,4},{7,0},{12,-30},{17,7},{19,25}}
        //thrustDegradation = (3401F*x4/720720F) - (44399F*x3/360360F) + (633091F*x2/720720F) - (829459F*x/360360F);
        //{{0,0},{2,-2},{5,-2},{7,-4},{18,20}}
            }
        FM.producedAF.x -=  thrustDegradation * 1000F;
    }


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
    protected SoundFX engineSFX;
    protected int engineSTimer;
    private NetMsgFiltered outCommand;
    private long tX4Prev;
    private float deltaAzimuth;
    private float deltaTangage;
    public float Timer1;
    public float Timer2;
    private int freq;
    private int counter;
    private int error;
    private long raretimer;
    private ArrayList counterFlareList;
    private ArrayList counterChaffList;

    private static String irstPlaneName[] =
    {
        "AD" ,     "BirdDog", "A_6" ,    "A_7A" ,  "A_7B" ,    "A_7D" ,  "A_7E" ,   "A_10" ,  "A_26" ,  "AV_8" ,
        "B_29" ,   "C_123" ,  "F_4" ,    "F_5" ,   "F_8E" ,    "F9F" ,   "F_14" ,   "F_15" ,  "F_16" ,  "F_18" ,
        "F84" ,    "F_86" ,   "F_100" ,  "F_104" , "F_105" ,   "Hunter", "IL_28" ,  "KC_10",  "L_39" ,  "Meteor" ,
        "Mig_15" , "Mig_17",  "Mig_19",  "MIG_21", "MIG_23" ,  "MIG_29", "MIRAGE",  "Mi24" ,  "MI8MT" , "P_80" ,
        "Skyhawk", "Su_7B" ,  "Su_9B" , "Su_11_8M", "Su_25" ,  "Su_27" , "Tucano",  "TU_95",  "Yak_36", "Vampire" ,
        "F_80" ,   "T_33" ,   "A1H" ,    "A1J" ,  "CAC_Sabre", "FJ_3M" , "P2V" ,    "OV_" ,   "TU_4" ,  "SM_12" ,
        "MIG21" ,  "L_159" ,  "SeaHawk", "Canberra", "F_102",  "F_106",  "Tu_22",   "Tu_26",  "T_37",   "A_3",
        "A_5",     "B_1",     "B_52",    "B_36",   "B_45",     "B_47",   "B_58",    "C_119",  "EE_Lightning", "Tornado",
        "C_121",   "C_130",   "C_141",   "C_17",   "C_5",      "F2H",    "F3H",     "F4D",    "F3D",    "F11F",
        "F_22",    "F_35",    "Su_50",   "F_89",   "F_101",    "F_93",   "F_111",   "P_3",    "S_3",    "E_2",
        "IL_76",   "An_12",   "An_22",   "An_124", "An_225",   "747",    "707",     "G_222",  "G_91",   "MB_326",
        "Yak_28",  "Tu_160",  "Su_24",   "M_4",    "Typhoon",  "Rafale", "Gripen", "MIRAGE_F1", "MIRAGE_2000", "Jaguar",
        "Su_7U",  "Su_7BMK", "Su_7BKL",  "Su_17M", "Su_17M2", "Su_17M3", "Su_17M4", "Su_17UM", "Su_20", "Su_22",
        "Su_22M", "Su_22M3", "Su_22M4",  "Su_22U", "F_8A",     "RF_8A",  "A_7",    "MB_326X", "G_91R",  "G_91Y"
    };
    private static String irstPlaneDisplay[] =
    {
        "Skyraider", "BirdDog", "A-6",       "A-7",       "A-7",    "A-7",    "A-7",     "A-10",         "A-26",   "Harrier",
        "B-29",      "C-123",   "F-4",       "F-5",       "F-8",    "F9F",    "F-14",    "F-15",         "F-16",   "F-18",
        "F-84",      "F-86",    "F-100",     "F-104",     "F-105",  "Hunter", "IL-28",   "KC-10",        "L-39",   "Meteor",
        "MiG-15",    "MiG-17",  "MiG-19",    "MiG-21",    "MiG-23", "MiG-29", "Mirage",  "Mi-24",        "Mi-8",   "F-80",
        "Skyhawk",   "Su-7",    "Fishpot",   "Fishpot",   "Su-25",  "Su-27",  "Tucano",  "Tupolev Bear", "Yak-36", "Vampire" ,
        "F-80" ,     "T-33" ,   "Skyraider", "Skyraider", "F-86" ,  "F-86" ,  "Neptune", "OV-1/10",      "Tu-4" ,  "MiG-19" ,
        "MiG-21" ,   "L-39" ,   "SeaHawk" ,  "Canberra",  "F-102",  "F-106",  "Tu-22",   "Tu-26",        "T-37",   "A-3",
        "A-5",       "B-1",     "B-52",      "B-36",      "B-45",   "B-47",   "B-58",    "C-119",    "EE-Lightning", "Tornado",
        "C-121",     "C-130",   "C-141",     "C-17",      "C-5",    "F2H",    "F3H",     "F4D",          "F3D",    "F11F",
        "F-22",      "F-35",    "Su-50",     "F-89",      "F-101",  "F-93",   "F-111",   "P-3",          "S-3",    "E-2",
        "IL-76",     "An-12",   "An-22",     "An-124",    "An-225", "747",    "707",     "G-222",        "G-91",   "MB-326",
        "Yak-28",    "Tu-160",  "Su-24",     "Bison",    "Typhoon","Rafale", "Gripen", "MIRAGE-F1", "MIRAGE-2000", "Jaguar",
        "Su-7",      "Su-7",    "Su-7",      "Su-17",     "Su-17",  "Su-17",  "Su-17",   "Su-17",        "Su-17",  "Su-17", 
        "Su-17",     "Su-17",   "Su-17",     "Su-17",     "F-8",    "F-8",    "A-7",     "MB-326",       "G-91",   "G-91"
    };
    private static double irstMaxDistance[] =
    {
        60000D , 40000D , 70000D , 60000D , 60000D , 60000D , 60000D , 60000D , 80000D , 60000D ,
       100000D ,110000D , 75000D , 55000D , 55000D , 50000D , 75000D , 75000D , 55000D , 65000D ,
        50000D , 50000D , 60000D , 50000D , 80000D , 55000D , 90000D ,110000D , 50000D , 55000D ,
        50000D , 50000D , 55000D , 55000D , 65000D , 70000D , 60000D , 65000D , 65000D , 50000D ,
        50000D , 75000D , 75000D , 75000D , 70000D , 75000D , 40000D ,110000D , 50000D , 50000D ,
        50000D , 50000D , 60000D , 60000D , 50000D , 50000D ,100000D , 60000D ,100000D , 55000D ,
        55000D , 50000D , 50000D , 60000D , 70000D , 70000D , 90000D , 90000D , 40000D , 80000D ,
        75000D , 90000D ,120000D ,130000D ,100000D ,100000D ,100000D ,100000D , 60000D , 65000D ,
       110000D ,100000D ,130000D ,130000D ,150000D , 60000D , 60000D , 60000D , 60000D , 60000D ,
        50000D , 50000D , 55000D , 70000D , 70000D , 60000D , 85000D , 90000D , 60000D , 90000D ,
       100000D , 85000D ,130000D ,150000D ,150000D ,150000D ,105000D , 90000D , 55000D , 50000D ,
        80000D ,110000D , 85000D ,110000D , 55000D , 55000D , 55000D , 60000D , 60000D , 60000D ,
        75000D , 75000D , 75000D , 75000D , 75000D , 75000D , 75000D , 75000D , 75000D , 75000D ,
        75000D , 75000D , 75000D , 75000D , 55000D , 55000D , 55000D , 50000D , 55000D , 55000D
    };

    static
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-14D");
        Property.set(class1, "meshName", "3DO/Plane/F-14/F14D.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/F-14B.fmd:F14_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitF_14D.class, com.maddox.il2.objects.air.CockpitF_14FLIR.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 9, 9, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 3, 3, 3, 3, 9, 9, 7, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
           "_CANNON01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_Rock05", "_Rock06", "_Rock07", "_Rock08","_ExternalRock09",
           "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalDev01", "_ExternalDev02","_ExternalRock17",
           "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27",
           "_ExternalRock28", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev03", "_ExternalDev04", "_ExternalRock29", "_ExternalRock30"

        });
        String s = "";
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            char c = '\50';
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[c];
            s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM9D";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7E";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7M";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7E+4xAIM9D";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7M+4xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM9D+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7E+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7M+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7E+4xAIM9D+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7E_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9D_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM7M+4xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54A";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54A+4xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54A+2xAIM7M+2xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54A+4xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54A+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54A";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54A+4xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54A+2xAIM7M+2xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54A+4xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54A+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_6xAIM54A";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_6xAIM54A+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54C";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54C+4xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54C+2xAIM7M+2xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54C+4xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2xAIM54C+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54C";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54C+4xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54C+2xAIM7M+2xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54C+4xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_4xAIM54C+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_6xAIM54C";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_6xAIM54C+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2xMk83+3xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2xMk83Ballute+3xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2xMk84+3xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2xMk84Ballute+3xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2xCBU100+3xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4xMk83+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4xMk83Ballute+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4xMk84+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4xMk84Ballute+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4xCBU100+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU12+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU16+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU10+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU12+1xAIM54A+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU16+1xAIM54A+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU10+1xAIM54A+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU12+1xAIM54C+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU16+1xAIM54C+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU10+1xAIM54C+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_ALQ167+1xGBU12+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_ALQ167+1xGBU16+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_ALQ167+1xGBU10+2xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_ALQ167+1xGBU12+1xAIM54A+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_ALQ167+1xGBU16+1xAIM54A+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_ALQ167+1xGBU10+1xAIM54A+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_ALQ167+1xGBU12+1xAIM54C+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_ALQ167+1xGBU16+1xAIM54C+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_ALQ167+1xGBU10+1xAIM54C+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU38JDAM+1xAIM54A+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU32JDAM+1xAIM54A+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_2xGBU31JDAM+1xAIM54A+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_4xGBU38JDAM+1xAIM54A+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_4xGBU32JDAM+1xAIM54A+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttackFLIR_4xGBU31JDAM+1xAIM54A+1xAIM7M+2xAIM9L+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ14_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Recon_2xAIM7M+4xAIM9L+2xDt+TARPS";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "Pylon_TARPS_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Recon_2xAIM54A+4xAIM9L+2xDt+TARPS";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "Pylon_TARPS_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Recon_2xAIM54A+2xAIM7M+2xAIM9L+2xDt+TARPS";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54A_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "Pylon_TARPS_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Recon_2xAIM54C+4xAIM9L+2xDt+TARPS";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "Pylon_TARPS_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Recon_2xAIM54C+2xAIM7M+2xAIM9L+2xDt+TARPS";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAIM54C_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "Pylon_TARPS_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Recon_ALQ167+4xAIM9L+2xDt+TARPS";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "Pylon_TARPS_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Recon_ALQ167+2xAIM7M+2xAIM9L+2xDt+TARPS";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61_A1", 650);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF14R_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(9, "Pylon_TARPS_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - F_14D : " + s);
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
