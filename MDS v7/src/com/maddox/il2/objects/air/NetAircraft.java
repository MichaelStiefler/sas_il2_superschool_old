/*4.10.1 class*/
package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.AirportCarrier;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.Way;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Point_Stay;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorFilter;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModelTrack;
import com.maddox.il2.fm.Turret;
import com.maddox.il2.fm.ZutiSupportMethods_FM;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiSupportMethods_Multicrew;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetSquadron;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.NetWing;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.Ship.RwyTransp;
import com.maddox.il2.objects.ships.Ship.RwyTranspSqr;
import com.maddox.il2.objects.ships.Ship.RwyTranspWide;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Message;
import com.maddox.rts.MsgAction;
import com.maddox.rts.MsgNet;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetChannelOutStream;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgSpawn;
import com.maddox.rts.NetObj;
import com.maddox.rts.NetSpawn;
import com.maddox.rts.NetUpdate;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.util.IntHashtable;

public abstract class NetAircraft extends SndAircraft
{
	static long timeOfPrevSpawn = -1L;
	
	public static final int FM_AI = 0;
	public static final int FM_REAL = 1;
	public static final int FM_NET_CLIENT = 2;
	public static String loadingCountry;
	public static boolean loadingCoopPlane;
	protected String thisWeaponsName = null;
	protected boolean bPaintShemeNumberOn = true;
	private boolean bCoopPlane;
	private FlightModelTrack fmTrack;
	public static final int NETG_ID_TOMASTER = 128;
	public static final int NETG_ID_CODE_ASTATE = 0;
	public static final int NETG_ID_CODE_UPDATE_WAY = 1;
	public static final int NETG_ID_CODE_UPDATE_WEAPONS = 2;
	public static final int NETG_ID_CODE_GUNPODS_ON = 3;
	public static final int NETG_ID_CODE_GUNPODS_OFF = 4;
	public static final int NETG_ID_CODE_DROP_FUEL_TANKS = 5;
	public static final int NETG_ID_CODE_HIT = 6;
	public static final int NETG_ID_CODE_HIT_PROP = 7;
	public static final int NETG_ID_CODE_CUT = 8;
	public static final int NETG_ID_CODE_EXPLODE = 9;
	public static final int NETG_ID_CODE_DEAD = 10;
	public static final int NETG_ID_CODE_FIRST_UPDATE = 11;
	public static final int NETG_ID_CODE_COCKPIT_ENTER = 12;
	public static final int NETG_ID_CODE_COCKPIT_AUTO = 13;
	public static final int NETG_ID_CODE_COCKPIT_DRIVER = 14;
	public static final int NETG_ID_CODE_DROP_EXTERNAL_STORES = 15;
	protected boolean bGunPodsExist = false;
	protected boolean bGunPodsOn = true;
	private int netCockpitIndxPilot = 0;
	private int netCockpitWeaponControlNum = 0;
	private int netCockpitTuretNum = -1;
	private boolean netCockpitValid = false;
	private NetMsgGuaranted netCockpitMsg = null;
	private boolean bWeaponsEventLog = false;
	private Actor[] netCockpitDrivers = null;
	private static Point3d corn = new Point3d();
	private static Point3d corn1 = new Point3d();
	private static Point3d pship = new Point3d();
	private static Loc lCorn = new Loc();
	static ClipFilter clipFilter = new ClipFilter();
	private ArrayList damagers = new ArrayList();
	private Actor damagerExclude = null;
	private Actor damager_ = null;
	static Class class$com$maddox$il2$objects$air$CockpitPilot;
	
	//TODO: |ZUTI| variables
	public static boolean ZUTI_REFLY_OWERRIDE = false;
	
	static class DamagerItem
	{
		public Actor damager;
		public int damage;
		public long lastTime;
		
		public DamagerItem(Actor actor, int i)
		{
			damager = actor;
			damage = i;
			lastTime = Time.current();
		}
	}
	
	public static class SPAWN implements ActorSpawn, NetSpawn
	{
		public Class cls;
		private NetUser _netUser;
		
		public SPAWN(Class var_class)
		{
			cls = var_class;
			Spawn.add(cls, this);
		}
		
		private Actor actorSpawnCoop(ActorSpawnArg actorspawnarg)
		{
			if (actorspawnarg.name == null)
				return null;
			String string = actorspawnarg.name.substring(3);
			boolean bool = false;
			NetAircraft netaircraft = (NetAircraft)Actor.getByName(string);
			if (netaircraft == null)
			{
				netaircraft = (NetAircraft)Actor.getByName(" " + string);
				if (netaircraft != null)
					bool = true;
			}
			if (netaircraft == null)
				return null;
			actorspawnarg.name = null;
			Wing wing = netaircraft.getWing();
			NetAircraft.loadingCountry = wing.regiment().country();
			NetAircraft netaircraft_0_;
			try
			{
				netaircraft_0_ = (NetAircraft)cls.newInstance();
			}
			catch (Exception exception)
			{
				NetAircraft.loadingCountry = null;
				printDebug(exception);
				return null;
			}
			netaircraft_0_.bCoopPlane = true;
			int i = netaircraft.aircIndex();
			if (!bool)
			{
				netaircraft.setName(" " + string);
				netaircraft.collide(false);
			}
			netaircraft_0_.setName(string);
			if (actorspawnarg.bPlayer && actorspawnarg.netChannel == null)
			{
				World.setPlayerAircraft((Aircraft)netaircraft_0_);
				netaircraft_0_.setFM(1, true);
				World.setPlayerFM();
			}
			else if (Mission.isServer())
				netaircraft_0_.setFM(1, actorspawnarg.netChannel == null);
			else if (_netUser != null && _netUser.isTrackWriter())
			{
				World.setPlayerAircraft((Aircraft)netaircraft_0_);
				netaircraft_0_.setFM(1, false);
				World.setPlayerFM();
			}
			else
				netaircraft_0_.setFM(2, actorspawnarg.netChannel == null);
			netaircraft_0_.FM.M.fuel = actorspawnarg.fuel * netaircraft_0_.FM.M.maxFuel;
			
			netaircraft_0_.bPaintShemeNumberOn = actorspawnarg.bNumberOn;
			netaircraft_0_.FM.AS.bIsEnableToBailout = netaircraft.FM.AS.bIsEnableToBailout;
			netaircraft_0_.createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
			((AircraftNet)netaircraft_0_.net).netUser = _netUser;
			((AircraftNet)netaircraft_0_.net).netName = string;
			netaircraft_0_.FM.setSkill(netaircraft.FM.Skill);
			try
			{
				netaircraft_0_.weaponsLoad(actorspawnarg.weapons);
				netaircraft_0_.thisWeaponsName = actorspawnarg.weapons;
				
				// System.out.println("WEAPONS: " + actorspawnarg.weapons);
			}
			catch (Exception exception)
			{
				printDebug(exception);
			}
			if (_netUser != null && (netaircraft_0_.net.isMaster() || _netUser.isTrackWriter()))
				netaircraft_0_.createCockpits();
			netaircraft_0_.FM.AP.way = new Way(netaircraft.FM.AP.way);
			netaircraft_0_.onAircraftLoaded();
			wing.airc[i] = (Aircraft)netaircraft_0_;
			netaircraft_0_.setArmy(netaircraft.getArmy());
			netaircraft_0_.setOwner(wing);
			if (_netUser != null && (netaircraft_0_.net.isMaster() || _netUser.isTrackWriter()))
				World.setPlayerRegiment();
			if (Mission.isServer())
				((Maneuver)netaircraft.FM).Group.changeAircraft((Aircraft)netaircraft, (Aircraft)netaircraft_0_);
			netaircraft_0_.FM.CT.set(netaircraft.FM.CT);
			netaircraft_0_.FM.CT.forceGear(netaircraft_0_.FM.CT.GearControl);
			if ((Aircraft)netaircraft_0_ != null)
			{
				/* empty */
			}
			Aircraft.forceGear(netaircraft_0_.getClass(), netaircraft_0_.hierMesh(), netaircraft_0_.FM.CT.getGear());
			netaircraft_0_.pos.setAbs(netaircraft.pos.getAbs());
			netaircraft_0_.pos.reset();
			Vector3d vector3d = new Vector3d();
			netaircraft.getSpeed(vector3d);
			netaircraft_0_.setSpeed(vector3d);
			if (netaircraft.FM.brakeShoe)
			{
				((Aircraft)netaircraft_0_).FM.AP.way.takeoffAirport = netaircraft.FM.AP.way.takeoffAirport;
				((Aircraft)netaircraft_0_).FM.brakeShoe = true;
				((Aircraft)netaircraft_0_).FM.turnOffCollisions = true;
				((Aircraft)netaircraft_0_).FM.brakeShoeLoc.set(netaircraft.FM.brakeShoeLoc);
				((Aircraft)netaircraft_0_).FM.brakeShoeLastCarrier = netaircraft.FM.brakeShoeLastCarrier;
				((Aircraft)netaircraft_0_).FM.Gears.bFlatTopGearCheck = true;
				((Aircraft)netaircraft_0_).makeMirrorCarrierRelPos();
			}
			if (netaircraft.FM.CT.bHasWingControl)
			{
				((Aircraft)netaircraft_0_).FM.CT.wingControl = netaircraft.FM.CT.wingControl;
				((Aircraft)netaircraft_0_).FM.CT.forceWing(netaircraft.FM.CT.wingControl);
			}
			netaircraft_0_.preparePaintScheme();
			netaircraft_0_.prepareCamouflage();
			NetAircraft.loadingCountry = null;
			if (_netUser != null)
			{
				_netUser.tryPrepareSkin(netaircraft_0_);
				_netUser.tryPrepareNoseart(netaircraft_0_);
				_netUser.tryPreparePilot(netaircraft_0_);
				_netUser.setArmy(netaircraft_0_.getArmy());
			}
			else if (Config.isUSE_RENDER())
				Mission.cur().prepareSkinAI((Aircraft)netaircraft_0_);
			netaircraft_0_.restoreLinksInCoopWing();
			if (netaircraft_0_.net.isMaster() && (!World.cur().diffCur.Takeoff_N_Landing || netaircraft.FM.AP.way.get(0).Action != 1 || !netaircraft.FM.isStationedOnGround()))
			{
				netaircraft_0_.FM.EI.setCurControlAll(true);
				netaircraft_0_.FM.EI.setEngineRunning();
				netaircraft_0_.FM.CT.setPowerControl(0.75F);
				netaircraft_0_.FM.setStationedOnGround(false);
				netaircraft_0_.FM.setWasAirborne(true);
			}
			
			return netaircraft_0_;
		}
		
		private void netSpawnCoop(int i, NetMsgInput netmsginput)
		{
			try
			{
				ActorSpawnArg actorspawnarg = new ActorSpawnArg();
				actorspawnarg.fuel = netmsginput.readFloat();
				actorspawnarg.bNumberOn = netmsginput.readBoolean();
				actorspawnarg.name = "net" + netmsginput.read255();
				actorspawnarg.weapons = netmsginput.read255();
				_netUser = (NetUser)netmsginput.readNetObj();
				actorspawnarg.netChannel = netmsginput.channel();
				actorspawnarg.netIdRemote = i;
				NetAircraft netaircraft = (NetAircraft)actorSpawnCoop(actorspawnarg);
				netSpawnCommon(netmsginput, actorspawnarg);
				netaircraft.pos.setAbs(actorspawnarg.point, actorspawnarg.orient);
				netaircraft.pos.reset();
				netaircraft.setSpeed(actorspawnarg.speed);
				netSpawnCommon(netmsginput, actorspawnarg, netaircraft);
			}
			catch (Exception exception)
			{
				printDebug(exception);
			}
		}
		
