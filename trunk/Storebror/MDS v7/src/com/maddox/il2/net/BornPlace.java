/*4.10.1 class*/
package com.maddox.il2.net;

import java.util.ArrayList;
import java.util.Map;

import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.AirportCarrier;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Point_Stay;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorFilter;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.Ship;
import com.maddox.il2.objects.ships.Ship.RwyTranspWide;
import com.maddox.il2.objects.ships.TestRunway;

public class BornPlace
{
	public Point2d place = new Point2d();
	public float r;
	public int army;
	public boolean bParachute = true;
	public ArrayList airNames;
	private static Vector3d v1 = new Vector3d();
	private static Point3d corn = new Point3d();
	private static Point3d corn1 = new Point3d();
	private static Point3d pship = new Point3d();
	static ClipFilter clipFilter = new ClipFilter();
	public int tmpForBrief;
	
	static class ClipFilter implements ActorFilter
	{
		public boolean isUse(Actor actor, double d)
		{
			return actor instanceof BigshipGeneric;
		}
	}
		
	public Loc getAircraftPlace(Aircraft paramAircraft, int stayPlace)
	{
		Loc loc = null;
				
		//TODO: Added by |ZUTI|: Support for Stand Alone born places and airspawn on the full deck
		//------------------------------------------------------------------	
				
		//System.out.println("StayPlace=" + stayPlace + " vs stayHold=" + World.cur().airdrome.stayHold.length);
		//System.out.println("StayPlace=" + stayPlace + " vs stay=" + World.cur().airdrome.stay.length);
		
		if( this.zutiIsStandAloneBornPlace )
		{
			Point_Stay ps = ZutiSupportMethods_Net.getFreeSpawnPointForStandAloneBornPlace(stayPlace);
			if( ps == null )
			{
				loc = new Loc(place.x, place.y, 1000.0, 0.0F, 0.0F, 0.0F);
				//System.out.println("BornPlace - no free spawn places found - airstarting!");
			}
			else
			{
				double height = Landscape.HQ(ps.x, ps.y);
				//System.out.println("BornPlace - calculated height for spawn location is: " + height + "m" + ", gear height: " + aircraft.FM.Gears.H );
				if( ps.zutiLocation != null )
				{
					loc = ps.zutiLocation;
					//System.out.println("Pitch: " + ps.zutiLocation.getOrient().getPitch());
					loc.set( new Point3d(ps.zutiLocation.getPoint().x, ps.zutiLocation.getPoint().y, (height + paramAircraft.FM.Gears.H + 0.3F) ) );
				}
				else
					loc = new Loc( (double)ps.x, (double)ps.y, height + paramAircraft.FM.Gears.H, 0.0F, 0.0F, 0.0F );
				//System.out.println("BornPlace - found free spawn point for stand alone born place at >" + loc + "<");
			}
			return loc;
		}
		//------------------------------------------------------------------

		//TODO: Altered by |ZUTI|: This rule applies only if home base is NOT on the carrier!
		if( (stayPlace < 0 || stayPlace > World.cur().airdrome.stay.length) && !this.zutiAlreadyAssigned )
		{
			double d1 = World.land().HQ(this.place.x, this.place.y);
			loc = new Loc(this.place.x, this.place.y, d1, 0.0F, 0.0F, 0.0F);
		}
		else
		{
			Point_Stay pointStay;
			AirportCarrier nearestAirport;
			if( this.zutiAlreadyAssigned || stayPlace >= World.cur().airdrome.stayHold.length )
			{
				if( this.zutiAlreadyAssigned )
				{
					//Location should be carrier location = home base location. Point stay is selected based on deck occupation
					loc = new Loc(this.place.x, this.place.y, 0.0D, 0.0F, 0.0F, 0.0F);
				}
				else
				{
					//Location should be point stay location
					pointStay = World.cur().airdrome.stay[stayPlace][0];
					loc = new Loc(pointStay.x, pointStay.y, 0.0D, 0.0F, 0.0F, 0.0F);
				}
				
				if ((!World.cur().diffCur.Takeoff_N_Landing) || (this.zutiAirspawnOnly))
				{
					loc.add( (Tuple3d)new Point3d(200.0D, 200.0D, 0.0D) );
					return loc;
				}
				
				nearestAirport = (AirportCarrier) Airport.nearest(loc.getPoint(), -1, 4);
				if (nearestAirport != null)
				{
					corn.set(loc.getPoint());
					corn1.set(loc.getPoint());
					corn.z = Engine.cur.land.HQ(loc.getPoint().x, loc.getPoint().y);
					corn.z += 40.0D;
					Actor localActor = Engine.collideEnv().getLine(corn, corn1, false, clipFilter, pship);
					if (
							nearestAirport.ship() != localActor && 
							!(nearestAirport.ship() instanceof RwyTranspWide) && 
							!(nearestAirport.ship() instanceof Ship.RwyTranspSqr) )
					{
						nearestAirport = null;
					}
				}

				if (nearestAirport != null)
				{
					if (Mission.isDogfight())
					{
						if( nearestAirport.ship() instanceof TestRunway )
						{
							nearestAirport.getTakeoff(paramAircraft, loc);
							return loc;
						}

						if ((paramAircraft == World.getPlayerAircraft()) && (!Main.cur().netServerParams.isMaster()))
							return nearestAirport.setClientTakeOff(loc.getPoint(), paramAircraft);
						
						nearestAirport.setTakeoff(loc.getPoint(), new Aircraft[] { paramAircraft });
						return paramAircraft.pos.getAbs();
					}

					nearestAirport.getTakeoff(paramAircraft, loc);
				}
				else
				{
					pointStay = World.cur().airdrome.stay[stayPlace][0];
					loc = new Loc(((Point_Stay) pointStay).x, ((Point_Stay) pointStay).y, 1000.0D, 0.0F, 0.0F, 0.0F);
				}
			}
			else
			{
				Point_Stay[][] point_stays = World.cur().airdrome.stay;
				Point_Stay point_stay = point_stays[stayPlace][point_stays[stayPlace].length - 1];
				World.land();
				double d2 = Landscape.HQ(((Point_Stay) point_stay).x, ((Point_Stay) point_stay).y);
				loc = new Loc(((Point_Stay) point_stay).x, ((Point_Stay) point_stay).y, d2, 0.0F, 0.0F, 0.0F);
				int i = point_stays[stayPlace].length - 2;
				if (i >= 0)
				{
					Point3d localPoint3d1 = new Point3d(((Point_Stay) point_stay).x, ((Point_Stay) point_stay).y, 0.0D);
					Point3d localPoint3d2 = new Point3d(point_stays[stayPlace][i].x, point_stays[stayPlace][i].y, 0.0D);
					Vector3d localVector3d = new Vector3d();
					localVector3d.sub(localPoint3d2, localPoint3d1);
					localVector3d.normalize();
					Orient localOrient = new Orient();
					localOrient.setAT0(localVector3d);
					if ((!World.cur().diffCur.Takeoff_N_Landing) || (this.zutiAirspawnOnly))
					{
						loc.set(localOrient);
						return loc;
					}
					loc.getPoint().z += paramAircraft.FM.Gears.H;
					Engine.land().N(loc.getPoint().x, loc.getPoint().y, v1);
					localOrient.orient(v1);
					localOrient.increment(0.0F, paramAircraft.FM.Gears.Pitch, 0.0F);
					loc.set(localOrient);
				}
			}
		}
		return loc;
	}
	
