/*4.10.1 file made compatible with UP*/
package com.maddox.il2.game;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.GUIWindowManager;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.fm.Turret;
import com.maddox.il2.gui.GUI;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.ActorViewPoint;
import com.maddox.il2.objects.air.A6M;
import com.maddox.il2.objects.air.A6M5C;
import com.maddox.il2.objects.air.A6M7_62;
import com.maddox.il2.objects.air.A_20;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.BLENHEIM;
import com.maddox.il2.objects.air.B_17;
import com.maddox.il2.objects.air.B_24;
import com.maddox.il2.objects.air.B_25;
import com.maddox.il2.objects.air.B_29X;
import com.maddox.il2.objects.air.CantZ1007;
import com.maddox.il2.objects.air.CantZ1007bis;
import com.maddox.il2.objects.air.Cockpit;
import com.maddox.il2.objects.air.CockpitGunner;
import com.maddox.il2.objects.air.CockpitPilot;
import com.maddox.il2.objects.air.DO_335;
import com.maddox.il2.objects.air.Do217;
import com.maddox.il2.objects.air.Do217_K1;
import com.maddox.il2.objects.air.Do217_K2;
import com.maddox.il2.objects.air.FW_200;
import com.maddox.il2.objects.air.HE_111H2;
import com.maddox.il2.objects.air.Halifax;
import com.maddox.il2.objects.air.Hurricane;
import com.maddox.il2.objects.air.IL_10;
import com.maddox.il2.objects.air.IL_2;
import com.maddox.il2.objects.air.IL_4;
import com.maddox.il2.objects.air.JU_88A4;
import com.maddox.il2.objects.air.JU_88C6;
import com.maddox.il2.objects.air.KI_21;
import com.maddox.il2.objects.air.LANCASTER;
import com.maddox.il2.objects.air.ME_210;
import com.maddox.il2.objects.air.ME_210CA1ZSTR;
import com.maddox.il2.objects.air.MOSQUITO;
import com.maddox.il2.objects.air.P2V;
import com.maddox.il2.objects.air.PE_2;
import com.maddox.il2.objects.air.PE_8;
import com.maddox.il2.objects.air.PZL37;
import com.maddox.il2.objects.air.P_51;
import com.maddox.il2.objects.air.Paratrooper;
import com.maddox.il2.objects.air.R_10;
import com.maddox.il2.objects.air.SB;
import com.maddox.il2.objects.air.SM79;
import com.maddox.il2.objects.air.SPITFIRE;
import com.maddox.il2.objects.air.SU_2;
import com.maddox.il2.objects.air.TBF;
import com.maddox.il2.objects.air.TB_3;
import com.maddox.il2.objects.air.TEMPEST;
import com.maddox.il2.objects.air.TU_2S;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeDiveBomber;
import com.maddox.il2.objects.air.TypeDockable;
import com.maddox.il2.objects.air.TypeFighterAceMaker;
import com.maddox.il2.objects.air.TypeHasToKG;
import com.maddox.il2.objects.air.TypeX4Carrier;
import com.maddox.il2.objects.air.YAK_9B;
import com.maddox.il2.objects.effects.ForceFeedback;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.TorpedoGun;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.HotKeyCmdEnv;
import com.maddox.rts.HotKeyCmdMouseMove;
import com.maddox.rts.HotKeyCmdMove;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Joy;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.RTSConf;
import com.maddox.rts.Time;
import com.maddox.sound.AudioDevice;
import com.maddox.sound.CmdMusic;
import com.maddox.sound.RadioChannel;

// TODO: Disabled for 410 compatibility
/*
 * import com.maddox.il2.objects.air.F84G1;
 * import com.maddox.il2.objects.air.F9F;
 * import com.maddox.il2.objects.air.Halifax;
 * import com.maddox.il2.objects.air.MOSQUITOS;
 * import com.maddox.il2.objects.air.P2V;
 * import com.maddox.il2.objects.air.P_51Mustang;
 * import com.maddox.il2.objects.air.SPITFIRE12;
 * import com.maddox.il2.objects.air.SPITFIRE14C;
 * import com.maddox.il2.objects.air.SPITFIRELF14E;
 */

public class AircraftHotKeys
{
	public static boolean							bFirstHotCmd				= false;
	private FlightModel								FM;
	private boolean									bPropAuto;
	private boolean									bAfterburner;
	private float									lastPower;
	private float									lastProp;
	private static final int						MOVE_POWER					= 1;
	private static final int						MOVE_FLAPS					= 2;
	private static final int						MOVE_AILERON				= 3;
	private static final int						MOVE_ELEVATOR				= 4;
	private static final int						MOVE_RUDDER					= 5;
	private static final int						MOVE_BRAKE					= 6;
	private static final int						MOVE_STEP					= 7;
	private static final int						MOVE_TRIMHOR				= 8;
	private static final int						MOVE_TRIMVER				= 9;
	private static final int						MOVE_TRIMRUD				= 10;
	private int										cptdmg;
	private static final int						BRAKE						= 0;
	private static final int						RUDDER_LEFT					= 1;
	private static final int						RUDDER_RIGHT				= 2;
	private static final int						ELEVATOR_UP					= 3;
	private static final int						ELEVATOR_DOWN				= 4;
	private static final int						AILERON_LEFT				= 5;
	private static final int						AILERON_RIGHT				= 6;
	private static final int						RADIATOR					= 7;
	private static final int						GEAR						= 9;
	private static final int						GUNPODS						= 15;
	private static final int						WEAPON0						= 16;
	private static final int						WEAPON1						= 17;
	private static final int						WEAPON2						= 18;
	private static final int						WEAPON3						= 19;
	private static final int						WEAPON01					= 64;
	private static final int						POWER_0						= 20;
	private static final int						POWER_1						= 21;
	private static final int						POWER_2						= 22;
	private static final int						POWER_3						= 23;
	private static final int						POWER_4						= 24;
	private static final int						POWER_5						= 25;
	private static final int						POWER_6						= 26;
	private static final int						POWER_7						= 27;
	private static final int						POWER_8						= 28;
	private static final int						POWER_9						= 29;
	private static final int						POWER_10					= 30;
	private static final int						STEP_0						= 31;
	private static final int						STEP_1						= 32;
	private static final int						STEP_2						= 33;
	private static final int						STEP_3						= 34;
	private static final int						STEP_4						= 35;
	private static final int						STEP_5						= 36;
	private static final int						STEP_6						= 37;
	private static final int						STEP_7						= 38;
	private static final int						STEP_8						= 39;
	private static final int						STEP_9						= 40;
	private static final int						STEP_10						= 41;
	private static final int						STEP_A						= 42;
	private static final int						AIRCRAFT_TRIM_V_0			= 43;
	private static final int						AIRCRAFT_TRIM_V_PLUS		= 44;
	private static final int						AIRCRAFT_TRIM_V_MINUS		= 45;
	private static final int						AIRCRAFT_TRIM_H_0			= 46;
	private static final int						AIRCRAFT_TRIM_H_PLUS		= 47;
	private static final int						AIRCRAFT_TRIM_H_MINUS		= 48;
	private static final int						AIRCRAFT_TRIM_R_0			= 49;
	private static final int						AIRCRAFT_TRIM_R_PLUS		= 50;
	private static final int						AIRCRAFT_TRIM_R_MINUS		= 51;
	private static final int						FLAPS_NOTCH_UP				= 52;
	private static final int						FLAPS_NOTCH_DOWN			= 53;
	private static final int						GEAR_UP_MANUAL				= 54;
	private static final int						GEAR_DOWN_MANUAL			= 55;
	private static final int						RUDDER_LEFT_1				= 56;
	private static final int						RUDDER_CENTRE				= 57;
	private static final int						RUDDER_RIGHT_1				= 58;
	private static final int						POWER_PLUS_5				= 59;
	private static final int						POWER_MINUS_5				= 60;
	private static final int						BOOST						= 61;
	private static final int						AIRCRAFT_DROP_TANKS			= 62;
	private static final int						AIRCRAFT_TOGGLE_AIRBRAKE	= 63;
	private static final int						AIRCRAFT_TOGGLE_ENGINE		= 70;
	private static final int						AIRCRAFT_STABILIZER			= 71;
	private static final int						AIRCRAFT_TAILWHEELLOCK		= 72;
	private static final int						STEP_PLUS_5					= 73;
	private static final int						STEP_MINUS_5				= 74;
	private static final int						MIX_0						= 75;
	private static final int						MIX_1						= 76;
	private static final int						MIX_2						= 77;
	private static final int						MIX_3						= 78;
	private static final int						MIX_4						= 79;
	private static final int						MIX_5						= 80;
	private static final int						MIX_6						= 81;
	private static final int						MIX_7						= 82;
	private static final int						MIX_8						= 83;
	private static final int						MIX_9						= 84;
	private static final int						MIX_10						= 85;
	private static final int						MIX_PLUS_20					= 86;
	private static final int						MIX_MINUS_20				= 87;
	private static final int						MAGNETO_PLUS_1				= 88;
	private static final int						MAGNETO_MINUS_1				= 89;
	private static final int						ENGINE_SELECT_ALL			= 90;
	private static final int						ENGINE_SELECT_NONE			= 91;
	private static final int						ENGINE_SELECT_LEFT			= 92;
	private static final int						ENGINE_SELECT_RIGHT			= 93;
	private static final int						ENGINE_SELECT_1				= 94;
	private static final int						ENGINE_SELECT_2				= 95;
	private static final int						ENGINE_SELECT_3				= 96;
	private static final int						ENGINE_SELECT_4				= 97;
	private static final int						ENGINE_SELECT_5				= 98;
	private static final int						ENGINE_SELECT_6				= 99;
	private static final int						ENGINE_SELECT_7				= 100;
	private static final int						ENGINE_SELECT_8				= 101;
	private static final int						ENGINE_TOGGLE_ALL			= 102;
	private static final int						ENGINE_TOGGLE_LEFT			= 103;
	private static final int						ENGINE_TOGGLE_RIGHT			= 104;
	private static final int						ENGINE_TOGGLE_1				= 105;
	private static final int						ENGINE_TOGGLE_2				= 106;
	private static final int						ENGINE_TOGGLE_3				= 107;
	private static final int						ENGINE_TOGGLE_4				= 108;
	private static final int						ENGINE_TOGGLE_5				= 109;
	private static final int						ENGINE_TOGGLE_6				= 110;
	private static final int						ENGINE_TOGGLE_7				= 111;
	private static final int						ENGINE_TOGGLE_8				= 112;
	private static final int						ENGINE_EXTINGUISHER			= 113;
	private static final int						ENGINE_FEATHER				= 114;
	private static final int						COMPRESSORSTEP_PLUS			= 115;
	private static final int						COMPRESSORSTEP_MINUS		= 116;
	private static final int						SIGHT_DIST_PLUS				= 117;
	private static final int						SIGHT_DIST_MINUS			= 118;
	private static final int						SIGHT_SIDE_RIGHT			= 119;
	private static final int						SIGHT_SIDE_LEFT				= 120;
	private static final int						SIGHT_ALT_PLUS				= 121;
	private static final int						SIGHT_ALT_MINUS				= 122;
	private static final int						SIGHT_SPD_PLUS				= 123;
	private static final int						SIGHT_SPD_MINUS				= 124;
	private static final int						SIGHT_AUTO_ONOFF			= 125;
	private static final int						AIRCRAFT_DOCK_UNDOCK		= 126;
	private static final int						WINGFOLD					= 127;
	private static final int						COCKPITDOOR					= 128;
	private static final int						AIRCRAFT_CARRIERHOOK		= 129;
	private static final int						AIRCRAFT_BRAKESHOE			= 130;
	public static int								hudLogPowerId;
	public static int								hudLogWeaponId;
	private boolean									bAutoAutopilot;
	private int										switchToCockpitRequest;
	private HotKeyCmd[]								cmdFov;
	private static Object[]							namedAll					= new Object[1];
	private static TreeMap							namedAircraft				= new TreeMap();
	private boolean									bMissionModsSet				= false;
	private boolean									bSpeedbarTAS				= false;
	private boolean									bSeparateGearUpDown			= false;
	private boolean									bSeparateHookUpDown			= false;
	private boolean									bSeparateRadiatorOpenClose	= false;
	private boolean									bToggleMusic				= false;
	private boolean									bViewExternalSelf			= false;
	private boolean									bViewExternalOnGround		= false;
	private boolean									bViewExternalWhenDead		= false;
	private boolean									bViewExternalFriendlies		= false;
	private boolean									bBombBayDoors				= true;
	private boolean									bMusicOn					= true;
	private int										iAirShowSmoke				= 0;
	private boolean									bAirShowSmokeEnhanced		= false;
	private boolean									bSideDoor					= true;
	private int										COCKPIT_DOOR				= 1;
	private int										SIDE_DOOR					= 2;
	private boolean									bAllowDumpFuel				= false;
	private boolean									bDumpFuel					= false;
	private boolean									bExtViewEnemy				= false;
	private boolean									bExtViewFriendly			= false;
	private boolean									bExtViewSelf				= false;
	private boolean									bExtViewGround				= false;
	private boolean									bExtViewDead				= false;
	private int										iExtViewGround				= -1;
	private int										iExtViewDead				= -1;
	private boolean									bPadlockEnemy				= false;
	private boolean									bPadlockFriendly			= false;
	private boolean									bExtPadlockEnemy			= false;
	private boolean									bExtPadlockFriendly			= false;
	private boolean									bMustangCompressorAuto		= true;

	private float									lastPower2;
	private float									lastPower1;
	private float									lastPower3;
	private float									lastPower4;
	private float									lastProp1;
	private float									lastProp2;
	private float									lastProp3;
	private float									lastProp4;
	private float									lastRadiator;
	private com.maddox.il2.objects.air.AircraftLH	bAAircraft;
	private com.maddox.il2.fm.FlightModel			baFM;
	private static final int						MOVE_POWER1					= 15;
	private static final int						MOVE_POWER2					= 16;
	private static final int						MOVE_POWER3					= 17;
	private static final int						MOVE_POWER4					= 18;
	private static final int						MOVE_RADIATOR				= 19;
	private static final int						MOVE_PROP1					= 20;
	private static final int						MOVE_PROP2					= 21;
	private static final int						MOVE_PROP3					= 22;
	private static final int						MOVE_PROP4					= 23;
	private static final int						MOVE_ZOOM					= 100;
	private boolean									changeFovEnabled;
	private static final int						BEACON_PLUS					= 139;
	private static final int						BEACON_MINUS				= 140;
	private static final int						AUX1_PLUS					= 149;
	private static final int						AUX1_MINUS					= 150;
	private static final int						AUX_A						= 157;

	class HotKeyCmdFire extends HotKeyCmd
	{
		int		cmd;
		long	time;

		public void begin()
		{
			AircraftHotKeys.this.doCmdPilot(cmd, true);
			time = Time.tick();
		}

		public void tick()
		{
			if (Time.tick() > time + 500L)
				AircraftHotKeys.this.doCmdPilotTick(cmd, true);
		}

		public boolean isTickInTime(boolean bool)
		{
			return !bool;
		}

		public void end()
		{
			AircraftHotKeys.this.doCmdPilot(cmd, false);
		}

		public boolean isDisableIfTimePaused()
		{
			return true;
		}

		public HotKeyCmdFire(String string, String string_0_, int i, int i_1_)
		{
			super(true, string_0_, string);
			cmd = i;
			setRecordId(i_1_);
		}
	}

	class HotKeyCmdFireMove extends HotKeyCmdMove
	{
		int		cmd;
		boolean	disableIfPaused;

		public void begin()
		{
			int i = name().charAt(0) != '-' ? 1 : -1;
			AircraftHotKeys.this.doCmdPilotMove(cmd, Joy.normal(i * move()));
		}

		public boolean isDisableIfTimePaused()
		{
			return disableIfPaused;
		}

		public HotKeyCmdFireMove(String string, String string_2_, int i, int i_3_)
		{
			super(true, string_2_, string);
			cmd = i;
			setRecordId(i_3_);
			disableIfPaused = true;
		}

		public HotKeyCmdFireMove(java.lang.String s, java.lang.String s1, int i, int j, boolean flag)
		{
			this(s, s1, i, j);
			disableIfPaused = flag;
		}
	}

	public boolean isAfterburner()
	{
		if (!Actor.isValid(World.getPlayerAircraft()))
			bAfterburner = false;
		return bAfterburner;
	}

	public void setAfterburner(boolean bool)
	{
		if (FM.EI.isSelectionHasControlAfterburner())
		{
			bAfterburner = bool;
			if (bAfterburner)
			{
				boolean bool_4_ = false;
				if (FM.actor instanceof Hurricane || (FM.actor instanceof A6M && !(FM.actor instanceof A6M7_62) && !(FM.actor instanceof A6M5C)) || FM.actor instanceof P_51
						|| FM.actor instanceof SPITFIRE || FM.actor instanceof MOSQUITO || FM.actor instanceof TEMPEST)
					bool_4_ = true;
				try
				{
					//TODO: Disabled for 410 compatibility
					/*
					 * if (FM.actor instanceof MOSQUITOS)
					 * bool_4_ = true;
					 */
				}
				catch (Throwable throwable)
				{
					/* empty */
				}
				if (bool_4_)
					HUD.logRightBottom("BoostWepTP0");
				else
					HUD.logRightBottom("BoostWepTP" + FM.EI.getFirstSelected().getAfterburnerType());
			}
			else
				HUD.logRightBottom(null);
		}
		FM.CT.setAfterburnerControl(bAfterburner);
	}

	public void setAfterburnerForAutoActivation(boolean bool)
	{
		bAfterburner = bool;
	}

	public boolean isPropAuto()
	{
		if (!Actor.isValid(World.getPlayerAircraft()))
			bPropAuto = false;
		return bPropAuto;
	}

	public void setPropAuto(boolean bool)
	{
		if (!bool || FM.EI.isSelectionAllowsAutoProp())
			bPropAuto = bool;
	}

	public void resetGame()
	{
		FM = null;
		bAfterburner = false;
		bPropAuto = true;
		lastPower = -0.5F;
		lastPower1 = -0.5F;
		lastPower2 = -0.5F;
		lastPower3 = -0.5F;
		lastPower4 = -0.5F;
		lastProp = 1.5F;
		lastProp1 = 1.5F;
		lastProp2 = 1.5F;
		lastProp3 = 1.5F;
		lastProp4 = 1.5F;

		bDumpFuel = false;
		bMissionModsSet = false;
		bMustangCompressorAuto = true;
	}

	private boolean setBombAimerAircraft()
	{
		bAAircraft = null;
		if (!com.maddox.il2.engine.Actor.isAlive(com.maddox.il2.ai.World.getPlayerAircraft()))
			return false;
		if (com.maddox.il2.ai.World.isPlayerParatrooper())
			return false;
		if (com.maddox.il2.ai.World.isPlayerDead())
			return false;
		com.maddox.il2.fm.FlightModel flightmodel = com.maddox.il2.ai.World.getPlayerFM();
		if (flightmodel == null)
		{
			return false;
		}
		else
		{
			bAAircraft = (com.maddox.il2.objects.air.AircraftLH) flightmodel.actor;
			baFM = flightmodel;
			return true;
		}
	}

	private void setPowerControl(float f, int i)
	{
		if (f < 0.0F)
			f = 0.0F;
		if (f > 1.1F)
			f = 1.1F;
		FM.CT.setPowerControl(f, i - 1);
		hudPower(f);
	}

	private void setPropControl(float f, int i)
	{
		if (f < 0.0F)
			f = 0.0F;
		if (f > 1.0F)
			f = 1.0F;
		FM.CT.setStepControl(f, i - 1);
	}

	public void resetUser()
	{
		resetGame();
	}

