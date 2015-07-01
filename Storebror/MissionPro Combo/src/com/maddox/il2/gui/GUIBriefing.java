//////////////////////////////////////////////////////////////////////////////////////////
//   GUIBriefing MODded
//   4.111 Function Added by PAL 
//   Added method: 	public void enterPop(GameState gamestate) to let BriefingGeneric know
//   lifted to 4.12 by SAS~Storebror
/////////////////////////////////////////////////////////////////////////////////////////

package com.maddox.il2.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.gwindow.GPoint;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.AirportCarrier;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiTargetsSupportMethods;
import com.maddox.il2.ai.air.CellAirField;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.CameraOrtho2D;
import com.maddox.il2.engine.GUIRenders;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiNetSendMethods;
import com.maddox.il2.game.ZutiPadObject;
import com.maddox.il2.game.ZutiRadarRefresh;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.vehicles.artillery.RocketryRocket;
import com.maddox.il2.objects.vehicles.radios.Beacon;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.NetEnv;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.util.NumberTokenizer;

public class GUIBriefing extends GUIBriefingGeneric
{
    public static class BeaconPoint
    {

        public float x;
        public float y;
        public Mat icon;
        public int army;
        public String id;

        public BeaconPoint()
        {
        }
    }

    public static class TargetPoint
    {

        public boolean isGroundUnit()
        {
            return ZutiPadObject.isGroundUnit(actor);
        }

        public boolean getIsAlive()
        {
            if(actor == null)
                return false;
            if(actor instanceof RocketryRocket)
                return !((RocketryRocket)actor).isDamaged();
            else
                return !actor.getDiedFlag();
        }

        public boolean equals(Object obj)
        {
            if(!(obj instanceof TargetPoint))
                return false;
            TargetPoint targetpoint = (TargetPoint)obj;
            if(actor != null)
            {
                if(actor.equals(targetpoint.actor))
                    return true;
            } else
            if(nameTargetOrig.equals(targetpoint.nameTargetOrig))
                return true;
            return false;
        }

        public int hashCode()
        {
            if(actor != null)
                return actor.hashCode();
            else
                return nameTargetOrig.hashCode();
        }

        public void refreshPosition()
        {
            //By PAL, not in 4.111 mission.getClass();
            if(!mission.zutiRadar_PlayerSideHasRadars)
                return;
            if(actor != null && actor.pos != null)
            {
                Point3d point3d = actor.pos.getAbsPoint();
                x = (float)point3d.x;
                y = (float)point3d.y;
                z = (float)point3d.z;
            }
        }

        public boolean isVisibleForPlayerArmy()
        {
            return visibleForPlayerArmy;
        }

        public void setVisibleForPlayerArmy(boolean flag)
        {
            visibleForPlayerArmy = flag;
        }

        public float x;
        public float y;
        public float z;
        public int r;
        public int type;
        public int typeOArmy;
        public int importance;
        public Mat icon;
        public Mat iconOArmy;
        public String nameTarget;
        public String nameTargetOrig;
        public Actor actor;
        public boolean isBaseActorWing;
        public Wing wing;
        private boolean visibleForPlayerArmy;
        private Mission mission;

        public TargetPoint()
        {
            z = 0.0F;
            isBaseActorWing = false;
            wing = null;
            visibleForPlayerArmy = false;
            mission = null;
            mission = Main.cur().mission;
        }
    }

    private static class PathPoint
    {

        public int type;
        public float x;
        public float y;
//By PAL, from 4.111
        public float z;

        private PathPoint()
        {
        }

    }

    public void enterPush(GameState gamestate) //By PAL, when I return from FMB
    {
    	super.enterPush(gamestate);  //By PAL, to parse coming from to enterPop (Arming)                                		    	    	
    }

    public void enterPop(GameState gamestate) //By PAL, when I return from FMB
    {
    	super.enterPop(gamestate);  //By PAL, to parse coming from to enterPop (Arming)                                		    	    	
    }
    
    public void enter(GameState gamestate) //By PAL, when I return from FMB
    {
    	super.enter(gamestate);  //By PAL, to parse coming from to enterPop (Arming)                                		    	    	
    }    

    public void _enter()
    {
        playerPath.clear();
        playerName = null;
        super._enter();
        ZutiRadarRefresh.findRadars(ZutiSupportMethods.getPlayerArmy());
        ZutiRadarRefresh.resetStartTimes();
        ZUTI_IS_BRIEFING_ACTIVE = true;
        ZutiNetSendMethods.requestUnavailableAircraftList();
        ZutiNetSendMethods.requestCompletedReconList();
        ZutiTargetsSupportMethods.checkForDeactivatedTargets();
        NetAircraft.ZUTI_REFLY_OWERRIDE = false;
        fillBeacons();
    }

    public void _leave()
    {
        super._leave();
        ZUTI_IS_BRIEFING_ACTIVE = false;
    }