	public BornPlace(double d, double d_2_, int i, float f)
	{
		place.set(d, d_2_);
		army = i;
		r = f;
		
		zutiOriginalX = place.x;
		zutiOriginalY = place.y;
	}
	
	// TODO: |ZUTI| variables
	// -----------------------------------------------------------------------
	public double zutiOriginalX = 0.0D;
	public double zutiOriginalY = 0.0D;
	public boolean zutiStaticPositionOnly = false;
	// Called from: BornPlace, BigShipGeneric, Front
	public ArrayList zutiBpStayPoints = null;
	// Called from: BigshipGeneric
	public boolean zutiAlreadyAssigned = false;
	// These three are called from Mission
	public int zutiSpawnHeight = 1000;
	public int zutiSpawnSpeed = 200;
	public int zutiSpawnOrient = 0;
	// Called from: Mission, Front
	public int zutiBpIndex = -1;
	// Called from: PlMisBorn, Front
	public String zutiBaseCapturedRedPlanes = "";
	public String zutiBaseCapturedBluePlanes = "";
	public boolean zutiCanThisHomeBaseBeCaptured = false;
	// Called from: BornPlace, Mission
	public int zutiRadarHeight_MIN = 0;// m
	public int zutiRadarHeight_MAX = 5000;// m
	public int zutiRadarRange = 50;// km
	// Called from: Mission,
	public int zutiMaxBasePilots = 0;
	// Called from: Mission, BornPlace, NetAircraft
	public boolean zutiAirspawnOnly = false;
	// Called from: Mission, NetAircraft, Front
	public ArrayList zutiAircraft = null;
	// Called from: Mission, NetAircraft, GUIAirArming, GUIBriefing
	public ArrayList zutiHomeBaseCountries = null;
	public ArrayList zutiHomeBaseCapturedRedCountries = null;
	public ArrayList zutiHomeBaseCapturedBlueCountries = null;
	// Called from: Mission, BornPlace, GUIAirArming
	public boolean zutiEnablePlaneLimits = false;
	public boolean zutiDecreasingNumberOfPlanes = false;
	public boolean zutiIncludeStaticPlanes = false;
	// Called from: Mission
	public boolean zutiDisableSpawning = false;
	public boolean zutiEnableFriction = false;
	public double zutiFriction = 3.8D;
	public boolean zutiDisableRendering = false;
	// Called from: Mission
	public ArrayList zutiCapturedAc_Red = null;
	public ArrayList zutiCapturedAc_Blue = null;
	
