/* 410 class + CTO Mod + GATTACK Mod by CY6 */
package com.maddox.il2.ai.air;

import com.maddox.JGP.Point2f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector2d;
import com.maddox.JGP.Vector2f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.AirportCarrier;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiSupportMethods_AI;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.TextScr;
import com.maddox.il2.fm.AIFlightModel;
import com.maddox.il2.fm.FMMath;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.objects.air.AR_234;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.BI_1;
import com.maddox.il2.objects.air.BI_6;
import com.maddox.il2.objects.air.HE_LERCHE3;
import com.maddox.il2.objects.air.JU_88NEW;
import com.maddox.il2.objects.air.KI_46_OTSUHEI;
import com.maddox.il2.objects.air.ME_163B1A;
import com.maddox.il2.objects.air.MXY_7;
import com.maddox.il2.objects.air.Mig_17PF;
import com.maddox.il2.objects.air.SM79;
import com.maddox.il2.objects.air.Scheme4;
import com.maddox.il2.objects.air.Swordfish;
import com.maddox.il2.objects.air.TA_152C;
import com.maddox.il2.objects.air.TA_183;
import com.maddox.il2.objects.air.TypeAcePlane;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeDiveBomber;
import com.maddox.il2.objects.air.TypeDockable;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeGlider;
import com.maddox.il2.objects.air.TypeGuidedBombCarrier;
import com.maddox.il2.objects.air.TypeHasToKG;
import com.maddox.il2.objects.air.TypeSailPlane;
import com.maddox.il2.objects.air.TypeStormovik;
import com.maddox.il2.objects.air.TypeSupersonic;
import com.maddox.il2.objects.air.TypeTransport;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.BombGunNull;
import com.maddox.il2.objects.weapons.BombGunPara;
import com.maddox.il2.objects.weapons.BombGunTorp45_36AV_A;
import com.maddox.il2.objects.weapons.ParaTorpedoGun;
import com.maddox.il2.objects.weapons.ToKGUtils;
import com.maddox.il2.objects.weapons.TorpedoGun;
import com.maddox.rts.MsgDestroy;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Maneuver extends AIFlightModel
{
	// @formatter:off
	private int					task;
	private int					maneuver					= 26;
	protected float				mn_time;
	private int[]				program						= new int[8];
	private boolean				bBusy						= true;
	public boolean				wasBusy						= true;
	public boolean				dontSwitch					= false;
	public boolean				aggressiveWingman			= false;
	public boolean				kamikaze					= false;
	public boolean				silence						= true;
	public boolean				bombsOut;
	private int					bombsOutCounter				= 0;
	public float				direction;
	public Loc					rwLoc;
	private boolean				first						= true;
	private int					rocketsDelay				= 0;
	private int					bulletDelay					= 0;
	private int					submanDelay					= 0;
	private float				maxG;
	private float				maxAOA;
	private float				LandHQ;
	public float				Alt							= 0.0F;
	private float				corrCoeff					= 0.0F;
	private float				corrElev					= 0.0F;
	private float				corrAile					= 0.0F;
	private boolean				checkGround					= false;
	private boolean				checkStrike					= false;
	private boolean				frequentControl				= false;
	private boolean				stallable					= false;
	public FlightModel			airClient					= null;
	public FlightModel			target						= null;
	public FlightModel			danger						= null;
	private float				dangerAggressiveness		= 0.0F;
	private float				oldDanCoeff					= 0.0F;
	private int					shotAtFriend				= 0;
	private float				distToFriend				= 0.0F;
	public Actor				target_ground				= null;
	public AirGroup				Group;
	protected boolean			TaxiMode					= false;
	protected boolean			finished					= false;
	protected boolean			canTakeoff					= false;
	public Point_Any			wayCurPos;
	protected Point_Any			wayPrevPos;
	protected Point_Any[]		airdromeWay;
	protected int				curAirdromePoi				= 0;
	public long					actionTimerStart			= 0L;
	public long					actionTimerStop				= 0L;
	protected int				gattackCounter				= 0;
	private int					beNearOffsPhase				= 0;
	public int					submaneuver					= 0;
	private boolean				dont_change_subm			= false;
	private int					tmpi						= 0;
	private int					sub_Man_Count				= 0;
	private float				dA;
	private float				dist						= 0.0F;
	private float				man1Dist					= 50.0F;
	private float				bullTime					= 0.0015F;
	protected static final int	STAY_ON_THE_TAIL			= 1;
	protected static final int	NOT_TOO_FAST				= 2;
	protected static final int	FROM_WAYPOINT				= 3;
	protected static final int	CONST_SPEED					= 4;
	protected static final int	MIN_SPEED					= 5;
	protected static final int	MAX_SPEED					= 6;
	protected static final int	CONST_POWER					= 7;
	protected static final int	ZERO_POWER					= 8;
	protected static final int	BOOST_ON					= 9;
	protected static final int	FOLLOW_WITHOUT_FLAPS		= 10;
	protected int				speedMode					= 3;
	protected float				smConstSpeed				= 90.0F;
	protected float				smConstPower				= 0.7F;
	protected FlightModel		tailForStaying				= null;
	public Vector3d				tailOffset					= new Vector3d();
	protected int				Speak5minutes;
	protected int				Speak1minute;
	protected int				SpeakBeginGattack;
	protected boolean			WeWereInGAttack				= false;
	protected boolean			WeWereInAttack				= false;
	protected boolean			SpeakMissionAccomplished	= false;
	public static final int		ROOKIE						= 0;
	public static final int		NORMAL						= 1;
	public static final int		VETERAN						= 2;
	public static final int		ACE							= 3;
	public static final int		NO_TASK						= 0;
	public static final int		WAIT						= 1;
	public static final int		STAY_FORMATION				= 2;
	public static final int		FLY_WAYPOINT				= 3;
	public static final int		DEFENCE						= 4;
	public static final int		DEFENDING					= 5;
	public static final int		ATTACK_AIR					= 6;
	public static final int		ATTACK_GROUND				= 7;
	public static final int		NONE						= 0;
	public static final int		HOLD						= 1;
	public static final int		PULL_UP						= 2;
	public static final int		LEVEL_PLANE					= 3;
	public static final int		ROLL						= 4;
	public static final int		ROLL_90						= 5;
	public static final int		ROLL_180					= 6;
	public static final int		SPIRAL_BRAKE				= 7;
	public static final int		SPIRAL_UP					= 8;
	public static final int		SPIRAL_DOWN					= 9;
	public static final int		CLIMB						= 10;
	public static final int		DIVING_0_RPM				= 11;
	public static final int		DIVING_30_DEG				= 12;
	public static final int		DIVING_45_DEG				= 13;
	public static final int		TURN						= 14;
	public static final int		MIL_TURN					= 15;
	public static final int		LOOP						= 16;
	public static final int		LOOP_DOWN					= 17;
	public static final int		HALF_LOOP_UP				= 18;
	public static final int		HALF_LOOP_DOWN				= 19;
	public static final int		STALL						= 20;
	public static final int		WAYPOINT					= 21;
	public static final int		SPEEDUP						= 22;
	public static final int		BELL						= 23;
	public static final int		FOLLOW						= 24;
	public static final int		LANDING						= 25;
	public static final int		TAKEOFF						= 26;
	public static final int		ATTACK						= 27;
	public static final int		WAVEOUT						= 28;
	public static final int		SINUS						= 29;
	public static final int		ZIGZAG_UP					= 30;
	public static final int		ZIGZAG_DOWN					= 31;
	public static final int		ZIGZAG_SPIT					= 32;
	public static final int		HALF_LOOP_DOWN_135			= 33;
	public static final int		HARTMANN_REDOUT				= 34;
	public static final int		ROLL_360					= 35;
	public static final int		STALL_POKRYSHKIN			= 36;
	public static final int		BARREL_POKRYSHKIN			= 37;
	public static final int		SLIDE_LEVEL					= 38;
	public static final int		SLIDE_DESCENT				= 39;
	public static final int		RANVERSMAN					= 40;
	public static final int		CUBAN						= 41;
	public static final int		CUBAN_INVERT				= 42;
	public static final int		GATTACK						= 43;
	public static final int		PILOT_DEAD					= 44;
	public static final int		HANG_ON						= 45;
	public static final int		DELAY						= 48;
	public static final int		EMERGENCY_LANDING			= 49;
	public static final int		GATTACK_DIVE				= 50;
	public static final int		GATTACK_TORPEDO				= 51;
	public static final int		GATTACK_CASSETTE			= 52;
	public static final int		GATTACK_KAMIKAZE			= 46;
	public static final int		GATTACK_TINYTIM				= 47;
	public static final int		GATTACK_HS293				= 79;
	public static final int		GATTACK_FRITZX				= 80;
	public static final int		GATTACK_TORPEDO_TOKG		= 81;
	public static final int		FAR_FOLLOW					= 53;
	public static final int		SPIRAL_DOWN_SLOW			= 54;
	public static final int		FOLLOW_SPIRAL_UP			= 55;
	public static final int		SINUS_SHALLOW				= 56;
	public static final int		GAIN						= 57;
	public static final int		SEPARATE					= 58;
	public static final int		BE_NEAR						= 59;
	public static final int		EVADE_UP					= 60;
	public static final int		EVADE_DN					= 61;
	public static final int		ENERGY_ATTACK				= 62;
	public static final int		ATTACK_BOMBER				= 63;
	public static final int		PARKED_STARTUP				= 64;
	public static final int		COVER						= 65;
	public static final int		TAXI						= 66;
	public static final int		RUN_AWAY					= 67;
	public static final int		FAR_COVER					= 68;
	public static final int		TAKEOFF_VTOL_A				= 69;
	public static final int		LANDING_VTOL_A				= 70;
	private static final int	SUB_MAN0					= 0;
	private static final int	SUB_MAN1					= 1;
	private static final int	SUB_MAN2					= 2;
	private static final int	SUB_MAN3					= 3;
	private static final int	SUB_MAN4					= 4;
	private static final int	SUB_MAN5					= 5;
	private static final int	SUB_MAN69					= 69;
	public static final int		LIVE						= 0;
	public static final int		RETURN						= 1;
	public static final int		TASK						= 2;
	public static final int		PROTECT_LEADER				= 3;
	public static final int		PROTECT_WINGMAN				= 4;
	public static final int		PROTECT_FRIENDS				= 5;
	public static final int		DESTROY_ENEMIES				= 6;
	public static final int		KEEP_ORDER					= 7;
	public float[]				takeIntoAccount				= new float[8];
	public float[]				AccountCoeff				= new float[8];
	public static final int		FVSB_BOOM_ZOOM				= 0;
	public static final int		FVSB_BOOM_ZOOM_TO_ENGINE	= 1;
	public static final int		FVSB_SHALLOW_DIVE_TO_ENGINE	= 2;
	public static final int		FVSB_FROM_AHEAD				= 3;
	public static final int		FVSB_FROM_BELOW				= 4;
	public static final int		FVSB_AS_IT_IS				= 5;
	public static final int		FVSB_FROM_SIDE				= 6;
	public static final int		FVSB_FROM_TAIL_TO_ENGINE	= 7;
	public static final int		FVSB_FROM_TAIL				= 8;
	public static final int		FVSB_SHALLOW_DIVE			= 9;
	public static final int		FVSB_FROM_BOTTOM			= 10;
	private Vector3d			ApproachV					= new Vector3d();
	private Vector3d			TargV						= new Vector3d();
	private Vector3d			TargDevV					= new Vector3d(0.0, 0.0, 0.0);
	private Vector3d			TargDevVnew					= new Vector3d(0.0, 0.0, 0.0);
	private Vector3d			scaledApproachV				= new Vector3d();
	private float				ApproachR;
	private float				TargY;
	private float				TargZ;
	private float				TargYS;
	private float				TargZS;
	private float				RandomVal;
	public Vector3d				followOffset				= new Vector3d();
	private Vector3d			followTargShift				= new Vector3d(0.0, 0.0, 0.0);
	private Vector3d			followCurShift				= new Vector3d(0.0, 0.0, 0.0);
	private float				raAilShift					= 0.0F;
	private float				raElevShift					= 0.0F;
	private float				raRudShift					= 0.0F;
	private float				sinKren						= 0.0F;
	private boolean				strikeEmer					= false;
	private FlightModel			strikeTarg					= null;
	private boolean				direc						= false;
	float						Kmax						= 10.0F;
	float						rmin;
	float						rmax;
	double						phase						= 0.0;
	double						radius						= 50.0;
	int							pointQuality				= -1;
	int							curPointQuality				= 50;
	private static Vector3d		tmpV3d						= new Vector3d();
	private static Vector3d		tmpV3f						= new Vector3d();
	public static Orient		tmpOr						= new Orient();
	public Orient				saveOr						= new Orient();
	private static Point3d		Po							= new Point3d();
	private static Point3d		Pc							= new Point3d();
	private static Point3d		Pd							= new Point3d();
	private static Vector3d		Ve							= new Vector3d();
	private Vector3d			oldVe						= new Vector3d();
	private Vector3d			Vtarg						= new Vector3d();
	private Vector3d			constVtarg					= new Vector3d();
	private Vector3d			constVtarg1					= new Vector3d();
	private static Vector3d		Vf							= new Vector3d();
	private Vector3d			Vxy							= new Vector3d();
	private static Vector3d		Vpl							= new Vector3d();
	private AnglesFork			AFo							= new AnglesFork();
	private float[]				headPos						= new float[3];
	private float[]				headOr						= new float[3];
	private static Point3d		P							= new Point3d();
	private static Point2f		Pcur						= new Point2f();
	private static Vector2d		Vcur						= new Vector2d();
	private static Vector2f		V_to						= new Vector2f();
	private static Vector2d		Vdiff						= new Vector2d();
	private static Loc			elLoc						= new Loc();
	public static boolean		showFM;
	private float				pilotHeadT					= 0.0F;
	private float				pilotHeadY					= 0.0F;
	Vector3d					windV						= new Vector3d();

	// TODO: CTO Mod
	// -------------------------------
	private boolean				bStage3;
	private boolean				bStage4;
	private boolean				bStage6;
	private boolean				bStage7;
	private boolean				bAlreadyCheckedStage7;
	private float				fNearestDistance;
	private boolean				bCatapultAI;
	private boolean				bNoNavLightsAI;
	private boolean				bFastLaunchAI;
	// -------------------------------

	// TODO: GATTACK
	//---------------------------------
	private int					passCounter					= 0;
	//--------------------------------
	// @formatter:on
	
	public void set_task(int i)
	{
		task = i;
	}

	public int get_task()
	{
		return task;
	}

	public int get_maneuver()
	{
		return maneuver;
	}

	public void set_maneuver(int i)
	{
		if (maneuver != 44 && (i == 44 || (maneuver != 26 && maneuver != 69 && maneuver != 66 && maneuver != 46)))
		{
			int j = maneuver;
			maneuver = i;
			if (j != maneuver)
				set_flags();
		}
	}

	public void pop()
	{
		if (maneuver != 44 && (program[0] == 44 || (maneuver != 26 && maneuver != 69 && maneuver != 66 && maneuver != 46)))
		{
			int i = maneuver;
			maneuver = program[0];
			for (int j = 0; j < program.length - 1; j++)
				program[j] = program[j + 1];
			program[program.length - 1] = 0;
			if (i != maneuver)
				set_flags();
		}
	}

	public void unblock()
	{
		maneuver = 0;
	}

	public void safe_set_maneuver(int i)
	{
		dont_change_subm = true;
		set_maneuver(i);
		dont_change_subm = true;
	}

	public void safe_pop()
	{
		dont_change_subm = true;
		pop();
		dont_change_subm = true;
	}

	public void clear_stack()
	{
		for (int i = 0; i < program.length; i++)
			program[i] = 0;
	}

	public void push(int i)
	{
		for (int j = program.length - 2; j >= 0; j--)
			program[j + 1] = program[j];
		program[0] = i;
	}

	public void push()
	{
		push(maneuver);
	}

	public void accurate_set_task_maneuver(int i, int j)
	{
		if (maneuver != 44 && maneuver != 26 && maneuver != 69 && maneuver != 64)
		{
			set_task(i);
			if (maneuver != j)
			{
				clear_stack();
				set_maneuver(j);
			}
		}
	}

	public void accurate_set_FOLLOW()
	{
		if (maneuver != 44 && maneuver != 26 && maneuver != 69 && maneuver != 64)
		{
			set_task(2);
			if (maneuver != 24 && maneuver != 53)
			{
				clear_stack();
				set_maneuver(24);
			}
		}
	}

	public void setBusy(boolean flag)
	{
		bBusy = flag;
	}

	public boolean isBusy()
	{
		return bBusy;
	}

	public void setSpeedMode(int i)
	{
		speedMode = i;
	}

	private boolean isStallable()
	{
		if (actor instanceof TypeStormovik)
			return false;
		return !(actor instanceof TypeTransport);
	}

	private void resetControls()
	{
		CT.AileronControl = CT.BrakeControl = CT.ElevatorControl = CT.FlapsControl = CT.RudderControl = 0.0F;
		CT.AirBrakeControl = 0.0F;
	}

	private void set_flags()
	{
		if (World.cur().isDebugFM())
			printDebugFM();
		AP.setStabAll(false);
		mn_time = 0.0F;
		minElevCoeff = 4.0F;
		if (!dont_change_subm)
		{
			setSpeedMode(3);
			first = true;
			submaneuver = SUB_MAN0;
			sub_Man_Count = 0;
		}
		dont_change_subm = false;
		if (maneuver != 48 && maneuver != 0 && maneuver != 26 && maneuver != 64 && maneuver != 44)
			resetControls();
		if (maneuver == 20 || maneuver == 25 || maneuver == 66 || maneuver == 1 || maneuver == 26 || maneuver == 69 || maneuver == 44 || maneuver == 49 || maneuver == 43 || maneuver == 50
				|| maneuver == 51 || maneuver == 81 || maneuver == 46 || maneuver == 2 || maneuver == 10 || maneuver == 57 || maneuver == 64)
			setCheckGround(false);
		else
			setCheckGround(true);
		if (maneuver == 24 || maneuver == 53 || maneuver == 68 || maneuver == 59 || maneuver == 8 || maneuver == 55 || maneuver == 27 || maneuver == 62 || maneuver == 63 || maneuver == 25
				|| maneuver == 66 || maneuver == 43 || maneuver == 50 || maneuver == 65 || maneuver == 44 || maneuver == 21 || maneuver == 64 || maneuver == 69)
			frequentControl = true;
		else
			frequentControl = false;
		if (maneuver == 25 || maneuver == 26 || maneuver == 69 || maneuver == 70)
			turnOnChristmasTree(true);
		else
			turnOnChristmasTree(false);
		if (maneuver == 25)
			turnOnCloudShine(true);
		else
			turnOnCloudShine(false);
		if (maneuver == 60 || maneuver == 61 || maneuver == 66 || maneuver == 1 || maneuver == 24 || maneuver == 26 || maneuver == 69 || maneuver == 64 || maneuver == 44)
			checkStrike = false;
		else
			checkStrike = true;
		if (maneuver == 44 || maneuver == 1 || maneuver == 48 || maneuver == 0 || maneuver == 26 || maneuver == 69 || maneuver == 64 || maneuver == 43 || maneuver == 50 || maneuver == 51
				|| maneuver == 52 || maneuver == 47 || maneuver == 79 || maneuver == 80)
			stallable = false;
		else
			stallable = true;
		if (maneuver == 44 || maneuver == 1 || maneuver == 26 || maneuver == 69 || maneuver == 64 || maneuver == 2 || maneuver == 57 || maneuver == 60 || maneuver == 61 || maneuver == 43
				|| maneuver == 50 || maneuver == 51 || maneuver == 52 || maneuver == 47 || maneuver == 29 || maneuver == 79 || maneuver == 80)
			setBusy(true);
	}

	public void setCheckStrike(boolean flag)
	{
		checkStrike = flag;
	}

	private void setCheckGround(boolean flag)
	{
		checkGround = flag;
	}

	public Maneuver(String s)
	{
		super(s);
		AP = new AutopilotAI(this);
		
		// TODO: CTO MOD
		// -------------------------------
		bStage3 = false;
		bStage4 = false;
		bStage6 = false;
		bStage7 = false;
		bAlreadyCheckedStage7 = false;
		bNoNavLightsAI = false;
		bFastLaunchAI = false;

		if (com.maddox.il2.engine.Config.cur.ini.get("Mods", "NoNavLightsAI", 0) == 1)
			bNoNavLightsAI = true;
		if (com.maddox.il2.game.Mission.cur().sectFile().get("Mods", "NoNavLightsAI", 0) == 1)
			bNoNavLightsAI = true;
		if (com.maddox.il2.engine.Config.cur.ini.get("Mods", "FastLaunchAI", 0) == 1)
			bFastLaunchAI = true;
		if (com.maddox.il2.game.Mission.cur().sectFile().get("Mods", "FastLaunchAI", 0) == 1)
			bFastLaunchAI = true;
		// -------------------------------
	}

	public void decDangerAggressiveness()
	{
		dangerAggressiveness -= 0.01F;
		if (dangerAggressiveness < 0.0F)
			dangerAggressiveness = 0.0F;
		oldDanCoeff -= 0.0050F;
		if (oldDanCoeff < 0.0F)
			oldDanCoeff = 0.0F;
	}

	public void incDangerAggressiveness(int i, float f, float f1, FlightModel flightmodel)
	{
		f -= 0.7F;
		if (f < 0.0F)
			f = 0.0F;
		f1 = 1000.0F - f1;
		if (f1 < 0.0F)
			f1 = 0.0F;
		double d = (double) (flightmodel.Energy - Energy) * 0.1019;
		double d1 = 1.0 + d * 0.00125;
		if (d1 > 1.2)
			d1 = 1.2;
		if (d1 < 0.8)
			d1 = 0.8;
		float f2 = (float) d1 * (7.0E-5F * f * f1);
		if (danger == null || f2 > oldDanCoeff)
			danger = flightmodel;
		oldDanCoeff = f2;
		dangerAggressiveness += f2 * (float) i;
		if (dangerAggressiveness > 1.0F)
			dangerAggressiveness = 1.0F;
	}

	public float getDangerAggressiveness()
	{
		return dangerAggressiveness;
	}

	public float getMaxHeightSpeed(float f)
	{
		if (f < HofVmax)
			return Vmax + (VmaxH - Vmax) * (f / HofVmax);
		float f1 = (f - HofVmax) / HofVmax;
		f1 = 1.0F - 1.5F * f1;
		if (f1 < 0.0F)
			f1 = 0.0F;
		return VmaxH * f1;
	}

	public float getMinHeightTurn(float f)
	{
		return Wing.T_turn;
	}

	public void setShotAtFriend(float f)
	{
		distToFriend = f;
		shotAtFriend = 30;
	}

	public boolean hasCourseWeaponBullets()
	{
		if (actor instanceof KI_46_OTSUHEI)
		{
			if (CT.Weapons[1] != null && CT.Weapons[1][0] != null && CT.Weapons[1][0].countBullets() != 0)
				return true;
			return false;
		}
		if (actor instanceof AR_234)
			return false;
		if ((CT.Weapons[0] == null || CT.Weapons[0][0] == null || CT.Weapons[0][0].countBullets() == 0) && (CT.Weapons[1] == null || CT.Weapons[1][0] == null || CT.Weapons[1][0].countBullets() == 0))
			return false;
		return true;
	}

	public boolean hasBombs()
	{
		if (CT.Weapons[3] != null)
		{
			for (int i = 0; i < CT.Weapons[3].length; i++)
			{
				if (CT.Weapons[3][i] != null && CT.Weapons[3][i].countBullets() != 0)
					return true;
			}
		}
		return false;
	}

	public boolean hasRockets()
	{
		if (CT.Weapons[2] != null)
		{
			for (int i = 0; i < CT.Weapons[2].length; i++)
			{
				if (CT.Weapons[2][i] != null && CT.Weapons[2][i].countBullets() != 0)
					return true;
			}
		}
		return false;
	}

	public boolean canAttack()
	{
		if (((Group.isWingman(Group.numInGroup((Aircraft) actor)) && !aggressiveWingman) || !this.isOk() || !hasCourseWeaponBullets()) && !hasRockets())
			return false;
		return true;
	}

	public void update(float f)
	{
		if (Config.isUSE_RENDER())
			headTurn(f);
		if (showFM)
			OutCT(20);
		super.update(f);
		callSuperUpdate = true;
		decDangerAggressiveness();
		if (Loc.z < -20.0)
			((Aircraft) actor).postEndAction(0.0, actor, 4, null);
		LandHQ = (float) Engine.land().HQ_Air(Loc.x, Loc.y);
		Po.set(Vwld);
		Po.scale(3.0);
		Po.add(Loc);
		LandHQ = (float) Math.max((double) LandHQ, Engine.land().HQ_Air(Po.x, Po.y));
		Alt = (float) Loc.z - LandHQ;
		indSpeed = this.getSpeed() * (float) Math.sqrt((double) (Density / 1.225F));
		if (!Gears.onGround() && this.isOk() && Alt > 8.0F)
		{
			if (AOA > AOA_Crit - 2.0F)
				Or.increment(0.0F, 0.05F * (AOA_Crit - 2.0F - AOA), 0.0F);
			if (AOA < -5.0F)
				Or.increment(0.0F, 0.05F * (-5.0F - AOA), 0.0F);
		}
		if (frequentControl || (Time.tickCounter() + actor.hashCode()) % 4 == 0)
		{
			turnOffTheWeapon();
			maxG = 3.5F + (float) Skill * 0.5F;
			Or.wrap();
			if (mn_time > 10.0F && AOA > AOA_Crit + 5.0F && isStallable() && stallable)
				safe_set_maneuver(20);
			switch_0_: switch (maneuver)
			{
				default:
				break;
				case 0:
					target_ground = null;
				break;
				case 1:
					dryFriction = 8.0F;
					CT.FlapsControl = 0.0F;
					CT.BrakeControl = 1.0F;
				break;
				case 48:
					if (mn_time >= 1.0F)
						pop();
				break;
				case 44:
					if (Gears.onGround() && Vwld.length() < 0.30000001192092896 && World.Rnd().nextInt(0, 99) < 4)
					{
						if (Group != null)
							Group.delAircraft((Aircraft) actor);
						if (actor instanceof TypeGlider || actor instanceof TypeSailPlane)
						{
							//TODO: Added by |ZUTI|: handle sea planes landing and despawning!
							//--------------------------------------------------------------------------
							//BornPlace zutiAirdromeBornPlace = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(P.x, P.y);
							
//							String currentAcName = Property.stringValue((actor).getClass(), "keyName");
							//System.out.println("Sea - Aircraft >" + currentAcName + "< landed!");
							//ZutiSupportMethods_Net.addAircraftToBornPlace(zutiAirdromeBornPlace, currentAcName, true, false);
							if( Mission.MDS_VARIABLES().zutiMisc_DespawnAIPlanesAfterLanding )//|| (zutiAirdromeBornPlace != null && zutiAirdromeBornPlace.zutiIsStandAloneBornPlace) )
							{
								MsgDestroy.Post((Time.current() + ZutiSupportMethods_AI.DESPAWN_AC_DELAY), actor);
							}
							//--------------------------------------------------------------------------
							break;
						}
						World.cur();
						if (actor != World.getPlayerAircraft())
						{
							if (Airport.distToNearestAirport(Loc) > 900.0)
								((Aircraft) actor).postEndAction(60.0, actor, 3, null);
							else
								MsgDestroy.Post(Time.current() + 30000L, actor);
						}
					}
					if (first)
					{
						AP.setStabAll(false);
						CT.ElevatorControl = World.Rnd().nextFloat(-0.07F, 0.4F);
						CT.AileronControl = World.Rnd().nextFloat(-0.15F, 0.15F);
					}
				break;
				case 7:
				{
					wingman(false);
					setSpeedMode(9);
					if (this.getW().x <= 0.0)
					{
						CT.AileronControl = -1.0F;
						CT.RudderControl = 1.0F;
					}
					else
					{
						CT.AileronControl = 1.0F;
						CT.RudderControl = -1.0F;
					}
					float f1 = Or.getKren();
					if (f1 > -90.0F && f1 < 90.0F)
					{
						float f4 = 0.01111F * (90.0F - Math.abs(f1));
						CT.ElevatorControl = -0.08F * f4 * (Or.getTangage() - 3.0F);
					}
					else
					{
						float f5 = 0.01111F * (Math.abs(f1) - 90.0F);
						CT.ElevatorControl = 0.08F * f5 * (Or.getTangage() - 3.0F);
					}
					if (this.getSpeed() < 1.7F * Vmin)
						pop();
					if (mn_time > 2.2F)
						pop();
					if (danger != null && danger instanceof Pilot && ((Maneuver) danger).target == this && Loc.distance(danger.Loc) > 400.0)
						pop();
					break;
				}
				case 8:
					if (first && !this.isCapableOfACM())
					{
						if (Skill > 0)
							pop();
						if (Skill > 1)
							this.setReadyToReturn(true);
					}
					setSpeedMode(6);
					tmpOr.setYPR(Or.getYaw(), 0.0F, 0.0F);
					if (Or.getKren() > 0.0F)
						Ve.set(100.0, -6.0, 10.0);
					else
						Ve.set(100.0, 6.0, 10.0);
					tmpOr.transform(Ve);
					Or.transformInv(Ve);
					Ve.normalize();
					farTurnToDirection();
					if (Alt > 250.0F && mn_time > 8.0F || mn_time > 120.0F)
						pop();
				break;
				case 55:
					if (first && !this.isCapableOfACM())
					{
						if (Skill > 0)
							pop();
						if (Skill > 1)
							this.setReadyToReturn(true);
					}
					setSpeedMode(6);
					tmpOr.setYPR(Or.getYaw(), 0.0F, 0.0F);
					if (Leader != null && Actor.isAlive(Leader.actor) && mn_time < 2.5F)
					{
						if (Leader.Or.getKren() > 0.0F)
							Ve.set(100.0, -6.0, 10.0);
						else
							Ve.set(100.0, 6.0, 10.0);
					}
					else if (Or.getKren() > 0.0F)
						Ve.set(100.0, -6.0, 10.0);
					else
						Ve.set(100.0, 6.0, 10.0);
					tmpOr.transform(Ve);
					Or.transformInv(Ve);
					Ve.normalize();
					farTurnToDirection();
					if (Alt > 250.0F && mn_time > 8.0F || mn_time > 120.0F)
						pop();
				break;
				
				// TODO: GATTACKSPIRAL
				//------------------------------------------------------					
				case 82: // 'I'
					setSpeedMode(6);
					if (first)
						super.AP.setStabAltitude(true);
					dA = super.Or.getKren();
					if ((double) getOverload() < 1.0D / java.lang.Math.abs(java.lang.Math.cos(dA)))
						super.CT.ElevatorControl += 1.0F * f;
					else
						super.CT.ElevatorControl -= 1.0F * f;
					if (dA > 0.0F)
						dA -= 50F;
					else
						dA -= 310F;
					if (dA < -180F)
						dA += 360F;
					dA = -0.01F * dA;
					super.CT.AileronControl = dA;
					if (mn_time > 5F)
						pop();
				break;
				//-----------------------------------------	
				
				case 45:
					setSpeedMode(7);
					smConstPower = 0.8F;
					dA = Or.getKren();
					if (dA > 0.0F)
						dA -= 25.0F;
					else
						dA -= 335.0F;
					if (dA < -180.0F)
						dA += 360.0F;
					dA *= -0.01F;
					CT.AileronControl = dA;
					CT.ElevatorControl = (-0.04F * (Or.getTangage() - 1.0F) + 0.0020F * (AP.way.curr().z() - (float) Loc.z + 250.0F));
					if (mn_time > 60.0F)
					{
						mn_time = 0.0F;
						pop();
					}
				break;
				case 54:
					if (Vwld.length() > (double) (VminFLAPS * 2.0F))
						Vwld.scale(0.9950000047683716);
					/* fall through */
				case 9:
					if (first && !this.isCapableOfACM())
					{
						if (Skill > 0)
							pop();
						if (Skill > 1)
							this.setReadyToReturn(true);
					}
					setSpeedMode(4);
					smConstSpeed = 100.0F;
					dA = Or.getKren();
					if (dA > 0.0F)
						dA -= 50.0F;
					else
						dA -= 310.0F;
					if (dA < -180.0F)
						dA += 360.0F;
					dA *= -0.01F;
					CT.AileronControl = dA;
					dA = (-10.0F - Or.getTangage()) * 0.05F;
					CT.ElevatorControl = dA;
					if ((double) this.getOverload() < 1.0 / Math.abs(Math.cos((double) dA)))
						CT.ElevatorControl += 1.0F * f;
					else
						CT.ElevatorControl -= 1.0F * f;
					if (Alt < 100.0F)
					{
						push(3);
						pop();
					}
					if (mn_time > 5.0F)
						pop();
				break;
				case 14:
					setSpeedMode(6);
					if (first)
						AP.setStabAltitude(true);
					dA = Or.getKren();
					if ((double) this.getOverload() < 1.0 / Math.abs(Math.cos((double) dA)))
						CT.ElevatorControl += 1.0F * f;
					else
						CT.ElevatorControl -= 1.0F * f;
					if (dA > 0.0F)
						dA -= 50.0F;
					else
						dA -= 310.0F;
					if (dA < -180.0F)
						dA += 360.0F;
					dA *= -0.01F;
					CT.AileronControl = dA;
					if (mn_time > 5.0F)
						pop();
				break;
				case 4:
					CT.AileronControl = this.getW().x > 0.0 ? 1.0F : -1.0F;
					CT.ElevatorControl = 0.1F * (float) (Math.cos((double) FMMath.DEG2RAD(Or.getKren())));
					CT.RudderControl = 0.0F;
					if (this.getSpeedKMH() < 220.0F)
					{
						push(3);
						pop();
					}
					if (mn_time > 7.0F)
						pop();
				break;
				case 2:
					minElevCoeff = 20.0F;
					if (first)
					{
						wingman(false);
						AP.setStabAll(false);
						CT.RudderControl = 0.0F;
						if (World.Rnd().nextInt(0, 99) < 10 && Alt < 80.0F)
							Voice.speakPullUp((Aircraft) actor);
					}
					setSpeedMode(9);
					CT.BayDoorControl = 0.0F;
					CT.AirBrakeControl = 0.0F;
					CT.AileronControl = -0.04F * (dA = Or.getKren());
					CT.ElevatorControl = 1.0F + 0.3F * (float) this.getW().y;
					if (CT.ElevatorControl < 0.0F)
						CT.ElevatorControl = 0.0F;
					if (AOA > 15.0F)
						Or.increment(0.0F, (15.0F - AOA) * 0.5F * f, 0.0F);
					if (Alt < 10.0F && Vwld.z < 0.0)
						Vwld.z *= 0.8999999761581421;
					if (Vwld.z > 1.0)
					{
						if (actor instanceof TypeGlider)
							push(49);
						else
							push(57);
						pop();
					}
					if (mn_time > 25.0F)
					{
						push(49);
						pop();
					}
				break;
				case 60:
					setSpeedMode(9);
					CT.RudderControl = 0.0F;
					CT.ElevatorControl = 1.0F;
					if (mn_time > 0.15F)
						pop();
				break;
				case 61:
					CT.RudderControl = 0.0F;
					CT.ElevatorControl = -0.4F;
					if (mn_time > 0.2F)
						pop();
				break;
				case 3:
					if (first && program[0] == 49)
						pop();
					setSpeedMode(6);
					CT.AileronControl = -0.04F * Or.getKren();
					dA = (this.getSpeedKMH() - 180.0F - Or.getTangage() * 10.0F - this.getVertSpeed() * 5.0F) * 0.0040F;
					CT.ElevatorControl = dA;
					if (this.getSpeed() > Vmin * 1.2F && this.getVertSpeed() > 0.0F)
					{
						setSpeedMode(7);
						smConstPower = 0.7F;
						pop();
					}
				break;
				case 10:
					AP.setStabAll(false);
					setSpeedMode(6);
					CT.AileronControl = -0.04F * Or.getKren();
					dA = CT.ElevatorControl;
					if (Or.getTangage() > 15.0F)
						dA -= (Or.getTangage() - 15.0F) * 0.1F * f;
					else
						dA = ((float) Vwld.length() / VminFLAPS * 140.0F - 50.0F - Or.getTangage() * 20.0F) * 0.0040F;
					dA += 0.5 * this.getW().y;
					CT.ElevatorControl = dA;
					if (Alt > 250.0F && mn_time > 6.0F || mn_time > 20.0F)
						pop();
				break;
				case 57:
					AP.setStabAll(false);
					setSpeedMode(9);
					CT.AileronControl = -0.04F * Or.getKren();
					dA = CT.ElevatorControl;
					if (Or.getTangage() > 25.0F)
						dA -= (Or.getTangage() - 25.0F) * 0.1F * f;
					else
						dA = ((float) Vwld.length() / VminFLAPS * 140.0F - 50.0F - Or.getTangage() * 20.0F) * 0.0040F;
					dA += 0.5 * this.getW().y;
					CT.ElevatorControl = dA;
					if (Alt > 150.0F || Alt > 100.0F && mn_time > 2.0F || mn_time > 3.0F)
						pop();
				break;
				case 11:
					setSpeedMode(8);
					if (Math.abs(Or.getKren()) < 90.0F)
					{
						CT.AileronControl = -0.04F * Or.getKren();
						if (Vwld.z > 0.0 || this.getSpeedKMH() < 270.0F)
							dA = -0.04F;
						else
							dA = 0.04F;
						CT.ElevatorControl = CT.ElevatorControl * 0.9F + dA * 0.1F;
					}
					else
					{
						CT.AileronControl = 0.04F * (180.0F - Math.abs(Or.getKren()));
						if (Or.getTangage() > -25.0F)
							dA = 0.33F;
						else if (Vwld.z > 0.0 || this.getSpeedKMH() < 270.0F)
							dA = 0.04F;
						else
							dA = -0.04F;
						CT.ElevatorControl = CT.ElevatorControl * 0.9F + dA * 0.1F;
					}
					if (Alt < 120.0F || mn_time > 4.0F)
						pop();
				break;
				case 12:
					setSpeedMode(4);
					smConstSpeed = 80.0F;
					CT.AileronControl = -0.04F * Or.getKren();
					if (Vwld.length() > (double) (VminFLAPS * 2.0F))
						Vwld.scale(0.9950000047683716);
					dA = -((float) Vwld.z / (Math.abs(this.getSpeed()) + 1.0F) + 0.5F);
					if (dA < -0.1F)
						dA = -0.1F;
					CT.ElevatorControl = (CT.ElevatorControl * 0.9F + dA * 0.1F + 0.3F * (float) this.getW().y);
					if (mn_time > 5.0F || Alt < 200.0F)
						pop();
				break;
				case 13:
					if (first)
					{
						AP.setStabAll(false);
						submaneuver = actor instanceof TypeFighter ? SUB_MAN0 : SUB_MAN2;
					}
					switch (submaneuver)
					{
						case SUB_MAN0:
							dA = Or.getKren() - 180.0F;
							if (dA < -180.0F)
								dA += 360.0F;
							dA *= -0.04F;
							CT.AileronControl = dA;
							if (mn_time > 3.0F || (Math.abs(Or.getKren()) > 175.0F - 5.0F * (float) Skill))
								submaneuver++;
						break;
						case SUB_MAN1:
							dA = Or.getKren() - 180.0F;
							if (dA < -180.0F)
								dA += 360.0F;
							dA *= -0.04F;
							CT.AileronControl = dA;
							CT.RudderControl = -0.1F * this.getAOS();
							setSpeedMode(8);
							if (Or.getTangage() > -45.0F && this.getOverload() < maxG)
								CT.ElevatorControl += 1.5F * f;
							else
								CT.ElevatorControl -= 0.5F * f;
							if (Or.getTangage() < -44.0F)
								submaneuver++;
							if ((double) Alt < -5.0 * Vwld.z || mn_time > 5.0F)
								pop();
						break;
						case SUB_MAN2:
							setSpeedMode(8);
							CT.AileronControl = -0.04F * Or.getKren();
							dA = -((float) Vwld.z / (Math.abs(this.getSpeed()) + 1.0F) + 0.707F);
							if (dA < -0.75F)
								dA = -0.75F;
							CT.ElevatorControl = (CT.ElevatorControl * 0.9F + dA * 0.1F + 0.5F * (float) this.getW().y);
							if ((double) Alt < -5.0 * Vwld.z || mn_time > 5.0F)
								pop();
						break;
					}
					if (Alt < 200.0F)
						pop();
				break;
				case 5:
					dA = Or.getKren();
					if (dA < 0.0F)
						dA -= 270.0F;
					else
						dA -= 90.0F;
					if (dA < -180.0F)
						dA += 360.0F;
					dA *= -0.02F;
					CT.AileronControl = dA;
					if (mn_time > 5.0F || Math.abs(Math.abs(Or.getKren()) - 90.0F) < 1.0F)
						pop();
				break;
				case 6:
					dA = Or.getKren() - 180.0F;
					if (dA < -180.0F)
						dA += 360.0F;
					CT.AileronControl = (float) ((double) (-0.04F * dA) - 0.5 * this.getW().x);
					if (mn_time > 4.0F || Math.abs(Or.getKren()) > 178.0F)
					{
						W.x = 0.0;
						pop();
					}
				break;
				case 35:
				{
					if (first)
					{
						AP.setStabAll(false);
						direction = Or.getKren();
						submaneuver = Or.getKren() <= 0.0F ? -1 : 1;
						tmpi = 0;
						setSpeedMode(9);
					}
					CT.AileronControl = 1.0F * (float) submaneuver;
					CT.RudderControl = 0.0F * (float) submaneuver;
					float f2 = Or.getKren();
					if (f2 > -90.0F && f2 < 90.0F)
					{
						float f6 = 0.01111F * (90.0F - Math.abs(f2));
						CT.ElevatorControl = -0.08F * f6 * (Or.getTangage() - 3.0F);
					}
					else
					{
						float f7 = 0.01111F * (90.0F - Math.abs(f2));
						CT.ElevatorControl = 0.08F * f7 * (Or.getTangage() - 3.0F);
					}
					if (Or.getKren() * direction < 0.0F)
						tmpi = 1;
					do
					{
						if (tmpi == 1)
						{
							if (submaneuver <= 0)
							{
								if (Or.getKren() < direction)
									break;
							}
							else if (Or.getKren() > direction)
								break;
						}
						if (!(mn_time > 17.5F))
							break switch_0_;
					}
					while (false);
					pop();
					break;
				}
				case 22:
					setSpeedMode(9);
					CT.AileronControl = -0.04F * Or.getKren();
					CT.ElevatorControl = -0.04F * (Or.getTangage() + 5.0F);
					CT.RudderControl = 0.0F;
					if (this.getSpeed() > Vmax || mn_time > 30.0F)
						pop();
				break;
				case 67:
					minElevCoeff = 18.0F;
					if (first)
					{
						sub_Man_Count = 0;
						setSpeedMode(9);
						CT.setPowerControl(1.1F);
					}
					if (danger != null)
					{
						float f8 = 700.0F - (float) danger.Loc.distance(Loc);
						if (f8 < 0.0F)
							f8 = 0.0F;
						f8 *= 0.00143F;
						float f3 = Or.getKren();
						if (sub_Man_Count == 0 || first)
						{
							if (raAilShift < 0.0F)
								raAilShift = f8 * World.Rnd().nextFloat(0.6F, 1.0F);
							else
								raAilShift = f8 * World.Rnd().nextFloat(-1.0F, -0.6F);
							raRudShift = f8 * World.Rnd().nextFloat(-0.5F, 0.5F);
							raElevShift = f8 * World.Rnd().nextFloat(-0.8F, 0.8F);
						}
						CT.AileronControl = 0.9F * CT.AileronControl + 0.1F * raAilShift;
						CT.RudderControl = 0.95F * CT.RudderControl + 0.05F * raRudShift;
						if (f3 > -90.0F && f3 < 90.0F)
							CT.ElevatorControl = -0.04F * (Or.getTangage() + 5.0F);
						else
							CT.ElevatorControl = 0.05F * (Or.getTangage() + 5.0F);
						CT.ElevatorControl += 0.1F * raElevShift;
						sub_Man_Count++;
						if ((float) sub_Man_Count >= 80.0F * (1.5F - f8) && f3 > -70.0F && f3 < 70.0F)
							sub_Man_Count = 0;
						if (mn_time > 30.0F)
							pop();
					}
					else
						pop();
				break;
				case 16:
					if (first)
					{
						if (!this.isCapableOfACM())
							pop();
						AP.setStabAll(false);
						setSpeedMode(6);
						if (this.getSpeed() < 0.75F * Vmax)
						{
							push();
							push(22);
							pop();
							break;
						}
						submaneuver = SUB_MAN0;
					}
					maxAOA = Vwld.z <= 0.0 ? 12.0F : 7.0F;
					switch (submaneuver)
					{
						case SUB_MAN0:
							CT.ElevatorControl = 0.05F;
							CT.AileronControl = -0.04F * Or.getKren();
							CT.RudderControl = -0.1F * this.getAOS();
							if (Math.abs(Or.getKren()) < 2.0F)
								submaneuver++;
						break;
						case SUB_MAN1:
							CT.AileronControl = -0.04F * Or.getKren();
							CT.RudderControl = -0.1F * this.getAOS();
							dA = 0.5F;
							if (this.getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA)
								CT.ElevatorControl -= 0.4F * f;
							else
								CT.ElevatorControl += 0.4F * f;
							if (Or.getTangage() > 80.0F)
								submaneuver++;
							if (this.getSpeed() < Vmin * 1.5F)
								pop();
						break;
						case SUB_MAN2:
							CT.RudderControl = -0.1F * this.getAOS() * (this.getSpeed() <= 300.0F ? 0.0F : 1.0F);
							dA = 1.0F;
							if (this.getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA)
								CT.ElevatorControl -= 0.4F * f;
							else
								CT.ElevatorControl += 0.4F * f;
							if (Or.getTangage() < 0.0F)
								submaneuver++;
						break;
						case SUB_MAN3:
							CT.RudderControl = -0.1F * this.getAOS() * (this.getSpeed() <= 300.0F ? 0.0F : 1.0F);
							dA = 1.0F;
							if (this.getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA)
								CT.ElevatorControl -= 0.2F * f;
							else
								CT.ElevatorControl += 0.2F * f;
							if (Or.getTangage() < -60.0F)
								submaneuver++;
						break;
						case SUB_MAN4:
							if (Or.getTangage() > -45.0F)
							{
								CT.AileronControl = -0.04F * Or.getKren();
								maxAOA = 3.5F;
							}
							CT.RudderControl = -0.1F * this.getAOS();
							dA = 0.5F;
							if (this.getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA)
								CT.ElevatorControl -= 1.0F * f;
							else
								CT.ElevatorControl += 0.4F * f;
							if (!(Or.getTangage() > 3.0F) && !(Vwld.z > 0.0))
								break;
							pop();
					}
				break;
				case 17:
					if (first)
					{
						if (Alt < 1000.0F)
							pop();
						else if (this.getSpeed() < Vmin * 1.2F)
						{
							push();
							push(22);
							pop();
						}
						else
						{
							push(18);
							push(19);
							pop();
						}
					}
					else
						pop();
				break;
				case 19:
					if (first)
					{
						if (Alt < 1000.0F)
						{
							pop();
							break;
						}
						AP.setStabAll(false);
						submaneuver = SUB_MAN0;
					}
					maxAOA = Vwld.z <= 0.0 ? 12.0F : 7.0F;
					switch (submaneuver)
					{
						case SUB_MAN0:
							CT.ElevatorControl = 0.05F;
							CT.AileronControl = 0.04F * (Or.getKren() <= 0.0F ? -180.0F + Or.getKren() : 180.0F - Or.getKren());
							CT.RudderControl = -0.1F * this.getAOS();
							if (Math.abs(Or.getKren()) > 178.0F)
								submaneuver++;
						break;
						case SUB_MAN1:
							setSpeedMode(7);
							smConstPower = 0.5F;
							CT.AileronControl = 0.0F;
							CT.RudderControl = -0.1F * this.getAOS();
							dA = 1.0F;
							if (this.getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA)
								CT.ElevatorControl -= 0.2F * f;
							else
								CT.ElevatorControl += 1.2F * f;
							if (Or.getTangage() < -60.0F)
								submaneuver++;
						break;
						case SUB_MAN2:
							if (Or.getTangage() > -45.0F)
							{
								CT.AileronControl = -0.04F * Or.getKren();
								setSpeedMode(9);
								maxAOA = 7.0F;
							}
							CT.RudderControl = -0.1F * this.getAOS();
							dA = 0.5F;
							if (this.getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA)
								CT.ElevatorControl -= 0.8F * f;
							else
								CT.ElevatorControl += 0.4F * f;
							if (!(Or.getTangage() > AOA - 1.0F) && !(Vwld.z > 0.0))
								break;
							pop();
					}
				break;
				case 18:
					if (first)
					{
						if (!this.isCapableOfACM())
							pop();
						if (this.getSpeed() < Vmax * 0.75F)
						{
							push();
							push(22);
							pop();
							break;
						}
						AP.setStabAll(false);
						submaneuver = SUB_MAN0;
						setSpeedMode(6);
					}
					maxAOA = Vwld.z <= 0.0 ? 12.0F : 7.0F;
					switch (submaneuver)
					{
						case SUB_MAN0:
							CT.AileronControl = -0.04F * Or.getKren();
							CT.RudderControl = -0.1F * this.getAOS();
							if (Math.abs(Or.getKren()) < 2.0F)
								submaneuver++;
						break;
						case SUB_MAN1:
							CT.AileronControl = -0.04F * Or.getKren();
							CT.RudderControl = -0.1F * this.getAOS();
							dA = 1.0F;
							if (this.getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA)
								CT.ElevatorControl -= 0.4F * f;
							else
								CT.ElevatorControl += 0.8F * f;
							if (Or.getTangage() > 80.0F)
								submaneuver++;
							if (this.getSpeed() < Vmin * 1.5F)
								pop();
						break;
						case SUB_MAN2:
							CT.RudderControl = -0.1F * this.getAOS() * (this.getSpeed() <= 300.0F ? 0.0F : 1.0F);
							dA = 1.0F;
							if (this.getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA)
								CT.ElevatorControl -= 0.4F * f;
							else
								CT.ElevatorControl += 0.4F * f;
							if (Or.getTangage() < 12.0F)
								submaneuver++;
						break;
						case SUB_MAN3:
							if (Math.abs(Or.getKren()) < 60.0F)
								CT.ElevatorControl = 0.05F;
							CT.AileronControl = -0.04F * Or.getKren();
							CT.RudderControl = -0.1F * this.getAOS();
							if (Math.abs(Or.getKren()) < 30.0F)
								submaneuver++;
						break;
						case SUB_MAN4:
							pop();
					}
				break;
				case 15:
					if (first && this.getSpeed() < 0.35F * (Vmin + Vmax))
						pop();
					else
					{
						push(8);
						pop();
						CT.RudderControl = -0.1F * this.getAOS();
						if (Or.getKren() < 0.0F)
							CT.AileronControl = -0.04F * (Or.getKren() + 30.0F);
						else
							CT.AileronControl = -0.04F * (Or.getKren() - 30.0F);
						if (mn_time > 7.5F)
							pop();
					}
				break;
				case 20:
					if (first)
					{
						wingman(false);
						setSpeedMode(6);
					}
					if (!this.isCapableOfBMP())
					{
						this.setReadyToDie(true);
						pop();
					}
					if (this.getW().z > 0.0)
						CT.RudderControl = 1.0F;
					else
						CT.RudderControl = -1.0F;
					if (AOA > AOA_Crit - 4.0F)
						Or.increment(0.0F, 0.01F * (AOA_Crit - 4.0F - AOA), 0.0F);
					if (AOA < -5.0F)
						Or.increment(0.0F, 0.01F * (-5.0F - AOA), 0.0F);
					if (AOA < AOA_Crit - 1.0F)
						pop();
					if (mn_time > 14.0F - (float) Skill * 4.0F || Alt < 50.0F)
						pop();
				break;
				case 21:
					AP.setWayPoint(true);
					if (this.getAltitude() < AP.way.curr().z() - 100.0F && actor instanceof TypeSupersonic)
						CT.ElevatorControl += 0.025F;
					if (mn_time > 300.0F)
						pop();
					if (this.isTick(256, 0) && !actor.isTaskComplete() && (AP.way.isLast() && AP.getWayPointDistance() < 1500.0F || AP.way.isLanding()))
						World.onTaskComplete(actor);
					if (((Aircraft) actor).aircIndex() == 0 && !this.isReadyToReturn())
					{
						World.cur();
						if (World.getPlayerAircraft() != null)
						{
							World.cur();
							if (((Aircraft) actor).getRegiment() == World.getPlayerAircraft().getRegiment())
							{
								float f9 = 1.0E12F;
								if (AP.way.curr().Action == 3)
									f9 = AP.getWayPointDistance();
								else
								{
									int i = AP.way.Cur();
									AP.way.next();
									if (AP.way.curr().Action == 3)
										f9 = AP.getWayPointDistance();
									AP.way.setCur(i);
								}
								if (Speak5minutes == 0 && 22000.0F < f9 && f9 < 30000.0F)
								{
									Voice.speak5minutes((Aircraft) actor);
									Speak5minutes = 1;
								}
								if (Speak1minute == 0 && f9 < 10000.0F)
								{
									Voice.speak1minute((Aircraft) actor);
									Speak1minute = 1;
									Speak5minutes = 1;
								}
								if ((WeWereInGAttack || WeWereInAttack) && mn_time > 5.0F)
								{
									if (!SpeakMissionAccomplished)
									{
										boolean flag = false;
										int j = AP.way.Cur();
										if (AP.way.curr().Action == 3 || AP.way.curr().getTarget() != null)
											break;
										while (AP.way.Cur() < AP.way.size() - 1)
										{
											AP.way.next();
											if (AP.way.curr().Action == 3 || (AP.way.curr().getTarget() != null))
												flag = true;
										}
										AP.way.setCur(j);
										if (!flag)
										{
											Voice.speakMissionAccomplished((Aircraft) actor);
											SpeakMissionAccomplished = true;
										}
									}
									if (!SpeakMissionAccomplished)
									{
										Speak5minutes = 0;
										Speak1minute = 0;
										SpeakBeginGattack = 0;
									}
									WeWereInGAttack = false;
									WeWereInAttack = false;
								}
							}
						}
					}
					if ((actor instanceof TypeBomber || actor instanceof TypeTransport) && AP.way.curr() != null && AP.way.curr().Action == 3
							&& (AP.way.curr().getTarget() == null || actor instanceof Scheme4))
					{
						double d = Loc.z - World.land().HQ(Loc.x, Loc.y);
						if (d < 0.0)
							d = 0.0;
						if (((double) AP.getWayPointDistance() < ((double) this.getSpeed() * Math.sqrt(d * 0.2038699984550476))) && !bombsOut)
						{
							if (CT.Weapons[3] != null && CT.Weapons[3][0] != null && CT.Weapons[3][0].countBullets() != 0 && !(CT.Weapons[3][0] instanceof BombGunPara))
								Voice.airSpeaks((Aircraft) actor, 85, 1);
							bombsOut = true;
							AP.way.curr().Action = 0;
							if (Group != null)
								Group.dropBombs();
						}
					}
					setSpeedMode(3);
					if (AP.way.isLandingOnShip() && AP.way.isLanding())
					{
						AP.way.landingAirport.rebuildLandWay(this);
						if (CT.bHasCockpitDoorControl)
							AS.setCockpitDoor(actor, 1);
					}
				break;
				case 23:
					if (first)
					{
						wingman(false);
						if (this.getSpeedKMH() < Vmin * 1.25F)
						{
							push();
							push(22);
							pop();
							break;
						}
					}
					setSpeedMode(6);
					CT.AileronControl = -0.04F * Or.getKren();
					CT.RudderControl = -0.1F * this.getAOS();
					if (Or.getTangage() < 70.0F && this.getOverload() < maxG && AOA < 14.0F)
						CT.ElevatorControl += 0.5F * f;
					else
						CT.ElevatorControl -= 0.5F * f;
					if (Vwld.z < 1.0)
						pop();
				break;
				case 24:
					if (Leader == null || !Actor.isAlive(Leader.actor) || !((Maneuver) Leader).isOk()
							|| (((Maneuver) Leader).isBusy() && (!(Leader instanceof RealFlightModel) || !((RealFlightModel) Leader).isRealMode())))
						set_maneuver(0);
					else
					{
						if (actor instanceof TypeGlider)
						{
							if (Leader.AP.way.curr().Action != 0 && Leader.AP.way.curr().Action != 1)
								set_maneuver(49);
						}
						else if (Leader.getSpeed() < 30.0F || ((Leader.Loc.z - Engine.land().HQ_Air(Leader.Loc.x, Leader.Loc.y)) < 50.0))
						{
							airClient = Leader;
							push();
							push(59);
							pop();
							break;
						}
						if (!Leader.AP.way.isLanding())
						{
							AP.way.setCur(Leader.AP.way.Cur());
							if (Leader.Wingman != this)
							{
								if (!bombsOut && ((Maneuver) Leader).bombsOut)
								{
									bombsOut = true;
									Maneuver maneuver1 = this;
									while (maneuver1.Wingman != null)
									{
										maneuver1 = (Maneuver) maneuver1.Wingman;
										maneuver1.bombsOut = true;
									}
								}
								if (CT.BayDoorControl != Leader.CT.BayDoorControl)
								{
									CT.BayDoorControl = Leader.CT.BayDoorControl;
									Pilot pilot = (Pilot) this;
									while (pilot.Wingman != null)
									{
										pilot = (Pilot) pilot.Wingman;
										pilot.CT.BayDoorControl = CT.BayDoorControl;
									}
								}
							}
						}
						else
						{
							if (Leader.Wingman != this)
							{
								push(8);
								push(8);
								push(World.Rnd().nextBoolean() ? 8 : 48);
								push(World.Rnd().nextBoolean() ? 8 : 48);
								pop();
							}
							Leader = null;
							pop();
							break;
						}
						airClient = Leader;
						tmpOr.setAT0(airClient.Vwld);
						tmpOr.increment(0.0F, airClient.getAOA(), 0.0F);
						Ve.set(followOffset);
						Ve.x -= 300.0;
						tmpV3f.sub(followTargShift, followCurShift);
						if (tmpV3f.lengthSquared() < 0.5)
							followTargShift.set((double) World.cur().rnd.nextFloat(-0.0F, 10.0F), (double) World.cur().rnd.nextFloat(-5.0F, 5.0F), (double) World.cur().rnd.nextFloat(-3.5F, 3.5F));
						tmpV3f.normalize();
						tmpV3f.scale((double) (2.0F * f));
						followCurShift.add(tmpV3f);
						Ve.add(followCurShift);
						tmpOr.transform(Ve, Po);
						Po.scale(-1.0);
						Po.add(airClient.Loc);
						Ve.sub(Po, Loc);
						Or.transformInv(Ve);
						dist = (float) Ve.length();
						if (followOffset.x > 600.0)
						{
							Ve.set(followOffset);
							Ve.x -= 0.5 * followOffset.x;
							tmpOr.transform(Ve, Po);
							Po.scale(-1.0);
							Po.add(airClient.Loc);
							Ve.sub(Po, Loc);
							Or.transformInv(Ve);
						}
						Ve.normalize();
						if ((double) dist > 600.0 + Ve.x * 400.0)
						{
							push();
							push(53);
							pop();
						}
						else
						{
							if (actor instanceof TypeTransport && (double) Vmax < 70.0)
								farTurnToDirection(6.2F);
							else
								attackTurnToDirection(dist, f, 10.0F);
							setSpeedMode(10);
							tailForStaying = Leader;
							tailOffset.set(followOffset);
							tailOffset.scale(-1.0);
						}
					}
				break;
				case 65:
					if (!(this instanceof RealFlightModel) || !((RealFlightModel) this).isRealMode())
					{
						bombsOut = true;
						CT.dropFuelTanks();
					}
					if (airClient == null || !Actor.isAlive(airClient.actor) || !this.isOk())
						set_maneuver(0);
					else
					{
						Maneuver maneuver2 = (Maneuver) airClient;
						Maneuver maneuver4 = (Maneuver) ((Maneuver) airClient).danger;
						if ((maneuver2.getDangerAggressiveness() >= 1.0F - (float) Skill * 0.2F) && maneuver4 != null && hasCourseWeaponBullets())
						{
							Voice.speakCheckYour6((Aircraft) maneuver2.actor, (Aircraft) maneuver4.actor);
							Voice.speakHelpFromAir((Aircraft) actor, 1);
							set_task(6);
							target = maneuver4;
							set_maneuver(27);
						}
						Ve.sub(airClient.Loc, Loc);
						Or.transformInv(Ve);
						dist = (float) Ve.length();
						Ve.normalize();
						attackTurnToDirection(dist, f, 10.0F + (float) Skill * 8.0F);
						if (Alt > 50.0F)
							setSpeedMode(1);
						else
							setSpeedMode(6);
						tailForStaying = airClient;
						tailOffset.set(followOffset);
						tailOffset.scale(-1.0);
						if ((double) dist > 600.0 + Ve.x * 400.0 && get_maneuver() != 27)
						{
							push();
							push(53);
							pop();
						}
					}
				break;
				case 53:
					if (airClient == null || !Actor.isAlive(airClient.actor) || !this.isOk())
					{
						airClient = null;
						set_maneuver(0);
					}
					else
					{
						maxAOA = 5.0F;
						Ve.set(followOffset);
						Ve.x -= 300.0;
						tmpOr.setAT0(airClient.Vwld);
						tmpOr.increment(0.0F, 4.0F, 0.0F);
						tmpOr.transform(Ve, Po);
						Po.scale(-1.0);
						Po.add(airClient.Loc);
						Ve.sub(Po, Loc);
						Or.transformInv(Ve);
						dist = (float) Ve.length();
						Ve.normalize();
						if (Vmax < 83.0F)
							farTurnToDirection(8.5F);
						else
							farTurnToDirection(7.0F);
						float f10 = (Energy - airClient.Energy) * 0.1019F;
						if ((double) f10 < -50.0 + followOffset.z)
							setSpeedMode(9);
						else
							setSpeedMode(10);
						tailForStaying = airClient;
						tailOffset.set(followOffset);
						tailOffset.scale(-1.0);
						if ((double) dist < 500.0 + Ve.x * 200.0)
							pop();
						else
						{
							if (AOA > 12.0F && Alt > 15.0F)
								Or.increment(0.0F, 12.0F - AOA, 0.0F);
							if (mn_time > 30.0F && (Ve.x < 0.0 || dist > 10000.0F))
								pop();
						}
					}
				break;
				case 68:
					if (airClient == null || !Actor.isAlive(airClient.actor) || !this.isOk())
						set_maneuver(0);
					else
					{
						Maneuver maneuver3 = (Maneuver) airClient;
						Maneuver maneuver5 = (Maneuver) ((Maneuver) airClient).danger;
						if ((maneuver3.getDangerAggressiveness() >= 1.0F - (float) Skill * 0.3F) && maneuver5 != null && hasCourseWeaponBullets())
						{
							tmpV3d.sub(maneuver3.Vwld, maneuver5.Vwld);
							if (tmpV3d.length() < 170.0)
							{
								set_task(6);
								target = maneuver5;
								set_maneuver(27);
							}
						}
						maxAOA = 5.0F;
						Ve.set(followOffset);
						Ve.x -= 300.0;
						tmpOr.setAT0(airClient.Vwld);
						tmpOr.increment(0.0F, 4.0F, 0.0F);
						tmpOr.transform(Ve, Po);
						Po.scale(-1.0);
						Po.add(airClient.Loc);
						Ve.sub(Po, Loc);
						Or.transformInv(Ve);
						dist = (float) Ve.length();
						Ve.normalize();
						if (Vmax < 83.0F)
							farTurnToDirection(8.5F);
						else
							farTurnToDirection(7.0F);
						setSpeedMode(10);
						tailForStaying = airClient;
						tailOffset.set(followOffset);
						tailOffset.scale(-1.0);
						if ((double) dist < 500.0 + Ve.x * 200.0)
							pop();
						else
						{
							if (AOA > 12.0F && Alt > 15.0F)
								Or.increment(0.0F, 12.0F - AOA, 0.0F);
							if (mn_time > 30.0F && (Ve.x < 0.0 || dist > 10000.0F))
								pop();
						}
					}
				break;
				case 59:
					if (airClient == null || !Actor.isValid(airClient.actor) || !this.isOk())
					{
						airClient = null;
						set_maneuver(0);
					}
					else
					{
						maxAOA = 5.0F;
						if (first)
							followOffset.set((double) (1000.0F * (float) (Math.sin((double) ((float) beNearOffsPhase * 0.7854F)))), (double) (1000.0F * (float) (Math
									.cos((double) ((float) beNearOffsPhase * 0.7854F)))), -400.0);
						Ve.set(followOffset);
						Ve.x -= 300.0;
						tmpOr.setAT0(airClient.Vwld);
						tmpOr.increment(0.0F, 4.0F, 0.0F);
						tmpOr.transform(Ve, Po);
						Po.scale(-1.0);
						Po.add(airClient.Loc);
						Ve.sub(Po, Loc);
						Or.transformInv(Ve);
						dist = (float) Ve.length();
						Ve.normalize();
						farTurnToDirection();
						setSpeedMode(2);
						tailForStaying = airClient;
						tailOffset.set(followOffset);
						tailOffset.scale(-1.0);
						if (dist < 300.0F)
						{
							beNearOffsPhase++;
							if (beNearOffsPhase > 3)
								beNearOffsPhase = 0;
							float f12 = (float) Math.sqrt(followOffset.x * followOffset.x + (followOffset.y * followOffset.y));
							followOffset.set((double) (f12 * (float) (Math.sin((double) ((float) beNearOffsPhase * 1.5708F)))), (double) (f12 * (float) (Math
									.cos((double) ((float) beNearOffsPhase * 1.5708F)))), followOffset.z);
						}
						if (AOA > 12.0F && Alt > 15.0F)
							Or.increment(0.0F, 12.0F - AOA, 0.0F);
						if (mn_time > 15.0F && (Ve.x < 0.0 || dist > 3000.0F))
							pop();
						if (mn_time > 30.0F)
							pop();
					}
				break;
				case 63:
					if (!(this instanceof RealFlightModel) || !((RealFlightModel) this).isRealMode())
					{
						bombsOut = true;
						CT.dropFuelTanks();
					}
					if (target == null || !Actor.isValid(target.actor) || target.isTakenMortalDamage() || !hasCourseWeaponBullets())
					{
						target = null;
						clear_stack();
						set_maneuver(3);
					}
					else if (target.getSpeedKMH() < 45.0F && (target.Loc.z - Engine.land().HQ_Air(target.Loc.x, target.Loc.y)) < 50.0 && target.actor.getArmy() != actor.getArmy())
					{
						target_ground = target.actor;
						set_task(7);
						clear_stack();
						set_maneuver(43);
					}
					else
					{
						if (actor instanceof HE_LERCHE3 && ((HE_LERCHE3) actor).bToFire)
						{
							CT.WeaponControl[2] = true;
							((HE_LERCHE3) actor).bToFire = false;
						}
						if (actor instanceof TA_183 && ((TA_183) actor).bToFire)
						{
							CT.WeaponControl[2] = true;
							((TA_183) actor).bToFire = false;
						}
						if (actor instanceof TA_152C && ((TA_152C) actor).bToFire)
						{
							CT.WeaponControl[2] = true;
							((TA_152C) actor).bToFire = false;
						}
						if (actor instanceof Mig_17PF && ((Mig_17PF) actor).bToFire)
						{
							CT.WeaponControl[2] = true;
							((Mig_17PF) actor).bToFire = false;
						}
						if (TargV.z == -400.0)
							fighterUnderBomber(f);
						else
							fighterVsBomber(f);
						if (AOA > AOA_Crit - 2.0F && Alt > 15.0F)
							Or.increment(0.0F, 0.01F * (AOA_Crit - 2.0F - AOA), 0.0F);
					}
				break;
				case 27:
					if (!(this instanceof RealFlightModel) || !((RealFlightModel) this).isRealMode())
					{
						bombsOut = true;
						CT.dropFuelTanks();
					}
					if (target == null || !Actor.isValid(target.actor) || target.isTakenMortalDamage() || target.actor.getArmy() == actor.getArmy() || !hasCourseWeaponBullets())
					{
						target = null;
						clear_stack();
						set_maneuver(0);
					}
					else if (target.getSpeedKMH() < 45.0F && (target.Loc.z - Engine.land().HQ_Air(target.Loc.x, target.Loc.y)) < 50.0 && target.actor.getArmy() != actor.getArmy())
					{
						target_ground = target.actor;
						set_task(7);
						clear_stack();
						set_maneuver(43);
					}
					else
					{
						if (first && actor instanceof TypeAcePlane)
						{
							if (target != null && target.actor != null && target.actor.getArmy() != actor.getArmy())
								target.Skill = 0;
							if (danger != null && danger.actor != null && danger.actor.getArmy() != actor.getArmy())
								danger.Skill = 0;
						}
						if (target.actor.getArmy() != actor.getArmy())
							((Maneuver) target).danger = this;
						if (this.isTick(64, 0))
						{
							float f11 = (target.Energy - Energy) * 0.1019F;
							Ve.sub(target.Loc, Loc);
							Or.transformInv(Ve);
							Ve.normalize();
							float f13 = 470.0F + (float) Ve.x * 120.0F - f11;
							if (f13 < 0.0F)
							{
								clear_stack();
								set_maneuver(62);
							}
						}
						fighterVsFighter(f);
						setSpeedMode(9);
						if (AOA > AOA_Crit - 1.0F && Alt > 15.0F)
							Or.increment(0.0F, 0.01F * (AOA_Crit - 1.0F - AOA), 0.0F);
						if (mn_time > 100.0F)
						{
							target = null;
							pop();
						}
					}
				break;
				case 62:
					if (target == null || !Actor.isValid(target.actor) || target.isTakenMortalDamage() || target.actor.getArmy() == actor.getArmy() || !hasCourseWeaponBullets())
					{
						target = null;
						clear_stack();
						set_maneuver(0);
					}
					else if (target.getSpeedKMH() < 45.0F && (target.Loc.z - Engine.land().HQ_Air(target.Loc.x, target.Loc.y)) < 50.0 && target.actor.getArmy() != actor.getArmy())
					{
						target_ground = target.actor;
						set_task(7);
						clear_stack();
						set_maneuver(43);
					}
					else
					{
						if (first && actor instanceof TypeAcePlane)
						{
							if (target != null && target.actor != null && target.actor.getArmy() != actor.getArmy())
								target.Skill = 0;
							if (danger != null && danger.actor != null && danger.actor.getArmy() != actor.getArmy())
								danger.Skill = 0;
						}
						if (target.actor.getArmy() != actor.getArmy())
							((Maneuver) target).danger = this;
						goodFighterVsFighter(f);
					}
				break;
				case 70:
					checkGround = false;
					checkStrike = false;
					frequentControl = true;
					stallable = false;
					if (actor instanceof HE_LERCHE3)
					{
						switch (submaneuver)
						{
							case SUB_MAN0:
								AP.setStabAll(false);
								submaneuver++;
								sub_Man_Count = 0;
								/* fall through */
							case SUB_MAN1:
								if (sub_Man_Count == 0)
									CT.AileronControl = World.cur().rnd.nextFloat(-0.15F, 0.15F);
								CT.AirBrakeControl = 1.0F;
								CT.setPowerControl(1.0F);
								CT.ElevatorControl = Aircraft.cvt(Or.getTangage(), 0.0F, 60.0F, 1.0F, 0.0F);
								if (Or.getTangage() > 30.0F)
								{
									submaneuver++;
									sub_Man_Count = 0;
								}
								sub_Man_Count++;
							break;
							case SUB_MAN2:
								CT.AileronControl = 0.0F;
								CT.ElevatorControl = 0.0F;
								CT.setPowerControl(0.1F);
								Or.increment(0.0F, (float) ((double) f * 0.1 * (double) sub_Man_Count * (90.0 - (double) Or.getTangage())), 0.0F);
								if (Or.getTangage() > 89.0F)
								{
									saveOr.set(Or);
									submaneuver++;
								}
								sub_Man_Count++;
							break;
							case SUB_MAN3:
								CT.ElevatorControl = 0.0F;
								if (Alt > 10.0F)
									CT.setPowerControl(0.33F);
								else
									CT.setPowerControl(0.0F);
								if (Alt < 20.0F)
								{
									if (Vwld.z < -4.0)
										Vwld.z *= 0.95;
									if (Vwld.lengthSquared() < 1.0)
										EI.setEngineStops();
								}
								Or.set(saveOr);
								if (!(mn_time > 100.0F))
									break;
								Vwld.set(0.0, 0.0, 0.0);
								MsgDestroy.Post(Time.current() + 12000L, actor);
						}
					}
				break;
				case 25:
					wingman(false);
					if (AP.way.isLanding())
					{
						if (AP.way.isLandingOnShip())
						{
							AP.way.landingAirport.rebuildLandWay(this);
							if (CT.GearControl == 1.0F && CT.arrestorControl < 1.0F && !Gears.onGround())
								AS.setArrestor(actor, 1);
						}
						if (first)
						{
							AP.setWayPoint(true);
							doDumpBombsPassively();
							submaneuver = SUB_MAN0;
						}
						if (actor instanceof HE_LERCHE3 && Alt < 50.0F)
							maneuver = 70;
						AP.way.curr().getP(Po);
						int k = AP.way.Cur();
						float f17 = (float) Loc.z - AP.way.last().z();
						AP.way.setCur(k);
						Alt = Math.min(Alt, f17);
						Po.add(0.0, 0.0, -3.0);
						Ve.sub(Po, Loc);
						float f20 = (float) Ve.length();
						boolean flag3 = Alt > 4.5F + Gears.H && AP.way.Cur() < 8;
						if (AP.way.isLandingOnShip())
							flag3 = Alt > 4.5F + Gears.H && AP.way.Cur() < 8;
						if (flag3)
						{
							AP.way.prev();
							AP.way.curr().getP(Pc);
							AP.way.next();
							tmpV3f.sub(Po, Pc);
							tmpV3f.normalize();
							if (tmpV3f.dot(Ve) < 0.0 && f20 > 1000.0F && !TaxiMode)
							{
								AP.way.first();
								push(10);
								pop();
								CT.GearControl = 0.0F;
							}
							float f25 = (float) tmpV3f.dot(Ve);
							tmpV3f.scale((double) -f25);
							tmpV3f.add(Po, tmpV3f);
							tmpV3f.sub(Loc);
							float f29 = 5.0E-4F * (3000.0F - f20);
							if (f29 > 1.0F)
								f29 = 1.0F;
							if (f29 < 0.1F)
								f29 = 0.1F;
							float f31 = (float) tmpV3f.length();
							if (f31 > 40.0F * f29)
							{
								f31 = 40.0F * f29;
								tmpV3f.normalize();
								tmpV3f.scale((double) f31);
							}
							float f33 = VminFLAPS;
							if (AP.way.Cur() >= 6)
							{
								if (AP.way.isLandingOnShip())
								{
									if (Actor.isAlive(AP.way.landingAirport) && (AP.way.landingAirport instanceof AirportCarrier))
									{
										float f34 = (float) ((AirportCarrier) AP.way.landingAirport).speedLen();
										if (VminFLAPS < f34 + 10.0F)
											f33 = f34 + 10.0F;
									}
								}
								else
									f33 = VminFLAPS * 1.2F;
								if (f33 < 14.0F)
									f33 = 14.0F;
							}
							else
								f33 = VminFLAPS * 2.0F;
							double d4 = Vwld.length();
							double d6 = (double) f33 - d4;
							float f35 = 2.0F * f;
							if (d6 > (double) f35)
								d6 = (double) f35;
							if (d6 < (double) -f35)
								d6 = (double) -f35;
							Ve.normalize();
							Ve.scale(d4);
							Ve.add(tmpV3f);
							Ve.sub(Vwld);
							float f36 = (50.0F * f29 - f31) * f;
							if (Ve.length() > (double) f36)
							{
								Ve.normalize();
								Ve.scale((double) f36);
							}
							Vwld.add(Ve);
							Vwld.normalize();
							Vwld.scale(d4 + d6);
							Loc.x += Vwld.x * (double) f;
							Loc.y += Vwld.y * (double) f;
							Loc.z += Vwld.z * (double) f;
							Or.transformInv(tmpV3f);
							tmpOr.setAT0(Vwld);
							float f37 = 0.0F;
							if (AP.way.isLandingOnShip())
								f37 = 0.9F * (45.0F - Alt);
							else
								f37 = 0.7F * (20.0F - Alt);
							if (f37 < 0.0F)
								f37 = 0.0F;
							tmpOr.increment(0.0F, 4.0F + f37, (float) (tmpV3f.y * 0.5));
							Or.interpolate(tmpOr, 0.5F * f);
							callSuperUpdate = false;
							W.set(0.0, 0.0, 0.0);
							CT.ElevatorControl = 0.05F + 0.3F * f37;
							CT.RudderControl = (float) (-tmpV3f.y * 0.02);
							direction = Or.getAzimut();
						}
						else
							AP.setStabDirection(true);
					}
					else
						AP.setStabDirection(true);
					dA = CT.ElevatorControl;
					AP.update(f);
					setSpeedControl(f);
					CT.ElevatorControl = dA;
					if (maneuver == 25)
					{
						if (Alt > 60.0F)
						{
							if (Alt < 160.0F)
								CT.FlapsControl = 0.32F;
							else
								CT.FlapsControl = 0.0F;
							setSpeedMode(7);
							smConstPower = 0.2F;
							dA = Math.min(130.0F + Alt, 270.0F);
							if (Vwld.z > 0.0 || this.getSpeedKMH() < dA)
								dA = -1.2F * f;
							else
								dA = 1.2F * f;
							CT.ElevatorControl = CT.ElevatorControl * 0.9F + dA * 0.1F;
						}
						else
						{
							minElevCoeff = 15.0F;
							if (AP.way.Cur() >= 6 || AP.way.Cur() == 0)
							{
								if (Or.getTangage() < -5.0F)
									Or.increment(0.0F, -Or.getTangage() - 5.0F, 0.0F);
								if (Or.getTangage() > Gears.Pitch + 10.0F)
									Or.increment(0.0F, -(Or.getTangage() - (Gears.Pitch + 10.0F)), 0.0F);
							}
							CT.FlapsControl = 1.0F;
							if (Vrel.length() < 1.0)
							{
								CT.FlapsControl = CT.BrakeControl = 0.0F;
								if (!TaxiMode)
								{
									setSpeedMode(8);
									if (AP.way.isLandingOnShip())
									{
										if (CT.getFlap() < 0.0010F)
											AS.setWingFold(actor, 1);
										CT.BrakeControl = 1.0F;
										if (CT.arrestorControl == 1.0F && Gears.onGround())
											AS.setArrestor(actor, 0);
										MsgDestroy.Post(Time.current() + 25000L, actor);
									}
									else
									{
										EI.setEngineStops();
										if (EI.engines[0].getPropw() < 1.0F)
										{
											World.cur();
											if (actor != World.getPlayerAircraft())
												MsgDestroy.Post((Time.current() + 12000L), actor);
										}
									}
								}
							}
							if (this.getSpeed() < VmaxFLAPS * 0.21F)
								CT.FlapsControl = 0.0F;
							if (Vrel.length() < (double) (VmaxFLAPS * 0.25F) && wayCurPos == null && !AP.way.isLandingOnShip())
							{
								TaxiMode = true;
								AP.way.setCur(0);
								return;
							}
							if (this.getSpeed() > VminFLAPS * 0.6F && Alt < 10.0F)
							{
								setSpeedMode(8);
								if (AP.way.isLandingOnShip() && CT.bHasArrestorControl)
								{
									if (Vwld.z < -5.5)
										Vwld.z *= 0.949999988079071;
									if (Vwld.z > 0.0)
										Vwld.z *= 0.9100000262260437;
								}
								else
								{
									if (Vwld.z < -0.6000000238418579)
										Vwld.z *= 0.9399999976158142;
									if (Vwld.z < -2.5)
										Vwld.z = -2.5;
									if (Vwld.z > 0.0)
										Vwld.z *= 0.9100000262260437;
								}
							}
							float f14 = Gears.Pitch - 2.0F;
							if (f14 < 5.0F)
								f14 = 5.0F;
							if (Alt < 7.0F && Or.getTangage() < f14 || AP.way.isLandingOnShip())
								CT.ElevatorControl += 1.5F * f;
							CT.ElevatorControl += 0.05000000074505806 * this.getW().y;
							if (Gears.onGround())
							{
								if (Gears.Pitch > 5.0F)
								{
									if (Or.getTangage() < Gears.Pitch)
										CT.ElevatorControl += 1.5F * f;
									if (!AP.way.isLandingOnShip())
										CT.ElevatorControl += 0.30000001192092896 * this.getW().y;
								}
								else
									CT.ElevatorControl = 0.0F;
								if (!TaxiMode)
								{
									AFo.setDeg(Or.getAzimut(), direction);
									CT.RudderControl = (8.0F * AFo.getDiffRad() + 0.5F * (float) this.getW().z);
								}
								else
									CT.RudderControl = 0.0F;
							}
						}
						AP.way.curr().getP(Po);
						return;
					}
					return;
				case 66:
					if (!this.isCapableOfTaxiing() || EI.getThrustOutput() < 0.01F)
					{
						set_task(3);
						maneuver = 0;
						set_maneuver(49);
						AP.setStabAll(false);
					}
					else
					{
						if (AS.isPilotDead(0))
						{
							set_task(3);
							maneuver = 0;
							set_maneuver(44);
							setSpeedMode(8);
							smConstPower = 0.0F;
							if (Airport.distToNearestAirport(Loc) > 900.0)
								((Aircraft) actor).postEndAction(6000.0, actor, 3, null);
							else
								MsgDestroy.Post(Time.current() + 300000L, actor);
						}
						else
						{
							P.x = Loc.x;
							P.y = Loc.y;
							P.z = Loc.z;
							Vcur.set(Vwld);
							if (wayCurPos == null)
							{
								World.cur().airdrome.findTheWay((Pilot) this);
								wayPrevPos = wayCurPos = getNextAirdromeWayPoint();
							}
							if (wayCurPos != null)
							{
								Point_Any point_any = wayCurPos;
//								Point_Any point_any1 = wayPrevPos;
								Pcur.set((float) P.x, (float) P.y);
								float f21 = Pcur.distance(point_any);
//								float f23 = Pcur.distance(point_any1);
								V_to.sub(point_any, Pcur);
								V_to.normalize();
								float f26 = 5.0F + 0.1F * f21;
								if (f26 > 12.0F)
									f26 = 12.0F;
								if (f26 > 0.9F * VminFLAPS)
									f26 = 0.9F * VminFLAPS;
								if ((curAirdromePoi < airdromeWay.length && f21 < 15.0F) || f21 < 4.0F)
								{
									f26 = 0.0F;
									Point_Any point_any2 = getNextAirdromeWayPoint();
									if (point_any2 == null)
									{
										CT.setPowerControl(0.0F);
										Loc.set(P);
										Loc.set(Loc);
										if (!finished)
										{
											finished = true;
											int l = 1000;
											if (wayCurPos != null)
												l = 2400000;
											actor.collide(true);
											Vwld.set(0.0, 0.0, 0.0);
											CT.setPowerControl(0.0F);
											EI.setCurControlAll(true);
											EI.setEngineStops();
											World.cur();
											if (actor != World.getPlayerAircraft())
											{
												MsgDestroy.Post((Time.current() + (long) l), actor);
												this.setStationedOnGround(true);
												maneuver = 0;
												set_maneuver(44);
											}
											return;
										}
										return;
									}
									wayPrevPos = wayCurPos;
									wayCurPos = point_any2;
								}
								V_to.scale(f26);
								float f30 = 2.0F * f;
								Vdiff.set(V_to);
								Vdiff.sub(Vcur);
								float f32 = (float) Vdiff.length();
								if (f32 > f30)
								{
									Vdiff.normalize();
									Vdiff.scale((double) f30);
								}
								Vcur.add(Vdiff);
								tmpOr.setYPR(FMMath.RAD2DEG((float) Vcur.direction()), Or.getPitch(), 0.0F);
								Or.interpolate(tmpOr, 0.2F);
								Vwld.x = Vcur.x;
								Vwld.y = Vcur.y;
								P.x += Vcur.x * (double) f;
								P.y += Vcur.y * (double) f;
							}
							else
							{
								wayPrevPos = wayCurPos = new Point_Null((float) Loc.x, (float) Loc.y);
								World.cur();
								if (actor != World.getPlayerAircraft())
								{
									MsgDestroy.Post(Time.current() + 30000L, actor);
									this.setStationedOnGround(true);
								}
							}
							Loc.set(P);
							Loc.set(Loc);
							return;
						}
						return;
					}
				break;
				case 49:
					emergencyLanding(f);
				break;
				case 64:
					if (actor instanceof TypeGlider)
						pop();
					else
					{
						if (actor instanceof HE_LERCHE3)
						{
							boolean flag1 = (Actor.isAlive(AP.way.takeoffAirport) && (AP.way.takeoffAirport instanceof AirportCarrier));
							if (!flag1)
								callSuperUpdate = false;
						}
						CT.BrakeControl = 1.0F;
						CT.ElevatorControl = 0.5F;
						CT.setPowerControl(0.0F);
						EI.setCurControlAll(false);
						setSpeedMode(0);
						if (World.Rnd().nextFloat() < 0.05F)
						{
							EI.setCurControl(submaneuver, true);
							if (EI.engines[submaneuver].getStage() == 0)
							{
								setSpeedMode(0);
								CT.setPowerControl(0.05F);
								EI.engines[submaneuver].setControlThrottle(0.2F);
								EI.toggle();
								if (actor instanceof BI_1 || actor instanceof BI_6)
								{
									pop();
									break;
								}
							}
						}
						if (EI.engines[submaneuver].getStage() == 6)
						{
							setSpeedMode(0);
							EI.engines[submaneuver].setControlThrottle(0.0F);
							submaneuver++;
							CT.setPowerControl(0.0F);
							if (submaneuver > EI.getNum() - 1)
							{
								EI.setCurControlAll(true);
								pop();
							}
						}
					}
				break;
				case 26:
				{
					float f15 = Alt;
					float f18 = 0.4F;
					if (Actor.isAlive(AP.way.takeoffAirport) && AP.way.takeoffAirport instanceof AirportCarrier)
					{
						f15 -= ((AirportCarrier) AP.way.takeoffAirport).height();
						f18 = 0.95F;
						if (Alt < 9.0F && Vwld.z < 0.0)
							Vwld.z *= 0.85;
						if (CT.bHasCockpitDoorControl)
							AS.setCockpitDoor(actor, 1);
					}
					if (first)
					{
						setCheckGround(false);
						CT.BrakeControl = 1.0F;
						CT.GearControl = 1.0F;
						CT.setPowerControl(0.0F);
						if (CT.bHasArrestorControl)
							AS.setArrestor(actor, 0);
						setSpeedMode(8);
						if (f15 > 7.21F && AP.way.Cur() == 0)
						{
							EI.setEngineRunning();
							Aircraft.debugprintln(actor, "in the air - engines running!.");
						}
						if (!Actor.isAlive(AP.way.takeoffAirport))
							direction = actor.pos.getAbsOrient().getAzimut();
						if (actor instanceof HE_LERCHE3)
						{
							maneuver = 69;
							break;
						}
					}
					if (Gears.onGround())
					{
						if (EI.engines[0].getStage() == 0)
						{
							CT.setPowerControl(0.0F);
							setSpeedMode(8);
							if (World.Rnd().nextFloat() < 0.05F && mn_time > 1.0F || mn_time > 8.0F)
							{
								push();
								push(64);
								submaneuver = SUB_MAN0;
								maneuver = 0;
								safe_pop();
								break;
							}
						}
						else
						{
							// TODO: CTO Mod
							// -------------------------------
							if (!bAlreadyCheckedStage7)
							{
								if (super.AP.way.takeoffAirport instanceof com.maddox.il2.ai.AirportCarrier)
								{
									com.maddox.il2.ai.AirportCarrier airportcarrier = (com.maddox.il2.ai.AirportCarrier) super.AP.way.takeoffAirport;
									com.maddox.il2.objects.ships.BigshipGeneric bigshipgeneric = airportcarrier.ship();
									super.Gears.setCatapultOffset(bigshipgeneric);
									bCatapultAI = super.Gears.getCatapultAI();
								}
								else
								{
									bStage7 = true;
								}
								bAlreadyCheckedStage7 = true;
							}
							if (!bCatapultAI)
							{
								Po.set(super.Loc);
								Vpl.set(60D, 0.0D, 0.0D);
								fNearestDistance = 70F;
								super.Or.transform(Vpl);
								Po.add(Vpl);
								Pd.set(Po);
							}
							else
							{
								Po.set(super.Loc);
								Vpl.set(200D, 0.0D, 0.0D);
								fNearestDistance = 210F;
								super.Or.transform(Vpl);
								Po.add(Vpl);
								Pd.set(Po);
							}
							if (canTakeoff)
							{
								if (!bStage7)
								{
									if (bStage6)
									{
										if (bFastLaunchAI || !super.CT.bHasWingControl || super.CT.bHasWingControl && super.CT.getWing() < 0.5F)
											bStage7 = true;
									}
									else if (bStage4)
									{
										if (super.CT.bHasWingControl && super.CT.getWing() > 0.001F)
										{
											if (bFastLaunchAI)
												super.CT.forceWing(0.0F);
											super.AS.setWingFold(super.actor, 0);
										}
										bStage6 = true;
									}
									else if (bStage3)
									{
										com.maddox.il2.engine.Loc loc = new Loc();
										com.maddox.il2.objects.ships.BigshipGeneric bigshipgeneric1 = (com.maddox.il2.objects.ships.BigshipGeneric) super.brakeShoeLastCarrier;
										com.maddox.il2.objects.air.Aircraft aircraft = (com.maddox.il2.objects.air.Aircraft) super.actor;
										com.maddox.il2.ai.air.CellAirField cellairfield = bigshipgeneric1.getCellTO();
										double d3;
										double d6;
										if (bCatapultAI)
										{
											d3 = -((com.maddox.JGP.Tuple3d) (cellairfield.leftUpperCorner())).x - super.Gears.getCatapultOffsetX();
											d6 = ((com.maddox.JGP.Tuple3d) (cellairfield.leftUpperCorner())).y - super.Gears.getCatapultOffsetY();
										}
										else
										{
											d3 = -((com.maddox.JGP.Tuple3d) (cellairfield.leftUpperCorner())).x - (double) (cellairfield.getWidth() / 2 - 3);
											d6 = super.brakeShoeLoc.getX() + (double) aircraft.getCellAirPlane().getHeight() + 4D;
										}
										loc.set(d6, d3, super.brakeShoeLoc.getZ(), super.brakeShoeLoc.getAzimut(), super.brakeShoeLoc.getTangage(), super.brakeShoeLoc.getKren());
										loc.add(super.brakeShoeLastCarrier.pos.getAbs());
										super.actor.pos.setAbs(loc.getPoint());
										super.brakeShoeLoc.set(super.actor.pos.getAbs());
										super.brakeShoeLoc.sub(super.brakeShoeLastCarrier.pos.getAbs());
										bStage4 = true;
									}
									else
									{
										super.CT.PowerControl = 1.0F;
										bStage3 = true;
									}
								}
								else
								{
									super.CT.PowerControl = 1.1F;
									setSpeedMode(9);
								}
							}
							// -------------------------------
							else
							{
								setSpeedMode(8);
								CT.BrakeControl = 1.0F;
								boolean bool = true;
								if (mn_time < 8.0F)
									bool = false;
								// TODO: CTO Mod
								// ---------------------------------------------------------
								if (super.actor != com.maddox.il2.ai.War.getNearestFriendAtPoint(Pd, (com.maddox.il2.objects.air.Aircraft) super.actor, fNearestDistance))
								{
									if (super.actor instanceof com.maddox.il2.objects.air.G4M2E)
									{
										if (com.maddox.il2.ai.War.getNearestFriendAtPoint(Pd, (com.maddox.il2.objects.air.Aircraft) super.actor, fNearestDistance) != ((com.maddox.il2.objects.air.G4M2E) super.actor).typeDockableGetDrone())
											bool = false;
									}
									else
									{
										bool = false;
									}
								}
								// ---------------------------------------------------------
								if (Actor.isAlive(AP.way.takeoffAirport) && AP.way.takeoffAirport.takeoffRequest > 0)
									bool = false;
								if (bool)
								{
									canTakeoff = true;
									if (Actor.isAlive(AP.way.takeoffAirport))
										AP.way.takeoffAirport.takeoffRequest = 300;
								}
							}
						}
						// TODO: Altered by CTO Mod
						// if (EI.engines[0].getStage() == 6 && CT.bHasWingControl && CT.getWing() > 0.0010F)
						if (EI.engines[0].getStage() == 6 && CT.bHasWingControl && CT.getWing() > 0.001F && !(AP.way.takeoffAirport instanceof AirportCarrier))
							AS.setWingFold(actor, 0);
					}
					else if (canTakeoff)
					{
						CT.setPowerControl(1.1F);
						setSpeedMode(9);
					}
					if (CT.FlapsControl == 0.0F && CT.getWing() < 0.0010F)
						CT.FlapsControl = 0.4F;
					if (EI.engines[0].getStage() == 6 && CT.getPower() > f18)
					{
						CT.BrakeControl = 0.0F;
						brakeShoe = false;
						float f22 = Vmin * M.getFullMass() / M.massEmpty;
						float f24 = 12.0F * (this.getSpeed() / f22 - 0.2F);
						if (Gears.bIsSail)
							f24 *= 2.0F;
						if (Gears.bFrontWheel)
							f24 = Gears.Pitch + 4.0F;
						if (f24 < 1.0F)
							f24 = 1.0F;
						if (f24 > 10.0F)
							f24 = 10.0F;
						float f27 = 1.5F;
						if (Actor.isAlive(AP.way.takeoffAirport) && AP.way.takeoffAirport instanceof AirportCarrier && !Gears.isUnderDeck())
						{
							CT.GearControl = 0.0F;
							if (f15 < 0.0F)
							{
								f24 = 18.0F;
								f27 = 0.05F;
							}
							else
							{
								f24 = 14.0F;
								f27 = 0.3F;
							}
						}
						if (Or.getTangage() < f24)
							dA = (-0.7F * (Or.getTangage() - f24) + f27 * (float) this.getW().y + 0.5F * (float) this.getAW().y);
						else
							dA = (-0.1F * (Or.getTangage() - f24) + f27 * (float) this.getW().y + 0.5F * (float) this.getAW().y);
						CT.ElevatorControl += (dA - CT.ElevatorControl) * 3.0F * f;
					}
					else
						CT.ElevatorControl = 1.0F;
					AFo.setDeg(Or.getAzimut(), direction);
					double d1 = (double) AFo.getDiffRad();
					if (EI.engines[0].getStage() == 6)
					{
						CT.RudderControl = 8.0F * (float) d1;
						if (d1 > -1.0 && d1 < 1.0)
						{
							if (Actor.isAlive(AP.way.takeoffAirport) && CT.getPower() > 0.3F)
							{
								double d2 = AP.way.takeoffAirport.ShiftFromLine(this);
								double d3 = 3.0 - 3.0 * Math.abs(d1);
								if (d3 > 1.0)
									d3 = 1.0;
								double d5 = 0.25 * d2 * d3;
								if (d5 > 1.5)
									d5 = 1.5;
								if (d5 < -1.5)
									d5 = -1.5;
								CT.RudderControl += (float) d5;
							}
						}
						else
							CT.BrakeControl = 1.0F;
					}
					CT.AileronControl = -0.05F * Or.getKren() + 0.3F * (float) this.getW().y;
					if (f15 > 5.0F && !Gears.isUnderDeck())
						CT.GearControl = 0.0F;
					float f28 = 1.0F;
					if (hasBombs())
						f28 *= 1.7F;
					if (f15 > 120.0F * f28 || this.getSpeed() > Vmin * 1.8F * f28 || f15 > 80.0F * f28 && this.getSpeed() > Vmin * 1.6F * f28
							|| (f15 > 40.0F * f28 && this.getSpeed() > Vmin * 1.3F * f28 && (mn_time > 60.0F + ((float) ((Aircraft) actor).aircIndex() * 3.0F))))
					{
						CT.FlapsControl = 0.0F;
						CT.GearControl = 0.0F;
						rwLoc = null;
						if (actor instanceof TypeGlider)
							push(24);
						maneuver = 0;
						brakeShoe = false;
						turnOffCollisions = false;
						if (CT.bHasCockpitDoorControl)
							AS.setCockpitDoor(actor, 0);
						pop();
					}
					if (actor instanceof TypeGlider)
					{
						CT.BrakeControl = 0.0F;
						CT.ElevatorControl = 0.05F;
						canTakeoff = true;
					}
					break;
				}
				case 69:
				{
					float f16 = Alt;
					float f19 = 0.4F;
					CT.BrakeControl = 1.0F;
					W.scale(0.0);
					boolean flag4 = (Actor.isAlive(AP.way.takeoffAirport) && AP.way.takeoffAirport instanceof AirportCarrier);
					if (flag4)
					{
						f16 -= ((AirportCarrier) AP.way.takeoffAirport).height();
						f19 = 0.8F;
						if (Alt < 9.0F && Vwld.z < 0.0)
							Vwld.z *= 0.85;
						if (CT.bHasCockpitDoorControl)
							AS.setCockpitDoor(actor, 1);
					}
					if (Loc.z != 0.0 && ((HE_LERCHE3) actor).suka.getPoint().z == 0.0)
						((HE_LERCHE3) actor).suka.set(Loc, Or);
					if (Loc.z != 0.0)
					{
						if (EI.getPowerOutput() < f19 && !flag4)
						{
							callSuperUpdate = false;
							Loc.set(((HE_LERCHE3) actor).suka.getPoint());
							Or.set(((HE_LERCHE3) actor).suka.getOrient());
						}
						else if (f16 < 100.0F)
							Or.set(((HE_LERCHE3) actor).suka.getOrient());
					}
					if (Gears.onGround() && EI.engines[0].getStage() == 0)
					{
						CT.setPowerControl(0.0F);
						setSpeedMode(8);
						if (World.Rnd().nextFloat() < 0.05F && mn_time > 1.0F || mn_time > 8.0F)
						{
							push();
							push(64);
							submaneuver = SUB_MAN0;
							maneuver = 0;
							safe_pop();
							break;
						}
					}
					if (EI.engines[0].getStage() == 6)
					{
						CT.BrakeControl = 0.0F;
						CT.AirBrakeControl = 1.0F;
						brakeShoe = false;
						CT.setPowerControl(1.1F);
						setSpeedMode(9);
					}
					if (f16 > 200.0F)
					{
						CT.GearControl = 0.0F;
						rwLoc = null;
						CT.ElevatorControl = -1.0F;
						CT.AirBrakeControl = 0.0F;
						if (Or.getTangage() < 25.0F)
							maneuver = 0;
						brakeShoe = false;
						turnOffCollisions = false;
						if (CT.bHasCockpitDoorControl)
							AS.setCockpitDoor(actor, 0);
						pop();
					}
					break;
				}
				case 28:
					if (first)
					{
						direction = World.Rnd().nextFloat(-25.0F, 25.0F);
						AP.setStabAll(false);
						setSpeedMode(6);
						CT.RudderControl = World.Rnd().nextFloat(-0.22F, 0.22F);
						submaneuver = SUB_MAN0;
						if (this.getSpeed() < Vmin * 1.5F)
							pop();
					}
					CT.AileronControl = -0.04F * (Or.getKren() - direction);
					switch (submaneuver)
					{
						case SUB_MAN0:
							dA = 1.0F;
							maxAOA = 12.0F + 1.0F * (float) Skill;
							if (AOA > maxAOA || CT.ElevatorControl > dA)
								CT.ElevatorControl -= 0.6F * f;
							else
								CT.ElevatorControl += 3.3F * f;
							if (Or.getTangage() > 40.0F + 5.1F * (float) Skill)
								submaneuver++;
						break;
						case SUB_MAN1:
							direction = World.Rnd().nextFloat(-25.0F, 25.0F);
							CT.RudderControl = World.Rnd().nextFloat(-0.75F, 0.75F);
							submaneuver++;
							/* fall through */
						case SUB_MAN2:
							dA = -1.0F;
							maxAOA = 12.0F + 5.0F * (float) Skill;
							if (AOA < -maxAOA || CT.ElevatorControl < dA)
								CT.ElevatorControl += 0.6F * f;
							else
								CT.ElevatorControl -= 3.3F * f;
							if (Or.getTangage() < -45.0F)
								pop();
						break;
					}
					if (mn_time > 3.0F)
						pop();
				break;
				case 29:
					minElevCoeff = 17.0F;
					if (first)
					{
						AP.setStabAll(false);
						setSpeedMode(9);
						sub_Man_Count = 0;
					}
					if (danger != null)
					{
						if (sub_Man_Count == 0)
						{
							tmpV3d.set(danger.getW());
							danger.Or.transform(tmpV3d);
							if (tmpV3d.z > 0.0)
								sinKren = -World.Rnd().nextFloat(60.0F, 90.0F);
							else
								sinKren = World.Rnd().nextFloat(60.0F, 90.0F);
						}
						if (Loc.distanceSquared(danger.Loc) < 5000.0)
						{
							setSpeedMode(8);
							CT.setPowerControl(0.0F);
						}
						else
							setSpeedMode(9);
					}
					else
						pop();
					CT.FlapsControl = 0.2F;
					if ((double) this.getSpeed() < 120.0)
						CT.FlapsControl = 0.33F;
					if ((double) this.getSpeed() < 80.0)
						CT.FlapsControl = 1.0F;
					CT.AileronControl = -0.08F * (Or.getKren() + sinKren);
					CT.ElevatorControl = 0.9F;
					CT.RudderControl = 0.0F;
					sub_Man_Count++;
					if (sub_Man_Count > 15)
						sub_Man_Count = 0;
					if (mn_time > 10.0F)
						pop();
				break;
				case 56:
					if (first)
					{
						submaneuver = World.Rnd().nextInt(SUB_MAN0, SUB_MAN1);
						direction = World.Rnd().nextFloat(-20.0F, -10.0F);
					}
					CT.AileronControl = -0.08F * (Or.getKren() - direction);
					switch (submaneuver)
					{
						case SUB_MAN0:
							dA = 1.0F;
							maxAOA = 10.0F + 2.0F * (float) Skill;
							if (((double) this.getOverload() > 1.0 / Math.abs(Math.cos(Math.toRadians((double) Or.getKren())))) || AOA > maxAOA || CT.ElevatorControl > dA)
								CT.ElevatorControl -= 0.2F * f;
							else
								CT.ElevatorControl += 0.4F * f;
							CT.RudderControl = -1.0F * (float) Math.sin(Math.toRadians((double) (Or.getKren() + 55.0F)));
							if (mn_time > 1.5F)
								submaneuver++;
						break;
						case SUB_MAN1:
							direction = World.Rnd().nextFloat(10.0F, 20.0F);
							submaneuver++;
							/* fall through */
						case SUB_MAN2:
							dA = 1.0F;
							maxAOA = 10.0F + 2.0F * (float) Skill;
							if (((double) this.getOverload() > 1.0 / Math.abs(Math.cos(Math.toRadians((double) Or.getKren())))) || AOA > maxAOA || CT.ElevatorControl > dA)
								CT.ElevatorControl -= 0.2F * f;
							else
								CT.ElevatorControl += 0.4F * f;
							CT.RudderControl = 1.0F * (float) Math.sin(Math.toRadians((double) (Or.getKren() - 55.0F)));
							if (mn_time > 4.5F)
								submaneuver++;
						break;
						case SUB_MAN3:
							direction = World.Rnd().nextFloat(-20.0F, -10.0F);
							submaneuver++;
							/* fall through */
						case SUB_MAN4:
							dA = 1.0F;
							maxAOA = 10.0F + 2.0F * (float) Skill;
							if (((double) this.getOverload() > 1.0 / Math.abs(Math.cos(Math.toRadians((double) Or.getKren())))) || AOA > maxAOA || CT.ElevatorControl > dA)
								CT.ElevatorControl -= 0.2F * f;
							else
								CT.ElevatorControl += 0.4F * f;
							CT.RudderControl = -1.0F * (float) Math.sin(Math.toRadians((double) (Or.getKren() + 55.0F)));
							if (!(mn_time > 6.0F))
								break;
							pop();
					}
				break;
				case 30:
					push(14);
					push(18);
					pop();
				break;
				case 31:
					push(14);
					push(19);
					pop();
				break;
				case 32:
					if (!this.isCapableOfACM() && World.Rnd().nextInt(-2, 9) < Skill)
						((Aircraft) actor).hitDaSilk();
					if (first)
					{
						AP.setStabAll(false);
						setSpeedMode(6);
						submaneuver = SUB_MAN0;
						direction = 178.0F - (World.Rnd().nextFloat(0.0F, 8.0F) * (float) Skill);
					}
					maxAOA = Vwld.z <= 0.0 ? 24.0F : 14.0F;
					if (danger != null)
					{
						Ve.sub(danger.Loc, Loc);
						dist = (float) Ve.length();
						Or.transformInv(Ve);
						Vpl.set(danger.Vwld);
						Or.transformInv(Vpl);
						Vpl.normalize();
					}
					switch (submaneuver)
					{
						case SUB_MAN0:
							dA = Or.getKren() - 180.0F;
							if (dA < -180.0F)
								dA += 360.0F;
							dA *= -0.08F;
							CT.AileronControl = dA;
							CT.RudderControl = dA <= 0.0F ? -1.0F : 1.0F;
							CT.ElevatorControl = 0.01111111F * Math.abs(Or.getKren());
							if (mn_time > 2.0F || Math.abs(Or.getKren()) > direction)
							{
								submaneuver++;
								CT.RudderControl = World.Rnd().nextFloat(-0.5F, 0.5F);
								direction = World.Rnd().nextFloat(-60.0F, -30.0F);
								mn_time = 0.5F;
							}
						break;
						case SUB_MAN1:
							dA = Or.getKren() - 180.0F;
							if (dA < -180.0F)
								dA += 360.0F;
							dA *= -0.04F;
							CT.AileronControl = dA;
							if (Or.getTangage() > direction + 5.0F && this.getOverload() < maxG && AOA < maxAOA)
							{
								if (CT.ElevatorControl < 0.0F)
									CT.ElevatorControl = 0.0F;
								CT.ElevatorControl += 1.0F * f;
							}
							else
							{
								if (CT.ElevatorControl > 0.0F)
									CT.ElevatorControl = 0.0F;
								CT.ElevatorControl -= 1.0F * f;
							}
							if (mn_time > 2.0F && Or.getTangage() < direction + 20.0F)
								submaneuver++;
							if (danger != null)
							{
								if (Skill >= 2 && Or.getTangage() < -30.0F && Vpl.x > 0.9986295104026794)
									submaneuver++;
								if (Skill >= 3 && Math.abs(Or.getTangage()) > 145.0F && Math.abs(danger.Or.getTangage()) > 135.0F && dist < 400.0F)
									submaneuver++;
							}
						break;
						case SUB_MAN2:
							direction = World.Rnd().nextFloat(-60.0F, 60.0F);
							setSpeedMode(6);
							submaneuver++;
							/* fall through */
						case SUB_MAN3:
							dA = Or.getKren() - direction;
							CT.AileronControl = -0.04F * dA;
							CT.RudderControl = dA <= 0.0F ? -1.0F : 1.0F;
							CT.ElevatorControl = 0.5F;
							if (Math.abs(dA) < 4.0F + 3.0F * (float) Skill)
								submaneuver++;
						break;
						case SUB_MAN4:
							direction *= World.Rnd().nextFloat(0.5F, 1.0F);
							setSpeedMode(6);
							submaneuver++;
							/* fall through */
						case SUB_MAN5:
							dA = Or.getKren() - direction;
							CT.AileronControl = -0.04F * dA;
							CT.RudderControl = -0.1F * this.getAOS();
							dA = 1.0F;
							if (this.getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA || Or.getTangage() > 40.0F)
								CT.ElevatorControl -= 0.8F * f;
							else
								CT.ElevatorControl += 1.6F * f;
							if (Or.getTangage() > 80.0F || mn_time > 4.0F || this.getSpeed() < Vmin * 1.5F)
								pop();
							if (danger != null && (Ve.z < -1.0 || dist > 600.0F || Vpl.x < 0.7880100011825562))
								pop();
						break;
					}
					if ((double) Alt < -7.0 * Vwld.z)
						pop();
				break;
				case 33:
					if (first)
					{
						if (Alt < 1000.0F)
						{
							pop();
							break;
						}
						AP.setStabAll(false);
						submaneuver = SUB_MAN0;
						direction = ((World.Rnd().nextBoolean() ? 1.0F : -1.0F) * World.Rnd().nextFloat(107.0F, 160.0F));
					}
					maxAOA = Vwld.z <= 0.0 ? 24.0F : 14.0F;
					switch (submaneuver)
					{
						case SUB_MAN0:
							if (Math.abs(Or.getKren()) < 45.0F)
								CT.ElevatorControl = 0.005555556F * Math.abs(Or.getKren());
							else if (this.getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > 1.0F)
								CT.ElevatorControl -= 0.2F * f;
							else
								CT.ElevatorControl += 1.2F * f;
							dA = Or.getKren() - direction;
							CT.AileronControl = -0.04F * dA;
							CT.RudderControl = -0.1F * this.getAOS();
							if (Math.abs(dA) < 4.0F + 1.0F * (float) Skill)
								submaneuver++;
						break;
						case SUB_MAN1:
							setSpeedMode(7);
							smConstPower = 0.5F;
							CT.AileronControl = 0.0F;
							CT.RudderControl = -0.1F * this.getAOS();
							dA = 1.0F;
							if (this.getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA)
								CT.ElevatorControl -= 0.2F * f;
							else
								CT.ElevatorControl += 1.2F * f;
							if (Or.getTangage() < -60.0F)
								submaneuver++;
						break;
						case SUB_MAN2:
							if (Or.getTangage() > -45.0F)
							{
								CT.AileronControl = -0.04F * Or.getKren();
								setSpeedMode(9);
								maxAOA = 7.0F;
							}
							CT.RudderControl = -0.1F * this.getAOS();
							dA = 1.0F;
							if (this.getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA)
								CT.ElevatorControl -= 0.8F * f;
							else
								CT.ElevatorControl += 0.4F * f;
							if (Or.getTangage() > AOA - 10.0F || Vwld.z > -1.0)
								pop();
						break;
					}
					if ((double) Alt < -7.0 * Vwld.z)
						pop();
				break;
				case 34:
					if (first)
					{
						if (Alt < 500.0F)
						{
							pop();
							break;
						}
						direction = Or.getTangage();
						setSpeedMode(9);
					}
					dA = Or.getKren() - (Or.getKren() <= 0.0F ? -35.0F : 35.0F);
					CT.AileronControl = -0.04F * dA;
					CT.RudderControl = Or.getKren() <= 0.0F ? -1.0F : 1.0F;
					CT.ElevatorControl = -1.0F;
					if (direction > Or.getTangage() + 45.0F || Or.getTangage() < -60.0F || mn_time > 4.0F)
						pop();
				break;
				case 36:
				case 37:
					if (first)
					{
						if (!this.isCapableOfACM())
							pop();
						if (this.getSpeed() < Vmax * 0.5F)
						{
							pop();
							break;
						}
						AP.setStabAll(false);
						submaneuver = SUB_MAN0;
						direction = World.Rnd().nextFloat(-30.0F, 80.0F);
						setSpeedMode(9);
					}
					maxAOA = Vwld.z <= 0.0 ? 24.0F : 14.0F;
					switch (submaneuver)
					{
						case SUB_MAN0:
							CT.AileronControl = -0.04F * Or.getKren();
							CT.RudderControl = -0.1F * this.getAOS();
							if (Math.abs(Or.getKren()) < 45.0F)
								submaneuver++;
						break;
						case SUB_MAN1:
							CT.AileronControl = 0.0F;
							dA = 1.0F;
							if (this.getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA)
								CT.ElevatorControl -= 0.4F * f;
							else
								CT.ElevatorControl += 0.8F * f;
							if (Or.getTangage() > direction)
								submaneuver++;
							if (this.getSpeed() < Vmin * 1.25F)
								pop();
						break;
						case SUB_MAN2:
							push(maneuver != 36 ? 35 : 7);
							pop();
					}
				break;
				case 38:
					if (first)
						CT.RudderControl = Or.getKren() <= 0.0F ? -1.0F : 1.0F;
					CT.AileronControl += -0.02F * Or.getKren();
					if (CT.AileronControl > 0.1F)
						CT.AileronControl = 0.1F;
					if (CT.AileronControl < -0.1F)
						CT.AileronControl = -0.1F;
					dA = (this.getSpeedKMH() - 180.0F - Or.getTangage() * 10.0F - this.getVertSpeed() * 5.0F) * 0.0040F;
					CT.ElevatorControl = dA;
					if (mn_time > 3.5F)
						pop();
				break;
				case 39:
					setSpeedMode(6);
					CT.AileronControl = -0.04F * Or.getKren();
					CT.ElevatorControl = -0.04F * (Or.getTangage() + 10.0F);
					if (CT.RudderControl > 0.1F)
						CT.RudderControl = 0.8F;
					else if (CT.RudderControl < -0.1F)
						CT.RudderControl = -0.8F;
					else
						CT.RudderControl = Or.getKren() <= 0.0F ? -1.0F : 1.0F;
					if (this.getSpeed() > Vmax || mn_time > 7.0F)
						pop();
				break;
				case 40:
					push(39);
					push(57);
					pop();
				break;
				case 41:
					push(13);
					push(18);
					pop();
				break;
				case 42:
					push(19);
					push(57);
					pop();
				break;
				case 46:
					if (target_ground == null || target_ground.pos == null)
					{
						if (Group != null)
						{
							dont_change_subm = true;
							boolean flag5 = isBusy();
							int j1 = Group.grTask;
//							AirGroup _tmp = Group;
							Group.grTask = 4;
							setBusy(false);
							Group.setTaskAndManeuver(Group.numInGroup((Aircraft) actor));
							setBusy(flag5);
							Group.grTask = j1;
						}
						if (target_ground == null || target_ground.pos == null)
						{
							AP.way.first();
							Airport airport = Airport.nearest(AP.way.curr().getP(), actor.getArmy(), 7);
							WayPoint waypoint;
							if (airport != null)
								waypoint = new WayPoint(airport.pos.getAbsPoint());
							else
								waypoint = new WayPoint(AP.way.first().getP());
							waypoint.set(0.6F * Vmax);
							waypoint.Action = 2;
							AP.way.add(waypoint);
							AP.way.last();
							set_task(3);
							clear_stack();
							maneuver = 21;
							set_maneuver(21);
							break;
						}
					}
					groundAttackKamikaze(target_ground, f);
				break;
				case 43:
				case 47:
				case 50:
				case 51:
				case 52:
				case 79:
				case 80:
				case 81:
					if (first && !this.isCapableOfACM())
					{
						bombsOut = true;
						this.setReadyToReturn(true);
						if (Group != null)
							Group.waitGroup(Group.numInGroup((Aircraft) actor));
						else
						{
							AP.way.next();
							set_task(3);
							clear_stack();
							set_maneuver(21);
						}
					}
					else
					{
						if (target_ground == null || target_ground.pos == null || !Actor.isAlive(target_ground))
						{
							int i1 = maneuver;
							if (Group != null)
							{
//								AirGroup _tmp1 = Group;
								if (Group.grTask == 4)
								{
									dont_change_subm = true;
									boolean flag6 = isBusy();
									setBusy(false);
									Group.setTaskAndManeuver(Group.numInGroup((Aircraft) actor));
									setBusy(flag6);
								}
							}
							if (target_ground == null || target_ground.pos == null || !Actor.isAlive(target_ground))
							{
								if (i1 == 50)
									bombsOut = true;
								if (Group != null)
									Group.waitGroup(Group.numInGroup((Aircraft) actor));
								else
								{
									AP.way.next();
									// TODO: GATTACK	set_task(3);
									// TODO: GATTACK	clear_stack();
									set_maneuver(21);
								}
								push(2);
								pop();
								break;
							}
						}
						switch (maneuver)
						{
							default:
							break;
							case 43:
								groundAttack(target_ground, f);
								if (mn_time > 120.0F)
									set_maneuver(0);
							break;
							case 50:
								groundAttackDiveBomber(target_ground, f);
								if (mn_time > 120.0F)
								{
									setSpeedMode(6);
									CT.BayDoorControl = 0.0F;
									CT.AirBrakeControl = 0.0F;
									CT.FlapsControl = 0.0F;
									pop();
									sub_Man_Count = 0;
								}
							break;
							case 51:
								groundAttackTorpedo(target_ground, f);
							break;
							case 81:
								groundAttackTorpedoToKG(target_ground, f);
							break;
							case 52:
								groundAttackCassette(target_ground, f);
							break;
							case 46:
								groundAttackKamikaze(target_ground, f);
							break;
							case 47:
								groundAttackTinyTim(target_ground, f);
							break;
							case 79:
								groundAttackHS293(target_ground, f);
							break;
							case 80:
								groundAttackFritzX(target_ground, f);
						}
					}
			}
			if (checkGround)
				doCheckGround(f);
			if (checkStrike && strikeEmer)
				doCheckStrike();
			strikeEmer = false;
			setSpeedControl(f);
			first = false;
			mn_time += f;
			if (frequentControl)
				AP.update(f);
			else
				AP.update(f * 4.0F);
			if (bBusy)
				wasBusy = true;
			else
				wasBusy = false;
			if (shotAtFriend > 0)
				shotAtFriend--;
		}
	}

	void OutCT(int i)
	{
		if (actor == Main3D.cur3D().viewActor())
		{
			TextScr.output(i + 5, 45, ("Alt(MSL)  " + (int) Loc.z + "    " + (CT.BrakeControl <= 0.0F ? "" : "BRAKE")));
			TextScr.output(i + 5, 65, ("Alt(AGL)  " + (int) (Loc.z - Engine.land().HQ_Air(Loc.x, Loc.y))));
			int j = 0;
			TextScr.output(i + 225, 140, ("---ENGINES (" + EI.getNum() + ")---" + EI.engines[j].getStage()));
			TextScr.output(i + 245, 120, ("THTL " + (int) (100.0F * EI.engines[j].getControlThrottle()) + "%" + (EI.engines[j].getControlAfterburner() ? " (NITROS)" : "")));
			TextScr.output(i + 245, 100, ("PROP " + (int) (100.0F * EI.engines[j].getControlProp()) + "%" + (CT.getStepControlAuto() ? " (AUTO)" : "")));
			TextScr.output(i + 245, 80, ("MIX " + (int) (100.0F * EI.engines[j].getControlMix()) + "%"));
			TextScr.output(i + 245, 60, ("RAD " + (int) (100.0F * EI.engines[j].getControlRadiator()) + "%" + (CT.getRadiatorControlAuto() ? " (AUTO)" : "")));
			TextScr.output(i + 245, 40, ("SUPC " + EI.engines[j].getControlCompressor() + "x"));
			TextScr.output(245, 20, ("PropAoA :" + (int) (Math.toDegrees((double) EI.engines[j].getPropAoA()))));
			TextScr.output(i + 455, 120, ("Cyls/Cams " + EI.engines[j].getCylindersOperable() + "/" + EI.engines[0].getCylinders()));
			TextScr.output(i + 455, 100, ("Readyness " + (int) (100.0F * EI.engines[j].getReadyness()) + "%"));
			TextScr.output(i + 455, 80, ("PRM " + (int) ((float) (int) (EI.engines[j].getRPM() * 0.02F) * 50.0F) + " rpm"));
			TextScr.output(i + 455, 60, ("Thrust " + (int) EI.engines[j].getEngineForce().x + " N"));
			TextScr.output(i + 455, 40, ("Fuel " + (int) (100.0F * M.fuel / M.maxFuel) + "% Nitro " + (int) (100.0F * M.nitro / M.maxNitro) + "%"));
			TextScr.output(i + 455, 20, ("MPrs " + (int) (1000.0F * EI.engines[j].getManifoldPressure()) + " mBar"));
			TextScr.output(i + 640, 140, "---Controls---");
			TextScr.output(i + 640, 120,
					("A/C: " + (CT.bHasAileronControl ? "" : "AIL ") + (CT.bHasElevatorControl ? "" : "ELEV ") + (CT.bHasRudderControl ? "" : "RUD ") + (Gears.bIsHydroOperable ? "" : "GEAR ")));
			TextScr.output(i + 640, 100, ("ENG: " + (EI.engines[j].isHasControlThrottle() ? "" : "THTL ") + (EI.engines[j].isHasControlProp() ? "" : "PROP ")
					+ (EI.engines[j].isHasControlMix() ? "" : "MIX ") + (EI.engines[j].isHasControlCompressor() ? "" : "SUPC ") + (EI.engines[j].isPropAngleDeviceOperational() ? "" : "GVRNR ")));
			TextScr.output(i + 640, 80, "PIL: (" + (int) (AS.getPilotHealth(0) * 100.0F) + "%)");
			TextScr.output(i + 5, 105, "V   " + (int) this.getSpeedKMH());
			TextScr.output(i + 5, 125, "AOA " + ((float) (int) (this.getAOA() * 1000.0F) / 1000.0F));
			TextScr.output(i + 5, 145, "AOS " + ((float) (int) (this.getAOS() * 1000.0F) / 1000.0F));
			TextScr.output(i + 5, 165, "Kr  " + (int) Or.getKren());
			TextScr.output(i + 5, 185, "Ta  " + (int) Or.getTangage());
			TextScr.output(i + 250, 185, ("way.speed  " + AP.way.curr().getV() * 3.6F + "way.z " + AP.way.curr().z() + "  mn_time = " + mn_time));
			TextScr.output(i + 5, 205, ("<" + actor.name() + ">: " + ManString.tname(task) + ":" + ManString.name(maneuver) + " , WP=" + AP.way.Cur() + "(" + (AP.way.size() - 1) + ")-" + ManString
					.wpname(AP.way.curr().Action)));
			TextScr.output(i + 7, 225, ("======= " + ManString.name(program[0]) + "  Sub = " + submaneuver + " follOffs.x = " + followOffset.x + "  "
					+ (((AutopilotAI) AP).bWayPoint ? "Stab WPoint " : "") + (((AutopilotAI) AP).bStabAltitude ? "Stab ALT " : "") + (((AutopilotAI) AP).bStabDirection ? "Stab DIR " : "")
					+ (((AutopilotAI) AP).bStabSpeed ? "Stab SPD " : "   ") + (((Pilot) ((Aircraft) actor).FM).isDumb() ? "DUMB " : " ") + (((Pilot) ((Aircraft) actor).FM).Gears.lgear ? "L " : " ")
					+ (((Pilot) ((Aircraft) actor).FM).Gears.rgear ? "R " : " ") + (((Pilot) ((Aircraft) actor).FM).TaxiMode ? "TaxiMode" : "")));
			TextScr.output(i + 7, 245, " ====== " + ManString.name(program[1]));
			TextScr.output(i + 7, 265, "  ===== " + ManString.name(program[2]));
			TextScr.output(i + 7, 285, "   ==== " + ManString.name(program[3]));
			TextScr.output(i + 7, 305, "    === " + ManString.name(program[4]));
			TextScr.output(i + 7, 325, "     == " + ManString.name(program[5]));
			TextScr.output(i + 7, 345, ("      = " + ManString.name(program[6]) + "  " + (target != null ? "TARGET  " : "") + (target_ground != null ? "GROUND  " : "") + (danger != null ? "DANGER  "
					: "")));
			if (target != null && Actor.isValid(target.actor))
				TextScr.output(i + 1, 365, (" AT: (" + target.actor.name() + ") " + (target.actor instanceof Aircraft ? "" : target.actor.getClass().getName())));
			if (target_ground != null && Actor.isValid(target_ground))
				TextScr.output(i + 1, 385, (" GT: (" + target_ground.name() + ") ..." + target_ground.getClass().getName()));
			TextScr.output(400, 500, "+");
			TextScr.output(400, 400, "|");
			TextScr.output((int) (400.0F + 200.0F * CT.AileronControl), (int) (500.0F - 200.0F * CT.ElevatorControl), "+");
			TextScr.output((int) (400.0F + 200.0F * CT.RudderControl), 400, "|");
			TextScr.output(250, 780, ("followOffset  " + followOffset.x + "  " + followOffset.y + "  " + followOffset.z + "  "));
			if (Group != null && Group.enemies != null)
				TextScr.output(250, 760, ("Enemies   " + AirGroupList.length(Group.enemies[0])));
			TextScr.output(700, 780, "speedMode   " + speedMode);
			if (Group != null)
				TextScr.output(700, 760, "Group task  " + Group.grTaskName());
			if (AP.way.isLandingOnShip())
				TextScr.output(5, 460, "Landing On Carrier");
			TextScr.output(700, 740, "gattackCounter  " + gattackCounter);
			TextScr.output(5, 360, "silence = " + silence);
		}
	}

	private void groundAttackGuns(Actor actor, float f)
	{
		if (submaneuver == SUB_MAN0 && sub_Man_Count == 0 && CT.Weapons[1] != null)
		{
			float f1 = ((GunGeneric) CT.Weapons[1][0]).bulletSpeed();
			if (f1 > 0.01F)
				bullTime = 1.0F / (f1 + this.getSpeed());
		}
		maxAOA = 15.0F;
		minElevCoeff = 20.0F;
		switch (submaneuver)
		{
			case SUB_MAN0:
			{
				setCheckGround(true);
				rocketsDelay = 0;
				if (sub_Man_Count == 0)
				{
					Vtarg.set(actor.pos.getAbsPoint());
					actor.getSpeed(tmpV3d);
					tmpV3f.x = (double) (float) tmpV3d.x;
					tmpV3f.y = (double) (float) tmpV3d.y;
					tmpV3f.z = (double) (float) tmpV3d.z;
					tmpV3f.z = 0.0;
					if (tmpV3f.length() < 9.999999747378752E-5)
					{
						tmpV3f.sub(Vtarg, Loc);
						tmpV3f.z = 0.0;
					}
					tmpV3f.normalize();
					Vtarg.x -= tmpV3f.x * 1500.0;
					Vtarg.y -= tmpV3f.y * 1500.0;
					Vtarg.z += 400.0;
					constVtarg.set(Vtarg);
					Ve.sub(constVtarg, Loc);
					Ve.normalize();
					Vxy.cross(Ve, tmpV3f);
					Ve.sub(tmpV3f);
					Vtarg.z += 100.0;
					if (Vxy.z > 0.0)
					{
						Vtarg.x += Ve.y * 1000.0;
						Vtarg.y -= Ve.x * 1000.0;
					}
					else
					{
						Vtarg.x -= Ve.y * 1000.0;
						Vtarg.y += Ve.x * 1000.0;
					}
					constVtarg1.set(Vtarg);
				}
				Ve.set(constVtarg1);
				Ve.sub(Loc);
				float f2 = (float) Ve.length();
				Or.transformInv(Ve);
				if (Ve.x < 0.0)
				{
					push(0);
					push(8);
					pop();
					dontSwitch = true;
				}
				else
				{
					Ve.normalize();
					setSpeedMode(4);
					smConstSpeed = 100.0F;
					farTurnToDirection();
					sub_Man_Count++;
					if (f2 < 300.0F)
					{
						submaneuver++;
						gattackCounter++;
						sub_Man_Count = 0;
					}
				}
				break;
			}
			case SUB_MAN1:
			{
				Ve.set(constVtarg);
				Ve.sub(Loc);
				float f3 = (float) Ve.length();
				Or.transformInv(Ve);
				Ve.normalize();
				setSpeedMode(4);
				smConstSpeed = 100.0F;
				farTurnToDirection();
				sub_Man_Count++;
				if (f3 < 300.0F)
				{
					submaneuver++;
					gattackCounter++;
					sub_Man_Count = 0;
				}
				break;
			}
			case SUB_MAN2:
			{
				if (rocketsDelay > 0)
					rocketsDelay--;
				if (sub_Man_Count > 100)
					setCheckGround(false);
				P.set(actor.pos.getAbsPoint());
				P.z += 4.0;
				Engine.land();
				if (Landscape.rayHitHQ(Loc, P, P))
				{
					push(0);
					push(38);
					pop();
					gattackCounter--;
					if (gattackCounter < 0)
						gattackCounter = 0;
				}
				Ve.sub(actor.pos.getAbsPoint(), Loc);
				Vtarg.set(Ve);
				float f4 = (float) Ve.length();
				actor.getSpeed(tmpV3d);
				tmpV3f.x = (double) (float) tmpV3d.x;
				tmpV3f.y = (double) (float) tmpV3d.y;
				tmpV3f.z = (double) (float) tmpV3d.z;
				tmpV3f.sub(Vwld);
				tmpV3f.scale((double) (f4 * bullTime * 0.3333F * (float) Skill));
				Ve.add(tmpV3f);
				float f5 = 0.3F * (f4 - 1000.0F);
				if (f5 < 0.0F)
					f5 = 0.0F;
				if (f5 > 300.0F)
					f5 = 300.0F;
				f5 += f4 * this.getAOA() * 0.0050F;
				Ve.z += (double) (f5 + (World.cur().rnd.nextFloat(-3.0F, 3.0F) * (float) (3 - Skill)));
				Or.transformInv(Ve);
				if (f4 < 800.0F && (shotAtFriend <= 0 || distToFriend > f4) && Math.abs(Ve.y) < 15.0 && Math.abs(Ve.z) < 10.0)
				{
					if (f4 < 550.0F)
						CT.WeaponControl[0] = true;
					if (f4 < 450.0F)
						CT.WeaponControl[1] = true;
					if (CT.Weapons[2] != null && CT.Weapons[2][0] != null && (CT.Weapons[2][CT.Weapons[2].length - 1].countBullets() != 0) && rocketsDelay < 1 && f4 < 500.0F)
					{
						CT.WeaponControl[2] = true;
						Voice.speakAttackByRockets((Aircraft) this.actor);
						rocketsDelay += 30;
					}
				}
				if (sub_Man_Count > 200 && Ve.x < 200.0 || Alt < 40.0F)
				{
					if (Leader == null || !Actor.isAlive(Leader.actor))
						Voice.speakEndGattack((Aircraft) this.actor);
					rocketsDelay = 0;
					push(0);
					push(55);
					push(10);
					pop();
					dontSwitch = true;
				}
				else
				{
					Ve.normalize();
					attackTurnToDirection(f4, f, 4.0F + (float) Skill * 2.0F);
					setSpeedMode(4);
					smConstSpeed = 100.0F;
					break;
				}
				break;
			}
			default:
				submaneuver = SUB_MAN0;
				sub_Man_Count = 0;
		}
	}

	private void groundAttack(Actor actor, float f)
	{
		setSpeedMode(4);
		smConstSpeed = 120.0F;
		float f3 = Vwld.z <= 0.0 ? 4.0F : 3.0F;
		boolean flag = false;
		boolean flag1 = false;
		if (CT.Weapons[3] != null && CT.Weapons[3][0] != null && CT.Weapons[3][0].countBullets() != 0)
		{
			flag = true;
			if (CT.Weapons[3][0] instanceof ParaTorpedoGun)
				flag1 = true;
		}
		else if (!(this.actor instanceof TypeStormovik) && !(this.actor instanceof TypeFighter) && !(this.actor instanceof TypeDiveBomber))
		{
			set_maneuver(0);
			return;
		}
		Ve.set(actor.pos.getAbsPoint());
		if (flag1)
		{
			if (CT.Weapons[3][0] instanceof BombGunTorp45_36AV_A)
				Ve.z = Loc.z - 100.0 + World.Rnd().nextDouble() * 50.0;
			else
				Ve.z = Loc.z - 200.0 + World.Rnd().nextDouble() * 50.0;
		}
		float f4 = (float) Loc.z - (float) Ve.z;
		if (f4 < 0.0F)
			f4 = 0.0F;
		float f5 = (float) Math.sqrt((double) (2.0F * f4 * 0.1019F)) + 0.0017F * f4;
		actor.getSpeed(tmpV3d);
		if (actor instanceof Aircraft && tmpV3d.length() > 20.0)
		{
			target = ((Aircraft) actor).FM;
			set_task(6);
			clear_stack();
			set_maneuver(27);
			dontSwitch = true;
		}
		else
		{
			float f6 = 0.5F;
			if (flag)
				f6 = (f5 + 5.0F) * 0.33333F;
			Vtarg.x = (double) ((float) tmpV3d.x * f6 * (float) Skill);
			Vtarg.y = (double) ((float) tmpV3d.y * f6 * (float) Skill);
			Vtarg.z = (double) ((float) tmpV3d.z * f6 * (float) Skill);
			Ve.add(Vtarg);
			Ve.sub(Loc);
			if (flag)
				addWindCorrection();
			Ve.add(0.0, 0.0, (double) (-0.5F + World.Rnd().nextFloat(-2.0F, 0.8F)));
			Vf.set(Ve);
			float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
			if (flag)
			{
				float f7 = this.getSpeed() * f5 + 500.0F;
				if (f1 > f7)
					Ve.z += 200.0;
				else
					Ve.z = 0.0;
			}
			Vtarg.set(Ve);
			Vtarg.normalize();
			Or.transformInv(Ve);
			if (!flag)
				groundAttackGuns(actor, f);
			//TODO: Altered by GATTACK mod
			//-------------------------------------------
			else if ((super.actor instanceof TypeFighter) || (super.actor instanceof TypeStormovik))
			{
				passCounter = 0;
				bombsOutCounter = 129;
				groundAttackShallowDive(actor, f);
			}
			//-------------------------------------------
			else
			{
				Ve.normalize();
				Vpl.set(Vwld);
				Vpl.normalize();
				CT.BayDoorControl = 1.0F;
				if (f1 + 4.0F * f5 < this.getSpeed() * f5 && Ve.x > 0.0 && Vpl.dot(Vtarg) > 0.9800000190734863)
				{
					if (!bombsOut)
					{
						bombsOut = true;
						if (CT.Weapons[3] != null && CT.Weapons[3][0] != null && CT.Weapons[3][0].countBullets() != 0 && !(CT.Weapons[3][0] instanceof BombGunPara))
							Voice.speakAttackByBombs((Aircraft) this.actor);
					}
					push(0);
					push(55);
					push(48);
					pop();
					Voice.speakEndGattack((Aircraft) this.actor);
					CT.BayDoorControl = 0.0F;
				}
				if (Ve.x < 0.0)
				{
					push(0);
					push(55);
					push(10);
					pop();
				}
				if (Math.abs(Ve.y) > 0.10000000149011612)
					CT.AileronControl = (-(float) Math.atan2(Ve.y, Ve.z) - 0.016F * Or.getKren());
				else
					CT.AileronControl = (-(float) Math.atan2(Ve.y, Ve.x) - 0.016F * Or.getKren());
				if (Math.abs(Ve.y) > 0.0010000000474974513)
					CT.RudderControl = -98.0F * (float) Math.atan2(Ve.y, Ve.x);
				else
					CT.RudderControl = 0.0F;
				if ((double) CT.RudderControl * W.z > 0.0)
					W.z = 0.0;
				else
					W.z *= 1.0399999618530273;
				float f2 = (float) Math.atan2(Ve.z, Ve.x);
				if (Math.abs(Ve.y) < 0.0020000000949949026 && Math.abs(Ve.z) < 0.0020000000949949026)
					f2 = 0.0F;
				if (Ve.x < 0.0)
					f2 = 1.0F;
				else
				{
					if ((double) f2 * W.y > 0.0)
						W.y = 0.0;
					if (f2 < 0.0F)
					{
						if (this.getOverload() < 0.1F)
							f2 = 0.0F;
						if (CT.ElevatorControl > 0.0F)
							CT.ElevatorControl = 0.0F;
					}
					else if (CT.ElevatorControl < 0.0F)
						CT.ElevatorControl = 0.0F;
				}
				if (this.getOverload() > maxG || AOA > f3 || CT.ElevatorControl > f2)
					CT.ElevatorControl -= 0.2F * f;
				else
					CT.ElevatorControl += 0.2F * f;
			}
		}
	}

	private void groundAttackKamikaze(Actor actor, float f)
	{
		if (submaneuver == SUB_MAN0 && sub_Man_Count == 0 && CT.Weapons[1] != null)
		{
			float f1 = ((GunGeneric) CT.Weapons[1][0]).bulletSpeed();
			if (f1 > 0.01F)
				bullTime = 1.0F / f1;
		}
		maxAOA = 15.0F;
		minElevCoeff = 20.0F;
		switch (submaneuver)
		{
			case SUB_MAN0:
			{
				setCheckGround(true);
				rocketsDelay = 0;
				if (sub_Man_Count == 0)
				{
					Vtarg.set(actor.pos.getAbsPoint());
					tmpV3f.set(Vwld);
					tmpV3f.x += (double) World.Rnd().nextFloat(-100.0F, 100.0F);
					tmpV3f.y += (double) World.Rnd().nextFloat(-100.0F, 100.0F);
					tmpV3f.z = 0.0;
					if (tmpV3f.length() < 9.999999747378752E-5)
					{
						tmpV3f.sub(Vtarg, Loc);
						tmpV3f.z = 0.0;
					}
					tmpV3f.normalize();
					Vtarg.x -= tmpV3f.x * 1500.0;
					Vtarg.y -= tmpV3f.y * 1500.0;
					Vtarg.z += 400.0;
					constVtarg.set(Vtarg);
					Ve.sub(constVtarg, Loc);
					Ve.normalize();
					Vxy.cross(Ve, tmpV3f);
					Ve.sub(tmpV3f);
					Vtarg.z += 100.0;
					if (Vxy.z > 0.0)
					{
						Vtarg.x += Ve.y * 1000.0;
						Vtarg.y -= Ve.x * 1000.0;
					}
					else
					{
						Vtarg.x -= Ve.y * 1000.0;
						Vtarg.y += Ve.x * 1000.0;
					}
					constVtarg1.set(Vtarg);
				}
				Ve.set(constVtarg1);
				Ve.sub(Loc);
				float f2 = (float) Ve.length();
				Or.transformInv(Ve);
				if (Ve.x < 0.0)
				{
					push(0);
					push(8);
					pop();
					dontSwitch = true;
				}
				else
				{
					Ve.normalize();
					setSpeedMode(6);
					farTurnToDirection();
					sub_Man_Count++;
					if (f2 < 300.0F)
					{
						submaneuver++;
						gattackCounter++;
						sub_Man_Count = 0;
					}
					if (sub_Man_Count > 1000)
						sub_Man_Count = 0;
				}
				break;
			}
			case SUB_MAN1:
			{
				setCheckGround(true);
				Ve.set(constVtarg);
				Ve.sub(Loc);
				float f3 = (float) Ve.length();
				Or.transformInv(Ve);
				Ve.normalize();
				setSpeedMode(6);
				farTurnToDirection();
				sub_Man_Count++;
				if (f3 < 300.0F)
				{
					submaneuver++;
					gattackCounter++;
					sub_Man_Count = 0;
				}
				if (sub_Man_Count > 700)
				{
					submaneuver++;
					sub_Man_Count = 0;
				}
				break;
			}
			case SUB_MAN2:
			{
				setCheckGround(false);
				if (rocketsDelay > 0)
					rocketsDelay--;
				if (sub_Man_Count > 100)
					setCheckGround(false);
				Ve.set(actor.pos.getAbsPoint());
				Ve.sub(Loc);
				Vtarg.set(Ve);
				float f4 = (float) Ve.length();
				if (this.actor instanceof MXY_7)
				{
					Ve.z += 0.01 * (double) f4;
					Vtarg.z += 0.01 * (double) f4;
				}
				actor.getSpeed(tmpV3d);
				tmpV3f.x = (double) (float) tmpV3d.x;
				tmpV3f.y = (double) (float) tmpV3d.y;
				tmpV3f.z = (double) (float) tmpV3d.z;
				tmpV3f.sub(Vwld);
				tmpV3f.scale((double) (f4 * bullTime * 0.3333F * (float) Skill));
				Ve.add(tmpV3f);
				float f5 = 0.3F * (f4 - 1000.0F);
				if (f5 < 0.0F)
					f5 = 0.0F;
				if (f5 > 300.0F)
					f5 = 300.0F;
				Ve.z += (double) (f5 + (World.cur().rnd.nextFloat(-3.0F, 3.0F) * (float) (3 - Skill)));
				Or.transformInv(Ve);
				if (f4 < 50.0F && Math.abs(Ve.y) < 40.0 && Math.abs(Ve.z) < 30.0)
				{
					CT.WeaponControl[0] = true;
					CT.WeaponControl[1] = true;
					CT.WeaponControl[2] = true;
					CT.WeaponControl[3] = true;
				}
				if (Ve.x < -50.0)
				{
					rocketsDelay = 0;
					push(0);
					push(55);
					push(10);
					pop();
					dontSwitch = true;
				}
				else
				{
					Ve.normalize();
					attackTurnToDirection(f4, f, 4.0F + (float) Skill * 2.0F);
					setSpeedMode(4);
					smConstSpeed = 130.0F;
					break;
				}
				break;
			}
			default:
				submaneuver = SUB_MAN0;
				sub_Man_Count = 0;
		}
	}

	private void groundAttackTinyTim(Actor actor, float f)
	{
		maxG = 5.0F;
		maxAOA = 8.0F;
		setSpeedMode(4);
		smConstSpeed = 120.0F;
		minElevCoeff = 20.0F;
		switch (submaneuver)
		{
			case SUB_MAN0:
			{
				if (sub_Man_Count == 0)
				{
					Vtarg.set(actor.pos.getAbsPoint());
					actor.getSpeed(tmpV3d);
					if (tmpV3d.length() < 0.5)
						tmpV3d.sub(Vtarg, Loc);
					tmpV3d.normalize();
					Vtarg.x -= tmpV3d.x * 3000.0;
					Vtarg.y -= tmpV3d.y * 3000.0;
					Vtarg.z += 500.0;
				}
				Ve.sub(Vtarg, Loc);
				double d = Ve.length();
				Or.transformInv(Ve);
				sub_Man_Count++;
				if (d < 1000.0)
				{
					submaneuver++;
					sub_Man_Count = 0;
				}
				Ve.normalize();
				farTurnToDirection();
				break;
			}
			case SUB_MAN1:
			{
				Vtarg.set(actor.pos.getAbsPoint());
				Vtarg.z += 80.0;
				Ve.sub(Vtarg, Loc);
				double d1 = Ve.length();
				Or.transformInv(Ve);
				sub_Man_Count++;
				if (d1 < 1500.0)
				{
					if (Math.abs(Ve.y) < 40.0 && Math.abs(Ve.z) < 30.0)
						CT.WeaponControl[2] = true;
					push(0);
					push(10);
					push(48);
					pop();
					dontSwitch = true;
				}
				if (d1 < 500.0 && Ve.x < 0.0)
				{
					push(0);
					push(10);
					pop();
				}
				Ve.normalize();
				if (Ve.x < 0.800000011920929)
					turnToDirection(f);
				else
					attackTurnToDirection((float) d1, f, 2.0F + (float) Skill * 1.5F);
				break;
			}
			default:
				submaneuver = SUB_MAN0;
				sub_Man_Count = 0;
		}
	}

	private void groundAttackHS293(Actor actor, float f)
	{
		maxG = 5.0F;
		maxAOA = 8.0F;
		setSpeedMode(4);
		smConstSpeed = 120.0F;
		minElevCoeff = 20.0F;
		switch (submaneuver)
		{
			case SUB_MAN0:
			{
				if (sub_Man_Count == 0)
				{
					Vtarg.set(actor.pos.getAbsPoint());
					actor.getSpeed(tmpV3d);
					if (tmpV3d.length() < 0.5)
						tmpV3d.sub(Vtarg, Loc);
					tmpV3d.normalize();
					Vtarg.x -= tmpV3d.x * 3000.0;
					Vtarg.y -= tmpV3d.y * 3000.0;
					Vtarg.z += 500.0;
				}
				Ve.sub(Vtarg, Loc);
				double d = Ve.length();
				Or.transformInv(Ve);
				sub_Man_Count++;
				if (d < 10000.0)
				{
					submaneuver++;
					sub_Man_Count = 0;
				}
				Ve.normalize();
				farTurnToDirection();
				break;
			}
			case SUB_MAN1:
			{
				Vtarg.set(actor.pos.getAbsPoint());
				Vtarg.z += 2000.0;
				Ve.sub(Vtarg, Loc);
				double d1 = Ve.angle(Vwld);
				Ve.z = 0.0;
				double d2 = Ve.length();
				Or.transformInv(Ve);
				sub_Man_Count++;
				TypeGuidedBombCarrier typeguidedbombcarrier = (TypeGuidedBombCarrier) this.actor;
				if (d2 > 2000.0 && d2 < 6500.0 && d1 < 0.9 && !typeguidedbombcarrier.typeGuidedBombCgetIsGuiding())
				{
					CT.WeaponControl[3] = true;
					push(0);
					push(10);
					pop();
					dontSwitch = true;
				}
				if (d2 < 500.0 && Ve.x < 0.0)
				{
					push(0);
					push(10);
					pop();
				}
				Ve.normalize();
				if (Ve.x < 99999.80000001192)
					turnToDirection(f);
				else
					attackTurnToDirection((float) d2, f, 2.0F + (float) Skill * 1.5F);
				break;
			}
			default:
				submaneuver = SUB_MAN0;
				sub_Man_Count = 0;
		}
	}

	private void groundAttackFritzX(Actor actor, float f)
	{
		maxG = 5.0F;
		maxAOA = 8.0F;
		setSpeedMode(4);
		smConstSpeed = 140.0F;
		minElevCoeff = 20.0F;
		switch (submaneuver)
		{
			case SUB_MAN0:
			{
				if (sub_Man_Count == 0)
				{
					Vtarg.set(actor.pos.getAbsPoint());
					actor.getSpeed(tmpV3d);
					if (tmpV3d.length() < 0.5)
						tmpV3d.sub(Vtarg, Loc);
					tmpV3d.normalize();
					Vtarg.x -= tmpV3d.x * 3000.0;
					Vtarg.y -= tmpV3d.y * 3000.0;
					Vtarg.z += 500.0;
				}
				Ve.sub(Vtarg, Loc);
				double d = Ve.length();
				Or.transformInv(Ve);
				sub_Man_Count++;
				if (d < 15000.0)
				{
					submaneuver++;
					sub_Man_Count = 0;
				}
				Ve.normalize();
				farTurnToDirection();
				break;
			}
			case SUB_MAN1:
			{
				Vtarg.set(actor.pos.getAbsPoint());
				Vtarg.z += 2000.0;
				Ve.sub(Vtarg, Loc);
				double d1 = Ve.angle(Vwld);
				Ve.z = 0.0;
				double d2 = Ve.length();
				Or.transformInv(Ve);
				sub_Man_Count++;
				TypeGuidedBombCarrier typeguidedbombcarrier = (TypeGuidedBombCarrier) this.actor;
				if (d2 < 4000.0 && d1 < 0.9 && (d2 < 2000.0 || d1 > 0.4) && !typeguidedbombcarrier.typeGuidedBombCgetIsGuiding())
				{
					CT.WeaponControl[3] = true;
					setSpeedMode(5);
					push(0);
					push(10);
					pop();
					dontSwitch = true;
				}
				if (d2 < 500.0 && Ve.x < 0.0)
				{
					push(0);
					push(10);
					pop();
				}
				Ve.normalize();
				if (Ve.x < 99999.80000001192)
					turnToDirection(f);
				else
					attackTurnToDirection((float) d2, f, 2.0F + (float) Skill * 1.5F);
				break;
			}
			default:
				submaneuver = SUB_MAN0;
				sub_Man_Count = 0;
		}
	}

	private void groundAttackShallowDive(Actor actor, float f)
	{
		maxAOA = 10.0F;
		if (!hasBombs())
		{
			set_maneuver(0);
			wingman(true);
		}
		else
		{
			if (first)
				RandomVal = 50.0F - World.cur().rnd.nextFloat(0.0F, 100.0F);
			setSpeedMode(4);
			smConstSpeed = 120.0F;
			Ve.set(actor.pos.getAbsPoint());
			Ve.sub(Loc);
			addWindCorrection();
			float f1 = (float) -Ve.z;
			if (f1 < 0.0F)
				f1 = 0.0F;
			Ve.z += 250.0;
			float f2 = ((float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y) + RandomVal * (float) (3 - Skill));
			if (Ve.z < (double) (-0.1F * f2))
				Ve.z = (double) (-0.1F * f2);
			if ((double) Alt + Ve.z < 250.0)
				Ve.z = (double) (250.0F - Alt);
			if (Alt < 50.0F)
			{
				push(10);
				pop();
			}
			Vf.set(Ve);
			CT.BayDoorControl = 1.0F;
			float f3 = (float) Vwld.z * 0.1019F;
			f3 += (float) Math.sqrt((double) (f3 * f3 + 2.0F * f1 * 0.1019F));
			float f4 = (float) Math.sqrt(Vwld.x * Vwld.x + Vwld.y * Vwld.y);
			float f5 = f4 * f3 + 10.0F;
			actor.getSpeed(tmpV3d);
			tmpV3d.scale((double) f3 * 0.35 * (double) Skill);
			Ve.x += (double) (float) tmpV3d.x;
			Ve.y += (double) (float) tmpV3d.y;
			Ve.z += (double) (float) tmpV3d.z;
			//TODO: Modified by GATTACK MOD
			//-----------------------------------------
			if ((f5 >= f2) && (passCounter == 0))
			{
				bombsOut = true;
				bombsOutCounter = 129;
				Voice.speakAttackByBombs((Aircraft) this.actor);
				setSpeedMode(6);
				CT.BayDoorControl = 0.0F;
				pop();
				sub_Man_Count = 0;
				passCounter++;
			}
			else if (passCounter >= 5)
			{
				passCounter = 0;
			}
			//-----------------------------------------
			Or.transformInv(Ve);
			Ve.normalize();
			turnToDirection(f);
		}
	}

	private void groundAttackDiveBomber(Actor actor, float f)
	{
		maxG = 5.0F;
		maxAOA = 10.0F;
		setSpeedMode(6);
		maxAOA = 4.0F;
		minElevCoeff = 20.0F;
		if (CT.Weapons[3] == null || CT.getWeaponCount(3) == 0)
		{
			if (AP.way.curr().Action == 3)
				AP.way.next();
			set_maneuver(0);
			wingman(true);
		}
		else
		{
			if (Alt < 350.0F)
			{
				bombsOut = true;
				Voice.speakAttackByBombs((Aircraft) this.actor);
				setSpeedMode(6);
				CT.BayDoorControl = 0.0F;
				CT.AirBrakeControl = 0.0F;
				AP.way.next();
				push(0);
				push(8);
				push(2);
				pop();
				sub_Man_Count = 0;
			}
			Ve.set(actor.pos.getAbsPoint());
			Ve.sub(Loc);
			float f5 = (float) -Ve.z;
			float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
			if (f1 > 1000.0F || submaneuver == SUB_MAN3 && sub_Man_Count > 100)
			{
				Vtarg.set(actor.pos.getAbsPoint());
				actor.getSpeed(tmpV3d);
				float f6 = 0.0F;
				switch (submaneuver)
				{
					case SUB_MAN0:
						f6 = f1 / 40.0F + 4.0F + Alt / 48.0F;
						/* fall through */
					case SUB_MAN1:
						f6 = ((f1 - man1Dist) / (float) Vwld.length() + 4.0F + Alt / 48.0F);
						/* fall through */
					case SUB_MAN2:
						f6 = Alt / 60.0F;
						/* fall through */
					case SUB_MAN3:
						f6 = Alt / 120.0F;
						/* fall through */
					default:
						f6 *= 0.33333F;
						Vtarg.x += (double) ((float) tmpV3d.x * f6 * (float) Skill);
						Vtarg.y += (double) ((float) tmpV3d.y * f6 * (float) Skill);
						Vtarg.z += (double) ((float) tmpV3d.z * f6 * (float) Skill);
				}
			}
			Ve.set(Vtarg);
			Ve.sub(Loc);
			float f4 = (float) -Ve.z;
			if (f4 < 0.0F)
				f4 = 0.0F;
			Ve.add(Vxy);
			f5 = (float) -Ve.z;
			f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
			if (submaneuver < SUB_MAN2)
				Ve.z = 0.0;
			Vf.set(Ve);
			Ve.normalize();
			Vpl.set(Vwld);
			Vpl.normalize();
			switch (submaneuver)
			{
				default:
				break;
				case SUB_MAN0:
				{
					push();
					pop();
					if (f5 < 1200.0F)
						man1Dist = 400.0F;
					else if (f5 > 4500.0F)
						man1Dist = 50.0F;
					else
						man1Dist = 50.0F + 350.0F * (4500.0F - f5) / 3300.0F;
					float f7 = 0.01F * f5;
					if (f7 < 10.0F)
						f7 = 10.0F;
					Vxy.set((double) World.Rnd().nextFloat(-10.0F, 10.0F), (double) World.Rnd().nextFloat(-10.0F, 10.0F), (double) World.Rnd().nextFloat(5.0F, f7));
					Vxy.scale((double) (4.0F - (float) Skill));
					float f8 = 2.0E-5F * f5 * f5;
					if (f8 < 60.0F)
						f8 = 60.0F;
					if (f8 > 350.0F)
						f8 = 350.0F;
					Vxy.z += (double) f8;
					submaneuver++;
					break;
				}
				case SUB_MAN1:
					setSpeedMode(4);
					smConstSpeed = 110.0F;
					if (!(f1 >= man1Dist))
					{
						CT.AirBrakeControl = 1.0F;
						if (this.actor instanceof TypeFighter)
							CT.FlapsControl = 1.0F;
						push();
						push(6);
						safe_pop();
						submaneuver++;
					}
				break;
				case SUB_MAN2:
				{
					setSpeedMode(4);
					smConstSpeed = 110.0F;
					sub_Man_Count++;
					CT.AirBrakeControl = 1.0F;
					if (this.actor instanceof TypeFighter)
						CT.FlapsControl = 1.0F;
					float f3 = Or.getKren();
					if (Or.getTangage() > -90.0F)
					{
						f3 -= 180.0F;
						if (f3 < -180.0F)
							f3 += 360.0F;
					}
					CT.AileronControl = (float) ((double) (-0.04F * f3) - 0.5 * this.getW().x);
					if (this.getOverload() < 4.0F)
						CT.ElevatorControl += 0.3F * f;
					else
						CT.ElevatorControl -= 0.3F * f;
					if (sub_Man_Count > 30 && Or.getTangage() < -90.0F || sub_Man_Count > 150)
					{
						sub_Man_Count = 0;
						submaneuver++;
					}
					break;
				}
				case SUB_MAN3:
					CT.AirBrakeControl = 1.0F;
					if (this.actor instanceof TypeFighter)
						CT.FlapsControl = 1.0F;
					CT.BayDoorControl = 1.0F;
					setSpeedMode(4);
					smConstSpeed = 110.0F;
					sub_Man_Count++;
					if (sub_Man_Count > 80)
					{
						float f9 = (float) Vwld.z * 0.1019F;
						f9 = (f9 + (float) Math.sqrt((double) (f9 * f9 + 2.0F * f4 * 0.1019F)) + 3.5E-4F * f4);
						float f10 = (float) Math.sqrt(Vwld.x * Vwld.x + Vwld.y * Vwld.y);
						float f11 = f10 * f9;
						float f12 = 0.2F * (f1 - f11);
						Vxy.z += (double) f12;
						if (Vxy.z > (double) (0.7F * f4))
							Vxy.z = (double) (0.7F * f4);
					}
					if ((sub_Man_Count > 100 && Alt < 1000.0F && Vpl.dot(Ve) > 0.9900000095367432) || Alt < 600.0F)
					{
						bombsOut = true;
						Voice.speakAttackByBombs((Aircraft) this.actor);
						CT.BayDoorControl = 0.0F;
						CT.AirBrakeControl = 0.0F;
						AP.way.next();
						push(0);
						push(8);
						push(2);
						pop();
					}
			}
			Or.transformInv(Ve);
			Ve.normalize();
			if (submaneuver == SUB_MAN3)
				attackTurnToDirection(1000.0F, f, 30.0F);
			else if (submaneuver != SUB_MAN2)
				turnToDirection(f);
		}
	}

	private void groundAttackTorpedo(Actor actor, float f)
	{
		float f4 = 50.0F;
		maxG = 5.0F;
		maxAOA = 8.0F;
		float f5 = 0.0F;
		setSpeedMode(4);
		Class class1 = null;
		if (CT.Weapons[3][0] instanceof TorpedoGun)
		{
			TorpedoGun torpedogun = (TorpedoGun) CT.Weapons[3][0];
			class1 = (Class) Property.value(torpedogun.getClass(), "bulletClass", null);
		}
		smConstSpeed = 100.0F;
		if (class1 != null)
		{
			smConstSpeed = Property.floatValue(class1, "dropSpeed", 100.0F) / 3.7F;
			f4 = Property.floatValue(class1, "dropAltitude", 50.0F) + 10.0F;
		}
		if (this.actor instanceof Swordfish)
		{
			setSpeedMode(9);
			f4 *= 0.75F;
		}
		minElevCoeff = 20.0F;
		Ve.set(actor.pos.getAbsPoint());
		Ve.sub(Loc);
		if (first)
		{
			Vtarg.set(actor.pos.getAbsPoint());
			submaneuver = SUB_MAN0;
		}
		if (submaneuver == SUB_MAN1 && sub_Man_Count == 0)
		{
			tmpV3f.set(actor.pos.getAbsPoint());
			tmpV3f.sub(Vtarg);
			actor.getSpeed(tmpV3d);
			if (tmpV3f.length() > 9.999999747378752E-5)
				tmpV3f.normalize();
			Vxy.set(tmpV3f.y * 3000.0, -tmpV3f.x * 3000.0, 0.0);
			direc = Vxy.dot(Ve) > 0.0;
			if (direc)
				Vxy.scale(-1.0);
			Vtarg.add(Vxy);
			Vtarg.x += tmpV3d.x * 80.0;
			Vtarg.y += tmpV3d.y * 80.0;
			Vtarg.z = 80.0;
			TargDevV.set(((double) World.cur().rnd.nextFloat(-16.0F, 16.0F) * (3.5 - (double) Skill)), ((double) World.cur().rnd.nextFloat(-16.0F, 16.0F) * (3.5 - (double) Skill)), 0.0);
		}
		if (submaneuver == SUB_MAN2)
		{
			Vtarg.set(actor.pos.getAbsPoint());
			actor.getSpeed(tmpV3d);
			float f6 = 20.0F;
			if (class1 != null)
				f6 = Property.floatValue(class1, "velocity", 1.0F);
			float f8 = actor.collisionR();
			if (f8 > 80.0F)
				f5 = 50.0F;
			if (f8 > 130.0F)
				f5 = 100.0F;
			if (f8 < 25.0F)
				f5 = -50.0F;
			float f11 = 950.0F;
			if (this.actor instanceof JU_88NEW)
				f11 += 90.0F;
			double d1 = Math.sqrt(0.204 * Loc.z);
			double d2 = 1.0 * d1 * (double) this.getSpeed();
			double d3 = ((double) (f11 + f5) - d2) / (double) f6;
			Vtarg.x += (double) (float) (tmpV3d.x * d3);
			Vtarg.y += (double) (float) (tmpV3d.y * d3);
			Vtarg.z = (double) f4;
			if (Loc.z < 30.0)
				Vtarg.z += 3.0 * (30.0 - Loc.z);
			Vtarg.add(TargDevV);
		}
		Ve.set(Vtarg);
		Ve.sub(Loc);
		float f2 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y + Ve.z * Ve.z);
		Vf.set(Ve);
		Vpl.set(Vwld);
		Vpl.normalize();
		if (Alt < f4 - 5.0F)
		{
			if (Vwld.z < 0.0)
				Vwld.z += (double) ((f4 - Alt) * 0.25F);
			if (Alt < 8.0F)
				set_maneuver(2);
			if (Alt < 20.0F && f2 < 75.0F)
				set_maneuver(2);
		}
		else if (Alt > f4 + 5.0F && submaneuver == SUB_MAN1 && Vwld.z > 0.0)
			Vwld.z--;
		switch (submaneuver)
		{
			default:
			break;
			case SUB_MAN0:
				sub_Man_Count++;
				if (sub_Man_Count > 60)
				{
					submaneuver++;
					sub_Man_Count = 0;
				}
				addWindCorrection();
			break;
			case SUB_MAN1:
				setSpeedMode(4);
				if (this.actor instanceof Swordfish)
					setSpeedMode(9);
				sub_Man_Count++;
				if (f2 < 1200.0F || f2 < 2000.0F && ZutiSupportMethods.isStaticActor(actor))
				{
					submaneuver++;
					sub_Man_Count = 0;
				}
				addWindCorrection();
			break;
			case SUB_MAN2:
				setSpeedMode(4);
				if (f2 < 800.0F + f5)
				{
					if (this.actor instanceof TypeHasToKG && ((TypeHasToKG) this.actor).isSalvo())
					{
						int i = 0;
						float f9 = actor.collisionR() * 1.8F;
						i = (int) Math.toDegrees(Math.atan((double) (f9 / 800.0F)));
						i += World.Rnd().nextInt(-2, 2);
						if (i < 3)
							i = 3;
						AS.setSpreadAngle(i);
					}
					CT.WeaponControl[3] = true;
					setSpeedMode(6);
					AP.way.next();
					submaneuver++;
					sub_Man_Count = 0;
				}
				else if (ZutiSupportMethods.isStaticActor(actor))
				{
					float f7 = Property.floatValue(class1, "velocity", 20.0F);
					float f10 = Property.floatValue(class1, "traveltime", 100.0F);
					float f12 = f7 * f10 - 150.0F;
					if (f2 < f12 && World.land().isWater(this.actor.pos.getAbsPoint().x, this.actor.pos.getAbsPoint().y))
					{
						CT.WeaponControl[3] = true;
						setSpeedMode(6);
						AP.way.next();
						submaneuver++;
						sub_Man_Count = 0;
					}
				}
			break;
			case SUB_MAN3:
				setSpeedMode(9);
				sub_Man_Count++;
				if (sub_Man_Count > 150)
				{
					task = 3;
					push(0);
					push(8);
					pop();
					submaneuver = SUB_MAN0;
					sub_Man_Count = 0;
				}
		}
		Or.transformInv(Ve);
		if (submaneuver == SUB_MAN3)
		{
			if (sub_Man_Count < 20)
				Ve.set(1.0, 0.09000000357627869, 0.029999999329447746);
			else
				Ve.set(1.0, 0.09000000357627869, 0.009999999776482582);
			if (!direc)
				Ve.y *= -1.0;
		}
		Ve.normalize();
		turnToDirection(f);
	}

	private void groundAttackTorpedoToKG(Actor actor, float f)
	{
		float f2 = 50.0F;
		maxG = 5.0F;
		maxAOA = 8.0F;
		setSpeedMode(4);
		Class class1 = null;
		if (CT.Weapons[3][0] instanceof TorpedoGun)
		{
			TorpedoGun torpedogun = (TorpedoGun) CT.Weapons[3][0];
			class1 = (Class) Property.value(torpedogun.getClass(), "bulletClass", null);
		}
		smConstSpeed = 100.0F;
		if (class1 != null)
		{
			smConstSpeed = Property.floatValue(class1, "dropSpeed", 100.0F) / 3.7F;
			f2 = Property.floatValue(class1, "dropAltitude", 50.0F) + 10.0F;
		}
		minElevCoeff = 20.0F;
		Ve.set(actor.pos.getAbsPoint());
		Ve.sub(Loc);
		if (first)
		{
			Vtarg.set(actor.pos.getAbsPoint());
			submaneuver = SUB_MAN0;
		}
		if (submaneuver == SUB_MAN1 && sub_Man_Count == 0)
		{
			tmpV3f.set(actor.pos.getAbsPoint());
			tmpV3f.sub(Vtarg);
			if (tmpV3f.length() > 9.999999747378752E-5)
				tmpV3f.normalize();
			Vxy.set(tmpV3f.y * 3000.0, -tmpV3f.x * 3000.0, 0.0);
			direc = Vxy.dot(Ve) > 0.0;
			if (direc)
				Vxy.scale(-1.0);
			Vtarg.add(Vxy);
			Vtarg.z = 80.0;
		}
		if (submaneuver == SUB_MAN2)
		{
			Vtarg.set(actor.pos.getAbsPoint());
			Vtarg.z = (double) f2;
			if (Loc.z < 30.0)
				Vtarg.z += 3.0 * (30.0 - Loc.z);
		}
		Ve.set(Vtarg);
		Ve.sub(Loc);
		float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y + Ve.z * Ve.z);
		Vf.set(Ve);
		Vpl.set(Vwld);
		Vpl.normalize();
		if (Alt < f2 - 5.0F)
		{
			if (Vwld.z < 0.0)
				Vwld.z += (double) ((f2 - Alt) * 0.25F);
			if (Alt < 8.0F)
				set_maneuver(2);
			if (Alt < 20.0F && f1 < 75.0F)
				set_maneuver(2);
		}
		else if (Alt > f2 + 5.0F && submaneuver == SUB_MAN1 && Vwld.z > 0.0)
			Vwld.z--;
		switch (submaneuver)
		{
			default:
			break;
			case SUB_MAN0:
				sub_Man_Count++;
				if (sub_Man_Count > 60)
				{
					submaneuver++;
					sub_Man_Count = 0;
				}
				addWindCorrection();
			break;
			case SUB_MAN1:
				setSpeedMode(4);
				sub_Man_Count++;
				if (f1 < 4000.0F)
				{
					submaneuver++;
					sub_Man_Count = 0;
				}
				addWindCorrection();
			break;
			case SUB_MAN2:
			{
				setSpeedMode(4);
				double d = Ve.angle(Vwld);
				float f3 = 180.0F - (Or.getYaw() - actor.pos.getAbsOrient().getYaw());
				if (f3 < -180.0F)
					f3 += 360.0F;
				else if (f3 > 180.0F)
					f3 -= 360.0F;
				float f4 = Property.floatValue(class1, "velocity", 20.0F);
				float f5 = Property.floatValue(class1, "traveltime", 100.0F);
				float f6 = f4 * f5 - 10.0F;
				if (f6 > 2700.0F)
					f6 = 2700.0F;
				double d1 = (double) ((Math.abs(f3) - 90.0F) * 8.0F);
				if (d1 < 0.0)
					d1 = 0.0;
				if (Skill == 2)
					d1 += 100.0;
				if ((double) f1 < (double) f6 - d1 && f1 > 1800.0F && d < 0.2)
				{
					actor.getSpeed(tmpV3d);
					float f7 = 1.0F;
					if (Skill == 2)
						f7 = World.Rnd().nextFloat(0.8F, 1.2F);
					else if (Skill == 3)
						f7 = World.Rnd().nextFloat(0.9F, 1.1F);
					f7 += 0.1;
					ToKGUtils.setTorpedoGyroAngle(this, f3, ((float) (1.95 * tmpV3d.length()) * f7));
					if (((TypeHasToKG) this.actor).isSalvo())
					{
						int i = 0;
						float f8 = actor.collisionR() * 1.8F;
						i = (int) Math.toDegrees(Math.atan((double) f8 / ((double) f6 - d1)));
						i += World.Rnd().nextInt(-2, 2);
						if (i < 1)
							i = 1;
						AS.setSpreadAngle(i);
					}
					CT.WeaponControl[3] = true;
					setSpeedMode(6);
					AP.way.next();
					push(15);
					submaneuver++;
					sub_Man_Count = 0;
				}
				else if (f1 < 1500.0F)
				{
					ToKGUtils.setTorpedoGyroAngle(this, 0.0F, 0.0F);
					set_maneuver(51);
				}
				break;
			}
			case SUB_MAN3:
			{
				setSpeedMode(9);
				push(15);
				pop();
				task = 61;
				sub_Man_Count++;
				boolean flag = false;
				for (int j = 0; j < CT.Weapons[3].length; j++)
				{
					if (CT.Weapons[3][j] != null && !(CT.Weapons[3][j] instanceof BombGunNull) && CT.Weapons[3][j].countBullets() != 0)
						flag = true;
				}
				if (sub_Man_Count > 800 || !flag)
				{
					task = 3;
					push(21);
					push(8);
					pop();
					submaneuver = SUB_MAN0;
					sub_Man_Count = 0;
				}
			}
		}
		Or.transformInv(Ve);
		if (submaneuver == SUB_MAN3)
		{
			if (sub_Man_Count < 20)
				Ve.set(1.0, 0.09000000357627869, 0.029999999329447746);
			else
				Ve.set(1.0, 0.09000000357627869, 0.009999999776482582);
			if (!direc)
				Ve.y *= -1.0;
		}
		Ve.normalize();
		turnToDirection(f);
	}

	private void groundAttackCassette(Actor actor, float f)
	{
		maxG = 5.0F;
		maxAOA = 8.0F;
		setSpeedMode(4);
		smConstSpeed = 120.0F;
		minElevCoeff = 20.0F;
		Ve.set(actor.pos.getAbsPoint());
		Ve.sub(Loc);
		float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
		if (submaneuver == SUB_MAN3 && sub_Man_Count > 0 && sub_Man_Count < 45 && f1 > 200.0F)
		{
			tmpV3f.set(Vxy);
			float f4 = (float) tmpV3f.dot(Ve);
			tmpV3f.scale((double) -f4);
			tmpV3f.add(Ve);
			float f7 = (float) tmpV3f.length();
			float f6;
			if (f7 > 150.0F)
				f6 = 7.5F / f7;
			else
				f6 = 0.05F;
			tmpV3f.scale((double) f6);
			tmpV3f.z = 0.0;
			Vwld.add(tmpV3f);
		}
		if (f1 <= 200.0F)
			sub_Man_Count = 50;
		if (first)
		{
			Vtarg.set(actor.pos.getAbsPoint());
			submaneuver = SUB_MAN0;
		}
		if (submaneuver == SUB_MAN1 && sub_Man_Count == 0)
		{
			tmpV3f.set(actor.pos.getAbsPoint());
			actor.getSpeed(tmpV3d);
			if (tmpV3d.length() < 0.5)
				tmpV3d.set(Ve);
			tmpV3d.normalize();
			Vxy.set((double) (float) tmpV3d.x, (double) (float) tmpV3d.y, (double) (float) tmpV3d.z);
			Vtarg.x -= tmpV3d.x * 3000.0;
			Vtarg.y -= tmpV3d.y * 3000.0;
			Vtarg.z += 250.0;
		}
		if (submaneuver == SUB_MAN2 && sub_Man_Count == 0)
		{
			Vtarg.set(actor.pos.getAbsPoint());
			Vtarg.x -= Vxy.x * 1000.0;
			Vtarg.y -= Vxy.y * 1000.0;
			Vtarg.z += 50.0;
		}
		if (submaneuver == SUB_MAN3 && sub_Man_Count == 0)
		{
			checkGround = false;
			Vtarg.set(actor.pos.getAbsPoint());
			Vtarg.x += Vxy.x * 1000.0;
			Vtarg.y += Vxy.y * 1000.0;
			Vtarg.z += 50.0;
		}
		Ve.set(Vtarg);
		Ve.sub(Loc);
		addWindCorrection();
		float f2 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y + Ve.z * Ve.z);
		Vf.set(Ve);
		Or.transformInv(Ve);
		Ve.normalize();
		Vpl.set(Vwld);
		Vpl.normalize();
		if (Alt < 10.0F)
		{
			push(0);
			push(2);
			pop();
		}
		switch (submaneuver)
		{
			case SUB_MAN0:
				setSpeedMode(9);
				sub_Man_Count++;
				if (sub_Man_Count > 60)
				{
					submaneuver++;
					sub_Man_Count = 0;
				}
			break;
			case SUB_MAN1:
				setSpeedMode(9);
				sub_Man_Count++;
				if (f2 < 1000.0F)
				{
					submaneuver++;
					sub_Man_Count = 0;
				}
			break;
			case SUB_MAN2:
				setSpeedMode(6);
				sub_Man_Count++;
				if (f2 < 155.0F)
				{
					submaneuver++;
					sub_Man_Count = 0;
				}
				if (sub_Man_Count > 320)
				{
					push(0);
					push(10);
					pop();
				}
			break;
			case SUB_MAN3:
			{
				setSpeedMode(6);
				sub_Man_Count++;
				Vwld.z *= 0.800000011920929;
				Or.transformInv(Vwld);
				Vwld.y *= 0.8999999761581421;
				Or.transform(Vwld);
				float f5 = (float) sub_Man_Count;
				if (f5 < 100.0F)
					f5 = 100.0F;
				if (Alt > 45.0F)
					Vwld.z -= (double) (0.0020F * (Alt - 45.0F) * f5);
				else
					Vwld.z -= (double) (0.0050F * (Alt - 45.0F) * f5);
				if (Alt < 0.0F)
					Alt = 0.0F;
				if (f2 < 1080.0F + (this.getSpeed() * (float) Math.sqrt((double) (2.0F * Alt / 9.81F))))
					bombsOut = true;
				if (Ve.x < 0.0 || f2 < 350.0F || sub_Man_Count > 160)
				{
					push(0);
					push(10);
					push(10);
					pop();
				}
				break;
			}
		}
		if (submaneuver == SUB_MAN0)
			Ve.set(1.0, 0.0, 0.0);
		turnToDirection(f);
	}

	public void goodFighterVsFighter(float f)
	{
		Ve.sub(target.Loc, Loc);
		float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
		Or.transformInv(Ve);
		float f2 = (float) Ve.length();
		float f4 = 1.0F / f2;
		Vtarg.set(Ve);
		Vtarg.scale((double) f4);
		float f5 = (Energy - target.Energy) * 0.1019F;
		tmpV3f.sub(target.Vwld, Vwld);
		if (sub_Man_Count == 0)
		{
			float f6 = 0.0F;
			if (CT.Weapons[1] != null && CT.Weapons[1][0].countBullets() > 0)
				f6 = ((GunGeneric) CT.Weapons[1][0]).bulletSpeed();
			else if (CT.Weapons[0] != null)
				f6 = ((GunGeneric) CT.Weapons[0][0]).bulletSpeed();
			if (f6 > 0.01F)
				bullTime = 1.0F / f6;
			submanDelay = 0;
		}
		if (f1 < 1500.0F)
		{
			float f7 = 0.0F;
			float f9 = 0.0F;
			if (Vtarg.x > -0.20000000298023224)
			{
				f7 = 3.0F * ((float) Vtarg.x + 0.2F);
				Vxy.set(tmpV3f);
				Vxy.scale(1.0);
				Or.transformInv(Vxy);
				Vxy.add(Ve);
				Vxy.normalize();
				f9 = 10.0F * (float) (Vxy.x - Vtarg.x);
				if (f9 < -1.0F)
					f9 = -1.0F;
				if (f9 > 1.0F)
					f9 = 1.0F;
			}
			else
				f7 = 3.0F * ((float) Vtarg.x + 0.2F);
			if (submaneuver == SUB_MAN4 && Vtarg.x < 0.6000000238418579 && (double) f2 < 300.0)
			{
				submaneuver = SUB_MAN1;
				submanDelay = 30;
			}
			if (submaneuver != SUB_MAN4 && (double) f5 > 300.0 && Vtarg.x > 0.75)
			{
				submaneuver = SUB_MAN4;
				submanDelay = 240;
			}
			float f11 = 0.0015F * f5 + 6.0E-4F * f1 + f7 + 0.5F * f9;
			if (f11 > 0.9F && submanDelay == 0)
			{
				if (Vtarg.x > 0.5 || (double) f1 * 2.0 < (double) f2)
				{
					submaneuver = SUB_MAN4;
					submanDelay = 240;
				}
				else
				{
					submaneuver = SUB_MAN3;
					submanDelay = 120;
				}
			}
			else if (f5 > 800.0F && submaneuver == SUB_MAN0 || f5 > 1000.0F)
			{
				Ve.set(0.0, 0.0, 800.0);
				if (submanDelay == 0)
				{
					submaneuver = SUB_MAN0;
					submanDelay = 30;
				}
			}
			else if (f2 > 450.0F && submaneuver == SUB_MAN2 || f2 > 600.0F)
			{
				if (submanDelay == 0)
				{
					submaneuver = SUB_MAN2;
					submanDelay = 60;
				}
			}
			else if (submanDelay == 0)
			{
				submaneuver = SUB_MAN1;
				submanDelay = 30;
			}
		}
		else if (f1 < 3000.0F)
		{
			if (Vtarg.x < 0.5)
			{
				if (submanDelay == 0)
				{
					submaneuver = SUB_MAN3;
					submanDelay = 120;
				}
			}
			else if (f5 > 600.0F && submaneuver == SUB_MAN0 || f5 > 800.0F)
			{
				Ve.set(0.0, 0.0, 800.0);
				if (submanDelay == 0)
				{
					submaneuver = SUB_MAN0;
					submanDelay = 120;
				}
			}
			else if (f5 > -200.0F && submaneuver >= SUB_MAN4 || f5 > -100.0F)
			{
				if (submanDelay == 0)
				{
					submaneuver = SUB_MAN4;
					submanDelay = 120;
				}
			}
			else
			{
				Ve.set(0.0, 0.0, Loc.z);
				if (submanDelay == 0)
				{
					submaneuver = SUB_MAN0;
					submanDelay = 120;
				}
			}
		}
		else
		{
			Ve.set(0.0, 0.0, 1000.0);
			if (submanDelay == 0)
			{
				submaneuver = SUB_MAN0;
				submanDelay = 180;
			}
		}
		switch (submaneuver)
		{
			case SUB_MAN0:
			{
				target.Or.transform(Ve);
				Ve.add(target.Loc);
				Ve.sub(Loc);
				tmpV3f.set(Ve);
				tmpV3f.z = 0.0;
				float f3 = (float) tmpV3f.length();
				tmpV3f.normalize();
				Vtarg.set(target.Vwld);
				Vtarg.z = 0.0;
				float f8 = (float) tmpV3f.dot(Vtarg);
				float f10 = this.getSpeed() - f8;
				if (f10 < 10.0F)
					f10 = 10.0F;
				float f12 = f3 / f10;
				if (f12 < 0.0F)
					f12 = 0.0F;
				tmpV3f.scale(Vtarg, (double) f12);
				Ve.add(tmpV3f);
				Or.transformInv(Ve);
				Ve.normalize();
				setSpeedMode(9);
				farTurnToDirection();
				sub_Man_Count++;
				break;
			}
			case SUB_MAN1:
				setSpeedMode(9);
				CT.AileronControl = -0.04F * Or.getKren();
				CT.ElevatorControl = -0.04F * (Or.getTangage() + 5.0F);
				CT.RudderControl = 0.0F;
			break;
			case SUB_MAN2:
			{
				setSpeedMode(9);
				tmpOr.setYPR(Or.getYaw(), 0.0F, 0.0F);
				float f13 = 6.0F;
				if (Or.getKren() > 0.0F)
					Ve.set(100.0, (double) -f13, 10.0);
				else
					Ve.set(100.0, (double) f13, 10.0);
				tmpOr.transform(Ve);
				Or.transformInv(Ve);
				Ve.normalize();
				farTurnToDirection();
				break;
			}
			case SUB_MAN3:
				minElevCoeff = 20.0F;
				setSpeedMode(9);
				tmpOr.setYPR(Or.getYaw(), 0.0F, 0.0F);
				Ve.sub(target.Loc, Loc);
				Ve.z = 0.0;
				Ve.normalize();
				Ve.z = 0.4;
				Or.transformInv(Ve);
				Ve.normalize();
				attackTurnToDirection(1000.0F, f, 15.0F);
			break;
			case SUB_MAN4:
				minElevCoeff = 20.0F;
				boomAttack(f);
				setSpeedMode(9);
			break;
			default:
				minElevCoeff = 20.0F;
				fighterVsFighter(f);
		}
		if (submanDelay > 0)
			submanDelay--;
	}

	public void bNZFighterVsFighter(float f)
	{
		Ve.sub(target.Loc, Loc);
		Or.transformInv(Ve);
		dist = (float) Ve.length();
		float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
		float f2 = 1.0F / dist;
		Vtarg.set(Ve);
		Vtarg.scale((double) f2);
		float f3 = (Energy - target.Energy) * 0.1019F;
		tmpV3f.sub(target.Vwld, Vwld);
		if (sub_Man_Count == 0)
		{
			float f4 = 0.0F;
			if (CT.Weapons[1] != null && CT.Weapons[1][0].countBullets() > 0)
				f4 = ((GunGeneric) CT.Weapons[1][0]).bulletSpeed();
			else if (CT.Weapons[0] != null)
				f4 = ((GunGeneric) CT.Weapons[0][0]).bulletSpeed();
			if (f4 > 0.01F)
				bullTime = 1.0F / f4;
			submanDelay = 0;
		}
		if (f1 < 1500.0F)
		{
			float f5 = 0.0F;
			float f7 = 0.0F;
			if (Vtarg.x > -0.20000000298023224)
			{
				f5 = 3.0F * ((float) Vtarg.x + 0.2F);
				Vxy.set(tmpV3f);
				Vxy.scale(1.0);
				Or.transformInv(Vxy);
				Vxy.add(Ve);
				Vxy.normalize();
				f7 = 20.0F * (float) (Vxy.x - Vtarg.x);
				if (f7 < -1.0F)
					f7 = -1.0F;
				if (f7 > 1.0F)
					f7 = 1.0F;
			}
			float f9 = f3 * 0.0015F + f1 * 6.0E-4F + f5 + f7;
			if (f9 > 0.8F && submaneuver >= SUB_MAN3 || f9 > 1.2F)
			{
				if (tmpV3f.length() < 100.0)
				{
					if (submanDelay == 0)
					{
						submaneuver = SUB_MAN4;
						submanDelay = 120;
					}
				}
				else if (submanDelay == 0)
				{
					submaneuver = SUB_MAN3;
					submanDelay = 120;
				}
			}
			else if (f3 > 800.0F && submaneuver == SUB_MAN0 || f3 > 1000.0F)
			{
				Ve.set(0.0, 0.0, 800.0);
				if (submanDelay == 0)
				{
					submaneuver = SUB_MAN0;
					submanDelay = 30;
				}
			}
			else if (dist > 450.0F && submaneuver == SUB_MAN2 || dist > 600.0F)
			{
				if (submanDelay == 0)
				{
					submaneuver = SUB_MAN2;
					submanDelay = 60;
				}
			}
			else if (submanDelay == 0)
			{
				submaneuver = SUB_MAN1;
				submanDelay = 30;
			}
		}
		else if (f1 < 3000.0F)
		{
			if (f3 > 600.0F && submaneuver == SUB_MAN0 || f3 > 800.0F)
			{
				Ve.set(0.0, 0.0, 800.0);
				if (submanDelay == 0)
				{
					submaneuver = SUB_MAN0;
					submanDelay = 120;
				}
			}
			else if (f3 > -200.0F && submaneuver >= SUB_MAN3 || f3 > -100.0F)
			{
				if (submanDelay == 0)
				{
					submaneuver = SUB_MAN3;
					submanDelay = 120;
				}
			}
			else
			{
				Ve.set(0.0, 0.0, Loc.z);
				if (submanDelay == 0)
				{
					submaneuver = SUB_MAN0;
					submanDelay = 120;
				}
			}
		}
		else
		{
			Ve.set(0.0, 0.0, 1000.0);
			if (submanDelay == 0)
			{
				submaneuver = SUB_MAN0;
				submanDelay = 180;
			}
		}
		switch (submaneuver)
		{
			case SUB_MAN0:
			{
				target.Or.transform(Ve);
				Ve.add(target.Loc);
				Ve.sub(Loc);
				tmpV3f.set(Ve);
				tmpV3f.z = 0.0;
				dist = (float) tmpV3f.length();
				tmpV3f.normalize();
				Vtarg.set(target.Vwld);
				Vtarg.z = 0.0;
				float f6 = (float) tmpV3f.dot(Vtarg);
				float f8 = this.getSpeed() - f6;
				if (f8 < 10.0F)
					f8 = 10.0F;
				float f10 = dist / f8;
				if (f10 < 0.0F)
					f10 = 0.0F;
				tmpV3f.scale(Vtarg, (double) f10);
				Ve.add(tmpV3f);
				Or.transformInv(Ve);
				Ve.normalize();
				setSpeedMode(9);
				farTurnToDirection();
				sub_Man_Count++;
				break;
			}
			case SUB_MAN1:
				setSpeedMode(9);
				CT.AileronControl = -0.04F * Or.getKren();
				CT.ElevatorControl = -0.04F * (Or.getTangage() + 5.0F);
				CT.RudderControl = 0.0F;
			break;
			case SUB_MAN2:
				setSpeedMode(9);
				tmpOr.setYPR(Or.getYaw(), 0.0F, 0.0F);
				if (Or.getKren() > 0.0F)
					Ve.set(100.0, -6.0, 10.0);
				else
					Ve.set(100.0, 6.0, 10.0);
				tmpOr.transform(Ve);
				Or.transformInv(Ve);
				Ve.normalize();
				farTurnToDirection();
			break;
			case SUB_MAN3:
				minElevCoeff = 20.0F;
				fighterVsFighter(f);
				setSpeedMode(6);
			break;
			default:
				minElevCoeff = 20.0F;
				fighterVsFighter(f);
		}
		if (submanDelay > 0)
			submanDelay--;
	}

	public void setBomberAttackType(int i)
	{
		float f;
		if (Actor.isValid(target.actor))
			f = target.actor.collisionR();
		else
			f = 15.0F;
		setRandomTargDeviation(0.8F);
		switch (i)
		{
			case 0:
				ApproachV.set((double) (-300.0F + World.Rnd().nextFloat(-100.0F, 100.0F)), 0.0, (double) (600.0F + World.Rnd().nextFloat(-100.0F, 100.0F)));
				TargV.set(0.0, 0.0, 0.0);
				ApproachR = 150.0F;
				TargY = 2.1F * f;
				TargZ = 3.9F * f;
				TargYS = 0.43F * f;
				TargZS = 0.43F * f;
			break;
			case 1:
				ApproachV.set((double) (-300.0F + World.Rnd().nextFloat(-100.0F, 100.0F)), 0.0, (double) (600.0F + World.Rnd().nextFloat(-100.0F, 100.0F)));
				TargV.set(target.EI.engines[World.Rnd().nextInt(0, target.EI.getNum() - 1)].getEnginePos());
				TargV.x--;
				ApproachR = 150.0F;
				TargY = 2.1F * f;
				TargZ = 3.9F * f;
				TargYS = 0.43F * f;
				TargZS = 0.43F * f;
			break;
			case 2:
				ApproachV.set((double) (-600.0F + World.Rnd().nextFloat(-100.0F, 100.0F)), 0.0, (double) (600.0F + World.Rnd().nextFloat(-100.0F, 100.0F)));
				TargV.set(target.EI.engines[World.Rnd().nextInt(0, target.EI.getNum() - 1)].getEnginePos());
				TargV.x--;
				ApproachR = 300.0F;
				TargY = 2.1F * f;
				TargZ = 3.9F * f;
				TargYS = 0.43F * f;
				TargZS = 0.43F * f;
			break;
			case 3:
				ApproachV.set(2600.0, 0.0, (double) (300.0F + World.Rnd().nextFloat(-100.0F, 100.0F)));
				TargV.set(0.0, 0.0, 0.0);
				ApproachR = 800.0F;
				TargY = 25.0F;
				TargZ = 15.0F;
				TargYS = 3.0F;
				TargZS = 3.0F;
			break;
			case 4:
				ApproachV.set((double) (-400.0F + World.Rnd().nextFloat(-100.0F, 100.0F)), 0.0, (double) (-200.0F + World.Rnd().nextFloat(-50.0F, 50.0F)));
				TargV.set(0.0, 0.0, 0.0);
				ApproachR = 300.0F;
				TargY = 2.1F * f;
				TargZ = 1.3F * f;
				TargYS = 0.26F * f;
				TargZS = 0.26F * f;
			break;
			case 5:
				ApproachV.set(0.0, 0.0, 0.0);
				TargV.set(0.0, 0.0, 0.0);
				ApproachR = 600.0F;
				TargY = 25.0F;
				TargZ = 12.0F;
				TargYS = 0.26F * f;
				TargZS = 0.26F * f;
			break;
			case 6:
				ApproachV.set(600.0, (double) ((float) (600 - (World.Rnd().nextInt(0, 1) * 1200)) + World.Rnd().nextFloat(-100.0F, 100.0F)), 300.0);
				TargV.set(0.0, 0.0, 0.0);
				ApproachR = 300.0F;
				TargY = 2.1F * f;
				TargZ = 1.2F * f;
				TargYS = 0.26F * f;
				TargZS = 0.26F * f;
			break;
			case 7:
				ApproachV.set((double) (-1000.0F + World.Rnd().nextFloat(-200.0F, 200.0F)), 0.0, (double) (100.0F + World.Rnd().nextFloat(-50.0F, 50.0F)));
				TargV.set(target.EI.engines[World.Rnd().nextInt(0, target.EI.getNum() - 1)].getEnginePos());
				ApproachR = 200.0F;
				TargY = 10.0F;
				TargZ = 10.0F;
				TargYS = 2.0F;
				TargZS = 2.0F;
			break;
			case 8:
				ApproachV.set((double) (-1000.0F + World.Rnd().nextFloat(-200.0F, 200.0F)), 0.0, (double) (100.0F + World.Rnd().nextFloat(-50.0F, 50.0F)));
				TargV.set(0.0, 0.0, 0.0);
				ApproachR = 200.0F;
				TargY = 2.1F * f;
				TargZ = 3.9F * f;
				TargYS = 0.2F * f;
				TargZS = 0.2F * f;
			break;
			case 9:
				ApproachV.set((double) (-600.0F + World.Rnd().nextFloat(-100.0F, 100.0F)), 0.0, (double) (600.0F + World.Rnd().nextFloat(-100.0F, 100.0F)));
				TargV.set(0.0, 0.0, 0.0);
				ApproachR = 300.0F;
				TargY = 2.1F * f;
				TargZ = 3.9F * f;
				TargYS = 0.2F * f;
				TargZS = 0.2F * f;
			break;
			case 10:
				ApproachV.set((double) (-600.0F + World.Rnd().nextFloat(-100.0F, 100.0F)), 0.0, (double) (-300.0F + World.Rnd().nextFloat(-100.0F, 100.0F)));
				TargV.set(100.0, 0.0, -400.0);
				ApproachR = 300.0F;
				TargY = 2.1F * f;
				TargZ = 3.9F * f;
				TargYS = 0.43F * f;
				TargZS = 0.43F * f;
			break;
			default:
				ApproachV.set((double) (-1000.0F + World.Rnd().nextFloat(-200.0F, 200.0F)), 0.0, (double) (100.0F + World.Rnd().nextFloat(-50.0F, 50.0F)));
				TargV.set(target.EI.engines[World.Rnd().nextInt(0, target.EI.getNum() - 1)].getEnginePos());
				ApproachR = 200.0F;
				TargY = 10.0F;
				TargZ = 10.0F;
				TargYS = 2.0F;
				TargZS = 2.0F;
		}
		float f1 = 0.0F;
		if (CT.Weapons[1] != null && CT.Weapons[1][0].countBullets() > 0)
			f1 = ((GunGeneric) CT.Weapons[1][0]).bulletSpeed();
		else if (CT.Weapons[0] != null)
			f1 = ((GunGeneric) CT.Weapons[0][0]).bulletSpeed();
		if (f1 > 0.01F)
			bullTime = 1.0F / f1;
	}

	public void fighterVsBomber(float f)
	{
		maxAOA = 15.0F;
		tmpOr.setAT0(target.Vwld);
		switch (submaneuver)
		{
			case SUB_MAN0:
			{
				minElevCoeff = 4.0F;
				rocketsDelay = 0;
				bulletDelay = 0;
				double d = 1.0E-4 * target.Loc.z;
				Ve.sub(target.Loc, Loc);
				tmpOr.transformInv(Ve);
				scaledApproachV.set(ApproachV);
				scaledApproachV.x -= 700.0 * d;
				scaledApproachV.z += 500.0 * d;
				Ve.add(scaledApproachV);
				Ve.normalize();
				tmpV3f.scale(scaledApproachV, -1.0);
				tmpV3f.normalize();
				double d1 = Ve.dot(tmpV3f);
				Ve.set(Vwld);
				Ve.normalize();
				tmpV3f.set(target.Vwld);
				tmpV3f.normalize();
				double d2 = Ve.dot(tmpV3f);
				Ve.set(scaledApproachV);
				Ve.x += 60.0 * (2.0 * (1.0 - d1) + 4.0 * (1.0 - d2));
				tmpOr.transform(Ve);
				Ve.add(target.Loc);
				Ve.sub(Loc);
				double d3 = Ve.z;
				tmpV3f.set(Ve);
				tmpV3f.z = 0.0;
				float f1 = (float) tmpV3f.length();
				float f4 = 0.55F + 0.15F * (float) Skill;
				tmpV3f.normalize();
				Vtarg.set(target.Vwld);
				Vtarg.z = 0.0;
				float f7 = (float) tmpV3f.dot(Vtarg);
				float f8 = this.getSpeed() - f7;
				if (f8 < 10.0F)
					f8 = 10.0F;
				float f9 = f1 / f8;
				if (f9 < 0.0F)
					f9 = 0.0F;
				tmpV3f.scale(Vtarg, (double) (f9 * f4));
				Ve.add(tmpV3f);
				Or.transformInv(Ve);
				Ve.normalize();
				if (d3 > -200.0)
					setSpeedMode(9);
				else
				{
					setSpeedMode(4);
					smConstSpeed = 180.0F;
				}
				attackTurnToDirection(f1, f, 10.0F * (float) (1.0 + d));
				sub_Man_Count++;
				if ((double) f1 < (double) ApproachR * (1.0 + d) && d3 < 200.0)
				{
					submaneuver++;
					sub_Man_Count = 0;
				}
				break;
			}
			case SUB_MAN1:
			{
				minElevCoeff = 20.0F;
				Ve.set(TargV);
				target.Or.transform(Ve);
				Ve.add(target.Loc);
				Ve.sub(Loc);
				float f2 = (float) Ve.length();
				float f5 = 0.55F + 0.15F * (float) Skill;
				tmpV3f.sub(target.Vwld, Vwld);
				float f10 = f2 * bullTime * 0.0025F;
				if (f10 > 0.05F)
					f10 = 0.05F;
				tmpV3f.scale((double) (f2 * f10 * f5));
				Ve.add(tmpV3f);
				Vtarg.set(Ve);
				Or.transformInv(Ve);
				Ve.normalize();
				((Maneuver) target).incDangerAggressiveness(1, (float) Ve.x, f2, this);
				if (f2 > 3200.0F || Vtarg.z > 1500.0)
				{
					submaneuver = 0;
					sub_Man_Count = 0;
					set_maneuver(0);
				}
				else
				{
					if (Ve.x < 0.30000001192092896)
					{
						Vtarg.z += ((double) (0.012F * (float) Skill * (800.0F + f2)) * (0.30000001192092896 - Ve.x));
						Ve.set(Vtarg);
						Or.transformInv(Ve);
						Ve.normalize();
					}
					attackTurnToDirection(f2, f, 10.0F + (float) Skill * 8.0F);
					setSpeedMode(4);
					smConstSpeed = 180.0F;
					if (f2 < 400.0F)
					{
						submaneuver++;
						sub_Man_Count = 0;
					}
				}
				break;
			}
			case SUB_MAN2:
			{
				minElevCoeff = 20.0F;
				if (rocketsDelay > 0)
					rocketsDelay--;
				if (bulletDelay > 0)
					bulletDelay--;
				if (bulletDelay == 120)
					bulletDelay = 0;
				setRandomTargDeviation(0.8F);
				Ve.set(TargV);
				Ve.add(TargDevV);
				target.Or.transform(Ve);
				Ve.add(target.Loc);
				Ve.sub(Loc);
				Vtarg.set(Ve);
				float f3 = (float) Ve.length();
				float f6 = 0.55F + 0.15F * (float) Skill;
				float f11 = 2.0E-4F * (float) Skill;
				tmpV3f.sub(target.Vwld, Vwld);
				Vpl.set(tmpV3f);
				tmpV3f.scale((double) (f3 * (bullTime + f11) * f6));
				Ve.add(tmpV3f);
				tmpV3f.set(Vpl);
				tmpV3f.scale((double) (f3 * bullTime * f6));
				Vtarg.add(tmpV3f);
				if (f3 > 4000.0F || Vtarg.z > 2000.0)
				{
					submaneuver = 0;
					sub_Man_Count = 0;
					set_maneuver(0);
				}
				else
				{
					Or.transformInv(Vtarg);
					if (Vtarg.x > 0.0 && f3 < 500.0F && (shotAtFriend <= 0 || distToFriend > f3) && Math.abs(Vtarg.y) < (double) (TargY - TargYS * (float) Skill)
							&& Math.abs(Vtarg.z) < (double) (TargZ - TargZS * (float) Skill) && bulletDelay < 120)
					{
						if (!(actor instanceof KI_46_OTSUHEI))
							CT.WeaponControl[0] = true;
						CT.WeaponControl[1] = true;
						bulletDelay += 2;
						if (bulletDelay >= 118)
						{
							int i = (int) (target.getW().length() * 150.0);
							if (i > 60)
								i = 60;
							bulletDelay += World.Rnd().nextInt(i * Skill, 2 * i * Skill);
						}
						if (CT.Weapons[2] != null && CT.Weapons[2][0] != null && CT.Weapons[2][CT.Weapons[2].length - 1].countBullets() != 0 && rocketsDelay < 1)
						{
							tmpV3f.sub(target.Vwld, Vwld);
							Or.transformInv(tmpV3f);
							if ((Math.abs(tmpV3f.y) < (double) (TargY - TargYS * (float) Skill)) && (Math.abs(tmpV3f.z) < (double) (TargZ - TargZS * (float) Skill)))
								CT.WeaponControl[2] = true;
							Voice.speakAttackByRockets((Aircraft) actor);
							rocketsDelay += 120;
						}
						((Maneuver) target).incDangerAggressiveness(1, 0.8F, f3, this);
						((Pilot) target).setAsDanger(actor);
					}
					Vtarg.set(Ve);
					Or.transformInv(Ve);
					Ve.normalize();
					((Maneuver) target).incDangerAggressiveness(1, (float) Ve.x, f3, this);
					if (Ve.x < 0.30000001192092896)
					{
						Vtarg.z += ((double) (0.01F * (float) Skill * (800.0F + f3)) * (0.30000001192092896 - Ve.x));
						Ve.set(Vtarg);
						Or.transformInv(Ve);
						Ve.normalize();
					}
					attackTurnToDirection(f3, f, 10.0F + (float) Skill * 8.0F);
					if (target.getSpeed() > 70.0F)
					{
						setSpeedMode(2);
						tailForStaying = target;
						tailOffset.set(-20.0, 0.0, 0.0);
					}
					else
					{
						setSpeedMode(4);
						smConstSpeed = 70.0F;
					}
					if (strikeEmer)
					{
						Vpl.sub(strikeTarg.Loc, Loc);
						tmpV3f.set(Vpl);
						Or.transformInv(tmpV3f);
						if (tmpV3f.x < 0.0)
							break;
						tmpV3f.sub(strikeTarg.Vwld, Vwld);
						tmpV3f.scale(0.699999988079071);
						Vpl.add(tmpV3f);
						Or.transformInv(Vpl);
						push();
						if (Vpl.z < 0.0)
						{
							push(0);
							push(8);
							push(60);
						}
						else
						{
							push(0);
							push(8);
							push(61);
						}
						pop();
						strikeEmer = false;
						submaneuver = 0;
						sub_Man_Count = 0;
					}
					if (sub_Man_Count > 600)
					{
						push(8);
						pop();
					}
				}
				break;
			}
			default:
				submaneuver = SUB_MAN0;
				sub_Man_Count = 0;
				set_maneuver(0);
		}
	}

	public void fighterUnderBomber(float f)
	{
		maxAOA = 15.0F;
		switch (submaneuver)
		{
			case SUB_MAN0:
			{
				rocketsDelay = 0;
				bulletDelay = 0;
				Ve.set(ApproachV);
				target.Or.transform(Ve);
				Ve.add(target.Loc);
				Ve.sub(Loc);
				tmpV3f.set(Ve);
				tmpV3f.z = 0.0;
				float f1 = (float) tmpV3f.length();
				float f4 = 0.55F + 0.15F * (float) Skill;
				tmpV3f.normalize();
				Vtarg.set(target.Vwld);
				Vtarg.z = 0.0;
				float f7 = (float) tmpV3f.dot(Vtarg);
				float f8 = this.getSpeed() - f7;
				if (f8 < 10.0F)
					f8 = 10.0F;
				float f9 = f1 / f8;
				if (f9 < 0.0F)
					f9 = 0.0F;
				tmpV3f.scale(Vtarg, (double) (f9 * f4));
				Ve.add(tmpV3f);
				Or.transformInv(Ve);
				Ve.normalize();
				setSpeedMode(4);
				smConstSpeed = 140.0F;
				farTurnToDirection();
				sub_Man_Count++;
				if (f1 < ApproachR)
				{
					submaneuver++;
					sub_Man_Count = 0;
				}
				break;
			}
			case SUB_MAN1:
			{
				Ve.set(TargV);
				target.Or.transform(Ve);
				Ve.add(target.Loc);
				Ve.sub(Loc);
				float f2 = (float) Ve.length();
				float f5 = 0.55F + 0.15F * (float) Skill;
				tmpV3f.sub(target.Vwld, Vwld);
				float f10 = f2 * bullTime * 0.0025F;
				if (f10 > 0.02F)
					f10 = 0.02F;
				tmpV3f.scale((double) (f2 * f10 * f5));
				Ve.add(tmpV3f);
				Vtarg.set(Ve);
				Or.transformInv(Ve);
				Ve.normalize();
				((Maneuver) target).incDangerAggressiveness(1, (float) Ve.x, f2, this);
				if (f2 > 3200.0F || Vtarg.z > 1500.0)
				{
					submaneuver = 0;
					sub_Man_Count = 0;
				}
				else
				{
					if (Ve.x < 0.30000001192092896)
					{
						Vtarg.z += ((double) (0.01F * (float) Skill * (800.0F + f2)) * (0.30000001192092896 - Ve.x));
						Ve.set(Vtarg);
						Or.transformInv(Ve);
						Ve.normalize();
					}
					attackTurnToDirection(f2, f, 10.0F);
					if (target.getSpeed() > 120.0F)
					{
						setSpeedMode(2);
						tailForStaying = target;
					}
					else
					{
						setSpeedMode(4);
						smConstSpeed = 120.0F;
					}
					if (f2 < 400.0F)
					{
						submaneuver++;
						sub_Man_Count = 0;
					}
				}
				break;
			}
			case SUB_MAN2:
			{
				setCheckStrike(false);
				if (rocketsDelay > 0)
					rocketsDelay--;
				if (bulletDelay > 0)
					bulletDelay--;
				if (bulletDelay == 120)
					bulletDelay = 0;
				setRandomTargDeviation(0.8F);
				Ve.set(TargV);
				Ve.add(TargDevV);
				target.Or.transform(Ve);
				Ve.add(target.Loc);
				Ve.sub(Loc);
				float f3 = (float) Ve.length();
				float f6 = 0.55F + 0.15F * (float) Skill;
				tmpV3f.sub(target.Vwld, Vwld);
				tmpV3f.scale((double) (f3 * bullTime * f6));
				Ve.add(tmpV3f);
				Vtarg.set(Ve);
				Or.transformInv(Ve);
				if (f3 > 4000.0F || Vtarg.z > 2000.0)
				{
					submaneuver = 0;
					sub_Man_Count = 0;
				}
				else
				{
					if (AS.astatePilotStates[1] < 100 && f3 < 330.0F && Math.abs(Or.getKren()) < 3.0F)
						CT.WeaponControl[0] = true;
					Ve.normalize();
					if (Ve.x < 0.30000001192092896)
					{
						Vtarg.z += ((double) (0.01F * (float) Skill * (800.0F + f3)) * (0.30000001192092896 - Ve.x));
						Ve.set(Vtarg);
						Or.transformInv(Ve);
						Ve.normalize();
					}
					attackTurnToDirection(f3, f, 6.0F + (float) Skill * 3.0F);
					setSpeedMode(1);
					tailForStaying = target;
					tailOffset.set(-190.0, 0.0, 0.0);
					if (sub_Man_Count > 10000 || f3 < 240.0F)
					{
						push(9);
						pop();
					}
				}
				break;
			}
			default:
				submaneuver = SUB_MAN0;
				sub_Man_Count = 0;
		}
	}

	public void fighterVsFighter(float f)
	{
		if (sub_Man_Count == 0)
		{
			float f1 = 0.0F;
			if (CT.Weapons[1] != null && CT.Weapons[1][0].countBullets() > 0)
				f1 = ((GunGeneric) CT.Weapons[1][0]).bulletSpeed();
			else if (CT.Weapons[0] != null)
				f1 = ((GunGeneric) CT.Weapons[0][0]).bulletSpeed();
			if (f1 > 0.01F)
				bullTime = 1.0F / f1;
		}
		maxAOA = 15.0F;
		if (rocketsDelay > 0)
			rocketsDelay--;
		if (bulletDelay > 0)
			bulletDelay--;
		if (bulletDelay == 122)
			bulletDelay = 0;
		Ve.sub(target.Loc, Loc);
		setRandomTargDeviation(0.3F);
		Ve.add(TargDevV);
		Vtarg.set(Ve);
		float f2 = (float) Ve.length();
		float f3 = 0.55F + 0.15F * (float) Skill;
		float f4 = 2.0E-4F * (float) Skill;
		tmpV3f.sub(target.Vwld, Vwld);
		Vpl.set(tmpV3f);
		tmpV3f.scale((double) (f2 * (bullTime + f4) * f3));
		Ve.add(tmpV3f);
		tmpV3f.set(Vpl);
		tmpV3f.scale((double) (f2 * bullTime * f3));
		Vtarg.add(tmpV3f);
		Or.transformInv(Vpl);
		float f5 = (float) -Vpl.x;
		if (f5 < 0.0010F)
			f5 = 0.0010F;
		Vpl.normalize();
		if (Vpl.x < -0.9399999976158142 && f2 / f5 < 1.5F * (float) (3 - Skill))
		{
			push();
			set_maneuver(14);
		}
		else
		{
			Or.transformInv(Vtarg);
			if (Vtarg.x > 0.0 && f2 < 500.0F && (shotAtFriend <= 0 || distToFriend > f2))
			{
				if (Math.abs(Vtarg.y) < 13.0 && Math.abs(Vtarg.z) < 13.0)
					((Maneuver) target).incDangerAggressiveness(1, 0.95F, f2, this);
				if (Math.abs(Vtarg.y) < (double) (12.5F - 3.5F * (float) Skill) && Math.abs(Vtarg.z) < 12.5 - 3.5 * (double) Skill && bulletDelay < 120)
				{
					if (!(actor instanceof KI_46_OTSUHEI))
						CT.WeaponControl[0] = true;
					bulletDelay += 2;
					if (bulletDelay >= 118)
					{
						int i = (int) (target.getW().length() * 150.0);
						if (i > 60)
							i = 60;
						bulletDelay += World.Rnd().nextInt(i * Skill, 2 * i * Skill);
					}
					if (f2 < 400.0F)
					{
						CT.WeaponControl[1] = true;
						if (f2 < 100.0F && CT.Weapons[2] != null && CT.Weapons[2][0] != null && CT.Weapons[2][CT.Weapons[2].length - 1].countBullets() != 0 && rocketsDelay < 1)
						{
							tmpV3f.sub(target.Vwld, Vwld);
							Or.transformInv(tmpV3f);
							if (Math.abs(tmpV3f.y) < 4.0 && Math.abs(tmpV3f.z) < 4.0)
								CT.WeaponControl[2] = true;
							Voice.speakAttackByRockets((Aircraft) actor);
							rocketsDelay += 120;
						}
					}
					((Pilot) target).setAsDanger(actor);
				}
			}
			Vtarg.set(Ve);
			Or.transformInv(Ve);
			Ve.normalize();
			((Maneuver) target).incDangerAggressiveness(1, (float) Ve.x, f2, this);
			if (Ve.x < 0.30000001192092896)
			{
				Vtarg.z += ((double) (0.01F * (float) Skill * (800.0F + f2)) * (0.30000001192092896 - Ve.x));
				Ve.set(Vtarg);
				Or.transformInv(Ve);
				Ve.normalize();
			}
			attackTurnToDirection(f2, f, 10.0F + (float) Skill * 8.0F);
			if (Alt > 150.0F && Ve.x > 0.6 && (double) f2 < 70.0)
			{
				setSpeedMode(1);
				tailForStaying = target;
				tailOffset.set(-20.0, 0.0, 0.0);
			}
			else
				setSpeedMode(9);
		}
	}

	public void boomAttack(float f)
	{
		if (sub_Man_Count == 0)
		{
			float f1 = 0.0F;
			if (CT.Weapons[1] != null && CT.Weapons[1][0].countBullets() > 0)
				f1 = ((GunGeneric) CT.Weapons[1][0]).bulletSpeed();
			else if (CT.Weapons[0] != null)
				f1 = ((GunGeneric) CT.Weapons[0][0]).bulletSpeed();
			if (f1 > 0.01F)
				bullTime = 1.0F / f1;
		}
		maxAOA = 15.0F;
		if (rocketsDelay > 0)
			rocketsDelay--;
		if (bulletDelay > 0)
			bulletDelay--;
		if (bulletDelay == 122)
			bulletDelay = 0;
		Ve.sub(target.Loc, Loc);
		setRandomTargDeviation(0.3F);
		Ve.add(TargDevV);
		Vtarg.set(Ve);
		float f2 = (float) Ve.length();
		float f3 = 0.55F + 0.15F * (float) Skill;
		float f4 = 3.33E-4F * (float) Skill;
		tmpV3f.sub(target.Vwld, Vwld);
		Vpl.set(tmpV3f);
		tmpV3f.scale((double) (f2 * (bullTime + f4) * f3));
		Ve.add(tmpV3f);
		tmpV3f.set(Vpl);
		tmpV3f.scale((double) (f2 * bullTime * f3));
		Vtarg.add(tmpV3f);
		Or.transformInv(Vpl);
		float f5 = (float) -Vpl.x;
		if (f5 < 0.0010F)
			f5 = 0.0010F;
		Vpl.normalize();
		if (Vpl.x < -0.9399999976158142 && f2 / f5 < 1.5F * (float) (3 - Skill))
		{
			push();
			set_maneuver(14);
		}
		else
		{
			Or.transformInv(Vtarg);
			if (Vtarg.x > 0.0 && f2 < 700.0F && (shotAtFriend <= 0 || distToFriend > f2) && Math.abs(Vtarg.y) < (double) (15.5F - 3.5F * (float) Skill)
					&& Math.abs(Vtarg.z) < 15.5 - 3.5 * (double) Skill && bulletDelay < 120)
			{
				((Maneuver) target).incDangerAggressiveness(1, 0.8F, f2, this);
				if (!(actor instanceof KI_46_OTSUHEI))
					CT.WeaponControl[0] = true;
				bulletDelay += 2;
				if (bulletDelay >= 118)
				{
					int i = (int) (target.getW().length() * 150.0);
					if (i > 60)
						i = 60;
					bulletDelay += World.Rnd().nextInt(i * Skill, 2 * i * Skill);
				}
				if (f2 < 500.0F)
				{
					CT.WeaponControl[1] = true;
					if (f2 < 100.0F && CT.Weapons[2] != null && CT.Weapons[2][0] != null && CT.Weapons[2][CT.Weapons[2].length - 1].countBullets() != 0 && rocketsDelay < 1)
					{
						tmpV3f.sub(target.Vwld, Vwld);
						Or.transformInv(tmpV3f);
						if (Math.abs(tmpV3f.y) < 4.0 && Math.abs(tmpV3f.z) < 4.0)
							CT.WeaponControl[2] = true;
						Voice.speakAttackByRockets((Aircraft) actor);
						rocketsDelay += 120;
					}
				}
				((Pilot) target).setAsDanger(actor);
			}
			Vtarg.set(Ve);
			Or.transformInv(Ve);
			Ve.normalize();
			((Maneuver) target).incDangerAggressiveness(1, (float) Ve.x, f2, this);
			if (Ve.x < 0.8999999761581421)
			{
				Vtarg.z += ((double) (0.03F * (float) Skill * (800.0F + f2)) * (0.8999999761581421 - Ve.x));
				Ve.set(Vtarg);
				Or.transformInv(Ve);
				Ve.normalize();
			}
			attackTurnToDirection(f2, f, 10.0F + (float) Skill * 8.0F);
		}
	}

	private void turnToDirection(float f)
	{
		if (Math.abs(Ve.y) > 0.10000000149011612)
			CT.AileronControl = -(float) Math.atan2(Ve.y, Ve.z) - 0.016F * Or.getKren();
		else
			CT.AileronControl = -(float) Math.atan2(Ve.y, Ve.x) - 0.016F * Or.getKren();
		CT.RudderControl = -10.0F * (float) Math.atan2(Ve.y, Ve.x);
		if ((double) CT.RudderControl * W.z > 0.0)
			W.z = 0.0;
		else
			W.z *= 1.0399999618530273;
		float f1 = (float) Math.atan2(Ve.z, Ve.x);
		if (Math.abs(Ve.y) < 0.0020000000949949026 && Math.abs(Ve.z) < 0.0020000000949949026)
			f1 = 0.0F;
		if (Ve.x < 0.0)
			f1 = 1.0F;
		else
		{
			if ((double) f1 * W.y > 0.0)
				W.y = 0.0;
			if (f1 < 0.0F)
			{
				if (this.getOverload() < 0.1F)
					f1 = 0.0F;
				if (CT.ElevatorControl > 0.0F)
					CT.ElevatorControl = 0.0F;
			}
			else if (CT.ElevatorControl < 0.0F)
				CT.ElevatorControl = 0.0F;
		}
		if (this.getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > f1)
			CT.ElevatorControl -= 0.3F * f;
		else
			CT.ElevatorControl += 0.3F * f;
	}

	private void farTurnToDirection()
	{
		farTurnToDirection(4.0F);
	}

	private void farTurnToDirection(float f)
	{
		Vpl.set(1.0, 0.0, 0.0);
		tmpV3f.cross(Vpl, Ve);
		Or.transform(tmpV3f);
		CT.RudderControl = -10.0F * (float) Math.atan2(Ve.y, Ve.x) + 1.0F * (float) W.y;
		float f7 = this.getSpeed() / Vmax * 45.0F;
		if (f7 > 85.0F)
			f7 = 85.0F;
		float f8 = (float) Ve.x;
		if (f8 < 0.0F)
			f8 = 0.0F;
		float f1;
		if (tmpV3f.z >= 0.0)
			f1 = (-0.02F * (f7 + Or.getKren()) * (1.0F - f8) - 0.05F * Or.getTangage() + 1.0F * (float) W.x);
		else
			f1 = (-0.02F * (-f7 + Or.getKren()) * (1.0F - f8) + 0.05F * Or.getTangage() + 1.0F * (float) W.x);
		float f2 = (-(float) Math.atan2(Ve.y, Ve.x) - 0.016F * Or.getKren() + 1.0F * (float) W.x);
		float f4 = -0.1F * (this.getAOA() - 10.0F) + 0.5F * (float) this.getW().y;
		float f5;
		if (Ve.z > 0.0)
			f5 = -0.1F * (this.getAOA() - f) + 0.5F * (float) this.getW().y;
		else
			f5 = 1.0F * (float) Ve.z + 0.5F * (float) this.getW().y;
		if (Math.abs(Ve.y) < 0.0020000000949949026)
		{
			f2 = 0.0F;
			CT.RudderControl = 0.0F;
		}
		tmpV3f.set(Ve);
		Or.transform(tmpV3f);
		float f9 = (float) Math.atan2(tmpV3f.y, tmpV3f.x) * 180.0F / 3.1415F;
		float f10 = 1.0F - Math.abs(f9 - Or.getYaw()) * 0.01F;
		if (f10 < 0.0F || Ve.x < 0.0)
			f10 = 0.0F;
		float f3 = f10 * f2 + (1.0F - f10) * f1;
		float f6 = f10 * f5 + (1.0F - f10) * f4;
		CT.AileronControl = f3;
		CT.ElevatorControl = f6;
	}

	private void attackTurnToDirection(float f, float f1, float f2)
	{
		if (Ve.x < 0.009999999776482582)
			Ve.x = 0.009999999776482582;
		if (sub_Man_Count == 0)
			oldVe.set(Ve);
		if (Ve.x > 0.949999988079071)
		{
			CT.RudderControl = (float) (-30.0 * Math.atan2(Ve.y, Ve.x) + 1.5 * (Ve.y - oldVe.y));
			float f3;
			if (Ve.z > 0.0 || CT.RudderControl > 0.9F)
			{
				f3 = (float) (10.0 * Math.atan2(Ve.z, Ve.x) + 6.0 * (Ve.z - oldVe.z));
				CT.AileronControl = (-30.0F * (float) Math.atan2(Ve.y, Ve.x) - 0.02F * Or.getKren() + 5.0F * (float) W.x);
			}
			else
			{
				f3 = (float) (5.0 * Math.atan2(Ve.z, Ve.x) + 6.0 * (Ve.z - oldVe.z));
				CT.AileronControl = (-5.0F * (float) Math.atan2(Ve.y, Ve.x) - 0.02F * Or.getKren() + 5.0F * (float) W.x);
			}
			if (Ve.x > (double) (1.0F - 0.0050F * (float) Skill))
			{
				tmpOr.set(Or);
				tmpOr.increment((float) Math.toDegrees(Math.atan2(Ve.y, Ve.x)), (float) Math.toDegrees(Math.atan2(Ve.z, Ve.x)), 0.0F);
				Or.interpolate(tmpOr, 0.1F);
			}
			if (Math.abs(CT.ElevatorControl - f3) < f2 * f1)
				CT.ElevatorControl = f3;
			else if (CT.ElevatorControl < f3)
				CT.ElevatorControl += f2 * f1;
			else
				CT.ElevatorControl -= f2 * f1;
		}
		else
		{
			if (Skill >= 2 && Ve.z > 0.5 && f < 600.0F)
				CT.FlapsControl = 0.1F;
			else
				CT.FlapsControl = 0.0F;
			float f5 = 0.6F - (float) Ve.z;
			if (f5 < 0.0F)
				f5 = 0.0F;
			CT.RudderControl = (float) (-30.0 * Math.atan2(Ve.y, Ve.x) * (double) f5 + 1.0 * (Ve.y - oldVe.y) * Ve.x + 0.5 * W.z);
			float f4;
			if (Ve.z > 0.0)
			{
				f4 = (float) (10.0 * Math.atan2(Ve.z, Ve.x) + 6.0 * (Ve.z - oldVe.z) + 0.5 * (double) (float) W.y);
				if (f4 < 0.0F)
					f4 = 0.0F;
				CT.AileronControl = (float) (-20.0 * Math.atan2(Ve.y, Ve.z) - 0.05 * (double) Or.getKren() + 5.0 * W.x);
			}
			else
			{
				f4 = (float) (-5.0 * Math.atan2(Ve.z, Ve.x) + 6.0 * (Ve.z - oldVe.z) + 0.5 * (double) (float) W.y);
				CT.AileronControl = (float) (-20.0 * Math.atan2(Ve.y, Ve.z) - 0.05 * (double) Or.getKren() + 5.0 * W.x);
			}
			if (f4 < 0.0F)
				f4 = 0.0F;
			if (Math.abs(CT.ElevatorControl - f4) < f2 * f1)
				CT.ElevatorControl = f4;
			else if (CT.ElevatorControl < f4)
				CT.ElevatorControl += 0.3F * f1;
			else
				CT.ElevatorControl -= 0.3F * f1;
		}
		float f6 = 0.054F * (600.0F - f);
		if (f6 < 4.0F)
			f6 = 4.0F;
		if (f6 > AOA_Crit)
			f6 = AOA_Crit;
		if (AOA > f6 - 1.5F)
			Or.increment(0.0F, 0.16F * (f6 - 1.5F - AOA), 0.0F);
		if (AOA < -5.0F)
			Or.increment(0.0F, 0.12F * (-5.0F - AOA), 0.0F);
		oldVe.set(Ve);
		sub_Man_Count++;
		W.x *= 0.95;
	}

	private void doCheckStrike()
	{
		if ((!(M.massEmpty > 5000.0F) || AP.way.isLanding()) && (maneuver != 24 || strikeTarg != Leader) && (!(actor instanceof TypeDockable) || !((TypeDockable) actor).typeDockableIsDocked()))
		{
			Vpl.sub(strikeTarg.Loc, Loc);
			tmpV3f.set(Vpl);
			Or.transformInv(tmpV3f);
			if (!(tmpV3f.x < 0.0))
			{
				tmpV3f.sub(strikeTarg.Vwld, Vwld);
				tmpV3f.scale(0.699999988079071);
				Vpl.add(tmpV3f);
				Or.transformInv(Vpl);
				push();
				if (Vpl.z > 0.0)
					push(61);
				else
					push(60);
				safe_pop();
			}
		}
	}

	public void setStrikeEmer(FlightModel flightmodel)
	{
		strikeEmer = true;
		strikeTarg = flightmodel;
	}

	protected void wingman(boolean flag)
	{
		if (Wingman != null)
			Wingman.Leader = flag ? this : null;
	}

	public float getMnTime()
	{
		return mn_time;
	}

	public void doDumpBombsPassively()
	{
		boolean flag = false;
		for (int i = 0; i < CT.Weapons.length; i++)
		{
			if (CT.Weapons[i] != null && CT.Weapons[i].length > 0)
			{
				for (int j = 0; j < CT.Weapons[i].length; j++)
				{
					if (CT.Weapons[i][j] instanceof BombGun)
					{
						if (CT.Weapons[i][j] instanceof BombGunPara || (actor instanceof SM79 && CT.Weapons[i][j] instanceof TorpedoGun))
							flag = true;
						else
						{
							((BombGun) CT.Weapons[i][j]).setBombDelay(3.3E7F);
							if (actor == World.getPlayerAircraft() && !World.cur().diffCur.Limited_Ammo)
								CT.Weapons[i][j].loadBullets(0);
						}
					}
				}
			}
		}
		if (!flag)
			bombsOut = true;
	}

	protected void printDebugFM()
	{
		System.out.print("<" + actor.name() + "> " + ManString.tname(task) + ":" + ManString.name(maneuver) + (target == null ? "t" : "T") + (danger == null ? "d" : "D")
				+ (target_ground == null ? "g " : "G "));
		switch (maneuver)
		{
			case 21:
				System.out.println(": WP=" + AP.way.Cur() + "(" + (AP.way.size() - 1) + ")-" + ManString.wpname(AP.way.curr().Action));
				if (target_ground != null)
					System.out.println(" GT=" + target_ground.getClass().getName() + "(" + target_ground.name() + ")");
			break;
			case 27:
				if (target == null || !Actor.isValid(target.actor))
					System.out.println(" T=null");
				else
					System.out.println(" T=" + target.actor.getClass().getName() + "(" + target.actor.name() + ")");
			break;
			case 43:
			case 50:
			case 51:
			case 52:
				if (target_ground == null || !Actor.isValid(target_ground))
					System.out.println(" GT=null");
				else
					System.out.println(" GT=" + target_ground.getClass().getName() + "(" + target_ground.name() + ")");
			break;
			default:
				System.out.println("");
				if (target == null || !Actor.isValid(target.actor))
					System.out.println(" T=null");
				else
				{
					System.out.println(" T=" + target.actor.getClass().getName() + "(" + target.actor.name() + ")");
					if (target_ground == null || !Actor.isValid(target_ground))
						System.out.println(" GT=null");
					else
						System.out.println(" GT=" + target_ground.getClass().getName() + "(" + target_ground.name() + ")");
				}
		}
	}

	protected void headTurn(float f)
	{
		if (actor == Main3D.cur3D().viewActor() && AS.astatePilotStates[0] < 90)
		{
			boolean flag = false;
			switch (get_task())
			{
				case 2:
					if (Leader != null)
					{
						Ve.set(Leader.Loc);
						flag = true;
					}
				break;
				case 6:
					if (target != null)
					{
						Ve.set(target.Loc);
						flag = true;
					}
				break;
				case 5:
					if (airClient != null)
					{
						Ve.set(airClient.Loc);
						flag = true;
					}
				break;
				case 4:
					if (danger != null)
					{
						Ve.set(danger.Loc);
						flag = true;
					}
				break;
				case 7:
					if (target_ground != null)
					{
						Ve.set(target_ground.pos.getAbsPoint());
						flag = true;
					}
				break;
			}
			float f1;
			float f2;
			if (flag)
			{
				Ve.sub(Loc);
				Or.transformInv(Ve);
				tmpOr.setAT0(Ve);
				f1 = tmpOr.getTangage();
				f2 = tmpOr.getYaw();
				if (f2 > 75.0F)
					f2 = 75.0F;
				if (f2 < -75.0F)
					f2 = -75.0F;
				if (f1 < -15.0F)
					f1 = -15.0F;
				if (f1 > 40.0F)
					f1 = 40.0F;
			}
			else if (get_maneuver() != 44)
			{
				f1 = 0.0F;
				f2 = 0.0F;
			}
			else
			{
				f2 = -15.0F;
				f1 = -15.0F;
			}
			if (Math.abs(pilotHeadT - f1) > 3.0F)
			{
				Maneuver maneuver = this;
				maneuver.pilotHeadT = (maneuver.pilotHeadT + 90.0F * (pilotHeadT <= f1 ? 1.0F : -1.0F) * f);
			}
			else
				pilotHeadT = f1;
			if (Math.abs(pilotHeadY - f2) > 2.0F)
			{
				Maneuver maneuver = this;
				maneuver.pilotHeadY = (maneuver.pilotHeadY + 60.0F * (pilotHeadY <= f2 ? 1.0F : -1.0F) * f);
			}
			else
				pilotHeadY = f2;
			tmpOr.setYPR(0.0F, 0.0F, 0.0F);
			tmpOr.increment(0.0F, pilotHeadY, 0.0F);
			tmpOr.increment(pilotHeadT, 0.0F, 0.0F);
			tmpOr.increment(0.0F, 0.0F, -0.2F * pilotHeadT + 0.05F * pilotHeadY);
			headOr[0] = tmpOr.getYaw();
			headOr[1] = tmpOr.getPitch();
			headOr[2] = tmpOr.getRoll();
			headPos[0] = 5.0E-4F * Math.abs(pilotHeadY);
			headPos[1] = -1.0E-4F * Math.abs(pilotHeadY);
			headPos[2] = 0.0F;
			((ActorHMesh) actor).hierMesh().chunkSetLocate("Head1_D0", headPos, headOr);
		}
	}

	protected void turnOffTheWeapon()
	{
		CT.WeaponControl[0] = false;
		CT.WeaponControl[1] = false;
		CT.WeaponControl[2] = false;
		CT.WeaponControl[3] = false;
		if (bombsOut)
		{
			bombsOutCounter++;
			if (bombsOutCounter > 128)
			{
				bombsOutCounter = 0;
				bombsOut = false;
			}
			if (CT.Weapons[3] != null)
				CT.WeaponControl[3] = true;
			else
				bombsOut = false;
			if (CT.Weapons[3] != null && CT.Weapons[3][0] != null)
			{
				int i = 0;
				for (int j = 0; j < CT.Weapons[3].length; j++)
					i += CT.Weapons[3][j].countBullets();
				if (i == 0)
				{
					bombsOut = false;
					for (int k = 0; k < CT.Weapons[3].length; k++)
						CT.Weapons[3][k].loadBullets(0);
				}
			}
		}
	}

	protected void turnOnCloudShine(boolean flag)
	{
		if (flag)
		{
			if (World.Sun().ToSun.z < -0.22F)
				AS.setLandingLightState(true);
		}
		else
			AS.setLandingLightState(false);
	}

	protected void doCheckGround(float f)
	{
		if (CT.AileronControl > 1.0F)
			CT.AileronControl = 1.0F;
		if (CT.AileronControl < -1.0F)
			CT.AileronControl = -1.0F;
		if (CT.ElevatorControl > 1.0F)
			CT.ElevatorControl = 1.0F;
		if (CT.ElevatorControl < -1.0F)
			CT.ElevatorControl = -1.0F;
		float f4 = 3.0E-4F * M.massEmpty;
		float f5 = 10.0F;
		float f6 = 80.0F;
		if (maneuver == 24)
		{
			f5 = 15.0F;
			f6 = 120.0F;
		}
		float f7 = (float) -Vwld.z * f5 * f4;
		float f8 = 1.0F + 7.0F * ((indSpeed - VmaxAllowed) / VmaxAllowed);
		if (f8 > 1.0F)
			f8 = 1.0F;
		if (f8 < 0.0F)
			f8 = 0.0F;
		float f1;
		float f2;
		float f3;
		if (f7 > Alt || Alt < f6 || f8 > 0.0F)
		{
			if (Alt < 0.0010F)
				Alt = 0.0010F;
			f1 = (f7 - Alt) / Alt;
			Math.max(0.01667 * (double) (f6 - Alt), (double) f1);
			if (f1 > 1.0F)
				f1 = 1.0F;
			if (f1 < 0.0F)
				f1 = 0.0F;
			if (f1 < f8)
				f1 = f8;
			f2 = -0.12F * (Or.getTangage() - 5.0F) + 3.0F * (float) W.y;
			f3 = -0.07F * Or.getKren() + 3.0F * (float) W.x;
			if (f3 > 2.0F)
				f3 = 2.0F;
			if (f3 < -2.0F)
				f3 = -2.0F;
			if (f2 > 2.0F)
				f2 = 2.0F;
			if (f2 < -2.0F)
				f2 = -2.0F;
		}
		else
		{
			f1 = 0.0F;
			f2 = 0.0F;
			f3 = 0.0F;
		}
		float f9 = 0.2F;
		if (corrCoeff < f1)
			corrCoeff = f1;
		if (corrCoeff > f1)
			corrCoeff *= 1.0 - (double) (f9 * f);
		if (f2 < 0.0F)
		{
			if (corrElev > f2)
				corrElev = f2;
			if (corrElev < f2)
				corrElev *= 1.0 - (double) (f9 * f);
		}
		else
		{
			if (corrElev < f2)
				corrElev = f2;
			if (corrElev > f2)
				corrElev *= 1.0 - (double) (f9 * f);
		}
		if (f3 < 0.0F)
		{
			if (corrAile > f3)
				corrAile = f3;
			if (corrAile < f3)
				corrAile *= 1.0 - (double) (f9 * f);
		}
		else
		{
			if (corrAile < f3)
				corrAile = f3;
			if (corrAile > f3)
				corrAile *= 1.0 - (double) (f9 * f);
		}
		CT.AileronControl = corrCoeff * corrAile + (1.0F - corrCoeff) * CT.AileronControl;
		CT.ElevatorControl = corrCoeff * corrElev + (1.0F - corrCoeff) * CT.ElevatorControl;
		if (Alt < 15.0F && Vwld.z < 0.0)
			Vwld.z *= 0.8500000238418579;
		if (-Vwld.z * 1.5 > (double) Alt || Alt < 10.0F)
		{
			if (maneuver == 27 || maneuver == 43 || maneuver == 21 || maneuver == 24 || maneuver == 68 || maneuver == 53)
				push();
			push(2);
			pop();
			submaneuver = SUB_MAN0;
			sub_Man_Count = 0;
		}
		W.x *= 0.95;
	}

	protected void setSpeedControl(float f)
	{
		float f1 = 0.8F;
		float f4 = 0.02F;
		float f5 = 1.5F;
		CT.setAfterburnerControl(false);
		switch (speedMode)
		{
			case 1:
				if (tailForStaying == null)
					f1 = 1.0F;
				else
				{
					Ve.sub(tailForStaying.Loc, Loc);
					tailForStaying.Or.transform(tailOffset, tmpV3f);
					Ve.add(tmpV3f);
					float f14 = (float) Ve.z;
					float f6 = 0.0050F * (200.0F - (float) Ve.length());
					Or.transformInv(Ve);
					float f10 = (float) Ve.x;
					Ve.normalize();
					f6 = Math.max(f6, (float) Ve.x);
					if (f6 < 0.0F)
						f6 = 0.0F;
					Ve.set(Vwld);
					Ve.normalize();
					tmpV3f.set(tailForStaying.Vwld);
					tmpV3f.normalize();
					float f8 = (float) tmpV3f.dot(Ve);
					if (f8 < 0.0F)
						f8 = 0.0F;
					f6 *= f8;
					if (f6 > 0.0F && f10 < 1000.0F)
					{
						if (f10 > 300.0F)
							f10 = 300.0F;
						float f17 = 0.6F;
						if (tailForStaying.VmaxH == VmaxH)
							f17 = tailForStaying.CT.getPower();
						if (Vmax < 83.0F)
						{
							float f19 = f14;
							if (f19 > 0.0F)
							{
								if (f19 > 20.0F)
									f19 = 20.0F;
								Vwld.z += (double) (0.01F * f19);
							}
						}
						float f12;
						if (f10 > 0.0F)
							f12 = 0.0070F * f10 + 0.0050F * f14;
						else
							f12 = 0.03F * f10 + 0.0010F * f14;
						float f20 = this.getSpeed() - tailForStaying.getSpeed();
						float f22 = -0.3F * f20;
						float f25 = -3.0F * (this.getForwAccel() - tailForStaying.getForwAccel());
						if (f20 > 27.0F)
						{
							CT.FlapsControl = 1.0F;
							f1 = 0.0F;
						}
						else
						{
							CT.FlapsControl = 0.02F * f20 + 0.02F * -f10;
							f1 = f17 + f12 + f22 + f25;
						}
					}
					else
						f1 = 1.1F;
				}
			break;
			case 2:
				f1 = (float) (1.0 - (8.0E-5 * (0.5 * Vwld.lengthSquared() - 9.8 * Ve.z - 0.5 * tailForStaying.Vwld.lengthSquared())));
			break;
			case 3:
			{
				f1 = CT.PowerControl;
				if (AP.way.curr().Speed < 10.0F)
					AP.way.curr().set(1.7F * Vmin);
				float f18 = AP.way.curr().Speed / VmaxH;
				f1 = 0.2F + 0.8F * (float) Math.pow((double) f18, 1.5);
				f1 += (0.1F * (AP.way.curr().Speed - Pitot.Indicator((float) Loc.z, this.getSpeed())) - 3.0F * this.getForwAccel());
				if (this.getAltitude() < AP.way.curr().z() - 70.0F)
					f1 += AP.way.curr().z() - 70.0F - this.getAltitude();
				if (f1 > 0.93F)
					f1 = 0.93F;
				if (f1 < 0.35F && !AP.way.isLanding())
					f1 = 0.35F;
				break;
			}
			case 4:
				f1 = CT.PowerControl;
				f1 += (((double) (f4 * (smConstSpeed - Pitot.Indicator((float) Loc.z, this.getSpeed()))) - (double) f5 * this.getLocalAccel().x / 9.8100004196167) * (double) f);
				if (f1 > 1.0F)
					f1 = 1.0F;
			break;
			case 5:
				f1 = CT.PowerControl;
				CT.FlapsControl = 1.0F;
				f1 += (f4 * (1.3F * VminFLAPS - Pitot.Indicator((float) Loc.z, this.getSpeed())) - f5 * this.getForwAccel()) * f;
			break;
			case 8:
				f1 = 0.0F;
			break;
			case 6:
				f1 = 1.0F;
			break;
			case 9:
				f1 = 1.1F;
				CT.setAfterburnerControl(true);
			break;
			case 7:
				f1 = smConstPower;
			break;
			case 10:
				if (tailForStaying == null)
					f1 = 1.0F;
				else
				{
					Ve.sub(tailForStaying.Loc, Loc);
					tailForStaying.Or.transform(tailOffset, tmpV3f);
					Ve.add(tmpV3f);
					float f15 = (float) Ve.z;
					float f7 = 0.0050F * (200.0F - (float) Ve.length());
					Or.transformInv(Ve);
					float f11 = (float) Ve.x;
					Ve.normalize();
					f7 = Math.max(f7, (float) Ve.x);
					if (f7 < 0.0F)
						f7 = 0.0F;
					Ve.set(Vwld);
					Ve.normalize();
					tmpV3f.set(tailForStaying.Vwld);
					tmpV3f.normalize();
					float f9 = (float) tmpV3f.dot(Ve);
					if (f9 < 0.0F)
						f9 = 0.0F;
					f7 *= f9;
					if (f7 > 0.0F && f11 < 1000.0F)
					{
						if (f11 > 300.0F)
							f11 = 300.0F;
						float f21 = 0.6F;
						if (tailForStaying.VmaxH == VmaxH)
							f21 = tailForStaying.CT.getPower();
						if (Vmax < 83.0F)
						{
							float f23 = f15;
							if (f23 > 0.0F)
							{
								if (f23 > 20.0F)
									f23 = 20.0F;
								Vwld.z += (double) (0.01F * f23);
							}
						}
						float f13;
						if (f11 > 0.0F)
							f13 = 0.0070F * f11 + 0.0050F * f15;
						else
							f13 = 0.03F * f11 + 0.0010F * f15;
						float f24 = this.getSpeed() - tailForStaying.getSpeed();
						float f26 = -0.3F * f24;
						float f27 = -3.0F * (this.getForwAccel() - tailForStaying.getForwAccel());
						if (f24 > 27.0F)
						{
							Vwld.scale(0.9800000190734863);
							f1 = 0.0F;
						}
						else
						{
							float f28 = 1.0F - 0.02F * (0.02F * f24 + 0.02F * -f11);
							if (f28 < 0.98F)
								f28 = 0.98F;
							if (f28 > 1.0F)
								f28 = 1.0F;
							Vwld.scale((double) f28);
							f1 = f21 + f13 + f26 + f27;
						}
					}
					else
						f1 = 1.1F;
				}
			break;
			default:
				return;
		}
		if (f1 > 1.1F)
			f1 = 1.1F;
		else if (f1 < 0.0F)
			f1 = 0.0F;
		if ((double) Math.abs(CT.PowerControl - f1) < 0.5 * (double) f)
			CT.setPowerControl(f1);
		else if (CT.PowerControl < f1)
			CT.setPowerControl(CT.getPowerControl() + 0.5F * f);
		else
			CT.setPowerControl(CT.getPowerControl() - 0.5F * f);
		float f16 = EI.engines[0].getCriticalW();
		if (EI.engines[0].getw() > 0.9F * f16)
		{
			float f2 = 10.0F * (f16 - EI.engines[0].getw()) / f16;
			if (f2 < CT.PowerControl)
				CT.setPowerControl(f2);
		}
		if (indSpeed > 0.8F * VmaxAllowed)
		{
			float f3 = 1.0F * (VmaxAllowed - indSpeed) / VmaxAllowed;
			if (f3 < CT.PowerControl)
				CT.setPowerControl(f3);
		}
	}

	private void setRandomTargDeviation(float f)
	{
		if (this.isTick(16, 0))
		{
			float f1 = f * (8.0F - 1.5F * (float) Skill);
			TargDevVnew.set((double) World.Rnd().nextFloat(-f1, f1), (double) World.Rnd().nextFloat(-f1, f1), (double) World.Rnd().nextFloat(-f1, f1));
		}
		TargDevV.interpolate(TargDevVnew, 0.01F);
	}

	private Point_Any getNextAirdromeWayPoint()
	{
		if (airdromeWay == null)
			return null;
		if (airdromeWay.length == 0)
			return null;
		if (curAirdromePoi >= airdromeWay.length)
			return null;
		return airdromeWay[curAirdromePoi++];
	}

	private void findPointForEmLanding(float f)
	{
		Point3d point3d = elLoc.getPoint();
		if (radius > 2.0 * (double) rmax)
		{
			if (submaneuver != SUB_MAN69)
				initTargPoint(f);
			else
				return;
		}
		for (int i = 0; i < 32; i++)
		{
			Po.set(Vtarg.x + radius * Math.sin(phase), Vtarg.y + radius * Math.cos(phase), Loc.z);
			radius += 0.01 * (double) rmax;
			phase += 0.3;
			Ve.sub(Po, Loc);
			double d = Ve.length();
			Or.transformInv(Ve);
			Ve.normalize();
			float f1 = 0.9F - 0.0050F * Alt;
			if (f1 < -1.0F)
				f1 = -1.0F;
			if (f1 > 0.8F)
				f1 = 0.8F;
			float f2 = 1.5F - 5.0E-4F * Alt;
			if (f2 < 1.0F)
				f2 = 1.0F;
			if (f2 > 1.5F)
				f2 = 1.5F;
			if ((double) rmax > d && d > (double) (rmin * f2) && Ve.x > (double) f1 && isConvenientPoint())
			{
				submaneuver = SUB_MAN69;
				point3d.set(Po);
				pointQuality = curPointQuality;
				break;
			}
		}
	}

	private boolean isConvenientPoint()
	{
		Po.z = Engine.cur.land.HQ_Air(Po.x, Po.y);
		curPointQuality = 50;
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				if (Engine.land().isWater(Po.x + 500.0 * (double) i, Po.y + 500.0 * (double) j))
				{
					if (!(actor instanceof TypeSailPlane))
						curPointQuality--;
				}
				else if (actor instanceof TypeSailPlane)
					curPointQuality--;
				if (Engine.cur.land.HQ_ForestHeightHere((Po.x + 500.0 * (double) i), (Po.y + 500.0 * (double) j)) > 1.0)
					curPointQuality -= 2;
				if (Engine.cur.land.EQN(Po.x + 500.0 * (double) i, Po.y + 500.0 * (double) j, Ve) > 1110.949951171875)
					curPointQuality -= 2;
			}
		}
		if (curPointQuality < 0)
			curPointQuality = 0;
		if (curPointQuality > pointQuality)
			return true;
		return false;
	}

	private void emergencyTurnToDirection(float f)
	{
		if (Math.abs(Ve.y) > 0.10000000149011612)
			CT.AileronControl = -(float) Math.atan2(Ve.y, Ve.z) - 0.016F * Or.getKren();
		else
			CT.AileronControl = -(float) Math.atan2(Ve.y, Ve.x) - 0.016F * Or.getKren();
		CT.RudderControl = -10.0F * (float) Math.atan2(Ve.y, Ve.x);
		if ((double) CT.RudderControl * W.z > 0.0)
			W.z = 0.0;
		else
			W.z *= 1.0399999618530273;
	}

	private void initTargPoint(float f)
	{
		int i = AP.way.Cur();
		Vtarg.set((double) AP.way.last().x(), (double) AP.way.last().y(), 0.0);
		AP.way.setCur(i);
		Vtarg.sub(Loc);
		Vtarg.z = 0.0;
		if (Vtarg.length() > (double) rmax)
		{
			Vtarg.normalize();
			Vtarg.scale((double) rmax);
		}
		Vtarg.add(Loc);
		phase = 0.0;
		radius = 50.0;
		pointQuality = -1;
	}

	private void emergencyLanding(float f)
	{
		if (first)
		{
			Kmax = Wing.new_Cya(5.0F) / Wing.new_Cxa(5.0F);
			if (Kmax > 14.0F)
				Kmax = 14.0F;
			Kmax *= 0.8F;
			rmax = 1.2F * Kmax * Alt;
			rmin = 0.6F * Kmax * Alt;
			initTargPoint(f);
			this.setReadyToDieSoftly(true);
			AP.setStabAll(false);
			if (TaxiMode)
			{
				setSpeedMode(8);
				smConstPower = 0.0F;
				push(44);
				pop();
			}
			dist = Alt;
			if (actor instanceof TypeSailPlane)
				EI.setEngineStops();
		}
		setSpeedMode(4);
		smConstSpeed = VminFLAPS * 1.25F;
		if (Alt < 500.0F && (actor instanceof TypeGlider || actor instanceof TypeSailPlane))
			CT.GearControl = 1.0F;
		if (Alt < 10.0F)
		{
			CT.AileronControl = -0.04F * Or.getKren();
			setSpeedMode(4);
			smConstSpeed = VminFLAPS * 1.1F;
			if (Alt < 5.0F)
				setSpeedMode(8);
			CT.BrakeControl = 0.2F;
			if (Vwld.length() < 0.30000001192092896 && World.Rnd().nextInt(0, 99) < 4)
			{
				this.setStationedOnGround(true);
				World.cur();
				if (actor != World.getPlayerAircraft())
				{
					push(44);
					safe_pop();
					if (actor instanceof TypeSailPlane)
					{
						EI.setCurControlAll(true);
						EI.setEngineStops();
						if (Engine.land().isWater(Loc.x, Loc.y))
							return;
					}
					((Aircraft) actor).hitDaSilk();
				}
				if (Group != null)
					Group.delAircraft((Aircraft) actor);
				if (actor instanceof TypeGlider || actor instanceof TypeSailPlane)
					return;
				World.cur();
				if (actor != World.getPlayerAircraft())
				{
					if (Airport.distToNearestAirport(Loc) > 900.0)
						((Aircraft) actor).postEndAction(60.0, actor, 4, null);
					else
						MsgDestroy.Post(Time.current() + 30000L, actor);
				}
			}
		}
		dA = 0.2F * (this.getSpeed() - Vmin * 1.3F) - 0.8F * (this.getAOA() - 5.0F);
		if (Alt < 40.0F)
		{
			CT.AileronControl = -0.04F * Or.getKren();
			CT.RudderControl = 0.0F;
			if (actor instanceof BI_1 || actor instanceof ME_163B1A)
				CT.GearControl = 1.0F;
			float f1;
			if (Gears.Pitch < 10.0F)
				f1 = (40.0F * (this.getSpeed() - VminFLAPS * 1.15F) - 60.0F * (Or.getTangage() + 3.0F) - 240.0F * (this.getVertSpeed() + 1.0F) - 120.0F * ((float) this.getAccel().z - 1.0F)) * 0.0040F;
			else
				f1 = (40.0F * (this.getSpeed() - VminFLAPS * 1.15F) - 60.0F * (Or.getTangage() - Gears.Pitch + 10.0F) - 240.0F * (this.getVertSpeed() + 1.0F) - 120.0F * ((float) this.getAccel().z - 1.0F)) * 0.0040F;
			if (Alt > 0.0F)
			{
				float f2 = 0.01666F * Alt;
				dA = dA * f2 + f1 * (1.0F - f2);
			}
			else
				dA = f1;
			CT.FlapsControl = 0.33F;
			if (Alt < 9.0F && Math.abs(Or.getKren()) < 5.0F && this.getVertSpeed() < -0.7F)
				Vwld.z *= 0.8700000047683716;
		}
		else
		{
			rmax = 1.2F * Kmax * Alt;
			rmin = 0.6F * Kmax * Alt;
			if (actor instanceof TypeGlider && Alt > 200.0F)
				CT.RudderControl = 0.0F;
			else if (pointQuality < 50 && mn_time > 0.5F)
				findPointForEmLanding(f);
			if (submaneuver == SUB_MAN69)
			{
				Ve.sub(elLoc.getPoint(), Loc);
				double d = Ve.length();
				Or.transformInv(Ve);
				Ve.normalize();
				float f3 = 0.9F - 0.0050F * Alt;
				if (f3 < -1.0F)
					f3 = -1.0F;
				if (f3 > 0.8F)
					f3 = 0.8F;
				if ((double) (rmax * 2.0F) < d || d < (double) rmin || Ve.x < (double) f3)
				{
					submaneuver = SUB_MAN0;
					initTargPoint(f);
				}
				if (d > 88.0)
				{
					emergencyTurnToDirection(f);
					if ((double) Alt > d)
						submaneuver = SUB_MAN0;
				}
				else
					CT.AileronControl = -0.04F * Or.getKren();
			}
			else
				CT.AileronControl = -0.04F * Or.getKren();
			if (Or.getTangage() > -1.0F)
				dA -= 0.1F * (Or.getTangage() + 1.0F);
			if (Or.getTangage() < -10.0F)
				dA -= 0.1F * (Or.getTangage() + 10.0F);
		}
		if (Alt < 2.0F || Gears.onGround())
			dA = -2.0F * (Or.getTangage() - Gears.Pitch);
		double d1 = Vwld.length() / (double) Vmin;
		if (d1 > 1.0)
			d1 = 1.0;
		CT.ElevatorControl += ((double) ((dA - CT.ElevatorControl) * 3.33F * f) + 1.5 * this.getW().y * d1 + 0.5 * this.getAW().y * d1);
	}

	public boolean canITakeoff()
	{
		Po.set(Loc);
		Vpl.set(69.0, 0.0, 0.0);
		Or.transform(Vpl);
		Po.add(Vpl);
		Pd.set(Po);
		if (actor != War.getNearestFriendAtPoint(Pd, (Aircraft) actor, 70.0F))
			return false;
		if (canTakeoff)
			return true;
		if (Actor.isAlive(AP.way.takeoffAirport))
		{
			if (AP.way.takeoffAirport.takeoffRequest <= 0)
			{
				AP.way.takeoffAirport.takeoffRequest = 2000;
				canTakeoff = true;
				return true;
			}
			return false;
		}
		return true;
	}

	public void set_maneuver_imm(int i)
	{
		int j = maneuver;
		maneuver = i;
		if (j != maneuver)
			set_flags();
	}

	private void addWindCorrection()
	{
		do
		{
			if (World.cur().diffCur.Wind_N_Turbulence)
			{
				World.cur();
				if (!World.wind().noWind)
					break;
			}
			return;
		}
		while (false);
		double d = Ve.length();
		World.cur();
		World.wind().getVectorAI(actor.pos.getAbsPoint(), windV);
		windV.scale(-1.0);
		Ve.normalize();
		Ve.scale((double) this.getSpeed());
		Ve.add(windV);
		Ve.normalize();
		Ve.scale(d);
	}
	
	// TODO: CTO Mod altered
	// ---------------------------------------------------
	protected void turnOnChristmasTree(boolean flag)
	{
		if (flag)
		{
			if (((com.maddox.JGP.Tuple3f) (com.maddox.il2.ai.World.Sun().ToSun)).z < -0.22F && !bNoNavLightsAI)
				super.AS.setNavLightsState(true);
		}
		else
		{
			super.AS.setNavLightsState(false);
		}
	}
	// ---------------------------------------------------
}
