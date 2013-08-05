//////////////////////////////////////////////////////////////////////////////////////////
//   FMBFull MODded
//   with changes from 4.111 / 4.101 MODded by PAL to implement mouse wheel, etc.
//   lifted to 4.12 by SAS~Storebror
/////////////////////////////////////////////////////////////////////////////////////////

package com.maddox.il2.builder;

import java.util.AbstractCollection;
import java.util.ArrayList;

import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.gwindow.GPoint;
import com.maddox.gwindow.GRegion;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowHScrollBar;
import com.maddox.gwindow.GWindowMenu;
import com.maddox.gwindow.GWindowMenuBarItem;
import com.maddox.gwindow.GWindowMenuItem;
import com.maddox.gwindow.GWindowMenuPopUp;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRootMenu;
import com.maddox.gwindow.GWindowVScrollBar;
import com.maddox.gwindow.GWindowVSliderInt;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorFilter;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.engine.ActorPosStatic;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.CameraOrtho2D;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.GUIWindowManager;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.engine.TextScr;
import com.maddox.il2.engine.hotkey.MouseXYZATK;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.gui.SquareLabels;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.air.Runaway;
import com.maddox.il2.objects.bridges.Bridge;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.vehicles.planes.PlaneGeneric;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.HotKeyCmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.MsgTimerListener;
import com.maddox.rts.MsgTimerParam;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SFSInputStream;
import com.maddox.rts.SectFile;
import com.maddox.rts.SoftClass;
import com.maddox.rts.Time;
import com.maddox.util.HashMapExt;

public class Builder
{
    public class ClientWindow extends GWindow
    {

        public void resized()
        {
            GRegion gregion = parentWindow.getClientRegion();
            win.set(gregion);
            mapXscrollBar.setSize(win.dx, lookAndFeel().getHScrollBarH());
            mapXscrollBar.setPos(0.0F, win.dy - lookAndFeel().getHScrollBarH());
            mapYscrollBar.setSize(lookAndFeel().getVScrollBarW(), win.dy - lookAndFeel().getHScrollBarH());
            mapYscrollBar.setPos(win.dx - lookAndFeel().getVScrollBarW(), 0.0F);
            mapZSlider.setSize(lookAndFeel().getVSliderIntW(), win.dy - lookAndFeel().getHScrollBarH());
            mapZSlider.setPos(0.0F, 0.0F);
            viewWindow.setPos(lookAndFeel().getVSliderIntW(), 0.0F);
            viewWindow.setSize(win.dx - 2.0F * lookAndFeel().getVScrollBarW(), win.dy - lookAndFeel().getHScrollBarH());
            if(isLoadedLandscape())
                computeViewMap2D(viewH, camera2D.worldXOffset + viewX / 2D, camera2D.worldYOffset + viewY / 2D);
        }

        public void resolutionChanged()
        {
            resized();
        }

        public ClientWindow()
        {
        }
    }

    public class ViewWindow extends GWindow
    {
        public void mouseMove(float f, float f1)
        {
            f1 = win.dy - f1 - 1.0F;
            doMouseAbsMove((int)f, (int)f1);
        }
            	
	/*	//By PAL, to detect Mouse Wheel
		public void mouseRelMove(float f, float f1, float f2)
		{
	    	if (f2 != 0.0F)
	    	{
	    		if (bPALZoomInUp) mapZSlider.setPos((int)(mapZSlider.pos() - f2 * 40.0F), true);
	    		  else mapZSlider.setPos((int)(mapZSlider.pos() + f2 * 40.0F), true);
	    		//mapZSlider.setPos((int)(Math.sqrt(viewH) * 100D), false);
	    	}
		}*/
		
        public void mouseRelMove(float f, float f1, float f2) //By PAL, from 4.11
        {
            boolean flag = true;
            if((double)f2 < 0.001D && (double)f2 > -0.001D)
                return;
            if((double)f2 > 0.0D)
                flag = false;
            Point3d point3d = new Point3d();
            point3d.set(posScreenToLand(mousePosX, mousePosY, 0.0D, 0.10000000000000001D));
            mapZSlider.setScrollPos(flag, true);
            Point3d point3d1 = new Point3d();
            point3d1.set(posScreenToLand(mousePosX, mousePosY, 0.0D, 0.10000000000000001D));
            camera2D.worldXOffset += point3d.x - point3d1.x;
            camera2D.worldYOffset += point3d.y - point3d1.y;
            worldScrool(0.0D, 0.0D);
        }		
		
        public void keyFocusEnter()
        {
            HotKeyCmdEnv.enable(Builder.envName, true);
        }

        public void keyFocusExit()
        {
            HotKeyCmdEnv.enable(Builder.envName, false);
        }

        public ViewWindow()
        {
        }
    }