	private void setRadiatorControl(float f)
	{
		if (f < 0.0F)
			f = 0.0F;
		if (f > 1.0F)
			f = 1.0F;
		if (!FM.EI.isSelectionHasControlRadiator())
			return;
		if (FM.CT.getRadiatorControlAuto())
		{
			if (f > 0.8F)
				return;
			FM.CT.setRadiatorControlAuto(false, FM.EI);
			if (com.maddox.il2.ai.World.cur().diffCur.ComplexEManagement)
			{
				FM.CT.setRadiatorControl(f);
				com.maddox.il2.game.HUD.log(hudLogPowerId, "Radiator {0}%", new java.lang.Object[] { new Integer(java.lang.Math.round(FM.CT.getRadiatorControl() * 100F)) });
			}
			else
			{
				FM.CT.setRadiatorControl(1.0F);
				com.maddox.il2.game.HUD.log("RadiatorON");
			}
			return;
		}
		if (com.maddox.il2.ai.World.cur().diffCur.ComplexEManagement)
		{
			if (FM.actor instanceof com.maddox.il2.objects.air.MOSQUITO)
			{
				if (f > 0.5F)
				{
					FM.CT.setRadiatorControl(1.0F);
					com.maddox.il2.game.HUD.log("RadiatorON");
				}
				else
				{
					FM.CT.setRadiatorControl(0.0F);
					com.maddox.il2.game.HUD.log("RadiatorOFF");
				}
			}
			else
			{
				FM.CT.setRadiatorControl(f);
				com.maddox.il2.game.HUD.log(hudLogPowerId, "Radiator {0}%", new java.lang.Object[] { new Integer(java.lang.Math.round(FM.CT.getRadiatorControl() * 100F)) });
			}
		}
		else
		{
			FM.CT.setRadiatorControlAuto(true, FM.EI);
			com.maddox.il2.game.HUD.log("RadiatorOFF");
		}
	}

	private boolean setPilot()
	{
		FM = null;
		if (!Actor.isAlive(World.getPlayerAircraft()))
		{
			// System.out.println("setPilot returning false - 1");
			return false;
		}
		if (World.isPlayerParatrooper())
		{
			// System.out.println("setPilot returning false - 2");
			return false;
		}
		if (World.isPlayerDead())
		{
			// System.out.println("setPilot returning false - 3");
			return false;
		}
		com.maddox.il2.fm.FlightModel flightmodel = World.getPlayerFM();
		if (flightmodel == null)
		{
			// System.out.println("setPilot returning false - 4");
			return false;
		}
		if (flightmodel instanceof RealFlightModel)
		{
			FM = (RealFlightModel) flightmodel;
			return ((RealFlightModel) flightmodel).isRealMode();
		}
		// TODO: Added by |ZUTI| - changed to enable gunners as bombardier.
		// -----------------------------------------------------------------
		else if (ZutiSupportMethods.IS_ACTING_INSTRUCTOR || Main3D.cur3D().cockpitCur instanceof CockpitPilot)
		{
			// System.out.println("AicraftHotKeys - player can operate the plane he is in!");
			FM = flightmodel;
			return true;
		}
		// -----------------------------------------------------------------
		else
		{
			return false;
		}
	}

	private void setPowerControl(float f)
	{
		if (f < 0.0F)
			f = 0.0F;
		if (f > 1.1F)
			f = 1.1F;
		lastPower = f;
		FM.CT.setPowerControl(f);
		hudPower(FM.CT.PowerControl);
	}

	private void setPropControl(float f)
	{
		if (World.cur().diffCur.ComplexEManagement)
		{
			if (f < 0.0F)
				f = 0.0F;
			if (f > 1.0F)
				f = 1.0F;
			lastProp = f;
			if (!FM.EI.isSelectionAllowsAutoProp())
				bPropAuto = false;
			if (!bPropAuto)
			{
				FM.CT.setStepControlAuto(false);
				FM.CT.setStepControl(f);
				HUD.log(hudLogPowerId, "PropPitch", (new Object[] { new Integer(Math.round(FM.CT.getStepControl() * 100.0F)) }));
			}
		}
	}

	private void setMixControl(float f)
	{
		if (World.cur().diffCur.ComplexEManagement)
		{
			if (f < 0.0F)
				f = 0.0F;
			if (f > 1.2F)
				f = 1.2F;
			if (FM.EI.getFirstSelected() != null)
			{
				FM.EI.setMix(f);
				f = FM.EI.getFirstSelected().getControlMix();
				FM.CT.setMixControl(f);
				HUD.log(hudLogPowerId, "PropMix", (new Object[] { new Integer(Math.round(FM.CT.getMixControl() * 100.0F)) }));
			}
		}
	}

	private void hudPower(float f)
	{
		HUD.log(hudLogPowerId, "Power", new Object[] { new Integer(Math.round(f * 100.0F)) });
	}

	private void hudWeapon(boolean bool, int weaponId)
	{
		String weaponName = "";

		boolean hasBullets = false;
		BulletEmitter[] bulletemitters = FM.CT.Weapons[weaponId];
		if (bulletemitters != null)
		{
			for (int i = 0; i < bulletemitters.length; i++)
			{
				if (bulletemitters[i] != null && bulletemitters[i].haveBullets())
				{
					hasBullets = true;

					// TODO: Added by |ZUTI|
					weaponName = bulletemitters[i].toString();

					break;
				}
			}
			if (!bool)
				ForceFeedback.fxTriggerShake(weaponId, false);
			else if (hasBullets)
			{
				ForceFeedback.fxTriggerShake(weaponId, true);

				// TODO: Added by |ZUTI|: 3 = bombs or other ordinance
				if (weaponId == 3)
				{
					ZutiWeaponsManagement.processDropCargoEvent(weaponName);
				}
			}
			else
				HUD.log(hudLogWeaponId, "OutOfAmmo");
		}
	}

	private void setMissionMods()
	{
		bExtViewEnemy = false;
		bExtViewFriendly = false;
		bExtViewSelf = false;
		bExtViewGround = false;
		bExtViewDead = false;
		iExtViewGround = -1;
		iExtViewDead = -1;
		bPadlockEnemy = false;
		bPadlockFriendly = false;
		bExtPadlockEnemy = false;
		bExtPadlockFriendly = false;
		bViewExternalOnGround = false;
		bViewExternalWhenDead = false;
		bViewExternalFriendlies = false;
		if (Mission.cur().sectFile() != null)
		{
			if (Mission.cur().sectFile().get("Mods", "ViewExternalSelf", 0) > 0)
				bViewExternalSelf = true;
			if (Mission.cur().sectFile().get("Mods", "ViewExternalOnGround", 0) > 0)
				bViewExternalOnGround = true;
			if (Mission.cur().sectFile().get("Mods", "ViewExternalWhenDead", 0) > 0)
				bViewExternalWhenDead = true;
			if (Mission.cur().sectFile().get("Mods", "ViewExternalFriendlies", 0) > 0)
				bViewExternalFriendlies = true;
			int i = Mission.cur().sectFile().get("Mods", "ExternalViewLevel", -1);
			if (i == 2)
			{
				bExtViewEnemy = true;
				bExtViewFriendly = true;
				bExtViewSelf = true;
			}
			else if (i == 1 || bViewExternalFriendlies)
			{
				bExtViewFriendly = true;
				bExtViewSelf = true;
			}
			else if (i == 0 || bViewExternalSelf)
				bExtViewSelf = true;
			iExtViewGround = Mission.cur().sectFile().get("Mods", "ExternalViewGround", -1);
			if (iExtViewGround == 1 || bViewExternalOnGround)
				bExtViewGround = true;
			iExtViewDead = Mission.cur().sectFile().get("Mods", "ExternalViewDead", -1);
			if (iExtViewDead == 1 || bViewExternalWhenDead)
				bExtViewDead = true;
			int i_10_ = Mission.cur().sectFile().get("Mods", "PadlockLevel", -1);
			if (i_10_ == 2)
			{
				bPadlockEnemy = true;
				bPadlockFriendly = true;
			}
			else if (i_10_ == 1)
				bPadlockFriendly = true;
			int i_11_ = Mission.cur().sectFile().get("Mods", "ExternalPadlockLevel", -1);
			if (i_11_ == 2)
			{
				bExtPadlockEnemy = true;
				bExtPadlockFriendly = true;
			}
			else if (i_11_ == 1)
				bExtPadlockFriendly = true;
			bMissionModsSet = true;
		}
	}

	private boolean viewAllowed(boolean bool)
	{
		if (!World.cur().diffCur.No_Outside_Views)
			return true;
		if (!bool)
			return false;
		if (bool && iExtViewGround == -1 && iExtViewDead == -1)
			return true;
		if (bExtViewGround && Actor.isValid(World.getPlayerAircraft()) && FM.Gears.onGround())
			return true;
		return bExtViewDead && (World.isPlayerParatrooper() || World.isPlayerDead() || FM.isReadyToDie() || !Actor.isValid(World.getPlayerAircraft()));
	}

	private boolean hasBayDoors()
	{
		boolean bool = false;
		if ((Aircraft) FM.actor instanceof A_20 || (Aircraft) FM.actor instanceof B_17 || (Aircraft) FM.actor instanceof B_24 || (Aircraft) FM.actor instanceof B_25
				|| (Aircraft) FM.actor instanceof B_29X || (Aircraft)FM.actor instanceof Halifax
				|| (Aircraft) FM.actor instanceof BLENHEIM || (Aircraft) FM.actor instanceof DO_335 || (Aircraft) FM.actor instanceof MOSQUITO || (Aircraft) FM.actor instanceof HE_111H2
				|| (Aircraft) FM.actor instanceof IL_10 || (Aircraft) FM.actor instanceof JU_88A4 || ((Aircraft) FM.actor instanceof ME_210 && !((Aircraft) FM.actor instanceof ME_210CA1ZSTR))
				|| (Aircraft) FM.actor instanceof PE_2 || (Aircraft) FM.actor instanceof PE_8 || (Aircraft) FM.actor instanceof R_10 || (Aircraft) FM.actor instanceof SB
				|| (Aircraft) FM.actor instanceof SU_2 || (Aircraft) FM.actor instanceof TB_3 || (Aircraft) FM.actor instanceof IL_2 || (Aircraft) FM.actor instanceof IL_4
				|| (Aircraft) FM.actor instanceof FW_200 || (Aircraft) FM.actor instanceof KI_21 || (Aircraft) FM.actor instanceof YAK_9B || (Aircraft) FM.actor instanceof TU_2S
				|| (Aircraft) FM.actor instanceof TBF || (Aircraft) FM.actor instanceof CantZ1007 || (Aircraft) FM.actor instanceof Do217_K1 || (Aircraft) FM.actor instanceof Halifax 
				|| (Aircraft) FM.actor instanceof JU_88C6 || (Aircraft) FM.actor instanceof MOSQUITO || (Aircraft) FM.actor instanceof LANCASTER || (Aircraft) FM.actor instanceof PZL37
				|| (Aircraft) FM.actor instanceof SM79 || (Aircraft) FM.actor instanceof CantZ1007bis || (Aircraft) FM.actor instanceof CantZ1007 || (Aircraft) FM.actor instanceof Do217_K1 
				|| (Aircraft) FM.actor instanceof Do217 || (Aircraft) FM.actor instanceof Do217_K2)
			bool = true;
		try
		{
			if ((Aircraft) FM.actor instanceof SM79)
				bool = true;
		}
		catch (Throwable throwable)
		{
			/* empty */
		}
		try
		{
			if ((Aircraft)FM.actor instanceof P2V)
				bool = true;
		}
		catch (Throwable throwable)
		{
			/* empty */
		}
		return bool;
	}

