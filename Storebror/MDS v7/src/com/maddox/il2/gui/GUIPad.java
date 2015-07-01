/*
 * Modded by PAL, multiple styles of MapPads + Merged with MDS class by |ZUTI| +
 * added few things
 */
package com.maddox.il2.gui;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.maddox.il2.game.ZutiMDSVariables;
import com.maddox.il2.game.ZutiRadarRefresh;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.ZutiSupportMethods_Net;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.vehicles.artillery.AAA;
import com.maddox.rts.RTSConf;
import com.maddox.rts.Time;
import com.maddox.util.HashMapExt;

public class GUIPad
{
	public com.maddox.il2.engine.TTFont	waypointFont;
	public com.maddox.il2.engine.Mat	_iconILS;
	private float						bigFontMultip;
	private com.maddox.JGP.Point3f		_wayP2;

	private float						lineBeamXYZ[];
	// public GMesh meshbrief; //By PAL, new mesh for Frontal briefing view
	public TTFont						smallFont;																							// By PAL, small font

	public static boolean				bStartMission	= true;
	private Main3D						main;
	public GUIClient					client;
	public GWindowFramed				frame;
	public GRegion						frameRegion;
	public float						mapView[]		= {
														// 0.09F, 0.09F, 0.82F, 0.82F Original
														// 0.03F, 0.03F, 0.96F, 0.96F //By PAL, to extend visual area
			0.01F, 0.01F, 0.98F, 0.98F					};
	public GUIRenders					renders;
	public GUIRenders					renders1;
	public RenderMap2D					renderMap2D;
	public CameraOrtho2D				cameraMap2D;
	public RenderMap2D1					renderMap2D1;
	public CameraOrtho2D				cameraMap2D1;
	public boolean						savedUseMesh;
	public int							saveIconDx;
	public int							saveIconDy;
	protected ArrayList					targets;
	public GMesh						mesh;
	public GMesh						meshradar;																							// By PAL, new mesh for radar view
	public GMesh						meshradarF;																						// By PAL, new mesh for Frontal radar view
	public TTFont						gridFont;
	public Mat							emptyMat;
	public Mat							_iconAir;
	public Mat							_iconRadar;																						// By PAL, icon of Planes in the radar
	public Mat							_iconShipRadar;																					// By PAL, icon of Ships planes in the radar
	public Mat							_iconAirField;																						// By PAL, to show icons in navigation map
	private float						scale[]			= { 0.064F, 0.032F, 0.016F, 0.008F, 0.004F, 0.002F, 0.001F, 0.0005F, 0.00025F };
	private int							scales;

	// TODO: Changed by |ZUTI|: from private to public
	// --------------------------------------------------
	public int							curScale;
	//TODO: Added by |ZUTI|
	private int 						lastScale;
	// --------------------------------------------------

	private int							curScaleDirect	= 1;
	public boolean						bActive;
	private float						line2XYZ[];
	private int							_gridCount;
	private int							_gridX[];
	private int							_gridY[];
	private int							_gridVal[];
	private ArrayList					airdrome;
	int									_army[];
	ArmyAccum							armyAccum;
	private ArrayList					airs;
	private Point3f						_wayP;
	private float						lineNXYZ[];
	private int							lineNCounter;

	// TODO: Modified by |ZUTI|: changed from private to public
	// --------------------------------------------------
	public Point3d						OwnPos3d;																							// By PAL, to store position
	public double						OwnAzimut;
	public float						OwnAngle;
	public int							GUIPadMode;
	private GWindowMenuPopUp			popUpMenu;
	public int							FrameOriginX, FrameOriginY, FrameGapX = 0;
	private int							to;																								// By PAL, to make update not permanent
	public String						textDescription;
	private ArrayList					radarPlane;
	private ArrayList					radarOther;

	// --------------------------------------------------

	public class RenderMap2D extends Render
	{
		public void preRender()
		{
			Front.preRender(false);
		}

		public void render()
		{
			// TODO: Added by |ZUTI|
			zutiRenderPad_MDSWay();
		}

		public String Target(int tNum)
		{
			String Descript = "";
			switch (tNum)
			{
				case 0: // '\0'
					Descript = "Destroy";
				case 1: // '\001'
					Descript = "Destroy Ground";
				case 2: // '\002'
					Descript = "Destroy Bridge";
				case 3: // '\003'
					Descript = "Inspect";
				case 4: // '\004'
					Descript = "Escort"; // Originally None
				case 5: // '\005'
					Descript = "Defence"; // Originally Escort
				case 6: // '\006'
					Descript = "Defence Ground";
				case 7: // '\007'
					Descript = "Defence Bridge";
			}
			return Descript;
		}

		public RenderMap2D(Renders renders2, float f)
		{
			super(renders2, f);
			useClearDepth(false);
			useClearColor(false);
		}
	}

	/*
	 * public int drawText(float f, float f1, String s, int i, int j, float f2,
	 * float f3, int k) { GCanvas gcanvas = frame.root.C; gcanvas.cur.x = f +
	 * gcanvas.org.x; gcanvas.cur.y = f1 + gcanvas.org.y; GFont gfont =
	 * gcanvas.font; int l = 0; while (j
	 * > 0 && k != 0) { int i1 = j; int j1 = s.indexOf('\n', i); if (j1 >= 0) i1
	 * = j1 - i; if (i1 > 0) { while (i1 > 0 && k != 0) { int k1 = gfont.len(s,
	 * i, i1, f2, true, true); if (k1 == 0) k1 = gfont.len(s, i, i1, f2, true,
	 * false); if (k1 == 0)
	 * return l; gcanvas.draw(s, i, k1); gridFont.output(0xffffffff, f, l * f3,
	 * 0.0F, s); gcanvas.cur.y += f3; l++; j -= k1; i += k1; i1 -= k1; k--; for
	 * (; i1 > 0; i1--) { if (s.charAt(i) != ' ') break; i++; j--; } } if (j1 >=
	 * 0) { i++; j--; } } else
	 * { gcanvas.cur.y += f3; l++; j--; i++; k--; } } return l; }
	 */

	public class RenderMap2D1 extends Render
	{
		public void preRender()
		{
		}

		public void render()
		{
			if (GUIPadMode == 1 || GUIPadMode == 2 || GUIPadMode == 3) // By PAL, default => normal or relative
			{
				renders1.draw(0.0F, 0.0F, renders1.win.dx, renders1.win.dy, mesh);
			}
			else if (GUIPadMode == 4) // By PAL, radar mode
			{
				renders1.draw(0.0F, 0.0F, renders1.win.dx, renders1.win.dy, meshradar);
			}
			if (GUIPadMode == 5) // By PAL, radar mode
			{
				renders1.draw(0.0F, 0.0F, renders1.win.dx, renders1.win.dy, meshradarF);
			}
		}

		public RenderMap2D1(Renders renders2, float f)
		{
			super(renders2, f);
			useClearDepth(false);
			useClearColor(false);
		}
	}

	class ArmyAccum implements Accumulator
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

	// TODO: Changed by |ZUTI|: added public access modifier to class and it's variables
	public class AirDrome
	{
		public Airport	airport;
		public int		color;
		public int		army;

		// TODO: Added by |ZUTI|
		// -------------------------------------
		public boolean	zutiIsOnShip		= false;
		public boolean	zutiRenderAirdrome	= true;
		// -------------------------------------
	}

	public boolean isActive()
	{
		return bActive;
	}

	public void enter()
	{
		if (bActive)
			return;

		/*
		 * for( int i=0; i<Mission.cur().actors.size(); i++ )
		 * {
		 * System.out.println("ACTOR CLASS=" +
		 * Mission.cur().actors.get(i).getClass());
		 * }
		 */
		//World.cur().scoreCounter.zutiAddEnemyDestroyed();
		// ZutiWeaponsManagement.weaponPrintout(World.getPlayerAircraft());
		// ZutiSupportMethods_Air.emptyAircraft( World.getPlayerAircraft() );

		frameRegion.set(0.05F, 0.1F, 0.35F, 0.6F); // By PAL, to restore size to the normal
		bActive = true;
		GUI.activate(true, false);
		client.showWindow();
		float f = client.root.win.dx;
		float f1 = client.root.win.dy;
		GUIPadMode = Config.cur.ini.get("game", "mapPadMode", 1); // By PAL, store mode
		frameRegion.x = Config.cur.ini.get("game", "mapPadX", frameRegion.x);
		frameRegion.y = Config.cur.ini.get("game", "mapPadY", frameRegion.y);

		// TODO: Added by |ZUTI|
		// ------------------------------------------------------------
		zutiZoomingOldMode = Config.cur.ini.get("game", "mapZoomMode", 0) == 1 ? true : false;
		zutiMouseWheelDirection = Config.cur.ini.get("game", "mapWheelMode", 1);
		// ------------------------------------------------------------

		if (GUIPadMode == 1 || GUIPadMode == 2 || GUIPadMode == 6) // By PAL, normal or relative mode
		{
			if (FrameGapX > 0)
			{
				frameRegion.x += FrameGapX / f;
				FrameGapX = 0;
			}
			frame.setPosSize(frameRegion.x * f, frameRegion.y * f1, frameRegion.dx * f, frameRegion.dy * f1); // Rectangular for normal maps
		}
		else if (GUIPadMode == 3) // By PAL => Full navigation map
		{
			if ((frameRegion.x + frameRegion.dx * World.land().getSizeX() / World.land().getSizeY()) <= 1)
			{
				FrameGapX = 0;
				frame.setPosSize(frameRegion.x * f, frameRegion.y * f1, frameRegion.dx * f * World.land().getSizeX() / World.land().getSizeY(), frameRegion.dx * f); // To make it squared for navigation
			}
			else
			{
				FrameGapX = (int) (frameRegion.x * f + frameRegion.dx * f * World.land().getSizeX() / World.land().getSizeY() - client.root.win.dx);
				frame.setPosSize(frameRegion.x * f - FrameGapX, frameRegion.y * f1, frameRegion.dx * f * World.land().getSizeX() / World.land().getSizeY(), frameRegion.dx * f); // To make it squared for navigation
			}
		}
		if (GUIPadMode == 4 || GUIPadMode == 5) // By PAL => Radar
		{
			if (FrameGapX > 0)
			{
				frameRegion.x += FrameGapX / f;
				FrameGapX = 0;
			}
			frame.setPosSize(frameRegion.x * f, frameRegion.y * f1, frameRegion.dx * f, frameRegion.dx * f); // To make it squared for Radar
		}
		FrameOriginX = (int) (renderMap2D.getViewPortWidth() / 2 + 0.5F); // By PAL, to avoid problem when finishing
		FrameOriginY = (int) (renderMap2D.getViewPortHeight() / 2 + 0.5F);
		cameraMap2D.set(0.0F, renderMap2D.getViewPortWidth(), 0.0F, renderMap2D.getViewPortHeight());
		cameraMap2D1.set(0.0F, renderMap2D1.getViewPortWidth(), 0.0F, renderMap2D1.getViewPortHeight());
		computeScales();
		scaleCamera();
		if (bStartMission)
		{
			// TODO: Added by |ZTUI|
			// --------------------------------------------------------------
			// Reset radars!
			ZutiRadarRefresh.findRadars(ZutiSupportMethods.getPlayerArmy());
			targets.clear();
			// Reset start timers
			ZutiRadarRefresh.resetStartTimes();
			// Added by |ZUTI|
			ZutiSupportMethods_GUI.fillNeutralHomeBases(zutiNeutralHomeBases);
			// Load targets
			if (Mission.cur() != null)
				ZutiSupportMethods_GUI.fillTargetPoints(Mission.cur().sectFile());
			// Fill ground targets. Use different approach because Engine.targets actors
			// are without icons if actor is ground unit...
			ZutiSupportMethods_GUI.fillGroundChiefsArray(this);
			ZutiRadarRefresh.update(lastScale < curScale);
            lastScale = curScale;
			// --------------------------------------------------------------

			// Original lines
			/*
			 * targets.clear(); if (Mission.isSingle() || Mission.isCoop())
			 * GUIBriefing.fillTargets(Mission.cur().sectFile(), targets);
			 */
		}
		if (!World.cur().diffCur.No_Map_Icons || bStartMission)
		{
			Actor actor = Actor.getByName("camera");
			float f2;
			float f3;
			if (Actor.isValid(actor))
			{
				Point3d point3d = actor.pos.getAbsPoint();
				f2 = (float) point3d.x;
				f3 = (float) point3d.y;
			}
			else
			{
				f2 = World.land().getSizeX() / 2.0F;
				f3 = World.land().getSizeY() / 2.0F;
			}
			setPosCamera(f2, f3);
			bStartMission = false;
		}
		frame.activateWindow();
		savedUseMesh = main.guiManager.isUseGMeshs();
		saveIconDx = IconDraw.scrSizeX();
		saveIconDy = IconDraw.scrSizeY();
		main.guiManager.setUseGMeshs(false);
		{
			String s = Main.cur().currentMissionFile.fileName(); // By PAL, to load only original name
			for (int j = s.length() - 1; j > 0; j--)
			{
				char c = s.charAt(j);
				if (c == '\\' || c == '/')
					break;
				if (c != '.')
					continue;
				s = s.substring(0, j);
				break;
			}

			// TODO: Added by |ZUTI|: try/catch block
			// --------------------------------------------------
			try
			{
				ResourceBundle resourcebundle = ResourceBundle.getBundle(s, RTSConf.cur.locale);
				textDescription = " Original Brief for the mission:\n\n" + resourcebundle.getString("Description");
			}
			catch (Exception ex)
			{
			}
			// --------------------------------------------------
		}
	}