		private Actor _actorSpawn(ActorSpawnArg actorspawnarg)
		{
			NetSquadron netsquadron = null;
			NetWing netwing = null;
			int i;
			NetAircraft netaircraft;
			try
			{
				int i_2_ = actorspawnarg.name.length();
				if (_netUser != null)
				{
					i = Integer.parseInt(actorspawnarg.name.substring(i_2_ - 2, i_2_));
					int i_3_ = actorspawnarg.name.charAt(i_2_ - 3) - 48;
					int i_4_ = actorspawnarg.name.charAt(i_2_ - 4) - 48;
					Regiment regiment;
					if (i_2_ == 4)
						regiment = _netUser.netUserRegiment;
					else
					{
						String string = actorspawnarg.name.substring(0, i_2_ - 4);
						regiment = (Regiment)Actor.getByName(string);
					}
					netsquadron = new NetSquadron(regiment, i_4_);
					netwing = new NetWing(netsquadron, i_3_);
				}
				else
				{
					i = Integer.parseInt(actorspawnarg.name.substring(i_2_ - 1, i_2_)) + 1;
					int i_5_ = actorspawnarg.name.charAt(i_2_ - 2) - 48;
					int i_6_ = actorspawnarg.name.charAt(i_2_ - 3) - 48;
					i += i_6_ * 16 + i_5_ * 4;
					String string = actorspawnarg.name.substring(0, i_2_ - 3);
					Regiment regiment = (Regiment)Actor.getByName(string);
					netsquadron = new NetSquadron(regiment, i_6_);
					netwing = new NetWing(netsquadron, i_5_);
				}
				NetAircraft.loadingCountry = netsquadron.regiment().country();
				
				netaircraft = (NetAircraft)cls.newInstance();
			}
			catch (Exception exception)
			{
				if (netsquadron != null)
					netsquadron.destroy();
				if (netwing != null)
					netwing.destroy();
				NetAircraft.loadingCountry = null;
				printDebug(exception);
				return null;
			}
			netaircraft.bCoopPlane = false;
			netaircraft.createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
			((AircraftNet)netaircraft.net).netUser = _netUser;
			((AircraftNet)netaircraft.net).netName = actorspawnarg.name;
			if (_netUser != null)
			{
				actorspawnarg.name = null;
				makeName(netaircraft);
			}
			if (actorspawnarg.bPlayer && actorspawnarg.netChannel == null || _netUser != null && _netUser.isTrackWriter())
			{
				World.cur().resetUser();
				World.setPlayerAircraft((Aircraft)netaircraft);
				netaircraft.setFM(1, actorspawnarg.netChannel == null);
				World.setPlayerFM();
				actorspawnarg.bPlayer = false;
			}
			// TODO: Added by |ZUTI|
			// ---------------------------------------------------------
			else if (Mission.isServer())
				netaircraft.setFM(1, actorspawnarg.netChannel == null);
			// ---------------------------------------------------------
			else
				netaircraft.setFM(2, actorspawnarg.netChannel == null);
			netaircraft.FM.setSkill(3);
			netaircraft.FM.M.fuel = actorspawnarg.fuel * netaircraft.FM.M.maxFuel;
			
			netaircraft.bPaintShemeNumberOn = actorspawnarg.bNumberOn;
			
			// TODO: Added by |ZUTI|: adding info about multiCrew positions
			// -----------------------------------------------------------------------
			netaircraft.FM.AS.zutiSetMultiCrew(actorspawnarg.bZutiMultiCrew);
			netaircraft.FM.AS.zutiSetMultiCrewAnytime(actorspawnarg.bZutiMultiCrewAnytime);
			// -----------------------------------------------------------------------
			
			try
			{
				netaircraft.weaponsLoad(actorspawnarg.weapons);
				netaircraft.thisWeaponsName = actorspawnarg.weapons;
				// System.out.println("WEAPONS: " + actorspawnarg.weapons);
				// NetAircraft.zutiShowWeapons(netaircraft.FM.CT.Weapons);
			}
			catch (Exception exception)
			{
				printDebug(exception);
			}
			if (netaircraft.net.isMaster() || _netUser != null && _netUser.isTrackWriter())
				netaircraft.createCockpits();
			netaircraft.onAircraftLoaded();
			NetAircraft.loadingCountry = null;
			boolean airStarting = false;
			BornPlace bornplace = null;
			if (actorspawnarg.bornPlaceExist)
			{
				bornplace = ((BornPlace)World.cur().bornPlaces.get(actorspawnarg.bornPlace));
				//System.out.println("------------------------------NetAircraft - spawn place designation: " + actorspawnarg.stayPlace);
				Loc loc = bornplace.getAircraftPlace((Aircraft)netaircraft, actorspawnarg.stayPlace);
				actorspawnarg.point = loc.getPoint();
				actorspawnarg.orient = loc.getOrient();
				
				// TODO: Added by |ZUTI|: check if you are spawning on my own defined spawn place
				// ---------------------------------------
				if( actorspawnarg.stayPlace > 0 )
				{
					Point_Stay[] zutiPss = World.cur().airdrome.stay[actorspawnarg.stayPlace];
					Point_Stay zutiPs = zutiPss[0];
					if (zutiPs.zutiLocation != null)
					{
						actorspawnarg.point = zutiPs.zutiLocation.getPoint();
						actorspawnarg.orient = zutiPs.zutiLocation.getOrient();
						// actorspawnarg.point.z = 288F;
						
						// System.out.println("SET!");
					}
				}
				// ---------------------------------------
				
				actorspawnarg.armyExist = true;
				actorspawnarg.army = bornplace.army;
				actorspawnarg.speed = new Vector3d();
				
				// TODO: Edited by |ZUTI|: added my checks for air spawn occasions
				if(	actorspawnarg.bZutiAirstart ||
					bornplace.zutiAirspawnOnly || 
					!World.cur().diffCur.Takeoff_N_Landing || 
					(!bornplace.zutiIsStandAloneBornPlace && (actorspawnarg.stayPlace >= World.cur().airdrome.stayHold.length) && !netaircraft.FM.brakeShoe) )
				{
					// TODO: Edited by |ZUTI|
					actorspawnarg.point.x = bornplace.place.x;
					actorspawnarg.point.y = bornplace.place.y;
					actorspawnarg.point.z = bornplace.zutiSpawnHeight;
					// Need to divide this with 3 to get aprox speed at kmh
					actorspawnarg.speed.x = bornplace.zutiSpawnSpeed / 3.6D;
					actorspawnarg.orient = new Orient(bornplace.zutiSpawnOrient - 90F, 0F, 0F);
					actorspawnarg.orient.transform(actorspawnarg.speed);
					
					airStarting = true;
				}
				else
				{
					netaircraft.FM.CT.setLanded();
                    Aircraft.forceGear(netaircraft.getClass(), netaircraft.hierMesh(), netaircraft.FM.CT.getGear());
                    AirportCarrier airportcarrier1 = (AirportCarrier)Airport.nearest(loc.getPoint(), -1, 4);
                    if(airportcarrier1 != null && airportcarrier1.ship() != null && ((airportcarrier1.ship() instanceof RwyTransp) || (airportcarrier1.ship() instanceof RwyTranspWide) || (airportcarrier1.ship() instanceof RwyTranspSqr)) && Engine.land().isWater(actorspawnarg.point.x, actorspawnarg.point.y))
                        netaircraft.FM.brakeShoe = false;
				}
				netaircraft.FM.AS.bIsEnableToBailout = bornplace.bParachute;
			}
			else
			{
				if (Mission.isDogfight() && World.cur().diffCur.Takeoff_N_Landing && Main.cur().netServerParams.isMaster())
				{
					Loc loc = new Loc(actorspawnarg.point.x, actorspawnarg.point.y, 0.0D, 0.0F, 0.0F, 0.0F);
					AirportCarrier airportcarrier = (AirportCarrier)Airport.nearest(loc.getPoint(), -1, 4);
					if (airportcarrier != null && !NetAircraft.isOnCarrierDeck(airportcarrier, loc))
						airportcarrier = null;
					if (airportcarrier != null)
						airportcarrier.setCellUsed((Aircraft)netaircraft);
				}
			}
			actorspawnarg.set(netaircraft);
			
			// TODO: Added by |ZUTI|
			netwing.setPlane(netaircraft, i);
			
			if (airStarting)
			{
				netaircraft.FM.EI.setCurControlAll(true);
				netaircraft.FM.EI.setEngineRunning();
				netaircraft.FM.CT.setPowerControl(0.75F);
				netaircraft.FM.setStationedOnGround(false);
				netaircraft.FM.setWasAirborne(true);
			}
			if (actorspawnarg.speed == null)
				netaircraft.setSpeed(new Vector3d());
			if (_netUser != null)
			{
				_netUser.tryPrepareSkin(netaircraft);
				_netUser.tryPrepareNoseart(netaircraft);
				_netUser.tryPreparePilot(netaircraft);
				_netUser.setArmy(netaircraft.getArmy());
			}
			else if (Config.isUSE_RENDER())
				Mission.cur().prepareSkinAI((Aircraft)netaircraft);
			if (netaircraft.net.isMaster() || _netUser != null && _netUser.isTrackWriter())
				World.setPlayerRegiment();
			
			// TODO: Edit by |ZUTI|
			if (netaircraft != null)
			{
				// If we are not spawning on carrier, align us with ground
				if (netaircraft.FM.brakeShoeLastCarrier == null && !airStarting && bornplace != null && bornplace.zutiIsStandAloneBornPlace)
				{
					ZutiSupportMethods_Air.alignAircraftToLandscape(netaircraft);
					// System.out.println("NetAircraft aligned to terrain.");
				}
				// Report user resources that he can have for his AC
				if (Mission.isServer())
					ZutiSupportMethods_NetSend.reportSpawnResources((Aircraft)netaircraft);

				//Disable chocks if air starting disables hanged up ac in the sky in some cases!
				if( airStarting )
					World.getPlayerFM().brakeShoe = false;
				
				//Set up deck related limitations. Only for carrier based home bases.
				if( bornplace != null && bornplace.zutiAlreadyAssigned )
				{
					if( !bornplace.zutiSpawnAcWithFoldedWings || actorspawnarg.bZutiAirstart )
					{
						netaircraft.FM.CT.wingControl = 0.0F;
						netaircraft.FM.CT.forceWing(0.0F);
					}
					
					if( actorspawnarg.bZutiAirstart && netaircraft.FM.CT.bHasGearControl)
					{
						//Store gears
						netaircraft.FM.CT.GearControl = 0.0F;
					}
					
					ZutiSupportMethods_FM.UPDATE_DECK_TIMER = bornplace.zutiEnableQueue;
					ZutiSupportMethods_FM.DECK_CLEAR_TIME = bornplace.zutiDeckClearTimeout * 1000;
					ZutiSupportMethods_FM.DECK_LAST_REFRESH = Time.current();
					ZutiSupportMethods_FM.UPDATE_VULNERABILITY_TIMER = bornplace.zutiPilotInVulnerableWhileOnTheDeck;
					
					if( ZutiSupportMethods_FM.UPDATE_VULNERABILITY_TIMER && World.cur().diffCur.Vulnerability )
					{
						ZutiSupportMethods_FM.VULNERABILITY_LAST_REFRESH = Time.current();
						ZutiSupportMethods_FM.IS_PLAYER_VULNERABLE = false;
						//System.out.println("Player is invulnerable!!!!");
					}
				}
				else
				{
					ZutiSupportMethods_FM.UPDATE_VULNERABILITY_TIMER = false;
					ZutiSupportMethods_FM.IS_PLAYER_VULNERABLE = true;
				}
			}
			
			return netaircraft;
		}
		
		private void makeName(NetAircraft netaircraft)
		{
			String string = ((AircraftNet)netaircraft.net).netUser.uniqueName();
			int i;
			for (i = 0; Actor.getByName(string + "_" + i) != null; i++)
			{
				/* empty */
			}
			netaircraft.setName(string + "_" + i);
		}
		
		private void _netSpawn(int i, NetMsgInput netmsginput)
		{
			try
			{
				ActorSpawnArg actorspawnarg = new ActorSpawnArg();
				actorspawnarg.army = netmsginput.readByte();
				actorspawnarg.armyExist = true;
				actorspawnarg.fuel = netmsginput.readFloat();
				actorspawnarg.bNumberOn = netmsginput.readBoolean();
				actorspawnarg.name = netmsginput.read255();
				actorspawnarg.weapons = netmsginput.read255();
				_netUser = (NetUser)netmsginput.readNetObj();
				actorspawnarg.netChannel = netmsginput.channel();
				actorspawnarg.netIdRemote = i;
				netSpawnCommon(netmsginput, actorspawnarg);
				NetAircraft netaircraft = (NetAircraft)_actorSpawn(actorspawnarg);
				if (netaircraft != null)
				{
					netSpawnCommon(netmsginput, actorspawnarg, netaircraft);
				}
			}
			catch (Exception exception)
			{
				printDebug(exception);
			}
		}
		
		public Actor actorSpawn(ActorSpawnArg actorspawnarg)
		{
			if (!Mission.isNet())
				return null;
			if (Main.cur().netServerParams == null)
				return null;
			if (actorspawnarg.netChannel == null && actorspawnarg.bPlayer)
				_netUser = (NetUser)NetEnv.host();
			Actor actor = null;
			if (Main.cur().netServerParams.isDogfight())
				actor = _actorSpawn(actorspawnarg);
			else if (Main.cur().netServerParams.isCoop())
				actor = actorSpawnCoop(actorspawnarg);
			_netUser = null;
			if (actor != null && actor == World.getPlayerAircraft() && NetMissionTrack.isRecording() && Main.cur().netServerParams.isDogfight())
			{
				try
				{
					NetMsgSpawn netmsgspawn = actor.netReplicate(NetMissionTrack.netChannelOut());
					actor.net.postTo(NetMissionTrack.netChannelOut(), netmsgspawn);
				}
				catch (Exception exception)
				{
					printDebug(exception);
				}
			}
			return actor;
		}
		
		public void netSpawn(int i, NetMsgInput netmsginput)
		{
			if (Main.cur().netServerParams != null)
			{
				if (netmsginput.channel() instanceof NetChannelInStream && NetMissionTrack.playingVersion() == 100)
				{
					if (Main.cur().netServerParams.isCoop())
						netSpawnCoop(i, netmsginput);
					else
						_netSpawn(i, netmsginput);
				}
				else
				{
					try
					{
						byte i_8_ = netmsginput.readByte();
						if ((i_8_ & 0x1) == 1)
							netSpawnCoop(i, netmsginput);
						else
							_netSpawn(i, netmsginput);
					}
					catch (Exception exception)
					{
						printDebug(exception);
						return;
					}
				}
				_netUser = null;
			}
		}
	}
	
