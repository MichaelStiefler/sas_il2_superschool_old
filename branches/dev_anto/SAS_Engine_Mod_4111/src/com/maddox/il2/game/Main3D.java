/*Modified Main3D class for the SAS Engine Mod*/

package com.maddox.il2.game;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maddox.JGP.Color4f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorFilter;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorLandMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.BulletGeneric;
import com.maddox.il2.engine.Camera;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.CameraOrtho2D;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.ConsoleGL0;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.GUIWindowManager;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookRender;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Land2D;
import com.maddox.il2.engine.Land2DText;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightEnvXY;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.MeshShared;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.RenderContext;
import com.maddox.il2.engine.Renders;
import com.maddox.il2.engine.RendersMain;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.engine.TTFontTransform;
import com.maddox.il2.engine.TextScr;
import com.maddox.il2.engine.hotkey.FreeFly;
import com.maddox.il2.engine.hotkey.FreeFlyXYZ;
import com.maddox.il2.engine.hotkey.HookGunner;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.engine.hotkey.HookView;
import com.maddox.il2.engine.hotkey.HookViewEnemy;
import com.maddox.il2.engine.hotkey.HookViewFly;
import com.maddox.il2.fm.FMMath;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.order.OrdersTree;
import com.maddox.il2.gui.GUI;
import com.maddox.il2.gui.GUIBWDemoPlay;
import com.maddox.il2.gui.GUIBuilder;
import com.maddox.il2.gui.GUIRecordPlay;
import com.maddox.il2.gui.GUITrainingPlay;
import com.maddox.il2.net.Connect;
import com.maddox.il2.net.NetLocalControl;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetServerParams;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.ActorViewPoint;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Cockpit;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.il2.objects.effects.Cinema;
import com.maddox.il2.objects.effects.ForceFeedback;
import com.maddox.il2.objects.effects.LightsGlare;
import com.maddox.il2.objects.effects.OverLoad;
import com.maddox.il2.objects.effects.SpritesFog;
import com.maddox.il2.objects.effects.SunFlare;
import com.maddox.il2.objects.effects.SunGlare;
import com.maddox.il2.objects.effects.Zip;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.TransparentTestRunway;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.lights.SearchlightGeneric;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.MissileInterceptable;
import com.maddox.il2.objects.weapons.Rocket;
import com.maddox.opengl.GLContext;
import com.maddox.opengl.gl;
import com.maddox.opengl.util.ScrShot;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.Finger;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.HotKeyCmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.InOutStreams;
import com.maddox.rts.KeyRecord;
import com.maddox.rts.KeyRecordCallback;
import com.maddox.rts.Keyboard;
import com.maddox.rts.Message;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;
import com.maddox.rts.cmd.CmdSFS;
import com.maddox.sound.AudioDevice;

public class Main3D extends Main
{
    public static float FOVX = 70.0F;
    public static final float ZNEAR = 1.2F;
    public static final float ZFAR = 48000.0F;
    protected boolean bDrawIfNotFocused = false;
    protected boolean bUseStartLog = false;
    protected boolean bUseGUI = true;
    private boolean bShowStartIntro = false;
    public GUIWindowManager guiManager;
    public KeyRecord keyRecord;
    public OrdersTree ordersTree;
    public TimeSkip timeSkip;
    public HookView hookView;
    public HookViewFly hookViewFly;
    public HookViewEnemy hookViewEnemy;
    public AircraftHotKeys aircraftHotKeys;
    public Cockpit[] cockpits;
    public Cockpit cockpitCur;
    public OverLoad overLoad;
    public OverLoad[] _overLoad = new OverLoad[3];
    public SunGlare sunGlare;
    public SunGlare[] _sunGlare = new SunGlare[3];
    public LightsGlare lightsGlare;
    public LightsGlare[] _lightsGlare = new LightsGlare[3];
    private SunFlare[] _sunFlare = new SunFlare[3];
    public Render[] _sunFlareRender = new Render[3];
    private boolean bViewFly = false;
    private boolean bViewEnemy = false;
    public boolean bEnableFog = true;
    private boolean bDrawLand = false;
    public Zip zip;
    public boolean bDrawClouds = false;
    public SpritesFog spritesFog;
    public Cinema[] _cinema = new Cinema[3];
    public Render3D0R render3D0R;
    public Camera3DR camera3DR;
    public Render3D0 render3D0;
    public Render3D1 render3D1;
    public Camera3D camera3D;
    public Render3D0[] _render3D0 = new Render3D0[3];
    public Render3D1[] _render3D1 = new Render3D1[3];
    public Camera3D[] _camera3D = new Camera3D[3];
    public Render2D render2D;
    public CameraOrtho2D camera2D;
    public Render2D[] _render2D = new Render2D[3];
    public CameraOrtho2D[] _camera2D = new CameraOrtho2D[3];
    public Render3D0Mirror render3D0Mirror;
    public Render3D1Mirror render3D1Mirror;
    public Camera3D camera3DMirror;
    public Render2DMirror render2DMirror;
    public CameraOrtho2D camera2DMirror;
    public RenderCockpit renderCockpit;
    public Camera3D cameraCockpit;
    public RenderCockpit[] _renderCockpit = new RenderCockpit[3];
    public Camera3D[] _cameraCockpit = new Camera3D[3];
    public RenderCockpitMirror renderCockpitMirror;
    public Camera3D cameraCockpitMirror;
    public RenderHUD renderHUD;
    public CameraOrtho2D cameraHUD;
    public HUD hud;
    public RenderMap2D renderMap2D;
    public CameraOrtho2D cameraMap2D;
    public Land2D land2D;
    public Land2DText land2DText;
    private static String _sLastMusic = "ru";
    protected int viewMirror = 2;
    private int iconTypes = 3;
    public static final String[] gameHotKeyCmdEnvs
	= { "Console", "hotkeys", "HookView", "PanView", "HeadMove",
	    "SnapView", "pilot", "move", "gunner", "misc", "orders",
	    "aircraftView", "timeCompression", "gui" };
    public static final String[] builderHotKeyCmdEnvs
	= { "Console", "builder", "hotkeys" };
    KeyRecordCallback playRecordedMissionCallback;
    String playRecordedFile;
    int playBatchCurRecord;
    boolean playEndBatch;
    SectFile playRecordedSect;
    int playRecorderIndx;
    String playRecordedPlayFile;
    InOutStreams playRecordedStreams;
    NetChannelInStream playRecordedNetChannelIn;
    GameTrack gameTrackRecord;
    GameTrack gameTrackPlay;
    private boolean bLoadRecordedStates1Before = false;
    private boolean bRenderMirror = false;
    private int iRenderIndx = 0;
    protected double[][] _modelMatrix3D = new double[3][16];
    protected double[][] _projMatrix3D = new double[3][16];
    protected int[][] _viewport = new int[3][4];
    protected double[] _modelMatrix3DMirror = new double[16];
    protected double[] _projMatrix3DMirror = new double[16];
    protected int[] _viewportMirror = new int[4];
    private double[] _dIn = new double[4];
    private double[] _dOut = new double[4];
    private static double shadowPairsR = 1000.0;
    private static double shadowPairsR2 = shadowPairsR * shadowPairsR;
    private ArrayList shadowPairsList1 = new ArrayList();
    private HashMap shadowPairsMap1 = new HashMap();
    private BigshipGeneric shadowPairsCur1 = null;
    private ArrayList shadowPairsList2 = new ArrayList();
    private ActorFilter shadowPairsFilter = new ShadowPairsFilter();
    private DrwArray[] drwMaster
	= { new DrwArray(), new DrwArray(), new DrwArray() };
    private DrwArray drwMirror = new DrwArray();
    private Loc __l = new Loc();
    private Point3d __p = new Point3d();
    private Orient __o = new Orient();
    private Vector3d __v = new Vector3d();
    private boolean bShowTime = false;
    public static final String ICONFAR_PROPERTY = "iconFar_shortClassName";
    public static final float iconFarActorSizeX = 2.0F;
    public static final float iconFarActorSizeY = 1.0F;
    public static final float iconFarSmallActorSize = 1.0F;
    public static int iconFarColor = 8355711;
    protected double iconGroundDrawMin = 200.0;
    protected double iconSmallDrawMin = 100.0;
    protected double iconAirDrawMin = 1000.0;
    protected double iconDrawMax = 10000.0;
    private Mat iconFarMat;
    private TTFont iconFarFont;
    private int iconFarFinger;
    private float iconFarFontHeight;
    private double iconClipX0;
    private double iconClipX1;
    private double iconClipY0;
    private double iconClipY1;
    private Actor iconFarPlayerActor;
    private Actor iconFarViewActor;
    private Actor iconFarPadlockActor;
    private FarActorItem iconFarPadlockItem
	= new FarActorItem(0, 0, 0, 0, 0.0F, null);
    private ArrayList[] iconFarList
	= { new ArrayList(), new ArrayList(), new ArrayList() };
    private int[] iconFarListLen = { 0, 0, 0 };
    private FarActorFilter farActorFilter = new FarActorFilter();
    private float[] line3XYZ = new float[9];
    private Point3d _lineP = new Point3d();
    private Orient _lineO = new Orient();
    private TransformMirror transformMirror = new TransformMirror();
    private long lastTimeScreenShot = 0L;
    private ScrShot scrShot = null;
    private ScreenSequence screenSequence;
    
    class CmdExit extends Cmd
    {
	public Object exec(CmdEnv cmdenv, Map map) {
	    Main.doGameExit();
	    return null;
	}
    }
    
    class CmdScreenSequence extends Cmd
    {
	public Object exec(CmdEnv cmdenv, Map map) {
	    if (screenSequence == null)
		screenSequence = new ScreenSequence();
	    screenSequence.doSave();
	    return null;
	}
    }
    
    class ScreenSequence extends Actor
    {
	boolean bSave = false;
	ScrShot shot = new ScrShot("s");
	
	class Interpolater extends Interpolate
	{
	    public boolean tick() {
		if (bSave)
		    shot.grab();
		return true;
	    }
	}
	
	public void doSave() {
	    bSave = !bSave;
	}
	
	public Object getSwitchListener(Message message) {
	    return this;
	}
	
	public ScreenSequence() {
	    flags |= 0x4000;
	    interpPut(new Interpolater(), "grabber", Time.current(), null);
	}
	
	protected void createActorHashCode() {
	    makeActorRealHashCode();
	}
    }
    
    private static class TransformMirror extends TTFontTransform
    {
	private float x0;
	private float y0;
	private float dx;
	private float z0;
	
	private TransformMirror() {
	    /* empty */
	}
	
	public void get(float f, float f_0_, float[] fs) {
	    fs[0] = x0 + dx - f;
	    fs[1] = y0 + f_0_;
	    fs[2] = z0;
	}
	
	public void set(float f, float f_1_, float f_2_, float f_3_) {
	    x0 = f;
	    y0 = f_1_;
	    z0 = f_2_;
	    dx = f_3_;
	}
    }
    
    class FarActorFilter implements ActorFilter
    {
	Point3d p3d = new Point3d();
	Point3d p2d = new Point3d();
	Point3d camp = new Point3d();
	
	private String lenToString(int i) {
	    String string;
	    if (i >= 1000)
		string = i / 1000 + ".";
	    else
		string = ".";
	    i %= 1000;
	    if (i < 10)
		return string + "00";
	    if (i < 100)
		return string + "0" + i / 10;
	    return string + i / 10;
	}
	
	private void drawPointer(int i, double d, int i_4_, int i_5_) {
	    double d_6_ = Math.atan2((double) i_5_, (double) i_4_);
	    double d_7_
		= Math.atan2(p2d.y - (double) i_5_, p2d.x - (double) i_4_);
	    if (p2d.z > 1.0) {
		if (d_7_ > 0.0)
		    d_7_ = -3.141592653589793 + d_7_;
		else
		    d_7_ = 3.141592653589793 + d_7_;
	    }
	    double d_8_;
	    double d_9_;
	    if (d_7_ >= 0.0) {
		if (d_7_ <= d_6_) {
		    d_8_ = (double) i_4_;
		    d_9_ = Math.tan(d_7_) * (double) i_4_;
		} else if (d_7_ <= 3.141592653589793 - d_6_) {
		    d_9_ = (double) i_5_;
		    d_8_ = Math.tan(1.5707963267948966 - d_7_) * (double) i_5_;
		} else {
		    d_8_ = (double) -i_4_;
		    d_9_ = -Math.tan(d_7_) * (double) i_4_;
		}
	    } else if (d_7_ >= -d_6_) {
		d_8_ = (double) i_4_;
		d_9_ = Math.tan(d_7_) * (double) i_4_;
	    } else if (d_7_ >= -3.141592653589793 + d_6_) {
		d_9_ = (double) -i_5_;
		d_8_ = -Math.tan(1.5707963267948966 - d_7_) * (double) i_5_;
	    } else {
		d_8_ = (double) -i_4_;
		d_9_ = -Math.tan(d_7_) * (double) i_4_;
	    }
	    d_8_ += (double) i_4_;
	    d_9_ += (double) i_5_;
	    HUD.addPointer((float) d_8_, (float) d_9_, Army.color(i),
			   (float) ((1.0 - d / iconDrawMax) * 0.8 + 0.2),
			   (float) d_7_);
	}
	