    public class ZSlider extends GWindowVSliderInt
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                if(isLoadedLandscape())
                {
                    double d = (double)pos() / 100D;
                    double d1 = d * d;
                    computeViewMap2D(d1, camera2D.worldXOffset + viewX / 2D, camera2D.worldYOffset + viewY / 2D);
                }
                return true;
            } else
            {
                return false;
            }
        }

        public void setScrollPos(boolean flag, boolean flag1)
        {
            int i = posCount / 64;
            if(i <= 0)
                i = 1;
            setPos(pos + (flag ? i : -i), flag1);
        }

        public ZSlider(GWindow gwindow)
        {
            super(gwindow);
        }
    }

    public class YScrollBar extends GWindowVScrollBar
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                if(isLoadedLandscape())
                    worldScrool(0.0D, Main3D.cur3D().land2D.mapSizeY() - viewY - (double)(pos() / 100F) - camera2D.worldYOffset - Main3D.cur3D().land2D.worldOfsY());
                return true;
            }
            if(i == 17)
                return super.notify(i, j);
            else
                return false;
        }

        public YScrollBar(GWindow gwindow)
        {
            super(gwindow);
        }
    }

    public class XScrollBar extends GWindowHScrollBar
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                if(isLoadedLandscape())
                    worldScrool((double)(pos() / 100F) - camera2D.worldXOffset - Main3D.cur3D().land2D.worldOfsX(), 0.0D);
                return true;
            } else
            {
                return false;
            }
        }

        public XScrollBar(GWindow gwindow)
        {
            super(gwindow);
        }
    }

    class AnimateView
        implements MsgTimerListener
    {

        public void msgTimer(MsgTimerParam msgtimerparam, int i, boolean flag, boolean flag1)
        {
            float f = (float)(i + 1) / 100F;
            cur.interpolate(from, to, f);
            camera.pos.setAbs(cur);
            if(flag1)
            {
                if(aView != null)
                    camera.pos.setBase(aView, Main3D.cur3D().hookView, false);
                else
                    endClearActorView();
                HotKeyCmdEnv.enable(Builder.envName, true);
            }
        }

        Actor aView;
        Loc from;
        Loc to;
        Loc cur;

        public AnimateView(Actor actor, Loc loc, Loc loc1)
        {
            cur = new Loc();
            aView = actor;
            from = loc;
            to = loc1;
            if(actor != null)
                camera.pos.setBase(null, null, false);
            HotKeyCmdEnv.enable(Builder.envName, false);
            RTSConf.cur.realTimer.msgAddListener(this, new MsgTimerParam(0, 0, Time.currentReal(), 100, 10, true, true));
        }
    }

    class FilterSelect
        implements ActorFilter
    {

        public boolean isUse(Actor actor, double d)
        {
            if(!conf.bEnableSelect)
                return false;
            if(!Property.containsValue(actor, "builderSpawn"))
                return false;
            if(actor instanceof ActorLabel)
                return false;
            if(actor instanceof BridgeSegment)
                return false;
            if(actor instanceof Bridge)
                return false;
            Point3d point3d = actor.pos.getAbsPoint();
            if(point3d.x < _selectX0 || point3d.x > _selectX1)
                return false;
            if(point3d.y < _selectY0 || point3d.y > _selectY1)
                return false;
            if(_bSelect)
                selectedActors.put(actor, null);
            else
                selectedActors.remove(actor);
            return true;
        }

        FilterSelect()
        {
        }
    }

    class InterpolateOnLand extends Interpolate
    {

        public boolean tick()
        {
            Actor actor = selectedActor();
            if(!Actor.isValid(actor))
                return true;
            if(isFreeView() && bMultiSelect)
            {
                actor.pos.getAbs(__pi, __oi);
                double d = Engine.land().HQ(__pi.x, __pi.y);
                double d1 = __pi.z - d;
                boolean flag = Engine.land().isWater(__pi.x, __pi.y);
                int i = (int)d1;
                if(d1 < 0.0D)
                    d1 = -d1;
                int j = (int)(d1 * 100D) % 100;
                int k = TextScr.font().height() - TextScr.font().descender();
				TextScr.output(5, 5, "curPos: " + (int)__pi.x + " " + (int)__pi.y + " H= " + i + "." + j + (flag ? " water HLand:" : " land HLand:") + (float)d + " type=" + Landscape.getPixelMapT(Engine.land().WORLD2PIXX(__pi.x), Engine.land().WORLD2PIXY(__pi.y)));
                TextScr.output(5, 5 + k, "Orient: " + (int)__oi.azimut() + " " + (int)__oi.tangage() + " " + (int)__oi.kren());
                Point3d point3d = this.actor.pos.getAbsPoint();
                TextScr.output(5, 5 + 2 * k, "Distance: " + (int)point3d.distance(__pi));
            }
            align(actor);
            return true;
        }

        InterpolateOnLand()
        {
        }
    }

    class SelectFilter
        implements ActorFilter
    {

        public void reset(double d)
        {
            _Actor = null;
            _maxLen2 = d;
        }

        public Actor get()
        {
            return _Actor;
        }

        public boolean isUse(Actor actor, double d)
        {
            if(!conf.bEnableSelect)
                return true;
            if(d <= _maxLen2)
            {
                if(actor instanceof BridgeSegment)
                    if(conf.bViewBridge)
                        actor = actor.getOwner();
                    else
                        return true;
                if(actor instanceof Bridge)
                {
                    if(!conf.bViewBridge)
                        return true;
                } else
                if(actor instanceof PPoint)
                {
                    if(isFreeView())
                        return true;
                    Path path = (Path)actor.getOwner();
                    if(!path.isDrawing())
                        return true;
                } else
                if(!Property.containsValue(actor, "builderSpawn"))
                    return true;
                if(_Actor == null)
                {
                    _Actor = actor;
                    _Len2 = d;
                } else
                if(d < _Len2)
                {
                    _Actor = actor;
                    _Len2 = d;
                }
            }
            return true;
        }

        private Actor _Actor;
        private double _Len2;
        private double _maxLen2;

        SelectFilter()
        {
            _Actor = null;
        }
    }


    public static int armyAmount()
    {
        return Army.amountSingle();
    }

    public static int colorSelected()
    {
        long l = Time.currentReal();
        long l1 = 1000L;
        double d = (2D * (double)(l % l1)) / (double)l1;
        if(d >= 1.0D)
            d = 2D - d;
        int i = (int)(255D * d);
        return 0xff000000 | i << 16 | i << 8 | i;
    }

    public static int colorMultiSelected(int i)
    {
        if(i == -1)
            i = 0xff000000;
        long l = Time.currentReal();
        long l1 = 1000L;
        double d = (2D * (double)(l % l1)) / (double)l1;
        if(d >= 1.0D)
            d = 2D - d;
        int j = (int)(255D * d);
        return 0xff000000 | j << 16 | j << 8 | j | i;
    }

    public boolean isLoadedLandscape()
    {
        return PlMapLoad.getLandLoaded() != null;
    }

    public boolean isFreeView()
    {
        return bFreeView;
    }

    public boolean isView3D()
    {
        return bView3D;
    }

    public double viewDistance()
    {
        return viewDistance;
    }

    public void computeViewMap2D(double d, double d1, double d2)
    {
        if(!isLoadedLandscape())
            return;
        int i = (int)viewWindow.win.dx;
        int j = (int)viewWindow.win.dy;
        double d3 = ((double)camera.FOV() * 3.1415926535897931D) / 180D / 2D;
        double d4 = (double)i / (double)j;
        if(d < 0.0D)
        {
            viewHMax = Main3D.cur3D().land2D.mapSizeX() / 2D / Math.tan(d3);
            double d5 = (Main3D.cur3D().land2D.mapSizeY() / 2D / Math.tan(d3)) * d4;
            if(d5 < viewHMax)
                viewHMax = d5;
            /*int k = (int)(Math.sqrt(1.0D) * 100D);
            int l = (int)(Math.sqrt(viewHMax) * 100D);
            mapZSlider.setRange(k, (l - k) + 1, k);*/
            //By PAL, from 4.11
            byte byte0 = 100;
            int k = (int)(Math.sqrt(viewHMax) * 100D);
            mapZSlider.setRange(byte0, (k - byte0) + 1, byte0);            
            d = viewHMax;
            d1 = Main3D.cur3D().land2D.mapSizeX() / 2D - Main3D.cur3D().land2D.worldOfsX();
            d2 = Main3D.cur3D().land2D.mapSizeY() / 2D - Main3D.cur3D().land2D.worldOfsY();
        }
        double d6 = Engine.land().HQ(d1, d2);
        if(d < 50D + d6)
            d = 50D + d6;
        if(d > viewHMax)
            d = viewHMax;
        if(viewH != d)
        {
            viewH = d;
            mapZSlider.setPos((int)(Math.sqrt(viewH) * 100D), false);
        }
        viewHLand = viewH - d6;
        camera2D.worldScale = (double)i / (2D * viewH * Math.tan(d3));
        boolean flag = (int)(Main3D.cur3D().land2D.mapSizeX() * camera2D.worldScale + 0.5D) > i;
        boolean flag1 = (int)(Main3D.cur3D().land2D.mapSizeY() * camera2D.worldScale + 0.5D) > j;
        viewX = (double)i / camera2D.worldScale;
        if(flag)
        {
            camera2D.worldXOffset = d1 - viewX / 2D;
            if(camera2D.worldXOffset < -Main3D.cur3D().land2D.worldOfsX())
                camera2D.worldXOffset = -Main3D.cur3D().land2D.worldOfsX();
            if(camera2D.worldXOffset > Main3D.cur3D().land2D.mapSizeX() - Main3D.cur3D().land2D.worldOfsX() - viewX)
                camera2D.worldXOffset = Main3D.cur3D().land2D.mapSizeX() - Main3D.cur3D().land2D.worldOfsX() - viewX;
            mapXscrollBar.setRange(0.0F, (int)(Main3D.cur3D().land2D.mapSizeX() * 100D), (int)(viewX * 100D), (int)((viewX * 100D) / 64D), (int)((camera2D.worldXOffset + Main3D.cur3D().land2D.worldOfsX()) * 100D));
        } else
        {
            camera2D.worldXOffset = -(viewX - Main3D.cur3D().land2D.mapSizeX()) / 2D - Main3D.cur3D().land2D.worldOfsX();
            mapXscrollBar.setRange(0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
        }
        viewY = (double)j / camera2D.worldScale;
        if(flag1)
        {
            camera2D.worldYOffset = d2 - viewY / 2D;
            if(camera2D.worldYOffset < -Main3D.cur3D().land2D.worldOfsY())
                camera2D.worldYOffset = -Main3D.cur3D().land2D.worldOfsY();
            if(camera2D.worldYOffset > Main3D.cur3D().land2D.mapSizeY() - Main3D.cur3D().land2D.worldOfsY() - viewY)
                camera2D.worldYOffset = Main3D.cur3D().land2D.mapSizeY() - Main3D.cur3D().land2D.worldOfsY() - viewY;
            mapYscrollBar.setRange(0.0F, (int)(Main3D.cur3D().land2D.mapSizeY() * 100D), (int)(viewY * 100D), (int)((viewY * 100D) / 64D), (int)((Main3D.cur3D().land2D.mapSizeY() - viewY - camera2D.worldYOffset - Main3D.cur3D().land2D.worldOfsY()) * 100D));
        } else
        {
            camera2D.worldYOffset = -(viewY - Main3D.cur3D().land2D.mapSizeY()) / 2D - Main3D.cur3D().land2D.worldOfsY();
            mapYscrollBar.setRange(0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
        }
        double d7 = Math.tan(d3) * viewH;
        double d8 = d7 / d4;
        double d9 = Math.sqrt(d7 * d7 + d8 * d8);
        viewDistance = Math.sqrt(viewH * viewH + d9 * d9);
        if(viewDistance > (double)MaxVisualDistance)
        {
            bView3D = false;
            Main3D.cur3D().land2D.show(conf.bShowLandscape);
            Main3D.cur3D().renderMap2D.useClearColor(true);
            Main3D.cur3D().render3D0.setShow(false);
            Main3D.cur3D().render3D1.setShow(false);
            Main3D.cur3D().render2D.setShow(false);
        } else
        {
            bView3D = true;
            Main3D.cur3D().land2D.show(false);
            Main3D.cur3D().renderMap2D.useClearColor(false);
            Main3D.cur3D().render3D0.setShow(true);
            Main3D.cur3D().render3D1.setShow(true);
            Main3D.cur3D().render2D.setShow(true);
        }
        setPosCamera3D();
        repaint();
    }

    private void setPosCamera3D()
    {
        _camPoint.z = viewH;
        _camPoint.x = camera2D.worldXOffset + (double)(camera2D.right - camera2D.left) / camera2D.worldScale / 2D;
        _camPoint.y = camera2D.worldYOffset + (double)(camera2D.top - camera2D.bottom) / camera2D.worldScale / 2D;
        camera.pos.setAbs(_camPoint, _camOrient);
        camera.pos.reset();
    }

    public double posX2DtoWorld(int i)
    {
        return camera2D.worldXOffset + (double)i / camera2D.worldScale;
    }

    public double posY2DtoWorld(int i)
    {
        return camera2D.worldYOffset + (double)i / camera2D.worldScale;
    }

    public Point3d posScreenToLand(int i, int j, double d, double d1)
    {
        Point3d point3d = __posScreenToLand;
        if(bView3D)
        {
            double d2 = camera2D.worldXOffset + (double)(camera2D.right - camera2D.left) / camera2D.worldScale / 2D;
            double d3 = camera2D.worldYOffset + (double)(camera2D.top - camera2D.bottom) / camera2D.worldScale / 2D;
            point3d.x = posX2DtoWorld(i);
            point3d.y = posY2DtoWorld(j);
            point3d.z = Engine.land().HQ(point3d.x, point3d.y) + d;
            if(point3d.z > viewH)
                point3d.z = viewH;
            double d4 = (point3d.x - d2) / viewH;
            double d5 = (point3d.y - d3) / viewH;
            double d6 = 0.0D;
            double d9 = (point3d.z - d6) * (point3d.z - d6);
            for(int k = 0; k < 8 && d9 > d1; k++)
            {
                double d7 = point3d.z;
                point3d.x = (viewH - d7) * d4 + d2;
                point3d.y = (viewH - d7) * d5 + d3;
                point3d.z = Engine.land().HQ(point3d.x, point3d.y) + d;
                if(point3d.z > viewH)
                    point3d.z = viewH;
                d9 = (point3d.z - d7) * (point3d.z - d7);
            }

            for(int l = 0; l < 8 && d9 > d1; l++)
            {
                double d8 = point3d.z;
                point3d.x = ((viewH - d8) * d4 + d2 + point3d.x) / 2D;
                point3d.y = ((viewH - d8) * d5 + d3 + point3d.y) / 2D;
                point3d.z = Engine.land().HQ(point3d.x, point3d.y) + d;
                if(point3d.z > viewH)
                    point3d.z = viewH;
                d9 = (point3d.z - d8) * (point3d.z - d8);
            }

        } else
        {
            point3d.x = posX2DtoWorld(i);
            point3d.y = posY2DtoWorld(j);
            point3d.z = Engine.land().HQ(point3d.x, point3d.y);
        }
        return point3d;
    }

    public Point3d mouseWorldPos()
    {
        return posScreenToLand(mousePosX, mousePosY, 0.0D, 0.10000000000000001D);
    }

    public Actor selectNear(Point3d point3d)
    {
        Actor actor = selectNear(point3d, 100D);
        if(actor != null)
            return actor;
        else
            return selectNear(point3d, 10000D);
    }

    public Actor selectNearFull(int i, int j)
    {
        if(i < 0 || j < 0)
        {
            return null;
        } else
        {
            Point3d point3d = posScreenToLand(i, j, 0.0D, 0.10000000000000001D);
            return selectNear(point3d);
        }
    }

    public Actor selectNear(int i, int j)
    {
        if(i < 0 || j < 0)
            return null;
        Point3d point3d = posScreenToLand(i, j, 0.0D, 0.10000000000000001D);
        double d = viewH - point3d.z;
        if(d < 0.001D)
            d = 0.001D;
        double d1 = ((double)conf.iconSize * d) / viewH / camera2D.worldScale / 2D;
        return selectNear(point3d, d1);
    }

    public Actor selectNear(Point3d point3d, double d)
    {
        _selectFilter.reset(d * d);
        Engine.drawEnv().getFiltered((AbstractCollection)null, point3d.x - d, point3d.y - d, point3d.x + d, point3d.y + d, 15, _selectFilter);
        return _selectFilter.get();
    }

    private void worldScrool(double d, double d1)
    {
        double d2 = camera2D.worldXOffset + viewX / 2D + d;
        double d3 = camera2D.worldYOffset + viewY / 2D + d1;
        double d4 = Engine.land().HQ(d2, d3);
        double d5 = viewH;
        if(conf.bSaveViewHLand)
            d5 = d4 + viewHLand;
        computeViewMap2D(d5, d2, d3);
    }

    public void align(Actor actor)
    {
        if(actor instanceof ActorAlign)
        {
            ((ActorAlign)actor).align();
        } else
        {
            actor.pos.getAbs(__pi);
            double d = Engine.land().HQ(__pi.x, __pi.y) + 0.20000000000000001D;
            __pi.z = d;
            actor.pos.setAbs(__pi);
        }
    }

    public void deleteAll()
    {
        setSelected(null);
        Plugin.doDeleteAll();
        Plugin.doAfterDelete();
        selectedActorsValidate();
        pathes.clear();
    }

    public int countSelectedActors()
    {
        //By PAL; This corresponds? Not in 4.111: if(bMultiSelect);; //By PAL; this corresponds????
        if(Actor.isValid(selectedActor))
        {
            if(selectedActors.containsKey(selectedActor))
                return selectedActors.size();
            else
                return selectedActors.size() + 1;
        } else
        {
            return selectedActors.size();
        }
    }

    public void selectActorsClear()
    {
        //if(bMultiSelect) //By PAL, do this corresponds? 
        selectedActors.clear();
    }

    public void selectActorsAdd(Actor actor)
    {
        //if(bMultiSelect) //By PAL, do this corresponds?
        selectedActors.put(actor, null);
    }

    public Actor[] selectedActors()
    {
        //By PAL; this corresponds???? if(bMultiSelect);;
        int i = countSelectedActors();
        Actor aactor[] = _selectedActors(i <= 0 ? 1 : i);
        int j = 0;
        if(Actor.isValid(selectedActor))
            aactor[j++] = selectedActor;
        if(selectedActors.size() > 0)
        {
            for(java.util.Map.Entry entry = selectedActors.nextEntry(null); entry != null; entry = selectedActors.nextEntry(entry))
            {
                Actor actor = (Actor)entry.getKey();
                if(Actor.isValid(actor) && actor != selectedActor)
                    aactor[j++] = actor;
            }

        }
        if(j == 0)
            aactor[0] = null;
        else
        if(aactor.length > j)
            aactor[j] = null;
        return aactor;
    }

    private Actor[] _selectedActors(int i)
    {
        if(_selectedActors == null || _selectedActors.length < i)
            _selectedActors = new Actor[i];
        return _selectedActors;
    }

    public void selectedActorsValidate()
    {
        Actor aactor[] = selectedActors();
        for(int i = 0; i < aactor.length; i++)
        {
            Actor actor = aactor[i];
            if(actor == null)
                break;
            if(!Actor.isValid(actor) || !actor.isDrawing())
                selectedActors.remove(actor);
        }

    }

    public void select(double d, double d1, double d2, double d3, boolean flag)
    {
        if(d2 > d)
        {
            _selectX0 = d;
            _selectX1 = d2;
        } else
        {
            _selectX0 = d2;
            _selectX1 = d;
        }
        if(d3 > d1)
        {
            _selectY0 = d1;
            _selectY1 = d3;
        } else
        {
            _selectY0 = d3;
            _selectY1 = d1;
        }
        _bSelect = flag;
        Engine.drawEnv().getFiltered((AbstractCollection)null, _selectX0, _selectY0, _selectX1, _selectY1, 15, filterSelect);
    }

    public boolean isMiltiSelected(Actor actor)
    {
        //By PAL; this corresponds???? if(bMultiSelect);;
        return selectedActors.containsKey(actor);
    }

    public boolean isSelected(Actor actor)
    {
        //By PAL; this corresponds???? if(bMultiSelect);;
        if(actor == selectedActor)
            return true;
        else
            return selectedActors.containsKey(actor);
    }

    public Actor selectedActor()
    {
        return selectedActor;
    }

    public PPoint selectedPoint()
    {
        if(pathes == null)
            return null;
        else
            return pathes.currentPPoint;
    }

//By PAL, from 4.11
    public PPoint selectedFirstPoint()
    {
        Path path = selectedPath();
        if(path != null)
            return path.point(0);
        else
            return null;
    }

    public Path selectedPath()
    {
        if(pathes == null)
            return null;
        if(!Actor.isValid(pathes.currentPPoint))
            return null;
        else
            return (Path)selectedPoint().getOwner();
    }

    public void setSelected(Actor actor)
    {
        if(!conf.bEnableSelect)
            return;
        if(Actor.isValid(selectedActor))
        {
            Plugin plugin = (Plugin)Property.value(selectedActor, "builderPlugin");
            if(plugin instanceof PlMisStatic)
                defaultAzimut = selectedActor.pos.getAbsOrient().azimut();
        }
        int i = wSelect.tabsClient.getCurrent();
        wSelect.clearExtendTabs();
        if(actor != null && (actor instanceof PPoint))
        {
            pathes.currentPPoint = (PPoint)actor;
            selectedActor = null;
            Plugin plugin1 = (Plugin)Property.value(actor.getOwner(), "builderPlugin");
            plugin1.syncSelector();
            if(actor instanceof PAir)
                tip(Plugin.i18n("Selected") + " " + ((PathAir)actor.getOwner()).typedName);
            else
            if(actor instanceof PNodes)
                tip(Plugin.i18n("Selected") + " " + Property.stringValue(actor.getOwner(), "i18nName", ""));
        } else
        {
            if(pathes != null)
                pathes.currentPPoint = null;
            selectedActor = actor;
            if(isFreeView())
                setActorView(actor);
            if(actor != null)
            {
                Plugin plugin2 = (Plugin)Property.value(actor, "builderPlugin");
                if(plugin2 != null)
                {
                    plugin2.syncSelector();
                    if(plugin2 instanceof PlMisStatic)
                        defaultAzimut = actor.pos.getAbsOrient().azimut();
                } else
                if(bMultiSelect) //By PAL, from 4.111 corresponds?
                {
                    Plugin plugin3 = Plugin.getPlugin("MapActors");
                    plugin3.syncSelector();
                }
                String s = null;
                if(bMultiSelect) //By PAL, from 4.111 corresponds?
                {
                    s = (actor instanceof SoftClass) ? ((SoftClass)actor).fullClassName() : actor.getClass().getName();
                    int j = s.lastIndexOf('.');
                    s = s.substring(j + 1);
                } else
                {
                    s = Property.stringValue(actor.getClass(), "i18nName", "");
                }
                tip(Plugin.i18n("Selected") + " " + s);
            } else
            {
                tip("");
            }
        }
        if(i > 0 && i < wSelect.tabsClient.sizeTabs())
            wSelect.tabsClient.setCurrent(i);
    }

    public void doUpdateSelector()
    {
        if(Actor.isValid(pathes.currentPPoint))
        {
            Plugin plugin = (Plugin)Property.value(pathes.currentPPoint.getOwner(), "builderPlugin");
            plugin.updateSelector();
        }
    }

    private void setActorView(Actor actor)
    {
        if(!Actor.isValid(actor))
            return;
        if(actorView() != actor)
        {
            if(actor.pos instanceof ActorPosStatic)
            {
                boolean flag = actor.isCollide();
                actor.collide(false);
                actor.drawing(false);
                actor.pos = new ActorPosMove(actor.pos);
                actor.drawing(true);
                if(flag)
                    actor.collide(true);
            }
            mouseXYZATK.setTarget(actor);
            camera.pos.setBase(actor, Main3D.cur3D().hookView, false);
            camera.pos.reset();
            cursor.drawing(actor == cursor);
        }
    }

    private void setActorView()
    {
        pathes.currentPPoint = null;
        bFreeView = true;
        saveMouseMode = RTSConf.cur.getUseMouse();
        if(saveMouseMode != 2)
            RTSConf.cur.setUseMouse(2);
        camera.pos.getAbs(_savCameraNoFreeLoc);
        Object obj;
        if(Actor.isValid(selectedActor()))
        {
            obj = selectedActor();
        } else
        {
            obj = cursor;
            selectedActor = ((Actor) (obj));
            Point3d point3d = new Point3d();
            camera.pos.getAbs(point3d);
            point3d.z = Engine.land().HQ(point3d.x, point3d.y) + 0.20000000000000001D;
            ((Actor) (obj)).pos.setAbs(point3d);
        }
        setActorView(((Actor) (obj)));
        clientWindow.root.manager.activateMouse(false);
        clientWindow.root.manager.activateKeyboard(false);
        HotKeyCmdEnv.enable("HookView", true);
        HotKeyCmdEnv.enable("MouseXYZ", true);
        viewWindow.mouseCursor = 0;
        if(!bMultiSelect)
        {
            Main3D.cur3D().spritesFog.setShow(true);
            if(Main3D.cur3D().clouds != null)
            {
                Main3D.cur3D().bDrawClouds = true;
                Main3D.cur3D().clouds.setShow(true);
            }
            Main3D.cur3D().bEnableFog = true;
        }
        if(conf.bAnimateCamera)
        {
            camera.pos.getAbs(_savCameraFreeLoc);
            new AnimateView(((Actor) (obj)), _savCameraNoFreeLoc, _savCameraFreeLoc);
        }
    }

    private void clearActorView()
    {
        camera.pos.getAbs(_savCameraFreeLoc);
        mouseXYZATK.setTarget(null);
        camera.pos.setBase(null, null, false);
        computeViewMap2D(_savCameraNoFreeLoc.getZ(), _savCameraFreeLoc.getX(), _savCameraFreeLoc.getY());
        camera.pos.getAbs(_savCameraNoFreeLoc);
        if(conf.bAnimateCamera)
            new AnimateView(null, _savCameraFreeLoc, _savCameraNoFreeLoc);
        else
            endClearActorView();
    }

    private void endClearActorView()
    {
        bFreeView = false;
        if(saveMouseMode != 2)
            RTSConf.cur.setUseMouse(saveMouseMode);
        cursor.drawing(false);
        if(selectedActor() == cursor)
            setSelected(null);
        selectedActorsValidate();
        clientWindow.root.manager.activateMouse(true);
        clientWindow.root.manager.activateKeyboard(true);
        HotKeyCmdEnv.enable("HookView", false);
        HotKeyCmdEnv.enable("MouseXYZ", false);
        viewWindow.mouseCursor = 1;
        PlMission.setChanged();
        if(!bMultiSelect)
        {
            Main3D.cur3D().spritesFog.setShow(false);
            if(Main3D.cur3D().clouds != null)
                Main3D.cur3D().clouds.setShow(false);
            Main3D.cur3D().bEnableFog = false;
        }
        repaint();
    }

    private Actor actorView()
    {
        return camera.pos.base();
    }

    public void doMouseAbsMove(int i, int j)
    {
        if(!isLoadedLandscape())
            return;
        if(isFreeView())
            return;
        if(mousePosX == -1)
        {
            mousePosX = i;
            mousePosY = j;
            return;
        }
        Point3d point3d = posScreenToLand(mousePosX, mousePosY, 0.0D, 0.10000000000000001D);
        switch(mouseState)
        {
        case 2: // '\002'
        case 3: // '\003'
        case 5: // '\005' //By PAL, from 4.11        
        default:
            break;

        case 1: // '\001'
            double d = camera2D.worldScale;
            if(bView3D)
            {
                double d1 = viewH - point3d.z;
                if(d1 < 0.001D)
                    d1 = 0.001D;
                d *= viewH / d1;
            }
            if(movedActor == null)
            {
                worldScrool((double)(mousePosX - i) / d, (double)(mousePosY - j) / d);
            } else
            {
                double d2 = (double)(i - mousePosX) / d;
                double d3 = (double)(j - mousePosY) / d;
                if(bSnap)
                {
                    movedActorPosSnap.x += d2;
                    movedActorPosSnap.y += d3;
                    movedActor.pos.getAbs(movedActorPosStepSave);
                    _objectMoveP.set(movedActorPosSnap);
                    _objectMoveP.x = (double)Math.round(movedActorPosSnap.x / snapStep) * snapStep;
                    _objectMoveP.y = (double)Math.round(movedActorPosSnap.y / snapStep) * snapStep;
                    _objectMoveP.z = movedActorPosSnap.z;
                } else
                {
                    movedActor.pos.getAbs(_objectMoveP);
                    _objectMoveP.x += d2;
                    _objectMoveP.y += d3;
                }
                try
                {
                    if(movedActor instanceof PPoint)
                    {
                        PPoint ppoint = (PPoint)movedActor;
                        ppoint.moveTo(_objectMoveP);
                        ((Path)ppoint.getOwner()).pointMoved(ppoint);
                        PlMission.setChanged();
                    } else
                    if(!(movedActor instanceof Bridge) && !(movedActor instanceof ActorLabel) && !(movedActor instanceof ActorBorn))
                    {
                        movedActor.pos.setAbs(_objectMoveP);
                        movedActor.pos.reset();
                        align(movedActor);
                        PlMission.setChanged();
                    }
                    //By PAL; this corresponds???? if(bMultiSelect);;
                    if(selectedActors.containsKey(movedActor))
                    {
                        Actor aactor[] = selectedActors();
                        int k = 0;
                        do
                        {
                            if(k >= aactor.length)
                                break;
                            Actor actor1 = aactor[k];
                            if(actor1 == null)
                                break;
                            if(Actor.isValid(actor1) && actor1 != movedActor)
                            {
                                if(actor1 instanceof ActorAlign)
                                {
                                    actor1.pos.getAbs(__pi);
                                    if(bSnap)
                                    {
                                        __pi.x += _objectMoveP.x - movedActorPosStepSave.x;
                                        __pi.y += _objectMoveP.y - movedActorPosStepSave.y;
                                    } else
                                    {
                                        __pi.x += d2;
                                        __pi.y += d3;
                                    }
                                    actor1.pos.setAbs(__pi);
                                    ((ActorAlign)actor1).align();
                                } else
                                {
                                    actor1.pos.getAbs(__pi);
                                    __pi.x += d2;
                                    __pi.y += d3;
                                    double d4 = Engine.land().HQ(__pi.x, __pi.y) + 0.20000000000000001D;
                                    __pi.z = d4;
                                    actor1.pos.setAbs(__pi);
                                }
                                actor1.pos.reset();
                            }
                            k++;
                        } while(true);
                    }
                }
                catch(Exception exception)
                {
                    mouseState = 0;
                    viewWindow.mouseCursor = 1;
                }
                repaint();
            }
            break;

        case 0: // '\0'
            Actor actor = selectNear(i, j);
            if(!bMultiSelect && actor != null && (actor instanceof Bridge))
            {
                if(movedActor != null)
                    viewWindow.mouseCursor = 1;
                movedActor = null;
                setOverActor(actor);
                break;
            }
            if(actor != null)
            {
                if(movedActor == null)
                    viewWindow.mouseCursor = 7;
                movedActor = actor;
                movedActor.pos.getAbs(movedActorPosSnap);
            } else
            {
                if(movedActor != null)
                    viewWindow.mouseCursor = 1;
                movedActor = null;
            }
            setOverActor(movedActor);
            break;

        case 4: // '\004'
            movedActor = null;
            setOverActor(selectNear(i, j));
            if(!Actor.isValid(selectedPoint()) && !(selectedActor() instanceof PlMisRocket.Rocket))
                breakSelectTarget();
            break;
            
//By PAL, from 4.11
        case 6: // '\006'
            movedActor = null;
            setOverActor(selectNear(i, j));
            if(!Actor.isValid(selectedPoint()) && !(selectedActor() instanceof PlMisRocket.Rocket))
                breakSelectSpawnPoint();
            break;            
        }
        mousePosX = i;
        mousePosY = j;
        if(mouseState == 5)
            Plugin.doFill(point3d);
        if(bMouseRenderRect)
            repaint();
    }

    public void render3D()
    {
        if(!isLoadedLandscape())
            return;
        Plugin.doRender3D();
        if(isFreeView())
            return;
        if(conf.bShowGrid && bView3D)
            drawGrid3D();
    }

    public boolean project2d(Point3d point3d, Point2d point2d)
    {
        return project2d(point3d.x, point3d.y, point3d.z, point2d);
    }

    public boolean project2d(double d, double d1, double d2, Point2d point2d)
    {
        if(bView3D)
        {
            Main3D.cur3D().project2d(d, d1, d2, projectPos3d);
            point2d.x = projectPos3d.x - (double)_viewPort[0];
            point2d.y = projectPos3d.y - (double)_viewPort[1];
        } else
        {
            point2d.x = (d - camera2D.worldXOffset) * camera2D.worldScale;
            point2d.y = (d1 - camera2D.worldYOffset) * camera2D.worldScale;
        }
        if(point2d.x + (double)conf.iconSize < 0.0D)
            return false;
        if(point2d.y + (double)conf.iconSize < 0.0D)
            return false;
        if(point2d.x - (double)conf.iconSize > (double)(camera2D.right - camera2D.left))
            return false;
        return point2d.y - (double)conf.iconSize <= (double)(camera2D.top - camera2D.bottom);
    }

    public void preRenderMap2D()
    {
        if(!isLoadedLandscape())
        {
            return;
        } else
        {
            Plugin.doPreRenderMap2D();
            return;
        }
    }

    public void renderMap2D()
    {
        if(!isLoadedLandscape())
            return;
        if(!isFreeView() && conf.iLightLand != 255)
        {
            int i = 255 - conf.iLightLand << 24 | 0x3f3f3f;
            Render.drawTile(0.0F, 0.0F, viewWindow.win.dx, viewWindow.win.dy, 0.0F, emptyMat, i, 0.0F, 0.0F, 1.0F, 1.0F);
            Render.drawEnd();
        }
        Plugin.doRenderMap2DBefore();
        if(!isFreeView())
            pathes.renderMap2DTargetLines();
        Plugin.doRenderMap2D();
        if(isFreeView())
            return;
        if(conf.bShowGrid)
        {
            if(!bView3D)
                drawGrid2D();
            drawGridText();
        }
        pathes.renderMap2D(bView3D, conf.iconSize);
        Plugin.doRenderMap2DAfter();
        if(bMouseRenderRect && (mouseFirstPosX != mousePosX || mouseFirstPosY != mousePosY))
        {
            line5XYZ[0] = mouseFirstPosX;
            line5XYZ[1] = mouseFirstPosY;
            line5XYZ[2] = 0.0F;
            line5XYZ[3] = mousePosX;
            line5XYZ[4] = mouseFirstPosY;
            line5XYZ[5] = 0.0F;
            line5XYZ[6] = mousePosX;
            line5XYZ[7] = mousePosY;
            line5XYZ[8] = 0.0F;
            line5XYZ[9] = mouseFirstPosX;
            line5XYZ[10] = mousePosY;
            line5XYZ[11] = 0.0F;
            Render.drawBeginLines(-1);
            Render.drawLines(line5XYZ, 4, 1.0F, colorSelected(), Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 4);
            Render.drawEnd();
        }
        drawInfoOverActor();
        //drawSelectTarget();
        //By PAL, from 4.11, replaced previous
        drawSelectTargetAndSpawnPoint();
        drawSelectedSpawnPoint();
                
        if(Main3D.cur3D().land2DText != null)
            Main3D.cur3D().land2DText.render();
        if(conf.bShowGrid && !bView3D)
            SquareLabels.draw(camera2D, Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.mapSizeX());
    }

    private int gridStep()
    {
        double d = viewX;
        if(viewY < viewX)
            d = viewY;
        d *= viewHLand / viewH;
        int i = 0x186a0;
        for(int j = 0; j < 5 && (double)(i * 3) > d; j++)
            i /= 10;

        return i;
    }

    private void drawGrid2D()
    {
        int i = gridStep();
        int j = (int)((camera2D.worldXOffset + Main3D.cur3D().land2D.worldOfsX()) / (double)i);
        int k = (int)((camera2D.worldYOffset + Main3D.cur3D().land2D.worldOfsY()) / (double)i);
        int l = (int)(viewX / (double)i) + 2;
        int i1 = (int)(viewY / (double)i) + 2;
        float f = (float)(((double)(j * i) - camera2D.worldXOffset - Main3D.cur3D().land2D.worldOfsX()) * camera2D.worldScale + 0.5D);
        float f1 = (float)(((double)(k * i) - camera2D.worldYOffset - Main3D.cur3D().land2D.worldOfsY()) * camera2D.worldScale + 0.5D);
        float f2 = (float)((double)(l * i) * camera2D.worldScale);
        float f3 = (float)((double)(i1 * i) * camera2D.worldScale);
        float f4 = (float)((double)i * camera2D.worldScale);
        _gridCount = 0;
        Render.drawBeginLines(-1);
        for(int j1 = 0; j1 <= i1; j1++)
        {
            float f5 = f1 + (float)j1 * f4;
            char c = (j1 + k) % 10 != 0 ? '\177' : '\300';
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
            char c1 = (k1 + j) % 10 != 0 ? '\177' : '\300';
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
            _gridFont.output(0xffc0c0c0, _gridX[i] + 2, _gridY[i] + 2, 0.0F, _gridVal[i] / 1000 + "." + (_gridVal[i] % 1000) / 100);

        _gridCount = 0;
    }

    private void drawGrid3D()
    {
        int i = gridStep();
        int j = (int)((camera2D.worldXOffset + Main3D.cur3D().land2D.worldOfsX()) / (double)i);
        int k = (int)((camera2D.worldYOffset + Main3D.cur3D().land2D.worldOfsY()) / (double)i);
        int l = (int)(viewX / (double)i) + 2;
        int i1 = (int)(viewY / (double)i) + 2;
        Render.drawBeginLines(0);
        for(int j1 = 0; j1 <= i1; j1++)
        {
            float f = (j1 + k) * i;
            boolean flag = true;
            if((j1 + k) % 2 == 0)
            {
                if((j1 + k) % 10 == 0)
                {
                    flag = false;
                }
            }
            double d = -1D;
            double d2 = -1D;
            if(lineNXYZ.length / 3 <= l)
                lineNXYZ = new float[(l + 1) * 3];
            lineNCounter = 0;
            for(int l1 = 0; l1 <= l; l1++)
            {
                float f2 = (l1 + j) * i;
                Engine.land();
                float f4 = Landscape.HQ(f2 - (float)Main3D.cur3D().land2D.worldOfsX(), f - (float)Main3D.cur3D().land2D.worldOfsY());
                lineNXYZ[lineNCounter * 3 + 0] = f2 - (float)Main3D.cur3D().land2D.worldOfsX();
                lineNXYZ[lineNCounter * 3 + 1] = f - (float)Main3D.cur3D().land2D.worldOfsY();
                lineNXYZ[lineNCounter * 3 + 2] = f4;
                lineNCounter++;
                if(flag)
                    continue;
                project2d(f2 - (float)Main3D.cur3D().land2D.worldOfsX(), f - (float)Main3D.cur3D().land2D.worldOfsY(), f4, projectPos2d);
                if(projectPos2d.x > 0.0D)
                {
                    if(l1 > 0)
                        drawGridText(0, (int)(d2 - (d / (projectPos2d.x - d)) * (projectPos2d.y - d2)), (int)f);
                    else
                        drawGridText(0, (int)projectPos2d.y, (int)f);
                    flag = true;
                }
                d = projectPos2d.x;
                d2 = projectPos2d.y;
            }
//By PAL, coming from 4.101:
            //Render.drawLines(lineNXYZ, lineNCounter, 1.0F, 0xff000000 | c << 16 | c << 8 | c, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 2);
//By PAL, in 4.111:
            Render.drawLines(lineNXYZ, 2, 2.0F, 0xff00ffff, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 2);

        }

        for(int k1 = 0; k1 <= l; k1++)
        {
            float f1 = (k1 + j) * i;
            char c1 = '@';
            boolean flag1 = true;
            if((k1 + j) % 2 == 0)
            {
                c1 = '\226';
                if((k1 + j) % 10 == 0)
                {
                    flag1 = false;
                    c1 = '\360';
                }
            }
            double d1 = -1D;
            double d3 = -1D;
            if(lineNXYZ.length / 3 <= i1)
                lineNXYZ = new float[(i1 + 1) * 3];
            lineNCounter = 0;
            for(int i2 = 0; i2 <= i1; i2++)
            {
                float f3 = (i2 + k) * i;
                Engine.land();
                float f5 = Landscape.HQ(f1 - (float)Main3D.cur3D().land2D.worldOfsX(), f3 - (float)Main3D.cur3D().land2D.worldOfsY());
                lineNXYZ[lineNCounter * 3 + 0] = f1 - (float)Main3D.cur3D().land2D.worldOfsX();
                lineNXYZ[lineNCounter * 3 + 1] = f3 - (float)Main3D.cur3D().land2D.worldOfsY();
                lineNXYZ[lineNCounter * 3 + 2] = f5;
                lineNCounter++;
                if(flag1)
                    continue;
                project2d(f1 - (float)Main3D.cur3D().land2D.worldOfsX(), f3 - (float)Main3D.cur3D().land2D.worldOfsY(), f5, projectPos2d);
                if(projectPos2d.y > 0.0D)
                {
                    if(i2 > 0)
                        drawGridText((int)(d1 - (d3 / (projectPos2d.y - d3)) * (projectPos2d.x - d1)), 0, (int)f1);
                    else
                        drawGridText((int)projectPos2d.x, 0, (int)f1);
                    flag1 = true;
                }
                d1 = projectPos2d.x;
                d3 = projectPos2d.y;
            }

            Render.drawLines(lineNXYZ, lineNCounter, 1.0F, 0xff000000 | c1 << 16 | c1 << 8 | c1, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 2);
        }

        Render.drawEnd();
    }

    public Actor getOverActor()
    {
        return overActor;
    }

    public void setOverActor(Actor actor)
    {
        overActor = actor;
    }

    private void drawInfoOverActor()
    {
        if(!Actor.isValid(overActor))
            return;
        Plugin plugin = (Plugin)Property.value((overActor instanceof PPoint) ? ((Object) (overActor.getOwner())) : ((Object) (overActor)), "builderPlugin");
        if(plugin == null)
            return;
        String as[] = plugin.actorInfo(overActor);
        if(as == null)
            return;
        Point3d point3d = overActor.pos.getAbsPoint();
        if(!project2d(point3d, projectPos2d))
            return;
        float f = 0.0F;
        int i = 0;
        for(int j = 0; j < as.length; j++)
        {
            String s = as[j];
            if(s == null)
                break;
            float f3 = smallFont.width(s);
            if(f3 > f)
                f = f3;
            i++;
        }

        if(f == 0.0F)
            return;
        float f1 = -smallFont.descender();
        float f2 = smallFont.height();
        float f4 = (float)i * (f2 + f1) + f1;
        int k = Army.color((overActor instanceof PPoint) ? overActor.getOwner().getArmy() : overActor.getArmy());
        Render.drawTile((float)projectPos2d.x, (float)(projectPos2d.y + (double)(conf.iconSize / 2) + (double)f1), f + 2.0F * f1, f4, 0.0F, emptyMat, k & 0x7fffffff, 0.0F, 0.0F, 1.0F, 1.0F);
        Render.drawEnd();
        int l = 0;
        do
        {
            if(l >= as.length)
                break;
            String s1 = as[l];
            if(s1 == null)
                break;
            smallFont.output(0xff000000 | ~k, (float)(projectPos2d.x + (double)f1), (float)(projectPos2d.y + (double)(conf.iconSize / 2) + (double)f1 + (double)((float)(i - l - 1) * (f1 + f2)) + (double)f1), 0.0F, s1);
            l++;
        } while(true);
    }

/*    private void drawSelectTarget()
    {
        if(mouseState != 4)
            return;
        if(!viewWindow.isMouseOver())
            return;
        PAir pair = (PAir)selectedPoint();
        Point3d point3d;
        if(Actor.isValid(pair))
        {
            point3d = pair.pos.getAbsPoint();
        } else
        {
            Actor actor = selectedActor();
            if(!(actor instanceof PlMisRocket.Rocket))
                return;
            point3d = actor.pos.getAbsPoint();
        }
        project2d(point3d, projectPos2d);
        lineNXYZ[0] = (float)projectPos2d.x;
        lineNXYZ[1] = (float)projectPos2d.y;
        lineNXYZ[2] = 0.0F;
        lineNXYZ[3] = mousePosX;
        lineNXYZ[4] = mousePosY;
        lineNXYZ[5] = 0.0F;
        Render.drawBeginLines(-1);
        Render.drawLines(lineNXYZ, 2, 1.0F, 0xff00ff00, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 2);
        Render.drawEnd();
        float f = conf.iconSize * 2;
        Render.drawTile((float)mousePosX - f / 2.0F, (float)mousePosY - f / 2.0F, f, f, 0.0F, selectTargetMat, -1, 0.0F, 0.0F, 1.0F, 1.0F);
    }*/

//By PAL, from 4.11
    private void drawSelectTargetAndSpawnPoint()
    {
        if(mouseState != 4 && mouseState != 6)
            return;
        if(!viewWindow.isMouseOver())
            return;
        PAir pair = null;
        if(mouseState == 6)
            pair = (PAir)selectedFirstPoint();
        else
            pair = (PAir)selectedPoint();
        Point3d point3d;
        if(Actor.isValid(pair))
        {
            point3d = pair.pos.getAbsPoint();
        } else
        {
            Actor actor = selectedActor();
            if(!(actor instanceof PlMisRocket.Rocket))
                return;
            point3d = actor.pos.getAbsPoint();
        }
        project2d(point3d, projectPos2d);
        lineNXYZ[0] = (float)projectPos2d.x;
        lineNXYZ[1] = (float)projectPos2d.y;
        lineNXYZ[2] = 0.0F;
        lineNXYZ[3] = mousePosX;
        lineNXYZ[4] = mousePosY;
        lineNXYZ[5] = 0.0F;
        Render.drawBeginLines(-1);
        int i = 0xff00ff00;
        if(mouseState == 6)
            i = 0xff00ffff;
        Render.drawLines(lineNXYZ, 2, 1.0F, i, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 2);
        Render.drawEnd();
        float f = conf.iconSize * 2;
        Render.drawTile((float)mousePosX - f / 2.0F, (float)mousePosY - f / 2.0F, f, f, 0.0F, selectTargetMat, -1, 0.0F, 0.0F, 1.0F, 1.0F);
    }

    private void drawSelectedSpawnPoint()
    {
        if(!(selectedFirstPoint() instanceof PAir))
            return;
        PAir pair = (PAir)selectedFirstPoint();
        PathAir pathair = (PathAir)selectedPath();
        if(pathair == null || pair == null || !Actor.isValid(pair))
            return;
        PlMisAir plmisair = (PlMisAir)Plugin.getPlugin("MisAir");
        PlaneGeneric planegeneric = pathair.getSpawnPoint(plmisair.getSelectedPlaneIndex());
        if(planegeneric == null)
        {
            return;
        } else
        {
            Point3d point3d = pair.pos.getAbsPoint();
            Point3d point3d1 = planegeneric.pos.getAbsPoint();
            project2d(point3d, projectPos2d);
            lineNXYZ[0] = (float)projectPos2d.x;
            lineNXYZ[1] = (float)projectPos2d.y;
            lineNXYZ[2] = 0.0F;
            project2d(point3d1, projectPos2d);
            lineNXYZ[3] = (float)projectPos2d.x;
            lineNXYZ[4] = (float)projectPos2d.y;
            lineNXYZ[5] = 0.0F;
            Render.drawBeginLines(-1);
            Render.drawLines(lineNXYZ, 2, 1.0F, 0xff00ffff, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 2);
            Render.drawEnd();
            return;
        }
    } 

    public void beginSelectTarget()
    {
        Path path = selectedPath();
        if(path == null || !(path instanceof PathAir))
        {
            Actor actor = selectedActor();
            if(!(actor instanceof PlMisRocket.Rocket))
                return;
        }
        mouseState = 4;
        viewWindow.mouseCursor = 0;
    }

    public void endSelectTarget()
    {
        if(mouseState != 4)
            return;
        mouseState = 0;
        viewWindow.mouseCursor = 1;
        Path path = selectedPath();
        if(path == null || !(path instanceof PathAir))
        {
            PlMisRocket.Rocket rocket = (PlMisRocket.Rocket)selectedActor();
            Point3d point3d = mouseWorldPos();
            rocket.target = new Point2d(point3d.x, point3d.y);
        } else
        {
            Actor actor = getOverActor();
            if(!Actor.isValid(actor))
                return;
            if((actor instanceof PPoint) && actor.getOwner() == selectedPath())
                return;
            PAir pair = (PAir)selectedPoint();
            pair.setTarget(actor);
            Plugin plugin = (Plugin)Property.value(pair.getOwner(), "builderPlugin");
            plugin.updateSelector();
        }
        PlMission.setChanged();
    }

    public void breakSelectTarget()
    {
        if(mouseState != 4)
        {
            return;
        } else
        {
            mouseState = 0;
            viewWindow.mouseCursor = 1;
            return;
        }
    }

//By PAL, new from 4.11
    public void beginSelectSpawnPoint()
    {
        Path path = selectedPath();
        if(path == null || !(path instanceof PathAir))
        {
            Actor actor = selectedActor();
            if(!(actor instanceof PlMisRocket.Rocket))
                return;
        }
        mouseState = 6;
        viewWindow.mouseCursor = 0;
    }

    public void endSelectSpawnPoint()
    {
        if(mouseState != 6)
            return;
        mouseState = 0;
        viewWindow.mouseCursor = 1;
        Actor actor = getOverActor();
        PathAir pathair = (PathAir)selectedPath();
        if(!Actor.isValid(actor))
            return;
        if(actor instanceof PlaneGeneric)
        {
            PlaneGeneric planegeneric = (PlaneGeneric)actor;
            PlMisAir plmisair = (PlMisAir)Plugin.getPlugin("MisAir");
            int i = plmisair.getSelectedPlaneIndex();
            pathair.setSpawnPoint(i, planegeneric);
            plmisair.wSpawnPointLabel[i].cap.set(PlMisAir.i18n("IndicatedByTheYellowLine"));
        }
        Plugin plugin = (Plugin)Property.value(pathair, "builderPlugin");
        if(plugin != null)
            plugin.updateSelector();
        PlMission.setChanged();
    }

    public void breakSelectSpawnPoint()
    {
        if(mouseState != 6)
        {
            return;
        } else
        {
            mouseState = 0;
            viewWindow.mouseCursor = 1;
            return;
        }
    }

    private void initHotKeys()
    {
        HotKeyCmdEnv.setCurrentEnv(envName);
        HotKeyEnv.fromIni(envName, Config.cur.ini, "HotKey " + envName);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "toLand") {

            public void begin()
            {
                if(Actor.isValid(selectedActor()))
                {
                    align(selectedActor());
                    PlMission.setChanged();
                    if(!isFreeView())
                        repaint();
                }
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "normalLand") {

            public void begin()
            {
                if(Actor.isValid(selectedActor()))
                {
                    Point3d point3d = new Point3d();
                    Orient orient = new Orient();
                    selectedActor().pos.getAbs(point3d, orient);
                    Vector3f vector3f = new Vector3f();
                    Engine.land().N(point3d.x, point3d.y, vector3f);
                    orient.orient(vector3f);
                    selectedActor().pos.setAbs(orient);
                    Builder.defaultAzimut = orient.azimut();
                    PlMission.setChanged();
                    if(!isFreeView())
                        repaint();
                }
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "land") {

            public void begin()
            {
                changeViewLand();
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "stepAzimut-5") {

            public void begin()
            {
                stepAzimut(-5);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "stepAzimut-15") {

            public void begin()
            {
                stepAzimut(-15);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "stepAzimut-30") {

            public void begin()
            {
                stepAzimut(-30);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "stepAzimut5") {

            public void begin()
            {
                stepAzimut(5);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "stepAzimut15") {

            public void begin()
            {
                stepAzimut(15);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "stepAzimut30") {

            public void begin()
            {
                stepAzimut(30);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "resetAngles") {

            public void begin()
            {
                if(!Actor.isValid(selectedActor()))
                    return;
                Orient orient = new Orient();
                orient.set(0.0F, 0.0F, 0.0F);
                Builder.defaultAzimut = 0.0F;
                selectedActor().pos.setAbs(orient);
                PlMission.setChanged();
                if(!isFreeView())
                    repaint();
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "resetTangage90") {

            public void begin()
            {
                if(!Actor.isValid(selectedActor()))
                    return;
                Orient orient = new Orient();
                orient.set(0.0F, 90F, 0.0F);
                Builder.defaultAzimut = 0.0F;
                selectedActor().pos.setAbs(orient);
                PlMission.setChanged();
                if(!isFreeView())
                    repaint();
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "change+") {

            public void begin()
            {
                changeType(false, false);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "change++") {

            public void begin()
            {
                changeType(false, true);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "change-") {

            public void begin()
            {
                changeType(true, false);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "change--") {

            public void begin()
            {
                changeType(true, true);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "insert") {

            public void begin()
            {
                insert(false);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "insert+") {

            public void begin()
            {
                insert(true);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "delete") {

            public void begin()
            {
                delete(false);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "delete+") {

            public void begin()
            {
                delete(true);
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "fill") {

            public void begin()
            {
                if(!isLoadedLandscape())
                    return;
                if(isFreeView())
                    return;
                if(bMultiSelect && !bView3D)
                    return;
                if(mouseState != 0)
                {
                    return;
                } else
                {
                    mouseState = 5;
                    Point3d point3d = posScreenToLand(mousePosX, mousePosY, 0.0D, 0.10000000000000001D);
                    Plugin.doBeginFill(point3d);
                    return;
                }
            }

            public void end()
            {
                if(mouseState != 5)
                {
                    return;
                } else
                {
                    mouseState = 0;
                    Point3d point3d = posScreenToLand(mousePosX, mousePosY, 0.0D, 0.10000000000000001D);
                    Plugin.doEndFill(point3d);
                    return;
                }
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cursor") {

            public void begin()
            {
                if(!isLoadedLandscape())
                    return;
                if(!isFreeView())
                    return;
                if(actorView() == cursor)
                {
                    Actor actor = selectNear(actorView().pos.getAbsPoint());
                    if(actor != null)
                    {
                        cursor.drawing(false);
                        setSelected(actor);
                    }
                } else
                if(Actor.isValid(actorView()))
                {
                    Loc loc = actorView().pos.getAbs();
                    cursor.pos.setAbs(loc);
                    cursor.pos.reset();
                    cursor.drawing(true);
                    setSelected(cursor);
                }
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "objectMove") {

            public void begin()
            {
                if(!isLoadedLandscape())
                    return;
                if(isFreeView())
                    return;
                if(mouseState == 4)
                {
                    endSelectTarget();
                    return;
                }
                //By PAL, from 4.11
                if(mouseState == 6)
                {
                    endSelectSpawnPoint();
                    return;
                }                
                if(mouseState != 0)
                    return;
                mouseState = 1;
                viewWindow.mouseCursor = 7;
                Actor actor = selectNear(mousePosX, mousePosY);
                if(actor != null)
                {
                    setSelected(actor);
                    repaint();
                }
                setOverActor(null);
            }

            public void end()
            {
                if(!isLoadedLandscape())
                    return;
                if(isFreeView())
                    return;
                if(mouseState != 1)
                {
                    return;
                } else
                {
                    mouseState = 0;
                    viewWindow.mouseCursor = 1;
                    return;
                }
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "worldZoom") {

            public void begin()
            {
                if(!isLoadedLandscape())
                    return;
                if(isFreeView())
                    return;
                if(mouseState != 0)
                {
                    return;
                } else
                {
                    mouseState = 2;
                    mouseFirstPosX = mousePosX;
                    mouseFirstPosY = mousePosY;
                    bMouseRenderRect = true;
                    viewWindow.mouseCursor = 2;
                    setOverActor(null);
                    return;
                }
            }

            public void end()
            {
                if(!isLoadedLandscape())
                    return;
                if(isFreeView())
                    return;
                if(mouseState != 2)
                    return;
                mouseState = 0;
                bMouseRenderRect = false;
                if(mouseFirstPosX == mousePosX)
                {
                    repaint();
                    return;
                }
                Point3d point3d = posScreenToLand(mouseFirstPosX, mouseFirstPosY, 0.0D, 0.10000000000000001D);
                double d = point3d.x;
                double d1 = point3d.y;
                point3d = posScreenToLand(mousePosX, mousePosY, 0.0D, 0.10000000000000001D);
                double d2 = ((double)camera.FOV() * 3.1415926535897931D) / 180D / 2D;
                double d3 = point3d.x - d;
                if(d3 < 0.0D)
                    d3 = -d3;
                double d4 = d3 / 2D / Math.tan(d2);
                computeViewMap2D(d4, (d + point3d.x) / 2D, (d1 + point3d.y) / 2D);
                viewWindow.mouseCursor = 1;
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "unselect") {

            public void begin()
            {
                if(!isLoadedLandscape())
                    return;
                if(isFreeView())
                    return;
                if(mouseState != 0)
                {
                    return;
                } else
                {
                    setSelected(null);
                    repaint();
                    return;
                }
            }

        }
);
        if(bMultiSelect) //By PAL; from 4.111 this corresponds????
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "select+") {

            public void begin()
            {
                if(!isLoadedLandscape())
                    return;
                if(isFreeView())
                    return;
                if(mouseState != 0)
                {
                    return;
                } else
                {
                    mouseState = 3;
                    mouseFirstPosX = mousePosX;
                    mouseFirstPosY = mousePosY;
                    bMouseRenderRect = true;
                    return;
                }
            }

            public void end()
            {
                if(!isLoadedLandscape())
                    return;
                if(isFreeView())
                    return;
                if(mouseState != 3)
                    return;
                mouseState = 0;
                bMouseRenderRect = false;
                if(mouseFirstPosX != mousePosX)
                {
                    Point3d point3d = posScreenToLand(mouseFirstPosX, mouseFirstPosY, 0.0D, 0.10000000000000001D);
                    double d = point3d.x;
                    double d1 = point3d.y;
                    point3d = posScreenToLand(mousePosX, mousePosY, 0.0D, 0.10000000000000001D);
                    select(d, d1, point3d.x, point3d.y, true);
                    setSelected(null);
                }
                repaint();
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "select-") {

            public void begin()
            {
                if(!isLoadedLandscape())
                    return;
                if(isFreeView())
                    return;
                if(mouseState != 0)
                {
                    return;
                } else
                {
                    mouseState = 3;
                    mouseFirstPosX = mousePosX;
                    mouseFirstPosY = mousePosY;
                    bMouseRenderRect = true;
                    return;
                }
            }

            public void end()
            {
                if(!isLoadedLandscape())
                    return;
                if(isFreeView())
                    return;
                if(mouseState != 3)
                    return;
                mouseState = 0;
                bMouseRenderRect = false;
                if(mouseFirstPosX != mousePosX)
                {
                    Point3d point3d = posScreenToLand(mouseFirstPosX, mouseFirstPosY, 0.0D, 0.10000000000000001D);
                    double d = point3d.x;
                    double d1 = point3d.y;
                    point3d = posScreenToLand(mousePosX, mousePosY, 0.0D, 0.10000000000000001D);
                    select(d, d1, point3d.x, point3d.y, false);
                    setSelected(null);
                }
                repaint();
            }

        }
);

//By PAL, from 4.11
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "beginSelectSpawnPoint") {

            public void end()
            {
                if(!isLoadedLandscape())
                    return;
                if(mouseState != 0)
                    return;
                if(isFreeView())
                {
                    return;
                } else
                {
                    beginSelectSpawnPoint();
                    return;
                }
            }

        }
);

        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "freeView") {

            public void end()
            {
                if(!isLoadedLandscape())
                    return;
                if(mouseState != 0)
                    return;
                if(isFreeView())
                    clearActorView();
                else
                if(bView3D)
                    setActorView();
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "break") {

            public void end()
            {
                if(!isLoadedLandscape())
                    return;
                if(isFreeView())
                {
                    clearActorView();
                } else
                {
                    setOverActor(null);
                    breakSelectTarget();
                    //By PAL, from 4.11
                  	breakSelectSpawnPoint();                   
                    mouseState = 0;
                }
            }

        }
);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "popupmenu") {

            public void begin()
            {
                if(!isLoadedLandscape())
                    return;
                if(mouseState != 0)
                    return;
                if(isFreeView())
                {
                    return;
                } else
                {
                    doPopUpMenu();
                    return;
                }
            }

        }
);
        if(!bMultiSelect)
        {
            HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "mis_cut") {

                public void begin()
                {
                    mis_cut();
                }

            }
);
            HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "mis_copy") {

                public void begin()
                {
                    mis_copy(true);
                }

            }
);
            HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "mis_paste") {

                public void begin()
                {
                    mis_paste();
                }

            }
);
        }
    }

    private void mis_cut()
    {
        if(Plugin.builder.isFreeView())
            return;
        mis_copy(false);
        Actor aactor[] = selectedActors();
        for(int i = 0; i < aactor.length; i++)
        {
            Actor actor = aactor[i];
            if(actor == null)
                break;
            if(!Actor.isValid(actor) || !isMiltiSelected(actor))
                continue;
            if(actor instanceof PAirdrome)
            {
                PathAirdrome pathairdrome = (PathAirdrome)actor.getOwner();
                if(pathairdrome.pointIndx((PAirdrome)actor) == 0)
                    pathairdrome.destroy();
            } else
            {
                actor.destroy();
            }
        }

        selectedActorsValidate();
        PlMission.setChanged();
        repaint();
    }

    private void mis_copy(boolean flag)
    {
        if(Plugin.builder.isFreeView())
            return;
        mis_properties.clear();
        mis_cBoxes.clear();
        mis_clipLoc.clear();
        int i = 0;
        mis_clipP0.x = mis_clipP0.y = mis_clipP0.z = 0.0D;
        Actor aactor[] = selectedActors();
        for(int j = 0; j < aactor.length; j++)
        {
            Actor actor = aactor[j];
            if(actor == null)
                break;
            if(Actor.isValid(actor) && isMiltiSelected(actor))
            {
                Loc loc = new Loc();
                actor.pos.getAbs(loc);
                mis_clipLoc.add(loc);
                mis_clipP0.add((loc).getPoint());
                setSelected(actor);
                mis_cBoxes.add("" + wSelect.comboBox1.getSelected());
                mis_cBoxes.add("" + wSelect.comboBox2.getSelected());
                mis_properties.add(Plugin.mis_doGetProperties(actor));
                setSelected(null);
                i++;
            }
        }

        if(i > 1)
        {
            mis_clipP0.x /= i;
            mis_clipP0.y /= i;
            mis_clipP0.z /= i;
        }
        if(flag)
            selectActorsClear();
        repaint();
    }

    private void mis_paste()
    {
        if(isFreeView())
            return;
        selectActorsClear();
        int i = mis_properties.size();
        if(i == 0)
            return;
        Point3d point3d = mouseWorldPos();
        Loc loc = new Loc();
        Point3d point3d1 = new Point3d();
        for(int j = 0; j < i; j++)
        {
            Loc loc1 = (Loc)mis_clipLoc.get(j);
            point3d1.sub(loc1.getPoint(), mis_clipP0);
            point3d1.add(point3d);
            loc.set(point3d1, loc1.getOrient());
            wSelect.comboBox1.setSelected(Integer.parseInt((String)mis_cBoxes.get(j * 2)), false, true);
            wSelect.comboBox2.setSelected(Integer.parseInt((String)mis_cBoxes.get(j * 2 + 1)), false, true);
            Actor actor = Plugin.mis_doInsert(loc, (String)mis_properties.get(j));
            selectActorsAdd(actor);
            setSelected(null);
        }

        PlMission.setChanged();
        repaint();
    }

//By PAL, not present in 4.111
/*
    private static final String getFullClassName(Actor actor)
    {
        return (actor instanceof SoftClass) ? ((SoftClass)actor).fullClassName() : actor.getClass().getName();
    }
    */

    public void doPopUpMenu()
    {
        if(mousePosX == -1)
            return;
        if(popUpMenu == null)
        {
            popUpMenu = (GWindowMenuPopUp)viewWindow.create(new GWindowMenuPopUp());
        } else
        {
            if(popUpMenu.isVisible())
                return;
            popUpMenu.clearItems();
        }
        Point3d point3d = posScreenToLand(mousePosX, mousePosY, 0.0D, 0.10000000000000001D);
        float f = viewWindow.win.dy - (float)mousePosY - 1.0F;
        if(Actor.isValid(selectedPoint()) || Actor.isValid(selectedActor()))
        {
            popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("&Unselect"), Plugin.i18n("TIPUnselect")) {

                public void execute()
                {
                    setSelected(null);
                }

            }
);
            popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("&Delete"), Plugin.i18n("TIPDelete")) {

                public void execute()
                {
                    delete(true);
                }

            }
);
        }
        if(selectedActors.size() > 0)
        {
            popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Unselect&All"), Plugin.i18n("TIPUnselectAll")) {

                public void execute()
                {
                    setSelected(null);
                    selectedActors.clear();
                }

            }
);
            popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Cut"), Plugin.i18n("TIPCut")) {

                public void execute()
                {
                    mis_cut();
                }

            }
);
            popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Copy"), Plugin.i18n("TIPCopy")) {

                public void execute()
                {
                    mis_copy(true);
                }

            }
);
        }
        if(mis_properties.size() > 0)
            popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Paste"), Plugin.i18n("TIPPaste")) {

                public void execute()
                {
                    mis_paste();
                }

            }
);
        Plugin.doFillPopUpMenu(popUpMenu, point3d);
        if(popUpMenu.size() > 0)
        {
            popUpMenu.setPos(mousePosX, f);
            popUpMenu.showModal();
            movedActor = null;
        } else
        {
            popUpMenu.hideWindow();
        }
    }

    public void setViewLand()
    {
        Main3D.cur3D().setDrawLand(conf.bShowLandscape);
        if(Main3D.cur3D().land2D != null)
            Main3D.cur3D().land2D.show(isView3D() ? false : conf.bShowLandscape);
    }

    public void changeViewLand()
    {
        conf.bShowLandscape = !conf.bShowLandscape;
        Main3D.cur3D().setDrawLand(conf.bShowLandscape);
        if(Main3D.cur3D().land2D != null)
            Main3D.cur3D().land2D.show(isView3D() ? false : conf.bShowLandscape);
        wViewLand.wShow.setChecked(conf.bShowLandscape, false);
        if(!isFreeView())
            repaint();
    }

    public void changeType(boolean flag, boolean flag1)
    {
        if(!isLoadedLandscape())
            return;
        if(mouseState != 0)
            return;
        if(!Actor.isValid(selectedActor()) || selectedActor() == cursor)
            return;
        Plugin.doChangeType(flag, flag1);
        if(!isFreeView())
            repaint();
    }

    private void insert(boolean flag)
    {
        if(!isLoadedLandscape())
            return;
        if(mouseState != 0)
            return;
        Loc loc = new Loc();
        if(isFreeView())
        {
            if(!Actor.isValid(actorView()))
                return;
            actorView().pos.getAbs(loc);
            if(actorView() != cursor)
                loc.add(new Point3d(1.0D, 1.0D, 0.0D));
        } else
        {
            loc.set(posScreenToLand(mousePosX, mousePosY, 0.0D, 0.10000000000000001D), new Orient(defaultAzimut, 0.0F, 0.0F));
        }
        Plugin.doInsert(loc, flag);
        if(!isFreeView())
            repaint();
    }

    private void delete(boolean flag)
    {
        if(!isLoadedLandscape())
            return;
        if(mouseState != 0)
            return;
        if(isFreeView())
        {
            if(!Actor.isValid(selectedActor()) || selectedActor() == cursor || (selectedActor() instanceof Bridge))
                return;
            Loc loc = selectedActor().pos.getAbs();
            selectedActor().destroy();
            Plugin.doAfterDelete();
            PlMission.setChanged();
            Object obj = null;
            if(flag)
                obj = selectNear(loc.getPoint());
            if(obj == null)
            {
                obj = cursor;
                cursor.pos.setAbs(loc);
                cursor.pos.reset();
                cursor.drawing(true);
            }
            setSelected(((Actor) (obj)));
        } else
        {
            if(Actor.isValid(selectedPoint()))
            {
                Path path = selectedPath();
                try
                {
                    PPoint ppoint = path.selectPrev(pathes.currentPPoint);
                    pathes.currentPPoint.destroy();
                    Plugin.doAfterDelete();
                    PlMission.setChanged();
                    if(ppoint == null)
                    {
                        path.destroy();
                        Plugin.doAfterDelete();
                        setSelected(null);
                    } else
                    {
                        setSelected(flag ? ((Actor) (ppoint)) : null);
                    }
                }
                catch(Exception exception) { }
            } else
            {
                boolean flag1 = false;
                if(Actor.isValid(selectedActor()) && !selectedActors.containsKey(selectedActor()))
                {
                    if(bMultiSelect && (selectedActor() instanceof Bridge))
                    {
                        if(deletingMessageBox == null)
                        {
                            deletingActor = selectedActor();
                            bDeletingChangeSelection = flag;
                            deletingMessageBox = new GWindowMessageBox(clientWindow, 20F, true, "Confirm DELETE", "Delete Bridge ?", 1, 0.0F) {

                                public void result(int j)
                                {
                                    if(j == 3)
                                    {
                                        if(deletingActor == selectedActor())
                                            delete(bDeletingChangeSelection);
                                    } else
                                    {
                                        deletingMessageBox = null;
                                        deletingActor = null;
                                        setSelected(null);
                                    }
                                }

                            }
;
                            return;
                        }
                        deletingMessageBox = null;
                        PlMapLoad.bridgeActors.remove(selectedActor());
                        selectedActor().destroy();
                        deletingActor = null;
                    } else
                    {
                        Plugin plugin = (Plugin)Property.value(selectedActor(), "builderPlugin");
                        if(plugin != null)
                            plugin.delete(selectedActor());
                    }
                    flag1 = true;
                } else
                {
                    Actor aactor[] = selectedActors();
                    for(int i = 0; i < aactor.length; i++)
                    {
                        Actor actor = aactor[i];
                        if(actor == null)
                            break;
                        if(Actor.isValid(actor))
                        {
                            Plugin plugin1 = (Plugin)Property.value(actor, "builderPlugin");
                            plugin1.delete(actor);
                            flag1 = true;
                        }
                    }

                }
                if(flag1)
                {
                    Plugin.doAfterDelete();
                    PlMission.setChanged();
                    selectedActorsValidate();
                    if(flag)
                        setSelected(selectNearFull(mousePosX, mousePosY));
                    else
                        setSelected(null);
                } else
                {
                    Loc loc1 = new Loc();
                    loc1.set(posScreenToLand(mousePosX, mousePosY, 0.0D, 0.10000000000000001D));
                    Plugin.doDelete(loc1);
                }
            }
            repaint();
        }
    }

