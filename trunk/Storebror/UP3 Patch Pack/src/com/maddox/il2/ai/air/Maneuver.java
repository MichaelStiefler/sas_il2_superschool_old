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

public class Maneuver extends AIFlightModel {
    // @formatter:off
    private int                task;
    private int                maneuver                    = 26;
    protected float            mn_time;
    private int[]              program                     = new int[8];
    private boolean            bBusy                       = true;
    public boolean             wasBusy                     = true;
    public boolean             dontSwitch                  = false;
    public boolean             aggressiveWingman           = false;
    public boolean             kamikaze                    = false;
    public boolean             silence                     = true;
    public boolean             bombsOut;
    private int                bombsOutCounter             = 0;
    public float               direction;
    public Loc                 rwLoc;
    private boolean            first                       = true;
    private int                rocketsDelay                = 0;
    private int                bulletDelay                 = 0;
    private int                submanDelay                 = 0;
    private float              maxG;
    private float              maxAOA;
    private float              LandHQ;
    public float               Alt                         = 0.0F;
    private float              corrCoeff                   = 0.0F;
    private float              corrElev                    = 0.0F;
    private float              corrAile                    = 0.0F;
    private boolean            checkGround                 = false;
    private boolean            checkStrike                 = false;
    private boolean            frequentControl             = false;
    private boolean            stallable                   = false;
    public FlightModel         airClient                   = null;
    public FlightModel         target                      = null;
    public FlightModel         danger                      = null;
    private float              dangerAggressiveness        = 0.0F;
    private float              oldDanCoeff                 = 0.0F;
    private int                shotAtFriend                = 0;
    private float              distToFriend                = 0.0F;
    public Actor               target_ground               = null;
    public AirGroup            Group;
    protected boolean          TaxiMode                    = false;
    protected boolean          finished                    = false;
    protected boolean          canTakeoff                  = false;
    public Point_Any           wayCurPos;
    protected Point_Any        wayPrevPos;
    protected Point_Any[]      airdromeWay;
    protected int              curAirdromePoi              = 0;
    public long                actionTimerStart            = 0L;
    public long                actionTimerStop             = 0L;
    protected int              gattackCounter              = 0;
    private int                beNearOffsPhase             = 0;
    public int                 submaneuver                 = 0;
    private boolean            dont_change_subm            = false;
    private int                tmpi                        = 0;
    private int                sub_Man_Count               = 0;
    private float              dA;
    private float              dist                        = 0.0F;
    private float              man1Dist                    = 50.0F;
    private float              bullTime                    = 0.0015F;
    protected static final int STAY_ON_THE_TAIL            = 1;
    protected static final int NOT_TOO_FAST                = 2;
    protected static final int FROM_WAYPOINT               = 3;
    protected static final int CONST_SPEED                 = 4;
    protected static final int MIN_SPEED                   = 5;
    protected static final int MAX_SPEED                   = 6;
    protected static final int CONST_POWER                 = 7;
    protected static final int ZERO_POWER                  = 8;
    protected static final int BOOST_ON                    = 9;
    protected static final int FOLLOW_WITHOUT_FLAPS        = 10;
    protected int              speedMode                   = 3;
    protected float            smConstSpeed                = 90.0F;
    protected float            smConstPower                = 0.7F;
    protected FlightModel      tailForStaying              = null;
    public Vector3d            tailOffset                  = new Vector3d();
    protected int              Speak5minutes;
    protected int              Speak1minute;
    protected int              SpeakBeginGattack;
    protected boolean          WeWereInGAttack             = false;
    protected boolean          WeWereInAttack              = false;
    protected boolean          SpeakMissionAccomplished    = false;
    public static final int    ROOKIE                      = 0;
    public static final int    NORMAL                      = 1;
    public static final int    VETERAN                     = 2;
    public static final int    ACE                         = 3;
    public static final int    NO_TASK                     = 0;
    public static final int    WAIT                        = 1;
    public static final int    STAY_FORMATION              = 2;
    public static final int    FLY_WAYPOINT                = 3;
    public static final int    DEFENCE                     = 4;
    public static final int    DEFENDING                   = 5;
    public static final int    ATTACK_AIR                  = 6;
    public static final int    ATTACK_GROUND               = 7;
    public static final int    NONE                        = 0;
    public static final int    HOLD                        = 1;
    public static final int    PULL_UP                     = 2;
    public static final int    LEVEL_PLANE                 = 3;
    public static final int    ROLL                        = 4;
    public static final int    ROLL_90                     = 5;
    public static final int    ROLL_180                    = 6;
    public static final int    SPIRAL_BRAKE                = 7;
    public static final int    SPIRAL_UP                   = 8;
    public static final int    SPIRAL_DOWN                 = 9;
    public static final int    CLIMB                       = 10;
    public static final int    DIVING_0_RPM                = 11;
    public static final int    DIVING_30_DEG               = 12;
    public static final int    DIVING_45_DEG               = 13;
    public static final int    TURN                        = 14;
    public static final int    MIL_TURN                    = 15;
    public static final int    LOOP                        = 16;
    public static final int    LOOP_DOWN                   = 17;
    public static final int    HALF_LOOP_UP                = 18;
    public static final int    HALF_LOOP_DOWN              = 19;
    public static final int    STALL                       = 20;
    public static final int    WAYPOINT                    = 21;
    public static final int    SPEEDUP                     = 22;
    public static final int    BELL                        = 23;
    public static final int    FOLLOW                      = 24;
    public static final int    LANDING                     = 25;
    public static final int    TAKEOFF                     = 26;
    public static final int    ATTACK                      = 27;
    public static final int    WAVEOUT                     = 28;
    public static final int    SINUS                       = 29;
    public static final int    ZIGZAG_UP                   = 30;
    public static final int    ZIGZAG_DOWN                 = 31;
    public static final int    ZIGZAG_SPIT                 = 32;
    public static final int    HALF_LOOP_DOWN_135          = 33;
    public static final int    HARTMANN_REDOUT             = 34;
    public static final int    ROLL_360                    = 35;
    public static final int    STALL_POKRYSHKIN            = 36;
    public static final int    BARREL_POKRYSHKIN           = 37;
    public static final int    SLIDE_LEVEL                 = 38;
    public static final int    SLIDE_DESCENT               = 39;
    public static final int    RANVERSMAN                  = 40;
    public static final int    CUBAN                       = 41;
    public static final int    CUBAN_INVERT                = 42;
    public static final int    GATTACK                     = 43;
    public static final int    PILOT_DEAD                  = 44;
    public static final int    HANG_ON                     = 45;
    public static final int    DELAY                       = 48;
    public static final int    EMERGENCY_LANDING           = 49;
    public static final int    GATTACK_DIVE                = 50;
    public static final int    GATTACK_TORPEDO             = 51;
    public static final int    GATTACK_CASSETTE            = 52;
    public static final int    GATTACK_KAMIKAZE            = 46;
    public static final int    GATTACK_TINYTIM             = 47;
    public static final int    GATTACK_HS293               = 79;
    public static final int    GATTACK_FRITZX              = 80;
    public static final int    GATTACK_TORPEDO_TOKG        = 81;
    public static final int    FAR_FOLLOW                  = 53;
    public static final int    SPIRAL_DOWN_SLOW            = 54;
    public static final int    FOLLOW_SPIRAL_UP            = 55;
    public static final int    SINUS_SHALLOW               = 56;
    public static final int    GAIN                        = 57;
    public static final int    SEPARATE                    = 58;
    public static final int    BE_NEAR                     = 59;
    public static final int    EVADE_UP                    = 60;
    public static final int    EVADE_DN                    = 61;
    public static final int    ENERGY_ATTACK               = 62;
    public static final int    ATTACK_BOMBER               = 63;
    public static final int    PARKED_STARTUP              = 64;
    public static final int    COVER                       = 65;
    public static final int    TAXI                        = 66;
    public static final int    RUN_AWAY                    = 67;
    public static final int    FAR_COVER                   = 68;
    public static final int    TAKEOFF_VTOL_A              = 69;
    public static final int    LANDING_VTOL_A              = 70;
    private static final int   SUB_MAN0                    = 0;
    private static final int   SUB_MAN1                    = 1;
    private static final int   SUB_MAN2                    = 2;
    private static final int   SUB_MAN3                    = 3;
    private static final int   SUB_MAN4                    = 4;
    private static final int   SUB_MAN5                    = 5;
    private static final int   SUB_MAN69                   = 69;
    public static final int    LIVE                        = 0;
    public static final int    RETURN                      = 1;
    public static final int    TASK                        = 2;
    public static final int    PROTECT_LEADER              = 3;
    public static final int    PROTECT_WINGMAN             = 4;
    public static final int    PROTECT_FRIENDS             = 5;
    public static final int    DESTROY_ENEMIES             = 6;
    public static final int    KEEP_ORDER                  = 7;
    public float[]             takeIntoAccount             = new float[8];
    public float[]             AccountCoeff                = new float[8];
    public static final int    FVSB_BOOM_ZOOM              = 0;
    public static final int    FVSB_BOOM_ZOOM_TO_ENGINE    = 1;
    public static final int    FVSB_SHALLOW_DIVE_TO_ENGINE = 2;
    public static final int    FVSB_FROM_AHEAD             = 3;
    public static final int    FVSB_FROM_BELOW             = 4;
    public static final int    FVSB_AS_IT_IS               = 5;
    public static final int    FVSB_FROM_SIDE              = 6;
    public static final int    FVSB_FROM_TAIL_TO_ENGINE    = 7;
    public static final int    FVSB_FROM_TAIL              = 8;
    public static final int    FVSB_SHALLOW_DIVE           = 9;
    public static final int    FVSB_FROM_BOTTOM            = 10;
    private Vector3d           ApproachV                   = new Vector3d();
    private Vector3d           TargV                       = new Vector3d();
    private Vector3d           TargDevV                    = new Vector3d(0.0, 0.0, 0.0);
    private Vector3d           TargDevVnew                 = new Vector3d(0.0, 0.0, 0.0);
    private Vector3d           scaledApproachV             = new Vector3d();
    private float              ApproachR;
    private float              TargY;
    private float              TargZ;
    private float              TargYS;
    private float              TargZS;
    private float              RandomVal;
    public Vector3d            followOffset                = new Vector3d();
    private Vector3d           followTargShift             = new Vector3d(0.0, 0.0, 0.0);
    private Vector3d           followCurShift              = new Vector3d(0.0, 0.0, 0.0);
    private float              raAilShift                  = 0.0F;
    private float              raElevShift                 = 0.0F;
    private float              raRudShift                  = 0.0F;
    private float              sinKren                     = 0.0F;
    private boolean            strikeEmer                  = false;
    private FlightModel        strikeTarg                  = null;
    private boolean            direc                       = false;
    float                      Kmax                        = 10.0F;
    float                      rmin;
    float                      rmax;
    double                     phase                       = 0.0;
    double                     radius                      = 50.0;
    int                        pointQuality                = -1;
    int                        curPointQuality             = 50;
    private static Vector3d    tmpV3d                      = new Vector3d();
    private static Vector3d    tmpV3f                      = new Vector3d();
    public static Orient       tmpOr                       = new Orient();
    public Orient              saveOr                      = new Orient();
    private static Point3d     Po                          = new Point3d();
    private static Point3d     Pc                          = new Point3d();
    private static Point3d     Pd                          = new Point3d();
    // TODO: Storebror: Implement Aircraft Control Surfaces and Pilot View Replication
//	private static Vector3d		Ve							= new Vector3d();
    public static Vector3d     Ve                          = new Vector3d();
    // ---
    private Vector3d           oldVe                       = new Vector3d();
    private Vector3d           Vtarg                       = new Vector3d();
    private Vector3d           constVtarg                  = new Vector3d();
    private Vector3d           constVtarg1                 = new Vector3d();
    private static Vector3d    Vf                          = new Vector3d();
    private Vector3d           Vxy                         = new Vector3d();
    private static Vector3d    Vpl                         = new Vector3d();
    private AnglesFork         AFo                         = new AnglesFork();
    private float[]            headPos                     = new float[3];
    private float[]            headOr                      = new float[3];
    private static Point3d     P                           = new Point3d();
    private static Point2f     Pcur                        = new Point2f();
    private static Vector2d    Vcur                        = new Vector2d();
    private static Vector2f    V_to                        = new Vector2f();
    private static Vector2d    Vdiff                       = new Vector2d();
    private static Loc         elLoc                       = new Loc();
    public static boolean      showFM;
    // TODO: Storebror: Implement Aircraft Control Surfaces and Pilot View Replication
//    private float              pilotHeadT                  = 0.0F;
//    private float              pilotHeadY                  = 0.0F;
    public float               pilotHeadT                  = 0.0F;
    public float               pilotHeadY                  = 0.0F;
    // ---
    Vector3d                   windV                       = new Vector3d();

    // TODO: CTO Mod
    // -------------------------------
    private boolean            bStage3;
    private boolean            bStage4;
    private boolean            bStage6;
    private boolean            bStage7;
    private boolean            bAlreadyCheckedStage7;
    private float              fNearestDistance;
    private boolean            bCatapultAI;
    private boolean            bNoNavLightsAI;
    private boolean            bFastLaunchAI;
    // -------------------------------

    // TODO: GATTACK
    // ---------------------------------
    private int                passCounter                 = 0;

    // --------------------------------
    // @formatter:on

    public void set_task(int i) {
        this.task = i;
    }

    public int get_task() {
        return this.task;
    }

    public int get_maneuver() {
        return this.maneuver;
    }

    public void set_maneuver(int i) {
        if (this.maneuver != 44 && (i == 44 || (this.maneuver != 26 && this.maneuver != 69 && this.maneuver != 66 && this.maneuver != 46))) {
            int j = this.maneuver;
            this.maneuver = i;
            if (j != this.maneuver)
                this.set_flags();
        }
    }

    public void pop() {
        if (this.maneuver != 44 && (this.program[0] == 44 || (this.maneuver != 26 && this.maneuver != 69 && this.maneuver != 66 && this.maneuver != 46))) {
            int i = this.maneuver;
            this.maneuver = this.program[0];
            for (int j = 0; j < this.program.length - 1; j++)
                this.program[j] = this.program[j + 1];
            this.program[this.program.length - 1] = 0;
            if (i != this.maneuver)
                this.set_flags();
        }
    }

    public void unblock() {
        this.maneuver = 0;
    }

    public void safe_set_maneuver(int i) {
        this.dont_change_subm = true;
        this.set_maneuver(i);
        this.dont_change_subm = true;
    }

    public void safe_pop() {
        this.dont_change_subm = true;
        this.pop();
        this.dont_change_subm = true;
    }

    public void clear_stack() {
        for (int i = 0; i < this.program.length; i++)
            this.program[i] = 0;
    }

    public void push(int i) {
        for (int j = this.program.length - 2; j >= 0; j--)
            this.program[j + 1] = this.program[j];
        this.program[0] = i;
    }

    public void push() {
        this.push(this.maneuver);
    }

    public void accurate_set_task_maneuver(int i, int j) {
        if (this.maneuver != 44 && this.maneuver != 26 && this.maneuver != 69 && this.maneuver != 64) {
            this.set_task(i);
            if (this.maneuver != j) {
                this.clear_stack();
                this.set_maneuver(j);
            }
        }
    }

    public void accurate_set_FOLLOW() {
        if (this.maneuver != 44 && this.maneuver != 26 && this.maneuver != 69 && this.maneuver != 64) {
            this.set_task(2);
            if (this.maneuver != 24 && this.maneuver != 53) {
                this.clear_stack();
                this.set_maneuver(24);
            }
        }
    }

    public void setBusy(boolean flag) {
        this.bBusy = flag;
    }

    public boolean isBusy() {
        return this.bBusy;
    }

    public void setSpeedMode(int i) {
        this.speedMode = i;
    }

    private boolean isStallable() {
        if (this.actor instanceof TypeStormovik)
            return false;
        return !(this.actor instanceof TypeTransport);
    }

    private void resetControls() {
        this.CT.AileronControl = this.CT.BrakeControl = this.CT.ElevatorControl = this.CT.FlapsControl = this.CT.RudderControl = 0.0F;
        this.CT.AirBrakeControl = 0.0F;
    }

    private void set_flags() {
        if (World.cur().isDebugFM())
            this.printDebugFM();
        this.AP.setStabAll(false);
        this.mn_time = 0.0F;
        this.minElevCoeff = 4.0F;
        if (!this.dont_change_subm) {
            this.setSpeedMode(3);
            this.first = true;
            this.submaneuver = SUB_MAN0;
            this.sub_Man_Count = 0;
        }
        this.dont_change_subm = false;
        if (this.maneuver != 48 && this.maneuver != 0 && this.maneuver != 26 && this.maneuver != 64 && this.maneuver != 44)
            this.resetControls();
        if (this.maneuver == 20 || this.maneuver == 25 || this.maneuver == 66 || this.maneuver == 1 || this.maneuver == 26 || this.maneuver == 69 || this.maneuver == 44 || this.maneuver == 49 || this.maneuver == 43 || this.maneuver == 50
                || this.maneuver == 51 || this.maneuver == 81 || this.maneuver == 46 || this.maneuver == 2 || this.maneuver == 10 || this.maneuver == 57 || this.maneuver == 64)
            this.setCheckGround(false);
        else
            this.setCheckGround(true);
        if (this.maneuver == 24 || this.maneuver == 53 || this.maneuver == 68 || this.maneuver == 59 || this.maneuver == 8 || this.maneuver == 55 || this.maneuver == 27 || this.maneuver == 62 || this.maneuver == 63 || this.maneuver == 25
                || this.maneuver == 66 || this.maneuver == 43 || this.maneuver == 50 || this.maneuver == 65 || this.maneuver == 44 || this.maneuver == 21 || this.maneuver == 64 || this.maneuver == 69)
            this.frequentControl = true;
        else
            this.frequentControl = false;
        if (this.maneuver == 25 || this.maneuver == 26 || this.maneuver == 69 || this.maneuver == 70)
            this.turnOnChristmasTree(true);
        else
            this.turnOnChristmasTree(false);
        if (this.maneuver == 25)
            this.turnOnCloudShine(true);
        else
            this.turnOnCloudShine(false);
        if (this.maneuver == 60 || this.maneuver == 61 || this.maneuver == 66 || this.maneuver == 1 || this.maneuver == 24 || this.maneuver == 26 || this.maneuver == 69 || this.maneuver == 64 || this.maneuver == 44)
            this.checkStrike = false;
        else
            this.checkStrike = true;
        if (this.maneuver == 44 || this.maneuver == 1 || this.maneuver == 48 || this.maneuver == 0 || this.maneuver == 26 || this.maneuver == 69 || this.maneuver == 64 || this.maneuver == 43 || this.maneuver == 50 || this.maneuver == 51
                || this.maneuver == 52 || this.maneuver == 47 || this.maneuver == 79 || this.maneuver == 80)
            this.stallable = false;
        else
            this.stallable = true;
        if (this.maneuver == 44 || this.maneuver == 1 || this.maneuver == 26 || this.maneuver == 69 || this.maneuver == 64 || this.maneuver == 2 || this.maneuver == 57 || this.maneuver == 60 || this.maneuver == 61 || this.maneuver == 43
                || this.maneuver == 50 || this.maneuver == 51 || this.maneuver == 52 || this.maneuver == 47 || this.maneuver == 29 || this.maneuver == 79 || this.maneuver == 80)
            this.setBusy(true);
    }

    public void setCheckStrike(boolean flag) {
        this.checkStrike = flag;
    }

    private void setCheckGround(boolean flag) {
        this.checkGround = flag;
    }

    public Maneuver(String s) {
        super(s);
        this.AP = new AutopilotAI(this);

        // TODO: CTO MOD
        // -------------------------------
        this.bStage3 = false;
        this.bStage4 = false;
        this.bStage6 = false;
        this.bStage7 = false;
        this.bAlreadyCheckedStage7 = false;
        this.bNoNavLightsAI = false;
        this.bFastLaunchAI = false;

        if (com.maddox.il2.engine.Config.cur.ini.get("Mods", "NoNavLightsAI", 0) == 1)
            this.bNoNavLightsAI = true;
        if (com.maddox.il2.game.Mission.cur().sectFile().get("Mods", "NoNavLightsAI", 0) == 1)
            this.bNoNavLightsAI = true;
        if (com.maddox.il2.engine.Config.cur.ini.get("Mods", "FastLaunchAI", 0) == 1)
            this.bFastLaunchAI = true;
        if (com.maddox.il2.game.Mission.cur().sectFile().get("Mods", "FastLaunchAI", 0) == 1)
            this.bFastLaunchAI = true;
        // -------------------------------
    }

    public void decDangerAggressiveness() {
        this.dangerAggressiveness -= 0.01F;
        if (this.dangerAggressiveness < 0.0F)
            this.dangerAggressiveness = 0.0F;
        this.oldDanCoeff -= 0.0050F;
        if (this.oldDanCoeff < 0.0F)
            this.oldDanCoeff = 0.0F;
    }

    public void incDangerAggressiveness(int i, float f, float f1, FlightModel flightmodel) {
        f -= 0.7F;
        if (f < 0.0F)
            f = 0.0F;
        f1 = 1000.0F - f1;
        if (f1 < 0.0F)
            f1 = 0.0F;
        double d = (flightmodel.Energy - this.Energy) * 0.1019;
        double d1 = 1.0 + d * 0.00125;
        if (d1 > 1.2)
            d1 = 1.2;
        if (d1 < 0.8)
            d1 = 0.8;
        float f2 = (float) d1 * (7.0E-5F * f * f1);
        if (this.danger == null || f2 > this.oldDanCoeff)
            this.danger = flightmodel;
        this.oldDanCoeff = f2;
        this.dangerAggressiveness += f2 * i;
        if (this.dangerAggressiveness > 1.0F)
            this.dangerAggressiveness = 1.0F;
    }

    public float getDangerAggressiveness() {
        return this.dangerAggressiveness;
    }

    public float getMaxHeightSpeed(float f) {
        if (f < this.HofVmax)
            return this.Vmax + (this.VmaxH - this.Vmax) * (f / this.HofVmax);
        float f1 = (f - this.HofVmax) / this.HofVmax;
        f1 = 1.0F - 1.5F * f1;
        if (f1 < 0.0F)
            f1 = 0.0F;
        return this.VmaxH * f1;
    }

    public float getMinHeightTurn(float f) {
        return this.Wing.T_turn;
    }

    public void setShotAtFriend(float f) {
        this.distToFriend = f;
        this.shotAtFriend = 30;
    }

    public boolean hasCourseWeaponBullets() {
        if (this.actor instanceof KI_46_OTSUHEI) {
            if (this.CT.Weapons[1] != null && this.CT.Weapons[1][0] != null && this.CT.Weapons[1][0].countBullets() != 0)
                return true;
            return false;
        }
        if (this.actor instanceof AR_234)
            return false;
        if ((this.CT.Weapons[0] == null || this.CT.Weapons[0][0] == null || this.CT.Weapons[0][0].countBullets() == 0) && (this.CT.Weapons[1] == null || this.CT.Weapons[1][0] == null || this.CT.Weapons[1][0].countBullets() == 0))
            return false;
        return true;
    }

    public boolean hasBombs() {
        if (this.CT.Weapons[3] != null) {
            for (int i = 0; i < this.CT.Weapons[3].length; i++) {
                if (this.CT.Weapons[3][i] != null && this.CT.Weapons[3][i].countBullets() != 0)
                    return true;
            }
        }
        return false;
    }

    public boolean hasRockets() {
        if (this.CT.Weapons[2] != null) {
            for (int i = 0; i < this.CT.Weapons[2].length; i++) {
                if (this.CT.Weapons[2][i] != null && this.CT.Weapons[2][i].countBullets() != 0)
                    return true;
            }
        }
        return false;
    }

    public boolean canAttack() {
        if (((this.Group.isWingman(this.Group.numInGroup((Aircraft) this.actor)) && !this.aggressiveWingman) || !this.isOk() || !this.hasCourseWeaponBullets()) && !this.hasRockets())
            return false;
        return true;
    }