    private void drawBornPlaces()
    {
        if(iconBornPlace == null)
            return;
        ArrayList arraylist = World.cur().bornPlaces;
        if(arraylist == null || arraylist.size() == 0)
            return;
        int i = arraylist.size();
        for(int j = 0; j < i; j++)
        {
            BornPlace bornplace = (BornPlace)arraylist.get(j);
            bornplace.tmpForBrief = 0;
        }

        NetUser netuser = (NetUser)NetEnv.host();
        int k = netuser.getBornPlace();
        if(k >= 0 && k < i)
        {
            BornPlace bornplace1 = (BornPlace)arraylist.get(k);
            bornplace1.tmpForBrief = 1;
        }
        List list = NetEnv.hosts();
        for(int l = 0; l < list.size(); l++)
        {
            NetUser netuser1 = (NetUser)list.get(l);
            int j1 = netuser1.getBornPlace();
            if(j1 >= 0 && j1 < i)
            {
                BornPlace bornplace3 = (BornPlace)arraylist.get(j1);
                bornplace3.tmpForBrief++;
            }
        }

        for(int i1 = 0; i1 < i; i1++)
        {
            BornPlace bornplace2 = (BornPlace)arraylist.get(i1);
            IconDraw.setColor(Army.color(bornplace2.army));
            float f = (float)((bornplace2.place.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
            float f1 = (float)((bornplace2.place.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
            if(bornplace2.zutiStaticPositionOnly)
            {
                f = (float)((bornplace2.zutiOriginalX - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
                f1 = (float)((bornplace2.zutiOriginalY - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
            }
            IconDraw.render(iconBornPlace, f, f1);
            if(i1 == k && iconPlayer != null)
                Render.drawTile(f, f1, IconDraw.scrSizeX(), IconDraw.scrSizeY(), 0.0F, iconPlayer, Army.color(bornplace2.army), 0.0F, 1.0F, 1.0F, -1F);
            if(bornplace2.tmpForBrief > 0 && !Main.cur().mission.zutiMisc_HidePlayersCountOnHomeBase)
                gridFont.output(Army.color(bornplace2.army), (int)f + IconDraw.scrSizeX() / 2 + 2, (int)f1 - IconDraw.scrSizeY() / 2 - 2, 0.0F, "" + bornplace2.tmpForBrief);
        }

    }

    private void fillBeacons()
    {
        SectFile sectfile = Main.cur().currentMissionFile;
        
        //+++ TODO: 4.12 changed code +++
//        int i = -1;
        playerArmy = 0;
        //--- TODO: 4.12 changed code ---
        if(Mission.isDogfight())
            //+++ TODO: 4.12 changed code +++
//            i = ((NetUser)NetEnv.host()).getArmy();
            playerArmy = ((NetUser)NetEnv.host()).getArmy();
        	//--- TODO: 4.12 changed code ---
        else
            //+++ TODO: 4.12 changed code +++
//            i = sectfile.get("MAIN", "army", 0);
            playerArmy = sectfile.get("MAIN", "army", 0);
        	//--- TODO: 4.12 changed code ---
        beacons.clear();
        int j = sectfile.sectionIndex("NStationary");
        if(j < 0)
            return;
        int k = sectfile.vars(j);
        for(int l = 0; l < k; l++)
        {
            NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(j, l));
            //+++ TODO: 4.12 changed code +++
//            BeaconPoint beaconpoint = loadbeacon(i, numbertokenizer.next(""), numbertokenizer.next(""), numbertokenizer.next(0), numbertokenizer.next(0.0D), numbertokenizer.next(0.0D));
            BeaconPoint beaconpoint = loadbeacon(playerArmy, numbertokenizer.next(""), numbertokenizer.next(""), numbertokenizer.next(0), numbertokenizer.next(0.0D), numbertokenizer.next(0.0D));
        	//--- TODO: 4.12 changed code ---
            if(beaconpoint != null)
                beacons.add(beaconpoint);
        }

    }

    private BeaconPoint loadbeacon(int i, String s, String s1, int j, double d, double d1)
    {
        if(i != j)
            return null;
        Class class1 = null;
        try
        {
            class1 = ObjIO.classForName(s1);
        }
        catch(Exception exception)
        {
            System.out.println("Mission: class '" + s1 + "' not found");
            return null;
        }
        
        //+++ TODO: 4.12 changed code +++
        if((com.maddox.il2.objects.vehicles.radios.TypeHasRadioStation.class).isAssignableFrom(class1))
        {
            if(i != j && j != 0)
                return null;
        } else
        if(i != j)
            return null;
        //--- TODO: 4.12 changed code ---
        
        if((com.maddox.il2.objects.vehicles.radios.TypeHasBeacon.class).isAssignableFrom(class1))
        {
            BeaconPoint beaconpoint = new BeaconPoint();
            beaconpoint.x = (float)d;
            beaconpoint.y = (float)d1;
            beaconpoint.army = j;
            
            //+++ TODO: 4.12 changed code +++
            String s2 = Beacon.getBeaconID(beacons.size());
            beaconpoint.id = s2;
            //--- TODO: 4.12 changed code ---
            
            if((com.maddox.il2.objects.vehicles.radios.Beacon.RadioBeacon.class).isAssignableFrom(class1))
                beaconpoint.icon = IconDraw.get("icons/beacon.mat");
            else
            if((com.maddox.il2.objects.vehicles.radios.Beacon.RadioBeaconLowVis.class).isAssignableFrom(class1))
                beaconpoint.icon = IconDraw.get("icons/beacon.mat");
            else
            if((com.maddox.il2.objects.vehicles.radios.Beacon.YGBeacon.class).isAssignableFrom(class1))
                beaconpoint.icon = IconDraw.get("icons/beaconYG.mat");
            else
            if((com.maddox.il2.objects.vehicles.radios.Beacon.LorenzBLBeacon.class).isAssignableFrom(class1))
                beaconpoint.icon = IconDraw.get("icons/ILS.mat");
            else
            if((com.maddox.il2.objects.vehicles.radios.Beacon.LorenzBLBeacon_LongRunway.class).isAssignableFrom(class1))
                beaconpoint.icon = IconDraw.get("icons/ILS.mat");
            else
            if((com.maddox.il2.objects.vehicles.radios.Beacon.LorenzBLBeacon_AAIAS.class).isAssignableFrom(class1))
                beaconpoint.icon = IconDraw.get("icons/ILS.mat");
            else
            	
            	//+++ TODO: 4.12 changed code +++
//                return null;
//            String s2 = Beacon.getBeaconID(beacons.size());
//            beaconpoint.id = s2;
            if((com.maddox.il2.objects.vehicles.radios.TypeHasRadioStation.class).isAssignableFrom(class1))
            {
                beaconpoint.icon = IconDraw.get("icons/RadioStation.mat");
                beaconpoint.id = "";
            } else
            {
                return null;
            }
            //--- TODO: 4.12 changed code ---
            
            return beaconpoint;
        } else
        {
            return null;
        }
    }

    public static void fillTargets(SectFile sectfile, ArrayList arraylist)
    {
        arraylist.clear();
        int i = sectfile.sectionIndex("Target");
        if(i >= 0)
        {
            int j = sectfile.vars(i);
            for(int k = 0; k < j; k++)
            {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, k));
                int l = numbertokenizer.next(0, 0, 7);
                int i1 = numbertokenizer.next(0, 0, 2);
                if(i1 == 2)
                    continue;
                TargetPoint targetpoint = new TargetPoint();
                targetpoint.type = l;
                targetpoint.importance = i1;
//                boolean flag = numbertokenizer.next(0) == 1;
//                int j1 = numbertokenizer.next(0, 0, 720);
//                boolean flag1 = numbertokenizer.next(0) == 1;
                targetpoint.x = numbertokenizer.next(0);
                targetpoint.y = numbertokenizer.next(0);
                int k1 = numbertokenizer.next(0);
                if(targetpoint.type == 3 || targetpoint.type == 6 || targetpoint.type == 1)
                {
                    if(k1 < 50)
                        k1 = 50;
                    if(k1 > 3000)
                        k1 = 3000;
                }
                targetpoint.r = k1;
//                int l1 = numbertokenizer.next(0);
                targetpoint.nameTarget = numbertokenizer.next(null);
                if(targetpoint.nameTarget != null && targetpoint.nameTarget.startsWith("Bridge"))
                    targetpoint.nameTarget = null;
                int i2 = numbertokenizer.next(0);
                int j2 = numbertokenizer.next(0);
                if(i2 != 0 && j2 != 0)
                {
                    targetpoint.x = i2;
                    targetpoint.y = j2;
                }
                switch(targetpoint.type)
                {
                case 0: // '\0'
                    targetpoint.icon = IconDraw.get("icons/tdestroyair.mat");
                    if(targetpoint.nameTarget != null && sectfile.exist("Chiefs", targetpoint.nameTarget))
                        targetpoint.icon = IconDraw.get("icons/tdestroychief.mat");
                    break;

                case 1: // '\001'
                    targetpoint.icon = IconDraw.get("icons/tdestroyground.mat");
                    break;

                case 2: // '\002'
                    targetpoint.icon = IconDraw.get("icons/tdestroybridge.mat");
                    targetpoint.nameTarget = null;
                    break;

                case 3: // '\003'
                    targetpoint.icon = IconDraw.get("icons/tinspect.mat");
                    break;

                case 4: // '\004'
                    targetpoint.icon = IconDraw.get("icons/tescort.mat");
                    break;

                case 5: // '\005'
                    targetpoint.icon = IconDraw.get("icons/tdefence.mat");
                    break;

                case 6: // '\006'
                    targetpoint.icon = IconDraw.get("icons/tdefenceground.mat");
                    break;

                case 7: // '\007'
                    targetpoint.icon = IconDraw.get("icons/tdefencebridge.mat");
                    targetpoint.nameTarget = null;
                    break;
                }
                if(targetpoint.nameTarget != null)
                    if(sectfile.exist("Chiefs", targetpoint.nameTarget))
                        try
                        {
                            StringTokenizer stringtokenizer = new StringTokenizer(sectfile.get("Chiefs", targetpoint.nameTarget, (String)null));
                            String s1 = stringtokenizer.nextToken();
                            int k2 = s1.indexOf(".");
                            targetpoint.nameTarget = I18N.technic(s1.substring(0, k2)) + " " + I18N.technic(s1.substring(k2 + 1));
                        }
                        catch(Exception exception)
                        {
                            targetpoint.nameTarget = null;
                        }
                    else
                    if(sectfile.sectionIndex(targetpoint.nameTarget) >= 0)
                        try
                        {
                            String s = sectfile.get(targetpoint.nameTarget, "Class", (String)null);
                            Class class1 = ObjIO.classForName(s);
                            targetpoint.nameTarget = Property.stringValue(class1, "iconFar_shortClassName", null);
                        }
                        catch(Exception exception1)
                        {
                            targetpoint.nameTarget = null;
                        }
                    else
                        targetpoint.nameTarget = null;
                arraylist.add(targetpoint);
            }

        }
    }

    public static void drawTargets(GUIRenders guirenders, TTFont ttfont, Mat mat, CameraOrtho2D cameraortho2d, ArrayList arraylist)
    {
        int i = arraylist.size();
        if(i == 0)
            return;
        GPoint gpoint = guirenders.getMouseXY();
        int j = -1;
        float f = gpoint.x;
        float f1 = guirenders.win.dy - 1.0F - gpoint.y;
        float f2 = IconDraw.scrSizeX() / 2;
        float f3 = f;
        float f4 = f1;
        IconDraw.setColor(0xff00ffff);
        for(int k = 0; k < i; k++)
        {
            TargetPoint targetpoint1 = (TargetPoint)arraylist.get(k);
            if(targetpoint1.icon == null)
                continue;
            float f6 = (float)(((double)targetpoint1.x - cameraortho2d.worldXOffset) * cameraortho2d.worldScale);
            float f7 = (float)(((double)targetpoint1.y - cameraortho2d.worldYOffset) * cameraortho2d.worldScale);
            IconDraw.render(targetpoint1.icon, f6, f7);
            if(f6 >= f - f2 && f6 <= f + f2 && f7 >= f1 - f2 && f7 <= f1 + f2)
            {
                j = k;
                f3 = f6;
                f4 = f7;
            }
        }

        if(j != -1)
        {
            TargetPoint targetpoint = (TargetPoint)arraylist.get(j);
            for(int l = 0; l < 3; l++)
                tip[l] = null;

            if(targetpoint.importance == 0)
                tip[0] = I18N.gui("brief.Primary");
            else
                tip[0] = I18N.gui("brief.Secondary");
            switch(targetpoint.type)
            {
            case 0: // '\0'
                tip[1] = I18N.gui("brief.Destroy");
                break;

            case 1: // '\001'
                tip[1] = I18N.gui("brief.DestroyGround");
                break;

            case 2: // '\002'
                tip[1] = I18N.gui("brief.DestroyBridge");
                break;

            case 3: // '\003'
                tip[1] = I18N.gui("brief.Inspect");
                break;

            case 4: // '\004'
                tip[1] = I18N.gui("brief.Escort");
                break;

            case 5: // '\005'
                tip[1] = I18N.gui("brief.Defence");
                break;

            case 6: // '\006'
                tip[1] = I18N.gui("brief.DefenceGround");
                break;

            case 7: // '\007'
                tip[1] = I18N.gui("brief.DefenceBridge");
                break;
            }
            if(targetpoint.nameTarget != null)
                tip[2] = targetpoint.nameTarget;
            float f5 = ttfont.width(tip[0]);
            int i1 = 1;
            for(int j1 = 1; j1 < 3 && tip[j1] != null; j1++)
            {
                i1 = j1;
                float f9 = ttfont.width(tip[j1]);
                if(f5 < f9)
                    f5 = f9;
            }

            float f8 = -ttfont.descender();
            float f10 = (float)ttfont.height() + f8;
            f5 += 2.0F * f8;
            float f11 = f10 * (float)(i1 + 1) + 2.0F * f8;
            float f12 = f3 - f5 / 2.0F;
            float f13 = f4 + f2;
            if(f12 + f5 > guirenders.win.dx)
                f12 = guirenders.win.dx - f5;
            if(f13 + f11 > guirenders.win.dy)
                f13 = guirenders.win.dy - f11;
            if(f12 < 0.0F)
                f12 = 0.0F;
            if(f13 < 0.0F)
                f13 = 0.0F;
            Render.drawTile(f12, f13, f5, f11, 0.0F, mat, 0xcf7fffff, 0.0F, 0.0F, 1.0F, 1.0F);
            Render.drawEnd();
            for(int k1 = 0; k1 <= i1; k1++)
                ttfont.output(0xff000000, f12 + f8, f13 + f8 + (float)(i1 - k1) * f10 + f8, 0.0F, tip[k1]);

        }
    }

    public void drawBeacons(GUIRenders guirenders, TTFont ttfont, Mat mat, CameraOrtho2D cameraortho2d, ArrayList arraylist)
    {
        int i = arraylist.size();
        if(i == 0)
            return;
        for(int j = 0; j < i; j++)
        {
            BeaconPoint beaconpoint = (BeaconPoint)arraylist.get(j);
            
            //+++ TODO: 4.12 changed code +++
//            int k = Army.color(beaconpoint.army);
            int k = 0;
            if(beaconpoint.army == 0)
                k = Army.color(playerArmy);
            else
                k = Army.color(beaconpoint.army);
            //--- TODO: 4.12 changed code ---
            
            IconDraw.setColor(k);
            if(beaconpoint.icon == null)
                continue;
            float f = (float)(((double)beaconpoint.x - cameraortho2d.worldXOffset) * cameraortho2d.worldScale);
            float f1 = (float)(((double)beaconpoint.y - cameraortho2d.worldYOffset) * cameraortho2d.worldScale);
            IconDraw.render(beaconpoint.icon, f, f1);
            if(cameraortho2d.worldScale > 0.019999999552965164D)
            {
                float f2 = 20F;
                float f3 = 15F;
                gridFont.output(k, f + f2, f1 - f3, 0.0F, beaconpoint.id);
            }
        }

    }

    private void drawBeacons()
    {
        if(World.cur().diffCur.RealisticNavigationInstruments)
            drawBeacons(renders, gridFont, emptyMat, cameraMap2D, beacons);
    }

    private void drawTargets()
    {
        drawTargets(renders, gridFont, emptyMat, cameraMap2D, targets);
    }

    private Mat getIconAir(int i)
    {
        String s = null;
        switch(i)
        {
        case 0: // '\0'
            s = "normfly";
            break;

        case 1: // '\001'
            s = "takeoff";
            break;

        case 2: // '\002'
            s = "landing";
            break;

        case 3: // '\003'
            s = "gattack";
            break;

        default:
            return null;
        }
        return IconDraw.get("icons/" + s + ".mat");
    }

    private void drawPlayerPath()
    {
        checkPlayerPath();
        int i = playerPath.size();
        if(i == 0)
            return;
        if(lineNXYZ.length / 3 <= i)
            lineNXYZ = new float[(i + 1) * 3];
        lineNCounter = 0;
        for(int j = 0; j < i; j++)
        {
            PathPoint pathpoint = (PathPoint)playerPath.get(j);
            lineNXYZ[lineNCounter * 3 + 0] = (float)(((double)pathpoint.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
            lineNXYZ[lineNCounter * 3 + 1] = (float)(((double)pathpoint.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
            lineNXYZ[lineNCounter * 3 + 2] = 0.0F;
            lineNCounter++;
        }

        Render.drawBeginLines(-1);
        Render.drawLines(lineNXYZ, lineNCounter, 2.0F, 0xff000000, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND, 3);
        Render.drawEnd();
        IconDraw.setColor(0xff00ffff);
        float f = 0.0F;
        for(int k = 0; k < i; k++)
        {
            PathPoint pathpoint1 = (PathPoint)playerPath.get(k);
            float f1 = (float)(((double)pathpoint1.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
            float f2 = (float)(((double)pathpoint1.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
            IconDraw.render(getIconAir(pathpoint1.type), f1, f2);
            if(k == i - 1)
            	//+++ TODO: 4.12 changed code +++
//                gridFont.output(0xff000000, (int)f1 + IconDraw.scrSizeX() / 2 + 2, (int)f2 - IconDraw.scrSizeY() / 2 - 2, 0.0F, "" + (k + 1));
            	waypointFont.output(0xff000000, (int)f1 + IconDraw.scrSizeX() / 2 + 2, (int)f2 - IconDraw.scrSizeY() / 2 - 2, 0.0F, (k + 1) + ".");
            	//--- TODO: 4.12 changed code ---
            	
            float f3 = 1.0F - (float)curScale / 7F;
            if(k >= i - 1)
                continue;
//By PAL, from 4.111:
            int l = (int)pathpoint1.z;
            PathPoint pathpoint2 = (PathPoint)playerPath.get(k + 1);
            Point3f point3f = new Point3f(pathpoint1.x, pathpoint1.y, 0.0F);
            Point3f point3f1 = new Point3f(pathpoint2.x, pathpoint2.y, 0.0F);
            point3f.sub(point3f1);
            float f4 = 57.32484F * (float)Math.atan2(point3f.x, point3f.y);
            for(f4 = (f4 + 180F) % 360F; f4 < 0.0F; f4 += 360F);
            for(; f4 >= 360F; f4 -= 360F);
            f4 = Math.round(f4);
            float f5 = 0.0F;
            float f6 = 0.0F;
            if(f4 >= 0.0F && f4 < 90F)
            {
                f5 = 15F;
                f6 = -40F;
                if(f >= 270F && f <= 360F)
                {
                	//+++ TODO: 4.12 changed code +++
//                    f5 = -70F;
//                    f6 = 60F;
                    f5 = -35F * bigFontMultip;
                    f6 = 40F * bigFontMultip;
                    //--- TODO: 4.12 changed code ---
                }
            } else
            if(f4 >= 90F && f4 < 180F)
            {
                f5 = 15F;
            	//+++ TODO: 4.12 changed code +++
//                f6 = 60F;
                f6 = 40F * bigFontMultip;
                //--- TODO: 4.12 changed code ---
                if(f >= 180F && f < 270F)
                {
                	//+++ TODO: 4.12 changed code +++
//                    f5 = -70F;
                    f5 = -35F * bigFontMultip;
                    //--- TODO: 4.12 changed code ---
                    f6 = -15F;
                }
            } else
            if(f4 >= 180F && f4 < 270F)
            {
            	//+++ TODO: 4.12 changed code +++
//                f5 = -70F;
//                f6 = 60F;
                f5 = -35F * bigFontMultip;
                f6 = 40F * bigFontMultip;
                //--- TODO: 4.12 changed code ---
                if(f >= 90F && f < 180F)
                {
                    f5 = 15F;
                	//+++ TODO: 4.12 changed code +++
//                    f6 = 60F;
                    f6 = 40F * bigFontMultip;
                    //--- TODO: 4.12 changed code ---
                }
            } else
            if(f4 >= 270F && f4 <= 360F)
            {
            	//+++ TODO: 4.12 changed code +++
//                f5 = -70F;
                f5 = -35F * bigFontMultip;
                //--- TODO: 4.12 changed code ---
                f6 = -40F;
                if(f >= 0.0F && f < 90F)
                {
                    f5 = 15F;
                    f6 = -40F;
                }
            }
            f5 *= f3;
            f6 *= f3;
            if(curScale >= 3)
            {
                if(f5 < 0.0F)
                    f5 /= 2.0F;
                if(f6 > 0.0F)
                    f6 /= 2.0F;
            }
            f = f4;
            
            //+++ TODO: 4.12 changed code +++
//            gridFont.output(0xff000000, f1 + f5, f2 + f6, 0.0F, "" + (k + 1));
            waypointFont.output(0xff000000, f1 + f5, f2 + f6, 0.0F, (k + 1) + ".");
            //--- TODO: 4.12 changed code ---
            
            if(curScale >= 2)
                continue;
            double d = Math.sqrt(point3f.y * point3f.y + point3f.x * point3f.x) / 1000D;
            if(d < 0.5D)
                continue;
            String s = " km";
            String s1 = "m";
            if(HUD.drawSpeed() == 2 || HUD.drawSpeed() == 3)
            {
                d *= 0.53995698690414429D;
                s = " nm";
                l = (int)((double)l * 3.28084D);
                s1 = "ft";
            }
            String s2 = "" + d;
            s2 = s2.substring(0, s2.indexOf(".") + 2);
            
            //+++ TODO: 4.12 changed code +++
//            gridFont.output(0xff000000, f1 + f5, (f2 + f6) - 22F, 0.0F, l + s1);
//            gridFont.output(0xff000000, f1 + f5, (f2 + f6) - 44F, 0.0F, (int)f4 + "\260");
//            gridFont.output(0xff000000, f1 + f5, (f2 + f6) - 66F, 0.0F, s2 + s);
            waypointFont.output(0xff000000, f1 + f5, (f2 + f6) - 12F * bigFontMultip, 0.0F, l + s1);
            waypointFont.output(0xff000000, f1 + f5, (f2 + f6) - 24F * bigFontMultip, 0.0F, (int)f4 + "\260");
            waypointFont.output(0xff000000, f1 + f5, (f2 + f6) - 36F * bigFontMultip, 0.0F, s2 + s);
            //--- TODO: 4.12 changed code ---
            
        }

    }

    private void checkPlayerPath()
    {
        SectFile sectfile = Main.cur().currentMissionFile;
        String s = null;
        if(Mission.isCoop())
        {
            s = GUINetAircraft.selectedWingName();
            if(s == null)
                s = sectfile.get("MAIN", "player", (String)null);
        } else
        {
            s = sectfile.get("MAIN", "player", (String)null);
        }
        if(s == null)
            if(playerName == null)
            {
                return;
            } else
            {
                playerPath.clear();
                playerName = null;
                return;
            }
        if(s.equals(playerName))
            return;
        playerName = s;
        playerPath.clear();
        if(playerName != null)
        {
            int i = sectfile.sectionIndex(playerName + "_WAY");
            if(i >= 0)
            {
                int j = sectfile.vars(i);
                for(int k = 0; k < j; k++)
                {
                    PathPoint pathpoint = new PathPoint();
                    String s1 = sectfile.var(i, k);
                    if(s1.startsWith("TRIGGERS"))
                        continue;
                    if(s1.startsWith("NORMFLY"))
                        pathpoint.type = 0;
                    else
                    if(s1.startsWith("TAKEOFF"))
                    //+++ TODO: 4.12 changed code +++
//                        pathpoint.type = 1;
                    {
                        if(s1.startsWith("TAKEOFF_004") || s1.startsWith("TAKEOFF_005"))
                            continue;
                        pathpoint.type = 1;
                    }
                    //+++ TODO: 4.12 changed code +++
                    else
                    if(s1.startsWith("LANDING"))
                        pathpoint.type = 2;
                    else
                    if(s1.startsWith("GATTACK"))
                        pathpoint.type = 3;
                    else
                        pathpoint.type = 0;
                    String s2 = sectfile.value(i, k);
                    if(s2 == null || s2.length() <= 0)
                    {
                        pathpoint.x = pathpoint.y = 0.0F;
                    } else
                    {
                        NumberTokenizer numbertokenizer = new NumberTokenizer(s2);
                        pathpoint.x = numbertokenizer.next(-1E+030F, -1E+030F, 1E+030F);
                        pathpoint.y = numbertokenizer.next(-1E+030F, -1E+030F, 1E+030F);
                        pathpoint.z = numbertokenizer.next(-1E+030F, -1E+030F, 1E+030F);
//                        double d = numbertokenizer.next(0.0D, 0.0D, 10000D);
//                        double d1 = numbertokenizer.next(0.0D, 0.0D, 1000D);
                    }
                    playerPath.add(pathpoint);
                }

            }
        }
    }

    protected void doRenderMap2D()
    {
        ZutiRadarRefresh.update(lastScale < cameraMap2D.worldScale);
        lastScale = cameraMap2D.worldScale;
        int i = (int)Math.round((32D * (double)client.root.win.dx) / 1024D);
        //By PAL, old:
        //int i = (int)Math.round(((double)Mission.ZUTI_ICON_SIZE * (double)client.root.win.dx) / 1024D);
        //+++ TODO: 4.12 changed code +++
        float f = client.root.win.dx / client.root.win.dy;
        float f1 = 1.333333F / f;
        i = (int)((float)i * f1);
        //--- TODO: 4.12 changed code ---
        IconDraw.setScrSize(i, i);
        try
        {
            Mission mission = Main.cur().mission;
            if(mission != null)
                ZutiSupportMethods.drawTargets(renders, gridFont, emptyMat, cameraMap2D);
            else
                drawTargets();
            drawBornPlaces();
            drawPlayerPath();
            drawBeacons();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    protected int findBornPlace(float f, float f1)
    {
        if(id() != 40 && id() != 39)
            return -1;
        ArrayList arraylist = World.cur().bornPlaces;
        if(arraylist == null || arraylist.size() == 0)
            return -1;
        int i = arraylist.size();
        double d = (double)(IconDraw.scrSizeX() / 2) / cameraMap2D.worldScale;
        d = 2D * d * d;
        for(int j = 0; j < i; j++)
        {
            BornPlace bornplace = (BornPlace)arraylist.get(j);
            if(bornplace.zutiDisableSpawning)
                continue;
            double d1 = bornplace.place.x;
            double d2 = bornplace.place.y;
            if(bornplace.zutiStaticPositionOnly)
            {
                d1 = bornplace.zutiOriginalX;
                d2 = bornplace.zutiOriginalY;
            }
            if((d1 - (double)f) * (d1 - (double)f) + (d2 - (double)f1) * (d2 - (double)f1) < d && bornplace.army != 0 && bornplace.zutiCanUserJoin())
                return j;
        }

        return -1;
    }

    protected boolean isBornPlace(float f, float f1)
    {
        return findBornPlace(f, f1) >= 0;
    }

    protected void setBornPlace(float f, float f1)
    {
        int i = findBornPlace(f, f1);
        if(i < 0)
            return;
        NetUser netuser = (NetUser)NetEnv.host();
        int j = netuser.getArmy();
        netuser.setBornPlace(i);
        wScrollDescription.resized();
        if(j != netuser.getArmy() && briefSound != null)
        {
            String s = Main.cur().currentMissionFile.get("MAIN", "briefSound" + netuser.getArmy());
            if(s != null)
            {
                briefSound = s;
                CmdEnv.top().exec("music LIST " + briefSound);
            }
        }
        BornPlace bornplace = (BornPlace)World.cur().bornPlaces.get(i);
        UserCfg usercfg = World.cur().userCfg;
        if(usercfg != null && !ZutiSupportMethods.isRegimentValidForSelectedHB(usercfg.netRegiment, bornplace))
        {
            String s1 = ZutiSupportMethods.getHomeBaseFirstCountry(bornplace);
            s1 = ZutiSupportMethods.getUserCfgRegiment(s1);
            usercfg.netRegiment = s1;
            usercfg.netSquadron = 0;
            GUIAirArming.stateId = 2;
            Main.stateStack().push(55);
        }
        if(usercfg != null)
        {
            boolean flag = false;
            ArrayList arraylist = bornplace.zutiGetAcLoadouts(usercfg.netAirName);
            int k = 0;
            do
            {
                if(k >= arraylist.size())
                    break;
                
                //+++ TODO: 4.12 changed code +++
//                if(((String)arraylist.get(k)).equals(I18N.weapons(usercfg.netAirName, usercfg.getWeapon(usercfg.netAirName))))
                if(((String)arraylist.get(k)).equals(usercfg.getWeapon(usercfg.netAirName)))
                //--- TODO: 4.12 changed code ---
                {
                    flag = true;
                    break;
                }
                k++;
            } while(true);
            if(!flag && arraylist.size() != 0)
            {
                usercfg.setWeapon(usercfg.netAirName, (String)arraylist.get(0));
                GUIAirArming.stateId = 2;
                Main.stateStack().push(55);
            }
        }
        ZutiRadarRefresh.findRadars(ZutiSupportMethods.getPlayerArmy());
        fillBeacons();
    }

    protected void doMouseButton(int i, boolean flag, float f, float f1)
    {
        //By PAL, not in 4.111 GUIRenders _tmp = renders;
        if(i == 0)
        {
            bLPressed = flag;
            if(bSelectBorn)
            {
                if(bLPressed)
                {
                    float f2 = (float)(cameraMap2D.worldXOffset + (double)f / cameraMap2D.worldScale);
                    float f3 = (float)(cameraMap2D.worldYOffset + (double)(renders.win.dy - f1 - 1.0F) / cameraMap2D.worldScale);
                    setBornPlace(f2, f3);
                }
                return;
            }
        }
        super.doMouseButton(i, flag, f, f1);
    }

    protected void doMouseMove(float f, float f1)
    {
        if(bLPressed && !bSelectBorn)
        {
            super.doMouseMove(f, f1);
        } else
        {
            float f2 = (float)(cameraMap2D.worldXOffset + (double)f / cameraMap2D.worldScale);
            float f3 = (float)(cameraMap2D.worldYOffset + (double)(renders.win.dy - f1 - 1.0F) / cameraMap2D.worldScale);
            bSelectBorn = isBornPlace(f2, f3);
            renders.mouseCursor = bSelectBorn ? 2 : 3;
        }
    }

    protected void fillMap()
        throws Exception
    {
        super.fillMap();
        SectFile sectfile = Main.cur().currentMissionFile;
        try
        {
            iconBornPlace = IconDraw.get("icons/born.mat");
            iconPlayer = IconDraw.get("icons/player.mat");
            ZutiSupportMethods.setTargetsLoaded(false);
            if(Mission.cur() != null)
                ZutiSupportMethods.fillTargets(sectfile);
            else
                fillTargets(sectfile, targets);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    protected void clientRender()
    {
        GUIBriefingGeneric.DialogClient dialogclient = dialogClient;
        //By PAL, not in 4.111
        //GUIBriefingGeneric.DialogClient _tmp = dialogclient;
        dialogclient.draw(dialogclient.x1024(427F), dialogclient.y1024(633F), dialogclient.x1024(170F), dialogclient.y1024(48F), 1, i18n("brief.Fly"));
    }

    protected String infoMenuInfo()
    {
        return i18n("brief.info");
    }

    public GUIBriefing(int i)
    {
        super(i);
        playerPath = new ArrayList();
        targets = new ArrayList();
        beacons = new ArrayList();
        
        //+++ TODO: 4.12 changed code +++
        playerArmy = 0;
        //--- TODO: 4.12 changed code ---
        
        lastScale = 0.0D;
        lineNXYZ = new float[6];
        bSelectBorn = false;
    }

    protected AirportCarrier getCarrier(NetUser netuser)
    {
        BornPlace bornplace = (BornPlace)World.cur().bornPlaces.get(netuser.getBornPlace());
        Loc loc = new Loc(bornplace.place.x, bornplace.place.y, 0.0D, 0.0F, 0.0F, 0.0F);
        AirportCarrier airportcarrier = (AirportCarrier)Airport.nearest(loc.getPoint(), -1, 4);
        if(airportcarrier == null)
            return null;
        Loc loc1 = airportcarrier.pos.getAbs();
        double d = loc.getX() - loc1.getX();
        double d1 = loc.getY() - loc1.getY();
        double d2 = Math.sqrt(d * d + d1 * d1);
        if(d2 < 50D)
            return airportcarrier;
        else
            return null;
    }
    
/* BY PAL, from Zutti
    protected boolean isCarrierDeckFree(NetUser netuser)
    {
        try
        {
            AirportCarrier airportcarrier = null;
            BornPlace bornplace = (BornPlace)World.cur().bornPlaces.get(netuser.getBornPlace());
            if(bornplace.zutiAirspawnOnly || !World.cur().diffCur.Takeoff_N_Landing || !World.land().isWater(((Tuple2d) (bornplace.place)).x, ((Tuple2d) (bornplace.place)).y))
                return true;
            Loc loc = new Loc(((Tuple2d) (bornplace.place)).x, ((Tuple2d) (bornplace.place)).y, 0.0D, 0.0F, 0.0F, 0.0F);
            airportcarrier = (AirportCarrier)Airport.nearest(loc.getPoint(), -1, 4);
            if(airportcarrier == null || !airportcarrier.ship().isAlive() || airportcarrier.ship().zutiIsStatic())
                return false;
            UserCfg usercfg = World.cur().userCfg;
            Class class1 = (Class)Property.value(usercfg.netAirName, z[0], null);
            NetAircraft netaircraft = (NetAircraft)class1.newInstance();
            CellAirField cellairfield = airportcarrier.ship().getCellTO();
            Aircraft aircraft = (Aircraft)netaircraft;
            if(((SndAircraft) (aircraft)).FM == null)
                return false;
            aircraft.setFM(1, false);
            com.maddox.il2.ai.air.CellAirPlane cellairplane = aircraft.getCellAirPlane();
            return cellairfield.findPlaceForAirPlane(cellairplane);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return false;
    }*/

    protected boolean isCarrierDeckFree(NetUser netuser)
    {
	    try
	    {    	
	        AirportCarrier airportcarrier;
	        BornPlace bornplace = (BornPlace)World.cur().bornPlaces.get(netuser.getBornPlace());
	        if(bornplace.zutiAirspawnIfCarrierFull || bornplace.zutiAirspawnOnly || !World.cur().diffCur.Takeoff_N_Landing || !World.land().isWater(bornplace.place.x, bornplace.place.y))
	            return true;
	        Loc loc = new Loc(bornplace.place.x, bornplace.place.y, 0.0D, 0.0F, 0.0F, 0.0F);
	        airportcarrier = (AirportCarrier)Airport.nearest(loc.getPoint(), -1, 4);
	        if(airportcarrier == null || !airportcarrier.ship().isAlive() || airportcarrier.ship().zutiIsStatic())
	            return false; //break MISSING_BLOCK_LABEL_238;
	        UserCfg usercfg = World.cur().userCfg;	
			Class class1 = (Class)Property.value(usercfg.netAirName, "airClass", null);
			NetAircraft netaircraft = (NetAircraft)class1.newInstance();
			CellAirField cellairfield = airportcarrier.ship().getCellTO();
			Aircraft aircraft = (Aircraft)netaircraft;
			if(aircraft.FM != null)//;
			{
				aircraft.setFM(1, false);
				com.maddox.il2.ai.air.CellAirPlane cellairplane = aircraft.getCellAirPlane();
				if(!cellairfield.findPlaceForAirPlane(cellairplane))
					return false; //    break MISSING_BLOCK_LABEL_224;
				aircraft = null;
				return true;				
			}
			else
			{
				aircraft = null;
				return false;	
			}
			//Object obj = null;			
			//return false;
	    }
		catch(Exception exception)
		{        
	        exception.printStackTrace();
		}
	    return false;		
        //return true;
    }

    protected boolean isValidArming()
    {
        try
        {    	
	        UserCfg usercfg;
	        usercfg = World.cur().userCfg;
	        if(usercfg.netRegiment == null)
	            return false;
	        if(((NetUser)NetEnv.host()).netUserRegiment.isEmpty() && Actor.getByName(usercfg.netRegiment) == null)
	            return false;
	        if(usercfg.netAirName == null)
	            return false;
	        if(Property.value(usercfg.netAirName, "airClass", null) == null)
	            return false;
	        if(usercfg.getWeapon(usercfg.netAirName) == null)
	            return false;
	        Class class1;
	        boolean flag;
	        class1 = (Class)Property.value(usercfg.netAirName, "airClass", null);
	        NetUser netuser = (NetUser)NetEnv.host();
	        int i = netuser.getBornPlace();
	        BornPlace bornplace = (BornPlace)World.cur().bornPlaces.get(i);
	        if(bornplace.airNames == null)
	            return false; //break MISSING_BLOCK_LABEL_215;
	        ArrayList arraylist = bornplace.airNames;
	        flag = false;
	        int j = 0;
	        do
	        {
	            if(j >= arraylist.size())
	                break;
	            String s = (String)arraylist.get(j);
	            Class class2 = (Class)Property.value(s, "airClass", null);
	            if(class2 != null && class1 == class2)
	            {
	                flag = true;
	                break;
	            }
	            j++;
	        } while(true);
	        if(!flag)
	            return false;
	        return Aircraft.weaponsExist(class1, usercfg.getWeapon(usercfg.netAirName));        	
        }
		catch(Exception exception)
		{
			return false;
		}
    }

/*
     protected boolean isCarrierDeckFree(NetUser netuser)
    {
        AirportCarrier airportcarrier;
        BornPlace bornplace = (BornPlace)World.cur().bornPlaces.get(netuser.getBornPlace());
        if(bornplace.zutiAirspawnIfCarrierFull || bornplace.zutiAirspawnOnly || !World.cur().diffCur.Takeoff_N_Landing || !World.land().isWater(bornplace.place.x, bornplace.place.y))
            return true;
        Loc loc = new Loc(bornplace.place.x, bornplace.place.y, 0.0D, 0.0F, 0.0F, 0.0F);
        airportcarrier = (AirportCarrier)Airport.nearest(loc.getPoint(), -1, 4);
        if(airportcarrier == null || !airportcarrier.ship().isAlive() || airportcarrier.ship().zutiIsStatic())
            break MISSING_BLOCK_LABEL_238;
        UserCfg usercfg = World.cur().userCfg;
        Class class1 = (Class)Property.value(usercfg.netAirName, "airClass", null);
        NetAircraft netaircraft = (NetAircraft)class1.newInstance();
        CellAirField cellairfield = airportcarrier.ship().getCellTO();
        Aircraft aircraft = (Aircraft)netaircraft;
        if(aircraft.FM != null);
        aircraft.setFM(1, false);
        com.maddox.il2.ai.air.CellAirPlane cellairplane = aircraft.getCellAirPlane();
        if(!cellairfield.findPlaceForAirPlane(cellairplane))
            break MISSING_BLOCK_LABEL_224;
        aircraft = null;
        return true;
        Object obj = null;
        return false;
        Exception exception;
        exception;
        exception.printStackTrace();
        return false;
        return true;
    }

    protected boolean isValidArming()
    {
        UserCfg usercfg;
        usercfg = World.cur().userCfg;
        if(usercfg.netRegiment == null)
            return false;
        if(((NetUser)NetEnv.host()).netUserRegiment.isEmpty() && Actor.getByName(usercfg.netRegiment) == null)
            return false;
        if(usercfg.netAirName == null)
            return false;
        if(Property.value(usercfg.netAirName, "airClass", null) == null)
            return false;
        if(usercfg.getWeapon(usercfg.netAirName) == null)
            return false;
        Class class1;
        boolean flag;
        class1 = (Class)Property.value(usercfg.netAirName, "airClass", null);
        NetUser netuser = (NetUser)NetEnv.host();
        int i = netuser.getBornPlace();
        BornPlace bornplace = (BornPlace)World.cur().bornPlaces.get(i);
        if(bornplace.airNames == null)
            break MISSING_BLOCK_LABEL_215;
        ArrayList arraylist = bornplace.airNames;
        flag = false;
        int j = 0;
        do
        {
            if(j >= arraylist.size())
                break;
            String s = (String)arraylist.get(j);
            Class class2 = (Class)Property.value(s, "airClass", null);
            if(class2 != null && class1 == class2)
            {
                flag = true;
                break;
            }
            j++;
        } while(true);
        if(!flag)
            return false;
        return Aircraft.weaponsExist(class1, usercfg.getWeapon(usercfg.netAirName));
        Exception exception;
        exception;
        return false;
    }
 */


    protected String playerName;
    protected ArrayList playerPath;
    protected ArrayList targets;
    protected ArrayList beacons;
    
    //+++ TODO: 4.12 changed code +++
    protected int playerArmy;
    //--- TODO: 4.12 changed code ---
    
    protected Mat iconBornPlace;
    protected Mat iconPlayer;
    private double lastScale;
    public static Set ZUTI_TARGETS = new HashSet();
    public static boolean ZUTI_IS_BRIEFING_ACTIVE = false;
    private static String tip[] = new String[3];
    private float lineNXYZ[];
    private int lineNCounter;
    protected boolean bSelectBorn;

}