	// TODO: Changed by |ZUTI|: from private to public.
	public void doCmdPilot(int i, boolean bool)
	{
		if (!setBombAimerAircraft())
			return;
		if (bool)
		{
			switch (i)
			{
				case 139:
				case 140:
				case 149:
				case 150:
					doCmdPilotTick(i, bool);
					return;

				case 157:
					bAAircraft.auxPressed(1);
					return;
			}
		}
		if (setPilot())
		{
			Aircraft aircraft = (Aircraft) FM.actor;

			// TODO: Added by |ZUTI|
			// ---------------------------------------------------------------------------
			if (!ZutiSupportMethods_Multicrew.canExecutePilotOrBombardierAction(aircraft, i, bool))
				return;
			// ---------------------------------------------------------------------------

			boolean bool_12_ = false;
			boolean bool_13_ = false;
			switch (i)
			{
				case 16:
					FM.CT.WeaponControl[0] = bool;
					hudWeapon(bool, 0);
				break;
				case 17:
					FM.CT.WeaponControl[1] = bool;
					hudWeapon(bool, 1);
				break;
				case 18:
					FM.CT.WeaponControl[2] = bool;
					hudWeapon(bool, 2);
				break;
				case 19:
					if (bBombBayDoors && hasBayDoors())
						FM.CT.bHasBayDoors = true;

					if ((aircraft instanceof TypeHasToKG) && FM.CT.Weapons[3] != null && (FM.CT.Weapons[3][0] instanceof TorpedoGun) && FM.CT.Weapons[3][0].haveBullets())
					{
						FM.AS.replicateGyroAngleToNet();
						FM.AS.replicateSpreadAngleToNet();
					}

					// TODO: Added by |ZUTI|
					// ----------------------------------------------------------------
					// Can we drop bombs at all? If not, send data to the one that can!
					if (ZutiSupportMethods_Multicrew.mustSyncACOperation((Aircraft) FM.actor))
					{
						// Each key press executes this command twice. That means that on the client end
						// user receives TWO bomb release commands which is not desirable.
						if (zutiBombReleaseCounter > 0)
						{
							Aircraft ac = (Aircraft) FM.actor;
							// Bomb drop must be synced
							ZutiSupportMethods_NetSend.bombardierReleasedOrdinance_ToServer(ac.name(), true, FM.CT.bHasBayDoors);
							zutiBombReleaseCounter = 0;
							return;
						}
						zutiBombReleaseCounter++;
					}
					// ----------------------------------------------------------------
					else
						FM.CT.WeaponControl[3] = bool;

					hudWeapon(bool, 3);
				break;
				case 64:
					FM.CT.WeaponControl[0] = bool;
					hudWeapon(bool, 0);
					FM.CT.WeaponControl[1] = bool;
					hudWeapon(bool, 1);
				break;
				case 73: // 'I'
					FM.CT.setElectricPropUp(bool);
				break;

				case 74: // 'J'
					FM.CT.setElectricPropDn(bool);
				break;
			}
			if (!bool)
			{
				switch (i)
				{
					default:
					break;
					case 71:
						//TODO: Modified to ensure 410 compatibility
						if (aircraft instanceof TypeBomber || aircraft instanceof DO_335 /*
																						 * ||
																						 * aircraft
																						 * instanceof
																						 * F84G1
																						 * ||
																						 * aircraft
																						 * instanceof
																						 * F84G1
																						 */)
						{
							FM.CT.StabilizerControl = !FM.CT.StabilizerControl;
							HUD.log("Stabilizer" + (FM.CT.StabilizerControl ? "On" : "Off"));
						}
					break;
					case 15:
						if (aircraft.isGunPodsExist())
						{
							if (aircraft.isGunPodsOn())
							{
								aircraft.setGunPodsOn(false);
								HUD.log("GunPodsOff");
							}
							else
							{
								aircraft.setGunPodsOn(true);
								HUD.log("GunPodsOn");
							}
							break;
						}
					break;
					case 1:
					case 2:
						if (!FM.CT.StabilizerControl)
							FM.CT.RudderControl = 0.0F;
					break;
					case 0:
						FM.CT.BrakeControl = 0.0F;
					break;
					case 3:
					case 4:
						if (!FM.CT.StabilizerControl)
							FM.CT.ElevatorControl = 0.0F;
					break;
					case 5:
					case 6:
						if (!FM.CT.StabilizerControl)
							FM.CT.AileronControl = 0.0F;
					break;
					case 54:
						if (!FM.Gears.onGround() && !(FM.CT.GearControl <= 0.0F) && FM.Gears.isOperable() && !FM.Gears.isHydroOperable())
						{
							FM.CT.GearControl -= 0.02F;
							if (FM.CT.GearControl <= 0.0F)
							{
								FM.CT.GearControl = 0.0F;
								HUD.log("GearUp");
							}
						}
					break;
					case 55:
						if (!FM.Gears.onGround() && !(FM.CT.GearControl >= 1.0F) && FM.Gears.isOperable() && !FM.Gears.isHydroOperable())
						{
							FM.CT.GearControl += 0.02F;
							if (FM.CT.GearControl >= 1.0F)
							{
								FM.CT.GearControl = 1.0F;
								HUD.log("GearDown");
							}
						}
					break;
					case 63:
						if (FM.CT.bHasAirBrakeControl)
						{
							FM.CT.AirBrakeControl = FM.CT.AirBrakeControl <= 0.5F ? 1.0F : 0.0F;
							HUD.log("Divebrake" + (FM.CT.AirBrakeControl != 0.0F ? "ON" : "OFF"));
						}
					break;
					case 70:
						if (!World.cur().diffCur.SeparateEStart || FM.EI.getNumSelected() <= 1 || FM.EI.getFirstSelected().getStage() != 0)
						{
							FM.EI.toggle();
							break;
						}
					break;
					case 126:
						if (FM.actor instanceof TypeDockable)
						{
							if (((TypeDockable) FM.actor).typeDockableIsDocked())
								((TypeDockable) FM.actor).typeDockableAttemptDetach();
							else
								((TypeDockable) FM.actor).typeDockableAttemptAttach();
						}
				}
			}
			else
			{
				switch (i)
				{
					default:
					break;
					case 7:
						if (bSeparateRadiatorOpenClose)
						{
							if (FM.EI.isSelectionHasControlRadiator())
							{
								if (FM.CT.getRadiatorControlAuto())
								{
									FM.CT.setRadiatorControlAuto(false, FM.EI);
									if (World.cur().diffCur.ComplexEManagement)
									{
										FM.CT.setRadiatorControl(0.0F);
										HUD.log("RadiatorControl" + (int) (FM.CT.getRadiatorControl() * 10.0F));
									}
									else
									{
										FM.CT.setRadiatorControl(1.0F);
										HUD.log("RadiatorON");
									}
								}
								else if (World.cur().diffCur.ComplexEManagement)
								{
									if (FM.CT.getRadiatorControl() != 1.0F)
									{
										if (FM.actor instanceof MOSQUITO)
											FM.CT.setRadiatorControl(1.0F);
										else
											FM.CT.setRadiatorControl(FM.CT.getRadiatorControl() + 0.2F);
										HUD.log("RadiatorControl" + (int) (FM.CT.getRadiatorControl() * 10.0F));
									}
								}
								else
								{
									FM.CT.setRadiatorControlAuto(true, FM.EI);
									HUD.log("RadiatorOFF");
								}
							}
						}
						else if (FM.EI.isSelectionHasControlRadiator())
						{
							if (FM.CT.getRadiatorControlAuto())
							{
								FM.CT.setRadiatorControlAuto(false, FM.EI);
								if (World.cur().diffCur.ComplexEManagement)
								{
									FM.CT.setRadiatorControl(0.0F);
									HUD.log("RadiatorControl" + (int) (FM.CT.getRadiatorControl() * 10.0F));
								}
								else
								{
									FM.CT.setRadiatorControl(1.0F);
									HUD.log("RadiatorON");
								}
							}
							else if (World.cur().diffCur.ComplexEManagement)
							{
								if (FM.CT.getRadiatorControl() == 1.0F)
								{
									if (FM.EI.isSelectionAllowsAutoRadiator())
									{
										FM.CT.setRadiatorControlAuto(true, FM.EI);
										HUD.log("RadiatorOFF");
									}
									else
									{
										FM.CT.setRadiatorControl(0.0F);
										HUD.log("RadiatorControl" + (int) (FM.CT.getRadiatorControl() * 10.0F));
									}
								}
								else
								{
									if (FM.actor instanceof MOSQUITO)
										FM.CT.setRadiatorControl(1.0F);
									else
										FM.CT.setRadiatorControl(FM.CT.getRadiatorControl() + 0.2F);
									HUD.log("RadiatorControl" + (int) (FM.CT.getRadiatorControl() * 10.0F));
								}
							}
							else
							{
								FM.CT.setRadiatorControlAuto(true, FM.EI);
								HUD.log("RadiatorOFF");
							}
						}
					break;
					case 0:
						FM.CT.BrakeControl = 1.0F;
					break;
					case 3:
						if (!FM.CT.StabilizerControl)
							FM.CT.ElevatorControl = -1.0F;
					break;
					case 4:
						if (!FM.CT.StabilizerControl)
							FM.CT.ElevatorControl = 1.0F;
					break;
					case 5:
						if (!FM.CT.StabilizerControl)
							FM.CT.AileronControl = -1.0F;
					break;
					case 6:
						if (!FM.CT.StabilizerControl)
							FM.CT.AileronControl = 1.0F;
					break;
					case 72:
						if (FM.CT.bHasLockGearControl)
						{
							FM.Gears.bTailwheelLocked = !FM.Gears.bTailwheelLocked;
							HUD.log("TailwheelLock" + (FM.Gears.bTailwheelLocked ? "ON" : "OFF"));
						}
					break;
					case 1:
						if (!FM.CT.StabilizerControl)
							FM.CT.RudderControl = -1.0F;
					break;
					case 56:
						if (!FM.CT.StabilizerControl && FM.CT.RudderControl > -1.0F)
							FM.CT.RudderControl -= 0.1F;
					break;
					case 57:
						if (!FM.CT.StabilizerControl)
							FM.CT.RudderControl = 0.0F;
					break;
					case 2:
						if (!FM.CT.StabilizerControl)
							FM.CT.RudderControl = 1.0F;
					break;
					case 58:
						if (!FM.CT.StabilizerControl && FM.CT.RudderControl < 1.0F)
							FM.CT.RudderControl += 0.1F;
					break;
					case 20:
						setPowerControl(0.0F);
					break;
					// TODO: Added by |ZUTI| for Bomb bay door mod:
					// ----------------------------------------------
					case 131:
					{
						if (bBombBayDoors)
						{
							if (hasBayDoors())
							{
								FM.CT.bHasBayDoors = true;
								if (FM.CT.BayDoorControl != 0.0F)
								{
									// TODO: Added by |ZUTI| - notify users that AC closed it's bomb bay doors
									if (ZutiSupportMethods_Multicrew.mustSyncACOperation(aircraft))
									{
										// Bomb drop must be synced
										ZutiSupportMethods_NetSend.bombBayDoorsStatus(0, World.getPlayerAircraft().name());
										// System.out.println("AircraftHotKeys - synced bomb bay doors to 0");
									}
									else
										FM.CT.BayDoorControl = 0.0F;

									HUD.log("BayDoorsClosed");
								}
								else
								{
									// TODO: Added by |ZUTI| - notify users that AC closed it's bomb bay doors
									if (ZutiSupportMethods_Multicrew.mustSyncACOperation(aircraft))
									{
										// Bomb drop must be synced
										ZutiSupportMethods_NetSend.bombBayDoorsStatus(1, World.getPlayerAircraft().name());
										// System.out.println("AircraftHotKeys - synced bomb bay doors to 1");
									}
									else
										FM.CT.BayDoorControl = 1.0F;

									HUD.log("BayDoorsOpen");
								}
							}
						}
						break;
					}
						// ----------------------------------------------
					case 21:
						setPowerControl(0.1F);
					break;
					case 22:
						if (bToggleMusic)
						{
							if (bMusicOn)
							{
								CmdMusic.setCurrentVolume(0.0F);
								bMusicOn = false;
							}
							else
							{
								CmdMusic.setCurrentVolume(1.0F);
								bMusicOn = true;
							}
						}
						else
							setPowerControl(0.2F);
					break;
					case 23:
						if (bSeparateRadiatorOpenClose)
						{
							if (FM.EI.isSelectionHasControlRadiator() && !FM.CT.getRadiatorControlAuto())
							{
								if (World.cur().diffCur.ComplexEManagement)
								{
									if (FM.CT.getRadiatorControl() == 0.0F)
									{
										if (FM.EI.isSelectionAllowsAutoRadiator())
										{
											FM.CT.setRadiatorControlAuto(true, FM.EI);
											HUD.log("RadiatorOFF");
										}
									}
									else
									{
										if (FM.actor instanceof MOSQUITO)
											FM.CT.setRadiatorControl(0.0F);
										else
											FM.CT.setRadiatorControl(FM.CT.getRadiatorControl() - 0.2F);
										HUD.log("RadiatorControl" + (int) (FM.CT.getRadiatorControl() * 10.0F));
									}
								}
								else
								{
									FM.CT.setRadiatorControlAuto(true, FM.EI);
									HUD.log("RadiatorOFF");
								}
							}
						}
						else
							setPowerControl(0.3F);
					break;
					case 24:
						if (bSideDoor)
						{
							boolean bool_14_ = false;
							try
							{
								//TODO: Disabled for 410 compatibility
								/*
								 * if ((Aircraft)FM.actor instanceof
								 * SPITFIRELF14E)
								 * bool_14_ = true;
								 */
							}
							catch (Throwable throwable)
							{
								/* empty */
							}
							try
							{
								//TODO: Disabled for 410 compatibility
								/*
								 * if ((Aircraft)FM.actor instanceof
								 * SPITFIRE14C)
								 * bool_14_ = true;
								 */
							}
							catch (Throwable throwable)
							{
								/* empty */
							}
							try
							{
								//TODO: Disabled for 410 compatibility
								/*
								 * if ((Aircraft)FM.actor instanceof SPITFIRE12)
								 * bool_14_ = true;
								 */
							}
							catch (Throwable throwable)
							{
								/* empty */
							}
							if (bool_14_)
							{
								FM.CT.setActiveDoor(SIDE_DOOR);
								if (FM.CT.cockpitDoorControl < 0.5F && FM.CT.getCockpitDoor() < 0.01F)
									FM.AS.setCockpitDoor(aircraft, 1);
								else if (FM.CT.cockpitDoorControl > 0.5F && FM.CT.getCockpitDoor() > 0.99F)
									FM.AS.setCockpitDoor(aircraft, 0);
							}
						}
						else
							setPowerControl(0.4F);
					break;
					case 25:
						if (bAllowDumpFuel)
						{
							//TODO: Disabled for 410 compatibility
							/*
							 * if ((Aircraft)FM.actor instanceof F9F)
							 * {
							 * if (bDumpFuel)
							 * bDumpFuel = false;
							 * else
							 * bDumpFuel = true;
							 * //TODO: Not available!?
							 * //FM.AS.setDumpFuelState(bDumpFuel);
							 * }
							 */
						}
						else
							setPowerControl(0.5F);
					break;
					case 26:
						setPowerControl(0.6F);
					break;
					case 27:
						setPowerControl(0.7F);
					break;
					case 28:
						setPowerControl(0.8F);
					break;
					case 29:
						setPowerControl(0.9F);
					break;
					case 30:
						setPowerControl(1.0F);
					break;
					case 59:
						setPowerControl(FM.CT.PowerControl + 0.05F);
					break;
					case 60:
						setPowerControl(FM.CT.PowerControl - 0.05F);
					break;
					case 61:
						setAfterburner(!bAfterburner);
					break;
					case 31:
						if (FM.EI.isSelectionHasControlProp())
							setPropControl(0.0F);
					break;
					case 32:
						if (FM.EI.isSelectionHasControlProp())
							setPropControl(0.1F);
					break;
					case 33:
						if (FM.EI.isSelectionHasControlProp())
							setPropControl(0.2F);
					break;
					case 34:
						if (FM.EI.isSelectionHasControlProp())
							setPropControl(0.3F);
					break;
					case 35:
						if (FM.EI.isSelectionHasControlProp())
							setPropControl(0.4F);
					break;
					case 36:
						if (FM.EI.isSelectionHasControlProp())
							setPropControl(0.5F);
					break;
					case 37:
						if (FM.EI.isSelectionHasControlProp())
							setPropControl(0.6F);
					break;
					case 38:
						if (FM.EI.isSelectionHasControlProp())
							setPropControl(0.7F);
					break;
					case 39:
						if (FM.EI.isSelectionHasControlProp())
							setPropControl(0.8F);
					break;
					case 40:
						if (FM.EI.isSelectionHasControlProp())
							setPropControl(0.9F);
					break;
					case 41:
						if (FM.EI.isSelectionHasControlProp())
							setPropControl(1.0F);
					break;
					case 73:
						if (FM.EI.isSelectionHasControlProp())
							setPropControl(lastProp + 0.05F);
					break;
					case 74:
						if (FM.EI.isSelectionHasControlProp())
							setPropControl(lastProp - 0.05F);
					break;
					case 42:
						if (FM.EI.isSelectionHasControlProp() && World.cur().diffCur.ComplexEManagement)
						{
							setPropAuto(!bPropAuto);
							if (bPropAuto)
							{
								HUD.log("PropAutoPitch");
								lastProp = FM.CT.getStepControl();
								FM.CT.setStepControlAuto(true);
							}
							else
							{
								FM.CT.setStepControlAuto(false);
								setPropControl(lastProp);
							}
						}
					break;
					case 114:
						if (FM.EI.isSelectionHasControlFeather() && World.cur().diffCur.ComplexEManagement && FM.EI.getFirstSelected() != null)
							FM.EI.setFeather(FM.EI.getFirstSelected().getControlFeather() != 0 ? 0 : 1);
					break;
					case 75:
						if (FM.EI.isSelectionHasControlMix())
							setMixControl(0.0F);
					break;
					case 76:
						if (FM.EI.isSelectionHasControlMix())
							setMixControl(0.1F);
					break;
					case 77:
						if (FM.EI.isSelectionHasControlMix())
							setMixControl(0.2F);
					break;
					case 78:
						if (FM.EI.isSelectionHasControlMix())
							setMixControl(0.3F);
					break;
					case 79:
						if (FM.EI.isSelectionHasControlMix())
							setMixControl(0.4F);
					break;
					case 80:
						if (FM.EI.isSelectionHasControlMix())
							setMixControl(0.5F);
					break;
					case 81:
						if (FM.EI.isSelectionHasControlMix())
							setMixControl(0.6F);
					break;
					case 82:
						if (FM.EI.isSelectionHasControlMix())
							setMixControl(0.7F);
					break;
					case 83:
						if (FM.EI.isSelectionHasControlMix())
							setMixControl(0.8F);
					break;
					case 84:
						if (FM.EI.isSelectionHasControlMix())
							setMixControl(0.9F);
					break;
					case 85:
						if (FM.EI.isSelectionHasControlMix())
							setMixControl(1.0F);
					break;
					case 86:
						if (FM.EI.isSelectionHasControlMix())
							setMixControl(FM.CT.getMixControl() + 0.2F);
					break;
					case 87:
						if (FM.EI.isSelectionHasControlMix())
							setMixControl(FM.CT.getMixControl() - 0.2F);
					break;
					case 89:
						if (!bSeparateGearUpDown)
						{
							if (FM.EI.isSelectionHasControlMagnetos() && FM.EI.getFirstSelected() != null && (FM.EI.getFirstSelected().getControlMagnetos() > 0))
							{
								FM.CT.setMagnetoControl(FM.EI.getFirstSelected().getControlMagnetos() - 1);
								HUD.log("MagnetoSetup" + FM.CT.getMagnetoControl());
							}
						}
						else if (FM.CT.bHasGearControl && !FM.Gears.onGround() && FM.Gears.isHydroOperable())
						{
							if (FM.CT.GearControl > 0.5F && FM.CT.getGear() > 0.99F)
							{
								FM.CT.GearControl = 0.0F;
								HUD.log("GearUp");
							}
							if (FM.Gears.isAnyDamaged())
								HUD.log("GearDamaged");
						}
					break;
					case 88:
						if (!bSeparateHookUpDown)
						{
							if (FM.EI.isSelectionHasControlMagnetos() && FM.EI.getFirstSelected() != null && (FM.EI.getFirstSelected().getControlMagnetos() < 3))
							{
								FM.CT.setMagnetoControl(FM.EI.getFirstSelected().getControlMagnetos() + 1);
								HUD.log("MagnetoSetup" + FM.CT.getMagnetoControl());
							}
						}
						else if (FM.CT.bHasArrestorControl && FM.CT.arrestorControl > 0.5F)
						{
							FM.AS.setArrestor(FM.actor, 0);
							HUD.log("HookUp");
						}
					break;
					case 116:
						try
						{
							//TODO: Disabled for 410 compatibility
							/*
							 * if (FM.actor instanceof P_51Mustang &&
							 * World.cur().diffCur.ComplexEManagement)
							 * {
							 * if (!bMustangCompressorAuto)
							 * {
							 * if (FM.CT.getCompressorControl() == 0)
							 * {
							 * FM.EI.getFirstSelected().bHasCompressorControl =
							 * false;
							 * bMustangCompressorAuto = true;
							 * HUD.log("Supercharger: Auto");
							 * }
							 * else
							 * {
							 * 
							 * FM.CT.setCompressorControl(FM.EI.getFirstSelected(
							 * ).getControlCompressor() - 1);
							 * HUD.log("CompressorSetup" +
							 * FM.CT.getCompressorControl());
							 * }
							 * }
							 * bool_13_ = true;
							 * }
							 */
						}
						catch (Throwable throwable)
						{
							/* empty */
						}
						if (!bool_13_ && FM.EI.isSelectionHasControlCompressor() && FM.EI.getFirstSelected() != null && World.cur().diffCur.ComplexEManagement)
						{
							FM.CT.setCompressorControl(FM.EI.getFirstSelected().getControlCompressor() - 1);
							HUD.log("CompressorSetup" + FM.CT.getCompressorControl());
						}
					break;
					case 115:
						try
						{
							//TODO: Disabled for 410 compatibility
							/*
							 * if (FM.actor instanceof P_51Mustang &&
							 * World.cur().diffCur.ComplexEManagement)
							 * {
							 * if (bMustangCompressorAuto)
							 * {
							 * FM.EI.getFirstSelected().bHasCompressorControl =
							 * true;
							 * FM.CT.setCompressorControl(0);
							 * bMustangCompressorAuto = false;
							 * HUD.log("CompressorSetup0");
							 * }
							 * else
							 * {
							 * 
							 * FM.CT.setCompressorControl(FM.EI.getFirstSelected(
							 * ).getControlCompressor() + 1);
							 * HUD.log("CompressorSetup" +
							 * FM.CT.getCompressorControl());
							 * }
							 * bool_13_ = true;
							 * }
							 */
						}
						catch (Throwable throwable)
						{
							/* empty */
						}
						if (!bool_13_ && FM.EI.isSelectionHasControlCompressor() && FM.EI.getFirstSelected() != null && World.cur().diffCur.ComplexEManagement)
						{
							FM.CT.setCompressorControl(FM.EI.getFirstSelected().getControlCompressor() + 1);
							HUD.log("CompressorSetup" + FM.CT.getCompressorControl());
						}
					break;
					case 90:
						if (FM.Scheme != 0 && FM.Scheme != 1)
						{
							FM.EI.setCurControlAll(true);
							HUD.log("EngineSelectAll");
							break;
						}
					break;
					case 91:
						if (FM.Scheme != 0 && FM.Scheme != 1)
						{
							FM.EI.setCurControlAll(false);
							HUD.log("EngineSelectNone");
							break;
						}
					break;
					case 92:
						if (FM.Scheme != 0 && FM.Scheme != 1)
						{
							FM.EI.setCurControlAll(false);
							int[] is = FM.EI.getSublist(FM.Scheme, 1);
							for (int i_15_ = 0; i_15_ < is.length; i_15_++)
								FM.EI.setCurControl(is[i_15_], true);
							HUD.log("EngineSelectLeft");
							break;
						}
					break;
					case 93:
						if (FM.Scheme != 0 && FM.Scheme != 1)
						{
							FM.EI.setCurControlAll(false);
							int[] is = FM.EI.getSublist(FM.Scheme, 2);
							for (int i_16_ = 0; i_16_ < is.length; i_16_++)
								FM.EI.setCurControl(is[i_16_], true);
							HUD.log("EngineSelectRight");
							break;
						}
					break;
					case 94:
						if (FM.Scheme != 0 && FM.Scheme != 1)
						{
							FM.EI.setCurControlAll(false);
							FM.EI.setCurControl(0, true);
							HUD.log("EngineSelect1");
							break;
						}
					break;
					case 95:
						if (FM.Scheme != 0 && FM.Scheme != 1)
						{
							FM.EI.setCurControlAll(false);
							FM.EI.setCurControl(1, true);
							HUD.log("EngineSelect2");
							break;
						}
					break;
					case 96:
						if (FM.Scheme != 0 && FM.Scheme != 1 && FM.EI.getNum() >= 3)
						{
							FM.EI.setCurControlAll(false);
							FM.EI.setCurControl(2, true);
							HUD.log("EngineSelect3");
							break;
						}
					break;
					case 97:
						if (FM.Scheme != 0 && FM.Scheme != 1 && FM.EI.getNum() >= 4)
						{
							FM.EI.setCurControlAll(false);
							FM.EI.setCurControl(3, true);
							HUD.log("EngineSelect4");
							break;
						}
					break;
					case 98:
						if (FM.Scheme != 0 && FM.Scheme != 1 && FM.EI.getNum() >= 5)
						{
							FM.EI.setCurControlAll(false);
							FM.EI.setCurControl(4, true);
							HUD.log("EngineSelect5");
							break;
						}
					break;
					case 99:
						if (FM.Scheme != 0 && FM.Scheme != 1 && FM.EI.getNum() >= 6)
						{
							FM.EI.setCurControlAll(false);
							FM.EI.setCurControl(5, true);
							HUD.log("EngineSelect6");
							break;
						}
					break;
					case 102:
						if (FM.Scheme != 0 && FM.Scheme != 1)
						{
							for (int i_17_ = 0; i_17_ < FM.EI.getNum(); i_17_++)
								FM.EI.setCurControl(i_17_, !FM.EI.getCurControl(i_17_));
							HUD.log("EngineToggleAll");
							break;
						}
					break;
					case 103:
						if (FM.Scheme != 0 && FM.Scheme != 1)
						{
							int[] is = FM.EI.getSublist(FM.Scheme, 1);
							for (int i_18_ = 0; i_18_ < is.length; i_18_++)
								FM.EI.setCurControl(is[i_18_], !FM.EI.getCurControl(is[i_18_]));
							HUD.log("EngineToggleLeft");
							break;
						}
					break;
					case 104:
						if (FM.Scheme != 0 && FM.Scheme != 1)
						{
							int[] is = FM.EI.getSublist(FM.Scheme, 2);
							for (int i_19_ = 0; i_19_ < is.length; i_19_++)
								FM.EI.setCurControl(is[i_19_], !FM.EI.getCurControl(is[i_19_]));
							HUD.log("EngineToggleRight");
							break;
						}
					break;
					case 105:
						if (FM.Scheme != 0 && FM.Scheme != 1)
						{
							FM.EI.setCurControl(0, !FM.EI.getCurControl(0));
							HUD.log("EngineSelect1" + (FM.EI.getCurControl(0) ? "" : "OFF"));
							break;
						}
					break;
					case 106:
						if (FM.Scheme != 0 && FM.Scheme != 1)
						{
							FM.EI.setCurControl(1, !FM.EI.getCurControl(1));
							HUD.log("EngineSelect2" + (FM.EI.getCurControl(1) ? "" : "OFF"));
							break;
						}
					break;
					case 107:
						if (FM.Scheme != 0 && FM.Scheme != 1)
						{
							FM.EI.setCurControl(2, !FM.EI.getCurControl(2));
							HUD.log("EngineSelect3" + (FM.EI.getCurControl(2) ? "" : "OFF"));
							break;
						}
					break;
					case 108:
						if (FM.Scheme != 0 && FM.Scheme != 1)
						{
							FM.EI.setCurControl(3, !FM.EI.getCurControl(3));
							HUD.log("EngineSelect4" + (FM.EI.getCurControl(3) ? "" : "OFF"));
							break;
						}
					break;
					case 109:
						if (FM.Scheme != 0 && FM.Scheme != 1)
						{
							FM.EI.setCurControl(4, !FM.EI.getCurControl(4));
							HUD.log("EngineSelect5" + (FM.EI.getCurControl(4) ? "" : "OFF"));
							break;
						}
					break;
					case 110:
						if (FM.Scheme != 0 && FM.Scheme != 1)
						{
							FM.EI.setCurControl(5, !FM.EI.getCurControl(5));
							HUD.log("EngineSelect6" + (FM.EI.getCurControl(5) ? "" : "OFF"));
							break;
						}
					break;
					case 113:
						if (FM.EI.isSelectionHasControlExtinguisher())
						{
							for (int i_20_ = 0; i_20_ < FM.EI.getNum(); i_20_++)
							{
								if (FM.EI.getCurControl(i_20_))
									FM.EI.engines[i_20_].setExtinguisherFire();
							}
						}
					break;
					case 53:
						if (FM.CT.bHasFlapsControl)
						{
							if (!FM.CT.bHasFlapsControlRed)
							{
								if (FM.CT.FlapsControl < 0.2F)
								{
									FM.CT.FlapsControl = 0.2F;
									HUD.log("FlapsCombat");
								}
								else if (FM.CT.FlapsControl < 0.33F)
								{
									FM.CT.FlapsControl = 0.33F;
									HUD.log("FlapsTakeOff");
								}
								else if (FM.CT.FlapsControl < 1.0F)
								{
									FM.CT.FlapsControl = 1.0F;
									HUD.log("FlapsLanding");
								}
							}
							else if (FM.CT.FlapsControl < 0.5F)
							{
								FM.CT.FlapsControl = 1.0F;
								HUD.log("FlapsLanding");
							}
						}
					break;
					case 52:
						if (FM.CT.bHasFlapsControl)
						{
							if (!FM.CT.bHasFlapsControlRed)
							{
								if (FM.CT.FlapsControl > 0.33F)
								{
									FM.CT.FlapsControl = 0.33F;
									HUD.log("FlapsTakeOff");
								}
								else if (FM.CT.FlapsControl > 0.2F)
								{
									FM.CT.FlapsControl = 0.2F;
									HUD.log("FlapsCombat");
								}
								else if (FM.CT.FlapsControl > 0.0F)
								{
									FM.CT.FlapsControl = 0.0F;
									HUD.log("FlapsRaised");
								}
							}
							else if (FM.CT.FlapsControl > 0.5F)
							{
								FM.CT.FlapsControl = 0.0F;
								HUD.log("FlapsRaised");
							}
						}
					break;
					case 9:
						if (FM.CT.bHasGearControl && !FM.Gears.onGround() && FM.Gears.isHydroOperable())
						{
							if (FM.CT.GearControl > 0.5F && FM.CT.getGear() > 0.99F && !bSeparateGearUpDown)
							{
								FM.CT.GearControl = 0.0F;
								HUD.log("GearUp");
							}
							else if (FM.CT.GearControl < 0.5F && FM.CT.getGear() < 0.01F)
							{
								FM.CT.GearControl = 1.0F;
								HUD.log("GearDown");
							}
							if (FM.Gears.isAnyDamaged())
								HUD.log("GearDamaged");
						}
					break;
					case 129:
						if (FM.CT.bHasArrestorControl)
						{
							if (FM.CT.arrestorControl > 0.5F && !bSeparateHookUpDown)
							{
								FM.AS.setArrestor(FM.actor, 0);
								HUD.log("HookUp");
							}
							else
							{
								FM.AS.setArrestor(FM.actor, 1);
								HUD.log("HookDown");
							}
						}
					break;
					case 130:
						if (FM.canChangeBrakeShoe)
						{
							if (FM.brakeShoe)
							{
								FM.brakeShoe = false;
								HUD.log("BrakeShoeOff");
							}
							else
							{
								FM.brakeShoe = true;
								HUD.log("BrakeShoeOn");
							}
						}
					break;
					case 127:
						if (FM.CT.bHasWingControl)
						{
							if (FM.CT.wingControl < 0.5F && FM.CT.getWing() < 0.01F)
							{
								FM.AS.setWingFold(aircraft, 1);
								HUD.log("WingFold");
							}
							else if (FM.CT.wingControl > 0.5F && FM.CT.getWing() > 0.99F)
							{
								FM.AS.setWingFold(aircraft, 0);
								HUD.log("WingExpand");
							}
						}
					break;
					case 128:
						if (FM.CT.bHasCockpitDoorControl)
						{
							if (bSideDoor)
							{
								boolean bool_21_ = false;
								try
								{
									//TODO: Disabled for 410 compatibility
									/*
									 * if ((Aircraft)FM.actor instanceof
									 * SPITFIRELF14E)
									 * bool_21_ = true;
									 */
								}
								catch (Throwable throwable)
								{
									/* empty */
								}
								try
								{
									//TODO: Disabled for 410 compatibility
									/*
									 * if ((Aircraft)FM.actor instanceof
									 * SPITFIRE14C)
									 * bool_21_ = true;
									 */
								}
								catch (Throwable throwable)
								{
									/* empty */
								}
								try
								{
									//TODO: Disabled for 410 compatibility
									/*
									 * if ((Aircraft)FM.actor instanceof
									 * SPITFIRE12)
									 * bool_21_ = true;
									 */
								}
								catch (Throwable throwable)
								{
									/* empty */
								}
								if (bool_21_)
									FM.CT.setActiveDoor(COCKPIT_DOOR);
							}
							if (FM.CT.cockpitDoorControl < 0.5F && FM.CT.getCockpitDoor() < 0.01F)
							{
								FM.AS.setCockpitDoor(aircraft, 1);
								HUD.log("CockpitDoorOPN");
							}
							else if (FM.CT.cockpitDoorControl > 0.5F && FM.CT.getCockpitDoor() > 0.99F)
							{
								FM.AS.setCockpitDoor(aircraft, 0);
								HUD.log("CockpitDoorCLS");
							}
						}
					break;
					case 43:
						if (FM.CT.bHasElevatorTrim)
							FM.CT.setTrimElevatorControl(0.0F);
					break;
					case 44:
						doCmdPilotTick(i, false);
					break;
					case 45:
						doCmdPilotTick(i, false);
					break;
					case 46:
						if (FM.CT.bHasAileronTrim)
							FM.CT.setTrimAileronControl(0.0F);
					break;
					case 47:
						doCmdPilotTick(i, false);
					break;
					case 48:
						doCmdPilotTick(i, false);
					break;
					case 49:
						if (FM.CT.bHasRudderTrim)
							FM.CT.setTrimRudderControl(0.0F);
					break;
					case 50:
						doCmdPilotTick(i, false);
					break;
					case 51:
						doCmdPilotTick(i, false);
					break;
					case 125:
						if (aircraft instanceof TypeBomber)
						{
							// TODO: Added by |ZUTI|: assigning result to designated variable
							aircraft.FM.CT.zutiSetBombsightAutomationEngaged(((TypeBomber) aircraft).typeBomberToggleAutomation());
							toTrackSign(i);
						}
						else if (aircraft instanceof TypeDiveBomber)
						{
							((TypeDiveBomber) aircraft).typeDiveBomberToggleAutomation();
							toTrackSign(i);
						}
						else if (aircraft instanceof TypeFighterAceMaker)
						{
							((TypeFighterAceMaker) aircraft).typeFighterAceMakerToggleAutomation();
							toTrackSign(i);
						}
					break;
					case 117:
						doCmdPilotTick(i, false);
					break;
					case 118:
						doCmdPilotTick(i, false);
					break;
					case 119:
						doCmdPilotTick(i, false);
					break;
					case 120:
						doCmdPilotTick(i, false);
					break;
					case 121:
						doCmdPilotTick(i, false);
					break;
					case 122:
						doCmdPilotTick(i, false);
					break;
					case 123:
						doCmdPilotTick(i, false);
					break;
					case 124:
						doCmdPilotTick(i, false);
					break;
					case 62:
						FM.CT.dropFuelTanks();
				}
			}
		}
	}