	public boolean isUse(Actor actor, double d) {
	    if (!actor.isDrawing())
		return false;
	    if (actor == iconFarViewActor)
		return false;
	    if (d <= 5.0)
		return false;
	    int i = actor.getArmy();
	    if (i == 0 && !Main3D.this.isBomb(actor))
		return false;
	    DotRange dotrange
		= i == World.getPlayerArmy() ? dotRangeFriendly : dotRangeFoe;
	    double d_10_ = 1.0;
	    double d_11_ = 0.078 + (double) (1.2F / Main3D.FOVX);
	    if (Main3D.FOVX < 24.0F)
		d_11_ = 0.16;
	    if (actor instanceof ActorMesh
		&& ((ActorMesh) actor).mesh() != null) {
		float f = ((ActorMesh) actor).mesh().visibilityR();
		if (f > 0.0F) {
		    if (f > 100.0F) {
			float f_12_ = ((ActorMesh) actor).collisionR();
			if (f_12_ > 0.0F)
			    d_10_ = (double) f_12_ * d_11_;
		    } else
			d_10_ = (double) f * d_11_;
		}
	    }
        if((actor instanceof Aircraft) || ((actor instanceof MissileInterceptable) && (((MissileInterceptable)actor).isReleased()))) {
		if (d_10_ < 0.65)
		    d_10_ = 0.65;
		if (d_10_ > 2.2)
		    d_10_ = 2.2;
	    } else
		d_10_ *= 1.2;
	    iconDrawMax = dotrange.dot(d_10_);
	    if (d > iconDrawMax)
		return false;
	    actor.pos.getRender(p3d);
	    if (!project2d_cam(p3d, p2d))
		return false;
	    if (p2d.z > 1.0 || p2d.x < iconClipX0 || p2d.x > iconClipX1
		|| p2d.y < iconClipY0 || p2d.y > iconClipY1) {
		if (bRenderMirror)
		    return false;
        if((!(actor instanceof Aircraft)) && (!((actor instanceof MissileInterceptable) && (((MissileInterceptable)actor).isReleased()))))
		    return false;
		if (isViewInsideShow())
		    return false;
		if (World.cur().diffCur.No_Icons)
		    return false;
		if (iRenderIndx == 0)
		    drawPointer(i, d, render2D.getViewPortWidth() / 2,
				render2D.getViewPortHeight() / 2);
		return false;
	    }
	    int i_13_ = (int) (p2d.x - 1.0);
	    int i_14_ = (int) (p2d.y - 0.5);
	    int i_15_ = 8355711;
	    int i_16_ = 255;
	    int i_17_ = 0;
	    if (bEnableFog) {
		Render.enableFog(true);
		i_15_ = Landscape.getFogRGBA((float) p3d.x, (float) p3d.y,
					     (float) p3d.z);
		i_17_ = i_15_ >>> 24;
		i_16_ -= i_17_;
		Render.enableFog(false);
	    }
	    int i_18_
		= (((int) (dotrange.alphaDot(d * 2.2, d_10_) * 255.0) & 0xff)
		   << 24);
	    int i_19_
		= ((int) (dotrange.alphaDot(d, d_10_) * 255.0) & 0xff) << 24;
	    int i_20_ = (i_18_
			 | ((Main3D.iconFarColor & 0xff) * i_16_
			    + (i_15_ & 0xff) * i_17_) >>> 8
			 | ((Main3D.iconFarColor >>> 8 & 0xff) * i_16_
			    + (i_15_ >>> 8 & 0xff) * i_17_) >>> 8 << 8
			 | ((Main3D.iconFarColor >>> 16 & 0xff) * i_16_
			    + (i_15_ >>> 16 & 0xff) * i_17_) >>> 8 << 16);
	    int i_21_ = (i_19_
			 | ((Main3D.iconFarColor >>> 1 & 0x3f) * i_16_
			    + (i_15_ >>> 0 & 0xff) * i_17_) >>> 8
			 | ((Main3D.iconFarColor >>> 9 & 0x3f) * i_16_
			    + (i_15_ >>> 8 & 0xff) * i_17_) >>> 8 << 8
			 | ((Main3D.iconFarColor >>> 17 & 0x3f) * i_16_
			    + (i_15_ >>> 16 & 0xff) * i_17_) >>> 8 << 16);
	    if (actor instanceof Aircraft) {
		if (d > iconAirDrawMin) {
		    Render.drawTile((float) i_13_, (float) i_14_ + 1.0F, 2.0F,
				    1.0F, (float) -p2d.z, iconFarMat, i_20_,
				    0.0F, 1.0F, 1.0F, -1.0F);
		    Render.drawTile((float) i_13_, (float) i_14_, 2.0F, 1.0F,
				    (float) -p2d.z, iconFarMat, i_21_, 0.0F,
				    1.0F, 1.0F, -1.0F);
		}
	    } else if (Main3D.this.isBomb(actor)) {
		if (d > iconSmallDrawMin) {
		    Render.drawTile((float) i_13_, (float) i_14_ + 1.0F, 1.0F,
				    1.0F, (float) -p2d.z, iconFarMat, i_20_,
				    0.0F, 1.0F, 1.0F, -1.0F);
		    Render.drawTile((float) i_13_, (float) i_14_, 1.0F, 1.0F,
				    (float) -p2d.z, iconFarMat, i_21_, 0.0F,
				    1.0F, 1.0F, -1.0F);
		}
	    } else if (d > iconGroundDrawMin) {
		Render.drawTile((float) i_13_, (float) i_14_ + 1.0F, 2.0F,
				1.0F, (float) -p2d.z, iconFarMat, i_20_, 0.0F,
				1.0F, 1.0F, -1.0F);
		Render.drawTile((float) i_13_, (float) i_14_, 2.0F, 1.0F,
				(float) -p2d.z, iconFarMat, i_21_, 0.0F, 1.0F,
				1.0F, -1.0F);
	    }
	    i_14_ += 8;
	    if (actor == iconFarPadlockActor && iconTypes() != 0) {
		iconFarPadlockItem.set(dotrange.colorIcon(d, i, i_16_), i_13_,
				       i_14_, 0, (float) -p2d.z, "");
		iconFarPadlockItem.bGround = !(actor instanceof Aircraft);
		if (World.cur().diffCur.No_Icons) {
		    int i_22_ = ((int) (dotrange.alphaIcon(d) * (double) i_16_)
				 & 0xff) << 24;
		    iconFarPadlockItem.color = i_22_ | 0xff00;
		}
	    }
	    if (!(actor instanceof Aircraft))
		return false;
	    if (World.cur().diffCur.No_Icons)
		return false;
	    if (iconTypes() == 0)
		return false;
	    if (d >= dotrange.icon())
		return false;
	    String string = null;
	    String string_23_ = null;
	    String string_24_ = null;
	    switch (iconTypes()) {
	    default:
		if (d <= dotrange.type()) {
		    string = Property.stringValue((Object) actor.getClass(),
						  iconFarFinger, null);
		    if (string == null) {
			string = actor.getClass().getName();
			int i_25_ = string.lastIndexOf('.');
			string = string.substring(i_25_ + 1);
			Property.set(actor.getClass(),
				     "iconFar_shortClassName", string);
		    }
		}
		/* fall through */
	    case 2:
		if (d <= dotrange.id())
		    string_23_ = ((Aircraft) actor).typedName();
		if (Mission.isNet() && ((NetAircraft) actor).isNetPlayer()
		    && i == World.getPlayerArmy() && d <= dotrange.name()) {
		    NetUser netuser = ((NetAircraft) actor).netUser();
		    if (string_23_ != null)
			string_23_ += " " + netuser.uniqueName();
		    else
			string_23_ = netuser.uniqueName();
		}
		/* fall through */
	    case 1: {
		if (d <= dotrange.range())
		    string_24_ = lenToString((int) d);
		String string_26_ = null;
		if (string_24_ != null)
		    string_26_ = string_24_;
		if (string != null) {
		    if (string_26_ != null)
			string_26_ += " " + (String) string;
		    else
			string_26_ = string;
		}
		if (string_23_ != null) {
		    if (string_26_ != null)
			string_26_ += ":" + (String) string_23_;
		    else
			string_26_ = string_23_;
		}
		if (dotrange.showAltIcon(d))
		    string_26_ = String.valueOf(dotrange.altSymbol());
		if (string_26_ != null)
		    Main3D.this.insertFarActorItem(dotrange.colorIcon(d, i,
								      i_16_),
						   i_13_, i_14_,
						   (float) -p2d.z, string_26_);
		return false;
	    }
	    }
	}
    }
    
    static class FarActorItem
    {
	public int color;
	public int x;
	public int y;
	public int dx;
	public float z;
	public String str;
	public boolean bGround;
	
	public void set(int i, int i_27_, int i_28_, int i_29_, float f,
			String string) {
	    color = i;
	    x = i_27_;
	    y = i_28_;
	    dx = i_29_;
	    z = f;
	    str = string;
	}
	
	public FarActorItem(int i, int i_30_, int i_31_, int i_32_, float f,
			    String string) {
	    set(i, i_30_, i_31_, i_32_, f, string);
	}
    }
    
    public class RenderMap2D extends Render
    {
	protected void contextResize(int i, int i_33_) {
	    super.contextResize(i, i_33_);
	    renderMap2DcontextResize(i, i_33_);
	    if (land2DText != null)
		land2DText.contextResized();
	}
	
	public void preRender() {
	    preRenderMap2D();
	    if (Main.state() != null && Main.state().id() == 18) {
		GUIBuilder guibuilder = (GUIBuilder) Main.state();
		guibuilder.builder.preRenderMap2D();
	    }
	}
	
	public void render() {
	    if (land2D != null)
		land2D.render();
	    if (Main.state() != null && Main.state().id() == 18) {
		GUIBuilder guibuilder = (GUIBuilder) Main.state();
		guibuilder.builder.renderMap2D();
	    } else if (land2DText != null)
		land2DText.render();
	    renderMap2D();
	}
	
	public RenderMap2D(float f) {
	    this(Engine.rendersMain(), f);
	}
	
	public RenderMap2D(Renders renders, float f) {
	    super(renders, f);
	    useClearDepth(false);
	    setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
	}
    }
    
    public class RenderHUD extends Render
    {
	protected void contextResize(int i, int i_34_) {
	    super.contextResize(i, i_34_);
	    renderHUDcontextResize(i, i_34_);
	    hud.contextResize(i, i_34_);
	    renders().setCommonClearColor(viewPort[0] != 0.0F
					  || viewPort[1] != 0.0F);
	}
	
	public void preRender() {
	    hud.preRender();
	    preRenderHUD();
	}
	
	public void render() {
	    hud.render();
	    renderHUD();
	}
	
	public RenderHUD(float f) {
	    this(Engine.rendersMain(), f);
	}
	
	public RenderHUD(Renders renders, float f) {
	    super(renders, f);
	    useClearDepth(false);
	    useClearColor(false);
	}
    }
    
    public class RenderCockpitMirror extends Render
    {
	protected void contextResize(int i, int i_35_) {
	    setViewPort(new int[] { mirrorX0(), mirrorY0(), mirrorWidth(),
				    mirrorHeight() });
	    if (camera != null)
		((Camera3D) camera).set(((Camera3D) camera).FOV(),
					((float) mirrorWidth()
					 / (float) mirrorHeight()));
	}
	
	public boolean isShow() {
	    if (viewMirror > 0 && renderCockpit.isShow())
		return cockpitCur.isExistMirror();
	    return false;
	}
	
	public void preRender() {
	    if (Actor.isValid(cockpitCur) && cockpitCur.isFocused())
		cockpitCur.preRender(true);
	}
	
	public void render() {
	    if (Actor.isValid(cockpitCur) && cockpitCur.isFocused()) {
		cockpitCur.render(true);
		Render.flush();
		cockpitCur.grabMirrorFromScreen(mirrorX0(), mirrorY0(),
						mirrorWidth(), mirrorHeight());
	    }
	}
	
	public RenderCockpitMirror(float f) {
	    super(Engine.rendersMain(), f);
	    useClearDepth(true);
	    useClearColor(false);
	    contextResized();
	}
    }
    
    public class RenderCockpit extends Render
    {
	int _indx = 0;
	
	public void preRender() {
	    iRenderIndx = _indx;
	    if (Actor.isValid(cockpitCur) && cockpitCur.isFocused())
		cockpitCur.preRender(false);
	    iRenderIndx = 0;
	}
	
	public void render() {
	    iRenderIndx = _indx;
	    if (Actor.isValid(cockpitCur) && cockpitCur.isFocused())
		cockpitCur.render(false);
	    iRenderIndx = 0;
	}
	
	public void getAspectViewPort(float[] fs) {
	    if (_indx == 0)
		super.getAspectViewPort(fs);
	    else
		_getAspectViewPort(_indx, fs);
	}
	
	public void getAspectViewPort(int[] is) {
	    if (_indx == 0)
		super.getAspectViewPort(is);
	    else
		_getAspectViewPort(_indx, is);
	}
	
	public boolean isShow() {
	    if (_indx == 0)
		return super.isShow();
	    return Config.cur.isUse3Renders() && renderCockpit.isShow();
	}
	
	public RenderCockpit(int i, float f) {
	    this(i, Engine.rendersMain(), f);
	}
	
	public RenderCockpit(int i, Renders renders, float f) {
	    super(renders, f);
	    _indx = i;
	    useClearDepth(true);
	    useClearColor(false);
	    contextResized();
	}
    }
    
    public class Render2DMirror extends Render
    {
	protected void contextResize(int i, int i_36_) {
	    setViewPort(new int[] { mirrorX0(), mirrorY0(), mirrorWidth(),
				    mirrorHeight() });
	    if (camera != null)
		((CameraOrtho2D) camera).set(0.0F, (float) mirrorWidth(), 0.0F,
					     (float) mirrorHeight());
	}
	
	public boolean isShow() {
	    return (viewMirror > 0 && render3D0.isShow() && !isViewOutside()
		    && cockpitCur.isExistMirror());
	}
	
	public void render() {
	    bRenderMirror = true;
	    if (bEnableFog)
		Render.enableFog(false);
	    drawFarActors();
	    if (bEnableFog)
		Render.enableFog(true);
	    bRenderMirror = false;
	}
	
	public Render2DMirror(float f) {
	    super(Engine.rendersMain(), f);
	    useClearDepth(false);
	    useClearColor(false);
	    contextResized();
	}
    }
    
    public class Render3D1Mirror extends Render3DMirror
    {
	public void render() {
	    bRenderMirror = true;
	    Main3D.this.doRender3D1(this);
	    bRenderMirror = false;
	}
	
	public Render3D1Mirror(float f) {
	    super(f);
	    useClearColor(false);
	    useClearDepth(false);
	    useClearStencil(false);
	}
    }
    
    public class Render3D0Mirror extends Render3DMirror
    {
	public void preRender() {
	    bRenderMirror = true;
	    Main3D.this.doPreRender3D(this);
	    bRenderMirror = false;
	}
	
	public void render() {
	    camera.activateWorldMode(0);
	    gl.GetDoublev(2982, _modelMatrix3DMirror);
	    gl.GetDoublev(2983, _projMatrix3DMirror);
	    gl.GetIntegerv(2978, _viewportMirror);
	    camera.deactivateWorldMode();
	    bRenderMirror = true;
	    Main3D.this.doRender3D0(this);
	    bRenderMirror = false;
	}
	
	public Render3D0Mirror(float f) {
	    super(f);
	    setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
	    useClearStencil(true);
	}
    }
    
    public class Render3DMirror extends Render
    {
	protected void contextResize(int i, int i_37_) {
	    setViewPort(new int[] { mirrorX0(), mirrorY0(), mirrorWidth(),
				    mirrorHeight() });
	    if (camera != null)
		((Camera3D) camera).set(((Camera3D) camera).FOV(),
					((float) mirrorWidth()
					 / (float) mirrorHeight()));
	}
	
	public boolean isShow() {
	    return (viewMirror > 0 && render3D0.isShow() && !isViewOutside()
		    && cockpitCur.isExistMirror());
	}
	
	public Render3DMirror(float f) {
	    super(Engine.rendersMain(), f);
	    contextResized();
	}
    }
    
    public class Render2D extends Render
    {
	int _indx = 0;
	
	public void render() {
	    iRenderIndx = _indx;
	    if (bEnableFog)
		Render.enableFog(false);
	    drawFarActors();
	    if (bEnableFog)
		Render.enableFog(true);
	    iRenderIndx = 0;
	}
	