	public class Mirror extends AircraftNet
	{
		NetMsgFiltered out = new NetMsgFiltered();
		private long tupdate = -1L;
		private long _t;
		private long tcur;
		private Point3f _p = new Point3f();
		private Vector3f _v = new Vector3f();
		private Orient _o = new Orient();
		private Vector3f _w = new Vector3f();
		private Vector3f TmpV = new Vector3f();
		private Vector3d TmpVd = new Vector3d();
		private float save_dt = 0.0010F;
		private float saveCoeff = 1.0F;
		private boolean bGround = false;
		private boolean bUnderDeck = false;
		private long tint;
		private long tlag;
		private boolean bFirstUpdate = true;
		private Loc _lRel = new Loc();
		
		public void makeFirstUnderDeck()
		{
			if (FM.brakeShoe)
			{
				NetAircraft.corn.set(pos.getAbsPoint());
				NetAircraft.corn1.set(pos.getAbsPoint());
				NetAircraft.corn1.z -= 20.0;
				Actor actor = Engine.collideEnv().getLine(NetAircraft.corn, NetAircraft.corn1, false, NetAircraft.clipFilter, NetAircraft.pship);
				if (!(actor instanceof BigshipGeneric) && Mission.isCoop() && Time.current() < 60000L)
					actor = FM.brakeShoeLastCarrier;
				if (actor instanceof BigshipGeneric)
				{
					bUnderDeck = true;
					_lRel.set(pos.getAbs());
					_lRel.sub(actor.pos.getAbs());
				}
			}
		}
		
		public void netFirstUpdate(float f, float f_9_, float f_10_, float f_11_, float f_12_, float f_13_, float f_14_, float f_15_, float f_16_)
		{
			FM.Vwld.set((double)f_14_, (double)f_15_, (double)f_16_);
			FM.getAccel().set(0.0, 0.0, 0.0);
			_t = tcur = tupdate = Time.current();
			_p.set(f, f_9_, f_10_);
			_v.set(FM.Vwld);
			_o.set(f_11_, f_12_, f_13_);
			_w.set(0.0F, 0.0F, 0.0F);
			FM.Loc.set((double)f, (double)f_9_, (double)f_10_);
			FM.Or.set(f_11_, f_12_, f_13_);
			tint = tcur;
			tlag = 0L;
		}
		
		public boolean netInput(NetMsgInput netmsginput) throws IOException
		{
			if (netmsginput.isGuaranted())
				return netGetGMsg(netmsginput, false);
			if (Time.isPaused() && !NetMissionTrack.isPlaying())
				return true;
			if (netmsginput.channel() != masterChannel())
			{
				postRealTo(Message.currentTime(true), masterChannel(), new NetMsgFiltered(netmsginput, 0));
				return true;
			}
			if (isMirrored())
			{
				out.unLockAndSet(netmsginput, 0);
				postReal(Message.currentTime(true), out);
			}
			int i = netmsginput.readByte();
			int i_17_ = netmsginput.readUnsignedByte();
			bGround = (i & 0x20) != 0;
			bUnderDeck = (i & 0x40) != 0;
			if (isFMTrackMirror())
			{
				netmsginput.readUnsignedByte();
				netmsginput.readUnsignedByte();
			}
			else
			{
				netControls(netmsginput.readUnsignedByte());
				netWeaponControl(netmsginput.readUnsignedByte());
				if (bFirstUpdate)
				{
					Aircraft aircraft = (Aircraft)superObj();
					aircraft.FM.CT.forceGear(aircraft.FM.CT.GearControl);
					if (aircraft != null)
					{
						/* empty */
					}
					Aircraft.forceGear(aircraft.getClass(), aircraft.hierMesh(), aircraft.FM.CT.getGear());
				}
			}
			float f = netmsginput.readFloat();
			float f_18_ = netmsginput.readFloat();
			float f_19_ = netmsginput.readFloat();
			short i_20_ = netmsginput.readShort();
			short i_21_ = netmsginput.readShort();
			short i_22_ = netmsginput.readShort();
			float f_23_ = -((float)i_20_ * 180.0F / 32000.0F);
			float f_24_ = (float)i_21_ * 90.0F / 32000.0F;
			float f_25_ = (float)i_22_ * 180.0F / 32000.0F;
			short i_26_ = netmsginput.readShort();
			short i_27_ = netmsginput.readShort();
			short i_28_ = netmsginput.readShort();
			float f_29_ = (float)i_26_ * 50.0F / 32000.0F;
			float f_30_ = (float)i_27_ * 50.0F / 32000.0F;
			float f_31_ = (float)i_28_ * 50.0F / 32000.0F;
			if (bUnderDeck)
				f_29_ = f_30_ = f_31_ = 0.0F;
			short i_32_ = netmsginput.readShort();
			short i_33_ = netmsginput.readShort();
			short i_34_ = netmsginput.readShort();
			float f_35_ = (float)i_32_ * 400.0F / 32000.0F;
			float f_36_ = (float)i_33_ * 400.0F / 32000.0F;
			float f_37_ = (float)i_34_ * 400.0F / 32000.0F;
			if (bGround && !bUnderDeck)
				f_37_ = 0.0F;
			short i_38_ = netmsginput.readShort();
			short i_39_ = netmsginput.readShort();
			short i_40_ = netmsginput.readShort();
			float f_41_ = (float)i_38_ * 2000.0F / 32000.0F;
			float f_42_ = (float)i_39_ * 2000.0F / 32000.0F;
			float f_43_ = (float)i_40_ * 2000.0F / 32000.0F;
			if (bGround || bUnderDeck)
			{
				f_41_ = 0.0F;
				f_42_ = 0.0F;
				f_43_ = 0.0F;
			}
			if (bUnderDeck)
				_lRel.set((double)i_38_ * 200.0 / 32000.0, (double)i_39_ * 200.0 / 32000.0, (double)i_40_ * 200.0 / 32000.0, -((float)i_26_ * 180.0F / 32000.0F), (float)i_27_ * 90.0F / 32000.0F, (float)i_28_ * 180.0F / 32000.0F);
			long l = Message.currentTime(false) + (long)i_17_;
			_t = l;
			if (NetEnv.testLag)
			{
				long l_44_ = Time.tickNext() - l;
				if (l_44_ < 0L)
					l_44_ = 0L;
				if (bFirstUpdate || tlag >= l_44_)
				{
					bFirstUpdate = false;
					tlag = l_44_;
				}
				else if (l > tupdate)
				{
					double d = ((double)(l_44_ - (tcur - tint)) / (double)(l - tupdate));
					if (d > 0.015)
						d = 0.015;
					long l_45_ = (long)((double)(l - tupdate) * d);
					if (l_45_ > (long)(Time.tickConstLen() / 2))
						l_45_ = (long)(Time.tickConstLen() / 2);
					tlag = tcur - tint + l_45_;
					if (tlag >= l_44_)
						tlag = l_44_;
				}
			}
			else
				bFirstUpdate = false;
			tupdate = _t;
			FM.Vwld.set((double)f_35_, (double)f_36_, (double)f_37_);
			FM.getAccel().set((double)f_41_, (double)f_42_, (double)f_43_);
			_p.set(f, f_18_, f_19_);
			_v.set(FM.Vwld);
			_o.set(f_23_, f_24_, f_25_);
			_o.transformInv(FM.Vwld, FM.getVflow());
			_w.set(f_29_, f_30_, f_31_);
			FM.getW().set((double)f_29_, (double)f_30_, (double)f_31_);
			int i_46_ = i & 0xf;
			if (i_46_ == 1)
			{
				float f_47_ = (float)netmsginput.readUnsignedByte() / 255.0F * 640.0F;
				float f_48_ = (float)netmsginput.readUnsignedByte() / 255.0F * 1.6F;
				i_46_ = FM.EI.getNum();
				for (int i_49_ = 0; i_49_ < i_46_; i_49_++)
				{
					if (!isFMTrackMirror())
					{
						FM.EI.engines[i_49_].setw(f_47_);
						FM.EI.engines[i_49_].setPropPhi(f_48_);
					}
				}
			}
			else
			{
				for (int i_50_ = 0; i_50_ < i_46_; i_50_++)
				{
					float f_51_ = ((float)netmsginput.readUnsignedByte() / 255.0F * 640.0F);
					float f_52_ = ((float)netmsginput.readUnsignedByte() / 255.0F * 1.6F);
					if (!isFMTrackMirror())
					{
						FM.EI.engines[i_50_].setw(f_51_);
						FM.EI.engines[i_50_].setPropPhi(f_52_);
					}
				}
			}
			if ((i & 0x10) != 0 && netCockpitTuretNum >= 0)
			{
				int i_53_ = netmsginput.readUnsignedShort();
				int i_54_ = netmsginput.readUnsignedShort();
				float f_55_ = unpackSY(i_53_);
				float f_56_ = unpackSP(i_54_ & 0x7fff);
				FM.CT.WeaponControl[netCockpitWeaponControlNum] = (i_54_ & 0x8000) != 0;
				if (superObj() == World.getPlayerAircraft())
				{
					Actor._tmpOrient.set(f_55_, f_56_, 0.0F);
					((CockpitGunner)Main3D.cur3D().cockpits[netCockpitIndxPilot]).moveGun(Actor._tmpOrient);
				}
				else
				{
					Turret turret = FM.turret[netCockpitTuretNum];
					turret.tu[0] = f_55_;
					turret.tu[1] = f_56_;
				}
			}
			return true;
		}
		
		float unpackSY(int i)
		{
			return (float)((double)i * 360.0 / 65000.0 - 180.0);
		}
		
		float unpackSP(int i)
		{
			return (float)((double)i * 360.0 / 32000.0 - 180.0);
		}
		
		public void fmUpdate(float f)
		{
			if (tupdate < 0L)
				netFirstUpdate((float)FM.Loc.x, (float)FM.Loc.y, (float)FM.Loc.z, FM.Or.getAzimut(), FM.Or.getTangage(), FM.Or.getKren(), (float)FM.Vwld.x, (float)FM.Vwld.y, (float)FM.Vwld.z);
			f = (float)(Time.tickNext() - tcur) * 0.0010F;
			if (!(f < 0.0010F))
			{
				tcur = Time.tickNext();
				FM.CT.update(f, 50.0F, FM.EI, false, isFMTrackMirror());
				FM.Gears.ground(FM, false, bGround);
				FM.Gears.bFlatTopGearCheck = false;
				for (int i = 0; i < 3; i++)
				{
					FM.Gears.gWheelAngles[i] = ((FM.Gears.gWheelAngles[i] + (float)Math.toDegrees(Math.atan((FM.Gears.gVelocity[i]) * (double)f / 0.375))) % 360.0F);
					FM.Gears.gVelocity[i] *= 0.949999988079071;
				}
				NetAircraft.this.hierMesh().chunkSetAngles("GearL1_D0", 0.0F, -(FM.Gears.gWheelAngles[0]), 0.0F);
				NetAircraft.this.hierMesh().chunkSetAngles("GearR1_D0", 0.0F, -(FM.Gears.gWheelAngles[1]), 0.0F);
				NetAircraft.this.hierMesh().chunkSetAngles("GearC1_D0", 0.0F, -(FM.Gears.gWheelAngles[2]), 0.0F);
				float f_57_ = FM.Gears.getSteeringAngle();
				moveSteering(f_57_);
				if (FM.Gears.nearGround())
					moveWheelSink();
				FM.EI.netupdate(f, isFMTrackMirror());
				FM.FMupdate(f);
				long l;
				for (tint = tcur - tlag; tint > _t; _t += l)
				{
					l = tint - _t;
					if (l > (long)Time.tickConstLen())
						l = (long)Time.tickConstLen();
					float f_58_ = (float)l * 0.0010F;
					_p.x += _v.x * f_58_;
					_p.y += _v.y * f_58_;
					_p.z += _v.z * f_58_;
					_v.x += FM.getAccel().x * (double)f_58_;
					_v.y += FM.getAccel().y * (double)f_58_;
					_v.z += FM.getAccel().z * (double)f_58_;
					TmpV.scale(f_58_, _w);
					_o.increment((float)-Math.toDegrees((double)TmpV.z), (float)-Math.toDegrees((double)TmpV.y), (float)Math.toDegrees((double)TmpV.x));
				}
				World.land();
				float f_59_ = Landscape.HQ(_p.x, _p.y);
				if (World.land().isWater((double)_p.x, (double)_p.y))
				{
					if (_p.z < f_59_ - 20.0F)
						_p.z = f_59_ - 20.0F;
				}
				else if (_p.z < f_59_ + 1.0F)
					_p.z = f_59_ + 1.0F;
				TmpVd.set(_p);
				save_dt = 0.98F * save_dt + 0.02F * ((float)(tint - tupdate) * 0.0010F);
				f_57_ = 0.03F;
				if (_v.length() > 0.0F)
				{
					f_57_ = 1.08F - save_dt * 2.0F;
					if (f_57_ > 1.0F)
						f_57_ = 1.0F;
					if (f_57_ < 0.03F)
						f_57_ = 0.03F;
				}
				saveCoeff = 0.98F * saveCoeff + 0.02F * f_57_;
				FM.Loc.interpolate(TmpVd, saveCoeff);
				float f_60_ = saveCoeff * 2.0F;
				if (NetMissionTrack.isPlaying())
					f_60_ = saveCoeff / 4.0F;
				if (f_60_ > 1.0F)
					f_60_ = 1.0F;
				FM.Or.interpolate(_o, f_60_);
				if (bUnderDeck)
				{
					NetAircraft.corn.set(FM.Loc);
					NetAircraft.corn1.set(FM.Loc);
					NetAircraft.corn1.z -= 20.0;
					Actor actor = Engine.collideEnv().getLine(NetAircraft.corn, NetAircraft.corn1, false, NetAircraft.clipFilter, NetAircraft.pship);
					if (!(actor instanceof BigshipGeneric) && Mission.isCoop() && Time.current() < 60000L)
						actor = FM.brakeShoeLastCarrier;
					if (actor instanceof BigshipGeneric)
					{
						NetAircraft.lCorn.set(_lRel);
						NetAircraft.lCorn.add(actor.pos.getAbs());
						FM.Loc.set(NetAircraft.lCorn.getPoint());
						FM.Or.set(NetAircraft.lCorn.getOrient());
						saveCoeff = 1.0F;
						_p.set(FM.Loc);
						_o.set(FM.Or);
						actor.getSpeed(FM.Vwld);
						_v.x = (float)FM.Vwld.x;
						_v.y = (float)FM.Vwld.y;
						_v.z = (float)FM.Vwld.z;
					}
				}
				if (isFMTrackMirror())
					fmTrack.FMupdate(FM);
				if (FM.isTick(44, 0))
				{
					FM.AS.update(f * 44.0F);
					((Aircraft)superObj()).rareAction(f * 44.0F, false);
					if (FM.Loc.z - Engine.land().HQ_Air(FM.Loc.x, FM.Loc.y) > 40.0)
					{
						FM.setWasAirborne(true);
						FM.setStationedOnGround(false);
					}
					else if (FM.Vwld.length() < 1.0)
						FM.setStationedOnGround(true);
				}
			}
		}
		
