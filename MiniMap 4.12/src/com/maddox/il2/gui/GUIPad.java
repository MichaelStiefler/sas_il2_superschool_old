package com.maddox.il2.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3f;
import com.maddox.gwindow.GMesh;
import com.maddox.gwindow.GPoint;
import com.maddox.gwindow.GRegion;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowMenuItem;
import com.maddox.gwindow.GWindowMenuPopUp;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.AirportCarrier;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.Chief;
import com.maddox.il2.ai.Front;
import com.maddox.il2.ai.ScoreCounter;
import com.maddox.il2.ai.ScoreItem;
import com.maddox.il2.ai.Target;
import com.maddox.il2.ai.Way;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiTargetsSupportMethods;
import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.builder.Plugin;
import com.maddox.il2.engine.Accumulator;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.CameraOrtho2D;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.GUIRenders;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightEnvXY;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.Renders;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.fm.Autopilotage;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiPadObject;
import com.maddox.il2.game.ZutiRadarRefresh;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.ships.TestRunway;
import com.maddox.il2.objects.vehicles.artillery.AAA;
import com.maddox.il2.objects.vehicles.radios.Beacon;
import com.maddox.il2.objects.vehicles.radios.TypeHasBeacon;
import com.maddox.il2.objects.vehicles.radios.TypeHasLorenzBlindLanding;
import com.maddox.il2.objects.vehicles.radios.TypeHasRadioStation;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.RTSConf;
import com.maddox.rts.Time;
import com.maddox.util.HashMapExt;
import com.maddox.util.HashMapInt;

public class GUIPad
{
    class AirDrome
    {

        Airport airport;
        int color;
        int army;
        boolean zutiIsOnShip;

        AirDrome()
        {
            zutiIsOnShip = false;
        }
    }

    class ArmyAccum
        implements Accumulator
    {

        public void clear()
        {
        }

        public boolean add(Actor actor, double d)
        {
            _army[actor.getArmy()]++;
            return true;
        }

        ArmyAccum()
        {
        }
    }

    public class RenderMap2D1 extends Render
    {

        public void preRender()
        {
        }

        public void render()
        {
            if(GUIPadMode == 1 || GUIPadMode == 2 || GUIPadMode == 3) {
                if(!full)
                {
	                renders1.draw(0.0F, 0.0F, renders1.win.dx, renders1.win.dy, mesh);
	                if(useNightLight())
	                    renders1.draw(0.0F, 0.0F, renders1.win.dx, renders1.win.dy, redTint);
	            } else
	            if(useNightLight())
	                renders1.draw(0.0F, 0.0F, renders1.win.dx, renders1.win.dy, redTintFull);
            }
            else
            if(GUIPadMode == 4)
                renders1.draw(0.0F, 0.0F, renders1.win.dx, renders1.win.dy, meshradar);
            if(GUIPadMode == 5)
                renders1.draw(0.0F, 0.0F, renders1.win.dx, renders1.win.dy, meshradarF);
            if(GUIPadMode == 6)
                renders1.draw(0.0F, 0.0F, renders1.win.dx, renders1.win.dy, meshbrief);
        }

        public RenderMap2D1(Renders renders2, float f)
        {
            super(renders2, f);
            useClearDepth(false);
            useClearColor(false);
        }
    }

    public class RenderMap2D extends Render
    {

        public int IconSize()
        {
            return 48 - curScale * 6;
        }

        public void preRender()
        {
            Front.preRender(false);
        }