	public void getAspectViewPort(float[] fs) {
	    if (_indx == 0)
		super.getAspectViewPort(fs);
	    else
		_getAspectViewPort(_indx, fs);
	}
	
	public void getAspectViewPort(int[] is) {
	    if (_indx == 0)
		super.getAspectViewPort(is);
	    else
		_getAspectViewPort(_indx, is);
	}
	
	public boolean isShow() {
	    if (_indx == 0)
		return super.isShow();
	    if (Main.state() != null && Main.state().id() == 18)
		return false;
	    return Config.cur.isUse3Renders() && render2D.isShow();
	}
	
	public Render2D(int i, float f) {
	    this(i, Engine.rendersMain(), f);
	}
	
	public Render2D(int i, Renders renders, float f) {
	    super(renders, f);
	    _indx = i;
	    useClearDepth(false);
	    useClearColor(false);
	    contextResized();
	}
    }
    
    public class Render3D1 extends Render
    {
	int _indx = 0;
	
	public void preRender() {
	    if (_indx == 0) {
		Main3D.this.drawTime();
		Main3D.this.drawTimeRP();
	    }
	}
	
	public void render() {
	    iRenderIndx = _indx;
	    Main3D.this.doRender3D1(this);
	    if (Main.state() != null && Main.state().id() == 18
		&& iRenderIndx == 0) {
		GUIBuilder guibuilder = (GUIBuilder) Main.state();
		guibuilder.builder.render3D();
	    }
	    iRenderIndx = 0;
	}
	
	public void getAspectViewPort(float[] fs) {
	    if (_indx == 0)
		super.getAspectViewPort(fs);
	    else
		_getAspectViewPort(_indx, fs);
	}
	
	public void getAspectViewPort(int[] is) {
	    if (_indx == 0)
		super.getAspectViewPort(is);
	    else
		_getAspectViewPort(_indx, is);
	}
	
	public boolean isShow() {
	    if (_indx == 0)
		return super.isShow();
	    if (Main.state() != null && Main.state().id() == 18)
		return false;
	    return Config.cur.isUse3Renders() && render3D1.isShow();
	}
	
	public Render3D1(int i, float f) {
	    this(i, Engine.rendersMain(), f);
	}
	
	public Render3D1(int i, Renders renders, float f) {
	    super(renders, f);
	    _indx = i;
	    useClearColor(false);
	    useClearDepth(false);
	    useClearStencil(false);
	    contextResized();
	}
    }
    
    public class Render3D0 extends Render
    {
	int _indx = 0;
	
	public void preRender() {
	    iRenderIndx = _indx;
	    if (_indx == 0)
		Main3D.this.shadowPairsClear();
	    Main3D.this.doPreRender3D(this);
	    iRenderIndx = 0;
	}
	
	public void render() {
	    iRenderIndx = _indx;
	    camera.activateWorldMode(0);
	    gl.GetDoublev(2982, _modelMatrix3D[iRenderIndx]);
	    gl.GetDoublev(2983, _projMatrix3D[iRenderIndx]);
	    gl.GetIntegerv(2978, _viewport[iRenderIndx]);
	    camera.deactivateWorldMode();
	    Main3D.this.doRender3D0(this);
	    iRenderIndx = 0;
	}
	
	public void getAspectViewPort(float[] fs) {
	    if (_indx == 0)
		super.getAspectViewPort(fs);
	    else
		_getAspectViewPort(_indx, fs);
	}
	
	public void getAspectViewPort(int[] is) {
	    if (_indx == 0)
		super.getAspectViewPort(is);
	    else
		_getAspectViewPort(_indx, is);
	}
	
	public boolean isShow() {
	    if (_indx == 0)
		return super.isShow();
	    if (Main.state() != null && Main.state().id() == 18)
		return false;
	    return Config.cur.isUse3Renders() && render3D0.isShow();
	}
	
	public Render3D0(int i, float f) {
	    this(i, Engine.rendersMain(), f);
	}
	
	public Render3D0(int i, Renders renders, float f) {
	    super(renders, f);
	    _indx = i;
	    setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
	    useClearStencil(true);
	    contextResized();
	}
    }
    
    class DrwArray
    {
	ArrayList drwSolidPlate = new ArrayList();
	ArrayList drwTranspPlate = new ArrayList();
	ArrayList drwShadowPlate = new ArrayList();
	ArrayList drwSolid = new ArrayList();
	ArrayList drwTransp = new ArrayList();
	ArrayList drwShadow = new ArrayList();
    }
    
    class ShadowPairsFilter implements ActorFilter
    {
	public boolean isUse(Actor actor, double d) {
	    if (actor != shadowPairsCur1 && actor instanceof ActorHMesh
		&& d <= Main3D.shadowPairsR2
		&& ((ActorHMesh) actor).hierMesh() != null) {
		shadowPairsList2.add(shadowPairsCur1.hierMesh());
		shadowPairsList2.add(((ActorHMesh) actor).hierMesh());
	    }
	    return false;
	}
    }
    
    public class Render3D0R extends Render
    {
	ArrayList drwSolidL = new ArrayList();
	ArrayList drwTranspL = new ArrayList();
	ArrayList drwSolid = new ArrayList();
	ArrayList drwTransp = new ArrayList();
	
	public void preRender() {
	    if (Engine.land().ObjectsReflections_Begin(0) == 1) {
		getCamera().pos.getRender(__p, __o);
		boolean bool = true;
		if (Landscape.isExistMeshs()) {
		    drwSolid.clear();
		    drwTransp.clear();
		    com.maddox.il2.engine.DrawEnv drawenv = Engine.drawEnv();
		    double d = __p.x;
		    double d_38_ = __p.y;
		    double d_39_ = __p.z;
		    if (Engine.cur.world != null) {
			/* empty */
		    }
		    drawenv.preRender(d, d_38_, d_39_,
				      World.MaxVisualDistance * 0.5F, 1,
				      drwSolidL, drwTranspL, null, bool);
		    int i = drwSolidL.size();
		    for (int i_40_ = 0; i_40_ < i; i_40_++) {
			Actor actor = (Actor) drwSolidL.get(i_40_);
			if (actor instanceof ActorLandMesh)
			    drwSolid.add(actor);
		    }
		    i = drwTranspL.size();
		    for (int i_41_ = 0; i_41_ < i; i_41_++) {
			Actor actor = (Actor) drwTranspL.get(i_41_);
			if (actor instanceof ActorLandMesh)
			    drwTransp.add(actor);
		    }
		    bool = false;
		}
		com.maddox.il2.engine.DrawEnv drawenv = Engine.drawEnv();
		double d = __p.x;
		double d_42_ = __p.y;
		double d_43_ = __p.z;
		if (Engine.cur.world != null) {
		    /* empty */
		}
		drawenv.preRender(d, d_42_, d_43_,
				  World.MaxVisualDistance * 0.5F, 14, drwSolid,
				  drwTransp, null, bool);
		Engine.land().ObjectsReflections_End();
	    }
	}
	
	public void render() {
	    if (Engine.land().ObjectsReflections_Begin(1) == 1) {
		draw(drwSolid, drwTransp);
		Engine.land().ObjectsReflections_End();
	    }
	}
	
	public boolean isShow() {
	    return bDrawLand && render3D0.isShow();
	}
	
	public Render3D0R(float f) {
	    super(Engine.rendersMain(), f);
	}
    }
    
    public static class HookReflection extends HookRender
    {
	public boolean computeRenderPos(Actor actor, Loc loc, Loc loc_44_) {
	    computePos(actor, loc, loc_44_);
	    return true;
	}
	
	public void computePos(Actor actor, Loc loc, Loc loc_45_) {
	    Point3d point3d = loc.getPoint();
	    Orient orient = loc.getOrient();
	    loc_45_.set(loc);
	}
    }
    
    public static class Camera3DR extends Camera3D
    {
	public boolean activate(float f, int i, int i_46_, int i_47_,
				int i_48_, int i_49_, int i_50_, int i_51_,
				int i_52_, int i_53_, int i_54_) {
	    Engine.land().ObjectsReflections_Begin(0);
	    return super.activate(f, i / 2, i_46_ / 2, i_47_, i_48_, i_49_ / 2,
				  i_50_ / 2, i_51_, i_52_, i_53_ / 2,
				  i_54_ / 2);
	}
    }
    
    public class NetCamera3D extends Camera3D
    {
	public void set(float f) {
	    super.set(f);
	    _camera3D[1].set(f);
	    _camera3D[1].pos.setRel(new Orient(-f, 0.0F, 0.0F));
	    _camera3D[2].set(f);
	    _camera3D[2].pos.setRel(new Orient(f, 0.0F, 0.0F));
	    _cameraCockpit[1].set(f);
	    _cameraCockpit[1].pos.setRel(new Orient(-f, 0.0F, 0.0F));
	    _cameraCockpit[2].set(f);
	    _cameraCockpit[2].pos.setRel(new Orient(f, 0.0F, 0.0F));
	    camera3DR.set(f);
	}
    }
    
    public static Main3D cur3D() {
	return (Main3D) cur();
    }
    
    public boolean isUseStartLog() {
	return bUseStartLog;
    }
    
    public boolean isShowStartIntro() {
	return bShowStartIntro;
    }
    
    public boolean isDrawLand() {
	return bDrawLand;
    }
    
    public void setDrawLand(boolean bool) {
	bDrawLand = bool;
    }
    
    public boolean isDemoPlaying() {
	if (playRecordedStreams != null)
	    return true;
	if (keyRecord == null)
	    return false;
	return keyRecord.isPlaying();
    }
    
    public Actor viewActor() {
	return camera3D.pos.base();
    }
    
    public boolean isViewInsideShow() {
	if (!Actor.isValid(cockpitCur))
	    return true;
	return !cockpitCur.isNullShow();
    }
    
    public boolean isEnableRenderingCockpit() {
	if (!Actor.isValid(cockpitCur))
	    return true;
	return cockpitCur.isEnableRendering();
    }
    
    public boolean isViewOutside() {
	if (!Actor.isValid(cockpitCur))
	    return true;
	return !cockpitCur.isFocused();
    }
    
    public boolean isViewPadlock() {
	if (!Actor.isValid(cockpitCur))
	    return false;
	return cockpitCur.isPadlock();
    }
    
    public Actor getViewPadlockEnemy() {
	if (!Actor.isValid(cockpitCur))
	    return null;
	return cockpitCur.getPadlockEnemy();
    }
    
    public void setViewInsideShow(boolean bool) {
	if (!isViewOutside() && isViewInsideShow() != bool
	    && Actor.isValid(cockpitCur))
	    cockpitCur.setNullShow(!bool);
    }
    
    public void setEnableRenderingCockpit(boolean bool) {
	if (!isViewOutside() && isEnableRenderingCockpit() != bool
	    && Actor.isValid(cockpitCur))
	    cockpitCur.setEnableRendering(bool);
    }
    
    private void endViewFly() {
	if (bViewFly) {
	    hookViewFly.use(false);
	    Engine.soundListener().setUseBaseSpeed(true);
	    bViewFly = false;
	}
    }
    
    private void endViewEnemy() {
	if (bViewEnemy) {
	    hookViewEnemy.stop();
	    bViewEnemy = false;
	}
    }
    
    private void endView() {
	if (isViewOutside() && (!bViewFly && !bViewEnemy))
	    hookView.use(false);
    }
    
    private void endViewInside() {
	if (!isViewOutside())
	    cockpitCur.focusLeave();
    }
    
    public void setViewFly(Actor actor) {
	endView();
	endViewEnemy();
	endViewInside();
	Selector.resetGame();
	hookViewFly.use(true);
	bViewFly = true;
	camera3D.pos.setRel(new Point3d(), new Orient());
	camera3D.pos.changeBase(actor, hookViewFly, false);
	camera3D.pos.resetAsBase();
	Engine.soundListener().setUseBaseSpeed(false);
    }
    
    private void setViewSomebody(Actor actor) {
	endView();
	endViewFly();
	endViewInside();
	bViewEnemy = true;
	camera3D.pos.setRel(new Point3d(), new Orient());
	camera3D.pos.changeBase(actor, hookViewEnemy, false);
	camera3D.pos.resetAsBase();
	if (actor instanceof ActorViewPoint)
	    ((ActorViewPoint) actor).setViewActor(hookViewEnemy.enemy());
    }
    
    public void setViewEnemy(Actor actor, boolean bool, boolean bool_55_) {
	Actor actor_56_ = Selector.look(bool, bool_55_, camera3D,
					actor.getArmy(), -1, actor, true);
	if (!hookViewEnemy.start(actor, actor_56_, bool_55_, true)) {
	    if (bViewEnemy)
		setView(actor);
	} else
	    setViewSomebody(actor);
    }
    
    public void setViewFriend(Actor actor, boolean bool, boolean bool_57_) {
	Actor actor_58_ = Selector.look(bool, bool_57_, camera3D, -1,
					actor.getArmy(), actor, true);
	if (!hookViewEnemy.start(actor, actor_58_, bool_57_, false)) {
	    if (bViewEnemy)
		setView(actor);
	} else
	    setViewSomebody(actor);
    }
    
    public void setViewPadlock(boolean bool, boolean bool_59_) {
	if (!isViewOutside() && cockpitCur.existPadlock()) {
	    Aircraft aircraft = World.getPlayerAircraft();
	    Actor actor;
	    if (World.cur().diffCur.No_Icons)
		actor = Selector.look(true, bool_59_, camera3D, -1, -1,
				      aircraft, false);
	    else if (bool)
		actor = Selector.look(true, bool_59_, camera3D, -1,
				      aircraft.getArmy(), aircraft, false);
	    else
		actor = Selector.look(true, bool_59_, camera3D,
				      aircraft.getArmy(), -1, aircraft, false);
	    if (actor == null || actor == aircraft
		|| !cockpitCur.startPadlock(actor)) {
		/* empty */
	    }
	}
    }
    
    public void setViewEndPadlock() {
	if (!isViewOutside() && cockpitCur.existPadlock())
	    cockpitCur.endPadlock();
    }
    
    public void setViewNextPadlock(boolean bool) {
	if (!isViewOutside() && cockpitCur.existPadlock()) {
	    Actor actor = Selector.next(bool);
	    if (actor == null || !cockpitCur.startPadlock(actor)) {
		/* empty */
	    }
	}
    }
    
    public void setViewPadlockForward(boolean bool) {
	if (isViewPadlock())
	    cockpitCur.setPadlockForward(bool);
    }
    
    public void setViewInside() {
	if ((isViewOutside() || isViewPadlock()) && Actor.isValid(cockpitCur)
	    && cockpitCur.isEnableFocusing()) {
	    endView();
	    endViewFly();
	    endViewEnemy();
	    endViewInside();
	    cockpitCur.focusEnter();
	    if (World.Sun().needsCubeUpdate((float) camera3D.pos.getAbsPoint().z))
		World.land().cubeFullUpdate();
	}
    }
    