		public void netControls(int i)
		{
			FM.CT.GearControl = (i & 0x1) != 0 ? 1.0F : 0.0F;
			FM.CT.FlapsControl = (i & 0x2) != 0 ? 1.0F : 0.0F;
			FM.CT.BrakeControl = (i & 0x4) != 0 ? 1.0F : 0.0F;
			FM.CT.setRadiatorControl((i & 0x8) != 0 ? 1.0F : 0.0F);
			FM.CT.BayDoorControl = (i & 0x10) != 0 ? 1.0F : 0.0F;
			FM.CT.AirBrakeControl = (i & 0x20) != 0 ? 1.0F : 0.0F;
		}
		
		public void netWeaponControl(int i)
		{
			int i_61_ = FM.CT.WeaponControl.length;
			int i_62_ = 0;
			for (int i_63_ = 1; i_62_ < i_61_ && i_63_ < 256; i_63_ <<= 1)
			{
				FM.CT.WeaponControl[i_62_] = (i & i_63_) != 0;
				i_62_++;
			}
		}
		
		public Mirror(Actor actor, NetChannel netchannel, int i)
		{
			super(actor, netchannel, i);
			try
			{
				out.setIncludeTime(true);
				out.setFilterArg(actor);
			}
			catch (Exception exception)
			{
				/* empty */
			}
		}
	}
	
	static class ClipFilter implements ActorFilter
	{
		public boolean isUse(Actor actor, double d)
		{
			return actor instanceof BigshipGeneric;
		}
	}
	
	class Master extends AircraftNet implements NetUpdate
	{
		NetMsgFiltered out = new NetMsgFiltered();
		
		public byte[] weaponsBitStates;
		
		public boolean weaponsIsEmpty = false;
		
		public boolean weaponsCheck = false;
		
		public long weaponsSyncTime = 0L;
		
		public int curWayPoint = 0;
		
		private Vector3f vec3f = new Vector3f();
		
		private Point3d p = new Point3d();
		
		private Orient o = new Orient();
		
		private int countUpdates = 0;
		
		public boolean netInput(NetMsgInput netmsginput) throws IOException
		{
			if (netmsginput.isGuaranted())
				return netGetGMsg(netmsginput, true);
			if (!Config.isUSE_RENDER())
				return true;
			int i = netmsginput.readByte();
			int i_64_ = netmsginput.readByte();
			int i_65_ = (i & 0x1) << 1 | i_64_ & 0x1;
			i &= ~0x1;
			i_64_ &= ~0x1;
			msgSndShot((float)i_65_ * 0.05F + 0.01F, (double)i * 0.25, (double)i_64_ * 0.25, 0.0);
			return true;
		}
		
		public void netUpdate()
		{
			if (Time.tickCounter() > 2)
			{
				if (netUser == null && FM.brakeShoe)
				{
					int i = FM.actor.hashCode() & 0xf;
					if ((countUpdates++ & 0xf) != i)
						return;
				}
				else
					countUpdates = 0;
				if (weaponsIsEmpty)
					FM.CT.WCT = (byte)0;
				boolean bool = (FM.CT.WCT & 0xf) != 0;
				try
				{
					out.unLockAndClear();
					int i = 0;
					boolean bool_66_ = false;
					boolean bool_67_ = false;
					int i_68_ = 0;
					for (int i_69_ = 0; i_69_ < FM.EI.getNum(); i_69_++)
					{
						int i_70_ = (int)(FM.EI.engines[i_69_].getw() / 640.0F * 255.0F);
						if (i_69_ == 0)
							i_68_ = i_70_;
						else if (i_68_ != i_70_)
							bool_67_ = true;
						if (i_70_ != FM.EI.engines[i_69_].wNetPrev)
						{
							bool_66_ = true;
							FM.EI.engines[i_69_].wNetPrev = i_70_;
						}
					}
					if (bool_66_)
					{
						if (bool_67_)
							i = 1;
						else
						{
							i = FM.EI.getNum();
							if (i > 15)
								i = 15;
						}
					}
					if (netCockpitValid && netCockpitTuretNum >= 0)
						i |= 0x10;
					if (FM.Gears.onGround())
						i |= 0x20;
					if (FM.Gears.isUnderDeck() && FM.Vrel.lengthSquared() < 2.0)
					{
						NetAircraft.corn.set(FM.Loc);
						NetAircraft.corn1.set(FM.Loc);
						NetAircraft.corn1.z -= 20.0;
						Actor actor = Engine.collideEnv().getLine(NetAircraft.corn, NetAircraft.corn1, false, (NetAircraft.clipFilter), NetAircraft.pship);
						if (!(actor instanceof BigshipGeneric) && Mission.isCoop() && Time.current() < 60000L)
							actor = FM.brakeShoeLastCarrier;
						if (actor instanceof BigshipGeneric)
						{
							NetAircraft.lCorn.set(pos.getAbs());
							NetAircraft.lCorn.sub(actor.pos.getAbs());
							if (Math.abs(NetAircraft.lCorn.getX()) < 200.0 && Math.abs(NetAircraft.lCorn.getY()) < 200.0 && Math.abs(NetAircraft.lCorn.getZ()) < 200.0)
								i |= 0x40;
						}
					}
					out.writeByte(i);
					int i_71_ = (int)(Time.tickNext() - Time.current());
					if (i_71_ > 255)
						i_71_ = 255;
					out.writeByte(i_71_);
					out.writeByte(FM.CT.CTL);
					out.writeByte(FM.CT.WCT);
					FM.CT.WCT &= 0x3;
					pos.getAbs(p, o);
					out.writeFloat((float)p.x);
					out.writeFloat((float)p.y);
					out.writeFloat((float)p.z);
					o.wrap();
					int i_72_ = (int)(o.getYaw() * 32000.0F / 180.0F);
					i_68_ = (int)(o.tangage() * 32000.0F / 90.0F);
					int i_73_ = (int)(o.kren() * 32000.0F / 180.0F);
					out.writeShort(i_72_);
					out.writeShort(i_68_);
					out.writeShort(i_73_);
					if ((i & 0x40) == 0)
					{
						vec3f.set(FM.getW());
						int i_74_ = (int)(vec3f.x * 32000.0F / 50.0F);
						int i_75_ = (int)(vec3f.y * 32000.0F / 50.0F);
						int i_76_ = (int)(vec3f.z * 32000.0F / 50.0F);
						out.writeShort(i_74_);
						out.writeShort(i_75_);
						out.writeShort(i_76_);
					}
					else
					{
						NetAircraft.lCorn.get(o);
						o.wrap();
						i_72_ = (int)(o.getYaw() * 32000.0F / 180.0F);
						i_68_ = (int)(o.tangage() * 32000.0F / 90.0F);
						i_73_ = (int)(o.kren() * 32000.0F / 180.0F);
						out.writeShort(i_72_);
						out.writeShort(i_68_);
						out.writeShort(i_73_);
					}
					vec3f.set(FM.Vwld);
					int i_77_ = (int)(vec3f.x * 32000.0F / 400.0F);
					int i_78_ = (int)(vec3f.y * 32000.0F / 400.0F);
					int i_79_ = (int)(vec3f.z * 32000.0F / 400.0F);
					out.writeShort(i_77_);
					out.writeShort(i_78_);
					out.writeShort(i_79_);
					if ((i & 0x40) == 0)
					{
						vec3f.set(FM.getAccel());
						int i_80_ = (int)(vec3f.x * 32000.0F / 2000.0F);
						int i_81_ = (int)(vec3f.y * 32000.0F / 2000.0F);
						int i_82_ = (int)(vec3f.z * 32000.0F / 2000.0F);
						out.writeShort(i_80_);
						out.writeShort(i_81_);
						out.writeShort(i_82_);
					}
					else
					{
						int i_83_ = (int)(NetAircraft.lCorn.getX() * 32000.0 / 200.0);
						int i_84_ = (int)(NetAircraft.lCorn.getY() * 32000.0 / 200.0);
						int i_85_ = (int)(NetAircraft.lCorn.getZ() * 32000.0 / 200.0);
						out.writeShort(i_83_);
						out.writeShort(i_84_);
						out.writeShort(i_85_);
					}
					for (int i_86_ = 0; i_86_ < (i & 0xf); i_86_++)
					{
						out.writeByte((byte)(int)(FM.EI.engines[i_86_].getw() / 640.0F * 255.0F));
						out.writeByte((byte)(int)(FM.EI.engines[i_86_].getPropPhi() / 1.6F * 255.0F));
					}
					if (netCockpitValid && netCockpitTuretNum >= 0)
					{
						Turret turret = FM.turret[netCockpitTuretNum];
						boolean bool_87_ = FM.CT.WeaponControl[netCockpitWeaponControlNum];
						out.writeShort(packSY(turret.tu[0]));
						out.writeShort(packSP(turret.tu[1]) | (bool_87_ ? 32768 : 0));
					}
					post(Time.current(), out);
				}
				catch (Exception exception)
				{
					NetObj.printDebug(exception);
				}
				if (weaponsCheck && Time.current() > weaponsSyncTime)
				{
					weaponsSyncTime = Time.current() + 5000L;
					weaponsCheck = false;
					if (NetAircraft.this.isWeaponsChanged(weaponsBitStates))
					{
						weaponsBitStates = NetAircraft.this.getWeaponsBitStates(weaponsBitStates);
						NetAircraft.this.netPutWeaponsBitStates(weaponsBitStates);
						weaponsIsEmpty = NetAircraft.this.isWeaponsAllEmpty();
					}
				}
				if (bool)
					weaponsCheck = true;
			}
		}
		
		int packSY(float f)
		{
			return (0xffff & (int)(((double)f % 360.0 + 180.0) * 65000.0 / 360.0));
		}
		
		int packSP(float f)
		{
			return (0x7fff & (int)(((double)f % 360.0 + 180.0) * 32000.0 / 360.0));
		}
		
		public Master(Actor actor)
		{
			super(actor);
			try
			{
				out.setIncludeTime(true);
				out.setFilterArg(actor);
			}
			catch (Exception exception)
			{
				/* empty */
			}
		}
	}
	
	public class AircraftNet extends ActorNet
	{
		public NetUser netUser;
		
		public String netName;
		
		public IntHashtable filterTable;
		
		private void createFilterTable()
		{
			if (Main.cur().netServerParams != null && !Main.cur().netServerParams.isMirror())
				filterTable = new IntHashtable();
		}
		
		public AircraftNet(Actor actor)
		{
			super(actor);
			createFilterTable();
		}
		
		public AircraftNet(Actor actor, NetChannel netchannel, int i)
		{
			super(actor, netchannel, i);
			createFilterTable();
		}
	}
	
	private boolean isCoopPlane()
	{
		return bCoopPlane;
	}
	
	protected static String[] partNames()
	{
		return Aircraft.partNames();
	}
	
	protected int curDMGLevel(int i)
	{
		return 0;
	}
	
	protected void nextCUTLevel(String string, int i, Actor actor)
	{
	/* empty */
	}
	
	protected void nextDMGLevel(String string, int i, Actor actor)
	{
	/* empty */
	}
	