/*    private void stepAzimut(int i)
    {
        if(!isLoadedLandscape())
            return;
        if(Actor.isValid(selectedActor()))
        {
            if(selectedActor() instanceof Bridge)
                return;
            Orient orient = new Orient();
            selectedActor().pos.getAbs(orient);
            orient.wrap();
            orient.set(orient.azimut() + (float)i, orient.tangage(), orient.kren());
            selectedActor().pos.setAbs(orient);
            defaultAzimut = orient.azimut();
            align(selectedActor());
            PlMission.setChanged();
            if(!isFreeView())
                repaint();
        } else
        {
            //By PAL; this corresponds???? if(bMultiSelect);;
            if(!isFreeView() && countSelectedActors() > 0)
            {
                Point3d point3d = posScreenToLand(mousePosX, mousePosY, 0.0D, 0.10000000000000001D);
                Point3d point3d1 = new Point3d();
                Orient orient1 = new Orient();
                Actor aactor[] = selectedActors();
                double d = Math.sin(((double)i * 3.1415926535897931D) / 180D);
                double d1 = Math.cos(((double)i * 3.1415926535897931D) / 180D);
                for(int j = 0; j < aactor.length; j++)
                {
                    Actor actor = aactor[j];
                    if(actor == null)
                        break;
                    if(!Actor.isValid(actor))
                        continue;
                    Point3d point3d2 = actor.pos.getAbsPoint();
                    point3d1.x = point3d2.x - point3d.x;
                    point3d1.y = point3d2.y - point3d.y;
                    if(point3d1.x * point3d1.x + point3d1.y * point3d1.y > 9.9999999999999995E-007D)
                    {
                        double d2 = point3d1.x * d1 + point3d1.y * d;
                        double d3 = point3d1.y * d1 - point3d1.x * d;
                        point3d1.x = d2 + point3d.x;
                        point3d1.y = d3 + point3d.y;
                    }
                    point3d1.z = point3d2.z;
                    if(bRotateObjects)
                    {
                        actor.pos.getAbs(orient1);
                        orient1.wrap();
                        orient1.set(orient1.azimut() + (float)i, orient1.tangage(), orient1.kren());
                        actor.pos.setAbs(point3d1, orient1);
                    } else
                    {
                        actor.pos.setAbs(point3d1);
                    }
                    align(actor);
                }

                repaint();
            }
        }
    }*/
    