    public void setViewFlow30(Actor actor) {
	setView(actor, true);
	hookView.set(actor, 30.0F, -30.0F);
	camera3D.pos.resetAsBase();
    }
    
    public void setViewFlow10(Actor actor, boolean bool) {
	hookView.setFollow(bool);
	setView(actor, true);
	hookView.set(actor, 10.0F, -10.0F);
	camera3D.pos.resetAsBase();
    }
    
    public void setView(Actor actor) {
	setView(actor, false);
    }
    
    public void setView(Actor actor, boolean bool) {
	if (viewActor() != actor || bool || bViewFly || bViewEnemy) {
	    boolean bool_60_ = viewActor() != actor;
	    endViewFly();
	    endViewEnemy();
	    endViewInside();
	    Selector.resetGame();
	    hookView.use(true);
	    camera3D.pos.setRel(new Point3d(), new Orient());
	    camera3D.pos.changeBase(actor, hookView, false);
	    camera3D.pos.resetAsBase();
	    if (bool_60_
		&& World.Sun()
		       .needsCubeUpdate((float) camera3D.pos.getAbsPoint().z))
		World.land().cubeFullUpdate();
	}
    }
    
    public int cockpitCurIndx() {
	if (cockpits == null || cockpitCur == null)
	    return -1;
	for (int i = 0; i < cockpits.length; i++) {
	    if (cockpitCur == cockpits[i])
		return i;
	}
	return -1;
    }
    
    public void beginStep(int i) {
	if (!bUseStartLog) {
	    if (i >= 0)
		ConsoleGL0.exclusiveDrawStep((I18N.gui("main.loading") + " "
					      + i + "%"),
					     i);
	    else
		ConsoleGL0.exclusiveDrawStep(null, -1);
	}
    }
    
    public boolean beginApp(String string, String string_61_, int i) {
	Config.cur.mainSection = string_61_;
	Engine.cur = new Engine();
	Config.typeProvider();
	GLContext glcontext
	    = Config.cur.createGlContext(Config.cur.ini.get((Config.cur
							     .mainSection),
							    "title", "il2"));
	return beginApp(glcontext, i);
    }
    
    public boolean beginApp(GLContext glcontext, int i) {
	Config.typeGlStrings();
	Config.cur.typeContextSettings(glcontext);
	bDrawIfNotFocused = Config.cur.ini.get("window", "DrawIfNotFocused",
					       bDrawIfNotFocused);
	bShowStartIntro = Config.cur.ini.get("game", "intro", bShowStartIntro);
	RTSConf.cur.start();
	PaintScheme.init();
	NetEnv.cur().connect = new Connect();
	NetEnv.cur();
	NetEnv.host().destroy();
	new NetUser("No Name");
	NetEnv.active(true);
	Config.cur.beginSound();
	RenderContext.activate(glcontext);
	RendersMain.setSaveAspect(true);
	RendersMain.setGlContext(glcontext);
	RendersMain.setTickPainting(true);
	TTFont.font[0] = TTFont.get("arialSmall");
	TTFont.font[1] = TTFont.get("arial10");
	TTFont.font[2] = TTFont.get("arialb12");
	TTFont.font[3] = TTFont.get("courSmall");
	ConsoleGL0.init("Console", i);
	bUseStartLog
	    = Config.cur.ini.get("Console", "UseStartLog", bUseStartLog);
	if (bUseStartLog)
	    ConsoleGL0.exclusiveDraw(true);
	else
	    ConsoleGL0.exclusiveDraw("gui/background0.mat");
	beginStep(5);
	CmdEnv.top().exec("CmdLoad com.maddox.rts.cmd.CmdLoad");
	CmdEnv.top().exec("load com.maddox.rts.cmd.CmdFile PARAM CURENV on");
	CmdSFS.bMountError = false;
	CmdEnv.top().exec("file .rc");
	if (CmdSFS.bMountError)
	    return false;
	beginStep(10);
	try {
	    Engine.setWorldAcoustics("Landscape");
	} catch (Exception exception) {
	    System.out.println("World Acoustics NOT initialized: "
			       + exception.getMessage());
	    return false;
	}
	Engine.soundListener().initDraw();
	beginStep(15);
	Regiment.loadAll();
	preloadNetClasses();
	beginStep(20);
	preloadAirClasses();
	beginStep(25);
	preloadChiefClasses();
	beginStep(30);
	preloadStationaryClasses();
	preload();
	beginStep(35);
	camera3D = new NetCamera3D();
	camera3D.setName("camera");
	camera3D.set(FOVX, 1.2F, 48000.0F);
	render3D0 = new Render3D0(0, 1.0F);
	render3D0.setSaveAspect(Config.cur.windowSaveAspect);
	render3D0.setName("render3D0");
	render3D0.setCamera(camera3D);
	Engine.lightEnv().sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
	Vector3f vector3f = new Vector3f(0.0F, 1.0F, -1.0F);
	vector3f.normalize();
	Engine.lightEnv().sun().set(vector3f);
	_camera3D[0] = camera3D;
	_render3D0[0] = render3D0;
	for (int i_62_ = 1; i_62_ < 3; i_62_++) {
	    _camera3D[i_62_] = new Camera3D();
	    _camera3D[i_62_].set(FOVX, 1.2F, 48000.0F);
	    _camera3D[i_62_].pos.setBase(camera3D, null, false);
	    _render3D0[i_62_]
		= new Render3D0(i_62_, 1.0F - (float) i_62_ * 0.0010F);
	    _render3D0[i_62_].setSaveAspect(true);
	    _render3D0[i_62_].setCamera(_camera3D[i_62_]);
	}
	_camera3D[1].pos.setRel(new Orient(-FOVX, 0.0F, 0.0F));
	_camera3D[2].pos.setRel(new Orient(FOVX, 0.0F, 0.0F));
	render3D1 = new Render3D1(0, 0.9F);
	render3D1.setSaveAspect(Config.cur.windowSaveAspect);
	render3D1.setName("render3D1");
	render3D1.setCamera(camera3D);
	for (int i_63_ = 1; i_63_ < 3; i_63_++) {
	    _render3D1[i_63_]
		= new Render3D1(i_63_, 0.9F - (float) i_63_ * 0.0010F);
	    _render3D1[i_63_].setSaveAspect(true);
	    _render3D1[i_63_].setCamera(_camera3D[i_63_]);
	}
	camera3DR = new Camera3DR();
	camera3DR.setName("cameraR");
	camera3DR.set(FOVX, 1.2F, 48000.0F);
	camera3DR.pos.setBase(camera3D, new HookReflection(), false);
	render3D0R = new Render3D0R(1.1F);
	render3D0R.setSaveAspect(Config.cur.windowSaveAspect);
	render3D0R.setName("render3D0R");
	render3D0R.setCamera(camera3DR);
	Engine.soundListener().pos.setBase(camera3D, null, false);
	TextScr.font();
	camera2D = new CameraOrtho2D();
	camera2D.setName("camera2D");
	render2D = new Render2D(0, 0.95F);
	render2D.setSaveAspect(Config.cur.windowSaveAspect);
	render2D.setName("render2D");
	render2D.setCamera(camera2D);
	camera2D.set(0.0F, (float) render2D.getViewPortWidth(), 0.0F,
		     (float) render2D.getViewPortHeight());
	camera2D.set(0.0F, 1.0F);
	render2D.setShow(true);
	_camera2D[0] = camera2D;
	_render2D[0] = render2D;
	for (int i_64_ = 1; i_64_ < 3; i_64_++) {
	    _camera2D[i_64_] = new CameraOrtho2D();
	    _render2D[i_64_]
		= new Render2D(i_64_, 0.95F - (float) i_64_ * 0.0010F);
	    _render2D[i_64_].setSaveAspect(true);
	    _render2D[i_64_].setCamera(_camera2D[i_64_]);
	    _camera2D[i_64_].set(0.0F,
				 (float) _render2D[i_64_].getViewPortWidth(),
				 0.0F,
				 (float) _render2D[i_64_].getViewPortHeight());
	    _camera2D[i_64_].set(0.0F, 1.0F);
	}
	camera3DMirror = new Cockpit.Camera3DMirror();
	camera3DMirror.setName("cameraMirror");
	camera3DMirror.set(FOVX, 1.2F, 48000.0F);
	camera3DMirror.pos
	    .setBase(camera3D, Cockpit.getHookCamera3DMirror(false), false);
	render3D0Mirror = new Render3D0Mirror(1.8F);
	render3D0Mirror.setName("render3D0Mirror");
	render3D0Mirror.setCamera(camera3DMirror);
	render3D1Mirror = new Render3D1Mirror(1.78F);
	render3D1Mirror.setName("render3D1Mirror");
	render3D1Mirror.setCamera(camera3DMirror);
	camera2DMirror = new CameraOrtho2D();
	camera2DMirror.setName("camera2DMirror");
	render2DMirror = new Render2DMirror(1.79F);
	render2DMirror.setName("render2DMirror");
	render2DMirror.setCamera(camera2DMirror);
	camera2DMirror.set(0.0F, (float) render2DMirror.getViewPortWidth(),
			   0.0F, (float) render2DMirror.getViewPortHeight());
	camera2DMirror.set(0.0F, 1.0F);
	cameraCockpit = new Camera3D();
	cameraCockpit.setName("cameraCockpit");
	cameraCockpit.set(FOVX, 0.05F, 12.5F);
	renderCockpit = new RenderCockpit(0, 0.5F);
	renderCockpit.setSaveAspect(Config.cur.windowSaveAspect);
	renderCockpit.setName("renderCockpit");
	renderCockpit.setCamera(cameraCockpit);
	renderCockpit.setShow(false);
	_cameraCockpit[0] = cameraCockpit;
	_renderCockpit[0] = renderCockpit;
	for (int i_65_ = 1; i_65_ < 3; i_65_++) {
	    _cameraCockpit[i_65_] = new Camera3D();
	    _cameraCockpit[i_65_].set(FOVX, 0.05F, 12.5F);
	    _cameraCockpit[i_65_].pos.setBase(cameraCockpit, null, false);
	    _renderCockpit[i_65_]
		= new RenderCockpit(i_65_, 0.5F - (float) i_65_ * 0.0010F);
	    _renderCockpit[i_65_].setSaveAspect(true);
	    _renderCockpit[i_65_].setCamera(_cameraCockpit[i_65_]);
	}
	_cameraCockpit[1].pos.setRel(new Orient(-FOVX, 0.0F, 0.0F));
	_cameraCockpit[2].pos.setRel(new Orient(FOVX, 0.0F, 0.0F));
	cameraCockpitMirror = new Cockpit.Camera3DMirror();
	cameraCockpitMirror.pos.setBase(cameraCockpit,
					Cockpit.getHookCamera3DMirror(true),
					false);
	cameraCockpitMirror.setName("cameraCockpitMirror");
	cameraCockpitMirror.set(FOVX, 0.05F, 12.5F);
	renderCockpitMirror = new RenderCockpitMirror(1.77F);
	renderCockpitMirror.setName("renderCockpitMirror");
	renderCockpitMirror.setCamera(cameraCockpitMirror);
	cameraHUD = new CameraOrtho2D();
	cameraHUD.setName("cameraHUD");
	renderHUD = new RenderHUD(0.3F);
	renderHUD.setName("renderHUD");
	renderHUD.setCamera(cameraHUD);
	cameraHUD.set(0.0F, (float) renderHUD.getViewPortWidth(), 0.0F,
		      (float) renderHUD.getViewPortHeight());
	cameraHUD.set(-1000.0F, 1000.0F);
	renderHUD.setShow(true);
	LightEnvXY lightenvxy = new LightEnvXY();
	renderHUD.setLightEnv(lightenvxy);
	lightenvxy.sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
	vector3f = new Vector3f(0.0F, 1.0F, -1.0F);
	vector3f.normalize();
	lightenvxy.sun().set(vector3f);
	hud = new HUD();
	renderHUD.contextResized();
	drawFarActorsInit();
	cameraMap2D = new CameraOrtho2D();
	cameraMap2D.setName("cameraMap2D");
	renderMap2D = new RenderMap2D(0.2F);
	renderMap2D.setName("renderMap2D");
	renderMap2D.setCamera(cameraMap2D);
	cameraMap2D.set(0.0F, (float) renderMap2D.getViewPortWidth(), 0.0F,
			(float) renderMap2D.getViewPortHeight());
	renderMap2D.setShow(false);
	beginStep(40);
	_sunFlareRender[0] = SunFlare.newRender(0, 0.19F, _camera3D[0]);
	_sunFlareRender[0].setName("renderSunFlare");
	_sunFlareRender[0].setSaveAspect(Config.cur.windowSaveAspect);
	_sunFlareRender[0].setShow(false);
	for (int i_66_ = 1; i_66_ < 3; i_66_++) {
	    _sunFlareRender[i_66_]
		= SunFlare.newRender(i_66_, 0.19F, _camera3D[i_66_]);
	    _sunFlareRender[i_66_].setSaveAspect(true);
	    _sunFlareRender[i_66_].setShow(false);
	}
	lightsGlare = new LightsGlare(0, 0.17F);
	lightsGlare.setSaveAspect(Config.cur.windowSaveAspect);
	lightsGlare.setCamera(new CameraOrtho2D());
	lightsGlare.setShow(false);
	_lightsGlare[0] = lightsGlare;
	for (int i_67_ = 1; i_67_ < 3; i_67_++) {
	    _lightsGlare[i_67_]
		= new LightsGlare(i_67_, 0.17F - (float) i_67_ * 0.0010F);
	    _lightsGlare[i_67_].setSaveAspect(true);
	    _lightsGlare[i_67_].setCamera(new CameraOrtho2D());
	}
	sunGlare = new SunGlare(0, 0.15F);
	sunGlare.setSaveAspect(Config.cur.windowSaveAspect);
	sunGlare.setCamera(new CameraOrtho2D());
	sunGlare.setShow(false);
	_sunGlare[0] = sunGlare;
	for (int i_68_ = 1; i_68_ < 3; i_68_++) {
	    _sunGlare[i_68_]
		= new SunGlare(i_68_, 0.15F - (float) i_68_ * 0.0010F);
	    _sunGlare[i_68_].setSaveAspect(true);
	    _sunGlare[i_68_].setCamera(new CameraOrtho2D());
	}
	overLoad = new OverLoad(0, 0.1F);
	overLoad.setSaveAspect(Config.cur.windowSaveAspect);
	overLoad.setCamera(new CameraOrtho2D());
	overLoad.setShow(false);
	_overLoad[0] = overLoad;
	for (int i_69_ = 1; i_69_ < 3; i_69_++) {
	    _overLoad[i_69_]
		= new OverLoad(i_69_, 0.1F - (float) i_69_ * 0.0010F);
	    _overLoad[i_69_].setSaveAspect(true);
	    _overLoad[i_69_].setCamera(new CameraOrtho2D());
	}
	_cinema[0] = new Cinema(0, 0.09F);
	_cinema[0].setSaveAspect(Config.cur.windowSaveAspect);
	_cinema[0].setCamera(new CameraOrtho2D());
	_cinema[0].setShow(false);
	for (int i_70_ = 1; i_70_ < 3; i_70_++) {
	    _cinema[i_70_]
		= new Cinema(i_70_, 0.09F - (float) i_70_ * 0.0010F);
	    _cinema[i_70_].setSaveAspect(true);
	    _cinema[i_70_].setCamera(new CameraOrtho2D());
	    _cinema[i_70_].setShow(false);
	}
	timeSkip = new TimeSkip(-1.1F);
	HotKeyEnv.fromIni("hotkeys", Config.cur.ini,
			  Config.cur.ini.get(Config.cur.mainSection, "hotkeys",
					     "hotkeys"));
	FreeFly.init("FreeFly");
	FreeFlyXYZ.init("FreeFlyXYZ");
	hookView = new HookView("HookView");
	hookView.setCamera(camera3D);
	hookViewFly = new HookViewFly("HookViewFly");
	hookViewEnemy = new HookViewEnemy();
	hookViewEnemy.setCamera(camera3D);
	HookPilot.New();
	HookPilot.current.setTarget(cameraCockpit);
	HookPilot.current.setTarget2(camera3D);
	HookKeys.New();
	aircraftHotKeys = new AircraftHotKeys();
	beginStep(45);
	HotKeyCmdEnv.enable("default", false);
	HotKeyCmdEnv.enable("Console", false);
	HotKeyCmdEnv.enable("hotkeys", false);
	HotKeyCmdEnv.enable("HookView", false);
	HotKeyCmdEnv.enable("PanView", false);
	HotKeyCmdEnv.enable("HeadMove", false);
	HotKeyCmdEnv.enable("SnapView", false);
	HotKeyCmdEnv.enable("pilot", false);
	HotKeyCmdEnv.enable("move", false);
	HotKeyCmdEnv.enable("gunner", false);
	HotKeyEnv.enable("pilot", false);
	HotKeyEnv.enable("move", false);
	HotKeyEnv.enable("gunner", false);
	HotKeyCmdEnv.enable("misc", false);
	HotKeyCmdEnv.enable("$$$misc", true);
	HotKeyEnv.enable("$$$misc", true);
	HotKeyCmdEnv.enable("orders", false);
	HotKeyCmdEnv.enable("aircraftView", false);
	HotKeyCmdEnv.enable("timeCompression", false);
	HotKeyCmdEnv.enable("gui", false);
	HotKeyCmdEnv.enable("builder", false);
	HotKeyCmdEnv.enable("MouseXYZ", false);
	HotKeyCmdEnv.enable("FreeFly", false);
	HotKeyCmdEnv.enable("FreeFlyXYZ", false);
	World.cur().userCfg = UserCfg.loadCurrent();
	World.cur().setUserCovers();
	ordersTree = new OrdersTree(true);
	beginStep(50);
	if (bUseGUI) {
	    guiManager = GUI.create("gui");
	    keyRecord = new KeyRecord();
	    keyRecord.addExcludePrevCmd(278);
	    Keyboard.adapter().setKeyEnable(27);
	}
	beginStep(90);
	initHotKeys();
	Voice.setEnableVoices(!Config.cur.ini.get("game", "NoChatter", false));
	beginStep(95);
	viewSet_Load();
	DeviceLink.start();
	onBeginApp();
	Time.setPause(false);
	RTSConf.cur.loopMsgs();
	Time.setPause(true);
	new MsgAction(64, 1.0 + Math.random() * 10.0) {
	    public void doAction() {
		try {
		    Class.forName("fbapi");
		    Main.doGameExit();
		} catch (Throwable throwable) {
		    /* empty */
		}
	    }
	};
	bDrawClouds = true;
	TextScr.setColor(new Color4f(1.0F, 0.0F, 0.0F, 1.0F));
	RTSConf.cur.console.getEnv().exec("file rcu");
	beginStep(-1);
	createConsoleServer();
	return true;
    }
    
