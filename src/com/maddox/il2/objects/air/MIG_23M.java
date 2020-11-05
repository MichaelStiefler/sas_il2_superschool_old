// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 09.08.2019 20:20:53
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MIG_23M.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapExt;
import com.maddox.util.HashMapInt;

import java.util.*;

// Referenced classes of package com.maddox.il2.objects.air:
//            MIG_23, Aircraft, TypeFighterAceMaker, TypeSupersonic, 
//            TypeFastJet, PaintSchemeFMPar05, TypeLaserSpotter, NetAircraft

public class MIG_23M extends MIG_23
    implements TypeLaserSpotter, TypeRadar
{

    public MIG_23M()
    {
        counter = 0;
        freq = 800;
        Timer1 = Timer2 = freq;
        fxSPO10 = newSound("aircraft.Sirena2", false);
        smplSPO10 = new Sample("sample.Sirena2.wav", 256, 65535);
        SPO10SoundPlaying = false;
        smplSPO10.setInfinite(true);
        super.TP_23_model = 30f;
        super.ECCM_power = 70000;
    }

    public boolean typeRadarToggleMode()
    {
    	/*
        //long l = Time.current();
        if(radarmode == 1)
            radarmode++;
        if(radarmode == 0)// && l > twait + 5000L)
        {
            radarmode++;
            //twait = l;
        }
        if(radarmode > 1)
            radarmode = 0;
            */
    	super.radarmode ++;
    	switch(super.radarmode)
    	{
    	case 0:
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "HUD: Navigation");
    		break;
    	case 1:
    		if(!super.air_ground)
    		{
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "HUD: R");
    		super.radarmode = 11;
            break;
            }
    		else
    		{
        		HUD.log(AircraftHotKeys.hudLogWeaponId, "HUD: AG_B");
        		super.radarmode = 1001;
                break;
    		}
    	case 12:
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "HUD: T");
            break;
    	case 13:
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "HUD: Phi0");
    		break;
    	case 14:
    		super.radarmode = 0;
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "HUD: Navigation");
    		break;

    	case 101:
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "HUD: A+RT");
    		break;
    	case 102:
    		super.radarmode = 100;
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "HUD: A+R");
    		break;
    		
    	case 201:
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "HUD: A+T");
    		break;
    	case 202:
			super.victim_RP = super.victim_TP;
    		super.radarmode = 101;
			super.victim_TP = null;
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "HUD: A+TR");
    		break;
    	case 1002:
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "HUD: AG_S-5");
    		break;
    	case 1003:
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "HUD: AG_G");
    		break;
    	case 1004:
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "HUD: AG_S-24");
    		break;
    	case 1005:
    		HUD.log(AircraftHotKeys.hudLogWeaponId, "HUD: NAV");
    		super.radarmode = 0;
    		break;
    	}

		super.rangeGate_range = 0;
		super.rangeGate_azimuth = 0;
		super.Myotka_x = 0;
		super.Myotka_y = 0;
        return false;
    }
    
    


    /*public boolean typeFighterAceMakerToggleAutomation()
    {
        super.k14Mode++;
        if(super.k14Mode > 7)
            super.k14Mode = 0;
        if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft()){}
            
        
        return true;
    }*/

    
    
    public void rareAction(float f, boolean flag)
    {
        if(raretimer != Time.current() && this == World.getPlayerAircraft())
        {
            if(counter++ % 5 == 0)
                SAPFIR23();
            if(counter++ % 12 == 3)
                IRST();
        }
        super.rareAction(f, flag);
        raretimer = Time.current();
    }
    
    public void typeRadarRangeMinus()
    {
    }

    public void typeRadarRangePlus()
    {
    }


    private boolean SAPFIR23()
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
        if(aircraft instanceof MIG_23M)
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
                new String();
                new String();
                double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                Engine.land();
                String s = "level with us";
                if(d2 - d6 - 300D >= 0.0D)
                    s = "below us";
                if((d2 - d6) + 300D <= 0.0D)
                    s = "above us";
                if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                    s = "slightly below";
                if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                    s = "slightly above";
                new String();
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
                float f2 = 43000F;
                float f3 = f2;
                if(d3 < 1200D)
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
                if((double)j1 <= (double)l2 && (double)j1 >= 100D && (double)j1 >= 700D && l1 >= -20 && l1 <= 20 && Math.sqrt(j2 * j2) <= 40D)
                {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Contact " + s1 + s2 + ", " + i2 + "m");
                    freq = 3;
                } else
                {
                    freq = 7;
                }
                setTimer(freq);
            }
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

    private boolean IRST()
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
        if(aircraft instanceof MIG_23M)
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
            if(flag1 && (actor instanceof Aircraft) && actor.getArmy() != World.getPlayerArmy() && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D && (Main.cur().clouds == null || Main.cur().clouds.getVisibility(actor.pos.getAbsPoint(), aircraft.pos.getAbsPoint()) >= 1.0F))
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
                float f2 = 35000F;
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
                double d12 = 0.0D;
                EnginesInterface enginesinterface = ((Aircraft)actor).FM.EI;
                for(int i3 = 0; i3 < enginesinterface.engines.length; i3++)
                {
                    float f6 = 0.0F;
                    if(enginesinterface.engines[i3].getType() == 2 || enginesinterface.engines[i3].getType() == 3)
                    {
                        f6 = enginesinterface.engines[i3].thrustMax;
                        if(enginesinterface.engines[i3].getPowerOutput() > 1.0F)
                            f6 *= enginesinterface.engines[i3].getPowerOutput() * 1.22F;
                        else
                            f6 *= enginesinterface.engines[i3].getPowerOutput() * 0.8F + 0.2F;
                    } else
                    {
                        f6 = Reflection.getFloat(enginesinterface.engines[i3], "horsePowers") * (enginesinterface.engines[i3].getPowerOutput() * 0.9F + 0.1F) * 0.36F;
                    }
                    d12 += f6;
                }

                if(enginesinterface.engines.length > 4)
                    d12 *= 2D / (double)enginesinterface.engines.length;
                else
                    d12 /= Math.sqrt(enginesinterface.engines.length);
                d12 *= 1.5D;
                if((double)j1 <= (double)l2 && l1 >= -20 && l1 <= 20 && Math.sqrt(j2 * j2) <= 60D && (double)j1 < d12)
                {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "IR Tracking! " + s3 + s);
                    freq = 1;
                    setTimer(freq);
                    return true;
                }
            }
        }

        return true;
    }

    private boolean sirenaWarning(float f)
    {
        boolean flag = false;
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = War.getNearestEnemy(this, 6000F);
        if(Actor.isValid(aircraft))
        {
            double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
            double d3 = d2 - (double)Landscape.Hmin((float)((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x, (float)((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i += 360;
            int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j += 360;
            Aircraft aircraft1 = War.getNearestEnemy(aircraft, f);
            if(Actor.isValid(aircraft1) && (aircraft1 instanceof Aircraft) && aircraft.getArmy() != World.getPlayerArmy() && (aircraft instanceof TypeFighterAceMaker) && ((aircraft instanceof TypeSupersonic) || (aircraft instanceof TypeFastJet)) && aircraft1 == World.getPlayerAircraft() && aircraft1.getSpeed(vector3d) > 20D)
            {
                super.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y;
                double d8 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z;
                new String();
                new String();
                int k = (int)(Math.floor(((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z * 0.10000000000000001D) * 10D);
                int l = (int)(Math.floor((aircraft1.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                double d10 = (int)(Math.ceil((d2 - d8) / 10D) * 10D);
                boolean flag1 = false;
                Engine.land();
                int j1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x), Engine.land().WORLD2PIXY(((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y));
                float f1 = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                if(j1 >= 28 && j1 < 32 && f1 < 7.5F)
                    flag1 = true;
                new String();
                double d14 = d4 - d;
                double d16 = d6 - d1;
                float f2 = 57.32484F * (float)Math.atan2(d16, -d14);
                int k1 = (int)(Math.floor((int)f2) - 90D);
                if(k1 < 0)
                    k1 += 360;
                int l1 = k1 - i;
                double d19 = d - d4;
                double d20 = d1 - d6;
                Random random = new Random();
                float f4 = ((float)random.nextInt(20) - 10F) / 100F + 1.0F;
                int l2 = random.nextInt(6) - 3;
                float f5 = 19000F;
                float f6 = f5;
                if(d3 < 1200D)
                    f6 = (float)(d3 * 0.80000000000000004D * 3D);
                int i3 = (int)(Math.ceil(Math.sqrt((d20 * d20 + d19 * d19) * (double)f4) / 10D) * 10D);
                if((float)i3 > f5)
                    i3 = (int)(Math.ceil(Math.sqrt(d20 * d20 + d19 * d19) / 10D) * 10D);
                float f7 = 57.32484F * (float)Math.atan2(i3, d10);
                int j3 = (int)(Math.floor((int)f7) - 90D);
                int k3 = (j3 - (90 - j)) + l2;
                int l3 = (int)f5;
                if((float)i3 < f5)
                    if(i3 > 1150)
                        l3 = (int)(Math.ceil((double)i3 / 900D) * 900D);
                    else
                        l3 = (int)(Math.ceil((double)i3 / 500D) * 500D);
                int i4 = l1 + l2;
                int j4 = i4;
                if(j4 < 0)
                    j4 += 360;
                float f8 = (float)((double)f6 + Math.sin(Math.toRadians(Math.sqrt(l1 * l1) * 3D)) * ((double)f6 * 0.25D));
                int k4 = (int)((double)f8 * Math.cos(Math.toRadians(k3)));
                if((double)i3 <= (double)k4 && (double)i3 <= 14000D && (double)i3 >= 200D && k3 >= -30 && k3 <= 30 && Math.sqrt(i4 * i4) <= 60D)
                    flag = true;
                else
                    flag = false;
            }
            Aircraft aircraft2 = World.getPlayerAircraft();
            if(Actor.isValid(aircraft2))
            {
                double d5 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x;
                double d7 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y;
                double d9 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z;
                int i1 = (int)(-((double)((Actor) (aircraft2)).pos.getAbsOrient().getYaw() - 90D));
                if(i1 < 0)
                    i1 += 360;
                if(flag && aircraft1 == World.getPlayerAircraft())
                {
                    super.pos.getAbs(point3d);
                    double d11 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
                    double d12 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
                    double d13 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
                    double d15 = (int)(Math.ceil((d9 - d13) / 10D) * 10D);
                    String s = "";
                    if(d9 - d13 - 500D >= 0.0D)
                        s = " low";
                    if((d9 - d13) + 500D < 0.0D)
                        s = " high";
                    new String();
                    double d17 = d11 - d5;
                    double d18 = d12 - d7;
                    float f3 = 57.32484F * (float)Math.atan2(d18, -d17);
                    int i2 = (int)(Math.floor((int)f3) - 90D);
                    if(i2 < 0)
                        i2 += 360;
                    int j2 = i2 - i1;
                    if(j2 < 0)
                        j2 += 360;
                    int k2 = (int)(Math.ceil((double)(j2 + 15) / 30D) - 1.0D);
                    if(k2 < 1)
                        k2 = 12;
                    double d21 = d5 - d11;
                    double d22 = d7 - d12;
                    double d23 = Math.ceil(Math.sqrt(d22 * d22 + d21 * d21) / 10D) * 10D;
                    bRadarWarning = d23 <= 8000D && d23 >= 500D && Math.sqrt(d15 * d15) <= 5000D;
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO: Spike at " + k2 + " o'clock" + s + "!");
                    playSirenaWarning(bRadarWarning);
                } else
                {
                    bRadarWarning = false;
                    playSirenaWarning(bRadarWarning);
                }
            }
        }
        return true;
    }

    public void playSirenaWarning(boolean flag)
    {
        if(this != World.getPlayerAircraft())
            return;
        if(flag && !sirenaSoundPlaying)
        {
            fxSPO10.play(smplSirena);
            sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO: Enemy on Six!");
        } else
        if(!flag && sirenaSoundPlaying)
        {
            fxSPO10.cancel();
            sirenaSoundPlaying = false;
        }
    }

    public void update(float f)
    {
        super.update(f);
        computeR29F_300_AB();
        
        
        if(super.radarmode == 11 || (!super.FM.isPlayers() && !super.FM.Gears.onGround() ))
        {
        	
        	if(isValid((Actor)this) && isAlive((Actor)this))
        	{
        		if (!super.FM.isPlayers() && super.getGuidedMissileUtils().getMissileTarget() != null)
				{
        			Actor targeted = super.getGuidedMissileUtils().getMissileTarget();
        			if(targeted instanceof TypeRadarWarningReceiver)
					((TypeRadarWarningReceiver) super.getGuidedMissileUtils().getMissileTarget()).myRadarLockYou(this, "aircraft.APR25AAA");
				}
					
        		Point3d point3d = ((Actor) (this)).pos.getAbsPoint();
                Orient orient = ((Actor) (this)).pos.getAbsOrient();
                List list = Engine.targets();
                int j = list.size();
                for(int k = 0; k < j; k++)
                {
                	Actor actor = (Actor)list.get(k);
                	if(actor instanceof TypeRadarWarningReceiver)
                	{
                		Vector3d vector3d = new Vector3d();
                        vector3d.set(point3d);
                        Point3d point3d1 = new Point3d();
                        point3d1.set(actor.pos.getAbsPoint());
                        point3d1.sub(point3d);
                        orient.transformInv(point3d1);
                        if(point3d1.x < 100000&& point3d1.x > 0)
                        {
                        	if(GuidedMissileUtils.angleBetween(this, actor)<35F)
                        	{
                        		
                        		((TypeRadarWarningReceiver) actor).myRadarSearchYou(this, "aircraft.APR25AAA");
                        	}
                        }
                	}
                }
        	}
        }
        	
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        return "MiG23M_";
    }

    public void computeR29F_300_AB()
    {
        if(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 41202D;
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6)
            if((double)f > 19.5D)
            {
                f1 = 16F;
            } else
            {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = ((0.00194398F * f4 - 0.0506077F * f3) + 0.328793F * f2) - 0.761769F * f;
            }
        ((FlightModelMain) (super.FM)).producedAF.x -= f1 * 1000F;
    }



    private long raretimer;
    private int radarmode;
    public float Timer1;
    public float Timer2;
    private int freq;
    private int counter;
    private SoundFX fxSirenaLaunch;
    private Sample smplSirenaLaunch;
    private boolean sirenaLaunchSoundPlaying;
    private boolean bRadarWarningLaunch;
    private SoundFX fxSirena;
    private Sample smplSirena;
    private boolean sirenaSoundPlaying;
    private SoundFX fxSPO10;
    private Sample smplSPO10;
    private boolean SPO10SoundPlaying;
    private boolean bRadarWarning;
    private long twait;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.MIG_23M.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-23");
        Property.set(class1, "meshName", "3DO/Plane/MiG-23/hierMiG23M.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1972F);
        Property.set(class1, "yearExpired", 2020F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-23M.fmd:MIG23FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitMIG_23ML.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 2, 2, 2, 2, 2, 2, 2, 2, 9, 
            2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 
            9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 
            3, 3, 9, 9, 1, 1, 1, 1, 9, 9, 
            9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 
            9, 9, 3, 3, 3, 3, 9, 9, 2, 2, 
            2, 2, 7, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_Gun01", "_Rock01", "_Rock02", "_Rock03", "_Rock04", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_ExternalDev01", 
            "_Rock09", "_Rock10", "_Rock11", "_Rock12", "_Rock13", "_Rock14", "_Rock15", "_Rock16", "_Dev02", "_Dev03", 
            "_Dev04", "_Dev05", "_Dev06", "_Dev07", "_Dev08", "_Dev09", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", 
            "_ExternalBomb05", "_ExternalBomb06", "_Dev10", "_Dev11", "_Gun02", "_Gun03", "_Gun04", "_Gun05", "_Dev12", "_Dev13", 
            "_Dev14", "_Dev15", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", 
            "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_Dev16", "_Dev17", 
            "_Dev18", "_Dev19", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalDev20", "_ExternalDev21", "_Rock17", "_Rock18", 
            "_Rock19", "_Rock20", "_Rock21", "_Rock22"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 74;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR3A";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 4xR3A";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR3A+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 4xR3A+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 4xR13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR13M+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 4xR13M+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR13M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 4xR3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR3R+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 4xR3R+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 4xR60";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 4xR60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60M+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 4xR60+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 4xR60M+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR3A+2xR3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR3A+2xR3R+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR13M+2xR3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR13M+2xR3R+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60+2xR3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60+2xR3R+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60M+2xR3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60M+2xR3R+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR3A+2xR23R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR3A+2xR23R+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR13M+2xR23R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR13M+2xR23R+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60+2xR23R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60+2xR23R+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60M+2xR23R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60M+2xR23R+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR3R+2xR23R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR3R+2xR23R+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR3A+2xR23T";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR3A+2xR23T+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR13M+2xR23T";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR13M+2xR23T+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60+2xR23T";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60+2xR23T+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60M+2xR23T";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR60M+2xR23T+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR3R+2xR23T";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter: 2xR3R+2xR23T+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60+2xR3A";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60+2xR3A+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60M+2xR3A";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60M+2xR3A+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60+2xR13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60+2xR13M+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60M+2xR13M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60M+2xR13M+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60+2xR3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60+2xR3R+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60M+2xR3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60M+2xR3R+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 6xR60";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 6xR60+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 6xR60M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 6xR60M+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60+2xR23R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60+2xR23R+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60M+2xR23R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60M+2xR23R+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60+2xR23T";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60+2xR23T+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60M+2xR23T";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Fighter_2: 4xR60M+2xR23T+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunR60M_myotka", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack: 2xFAB250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack: 2xFAB250+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack: 4xFAB250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack: 4xFAB250+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack: 2xFAB500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack: 2xFAB500+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack: 4xFAB500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack: 2xFAB500+2xFAB250+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2: 2xUB16";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2: 2xUB16+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_3: 4xUB16";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_3: 4xUB16+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xUB32";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xUB32+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_5: 2xUB32+2xUB16";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_5: 2xUB32+2xUB16+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xB8";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xB8+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2: 2xFAB250+2xUB16";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2: 2xFAB250+2xUB16+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xFAB250+2xUB32";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xFAB250+2xUB32+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xFAB250+2xB8";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xFAB250+2xB8+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2: 2xFAB500+2xUB16";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2: 2xFAB500+2xUB16+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xFAB500+2xUB32";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xFAB500+2xB8";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xSPPU22";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xSPPU22+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xSPPU22+2xFAB250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xSPPU22+2xFAB250+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xSPPU22+2xFAB500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_5: 2xSPPU22+2xUB16";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_5: 2xSPPU22+2xUB16+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_6: 4xS24";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_6: 4xS24+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_7: 2xS24+2xUB16";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 16);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 16);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_7: 2xS24+2xUB16+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 16);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 16);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_8: 2xS24+2xUB32";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 32);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_8: 2xS24+2xUB32+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 32);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_8: 2xS24+2xB8";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM", 20);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_8: 2xS24+2xB8+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM", 20);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_8: 2xS24+2xSPPU22";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_8: 2xS24+2xSPPU22+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_9: 2xS24+2xFAB250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_9: 2xS24+2xFAB250+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_9: 2xS24+2xFAB500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_9: 2xS24+2xFAB500+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_11: 8xFAB100";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_10: 16xFAB100";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_11: 8xFAB100+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_11: 8xFAB100+2xFAB250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_11: 8xFAB100+2xFAB250+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_11: 8xFAB100+2xFAB500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_12: 8xFAB100+2xUB16";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_12: 8xFAB100+2xUB16+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_13: 8xFAB100+2xS24";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_13: 8xFAB100+2xS24+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_14: 8xFAB100+2xUB32";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_14: 8xFAB100+2xB8";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_14: 8xFAB100+2xSPPU22";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR3A+2xR23R+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR3A+2xR23R+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR13M+2xR23R+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR13M+2xR23R+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR60+2xR23R+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR60+2xR23R+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR60M+2xR23R+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR60M+2xR23R+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR3R+2xR23R+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR3R+2xR23R+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23R", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR3A+2xR23T+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR3A+2xR23T+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunK13A", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR13M+2xR23T+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR13M+2xR23T+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR13M", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR60+2xR23T+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR60+2xR23T+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR60M+2xR23T+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR60M+2xR23T+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR60M_myotka", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR3R+2xR23T+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry: 2xR3R+2xR23T+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(4, "RocketGunR3R", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunR23T", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack: 2xFAB250+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2: 2xUB16+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2: 2xUB16+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_3: 4xUB16+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_3: 4xUB16+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xUB32+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xUB32+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_5: 2xUB32+2xUB16+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xB8+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xB8+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_2: 2xFAB250+2xUB16+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xFAB250+2xUB32+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xFAB250+2xB8+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM", 20);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xSPPU22+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_4: 2xSPPU22+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_5: 2xSPPU22+2xUB16+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_5: 2xSPPU22+2xUB16+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 16);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_6: 4xS24+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_6: 4xS24+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_7: 2xS24+2xUB16+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 16);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 16);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_7: 2xS24+2xUB16+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 16);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 16);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_8: 2xS24+2xUB32+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 32);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 32);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_8: 2xS24+2xB8+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM", 20);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "B8_Pylon", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_8: 2xS24+2xSPPU22+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(9, "PylonSPPU22", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 150);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_9: 2xS24+2xFAB250+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_11: 8xFAB100+2xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_7: 2xKh23";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_9: 2xKh23+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_9: 2xKh23+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_9: 2xKh23+2xFAB250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_9: 2xKh23+2xFAB250+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_9: 2xKh23+2xFAB250+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_9: 2xKh23+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_9: 2xKh23+2xFAB250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_9: 2xKh23+2xFAB250+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_9: 2xKh23+2xFAB250+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_7: 2xKh23+2xUB16";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 16);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_7: 2xKh23+2xUB16+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 16);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_7: 2xKh23+2xUB16+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 16);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunS5M", 16);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB_16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_7: 2xKh23+2xS24";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunS24", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_7: 2xKh23+2xS24+1xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunS24", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_7: 2xKh23+2xS24+3xDroptank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(4, "RocketGunS24", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(4, "RocketGunS24", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank800W", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "GAttack_13: 2xKh23+8xFAB100";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSh23LsM", 260);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunKh23", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}