	public void leave(boolean flag)
	{
		if (!bActive)
			return;
		bActive = false;
		float f = client.root.win.dx;
		float f1 = client.root.win.dy;
		frameRegion.x = frame.win.x / f;
		frameRegion.y = frame.win.y / f1;
		frameRegion.dx = frame.win.dx / f;
		frameRegion.dy = frame.win.dy / f1;

		if (FrameGapX > 0) // By PAL, to reposition the navigation map
		{
			frameRegion.x += FrameGapX / f;
			FrameGapX = 0;
		}

		Config.cur.ini.set("game", "mapPadX", frameRegion.x);
		Config.cur.ini.set("game", "mapPadY", frameRegion.y);
		Config.cur.ini.set("game", "mapPadMode", GUIPadMode);

		// TODO: Added by |ZUTI|
		// ------------------------------------------------------------
		Config.cur.ini.set("game", "mapZoomMode", ZutiSupportMethods.boolToInt(zutiZoomingOldMode));
		Config.cur.ini.set("game", "mapWheelMode", zutiMouseWheelDirection);
		// ------------------------------------------------------------

		client.hideWindow();
		Main3D.cur3D().guiManager.setUseGMeshs(savedUseMesh);
		IconDraw.setScrSize(saveIconDx, saveIconDy);
		if (!flag)
			GUI.unActivate();
	}