    public void setSaveAspect(boolean bool) {
	if (Config.cur.windowSaveAspect != bool) {
	    render3D0.setSaveAspect(bool);
	    render3D1.setSaveAspect(bool);
	    render2D.setSaveAspect(bool);
	    renderCockpit.setSaveAspect(bool);
	    _sunFlareRender[0].setSaveAspect(bool);
	    lightsGlare.setSaveAspect(bool);
	    sunGlare.setSaveAspect(bool);
	    overLoad.setSaveAspect(bool);
	    _cinema[0].setSaveAspect(bool);
	    Config.cur.windowSaveAspect = bool;
	}
    }
    
    public static void menuMusicPlay() {
	menuMusicPlay(_sLastMusic);
    }
    
    public static void menuMusicPlay(String string) {
	string = Regiment.getCountryFromBranch(string);
	_sLastMusic = string;
	CmdEnv.top().exec("music FILE music/menu/" + _sLastMusic);
    }
    
    public void viewSet_Load() {
	int i = Config.cur.ini.get("game", "viewSet", 0);
	viewSet_Set(i);
	iconTypes = Config.cur.ini.get("game", "iconTypes", 3, 0, 3);
    }
    
    public void viewSet_Save() {
	if (aircraftHotKeys != null) {
	    Config.cur.ini.set("game", "viewSet", viewSet_Get());
	    Config.cur.ini.set("game", "iconTypes", iconTypes());
	}
    }
    
    protected int viewSet_Get() {
	int i = 0;
	if (HookKeys.current != null && HookKeys.current.isPanView())
	    i |= 0x1;
	if (HookPilot.current != null && HookPilot.current.isAim())
	    i |= 0x2;
	i |= (viewMirror & 0x3) << 2;
	if (!aircraftHotKeys.isAutoAutopilot())
	    i |= 0x10;
	i |= (HUD.drawSpeed() & 0x3) << 5;
	return i;
    }
    
    private void viewSet_Set(int i) {
	HookKeys.current.setMode((i & 0x1) != 0);
	HookPilot.current.doAim((i & 0x2) != 0);
	viewMirror = i >> 2 & 0x3;
	aircraftHotKeys.setAutoAutopilot((i & 0x10) == 0);
	HUD.setDrawSpeed(i >> 5 & 0x3);
    }
    
    public boolean isViewMirror() {
	return viewMirror > 0;
    }
    
    public int iconTypes() {
	return iconTypes;
    }
    
    protected void changeIconTypes() {
	iconTypes = (iconTypes + 1) % 4;
    }
    
    public void disableAllHotKeyCmdEnv() {
	List list = HotKeyCmdEnv.allEnv();
	int i = list.size();
	for (int i_73_ = 0; i_73_ < i; i_73_++) {
	    HotKeyCmdEnv hotkeycmdenv = (HotKeyCmdEnv) list.get(i_73_);
	    hotkeycmdenv.enable(false);
	}
	HotKeyCmdEnv.enable("hotkeys", true);
	HotKeyCmdEnv.enable("$$$misc", true);
    }
    
    public void enableHotKeyCmdEnvs(String[] strings) {
	for (int i = 0; i < strings.length; i++)
	    HotKeyCmdEnv.enable(strings[i], true);
    }
    
    public void enableOnlyHotKeyCmdEnvs(String[] strings) {
	disableAllHotKeyCmdEnv();
	enableHotKeyCmdEnvs(strings);
    }
    
    public void enableOnlyHotKeyCmdEnv(String string) {
	disableAllHotKeyCmdEnv();
	HotKeyCmdEnv.enable(string, true);
    }
    
    public void enableGameHotKeyCmdEnvs() {
	enableHotKeyCmdEnvs(gameHotKeyCmdEnvs);
    }
    
    public void enableOnlyGameHotKeyCmdEnvs() {
	enableOnlyHotKeyCmdEnvs(gameHotKeyCmdEnvs);
    }
    
    public void enableCockpitHotKeys() {
	if (!isDemoPlaying() && Actor.isValid(cockpitCur)) {
	    String[] strings = cockpitCur.getHotKeyEnvs();
	    if (strings != null) {
		for (int i = 0; i < strings.length; i++) {
		    if (strings[i] != null)
			HotKeyEnv.enable(strings[i], true);
		}
	    }
	}
    }
    
    public void disableCockpitHotKeys() {
	if (!isDemoPlaying() && Actor.isValid(cockpitCur)) {
	    String[] strings = cockpitCur.getHotKeyEnvs();
	    if (strings != null) {
		for (int i = 0; i < strings.length; i++) {
		    if (strings[i] != null)
			HotKeyEnv.enable(strings[i], false);
		}
	    }
	}
    }
    
    private void _disableCockpitsHotKeys() {
	HotKeyEnv.enable("pilot", false);
	HotKeyEnv.enable("move", false);
	HotKeyEnv.enable("gunner", false);
    }
    
    public void disableCockpitsHotKeys() {
	if (!isDemoPlaying())
	    _disableCockpitsHotKeys();
    }
    
    public void resetGameClear() {
	SearchlightGeneric.resetGame();
	disableCockpitsHotKeys();
	camera3D.pos.changeBase(null, null, false);
	camera3D.pos.setAbs(new Point3d(), new Orient());
	FreeFly.adapter().resetGame();
	if (HookPilot.current != null) {
	    HookPilot.current.use(false);
	    HookPilot.current.resetGame();
	}
	HookGunner.resetGame();
	if (HookKeys.current != null)
	    HookKeys.current.resetGame();
	hookViewFly.reset();
	hookViewEnemy.reset();
	hookView.reset();
	hookView.resetGame();
	hookViewEnemy.resetGame();
	overLoad.setShow(false);
	for (int i = 0; i < 3; i++) {
	    _lightsGlare[i].setShow(false);
	    _lightsGlare[i].resetGame();
	    _sunGlare[i].setShow(false);
	    _sunGlare[i].resetGame();
	}
	Selector.resetGame();
	hud.resetGame();
	aircraftHotKeys.resetGame();
	bViewFly = false;
	bViewEnemy = false;
	ordersTree.resetGameClear();
	if (clouds != null) {
	    clouds.destroy();
	    clouds = null;
	}
	if (zip != null) {
	    zip.destroy();
	    zip = null;
	}
	sunFlareDestroy();
	if (Actor.isValid(spritesFog))
	    spritesFog.destroy();
	spritesFog = null;
	if (land2D != null) {
	    if (!land2D.isDestroyed())
		land2D.destroy();
	    land2D = null;
	}
	if (land2DText != null) {
	    if (!land2DText.isDestroyed())
		land2DText.destroy();
	    land2DText = null;
	}
	if (cockpits != null) {
	    for (int i = 0; i < cockpits.length; i++) {
		if (Actor.isValid(cockpits[i]))
		    cockpits[i].destroy();
		cockpits[i] = null;
	    }
	    cockpits = null;
	}
	cockpitCur = null;
	super.resetGameClear();
    }
    
    public void resetGameCreate() {
	super.resetGameCreate();
	Engine.soundListener().pos.setBase(camera3D, null, false);
	Engine.soundListener().setUseBaseSpeed(true);
    }
    
    public void resetUserClear() {
	World.cur().resetUser();
	aircraftHotKeys.resetUser();
	if (cockpits != null) {
	    for (int i = 0; i < cockpits.length; i++) {
		if (Actor.isValid(cockpits[i]))
		    cockpits[i].destroy();
		cockpits[i] = null;
	    }
	    cockpits = null;
	}
	cockpitCur = null;
	super.resetUserClear();
    }
    
    public void sunFlareCreate() {
	sunFlareDestroy();
	for (int i = 0; i < 3; i++)
	    _sunFlare[i] = new SunFlare(_sunFlareRender[i]);
    }
    
    public void sunFlareDestroy() {
	for (int i = 0; i < 3; i++) {
	    if (Actor.isValid(_sunFlare[i]))
		_sunFlare[i].destroy();
	    _sunFlare[i] = null;
	}
    }
    
    public void sunFlareShow(boolean bool) {
	for (int i = 0; i < 3; i++)
	    _sunFlareRender[i].setShow(bool);
    }
    
    public KeyRecordCallback playRecordedMissionCallback() {
	return playRecordedMissionCallback;
    }
    
    public InOutStreams playRecordedStreams() {
	return playRecordedStreams;
    }
    
    NetChannelInStream playRecordedNetChannelIn() {
	return playRecordedNetChannelIn;
    }
    
    public GameTrack gameTrackRecord() {
	return gameTrackRecord;
    }
    
    public void setGameTrackRecord(GameTrack gametrack) {
	gameTrackRecord = gametrack;
    }
    
    public GameTrack gameTrackPlay() {
	return gameTrackPlay;
    }
    
    public void setGameTrackPlay(GameTrack gametrack) {
	gameTrackPlay = gametrack;
    }
    
    public void clearGameTrack(GameTrack gametrack) {
	if (gametrack == gameTrackRecord)
	    gameTrackRecord = null;
	if (gametrack == gameTrackPlay)
	    gameTrackPlay = null;
    }
    
    public String playRecordedMission(String string) {
	playBatchCurRecord = -1;
	playEndBatch = true;
	playRecordedStreams = null;
	return playRecordedMission(string, true);
    }
    