    public void update(float f) {
        if (Config.isUSE_RENDER())
            this.headTurn(f);
        if (showFM)
            this.OutCT(20);
        super.update(f);
        this.callSuperUpdate = true;
        this.decDangerAggressiveness();
        if (this.Loc.z < -20.0)
            ((Aircraft) this.actor).postEndAction(0.0, this.actor, 4, null);
        this.LandHQ = (float) Engine.land().HQ_Air(this.Loc.x, this.Loc.y);
        Po.set(this.Vwld);
        Po.scale(3.0);
        Po.add(this.Loc);
        this.LandHQ = (float) Math.max(this.LandHQ, Engine.land().HQ_Air(Po.x, Po.y));
        this.Alt = (float) this.Loc.z - this.LandHQ;
        this.indSpeed = this.getSpeed() * (float) Math.sqrt(this.Density / 1.225F);
        if (!this.Gears.onGround() && this.isOk() && this.Alt > 8.0F) {
            if (this.AOA > this.AOA_Crit - 2.0F)
                this.Or.increment(0.0F, 0.05F * (this.AOA_Crit - 2.0F - this.AOA), 0.0F);
            if (this.AOA < -5.0F)
                this.Or.increment(0.0F, 0.05F * (-5.0F - this.AOA), 0.0F);
        }
        if (this.frequentControl || (Time.tickCounter() + this.actor.hashCode()) % 4 == 0) {
            this.turnOffTheWeapon();
            this.maxG = 3.5F + this.Skill * 0.5F;
            this.Or.wrap();
            if (this.mn_time > 10.0F && this.AOA > this.AOA_Crit + 5.0F && this.isStallable() && this.stallable)
                this.safe_set_maneuver(20);
            switch_0_: switch (this.maneuver) {
                default:
                    break;
                case 0:
                    this.target_ground = null;
                    break;
                case 1:
                    this.dryFriction = 8.0F;
                    this.CT.FlapsControl = 0.0F;
                    this.CT.BrakeControl = 1.0F;
                    break;
                case 48:
                    if (this.mn_time >= 1.0F)
                        this.pop();
                    break;
                case 44:
                    if (this.Gears.onGround() && this.Vwld.length() < 0.30000001192092896 && World.Rnd().nextInt(0, 99) < 4) {
                        if (this.Group != null)
                            this.Group.delAircraft((Aircraft) this.actor);
                        if (this.actor instanceof TypeGlider || this.actor instanceof TypeSailPlane) {
                            // TODO: Added by |ZUTI|: handle sea planes landing and despawning!
                            // --------------------------------------------------------------------------
                            // BornPlace zutiAirdromeBornPlace = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(P.x, P.y);

//							String currentAcName = Property.stringValue((actor).getClass(), "keyName");
                            // System.out.println("Sea - Aircraft >" + currentAcName + "< landed!");
                            // ZutiSupportMethods_Net.addAircraftToBornPlace(zutiAirdromeBornPlace, currentAcName, true, false);
                            if (Mission.MDS_VARIABLES().zutiMisc_DespawnAIPlanesAfterLanding)// || (zutiAirdromeBornPlace != null && zutiAirdromeBornPlace.zutiIsStandAloneBornPlace) )
                            {
                                MsgDestroy.Post((Time.current() + ZutiSupportMethods_AI.DESPAWN_AC_DELAY), this.actor);
                            }
                            // --------------------------------------------------------------------------
                            break;
                        }
                        World.cur();
                        if (this.actor != World.getPlayerAircraft()) {
                            if (Airport.distToNearestAirport(this.Loc) > 900.0)
                                ((Aircraft) this.actor).postEndAction(60.0, this.actor, 3, null);
                            else
                                MsgDestroy.Post(Time.current() + 30000L, this.actor);
                        }
                    }
                    if (this.first) {
                        this.AP.setStabAll(false);
                        this.CT.ElevatorControl = World.Rnd().nextFloat(-0.07F, 0.4F);
                        this.CT.AileronControl = World.Rnd().nextFloat(-0.15F, 0.15F);
                    }
                    break;
                case 7: {
                    this.wingman(false);
                    this.setSpeedMode(9);
                    if (this.getW().x <= 0.0) {
                        this.CT.AileronControl = -1.0F;
                        this.CT.RudderControl = 1.0F;
                    } else {
                        this.CT.AileronControl = 1.0F;
                        this.CT.RudderControl = -1.0F;
                    }
                    float f1 = this.Or.getKren();
                    if (f1 > -90.0F && f1 < 90.0F) {
                        float f4 = 0.01111F * (90.0F - Math.abs(f1));
                        this.CT.ElevatorControl = -0.08F * f4 * (this.Or.getTangage() - 3.0F);
                    } else {
                        float f5 = 0.01111F * (Math.abs(f1) - 90.0F);
                        this.CT.ElevatorControl = 0.08F * f5 * (this.Or.getTangage() - 3.0F);
                    }
                    if (this.getSpeed() < 1.7F * this.Vmin)
                        this.pop();
                    if (this.mn_time > 2.2F)
                        this.pop();
                    if (this.danger != null && this.danger instanceof Pilot && ((Maneuver) this.danger).target == this && this.Loc.distance(this.danger.Loc) > 400.0)
                        this.pop();
                    break;
                }
                case 8:
                    if (this.first && !this.isCapableOfACM()) {
                        if (this.Skill > 0)
                            this.pop();
                        if (this.Skill > 1)
                            this.setReadyToReturn(true);
                    }
                    this.setSpeedMode(6);
                    tmpOr.setYPR(this.Or.getYaw(), 0.0F, 0.0F);
                    if (this.Or.getKren() > 0.0F)
                        Ve.set(100.0, -6.0, 10.0);
                    else
                        Ve.set(100.0, 6.0, 10.0);
                    tmpOr.transform(Ve);
                    this.Or.transformInv(Ve);
                    Ve.normalize();
                    this.farTurnToDirection();
                    if (this.Alt > 250.0F && this.mn_time > 8.0F || this.mn_time > 120.0F)
                        this.pop();
                    break;
                case 55:
                    if (this.first && !this.isCapableOfACM()) {
                        if (this.Skill > 0)
                            this.pop();
                        if (this.Skill > 1)
                            this.setReadyToReturn(true);
                    }
                    this.setSpeedMode(6);
                    tmpOr.setYPR(this.Or.getYaw(), 0.0F, 0.0F);
                    if (this.Leader != null && Actor.isAlive(this.Leader.actor) && this.mn_time < 2.5F) {
                        if (this.Leader.Or.getKren() > 0.0F)
                            Ve.set(100.0, -6.0, 10.0);
                        else
                            Ve.set(100.0, 6.0, 10.0);
                    } else if (this.Or.getKren() > 0.0F)
                        Ve.set(100.0, -6.0, 10.0);
                    else
                        Ve.set(100.0, 6.0, 10.0);
                    tmpOr.transform(Ve);
                    this.Or.transformInv(Ve);
                    Ve.normalize();
                    this.farTurnToDirection();
                    if (this.Alt > 250.0F && this.mn_time > 8.0F || this.mn_time > 120.0F)
                        this.pop();
                    break;

                // TODO: GATTACKSPIRAL
                // ------------------------------------------------------
                case 82: // 'I'
                    this.setSpeedMode(6);
                    if (this.first)
                        super.AP.setStabAltitude(true);
                    this.dA = super.Or.getKren();
                    if (this.getOverload() < 1.0D / java.lang.Math.abs(java.lang.Math.cos(this.dA)))
                        super.CT.ElevatorControl += 1.0F * f;
                    else
                        super.CT.ElevatorControl -= 1.0F * f;
                    if (this.dA > 0.0F)
                        this.dA -= 50F;
                    else
                        this.dA -= 310F;
                    if (this.dA < -180F)
                        this.dA += 360F;
                    this.dA = -0.01F * this.dA;
                    super.CT.AileronControl = this.dA;
                    if (this.mn_time > 5F)
                        this.pop();
                    break;
                // -----------------------------------------

                case 45:
                    this.setSpeedMode(7);
                    this.smConstPower = 0.8F;
                    this.dA = this.Or.getKren();
                    if (this.dA > 0.0F)
                        this.dA -= 25.0F;
                    else
                        this.dA -= 335.0F;
                    if (this.dA < -180.0F)
                        this.dA += 360.0F;
                    this.dA *= -0.01F;
                    this.CT.AileronControl = this.dA;
                    this.CT.ElevatorControl = (-0.04F * (this.Or.getTangage() - 1.0F) + 0.0020F * (this.AP.way.curr().z() - (float) this.Loc.z + 250.0F));
                    if (this.mn_time > 60.0F) {
                        this.mn_time = 0.0F;
                        this.pop();
                    }
                    break;
                case 54:
                    if (this.Vwld.length() > this.VminFLAPS * 2.0F)
                        this.Vwld.scale(0.9950000047683716);
                    /* fall through */
                case 9:
                    if (this.first && !this.isCapableOfACM()) {
                        if (this.Skill > 0)
                            this.pop();
                        if (this.Skill > 1)
                            this.setReadyToReturn(true);
                    }
                    this.setSpeedMode(4);
                    this.smConstSpeed = 100.0F;
                    this.dA = this.Or.getKren();
                    if (this.dA > 0.0F)
                        this.dA -= 50.0F;
                    else
                        this.dA -= 310.0F;
                    if (this.dA < -180.0F)
                        this.dA += 360.0F;
                    this.dA *= -0.01F;
                    this.CT.AileronControl = this.dA;
                    this.dA = (-10.0F - this.Or.getTangage()) * 0.05F;
                    this.CT.ElevatorControl = this.dA;
                    if (this.getOverload() < 1.0 / Math.abs(Math.cos(this.dA)))
                        this.CT.ElevatorControl += 1.0F * f;
                    else
                        this.CT.ElevatorControl -= 1.0F * f;
                    if (this.Alt < 100.0F) {
                        this.push(3);
                        this.pop();
                    }
                    if (this.mn_time > 5.0F)
                        this.pop();
                    break;
                case 14:
                    this.setSpeedMode(6);
                    if (this.first)
                        this.AP.setStabAltitude(true);
                    this.dA = this.Or.getKren();
                    if (this.getOverload() < 1.0 / Math.abs(Math.cos(this.dA)))
                        this.CT.ElevatorControl += 1.0F * f;
                    else
                        this.CT.ElevatorControl -= 1.0F * f;
                    if (this.dA > 0.0F)
                        this.dA -= 50.0F;
                    else
                        this.dA -= 310.0F;
                    if (this.dA < -180.0F)
                        this.dA += 360.0F;
                    this.dA *= -0.01F;
                    this.CT.AileronControl = this.dA;
                    if (this.mn_time > 5.0F)
                        this.pop();
                    break;
                case 4:
                    this.CT.AileronControl = this.getW().x > 0.0 ? 1.0F : -1.0F;
                    this.CT.ElevatorControl = 0.1F * (float) (Math.cos(FMMath.DEG2RAD(this.Or.getKren())));
                    this.CT.RudderControl = 0.0F;
                    if (this.getSpeedKMH() < 220.0F) {
                        this.push(3);
                        this.pop();
                    }
                    if (this.mn_time > 7.0F)
                        this.pop();
                    break;
                case 2:
                    this.minElevCoeff = 20.0F;
                    if (this.first) {
                        this.wingman(false);
                        this.AP.setStabAll(false);
                        this.CT.RudderControl = 0.0F;
                        if (World.Rnd().nextInt(0, 99) < 10 && this.Alt < 80.0F)
                            Voice.speakPullUp((Aircraft) this.actor);
                    }
                    this.setSpeedMode(9);
                    this.CT.BayDoorControl = 0.0F;
                    this.CT.AirBrakeControl = 0.0F;
                    this.CT.AileronControl = -0.04F * (this.dA = this.Or.getKren());
                    this.CT.ElevatorControl = 1.0F + 0.3F * (float) this.getW().y;
                    if (this.CT.ElevatorControl < 0.0F)
                        this.CT.ElevatorControl = 0.0F;
                    if (this.AOA > 15.0F)
                        this.Or.increment(0.0F, (15.0F - this.AOA) * 0.5F * f, 0.0F);
                    if (this.Alt < 10.0F && this.Vwld.z < 0.0)
                        this.Vwld.z *= 0.8999999761581421;
                    if (this.Vwld.z > 1.0) {
                        if (this.actor instanceof TypeGlider)
                            this.push(49);
                        else
                            this.push(57);
                        this.pop();
                    }
                    if (this.mn_time > 25.0F) {
                        this.push(49);
                        this.pop();
                    }
                    break;
                case 60:
                    this.setSpeedMode(9);
                    this.CT.RudderControl = 0.0F;
                    this.CT.ElevatorControl = 1.0F;
                    if (this.mn_time > 0.15F)
                        this.pop();
                    break;
                case 61:
                    this.CT.RudderControl = 0.0F;
                    this.CT.ElevatorControl = -0.4F;
                    if (this.mn_time > 0.2F)
                        this.pop();
                    break;
                case 3:
                    if (this.first && this.program[0] == 49)
                        this.pop();
                    this.setSpeedMode(6);
                    this.CT.AileronControl = -0.04F * this.Or.getKren();
                    this.dA = (this.getSpeedKMH() - 180.0F - this.Or.getTangage() * 10.0F - this.getVertSpeed() * 5.0F) * 0.0040F;
                    this.CT.ElevatorControl = this.dA;
                    if (this.getSpeed() > this.Vmin * 1.2F && this.getVertSpeed() > 0.0F) {
                        this.setSpeedMode(7);
                        this.smConstPower = 0.7F;
                        this.pop();
                    }
                    break;
                case 10:
                    this.AP.setStabAll(false);
                    this.setSpeedMode(6);
                    this.CT.AileronControl = -0.04F * this.Or.getKren();
                    this.dA = this.CT.ElevatorControl;
                    if (this.Or.getTangage() > 15.0F)
                        this.dA -= (this.Or.getTangage() - 15.0F) * 0.1F * f;
                    else
                        this.dA = ((float) this.Vwld.length() / this.VminFLAPS * 140.0F - 50.0F - this.Or.getTangage() * 20.0F) * 0.0040F;
                    this.dA += 0.5 * this.getW().y;
                    this.CT.ElevatorControl = this.dA;
                    if (this.Alt > 250.0F && this.mn_time > 6.0F || this.mn_time > 20.0F)
                        this.pop();
                    break;
                case 57:
                    this.AP.setStabAll(false);
                    this.setSpeedMode(9);
                    this.CT.AileronControl = -0.04F * this.Or.getKren();
                    this.dA = this.CT.ElevatorControl;
                    if (this.Or.getTangage() > 25.0F)
                        this.dA -= (this.Or.getTangage() - 25.0F) * 0.1F * f;
                    else
                        this.dA = ((float) this.Vwld.length() / this.VminFLAPS * 140.0F - 50.0F - this.Or.getTangage() * 20.0F) * 0.0040F;
                    this.dA += 0.5 * this.getW().y;
                    this.CT.ElevatorControl = this.dA;
                    if (this.Alt > 150.0F || this.Alt > 100.0F && this.mn_time > 2.0F || this.mn_time > 3.0F)
                        this.pop();
                    break;
                case 11:
                    this.setSpeedMode(8);
                    if (Math.abs(this.Or.getKren()) < 90.0F) {
                        this.CT.AileronControl = -0.04F * this.Or.getKren();
                        if (this.Vwld.z > 0.0 || this.getSpeedKMH() < 270.0F)
                            this.dA = -0.04F;
                        else
                            this.dA = 0.04F;
                        this.CT.ElevatorControl = this.CT.ElevatorControl * 0.9F + this.dA * 0.1F;
                    } else {
                        this.CT.AileronControl = 0.04F * (180.0F - Math.abs(this.Or.getKren()));
                        if (this.Or.getTangage() > -25.0F)
                            this.dA = 0.33F;
                        else if (this.Vwld.z > 0.0 || this.getSpeedKMH() < 270.0F)
                            this.dA = 0.04F;
                        else
                            this.dA = -0.04F;
                        this.CT.ElevatorControl = this.CT.ElevatorControl * 0.9F + this.dA * 0.1F;
                    }
                    if (this.Alt < 120.0F || this.mn_time > 4.0F)
                        this.pop();
                    break;
                case 12:
                    this.setSpeedMode(4);
                    this.smConstSpeed = 80.0F;
                    this.CT.AileronControl = -0.04F * this.Or.getKren();
                    if (this.Vwld.length() > this.VminFLAPS * 2.0F)
                        this.Vwld.scale(0.9950000047683716);
                    this.dA = -((float) this.Vwld.z / (Math.abs(this.getSpeed()) + 1.0F) + 0.5F);
                    if (this.dA < -0.1F)
                        this.dA = -0.1F;
                    this.CT.ElevatorControl = (this.CT.ElevatorControl * 0.9F + this.dA * 0.1F + 0.3F * (float) this.getW().y);
                    if (this.mn_time > 5.0F || this.Alt < 200.0F)
                        this.pop();
                    break;
                case 13:
                    if (this.first) {
                        this.AP.setStabAll(false);
                        this.submaneuver = this.actor instanceof TypeFighter ? SUB_MAN0 : SUB_MAN2;
                    }
                    switch (this.submaneuver) {
                        case SUB_MAN0:
                            this.dA = this.Or.getKren() - 180.0F;
                            if (this.dA < -180.0F)
                                this.dA += 360.0F;
                            this.dA *= -0.04F;
                            this.CT.AileronControl = this.dA;
                            if (this.mn_time > 3.0F || (Math.abs(this.Or.getKren()) > 175.0F - 5.0F * this.Skill))
                                this.submaneuver++;
                            break;
                        case SUB_MAN1:
                            this.dA = this.Or.getKren() - 180.0F;
                            if (this.dA < -180.0F)
                                this.dA += 360.0F;
                            this.dA *= -0.04F;
                            this.CT.AileronControl = this.dA;
                            this.CT.RudderControl = -0.1F * this.getAOS();
                            this.setSpeedMode(8);
                            if (this.Or.getTangage() > -45.0F && this.getOverload() < this.maxG)
                                this.CT.ElevatorControl += 1.5F * f;
                            else
                                this.CT.ElevatorControl -= 0.5F * f;
                            if (this.Or.getTangage() < -44.0F)
                                this.submaneuver++;
                            if (this.Alt < -5.0 * this.Vwld.z || this.mn_time > 5.0F)
                                this.pop();
                            break;
                        case SUB_MAN2:
                            this.setSpeedMode(8);
                            this.CT.AileronControl = -0.04F * this.Or.getKren();
                            this.dA = -((float) this.Vwld.z / (Math.abs(this.getSpeed()) + 1.0F) + 0.707F);
                            if (this.dA < -0.75F)
                                this.dA = -0.75F;
                            this.CT.ElevatorControl = (this.CT.ElevatorControl * 0.9F + this.dA * 0.1F + 0.5F * (float) this.getW().y);
                            if (this.Alt < -5.0 * this.Vwld.z || this.mn_time > 5.0F)
                                this.pop();
                            break;
                    }
                    if (this.Alt < 200.0F)
                        this.pop();
                    break;
                case 5:
                    this.dA = this.Or.getKren();
                    if (this.dA < 0.0F)
                        this.dA -= 270.0F;
                    else
                        this.dA -= 90.0F;
                    if (this.dA < -180.0F)
                        this.dA += 360.0F;
                    this.dA *= -0.02F;
                    this.CT.AileronControl = this.dA;
                    if (this.mn_time > 5.0F || Math.abs(Math.abs(this.Or.getKren()) - 90.0F) < 1.0F)
                        this.pop();
                    break;
                case 6:
                    this.dA = this.Or.getKren() - 180.0F;
                    if (this.dA < -180.0F)
                        this.dA += 360.0F;
                    this.CT.AileronControl = (float) (-0.04F * this.dA - 0.5 * this.getW().x);
                    if (this.mn_time > 4.0F || Math.abs(this.Or.getKren()) > 178.0F) {
                        this.W.x = 0.0;
                        this.pop();
                    }
                    break;
                case 35: {
                    if (this.first) {
                        this.AP.setStabAll(false);
                        this.direction = this.Or.getKren();
                        this.submaneuver = this.Or.getKren() <= 0.0F ? -1 : 1;
                        this.tmpi = 0;
                        this.setSpeedMode(9);
                    }
                    this.CT.AileronControl = 1.0F * this.submaneuver;
                    this.CT.RudderControl = 0.0F * this.submaneuver;
                    float f2 = this.Or.getKren();
                    if (f2 > -90.0F && f2 < 90.0F) {
                        float f6 = 0.01111F * (90.0F - Math.abs(f2));
                        this.CT.ElevatorControl = -0.08F * f6 * (this.Or.getTangage() - 3.0F);
                    } else {
                        float f7 = 0.01111F * (90.0F - Math.abs(f2));
                        this.CT.ElevatorControl = 0.08F * f7 * (this.Or.getTangage() - 3.0F);
                    }
                    if (this.Or.getKren() * this.direction < 0.0F)
                        this.tmpi = 1;
                    do {
                        if (this.tmpi == 1) {
                            if (this.submaneuver <= 0) {
                                if (this.Or.getKren() < this.direction)
                                    break;
                            } else if (this.Or.getKren() > this.direction)
                                break;
                        }
                        if (!(this.mn_time > 17.5F))
                            break switch_0_;
                    } while (false);
                    this.pop();
                    break;
                }
                case 22:
                    this.setSpeedMode(9);
                    this.CT.AileronControl = -0.04F * this.Or.getKren();
                    this.CT.ElevatorControl = -0.04F * (this.Or.getTangage() + 5.0F);
                    this.CT.RudderControl = 0.0F;
                    if (this.getSpeed() > this.Vmax || this.mn_time > 30.0F)
                        this.pop();
                    break;
                case 67:
                    this.minElevCoeff = 18.0F;
                    if (this.first) {
                        this.sub_Man_Count = 0;
                        this.setSpeedMode(9);
                        this.CT.setPowerControl(1.1F);
                    }
                    if (this.danger != null) {
                        float f8 = 700.0F - (float) this.danger.Loc.distance(this.Loc);
                        if (f8 < 0.0F)
                            f8 = 0.0F;
                        f8 *= 0.00143F;
                        float f3 = this.Or.getKren();
                        if (this.sub_Man_Count == 0 || this.first) {
                            if (this.raAilShift < 0.0F)
                                this.raAilShift = f8 * World.Rnd().nextFloat(0.6F, 1.0F);
                            else
                                this.raAilShift = f8 * World.Rnd().nextFloat(-1.0F, -0.6F);
                            this.raRudShift = f8 * World.Rnd().nextFloat(-0.5F, 0.5F);
                            this.raElevShift = f8 * World.Rnd().nextFloat(-0.8F, 0.8F);
                        }
                        this.CT.AileronControl = 0.9F * this.CT.AileronControl + 0.1F * this.raAilShift;
                        this.CT.RudderControl = 0.95F * this.CT.RudderControl + 0.05F * this.raRudShift;
                        if (f3 > -90.0F && f3 < 90.0F)
                            this.CT.ElevatorControl = -0.04F * (this.Or.getTangage() + 5.0F);
                        else
                            this.CT.ElevatorControl = 0.05F * (this.Or.getTangage() + 5.0F);
                        this.CT.ElevatorControl += 0.1F * this.raElevShift;
                        this.sub_Man_Count++;
                        if (this.sub_Man_Count >= 80.0F * (1.5F - f8) && f3 > -70.0F && f3 < 70.0F)
                            this.sub_Man_Count = 0;
                        if (this.mn_time > 30.0F)
                            this.pop();
                    } else
                        this.pop();
                    break;
                case 16:
                    if (this.first) {
                        if (!this.isCapableOfACM())
                            this.pop();
                        this.AP.setStabAll(false);
                        this.setSpeedMode(6);
                        if (this.getSpeed() < 0.75F * this.Vmax) {
                            this.push();
                            this.push(22);
                            this.pop();
                            break;
                        }
                        this.submaneuver = SUB_MAN0;
                    }
                    this.maxAOA = this.Vwld.z <= 0.0 ? 12.0F : 7.0F;
                    switch (this.submaneuver) {
                        case SUB_MAN0:
                            this.CT.ElevatorControl = 0.05F;
                            this.CT.AileronControl = -0.04F * this.Or.getKren();
                            this.CT.RudderControl = -0.1F * this.getAOS();
                            if (Math.abs(this.Or.getKren()) < 2.0F)
                                this.submaneuver++;
                            break;
                        case SUB_MAN1:
                            this.CT.AileronControl = -0.04F * this.Or.getKren();
                            this.CT.RudderControl = -0.1F * this.getAOS();
                            this.dA = 0.5F;
                            if (this.getOverload() > this.maxG || this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA)
                                this.CT.ElevatorControl -= 0.4F * f;
                            else
                                this.CT.ElevatorControl += 0.4F * f;
                            if (this.Or.getTangage() > 80.0F)
                                this.submaneuver++;
                            if (this.getSpeed() < this.Vmin * 1.5F)
                                this.pop();
                            break;
                        case SUB_MAN2:
                            this.CT.RudderControl = -0.1F * this.getAOS() * (this.getSpeed() <= 300.0F ? 0.0F : 1.0F);
                            this.dA = 1.0F;
                            if (this.getOverload() > this.maxG || this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA)
                                this.CT.ElevatorControl -= 0.4F * f;
                            else
                                this.CT.ElevatorControl += 0.4F * f;
                            if (this.Or.getTangage() < 0.0F)
                                this.submaneuver++;
                            break;
                        case SUB_MAN3:
                            this.CT.RudderControl = -0.1F * this.getAOS() * (this.getSpeed() <= 300.0F ? 0.0F : 1.0F);
                            this.dA = 1.0F;
                            if (this.getOverload() > this.maxG || this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA)
                                this.CT.ElevatorControl -= 0.2F * f;
                            else
                                this.CT.ElevatorControl += 0.2F * f;
                            if (this.Or.getTangage() < -60.0F)
                                this.submaneuver++;
                            break;
                        case SUB_MAN4:
                            if (this.Or.getTangage() > -45.0F) {
                                this.CT.AileronControl = -0.04F * this.Or.getKren();
                                this.maxAOA = 3.5F;
                            }
                            this.CT.RudderControl = -0.1F * this.getAOS();
                            this.dA = 0.5F;
                            if (this.getOverload() > this.maxG || this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA)
                                this.CT.ElevatorControl -= 1.0F * f;
                            else
                                this.CT.ElevatorControl += 0.4F * f;
                            if (!(this.Or.getTangage() > 3.0F) && !(this.Vwld.z > 0.0))
                                break;
                            this.pop();
                    }
                    break;
                case 17:
                    if (this.first) {
                        if (this.Alt < 1000.0F)
                            this.pop();
                        else if (this.getSpeed() < this.Vmin * 1.2F) {
                            this.push();
                            this.push(22);
                            this.pop();
                        } else {
                            this.push(18);
                            this.push(19);
                            this.pop();
                        }
                    } else
                        this.pop();
                    break;
                case 19:
                    if (this.first) {
                        if (this.Alt < 1000.0F) {
                            this.pop();
                            break;
                        }
                        this.AP.setStabAll(false);
                        this.submaneuver = SUB_MAN0;
                    }
                    this.maxAOA = this.Vwld.z <= 0.0 ? 12.0F : 7.0F;
                    switch (this.submaneuver) {
                        case SUB_MAN0:
                            this.CT.ElevatorControl = 0.05F;
                            this.CT.AileronControl = 0.04F * (this.Or.getKren() <= 0.0F ? -180.0F + this.Or.getKren() : 180.0F - this.Or.getKren());
                            this.CT.RudderControl = -0.1F * this.getAOS();
                            if (Math.abs(this.Or.getKren()) > 178.0F)
                                this.submaneuver++;
                            break;
                        case SUB_MAN1:
                            this.setSpeedMode(7);
                            this.smConstPower = 0.5F;
                            this.CT.AileronControl = 0.0F;
                            this.CT.RudderControl = -0.1F * this.getAOS();
                            this.dA = 1.0F;
                            if (this.getOverload() > this.maxG || this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA)
                                this.CT.ElevatorControl -= 0.2F * f;
                            else
                                this.CT.ElevatorControl += 1.2F * f;
                            if (this.Or.getTangage() < -60.0F)
                                this.submaneuver++;
                            break;
                        case SUB_MAN2:
                            if (this.Or.getTangage() > -45.0F) {
                                this.CT.AileronControl = -0.04F * this.Or.getKren();
                                this.setSpeedMode(9);
                                this.maxAOA = 7.0F;
                            }
                            this.CT.RudderControl = -0.1F * this.getAOS();
                            this.dA = 0.5F;
                            if (this.getOverload() > this.maxG || this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA)
                                this.CT.ElevatorControl -= 0.8F * f;
                            else
                                this.CT.ElevatorControl += 0.4F * f;
                            if (!(this.Or.getTangage() > this.AOA - 1.0F) && !(this.Vwld.z > 0.0))
                                break;
                            this.pop();
                    }
                    break;
                case 18:
                    if (this.first) {
                        if (!this.isCapableOfACM())
                            this.pop();
                        if (this.getSpeed() < this.Vmax * 0.75F) {
                            this.push();
                            this.push(22);
                            this.pop();
                            break;
                        }
                        this.AP.setStabAll(false);
                        this.submaneuver = SUB_MAN0;
                        this.setSpeedMode(6);
                    }
                    this.maxAOA = this.Vwld.z <= 0.0 ? 12.0F : 7.0F;
                    switch (this.submaneuver) {
                        case SUB_MAN0:
                            this.CT.AileronControl = -0.04F * this.Or.getKren();
                            this.CT.RudderControl = -0.1F * this.getAOS();
                            if (Math.abs(this.Or.getKren()) < 2.0F)
                                this.submaneuver++;
                            break;
                        case SUB_MAN1:
                            this.CT.AileronControl = -0.04F * this.Or.getKren();
                            this.CT.RudderControl = -0.1F * this.getAOS();
                            this.dA = 1.0F;
                            if (this.getOverload() > this.maxG || this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA)
                                this.CT.ElevatorControl -= 0.4F * f;
                            else
                                this.CT.ElevatorControl += 0.8F * f;
                            if (this.Or.getTangage() > 80.0F)
                                this.submaneuver++;
                            if (this.getSpeed() < this.Vmin * 1.5F)
                                this.pop();
                            break;
                        case SUB_MAN2:
                            this.CT.RudderControl = -0.1F * this.getAOS() * (this.getSpeed() <= 300.0F ? 0.0F : 1.0F);
                            this.dA = 1.0F;
                            if (this.getOverload() > this.maxG || this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA)
                                this.CT.ElevatorControl -= 0.4F * f;
                            else
                                this.CT.ElevatorControl += 0.4F * f;
                            if (this.Or.getTangage() < 12.0F)
                                this.submaneuver++;
                            break;
                        case SUB_MAN3:
                            if (Math.abs(this.Or.getKren()) < 60.0F)
                                this.CT.ElevatorControl = 0.05F;
                            this.CT.AileronControl = -0.04F * this.Or.getKren();
                            this.CT.RudderControl = -0.1F * this.getAOS();
                            if (Math.abs(this.Or.getKren()) < 30.0F)
                                this.submaneuver++;
                            break;
                        case SUB_MAN4:
                            this.pop();
                    }
                    break;
                case 15:
                    if (this.first && this.getSpeed() < 0.35F * (this.Vmin + this.Vmax))
                        this.pop();
                    else {
                        this.push(8);
                        this.pop();
                        this.CT.RudderControl = -0.1F * this.getAOS();
                        if (this.Or.getKren() < 0.0F)
                            this.CT.AileronControl = -0.04F * (this.Or.getKren() + 30.0F);
                        else
                            this.CT.AileronControl = -0.04F * (this.Or.getKren() - 30.0F);
                        if (this.mn_time > 7.5F)
                            this.pop();
                    }
                    break;
                case 20:
                    if (this.first) {
                        this.wingman(false);
                        this.setSpeedMode(6);
                    }
                    if (!this.isCapableOfBMP()) {
                        this.setReadyToDie(true);
                        this.pop();
                    }
                    if (this.getW().z > 0.0)
                        this.CT.RudderControl = 1.0F;
                    else
                        this.CT.RudderControl = -1.0F;
                    if (this.AOA > this.AOA_Crit - 4.0F)
                        this.Or.increment(0.0F, 0.01F * (this.AOA_Crit - 4.0F - this.AOA), 0.0F);
                    if (this.AOA < -5.0F)
                        this.Or.increment(0.0F, 0.01F * (-5.0F - this.AOA), 0.0F);
                    if (this.AOA < this.AOA_Crit - 1.0F)
                        this.pop();
                    if (this.mn_time > 14.0F - this.Skill * 4.0F || this.Alt < 50.0F)
                        this.pop();
                    break;
                case 21:
                    this.AP.setWayPoint(true);
                    if (this.getAltitude() < this.AP.way.curr().z() - 100.0F && this.actor instanceof TypeSupersonic)
                        this.CT.ElevatorControl += 0.025F;
                    if (this.mn_time > 300.0F)
                        this.pop();
                    if (this.isTick(256, 0) && !this.actor.isTaskComplete() && (this.AP.way.isLast() && this.AP.getWayPointDistance() < 1500.0F || this.AP.way.isLanding()))
                        World.onTaskComplete(this.actor);
                    if (((Aircraft) this.actor).aircIndex() == 0 && !this.isReadyToReturn()) {
                        World.cur();
                        if (World.getPlayerAircraft() != null) {
                            World.cur();
                            if (((Aircraft) this.actor).getRegiment() == World.getPlayerAircraft().getRegiment()) {
                                float f9 = 1.0E12F;
                                if (this.AP.way.curr().Action == 3)
                                    f9 = this.AP.getWayPointDistance();
                                else {
                                    int i = this.AP.way.Cur();
                                    this.AP.way.next();
                                    if (this.AP.way.curr().Action == 3)
                                        f9 = this.AP.getWayPointDistance();
                                    this.AP.way.setCur(i);
                                }
                                if (this.Speak5minutes == 0 && 22000.0F < f9 && f9 < 30000.0F) {
                                    Voice.speak5minutes((Aircraft) this.actor);
                                    this.Speak5minutes = 1;
                                }
                                if (this.Speak1minute == 0 && f9 < 10000.0F) {
                                    Voice.speak1minute((Aircraft) this.actor);
                                    this.Speak1minute = 1;
                                    this.Speak5minutes = 1;
                                }
                                if ((this.WeWereInGAttack || this.WeWereInAttack) && this.mn_time > 5.0F) {
                                    if (!this.SpeakMissionAccomplished) {
                                        boolean flag = false;
                                        int j = this.AP.way.Cur();
                                        if (this.AP.way.curr().Action == 3 || this.AP.way.curr().getTarget() != null)
                                            break;
                                        while (this.AP.way.Cur() < this.AP.way.size() - 1) {
                                            this.AP.way.next();
                                            if (this.AP.way.curr().Action == 3 || (this.AP.way.curr().getTarget() != null))
                                                flag = true;
                                        }
                                        this.AP.way.setCur(j);
                                        if (!flag) {
                                            Voice.speakMissionAccomplished((Aircraft) this.actor);
                                            this.SpeakMissionAccomplished = true;
                                        }
                                    }
                                    if (!this.SpeakMissionAccomplished) {
                                        this.Speak5minutes = 0;
                                        this.Speak1minute = 0;
                                        this.SpeakBeginGattack = 0;
                                    }
                                    this.WeWereInGAttack = false;
                                    this.WeWereInAttack = false;
                                }
                            }
                        }
                    }
                    if ((this.actor instanceof TypeBomber || this.actor instanceof TypeTransport) && this.AP.way.curr() != null && this.AP.way.curr().Action == 3 && (this.AP.way.curr().getTarget() == null || this.actor instanceof Scheme4)) {
                        double d = this.Loc.z - World.land().HQ(this.Loc.x, this.Loc.y);
                        if (d < 0.0)
                            d = 0.0;
                        if ((this.AP.getWayPointDistance() < (this.getSpeed() * Math.sqrt(d * 0.2038699984550476))) && !this.bombsOut) {
                            if (this.CT.Weapons[3] != null && this.CT.Weapons[3][0] != null && this.CT.Weapons[3][0].countBullets() != 0 && !(this.CT.Weapons[3][0] instanceof BombGunPara))
                                Voice.airSpeaks((Aircraft) this.actor, 85, 1);
                            this.bombsOut = true;
                            this.AP.way.curr().Action = 0;
                            if (this.Group != null)
                                this.Group.dropBombs();
                        }
                    }
                    this.setSpeedMode(3);
                    if (this.AP.way.isLandingOnShip() && this.AP.way.isLanding()) {
                        this.AP.way.landingAirport.rebuildLandWay(this);
                        if (this.CT.bHasCockpitDoorControl)
                            this.AS.setCockpitDoor(this.actor, 1);
                    }
                    break;
                case 23:
                    if (this.first) {
                        this.wingman(false);
                        if (this.getSpeedKMH() < this.Vmin * 1.25F) {
                            this.push();
                            this.push(22);
                            this.pop();
                            break;
                        }
                    }
                    this.setSpeedMode(6);
                    this.CT.AileronControl = -0.04F * this.Or.getKren();
                    this.CT.RudderControl = -0.1F * this.getAOS();
                    if (this.Or.getTangage() < 70.0F && this.getOverload() < this.maxG && this.AOA < 14.0F)
                        this.CT.ElevatorControl += 0.5F * f;
                    else
                        this.CT.ElevatorControl -= 0.5F * f;
                    if (this.Vwld.z < 1.0)
                        this.pop();
                    break;
                case 24:
                    if (this.Leader == null || !Actor.isAlive(this.Leader.actor) || !((Maneuver) this.Leader).isOk() || (((Maneuver) this.Leader).isBusy() && (!(this.Leader instanceof RealFlightModel) || !((RealFlightModel) this.Leader).isRealMode())))
                        this.set_maneuver(0);
                    else {
                        if (this.actor instanceof TypeGlider) {
                            if (this.Leader.AP.way.curr().Action != 0 && this.Leader.AP.way.curr().Action != 1)
                                this.set_maneuver(49);
                        } else if (this.Leader.getSpeed() < 30.0F || ((this.Leader.Loc.z - Engine.land().HQ_Air(this.Leader.Loc.x, this.Leader.Loc.y)) < 50.0)) {
                            this.airClient = this.Leader;
                            this.push();
                            this.push(59);
                            this.pop();
                            break;
                        }
                        if (!this.Leader.AP.way.isLanding()) {
                            this.AP.way.setCur(this.Leader.AP.way.Cur());
                            if (this.Leader.Wingman != this) {
                                if (!this.bombsOut && ((Maneuver) this.Leader).bombsOut) {
                                    this.bombsOut = true;
                                    Maneuver maneuver1 = this;
                                    while (maneuver1.Wingman != null) {
                                        maneuver1 = (Maneuver) maneuver1.Wingman;
                                        maneuver1.bombsOut = true;
                                    }
                                }
                                if (this.CT.BayDoorControl != this.Leader.CT.BayDoorControl) {
                                    this.CT.BayDoorControl = this.Leader.CT.BayDoorControl;
                                    Pilot pilot = (Pilot) this;
                                    while (pilot.Wingman != null) {
                                        pilot = (Pilot) pilot.Wingman;
                                        pilot.CT.BayDoorControl = this.CT.BayDoorControl;
                                    }
                                }
                            }
                        } else {
                            if (this.Leader.Wingman != this) {
                                this.push(8);
                                this.push(8);
                                this.push(World.Rnd().nextBoolean() ? 8 : 48);
                                this.push(World.Rnd().nextBoolean() ? 8 : 48);
                                this.pop();
                            }
                            this.Leader = null;
                            this.pop();
                            break;
                        }
                        this.airClient = this.Leader;
                        tmpOr.setAT0(this.airClient.Vwld);
                        tmpOr.increment(0.0F, this.airClient.getAOA(), 0.0F);
                        Ve.set(this.followOffset);
                        Ve.x -= 300.0;
                        tmpV3f.sub(this.followTargShift, this.followCurShift);
                        if (tmpV3f.lengthSquared() < 0.5)
                            this.followTargShift.set(World.cur().rnd.nextFloat(-0.0F, 10.0F), World.cur().rnd.nextFloat(-5.0F, 5.0F), World.cur().rnd.nextFloat(-3.5F, 3.5F));
                        tmpV3f.normalize();
                        tmpV3f.scale(2.0F * f);
                        this.followCurShift.add(tmpV3f);
                        Ve.add(this.followCurShift);
                        tmpOr.transform(Ve, Po);
                        Po.scale(-1.0);
                        Po.add(this.airClient.Loc);
                        Ve.sub(Po, this.Loc);
                        this.Or.transformInv(Ve);
                        this.dist = (float) Ve.length();
                        if (this.followOffset.x > 600.0) {
                            Ve.set(this.followOffset);
                            Ve.x -= 0.5 * this.followOffset.x;
                            tmpOr.transform(Ve, Po);
                            Po.scale(-1.0);
                            Po.add(this.airClient.Loc);
                            Ve.sub(Po, this.Loc);
                            this.Or.transformInv(Ve);
                        }
                        Ve.normalize();
                        if (this.dist > 600.0 + Ve.x * 400.0) {
                            this.push();
                            this.push(53);
                            this.pop();
                        } else {
                            if (this.actor instanceof TypeTransport && this.Vmax < 70.0)
                                this.farTurnToDirection(6.2F);
                            else
                                this.attackTurnToDirection(this.dist, f, 10.0F);
                            this.setSpeedMode(10);
                            this.tailForStaying = this.Leader;
                            this.tailOffset.set(this.followOffset);
                            this.tailOffset.scale(-1.0);
                        }
                    }
                    break;
                case 65:
                    if (!(this instanceof RealFlightModel) || !((RealFlightModel) this).isRealMode()) {
                        this.bombsOut = true;
                        this.CT.dropFuelTanks();
                    }
                    if (this.airClient == null || !Actor.isAlive(this.airClient.actor) || !this.isOk())
                        this.set_maneuver(0);
                    else {
                        Maneuver maneuver2 = (Maneuver) this.airClient;
                        Maneuver maneuver4 = (Maneuver) ((Maneuver) this.airClient).danger;
                        if ((maneuver2.getDangerAggressiveness() >= 1.0F - this.Skill * 0.2F) && maneuver4 != null && this.hasCourseWeaponBullets()) {
                            Voice.speakCheckYour6((Aircraft) maneuver2.actor, (Aircraft) maneuver4.actor);
                            Voice.speakHelpFromAir((Aircraft) this.actor, 1);
                            this.set_task(6);
                            this.target = maneuver4;
                            this.set_maneuver(27);
                        }
                        Ve.sub(this.airClient.Loc, this.Loc);
                        this.Or.transformInv(Ve);
                        this.dist = (float) Ve.length();
                        Ve.normalize();
                        this.attackTurnToDirection(this.dist, f, 10.0F + this.Skill * 8.0F);
                        if (this.Alt > 50.0F)
                            this.setSpeedMode(1);
                        else
                            this.setSpeedMode(6);
                        this.tailForStaying = this.airClient;
                        this.tailOffset.set(this.followOffset);
                        this.tailOffset.scale(-1.0);
                        if (this.dist > 600.0 + Ve.x * 400.0 && this.get_maneuver() != 27) {
                            this.push();
                            this.push(53);
                            this.pop();
                        }
                    }
                    break;
                case 53:
                    if (this.airClient == null || !Actor.isAlive(this.airClient.actor) || !this.isOk()) {
                        this.airClient = null;
                        this.set_maneuver(0);
                    } else {
                        this.maxAOA = 5.0F;
                        Ve.set(this.followOffset);
                        Ve.x -= 300.0;
                        tmpOr.setAT0(this.airClient.Vwld);
                        tmpOr.increment(0.0F, 4.0F, 0.0F);
                        tmpOr.transform(Ve, Po);
                        Po.scale(-1.0);
                        Po.add(this.airClient.Loc);
                        Ve.sub(Po, this.Loc);
                        this.Or.transformInv(Ve);
                        this.dist = (float) Ve.length();
                        Ve.normalize();
                        if (this.Vmax < 83.0F)
                            this.farTurnToDirection(8.5F);
                        else
                            this.farTurnToDirection(7.0F);
                        float f10 = (this.Energy - this.airClient.Energy) * 0.1019F;
                        if (f10 < -50.0 + this.followOffset.z)
                            this.setSpeedMode(9);
                        else
                            this.setSpeedMode(10);
                        this.tailForStaying = this.airClient;
                        this.tailOffset.set(this.followOffset);
                        this.tailOffset.scale(-1.0);
                        if (this.dist < 500.0 + Ve.x * 200.0)
                            this.pop();
                        else {
                            if (this.AOA > 12.0F && this.Alt > 15.0F)
                                this.Or.increment(0.0F, 12.0F - this.AOA, 0.0F);
                            if (this.mn_time > 30.0F && (Ve.x < 0.0 || this.dist > 10000.0F))
                                this.pop();
                        }
                    }
                    break;
                case 68:
                    if (this.airClient == null || !Actor.isAlive(this.airClient.actor) || !this.isOk())
                        this.set_maneuver(0);
                    else {
                        Maneuver maneuver3 = (Maneuver) this.airClient;
                        Maneuver maneuver5 = (Maneuver) ((Maneuver) this.airClient).danger;
                        if ((maneuver3.getDangerAggressiveness() >= 1.0F - this.Skill * 0.3F) && maneuver5 != null && this.hasCourseWeaponBullets()) {
                            tmpV3d.sub(maneuver3.Vwld, maneuver5.Vwld);
                            if (tmpV3d.length() < 170.0) {
                                this.set_task(6);
                                this.target = maneuver5;
                                this.set_maneuver(27);
                            }
                        }
                        this.maxAOA = 5.0F;
                        Ve.set(this.followOffset);
                        Ve.x -= 300.0;
                        tmpOr.setAT0(this.airClient.Vwld);
                        tmpOr.increment(0.0F, 4.0F, 0.0F);
                        tmpOr.transform(Ve, Po);
                        Po.scale(-1.0);
                        Po.add(this.airClient.Loc);
                        Ve.sub(Po, this.Loc);
                        this.Or.transformInv(Ve);
                        this.dist = (float) Ve.length();
                        Ve.normalize();
                        if (this.Vmax < 83.0F)
                            this.farTurnToDirection(8.5F);
                        else
                            this.farTurnToDirection(7.0F);
                        this.setSpeedMode(10);
                        this.tailForStaying = this.airClient;
                        this.tailOffset.set(this.followOffset);
                        this.tailOffset.scale(-1.0);
                        if (this.dist < 500.0 + Ve.x * 200.0)
                            this.pop();
                        else {
                            if (this.AOA > 12.0F && this.Alt > 15.0F)
                                this.Or.increment(0.0F, 12.0F - this.AOA, 0.0F);
                            if (this.mn_time > 30.0F && (Ve.x < 0.0 || this.dist > 10000.0F))
                                this.pop();
                        }
                    }
                    break;
                case 59:
                    if (this.airClient == null || !Actor.isValid(this.airClient.actor) || !this.isOk()) {
                        this.airClient = null;
                        this.set_maneuver(0);
                    } else {
                        this.maxAOA = 5.0F;
                        if (this.first)
                            this.followOffset.set(1000.0F * (float) (Math.sin(this.beNearOffsPhase * 0.7854F)), 1000.0F * (float) (Math.cos(this.beNearOffsPhase * 0.7854F)), -400.0);
                        Ve.set(this.followOffset);
                        Ve.x -= 300.0;
                        tmpOr.setAT0(this.airClient.Vwld);
                        tmpOr.increment(0.0F, 4.0F, 0.0F);
                        tmpOr.transform(Ve, Po);
                        Po.scale(-1.0);
                        Po.add(this.airClient.Loc);
                        Ve.sub(Po, this.Loc);
                        this.Or.transformInv(Ve);
                        this.dist = (float) Ve.length();
                        Ve.normalize();
                        this.farTurnToDirection();
                        this.setSpeedMode(2);
                        this.tailForStaying = this.airClient;
                        this.tailOffset.set(this.followOffset);
                        this.tailOffset.scale(-1.0);
                        if (this.dist < 300.0F) {
                            this.beNearOffsPhase++;
                            if (this.beNearOffsPhase > 3)
                                this.beNearOffsPhase = 0;
                            float f12 = (float) Math.sqrt(this.followOffset.x * this.followOffset.x + (this.followOffset.y * this.followOffset.y));
                            this.followOffset.set(f12 * (float) (Math.sin(this.beNearOffsPhase * 1.5708F)), f12 * (float) (Math.cos(this.beNearOffsPhase * 1.5708F)), this.followOffset.z);
                        }
                        if (this.AOA > 12.0F && this.Alt > 15.0F)
                            this.Or.increment(0.0F, 12.0F - this.AOA, 0.0F);
                        if (this.mn_time > 15.0F && (Ve.x < 0.0 || this.dist > 3000.0F))
                            this.pop();
                        if (this.mn_time > 30.0F)
                            this.pop();
                    }
                    break;
                case 63:
                    if (!(this instanceof RealFlightModel) || !((RealFlightModel) this).isRealMode()) {
                        this.bombsOut = true;
                        this.CT.dropFuelTanks();
                    }
                    if (this.target == null || !Actor.isValid(this.target.actor) || this.target.isTakenMortalDamage() || !this.hasCourseWeaponBullets()) {
                        this.target = null;
                        this.clear_stack();
                        this.set_maneuver(3);
                    } else if (this.target.getSpeedKMH() < 45.0F && (this.target.Loc.z - Engine.land().HQ_Air(this.target.Loc.x, this.target.Loc.y)) < 50.0 && this.target.actor.getArmy() != this.actor.getArmy()) {
                        this.target_ground = this.target.actor;
                        this.set_task(7);
                        this.clear_stack();
                        this.set_maneuver(43);
                    } else {
                        if (this.actor instanceof HE_LERCHE3 && ((HE_LERCHE3) this.actor).bToFire) {
                            this.CT.WeaponControl[2] = true;
                            ((HE_LERCHE3) this.actor).bToFire = false;
                        }
                        if (this.actor instanceof TA_183 && ((TA_183) this.actor).bToFire) {
                            this.CT.WeaponControl[2] = true;
                            ((TA_183) this.actor).bToFire = false;
                        }
                        if (this.actor instanceof TA_152C && ((TA_152C) this.actor).bToFire) {
                            this.CT.WeaponControl[2] = true;
                            ((TA_152C) this.actor).bToFire = false;
                        }
                        if (this.actor instanceof Mig_17PF && ((Mig_17PF) this.actor).bToFire) {
                            this.CT.WeaponControl[2] = true;
                            ((Mig_17PF) this.actor).bToFire = false;
                        }
                        if (this.TargV.z == -400.0)
                            this.fighterUnderBomber(f);
                        else
                            this.fighterVsBomber(f);
                        if (this.AOA > this.AOA_Crit - 2.0F && this.Alt > 15.0F)
                            this.Or.increment(0.0F, 0.01F * (this.AOA_Crit - 2.0F - this.AOA), 0.0F);
                    }
                    break;
                case 27:
                    if (!(this instanceof RealFlightModel) || !((RealFlightModel) this).isRealMode()) {
                        this.bombsOut = true;
                        this.CT.dropFuelTanks();
                    }
                    if (this.target == null || !Actor.isValid(this.target.actor) || this.target.isTakenMortalDamage() || this.target.actor.getArmy() == this.actor.getArmy() || !this.hasCourseWeaponBullets()) {
                        this.target = null;
                        this.clear_stack();
                        this.set_maneuver(0);
                    } else if (this.target.getSpeedKMH() < 45.0F && (this.target.Loc.z - Engine.land().HQ_Air(this.target.Loc.x, this.target.Loc.y)) < 50.0 && this.target.actor.getArmy() != this.actor.getArmy()) {
                        this.target_ground = this.target.actor;
                        this.set_task(7);
                        this.clear_stack();
                        this.set_maneuver(43);
                    } else {
                        if (this.first && this.actor instanceof TypeAcePlane) {
                            if (this.target != null && this.target.actor != null && this.target.actor.getArmy() != this.actor.getArmy())
                                this.target.Skill = 0;
                            if (this.danger != null && this.danger.actor != null && this.danger.actor.getArmy() != this.actor.getArmy())
                                this.danger.Skill = 0;
                        }
                        if (this.target.actor.getArmy() != this.actor.getArmy())
                            ((Maneuver) this.target).danger = this;
                        if (this.isTick(64, 0)) {
                            float f11 = (this.target.Energy - this.Energy) * 0.1019F;
                            Ve.sub(this.target.Loc, this.Loc);
                            this.Or.transformInv(Ve);
                            Ve.normalize();
                            float f13 = 470.0F + (float) Ve.x * 120.0F - f11;
                            if (f13 < 0.0F) {
                                this.clear_stack();
                                this.set_maneuver(62);
                            }
                        }
                        this.fighterVsFighter(f);
                        this.setSpeedMode(9);
                        if (this.AOA > this.AOA_Crit - 1.0F && this.Alt > 15.0F)
                            this.Or.increment(0.0F, 0.01F * (this.AOA_Crit - 1.0F - this.AOA), 0.0F);
                        if (this.mn_time > 100.0F) {
                            this.target = null;
                            this.pop();
                        }
                    }
                    break;
                case 62:
                    if (this.target == null || !Actor.isValid(this.target.actor) || this.target.isTakenMortalDamage() || this.target.actor.getArmy() == this.actor.getArmy() || !this.hasCourseWeaponBullets()) {
                        this.target = null;
                        this.clear_stack();
                        this.set_maneuver(0);
                    } else if (this.target.getSpeedKMH() < 45.0F && (this.target.Loc.z - Engine.land().HQ_Air(this.target.Loc.x, this.target.Loc.y)) < 50.0 && this.target.actor.getArmy() != this.actor.getArmy()) {
                        this.target_ground = this.target.actor;
                        this.set_task(7);
                        this.clear_stack();
                        this.set_maneuver(43);
                    } else {
                        if (this.first && this.actor instanceof TypeAcePlane) {
                            if (this.target != null && this.target.actor != null && this.target.actor.getArmy() != this.actor.getArmy())
                                this.target.Skill = 0;
                            if (this.danger != null && this.danger.actor != null && this.danger.actor.getArmy() != this.actor.getArmy())
                                this.danger.Skill = 0;
                        }
                        if (this.target.actor.getArmy() != this.actor.getArmy())
                            ((Maneuver) this.target).danger = this;
                        this.goodFighterVsFighter(f);
                    }
                    break;
                case 70:
                    this.checkGround = false;
                    this.checkStrike = false;
                    this.frequentControl = true;
                    this.stallable = false;
                    if (this.actor instanceof HE_LERCHE3) {
                        switch (this.submaneuver) {
                            case SUB_MAN0:
                                this.AP.setStabAll(false);
                                this.submaneuver++;
                                this.sub_Man_Count = 0;
                                /* fall through */
                            case SUB_MAN1:
                                if (this.sub_Man_Count == 0)
                                    this.CT.AileronControl = World.cur().rnd.nextFloat(-0.15F, 0.15F);
                                this.CT.AirBrakeControl = 1.0F;
                                this.CT.setPowerControl(1.0F);
                                this.CT.ElevatorControl = Aircraft.cvt(this.Or.getTangage(), 0.0F, 60.0F, 1.0F, 0.0F);
                                if (this.Or.getTangage() > 30.0F) {
                                    this.submaneuver++;
                                    this.sub_Man_Count = 0;
                                }
                                this.sub_Man_Count++;
                                break;
                            case SUB_MAN2:
                                this.CT.AileronControl = 0.0F;
                                this.CT.ElevatorControl = 0.0F;
                                this.CT.setPowerControl(0.1F);
                                this.Or.increment(0.0F, (float) (f * 0.1 * this.sub_Man_Count * (90.0 - this.Or.getTangage())), 0.0F);
                                if (this.Or.getTangage() > 89.0F) {
                                    this.saveOr.set(this.Or);
                                    this.submaneuver++;
                                }
                                this.sub_Man_Count++;
                                break;
                            case SUB_MAN3:
                                this.CT.ElevatorControl = 0.0F;
                                if (this.Alt > 10.0F)
                                    this.CT.setPowerControl(0.33F);
                                else
                                    this.CT.setPowerControl(0.0F);
                                if (this.Alt < 20.0F) {
                                    if (this.Vwld.z < -4.0)
                                        this.Vwld.z *= 0.95;
                                    if (this.Vwld.lengthSquared() < 1.0)
                                        this.EI.setEngineStops();
                                }
                                this.Or.set(this.saveOr);
                                if (!(this.mn_time > 100.0F))
                                    break;
                                this.Vwld.set(0.0, 0.0, 0.0);
                                MsgDestroy.Post(Time.current() + 12000L, this.actor);
                        }
                    }
                    break;
                case 25:
                    this.wingman(false);
                    if (this.AP.way.isLanding()) {
                        if (this.AP.way.isLandingOnShip()) {
                            this.AP.way.landingAirport.rebuildLandWay(this);
                            if (this.CT.GearControl == 1.0F && this.CT.arrestorControl < 1.0F && !this.Gears.onGround())
                                this.AS.setArrestor(this.actor, 1);
                        }
                        if (this.first) {
                            this.AP.setWayPoint(true);
                            this.doDumpBombsPassively();
                            this.submaneuver = SUB_MAN0;
                        }
                        if (this.actor instanceof HE_LERCHE3 && this.Alt < 50.0F)
                            this.maneuver = 70;
                        this.AP.way.curr().getP(Po);
                        int k = this.AP.way.Cur();
                        float f17 = (float) this.Loc.z - this.AP.way.last().z();
                        this.AP.way.setCur(k);
                        this.Alt = Math.min(this.Alt, f17);
                        Po.add(0.0, 0.0, -3.0);
                        Ve.sub(Po, this.Loc);
                        float f20 = (float) Ve.length();
                        boolean flag3 = this.Alt > 4.5F + this.Gears.H && this.AP.way.Cur() < 8;
                        if (this.AP.way.isLandingOnShip())
                            flag3 = this.Alt > 4.5F + this.Gears.H && this.AP.way.Cur() < 8;
                        if (flag3) {
                            this.AP.way.prev();
                            this.AP.way.curr().getP(Pc);
                            this.AP.way.next();
                            tmpV3f.sub(Po, Pc);
                            tmpV3f.normalize();
                            if (tmpV3f.dot(Ve) < 0.0 && f20 > 1000.0F && !this.TaxiMode) {
                                this.AP.way.first();
                                this.push(10);
                                this.pop();
                                this.CT.GearControl = 0.0F;
                            }
                            float f25 = (float) tmpV3f.dot(Ve);
                            tmpV3f.scale(-f25);
                            tmpV3f.add(Po, tmpV3f);
                            tmpV3f.sub(this.Loc);
                            float f29 = 5.0E-4F * (3000.0F - f20);
                            if (f29 > 1.0F)
                                f29 = 1.0F;
                            if (f29 < 0.1F)
                                f29 = 0.1F;
                            float f31 = (float) tmpV3f.length();
                            if (f31 > 40.0F * f29) {
                                f31 = 40.0F * f29;
                                tmpV3f.normalize();
                                tmpV3f.scale(f31);
                            }
                            float f33 = this.VminFLAPS;
                            if (this.AP.way.Cur() >= 6) {
                                if (this.AP.way.isLandingOnShip()) {
                                    if (Actor.isAlive(this.AP.way.landingAirport) && (this.AP.way.landingAirport instanceof AirportCarrier)) {
                                        float f34 = (float) ((AirportCarrier) this.AP.way.landingAirport).speedLen();
                                        if (this.VminFLAPS < f34 + 10.0F)
                                            f33 = f34 + 10.0F;
                                    }
                                } else
                                    f33 = this.VminFLAPS * 1.2F;
                                if (f33 < 14.0F)
                                    f33 = 14.0F;
                            } else
                                f33 = this.VminFLAPS * 2.0F;
                            double d4 = this.Vwld.length();
                            double d6 = f33 - d4;
                            float f35 = 2.0F * f;
                            if (d6 > f35)
                                d6 = f35;
                            if (d6 < -f35)
                                d6 = -f35;
                            Ve.normalize();
                            Ve.scale(d4);
                            Ve.add(tmpV3f);
                            Ve.sub(this.Vwld);
                            float f36 = (50.0F * f29 - f31) * f;
                            if (Ve.length() > f36) {
                                Ve.normalize();
                                Ve.scale(f36);
                            }
                            this.Vwld.add(Ve);
                            this.Vwld.normalize();
                            this.Vwld.scale(d4 + d6);
                            this.Loc.x += this.Vwld.x * f;
                            this.Loc.y += this.Vwld.y * f;
                            this.Loc.z += this.Vwld.z * f;
                            this.Or.transformInv(tmpV3f);
                            tmpOr.setAT0(this.Vwld);
                            float f37 = 0.0F;
                            if (this.AP.way.isLandingOnShip())
                                f37 = 0.9F * (45.0F - this.Alt);
                            else
                                f37 = 0.7F * (20.0F - this.Alt);
                            if (f37 < 0.0F)
                                f37 = 0.0F;
                            tmpOr.increment(0.0F, 4.0F + f37, (float) (tmpV3f.y * 0.5));
                            this.Or.interpolate(tmpOr, 0.5F * f);
                            this.callSuperUpdate = false;
                            this.W.set(0.0, 0.0, 0.0);
                            this.CT.ElevatorControl = 0.05F + 0.3F * f37;
                            this.CT.RudderControl = (float) (-tmpV3f.y * 0.02);
                            this.direction = this.Or.getAzimut();
                        } else
                            this.AP.setStabDirection(true);
                    } else
                        this.AP.setStabDirection(true);
                    this.dA = this.CT.ElevatorControl;
                    this.AP.update(f);
                    this.setSpeedControl(f);
                    this.CT.ElevatorControl = this.dA;
                    if (this.maneuver == 25) {
                        if (this.Alt > 60.0F) {
                            if (this.Alt < 160.0F)
                                this.CT.FlapsControl = 0.32F;
                            else
                                this.CT.FlapsControl = 0.0F;
                            this.setSpeedMode(7);
                            this.smConstPower = 0.2F;
                            this.dA = Math.min(130.0F + this.Alt, 270.0F);
                            if (this.Vwld.z > 0.0 || this.getSpeedKMH() < this.dA)
                                this.dA = -1.2F * f;
                            else
                                this.dA = 1.2F * f;
                            this.CT.ElevatorControl = this.CT.ElevatorControl * 0.9F + this.dA * 0.1F;
                        } else {
                            this.minElevCoeff = 15.0F;
                            if (this.AP.way.Cur() >= 6 || this.AP.way.Cur() == 0) {
                                if (this.Or.getTangage() < -5.0F)
                                    this.Or.increment(0.0F, -this.Or.getTangage() - 5.0F, 0.0F);
                                if (this.Or.getTangage() > this.Gears.Pitch + 10.0F)
                                    this.Or.increment(0.0F, -(this.Or.getTangage() - (this.Gears.Pitch + 10.0F)), 0.0F);
                            }
                            this.CT.FlapsControl = 1.0F;
                            if (this.Vrel.length() < 1.0) {
                                this.CT.FlapsControl = this.CT.BrakeControl = 0.0F;
                                if (!this.TaxiMode) {
                                    this.setSpeedMode(8);
                                    if (this.AP.way.isLandingOnShip()) {
                                        if (this.CT.getFlap() < 0.0010F)
                                            this.AS.setWingFold(this.actor, 1);
                                        this.CT.BrakeControl = 1.0F;
                                        if (this.CT.arrestorControl == 1.0F && this.Gears.onGround())
                                            this.AS.setArrestor(this.actor, 0);
                                        MsgDestroy.Post(Time.current() + 25000L, this.actor);
                                    } else {
                                        this.EI.setEngineStops();
                                        if (this.EI.engines[0].getPropw() < 1.0F) {
                                            World.cur();
                                            if (this.actor != World.getPlayerAircraft())
                                                MsgDestroy.Post((Time.current() + 12000L), this.actor);
                                        }
                                    }
                                }
                            }
                            if (this.getSpeed() < this.VmaxFLAPS * 0.21F)
                                this.CT.FlapsControl = 0.0F;
                            if (this.Vrel.length() < this.VmaxFLAPS * 0.25F && this.wayCurPos == null && !this.AP.way.isLandingOnShip()) {
                                this.TaxiMode = true;
                                this.AP.way.setCur(0);
                                return;
                            }
                            if (this.getSpeed() > this.VminFLAPS * 0.6F && this.Alt < 10.0F) {
                                this.setSpeedMode(8);
                                if (this.AP.way.isLandingOnShip() && this.CT.bHasArrestorControl) {
                                    if (this.Vwld.z < -5.5)
                                        this.Vwld.z *= 0.949999988079071;
                                    if (this.Vwld.z > 0.0)
                                        this.Vwld.z *= 0.9100000262260437;
                                } else {
                                    if (this.Vwld.z < -0.6000000238418579)
                                        this.Vwld.z *= 0.9399999976158142;
                                    if (this.Vwld.z < -2.5)
                                        this.Vwld.z = -2.5;
                                    if (this.Vwld.z > 0.0)
                                        this.Vwld.z *= 0.9100000262260437;
                                }
                            }
                            float f14 = this.Gears.Pitch - 2.0F;
                            if (f14 < 5.0F)
                                f14 = 5.0F;
                            if (this.Alt < 7.0F && this.Or.getTangage() < f14 || this.AP.way.isLandingOnShip())
                                this.CT.ElevatorControl += 1.5F * f;
                            this.CT.ElevatorControl += 0.05000000074505806 * this.getW().y;
                            if (this.Gears.onGround()) {
                                if (this.Gears.Pitch > 5.0F) {
                                    if (this.Or.getTangage() < this.Gears.Pitch)
                                        this.CT.ElevatorControl += 1.5F * f;
                                    if (!this.AP.way.isLandingOnShip())
                                        this.CT.ElevatorControl += 0.30000001192092896 * this.getW().y;
                                } else
                                    this.CT.ElevatorControl = 0.0F;
                                if (!this.TaxiMode) {
                                    this.AFo.setDeg(this.Or.getAzimut(), this.direction);
                                    this.CT.RudderControl = (8.0F * this.AFo.getDiffRad() + 0.5F * (float) this.getW().z);
                                } else
                                    this.CT.RudderControl = 0.0F;
                            }
                        }
                        this.AP.way.curr().getP(Po);
                        return;
                    }
                    return;
                case 66:
                    if (!this.isCapableOfTaxiing() || this.EI.getThrustOutput() < 0.01F) {
                        this.set_task(3);
                        this.maneuver = 0;
                        this.set_maneuver(49);
                        this.AP.setStabAll(false);
                    } else {
                        if (this.AS.isPilotDead(0)) {
                            this.set_task(3);
                            this.maneuver = 0;
                            this.set_maneuver(44);
                            this.setSpeedMode(8);
                            this.smConstPower = 0.0F;
                            if (Airport.distToNearestAirport(this.Loc) > 900.0)
                                ((Aircraft) this.actor).postEndAction(6000.0, this.actor, 3, null);
                            else
                                MsgDestroy.Post(Time.current() + 300000L, this.actor);
                        } else {
                            P.x = this.Loc.x;
                            P.y = this.Loc.y;
                            P.z = this.Loc.z;
                            Vcur.set(this.Vwld);
                            if (this.wayCurPos == null) {
                                World.cur().airdrome.findTheWay((Pilot) this);
                                this.wayPrevPos = this.wayCurPos = this.getNextAirdromeWayPoint();
                            }
                            if (this.wayCurPos != null) {
                                Point_Any point_any = this.wayCurPos;
//								Point_Any point_any1 = wayPrevPos;
                                Pcur.set((float) P.x, (float) P.y);
                                float f21 = Pcur.distance(point_any);
//								float f23 = Pcur.distance(point_any1);
                                V_to.sub(point_any, Pcur);
                                V_to.normalize();
                                float f26 = 5.0F + 0.1F * f21;
                                if (f26 > 12.0F)
                                    f26 = 12.0F;
                                if (f26 > 0.9F * this.VminFLAPS)
                                    f26 = 0.9F * this.VminFLAPS;
                                if ((this.curAirdromePoi < this.airdromeWay.length && f21 < 15.0F) || f21 < 4.0F) {
                                    f26 = 0.0F;
                                    Point_Any point_any2 = this.getNextAirdromeWayPoint();
                                    if (point_any2 == null) {
                                        this.CT.setPowerControl(0.0F);
                                        this.Loc.set(P);
                                        this.Loc.set(this.Loc);
                                        if (!this.finished) {
                                            this.finished = true;
                                            int l = 1000;
                                            if (this.wayCurPos != null)
                                                l = 2400000;
                                            this.actor.collide(true);
                                            this.Vwld.set(0.0, 0.0, 0.0);
                                            this.CT.setPowerControl(0.0F);
                                            this.EI.setCurControlAll(true);
                                            this.EI.setEngineStops();
                                            World.cur();
                                            if (this.actor != World.getPlayerAircraft()) {
                                                MsgDestroy.Post((Time.current() + l), this.actor);
                                                this.setStationedOnGround(true);
                                                this.maneuver = 0;
                                                this.set_maneuver(44);
                                            }
                                            return;
                                        }
                                        return;
                                    }
                                    this.wayPrevPos = this.wayCurPos;
                                    this.wayCurPos = point_any2;
                                }
                                V_to.scale(f26);
                                float f30 = 2.0F * f;
                                Vdiff.set(V_to);
                                Vdiff.sub(Vcur);
                                float f32 = (float) Vdiff.length();
                                if (f32 > f30) {
                                    Vdiff.normalize();
                                    Vdiff.scale(f30);
                                }
                                Vcur.add(Vdiff);
                                tmpOr.setYPR(FMMath.RAD2DEG((float) Vcur.direction()), this.Or.getPitch(), 0.0F);
                                this.Or.interpolate(tmpOr, 0.2F);
                                this.Vwld.x = Vcur.x;
                                this.Vwld.y = Vcur.y;
                                P.x += Vcur.x * f;
                                P.y += Vcur.y * f;
                            } else {
                                this.wayPrevPos = this.wayCurPos = new Point_Null((float) this.Loc.x, (float) this.Loc.y);
                                World.cur();
                                if (this.actor != World.getPlayerAircraft()) {
                                    MsgDestroy.Post(Time.current() + 30000L, this.actor);
                                    this.setStationedOnGround(true);
                                }
                            }
                            this.Loc.set(P);
                            this.Loc.set(this.Loc);
                            return;
                        }
                        return;
                    }
                    break;
                case 49:
                    this.emergencyLanding(f);
                    break;
                case 64:
                    if (this.actor instanceof TypeGlider)
                        this.pop();
                    else {
                        if (this.actor instanceof HE_LERCHE3) {
                            boolean flag1 = (Actor.isAlive(this.AP.way.takeoffAirport) && (this.AP.way.takeoffAirport instanceof AirportCarrier));
                            if (!flag1)
                                this.callSuperUpdate = false;
                        }
                        this.CT.BrakeControl = 1.0F;
                        this.CT.ElevatorControl = 0.5F;
                        this.CT.setPowerControl(0.0F);
                        this.EI.setCurControlAll(false);
                        this.setSpeedMode(0);
                        if (World.Rnd().nextFloat() < 0.05F) {
                            this.EI.setCurControl(this.submaneuver, true);
                            if (this.EI.engines[this.submaneuver].getStage() == 0) {
                                this.setSpeedMode(0);
                                this.CT.setPowerControl(0.05F);
                                this.EI.engines[this.submaneuver].setControlThrottle(0.2F);
                                this.EI.toggle();
                                if (this.actor instanceof BI_1 || this.actor instanceof BI_6) {
                                    this.pop();
                                    break;
                                }
                            }
                        }
                        if (this.EI.engines[this.submaneuver].getStage() == 6) {
                            this.setSpeedMode(0);
                            this.EI.engines[this.submaneuver].setControlThrottle(0.0F);
                            this.submaneuver++;
                            this.CT.setPowerControl(0.0F);
                            if (this.submaneuver > this.EI.getNum() - 1) {
                                this.EI.setCurControlAll(true);
                                this.pop();
                            }
                        }
                    }
                    break;
                case 26: {
                    float f15 = this.Alt;
                    float f18 = 0.4F;
                    if (Actor.isAlive(this.AP.way.takeoffAirport) && this.AP.way.takeoffAirport instanceof AirportCarrier) {
                        f15 -= ((AirportCarrier) this.AP.way.takeoffAirport).height();
                        f18 = 0.95F;
                        if (this.Alt < 9.0F && this.Vwld.z < 0.0)
                            this.Vwld.z *= 0.85;
                        if (this.CT.bHasCockpitDoorControl)
                            this.AS.setCockpitDoor(this.actor, 1);
                    }
                    if (this.first) {
                        this.setCheckGround(false);
                        this.CT.BrakeControl = 1.0F;
                        this.CT.GearControl = 1.0F;
                        this.CT.setPowerControl(0.0F);
                        if (this.CT.bHasArrestorControl)
                            this.AS.setArrestor(this.actor, 0);
                        this.setSpeedMode(8);
                        if (f15 > 7.21F && this.AP.way.Cur() == 0) {
                            this.EI.setEngineRunning();
                            Aircraft.debugprintln(this.actor, "in the air - engines running!.");
                        }
                        if (!Actor.isAlive(this.AP.way.takeoffAirport))
                            this.direction = this.actor.pos.getAbsOrient().getAzimut();
                        if (this.actor instanceof HE_LERCHE3) {
                            this.maneuver = 69;
                            break;
                        }
                    }
                    if (this.Gears.onGround()) {
                        if (this.EI.engines[0].getStage() == 0) {
                            this.CT.setPowerControl(0.0F);
                            this.setSpeedMode(8);
                            if (World.Rnd().nextFloat() < 0.05F && this.mn_time > 1.0F || this.mn_time > 8.0F) {
                                this.push();
                                this.push(64);
                                this.submaneuver = SUB_MAN0;
                                this.maneuver = 0;
                                this.safe_pop();
                                break;
                            }
                        } else {
                            // TODO: CTO Mod
                            // -------------------------------
                            if (!this.bAlreadyCheckedStage7) {
                                if (super.AP.way.takeoffAirport instanceof com.maddox.il2.ai.AirportCarrier) {
                                    com.maddox.il2.ai.AirportCarrier airportcarrier = (com.maddox.il2.ai.AirportCarrier) super.AP.way.takeoffAirport;
                                    com.maddox.il2.objects.ships.BigshipGeneric bigshipgeneric = airportcarrier.ship();
                                    super.Gears.setCatapultOffset(bigshipgeneric);
                                    this.bCatapultAI = super.Gears.getCatapultAI();
                                } else {
                                    this.bStage7 = true;
                                }
                                this.bAlreadyCheckedStage7 = true;
                            }
                            if (!this.bCatapultAI) {
                                Po.set(super.Loc);
                                Vpl.set(60D, 0.0D, 0.0D);
                                this.fNearestDistance = 70F;
                                super.Or.transform(Vpl);
                                Po.add(Vpl);
                                Pd.set(Po);
                            } else {
                                Po.set(super.Loc);
                                Vpl.set(200D, 0.0D, 0.0D);
                                this.fNearestDistance = 210F;
                                super.Or.transform(Vpl);
                                Po.add(Vpl);
                                Pd.set(Po);
                            }
                            if (this.canTakeoff) {
                                if (!this.bStage7) {
                                    if (this.bStage6) {
                                        if (this.bFastLaunchAI || !super.CT.bHasWingControl || super.CT.bHasWingControl && super.CT.getWing() < 0.5F)
                                            this.bStage7 = true;
                                    } else if (this.bStage4) {
                                        if (super.CT.bHasWingControl && super.CT.getWing() > 0.001F) {
                                            if (this.bFastLaunchAI)
                                                super.CT.forceWing(0.0F);
                                            super.AS.setWingFold(super.actor, 0);
                                        }
                                        this.bStage6 = true;
                                    } else if (this.bStage3) {
                                        com.maddox.il2.engine.Loc loc = new Loc();
                                        com.maddox.il2.objects.ships.BigshipGeneric bigshipgeneric1 = (com.maddox.il2.objects.ships.BigshipGeneric) super.brakeShoeLastCarrier;
                                        com.maddox.il2.objects.air.Aircraft aircraft = (com.maddox.il2.objects.air.Aircraft) super.actor;
                                        com.maddox.il2.ai.air.CellAirField cellairfield = bigshipgeneric1.getCellTO();
                                        double d3;
                                        double d6;
                                        if (this.bCatapultAI) {
                                            d3 = -((com.maddox.JGP.Tuple3d) (cellairfield.leftUpperCorner())).x - super.Gears.getCatapultOffsetX();
                                            d6 = ((com.maddox.JGP.Tuple3d) (cellairfield.leftUpperCorner())).y - super.Gears.getCatapultOffsetY();
                                        } else {
                                            d3 = -((com.maddox.JGP.Tuple3d) (cellairfield.leftUpperCorner())).x - (cellairfield.getWidth() / 2 - 3);
                                            d6 = super.brakeShoeLoc.getX() + aircraft.getCellAirPlane().getHeight() + 4D;
                                        }
                                        loc.set(d6, d3, super.brakeShoeLoc.getZ(), super.brakeShoeLoc.getAzimut(), super.brakeShoeLoc.getTangage(), super.brakeShoeLoc.getKren());
                                        loc.add(super.brakeShoeLastCarrier.pos.getAbs());
                                        super.actor.pos.setAbs(loc.getPoint());
                                        super.brakeShoeLoc.set(super.actor.pos.getAbs());
                                        super.brakeShoeLoc.sub(super.brakeShoeLastCarrier.pos.getAbs());
                                        this.bStage4 = true;
                                    } else {
                                        super.CT.PowerControl = 1.0F;
                                        this.bStage3 = true;
                                    }
                                } else {
                                    super.CT.PowerControl = 1.1F;
                                    this.setSpeedMode(9);
                                }
                            }
                            // -------------------------------
                            else {
                                this.setSpeedMode(8);
                                this.CT.BrakeControl = 1.0F;
                                boolean bool = true;
                                if (this.mn_time < 8.0F)
                                    bool = false;
                                // TODO: CTO Mod
                                // ---------------------------------------------------------
                                if (super.actor != com.maddox.il2.ai.War.getNearestFriendAtPoint(Pd, (com.maddox.il2.objects.air.Aircraft) super.actor, this.fNearestDistance)) {
                                    if (super.actor instanceof com.maddox.il2.objects.air.G4M2E) {
                                        if (com.maddox.il2.ai.War.getNearestFriendAtPoint(Pd, (com.maddox.il2.objects.air.Aircraft) super.actor, this.fNearestDistance) != ((com.maddox.il2.objects.air.G4M2E) super.actor).typeDockableGetDrone())
                                            bool = false;
                                    } else {
                                        bool = false;
                                    }
                                }
                                // ---------------------------------------------------------
                                if (Actor.isAlive(this.AP.way.takeoffAirport) && this.AP.way.takeoffAirport.takeoffRequest > 0)
                                    bool = false;
                                if (bool) {
                                    this.canTakeoff = true;
                                    if (Actor.isAlive(this.AP.way.takeoffAirport))
                                        this.AP.way.takeoffAirport.takeoffRequest = 300;
                                }
                            }
                        }
                        // TODO: Altered by CTO Mod
                        // if (EI.engines[0].getStage() == 6 && CT.bHasWingControl && CT.getWing() > 0.0010F)
                        if (this.EI.engines[0].getStage() == 6 && this.CT.bHasWingControl && this.CT.getWing() > 0.001F && !(this.AP.way.takeoffAirport instanceof AirportCarrier))
                            this.AS.setWingFold(this.actor, 0);
                    } else if (this.canTakeoff) {
                        this.CT.setPowerControl(1.1F);
                        this.setSpeedMode(9);
                    }
                    if (this.CT.FlapsControl == 0.0F && this.CT.getWing() < 0.0010F)
                        this.CT.FlapsControl = 0.4F;
                    if (this.EI.engines[0].getStage() == 6 && this.CT.getPower() > f18) {
                        this.CT.BrakeControl = 0.0F;
                        this.brakeShoe = false;
                        float f22 = this.Vmin * this.M.getFullMass() / this.M.massEmpty;
                        float f24 = 12.0F * (this.getSpeed() / f22 - 0.2F);
                        if (this.Gears.bIsSail)
                            f24 *= 2.0F;
                        if (this.Gears.bFrontWheel)
                            f24 = this.Gears.Pitch + 4.0F;
                        if (f24 < 1.0F)
                            f24 = 1.0F;
                        if (f24 > 10.0F)
                            f24 = 10.0F;
                        float f27 = 1.5F;
                        if (Actor.isAlive(this.AP.way.takeoffAirport) && this.AP.way.takeoffAirport instanceof AirportCarrier && !this.Gears.isUnderDeck()) {
                            this.CT.GearControl = 0.0F;
                            if (f15 < 0.0F) {
                                f24 = 18.0F;
                                f27 = 0.05F;
                            } else {
                                f24 = 14.0F;
                                f27 = 0.3F;
                            }
                        }
                        if (this.Or.getTangage() < f24)
                            this.dA = (-0.7F * (this.Or.getTangage() - f24) + f27 * (float) this.getW().y + 0.5F * (float) this.getAW().y);
                        else
                            this.dA = (-0.1F * (this.Or.getTangage() - f24) + f27 * (float) this.getW().y + 0.5F * (float) this.getAW().y);
                        this.CT.ElevatorControl += (this.dA - this.CT.ElevatorControl) * 3.0F * f;
                    } else
                        this.CT.ElevatorControl = 1.0F;
                    this.AFo.setDeg(this.Or.getAzimut(), this.direction);
                    double d1 = this.AFo.getDiffRad();
                    if (this.EI.engines[0].getStage() == 6) {
                        this.CT.RudderControl = 8.0F * (float) d1;
                        if (d1 > -1.0 && d1 < 1.0) {
                            if (Actor.isAlive(this.AP.way.takeoffAirport) && this.CT.getPower() > 0.3F) {
                                double d2 = this.AP.way.takeoffAirport.ShiftFromLine(this);
                                double d3 = 3.0 - 3.0 * Math.abs(d1);
                                if (d3 > 1.0)
                                    d3 = 1.0;
                                double d5 = 0.25 * d2 * d3;
                                if (d5 > 1.5)
                                    d5 = 1.5;
                                if (d5 < -1.5)
                                    d5 = -1.5;
                                this.CT.RudderControl += (float) d5;
                            }
                        } else
                            this.CT.BrakeControl = 1.0F;
                    }
                    this.CT.AileronControl = -0.05F * this.Or.getKren() + 0.3F * (float) this.getW().y;
                    if (f15 > 5.0F && !this.Gears.isUnderDeck())
                        this.CT.GearControl = 0.0F;
                    float f28 = 1.0F;
                    if (this.hasBombs())
                        f28 *= 1.7F;
                    if (f15 > 120.0F * f28 || this.getSpeed() > this.Vmin * 1.8F * f28 || f15 > 80.0F * f28 && this.getSpeed() > this.Vmin * 1.6F * f28
                            || (f15 > 40.0F * f28 && this.getSpeed() > this.Vmin * 1.3F * f28 && (this.mn_time > 60.0F + (((Aircraft) this.actor).aircIndex() * 3.0F)))) {
                        this.CT.FlapsControl = 0.0F;
                        this.CT.GearControl = 0.0F;
                        this.rwLoc = null;
                        if (this.actor instanceof TypeGlider)
                            this.push(24);
                        this.maneuver = 0;
                        this.brakeShoe = false;
                        this.turnOffCollisions = false;
                        if (this.CT.bHasCockpitDoorControl)
                            this.AS.setCockpitDoor(this.actor, 0);
                        this.pop();
                    }
                    if (this.actor instanceof TypeGlider) {
                        this.CT.BrakeControl = 0.0F;
                        this.CT.ElevatorControl = 0.05F;
                        this.canTakeoff = true;
                    }
                    break;
                }
                case 69: {
                    float f16 = this.Alt;
                    float f19 = 0.4F;
                    this.CT.BrakeControl = 1.0F;
                    this.W.scale(0.0);
                    boolean flag4 = (Actor.isAlive(this.AP.way.takeoffAirport) && this.AP.way.takeoffAirport instanceof AirportCarrier);
                    if (flag4) {
                        f16 -= ((AirportCarrier) this.AP.way.takeoffAirport).height();
                        f19 = 0.8F;
                        if (this.Alt < 9.0F && this.Vwld.z < 0.0)
                            this.Vwld.z *= 0.85;
                        if (this.CT.bHasCockpitDoorControl)
                            this.AS.setCockpitDoor(this.actor, 1);
                    }
                    if (this.Loc.z != 0.0 && ((HE_LERCHE3) this.actor).suka.getPoint().z == 0.0)
                        ((HE_LERCHE3) this.actor).suka.set(this.Loc, this.Or);
                    if (this.Loc.z != 0.0) {
                        if (this.EI.getPowerOutput() < f19 && !flag4) {
                            this.callSuperUpdate = false;
                            this.Loc.set(((HE_LERCHE3) this.actor).suka.getPoint());
                            this.Or.set(((HE_LERCHE3) this.actor).suka.getOrient());
                        } else if (f16 < 100.0F)
                            this.Or.set(((HE_LERCHE3) this.actor).suka.getOrient());
                    }
                    if (this.Gears.onGround() && this.EI.engines[0].getStage() == 0) {
                        this.CT.setPowerControl(0.0F);
                        this.setSpeedMode(8);
                        if (World.Rnd().nextFloat() < 0.05F && this.mn_time > 1.0F || this.mn_time > 8.0F) {
                            this.push();
                            this.push(64);
                            this.submaneuver = SUB_MAN0;
                            this.maneuver = 0;
                            this.safe_pop();
                            break;
                        }
                    }
                    if (this.EI.engines[0].getStage() == 6) {
                        this.CT.BrakeControl = 0.0F;
                        this.CT.AirBrakeControl = 1.0F;
                        this.brakeShoe = false;
                        this.CT.setPowerControl(1.1F);
                        this.setSpeedMode(9);
                    }
                    if (f16 > 200.0F) {
                        this.CT.GearControl = 0.0F;
                        this.rwLoc = null;
                        this.CT.ElevatorControl = -1.0F;
                        this.CT.AirBrakeControl = 0.0F;
                        if (this.Or.getTangage() < 25.0F)
                            this.maneuver = 0;
                        this.brakeShoe = false;
                        this.turnOffCollisions = false;
                        if (this.CT.bHasCockpitDoorControl)
                            this.AS.setCockpitDoor(this.actor, 0);
                        this.pop();
                    }
                    break;
                }
                case 28:
                    if (this.first) {
                        this.direction = World.Rnd().nextFloat(-25.0F, 25.0F);
                        this.AP.setStabAll(false);
                        this.setSpeedMode(6);
                        this.CT.RudderControl = World.Rnd().nextFloat(-0.22F, 0.22F);
                        this.submaneuver = SUB_MAN0;
                        if (this.getSpeed() < this.Vmin * 1.5F)
                            this.pop();
                    }
                    this.CT.AileronControl = -0.04F * (this.Or.getKren() - this.direction);
                    switch (this.submaneuver) {
                        case SUB_MAN0:
                            this.dA = 1.0F;
                            this.maxAOA = 12.0F + 1.0F * this.Skill;
                            if (this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA)
                                this.CT.ElevatorControl -= 0.6F * f;
                            else
                                this.CT.ElevatorControl += 3.3F * f;
                            if (this.Or.getTangage() > 40.0F + 5.1F * this.Skill)
                                this.submaneuver++;
                            break;
                        case SUB_MAN1:
                            this.direction = World.Rnd().nextFloat(-25.0F, 25.0F);
                            this.CT.RudderControl = World.Rnd().nextFloat(-0.75F, 0.75F);
                            this.submaneuver++;
                            /* fall through */
                        case SUB_MAN2:
                            this.dA = -1.0F;
                            this.maxAOA = 12.0F + 5.0F * this.Skill;
                            if (this.AOA < -this.maxAOA || this.CT.ElevatorControl < this.dA)
                                this.CT.ElevatorControl += 0.6F * f;
                            else
                                this.CT.ElevatorControl -= 3.3F * f;
                            if (this.Or.getTangage() < -45.0F)
                                this.pop();
                            break;
                    }
                    if (this.mn_time > 3.0F)
                        this.pop();
                    break;
                case 29:
                    this.minElevCoeff = 17.0F;
                    if (this.first) {
                        this.AP.setStabAll(false);
                        this.setSpeedMode(9);
                        this.sub_Man_Count = 0;
                    }
                    if (this.danger != null) {
                        if (this.sub_Man_Count == 0) {
                            tmpV3d.set(this.danger.getW());
                            this.danger.Or.transform(tmpV3d);
                            if (tmpV3d.z > 0.0)
                                this.sinKren = -World.Rnd().nextFloat(60.0F, 90.0F);
                            else
                                this.sinKren = World.Rnd().nextFloat(60.0F, 90.0F);
                        }
                        if (this.Loc.distanceSquared(this.danger.Loc) < 5000.0) {
                            this.setSpeedMode(8);
                            this.CT.setPowerControl(0.0F);
                        } else
                            this.setSpeedMode(9);
                    } else
                        this.pop();
                    this.CT.FlapsControl = 0.2F;
                    if (this.getSpeed() < 120.0)
                        this.CT.FlapsControl = 0.33F;
                    if (this.getSpeed() < 80.0)
                        this.CT.FlapsControl = 1.0F;
                    this.CT.AileronControl = -0.08F * (this.Or.getKren() + this.sinKren);
                    this.CT.ElevatorControl = 0.9F;
                    this.CT.RudderControl = 0.0F;
                    this.sub_Man_Count++;
                    if (this.sub_Man_Count > 15)
                        this.sub_Man_Count = 0;
                    if (this.mn_time > 10.0F)
                        this.pop();
                    break;
                case 56:
                    if (this.first) {
                        this.submaneuver = World.Rnd().nextInt(SUB_MAN0, SUB_MAN1);
                        this.direction = World.Rnd().nextFloat(-20.0F, -10.0F);
                    }
                    this.CT.AileronControl = -0.08F * (this.Or.getKren() - this.direction);
                    switch (this.submaneuver) {
                        case SUB_MAN0:
                            this.dA = 1.0F;
                            this.maxAOA = 10.0F + 2.0F * this.Skill;
                            if ((this.getOverload() > 1.0 / Math.abs(Math.cos(Math.toRadians(this.Or.getKren())))) || this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA)
                                this.CT.ElevatorControl -= 0.2F * f;
                            else
                                this.CT.ElevatorControl += 0.4F * f;
                            this.CT.RudderControl = -1.0F * (float) Math.sin(Math.toRadians(this.Or.getKren() + 55.0F));
                            if (this.mn_time > 1.5F)
                                this.submaneuver++;
                            break;
                        case SUB_MAN1:
                            this.direction = World.Rnd().nextFloat(10.0F, 20.0F);
                            this.submaneuver++;
                            /* fall through */
                        case SUB_MAN2:
                            this.dA = 1.0F;
                            this.maxAOA = 10.0F + 2.0F * this.Skill;
                            if ((this.getOverload() > 1.0 / Math.abs(Math.cos(Math.toRadians(this.Or.getKren())))) || this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA)
                                this.CT.ElevatorControl -= 0.2F * f;
                            else
                                this.CT.ElevatorControl += 0.4F * f;
                            this.CT.RudderControl = 1.0F * (float) Math.sin(Math.toRadians(this.Or.getKren() - 55.0F));
                            if (this.mn_time > 4.5F)
                                this.submaneuver++;
                            break;
                        case SUB_MAN3:
                            this.direction = World.Rnd().nextFloat(-20.0F, -10.0F);
                            this.submaneuver++;
                            /* fall through */
                        case SUB_MAN4:
                            this.dA = 1.0F;
                            this.maxAOA = 10.0F + 2.0F * this.Skill;
                            if ((this.getOverload() > 1.0 / Math.abs(Math.cos(Math.toRadians(this.Or.getKren())))) || this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA)
                                this.CT.ElevatorControl -= 0.2F * f;
                            else
                                this.CT.ElevatorControl += 0.4F * f;
                            this.CT.RudderControl = -1.0F * (float) Math.sin(Math.toRadians(this.Or.getKren() + 55.0F));
                            if (!(this.mn_time > 6.0F))
                                break;
                            this.pop();
                    }
                    break;
                case 30:
                    this.push(14);
                    this.push(18);
                    this.pop();
                    break;
                case 31:
                    this.push(14);
                    this.push(19);
                    this.pop();
                    break;
                case 32:
                    if (!this.isCapableOfACM() && World.Rnd().nextInt(-2, 9) < this.Skill)
                        ((Aircraft) this.actor).hitDaSilk();
                    if (this.first) {
                        this.AP.setStabAll(false);
                        this.setSpeedMode(6);
                        this.submaneuver = SUB_MAN0;
                        this.direction = 178.0F - (World.Rnd().nextFloat(0.0F, 8.0F) * this.Skill);
                    }
                    this.maxAOA = this.Vwld.z <= 0.0 ? 24.0F : 14.0F;
                    if (this.danger != null) {
                        Ve.sub(this.danger.Loc, this.Loc);
                        this.dist = (float) Ve.length();
                        this.Or.transformInv(Ve);
                        Vpl.set(this.danger.Vwld);
                        this.Or.transformInv(Vpl);
                        Vpl.normalize();
                    }
                    switch (this.submaneuver) {
                        case SUB_MAN0:
                            this.dA = this.Or.getKren() - 180.0F;
                            if (this.dA < -180.0F)
                                this.dA += 360.0F;
                            this.dA *= -0.08F;
                            this.CT.AileronControl = this.dA;
                            this.CT.RudderControl = this.dA <= 0.0F ? -1.0F : 1.0F;
                            this.CT.ElevatorControl = 0.01111111F * Math.abs(this.Or.getKren());
                            if (this.mn_time > 2.0F || Math.abs(this.Or.getKren()) > this.direction) {
                                this.submaneuver++;
                                this.CT.RudderControl = World.Rnd().nextFloat(-0.5F, 0.5F);
                                this.direction = World.Rnd().nextFloat(-60.0F, -30.0F);
                                this.mn_time = 0.5F;
                            }
                            break;
                        case SUB_MAN1:
                            this.dA = this.Or.getKren() - 180.0F;
                            if (this.dA < -180.0F)
                                this.dA += 360.0F;
                            this.dA *= -0.04F;
                            this.CT.AileronControl = this.dA;
                            if (this.Or.getTangage() > this.direction + 5.0F && this.getOverload() < this.maxG && this.AOA < this.maxAOA) {
                                if (this.CT.ElevatorControl < 0.0F)
                                    this.CT.ElevatorControl = 0.0F;
                                this.CT.ElevatorControl += 1.0F * f;
                            } else {
                                if (this.CT.ElevatorControl > 0.0F)
                                    this.CT.ElevatorControl = 0.0F;
                                this.CT.ElevatorControl -= 1.0F * f;
                            }
                            if (this.mn_time > 2.0F && this.Or.getTangage() < this.direction + 20.0F)
                                this.submaneuver++;
                            if (this.danger != null) {
                                if (this.Skill >= 2 && this.Or.getTangage() < -30.0F && Vpl.x > 0.9986295104026794)
                                    this.submaneuver++;
                                if (this.Skill >= 3 && Math.abs(this.Or.getTangage()) > 145.0F && Math.abs(this.danger.Or.getTangage()) > 135.0F && this.dist < 400.0F)
                                    this.submaneuver++;
                            }
                            break;
                        case SUB_MAN2:
                            this.direction = World.Rnd().nextFloat(-60.0F, 60.0F);
                            this.setSpeedMode(6);
                            this.submaneuver++;
                            /* fall through */
                        case SUB_MAN3:
                            this.dA = this.Or.getKren() - this.direction;
                            this.CT.AileronControl = -0.04F * this.dA;
                            this.CT.RudderControl = this.dA <= 0.0F ? -1.0F : 1.0F;
                            this.CT.ElevatorControl = 0.5F;
                            if (Math.abs(this.dA) < 4.0F + 3.0F * this.Skill)
                                this.submaneuver++;
                            break;
                        case SUB_MAN4:
                            this.direction *= World.Rnd().nextFloat(0.5F, 1.0F);
                            this.setSpeedMode(6);
                            this.submaneuver++;
                            /* fall through */
                        case SUB_MAN5:
                            this.dA = this.Or.getKren() - this.direction;
                            this.CT.AileronControl = -0.04F * this.dA;
                            this.CT.RudderControl = -0.1F * this.getAOS();
                            this.dA = 1.0F;
                            if (this.getOverload() > this.maxG || this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA || this.Or.getTangage() > 40.0F)
                                this.CT.ElevatorControl -= 0.8F * f;
                            else
                                this.CT.ElevatorControl += 1.6F * f;
                            if (this.Or.getTangage() > 80.0F || this.mn_time > 4.0F || this.getSpeed() < this.Vmin * 1.5F)
                                this.pop();
                            if (this.danger != null && (Ve.z < -1.0 || this.dist > 600.0F || Vpl.x < 0.7880100011825562))
                                this.pop();
                            break;
                    }
                    if (this.Alt < -7.0 * this.Vwld.z)
                        this.pop();
                    break;
                case 33:
                    if (this.first) {
                        if (this.Alt < 1000.0F) {
                            this.pop();
                            break;
                        }
                        this.AP.setStabAll(false);
                        this.submaneuver = SUB_MAN0;
                        this.direction = ((World.Rnd().nextBoolean() ? 1.0F : -1.0F) * World.Rnd().nextFloat(107.0F, 160.0F));
                    }
                    this.maxAOA = this.Vwld.z <= 0.0 ? 24.0F : 14.0F;
                    switch (this.submaneuver) {
                        case SUB_MAN0:
                            if (Math.abs(this.Or.getKren()) < 45.0F)
                                this.CT.ElevatorControl = 0.005555556F * Math.abs(this.Or.getKren());
                            else if (this.getOverload() > this.maxG || this.AOA > this.maxAOA || this.CT.ElevatorControl > 1.0F)
                                this.CT.ElevatorControl -= 0.2F * f;
                            else
                                this.CT.ElevatorControl += 1.2F * f;
                            this.dA = this.Or.getKren() - this.direction;
                            this.CT.AileronControl = -0.04F * this.dA;
                            this.CT.RudderControl = -0.1F * this.getAOS();
                            if (Math.abs(this.dA) < 4.0F + 1.0F * this.Skill)
                                this.submaneuver++;
                            break;
                        case SUB_MAN1:
                            this.setSpeedMode(7);
                            this.smConstPower = 0.5F;
                            this.CT.AileronControl = 0.0F;
                            this.CT.RudderControl = -0.1F * this.getAOS();
                            this.dA = 1.0F;
                            if (this.getOverload() > this.maxG || this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA)
                                this.CT.ElevatorControl -= 0.2F * f;
                            else
                                this.CT.ElevatorControl += 1.2F * f;
                            if (this.Or.getTangage() < -60.0F)
                                this.submaneuver++;
                            break;
                        case SUB_MAN2:
                            if (this.Or.getTangage() > -45.0F) {
                                this.CT.AileronControl = -0.04F * this.Or.getKren();
                                this.setSpeedMode(9);
                                this.maxAOA = 7.0F;
                            }
                            this.CT.RudderControl = -0.1F * this.getAOS();
                            this.dA = 1.0F;
                            if (this.getOverload() > this.maxG || this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA)
                                this.CT.ElevatorControl -= 0.8F * f;
                            else
                                this.CT.ElevatorControl += 0.4F * f;
                            if (this.Or.getTangage() > this.AOA - 10.0F || this.Vwld.z > -1.0)
                                this.pop();
                            break;
                    }
                    if (this.Alt < -7.0 * this.Vwld.z)
                        this.pop();
                    break;
                case 34:
                    if (this.first) {
                        if (this.Alt < 500.0F) {
                            this.pop();
                            break;
                        }
                        this.direction = this.Or.getTangage();
                        this.setSpeedMode(9);
                    }
                    this.dA = this.Or.getKren() - (this.Or.getKren() <= 0.0F ? -35.0F : 35.0F);
                    this.CT.AileronControl = -0.04F * this.dA;
                    this.CT.RudderControl = this.Or.getKren() <= 0.0F ? -1.0F : 1.0F;
                    this.CT.ElevatorControl = -1.0F;
                    if (this.direction > this.Or.getTangage() + 45.0F || this.Or.getTangage() < -60.0F || this.mn_time > 4.0F)
                        this.pop();
                    break;
                case 36:
                case 37:
                    if (this.first) {
                        if (!this.isCapableOfACM())
                            this.pop();
                        if (this.getSpeed() < this.Vmax * 0.5F) {
                            this.pop();
                            break;
                        }
                        this.AP.setStabAll(false);
                        this.submaneuver = SUB_MAN0;
                        this.direction = World.Rnd().nextFloat(-30.0F, 80.0F);
                        this.setSpeedMode(9);
                    }
                    this.maxAOA = this.Vwld.z <= 0.0 ? 24.0F : 14.0F;
                    switch (this.submaneuver) {
                        case SUB_MAN0:
                            this.CT.AileronControl = -0.04F * this.Or.getKren();
                            this.CT.RudderControl = -0.1F * this.getAOS();
                            if (Math.abs(this.Or.getKren()) < 45.0F)
                                this.submaneuver++;
                            break;
                        case SUB_MAN1:
                            this.CT.AileronControl = 0.0F;
                            this.dA = 1.0F;
                            if (this.getOverload() > this.maxG || this.AOA > this.maxAOA || this.CT.ElevatorControl > this.dA)
                                this.CT.ElevatorControl -= 0.4F * f;
                            else
                                this.CT.ElevatorControl += 0.8F * f;
                            if (this.Or.getTangage() > this.direction)
                                this.submaneuver++;
                            if (this.getSpeed() < this.Vmin * 1.25F)
                                this.pop();
                            break;
                        case SUB_MAN2:
                            this.push(this.maneuver != 36 ? 35 : 7);
                            this.pop();
                    }
                    break;
                case 38:
                    if (this.first)
                        this.CT.RudderControl = this.Or.getKren() <= 0.0F ? -1.0F : 1.0F;
                    this.CT.AileronControl += -0.02F * this.Or.getKren();
                    if (this.CT.AileronControl > 0.1F)
                        this.CT.AileronControl = 0.1F;
                    if (this.CT.AileronControl < -0.1F)
                        this.CT.AileronControl = -0.1F;
                    this.dA = (this.getSpeedKMH() - 180.0F - this.Or.getTangage() * 10.0F - this.getVertSpeed() * 5.0F) * 0.0040F;
                    this.CT.ElevatorControl = this.dA;
                    if (this.mn_time > 3.5F)
                        this.pop();
                    break;
                case 39:
                    this.setSpeedMode(6);
                    this.CT.AileronControl = -0.04F * this.Or.getKren();
                    this.CT.ElevatorControl = -0.04F * (this.Or.getTangage() + 10.0F);
                    if (this.CT.RudderControl > 0.1F)
                        this.CT.RudderControl = 0.8F;
                    else if (this.CT.RudderControl < -0.1F)
                        this.CT.RudderControl = -0.8F;
                    else
                        this.CT.RudderControl = this.Or.getKren() <= 0.0F ? -1.0F : 1.0F;
                    if (this.getSpeed() > this.Vmax || this.mn_time > 7.0F)
                        this.pop();
                    break;
                case 40:
                    this.push(39);
                    this.push(57);
                    this.pop();
                    break;
                case 41:
                    this.push(13);
                    this.push(18);
                    this.pop();
                    break;
                case 42:
                    this.push(19);
                    this.push(57);
                    this.pop();
                    break;
                case 46:
                    if (this.target_ground == null || this.target_ground.pos == null) {
                        if (this.Group != null) {
                            this.dont_change_subm = true;
                            boolean flag5 = this.isBusy();
                            int j1 = this.Group.grTask;
//							AirGroup _tmp = Group;
                            this.Group.grTask = 4;
                            this.setBusy(false);
                            this.Group.setTaskAndManeuver(this.Group.numInGroup((Aircraft) this.actor));
                            this.setBusy(flag5);
                            this.Group.grTask = j1;
                        }
                        if (this.target_ground == null || this.target_ground.pos == null) {
                            this.AP.way.first();
                            Airport airport = Airport.nearest(this.AP.way.curr().getP(), this.actor.getArmy(), 7);
                            WayPoint waypoint;
                            if (airport != null)
                                waypoint = new WayPoint(airport.pos.getAbsPoint());
                            else
                                waypoint = new WayPoint(this.AP.way.first().getP());
                            waypoint.set(0.6F * this.Vmax);
                            waypoint.Action = 2;
                            this.AP.way.add(waypoint);
                            this.AP.way.last();
                            this.set_task(3);
                            this.clear_stack();
                            this.maneuver = 21;
                            this.set_maneuver(21);
                            break;
                        }
                    }
                    this.groundAttackKamikaze(this.target_ground, f);
                    break;
                case 43:
                case 47:
                case 50:
                case 51:
                case 52:
                case 79:
                case 80:
                case 81:
                    if (this.first && !this.isCapableOfACM()) {
                        this.bombsOut = true;
                        this.setReadyToReturn(true);
                        if (this.Group != null)
                            this.Group.waitGroup(this.Group.numInGroup((Aircraft) this.actor));
                        else {
                            this.AP.way.next();
                            this.set_task(3);
                            this.clear_stack();
                            this.set_maneuver(21);
                        }
                    } else {
                        if (this.target_ground == null || this.target_ground.pos == null || !Actor.isAlive(this.target_ground)) {
                            int i1 = this.maneuver;
                            if (this.Group != null) {
//								AirGroup _tmp1 = Group;
                                if (this.Group.grTask == 4) {
                                    this.dont_change_subm = true;
                                    boolean flag6 = this.isBusy();
                                    this.setBusy(false);
                                    this.Group.setTaskAndManeuver(this.Group.numInGroup((Aircraft) this.actor));
                                    this.setBusy(flag6);
                                }
                            }
                            if (this.target_ground == null || this.target_ground.pos == null || !Actor.isAlive(this.target_ground)) {
                                if (i1 == 50)
                                    this.bombsOut = true;
                                if (this.Group != null)
                                    this.Group.waitGroup(this.Group.numInGroup((Aircraft) this.actor));
                                else {
                                    this.AP.way.next();
                                    // TODO: GATTACK set_task(3);
                                    // TODO: GATTACK clear_stack();
                                    this.set_maneuver(21);
                                }
                                this.push(2);
                                this.pop();
                                break;
                            }
                        }
                        switch (this.maneuver) {
                            default:
                                break;
                            case 43:
                                this.groundAttack(this.target_ground, f);
                                if (this.mn_time > 120.0F)
                                    this.set_maneuver(0);
                                break;
                            case 50:
                                this.groundAttackDiveBomber(this.target_ground, f);
                                if (this.mn_time > 120.0F) {
                                    this.setSpeedMode(6);
                                    this.CT.BayDoorControl = 0.0F;
                                    this.CT.AirBrakeControl = 0.0F;
                                    this.CT.FlapsControl = 0.0F;
                                    this.pop();
                                    this.sub_Man_Count = 0;
                                }
                                break;
                            case 51:
                                this.groundAttackTorpedo(this.target_ground, f);
                                break;
                            case 81:
                                this.groundAttackTorpedoToKG(this.target_ground, f);
                                break;
                            case 52:
                                this.groundAttackCassette(this.target_ground, f);
                                break;
                            case 46:
                                this.groundAttackKamikaze(this.target_ground, f);
                                break;
                            case 47:
                                this.groundAttackTinyTim(this.target_ground, f);
                                break;
                            case 79:
                                this.groundAttackHS293(this.target_ground, f);
                                break;
                            case 80:
                                this.groundAttackFritzX(this.target_ground, f);
                        }
                    }
            }
            if (this.checkGround)
                this.doCheckGround(f);
            if (this.checkStrike && this.strikeEmer)
                this.doCheckStrike();
            this.strikeEmer = false;
            this.setSpeedControl(f);
            this.first = false;
            this.mn_time += f;
            if (this.frequentControl)
                this.AP.update(f);
            else
                this.AP.update(f * 4.0F);
            if (this.bBusy)
                this.wasBusy = true;
            else
                this.wasBusy = false;
            if (this.shotAtFriend > 0)
                this.shotAtFriend--;
        }
    }