	private void setPosCamera(float f, float f1)
	{
		float f2 = (float) ((double) (cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale);
		cameraMap2D.worldXOffset = f - f2 / 2.0F;
		float f3 = (float) ((double) (cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale);
		cameraMap2D.worldYOffset = f1 - f3 / 2.0F;
		clipCamera();
	}

	private void scaleCamera()
	{
		cameraMap2D.worldScale = (scale[curScale] * client.root.win.dx) / 1024F;
	}

	private void clipCamera()
	{
		if (cameraMap2D.worldXOffset < -Main3D.cur3D().land2D.worldOfsX())
			cameraMap2D.worldXOffset = -Main3D.cur3D().land2D.worldOfsX();
		float f = (float) ((double) (cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale);
		if (cameraMap2D.worldXOffset > Main3D.cur3D().land2D.mapSizeX() - Main3D.cur3D().land2D.worldOfsX() - (double) f)
			cameraMap2D.worldXOffset = Main3D.cur3D().land2D.mapSizeX() - Main3D.cur3D().land2D.worldOfsX() - (double) f;
		if (cameraMap2D.worldYOffset < -Main3D.cur3D().land2D.worldOfsY())
			cameraMap2D.worldYOffset = -Main3D.cur3D().land2D.worldOfsY();
		float f1 = (float) ((double) (cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale);
		if (cameraMap2D.worldYOffset > Main3D.cur3D().land2D.mapSizeY() - Main3D.cur3D().land2D.worldOfsY() - (double) f1)
			cameraMap2D.worldYOffset = Main3D.cur3D().land2D.mapSizeY() - Main3D.cur3D().land2D.worldOfsY() - (double) f1;
	}

	private void computeScales()
	{
		float f = (renders.win.dx * 1024F) / client.root.win.dx;
		float f1 = (renders.win.dy * 768F) / client.root.win.dy;
		int i = 0;
		float f2 = 0.064F;
		for (; i < scale.length; i++)
		{
			scale[i] = f2;
			float f3 = (float) (Main3D.cur3D().land2D.mapSizeX() * (double) f2);
			if (f3 < f)
				break;
			float f5 = (float) (Main3D.cur3D().land2D.mapSizeY() * (double) f2);
			if (f5 < f1)
				break;
			f2 /= 2.0F;
		}

		scales = i;
		if (scales < scale.length)
		{
			float f4 = f / (float) Main3D.cur3D().land2D.mapSizeX();
			float f6 = f1 / (float) Main3D.cur3D().land2D.mapSizeY();
			scale[i] = f4;
			if (f6 > f4)
				scale[i] = f6;
			scales = i + 1;
		}
		if (curScale >= scales)
			curScale = scales - 1;
		if (curScale < 0)
			curScale = 0;
	}

	// TODO: Changed by |ZUTI|: from private to public
	public void drawGrid2DFixed() // By PAL, for Radar and relative mode
	{
		int i = gridStep();
		// int j = 0;//(int)((cameraMap2D.worldXOffset + Main3D.cur3D().land2D.worldOfsX()) / (double)i);
		// int k = 0;//(int)((cameraMap2D.worldYOffset + Main3D.cur3D().land2D.worldOfsY()) / (double)i);
		double d = (double) (cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale;
		double d1 = (double) (cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale;
		int l = (int) (d / (double) i + 0.5F) + 2;
		int i1 = (int) (d1 / (double) i + 0.5F) + 2;
		float f2 = (float) ((double) (l * i) * cameraMap2D.worldScale);
		float f3 = (float) ((double) (i1 * i) * cameraMap2D.worldScale);
		float f4 = (float) ((double) i * cameraMap2D.worldScale);
		float f = (renderMap2D.getViewPortWidth() / 2);// 0;//(float)(((double)(j * i) - cameraMap2D.worldXOffset - Main3D.cur3D().land2D.worldOfsX()) * cameraMap2D.worldScale + 0.5D);
		float f1 = (renderMap2D.getViewPortHeight() / 2);// 0;//(float)(((double)(k * i) - cameraMap2D.worldYOffset - Main3D.cur3D().land2D.worldOfsY()) * cameraMap2D.worldScale + 0.5D);
		_gridCount = 0;
		smallFont = TTFont.font[0];
		Render.drawBeginLines(-1);
		if (GUIPadMode == 4)
		{ // By PAL, draw radial beam
			lineBeamXYZ[0] = lineBeamXYZ[6];
			lineBeamXYZ[1] = lineBeamXYZ[7];
			lineBeamXYZ[2] = 0.0F;
			lineBeamXYZ[3] = f;
			lineBeamXYZ[4] = f1;
			lineBeamXYZ[5] = 0.0F;
			lineBeamXYZ[6] = (float) (1 - Math.cos(Time.current() / 1000.0F)) * f;
			lineBeamXYZ[7] = (float) (1 + Math.sin(Time.current() / 1000.0F)) * f1;
			lineBeamXYZ[8] = 0.0F;
			Render.drawLines(lineBeamXYZ, 3, 2.0F /* 2.0F */, 0xffffffff, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 3);
		}
		// for(int j1 = 0; j1 <= i1; j1++)
		int numH = (int) (i1 / 2);
		for (int j1 = -numH; j1 <= numH; j1++)
		{
			float f5 = f1 + (float) j1 * f4;
			char c = (j1/* + k */) % 10 != 0 ? '\177' : '\300';
			line2XYZ[0] = 0.0F; // f;
			line2XYZ[1] = f5;
			line2XYZ[2] = 0.0F;
			line2XYZ[3] = f2; // f + f2;
			line2XYZ[4] = f5;
			line2XYZ[5] = 0.0F;
			Render.drawLines(line2XYZ, 2, 1.0F, 0xff000000 | c << 16 | c << 8 | c, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
			if (((l >= 12) && (j1 % 5 == 0)) || (l < 12)) // if(j1 % (l /4 + 1) == 0) //drawGridText(0, (int)f5, (k + j1 + numH) * i); //numH by PAL
				smallFont.output(0xff000000, 25, (int) f5 + 2, 0.0F, "" + Math.abs(j1 * i / 1000));
		}
		// gridFont.output(0xffc0c0c0, 0, (int)f5 + 2, 0.0F, "x" + cameraMap2D.worldScale);//_gridVal[i] / 1000);

		// for(int k1 = 0; k1 <= l; k1++)
		int numV = (int) (l / 2);
		for (int k1 = -numV; k1 <= numV; k1++)
		{
			float f6 = f + (float) k1 * f4;
			char c1 = (k1/* + j */) % 10 != 0 ? '\177' : '\300';
			line2XYZ[0] = f6;
			line2XYZ[1] = 0.0F; // f1;
			line2XYZ[2] = 0.0F;
			line2XYZ[3] = f6;
			line2XYZ[4] = f3; // f1 + f3;
			line2XYZ[5] = 0.0F;
			// Try to implement oval circles for radial radar view
			Render.drawLines(line2XYZ, 2, 1.0F, 0xff000000 | c1 << 16 | c1 << 8 | c1, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
			if (((l >= 12) && (k1 % 5 == 0)) || (l < 12)) // drawGridText((int)f6, 0, (j + k1 + numV) * i); //numV y PAL
				smallFont.output(0xff000000, (int) f6 + 2, 25, 0.0F, "" + Math.abs(k1 * i / 1000));
		}
		Render.drawEnd();
		gridFont.output(0xff000000, (int) f - 50, renderMap2D.getViewPortHeight() - 45, 0.0F, (curScale > 2) ? "x1: 10km/div" : "x10: 1km/div"); // Fixed draw labels
		// gridFont.output(0xffc0c0c0, (int)f + 2, 25, 0.0F, ""+(int)Main3D.cur3D().land2D.mapSizeX());//(curScale > 2)? "x1" : "x10"); //Fixed draw labels
		// gridFont.output(0xffc0c0c0, 25, (int) f1 + 2, 0.0F, ""+(int)Main3D.cur3D().land2D.mapSizeY());//(curScale > 2)? "x1" : "x10");
		// drawGridText();
	}

	private void drawGrid2D()
	{
		int i = gridStep();
		int j = (int) ((cameraMap2D.worldXOffset + Main3D.cur3D().land2D.worldOfsX()) / (double) i);
		int k = (int) ((cameraMap2D.worldYOffset + Main3D.cur3D().land2D.worldOfsY()) / (double) i);
		double d = (double) (cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale;
		double d1 = (double) (cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale;
		int l = (int) (d / (double) i) + 2;
		int i1 = (int) (d1 / (double) i) + 2;
		float f = (float) (((double) (j * i) - cameraMap2D.worldXOffset - Main3D.cur3D().land2D.worldOfsX()) * cameraMap2D.worldScale + 0.5D);
		float f1 = (float) (((double) (k * i) - cameraMap2D.worldYOffset - Main3D.cur3D().land2D.worldOfsY()) * cameraMap2D.worldScale + 0.5D);
		float f2 = (float) ((double) (l * i) * cameraMap2D.worldScale);
		float f3 = (float) ((double) (i1 * i) * cameraMap2D.worldScale);
		float f4 = (float) ((double) i * cameraMap2D.worldScale);
		_gridCount = 0;
		Render.drawBeginLines(-1);
		for (int j1 = 0; j1 <= i1; j1++)
		{
			float f5 = f1 + (float) j1 * f4;
			char c = (j1 + k) % 10 != 0 ? '\177' : '\300';
			line2XYZ[0] = f;
			line2XYZ[1] = f5;
			line2XYZ[2] = 0.0F;
			line2XYZ[3] = f + f2;
			line2XYZ[4] = f5;
			line2XYZ[5] = 0.0F;
			Render.drawLines(line2XYZ, 2, 1.0F, 0xff000000 | c << 16 | c << 8 | c, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
			if (c == '\300')
				drawGridText(0, (int) f5, (k + j1) * i);
		}

		for (int k1 = 0; k1 <= l; k1++)
		{
			float f6 = f + (float) k1 * f4;
			char c1 = (k1 + j) % 10 != 0 ? '\177' : '\300';
			line2XYZ[0] = f6;
			line2XYZ[1] = f1;
			line2XYZ[2] = 0.0F;
			line2XYZ[3] = f6;
			line2XYZ[4] = f1 + f3;
			line2XYZ[5] = 0.0F;
			Render.drawLines(line2XYZ, 2, 1.0F, 0xff000000 | c1 << 16 | c1 << 8 | c1, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
			if (c1 == '\300')
				drawGridText((int) f6, 0, (j + k1) * i);
		}

		Render.drawEnd();
		drawGridText();
	}

	private int gridStep()
	{
		float f = cameraMap2D.right - cameraMap2D.left;
		float f1 = cameraMap2D.top - cameraMap2D.bottom;
		double d = f;
		if (f1 < f)
			d = f1;
		d /= cameraMap2D.worldScale;
		int i = 0x186a0;
		for (int j = 0; j < 5; j++)
		{
			if ((double) (i * 3) <= d)
				break;
			i /= 10;
		}

		return i;
	}

	private void drawGridText(int i, int j, int k)
	{
		if (i < 0 || j < 0 || k <= 0 || _gridCount == 20)
		{
			return;
		}
		else
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
		for (int i = 0; i < _gridCount; i++)
			gridFont.output(0xffc0c0c0, _gridX[i] + 2, _gridY[i] + 2, 0.0F, _gridVal[i] / 1000 + "." + (_gridVal[i] % 1000) / 100);

		_gridCount = 0;
	}

	private void drawAirports()
	{
		Mission mission = Main.cur().mission;

		if (mission == null)
			return;

		int pilotArmy = World.getPlayerArmy();

		Mat mat = IconDraw.get("icons/runaway.mat");
		AirDrome airdrome = null;
		// Point3d point3d = null;

		for (int i = 0; i < this.airdrome.size(); i++)
		{
			airdrome = (AirDrome) this.airdrome.get(i);

			// TODO: Edited by |ZUTI|: color ours with their home base color, if they have home base attached. But do this only the first time minimap is displayed. Don't waste resourcer further.
			if (zutiColorAirfields)
				ZutiSupportMethods.changeAirdromeArmyAndColorToBornPlaceArmyAndColor(airdrome);

			// TODO: Added by |ZUTI|: disable of rendering white airports
			if (airdrome.army != pilotArmy && Mission.MDS_VARIABLES().zutiIcons_HideUnpopulatedAirstripsFromMinimap)
				continue;

			// TODO: Edited by |ZUTI|: enemy airfield will be colored white, like unpopulated ones. Friendly have their own color
			if (Actor.isValid(airdrome.airport) && Actor.isAlive(airdrome.airport))// && (airdrome.army == 0 || airdrome.army == World.getPlayerArmy()))
			{
				// point3d = airdrome.airport.pos.getAbsPoint();
				// float f = (float) ((point3d.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
				// float f_34_ = (float) ((point3d.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);

				// Edit by |ZUTI|: since we are coloring the airfields with home base colors, this is not good since enemy can see which airfields have home bases. So, here we check if
				// airport is of different color than player army. And if so, just make it white, like unpopulated ones. Should hide populated ones nicely.
				if (airdrome.army != pilotArmy)
				{
					// Do not show airports that are on carriers with white color. This reveals CV location to the enemy, even though it's white (it's moving :D).
					if (airdrome.zutiIsOnShip)
						continue;

					IconDraw.setColor(-1);
				}
				else
					IconDraw.setColor(airdrome.color);

				// Disabled |ZUTI| way for enabling Pablos way
				// IconDraw.render(mat, f, f_34_);
				pablo_drawAirports(airdrome.airport.pos.getAbsPoint(), airdrome.color, airdrome.airport.pos.getAbsOrient().azimut(), mat);
			}
		}

		Point3d bpPos = new Point3d(0D, 0D, 0D);
		for (int i = 0; i < ZutiSupportMethods_GUI.STD_HOME_BASE_AIRFIELDS.size(); i++)
		{
			BornPlace bp = (BornPlace) ZutiSupportMethods_GUI.STD_HOME_BASE_AIRFIELDS.get(i);

			// TODO: Added by |ZUTI|: disable of rendering white airports
			if (bp.army != pilotArmy && Mission.MDS_VARIABLES().zutiIcons_HideUnpopulatedAirstripsFromMinimap)
				continue;

			if (bp.army != pilotArmy)
			{
				IconDraw.setColor(-1);
			}
			else
				IconDraw.setColor(bp.zutiColor);

			bpPos.set(bp.place.x, bp.place.y, 0D);
			pablo_drawAirports(bpPos, bp.zutiColor, 0F, mat);
		}
		// TODO: Added by |ZUTI|: Airfields were colored for this mission session, disable it until new mission is loaded.
		zutiColorAirfields = false;
	}

	public void fillAirports()
	{
		// TODO: Added by |ZUTI|
		// ----------------------------------------
		Point3d zutiAirdromePoint = null;
		BornPlace zutiBornPlace = null;
		// ----------------------------------------

		this.airdrome.clear();
		ArrayList arraylist = new ArrayList();
		World.cur();
		World.getAirports(arraylist);
		for (int i = 0; i < arraylist.size(); i++)
		{
			Airport airport = (Airport) arraylist.get(i);
			AirDrome airdrome = new AirDrome();
			airdrome.airport = airport;
			if (airport instanceof AirportCarrier && Actor.isValid(((AirportCarrier) airport).ship()))
			{
				if (World.cur().diffCur.RealisticNavigationInstruments)
					continue;

				airdrome.army = ((AirportCarrier) airport).ship().getArmy();

				// TODO: Added by |ZUTI|
				// --------------------------------------------------
				airdrome.zutiIsOnShip = true;
				// --------------------------------------------------
			}

			// TODO: Added by |ZUTI|
			// --------------------------------------------------------------------
			if (airdrome.airport != null)
			{
				zutiAirdromePoint = airdrome.airport.pos.getAbsPoint();
				zutiBornPlace = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(zutiAirdromePoint.x, zutiAirdromePoint.y);
				if (zutiBornPlace != null)
				{
					// check if airdrome is actually inside born place circle...
					double rr = zutiBornPlace.r * zutiBornPlace.r;
					double tmpDistance = Math.pow(zutiAirdromePoint.x - zutiBornPlace.place.x, 2) + Math.pow(zutiAirdromePoint.y - zutiBornPlace.place.y, 2);
					if (tmpDistance <= rr && zutiBornPlace.zutiDisableRendering)
					{
						airdrome.zutiRenderAirdrome = false;
					}
				}
			}

			if (airdrome.zutiRenderAirdrome)
				this.airdrome.add(airdrome);
			// --------------------------------------------------------------------
			// TODO: Comment by |ZUTI|: disabled original line
			// this.airdrome.add(airdrome);
		}
		Point3d point3d = new Point3d();
		for (int i = 0; i < this.airdrome.size(); i++)
		{
			AirDrome airdrome = (AirDrome) this.airdrome.get(i);
			Point3d point3d_35_ = airdrome.airport.pos.getAbsPoint();
			Point3d point3d_36_ = point3d;
			double d = point3d_35_.x;
			double d_37_ = point3d_35_.y;
			World.land();
			point3d_36_.set(d, d_37_, (double) Landscape.HQ((float) point3d_35_.x, (float) point3d_35_.y));
			int i_38_ = 0;
			if (airdrome.army == 0)
			{
				Engine.collideEnv().getNearestEnemies(point3d, 2000.0, 0, armyAccum);
				int i_39_ = 0;
				for (int i_40_ = 1; i_40_ < _army.length; i_40_++)
				{
					if (i_39_ < _army[i_40_])
					{
						i_39_ = _army[i_40_];
						i_38_ = i_40_;
					}
					_army[i_40_] = 0;
				}
				airdrome.army = i_38_;
			}
			airdrome.color = Army.color(airdrome.army);
		}
	}

	private void drawRadioBeacons()
	{
		double d = cameraMap2D.worldXOffset;
		double d1 = cameraMap2D.worldYOffset;
		double d2 = cameraMap2D.worldXOffset + (double) (cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale;
		double d3 = cameraMap2D.worldYOffset + (double) (cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale;
		com.maddox.il2.objects.air.Aircraft aircraft = com.maddox.il2.ai.World.getPlayerAircraft();
		if (!com.maddox.il2.engine.Actor.isValid(aircraft))
			return;
		java.util.ArrayList arraylist = com.maddox.il2.game.Main.cur().mission.getBeacons(aircraft.getArmy());
		if (arraylist == null)
			return;
		int i = arraylist.size();
		for (int j = 0; j < i; j++)
		{
			com.maddox.il2.engine.Actor actor = (com.maddox.il2.engine.Actor) arraylist.get(j);
			if (!(actor instanceof com.maddox.il2.objects.vehicles.radios.TypeHasBeacon) || (actor instanceof com.maddox.il2.ai.ground.TgtShip)
					|| (actor instanceof com.maddox.il2.objects.vehicles.radios.TypeHasRadioStation) || actor.getArmy() != com.maddox.il2.ai.World.getPlayerAircraft().getArmy())
				continue;
			com.maddox.JGP.Point3d point3d = actor.pos.getAbsPoint();
			if (point3d.x < d || point3d.x > d2 || point3d.y < d1 || point3d.y > d3)
				continue;
			float f = (float) ((point3d.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
			float f1 = (float) ((point3d.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
			int k = com.maddox.il2.ai.Army.color(actor.getArmy());
			if (actor instanceof com.maddox.il2.objects.vehicles.radios.TypeHasLorenzBlindLanding)
			{
				float f2 = actor.pos.getAbsOrient().azimut() + 90F;
				for (f2 = (f2 + 180F) % 360F; f2 < 0.0F; f2 += 360F);
				for (; f2 >= 360F; f2 -= 360F);
				com.maddox.il2.engine.IconDraw.setColor(-1);
				com.maddox.il2.engine.IconDraw.render(_iconILS, f, f1, f2 - 90F);
				float f3 = 20F;
				java.lang.String s = com.maddox.il2.objects.vehicles.radios.Beacon.getBeaconID(j);
				gridFont.output(k, f + f3, f1 - f3, 0.0F, s);
				if (curScale < 2)
				{
					int l = (int) actor.pos.getAbsPoint().z;
					java.lang.String s1 = (int) f2 + "\260";
					gridFont.output(k, f + f3, f1 - f3 - 20F, 0.0F, s1);
					s1 = l + "m";
					gridFont.output(k, f + f3, f1 - f3 - 40F, 0.0F, s1);
				}
				continue;
			}
			com.maddox.il2.engine.Mat mat = com.maddox.il2.engine.IconDraw.create(actor);
			if (mat != null)
			{
				com.maddox.il2.engine.IconDraw.setColor(k);
				com.maddox.il2.engine.IconDraw.render(mat, f, f1);
				float f4 = 20F;
				float f5 = 15F;
				java.lang.String s2 = com.maddox.il2.objects.vehicles.radios.Beacon.getBeaconID(j);
				gridFont.output(k, f + f4, f1 - f5, 0.0F, s2);
			}
		}
	}

	private void drawChiefs()
	{
		HashMapExt hashmapext = Engine.name2Actor();
		for (java.util.Map.Entry entry = hashmapext.nextEntry(null); entry != null; entry = hashmapext.nextEntry(entry))
		{
			Actor actor = (Actor) entry.getValue();
			if (actor instanceof Chief)
			{
				Mat mat = actor.icon;
				if (mat != null)
				{
					Point3d point3d = actor.pos.getAbsPoint();
					if (GUIPadMode == 1 || GUIPadMode == 3)
					{
						float f = (float) ((point3d.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
						float f1 = (float) ((point3d.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
						IconDraw.setColor(Army.color(actor.getArmy()));
						IconDraw.render(mat, f, f1);
					}
					else if (GUIPadMode == 2)
					{
						float NewY = (float) ((((Tuple3d) (point3d)).x - ((Tuple3d) (OwnPos3d)).x) * Math.cos(OwnAzimut))
								- (float) ((-((Tuple3d) (point3d)).y + ((Tuple3d) (OwnPos3d)).y) * Math.sin(OwnAzimut));
						float NewX = (float) ((-((Tuple3d) (point3d)).y + ((Tuple3d) (OwnPos3d)).y) * Math.cos(OwnAzimut))
								+ (float) ((((Tuple3d) (point3d)).x - ((Tuple3d) (OwnPos3d)).x) * Math.sin(OwnAzimut));
						float f = FrameOriginX + (float) (NewX * cameraMap2D.worldScale);
						float f1 = FrameOriginY + (float) (NewY * cameraMap2D.worldScale);
						IconDraw.setColor(Army.color(actor.getArmy()));
						IconDraw.render(mat, f, f1);
					}
				}
			}
			if ((actor instanceof BigshipGeneric) || (actor instanceof BigshipGeneric))
				; // By PAL, to show Ships
			{
				Mat mat = actor.icon;
				if (mat != null)
				{
					Point3d point3d = actor.pos.getAbsPoint();
					if (GUIPadMode == 1 || GUIPadMode == 3)
					{
						float f = (float) ((point3d.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
						float f1 = (float) ((point3d.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
						IconDraw.setColor(Army.color(actor.getArmy()));
						IconDraw.render(mat, f, f1);
					}
					else if (GUIPadMode == 2)
					{
						float NewY = (float) ((((Tuple3d) (point3d)).x - ((Tuple3d) (OwnPos3d)).x) * Math.cos(OwnAzimut))
								- (float) ((-((Tuple3d) (point3d)).y + ((Tuple3d) (OwnPos3d)).y) * Math.sin(OwnAzimut));
						float NewX = (float) ((-((Tuple3d) (point3d)).y + ((Tuple3d) (OwnPos3d)).y) * Math.cos(OwnAzimut))
								+ (float) ((((Tuple3d) (point3d)).x - ((Tuple3d) (OwnPos3d)).x) * Math.sin(OwnAzimut));
						float f = FrameOriginX + (float) (NewX * cameraMap2D.worldScale);
						float f1 = FrameOriginY + (float) (NewY * cameraMap2D.worldScale);
						IconDraw.setColor(Army.color(actor.getArmy()));
						IconDraw.render(mat, f, f1);
					}
				}
			}
		}

	}

	private void drawAAAandFillAir()
	{
		double d = cameraMap2D.worldXOffset;
		double d1 = cameraMap2D.worldYOffset;
		double d2 = cameraMap2D.worldXOffset + (double) (cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale;
		double d3 = cameraMap2D.worldYOffset + (double) (cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale;
		List list = Engine.targets();
		int i = list.size();
		for (int j = 0; j < i; j++)
		{
			Actor actor = (Actor) list.get(j);
			if (actor instanceof Aircraft)
			{
				if (actor != World.getPlayerAircraft())
				{
					Point3d point3d = actor.pos.getAbsPoint();
					if (GUIPadMode == 2 || GUIPadMode == 4 || GUIPadMode == 5)
						airs.add(actor); // By PAL, in relative I allways add actors
					else if (point3d.x >= d && point3d.x <= d2 && point3d.y >= d1 && point3d.y <= d3)
						airs.add(actor);
				}
			}
			else if (actor instanceof AAA)
			{
				Point3d point3d1 = actor.pos.getAbsPoint();
				if (point3d1.x >= d && point3d1.x <= d2 && point3d1.y >= d1 && point3d1.y <= d3)
				{
					Mat mat = IconDraw.create(actor);
					if (mat != null)
					{
						if (GUIPadMode == 1) // By PAL: normal view
						{
							float f = (float) ((point3d1.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
							float f1 = (float) ((point3d1.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
							IconDraw.setColor(Army.color(actor.getArmy()));
							IconDraw.render(mat, f, f1);
						}
						else if (GUIPadMode == 2) // By PAL: relative view
						{
							float NewY = (float) ((((Tuple3d) (point3d1)).x - ((Tuple3d) (OwnPos3d)).x) * Math.cos(OwnAzimut))
									- (float) ((-((Tuple3d) (point3d1)).y + ((Tuple3d) (OwnPos3d)).y) * Math.sin(OwnAzimut));
							float NewX = (float) ((-((Tuple3d) (point3d1)).y + ((Tuple3d) (OwnPos3d)).y) * Math.cos(OwnAzimut))
									+ (float) ((((Tuple3d) (point3d1)).x - ((Tuple3d) (OwnPos3d)).x) * Math.sin(OwnAzimut));
							float f = FrameOriginX + (float) (NewX * cameraMap2D.worldScale);
							float f1 = FrameOriginY + (float) (NewY * cameraMap2D.worldScale);
							IconDraw.setColor(Army.color(actor.getArmy()));
							IconDraw.render(mat, f, f1);
						}
					}
				}
			}
		}
	}

	private void drawAir()
	{
		int i = airs.size();
		for (int j = 0; j < i; j++)
		{
			Actor actor = (Actor) airs.get(j);
			Point3d point3d = actor.pos.getAbsPoint();
			if (GUIPadMode == 1)
			{
				float f = (float) ((((Tuple3d) (point3d)).x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
				float f1 = (float) ((((Tuple3d) (point3d)).y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
				IconDraw.setColor(Army.color(actor.getArmy()));
				IconDraw.render(_iconAir, f, f1, actor.pos.getAbsOrient().azimut());
			}
			else if (GUIPadMode == 2) // By PAL, Relative mode
			{
				float NewY = (float) ((((Tuple3d) (point3d)).x - ((Tuple3d) (OwnPos3d)).x) * Math.cos(OwnAzimut))
						- (float) ((-((Tuple3d) (point3d)).y + ((Tuple3d) (OwnPos3d)).y) * Math.sin(OwnAzimut));
				float NewX = (float) ((-((Tuple3d) (point3d)).y + ((Tuple3d) (OwnPos3d)).y) * Math.cos(OwnAzimut))
						+ (float) ((((Tuple3d) (point3d)).x - ((Tuple3d) (OwnPos3d)).x) * Math.sin(OwnAzimut));
				float f = FrameOriginX + (float) (NewX * cameraMap2D.worldScale);
				float f1 = FrameOriginY + (float) (NewY * cameraMap2D.worldScale);
				IconDraw.setColor(Army.color(actor.getArmy()));
				IconDraw.render(_iconAir, f, f1, actor.pos.getAbsOrient().azimut() - OwnAngle - 90.0F);
			}
			else if (GUIPadMode == 4)
			{
				Aircraft aircraft = World.getPlayerAircraft();
				if (Actor.isValid(aircraft)) // Si el actor no era válido, no lo voy a dibujar
				{
					Point3d pointAC = (World.getPlayerAircraft()).pos.getAbsPoint();
					Orient orientAC = (World.getPlayerAircraft()).pos.getAbsOrient();
					Point3d pointOrtho = new Point3d();
					pointOrtho.set(actor.pos.getAbsPoint());

					pointOrtho.sub(pointAC);
					orientAC.transformInv(pointOrtho);

					double x = ((Tuple3d) (pointOrtho)).x;
					double y = ((Tuple3d) (pointOrtho)).y;
					double z = ((Tuple3d) (pointOrtho)).z;

					double FOV = Math.sqrt(x * x + y * y + z * z) / Math.sqrt(x * x + y * y); // Math.abs(((Tuple3d)(pointOrtho)).z) / 5.0F;

					double NewX = -y * FOV;
					double NewY = x * FOV;

					float f = FrameOriginX + (float) (NewX * cameraMap2D.worldScale);
					float f1 = FrameOriginY + (float) (NewY * cameraMap2D.worldScale);

					int size = (int) Math.round(((10 - curScale) * 2D * (double) client.root.win.dx) / 1024D); // By PAL 32D
					IconDraw.setScrSize(size, size);

					IconDraw.setColor(0xff00ffff); // Green radar
					IconDraw.render(_iconRadar, f, f1, 0.0F);// actor.pos.getAbsOrient().azimut() - orientAC.azimut() - 90.0F);
				}
			}
			else if (GUIPadMode == 5)
			{
				Aircraft aircraft = World.getPlayerAircraft();
				if (Actor.isValid(aircraft)) // Si el actor no era válido, no lo voy a dibujar
				{
					Point3d pointAC = (World.getPlayerAircraft()).pos.getAbsPoint();
					Orient orientAC = (World.getPlayerAircraft()).pos.getAbsOrient();
					Point3d pointOrtho = new Point3d();
					pointOrtho.set(actor.pos.getAbsPoint());

					pointOrtho.sub(pointAC);
					orientAC.transformInv(pointOrtho);

					double x = ((Tuple3d) (pointOrtho)).x;
					if (x > 5.0F) // Mayor que 5m, en frente del radar
					{
						double y = ((Tuple3d) (pointOrtho)).y;
						double z = ((Tuple3d) (pointOrtho)).z;

						double FOV = 1; // Math.abs(((Tuple3d)(pointOrtho)).x) / 5.0F;

						double NewX = -y * FOV;
						double NewY = z * FOV;

						float f = FrameOriginX + (float) (NewX * cameraMap2D.worldScale);
						float f1 = FrameOriginY + (float) (NewY * cameraMap2D.worldScale);

						int size = (int) Math.round(((20 - curScale) * 2D * (double) client.root.win.dx) / 1024D / (1 + x / 800)); // By PAL 32D
						IconDraw.setScrSize(size, size);

						IconDraw.setColor(0xff00ffff); // Green radar
						IconDraw.render(_iconRadar, f, f1, 0.0F);// actor.pos.getAbsOrient().kren() - orientAC.kren() - 360.0F);
					}
				}
			}
		}
		// By PAL, I will clear the airs in FillAir!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		airs.clear();
	}

	private Mat getIconAir(int i)
	{
		String s = null;
		switch (i)
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
		if (autopilotage != null)
		{
			Way way = autopilotage.way;
			if (way != null)
			{
				int i = way.Cur();
				int i_62_ = way.size();
				if (i_62_ > 0 && i < i_62_)
				{
					if (lineNXYZ.length / 3 <= i_62_)
						lineNXYZ = new float[(i_62_ + 1) * 3];
					lineNCounter = 0;
					int i_63_ = 0;
					Render.drawBeginLines(-1);
					while (i_63_ < i_62_)
					{
						WayPoint waypoint = way.get(i_63_++);
						waypoint.getP(_wayP);
						lineNXYZ[lineNCounter * 3 + 0] = (float) (((double) _wayP.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
						lineNXYZ[lineNCounter * 3 + 1] = (float) (((double) _wayP.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
						lineNXYZ[lineNCounter * 3 + 2] = 0.0F;
						lineNCounter++;
					}
					Render.drawLines(lineNXYZ, lineNCounter, 2.0F, -16777216, (Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND), 3);
					if (!World.cur().diffCur.No_Player_Icon && !(World.cur().diffCur.RealisticNavigationInstruments))
					{
						Point3d point3d = World.getPlayerAircraft().pos.getAbsPoint();
						lineNXYZ[0] = (float) ((point3d.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
						lineNXYZ[1] = (float) ((point3d.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
						lineNXYZ[2] = 0.0F;
						WayPoint waypoint = way.get(i);
						waypoint.getP(_wayP);
						lineNXYZ[3] = (float) (((double) _wayP.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
						lineNXYZ[4] = (float) (((double) _wayP.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
						lineNXYZ[5] = 0.0F;
						Render.drawLines(lineNXYZ, 2, 2.0F, -16777216, (Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND), 3);
					}
					float f = 0.0F;
					Render.drawEnd();
					i_63_ = 0;
					while (i_63_ < i_62_)
					{
						WayPoint waypoint = way.get(i_63_++);
						waypoint.getP(_wayP);
						float f_64_ = (float) (((double) _wayP.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
						float f_65_ = (float) (((double) _wayP.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
						IconDraw.render(getIconAir(waypoint.Action), f_64_, f_65_);
						if (curScale < (int) (4.0F - bigFontMultip) && i_63_ < i_62_)
						{
							WayPoint waypoint_66_ = way.get(i_63_);
							waypoint_66_.getP(_wayP2);
							_wayP.sub(_wayP2);
							float f_67_ = (57.32484F * (float) Math.atan2((double) _wayP.x, (double) _wayP.y));
							for (f_67_ = (f_67_ + 180.0F) % 360.0F; f_67_ < 0.0F; f_67_ += 360.0F);
							for (; f_67_ >= 360.0F; f_67_ -= 360.0F);
							f_67_ = (float) Math.round(f_67_);
							double d = (Math.sqrt((double) (_wayP.y * _wayP.y + _wayP.x * _wayP.x)) / 1000.0);
							if (!(d < 1.0))
							{
								String string = "km";
								if (HUD.drawSpeed() == 2 || HUD.drawSpeed() == 3)
								{
									d *= 0.5399569869041443;
									string = "nm";
								}
								d = (double) Math.round(d);
								float f_68_ = 0.0F;
								float f_69_ = 0.0F;
								if (f_67_ >= 0.0F && f_67_ < 90.0F)
								{
									f_68_ = 15.0F;
									f_69_ = -20.0F;
									if (f >= 270.0F && f <= 360.0F)
									{
										f_68_ = -35.0F * bigFontMultip;
										f_69_ = 30.0F * bigFontMultip;
									}
								}
								else if (f_67_ >= 90.0F && f_67_ < 180.0F)
								{
									f_68_ = 15.0F;
									f_69_ = 30.0F * bigFontMultip;
									if (f >= 180.0F && f < 270.0F)
									{
										f_68_ = -35.0F * bigFontMultip;
										f_69_ = -20.0F;
									}
								}
								else if (f_67_ >= 180.0F && f_67_ < 270.0F)
								{
									f_68_ = -35.0F * bigFontMultip;
									f_69_ = 30.0F * bigFontMultip;
									if (f >= 90.0F && f < 180.0F)
									{
										f_68_ = 15.0F;
										f_69_ = 30.0F * bigFontMultip;
									}
								}
								else if (f_67_ >= 270.0F && f_67_ <= 360.0F)
								{
									f_68_ = -35.0F * bigFontMultip;
									f_69_ = -20.0F;
									if (f >= 0.0F && f < 90.0F)
									{
										f_68_ = 15.0F;
										f_69_ = -20.0F;
									}
								}
								f = f_67_;
								waypointFont.output(-16777216, f_64_ + f_68_, f_65_ + f_69_ - 0.0F, 0.0F, "" + i_63_);
								waypointFont.output(-16777216, f_64_ + f_68_, (f_65_ + f_69_ - 12.0F * bigFontMultip), 0.0F, (int) f_67_ + "\u00b0");
								waypointFont.output(-16777216, f_64_ + f_68_, (f_65_ + f_69_ - 24.0F * bigFontMultip), 0.0F, (int) d + string);
							}
						}
					}
				}
			}
		}
	}

	public GUIPad THIS()
	{
		return this;
	}

	public GUIPad(GWindowRoot gwindowroot)
	{
		// TODO: Added by |ZUTI|
		// ---------------------------------------------
		iconBornPlace = IconDraw.get("icons/born.mat");

		lineBeamXYZ = new float[9]; // By PAL, for radar Beam (2 lines)
		lineBeamXYZ[6] = 0.0F;
		lineBeamXYZ[7] = 0.0F;
		// ---------------------------------------------

		bigFontMultip = 1.0F;
		_wayP2 = new Point3f();

		frameRegion = new GRegion(0.05F, 0.1F, 0.35F, 0.6F);
		targets = new ArrayList();
		scales = scale.length;
		curScale = 0;
		bActive = false;
		line2XYZ = new float[6];
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
		lineNXYZ = new float[6];
		client = (GUIClient) gwindowroot.create(new GUIClient()
		{

			public void render()
			{
				int i = client.root.C.alpha;
				client.root.C.alpha = 255;
				super.render();
				client.root.C.alpha = i;
			}

		});
		frame = (GWindowFramed) client.create(0.0F, 0.0F, 1.0F, 1.0F, false, new GWindowFramed()
		{

			public void resized()
			{
				super.resized();
				if (renders != null)
					renders.setPosSize(win.dx * mapView[0], win.dy * mapView[1], win.dx * mapView[2], win.dy * mapView[3]);
				if (renders1 != null)
					renders1.setPosSize(0.0F, 0.0F, win.dx, win.dy);
			}

			public void render()
			{
			}
		});
		frame.bSizable = false;
		renders = new GUIRenders(frame, 0.0F, 0.0F, 1.0F, 1.0F, false);
		renders1 = new GUIRenders(frame, 0.0F, 0.0F, 1.0F, 1.0F, false)
		{
			public void doPopUpMenu()
			{
				// if (popUpMenu == null)
				{
					if (World.cur().diffCur.No_Map_Icons)
					{
						popUpMenu = (GWindowMenuPopUp) create(new GWindowMenuPopUp());
						popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Normal_Mode"), Plugin.i18n("Normal Mode"))
						{
							public void execute()
							{
								GUIPadMode = 1;
								leave(false);
								// frameRegion = new GRegion(0.05F, 0.1F, 0.35F, 0.6F);
								enter();
							}
						});
						popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Navigation_Map"), Plugin.i18n("Navigation Map"))
						{
							public void execute()
							{
								GUIPadMode = 3;
								leave(false);
								// frameRegion.set(0.05F, 0.1F, 0.6F, 0.6F);
								// int Alpha = frame.root.C.alpha;
								// renders1.root.C.alpha = 128; //By PAL, Make it semi transparent
								enter();
								// renders1.root.C.alpha = Alpha; //By PAL, return it to the original transparency level
							}
						});
						popUpMenu.addItem("-", null);
						popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Mission_Brief"), Plugin.i18n("Mission Brief"))
						{
							public void execute()
							{
								GUIPadMode = 6;
								leave(false);
								// frameRegion = new GRegion(0.05F, 0.1F, 0.35F, 0.6F);
								enter();
							}
						});
						popUpMenu.addItem("-", null);
						popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Revert_zoom_axis"), Plugin.i18n("Revert mouse wheel/zoom axis"))
						{
							public void execute()
							{
								// TODO:
								zutiMouseWheelDirection = zutiMouseWheelDirection * -1;
								leave(false);
								// frameRegion = new GRegion(0.05F, 0.1F, 0.35F, 0.6F);
								enter();
							}
						});
						popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("IL2_zoom_mode"), Plugin.i18n("Old Zooming"))
						{
							public void execute()
							{
								zutiZoomingOldMode = !zutiZoomingOldMode;
								leave(false);
								// frameRegion = new GRegion(0.05F, 0.1F, 0.35F, 0.6F);
								enter();
							}
						});

					}
					else
					{
						popUpMenu = (GWindowMenuPopUp) create(new GWindowMenuPopUp());
						popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Normal_Mode"), Plugin.i18n("Normal Mode"))
						{
							public void execute()
							{
								GUIPadMode = 1;
								leave(false);
								// frameRegion = new GRegion(0.05F, 0.1F, 0.35F, 0.6F);
								enter();
							}
						});
						popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Relative_Mode"), Plugin.i18n("Relative Mode"))
						{
							public void execute()
							{
								GUIPadMode = 2;
								leave(false);
								// frameRegion = new GRegion(0.05F, 0.1F, 0.35F, 0.6F);
								enter();
							}
						});
						popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Navigation_Map"), Plugin.i18n("Navigation Map"))
						{
							public void execute()
							{
								GUIPadMode = 3;
								leave(false);
								// frameRegion.set(0.05F, 0.1F, 0.6F, 0.6F);
								// int Alpha = frame.root.C.alpha;
								// renders1.root.C.alpha = 128; //By PAL, Make it semi transparent
								enter();
								// renders1.root.C.alpha = Alpha; //By PAL, return it to the original transparency level
							}
						});
						popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Radar_View_Radial"), Plugin.i18n("Radar View (Radial)"))
						{
							public void execute()
							{
								GUIPadMode = 4;
								leave(false);
								// frameRegion.set(0.05F, 0.1F, 0.35F, 0.42F);
								enter();
							}
						});
						popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Radar_View_Frontal"), Plugin.i18n("Radar View (Frontal)"))
						{
							public void execute()
							{
								GUIPadMode = 5;
								leave(false);
								// frameRegion.set(0.05F, 0.1F, 0.35F, 0.42F);
								enter();
							}
						});
						popUpMenu.addItem("-", null);
						popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Mission_Brief"), Plugin.i18n("Mission Brief"))
						{
							public void execute()
							{
								GUIPadMode = 6;
								leave(false);
								// frameRegion = new GRegion(0.05F, 0.1F, 0.35F, 0.6F);
								enter();
							}
						});
						popUpMenu.addItem("-", null);
						popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("Revert_zoom_axis"), Plugin.i18n("Revert mouse wheel/zoom axis"))
						{
							public void execute()
							{
								// TODO:
								zutiMouseWheelDirection = zutiMouseWheelDirection * -1;
								leave(false);
								// frameRegion = new GRegion(0.05F, 0.1F, 0.35F, 0.6F);
								enter();
							}
						});
						popUpMenu.addItem(new GWindowMenuItem(popUpMenu, Plugin.i18n("IL2_zoom_mode"), Plugin.i18n("Old Zooming"))
						{
							public void execute()
							{
								zutiZoomingOldMode = !zutiZoomingOldMode;
								leave(false);
								// frameRegion = new GRegion(0.05F, 0.1F, 0.35F, 0.6F);
								enter();
							}
						});

					}

					Plugin.builder.mEdit.subMenu.addItem(2, "-", null);

					GPoint MPos = getMouseXY();
					popUpMenu.setPos(MPos.x, MPos.y);
					for (int i = 1; i < popUpMenu.size(); i++)
						popUpMenu.getItem(i).bChecked = false;

					if (World.cur().diffCur.No_Map_Icons)
					{
						switch (GUIPadMode)
						{
							case 1:
								popUpMenu.getItem(0).bChecked = true;
							break;
							case 3:
								popUpMenu.getItem(1).bChecked = true;
							break;
							case 6:
								popUpMenu.getItem(3).bChecked = true;
							break;
							default:
							break;
						}
					}
					else
					{
						switch (GUIPadMode)
						{
							case 1:
								popUpMenu.getItem(0).bChecked = true;
							break;
							case 2:
								popUpMenu.getItem(1).bChecked = true;
							break;
							case 3:
								popUpMenu.getItem(2).bChecked = true;
							break;
							case 4:
								popUpMenu.getItem(3).bChecked = true;
							break;
							case 5:
								popUpMenu.getItem(4).bChecked = true;
							break;
							case 6:
								popUpMenu.getItem(6).bChecked = true;
							break;
							default:
							break;
						}
					}
				}
				// else
				{
					if (popUpMenu.isVisible())
						return;
					// popUpMenu.clearItems();
					if (popUpMenu.size() > 0)
					{
						GPoint MPos = getMouseXY();
						popUpMenu.setPos(MPos.x, MPos.y);
						for (int i = 1; i < popUpMenu.size(); i++)
							popUpMenu.getItem(i).bChecked = false;

						if (World.cur().diffCur.No_Map_Icons)
						{
							switch (GUIPadMode)
							{
								case 1:
									popUpMenu.getItem(0).bChecked = true;
								break;
								case 3:
									popUpMenu.getItem(1).bChecked = true;
								break;
								case 6:
									popUpMenu.getItem(3).bChecked = true;
								break;
								default:
								break;
							}
						}
						else
						{
							switch (GUIPadMode)
							{
								case 1:
									popUpMenu.getItem(0).bChecked = true;
								break;
								case 2:
									popUpMenu.getItem(1).bChecked = true;
								break;
								case 3:
									popUpMenu.getItem(2).bChecked = true;
								break;
								case 4:
									popUpMenu.getItem(3).bChecked = true;
								break;
								case 5:
									popUpMenu.getItem(4).bChecked = true;
								break;
								case 6:
									popUpMenu.getItem(6).bChecked = true;
								break;
								default:
								break;
							}
						}
						popUpMenu.showModal();
					}
				}
			}

			public void mouseButton_oldWay(int i, boolean bool, float f, float f_58_)
			{
				if (i == 0)
				{
					bLPressed = bool;
					mouseCursor = bLPressed ? 3 : 1;
				}
				else if (i == 1 && scales > 1)
				{
					bRPressed = bool;
					if (bRPressed && !bLPressed)
					{
						f -= THIS().renders.win.x;
						f_58_ -= THIS().renders.win.y;
						float f_59_ = (float) (cameraMap2D.worldXOffset + (double) f / cameraMap2D.worldScale);
						float f_60_ = (float) (cameraMap2D.worldYOffset + ((double) (THIS().renders.win.dy - f_58_ - 1.0F) / cameraMap2D.worldScale));
						access$812(GUIPad.this, curScaleDirect);
						if (curScaleDirect < 0)
						{
							if (curScale < 0)
							{
								curScale = 1;
								curScaleDirect = 1;
							}
						}
						else if (curScale >= scales)
						{
							curScale = scales - 2;
							curScaleDirect = -1;
						}
						GUIPad.this.scaleCamera();
						f_59_ -= ((double) (f - THIS().renders.win.dx / 2.0F) / cameraMap2D.worldScale);
						f_60_ += ((double) (f_58_ - THIS().renders.win.dy / 2.0F) / cameraMap2D.worldScale);
						GUIPad.this.setPosCamera(f_59_, f_60_);
					}
				}
			}

			// By PAL, to allow operation with Mouse Wheel
			public void mouseButton(int i, boolean flag, float f, float f1)
			{
				if (i == 0)
				{
					bLPressed = flag;
				}
				else if (i == 1)
				{
					bRPressed = flag;
					if (!bMPressed && !bLPressed && !zutiZoomingOldMode)
						doPopUpMenu();
					else
						mouseButton_oldWay(i, flag, f, f1);
				}
				else if (i == 2)
				{
					bMPressed = flag;
				}
				if (bLPressed && bRPressed || bMPressed)
				{
					mouseCursor = 7;
				}

				// TODO: Added by |ZUTI|: if both buttons are pressed and we have OLD way of zooming, enable drop down
				// -----------------------------------------------
				if (bLPressed && bRPressed)
				{
					zutiZoomingOldMode = false;
				}
				// -----------------------------------------------

				if (GUIPadMode == 1 || GUIPadMode == 4 || GUIPadMode == 5)
					if (bLPressed && !bRPressed && !bMPressed)
						mouseCursor = 3; // Only left mouse
				if (!bLPressed && !bMPressed && !bRPressed)
					mouseCursor = 1;
			}

			public void mouseRelMove(float f, float f1, float v) // By PAL
			{
				// bRPressed = flag;
				// if(bRPressed && !bLPressed)
				if (GUIPadMode == 6)
					return;
				if (bRPressed || bLPressed || bMPressed)
					return;
				GPoint MPos = getMouseXY();
				f = MPos.x;
				f1 = MPos.y;
				if (v != 0.0F)
				{
					f -= THIS().renders.win.x;
					f1 -= THIS().renders.win.y;
					// if (f < 0) f = (THIS().renders).win.x;
					// if (f1 < 0) f1 = (THIS().renders).win.y;
					float f2 = (float) (cameraMap2D.worldXOffset + (double) f / cameraMap2D.worldScale);
					float f3 = (float) (cameraMap2D.worldYOffset + (double) (THIS().renders.win.dy - f1 - 1.0F) / cameraMap2D.worldScale);

					// TODO: Added by |ZUTI|: reverting mouse wheel axis
					if (zutiMouseWheelDirection > 0)
					{
						if (v > 0.0F)
						{
							if (curScale < scales - 1)
								curScale++;
						}
						if (v < 0.0F)
						{
							if (curScale > 0)
								curScale--;
						}
					}
					else
					{
						if (v < 0.0F)
						{
							if (curScale < scales - 1)
								curScale++;
						}
						if (v > 0.0F)
						{
							if (curScale > 0)
								curScale--;
						}
					}

					scaleCamera();
					f2 = (float) ((double) f2 - (double) (f - THIS().renders.win.dx / 2.0F) / cameraMap2D.worldScale);
					f3 = (float) ((double) f3 + (double) (f1 - THIS().renders.win.dy / 2.0F) / cameraMap2D.worldScale);
					setPosCamera(f2, f3);
				}
			}

			public void mouseMove(float f, float f1)
			{
				if (bLPressed && bRPressed || bMPressed)
					frame.setPos(frame.win.x + root.mouseStep.dx, frame.win.y + root.mouseStep.dy);
				else if (bLPressed)
					if (GUIPadMode == 1 || GUIPadMode == 3)
					{
						cameraMap2D.worldXOffset -= (double) root.mouseStep.dx / cameraMap2D.worldScale;
						cameraMap2D.worldYOffset += (double) root.mouseStep.dy / cameraMap2D.worldScale;
						clipCamera();
					}
			}

			// By PAL
			boolean	bLPressed;
			boolean	bRPressed;
			boolean	bMPressed;
			{
				bLPressed = false;
				bRPressed = false;
				bMPressed = false;
			}
		};
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
		smallFont = TTFont.font[0]; // By PAL, small font for different purpuouses
		mesh = GMesh.New("gui/game/pad/mono.sim");
		meshradar = GMesh.New("gui/game/radar/mono.sim");
		meshradarF = GMesh.New("gui/game/radarF/mono.sim");
		_iconAir = Mat.New("icons/plane.mat");
		// meshbrief = GMesh.New("gui/game/brief/mono.sim");
		_iconRadar = Mat.New("icons/radarimage.mat"); // By PAL, icon of the radar
		_iconShipRadar = Mat.New("icons/radarshipimage.mat"); // By PAL, icon of the radar
		_iconAirField = Mat.New("icons/airfield.mat"); // By PAL, icon of the radar
		emptyMat = Mat.New("icons/empty.mat");

		if (com.maddox.il2.ai.World.cur().smallMapWPLabels)
		{
			waypointFont = com.maddox.il2.engine.TTFont.font[0];
			bigFontMultip = 1.0F;
		}
		else
		{
			waypointFont = com.maddox.il2.engine.TTFont.font[1];
			bigFontMultip = 2.0F;
		}
		_iconILS = com.maddox.il2.engine.Mat.New("icons/ILS.mat");

		main = Main3D.cur3D();
		client.hideWindow();
	}

	static int access$812(GUIPad guipad, int i)
	{
		return guipad.curScale += i;
	}

	// TODO: Methods and variables by |ZUTI|
	// ---------------------------------------------------------------------
	/**
	 * Key=integer(ZutiPadObject hash code), value = ZutiPadObject
	 */
	public Map			zutiPadObjects			= new HashMap();
	public boolean		zutiPlayeAcDrawn		= false;
	public ArrayList	zutiNeutralHomeBases	= null;
	public Mat			iconBornPlace			= null;
	private boolean		zutiZoomingOldMode		= false;
	private int			zutiMouseWheelDirection	= 1;

	private boolean		zutiColorAirfields		= true;

	/**
	 * Reset class variables
	 */
	public static void resetClassVariables()
	{
		if (GUI.pad != null)
		{
			GUI.pad.zutiPadObjects.clear();
			GUI.pad.zutiColorAirfields = true;
			GUI.pad.zutiPlayeAcDrawn = false;
			if (GUI.pad.zutiNeutralHomeBases != null)
				GUI.pad.zutiNeutralHomeBases.clear();
			GUI.pad.iconBornPlace = null;
		}
	}

	/**
	 * Get a list containing all airports on the loaded map.
	 * 
	 * @return
	 */
	public List zutiGetAirdromes()
	{
		return airdrome;
	}

	private void pablo_drawAirports(Point3d position, int color, float azimut, Mat mat)
	{
		if (GUIPadMode == 1)
		{
			float f = (float) ((position.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
			float f1 = (float) ((position.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
			IconDraw.setColor(color);
			IconDraw.render(mat, f, f1);
		}
		else if (GUIPadMode == 2)
		{
			float NewY = (float) ((((Tuple3d) (position)).x - ((Tuple3d) (OwnPos3d)).x) * Math.cos(OwnAzimut)) - (float) ((-((Tuple3d) (position)).y + ((Tuple3d) (OwnPos3d)).y) * Math.sin(OwnAzimut));
			float NewX = (float) ((-((Tuple3d) (position)).y + ((Tuple3d) (OwnPos3d)).y) * Math.cos(OwnAzimut)) + (float) ((((Tuple3d) (position)).x - ((Tuple3d) (OwnPos3d)).x) * Math.sin(OwnAzimut));
			float f = FrameOriginX + (float) (NewX * cameraMap2D.worldScale);
			float f1 = FrameOriginY + (float) (NewY * cameraMap2D.worldScale);
			IconDraw.setColor(color);
			IconDraw.render(mat, f, f1);
		}
		else if (GUIPadMode == 3)
		{
			float f = (float) ((position.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
			float f1 = (float) ((position.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
			IconDraw.setColor(color);
			// IconDraw.render(mat, f, f1); //By PAL, to draw them twisted
			IconDraw.render(_iconAirField, f, f1, azimut);
		}
	}

	private void zutiRenderPad_MDSWay()
	{
		// Added by |ZUTI|
		// -----------------------------
		if (GUIPadMode == 4 || GUIPadMode == 5 || GUIPadMode == 6 || !World.cur().diffCur.No_Map_Icons)
		{
			// If user selected any of the above pad modes, forward things to original Pablo methods.
			// Also, execute this if user allows rendering of map icons.
			zutiRenderPad_PabloWay();

			drawRadioBeacons();
			return;
		}
		// -----------------------------

		try
		{
			// Modified by |ZUTI|
			// int i = (int) Math.round(32.0 * (double) client.root.win.dx / 1024.0); //Original
			// int i = (int) Math.round(ZutiMDSVariables.ZUTI_ICON_SIZE * (double) client.root.win.dx / 1024.0); //MDS way
			// int iconsSizeMultiplier = (int) Math.round(((10 - curScale) * 3D * (double) client.root.win.dx) / 1024D); //By PAL 32D
			int iconsSizeMultiplier = (int) Math.round(((ZutiMDSVariables.ZUTI_ICON_SIZE - curScale) * (double) client.root.win.dx) / 1024D); // Hybrid - MDS and PAL - icon size depends olso on current scale
			Aircraft playerAc = World.getPlayerAircraft();
			// Do pad mode related stuff before you continue to rendering
			switch (GUIPadMode)
			{
				case 2:
				{
					drawGrid2DFixed();
					if (Actor.isValid(playerAc))
					{
						OwnPos3d = playerAc.pos.getAbsPoint();
						OwnAzimut = Math.toRadians(360.0F - playerAc.pos.getAbsOrient().azimut());
						OwnAngle = playerAc.pos.getAbsOrient().azimut();
					}
					else
					{
						// If player AC is not valid just return as all data
						// for this mode relies on that!
						return;
					}

					break;
				}
				default:
				{
					// Mode 2 must not show ground/map texture in the background
					if (main.land2D != null)
						main.land2D.render(0.9F, 0.9F, 0.9F, 1.0F);

					if (main.land2DText != null)
						main.land2DText.render();

					GUIPad.this.drawGrid2D();
					break;
				}
			}

			// Update radar objects and their visibility
			ZutiRadarRefresh.update(lastScale < curScale);
			lastScale = curScale;
			zutiPlayeAcDrawn = false;
			// Draw Front lines
			Front.render(false);
			// Draw icons with predefined (mode dependent) size
			IconDraw.setScrSize(iconsSizeMultiplier, iconsSizeMultiplier);
			// Draw targets
			ZutiSupportMethods_GUI.drawTargets(GUIPad.this.renders, gridFont, emptyMat, cameraMap2D);
			// Draw airports
			GUIPad.this.drawAirports();
			// Draw born places
			ZutiSupportMethods.drawBornPlaces();

			if (!World.cur().diffCur.NoMinimapPath)
			{
				if (Actor.isValid(playerAc))
				{
					IconDraw.setColor(-16711681);
					GUIPad.this.drawPlayerPath();
				}
			}

			// Added by |ZUTI|: changed this with following logic:
			// If player side has any radar object alive, show these objects, else don't show them!
			// Draw only if player side has at least one radar alive! This is called here because in our new method we will also re-draw player icon with different color (color of his army)
			if (World.cur().diffCur.No_Map_Icons && Mission.MDS_VARIABLES().zutiRadar_PlayerSideHasRadars)
			{
				ZutiSupportMethods_GUI.drawPadObjects();
			}

			// Do this last, just in case we have not drawn player AC before
			if ((!World.cur().diffCur.NoMinimapPath || !World.cur().diffCur.No_Map_Icons) && !zutiPlayeAcDrawn)
			{
				if (Actor.isValid(playerAc))
				{
					Point3d point3d = playerAc.pos.getAbsPoint();
					float f = (float) ((point3d.x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
					float f_0_ = (float) ((point3d.y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
					IconDraw.setColor(-1);
					IconDraw.render(_iconAir, f, f_0_, playerAc.pos.getAbsOrient().azimut());
				}
			}

			drawRadioBeacons();

			SquareLabels.draw(cameraMap2D, Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.mapSizeX());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Default pad rendering defined by Pablo.
	 */
	public void zutiRenderPad_PabloWay()
	{
		if (GUIPadMode == 1) // By PAL, standard map, original routine
		{
			if (main.land2D != null)
				main.land2D.render(0.9F, 0.9F, 0.9F, 1.0F);
			if (main.land2DText != null)
				main.land2DText.render();
			drawGrid2D();
			Front.render(false);
			int i = (int) Math.round(((10 - curScale) * 3D * (double) client.root.win.dx) / 1024D); // By PAL 32D
			IconDraw.setScrSize(i, i);
			drawAirports();
			if (!World.cur().diffCur.No_Map_Icons)
			{
				drawChiefs();
				drawAAAandFillAir();
				drawAir();
			}
			GUIBriefing.drawTargets(renders, gridFont, emptyMat, cameraMap2D, targets);
			if (!World.cur().diffCur.NoMinimapPath)
			{
				Aircraft aircraft = World.getPlayerAircraft();
				if (Actor.isValid(aircraft))
				{
					IconDraw.setColor(0xff00ffff);
					drawPlayerPath();
				}
			}
			if (!World.cur().diffCur.NoMinimapPath || !World.cur().diffCur.No_Map_Icons)
			{
				Aircraft aircraft1 = World.getPlayerAircraft();
				if (Actor.isValid(aircraft1))
				{ // By PAL, draw absolute position for own plane
					Point3d point3d = ((Actor) (aircraft1)).pos.getAbsPoint();
					float f = (float) ((((Tuple3d) (point3d)).x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
					float f1 = (float) ((((Tuple3d) (point3d)).y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
					IconDraw.setColor(-1);
					IconDraw.render(_iconAir, f, f1, ((Actor) (aircraft1)).pos.getAbsOrient().azimut());
				}
			}
			SquareLabels.draw(cameraMap2D, Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.mapSizeX());
		}
		else if (GUIPadMode == 2) // By PAL, relative map
		{
			drawGrid2DFixed();
			// Front.renderRot(false, 0.0F);
			if (Actor.isValid(World.getPlayerAircraft()) && Actor.isAlive(World.getPlayerAircraft()))
			{
				OwnPos3d = World.getPlayerAircraft().pos.getAbsPoint();
				OwnAzimut = Math.toRadians(360.0F - World.getPlayerAircraft().pos.getAbsOrient().azimut());
				OwnAngle = World.getPlayerAircraft().pos.getAbsOrient().azimut();

				int i = (int) Math.round(((10 - curScale) * 3D * (double) client.root.win.dx) / 1024D); // By PAL 32D
				IconDraw.setScrSize(i, i);
				drawAirports();
				if (!World.cur().diffCur.No_Map_Icons)
				{
					drawChiefs();
					drawAAAandFillAir();
					drawAir();
				}
				// GUIBriefing.drawTargets(renders, gridFont, emptyMat, cameraMap2D, targets);
				if (!World.cur().diffCur.NoMinimapPath || !World.cur().diffCur.No_Map_Icons)
				{
					Aircraft aircraft1 = World.getPlayerAircraft();
					if (Actor.isValid(aircraft1))
					{ // By PAL, draw fixed central position for own plane
						IconDraw.setColor(-1);
						IconDraw.render(_iconAir, FrameOriginX, FrameOriginY, -90.0F);
					}
				}
			}
			// SquareLabels.draw(cameraMap2D, Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.mapSizeX());
		}
		else if (GUIPadMode == 3)
		{ // By PAL, navigation map
			if (main.land2D != null)
				main.land2D.render(0.9F, 0.9F, 0.9F, 1.0F);
			if (main.land2DText != null)
				main.land2DText.render();
			drawGrid2D();
			Front.render(false);
			int i = (int) Math.round((32D * (double) client.root.win.dx) / 1024D); // By PAL 32D
			IconDraw.setScrSize(i, i);
			drawAirports();

			GUIBriefing.drawTargets(renders, gridFont, emptyMat, cameraMap2D, targets);
			if (!World.cur().diffCur.NoMinimapPath)
			{
				Aircraft aircraft = World.getPlayerAircraft();
				if (Actor.isValid(aircraft))
				{
					IconDraw.setColor(0xff00ffff);
					drawPlayerPath();
				}
			}
			if (!World.cur().diffCur.NoMinimapPath || !World.cur().diffCur.No_Map_Icons)
			{
				Aircraft aircraft1 = World.getPlayerAircraft();
				if (Actor.isValid(aircraft1))
				{ // By PAL, draw absolute position
					Point3d point3d = ((Actor) (aircraft1)).pos.getAbsPoint();
					float f = (float) ((((Tuple3d) (point3d)).x - cameraMap2D.worldXOffset) * cameraMap2D.worldScale);
					float f1 = (float) ((((Tuple3d) (point3d)).y - cameraMap2D.worldYOffset) * cameraMap2D.worldScale);
					IconDraw.setColor(-1);
					IconDraw.render(_iconAir, f, f1, ((Actor) (aircraft1)).pos.getAbsOrient().azimut());
				}
			}
			SquareLabels.draw(cameraMap2D, Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.mapSizeX());
		}
		else if (GUIPadMode == 4)
		{ // By PAL, radar views
			drawGrid2DFixed();
			Aircraft ownaircraft = World.getPlayerAircraft();
			if (!World.cur().diffCur.No_Map_Icons)
				if (Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft)) // Si el actor no era válido, no voy a dibujar nada
				{
					int ti = (int) ((Time.current() % 1000) / 500);
					if (ti != to)
					{
						to = ti;
						Point3d pointAC = ownaircraft.pos.getAbsPoint();
						Orient orientAC = ownaircraft.pos.getAbsOrient();
						double d = (double) (cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale; // By PAL, relative limits
						radarPlane.clear(); // By PAL, cleans all the items for the radar
						radarOther.clear();
						List list = Engine.targets();
						int i = list.size();
						for (int j = 0; j < i; j++)
						{
							Actor actor = (Actor) list.get(j);
							if (actor instanceof Aircraft)
							{
								if (actor != World.getPlayerAircraft())
								{
									Point3d pointOrtho = new Point3d();
									pointOrtho.set(actor.pos.getAbsPoint());

									pointOrtho.sub(pointAC);
									orientAC.transformInv(pointOrtho);

									double NewX = -((Tuple3d) (pointOrtho)).y;
									double NewY = ((Tuple3d) (pointOrtho)).x;

									if (NewX >= -d && NewX <= d && NewY >= -d && NewY <= d)
										radarPlane.add(pointOrtho); // airs.add(actor);
								}
							}
							else if (actor instanceof ShipGeneric || actor instanceof BigshipGeneric) // Add the ships
							{
								Point3d pointOrtho = new Point3d();
								pointOrtho.set(actor.pos.getAbsPoint());

								pointOrtho.sub(pointAC);
								orientAC.transformInv(pointOrtho);

								double NewX = -((Tuple3d) (pointOrtho)).y;
								double NewY = ((Tuple3d) (pointOrtho)).x;

								if (NewX >= -d && NewX <= d && NewY >= -d && NewY <= d)
									radarOther.add(pointOrtho); // airs.add(actor);
							}
						}
					}
					int i = radarPlane.size();
					for (int j = 0; j < i; j++)
					{
						double x = ((Tuple3d) ((Point3d) radarPlane.get(j))).x;
						double y = ((Tuple3d) ((Point3d) radarPlane.get(j))).y;
						double z = ((Tuple3d) ((Point3d) radarPlane.get(j))).z;

						double FOV = Math.sqrt(x * x + y * y + z * z) / Math.sqrt(x * x + y * y); // Math.abs(((Tuple3d)(pointOrtho)).z) / 5.0F;

						double NewX = -y * FOV;
						double NewY = x * FOV;

						float f = FrameOriginX + (float) (NewX * cameraMap2D.worldScale);
						float f1 = FrameOriginY + (float) (NewY * cameraMap2D.worldScale);

						int size = (int) Math.round(((16 - curScale) * 2D * (double) client.root.win.dx) / 1024D); // By PAL 32D
						IconDraw.setScrSize(size, size);

						IconDraw.setColor(0xff00ffff); // Green radar
						IconDraw.render(_iconRadar, f, f1, 0.0F);// actor.pos.getAbsOrient().kren() - orientAC.kren() - 360.0F);
					}
					i = radarOther.size();
					for (int j = 0; j < i; j++)
					{
						double x = ((Tuple3d) ((Point3d) radarOther.get(j))).x;
						double y = ((Tuple3d) ((Point3d) radarOther.get(j))).y;
						double z = ((Tuple3d) ((Point3d) radarOther.get(j))).z;

						double FOV = Math.sqrt(x * x + y * y + z * z) / Math.sqrt(x * x + y * y); // Math.abs(((Tuple3d)(pointOrtho)).z) / 5.0F;

						double NewX = -y * FOV;
						double NewY = x * FOV;

						float f = FrameOriginX + (float) (NewX * cameraMap2D.worldScale);
						float f1 = FrameOriginY + (float) (NewY * cameraMap2D.worldScale);

						int size = (int) Math.round(((16 - curScale) * 2D * (double) client.root.win.dx) / 1024D); // By PAL 32D
						IconDraw.setScrSize(size, size);

						IconDraw.setColor(0xff00ffff); // Green radar
						IconDraw.render(_iconShipRadar, f, f1, 0.0F);// actor.pos.getAbsOrient().kren() - orientAC.kren() - 360.0F);
					}
				}
		}
		else if (GUIPadMode == 5)
		{
			drawGrid2DFixed();
			Aircraft ownaircraft = World.getPlayerAircraft();
			if (!World.cur().diffCur.No_Map_Icons)
				if (Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft)) // Si el actor no era válido, no voy a dibujar nada
				{
					int ti = (int) ((Time.current() % 1000) / 500);
					if (ti != to)
					{
						to = ti;
						Point3d pointAC = ownaircraft.pos.getAbsPoint();
						Orient orientAC = ownaircraft.pos.getAbsOrient();
						double d = (double) (cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale; // By PAL, relative limits
						radarPlane.clear(); // By PAL, cleans all the items for the radar
						radarOther.clear();
						List list = Engine.targets();
						int i = list.size();
						for (int j = 0; j < i; j++)
						{
							Actor actor = (Actor) list.get(j);
							if (actor instanceof Aircraft)
							{
								if (actor != World.getPlayerAircraft())
								{
									Point3d pointOrtho = new Point3d();
									pointOrtho.set(actor.pos.getAbsPoint());

									pointOrtho.sub(pointAC);
									orientAC.transformInv(pointOrtho);

									double NewX = -((Tuple3d) (pointOrtho)).y;
									double NewY = ((Tuple3d) (pointOrtho)).z;

									if (NewX >= -d && NewX <= d && NewY >= -d && NewY <= d)
										radarPlane.add(pointOrtho); // airs.add(actor);
								}
							}
							else if (actor instanceof ShipGeneric || actor instanceof BigshipGeneric) // Add the ships
							{
								Point3d pointOrtho = new Point3d();
								pointOrtho.set(actor.pos.getAbsPoint());

								pointOrtho.sub(pointAC);
								orientAC.transformInv(pointOrtho);

								double NewX = -((Tuple3d) (pointOrtho)).y;
								double NewY = ((Tuple3d) (pointOrtho)).z;

								if (NewX >= -d && NewX <= d && NewY >= -d && NewY <= d)
									radarOther.add(pointOrtho); // airs.add(actor);
							}
						}
					}
					int i = radarPlane.size();
					for (int j = 0; j < i; j++)
					{
						double x = ((Tuple3d) ((Point3d) radarPlane.get(j))).x;
						if (x > 5.0F) // Mayor que 5m, en frente del radar
						{
							double FOV = 1; // Math.abs(((Tuple3d)((Point3d)radarPlane.get(j))).x) / 5.0F;

							double NewX = -((Tuple3d) ((Point3d) radarPlane.get(j))).y * FOV;
							double NewY = ((Tuple3d) ((Point3d) radarPlane.get(j))).z * FOV;

							float f = FrameOriginX + (float) (NewX * cameraMap2D.worldScale);
							float f1 = FrameOriginY + (float) (NewY * cameraMap2D.worldScale);

							int size = (int) Math.round(((16 - curScale) * 3D * (double) client.root.win.dx) / 1024D / (1 + x / 800)); // By PAL 32D
							IconDraw.setScrSize(size, size);

							IconDraw.setColor(0xff00ffff); // Green radar
							IconDraw.render(_iconRadar, f, f1, 0.0F);// actor.pos.getAbsOrient().kren() - orientAC.kren() - 360.0F);
						}
					}
					i = radarOther.size();
					for (int j = 0; j < i; j++)
					{
						double x = ((Tuple3d) ((Point3d) radarOther.get(j))).x;
						if (x > 5.0F) // Mayor que 5m, en frente del radar
						{
							double FOV = 1; // Math.abs(((Tuple3d)((Point3d)radarPlane.get(j))).x) / 5.0F;

							double NewX = -((Tuple3d) ((Point3d) radarOther.get(j))).y * FOV;
							double NewY = ((Tuple3d) ((Point3d) radarOther.get(j))).z * FOV;

							float f = FrameOriginX + (float) (NewX * cameraMap2D.worldScale);
							float f1 = FrameOriginY + (float) (NewY * cameraMap2D.worldScale);

							int size = (int) Math.round(((16 - curScale) * 3D * (double) client.root.win.dx) / 1024D / (1 + x / 800)); // By PAL 32D
							IconDraw.setScrSize(size, size);

							IconDraw.setColor(0xff00ffff); // Green radar
							IconDraw.render(_iconShipRadar, f, f1, 0.0F);// actor.pos.getAbsOrient().kren() - orientAC.kren() - 360.0F);
						}
					}
				}
		}
		if (GUIPadMode == 6)
		{ // By PAL, Write Briefing
			int enemyAirKill = 0;
			int enemyGroundKill = 0;
			int friendlyKill = 0;

			ScoreCounter scorecounter = World.cur().scoreCounter;
			ArrayList arraylist = scorecounter.enemyItems;
			for (int i = 0; i < arraylist.size(); i++)
			{
				ScoreItem scoreitem = (ScoreItem) arraylist.get(i);
				if (scoreitem.type == 0)
				{
					enemyAirKill++;
				}
				else
				{
					enemyGroundKill++;
				}
			}
			ArrayList arraylist1 = scorecounter.friendItems;
			for (int k = 0; k < arraylist1.size(); k++)
			{
				// ScoreItem scoreitem1 = (ScoreItem)arraylist1.get(k);
				friendlyKill++;
			}

			List targets = World.cur().targetsGuard.zutiGetTargets(); // By PAL from Zuti, GetTargets() implemented in TargetsGuard
			Target target = null;
			int TargetPrimary = 0;
			int TargetSecondary = 0;
			int TargetSecret = 0;
			for (int i = 0; i < targets.size(); i++)
			{
				target = (Target) targets.get(i);
				if (target.getDiedFlag() || target.isTaskComplete())
					continue;

				switch (target.importance())
				{
					case 0:
					{
						TargetPrimary++;
						break;
					}
					case 1:
					{
						TargetSecondary++;
						break;
					}
					case 2:
					{
						TargetSecret++;
						break;
					}
				}
			}

			/*
			 * arraylist = scorecounter.targetOnItems; for(int l = 0; l <
			 * arraylist.size(); l++) { ScoreItem scoreitem2 =
			 * (ScoreItem)arraylist.get(l); //Target target1 =
			 * (Target)arraylist.get(l); //if (scoreitem3.TARGET_PRIMARY > 0)
			 * TargetPrimary++;
			 * //if (scoreitem3.TARGET_SECONDARY > 0) TargetSecondary++; //if
			 * (scoreitem3.TARGET_SECRET > 0) TargetSecret++; //if
			 * (target1.importance() == 0) TargetPrimary++; //if
			 * (target1.importance() == 1) TargetSecondary++; //if
			 * (target1.importance() == 2) TargetSecret++;
			 * //World.cur().targetsGuard. if (scoreitem2.score == 100)
			 * TargetPrimary++; if (scoreitem2.score == 101) TargetSecondary++;
			 * if (scoreitem2.score == 102) TargetSecret++;
			 * World.cur().targetsGuard.isTaskComplete(); //Target.PRIMARY }
			 * arraylist = scorecounter.targetOffItems; for(int i1 = 0; i1 <
			 * arraylist.size(); i1++) { ScoreItem scoreitem3 =
			 * (ScoreItem)arraylist.get(i1); //Target target1 =
			 * (Target)arraylist.get(i1); //if (target1.importance() == 0)
			 * TargetPrimary = 1; //if (target1.importance() == 1)
			 * TargetSecondary = 1; //if (target1.importance() == 2)
			 * TargetSecret = 1; //World.cur().targetsGuard. } /* if(bShowTime
			 * ||
			 * isDemoPlaying()) { int i = TextScr.This().getViewPortWidth();
			 * long l = Time.current(); if(NetMissionTrack.isPlaying()) l -=
			 * NetMissionTrack.playingStartTime; int j = (int)((l / 1000L) %
			 * 60L); int k = (int)(l / 1000L / 60L); if(j > 9)
			 * TextScr.output(i - TextScr.font().height() * 3, 5, "" + k + ":" +
			 * j); else TextScr.output(i - TextScr.font().height() * 3, 5, "" +
			 * k + ":0" + j); } public static final int PRIMARY = 0; public
			 * static final int SECONDARY = 1; public
			 * static final int SECRET = 2; public static final int DESTROY = 0;
			 * public static final int DESTROY_GROUND = 1; public static final
			 * int DESTROY_BRIDGE = 2; public static final int INSPECT = 3;
			 * public static final int ESCORT = 4; public
			 * static final int DEFENCE = 5; public static final int
			 * DEFENCE_GROUND = 6; public static final int DEFENCE_BRIDGE = 7;/*
			 * //if (target.PRIMARY > 0) //World.cur().scoreCounter.t /* int i =
			 * targets.size(); for(int j = 0; j < i; j++) {
			 * Target target = (Target)targets.get(j); if target.
			 * switch(checkType) { default: break; case 0: // '\0'
			 * if(target.importance() != 0 || target.isTaskComplete()) break;
			 * if(!target.isAlive()) flag1 = true; flag = false; break; case 1:
			 * //
			 * '\001' if(target.importance() != 1 || target.isTaskComplete())
			 * break; if(!target.isAlive()) flag1 = true; flag = false; break;
			 * case 2: // '\002' if(target.importance() != 2) break;
			 * if(target.isAlive()) { flag2 = false; break; }
			 * if(target.isTaskComplete()) flag = true; else flag1 = true;
			 * break; }
			 */

			String s = textDescription();
			if (textDescription == null)
				s = "\n           No brief was found for this mission";
			else
				s = "\n" + s;

			{
				// renders1.setCanvasFont(0);
				// renders1.setCanvasColor(0xc0dd1010);

				long l = Time.current();
				// if(NetMissionTrack.isPlaying())
				// l -= NetMissionTrack.playingStartTime;
				int se = (int) ((l / 1000L) % 60L);
				int mi = (int) (l / 1000L / 60L);
				// long timeStart = Time.currentReal();
				float todStart = scorecounter.todStart; // World.getTimeofDay();
				int h = (int) (todStart);
				int m = (int) ((todStart - h) * 60);

				int sp = (int) (smallFont.height() * 1.5) + smallFont.descender();
				int li = 5;
				int x = renderMap2D.getViewPortWidth();

				int y = renderMap2D.getViewPortHeight();
				int Border = (int) (x * 0.08);
				int col = 0xff000000;

				smallFont.output(col, Border, y - Border - sp * li++, 0.0F, "Elapsed time: " + (mi <= 9 ? "0" + mi + "m " : mi + "m ") + (se <= 9 ? "0" + se + "s" : se + "s"));
				smallFont.output(col, Border, y - Border - sp * li++, 0.0F, "Mission started at: " + (h <= 9 ? "0" + h + ":" : h + ":") + (m <= 9 ? "0" + m : "" + m) + " hours");
				smallFont.output(col, Border, y - Border - sp * li++, 0.0F, "Enemy air kills: " + (enemyAirKill == 0 ? "none" : "" + enemyAirKill));
				smallFont.output(col, Border, y - Border - sp * li++, 0.0F, "Enemy ground kills: " + (enemyGroundKill == 0 ? "none" : "" + enemyGroundKill));
				smallFont.output(col, Border, y - Border - sp * li++, 0.0F, "Friendly kills: " + (friendlyKill == 0 ? "none" : "" + friendlyKill));
				smallFont.output(col, Border, y - Border - sp * li++, 0.0F, "Primary Targets pending: " + (TargetPrimary == 0 ? "none" : "" + TargetPrimary));
				smallFont.output(col, Border, y - Border - sp * li++, 0.0F, "Secondary Targets pending:  " + (TargetSecondary == 0 ? "none" : "" + TargetSecondary));
				smallFont.output(col, Border, y - Border - sp * li++, 0.0F, "Secret Targets pending: " + (TargetSecret == 0 ? "none" : "" + TargetSecret));
				smallFont.output(col, Border, y - Border - sp * li++, 0.0F, "Original mission brief:");
				writeLines(col, Border, y - Border - sp * li++, s, 0, s.length(), x, y, Border);
			}
		}
	}

	protected String textDescription()
	{
		return textDescription;

	}

	public int writeLines(int c, int x, int y, String s, int i, int j, float f1, float f2, float f3)// ;, float f3, int k)
	{
		int sp = (int) (smallFont.height() * 1.5) + smallFont.descender();
		int l = 0; // Number of lines used
		int k = 20; // Maximum of lines wanted gridFont.height();
		while (j > 0 && k != 0)
		{
			int i1 = j;
			int j1 = s.indexOf('\n', i);
			if (j1 >= 0)
				i1 = j1 - i;
			if (i1 > 0)
			{
				while (i1 > 0 && k != 0)

				{
					int k1 = smallFont.len(s, i, i1, f1 - 2 * f3, true);
					if (k1 == 0)
						k1 = smallFont.len(s, i, i1, f1 - 2 * f3, false);
					if (k1 == 0)
						return l;
					// gcanvas.draw(s, i, k1);
					// if (k1 <= s.length() && k1 >= i)
					// gridFont.output(c, x, y - sp * l, 0.0F, s.substring(i, k1));
					smallFont.outputClip(c, x, y - sp * l, 0.0F, s, i, k1, f3, f3, f1 - 2 * f3, f2 - f3);
					// gcanvas.cur.y += f3;
					l++;
					j -= k1;
					i += k1;
					i1 -= k1;
					// k--;
					for (; i1 > 0; i1--)

					{
						if (s.charAt(i) != ' ')
							break;
						i++;
						j--;
					}

				}
				if (j1 >= 0)
				{
					i++;
					j--;

				}
			}
			else

			{
				// gcanvas.cur.y += f3;
				l++;
				j--;
				i++;
				// k--;
			}

		}
		return l;
	}

	public String Target(int tNum)
	{
		String Descript = "";
		switch (tNum)
		{
			case 0: // '\0'
				Descript = "Destroy";
			case 1: // '\001'
				Descript = "Destroy Ground";
			case 2: // '\002'
				Descript = "Destroy Bridge";
			case 3: // '\003'
				Descript = "Inspect";
			case 4: // '\004'
				Descript = "Escort"; // Originally None
			case 5: // '\005'
				Descript = "Defence"; // Originally Escort
			case 6: // '\006'
				Descript = "Defence Ground";
			case 7: // '\007'
				Descript = "Defence Bridge";
		}
		return Descript;
	}
	// ---------------------------------------------------------------------
}