    public String playRecordedMission(String string, boolean bool) {
	playRecordedFile = string;
	if (playRecordedMissionCallback == null)
	    playRecordedMissionCallback = new KeyRecordCallback() {
		public void playRecordedEnded() {
		    if (this == playRecordedMissionCallback) {
			GameState gamestate = Main.state();
			if (gamestate instanceof GUIRecordPlay) {
			    GUIRecordPlay guirecordplay
				= (GUIRecordPlay) gamestate;
			    guirecordplay.doReplayMission(playRecordedFile,
							  playEndBatch);
			} else if (gamestate instanceof GUITrainingPlay) {
			    GUITrainingPlay guitrainingplay
				= (GUITrainingPlay) gamestate;
			    guitrainingplay.doQuitMission();
			    guitrainingplay.doExit();
			} else if (gamestate instanceof GUIBWDemoPlay) {
			    GUIBWDemoPlay guibwdemoplay
				= (GUIBWDemoPlay) gamestate;
			    guibwdemoplay.doQuitMission();
			}
		    }
		}
		
		public void doFirstHotCmd(boolean bool_75_) {
		    if (playRecordedStreams != null) {
			if (aircraftHotKeys != null) {
			    /* empty */
			}
			AircraftHotKeys.bFirstHotCmd = bool_75_;
			loadRecordedStates1(bool_75_);
			if (!bool_75_)
			    loadRecordedStates2();
		    }
		}
	    };
	if (playRecordedStreams != null) {
	    try {
		playRecordedStreams.close();
	    } catch (Exception exception) {
		/* empty */
	    }
	    playRecordedStreams = null;
	    NetMissionTrack.stopPlaying();
	}
	if (playRecordedNetChannelIn != null)
	    playRecordedNetChannelIn.destroy();
	playRecordedNetChannelIn = null;
	if (InOutStreams.isExistAndValid(new File(string)))
	    return playNetRecordedMission(string, bool);
	String string_76_ = string;
	SectFile sectfile = new SectFile(string, 0, false);
	int i = sectfile.sectionIndex("batch");
	if (i >= 0) {
	    int i_77_ = sectfile.vars(i);
	    if (i_77_ <= 0)
		return "Track file '" + string + "' is empty";
	    playEndBatch
		= playBatchCurRecord != -1 && playBatchCurRecord == i_77_ - 2;
	    if (i_77_ == 1)
		playEndBatch = true;
	    playBatchCurRecord++;
	    if (playBatchCurRecord >= i_77_)
		playBatchCurRecord = 0;
	    string_76_ = "Records/" + sectfile.line(i, playBatchCurRecord);
	    if (InOutStreams.isExistAndValid(new File(string_76_)))
		return playNetRecordedMission(string_76_, bool);
	    sectfile = new SectFile(string_76_, 0, false);
	} else
	    playEndBatch = true;
	i = sectfile.sectionIndex("$$$record");
	if (i < 0)
	    return ("Track file '" + string_76_
		    + "' not included section [$$$record]");
	if (sectfile.vars(i) <= 12)
	    return "Track file '" + string_76_ + "' is empty";
	int i_78_ = Integer.parseInt(sectfile.var(i, 0));
	if (i_78_ != 132)
	    return "Track file '" + string_76_ + "' version is not supported";
	long l = Long.parseLong(sectfile.var(i, 1));
	float f = Float.parseFloat(sectfile.var(i, 2));
	float f_79_ = Float.parseFloat(sectfile.var(i, 3));
	float f_80_ = Float.parseFloat(sectfile.var(i, 4));
	float f_81_ = Float.parseFloat(sectfile.var(i, 5));
	float f_82_ = Float.parseFloat(sectfile.var(i, 6));
	int i_83_ = Integer.parseInt(sectfile.var(i, 7));
	int i_84_ = Integer.parseInt(sectfile.var(i, 8));
	int i_85_ = Integer.parseInt(sectfile.var(i, 9));
	long l_86_ = Long.parseLong(sectfile.var(i, 10));
	long l_87_ = Long.parseLong(sectfile.var(i, 11));
	long l_88_ = sectfile.fingerExcludeSectPrefix("$$$");
	l_88_ = Finger.incLong(l_88_, l);
	l_88_ = Finger.incLong(l_88_, (double) f);
	l_88_ = Finger.incLong(l_88_, (double) f_79_);
	l_88_ = Finger.incLong(l_88_, (double) f_80_);
	l_88_ = Finger.incLong(l_88_, (double) f_81_);
	l_88_ = Finger.incLong(l_88_, (double) f_82_);
	l_88_ = Finger.incLong(l_88_, i_83_);
	l_88_ = Finger.incLong(l_88_, i_84_);
	l_88_ = Finger.incLong(l_88_, i_85_);
	l_88_ = Finger.incLong(l_88_, l_86_);
	if (l_87_ != l_88_)
	    return "Track file '" + string_76_ + "' is changed";
	World.cur().diffCur.set(l);
	World.cur().diffCur.Cockpit_Always_On = false;
	World.cur().diffCur.No_Outside_Views = false;
	World.cur().diffCur.No_Padlock = false;
	World.cur().diffCur.NoAircraftViews = false;
	World.cur().diffCur.NoEnemyViews = false;
	World.cur().diffCur.NoFriendlyViews = false;
	World.cur().diffCur.NoOwnPlayerViews = false;
	World.cur().diffCur.NoSeaUnitViews = false;
	World.cur().userCoverMashineGun = f;
	World.cur().userCoverCannon = f_79_;
	World.cur().userCoverRocket = f_80_;
	World.cur().userRocketDelay = f_81_;
	World.cur().userBombDelay = f_82_;
	World.cur().userFuzeType = i_85_;
	FMMath.initSeed(l_86_);
	viewSet_Set(i_83_);
	iconTypes = i_84_;
	if (Main.cur().netServerParams == null) {
	    new NetServerParams();
	    Main.cur().netServerParams.setMode(2);
	    new NetLocalControl();
	}
	try {
	    Mission.loadFromSect(sectfile, true);
	} catch (Exception exception) {
	    System.out.println(exception.getMessage());
	    exception.printStackTrace();
	    return ("Track file '" + string_76_ + "' load failed: "
		    + exception.getMessage());
	}
	playRecordedSect = sectfile;
	playRecorderIndx = i;
	playRecordedPlayFile = string_76_;
	if (bool)
	    doRecordedPlayFirst();
	return null;
    }
    
    private String playNetRecordedMission(String string, boolean bool) {
        try {
            this.playRecordedStreams = new InOutStreams();
            this.playRecordedStreams.open(new File(string), false);

            InputStream localInputStream1 = this.playRecordedStreams.openStream("version");
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream1));
            int i = Integer.parseInt(localBufferedReader.readLine());
            int j = i;
            if (i >= 104)
              j = Integer.parseInt(localBufferedReader.readLine());
            localBufferedReader.close();

            if ((i != 100) && (i != 101) && (i != 102) && (i != 103) && (i != 104)) {
              try { this.playRecordedStreams.close(); } catch (Exception localException3) {
              }this.playRecordedStreams = null;
              return "Track file '" + string + "' version is not supported";
            }