//By PAL, different from 4.11
    private void stepAzimut(int i)
    {
        if(!isLoadedLandscape())
            return;
        if(Actor.isValid(selectedActor()))
        {
            if(selectedActor() instanceof Bridge)
                return;
            Orient orient = new Orient();
            selectedActor().pos.getAbs(orient);
            orient.wrap();
            orient.set(orient.azimut() + (float)i, orient.tangage(), orient.kren());
            selectedActor().pos.setAbs(orient);
            defaultAzimut = orient.azimut();
            align(selectedActor());
            PlMission.setChanged();
            if(!isFreeView())
                repaint();
        } else
        if(!isFreeView() && countSelectedActors() > 0)
        {
            Point3d point3d = posScreenToLand(mousePosX, mousePosY, 0.0D, 0.10000000000000001D);
            Point3d point3d1 = new Point3d();
            Orient orient1 = new Orient();
            Actor aactor[] = selectedActors();
            double d = Math.sin(((double)i * 3.1415926535897931D) / 180D);
            double d1 = Math.cos(((double)i * 3.1415926535897931D) / 180D);
            for(int j = 0; j < aactor.length; j++)
            {
                Actor actor = aactor[j];
                if(actor == null)
                    break;
                if(!Actor.isValid(actor))
                    continue;
                Point3d point3d2 = actor.pos.getAbsPoint();
                point3d1.x = point3d2.x - point3d.x;
                point3d1.y = point3d2.y - point3d.y;
                if(point3d1.x * point3d1.x + point3d1.y * point3d1.y > 9.9999999999999995E-007D)
                {
                    double d2 = point3d1.x * d1 + point3d1.y * d;
                    double d3 = point3d1.y * d1 - point3d1.x * d;
                    point3d1.x = d2 + point3d.x;
                    point3d1.y = d3 + point3d.y;
                }
                point3d1.z = point3d2.z;
                if(bRotateObjects)
                {
                    actor.pos.getAbs(orient1);
                    orient1.wrap();
                    orient1.set(orient1.azimut() + (float)i, orient1.tangage(), orient1.kren());
                    actor.pos.setAbs(point3d1, orient1);
                } else
                {
                    actor.pos.setAbs(point3d1);
                }
                align(actor);
            }

            repaint();
        }
    }    

    public void tipErr(String s)
    {
        System.err.println(s);
        clientWindow.toolTip(s);
    }

    public void tip(String s)
    {
        clientWindow.toolTip(s);
    }

    protected void doMenu_FileExit()
    {
        if(Plugin.doExitBuilder())
            Main.stateStack().pop();
    }