	protected void netHits(int i, int i_88_, int i_89_, Actor actor)
	{
	/* empty */
	}
	
	protected void doExplosion()
	{
	/* empty */
	}
	
	public int curDMGProp(int i)
	{
		return 0;
	}
	
	protected void weaponsLoad(String string) throws Exception
	{
	/* empty */
	}
	
	protected void createCockpits()
	{
	/* empty */
	}
	
	public void setFM(int i, boolean bool)
	{
	/* empty */
	}
	
	public void preparePaintScheme()
	{
	/* empty */
	}
	
	public void prepareCamouflage()
	{
	/* empty */
	}
	
	public int aircIndex()
	{
		return -1;
	}
	
	public Wing getWing()
	{
		return null;
	}
	
	public void onAircraftLoaded()
	{
	/* empty */
	}
	
	public NetUser netUser()
	{
		if (!isNet())
			return null;
		return ((AircraftNet)net).netUser;
	}
	
	public String netName()
	{
		if (!isNet())
			return null;
		return ((AircraftNet)net).netName;
	}
	
	public boolean isNetPlayer()
	{
		if (!isNet())
			return false;
		return ((AircraftNet)net).netUser != null;
	}
	
	public void moveSteering(float f)
	{
	/* empty */
	}
	
	public void moveWheelSink()
	{
	/* empty */
	}
	
	public void setFMTrack(FlightModelTrack flightmodeltrack)
	{
		fmTrack = flightmodeltrack;
	}
	
	public FlightModelTrack fmTrack()
	{
		return fmTrack;
	}
	
	public boolean isFMTrackMirror()
	{
		return fmTrack != null && fmTrack.isMirror();
	}
	
	public boolean netNewAState_isEnable(boolean bool)
	{
		if (!isNet())
			return false;
		if (bool && net.isMaster())
			return false;
		if (!bool && !net.isMirrored())
			return false;
		if (bool && net.masterChannel() instanceof NetChannelInStream)
			return false;
		return true;
	}
	
	public NetMsgGuaranted netNewAStateMsg(boolean bool) throws IOException
	{
		if (!isNet())
			return null;
		if (bool && net.isMaster())
			return null;
		if (!bool && !net.isMirrored())
			return null;
		if (bool && net.masterChannel() instanceof NetChannelInStream)
			return null;
		NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
		if (bool)
			netmsgguaranted.writeByte(128);
		else
			netmsgguaranted.writeByte(0);
		return netmsgguaranted;
	}
	
	public void netSendAStateMsg(boolean bool, NetMsgGuaranted netmsgguaranted) throws IOException
	{
		if (bool)
			net.postTo(net.masterChannel(), netmsgguaranted);
		else
			net.post(netmsgguaranted);
	}
	
	public void netUpdateWayPoint()
	{
		if (net != null && Main.cur().netServerParams != null && Main.cur().netServerParams.isCoop() && !Main.cur().netServerParams.isMaster() && net.isMaster() && net.isMirrored())
		{
			Master master = (Master)net;
			if (master.curWayPoint != FM.AP.way.Cur())
			{
				master.curWayPoint = FM.AP.way.Cur();
				try
				{
					NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
					netmsgguaranted.writeByte(1);
					netmsgguaranted.writeShort(master.curWayPoint);
					master.postTo(Main.cur().netServerParams.masterChannel(), netmsgguaranted);
				}
				catch (Exception exception)
				{
					printDebug(exception);
				}
			}
		}
	}
	
	private boolean netGetUpdateWayPoint(NetMsgInput netmsginput, boolean bool, boolean bool_90_) throws IOException
	{
		if (bool)
			return false;
		int i = netmsginput.readUnsignedShort();
		if (Main.cur().netServerParams.isMaster())
		{
			FM.AP.way.setCur(i);
			if (i == FM.AP.way.size() - 1)
				FM.AP.way.next();
		}
		else
		{
			try
			{
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(1);
				netmsgguaranted.writeShort(i);
				net.postTo(Main.cur().netServerParams.masterChannel(), netmsgguaranted);
			}
			catch (Exception exception)
			{
				printDebug(exception);
			}
		}
		return true;
	}
	
	private int getWeaponsAmount()
	{
		int i = FM.CT.Weapons.length;
		if (FM.CT.Weapons.length == 0)
			return 0;
		int i_91_ = 0;
		for (int i_92_ = 0; i_92_ < i; i_92_++)
		{
			BulletEmitter[] bulletemitters = FM.CT.Weapons[i_92_];
			if (bulletemitters != null)
			{
				for (int i_93_ = 0; i_93_ < bulletemitters.length; i_93_++)
				{
					if (bulletemitters[i_93_] != null)
						i_91_++;
				}
			}
		}
		return i_91_;
	}
	
	private boolean isWeaponsAllEmpty()
	{
		int i = FM.CT.Weapons.length;
		if (FM.CT.Weapons.length == 0)
			return true;
		for (int i_94_ = 0; i_94_ < i; i_94_++)
		{
			BulletEmitter[] bulletemitters = FM.CT.Weapons[i_94_];
			if (bulletemitters != null)
			{
				for (int i_95_ = 0; i_95_ < bulletemitters.length; i_95_++)
				{
					if (bulletemitters[i_95_] != null && bulletemitters[i_95_].countBullets() != 0)
						return false;
				}
			}
		}
		return true;
	}
	
	private byte[] getWeaponsBitStatesBuf(byte[] is)
	{
		int i = getWeaponsAmount();
		if (i == 0)
			return null;
		int i_96_ = (i + 7) / 8;
		if (is == null || is.length != i_96_)
			is = new byte[i_96_];
		for (int i_97_ = 0; i_97_ < i_96_; i_97_++)
			is[i_97_] = (byte)0;
		return is;
	}
	
	private byte[] getWeaponsBitStates(byte[] is)
	{
		is = getWeaponsBitStatesBuf(is);
		if (is == null)
			return null;
		int i = 0;
		int i_98_ = FM.CT.Weapons.length;
		for (int i_99_ = 0; i_99_ < i_98_; i_99_++)
		{
			BulletEmitter[] bulletemitters = FM.CT.Weapons[i_99_];
			if (bulletemitters != null)
			{
				for (int i_100_ = 0; i_100_ < bulletemitters.length; i_100_++)
				{
					if (bulletemitters[i_100_] != null)
					{
						if (bulletemitters[i_100_].countBullets() != 0)
							is[i / 8] |= 1 << i % 8;
						i++;
					}
				}
			}
		}
		return is;
	}
	
	private void setWeaponsBitStates(byte[] is)
	{
		int i = 0;
		int i_101_ = FM.CT.Weapons.length;
		for (int i_102_ = 0; i_102_ < i_101_; i_102_++)
		{
			BulletEmitter[] bulletemitters = FM.CT.Weapons[i_102_];
			if (bulletemitters != null)
			{
				for (int i_103_ = 0; i_103_ < bulletemitters.length; i_103_++)
				{
					if (bulletemitters[i_103_] != null)
					{
						if ((is[i / 8] & 1 << i % 8) == 0)
							bulletemitters[i_103_]._loadBullets(0);
						i++;
					}
				}
			}
		}
	}
	
	private boolean isWeaponsChanged(byte[] is)
	{
		if (getWeaponsAmount() == 0)
			return false;
		if (is == null)
			return true;
		int i = 0;
		int i_104_ = FM.CT.Weapons.length;
		for (int i_105_ = 0; i_105_ < i_104_; i_105_++)
		{
			BulletEmitter[] bulletemitters = FM.CT.Weapons[i_105_];
			if (bulletemitters != null)
			{
				for (int i_106_ = 0; i_106_ < bulletemitters.length; i_106_++)
				{
					if (bulletemitters[i_106_] != null)
					{
						if (((is[i / 8] & 1 << i % 8) == 0) != (bulletemitters[i_106_].countBullets() == 0))
							return true;
						i++;
					}
				}
			}
		}
		return false;
	}
	
	private void netPutWeaponsBitStates(byte[] is)
	{
		if (isNet() && net.countMirrors() != 0)
		{
			try
			{
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(2);
				netmsgguaranted.write(is);
				net.post(netmsgguaranted);
			}
			catch (Exception exception)
			{
				printDebug(exception);
			}
		}
	}
	
	private boolean netGetWeaponsBitStates(NetMsgInput netmsginput, boolean bool, boolean bool_107_) throws IOException
	{
		if (bool || bool_107_)
			return false;
		byte[] is = getWeaponsBitStatesBuf(null);
		for (int i = 0; i < is.length; i++)
			is[i] = (byte)netmsginput.readUnsignedByte();
		setWeaponsBitStates(is);
		netPutWeaponsBitStates(is);
		return true;
	}
	
	public boolean isGunPodsExist()
	{
		return bGunPodsExist;
	}
	
	public boolean isGunPodsOn()
	{
		return bGunPodsOn;
	}
	
	public void setGunPodsOn(boolean bool)
	{
		if (bGunPodsOn != bool)
		{
			for (int i = 0; i < FM.CT.Weapons.length; i++)
			{
				BulletEmitter[] bulletemitters = FM.CT.Weapons[i];
				if (bulletemitters != null)
				{
					for (int i_108_ = 0; i_108_ < bulletemitters.length; i_108_++)
					{
						if (bulletemitters[i_108_] != null)
							bulletemitters[i_108_].setPause(!bool);
					}
				}
			}
			bGunPodsOn = bool;
			if (isNet() && net.countMirrors() != 0)
			{
				try
				{
					NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
					if (bool)
						netmsgguaranted.writeByte(3);
					else
						netmsgguaranted.writeByte(4);
					net.post(netmsgguaranted);
				}
				catch (Exception exception)
				{
					printDebug(exception);
				}
			}
		}
	}
	
	public void replicateDropFuelTanks()
	{
		if (isNet() && net.countMirrors() != 0)
		{
			try
			{
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(5);
				net.post(netmsgguaranted);
			}
			catch (Exception exception)
			{
				printDebug(exception);
			}
		}
	}
	
	protected void netPutHits(boolean bool, NetChannel netchannel, int i, int i_109_, int i_110_, Actor actor)
	{
		if ((bool || net.countMirrors() != 0) && (Actor.isValid(actor) && actor.isNet()))
		{
			try
			{
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				if (bool)
					netmsgguaranted.writeByte(134);
				else
					netmsgguaranted.writeByte(6);
				netmsgguaranted.writeByte(i & 0xf | i_109_ << 4 & 0xf0);
				netmsgguaranted.writeByte(i_110_);
				netmsgguaranted.writeNetObj(actor.net);
				if (bool)
					net.postTo(net.masterChannel(), netmsgguaranted);
				else
					net.postExclude(netchannel, netmsgguaranted);
			}
			catch (Exception exception)
			{
				printDebug(exception);
			}
		}
	}
	
	private boolean netGetHits(NetMsgInput netmsginput, boolean bool, boolean bool_111_) throws IOException
	{
		if (bool && !bool_111_)
			return false;
		int i = netmsginput.readUnsignedByte();
		int i_112_ = i >> 4 & 0xf;
		i &= 0xf;
		int i_113_ = netmsginput.readUnsignedByte();
		if (i_113_ >= 44)
			return false;
		NetObj netobj = netmsginput.readNetObj();
		if (netobj == null)
			return true;
		Actor actor = (Actor)netobj.superObj();
		if (!Actor.isValid(actor))
			return true;
		if (!bool && bool_111_)
			netPutHits(true, null, i, i_112_, i_113_, actor);
		if (net.countMirrors() > 1)
			netPutHits(false, netmsginput.channel(), i, i_112_, i_113_, actor);
		netHits(i, i_112_, i_113_, actor);
		return true;
	}
	
	public void hitProp(int i, int i_114_, Actor actor)
	{
		if (isNet() && net.isMirrored())
		{
			try
			{
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(7);
				netmsgguaranted.writeByte(i);
				netmsgguaranted.writeByte(i_114_);
				netmsgguaranted.writeNetObj(actor != null ? actor.net : null);
				net.post(netmsgguaranted);
			}
			catch (Exception exception)
			{
				printDebug(exception);
			}
		}
	}
	
	private boolean netGetHitProp(NetMsgInput netmsginput, boolean bool, boolean bool_115_) throws IOException
	{
		if (bool || bool_115_)
			return false;
		int i = netmsginput.readUnsignedByte();
		int i_116_ = netmsginput.readUnsignedByte();
		NetObj netobj = netmsginput.readNetObj();
		hitProp(i, i_116_, netobj != null ? (Actor)netobj.superObj() : null);
		return true;
	}
	
	protected void netPutCut(int i, int i_117_, Actor actor)
	{
		if (isNet() && net.countMirrors() != 0)
		{
			try
			{
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(8);
				netmsgguaranted.writeByte(i);
				netmsgguaranted.writeByte(i_117_);
				netmsgguaranted.writeNetObj(actor != null ? actor.net : null);
				net.post(netmsgguaranted);
			}
			catch (Exception exception)
			{
				printDebug(exception);
			}
		}
	}
	