	// TODO: Added by |ZUTI|: added boolean zutiCheckCommand to avoid doubling of
	// canExecuteBombardierAction check...
	private void doCmdPilotTick(int i, boolean zutiCheckCommand)
	{
		// System.out.println("AircraftHotKeys - key pressed: " + i);
		if (!setBombAimerAircraft())
			return;
		switch (i)
		{
			case 149:
				bAAircraft.auxPlus(1);
				toTrackSign(i);
				return;

			case 150:
				bAAircraft.auxMinus(1);
				toTrackSign(i);
				return;

			case 139:
				bAAircraft.beaconPlus();
				toTrackSign(i);
				return;

			case 140:
				bAAircraft.beaconMinus();
				toTrackSign(i);
				return;
		}

		if (setPilot())
		{
			Aircraft aircraft = (Aircraft) FM.actor;

			// TODO: Added by |ZUTI|: if checkCommand parameter is true then check it and process
			if (zutiCheckCommand && !ZutiSupportMethods_Multicrew.canExecutePilotOrBombardierAction(aircraft, i, false))
				return;

			switch (i)
			{
				default:
				break;
				case 44:
					if (FM.CT.bHasElevatorTrim && FM.CT.getTrimElevatorControl() < 0.5F)
						FM.CT.setTrimElevatorControl(FM.CT.getTrimElevatorControl() + 0.00625F);
				break;
				case 45:
					if (FM.CT.bHasElevatorTrim && FM.CT.getTrimElevatorControl() > -0.5F)
						FM.CT.setTrimElevatorControl(FM.CT.getTrimElevatorControl() - 0.00625F);
				break;
				case 47:
					if (FM.CT.bHasAileronTrim && FM.CT.getTrimAileronControl() < 0.5F)
						FM.CT.setTrimAileronControl(FM.CT.getTrimAileronControl() + 0.00625F);
				break;
				case 48:
					if (FM.CT.bHasAileronTrim && FM.CT.getTrimAileronControl() > -0.5F)
						FM.CT.setTrimAileronControl(FM.CT.getTrimAileronControl() - 0.00625F);
				break;
				case 50:
					if (FM.CT.bHasRudderTrim && FM.CT.getTrimRudderControl() < 0.5F)
						FM.CT.setTrimRudderControl(FM.CT.getTrimRudderControl() + 0.00625F);
				break;
				case 51:
					if (FM.CT.bHasRudderTrim && FM.CT.getTrimRudderControl() > -0.5F)
						FM.CT.setTrimRudderControl(FM.CT.getTrimRudderControl() - 0.00625F);
				break;
				case 117:
					if (aircraft instanceof TypeBomber)
					{
						((TypeBomber) aircraft).typeBomberAdjDistancePlus();
						toTrackSign(i);
					}
					else if (aircraft instanceof TypeFighterAceMaker)
					{
						((TypeFighterAceMaker) aircraft).typeFighterAceMakerAdjDistancePlus();
						toTrackSign(i);
					}
					else if (aircraft instanceof TypeDiveBomber)
					{
						((TypeDiveBomber) aircraft).typeDiveBomberAdjDiveAnglePlus();
						toTrackSign(i);
					}
				break;
				case 118:
					if (aircraft instanceof TypeBomber)
					{
						((TypeBomber) aircraft).typeBomberAdjDistanceMinus();
						toTrackSign(i);
					}
					else if (aircraft instanceof TypeFighterAceMaker)
					{
						((TypeFighterAceMaker) aircraft).typeFighterAceMakerAdjDistanceMinus();
						toTrackSign(i);
					}
					else if (aircraft instanceof TypeDiveBomber)
					{
						((TypeDiveBomber) aircraft).typeDiveBomberAdjDiveAngleMinus();
						toTrackSign(i);
					}
				break;
				case 119:
					if (aircraft instanceof TypeBomber)
					{
						((TypeBomber) aircraft).typeBomberAdjSideslipPlus();
						toTrackSign(i);
					}
					if (aircraft instanceof TypeFighterAceMaker)
					{
						((TypeFighterAceMaker) aircraft).typeFighterAceMakerAdjSideslipPlus();
						toTrackSign(i);
					}
					if (aircraft instanceof TypeX4Carrier)
					{
						((TypeX4Carrier) aircraft).typeX4CAdjSidePlus();
						toTrackSign(i);
					}
				break;
				case 120:
					if (aircraft instanceof TypeBomber)
					{
						((TypeBomber) aircraft).typeBomberAdjSideslipMinus();
						toTrackSign(i);
					}
					if (aircraft instanceof TypeFighterAceMaker)
					{
						((TypeFighterAceMaker) aircraft).typeFighterAceMakerAdjSideslipMinus();
						toTrackSign(i);
					}
					if (aircraft instanceof TypeX4Carrier)
					{
						((TypeX4Carrier) aircraft).typeX4CAdjSideMinus();
						toTrackSign(i);
					}
				break;
				case 121:
					if (aircraft instanceof TypeBomber)
					{
						((TypeBomber) aircraft).typeBomberAdjAltitudePlus();
						toTrackSign(i);
					}
					else if (aircraft instanceof TypeDiveBomber)
					{
						((TypeDiveBomber) aircraft).typeDiveBomberAdjAltitudePlus();
						toTrackSign(i);
					}
					if (aircraft instanceof TypeX4Carrier)
					{
						((TypeX4Carrier) aircraft).typeX4CAdjAttitudePlus();
						toTrackSign(i);
					}
				break;
				case 122:
					if (aircraft instanceof TypeBomber)
					{
						((TypeBomber) aircraft).typeBomberAdjAltitudeMinus();
						toTrackSign(i);
					}
					else if (aircraft instanceof TypeDiveBomber)
					{
						((TypeDiveBomber) aircraft).typeDiveBomberAdjAltitudeMinus();
						toTrackSign(i);
					}
					if (aircraft instanceof TypeX4Carrier)
					{
						((TypeX4Carrier) aircraft).typeX4CAdjAttitudeMinus();
						toTrackSign(i);
					}
				break;
				case 123:
					if (aircraft instanceof TypeBomber)
					{
						((TypeBomber) aircraft).typeBomberAdjSpeedPlus();
						toTrackSign(i);
					}
					else if (aircraft instanceof TypeDiveBomber)
					{
						((TypeDiveBomber) aircraft).typeDiveBomberAdjVelocityPlus();
						toTrackSign(i);
					}
				break;
				case 124:
					if (aircraft instanceof TypeBomber)
					{
						((TypeBomber) aircraft).typeBomberAdjSpeedMinus();
						toTrackSign(i);
					}
					else if (aircraft instanceof TypeDiveBomber)
					{
						((TypeDiveBomber) aircraft).typeDiveBomberAdjVelocityMinus();
						toTrackSign(i);
					}
			}
		}
	}

	public void fromTrackSign(NetMsgInput netmsginput) throws IOException
	{
		if (Actor.isAlive(World.getPlayerAircraft()) && !World.isPlayerParatrooper() && !World.isPlayerDead())
		{
			if (World.getPlayerAircraft() instanceof TypeBomber)
			{
				TypeBomber typebomber = (TypeBomber) World.getPlayerAircraft();
				int i = netmsginput.readUnsignedShort();
				switch (i)
				{
					case 125:
						// TODO: Added by |ZUTI|: assigning result to designated variable
						World.getPlayerAircraft().FM.CT.zutiSetBombsightAutomationEngaged(typebomber.typeBomberToggleAutomation());
					break;
					case 117:
						typebomber.typeBomberAdjDistancePlus();
					break;
					case 118:
						typebomber.typeBomberAdjDistanceMinus();
					break;
					case 119:
						typebomber.typeBomberAdjSideslipPlus();
					break;
					case 120:
						typebomber.typeBomberAdjSideslipMinus();
					break;
					case 121:
						typebomber.typeBomberAdjAltitudePlus();
					break;
					case 122:
						typebomber.typeBomberAdjAltitudeMinus();
					break;
					case 123:
						typebomber.typeBomberAdjSpeedPlus();
					break;
					case 124:
						typebomber.typeBomberAdjSpeedMinus();
					break;
					default:
						return;
				}
			}
			if (World.getPlayerAircraft() instanceof TypeDiveBomber)
			{
				TypeDiveBomber typedivebomber = (TypeDiveBomber) World.getPlayerAircraft();
				int i = netmsginput.readUnsignedShort();
				switch (i)
				{
					case 125:
						typedivebomber.typeDiveBomberToggleAutomation();
					break;
					case 121:
						typedivebomber.typeDiveBomberAdjAltitudePlus();
					break;
					case 122:
						typedivebomber.typeDiveBomberAdjAltitudeMinus();
					break;
					default:
						return;
				}
			}
			if (World.getPlayerAircraft() instanceof TypeFighterAceMaker)
			{
				TypeFighterAceMaker typefighteracemaker = (TypeFighterAceMaker) World.getPlayerAircraft();
				int i = netmsginput.readUnsignedShort();
				switch (i)
				{
					case 125:
						typefighteracemaker.typeFighterAceMakerToggleAutomation();
					break;
					case 117:
						typefighteracemaker.typeFighterAceMakerAdjDistancePlus();
					break;
					case 118:
						typefighteracemaker.typeFighterAceMakerAdjDistanceMinus();
					break;
					case 119:
						typefighteracemaker.typeFighterAceMakerAdjSideslipPlus();
					break;
					case 120:
						typefighteracemaker.typeFighterAceMakerAdjSideslipMinus();
					break;
				}
			}
		}
	}

	private void toTrackSign(int i)
	{
		if (Main3D.cur3D().gameTrackRecord() != null)
		{
			try
			{
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(5);
				netmsgguaranted.writeShort(i);
				Main3D.cur3D().gameTrackRecord().postTo(Main3D.cur3D().gameTrackRecord().channel(), netmsgguaranted);
			}
			catch (Exception exception)
			{
				/* empty */
			}
		}
	}

	// TODO: Changed by |ZUTI|: changed from private to public
	public void doCmdPilotMove(int i, float f)
	{
		if (setPilot())
		{
			switch (i)
			{
				case 1:
				{
					float f_22_ = f * 0.55F + 0.55F;
					if (Math.abs(f_22_ - lastPower) >= 0.01F)
						setPowerControl(f_22_);
					break;
				}
				case 7:
				{
					float f_23_ = f * 0.5F + 0.5F;
					if (Math.abs(f_23_ - lastProp) >= 0.02F && FM.EI.isSelectionHasControlProp())
						setPropControl(f_23_);
					break;
				}
				case 2:
					if (!FM.CT.bHasFlapsControl)
						break;
					if (!FM.CT.bHasFlapsControlRed)
					{
						FM.CT.FlapsControl = f * 0.5F + 0.5F;
						break;
					}
					if (f < 0.0F)
					{
						FM.CT.FlapsControl = 0.0F;
						com.maddox.il2.game.HUD.log("FlapsRaised");
					}
					else
					{
						FM.CT.FlapsControl = 1.0F;
						com.maddox.il2.game.HUD.log("FlapsLanding");
					}
				break;
				case 3:
					if (!FM.CT.StabilizerControl)
						FM.CT.AileronControl = f;
				break;
				case 4:
					if (!FM.CT.StabilizerControl)
						FM.CT.ElevatorControl = f;
				break;
				case 5:
					if (!FM.CT.StabilizerControl)
						FM.CT.RudderControl = f;
				break;
				case 6:
					FM.CT.BrakeControl = f * 0.5F + 0.5F;
				break;
				case 8:
					if (FM.CT.bHasAileronTrim)
						FM.CT.setTrimAileronControl(f * 0.5F);
				break;
				case 9:
					if (FM.CT.bHasElevatorTrim)
						FM.CT.setTrimElevatorControl(f * 0.5F);
				break;
				case 10:
					if (FM.CT.bHasRudderTrim)
						FM.CT.setTrimRudderControl(f * 0.5F);
				break;
				// TAK++
				case 11: // BrakeRight
					FM.CT.BrakeRightControl = f * 0.5F + 0.5F;
					FM.CT.BrakeControl = (FM.CT.BrakeRightControl + FM.CT.BrakeLeftControl) / 2.0F;
				break;
				case 12: // BrakeLeft
					FM.CT.BrakeLeftControl = f * 0.5F + 0.5F;
					FM.CT.BrakeControl = (FM.CT.BrakeRightControl + FM.CT.BrakeLeftControl) / 2.0F;
				break;
				// TAK--
				case 100: // 'd'
					if (changeFovEnabled)
					{
						f = (f * 0.5F + 0.5F) * 60F + 30F;
						com.maddox.rts.CmdEnv.top().exec("fov " + f);
					}
				break;

				case 15: // '\017'
					float f3 = f * 0.55F + 0.55F;
					if (java.lang.Math.abs(f3 - lastPower1) >= 0.01F)
					{
						lastPower1 = f3;
						setPowerControl(f3, 1);
					}
				break;

				case 16: // '\020'
					float f4 = f * 0.55F + 0.55F;
					if (java.lang.Math.abs(f4 - lastPower2) >= 0.01F)
					{
						lastPower2 = f4;
						setPowerControl(f4, 2);
					}
				break;

				case 17: // '\021'
					float f5 = f * 0.55F + 0.55F;
					if (java.lang.Math.abs(f5 - lastPower3) >= 0.01F)
					{
						lastPower3 = f5;
						setPowerControl(f5, 3);
					}
				break;

				case 18: // '\022'
					float f6 = f * 0.55F + 0.55F;
					if (java.lang.Math.abs(f6 - lastPower4) >= 0.01F)
					{
						lastPower4 = f6;
						setPowerControl(f6, 4);
					}
				break;

				case 19: // '\023'
					float f7 = f * 0.5F + 0.5F;
					if (java.lang.Math.abs(f7 - lastRadiator) >= 0.02F)
					{
						lastRadiator = f7;
						setRadiatorControl(f7);
					}
				break;

				case 20: // '\024'
					float f8 = f * 0.5F + 0.5F;
					if (java.lang.Math.abs(f8 - lastProp1) >= 0.02F && 0 < FM.EI.getNum() && FM.EI.engines[0].isHasControlProp())
						setPropControl(f8, 1);
				break;

				case 21: // '\025'
					float f9 = f * 0.5F + 0.5F;
					if (java.lang.Math.abs(f9 - lastProp2) >= 0.02F && 1 < FM.EI.getNum() && FM.EI.engines[1].isHasControlProp())
						setPropControl(f9, 2);
				break;

				case 22: // '\026'
					float f10 = f * 0.5F + 0.5F;
					if (java.lang.Math.abs(f10 - lastProp3) >= 0.02F && 2 < FM.EI.getNum() && FM.EI.engines[2].isHasControlProp())
						setPropControl(f10, 3);
				break;

				case 23: // '\027'
					float f11 = f * 0.5F + 0.5F;
					if (java.lang.Math.abs(f11 - lastProp4) >= 0.02F && 3 < FM.EI.getNum() && FM.EI.engines[3].isHasControlProp())
						setPropControl(f11, 4);
				break;
				default:
					return;
			}

			// TODO: Added by |ZUTI|: send data over to the new only if mover is not owner of RealFlightModel
			if (!(FM instanceof RealFlightModel))
				ZutiSupportMethods_NetSend.aircraftControlsMoved_ToServer(i, f);
		}
	}