    void OutCT(int i) {
        if (this.actor == Main3D.cur3D().viewActor()) {
            TextScr.output(i + 5, 45, ("Alt(MSL)  " + (int) this.Loc.z + "    " + (this.CT.BrakeControl <= 0.0F ? "" : "BRAKE")));
            TextScr.output(i + 5, 65, ("Alt(AGL)  " + (int) (this.Loc.z - Engine.land().HQ_Air(this.Loc.x, this.Loc.y))));
            int j = 0;
            TextScr.output(i + 225, 140, ("---ENGINES (" + this.EI.getNum() + ")---" + this.EI.engines[j].getStage()));
            TextScr.output(i + 245, 120, ("THTL " + (int) (100.0F * this.EI.engines[j].getControlThrottle()) + "%" + (this.EI.engines[j].getControlAfterburner() ? " (NITROS)" : "")));
            TextScr.output(i + 245, 100, ("PROP " + (int) (100.0F * this.EI.engines[j].getControlProp()) + "%" + (this.CT.getStepControlAuto() ? " (AUTO)" : "")));
            TextScr.output(i + 245, 80, ("MIX " + (int) (100.0F * this.EI.engines[j].getControlMix()) + "%"));
            TextScr.output(i + 245, 60, ("RAD " + (int) (100.0F * this.EI.engines[j].getControlRadiator()) + "%" + (this.CT.getRadiatorControlAuto() ? " (AUTO)" : "")));
            TextScr.output(i + 245, 40, ("SUPC " + this.EI.engines[j].getControlCompressor() + "x"));
            TextScr.output(245, 20, ("PropAoA :" + (int) (Math.toDegrees(this.EI.engines[j].getPropAoA()))));
            TextScr.output(i + 455, 120, ("Cyls/Cams " + this.EI.engines[j].getCylindersOperable() + "/" + this.EI.engines[0].getCylinders()));
            TextScr.output(i + 455, 100, ("Readyness " + (int) (100.0F * this.EI.engines[j].getReadyness()) + "%"));
            TextScr.output(i + 455, 80, ("PRM " + (int) ((int) (this.EI.engines[j].getRPM() * 0.02F) * 50.0F) + " rpm"));
            TextScr.output(i + 455, 60, ("Thrust " + (int) this.EI.engines[j].getEngineForce().x + " N"));
            TextScr.output(i + 455, 40, ("Fuel " + (int) (100.0F * this.M.fuel / this.M.maxFuel) + "% Nitro " + (int) (100.0F * this.M.nitro / this.M.maxNitro) + "%"));
            TextScr.output(i + 455, 20, ("MPrs " + (int) (1000.0F * this.EI.engines[j].getManifoldPressure()) + " mBar"));
            TextScr.output(i + 640, 140, "---Controls---");
            TextScr.output(i + 640, 120, ("A/C: " + (this.CT.bHasAileronControl ? "" : "AIL ") + (this.CT.bHasElevatorControl ? "" : "ELEV ") + (this.CT.bHasRudderControl ? "" : "RUD ") + (this.Gears.bIsHydroOperable ? "" : "GEAR ")));
            TextScr.output(i + 640, 100,
                    ("ENG: " + (this.EI.engines[j].isHasControlThrottle() ? "" : "THTL ") + (this.EI.engines[j].isHasControlProp() ? "" : "PROP ") + (this.EI.engines[j].isHasControlMix() ? "" : "MIX ")
                            + (this.EI.engines[j].isHasControlCompressor() ? "" : "SUPC ") + (this.EI.engines[j].isPropAngleDeviceOperational() ? "" : "GVRNR ")));
            TextScr.output(i + 640, 80, "PIL: (" + (int) (this.AS.getPilotHealth(0) * 100.0F) + "%)");
            TextScr.output(i + 5, 105, "V   " + (int) this.getSpeedKMH());
            TextScr.output(i + 5, 125, "AOA " + ((int) (this.getAOA() * 1000.0F) / 1000.0F));
            TextScr.output(i + 5, 145, "AOS " + ((int) (this.getAOS() * 1000.0F) / 1000.0F));
            TextScr.output(i + 5, 165, "Kr  " + (int) this.Or.getKren());
            TextScr.output(i + 5, 185, "Ta  " + (int) this.Or.getTangage());
            TextScr.output(i + 250, 185, ("way.speed  " + this.AP.way.curr().getV() * 3.6F + "way.z " + this.AP.way.curr().z() + "  mn_time = " + this.mn_time));
            TextScr.output(i + 5, 205,
                    ("<" + this.actor.name() + ">: " + ManString.tname(this.task) + ":" + ManString.name(this.maneuver) + " , WP=" + this.AP.way.Cur() + "(" + (this.AP.way.size() - 1) + ")-" + ManString.wpname(this.AP.way.curr().Action)));
            TextScr.output(
                    i + 7,
                    225,
                    ("======= " + ManString.name(this.program[0]) + "  Sub = " + this.submaneuver + " follOffs.x = " + this.followOffset.x + "  " + (((AutopilotAI) this.AP).bWayPoint ? "Stab WPoint " : "")
                            + (((AutopilotAI) this.AP).bStabAltitude ? "Stab ALT " : "") + (((AutopilotAI) this.AP).bStabDirection ? "Stab DIR " : "") + (((AutopilotAI) this.AP).bStabSpeed ? "Stab SPD " : "   ")
                            + (((Pilot) ((Aircraft) this.actor).FM).isDumb() ? "DUMB " : " ") + (((Pilot) ((Aircraft) this.actor).FM).Gears.lgear ? "L " : " ") + (((Pilot) ((Aircraft) this.actor).FM).Gears.rgear ? "R " : " ") + (((Pilot) ((Aircraft) this.actor).FM).TaxiMode ? "TaxiMode"
                            : "")));
            TextScr.output(i + 7, 245, " ====== " + ManString.name(this.program[1]));
            TextScr.output(i + 7, 265, "  ===== " + ManString.name(this.program[2]));
            TextScr.output(i + 7, 285, "   ==== " + ManString.name(this.program[3]));
            TextScr.output(i + 7, 305, "    === " + ManString.name(this.program[4]));
            TextScr.output(i + 7, 325, "     == " + ManString.name(this.program[5]));
            TextScr.output(i + 7, 345, ("      = " + ManString.name(this.program[6]) + "  " + (this.target != null ? "TARGET  " : "") + (this.target_ground != null ? "GROUND  " : "") + (this.danger != null ? "DANGER  " : "")));
            if (this.target != null && Actor.isValid(this.target.actor))
                TextScr.output(i + 1, 365, (" AT: (" + this.target.actor.name() + ") " + (this.target.actor instanceof Aircraft ? "" : this.target.actor.getClass().getName())));
            if (this.target_ground != null && Actor.isValid(this.target_ground))
                TextScr.output(i + 1, 385, (" GT: (" + this.target_ground.name() + ") ..." + this.target_ground.getClass().getName()));
            TextScr.output(400, 500, "+");
            TextScr.output(400, 400, "|");
            TextScr.output((int) (400.0F + 200.0F * this.CT.AileronControl), (int) (500.0F - 200.0F * this.CT.ElevatorControl), "+");
            TextScr.output((int) (400.0F + 200.0F * this.CT.RudderControl), 400, "|");
            TextScr.output(250, 780, ("followOffset  " + this.followOffset.x + "  " + this.followOffset.y + "  " + this.followOffset.z + "  "));
            if (this.Group != null && this.Group.enemies != null)
                TextScr.output(250, 760, ("Enemies   " + AirGroupList.length(this.Group.enemies[0])));
            TextScr.output(700, 780, "speedMode   " + this.speedMode);
            if (this.Group != null)
                TextScr.output(700, 760, "Group task  " + this.Group.grTaskName());
            if (this.AP.way.isLandingOnShip())
                TextScr.output(5, 460, "Landing On Carrier");
            TextScr.output(700, 740, "gattackCounter  " + this.gattackCounter);
            TextScr.output(5, 360, "silence = " + this.silence);
        }
    }