	// RRR options
	// Called from: Mission, RRR Timers
	public boolean zutiOverrideDefaultRRRSettings = false;
	public int zutiOneMgCannonRearmSecond = 10;
	public int zutiOneBombFTankTorpedoeRearmSeconds = 25;
	public int zutiOneRocketRearmSeconds = 20;
	public boolean zutiRearmOnlyIfAmmoBoxesExist = false;
	
	public int zutiLoadoutChangePenaltySeconds = 30;
	public boolean zutiOnlyHomeBaseSpecificLoadouts = true;
	
	public int zutiGallonsLitersPerSecond = 3;
	public boolean zutiRefuelOnlyIfFuelTanksExist = false;
	
	public int zutiEngineRepairSeconds = 90;
	public int zutiOneControlCableRepairSeconds = 15;
	public int zutiFlapsRepairSeconds = 30;
	public int zutiOneWeaponRepairSeconds = 3;
	public int zutiCockpitRepairSeconds = 30;
	public int zutiOneFuelOilTankRepairSeconds = 20;
	public boolean zutiRepairOnlyIfWorkshopExist = false;
	
	public int zutiCapturingRequiredParatroopers = 100;
	public int zutiParatroopersInsideHomeBaseArea = 0;
	
	public boolean zutiEnableQueue = false;
	public int zutiDeckClearTimeout = 30;
	public boolean zutiAirspawnIfQueueFull = false;
	public boolean zutiPilotInVulnerableWhileOnTheDeck = false;
	public boolean zutiSpawnAcWithFoldedWings = true;
	
	public boolean zutiIsStandAloneBornPlace = false;
	
	public boolean zutiEnableResourcesManagement = false;
	public Map objectsMap = null;
	public long zutiBulletsSupply = 0;
	public int[] zutiBombsSupply = new int[]{0, 0, 0, 0, 0, 0};
	public long zutiRocketsSupply = 0;
	public long zutiFuelSupply = 0;
	public long zutiEnginesSupply = 0;
	public long zutiRepairKitsSupply = 0;
	
	public int zutiColor = -1;
	public boolean zutiCaptureOnlyIfNoChiefPresent = false;
	// -----------------------------------------------------------------------
}