        public void render()
        {
            ZutiRadarRefresh.update(lastScale < curScale);
            lastScale = curScale;
//            zutiPlayeAcDrawn = false;
            try
            {
                if(GUIPadMode == 1)
                {
                    if(main.land2D != null)
//                        main.land2D.render(0.9F, 0.9F, 0.9F, 1.0F);
                    {
                        float f = 1.0F;
                        if(full)
                            f = Config.cur.mapAlpha;
                        if(useNightLight())
                            main.land2D.render(1.0F, 0.5F, 0.5F, f);
                        else
                            main.land2D.render(0.9F, 0.9F, 0.9F, f);
                    }
                    if(main.land2DText != null)
                        main.land2DText.render();
                    drawGrid2D();
                    Front.render(false);
                    int i = IconSize();
                    float f1 = client.root.win.dx / client.root.win.dy;
                    float f2 = 1.333333F / f1;
                    i = (int)((float)i * f2);
                    if(f1 < 1.0F)
                        i = (int)((float)i * 0.5F);
                    IconDraw.setScrSize(i, i);
                    Mission mission = Main.cur().mission;
                    ZutiSupportMethods.drawTargets(renders, gridFont, emptyMat, cameraMap2D);
                    drawAirports();
                    if(!World.cur().diffCur.No_Map_Icons)
                    {
                        drawChiefs();
                        drawAAAandFillAir();
                        drawAir();
                    }
                    drawRadioBeacons();
                    if(!World.cur().diffCur.NoMinimapPath)
                    {
                        Aircraft aircraft5 = World.getPlayerAircraft();
                        if(Actor.isValid(aircraft5))
                        {
                            IconDraw.setColor(0xff00ffff);
                            drawPlayerPath();
                        }
                    }
                    if(World.cur().diffCur.No_Map_Icons && mission != null && mission.zutiRadar_PlayerSideHasRadars)
                        zutiDrawAirInterval();
                    if(!World.cur().diffCur.No_Player_Icon)
                    {
                        Aircraft aircraft6 = World.getPlayerAircraft();
                        if(Actor.isValid(aircraft6))
                        {
                            Point3d point3d3 = aircraft6.pos.getAbsPoint();
                            float f3 = (float)((point3d3.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
                            float f4 = (float)((point3d3.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
                            IconDraw.setColor(-1);
                            IconDraw.render(_iconAir, f3, f4, aircraft6.pos.getAbsOrient().azimut());
                        }
                    }
                    SquareLabels.draw(cameraMap2D, Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.mapSizeX());
                } else
                if(GUIPadMode == 2)
                {
                    drawGrid2DFixed();
                    if(Actor.isValid(World.getPlayerAircraft()) && Actor.isAlive(World.getPlayerAircraft()))
                    {
                        OwnPos3d = World.getPlayerAircraft().pos.getAbsPoint();
                        OwnAzimut = Math.toRadians(360F - World.getPlayerAircraft().pos.getAbsOrient().azimut());
                        OwnAngle = World.getPlayerAircraft().pos.getAbsOrient().azimut();
                        int j = IconSize();
                        IconDraw.setScrSize(j, j);
                        if(!World.cur().diffCur.No_Map_Icons)
                        {
                            drawAirports();
                            drawChiefs();
                            drawAAAandFillAir();
                            drawAir();
                        }
                        if(!World.cur().diffCur.NoMinimapPath && !World.cur().diffCur.No_Player_Icon)
                        {
                            Aircraft aircraft2 = World.getPlayerAircraft();
                            if(Actor.isValid(aircraft2))
                            {
                                IconDraw.setColor(-1);
                                IconDraw.render(_iconAir, FrameOriginX, FrameOriginY, -90F);
                            }
                        }
                    }
                    SquareLabels.draw(cameraMap2D, Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.mapSizeX());
                } else
                if(GUIPadMode == 3)
                {
                    if(main.land2D != null)
                        main.land2D.render(0.9F, 0.9F, 0.9F, 1.0F);
                    if(main.land2DText != null)
                        main.land2DText.render();
                    drawGrid2D();
                    Front.render(false);
                    int k = IconSize();
                    IconDraw.setScrSize(k, k);
                    drawAirports();
                    GUIBriefing.drawTargets(renders, gridFont, emptyMat, cameraMap2D, targets);
                    if(!World.cur().diffCur.NoMinimapPath)
                    {
                        Aircraft aircraft3 = World.getPlayerAircraft();
                        if(Actor.isValid(aircraft3))
                        {
                            IconDraw.setColor(0xff00ffff);
                            drawPlayerPath();
                        }
                    }
                    if(!World.cur().diffCur.NoMinimapPath && !World.cur().diffCur.No_Player_Icon)
                    {
                        Aircraft aircraft4 = World.getPlayerAircraft();
                        if(Actor.isValid(aircraft4))
                        {
                            Point3d point3d = ((Actor) (aircraft4)).pos.getAbsPoint();
                            float f = (float)((((Tuple3d) (point3d)).x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
                            float f2 = (float)((((Tuple3d) (point3d)).y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
                            IconDraw.setColor(-1);
                            IconDraw.render(_iconAir, f, f2, ((Actor) (aircraft4)).pos.getAbsOrient().azimut());
                        }
                    }
                    SquareLabels.draw(cameraMap2D, Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.mapSizeX());
                } else
                if(GUIPadMode == 4)
                {
                    drawGrid2DFixed();
                    Aircraft aircraft = World.getPlayerAircraft();
                    if(!Mission.isNet() && Actor.isValid(aircraft) && Actor.isAlive(aircraft))
                    {
                        int i1 = (int)((Time.current() % 1000L) / 500L);
                        if(i1 != to)
                        {
                            to = i1;
                            Point3d point3d1 = aircraft.pos.getAbsPoint();
                            Orient orient = aircraft.pos.getAbsOrient();
                            double d = (double)(cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale;
                            radarPlane.clear();
                            radarOther.clear();
                            List list = Engine.targets();
                            int i4 = list.size();
                            for(int k4 = 0; k4 < i4; k4++)
                            {
                                Actor actor = (Actor)list.get(k4);
                                if(actor instanceof Aircraft)
                                {
                                    if(actor == World.getPlayerAircraft())
                                        continue;
                                    Point3d point3d4 = new Point3d();
                                    point3d4.set(actor.pos.getAbsPoint());
                                    point3d4.sub(point3d1);
                                    orient.transformInv(point3d4);
                                    double d18 = -((Tuple3d) (point3d4)).y;
                                    double d24 = ((Tuple3d) (point3d4)).x;
                                    if(d18 >= -d && d18 <= d && d24 >= -d && d24 <= d)
                                        radarPlane.add(point3d4);
                                    continue;
                                }
                                if(!(actor instanceof ShipGeneric) && !(actor instanceof BigshipGeneric))
                                    continue;
                                Point3d point3d5 = new Point3d();
                                point3d5.set(actor.pos.getAbsPoint());
                                point3d5.sub(point3d1);
                                orient.transformInv(point3d5);
                                double d19 = -((Tuple3d) (point3d5)).y;
                                double d25 = ((Tuple3d) (point3d5)).x;
                                if(d19 >= -d && d19 <= d && d25 >= -d && d25 <= d)
                                    radarOther.add(point3d5);
                            }

                        }
                        int l1 = radarPlane.size();
                        for(int k2 = 0; k2 < l1; k2++)
                        {
                            double d1 = ((Tuple3d) ((Point3d)radarPlane.get(k2))).x;
                            double d6 = ((Tuple3d) ((Point3d)radarPlane.get(k2))).y;
                            double d10 = ((Tuple3d) ((Point3d)radarPlane.get(k2))).z;
                            double d14 = Math.sqrt(d1 * d1 + d6 * d6 + d10 * d10) / Math.sqrt(d1 * d1 + d6 * d6);
                            double d22 = -d6 * d14;
                            double d28 = d1 * d14;
                            float f8 = (float)FrameOriginX + (float)(d22 * cameraMap2D.worldScale);
                            float f11 = (float)FrameOriginY + (float)(d28 * cameraMap2D.worldScale);
                            int k7 = (int)Math.round(((double)(12 - curScale) * 2D * (double)client.root.win.dx) / 1024D);
                            IconDraw.setScrSize(k7, k7);
                            IconDraw.setColor(0xff00ff00);
                            IconDraw.render(_iconRadar, f8, f11, 0.0F);
                        }

                        l1 = radarOther.size();
                        for(int l2 = 0; l2 < l1; l2++)
                        {
                            double d2 = ((Tuple3d) ((Point3d)radarOther.get(l2))).x;
                            double d7 = ((Tuple3d) ((Point3d)radarOther.get(l2))).y;
                            double d11 = ((Tuple3d) ((Point3d)radarOther.get(l2))).z;
                            double d15 = Math.sqrt(d2 * d2 + d7 * d7 + d11 * d11) / Math.sqrt(d2 * d2 + d7 * d7);
                            double d23 = -d7 * d15;
                            double d29 = d2 * d15;
                            float f9 = (float)FrameOriginX + (float)(d23 * cameraMap2D.worldScale);
                            float f12 = (float)FrameOriginY + (float)(d29 * cameraMap2D.worldScale);
                            int l7 = (int)Math.round(((double)(12 - curScale) * 2D * (double)client.root.win.dx) / 1024D);
                            IconDraw.setScrSize(l7, l7);
                            IconDraw.setColor(0xff00ff00);
                            IconDraw.render(_iconShipRadar, f9, f12, 0.0F);
                        }

                    }
                } else
                if(GUIPadMode == 5)
                {
                    drawGrid2DFixed();
                    Aircraft aircraft1 = World.getPlayerAircraft();
                    if(!Mission.isNet() && Actor.isValid(aircraft1) && Actor.isAlive(aircraft1))
                    {
                        int j1 = (int)((Time.current() % 1000L) / 500L);
                        if(j1 != to)
                        {
                            to = j1;
                            Point3d point3d2 = aircraft1.pos.getAbsPoint();
                            Orient orient1 = aircraft1.pos.getAbsOrient();
                            double d3 = (double)(cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale;
                            radarPlane.clear();
                            radarOther.clear();
                            List list1 = Engine.targets();
                            int j4 = list1.size();
                            for(int l4 = 0; l4 < j4; l4++)
                            {
                                Actor actor1 = (Actor)list1.get(l4);
                                if(actor1 instanceof Aircraft)
                                {
                                    if(actor1 == World.getPlayerAircraft())
                                        continue;
                                    Point3d point3d6 = new Point3d();
                                    point3d6.set(actor1.pos.getAbsPoint());
                                    point3d6.sub(point3d2);
                                    orient1.transformInv(point3d6);
                                    double d20 = -((Tuple3d) (point3d6)).y;
                                    double d26 = ((Tuple3d) (point3d6)).z;
                                    if(d20 >= -d3 && d20 <= d3 && d26 >= -d3 && d26 <= d3)
                                        radarPlane.add(point3d6);
                                    continue;
                                }
                                if(!(actor1 instanceof ShipGeneric) && !(actor1 instanceof BigshipGeneric))
                                    continue;
                                Point3d point3d7 = new Point3d();
                                point3d7.set(actor1.pos.getAbsPoint());
                                point3d7.sub(point3d2);
                                orient1.transformInv(point3d7);
                                double d21 = -((Tuple3d) (point3d7)).y;
                                double d27 = ((Tuple3d) (point3d7)).z;
                                if(d21 >= -d3 && d21 <= d3 && d27 >= -d3 && d27 <= d3)
                                    radarOther.add(point3d7);
                            }

                        }
                        int i2 = radarPlane.size();
                        for(int i3 = 0; i3 < i2; i3++)
                        {
                            double d4 = ((Tuple3d) ((Point3d)radarPlane.get(i3))).x;
                            if(d4 > 5D)
                            {
                                double d8 = 5D / d4;
                                double d12 = -((Tuple3d) ((Point3d)radarPlane.get(i3))).y * d8;
                                double d16 = ((Tuple3d) ((Point3d)radarPlane.get(i3))).z * d8;
                                float f4 = (float)FrameOriginX + (float)(d12 * cameraMap2D.worldScale);
                                float f6 = (float)FrameOriginY + (float)(d16 * cameraMap2D.worldScale);
                                int i6 = (int)Math.round(((double)(16 - curScale) * 3D * (double)client.root.win.dx) / 1024D / (1.0D + d4 / 800D));
                                IconDraw.setScrSize(i6, i6);
                                IconDraw.setColor(0xff00ff00);
                                IconDraw.render(_iconRadar, f4, f6, 0.0F);
                            }
                        }

                        i2 = radarOther.size();
                        for(int j3 = 0; j3 < i2; j3++)
                        {
                            double d5 = ((Tuple3d) ((Point3d)radarOther.get(j3))).x;
                            if(d5 > 5D)
                            {
                                double d9 = 1.0D;
                                double d13 = -((Tuple3d) ((Point3d)radarOther.get(j3))).y * d9;
                                double d17 = ((Tuple3d) ((Point3d)radarOther.get(j3))).z * d9;
                                float f5 = (float)FrameOriginX + (float)(d13 * cameraMap2D.worldScale);
                                float f7 = (float)FrameOriginY + (float)(d17 * cameraMap2D.worldScale);
                                int j6 = (int)Math.round(((double)(16 - curScale) * 3D * (double)client.root.win.dx) / 1024D / (1.0D + d5 / 800D));
                                IconDraw.setScrSize(j6, j6);
                                IconDraw.setColor(0xff00ff00);
                                IconDraw.render(_iconShipRadar, f5, f7, 0.0F);
                            }
                        }

                    }
                } else
                if(GUIPadMode == 6)
                {
                    int l = 0;
                    int k1 = 0;
                    int j2 = 0;
                    ScoreCounter scorecounter = World.cur().scoreCounter;
                    ArrayList arraylist = scorecounter.enemyItems;
                    for(int k3 = 0; k3 < arraylist.size(); k3++)
                    {
                        ScoreItem scoreitem = (ScoreItem)arraylist.get(k3);
                        if(scoreitem.type == 0)
                            l++;
                        else
                            k1++;
                    }

                    ArrayList arraylist1 = scorecounter.friendItems;
                    for(int l3 = 0; l3 < arraylist1.size(); l3++)
                        j2++;

                    List list2 = World.cur().targetsGuard.zutiGetTargets();
                    int i5 = 0;
                    int j5 = 0;
                    int k5 = 0;
                    for(int l5 = 0; l5 < list2.size(); l5++)
                    {
                        Target target = (Target)list2.get(l5);
                        switch(target.importance())
                        {
                        default:
                            break;

                        case 0: // '\0'
                            if(target.isAlive())
                                i5++;
                            break;

                        case 1: // '\001'
                            if(target.isAlive())
                                j5++;
                            break;

                        case 2: // '\002'
                            if(target.isAlive() || !target.isTaskComplete())
                                k5++;
                            break;
                        }
                    }

                    String s = textDescription();
                    if(GUIPad.this.textDescription == null)
                        s = "\n           No brief was found for this mission";
                    else
                        s = "\n" + s;
                    long l6 = Time.current();
                    int k6 = (int)((l6 / 1000L) % 60L);
                    int i7 = (int)(l6 / 1000L / 60L);
                    float f10 = scorecounter.todStart;
                    int j7 = (int)f10;
                    int i8 = (int)((f10 % 1.0F) * 60F);
                    int j8 = (int)((double)smallFont.height() * 1.5D) + smallFont.descender();
                    int k8 = 5;
                    int l8 = renderMap2D.getViewPortWidth();
                    int i9 = renderMap2D.getViewPortHeight();
                    int j9 = (int)((double)l8 * 0.080000000000000002D);
                    int k9 = 0xff000000;
                    smallFont.output(k9, j9, i9 - j9 - j8 * k8++, 0.0F, "Elapsed time: " + (i7 > 9 ? i7 + "m " : "0" + i7 + "m ") + (k6 > 9 ? k6 + "s" : "0" + k6 + "s"));
                    smallFont.output(k9, j9, i9 - j9 - j8 * k8++, 0.0F, "Mission started at: " + (j7 > 9 ? j7 + ":" : "0" + j7 + ":") + (i8 > 9 ? "" + i8 : "0" + i8) + " hours");
                    smallFont.output(k9, j9, i9 - j9 - j8 * k8++, 0.0F, "Enemy air kills: " + (l != 0 ? "" + l : "none"));
                    smallFont.output(k9, j9, i9 - j9 - j8 * k8++, 0.0F, "Enemy ground kills: " + (k1 != 0 ? "" + k1 : "none"));
                    smallFont.output(k9, j9, i9 - j9 - j8 * k8++, 0.0F, "Friendly kills: " + (j2 != 0 ? "" + j2 : "none"));
                    smallFont.output(k9, j9, i9 - j9 - j8 * k8++, 0.0F, "Primary Targets pending: " + (i5 != 0 ? "" + i5 : "none"));
                    smallFont.output(k9, j9, i9 - j9 - j8 * k8++, 0.0F, "Secondary Targets pending:  " + (j5 != 0 ? "" + j5 : "none"));
                    smallFont.output(k9, j9, i9 - j9 - j8 * k8++, 0.0F, "Secret Targets pending: " + (k5 != 0 ? "" + k5 : "none"));
                    smallFont.output(k9, j9, i9 - j9 - j8 * k8++, 0.0F, "Original mission brief:");
                    writeLines(k9, j9, i9 - j9 - j8 * k8++, s, 0, s.length(), l8, i9, j9);
                }
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }

        protected String textDescription()
        {
            return GUIPad.this.textDescription;
        }

        public int writeLines(int i, int j, int k, String s, int l, int i1, float f, 
                float f1, float f2)
        {
            int j1 = (int)((double)smallFont.height() * 1.5D) + smallFont.descender();
            int k1 = 0;
            byte byte0 = 20;
            do
            {
                if(i1 <= 0 || byte0 == 0)
                    break;
                int l1 = i1;
                int i2 = s.indexOf('\n', l);
                if(i2 >= 0)
                    l1 = i2 - l;
                if(l1 > 0)
                {
                    while(l1 > 0 && byte0 != 0) 
                    {
                        int j2 = smallFont.len(s, l, l1, f - 2.0F * f2, true);
                        if(j2 == 0)
                            j2 = smallFont.len(s, l, l1, f - 2.0F * f2, false);
                        if(j2 == 0)
                            return k1;
                        smallFont.outputClip(i, j, k - j1 * k1, 0.0F, s, l, j2, f2, f2, f - 2.0F * f2, f1 - f2);
                        k1++;
                        i1 -= j2;
                        l += j2;
                        l1 -= j2;
                        while(l1 > 0 && s.charAt(l) == ' ') 
                        {
                            l++;
                            i1--;
                            l1--;
                        }
                    }
                    if(i2 >= 0)
                    {
                        l++;
                        i1--;
                    }
                } else
                {
                    k1++;
                    i1--;
                    l++;
                }
            } while(true);
            return k1;
        }

        public String Target(int i)
        {
            String s = "";
            switch(i)
            {
            case 0: // '\0'
                s = "Destroy";
                // fall through

            case 1: // '\001'
                s = "Destroy Ground";
                // fall through

            case 2: // '\002'
                s = "Destroy Bridge";
                // fall through

            case 3: // '\003'
                s = "Inspect";
                // fall through

            case 4: // '\004'
                s = "Escort";
                // fall through

            case 5: // '\005'
                s = "Defence";
                // fall through

            case 6: // '\006'
                s = "Defence Ground";
                // fall through

            case 7: // '\007'
                s = "Defence Bridge";
                // fall through

            default:
                return s;
            }
        }

        public RenderMap2D(Renders renders2, float f)
        {
            super(renders2, f);
            useClearDepth(false);
            useClearColor(false);
        }
    }


    public boolean isActive()
    {
        return bActive;
    }

    public void enter(boolean flag)
    {
        if(bActive)
            return;
        full = flag;
        useMouseWheel();
        frameRegion.set(0.05F, 0.1F, 0.35F, 0.6F);
        bActive = true;
        GUI.activate(true, false);
        client.showWindow();
        float f = client.root.win.dx;
        float f1 = client.root.win.dy;
        float f2 = f / f1;
        float f3 = 1.333333F / f2;
        if(flag)
        {
            frame.setPosSize(0.0F, 0.0F, f, f1);
            cameraMap2D.set(0.0F, f, 0.0F, f1);
            cameraMap2D1.set(0.0F, f, 0.0F, f1);
        } else
        {
            f *= f3;
            if(f2 < 1.0F)
            {
                f *= 0.5F;
                f1 *= 0.5F;
            }
            GUIPadMode = Config.cur.ini.get("game", "mapPadMode", 1);
            frameRegion.x = Config.cur.ini.get("game", "mapPadX", frameRegion.x);
	        frameRegion.y = Config.cur.ini.get("game", "mapPadY", frameRegion.y);
	        if(GUIPadMode == 1 || GUIPadMode == 2 || GUIPadMode == 6)
	        {
	            if(FrameGapX > 0)
	            {
	                frameRegion.x += (float)FrameGapX / f;
	                FrameGapX = 0;
	            }
	            frame.setPosSize(frameRegion.x * f, frameRegion.y * f1, frameRegion.dx * f, frameRegion.dy * f1);
	        } else
	        if(GUIPadMode == 3)
	            if(frameRegion.x + (frameRegion.dx * World.land().getSizeX()) / World.land().getSizeY() <= 1.0F)
	            {
	                FrameGapX = 0;
	                frame.setPosSize(frameRegion.x * f, frameRegion.y * f1, (frameRegion.dx * f * World.land().getSizeX()) / World.land().getSizeY(), frameRegion.dx * f);
	            } else
	            {
	                FrameGapX = (int)((frameRegion.x * f + (frameRegion.dx * f * World.land().getSizeX()) / World.land().getSizeY()) - client.root.win.dx);
	                frame.setPosSize(frameRegion.x * f - (float)FrameGapX, frameRegion.y * f1, (frameRegion.dx * f * World.land().getSizeX()) / World.land().getSizeY(), frameRegion.dx * f);
	            }
	        if(GUIPadMode == 4 || GUIPadMode == 5)
	        {
	            if(FrameGapX > 0)
	            {
	                frameRegion.x += (float)FrameGapX / f;
	                FrameGapX = 0;
	            }
	            frame.setPosSize(frameRegion.x * f, frameRegion.y * f1, frameRegion.dx * f, frameRegion.dx * f);
	        }
	        FrameOriginX = (int)((float)(renderMap2D.getViewPortWidth() / 2) + 0.5F);
	        FrameOriginY = (int)((float)(renderMap2D.getViewPortHeight() / 2) + 0.5F);
	        cameraMap2D.set(0.0F, renderMap2D.getViewPortWidth(), 0.0F, renderMap2D.getViewPortHeight());
	        cameraMap2D1.set(0.0F, renderMap2D1.getViewPortWidth(), 0.0F, renderMap2D1.getViewPortHeight());
        }
        if(frame != null)
            frame.resized();
        computeScales();
        scaleCamera();
        if(bStartMission)
        {
            targets.clear();
            ZutiRadarRefresh.update(lastScale < curScale);
            lastScale = curScale;
            ZutiSupportMethods.fillTargets(Mission.cur().sectFile());
            ZutiTargetsSupportMethods.checkForDeactivatedTargets();
            ZutiSupportMethods.fillGroundChiefsArray(this);
        }
        if(!World.cur().diffCur.No_Player_Icon || bStartMission)
        {
            Actor actor = Actor.getByName("camera");
            float f4;
            float f5;
            if(Actor.isValid(actor) && !World.cur().diffCur.No_Player_Icon)
            {
                Point3d point3d = actor.pos.getAbsPoint();
                f4 = (float)point3d.x;
                f5 = (float)point3d.y;
            } else
            {
            	f4 = World.land().getSizeX() / 2.0F;
            	f5 = World.land().getSizeY() / 2.0F;
            }
            setPosCamera(f4, f5);
            bStartMission = false;
        }
        frame.activateWindow();
        savedUseMesh = main.guiManager.isUseGMeshs();
        saveIconDx = IconDraw.scrSizeX();
        saveIconDy = IconDraw.scrSizeY();
        if(f2 < 1.0F)
        {
            saveIconDx *= 0.5F;
            saveIconDy *= 0.5F;
        }
        saveIconDx = (int)((float)saveIconDx * f3);
        main.guiManager.setUseGMeshs(false);
        try
        {
            String s = Main.cur().currentMissionFile.fileName();
            int i = s.length() - 1;
            do
            {
                if(i <= 0)
                    break;
                char c = s.charAt(i);
                if(c == '\\' || c == '/')
                    break;
                if(c == '.')
                {
                    s = s.substring(0, i);
                    break;
                }
                i--;
            } while(true);
            ResourceBundle resourcebundle = ResourceBundle.getBundle(s, RTSConf.cur.locale);
            textDescription = resourcebundle.getString("Description");
        }
        catch(Exception exception)
        {
            textDescription = null;
        }
    }

    public void leave(boolean flag)
    {
        if(!bActive)
            return;
        bActive = false;
        float f = client.root.win.dx;
        float f1 = client.root.win.dy;
        float f2 = f / f1;
        float f3 = 1.333333F / f2;
        f *= f3;
        if(f2 < 1.0F)
        {
            f *= 0.5F;
            f1 *= 0.5F;
        }
        if(!full)
        {
	        frameRegion.x = frame.win.x / f;
	        frameRegion.y = frame.win.y / f1;
	        frameRegion.dx = frame.win.dx / f;
	        frameRegion.dy = frame.win.dy / f1;
	        if(FrameGapX > 0)
	        {
	            frameRegion.x += (float)FrameGapX / f;
	            FrameGapX = 0;
	        }
	        Config.cur.ini.set("game", "mapPadX", frameRegion.x);
	        Config.cur.ini.set("game", "mapPadY", frameRegion.y);
	        Config.cur.ini.set("game", "mapPadMode", GUIPadMode);
        }
        client.hideWindow();
        Main3D.cur3D().guiManager.setUseGMeshs(savedUseMesh);
        IconDraw.setScrSize(saveIconDx, saveIconDy);
        if(!flag)
            GUI.unActivate();
    }

    private void useMouseWheel()
    {
        HashMapInt hashmapint = HotKeyEnv.env("move").all();
        String s = (String)hashmapint.get(530);
        if(s != null)
            bUseMouseWheel = false;
        else
            bUseMouseWheel = true;
    }

    private void setPosCamera(float f, float f1)
    {
        float f2 = (float)((double)(cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale);
        cameraMap2D.worldXOffset = f - f2 / 2.0F;
        float f3 = (float)((double)(cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale);
        cameraMap2D.worldYOffset = f1 - f3 / 2.0F;
        clipCamera();
    }

    private void scaleCamera()
    {
        cameraMap2D.worldScale = (scale[curScale] * client.root.win.dx) / 1024F;
    }

    private void clipCamera()
    {
        if(cameraMap2D.worldXOffset < -Main3D.cur3D().land2D.worldOfsX())
            cameraMap2D.worldXOffset = -Main3D.cur3D().land2D.worldOfsX();
        float f = (float)((double)(cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale);
        if(cameraMap2D.worldXOffset > Main3D.cur3D().land2D.mapSizeX() - Main3D.cur3D().land2D.worldOfsX() - (double)f)
            cameraMap2D.worldXOffset = Main3D.cur3D().land2D.mapSizeX() - Main3D.cur3D().land2D.worldOfsX() - (double)f;
        if(cameraMap2D.worldYOffset < -Main3D.cur3D().land2D.worldOfsY())
            cameraMap2D.worldYOffset = -Main3D.cur3D().land2D.worldOfsY();
        float f1 = (float)((double)(cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale);
        if(cameraMap2D.worldYOffset > Main3D.cur3D().land2D.mapSizeY() - Main3D.cur3D().land2D.worldOfsY() - (double)f1)
            cameraMap2D.worldYOffset = Main3D.cur3D().land2D.mapSizeY() - Main3D.cur3D().land2D.worldOfsY() - (double)f1;
    }

    private void computeScales()
    {
        float f = (renders.win.dx * 1024F) / client.root.win.dx;
        float f1 = (renders.win.dy * 768F) / client.root.win.dy;
        int i = 0;
        float f2 = 0.064F;
        do
        {
            if(i >= scale.length)
                break;
            scale[i] = f2;
            float f3 = (float)(Main3D.cur3D().land2D.mapSizeX() * (double)f2);
            if(f3 < f)
                break;
            float f5 = (float)(Main3D.cur3D().land2D.mapSizeY() * (double)f2);
            if(f5 < f1)
                break;
            f2 /= 2.0F;
            i++;
        } while(true);
        scales = i;
        if(scales < scale.length)
        {
            float f4 = f / (float)Main3D.cur3D().land2D.mapSizeX();
            float f6 = f1 / (float)Main3D.cur3D().land2D.mapSizeY();
            scale[i] = f4;
            if(f6 > f4)
                scale[i] = f6;
            scales = i + 1;
        }
        if(curScale >= scales)
            curScale = scales - 1;
        if(curScale < 0)
            curScale = 0;
    }

    private void drawGrid2DFixed()
    {
        int i = gridStep();
        double d = (double)(cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale;
        double d1 = (double)(cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale;
        int j = (int)(d / (double)i + 0.5D) + 2;
        int k = (int)(d1 / (double)i + 0.5D) + 2;
        float f = (float)((double)(j * i) * cameraMap2D.worldScale);
        float f1 = (float)((double)(k * i) * cameraMap2D.worldScale);
        float f2 = (float)((double)i * cameraMap2D.worldScale);
        float f3 = renderMap2D.getViewPortWidth() / 2;
        float f4 = renderMap2D.getViewPortHeight() / 2;
        _gridCount = 0;
        smallFont = TTFont.font[0];
        Render.drawBeginLines(-1);
        if(GUIPadMode == 4)
        {
            lineBeamXYZ[0] = lineBeamXYZ[6];
            lineBeamXYZ[1] = lineBeamXYZ[7];
            lineBeamXYZ[2] = 0.0F;
            lineBeamXYZ[3] = f3;
            lineBeamXYZ[4] = f4;
            lineBeamXYZ[5] = 0.0F;
            lineBeamXYZ[6] = (float)(1.0D - Math.cos((float)Time.current() / 1000F)) * f3;
            lineBeamXYZ[7] = (float)(1.0D + Math.sin((float)Time.current() / 1000F)) * f4;
            lineBeamXYZ[8] = 0.0F;
            Render.drawLines(lineBeamXYZ, 3, 2.0F, -1, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 3);
        }
        int l = k / 2;
        for(int i1 = -l; i1 <= l; i1++)
        {
            float f5 = f4 + (float)i1 * f2;
            char c = i1 % 10 == 0 ? '\300' : '\177';
            line2XYZ[0] = 0.0F;
            line2XYZ[1] = f5;
            line2XYZ[2] = 0.0F;
            line2XYZ[3] = f;
            line2XYZ[4] = f5;
            line2XYZ[5] = 0.0F;
            Render.drawLines(line2XYZ, 2, 1.0F, 0xff000000 | c << 16 | c << 8 | c, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
            if(j >= 12 && i1 % 5 == 0 || j < 12)
                smallFont.output(0xff000000, 25F, (int)f5 + 2, 0.0F, "" + Math.abs((i1 * i) / 1000));
        }

        int j1 = j / 2;
        for(int k1 = -j1; k1 <= j1; k1++)
        {
            float f6 = f3 + (float)k1 * f2;
            char c1 = k1 % 10 == 0 ? '\300' : '\177';
            line2XYZ[0] = f6;
            line2XYZ[1] = 0.0F;
            line2XYZ[2] = 0.0F;
            line2XYZ[3] = f6;
            line2XYZ[4] = f1;
            line2XYZ[5] = 0.0F;
            Render.drawLines(line2XYZ, 2, 1.0F, 0xff000000 | c1 << 16 | c1 << 8 | c1, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
            if(j >= 12 && k1 % 5 == 0 || j < 12)
                smallFont.output(0xff000000, (int)f6 + 2, 25F, 0.0F, "" + Math.abs((k1 * i) / 1000));
        }

        Render.drawEnd();
        gridFont.output(0xff000000, (int)f3 - 50, renderMap2D.getViewPortHeight() - 45, 0.0F, curScale <= 2 ? "x10: 1km/div" : "x1: 10km/div");
    }

    private void drawGrid2D()
    {
        int i = gridStep();
        int j = (int)((cameraMap2D.worldXOffset + Main3D.cur3D().land2D.worldOfsX()) / (double)i);
        int k = (int)((cameraMap2D.worldYOffset + Main3D.cur3D().land2D.worldOfsY()) / (double)i);
        double d = (double)(cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale;
        double d1 = (double)(cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale;
        int l = (int)(d / (double)i) + 2;
        int i1 = (int)(d1 / (double)i) + 2;
        float f = (float)(((double)(j * i) - cameraMap2D.worldXOffset - Main3D.cur3D().land2D.worldOfsX()) * cameraMap2D.worldScale + 0.5D);
        float f1 = (float)(((double)(k * i) - cameraMap2D.worldYOffset - Main3D.cur3D().land2D.worldOfsY()) * cameraMap2D.worldScale + 0.5D);
        float f2 = (float)((double)(l * i) * cameraMap2D.worldScale);
        float f3 = (float)((double)(i1 * i) * cameraMap2D.worldScale);
        float f4 = (float)((double)i * cameraMap2D.worldScale);
        _gridCount = 0;
        Render.drawBeginLines(-1);
        for(int j1 = 0; j1 <= i1; j1++)
        {
            float f5 = f1 + (float)j1 * f4;
            char c = (j1 + k) % 10 == 0 ? '\300' : '\177';
            line2XYZ[0] = f;
            line2XYZ[1] = f5;
            line2XYZ[2] = 0.0F;
            line2XYZ[3] = f + f2;
            line2XYZ[4] = f5;
            line2XYZ[5] = 0.0F;
            Render.drawLines(line2XYZ, 2, 1.0F, 0xff000000 | c << 16 | c << 8 | c, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
            if(c == '\300')
                drawGridText(0, (int)f5, (k + j1) * i);
        }

        for(int k1 = 0; k1 <= l; k1++)
        {
            float f6 = f + (float)k1 * f4;
            char c1 = (k1 + j) % 10 == 0 ? '\300' : '\177';
            line2XYZ[0] = f6;
            line2XYZ[1] = f1;
            line2XYZ[2] = 0.0F;
            line2XYZ[3] = f6;
            line2XYZ[4] = f1 + f3;
            line2XYZ[5] = 0.0F;
            Render.drawLines(line2XYZ, 2, 1.0F, 0xff000000 | c1 << 16 | c1 << 8 | c1, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
            if(c1 == '\300')
                drawGridText((int)f6, 0, (j + k1) * i);
        }

        Render.drawEnd();
        drawGridText();
    }

    private int gridStep()
    {
        float f = cameraMap2D.right - cameraMap2D.left;
        float f1 = cameraMap2D.top - cameraMap2D.bottom;
        double d = f;
        if(f1 < f)
            d = f1;
        d /= cameraMap2D.worldScale;
        int i = 0x186a0;
        for(int j = 0; j < 5 && (double)(i * 3) > d; j++)
            i /= 10;

        return i;
    }

    private void drawGridText(int i, int j, int k)
    {
        if(i < 0 || j < 0 || k <= 0 || _gridCount == 20)
        {
            return;
        } else
        {
            _gridX[_gridCount] = i;
            _gridY[_gridCount] = j;
            _gridVal[_gridCount] = k;
            _gridCount++;
            return;
        }
    }

    private void drawGridText()
    {
        for(int i = 0; i < _gridCount; i++)
            gridFont.output(0xffc0c0c0, _gridX[i] + 2, _gridY[i] + 2, 0.0F, _gridVal[i] / 1000 + "." + (_gridVal[i] % 1000) / 100);

        _gridCount = 0;
    }

    private void drawAirports()
    {
        Mission mission = Main.cur().mission;
        if(mission == null)
            return;
        int i = World.getPlayerArmy();
        Mat mat = IconDraw.get("icons/runaway.mat");
        for(int j = 0; j < airdrome.size(); j++)
        {
            AirDrome airdrome1 = (AirDrome)airdrome.get(j);
            if(zutiColorAirfields)
                zutiChangeAirportArmyAndColor(airdrome1);
            if(airdrome1.army != i && mission.zutiRadar_HideUnpopulatedAirstripsFromMinimap || !Actor.isValid(airdrome1.airport) || !Actor.isAlive(airdrome1.airport))
                continue;
            Point3d point3d = airdrome1.airport.pos.getAbsPoint();
            if(GUIPadMode == 1)
            {
                float f = (float)((point3d.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
                float f3 = (float)((point3d.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
                if(airdrome1.army != i)
                {
                    if(airdrome1.zutiIsOnShip)
                        continue;
                    IconDraw.setColor(-1);
                } else
                {
                    IconDraw.setColor(airdrome1.color);
                }
                IconDraw.render(mat, f, f3);
            } else
            if(GUIPadMode == 2)
            {
                float f1 = (float)((((Tuple3d) (point3d)).x - ((Tuple3d) (OwnPos3d)).x) * Math.cos(OwnAzimut)) - (float)((-((Tuple3d) (point3d)).y + ((Tuple3d) (OwnPos3d)).y) * Math.sin(OwnAzimut));
                float f4 = (float)((-((Tuple3d) (point3d)).y + ((Tuple3d) (OwnPos3d)).y) * Math.cos(OwnAzimut)) + (float)((((Tuple3d) (point3d)).x - ((Tuple3d) (OwnPos3d)).x) * Math.sin(OwnAzimut));
                float f6 = (float)FrameOriginX + (float)((double)f4 * cameraMap2D.worldScale);
                float f7 = (float)FrameOriginY + (float)((double)f1 * cameraMap2D.worldScale);
                if(airdrome1.army != i)
                {
                    if(airdrome1.zutiIsOnShip)
                        continue;
                    IconDraw.setColor(-1);
                } else
                {
                    IconDraw.setColor(airdrome1.color);
                }
                IconDraw.render(mat, f6, f7);
            } else
            if(GUIPadMode == 3)
            {
                float f2 = (float)((point3d.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
                float f5 = (float)((point3d.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
                if(airdrome1.army != i)
                {
                    if(airdrome1.zutiIsOnShip)
                        continue;
                    IconDraw.setColor(-1);
                } else
                {
                    IconDraw.setColor(airdrome1.color);
                }
                IconDraw.render(_iconAirField, f2, f5, airdrome1.airport.pos.getAbsOrient().azimut());
            }
            zutiColorAirfields = false;
        }

    }

    public int getAirportArmy(Airport airport)
    {
        for(int i = 0; i < airdrome.size(); i++)
        {
            AirDrome airdrome1 = (AirDrome)airdrome.get(i);
            if(airdrome1.airport == airport)
                return airdrome1.army;
        }

        return 0;
    }

    public void fillAirports()
    {
        airdrome.clear();
        ArrayList arraylist = new ArrayList();
        World.cur();
        World.getAirports(arraylist);
        for(int i = 0; i < arraylist.size(); i++)
        {
            Airport airport = (Airport)arraylist.get(i);
            AirDrome airdrome1 = new AirDrome();
            airdrome1.airport = airport;
            if(airport instanceof AirportCarrier)
            {
                AirportCarrier airportcarrier = (AirportCarrier)airport;
                if(World.cur().diffCur.RealisticNavigationInstruments && !(airportcarrier.ship() instanceof TestRunway))
                    continue;
                if(Actor.isValid(airportcarrier.ship()))
                    airdrome1.army = airportcarrier.ship().getArmy();
                airdrome1.zutiIsOnShip = true;
            }
            airdrome.add(airdrome1);
        }

        Point3d point3d = new Point3d();
        for(int j = 0; j < airdrome.size(); j++)
        {
            AirDrome airdrome2 = (AirDrome)airdrome.get(j);
            Point3d point3d1 = airdrome2.airport.pos.getAbsPoint();
            World.land();
            point3d.set(point3d1.x, point3d1.y, Landscape.HQ((float)point3d1.x, (float)point3d1.y));
            int k = 0;
            if(airdrome2.army == 0)
            {
                Engine.collideEnv().getNearestEnemies(point3d, 2000D, 0, armyAccum);
                int l = 0;
                for(int i1 = 1; i1 < _army.length; i1++)
                {
                    if(l < _army[i1])
                    {
                        l = _army[i1];
                        k = i1;
                    }
                    _army[i1] = 0;
                }

                airdrome2.army = k;
            }
            airdrome2.color = Army.color(airdrome2.army);
        }

    }

    private void drawChiefs()
    {
        HashMapExt hashmapext = Engine.name2Actor();
        for(java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry))
        {
            Actor actor = (Actor)entry.getValue();
            if(!(actor instanceof Chief))
                continue;
            if((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric))
            {
                Mat mat = actor.icon;
                if(mat == null)
                    continue;
                Point3d point3d = actor.pos.getAbsPoint();
                if(GUIPadMode == 1 || GUIPadMode == 3)
                {
                    float f = (float)((point3d.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
                    float f4 = (float)((point3d.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
                    IconDraw.setColor(Army.color(actor.getArmy()));
                    IconDraw.render(mat, f, f4);
                    continue;
                }
                if(GUIPadMode == 2)
                {
                    float f1 = (float)((((Tuple3d) (point3d)).x - ((Tuple3d) (OwnPos3d)).x) * Math.cos(OwnAzimut)) - (float)((-((Tuple3d) (point3d)).y + ((Tuple3d) (OwnPos3d)).y) * Math.sin(OwnAzimut));
                    float f5 = (float)((-((Tuple3d) (point3d)).y + ((Tuple3d) (OwnPos3d)).y) * Math.cos(OwnAzimut)) + (float)((((Tuple3d) (point3d)).x - ((Tuple3d) (OwnPos3d)).x) * Math.sin(OwnAzimut));
                    float f8 = (float)FrameOriginX + (float)((double)f5 * cameraMap2D.worldScale);
                    float f10 = (float)FrameOriginY + (float)((double)f1 * cameraMap2D.worldScale);
                    IconDraw.setColor(Army.color(actor.getArmy()));
                    IconDraw.render(mat, f8, f10);
                }
                continue;
            }
            if(!(actor instanceof Chief))
                continue;
            Mat mat1 = actor.icon;
            if(mat1 == null)
                continue;
            Point3d point3d1 = actor.pos.getAbsPoint();
            if(GUIPadMode == 1 || GUIPadMode == 3)
            {
                float f2 = (float)((point3d1.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
                float f6 = (float)((point3d1.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
                IconDraw.setColor(Army.color(actor.getArmy()));
                IconDraw.render(mat1, f2, f6);
                continue;
            }
            if(GUIPadMode == 2)
            {
                float f3 = (float)((((Tuple3d) (point3d1)).x - ((Tuple3d) (OwnPos3d)).x) * Math.cos(OwnAzimut)) - (float)((-((Tuple3d) (point3d1)).y + ((Tuple3d) (OwnPos3d)).y) * Math.sin(OwnAzimut));
                float f7 = (float)((-((Tuple3d) (point3d1)).y + ((Tuple3d) (OwnPos3d)).y) * Math.cos(OwnAzimut)) + (float)((((Tuple3d) (point3d1)).x - ((Tuple3d) (OwnPos3d)).x) * Math.sin(OwnAzimut));
                float f9 = (float)FrameOriginX + (float)((double)f7 * cameraMap2D.worldScale);
                float f11 = (float)FrameOriginY + (float)((double)f3 * cameraMap2D.worldScale);
                IconDraw.setColor(Army.color(actor.getArmy()));
                IconDraw.render(mat1, f9, f11);
            }
        }

    }

    private void drawAAAandFillAir()
    {
        double d = cameraMap2D.worldXOffset;
        double d1 = cameraMap2D.worldYOffset;
        double d2 = cameraMap2D.worldXOffset + (double)(cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale;
        double d3 = cameraMap2D.worldYOffset + (double)(cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale;
        List list = Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if(actor instanceof Aircraft)
            {
                if(actor == World.getPlayerAircraft())
                    continue;
                Point3d point3d = actor.pos.getAbsPoint();
                if(GUIPadMode == 2 || GUIPadMode == 4 || GUIPadMode == 5)
                {
                    airs.add(actor);
                    continue;
                }
                if(point3d.x >= d && point3d.x <= d2 && point3d.y >= d1 && point3d.y <= d3)
                    airs.add(actor);
                continue;
            }
            if(!(actor instanceof AAA))
                continue;
            Point3d point3d1 = actor.pos.getAbsPoint();
            if(point3d1.x < d || point3d1.x > d2 || point3d1.y < d1 || point3d1.y > d3)
                continue;
            Mat mat = IconDraw.create(actor);
            if(mat == null)
                continue;
            if(GUIPadMode == 1)
            {
                float f = (float)((point3d1.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
                float f2 = (float)((point3d1.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
                IconDraw.setColor(Army.color(actor.getArmy()));
                IconDraw.render(mat, f, f2);
                continue;
            }
            if(GUIPadMode == 2)
            {
                float f1 = (float)((((Tuple3d) (point3d1)).x - ((Tuple3d) (OwnPos3d)).x) * Math.cos(OwnAzimut)) - (float)((-((Tuple3d) (point3d1)).y + ((Tuple3d) (OwnPos3d)).y) * Math.sin(OwnAzimut));
                float f3 = (float)((-((Tuple3d) (point3d1)).y + ((Tuple3d) (OwnPos3d)).y) * Math.cos(OwnAzimut)) + (float)((((Tuple3d) (point3d1)).x - ((Tuple3d) (OwnPos3d)).x) * Math.sin(OwnAzimut));
                float f4 = (float)FrameOriginX + (float)((double)f3 * cameraMap2D.worldScale);
                float f5 = (float)FrameOriginY + (float)((double)f1 * cameraMap2D.worldScale);
                IconDraw.setColor(Army.color(actor.getArmy()));
                IconDraw.render(mat, f4, f5);
            }
        }

    }

    private void drawRadioBeacons()
    {
        double d = cameraMap2D.worldXOffset;
        double d1 = cameraMap2D.worldYOffset;
        double d2 = cameraMap2D.worldXOffset + (double)(cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale;
        double d3 = cameraMap2D.worldYOffset + (double)(cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale;
        Aircraft aircraft = World.getPlayerAircraft();
        if(!Actor.isValid(aircraft))
            return;
        ArrayList arraylist = Main.cur().mission.getBeacons(aircraft.getArmy());
        if(arraylist == null)
            return;
        int i = arraylist.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)arraylist.get(j);
//            if(!(actor instanceof TypeHasBeacon) || (actor instanceof TgtShip) || (actor instanceof TypeHasRadioStation) || actor.getArmy() != World.getPlayerAircraft().getArmy())
            if(!(actor instanceof TypeHasBeacon) || (actor instanceof TgtShip) || actor.getArmy() != World.getPlayerAircraft().getArmy() && (!(actor instanceof TypeHasRadioStation) || actor.getArmy() != 0))
                continue;
            Point3d point3d = actor.pos.getAbsPoint();
            if(point3d.x < d || point3d.x > d2 || point3d.y < d1 || point3d.y > d3)
                continue;
            float f = (float)((point3d.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
            float f1 = (float)((point3d.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
//            int k = Army.color(actor.getArmy());
            int k = Army.color(World.getPlayerAircraft().getArmy());
            if(actor instanceof TypeHasLorenzBlindLanding)
            {
                float f2 = actor.pos.getAbsOrient().azimut() + 90F;
                for(f2 = (f2 + 180F) % 360F; f2 < 0.0F; f2 += 360F);
                for(; f2 >= 360F; f2 -= 360F);
                IconDraw.setColor(-1);
                IconDraw.render(_iconILS, f, f1, f2 - 90F);
                float f3 = 20F;
                String s = Beacon.getBeaconID(j);
                gridFont.output(k, f + f3, f1 - f3, 0.0F, s);
                if(curScale < 2)
                {
                    int l = (int)actor.pos.getAbsPoint().z;
                    String s2 = (int)f2 + "\260";
                    gridFont.output(k, f + f3, f1 - f3 - 20F, 0.0F, s2);
                    s2 = l + "m";
                    gridFont.output(k, f + f3, f1 - f3 - 40F, 0.0F, s2);
                }
//                continue;
            }
            if(actor instanceof TypeHasRadioStation)
            {
	            Mat mat = IconDraw.create(actor);
	            if(mat != null)
	            {
	                IconDraw.setColor(k);
	                IconDraw.render(mat, f, f1);
                }
                continue;
            }
            Mat mat1 = IconDraw.create(actor);
            if(mat1 != null)
            {
                IconDraw.setColor(k);
                IconDraw.render(mat1, f, f1);
                float f4 = 20F;
                float f5 = 15F;
                String s1 = Beacon.getBeaconID(j);
                gridFont.output(k, f + f4, f1 - f5, 0.0F, s1);
            }
        }

    }

    private void drawAir()
    {
        int i = airs.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)airs.get(j);
            Point3d point3d = actor.pos.getAbsPoint();
            if(GUIPadMode == 1)
            {
                float f = (float)((point3d.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
                float f2 = (float)((point3d.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
                IconDraw.setColor(Army.color(actor.getArmy()));
                IconDraw.render(_iconAir, f, f2, actor.pos.getAbsOrient().azimut());
                continue;
            }
            if(GUIPadMode == 2)
            {
                float f1 = (float)((((Tuple3d) (point3d)).x - ((Tuple3d) (OwnPos3d)).x) * Math.cos(OwnAzimut)) - (float)((-((Tuple3d) (point3d)).y + ((Tuple3d) (OwnPos3d)).y) * Math.sin(OwnAzimut));
                float f3 = (float)((-((Tuple3d) (point3d)).y + ((Tuple3d) (OwnPos3d)).y) * Math.cos(OwnAzimut)) + (float)((((Tuple3d) (point3d)).x - ((Tuple3d) (OwnPos3d)).x) * Math.sin(OwnAzimut));
                float f4 = (float)FrameOriginX + (float)((double)f3 * cameraMap2D.worldScale);
                float f5 = (float)FrameOriginY + (float)((double)f1 * cameraMap2D.worldScale);
                IconDraw.setColor(Army.color(actor.getArmy()));
                IconDraw.render(_iconAir, f4, f5, actor.pos.getAbsOrient().azimut() - OwnAngle - 90F);
                continue;
            }
            if(GUIPadMode == 4)
            {
                Aircraft aircraft = World.getPlayerAircraft();
                if(Actor.isValid(aircraft))
                {
                    Point3d point3d1 = World.getPlayerAircraft().pos.getAbsPoint();
                    Orient orient = World.getPlayerAircraft().pos.getAbsOrient();
                    Point3d point3d3 = new Point3d();
                    point3d3.set(actor.pos.getAbsPoint());
                    point3d3.sub(point3d1);
                    orient.transformInv(point3d3);
                    double d = ((Tuple3d) (point3d3)).x;
                    double d2 = ((Tuple3d) (point3d3)).y;
                    double d4 = ((Tuple3d) (point3d3)).z;
                    double d6 = Math.sqrt(d * d + d2 * d2 + d4 * d4) / Math.sqrt(d * d + d2 * d2);
                    double d8 = -d2 * d6;
                    double d10 = d * d6;
                    float f6 = (float)FrameOriginX + (float)(d8 * cameraMap2D.worldScale);
                    float f8 = (float)FrameOriginY + (float)(d10 * cameraMap2D.worldScale);
                    int k = (int)Math.round(((double)(10 - curScale) * 2D * (double)client.root.win.dx) / 1024D);
                    IconDraw.setScrSize(k, k);
                    IconDraw.setColor(0xff00ffff);
                    IconDraw.render(_iconRadar, f6, f8, 0.0F);
                }
                continue;
            }
            if(GUIPadMode != 5)
                continue;
            Aircraft aircraft1 = World.getPlayerAircraft();
            if(!Actor.isValid(aircraft1))
                continue;
            Point3d point3d2 = World.getPlayerAircraft().pos.getAbsPoint();
            Orient orient1 = World.getPlayerAircraft().pos.getAbsOrient();
            Point3d point3d4 = new Point3d();
            point3d4.set(actor.pos.getAbsPoint());
            point3d4.sub(point3d2);
            orient1.transformInv(point3d4);
            double d1 = ((Tuple3d) (point3d4)).x;
            if(d1 > 5D)
            {
                double d3 = ((Tuple3d) (point3d4)).y;
                double d5 = ((Tuple3d) (point3d4)).z;
                double d7 = 1.0D;
                double d9 = -d3 * d7;
                double d11 = d5 * d7;
                float f7 = (float)FrameOriginX + (float)(d9 * cameraMap2D.worldScale);
                float f9 = (float)FrameOriginY + (float)(d11 * cameraMap2D.worldScale);
                int l = (int)Math.round(((double)(20 - curScale) * 2D * (double)client.root.win.dx) / 1024D / (1.0D + d1 / 800D));
                IconDraw.setScrSize(l, l);
                IconDraw.setColor(0xff00ffff);
                IconDraw.render(_iconRadar, f7, f9, 0.0F);
            }
        }

        airs.clear();
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
        Autopilotage autopilotage = World.getPlayerFM().AP;
        if(autopilotage == null)
            return;
        Way way = autopilotage.way;
        if(way == null)
            return;
        int i = way.Cur();
        int j = way.size();
        if(j <= 0 || i >= j)
            return;
        if(lineNXYZ.length / 3 <= j)
            lineNXYZ = new float[(j + 1) * 3];
        lineNCounter = 0;
        int k = 0;
        Render.drawBeginLines(-1);
        while(k < j) 
        {
            WayPoint waypoint = way.get(k++);
            if(waypoint.waypointType != 4 && waypoint.waypointType != 5)
            {
	            waypoint.getP(_wayP);
	            lineNXYZ[lineNCounter * 3 + 0] = (float)(((double)_wayP.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
	            lineNXYZ[lineNCounter * 3 + 1] = (float)(((double)_wayP.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
	            lineNXYZ[lineNCounter * 3 + 2] = 0.0F;
	            lineNCounter++;
            }
        }
        Render.drawLines(lineNXYZ, lineNCounter, 2.0F, 0xff000000, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND, 3);
        if(!World.cur().diffCur.No_Player_Icon && !World.cur().diffCur.RealisticNavigationInstruments)
        {
            Point3d point3d = World.getPlayerAircraft().pos.getAbsPoint();
            lineNXYZ[0] = (float)((point3d.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
            lineNXYZ[1] = (float)((point3d.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
            lineNXYZ[2] = 0.0F;
            WayPoint waypoint1 = way.get(i);
            waypoint1.getP(_wayP);
            lineNXYZ[3] = (float)(((double)_wayP.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
            lineNXYZ[4] = (float)(((double)_wayP.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
            lineNXYZ[5] = 0.0F;
            Render.drawLines(lineNXYZ, 2, 2.0F, 0xff000000, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND, 3);
        }
        float f = 0.0F;
        Render.drawEnd();
        k = 0;
        int l = 0;
        do
        {
            if(k >= j)
                break;
            WayPoint waypoint2 = way.get(k++);
            if(waypoint2.waypointType != 4 && waypoint2.waypointType != 5)
            {
                l++;
	            waypoint2.getP(_wayP);
	            float f1 = (float)(((double)_wayP.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
	            float f2 = (float)(((double)_wayP.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
	            IconDraw.render(getIconAir(waypoint2.Action), f1, f2);
	            if(curScale < (int)(4F - bigFontMultip) && k < j)
	            {
	                WayPoint waypoint3 = way.get(k);
	                waypoint3.getP(_wayP2);
	                _wayP.sub(_wayP2);
	                float f3 = 57.32484F * (float)Math.atan2(_wayP.x, _wayP.y);
	                for(f3 = (f3 + 180F) % 360F; f3 < 0.0F; f3 += 360F);
	                for(; f3 >= 360F; f3 -= 360F);
	                f3 = Math.round(f3);
	                double d = Math.sqrt(_wayP.y * _wayP.y + _wayP.x * _wayP.x) / 1000D;
	                if(waypoint2.deltaZ == -1)
	                {
	                    World.land();
	                    waypoint2.deltaZ = (int)Landscape.HQ(waypoint2.x(), waypoint2.y());
	                }
	                int i1 = (int)(waypoint2.z() - (float)waypoint2.deltaZ);
	                if(d >= 1.0D)
	                {
	                    String s = "km";
	                    String s1 = "m";
	                    if(HUD.drawSpeed() == 2 || HUD.drawSpeed() == 3)
	                    {
	                        d *= 0.53995698690414429D;
	                        i1 = (int)((double)i1 * 3.28084D);
	                        s = "nm";
	                        s1 = "ft";
	                    }
	                    d = Math.round(d);
	                    float f4 = 0.0F;
	                    float f5 = 0.0F;
	                    if(f3 >= 0.0F && f3 < 90F)
	                    {
	                        f4 = 15F;
	                        f5 = -20F;
	                        if(f >= 270F && f <= 360F)
	                        {
	                            f4 = -35F * bigFontMultip;
	                            f5 = 40F * bigFontMultip;
	                        }
	                    } else
	                    if(f3 >= 90F && f3 < 180F)
	                    {
	                        f4 = 15F;
	                        f5 = 40F * bigFontMultip;
	                        if(f >= 180F && f < 270F)
	                        {
	                            f4 = -35F * bigFontMultip;
	                            f5 = -20F;
	                        }
	                    } else
	                    if(f3 >= 180F && f3 < 270F)
	                    {
	                        f4 = -35F * bigFontMultip;
	                        f5 = 40F * bigFontMultip;
	                        if(f >= 90F && f < 180F)
	                        {
	                            f4 = 15F;
	                            f5 = 40F * bigFontMultip;
	                        }
	                    } else
	                    if(f3 >= 270F && f3 <= 360F)
	                    {
	                        f4 = -35F * bigFontMultip;
	                        f5 = -20F;
	                        if(f >= 0.0F && f < 90F)
	                        {
	                            f4 = 15F;
	                            f5 = -20F;
	                        }
	                    }
	                    f = f3;
	                    waypointFont.output(0xff000000, f1 + f4, (f2 + f5) - 0.0F, 0.0F, l + ".");
	                    waypointFont.output(0xff000000, f1 + f4, (f2 + f5) - 12F * bigFontMultip, 0.0F, i1 + s1);
	                    waypointFont.output(0xff000000, f1 + f4, (f2 + f5) - 24F * bigFontMultip, 0.0F, (int)f3 + "\260");
	                    waypointFont.output(0xff000000, f1 + f4, (f2 + f5) - 36F * bigFontMultip, 0.0F, (int)d + s);
	                }
	            }
            }
        } while(true);
    }

    public GUIPad THIS()
    {
        return this;
    }

    public GUIPad(GWindowRoot gwindowroot)
    {
        FrameGapX = 0;
//        bPALZoomInUp = Config.cur.ini.get("Mods", "PALZoomInUp", false);
        frameRegion = new GRegion(0.05F, 0.1F, 0.35F, 0.6F);
        targets = new ArrayList();
        bigFontMultip = 1.0F;
        scales = scale.length;
        curScale = 0;
        curScaleDirect = 1;
        full = true;
        bActive = false;
        bUseMouseWheel = true;
        line2XYZ = new float[6];
        lineBeamXYZ = new float[9];
        lineBeamXYZ[6] = 0.0F;
        lineBeamXYZ[7] = 0.0F;
        _gridX = new int[20];
        _gridY = new int[20];
        _gridVal = new int[20];
        airdrome = new ArrayList();
        _army = new int[Army.amountNet()];
        armyAccum = new ArmyAccum();
        airs = new ArrayList();
        radarPlane = new ArrayList();
        radarOther = new ArrayList();
        _wayP = new Point3f();
        _wayP2 = new Point3f();
        lineNXYZ = new float[6];
        zutiColorAirfields = true;
        zutiPadObjects = new HashMap();
//        zutiPlayeAcDrawn = false;
        iconBornPlace = null;
        iconBornPlace = IconDraw.get("icons/born.mat");
        client = (GUIClient)gwindowroot.create(new GUIClient() {

            public void render()
            {
                int i = client.root.C.alpha;
                client.root.C.alpha = 255;
                super.render();
                client.root.C.alpha = i;
            }

        }
);
        frame = (GWindowFramed)client.create(0.0F, 0.0F, 1.0F, 1.0F, false, new GWindowFramed() {

            public void resized()
            {
                super.resized();
                if(full)
                {
                    if(renders != null)
                        renders.setPosSize(0.0F, 0.0F, win.dx, win.dy);
                    if(renders1 != null)
                        renders1.setPosSize(0.0F, 0.0F, win.dx, win.dy);
                } else
                {
	                if(renders != null)
	                    renders.setPosSize(win.dx * mapView[0], win.dy * mapView[1], win.dx * mapView[2], win.dy * mapView[3]);
	                if(renders1 != null)
	                    renders1.setPosSize(0.0F, 0.0F, win.dx, win.dy);
                }
            }

            public void render()
            {
            }

        }
);
        frame.bSizable = false;
        renders = new GUIRenders(frame, 0.0F, 0.0F, 1.0F, 1.0F, false);
        renders1 = new GUIRenders(frame, 0.0F, 0.0F, 1.0F, 1.0F, false) {

//            public void initHotKeys()
//            {
//                HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "popupmenu") {
//
//                    public void begin()
//                    {
//                        doPopUpMenu();
//                    }
//
//                }
//);
//            }

            public void doPopUpMenu()
            {
                if(popUpMenu == null)
                {
                    popUpMenu = (GWindowMenuPopUp)create(new GWindowMenuPopUp());
                    popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Normal Mode"), Plugin.i18n("Normal Mode")) {

                        public void execute()
                        {
                            GUIPadMode = 1;
                            leave(false);
                            enter(false);
                        }

                    }
);
                    popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Relative Mode"), Plugin.i18n("Relative Mode")) {

                        public void execute()
                        {
                            GUIPadMode = 2;
                            leave(false);
                            enter(false);
                        }

                    }
);
                    popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Navigation Map"), Plugin.i18n("Navigation Map")) {

                        public void execute()
                        {
                            GUIPadMode = 3;
                            leave(false);
                            enter(false);
                        }

                    }
);
                    popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Radar View (Radial)"), Plugin.i18n("Radar View (Radial)")) {

                        public void execute()
                        {
                            GUIPadMode = 4;
                            leave(false);
                            enter(false);
                        }

                    }
);
                    popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Radar View (Frontal)"), Plugin.i18n("Radar View (Frontal)")) {

                        public void execute()
                        {
                            GUIPadMode = 5;
                            leave(false);
                            enter(false);
                        }

                    }
);
                    popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Mission Brief"), Plugin.i18n("Mission Brief")) {

                        public void execute()
                        {
                            GUIPadMode = 6;
                            leave(false);
                            enter(false);
                        }

                    }
);
                    GPoint gpoint = getMouseXY();
                    popUpMenu.setPos(gpoint.x, gpoint.y);
                    for(int i = 1; i <= popUpMenu.size(); i++)
                        popUpMenu.getItem(i - 1).bChecked = i == GUIPadMode;

                } else
                {
                    if(popUpMenu.isVisible())
                        return;
                    if(popUpMenu.size() > 0)
                    {
                        GPoint gpoint1 = getMouseXY();
                        popUpMenu.setPos(gpoint1.x, gpoint1.y);
                        for(int j = 1; j <= popUpMenu.size(); j++)
                            popUpMenu.getItem(j - 1).bChecked = j == GUIPadMode;

                        popUpMenu.showModal();
                    }
                }
            }

            public void mouseButton(int i, boolean flag, float f, float f1)
            {
                if(i == 0)
                    bLPressed = flag;
                else
                if(i == 1)
                {
                    bRPressed = flag;
                    if(!bRPressed && !bLPressed)
                        doPopUpMenu();
                } else
                if(i == 2)
                    bMPressed = flag;
                if(bLPressed && bRPressed || bMPressed)
                    mouseCursor = 7;
                if((GUIPadMode == 1 || GUIPadMode == 4 || GUIPadMode == 5) && bLPressed && !bRPressed && !bMPressed)
                    mouseCursor = 3;
                if(!bLPressed && !bMPressed && !bRPressed)
                    mouseCursor = 1;
            }

//            public void mouseRelMovePAL(float f, float f1, float f2)
//            {
//                if(GUIPadMode == 6)
//                    return;
//                if(bRPressed || bLPressed || bMPressed)
//                    return;
//                GPoint gpoint = getMouseXY();
//                f = gpoint.x;
//                f1 = gpoint.y;
//                if(f2 != 0.0F)
//                {
//                    f -= THIS().renders.win.x;
//                    f1 -= THIS().renders.win.y;
//                    float f3 = (float)(cameraMap2D.worldXOffset + (double)f / cameraMap2D.worldScale);
//                    float f4 = (float)(cameraMap2D.worldYOffset + (double)(THIS().renders.win.dy - f1 - 1.0F) / cameraMap2D.worldScale);
//                    if(bPALZoomInUp)
//                        f2 = -f2;
//                    if(f2 > 0.0F && curScale < scales - 1)
//                        curScale++;
//                    if(f2 < 0.0F && curScale > 0)
//                        curScale--;
//                    scaleCamera();
//                    f3 = (float)((double)f3 - (double)(f - THIS().renders.win.dx / 2.0F) / cameraMap2D.worldScale);
//                    f4 = (float)((double)f4 + (double)(f1 - THIS().renders.win.dy / 2.0F) / cameraMap2D.worldScale);
//                    setPosCamera(f3, f4);
//                }
//            }

            public void mouseRelMove(float f, float f1, float f2)
            {
                if(!bUseMouseWheel)
                    return;
                if((double)f2 < 0.001D && (double)f2 > -0.001D)
                    return;
                if((double)f2 < 0.0D)
                    curScaleDirect = 1;
                if((double)f2 > 0.0D)
                    curScaleDirect = -1;
                float f3 = THIS().renders.root.mousePos.x - THIS().renders.win.x - THIS().renders.parentWindow.win.x;
                float f4 = THIS().renders.root.mousePos.y - THIS().renders.win.y - THIS().renders.parentWindow.win.y;
                float f5 = (float)(cameraMap2D.worldXOffset + (double)f3 / cameraMap2D.worldScale);
                float f6 = (float)(cameraMap2D.worldYOffset + (double)(THIS().renders.win.dy - f4 - 1.0F) / cameraMap2D.worldScale);
                curScale +=  curScaleDirect;
                if(curScaleDirect < 0)
                {
                    if(curScale < 0)
                        curScale = 0;
                } else
                if(curScale >= scales)
                    curScale = scales - 1;
                scaleCamera();
                f5 = (float)((double)f5 - (double)(f3 - THIS().renders.win.dx / 2.0F) / cameraMap2D.worldScale);
                f6 = (float)((double)f6 + (double)(f4 - THIS().renders.win.dy / 2.0F) / cameraMap2D.worldScale);
                setPosCamera(f5, f6);
            }

            public void mouseMove(float f, float f1)
            {
                if(bLPressed && bRPressed || bMPressed)
                    frame.setPos(frame.win.x + root.mouseStep.dx, frame.win.y + root.mouseStep.dy);
                else
                if(bLPressed && (GUIPadMode == 1 || GUIPadMode == 3))
                {
                    cameraMap2D.worldXOffset -= (double)root.mouseStep.dx / cameraMap2D.worldScale;
                    cameraMap2D.worldYOffset += (double)root.mouseStep.dy / cameraMap2D.worldScale;
                    clipCamera();
                }
            }

            boolean bLPressed;
            boolean bRPressed;
            boolean bMPressed;


            
            {
                bLPressed = false;
                bRPressed = false;
                bMPressed = false;
            }
        }
;
        cameraMap2D = new CameraOrtho2D();
        cameraMap2D.worldScale = scale[curScale];
        renderMap2D = new RenderMap2D(renders.renders, 1.0F);
        renderMap2D.setCamera(cameraMap2D);
        renderMap2D.setShow(true);
        cameraMap2D1 = new CameraOrtho2D();
        renderMap2D1 = new RenderMap2D1(renders1.renders, 1.0F);
        renderMap2D1.setCamera(cameraMap2D1);
        renderMap2D1.setShow(true);
        LightEnvXY lightenvxy = new LightEnvXY();
        renderMap2D.setLightEnv(lightenvxy);
        renderMap2D1.setLightEnv(lightenvxy);
        lightenvxy.sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
        Vector3f vector3f = new Vector3f(1.0F, -2F, -1F);
        vector3f.normalize();
        lightenvxy.sun().set(vector3f);
        gridFont = TTFont.font[1];
        setFont(World.cur().smallMapWPLabels);
        mesh = GMesh.New("gui/game/pad/mono.sim");
        redTint = GMesh.New("gui/game/pad/red.sim");
        redTintFull = GMesh.New("gui/game/pad/redFull.sim");
        _iconAir = Mat.New("icons/plane.mat");
        emptyMat = Mat.New("icons/empty.mat");
        _iconILS = Mat.New("icons/ILS.mat");
        main = Main3D.cur3D();
        client.hideWindow();
        smallFont = TTFont.font[0];
//        if(World.cur().smallMapWPLabels)
//        {
//            waypointFont = TTFont.font[0];
//            bigFontMultip = 1.0F;
//        } else
//        {
//            waypointFont = TTFont.font[1];
//            bigFontMultip = 2.0F;
//        }
//        mesh = GMesh.New("gui/game/pad/mono.sim");
//        _iconAir = Mat.New("icons/plane.mat");
//        emptyMat = Mat.New("icons/empty.mat");
//        _iconILS = Mat.New("icons/ILS.mat");
        meshradar = GMesh.New("gui/game/radar/mono.sim");
        meshradarF = GMesh.New("gui/game/radarF/mono.sim");
        meshbrief = GMesh.New("gui/game/brief/mono.sim");
        _iconAir = Mat.New("icons/plane.mat");
        _iconRadar = Mat.New("icons/radarimage.mat");
        _iconShipRadar = Mat.New("icons/radarshipimage.mat");
        _iconAirField = Mat.New("icons/airfield.mat");
//        main = Main3D.cur3D();
//        client.hideWindow();
    }

    public void setFont(boolean flag)
    {
        if(flag)
        {
            waypointFont = TTFont.font[0];
            bigFontMultip = 1.0F;
        } else
        {
            waypointFont = TTFont.font[1];
            bigFontMultip = 2.0F;
        }
    }
    
    public void zutiChangeAirportArmyAndColor(BornPlace bornplace)
    {
        if(bornplace == null)
            return;
        double d = bornplace.r * bornplace.r;
        int i = airdrome.size();
        for(int j = 0; j < i; j++)
        {
            AirDrome airdrome1 = (AirDrome)airdrome.get(j);
            if(airdrome1.airport == null)
                continue;
            Point3d point3d = airdrome1.airport.pos.getAbsPoint();
            double d1 = (point3d.x - bornplace.place.x) * (point3d.x - bornplace.place.x) + (point3d.y - bornplace.place.y) * (point3d.y - bornplace.place.y);
            if(d1 <= d)
            {
                airdrome1.army = bornplace.army;
                airdrome1.color = Army.color(airdrome1.army);
            }
        }

    }

    private void zutiDrawAirInterval()
    {
        boolean flag = World.cur().diffCur.NoMinimapPath;
        try
        {
            Iterator iterator = zutiPadObjects.keySet().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                ZutiPadObject zutipadobject = (ZutiPadObject)zutiPadObjects.get(iterator.next());
                if(zutipadobject != null && zutipadobject.getOwner() != null && zutipadobject.isAlive())
                {
                    if(!flag && zutipadobject.isPlayerPlane())
                        zutipadobject.setVisibleForPlayerArmy(true);
                    if(zutipadobject.isVisibleForPlayerArmy())
                    {
                        Point3d point3d = zutipadobject.getPosition();
                        float f = (float)((point3d.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
                        float f1 = (float)((point3d.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
                        IconDraw.setColor(Army.color(zutipadobject.getArmy()));
                        if(zutipadobject.isPlayerPlane())
                        {
//                            zutiPlayeAcDrawn = true;
                            IconDraw.setColor(-1);
                        }
                        switch(zutipadobject.type)
                        {
                        default:
                            break;

                        case 0: // '\0'
                        case 3: // '\003'
                            IconDraw.render(_iconAir, f, f1, zutipadobject.getAzimut());
                            break;

                        case 1: // '\001'
                        case 2: // '\002'
                        case 4: // '\004'
                        case 5: // '\005'
                            Mat mat = zutipadobject.getIcon();
                            if(mat != null)
                                IconDraw.render(mat, f, f1);
                            break;
                        }
                    }
                }
            } while(true);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void zutiChangeAirportArmyAndColor(AirDrome airdrome1)
    {
        if(airdrome1 == null || World.cur().bornPlaces == null)
            return;
        ArrayList arraylist = World.cur().bornPlaces;
        int i = arraylist.size();
        for(int j = 0; j < i; j++)
        {
            BornPlace bornplace = (BornPlace)arraylist.get(j);
            if(bornplace == null)
                continue;
            double d = bornplace.r * bornplace.r;
            Point3d point3d = airdrome1.airport.pos.getAbsPoint();
            double d1 = (point3d.x - bornplace.place.x) * (point3d.x - bornplace.place.x) + (point3d.y - bornplace.place.y) * (point3d.y - bornplace.place.y);
            if(d1 <= d)
            {
                airdrome1.army = bornplace.army;
                airdrome1.color = Army.color(airdrome1.army);
            }
        }

    }

    private boolean useNightLight()
    {
        return World.Sun().ToSun.z < 0.0F && Main3D.cur3D().cockpitCur != null && Main3D.cur3D().cockpitCur.cockpitLightControl;
    }

    public static boolean bStartMission = true;
    private Main3D main;
    public GUIClient client;
    public GWindowFramed frame;
    public GRegion frameRegion;
    public float mapView[] = {
        0.01F, 0.01F, 0.98F, 0.98F
    };
    public GUIRenders renders;
    public GUIRenders renders1;
    public RenderMap2D renderMap2D;
    public CameraOrtho2D cameraMap2D;
    public RenderMap2D1 renderMap2D1;
    public CameraOrtho2D cameraMap2D1;
    public boolean savedUseMesh;
    public int saveIconDx;
    public int saveIconDy;
    protected ArrayList targets;
    public GMesh mesh;
    public GMesh redTint;
    public GMesh redTintFull;
    public GMesh meshradar;
    public GMesh meshradarF;
    public GMesh meshbrief;
    public TTFont gridFont;
    public TTFont waypointFont;
    public TTFont smallFont;
    public Mat emptyMat;
    public Mat _iconAir;
    public Mat _iconILS;
    public Mat _iconRadar;
    public Mat _iconShipRadar;
    public Mat _iconAirField;
    private float bigFontMultip;
    private int lastScale;
    private float scale[] = {
        0.064F, 0.032F, 0.016F, 0.008F, 0.004F, 0.002F, 0.001F, 0.0005F, 0.00025F
    };
    private int scales;
    private int curScale;
    private int curScaleDirect;
    private boolean full;
    public boolean bActive;
    private boolean bUseMouseWheel;
    private float line2XYZ[];
    private int _gridCount;
    private int _gridX[];
    private int _gridY[];
    private int _gridVal[];
    private ArrayList airdrome;
    int _army[];
    ArmyAccum armyAccum;
    private ArrayList airs;
    private Point3f _wayP;
    private Point3f _wayP2;
    private float lineNXYZ[];
    private int lineNCounter;
    public boolean zutiColorAirfields;
    public Map zutiPadObjects;
//    private boolean zutiPlayeAcDrawn;
//    private ArrayList zutiNeutralHomeBases;
    protected Mat iconBornPlace;
    private Point3d OwnPos3d;
    private double OwnAzimut;
    private float OwnAngle;
    private int GUIPadMode;
    private GWindowMenuPopUp popUpMenu;
    private int FrameOriginX;
    private int FrameOriginY;
    private int FrameGapX;
    private int to;
    private float lineBeamXYZ[];
    private String textDescription;
    private ArrayList radarPlane;
    private ArrayList radarOther;
//    private boolean bPALZoomInUp;









































}