    private void groundAttackGuns(Actor actor, float f) {
        if (this.submaneuver == SUB_MAN0 && this.sub_Man_Count == 0 && this.CT.Weapons[1] != null) {
            float f1 = ((GunGeneric) this.CT.Weapons[1][0]).bulletSpeed();
            if (f1 > 0.01F)
                this.bullTime = 1.0F / (f1 + this.getSpeed());
        }
        this.maxAOA = 15.0F;
        this.minElevCoeff = 20.0F;
        switch (this.submaneuver) {
            case SUB_MAN0: {
                this.setCheckGround(true);
                this.rocketsDelay = 0;
                if (this.sub_Man_Count == 0) {
                    this.Vtarg.set(actor.pos.getAbsPoint());
                    actor.getSpeed(tmpV3d);
                    tmpV3f.x = (float) tmpV3d.x;
                    tmpV3f.y = (float) tmpV3d.y;
                    tmpV3f.z = (float) tmpV3d.z;
                    tmpV3f.z = 0.0;
                    if (tmpV3f.length() < 9.999999747378752E-5) {
                        tmpV3f.sub(this.Vtarg, this.Loc);
                        tmpV3f.z = 0.0;
                    }
                    tmpV3f.normalize();
                    this.Vtarg.x -= tmpV3f.x * 1500.0;
                    this.Vtarg.y -= tmpV3f.y * 1500.0;
                    this.Vtarg.z += 400.0;
                    this.constVtarg.set(this.Vtarg);
                    Ve.sub(this.constVtarg, this.Loc);
                    Ve.normalize();
                    this.Vxy.cross(Ve, tmpV3f);
                    Ve.sub(tmpV3f);
                    this.Vtarg.z += 100.0;
                    if (this.Vxy.z > 0.0) {
                        this.Vtarg.x += Ve.y * 1000.0;
                        this.Vtarg.y -= Ve.x * 1000.0;
                    } else {
                        this.Vtarg.x -= Ve.y * 1000.0;
                        this.Vtarg.y += Ve.x * 1000.0;
                    }
                    this.constVtarg1.set(this.Vtarg);
                }
                Ve.set(this.constVtarg1);
                Ve.sub(this.Loc);
                float f2 = (float) Ve.length();
                this.Or.transformInv(Ve);
                if (Ve.x < 0.0) {
                    this.push(0);
                    this.push(8);
                    this.pop();
                    this.dontSwitch = true;
                } else {
                    Ve.normalize();
                    this.setSpeedMode(4);
                    this.smConstSpeed = 100.0F;
                    this.farTurnToDirection();
                    this.sub_Man_Count++;
                    if (f2 < 300.0F) {
                        this.submaneuver++;
                        this.gattackCounter++;
                        this.sub_Man_Count = 0;
                    }
                }
                break;
            }
            case SUB_MAN1: {
                Ve.set(this.constVtarg);
                Ve.sub(this.Loc);
                float f3 = (float) Ve.length();
                this.Or.transformInv(Ve);
                Ve.normalize();
                this.setSpeedMode(4);
                this.smConstSpeed = 100.0F;
                this.farTurnToDirection();
                this.sub_Man_Count++;
                if (f3 < 300.0F) {
                    this.submaneuver++;
                    this.gattackCounter++;
                    this.sub_Man_Count = 0;
                }
                break;
            }
            case SUB_MAN2: {
                if (this.rocketsDelay > 0)
                    this.rocketsDelay--;
                if (this.sub_Man_Count > 100)
                    this.setCheckGround(false);
                P.set(actor.pos.getAbsPoint());
                P.z += 4.0;
                Engine.land();
                if (Landscape.rayHitHQ(this.Loc, P, P)) {
                    this.push(0);
                    this.push(38);
                    this.pop();
                    this.gattackCounter--;
                    if (this.gattackCounter < 0)
                        this.gattackCounter = 0;
                }
                Ve.sub(actor.pos.getAbsPoint(), this.Loc);
                this.Vtarg.set(Ve);
                float f4 = (float) Ve.length();
                actor.getSpeed(tmpV3d);
                tmpV3f.x = (float) tmpV3d.x;
                tmpV3f.y = (float) tmpV3d.y;
                tmpV3f.z = (float) tmpV3d.z;
                tmpV3f.sub(this.Vwld);
                tmpV3f.scale(f4 * this.bullTime * 0.3333F * this.Skill);
                Ve.add(tmpV3f);
                float f5 = 0.3F * (f4 - 1000.0F);
                if (f5 < 0.0F)
                    f5 = 0.0F;
                if (f5 > 300.0F)
                    f5 = 300.0F;
                f5 += f4 * this.getAOA() * 0.0050F;
                Ve.z += f5 + (World.cur().rnd.nextFloat(-3.0F, 3.0F) * (3 - this.Skill));
                this.Or.transformInv(Ve);
                if (f4 < 800.0F && (this.shotAtFriend <= 0 || this.distToFriend > f4) && Math.abs(Ve.y) < 15.0 && Math.abs(Ve.z) < 10.0) {
                    if (f4 < 550.0F)
                        this.CT.WeaponControl[0] = true;
                    if (f4 < 450.0F)
                        this.CT.WeaponControl[1] = true;
                    if (this.CT.Weapons[2] != null && this.CT.Weapons[2][0] != null && (this.CT.Weapons[2][this.CT.Weapons[2].length - 1].countBullets() != 0) && this.rocketsDelay < 1 && f4 < 500.0F) {
                        this.CT.WeaponControl[2] = true;
                        Voice.speakAttackByRockets((Aircraft) this.actor);
                        this.rocketsDelay += 30;
                    }
                }
                if (this.sub_Man_Count > 200 && Ve.x < 200.0 || this.Alt < 40.0F) {
                    if (this.Leader == null || !Actor.isAlive(this.Leader.actor))
                        Voice.speakEndGattack((Aircraft) this.actor);
                    this.rocketsDelay = 0;
                    this.push(0);
                    this.push(55);
                    this.push(10);
                    this.pop();
                    this.dontSwitch = true;
                } else {
                    Ve.normalize();
                    this.attackTurnToDirection(f4, f, 4.0F + this.Skill * 2.0F);
                    this.setSpeedMode(4);
                    this.smConstSpeed = 100.0F;
                    break;
                }
                break;
            }
            default:
                this.submaneuver = SUB_MAN0;
                this.sub_Man_Count = 0;
        }
    }