	private boolean netGetCut(NetMsgInput netmsginput, boolean bool, boolean bool_118_) throws IOException
	{
		if (bool || bool_118_)
			return false;
		int i = netmsginput.readUnsignedByte();
		if (i >= 44)
			return false;
		int i_119_ = netmsginput.readUnsignedByte();
		NetObj netobj = netmsginput.readNetObj();
		nextCUTLevel(partNames()[i] + "_D0", i_119_, netobj != null ? (Actor)netobj.superObj() : null);
		return true;
	}
	
	public void netExplode()
	{
		if (isNet() && net.countMirrors() != 0)
		{
			try
			{
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(9);
				net.post(netmsgguaranted);
			}
			catch (Exception exception)
			{
				printDebug(exception);
			}
		}
	}
	
	private boolean netGetExplode(NetMsgInput netmsginput, boolean bool, boolean bool_120_) throws IOException
	{
		if (bool || bool_120_)
			return false;
		netExplode();
		doExplosion();
		return true;
	}
	
	public void setDiedFlag(boolean bool)
	{
		if (isAlive() && bool && isNet() && Actor.isValid(getDamager()) && getDamager().isNet() && net.countMirrors() > 0)
		{
			try
			{
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(10);
				netmsgguaranted.writeNetObj(getDamager().net);
				net.post(netmsgguaranted);
			}
			catch (Exception exception)
			{
				printDebug(exception);
			}
		}
		super.setDiedFlag(bool);
	}
	
	private boolean netGetDead(NetMsgInput netmsginput, boolean bool, boolean bool_121_) throws IOException
	{
		NetObj netobj = netmsginput.readNetObj();
		if (netobj != null)
		{
			if (isAlive())
				World.onActorDied(this, (Actor)netobj.superObj());
			if (net.countMirrors() > 0)
			{
				try
				{
					NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
					netmsgguaranted.writeByte(10);
					netmsgguaranted.writeNetObj(netobj);
					net.post(netmsgguaranted);
				}
				catch (Exception exception)
				{
					printDebug(exception);
				}
			}
		}
		return true;
	}
	
	public void netFirstUpdate(NetChannel netchannel) throws IOException
	{
		// TODO: Changed by |ZUTI|: fixes propellers not spinning for DF at start, folded wings etc.
		// if (!Mission.isDogfight() && (!Mission.isCoop() || !isNetPlayer()) && netchannel instanceof NetChannelOutStream)
		if ((!Mission.isCoop() || !isNetPlayer()) && ((netchannel instanceof NetChannelOutStream) || Mission.isDogfight()))
		{
			NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
			netmsgguaranted.writeByte(11);
			netReplicateFirstUpdate(netchannel, netmsgguaranted);
			net.postTo(netchannel, netmsgguaranted);
			if (Mission.isSingle() && World.getPlayerAircraft() == this)
			{
				if (fmTrack() == null)
				{
					if (isNetMaster())
						new MsgAction(true, this)
						{
							public void doAction(Object object)
							{
								new FlightModelTrack((Aircraft)object);
							}
						};
				}
				else
					MsgNet.postRealNewChannel(fmTrack(), netchannel);
			}
		}
		netCockpitFirstUpdate(this, netchannel);
	}
	
	private boolean netGetFirstUpdate(NetMsgInput netmsginput) throws IOException
	{
		ActorSpawnArg actorspawnarg = new ActorSpawnArg();
		try
		{
			netSpawnCommon(netmsginput, actorspawnarg);
			pos.setAbs(actorspawnarg.point, actorspawnarg.orient);
			pos.reset();
			setSpeed(actorspawnarg.speed);
			netSpawnCommon(netmsginput, actorspawnarg, this);
		}
		catch (Exception exception)
		{
			printDebug(exception);
		}
		return true;
	}
	
	public int netCockpitAstatePilotIndx(int i)
	{
		return netCockpitAstatePilotIndx(this.getClass(), i);
	}
	
	public static int netCockpitAstatePilotIndx(Class var_class, int i)
	{
		if (i < 0)
			return -1;
		Object object = Property.value(var_class, "cockpitClass");
		if (object == null)
			return -1;
		if (object instanceof Class)
		{
			if (i > 0)
				return -1;
			return Property.intValue((Class)object, "astatePilotIndx", 0);
		}
		Class[] var_classes = (Class[])object;
		if (i >= var_classes.length)
			return -1;
		return Property.intValue(var_classes[i], "astatePilotIndx", 0);
	}
	
	public void netCockpitAuto(Actor actor, int i, boolean bool)
	{
		short[] is = null;
		Object object = Property.value(this.getClass(), "cockpitClass");
		if (object != null)
		{
			int i_124_;
			if (object instanceof Class)
			{
				if (i > 0)
					return;
				i_124_ = Property.intValue((Class)object, "weaponControlNum", 10);
			}
			else
			{
				Class[] var_classes = (Class[])object;
				if (i >= var_classes.length)
					return;
				i_124_ = Property.intValue(var_classes[i], "weaponControlNum", 10);
			}
			if (World.cur().diffCur.Limited_Ammo)
			{
				BulletEmitter[] bulletemitters = FM.CT.Weapons[i_124_];
				if (bulletemitters != null)
				{
					is = new short[bulletemitters.length];
					for (int i_125_ = 0; i_125_ < bulletemitters.length; i_125_++)
					{
						int i_126_ = bulletemitters[i_125_].countBullets();
						if (i_126_ < 0)
							is[i_125_] = (short)-1;
						else
							is[i_125_] = (short)i_126_;
					}
				}
			}
			netCockpitAuto(actor, i, bool, is, null);
		}
	}
	
	private void netCockpitAuto(Actor actor, int i, boolean bool, short[] is, NetChannel netchannel)
	{
		Object object = Property.value(this.getClass(), "cockpitClass");
		if (object != null)
		{
			Class var_class;
			if (object instanceof Class)
			{
				if (i > 0)
					return;
				var_class = (Class)object;
			}
			else
			{
				Class[] var_classes = (Class[])object;
				if (i >= var_classes.length)
					return;
				var_class = var_classes[i];
			}
			if (!(class$com$maddox$il2$objects$air$CockpitPilot == null ? (class$com$maddox$il2$objects$air$CockpitPilot = class$ZutiNetAircraft("com.maddox.il2.objects.air.CockpitPilot")) : class$com$maddox$il2$objects$air$CockpitPilot)
					.isAssignableFrom(var_class))
			{
				int i_128_ = Property.intValue(var_class, "weaponControlNum", 10);
				int i_129_ = Property.intValue(var_class, "aiTuretNum", 0);
				if (this == World.getPlayerAircraft())
				{
					CockpitGunner cockpitgunner = (CockpitGunner)Main3D.cur3D().cockpits[i];
					cockpitgunner.setRealMode(!bool);
				}
				else
				{
					Turret turret = FM.turret[i_129_];
					turret.bIsAIControlled = bool;
				}
				BulletEmitter[] bulletemitters = FM.CT.Weapons[i_128_];
				if (bulletemitters != null)
				{
					boolean bool_130_ = (!actor.net.isMaster() || World.cur().diffCur.Realistic_Gunnery);
					if (bool)
						bool_130_ = true;
					for (int i_131_ = 0; i_131_ < bulletemitters.length; i_131_++)
					{
						if (bulletemitters[i_131_] instanceof Actor)
						{
							((Actor)bulletemitters[i_131_]).setOwner(bool ? (Actor)this : actor);
							if (bulletemitters[i_131_] instanceof Gun)
								((Gun)bulletemitters[i_131_]).initRealisticGunnery(bool_130_);
						}
						if (is != null)
						{
							short i_132_ = is[i_131_];
							if (i_132_ == 65535)
								i_132_ = (short)-1;
							bulletemitters[i_131_]._loadBullets(i_132_);
						}
						else if (!World.cur().diffCur.Limited_Ammo)
							bulletemitters[i_131_].loadBullets(-1);
					}
				}
				if (actor instanceof NetGunner)
				{
					((NetGunner)actor).netCockpitTuretNum = bool ? -1 : i_129_;
					((NetGunner)actor).netCockpitWeaponControlNum = i_128_;
				}
				else
				{
					netCockpitTuretNum = bool ? -1 : i_129_;
					netCockpitWeaponControlNum = i_128_;
				}
			}
			else if (actor instanceof NetGunner)
				((NetGunner)actor).netCockpitTuretNum = -1;
			else
				netCockpitTuretNum = -1;
			int i_133_ = net.countMirrors();
			if (net.isMirror())
				i_133_++;
			if (netchannel != null)
				i_133_--;
			if (i_133_ > 0)
			{
				if (actor instanceof NetGunner)
					((NetGunner)actor).netCockpitValid = false;
				else
					netCockpitValid = false;
				try
				{
					NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted()
					{
						public void unLocking()
						{
							try
							{
								Actor actor_135_ = ((Actor)((NetObj)objects().get(0)).superObj());
								if (actor_135_ instanceof NetGunner)
								{
									if (((NetGunner)actor_135_).netCockpitMsg == this)
										((NetGunner)actor_135_).netCockpitValid = true;
								}
								else if (netCockpitMsg == this)
									netCockpitValid = true;
							}
							catch (Exception exception)
							{
								printDebug(exception);
							}
						}
					};
					if (actor instanceof NetGunner)
						((NetGunner)actor).netCockpitMsg = netmsgguaranted;
					else
						netCockpitMsg = netmsgguaranted;
					netmsgguaranted.writeByte(13);
					netmsgguaranted.writeNetObj(actor.net);
					if (bool)
						i |= 0x80;
					netmsgguaranted.writeByte(i);
					if (is != null)
					{
						for (int i_136_ = 0; i_136_ < is.length; i_136_++)
							netmsgguaranted.writeShort(is[i_136_]);
					}
					net.postExclude(netchannel, netmsgguaranted);
				}
				catch (Exception exception)
				{
					printDebug(exception);
				}
			}
			else if (actor instanceof NetGunner)
				((NetGunner)actor).netCockpitValid = true;
			else
				netCockpitValid = true;
		}
	}
	
	private boolean netGetCockpitAuto(NetMsgInput netmsginput, boolean bool, boolean bool_137_) throws IOException
	{
		NetObj netobj = netmsginput.readNetObj();
		if (netobj == null)
			return false;
		Actor actor = (Actor)netobj.superObj();
		int i = netmsginput.readUnsignedByte();
		boolean bool_138_ = (i & 0x80) != 0;
		i &= ~0x80;
		short[] is = null;
		int i_139_ = netmsginput.available() / 2;
		if (i_139_ > 0)
		{
			is = new short[i_139_];
			for (int i_140_ = 0; i_140_ < is.length; i_140_++)
				is[i_140_] = (short)netmsginput.readUnsignedShort();
		}
		netCockpitAuto(actor, i, bool_138_, is, netmsginput.channel());
		return true;
	}
	
	public void netCockpitEnter(Actor actor, int i)
	{
		netCockpitEnter(actor, i, true);
	}
	
	public void netCockpitEnter(Actor actor, int i, boolean bool)
	{
		if (bool)
		{
			if (actor instanceof NetGunner)
				EventLog.onOccupied((Aircraft)this, ((NetGunner)actor).getUser(), netCockpitAstatePilotIndx(i));
			else
			{
				EventLog.onOccupied((Aircraft)this, ((Aircraft)actor).netUser(), netCockpitAstatePilotIndx(i));
				if (actor == World.getPlayerAircraft() && actor.isNetMaster() && i == 0 && !bWeaponsEventLog)
				{
					bWeaponsEventLog = true;
					EventLog.onWeaponsLoad(actor, thisWeaponsName, (int)(FM.M.fuel * 100.0F / FM.M.maxFuel));
				}
			}
		}
		netCockpitEnter(actor, i, (NetChannel)null);
	}
	