	public void createPilotHotMoves()
	{
		String string = "move";
		HotKeyCmdEnv.setCurrentEnv(string);
		HotKeyEnv.fromIni(string, Config.cur.ini, "HotKey " + string);
		HotKeyCmdEnv hotkeycmdenv = HotKeyCmdEnv.currentEnv();
		HotKeyCmdEnv hotkeycmdenv_24_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("01", "power", 1, 1));
		HotKeyCmdEnv hotkeycmdenv_25_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("02", "flaps", 2, 2));
		HotKeyCmdEnv hotkeycmdenv_26_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("03", "aileron", 3, 3));
		HotKeyCmdEnv hotkeycmdenv_27_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("04", "elevator", 4, 4));
		HotKeyCmdEnv hotkeycmdenv_28_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("05", "rudder", 5, 5));
		HotKeyCmdEnv hotkeycmdenv_29_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("06", "brakes", 6, 6));
		HotKeyCmdEnv hotkeycmdenv_30_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("07", "pitch", 7, 7));
		HotKeyCmdEnv hotkeycmdenv_31_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("08", "trimaileron", 8, 8));
		HotKeyCmdEnv hotkeycmdenv_32_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("09", "trimelevator", 9, 9));
		HotKeyCmdEnv hotkeycmdenv_33_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("10", "trimrudder", 10, 10));
		HotKeyCmdEnv hotkeycmdenv_34_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-power", 1, 11));
		HotKeyCmdEnv hotkeycmdenv_35_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-flaps", 2, 12));
		HotKeyCmdEnv hotkeycmdenv_36_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-aileron", 3, 13));
		HotKeyCmdEnv hotkeycmdenv_37_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-elevator", 4, 14));
		HotKeyCmdEnv hotkeycmdenv_38_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-rudder", 5, 15));
		HotKeyCmdEnv hotkeycmdenv_39_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-brakes", 6, 16));
		HotKeyCmdEnv hotkeycmdenv_40_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-pitch", 7, 17));
		HotKeyCmdEnv hotkeycmdenv_41_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-trimaileron", 8, 18));
		HotKeyCmdEnv hotkeycmdenv_42_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-trimelevator", 9, 19));
		HotKeyCmdEnv hotkeycmdenv_43_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-trimrudder", 10, 20));

		// TAK++
		HotKeyCmdEnv _tmp80 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("11", "BrakeRight", 11, 21));
		HotKeyCmdEnv _tmp81 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("12", "BrakeLeft", 12, 22));
		HotKeyCmdEnv _tmp82 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-BrakeRight", 11, 23));
		HotKeyCmdEnv _tmp89 = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-BrakeLeft", 12, 24));
		// TAK--

		com.maddox.rts.HotKeyCmdEnv _tmp20 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("11", "zoom", 100, 30, true));
		com.maddox.rts.HotKeyCmdEnv _tmp21 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-zoom", 100, 31, true));
		com.maddox.rts.HotKeyCmdEnv _tmp22 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("15", "power1", 15, 32));
		com.maddox.rts.HotKeyCmdEnv _tmp23 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-power1", 15, 23));
		com.maddox.rts.HotKeyCmdEnv _tmp24 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("16", "power2", 16, 34));
		com.maddox.rts.HotKeyCmdEnv _tmp25 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-power2", 16, 35));
		com.maddox.rts.HotKeyCmdEnv _tmp26 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("17", "power3", 17, 170));
		com.maddox.rts.HotKeyCmdEnv _tmp27 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-power3", 17, 171));
		com.maddox.rts.HotKeyCmdEnv _tmp28 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("18", "power4", 18, 172));
		com.maddox.rts.HotKeyCmdEnv _tmp29 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-power4", 18, 174));
		com.maddox.rts.HotKeyCmdEnv _tmp30 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("25", "radiator", 19, 36, true));
		com.maddox.rts.HotKeyCmdEnv _tmp31 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-radiator", 19, 37, true));
		com.maddox.rts.HotKeyCmdEnv _tmp32 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("26", "prop1", 20, 38));
		com.maddox.rts.HotKeyCmdEnv _tmp33 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-prop1", 20, 39));
		com.maddox.rts.HotKeyCmdEnv _tmp34 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("27", "prop2", 21, 40));
		com.maddox.rts.HotKeyCmdEnv _tmp35 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-prop2", 21, 41));
		com.maddox.rts.HotKeyCmdEnv _tmp36 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("28", "prop3", 22, 175));
		com.maddox.rts.HotKeyCmdEnv _tmp37 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-prop3", 22, 176));
		com.maddox.rts.HotKeyCmdEnv _tmp38 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove("29", "prop4", 23, 177));
		com.maddox.rts.HotKeyCmdEnv _tmp39 = hotkeycmdenv;
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFireMove(null, "-prop4", 23, 178));
	}

	private void createCommonHotKeys()
	{
		java.lang.String s = "misc";
		com.maddox.rts.HotKeyCmdEnv.setCurrentEnv(s);
		com.maddox.rts.HotKeyEnv.fromIni(s, com.maddox.il2.engine.Config.cur.ini, "HotKey pilot");
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFire("ZZZ18", "BEACON_PLUS", 139, 359));
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFire("ZZZ19", "BEACON_MINUS", 140, 360));
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFire("ZZZ40", "AUX1_PLUS", 149, 369));
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFire("ZZZ41", "AUX1_MINUS", 150, 370));
		com.maddox.rts.HotKeyCmdEnv.addCmd(new HotKeyCmdFire("ZZZ60", "AUX_A", 157, 377));
	}

	public void createPilotHotKeys()
	{
		String string = "pilot";
		HotKeyCmdEnv.setCurrentEnv(string);
		HotKeyEnv.fromIni(string, Config.cur.ini, "HotKey " + string);
		HotKeyCmdEnv hotkeycmdenv = HotKeyCmdEnv.currentEnv();
		HotKeyCmdEnv hotkeycmdenv_44_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic01", "ElevatorUp", 3, 103));
		HotKeyCmdEnv hotkeycmdenv_45_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic02", "ElevatorDown", 4, 104));
		HotKeyCmdEnv hotkeycmdenv_46_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic03", "AileronLeft", 5, 105));
		HotKeyCmdEnv hotkeycmdenv_47_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic04", "AileronRight", 6, 106));
		HotKeyCmdEnv hotkeycmdenv_48_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic05", "RudderLeft", 1, 101));
		HotKeyCmdEnv hotkeycmdenv_49_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic06", "RudderRight", 2, 102));
		HotKeyCmdEnv hotkeycmdenv_50_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic07", "Stabilizer", 71, 165));
		HotKeyCmdEnv hotkeycmdenv_51_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic08", "AIRCRAFT_RUDDER_LEFT_1", 56, 156));
		HotKeyCmdEnv hotkeycmdenv_52_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic09", "AIRCRAFT_RUDDER_CENTRE", 57, 157));
		HotKeyCmdEnv hotkeycmdenv_53_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic10", "AIRCRAFT_RUDDER_RIGHT_1", 58, 158));
		HotKeyCmdEnv hotkeycmdenv_54_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic11", "AIRCRAFT_TRIM_V_PLUS", 44, 144));
		HotKeyCmdEnv hotkeycmdenv_55_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic12", "AIRCRAFT_TRIM_V_0", 43, 143));
		HotKeyCmdEnv hotkeycmdenv_56_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic13", "AIRCRAFT_TRIM_V_MINUS", 45, 145));
		HotKeyCmdEnv hotkeycmdenv_57_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic14", "AIRCRAFT_TRIM_H_MINUS", 48, 148));
		HotKeyCmdEnv hotkeycmdenv_58_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic15", "AIRCRAFT_TRIM_H_0", 46, 146));
		HotKeyCmdEnv hotkeycmdenv_59_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic16", "AIRCRAFT_TRIM_H_PLUS", 47, 147));
		HotKeyCmdEnv hotkeycmdenv_60_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic17", "AIRCRAFT_TRIM_R_MINUS", 51, 151));
		HotKeyCmdEnv hotkeycmdenv_61_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic18", "AIRCRAFT_TRIM_R_0", 49, 149));
		HotKeyCmdEnv hotkeycmdenv_62_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("1basic19", "AIRCRAFT_TRIM_R_PLUS", 50, 150));
		HotKeyCmdEnv hotkeycmdenv_63_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$1", "1basic20")
		{
		});
		hudLogPowerId = HUD.makeIdLog();
		HotKeyCmdEnv hotkeycmdenv_67_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine01", "AIRCRAFT_TOGGLE_ENGINE", 70, 164));
		HotKeyCmdEnv hotkeycmdenv_68_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine02", "AIRCRAFT_POWER_PLUS_5", 59, 159));
		HotKeyCmdEnv hotkeycmdenv_69_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine03", "AIRCRAFT_POWER_MINUS_5", 60, 160));
		HotKeyCmdEnv hotkeycmdenv_70_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine04", "Boost", 61, 161));
		HotKeyCmdEnv hotkeycmdenv_71_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine05", "Power0", 20, 120));
		HotKeyCmdEnv hotkeycmdenv_72_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine06", "Power10", 21, 121));
		HotKeyCmdEnv hotkeycmdenv_73_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine07", "Power20", 22, 122));
		HotKeyCmdEnv hotkeycmdenv_74_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine08", "Power30", 23, 123));
		HotKeyCmdEnv hotkeycmdenv_75_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine09", "Power40", 24, 124));
		HotKeyCmdEnv hotkeycmdenv_76_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine10", "Power50", 25, 125));
		HotKeyCmdEnv hotkeycmdenv_77_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine11", "Power60", 26, 126));
		HotKeyCmdEnv hotkeycmdenv_78_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine12", "Power70", 27, 127));
		HotKeyCmdEnv hotkeycmdenv_79_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine13", "Power80", 28, 128));
		HotKeyCmdEnv hotkeycmdenv_80_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine14", "Power90", 29, 129));
		HotKeyCmdEnv hotkeycmdenv_81_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine15", "Power100", 30, 130));
		HotKeyCmdEnv hotkeycmdenv_82_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$2", "2engine16")
		{
		});
		HotKeyCmdEnv hotkeycmdenv_86_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine17", "Step0", 31, 131));
		HotKeyCmdEnv hotkeycmdenv_87_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine18", "Step10", 32, 132));
		HotKeyCmdEnv hotkeycmdenv_88_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine19", "Step20", 33, 133));
		HotKeyCmdEnv hotkeycmdenv_89_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine20", "Step30", 34, 134));
		HotKeyCmdEnv hotkeycmdenv_90_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine21", "Step40", 35, 135));
		HotKeyCmdEnv hotkeycmdenv_91_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine22", "Step50", 36, 136));
		HotKeyCmdEnv hotkeycmdenv_92_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine23", "Step60", 37, 137));
		HotKeyCmdEnv hotkeycmdenv_93_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine24", "Step70", 38, 138));
		HotKeyCmdEnv hotkeycmdenv_94_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine25", "Step80", 39, 139));
		HotKeyCmdEnv hotkeycmdenv_95_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine26", "Step90", 40, 140));
		HotKeyCmdEnv hotkeycmdenv_96_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine27", "Step100", 41, 141));
		HotKeyCmdEnv hotkeycmdenv_97_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine28", "StepAuto", 42, 142));
		HotKeyCmdEnv hotkeycmdenv_98_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine29", "StepPlus5", 73, 290));
		HotKeyCmdEnv hotkeycmdenv_99_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine30", "StepMinus5", 74, 291));
		HotKeyCmdEnv hotkeycmdenv_100_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$3", "2engine31")
		{
		});
		HotKeyCmdEnv hotkeycmdenv_104_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine32", "Mix0", 75, 292));
		HotKeyCmdEnv hotkeycmdenv_105_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine33", "Mix10", 76, 293));
		HotKeyCmdEnv hotkeycmdenv_106_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine34", "Mix20", 77, 294));
		HotKeyCmdEnv hotkeycmdenv_107_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine35", "Mix30", 78, 295));
		HotKeyCmdEnv hotkeycmdenv_108_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine36", "Mix40", 79, 296));
		HotKeyCmdEnv hotkeycmdenv_109_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine37", "Mix50", 80, 297));
		HotKeyCmdEnv hotkeycmdenv_110_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine38", "Mix60", 81, 298));
		HotKeyCmdEnv hotkeycmdenv_111_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine39", "Mix70", 82, 299));
		HotKeyCmdEnv hotkeycmdenv_112_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine40", "Mix80", 83, 300));
		HotKeyCmdEnv hotkeycmdenv_113_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine41", "Mix90", 84, 301));
		HotKeyCmdEnv hotkeycmdenv_114_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine42", "Mix100", 85, 302));
		HotKeyCmdEnv hotkeycmdenv_115_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine43", "MixPlus20", 86, 303));
		HotKeyCmdEnv hotkeycmdenv_116_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine44", "MixMinus20", 87, 304));
		HotKeyCmdEnv hotkeycmdenv_117_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$4", "2engine45")
		{
		});
		HotKeyCmdEnv hotkeycmdenv_121_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine46", "MagnetoPlus", 88, 305));
		HotKeyCmdEnv hotkeycmdenv_122_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine47", "MagnetoMinus", 89, 306));
		HotKeyCmdEnv hotkeycmdenv_123_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$5", "2engine48")
		{
		});
		HotKeyCmdEnv hotkeycmdenv_127_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine49", "CompressorPlus", 115, 334));
		HotKeyCmdEnv hotkeycmdenv_128_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine50", "CompressorMinus", 116, 335));
		HotKeyCmdEnv hotkeycmdenv_129_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$6", "2engine51")
		{
		});
		HotKeyCmdEnv hotkeycmdenv_133_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine52", "EngineSelectAll", 90, 307));
		HotKeyCmdEnv hotkeycmdenv_134_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine53", "EngineSelectNone", 91, 318));
		HotKeyCmdEnv hotkeycmdenv_135_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine54", "EngineSelectLeft", 92, 316));
		HotKeyCmdEnv hotkeycmdenv_136_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine55", "EngineSelectRight", 93, 317));
		HotKeyCmdEnv hotkeycmdenv_137_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine56", "EngineSelect1", 94, 308));
		HotKeyCmdEnv hotkeycmdenv_138_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine57", "EngineSelect2", 95, 309));
		HotKeyCmdEnv hotkeycmdenv_139_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine58", "EngineSelect3", 96, 310));
		HotKeyCmdEnv hotkeycmdenv_140_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine59", "EngineSelect4", 97, 311));
		HotKeyCmdEnv hotkeycmdenv_141_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine60", "EngineSelect5", 98, 312));
		HotKeyCmdEnv hotkeycmdenv_142_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine61", "EngineSelect6", 99, 313));
		HotKeyCmdEnv hotkeycmdenv_143_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine62", "EngineSelect7", 100, 314));
		HotKeyCmdEnv hotkeycmdenv_144_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine63", "EngineSelect8", 101, 315));
		HotKeyCmdEnv hotkeycmdenv_145_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine64", "EngineToggleAll", 102, 319));
		HotKeyCmdEnv hotkeycmdenv_146_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine65", "EngineToggleLeft", 103, 328));
		HotKeyCmdEnv hotkeycmdenv_147_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine66", "EngineToggleRight", 104, 329));
		HotKeyCmdEnv hotkeycmdenv_148_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine67", "EngineToggle1", 105, 320));
		HotKeyCmdEnv hotkeycmdenv_149_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine68", "EngineToggle2", 106, 321));
		HotKeyCmdEnv hotkeycmdenv_150_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine69", "EngineToggle3", 107, 322));
		HotKeyCmdEnv hotkeycmdenv_151_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine70", "EngineToggle4", 108, 323));
		HotKeyCmdEnv hotkeycmdenv_152_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine71", "EngineToggle5", 109, 324));
		HotKeyCmdEnv hotkeycmdenv_153_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine72", "EngineToggle6", 110, 325));
		HotKeyCmdEnv hotkeycmdenv_154_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine73", "EngineToggle7", 111, 326));
		HotKeyCmdEnv hotkeycmdenv_155_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine74", "EngineToggle8", 112, 327));
		HotKeyCmdEnv hotkeycmdenv_156_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$7", "2engine75")
		{
		});
		HotKeyCmdEnv hotkeycmdenv_160_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine76", "EngineExtinguisher", 113, 330));
		HotKeyCmdEnv hotkeycmdenv_161_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("2engine77", "EngineFeather", 114, 333));
		HotKeyCmdEnv hotkeycmdenv_162_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$8", "2engine78")
		{
		});
		HotKeyCmdEnv hotkeycmdenv_166_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced01", "AIRCRAFT_FLAPS_NOTCH_UP", 52, 152));
		HotKeyCmdEnv hotkeycmdenv_167_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced02", "AIRCRAFT_FLAPS_NOTCH_DOWN", 53, 153));
		HotKeyCmdEnv hotkeycmdenv_168_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced03", "Gear", 9, 109));
		HotKeyCmdEnv hotkeycmdenv_169_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced04", "AIRCRAFT_GEAR_UP_MANUAL", 54, 154));
		HotKeyCmdEnv hotkeycmdenv_170_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced05", "AIRCRAFT_GEAR_DOWN_MANUAL", 55, 155));
		HotKeyCmdEnv hotkeycmdenv_171_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced06", "Radiator", 7, 107));
		HotKeyCmdEnv hotkeycmdenv_172_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced07", "AIRCRAFT_TOGGLE_AIRBRAKE", 63, 163));
		HotKeyCmdEnv hotkeycmdenv_173_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced08", "Brake", 0, 100));
		HotKeyCmdEnv hotkeycmdenv_174_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced09", "AIRCRAFT_TAILWHEELLOCK", 72, 166));
		HotKeyCmdEnv hotkeycmdenv_175_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced10", "AIRCRAFT_DROP_TANKS", 62, 162));
		HotKeyCmdEnv hotkeycmdenv_176_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$9", "3advanced11")
		{
		});
		HotKeyCmdEnv hotkeycmdenv_180_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced12", "AIRCRAFT_DOCK_UNDOCK", 126, 346));
		HotKeyCmdEnv hotkeycmdenv_181_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced13", "WINGFOLD", 127, 347));
		HotKeyCmdEnv hotkeycmdenv_182_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced14", "AIRCRAFT_CARRIERHOOK", 129, 349));
		HotKeyCmdEnv hotkeycmdenv_183_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced15", "AIRCRAFT_BRAKESHOE", 130, 350));
		HotKeyCmdEnv hotkeycmdenv_184_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced16", "COCKPITDOOR", 128, 348));

		//TODO: Added by |ZUTI|: Bomb Bay Door mod keys
		//-----------------------------------------------------------------------------
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("3advanced17", "BombBayDoor", 131, 400));
		//-----------------------------------------------------------------------------

		HotKeyCmdEnv hotkeycmdenv_185_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$$10", "3advanced18")
		{
		});
		hudLogWeaponId = HUD.makeIdLog();
		HotKeyCmdEnv hotkeycmdenv_189_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic0", "Weapon0", 16, 116));
		HotKeyCmdEnv hotkeycmdenv_190_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic1", "Weapon1", 17, 117));
		HotKeyCmdEnv hotkeycmdenv_191_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic2", "Weapon2", 18, 118));
		HotKeyCmdEnv hotkeycmdenv_192_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic3", "Weapon3", 19, 119));
		HotKeyCmdEnv hotkeycmdenv_193_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic4", "Weapon01", 64, 173));
		HotKeyCmdEnv hotkeycmdenv_194_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("4basic5", "GunPods", 15, 115));
		HotKeyCmdEnv hotkeycmdenv_195_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmd(false, "$$+SIGHTCONTROLS", "4basic6")
		{
		});
		HotKeyCmdEnv hotkeycmdenv_199_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced01", "SIGHT_AUTO_ONOFF", 125, 344));
		HotKeyCmdEnv hotkeycmdenv_200_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced02", "SIGHT_DIST_PLUS", 117, 336));
		HotKeyCmdEnv hotkeycmdenv_201_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced03", "SIGHT_DIST_MINUS", 118, 337));
		HotKeyCmdEnv hotkeycmdenv_202_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced04", "SIGHT_SIDE_RIGHT", 119, 338));
		HotKeyCmdEnv hotkeycmdenv_203_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced05", "SIGHT_SIDE_LEFT", 120, 339));
		HotKeyCmdEnv hotkeycmdenv_204_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced06", "SIGHT_ALT_PLUS", 121, 340));
		HotKeyCmdEnv hotkeycmdenv_205_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced07", "SIGHT_ALT_MINUS", 122, 341));
		HotKeyCmdEnv hotkeycmdenv_206_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced08", "SIGHT_SPD_PLUS", 123, 342));
		HotKeyCmdEnv hotkeycmdenv_207_ = hotkeycmdenv;
		HotKeyCmdEnv.addCmd(new HotKeyCmdFire("5advanced09", "SIGHT_SPD_MINUS", 124, 343));
	}

	private CockpitGunner getActiveCockpitGuner()
	{
		if (!Actor.isAlive(World.getPlayerAircraft()))
			return null;
		if (World.isPlayerParatrooper())
			return null;
		if (World.isPlayerDead())
			return null;
		if (Main3D.cur3D().cockpits == null)
			return null;
		int i = World.getPlayerAircraft().FM.AS.astatePlayerIndex;
		for (int i_208_ = 0; i_208_ < Main3D.cur3D().cockpits.length; i_208_++)
		{
			if (Main3D.cur3D().cockpits[i_208_] instanceof CockpitGunner)
			{
				CockpitGunner cockpitgunner = (CockpitGunner) Main3D.cur3D().cockpits[i_208_];
				if (i == cockpitgunner.astatePilotIndx() && cockpitgunner.isRealMode())
				{
					Turret turret = cockpitgunner.aiTurret();
					if (!turret.bIsNetMirror)
						return cockpitgunner;
				}
			}
		}
		return null;
	}

	public void createGunnerHotKeys()
	{
		String string = "gunner";
		HotKeyCmdEnv.setCurrentEnv(string);
		HotKeyEnv.fromIni(string, Config.cur.ini, "HotKey " + string);

		HotKeyCmdEnv.addCmd(string, new HotKeyCmdMouseMove(true, "Mouse")
		{
			public void created()
			{
				setRecordId(51);
				sortingName = null;
			}

			public boolean isDisableIfTimePaused()
			{
				return true;
			}

			public void move(int i, int i_211_, int i_212_)
			{
				CockpitGunner cockpitgunner = AircraftHotKeys.this.getActiveCockpitGuner();
				if (cockpitgunner != null)
					cockpitgunner.hookGunner().mouseMove(i, i_211_, i_212_);
			}
		});
		HotKeyCmdEnv.addCmd(string, new HotKeyCmd(true, "Fire")
		{
			CockpitGunner	coc	= null;

			public void created()
			{
				setRecordId(52);
			}

			public boolean isDisableIfTimePaused()
			{
				return true;
			}

			private boolean isExistAmmo(CockpitGunner cockpitgunner)
			{
				com.maddox.il2.fm.FlightModel flightmodel = World.getPlayerFM();
				BulletEmitter[] bulletemitters = flightmodel.CT.Weapons[cockpitgunner.weaponControlNum()];
				if (bulletemitters == null)
					return false;
				for (int i = 0; i < bulletemitters.length; i++)
				{
					if (bulletemitters[i] != null && bulletemitters[i].haveBullets())
						return true;
				}
				return false;
			}

			public void begin()
			{
				coc = AircraftHotKeys.this.getActiveCockpitGuner();
				if (coc != null)
				{
					if (isExistAmmo(coc))
						coc.hookGunner().gunFire(true);
					else
						HUD.log(AircraftHotKeys.hudLogWeaponId, "OutOfAmmo");
				}
			}

			public void end()
			{
				if (coc != null)
				{
					if (Actor.isValid(coc))
						coc.hookGunner().gunFire(false);
					coc = null;
				}
			}
		});
	}

	public boolean isAutoAutopilot()
	{
		return bAutoAutopilot;
	}

	public void setAutoAutopilot(boolean bool)
	{
		bAutoAutopilot = bool;
	}

	public static boolean isCockpitRealMode(int i)
	{
		if (Main3D.cur3D().cockpits[i] instanceof CockpitPilot)
		{
			// TODO: Added by |ZUTI|: try/catch block
			try
			{
				RealFlightModel realflightmodel = (RealFlightModel) World.getPlayerFM();
				return realflightmodel.isRealMode();
			}
			catch (ClassCastException ex)
			{
				return false;
			}
		}
		if (Main3D.cur3D().cockpits[i] instanceof CockpitGunner)
		{
			CockpitGunner cockpitgunner = (CockpitGunner) Main3D.cur3D().cockpits[i];
			return cockpitgunner.isRealMode();
		}
		return false;
	}

	public static void setCockpitRealMode(int i, boolean bool)
	{
		if (Main3D.cur3D().cockpits[i] instanceof CockpitPilot)
		{
			if (!Mission.isNet())
			{
				RealFlightModel realflightmodel = (RealFlightModel) World.getPlayerFM();
				if (realflightmodel.get_maneuver() != 44 && realflightmodel.isRealMode() != bool)
				{
					if (realflightmodel.isRealMode())
						Main3D.cur3D().aircraftHotKeys.bAfterburner = false;
					realflightmodel.CT.resetControl(0);
					realflightmodel.CT.resetControl(1);
					realflightmodel.CT.resetControl(2);
					realflightmodel.EI.setCurControlAll(true);
					realflightmodel.setRealMode(bool);
					HUD.log("PilotAI" + (realflightmodel.isRealMode() ? "OFF" : "ON"));
				}
			}
		}
		else if (Main3D.cur3D().cockpits[i] instanceof CockpitGunner)
		{
			CockpitGunner cockpitgunner = (CockpitGunner) Main3D.cur3D().cockpits[i];
			if (cockpitgunner.isRealMode() != bool)
			{
				cockpitgunner.setRealMode(bool);
				if (!NetMissionTrack.isPlaying())
				{
					Aircraft aircraft = World.getPlayerAircraft();
					if (World.isPlayerGunner())
						aircraft.netCockpitAuto(World.getPlayerGunner(), i, !cockpitgunner.isRealMode());
					else
						aircraft.netCockpitAuto(aircraft, i, !cockpitgunner.isRealMode());
				}
				com.maddox.il2.fm.FlightModel flightmodel = World.getPlayerFM();
				AircraftState aircraftstate = flightmodel.AS;
				String string = (AircraftState.astateHUDPilotHits[(flightmodel.AS.astatePilotFunctions[cockpitgunner.astatePilotIndx()])]);
				HUD.log(string + (cockpitgunner.isRealMode() ? "AIOFF" : "AION"));
			}
		}
	}

	private boolean isMiscValid()
	{
		if (!Actor.isAlive(World.getPlayerAircraft()))
			return false;
		if (World.isPlayerParatrooper())
			return false;
		if (World.isPlayerDead())
			return false;
		return Mission.isPlaying();
	}

	public void createMiscHotKeys()
	{
		String string = "misc";
		HotKeyCmdEnv.setCurrentEnv(string);
		HotKeyEnv.fromIni(string, Config.cur.ini, "HotKey " + string);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "autopilot", "00")
		{
			public void created()
			{
				setRecordId(270);
			}

			public void begin()
			{
				if (AircraftHotKeys.this.isMiscValid() && !Main3D.cur3D().isDemoPlaying() && !(World.getPlayerFM().AS.isPilotDead(Main3D.cur3D().cockpitCur.astatePilotIndx())))
				{
					int i = Main3D.cur3D().cockpitCurIndx();
					if (isCockpitRealMode(i))
						new MsgAction(true, new Integer(i))
						{
							public void doAction(Object object)
							{
								int i_219_ = ((Integer) object).intValue();
								HotKeyCmd.exec("misc", "cockpitRealOff" + i_219_);
							}
						};
					else
						new MsgAction(true, new Integer(i))
						{
							public void doAction(Object object)
							{
								int i_221_ = ((Integer) object).intValue();
								HotKeyCmd.exec("misc", "cockpitRealOn" + i_221_);
							}
						};
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "autopilotAuto", "01")
		{
			public void begin()
			{
				if (AircraftHotKeys.this.isMiscValid() && !Main3D.cur3D().isDemoPlaying())
					new MsgAction(true)
					{
						public void doAction()
						{
							HotKeyCmd.exec("misc", "autopilotAuto_");
						}
					};
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "autopilotAuto_", null)
		{
			public void created()
			{
				setRecordId(271);
				HotKeyEnv.currentEnv().remove(sName);
			}

			public void begin()
			{
				if (AircraftHotKeys.this.isMiscValid())
				{
					setAutoAutopilot(!isAutoAutopilot());
					HUD.log("AutopilotAuto" + (isAutoAutopilot() ? "ON" : "OFF"));
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "target_", null)
		{
			public void created()
			{
				setRecordId(278);
				HotKeyEnv.currentEnv().remove(sName);
			}

			public void begin()
			{
				Object object = null;
				Actor actor;
				if (Main3D.cur3D().isDemoPlaying())
					actor = Selector._getTrackArg0();
				else
					actor = HookPilot.cur().getEnemy();
				Selector.setTarget(Selector.setCurRecordArg0(actor));
			}
		});
		for (int i = 0; i < 10; i++)
		{
			HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitRealOn" + i, null)
			{
				int	indx;

				public void created()
				{
					indx = ((Character.getNumericValue(name().charAt(name().length() - 1))) - Character.getNumericValue('0'));
					setRecordId(500 + indx);
					HotKeyEnv.currentEnv().remove(sName);
				}

				public void begin()
				{
					if (AircraftHotKeys.this.isMiscValid())
						setCockpitRealMode(indx, true);
				}
			});
			HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitRealOff" + i, null)
			{
				int	indx;

				public void created()
				{
					indx = ((Character.getNumericValue(name().charAt(name().length() - 1))) - Character.getNumericValue('0'));
					setRecordId(510 + indx);
					HotKeyEnv.currentEnv().remove(sName);
				}

				public void begin()
				{
					if (AircraftHotKeys.this.isMiscValid())
						setCockpitRealMode(indx, false);
				}
			});
			HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitEnter" + i, null)
			{
				int	indx;

				public void created()
				{
					indx = ((Character.getNumericValue(name().charAt(name().length() - 1))) - Character.getNumericValue('0'));
					setRecordId(520 + indx);
					HotKeyEnv.currentEnv().remove(sName);
				}

				public void begin()
				{
					if (AircraftHotKeys.this.isMiscValid())
					{
						if (Main3D.cur3D().cockpits != null && indx < Main3D.cur3D().cockpits.length)
						{
							World.getPlayerAircraft().FM.AS.astatePlayerIndex = Main3D.cur3D().cockpits[indx].astatePilotIndx();
							if (!NetMissionTrack.isPlaying())
							{
								Aircraft aircraft = World.getPlayerAircraft();
								// TODO: Added by |ZUTI|: once player spawns as gunner isPlayeGunner function is useless.
								// If/when reseting PlayerGunner variable in World class engine also destroys
								// aircraft to which gunner is assigned. That is NOT desired so I have included gunner
								// cockpit number because I reset that to -1 after player ejects/leaves it.
								if (World.isPlayerGunner() && World.getPlayerGunner().zutiGetCockpitNum() > 0)
									aircraft.netCockpitEnter(World.getPlayerGunner(), indx);
								else
									aircraft.netCockpitEnter(aircraft, indx);
							}
						}
					}
				}
			});
			HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitLeave" + i, null)
			{
				int	indx;

				public void created()
				{
					indx = ((Character.getNumericValue(name().charAt(name().length() - 1))) - Character.getNumericValue('0'));
					setRecordId(530 + indx);
					HotKeyEnv.currentEnv().remove(sName);
				}

				public void begin()
				{
					if (AircraftHotKeys.this.isMiscValid())
					{
						if (Main3D.cur3D().cockpits != null && indx < Main3D.cur3D().cockpits.length && (Main3D.cur3D().cockpits[indx] instanceof CockpitGunner) && isCockpitRealMode(indx))
							((CockpitGunner) Main3D.cur3D().cockpits[indx]).hookGunner().gunFire(false);
					}
				}
			});
		}
		// ZUTI: multicrew ejecting
		// -------------------------------------------------------------------------------------
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ejectPilot", "02")
		{
			public void created()
			{
				setRecordId(272);
			}

			public void begin()
			{
				if (AircraftHotKeys.this.isMiscValid() && !World.isPlayerGunner() && World.getPlayerFM() instanceof RealFlightModel)
				{
					RealFlightModel realflightmodel = (RealFlightModel) World.getPlayerFM();
					if (realflightmodel.isRealMode() && !realflightmodel.AS.bIsAboutToBailout && realflightmodel.AS.bIsEnableToBailout)
					{
						AircraftState.bCheckPlayerAircraft = false;
						((Aircraft) realflightmodel.actor).hitDaSilk();
						AircraftState.bCheckPlayerAircraft = true;
						Voice.cur().SpeakBailOut[realflightmodel.actor.getArmy() - 1 & 0x1][((Aircraft) realflightmodel.actor).aircIndex()] = (int) (Time.current() / 60000L) + 1;
						new MsgAction(true)
						{
							public void doAction()
							{
								if (!Main3D.cur3D().isDemoPlaying() || !HotKeyEnv.isEnabled("aircraftView"))
									HotKeyCmd.exec("aircraftView", "OutsideView");
							}
						};
					}
				}
				// TODO: Added by |ZUTI|: enable bailout if player is gunner
				else if (World.isPlayerGunner() && !World.isPlayerDead() && !World.isPlayerParatrooper())
				{
					ZutiSupportMethods_NetSend.ejectPlayer((NetUser) NetEnv.host());
				}
			}
		});
		// -------------------------------------------------------------------------------------
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitDim", "03")
		{
			public void created()
			{
				setRecordId(274);
			}

			public void begin()
			{
				if (!Main3D.cur3D().isViewOutside() && AircraftHotKeys.this.isMiscValid() && Actor.isValid(Main3D.cur3D().cockpitCur))
					Main3D.cur3D().cockpitCur.doToggleDim();
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitLight", "04")
		{
			public void created()
			{
				setRecordId(275);
			}

			public void begin()
			{
				if (!Main3D.cur3D().isViewOutside() && AircraftHotKeys.this.isMiscValid() && Actor.isValid(Main3D.cur3D().cockpitCur))
					Main3D.cur3D().cockpitCur.doToggleLight();
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "toggleNavLights", "05")
		{
			public void created()
			{
				setRecordId(331);
			}

			public void begin()
			{
				if (AircraftHotKeys.this.isMiscValid())
				{
					com.maddox.il2.fm.FlightModel flightmodel = World.getPlayerFM();
					if (flightmodel != null)
					{
						boolean bool = flightmodel.AS.bNavLightsOn;
						flightmodel.AS.setNavLightsState(!flightmodel.AS.bNavLightsOn);
						if (bool || flightmodel.AS.bNavLightsOn)
							HUD.log("NavigationLights" + (flightmodel.AS.bNavLightsOn ? "ON" : "OFF"));
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "toggleLandingLight", "06")
		{
			public void created()
			{
				setRecordId(345);
			}

			public void begin()
			{
				if (AircraftHotKeys.this.isMiscValid())
				{
					com.maddox.il2.fm.FlightModel flightmodel = World.getPlayerFM();
					if (flightmodel != null)
					{
						boolean bool = flightmodel.AS.bLandingLightOn;
						flightmodel.AS.setLandingLightState(!flightmodel.AS.bLandingLightOn);
						if (bool || flightmodel.AS.bLandingLightOn)
						{
							HUD.log("LandingLight" + (flightmodel.AS.bLandingLightOn ? "ON" : "OFF"));
							EventLog.onToggleLandingLight(flightmodel.actor, (flightmodel.AS.bLandingLightOn));
						}
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "toggleSmokes", "07")
		{
			public void created()
			{
				setRecordId(273);
			}

			public void begin()
			{
				if (AircraftHotKeys.this.isMiscValid())
				{
					com.maddox.il2.fm.FlightModel flightmodel = World.getPlayerFM();
					if (flightmodel != null)
					{
						//TODO: Not available?!
						//flightmodel.AS.setAirShowSmokeType(iAirShowSmoke);
						//flightmodel.AS.setAirShowSmokeEnhanced(bAirShowSmokeEnhanced);
						flightmodel.AS.setAirShowState(!flightmodel.AS.bShowSmokesOn);
						EventLog.onToggleSmoke(flightmodel.actor, flightmodel.AS.bShowSmokesOn);
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "pad", "08")
		{
			public void end()
			{
				int i = Main.state().id();
				boolean bool = (i == 5 || i == 29 || i == 63 || i == 49 || i == 50 || i == 42 || i == 43);
				if (GUI.pad.isActive())
					GUI.pad.leave(!bool);
				else if (bool && !Main3D.cur3D().guiManager.isMouseActive())
					GUI.pad.enter();
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "chat", "09")
		{
			public void end()
			{
				GUI.chatActivate();
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "onlineRating", "10")
		{
			public void begin()
			{
				Main3D.cur3D().hud.startNetStat();
			}

			public void end()
			{
				Main3D.cur3D().hud.stopNetStat();
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "onlineRatingPage", "11")
		{
			public void end()
			{
				Main3D.cur3D().hud.pageNetStat();
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "showPositionHint", "12")
		{
			public void begin()
			{
				if (!bSpeedbarTAS)
					HUD.setDrawSpeed((HUD.drawSpeed() + 1) % 4);
				else
					HUD.setDrawSpeed((HUD.drawSpeed() + 1) % 7);
			}

			public void created()
			{
				setRecordId(277);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "iconTypes", "13")
		{
			public void end()
			{
				Main3D.cur3D().changeIconTypes();
			}

			public void created()
			{
				setRecordId(279);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "showMirror", "14")
		{
			public void end()
			{
				Main3D.cur3D().viewMirror = (Main3D.cur3D().viewMirror + 1) % 3;
			}

			public void created()
			{
				setRecordId(280);
			}
		});
	}

	public void create_MiscHotKeys()
	{
		String string = "$$$misc";
		HotKeyCmdEnv.setCurrentEnv(string);
		HotKeyEnv.fromIni(string, Config.cur.ini, "HotKey " + string);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "quickSaveNetTrack", "01")
		{
			public void end()
			{
				GUIWindowManager guiwindowmanager = Main3D.cur3D().guiManager;
				if (!guiwindowmanager.isKeyboardActive())
				{
					if (NetMissionTrack.isQuickRecording())
						NetMissionTrack.stopRecording();
					else
						NetMissionTrack.startQuickRecording();
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "radioMuteKey", "02")
		{
			public void begin()
			{
				AudioDevice.setPTT(true);
			}

			public void end()
			{
				AudioDevice.setPTT(false);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "radioChannelSwitch", "03")
		{
			public void end()
			{
				if (GUI.chatDlg != null && Main.cur().chat != null && GUI.chatDlg.mode() != 2 && !RadioChannel.tstLoop && AudioDevice.npFlags.get(0) && !NetMissionTrack.isPlaying())
				{
					NetUser netuser = (NetUser) NetEnv.host();
					String string_293_ = null;
					String string_294_ = null;
					if (netuser.isRadioPrivate())
					{
						string_293_ = "radio NONE";
						string_294_ = "radioNone";
					}
					else if (netuser.isRadioArmy())
					{
						string_293_ = "radio NONE";
						string_294_ = "radioNone";
					}
					else if (netuser.isRadioCommon())
					{
						if (netuser.getArmy() != 0)
						{
							string_293_ = "radio ARMY";
							string_294_ = "radioArmy";
						}
						else
						{
							string_293_ = "radio NONE";
							string_294_ = "radioNone";
						}
					}
					else if (netuser.isRadioNone())
					{
						string_293_ = "radio COMMON";
						string_294_ = "radioCommon";
					}
					System.out.println(RTSConf.cur.console.getPrompt() + string_293_);
					RTSConf.cur.console.getEnv().exec(string_293_);
					RTSConf.cur.console.addHistoryCmd(string_293_);
					RTSConf.cur.console.curHistoryCmd = -1;
					if (!Time.isPaused())
						HUD.log(string_294_);
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "soundMuteKey", "04")
		{
			public void end()
			{
				AudioDevice.toggleMute();
			}
		});
	}

	private void switchToAIGunner()
	{
		if (!Main3D.cur3D().isDemoPlaying() && Main3D.cur3D().cockpitCur instanceof CockpitGunner && Main3D.cur3D().isViewOutside() && isAutoAutopilot())
		{
			CockpitGunner cockpitgunner = (CockpitGunner) Main3D.cur3D().cockpitCur;
			if (cockpitgunner.isRealMode())
				new MsgAction(true, new Integer(Main3D.cur3D().cockpitCurIndx()))
				{
					public void doAction(Object object)
					{
						int i = ((Integer) object).intValue();
						HotKeyCmd.exec("misc", "cockpitRealOff" + i);
					}
				};
		}
	}

	private boolean isValidCockpit(int i)
	{
		if (!Actor.isValid(World.getPlayerAircraft()))
			return false;
		if (!Mission.isPlaying())
			return false;
		if (World.isPlayerParatrooper())
			return false;
		if (Main3D.cur3D().cockpits == null)
			return false;
		if (i >= Main3D.cur3D().cockpits.length)
			return false;
		if (World.getPlayerAircraft().isUnderWater())
			return false;
		Cockpit cockpit = Main3D.cur3D().cockpits[i];
		if (!cockpit.isEnableFocusing())
			return false;
		int i_141_ = cockpit.astatePilotIndx();
		if (World.getPlayerFM().AS.isPilotParatrooper(i_141_))
			return false;
		if (World.getPlayerFM().AS.isPilotDead(i_141_))
			return false;
		if (Mission.isNet())
		{
			if (Mission.isCoop())
			{
				if (World.isPlayerGunner())
				{
					if (cockpit instanceof CockpitPilot)
						return false;
				}
				else if (cockpit instanceof CockpitPilot)
					return true;
				if (Time.current() == 0L)
					return false;
				if (Main3D.cur3D().isDemoPlaying())
					return true;
				if (Actor.isValid(World.getPlayerAircraft().netCockpitGetDriver(i)) && !World.isPlayerDead())
				{
					return false;
				}
				return true;
			}
			if (Mission.isDogfight())
				return true;
		}
		return true;
	}

	// TODO: Changed by |ZUTI|: from private to public
	public void switchToCockpit(int i)
	{
		if (Mission.isCoop() && Main3D.cur3D().cockpits[i] instanceof CockpitGunner && !Main3D.cur3D().isDemoPlaying() && !World.isPlayerDead())
		{
			Actor actor = World.getPlayerAircraft();
			if (World.isPlayerGunner())
				actor = World.getPlayerGunner();
			Actor actor_142_ = World.getPlayerAircraft().netCockpitGetDriver(i);
			if (actor != actor_142_)
			{
				if (!Actor.isValid(actor_142_))
				{
					switchToCockpitRequest = i;
					World.getPlayerAircraft().netCockpitDriverRequest(actor, i);
					return;
				}
				return;
			}
		}
		doSwitchToCockpit(i);

		// TODO: Added by |ZUTI|: NEEDS TO BE CALLED!!!!!
		String acName = World.getPlayerAircraft().name();
		int cockpitNum = Main3D.cur3D().cockpitCurIndx();
		int newNetPlace = ZutiSupportMethods_Multicrew.getNetPlaceFromAircraftCockpit(acName, cockpitNum);
		((NetUser) NetEnv.host()).requestPlace(newNetPlace);
	}

	// ------------------------------------------------------------------------------

	public void netSwitchToCockpit(int i)
	{
		if (!Main3D.cur3D().isDemoPlaying())
		{
			if (i == switchToCockpitRequest)
				new MsgAction(true, new Integer(i))
				{
					public void doAction(Object object)
					{
						int i_302_ = ((Integer) object).intValue();
						HotKeyCmd.exec("aircraftView", "cockpitSwitch" + i_302_);
					}
				};
		}
	}

	private void doSwitchToCockpit(int i)
	{
		Selector.setCurRecordArg0(World.getPlayerAircraft());
		if (!World.isPlayerDead() && !World.isPlayerParatrooper() && !Main3D.cur3D().isDemoPlaying())
		{
			boolean bool = true;
			if (Main3D.cur3D().cockpitCur instanceof CockpitPilot && Main3D.cur3D().cockpits[i] instanceof CockpitPilot)
				bool = false;
			if (bool && isAutoAutopilot())
				new MsgAction(true, new Integer(Main3D.cur3D().cockpitCurIndx()))
				{
					public void doAction(Object object)
					{
						int i_305_ = ((Integer) object).intValue();
						HotKeyCmd.exec("misc", "cockpitRealOff" + i_305_);
					}
				};
			new MsgAction(true, new Integer(Main3D.cur3D().cockpitCurIndx()))
			{
				public void doAction(Object object)
				{
					int i_308_ = ((Integer) object).intValue();
					HotKeyCmd.exec("misc", "cockpitLeave" + i_308_);
				}
			};
			new MsgAction(true, new Integer(i))
			{
				public void doAction(Object object)
				{
					int i_311_ = ((Integer) object).intValue();
					HotKeyCmd.exec("misc", "cockpitEnter" + i_311_);
				}
			};
			if (bool && isAutoAutopilot())
				new MsgAction(true, new Integer(i))
				{
					public void doAction(Object object)
					{
						int i_314_ = ((Integer) object).intValue();
						HotKeyCmd.exec("misc", "cockpitRealOn" + i_314_);
					}
				};
		}
		Main3D.cur3D().cockpitCur.focusLeave();
		Main3D.cur3D().cockpitCur = Main3D.cur3D().cockpits[i];
		Main3D.cur3D().cockpitCur.focusEnter();
	}

	private int nextValidCockpit()
	{
		int i = Main3D.cur3D().cockpitCurIndx();
		if (i < 0)
			return -1;
		int cockpits = Main3D.cur3D().cockpits.length;
		if (cockpits < 2)
			return -1;
		for (int x = 0; x < cockpits - 1; x++)
		{
			int cockpitNr = (i + x + 1) % cockpits;
			// TODO: Added by |ZUTI|: added new check for dogfight mode!
			// --------------------------------------------
			if (Main.cur().mission.isDogfight())
			{
				// System.out.print("Can control cockpit id: " + cockpitNr);
				ZutiSupportMethods_Multicrew.requestNextFreeCockpit(World.getPlayerAircraft().name(), cockpitNr);
				// Return -1 because we have send request about cockpit change to server!
				// It determins if cockpit is free or not!
				return -1;
			}
			// --------------------------------------------
			else if (isValidCockpit(cockpitNr))
				return cockpitNr;
		}
		return -1;
	}

	public void setEnableChangeFov(boolean bool)
	{
		for (int i = 0; i < cmdFov.length; i++)
			cmdFov[i].enable(bool);

		changeFovEnabled = bool;
	}

	public void createViewHotKeys()
	{
		String string = "aircraftView";
		HotKeyCmdEnv.setCurrentEnv(string);
		HotKeyEnv.fromIni(string, Config.cur.ini, "HotKey " + string);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "changeCockpit", "0")
		{
			public void begin()
			{
				int i = AircraftHotKeys.this.nextValidCockpit();
				if (i >= 0)
					new MsgAction(true, new Integer(i))
					{
						public void doAction(Object object)
						{
							int i_322_ = ((Integer) object).intValue();
							HotKeyCmd.exec("aircraftView", "cockpitSwitch" + i_322_);
						}
					};
			}
		});
		for (int i = 0; i < 10; i++)
		{
			HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitView" + i, "0" + i)
			{
				int	indx;

				public void created()
				{
					indx = ((Character.getNumericValue(name().charAt(name().length() - 1))) - Character.getNumericValue('0'));
				}

				public void begin()
				{
					// TODO: Altered by |ZUTI|: disabled because this option bypasses security locks to prevent two
					// users sitting in the same cockpit on a multi-crew plane! But only for dogfight missions.
					if (Main.cur().mission != null && !Main.cur().mission.isDogfight())
					{
						if (AircraftHotKeys.this.isValidCockpit(indx))
						{
							new MsgAction(true, new Integer(indx))
							{
								public void doAction(Object object)
								{
									int i_327_ = ((Integer) object).intValue();
									HotKeyCmd.exec("aircraftView", "cockpitSwitch" + i_327_);
								}
							};
						}
					}
					// TODO: Added by |ZUTI|: added new check for dogfight mode!
					// --------------------------------------------
					else
					{
						// System.out.println("Can jump to cockpit num: " + indx);
						ZutiSupportMethods_Multicrew.requestNextFreeCockpit(World.getPlayerAircraft().name(), indx);
					}
					// --------------------------------------------
				}
			});
			HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitSwitch" + i, null)
			{
				int	indx;

				public void created()
				{
					indx = ((Character.getNumericValue(name().charAt(name().length() - 1))) - Character.getNumericValue('0'));
					setRecordId(230 + indx);

					HotKeyEnv.currentEnv().remove(sName);
				}

				public void begin()
				{
					if (Main3D.cur3D().cockpitCurIndx() != indx || Main3D.cur3D().isViewOutside())
						AircraftHotKeys.this.switchToCockpit(indx);
				}
			});
		}
		HotKeyCmdEnv.addCmd(cmdFov[0] = new HotKeyCmd(true, "fov90", "11")
		{
			public void begin()
			{
				CmdEnv.top().exec("fov 90");
			}

			public void created()
			{
				setRecordId(216);
			}
		});
		HotKeyCmdEnv.addCmd(cmdFov[1] = new HotKeyCmd(true, "fov85", "12")
		{
			public void begin()
			{
				CmdEnv.top().exec("fov 85");
			}

			public void created()
			{
				setRecordId(244);
			}
		});
		HotKeyCmdEnv.addCmd(cmdFov[2] = new HotKeyCmd(true, "fov80", "13")
		{
			public void begin()
			{
				CmdEnv.top().exec("fov 80");
			}

			public void created()
			{
				setRecordId(243);
			}
		});
		HotKeyCmdEnv.addCmd(cmdFov[3] = new HotKeyCmd(true, "fov75", "14")
		{
			public void begin()
			{
				CmdEnv.top().exec("fov 75");
			}

			public void created()
			{
				setRecordId(242);
			}
		});
		HotKeyCmdEnv.addCmd(cmdFov[4] = new HotKeyCmd(true, "fov70", "15")
		{
			public void begin()
			{
				CmdEnv.top().exec("fov 70");
			}

			public void created()
			{
				setRecordId(215);
			}
		});
		HotKeyCmdEnv.addCmd(cmdFov[5] = new HotKeyCmd(true, "fov65", "16")
		{
			public void begin()
			{
				CmdEnv.top().exec("fov 65");
			}

			public void created()
			{
				setRecordId(241);
			}
		});
		HotKeyCmdEnv.addCmd(cmdFov[6] = new HotKeyCmd(true, "fov60", "17")
		{
			public void begin()
			{
				CmdEnv.top().exec("fov 60");
			}

			public void created()
			{
				setRecordId(240);
			}
		});
		HotKeyCmdEnv.addCmd(cmdFov[7] = new HotKeyCmd(true, "fov55", "18")
		{
			public void begin()
			{
				CmdEnv.top().exec("fov 55");
			}

			public void created()
			{
				setRecordId(229);
			}
		});
		HotKeyCmdEnv.addCmd(cmdFov[8] = new HotKeyCmd(true, "fov50", "19")
		{
			public void begin()
			{
				CmdEnv.top().exec("fov 50");
			}

			public void created()
			{
				setRecordId(228);
			}
		});
		HotKeyCmdEnv.addCmd(cmdFov[9] = new HotKeyCmd(true, "fov45", "20")
		{
			public void begin()
			{
				CmdEnv.top().exec("fov 45");
			}

			public void created()
			{
				setRecordId(227);
			}
		});
		HotKeyCmdEnv.addCmd(cmdFov[10] = new HotKeyCmd(true, "fov40", "21")
		{
			public void begin()
			{
				CmdEnv.top().exec("fov 40");
			}

			public void created()
			{
				setRecordId(226);
			}
		});
		HotKeyCmdEnv.addCmd(cmdFov[11] = new HotKeyCmd(true, "fov35", "22")
		{
			public void begin()
			{
				CmdEnv.top().exec("fov 35");
			}

			public void created()
			{
				setRecordId(225);
			}
		});
		HotKeyCmdEnv.addCmd(cmdFov[12] = new HotKeyCmd(true, "fov30", "23")
		{
			public void begin()
			{
				CmdEnv.top().exec("fov 30");
			}

			public void created()
			{
				setRecordId(214);
			}
		});
		HotKeyCmdEnv.addCmd(cmdFov[13] = new HotKeyCmd(true, "fovSwitch", "24")
		{
			public void begin()
			{
				float f = (Main3D.FOVX - 30.0F) * (Main3D.FOVX - 30.0F);
				float f_373_ = (Main3D.FOVX - 70.0F) * (Main3D.FOVX - 70.0F);
				float f_374_ = (Main3D.FOVX - 90.0F) * (Main3D.FOVX - 90.0F);
				boolean bool = false;
				int i;
				if (f <= f_373_)
					i = 70;
				else if (f_373_ <= f_374_)
					i = 90;
				else
					i = 30;
				new MsgAction(true, new Integer(i))
				{
					public void doAction(Object object)
					{
						int i_377_ = ((Integer) object).intValue();
						HotKeyCmd.exec("aircraftView", "fov" + i_377_);
					}
				};
			}
		});
		HotKeyCmdEnv.addCmd(cmdFov[14] = new HotKeyCmd(true, "fovInc", "25")
		{
			public void begin()
			{
				int i = (int) ((double) Main3D.FOVX + 2.5) / 5 * 5;
				if (i < 30)
					i = 30;
				if (i > 85)
					i = 85;
				i += 5;
				new MsgAction(true, new Integer(i))
				{
					public void doAction(Object object)
					{
						int i_382_ = ((Integer) object).intValue();
						HotKeyCmd.exec("aircraftView", "fov" + i_382_);
					}
				};
			}
		});
		HotKeyCmdEnv.addCmd(cmdFov[15] = new HotKeyCmd(true, "fovDec", "26")
		{
			public void begin()
			{
				int i = (int) ((double) Main3D.FOVX + 2.5) / 5 * 5;
				if (i < 35)
					i = 35;
				if (i > 90)
					i = 90;
				i -= 5;
				new MsgAction(true, new Integer(i))
				{
					public void doAction(Object object)
					{
						int i_387_ = ((Integer) object).intValue();
						HotKeyCmd.exec("aircraftView", "fov" + i_387_);
					}
				};
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "CockpitView", "27")
		{
			public void begin()
			{
				if (Actor.isValid(World.getPlayerAircraft()) && !World.isPlayerParatrooper() && !World.getPlayerAircraft().isUnderWater())
				{
					Main3D.cur3D().setViewInside();
					Selector.setCurRecordArg0(World.getPlayerAircraft());
					if (!Main3D.cur3D().isDemoPlaying() && (World.getPlayerAircraft().netCockpitGetDriver(Main3D.cur3D().cockpitCurIndx())) == null)
						new MsgAction(true, new Integer(Main3D.cur3D().cockpitCurIndx()))
						{
							public void doAction(Object object)
							{
								int i = ((Integer) object).intValue();
								HotKeyCmd.exec("misc", "cockpitEnter" + i);
							}
						};
					if (!Main3D.cur3D().isDemoPlaying() && !Main3D.cur3D().isViewOutside() && isAutoAutopilot() && !isCockpitRealMode(Main3D.cur3D().cockpitCurIndx()))
						new MsgAction(true, new Integer(Main3D.cur3D().cockpitCurIndx()))
						{
							public void doAction(Object object)
							{
								int i = ((Integer) object).intValue();
								HotKeyCmd.exec("misc", "cockpitRealOn" + i);
							}
						};
				}
			}

			public void created()
			{
				setRecordId(212);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "CockpitShow", "28")
		{
			public void created()
			{
				setRecordId(213);
			}

			public void begin()
			{
				if (!World.cur().diffCur.Cockpit_Always_On && !Main3D.cur3D().isViewOutside() && Main3D.cur3D().cockpitCur instanceof CockpitPilot)
				{
					if (Main3D.cur3D().isViewInsideShow())
					{
						Main3D.cur3D().hud.bDrawDashBoard = true;
						Main3D.cur3D().setViewInsideShow(false);
						Main3D.cur3D().cockpitCur.setEnableRenderingBall(true);
					}
					else if (Main3D.cur3D().hud.bDrawDashBoard && Main3D.cur3D().cockpitCur.isEnableRenderingBall())
						Main3D.cur3D().cockpitCur.setEnableRenderingBall(false);
					else if (Main3D.cur3D().hud.bDrawDashBoard && !Main3D.cur3D().cockpitCur.isEnableRenderingBall())
					{
						Main3D.cur3D().hud.bDrawDashBoard = false;
						Main3D.cur3D().cockpitCur.setEnableRenderingBall(true);
					}
					else if (Main3D.cur3D().isEnableRenderingCockpit() && Main3D.cur3D().cockpitCur.isEnableRenderingBall())
						Main3D.cur3D().cockpitCur.setEnableRenderingBall(false);
					else if (Main3D.cur3D().isEnableRenderingCockpit() && !Main3D.cur3D().cockpitCur.isEnableRenderingBall())
						Main3D.cur3D().setEnableRenderingCockpit(false);
					else
					{
						Main3D.cur3D().setEnableRenderingCockpit(true);
						Main3D.cur3D().setViewInsideShow(true);
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "OutsideView", "29")
		{
			public void created()
			{
				setRecordId(205);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (AircraftHotKeys.this.viewAllowed(bExtViewSelf))
				{
					Actor actor = World.getPlayerAircraft();
					Selector.setCurRecordArg0(actor);
					if (!Actor.isValid(actor))
						actor = AircraftHotKeys.this.getViewActor();
					if (Actor.isValid(actor))
					{
						boolean bool = !Main3D.cur3D().isViewOutside();
						Main3D.cur3D().setViewFlow10(actor, false);
						if (bool)
							AircraftHotKeys.this.switchToAIGunner();
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "NextView", "30")
		{
			public void created()
			{
				setRecordId(206);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (AircraftHotKeys.this.viewAllowed(bExtViewFriendly))
				{
					Actor actor = AircraftHotKeys.this.nextViewActor(false);
					if (Actor.isValid(actor))
					{
						boolean bool = !Main3D.cur3D().isViewOutside();
						Main3D.cur3D().setViewFlow10(actor, false);
						if (bool)
							AircraftHotKeys.this.switchToAIGunner();
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "NextViewEnemy", "31")
		{
			public void created()
			{
				setRecordId(207);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (AircraftHotKeys.this.viewAllowed(bExtViewEnemy))
				{
					Actor actor = AircraftHotKeys.this.nextViewActor(true);
					if (Actor.isValid(actor))
					{
						boolean bool = !Main3D.cur3D().isViewOutside();
						Main3D.cur3D().setViewFlow10(actor, false);
						if (bool)
							AircraftHotKeys.this.switchToAIGunner();
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "OutsideViewFly", "32")
		{
			public void created()
			{
				setRecordId(200);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (AircraftHotKeys.this.viewAllowed(bExtViewSelf))
				{
					Actor actor = AircraftHotKeys.this.getViewActor();
					if (Actor.isValid(actor) && !(actor instanceof ActorViewPoint) && !(actor instanceof BigshipGeneric))
					{
						boolean bool = !Main3D.cur3D().isViewOutside();
						Main3D.cur3D().setViewFly(actor);
						if (bool)
							AircraftHotKeys.this.switchToAIGunner();
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "PadlockView", "33")
		{
			public void created()
			{
				setRecordId(217);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (!World.cur().diffCur.No_Padlock || bPadlockEnemy)
				{
					Aircraft aircraft = World.getPlayerAircraft();
					if (Actor.isValid(aircraft) && !World.isPlayerDead() && !World.isPlayerParatrooper())
					{
						if (Main3D.cur3D().isViewPadlock())
						{
							Main3D.cur3D().setViewEndPadlock();
							Selector.setCurRecordArg1(aircraft);
						}
						else
						{
							if (AircraftHotKeys.bFirstHotCmd && Actor.isValid(World.getPlayerAircraft()) && !World.isPlayerParatrooper())
								Main3D.cur3D().setViewInside();
							Main3D.cur3D().setViewPadlock(false, false);
						}
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "PadlockViewFriend", "34")
		{
			public void created()
			{
				setRecordId(218);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (!World.cur().diffCur.No_Padlock || bPadlockFriendly)
				{
					Aircraft aircraft = World.getPlayerAircraft();
					if (Actor.isValid(aircraft) && !World.isPlayerDead() && !World.isPlayerParatrooper())
					{
						if (Main3D.cur3D().isViewPadlock())
						{
							Main3D.cur3D().setViewEndPadlock();
							Selector.setCurRecordArg1(aircraft);
						}
						else
						{
							if (AircraftHotKeys.bFirstHotCmd && Actor.isValid(World.getPlayerAircraft()) && !World.isPlayerParatrooper())
								Main3D.cur3D().setViewInside();
							Main3D.cur3D().setViewPadlock(true, false);
						}
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "PadlockViewGround", "35")
		{
			public void created()
			{
				setRecordId(221);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (!World.cur().diffCur.No_Padlock || bPadlockEnemy)
				{
					Aircraft aircraft = World.getPlayerAircraft();
					if (Actor.isValid(aircraft) && !World.isPlayerDead() && !World.isPlayerParatrooper())
					{
						if (Main3D.cur3D().isViewPadlock())
						{
							Main3D.cur3D().setViewEndPadlock();
							Selector.setCurRecordArg1(aircraft);
						}
						else
						{
							if (AircraftHotKeys.bFirstHotCmd && Actor.isValid(World.getPlayerAircraft()) && !World.isPlayerParatrooper())
								Main3D.cur3D().setViewInside();
							Main3D.cur3D().setViewPadlock(false, true);
						}
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "PadlockViewFriendGround", "36")
		{
			public void created()
			{
				setRecordId(222);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (!World.cur().diffCur.No_Padlock || bPadlockFriendly)
				{
					Aircraft aircraft = World.getPlayerAircraft();
					if (Actor.isValid(aircraft) && !World.isPlayerDead() && !World.isPlayerParatrooper())
					{
						if (Main3D.cur3D().isViewPadlock())
						{
							Main3D.cur3D().setViewEndPadlock();
							Selector.setCurRecordArg1(aircraft);
						}
						else
						{
							if (AircraftHotKeys.bFirstHotCmd && Actor.isValid(World.getPlayerAircraft()) && !World.isPlayerParatrooper())
								Main3D.cur3D().setViewInside();
							Main3D.cur3D().setViewPadlock(true, true);
						}
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "PadlockViewNext", "37")
		{
			public void created()
			{
				setRecordId(223);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (!World.cur().diffCur.No_Padlock || bPadlockFriendly || bExtPadlockFriendly)
				{
					Aircraft aircraft = World.getPlayerAircraft();
					if (Actor.isValid(aircraft) && !World.isPlayerDead() && !World.isPlayerParatrooper())
					{
						if (AircraftHotKeys.bFirstHotCmd)
						{
							Main3D.cur3D().setViewInside();
							if (Actor.isValid(Main3D.cur3D().cockpitCur) && Main3D.cur3D().cockpitCur.existPadlock())
								Main3D.cur3D().cockpitCur.startPadlock(Selector._getTrackArg1());
						}
						else if (Main3D.cur3D().isViewPadlock())
							Main3D.cur3D().setViewNextPadlock(true);
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "PadlockViewPrev", "38")
		{
			public void created()
			{
				setRecordId(224);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (!World.cur().diffCur.No_Padlock || bPadlockFriendly || bExtPadlockFriendly)
				{
					Aircraft aircraft = World.getPlayerAircraft();
					if (Actor.isValid(aircraft) && !World.isPlayerDead() && !World.isPlayerParatrooper())
					{
						if (AircraftHotKeys.bFirstHotCmd)
						{
							Main3D.cur3D().setViewInside();
							if (Actor.isValid(Main3D.cur3D().cockpitCur) && Main3D.cur3D().cockpitCur.existPadlock())
								Main3D.cur3D().cockpitCur.startPadlock(Selector._getTrackArg1());
						}
						else if (Main3D.cur3D().isViewPadlock())
							Main3D.cur3D().setViewNextPadlock(false);
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "PadlockViewForward", "39")
		{
			public void created()
			{
				setRecordId(220);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (!World.cur().diffCur.No_Padlock || bPadlockFriendly || bExtPadlockFriendly)
				{
					Aircraft aircraft = World.getPlayerAircraft();
					if (Actor.isValid(aircraft) && !World.isPlayerDead() && !World.isPlayerParatrooper())
						Main3D.cur3D().setViewPadlockForward(true);
				}
			}

			public void end()
			{
				if (!World.cur().diffCur.No_Padlock)
				{
					Aircraft aircraft = World.getPlayerAircraft();
					if (Actor.isValid(aircraft) && !World.isPlayerDead() && !World.isPlayerParatrooper())
						Main3D.cur3D().setViewPadlockForward(false);
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ViewEnemyAir", "40")
		{
			public void created()
			{
				setRecordId(203);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (!World.cur().diffCur.No_Outside_Views || bExtPadlockEnemy)
				{
					Actor actor = AircraftHotKeys.this.getViewActor();
					if (Actor.isValid(actor) && !(actor instanceof BigshipGeneric))
					{
						boolean bool = !Main3D.cur3D().isViewOutside();
						Main3D.cur3D().setViewEnemy(actor, false, false);
						if (bool)
							AircraftHotKeys.this.switchToAIGunner();
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ViewFriendAir", "41")
		{
			public void created()
			{
				setRecordId(198);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (!World.cur().diffCur.No_Outside_Views || bExtPadlockFriendly)
				{
					Actor actor = AircraftHotKeys.this.getViewActor();
					if (Actor.isValid(actor) && !(actor instanceof BigshipGeneric))
					{
						boolean bool = !Main3D.cur3D().isViewOutside();
						Main3D.cur3D().setViewFriend(actor, false, false);
						if (bool)
							AircraftHotKeys.this.switchToAIGunner();
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ViewEnemyDirectAir", "42")
		{
			public void created()
			{
				setRecordId(201);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (!World.cur().diffCur.No_Outside_Views || bExtPadlockEnemy)
				{
					Actor actor = AircraftHotKeys.this.getViewActor();
					if (Actor.isValid(actor) && !(actor instanceof BigshipGeneric))
					{
						boolean bool = !Main3D.cur3D().isViewOutside();
						Main3D.cur3D().setViewEnemy(actor, true, false);
						if (bool)
							AircraftHotKeys.this.switchToAIGunner();
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ViewEnemyGround", "43")
		{
			public void created()
			{
				setRecordId(204);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (!World.cur().diffCur.No_Outside_Views || bExtPadlockEnemy)
				{
					Actor actor = AircraftHotKeys.this.getViewActor();
					if (Actor.isValid(actor) && !(actor instanceof BigshipGeneric))
					{
						boolean bool = !Main3D.cur3D().isViewOutside();
						Main3D.cur3D().setViewEnemy(actor, false, true);
						if (bool)
							AircraftHotKeys.this.switchToAIGunner();
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ViewFriendGround", "44")
		{
			public void created()
			{
				setRecordId(199);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (!World.cur().diffCur.No_Outside_Views || bExtPadlockFriendly)
				{
					Actor actor = AircraftHotKeys.this.getViewActor();
					if (Actor.isValid(actor) && !(actor instanceof BigshipGeneric))
					{
						boolean bool = !Main3D.cur3D().isViewOutside();
						Main3D.cur3D().setViewFriend(actor, false, true);
						if (bool)
							AircraftHotKeys.this.switchToAIGunner();
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "ViewEnemyDirectGround", "45")
		{
			public void created()
			{
				setRecordId(202);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (!World.cur().diffCur.No_Outside_Views || bExtPadlockEnemy)
				{
					Actor actor = AircraftHotKeys.this.getViewActor();
					if (Actor.isValid(actor) && !(actor instanceof BigshipGeneric))
					{
						boolean bool = !Main3D.cur3D().isViewOutside();
						Main3D.cur3D().setViewEnemy(actor, true, true);
						if (bool)
							AircraftHotKeys.this.switchToAIGunner();
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "OutsideViewFollow", "46")
		{
			public void created()
			{
				setRecordId(208);
			}

			public void begin()
			{
				if (World.cur().diffCur.No_Outside_Views)
					return;
				Object obj = World.getPlayerAircraft();
				Selector.setCurRecordArg0(((Actor) (obj)));
				if (!Actor.isValid(((Actor) (obj))))
					obj = getViewActor();
				if (Actor.isValid(((Actor) (obj))))
				{
					boolean flag = !Main3D.cur3D().isViewOutside();
					Main3D.cur3D().setViewFlow10(((Actor) (obj)), true);
					if (flag)
						switchToAIGunner();
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "NextViewFollow", "47")
		{
			public void created()
			{
				setRecordId(209);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (AircraftHotKeys.this.viewAllowed(bExtViewFriendly))
				{
					Actor actor = AircraftHotKeys.this.nextViewActor(false);
					if (Actor.isValid(actor))
					{
						boolean bool = !Main3D.cur3D().isViewOutside();
						Main3D.cur3D().setViewFlow10(actor, true);
						if (bool)
							AircraftHotKeys.this.switchToAIGunner();
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "NextViewEnemyFollow", "48")
		{
			public void created()
			{
				setRecordId(210);
			}

			public void begin()
			{
				if (!bMissionModsSet)
					AircraftHotKeys.this.setMissionMods();
				if (AircraftHotKeys.this.viewAllowed(bExtViewEnemy))
				{
					Actor actor = AircraftHotKeys.this.nextViewActor(true);
					if (Actor.isValid(actor))
					{
						boolean bool = !Main3D.cur3D().isViewOutside();
						Main3D.cur3D().setViewFlow10(actor, true);
						if (bool)
							AircraftHotKeys.this.switchToAIGunner();
					}
				}
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitAim", "49")
		{
			public void created()
			{
				setRecordId(276);
			}

			public void begin()
			{
				if (!Main3D.cur3D().isViewOutside() && AircraftHotKeys.this.isMiscValid() && Actor.isValid(Main3D.cur3D().cockpitCur) && !Main3D.cur3D().cockpitCur.isToggleUp())
					Main3D.cur3D().cockpitCur.doToggleAim(!Main3D.cur3D().cockpitCur.isToggleAim());
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "cockpitUp", "50")
		{
			public void created()
			{
				setRecordId(281);
			}

			public void begin()
			{
				if (!Main3D.cur3D().isViewOutside() && AircraftHotKeys.this.isMiscValid() && Actor.isValid(Main3D.cur3D().cockpitCur) && !Main3D.cur3D().cockpitCur.isToggleAim()
						&& World.getPlayerFM().CT.bHasCockpitDoorControl
						&& (Main3D.cur3D().cockpitCur.isToggleUp() || (!(World.getPlayerFM().CT.cockpitDoorControl < 0.5F) && !(World.getPlayerFM().CT.getCockpitDoor() < 0.99F))))
					Main3D.cur3D().cockpitCur.doToggleUp(!Main3D.cur3D().cockpitCur.isToggleUp());
			}
		});
	}

	public void createTimeHotKeys()
	{
		String string = "timeCompression";
		HotKeyCmdEnv.setCurrentEnv(string);
		HotKeyEnv.fromIni(string, Config.cur.ini, "HotKey " + string);
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "timeSpeedUp", "0")
		{
			public void begin()
			{
				if (!TimeSkip.isDo())
				{
					if (Time.isEnableChangeSpeed())
					{
						float f = Time.nextSpeed() * 2.0F;
						if (f <= 8.0F)
						{
							Time.setSpeed(f);
							AircraftHotKeys.this.showTimeSpeed(f);
						}
					}
				}
			}

			public void created()
			{
				setRecordId(25);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "timeSpeedNormal", "1")
		{
			public void begin()
			{
				if (!TimeSkip.isDo())
				{
					if (Time.isEnableChangeSpeed())
					{
						Time.setSpeed(1.0F);
						AircraftHotKeys.this.showTimeSpeed(1.0F);
					}
				}
			}

			public void created()
			{
				setRecordId(24);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "timeSpeedDown", "2")
		{
			public void begin()
			{
				if (!TimeSkip.isDo())
				{
					if (Time.isEnableChangeSpeed())
					{
						float f = Time.nextSpeed() / 2.0F;
						if (f >= 0.25F)
						{
							Time.setSpeed(f);
							AircraftHotKeys.this.showTimeSpeed(f);
						}
					}
				}
			}

			public void created()
			{
				setRecordId(26);
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "timeSpeedPause", "3")
		{
			public void begin()
			{
				/* empty */
			}
		});
		HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "timeSkip", "4")
		{
			public void begin()
			{
				if (TimeSkip.isDo())
					Main3D.cur3D().timeSkip.stop();
				else
					Main3D.cur3D().timeSkip.start();
			}
		});
	}

	private void showTimeSpeed(float f)
	{
		int i = Math.round(f * 4.0F);
		switch (i)
		{
			case 4:
				Main3D.cur3D().hud._log(0, "TimeSpeedNormal");
			break;
			case 8:
				Main3D.cur3D().hud._log(0, "TimeSpeedUp2");
			break;
			case 16:
				Main3D.cur3D().hud._log(0, "TimeSpeedUp4");
			break;
			case 32:
				Main3D.cur3D().hud._log(0, "TimeSpeedUp8");
			break;
			case 2:
				Main3D.cur3D().hud._log(0, "TimeSpeedDown2");
			break;
			case 1:
				Main3D.cur3D().hud._log(0, "TimeSpeedDown4");
			break;
		}
	}

	public AircraftHotKeys()
	{
		lastPower2 = -0.5F;
		lastPower1 = -0.5F;
		lastPower3 = -0.5F;
		lastPower4 = -0.5F;
		lastProp1 = 1.5F;
		lastProp2 = 1.5F;
		lastProp3 = 1.5F;
		lastProp4 = 1.5F;
		lastRadiator = -0.5F;
		changeFovEnabled = true;

		bPropAuto = true;
		bAfterburner = false;
		lastPower = -0.5F;
		lastProp = 1.5F;
		cptdmg = 1;
		bAutoAutopilot = false;
		switchToCockpitRequest = -1;
		cmdFov = new HotKeyCmd[16];
		createPilotHotKeys();
		createPilotHotMoves();
		createGunnerHotKeys();
		createMiscHotKeys();
		create_MiscHotKeys();
		createViewHotKeys();
		createTimeHotKeys();

		createCommonHotKeys();

		if (Config.cur.ini.get("Mods", "SpeedbarTAS", 0) > 0)
			bSpeedbarTAS = true;
		if (Config.cur.ini.get("Mods", "SeparateGearUpDown", 0) > 0)
			bSeparateGearUpDown = true;
		if (Config.cur.ini.get("Mods", "SeparateHookUpDown", 0) > 0)
			bSeparateHookUpDown = true;
		if (Config.cur.ini.get("Mods", "SeparateRadiatorOpenClose", 0) > 0)
			bSeparateRadiatorOpenClose = true;
		if (Config.cur.ini.get("Mods", "ToggleMusic", 0) > 0)
			bToggleMusic = true;
		if (Config.cur.ini.get("Mods", "BombBayDoors", 1) == 0)
			bBombBayDoors = false;
		if (Config.cur.ini.get("Mods", "SideDoor", 1) == 0)
			bSideDoor = false;
		iAirShowSmoke = Config.cur.ini.get("Mods", "AirShowSmoke", 0);
		if (iAirShowSmoke < 1 || iAirShowSmoke > 3)
			iAirShowSmoke = 0;
		if (Config.cur.ini.get("Mods", "AirShowSmokeEnhanced", 0) > 0)
			bAirShowSmokeEnhanced = true;
		if (Config.cur.ini.get("Mods", "DumpFuel", 0) > 0)
			bAllowDumpFuel = true;
	}

	private Actor getViewActor()
	{
		if (Selector.isEnableTrackArgs())
			return Selector.setCurRecordArg0(Selector.getTrackArg0());
		Actor actor = Main3D.cur3D().viewActor();
		if (isViewed(actor))
			return Selector.setCurRecordArg0(actor);
		return Selector.setCurRecordArg0(World.getPlayerAircraft());
	}

	private Actor nextViewActor(boolean bool)
	{
		if (Selector.isEnableTrackArgs())
			return Selector.setCurRecordArg0(Selector.getTrackArg0());
		int i = World.getPlayerArmy();
		namedAircraft.clear();
		Actor actor = Main3D.cur3D().viewActor();
		if (isViewed(actor))
			namedAircraft.put(actor.name(), null);
		for (Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
		{
			Actor actor_319_ = (Actor) entry.getValue();

			// TODO: Added by |ZUTI|: Disable showing of paratroopers! New difficulty setting?
			// -------------------------------------------------
			if (actor_319_ instanceof Paratrooper)
				continue;
			// -------------------------------------------------

			if (isViewed(actor_319_) && actor_319_ != actor)
			{
				if (bool)
				{
					if (actor_319_.getArmy() != i)
						namedAircraft.put(actor_319_.name(), null);
				}
				else if (actor_319_.getArmy() == i)
					namedAircraft.put(actor_319_.name(), null);
			}
		}
		if (namedAircraft.size() == 0)
			return Selector.setCurRecordArg0(null);
		if (!isViewed(actor))
			return (Selector.setCurRecordArg0((Actor) Engine.name2Actor().get((String) namedAircraft.firstKey())));
		if (namedAircraft.size() == 1 && isViewed(actor))
			return Selector.setCurRecordArg0(null);
		namedAll = namedAircraft.keySet().toArray(namedAll);
		int i_320_ = 0;
		String string = actor.name();
		for (/**/; namedAll[i_320_] != null; i_320_++)
		{
			if (string.equals(namedAll[i_320_]))
				break;
		}
		if (namedAll[i_320_] == null)
			return Selector.setCurRecordArg0(null);
		i_320_++;
		if (namedAll.length == i_320_ || namedAll[i_320_] == null)
			i_320_ = 0;
		return Selector.setCurRecordArg0((Actor) Engine.name2Actor().get((String) namedAll[i_320_]));
	}

	private boolean isViewed(Actor actor)
	{
		if (!Actor.isValid(actor))
			return false;
		return (actor instanceof Aircraft || actor instanceof Paratrooper || actor instanceof ActorViewPoint || (actor instanceof BigshipGeneric && ((BigshipGeneric) actor).getAirport() != null));
	}

	// TODO: |ZUTI| methods and variables
	// ------------------------------------------------
	private int	zutiBombReleaseCounter	= 0;

	public FlightModel zutiGetFM()
	{
		return this.FM;
	}
}