    private void groundAttack(Actor actor, float f) {
        this.setSpeedMode(4);
        this.smConstSpeed = 120.0F;
        float f3 = this.Vwld.z <= 0.0 ? 4.0F : 3.0F;
        boolean flag = false;
        boolean flag1 = false;
        if (this.CT.Weapons[3] != null && this.CT.Weapons[3][0] != null && this.CT.Weapons[3][0].countBullets() != 0) {
            flag = true;
            if (this.CT.Weapons[3][0] instanceof ParaTorpedoGun)
                flag1 = true;
        } else if (!(this.actor instanceof TypeStormovik) && !(this.actor instanceof TypeFighter) && !(this.actor instanceof TypeDiveBomber)) {
            this.set_maneuver(0);
            return;
        }
        Ve.set(actor.pos.getAbsPoint());
        if (flag1) {
            if (this.CT.Weapons[3][0] instanceof BombGunTorp45_36AV_A)
                Ve.z = this.Loc.z - 100.0 + World.Rnd().nextDouble() * 50.0;
            else
                Ve.z = this.Loc.z - 200.0 + World.Rnd().nextDouble() * 50.0;
        }
        float f4 = (float) this.Loc.z - (float) Ve.z;
        if (f4 < 0.0F)
            f4 = 0.0F;
        float f5 = (float) Math.sqrt(2.0F * f4 * 0.1019F) + 0.0017F * f4;
        actor.getSpeed(tmpV3d);
        if (actor instanceof Aircraft && tmpV3d.length() > 20.0) {
            this.target = ((Aircraft) actor).FM;
            this.set_task(6);
            this.clear_stack();
            this.set_maneuver(27);
            this.dontSwitch = true;
        } else {
            float f6 = 0.5F;
            if (flag)
                f6 = (f5 + 5.0F) * 0.33333F;
            this.Vtarg.x = (float) tmpV3d.x * f6 * this.Skill;
            this.Vtarg.y = (float) tmpV3d.y * f6 * this.Skill;
            this.Vtarg.z = (float) tmpV3d.z * f6 * this.Skill;
            Ve.add(this.Vtarg);
            Ve.sub(this.Loc);
            if (flag)
                this.addWindCorrection();
            Ve.add(0.0, 0.0, -0.5F + World.Rnd().nextFloat(-2.0F, 0.8F));
            Vf.set(Ve);
            float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
            if (flag) {
                float f7 = this.getSpeed() * f5 + 500.0F;
                if (f1 > f7)
                    Ve.z += 200.0;
                else
                    Ve.z = 0.0;
            }
            this.Vtarg.set(Ve);
            this.Vtarg.normalize();
            this.Or.transformInv(Ve);
            if (!flag)
                this.groundAttackGuns(actor, f);
            // TODO: Altered by GATTACK mod
            // -------------------------------------------
            else if ((super.actor instanceof TypeFighter) || (super.actor instanceof TypeStormovik)) {
                this.passCounter = 0;
                this.bombsOutCounter = 129;
                this.groundAttackShallowDive(actor, f);
            }
            // -------------------------------------------
            else {
                Ve.normalize();
                Vpl.set(this.Vwld);
                Vpl.normalize();
                this.CT.BayDoorControl = 1.0F;
                if (f1 + 4.0F * f5 < this.getSpeed() * f5 && Ve.x > 0.0 && Vpl.dot(this.Vtarg) > 0.9800000190734863) {
                    if (!this.bombsOut) {
                        this.bombsOut = true;
                        if (this.CT.Weapons[3] != null && this.CT.Weapons[3][0] != null && this.CT.Weapons[3][0].countBullets() != 0 && !(this.CT.Weapons[3][0] instanceof BombGunPara))
                            Voice.speakAttackByBombs((Aircraft) this.actor);
                    }
                    this.push(0);
                    this.push(55);
                    this.push(48);
                    this.pop();
                    Voice.speakEndGattack((Aircraft) this.actor);
                    this.CT.BayDoorControl = 0.0F;
                }
                if (Ve.x < 0.0) {
                    this.push(0);
                    this.push(55);
                    this.push(10);
                    this.pop();
                }
                if (Math.abs(Ve.y) > 0.10000000149011612)
                    this.CT.AileronControl = (-(float) Math.atan2(Ve.y, Ve.z) - 0.016F * this.Or.getKren());
                else
                    this.CT.AileronControl = (-(float) Math.atan2(Ve.y, Ve.x) - 0.016F * this.Or.getKren());
                if (Math.abs(Ve.y) > 0.0010000000474974513)
                    this.CT.RudderControl = -98.0F * (float) Math.atan2(Ve.y, Ve.x);
                else
                    this.CT.RudderControl = 0.0F;
                if (this.CT.RudderControl * this.W.z > 0.0)
                    this.W.z = 0.0;
                else
                    this.W.z *= 1.0399999618530273;
                float f2 = (float) Math.atan2(Ve.z, Ve.x);
                if (Math.abs(Ve.y) < 0.0020000000949949026 && Math.abs(Ve.z) < 0.0020000000949949026)
                    f2 = 0.0F;
                if (Ve.x < 0.0)
                    f2 = 1.0F;
                else {
                    if (f2 * this.W.y > 0.0)
                        this.W.y = 0.0;
                    if (f2 < 0.0F) {
                        if (this.getOverload() < 0.1F)
                            f2 = 0.0F;
                        if (this.CT.ElevatorControl > 0.0F)
                            this.CT.ElevatorControl = 0.0F;
                    } else if (this.CT.ElevatorControl < 0.0F)
                        this.CT.ElevatorControl = 0.0F;
                }
                if (this.getOverload() > this.maxG || this.AOA > f3 || this.CT.ElevatorControl > f2)
                    this.CT.ElevatorControl -= 0.2F * f;
                else
                    this.CT.ElevatorControl += 0.2F * f;
            }
        }
    }

    private void groundAttackKamikaze(Actor actor, float f) {
        if (this.submaneuver == SUB_MAN0 && this.sub_Man_Count == 0 && this.CT.Weapons[1] != null) {
            float f1 = ((GunGeneric) this.CT.Weapons[1][0]).bulletSpeed();
            if (f1 > 0.01F)
                this.bullTime = 1.0F / f1;
        }
        this.maxAOA = 15.0F;
        this.minElevCoeff = 20.0F;
        switch (this.submaneuver) {
            case SUB_MAN0: {
                this.setCheckGround(true);
                this.rocketsDelay = 0;
                if (this.sub_Man_Count == 0) {
                    this.Vtarg.set(actor.pos.getAbsPoint());
                    tmpV3f.set(this.Vwld);
                    tmpV3f.x += World.Rnd().nextFloat(-100.0F, 100.0F);
                    tmpV3f.y += World.Rnd().nextFloat(-100.0F, 100.0F);
                    tmpV3f.z = 0.0;
                    if (tmpV3f.length() < 9.999999747378752E-5) {
                        tmpV3f.sub(this.Vtarg, this.Loc);
                        tmpV3f.z = 0.0;
                    }
                    tmpV3f.normalize();
                    this.Vtarg.x -= tmpV3f.x * 1500.0;
                    this.Vtarg.y -= tmpV3f.y * 1500.0;
                    this.Vtarg.z += 400.0;
                    this.constVtarg.set(this.Vtarg);
                    Ve.sub(this.constVtarg, this.Loc);
                    Ve.normalize();
                    this.Vxy.cross(Ve, tmpV3f);
                    Ve.sub(tmpV3f);
                    this.Vtarg.z += 100.0;
                    if (this.Vxy.z > 0.0) {
                        this.Vtarg.x += Ve.y * 1000.0;
                        this.Vtarg.y -= Ve.x * 1000.0;
                    } else {
                        this.Vtarg.x -= Ve.y * 1000.0;
                        this.Vtarg.y += Ve.x * 1000.0;
                    }
                    this.constVtarg1.set(this.Vtarg);
                }
                Ve.set(this.constVtarg1);
                Ve.sub(this.Loc);
                float f2 = (float) Ve.length();
                this.Or.transformInv(Ve);
                if (Ve.x < 0.0) {
                    this.push(0);
                    this.push(8);
                    this.pop();
                    this.dontSwitch = true;
                } else {
                    Ve.normalize();
                    this.setSpeedMode(6);
                    this.farTurnToDirection();
                    this.sub_Man_Count++;
                    if (f2 < 300.0F) {
                        this.submaneuver++;
                        this.gattackCounter++;
                        this.sub_Man_Count = 0;
                    }
                    if (this.sub_Man_Count > 1000)
                        this.sub_Man_Count = 0;
                }
                break;
            }
            case SUB_MAN1: {
                this.setCheckGround(true);
                Ve.set(this.constVtarg);
                Ve.sub(this.Loc);
                float f3 = (float) Ve.length();
                this.Or.transformInv(Ve);
                Ve.normalize();
                this.setSpeedMode(6);
                this.farTurnToDirection();
                this.sub_Man_Count++;
                if (f3 < 300.0F) {
                    this.submaneuver++;
                    this.gattackCounter++;
                    this.sub_Man_Count = 0;
                }
                if (this.sub_Man_Count > 700) {
                    this.submaneuver++;
                    this.sub_Man_Count = 0;
                }
                break;
            }
            case SUB_MAN2: {
                this.setCheckGround(false);
                if (this.rocketsDelay > 0)
                    this.rocketsDelay--;
                if (this.sub_Man_Count > 100)
                    this.setCheckGround(false);
                Ve.set(actor.pos.getAbsPoint());
                Ve.sub(this.Loc);
                this.Vtarg.set(Ve);
                float f4 = (float) Ve.length();
                if (this.actor instanceof MXY_7) {
                    Ve.z += 0.01 * f4;
                    this.Vtarg.z += 0.01 * f4;
                }
                actor.getSpeed(tmpV3d);
                tmpV3f.x = (float) tmpV3d.x;
                tmpV3f.y = (float) tmpV3d.y;
                tmpV3f.z = (float) tmpV3d.z;
                tmpV3f.sub(this.Vwld);
                tmpV3f.scale(f4 * this.bullTime * 0.3333F * this.Skill);
                Ve.add(tmpV3f);
                float f5 = 0.3F * (f4 - 1000.0F);
                if (f5 < 0.0F)
                    f5 = 0.0F;
                if (f5 > 300.0F)
                    f5 = 300.0F;
                Ve.z += f5 + (World.cur().rnd.nextFloat(-3.0F, 3.0F) * (3 - this.Skill));
                this.Or.transformInv(Ve);
                if (f4 < 50.0F && Math.abs(Ve.y) < 40.0 && Math.abs(Ve.z) < 30.0) {
                    this.CT.WeaponControl[0] = true;
                    this.CT.WeaponControl[1] = true;
                    this.CT.WeaponControl[2] = true;
                    this.CT.WeaponControl[3] = true;
                }
                if (Ve.x < -50.0) {
                    this.rocketsDelay = 0;
                    this.push(0);
                    this.push(55);
                    this.push(10);
                    this.pop();
                    this.dontSwitch = true;
                } else {
                    Ve.normalize();
                    this.attackTurnToDirection(f4, f, 4.0F + this.Skill * 2.0F);
                    this.setSpeedMode(4);
                    this.smConstSpeed = 130.0F;
                    break;
                }
                break;
            }
            default:
                this.submaneuver = SUB_MAN0;
                this.sub_Man_Count = 0;
        }
    }

    private void groundAttackTinyTim(Actor actor, float f) {
        this.maxG = 5.0F;
        this.maxAOA = 8.0F;
        this.setSpeedMode(4);
        this.smConstSpeed = 120.0F;
        this.minElevCoeff = 20.0F;
        switch (this.submaneuver) {
            case SUB_MAN0: {
                if (this.sub_Man_Count == 0) {
                    this.Vtarg.set(actor.pos.getAbsPoint());
                    actor.getSpeed(tmpV3d);
                    if (tmpV3d.length() < 0.5)
                        tmpV3d.sub(this.Vtarg, this.Loc);
                    tmpV3d.normalize();
                    this.Vtarg.x -= tmpV3d.x * 3000.0;
                    this.Vtarg.y -= tmpV3d.y * 3000.0;
                    this.Vtarg.z += 500.0;
                }
                Ve.sub(this.Vtarg, this.Loc);
                double d = Ve.length();
                this.Or.transformInv(Ve);
                this.sub_Man_Count++;
                if (d < 1000.0) {
                    this.submaneuver++;
                    this.sub_Man_Count = 0;
                }
                Ve.normalize();
                this.farTurnToDirection();
                break;
            }
            case SUB_MAN1: {
                this.Vtarg.set(actor.pos.getAbsPoint());
                this.Vtarg.z += 80.0;
                Ve.sub(this.Vtarg, this.Loc);
                double d1 = Ve.length();
                this.Or.transformInv(Ve);
                this.sub_Man_Count++;
                if (d1 < 1500.0) {
                    if (Math.abs(Ve.y) < 40.0 && Math.abs(Ve.z) < 30.0)
                        this.CT.WeaponControl[2] = true;
                    this.push(0);
                    this.push(10);
                    this.push(48);
                    this.pop();
                    this.dontSwitch = true;
                }
                if (d1 < 500.0 && Ve.x < 0.0) {
                    this.push(0);
                    this.push(10);
                    this.pop();
                }
                Ve.normalize();
                if (Ve.x < 0.800000011920929)
                    this.turnToDirection(f);
                else
                    this.attackTurnToDirection((float) d1, f, 2.0F + this.Skill * 1.5F);
                break;
            }
            default:
                this.submaneuver = SUB_MAN0;
                this.sub_Man_Count = 0;
        }
    }

    private void groundAttackHS293(Actor actor, float f) {
        this.maxG = 5.0F;
        this.maxAOA = 8.0F;
        this.setSpeedMode(4);
        this.smConstSpeed = 120.0F;
        this.minElevCoeff = 20.0F;
        switch (this.submaneuver) {
            case SUB_MAN0: {
                if (this.sub_Man_Count == 0) {
                    this.Vtarg.set(actor.pos.getAbsPoint());
                    actor.getSpeed(tmpV3d);
                    if (tmpV3d.length() < 0.5)
                        tmpV3d.sub(this.Vtarg, this.Loc);
                    tmpV3d.normalize();
                    this.Vtarg.x -= tmpV3d.x * 3000.0;
                    this.Vtarg.y -= tmpV3d.y * 3000.0;
                    this.Vtarg.z += 500.0;
                }
                Ve.sub(this.Vtarg, this.Loc);
                double d = Ve.length();
                this.Or.transformInv(Ve);
                this.sub_Man_Count++;
                if (d < 10000.0) {
                    this.submaneuver++;
                    this.sub_Man_Count = 0;
                }
                Ve.normalize();
                this.farTurnToDirection();
                break;
            }
            case SUB_MAN1: {
                this.Vtarg.set(actor.pos.getAbsPoint());
                this.Vtarg.z += 2000.0;
                Ve.sub(this.Vtarg, this.Loc);
                double d1 = Ve.angle(this.Vwld);
                Ve.z = 0.0;
                double d2 = Ve.length();
                this.Or.transformInv(Ve);
                this.sub_Man_Count++;
                TypeGuidedBombCarrier typeguidedbombcarrier = (TypeGuidedBombCarrier) this.actor;
                if (d2 > 2000.0 && d2 < 6500.0 && d1 < 0.9 && !typeguidedbombcarrier.typeGuidedBombCgetIsGuiding()) {
                    this.CT.WeaponControl[3] = true;
                    this.push(0);
                    this.push(10);
                    this.pop();
                    this.dontSwitch = true;
                }
                if (d2 < 500.0 && Ve.x < 0.0) {
                    this.push(0);
                    this.push(10);
                    this.pop();
                }
                Ve.normalize();
                if (Ve.x < 99999.80000001192)
                    this.turnToDirection(f);
                else
                    this.attackTurnToDirection((float) d2, f, 2.0F + this.Skill * 1.5F);
                break;
            }
            default:
                this.submaneuver = SUB_MAN0;
                this.sub_Man_Count = 0;
        }
    }

    private void groundAttackFritzX(Actor actor, float f) {
        this.maxG = 5.0F;
        this.maxAOA = 8.0F;
        this.setSpeedMode(4);
        this.smConstSpeed = 140.0F;
        this.minElevCoeff = 20.0F;
        switch (this.submaneuver) {
            case SUB_MAN0: {
                if (this.sub_Man_Count == 0) {
                    this.Vtarg.set(actor.pos.getAbsPoint());
                    actor.getSpeed(tmpV3d);
                    if (tmpV3d.length() < 0.5)
                        tmpV3d.sub(this.Vtarg, this.Loc);
                    tmpV3d.normalize();
                    this.Vtarg.x -= tmpV3d.x * 3000.0;
                    this.Vtarg.y -= tmpV3d.y * 3000.0;
                    this.Vtarg.z += 500.0;
                }
                Ve.sub(this.Vtarg, this.Loc);
                double d = Ve.length();
                this.Or.transformInv(Ve);
                this.sub_Man_Count++;
                if (d < 15000.0) {
                    this.submaneuver++;
                    this.sub_Man_Count = 0;
                }
                Ve.normalize();
                this.farTurnToDirection();
                break;
            }
            case SUB_MAN1: {
                this.Vtarg.set(actor.pos.getAbsPoint());
                this.Vtarg.z += 2000.0;
                Ve.sub(this.Vtarg, this.Loc);
                double d1 = Ve.angle(this.Vwld);
                Ve.z = 0.0;
                double d2 = Ve.length();
                this.Or.transformInv(Ve);
                this.sub_Man_Count++;
                TypeGuidedBombCarrier typeguidedbombcarrier = (TypeGuidedBombCarrier) this.actor;
                if (d2 < 4000.0 && d1 < 0.9 && (d2 < 2000.0 || d1 > 0.4) && !typeguidedbombcarrier.typeGuidedBombCgetIsGuiding()) {
                    this.CT.WeaponControl[3] = true;
                    this.setSpeedMode(5);
                    this.push(0);
                    this.push(10);
                    this.pop();
                    this.dontSwitch = true;
                }
                if (d2 < 500.0 && Ve.x < 0.0) {
                    this.push(0);
                    this.push(10);
                    this.pop();
                }
                Ve.normalize();
                if (Ve.x < 99999.80000001192)
                    this.turnToDirection(f);
                else
                    this.attackTurnToDirection((float) d2, f, 2.0F + this.Skill * 1.5F);
                break;
            }
            default:
                this.submaneuver = SUB_MAN0;
                this.sub_Man_Count = 0;
        }
    }

    private void groundAttackShallowDive(Actor actor, float f) {
        this.maxAOA = 10.0F;
        if (!this.hasBombs()) {
            this.set_maneuver(0);
            this.wingman(true);
        } else {
            if (this.first)
                this.RandomVal = 50.0F - World.cur().rnd.nextFloat(0.0F, 100.0F);
            this.setSpeedMode(4);
            this.smConstSpeed = 120.0F;
            Ve.set(actor.pos.getAbsPoint());
            Ve.sub(this.Loc);
            this.addWindCorrection();
            float f1 = (float) -Ve.z;
            if (f1 < 0.0F)
                f1 = 0.0F;
            Ve.z += 250.0;
            float f2 = ((float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y) + this.RandomVal * (3 - this.Skill));
            if (Ve.z < -0.1F * f2)
                Ve.z = -0.1F * f2;
            if (this.Alt + Ve.z < 250.0)
                Ve.z = 250.0F - this.Alt;
            if (this.Alt < 50.0F) {
                this.push(10);
                this.pop();
            }
            Vf.set(Ve);
            this.CT.BayDoorControl = 1.0F;
            float f3 = (float) this.Vwld.z * 0.1019F;
            f3 += (float) Math.sqrt(f3 * f3 + 2.0F * f1 * 0.1019F);
            float f4 = (float) Math.sqrt(this.Vwld.x * this.Vwld.x + this.Vwld.y * this.Vwld.y);
            float f5 = f4 * f3 + 10.0F;
            actor.getSpeed(tmpV3d);
            tmpV3d.scale(f3 * 0.35 * this.Skill);
            Ve.x += (float) tmpV3d.x;
            Ve.y += (float) tmpV3d.y;
            Ve.z += (float) tmpV3d.z;
            // TODO: Modified by GATTACK MOD
            // -----------------------------------------
            if ((f5 >= f2) && (this.passCounter == 0)) {
                this.bombsOut = true;
                this.bombsOutCounter = 129;
                Voice.speakAttackByBombs((Aircraft) this.actor);
                this.setSpeedMode(6);
                this.CT.BayDoorControl = 0.0F;
                this.pop();
                this.sub_Man_Count = 0;
                this.passCounter++;
            } else if (this.passCounter >= 5) {
                this.passCounter = 0;
            }
            // -----------------------------------------
            this.Or.transformInv(Ve);
            Ve.normalize();
            this.turnToDirection(f);
        }
    }

    private void groundAttackDiveBomber(Actor actor, float f) {
        this.maxG = 5.0F;
        this.maxAOA = 10.0F;
        this.setSpeedMode(6);
        this.maxAOA = 4.0F;
        this.minElevCoeff = 20.0F;
        if (this.CT.Weapons[3] == null || this.CT.getWeaponCount(3) == 0) {
            if (this.AP.way.curr().Action == 3)
                this.AP.way.next();
            this.set_maneuver(0);
            this.wingman(true);
        } else {
            if (this.Alt < 350.0F) {
                this.bombsOut = true;
                Voice.speakAttackByBombs((Aircraft) this.actor);
                this.setSpeedMode(6);
                this.CT.BayDoorControl = 0.0F;
                this.CT.AirBrakeControl = 0.0F;
                this.AP.way.next();
                this.push(0);
                this.push(8);
                this.push(2);
                this.pop();
                this.sub_Man_Count = 0;
            }
            Ve.set(actor.pos.getAbsPoint());
            Ve.sub(this.Loc);
            float f5 = (float) -Ve.z;
            float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
            if (f1 > 1000.0F || this.submaneuver == SUB_MAN3 && this.sub_Man_Count > 100) {
                this.Vtarg.set(actor.pos.getAbsPoint());
                actor.getSpeed(tmpV3d);
                float f6 = 0.0F;
                switch (this.submaneuver) {
                    case SUB_MAN0:
                        f6 = f1 / 40.0F + 4.0F + this.Alt / 48.0F;
                        /* fall through */
                    case SUB_MAN1:
                        f6 = ((f1 - this.man1Dist) / (float) this.Vwld.length() + 4.0F + this.Alt / 48.0F);
                        /* fall through */
                    case SUB_MAN2:
                        f6 = this.Alt / 60.0F;
                        /* fall through */
                    case SUB_MAN3:
                        f6 = this.Alt / 120.0F;
                        /* fall through */
                    default:
                        f6 *= 0.33333F;
                        this.Vtarg.x += (float) tmpV3d.x * f6 * this.Skill;
                        this.Vtarg.y += (float) tmpV3d.y * f6 * this.Skill;
                        this.Vtarg.z += (float) tmpV3d.z * f6 * this.Skill;
                }
            }
            Ve.set(this.Vtarg);
            Ve.sub(this.Loc);
            float f4 = (float) -Ve.z;
            if (f4 < 0.0F)
                f4 = 0.0F;
            Ve.add(this.Vxy);
            f5 = (float) -Ve.z;
            f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
            if (this.submaneuver < SUB_MAN2)
                Ve.z = 0.0;
            Vf.set(Ve);
            Ve.normalize();
            Vpl.set(this.Vwld);
            Vpl.normalize();
            switch (this.submaneuver) {
                default:
                    break;
                case SUB_MAN0: {
                    this.push();
                    this.pop();
                    if (f5 < 1200.0F)
                        this.man1Dist = 400.0F;
                    else if (f5 > 4500.0F)
                        this.man1Dist = 50.0F;
                    else
                        this.man1Dist = 50.0F + 350.0F * (4500.0F - f5) / 3300.0F;
                    float f7 = 0.01F * f5;
                    if (f7 < 10.0F)
                        f7 = 10.0F;
                    this.Vxy.set(World.Rnd().nextFloat(-10.0F, 10.0F), World.Rnd().nextFloat(-10.0F, 10.0F), World.Rnd().nextFloat(5.0F, f7));
                    this.Vxy.scale(4.0F - this.Skill);
                    float f8 = 2.0E-5F * f5 * f5;
                    if (f8 < 60.0F)
                        f8 = 60.0F;
                    if (f8 > 350.0F)
                        f8 = 350.0F;
                    this.Vxy.z += f8;
                    this.submaneuver++;
                    break;
                }
                case SUB_MAN1:
                    this.setSpeedMode(4);
                    this.smConstSpeed = 110.0F;
                    if (!(f1 >= this.man1Dist)) {
                        this.CT.AirBrakeControl = 1.0F;
                        if (this.actor instanceof TypeFighter)
                            this.CT.FlapsControl = 1.0F;
                        this.push();
                        this.push(6);
                        this.safe_pop();
                        this.submaneuver++;
                    }
                    break;
                case SUB_MAN2: {
                    this.setSpeedMode(4);
                    this.smConstSpeed = 110.0F;
                    this.sub_Man_Count++;
                    this.CT.AirBrakeControl = 1.0F;
                    if (this.actor instanceof TypeFighter)
                        this.CT.FlapsControl = 1.0F;
                    float f3 = this.Or.getKren();
                    if (this.Or.getTangage() > -90.0F) {
                        f3 -= 180.0F;
                        if (f3 < -180.0F)
                            f3 += 360.0F;
                    }
                    this.CT.AileronControl = (float) (-0.04F * f3 - 0.5 * this.getW().x);
                    if (this.getOverload() < 4.0F)
                        this.CT.ElevatorControl += 0.3F * f;
                    else
                        this.CT.ElevatorControl -= 0.3F * f;
                    if (this.sub_Man_Count > 30 && this.Or.getTangage() < -90.0F || this.sub_Man_Count > 150) {
                        this.sub_Man_Count = 0;
                        this.submaneuver++;
                    }
                    break;
                }
                case SUB_MAN3:
                    this.CT.AirBrakeControl = 1.0F;
                    if (this.actor instanceof TypeFighter)
                        this.CT.FlapsControl = 1.0F;
                    this.CT.BayDoorControl = 1.0F;
                    this.setSpeedMode(4);
                    this.smConstSpeed = 110.0F;
                    this.sub_Man_Count++;
                    if (this.sub_Man_Count > 80) {
                        float f9 = (float) this.Vwld.z * 0.1019F;
                        f9 = (f9 + (float) Math.sqrt(f9 * f9 + 2.0F * f4 * 0.1019F) + 3.5E-4F * f4);
                        float f10 = (float) Math.sqrt(this.Vwld.x * this.Vwld.x + this.Vwld.y * this.Vwld.y);
                        float f11 = f10 * f9;
                        float f12 = 0.2F * (f1 - f11);
                        this.Vxy.z += f12;
                        if (this.Vxy.z > 0.7F * f4)
                            this.Vxy.z = 0.7F * f4;
                    }
                    if ((this.sub_Man_Count > 100 && this.Alt < 1000.0F && Vpl.dot(Ve) > 0.9900000095367432) || this.Alt < 600.0F) {
                        this.bombsOut = true;
                        Voice.speakAttackByBombs((Aircraft) this.actor);
                        this.CT.BayDoorControl = 0.0F;
                        this.CT.AirBrakeControl = 0.0F;
                        this.AP.way.next();
                        this.push(0);
                        this.push(8);
                        this.push(2);
                        this.pop();
                    }
            }
            this.Or.transformInv(Ve);
            Ve.normalize();
            if (this.submaneuver == SUB_MAN3)
                this.attackTurnToDirection(1000.0F, f, 30.0F);
            else if (this.submaneuver != SUB_MAN2)
                this.turnToDirection(f);
        }
    }