	private void netCockpitEnter(Actor actor, int i, NetChannel netchannel)
	{
		int i_141_ = netCockpitIndxPilot;
		if (actor instanceof NetGunner)
			i_141_ = ((NetGunner)actor).netCockpitIndxPilot;
		Object object_143_ = Property.value(this.getClass(), "cockpitClass");
		if (object_143_ != null)
		{
			Class var_class;
			Class var_class_144_;
			if (object_143_ instanceof Class)
			{
				if (i_141_ > 0 || i > 0)
					return;
				var_class = var_class_144_ = (Class)object_143_;
			}
			else
			{
				Class[] var_classes = (Class[])object_143_;
				if (i_141_ >= var_classes.length || i >= var_classes.length)
					return;
				var_class = var_classes[i_141_];
				var_class_144_ = var_classes[i];
			}
			if (!(class$com$maddox$il2$objects$air$CockpitPilot == null ? (class$com$maddox$il2$objects$air$CockpitPilot = class$ZutiNetAircraft("com.maddox.il2.objects.air.CockpitPilot")) : class$com$maddox$il2$objects$air$CockpitPilot)
					.isAssignableFrom(var_class))
			{
				int i_145_ = Property.intValue(var_class, "aiTuretNum", 0);
				Turret turret = FM.turret[i_145_];
				turret.bIsNetMirror = false;
			}
			if (!(class$com$maddox$il2$objects$air$CockpitPilot == null ? (class$com$maddox$il2$objects$air$CockpitPilot = class$ZutiNetAircraft("com.maddox.il2.objects.air.CockpitPilot")) : class$com$maddox$il2$objects$air$CockpitPilot)
					.isAssignableFrom(var_class_144_))
			{
				int i_146_ = Property.intValue(var_class_144_, "aiTuretNum", 0);
				Turret turret = FM.turret[i_146_];
				turret.bIsNetMirror = actor.net.isMirror();
			}
			if (actor instanceof NetGunner)
				((NetGunner)actor).netCockpitIndxPilot = i;
			else
				netCockpitIndxPilot = i;
			netCockpitDriverSet(actor, i);
			int i_147_ = 0;
			int i_148_ = -1;
			if (!(class$com$maddox$il2$objects$air$CockpitPilot == null ? (class$com$maddox$il2$objects$air$CockpitPilot = class$ZutiNetAircraft("com.maddox.il2.objects.air.CockpitPilot")) : class$com$maddox$il2$objects$air$CockpitPilot)
					.isAssignableFrom(var_class_144_))
			{
				i_148_ = Property.intValue(var_class_144_, "aiTuretNum", 0);
				Turret turret = FM.turret[i_148_];
				if (turret.bIsAIControlled)
					i_148_ = -1;
				else
					i_147_ = Property.intValue(var_class_144_, "weaponControlNum", 10);
			}
			if (actor instanceof NetGunner)
			{
				((NetGunner)actor).netCockpitTuretNum = i_148_;
				((NetGunner)actor).netCockpitWeaponControlNum = i_147_;
			}
			else
			{
				netCockpitTuretNum = i_148_;
				netCockpitWeaponControlNum = i_147_;
			}
			int i_149_ = net.countMirrors();
			if (net.isMirror())
				i_149_++;
			if (netchannel != null)
				i_149_--;
			if (i_149_ > 0)
			{
				try
				{
					NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
					netmsgguaranted.writeByte(12);
					netmsgguaranted.writeNetObj(actor.net);
					netmsgguaranted.writeByte(i);
					net.postExclude(netchannel, netmsgguaranted);
				}
				catch (Exception exception)
				{
					printDebug(exception);
				}
			}
		}
	}
	
	private boolean netGetCockpitEnter(NetMsgInput netmsginput, boolean bool, boolean bool_150_) throws IOException
	{
		NetObj netobj = netmsginput.readNetObj();
		if (netobj == null)
			return false;
		Actor actor = (Actor)netobj.superObj();
		int i = netmsginput.readUnsignedByte();
		netCockpitEnter(actor, i, netmsginput.channel());
		return true;
	}
	
	protected void netCockpitFirstUpdate(Actor actor, NetChannel netchannel) throws IOException
	{
		int i = netCockpitIndxPilot;
		int i_151_ = netCockpitTuretNum;
		if (actor instanceof NetGunner)
		{
			i = ((NetGunner)actor).netCockpitIndxPilot;
			i_151_ = ((NetGunner)actor).netCockpitTuretNum;
		}
		if (i != 0)
		{
			NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
			netmsgguaranted.writeByte(12);
			netmsgguaranted.writeNetObj(actor.net);
			netmsgguaranted.writeByte(i);
			net.postTo(netchannel, netmsgguaranted);
		}
		if (i_151_ >= 0)
		{
			short[] is = null;
			Object object = Property.value(this.getClass(), "cockpitClass");
			if (object != null)
			{
				Class var_class;
				if (object instanceof Class)
				{
					if (i > 0)
						return;
					var_class = (Class)object;
				}
				else
				{
					Class[] var_classes = (Class[])object;
					if (i >= var_classes.length)
						return;
					var_class = var_classes[i];
				}
				int i_153_ = Property.intValue(var_class, "weaponControlNum", 10);
				if (World.cur().diffCur.Limited_Ammo)
				{
					BulletEmitter[] bulletemitters = FM.CT.Weapons[i_153_];
					if (bulletemitters != null)
					{
						is = new short[bulletemitters.length];
						for (int i_154_ = 0; i_154_ < bulletemitters.length; i_154_++)
						{
							int i_155_ = bulletemitters[i_154_].countBullets();
							if (i_155_ < 0)
								is[i_154_] = (short)-1;
							else
								is[i_154_] = (short)i_155_;
						}
					}
				}
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted()
				{
					public void unLocking()
					{
						Actor actor_157_ = (Actor)((NetObj)objects().get(0)).superObj();
						if (actor_157_ instanceof NetGunner)
						{
							if (((NetGunner)actor_157_).netCockpitMsg == this)
								((NetGunner)actor_157_).netCockpitValid = true;
						}
						else if (netCockpitMsg == this)
							netCockpitValid = true;
					}
				};
				if (actor instanceof NetGunner)
				{
					if (!((NetGunner)actor).netCockpitValid)
						((NetGunner)actor).netCockpitMsg = netmsgguaranted;
				}
				else if (!netCockpitValid)
					netCockpitMsg = netmsgguaranted;
				netmsgguaranted.writeByte(13);
				netmsgguaranted.writeNetObj(actor.net);
				netmsgguaranted.writeByte(i);
				if (is != null)
				{
					for (int i_158_ = 0; i_158_ < is.length; i_158_++)
						netmsgguaranted.writeShort(is[i_158_]);
				}
				net.postTo(netchannel, netmsgguaranted);
			}
		}
	}
	
	private boolean netCockpitCheckDrivers()
	{
		if (netCockpitDrivers != null)
			return true;
		Object object = Property.value(this.getClass(), "cockpitClass");
		if (object == null)
			return false;
		if (object instanceof Class)
			netCockpitDrivers = new Actor[1];
		else
		{
			Class[] var_classes = (Class[])object;
			netCockpitDrivers = new Actor[var_classes.length];
		}
		return true;
	}
	
	public Actor netCockpitGetDriver(int i)
	{
		if (!netCockpitCheckDrivers())
			return null;
		if (i < 0 || i >= netCockpitDrivers.length)
			return null;
		return netCockpitDrivers[i];
	}
	
	private void netCockpitDriverSet(Actor actor, int i)
	{
		if (netCockpitCheckDrivers())
		{
			NetUser netuser = netUser();
			if (actor instanceof NetGunner)
				netuser = ((NetGunner)actor).getUser();
			if (netuser == null)
				netuser = (NetUser)NetEnv.host();
			for (int i_159_ = 0; i_159_ < netCockpitDrivers.length; i_159_++)
			{
				if (netCockpitDrivers[i_159_] == actor)
				{
					netCockpitDrivers[i_159_] = null;
					netuser.tryPreparePilotDefaultSkin((Aircraft)this, netCockpitAstatePilotIndx(i_159_));
				}
			}
			netCockpitDrivers[i] = actor;
			netuser.tryPreparePilotSkin(this, netCockpitAstatePilotIndx(i));
		}
	}
	
	public void netCockpitDriverRequest(Actor actor, int i)
	{
		if (netCockpitCheckDrivers() && (i >= 0 && i < netCockpitDrivers.length))
		{
			if (net.isMaster())
			{
				if (netCockpitDrivers[i] == null)
				{
					netCockpitDriverSet(actor, i);
					Main3D.cur3D().aircraftHotKeys.netSwitchToCockpit(i);
				}
			}
			else
			{
				try
				{
					NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
					netmsgguaranted.writeByte(14);
					netmsgguaranted.writeNetObj(actor.net);
					netmsgguaranted.writeByte(i);
					net.postTo(net.masterChannel(), netmsgguaranted);
				}
				catch (Exception exception)
				{
					printDebug(exception);
				}
			}
		}
	}
	
	private boolean netGetCockpitDriver(NetMsgInput netmsginput, boolean bool, boolean bool_160_) throws IOException
	{
		NetObj netobj = netmsginput.readNetObj();
		if (netobj == null)
			return false;
		Actor actor = (Actor)netobj.superObj();
		int i = netmsginput.readUnsignedByte();
		if (!netCockpitCheckDrivers())
			return false;
		if (i < 0 || i >= netCockpitDrivers.length)
			return true;
		if (bool)
		{
			if (netCockpitDrivers[i] != null)
				return true;
			netCockpitDriverSet(actor, i);
		}
		else if (bool_160_)
		{
			try
			{
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(14);
				netmsgguaranted.writeNetObj(actor.net);
				netmsgguaranted.writeByte(i);
				net.postTo(net.masterChannel(), netmsgguaranted);
			}
			catch (Exception exception)
			{
				printDebug(exception);
			}
		}
		else
		{
			netCockpitDriverSet(actor, i);
			if (actor.net.isMaster())
				Main3D.cur3D().aircraftHotKeys.netSwitchToCockpit(i);
		}
		if (net.countMirrors() > 0)
		{
			try
			{
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(14);
				netmsgguaranted.writeNetObj(actor.net);
				netmsgguaranted.writeByte(i);
				net.post(netmsgguaranted);
			}
			catch (Exception exception)
			{
				printDebug(exception);
			}
		}
		return true;
	}
	
	boolean netGetGMsg(NetMsgInput netmsginput, boolean bool) throws IOException
	{
		int i = netmsginput.readUnsignedByte();
		
		// TODO: Added by |ZUTI|
		// -----------------------------------------------------------------
		if (ZutiSupportMethods_Air.processNetAircraftMirroredMessage((Aircraft)this, i, netmsginput))
		{
			return true;
		}
		// -----------------------------------------------------------------
		
		boolean bool_161_ = (i & 0x80) == 128;
		i &= ~0x80;
		switch (i)
		{
			case 0 :
				FM.AS.netUpdate(bool, bool_161_, netmsginput);
				return true;
			case 1 :
				return netGetUpdateWayPoint(netmsginput, bool, bool_161_);
			case 2 :
				return netGetWeaponsBitStates(netmsginput, bool, bool_161_);
			case 3 :
				setGunPodsOn(true);
				return true;
			case 4 :
				setGunPodsOn(false);
				return true;
			case 5 :
				FM.CT.dropFuelTanks();
				return true;
			case 6 :
				return netGetHits(netmsginput, bool, bool_161_);
			case 7 :
				return netGetHitProp(netmsginput, bool, bool_161_);
			case 8 :
				return netGetCut(netmsginput, bool, bool_161_);
			case 9 :
				return netGetExplode(netmsginput, bool, bool_161_);
			case 10 :
				return netGetDead(netmsginput, bool, bool_161_);
			case 11 :
				return netGetFirstUpdate(netmsginput);
			case 12 :
				return netGetCockpitEnter(netmsginput, bool, bool_161_);
			case 13 :
				return netGetCockpitAuto(netmsginput, bool, bool_161_);
			case 14 :
				return netGetCockpitDriver(netmsginput, bool, bool_161_);
			case 15: // '\017'
	            FM.CT.dropExternalStores(false);
	            return true;
			default :
				return false;
		}
	}
	
	protected void sendMsgSndShot(Shot shot)
	{
		int i = shot.mass > 0.05F ? 1 : 0;
		Actor._tmpPoint.set(pos.getAbsPoint());
		Actor._tmpPoint.sub(shot.p);
		int i_162_ = (int)(Actor._tmpPoint.x / 0.25) & 0xfe;
		int i_163_ = (int)(Actor._tmpPoint.y / 0.25) & 0xfe;
		i &= 0x3;
		try
		{
			NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
			netmsgfiltered.writeByte(i_162_ | i >> 1);
			netmsgfiltered.writeByte(i_163_ | i & 0x1);
			net.postTo(Time.current(), net.masterChannel(), netmsgfiltered);
		}
		catch (Exception exception)
		{
			/* empty */
		}
	}
	
	protected void msgSndShot(float f, double d, double d_164_, double d_165_)
	{
	/* empty */
	}
	
	public void makeMirrorCarrierRelPos()
	{
		if (net != null && !net.isMaster())
			((Mirror)net).makeFirstUnderDeck();
	}
	
	public boolean isMirrorUnderDeck()
	{
		if (net == null || net.isMaster())
			return false;
		return ((Mirror)net).bUnderDeck;
	}
	
	public void destroy()
	{
		if (!isDestroyed())
		{
			if (isNetMaster() && fmTrack != null && !fmTrack.isDestroyed())
				fmTrack.destroy();
			fmTrack = null;
			
			// TODO: Added by |ZUTI| - eject all connected gunners
			// ------------------------------------------------------------------
			if (isNetMaster())
			{
				ZutiSupportMethods_Multicrew.ejectGunnersForAircraft(this.name());
			}
			// ------------------------------------------------------------------
			super.destroy();
			damagers.clear();
		}
	}
	
	public void createNetObject(NetChannel netchannel, int i)
	{
		if (netchannel == null)
			net = new Master(this);
		else
			net = new Mirror(this, netchannel, i);
	}
	
	public void restoreLinksInCoopWing()
	{
		if (Main.cur().netServerParams != null && Main.cur().netServerParams.isCoop())
		{
			Wing wing = getWing();
			Aircraft[] aircrafts = wing.airc;
			int i;
			for (i = 0; i < aircrafts.length; i++)
			{
				if (Actor.isValid(aircrafts[i]))
					break;
			}
			if (i != aircrafts.length)
			{
				aircrafts[i].FM.Leader = null;
				for (int i_166_ = i + 1; i_166_ < aircrafts.length; i_166_++)
				{
					if (Actor.isValid(aircrafts[i_166_]))
					{
						aircrafts[i].FM.Wingman = aircrafts[i_166_].FM;
						aircrafts[i_166_].FM.Leader = aircrafts[i].FM;
						i = i_166_;
					}
				}
			}
		}
	}
	