//By PAL, is this for FMB Plus???
    protected void doMenu_LoadPlugins()
    {
    	//GWindowRootMenu gwindowrootmenu = (GWindowRootMenu)clientWindow.root;
    	//gwindowrootmenu.close(true);
        /*SectFile plugins = new SectFile();
        plugins.sectionAdd("PLUGINS_SECTION");
        plugins.set("PLUGINS_SECTION", "(builder.PlMapLoad)");
        plugins.set("PLUGINS_SECTION", "(builder.PlMapActors,sectFile=com/maddox/il2/objects/static.ini)");
        plugins.set("PLUGINS_SECTION", "(builder.PlMapAirdrome)");        
        plugins.set("PLUGINS_SECTION", "(builder.PlMapLabel)");
        plugins.set("PLUGINS_SECTION", "(builder.PlMapText)");
        Plugin.
        Plugin.loadAll(plugins, PLUGINS_SECTION, this);*/
        //Plugin.doExitBuilder();
        
        //Plugin.loadAll(new SectFile("bldconfUn.ini", 0), PLUGINS_SECTION, this);
        //enter();
        //Plugin.doExitBuilder();

        //Plugin.doFreeResources();
        //Plugin.doFinalize();
        if(Plugin.doExitBuilder())
        {
	    	GWindowRootMenu gwindowrootmenu = (GWindowRootMenu)clientWindow.root;
	    	gwindowrootmenu.close(true);        
	        bMapBuilding = true;        
	        leave();        
        
            conf.load(new SectFile("bldconfUn.ini", 1), "builder config");
        	Plugin.loadAll(new SectFile("bldconfUn.ini", 0), PLUGINS_SECTION, this);
            Plugin.doStart();
            enter();        	
        }

/*        	
[builder_plugins]
  (builder.PlMapLoad)
  (builder.PlMapActors,sectFile=com/maddox/il2/objects/static.ini)
  (builder.PlMapAirdrome)
  (builder.PlMapLabel)
  (builder.PlMapText)        	
        Plugin.loadAll(new SectFile("bldconf.ini", 0), PLUGINS_SECTION, this);*/             
    }      

    public void repaint()
    {
    }

    public void enterRenders()
    {
        bView3D = false;
        Main3D.cur3D().renderMap2D.setShow(true);
        Main3D.cur3D().renderMap2D.useClearColor(true);
        Main3D.cur3D().render3D0.setShow(false);
        Main3D.cur3D().render3D1.setShow(false);
        Main3D.cur3D().render2D.setShow(false);
        _viewPort[2] = (int)viewWindow.win.dx;
        _viewPort[3] = (int)viewWindow.win.dy;
        GPoint gpoint = viewWindow.windowToGlobal(0.0F, 0.0F);
        _viewPort[0] = (int)gpoint.x + ((GUIWindowManager)viewWindow.root.manager).render.getViewPortX0();
        _viewPort[1] = (int)(viewWindow.root.win.dy - gpoint.y - viewWindow.win.dy) + ((GUIWindowManager)viewWindow.root.manager).render.getViewPortY0();
        Main3D.cur3D().renderMap2D.setViewPort(_viewPort);
        Main3D.cur3D().render3D0.setViewPort(_viewPort);
        Main3D.cur3D().render3D1.setViewPort(_viewPort);
        camera2D.set(0.0F, _viewPort[2], 0.0F, _viewPort[3]);
        Render render = (Render)Actor.getByName("renderConsoleGL0");
        if(render != null)
        {
            CameraOrtho2D cameraortho2d = (CameraOrtho2D)render.getCamera();
            render.setViewPort(_viewPort);
            cameraortho2d.set(0.0F, _viewPort[2], 0.0F, _viewPort[3]);
        }
        render = (Render)Actor.getByName("renderTextScr");
        if(render != null)
        {
            CameraOrtho2D cameraortho2d1 = (CameraOrtho2D)render.getCamera();
            render.setViewPort(_viewPort);
            cameraortho2d1.set(0.0F, _viewPort[2], 0.0F, _viewPort[3]);
        }
    }

    public void leaveRenders()
    {
        Main3D.cur3D().renderMap2D.setShow(false);
        Main3D.cur3D().renderMap2D.useClearColor(false);
        Main3D.cur3D().render3D0.setShow(true);
        Main3D.cur3D().render3D1.setShow(true);
        Main3D.cur3D().render2D.setShow(true);
        leaveRender(Main3D.cur3D().renderMap2D);
        leaveRender(Main3D.cur3D().render3D0);
        leaveRender(Main3D.cur3D().render3D1);
        leaveRender(Main3D.cur3D().render2D);
        leaveRender((Render)Actor.getByName("renderConsoleGL0"));
        leaveRender((Render)Actor.getByName("renderTextScr"));
    }

    private void leaveRender(Render render)
    {
        if(render == null)
        {
            return;
        } else
        {
            render.contextResized();
            return;
        }
    }

    public void mapLoaded()
    {
        enterRenders();
        if(!isLoadedLandscape())
        {
            bView3D = false;
            Main3D.cur3D().renderMap2D.setShow(true);
            Main3D.cur3D().render3D0.setShow(false);
            Main3D.cur3D().render3D1.setShow(false);
            Main3D.cur3D().render2D.setShow(false);
            mapXscrollBar.setRange(0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
            mapYscrollBar.setRange(0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
            mapZSlider.setRange(0, 2, 0);
        } else
        {
            computeViewMap2D(-1D, 0.0D, 0.0D);
            if(Main3D.cur3D().land2D != null)
                Main3D.cur3D().land2D.show(conf.bShowLandscape);
        }
    }

    public void enter()
    {
        Main3D.cur3D().resetGame();
        saveMaxVisualDistance = World.MaxVisualDistance;
        saveMaxStaticVisualDistance = World.MaxStaticVisualDistance;
        World.MaxVisualDistance = MaxVisualDistance;
        World.MaxStaticVisualDistance = MaxVisualDistance;
        enterRenders();
        setViewLand();
        Main3D.cur3D().camera3D.dreamFire(true);
        Main3D.cur3D().hookView.use(true);
        Main3D.cur3D().hookView.reset();
        Main3D.cur3D().bEnableFog = false;
        Runaway.bDrawing = bMultiSelect;
        camera.interpPut(new InterpolateOnLand(), "onLand", Time.currentReal(), null);
        viewWindow.mouseCursor = 1;
        pathes = new Pathes();
        //By PAL, from 4.111
        mis_properties = new ArrayList();
        mis_cBoxes = new ArrayList();
        mis_clipLoc = new ArrayList();
        mis_clipP0 = new Point3d();
                
        PlMission.doMissionReload();
        cursor = new CursorMesh("3do/primitive/coord/mono.sim");
    }

    public void leave()
    {
        camera.interpEnd("onLand");
        PlMapLoad plmapload = (PlMapLoad)Plugin.getPlugin("MapLoad");
        plmapload.mapUnload();
//By PAL, from 4.111
        mis_properties = null;
        mis_cBoxes = null;
        mis_clipLoc = null;
        mis_clipP0 = null;
        selectedActors.clear();
                
        mouseState = 0;
        setSelected(null);
        if(wSelect.isVisible())
            wSelect.hideWindow();
        if(wViewLand.isVisible())
            wViewLand.hideWindow();
        //By PAL; this corresponds???? This is in 4.111 without logic: if(bMultiSelect);;
        if(wSnap.isVisible())
            wSnap.hideWindow();
        Plugin.doFreeResources();
        leaveRenders();
        Main3D.cur3D().bEnableFog = true;
        Main3D.cur3D().camera3D.dreamFire(false);
        Runaway.bDrawing = false;
        pathes.destroy();
        pathes = null;
        cursor.destroy();
        cursor = null;
        mouseXYZATK.resetGame();
        Main3D.cur3D().resetGame();
        conf.save();
        World.MaxVisualDistance = saveMaxVisualDistance;
        World.MaxStaticVisualDistance = saveMaxVisualDistance;
    }

//By PAL, doesn't exist in 4.111:
    private static boolean exisstFile(String s)
    {
        try
        {
            SFSInputStream sfsinputstream = new SFSInputStream(s);
            sfsinputstream.close();
        }
        catch(Exception exception)
        {
            return false;
        }
        return true;
    }
    
    public Builder(GWindowRootMenu gwindowrootmenu, String s)
    {
        selectedActors = new HashMapExt();
        bSnap = false;
        snapStep = 10D;
        bFreeView = false;
        viewDistance = 100000D;
        viewHMax = -1D;
        viewH = -1D;
        viewHLand = -1D;
        viewX = 1.0D;
        viewY = 1.0D;
        bView3D = false;
        _camPoint = new Point3d();
        _camOrient = new Orient(-90F, -90F, 0.0F);
        __posScreenToLand = new Point3d();
        _selectFilter = new SelectFilter();
        __pi = new Point3d();
        __oi = new Orient();
        _deleted = new ArrayList();
        filterSelect = new FilterSelect();
        saveMouseMode = 2;
        _savCameraNoFreeLoc = new Loc();
        _savCameraFreeLoc = new Loc();
        mouseState = 0;
        mouseFirstPosX = 0;
        mouseFirstPosY = 0;
        mousePosX = 0;
        mousePosY = 0;
        bMouseRenderRect = false;
        movedActor = null;
        movedActorPosSnap = new Point3d();
        movedActorPosStepSave = new Point3d();
        _objectMoveP = new Point3d();
        projectPos3d = new Point3d();
        line5XYZ = new float[15];
        line2XYZ = new float[6];
        _gridX = new int[20];
        _gridY = new int[20];
        _gridVal = new int[20];
        projectPos2d = new Point2d();
        lineNXYZ = new float[6];
        overActor = null;
        bRotateObjects = false;
        envName = s;
        ((GUIWindowManager)gwindowrootmenu.manager).setUseGMeshs(true);
        camera = (Camera3D)Actor.getByName("camera");
        camera2D = (CameraOrtho2D)Actor.getByName("cameraMap2D");
        mouseXYZATK = new MouseXYZATK("MouseXYZ");
        mouseXYZATK.setCamera(camera);
        conf = new BldConfig();
        	//By PAL, this section original for non Map-Building
        	//conf.load(new SectFile("bldconfUn.ini", 1), "builder config");
        	//Plugin.loadAll(new SectFile("bldconfUn.ini", 0), PLUGINS_SECTION, this);         
        if (bMapBuilding && exisstFile("bldconfUn.ini"))
        {
        	//By PAL, this section original for non Map-Building
        	conf.load(new SectFile("bldconfUn.ini", 1), "builder config");
        	Plugin.loadAll(new SectFile("bldconfUn.ini", 0), PLUGINS_SECTION, this);        	        	
        }        
        else
        {
        	conf.load(new SectFile("bldconf.ini", 1), "builder config");
        	Plugin.loadAll(new SectFile("bldconf.ini", 0), PLUGINS_SECTION, this);
        	bMapBuilding = false;        	
        }      	
        gwindowrootmenu.clientWindow = gwindowrootmenu.create(clientWindow = new ClientWindow());
        mapXscrollBar = new XScrollBar(clientWindow);
        mapYscrollBar = new YScrollBar(clientWindow);
        mapZSlider = new ZSlider(clientWindow);
        mapXscrollBar.setRange(0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
        mapYscrollBar.setRange(0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
        mapZSlider.setRange(0, 2, 0);
        mapZSlider.bSlidingNotify = true;
        clientWindow.create(viewWindow = new ViewWindow());
        clientWindow.resized();
        gwindowrootmenu.statusBar.defaultHelp = null;        
 /*     mFile = gwindowrootmenu.menuBar.addItem(Plugin.i18n("&File"), Plugin.i18n("Load/SaveMissionFiles"));
        mFile.subMenu = (GWindowMenu)mFile.create(new GWindowMenu());
        mFile.subMenu.close(false);
        mFile.subMenu.addItem("-", null);
        mFile.subMenu.addItem(new GWindowMenuItem(mFile.subMenu, Plugin.i18n("&Exit"), Plugin.i18n("ExitBuilder")) {

            public void execute()
            {
                doMenu_FileExit();
            }

        }
);
        mEdit = gwindowrootmenu.menuBar.addItem(Plugin.i18n("&Edit"), Plugin.i18n("TIPEdit"));
        mEdit.subMenu = (GWindowMenu)mEdit.create(new GWindowMenu());
        mEdit.subMenu.close(false);  */
        
        mFile = gwindowrootmenu.menuBar.addItem(Plugin.i18n("&File"), Plugin.i18n("Load/SaveMissionFiles"));              
        mFile.subMenu = (GWindowMenu)mFile.create(new GWindowMenu());
        mFile.subMenu.close(false);
        //gwindowmenuitem.bChecked = conf.bAnimateCamera;        
        GWindowMenuItem gw = mFile.subMenu.addItem(new GWindowMenuItem(mFile.subMenu, Plugin.i18n("&Unlocked FMB"), Plugin.i18n("Map Building FMB")) {
            public void execute()
            {
                //doMenu_LoadPlugins();
                /*String header, text;
                if (!bMapBuilding)
                {
                	if (exisstFile("bldconfUn.ini"))
                	{
                		bMapBuilding = true;
	                	Config.cur.ini.set("Mods", "PALUnlockedFMB", bMapBuilding); //By PAL
	                	bChecked = bMapBuilding;
	                	header = "Notice";
	                	text = "After restarting the program the Full Unlocked FMB for Map Design will be loaded.";
                	}
                	else
                 	{
                		bMapBuilding = false;
	                	Config.cur.ini.set("Mods", "PALUnlockedFMB", bMapBuilding); //By PAL
	                	bChecked = bMapBuilding;
	                	header = "Warning";
	                	text = "To select the Unlocked FMB from here, you need the bldconfUn.ini file in the main game directory. Make sure it is present and try again!";
                	}               				                	                	
                }
                else
                {
            		bMapBuilding = false;
                	Config.cur.ini.set("Mods", "PALUnlockedFMB", bMapBuilding); //By PAL
                	bChecked = bMapBuilding;
                	header = "Notice";
                	text = "You choosed to abandon the Full Unlocked FMB mode. The next time you start the game, the conventional FMB will be loaded.";                	
                }*/
                
                
                //Plugin.doFinalize();
                //gwindowrootmenu.close(true);
                String header = "MissionPro by PAL - Notice";              
                String text = "This function is not implemented yet!";
   		 		new GWindowMessageBox(clientWindow.root, 20F, true, header, text, 4, 0.0F);
      		 	 
            }
        }
);
        gw.bChecked = bMapBuilding;        
        mFile.subMenu.addItem("-", null); //by PAL, I don't want the separator
        mFile.subMenu.addItem(new GWindowMenuItem(mFile.subMenu, Plugin.i18n("&Exit"), Plugin.i18n("ExitBuilder")) {

            public void execute()
            {
                doMenu_FileExit();
            }

        }
);
        mMap = gwindowrootmenu.menuBar.addItem(Plugin.i18n("&Map"), Plugin.i18n("Load Landscape")); //By PAL
        mMap.subMenu = (GWindowMenu)mMap.create(new GWindowMenu());
        mMap.subMenu.close(false);

        mEdit = gwindowrootmenu.menuBar.addItem(Plugin.i18n("&Edit"), Plugin.i18n("TIPEdit"));
        mEdit.subMenu = (GWindowMenu)mEdit.create(new GWindowMenu());
        mEdit.subMenu.close(false);
        if(bMultiSelect) //By PAL; this corresponds????
            mEdit.subMenu.addItem(new GWindowMenuItem(mEdit.subMenu, Plugin.i18n("&Select_All"), null) {

                public void execute()
                {
                    Plugin.doSelectAll();
                }

            }
);
        if(bMultiSelect) //By PAL; this corresponds????
        mEdit.subMenu.addItem(new GWindowMenuItem(mEdit.subMenu, Plugin.i18n("&Unselect_All"), null) {

            public void execute()
            {
                setSelected(null);
                selectedActors.clear();
            }

        }
);
        GWindowMenuItem gwindowmenuitem = mEdit.subMenu.addItem(new GWindowMenuItem(mEdit.subMenu, Plugin.i18n("&Enable_Select"), null) {

            public void execute()
            {
                setSelected(null);
                selectedActors.clear();
                conf.bEnableSelect = !conf.bEnableSelect;
                bChecked = conf.bEnableSelect;
            }

        }
);
        gwindowmenuitem.bChecked = conf.bEnableSelect;
        mEdit.subMenu.addItem("-", null);
        mEdit.subMenu.addItem(new GWindowMenuItem(mEdit.subMenu, Plugin.i18n("&DeleteAll"), Plugin.i18n("TIPDeleteAll")) {

            public void execute()
            {
                deleteAll();
            }

        }
);
        //By PAL; this corresponds???? if(bMultiSelect);;
        mEdit.subMenu.addItem("-", null);
        gwindowmenuitem = mEdit.subMenu.addItem(new GWindowMenuItem(mEdit.subMenu, Plugin.i18n("&Rotate_Objects"), null) {

            public void execute()
            {
                bRotateObjects = !bRotateObjects;
                bChecked = bRotateObjects;
            }

        }
);
        gwindowmenuitem.bChecked = bRotateObjects;
        mConfigure = gwindowrootmenu.menuBar.addItem(Plugin.i18n("&Configure"), Plugin.i18n("TIPConfigure"));
        mConfigure.subMenu = (GWindowMenu)mConfigure.create(new GWindowMenu());
        mConfigure.subMenu.close(false);
        mView = gwindowrootmenu.menuBar.addItem(Plugin.i18n("&View"), Plugin.i18n("TIPView"));
        mView.subMenu = (GWindowMenu)mView.create(new GWindowMenu());
        mView.subMenu.close(false);
        mSelectItem = mView.subMenu.addItem(new GWindowMenuItem(mView.subMenu, Plugin.i18n("&Object"), Plugin.i18n("TIPObject")) {

            public void execute()
            {
                if(wSelect.isVisible())
                    wSelect.hideWindow();
                else
                    wSelect.showWindow();
            }

        }
);
        mViewLand = mView.subMenu.addItem(new GWindowMenuItem(mView.subMenu, Plugin.i18n("&Landscape"), Plugin.i18n("TIPLandscape")) {

            public void execute()
            {
                if(wViewLand.isVisible())
                    wViewLand.hideWindow();
                else
                    wViewLand.showWindow();
            }

        }
);
        //By PAL; this corresponds???? if(bMultiSelect);;
        mSnap = mView.subMenu.addItem(new GWindowMenuItem(mView.subMenu, Plugin.i18n("&Snap"), null) {

            public void execute()
            {
                if(wSnap.isVisible())
                    wSnap.hideWindow();
                else
                    wSnap.showWindow();
            }

        }
);
        mView.subMenu.addItem("-", null);
        mDisplayFilter = mView.subMenu.addItem(new GWindowMenuItem(mView.subMenu, Plugin.i18n("&DisplayFilter"), Plugin.i18n("TIPDisplayFilter")));
        mDisplayFilter.subMenu = (GWindowMenu)mDisplayFilter.create(new GWindowMenu());
        mDisplayFilter.subMenu.close(false);
        mView.subMenu.addItem("-", null);
        gwindowmenuitem = mView.subMenu.addItem(new GWindowMenuItem(mView.subMenu, Plugin.i18n("&IconSize"), Plugin.i18n("TIPIconSize")));
        gwindowmenuitem.subMenu = (GWindowMenu)gwindowmenuitem.create(new GWindowMenu());
        gwindowmenuitem.subMenu.close(false);
        mIcon8 = gwindowmenuitem.subMenu.addItem(new GWindowMenuItem(gwindowmenuitem.subMenu, "&8", null) {

            public void execute()
            {
                conf.iconSize = 8;
                IconDraw.setScrSize(conf.iconSize, conf.iconSize);
                mIcon8.bChecked = true;
                mIcon16.bChecked = mIcon32.bChecked = mIcon64.bChecked = false;
            }

        }
);
        mIcon16 = gwindowmenuitem.subMenu.addItem(new GWindowMenuItem(gwindowmenuitem.subMenu, "&16", null) {

            public void execute()
            {
                conf.iconSize = 16;
                IconDraw.setScrSize(conf.iconSize, conf.iconSize);
                mIcon16.bChecked = true;
                mIcon8.bChecked = mIcon32.bChecked = mIcon64.bChecked = false;
            }

        }
);
        mIcon32 = gwindowmenuitem.subMenu.addItem(new GWindowMenuItem(gwindowmenuitem.subMenu, "&32", null) {

            public void execute()
            {
                conf.iconSize = 32;
                IconDraw.setScrSize(conf.iconSize, conf.iconSize);
                mIcon32.bChecked = true;
                mIcon8.bChecked = mIcon16.bChecked = mIcon64.bChecked = false;
            }

        }
);
        mIcon64 = gwindowmenuitem.subMenu.addItem(new GWindowMenuItem(gwindowmenuitem.subMenu, "&64", null) {

            public void execute()
            {
                conf.iconSize = 64;
                IconDraw.setScrSize(conf.iconSize, conf.iconSize);
                mIcon64.bChecked = true;
                mIcon8.bChecked = mIcon16.bChecked = mIcon32.bChecked = false;
            }

        }
);
        switch(conf.iconSize)
        {
        case 8: // '\b'
            mIcon8.bChecked = true;
            break;

        case 16: // '\020'
            mIcon16.bChecked = true;
            break;

        case 32: // ' '
            mIcon32.bChecked = true;
            break;

        case 64: // '@'
            mIcon64.bChecked = true;
            break;

        default:
            conf.iconSize = 16;
            mIcon16.bChecked = true;
            break;
        }
        IconDraw.setScrSize(conf.iconSize, conf.iconSize);
        gwindowmenuitem = mView.subMenu.addItem(new GWindowMenuItem(mView.subMenu, Plugin.i18n("Save&ViewHLand"), Plugin.i18n("TIPSaveViewHLand")) {

            public void execute()
            {
                conf.bSaveViewHLand = !conf.bSaveViewHLand;
                bChecked = conf.bSaveViewHLand;
            }

        }
);
        gwindowmenuitem.bChecked = conf.bSaveViewHLand;
        gwindowmenuitem = mView.subMenu.addItem(new GWindowMenuItem(mView.subMenu, Plugin.i18n("Show&Grid"), Plugin.i18n("TIPShowGrid")) {

            public void execute()
            {
                conf.bShowGrid = !conf.bShowGrid;
                bChecked = conf.bShowGrid;
            }

        }
);
        gwindowmenuitem.bChecked = conf.bShowGrid;
        gwindowmenuitem = mView.subMenu.addItem(new GWindowMenuItem(mView.subMenu, Plugin.i18n("&AnimateCamera"), Plugin.i18n("TIPAnimateCamera")) {

            public void execute()
            {
                conf.bAnimateCamera = !conf.bAnimateCamera;
                bChecked = conf.bAnimateCamera;
            }

        }
);

//By PAL, added menu for About
        GWindowMenuBarItem mAbout = gwindowrootmenu.menuBar.addItem(Plugin.i18n("About"), Plugin.i18n("About this F.M.B."));
        mAbout.subMenu = (GWindowMenu)mView.create(new GWindowMenu());
        mAbout.subMenu.close(false);
        mAbout.subMenu.addItem(new GWindowMenuItem(mAbout.subMenu, Plugin.i18n("About"), Plugin.i18n("About this F.M.B.")) {

            public void execute()
            {
                String header = "MissionPro by PAL - Notice";              
                String text = "This implementation of the Full Mission Builder\n\nwas written by PAL (benitomuso).\n\nIt adds several new features for selecting Countries, Planes, Maps... Enjoy!";
                new GWindowMessageBox(clientWindow.root, 20F, true, header, text, 4, 0.0F);
            }

        }
);

        gwindowmenuitem.bChecked = conf.bAnimateCamera;
        wSelect = new WSelect(this, clientWindow);
        wViewLand = new WViewLand(this, clientWindow);
        //By PAL; this corresponds???? if(bMultiSelect);;
        wSnap = new WSnap(this, clientWindow);
        Plugin.doCreateGUI();
        _gridFont = TTFont.font[1];
        smallFont = TTFont.font[0];
        emptyMat = Mat.New("icons/empty.mat");
        selectTargetMat = Mat.New("icons/selecttarget.mat");
        initHotKeys();
        Plugin.doStart();
        HotKeyCmdEnv.enable(envName, false);
        HotKeyCmdEnv.enable("MouseXYZ", false);
    }

    private ArrayList mis_cBoxes;
    private ArrayList mis_properties;
    private ArrayList mis_clipLoc;
    private Point3d mis_clipP0;
    public static String PLUGINS_SECTION = "builder_plugins";
    public static String envName;
    public static float defaultAzimut = 0.0F;
    public static final int colorTargetPrimary = -1;
    public static final int colorTargetSecondary = 0xff00ff00;
    public static final int colorTargetSecret = 0xff7f0000;
    public static float MaxVisualDistance = 16000F;
    public static float saveMaxVisualDistance = 5000F;
    public static float saveMaxStaticVisualDistance = 3000F;
    public BldConfig conf;
    public TTFont smallFont;
    public static final double viewHLandMin = 50D;
    private Actor selectedActor;
    private HashMapExt selectedActors;
    public Camera3D camera;
    public CameraOrtho2D camera2D;
    private MouseXYZATK mouseXYZATK;
    private CursorMesh cursor;
    public boolean bMultiSelect;
    public boolean bSnap;
    public double snapStep;
    public Pathes pathes;
    private boolean bFreeView;
    private double viewDistance;
    private double viewHMax;
    private double viewH;
    private double viewHLand;
    private double viewX;
    private double viewY;
    private boolean bView3D;
    private Point3d _camPoint;
    private Orient _camOrient;
    private Point3d __posScreenToLand;
    private SelectFilter _selectFilter;
    private Point3d __pi;
    private Orient __oi;
    ArrayList _deleted;
    private Actor _selectedActors[];
    private boolean _bSelect;
    private double _selectX0;
    private double _selectY0;
    private double _selectX1;
    private double _selectY1;
    private FilterSelect filterSelect;
    private int saveMouseMode;
    private Loc _savCameraNoFreeLoc;
    private Loc _savCameraFreeLoc;
    public static final int MOUSE_NONE = 0;
    public static final int MOUSE_OBJECT_MOVE = 1;
    public static final int MOUSE_WORLD_ZOOM = 2;
    public static final int MOUSE_MULTISELECT = 3;
    public static final int MOUSE_SELECT_TARGET = 4;
    public static final int MOUSE_FILL = 5;
    //By PAL, new from 4.11
    public static final int MOUSE_SELECT_SPAWNPOINT = 6;    
    public int mouseState;
    int mouseFirstPosX;
    int mouseFirstPosY;
    int mousePosX;
    int mousePosY;
    boolean bMouseRenderRect;
    Actor movedActor;
    Point3d movedActorPosSnap;
    Point3d movedActorPosStepSave;
    private Point3d _objectMoveP;
    protected Point3d projectPos3d;
    private float line5XYZ[];
    private Mat emptyMat;
    private float line2XYZ[];
    protected TTFont _gridFont;
    private int _gridCount;
    private int _gridX[];
    private int _gridY[];
    private int _gridVal[];
    protected Point2d projectPos2d;
    private float lineNXYZ[];
    private int lineNCounter;
    private Actor overActor;
    private Mat selectTargetMat;
    private GWindowMenuPopUp popUpMenu;
    private GWindowMessageBox deletingMessageBox;
    private boolean bDeletingChangeSelection;
    private Actor deletingActor;
    private boolean bRotateObjects;
    public ClientWindow clientWindow;
    public ViewWindow viewWindow;
    public XScrollBar mapXscrollBar;
    public YScrollBar mapYscrollBar;
    public ZSlider mapZSlider;
    public WSelect wSelect;
    public WViewLand wViewLand;
    public WSnap wSnap;
    public GWindowMenuBarItem mFile;
	public GWindowMenuBarItem mMap; //By PAL, to separate maps    
    public GWindowMenuBarItem mEdit;
    public GWindowMenuBarItem mConfigure;
    public GWindowMenuBarItem mView;
    public GWindowMenuItem mSelectItem;
    public GWindowMenuItem mViewLand;
    public GWindowMenuItem mSnap;
    public GWindowMenuItem mAlignItem;
    public GWindowMenuItem mDisplayFilter;
    public GWindowMenuItem mGrayScaleMap;
    public GWindowMenuItem mIcon8;
    public GWindowMenuItem mIcon16;
    public GWindowMenuItem mIcon32;
    public GWindowMenuItem mIcon64;
    private static int _viewPort[] = new int[4];
    //private boolean bPALZoomInUp = Config.cur.ini.get("Mods", "PALZoomInUp", false); //By PAL
    private boolean bMapBuilding = false; //Config.cur.ini.get("Mods", "PALUnlockedFMB", false); //By PAL 

}