    private void groundAttackTorpedo(Actor actor, float f) {
        float f4 = 50.0F;
        this.maxG = 5.0F;
        this.maxAOA = 8.0F;
        float f5 = 0.0F;
        this.setSpeedMode(4);
        Class class1 = null;
        if (this.CT.Weapons[3][0] instanceof TorpedoGun) {
            TorpedoGun torpedogun = (TorpedoGun) this.CT.Weapons[3][0];
            class1 = (Class) Property.value(torpedogun.getClass(), "bulletClass", null);
        }
        this.smConstSpeed = 100.0F;
        if (class1 != null) {
            this.smConstSpeed = Property.floatValue(class1, "dropSpeed", 100.0F) / 3.7F;
            f4 = Property.floatValue(class1, "dropAltitude", 50.0F) + 10.0F;
        }
        if (this.actor instanceof Swordfish) {
            this.setSpeedMode(9);
            f4 *= 0.75F;
        }
        this.minElevCoeff = 20.0F;
        Ve.set(actor.pos.getAbsPoint());
        Ve.sub(this.Loc);
        if (this.first) {
            this.Vtarg.set(actor.pos.getAbsPoint());
            this.submaneuver = SUB_MAN0;
        }
        if (this.submaneuver == SUB_MAN1 && this.sub_Man_Count == 0) {
            tmpV3f.set(actor.pos.getAbsPoint());
            tmpV3f.sub(this.Vtarg);
            actor.getSpeed(tmpV3d);
            if (tmpV3f.length() > 9.999999747378752E-5)
                tmpV3f.normalize();
            this.Vxy.set(tmpV3f.y * 3000.0, -tmpV3f.x * 3000.0, 0.0);
            this.direc = this.Vxy.dot(Ve) > 0.0;
            if (this.direc)
                this.Vxy.scale(-1.0);
            this.Vtarg.add(this.Vxy);
            this.Vtarg.x += tmpV3d.x * 80.0;
            this.Vtarg.y += tmpV3d.y * 80.0;
            this.Vtarg.z = 80.0;
            this.TargDevV.set((World.cur().rnd.nextFloat(-16.0F, 16.0F) * (3.5 - this.Skill)), (World.cur().rnd.nextFloat(-16.0F, 16.0F) * (3.5 - this.Skill)), 0.0);
        }
        if (this.submaneuver == SUB_MAN2) {
            this.Vtarg.set(actor.pos.getAbsPoint());
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
            double d1 = Math.sqrt(0.204 * this.Loc.z);
            double d2 = 1.0 * d1 * this.getSpeed();
            double d3 = (f11 + f5 - d2) / f6;
            this.Vtarg.x += (float) (tmpV3d.x * d3);
            this.Vtarg.y += (float) (tmpV3d.y * d3);
            this.Vtarg.z = f4;
            if (this.Loc.z < 30.0)
                this.Vtarg.z += 3.0 * (30.0 - this.Loc.z);
            this.Vtarg.add(this.TargDevV);
        }
        Ve.set(this.Vtarg);
        Ve.sub(this.Loc);
        float f2 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y + Ve.z * Ve.z);
        Vf.set(Ve);
        Vpl.set(this.Vwld);
        Vpl.normalize();
        if (this.Alt < f4 - 5.0F) {
            if (this.Vwld.z < 0.0)
                this.Vwld.z += (f4 - this.Alt) * 0.25F;
            if (this.Alt < 8.0F)
                this.set_maneuver(2);
            if (this.Alt < 20.0F && f2 < 75.0F)
                this.set_maneuver(2);
        } else if (this.Alt > f4 + 5.0F && this.submaneuver == SUB_MAN1 && this.Vwld.z > 0.0)
            this.Vwld.z--;
        switch (this.submaneuver) {
            default:
                break;
            case SUB_MAN0:
                this.sub_Man_Count++;
                if (this.sub_Man_Count > 60) {
                    this.submaneuver++;
                    this.sub_Man_Count = 0;
                }
                this.addWindCorrection();
                break;
            case SUB_MAN1:
                this.setSpeedMode(4);
                if (this.actor instanceof Swordfish)
                    this.setSpeedMode(9);
                this.sub_Man_Count++;
                if (f2 < 1200.0F || f2 < 2000.0F && ZutiSupportMethods.isStaticActor(actor)) {
                    this.submaneuver++;
                    this.sub_Man_Count = 0;
                }
                this.addWindCorrection();
                break;
            case SUB_MAN2:
                this.setSpeedMode(4);
                if (f2 < 800.0F + f5) {
                    if (this.actor instanceof TypeHasToKG && ((TypeHasToKG) this.actor).isSalvo()) {
                        int i = 0;
                        float f9 = actor.collisionR() * 1.8F;
                        i = (int) Math.toDegrees(Math.atan(f9 / 800.0F));
                        i += World.Rnd().nextInt(-2, 2);
                        if (i < 3)
                            i = 3;
                        this.AS.setSpreadAngle(i);
                    }
                    this.CT.WeaponControl[3] = true;
                    this.setSpeedMode(6);
                    this.AP.way.next();
                    this.submaneuver++;
                    this.sub_Man_Count = 0;
                } else if (ZutiSupportMethods.isStaticActor(actor)) {
                    float f7 = Property.floatValue(class1, "velocity", 20.0F);
                    float f10 = Property.floatValue(class1, "traveltime", 100.0F);
                    float f12 = f7 * f10 - 150.0F;
                    if (f2 < f12 && World.land().isWater(this.actor.pos.getAbsPoint().x, this.actor.pos.getAbsPoint().y)) {
                        this.CT.WeaponControl[3] = true;
                        this.setSpeedMode(6);
                        this.AP.way.next();
                        this.submaneuver++;
                        this.sub_Man_Count = 0;
                    }
                }
                break;
            case SUB_MAN3:
                this.setSpeedMode(9);
                this.sub_Man_Count++;
                if (this.sub_Man_Count > 150) {
                    this.task = 3;
                    this.push(0);
                    this.push(8);
                    this.pop();
                    this.submaneuver = SUB_MAN0;
                    this.sub_Man_Count = 0;
                }
        }
        this.Or.transformInv(Ve);
        if (this.submaneuver == SUB_MAN3) {
            if (this.sub_Man_Count < 20)
                Ve.set(1.0, 0.09000000357627869, 0.029999999329447746);
            else
                Ve.set(1.0, 0.09000000357627869, 0.009999999776482582);
            if (!this.direc)
                Ve.y *= -1.0;
        }
        Ve.normalize();
        this.turnToDirection(f);
    }

    private void groundAttackTorpedoToKG(Actor actor, float f) {
        float f2 = 50.0F;
        this.maxG = 5.0F;
        this.maxAOA = 8.0F;
        this.setSpeedMode(4);
        Class class1 = null;
        if (this.CT.Weapons[3][0] instanceof TorpedoGun) {
            TorpedoGun torpedogun = (TorpedoGun) this.CT.Weapons[3][0];
            class1 = (Class) Property.value(torpedogun.getClass(), "bulletClass", null);
        }
        this.smConstSpeed = 100.0F;
        if (class1 != null) {
            this.smConstSpeed = Property.floatValue(class1, "dropSpeed", 100.0F) / 3.7F;
            f2 = Property.floatValue(class1, "dropAltitude", 50.0F) + 10.0F;
        }
        this.minElevCoeff = 20.0F;
        Ve.set(actor.pos.getAbsPoint());
        Ve.sub(this.Loc);
        if (this.first) {
            this.Vtarg.set(actor.pos.getAbsPoint());
            this.submaneuver = SUB_MAN0;
        }
        if (this.submaneuver == SUB_MAN1 && this.sub_Man_Count == 0) {
            tmpV3f.set(actor.pos.getAbsPoint());
            tmpV3f.sub(this.Vtarg);
            if (tmpV3f.length() > 9.999999747378752E-5)
                tmpV3f.normalize();
            this.Vxy.set(tmpV3f.y * 3000.0, -tmpV3f.x * 3000.0, 0.0);
            this.direc = this.Vxy.dot(Ve) > 0.0;
            if (this.direc)
                this.Vxy.scale(-1.0);
            this.Vtarg.add(this.Vxy);
            this.Vtarg.z = 80.0;
        }
        if (this.submaneuver == SUB_MAN2) {
            this.Vtarg.set(actor.pos.getAbsPoint());
            this.Vtarg.z = f2;
            if (this.Loc.z < 30.0)
                this.Vtarg.z += 3.0 * (30.0 - this.Loc.z);
        }
        Ve.set(this.Vtarg);
        Ve.sub(this.Loc);
        float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y + Ve.z * Ve.z);
        Vf.set(Ve);
        Vpl.set(this.Vwld);
        Vpl.normalize();
        if (this.Alt < f2 - 5.0F) {
            if (this.Vwld.z < 0.0)
                this.Vwld.z += (f2 - this.Alt) * 0.25F;
            if (this.Alt < 8.0F)
                this.set_maneuver(2);
            if (this.Alt < 20.0F && f1 < 75.0F)
                this.set_maneuver(2);
        } else if (this.Alt > f2 + 5.0F && this.submaneuver == SUB_MAN1 && this.Vwld.z > 0.0)
            this.Vwld.z--;
        switch (this.submaneuver) {
            default:
                break;
            case SUB_MAN0:
                this.sub_Man_Count++;
                if (this.sub_Man_Count > 60) {
                    this.submaneuver++;
                    this.sub_Man_Count = 0;
                }
                this.addWindCorrection();
                break;
            case SUB_MAN1:
                this.setSpeedMode(4);
                this.sub_Man_Count++;
                if (f1 < 4000.0F) {
                    this.submaneuver++;
                    this.sub_Man_Count = 0;
                }
                this.addWindCorrection();
                break;
            case SUB_MAN2: {
                this.setSpeedMode(4);
                double d = Ve.angle(this.Vwld);
                float f3 = 180.0F - (this.Or.getYaw() - actor.pos.getAbsOrient().getYaw());
                if (f3 < -180.0F)
                    f3 += 360.0F;
                else if (f3 > 180.0F)
                    f3 -= 360.0F;
                float f4 = Property.floatValue(class1, "velocity", 20.0F);
                float f5 = Property.floatValue(class1, "traveltime", 100.0F);
                float f6 = f4 * f5 - 10.0F;
                if (f6 > 2700.0F)
                    f6 = 2700.0F;
                double d1 = (Math.abs(f3) - 90.0F) * 8.0F;
                if (d1 < 0.0)
                    d1 = 0.0;
                if (this.Skill == 2)
                    d1 += 100.0;
                if (f1 < f6 - d1 && f1 > 1800.0F && d < 0.2) {
                    actor.getSpeed(tmpV3d);
                    float f7 = 1.0F;
                    if (this.Skill == 2)
                        f7 = World.Rnd().nextFloat(0.8F, 1.2F);
                    else if (this.Skill == 3)
                        f7 = World.Rnd().nextFloat(0.9F, 1.1F);
                    f7 += 0.1;
                    ToKGUtils.setTorpedoGyroAngle(this, f3, ((float) (1.95 * tmpV3d.length()) * f7));
                    if (((TypeHasToKG) this.actor).isSalvo()) {
                        int i = 0;
                        float f8 = actor.collisionR() * 1.8F;
                        i = (int) Math.toDegrees(Math.atan(f8 / (f6 - d1)));
                        i += World.Rnd().nextInt(-2, 2);
                        if (i < 1)
                            i = 1;
                        this.AS.setSpreadAngle(i);
                    }
                    this.CT.WeaponControl[3] = true;
                    this.setSpeedMode(6);
                    this.AP.way.next();
                    this.push(15);
                    this.submaneuver++;
                    this.sub_Man_Count = 0;
                } else if (f1 < 1500.0F) {
                    ToKGUtils.setTorpedoGyroAngle(this, 0.0F, 0.0F);
                    this.set_maneuver(51);
                }
                break;
            }
            case SUB_MAN3: {
                this.setSpeedMode(9);
                this.push(15);
                this.pop();
                this.task = 61;
                this.sub_Man_Count++;
                boolean flag = false;
                for (int j = 0; j < this.CT.Weapons[3].length; j++) {
                    if (this.CT.Weapons[3][j] != null && !(this.CT.Weapons[3][j] instanceof BombGunNull) && this.CT.Weapons[3][j].countBullets() != 0)
                        flag = true;
                }
                if (this.sub_Man_Count > 800 || !flag) {
                    this.task = 3;
                    this.push(21);
                    this.push(8);
                    this.pop();
                    this.submaneuver = SUB_MAN0;
                    this.sub_Man_Count = 0;
                }
            }
        }
        this.Or.transformInv(Ve);
        if (this.submaneuver == SUB_MAN3) {
            if (this.sub_Man_Count < 20)
                Ve.set(1.0, 0.09000000357627869, 0.029999999329447746);
            else
                Ve.set(1.0, 0.09000000357627869, 0.009999999776482582);
            if (!this.direc)
                Ve.y *= -1.0;
        }
        Ve.normalize();
        this.turnToDirection(f);
    }

    private void groundAttackCassette(Actor actor, float f) {
        this.maxG = 5.0F;
        this.maxAOA = 8.0F;
        this.setSpeedMode(4);
        this.smConstSpeed = 120.0F;
        this.minElevCoeff = 20.0F;
        Ve.set(actor.pos.getAbsPoint());
        Ve.sub(this.Loc);
        float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
        if (this.submaneuver == SUB_MAN3 && this.sub_Man_Count > 0 && this.sub_Man_Count < 45 && f1 > 200.0F) {
            tmpV3f.set(this.Vxy);
            float f4 = (float) tmpV3f.dot(Ve);
            tmpV3f.scale(-f4);
            tmpV3f.add(Ve);
            float f7 = (float) tmpV3f.length();
            float f6;
            if (f7 > 150.0F)
                f6 = 7.5F / f7;
            else
                f6 = 0.05F;
            tmpV3f.scale(f6);
            tmpV3f.z = 0.0;
            this.Vwld.add(tmpV3f);
        }
        if (f1 <= 200.0F)
            this.sub_Man_Count = 50;
        if (this.first) {
            this.Vtarg.set(actor.pos.getAbsPoint());
            this.submaneuver = SUB_MAN0;
        }
        if (this.submaneuver == SUB_MAN1 && this.sub_Man_Count == 0) {
            tmpV3f.set(actor.pos.getAbsPoint());
            actor.getSpeed(tmpV3d);
            if (tmpV3d.length() < 0.5)
                tmpV3d.set(Ve);
            tmpV3d.normalize();
            this.Vxy.set((float) tmpV3d.x, (float) tmpV3d.y, (float) tmpV3d.z);
            this.Vtarg.x -= tmpV3d.x * 3000.0;
            this.Vtarg.y -= tmpV3d.y * 3000.0;
            this.Vtarg.z += 250.0;
        }
        if (this.submaneuver == SUB_MAN2 && this.sub_Man_Count == 0) {
            this.Vtarg.set(actor.pos.getAbsPoint());
            this.Vtarg.x -= this.Vxy.x * 1000.0;
            this.Vtarg.y -= this.Vxy.y * 1000.0;
            this.Vtarg.z += 50.0;
        }
        if (this.submaneuver == SUB_MAN3 && this.sub_Man_Count == 0) {
            this.checkGround = false;
            this.Vtarg.set(actor.pos.getAbsPoint());
            this.Vtarg.x += this.Vxy.x * 1000.0;
            this.Vtarg.y += this.Vxy.y * 1000.0;
            this.Vtarg.z += 50.0;
        }
        Ve.set(this.Vtarg);
        Ve.sub(this.Loc);
        this.addWindCorrection();
        float f2 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y + Ve.z * Ve.z);
        Vf.set(Ve);
        this.Or.transformInv(Ve);
        Ve.normalize();
        Vpl.set(this.Vwld);
        Vpl.normalize();
        if (this.Alt < 10.0F) {
            this.push(0);
            this.push(2);
            this.pop();
        }
        switch (this.submaneuver) {
            case SUB_MAN0:
                this.setSpeedMode(9);
                this.sub_Man_Count++;
                if (this.sub_Man_Count > 60) {
                    this.submaneuver++;
                    this.sub_Man_Count = 0;
                }
                break;
            case SUB_MAN1:
                this.setSpeedMode(9);
                this.sub_Man_Count++;
                if (f2 < 1000.0F) {
                    this.submaneuver++;
                    this.sub_Man_Count = 0;
                }
                break;
            case SUB_MAN2:
                this.setSpeedMode(6);
                this.sub_Man_Count++;
                if (f2 < 155.0F) {
                    this.submaneuver++;
                    this.sub_Man_Count = 0;
                }
                if (this.sub_Man_Count > 320) {
                    this.push(0);
                    this.push(10);
                    this.pop();
                }
                break;
            case SUB_MAN3: {
                this.setSpeedMode(6);
                this.sub_Man_Count++;
                this.Vwld.z *= 0.800000011920929;
                this.Or.transformInv(this.Vwld);
                this.Vwld.y *= 0.8999999761581421;
                this.Or.transform(this.Vwld);
                float f5 = this.sub_Man_Count;
                if (f5 < 100.0F)
                    f5 = 100.0F;
                if (this.Alt > 45.0F)
                    this.Vwld.z -= 0.0020F * (this.Alt - 45.0F) * f5;
                else
                    this.Vwld.z -= 0.0050F * (this.Alt - 45.0F) * f5;
                if (this.Alt < 0.0F)
                    this.Alt = 0.0F;
                if (f2 < 1080.0F + (this.getSpeed() * (float) Math.sqrt(2.0F * this.Alt / 9.81F)))
                    this.bombsOut = true;
                if (Ve.x < 0.0 || f2 < 350.0F || this.sub_Man_Count > 160) {
                    this.push(0);
                    this.push(10);
                    this.push(10);
                    this.pop();
                }
                break;
            }
        }
        if (this.submaneuver == SUB_MAN0)
            Ve.set(1.0, 0.0, 0.0);
        this.turnToDirection(f);
    }

    public void goodFighterVsFighter(float f) {
        Ve.sub(this.target.Loc, this.Loc);
        float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
        this.Or.transformInv(Ve);
        float f2 = (float) Ve.length();
        float f4 = 1.0F / f2;
        this.Vtarg.set(Ve);
        this.Vtarg.scale(f4);
        float f5 = (this.Energy - this.target.Energy) * 0.1019F;
        tmpV3f.sub(this.target.Vwld, this.Vwld);
        if (this.sub_Man_Count == 0) {
            float f6 = 0.0F;
            if (this.CT.Weapons[1] != null && this.CT.Weapons[1][0].countBullets() > 0)
                f6 = ((GunGeneric) this.CT.Weapons[1][0]).bulletSpeed();
            else if (this.CT.Weapons[0] != null)
                f6 = ((GunGeneric) this.CT.Weapons[0][0]).bulletSpeed();
            if (f6 > 0.01F)
                this.bullTime = 1.0F / f6;
            this.submanDelay = 0;
        }
        if (f1 < 1500.0F) {
            float f7 = 0.0F;
            float f9 = 0.0F;
            if (this.Vtarg.x > -0.20000000298023224) {
                f7 = 3.0F * ((float) this.Vtarg.x + 0.2F);
                this.Vxy.set(tmpV3f);
                this.Vxy.scale(1.0);
                this.Or.transformInv(this.Vxy);
                this.Vxy.add(Ve);
                this.Vxy.normalize();
                f9 = 10.0F * (float) (this.Vxy.x - this.Vtarg.x);
                if (f9 < -1.0F)
                    f9 = -1.0F;
                if (f9 > 1.0F)
                    f9 = 1.0F;
            } else
                f7 = 3.0F * ((float) this.Vtarg.x + 0.2F);
            if (this.submaneuver == SUB_MAN4 && this.Vtarg.x < 0.6000000238418579 && f2 < 300.0) {
                this.submaneuver = SUB_MAN1;
                this.submanDelay = 30;
            }
            if (this.submaneuver != SUB_MAN4 && f5 > 300.0 && this.Vtarg.x > 0.75) {
                this.submaneuver = SUB_MAN4;
                this.submanDelay = 240;
            }
            float f11 = 0.0015F * f5 + 6.0E-4F * f1 + f7 + 0.5F * f9;
            if (f11 > 0.9F && this.submanDelay == 0) {
                if (this.Vtarg.x > 0.5 || f1 * 2.0 < f2) {
                    this.submaneuver = SUB_MAN4;
                    this.submanDelay = 240;
                } else {
                    this.submaneuver = SUB_MAN3;
                    this.submanDelay = 120;
                }
            } else if (f5 > 800.0F && this.submaneuver == SUB_MAN0 || f5 > 1000.0F) {
                Ve.set(0.0, 0.0, 800.0);
                if (this.submanDelay == 0) {
                    this.submaneuver = SUB_MAN0;
                    this.submanDelay = 30;
                }
            } else if (f2 > 450.0F && this.submaneuver == SUB_MAN2 || f2 > 600.0F) {
                if (this.submanDelay == 0) {
                    this.submaneuver = SUB_MAN2;
                    this.submanDelay = 60;
                }
            } else if (this.submanDelay == 0) {
                this.submaneuver = SUB_MAN1;
                this.submanDelay = 30;
            }
        } else if (f1 < 3000.0F) {
            if (this.Vtarg.x < 0.5) {
                if (this.submanDelay == 0) {
                    this.submaneuver = SUB_MAN3;
                    this.submanDelay = 120;
                }
            } else if (f5 > 600.0F && this.submaneuver == SUB_MAN0 || f5 > 800.0F) {
                Ve.set(0.0, 0.0, 800.0);
                if (this.submanDelay == 0) {
                    this.submaneuver = SUB_MAN0;
                    this.submanDelay = 120;
                }
            } else if (f5 > -200.0F && this.submaneuver >= SUB_MAN4 || f5 > -100.0F) {
                if (this.submanDelay == 0) {
                    this.submaneuver = SUB_MAN4;
                    this.submanDelay = 120;
                }
            } else {
                Ve.set(0.0, 0.0, this.Loc.z);
                if (this.submanDelay == 0) {
                    this.submaneuver = SUB_MAN0;
                    this.submanDelay = 120;
                }
            }
        } else {
            Ve.set(0.0, 0.0, 1000.0);
            if (this.submanDelay == 0) {
                this.submaneuver = SUB_MAN0;
                this.submanDelay = 180;
            }
        }
        switch (this.submaneuver) {
            case SUB_MAN0: {
                this.target.Or.transform(Ve);
                Ve.add(this.target.Loc);
                Ve.sub(this.Loc);
                tmpV3f.set(Ve);
                tmpV3f.z = 0.0;
                float f3 = (float) tmpV3f.length();
                tmpV3f.normalize();
                this.Vtarg.set(this.target.Vwld);
                this.Vtarg.z = 0.0;
                float f8 = (float) tmpV3f.dot(this.Vtarg);
                float f10 = this.getSpeed() - f8;
                if (f10 < 10.0F)
                    f10 = 10.0F;
                float f12 = f3 / f10;
                if (f12 < 0.0F)
                    f12 = 0.0F;
                tmpV3f.scale(this.Vtarg, f12);
                Ve.add(tmpV3f);
                this.Or.transformInv(Ve);
                Ve.normalize();
                this.setSpeedMode(9);
                this.farTurnToDirection();
                this.sub_Man_Count++;
                break;
            }
            case SUB_MAN1:
                this.setSpeedMode(9);
                this.CT.AileronControl = -0.04F * this.Or.getKren();
                this.CT.ElevatorControl = -0.04F * (this.Or.getTangage() + 5.0F);
                this.CT.RudderControl = 0.0F;
                break;
            case SUB_MAN2: {
                this.setSpeedMode(9);
                tmpOr.setYPR(this.Or.getYaw(), 0.0F, 0.0F);
                float f13 = 6.0F;
                if (this.Or.getKren() > 0.0F)
                    Ve.set(100.0, -f13, 10.0);
                else
                    Ve.set(100.0, f13, 10.0);
                tmpOr.transform(Ve);
                this.Or.transformInv(Ve);
                Ve.normalize();
                this.farTurnToDirection();
                break;
            }
            case SUB_MAN3:
                this.minElevCoeff = 20.0F;
                this.setSpeedMode(9);
                tmpOr.setYPR(this.Or.getYaw(), 0.0F, 0.0F);
                Ve.sub(this.target.Loc, this.Loc);
                Ve.z = 0.0;
                Ve.normalize();
                Ve.z = 0.4;
                this.Or.transformInv(Ve);
                Ve.normalize();
                this.attackTurnToDirection(1000.0F, f, 15.0F);
                break;
            case SUB_MAN4:
                this.minElevCoeff = 20.0F;
                this.boomAttack(f);
                this.setSpeedMode(9);
                break;
            default:
                this.minElevCoeff = 20.0F;
                this.fighterVsFighter(f);
        }
        if (this.submanDelay > 0)
            this.submanDelay--;
    }

    public void bNZFighterVsFighter(float f) {
        Ve.sub(this.target.Loc, this.Loc);
        this.Or.transformInv(Ve);
        this.dist = (float) Ve.length();
        float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
        float f2 = 1.0F / this.dist;
        this.Vtarg.set(Ve);
        this.Vtarg.scale(f2);
        float f3 = (this.Energy - this.target.Energy) * 0.1019F;
        tmpV3f.sub(this.target.Vwld, this.Vwld);
        if (this.sub_Man_Count == 0) {
            float f4 = 0.0F;
            if (this.CT.Weapons[1] != null && this.CT.Weapons[1][0].countBullets() > 0)
                f4 = ((GunGeneric) this.CT.Weapons[1][0]).bulletSpeed();
            else if (this.CT.Weapons[0] != null)
                f4 = ((GunGeneric) this.CT.Weapons[0][0]).bulletSpeed();
            if (f4 > 0.01F)
                this.bullTime = 1.0F / f4;
            this.submanDelay = 0;
        }
        if (f1 < 1500.0F) {
            float f5 = 0.0F;
            float f7 = 0.0F;
            if (this.Vtarg.x > -0.20000000298023224) {
                f5 = 3.0F * ((float) this.Vtarg.x + 0.2F);
                this.Vxy.set(tmpV3f);
                this.Vxy.scale(1.0);
                this.Or.transformInv(this.Vxy);
                this.Vxy.add(Ve);
                this.Vxy.normalize();
                f7 = 20.0F * (float) (this.Vxy.x - this.Vtarg.x);
                if (f7 < -1.0F)
                    f7 = -1.0F;
                if (f7 > 1.0F)
                    f7 = 1.0F;
            }
            float f9 = f3 * 0.0015F + f1 * 6.0E-4F + f5 + f7;
            if (f9 > 0.8F && this.submaneuver >= SUB_MAN3 || f9 > 1.2F) {
                if (tmpV3f.length() < 100.0) {
                    if (this.submanDelay == 0) {
                        this.submaneuver = SUB_MAN4;
                        this.submanDelay = 120;
                    }
                } else if (this.submanDelay == 0) {
                    this.submaneuver = SUB_MAN3;
                    this.submanDelay = 120;
                }
            } else if (f3 > 800.0F && this.submaneuver == SUB_MAN0 || f3 > 1000.0F) {
                Ve.set(0.0, 0.0, 800.0);
                if (this.submanDelay == 0) {
                    this.submaneuver = SUB_MAN0;
                    this.submanDelay = 30;
                }
            } else if (this.dist > 450.0F && this.submaneuver == SUB_MAN2 || this.dist > 600.0F) {
                if (this.submanDelay == 0) {
                    this.submaneuver = SUB_MAN2;
                    this.submanDelay = 60;
                }
            } else if (this.submanDelay == 0) {
                this.submaneuver = SUB_MAN1;
                this.submanDelay = 30;
            }
        } else if (f1 < 3000.0F) {
            if (f3 > 600.0F && this.submaneuver == SUB_MAN0 || f3 > 800.0F) {
                Ve.set(0.0, 0.0, 800.0);
                if (this.submanDelay == 0) {
                    this.submaneuver = SUB_MAN0;
                    this.submanDelay = 120;
                }
            } else if (f3 > -200.0F && this.submaneuver >= SUB_MAN3 || f3 > -100.0F) {
                if (this.submanDelay == 0) {
                    this.submaneuver = SUB_MAN3;
                    this.submanDelay = 120;
                }
            } else {
                Ve.set(0.0, 0.0, this.Loc.z);
                if (this.submanDelay == 0) {
                    this.submaneuver = SUB_MAN0;
                    this.submanDelay = 120;
                }
            }
        } else {
            Ve.set(0.0, 0.0, 1000.0);
            if (this.submanDelay == 0) {
                this.submaneuver = SUB_MAN0;
                this.submanDelay = 180;
            }
        }
        switch (this.submaneuver) {
            case SUB_MAN0: {
                this.target.Or.transform(Ve);
                Ve.add(this.target.Loc);
                Ve.sub(this.Loc);
                tmpV3f.set(Ve);
                tmpV3f.z = 0.0;
                this.dist = (float) tmpV3f.length();
                tmpV3f.normalize();
                this.Vtarg.set(this.target.Vwld);
                this.Vtarg.z = 0.0;
                float f6 = (float) tmpV3f.dot(this.Vtarg);
                float f8 = this.getSpeed() - f6;
                if (f8 < 10.0F)
                    f8 = 10.0F;
                float f10 = this.dist / f8;
                if (f10 < 0.0F)
                    f10 = 0.0F;
                tmpV3f.scale(this.Vtarg, f10);
                Ve.add(tmpV3f);
                this.Or.transformInv(Ve);
                Ve.normalize();
                this.setSpeedMode(9);
                this.farTurnToDirection();
                this.sub_Man_Count++;
                break;
            }
            case SUB_MAN1:
                this.setSpeedMode(9);
                this.CT.AileronControl = -0.04F * this.Or.getKren();
                this.CT.ElevatorControl = -0.04F * (this.Or.getTangage() + 5.0F);
                this.CT.RudderControl = 0.0F;
                break;
            case SUB_MAN2:
                this.setSpeedMode(9);
                tmpOr.setYPR(this.Or.getYaw(), 0.0F, 0.0F);
                if (this.Or.getKren() > 0.0F)
                    Ve.set(100.0, -6.0, 10.0);
                else
                    Ve.set(100.0, 6.0, 10.0);
                tmpOr.transform(Ve);
                this.Or.transformInv(Ve);
                Ve.normalize();
                this.farTurnToDirection();
                break;
            case SUB_MAN3:
                this.minElevCoeff = 20.0F;
                this.fighterVsFighter(f);
                this.setSpeedMode(6);
                break;
            default:
                this.minElevCoeff = 20.0F;
                this.fighterVsFighter(f);
        }
        if (this.submanDelay > 0)
            this.submanDelay--;
    }

    public void setBomberAttackType(int i) {
        float f;
        if (Actor.isValid(this.target.actor))
            f = this.target.actor.collisionR();
        else
            f = 15.0F;
        this.setRandomTargDeviation(0.8F);
        switch (i) {
            case 0:
                this.ApproachV.set(-300.0F + World.Rnd().nextFloat(-100.0F, 100.0F), 0.0, 600.0F + World.Rnd().nextFloat(-100.0F, 100.0F));
                this.TargV.set(0.0, 0.0, 0.0);
                this.ApproachR = 150.0F;
                this.TargY = 2.1F * f;
                this.TargZ = 3.9F * f;
                this.TargYS = 0.43F * f;
                this.TargZS = 0.43F * f;
                break;
            case 1:
                this.ApproachV.set(-300.0F + World.Rnd().nextFloat(-100.0F, 100.0F), 0.0, 600.0F + World.Rnd().nextFloat(-100.0F, 100.0F));
                this.TargV.set(this.target.EI.engines[World.Rnd().nextInt(0, this.target.EI.getNum() - 1)].getEnginePos());
                this.TargV.x--;
                this.ApproachR = 150.0F;
                this.TargY = 2.1F * f;
                this.TargZ = 3.9F * f;
                this.TargYS = 0.43F * f;
                this.TargZS = 0.43F * f;
                break;
            case 2:
                this.ApproachV.set(-600.0F + World.Rnd().nextFloat(-100.0F, 100.0F), 0.0, 600.0F + World.Rnd().nextFloat(-100.0F, 100.0F));
                this.TargV.set(this.target.EI.engines[World.Rnd().nextInt(0, this.target.EI.getNum() - 1)].getEnginePos());
                this.TargV.x--;
                this.ApproachR = 300.0F;
                this.TargY = 2.1F * f;
                this.TargZ = 3.9F * f;
                this.TargYS = 0.43F * f;
                this.TargZS = 0.43F * f;
                break;
            case 3:
                this.ApproachV.set(2600.0, 0.0, 300.0F + World.Rnd().nextFloat(-100.0F, 100.0F));
                this.TargV.set(0.0, 0.0, 0.0);
                this.ApproachR = 800.0F;
                this.TargY = 25.0F;
                this.TargZ = 15.0F;
                this.TargYS = 3.0F;
                this.TargZS = 3.0F;
                break;
            case 4:
                this.ApproachV.set(-400.0F + World.Rnd().nextFloat(-100.0F, 100.0F), 0.0, -200.0F + World.Rnd().nextFloat(-50.0F, 50.0F));
                this.TargV.set(0.0, 0.0, 0.0);
                this.ApproachR = 300.0F;
                this.TargY = 2.1F * f;
                this.TargZ = 1.3F * f;
                this.TargYS = 0.26F * f;
                this.TargZS = 0.26F * f;
                break;
            case 5:
                this.ApproachV.set(0.0, 0.0, 0.0);
                this.TargV.set(0.0, 0.0, 0.0);
                this.ApproachR = 600.0F;
                this.TargY = 25.0F;
                this.TargZ = 12.0F;
                this.TargYS = 0.26F * f;
                this.TargZS = 0.26F * f;
                break;
            case 6:
                this.ApproachV.set(600.0, 600 - (World.Rnd().nextInt(0, 1) * 1200) + World.Rnd().nextFloat(-100.0F, 100.0F), 300.0);
                this.TargV.set(0.0, 0.0, 0.0);
                this.ApproachR = 300.0F;
                this.TargY = 2.1F * f;
                this.TargZ = 1.2F * f;
                this.TargYS = 0.26F * f;
                this.TargZS = 0.26F * f;
                break;
            case 7:
                this.ApproachV.set(-1000.0F + World.Rnd().nextFloat(-200.0F, 200.0F), 0.0, 100.0F + World.Rnd().nextFloat(-50.0F, 50.0F));
                this.TargV.set(this.target.EI.engines[World.Rnd().nextInt(0, this.target.EI.getNum() - 1)].getEnginePos());
                this.ApproachR = 200.0F;
                this.TargY = 10.0F;
                this.TargZ = 10.0F;
                this.TargYS = 2.0F;
                this.TargZS = 2.0F;
                break;
            case 8:
                this.ApproachV.set(-1000.0F + World.Rnd().nextFloat(-200.0F, 200.0F), 0.0, 100.0F + World.Rnd().nextFloat(-50.0F, 50.0F));
                this.TargV.set(0.0, 0.0, 0.0);
                this.ApproachR = 200.0F;
                this.TargY = 2.1F * f;
                this.TargZ = 3.9F * f;
                this.TargYS = 0.2F * f;
                this.TargZS = 0.2F * f;
                break;
            case 9:
                this.ApproachV.set(-600.0F + World.Rnd().nextFloat(-100.0F, 100.0F), 0.0, 600.0F + World.Rnd().nextFloat(-100.0F, 100.0F));
                this.TargV.set(0.0, 0.0, 0.0);
                this.ApproachR = 300.0F;
                this.TargY = 2.1F * f;
                this.TargZ = 3.9F * f;
                this.TargYS = 0.2F * f;
                this.TargZS = 0.2F * f;
                break;
            case 10:
                this.ApproachV.set(-600.0F + World.Rnd().nextFloat(-100.0F, 100.0F), 0.0, -300.0F + World.Rnd().nextFloat(-100.0F, 100.0F));
                this.TargV.set(100.0, 0.0, -400.0);
                this.ApproachR = 300.0F;
                this.TargY = 2.1F * f;
                this.TargZ = 3.9F * f;
                this.TargYS = 0.43F * f;
                this.TargZS = 0.43F * f;
                break;
            default:
                this.ApproachV.set(-1000.0F + World.Rnd().nextFloat(-200.0F, 200.0F), 0.0, 100.0F + World.Rnd().nextFloat(-50.0F, 50.0F));
                this.TargV.set(this.target.EI.engines[World.Rnd().nextInt(0, this.target.EI.getNum() - 1)].getEnginePos());
                this.ApproachR = 200.0F;
                this.TargY = 10.0F;
                this.TargZ = 10.0F;
                this.TargYS = 2.0F;
                this.TargZS = 2.0F;
        }
        float f1 = 0.0F;
        if (this.CT.Weapons[1] != null && this.CT.Weapons[1][0].countBullets() > 0)
            f1 = ((GunGeneric) this.CT.Weapons[1][0]).bulletSpeed();
        else if (this.CT.Weapons[0] != null)
            f1 = ((GunGeneric) this.CT.Weapons[0][0]).bulletSpeed();
        if (f1 > 0.01F)
            this.bullTime = 1.0F / f1;
    }

    public void fighterVsBomber(float f) {
        this.maxAOA = 15.0F;
        tmpOr.setAT0(this.target.Vwld);
        switch (this.submaneuver) {
            case SUB_MAN0: {
                this.minElevCoeff = 4.0F;
                this.rocketsDelay = 0;
                this.bulletDelay = 0;
                double d = 1.0E-4 * this.target.Loc.z;
                Ve.sub(this.target.Loc, this.Loc);
                tmpOr.transformInv(Ve);
                this.scaledApproachV.set(this.ApproachV);
                this.scaledApproachV.x -= 700.0 * d;
                this.scaledApproachV.z += 500.0 * d;
                Ve.add(this.scaledApproachV);
                Ve.normalize();
                tmpV3f.scale(this.scaledApproachV, -1.0);
                tmpV3f.normalize();
                double d1 = Ve.dot(tmpV3f);
                Ve.set(this.Vwld);
                Ve.normalize();
                tmpV3f.set(this.target.Vwld);
                tmpV3f.normalize();
                double d2 = Ve.dot(tmpV3f);
                Ve.set(this.scaledApproachV);
                Ve.x += 60.0 * (2.0 * (1.0 - d1) + 4.0 * (1.0 - d2));
                tmpOr.transform(Ve);
                Ve.add(this.target.Loc);
                Ve.sub(this.Loc);
                double d3 = Ve.z;
                tmpV3f.set(Ve);
                tmpV3f.z = 0.0;
                float f1 = (float) tmpV3f.length();
                float f4 = 0.55F + 0.15F * this.Skill;
                tmpV3f.normalize();
                this.Vtarg.set(this.target.Vwld);
                this.Vtarg.z = 0.0;
                float f7 = (float) tmpV3f.dot(this.Vtarg);
                float f8 = this.getSpeed() - f7;
                if (f8 < 10.0F)
                    f8 = 10.0F;
                float f9 = f1 / f8;
                if (f9 < 0.0F)
                    f9 = 0.0F;
                tmpV3f.scale(this.Vtarg, f9 * f4);
                Ve.add(tmpV3f);
                this.Or.transformInv(Ve);
                Ve.normalize();
                if (d3 > -200.0)
                    this.setSpeedMode(9);
                else {
                    this.setSpeedMode(4);
                    this.smConstSpeed = 180.0F;
                }
                this.attackTurnToDirection(f1, f, 10.0F * (float) (1.0 + d));
                this.sub_Man_Count++;
                if (f1 < this.ApproachR * (1.0 + d) && d3 < 200.0) {
                    this.submaneuver++;
                    this.sub_Man_Count = 0;
                }
                break;
            }
            case SUB_MAN1: {
                this.minElevCoeff = 20.0F;
                Ve.set(this.TargV);
                this.target.Or.transform(Ve);
                Ve.add(this.target.Loc);
                Ve.sub(this.Loc);
                float f2 = (float) Ve.length();
                float f5 = 0.55F + 0.15F * this.Skill;
                tmpV3f.sub(this.target.Vwld, this.Vwld);
                float f10 = f2 * this.bullTime * 0.0025F;
                if (f10 > 0.05F)
                    f10 = 0.05F;
                tmpV3f.scale(f2 * f10 * f5);
                Ve.add(tmpV3f);
                this.Vtarg.set(Ve);
                this.Or.transformInv(Ve);
                Ve.normalize();
                ((Maneuver) this.target).incDangerAggressiveness(1, (float) Ve.x, f2, this);
                if (f2 > 3200.0F || this.Vtarg.z > 1500.0) {
                    this.submaneuver = 0;
                    this.sub_Man_Count = 0;
                    this.set_maneuver(0);
                } else {
                    if (Ve.x < 0.30000001192092896) {
                        this.Vtarg.z += (0.012F * this.Skill * (800.0F + f2) * (0.30000001192092896 - Ve.x));
                        Ve.set(this.Vtarg);
                        this.Or.transformInv(Ve);
                        Ve.normalize();
                    }
                    this.attackTurnToDirection(f2, f, 10.0F + this.Skill * 8.0F);
                    this.setSpeedMode(4);
                    this.smConstSpeed = 180.0F;
                    if (f2 < 400.0F) {
                        this.submaneuver++;
                        this.sub_Man_Count = 0;
                    }
                }
                break;
            }
            case SUB_MAN2: {
                this.minElevCoeff = 20.0F;
                if (this.rocketsDelay > 0)
                    this.rocketsDelay--;
                if (this.bulletDelay > 0)
                    this.bulletDelay--;
                if (this.bulletDelay == 120)
                    this.bulletDelay = 0;
                this.setRandomTargDeviation(0.8F);
                Ve.set(this.TargV);
                Ve.add(this.TargDevV);
                this.target.Or.transform(Ve);
                Ve.add(this.target.Loc);
                Ve.sub(this.Loc);
                this.Vtarg.set(Ve);
                float f3 = (float) Ve.length();
                float f6 = 0.55F + 0.15F * this.Skill;
                float f11 = 2.0E-4F * this.Skill;
                tmpV3f.sub(this.target.Vwld, this.Vwld);
                Vpl.set(tmpV3f);
                tmpV3f.scale(f3 * (this.bullTime + f11) * f6);
                Ve.add(tmpV3f);
                tmpV3f.set(Vpl);
                tmpV3f.scale(f3 * this.bullTime * f6);
                this.Vtarg.add(tmpV3f);
                if (f3 > 4000.0F || this.Vtarg.z > 2000.0) {
                    this.submaneuver = 0;
                    this.sub_Man_Count = 0;
                    this.set_maneuver(0);
                } else {
                    this.Or.transformInv(this.Vtarg);
                    if (this.Vtarg.x > 0.0 && f3 < 500.0F && (this.shotAtFriend <= 0 || this.distToFriend > f3) && Math.abs(this.Vtarg.y) < this.TargY - this.TargYS * this.Skill && Math.abs(this.Vtarg.z) < this.TargZ - this.TargZS * this.Skill
                            && this.bulletDelay < 120) {
                        if (!(this.actor instanceof KI_46_OTSUHEI))
                            this.CT.WeaponControl[0] = true;
                        this.CT.WeaponControl[1] = true;
                        this.bulletDelay += 2;
                        if (this.bulletDelay >= 118) {
                            int i = (int) (this.target.getW().length() * 150.0);
                            if (i > 60)
                                i = 60;
                            this.bulletDelay += World.Rnd().nextInt(i * this.Skill, 2 * i * this.Skill);
                        }
                        if (this.CT.Weapons[2] != null && this.CT.Weapons[2][0] != null && this.CT.Weapons[2][this.CT.Weapons[2].length - 1].countBullets() != 0 && this.rocketsDelay < 1) {
                            tmpV3f.sub(this.target.Vwld, this.Vwld);
                            this.Or.transformInv(tmpV3f);
                            if ((Math.abs(tmpV3f.y) < this.TargY - this.TargYS * this.Skill) && (Math.abs(tmpV3f.z) < this.TargZ - this.TargZS * this.Skill))
                                this.CT.WeaponControl[2] = true;
                            Voice.speakAttackByRockets((Aircraft) this.actor);
                            this.rocketsDelay += 120;
                        }
                        ((Maneuver) this.target).incDangerAggressiveness(1, 0.8F, f3, this);
                        ((Pilot) this.target).setAsDanger(this.actor);
                    }
                    this.Vtarg.set(Ve);
                    this.Or.transformInv(Ve);
                    Ve.normalize();
                    ((Maneuver) this.target).incDangerAggressiveness(1, (float) Ve.x, f3, this);
                    if (Ve.x < 0.30000001192092896) {
                        this.Vtarg.z += (0.01F * this.Skill * (800.0F + f3) * (0.30000001192092896 - Ve.x));
                        Ve.set(this.Vtarg);
                        this.Or.transformInv(Ve);
                        Ve.normalize();
                    }
                    this.attackTurnToDirection(f3, f, 10.0F + this.Skill * 8.0F);
                    if (this.target.getSpeed() > 70.0F) {
                        this.setSpeedMode(2);
                        this.tailForStaying = this.target;
                        this.tailOffset.set(-20.0, 0.0, 0.0);
                    } else {
                        this.setSpeedMode(4);
                        this.smConstSpeed = 70.0F;
                    }
                    if (this.strikeEmer) {
                        Vpl.sub(this.strikeTarg.Loc, this.Loc);
                        tmpV3f.set(Vpl);
                        this.Or.transformInv(tmpV3f);
                        if (tmpV3f.x < 0.0)
                            break;
                        tmpV3f.sub(this.strikeTarg.Vwld, this.Vwld);
                        tmpV3f.scale(0.699999988079071);
                        Vpl.add(tmpV3f);
                        this.Or.transformInv(Vpl);
                        this.push();
                        if (Vpl.z < 0.0) {
                            this.push(0);
                            this.push(8);
                            this.push(60);
                        } else {
                            this.push(0);
                            this.push(8);
                            this.push(61);
                        }
                        this.pop();
                        this.strikeEmer = false;
                        this.submaneuver = 0;
                        this.sub_Man_Count = 0;
                    }
                    if (this.sub_Man_Count > 600) {
                        this.push(8);
                        this.pop();
                    }
                }
                break;
            }
            default:
                this.submaneuver = SUB_MAN0;
                this.sub_Man_Count = 0;
                this.set_maneuver(0);
        }
    }

    public void fighterUnderBomber(float f) {
        this.maxAOA = 15.0F;
        switch (this.submaneuver) {
            case SUB_MAN0: {
                this.rocketsDelay = 0;
                this.bulletDelay = 0;
                Ve.set(this.ApproachV);
                this.target.Or.transform(Ve);
                Ve.add(this.target.Loc);
                Ve.sub(this.Loc);
                tmpV3f.set(Ve);
                tmpV3f.z = 0.0;
                float f1 = (float) tmpV3f.length();
                float f4 = 0.55F + 0.15F * this.Skill;
                tmpV3f.normalize();
                this.Vtarg.set(this.target.Vwld);
                this.Vtarg.z = 0.0;
                float f7 = (float) tmpV3f.dot(this.Vtarg);
                float f8 = this.getSpeed() - f7;
                if (f8 < 10.0F)
                    f8 = 10.0F;
                float f9 = f1 / f8;
                if (f9 < 0.0F)
                    f9 = 0.0F;
                tmpV3f.scale(this.Vtarg, f9 * f4);
                Ve.add(tmpV3f);
                this.Or.transformInv(Ve);
                Ve.normalize();
                this.setSpeedMode(4);
                this.smConstSpeed = 140.0F;
                this.farTurnToDirection();
                this.sub_Man_Count++;
                if (f1 < this.ApproachR) {
                    this.submaneuver++;
                    this.sub_Man_Count = 0;
                }
                break;
            }
            case SUB_MAN1: {
                Ve.set(this.TargV);
                this.target.Or.transform(Ve);
                Ve.add(this.target.Loc);
                Ve.sub(this.Loc);
                float f2 = (float) Ve.length();
                float f5 = 0.55F + 0.15F * this.Skill;
                tmpV3f.sub(this.target.Vwld, this.Vwld);
                float f10 = f2 * this.bullTime * 0.0025F;
                if (f10 > 0.02F)
                    f10 = 0.02F;
                tmpV3f.scale(f2 * f10 * f5);
                Ve.add(tmpV3f);
                this.Vtarg.set(Ve);
                this.Or.transformInv(Ve);
                Ve.normalize();
                ((Maneuver) this.target).incDangerAggressiveness(1, (float) Ve.x, f2, this);
                if (f2 > 3200.0F || this.Vtarg.z > 1500.0) {
                    this.submaneuver = 0;
                    this.sub_Man_Count = 0;
                } else {
                    if (Ve.x < 0.30000001192092896) {
                        this.Vtarg.z += (0.01F * this.Skill * (800.0F + f2) * (0.30000001192092896 - Ve.x));
                        Ve.set(this.Vtarg);
                        this.Or.transformInv(Ve);
                        Ve.normalize();
                    }
                    this.attackTurnToDirection(f2, f, 10.0F);
                    if (this.target.getSpeed() > 120.0F) {
                        this.setSpeedMode(2);
                        this.tailForStaying = this.target;
                    } else {
                        this.setSpeedMode(4);
                        this.smConstSpeed = 120.0F;
                    }
                    if (f2 < 400.0F) {
                        this.submaneuver++;
                        this.sub_Man_Count = 0;
                    }
                }
                break;
            }
            case SUB_MAN2: {
                this.setCheckStrike(false);
                if (this.rocketsDelay > 0)
                    this.rocketsDelay--;
                if (this.bulletDelay > 0)
                    this.bulletDelay--;
                if (this.bulletDelay == 120)
                    this.bulletDelay = 0;
                this.setRandomTargDeviation(0.8F);
                Ve.set(this.TargV);
                Ve.add(this.TargDevV);
                this.target.Or.transform(Ve);
                Ve.add(this.target.Loc);
                Ve.sub(this.Loc);
                float f3 = (float) Ve.length();
                float f6 = 0.55F + 0.15F * this.Skill;
                tmpV3f.sub(this.target.Vwld, this.Vwld);
                tmpV3f.scale(f3 * this.bullTime * f6);
                Ve.add(tmpV3f);
                this.Vtarg.set(Ve);
                this.Or.transformInv(Ve);
                if (f3 > 4000.0F || this.Vtarg.z > 2000.0) {
                    this.submaneuver = 0;
                    this.sub_Man_Count = 0;
                } else {
                    if (this.AS.astatePilotStates[1] < 100 && f3 < 330.0F && Math.abs(this.Or.getKren()) < 3.0F)
                        this.CT.WeaponControl[0] = true;
                    Ve.normalize();
                    if (Ve.x < 0.30000001192092896) {
                        this.Vtarg.z += (0.01F * this.Skill * (800.0F + f3) * (0.30000001192092896 - Ve.x));
                        Ve.set(this.Vtarg);
                        this.Or.transformInv(Ve);
                        Ve.normalize();
                    }
                    this.attackTurnToDirection(f3, f, 6.0F + this.Skill * 3.0F);
                    this.setSpeedMode(1);
                    this.tailForStaying = this.target;
                    this.tailOffset.set(-190.0, 0.0, 0.0);
                    if (this.sub_Man_Count > 10000 || f3 < 240.0F) {
                        this.push(9);
                        this.pop();
                    }
                }
                break;
            }
            default:
                this.submaneuver = SUB_MAN0;
                this.sub_Man_Count = 0;
        }
    }

    public void fighterVsFighter(float f) {
        if (this.sub_Man_Count == 0) {
            float f1 = 0.0F;
            if (this.CT.Weapons[1] != null && this.CT.Weapons[1][0].countBullets() > 0)
                f1 = ((GunGeneric) this.CT.Weapons[1][0]).bulletSpeed();
            else if (this.CT.Weapons[0] != null)
                f1 = ((GunGeneric) this.CT.Weapons[0][0]).bulletSpeed();
            if (f1 > 0.01F)
                this.bullTime = 1.0F / f1;
        }
        this.maxAOA = 15.0F;
        if (this.rocketsDelay > 0)
            this.rocketsDelay--;
        if (this.bulletDelay > 0)
            this.bulletDelay--;
        if (this.bulletDelay == 122)
            this.bulletDelay = 0;
        Ve.sub(this.target.Loc, this.Loc);
        this.setRandomTargDeviation(0.3F);
        Ve.add(this.TargDevV);
        this.Vtarg.set(Ve);
        float f2 = (float) Ve.length();
        float f3 = 0.55F + 0.15F * this.Skill;
        float f4 = 2.0E-4F * this.Skill;
        tmpV3f.sub(this.target.Vwld, this.Vwld);
        Vpl.set(tmpV3f);
        tmpV3f.scale(f2 * (this.bullTime + f4) * f3);
        Ve.add(tmpV3f);
        tmpV3f.set(Vpl);
        tmpV3f.scale(f2 * this.bullTime * f3);
        this.Vtarg.add(tmpV3f);
        this.Or.transformInv(Vpl);
        float f5 = (float) -Vpl.x;
        if (f5 < 0.0010F)
            f5 = 0.0010F;
        Vpl.normalize();
        if (Vpl.x < -0.9399999976158142 && f2 / f5 < 1.5F * (3 - this.Skill)) {
            this.push();
            this.set_maneuver(14);
        } else {
            this.Or.transformInv(this.Vtarg);
            if (this.Vtarg.x > 0.0 && f2 < 500.0F && (this.shotAtFriend <= 0 || this.distToFriend > f2)) {
                if (Math.abs(this.Vtarg.y) < 13.0 && Math.abs(this.Vtarg.z) < 13.0)
                    ((Maneuver) this.target).incDangerAggressiveness(1, 0.95F, f2, this);
                if (Math.abs(this.Vtarg.y) < 12.5F - 3.5F * this.Skill && Math.abs(this.Vtarg.z) < 12.5 - 3.5 * this.Skill && this.bulletDelay < 120) {
                    if (!(this.actor instanceof KI_46_OTSUHEI))
                        this.CT.WeaponControl[0] = true;
                    this.bulletDelay += 2;
                    if (this.bulletDelay >= 118) {
                        int i = (int) (this.target.getW().length() * 150.0);
                        if (i > 60)
                            i = 60;
                        this.bulletDelay += World.Rnd().nextInt(i * this.Skill, 2 * i * this.Skill);
                    }
                    if (f2 < 400.0F) {
                        this.CT.WeaponControl[1] = true;
                        if (f2 < 100.0F && this.CT.Weapons[2] != null && this.CT.Weapons[2][0] != null && this.CT.Weapons[2][this.CT.Weapons[2].length - 1].countBullets() != 0 && this.rocketsDelay < 1) {
                            tmpV3f.sub(this.target.Vwld, this.Vwld);
                            this.Or.transformInv(tmpV3f);
                            if (Math.abs(tmpV3f.y) < 4.0 && Math.abs(tmpV3f.z) < 4.0)
                                this.CT.WeaponControl[2] = true;
                            Voice.speakAttackByRockets((Aircraft) this.actor);
                            this.rocketsDelay += 120;
                        }
                    }
                    ((Pilot) this.target).setAsDanger(this.actor);
                }
            }
            this.Vtarg.set(Ve);
            this.Or.transformInv(Ve);
            Ve.normalize();
            ((Maneuver) this.target).incDangerAggressiveness(1, (float) Ve.x, f2, this);
            if (Ve.x < 0.30000001192092896) {
                this.Vtarg.z += (0.01F * this.Skill * (800.0F + f2) * (0.30000001192092896 - Ve.x));
                Ve.set(this.Vtarg);
                this.Or.transformInv(Ve);
                Ve.normalize();
            }
            this.attackTurnToDirection(f2, f, 10.0F + this.Skill * 8.0F);
            if (this.Alt > 150.0F && Ve.x > 0.6 && f2 < 70.0) {
                this.setSpeedMode(1);
                this.tailForStaying = this.target;
                this.tailOffset.set(-20.0, 0.0, 0.0);
            } else
                this.setSpeedMode(9);
        }
    }

    public void boomAttack(float f) {
        if (this.sub_Man_Count == 0) {
            float f1 = 0.0F;
            if (this.CT.Weapons[1] != null && this.CT.Weapons[1][0].countBullets() > 0)
                f1 = ((GunGeneric) this.CT.Weapons[1][0]).bulletSpeed();
            else if (this.CT.Weapons[0] != null)
                f1 = ((GunGeneric) this.CT.Weapons[0][0]).bulletSpeed();
            if (f1 > 0.01F)
                this.bullTime = 1.0F / f1;
        }
        this.maxAOA = 15.0F;
        if (this.rocketsDelay > 0)
            this.rocketsDelay--;
        if (this.bulletDelay > 0)
            this.bulletDelay--;
        if (this.bulletDelay == 122)
            this.bulletDelay = 0;
        Ve.sub(this.target.Loc, this.Loc);
        this.setRandomTargDeviation(0.3F);
        Ve.add(this.TargDevV);
        this.Vtarg.set(Ve);
        float f2 = (float) Ve.length();
        float f3 = 0.55F + 0.15F * this.Skill;
        float f4 = 3.33E-4F * this.Skill;
        tmpV3f.sub(this.target.Vwld, this.Vwld);
        Vpl.set(tmpV3f);
        tmpV3f.scale(f2 * (this.bullTime + f4) * f3);
        Ve.add(tmpV3f);
        tmpV3f.set(Vpl);
        tmpV3f.scale(f2 * this.bullTime * f3);
        this.Vtarg.add(tmpV3f);
        this.Or.transformInv(Vpl);
        float f5 = (float) -Vpl.x;
        if (f5 < 0.0010F)
            f5 = 0.0010F;
        Vpl.normalize();
        if (Vpl.x < -0.9399999976158142 && f2 / f5 < 1.5F * (3 - this.Skill)) {
            this.push();
            this.set_maneuver(14);
        } else {
            this.Or.transformInv(this.Vtarg);
            if (this.Vtarg.x > 0.0 && f2 < 700.0F && (this.shotAtFriend <= 0 || this.distToFriend > f2) && Math.abs(this.Vtarg.y) < 15.5F - 3.5F * this.Skill && Math.abs(this.Vtarg.z) < 15.5 - 3.5 * this.Skill && this.bulletDelay < 120) {
                ((Maneuver) this.target).incDangerAggressiveness(1, 0.8F, f2, this);
                if (!(this.actor instanceof KI_46_OTSUHEI))
                    this.CT.WeaponControl[0] = true;
                this.bulletDelay += 2;
                if (this.bulletDelay >= 118) {
                    int i = (int) (this.target.getW().length() * 150.0);
                    if (i > 60)
                        i = 60;
                    this.bulletDelay += World.Rnd().nextInt(i * this.Skill, 2 * i * this.Skill);
                }
                if (f2 < 500.0F) {
                    this.CT.WeaponControl[1] = true;
                    if (f2 < 100.0F && this.CT.Weapons[2] != null && this.CT.Weapons[2][0] != null && this.CT.Weapons[2][this.CT.Weapons[2].length - 1].countBullets() != 0 && this.rocketsDelay < 1) {
                        tmpV3f.sub(this.target.Vwld, this.Vwld);
                        this.Or.transformInv(tmpV3f);
                        if (Math.abs(tmpV3f.y) < 4.0 && Math.abs(tmpV3f.z) < 4.0)
                            this.CT.WeaponControl[2] = true;
                        Voice.speakAttackByRockets((Aircraft) this.actor);
                        this.rocketsDelay += 120;
                    }
                }
                ((Pilot) this.target).setAsDanger(this.actor);
            }
            this.Vtarg.set(Ve);
            this.Or.transformInv(Ve);
            Ve.normalize();
            ((Maneuver) this.target).incDangerAggressiveness(1, (float) Ve.x, f2, this);
            if (Ve.x < 0.8999999761581421) {
                this.Vtarg.z += (0.03F * this.Skill * (800.0F + f2) * (0.8999999761581421 - Ve.x));
                Ve.set(this.Vtarg);
                this.Or.transformInv(Ve);
                Ve.normalize();
            }
            this.attackTurnToDirection(f2, f, 10.0F + this.Skill * 8.0F);
        }
    }

    private void turnToDirection(float f) {
        if (Math.abs(Ve.y) > 0.10000000149011612)
            this.CT.AileronControl = -(float) Math.atan2(Ve.y, Ve.z) - 0.016F * this.Or.getKren();
        else
            this.CT.AileronControl = -(float) Math.atan2(Ve.y, Ve.x) - 0.016F * this.Or.getKren();
        this.CT.RudderControl = -10.0F * (float) Math.atan2(Ve.y, Ve.x);
        if (this.CT.RudderControl * this.W.z > 0.0)
            this.W.z = 0.0;
        else
            this.W.z *= 1.0399999618530273;
        float f1 = (float) Math.atan2(Ve.z, Ve.x);
        if (Math.abs(Ve.y) < 0.0020000000949949026 && Math.abs(Ve.z) < 0.0020000000949949026)
            f1 = 0.0F;
        if (Ve.x < 0.0)
            f1 = 1.0F;
        else {
            if (f1 * this.W.y > 0.0)
                this.W.y = 0.0;
            if (f1 < 0.0F) {
                if (this.getOverload() < 0.1F)
                    f1 = 0.0F;
                if (this.CT.ElevatorControl > 0.0F)
                    this.CT.ElevatorControl = 0.0F;
            } else if (this.CT.ElevatorControl < 0.0F)
                this.CT.ElevatorControl = 0.0F;
        }
        if (this.getOverload() > this.maxG || this.AOA > this.maxAOA || this.CT.ElevatorControl > f1)
            this.CT.ElevatorControl -= 0.3F * f;
        else
            this.CT.ElevatorControl += 0.3F * f;
    }

    private void farTurnToDirection() {
        this.farTurnToDirection(4.0F);
    }

    private void farTurnToDirection(float f) {
        Vpl.set(1.0, 0.0, 0.0);
        tmpV3f.cross(Vpl, Ve);
        this.Or.transform(tmpV3f);
        this.CT.RudderControl = -10.0F * (float) Math.atan2(Ve.y, Ve.x) + 1.0F * (float) this.W.y;
        float f7 = this.getSpeed() / this.Vmax * 45.0F;
        if (f7 > 85.0F)
            f7 = 85.0F;
        float f8 = (float) Ve.x;
        if (f8 < 0.0F)
            f8 = 0.0F;
        float f1;
        if (tmpV3f.z >= 0.0)
            f1 = (-0.02F * (f7 + this.Or.getKren()) * (1.0F - f8) - 0.05F * this.Or.getTangage() + 1.0F * (float) this.W.x);
        else
            f1 = (-0.02F * (-f7 + this.Or.getKren()) * (1.0F - f8) + 0.05F * this.Or.getTangage() + 1.0F * (float) this.W.x);
        float f2 = (-(float) Math.atan2(Ve.y, Ve.x) - 0.016F * this.Or.getKren() + 1.0F * (float) this.W.x);
        float f4 = -0.1F * (this.getAOA() - 10.0F) + 0.5F * (float) this.getW().y;
        float f5;
        if (Ve.z > 0.0)
            f5 = -0.1F * (this.getAOA() - f) + 0.5F * (float) this.getW().y;
        else
            f5 = 1.0F * (float) Ve.z + 0.5F * (float) this.getW().y;
        if (Math.abs(Ve.y) < 0.0020000000949949026) {
            f2 = 0.0F;
            this.CT.RudderControl = 0.0F;
        }
        tmpV3f.set(Ve);
        this.Or.transform(tmpV3f);
        float f9 = (float) Math.atan2(tmpV3f.y, tmpV3f.x) * 180.0F / 3.1415F;
        float f10 = 1.0F - Math.abs(f9 - this.Or.getYaw()) * 0.01F;
        if (f10 < 0.0F || Ve.x < 0.0)
            f10 = 0.0F;
        float f3 = f10 * f2 + (1.0F - f10) * f1;
        float f6 = f10 * f5 + (1.0F - f10) * f4;
        this.CT.AileronControl = f3;
        this.CT.ElevatorControl = f6;
    }

    private void attackTurnToDirection(float f, float f1, float f2) {
        if (Ve.x < 0.009999999776482582)
            Ve.x = 0.009999999776482582;
        if (this.sub_Man_Count == 0)
            this.oldVe.set(Ve);
        if (Ve.x > 0.949999988079071) {
            this.CT.RudderControl = (float) (-30.0 * Math.atan2(Ve.y, Ve.x) + 1.5 * (Ve.y - this.oldVe.y));
            float f3;
            if (Ve.z > 0.0 || this.CT.RudderControl > 0.9F) {
                f3 = (float) (10.0 * Math.atan2(Ve.z, Ve.x) + 6.0 * (Ve.z - this.oldVe.z));
                this.CT.AileronControl = (-30.0F * (float) Math.atan2(Ve.y, Ve.x) - 0.02F * this.Or.getKren() + 5.0F * (float) this.W.x);
            } else {
                f3 = (float) (5.0 * Math.atan2(Ve.z, Ve.x) + 6.0 * (Ve.z - this.oldVe.z));
                this.CT.AileronControl = (-5.0F * (float) Math.atan2(Ve.y, Ve.x) - 0.02F * this.Or.getKren() + 5.0F * (float) this.W.x);
            }
            if (Ve.x > 1.0F - 0.0050F * this.Skill) {
                tmpOr.set(this.Or);
                tmpOr.increment((float) Math.toDegrees(Math.atan2(Ve.y, Ve.x)), (float) Math.toDegrees(Math.atan2(Ve.z, Ve.x)), 0.0F);
                this.Or.interpolate(tmpOr, 0.1F);
            }
            if (Math.abs(this.CT.ElevatorControl - f3) < f2 * f1)
                this.CT.ElevatorControl = f3;
            else if (this.CT.ElevatorControl < f3)
                this.CT.ElevatorControl += f2 * f1;
            else
                this.CT.ElevatorControl -= f2 * f1;
        } else {
            if (this.Skill >= 2 && Ve.z > 0.5 && f < 600.0F)
                this.CT.FlapsControl = 0.1F;
            else
                this.CT.FlapsControl = 0.0F;
            float f5 = 0.6F - (float) Ve.z;
            if (f5 < 0.0F)
                f5 = 0.0F;
            this.CT.RudderControl = (float) (-30.0 * Math.atan2(Ve.y, Ve.x) * f5 + 1.0 * (Ve.y - this.oldVe.y) * Ve.x + 0.5 * this.W.z);
            float f4;
            if (Ve.z > 0.0) {
                f4 = (float) (10.0 * Math.atan2(Ve.z, Ve.x) + 6.0 * (Ve.z - this.oldVe.z) + 0.5 * (float) this.W.y);
                if (f4 < 0.0F)
                    f4 = 0.0F;
                this.CT.AileronControl = (float) (-20.0 * Math.atan2(Ve.y, Ve.z) - 0.05 * this.Or.getKren() + 5.0 * this.W.x);
            } else {
                f4 = (float) (-5.0 * Math.atan2(Ve.z, Ve.x) + 6.0 * (Ve.z - this.oldVe.z) + 0.5 * (float) this.W.y);
                this.CT.AileronControl = (float) (-20.0 * Math.atan2(Ve.y, Ve.z) - 0.05 * this.Or.getKren() + 5.0 * this.W.x);
            }
            if (f4 < 0.0F)
                f4 = 0.0F;
            if (Math.abs(this.CT.ElevatorControl - f4) < f2 * f1)
                this.CT.ElevatorControl = f4;
            else if (this.CT.ElevatorControl < f4)
                this.CT.ElevatorControl += 0.3F * f1;
            else
                this.CT.ElevatorControl -= 0.3F * f1;
        }
        float f6 = 0.054F * (600.0F - f);
        if (f6 < 4.0F)
            f6 = 4.0F;
        if (f6 > this.AOA_Crit)
            f6 = this.AOA_Crit;
        if (this.AOA > f6 - 1.5F)
            this.Or.increment(0.0F, 0.16F * (f6 - 1.5F - this.AOA), 0.0F);
        if (this.AOA < -5.0F)
            this.Or.increment(0.0F, 0.12F * (-5.0F - this.AOA), 0.0F);
        this.oldVe.set(Ve);
        this.sub_Man_Count++;
        this.W.x *= 0.95;
    }

    private void doCheckStrike() {
        if ((!(this.M.massEmpty > 5000.0F) || this.AP.way.isLanding()) && (this.maneuver != 24 || this.strikeTarg != this.Leader) && (!(this.actor instanceof TypeDockable) || !((TypeDockable) this.actor).typeDockableIsDocked())) {
            Vpl.sub(this.strikeTarg.Loc, this.Loc);
            tmpV3f.set(Vpl);
            this.Or.transformInv(tmpV3f);
            if (!(tmpV3f.x < 0.0)) {
                tmpV3f.sub(this.strikeTarg.Vwld, this.Vwld);
                tmpV3f.scale(0.699999988079071);
                Vpl.add(tmpV3f);
                this.Or.transformInv(Vpl);
                this.push();
                if (Vpl.z > 0.0)
                    this.push(61);
                else
                    this.push(60);
                this.safe_pop();
            }
        }
    }

    public void setStrikeEmer(FlightModel flightmodel) {
        this.strikeEmer = true;
        this.strikeTarg = flightmodel;
    }

    protected void wingman(boolean flag) {
        if (this.Wingman != null)
            this.Wingman.Leader = flag ? this : null;
    }

    public float getMnTime() {
        return this.mn_time;
    }

    public void doDumpBombsPassively() {
        boolean flag = false;
        for (int i = 0; i < this.CT.Weapons.length; i++) {
            if (this.CT.Weapons[i] != null && this.CT.Weapons[i].length > 0) {
                for (int j = 0; j < this.CT.Weapons[i].length; j++) {
                    if (this.CT.Weapons[i][j] instanceof BombGun) {
                        if (this.CT.Weapons[i][j] instanceof BombGunPara || (this.actor instanceof SM79 && this.CT.Weapons[i][j] instanceof TorpedoGun))
                            flag = true;
                        else {
                            ((BombGun) this.CT.Weapons[i][j]).setBombDelay(3.3E7F);
                            if (this.actor == World.getPlayerAircraft() && !World.cur().diffCur.Limited_Ammo)
                                this.CT.Weapons[i][j].loadBullets(0);
                        }
                    }
                }
            }
        }
        if (!flag)
            this.bombsOut = true;
    }

    protected void printDebugFM() {
        System.out.print("<" + this.actor.name() + "> " + ManString.tname(this.task) + ":" + ManString.name(this.maneuver) + (this.target == null ? "t" : "T") + (this.danger == null ? "d" : "D") + (this.target_ground == null ? "g " : "G "));
        switch (this.maneuver) {
            case 21:
                System.out.println(": WP=" + this.AP.way.Cur() + "(" + (this.AP.way.size() - 1) + ")-" + ManString.wpname(this.AP.way.curr().Action));
                if (this.target_ground != null)
                    System.out.println(" GT=" + this.target_ground.getClass().getName() + "(" + this.target_ground.name() + ")");
                break;
            case 27:
                if (this.target == null || !Actor.isValid(this.target.actor))
                    System.out.println(" T=null");
                else
                    System.out.println(" T=" + this.target.actor.getClass().getName() + "(" + this.target.actor.name() + ")");
                break;
            case 43:
            case 50:
            case 51:
            case 52:
                if (this.target_ground == null || !Actor.isValid(this.target_ground))
                    System.out.println(" GT=null");
                else
                    System.out.println(" GT=" + this.target_ground.getClass().getName() + "(" + this.target_ground.name() + ")");
                break;
            default:
                System.out.println("");
                if (this.target == null || !Actor.isValid(this.target.actor))
                    System.out.println(" T=null");
                else {
                    System.out.println(" T=" + this.target.actor.getClass().getName() + "(" + this.target.actor.name() + ")");
                    if (this.target_ground == null || !Actor.isValid(this.target_ground))
                        System.out.println(" GT=null");
                    else
                        System.out.println(" GT=" + this.target_ground.getClass().getName() + "(" + this.target_ground.name() + ")");
                }
        }
    }

    protected void headTurn(float f) {
        if (this.actor == Main3D.cur3D().viewActor() && this.AS.astatePilotStates[0] < 90) {
            boolean flag = false;
            switch (this.get_task()) {
                case 2:
                    if (this.Leader != null) {
                        Ve.set(this.Leader.Loc);
                        flag = true;
                    }
                    break;
                case 6:
                    if (this.target != null) {
                        Ve.set(this.target.Loc);
                        flag = true;
                    }
                    break;
                case 5:
                    if (this.airClient != null) {
                        Ve.set(this.airClient.Loc);
                        flag = true;
                    }
                    break;
                case 4:
                    if (this.danger != null) {
                        Ve.set(this.danger.Loc);
                        flag = true;
                    }
                    break;
                case 7:
                    if (this.target_ground != null) {
                        Ve.set(this.target_ground.pos.getAbsPoint());
                        flag = true;
                    }
                    break;
            }
            float f1;
            float f2;
            if (flag) {
                Ve.sub(this.Loc);
                this.Or.transformInv(Ve);
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
            } else if (this.get_maneuver() != 44) {
                f1 = 0.0F;
                f2 = 0.0F;
            } else {
                f2 = -15.0F;
                f1 = -15.0F;
            }
            if (Math.abs(this.pilotHeadT - f1) > 3.0F) {
                Maneuver maneuver = this;
                maneuver.pilotHeadT = (maneuver.pilotHeadT + 90.0F * (this.pilotHeadT <= f1 ? 1.0F : -1.0F) * f);
            } else
                this.pilotHeadT = f1;
            if (Math.abs(this.pilotHeadY - f2) > 2.0F) {
                Maneuver maneuver = this;
                maneuver.pilotHeadY = (maneuver.pilotHeadY + 60.0F * (this.pilotHeadY <= f2 ? 1.0F : -1.0F) * f);
            } else
                this.pilotHeadY = f2;
            tmpOr.setYPR(0.0F, 0.0F, 0.0F);
            tmpOr.increment(0.0F, this.pilotHeadY, 0.0F);
            tmpOr.increment(this.pilotHeadT, 0.0F, 0.0F);
            tmpOr.increment(0.0F, 0.0F, -0.2F * this.pilotHeadT + 0.05F * this.pilotHeadY);
            this.headOr[0] = tmpOr.getYaw();
            this.headOr[1] = tmpOr.getPitch();
            this.headOr[2] = tmpOr.getRoll();
            this.headPos[0] = 5.0E-4F * Math.abs(this.pilotHeadY);
            this.headPos[1] = -1.0E-4F * Math.abs(this.pilotHeadY);
            this.headPos[2] = 0.0F;
            ((ActorHMesh) this.actor).hierMesh().chunkSetLocate("Head1_D0", this.headPos, this.headOr);
        }
    }

    protected void turnOffTheWeapon() {
        this.CT.WeaponControl[0] = false;
        this.CT.WeaponControl[1] = false;
        this.CT.WeaponControl[2] = false;
        this.CT.WeaponControl[3] = false;
        if (this.bombsOut) {
            this.bombsOutCounter++;
            if (this.bombsOutCounter > 128) {
                this.bombsOutCounter = 0;
                this.bombsOut = false;
            }
            if (this.CT.Weapons[3] != null)
                this.CT.WeaponControl[3] = true;
            else
                this.bombsOut = false;
            if (this.CT.Weapons[3] != null && this.CT.Weapons[3][0] != null) {
                int i = 0;
                for (int j = 0; j < this.CT.Weapons[3].length; j++)
                    i += this.CT.Weapons[3][j].countBullets();
                if (i == 0) {
                    this.bombsOut = false;
                    for (int k = 0; k < this.CT.Weapons[3].length; k++)
                        this.CT.Weapons[3][k].loadBullets(0);
                }
            }
        }
    }

    protected void turnOnCloudShine(boolean flag) {
        if (flag) {
            if (World.Sun().ToSun.z < -0.22F)
                this.AS.setLandingLightState(true);
        } else
            this.AS.setLandingLightState(false);
    }

    protected void doCheckGround(float f) {
        if (this.CT.AileronControl > 1.0F)
            this.CT.AileronControl = 1.0F;
        if (this.CT.AileronControl < -1.0F)
            this.CT.AileronControl = -1.0F;
        if (this.CT.ElevatorControl > 1.0F)
            this.CT.ElevatorControl = 1.0F;
        if (this.CT.ElevatorControl < -1.0F)
            this.CT.ElevatorControl = -1.0F;
        float f4 = 3.0E-4F * this.M.massEmpty;
        float f5 = 10.0F;
        float f6 = 80.0F;
        if (this.maneuver == 24) {
            f5 = 15.0F;
            f6 = 120.0F;
        }
        float f7 = (float) -this.Vwld.z * f5 * f4;
        float f8 = 1.0F + 7.0F * ((this.indSpeed - this.VmaxAllowed) / this.VmaxAllowed);
        if (f8 > 1.0F)
            f8 = 1.0F;
        if (f8 < 0.0F)
            f8 = 0.0F;
        float f1;
        float f2;
        float f3;
        if (f7 > this.Alt || this.Alt < f6 || f8 > 0.0F) {
            if (this.Alt < 0.0010F)
                this.Alt = 0.0010F;
            f1 = (f7 - this.Alt) / this.Alt;
            Math.max(0.01667 * (f6 - this.Alt), f1);
            if (f1 > 1.0F)
                f1 = 1.0F;
            if (f1 < 0.0F)
                f1 = 0.0F;
            if (f1 < f8)
                f1 = f8;
            f2 = -0.12F * (this.Or.getTangage() - 5.0F) + 3.0F * (float) this.W.y;
            f3 = -0.07F * this.Or.getKren() + 3.0F * (float) this.W.x;
            if (f3 > 2.0F)
                f3 = 2.0F;
            if (f3 < -2.0F)
                f3 = -2.0F;
            if (f2 > 2.0F)
                f2 = 2.0F;
            if (f2 < -2.0F)
                f2 = -2.0F;
        } else {
            f1 = 0.0F;
            f2 = 0.0F;
            f3 = 0.0F;
        }
        float f9 = 0.2F;
        if (this.corrCoeff < f1)
            this.corrCoeff = f1;
        if (this.corrCoeff > f1)
            this.corrCoeff *= 1.0 - f9 * f;
        if (f2 < 0.0F) {
            if (this.corrElev > f2)
                this.corrElev = f2;
            if (this.corrElev < f2)
                this.corrElev *= 1.0 - f9 * f;
        } else {
            if (this.corrElev < f2)
                this.corrElev = f2;
            if (this.corrElev > f2)
                this.corrElev *= 1.0 - f9 * f;
        }
        if (f3 < 0.0F) {
            if (this.corrAile > f3)
                this.corrAile = f3;
            if (this.corrAile < f3)
                this.corrAile *= 1.0 - f9 * f;
        } else {
            if (this.corrAile < f3)
                this.corrAile = f3;
            if (this.corrAile > f3)
                this.corrAile *= 1.0 - f9 * f;
        }
        this.CT.AileronControl = this.corrCoeff * this.corrAile + (1.0F - this.corrCoeff) * this.CT.AileronControl;
        this.CT.ElevatorControl = this.corrCoeff * this.corrElev + (1.0F - this.corrCoeff) * this.CT.ElevatorControl;
        if (this.Alt < 15.0F && this.Vwld.z < 0.0)
            this.Vwld.z *= 0.8500000238418579;
        if (-this.Vwld.z * 1.5 > this.Alt || this.Alt < 10.0F) {
            if (this.maneuver == 27 || this.maneuver == 43 || this.maneuver == 21 || this.maneuver == 24 || this.maneuver == 68 || this.maneuver == 53)
                this.push();
            this.push(2);
            this.pop();
            this.submaneuver = SUB_MAN0;
            this.sub_Man_Count = 0;
        }
        this.W.x *= 0.95;
    }

    protected void setSpeedControl(float f) {
        float f1 = 0.8F;
        float f4 = 0.02F;
        float f5 = 1.5F;
        this.CT.setAfterburnerControl(false);
        switch (this.speedMode) {
            case 1:
                if (this.tailForStaying == null)
                    f1 = 1.0F;
                else {
                    Ve.sub(this.tailForStaying.Loc, this.Loc);
                    this.tailForStaying.Or.transform(this.tailOffset, tmpV3f);
                    Ve.add(tmpV3f);
                    float f14 = (float) Ve.z;
                    float f6 = 0.0050F * (200.0F - (float) Ve.length());
                    this.Or.transformInv(Ve);
                    float f10 = (float) Ve.x;
                    Ve.normalize();
                    f6 = Math.max(f6, (float) Ve.x);
                    if (f6 < 0.0F)
                        f6 = 0.0F;
                    Ve.set(this.Vwld);
                    Ve.normalize();
                    tmpV3f.set(this.tailForStaying.Vwld);
                    tmpV3f.normalize();
                    float f8 = (float) tmpV3f.dot(Ve);
                    if (f8 < 0.0F)
                        f8 = 0.0F;
                    f6 *= f8;
                    if (f6 > 0.0F && f10 < 1000.0F) {
                        if (f10 > 300.0F)
                            f10 = 300.0F;
                        float f17 = 0.6F;
                        if (this.tailForStaying.VmaxH == this.VmaxH)
                            f17 = this.tailForStaying.CT.getPower();
                        if (this.Vmax < 83.0F) {
                            float f19 = f14;
                            if (f19 > 0.0F) {
                                if (f19 > 20.0F)
                                    f19 = 20.0F;
                                this.Vwld.z += 0.01F * f19;
                            }
                        }
                        float f12;
                        if (f10 > 0.0F)
                            f12 = 0.0070F * f10 + 0.0050F * f14;
                        else
                            f12 = 0.03F * f10 + 0.0010F * f14;
                        float f20 = this.getSpeed() - this.tailForStaying.getSpeed();
                        float f22 = -0.3F * f20;
                        float f25 = -3.0F * (this.getForwAccel() - this.tailForStaying.getForwAccel());
                        if (f20 > 27.0F) {
                            this.CT.FlapsControl = 1.0F;
                            f1 = 0.0F;
                        } else {
                            this.CT.FlapsControl = 0.02F * f20 + 0.02F * -f10;
                            f1 = f17 + f12 + f22 + f25;
                        }
                    } else
                        f1 = 1.1F;
                }
                break;
            case 2:
                f1 = (float) (1.0 - (8.0E-5 * (0.5 * this.Vwld.lengthSquared() - 9.8 * Ve.z - 0.5 * this.tailForStaying.Vwld.lengthSquared())));
                break;
            case 3: {
                f1 = this.CT.PowerControl;
                if (this.AP.way.curr().Speed < 10.0F)
                    this.AP.way.curr().set(1.7F * this.Vmin);
                float f18 = this.AP.way.curr().Speed / this.VmaxH;
                f1 = 0.2F + 0.8F * (float) Math.pow(f18, 1.5);
                f1 += (0.1F * (this.AP.way.curr().Speed - Pitot.Indicator((float) this.Loc.z, this.getSpeed())) - 3.0F * this.getForwAccel());
                if (this.getAltitude() < this.AP.way.curr().z() - 70.0F)
                    f1 += this.AP.way.curr().z() - 70.0F - this.getAltitude();
                if (f1 > 0.93F)
                    f1 = 0.93F;
                if (f1 < 0.35F && !this.AP.way.isLanding())
                    f1 = 0.35F;
                break;
            }
            case 4:
                f1 = this.CT.PowerControl;
                f1 += ((f4 * (this.smConstSpeed - Pitot.Indicator((float) this.Loc.z, this.getSpeed())) - f5 * this.getLocalAccel().x / 9.8100004196167) * f);
                if (f1 > 1.0F)
                    f1 = 1.0F;
                break;
            case 5:
                f1 = this.CT.PowerControl;
                this.CT.FlapsControl = 1.0F;
                f1 += (f4 * (1.3F * this.VminFLAPS - Pitot.Indicator((float) this.Loc.z, this.getSpeed())) - f5 * this.getForwAccel()) * f;
                break;
            case 8:
                f1 = 0.0F;
                break;
            case 6:
                f1 = 1.0F;
                break;
            case 9:
                f1 = 1.1F;
                this.CT.setAfterburnerControl(true);
                break;
            case 7:
                f1 = this.smConstPower;
                break;
            case 10:
                if (this.tailForStaying == null)
                    f1 = 1.0F;
                else {
                    Ve.sub(this.tailForStaying.Loc, this.Loc);
                    this.tailForStaying.Or.transform(this.tailOffset, tmpV3f);
                    Ve.add(tmpV3f);
                    float f15 = (float) Ve.z;
                    float f7 = 0.0050F * (200.0F - (float) Ve.length());
                    this.Or.transformInv(Ve);
                    float f11 = (float) Ve.x;
                    Ve.normalize();
                    f7 = Math.max(f7, (float) Ve.x);
                    if (f7 < 0.0F)
                        f7 = 0.0F;
                    Ve.set(this.Vwld);
                    Ve.normalize();
                    tmpV3f.set(this.tailForStaying.Vwld);
                    tmpV3f.normalize();
                    float f9 = (float) tmpV3f.dot(Ve);
                    if (f9 < 0.0F)
                        f9 = 0.0F;
                    f7 *= f9;
                    if (f7 > 0.0F && f11 < 1000.0F) {
                        if (f11 > 300.0F)
                            f11 = 300.0F;
                        float f21 = 0.6F;
                        if (this.tailForStaying.VmaxH == this.VmaxH)
                            f21 = this.tailForStaying.CT.getPower();
                        if (this.Vmax < 83.0F) {
                            float f23 = f15;
                            if (f23 > 0.0F) {
                                if (f23 > 20.0F)
                                    f23 = 20.0F;
                                this.Vwld.z += 0.01F * f23;
                            }
                        }
                        float f13;
                        if (f11 > 0.0F)
                            f13 = 0.0070F * f11 + 0.0050F * f15;
                        else
                            f13 = 0.03F * f11 + 0.0010F * f15;
                        float f24 = this.getSpeed() - this.tailForStaying.getSpeed();
                        float f26 = -0.3F * f24;
                        float f27 = -3.0F * (this.getForwAccel() - this.tailForStaying.getForwAccel());
                        if (f24 > 27.0F) {
                            this.Vwld.scale(0.9800000190734863);
                            f1 = 0.0F;
                        } else {
                            float f28 = 1.0F - 0.02F * (0.02F * f24 + 0.02F * -f11);
                            if (f28 < 0.98F)
                                f28 = 0.98F;
                            if (f28 > 1.0F)
                                f28 = 1.0F;
                            this.Vwld.scale(f28);
                            f1 = f21 + f13 + f26 + f27;
                        }
                    } else
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
        if (Math.abs(this.CT.PowerControl - f1) < 0.5 * f)
            this.CT.setPowerControl(f1);
        else if (this.CT.PowerControl < f1)
            this.CT.setPowerControl(this.CT.getPowerControl() + 0.5F * f);
        else
            this.CT.setPowerControl(this.CT.getPowerControl() - 0.5F * f);
        float f16 = this.EI.engines[0].getCriticalW();
        if (this.EI.engines[0].getw() > 0.9F * f16) {
            float f2 = 10.0F * (f16 - this.EI.engines[0].getw()) / f16;
            if (f2 < this.CT.PowerControl)
                this.CT.setPowerControl(f2);
        }
        if (this.indSpeed > 0.8F * this.VmaxAllowed) {
            float f3 = 1.0F * (this.VmaxAllowed - this.indSpeed) / this.VmaxAllowed;
            if (f3 < this.CT.PowerControl)
                this.CT.setPowerControl(f3);
        }
    }

    private void setRandomTargDeviation(float f) {
        if (this.isTick(16, 0)) {
            float f1 = f * (8.0F - 1.5F * this.Skill);
            this.TargDevVnew.set(World.Rnd().nextFloat(-f1, f1), World.Rnd().nextFloat(-f1, f1), World.Rnd().nextFloat(-f1, f1));
        }
        this.TargDevV.interpolate(this.TargDevVnew, 0.01F);
    }

    private Point_Any getNextAirdromeWayPoint() {
        if (this.airdromeWay == null)
            return null;
        if (this.airdromeWay.length == 0)
            return null;
        if (this.curAirdromePoi >= this.airdromeWay.length)
            return null;
        return this.airdromeWay[this.curAirdromePoi++];
    }

    private void findPointForEmLanding(float f) {
        Point3d point3d = elLoc.getPoint();
        if (this.radius > 2.0 * this.rmax) {
            if (this.submaneuver != SUB_MAN69)
                this.initTargPoint(f);
            else
                return;
        }
        for (int i = 0; i < 32; i++) {
            Po.set(this.Vtarg.x + this.radius * Math.sin(this.phase), this.Vtarg.y + this.radius * Math.cos(this.phase), this.Loc.z);
            this.radius += 0.01 * this.rmax;
            this.phase += 0.3;
            Ve.sub(Po, this.Loc);
            double d = Ve.length();
            this.Or.transformInv(Ve);
            Ve.normalize();
            float f1 = 0.9F - 0.0050F * this.Alt;
            if (f1 < -1.0F)
                f1 = -1.0F;
            if (f1 > 0.8F)
                f1 = 0.8F;
            float f2 = 1.5F - 5.0E-4F * this.Alt;
            if (f2 < 1.0F)
                f2 = 1.0F;
            if (f2 > 1.5F)
                f2 = 1.5F;
            if (this.rmax > d && d > this.rmin * f2 && Ve.x > f1 && this.isConvenientPoint()) {
                this.submaneuver = SUB_MAN69;
                point3d.set(Po);
                this.pointQuality = this.curPointQuality;
                break;
            }
        }
    }

    private boolean isConvenientPoint() {
        Po.z = Engine.cur.land.HQ_Air(Po.x, Po.y);
        this.curPointQuality = 50;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (Engine.land().isWater(Po.x + 500.0 * i, Po.y + 500.0 * j)) {
                    if (!(this.actor instanceof TypeSailPlane))
                        this.curPointQuality--;
                } else if (this.actor instanceof TypeSailPlane)
                    this.curPointQuality--;
                if (Engine.cur.land.HQ_ForestHeightHere((Po.x + 500.0 * i), (Po.y + 500.0 * j)) > 1.0)
                    this.curPointQuality -= 2;
                if (Engine.cur.land.EQN(Po.x + 500.0 * i, Po.y + 500.0 * j, Ve) > 1110.949951171875)
                    this.curPointQuality -= 2;
            }
        }
        if (this.curPointQuality < 0)
            this.curPointQuality = 0;
        if (this.curPointQuality > this.pointQuality)
            return true;
        return false;
    }

    private void emergencyTurnToDirection(float f) {
        if (Math.abs(Ve.y) > 0.10000000149011612)
            this.CT.AileronControl = -(float) Math.atan2(Ve.y, Ve.z) - 0.016F * this.Or.getKren();
        else
            this.CT.AileronControl = -(float) Math.atan2(Ve.y, Ve.x) - 0.016F * this.Or.getKren();
        this.CT.RudderControl = -10.0F * (float) Math.atan2(Ve.y, Ve.x);
        if (this.CT.RudderControl * this.W.z > 0.0)
            this.W.z = 0.0;
        else
            this.W.z *= 1.0399999618530273;
    }

    private void initTargPoint(float f) {
        int i = this.AP.way.Cur();
        this.Vtarg.set(this.AP.way.last().x(), this.AP.way.last().y(), 0.0);
        this.AP.way.setCur(i);
        this.Vtarg.sub(this.Loc);
        this.Vtarg.z = 0.0;
        if (this.Vtarg.length() > this.rmax) {
            this.Vtarg.normalize();
            this.Vtarg.scale(this.rmax);
        }
        this.Vtarg.add(this.Loc);
        this.phase = 0.0;
        this.radius = 50.0;
        this.pointQuality = -1;
    }

    private void emergencyLanding(float f) {
        if (this.first) {
            this.Kmax = this.Wing.new_Cya(5.0F) / this.Wing.new_Cxa(5.0F);
            if (this.Kmax > 14.0F)
                this.Kmax = 14.0F;
            this.Kmax *= 0.8F;
            this.rmax = 1.2F * this.Kmax * this.Alt;
            this.rmin = 0.6F * this.Kmax * this.Alt;
            this.initTargPoint(f);
            this.setReadyToDieSoftly(true);
            this.AP.setStabAll(false);
            if (this.TaxiMode) {
                this.setSpeedMode(8);
                this.smConstPower = 0.0F;
                this.push(44);
                this.pop();
            }
            this.dist = this.Alt;
            if (this.actor instanceof TypeSailPlane)
                this.EI.setEngineStops();
        }
        this.setSpeedMode(4);
        this.smConstSpeed = this.VminFLAPS * 1.25F;
        if (this.Alt < 500.0F && (this.actor instanceof TypeGlider || this.actor instanceof TypeSailPlane))
            this.CT.GearControl = 1.0F;
        if (this.Alt < 10.0F) {
            this.CT.AileronControl = -0.04F * this.Or.getKren();
            this.setSpeedMode(4);
            this.smConstSpeed = this.VminFLAPS * 1.1F;
            if (this.Alt < 5.0F)
                this.setSpeedMode(8);
            this.CT.BrakeControl = 0.2F;
            if (this.Vwld.length() < 0.30000001192092896 && World.Rnd().nextInt(0, 99) < 4) {
                this.setStationedOnGround(true);
                World.cur();
                if (this.actor != World.getPlayerAircraft()) {
                    this.push(44);
                    this.safe_pop();
                    if (this.actor instanceof TypeSailPlane) {
                        this.EI.setCurControlAll(true);
                        this.EI.setEngineStops();
                        if (Engine.land().isWater(this.Loc.x, this.Loc.y))
                            return;
                    }
                    ((Aircraft) this.actor).hitDaSilk();
                }
                if (this.Group != null)
                    this.Group.delAircraft((Aircraft) this.actor);
                if (this.actor instanceof TypeGlider || this.actor instanceof TypeSailPlane)
                    return;
                World.cur();
                if (this.actor != World.getPlayerAircraft()) {
                    if (Airport.distToNearestAirport(this.Loc) > 900.0)
                        ((Aircraft) this.actor).postEndAction(60.0, this.actor, 4, null);
                    else
                        MsgDestroy.Post(Time.current() + 30000L, this.actor);
                }
            }
        }
        this.dA = 0.2F * (this.getSpeed() - this.Vmin * 1.3F) - 0.8F * (this.getAOA() - 5.0F);
        if (this.Alt < 40.0F) {
            this.CT.AileronControl = -0.04F * this.Or.getKren();
            this.CT.RudderControl = 0.0F;
            if (this.actor instanceof BI_1 || this.actor instanceof ME_163B1A)
                this.CT.GearControl = 1.0F;
            float f1;
            if (this.Gears.Pitch < 10.0F)
                f1 = (40.0F * (this.getSpeed() - this.VminFLAPS * 1.15F) - 60.0F * (this.Or.getTangage() + 3.0F) - 240.0F * (this.getVertSpeed() + 1.0F) - 120.0F * ((float) this.getAccel().z - 1.0F)) * 0.0040F;
            else
                f1 = (40.0F * (this.getSpeed() - this.VminFLAPS * 1.15F) - 60.0F * (this.Or.getTangage() - this.Gears.Pitch + 10.0F) - 240.0F * (this.getVertSpeed() + 1.0F) - 120.0F * ((float) this.getAccel().z - 1.0F)) * 0.0040F;
            if (this.Alt > 0.0F) {
                float f2 = 0.01666F * this.Alt;
                this.dA = this.dA * f2 + f1 * (1.0F - f2);
            } else
                this.dA = f1;
            this.CT.FlapsControl = 0.33F;
            if (this.Alt < 9.0F && Math.abs(this.Or.getKren()) < 5.0F && this.getVertSpeed() < -0.7F)
                this.Vwld.z *= 0.8700000047683716;
        } else {
            this.rmax = 1.2F * this.Kmax * this.Alt;
            this.rmin = 0.6F * this.Kmax * this.Alt;
            if (this.actor instanceof TypeGlider && this.Alt > 200.0F)
                this.CT.RudderControl = 0.0F;
            else if (this.pointQuality < 50 && this.mn_time > 0.5F)
                this.findPointForEmLanding(f);
            if (this.submaneuver == SUB_MAN69) {
                Ve.sub(elLoc.getPoint(), this.Loc);
                double d = Ve.length();
                this.Or.transformInv(Ve);
                Ve.normalize();
                float f3 = 0.9F - 0.0050F * this.Alt;
                if (f3 < -1.0F)
                    f3 = -1.0F;
                if (f3 > 0.8F)
                    f3 = 0.8F;
                if (this.rmax * 2.0F < d || d < this.rmin || Ve.x < f3) {
                    this.submaneuver = SUB_MAN0;
                    this.initTargPoint(f);
                }
                if (d > 88.0) {
                    this.emergencyTurnToDirection(f);
                    if (this.Alt > d)
                        this.submaneuver = SUB_MAN0;
                } else
                    this.CT.AileronControl = -0.04F * this.Or.getKren();
            } else
                this.CT.AileronControl = -0.04F * this.Or.getKren();
            if (this.Or.getTangage() > -1.0F)
                this.dA -= 0.1F * (this.Or.getTangage() + 1.0F);
            if (this.Or.getTangage() < -10.0F)
                this.dA -= 0.1F * (this.Or.getTangage() + 10.0F);
        }
        if (this.Alt < 2.0F || this.Gears.onGround())
            this.dA = -2.0F * (this.Or.getTangage() - this.Gears.Pitch);
        double d1 = this.Vwld.length() / this.Vmin;
        if (d1 > 1.0)
            d1 = 1.0;
        this.CT.ElevatorControl += ((this.dA - this.CT.ElevatorControl) * 3.33F * f + 1.5 * this.getW().y * d1 + 0.5 * this.getAW().y * d1);
    }

    public boolean canITakeoff() {
        Po.set(this.Loc);
        Vpl.set(69.0, 0.0, 0.0);
        this.Or.transform(Vpl);
        Po.add(Vpl);
        Pd.set(Po);
        if (this.actor != War.getNearestFriendAtPoint(Pd, (Aircraft) this.actor, 70.0F))
            return false;
        if (this.canTakeoff)
            return true;
        if (Actor.isAlive(this.AP.way.takeoffAirport)) {
            if (this.AP.way.takeoffAirport.takeoffRequest <= 0) {
                this.AP.way.takeoffAirport.takeoffRequest = 2000;
                this.canTakeoff = true;
                return true;
            }
            return false;
        }
        return true;
    }

    public void set_maneuver_imm(int i) {
        int j = this.maneuver;
        this.maneuver = i;
        if (j != this.maneuver)
            this.set_flags();
    }

    private void addWindCorrection() {
        do {
            if (World.cur().diffCur.Wind_N_Turbulence) {
                World.cur();
                if (!World.wind().noWind)
                    break;
            }
            return;
        } while (false);
        double d = Ve.length();
        World.cur();
        World.wind().getVectorAI(this.actor.pos.getAbsPoint(), this.windV);
        this.windV.scale(-1.0);
        Ve.normalize();
        Ve.scale(this.getSpeed());
        Ve.add(this.windV);
        Ve.normalize();
        Ve.scale(d);
    }

    // TODO: CTO Mod altered
    // ---------------------------------------------------
    protected void turnOnChristmasTree(boolean flag) {
        if (flag) {
            if (((com.maddox.JGP.Tuple3f) (com.maddox.il2.ai.World.Sun().ToSun)).z < -0.22F && !this.bNoNavLightsAI)
                super.AS.setNavLightsState(true);
        } else {
            super.AS.setNavLightsState(false);
        }
    }
    // ---------------------------------------------------
}