            loadRecordedStates0(i);
            InputStream localInputStream2 = this.playRecordedStreams.openStream("traffic");
            if (localInputStream2 == null)
              throw new Exception("Stream 'traffic' not found.");
            this.playRecordedNetChannelIn = new NetChannelInStream(localInputStream2, 1);
            RTSConf.cur.netEnv.addChannel(this.playRecordedNetChannelIn);
            this.playRecordedNetChannelIn.setStateInit(0);
            this.playRecordedNetChannelIn.userState = 1;
            NetMissionTrack.startPlaying(this.playRecordedStreams, i, j);
            if (bool) doRecordedPlayFirst(); 
          }
          catch (Exception localException1) {
            localException1.printStackTrace();
            if (this.playRecordedStreams != null) try {
                this.playRecordedStreams.close(); } catch (Exception localException2) {
              } this.playRecordedStreams = null;
            return "Track file '" + string + "' load failed: " + localException1.getMessage();
          }
          return null;
    }
    
    private void doRecordedPlayFirst() {
	_disableCockpitsHotKeys();
	HotKeyEnv.enable("misc", false);
	HotKeyEnv.enable("orders", false);
	HotKeyEnv.enable("timeCompression", false);
	HotKeyEnv.enable("aircraftView", false);
	HotKeyEnv.enable("HookView", false);
	HotKeyEnv.enable("PanView", false);
	HotKeyEnv.enable("HeadMove", false);
	HotKeyEnv.enable("SnapView", false);
    }
    
    public String startPlayRecordedMission() {
	if (playRecordedStreams != null)
	    keyRecord.startPlay(playRecordedMissionCallback);
	else if (!keyRecord.startPlay(playRecordedSect, playRecorderIndx, 12,
				      playRecordedMissionCallback))
	    return "Track file '" + playRecordedPlayFile + "' load failed";
	return null;
    }
    
    public void stopPlayRecordedMission() {
	playRecordedSect = null;
	if (keyRecord.isPlaying())
	    keyRecord.stopPlay();
	if (playRecordedStreams != null) {
	    try {
		playRecordedStreams.close();
	    } catch (Exception exception) {
		/* empty */
	    }
	    playRecordedStreams = null;
	    NetMissionTrack.stopPlaying();
	}
	if (playRecordedNetChannelIn != null)
	    playRecordedNetChannelIn.destroy();
	playRecordedNetChannelIn = null;
	_disableCockpitsHotKeys();
	HotKeyEnv.enable("misc", true);
	HotKeyEnv.enable("orders", true);
	HotKeyEnv.enable("timeCompression", true);
	HotKeyEnv.enable("aircraftView", true);
	HotKeyEnv.enable("HookView", true);
	HotKeyEnv.enable("PanView", true);
	HotKeyEnv.enable("HeadMove", true);
	HotKeyEnv.enable("SnapView", true);
    }
    
    public void flyRecordedMission() {
	if (keyRecord.isPlaying()) {
	    keyRecord.stopPlay();
	    playRecordedMissionCallback = null;
	    if (Actor.isValid(cockpitCur))
		HotKeyCmd.exec("misc", "cockpitEnter" + cockpitCurIndx());
	    enableCockpitHotKeys();
	    HotKeyEnv.enable("misc", true);
	    HotKeyEnv.enable("orders", true);
	    HotKeyEnv.enable("timeCompression", true);
	    HotKeyEnv.enable("aircraftView", true);
	    HotKeyEnv.enable("HookView", true);
	    HotKeyEnv.enable("PanView", true);
	    HotKeyEnv.enable("HeadMove", true);
	    HotKeyEnv.enable("SnapView", true);
	    ForceFeedback.startMission();
	}
    }
    
    public boolean saveRecordedMission(String string) {
	if (mission == null)
	    return false;
	if (mission.isDestroyed())
	    return false;
	if (!keyRecord.isContainRecorded())
	    return false;
	boolean bool;
	try {
	    SectFile sectfile = mission.sectFile();
	    int i = sectfile.sectionIndex("$$$record");
	    if (i >= 0)
		sectfile.sectionClear(i);
	    else
		i = sectfile.sectionAdd("$$$record");
	    sectfile.lineAdd(i, "132", "");
	    long l = Finger.incLong(mission.finger(), 132);
	    long l_96_ = World.cur().diffCur.get();
	    sectfile.lineAdd(i, "" + l_96_, "");
	    l = Finger.incLong(mission.finger(), l_96_);
	    sectfile.lineAdd(i, "" + World.cur().userCoverMashineGun, "");
	    l = Finger.incLong(l, (double) World.cur().userCoverMashineGun);
	    sectfile.lineAdd(i, "" + World.cur().userCoverCannon, "");
	    l = Finger.incLong(l, (double) World.cur().userCoverCannon);
	    sectfile.lineAdd(i, "" + World.cur().userCoverRocket, "");
	    l = Finger.incLong(l, (double) World.cur().userCoverRocket);
	    sectfile.lineAdd(i, "" + World.cur().userRocketDelay, "");
	    l = Finger.incLong(l, (double) World.cur().userRocketDelay);
	    sectfile.lineAdd(i, "" + World.cur().userBombDelay, "");
	    l = Finger.incLong(l, (double) World.cur().userBombDelay);
	    sectfile.lineAdd(i, "" + Mission.viewSet, "");
	    l = Finger.incLong(l, Mission.viewSet);
	    sectfile.lineAdd(i, "" + Mission.iconTypes, "");
	    l = Finger.incLong(l, Mission.iconTypes);
	    sectfile.lineAdd(i, "" + World.cur().userFuzeType, "");
	    l = Finger.incLong(l, World.cur().userFuzeType);
	    sectfile.lineAdd(i, "" + FMMath.getSeed(), "");
	    l = Finger.incLong(l, FMMath.getSeed());
	    sectfile.lineAdd(i, "" + l, "");
	    keyRecord.saveRecorded(sectfile, i);
	    bool = sectfile.saveFile(string);
	} catch (Exception exception) {
	    return false;
	}
	return bool;
    }
    
    public boolean saveRecordedStates0(InOutStreams inoutstreams) {
	boolean bool;
	try {
	    PrintWriter printwriter
		= new PrintWriter(inoutstreams.createStream("states0"));
	    printwriter.println(World.cur().diffCur.get());
	    printwriter.println(World.cur().userCoverMashineGun);
	    printwriter.println(World.cur().userCoverCannon);
	    printwriter.println(World.cur().userCoverRocket);
	    printwriter.println(World.cur().userRocketDelay);
	    printwriter.println(World.cur().userBombDelay);
	    printwriter.println(viewSet_Get());
	    printwriter.println(iconTypes);
	    printwriter.println(World.cur().userFuzeType);
	    printwriter.println(isViewOutside() ? "0" : "1");
	    printwriter.println(FOVX);
	    printwriter.flush();
	    printwriter.close();
	    bool = true;
	} catch (Exception exception) {
	    System.out.println(exception.getMessage());
	    exception.printStackTrace();
	    return false;
	}
	return bool;
    }
    
    public boolean saveRecordedStates1(InOutStreams inoutstreams) {
	boolean bool;
	try {
	    PrintWriter printwriter
		= new PrintWriter(inoutstreams.createStream("states1"));
	    HookView.cur().saveRecordedStates(printwriter);
	    HookPilot.cur().saveRecordedStates(printwriter);
	    HookGunner.saveRecordedStates(printwriter);
	    printwriter.flush();
	    printwriter.close();
	    bool = true;
	} catch (Exception exception) {
	    System.out.println(exception.getMessage());
	    exception.printStackTrace();
	    return false;
	}
	return bool;
    }
    
    public boolean saveRecordedStates2(InOutStreams inoutstreams) {
	boolean bool;
	try {
	    PrintWriter printwriter
		= new PrintWriter(inoutstreams.createStream("states2"));
	    printwriter.println(FOVX);
	    int i = 0;
	    if (hud.bDrawDashBoard)
		i |= 0x1;
	    if (isViewInsideShow())
		i |= 0x2;
	    if (Actor.isValid(cockpitCur) && cockpitCur.isToggleAim())
		i |= 0x4;
	    FlightModel flightmodel = World.getPlayerFM();
	    if (flightmodel != null && flightmodel.AS.bShowSmokesOn)
		i |= 0x8;
	    if (isEnableRenderingCockpit())
		i |= 0x10;
	    if (Actor.isValid(cockpitCur) && cockpitCur.isToggleUp())
		i |= 0x20;
	    if (Actor.isValid(cockpitCur) && cockpitCur.isToggleDim())
		i |= 0x40;
	    if (Actor.isValid(cockpitCur) && cockpitCur.isToggleLight())
		i |= 0x80;
	    if (Actor.isValid(cockpitCur)
		&& !cockpitCur.isEnableRenderingBall())
		i |= 0x100;
	    printwriter.println(i);
	    printwriter.flush();
	    printwriter.close();
	    bool = true;
	} catch (Exception exception) {
	    System.out.println(exception.getMessage());
	    exception.printStackTrace();
	    return false;
	}
	return bool;
    }
    
    public void loadRecordedStates0(int i) {
	try {
	    InputStream inputstream
		= playRecordedStreams.openStream("states0");
	    BufferedReader bufferedreader
		= new BufferedReader(new InputStreamReader(inputstream));
	    World.cur().diffCur.set(Long.parseLong(bufferedreader.readLine()));
	    World.cur().diffCur.Cockpit_Always_On = false;
	    World.cur().diffCur.No_Outside_Views = false;
	    World.cur().diffCur.No_Padlock = false;
	    World.cur().diffCur.NoAircraftViews = false;
	    World.cur().diffCur.NoEnemyViews = false;
	    World.cur().diffCur.NoFriendlyViews = false;
	    World.cur().diffCur.NoOwnPlayerViews = false;
	    World.cur().diffCur.NoSeaUnitViews = false;
	    World.cur().userCoverMashineGun
		= Float.parseFloat(bufferedreader.readLine());
	    World.cur().userCoverCannon
		= Float.parseFloat(bufferedreader.readLine());
	    World.cur().userCoverRocket
		= Float.parseFloat(bufferedreader.readLine());
	    World.cur().userRocketDelay
		= Float.parseFloat(bufferedreader.readLine());
	    World.cur().userBombDelay
		= Float.parseFloat(bufferedreader.readLine());
	    viewSet_Set(Integer.parseInt(bufferedreader.readLine()));
	    iconTypes = Integer.parseInt(bufferedreader.readLine());
	    if (i >= 104)
		World.cur().userFuzeType
		    = Integer.parseInt(bufferedreader.readLine());
	    bLoadRecordedStates1Before
		= Integer.parseInt(bufferedreader.readLine()) == 1;
	    float f = Float.parseFloat(bufferedreader.readLine());
	    if (f != FOVX)
		CmdEnv.top().exec("fov " + f);
	    inputstream.close();
	} catch (Exception exception) {
	    System.out.println(exception.getMessage());
	    exception.printStackTrace();
	}
    }
    
    public void loadRecordedStates1(boolean bool) {
	if (bool == bLoadRecordedStates1Before) {
	    try {
		InputStream inputstream
		    = playRecordedStreams.openStream("states1");
		BufferedReader bufferedreader
		    = new BufferedReader(new InputStreamReader(inputstream));
		HookView.cur().loadRecordedStates(bufferedreader);
		HookPilot.cur().loadRecordedStates(bufferedreader);
		HookGunner.loadRecordedStates(bufferedreader);
		inputstream.close();
	    } catch (Exception exception) {
		System.out.println(exception.getMessage());
		exception.printStackTrace();
	    }
	}
    }
    
    public void loadRecordedStates2() {
	try {
	    InputStream inputstream
		= playRecordedStreams.openStream("states2");
	    BufferedReader bufferedreader
		= new BufferedReader(new InputStreamReader(inputstream));
	    float f = Float.parseFloat(bufferedreader.readLine());
	    if (f != FOVX)
		CmdEnv.top().exec("fov " + f);
	    int i = Integer.parseInt(bufferedreader.readLine());
	    hud.bDrawDashBoard = (i & 0x1) != 0;
	    setViewInsideShow((i & 0x2) != 0);
	    if (Actor.isValid(cockpitCur))
		cockpitCur.doToggleAim((i & 0x4) != 0);
	    FlightModel flightmodel = World.getPlayerFM();
	    if (flightmodel != null)
		flightmodel.AS.setAirShowState((i & 0x8) != 0);
	    if (Actor.isValid(cockpitCur)) {
		setEnableRenderingCockpit((i & 0x10) != 0);
		cockpitCur.doToggleUp((i & 0x20) != 0);
		if ((i & 0x40) != 0 && !cockpitCur.isToggleDim())
		    cockpitCur.doToggleDim();
		if ((i & 0x80) != 0 && !cockpitCur.isToggleLight())
		    cockpitCur.doToggleLight();
		cockpitCur.setEnableRenderingBall((i & 0x100) == 0);
	    }
	    inputstream.close();
	} catch (Exception exception) {
	    System.out.println(exception.getMessage());
	    exception.printStackTrace();
	}
    }
    
    public void setRenderIndx(int i) {
	iRenderIndx = i;
    }
    
    public int getRenderIndx() {
	return iRenderIndx;
    }
    
    private void transform_point(double[] ds, double[] ds_97_,
				 double[] ds_98_) {
	ds[0] = (ds_97_[0] * ds_98_[0] + ds_97_[4] * ds_98_[1]
		 + ds_97_[8] * ds_98_[2] + ds_97_[12] * ds_98_[3]);
	ds[1] = (ds_97_[1] * ds_98_[0] + ds_97_[5] * ds_98_[1]
		 + ds_97_[9] * ds_98_[2] + ds_97_[13] * ds_98_[3]);
	ds[2] = (ds_97_[2] * ds_98_[0] + ds_97_[6] * ds_98_[1]
		 + ds_97_[10] * ds_98_[2] + ds_97_[14] * ds_98_[3]);
	ds[3] = (ds_97_[3] * ds_98_[0] + ds_97_[7] * ds_98_[1]
		 + ds_97_[11] * ds_98_[2] + ds_97_[15] * ds_98_[3]);
    }
    
    public boolean project2d(double d, double d_99_, double d_100_,
			     Point3d point3d) {
	_dIn[0] = d;
	_dIn[1] = d_99_;
	_dIn[2] = d_100_;
	_dIn[3] = 1.0;
	if (bRenderMirror) {
	    transform_point(_dOut, _modelMatrix3DMirror, _dIn);
	    transform_point(_dIn, _projMatrix3DMirror, _dOut);
	} else {
	    transform_point(_dOut, _modelMatrix3D[iRenderIndx], _dIn);
	    transform_point(_dIn, _projMatrix3D[iRenderIndx], _dOut);
	}
	if (_dIn[3] == 0.0) {
	    System.out
		.println("BAD glu.Project: " + d + " " + d_99_ + " " + d_100_);
	    return false;
	}
	_dIn[0] /= _dIn[3];
	_dIn[1] /= _dIn[3];
	_dIn[2] /= _dIn[3];
	if (bRenderMirror) {
	    point3d.x
		= ((double) _viewportMirror[0]
		   + (1.0 + _dIn[0]) * (double) _viewportMirror[2] / 2.0);
	    point3d.y
		= ((double) _viewportMirror[1]
		   + (1.0 + _dIn[1]) * (double) _viewportMirror[3] / 2.0);
	} else {
	    point3d.x = ((double) _viewport[iRenderIndx][0]
			 + ((1.0 + _dIn[0])
			    * (double) _viewport[iRenderIndx][2] / 2.0));
	    point3d.y = ((double) _viewport[iRenderIndx][1]
			 + ((1.0 + _dIn[1])
			    * (double) _viewport[iRenderIndx][3] / 2.0));
	}
	point3d.z = (1.0 + _dIn[2]) / 2.0;
	return true;
    }
    
    public boolean project2d_cam(double d, double d_101_, double d_102_,
				 Point3d point3d) {
	if (!project2d(d, d_101_, d_102_, point3d))
	    return false;
	if (bRenderMirror) {
	    point3d.x -= (double) _viewportMirror[0];
	    point3d.y -= (double) _viewportMirror[1];
	} else {
	    point3d.x -= (double) _viewport[iRenderIndx][0];
	    point3d.y -= (double) _viewport[iRenderIndx][1];
	}
	return true;
    }
    
    public boolean project2d_norm(double d, double d_103_, double d_104_,
				  Point3d point3d) {
	_dIn[0] = d;
	_dIn[1] = d_103_;
	_dIn[2] = d_104_;
	_dIn[3] = 1.0;
	if (bRenderMirror) {
	    transform_point(_dOut, _modelMatrix3DMirror, _dIn);
	    transform_point(_dIn, _projMatrix3DMirror, _dOut);
	} else {
	    transform_point(_dOut, _modelMatrix3D[iRenderIndx], _dIn);
	    transform_point(_dIn, _projMatrix3D[iRenderIndx], _dOut);
	}
	if (_dIn[3] == 0.0) {
	    System.out.println("BAD glu.Project2: " + d + " " + d_103_ + " "
			       + d_104_);
	    return false;
	}
	double d_105_ = 1.0 / _dIn[3];
	point3d.x = _dIn[0] * d_105_;
	point3d.y = _dIn[1] * d_105_;
	point3d.z = _dIn[2] * d_105_;
	return true;
    }
    
    public boolean project2d(Point3d point3d, Point3d point3d_106_) {
	return project2d(point3d.x, point3d.y, point3d.z, point3d_106_);
    }
    
    public boolean project2d_cam(Point3d point3d, Point3d point3d_107_) {
	return project2d_cam(point3d.x, point3d.y, point3d.z, point3d_107_);
    }
    
    private void shadowPairsClear() {
	shadowPairsList1.clear();
	shadowPairsMap1.clear();
	shadowPairsList2.clear();
	shadowPairsCur1 = null;
    }
    
    private void shadowPairsAdd(ArrayList arraylist) {
	int i = arraylist.size();
	for (int i_108_ = 0; i_108_ < i; i_108_++) {
	    Object object = arraylist.get(i_108_);
	    if (object instanceof BigshipGeneric
		&& !(object instanceof TransparentTestRunway)) {
		BigshipGeneric bigshipgeneric = (BigshipGeneric) object;
		if (Actor.isValid(bigshipgeneric.getAirport())
		    && !shadowPairsMap1.containsKey(bigshipgeneric)) {
		    shadowPairsList1.add(bigshipgeneric);
		    shadowPairsMap1.put(bigshipgeneric, null);
		}
	    }
	}
    }
    
    private void shadowPairsRender() {
	int i = shadowPairsList1.size();
	if (i != 0) {
	    for (int i_109_ = 0; i_109_ < i; i_109_++) {
		shadowPairsCur1
		    = (BigshipGeneric) shadowPairsList1.get(i_109_);
		Point3d point3d = shadowPairsCur1.pos.getAbsPoint();
		double d = point3d.x - shadowPairsR;
		double d_110_ = point3d.y - shadowPairsR;
		double d_111_ = point3d.x + shadowPairsR;
		double d_112_ = point3d.y + shadowPairsR;
		Engine.drawEnv().getFiltered((AbstractCollection) null, d,
					     d_110_, d_111_, d_112_, 14,
					     shadowPairsFilter);
	    }
	    if (shadowPairsList2.size() != 0)
		HierMesh.renderShadowPairs(shadowPairsList2);
	}
    }
    
    private void doPreRender3D(Render render) {
	render.useClearColor(!bDrawLand
			     || (RenderContext.texGetFlags() & 0x20) != 0);
	render.getCamera().pos.getRender(__p, __o);
	if (!bRenderMirror && iRenderIndx == 0) {
	    SearchlightGeneric.lightPlanesBySearchlights();
	    Actor actor = render.getCamera().pos.base();
	    if (Actor.isValid(actor)) {
		actor.getSpeed(__v);
		Camera.SetTargetSpeed((float) __v.x, (float) __v.y,
				      (float) __v.z);
	    } else
		Camera.SetTargetSpeed(0.0F, 0.0F, 0.0F);
	}
	Render.enableFog(bEnableFog);
	if (bDrawClouds && clouds != null)
	    clouds.preRender();
	if (bDrawLand)
	    Engine.land().preRender((float) __p.z, false);
	DrwArray drwarray = bRenderMirror ? drwMirror : drwMaster[iRenderIndx];
	com.maddox.il2.engine.DrawEnv drawenv = Engine.drawEnv();
	double d = __p.x;
	double d_113_ = __p.y;
	double d_114_ = __p.z;
	if (Engine.cur.world != null) {
	    /* empty */
	}
	drawenv.preRender(d, d_113_, d_114_, World.MaxVisualDistance, 4,
			  drwarray.drwSolid, drwarray.drwTransp,
			  drwarray.drwShadow, true);
	com.maddox.il2.engine.DrawEnv drawenv_115_ = Engine.drawEnv();
	double d_116_ = __p.x;
	double d_117_ = __p.y;
	double d_118_ = __p.z;
	if (Engine.cur.world != null) {
	    /* empty */
	}
	drawenv_115_.preRender(d_116_, d_117_, d_118_,
			       World.MaxLongVisualDistance, 8,
			       drwarray.drwSolid, drwarray.drwTransp,
			       drwarray.drwShadow, false);
	if (!bRenderMirror) {
	    shadowPairsAdd(drwarray.drwSolid);
	    shadowPairsAdd(drwarray.drwTransp);
	}
	if (!bRenderMirror || viewMirror > 1) {
	    com.maddox.il2.engine.DrawEnv drawenv_119_ = Engine.drawEnv();
	    double d_120_ = __p.x;
	    double d_121_ = __p.y;
	    double d_122_ = __p.z;
	    if (Engine.cur.world != null) {
		/* empty */
	    }
	    drawenv_119_.preRender(d_120_, d_121_, d_122_,
				   World.MaxStaticVisualDistance, 2,
				   drwarray.drwSolid, drwarray.drwTransp,
				   drwarray.drwShadow, false);
	    com.maddox.il2.engine.DrawEnv drawenv_123_ = Engine.drawEnv();
	    double d_124_ = __p.x;
	    double d_125_ = __p.y;
	    double d_126_ = __p.z;
	    if (Engine.cur.world != null) {
		/* empty */
	    }
	    drawenv_123_.preRender(d_124_, d_125_, d_126_,
				   World.MaxPlateVisualDistance, 1,
				   drwarray.drwSolidPlate,
				   drwarray.drwTranspPlate,
				   drwarray.drwShadowPlate, true);
	}
	BulletGeneric.preRenderAll();
	if (bEnableFog)
	    Render.enableFog(false);
    }
    
    private void doRender3D0(Render render) {
	boolean bool = false;
	Render.enableFog(bEnableFog);
	if (bDrawLand) {
	    Engine.lightEnv().prepareForRender(camera3D.pos.getAbsPoint(),
					       8000.0F);
	    bool = Engine.land().render0(bRenderMirror) != 2;
	    LightPoint.clearRender();
	}
	if (bool && bEnableFog)
	    Render.enableFog(false);
	DrwArray drwarray = bRenderMirror ? drwMirror : drwMaster[iRenderIndx];
	plateToRenderArray(drwarray.drwSolidPlate, drwarray.drwSolid);
	plateToRenderArray(drwarray.drwTranspPlate, drwarray.drwTransp);
	plateToRenderArray(drwarray.drwShadowPlate, drwarray.drwShadow);
	MeshShared.renderArray(true);
	render.drawShadow(drwarray.drwShadow);
	if (bool && bEnableFog)
	    Render.enableFog(true);
	if (bDrawLand)
	    Engine.land().render1(bRenderMirror);
	int i = gl.GetError();
	if (i != 0)
	    System.out.println("***( GL error: " + i + " (render3d0)");
    }
    
    private void doRender3D1(Render render) {
	if (bDrawClouds && clouds != null && RenderContext.cfgSky.get() > 0) {
	    Engine.lightEnv().prepareForRender(camera3D.pos.getAbsPoint(),
					       (float) RenderContext.cfgSky
							   .get() * 4000.0F);
	    SearchlightGeneric.lightCloudsBySearchlights();
	    clouds.render();
	    LightPoint.clearRender();
	}
	DrwArray drwarray = bRenderMirror ? drwMirror : drwMaster[iRenderIndx];
	render.draw(drwarray.drwSolid, drwarray.drwTransp);
	if (!bRenderMirror)
	    shadowPairsRender();
	BulletGeneric.renderAll();
	if (bEnableFog) {
	    Render.flush();
	    Render.enableFog(false);
	}
    }
    
    private void plateToRenderArray(ArrayList arraylist,
				    ArrayList arraylist_127_) {
	int i = arraylist.size();
	for (int i_128_ = 0; i_128_ < i; i_128_++) {
	    Actor actor = (Actor) arraylist.get(i_128_);
	    if (actor instanceof ActorLandMesh)
		arraylist_127_.add(actor);
	    else if (actor instanceof ActorMesh
		     && ((ActorMesh) actor).mesh() instanceof MeshShared) {
		actor.pos.getRender(__l);
		if (!((MeshShared) ((ActorMesh) actor).mesh())
			 .putToRenderArray(__l))
		    actor.draw.render(actor);
	    } else
		actor.draw.render(actor);
	}
	arraylist.clear();
    }
    
    public void _getAspectViewPort(int i, float[] fs) {
	fs[0] = i == 1 ? 0.0F : 0.6666667F;
	fs[1] = 0.0F;
	fs[2] = 0.33333334F;
	fs[3] = 1.0F;
    }
    
    public void _getAspectViewPort(int i, int[] is) {
	is[0] = i == 1 ? 0 : 2 * RendersMain.width() / 3;
	is[1] = 0;
	is[2] = RendersMain.width() / 3;
	is[3] = RendersMain.height();
    }
    
    private void drawTime() {
	if (hud.bDrawAllMessages) {
	    if (bShowTime || isDemoPlaying()) {
		int i = TextScr.This().getViewPortWidth();
		long l = Time.current();
		if (NetMissionTrack.isPlaying())
		    l -= NetMissionTrack.playingStartTime;
		int i_129_ = (int) (l / 1000L % 60L);
		int i_130_ = (int) (l / 1000L / 60L);
		if (i_129_ > 9)
		    TextScr.output(i - TextScr.font().height() * 3, 5,
				   "" + i_130_ + ":" + i_129_);
		else
		    TextScr.output(i - TextScr.font().height() * 3, 5,
				   "" + i_130_ + ":0" + i_129_);
	    }
	}
    }
    
    private void drawTimeRP() {
	if (hud.bRec && NetMissionTrack.isRecording()) {
	    int i = TextScr.This().getViewPortWidth();
	    long l = Time.current();
	    l -= NetMissionTrack.recStartTime;
	    int i_131_ = (int) (l / 1000L % 60L);
	    int i_132_ = (int) (l / 1000L / 60L);
	    if (NetMissionTrack.isPlaying()) {
		if (i_131_ > 9)
		    TextScr.output(i - TextScr.font().height() * 10, 5,
				   "REC " + i_132_ + ":" + i_131_);
		else
		    TextScr.output(i - TextScr.font().height() * 10, 5,
				   "REC " + i_132_ + ":0" + i_131_);
	    } else if (i_131_ > 9)
		TextScr.output(i - TextScr.font().height() * 6, 5,
			       "REC " + i_132_ + ":" + i_131_);
	    else
		TextScr.output(i - TextScr.font().height() * 6, 5,
			       "REC " + i_132_ + ":0" + i_131_);
	}
    }
    
    public int mirrorX0() {
	return render3D0.getViewPortX0();
    }
    
    public int mirrorY0() {
	return 0;
    }
    
    public int mirrorWidth() {
	return 256;
    }
    
    public int mirrorHeight() {
	return 256;
    }
    
    public void preRenderHUD() {
	/* empty */
    }
    
    public void renderHUD() {
	/* empty */
    }
    
    public void renderHUDcontextResize(int i, int i_133_) {
	/* empty */
    }
    
    public void preRenderMap2D() {
	/* empty */
    }
    
    public void renderMap2D() {
	/* empty */
    }
    
    public void renderMap2DcontextResize(int i, int i_134_) {
	/* empty */
    }
    
    private void insertFarActorItem(int i, int i_135_, int i_136_, float f,
				    String string) {
	int i_137_ = (int) iconFarFont.width(string);
	for (int i_138_ = 0; i_138_ < iconFarListLen[iRenderIndx]; i_138_++) {
	    FarActorItem faractoritem
		= (FarActorItem) iconFarList[iRenderIndx].get(i_138_);
	    if (f > faractoritem.z) {
		if (iconFarList[iRenderIndx].size()
		    == iconFarListLen[iRenderIndx]) {
		    faractoritem = new FarActorItem(i, i_135_, i_136_, i_137_,
						    f, string);
		    iconFarList[iRenderIndx].add(faractoritem);
		} else {
		    faractoritem
			= (FarActorItem) iconFarList[iRenderIndx]
					     .get(iconFarListLen[iRenderIndx]);
		    faractoritem.set(i, i_135_, i_136_, i_137_, f, string);
		    iconFarList[iRenderIndx]
			.remove(iconFarListLen[iRenderIndx]);
		    iconFarList[iRenderIndx].add(i_138_, faractoritem);
		}
		iconFarListLen[iRenderIndx]++;
		return;
	    }
	}
	if (iconFarList[iRenderIndx].size() == iconFarListLen[iRenderIndx]) {
	    FarActorItem faractoritem
		= new FarActorItem(i, i_135_, i_136_, i_137_, f, string);
	    iconFarList[iRenderIndx].add(faractoritem);
	} else {
	    FarActorItem faractoritem
		= ((FarActorItem)
		   iconFarList[iRenderIndx].get(iconFarListLen[iRenderIndx]));
	    faractoritem.set(i, i_135_, i_136_, i_137_, f, string);
	}
	iconFarListLen[iRenderIndx]++;
    }
    
    private void clipFarActorItems() {
	int i = iconFarFont.height();
	for (int i_139_ = 0; i_139_ < iconFarListLen[iRenderIndx]; i_139_++) {
	    FarActorItem faractoritem
		= (FarActorItem) iconFarList[iRenderIndx].get(i_139_);
	    for (int i_140_ = i_139_ + 1; i_140_ < iconFarListLen[iRenderIndx];
		 i_140_++) {
		FarActorItem faractoritem_141_
		    = (FarActorItem) iconFarList[iRenderIndx].get(i_140_);
		if ((faractoritem_141_.x + faractoritem_141_.dx
		     >= faractoritem.x)
		    && faractoritem_141_.x <= faractoritem.x + faractoritem.dx
		    && faractoritem_141_.y + i >= faractoritem.y
		    && faractoritem_141_.y <= faractoritem.y + i) {
		    iconFarList[iRenderIndx].remove(i_140_);
		    iconFarList[iRenderIndx].add(faractoritem_141_);
		    i_140_--;
		    iconFarListLen[iRenderIndx]--;
		}
	    }
	}
    }
    
    private void clearFarActorItems() {
	iconFarListLen[iRenderIndx] = 0;
    }
    
    private boolean isBomb(Actor actor) {
	return actor instanceof Bomb || actor instanceof Rocket;
    }
    
    protected void drawFarActors() {
	if ((Main.state() == null || Main.state().id() != 18)
	    && iconFarMat != null) {
	    iconFarFontHeight = (float) iconFarFont.height();
	    iconClipX0 = -2.0;
	    iconClipY0 = -1.0;
	    if (bRenderMirror) {
		iconClipX1
		    = (double) ((float) render2DMirror.getViewPortWidth()
				+ 2.0F);
		iconClipY1
		    = (double) ((float) render2DMirror.getViewPortHeight()
				+ 1.0F);
	    } else {
		iconClipX1 = (double) ((float) _render2D[iRenderIndx]
						   .getViewPortWidth()
				       + 2.0F);
		iconClipY1 = (double) ((float) _render2D[iRenderIndx]
						   .getViewPortHeight()
				       + 1.0F);
	    }
	    iconFarPlayerActor = World.getPlayerAircraft();
	    iconFarViewActor = viewActor();
	    iconFarPadlockItem.str = null;
	    iconFarPadlockActor = getViewPadlockEnemy();
	    _camera3D[iRenderIndx].pos.getRender(farActorFilter.camp);
	    Point3d point3d = farActorFilter.camp;
	    float f = Engine.lightEnv().sun().ToLight.z;
	    if (f < 0.0F)
		f = 0.0F;
	    float f_142_
		= (Engine.lightEnv().sun().Ambient
		   + Engine.lightEnv().sun().Diffuze * (0.25F + 0.4F * f));
	    if (RenderContext.cfgHardwareShaders.get() > 0) {
		f = (Engine.lightEnv().sun().Ambient
		     + f * Engine.lightEnv().sun().Diffuze);
		if (f > 1.0F)
		    f_142_ *= f;
	    }
	    int i = (int) (127.0F * f_142_);
	    if (i > 255)
		i = 255;
	    iconFarColor = i | i << 8 | i << 16;
	    List list = Engine.targets();
	    int i_143_ = list.size();
	    for (int i_144_ = 0; i_144_ < i_143_; i_144_++) {
		Actor actor = (Actor) list.get(i_144_);
		Point3d point3d_145_ = actor.pos.getAbsPoint();
		double d = point3d.distance(point3d_145_);
		if (d < 25000.0)
		    farActorFilter.isUse(actor, d);
	    }
	    iconFarPlayerActor = null;
	    iconFarViewActor = null;
	    iconFarPadlockActor = null;
	    if (iconFarListLen[iRenderIndx] != 0) {
		clipFarActorItems();
		for (int i_146_ = 0; i_146_ < iconFarListLen[iRenderIndx];
		     i_146_++) {
		    FarActorItem faractoritem
			= (FarActorItem) iconFarList[iRenderIndx].get(i_146_);
		    if (bRenderMirror) {
			transformMirror.set((float) (faractoritem.x
						     - faractoritem.dx),
					    (float) faractoritem.y,
					    faractoritem.z,
					    (float) faractoritem.dx);
			iconFarFont.transform(transformMirror,
					      faractoritem.color,
					      faractoritem.str);
		    } else
			iconFarFont.output(faractoritem.color,
					   (float) faractoritem.x,
					   (float) faractoritem.y,
					   faractoritem.z, faractoritem.str);
		}
	    }
	    if (iconFarPadlockItem.str != null) {
		if (!iconFarPadlockItem.bGround) {
		    iconFarPadlockItem.x += 1.0F;
		    iconFarPadlockItem.y += -7.5F;
		    f = 16.0F;
		    line3XYZ[0] = (float) ((double) iconFarPadlockItem.x
					   - (double) f * 0.866);
		    line3XYZ[1] = (float) ((double) iconFarPadlockItem.y
					   - (double) f * 0.5);
		    line3XYZ[2] = iconFarPadlockItem.z;
		    line3XYZ[3] = (float) ((double) iconFarPadlockItem.x
					   + (double) f * 0.866);
		    line3XYZ[4] = (float) ((double) iconFarPadlockItem.y
					   - (double) f * 0.5);
		    line3XYZ[5] = iconFarPadlockItem.z;
		    line3XYZ[6] = (float) iconFarPadlockItem.x;
		    line3XYZ[7] = (float) iconFarPadlockItem.y + f;
		    line3XYZ[8] = iconFarPadlockItem.z;
		} else {
		    camera3D.pos.getRender(_lineP, _lineO);
		    double d = ((double) -_lineO.getKren() * 3.141592653589793
				/ 180.0);
		    double d_147_ = Math.sin(d);
		    double d_148_ = Math.cos(d);
		    iconFarPadlockItem.x += 1.0F;
		    iconFarPadlockItem.y += -7.5F;
		    float f_149_ = 16.0F;
		    line3XYZ[0] = (float) iconFarPadlockItem.x;
		    line3XYZ[1] = (float) iconFarPadlockItem.y;
		    line3XYZ[2] = iconFarPadlockItem.z;
		    line3XYZ[3] = (float) ((double) iconFarPadlockItem.x
					   + d_148_ * (double) f_149_ * 0.25
					   + d_147_ * 1.5 * (double) f_149_);
		    line3XYZ[4] = (float) ((double) iconFarPadlockItem.y
					   - d_147_ * (double) f_149_ * 0.25
					   + d_148_ * 1.5 * (double) f_149_);
		    line3XYZ[5] = iconFarPadlockItem.z;
		    line3XYZ[6] = (float) ((double) iconFarPadlockItem.x
					   - d_148_ * (double) f_149_ * 0.25
					   + d_147_ * 1.5 * (double) f_149_);
		    line3XYZ[7] = (float) ((double) iconFarPadlockItem.y
					   + d_147_ * (double) f_149_ * 0.25
					   + d_148_ * 1.5 * (double) f_149_);
		    line3XYZ[8] = iconFarPadlockItem.z;
		}
		Render.drawBeginLines(-1);
		Render.drawLines(line3XYZ, 3, 1.0F, iconFarPadlockItem.color,
				 (Mat.TESTZ | Mat.MODULATE | Mat.NOTEXTURE
				  | Mat.BLEND),
				 5);
		Render.drawEnd();
	    }
	    clearFarActorItems();
	}
    }
    
    protected void drawFarActorsInit() {
	iconFarMat = Mat.New("icons/faractor.mat");
	iconFarFont = TTFont.get("arialSmallZ");
	iconFarFinger = Finger.Int("iconFar_shortClassName");
    }
    
    public void initHotKeys() {
	CmdEnv.top().setCommand(new CmdExit(), "exit", "exit game");
	HotKeyCmdEnv.setCurrentEnv("hotkeys");
	HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "exit") {
	    public void begin() {
		Main.doGameExit();
	    }
	});
	HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ScreenShot") {
	    public void begin() {
		if (scrShot == null)
		    scrShot = new ScrShot("grab");
		if (Mission.isNet()) {
		    long l = Time.real();
		    if (lastTimeScreenShot + 10000L < l)
			lastTimeScreenShot = l;
		    else
			return;
		}
		scrShot.grab();
	    }
	});
	CmdEnv.top().setCommand(new CmdScreenSequence(), "avi",
				"start/stop save screen shot sequence");
	HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ScreenSequence") {
	    public void begin() {
		if (screenSequence == null)
		    screenSequence = new ScreenSequence();
		screenSequence.doSave();
	    }
	});
	HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "land") {
	    public void begin() {
		if (RTSConf.cur.console.getEnv().levelAccess() == 0)
		    setDrawLand(!isDrawLand());
	    }
	});
	HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "clouds") {
	    public void begin() {
		if (Mission.isSingle()
		    && RTSConf.cur.console.getEnv().levelAccess() == 0)
		    bDrawClouds = !bDrawClouds;
	    }
	});
	HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "showTime") {
	    public void begin() {
		if (RTSConf.cur.console.getEnv().levelAccess() == 0)
		    bShowTime = !bShowTime;
	    }
	});
	HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "pause") {
	    public void begin() {
		if (!TimeSkip.isDo()) {
		    if (Time.isEnableChangePause()) {
			Time.setPause(!Time.isPaused());
			if (Config.cur.isSoundUse()) {
			    if (Time.isPaused())
				AudioDevice.soundsOff();
			    else
				AudioDevice.soundsOn();
			}
		    }
		}
	    }
	});
    }
}