	private static void netSpawnCommon(NetMsgInput netmsginput, ActorSpawnArg actorspawnarg) throws Exception
	{
		actorspawnarg.point = new Point3d((double)netmsginput.readFloat(), (double)netmsginput.readFloat(), (double)netmsginput.readFloat());
		actorspawnarg.orient = new Orient(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
		actorspawnarg.speed = new Vector3d((double)netmsginput.readFloat(), (double)netmsginput.readFloat(), (double)netmsginput.readFloat());
	}
	
	private static void netSpawnCommon(NetMsgInput netmsginput, ActorSpawnArg actorspawnarg, NetAircraft netaircraft) throws Exception
	{
		Mirror mirror = (Mirror)netaircraft.net;
		mirror.netFirstUpdate((float)actorspawnarg.point.x, (float)actorspawnarg.point.y, (float)actorspawnarg.point.z, actorspawnarg.orient.azimut(), actorspawnarg.orient.tangage(), actorspawnarg.orient.kren(), (float)actorspawnarg.speed.x,
				(float)actorspawnarg.speed.y, (float)actorspawnarg.speed.z);
		int i = 0;
		for (int i_167_ = 0; i_167_ < 44; i_167_++)
		{
			int i_168_;
			if ((i_167_ & 0x1) == 0)
			{
				i = netmsginput.readUnsignedByte();
				i_168_ = i & 0xff;
			}
			else
				i_168_ = i >> 8 & 0xff;
			while (i_168_-- > 0)
			{
				NetAircraft netaircraft_169_ = netaircraft;
				StringBuffer stringbuffer = new StringBuffer();
				if (netaircraft != null)
				{
					/* empty */
				}
				netaircraft_169_.nextDMGLevel(stringbuffer.append(partNames()[i_167_]).append("_D0").toString(), 0, null);
			}
		}
		long l = netmsginput.readLong();
		if (l != netaircraft.FM.Operate)
		{
			int i_170_ = 0;
			long l_171_ = 1L;
			while (i_170_ < 44)
			{
				if ((l & l_171_) == 0L && (netaircraft.FM.Operate & l_171_) != 0L)
				{
					NetAircraft netaircraft_172_ = netaircraft;
					StringBuffer stringbuffer = new StringBuffer();
					if (netaircraft != null)
					{
						/* empty */
					}
					netaircraft_172_.nextCUTLevel(stringbuffer.append(partNames()[i_170_]).append("_D0").toString(), 0, null);
				}
				i_170_++;
				l_171_ <<= 1;
			}
		}
		int i_173_ = netmsginput.readByte();
		for (int i_174_ = 0; i_174_ < 4; i_174_++)
		{
			if ((i_173_ & 1 << i_174_) != 0)
				netaircraft.hitProp(i_174_, 0, null);
		}
		if ((i_173_ & 0x10) != 0)
			netaircraft.setGunPodsOn(false);
		byte[] is = netaircraft.getWeaponsBitStatesBuf(null);
		if (is != null)
		{
			for (int i_175_ = 0; i_175_ < is.length; i_175_++)
				is[i_175_] = (byte)netmsginput.readUnsignedByte();
			netaircraft.setWeaponsBitStates(is);
		}
		// TODO: Added by |ZUTI|: added try/catch block here to catch exception on server side, if player takes on a plane that is not available
		//---------------------------------------------------
		try
		{
			netaircraft.FM.AS.netFirstUpdate(netmsginput);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		//---------------------------------------------------
	}
	
	private void netReplicateCommon(NetChannel netchannel, NetMsgGuaranted netmsgguaranted) throws IOException
	{
		netmsgguaranted.writeFloat(FM.M.fuel / FM.M.maxFuel);
		netmsgguaranted.writeBoolean(bPaintShemeNumberOn);
		netmsgguaranted.write255(((AircraftNet)net).netName);
		netmsgguaranted.write255(thisWeaponsName);
		netmsgguaranted.writeNetObj(((AircraftNet)net).netUser);
	}
	
	private void netReplicateFirstUpdate(NetChannel netchannel, NetMsgGuaranted netmsgguaranted) throws IOException
	{
		Point3d point3d = pos.getAbsPoint();
		netmsgguaranted.writeFloat((float)point3d.x);
		netmsgguaranted.writeFloat((float)point3d.y);
		netmsgguaranted.writeFloat((float)point3d.z);
		Orient orient = pos.getAbsOrient();
		netmsgguaranted.writeFloat(orient.azimut());
		netmsgguaranted.writeFloat(orient.tangage());
		netmsgguaranted.writeFloat(orient.kren());
		Vector3d vector3d = new Vector3d();
		getSpeed(vector3d);
		netmsgguaranted.writeFloat((float)vector3d.x);
		netmsgguaranted.writeFloat((float)vector3d.y);
		netmsgguaranted.writeFloat((float)vector3d.z);
		int i = 0;
		int i_176_;
		for (i_176_ = 0; i_176_ < 44; i_176_++)
		{
			if ((i_176_ & 0x1) == 0)
				i = curDMGLevel(i_176_) & 0xff;
			else
			{
				i |= (curDMGLevel(i_176_) & 0xff) << 8;
				netmsgguaranted.writeByte(i);
			}
		}
		if ((i_176_ & 0x1) == 1)
			netmsgguaranted.writeByte(i);
		netmsgguaranted.writeLong(FM.Operate);
		int i_177_ = (curDMGProp(0) | curDMGProp(1) << 1 | curDMGProp(2) << 2 | curDMGProp(3) << 3);
		if (!isGunPodsOn())
			i_177_ |= 0x10;
		netmsgguaranted.writeByte(i_177_);
		byte[] is = getWeaponsBitStates(null);
		if (is != null)
			netmsgguaranted.write(is);
		FM.AS.netReplicate(netmsgguaranted);
	}
	
	private NetMsgSpawn netReplicateCoop(NetChannel netchannel) throws IOException
	{
		NetMsgSpawn netmsgspawn = super.netReplicate(netchannel);
		netmsgspawn.writeByte(1);
		netReplicateCommon(netchannel, netmsgspawn);
		netReplicateFirstUpdate(netchannel, netmsgspawn);
		return netmsgspawn;
	}
	
	private NetMsgSpawn _netReplicate(NetChannel netchannel) throws IOException
	{
		NetMsgSpawn netmsgspawn = super.netReplicate(netchannel);
		netmsgspawn.writeByte(0);
		netmsgspawn.writeByte(getArmy());
		netReplicateCommon(netchannel, netmsgspawn);
		netReplicateFirstUpdate(netchannel, netmsgspawn);
		
		return netmsgspawn;
	}
	
	public NetMsgSpawn netReplicate(NetChannel netchannel) throws IOException
	{
		if (Main.cur().netServerParams == null)
			return null;
		if (netchannel.isMirrored(net))
			return null;
		NetMsgSpawn netmsgspawn;
		if (isCoopPlane())
			netmsgspawn = netReplicateCoop(netchannel);
		else
			netmsgspawn = _netReplicate(netchannel);
		if (World.getPlayerAircraft() == this && netchannel instanceof NetChannelOutStream)
		{
			if (fmTrack() == null)
			{
				if (isNetMaster())
					new MsgAction(true, this)
					{
						public void doAction(Object object_180_)
						{
							new FlightModelTrack((Aircraft)object_180_);
						}
					};
			}
			else
				MsgNet.postRealNewChannel(fmTrack(), netchannel);
		}
		
		return netmsgspawn;
	}
	
	public NetAircraft()
	{
		bCoopPlane = loadingCoopPlane;
	}
	
	public void setDamagerExclude(Actor actor)
	{
		damagerExclude = actor;
		if (damager_ == actor)
			damager_ = null;
	}
	
	public void setDamager(Actor actor)
	{
		setDamager(actor, 1);
	}
	
	public void setDamager(Actor actor, int i)
	{
		if (Actor.isValid(actor) && this != actor && i > 0)
		{
			if (i > 4)
				i = 4;
			damager_ = null;
			int i_181_ = damagers.size();
			for (int i_182_ = 0; i_182_ < i_181_; i_182_++)
			{
				DamagerItem damageritem = (DamagerItem)damagers.get(i_182_);
				if (damageritem.damager == actor)
				{
					damageritem.damage += i;
					damageritem.lastTime = Time.current();
					return;
				}
			}
			damagers.add(new DamagerItem(actor, i));
			if (World.cur().isDebugFM())
			{
				Aircraft.debugprintln(this, "Printing Registered Damagers: *****");
				for (int i_183_ = 0; i_183_ < i_181_; i_183_++)
				{
					DamagerItem damageritem = (DamagerItem)damagers.get(i_183_);
					if (Actor.isValid(damageritem.damager))
						Aircraft.debugprintln(damageritem.damager, ("inflicted " + damageritem.damage + " puntos.."));
				}
			}
		}
	}
	
	public Actor getDamager()
	{
		if (Actor.isValid(damager_))
			return damager_;
		damager_ = null;
		long l = 0L;
		Actor actor = null;
		long l_184_ = 0L;
		Actor actor_185_ = null;
		Actor actor_186_ = null;
		int i = damagers.size();
		for (int i_187_ = 0; i_187_ < i; i_187_++)
		{
			DamagerItem damageritem = (DamagerItem)damagers.get(i_187_);
			if (damageritem.damager != damagerExclude && Actor.isValid(damageritem.damager))
			{
				if (damageritem.damager instanceof Aircraft)
				{
					if (damageritem.lastTime > l_184_)
					{
						l_184_ = damageritem.lastTime;
						actor_185_ = damageritem.damager;
					}
				}
				else if (damageritem.damager == Engine.actorLand())
					actor_186_ = damageritem.damager;
				else if (damageritem.lastTime > l)
				{
					l = damageritem.lastTime;
					actor = damageritem.damager;
				}
			}
		}
		if (actor_185_ != null)
			damager_ = actor_185_;
		else if (actor != null)
			damager_ = actor;
		else if (actor_186_ != null)
			damager_ = actor_186_;
		return damager_;
	}
	
	public boolean isDamagerExclusive()
	{
		int i = 0;
		for (int i_188_ = 0; i_188_ < damagers.size(); i_188_++)
		{
			if (damagerExclude != damagers.get(i_188_))
				i++;
		}
		return i == 1;
	}
	
	protected static void printDebug(Exception exception)
	{
		System.out.println(exception.getMessage());
		exception.printStackTrace();
	}
	
	public int getPilotsCount()
	{
		return FM.crew;
	}
	
	static Class class$ZutiNetAircraft(String string)
	{
		Class var_class;
		try
		{
			var_class = Class.forName(string);
		}
		catch (ClassNotFoundException classnotfoundexception)
		{
			throw new NoClassDefFoundError(classnotfoundexception.getMessage());
		}
		return var_class;
	}
	
	public void replicateDropExternalStores()
    {
        if(isNet() && net.countMirrors() != 0)
            try
            {
                com.maddox.rts.NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(15);
                net.post(netmsgguaranted);
            }
            catch(java.lang.Exception exception)
            {
                com.maddox.il2.objects.air.NetAircraft.printDebug(exception);
            }
    }
	
	public static boolean isOnCarrierDeck(com.maddox.il2.ai.AirportCarrier airportcarrier, com.maddox.il2.engine.Loc loc)
    {
        return com.maddox.il2.objects.air.NetAircraft.isOnCarrierDeck(airportcarrier, loc.getPoint());
    }

    public static boolean isOnCarrierDeck(com.maddox.il2.ai.AirportCarrier airportcarrier, com.maddox.JGP.Point3d point3d)
    {
        com.maddox.JGP.Point3d point3d1 = new Point3d();
        com.maddox.JGP.Point3d point3d2 = new Point3d();
        point3d1.set(point3d);
        point3d2.set(point3d);
        point3d1.z = com.maddox.il2.engine.Engine.cur.land.HQ(point3d.x, point3d.y);
        point3d2.z = point3d1.z + 40D;
        com.maddox.il2.engine.Actor actor = com.maddox.il2.engine.Engine.collideEnv().getLine(point3d1, point3d2, false, clipFilter, pship);
        return airportcarrier.ship() == actor;
    }
	
	// TODO: |ZUTI| methods
	// ------------------------------------------------------------------
	public void restoreLinksInDogWing()
	{
		if (com.maddox.il2.game.Main.cur().netServerParams == null)
			return;
		com.maddox.il2.ai.Wing wing = getWing();
		com.maddox.il2.objects.air.Aircraft aaircraft[] = wing.airc;
		int i;
		for (i = 0; i < aaircraft.length && !com.maddox.il2.engine.Actor.isValid(aaircraft[i]); i++);
		if (i == aaircraft.length)
			return;
		aaircraft[i].FM.Leader = null;
		for (int j = i + 1; j < aaircraft.length; j++)
			if (com.maddox.il2.engine.Actor.isValid(aaircraft[j]))
			{
				aaircraft[i].FM.Wingman = aaircraft[j].FM;
				aaircraft[j].FM.Leader = aaircraft[i].FM;
				i = j;
			}
		
	}
	// -------------------------------------------------------------------------
}